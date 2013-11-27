package com.telenav.navservice;


import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.json.tnme.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.telenav.navservice.model.App;
import com.telenav.navservice.policy.Policies;
import com.telenav.navservice.policy.PolicyRequester;
import com.telenav.navservice.util.TrackingLogger;

public class ControllerTestTiming
{
    String s1 = "{\n"+
    "\"validityPeriod\":86400,\n"+
    "\"version\":\"1.0\",\n\n"+
 
    "\"CPolicy\":[\n"+
         "{\n"+
          "\"enable\":\"true\",\n"+
          "\"policyTypes\":[\n"+
               "{\"main\":\"on\",\"wire\":\"off\",\"road\":\"off\"}],\n"+
          "\"sampleSize\":1,\n"+
          "\"r-interval\":300,\n"+
          "\"s-interval\":300,\n"+
          "\"stop\":\"true\",\n"+
          "\"maxStop\":5,\n"+
          "\"stopTime\":3600,\n"+
          "\"check\":1800,\n"+
          "\"attachG\":\"false\"\n"+
         "}],\n"+
 
      "\"GPolicy\":[\n"+
     "{\n"+
          "\"enable\":\"true\",\n"+
          "\"policyTypes\":[\n"+
              "{\"main\":\"off\",\"wire\":\"on\",\"road\":\"on\"}],\n"+
          "\"sampleSize\":60,\n"+
          "\"r-interval\":60,\n"+
          "\"s-interval\":60,\n"+
          "\"stop\":\"false\",\n"+
          "\"maxStop\":2,\n"+
          "\"stopTime\":3600,\n"+
          "\"check\":36000,\n"+
          "\"attachC\":\"false\"\n"+
          "}]\n"+
     "}";
    
    String s2 = "{\n"+
    "\"validityPeriod\":86400,\n"+
    "\"version\":\"1.0\",\n\n"+
 
    "\"CPolicy\":[\n"+
         "{\n"+
          "\"enable\":\"true\",\n"+
          "\"policyTypes\":[\n"+
               "{\"main\":\"on\",\"wire\":\"on\",\"road\":\"on\"}],\n"+
          "\"sampleSize\":1,\n"+
          "\"r-interval\":300,\n"+
          "\"s-interval\":300,\n"+
          "\"stop\":\"true\",\n"+
          "\"maxStop\":5,\n"+
          "\"stopTime\":3600,\n"+
          "\"check\":1800,\n"+
          "\"attachG\":\"false\"\n"+
         "}],\n"+
 
      "\"GPolicy\":[\n"+
     "{\n"+
          "\"enable\":\"true\",\n"+
          "\"policyTypes\":[\n"+
              "{\"main\":\"off\",\"wire\":\"on\",\"road\":\"off\"}],\n"+
          "\"sampleSize\":60,\n"+
          "\"r-interval\":60,\n"+
          "\"s-interval\":60,\n"+
          "\"stop\":\"false\",\n"+
          "\"maxStop\":2,\n"+
          "\"stopTime\":3600,\n"+
          "\"check\":36000,\n"+
          "\"attachC\":\"false\"\n"+
          "}]\n"+
     "}";
    
    private Controller ctrl;

    @BeforeClass
    public static void setUpClass() throws Exception
    {
        TestUtil.initLog();
    }
    
    @Before
    public void setUp() throws Exception
    {
        ctrl = new Controller(new App(), new Controller.IControllerListener()
        {
            public void stopService()
            {
            }
        }, new IClientStateMonitor()
        {
            public boolean isClientRunning()
            {
                // TODO Auto-generated method stub
                return false;
            }
        });
    }

    @After
    public void tearDown() throws Exception
    {
        ctrl.stop();
    }

    @Test(timeout = 5000)
    public void testStop() throws Exception
    {
        ctrl.start();
        assertTrue(ctrl.isRunning);
        
        ctrl.stop();
        
        while(!ctrl.isStopped)
            Thread.sleep(5);
        
        assertFalse(ctrl.isRunning);
        assertTrue(ctrl.isStopped);
    }
    
    @Test(timeout = 10000)
    public void testSetParamters() throws Exception
    {
        ctrl.start();
        
        assertEquals("", ctrl.app.getAppName());
        NavServiceParameter param = new NavServiceParameter();
        String appName = "testdevice";
        param.setAppName(appName);
        ctrl.setParameters(param);
        while(!appName.equals(ctrl.app.getAppName()))
            Thread.sleep(5);
        assertEquals(appName, ctrl.app.getAppName());
        
        assertEquals("", ctrl.app.getAppVersion());
        param = new NavServiceParameter();
        String appVersion = "0.0.0";
        param.setAppVersion(appVersion);
        ctrl.setParameters(param);
        while(!appVersion.equals(ctrl.app.getAppVersion()))
            Thread.sleep(5);
        assertEquals(appName, ctrl.app.getAppName());
        assertEquals(appVersion, ctrl.app.getAppVersion());
        
        assertEquals("", ctrl.app.getCarrier());
        param = new NavServiceParameter();
        String carrierName = "Carrier";
        param.setCarrierName(carrierName);
        ctrl.setParameters(param);
        while(!carrierName.equals(ctrl.app.getCarrier()))
            Thread.sleep(5);
        assertEquals(appName, ctrl.app.getAppName());
        assertEquals(appVersion, ctrl.app.getAppVersion());
        assertEquals(carrierName, ctrl.app.getCarrier());
        
        assertFalse(ctrl.app.isForeground());
        param = new NavServiceParameter();
        param.setForeground(true);
        ctrl.setParameters(param);
        while(!ctrl.app.isForeground())
            Thread.sleep(5);
        assertTrue(ctrl.app.isForeground());

        assertFalse(TrackingLogger.isLogEnabled);
        param = new NavServiceParameter();
        param.setLogEnabled(true);
        ctrl.setParameters(param);
        while(!TrackingLogger.isLogEnabled)
            Thread.sleep(5);
        assertTrue(TrackingLogger.isLogEnabled);
        
        assertEquals("", ctrl.app.getRouteId());
        assertFalse(ctrl.app.isDriveTo());
        param = new NavServiceParameter();
        String routeId = "100";
        param.setRouteId(routeId);
        ctrl.setParameters(param);
        while(!routeId.equals(ctrl.app.getRouteId()))
            Thread.sleep(5);
        assertEquals(routeId, ctrl.app.getRouteId());
        assertTrue(ctrl.app.isDriveTo());

        param = new NavServiceParameter();
        routeId = "-1";
        param.setRouteId(routeId);
        ctrl.setParameters(param);
        while(!routeId.equals(ctrl.app.getRouteId()))
            Thread.sleep(5);
        assertEquals(routeId, ctrl.app.getRouteId());
        assertFalse(ctrl.app.isDriveTo());

        assertEquals("", ctrl.app.getUserId());
        param = new NavServiceParameter();
        String userId = "testUser";
        param.setUserId(userId);
        ctrl.setParameters(param);
        while(!userId.equals(ctrl.app.getUserId()))
            Thread.sleep(5);
        assertEquals(userId, ctrl.app.getUserId());

        assertNull(ctrl.serviceLocatorUrl);
        param = new NavServiceParameter();
        String url = "http://testurl.com/telenav-global/service-locator";
        ctrl.serviceLocatorUrl = url;
        param.setServerUrl(url);
        ctrl.setParameters(param);
        while(!url.equals(ctrl.serviceLocatorUrl))
            Thread.sleep(5);
        assertEquals(url, ctrl.serviceLocatorUrl);

        assertFalse(ctrl.isStopped);
        param = new NavServiceParameter();
        param.setForceStop(true);
        ctrl.setParameters(param);
        while(!ctrl.isStopped)
            Thread.sleep(5);
        assertTrue(ctrl.isStopped);
        assertFalse(ctrl.isRunning);
    }
    
    @Test(timeout = 5000)
    public void testSetCharging() throws Exception
    {
        ctrl.start();
        assertFalse(ctrl.app.isCharging());
        ctrl.setCharging(true);
        while(!ctrl.app.isCharging())
            Thread.sleep(5);
        assertTrue(ctrl.app.isCharging());
    }
    
    @Test(timeout = 5000)
    public void testStopService() throws Exception
    {
        ctrl.start();
        ctrl.stopService();
        while(!ctrl.isStopped)
            Thread.sleep(5);
        assertTrue(ctrl.isStopped);
        assertFalse(ctrl.isRunning);
    }
    
    @Test(timeout = 5000)
    public void testPolicyExpiration() throws Exception
    {
        Policies p1 = new PolicyRequester()
        {
            public Policies convertPolicies(JSONObject json) throws Exception
            {
                return super.convertPolicies(json);
            }
        }.convertPolicies(new JSONObject(s1));
        p1.setValidityPeriod(2);
        
        Policies p2 = new PolicyRequester()
        {
            public Policies convertPolicies(JSONObject json) throws Exception
            {
                return super.convertPolicies(json);
            }
        }.convertPolicies(new JSONObject(s2));
        
        PolicyRequester req = createMock(PolicyRequester.class);
        expect(req.getPolicies((String)anyObject())).andReturn(p2).anyTimes();
        
        ctrl.policyRequester = req;
        replay(req);
        
        ctrl.policies = p1;
        ctrl.start();
        
        while(!ctrl.policies.equals(p2))
            Thread.sleep(100);
        
        assertTrue(ctrl.policies.equals(p2));
    }

    @Test(timeout = 5000)
    public void testPolicyExpiration_ShortTime() throws Exception
    {
        Policies p1 = new PolicyRequester()
        {
            public Policies convertPolicies(JSONObject json) throws Exception
            {
                return super.convertPolicies(json);
            }
        }.convertPolicies(new JSONObject(s1));
        p1.setValidityPeriod(1);
        
        Policies p2 = new PolicyRequester()
        {
            public Policies convertPolicies(JSONObject json) throws Exception
            {
                return super.convertPolicies(json);
            }
        }.convertPolicies(new JSONObject(s2));
        
        PolicyRequester req = createMock(PolicyRequester.class);
        expect(req.getPolicies((String)anyObject())).andReturn(p2).anyTimes();
        
        ctrl.policyRequester = req;
        replay(req);
        
        ctrl.policies = p1;
        ctrl.start();
        
        while(!ctrl.policies.equals(p2))
            Thread.sleep(100);
        
        assertTrue(ctrl.policies.equals(p2));
    }
}
