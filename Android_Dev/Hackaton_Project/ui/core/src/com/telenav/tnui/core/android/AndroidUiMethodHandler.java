/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * AndroidUiMethodHandler.java
 *
 */
package com.telenav.tnui.core.android;

import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.core.AbstractTnUiHelper;
import com.telenav.tnui.core.ITnFocusChangeListener;
import com.telenav.tnui.core.TnUiAnimationContext;


import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.animation.Animation;

/**
 * common native ui method handler at android platform.
 * 
 *@author fqming (fqming@telenav.cn)
 *@date 2010-6-22
 */
public class AndroidUiMethodHandler
{
    /**
     * Means that don't handle this ui method at the common handler of this class.
     */
    public static final Object NO_HANDLED = new Object();

   
    /**
     * handle the common native ui method.
     * 
     * @param tnComponent owner of the ui method.
     * @param view native ui component of the ui method.
     * @param eventMethod the name of method.
     * @param args the arguments when invoke the method.
     * @return the return value after invoke the method.
     */
    public static Object callUiMethod(final AbstractTnComponent tnComponent, final View view, final int eventMethod, final Object[] args)
    {
        switch (eventMethod)
        {
            case AbstractTnComponent.METHOD_FOCUS_LISTENER:
            {
                final ITnFocusChangeListener focusChangeListener = (ITnFocusChangeListener) args[0];
                if (focusChangeListener == null)
                {
                    view.setOnFocusChangeListener(null);
                }
                else
                {
                    view.setOnFocusChangeListener(new OnFocusChangeListener()
                    {

                        public void onFocusChange(View v, boolean hasFocus)
                        {
                            focusChangeListener.focusChange(tnComponent, hasFocus);
                        }
                    });
                }
                
                return null;
            }
            case AbstractTnComponent.METHOD_REQUEST_RELAYOUT:
            {
                ((AndroidUiHelper) AbstractTnUiHelper.getInstance())
                        .runOnUiThread(new Runnable()
                        {
                            public void run()
                            {
                                view.requestLayout();
                            }
                        });
    
                return null;
            }
            case AbstractTnComponent.METHOD_SET_ENABLED:
            {
                view.setEnabled(((Boolean)args[0]).booleanValue());
                
                return null;
            }
            case AbstractTnComponent.METHOD_IS_ENABLED:
            {
                return new Boolean(view.isEnabled());
            }
            case AbstractTnComponent.METHOD_SET_ANIMATION:
            {
                TnUiAnimationContext context = (TnUiAnimationContext)args[0];
                view.setAnimation((Animation)context.getNativeAnimation());
                break;
            }
            case AbstractTnComponent.METHOD_SET_ID:
            {
                view.setId(tnComponent.getId());
                break;
            }
            case AbstractTnComponent.METHOD_SET_CONTENT_DESCRIPTION:
            {
                if (args[0] instanceof String)
                {
                    view.setContentDescription((String)args[0]);               
                }
                break;
            }
        }

        return NO_HANDLED;
    }
}
