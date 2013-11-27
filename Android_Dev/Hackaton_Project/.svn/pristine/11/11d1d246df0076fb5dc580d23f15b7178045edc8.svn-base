/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * ShortcutCreator.java
 *
 */
package com.telenav.app.android;

import android.content.Context;
import android.content.SharedPreferences;

import com.telenav.app.IApplicationListener;
import com.telenav.app.TeleNavDelegate;
import com.telenav.app.android.ExitNavSessionConfirmer.INavSessionEndListener;
import com.telenav.data.dao.misc.DaoManager;
import com.telenav.module.nav.NavSdkNavEngine;
import com.telenav.mvc.AbstractController;
import com.telenav.mvc.ICommonConstants;

/**
 * @author htli
 * @date 2012-9-13
 */
public class ShortcutManager implements IApplicationListener
{
    static final public String ACTIVITY_NAME_MYMAP     = ".ShortcutMapsActivity";
    static final public String ACTIVITY_NAME_MYPLACE   = ".ShortcutPlaceActivity";
    static final public String ACTIVITY_NAME_MYDRIVE   = ".ShortcutDriveActivity";
    
    static final private String NEED_CREATE_SHORTCUT = "CreateShortcutFlag";
    static final private String PREFERENCE_SHORTCUT = "PreferenceShortcut";
    
    static final public String ACTION_SHORTCUT = "com.telenav.intent.action.SHORTCUT";
    static final public String SHORTCUT_REQUEST = "com.telenav.shortcut.data";
    static final public String SHORTCUT_MYMAP = "com.telenav.intent.action.MYMAP";
    static final public String SHORTCUT_MYDRIVE = "com.telenav.intent.action.MYDRIVE";
    static final public String SHORTCUT_MYPLACE = "com.telenav.intent.action.MYPLACE";
    
    private static ShortcutManager instance = new ShortcutManager();
    
    private Context context;
    private boolean isFromShortcut;
    private String action;
	
    private boolean isInShortcutScreen;

    private ShortcutManager()
    {
        TeleNavDelegate.getInstance().registerApplicationListener(this);
    }
    
    public static ShortcutManager getInstance()
    {
        return instance;
    }
    
    
    public boolean needCreateShortcut()
    {
        SharedPreferences shortcutPreferences = context.getSharedPreferences(PREFERENCE_SHORTCUT, Context.MODE_PRIVATE);
        return shortcutPreferences.getBoolean(NEED_CREATE_SHORTCUT, true);
    }
    
    public void setNeedCreateShortcutFlag(boolean needCreateShortcut)
    {
        SharedPreferences shortcutPreferences = context.getSharedPreferences(PREFERENCE_SHORTCUT, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shortcutPreferences.edit();
        editor.putBoolean(NEED_CREATE_SHORTCUT, needCreateShortcut);
        editor.commit();
    }
    
    @Override
    public void appDeactivated(String[] params)
    {
        isFromShortcut = false;
    }

    @Override
    public void appActivated(String[] params)
    {
        
        if (!isFromShortcut
                || !DaoManager.getInstance().getBillingAccountDao().isLogin()
                || !DaoManager.getInstance().getResourceBarDao().isResourceSyncFinish())
        {
            return;
        }
        
        //if it is do navigation, simply call app from background when click MyNav icon.
        if (SHORTCUT_MYDRIVE.equals(getAction()) && NavSdkNavEngine.getInstance().isRunning())
        {
            fetchAction();
            ExitNavSessionConfirmer.getInstance().hide();
            return;
        }
        
        boolean isNavRunning = NavSdkNavEngine.getInstance().isRunning();
        if(isNavRunning)
        {
            Context context = AndroidPersistentContext.getInstance().getContext();
            ExitNavSessionConfirmer.getInstance().showExitNavSessionConfirm(context, new INavSessionEndListener()
            {
                @Override
                public void onNavSessionEnd()
                {
                    startShortcutDelegate();
                }
                
                @Override
                public void onCancel()
                {
                    fetchAction();
                    isFromShortcut = false;
                }
            });
        }
        else
        {
            startShortcutDelegate();
        }
    }
    
    protected void startShortcutDelegate()
    {
        setInShortcutScreen(true);
        
        //stop navigation first
        AbstractController controller = AbstractController.getCurrentController();
        controller.handleModelEvent(ICommonConstants.EVENT_MODEL_EXIT_NAV_FROM_SHORTCUT);
    }
  
    public boolean isFromShortcut()
    {
        return isFromShortcut;
    }

    public void setFromShortcut(boolean isFromShortcut)
    {
        this.isFromShortcut = isFromShortcut;
    }
    
    public String getAction()
    {
        return action;
    }

    public void setAction(String action)
    {
        this.action = action;
    }
    
    public String fetchAction()
    {
        String tmp = action;
        action = null;
        return tmp;
    }
    
    public boolean isInShortcutScreen()
    {
        return isInShortcutScreen;
    }

    public void setInShortcutScreen(boolean isInShortcutScreen)
    {
        this.isInShortcutScreen = isInShortcutScreen;
    }
    
    public void setContext(Context context)
    {
        this.context = context;
    } 


}
