/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * ProtoBufStartupProxy.java
 *
 */
package com.telenav.data.serverproxy.impl.protobuf;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.Vector;

import org.easymock.EasyMock;
import org.easymock.IArgumentMatcher;
import org.junit.Before;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.dao.misc.SimpleConfigDao;
import com.telenav.data.dao.serverproxy.AbstractDaoManager;
import com.telenav.data.dao.serverproxy.AddressDao;
import com.telenav.data.dao.serverproxy.MandatoryNodeDao;
import com.telenav.data.dao.serverproxy.ResourceBarDao;
import com.telenav.data.dao.serverproxy.ServerDrivenParamsDao;
import com.telenav.data.dao.serverproxy.StartupDao;
import com.telenav.data.datatypes.mandatory.MandatoryProfile;
import com.telenav.data.datatypes.mandatory.MandatoryProfile.UserInfo;
import com.telenav.data.datatypes.primitive.StringList;
import com.telenav.data.serverproxy.IServerProxyConstants;
import com.telenav.data.serverproxy.RequestItem;
import com.telenav.j2me.datatypes.ProtocolBuffer;
import com.telenav.j2me.framework.util.ProtocolBufferUtil;
import com.telenav.module.AppConfigHelper;
import com.telenav.util.PrimitiveTypeCache;

/**
 * @author bmyang
 * @date 2011-9-27
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(AbstractDaoManager.class)
@SuppressStaticInitializationFor("com.telenav.module.AppConfigHelper")
public class ProtoBufStartupProxyTest
{

    byte[] requestData = {8, 0, 18, -20, 1, 18, 5, 49, 46, 51, 46, 48, 26, 5, 49, 46, 51, 46, 48, 34, 5, 49, 46, 48, 46, 48, 42, 5, 49, 46, 48, 46, 48, 50, 3, 49, 46, 48, 58, -56, 1, 10, -78, 1, 45, 53, 56, 55, 56, 54, 53, 54, 49, 44, 45, 51, 51, 44, 50, 48, 56, 49, 52, 50, 50, 55, 49, 44, 45, 50, 57, 52, 57, 50, 48, 44, 50, 49, 52, 55, 52, 56, 51, 54, 52, 55, 44, 48, 44, 45, 56, 48, 53, 54, 51, 52, 48, 52, 56, 44, 45, 49, 44, 50, 48, 51, 49, 54, 49, 53, 44, 48, 44, 48, 44, 45, 50, 49, 52, 55, 52, 56, 51, 54, 52, 56, 44, 50, 49, 52, 55, 51, 53, 50, 55, 48, 51, 44, 48, 44, 48, 44, 45, 49, 48, 52, 56, 53, 55, 54, 44, 50, 54, 56, 54, 57, 56, 49, 49, 49, 44, 54, 55, 49, 55, 52, 52, 54, 52, 44, 49, 54, 55, 57, 51, 54, 49, 54, 44, 48, 44, 48, 44, 48, 44, 48, 44, 48, 44, 48, 44, 48, 44, 48, 44, 48, 44, 48, 44, 48, 44, 48, 44, 48, 44, 48, 44, 48, 44, 48, 44, 48, 44, 49, 52, 50, 48, 50, 55, 54, 44, 48, 18, 17, 49, 48, 52, 56, 53, 55, 48, 44, 48, 44, 49, 50, 52, 48, 56, 44, 48};
    byte[] responseData = {7, 0, 0, 0, 83, 116, 97, 114, 116, 117, 112, -104, 0, 0, 0, 8, 0, 18, 0, 26, 8, 79, 80, 84, 73, 79, 78, 65, 76, 34, 7, 116, 101, 108, 101, 110, 97, 118, 42, 6, 55, 46, 48, 46, 48, 49, 50, 39, 104, 116, 116, 112, 58, 47, 47, 54, 51, 46, 50, 51, 55, 46, 50, 50, 48, 46, 56, 54, 58, 56, 48, 56, 48, 47, 111, 116, 97, 47, 116, 110, 54, 48, 46, 104, 116, 109, 108, 58, 45, 65, 32, 110, 101, 119, 32, 118, 101, 114, 115, 105, 111, 110, 32, 111, 102, 32, 65, 84, 38, 84, 32, 78, 97, 118, 105, 103, 97, 116, 111, 114, 32, 105, 115, 32, 97, 118, 97, 105, 108, 97, 98, 108, 101, 46, 66, 4, 50, 50, 53, 48, 72, 0, 80, 1, 98, 3, 49, 46, 49, 106, 10, 84, 101, 108, 101, 32, 65, 116, 108, 97, 115, 114, 1, 56, 122, 1, 57};
    private ProtoBufStartupProxy proxy;
    private StartupDao startupDao;
    private DaoManager daoManager;
    @Before
    public void setUp() throws Exception
    {
    	Whitebox.setInternalState(AppConfigHelper.class, "isLoggerEnable", false);
        proxy = new ProtoBufStartupProxy(null, null, null, null);
        daoManager = PowerMock.createMock(DaoManager.class);
        PowerMock.mockStatic(AbstractDaoManager.class);
        EasyMock.expect(AbstractDaoManager.getInstance()).andReturn(daoManager).anyTimes();
        startupDao = PowerMock.createMock(StartupDao.class);
        EasyMock.expect(daoManager.getStartupDao()).andReturn(startupDao).anyTimes();
    }

    /**
     * Test method for {@link com.telenav.data.serverproxy.impl.protobuf.ProtoBufStartupProxyTest#addRequestArgs(java.util.Vector, com.telenav.data.serverproxy.RequestItem)}.
     */
    @Test
    public void testAddRequestArgs()
    {
        RequestItem requestItem = new RequestItem(IServerProxyConstants.ACT_STARTUP, proxy);
        requestItem.params = new Vector();
        requestItem.params.addElement(PrimitiveTypeCache.valueOf(0L));
        Vector requestVector = new Vector();
        
        ServerDrivenParamsDao sdpDao = PowerMock.createMock(ServerDrivenParamsDao.class);
        EasyMock.expect(daoManager.getServerDrivenParamsDao()).andReturn(sdpDao).anyTimes();
        EasyMock.expect(sdpDao.getVersion("")).andReturn("1.3.0").anyTimes();
        
        ResourceBarDao rbDao = PowerMock.createMock(ResourceBarDao.class);
        EasyMock.expect(daoManager.getResourceBarDao()).andReturn(rbDao).anyTimes();
        EasyMock.expect(rbDao.getCategoryVersion("")).andReturn("1.0.0").anyTimes();
        EasyMock.expect(rbDao.getAirportVersion()).andReturn("1.0.0").anyTimes();
        EasyMock.expect(rbDao.getResourceFormatVersion()).andReturn("1.0").anyTimes();
        
        EasyMock.expect(rbDao.getAudioInventory()).andReturn("-58786561,-33,208142271,-294920,2147483647,0,-805634048,-1,2031615,0,0,-2147483648,2147352703,0,0,-1048576,268698111,67174464,16793616,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1420276").anyTimes();
        EasyMock.expect(rbDao.getAudioRuleInventory()).andReturn("1048570,0,12408").anyTimes();
        
        EasyMock.expect(rbDao.getAudioTimestamp()).andReturn("0").anyTimes();
        EasyMock.expect(rbDao.getAudioRuleTimestamp()).andReturn("0").anyTimes();
        
        MandatoryNodeDao mandatoryNodeDao = PowerMock.createMock(MandatoryNodeDao.class);
        MandatoryProfile mandatoryProfile = PowerMock.createMock(MandatoryProfile.class);
        EasyMock.expect(daoManager.getMandatoryNodeDao()).andReturn(mandatoryNodeDao).anyTimes();
        EasyMock.expect(mandatoryNodeDao.getMandatoryNode()).andReturn(mandatoryProfile).anyTimes();
        UserInfo userInfo = PowerMock.createMock(UserInfo.class);
        EasyMock.expect(mandatoryProfile.getUserInfo()).andReturn(userInfo).anyTimes();
        userInfo.region = "";
        
        PowerMock.replayAll();
        try
        {
            proxy.addRequestArgs(requestVector, requestItem);
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            fail(e.toString());
        }
        Assert.assertEquals(1, requestVector.size());
        ProtocolBuffer pb = (ProtocolBuffer) requestVector.get(0);
        Assert.assertEquals(IServerProxyConstants.ACT_STARTUP, pb.getObjType());
        Assert.assertArrayEquals(requestData, pb.getBufferData());
        PowerMock.verifyAll();
    }

    /**
     * Test method for {@link com.telenav.data.serverproxy.impl.protobuf.ProtoBufStartupProxyTest#parseRequestSpecificData(com.telenav.j2me.datatypes.ProtocolBuffer)}.
     */
//    @Test
//    public void testParseRequestSpecificData()
//    {
//        AddressDao addressDao = PowerMock.createMock(AddressDao.class);
//        EasyMock.expect(daoManager.getAddressDao()).andReturn(addressDao).anyTimes();
//        StringList list = new StringList();
//        ProtocolBuffer[] bufs = ProtocolBufferUtil.toBufArray(responseData);
//        String upgradeType = "OPTIONAL";
//        String upgradeFileName = "telenav";
//        String upgradeAppVersion = "7.0.01";
//        String upgradeUrl = "http://63.237.220.86:8080/ota/tn60.html";
//        String upgradeSummary = "A new version of AT&T Navigator is available.";
//        String upgradeBuildNumber = "2250";
//        String upgradeMinOSVersion = "8";
//        String upgradeMaxOSVersion = "9";   
//        list.add(upgradeType);
//        list.add(upgradeFileName);
//        list.add(upgradeAppVersion);
//        list.add(upgradeUrl);
//        list.add(upgradeSummary);
//        list.add(upgradeBuildNumber);                
//        list.add(upgradeMinOSVersion);
//        list.add(upgradeMaxOSVersion);
//        
//        SimpleConfigDao simpleConfigDao = PowerMock.createMock(SimpleConfigDao.class);
//        EasyMock.expect(daoManager.getSimpleConfigDao()).andReturn(simpleConfigDao).anyTimes();
//        simpleConfigDao.put(SimpleConfigDao.KEY_SWITCHED_DATASET,"Tele Atlas");
//        simpleConfigDao.put(SimpleConfigDao.KEY_MAP_COPYRIGHT,"2010 TomTom");
//        simpleConfigDao.store();
//        
//        startupDao.setUpgradeInfo(ProtoBufStartupProxyTest.stirnglistMatch(list));
//        int sharedAddrNumber = 0;
//        addressDao.setNetworkUnrevieweAddressSize(sharedAddrNumber);
//        String newAppVersion = "1.1";
//        list = new StringList();
//        list.add(newAppVersion);
//        startupDao.setNewAppInfo(ProtoBufStartupProxyTest.stirnglistMatch(list));
////        startupDao.setMapDataset("Tele Atlas");
//        EasyMock.expect(startupDao.getMapDataset()).andReturn("").anyTimes();
//        startupDao.store();
//        PowerMock.replayAll();
//        try
//        {
//            proxy.parseRequestSpecificData(bufs[0]);
//        }
//        catch (IOException e)
//        {
//            // TODO Auto-generated catch block
//            fail(e.toString());
//        }
//        PowerMock.verifyAll();
//    }
    
   static class StringListMatcher implements IArgumentMatcher
    {

        StringList expected;
        StringListMatcher(StringList expected)
        {
            this.expected = expected;
        }
        
        /* (non-Javadoc)
         * @see org.easymock.IArgumentMatcher#appendTo(java.lang.StringBuffer)
         */
        public void appendTo(StringBuffer arg0)
        {
            // TODO Auto-generated method stub
            
        }

        /* (non-Javadoc)
         * @see org.easymock.IArgumentMatcher#matches(java.lang.Object)
         */
        public boolean matches(Object actual)
        {
            if (!(actual instanceof StringList))
            {
                return false;
            }
            StringList list = (StringList) actual;
            if (list.size() != expected.size())
            {
                return false;
            }
            for (int i = 0; i< list.size(); i++)
            {
               if (!list.elementAt(i).equals(expected.elementAt(i)))
               {
                   return false;
               }
            }
            return true;
        }
    }
    
    public static StringList stirnglistMatch(StringList in) {  
        EasyMock.reportMatcher(new StringListMatcher(in));  
        return in;  
    }  

}
