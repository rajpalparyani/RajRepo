/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * NavsdkSystemService.java
 *
 */
package com.telenav.data.serverproxy.impl.navsdk.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.telenav.comm.TnNetworkInfo;
import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.dao.misc.SecretSettingDao;
import com.telenav.data.serverproxy.NetworkStatusManager;
import com.telenav.logger.Logger;
import com.telenav.mvc.ICommonConstants;
import com.telenav.navsdk.eventmanager.EventBus;
import com.telenav.navsdk.events.SystemData.NetworkStatus;
import com.telenav.navsdk.events.SystemEvents.BSSIDNotifyRequest;
import com.telenav.navsdk.events.SystemEvents.DataConnectionStatusUpdate;
import com.telenav.navsdk.events.SystemEvents.GetBSSID;
import com.telenav.navsdk.events.SystemEvents.GetNetworkStatus;
import com.telenav.navsdk.events.SystemEvents.NetworkStatusNotifyRequest;
import com.telenav.navsdk.events.SystemEvents.OnNetworkStatusChangedRequest;
import com.telenav.navsdk.events.SystemEvents.RegisterNetworkStatusUpdate;
import com.telenav.navsdk.services.SystemListener;
import com.telenav.navsdk.services.SystemServiceProxy;
import com.telenav.radio.TnRadioManager;
import com.telenav.radio.TnWifiInfo;

/**
 * @author pwang
 * @date 2012-12-5
 */
public class NavsdkNetworkService implements SystemListener
{
    private boolean isOutOfNetWork;

    private boolean isMobileNetWorkConnected;

    private boolean isWifiNetWorkConnected;

    private boolean isNavsdkRegistered = false;
    
    private Context context;

    private SystemServiceProxy serverProxy;

    private ConnectivityBroadcastReceiver receiver = null;
    
    private static NavsdkNetworkService instance;

    private NavsdkNetworkService(Context context)
    {
        isMobileNetWorkConnected = TnRadioManager.getInstance().isNetworkConnected();
        isWifiNetWorkConnected = TnRadioManager.getInstance().isWifiConnected();
        isOutOfNetWork = !(isMobileNetWorkConnected || isWifiNetWorkConnected);
        this.context = context;
        receiver = new ConnectivityBroadcastReceiver();
        receiver.start();
    }

    public synchronized static void init(EventBus bus, Context context)
    {
        if (instance == null)
        {
            instance = new NavsdkNetworkService(context);
            instance.setEventBus(bus);
        }
    }

    public static NavsdkNetworkService getInstance()
    {
        return instance;
    }
    
    private void setEventBus(EventBus bus)
    {
        serverProxy = new SystemServiceProxy(bus);
        serverProxy.addListener(this);
    }

    public void onGetNetworkStatus(GetNetworkStatus event)
    {
        NetworkStatusNotifyRequest.Builder requestBuilder = NetworkStatusNotifyRequest.newBuilder();
        requestBuilder.setStatus(getNetworkStatus());
        serverProxy.networkStatusNotify(requestBuilder.build());
    }

    public void onRegisterNetworkStatusUpdate(RegisterNetworkStatusUpdate event)
    {
        isNavsdkRegistered = true;
    }

    public void connectivityChanged(TnNetworkInfo newNetwork, boolean isConnected, boolean isFailOver)
    {
        switch (newNetwork.getType())
        {
            case TnNetworkInfo.TYPE_MOBILE:
            case TnNetworkInfo.TYPE_WIMAX:
            {
                isOutOfNetWork = !isConnected && !isWifiNetWorkConnected;
                if (!isWifiNetWorkConnected)// each time wifi info should send before mobile info.
                {
                    if (isConnected)
                    {
                        sendConnectedMessage();
                    }
                    else
                    {
                        sendDisconnectedMessage();
                    }
                }
                isMobileNetWorkConnected = isConnected;
                break;
            }
            case TnNetworkInfo.TYPE_WIFI:
            {
                isMobileNetWorkConnected = TnRadioManager.getInstance().isNetworkConnected();
                isOutOfNetWork = !isConnected && !isMobileNetWorkConnected;
                if (isWifiNetWorkConnected != isConnected)
                {
                    if (isConnected)
                    {
                        sendConnectedMessage();
                    }
                    else
                    {
                        sendDisconnectedMessage();
                    }
                }
                isWifiNetWorkConnected = isConnected;
                break;
            }
        }
    }

    public void sendConnectedMessage()
    {
        if (isNavsdkRegistered)
        {
            OnNetworkStatusChangedRequest.Builder requestBuilder = OnNetworkStatusChangedRequest.newBuilder();
            requestBuilder.setStatus(NetworkStatus.NetworkStatus_Connected);
            serverProxy.onNetworkStatusChanged(requestBuilder.build());
            Logger.log(Logger.INFO, this.getClass().getName(), "Casper:send connected");
        }
    }

    public void sendDisconnectedMessage()
    {
        if (isNavsdkRegistered)
        {
            OnNetworkStatusChangedRequest.Builder requestBuilder = OnNetworkStatusChangedRequest.newBuilder();
            requestBuilder.setStatus(NetworkStatus.NetworkStatus_Disconnected);
            serverProxy.onNetworkStatusChanged(requestBuilder.build());
            Logger.log(Logger.INFO, this.getClass().getName(), "Casper:send disconnected");
        }
    }

    private NetworkStatus getNetworkStatus()
    {
        int dataSourceMode = ((DaoManager) DaoManager.getInstance()).getSecretSettingDao().get(
            SecretSettingDao.ONBOARD_MODE_INDEX);

        switch (dataSourceMode)
        {
            case ICommonConstants.DATASOURCE_MODE_AUTO:
            {
                if (isOutOfNetWork)
                {
                    return NetworkStatus.NetworkStatus_Disconnected;
                }
                else
                {
                    return NetworkStatus.NetworkStatus_Connected;
                }
            }
            case ICommonConstants.DATASOURCE_MODE_ONBOARD:
            {
                return NetworkStatus.NetworkStatus_Disconnected;
            }
            case ICommonConstants.DATASOURCE_MODE_OFFBOARD:
            {
                return NetworkStatus.NetworkStatus_Connected;
            }
            default:
            {
                if (isOutOfNetWork)
                {
                    return NetworkStatus.NetworkStatus_Disconnected;
                }
                else
                {
                    return NetworkStatus.NetworkStatus_Connected;
                }
            }
        }
    }
    
    public void release()
    {
        if (receiver != null)
        {
            receiver.stop();
        }
    }

    public void onGetBSSID(GetBSSID event)
    {
        BSSIDNotifyRequest.Builder requestBuilder = BSSIDNotifyRequest.newBuilder();
        TnWifiInfo tnWifiInfo = TnRadioManager.getInstance().getWifiInfo();
        if (tnWifiInfo != null && tnWifiInfo.bssid != null)
        {
            requestBuilder.setBssid(tnWifiInfo.bssid);
        }
        serverProxy.bSSIDNotify(requestBuilder.build());
        Logger.log(Logger.INFO, this.getClass().getName(), "Casper:annswer getBssid :"
                + ((tnWifiInfo == null || tnWifiInfo.ssid == null || tnWifiInfo.ssid.length() == 0) ? "null" : tnWifiInfo.ssid));
    }

    public void onDataConnectionStatusUpdate(DataConnectionStatusUpdate event)
    {
        NetworkStatusManager.getInstance().networkStatusUpdate(event.getConnected());
    }
    
    private class ConnectivityBroadcastReceiver extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent) 
        {
            String action = intent.getAction();
            
            if (!action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) 
            {
                return;
            }
             
            NetworkInfo networkInfo = intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
            NetworkInfo otherNetworkInfo = intent.getParcelableExtra(ConnectivityManager.EXTRA_OTHER_NETWORK_INFO);
            final boolean isFailOver = intent.getBooleanExtra(ConnectivityManager.EXTRA_IS_FAILOVER, false);
            final boolean isNoConnectivity = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
                        
            boolean is4GNetwork = false;
            boolean is3GNetwork = false;
            boolean isWifi = false;
            boolean isConnected = false;
            boolean otherIs4GNetwork = false;
            boolean otherIs3GNetwork = false;
            boolean otherIsWIfi = false;
            
            if(networkInfo == null)
            {
                return;
            }
            int networkType = networkInfo.getType();
            is4GNetwork = is4GNetwork(networkType);
            is3GNetwork = is3GNetwork(networkType);
            isWifi = isWifiNetwork(networkType);
            isConnected = networkInfo.isConnected();
            
            if(otherNetworkInfo != null)
            {
                int otherNetworkType = otherNetworkInfo.getType();
                otherIs4GNetwork = is4GNetwork(otherNetworkType);
                otherIs3GNetwork = is3GNetwork(otherNetworkType);
                otherIsWIfi = isWifiNetwork(otherNetworkType);
            }
            
            Logger.log(Logger.INFO, this.getClass().getName(), "Casper ---------: NetworkType:" + (isWifi ? "Wifi" : is3GNetwork ? "3G" : is4GNetwork ? "4G" : "Unknown"));
            Logger.log(Logger.INFO, this.getClass().getName(), "Casper ---------: isConnected? :" + networkInfo.isConnected());
            Logger.log(Logger.INFO, this.getClass().getName(), "Casper ---------: OtherNetworkType:" + (otherIsWIfi ? "Wifi" : otherIs3GNetwork ? "3G" : otherIs4GNetwork ? "4G" : "Unknown"));
            Logger.log(Logger.INFO, this.getClass().getName(), "Casper ---------: "  + (isNoConnectivity ? "disconnected" : "connected"));
            Logger.log(Logger.INFO, this.getClass().getName(), "Casper ---------: isFailOver: " + isFailOver);
            if(otherNetworkInfo != null)
            {
                Logger.log(Logger.INFO, this.getClass().getName(), "Casper ---------: is other network available : " + otherNetworkInfo.isAvailable());
                Logger.log(Logger.INFO, this.getClass().getName(), "Casper ---------: other network state : " + otherNetworkInfo.getState());
                Logger.log(Logger.INFO, this.getClass().getName(), "Casper ---------: other network detailed state : " + otherNetworkInfo.getDetailedState());
            }
            Logger.log(Logger.INFO, this.getClass().getName(), "Casper -------------------------------------");
            
            final TnNetworkInfo tnNetworkInfo = new TnNetworkInfo();
            tnNetworkInfo.setType(convertNetworkType(networkType));
            
            connectivityChanged(tnNetworkInfo, isConnected, isFailOver);
        }
        
        /**
         * This method starts listening for network connectivity state changes.
         * @param context
         */
        private synchronized void start() 
        {
            IntentFilter filter = new IntentFilter();
            filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            context.registerReceiver(receiver, filter);
        }

        /**
         * This method stops this class from listening for network changes.
         */
        private synchronized void stop()
        {
            context.unregisterReceiver(receiver);
        }
        
        private int convertNetworkType(int systemType)
        {
            int type = -1;
            switch (systemType)
            {
                case ConnectivityManager.TYPE_MOBILE:
                    type = TnNetworkInfo.TYPE_MOBILE;
                    break;
                case ConnectivityManager.TYPE_WIFI:
                    type = TnNetworkInfo.TYPE_WIFI;
                    break;
                case ConnectivityManager.TYPE_WIMAX:
                    type = TnNetworkInfo.TYPE_WIMAX;
                    break;
            }
            return type;
        }
        
        private boolean isWifiNetwork(int networkType)
        {
            return networkType == ConnectivityManager.TYPE_WIFI;
        }
        
        private boolean is3GNetwork(int networkType)
        {
            return networkType == ConnectivityManager.TYPE_MOBILE;
        }

        private boolean is4GNetwork(int networkType)
        {
            return networkType == ConnectivityManager.TYPE_WIMAX;
        }
    };

}
