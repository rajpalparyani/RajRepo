/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * AndroidNotificationManager.java
 *
 */
package com.telenav.app.android.scout_us;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.telenav.app.NotificationManager;
import com.telenav.module.marketbilling.IMarketBillingConstants;

/**
 * @author xinrongl (xinrongl@telenav.com)
 * @date Mar 8, 2011
 */

public class AndroidNotificationManager extends NotificationManager
{
    private static int MOOD_NOTIFICATIONS = 1234;
    
    private android.app.NotificationManager notifier;    
    private Context context;
    
    private String title;
    private String label;
    
    public AndroidNotificationManager(Context context)
    {
        this.context = context;
        
        notifier = (android.app.NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }
    
    public void showNotification(String title, String label)
    {
        if (this.title != null && this.title.length() > 0 && this.title.equals(title)
                && this.label != null && this.label.length() > 0 && this.label.equals(label))
        {
            return;
        }
        
        this.title = title;
        this.label = label;
      
        Notification notification = new Notification(R.drawable.app_notification, null, System.currentTimeMillis());

        // The PendingIntent to launch our activity if the user selects this notification
        Intent notificationIntent = new Intent(context, TeleNav.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        notificationIntent.setAction(Intent.ACTION_MAIN);
        notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);

        // Set the info for the views that show in the notification panel.
        notification.setLatestEventInfo(context, title, label, contentIntent);
        notification.flags = Notification.FLAG_ONGOING_EVENT;
        // Send the notification.
        // We use a layout id because it is a unique number. We use it later to cancel.
        notifier.notify(MOOD_NOTIFICATIONS, notification);
    }
    
    public void showMarketBillingNotification(String lable, String notifyMsg, String action)
    {
        Notification notification = new Notification(R.drawable.app_notification, notifyMsg, System.currentTimeMillis());
        Intent newIntent = null;
        if(IMarketBillingConstants.ACTION_BILLING_SHOW_NOTIFY_RECEIVED.equals(action))
        {
            newIntent = new Intent(context, TeleNav.class);
        }
        if(newIntent != null)
        {
            newIntent.setAction(action);
            PendingIntent contentIntent = PendingIntent.getActivity(context, 0, newIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            notification.setLatestEventInfo(context, context.getText(R.string.app_name), lable, contentIntent);
            notifier.notify("TN_ANDROID_BILING",
                IMarketBillingConstants.ANDROID_BILLING_NOTIFICATION_ID, notification);
        }
    }
    
    public void cancelMarketBillingNotification()
    {
        notifier.cancel("TN_ANDROID_BILING", IMarketBillingConstants.ANDROID_BILLING_NOTIFICATION_ID);
    }

    public void cancelNotification()
    {
        notifier.cancel(MOOD_NOTIFICATIONS);
    }
}
