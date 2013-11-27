/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * CitizenRadioButtonContainer.java
 *
 */
package com.telenav.ui.citizen;

import java.util.Vector;

import com.telenav.tnui.core.ITnUiEventListener;
import com.telenav.tnui.core.TnMotionEvent;
import com.telenav.tnui.core.TnUiEvent;
import com.telenav.tnui.widget.TnLinearContainer;

/**
 *@author Albert Ma (byma@telenav.cn)
 *@date 2011-2-15
 */
public class CitizenRadioButtonContainer extends TnLinearContainer implements ITnUiEventListener
{

    protected Vector buttons;
    protected int focusIndex;
    public CitizenRadioButtonContainer(int id, boolean isVertical, int anchor)
    {
        super(id, isVertical, anchor);
        buttons = new Vector();
    }

    public void addRadioButton(CitizenRadioButton button)
    {
        buttons.addElement(button);
        button.setCommandEventListener(this);
        this.add(button);
    }
    
    public void setHightLightIndex(int index)
    {
        if(index >= 0 && index < buttons.size())
        {
            for(int i = 0; i < buttons.size(); i++)
            {
                if(index == i)
                {
                    ((CitizenRadioButton)buttons.elementAt(i)).setIsHighLight(true);
                }
                else
                {
                    ((CitizenRadioButton)buttons.elementAt(i)).setIsHighLight(false);
                }
            }
        }
    }
    public boolean handleUiEvent(TnUiEvent tnUiEvent)
    {
        if(tnUiEvent.getType() == TnUiEvent.TYPE_TOUCH_EVENT
                && tnUiEvent.getMotionEvent().getAction() == TnMotionEvent.ACTION_DOWN
                && tnUiEvent.getComponent() instanceof CitizenRadioButton)
        {
            int index = buttons.indexOf(tnUiEvent.getComponent());
            this.setHightLightIndex(index);
        }
        return super.handleUiEvent(tnUiEvent);
    }
    
}
