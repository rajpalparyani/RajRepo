package com.telenav.persistent.android;

import static org.junit.Assert.assertEquals;

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
import com.telenav.persistent.TnStore;
import com.telenav.persistent.TnStoreMock;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

@RunWith(PowerMockRunner.class)
@PrepareForTest({AndroidDatabaseStore.class, TnPersistentManager.class, AndroidDatabasePersistentUtil.class, AndroidFilePersistentUtil.class})
public class AndroidDatabaseStoreTest 
{
//	private AndroidDatabaseStore store;
//	private TnPersistentManager manager;
//	private IAndroidDatabasePersistentContext context;
//	private SQLiteDatabase  db;
//	
//	@Before
//	public void setUp() throws Exception
//	{
//		store = new AndroidDatabaseStore("name", 1);
//		manager = PowerMock.createMock(TnPersistentManager.class);
//		context = PowerMock.createMock(IAndroidDatabasePersistentContext.class);
//		db = PowerMock.createMock(SQLiteDatabase .class);
//	}
//	
//	@Test
//	public void testClearDelegate()
//	{
//		PowerMock.mockStatic(TnPersistentManager.class);
//		EasyMock.expect(TnPersistentManager.getInstance()).andReturn(manager).times(3);
//
//		EasyMock.expect(manager.getPersistentContext()).andReturn(context).times(3);
//		EasyMock.expect(context.getApplicationSQLiteDatabase()).andReturn(db);
//		Context context2 = PowerMock.createMock(Context.class);
//		EasyMock.expect(context.getContext()).andReturn(context2);
//		EasyMock.expect(context.getApplicationName()).andReturn("appName");
//		
//		PowerMock.mockStatic(AndroidDatabasePersistentUtil.class);
//		AndroidDatabasePersistentUtil.deleteTable(db, "name");
//		
//		PowerMock.mockStatic(AndroidFilePersistentUtil.class);
//		AndroidFilePersistentUtil.deleteData(context2, "appName_name.index");
//		PowerMock.replayAll();
//		store.clearDelegate();
//		PowerMock.verifyAll();
//	}
//	
//	@Test
//	public void testGetDelegate()
//	{
//		PowerMock.resetAll();
//		PowerMock.mockStatic(TnPersistentManager.class);
//		EasyMock.expect(TnPersistentManager.getInstance()).andReturn(manager);
//		EasyMock.expect(manager.getPersistentContext()).andReturn(context);
//		EasyMock.expect(context.getApplicationSQLiteDatabase()).andReturn(db);
//		
//		PowerMock.mockStatic(AndroidDatabasePersistentUtil.class);
//		EasyMock.expect(AndroidDatabasePersistentUtil.readData(db, "name", "id")).andReturn(new byte[10]);
//		PowerMock.replayAll();
//		store.getDelegate("id");
//		PowerMock.verifyAll();
//	}
//	
//	@Test
//	public void testLoadDelegate()
//	{
//		PowerMock.mockStatic(TnPersistentManager.class);
//		EasyMock.expect(TnPersistentManager.getInstance()).andReturn(manager).times(3);
//
//		EasyMock.expect(manager.getPersistentContext()).andReturn(context).times(3);
//		EasyMock.expect(context.getApplicationSQLiteDatabase()).andReturn(db);
//		Context context2 = PowerMock.createMock(Context.class);
//		EasyMock.expect(context.getContext()).andReturn(context2);
//		EasyMock.expect(context.getApplicationName()).andReturn("appName");
//		
//		PowerMock.mockStatic(AndroidDatabasePersistentUtil.class);
//		AndroidDatabasePersistentUtil.createTable(db, "name");
//		
//		PowerMock.mockStatic(AndroidFilePersistentUtil.class);
//		EasyMock.expect(AndroidFilePersistentUtil.readIndexes(context2, "appName_name.index")).andReturn(new Vector());
//		PowerMock.replayAll();
//		store.loadDelegate();
//		PowerMock.verifyAll();
//	}
//	
//	@Test
//	public void testSaveDelegate()
//	{
//		PowerMock.mockStatic(TnPersistentManager.class);
//		EasyMock.expect(TnPersistentManager.getInstance()).andReturn(manager).times(2);
//		
//		EasyMock.expect(manager.getPersistentContext()).andReturn(context).times(2);
//		Context context2 = PowerMock.createMock(Context.class);
//		EasyMock.expect(context.getContext()).andReturn(context2);
//		EasyMock.expect(context.getApplicationName()).andReturn("appName");
//		
//		PowerMock.mockStatic(AndroidFilePersistentUtil.class);
//		AndroidFilePersistentUtil.saveIndexes(EasyMock.anyObject(Context.class), EasyMock.anyObject(Vector.class), EasyMock.anyObject(String.class));
//		PowerMock.replayAll();
//		store.saveDelegate();
//		PowerMock.verifyAll();
//	}
//	
//	@Test
//	public void testPutDelegate()
//	{
//		PowerMock.resetAll();
//		byte[] data = new byte[10];
//		PowerMock.mockStatic(TnPersistentManager.class);
//		EasyMock.expect(TnPersistentManager.getInstance()).andReturn(manager);
//		EasyMock.expect(manager.getPersistentContext()).andReturn(context);
//		EasyMock.expect(context.getApplicationSQLiteDatabase()).andReturn(db);
//		
//		PowerMock.mockStatic(AndroidDatabasePersistentUtil.class);
//		AndroidDatabasePersistentUtil.saveData(db, "name", "id", data);
//		PowerMock.replayAll();
//		store.putDelegate("id", data);
//		PowerMock.verifyAll();
//	}
//	
//	@Test
//	public void testRemoveDelegate()
//	{
//		PowerMock.resetAll();
//		byte[] data = new byte[10];
//		PowerMock.mockStatic(TnPersistentManager.class);
//		EasyMock.expect(TnPersistentManager.getInstance()).andReturn(manager);
//		EasyMock.expect(manager.getPersistentContext()).andReturn(context);
//		EasyMock.expect(context.getApplicationSQLiteDatabase()).andReturn(db);
//		
//		PowerMock.mockStatic(AndroidDatabasePersistentUtil.class);
//		AndroidDatabasePersistentUtil.deleteData(db, "name", "id");
//		PowerMock.replayAll();
//		store.removeDelegate("id");
//		PowerMock.verifyAll();
//	}
//	
//	@Test
//	public void testBaseClassGetter()
//	{
//		store = new AndroidDatabaseStore("name", 1);
//		assertEquals(store.getName(),"name");
//		assertEquals(store.getType(),1);
//		
//	}
//	
//	@Test(expected=IllegalStateException.class)
//	public void testBaseClassSave()
//	{
//		store = new AndroidDatabaseStore("name", 1);
//		store.save();	
//	}
//	
//	@Test(expected=IllegalStateException.class)
//	public void testBaseClassPut()
//	{
//		store = new AndroidDatabaseStore("name", 1);
//		store.put("id", new byte[20]);	
//	}
//	
//	@Test(expected=IllegalStateException.class)
//	public void testBaseClassGet_Exception()
//	{
//		store = new AndroidDatabaseStore("name", 1);
//		store.get(20);	
//	}
//	
//	@Test(expected=IllegalStateException.class)
//	public void testBaseClassRemove_Exception()
//	{
//		store = new AndroidDatabaseStore("name", 1);
//		store.remove("id");	
//	}
//	
//	@Test(expected=IllegalStateException.class)
//	public void testBaseClassClear_Exception()
//	{
//		store = new AndroidDatabaseStore("name", 1);
//		store.clear();	
//	}
//	
//	
//	@Test(expected=IllegalStateException.class)
//	public void testBaseClassSize_Exception()
//	{
//		store = new AndroidDatabaseStore("name", 1);
//		store.size();	
//	}
//
//	@Test(expected=IllegalStateException.class)
//	public void testBaseClassKeys_Exception()
//	{
//		store = new AndroidDatabaseStore("name", 1);
//		store.keys();	
//	}
	
	
}
