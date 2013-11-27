/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * TestResourceBundle.java
 *
 */
package com.telenav.i18n;

import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;

import org.easymock.EasyMock;
import org.powermock.api.easymock.PowerMock;

import com.telenav.io.TnIoManager;
import com.telenav.io.TnProperties;
import com.telenav.logger.Logger;
import com.telenav.persistent.TnPersistentManager;
import com.telenav.persistent.TnStore;

import junit.framework.TestCase;

/**
 *@author yning
 *@date 2011-6-13
 */
public class ResourceBundleTest extends TestCase
{
    public void testGetAudio()
    {
        byte[] result = new byte[100];
        String locale = Locale.en_US;
        TnIoManager mockIoManager = PowerMock.createMock(TnIoManager.class);
        TnPersistentManager mockPersistentManager = PowerMock.createMock(TnPersistentManager.class);
        TnStore mockStore = PowerMock.createMock(TnStore.class);
        PersistentResourceBundle mockPersistentResourceBundle = PowerMock.createMock(PersistentResourceBundle.class);
        
        EasyMock.expect(mockPersistentManager.createStore(TnPersistentManager.RMS_CRUMB, ResourceBundle.ROOT_PATH + "_" + "meta")).andReturn(mockStore);
        mockStore.load();
        EasyMock.expect(mockPersistentResourceBundle.getAudio(EasyMock.anyInt(), EasyMock.anyObject(String.class))).andReturn(result).times(1).andReturn(null).times(1);
        EasyMock.expect(mockIoManager.openFileBytesFromAppBundle(EasyMock.anyObject(String.class))).andReturn(result);
        
        PowerMock.replayAll();
        
        ResourceBundle bundle = new ResourceBundle(locale, mockIoManager, mockPersistentManager);
        bundle.persistentResourceBundle = mockPersistentResourceBundle;
        byte[] actual = bundle.getAudio(0, "");
        assertEquals(result, actual);
        
        actual = bundle.getAudio(0, "");
        assertEquals(result, actual);
        
        PowerMock.verifyAll();
    }
    
    public void testGetAudioFromFile()
    {
        byte[] result = new byte[100];
        //Used in construction.
        String locale = Locale.en_US;
        TnIoManager mockIoManager = PowerMock.createMock(TnIoManager.class);
        TnPersistentManager mockPersistentManager = PowerMock.createMock(TnPersistentManager.class);
        TnStore mockStore = PowerMock.createMock(TnStore.class);
        
        //record
        EasyMock.expect(mockPersistentManager.createStore(TnPersistentManager.RMS_CRUMB, ResourceBundle.ROOT_PATH + "_" + "meta")).andReturn(mockStore);
        mockStore.load();
        EasyMock.expect(mockIoManager.openFileBytesFromAppBundle(EasyMock.anyObject(String.class))).andReturn(result);
        
        //replay
        PowerMock.replayAll();
        
        ResourceBundle bundle = new ResourceBundle(locale, mockIoManager, mockPersistentManager);
        byte[] actual = bundle.getAudioFromFile(0, "");
        assertEquals(result, actual);
        
        PowerMock.verifyAll();
    }
    
    public void testGetBinary()
    {
        byte[] expected = new byte[100];
        //Used in construction.
        String locale = Locale.en_US;
        TnIoManager mockIoManager = PowerMock.createMock(TnIoManager.class);
        TnPersistentManager mockPersistentManager = PowerMock.createMock(TnPersistentManager.class);
        TnStore mockStore = PowerMock.createMock(TnStore.class);
        PersistentResourceBundle mockPersistentResourceBundle = PowerMock.createMock(PersistentResourceBundle.class);
        
        //record
        EasyMock.expect(mockPersistentManager.createStore(TnPersistentManager.RMS_CRUMB, ResourceBundle.ROOT_PATH + "_" + "meta")).andReturn(mockStore);
        mockStore.load();
        EasyMock.expect(mockPersistentResourceBundle.getBinary(EasyMock.anyObject(String.class), EasyMock.anyObject(String.class))).andReturn(expected).times(1).andReturn(null).times(1);
        EasyMock.expect(mockIoManager.openFileBytesFromAppBundle(EasyMock.anyObject(String.class))).andReturn(expected).times(1);
        
        //replay
        PowerMock.replayAll();
        
        ResourceBundle bundle = new ResourceBundle(locale, mockIoManager, mockPersistentManager);
        bundle.persistentResourceBundle = mockPersistentResourceBundle;
        byte[] actual = bundle.getBinary("", "");
        assertEquals(expected, actual);
        
        actual = bundle.getBinary("", "");
        assertEquals(expected, actual);
        
        //verify
        PowerMock.verifyAll();
    }
    
    public void testGetBinaryFromFile()
    {
        byte[] expected = new byte[100];
        //Used in construction.
        String locale = Locale.en_US;
        TnIoManager mockIoManager = PowerMock.createMock(TnIoManager.class);
        TnPersistentManager mockPersistentManager = PowerMock.createMock(TnPersistentManager.class);
        TnStore mockStore = PowerMock.createMock(TnStore.class);
        
        //record
        EasyMock.expect(mockPersistentManager.createStore(TnPersistentManager.RMS_CRUMB, ResourceBundle.ROOT_PATH + "_" + "meta")).andReturn(mockStore);
        mockStore.load();
        EasyMock.expect(mockIoManager.openFileBytesFromAppBundle(EasyMock.anyObject(String.class))).andReturn(expected).times(1);
        
        //replay
        PowerMock.replayAll();
        
        ResourceBundle bundle = new ResourceBundle(locale, mockIoManager, mockPersistentManager);
        byte[] actual = bundle.getBinaryFromFile("", "");
        assertEquals(expected, actual);
        
        //verify
        PowerMock.verifyAll();
    }

    public void testGetGenericBinary()
    {
        byte[] expected = new byte[100];
        //Used in construction.
        String locale = Locale.en_US;
        TnIoManager mockIoManager = PowerMock.createMock(TnIoManager.class);
        TnPersistentManager mockPersistentManager = PowerMock.createMock(TnPersistentManager.class);
        TnStore mockStore = PowerMock.createMock(TnStore.class);
        PersistentResourceBundle mockPersistentResourceBundle = PowerMock.createMock(PersistentResourceBundle.class);
        
        //record
        EasyMock.expect(mockPersistentManager.createStore(TnPersistentManager.RMS_CRUMB, ResourceBundle.ROOT_PATH + "_" + "meta")).andReturn(mockStore);
        mockStore.load();
        EasyMock.expect(mockPersistentResourceBundle.getGenericBinary(EasyMock.anyObject(String.class), EasyMock.anyObject(String.class))).andReturn(expected).times(1).andReturn(null).times(1);
        EasyMock.expect(mockIoManager.openFileBytesFromAppBundle(EasyMock.anyObject(String.class))).andReturn(expected).times(1);
        
        //replay
        PowerMock.replayAll();
        
        ResourceBundle bundle = new ResourceBundle(locale, mockIoManager, mockPersistentManager);
        bundle.persistentResourceBundle = mockPersistentResourceBundle;
        
        byte[] actual = bundle.getGenericBinary("", "");
        assertEquals(expected, actual);
        actual = bundle.getGenericBinary("", "");
        assertEquals(expected, actual);
        
        //verify
        PowerMock.verifyAll();
    }
    
    public void testGetGenericBinaryFromFile()
    {
        byte[] expected = new byte[100];
        //Used in construction.
        String locale = Locale.en_US;
        TnIoManager mockIoManager = PowerMock.createMock(TnIoManager.class);
        TnPersistentManager mockPersistentManager = PowerMock.createMock(TnPersistentManager.class);
        TnStore mockStore = PowerMock.createMock(TnStore.class);
        
        //record
        EasyMock.expect(mockPersistentManager.createStore(TnPersistentManager.RMS_CRUMB, ResourceBundle.ROOT_PATH + "_" + "meta")).andReturn(mockStore);
        mockStore.load();
        EasyMock.expect(mockIoManager.openFileBytesFromAppBundle(EasyMock.anyObject(String.class))).andReturn(expected).times(1);
        
        //replay
        PowerMock.replayAll();
        
        ResourceBundle bundle = new ResourceBundle(locale, mockIoManager, mockPersistentManager);
        
        byte[] actual = bundle.getGenericBinaryFromFile("", "");
        assertEquals(expected, actual);
        
        //verify
        PowerMock.verifyAll();
    }
    
    public void testGetGenericImage()
    {
        byte[] expected1 = new byte[100];
        byte[] expected2 = new byte[100];
        //Used in construction.
        String locale = Locale.en_US;
        TnIoManager mockIoManager = PowerMock.createMock(TnIoManager.class);
        TnPersistentManager mockPersistentManager = PowerMock.createMock(TnPersistentManager.class);
        TnStore mockStore = PowerMock.createMock(TnStore.class);
        PersistentResourceBundle mockPersistentResourceBundle = PowerMock.createMock(PersistentResourceBundle.class);
        
        //record
        EasyMock.expect(mockPersistentManager.createStore(TnPersistentManager.RMS_CRUMB, ResourceBundle.ROOT_PATH + "_" + "meta")).andReturn(mockStore);
        mockStore.load();
        EasyMock.expect(mockPersistentResourceBundle.getGenericImage(EasyMock.anyObject(String.class), EasyMock.anyObject(String.class))).andReturn(expected1).times(1).andReturn(null).times(1);
        EasyMock.expect(mockIoManager.openFileBytesFromAppBundle(EasyMock.anyObject(String.class))).andReturn(expected2).times(1);
        
        //replay
        PowerMock.replayAll();
        
        ResourceBundle bundle = new ResourceBundle(locale, mockIoManager, mockPersistentManager);
        bundle.persistentResourceBundle = mockPersistentResourceBundle;
        
        byte[] actual = bundle.getGenericImage("0", "");
        assertEquals(expected1, actual);
        actual = bundle.getGenericImage("0", "");
        assertEquals(expected2, actual);
        
        //verify
        PowerMock.verifyAll();
    }
    
    public void testGetGenericImageFromFile()
    {
        byte[] expected1 = new byte[100];
        //Used in construction.
        String locale = Locale.en_US;
        TnIoManager mockIoManager = PowerMock.createMock(TnIoManager.class);
        TnPersistentManager mockPersistentManager = PowerMock.createMock(TnPersistentManager.class);
        TnStore mockStore = PowerMock.createMock(TnStore.class);
        
        //record
        EasyMock.expect(mockPersistentManager.createStore(TnPersistentManager.RMS_CRUMB, ResourceBundle.ROOT_PATH + "_" + "meta")).andReturn(mockStore);
        mockStore.load();
        EasyMock.expect(mockIoManager.openFileBytesFromAppBundle(EasyMock.anyObject(String.class))).andReturn(expected1).times(1);
        
        //replay
        PowerMock.replayAll();
        
        ResourceBundle bundle = new ResourceBundle(locale, mockIoManager, mockPersistentManager);
        
        byte[] actual = bundle.getGenericImageFromFile("0", "");
        assertEquals(expected1, actual);
        
        //verify
        PowerMock.verifyAll();
    }
    
    public void testGetImage()
    {
        byte[] expected1 = new byte[100];
        byte[] expected2 = new byte[100];
        //Used in construction.
        String locale = Locale.en_US;
        TnIoManager mockIoManager = PowerMock.createMock(TnIoManager.class);
        TnPersistentManager mockPersistentManager = PowerMock.createMock(TnPersistentManager.class);
        TnStore mockStore = PowerMock.createMock(TnStore.class);
        PersistentResourceBundle mockPersistentResourceBundle = PowerMock.createMock(PersistentResourceBundle.class);
        
        //record
        EasyMock.expect(mockPersistentManager.createStore(TnPersistentManager.RMS_CRUMB, ResourceBundle.ROOT_PATH + "_" + "meta")).andReturn(mockStore);
        mockStore.load();
        EasyMock.expect(mockPersistentResourceBundle.getImage(EasyMock.anyObject(String.class), EasyMock.anyObject(String.class))).andReturn(expected1).times(1).andReturn(null).times(1);
        EasyMock.expect(mockIoManager.openFileBytesFromAppBundle(EasyMock.anyObject(String.class))).andReturn(expected2).times(1);
        
        //replay
        PowerMock.replayAll();
        
        ResourceBundle bundle = new ResourceBundle(locale, mockIoManager, mockPersistentManager);
        bundle.persistentResourceBundle = mockPersistentResourceBundle;
        
        byte[] actual = bundle.getImage("0", "");
        assertEquals(expected1, actual);
        actual = bundle.getImage("0", "");
        assertEquals(expected2, actual);
        
        //verify
        PowerMock.verifyAll();
    }
    
    public void testGetImageFromFile()
    {
        byte[] expected1 = new byte[100];
        //Used in construction.
        String locale = Locale.en_US;
        TnIoManager mockIoManager = PowerMock.createMock(TnIoManager.class);
        TnPersistentManager mockPersistentManager = PowerMock.createMock(TnPersistentManager.class);
        TnStore mockStore = PowerMock.createMock(TnStore.class);
        
        //record
        EasyMock.expect(mockPersistentManager.createStore(TnPersistentManager.RMS_CRUMB, ResourceBundle.ROOT_PATH + "_" + "meta")).andReturn(mockStore);
        mockStore.load();
        EasyMock.expect(mockIoManager.openFileBytesFromAppBundle(EasyMock.anyObject(String.class))).andReturn(expected1).times(1);
        
        //replay
        PowerMock.replayAll();
        
        ResourceBundle bundle = new ResourceBundle(locale, mockIoManager, mockPersistentManager);
        
        byte[] actual = bundle.getImageFromFile("0", "");
        assertEquals(expected1, actual);
        
        //verify
        PowerMock.verifyAll();
    }
    
    public void testGetLocale()
    {
        //Used in construction.
        String locale = Locale.en_US;
        TnIoManager mockIoManager = PowerMock.createMock(TnIoManager.class);
        TnPersistentManager mockPersistentManager = PowerMock.createMock(TnPersistentManager.class);
        TnStore mockStore = PowerMock.createMock(TnStore.class);
        
        //record
        EasyMock.expect(mockPersistentManager.createStore(TnPersistentManager.RMS_CRUMB, ResourceBundle.ROOT_PATH + "_" + "meta")).andReturn(mockStore).times(4);
        mockStore.load();
        mockStore.load();
        mockStore.load();
        mockStore.load();
        
        //replay
        PowerMock.replayAll();
        
        ResourceBundle bundle = new ResourceBundle(locale, mockIoManager, mockPersistentManager);
        assertEquals(Locale.en_US, bundle.getLocale());
        locale = Locale.en_GB;
        bundle = new ResourceBundle(locale, mockIoManager, mockPersistentManager);
        assertEquals(Locale.en_GB, bundle.getLocale());
        locale = Locale.es_MX;
        bundle = new ResourceBundle(locale, mockIoManager, mockPersistentManager);
        assertEquals(Locale.es_MX, bundle.getLocale());
        locale = Locale.fr_CA;
        bundle = new ResourceBundle(locale, mockIoManager, mockPersistentManager);
        assertEquals(Locale.fr_CA, bundle.getLocale());
        
        //verify
        PowerMock.verifyAll();
    }
    
    public void testGetString()
    {
        String expected1 = "It's interesting";
        //Used in construction.
        String locale = Locale.en_US;
        TnIoManager mockIoManager = PowerMock.createMock(TnIoManager.class);
        TnPersistentManager mockPersistentManager = PowerMock.createMock(TnPersistentManager.class);
        TnStore mockStore = PowerMock.createMock(TnStore.class);
        PersistentResourceBundle mockPersistentResourceBundle = PowerMock.createMock(PersistentResourceBundle.class);
        
        //record
        EasyMock.expect(mockPersistentManager.createStore(TnPersistentManager.RMS_CRUMB, ResourceBundle.ROOT_PATH + "_" + "meta")).andReturn(mockStore);
        mockStore.load();
        EasyMock.expect(mockPersistentResourceBundle.getString(EasyMock.anyInt(), EasyMock.anyObject(String.class))).andReturn(expected1).times(1);
        
        //replay
        PowerMock.replayAll();
        
        ResourceBundle bundle = new ResourceBundle(locale, mockIoManager, mockPersistentManager);
        bundle.persistentResourceBundle = mockPersistentResourceBundle;
        try
        {
            bundle.getString(0, "");
        }
        catch(Exception e)
        {
            assertTrue(e instanceof IllegalArgumentException);
        }
        
        String actual = bundle.getString(0, "aa");
        assertEquals(expected1, actual);
        //verify
        PowerMock.verifyAll();
    
    }
    
    public void testGetPersistentBundle()
    {
//        byte[] result = new byte[100];
        String locale = Locale.en_US;
        TnIoManager mockIoManager = PowerMock.createMock(TnIoManager.class);
        TnPersistentManager mockPersistentManager = PowerMock.createMock(TnPersistentManager.class);
        TnStore mockStore = PowerMock.createMock(TnStore.class);
        PersistentResourceBundle mockPersistentResourceBundle = PowerMock.createMock(PersistentResourceBundle.class);
        
        EasyMock.expect(mockPersistentManager.createStore(TnPersistentManager.RMS_CRUMB, ResourceBundle.ROOT_PATH + "_" + "meta")).andReturn(mockStore);
        mockStore.load();
//        EasyMock.expect(mockPersistentResourceBundle.getAudio(EasyMock.anyInt(), EasyMock.anyObject(String.class))).andReturn(result).times(1).andReturn(null).times(1);
//        EasyMock.expect(mockIoManager.openFileBytesFromAppBundle(EasyMock.anyObject(String.class))).andReturn(result);
        
        PowerMock.replayAll();
        
        ResourceBundle bundle = new ResourceBundle(locale, mockIoManager, mockPersistentManager);
        bundle.persistentResourceBundle = mockPersistentResourceBundle;
        assertSame(mockPersistentResourceBundle, bundle.getPersistentBundle());
        
        PowerMock.verifyAll();
    }
    
    public void testGetStringFromFile()
    {
        String expected1 = "It's interesting";
        String expected2 = "It's very interesting";
        String familyName = "familyName";
        int key = 0;
        //Used in construction.
        String locale = Locale.en_US;
        TnIoManager mockIoManager = PowerMock.createMock(TnIoManager.class);
        TnPersistentManager mockPersistentManager = PowerMock.createMock(TnPersistentManager.class);
        TnStore mockStore = PowerMock.createMock(TnStore.class);
        Hashtable mockPropertiesTable = PowerMock.createMock(Hashtable.class);
        TnProperties mockProperties = PowerMock.createMock(TnProperties.class);
        InputStream mockIs = PowerMock.createMock(InputStream.class);
        
        //record
        EasyMock.expect(mockPersistentManager.createStore(TnPersistentManager.RMS_CRUMB, ResourceBundle.ROOT_PATH + "_" + "meta")).andReturn(mockStore);
        mockStore.load();
        
        //case1
        EasyMock.expect(mockPropertiesTable.get(familyName)).andReturn(mockProperties);
        EasyMock.expect(mockProperties.getProperty(key + "")).andReturn(expected1);
        
        //case2
        EasyMock.expect(mockPropertiesTable.get(familyName)).andReturn(null);
        EasyMock.expect(mockIoManager.createProperties()).andReturn(mockProperties);
        try
        {
            EasyMock.expect(mockIoManager.openFileFromAppBundle(EasyMock.anyObject(String.class))).andReturn(mockIs);
            mockProperties.load(mockIs);
            EasyMock.expect(mockPropertiesTable.put(familyName, mockProperties)).andReturn(mockProperties);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if (mockIs != null)
                {
                    mockIs.close();
                }
            }
            catch (IOException e)
            {
            }
        }
        
        EasyMock.expect(mockProperties.getProperty(key + "")).andReturn(expected2);
        
        //replay
        PowerMock.replayAll();
        
        ResourceBundle bundle = new ResourceBundle(locale, mockIoManager, mockPersistentManager);
        bundle.propertiesTable = mockPropertiesTable;
        String actual = bundle.getStringFromFile(key, familyName);
        assertEquals(expected1, actual);
        
        actual = bundle.getStringFromFile(key, familyName);
        assertEquals(expected2, actual);
        
        //verify
        PowerMock.verifyAll();
    }
    
    public void testSetLocale()
    {
        //Used in construction.
        String locale = Locale.en_US;
        TnIoManager mockIoManager = PowerMock.createMock(TnIoManager.class);
        TnPersistentManager mockPersistentManager = PowerMock.createMock(TnPersistentManager.class);
        TnStore mockStore = PowerMock.createMock(TnStore.class);
        Hashtable mockPropertiesTable = PowerMock.createMock(Hashtable.class);
        PersistentResourceBundle mockPersistentResourceBundle = PowerMock.createMock(PersistentResourceBundle.class);
        
        //record
        EasyMock.expect(mockPersistentManager.createStore(TnPersistentManager.RMS_CRUMB, ResourceBundle.ROOT_PATH + "_" + "meta")).andReturn(mockStore);
        mockStore.load();
        mockPropertiesTable.clear();
        mockPersistentResourceBundle.clearCurrentLocaleData();
        
        //replay
        PowerMock.replayAll();
        
        ResourceBundle bundle = new ResourceBundle(locale, mockIoManager, mockPersistentManager);
        
        bundle.propertiesTable = mockPropertiesTable;
        bundle.persistentResourceBundle = mockPersistentResourceBundle;
        
        try
        {
            bundle.setLocale("");
        }
        catch(Exception e)
        {
            assertTrue(e instanceof IllegalArgumentException);
        }
        
        bundle.setLocale(Locale.es_MX);
        
        //verify
        PowerMock.verifyAll();
    }
}
