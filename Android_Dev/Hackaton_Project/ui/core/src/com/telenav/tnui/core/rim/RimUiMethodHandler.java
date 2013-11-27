/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * RimUiMethodHandler.java
 *
 */
package com.telenav.tnui.core.rim;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FocusChangeListener;
import net.rim.device.api.ui.UiApplication;

import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.core.AbstractTnUiHelper;
import com.telenav.tnui.core.ITnFocusChangeListener;

/**
 * common native ui method handler at rim platform.
 * 
 *@author fqming (fqming@telenav.cn)
 *@date 2010-11-10
 */
public class RimUiMethodHandler
{
    /**
     * Means that don't handle this ui method at the common handler of this class.
     */
    public static final Object NO_HANDLED = new Object();
    
    /**
     * handle the common native ui method.
     * 
     * @param tnComponent owner of the ui method.
     * @param field native ui component of the ui method.
     * @param eventMethod the name of method.
     * @param args the arguments when invoke the method.
     * @return the return value after invoke the method.
     */
    public static Object callUiMethod(final AbstractTnComponent tnComponent, final Field field, final int eventMethod, final Object[] args)
    {
        switch (eventMethod)
        {
            case AbstractTnComponent.METHOD_FOCUS_LISTENER:
            {
                final ITnFocusChangeListener focusChangeListener = (ITnFocusChangeListener) args[0];
                if (focusChangeListener == null)
                {
                    field.setFocusListener(null);
                }
                else
                {
                    field.setFocusListener(new FocusChangeListener()
                    {
                        
                        public void focusChanged(Field field, int focus)
                        {
                            focusChangeListener.focusChange(tnComponent, focus == FocusChangeListener.FOCUS_GAINED);                            
                        }
                    });
                }
                
                return null;
            }
            case AbstractTnComponent.METHOD_REQUEST_RELAYOUT:
            {
                ((RimUiHelper) AbstractTnUiHelper.getInstance())
                        .runOnUiThread(new Runnable()
                        {
                            public void run()
                            {
                                UiApplication.getUiApplication().relayout();
                            }
                        });
    
                return null;
            }
            case AbstractTnComponent.METHOD_SET_ENABLED:
            {
                field.setEnabled(((Boolean)args[0]).booleanValue());
                
                return null;
            }
            case AbstractTnComponent.METHOD_IS_ENABLED:
            {
                return new Boolean(field.isEnabled());
            }
        }

        return NO_HANDLED;
    }
}
