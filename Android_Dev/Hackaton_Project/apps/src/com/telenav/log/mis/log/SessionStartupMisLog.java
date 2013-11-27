/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * SessionStartup.java
 *
 */
package com.telenav.log.mis.log;

import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.datatypes.mandatory.MandatoryProfile.ClientInfo;
import com.telenav.log.mis.MisLogManager;
import com.telenav.telephony.TnTelephonyManager;

/**
 *@author Casper (pwang@telenav.cn)
 *@date 2010-12-27
 */
public class SessionStartupMisLog extends AbstractMisLog
{
    public SessionStartupMisLog(String id, int priority)
    {
        super(id, TYPE_SESSION_STARTUP, priority);
    }
    
    public void processLog()
    {
        super.processLog();
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

}
