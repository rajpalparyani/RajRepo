/**
 *
 * Copyright 2013 TeleNav, Inc. All rights reserved.
 * ExitNavSessionConfirmer.java
 *
 */
package com.telenav.app.android;


import android.content.Context;
import com.telenav.mvc.AbstractController;
import com.telenav.mvc.ICommonConstants;

/**
 *@author yning
 *@date 2013-1-28
 */
public class ExitNavSessionConfirmer
{
    boolean isShowingExitNavConfirm = false;
    
    private Object exitNavConfirmMutex = new Object();
    
    private INavSessionEndListener listener;
    
    private static ExitNavSessionConfirmer instance = new ExitNavSessionConfirmer();
    
    private boolean isCancel = true;
    
    public static ExitNavSessionConfirmer getInstance()
    {
        return instance;
    }

    public void showExitNavSessionConfirm(Context context, INavSessionEndListener listener)
    {
        this.listener = listener;
        synchronized(exitNavConfirmMutex)
        {
            if(isShowingExitNavConfirm)
            {
                return;
            }
            
            isShowingExitNavConfirm = true;
            isCancel = true;
            
            AbstractController controller = AbstractController.getCurrentController();
            controller.handleModelEvent(ICommonConstants.EVENT_MODEL_EXIT_NAV_CONFIRM);
        }
    }
    
    public void endNav()
    {
        isCancel = false;
        if (ExitNavSessionConfirmer.this.listener != null)
        {
            ExitNavSessionConfirmer.this.listener.onNavSessionEnd();
        }
    }
    
    public boolean isShowingExitNavConfirm()
    {
        return isShowingExitNavConfirm;
    }
    
    public void closePopup()
    {
        synchronized (exitNavConfirmMutex)
        {
            if (isCancel)
            {
                if (ExitNavSessionConfirmer.this.listener != null)
                {
                    ExitNavSessionConfirmer.this.listener.onCancel();
                }
            }
            isShowingExitNavConfirm = false;
        }
    }
    
    public void hide()
    {
        synchronized (exitNavConfirmMutex)
        {
            AbstractController controller = AbstractController.getCurrentController();
            controller.handleModelEvent(ICommonConstants.EVENT_MODEL_HIDE_NAV_CONFIRM);
        }
    }

    public interface INavSessionEndListener
    {
        public void onNavSessionEnd();
        
        public void onCancel();
    }
}
