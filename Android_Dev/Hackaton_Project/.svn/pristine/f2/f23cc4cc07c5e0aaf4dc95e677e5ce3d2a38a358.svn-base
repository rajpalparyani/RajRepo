/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * AndroidPlatformIniter.java
 *
 */
package com.telenav.app.android;

import com.telenav.app.AbstractContactProvider;
import com.telenav.app.AbstractPlatformIniter;
import com.telenav.app.TnSdLogCollector;
import com.telenav.app.android.jni.NativeSpeexEncoder;
import com.telenav.app.android.jni.NavSdkEngine;
import com.telenav.audio.codec.AbstractEncoder;
import com.telenav.backlight.TnBacklightManager;
import com.telenav.backlight.android.AndroidBacklightManager;
import com.telenav.io.TnIoManager;
import com.telenav.io.android.AndroidIoManager;
import com.telenav.location.TnLocationManager;
import com.telenav.location.android.AndroidLocationManager;
import com.telenav.media.TnMediaManager;
import com.telenav.media.android.AndroidMediaManager;
import com.telenav.module.location.LocationProvider;
import com.telenav.module.location.android.AndroidNativeLocationProvider;
import com.telenav.module.location.android.GoogleServiceLocationProvider;
import com.telenav.network.TnNetworkManager;
import com.telenav.network.android.AndroidNetworkManager;
import com.telenav.nio.TnNioManager;
import com.telenav.nio.android.AndroidNioManager;
import com.telenav.persistent.TnPersistentManager;
import com.telenav.persistent.android.AndroidPersistentManager;
import com.telenav.radio.TnRadioManager;
import com.telenav.radio.android.AndroidRadioManager;
import com.telenav.res.ResourceManager;
import com.telenav.telephony.TnBatteryManager;
import com.telenav.telephony.TnSensorManager;
import com.telenav.telephony.TnTelephonyManager;
import com.telenav.telephony.android.AndroidBatteryManager;
import com.telenav.telephony.android.AndroidSensorManager;
import com.telenav.telephony.android.AndroidTelephonyManager;
import com.telenav.tnui.core.AbstractTnUiBinder;
import com.telenav.tnui.core.TnUiContext;
import com.telenav.tnui.core.android.AndroidUiHelper;
import com.telenav.tnui.graphics.AbstractTnGraphicsHelper;
import com.telenav.ui.citizen.android.AndroidCitizenUiBinder;

/**
 *@author fqming (fqming@telenav.cn)
 *@date Jul 20, 2010
 */
public class AndroidPlatformIniter extends AbstractPlatformIniter
{
    public void initBacklight()
    {
        if(TnBacklightManager.getInstance() == null)
        {
            TnBacklightManager.init(new AndroidBacklightManager(AndroidPersistentContext.getInstance().getContext()));
        }
    }

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

    public void initMedia()
    {
        if(TnMediaManager.getInstance() == null)
        {
            TnMediaManager.init(new AndroidMediaManager(AndroidPersistentContext.getInstance().getContext()));
        }
    }

    public void initNetwork()
    {
        if(TnNetworkManager.getInstance() == null)
        {
            TnNetworkManager.init(new AndroidNetworkManager());
        }
    }

    public void initPersistent()
    {
        if(TnPersistentManager.getInstance() == null)
        {
            TnPersistentManager.init(new AndroidPersistentManager(), AndroidPersistentContext.getInstance());
        }
    }

    public void initRadio()
    {
        if(TnRadioManager.getInstance() == null)
        {
            TnRadioManager.init(new AndroidRadioManager(AndroidPersistentContext.getInstance().getContext()));
        }
    }

    public void initSensor()
    {
        if(TnSensorManager.getInstance() == null)
        {
            TnSensorManager.init(new AndroidSensorManager(AndroidPersistentContext.getInstance().getContext()));
        }        
    }
    
    public void initTelephony()
    {
        if(TnTelephonyManager.getInstance() == null)
        {
            TnTelephonyManager.init(new AndroidTelephonyManager(AndroidPersistentContext.getInstance().getContext()));
        }
    }

    public void initUi()
    {
        if(AbstractTnUiBinder.getInstance() == null)
        {
        	AbstractTnUiBinder.init(new AndroidCitizenUiBinder());
        }
        
        if(AbstractTnGraphicsHelper.getInstance() == null)
        {
            AbstractTnGraphicsHelper.init(new AndroidUiHelper());
        }
        
        TnUiContext.init(AndroidPersistentContext.getInstance().getContext(), AbstractTnUiBinder.getInstance(), AbstractTnGraphicsHelper.getInstance(), ResourceManager
                .getInstance());
    }

    public void initBattery()
    {
        if(TnBatteryManager.getInstance() == null)
        {
            TnBatteryManager.init(new AndroidBatteryManager(AndroidPersistentContext.getInstance().getContext()));
        }
    }
    
    public void initContactPrvoider()
    {
        if(AbstractContactProvider.getInstance() == null)
        {
            AbstractContactProvider.init(new AndroidContactProvider());
        }
    }
    
    public void initAudioEncoder()
    {
        if(AbstractEncoder.getInstance() == null)
        {
            AbstractEncoder.init(new NativeSpeexEncoder());
        }
    }

    public void initLocationProvider()
    {
        if (GoogleServiceLocationProvider.isAvailable())
        {
            LocationProvider.getInstance().init(new GoogleServiceLocationProvider());
        }
        else
        {
            LocationProvider.getInstance().init(new AndroidNativeLocationProvider());
        }
    }
    
    public void resetLocationProviderByRawGps()
    {
        LocationProvider.getInstance().stop();
        LocationProvider.getInstance().init(new AndroidNativeLocationProvider());
    }

    public void initSdLogCollector()
    {
        TnSdLogCollector.init(new AndroidSdLogCollector());
    }
    
    public void initNavSdk()
    {
        if(NavSdkEngine.getInstance() == null)
        {
            NavSdkEngine.init(new NavSdkEngine(AndroidPersistentContext.getInstance().getContext()));
        }
    }
}
