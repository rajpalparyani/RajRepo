/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * TestWebViewCacheManager.java
 *
 */
package com.telenav.data.cache;

import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import com.telenav.cache.AbstractCache;
import com.telenav.module.location.LocationProvider;
import com.telenav.ui.UiFactory;
import com.telenav.ui.citizen.CitizenWebComponent;

/**
 *@author bduan
 *@date 2011-6-20
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({UiFactory.class, CitizenWebComponent.class, AbstractCache.class, WebViewCacheManager.class})
public class WebViewCacheManagerTest extends TestCase
{
    String KEY = "TestKey";
    CitizenWebComponent mockCitizenWebComponent;
    AbstractCache mockCache;
    WebViewCacheManager webViewCacheManager;
    
    @Override
    protected void setUp() throws Exception
    {
        mockCache = PowerMock.createMock(AbstractCache.class);
        
        super.setUp();
    }
    
    @Override
    protected void tearDown() throws Exception
    {
        super.tearDown();
    }
    
    protected void initLocationProvider(String[] methodName)
    {
        PowerMock.suppress(PowerMock.constructor(WebViewCacheManager.class));
        PowerMock.mockStatic(WebViewCacheManager.class);
        webViewCacheManager = PowerMock.createNicePartialMockForAllMethodsExcept(WebViewCacheManager.class, methodName);
    }
    
    protected void setInternalField()
    {
        try
        {
            PowerMock.field(WebViewCacheManager.class, "webViewCache").set(webViewCacheManager, mockCache);
        }
        catch (IllegalArgumentException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IllegalAccessException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public void testContainsKey()
    {
        String key = EasyMock.anyObject(String.class);
        initLocationProvider(new String[]{"containsKey"});
        setInternalField();
        
        EasyMock.expect(mockCache.containsKey(key)).andReturn(false);
        
        PowerMock.replayAll();
        assertFalse(webViewCacheManager.containsKey(key));
        PowerMock.verifyAll();

    }
    
    public void testGetWebView()
    {
        String key = "TestKEY";
        int id = EasyMock.anyInt();
        initLocationProvider(new String[]{"getWebView"});
        setInternalField();
        
        PowerMock.mockStatic(UiFactory.class);
        UiFactory mockUiFactory = PowerMock.createMock(UiFactory.class);
        mockCitizenWebComponent = PowerMock.createMock(CitizenWebComponent.class);
        
        EasyMock.expect(mockCache.containsKey(key)).andReturn(false).anyTimes();
        EasyMock.expect(UiFactory.getInstance()).andReturn(mockUiFactory).anyTimes();
        EasyMock.expect(mockUiFactory.createCitizenWebComponent(null, id, false)).andReturn(mockCitizenWebComponent).anyTimes();
        mockCitizenWebComponent.initDefaultZoomDensity();
        
        EasyMock.expect(mockCitizenWebComponent.getParent()).andReturn(null);
        
        PowerMock.replayAll();
        webViewCacheManager.getWebView(key, id);
        PowerMock.verifyAll();
    }
    
    public void testAdd()
    {
        String key = "Test";
        CitizenWebComponent value = PowerMock.createMock(CitizenWebComponent.class);
        
        initLocationProvider(new String[]{"add"});
        setInternalField();
        
        EasyMock.expect(mockCache.put(EasyMock.eq(key), EasyMock.eq(value))).andReturn(null);
        
        PowerMock.replayAll();
        webViewCacheManager.add(key, value);
        PowerMock.verifyAll();
        
    }
}
