package com.telenav.navservice.model;

import com.telenav.navservice.util.Constants;

public class App
{
    protected String userId = "";
    protected String appVersion = "";
    protected String appName = "";
    protected String routeId = "";
    
    protected boolean isForeground, isCharging, isDriveTo;
    
    protected String carrier = "";
    protected byte radioType;
    protected byte platform;
    protected String deviceName = ""; 
    protected String firmwareVersion = "";
    
    public String getRouteId()
    {
        return routeId;
    }

    public void setRouteId(String routeId)
    {
        this.routeId = routeId;
        if (this.routeId == null || routeId.length() == 0)
        {
            isDriveTo = false;
        }
        else
        {
            try{
                long id = Long.parseLong(routeId);
                if (id == -1)
                {
                    isDriveTo = false;
                }
                else
                {
                    isDriveTo = true;
                }
            }catch(Exception e)
            {
                isDriveTo = true;
            }
        }
    }

    public String getUserId()
    {
        return userId;
    }
    
    public void setUserId(String userId)
    {
        this.userId = userId;
    }
    
    public byte getApplication()
    {
        return Constants.NAVIGATOR;
    }
    
    public String getAppVersion()
    {
        return appVersion;
    }

    public void setAppVersion(String version)
    {
        this.appVersion = version;
    }

    public String getAppName()
    {
        return appName;
    }

    public void setAppName(String appName)
    {
        this.appName = appName;
    }

    public boolean isForeground()
    {
        return isForeground;
    }

    public void setForeground(boolean isForeground)
    {
        this.isForeground = isForeground;
    }

    public boolean isCharging()
    {
        return isCharging;
    }

    public void setCharging(boolean isCharging)
    {
        this.isCharging = isCharging;
    }

    public boolean isDriveTo()
    {
        return isDriveTo;
    }

    public String getCarrier()
    {
        return carrier;
    }
    
    public void setCarrier(String carrier)
    {
        this.carrier = carrier;
    }
    
    public byte getRadioType()
    {
        return radioType;
    }
    
    public void setRadioType(byte radioType)
    {
        this.radioType = radioType;
    }
    
    public byte getPlatform()
    {
        return platform;
    }
    
    public void setPlatform(byte platform)
    {
        this.platform = platform;
    }
    
    public String getPlatformString()
    {
        switch(this.platform)
        {
            case Constants.OS_ANDROID:
                return "ANDROID";
            case Constants.OS_RIM:
                return "RIM";
            case Constants.OS_J2ME:
                return "J2ME";
            default:
                return "UNKONWN";
        }
    }

    public String getDeviceName()
    {
        return deviceName;
    }
    
    public void setDeviceName(String deviceName)
    {
        this.deviceName = deviceName;
    }
    
    public String getFirmwareVersion()
    {
        return firmwareVersion;
    }
    
    public void setFirmwareVersion(String firmwareVersion)
    {
        this.firmwareVersion = firmwareVersion;
    }

}
