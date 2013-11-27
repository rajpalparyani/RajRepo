/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * ProtoBufAddressProxyTest.java
 *
 */
package com.telenav.data.serverproxy.impl.protobuf;

import java.io.IOException;
import java.util.Vector;

import junit.framework.TestCase;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import com.telenav.data.datatypes.address.Address;
import com.telenav.data.datatypes.address.AddressAssert;
import com.telenav.data.datatypes.address.FavoriteCatalog;
import com.telenav.data.datatypes.address.Stop;
import com.telenav.data.serverproxy.IServerProxyConstants;
import com.telenav.data.serverproxy.RequestItem;
import com.telenav.j2me.datatypes.ProtocolBuffer;
import com.telenav.j2me.framework.protocol.ProtoCacheCitiesResp;
import com.telenav.j2me.framework.protocol.ProtoCallInStopResp;
import com.telenav.j2me.framework.protocol.ProtoStopPoiWrapper;
import com.telenav.module.AppConfigHelper;

/**
 *@author yhzhou
 *@date 2011-9-28
 */
@RunWith(PowerMockRunner.class)
@SuppressStaticInitializationFor("com.telenav.module.AppConfigHelper")
@PrepareForTest({ProtoBufAddressProxy.class})
public class ProtoBufAddressProxyTest extends TestCase
{
    byte[] synStops_data = {10, 49, 8, 3, 16, 1, 24, 0, 34, 2, 85, 83, 42, 0, 50, 0, 66, 29, 8, 0, 16, 0, 24, 0, 32, 0, 40, 0, 56, 0, 66, 2, 85, 83, 74, 0, 82, 2, 85, 83, 90, 2, 77, 65, 98, 1, 48, 80, 0, 88, 0, 34, 49, 8, 3, 16, 1, 24, 0, 34, 2, 85, 83, 42, 0, 50, 0, 66, 29, 8, 0, 16, 0, 24, 0, 32, 0, 40, 0, 56, 0, 66, 2, 85, 83, 74, 0, 82, 2, 85, 83, 90, 2, 77, 65, 98, 1, 48, 80, 0, 88, 0, 80, -72, -118, -25, 62, 88, -72, -118, -25, 62};
    byte[] getCallIn_data = {10, 2, 99, 99};
    byte[] cacheCities_data = {10, 8, 51, 52, 46, 48, 53, 51, 52, 56, 18, 10, 45, 49, 49, 56, 46, 50, 52, 53, 51, 50};
    
    byte[] cacheCities_resp = {7, 0, 0, 0, 83, 116, 97, 114, 116, 117, 112, -104, 0, 0, 0, 8, 0, 18, 0, 26, 8, 79, 80, 84, 73, 79, 78, 65, 76, 34, 7, 116, 101, 108, 101, 110, 97, 118, 42, 6, 55, 46, 48, 46, 48, 49, 50, 39, 104, 116, 116, 112, 58, 47, 47, 54, 51, 46, 50, 51, 55, 46, 50, 50, 48, 46, 56, 54, 58, 56, 48, 56, 48, 47, 111, 116, 97, 47, 116, 110, 54, 48, 46, 104, 116, 109, 108, 58, 45, 65, 32, 110, 101, 119, 32, 118, 101, 114, 115, 105, 111, 110, 32, 111, 102, 32, 65, 84, 38, 84, 32, 78, 97, 118, 105, 103, 97, 116, 111, 114, 32, 105, 115, 32, 97, 118, 97, 105, 108, 97, 98, 108, 101, 46, 66, 4, 50, 50, 53, 48, 72, 0, 80, 1, 98, 3, 49, 46, 49, 106, 10, 84, 101, 108, 101, 32, 65, 116, 108, 97, 115, 114, 1, 56, 122, 1, 57};
    byte[] fetchAllStops_resp = {7, 0, 0, 0, 83, 116, 97, 114, 116, 117, 112, -104, 0, 0, 0, 8, 0, 18, 0, 26, 8, 79, 80, 84, 73, 79, 78, 65, 76, 34, 7, 116, 101, 108, 101, 110, 97, 118, 42, 6, 55, 46, 48, 46, 48, 49, 50, 39, 104, 116, 116, 112, 58, 47, 47, 54, 51, 46, 50, 51, 55, 46, 50, 50, 48, 46, 56, 54, 58, 56, 48, 56, 48, 47, 111, 116, 97, 47, 116, 110, 54, 48, 46, 104, 116, 109, 108, 58, 45, 65, 32, 110, 101, 119, 32, 118, 101, 114, 115, 105, 111, 110, 32, 111, 102, 32, 65, 84, 38, 84, 32, 78, 97, 118, 105, 103, 97, 116, 111, 114, 32, 105, 115, 32, 97, 118, 97, 105, 108, 97, 98, 108, 101, 46, 66, 4, 50, 50, 53, 48, 72, 0, 80, 1, 98, 3, 49, 46, 49, 106, 10, 84, 101, 108, 101, 32, 65, 116, 108, 97, 115, 114, 1, 56, 122, 1, 57};
    byte[] synStops_resp = {8, 0, 18, 0, 26, 16, 72, -80, -43, -105, -6, -86, 38, 80, -80, -43, -105, -6, -86, 38, 88, 0};
    RequestItem requestItem = null;
    Vector requestVector = null;
    ProtoBufAddressProxy proxy = new ProtoBufAddressProxy(null, null, null, null, false); 
    IllegalArgumentException illegalArgumentException = null;
    
    protected void setUp() throws Exception
    {
        requestVector = new Vector();
        Whitebox.setInternalState(AppConfigHelper.class, "isLoggerEnable", false);
        super.setUp();
    }

    protected void tearDown() throws Exception
    {
        requestItem = null;
        illegalArgumentException = null;
        requestVector.clear();
        super.tearDown();
    }

    public void testAddRequestArgs_fetchAllStops()
    {
        requestItem = new RequestItem(IServerProxyConstants.ACT_FETCH_ALL_STOPS, proxy);
        try
        {
            proxy.addRequestArgs(requestVector, requestItem);
        }
        catch (Exception e)
        {
            fail(e.toString());
        }
        ProtocolBuffer pb = (ProtocolBuffer) requestVector.get(0);
        assertEquals(1, requestVector.size());
        assertEquals(IServerProxyConstants.ACT_FETCH_ALL_STOPS, pb.getObjType());
    }

    public void testAddRequestArgs_syncStops()
    {
        requestItem = new RequestItem(IServerProxyConstants.ACT_SYNC_STOPS, proxy);
        requestItem.params = new Vector();
        Address address = new Address();
        Stop stop = new Stop();
        stop.setCity("EAST BOSTON");
        stop.setCity("US");
        stop.setProvince("MA");
        address.setStop(stop);
        Vector addresses = new Vector();
        addresses.add(address);
        FavoriteCatalog favoriteCatalog = new FavoriteCatalog();
        favoriteCatalog.setName("sports");
        Vector categories = new Vector();
        Vector receivedAddresses = new Vector();
        categories.add(favoriteCatalog);
        requestItem.params.add(0, 131712312L);
        requestItem.params.add(1, addresses);
        requestItem.params.add(2, addresses);
        requestItem.params.add(3, categories);
        requestItem.params.add(4, receivedAddresses);
        try
        {
            proxy.addRequestArgs(requestVector, requestItem);
        }
        catch (Exception e)
        {
            fail(e.toString());
        }
        ProtocolBuffer pb = (ProtocolBuffer) requestVector.get(0);
        assertEquals(1, requestVector.size());
//        Assert.assertArrayEquals(synStops_data, pb.getBufferData());
        assertEquals(IServerProxyConstants.ACT_SYNC_STOPS, pb.getObjType());
    }
    
    public void testAddRequestArgs_syncStops_exception_nullParames()
    {
        requestItem = new RequestItem(IServerProxyConstants.ACT_SYNC_STOPS, proxy);
        try
        {
            proxy.addRequestArgs(requestVector, requestItem);
        }
        catch(IllegalArgumentException e)
        {
            illegalArgumentException = e;
        }
        catch (Exception e)
        {
            fail(e.toString());
        }
        finally
        {
            assertNotNull(illegalArgumentException);
        }
    }
    
    public void testAddRequestArgs_syncStops_exception_lessParames()
    {
        requestItem = new RequestItem(IServerProxyConstants.ACT_SYNC_STOPS, proxy);
        requestItem.params = new Vector();
        Address address = new Address();
        Stop stop = new Stop();
        stop.setCity("EAST BOSTON");
        stop.setCity("US");
        stop.setProvince("MA");
        address.setStop(stop);
        Vector addresses = new Vector();
        addresses.add(address);
        requestItem.params.add(0, 131712312L);
        requestItem.params.add(1, addresses);
        requestItem.params.add(2, addresses);
        try
        {
            proxy.addRequestArgs(requestVector, requestItem);
        }
        catch(IllegalArgumentException e)
        {
            illegalArgumentException = e;
        }
        catch (Exception e)
        {
            fail(e.toString());
        }
        finally
        {
            assertNotNull(illegalArgumentException);
        }
    }
    
    public void testAddRequestArgs_cacheCities()
    {
        requestItem = new RequestItem(IServerProxyConstants.ACT_CACHE_CITIES, proxy);
        requestItem.params = new Vector();
        requestItem.params.add(0, 34.05348);
        requestItem.params.add(1, -118.24532);
        try
        {
            proxy.addRequestArgs(requestVector, requestItem);
        }
        catch (Exception e)
        {
            fail(e.toString());
        }
        ProtocolBuffer pb = (ProtocolBuffer) requestVector.get(0);
        assertEquals(1, requestVector.size());
        Assert.assertArrayEquals(cacheCities_data, pb.getBufferData());
        assertEquals(IServerProxyConstants.ACT_CACHE_CITIES, pb.getObjType());
    }
    
    public void testAddRequestArgs_getCallIn()
    {
//        requestItem = new RequestItem(IServerProxyConstants.ACT_GET_CALL_IN, proxy);
//        requestItem.params = new Vector();
//        requestItem.params.add("cc");
//        try
//        {
//            proxy.addRequestArgs(requestVector, requestItem);
//        }
//        catch (Exception e)
//        {
//            fail(e.toString());
//        }
//        ProtocolBuffer pb = (ProtocolBuffer) requestVector.get(0);
//        assertEquals(1, requestVector.size());
//        Assert.assertArrayEquals(getCallIn_data, pb.getBufferData());
//        assertEquals(IServerProxyConstants.ACT_GET_CALL_IN, pb.getObjType());
    }
    
    public void testParseRequestSpecificData_cacheCities()
    {
        ProtoBufAddressProxy mockProxy = PowerMock.createPartialMock(ProtoBufAddressProxy.class, "parseNearCities");
        ProtocolBuffer protocolBuffer = new ProtocolBuffer();
        protocolBuffer.setBufferData(cacheCities_resp);
        protocolBuffer.setObjType(IServerProxyConstants.ACT_CACHE_CITIES);
        try
        {
            PowerMock.expectPrivate(mockProxy, "parseNearCities", EasyMock.anyObject(ProtoCacheCitiesResp.class));
        }
        catch (Exception e1)
        {
            fail(e1.toString());
        }
        EasyMock.expectLastCall().once();
        PowerMock.replay(mockProxy);
        try
        {
            mockProxy.parseRequestSpecificData(protocolBuffer);
        }
        catch (IOException e)
        {
            fail(e.toString());
        }
        PowerMock.verify(mockProxy);
    }
    
    public void testParseRequestSpecificData_fetchAllStops()
    {
        ProtoBufAddressProxy mockProxy = PowerMock.createPartialMock(ProtoBufAddressProxy.class, "parseFetchAllInThread");
        ProtocolBuffer protocolBuffer = new ProtocolBuffer();
        protocolBuffer.setBufferData(fetchAllStops_resp);
        protocolBuffer.setObjType(IServerProxyConstants.ACT_FETCH_ALL_STOPS);
        Whitebox.setInternalState(mockProxy, "isParseFetchAllInThread", true);
        try
        {
            PowerMock.expectPrivate(mockProxy, "parseFetchAllInThread", EasyMock.anyObject(ProtoCacheCitiesResp.class));
        }
        catch (Exception e1)
        {
            fail(e1.toString());
        }
        EasyMock.expectLastCall().once();
        PowerMock.replay(mockProxy);
        try
        {
            mockProxy.parseRequestSpecificData(protocolBuffer);
        }
        catch (IOException e)
        {
            fail(e.toString());
        }
        PowerMock.verify(mockProxy);
    }
    
    public void testParseRequestSpecificData_syncStops()
    {
        ProtoBufAddressProxy mockProxy = PowerMock.createPartialMock(ProtoBufAddressProxy.class, "parseSyncStops");
        ProtocolBuffer protocolBuffer = new ProtocolBuffer();
        protocolBuffer.setBufferData(synStops_resp);
        protocolBuffer.setObjType(IServerProxyConstants.ACT_SYNC_STOPS);
        try
        {
            PowerMock.expectPrivate(mockProxy, "parseSyncStops", EasyMock.anyObject(ProtoCacheCitiesResp.class));
        }
        catch (Exception e1)
        {
            fail(e1.toString());
        }
        EasyMock.expectLastCall().once();
        PowerMock.replay(mockProxy);
        try
        {
            mockProxy.parseRequestSpecificData(protocolBuffer);
        }
        catch (IOException e)
        {
            fail(e.toString());
        }
        PowerMock.verify(mockProxy);
    }
    
//    public void testParseRequestSpecificData_getCallIn() throws Exception
//    {
//        ProtoBufAddressProxy mockProxy = PowerMock.createPartialMockForAllMethodsExcept(ProtoBufAddressProxy.class, "parseRequestSpecificData");
//        
//        ProtoStopPoiWrapper.Builder stopPoiBuilder = ProtoStopPoiWrapper.newBuilder();
//        ProtoStopPoiWrapper stopPoiWrapper = stopPoiBuilder.build();
//        
//        Vector viaPoints = new Vector();
//        for (int i = 0; i < 5; i++)
//        {
//            ProtoStopPoiWrapper.Builder protoStopPoiBuilder = ProtoStopPoiWrapper.newBuilder();
//            ProtoStopPoiWrapper protoStopPoiWrapper = protoStopPoiBuilder.build();
//            viaPoints.add(protoStopPoiWrapper);
//        }
//        
//        byte[] responseData;
//        ProtoCallInStopResp.Builder builder = ProtoCallInStopResp.newBuilder();
//        builder.setErrorMessage("");
//        builder.setStatus(0);
//        builder.setDestination(stopPoiWrapper);
//        builder.setViaPoints(viaPoints);        
//        responseData = builder.build().toByteArray();
//        
//        ProtocolBuffer protoBuf = PowerMock.createMock(ProtocolBuffer.class);
//        EasyMock.expect(protoBuf.getObjType()).andReturn(IServerProxyConstants.ACT_GET_CALL_IN).anyTimes();
//        EasyMock.expect(protoBuf.getBufferData()).andReturn(responseData);
//        
//        PowerMock.replayAll();
//        try
//        {
//            mockProxy.parseRequestSpecificData(protoBuf);
//        }
//        catch (IOException e)
//        {
//            fail(e.toString());
//        }
////        AddressAssert.assertAddress(ProtoBufServerProxyUtil.convertAddress(stopPoiWrapper), ((CallInResult)Whitebox.getInternalState(mockProxy, "callInResult")).getAddress());
//        PowerMock.verifyAll();
//    }
}