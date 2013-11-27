/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * PluginManager.java
 *
 */
package com.telenav.sdk.plugin;

import java.util.Hashtable;

import android.content.Context;

import com.telenav.app.IApplicationListener;
import com.telenav.app.TeleNavDelegate;
import com.telenav.app.android.AndroidPersistentContext;
import com.telenav.app.android.ExitNavSessionConfirmer;
import com.telenav.app.android.ExitNavSessionConfirmer.INavSessionEndListener;
import com.telenav.data.dao.misc.DaoManager;
import com.telenav.dwf.aidl.DwfLogger;
import com.telenav.logger.Logger;
import com.telenav.module.nav.NavSdkNavEngine;
import com.telenav.mvc.AbstractController;
import com.telenav.mvc.ICommonConstants;
import com.telenav.mvc.Parameter;
import com.telenav.sdk.maitai.IMaiTai;

/**
 *@author qli
 *@date 2011-2-24
 */
public class PluginManager implements IApplicationListener
{
    private static PluginManager instance = new PluginManager();
    private boolean isFromPlugin = false;
    private boolean isPluginExit = false;
    private boolean startContactsFromApp = false;
    private Hashtable requestParam = null;
    
    private PluginManager()
    {
        TeleNavDelegate.getInstance().registerApplicationListener(this);
    }
    
    public static PluginManager getInstance()
    {
        return instance;
    }
    
    protected void initPlugin()
    {
        startContactsFromApp = false;
    }
    
    public void setFromPlugin(boolean fromPlugin)
    {
        isFromPlugin = fromPlugin;
        isPluginExit = fromPlugin;
        if( isFromPlugin )
        {
            initPlugin();
        }
    }
    
    
    public void setCallFromExternal(Hashtable table)
    {
        setFromPlugin(true);
        PluginDataProvider.getInstance().initialize(table);
        requestParam = new Hashtable();
        requestParam.put(IMaiTai.KEY_SELECTED_MENU_ITEM, PluginDataProvider.getInstance().getSelectedAction());
        requestParam.put(IMaiTai.KEY_SELECTED_ADDRESS, PluginDataProvider.getInstance().getStop());
        String item = PluginDataProvider.getInstance().getSearchTerm();
        if ( item != null )
        {
            requestParam.put(IMaiTai.KEY_SEARCH_ITEM, item);
        }
        startPlugin();
    }
    
    public void setCallFromDWF(Hashtable table)
    {
        requestParam = table;
        
        setFromPlugin(true);
        
        Parameter data = null;
        if (table != null)
        {
            data = new Parameter();
            data.put(ICommonConstants.KEY_O_PLUGIN_REQUEST, table);
        }
        
        if(TeleNavDelegate.getInstance().isJumb2Background())
        {
            table.put(IMaiTai.KEY_DWF_EXIT_APP, true);
        }
        
        AbstractController controller = AbstractController.getCurrentController();
        
//        Logger.dwfLog(DwfLogger.INFO, "Which controllre to break ? " + controller);
        
        if (controller != null)
        {
            controller.activate(ICommonConstants.EVENT_CONTROLLER_FROM_PLUGIN_REQUEST, data);
            controller.handleModelEvent(ICommonConstants.EVENT_MODEL_DWF_FROM_PLUGIN);
            controller = null;
            isFromPlugin = false;
        }
        else
        {
            table.put(IMaiTai.KEY_DWF_EXIT_APP, true);
        }
    }
    
    /**
     * start contacts whether shutdown app.
     * @param bool false-shutdown app, true-no.
     */
    public void setStartContactsFromApp(boolean bool)
    {
        startContactsFromApp = bool;
    }
    
    public boolean isFromPlugin()
    {
        return isFromPlugin;
    }
    
    public boolean isPluginExit()
    {
        return isPluginExit;
    }
    
    public void setRequestData(Hashtable request)
    {
        requestParam = request;
    }
    
    public void appDeactivated(String[] params)
    {
    }

    public void appActivated(String[] params)
    {
        if( isFromPlugin )
        {
            if( DaoManager.getInstance().getBillingAccountDao().isLogin() )
            {
                startPlugin();
            }
        }
    }
    
    public void startPlugin()
    {
        if (startContactsFromApp)
        {
            return;
        }
        
        if (requestParam != null
                && (requestParam.containsKey(IMaiTai.KEY_DWF_SESSION_ID) || requestParam.containsKey(IMaiTai.KEY_DWF_TINY_URL)))
        {
            this.setCallFromDWF(requestParam);
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
                    startPluginDelegate();
                }
                
                @Override
                public void onCancel()
                {
                    PluginDataProvider.getInstance().clear();
                    isFromPlugin = false;
                }
            });
        }
        else
        {
            startPluginDelegate();
        }
    }
    
    protected void startPluginDelegate()
    {
        Parameter data = null;
        if (requestParam != null)
        {
            data = new Parameter();
            data.put(ICommonConstants.KEY_O_PLUGIN_REQUEST, requestParam);
        }
        
        AbstractController controller = AbstractController.getCurrentController();
        
        if (controller != null)
        {
            controller.activate(ICommonConstants.EVENT_CONTROLLER_FROM_PLUGIN_REQUEST, data);
            controller.handleModelEvent(ICommonConstants.EVENT_MODEL_EXIT_NAV_FROM_PLUGIN);
            controller = null;
        }
        
        isFromPlugin = false;
    }
    
    public void finish()
    {
        requestParam = null;
        TeleNavDelegate.getInstance().exitApp(true, null);
    }
    


}
