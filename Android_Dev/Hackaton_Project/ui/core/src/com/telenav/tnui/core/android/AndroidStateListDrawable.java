package com.telenav.tnui.core.android;

/**
 *
 * Copyright 2009 TeleNav, Inc. All rights reserved.
 * AndroidStateListDrawable.java
 *
 */


import android.graphics.drawable.StateListDrawable;

/**
 *@author jwchen (jwchen@telenav.cn)
 *@date Nov 21, 2009
 */
public class AndroidStateListDrawable extends StateListDrawable
{
    int STATE_BLUR = 0;
    int STATE_FOCUS = 1;
    int STATE_PRESSED = 2;
    
    protected boolean onStateChange(int[] stateSet)
    {
        int idx = STATE_BLUR;
        if (stateSet != null && stateSet.length > 0)
        {
            for (int i = 0; i < stateSet.length; i++)
            {
                if(stateSet[i] == android.R.attr.state_pressed || stateSet[i] == android.R.attr.state_selected)
                {
                    idx = STATE_PRESSED;
                    break;
                }
                else if(stateSet[i] == android.R.attr.state_focused)
                {
                    idx = STATE_FOCUS;
                    break;
                }
            }
        }

        if (selectDrawable(idx))
        {
            return true;
        }
        return super.onStateChange(stateSet);
    }
}
