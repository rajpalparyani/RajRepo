/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * J2seUiMethodHandler.java
 *
 */
package com.telenav.tnui.core.j2se;

import java.awt.Component;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.core.ITnFocusChangeListener;

/**
 *@author fqming (fqming@telenav.cn)
 *@date Aug 3, 2010
 */
public class J2seUiMethodHandler
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
    public static Object callUiMethod(final AbstractTnComponent tnComponent, final Component component, final int eventMethod, final Object[] args)
    {
        switch (eventMethod)
        {
            case AbstractTnComponent.METHOD_FOCUS_LISTENER:
            {
                final ITnFocusChangeListener focusChangeListener = (ITnFocusChangeListener) args[0];
                if (focusChangeListener == null)
                {
                    FocusListener[] listeners = component.getFocusListeners();
                    if(listeners != null)
                    {
                        for(int i = 0; i < listeners.length; i++)
                        {
                            component.removeFocusListener(listeners[i]);
                        }
                    }
                }
                else
                {
                    component.addFocusListener(new FocusListener()
                    {
                        public void focusLost(FocusEvent e)
                        {
                            focusChangeListener.focusChange(tnComponent, false);
                        }
                        
                        public void focusGained(FocusEvent e)
                        {
                            focusChangeListener.focusChange(tnComponent, true);
                        }
                    });
                }
                
                return null;
            }
            case AbstractTnComponent.METHOD_REQUEST_RELAYOUT:
            {
                component.invalidate();
                return null;
            }
            case AbstractTnComponent.METHOD_SET_ENABLED:
            {
                component.setEnabled(((Boolean)args[0]).booleanValue());
                
                return null;
            }
            case AbstractTnComponent.METHOD_IS_ENABLED:
            {
                return new Boolean(component.isEnabled());
            }
        }

        return NO_HANDLED;
    }
}
