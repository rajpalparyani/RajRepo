/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * MaiTaiHandler.java
 *
 */
/**
 * 
 */
package com.telenav.sdk.maitai.impl;

import java.util.Hashtable;

import org.easymock.EasyMock;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import com.telenav.data.dao.serverproxy.AbstractDaoManager;
import com.telenav.module.location.LocationProvider;
import com.telenav.sdk.plugin.PluginDataProvider;

import junit.framework.TestCase;

/**
 *@author Bu changrong
 *@date 2011-11-17
 */
/**
 * @author chrbu
 *
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(
{ MaiTaiHandler.class, MaiTaiUtil.class, MaiTaiHandler.class ,MaiTaiAuthenticate.class,
    MaiTaiManager.class,MaiTaiParameter.class,PluginDataProvider.class})
public class MaiTaiHandlerTest extends TestCase
{
   public void test_getRequest() throws Exception
   {
       MaiTaiHandler mockMaiTaiHandler = PowerMock.createPartialMock(MaiTaiHandler.class, "execute");
       String uri="telenav://map?v=2.0&cb=planner%3A%2F%2F08092009&k=AQAAASSR1YUgf%2F%2F%2F%2F%2F%2F%2F%2F%2F8AAAABAAAAAQEAAAAQUm2yry0wCYjB0QetcacwXwEAAAAOAwAAABEAAAAZAAAAAQA%3D&c=cn&markers=label%3ADavid%7C37.3739%2C-121.9993";
       Whitebox.setInternalState(mockMaiTaiHandler, "uri", uri);
       Whitebox.setInternalState(mockMaiTaiHandler, "results", new Hashtable());
       
       PowerMock.mockStatic(MaiTaiManager.class);
       MaiTaiManager mockMaiTaiManager = PowerMock.createPartialMock(MaiTaiManager.class, "getMaiTaiParameter");
       MaiTaiParameter mockMaiTaiParameter = PowerMock.createPartialMock(MaiTaiParameter.class, "notify","getAction");
       PowerMock.expectPrivate(mockMaiTaiParameter, "notify",EasyMock.anyObject(String.class)).anyTimes();
       PowerMock.expectPrivate(mockMaiTaiParameter, "getAction").andReturn("navTo").anyTimes();
       EasyMock.expect(MaiTaiManager.getInstance()).andReturn(mockMaiTaiManager).anyTimes();
       EasyMock.expect(mockMaiTaiManager.getMaiTaiParameter()).andReturn(mockMaiTaiParameter).anyTimes();
       
       PowerMock.mockStatic(PluginDataProvider.class);
       PluginDataProvider mockPluginDataProvider = PowerMock.createPartialMock(PluginDataProvider.class, "initialize");
       PowerMock.expectPrivate(mockPluginDataProvider, "initialize",EasyMock.anyObject(Hashtable.class)).anyTimes();
       EasyMock.expect(PluginDataProvider.getInstance()).andReturn(mockPluginDataProvider).anyTimes();
  
       PowerMock.replayAll();
       Whitebox.invokeMethod(mockMaiTaiHandler, "getRequest");
       assertEquals(false, mockMaiTaiHandler.isCancelled());
       PowerMock.replayAll();
   }
}
