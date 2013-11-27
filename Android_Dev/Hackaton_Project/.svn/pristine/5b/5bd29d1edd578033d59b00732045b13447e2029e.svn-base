/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * MdotmReceiver.java
 *
 */
package com.telenav.app.android.scout_us;

/**
 *@author xlliu
 *@date 2012-11-2
 */
import java.net.URLEncoder;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.telenav.logger.Logger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.provider.Settings;

public class MdotmReceiver extends BroadcastReceiver
{

    @Override
    public void onReceive(Context context, Intent intent)
    {
        Logger.log(Logger.INFO, this.getClass().getName(), "=====Mdotm======onReceive====");
        
        String referrer = "";
        String deviceId = "0";
        String androidId = "0";
        String packageName = "null_package";
        
        try
        {
            referrer = intent.getStringExtra("referrer");
            if (referrer == null)
            {
                referrer = "null_referrer_found";
            }
        }
        catch (Exception e)
        {
            referrer = "exception_found_retrieving_referrer";
        }

        try
        {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            deviceId = telephonyManager.getDeviceId();
            if (deviceId == null)
                deviceId = "0";
        }
        catch (Exception e)
        {
            Logger.log(Logger.INFO, this.getClass().getName(), e.getMessage());
        }

        try
        {
            androidId = Settings.Secure.getString(context.getContentResolver(),Settings.Secure.ANDROID_ID);
            if (androidId == null)
                androidId = "0";
        }
        catch (Exception e)
        {
            Logger.log(Logger.INFO, this.getClass().getName(), e.getMessage());
        }

        Context applicationContext = context.getApplicationContext();
        if (applicationContext != null)
        {
            packageName = applicationContext.getPackageName();
        }

        final String postBackUrl = "http://ads.mdotm.com/ads/receiver.php?referrer="
                + URLEncoder.encode(referrer) + "&package="
                + URLEncoder.encode(packageName) + "&deviceid="
                + URLEncoder.encode(deviceId) + "&androidid="
                + URLEncoder.encode(androidId);
        
        Logger.log(Logger.INFO, this.getClass().getName(), "=====Mdotm======postBackUrl====" + postBackUrl);
        
        new Thread()
        {
            public void run()
            {
                try
                {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpGet httpGet = new HttpGet(postBackUrl);
                    HttpResponse httpResponse = httpClient.execute(httpGet);
                    Logger.log(Logger.INFO, this.getClass().getName(), "====Mdotm httpResponse StatusCode======" + httpResponse.getStatusLine().getStatusCode());
                }
                catch (Exception e)
                {
                    Logger.log(Logger.INFO, this.getClass().getName(), "====Mdotm postBackUrl error======" + e.getMessage());
                }
            }
        }.start();
    }
}