/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * PluginModelTest.java
 *
 */
package com.telenav.sdk.plugin.module;

import java.util.Hashtable;
import java.util.Vector;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.telenav.app.CommManager;
import com.telenav.comm.Comm;
import com.telenav.data.dao.serverproxy.AbstractDaoManager;
import com.telenav.data.datatypes.address.Address;
import com.telenav.data.datatypes.address.Stop;
import com.telenav.data.serverproxy.impl.IRgcProxy;
import com.telenav.data.serverproxy.impl.ServerProxyFactory;
import com.telenav.mvc.MvcAssert;
import com.telenav.sdk.maitai.IMaiTai;
import com.telenav.sdk.plugin.PluginAddress;
import com.telenav.sdk.plugin.PluginManager;

/**
 * @author hchai
 * @date 2011-11-8
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(
{ AbstractDaoManager.class, PluginManager.class, ServerProxyFactory.class,
        CommManager.class })
public class PluginModelTest
{
    private PluginModel model;

    @Before
    public void setUp()
    {
        MvcAssert.init();
    }

    @After
    public void tearDown()
    {
    }

    /*@Test
    public void testActionInit_hasAddress()
    {
        initModel(null);

        Stop stop = new Stop();
        stop.setCity("sunnyvale");

        Hashtable pluginDatas = new Hashtable();
        pluginDatas.put(IMaiTai.KEY_SELECTED_MENU_ITEM, "search");
        pluginDatas.put(IMaiTai.KEY_ONE_BOX_ADDRESS, "1130 kifer rd");
        pluginDatas.put(IMaiTai.KEY_SELECTED_ADDRESS, stop);
        pluginDatas.put(IMaiTai.KEY_SEARCH_ITEM, "coffee");
        model.put(IPluginConstants.KEY_O_PLUGIN_REQUEST, pluginDatas);

        PowerMock.replayAll();
        model.doActionDelegate(IPluginConstants.ACTION_INIT);

        Assert.assertEquals(model.get(IPluginConstants.KEY_S_PLUGIN_ACTION), "search");
        Assert.assertEquals(model.get(IPluginConstants.KEY_S_COMMON_SEARCH_TEXT),
            "coffee");
        Assert.assertEquals(model.get(IPluginConstants.KEY_O_VALIDATED_ADDRESS), stop);

        PowerMock.verifyAll();
    }*/

    @Test
    public void testActionInit_noAddress()
    {
        initModel(null);

        Stop stop = new Stop();
        stop.setCity("sunnyvale");

        Hashtable pluginDatas = new Hashtable();
        pluginDatas.put(IMaiTai.KEY_SELECTED_MENU_ITEM, "search");
        pluginDatas.put(IMaiTai.KEY_ONE_BOX_ADDRESS, "1130 kifer rd");
        pluginDatas.put(IMaiTai.KEY_SEARCH_ITEM, "coffee");
        model.put(IPluginConstants.KEY_O_PLUGIN_REQUEST, pluginDatas);

        PowerMock.replayAll();
        model.doActionDelegate(IPluginConstants.ACTION_INIT);

        Assert.assertEquals(model.get(IPluginConstants.KEY_S_PLUGIN_ACTION), "search");
        Assert.assertEquals(model.get(IPluginConstants.KEY_S_COMMON_SEARCH_TEXT),
            "coffee");

        PowerMock.verifyAll();
    }

    @Test
    public void testActionInit_noAction()
    {
        initModel(null);

        Stop stop = new Stop();
        stop.setCity("sunnyvale");

        Hashtable pluginDatas = new Hashtable();
        pluginDatas.put(IMaiTai.KEY_ONE_BOX_ADDRESS, "1130 kifer rd");
        pluginDatas.put(IMaiTai.KEY_SEARCH_ITEM, "coffee");
        model.put(IPluginConstants.KEY_O_PLUGIN_REQUEST, pluginDatas);

        PowerMock.replayAll();
        model.doActionDelegate(IPluginConstants.ACTION_INIT);

        Assert.assertEquals(model.get(IPluginConstants.KEY_S_COMMON_SEARCH_TEXT),
            "coffee");

        PowerMock.verifyAll();
    }

    @Test
    public void testActionStartPlugin()
    {
        initModel(null);

        Vector address = new Vector();
        PluginAddress pluginAddress = new PluginAddress();
        pluginAddress.setAddressLine("3737000,-12111111");
        address.add(pluginAddress);

        model.put(IPluginConstants.KEY_S_PLUGIN_ACTION, IMaiTai.MENU_ITEM_DRIVE_TO);
        model.put(IPluginConstants.KEY_O_PLUGIN_ADDRESS, address);

        PowerMock.replayAll();
        model.doActionDelegate(IPluginConstants.ACTION_START_PLUGIN);

        Assert.assertNotNull(model.get(IPluginConstants.KEY_O_PLUGIN_RGC_ADDRESS));

        PowerMock.verifyAll();
    }

    /*@Test
    public void testActionGotoModule()
    {
        initModel(null);

        Stop stop = new Stop();
        stop.setCity("sunnyvale");

        model.put(IPluginConstants.KEY_O_VALIDATED_ADDRESS, stop);

        PowerMock.replayAll();
        model.doActionDelegate(IPluginConstants.ACTION_GOTO_MODULE);

        PowerMock.verifyAll();
    }*/

    @Test
    public void testActionGotoModule_noAddress() throws Exception
    {
        initModel(null);
        PowerMock.mockStatic(PluginManager.class);
        PluginManager pluginManager = PowerMock.createNiceMock(PluginManager.class);
        EasyMock.expect(PluginManager.getInstance()).andReturn(pluginManager);

        PowerMock.replayAll();
        model.doActionDelegate(IPluginConstants.ACTION_GOTO_MODULE);

        PowerMock.verifyAll();
    }

    @Test
    public void testActionDoCancel() throws Exception
    {
        initModel(null);

        PowerMock.replayAll();
        model.doActionDelegate(IPluginConstants.ACTION_JUMP_BACKGROUND);

        Assert.assertTrue(model.getBool(IPluginConstants.KEY_B_PLUGIN_GOTO_MODULE));
        PowerMock.verifyAll();
    }

    @Test
    public void testActionRgcAddress() throws Exception
    {
        initModel(null);

        Address address = new Address();
        Stop stop = new Stop();
        stop.setCity("sunnyvale");
        stop.setLat(3700000);
        stop.setLon(-12100000);
        address.setStop(stop);
        model.put(IPluginConstants.KEY_O_PLUGIN_RGC_ADDRESS, address);

        PowerMock.mockStatic(ServerProxyFactory.class);
        ServerProxyFactory serverProxyFactory = PowerMock
                .createMock(ServerProxyFactory.class);
        EasyMock.expect(ServerProxyFactory.getInstance()).andReturn(serverProxyFactory)
                .anyTimes();

        PowerMock.mockStatic(CommManager.class);
        CommManager commManager = PowerMock.createMock(CommManager.class);
        EasyMock.expect(CommManager.getInstance()).andReturn(commManager).anyTimes();
        Comm comm = PowerMock.createMock(Comm.class);
        EasyMock.expect(commManager.getComm()).andReturn(comm).anyTimes();
        IRgcProxy rgcProxy = PowerMock.createNiceMock(IRgcProxy.class);
        EasyMock.expect(serverProxyFactory.createRgcProxy(null, comm, model))
                .andReturn(rgcProxy).anyTimes();

        rgcProxy.requestRgc(EasyMock.anyInt(), EasyMock.anyInt());

        PowerMock.replayAll();
        model.doActionDelegate(IPluginConstants.ACTION_RGC_ADDRESS);

        PowerMock.verifyAll();
    }

    public void initModel(String[] methodName)
    {
        if (methodName == null)
        {
            model = new PluginModel();
        }
        else
        {
            try
            {
                model = PowerMock.createPartialMockAndInvokeDefaultConstructor(
                    PluginModel.class, methodName);
            }
            catch (Exception e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

}
