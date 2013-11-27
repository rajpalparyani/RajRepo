package com.telenav.persistent.android;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

import java.util.Hashtable;
import java.util.Vector;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.telenav.persistent.ITnPersistentContext;
import com.telenav.persistent.TnPersistentManager;

import android.content.Context;

@RunWith(PowerMockRunner.class)
@PrepareForTest({AndroidRmsStore.class, TnPersistentManager.class, AndroidFilePersistentUtil.class})
public class AndroidRmsStoreTest 
{
	private AndroidRmsStore rmsStore;
	
	@Before
	public void setUp() throws Exception
	{
		rmsStore = new AndroidRmsStore("name", true, 1);
	}
	
	@Test
	public void testClearDelegate()
	{
		PowerMock.resetAll();
		
		TnPersistentManager persisManager = PowerMock.createMock(TnPersistentManager.class);
		IAndroidPersistentContext persisContext = PowerMock.createMock(IAndroidPersistentContext.class);
		Context context = PowerMock.createMock(Context.class);
		
		PowerMock.mockStatic(TnPersistentManager.class);
		EasyMock.expect(TnPersistentManager.getInstance()).andReturn(persisManager).times(2);
		EasyMock.expect(persisManager.getPersistentContext()).andReturn(persisContext).times(2);
		EasyMock.expect(persisContext.getContext()).andReturn(context);
		EasyMock.expect(persisContext.getApplicationName()).andReturn("appName");
		
		PowerMock.mockStatic(AndroidFilePersistentUtil.class);
		AndroidFilePersistentUtil.deleteData(EasyMock.anyObject(Context.class), EasyMock.anyObject(String.class));
		
//		PowerMock.mockStatic(AndroidFilePersistentUtil.class);
//		AndroidFilePersistentUtil.storeObject(EasyMock.anyObject(Context.class), EasyMock.anyObject(String.class), EasyMock.anyObject(Hashtable.class));
//		
//		PowerMock.mockStatic(AndroidFilePersistentUtil.class);
//		AndroidFilePersistentUtil.deleteData(EasyMock.anyObject(Context.class), EasyMock.anyObject(String.class));
//		
//		byte[] data = new byte[20];
//		rmsStore.put(20, data);	
		PowerMock.replayAll();
		rmsStore.clearDelegate();
		PowerMock.verifyAll();	
	}
	
	
	@Test
	public void testClearDelegate_NotCrumb()
	{
		PowerMock.resetAll();
		
		rmsStore.isCrumb = true;
		
		TnPersistentManager persisManager = PowerMock.createMock(TnPersistentManager.class);
		IAndroidPersistentContext persisContext = PowerMock.createMock(IAndroidPersistentContext.class);
		Context context = PowerMock.createMock(Context.class);
		
		PowerMock.mockStatic(TnPersistentManager.class);
		EasyMock.expect(TnPersistentManager.getInstance()).andReturn(persisManager).times(2);
		EasyMock.expect(persisManager.getPersistentContext()).andReturn(persisContext).times(2);
		EasyMock.expect(persisContext.getContext()).andReturn(context).times(1);
		EasyMock.expect(persisContext.getApplicationName()).andReturn("appName").times(1);

		Hashtable hashTable = new Hashtable();
		hashTable.put(new String("20"), new String("key"));
		PowerMock.mockStatic(AndroidFilePersistentUtil.class);
		EasyMock.expect(AndroidFilePersistentUtil.loadObject(EasyMock.anyObject(Context.class), EasyMock.anyObject(String.class))).andReturn(hashTable);
			
		PowerMock.replayAll();
		rmsStore.load();
		PowerMock.verifyAll();	
		
		/******************************************************************************/
		
		
		byte[] data = new byte[20];
		rmsStore.put(20, data);	
		rmsStore.put(40, data);
		rmsStore.isCrumb = false;
		assertEquals(rmsStore.size(),2);
		rmsStore.keys();
		
		assertEquals(rmsStore.get(30), null);
		assertEquals(rmsStore.contains(20), true);
		rmsStore.isCrumb = true;
		assertNotNull(rmsStore.get(40));
		rmsStore.remove(30);
		rmsStore.remove("30");
		rmsStore.remove("20");
		rmsStore.isCrumb = false;
		
		PowerMock.resetAll();
		PowerMock.mockStatic(TnPersistentManager.class);
		EasyMock.expect(TnPersistentManager.getInstance()).andReturn(persisManager).times(4);
		EasyMock.expect(persisManager.getPersistentContext()).andReturn(persisContext).times(4);
		EasyMock.expect(persisContext.getContext()).andReturn(context).times(2);
		EasyMock.expect(persisContext.getApplicationName()).andReturn("appName").times(2);
	
		PowerMock.mockStatic(AndroidFilePersistentUtil.class);
		AndroidFilePersistentUtil.deleteData(EasyMock.anyObject(Context.class), EasyMock.anyObject(String.class));
		
		//PowerMock.mockStatic(AndroidFilePersistentUtil.class); //NOTE
		AndroidFilePersistentUtil.deleteData(EasyMock.anyObject(Context.class), EasyMock.anyObject(String.class));
			
		PowerMock.replayAll();
		rmsStore.clear();
		PowerMock.verifyAll();	
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testBasePut() {

		PowerMock.resetAll();

		rmsStore.isCrumb = true;

		TnPersistentManager persisManager = PowerMock
				.createMock(TnPersistentManager.class);
		IAndroidPersistentContext persisContext = PowerMock
				.createMock(IAndroidPersistentContext.class);
		Context context = PowerMock.createMock(Context.class);

		PowerMock.mockStatic(TnPersistentManager.class);
		EasyMock.expect(TnPersistentManager.getInstance()).andReturn(
				persisManager).times(2);
		EasyMock.expect(persisManager.getPersistentContext()).andReturn(
				persisContext).times(2);
		EasyMock.expect(persisContext.getContext()).andReturn(context).times(1);
		EasyMock.expect(persisContext.getApplicationName())
				.andReturn("appName").times(1);

		Hashtable hashTable = new Hashtable();
		hashTable.put(new String("20"), new String("key"));
		PowerMock.mockStatic(AndroidFilePersistentUtil.class);
		EasyMock.expect(
				AndroidFilePersistentUtil.loadObject(EasyMock
						.anyObject(Context.class), EasyMock
						.anyObject(String.class))).andReturn(hashTable);

		PowerMock.replayAll();
		rmsStore.load();
		PowerMock.verifyAll();
		rmsStore.put("id", null);
	}
	
	@Test
	public void testGetDelegate()
	{
		rmsStore.isCrumb = true;
		assertEquals(rmsStore.getDelegate("522"), null);
		
	}
	
	@Test
	public void testGetDelegate_NotCrumb()
	{
		PowerMock.resetAll();
		rmsStore.isCrumb = false;
		TnPersistentManager persisManager = PowerMock.createMock(TnPersistentManager.class);
		IAndroidPersistentContext persisContext = PowerMock.createMock(IAndroidPersistentContext.class);
		Context context = PowerMock.createMock(Context.class);
		
		PowerMock.mockStatic(TnPersistentManager.class);
		EasyMock.expect(TnPersistentManager.getInstance()).andReturn(persisManager).times(2);
		EasyMock.expect(persisManager.getPersistentContext()).andReturn(persisContext).times(2);
		EasyMock.expect(persisContext.getContext()).andReturn(context);
		EasyMock.expect(persisContext.getApplicationName()).andReturn("appName");
		
		PowerMock.mockStatic(AndroidFilePersistentUtil.class);
		EasyMock.expect(AndroidFilePersistentUtil.readData(EasyMock.anyObject(Context.class), EasyMock.anyObject(String.class))).andReturn(null);
		
		PowerMock.replayAll();
		rmsStore.getDelegate("522");
		PowerMock.verifyAll();		
	}
	
	@Test
	public void testLoadDelegate()
	{
		PowerMock.resetAll();
		
		rmsStore.isCrumb = false;
		TnPersistentManager persisManager = PowerMock.createMock(TnPersistentManager.class);
		IAndroidPersistentContext persisContext = PowerMock.createMock(IAndroidPersistentContext.class);
		Context context = PowerMock.createMock(Context.class);
		
		PowerMock.mockStatic(TnPersistentManager.class);
		EasyMock.expect(TnPersistentManager.getInstance()).andReturn(persisManager).times(4);
		EasyMock.expect(persisManager.getPersistentContext()).andReturn(persisContext).times(4);
		EasyMock.expect(persisContext.getContext()).andReturn(context).times(2);
		EasyMock.expect(persisContext.getApplicationName()).andReturn("appName").times(2);
		
		PowerMock.mockStatic(AndroidFilePersistentUtil.class);
		AndroidFilePersistentUtil.mkDir(EasyMock.anyObject(Context.class), EasyMock.anyObject(String.class));
		EasyMock.expect(AndroidFilePersistentUtil.readIndexes(EasyMock.anyObject(Context.class), EasyMock.anyObject(String.class))).andReturn(null);
		
		PowerMock.replayAll();
		rmsStore.loadDelegate();
		PowerMock.verifyAll();
	}
	
	@Test
	public void testSaveDelegate_NotCrumb()
	{
		PowerMock.resetAll();
		rmsStore.isCrumb = true;
		TnPersistentManager persisManager = PowerMock.createMock(TnPersistentManager.class);
		IAndroidPersistentContext persisContext = PowerMock.createMock(IAndroidPersistentContext.class);
		Context context = PowerMock.createMock(Context.class);
		
		PowerMock.mockStatic(TnPersistentManager.class);
		EasyMock.expect(TnPersistentManager.getInstance()).andReturn(persisManager).times(2);
		EasyMock.expect(persisManager.getPersistentContext()).andReturn(persisContext).times(2);
		EasyMock.expect(persisContext.getContext()).andReturn(context).times(1);
		EasyMock.expect(persisContext.getApplicationName()).andReturn("appName").times(1);
		
		PowerMock.mockStatic(AndroidFilePersistentUtil.class);
		AndroidFilePersistentUtil.storeObject(EasyMock.anyObject(Context.class), EasyMock.anyObject(String.class), EasyMock.anyObject(Hashtable.class));
		PowerMock.replayAll();
		rmsStore.saveDelegate();
		PowerMock.verifyAll();	
	}
	
	@Test
	public void testSaveDelegate_()
	{
		PowerMock.resetAll();
		rmsStore.isCrumb = false;
		TnPersistentManager persisManager = PowerMock.createMock(TnPersistentManager.class);
		IAndroidPersistentContext persisContext = PowerMock.createMock(IAndroidPersistentContext.class);
		Context context = PowerMock.createMock(Context.class);
		
		PowerMock.mockStatic(TnPersistentManager.class);
		EasyMock.expect(TnPersistentManager.getInstance()).andReturn(persisManager).times(2);
		EasyMock.expect(persisManager.getPersistentContext()).andReturn(persisContext).times(2);
		EasyMock.expect(persisContext.getContext()).andReturn(context).times(1);
		EasyMock.expect(persisContext.getApplicationName()).andReturn("appName").times(1);
		
		PowerMock.mockStatic(AndroidFilePersistentUtil.class);
		AndroidFilePersistentUtil.saveIndexes(EasyMock.anyObject(Context.class), EasyMock.anyObject(Vector.class), EasyMock.anyObject(String.class));
		PowerMock.replayAll();
		rmsStore.saveDelegate();
		PowerMock.verifyAll();
	}
	
	@Test
	public void testPutDelegate()
	{
		PowerMock.resetAll();
		
		rmsStore.isCrumb = false;
		TnPersistentManager persisManager = PowerMock.createMock(TnPersistentManager.class);
		IAndroidPersistentContext persisContext = PowerMock.createMock(IAndroidPersistentContext.class);
		Context context = PowerMock.createMock(Context.class);
		
		PowerMock.mockStatic(TnPersistentManager.class);
		EasyMock.expect(TnPersistentManager.getInstance()).andReturn(persisManager).times(2);
		EasyMock.expect(persisManager.getPersistentContext()).andReturn(persisContext).times(2);
		EasyMock.expect(persisContext.getContext()).andReturn(context).times(1);
		EasyMock.expect(persisContext.getApplicationName()).andReturn("appName").times(1);
		
		PowerMock.mockStatic(AndroidFilePersistentUtil.class);
		AndroidFilePersistentUtil.saveData(EasyMock.anyObject(Context.class), EasyMock.anyObject(String.class), EasyMock.anyObject(byte[].class));
		PowerMock.replayAll();
		rmsStore.putDelegate("id", new byte[20]);
		PowerMock.verifyAll();
	}
	
	@Test
	public void testRemoveDelegate()
	{
		rmsStore.isCrumb = true;
		rmsStore.removeDelegate("id");
		
		PowerMock.resetAll();
		rmsStore.isCrumb = false;
		TnPersistentManager persisManager = PowerMock.createMock(TnPersistentManager.class);
		IAndroidPersistentContext persisContext = PowerMock.createMock(IAndroidPersistentContext.class);
		Context context = PowerMock.createMock(Context.class);
		
		PowerMock.mockStatic(TnPersistentManager.class);
		EasyMock.expect(TnPersistentManager.getInstance()).andReturn(persisManager).times(2);
		EasyMock.expect(persisManager.getPersistentContext()).andReturn(persisContext).times(2);
		EasyMock.expect(persisContext.getContext()).andReturn(context).times(1);
		EasyMock.expect(persisContext.getApplicationName()).andReturn("appName").times(1);
		
		PowerMock.mockStatic(AndroidFilePersistentUtil.class);
		AndroidFilePersistentUtil.deleteData(EasyMock.anyObject(Context.class), EasyMock.anyObject(String.class));
		PowerMock.replayAll();
		rmsStore.removeDelegate("id");
		PowerMock.verifyAll();
	}
	
}
