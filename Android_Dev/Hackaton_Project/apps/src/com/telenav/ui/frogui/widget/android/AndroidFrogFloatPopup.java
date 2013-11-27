/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * AndroidFrogFloatPopup.java
 *
 */
package com.telenav.ui.frogui.widget.android;

import android.content.Context;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.core.INativeUiComponent;
import com.telenav.tnui.core.TnUiAnimationContext.ITnUiAnimationListener;
import com.telenav.tnui.core.android.AndroidUiHelper;
import com.telenav.tnui.core.android.AndroidUiMethodHandler;
import com.telenav.ui.frogui.widget.FrogFloatPopup;

/**
 *@author bduan
 *@date 2010-7-14
 */
class AndroidFrogFloatPopup extends PopupWindow implements INativeUiComponent
{
    protected Object mutex = new Object();
    protected Context context;
    protected boolean isForce = false;
    protected FrogFloatPopup frogFloatPopup;
    protected View contentView;
    
    public AndroidFrogFloatPopup(Context context, FrogFloatPopup frogFloatPopup)
    {
        super(context);
        this.context = context;
        this.frogFloatPopup = frogFloatPopup;
        this.setBackgroundDrawable(null);
        this.setInputMethodMode(INPUT_METHOD_NEEDED);//should find a way let it behind IM window.
    }
    
    public void setContent(AbstractTnComponent component)
    {
        if(component != null && component.getNativeUiComponent() != null)
        {
            View v = (View) component.getNativeUiComponent();
            ViewParent parent = v.getParent();
            if (parent != null && parent instanceof ViewGroup)
            {
                ViewGroup groupParent = (ViewGroup) parent;
                groupParent.removeView(v);
            }

            if (component.getPreferredWidth() > 0 && component.getPreferredHeight() > 0)
            {
                PopupViewContainer.LayoutParams popupViewContainerParams = new PopupViewContainer.LayoutParams(component.getPreferredWidth(), component.getPreferredHeight());
                v.setLayoutParams(popupViewContainerParams);
            }
            
            this.contentView = v;
            
            PopupViewContainer popupViewContainer = new PopupViewContainer(context);
            popupViewContainer.addView(v);
            this.setContentView(popupViewContainer);
        }
        
    }
    
    private void showFloatPopup(AbstractTnComponent anchor, int x, int y, int w, int h, boolean isDropDown)
    {
        if(w <= 0 || h <= 0)
        {
            return;
        }
        
        if(isShowing())
        {
            update(x, y, w, h);
            if(contentView != null)
            {
                contentView.invalidate();
            }
            return;
        }
        
        if(w != 0)
            this.setWidth(w);
        
        if(h != 0)
            this.setHeight(h);
        
        if(isDropDown)
        {
            showAsDropDown((View)anchor.getNativeUiComponent(), x, y);
        }
        else
        {
            showAtLocation((View)anchor.getNativeUiComponent(), Gravity.NO_GRAVITY, x, y);
        }
    }
    
    private void updateFloatPopup(final AbstractTnComponent anchor, final int x, final int y, final int w, final int h)
    {
        if(anchor != null && anchor.getNativeUiComponent() != null)
        {
            isForce = true;
            update((View)anchor.getNativeUiComponent(), x, y, w, h);
        }
        else
        {
            update(x, y, w, h, true);
        }
    }
    
    private void updateFloatPopup(final AbstractTnComponent anchor, final int w, final int h)
    {
        if(anchor != null && anchor.getNativeUiComponent() != null)
        {
            isForce = true;
            update((View)anchor.getNativeUiComponent(), w, h);
        }
        else
        {
            update(w, h);
        }
    }
    
    public void update(int x, int y, int width, int height, boolean force)
    {
        if(isForce)
        {
            isForce = false;
            force = true;
        }
        super.update(x, y, width, height, force);
    }
    
    public Object callUiMethod(int eventMethod, final Object[] args)
    {
        if(this.contentView != null)
        {
            Object obj = AndroidUiMethodHandler.callUiMethod(this.frogFloatPopup, this.contentView, eventMethod, args);

            if (!AndroidUiMethodHandler.NO_HANDLED.equals(obj))
                return obj;
        }
        
        switch (eventMethod)
        {
            case FrogFloatPopup.METHOD_SET:
            {
                if(args != null && args.length > 0)
                {
                    setContent((AbstractTnComponent)args[0]);
                    if(args.length > 2)
                    {
                        setWidth( ((Integer)args[1]).intValue() );
                        setHeight( ((Integer)args[2]).intValue() );
                    }
                }
                break;
            }
            case FrogFloatPopup.METHOD_GET:
            {
                if(this.contentView instanceof INativeUiComponent)
                {
                    INativeUiComponent nativeUiComponent = (INativeUiComponent)contentView;
                    return nativeUiComponent.getTnUiComponent();
                }
                else if(this.getContentView() instanceof INativeUiComponent)
                {
                    INativeUiComponent nativeUiComponent = (INativeUiComponent)this.getContentView();
                    return nativeUiComponent.getTnUiComponent();
                }
                break;
            }
            case FrogFloatPopup.METHOD_SHOW:
            {
                if(args != null && args.length > 5)
                {
                    ((AndroidUiHelper)AndroidUiHelper.getInstance()).runOnUiThread(new Runnable()
                    {
                        public void run()
                        {
                            if (frogFloatPopup.getAnimationContext() != null
                                    && frogFloatPopup.getAnimationContext().getNativeAnimation() != null)
                            {
                                ((Animation) frogFloatPopup.getAnimationContext().getNativeAnimation()).startNow();
                                frogFloatPopup.setAnimationContext(null);
                            }

                            int x = ((Integer) args[1]).intValue();
                            int y = ((Integer) args[2]).intValue();

                            int w = ((Integer) args[3]).intValue();
                            int h = ((Integer) args[4]).intValue();
                            boolean isDropDown = ((Boolean) args[5]).booleanValue();
                            showFloatPopup((AbstractTnComponent) args[0], x, y, w, h, isDropDown);
                        }
                    });
                }
                break;
            }
            case FrogFloatPopup.METHOD_HIDE:
            {
                if(this.isShowing())
                {
                    ((AndroidUiHelper)AndroidUiHelper.getInstance()).runOnUiThread(new Runnable()
                    {
                        public void run()
                        {
                            synchronized (mutex)
                            {
                                if(contentView != null && frogFloatPopup.getAnimationContext() != null)
                                {
                                    final ITnUiAnimationListener animationListener = frogFloatPopup.getAnimationContext().getAnimationListener();
                                    
                                    Animation animation = (Animation)frogFloatPopup.getAnimationContext().getNativeAnimation();
                                    animation.setAnimationListener(new AnimationListener()
                                    {
                                        public void onAnimationStart(Animation animation)
                                        {
                                            if(animationListener != null)
                                            {
                                                animationListener.onAnimationStart(frogFloatPopup.getAnimationContext());
                                            }
                                        }
                                        
                                        public void onAnimationRepeat(Animation animation)
                                        {
                                            if(animationListener != null)
                                            {
                                                animationListener.onAnimationRepeat(frogFloatPopup.getAnimationContext());
                                            }
                                        }
                                        
                                        public void onAnimationEnd(Animation animation)
                                        {
                                            if(animationListener != null)
                                            {
                                                animationListener.onAnimationEnd(frogFloatPopup.getAnimationContext());
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
                        }
                    });
                }
                break;
            }
            case FrogFloatPopup.METHOD_UPDATE:
            {
                if(args != null && args.length > 4)
                {
                    int x = ((Integer)args[1]).intValue();
                    int y = ((Integer)args[2]).intValue();
                    
                    int w = ((Integer)args[3]).intValue();
                    int h = ((Integer)args[4]).intValue();
                    
                    updateFloatPopup((AbstractTnComponent)args[0], x, y, w, h);
                }
                else if(args != null && args.length > 2)
                {
                    int w = ((Integer)args[1]).intValue();
                    int h = ((Integer)args[2]).intValue();
                    
                    updateFloatPopup((AbstractTnComponent)args[0], w, h);
                }
                break;
            }
            case FrogFloatPopup.METHOD_SET_WDITH:
            {
                if(args != null && args.length > 0)
                    setWidth( ((Integer)args[0]).intValue() );
                
                break;
            }
            case FrogFloatPopup.METHOD_SET_HEIGHT:
            {
                if(args != null && args.length > 0)
                    setHeight( ((Integer)args[0]).intValue() );
                
                break;
            }
        }
        
        return null;
    }

    public int getNativeHeight()
    {
        return this.getHeight();
    }

    public int getNativeWidth()
    {
        return this.getWidth();
    }

    public int getNativeX()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    public int getNativeY()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    public AbstractTnComponent getTnUiComponent()
    {
        return null;
    }

    public boolean isNativeFocusable()
    {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean isNativeVisible()
    {
        return isShowing();
    }

    public boolean requestNativeFocus()
    {
        // TODO Auto-generated method stub
        return false;
    }

    public void requestNativePaint()
    {
        // TODO Auto-generated method stub
    }

    public void setNativeFocusable(boolean isFocusable)
    {
        this.setFocusable(isFocusable);
    }

    public void setNativeVisible(boolean isVisible)
    {
        // TODO Auto-generated method stub
        
    }

    protected void onDismiss()
    {
        this.dismiss();
    }
    
    private class PopupViewContainer extends LinearLayout
    {

        public PopupViewContainer(Context context)
        {
            super(context);
        }

        public boolean dispatchKeyEvent(KeyEvent event)
        {
            if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN)
            {
                onDismiss();
                return true;
            }
            else
            {
                return super.dispatchKeyEvent(event);
            }
        }
    }
}
