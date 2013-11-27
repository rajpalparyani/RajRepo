/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * CarConnectMisLogFactory.java
 *
 */
package com.telenav.carconnect.mislog;

import com.telenav.log.mis.IMisLogConstants;
import com.telenav.log.mis.MisLogFactory;

/**
 * @author chihlh
 * @date May 10, 2012
 */
public class CarConnectMisLogFactory extends MisLogFactory implements IMisLogConstants
{
    private final static CarConnectMisLogFactory instance = new CarConnectMisLogFactory();

    public static CarConnectMisLogFactory getInstance()
    {
        return instance;
    }

    static class MandatoryData
    {
        String sessionId;

        int productCategory;

        String huOEM; // head unit OEM

        String connTech;

        String connMedium;

        String huModel;

        String vin;

        String huId;

        int hybridFlag;

        String protocolVer;
    }

    private MandatoryData mandatory = new MandatoryData();

    public void setMandatoryData(int productCategory, String huOEM, String connTech, String connMedium, String huModel,
            String vin, String huId, int hybridFlag, String protocolVer)
    {
        mandatory.sessionId = "CC_" + System.currentTimeMillis();
        mandatory.productCategory = productCategory;
        mandatory.huOEM = huOEM;
        mandatory.connTech = connTech;
        mandatory.connMedium = connMedium;
        mandatory.huModel = huModel;
        mandatory.vin = vin;
        mandatory.huId = huId;
        mandatory.hybridFlag = hybridFlag;
        mandatory.protocolVer = protocolVer;
    }

    public CommonCarConnectMisLog creatCarConnectMisLog()
    {
        return new CommonCarConnectMisLog(getId(), IMisLogConstants.TYPE_HEAD_UNIT_CAR_CONNECT, IMisLogConstants.PRIORITY_1,
                mandatory);
    }

    public CommonCarConnectMisLog creatCarDisconnectMisLog()
    {
        return new CommonCarConnectMisLog(getId(), IMisLogConstants.TYPE_HEAD_UNIT_CAR_DISCONNECT, IMisLogConstants.PRIORITY_1,
                mandatory);
    }

    public CommonCarConnectMisLog createCarConnectPoiSearchMisLog()
    {
        return new CommonCarConnectMisLog(getId(), IMisLogConstants.TYPE_HEAD_UNIT_CONNECTED_POI_SEARCH,
                IMisLogConstants.PRIORITY_1, mandatory);
    }

    public CommonCarConnectMisLog createCarConnectNavMisLog()
    {
        return new CommonCarConnectMisLog(getId(), IMisLogConstants.TYPE_HEAD_UNIT_CONNECTED_DRIVE_TO,
                IMisLogConstants.PRIORITY_1, mandatory);
    }
}
