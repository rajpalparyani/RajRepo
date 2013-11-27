/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * PersistentResourceBundle.java
 *
 */
package com.telenav.i18n;

/**
 *@author jyxu
 *@date 2011-7-10
 */

import java.util.Hashtable;
import java.util.Vector;

import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.telenav.persistent.TnPersistentManager;
import com.telenav.persistent.TnStore;
import com.telenav.persistent.TnStoreMock;

/**
 * Resource bundles contain locale-specific objects, for this class, will store the resource into persistent system.
 * 
 *@author fqming (fqming@telenav.cn)
 *@date Jul 15, 2010
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(PersistentResourceBundle.class)
public class PersistentResourceBundleTest extends TestCase
{

    TnPersistentManager persistentManager;
    ResourceBundle resourceBundle;
    TnStore mockStore; 
    int key;
    String familyName;
    /**
     * construct a persistent resource bundle.
     * @param resourceBundle
     */
    public void setUp()
    {
        resourceBundle = PowerMock.createMock(ResourceBundle.class);
        persistentManager = PowerMock.createMock(TnPersistentManager.class);
        resourceBundle.persistentManager = persistentManager;
        mockStore = new TnStoreMock();
        mockStore.load();
        
        key = 123;
        familyName = "en_US";
    }
    
    public void testGetString()
    {
//        PersistentResourceBundle tested = PowerMock.createPartialMock(PersistentResourceBundle.class, "getDataStore", String.class, String.class, boolean.class);
//        tested.resourceBundle = resourceBundle;
//        tested.textCaches = new Hashtable();
        PersistentResourceBundle tested = new PersistentResourceBundle(resourceBundle);
//        tested.dataStoreTable.put("abc", mockStore);
        EasyMock.expect(resourceBundle.getLocale()).andReturn("us_en").anyTimes();
        EasyMock.expect(persistentManager.createStore(TnPersistentManager.RMS_CRUMB, "i18n_us_en_strings_en_US")).andReturn(mockStore);

        PowerMock.replayAll();
        String expString = "Test persistentResourceBundle";
//        try
//        {
//            tested.putString(key, "", expString);
//        }
//        catch(Exception e)
//        {
//            assertTrue(e instanceof IllegalArgumentException);
//        }
        tested.putString(key, familyName, expString);
        String actualString = tested.getString(key, familyName);
        assertEquals(expString,actualString);
        PowerMock.verifyAll();
    }
    
      /**
     * Update String in persistent resource bundle.
     * 
     * @param key Key for searched-for resource object.
     * @param familyName the group's name of the strings.
     * @param value String form of resource object.
     */
    public void testPutString()
    {
//        PersistentResourceBundle tested = PowerMock.createPartialMock(PersistentResourceBundle.class, "getDataStore", String.class, String.class, boolean.class);
//        tested.resourceBundle = resourceBundle;
//        tested.textCaches = new Hashtable();
        PersistentResourceBundle tested = new PersistentResourceBundle(resourceBundle);
//        tested.dataStoreTable.put("abc", mockStore);
        EasyMock.expect(resourceBundle.getLocale()).andReturn("us_en").anyTimes();
        EasyMock.expect(persistentManager.createStore(TnPersistentManager.RMS_CRUMB, "i18n_us_en_strings_en_US")).andReturn(mockStore);

        PowerMock.replayAll();
        String expString = "Test persistentResourceBundle";
//        try
//        {
//            tested.putString(key, "", expString);
//        }
//        catch(Exception e)
//        {
//            assertTrue(e instanceof IllegalArgumentException);
//        }
        tested.putString(key, familyName, expString);
        String actualString = tested.getString(key, familyName);
        assertEquals(expString,actualString);
        PowerMock.verifyAll();
    }
 
  public void testStore()
  {
      PersistentResourceBundle tested = PowerMock.createPartialMock(PersistentResourceBundle.class, "getStoreNames");
      tested.resourceBundle = resourceBundle;
      EasyMock.expect(resourceBundle.getLocale()).andReturn(Locale.en_US).anyTimes();
      tested.resourceBundle.metaStore = new TnStoreMock();
      tested.resourceBundle.metaStore.load();
      tested.dataStoreTable = new Hashtable();
      tested.textCaches = new Hashtable();
      Vector v = new Vector();
      PersistentResourceBundle.StoreBean bean = new PersistentResourceBundle.StoreBean();
      bean.storeName = "unit test";
      bean.storeType = 1;
      v.addElement(bean);
      try
      {
          PowerMock.expectPrivate(tested, "getStoreNames").andReturn(v);
      }
      catch (Exception e)
      {
          e.printStackTrace();
      }
      tested.dataStoreTable.put("unit test", mockStore);
      PowerMock.replayAll();
      
      tested.store();

      assertEquals(1, resourceBundle.metaStore.size());
      PowerMock.verifyAll();
  }
    /**
     * Clear data in persistent resource bundle.
     */
    public void testClear()
    {
        PersistentResourceBundle tested = PowerMock.createPartialMock(PersistentResourceBundle.class,"getDataStore");
        tested.resourceBundle = resourceBundle;
        TnStore metaStoreMock = new TnStoreMock();
        metaStoreMock.load();
        metaStoreMock.put(ResourceBundle.META_STORE_NAMES
            + 10000, "unit test=1".getBytes());
        resourceBundle.metaStore = metaStoreMock;

        EasyMock.expect(resourceBundle.getLocale()).andReturn(Locale.en_US).anyTimes();
        tested.dataStoreTable = new Hashtable();
        tested.textCaches = new Hashtable();
//        Vector v = new Vector();
//        PersistentResourceBundle.StoreBean bean = new PersistentResourceBundle.StoreBean();
//        bean.storeName = "unit test";
//        bean.storeType = 1;
//        v.addElement(bean);
        try
        {
             PowerMock.expectPrivate(tested, "getDataStore", new Class<?>[] { int.class,  String.class}, 1,  "unit test").andReturn(mockStore);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        tested.dataStoreTable.put("unit test", mockStore);
        PowerMock.replayAll();
        
        tested.clear();

        assertEquals(0, tested.dataStoreTable.size());
        PowerMock.verifyAll();
    }

    public void testClearCurrentLocaleData()
    {
        PersistentResourceBundle tested = new PersistentResourceBundle(resourceBundle);

        tested.dataStoreTable.put("unit test", mockStore); //current locale data
        tested.dataStoreTable.put("_" + ResourceBundle.GENERIC_PATH + "_123", new TnStoreMock()); //generic data
        
        tested.textCaches.put("1", "test1");
        tested.textCaches.put("2", "test2");
        
        tested.clearCurrentLocaleData();

        assertEquals(1, tested.dataStoreTable.size());
        assertEquals(0, tested.textCaches.size());
    }
  
    public void testPutImage()
    {
        PersistentResourceBundle tested = PowerMock.createPartialMock(PersistentResourceBundle.class, "getDataStore", String.class, String.class, boolean.class);
        try
        {
            PowerMock.expectPrivate(tested, "getDataStore", ResourceBundle.IMAGE_PATH, familyName, false).andReturn(mockStore).times(2);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        PowerMock.replayAll();
        
        byte[] expData = new byte[]{0x0a,0x0b, 0x0c};
        String key = "test";
        tested.putImage(key, familyName, expData);
        
        byte[] actualData = tested.getImage(key, familyName);
        assertEquals(actualData, expData);
        PowerMock.verifyAll();
    }
    
    public void testGetImage()
    {
        PersistentResourceBundle tested = PowerMock.createPartialMock(PersistentResourceBundle.class, "getDataStore", String.class, String.class, boolean.class);
        try
        {
            PowerMock.expectPrivate(tested, "getDataStore", ResourceBundle.IMAGE_PATH, familyName, false).andReturn(mockStore).times(2);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        PowerMock.replayAll();
        
        byte[] expData = new byte[]{0x0a,0x0b, 0x0c};
        String key = "test";
        tested.putImage(key, familyName, expData);
        
        byte[] actualData = tested.getImage(key, familyName);
        assertEquals(actualData, expData);
        PowerMock.verifyAll();
    }
    
    public void testGetGenericImage()
    {
        PersistentResourceBundle tested = PowerMock.createPartialMock(PersistentResourceBundle.class, "getDataStore", String.class, String.class, boolean.class);
        try
        {
            PowerMock.expectPrivate(tested, "getDataStore", ResourceBundle.IMAGE_PATH, familyName, true).andReturn(mockStore).times(2);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        PowerMock.replayAll();
        
        byte[] expData = new byte[]{0x0a,0x0b, 0x0c};
        String key = "test";
        tested.putGenericImage(key, familyName, expData);
        
        byte[] actualData = tested.getGenericImage(key, familyName);
        assertEquals(actualData, expData);
        PowerMock.verifyAll();
    }
    
    public void testPutGenericImage()
    {
        PersistentResourceBundle tested = PowerMock.createPartialMock(PersistentResourceBundle.class, "getDataStore", String.class, String.class, boolean.class);
        try
        {
            PowerMock.expectPrivate(tested, "getDataStore", ResourceBundle.IMAGE_PATH, familyName, true).andReturn(mockStore).times(2);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        PowerMock.replayAll();
        
        byte[] expData = new byte[]{0x0a,0x0b, 0x0c};
        String key = "test";
        tested.putGenericImage(key, familyName, expData);
        
        byte[] actualData = tested.getGenericImage(key, familyName);
        assertEquals(actualData, expData);
        PowerMock.verifyAll();
    }

    public void testGetAudio()
    {
        //mock private method
        PersistentResourceBundle tested = PowerMock.createPartialMock(PersistentResourceBundle.class, "getDataStore", String.class, String.class, boolean.class);
        try
        {
            PowerMock.expectPrivate(tested, "getDataStore", ResourceBundle.AUDIO_PATH, familyName, false).andReturn(mockStore).times(2);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        PowerMock.replayAll();
        
        byte[] expData = new byte[]{0x0a,0x0b, 0x0c};

        tested.putAudio(key, familyName, expData);
        
        byte[] actualData = tested.getAudio(key, familyName);
        assertEquals(actualData, expData);
        PowerMock.verifyAll();
    }
    
    public void testPutAudio()
    {
        //mock private method
        PersistentResourceBundle tested = PowerMock.createPartialMock(PersistentResourceBundle.class, "getDataStore", String.class, String.class, boolean.class);
        try
        {
            PowerMock.expectPrivate(tested, "getDataStore", ResourceBundle.AUDIO_PATH, familyName, false).andReturn(mockStore).times(2);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        PowerMock.replayAll();
        
        byte[] expData = new byte[]{0x0a,0x0b, 0x0c};

        tested.putAudio(key, familyName, expData);
        
        byte[] actualData = tested.getAudio(key, familyName);
        assertEquals(actualData, expData);
        PowerMock.verifyAll();
    }
    
    public void testGetBinary()
    {
        //mock private method
        PersistentResourceBundle tested = PowerMock.createPartialMock(PersistentResourceBundle.class, "getDataStore", String.class, String.class, boolean.class);
        try
        {
            PowerMock.expectPrivate(tested, "getDataStore", ResourceBundle.BINARY_PATH, familyName, false).andReturn(mockStore).times(2);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        PowerMock.replayAll();
        
        byte[] expData = new byte[]{0x0a,0x0b, 0x0c};
        String key = "test_key";

        tested.putBinary(key, familyName, expData);
        
        byte[] actualData = tested.getBinary(key, familyName);
        assertEquals(actualData, expData);
        PowerMock.verifyAll();
    }
    
    public void testPutBinary()
    {
        //mock private method
        PersistentResourceBundle tested = PowerMock.createPartialMock(PersistentResourceBundle.class, "getDataStore", String.class, String.class, boolean.class);
        try
        {
            PowerMock.expectPrivate(tested, "getDataStore", ResourceBundle.BINARY_PATH, familyName, false).andReturn(mockStore).times(2);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        PowerMock.replayAll();
        
        byte[] expData = new byte[]{0x0a,0x0b, 0x0c};
        String key = "test_key";

        tested.putBinary(key, familyName, expData);
        
        byte[] actualData = tested.getBinary(key, familyName);
        assertEquals(actualData, expData);
        PowerMock.verifyAll();
    }
    
    public void testGetGenericBinary()
    {
        //mock private method
        PersistentResourceBundle tested = PowerMock.createPartialMock(PersistentResourceBundle.class, "getDataStore", String.class, String.class, boolean.class);
        try
        {
            PowerMock.expectPrivate(tested, "getDataStore", ResourceBundle.BINARY_PATH, familyName, true).andReturn(mockStore).times(2);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        PowerMock.replayAll();
        
        byte[] expData = new byte[]{0x0a,0x0b, 0x0c};
        String key = "test_key";

        tested.putGenericBinary(key, familyName, expData);
        
        byte[] actualData = tested.getGenericBinary(key, familyName);
        assertEquals(actualData, expData);
        PowerMock.verifyAll();
    }
    
    public void testPutGenericBinary()
    {
        //mock private method
        PersistentResourceBundle tested = PowerMock.createPartialMock(PersistentResourceBundle.class, "getDataStore", String.class, String.class, boolean.class);
        try
        {
            PowerMock.expectPrivate(tested, "getDataStore", ResourceBundle.BINARY_PATH, familyName, true).andReturn(mockStore).times(2);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        PowerMock.replayAll();
        
        byte[] expData = new byte[]{0x0a,0x0b, 0x0c};
        String key = "test_key";

        tested.putGenericBinary(key, familyName, expData);
        
        byte[] actualData = tested.getGenericBinary(key, familyName);
        assertEquals(actualData, expData);
        PowerMock.verifyAll();
    }
}
