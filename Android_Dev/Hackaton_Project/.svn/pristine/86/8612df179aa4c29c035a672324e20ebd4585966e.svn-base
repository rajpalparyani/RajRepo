/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * ResourceManagerTest.java
 *
 */

package com.telenav.searchwidget.res;

import org.easymock.EasyMock;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.telenav.i18n.ResourceBundle;

import junit.framework.TestCase;

/**
 * @author xinrongl
 * @date Oct 6, 2011
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({ResourceManager.class, ResourceBundle.class})
public class ResourceManagerTest extends TestCase
{
	private ResourceManager manager;
	private ResourceBundle currentBundle;
	
    @Override
    protected void setUp() throws Exception
    {
        super.setUp();        
        
    	PowerMock.mockStatic(ResourceManager.class);
    	manager = PowerMock.createPartialMockAndInvokeDefaultConstructor(ResourceManager.class, new String[]{"getCurrentBundle"});
    	
        currentBundle = PowerMock.createMock(ResourceBundle.class);
        EasyMock.expect(manager.getCurrentBundle()).andReturn(currentBundle).anyTimes();
    }

    @Override
    protected void tearDown() throws Exception
    {
        super.tearDown();
    }
    
    public void testSetCurrentLocale()
    {
    	currentBundle.setLocale("es_MX");    	
    	EasyMock.expectLastCall().once();
    	PowerMock.replayAll();
    	
    	manager.setLocale("es_MX");
    	String locale = manager.getCurrentLocale();
    	
    	PowerMock.verifyAll();
    	
    	assertEquals("es_MX", locale);
    	
    	PowerMock.resetAll();
    }

}

