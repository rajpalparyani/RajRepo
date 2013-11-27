/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * AndroidTelephonyManagerTest.java
 *
 */
package com.telenav.telephony.android;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Locale;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;
import android.telephony.PhoneStateListener;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;

import com.telenav.logger.Logger;
import com.telenav.telephony.ITnPhoneListener;
import com.telenav.telephony.TnDeviceInfo;
import com.telenav.telephony.TnSimCardInfo;

/**
 *@author Casper (pwang@telenav.cn)
 *@date 2011-7-11
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({AndroidTelephonyManager.class, Uri.class, Intent.class, AndroidTelephonyUtil.class, Configuration.class, Build.VERSION.class, Build.class, Logger.class, Uri.class, SmsManager.class})
public class AndroidTelephonyManagerTest
{
    public static final String NO_PERMISSION = "No permission!";
    Context mockContext;
    TelephonyManager mockTelephonyManager;
    AndroidTelephonyManager androidTelephonyManager;
    
    @BeforeClass
    public static void setUpClass()
    {
        PowerMock.mockStatic(Uri.class);
        Uri uri1 = PowerMock.createMock(Uri.class);
        Uri uri2 = PowerMock.createMock(Uri.class);
        EasyMock.expect(Uri.parse("content://telephony/carriers")).andReturn(uri1);
        EasyMock.expect(Uri.parse("content://telephony/carriers/preferapn")).andReturn(uri2);
        PowerMock.replay(Uri.class);
    }
    
    @Before
    public void setUp()
    {
        mockContext = PowerMock.createMock(Context.class);
        mockTelephonyManager = PowerMock.createMock(TelephonyManager.class);
        androidTelephonyManager = new AndroidTelephonyManager(mockContext);
    }
    
    @Test
    public void testConstructor()
    {
        PowerMock.replayAll();
        assertEquals(Whitebox.getInternalState(androidTelephonyManager, "context"), mockContext);
        PowerMock.verifyAll();
    }
    
    @Test
    public void testAddListener()
    {
        ITnPhoneListener mockTnPhoneListener = PowerMock.createMock(ITnPhoneListener.class);
        AndoidPhoneListener mockAndoidPhoneListener = PowerMock.createMock(AndoidPhoneListener.class);
        try
        {
            PowerMock.expectNew(AndoidPhoneListener.class, mockTnPhoneListener).andReturn(mockAndoidPhoneListener);
        }
        catch (Exception e)
        {
            fail(e.getMessage());
        }
        
        EasyMock.expect(mockContext.getSystemService(Context.TELEPHONY_SERVICE)).andReturn(mockTelephonyManager);
        mockTelephonyManager.listen(mockAndoidPhoneListener, PhoneStateListener.LISTEN_CALL_STATE);
        PowerMock.replayAll();
        
        androidTelephonyManager.listeners = null;
        androidTelephonyManager.addListener(null);
        assertNull(androidTelephonyManager.listeners);
        androidTelephonyManager.addListener(mockTnPhoneListener);
        assertNotNull(androidTelephonyManager.listeners);
        assertEquals(androidTelephonyManager.listeners.get(mockTnPhoneListener), mockAndoidPhoneListener);
        PowerMock.verifyAll();
    }
    
    @Test
    public void testGetDeviceInfo()
    {
        EasyMock.expect(mockContext.getSystemService(Context.TELEPHONY_SERVICE)).andReturn(mockTelephonyManager).times(2);
        EasyMock.expect(mockTelephonyManager.getDeviceId()).andReturn("12345").times(2).andReturn("000000000000000").times(2);
        PowerMock.mockStatic(AndroidTelephonyUtil.class);
        EasyMock.expect(AndroidTelephonyUtil.getManufacturerName()).andReturn("Nokia");
        EasyMock.expect(mockTelephonyManager.getDeviceSoftwareVersion()).andReturn("2.2").andReturn("1.6");
        
        Resources mockResources = PowerMock.createMock(Resources.class);
        PowerMock.mockStatic(Configuration.class);
        Configuration mockConfiguration = PowerMock.createMock(Configuration.class);
        Locale mockLocale = PowerMock.createMock(Locale.class);
        EasyMock.expect(mockContext.getResources()).andReturn(mockResources).times(2);
        EasyMock.expect(mockResources.getConfiguration()).andReturn(mockConfiguration).times(2);
        mockConfiguration.locale = mockLocale;
        EasyMock.expect(mockLocale.getLanguage()).andReturn("English").andReturn("Chinese");
        
        PowerMock.replayAll();
        
        
        Field deviceField = PowerMock.field(Build.class, "DEVICE");
        Field buildField = PowerMock.field(Build.VERSION.class, "SDK");
        try
        {
            deviceField.set(Whitebox.newInstance(TnDeviceInfo.class), "N82");
            buildField.set(Whitebox.newInstance(Build.VERSION.class), "4");
        }
        catch (Exception e)
        {
            fail(e.toString());
        }
        TnDeviceInfo deviceInfo = androidTelephonyManager.getDeviceInfo();
        assertEquals(TnDeviceInfo.PLATFORM_ANDROID, deviceInfo.platform);
        assertEquals("12345", deviceInfo.deviceId);
        assertEquals("N82", deviceInfo.deviceName);
        assertEquals(false, deviceInfo.isSimulator);
        assertEquals("Nokia", deviceInfo.manufacturerName);
        assertEquals("4", deviceInfo.platformVersion);
        assertEquals("2.2", deviceInfo.softwareVersion);
        assertEquals("English", deviceInfo.systemLocale);
        
        try
        {
            deviceField.set(Whitebox.newInstance(TnDeviceInfo.class), "N95");
            buildField.set(Whitebox.newInstance(Build.VERSION.class), "3");
        }
        catch (Exception e)
        {
            fail(e.toString());
        }
        deviceInfo = androidTelephonyManager.getDeviceInfo();
        assertEquals(TnDeviceInfo.PLATFORM_ANDROID, deviceInfo.platform);
        assertEquals("000000000000000", deviceInfo.deviceId);
        assertEquals("N95", deviceInfo.deviceName);
        assertEquals(true, deviceInfo.isSimulator);
        assertEquals(null, deviceInfo.manufacturerName);
        assertEquals("3", deviceInfo.platformVersion);
        assertEquals("1.6", deviceInfo.softwareVersion);
        assertEquals("Chinese", deviceInfo.systemLocale);
        PowerMock.verifyAll();
    }
    
    @Test
    public void testGetPhoneNumber()
    {
        PowerMock.mockStatic(Logger.class);
        Logger.log(EasyMock.anyObject(String.class), EasyMock.anyObject(RuntimeException.class));
        EasyMock.expect(mockContext.getSystemService(Context.TELEPHONY_SERVICE)).andThrow(new RuntimeException()).andReturn(mockTelephonyManager);
        EasyMock.expect(mockTelephonyManager.getLine1Number()).andReturn("1234567890");
        PowerMock.replayAll();

        androidTelephonyManager.getPhoneNumber();
        assertEquals("1234567890", androidTelephonyManager.getPhoneNumber());;
        PowerMock.verifyAll();
    }
    
    @Test
    public void testGetSimCardInfo()
    {
        EasyMock.expect(mockContext.getSystemService(Context.TELEPHONY_SERVICE)).andReturn(mockTelephonyManager);
        EasyMock.expect(mockTelephonyManager.getSubscriberId()).andReturn("123456");
        EasyMock.expect(mockTelephonyManager.getSimCountryIso()).andReturn("CN");
        EasyMock.expect(mockTelephonyManager.getSimOperator()).andReturn("CMMC");
        EasyMock.expect(mockTelephonyManager.getSimOperatorName()).andReturn("China Mobile");
        PowerMock.replayAll();
        
        TnSimCardInfo tnSimCardInfo = androidTelephonyManager.getSimCardInfo();
        assertEquals("123456", tnSimCardInfo.simCardId);
        assertEquals("CN", tnSimCardInfo.countryIso);
        assertEquals("CMMC", tnSimCardInfo.operator);
        assertEquals("China Mobile", tnSimCardInfo.operatorName);
        PowerMock.verifyAll();

        
    }
    
    @Test
    public void testRemoveListener()
    {
        ITnPhoneListener mockTnPhoneListener = PowerMock.createMock(ITnPhoneListener.class);
        ITnPhoneListener mockTnPhoneListener2 = PowerMock.createMock(ITnPhoneListener.class);
        EasyMock.expect(mockContext.getSystemService(Context.TELEPHONY_SERVICE)).andReturn(mockTelephonyManager);
        AndoidPhoneListener androidPhoneListener = Whitebox.newInstance(AndoidPhoneListener.class);
        mockTelephonyManager.listen(androidPhoneListener, PhoneStateListener.LISTEN_NONE);
        PowerMock.replayAll();

        androidTelephonyManager.listeners = new Hashtable();
        androidTelephonyManager.listeners.put(mockTnPhoneListener, androidPhoneListener);
        androidTelephonyManager.removeListener(null);
        assertTrue(androidTelephonyManager.listeners.containsValue(androidPhoneListener));
        androidTelephonyManager.removeListener(mockTnPhoneListener2);
        assertTrue(androidTelephonyManager.listeners.containsValue(androidPhoneListener));
        androidTelephonyManager.removeListener(mockTnPhoneListener);
        assertFalse(androidTelephonyManager.listeners.containsValue(androidPhoneListener));
        assertEquals(0, androidTelephonyManager.listeners.size());
        
        androidTelephonyManager.listeners = null;
        androidTelephonyManager.removeListener(mockTnPhoneListener2);
        assertNull(androidTelephonyManager.listeners);
        PowerMock.verifyAll();

    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testStartBrowserUrlNull()
    {
        androidTelephonyManager.startBrowser(null);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testStartBrowserUrlEmpty()
    {
        androidTelephonyManager.startBrowser("");
    }
    
    @Test
    public void testStartBrowser()
    {
        Intent mockIntent = PowerMock.createMock(Intent.class);
        Uri mockUri = PowerMock.createMock(Uri.class);
        try
        {
            PowerMock.expectNew(Intent.class, Intent.ACTION_VIEW).andReturn(mockIntent).times(3);
        }
        catch (Exception e)
        {
            fail(e.getMessage());
        }
        EasyMock.expect(mockIntent.addCategory(Intent.CATEGORY_BROWSABLE)).andReturn(mockIntent).times(3);
        EasyMock.expect(mockIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)).andReturn(mockIntent).times(3);
        EasyMock.expect(mockIntent.setData(mockUri)).andReturn(mockIntent).times(2);
        
        PowerMock.mockStatic(Uri.class);
        EasyMock.expect(Uri.parse(EasyMock.anyObject(String.class))).andReturn(mockUri).times(2).andThrow(new RuntimeException());
        mockContext.startActivity(mockIntent);
        PowerMock.expectLastCall().times(2);
        PowerMock.mockStatic(Logger.class);
        Logger.log(EasyMock.anyObject(String.class), EasyMock.anyObject(RuntimeException.class));
        PowerMock.replayAll();

        androidTelephonyManager.startBrowser("http://www.163.com");
        androidTelephonyManager.startBrowser("www.163.com");
        androidTelephonyManager.startBrowser("1234567");
        PowerMock.verifyAll();
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testStartCallPhoneNumberNull()
    {
        androidTelephonyManager.startCall(null);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testStartCallPhoneNumberEmpty()
    {
        androidTelephonyManager.startCall("");
    }
    
    @Test
    public void testStartCall()
    {
        PackageManager mockPackageManager = PowerMock.createMock(PackageManager.class);
        EasyMock.expect(mockContext.getPackageName()).andReturn("com.telenav.telephony.android").times(3);
        EasyMock.expect(mockContext.getPackageManager()).andReturn(mockPackageManager).times(3);
        EasyMock.expect(mockPackageManager.checkPermission(EasyMock.anyObject(String.class), EasyMock.anyObject(String.class))).andReturn(PackageManager.PERMISSION_DENIED).andReturn(PackageManager.PERMISSION_GRANTED).times(2);
        
        Intent mockIntent = PowerMock.createMock(Intent.class);
        Uri mockUri = PowerMock.createMock(Uri.class);
        try
        {
            PowerMock.expectNew(Intent.class, Intent.ACTION_CALL).andReturn(mockIntent).times(2);
        }
        catch (Exception e)
        {
            fail(e.getMessage());
        }
        EasyMock.expect(mockIntent.setData(mockUri)).andReturn(mockIntent);
        EasyMock.expect(mockIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)).andReturn(mockIntent);
        
        PowerMock.mockStatic(Uri.class);
        EasyMock.expect(Uri.parse(EasyMock.anyObject(String.class))).andReturn(mockUri).andThrow(new RuntimeException());
        mockContext.startActivity(mockIntent);
        PowerMock.mockStatic(Logger.class);
        Logger.log(EasyMock.anyObject(String.class), EasyMock.anyObject(RuntimeException.class));
        PowerMock.replayAll();

        androidTelephonyManager.startCall(NO_PERMISSION);
        androidTelephonyManager.startCall("1234567890");
        androidTelephonyManager.startCall("abcde");
        PowerMock.verifyAll();

    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testStartMMSRecipientNull()
    {
        androidTelephonyManager.startMMS(null, "");
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testStartMMSRecipientEmpty()
    {
        androidTelephonyManager.startMMS("", "");
    }
    
    @Test
    public void testStartMMS()
    {
        String testRecipient = "4083904318";
        String testSMS = "Come on";
        
        PackageManager mockPackageManager = PowerMock.createMock(PackageManager.class);
        EasyMock.expect(mockContext.getPackageName()).andReturn("android.permission.SEND_SMS").times(2);
        EasyMock.expect(mockContext.getPackageManager()).andReturn(mockPackageManager).times(2);
        EasyMock.expect(mockPackageManager.checkPermission(EasyMock.anyObject(String.class), EasyMock.anyObject(String.class))).andReturn(PackageManager.PERMISSION_DENIED).andReturn(PackageManager.PERMISSION_GRANTED);
        
        Intent mockIntent = PowerMock.createMock(Intent.class);
        try
        {
            PowerMock.expectNew(Intent.class, Intent.ACTION_VIEW).andReturn(mockIntent);
        }
        catch (Exception e)
        {
            fail(e.getMessage());
        }
        EasyMock.expect(mockIntent.putExtra("address", testRecipient)).andReturn(mockIntent);
        EasyMock.expect(mockIntent.putExtra("sms_body", testSMS)).andReturn(mockIntent);
        EasyMock.expect(mockIntent.setType(EasyMock.anyObject(String.class))).andReturn(mockIntent);

        mockContext.startActivity(mockIntent);
        PowerMock.replayAll();

        androidTelephonyManager.startMMS(NO_PERMISSION, NO_PERMISSION);
        androidTelephonyManager.startMMS(testRecipient, testSMS);
        PowerMock.verifyAll();
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testStartMMSWitchAttachDataRecipientNull()
    {
        androidTelephonyManager.startMMS(null, "", null, "");
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testStartMMSWitchAttachDataRecipientEmpty()
    {
        androidTelephonyManager.startMMS("", "", null, "");
    }
    
    @Test
    public void testStartMMSWitchAttachData()
    {
        String testRecipient = "4083904318";
        String testSMS = "Come on";
        
        byte[] testAttachData = new byte[]{11, 22, 33};
        
        PackageManager mockPackageManager = PowerMock.createMock(PackageManager.class);
        EasyMock.expect(mockContext.getPackageName()).andReturn("android.permission.SEND_SMS").times(6);
        EasyMock.expect(mockContext.getPackageManager()).andReturn(mockPackageManager).times(6);
        EasyMock.expect(mockPackageManager.checkPermission(EasyMock.anyObject(String.class), EasyMock.anyObject(String.class))).andReturn(PackageManager.PERMISSION_DENIED).andReturn(PackageManager.PERMISSION_GRANTED).times(5);
        
        Intent mockIntent = PowerMock.createMock(Intent.class);
        FileOutputStream mockFileOutputStream = PowerMock.createMock(FileOutputStream.class);
        File mockFile = PowerMock.createMock(File.class);
        Uri mockUri = PowerMock.createMock(Uri.class);
        
        try
        {
            PowerMock.expectNew(Intent.class, Intent.ACTION_SEND).andReturn(mockIntent).times(5);
        }
        catch (Exception e)
        {
            fail(e.getMessage());
        }
        EasyMock.expect(mockIntent.putExtra("address", testRecipient)).andReturn(mockIntent).times(5);
        EasyMock.expect(mockIntent.putExtra("sms_body", testSMS)).andReturn(mockIntent).times(5);
        EasyMock.expect(mockIntent.putExtra(Intent.EXTRA_STREAM, mockUri)).andReturn(mockIntent).times(4);
        EasyMock.expect(mockIntent.setType(EasyMock.anyObject(String.class))).andReturn(mockIntent).times(5);
        
        try
        {
            EasyMock.expect(mockContext.openFileOutput(EasyMock.anyObject(String.class), EasyMock.anyInt())).andReturn(mockFileOutputStream).times(3).andThrow(new FileNotFoundException());
            mockFileOutputStream.write(testAttachData);
            EasyMock.expectLastCall().times(2).andThrow(new IOException());
            mockFileOutputStream.close();
            EasyMock.expectLastCall().times(2);
            PowerMock.mockStatic(Uri.class);
            EasyMock.expect(this.mockContext.getFileStreamPath(EasyMock.anyObject(String.class))).andReturn(mockFile).times(4);
            EasyMock.expect(Uri.fromFile(mockFile)).andReturn(mockUri).times(4);
        }
        catch (Exception e)
        {
            fail(e.getMessage());
        }

        mockContext.startActivity(mockIntent);
        EasyMock.expectLastCall().times(5);
        
        PowerMock.mockStatic(Logger.class);
        Logger.log(EasyMock.anyObject(String.class), EasyMock.anyObject(FileNotFoundException.class));
        Logger.log(EasyMock.anyObject(String.class), EasyMock.anyObject(IOException.class));
        PowerMock.replayAll();

        androidTelephonyManager.startMMS(NO_PERMISSION, NO_PERMISSION, null, NO_PERMISSION);
        androidTelephonyManager.startMMS(testRecipient, testSMS, null, "image/png");
        androidTelephonyManager.startMMS(testRecipient, testSMS, testAttachData, "image/png");
        androidTelephonyManager.startMMS(testRecipient, testSMS, testAttachData, "audio/mp3");
        androidTelephonyManager.startMMS(testRecipient, testSMS, testAttachData, "image/png");
        androidTelephonyManager.startMMS(testRecipient, testSMS, testAttachData, "audio/mp3");
        PowerMock.verifyAll();
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testStartMMSAtBackgroundRecipientNull()
    {
        androidTelephonyManager.startMMSAtBackground(null, "");
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testStartMMSAtBackgroundRecipientEmpty()
    {
        androidTelephonyManager.startMMSAtBackground("", "");
    }
    
    @Test
    public void testStartMMSAtBackground()
    {
        String testRecipient = "4083904318";
        String testSMS = "111222333";
        String testSMS1 = "111";
        String testSMS2 = "222";
        String testSMS3 = "333";
        
        PackageManager mockPackageManager = PowerMock.createMock(PackageManager.class);
        EasyMock.expect(mockContext.getPackageName()).andReturn("android.permission.SEND_SMS").times(2);
        EasyMock.expect(mockContext.getPackageManager()).andReturn(mockPackageManager).times(2);
        EasyMock.expect(mockPackageManager.checkPermission(EasyMock.anyObject(String.class), EasyMock.anyObject(String.class))).andReturn(PackageManager.PERMISSION_DENIED).andReturn(PackageManager.PERMISSION_GRANTED);
        
        PowerMock.mockStatic(SmsManager.class);
        SmsManager mockSmsManager = PowerMock.createMock(SmsManager.class);
        ArrayList testDividedMessage = new ArrayList();
        testDividedMessage.add(testSMS1);
        testDividedMessage.add(testSMS2);
        testDividedMessage.add(testSMS3);
        EasyMock.expect(SmsManager.getDefault()).andReturn(mockSmsManager).times(4);
        EasyMock.expect(mockSmsManager.divideMessage(testSMS)).andReturn(testDividedMessage);
        mockSmsManager.sendTextMessage(testRecipient, null, testSMS1, null, null);
        mockSmsManager.sendTextMessage(testRecipient, null, testSMS2, null, null);
        mockSmsManager.sendTextMessage(testRecipient, null, testSMS3, null, null);
        PowerMock.replayAll();

        androidTelephonyManager.startMMSAtBackground(NO_PERMISSION, NO_PERMISSION);
        androidTelephonyManager.startMMSAtBackground(testRecipient, testSMS);
        PowerMock.verifyAll();
    }
    
    @Test
    public void testStartEmail()
    {
        Intent mockIntent = PowerMock.createMock(Intent.class);
        String testRecipient = "telenav@telenav.cn";
        String testSubject = "Telenav Subject";
        String testText = "Telenav Text";
        try
        {
            PowerMock.expectNew(Intent.class, Intent.ACTION_SEND).andReturn(mockIntent);
        }
        catch (Exception e)
        {
            fail(e.getMessage());
        }
        EasyMock.expect(mockIntent.putExtra(EasyMock.anyObject(String.class), EasyMock.anyObject(String[].class))).andReturn(mockIntent);
        EasyMock.expect(mockIntent.putExtra(Intent.EXTRA_SUBJECT, testSubject)).andReturn(mockIntent);
        EasyMock.expect(mockIntent.putExtra(Intent.EXTRA_TEXT, testText)).andReturn(mockIntent);
        EasyMock.expect(mockIntent.setType(EasyMock.anyObject(String.class))).andReturn(mockIntent);
        PowerMock.mockStatic(Intent.class);
        Intent mockIntent2 = PowerMock.createMock(Intent.class);
        EasyMock.expect(Intent.createChooser(mockIntent, "@")).andReturn(mockIntent2);
        mockContext.startActivity(mockIntent2);
        PowerMock.replayAll();
        androidTelephonyManager.startEmail(testRecipient, testSubject, testText);
        PowerMock.verifyAll();
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testStartEmailRecipientEmpty()
    {
        PowerMock.replayAll();
        androidTelephonyManager.startEmail("", "", "");
        PowerMock.verifyAll();
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testStartEmailRecipientNull()
    {
        PowerMock.replayAll();
        androidTelephonyManager.startEmail(null, "", "");
        PowerMock.verifyAll();
    }
    
    @Test
    public void testVibrate()
    {
        Vibrator mockVibrator = PowerMock.createMock(Vibrator.class);
        EasyMock.expect(mockContext.getSystemService("vibrator")).andReturn(mockVibrator);
        mockVibrator.vibrate(1000);
        PowerMock.replayAll();
        
        androidTelephonyManager.vibrate(-1);
        androidTelephonyManager.vibrate(0);
        androidTelephonyManager.vibrate(1000);
        PowerMock.verifyAll();
    }
}
