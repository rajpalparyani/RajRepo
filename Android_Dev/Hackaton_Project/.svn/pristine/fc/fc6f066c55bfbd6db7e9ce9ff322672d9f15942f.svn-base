/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * BillboardMisLog.java
 *
 */
package com.telenav.log.mis.log;

import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.datatypes.mandatory.MandatoryProfile;
import com.telenav.data.datatypes.mandatory.MandatoryProfile.ClientInfo;
import com.telenav.location.TnLocation;
import com.telenav.log.mis.MisLogManager;
import com.telenav.module.location.LocationProvider;
import com.telenav.telephony.TnDeviceInfo;
import com.telenav.telephony.TnTelephonyManager;

/**
 *@author zhdong@telenav.cn
 *@date 2011-4-1
 */
public class BillboardMisLog extends AbstractMisLog
{
    public BillboardMisLog(String id, int eventType, int priority)
    {
        super(id, eventType, priority);
    }

    public void processLog()
    {
        super.processLog();
        
        setTimestamp(System.currentTimeMillis());

        ClientInfo clientInfo = DaoManager.getInstance().getMandatoryNodeDao().getMandatoryNode().getClientInfo();
        setProgramCode(clientInfo.programCode);

        String manufacturer = TnTelephonyManager.getInstance().getDeviceInfo().manufacturerName;
        if (manufacturer == null || manufacturer.length() == 0)
        {
            setDeviceMake(clientInfo.platform);
        }
        else
        {
            setDeviceMake(manufacturer);
        }

        setDeviceModel(clientInfo.device);
        setDeviceOs(clientInfo.platform);
        setAppVersion(clientInfo.productType);
        setClientVersion(clientInfo.version);
        setSessionId(MisLogManager.getInstance().getSessionId());
        setPtn(DaoManager.getInstance().getBillingAccountDao().getPtn());
        
        TnDeviceInfo deviceInfo = TnTelephonyManager.getInstance().getDeviceInfo();
        if (deviceInfo != null)
        {
            setDeviceId(deviceInfo.deviceId);
            setDeviceOsVersion(deviceInfo.platformVersion);
        }
        
        MandatoryProfile mandatoryNode = DaoManager.getInstance().getMandatoryNodeDao().getMandatoryNode();        
        if (mandatoryNode != null && mandatoryNode.getUserInfo() != null)
        {
            setUserId(mandatoryNode.getUserInfo().userId);
        }

        TnLocation location = LocationProvider.getInstance().getCurrentLocation(LocationProvider.TYPE_GPS | LocationProvider.TYPE_NETWORK);

        if (location != null)
        {
            this.setAttribute(ATTR_GENERIC_LAT, String.valueOf(location.getLatitude()));
            this.setAttribute(ATTR_GENERIC_LON, String.valueOf(location.getLongitude()));

            // TODO what is the attr for speed?
        }
    }

    public void setProgramCode(String programCode)
    {
        this.setAttribute(ATTR_PROGRAM_CODE, programCode);
    }

    public void setDeviceMake(String deviceMake)
    {
        this.setAttribute(ATTR_DEVICE_MAKE, deviceMake);
    }

    public void setDeviceModel(String model)
    {
        this.setAttribute(ATTR_DEVICE_MODEL, model);
    }

    public void setDeviceOs(String deviceOs)
    {
        this.setAttribute(ATTR_DEVICE_OS, deviceOs);
    }

    public void setAppVersion(String appVersion)
    {
        this.setAttribute(ATTR_APP_VERSION, appVersion);
    }

    public void setClientVersion(String clientVersion)
    {
        this.setAttribute(ATTR_CLIENT_VERSION, clientVersion);
    }

    public void setSessionId(String sessionId)
    {
        this.setAttribute(ATTR_SESSION_ID, MisLogManager.getInstance().getSessionId());
    }

    public void setPtn(String ptn)
    {
        this.setAttribute(ATTR_PTN, ptn);
    }
    
    public void setDeviceId(String devideId)
    {
        this.setAttribute(ATTR_GENERIC_DEVICE_ID, devideId);
    }
    
    public void setUserId(String userId)
    {
        this.setAttribute(ATTR_GENERIC_USER_ID, userId);
    }
    
    public void setDeviceOsVersion(String deviceOsVersion)
    {
        this.setAttribute(ATTR_DEVICE_OS_VERSION, deviceOsVersion);
    }

}
