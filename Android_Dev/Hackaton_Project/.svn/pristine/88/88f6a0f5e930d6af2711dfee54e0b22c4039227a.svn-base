package com.telenav.navservice.android;

import java.util.List;

import android.app.ActivityManager;
import android.app.Service;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

import com.telenav.location.TnLocationManager;
import com.telenav.location.android.AndroidLocationManager;
import com.telenav.logger.Logger;
import com.telenav.navservice.Controller;
import com.telenav.navservice.IClientStateMonitor;
import com.telenav.navservice.NavServiceApi;
import com.telenav.navservice.NavServiceParameter;
import com.telenav.navservice.Controller.IControllerListener;
import com.telenav.navservice.model.App;
import com.telenav.navservice.util.Constants;
import com.telenav.navservice.util.TrackingLogger;
import com.telenav.network.TnNetworkManager;
import com.telenav.network.android.AndroidNetworkManager;
import com.telenav.radio.TnCellInfo;
import com.telenav.radio.TnRadioManager;
import com.telenav.radio.android.AndroidRadioManager;
import com.telenav.telephony.TnDeviceInfo;
import com.telenav.telephony.TnTelephonyManager;
import com.telenav.telephony.android.AndroidTelephonyManager;

public class NavServiceImpl implements IControllerListener, IClientStateMonitor
{
    private static final String ACTION_BATTERY_CHANGED = Intent.ACTION_BATTERY_CHANGED;
    
    protected Service service;
    
    protected Controller controller;
    
    protected AndroidTrackingLogger logger;
    
    protected int clientPid;
    
    protected long lastClientStateCheckTime;
    protected int clientDeadCounter;
    
    private BroadcastReceiver eventListener = new BroadcastReceiver() 
    {
        public void onReceive(Context context, Intent intent)
        {
            try{
                Logger.log(Logger.INFO, this.getClass().getName(), "Got system broadcast: "+intent);
                String action = intent.getAction();
                if (ACTION_BATTERY_CHANGED.equals(action))
                {
                    int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, BatteryManager.BATTERY_STATUS_UNKNOWN);
                    switch(status)
                    {
                        case BatteryManager.BATTERY_STATUS_CHARGING:
                        case BatteryManager.BATTERY_STATUS_FULL:
                        {
                            controller.setCharging(true);
                            break;
                        }
                        case BatteryManager.BATTERY_STATUS_DISCHARGING:
                        case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
                        {
                            controller.setCharging(false);
                            break;
                        }
                        default:
                        {
                            //do nothing
                            break;
                        }
                    }
                }
            }
            catch(Throwable t)
            {
                Logger.log(this.getClass().getName(), t);
            }
        }
    };
    
    public NavServiceImpl(Service service)
    {
        this.service = service;
    }
    
    protected void init()
    {
        try{
            Logger.log(Logger.INFO, this.getClass().getName(), "NavService.init() enter");
            TnNetworkManager.init(new AndroidNetworkManager());
            TnTelephonyManager.init(new AndroidTelephonyManager(service));
            TnRadioManager.init(new AndroidRadioManager(service));
            TnLocationManager.init(new AndroidLocationManager(service));
            
            App app = new App();
            app.setPlatform(Constants.OS_ANDROID);
            try{
                if (TnRadioManager.getInstance() != null)
                {
                    TnCellInfo cellInfo = TnRadioManager.getInstance().getCellInfo();
                    if (cellInfo != null)
                        app.setRadioType(Constants.convertRadioType(TnRadioManager.getInstance().getCellInfo().networkRadioMode));
                }
                if (TnTelephonyManager.getInstance() != null)
                {
                    TnDeviceInfo deviceInfo = TnTelephonyManager.getInstance().getDeviceInfo();
                    if (deviceInfo != null)
                    {
                        app.setDeviceName(TnTelephonyManager.getInstance().getDeviceInfo().deviceName);
                        app.setFirmwareVersion(TnTelephonyManager.getInstance().getDeviceInfo().softwareVersion);
                    }
                }
            }catch(Exception e)
            {
                Logger.log(this.getClass().getName(), e);
            }
            
            Logger.log(Logger.INFO, this.getClass().getName(), "trying to start Controller");
            controller = new Controller(app, this, this);
            controller.start();
        
            IntentFilter eventFilter = new IntentFilter();
            eventFilter.addAction(ACTION_BATTERY_CHANGED);
            service.registerReceiver(eventListener, eventFilter);
            Logger.log(Logger.INFO, this.getClass().getName(), "registered battery listener");
            
            Intent callbackIntent = new Intent();
            callbackIntent.setAction(NavServiceApi.NAV_SERVICE_CALLBACK_ACTION);
            service.sendBroadcast(callbackIntent);
            Logger.log(Logger.INFO, this.getClass().getName(), "sent callback action to client");
        }
        catch(Throwable t)
        {
            Logger.log(this.getClass().getName(), t);
        }
    }
    
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        try{
            NavServiceParameter param = AndroidNavServiceApi.convertIntentToNavServiceParameters(intent);

            if (logger == null)
            {
                logger = new AndroidTrackingLogger();
                logger.initLogger();
            }
            if (param.hasSetLogEnabled())
            {
                TrackingLogger.isLogEnabled = param.isLogEnabled();
            }
            
            //------------------ log is working from here ----------------
            Logger.log(Logger.INFO, this.getClass().getName(), "Got intent from client");
            
            if (param.hasSetPid())
                this.clientPid = param.getPid();
            
            if (controller == null)
            {
                init();
            }
            
            controller.setParameters(param);
        }
        catch(Throwable t)
        {
            Logger.log(this.getClass().getName(), t);
        }
        
        return Service.START_STICKY; //stick means the service will be restarted automatically if it was killed by the system
    }
    
    public void stopService()
    {
        try{
            service.unregisterReceiver(eventListener);
        }catch(Throwable t)
        {
            Logger.log(this.getClass().getName(), t);
        }
        Logger.log(Logger.INFO, this.getClass().getName(), "Service stopped");
        
        logger.close();
        service.stopSelf();
    }

    public boolean isClientRunning()
    {
        if (System.currentTimeMillis() - lastClientStateCheckTime < 30 * 1000)
        {
            return true;
        }
        
        if (this.clientPid == 0)
        {
            //client doesn't pass pid, the nav serice will keep running even after client is killed
            return true;
        }
        
        lastClientStateCheckTime = System.currentTimeMillis();
        
        ActivityManager am = (ActivityManager)service.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> infos = am.getRunningAppProcesses();
        if (infos != null)
        {
            for (int i=0; i<infos.size(); i++)
            {
                RunningAppProcessInfo info = infos.get(i);
                if (info.pid == this.clientPid)
                {
                    clientDeadCounter = 0;
                    return true;
                }
            }
        }
        clientDeadCounter++;
        if (clientDeadCounter < 3)
        {
            return true;
        }
        
        //client has been dead for 5 cycles
        return false;
    }

}
