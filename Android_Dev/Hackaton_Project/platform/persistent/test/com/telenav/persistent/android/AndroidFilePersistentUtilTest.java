package com.telenav.persistent.android;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Vector;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.telenav.logger.Logger;

import android.content.Context;

@RunWith(PowerMockRunner.class)
@PrepareForTest({AndroidFilePersistentUtil.class, Logger.class, File.class})
public class AndroidFilePersistentUtilTest 
{
	
	private Context context;
	
	@Before
	public void setUp() throws Exception
	{
		context = PowerMock.createMock(Context.class);
	}
	
	
	@Test
	public void testMkDir() throws Exception
	{
		
		File f2 = PowerMock.createMock(File.class);
		EasyMock.expect(context.getFilesDir()).andReturn(f2);
		EasyMock.expect(f2.getAbsolutePath()).andReturn("path");
		
		File f = PowerMock.createMock(File.class);
		PowerMock.expectNew(File.class, "path/name").andReturn(f);
		EasyMock.expect(f.exists()).andReturn(true);
		PowerMock.replayAll();
		AndroidFilePersistentUtil.mkDir(context, "name");
		PowerMock.verifyAll();
		
		/**********************************************/
		PowerMock.resetAll();
		EasyMock.expect(f2.getAbsolutePath()).andReturn("path");
		EasyMock.expect(context.getFilesDir()).andReturn(f2);
		PowerMock.expectNew(File.class, "path/name").andReturn(f);
		EasyMock.expect(f.exists()).andReturn(false);
		EasyMock.expect(f.mkdir()).andReturn(false);
		PowerMock.mockStatic(Logger.class);
		Logger.log(EasyMock.anyInt(), EasyMock.anyObject(String.class), EasyMock.anyObject(String.class));
		
		PowerMock.replayAll();
		AndroidFilePersistentUtil.mkDir(context, "name");
		PowerMock.verifyAll();
	}
	
	@Test
	public void testSaveData_NotMkDir() throws Exception
	{
		PowerMock.resetAll();
		
		byte [] data = new byte[20];
		File f2 = PowerMock.createMock(File.class);
		EasyMock.expect(context.getFilesDir()).andReturn(f2).times(2);
		EasyMock.expect(f2.getAbsolutePath()).andReturn("path").times(2);
		
		File f = PowerMock.createMock(File.class);
		PowerMock.expectNew(File.class, "path/name/test").andReturn(f);
		EasyMock.expect(f.exists()).andReturn(false);
		
		PowerMock.expectNew(File.class, "path/name").andReturn(f);
		EasyMock.expect(f.exists()).andReturn(false);
		EasyMock.expect(f.mkdirs()).andReturn(false);
		PowerMock.replayAll();
		AndroidFilePersistentUtil.saveData(context, "name/test", data);
		PowerMock.verifyAll();	
	}
	
	@Test
	public void testSaveData_NotNew() throws Exception
	{
		PowerMock.resetAll();
		
		byte [] data = new byte[20];
		File f2 = PowerMock.createMock(File.class);
		EasyMock.expect(context.getFilesDir()).andReturn(f2).times(2);
		EasyMock.expect(f2.getAbsolutePath()).andReturn("path").times(2);
		
		File f = PowerMock.createMock(File.class);
		PowerMock.expectNew(File.class, "path/name/test").andReturn(f);
		EasyMock.expect(f.exists()).andReturn(false);
		
		PowerMock.expectNew(File.class, "path/name").andReturn(f);
		EasyMock.expect(f.exists()).andReturn(false);
		EasyMock.expect(f.mkdirs()).andReturn(true);
		EasyMock.expect(f.createNewFile()).andReturn(false);
		
		PowerMock.replayAll();
		AndroidFilePersistentUtil.saveData(context, "name/test", data);
		PowerMock.verifyAll();	
	}
	
	@Test
	public void testSaveData() throws Exception
	{
		PowerMock.resetAll();
		
		byte [] data = new byte[20];
		File f2 = PowerMock.createMock(File.class);
		EasyMock.expect(context.getFilesDir()).andReturn(f2).times(2);
		EasyMock.expect(f2.getAbsolutePath()).andReturn("path").times(2);
		
		File f = PowerMock.createMock(File.class);
		PowerMock.expectNew(File.class, "path/name/test").andReturn(f);
		EasyMock.expect(f.exists()).andReturn(false);
		
		PowerMock.expectNew(File.class, "path/name").andReturn(f);
		EasyMock.expect(f.exists()).andReturn(false);
		EasyMock.expect(f.mkdirs()).andReturn(true);
		EasyMock.expect(f.createNewFile()).andReturn(true);

		FileOutputStream stream = PowerMock.createMock(FileOutputStream.class);
		PowerMock.expectNew(FileOutputStream.class, f).andReturn(stream);
		stream.write(data);
		stream.close();
		PowerMock.replayAll();
		AndroidFilePersistentUtil.saveData(context, "name/test", data);
		PowerMock.verifyAll();	
	}

	@Test
	public void testSaveData_WriteException() throws Exception
	{
		PowerMock.resetAll();
		
		byte [] data = new byte[20];
		File f2 = PowerMock.createMock(File.class);
		EasyMock.expect(context.getFilesDir()).andReturn(f2).times(2);
		EasyMock.expect(f2.getAbsolutePath()).andReturn("path").times(2);
		
		File f = PowerMock.createMock(File.class);
		PowerMock.expectNew(File.class, "path/name/test").andReturn(f);
		EasyMock.expect(f.exists()).andReturn(false);
		
		PowerMock.expectNew(File.class, "path/name").andReturn(f);
		EasyMock.expect(f.exists()).andReturn(false);
		EasyMock.expect(f.mkdirs()).andReturn(true);
		EasyMock.expect(f.createNewFile()).andReturn(true);
		
		PowerMock.mockStatic(Logger.class);
		Logger.log(EasyMock.anyObject(String.class), EasyMock.anyObject(IOException.class));
		Logger.log(EasyMock.anyObject(String.class), EasyMock.anyObject(IOException.class));
		FileOutputStream stream = PowerMock.createMock(FileOutputStream.class);
		PowerMock.expectNew(FileOutputStream.class, f).andReturn(stream);
		IOException e = new IOException();
		stream.write(data);
		EasyMock.expectLastCall().andThrow(e);
		stream.close();
		EasyMock.expectLastCall().andThrow(e);
		PowerMock.replayAll();
		AndroidFilePersistentUtil.saveData(context, "name/test", data);
		PowerMock.verifyAll();	
	}
	
	@Test
	public void testReadData() throws Exception
	{
		PowerMock.resetAll();
		File f2 = PowerMock.createMock(File.class);
		EasyMock.expect(context.getFilesDir()).andReturn(f2);
		EasyMock.expect(f2.getAbsolutePath()).andReturn("path");
		
		File f = PowerMock.createMock(File.class);
		PowerMock.expectNew(File.class, "path/name").andReturn(f);
		EasyMock.expect(f.exists()).andReturn(true);
		
		FileInputStream stream = PowerMock.createMock(FileInputStream.class);
		PowerMock.expectNew(FileInputStream.class,f).andReturn(stream);
		EasyMock.expect(f.length()).andReturn(50L);
		EasyMock.expect(stream.read(EasyMock.anyObject(byte[].class))).andReturn(20);
		stream.close();
		PowerMock.replayAll();
		AndroidFilePersistentUtil.readData(context, "name");
		PowerMock.verifyAll();
	}
	
	
	@Test
	public void testReadData_Exception() throws Exception
	{
		PowerMock.resetAll();
		File f2 = PowerMock.createMock(File.class);
		EasyMock.expect(context.getFilesDir()).andReturn(f2);
		EasyMock.expect(f2.getAbsolutePath()).andReturn("path");
		
		File f = PowerMock.createMock(File.class);
		PowerMock.expectNew(File.class, "path/name").andReturn(f);
		EasyMock.expect(f.exists()).andReturn(true);
		
		FileInputStream stream = PowerMock.createMock(FileInputStream.class);
		PowerMock.expectNew(FileInputStream.class,f).andReturn(stream);
		EasyMock.expect(f.length()).andReturn(50l);
		IOException e = new IOException();
		EasyMock.expect(stream.read(EasyMock.anyObject(byte[].class))).andThrow(e);
		stream.close();
		EasyMock.expectLastCall().andThrow(e);
		PowerMock.replayAll();
		AndroidFilePersistentUtil.readData(context, "name");
		PowerMock.verifyAll();
	}
	
	@Test
	public void testDeleteData() throws Exception
	{
		PowerMock.resetAll();
		File f2 = PowerMock.createMock(File.class);
		EasyMock.expect(context.getFilesDir()).andReturn(f2);
		EasyMock.expect(f2.getAbsolutePath()).andReturn("path");
		
		File f = PowerMock.createMock(File.class);
		PowerMock.expectNew(File.class, "path/name").andReturn(f);
		EasyMock.expect(f.exists()).andReturn(true);
		EasyMock.expect(f.delete()).andReturn(false);
		PowerMock.replayAll();
		AndroidFilePersistentUtil.deleteData(context, "name");
		PowerMock.verifyAll();
		
	}
	
	@Test
	public void testSaveIndexed() throws Exception
	{
		PowerMock.resetAll();
		
		Vector v = new Vector();
		v.add("test1");
		v.add("test2");
		v.add("test3");
		
		FileOutputStream stream = PowerMock.createMock(FileOutputStream.class);
		EasyMock.expect(context.openFileOutput("key", Context.MODE_PRIVATE)).andReturn(stream);
		
		stream.write(EasyMock.anyObject(byte[].class));
		stream.close();
		PowerMock.replayAll();
		AndroidFilePersistentUtil.saveIndexes(context, v, "key");
		PowerMock.verifyAll();
	}
	
	@Test
	public void testSaveIndexed_Exception() throws Exception
	{
		PowerMock.resetAll();
		AndroidFilePersistentUtil.saveIndexes(context, null, "key");
		
		PowerMock.resetAll();
		
		Vector v = new Vector();
		v.add("test1");
		v.add("test2");
		v.add("test3");
		
		FileOutputStream stream = PowerMock.createMock(FileOutputStream.class);
		EasyMock.expect(context.openFileOutput("key", Context.MODE_PRIVATE)).andReturn(stream);
		IOException e = new IOException();
		stream.write(EasyMock.anyObject(byte[].class));
		EasyMock.expectLastCall().andThrow(e);
		stream.close();
		EasyMock.expectLastCall().andThrow(e);
		PowerMock.replayAll();
		AndroidFilePersistentUtil.saveIndexes(context, v, "key");
		PowerMock.verifyAll();
	}
	
	@Test
	public void testReadIndexes() throws Exception
	{
		PowerMock.resetAll();
		
		File f = PowerMock.createMock(File.class);
		PowerMock.expectNew(File.class, "path/key").andReturn(f);
		File file = PowerMock.createMock(File.class);
		FileInputStream fis = PowerMock.createMock(FileInputStream.class);
		PowerMock.expectNew(FileInputStream.class, f).andReturn(fis);
		EasyMock.expect(context.getFilesDir()).andReturn(file);
		EasyMock.expect(file.getAbsolutePath()).andReturn("path");
		EasyMock.expect(f.exists()).andReturn(true);
		EasyMock.expect(f.length()).andReturn(50L);
		EasyMock.expect(fis.read(EasyMock.anyObject(byte[].class))).andReturn(20);
		fis.close();
		PowerMock.replayAll();
		AndroidFilePersistentUtil.readIndexes(context, "key");
		PowerMock.verifyAll();
	}
	
	@Test
	public void testReadIndexes_Exception() throws Exception
	{
		PowerMock.resetAll();
		IOException e = new IOException();
		File f = PowerMock.createMock(File.class);
        PowerMock.expectNew(File.class, "path/key").andReturn(f);
        File file = PowerMock.createMock(File.class);
        FileInputStream fis = PowerMock.createMock(FileInputStream.class);
        PowerMock.expectNew(FileInputStream.class, f).andReturn(fis);
        EasyMock.expect(context.getFilesDir()).andReturn(file);
        EasyMock.expect(file.getAbsolutePath()).andReturn("path");
        EasyMock.expect(f.exists()).andReturn(true);
        EasyMock.expect(f.length()).andReturn(50L);
        EasyMock.expect(fis.read(EasyMock.anyObject(byte[].class))).andReturn(20);
        fis.close();
		EasyMock.expectLastCall().andThrow(e);
		PowerMock.replayAll();
		AndroidFilePersistentUtil.readIndexes(context, "key");
		PowerMock.verifyAll();
	}

	@Test
	public void testStoreObject() throws Exception
	{
		PowerMock.resetAll();
		File f2 = PowerMock.createMock(File.class);
		EasyMock.expect(context.getFilesDir()).andReturn(f2);
		EasyMock.expect(f2.getAbsolutePath()).andReturn("path");
		
		File f = PowerMock.createMock(File.class);
		PowerMock.expectNew(File.class, "path/name").andReturn(f);
		FileOutputStream fos = PowerMock.createMock(FileOutputStream.class);
		ObjectOutputStream oos = PowerMock.createMock(ObjectOutputStream.class);
		PowerMock.expectNew(FileOutputStream.class, f).andReturn(fos);
		PowerMock.expectNew(ObjectOutputStream.class , fos).andReturn(oos);
		Object obj = new String("object");
		oos.writeObject(obj);
		fos.close();
		oos.close();
		PowerMock.replayAll();
		AndroidFilePersistentUtil.storeObject(context, "name", obj);
		PowerMock.verifyAll();
	}
	
	@Test
	public void testStoreObject_Exception() throws Exception
	{
		PowerMock.resetAll();
		File f2 = PowerMock.createMock(File.class);
		EasyMock.expect(context.getFilesDir()).andReturn(f2);
		EasyMock.expect(f2.getAbsolutePath()).andReturn("path");
		
		IOException e = new IOException();
		File f = PowerMock.createMock(File.class);
		PowerMock.expectNew(File.class, "path/name").andReturn(f);
		FileOutputStream fos = PowerMock.createMock(FileOutputStream.class);
		ObjectOutputStream oos = PowerMock.createMock(ObjectOutputStream.class);
		PowerMock.expectNew(FileOutputStream.class, f).andReturn(fos);
		PowerMock.expectNew(ObjectOutputStream.class , fos).andReturn(oos);
		Object obj = new String("object");
		oos.writeObject(obj);
		EasyMock.expectLastCall().andThrow(e);
		fos.close();
		EasyMock.expectLastCall().andThrow(e);
		oos.close();
		EasyMock.expectLastCall().andThrow(e);
		PowerMock.replayAll();
		AndroidFilePersistentUtil.storeObject(context, "name", obj);
		PowerMock.verifyAll();
	}
	
	@Test
	public void testLoadObject() throws Exception
	{
		PowerMock.resetAll();
		File f2 = PowerMock.createMock(File.class);
		EasyMock.expect(context.getFilesDir()).andReturn(f2);
		EasyMock.expect(f2.getAbsolutePath()).andReturn("path");
		
		File f = PowerMock.createMock(File.class);
		PowerMock.expectNew(File.class, "path/name").andReturn(f);
		FileInputStream fis = PowerMock.createMock(FileInputStream.class);
		ObjectInputStream ois = PowerMock.createMock(ObjectInputStream.class);
		PowerMock.expectNew(FileInputStream.class, f).andReturn(fis);
		PowerMock.expectNew(ObjectInputStream.class, fis).andReturn(ois);
		Object obj = new String("test");
		fis.close();
		ois.close();
		PowerMock.replayAll();
		AndroidFilePersistentUtil.loadObject(context, "name");
		PowerMock.verifyAll();
			
	}
	@Test
	public void testLoadObject_Exception() throws Exception
	{
		PowerMock.resetAll();
		File f2 = PowerMock.createMock(File.class);
		EasyMock.expect(context.getFilesDir()).andReturn(f2);
		EasyMock.expect(f2.getAbsolutePath()).andReturn("path");
		
		IOException e = new IOException();
		File f = PowerMock.createMock(File.class);
		PowerMock.expectNew(File.class, "path/name").andReturn(f);
		FileInputStream fis = PowerMock.createMock(FileInputStream.class);
		ObjectInputStream ois = PowerMock.createMock(ObjectInputStream.class);
		PowerMock.expectNew(FileInputStream.class, f).andReturn(fis);
		PowerMock.expectNew(ObjectInputStream.class, fis).andReturn(ois);
		Object obj = new String("test");
		EasyMock.expect(ois.readObject()).andThrow(e);
		fis.close();
		EasyMock.expectLastCall().andThrow(e);
		ois.close();
		EasyMock.expectLastCall().andThrow(e);
		PowerMock.replayAll();
		AndroidFilePersistentUtil.loadObject(context, "name");
		PowerMock.verifyAll();
		
	}
}
