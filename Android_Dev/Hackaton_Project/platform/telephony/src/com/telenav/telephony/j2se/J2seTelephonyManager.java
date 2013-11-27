/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * J2seTelephonyManager.java
 *
 */
package com.telenav.telephony.j2se;

import java.io.IOException;
import java.util.Hashtable;

import com.telenav.io.TnProperties;
import com.telenav.logger.Logger;
import com.telenav.telephony.ITnApnListener;
import com.telenav.telephony.ITnPhoneListener;
import com.telenav.telephony.TnApnInfo;
import com.telenav.telephony.TnDeviceInfo;
import com.telenav.telephony.TnSimCardInfo;
import com.telenav.telephony.TnTelephonyManager;

/**
 *@author fqming (fqming@telenav.cn)
 *@date Aug 25, 2010
 */
public class J2seTelephonyManager extends TnTelephonyManager
{
    private final static String PTN_KEY = "PhoneNumber";
    private final static String MANUFACTURER_KEY = "Manufacturer";
    private final static String DEVICE_ID_KEY = "DeviceId";
    private final static String DEVICE_NAME_KEY = "DeviceName";
    private final static String PLATFORM_VERSION_KEY = "PlatformVersion";
    private final static String SOFTWARE_VERSION_KEY = "SoftwareVersion";
    private final static String SIMCARD_ID_KEY = "SimCardId";
    
    protected final TnProperties properties;
    
    protected Hashtable listeners;
    
    public J2seTelephonyManager(TnProperties tProperties)
    {
        this.properties = tProperties;
    }
    
    public void addListener(ITnPhoneListener phoneListener)
    {
        if (phoneListener == null)
            return;

        if (listeners == null)
        {
            listeners = new Hashtable();
        }
        
        listeners.put(phoneListener, phoneListener);
    }

    public TnDeviceInfo getDeviceInfo()
    {
        TnDeviceInfo deviceInfo = new TnDeviceInfo();

        deviceInfo.platform = TnDeviceInfo.PLATFORM_ANDROID;
        
        if(properties != null)
        {
            deviceInfo.manufacturerName = (String)properties.getProperty(MANUFACTURER_KEY);
            deviceInfo.deviceId = (String)properties.getProperty(DEVICE_ID_KEY);
            deviceInfo.deviceName = (String)properties.getProperty(DEVICE_NAME_KEY);
            deviceInfo.platformVersion = (String)properties.getProperty(PLATFORM_VERSION_KEY);
            deviceInfo.softwareVersion = (String)properties.getProperty(SOFTWARE_VERSION_KEY);
        }
        else
        {
            deviceInfo.manufacturerName = "TELENAV";
            deviceInfo.deviceId = "TN_J2SE";
            deviceInfo.deviceName = "TN_J2SE";
            deviceInfo.platformVersion = "1.6";
            deviceInfo.softwareVersion = "1.1";
        }
        
        return deviceInfo;
    }

    public String getPhoneNumber()
    {
        if(properties != null)
            return (String)properties.getProperty(PTN_KEY);
        
        String phoneNumber = "5055055050";
        
        return phoneNumber;
    }

    public TnSimCardInfo getSimCardInfo()
    {
        TnSimCardInfo simCardInfo = new TnSimCardInfo();
        
        if (properties != null)
        {
            simCardInfo.simCardId = (String)properties.getProperty(SIMCARD_ID_KEY);
        }
        else
        {
            simCardInfo.simCardId = "00000000000001";
        }

        return simCardInfo;
    }

    public void removeListener(ITnPhoneListener phoneListener)
    {
        if (phoneListener == null || listeners == null)
            return;
        
        listeners.remove(phoneListener);
    }

    public void startBrowser(String url)
    {
        if (url == null || url.length() == 0)
        {
            throw new IllegalArgumentException("the url is empty.");
        }
        
        if (!url.toLowerCase().startsWith("http"))
        {
            url = "http://" + url;
        }
        try
        {
            Runtime.getRuntime().exec(url);
        }
        catch (IOException e)
        {
            Logger.log(this.getClass().getName(), e);
        }
    }

    public void startCall(String phoneNumber)
    {
        // TODO Auto-generated method stub
        
    }

    public void startEmail(String sentTo, String subject, String content)
    {
        // TODO Auto-generated method stub
    }

	public void startMMS(String sentTo, String message)
	{
		//TODO Auto-generated method stub
	}

	public void startMMS(String sentTo, String message, byte[] attachData, String streamType)
	{
		//TODO Auto-generated method stub
	}

    public void startMMSAtBackground(String sentTo, String message)
    {
        // TODO Auto-generated method stub
        
    }
    
    public void vibrate(int durationTime)
    {
        // TODO Auto-generated method stub
        
    }

    public void addApnListener(ITnApnListener listener)
    {
    }

    public void removeApnListener(ITnApnListener listener)
    {
    }

    public String insertApn(TnApnInfo apn, Hashtable extras)
    {
        return null;
    }

    public void setPrefered(String apnId)
    {
    }

    protected TnApnInfo[] getMatchedApn(String apn, String type, String mcc, String mnc)
    {
        return null;
    }

    protected String getSimOperator()
    {
        return null;
    }

    protected TnApnInfo getPreferredApn(String apn, String type, String mcc, String mnc)
    {
        return null;
    }

	public String getIpAddress()
    {
		return null;
	}
}
