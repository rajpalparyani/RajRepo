/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * CommonCarConnectMisLog.java
 *
 */
package com.telenav.carconnect.mislog;

import com.telenav.carconnect.mislog.CarConnectMisLogFactory.MandatoryData;
import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.datatypes.mandatory.MandatoryProfile;
import com.telenav.log.mis.IMisLogConstants;
import com.telenav.log.mis.log.AbstractMisLog;
import com.telenav.telephony.TnDeviceInfo;
import com.telenav.telephony.TnTelephonyManager;

/**
 * @author chihlh
 * @date May 10, 2012
 */
public class CommonCarConnectMisLog extends AbstractMisLog
{

    CommonCarConnectMisLog(String id, int eventType, int priority, MandatoryData data)
    {
        super(id, eventType, priority);
        // add mandatory data
        setAttribute(IMisLogConstants.ATTR_CAR_CONNECT_SESSION_ID, data.sessionId);
        setAttribute(IMisLogConstants.ATTR_CAR_CONNECT_CONNCTION_MEDIUM, data.connMedium);
        setAttribute(IMisLogConstants.ATTR_CAR_CONNECT_CONNECTION_TECHNOLOGY, data.connTech);
        setAttribute(IMisLogConstants.ATTR_CAR_CONNECT_CONNECTIVITY_PROTOCOL_VERSION, data.protocolVer);
        setAttribute(IMisLogConstants.ATTR_CAR_CONNECT_DEVICE_TYPE, data.huModel);
        setAttribute(IMisLogConstants.ATTR_CAR_CONNECT_HU_ID, data.huId);
        setAttribute(IMisLogConstants.ATTR_CAR_CONNECT_HYBRID_FLAG, String.valueOf(data.hybridFlag));
        setAttribute(IMisLogConstants.ATTR_CAR_CONNECT_OEM, data.huOEM);
        setAttribute(IMisLogConstants.ATTR_CAR_CONNECT_PRODUCT_CATEGORY, String.valueOf(data.productCategory));
        setAttribute(IMisLogConstants.ATTR_CAR_CONNECT_VIN_NUM, data.vin);
        // set user id for every node.
        MandatoryProfile mandatoryNode = DaoManager.getInstance().getMandatoryNodeDao().getMandatoryNode();
        if (mandatoryNode != null && mandatoryNode.getUserInfo() != null)
        {
            setAttribute(IMisLogConstants.ATTR_GENERIC_USER_ID, mandatoryNode.getUserInfo().userId);
        }
        // set device related attributes same as the VBB
        TnDeviceInfo deviceInfo = TnTelephonyManager.getInstance().getDeviceInfo();
        if (deviceInfo != null)
        {
            this.setAttribute(ATTR_GENERIC_DEVICE_ID, deviceInfo.deviceId);
            // this.setAttribute(ATTR_DEVICE_OS_VERSION, deviceInfo.platformVersion);
        }
        this.setTimestamp(System.currentTimeMillis());
    }

    public void setSessionDuration(long duration)
    {
        setAttribute(IMisLogConstants.ATTR_CAR_CONNECT_SESSION_LENGTH, String.valueOf(duration));
    }

    public void setSearchId(String searchId)
    {
        setAttribute(IMisLogConstants.ATTR_SEARCH_UID, searchId);
    }
}
