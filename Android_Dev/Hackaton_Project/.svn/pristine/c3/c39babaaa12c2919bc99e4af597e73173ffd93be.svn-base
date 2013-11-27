/**
 *
 * Copyright 2013 TeleNav, Inc. All rights reserved.
 * DwfShareLocationTask.java
 *
 */
package com.telenav.dwf.aidl;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.RemoteViews;

import com.telenav.app.android.scout_us.R;
import com.telenav.app.android.scout_us.TeleNav;
import com.telenav.dwf.aidl.Friend.FriendStatus;
import com.telenav.dwf.request.DwfNetworkUtil;
import com.telenav.dwf.vo.DwfResponseStatus;
import com.telenav.dwf.vo.UpdateSessionRequest;
import com.telenav.dwf.vo.UpdateSessionResponse;
import com.telenav.sdk.kontagent.IKontagentConstants;
import com.telenav.sdk.kontagent.KontagentLogger;
import com.telenav.sdk.maitai.IMaiTai;

/**
 * @author fangquanm
 * @date Jul 9, 2013
 */
public class DwfShareLocationTask implements Runnable, LocationListener
{
    private static DwfShareLocationTask instance = new DwfShareLocationTask();

    private final static int NOTIFICATION_ID = 1000234;

    private Intent notificationIntent;
    
    private DwfShareLocationTask()
    {
    }

    public static DwfShareLocationTask getInstance()
    {
        return instance;
    }

    private Context context;

    private boolean isStarted;

    private boolean isCancelled;

    private long interval = 60000;

    private Object mutex = new Object();

    private Location previousBestLocation;

    private String url;

    private String sessionId;

    private Friend host;

    private String formattedDt = "";

    private long lastOutsideUpdateTime = -1;
    
    private long lastOutsideUpdateEtaTime = -1;

    private long lastOutsideUpdateLocationTime = -1;

    private Looper locationLooper;

    private MainAppStatus mainAppStatus;

    private long mainAppStatusUpdateStartTime = -1;

    private Hashtable<String, DwfUpdateListener> listeners = new Hashtable<String, DwfUpdateListener>();

    private ArrayList<Friend> lastUpdateFriends;

    private boolean isPaused = false;
    
    public synchronized void start(Context context, String url, String sessionId, Friend f, String formattedDt)
    {
        if (isStarted)
        {
            return;
        }
        
        this.lastOutsideUpdateTime = System.currentTimeMillis();
        this.lastOutsideUpdateEtaTime = System.currentTimeMillis();
        this.lastOutsideUpdateLocationTime = System.currentTimeMillis();

        //clear listeners during INIT in dwfmodel.
       // this.listeners.clear();

        this.context = context;

        isStarted = true;
        isCancelled = false;

        this.url = url;
        this.sessionId = sessionId;

        this.host = f.copy();

        this.formattedDt = formattedDt;

        this.notificationIntent = null;
        
        getLastKnownloation();
        
        Thread t = new Thread(this);
        t.start();
    }

    public boolean isStarted()
    {
        return this.isStarted;
    }

    public synchronized void stop(Context context)
    {
        if (!isStarted)
        {
            return;
        }

        isStarted = false;
        isCancelled = true;

        this.listeners.clear();

        synchronized (mutex)
        {
            mutex.notify();
        }
    }
    
    public void resume()
    {
        synchronized (mutex)
        {
            isPaused = false;
            mutex.notifyAll();
        }
        showNotification(context, MainAppStatus.background.name());
    }

    public void pause()
    {
        isPaused = true;
        showNotification(context, MainAppStatus.background.name());
    }
    
    private void checkForPause()
    {
        if (isPaused )
        {
            try
            {
                synchronized (mutex)
                {
                    mutex.wait(0);
                }
            }
            catch (Throwable t)
            {
                t.printStackTrace();
            }
        }
    }

    public void setInteval(long interval)
    {
        this.interval = interval;

        synchronized (mutex)
        {
            mutex.notify();
        }
    }

    public void updateStatus(long eta, String status, double lat, double lon)
    {
        if (this.host == null)
        {
            return;
        }

        this.lastOutsideUpdateTime = System.currentTimeMillis();

        if(status != null && status.length() > 0 && this.host.getStatus() == FriendStatus.DRIVING)
        {
            if (eta != Integer.MIN_VALUE && eta != -1)
            {
                this.lastOutsideUpdateEtaTime = System.currentTimeMillis();
                this.host.setEta(eta);
            }
        }
        else if(eta != Integer.MIN_VALUE)
        {

            this.lastOutsideUpdateEtaTime = System.currentTimeMillis();
            this.host.setEta(eta);

        }
        
        if (status != null && status.length() > 0)
        {
            if(status.equals(FriendStatus.END.name()))
            {
                this.host.setStatus(FriendStatus.valueOf(status));
            }
            else if(this.host.getStatus() != FriendStatus.ARRIVED)
            {
                this.host.setStatus(FriendStatus.valueOf(status));
            }
        }

        if (lat != 0 && lon != 0)
        {
            this.lastOutsideUpdateLocationTime = System.currentTimeMillis();

            this.host.setLat(lat);
            this.host.setLon(lon);
        }
    }

    
    protected void getLastKnownloation()
    {
        try
        {
            LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            
            previousBestLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (previousBestLocation == null)
            {
                previousBestLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }
            
            DwfLogger.getInstance().log(DwfLogger.INFO, "Last known Location : " + previousBestLocation.toString());
            
            if (host != null)
            {
                host.setLat(previousBestLocation.getLatitude());
                host.setLon(previousBestLocation.getLongitude());
            }
            
            DwfLogger.getInstance().log(DwfLogger.INFO, "Host : " + host);
            
        }catch(Exception e)
        {
            
        }        
    }
    
    public void addUpdateListener(final DwfUpdateListener listener, String key)
    {
        this.listeners.put(key, listener);
        
        if(lastUpdateFriends != null)
        {
            Thread t = new Thread(new Runnable()
            {
                public void run()
                {
                    try
                    {

                        listener.updateDwf(lastUpdateFriends);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            });
            t.start();
        }
    }
    
    public void clearUpdateListener()
    {
        this.listeners.clear();
    }
    
    public void setFriends(ArrayList<Friend> friends)
    {
        lastUpdateFriends = friends;
    }

    public void removeUpdateLister(String key)
    {
        this.listeners.remove(key);
    }

    public void setMainAppStatus(String status)
    {
        if (status == null || status.length() == 0)
            return;

        this.mainAppStatus = MainAppStatus.valueOf(status);
        this.mainAppStatusUpdateStartTime = System.currentTimeMillis();
    }
    
    public void enableNotification(boolean isEnabled, String appStatus)
    {
        if(this.context == null)
            return;
        
        if(isEnabled)
        {
            showNotification(this.context, appStatus);
        }
        else
        {
            hideNotification(this.context);
        }
    }

    @Override
    public void run()
    {
        try
        {
            showNotification(context, MainAppStatus.foreground.name());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        try
        {
            addLocationUpdates(context);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        while (!isCancelled)
        {
            synchronized (mutex)
            {
                try
                {
                    checkForPause();
                    if (this.mainAppStatus == MainAppStatus.background && mainAppStatusUpdateStartTime > 0
                            && (System.currentTimeMillis() - this.mainAppStatusUpdateStartTime) > 7200000L) // 2 hours
                    {
                        if (this.host != null)
                        {
                            this.host.setStatus(FriendStatus.END);
                        }
                        KontagentLogger.getInstance().addCustomEvent(IKontagentConstants.CATEGORY_DWF, IKontagentConstants.DWF_LOCATIONSHARE_END, 2);
                        this.stop(context);
                        break;
                    }
                    else if (lastOutsideUpdateTime > 0 && (System.currentTimeMillis() - this.lastOutsideUpdateTime) > 3600000L)// 60
                                                                                                         // minutes
                    {
                        if (this.host != null)
                        {
                            this.host.setStatus(FriendStatus.END);
                        }
                        
                        this.stop(context);
                        break;
                    }
                    else if (this.host != null && this.host.getStatus() == FriendStatus.ARRIVED
                            && this.mainAppStatus != MainAppStatus.dwfScreen
                            && (System.currentTimeMillis() - this.mainAppStatusUpdateStartTime) > 600000L)// 10 minutes
                    {
                        if (this.host != null)
                        {
                            this.host.setStatus(FriendStatus.END);
                        }
                        KontagentLogger.getInstance().addCustomEvent(IKontagentConstants.CATEGORY_DWF, IKontagentConstants.DWF_LOCATIONSHARE_END, 1);
                        this.stop(context);
                        break;
                    }

                    long delta = System.currentTimeMillis() - this.lastOutsideUpdateEtaTime;
                    if (lastOutsideUpdateEtaTime > 0 && delta > 30000) // we will not use service location when update the
                                                                    // status from the outside of
                    // service such as main acitivy < 30000.
                    {
                        this.host.setEta(-1);
                    }

                    if (isCancelled)
                    {
                        break;
                    }
                    
                    try
                    {
                        sendUserInfo();
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                    if (isCancelled)
                    {
                        break;
                    }

                    mutex.wait(this.interval);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }

        try
        {
            sendUserInfo();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        try
        {
            removeLocationUpdates(context);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        try
        {
            hideNotification(context);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location)
    {
        if (isBetterLocation(location, previousBestLocation))
        {
            previousBestLocation = location;

            if (host == null)
                return;

            long delta = System.currentTimeMillis() - this.lastOutsideUpdateLocationTime;
            if (delta > 3000 || (host.getLat() == 0 && host.getLon() == 0))
            {
                host.setLat(previousBestLocation.getLatitude());
                host.setLon(previousBestLocation.getLongitude());
            }
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras)
    {
    }

    @Override
    public void onProviderEnabled(String provider)
    {
    }

    @Override
    public void onProviderDisabled(String provider)
    {
    }

    public Intent getNotificationIntent()
    {
        if (!this.isStarted)
            return null;

        if (notificationIntent == null)
        {
            notificationIntent = new Intent();
            
            notificationIntent.setClass(context, TeleNav.class);
            
            notificationIntent.addCategory("com.telenav.intent.category.PLUGIN_LAUNCH");
            notificationIntent.setAction("com.telenav.intent.action.DWF_LIST");
            
            notificationIntent.putExtra(IMaiTai.KEY_DWF_SESSION_ID, this.sessionId);
            notificationIntent.putExtra(IMaiTai.KEY_DWF_USER_ID, this.host.getUid());
            notificationIntent.putExtra(IMaiTai.KEY_DWF_USER_KEY, this.host.getKey());
            notificationIntent.putExtra(IMaiTai.KEY_DWF_ADDRESS_FORMATDT, this.formattedDt);
        }

        return notificationIntent;
    }

    private void sendUserInfo()
    {
        UpdateSessionRequest request = new UpdateSessionRequest();
        request.setSessionId(sessionId);
        request.setFriend(host);

        DwfLogger.getInstance().log(DwfLogger.INFO, "UpdateSessionRequest : \n" + request);
        
        UpdateSessionResponse response = DwfNetworkUtil.requestSessionUpdate(url, request, host, this.context);
        
        DwfLogger.getInstance().log(DwfLogger.INFO, "UpdateSessionResponse : \n" + response);
        
        if (response != null && response.getStatus() == DwfResponseStatus.OK
                && mainAppStatus != MainAppStatus.background)
        {
            lastUpdateFriends = response.getFriends();
            
            Enumeration<DwfUpdateListener> enums = this.listeners.elements();
            DwfUpdateListener listener;
            while (enums.hasMoreElements())
            {
                try
                {
                    listener = enums.nextElement();
                    listener.updateDwf(response.getFriends());
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    @SuppressWarnings("deprecation")
    private void showNotification(Context context, String appStatus)
    {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notification = new Notification();
        notification.icon = R.drawable.app_notification;
        notification.tickerText = this.context.getText(R.string.dwfNotificationTicker);
        notification.flags = Notification.FLAG_NO_CLEAR;

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
            R.layout.dwf_notification_with_button);
        
        if (appStatus.equals(MainAppStatus.background.name()))
        {
            remoteViews.setTextViewText(R.id.dwf0NotificationTitle, context.getText(R.string.dwfNotificationTitle));
            remoteViews.setViewVisibility(R.id.dwf0NotificationContent, View.VISIBLE);
            remoteViews.setTextViewText(R.id.dwf0NotificationContent, context.getText(R.string.dwfNotificationContent));
            remoteViews.setViewVisibility(R.id.dwf0NotificationButton, View.VISIBLE);
        }
        else if (appStatus.equals(MainAppStatus.foreground.name()))
        {
            remoteViews.setTextViewText(R.id.dwf0NotificationTitle, context.getText(R.string.dwfNotificationTitle));
            remoteViews.setViewVisibility(R.id.dwf0NotificationContent, View.GONE);
            remoteViews.setViewVisibility(R.id.dwf0NotificationButton, View.GONE);
        }
        else if (appStatus.equals(MainAppStatus.dwfStop.name()))
        {
            remoteViews.setTextViewText(R.id.dwf0NotificationTitle, context.getText(R.string.dwfNotificationTimeoutTitle));
            remoteViews.setViewVisibility(R.id.dwf0NotificationContent, View.VISIBLE);
            remoteViews.setTextViewText(R.id.dwf0NotificationContent, context.getText(R.string.dwfNotificationTimeoutContent));
            remoteViews.setViewVisibility(R.id.dwf0NotificationButton, View.GONE);
        }
        remoteViews.setOnClickPendingIntent(R.id.dwf0NotificationButton, getActionIntent(context, remoteViews));
        notification.contentView = remoteViews;

        Intent notificationIntent = this.getNotificationIntent();

        int iUniqueId = (int) (System.currentTimeMillis() & 0xfffffff);

        PendingIntent contentIntent = PendingIntent.getActivity(context, iUniqueId, notificationIntent, 0);

        notification.contentIntent = contentIntent;

        notificationManager.notify(this.sessionId, NOTIFICATION_ID, notification);
    }
    
    private PendingIntent getActionIntent(Context context, RemoteViews remoteViews)
    {
        remoteViews.setImageViewResource(R.id.dwf0NotificationButton, isPaused ? R.drawable.switch_off_unfocused
                : R.drawable.switch_on_unfocused);
        return PendingIntent.getBroadcast(context, 0,
            new Intent("com.telenav.intent.action.DWF_UPDATE_ACTION").putExtra("isDwfStopped", !isPaused),
            PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private void hideNotification(Context context)
    {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.cancel(this.sessionId, NOTIFICATION_ID);
    }

    private void addLocationUpdates(final Context context)
    {
        SharedPreferences pre = context.getSharedPreferences("scout.dwf.preference", Context.MODE_PRIVATE);
        if (pre.getBoolean("scout.dwf.preference.location", false))
        {
            return;
        }
        Thread t = new Thread(new Runnable()
        {
            public void run()
            {
                Looper.prepare();

                locationLooper = Looper.myLooper();

                // In future, maybe need change this interval according to the share interval...
                LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
                if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
                {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 0,
                        DwfShareLocationTask.this);
                }
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
                {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, DwfShareLocationTask.this);
                }
                if (locationManager.isProviderEnabled(LocationManager.PASSIVE_PROVIDER))
                {
                    locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 10000, 0, DwfShareLocationTask.this);
                }
                Looper.loop();
            }
        });

        t.start();
    }

    private void removeLocationUpdates(Context context)
    {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        locationManager.removeUpdates(this);

        if (this.locationLooper != null)
        {
            this.locationLooper.quit();
            this.locationLooper = null;
        }
    }

    private boolean isBetterLocation(Location location, Location currentBestLocation)
    {
        if (currentBestLocation == null)
        {
            return true;
        }

        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > interval;
        boolean isSignificantlyOlder = timeDelta < -interval;
        boolean isNewer = timeDelta > 0;

        if (isSignificantlyNewer)
        {
            return true;
        }
        else if (isSignificantlyOlder)
        {
            return false;
        }

        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        boolean isFromSameProvider = isSameProvider(location.getProvider(), currentBestLocation.getProvider());

        if (isMoreAccurate)
        {
            return true;
        }
        else if (isNewer && !isLessAccurate)
        {
            return true;
        }
        else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider)
        {
            return true;
        }
        return false;
    }

    private boolean isSameProvider(String provider1, String provider2)
    {
        if (provider1 == null)
        {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }
}
