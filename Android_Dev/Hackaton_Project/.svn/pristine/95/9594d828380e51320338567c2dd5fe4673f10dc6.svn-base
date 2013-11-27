/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * AndroidFrogDialog.java
 *
 */
package com.telenav.tnui.widget.android;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnShowListener;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.LinearLayout;

import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.core.AbstractTnUiHelper;
import com.telenav.tnui.core.INativeUiComponent;
import com.telenav.tnui.core.TnKeyEvent;
import com.telenav.tnui.core.TnUiArgs;
import com.telenav.tnui.core.TnUiEvent;
import com.telenav.tnui.core.TnUiAnimationContext.ITnUiAnimationListener;
import com.telenav.tnui.core.android.AndroidUiEventHandler;
import com.telenav.tnui.core.android.AndroidUiHelper;
import com.telenav.tnui.core.android.AndroidUiMethodHandler;
import com.telenav.tnui.graphics.TnColor;
import com.telenav.tnui.widget.TnPopupContainer;

/**
 * FrogAndroidDialog extends Android system Dialog
 * <br/>
 * <br/>
 * Use TnPopuplinearContainer control the layout of this Dialog.
 * 
 *@author jshjin (jshjin@telenav.cn)
 *@date 2010-6-30
 */
class AndroidDialog extends Dialog implements INativeUiComponent, OnCancelListener, OnShowListener
{
    protected TnPopupContainer popupContainer;
    protected View contentView;
    
    /**
     * constructor for AndroidDialog.
     * @param context the context
     * @param popupContainer the outer container.
     */
    public AndroidDialog(Context context, TnPopupContainer popupContainer)
    {
        super(context);
        
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_PANEL);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE); 
        this.popupContainer = popupContainer;
        
        this.initBackGroundDrawable();
        this.setOnCancelListener(this);
    }
    
    protected void initBackGroundDrawable()
    {
        Drawable backgroundDrawable = new ColorDrawable(TnColor.TRANSPARENT);
        getWindow().setBackgroundDrawable(backgroundDrawable);
    }
    
    protected void calcPosition()
    {
        if(popupContainer.getTnUiArgs().get(TnUiArgs.KEY_POSITION_X) != null || popupContainer.getTnUiArgs().get(TnUiArgs.KEY_POSITION_Y) != null)
        {
            int xDelta = 0;
            int yDelta = 0;
            
            if(popupContainer.getTnUiArgs().get(TnUiArgs.KEY_POSITION_X) != null)
            {
                if(this.getNativeWidth() != 0)
                {
                    int x = popupContainer.getTnUiArgs().get(TnUiArgs.KEY_POSITION_X).getInt();
                    int screenWidth = ((AbstractTnUiHelper)AbstractTnUiHelper.getInstance()).getDisplayWidth();
                    int dialogWidth = this.getNativeWidth();
                    xDelta = x - (screenWidth - dialogWidth) / 2;
                }
            }
            
            if(popupContainer.getTnUiArgs().get(TnUiArgs.KEY_POSITION_Y) != null)
            {
                if(this.getNativeHeight() != 0)
                {
                    int y = popupContainer.getTnUiArgs().get(TnUiArgs.KEY_POSITION_Y).getInt();
                    int screenHeight = ((AbstractTnUiHelper)AbstractTnUiHelper.getInstance()).getDisplayHeight();
                    int popupHeight = this.getNativeHeight();
                    yDelta = y - (screenHeight - popupHeight) / 2;
                }
            }
            
            Window window = getWindow();
            if(window != null)
            {
                WindowManager.LayoutParams windowLayoutParams = getWindow().getAttributes();
                windowLayoutParams.x = xDelta;
                windowLayoutParams.y = yDelta;
                window.setAttributes(windowLayoutParams);
            }
        }
    }

    public Object callUiMethod(int eventMethod, Object[] args)
    { 
        if(this.contentView != null)
        {
            Object obj = AndroidUiMethodHandler.callUiMethod(this.popupContainer, this.contentView, eventMethod, args);

            if (!AndroidUiMethodHandler.NO_HANDLED.equals(obj))
                return obj;
        }
        
        switch (eventMethod)
        {
            case TnPopupContainer.METHOD_SHOW:
            {
                ((AndroidUiHelper)AndroidUiHelper.getInstance()).runOnUiThread(new Runnable()
                {
                    public void run()
                    {
                        if(popupContainer.getAnimationContext() != null)
                        {
                            ((Animation)popupContainer.getAnimationContext().getNativeAnimation()).startNow();
                            popupContainer.setAnimationContext(null);
                        }
                        
                        if(popupContainer.getTnUiArgs().get(TnUiArgs.KEY_POSITION_X) != null || popupContainer.getTnUiArgs().get(TnUiArgs.KEY_POSITION_Y) != null)
                        {
                            setOnShowListener(AndroidDialog.this);
                        }
                        
                        AndroidDialog.this.getWindow().setLayout(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
                        
                        show();
                    }
                });
                break;
            }
            case TnPopupContainer.METHOD_HIDE:
            {
            
                ((AndroidUiHelper)AndroidUiHelper.getInstance()).runOnUiThread(new Runnable()
                {
                    public void run()
                    {
                        if(!isShowing())
                           return;
                        if(contentView != null && popupContainer.getAnimationContext() != null)
                        {
                            final ITnUiAnimationListener animationListener = popupContainer.getAnimationContext().getAnimationListener();
                            
                            Animation animation = (Animation)popupContainer.getAnimationContext().getNativeAnimation();
                            animation.setAnimationListener(new AnimationListener()
                            {
                                public void onAnimationStart(Animation animation)
                                {
                                    if(animationListener != null)
                                    {
                                        animationListener.onAnimationStart(popupContainer.getAnimationContext());
                                    }
                                }
                                
                                public void onAnimationRepeat(Animation animation)
                                {
                                    if(animationListener != null)
                                    {
                                        animationListener.onAnimationRepeat(popupContainer.getAnimationContext());
                                    }
                                }
                                
                                public void onAnimationEnd(Animation animation)
                                {
                                    if(animationListener != null)
                                    {
                                        animationListener.onAnimationEnd(popupContainer.getAnimationContext());
                                    }
                                    
                                    dismiss();
                                }
                            });
                            
                            contentView.startAnimation(animation);
                        }
                        else
                        {
                            dismiss();
                        }
                    }
                });
                break;
            }
            case TnPopupContainer.METHOD_SET_CONTENT:
            {
                this.contentView = (View)((AbstractTnComponent)args[0]).getNativeUiComponent();
                
                LinearLayout layout = new LinearLayout(AndroidDialog.this.getContext());
                layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
                layout.addView(contentView);
                layout.setGravity(Gravity.CENTER);
                
                this.setContentView(layout);
                break;
            }
            case TnPopupContainer.METHOD_SIZE_CHANGE:
            {
                calcPosition();
                break;
            }
            case TnPopupContainer.METHOD_SET_WINDOW_TYPE:
            {
                int windowType = ((Integer)args[0]).intValue();
                switch(windowType)
                {
                    case TnPopupContainer.WINDOW_TYPE_APPLICATION:
                    {
                        this.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION);
                        break;
                    }
                    case TnPopupContainer.WINDOW_TYPE_APPLICATION_PANEL:
                    {
                        this.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_PANEL);
                        break;
                    }
                }
                break;
            }
        }

        return null;
    }

    public int getNativeHeight()
    {
        return this.popupContainer.getHeight();
    }

    public int getNativeWidth()
    {
        return this.popupContainer.getWidth();
    }

    public int getNativeX()
    {
        return 0;
    }

    public int getNativeY()
    {
        return 0;
    }

    public AbstractTnComponent getTnUiComponent()
    {
        return this.popupContainer;
    }

    public boolean isNativeFocusable()
    {
        return true;
    }

    public boolean isNativeVisible()
    {
        return this.isShowing();
    }

    public boolean requestNativeFocus()
    {
        return true;
    }

    public void requestNativePaint()
    {
        
    }

    public void setNativeFocusable(boolean isFocusable)
    {
           
    }

    public void setNativeVisible(boolean isVisible)
    {
            
    }
    
    public void onCancel(DialogInterface dialog)
    {
        TnUiEvent uiEvent = new TnUiEvent(TnUiEvent.TYPE_KEY_EVENT, this.popupContainer);
        uiEvent.setKeyEvent(new TnKeyEvent(TnKeyEvent.ACTION_UP, TnKeyEvent.KEYCODE_BACK));
        
        popupContainer.dispatchUiEvent(uiEvent);
    }
    
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        boolean isHandled = AndroidUiEventHandler.onKeyDown(this.popupContainer, keyCode, event);
        
        return isHandled ? true : super.onKeyDown(keyCode, event);
    }

    public boolean onKeyUp(int keyCode, KeyEvent event)
    {
        boolean isHandled = AndroidUiEventHandler.onKeyUp(this.popupContainer, keyCode, event);
        
        return isHandled ? true : super.onKeyUp(keyCode, event);
    }
    
    public boolean onTouchEvent(MotionEvent event)
    {
        boolean isHandled = AndroidUiEventHandler.onTouch(this.popupContainer, event);
        
        return isHandled ? true : super.onTouchEvent(event);
    }
    
    public boolean onTrackballEvent(MotionEvent event)
    {
        boolean isHandled = AndroidUiEventHandler.onTrackball(this.popupContainer, event);

        return isHandled ? true : super.onTrackballEvent(event);
    }
    
    public void onShow(DialogInterface dialog)
    {
        calcPosition();
    }
    
    public boolean onSearchRequested()
    {
    	//Disable the searchRequest which is triggered by long pressing menu.
    	return false;
    }
    
}
