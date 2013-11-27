/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * ScreenTouchListener.java
 *
 */
package com.telenav.module.entry;

import android.view.MotionEvent;
import android.view.View;

import com.telenav.module.AppConfigHelper;
import com.telenav.mvc.AbstractBaseView;
import com.telenav.tnui.core.ITnUiEventListener;
import com.telenav.tnui.core.TnCommandEvent;
import com.telenav.tnui.core.TnMotionEvent;
import com.telenav.tnui.core.TnUiEvent;
import com.telenav.ui.citizen.android.AndroidCitizenUiHelper;

/**
 *@author chrbu
 *@date 2012-12-5
*/
public class SecretKeyListener implements View.OnTouchListener,ITnUiEventListener
{
    private int secretKeyMiddleCount;

    private int secretKeyRightCount;

    private long secretKeyExecStartTime;

    final static private int SECRET_KEY_EXEC_TIME = 4000;

    final static private int SECRET_KEY_EXEC_COUNT = 2;

    private AbstractBaseView mvcView;

    private int command;

    public SecretKeyListener(AbstractBaseView mvcView, int command)
    {
        super();
        this.mvcView = mvcView;
        this.command = command;
    }

    public boolean onTouch(View v, MotionEvent event)
    {
        int action = event.getAction();
        switch (action)
        {
            case MotionEvent.ACTION_DOWN:
                TnMotionEvent motionEvent = composeTnMotionEvent(event);
                handleSecretKey(motionEvent);
                break;
            default:
                break;
        }
        return false;
    }

    private TnMotionEvent composeTnMotionEvent(MotionEvent event)
    {
        TnMotionEvent motionEvent = new TnMotionEvent(event.getAction());
        motionEvent.setLocation((int)event.getX(), (int)event.getY());
        motionEvent.setEventTime(event.getEventTime());
        return motionEvent;
    }
    
    
    private void handleSecretKey(TnMotionEvent motionEvent)
    {
        int pointThresHold = AndroidCitizenUiHelper.getPixelsByDip(50);
        
        boolean isMiddleTapped = motionEvent.getX() <= AppConfigHelper.getDisplayWidth() / 2 + pointThresHold
                && motionEvent.getX() >= AppConfigHelper.getDisplayWidth() / 2 - pointThresHold
                && motionEvent.getY() <= pointThresHold;

        boolean isRightTapped = motionEvent.getX() >= AppConfigHelper.getDisplayWidth() - pointThresHold
                && motionEvent.getY() <= pointThresHold;
       
        if (isMiddleTapped)
            secretKeyMiddleCount++;
        else
        {
            if (!isRightTapped)
            {
                secretKeyMiddleCount = 0;
            }
        }
        
        
        if(secretKeyMiddleCount>=SECRET_KEY_EXEC_COUNT && isRightTapped)
            secretKeyRightCount++;
        else
            secretKeyRightCount=0;

        if (secretKeyMiddleCount == 1)
        {
            secretKeyExecStartTime = System.currentTimeMillis();
        }

        if( System.currentTimeMillis() - secretKeyExecStartTime > SECRET_KEY_EXEC_TIME)
        {
            secretKeyExecStartTime =System.currentTimeMillis();
        }
        
        if (secretKeyMiddleCount >= SECRET_KEY_EXEC_COUNT && secretKeyRightCount >= SECRET_KEY_EXEC_COUNT
                && (System.currentTimeMillis() - secretKeyExecStartTime) <= SECRET_KEY_EXEC_TIME)
        {
            secretKeyMiddleCount = 0;
            secretKeyExecStartTime = 0;
            
            TnUiEvent uiEvent = new TnUiEvent(TnUiEvent.TYPE_COMMAND_EVENT, null);
            uiEvent.setCommandEvent(new TnCommandEvent(command));
            mvcView.handleUiEvent(uiEvent);
        }

    }

    @Override
    public boolean handleUiEvent(TnUiEvent tnUiEvent)
    {
        int action = tnUiEvent.getMotionEvent().getAction();
        switch (action)
        {
            case MotionEvent.ACTION_DOWN:
                handleSecretKey(tnUiEvent.getMotionEvent());
                break;
            default:
                break;
        }
        return false;
    }
    
}