/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * AndroidActivity.java
 *
 */
package com.telenav.tnui.core.android;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.telenav.logger.Logger;
import com.telenav.tnui.core.AbstractTnUiHelper;
import com.telenav.tnui.core.INativeUiComponent;
import com.telenav.tnui.core.ITnScreenAttachedListener;
import com.telenav.tnui.core.TnScreen;
import com.telenav.tnui.core.TnUiEvent;
import com.telenav.tnui.core.TnUiTimer;

/**
 * An activity is a single, focused thing that the user can do. Almost all activities interact with the user, so the
 * Activity class takes care of creating a window for you in which you can place your UI with setContentView. While
 * activities are often presented to the user as full-screen windows, they can also be used in other ways: as floating
 * windows (via a theme with android.R.attr#windowIsFloating set) or embedded inside of another activity (using
 * ActivityGroup).
 * 
 *@author fqming (fqming@telenav.cn)
 *@date 2010-6-29
 */
public class AndroidActivity extends Activity implements IAndroidBaseActivity
{

    protected FrameLayout contentView;
    
    /**
     * Called when an activity you launched exits, giving you the requestCode you started it with, the resultCode it
     * returned, and any additional data from it.
     * 
     * @author fqming
     * 
     */
    public interface IAndroidActivityResultCallback
    {
        /**
         * Called when an activity you launched exits, giving you the requestCode you started it with, the resultCode it
         * returned, and any additional data from it.
         * 
         * @param requestCode The integer request code originally supplied to startActivityForResult(), allowing you to
         *            identify who this result came from.
         * @param resultCode The integer result code returned by the child activity through its setResult().
         * @param data An Intent, which can return result data to the caller (various data can be attached to Intent
         *            "extras").
         */
        public void onActivityResult(int requestCode, int resultCode, Intent data);
    }
    
    /**
     * The top screen of this activity.
     */
    protected TnScreen currentScreen;
    
    /**
     * the last status of Orientation.
     */
    protected int oldOrientation;

    /**
     * status bar height
     */
    protected int[] statusBarHeights;
    
    /**
     * the callback of activity result.
     */
    protected IAndroidActivityResultCallback callback;
    
    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        
        statusBarHeights = new int[]{0, -1};
        
        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);

        ll.post(new Runnable()
        {
            public void run()
            {
                loadSystemBarHeight();
            }
        });
    }
    
    /**
     * @see Activity#onRestart()
     */
    public void onRestart()
    {
        super.onRestart();
        
        TnUiTimer.getInstance().enable(true);
    }
    
    /**
     * @see Activity#onStop()
     */
    public void onStop()
    {
        super.onStop();
        
        TnUiTimer.getInstance().enable(false);
    }
    
    /**
     * set the callback of result.
     * 
     * @param callback the callback object
     */
    public void setActivityResultCallback(IAndroidActivityResultCallback callback)
    {
        this.callback = callback;
    }
    
    /**
     * Retrieve the callback of result.
     * 
     * @return callback object.
     */
    public IAndroidActivityResultCallback getActivityResultCallback()
    {
        return this.callback;
    }
    
    public void onConfigurationChanged(Configuration config) 
    {
        handleConfigChange(config);
        super.onConfigurationChanged(config);
    }
    
    protected void handleConfigChange(Configuration config)
    {
        // get new orientation
        int orientation = ((AbstractTnUiHelper)AbstractTnUiHelper.getInstance()).getOrientation();
        
        if(orientation != oldOrientation)
        {
            if (this.currentScreen != null)
            {
                AndroidUiEventHandler.onPreSizeChanged(this.currentScreen.getRootContainer());
                
                if(this.currentScreen.getCurrentPopup() != null)
                {
                    this.currentScreen.getCurrentPopup().dispatchSizeChanged(0, 0, 0, 0);
                    AndroidUiEventHandler.onPreSizeChanged(this.currentScreen.getCurrentPopup());
                }
            }
            
            View focusView = this.getCurrentFocus();
            if (focusView instanceof INativeUiComponent)
            {
                AndroidUiEventHandler.onPreSizeChanged(((INativeUiComponent) focusView).getTnUiComponent());
            }
        }
        
        oldOrientation = orientation;
        return;
    }
    
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(this.callback != null)
        {
            this.callback.onActivityResult(requestCode, resultCode, data);
            this.callback = null;
        }
        
        super.onActivityResult(requestCode, resultCode, data);
    }

    public final boolean onKeyDown(int keyCode, KeyEvent event)
    {
        boolean isHandle = handleKeyDown(keyCode, event);
        return isHandle ? true : super.onKeyDown(keyCode, event);
    }
    
    protected boolean handleKeyDown(int keyCode, KeyEvent event)
    {
        View focusView = this.getCurrentFocus();
        boolean isHandled = false;
        if (focusView instanceof INativeUiComponent)
        {
            isHandled = AndroidUiEventHandler.onKeyDown(((INativeUiComponent) focusView).getTnUiComponent(), keyCode, event);
        }
        if (this.currentScreen != null && !isHandled)
        {
            isHandled = AndroidUiEventHandler.onKeyDown(this.currentScreen.getRootContainer(), keyCode, event);
        }
        return isHandled;
    }
    
    public final boolean onKeyUp(int keyCode, KeyEvent event)
    {
        boolean isHandled = handleKeyUp(keyCode, event);
        return isHandled ? true : super.onKeyUp(keyCode, event);
    }
    
    protected boolean handleKeyUp(int keyCode, KeyEvent event)
    {
        View focusView = this.getCurrentFocus();
        boolean isHandled = false;
        if (focusView instanceof INativeUiComponent)
        {
            isHandled = AndroidUiEventHandler.onKeyUp(((INativeUiComponent) focusView).getTnUiComponent(), keyCode, event);
        }
        else if (this.currentScreen != null)
        {
            isHandled = AndroidUiEventHandler.onKeyUp(this.currentScreen.getRootContainer(), keyCode, event);
        }
        return isHandled;
    }
    
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        super.onPrepareOptionsMenu(menu);
        menu.clear();
		
        return AndroidUiEventHandler.onCreateOptionsMenu(this.currentScreen.getRootContainer(), menu);
    }
    
    public boolean onOptionsItemSelected(MenuItem item)
    {
        Logger.log(Logger.INFO, AndroidUiEventHandler.class.getName(), "onMenuItemSelected", 
            null, new Object[]{TnUiEvent.LOG_UI_EVENT, Integer.valueOf(0), Integer.valueOf(item.getItemId())}, true);
        return AndroidUiEventHandler.onOptionsItemSelected(this.currentScreen.getRootContainer(), item);
    }
    
    public boolean onContextItemSelected(MenuItem item)
    {
        Logger.log(Logger.INFO, AndroidUiEventHandler.class.getName(), "onMenuItemSelected", 
            null, new Object[]{TnUiEvent.LOG_UI_EVENT, Integer.valueOf(1), Integer.valueOf(item.getItemId())}, true);
        boolean isHandled = AndroidUiEventHandler.onOptionsItemSelected(this.currentScreen.getRootContainer(), item);
        return isHandled ? true : super.onContextItemSelected(item);
    }
    
    public TnScreen getCurrentScreen()
    {
        return this.currentScreen;
    }
    
    public int getStatusBarHeight(int position)
    {
        return this.statusBarHeights[position];
    }
    
    /**
     * Set the screen content to an explicit screen. This screen is placed directly into the activity's view hierarchy.
     * It can itself be a complex view hierarchy.
     * 
     * @param screen
     */
    public final void showScreen(TnScreen screen)
    {
        if (screen == null)
            return;

        final View view = (View) screen.getRootContainer().getNativeUiComponent();
        final View backgroundView = screen.getBackgroundComponent() == null ? null : (View) screen.getBackgroundComponent().getNativeUiComponent();
        
        if (screen.equals(currentScreen))
        {
            view.postInvalidate();
        }
        else
        {
            handleScreenAttachedEvent(currentScreen, ITnScreenAttachedListener.DETTACHED);
            
            if(currentScreen != null && currentScreen.getRootContainer().getAnimationContext() != null)
            {
                ((Animation)currentScreen.getRootContainer().getAnimationContext().getNativeAnimation()).startNow();
                currentScreen.getRootContainer().setAnimationContext(null);
            }
            
            currentScreen = screen;
            
            if(screen != null && screen.getRootContainer().getAnimationContext() != null)
            {
                ((Animation)screen.getRootContainer().getAnimationContext().getNativeAnimation()).startNow();
                screen.getRootContainer().setAnimationContext(null);
            }
            
            handleScreenAttachedEvent(currentScreen, ITnScreenAttachedListener.BEFORE_ATTACHED);
            
            final View leafFocusedView = view.hasFocus() ? getLeafFocusedView(view) : null;
            
            setContentView(view, leafFocusedView, backgroundView);    
        }
    }
    
    /**
     * If a view only contains view groups, we treat it as an empty view
     * 
     * @param view
     * @return
     */
    private boolean isViewEmpty(View view)
    {
        if (view instanceof ViewGroup)
        {
            ViewGroup vg = (ViewGroup) view;
            for (int i = 0; i < vg.getChildCount(); i++)
            {
                if (!isViewEmpty(vg.getChildAt(i)))
                {
                    return false;
                }
            }
            return true;
        }
        else
        {
            return false;
        }
    }

    protected void setContentView(View view, View leafFocusedView, View backgroundView)
    {
        if(backgroundView == null)
        {
            setContentView(view);
            contentView = null;
        }
        else
        {
            if(contentView == null)
            {
                contentView = new FrameLayout(this);
                contentView.addView(backgroundView, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.FILL_PARENT, FrameLayout.LayoutParams.FILL_PARENT));

                if (isViewEmpty(view))
                {
                    contentView.addView(view, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
                            FrameLayout.LayoutParams.WRAP_CONTENT));
                }
                else
                {
                    contentView.addView(view, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.FILL_PARENT,
                            FrameLayout.LayoutParams.FILL_PARENT));
                }
                
                setContentView(contentView);
            }
            else
            {
                if(!backgroundView.equals(contentView.getChildAt(0)))
                {
                    contentView.removeViewAt(0);
                    contentView.addView(backgroundView, 0, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.FILL_PARENT, FrameLayout.LayoutParams.FILL_PARENT));
                }
                if(contentView.getChildCount() > 1)
                {
                    contentView.removeViewAt(1);
                }
                
                boolean needAddView = true;
                if(view instanceof ViewGroup)
                {
                    ViewGroup vg = (ViewGroup)view;
                    if(vg.getChildCount() <= 0)
                    {
                        needAddView = false;
                    }
                }
                if(needAddView)
                {
                    contentView.addView(view, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT));
                }
            }
        }

        loadSystemBarHeight();
        
        handleScreenAttachedEvent(currentScreen, ITnScreenAttachedListener.AFTER_ATTACHED);
        
        View rootView = view.getRootView();
        if (leafFocusedView == null)
        {
            if(!view.isInTouchMode())
            {
                rootView.requestFocus(View.FOCUS_FORWARD);
            }
        }
        else
        {
            leafFocusedView.clearFocus();
            leafFocusedView.requestFocus();
        }
    }
    
    protected View getLeafFocusedView(View view)
    {
        View leafFocusedView = view;
        while(leafFocusedView instanceof ViewGroup)
        {
            View focusedChild = ((ViewGroup)leafFocusedView).getFocusedChild();
            if(focusedChild == null)
            {
                break;
            }
            else
            {
                leafFocusedView = focusedChild;
            }
            
        }
        return leafFocusedView;
    }
    
    protected void handleScreenAttachedEvent(TnScreen screen, int attached)
    {
        if (screen != null)
        {
            for (int i = 0; i < screen.getScreenAttachedListenerSize(); i++)
            {
                ITnScreenAttachedListener listener = screen.getScreenAttachedListener(i);
                try
                {
                    listener.onScreenUiEngineAttached(screen, attached);
                    screen.getRootContainer().requestPaint();
                }
                catch (Exception e)
                {
                    Logger.log(this.getClass().getName(), e);
                }
            }
        }
    }
    
    protected void loadSystemBarHeight()
    {
        int bottomDefaultValue = 0;
        if(Build.VERSION.SDK_INT != 13)
        {
            bottomDefaultValue = 0;
        }
        else
        {
            bottomDefaultValue = -1;
        }
        //Since we the bottom height is 0 in SDK3.2, so we initialize it as -1. 
        if (this.statusBarHeights[0] <= 0 && this.statusBarHeights[1] <= bottomDefaultValue)
        {
            Rect rect = new Rect();
            Window window = getWindow();
            window.getDecorView().getWindowVisibleDisplayFrame(rect);
            if (rect.top > 0)
            {
                this.statusBarHeights[0] = rect.top;
            }
            
            this.statusBarHeights[1] = window.getWindowManager().getDefaultDisplay().getHeight() - rect.bottom;
        }
    }
    
    public void onContextMenuClosed(Menu menu)
    {
        Logger.log(Logger.INFO, this.getClass().getName(), "onMenuClosed", null, new Object[]{TnUiEvent.LOG_UI_EVENT, Integer.valueOf(1)}, true);
        super.onContextMenuClosed(menu);
    }

    public void onOptionsMenuClosed(Menu menu)
    {
        Logger.log(Logger.INFO, this.getClass().getName(), "onMenuClosed", null, new Object[]{TnUiEvent.LOG_UI_EVENT, Integer.valueOf(0)}, true);
        super.onOptionsMenuClosed(menu);
    }
}
