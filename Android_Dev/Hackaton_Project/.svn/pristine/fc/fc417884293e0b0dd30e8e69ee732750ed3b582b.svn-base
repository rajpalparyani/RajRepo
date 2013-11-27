package com.telenav.persistent.android;

import static org.junit.Assert.*;
import java.io.File;
import java.io.IOException;
import java.util.Vector;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Environment;

import com.telenav.logger.Logger;
import com.telenav.persistent.ITnExternalStorageListener;
import com.telenav.persistent.ITnPersistentContext;
import com.telenav.persistent.TnPersistentManager;

@RunWith(PowerMockRunner.class)
@PrepareForTest({AndroidPersistentManager.class, Logger.class, Environment.class, Activity.class, BroadcastReceiver.class, IntentFilter.class})
public class AndroidPersistentManagerTest 
{
	private Context context;
	private AndroidPersistentManager manager;
	private IAndroidPersistentContext persistentContext;
	private TnPersistentManager persistentMngr;
	
	@Before
	public void setUp() throws Exception
	{
		context = PowerMock.createMock(Context.class);
		manager = new AndroidPersistentManager();
		persistentContext = PowerMock.createMock(IAndroidPersistentContext.class);
		persistentMngr = PowerMock.createMock(TnPersistentManager.class);
		TnPersistentManager.init(persistentMngr, persistentContext);
		PowerMock.replayAll();
	}
	
	@Test
	public void createStoreDelegate() throws Exception
	{
		PowerMock.resetAll();
		AndroidDatabaseStore stroe = PowerMock.createMock(AndroidDatabaseStore.class);
		PowerMock.expectNew(AndroidDatabaseStore.class, "name", TnPersistentManager.DATABASE).andReturn(stroe);
		PowerMock.replayAll();
		manager.createStore(TnPersistentManager.DATABASE, "name");
		PowerMock.verifyAll();
		
		PowerMock.resetAll();
		AndroidRmsStore rmsStroe = PowerMock.createMock(AndroidRmsStore.class);
		PowerMock.expectNew(AndroidRmsStore.class, "name", false, TnPersistentManager.RMS_CHUNK).andReturn(rmsStroe);
		PowerMock.replayAll();
		manager.createStore(TnPersistentManager.RMS_CHUNK, "name");
		PowerMock.verifyAll();
		
		PowerMock.resetAll();
		PowerMock.expectNew(AndroidRmsStore.class, "name", true, TnPersistentManager.RMS_CRUMB).andReturn(rmsStroe);
		PowerMock.replayAll();
		manager.createStore(TnPersistentManager.RMS_CRUMB, "name");
		PowerMock.verifyAll();
	}
	
	@Test(expected=IOException.class)
	public void testOpenFileConnection_FileIsNull() throws Exception
	{
		PowerMock.resetAll();
		//File rootStorage = PowerMock.createMock(File.class);
		PowerMock.mockStatic(Environment.class);
		EasyMock.expect(Environment.getExternalStorageDirectory()).andReturn(null);
		PowerMock.replayAll();
		manager.openFileConnection("directory", "fileName", 0);
		PowerMock.verifyAll();
		
	}
	
	@Test
	public void testOpenFileConnection_NameIsNull() throws Exception
	{
		PowerMock.resetAll();
		File rootStorage = PowerMock.createMock(File.class);
		File file = PowerMock.createMock(File.class);
		PowerMock.mockStatic(Environment.class);
		EasyMock.expect(Environment.getExternalStorageDirectory()).andReturn(rootStorage);
		EasyMock.expect(rootStorage.getAbsolutePath()).andReturn("path");
		PowerMock.expectNew(File.class, "path/directory").andReturn(file);
		AndroidFileConnection conn = PowerMock.createMock(AndroidFileConnection.class);
		PowerMock.expectNew(AndroidFileConnection.class, file).andReturn(conn);
		
		PowerMock.replayAll();
		manager.openFileConnection("directory", "", 0);
		PowerMock.verifyAll();
		
	}
	
	@Test
	public void testOpenFileConnection() throws Exception
	{
		PowerMock.resetAll();
		File rootStorage = PowerMock.createMock(File.class);
		File file = PowerMock.createMock(File.class);
		PowerMock.mockStatic(Environment.class);
		EasyMock.expect(Environment.getExternalStorageDirectory()).andReturn(rootStorage);
		EasyMock.expect(rootStorage.getAbsolutePath()).andReturn("path");
		PowerMock.expectNew(File.class, "path/directory", "fileName").andReturn(file);
		AndroidFileConnection conn = PowerMock.createMock(AndroidFileConnection.class);
		PowerMock.expectNew(AndroidFileConnection.class, file).andReturn(conn);
		
		PowerMock.replayAll();
		manager.openFileConnection("directory", "fileName", 0);
		PowerMock.verifyAll();
		
	}
	
	@Test
	public void testOpenFileConnection_Internal() throws Exception
	{
		PowerMock.resetAll();
		
		IAndroidPersistentContext ctxt = PowerMock.createMock(IAndroidPersistentContext.class);
		//Context con = PowerMock.createMock(Context.class);

		Whitebox.setInternalState(TnPersistentManager.class, "persistentContext", ctxt);
		EasyMock.expect(ctxt.getContext()).andReturn(context);
		File file = PowerMock.createMock(File.class);
		EasyMock.expect(context.getFilesDir()).andReturn(file);
		EasyMock.expect(file.getAbsolutePath()).andReturn("absPath");		
		File rootStorage = PowerMock.createMock(File.class);
		PowerMock.expectNew(File.class, "absPath").andReturn(rootStorage);
		EasyMock.expect(rootStorage.getAbsolutePath()).andReturn("path");
		File file2 = PowerMock.createMock(File.class);
		PowerMock.expectNew(File.class, "path/directory", "fileName").andReturn(file2);

		PowerMock.replayAll();
		manager.openFileConnection("directory", "fileName", 1);
		PowerMock.verifyAll();
		
	}
	
	@Test
	public void testGetExternalStorageState_RW() throws Exception
	{
		PowerMock.resetAll();
		PowerMock.mockStatic(Environment.class);
		EasyMock.expect(Environment.getExternalStorageState()).andReturn(Environment.MEDIA_MOUNTED);
		PowerMock.replayAll();
		int result = manager.getExternalStorageState();
		PowerMock.verifyAll();
		assertEquals(result, 0);
		
	}
	
	@Test
	public void testGetExternalStorageState_R() throws Exception
	{
		PowerMock.resetAll();
		PowerMock.mockStatic(Environment.class);
		EasyMock.expect(Environment.getExternalStorageState()).andReturn(Environment.MEDIA_MOUNTED_READ_ONLY);
		PowerMock.replayAll();
		int result = manager.getExternalStorageState();
		PowerMock.verifyAll();
		assertEquals(result, 1);
	}
	
	@Test
	public void testBaseClass()
	{
		PowerMock.resetAll();
		manager = new AndroidPersistentManager();
		assertNotNull(manager.getInstance());

		ITnPersistentContext ctxt = PowerMock.createMock(ITnPersistentContext.class);
		TnPersistentManager  mngr = PowerMock.createMock(TnPersistentManager.class);
		manager.init(mngr, ctxt);

	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testCreateStore_Exception1()
	{
		manager.createStore(-1, "name");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testCreateStore_Exception2()
	{
		manager.createStore(3, "name");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testCreateStore_Exception3()
	{
		manager.createStore(1, null);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testCreateStore_Exception4()
	{
		manager.createStore(1, "");
	}
	
	
	@Test
	public void testRemoveAllExternalFileListeners()
	{
		PowerMock.resetAll();

		IAndroidPersistentContext persistentContext = PowerMock.createMock(IAndroidPersistentContext.class);
		Whitebox.setInternalState(TnPersistentManager.class, "persistentContext", persistentContext);
		
		BroadcastReceiver mExternalStorageReceiver = PowerMock.createMock(BroadcastReceiver.class);
		AndroidPersistentManager.mExternalStorageReceiver = mExternalStorageReceiver;
		EasyMock.expect(persistentContext.getContext()).andReturn(context);
		context.unregisterReceiver(mExternalStorageReceiver);
		PowerMock.replayAll();
		manager.removeAllExternalFileListeners();
		PowerMock.verifyAll();
	}
	
	@Test
	public void testRemoveAllExternalFileListeners_Exception()
	{
		PowerMock.resetAll();
		
		IAndroidPersistentContext persistentContext = PowerMock.createMock(IAndroidPersistentContext.class);
		Whitebox.setInternalState(TnPersistentManager.class, "persistentContext", persistentContext);
		
		BroadcastReceiver mExternalStorageReceiver = PowerMock.createMock(BroadcastReceiver.class);
		AndroidPersistentManager.mExternalStorageReceiver = mExternalStorageReceiver;
		EasyMock.expect(persistentContext.getContext()).andReturn(context);
		context.unregisterReceiver(mExternalStorageReceiver);
		RuntimeException e = new RuntimeException();
		EasyMock.expectLastCall().andThrow(e);
		PowerMock.mockStatic(Logger.class);
		Logger.log(EasyMock.anyObject(String.class), EasyMock.anyObject(Throwable.class));
		PowerMock.replayAll();
		manager.removeAllExternalFileListeners();
		PowerMock.verifyAll();
	}
	
	@Test
	public void testRemoveAllExternalFileListeners_Activity()
	{
		PowerMock.resetAll();

		IAndroidPersistentContext persistentContext = PowerMock.createMock(IAndroidPersistentContext.class);
		Whitebox.setInternalState(TnPersistentManager.class, "persistentContext", persistentContext);
		
		BroadcastReceiver mExternalStorageReceiver = PowerMock.createMock(BroadcastReceiver.class);
		AndroidPersistentManager.mExternalStorageReceiver = mExternalStorageReceiver;
		Activity ac = PowerMock.createMock(Activity.class);//new Activity();
		EasyMock.expect(persistentContext.getContext()).andReturn(ac);
		//ac.unregisterReceiver(mExternalStorageReceiver);
		ac.runOnUiThread(EasyMock.anyObject(Runnable.class));
		PowerMock.replayAll();
		manager.removeAllExternalFileListeners();
		PowerMock.verifyAll();
	}
	
	@Test
	public void testAddExternalFileListener() throws Exception //NOTE
	{
		PowerMock.resetAll();
		AndroidPersistentManager.mExternalStorageReceiver = null;
		ITnExternalStorageListener listener = PowerMock.createMock(ITnExternalStorageListener.class);
		BroadcastReceiver receiver = PowerMock.createMock(BroadcastReceiver.class);
		IAndroidPersistentContext persistentContext = PowerMock.createMock(IAndroidPersistentContext.class);
		Whitebox.setInternalState(TnPersistentManager.class, "persistentContext", persistentContext);
		
		Class anonymousClass = Class.forName("com.telenav.persistent.android.AndroidPersistentManager$1");
		BroadcastReceiver broadReceiver = PowerMock.createMock(anonymousClass);
		PowerMock.expectNew(anonymousClass).andReturn(broadReceiver);
		
		EasyMock.expect(persistentContext.getContext()).andReturn(context);
		IntentFilter filter = PowerMock.createMock(IntentFilter.class);
		PowerMock.expectNew(IntentFilter.class).andReturn(filter);
		filter.addAction(Intent.ACTION_MEDIA_BAD_REMOVAL);
		filter.addAction(Intent.ACTION_MEDIA_CHECKING);
		filter.addAction(Intent.ACTION_MEDIA_SCANNER_STARTED);
        filter.addAction(Intent.ACTION_MEDIA_MOUNTED);
        filter.addAction(Intent.ACTION_MEDIA_REMOVED);
        filter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
        filter.addAction(Intent.ACTION_MEDIA_EJECT);
        filter.addDataScheme("file");
        
        Intent intent = PowerMock.createMock(Intent.class);
        EasyMock.expect(context.registerReceiver(broadReceiver, filter)).andReturn(intent);
       	
		PowerMock.replayAll();
		manager.addExternalFileListener(listener);
		PowerMock.verifyAll();
	}
	
	@Test
	public void testAddExternalFileListener_Activity() throws Exception //NOTE
	{
		PowerMock.resetAll();
		AndroidPersistentManager.mExternalStorageReceiver = null;
		ITnExternalStorageListener listener = PowerMock.createMock(ITnExternalStorageListener.class);
		BroadcastReceiver receiver = PowerMock.createMock(BroadcastReceiver.class);
		IAndroidPersistentContext persistentContext = PowerMock.createMock(IAndroidPersistentContext.class);
		Whitebox.setInternalState(TnPersistentManager.class, "persistentContext", persistentContext);
		
		Class anonymousClass = Class.forName("com.telenav.persistent.android.AndroidPersistentManager$1");
		BroadcastReceiver broadReceiver = PowerMock.createMock(anonymousClass);
		PowerMock.expectNew(anonymousClass).andReturn(broadReceiver);
		Activity ac = PowerMock.createMock(Activity.class);
		EasyMock.expect(persistentContext.getContext()).andReturn(ac);
		
		Class anonyActivity = Class.forName("com.telenav.persistent.android.AndroidPersistentManager$2");
		Runnable run = PowerMock.createMock(anonyActivity);
		ac.runOnUiThread(EasyMock.anyObject(Runnable.class));
		PowerMock.replayAll();
		manager.addExternalFileListener(listener);
		PowerMock.verifyAll();
	}
	
	@Test
	public void testAnonyRunnable() throws Exception
	{
//		Class anonyActivity = Class.forName("com.telenav.persistent.android.AndroidPersistentManager$2");
//		Constructor[] constructor = anonyActivity.getDeclaredConstructors();
//		constructor[0].setAccessible(true);
//		Runnable runnable = (Runnable)constructor[0].newInstance(manager);
//		IntentFilter filter = PowerMock.createMock(IntentFilter.class);
//		PowerMock.expectNew(IntentFilter.class).andReturn(filter);
//		filter.addAction(Intent.ACTION_MEDIA_BAD_REMOVAL);
//		PowerMock.replayAll();
//		runnable.run();
//		PowerMock.verifyAll();
	}
	
	@Test
	public void testUpdateExternalStorageState()
	{
		PowerMock.resetAll();
		PowerMock.mockStatic(Environment.class);
		EasyMock.expect(Environment.getExternalStorageState()).andReturn(Environment.MEDIA_MOUNTED_READ_ONLY);
		Vector v = new Vector();
		manager.externalListeners = v;
		ITnExternalStorageListener listener = PowerMock.createMock(ITnExternalStorageListener.class);
		v.add(listener);
		listener.updateStorageState(1);
		PowerMock.replayAll();
		manager.updateExternalStorageState();
		PowerMock.verifyAll();
	}
	
	@Test
	public void testUpdateExternalStorageState_Exception()
	{
		PowerMock.resetAll();
		PowerMock.mockStatic(Environment.class);
		EasyMock.expect(Environment.getExternalStorageState()).andReturn(Environment.MEDIA_MOUNTED_READ_ONLY);
		Vector v = new Vector();
		manager.externalListeners = v;
		ITnExternalStorageListener listener = PowerMock.createMock(ITnExternalStorageListener.class);
		v.add(listener);
		listener.updateStorageState(1);
		RuntimeException e = new RuntimeException();
		EasyMock.expectLastCall().andThrow(e);
		PowerMock.mockStatic(Logger.class);
		Logger.log(EasyMock.anyObject(String.class), EasyMock.anyObject(Throwable.class));
		PowerMock.replayAll();
		manager.updateExternalStorageState();
		PowerMock.verifyAll();
	}
	
}
