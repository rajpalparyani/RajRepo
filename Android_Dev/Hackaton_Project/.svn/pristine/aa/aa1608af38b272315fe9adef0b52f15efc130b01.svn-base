/**
 *
 * Copyright 2013 TeleNav, Inc. All rights reserved.
 * AccountChangeListener.java
 *
 */
package com.telenav.module.dashboard;

import java.util.Vector;

import com.telenav.tnui.core.AbstractTnUiHelper;

/**
 *@author yning
 *@date 2013-4-3
 */
public class AccountChangeListener
{
    private static AccountChangeListener instance = new AccountChangeListener();
    
    private Vector<IAccountTypeChangeListener> listeners = new Vector<IAccountTypeChangeListener>();
    
    public static final AccountChangeListener getInstance()
    {
        return instance;
    }
    
    public void accountChanged()
    {
        ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).runOnUiThread(new Runnable()
        {
            public void run()
            {
                AdJuggerManager.getInstance().reload();
                for (int i = 0; i < listeners.size(); i++)
                {
                    IAccountTypeChangeListener listener = listeners.elementAt(i);
                    listener.onAccountTypeChanged();
                }
            }
        });
    }
    
    public void addListener(IAccountTypeChangeListener listener)
    {
        listeners.removeElement(listener);
        listeners.addElement(listener);
    }
    
    public void removeListener(IAccountTypeChangeListener listener)
    {
        listeners.removeElement(listener);
    }
}
