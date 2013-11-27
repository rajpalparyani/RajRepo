/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * RimTelephonyManager.java
 *
 */
package com.telenav.telephony.rim;

import java.util.Hashtable;

import javax.microedition.io.Connector;
import javax.wireless.messaging.MessageConnection;
import javax.wireless.messaging.TextMessage;

import net.rim.blackberry.api.browser.Browser;
import net.rim.blackberry.api.invoke.Invoke;
import net.rim.blackberry.api.invoke.MessageArguments;
import net.rim.blackberry.api.invoke.PhoneArguments;
import net.rim.blackberry.api.phone.Phone;
import net.rim.device.api.system.Alert;
import net.rim.device.api.system.DeviceInfo;
import net.rim.device.api.system.SIMCardException;
import net.rim.device.api.system.SIMCardInfo;

import com.telenav.logger.Logger;
import com.telenav.telephony.ITnApnListener;
import com.telenav.telephony.ITnPhoneListener;
import com.telenav.telephony.TnApnInfo;
import com.telenav.telephony.TnDeviceInfo;
import com.telenav.telephony.TnSimCardInfo;
import com.telenav.telephony.TnTelephonyManager;

/**
 * Provides access to the device's SimCard, Call, Mail etc. at rim platform.
 * 
 *@author fqming (fqming@telenav.cn)
 *@date Aug 17, 2010
 */
public class RimTelephonyManager extends TnTelephonyManager
{
    protected Hashtable listeners;
    
    /**
     * Construct the telephony manager at rim platform. <br />
     * <br />
     * <br />
     * Please make sure that grant below class's permission.
     * <br />
     * ApplicationPermissions#PERMISSION_PHONE;#PERMISSION_SECURITY_DATA;
     * <br />
     * 
     * @param context
     */
    public RimTelephonyManager()
    {
        
    }
    
    public void addListener(ITnPhoneListener phoneListener)
    {
        if (phoneListener == null)
            return;

        if (listeners == null)
        {
            listeners = new Hashtable();
        }

        RimPhoneListener rimPhoneListener = new RimPhoneListener(phoneListener);
        listeners.put(phoneListener, rimPhoneListener);
        
        Phone.addPhoneListener(rimPhoneListener);
    }

    public TnDeviceInfo getDeviceInfo()
    {
        TnDeviceInfo deviceInfo = new TnDeviceInfo();

        deviceInfo.platform = TnDeviceInfo.PLATFORM_RIM;
        
        deviceInfo.deviceId = "" + DeviceInfo.getDeviceId();
        deviceInfo.deviceName = DeviceInfo.getDeviceName();
        
        deviceInfo.isSimulator = DeviceInfo.isSimulator();
        
        deviceInfo.manufacturerName = DeviceInfo.getManufacturerName();
        
        deviceInfo.platformVersion = DeviceInfo.getPlatformVersion();
        deviceInfo.softwareVersion = DeviceInfo.getSoftwareVersion();

        return deviceInfo;
    }

    public String getPhoneNumber()
    {
        String phoneNumber = Phone.getDevicePhoneNumber(false);
        
        return phoneNumber;
    }

    public TnSimCardInfo getSimCardInfo()
    {
        TnSimCardInfo simCardInfo = new TnSimCardInfo();
        try
        {
            simCardInfo.simCardId = new String(SIMCardInfo.getIMSI());
        }
        catch (SIMCardException e)
        {
            Logger.log(this.getClass().getName(), e);
        }

        return simCardInfo;
    }

    public void removeListener(ITnPhoneListener phoneListener)
    {
        if (phoneListener == null || listeners == null)
            return;

        RimPhoneListener rimPhoneListener = (RimPhoneListener) listeners.get(phoneListener);
        if (rimPhoneListener == null)
            return;

        listeners.remove(phoneListener);
        
        Phone.removePhoneListener(rimPhoneListener);
    }

    public void startBrowser(String url)
    {
        Browser.getDefaultSession().displayPage(url);
    }

    public void startCall(String phoneNumber)
    {
        PhoneArguments call = new PhoneArguments(PhoneArguments.ARG_CALL, phoneNumber);
        Invoke.invokeApplication(Invoke.APP_TYPE_PHONE, call);
    }
    
    public void startMMS(String sentTo, String message)
    {
        if (sentTo == null || sentTo.length() == 0)
        {
            throw new IllegalArgumentException("the sentTo is empty.");
        }
        
        MessageArguments arguments = new MessageArguments(MessageArguments.ARG_NEW_MMS, sentTo, "", message);
        Invoke.invokeApplication(Invoke.APP_TYPE_MESSAGES, arguments);
    }
    
//    public void startMMS(String sentTo, String message, byte[] attachData, String streamType)
//    {
//        if (sentTo == null || sentTo.length() == 0)
//        {
//            throw new IllegalArgumentException("the sentTo is empty.");
//        }
//        
//        MessagePart message = new MessagePart(attachData, streamType, "", null, null);
//        
//        MessageArguments arguments = new MessageArguments(MessageArguments.ARG_NEW_MMS, sentTo, "", message);
//        Invoke.invokeApplication(Invoke.APP_TYPE_MESSAGES, arguments);
//    }
    
    public void startMMSAtBackground(String sentTo, String message)
    {
        String url = "sms://" + sentTo; //such as:"sms://+4113501667172"
        MessageConnection conn = null;
        try
        {
            conn = (MessageConnection) Connector.open(url);
            TextMessage msg = (TextMessage) conn.newMessage(MessageConnection.TEXT_MESSAGE);
            msg.setPayloadText(message);
            conn.send(msg);
        }
        catch(Throwable e)
        {
            Logger.log(this.getClass().getName(), e);
        }
        finally
        {
            if(conn != null)
            {
                try
                {
                    conn.close();
                }
                catch (Throwable e)
                {
                    Logger.log(this.getClass().getName(), e);
                }
                conn = null;
            }
        }
    }

    public void startEmail(String sentTo, String subject, String content)
    {
        if (sentTo == null || sentTo.length() == 0)
        {
            throw new IllegalArgumentException("the sentTo is empty.");
        }
        
        MessageArguments messageArg = new MessageArguments(MessageArguments.ARG_NEW, sentTo, subject, content);
        
        Invoke.invokeApplication(Invoke.APP_TYPE_MESSAGES, messageArg);        
    }

    public void vibrate(int durationTime)
    {
        if(durationTime <= 0 )
            return;
        
        if(Alert.isVibrateSupported())
        {
            Alert.startVibrate(durationTime);
        }
    }

    public void addApnListener(ITnApnListener listener)
    {
    }

    public void removeApnListener(ITnApnListener listener)
    {
    }

    public String insertApn(TnApnInfo apn, Hashtable extras)
    {
        return null;
    }

    public void setPrefered(String apnId)
    {
    }

    protected TnApnInfo[] getMatchedApn(String apn, String type, String mcc, String mnc)
    {
        return null;
    }

    protected String getSimOperator()
    {
        return null;
    }

    protected TnApnInfo getPreferredApn(String apn, String type, String mcc, String mnc)
    {
        return null;
    }

	public String getIpAddress()
    {
		return null;
	}
}
