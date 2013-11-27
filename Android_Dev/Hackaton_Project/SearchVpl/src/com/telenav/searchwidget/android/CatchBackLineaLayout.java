/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * CatchBackLineaLayout.java
 *
 */
package com.telenav.searchwidget.android;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.LinearLayout;

/**
 *@author jwchen (jwchen@telenav.cn)
 *@date Sep 9, 2010
 */
public class CatchBackLineaLayout extends LinearLayout
{
    private BackEventListener backEventListener;
    
    public CatchBackLineaLayout(Context context)
    {
        super(context);
    }
    
    public CatchBackLineaLayout(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }
    
    public BackEventListener getBackEventListener()
    {
        return backEventListener;
    }

    public void setBackEventListener(BackEventListener backEventListener)
    {
        this.backEventListener = backEventListener;
    }

    public boolean dispatchKeyEventPreIme(KeyEvent event)
    {
       if(event.getKeyCode() == KeyEvent.KEYCODE_BACK)
       {
           if(backEventListener != null)
           {
               if(event.getAction() == KeyEvent.ACTION_UP)
               {
                   backEventListener.handleBackEvent(true);
               }
               else
               {
                   backEventListener.handleBackEvent(false);
               }
           }
           return true;
       }
       
       return super.dispatchKeyEventPreIme(event);
    }

}

interface BackEventListener
{
    public void handleBackEvent(boolean isUp);
}
