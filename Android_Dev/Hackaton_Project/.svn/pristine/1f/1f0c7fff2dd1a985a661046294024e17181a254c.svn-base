package com.telenav.data.dao.misc;

import java.lang.reflect.Field;
import java.util.Enumeration;
import java.util.Hashtable;

import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import com.telenav.data.datatypes.preference.Preference;
import com.telenav.data.datatypes.preference.PreferenceGroup;
import com.telenav.data.datatypes.primitive.BytesList;
import com.telenav.data.serializable.IPreferenceSerializable;
import com.telenav.data.serializable.IPrimitiveSerializable;
import com.telenav.data.serializable.SerializableManager;
import com.telenav.data.serializable.txnode.TxNodeSerializableManager;
import com.telenav.persistent.TnStore;
import com.telenav.persistent.TnStoreMock;
import com.telenav.util.PrimitiveTypeCache;

/**
 *@author gbwang
 *@date 2011-6-24
 */

@RunWith(PowerMockRunner.class)
//@PrepareForTest({SerializableManager.class,IPrimitiveSerializable.class, IPreferenceSerializable.class, Preference.class, PreferenceDao.class})


public class PreferenceDaoTest extends TestCase
{
	int TN_PREFERENCES_INDEX = 100;
	int TN_PREFERENCES_GROUPS_INDEX = 101;
	int FILTER_PREFERENCES_HIDE_GROUPS_INDEX = 8000;
	short ID_PREFERENCE_ROUTE_SETTING = 15;
	
	byte[] prefersData = {48, -99, -101, 58, 0, 0, 0, 44, 57, 1, 0, 0, 0, 0, 33, 1, 48, -99, -101, 58, 6, 12, 0, 2, 2, 4, 6, 14, 12, 26, 82, 111, 117, 116, 101, 32, 83, 101, 116, 116, 105, 110, 103, 0, 14, 70, 97, 115, 116, 101, 115, 116, 16, 83, 104, 111, 114, 116, 101, 115, 116, 26, 65, 118, 111, 105, 100, 32, 72, 105, 103, 104, 119, 97, 121, 20, 80, 101, 100, 101, 115, 116, 114, 105, 97, 110, 0, 0, 92, 0, 0, 0, 0, 82, 48, -99, -101, 58, 4, 10, -99, 1, 0, 0, 2, 8, 22, 76, 97, 110, 101, 32, 65, 115, 115, 105, 115, 116, 8, 83, 104, 111, 119, 8, 83, 104, 111, 119, 8, 72, 105, 100, 101, 0, 0, 1, 1, 0, 0, 0, 0, 118, 48, -99, -101, 58, 4, 8, 2, 2, 0, 2, 8, 28, 68, 105, 115, 116, 97, 110, 99, 101, 32, 85, 110, 105, 116, 115, 18, 75, 109, 47, 77, 101, 116, 101, 114, 115, 18, 75, 109, 47, 77, 101, 116, 101, 114, 115, 20, 77, 105, 108, 101, 115, 47, 70, 101, 101, 116, 0, 0, 88, 0, 0, 0, 0, 78, 48, -99, -101, 58, 4, 10, -111, 1, 2, 0, 2, 8, 28, 84, 114, 97, 102, 102, 105, 99, 32, 67, 97, 109, 101, 114, 97, 4, 79, 110, 4, 79, 110, 6, 79, 102, 102, 0, 0, 118, 0, 0, 0, 0, 108, 48, -99, -101, 58, 4, 8, 4, 4, 4, 2, 8, 18, 77, 97, 112, 32, 83, 116, 121, 108, 101, 0, 28, 51, 68, 32, 77, 111, 118, 105, 110, 103, 32, 77, 97, 112, 115, 28, 50, 68, 32, 77, 111, 118, 105, 110, 103, 32, 77, 97, 112, 115, 0, 0, -127, 1, 0, 0, 0, 0, 105, 1, 48, -99, -101, 58, 6, 12, 6, 6, 6, 4, 2, 0, 12, 28, 65, 117, 100, 105, 111, 32, 71, 117, 105, 100, 97, 110, 99, 101, 8, 78, 111, 110, 101, 40, 68, 105, 114, 101, 99, 116, 105, 111, 110, 115, 32, 38, 32, 84, 114, 97, 102, 102, 105, 99, 30, 68, 105, 114, 101, 99, 116, 105, 111, 110, 115, 32, 79, 110, 108, 121, 24, 84, 114, 97, 102, 102, 105, 99, 32, 79, 110, 108, 121, 8, 78, 111, 110, 101, 0, 0, 80, 0, 0, 0, 0, 70, 48, -99, -101, 58, 4, 10, -107, 1, 0, 0, 2, 8, 20, 83, 112, 101, 101, 100, 32, 84, 114, 97, 112, 4, 79, 110, 4, 79, 110, 6, 79, 102, 102, 0, 0, 102, 0, 0, 0, 0, 92, 48, -99, -101, 58, 4, 8, 14, 0, 0, 2, 8, 44, 84, 114, 97, 102, 102, 105, 99, 32, 73, 110, 99, 105, 100, 101, 110, 116, 32, 65, 108, 101, 114, 116, 4, 79, 110, 4, 79, 110, 6, 79, 102, 102, 0, 0, 126, 0, 0, 0, 0, 116, 48, -99, -101, 58, 4, 8, 18, 0, 0, 2, 8, 16, 76, 97, 110, 103, 117, 97, 103, 101, 22, 69, 110, 103, 108, 105, 115, 104, 40, 85, 83, 41, 22, 69, 110, 103, 108, 105, 115, 104, 40, 85, 83, 41, 22, 83, 112, 97, 110, 105, 115, 104, 40, 77, 88, 41, 0, 0, 104, 0, 0, 0, 0, 94, 48, -99, -101, 58, 3, 6, 20, 0, 0, 6, 12, 82, 101, 103, 105, 111, 110, 26, 78, 111, 114, 116, 104, 32, 65, 109, 101, 114, 105, 99, 97, 26, 78, 111, 114, 116, 104, 32, 65, 109, 101, 114, 105, 99, 97, 0, 0, 116, 0, 0, 0, 0, 106, 48, -99, -101, 58, 4, 8, 24, 2, 2, 0, 8, 20, 71, 80, 83, 32, 83, 111, 117, 114, 99, 101, 18, 66, 108, 117, 101, 116, 111, 111, 116, 104, 16, 73, 110, 116, 101, 114, 110, 97, 108, 18, 66, 108, 117, 101, 116, 111, 111, 116, 104, 0, 0, 53, 1, 0, 0, 0, 0, 29, 1, 48, -99, -101, 58, 5, 10, 26, 0, 0, 4, 2, 10, 18, 66, 97, 99, 107, 76, 105, 103, 104, 116, 18, 65, 108, 119, 97, 121, 115, 32, 79, 110, 18, 65, 108, 119, 97, 121, 115, 32, 79, 110, 22, 79, 110, 32, 97, 116, 32, 84, 117, 114, 110, 115, 28, 68, 101, 118, 105, 99, 101, 32, 68, 101, 102, 97, 117, 108, 116, 0, 0, 92, 0, 0, 0, 0, 82, 48, -99, -101, 58, 4, 10, -95, 1, 0, 0, 2, 8, 22, 83, 112, 101, 101, 100, 32, 76, 105, 109, 105, 116, 8, 83, 104, 111, 119, 8, 83, 104, 111, 119, 8, 72, 105, 100, 101, 0, 0, 97, 1, 0, 0, 0, 0, 73, 1, 48, -99, -101, 58, 5, 10, 30, 10, 8, 4, 0, 10, 10, 65, 118, 111, 105, 100, 34, 85, 115, 101, 32, 67, 97, 114, 112, 111, 111, 108, 32, 76, 97, 110, 101, 115, 26, 65, 118, 111, 105, 100, 32, 84, 114, 97, 102, 102, 105, 99, 22, 65, 118, 111, 105, 100, 32, 84, 111, 108, 108, 115, 34, 85, 115, 101, 32, 67, 97, 114, 112, 111, 111, 108, 32, 76, 97, 110, 101, 115, 0, 0, 114, 0, 0, 0, 0, 104, 48, -99, -101, 58, 4, 8, 36, 0, 0, 2, 8, 34, 65, 117, 100, 105, 111, 32, 68, 117, 114, 105, 110, 103, 32, 67, 97, 108, 108, 14, 83, 117, 115, 112, 101, 110, 100, 14, 83, 117, 115, 112, 101, 110, 100, 8, 80, 108, 97, 121, 0, 0, 106, 0, 0, 0, 0, 96, 48, -99, -101, 58, 5, 10, 46, 6, 6, 2, 4, 10, 18, 77, 97, 112, 32, 67, 111, 108, 111, 114, 0, 8, 65, 117, 116, 111, 14, 68, 97, 121, 116, 105, 109, 101, 18, 78, 105, 103, 104, 116, 116, 105, 109, 101, 0, 0, 44, 0, 0, 0, 0, 34, 48, -99, -101, 58, 2, 4, 50, 0, 4, 8, 78, 97, 109, 101, 0, 0, 0, 46, 0, 0, 0, 0, 36, 48, -99, -101, 58, 2, 4, 48, 0, 4, 10, 69, 109, 97, 105, 108, 0, 0, 0, 66, 0, 0, 0, 0, 56, 48, -99, -101, 58, 2, 4, 54, 0, 4, 10, 80, 104, 111, 110, 101, 20, 52, 48, 56, 51, 54, 56, 55, 53, 52, 49, 0, 0, 54, 0, 0, 0, 0, 44, 48, -99, -101, 58, 2, 4, 52, 0, 4, 18, 76, 97, 115, 116, 32, 78, 97, 109, 101, 0, 0, 0, 124, 0, 0, 0, 0, 114, 48, -99, -101, 58, 5, 10, 56, 0, 0, 2, 4, 10, 30, 67, 111, 110, 118, 101, 110, 105, 101, 110, 99, 101, 32, 75, 101, 121, 8, 76, 101, 102, 116, 8, 76, 101, 102, 116, 10, 82, 105, 103, 104, 116, 20, 68, 111, 32, 110, 111, 116, 32, 117, 115, 101, 0, 0, 54, 0, 0, 0, 0, 44, 48, -99, -101, 58, 2, 6, -31, 1, 0, 4, 16, 85, 115, 101, 114, 110, 97, 109, 101, 0, 0, 0, 0};
	byte[] groupData = {48, -99, -101, 58, 0, 0, 0, 14, 56, 0, 0, 0, 0, 46, 48, -99, -101, 58, 5, 12, 2, 50, 52, 48, -31, 1, 2, 14, 80, 114, 111, 102, 105, 108, 101, 0, 0, 52, 0, 0, 0, 0, 42, 48, -99, -101, 58, 4, 8, 4, 20, 18, 2, 2, 14, 71, 101, 110, 101, 114, 97, 108, 0, 0, 76, 0, 0, 0, 0, 66, 48, -99, -101, 58, 10, 26, 6, 0, 46, 30, 4, 26, -95, 1, -99, 1, 14, -111, 1, 2, 20, 78, 97, 118, 105, 103, 97, 116, 105, 111, 110, 0, 0, 46, 0, 0, 0, 0, 36, 48, -99, -101, 58, 3, 6, 8, 6, 36, 2, 10, 65, 117, 100, 105, 111, 0, 0, 56, 0, 0, 0, 0, 46, 48, -99, -101, 58, 1, 2, 10, 2, 24, 72, 111, 109, 101, 32, 65, 100, 100, 114, 101, 115, 115, 0, 0, 56, 0, 0, 0, 0, 46, 48, -99, -101, 58, 1, 2, 12, 2, 24, 87, 111, 114, 107, 32, 65, 100, 100, 114, 101, 115, 115, 0, 0, 64, 0, 0, 0, 0, 54, 48, -99, -101, 58, 5, 14, 1, 125, 54, 24, -107, 1, 56, 2, 20, 72, 73, 68, 69, 95, 71, 82, 79, 85, 80, 0, 0, 0};
	TnStoreMock mockStore;
	PreferenceDao preferenceDao;
	
	BytesList prefList, mockPrefList;
	BytesList groupList, mockGroupList;	
	Hashtable preferenceParam, tempParam;	
    PreferenceGroup[] prefGroups, mockPrefGroups;
    PreferenceGroup[] filterPrefGroups, mockFilterPrefGroups;
    
    Preference mockPreference;
	PreferenceGroup mockPreferenceGroup;    
	
	SerializableManager serializableManager;
	IPrimitiveSerializable iPrimitiveSerializable;
	IPreferenceSerializable iPreferenceSerializable;
	
//	protected void setUp() throws Exception
//	{		
//		mockStore = new TnStoreMock(); 
//    	mockStore.load();
//    	mockStore.put(TN_PREFERENCES_INDEX, prefersData);
//		mockStore.put(TN_PREFERENCES_GROUPS_INDEX, groupData);
//		preferenceDao = new PreferenceDao(mockStore);
//
//		SerializableManager.init(new TxNodeSerializableManager());		
//		prefList = SerializableManager.getInstance().getPrimitiveSerializable().createBytesList(prefersData);	
//		preferenceParam = new Hashtable();
//		tempParam = new Hashtable();
//    	for (int i = 0; i < prefList.size(); i++)
//        {
//            Preference pref = SerializableManager.getInstance().getPreferenceSerializable().createPreference(prefList.elementAt(i));
//            preferenceParam.put(PrimitiveTypeCache.valueOf(pref.getId()), pref); 
//            tempParam.put(i, pref); 
//        }
//    	
//    	groupList = SerializableManager.getInstance().getPrimitiveSerializable().createBytesList(groupData);
//    	prefGroups = new PreferenceGroup[groupList.size()];
//    	for (int i = 0; i < groupList.size(); i++)
//        {
//            prefGroups[i] = SerializableManager.getInstance().getPreferenceSerializable().createPreferenceGroup(groupList.elementAt(i));
//        }
//    	filterPrefGroups();
//		
//		PowerMock.mockStatic(SerializableManager.class);
//		serializableManager = PowerMock.createMock(SerializableManager.class);
//		EasyMock.expect(SerializableManager.getInstance()).andReturn(serializableManager).anyTimes();
//		
//		PowerMock.mockStatic(IPrimitiveSerializable.class);
//		iPrimitiveSerializable = PowerMock.createMock(IPrimitiveSerializable.class);
//		EasyMock.expect(serializableManager.getPrimitiveSerializable()).andReturn(iPrimitiveSerializable).anyTimes();
//		
//		PowerMock.mockStatic(IPreferenceSerializable.class);
//		iPreferenceSerializable = PowerMock.createMock(IPreferenceSerializable.class);
//		EasyMock.expect(serializableManager.getPreferenceSerializable()).andReturn(iPreferenceSerializable).anyTimes();
//		
//		mockPrefList = PowerMock.createMock(BytesList.class);
//		EasyMock.expect(iPrimitiveSerializable.createBytesList(mockStore.get(TN_PREFERENCES_INDEX))).andReturn(mockPrefList).anyTimes();
//		EasyMock.expect(mockPrefList.size()).andReturn(prefList.size()).anyTimes();
//		for(int i = 0; i < prefList.size(); i++)
//		{
//			byte[] prefListElement = prefList.elementAt(i);
//			EasyMock.expect(mockPrefList.elementAt(i)).andReturn(prefListElement).anyTimes();
//			Preference pref = (Preference) tempParam.get(i);
//			mockPreference = PowerMock.createMock(Preference.class);
//			EasyMock.expect(iPreferenceSerializable.createPreference(prefListElement)).andReturn(mockPreference).anyTimes();
//			EasyMock.expect(mockPreference.getId()).andReturn(pref.getId()).anyTimes();
//			EasyMock.expect(mockPreference.getIntValue()).andReturn(pref.getIntValue()).anyTimes();
//		}
//		mockGroupList = PowerMock.createMock(BytesList.class);
//		EasyMock.expect(iPrimitiveSerializable.createBytesList(mockStore.get(TN_PREFERENCES_GROUPS_INDEX))).andReturn(mockGroupList).anyTimes();
//		EasyMock.expect(mockGroupList.size()).andReturn(groupList.size()).anyTimes();
//		for(int i = 0; i < groupList.size(); i++)
//		{
//			byte[] groupListElement = groupList.elementAt(i);
//			EasyMock.expect(mockGroupList.elementAt(i)).andReturn(groupListElement).anyTimes();
//			EasyMock.expect(iPreferenceSerializable.createPreferenceGroup(groupListElement)).andReturn(prefGroups[i]).anyTimes();
//		}
//	}
//	
//	private void filterPrefGroups()
//    {
//        int filterNum = 0;
//        for (int i = 0; i < prefGroups.length; i++)
//        {
//            if ( prefGroups[i].getId() == FILTER_PREFERENCES_HIDE_GROUPS_INDEX )
//            {
//                filterNum ++;
//            }
//        }
//        
//        if( filterNum == 0 )
//        {
//            filterPrefGroups = prefGroups;
//            return;
//        }
//        filterPrefGroups = new PreferenceGroup[prefGroups.length-filterNum];
//        for (int i = 0, j = 0; i < prefGroups.length && j < filterPrefGroups.length; i++)
//        {
//            if ( prefGroups[i].getId() != FILTER_PREFERENCES_HIDE_GROUPS_INDEX )
//            {
//                filterPrefGroups[j] = prefGroups[i];
//                j ++;
//            }
//        }
//    }
//	
//	public void testSetDataStore() throws IllegalArgumentException, IllegalAccessException
//	{
//		PowerMock.replayAll();
//		
//		preferenceDao.load(0);
//		preferenceDao.setDataStore(mockStore);
//		Field prefStoreField = Whitebox.getField(PreferenceDao.class, "prefStore");
//		TnStore prefStoreWhiteBox = (TnStore) prefStoreField.get(preferenceDao);
//		
//		Assert.assertEquals(mockStore, prefStoreWhiteBox);
//		
//		PowerMock.verifyAll();
//	}
//	
//	public void testGetSyncPreferenceSettingDataTime()
//	{
//		PowerMock.replayAll();
//		
//		preferenceDao.load(0);
//
//		String syncTime = preferenceDao.getSyncPreferenceSettingDataTime();
//		Assert.assertEquals("0", syncTime);
//		
//		PowerMock.verifyAll();
//	}
//	
//	public void testSetSyncPreferenceSettingDataTime()
//	{
//		PowerMock.replayAll();
//		
//		preferenceDao.load(0);
//		String timeStamp =System.currentTimeMillis() + "";
//		mockStore.put(PreferenceDao.SYNC_PREFERENCE_SETTING_DATA_TIME, timeStamp.getBytes());
//		preferenceDao.setSyncPreferenceSettingDataTime(timeStamp);
//		
//		Assert.assertEquals(timeStamp, preferenceDao.getSyncPreferenceSettingDataTime());
//		
//		PowerMock.verifyAll();
//	}
//	
//	public void testGetSettingData() throws Exception
//	{
//		PowerMock.replayAll();
//		
//		preferenceDao.load(0);
//		preferenceDao.getSettingData();
//		
//		PowerMock.verifyAll();
//	}
//	
//	public void testGetPreferenceGroups() throws IllegalArgumentException, IllegalAccessException
//	{
//		PowerMock.replayAll();
//		
//		preferenceDao.load(0);
//		Field prefGroupsField = Whitebox.getField(PreferenceDao.class, "prefGroups");
//		PreferenceGroup[] prefGroupsWhiteBox = (PreferenceGroup[]) prefGroupsField.get(preferenceDao);
//		
//		Assert.assertArrayEquals(preferenceDao.getPreferenceGroups(), prefGroupsWhiteBox);
//		
//		PowerMock.verifyAll();
//	}
//	
//	public void testGetFilterPreferenceGroups() throws IllegalArgumentException, IllegalAccessException
//	{
//		PowerMock.replayAll();
//		
//		preferenceDao.load(0);
//		Field filterPrefGroupsField = Whitebox.getField(PreferenceDao.class, "filterPrefGroups");
//		PreferenceGroup[] filterPrefGroupsWhiteBox = (PreferenceGroup[]) filterPrefGroupsField.get(preferenceDao);
//		
//		Assert.assertArrayEquals(preferenceDao.getFilterPreferenceGroups(), filterPrefGroupsWhiteBox);
//		
//		PowerMock.verifyAll();
//	}
//	
//	public void testGetPreferenceIdsByGroup()
//	{
//		PowerMock.replayAll();
//		
//		preferenceDao.load(0);
//		for(int i = 0; i < prefGroups.length; i++)
//		{
//			Assert.assertArrayEquals(prefGroups[i].getPreferenceIds(), preferenceDao.getPreferenceIdsByGroup(prefGroups[i].getId()));
//		}
//
//		PowerMock.verifyAll();
//	}
//	
//	public void testGetPreference()
//	{
//		PowerMock.replayAll();
//		
//		preferenceDao.load(0);
//		Enumeration prefEnum = this.preferenceParam.keys();
//        while(prefEnum.hasMoreElements())
//        {
//            Preference pref = (Preference)this.preferenceParam.get(prefEnum.nextElement());
//            int id = pref.getId();
//            Assert.assertEquals(id, preferenceDao.getPreference(id).getId());
//        }
//
//		PowerMock.verifyAll();
//	}
//	
//	public void testGetIntValue()
//	{
//		PowerMock.replayAll();
//		
//		preferenceDao.load(0);
//		int value = preferenceDao.getIntValue(Preference.ID_PREFERENCE_ROUTE_SETTING);
//		Assert.assertEquals(1, value);
//		
//		PowerMock.verifyAll();
//	}
//	
//	public void testGetStrValue()
//	{
//		PowerMock.replayAll();
//		
//		preferenceDao.load(0);
//		String value = preferenceDao.getStrValue(Preference.ID_PREFERENCE_ROUTE_SETTING);
//		preferenceDao.setStrValue(Preference.ID_PREFERENCE_ROUTE_SETTING, value);
//		Assert.assertEquals("", value);
//		
//		PowerMock.verifyAll();
//	}
//	
//	public void testIndex2Value()
//	{
//		PowerMock.replayAll();
//		
//		preferenceDao.load(0);
//		Preference pref = (Preference) preferenceParam.get(PrimitiveTypeCache.valueOf(Preference.ID_PREFERENCE_ROUTE_SETTING));
//		int index = 0;
//		int value = preferenceDao.index2Value(pref, index);
//		Assert.assertEquals(4, value);
//		
//		PowerMock.verifyAll();
//	}
//	
//	public void testValue2Index()
//	{
//		PowerMock.replayAll();
//		
//		preferenceDao.load(0);
//		Preference pref = (Preference) preferenceParam.get(PrimitiveTypeCache.valueOf(Preference.ID_PREFERENCE_ROUTE_SETTING));
//		int value = 4;
//		int index = preferenceDao.value2Index(pref, value);
//		Assert.assertEquals(0, index);
//		
//		PowerMock.verifyAll();
//	}
//	
//	public void testNeedUpload()
//	{
//		PowerMock.replayAll();
//		
//		preferenceDao.load(0);
//		preferenceDao.setIsNeedUpload(true);
//		Assert.assertTrue(preferenceDao.isNeedUpload());
//		
//		PowerMock.verifyAll();
//	}
//	
//	public void testLoadPreferences_A() throws IllegalArgumentException, IllegalAccessException
//	{
//		PowerMock.replayAll();
//		preferenceDao.load(0);
//		byte[] buf = "test home=2".getBytes();
//		preferenceDao.loadPreferences(buf);
//		Field prefGroupsField = Whitebox.getField(PreferenceDao.class, "prefGroups");
//		PreferenceGroup[] prefGroupsWhiteBox = (PreferenceGroup[]) prefGroupsField.get(preferenceDao);
//		Assert.assertEquals(1, prefGroupsWhiteBox.length);
//		Assert.assertEquals(2, prefGroupsWhiteBox[0].getId());
//		Assert.assertEquals("home", prefGroupsWhiteBox[0].getName());
//		PowerMock.verifyAll();
//	}
//	
//	public void testLoadPreferences_B() throws IllegalArgumentException, IllegalAccessException
//	{
//		PowerMock.replayAll();
//		preferenceDao.load(0);
//		byte[] buf = "1000 Profile=1,General=2,Navigation=3,Audio=4,HIDE_GROUP=8000\n 4 Language=2,English(US)|en_US#0,Spanish(MX)|es_MX#1".getBytes();
//		preferenceDao.loadPreferences(buf);
//		Field prefGroupsField = Whitebox.getField(PreferenceDao.class, "prefGroups");
//		PreferenceGroup[] prefGroupsWhiteBox = (PreferenceGroup[]) prefGroupsField.get(preferenceDao);
//		Assert.assertEquals(5, prefGroupsWhiteBox.length);
//		Assert.assertEquals(1, prefGroupsWhiteBox[0].getId());
//		Assert.assertEquals("Profile", prefGroupsWhiteBox[0].getName());
//		Assert.assertEquals(2, prefGroupsWhiteBox[1].getId());
//		Assert.assertEquals("General", prefGroupsWhiteBox[1].getName());
//		Assert.assertEquals(3, prefGroupsWhiteBox[2].getId());
//		Assert.assertEquals("Navigation", prefGroupsWhiteBox[2].getName());
//		Assert.assertEquals(4, prefGroupsWhiteBox[3].getId());
//		Assert.assertEquals("Audio", prefGroupsWhiteBox[3].getName());
//		Assert.assertEquals(8000, prefGroupsWhiteBox[4].getId());
//		Assert.assertEquals("HIDE_GROUP", prefGroupsWhiteBox[4].getName());
//		PowerMock.verifyAll();
//	}
//	
//	public void testClear() throws IllegalArgumentException, IllegalAccessException
//	{
//		PowerMock.replayAll();
//		
//		preferenceDao.load(0);
//		preferenceDao.clear();
//		Field prefStoreField = Whitebox.getField(PreferenceDao.class, "prefStore");
//		TnStore prefStoreWhiteBox = (TnStore) prefStoreField.get(preferenceDao);
//		Assert.assertEquals(0, prefStoreWhiteBox.size());
//		PowerMock.verifyAll();
//	}
//	
//	public void testclearPreferenceCache() throws IllegalArgumentException, IllegalAccessException
//	{
//		PowerMock.replayAll();
//		preferenceDao.load(0);
//		preferenceDao.clearPreferenceCache();
//		Field preferenceParamField = Whitebox.getField(PreferenceDao.class, "preferenceParam");
//		Hashtable preferenceParamWhiteBox = (Hashtable) preferenceParamField.get(preferenceDao);
//		Assert.assertTrue(preferenceParamWhiteBox.isEmpty());
//		
//		Field prefGroupsField = Whitebox.getField(PreferenceDao.class, "prefGroups");
//		PreferenceGroup[] prefGroupsWhiteBox = (PreferenceGroup[]) prefGroupsField.get(preferenceDao);
//		Assert.assertNull(prefGroupsWhiteBox);
//		
//		PowerMock.verifyAll();
//	}
}
