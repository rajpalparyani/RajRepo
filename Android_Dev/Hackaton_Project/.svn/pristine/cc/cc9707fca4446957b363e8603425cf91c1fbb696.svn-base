/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * AndroidPlatformIniter.java
 *
 */
package com.telenav.searchwidget.app.android;

import com.telenav.io.TnIoManager;
import com.telenav.io.android.AndroidIoManager;
import com.telenav.location.TnLocationManager;
import com.telenav.location.android.AndroidLocationManager;
import com.telenav.network.TnNetworkManager;
import com.telenav.network.android.AndroidXmlNetworkManager;
import com.telenav.nio.TnNioManager;
import com.telenav.nio.android.AndroidNioManager;
import com.telenav.persistent.TnPersistentManager;
import com.telenav.persistent.android.AndroidPersistentManager;
import com.telenav.searchwidget.app.AbstractPlatformIniter;
import com.telenav.tnui.core.android.AndroidUiHelper;
import com.telenav.tnui.graphics.AbstractTnGraphicsHelper;

/**
 *@author fqming (fqming@telenav.cn)
 *@date Jul 20, 2010
 */
public class AndroidPlatformIniter extends AbstractPlatformIniter
{
    public void initIo()
    {
        if(TnIoManager.getInstance() == null)
        {
            TnIoManager.init(new AndroidIoManager(AndroidPersistentContext.getInstance().getContext()));
        }
    }
    
    public void initNio()
    {
        if (TnNioManager.getInstance() == null)
        {
            TnNioManager.init(new AndroidNioManager());
        }
    }

    public void initLocation()
    {
        if(TnLocationManager.getInstance() == null)
        {
            TnLocationManager.init(new AndroidLocationManager(AndroidPersistentContext.getInstance().getContext()));
        }
    }

    public void initNetwork()
    {
        if(TnNetworkManager.getInstance() == null)
        {
            TnNetworkManager.init(new AndroidXmlNetworkManager());
        }
    }

    public void initPersistent()
    {
        if(TnPersistentManager.getInstance() == null)
        {
            TnPersistentManager.init(new AndroidPersistentManager(), AndroidPersistentContext.getInstance());
        }
    }

    public void initUi()
    {
        if(AbstractTnGraphicsHelper.getInstance() == null)
        {
            AbstractTnGraphicsHelper.init(new AndroidUiHelper());
        }
    }

    public void initNetworkConnectivityHandler()
    {
//        AndroidNetworkConnectivityHandler handler = new AndroidNetworkConnectivityHandler();
//        handler.init(AndroidPersistentContext.getInstance().getContext());
//        TnNetworkConnectivityHandler.init(handler);
    }
}
