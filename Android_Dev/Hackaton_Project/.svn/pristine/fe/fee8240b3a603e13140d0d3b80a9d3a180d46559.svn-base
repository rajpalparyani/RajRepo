package com.telenav.persistent.android;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;
import android.os.StatFs;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;


@RunWith(PowerMockRunner.class)
@PrepareForTest(AndroidFileConnection.class)
public class AndroidFileConnectionTest
{
	private File file;
	private AndroidFileConnection conn;
	
	@Before
	public void setUp() throws Exception 
	{
		file = PowerMock.createMock(File.class);
		conn = new AndroidFileConnection(file);
	}
	
	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception 
	{
		
	}
	
	@Test
	public void testAvailableSize() throws Exception
	{
		EasyMock.expect(file.getPath()).andReturn("path");
		StatFs statFs = PowerMock.createMock(StatFs.class);
		PowerMock.expectNew(StatFs.class, "path").andReturn(statFs);
		EasyMock.expect(statFs.getAvailableBlocks()).andReturn(1);
		EasyMock.expect(statFs.getBlockSize()).andReturn(1);
		PowerMock.replayAll();
		conn.availableSize();
		PowerMock.verifyAll();
	}
	
	@Test
	public void testCanRead()
	{
		PowerMock.resetAll();
		EasyMock.expect(file.canRead()).andReturn(true);
		PowerMock.replayAll();
		conn.canRead();
		PowerMock.verifyAll();
	}
	
	@Test
	public void testCanWrite()
	{
		PowerMock.resetAll();
		EasyMock.expect(file.canWrite()).andReturn(true);
		PowerMock.replayAll();
		conn.canWrite();
		PowerMock.verifyAll();
	}
	
	@Test
	public void testCreate_Normal() throws Exception
	{
		PowerMock.resetAll();
		EasyMock.expect(file.createNewFile()).andReturn(true);
		PowerMock.replayAll();
		conn.create();
		PowerMock.verifyAll();		
	}
	
	@Test(expected=IOException.class)
	public void testCreate_Exception() throws Exception
	{
		PowerMock.resetAll();
		EasyMock.expect(file.createNewFile()).andReturn(false);
		PowerMock.replayAll();
		conn.create();
		PowerMock.verifyAll();		
	}
	
	@Test
	public void testDelete_Normal() throws Exception
	{
		PowerMock.resetAll();
		EasyMock.expect(file.delete()).andReturn(true);
		PowerMock.replayAll();
		conn.delete();
		PowerMock.verifyAll();
	}
	
	@Test(expected=IOException.class)
	public void testDelete_Exception() throws Exception
	{
		PowerMock.resetAll();
		EasyMock.expect(file.delete()).andReturn(false);
		PowerMock.replayAll();
		conn.delete();
		PowerMock.verifyAll();
	}
	
	@Test
	public void testExists()
	{
		PowerMock.resetAll();
		EasyMock.expect(file.exists()).andReturn(true);
		PowerMock.replayAll();
		conn.exists();
		PowerMock.verifyAll();
	}
	
	@Test
	public void testFileSize() throws Exception
	{
		PowerMock.resetAll();
		EasyMock.expect(file.length()).andReturn((long)20);
		PowerMock.replayAll();
		conn.fileSize();
		PowerMock.verifyAll();
	}
	
	@Test
	public void testGetName()
	{
		PowerMock.resetAll();
		EasyMock.expect(file.getName()).andReturn("fileName");
		PowerMock.replayAll();
		conn.getName();
		PowerMock.verifyAll();
	}
	
	@Test
	public void testGetPath()
	{
		PowerMock.resetAll();
		EasyMock.expect(file.getPath()).andReturn("path");
		PowerMock.replayAll();
		conn.getPath();
		PowerMock.verifyAll();
	}
	
	@Test
	public void testGetURL() throws Exception
	{
		PowerMock.resetAll();
		URL url = PowerMock.createMock(URL.class);
		EasyMock.expect(file.toURL()).andReturn(url);
		PowerMock.replayAll();
		conn.getURL();
		PowerMock.verifyAll();
	}
	
	@Test
	public void testIsDirectory()
	{
		PowerMock.resetAll();
		EasyMock.expect(file.isDirectory()).andReturn(true);
		PowerMock.replayAll();
		conn.isDirectory();
		PowerMock.verifyAll();
	}
	
	@Test
	public void testIsHidden()
	{
		PowerMock.resetAll();
		EasyMock.expect(file.isHidden()).andReturn(true);
		PowerMock.replayAll();
		conn.isHidden();
		PowerMock.verifyAll();
	}
	
	@Test
	public void testLastModified()
	{
		PowerMock.resetAll();
		EasyMock.expect(file.lastModified()).andReturn((long)20);
		PowerMock.replayAll();
		conn.lastModified();
		PowerMock.verifyAll();
	}
	
	@Test
	public void testList() throws Exception
	{
		PowerMock.resetAll();
		EasyMock.expect(file.list()).andReturn(new String[20]);
		PowerMock.replayAll();
		conn.list();
		PowerMock.verifyAll();
	}
	
	@Test
	public void testMkdir() throws Exception
	{
		PowerMock.resetAll();
		EasyMock.expect(file.mkdir()).andReturn(true);
		PowerMock.replayAll();
		conn.mkdir();
		PowerMock.verifyAll();
	}
	
	@Test 
	public void testOpenInputStream() throws Exception
	{
		PowerMock.resetAll();
		FileInputStream stream = PowerMock.createMock(FileInputStream.class);
		PowerMock.expectNew(FileInputStream.class, file).andReturn(stream);
		PowerMock.replayAll();
		conn.openInputStream();
		PowerMock.verifyAll();
	}

	@Test 
	public void testOpenOutputStream() throws Exception
	{
		PowerMock.resetAll();
		FileOutputStream stream = PowerMock.createMock(FileOutputStream.class);
		PowerMock.expectNew(FileOutputStream.class, file).andReturn(stream);
		PowerMock.replayAll();
		conn.openOutputStream();
		PowerMock.verifyAll();
	}
	
	@Test
	public void testRename() throws Exception
	{
		PowerMock.resetAll();
		File file2 = PowerMock.createMock(File.class);
		File file3 = PowerMock.createMock(File.class);
		
		EasyMock.expect(file.getParentFile()).andReturn(file2);
		PowerMock.expectNew(File.class, file2,"newName").andReturn(file3);
		EasyMock.expect(file.renameTo(file3)).andReturn(true);
		PowerMock.replayAll();
		conn.rename("newName");
		PowerMock.verifyAll();
		
	}
	
	@Test(expected=IOException.class)
	public void testRename_Exception() throws Exception
	{
		PowerMock.resetAll();
		File file2 = PowerMock.createMock(File.class);
		File file3 = PowerMock.createMock(File.class);
		
		EasyMock.expect(file.getParentFile()).andReturn(file2);
		PowerMock.expectNew(File.class, file2,"newName").andReturn(file3);
		EasyMock.expect(file.renameTo(file3)).andReturn(false);
		PowerMock.replayAll();
		conn.rename("newName");
		PowerMock.verifyAll();
		
	}
	
	@Test
	public void testSetReadable() throws Exception
	{
		PowerMock.resetAll();
		EasyMock.expect(file.setReadOnly()).andReturn(true);
		PowerMock.replayAll();
		conn.setReadable();
		PowerMock.verifyAll();
	}
	
	@Test(expected=IOException.class)
	public void testSetReadable_Exception() throws Exception
	{
		PowerMock.resetAll();
		EasyMock.expect(file.setReadOnly()).andReturn(false);
		PowerMock.replayAll();
		conn.setReadable();
		PowerMock.verifyAll();
	}
	
	@Test
	public void testTotalSize() throws Exception
	{
		PowerMock.resetAll();
		EasyMock.expect(file.getPath()).andReturn("path");
		StatFs statfs = PowerMock.createMock(StatFs.class);
		PowerMock.expectNew(StatFs.class, "path").andReturn(statfs);
		EasyMock.expect(statfs.getBlockCount()).andReturn(20);
		EasyMock.expect(statfs.getBlockSize()).andReturn(20);
		PowerMock.replayAll();
		conn.totalSize();
		PowerMock.verifyAll();
	}
	
	@Test
	public void testClose() throws Exception
	{
		conn.close();
	}
}
