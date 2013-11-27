/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * NavSdkEngine.java
 *
 */
package com.telenav.app.android.jni;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.json.tnme.JSONException;
import org.json.tnme.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.SystemClock;

import com.telenav.app.TnBacklightManagerImpl;
import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.dao.misc.SimpleConfigDao;
import com.telenav.data.serverproxy.impl.navsdk.helper.NavSdkApplicationLifecycleProxyHelper;
import com.telenav.data.serverproxy.impl.navsdk.helper.NavSdkLocationProviderProxyHelper;
import com.telenav.data.serverproxy.impl.navsdk.helper.NavSdkMapDownloadProxyHelper;
import com.telenav.data.serverproxy.impl.navsdk.helper.NavSdkMapProxyHelper;
import com.telenav.data.serverproxy.impl.navsdk.helper.NavSdkNavigationProxyHelper;
import com.telenav.data.serverproxy.impl.navsdk.helper.NavSdkPoiProxyHelper;
import com.telenav.data.serverproxy.impl.navsdk.helper.NavSdkUserManagementProxyHelper;
import com.telenav.data.serverproxy.impl.navsdk.service.NavsdkAudioService;
import com.telenav.data.serverproxy.impl.navsdk.service.NavsdkBacklightService;
import com.telenav.data.serverproxy.impl.navsdk.service.NavsdkMisLogService;
import com.telenav.data.serverproxy.impl.navsdk.service.NavsdkNetworkService;
import com.telenav.data.serverproxy.impl.navsdk.service.NavsdkSensorService;
import com.telenav.data.serverproxy.impl.navsdk.service.NavsdkUserManagementService;
import com.telenav.modules.Foundation;
import com.telenav.navsdk.NavigationSDK;
import com.telenav.navsdk.NavsdkFileUtil;
import com.telenav.navsdk.eventmanager.EventBus;
import com.telenav.tnui.core.AbstractTnUiHelper;

/**
 * @author Casper(pwang@telenav.cn)
 * @date 2011-12-20
 */
public class NavSdkEngine
{
    static
    {
        System.loadLibrary("navigationsdk");
    }

    private static NavSdkEngine instance;

    protected Context context;

    private boolean isRunning;

    private boolean isInit;

    public NavSdkEngine(Object context)
    {
        this.context = (Context) context;

        SharedPreferences settings = this.context.getSharedPreferences("app.ini",
            Activity.MODE_PRIVATE);
        copyResource(settings);

        init();
    }

    public static NavSdkEngine getInstance()
    {
        return instance;
    }

    public static void init(NavSdkEngine navSdkEngine)
    {
        instance = navSdkEngine;
    }

    public boolean isRunning()
    {
        return isRunning;
    }
    
    private void copyResource(SharedPreferences settings)
    {
        NavsdkFileUtil.init(context);
        boolean isSetResourcePathEnable = ((DaoManager) DaoManager.getInstance()).getSimpleConfigDao().getBoolean(
            SimpleConfigDao.KEY_SET_RESOURCE_PATH_ENABLE);
        if (isSetResourcePathEnable)
        {
            NavsdkFileUtil.copyAssetFile2Resource("secretSettings_respath.json", "secretSettings.json", true);
        }
        else
        {
            NavsdkFileUtil.copyAssetFile2Resource("secretSettings.json");
        }
        
        checkExternalDirectory();
        
        NavsdkFileUtil.copyResourceFile2CacheSubDir("secretSettings.json", "config");
        NavsdkFileUtil.copyAssetFolder2Resource("ttsparser");
    }

    protected void checkExternalDirectory()
    {
        boolean isOldPathAvailable = false;
        String oldPath = DaoManager.getInstance().getSimpleConfigDao().getString(SimpleConfigDao.KEY_MAP_DOWNLOAD_DIR);
        String systemPath = NavsdkFileUtil.getNavsdkMapDownloadDirectory();
        if(oldPath != null && oldPath.trim().length() > 0)
        {
            File file = new File(oldPath);
            if(file.exists())
            {
                isOldPathAvailable = true;
            }
        }
        
        String currentPath = null;
        if(isOldPathAvailable)
        {
            currentPath = oldPath;
        }
        else
        {
            if(systemPath != null && systemPath.trim().length() > 0)
            {
                currentPath = systemPath;
            }
            else if(oldPath != null && oldPath.trim().length() > 0)
            {
                currentPath = oldPath;
            }
        }
        
        if(currentPath != null && currentPath.trim().length() > 0)
        {
            DaoManager.getInstance().getSimpleConfigDao().put(SimpleConfigDao.KEY_MAP_DOWNLOAD_DIR, currentPath);
            DaoManager.getInstance().getSimpleConfigDao().store();
            byte[] data = NavsdkFileUtil.readFile(NavsdkFileUtil.getNavsdkResourcePath(), "secretSettings.json");
            
            if(data == null || data.length <= 0)
            {
                return;
            }
            
            JSONObject jsonObject = null;
            try
            {
                jsonObject = new JSONObject(new String(data));
                
                jsonObject.put("MapDownloadDataPath", currentPath);
            }
            catch (JSONException e)
            {
                e.printStackTrace();
                jsonObject = null;
            }
            
            if(jsonObject != null)
            {
                data = jsonObject.toString().getBytes();
                NavsdkFileUtil.deleteFile(NavsdkFileUtil.getNavsdkResourcePath() + "/secretSettings.json");
                NavsdkFileUtil.createFile(NavsdkFileUtil.getNavsdkResourcePath(), "secretSettings.json", data);
            }
        }
    }
    
    protected void copyTo(InputStream input, FileOutputStream output) throws IOException
    {
        byte[] datas = new byte[input.available()];
        if (input.read(datas) > 0)
        {
            output.write(datas);
        }
        output.close();
        input.close();
    }

    private void init()
    {
        if (isInit)
        {
            return;
        }
        isInit = true;

        // this.connector = new Connector(EventBus.getMain());

        TnBacklightManagerImpl.getInstance();

        // init protocol adapter
        NavSdkApplicationLifecycleProxyHelper.init(EventBus.getMain());
        NavSdkLocationProviderProxyHelper.init(EventBus.getMain());
        NavSdkMapDownloadProxyHelper.init(EventBus.getMain());
        NavSdkMapProxyHelper.init(EventBus.getMain());
        NavSdkNavigationProxyHelper.init(EventBus.getMain());
        NavSdkPoiProxyHelper.init(EventBus.getMain());
        NavSdkUserManagementProxyHelper.init(EventBus.getMain());
    }
    
    public void start()
    {
        if (isRunning)
        {
            return;
        }

        isRunning = true;
        NavsdkAudioService.init(EventBus.getMain());
        NavsdkBacklightService.init(EventBus.getMain());
        NavsdkMisLogService.init(EventBus.getMain());
        NavsdkNetworkService.init(EventBus.getMain(), context);
        NavsdkSensorService.init(EventBus.getMain());
        NavsdkUserManagementService.init(EventBus.getMain());
        startEventBus();
    }
    
    public void stop()
    {
        if(!isRunning)
        {
            return;
        }
        isRunning = false;
        destory();
    }

    private void startEventBus()
    {
        ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).runOnUiThread(new Runnable()
        {
            public void run()
            {
                NavigationSDK.getInstance(context);
            }
        });
        SystemClock.sleep(200);
    }

    public void destory()
    {
        Foundation.destroyFoundationModule();
    }
}
