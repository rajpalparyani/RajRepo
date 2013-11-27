/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * AndroidCitizenGallery.java
 *
 */
package com.telenav.ui.citizen.android;

import android.app.Activity;
import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.core.INativeUiComponent;
import com.telenav.tnui.core.android.AndroidUiMethodHandler;
import com.telenav.ui.citizen.CitizenGallery;
import com.telenav.ui.citizen.CitizenGallery.ICitizenGalleryAdapter;
import com.telenav.util.PrimitiveTypeCache;

/**
 * @author xinrongl (xinrongl@telenav.com)
 * @date Apr 19, 2011
 */

public class AndroidCitizenGallery extends FrameLayout implements INativeUiComponent
{
    private static final int THRESHOLD_VELOCITY         = 2000;
    private static final int MIN_FLING_DURATION         = 30;
        
    private static final int VIEW_PADDING_WIDTH         = 0;  
    private static final int ANIMATION_DURATION         = 150;  
    private static final float SNAP_BORDER_RATIO        = 0.5f;  
    
    protected Context context;
    
    private CitizenGallery tnComponent;
    private ICitizenGalleryAdapter adapter;
    
    private int galleryWidth = 0;  
    private boolean isTouched = false;  
    private boolean isDragging = false;  
    private float currentOffset = 0.0f;  
    private long scrollTimestamp = 0;  

    private int flingDirection = 0;  

    private GestureDetector gestureDetector;  

    private boolean isScrollingLeftAndRight;
    private boolean isFirstTimeScrolling;
    
    private float mMaxVelocity;
    private long mFlingDuration;
    
    private GalleryView galleryView;

    public AndroidCitizenGallery(Context context, CitizenGallery tnComponent)
    {
        super(context);
        
        this.context = context;
        this.tnComponent = tnComponent;
        

        galleryView = new GalleryView(this, true);
        
        gestureDetector = new GestureDetector(new GalleryGestureDetector());  
    }

    public void requestNativePaint()
    {
        this.postInvalidate();
    }

    public void setNativeVisible(boolean isVisible)
    {
        this.setVisibility(isVisible ? VISIBLE : GONE);
    }

    public boolean isNativeVisible()
    {
        return this.getVisibility() == VISIBLE;
    }

    public void setNativeFocusable(boolean isFocusable)
    {
        this.setFocusable(isFocusable);
    }

    public boolean isNativeFocusable()
    {
        return this.isFocusable();
    }

    public boolean requestNativeFocus()
    {
        return this.requestFocus();
    }

    public int getNativeWidth()
    {
        return this.getWidth();
    }

    public int getNativeHeight()
    {
        return this.getHeight();
    }

    public int getNativeX()
    {
        return this.getLeft();
    }

    public int getNativeY()
    {
        return this.getTop();
    }

    public Object callUiMethod(int eventMethod, Object[] args)
    {
        Object obj = AndroidUiMethodHandler.callUiMethod(tnComponent, this, eventMethod, args);

        if (!AndroidUiMethodHandler.NO_HANDLED.equals(obj))
            return obj;
        
        switch(eventMethod)
        {
            case CitizenGallery.SET_GALLERY_ADAPTER:
            {
                ICitizenGalleryAdapter adapter = (ICitizenGalleryAdapter)args[0];
                setAdapter(adapter);
                break;
            }
            case CitizenGallery.MOVE_TO_PREVIOUS:
            {
                this.movePrevious();
                break;
            }
            case CitizenGallery.MOVE_TO_NEXT:
            {
                this.moveNext();
                break;
            }
            case CitizenGallery.SET_GALLERY_INDEX:
            {
                galleryView.setGalleryPosition((Integer)args[0]);
                break;
            }
            case CitizenGallery.GET_GALLERY_INDEX:
            {
                int currentPosition = galleryView.getGalleryPosition();
                return PrimitiveTypeCache.valueOf(currentPosition);
            }
            case CitizenGallery.ON_TOUCH_EVENT:
            {
                MotionEvent event = (MotionEvent)args[0];
                boolean ret = this.onGalleryTouchEvent(event);
                return PrimitiveTypeCache.valueOf(ret);
            }
            default:
            {
                break;
            }
        }
        
        return null;
    }

    public AbstractTnComponent getTnUiComponent()
    {
        return this.tnComponent;
    }
    
    protected void onLayout(boolean changed, int left, int top, int right, int bottom)  
    {  
        super.onLayout(changed, left, top, right, bottom);  

        // Calculate our view width  
        galleryWidth = right - left;  
   
        if (changed == true)  
        {  
            galleryView.setOffset(0);
        }  
    }  

    public boolean onGalleryTouchEvent(MotionEvent event)  
    {  
        if (event.getAction() == MotionEvent.ACTION_DOWN)
        {
            this.isScrollingLeftAndRight = false;
            isFirstTimeScrolling = true;
            mMaxVelocity = 0;
            
            mFlingDuration = System.currentTimeMillis();
        }
        boolean consumed = gestureDetector.onTouchEvent(event);
        
        if (event.getAction() == MotionEvent.ACTION_UP)  
        {  
            mFlingDuration = ANIMATION_DURATION - (System.currentTimeMillis() - mFlingDuration);
            if (mFlingDuration <= 0)
                mFlingDuration = MIN_FLING_DURATION;
            
            if (isTouched || isDragging)  
            {  
                processScrollSnap();  

                processGesture();  
            }  
        }  

        return consumed;  
    }  
    
    private void setAdapter(ICitizenGalleryAdapter adapter)
    {
        this.adapter = adapter;

        galleryView.recycleAllViews();
    }

    private void movePrevious()  
    {  
        // Slide to previous view  
        flingDirection = 1;  
        
        processGesture();  
    }  

    private void moveNext()
    {  
        // Slide to next view  
        flingDirection = -1;  
        
        processGesture();  
    }  
    
    private void processGesture()  
    {  
        isTouched = false;  
        isDragging = false;  

        galleryView.switchView(flingDirection);
        

        // Reset fling state  
        flingDirection = 0;  
    }  

    private void processScrollSnap()  
    {  
        // Snap to next view if scrolled passed snap position  
        float rollEdgeWidth = galleryWidth * SNAP_BORDER_RATIO;  
        int rollOffset = galleryWidth - (int) rollEdgeWidth;          
        int minRollOffset = (int)(galleryWidth * SNAP_BORDER_RATIO / 2);        
        int currentOffset = galleryView.getOffset();
        
        if (currentOffset <= rollOffset * -1
                || currentOffset > rollOffset * -1 && currentOffset <= minRollOffset * -1 && this.mMaxVelocity > THRESHOLD_VELOCITY)  
        {  
            // Snap to previous view  
            flingDirection = 1;  
        }  

        if (currentOffset >= rollOffset
                || currentOffset >= minRollOffset && currentOffset < rollOffset && this.mMaxVelocity > THRESHOLD_VELOCITY)  
        {  
            // Snap to next view  
            flingDirection = -1;  
        }  
    }  
    
    private int getFirstPosition()  
    {  
        return 0;  
    }  

    private int getLastPosition()  
    {  
        return (getGalleryCount() == 0) ? 0 : getGalleryCount() - 1;  
    }  

    private int getPrevPosition(int relativePosition)  
    {  
        int prevPosition = relativePosition - 1;  

        if (prevPosition < getFirstPosition())  
        {  
            prevPosition = getFirstPosition() - 1;  
        }  

        return prevPosition;  
    }  

    private int getNextPosition(int relativePosition)  
    {  
        int nextPosition = relativePosition + 1;  

        if (nextPosition > getLastPosition())  
        {  
            nextPosition = getLastPosition() + 1;  
        }  

        return nextPosition;  
    }  

    private int getGalleryCount()  
    {  
        return (adapter == null) ? 0 : adapter.getGallerySize();  
    }  

    
    private class GalleryView implements Runnable
    {
        private GalleryItemView[] views;
        private FrameLayout parentLayout;
        
        private boolean needPrefetch;
        
        private int currentPosition;
        private int currentViewNumber;
        
        private int initialOffset;  
        private int targetOffset;  
        private int targetDistance;
        
        GalleryView(FrameLayout parentLayout, boolean needPrefetch)
        {
            this.parentLayout = parentLayout;
            this.needPrefetch = needPrefetch;
            
            views = new GalleryItemView[3];  
            views[0] = new GalleryItemView(parentLayout);  
            views[1] = new GalleryItemView(parentLayout);  
            views[2] = new GalleryItemView(parentLayout);
        }
        
        public void setGalleryPosition(int position)
        {
            this.currentPosition = position;
            this.currentViewNumber = 0;
        }
        
        public int getGalleryPosition()
        {
            return this.currentPosition;
        }
        
        public int getOffset()
        {
            return views[currentViewNumber].getCurrentOffset();
        }
        
        public void setOffset(int offset)
        {
            // Position views at correct starting offsets              
            int size = views.length;
            for (int i = 0; i < size; i ++)
            {
                int viewOffset = getViewOffset(i, currentViewNumber);            
                views[i].setOffset(viewOffset + offset, 0, currentViewNumber);                  
            }
            
            parentLayout.invalidate();
        }
        
        public void recycleAllViews()
        {
            // Load the initial views from adapter  
            if (needPrefetch)
            {
                views[0].recycleView(currentPosition);  
                views[1].recycleView(getNextPosition(currentPosition));  
                views[2].recycleView(getPrevPosition(currentPosition));
            }
            else
            {
                views[0].recycleView(currentPosition);  
                views[1].recycleView(-1);
                views[2].recycleView(-1); 
            }
        }
        
        public void switchView(int flingDirection)
        {
            if (needPrefetch)
            {
                switchViewPrefetch(flingDirection);
            }
            else
            {
                switchViewNonPrefetch(flingDirection);
            }

            // Run the slide animations for view transitions  
            prepareAnimation();  

            startAnimation();            
        }
        
        private void recycleInactiveViews(int flingDirection)
        {
            if (!needPrefetch)
            {                
                int newPosition = -1;
                int viewNumber = -1;
                if (flingDirection > 0)
                {
                    newPosition = getPrevPosition(this.currentPosition);
                    viewNumber = this.getPrevViewNumber(currentViewNumber);
                }
                else if (flingDirection < 0)
                {
                    newPosition = getNextPosition(this.currentPosition);
                    viewNumber = this.getNextViewNumber(currentViewNumber);
                }
                
                int size = views.length;
                for (int i = 0; i < size; i ++)
                {
                    if (i == viewNumber)
                    {
                        int position = views[i].getPosition();
                        if (newPosition != position)
                        {
                            views[i].recycleView(newPosition);
                        }
                    }
                }
            }
        }
        
        private void switchViewPrefetch(int flingDirection)
        {
            int newViewNumber = currentViewNumber;  
            int reloadViewNumber = 0;  
            int reloadPosition = 0;  

            if (flingDirection > 0)  
            {  
                if (currentPosition > getFirstPosition())  
                {  
                    // Determine previous view and outgoing view to recycle  
                    newViewNumber = getPrevViewNumber(currentViewNumber);  

                    currentPosition = getPrevPosition(currentPosition);  

                    reloadViewNumber = getNextViewNumber(currentViewNumber);   

                    reloadPosition = getPrevPosition(currentPosition);  
                    
                    tnComponent.notifyGalleryChanged(false);
                }  
            }  

            if (flingDirection < 0)  
            {  
                if (currentPosition < getLastPosition())  
                {  
                    // Determine the next view and outgoing view to recycle  
                    newViewNumber = getNextViewNumber(currentViewNumber);  

                    currentPosition = getNextPosition(currentPosition);  

                    reloadViewNumber = getPrevViewNumber(currentViewNumber);  

                    reloadPosition = getNextPosition(currentPosition);  
                    
                    tnComponent.notifyGalleryChanged(true);
                    
                }

                if (currentPosition+1 >= getLastPosition())
                {
                    tnComponent.notifyGalleryReachEnd();
                }
            }  

            if (newViewNumber != currentViewNumber)  
            {  
                currentViewNumber = newViewNumber;   

                // Reload outgoing view from adapter in new position  
                views[reloadViewNumber].recycleView(reloadPosition);  
            }  
        }
        
        private void switchViewNonPrefetch(int flingDirection)
        {
            int newViewNumber = currentViewNumber;  
            int reloadViewNumber = 0;  
            int reloadPosition = 0;  

            boolean needSwitch = false;
            
            if (flingDirection > 0)  
            {  
                if (currentPosition > getFirstPosition())  
                {  
                    // Determine previous view and outgoing view to recycle  
                    newViewNumber = getPrevViewNumber(currentViewNumber);  

                    currentPosition = getPrevPosition(currentPosition);  

                    reloadViewNumber = newViewNumber;   

                    reloadPosition = currentPosition;  
                    
                    tnComponent.notifyGalleryChanged(false);
                    
                    needSwitch = true;
                }  
                else if (currentPosition == getFirstPosition())
                {
                    // Determine previous view and outgoing view to recycle  
                    newViewNumber = getPrevViewNumber(currentViewNumber);
                    
                    reloadViewNumber = newViewNumber;
                    reloadPosition = -1;  
                }
            }  

            if (flingDirection < 0)  
            {  
                if (currentPosition < getLastPosition())  
                {  
                    // Determine the next view and outgoing view to recycle  
                    newViewNumber = getNextViewNumber(currentViewNumber);  

                    currentPosition = getNextPosition(currentPosition);  

                    reloadViewNumber = newViewNumber;  

                    reloadPosition = currentPosition;  
                    
                    tnComponent.notifyGalleryChanged(true);
                    
                    needSwitch = true;
                }
                else if (currentPosition == getLastPosition())
                {
                    // Determine the next view and outgoing view to recycle  
                    newViewNumber = getNextViewNumber(currentViewNumber);
                    
                    reloadViewNumber = newViewNumber;
                    reloadPosition = -1;  
                }
            }  

            if (newViewNumber != currentViewNumber)  
            {  
                if (needSwitch)
                {
                    currentViewNumber = newViewNumber;   
                }

                // Reload outgoing view from adapter in new position  
                views[reloadViewNumber].recycleView(reloadPosition);  
            }  
        }
        
        private void prepareAnimation()  
        {
            // Note: In this implementation the targetOffset will always be zero  
            // as we are centering the view; but we include the calculations of  
            // targetOffset and targetDistance for use in future implementations  
            initialOffset = getOffset();  
            targetOffset = getViewOffset(currentViewNumber, currentViewNumber);  
            targetDistance = targetOffset - initialOffset;  
        }
        
        private void startAnimation()
        {
            new Thread(this).start();
        }
        
        private int getViewOffset(int viewNumber, int relativeViewNumber)  
        {  
            // Determine width including configured padding width  
            int offsetWidth = galleryWidth + VIEW_PADDING_WIDTH;  

            // Position the previous view one measured width to left  
            if (viewNumber == getPrevViewNumber(relativeViewNumber))  
            {  
                return offsetWidth;  
            }  

            // Position the next view one measured width to the right  
            if (viewNumber == getNextViewNumber(relativeViewNumber))  
            {  
                return offsetWidth * -1;  
            }  

            return 0;  
        }  
        
        private int getPrevViewNumber(int relativeViewNumber)  
        {  
            return (relativeViewNumber == 0) ? 2 : relativeViewNumber - 1;
        }  

        private int getNextViewNumber(int relativeViewNumber)  
        {
            return (relativeViewNumber == 2) ? 0 : relativeViewNumber + 1;  
        }          

        public void run()
        {
            int curStep = 0;
            int steps = 4;
            
            while (curStep < steps)
            {
                float interpolatedDuration = (float)curStep / steps;
                DecelerateInterpolator interpolator = new DecelerateInterpolator();
                interpolatedDuration = interpolator.getInterpolation(interpolatedDuration);
                long interpolatedTime = (long)(mFlingDuration * interpolatedDuration);
                
                //safety check
                if (interpolatedTime == 0)
                {
                    interpolatedTime = 1;
                }
                else if (interpolatedTime > 1000)
                {
                    interpolatedTime = 1000;
                }
                
                int offset = initialOffset + (int) (targetDistance * interpolatedDuration);
                
                int size = views.length;
                for (int viewNumber = 0; viewNumber < size; viewNumber ++)  
                {  
                    // Only need to animate the visible views as the other view will always be off-screen  
                    if ((targetDistance > 0 && viewNumber != getNextViewNumber(currentViewNumber)) ||  
                        (targetDistance < 0 && viewNumber != getPrevViewNumber(currentViewNumber)))  
                    {  
                        int viewOffset = getViewOffset(viewNumber, currentViewNumber);
                        views[viewNumber].setOffset(viewOffset + offset, 0, currentViewNumber, true);  
                    }                      
                }  

                try
                {
                    Thread.sleep(interpolatedTime);
                }
                catch(InterruptedException e)
                {}
                curStep ++;
            }
            
            int size = views.length;
            for (int i = 0; i < size; i ++)
            {
                int viewOffset = getViewOffset(i, currentViewNumber);
                views[i].setOffset(viewOffset + targetOffset, 0, currentViewNumber, true);                
            }
        }
    }

    private class GalleryItemView  
    {
        private FrameLayout parentLayout;  
        private FrameLayout invalidLayout;  
        private GalleryLayout galleryLayout;  
        private View externalView;  
        
        private int position = -1;

        public GalleryItemView(FrameLayout parentLayout)  
        {
            this.parentLayout = parentLayout;  

            // Invalid layout is used when outside gallery  
            invalidLayout = new FrameLayout(context);  
            invalidLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));  

            // Internal layout is permanent for duration  
            galleryLayout = new GalleryLayout(context);  
            galleryLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)); 

            this.parentLayout.addView(galleryLayout);
        }

        public void recycleView(int newPosition)  
        {
            if (position == newPosition)
            {
                return;
            }
            
            position = newPosition;
            
            if (externalView != null)  
            {
                galleryLayout.removeView(externalView);  
            }  

            if (adapter != null)
            {
                if (newPosition >= getFirstPosition() && newPosition <= getLastPosition())
                {  
                    AbstractTnComponent component = tnComponent.getDecorationPage(newPosition); 
                    externalView = (View)component.getNativeUiComponent();
                }  
                else 
                {  
                    externalView = invalidLayout;  
                }  
            }  

            if (externalView != null)  
            {  
                galleryLayout.addView(externalView, new LinearLayout.LayoutParams(   
                    LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));  
            }
        }  
        
        public int getPosition()
        {
            return this.position;
        }

        public void setOffset(int xOffset, int yOffset, int relativeViewNumber)
        {
            setOffset(xOffset, yOffset, relativeViewNumber, false);
        }
        
        public void setOffset(final int xOffset, final int yOffset, final int relativeViewNumber, final boolean invalidateNow)  
        {  
            // Scroll the target view relative to its own position relative to currently displayed view            
            ((Activity)context).runOnUiThread(new Runnable()
            {
                public void run()
                {
                    galleryLayout.scrollTo(xOffset, yOffset, invalidateNow);                      
                }
            });
        }  

        public int getCurrentOffset()
        {  
            // Return the current scroll position  
            return galleryLayout.getScrollX();  
        }  
    }
    
    private class GalleryGestureDetector extends GestureDetector.SimpleOnGestureListener  
    {  
        public boolean onDown(MotionEvent e)  
        {  
            // Stop animation  
            isTouched = true;
            
            // Reset fling state  
            flingDirection = 0;  

            return true;  
        }  

        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY)  
        {  
            if (e2.getAction() == MotionEvent.ACTION_MOVE)  
            {  
                if (isDragging == false)  
                {  
                    // Stop animation  
                    isTouched = true;  

                    // Reconfigure scroll  
                    isDragging = true;  
                    flingDirection = 0;  

                    scrollTimestamp = System.currentTimeMillis();  
                    currentOffset = galleryView.getOffset();  
                }  

                float maxVelocity = Float.MAX_VALUE;
                if (ANIMATION_DURATION / 1000.0f > 0)
                {
                    maxVelocity = galleryWidth / (ANIMATION_DURATION / 1000.0f);  
                }
                
                if (maxVelocity > mMaxVelocity)
                {
                    mMaxVelocity = maxVelocity;
                }
                long timestampDelta = System.currentTimeMillis() - scrollTimestamp;  

                float maxScrollDelta = maxVelocity * (timestampDelta / 1000.0f);   

                float currentScrollDelta = e1.getRawX() - e2.getRawX();

                if (currentScrollDelta < maxScrollDelta * -1) currentScrollDelta = maxScrollDelta * -1;  
                if (currentScrollDelta > maxScrollDelta) currentScrollDelta = maxScrollDelta;  

                int scrollOffset = Math.round(currentOffset + currentScrollDelta);  

                // We can't scroll more than the width of our own frame layout  

                if (scrollOffset >= galleryWidth) scrollOffset = galleryWidth;  

                if (scrollOffset <= galleryWidth * -1) scrollOffset = galleryWidth * -1;  

                   
                distanceX = Math.abs(e1.getRawX() - e2.getRawX());
                distanceY = Math.abs(e1.getRawY() - e2.getRawY());

                if (distanceX <= 1.0 
//                        || distanceY <= 1.0 
//                        || Math.abs(scrollOffset) <= 10
                        || distanceX < distanceY
                        || isScrollingLeftAndRight)
                {
                    if (isFirstTimeScrolling)
                    {
                        isScrollingLeftAndRight = true;
                        isFirstTimeScrolling = false;
                    }
                    return false;                
                }
                else
                {
                    isFirstTimeScrolling = false;
                }
                
                int flingDirection = (scrollOffset > 0) ? -1 : ((scrollOffset < 0) ? 1 : 0); 
                galleryView.recycleInactiveViews(flingDirection);
                galleryView.setOffset(scrollOffset);
            }  

            return true;  
        }  

        public void onLongPress(MotionEvent e)  
        {  
            // Finalise scrolling  
            flingDirection = 0;  

            processGesture();  
        }  

        public void onShowPress(MotionEvent e)  
        {  
        }  

        public boolean onSingleTapUp(MotionEvent e)  
        {  
            // Reset fling state  
            flingDirection = 0;  

            return false;  
        }  
    }  
    
}

class GalleryLayout extends LinearLayout
{
    private boolean invalidateNow;
    
    public GalleryLayout(Context context)
    {
        super(context);
    }

    public void scrollTo(int xOffset, int yOffset, boolean invalidateNow)
    {
        this.invalidateNow = invalidateNow;
        scrollTo(xOffset, yOffset);
    }
    
    public void invalidate()
    {
        if (invalidateNow)
        {
            super.invalidate();
        }
    }
}

