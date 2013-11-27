/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * FrogTextField.java
 *
 */
package com.telenav.ui.frogui.widget;

import com.telenav.tnui.core.TnPrivateEvent;
import com.telenav.tnui.core.TnUiEvent;
import com.telenav.tnui.widget.TnTextField;
import com.telenav.util.PrimitiveTypeCache;

/**
 * Frog style TextField, display as a input box.
 * 
 *@author jshjin (jshjin@telenav.cn)
 *@date 2010-7-13
 */
public class FrogTextField extends TnTextField
{
    protected int inputBoxWidth;
    protected int inputBoxHeight;
    protected boolean isAddrConstraint = false;
    
    /**
     * construct a FrogTextField
     * 
     * @param initText
     * @param maxSize
     * @param keypadConstraints
     * @param hint
     * @param id
     */
    public FrogTextField(String initText, int maxSize,
            int keypadConstraints, String hint, int id)
    {
        super(id, initText, hint, maxSize, keypadConstraints);
        if((keypadConstraints & ADDRESS) == ADDRESS)
        {
            isAddrConstraint = true;
        }
    }
    
    /**
     * set keypad constraints
     * @param constraints
     */
    public void setConstraints(int constraints)
    {
        super.setConstraints(constraints);
        if((this.constraints & ADDRESS) == ADDRESS)
        {
            isAddrConstraint = true;
        }
    }

    protected void changeTextFilter()
    {
        this.nativeUiComponent.callUiMethod(METHOD_SET_KEY_LISTENER, new Object[]{PrimitiveTypeCache.valueOf(textType)});

        switch(constraints)
        {
            case ANY:
                break;
            case NUMERIC:
            case ADDRESS:
            {
                nativeUiComponent.callUiMethod(METHOD_SET_KEY_LISTENER, new Object[]{PrimitiveTypeCache.valueOf(ADDRESS)});
                break;
            }
            case PHONENUMBER:
            {
                nativeUiComponent.callUiMethod(METHOD_SET_KEY_LISTENER, new Object[]{PrimitiveTypeCache.valueOf(PHONENUMBER)});
                break;
            }
            case UNEDITABLE:
            {
                nativeUiComponent.callUiMethod(METHOD_SET_ENABLE, new Object[]{PrimitiveTypeCache.valueOf(false)});
                break;
            }
        }
    }

    /**
     * close the virtual keyboard
     */
    public void closeVirtualKeyBoard()
    {
        nativeUiComponent.callUiMethod(METHOD_CLOSE_VIRTUAL_KEYBOARD, null);
    }
    
    /**
     * show the virtual keyboard.
     */
    public void showVirtualKeyBoard()
    {
        nativeUiComponent.callUiMethod(METHOD_SHOW_VIRTUAL_KEYBOARD, null);
    }
    
    protected void handleConstraint(String text)
    {
        if(isAddrConstraint)
        {
            if(text.length() > 0)
            {
                boolean isAllDigit = true;
                for(int i = text.length() - 1; i >= 0; i--)
                {
                    if(!isDigit(text.charAt(i)))
                    {
                        if(this.getConstraints() != TnTextField.ANY)
                        {
                            this.setConstraints(TnTextField.ANY);
                        }
                        
                        isAllDigit = false;
                        break;
                    }
                }
                
                if(isAllDigit && this.getConstraints() != TnTextField.ADDRESS)
                {
                    this.setConstraints(TnTextField.ADDRESS);
                }
            }
            else
            {
                this.setConstraints(TnTextField.ADDRESS);
            }
        }
    }
    
    /**
     * Check the Ascii code to see if it is digit
     * 
     * @param c
     * @return
     */
    protected static boolean isDigit(char c)
    {
        int diff = c - '0';
        if (diff >= 0 && diff < 10)
        {
            return true;
        }
        return false;
    }
    
    protected boolean handleUiEvent(TnUiEvent tnUiEvent)
    {
        int eventType = tnUiEvent.getType();
        switch(eventType)
        {
            case TnUiEvent.TYPE_PRIVATE_EVENT:
            {
                if(tnUiEvent.getPrivateEvent().getAction() == TnPrivateEvent.ACTION_TEXT_CHANGE)
                {
                    String text = this.getText();
                    handleConstraint(text);
                    
                    if(this.textChangeListener != null)
                        textChangeListener.onTextChange(this, text);
                    return true;
                }
                break;
            }
            default:
                break;
        }
        return false;
    }
}
