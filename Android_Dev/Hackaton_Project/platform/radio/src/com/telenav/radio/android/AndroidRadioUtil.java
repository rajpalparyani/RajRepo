/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * AndroidRadioUtil.java
 *
 */
package com.telenav.radio.android;

import android.telephony.CellLocation;

import com.telenav.radio.TnCellInfo;

/**
 *@author fqming (fqming@telenav.cn)
 *@date Aug 9, 2010
 */
class AndroidRadioUtil
{
    public static void retrieveOtherCellLocation(CellLocation cellLocation, TnCellInfo tnCellInfo)
    {
        if (cellLocation instanceof android.telephony.cdma.CdmaCellLocation)
        {
            android.telephony.cdma.CdmaCellLocation cdmaCellLoc = (android.telephony.cdma.CdmaCellLocation) cellLocation;
            tnCellInfo.baseStationId = "" + cdmaCellLoc.getNetworkId();
            tnCellInfo.cellId = "" + cdmaCellLoc.getBaseStationId();
            tnCellInfo.locationAreaCode = "" + cdmaCellLoc.getSystemId();
            tnCellInfo.networkRadioMode = TnCellInfo.MODE_CDMA;
        }
    }
}
