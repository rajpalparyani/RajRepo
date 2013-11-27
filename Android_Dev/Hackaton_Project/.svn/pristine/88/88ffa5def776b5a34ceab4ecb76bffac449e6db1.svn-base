/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * AndoidRadioManager.java
 *
 */
package com.telenav.radio.android;

import java.util.Hashtable;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.CellLocation;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;

import com.telenav.logger.Logger;
import com.telenav.radio.ITnCoverageListener;
import com.telenav.radio.TnCellInfo;
import com.telenav.radio.TnRadioManager;
import com.telenav.radio.TnWifiInfo;

/**
 * Provides access to information about the radio services on the device at android platform.
 * 
 *@author fqming (fqming@telenav.cn)
 *@date Jul 11, 2010
 */
public class AndroidRadioManager extends TnRadioManager
{
    protected Context context;
    protected Hashtable listeners;
    
    /**
     * Construct the radio manager at android platform. <br />
     * <br />
     * Please make sure that grant below class's permissions. <br />
     * WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE); <br />
     * TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
     * 
     * @param context
     */
    public AndroidRadioManager(Context context)
    {
        this.context = context;
    }

    public void addListener(final ITnCoverageListener coverageListener)
    {
        if(coverageListener == null)
            return;
        
        if(listeners == null)
        {
            listeners = new Hashtable();
        }
        
        if(this.context instanceof Activity)
        {
            Activity activity = (Activity) this.context;
            activity.runOnUiThread(new Runnable()
            {
                public void run()
                {
                    AndroidRadioStatusListener androidRadioListener = new AndroidRadioStatusListener(coverageListener);
                    listeners.put(coverageListener, androidRadioListener);

                    TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                    manager.listen(androidRadioListener, PhoneStateListener.LISTEN_SERVICE_STATE | PhoneStateListener.LISTEN_SIGNAL_STRENGTH);
                }
            });
        }
        else
        {
            AndroidRadioStatusListener androidRadioListener = new AndroidRadioStatusListener(coverageListener);
            listeners.put(coverageListener, androidRadioListener);
            
            TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            manager.listen(androidRadioListener, PhoneStateListener.LISTEN_SERVICE_STATE
                    | PhoneStateListener.LISTEN_SIGNAL_STRENGTH);
        }
    }

    public TnCellInfo getCellInfo()
    {
        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        CellLocation cellLocation = manager.getCellLocation();
        if (cellLocation == null)
            return null;

        TnCellInfo tnCellInfo = new TnCellInfo();

        tnCellInfo.networkCode = "" + getMNC(manager.getNetworkOperator());
        tnCellInfo.countryCode = "" + getMCC(manager.getNetworkOperator());
        tnCellInfo.networkType = "" + manager.getNetworkType();
        tnCellInfo.networkOperatorName = "" + manager.getNetworkOperatorName();
        
        if (cellLocation instanceof GsmCellLocation)
        {
            GsmCellLocation gsmCellLoc = (GsmCellLocation) cellLocation;
            tnCellInfo.baseStationId = "" + getBSID(gsmCellLoc.getCid());
            tnCellInfo.cellId = "" + getCID(gsmCellLoc.getCid());
            tnCellInfo.locationAreaCode = "" + gsmCellLoc.getLac();
            tnCellInfo.networkRadioMode = TnCellInfo.MODE_GSM;
        }
        else
        {
            retrieveOtherCellLocation(cellLocation, tnCellInfo);
        }

        return tnCellInfo;
    }

    protected void retrieveOtherCellLocation(CellLocation cellLocation, TnCellInfo tnCellInfo)
    {
        try
        {
            if (Integer.parseInt(Build.VERSION.SDK) >= 5)
            {
                AndroidRadioUtil.retrieveOtherCellLocation(cellLocation, tnCellInfo);
            }
        }
        catch(NumberFormatException e)
        {
            Logger.log(Logger.EXCEPTION, getClass().getName(), e.toString());
        }
    }

    public TnWifiInfo getWifiInfo()
    {
        WifiInfo wifiInfo = getConnectionWifiInfo();

        if (wifiInfo == null)
            return null;

        TnWifiInfo tnWifiInfo = new TnWifiInfo();
        tnWifiInfo.bssid = wifiInfo.getBSSID();
        tnWifiInfo.signalLevel = wifiInfo.getRssi();
        tnWifiInfo.speed = wifiInfo.getLinkSpeed();
        tnWifiInfo.ssid = wifiInfo.getSSID();

        return tnWifiInfo;
    }

    public boolean isWifiConnected()
    {
        WifiInfo wifiInfo = getConnectionWifiInfo();
        if (wifiInfo != null)
        {
            Logger.log(Logger.EXCEPTION, getClass().getName(), "wifi info state: " + wifiInfo.getSupplicantState());
        }
        else
        {
            Logger.log(Logger.EXCEPTION, getClass().getName(), "wifi info is null ");
        }

        return wifiInfo != null && SupplicantState.COMPLETED == wifiInfo.getSupplicantState();
    }
    
    public boolean isNetworkConnected()
    {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE); 
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if(info == null)
        {
            if(telephonyManager.getDataState() != TelephonyManager.DATA_DISCONNECTED)
            {
                return true;
            }
            
            return false;
        }
        else
        {
            if (info.isAvailable() && info.isConnected())
            {
                return true;
            }
            else if (! info.isAvailable() && ! info.isConnected())
            {
                return false;
            }
            else
            {
                // for 4G, network info type is 6
                if (info.getType() == ConnectivityManager.TYPE_MOBILE && telephonyManager.getDataState() != TelephonyManager.DATA_DISCONNECTED)
                {   
                    return true;
                }   
                else
                {
                    return false;
                }
            }
        }
    }
    
    public boolean isAirportMode()
    {
        boolean isAirplaneMode = Settings.System.getInt(context.getContentResolver(), Settings.System.AIRPLANE_MODE_ON, 0) == 1;

        return isAirplaneMode;
    }

    public boolean isRoaming()
    {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        
        return tm.isNetworkRoaming();
    }
    
    public void removeListener(ITnCoverageListener coverageListener)
    {
        if(coverageListener == null || listeners == null)
            return;
        
        final AndroidRadioStatusListener androidRadioListener = (AndroidRadioStatusListener)listeners.get(coverageListener);
        if(androidRadioListener == null)
            return;
        
        listeners.remove(coverageListener);
        
        if(this.context instanceof Activity)
        {
            Activity activity = (Activity) this.context;
            activity.runOnUiThread(new Runnable()
            {
                public void run()
                {
                    TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                    manager.listen(androidRadioListener, PhoneStateListener.LISTEN_NONE);
                }
            });
        }
        else
        {
            TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            manager.listen(androidRadioListener, PhoneStateListener.LISTEN_NONE);
        }
    }

    protected WifiInfo getConnectionWifiInfo()
    {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

        if (wifiManager.isWifiEnabled())
        {
            return wifiManager.getConnectionInfo();
        }

        return null;
    }

    protected int getMCC(String operatorCode)
    {
        int mcc = -1;
        if (operatorCode != null && operatorCode.length() >= 5)
        {
            try
            {
                mcc = Integer.parseInt(operatorCode.substring(0, 3));
            }
            catch(NumberFormatException e)
            {
                Logger.log(Logger.EXCEPTION, getClass().getName(), e.toString());
            }
        }
        return mcc;
    }

    protected int getMNC(String operatorCode)
    {
        int mnc = -1;

        if (operatorCode != null && operatorCode.length() >= 5)
        {
            try
            {
                mnc = Integer.parseInt(operatorCode.substring(3));
            }
            catch(NumberFormatException e)
            {
                Logger.log(Logger.EXCEPTION, getClass().getName(), e.toString());
            }
        }
        return mnc;

    }

    protected int getCID(int androidCID)
    {
        int iCid = androidCID % (1 << 16);
        return iCid;
    }

    protected int getBSID(int androidCID)
    {
        int bsid = androidCID >> 16;
        return bsid;
    }
}
