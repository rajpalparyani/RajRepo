/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * AndroidTelephonyManager.java
 *
 */
package com.telenav.telephony.android;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Vibrator;
import android.telephony.PhoneStateListener;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.format.Formatter;

import com.telenav.logger.Logger;
import com.telenav.telephony.ITnApnListener;
import com.telenav.telephony.ITnPhoneListener;
import com.telenav.telephony.TnApnInfo;
import com.telenav.telephony.TnDeviceInfo;
import com.telenav.telephony.TnSimCardInfo;
import com.telenav.telephony.TnTelephonyManager;

/**
 * Provides access to the device's SimCard, Call, Mail etc. at android platform.
 * 
 *@author fqming (fqming@telenav.cn)
 *@date Jul 12, 2010
 */
public class AndroidTelephonyManager extends TnTelephonyManager
{
    
    protected static final Uri URI_APN_TABLE = Uri.parse("content://telephony/carriers");

    protected static final Uri URI_APN_PREFER = Uri.parse("content://telephony/carriers/preferapn");
    
    protected Context context;

    protected Hashtable listeners;
    
    protected Vector apnListeners;

    private ContentObserver observer;
    /**
     * Construct the telephony manager at android platform. <br />
     * <br />
     * Please make sure that grant below class's permissions. <br />
     * TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
     * 
     * @param context
     */
    public AndroidTelephonyManager(Context context)
    {
        this.context = context;
    }

    public void addListener(ITnPhoneListener phoneListener)
    {
        if (phoneListener == null)
            return;

        if (listeners == null)
        {
            listeners = new Hashtable();
        }

        AndoidPhoneListener androidPhoneListener = new AndoidPhoneListener(phoneListener);
        listeners.put(phoneListener, androidPhoneListener);

        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        tm.listen(androidPhoneListener, PhoneStateListener.LISTEN_CALL_STATE);
    }

    public TnDeviceInfo getDeviceInfo()
    {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        TnDeviceInfo deviceInfo = new TnDeviceInfo();

        deviceInfo.platform = TnDeviceInfo.PLATFORM_ANDROID;
        
        deviceInfo.deviceId = tm.getDeviceId();
        deviceInfo.deviceName = Build.DEVICE;
        deviceInfo.releaseVersion = Build.VERSION.RELEASE;
        
        deviceInfo.isSimulator = "000000000000000".equals(tm.getDeviceId()); 
        
        if (Integer.parseInt(Build.VERSION.SDK) >= 4)
        {
            deviceInfo.manufacturerName = AndroidTelephonyUtil.getManufacturerName();
        }
        
        deviceInfo.platformVersion = "" + Build.VERSION.SDK;
        deviceInfo.softwareVersion = tm.getDeviceSoftwareVersion();
        
        deviceInfo.systemLocale = context.getResources().getConfiguration().locale.getLanguage();

        return deviceInfo;
    }

    public String getPhoneNumber()
    {
        String phoneNumber = "";
        try
        {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

            phoneNumber = tm.getLine1Number();
        }
        catch (Exception e)
        {
            Logger.log(this.getClass().getName(), e);
        }

        return phoneNumber;
    }

    public TnSimCardInfo getSimCardInfo()
    {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        TnSimCardInfo simCardInfo = new TnSimCardInfo();
        simCardInfo.simCardId = tm.getSubscriberId();
        simCardInfo.countryIso = tm.getSimCountryIso();
        simCardInfo.operator = tm.getSimOperator();
        simCardInfo.operatorName = tm.getSimOperatorName();
        
        return simCardInfo;
    }

    public void removeListener(ITnPhoneListener phoneListener)
    {
        if (phoneListener == null || listeners == null)
            return;

        AndoidPhoneListener androidPhoneListener = (AndoidPhoneListener) listeners.get(phoneListener);
        if (androidPhoneListener == null)
            return;

        listeners.remove(phoneListener);
        
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        tm.listen(androidPhoneListener, PhoneStateListener.LISTEN_NONE);
    }

    public void startBrowser(String url)
    {
        if (url == null || url.length() == 0)
        {
            throw new IllegalArgumentException("the url is empty.");
        }

        try
        {
            if (!url.toLowerCase().startsWith("http"))
            {
                url = "http://" + url;
            }
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addCategory(Intent.CATEGORY_BROWSABLE);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Uri u = Uri.parse(url);
            intent.setData(u);
            context.startActivity(intent);
        }
        catch (Exception e)
        {
            Logger.log(this.getClass().getName(), e);
        }
    }

    public void startCall(String phoneNumber)
    {
        if (phoneNumber == null || phoneNumber.length() == 0)
        {
            throw new IllegalArgumentException("the phone number is empty.");
        }

        if (PackageManager.PERMISSION_DENIED == this.context.getPackageManager()
                .checkPermission("android.permission.CALL_PHONE",
                    this.context.getPackageName()))
        {
            return;
        }
        
        try
        {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + phoneNumber));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            this.context.startActivity(intent);
        }
        catch (Exception e)
        {
            Logger.log(this.getClass().getName(), e);
        }

    }
    
    public void startMMS(String sentTo, String message)
    {
        if (sentTo == null || sentTo.length() == 0)
        {
            throw new IllegalArgumentException("the sentTo is empty.");
        }
        
        if (PackageManager.PERMISSION_DENIED == this.context.getPackageManager()
                .checkPermission("android.permission.SEND_SMS",
                    this.context.getPackageName()))
        {
            return;
        }
        
        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
        sendIntent.putExtra("address", sentTo);
        sendIntent.putExtra("sms_body", message);
        sendIntent.setType("vnd.android-dir/mms-sms");
        
        this.context.startActivity(sendIntent);
    }
    
    public void startMMS(String sentTo, String message, byte[] attachData, String streamType)
    {
        if (sentTo == null || sentTo.length() == 0)
        {
            throw new IllegalArgumentException("the sentTo is empty.");
        }
        
        if (PackageManager.PERMISSION_DENIED == this.context.getPackageManager()
                .checkPermission("android.permission.SEND_SMS",
                    this.context.getPackageName()))
        {
            return;
        }
        
        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.putExtra("address", sentTo);
        sendIntent.putExtra("sms_body", message);
        
        if(attachData != null)
        {
            String name = "tmp_" + System.currentTimeMillis();
            FileOutputStream fileOutputStream = null;
            try
            {
                if ("image/png".equals(streamType))
                {
                    name += ".jpg";
                }
                else
                {
                    name += ".dat";
                }
                fileOutputStream = this.context.openFileOutput(name, Context.MODE_WORLD_READABLE);
                fileOutputStream.write(attachData);
                fileOutputStream.close();
            }
            catch (FileNotFoundException e)
            {
                Logger.log(this.getClass().getName(), e);
            }
            catch(IOException e)
            {
                Logger.log(this.getClass().getName(), e);
            }
            File tempFile = this.context.getFileStreamPath(name);
            sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(tempFile));
        }
        
        sendIntent.setType(streamType);
        this.context.startActivity(sendIntent);
    }

    public void startMMSAtBackground(String sentTo, String message)
    {
        if (sentTo == null || sentTo.length() == 0)
        {
            throw new IllegalArgumentException("the sentTo is empty.");
        }
        
        if (PackageManager.PERMISSION_DENIED == this.context.getPackageManager()
                .checkPermission("android.permission.SEND_SMS",
                    this.context.getPackageName()))
        {
            return;
        }
        
        ArrayList list = SmsManager.getDefault().divideMessage(message);
        
        for(int i = 0; i < list.size(); i++)
        {
            String s = (String)list.get(i);
            SmsManager.getDefault().sendTextMessage(sentTo, null, s, null, null);
        }
    }
    
    public void startEmail(String sentTo, String subject, String content)
    {
        if (sentTo == null || sentTo.length() == 0)
        {
            throw new IllegalArgumentException("the sentTo is empty.");
        }
        
        String[] mailto = { sentTo };
        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_EMAIL, mailto);
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        sendIntent.putExtra(Intent.EXTRA_TEXT, content);
        sendIntent.setType("text/plain");
        this.context.startActivity(Intent.createChooser(sendIntent, "@"));
    }

    public void vibrate(int durationTime)
    {
        if(durationTime <= 0)
            return;
        
        ((Vibrator) this.context.getSystemService(Context.VIBRATOR_SERVICE)).vibrate(durationTime);
    }

    public String insertApn(TnApnInfo apn, Hashtable extras)
    {
        if(!checkWriteApnPermission())
        {
            return null;
        }
        ContentValues row = createRowFromApn(apn);
        if(extras != null)
        {
            for(Enumeration keys = extras.keys();keys.hasMoreElements();)
            {
                String key = (String)keys.nextElement();
                Object val = extras.get(key);
                if(val!= null)
                {
                    row.put(key,val.toString());
                }
            }
        }
        Uri newRow = context.getContentResolver().insert(URI_APN_TABLE, row);
        if (newRow != null)
        {
            Cursor c = context.getContentResolver().query(newRow, null, null, null, null);
            try
            {
                int idindex = c.getColumnIndex("_id");
                c.moveToFirst();
                return c.getString(idindex);
            }
            finally
            {
                if (c != null)
                {
                    c.close();
                }
            }
        }
        return null;
    }

    public void setPrefered(String apnId)
    {
        if(!checkWriteApnPermission())
        {
            return ;
        }
        ContentResolver resolver = context.getContentResolver();
        try
        {
            ContentValues values = new ContentValues();
            values.put("apn_id", apnId);
            resolver.update(URI_APN_PREFER, values, null, null);
        }
        catch (Exception e)
        {
        }
    }
    
    public void addApnListener(ITnApnListener listener)
    {
        if(listener == null)
        {
            return;
        }
        synchronized (this)
        {
            if(apnListeners == null)
            {
                apnListeners = new Vector();
            }
            else if(apnListeners.contains(listener))
            {
                return;
            }
            apnListeners.addElement(listener);
            if(apnListeners.size() > 0)
            {
                if(observer == null)
                {
                    observer = new ApnObserver(new Handler(context.getMainLooper()));
                    context.getContentResolver().registerContentObserver(URI_APN_PREFER, true, observer);
                }
            }
        }
    }
    
    public void removeApnListener(ITnApnListener listener)
    {
        if (listener == null)
        {
            return;
        }
        synchronized (this)
        {
            if(apnListeners == null)
            {
                return;
            }
            apnListeners.remove(listener);
            if (apnListeners.size() == 0)
            {
                if (observer != null)
                {
                    context.getContentResolver().unregisterContentObserver(observer);
                    observer = null;
                }
            }
        }
    }

    protected TnApnInfo[] getMatchedApn(String apn, String type, String mcc, String mnc)
    {
        return findApnFromUri(URI_APN_TABLE, apn, type, mcc, mnc);
    }

    protected TnApnInfo getPreferredApn(String apn, String type, String mcc, String mnc)
    {
        TnApnInfo[] apns = findApnFromUri(URI_APN_PREFER, apn, type, mcc, mnc);
        for(int i = 0;i<apns.length;i++)
        {
            if(!"mms".equals(apns[i].getApType()))
            {
                return apns[i];
            }
        }
        return null;
    }
    
    protected String getSimOperator()
    {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getSimOperator();
    }
    
    private ContentValues createRowFromApn(TnApnInfo apn)
    {
        String mcc = apn.getMcc();
        String mnc = apn.getMnc();
        ContentValues row = new ContentValues();
        row.put("name", apn.getName());
        row.put("apn", apn.getApn());
        row.put("numeric", new Integer(mcc + mnc));
        row.put("mcc", mcc);
        row.put("mnc", mnc);
        row.put("user", apn.getUser());
        row.put("server", apn.getServer());
        row.put("password", apn.getPassword());
        row.put("mmsproxy", apn.getMmsproxy());
        row.put("mmsport", apn.getMmsport());
        row.put("mmsc", apn.getMmsc());

        row.put("proxy", apn.getProxy());
        row.put("port", apn.getPort());
        row.put("type", apn.getApType());
        return row;
    }
    
    private void copyApnFromCursor(Cursor cr, TnApnInfo apn)
    {
        try
        {
            apn.setId(cr.getString(cr.getColumnIndex("_id")));
            apn.setName(cr.getString(cr.getColumnIndex("name")));
            apn.setApn(cr.getString(cr.getColumnIndex("apn")));
            apn.setMcc(cr.getString(cr.getColumnIndex("mcc")));
            apn.setMnc(cr.getString(cr.getColumnIndex("mnc")));
            apn.setProxy(cr.getString(cr.getColumnIndex("proxy")));
            apn.setPort(cr.getString(cr.getColumnIndex("port")));
            apn.setApType(cr.getString(cr.getColumnIndex("type")));
            apn.setUser(cr.getString(cr.getColumnIndex("user")));
            apn.setServer(cr.getString(cr.getColumnIndex("server")));
            apn.setPassword(cr.getString(cr.getColumnIndex("password")));
        }
        catch (Exception e) {
            Logger.log(getClass().getName(),e);
        }
    }

    protected TnApnInfo[] findApnFromUri(Uri uri, String apn, String type, String mcc, String mnc)
    {
        boolean needAnd = false;
        StringBuilder ql = new StringBuilder();
        int argIndex = 0;
        String[] tags = new String[]
        { "apn=?", "type=?", "mcc=?", "mnc=?" };
        String[] vals = new String[]
        { apn, type, mcc, mnc };
        for (int i = 0; i < tags.length; i++)
        {
            String val = vals[i];
            if (val != null)
            {
                // since argIndex must smaller than i, so vals can be reused.
                vals[argIndex++] = val;
                ql.append(needAnd ? " and " + tags[i] : tags[i]);
                needAnd = true;
            }
        }
        String[] args = new String[argIndex];
        System.arraycopy(vals, 0, args, 0, argIndex);
        Cursor cr = context.getContentResolver().query(uri, null, ql.toString(), args, null);
        try
        {
            TnApnInfo[] result = new TnApnInfo[cr.getCount()];
            int index = 0;
            if (cr.moveToFirst())
            {
                do
                {
                    TnApnInfo ret = new TnApnInfo();
                    copyApnFromCursor(cr, ret);
                    result[index++] = ret;
                }
                while(cr.moveToNext());
                return result;
            }
        }
        finally
        {
            if (cr != null)
            {
                cr.close();
            }
        }
        return new TnApnInfo[0];
    }

    private boolean checkWriteApnPermission()
    {
        return context.getPackageManager().checkPermission("android.permission.WRITE_APN_SETTINGS", context.getPackageName()) != PackageManager.PERMISSION_DENIED;
    }
    
    public String getIpAddress() 
    {
        try
        {
            Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
            if (en == null)
            {
                return null;
            }
            while(en.hasMoreElements()) 
            {
                Enumeration<InetAddress> enumIpAddr = en.nextElement().getInetAddresses();
                while(enumIpAddr.hasMoreElements())
                {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress())
                    {
                        return Formatter.formatIpAddress(inetAddress.hashCode());
                    }
                }
            }
        } 
        catch (Exception ex) 
        {
            Logger.log(getClass().getName(),ex);
        }
        return null;
    }

    class ApnObserver extends ContentObserver
    {

        /**
         * @param handler
         */
        public ApnObserver(Handler handler)
        {
            super(handler);
            // TODO Auto-generated constructor stub
        }
        
        public void onChange(boolean selfChange)
        {
            TnApnInfo apn = TnTelephonyManager.getInstance().getPreferredApn(null, null);
            ITnApnListener[] listenersArray;
            synchronized (AndroidTelephonyManager.this)
            {
                if(apnListeners == null)
                {
                    return;
                }
                listenersArray = new ITnApnListener[apnListeners.size()];
                apnListeners.toArray(listenersArray);
            }
            for(int i = 0;i<listenersArray.length;i++)
            {
                ITnApnListener listener = listenersArray[i];
                listener.onChanged(apn);
            }
        }
    }
}
