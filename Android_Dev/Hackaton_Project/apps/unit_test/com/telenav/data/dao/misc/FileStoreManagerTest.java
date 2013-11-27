package com.telenav.data.dao.misc;

import java.io.IOException;

import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.telenav.persistent.ITnPersistentContext;
import com.telenav.persistent.TnFileConnection;
import com.telenav.persistent.TnPersistentManager;
import com.telenav.ui.citizen.map.GlMapComponent;

/**
 *@author gbwang
 *@date 2011-6-24
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({TnPersistentManager.class, ITnPersistentContext.class})
public class FileStoreManagerTest extends TestCase
{
	TnPersistentManager manager;
	TnFileConnection directory;
	
	protected void setUp() throws Exception
	{
		PowerMock.mockStatic(TnPersistentManager.class); 
		manager = PowerMock.createMock(TnPersistentManager.class);
		EasyMock.expect(TnPersistentManager.getInstance()).andReturn(manager).anyTimes();
		
		directory = PowerMock.createMock(TnFileConnection.class);	
		
		super.setUp();
	}
	
	public void testClearFileSystem()
	{
		EasyMock.expect(directory.exists()).andReturn(false).anyTimes();
		
		try
		{
//			directory.delete();
			EasyMock.expect(manager.openFileConnection("telenav70", "", TnPersistentManager.FILE_SYSTEM_EXTERNAL)).andReturn(directory).anyTimes();
		} catch (IOException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		PowerMock.replayAll();
		
		FileStoreManager.clearFileSystem(TnPersistentManager.FILE_SYSTEM_EXTERNAL);
		
		PowerMock.verifyAll();
		
	}
	
	public void testCreateMapCacheFolder_A()
	{
		PowerMock.mockStatic(ITnPersistentContext.class);
		ITnPersistentContext iTnPersistentContext = PowerMock.createMock(ITnPersistentContext.class);
		EasyMock.expect(manager.getPersistentContext()).andReturn(iTnPersistentContext).anyTimes();
		EasyMock.expect(iTnPersistentContext.getApplicationName()).andReturn("TELENAV_7.1.0.9999").anyTimes();
		
		EasyMock.expect(directory.exists()).andReturn(false);
		
		try
		{
			EasyMock.expect(directory.mkdirs()).andReturn(true);
			EasyMock.expect(manager.openFileConnection("/TELENAV_7.1.0.9999_opengl_map_cache/", null, 
                    TnPersistentManager.FILE_SYSTEM_INTERNAL)).andReturn(directory).anyTimes();
		} catch (IOException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		PowerMock.replayAll();
		
		FileStoreManager.createMapCacheFolder();
		
		PowerMock.verifyAll();
	}
	
	public void testCreateMapCacheFolder_B()
	{
		PowerMock.mockStatic(ITnPersistentContext.class);
		ITnPersistentContext iTnPersistentContext = PowerMock.createMock(ITnPersistentContext.class);
		EasyMock.expect(manager.getPersistentContext()).andReturn(iTnPersistentContext).anyTimes();
		EasyMock.expect(iTnPersistentContext.getApplicationName()).andReturn("TELENAV_7.1.0.9999").anyTimes();
		
		EasyMock.expect(directory.exists()).andReturn(true);
		
		try
		{
			EasyMock.expect(manager.openFileConnection("/TELENAV_7.1.0.9999_opengl_map_cache/", null, 
                    TnPersistentManager.FILE_SYSTEM_INTERNAL)).andReturn(directory).anyTimes();
		} catch (IOException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		PowerMock.replayAll();
		
		FileStoreManager.createMapCacheFolder();
		
		PowerMock.verifyAll();
	}
	
	public void testCreateMapCacheFolder_C()
	{
		PowerMock.mockStatic(ITnPersistentContext.class);
		ITnPersistentContext iTnPersistentContext = PowerMock.createMock(ITnPersistentContext.class);
		EasyMock.expect(manager.getPersistentContext()).andReturn(iTnPersistentContext).anyTimes();
		EasyMock.expect(iTnPersistentContext.getApplicationName()).andReturn("TELENAV_7.1.0.9999").anyTimes();
		
		try
		{
			EasyMock.expect(manager.openFileConnection("/TELENAV_7.1.0.9999_opengl_map_cache/", null, 
                    TnPersistentManager.FILE_SYSTEM_INTERNAL)).andReturn(null).anyTimes();
		} catch (IOException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		PowerMock.replayAll();
		
		FileStoreManager.createMapCacheFolder();
		
		PowerMock.verifyAll();
	}
	
	public void testClearMapCache()
	{
		PowerMock.mockStatic(ITnPersistentContext.class);
		ITnPersistentContext iTnPersistentContext = PowerMock.createMock(ITnPersistentContext.class);
		EasyMock.expect(manager.getPersistentContext()).andReturn(iTnPersistentContext).anyTimes();
		EasyMock.expect(iTnPersistentContext.getApplicationName()).andReturn("TELENAV_7.1.0.9999").anyTimes();
		
		EasyMock.expect(directory.exists()).andReturn(false).anyTimes();
		
		try
		{
//			directory.delete();
			EasyMock.expect(manager.openFileConnection("/TELENAV_7.1.0.9999_opengl_map_cache/", "", 
                    TnPersistentManager.FILE_SYSTEM_INTERNAL)).andReturn(directory).anyTimes();
		} catch (IOException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		PowerMock.replayAll();
		
		FileStoreManager.clearMapCache();
		
		PowerMock.verifyAll();
		
	}
	
	public void testClearOpenglResource()
	{
		PowerMock.mockStatic(ITnPersistentContext.class);
		ITnPersistentContext iTnPersistentContext = PowerMock.createMock(ITnPersistentContext.class);
		EasyMock.expect(manager.getPersistentContext()).andReturn(iTnPersistentContext).anyTimes();
		EasyMock.expect(iTnPersistentContext.getApplicationName()).andReturn("TELENAV_7.1.0.9999").anyTimes();
		
		EasyMock.expect(directory.exists()).andReturn(false).anyTimes();
		
		try
		{
//			directory.delete();
			EasyMock.expect(manager.openFileConnection("/TELENAV_7.1.0.9999_opengl_map_resource/", "", 
                    TnPersistentManager.FILE_SYSTEM_INTERNAL)).andReturn(directory).anyTimes();
		} catch (IOException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		PowerMock.replayAll();
		
		FileStoreManager.clearOpenglResource();
		
		PowerMock.verifyAll();
	}
	
	public void testDelDirectory_directoryNotExist()
	{
		EasyMock.expect(directory.exists()).andReturn(false).anyTimes();
		
		try
		{
//			directory.delete();
			EasyMock.expect(manager.openFileConnection("telenav", "", TnPersistentManager.FILE_SYSTEM_EXTERNAL)).andReturn(directory).times(2);
		} catch (IOException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		PowerMock.replayAll();
		
		try
		{
			FileStoreManager.delDirectory("telenav", TnPersistentManager.FILE_SYSTEM_EXTERNAL);
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		PowerMock.verifyAll();
	}
	
	public void testDelFilesInDirectory_A()
	{
		TnFileConnection tn70Directory = PowerMock.createMock(TnFileConnection.class);
		TnFileConnection tn701Directory = PowerMock.createMock(TnFileConnection.class);	
		String[] tempList = {"tn70", "tn701"};

		try
		{						
			EasyMock.expect(manager.openFileConnection("telenav", "", TnPersistentManager.FILE_SYSTEM_EXTERNAL)).andReturn(directory);
			EasyMock.expect(directory.exists()).andReturn(true);
			EasyMock.expect(directory.isDirectory()).andReturn(true);
			EasyMock.expect(directory.list()).andReturn(tempList);	
			
			EasyMock.expect(manager.openFileConnection("telenav",tempList[0], TnPersistentManager.FILE_SYSTEM_EXTERNAL)).andReturn(tn70Directory);
			EasyMock.expect(tn70Directory.isDirectory()).andReturn(false);
			tn70Directory.delete();
			
			EasyMock.expect(manager.openFileConnection("telenav",tempList[1], TnPersistentManager.FILE_SYSTEM_EXTERNAL)).andReturn(tn701Directory);
			EasyMock.expect(tn701Directory.isDirectory()).andReturn(false);
			tn701Directory.delete();
			
		} catch (IOException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		PowerMock.replayAll();
		
		try
		{
			FileStoreManager.delFilesInDirectory("telenav", TnPersistentManager.FILE_SYSTEM_EXTERNAL);
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		PowerMock.verifyAll();
	}
	
	public void testDelFilesInDirectory_B()
	{
		TnFileConnection tn701Directory = PowerMock.createMock(TnFileConnection.class);
		TnFileConnection tn701ChildDirectory = PowerMock.createMock(TnFileConnection.class);	
		String[] tempList = {"tn701"};

		try
		{						
			EasyMock.expect(manager.openFileConnection("telenav", "", TnPersistentManager.FILE_SYSTEM_EXTERNAL)).andReturn(directory);
			EasyMock.expect(directory.exists()).andReturn(true);
			EasyMock.expect(directory.isDirectory()).andReturn(true);
			EasyMock.expect(directory.list()).andReturn(tempList);	
			
			EasyMock.expect(manager.openFileConnection("telenav",tempList[0], TnPersistentManager.FILE_SYSTEM_EXTERNAL)).andReturn(tn701Directory);
			EasyMock.expect(tn701Directory.isDirectory()).andReturn(true);
			
			EasyMock.expect(manager.openFileConnection("telenav" + "/" + tempList[0],"", TnPersistentManager.FILE_SYSTEM_EXTERNAL)).andReturn(tn701ChildDirectory).times(2);
			EasyMock.expect(tn701ChildDirectory.exists()).andReturn(true).anyTimes();
			EasyMock.expect(tn701ChildDirectory.isDirectory()).andReturn(false);
			tn701ChildDirectory.delete();
			
		} catch (IOException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		PowerMock.replayAll();
		
		try
		{
			FileStoreManager.delFilesInDirectory("telenav", TnPersistentManager.FILE_SYSTEM_EXTERNAL);
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		PowerMock.verifyAll();
	}

}
