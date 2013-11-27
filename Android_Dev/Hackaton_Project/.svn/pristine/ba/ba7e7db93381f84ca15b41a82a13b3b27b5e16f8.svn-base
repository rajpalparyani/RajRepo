/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * JsonSerializableManagerTest.java
 *
 */
package com.telenav.data.serializable.json;

import org.easymock.EasyMock;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.telenav.data.dao.misc.ApacheResourceSaver;
import com.telenav.res.ResourceManager;


import junit.framework.TestCase;

/**
 *@author qli (qli@telenav.cn)
 *@date 2011-6-22
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({JsonSerializableManager.class, JsonAddressSerializable.class, JsonPoiSerializable.class})
public class JsonSerializableManagerTest extends TestCase
{
    JsonSerializableManager mockJsonSerializableManager;
    
    protected void setUp() throws Exception
    {
        PowerMock.suppress(PowerMock.constructor(JsonSerializableManager.class)); 
        PowerMock.mockStatic(JsonSerializableManager.class);
        mockJsonSerializableManager = PowerMock.createMock(JsonSerializableManager.class);
        EasyMock.expect(JsonSerializableManager.getJsonInstance()).andReturn(mockJsonSerializableManager);
    }
    
    public void testGetAddressSerializable()
    {
        JsonAddressSerializable mockJsonAddress = PowerMock.createMock(JsonAddressSerializable.class);
        EasyMock.expect(mockJsonSerializableManager.getAddressSerializable()).andReturn(mockJsonAddress);
        PowerMock.replayAll();
        JsonSerializableManager.getJsonInstance().getAddressSerializable();
        PowerMock.verifyAll();
    }
    
    public void testGetPoiSerializable()
    {
        JsonPoiSerializable mockJsonPoi = PowerMock.createMock(JsonPoiSerializable.class);
        EasyMock.expect(mockJsonSerializableManager.getPoiSerializable()).andReturn(mockJsonPoi);
        PowerMock.replayAll();
        JsonSerializableManager.getJsonInstance().getPoiSerializable();
        PowerMock.verifyAll();
    }
}
