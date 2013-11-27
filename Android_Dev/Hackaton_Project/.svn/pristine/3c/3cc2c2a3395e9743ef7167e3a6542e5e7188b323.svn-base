/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * ProtoBufServiceLocatorProxyTest.java
 *
 */
package com.telenav.data.serverproxy.impl.protobuf;

import static org.junit.Assert.assertArrayEquals;

import java.util.Vector;

import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import com.telenav.data.dao.serverproxy.AbstractDaoManager;
import com.telenav.data.dao.serverproxy.ServiceLocatorDao;
import com.telenav.data.serializable.SerializableManager;
import com.telenav.data.serverproxy.IServerProxyConstants;
import com.telenav.data.serverproxy.RequestItem;
import com.telenav.j2me.datatypes.ProtocolBuffer;
import com.telenav.j2me.framework.protocol.ProtoSyncServiceLocatorReq;
import com.telenav.module.AppConfigHelper;



/**
 *@author Barney (cchen@telenavsoftware.com)
 *@date 2011-10-8
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({AbstractDaoManager.class, SerializableManager.class})
@SuppressStaticInitializationFor("com.telenav.module.AppConfigHelper")
public class ProtoBufServiceLocatorProxyTest extends TestCase
{
    private ProtoBufServiceLocatorProxy proxy;

    @Before
    public void setUp() throws Exception
    {
        proxy = PowerMock.createPartialMock(ProtoBufServiceLocatorProxy.class, "getRegion");
        Whitebox.setInternalState(AppConfigHelper.class, "isLoggerEnable", false);
        super.setUp();
    }

    @After
    public void tearDown() throws Exception
    {
        super.tearDown();
    }

    @Test
    public void testAddRequestArgs() throws Exception
    {
        RequestItem requestItem = new RequestItem(IServerProxyConstants.ACT_SYNC_SERVICE_LOCATOR, proxy);
        Vector requestVector = new Vector();
        
        AbstractDaoManager daoManager = PowerMock.createMock(AbstractDaoManager.class);
        PowerMock.mockStatic(AbstractDaoManager.class);
        EasyMock.expect(AbstractDaoManager.getInstance()).andReturn(daoManager).anyTimes();
        EasyMock.expect(proxy.getRegion()).andReturn(null).anyTimes();
        ServiceLocatorDao serviceLocatorDao = PowerMock.createMock(ServiceLocatorDao.class);
        EasyMock.expect(daoManager.getServiceLocatorDao()).andReturn(serviceLocatorDao).anyTimes();
        EasyMock.expect(serviceLocatorDao.getVersion()).andReturn("");
        ProtoSyncServiceLocatorReq.Builder builder = ProtoSyncServiceLocatorReq.newBuilder();
        builder.setServiceMappingVersion("");
        EasyMock.expectLastCall().once();
        PowerMock.replayAll();
        
        proxy.addRequestArgs(requestVector, requestItem);
        
        assertEquals(1, requestVector.size());
        ProtocolBuffer pb = (ProtocolBuffer) requestVector.get(0);
        assertArrayEquals(builder.build().toByteArray(), pb.getBufferData());
        assertEquals(IServerProxyConstants.ACT_SYNC_SERVICE_LOCATOR, pb.getObjType());
        PowerMock.verifyAll();
    }

//    FIXME, we will fix this test case later.
//    @Test
//    public void testParseRequestSpecificData() throws IOException
//    {
//        String urlItem = "URLItem";
//        String domainName = "DomainName";
//        String domainAlias = "DomainAlias";
//        String action = "Action";
//        Vector urlEntries = new Vector();
//        urlEntries.add(domainAlias);
//        Vector urlItems = new Vector();
//        urlItems.add(urlItem);
//        Vector actions = new Vector();
//        actions.add(action);
//        
//        StringMap domainMap = new StringMap();
//        StringMap actionMap = new StringMap();
//        String domainMapKey = domainAlias;
//        String domainMapValue = urlItem;
//        String actionMapKey = action;
//        String actionMapValue = urlItem + domainName;
//        domainMap.put(domainMapKey, domainMapValue);
//        actionMap.put(actionMapKey, actionMapValue);
//        
//        ProtoServiceItem.Builder serviceItemBuilder = ProtoServiceItem.newBuilder();
//        serviceItemBuilder.setActions(actions);
//        serviceItemBuilder.setServiceDomainName(domainName);
//        serviceItemBuilder.setServiceItemType("serviceItemType");
//        serviceItemBuilder.setUrlEntry(urlEntries);
//        serviceItemBuilder.setUrlItem(urlItems);
//        ProtoServiceItem serviceItem = serviceItemBuilder.build();
//        
//        Vector serviceItems = new Vector();
//        serviceItems.add(serviceItem);
//        String serviceVerision = "1000";
//        ProtoServiceMapping.Builder serviceMappingBuilder = ProtoServiceMapping.newBuilder();
//        serviceMappingBuilder.setServiceItems(serviceItems);
//        serviceMappingBuilder.setServiceVersion(serviceVerision);
//        ProtoServiceMapping serviceMapping = serviceMappingBuilder.build();
//        
//        ProtoSyncServiceLocatorResp.Builder builder = ProtoSyncServiceLocatorResp.newBuilder();
//        builder.setStatus(IServerProxyConstants.SUCCESS);
//        builder.setServiceMapping(serviceMapping);
//        
//        ProtocolBuffer protoBuf = new ProtocolBuffer();
//        protoBuf.setObjType(IServerProxyConstants.ACT_SYNC_SERVICE_LOCATOR);
//        protoBuf.setBufferData(builder.build().toByteArray());
//        
//        AbstractDaoManager daoManager = PowerMock.createMock(AbstractDaoManager.class);
//        PowerMock.mockStatic(AbstractDaoManager.class);
//        EasyMock.expect(AbstractDaoManager.getInstance()).andReturn(daoManager).anyTimes();
//        ServiceLocatorDao serviceLocatorDao = PowerMock.createPartialMockForAllMethodsExcept(ServiceLocatorDao.class, "getActionMap", "getDomainMap", "getVersion", "setServerMappings");
//        EasyMock.expect(daoManager.getServiceLocatorDao()).andReturn(serviceLocatorDao).anyTimes();
//        
//        SerializableManager serializableManager = PowerMock.createMock(SerializableManager.class);
//        PowerMock.mockStatic(SerializableManager.class);
//        EasyMock.expect(SerializableManager.getInstance()).andReturn(serializableManager).anyTimes();
//        IPrimitiveSerializable primitiveSerializable = PowerMock.createMock(IPrimitiveSerializable.class);
//        EasyMock.expect(serializableManager.getPrimitiveSerializable()).andReturn(primitiveSerializable).anyTimes();
//        EasyMock.expect(primitiveSerializable.toBytes(EasyMock.anyObject(StringMap.class))).andReturn(new byte[10]).times(2);
//        EasyMock.expect(primitiveSerializable.createStringMap(EasyMock.anyObject(byte[].class))).andReturn(domainMap);
//        EasyMock.expect(primitiveSerializable.createStringMap(EasyMock.anyObject(byte[].class))).andReturn(actionMap);
//        
//        TnStore serverMappingStore = new TnStoreMock();
//        serverMappingStore.load();
//        Whitebox.setInternalState(serviceLocatorDao, "serverMappingStore", serverMappingStore);
//        serviceLocatorDao.store();
//        
//        EasyMock.expectLastCall().once();
//        PowerMock.replayAll();
//        proxy.parseRequestSpecificData(protoBuf);
//        
//        assertEquals(domainMapValue, serviceLocatorDao.getDomainMap().get(domainMapKey));
//        assertEquals(actionMapValue, serviceLocatorDao.getActionMap().get(actionMapKey));
//        assertEquals(serviceVerision, serviceLocatorDao.getVersion());
//        assertEquals(IServerProxyConstants.SUCCESS, proxy.getStatus());
//        PowerMock.verifyAll();
//    }

}
