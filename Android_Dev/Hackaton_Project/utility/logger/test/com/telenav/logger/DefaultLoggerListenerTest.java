package com.telenav.logger;

import java.util.Calendar;
import java.util.logging.Logger;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.telenav.logger.Logger.LoggerMessage;

/**
 *@author gbwang
 *@date 2011-7-12
 */

public class DefaultLoggerListenerTest extends TestCase
{
	DefaultLoggerListener defaultLoggerListener;
	String clazz;
	String message;
	Object[] params;
	int INFO = 0;
	int WARNING = 1;
	int EXCEPTION = 2;
	int ERROR = 3;
	protected void setUp() throws Exception
    {   	
		defaultLoggerListener = new DefaultLoggerListener();
		clazz = DefaultLoggerListenerTest.class.getName();  
		message = "test DefaultLoggerListener";
		params = null;
        super.setUp();
    }
	
	public void testLog_A()
	{
		int level = INFO;
		Throwable t = null;
		defaultLoggerListener.log(level, clazz, message, t, params);
		
		String className = defaultLoggerListener.getClass(clazz);
		String expect = "DefaultLoggerListenerTest";
		Assert.assertEquals(expect ,className);
	}
	
	public void testLog_B()
	{
		int level = WARNING;
		Throwable t = null;
		defaultLoggerListener.log(level, clazz, message, t, params);
		
		String className = defaultLoggerListener.getClass(clazz);
		String expect = "DefaultLoggerListenerTest";
		Assert.assertEquals(expect ,className);
	}
	public void testLog_C()
	{
		int level = EXCEPTION;
		Throwable t = null;
		defaultLoggerListener.log(level, clazz, message, t, params);
		
		String className = defaultLoggerListener.getClass(clazz);
		String expect = "DefaultLoggerListenerTest";
		Assert.assertEquals(expect ,className);
	}
	
	public void testLog_D()
	{
		int level = ERROR;
		try {
			Throwable t = new Throwable(message);
			defaultLoggerListener.log(level, clazz, message, t, params);
		} catch (Exception e) {

		}

		String className = defaultLoggerListener.getClass(clazz);
		String expect = "DefaultLoggerListenerTest";
		Assert.assertEquals(expect, className);
	}
}
