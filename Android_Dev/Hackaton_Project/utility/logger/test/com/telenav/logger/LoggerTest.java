package com.telenav.logger;

import java.lang.reflect.Field;
import java.util.Vector;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.powermock.reflect.Whitebox;

import com.telenav.logger.Logger.LoggerCallback;
import com.telenav.logger.Logger.LoggerJob;
import com.telenav.logger.Logger.LoggerMessage;

/**
 *@author gbwang
 *@date 2011-7-8
 */

public class LoggerTest extends TestCase
{
	int INFO = 0;
	int WARNING = 1;
	int EXCEPTION = 2;
	int ERROR = 3;	
	Logger logger;		
	int initCount;
	LoggerJob loggerJob;
	boolean isShutDown;

	Vector loggerCallbacks;	
	Vector logMessages;
	
	protected void setUp() throws Exception
	{
		logger = new Logger();		
		initCount = 0;
		loggerJob = null;
		isShutDown = false;
		loggerCallbacks = new Vector();
		Field logMessagesField = Whitebox.getField(Logger.class, "logMessages");
		logMessages = (Vector) logMessagesField.get(Logger.class);
	    logMessages.removeAllElements();
		
		super.setUp();
	}
	
	private void util() throws IllegalArgumentException, IllegalAccessException
	{		
		Field initCountField = Whitebox.getField(Logger.class, "initCount");
		Field loggerJobField = Whitebox.getField(Logger.class, "loggerJob");
		Field isShutDownField = Whitebox.getField(Logger.class, "isShutDown");
		Field loggerCallbacksField = Whitebox.getField(Logger.class, "loggerCallbacks");
		
		initCount = initCountField.getInt(logger);
		loggerJob = (LoggerJob) loggerJobField.get(logger);
		isShutDown = isShutDownField.getBoolean(logger);
		loggerCallbacks = (Vector) loggerCallbacksField.get(logger);
	}
	
	public void testLogger() throws IllegalArgumentException, IllegalAccessException
	{
		Logger log = new Logger();		
		Throwable t = new Throwable();
		Logger.log(LoggerTest.class.getName(), t);
		Field logMessagesField = Whitebox.getField(Logger.class, "logMessages");
		logMessages = (Vector) logMessagesField.get(log);
		Assert.assertTrue(logMessages.size() > 0);
//		if(logMessages.size() > 0)
		{
	        LoggerMessage loggerMessage = (LoggerMessage) logMessages.elementAt(0);
	        String actual = loggerMessage.toString();
	        String expect = "[" + EXCEPTION + "]" + "[" + LoggerTest.class.getName() + "] " + "null";
	        Assert.assertEquals(expect ,actual);
		}
		
		String message = null;
		Logger.log(EXCEPTION, LoggerTest.class.getName(), message);
		logMessagesField = Whitebox.getField(Logger.class, "logMessages");
		logMessages = (Vector) logMessagesField.get(log);
		Assert.assertTrue(logMessages.size() > 0);
//		if(logMessages.size() > 0)
		{
	        LoggerMessage loggerMessage = (LoggerMessage) logMessages.elementAt(0);
	        String actual = loggerMessage.toString();
	        String expect = "[" + EXCEPTION + "]" + "[" + LoggerTest.class.getName() + "] " + "null";
	        Assert.assertEquals(expect ,actual);
		}
		
		Logger.start();
		util();
		Assert.assertEquals(1, initCount);
		Assert.assertNotNull(loggerJob);
		Assert.assertFalse(isShutDown);
        LoggerCallback callback = (LoggerCallback) loggerCallbacks.elementAt(0);
        Assert.assertNotNull(callback.logListener);
        Assert.assertNotNull(callback.logFilter);
		
		Logger.shutdown();
		util();
		Assert.assertTrue(isShutDown);
		Assert.assertEquals(0, initCount);

	}
}
