/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * RimRadioManager.java
 *
 */
package com.telenav.radio.rim;

import java.util.Hashtable;

import net.rim.device.api.system.Application;
import net.rim.device.api.system.CDMAInfo;
import net.rim.device.api.system.GPRSInfo;
import net.rim.device.api.system.RadioInfo;
import net.rim.device.api.system.WLANInfo;
import net.rim.device.api.system.WLANInfo.WLANAPInfo;

import com.telenav.radio.ITnCoverageListener;
import com.telenav.radio.TnCellInfo;
import com.telenav.radio.TnRadioManager;
import com.telenav.radio.TnWifiInfo;

/**
 * Provides access to information about the radio services on the device at rim platform.
 * 
 *@author fqming (fqming@telenav.cn)
 *@date Aug 17, 2010
 */
public class RimRadioManager extends TnRadioManager
{
    protected Hashtable listeners;
    protected Application application;
    
    /**
     * Construct the radio manager at rim platform. <br />
     * <br />
     * <br />
     * Please make sure that grant below class's permission.
     * <br />
     * ApplicationPermissions#PERMISSION_PHONE;#PERMISSION_SECURITY_DATA;
     * <br />
     * 
     * @param application
     */
    public RimRadioManager(Application application)
    {
        this.application = application;
    }
    
    public void addListener(ITnCoverageListener coverageListener)
    {
        if(coverageListener == null)
            return;
        
        if(listeners == null)
        {
            listeners = new Hashtable();
        }
        
        RimRadioStatusListener rimRadioListener = new RimRadioStatusListener(coverageListener);
        listeners.put(coverageListener, rimRadioListener);
        
        application.addRadioListener(rimRadioListener);
    }

    public TnCellInfo getCellInfo()
    {
        if (RadioInfo.getSignalLevel() == RadioInfo.LEVEL_NO_COVERAGE || RadioInfo.getNetworkType() == RadioInfo.NETWORK_IDEN)
            return null;
        
        TnCellInfo tnCellInfo = new TnCellInfo();
        
        int netIndex = RadioInfo.getCurrentNetworkIndex();
        tnCellInfo.countryCode = convertMCC(RadioInfo.getMCC(netIndex));
        tnCellInfo.networkCode = convertMNC(RadioInfo.getMNC(netIndex));
        tnCellInfo.networkType = "" + getNetworkType();
        tnCellInfo.networkOperatorName = RadioInfo.getCurrentNetworkName();
        
        if (isGSM())
        {
            GPRSInfo.GPRSCellInfo gprsInfo = GPRSInfo.getCellInfo();
            if (gprsInfo != null)
            {
                tnCellInfo.baseStationId = "" + gprsInfo.getBSIC();
                tnCellInfo.cellId = "" + gprsInfo.getCellId();
                tnCellInfo.locationAreaCode = "" + gprsInfo.getLAC();
            }
            tnCellInfo.networkRadioMode = TnCellInfo.MODE_GSM;
        }
        else
        {
            CDMAInfo.CDMACellInfo cdmaInfo = CDMAInfo.getCellInfo();

            if (cdmaInfo != null)
            {
                tnCellInfo.baseStationId = "" + cdmaInfo.getNID();
                tnCellInfo.cellId = "" + cdmaInfo.getBID();
                tnCellInfo.locationAreaCode = "" + cdmaInfo.getSID();
            }
            tnCellInfo.networkRadioMode = TnCellInfo.MODE_CDMA;
        }
        return tnCellInfo;
    }

    public TnWifiInfo getWifiInfo()
    {
        WLANAPInfo wlanInfo = WLANInfo.getAPInfo();
        if(wlanInfo == null)
            return null;
        
        TnWifiInfo tnWifiInfo = new TnWifiInfo();
        tnWifiInfo.bssid = wlanInfo.getBSSID();
        tnWifiInfo.signalLevel = wlanInfo.getSignalLevel();
        tnWifiInfo.speed = wlanInfo.getDataRate();
        tnWifiInfo.ssid = wlanInfo.getSSID();

        return tnWifiInfo;
    }

    public boolean isRoaming()
    {
        return RadioInfo.getNetworkService() == RadioInfo.NETWORK_SERVICE_ROAMING;
    }

    public boolean isWifiConnected()
    {
        return WLANInfo.getWLANState() == WLANInfo.WLAN_STATE_CONNECTED;
    }
    
    public boolean isNetworkConnected()
    {
        boolean isOperational = RadioInfo.isDataServiceOperational();
        if(!isOperational)
        {
            isOperational = RadioInfo.isDataServiceOperational(RadioInfo.WAF_WLAN);
        }
        
        return isOperational;
    }
    
    public boolean isAirportMode()
    {
        return false;
    }

    public void removeListener(ITnCoverageListener coverageListener)
    {
        if(coverageListener == null || listeners == null)
            return;
        
        RimRadioStatusListener rimRadioListener = (RimRadioStatusListener)listeners.get(coverageListener);
        if(rimRadioListener == null)
            return;
        
        listeners.remove(coverageListener);
        
        application.removeRadioListener(rimRadioListener);
    }
    
    protected int getNetworkType()
    {
        int networkService = RadioInfo.getNetworkService();
        
        if(isGSM())
        {
            if ((networkService & RadioInfo.NETWORK_SERVICE_EDGE) > 0)
                return 2; // NETWORK_TYPE_EDGE
            
            if ((networkService & RadioInfo.NETWORK_SERVICE_UMTS) > 0)
                return 3; // NETWORK_TYPE_UMTS
        }
        else
        {
            if ((networkService & RadioInfo.NETWORK_SERVICE_EVDO_ONLY) > 0)
                return 5; // NETWORK_TYPE_EVDO_0
            
            if ((networkService & RadioInfo.NETWORK_SERVICE_EVDO) > 0)
                return 6; // NETWORK_TYPE_EVDO_A
        }
        
        return 0;
    }

    protected boolean isGSM()
    {
        int networkType = RadioInfo.getNetworkType();
        switch( networkType )
        {
            case RadioInfo.NETWORK_GPRS:
            case RadioInfo.NETWORK_UMTS:
                return true;
            default:
                return false;
        }
    }

    protected String convertMCC(int mcc)
    {
        return Integer.toHexString(mcc);
    }

    protected String convertMNC(int mnc)
    {
        return Integer.toHexString(mnc);
    }
}
