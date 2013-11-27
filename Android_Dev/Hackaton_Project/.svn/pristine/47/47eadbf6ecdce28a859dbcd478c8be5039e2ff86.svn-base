package com.telenav.navservice;

import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.easymock.EasyMock;
import org.json.tnme.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.telenav.navservice.model.App;
import com.telenav.navservice.policy.Policies;
import com.telenav.navservice.policy.PolicyRequester;

public class ControllerTest
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

    @Test
    public void testStart() throws Exception
    {
        ctrl.start();
        assertTrue(ctrl.isRunning);
        assertNotNull(ctrl.app);
        assertNotNull(ctrl.gpsBuffer);
        assertNotNull(ctrl.cellBuffer);
        assertNotNull(ctrl.serviceLocator);
        assertNotNull(ctrl.policyRequester);
        assertNotNull(ctrl.gpsRecorder);
        assertNotNull(ctrl.cellRecorder);
    }
    
    @Test(expected = IllegalStateException.class)
    public void testRestart() throws Exception
    {
        ctrl.start();
        assertTrue(ctrl.isRunning);
        
        ctrl.stop();
        ctrl.start();
    }
    
    @Test
    public void testAddJob() throws Exception
    {
        ControllerJob job = new ControllerJob();
        job.type = ControllerJob.SET_PARAM;
        ctrl.addJob(job);

        job = new ControllerJob();
        job.type = ControllerJob.CHANGE_CHARGING;
        ctrl.addJob(job);

        job = new ControllerJob();
        job.type = ControllerJob.DOWNLOAD_URLS;
        job.scheduledTime = 200;
        ctrl.addJob(job);
        
        job = new ControllerJob();
        job.type = ControllerJob.DOWNLOAD_POLICIES;
        job.scheduledTime = 100;
        ctrl.addJob(job);
        
        job = new ControllerJob();
        job.type = ControllerJob.STOP_SERVICE;
        ctrl.addJob(job);

        job = new ControllerJob();
        job.type = ControllerJob.SET_PARAM;
        job.scheduledTime = 200;
        ctrl.addJob(job);
        
        job = (ControllerJob)ctrl.jobQueue.elementAt(0);
        assertEquals(job.type, ControllerJob.SET_PARAM);
        job = (ControllerJob)ctrl.jobQueue.elementAt(1);
        assertEquals(job.type, ControllerJob.CHANGE_CHARGING);
        job = (ControllerJob)ctrl.jobQueue.elementAt(2);
        assertEquals(job.type, ControllerJob.STOP_SERVICE);
        job = (ControllerJob)ctrl.jobQueue.elementAt(3);
        assertEquals(job.type, ControllerJob.DOWNLOAD_POLICIES);
        job = (ControllerJob)ctrl.jobQueue.elementAt(4);
        assertEquals(job.type, ControllerJob.DOWNLOAD_URLS);
        job = (ControllerJob)ctrl.jobQueue.elementAt(5);
        assertEquals(job.type, ControllerJob.SET_PARAM);
    }
    
    @Test
    public void testHandlePolicies() throws Exception
    {
        Policies p1 = new PolicyRequester()
        {
            public Policies convertPolicies(JSONObject json) throws Exception
            {
                return super.convertPolicies(json);
            }
        }.convertPolicies(new JSONObject(s1));
        
        Policies p2 = new PolicyRequester()
        {
            public Policies convertPolicies(JSONObject json) throws Exception
            {
                return super.convertPolicies(json);
            }
        }.convertPolicies(new JSONObject(s2));
        
        ctrl.app.setForeground(false);
        ctrl.app.setCharging(false);
        ctrl.app.setRouteId("-1");
        ctrl.handlePolicies(p1);
        assertNull(ctrl.currentCPolicy);
        assertNull(ctrl.currentGPolicy);
        assertNull(ctrl.locationSender);
     
        ctrl.app.setForeground(true);
        ctrl.handlePolicies(p1);
        assertNotNull(ctrl.currentCPolicy);
        assertNull(ctrl.currentGPolicy);
        assertNotNull(ctrl.locationSender);
        assertTrue(ctrl.locationSender.isRunning());
        
        ctrl.app.setForeground(false);
        ctrl.app.setCharging(true);
        ctrl.app.setRouteId("1");
        ctrl.handlePolicies(p1);
        assertNull(ctrl.currentCPolicy);
        assertNotNull(ctrl.currentGPolicy);
        assertNotNull(ctrl.locationSender);
        assertTrue(ctrl.locationSender.isRunning());

        ctrl.handlePolicies(p2);
        assertNull(ctrl.currentCPolicy);
        assertNull(ctrl.currentGPolicy);
        assertNull(ctrl.locationSender);
        
        ctrl.app.setForeground(true);
        ctrl.handlePolicies(p2);
        assertNotNull(ctrl.currentCPolicy);
        assertNull(ctrl.currentGPolicy);
        assertNotNull(ctrl.locationSender);
        assertTrue(ctrl.locationSender.isRunning());
    }
    
    @Test
    public void testHandlePolicies_Same() throws Exception
    {
        //p1 and p2 are same
        Policies p1 = new PolicyRequester()
        {
            public Policies convertPolicies(JSONObject json) throws Exception
            {
                return super.convertPolicies(json);
            }
        }.convertPolicies(new JSONObject(s1));
        
        Policies p2 = new PolicyRequester()
        {
            public Policies convertPolicies(JSONObject json) throws Exception
            {
                return super.convertPolicies(json);
            }
        }.convertPolicies(new JSONObject(s1));
        
        ctrl.app.setForeground(true);
        ctrl.handlePolicies(p1);
        assertNotNull(ctrl.currentCPolicy);
        assertNull(ctrl.currentGPolicy);
        assertNotNull(ctrl.locationSender);
        assertTrue(ctrl.locationSender.isRunning());

        ctrl.handlePolicies(p2);
        assertNotNull(ctrl.currentCPolicy);
        assertNull(ctrl.currentGPolicy);
        assertNotNull(ctrl.locationSender);
        assertTrue(ctrl.locationSender.isRunning());
    }
    
    @Test
    public void testHandleRequestPolicies() throws Exception
    {
        Policies p1 = new PolicyRequester()
        {
            public Policies convertPolicies(JSONObject json) throws Exception
            {
                return super.convertPolicies(json);
            }
        }.convertPolicies(new JSONObject(s1));
        
        PolicyRequester req = EasyMock.createMock(PolicyRequester.class);
        expect(req.getPolicies((String)anyObject())).andReturn(p1).anyTimes();
        replay(req);
        
        ctrl.policyRequester = req;
        ctrl.handleRequestPolicies();
        assertEquals(ctrl.jobQueue.size(), 0);
    }
    
    @Test
    public void testHandleRequestPolicies_Fail() throws Exception
    {
        PolicyRequester req = EasyMock.createMock(PolicyRequester.class);
        expect(req.getPolicies((String)anyObject())).andReturn(null).anyTimes();
        replay(req);
        
        ctrl.policyRequester = req;
        ctrl.handleRequestPolicies();
        assertEquals(ctrl.jobQueue.size(), 1);
        ControllerJob job = (ControllerJob)ctrl.jobQueue.elementAt(0);
        assertEquals(job.type, ControllerJob.DOWNLOAD_POLICIES);
    }
    
    @Test
    public void testHandleRequestUrls() throws Exception
    {
        ServiceLocator locator = EasyMock.createMock(ServiceLocator.class);
        expect(locator.getNavServiceUrls((String)anyObject(), (App)anyObject())).andReturn(new String[]{"", ""}).anyTimes();
        replay(locator);
        
        ctrl.serviceLocatorUrl = "http://testurl";
        ctrl.serviceLocator = locator;
        ctrl.handleRequestUrls();
        assertEquals(ctrl.jobQueue.size(), 1);
        ControllerJob job = (ControllerJob)ctrl.jobQueue.elementAt(0);
        assertEquals(job.type, ControllerJob.DOWNLOAD_POLICIES);
    }
    
    @Test
    public void testHandleRequestUrls_Fail() throws Exception
    {
        ServiceLocator locator = EasyMock.createMock(ServiceLocator.class);
        expect(locator.getNavServiceUrls((String)anyObject(), (App)anyObject())).andReturn(null).anyTimes();
        replay(locator);
        
        ctrl.serviceLocatorUrl = "http://testurl";
        ctrl.serviceLocator = locator;
        ctrl.handleRequestUrls();
        assertEquals(ctrl.jobQueue.size(), 1);
        ControllerJob job = (ControllerJob)ctrl.jobQueue.elementAt(0);
        assertEquals(job.type, ControllerJob.DOWNLOAD_URLS);
    }
}
