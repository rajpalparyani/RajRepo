/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * FrogTextArea.java
 *
 */
package com.telenav.ui.frogui.widget;

import com.telenav.tnui.core.TnPrivateEvent;
import com.telenav.tnui.core.TnUiEvent;
import com.telenav.tnui.graphics.AbstractTnGraphics;
import com.telenav.tnui.widget.TnTextField;
import com.telenav.util.PrimitiveTypeCache;

/**
 *@author Albert Ma (byma@telenav.cn)
 *@date 2010-8-9
 */
public class FrogTextArea extends TnTextField
{

    /**
     * Contructor of FrogTextArea
     * 
     * @param id
     * @param initText initial text
     * @param hint hint of input area.
     * @param linesSize line size of text area
     */
    public FrogTextArea(int id, String initText, String hint, int linesSize)
    {
        super(id, initText, hint, linesSize);

        setTextGravity(AbstractTnGraphics.LEFT | AbstractTnGraphics.TOP);
    }

    protected boolean handleUiEvent(TnUiEvent tnUiEvent)
    {
        int eventType = tnUiEvent.getType();
        switch (eventType)
        {
            case TnUiEvent.TYPE_PRIVATE_EVENT:
            {
                if (tnUiEvent.getPrivateEvent().getAction() == TnPrivateEvent.ACTION_TEXT_CHANGE)
                {
                    String text = this.getText();

                    if (this.textChangeListener != null)
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

    /**
     * Sets the horizontal alignment of the text and the vertical gravity that will be used when there is extra space in
     * the Text Area beyond what is required for the text itself.
     * 
     * @param gravity AbstractTnGraphics.LEFT, AbstractTnGraphics.TOP etc..
     */
    public void setTextGravity(int gravity)
    {
        nativeUiComponent.callUiMethod(METHOD_SET_TEXT_GRAVITY, new Object[]
        { PrimitiveTypeCache.valueOf(gravity) });
    }
}
