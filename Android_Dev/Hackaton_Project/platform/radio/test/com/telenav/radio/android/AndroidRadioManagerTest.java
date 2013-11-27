/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * AndroidRadioManagerTest.java
 *
 */
package com.telenav.radio.android;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Hashtable;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.telenav.radio.ITnCoverageListener;
import com.telenav.radio.TnCellInfo;
import com.telenav.radio.TnWifiInfo;

/**
 *@author Casper (pwang@telenav.cn)
 *@date 2011-7-6
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({Build.VERSION.class,Settings.System.class, PhoneStateListener.class, AndroidRadioStatusListener.class, AndroidRadioManager.class, Activity.class})
public class AndroidRadioManagerTest
{
    Context mockContext;
    TelephonyManager mockTelephonyManager;
    AndroidRadioManager androidRadioManager;
    
    @Before
    public void setUp()
    {
        mockContext = PowerMock.createMock(Context.class);
        mockTelephonyManager = PowerMock.createMock(TelephonyManager.class);
        androidRadioManager = new AndroidRadioManager(mockContext);
    }
    
    @Test
    public void testConstructor()
    {
        PowerMock.replayAll();
        assertEquals(Whitebox.getInternalState(androidRadioManager, "context"), mockContext);
        PowerMock.verifyAll();
    }
    
    
    @Test
    public void testAddListener()
    {
        PowerMock.suppressConstructor(PhoneStateListener.class);
        PowerMock.suppressConstructor(AndroidRadioStatusListener.class);
        
        EasyMock.expect(mockContext.getSystemService(Context.TELEPHONY_SERVICE)).andReturn(mockTelephonyManager).times(2);
        Activity mockActivity = PowerMock.createMock(Activity.class);
        mockActivity.runOnUiThread(EasyMock.anyObject(Runnable.class));
        
        ITnCoverageListener mockCoverageListener = PowerMock.createMock(ITnCoverageListener.class);
        AndroidRadioStatusListener mockRadioStatusListener = Whitebox.newInstance(AndroidRadioStatusListener.class);
        Whitebox.setInternalState(mockRadioStatusListener, "coverageListener", mockCoverageListener);
        
        ITnCoverageListener mockCoverageListener2 = PowerMock.createMock(ITnCoverageListener.class);
        AndroidRadioStatusListener mockRadioStatusListener2 = Whitebox.newInstance(AndroidRadioStatusListener.class);
        Whitebox.setInternalState(mockRadioStatusListener2, "coverageListener", mockCoverageListener2);
        
        try
        {
            PowerMock.expectNew(AndroidRadioStatusListener.class, mockCoverageListener).andReturn(mockRadioStatusListener);
            PowerMock.expectNew(AndroidRadioStatusListener.class, mockCoverageListener2).andReturn(mockRadioStatusListener2);
        }
        catch (Exception e)
        {
            fail(e.toString());
        }
        
        mockTelephonyManager.listen(mockRadioStatusListener, PhoneStateListener.LISTEN_SERVICE_STATE | PhoneStateListener.LISTEN_SIGNAL_STRENGTH);
        mockTelephonyManager.listen(mockRadioStatusListener2, PhoneStateListener.LISTEN_SERVICE_STATE | PhoneStateListener.LISTEN_SIGNAL_STRENGTH);
        PowerMock.replayAll();
        
        androidRadioManager.addListener(null);
        assertNull(androidRadioManager.listeners);
        androidRadioManager.addListener(mockCoverageListener);
        assertNotNull(androidRadioManager.listeners);
        assertEquals(androidRadioManager.listeners.get(mockCoverageListener), mockRadioStatusListener);
        androidRadioManager.addListener(mockCoverageListener2);
        assertEquals(androidRadioManager.listeners.get(mockCoverageListener2), mockRadioStatusListener2);
        
        androidRadioManager = new AndroidRadioManager(mockActivity);
        androidRadioManager.addListener(mockCoverageListener);
        PowerMock.verifyAll();
    }
    
    @Test
    public void testRemoveListener()
    {
        PowerMock.suppressConstructor(PhoneStateListener.class);
        PowerMock.suppressConstructor(AndroidRadioStatusListener.class);
        
        EasyMock.expect(mockContext.getSystemService(Context.TELEPHONY_SERVICE)).andReturn(mockTelephonyManager);
        Activity mockActivity = PowerMock.createMock(Activity.class);
        mockActivity.runOnUiThread(EasyMock.anyObject(Runnable.class));
        
        ITnCoverageListener mockCoverageListener = PowerMock.createMock(ITnCoverageListener.class);
        AndroidRadioStatusListener mockRadioStatusListener = Whitebox.newInstance(AndroidRadioStatusListener.class);
        Whitebox.setInternalState(mockRadioStatusListener, "coverageListener", mockCoverageListener);
        
        ITnCoverageListener mockCoverageListener2 = PowerMock.createMock(ITnCoverageListener.class);
        AndroidRadioStatusListener mockRadioStatusListener2 = Whitebox.newInstance(AndroidRadioStatusListener.class);
        Whitebox.setInternalState(mockRadioStatusListener2, "coverageListener", mockCoverageListener2);
        
        mockTelephonyManager.listen(mockRadioStatusListener, PhoneStateListener.LISTEN_NONE);
        PowerMock.replayAll();
        
        androidRadioManager.removeListener(null);
        androidRadioManager.listeners = null;
        androidRadioManager.removeListener(mockCoverageListener);
        assertNull(androidRadioManager.listeners);
        
        androidRadioManager.listeners = new Hashtable();
        androidRadioManager.listeners.put(mockCoverageListener, mockRadioStatusListener);
        androidRadioManager.removeListener(mockCoverageListener2);
        assertEquals(androidRadioManager.listeners.get(mockCoverageListener), mockRadioStatusListener);
        androidRadioManager.removeListener(mockCoverageListener);
        assertNull(androidRadioManager.listeners.get(mockCoverageListener));
        
        androidRadioManager = new AndroidRadioManager(mockActivity);
        androidRadioManager.listeners = new Hashtable();
        androidRadioManager.listeners.put(mockCoverageListener, mockRadioStatusListener);
        androidRadioManager.removeListener(mockCoverageListener);
        PowerMock.verifyAll();
    }
    
    
    @Test
    public void testGetCellInfo()
    {
        android.telephony.gsm.GsmCellLocation mockGsmCellLocation = PowerMock.createMock(android.telephony.gsm.GsmCellLocation.class);
        android.telephony.cdma.CdmaCellLocation mockCdmaCellLocation = PowerMock.createMock(android.telephony.cdma.CdmaCellLocation.class);
        
        EasyMock.expect(mockContext.getSystemService(Context.TELEPHONY_SERVICE)).andReturn(mockTelephonyManager);
        EasyMock.expect(mockTelephonyManager.getCellLocation()).andReturn(null);
        
        EasyMock.expect(mockContext.getSystemService(Context.TELEPHONY_SERVICE)).andReturn(mockTelephonyManager);
        EasyMock.expect(mockTelephonyManager.getCellLocation()).andReturn(mockGsmCellLocation);
        EasyMock.expect(mockTelephonyManager.getNetworkOperator()).andReturn("123456").times(2);
        EasyMock.expect(mockTelephonyManager.getNetworkType()).andReturn(123);
        EasyMock.expect(mockTelephonyManager.getNetworkOperatorName()).andReturn("9876");
        EasyMock.expect(mockGsmCellLocation.getCid()).andReturn(1234511111).times(2);
        EasyMock.expect(mockGsmCellLocation.getLac()).andReturn(12345);
        
        EasyMock.expect(mockContext.getSystemService(Context.TELEPHONY_SERVICE)).andReturn(mockTelephonyManager);
        EasyMock.expect(mockTelephonyManager.getCellLocation()).andReturn(mockCdmaCellLocation);
        EasyMock.expect(mockTelephonyManager.getNetworkOperator()).andReturn("123456").times(2);
        EasyMock.expect(mockTelephonyManager.getNetworkType()).andReturn(123);
        EasyMock.expect(mockTelephonyManager.getNetworkOperatorName()).andReturn("9876");
        EasyMock.expect(mockCdmaCellLocation.getNetworkId()).andReturn(11);
        EasyMock.expect(mockCdmaCellLocation.getBaseStationId()).andReturn(22);
        EasyMock.expect(mockCdmaCellLocation.getSystemId()).andReturn(33);
        
        PowerMock.replayAll();
        
        assertEquals(androidRadioManager.getCellInfo(), null);
        
        Field field = PowerMock.field(Build.VERSION.class, "SDK");
        try
        {
            field.set(Whitebox.newInstance(Build.VERSION.class), "6");
        }
        catch (Exception e)
        {
            fail(e.toString());
        } 
        
        TnCellInfo gsmCellInfo = androidRadioManager.getCellInfo();
        assertEquals(gsmCellInfo.networkCode, "456");
        assertEquals(gsmCellInfo.countryCode, "123");
        assertEquals(gsmCellInfo.networkType, "123");
        assertEquals(gsmCellInfo.networkOperatorName, "9876");
        assertEquals(gsmCellInfo.baseStationId, "18837");
        assertEquals(gsmCellInfo.cellId, "9479");
        assertEquals(gsmCellInfo.locationAreaCode, "12345");
        assertEquals(gsmCellInfo.networkRadioMode, TnCellInfo.MODE_GSM);
        
        TnCellInfo cdmaCellInfo = androidRadioManager.getCellInfo();
        assertEquals(cdmaCellInfo.networkCode, "456");
        assertEquals(cdmaCellInfo.countryCode, "123");
        assertEquals(cdmaCellInfo.networkType, "123");
        assertEquals(cdmaCellInfo.networkOperatorName, "9876");
        assertEquals(cdmaCellInfo.baseStationId, "11");
        assertEquals(cdmaCellInfo.cellId, "22");
        assertEquals(cdmaCellInfo.locationAreaCode, "33");
        assertEquals(cdmaCellInfo.networkRadioMode, TnCellInfo.MODE_CDMA);
        PowerMock.verifyAll();

    }
    
    @Test
    public void testRetrieveOtherCellLocation()
    {
        android.telephony.cdma.CdmaCellLocation mockCdmaCellLocation = PowerMock.createMock(android.telephony.cdma.CdmaCellLocation.class);
        EasyMock.expect(mockCdmaCellLocation.getNetworkId()).andReturn(11);
        EasyMock.expect(mockCdmaCellLocation.getBaseStationId()).andReturn(22);
        EasyMock.expect(mockCdmaCellLocation.getSystemId()).andReturn(33);
        
        PowerMock.replayAll();
        
        TnCellInfo cellInfo = new TnCellInfo();
        
        Field field = PowerMock.field(Build.VERSION.class, "SDK");
        try
        {
            field.set(Whitebox.newInstance(Build.VERSION.class), "5");
        }
        catch (Exception e)
        {
            fail(e.toString());
        }
        androidRadioManager.retrieveOtherCellLocation(mockCdmaCellLocation, cellInfo);
        assertEquals("11", cellInfo.baseStationId);
        assertEquals("22", cellInfo.cellId);
        assertEquals("33", cellInfo.locationAreaCode);
        assertEquals(TnCellInfo.MODE_CDMA, cellInfo.networkRadioMode);
        
        try
        {
            field.set(Whitebox.newInstance(Build.VERSION.class), "4");
        }
        catch (Exception e)
        {
            fail(e.toString());
        }
        androidRadioManager.retrieveOtherCellLocation(mockCdmaCellLocation, cellInfo);
        PowerMock.verifyAll();
    }
    
    @Test
    public void testGetWifiInfo()
    {
        WifiManager mockWifiManager = PowerMock.createMock(WifiManager.class);;
        WifiInfo mockWifiInfo = PowerMock.createMock(WifiInfo.class);;
        EasyMock.expect(mockContext.getSystemService(Context.WIFI_SERVICE)).andReturn(mockWifiManager).times(2);
        EasyMock.expect(mockWifiManager.isWifiEnabled()).andReturn(false).andReturn(true);
        EasyMock.expect(mockWifiManager.getConnectionInfo()).andReturn(mockWifiInfo);
        EasyMock.expect(mockWifiInfo.getBSSID()).andReturn("111");
        EasyMock.expect(mockWifiInfo.getRssi()).andReturn(222);
        EasyMock.expect(mockWifiInfo.getLinkSpeed()).andReturn(333);
        EasyMock.expect(mockWifiInfo.getSSID()).andReturn("444");
        PowerMock.replayAll();

        assertEquals(androidRadioManager.getWifiInfo(), null);
        TnWifiInfo tnWifiInfo = androidRadioManager.getWifiInfo();
        assertEquals(tnWifiInfo.bssid, "111");
        assertEquals(tnWifiInfo.signalLevel, 222);
        assertEquals(tnWifiInfo.speed, 333);
        assertEquals(tnWifiInfo.ssid, "444");
        PowerMock.verifyAll();
    }
    
    @Test
    public void testIsWifiConnected()
    {
        WifiManager mockWifiManager = PowerMock.createMock(WifiManager.class);
        WifiInfo mockWifiInfo = PowerMock.createMock(WifiInfo.class);
        EasyMock.expect(mockContext.getSystemService(Context.WIFI_SERVICE)).andReturn(mockWifiManager).times(3);
        EasyMock.expect(mockWifiManager.isWifiEnabled()).andReturn(false).andReturn(true).times(2);
        EasyMock.expect(mockWifiManager.getConnectionInfo()).andReturn(mockWifiInfo).times(2);
        EasyMock.expect(mockWifiInfo.getSupplicantState()).andReturn(SupplicantState.INVALID).times(2).andReturn(SupplicantState.COMPLETED).times(2);
        PowerMock.replayAll();

        assertEquals(androidRadioManager.isWifiConnected(), false);
        assertEquals(androidRadioManager.isWifiConnected(), false);
        assertEquals(androidRadioManager.isWifiConnected(), true);
        PowerMock.verifyAll();
    }
    
    @Test
    public void testIsNetworkConnected()
    {
        PowerMock.mockStatic(Settings.System.class);
        ConnectivityManager mockConnectivityManager = PowerMock.createMock(ConnectivityManager.class);
        NetworkInfo mockNetworkInfo = PowerMock.createMock(NetworkInfo.class);

        EasyMock.expect(mockContext.getSystemService(Context.TELEPHONY_SERVICE)).andReturn(mockTelephonyManager).times(10);
        EasyMock.expect(mockContext.getSystemService(Context.CONNECTIVITY_SERVICE)).andReturn(mockConnectivityManager).times(10);
        EasyMock.expect(mockConnectivityManager.getActiveNetworkInfo()).andReturn(null).times(2).andReturn(mockNetworkInfo).times(8);
        EasyMock.expect(mockTelephonyManager.getDataState()).andReturn(TelephonyManager.DATA_CONNECTED).andReturn(TelephonyManager.DATA_DISCONNECTED);
        
        EasyMock.expect(mockNetworkInfo.isAvailable()).andReturn(false).times(2).andReturn(true).times(1);
        EasyMock.expect(mockNetworkInfo.isConnected()).andReturn(false).times(1).andReturn(true).times(1);

        EasyMock.expect(mockNetworkInfo.isAvailable()).andReturn(false).times(4).andReturn(true).times(4).andReturn(false).times(2).andReturn(true).times(2);
        EasyMock.expect(mockNetworkInfo.isConnected()).andReturn(true).times(2).andReturn(false).times(2).andReturn(true).times(1).andReturn(false).times(1);
        EasyMock.expect(mockNetworkInfo.getType()).andReturn(ConnectivityManager.TYPE_WIFI).times(2).andReturn(ConnectivityManager.TYPE_MOBILE).times(4);
        EasyMock.expect(mockTelephonyManager.getDataState()).andReturn(TelephonyManager.DATA_DISCONNECTED).times(2).andReturn(TelephonyManager.DATA_CONNECTED).times(2);

        PowerMock.replayAll();
        
        assertEquals(androidRadioManager.isNetworkConnected(), true);
        assertEquals(androidRadioManager.isNetworkConnected(), false);
        assertEquals(androidRadioManager.isNetworkConnected(), false);

        assertEquals(androidRadioManager.isNetworkConnected(), true);
        assertEquals(androidRadioManager.isNetworkConnected(), false);

        //Fall into the last if cluase.
        assertEquals(androidRadioManager.isNetworkConnected(), false);
        assertEquals(androidRadioManager.isNetworkConnected(), false);
        assertEquals(androidRadioManager.isNetworkConnected(), false);
        assertEquals(androidRadioManager.isNetworkConnected(), true);
        assertEquals(androidRadioManager.isNetworkConnected(), true);

        PowerMock.verifyAll();
    }
    
    @Test
    public void testIsisAirportMode()
    {
        PowerMock.mockStatic(Settings.System.class);
        ContentResolver mockContentResolver = PowerMock.createMock(ContentResolver.class);
        EasyMock.expect(mockContext.getContentResolver()).andReturn(mockContentResolver).times(2);
        EasyMock.expect(Settings.System.getInt(mockContentResolver, Settings.System.AIRPLANE_MODE_ON, 0)).andReturn(0).andReturn(1);
        
        PowerMock.replayAll();
        
        assertEquals(androidRadioManager.isAirportMode(), false);
        assertEquals(androidRadioManager.isAirportMode(), true);
        PowerMock.verifyAll();
    }
    
    @Test
    public void testIsRoaming()
    {
        EasyMock.expect(mockContext.getSystemService(Context.TELEPHONY_SERVICE)).andReturn(mockTelephonyManager).times(2);
        EasyMock.expect(mockTelephonyManager.isNetworkRoaming()).andReturn(false).andReturn(true);
        PowerMock.replayAll();
        
        assertEquals(androidRadioManager.isRoaming(), false);
        assertEquals(androidRadioManager.isRoaming(), true);
        PowerMock.verifyAll();

    }
    
    @Test
    public void testGetConnectionWifiInfo()
    {
        WifiManager mockWifiManager = PowerMock.createMock(WifiManager.class);;
        WifiInfo mockWifiInfo = PowerMock.createMock(WifiInfo.class);;
        EasyMock.expect(mockContext.getSystemService(Context.WIFI_SERVICE)).andReturn(mockWifiManager).times(2);
        EasyMock.expect(mockWifiManager.isWifiEnabled()).andReturn(false).andReturn(true);
        EasyMock.expect(mockWifiManager.getConnectionInfo()).andReturn(mockWifiInfo);
        PowerMock.replayAll();
        
        assertEquals(androidRadioManager.getConnectionWifiInfo(), null);
        assertEquals(androidRadioManager.getConnectionWifiInfo(), mockWifiInfo);
        PowerMock.verifyAll();
    }
    
    @Test
    public void testGetMCC()
    {
        assertEquals(androidRadioManager.getMCC(null), -1);
        assertEquals(androidRadioManager.getMCC("123"), -1);
        assertEquals(androidRadioManager.getMCC("123456"), 123);
        androidRadioManager.getMCC("abcdefg");
    }
    
    @Test
    public void testGetMNC()
    {
        assertEquals(androidRadioManager.getMNC(null), -1);
        assertEquals(androidRadioManager.getMNC("1234"), -1);
        assertEquals(androidRadioManager.getMNC("1234567"), 4567);
        androidRadioManager.getMNC("hijklmn");
    }
    
    @Test
    public void testGetCID()
    {
        assertEquals(androidRadioManager.getCID(12345678), 24910);
    }
    
    @Test
    public void testGetBSID()
    {
        assertEquals(androidRadioManager.getBSID(12345678), 188);
    }
}
