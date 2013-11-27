package com.telenav.persistent.android;

import org.junit.Test;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.media.MediaPlayer;
import static org.junit.Assert.*;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.telenav.logger.Logger;

@RunWith(PowerMockRunner.class)
@PrepareForTest({AndroidDatabasePersistentUtil.class, ContentValues.class, Logger.class})
public class AndroidDatabasePersistentUtilTest 
{
//	private SQLiteDatabase db;
//	
//	@Before
//	public void setUp() throws Exception 
//	{
//		db = PowerMock.createMock(SQLiteDatabase.class);
//	}
//
//	/**
//	 * @throws java.lang.Exception
//	 */
//	@After
//	public void tearDown() throws Exception 
//	{
//		
//	}
//	
//	@Test
//	public void testCreateTable()
//	{
//		PowerMock.resetAll();
//		db.execSQL("CREATE TABLE IF NOT EXISTS " + "tableName" + " (" + "id" + " varchar(32) PRIMARY KEY, " + "data" + " byte[])");
//		PowerMock.replayAll();
//		AndroidDatabasePersistentUtil.createTable(db, "tableName");
//		PowerMock.verifyAll();
//	}
//	
//	@Test
//	public void testCreateTable_Exception()
//	{
//		PowerMock.resetAll();
//		SQLException e = PowerMock.createMock(SQLException.class);
//		PowerMock.mockStatic(Logger.class);
//		Logger.log(EasyMock.anyObject(String.class), EasyMock.anyObject(SQLException.class));
//		db.execSQL("CREATE TABLE IF NOT EXISTS " + "tableName" + " (" + "id" + " varchar(32) PRIMARY KEY, " + "data" + " byte[])");
//		EasyMock.expectLastCall().andThrow(e);
//		PowerMock.replayAll();
//		AndroidDatabasePersistentUtil.createTable(db, "tableName");
//		PowerMock.verifyAll();
//	}
//	
//	@Test
//	public void testDeleteTable()
//	{
//		PowerMock.resetAll();
//		EasyMock.expect(db.delete("tableName", null, null)).andReturn(1);
//		PowerMock.replayAll();
//		AndroidDatabasePersistentUtil.deleteTable(db, "tableName");
//		PowerMock.verifyAll();
//	}
//
//	@Test
//	public void testDeleteTable_Exception()
//	{
//		PowerMock.resetAll();
//		
//		RuntimeException e = new RuntimeException();
//		PowerMock.mockStatic(Logger.class);
//		Logger.log(EasyMock.anyObject(String.class), EasyMock.anyObject(RuntimeException.class));
//		
//		EasyMock.expect(db.delete("tableName", null, null)).andThrow(e);	
//		PowerMock.replayAll();
//		AndroidDatabasePersistentUtil.deleteTable(db, "tableName");
//		PowerMock.verifyAll();
//	}
//	
//	@Test
//	public void saveData() throws Exception//NOTE
//	{
//		PowerMock.resetAll();
//		
//		byte[] data = new byte[20];
//		ContentValues value = PowerMock.createMock(ContentValues.class);
//		PowerMock.expectNew(ContentValues.class).andReturn(value);
//		value.put("id", "id");
//		value.put("data",data);
//		EasyMock.expect(db.replace("tableName", "", value)).andReturn(1l);
//		PowerMock.replayAll();
//		AndroidDatabasePersistentUtil.saveData(db, "tableName", "id", data);
//		PowerMock.verifyAll();
//	}
//	
//	@Test
//	public void saveData_Exception() throws Exception//NOTE
//	{
//		PowerMock.resetAll();
//		
//		RuntimeException e = new RuntimeException();
//		PowerMock.mockStatic(Logger.class);
//		Logger.log(EasyMock.anyObject(String.class), EasyMock.anyObject(RuntimeException.class));
//		byte[] data = new byte[20];
//		ContentValues value = PowerMock.createMock(ContentValues.class);
//		PowerMock.expectNew(ContentValues.class).andReturn(value);
//		value.put("id", "id");
//		value.put("data",data);
//		EasyMock.expect(db.replace("tableName", "", value)).andThrow(e);
//		PowerMock.replayAll();
//		AndroidDatabasePersistentUtil.saveData(db, "tableName", "id", data);
//		PowerMock.verifyAll();
//	}
//	
//	@Test
//	public void testReadData()//TODO
//	{
//		PowerMock.resetAll();
//		
//		Cursor cursor = PowerMock.createMock(Cursor.class);
//		EasyMock.expect(db.query(EasyMock.eq("tableName"), EasyMock.aryEq(new String[]{"data"}), EasyMock.eq("id" + " = ?"), EasyMock.aryEq(new String[]{"id"}), EasyMock.anyObject(String.class), EasyMock.anyObject(String.class), EasyMock.anyObject(String.class))).andReturn(cursor);
//		EasyMock.expect(cursor.moveToFirst()).andReturn(true);
//		EasyMock.expect(cursor.getBlob(0)).andReturn(new byte[10]);
//		cursor.close();
//		PowerMock.replayAll();
//		AndroidDatabasePersistentUtil.readData(db, "tableName", "id");
//		PowerMock.verifyAll();
//	
//	}
//	
//	@Test
//	public void testReadData_Exception() {
//		PowerMock.resetAll();
//
//		//Cursor cursor = PowerMock.createMock(Cursor.class);
//		RuntimeException e = new RuntimeException();
//		EasyMock.expect(
//				db.query(EasyMock.eq("tableName"), EasyMock
//						.aryEq(new String[] { "data" }), EasyMock.eq("id"
//						+ " = ?"), EasyMock.aryEq(new String[] { "id" }),
//						EasyMock.anyObject(String.class), EasyMock
//								.anyObject(String.class), EasyMock
//								.anyObject(String.class))).andThrow(e);
//		//EasyMock.expect(cursor.moveToFirst()).andReturn(true);
//		//EasyMock.expect(cursor.getBlob(0)).andReturn(new byte[10]);
//		//cursor.close();
//		PowerMock.replayAll();
//		AndroidDatabasePersistentUtil.readData(db, "tableName", "id");
//		PowerMock.verifyAll();
//
//	}
//	
//	@Test
//	public void testDeleteData()//NOTE
//	{
//		PowerMock.resetAll();
//		String tableName = "tableName";
//		String whereClause = "id" + " = ?";
//		String id = "id";
//		EasyMock.expect(db.delete(EasyMock.eq(tableName), EasyMock.eq(whereClause), EasyMock.aryEq(new String[]{id}))).andReturn(1);
//		PowerMock.replayAll();
//		AndroidDatabasePersistentUtil.deleteData(db, tableName, id);
//		PowerMock.verifyAll();
//		
//	}
//	
//	@Test
//	public void testDeleteData_Exception()//NOTE
//	{
//		PowerMock.resetAll();
//		RuntimeException e = new RuntimeException();
//		PowerMock.mockStatic(Logger.class);
//		Logger.log(EasyMock.anyObject(String.class), EasyMock.anyObject(RuntimeException.class));
//		String tableName = "tableName";
//		String whereClause = "id" + " = ?";
//		String id = "id";
//		EasyMock.expect(db.delete(EasyMock.eq(tableName), EasyMock.eq(whereClause), EasyMock.aryEq(new String[]{id}))).andThrow(e);
//		PowerMock.replayAll();
//		AndroidDatabasePersistentUtil.deleteData(db, tableName, id);
//		PowerMock.verifyAll();
//		
//	}
}
