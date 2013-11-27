package com.telenav.navservice.policy;

import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.json.tnme.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.telenav.navservice.TestUtil;
import com.telenav.navservice.network.TnNetwork;

public class PolicyRequesterTest
{

    @BeforeClass
    public static void setUpClass() throws Exception
    {
        TestUtil.initLog();
    }
    
    @Before
    public void setUp() throws Exception
    {
    }

    @After
    public void tearDown() throws Exception
    {
    }

    @Test
    public void testGetPolicies() throws Exception
    {
        String s = "{\n"+
        "\"validityPeriod\":86400,\n"+
        "\"version\":\"1.0\",\n\n"+
     
        "\"CPolicy\":[\n"+
             "{\n"+
              "\"enable\":\"false\",\n"+
              "\"policyTypes\":[\n"+
                   "{\"main\":\"on\",\"wire\":\"off\",\"road\":\"off\"},\n"+
                   "{\"main\":\"on\",\"wire\":\"on\",\"road\":\"off\"},\n"+
                   "{\"main\":\"on\",\"wire\":\"off\",\"road\":\"on\"},\n"+
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
                  "{\"main\":\"off\",\"wire\":\"off\",\"road\":\"off\"},\n"+
                  "{\"main\":\"off\",\"wire\":\"on\",\"road\":\"off\"},\n"+
                  "{\"main\":\"off\",\"wire\":\"off\",\"road\":\"on\"},\n"+
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

        TnNetwork network = createMock(TnNetwork.class);
        expect(network.sendHttpGet((String)anyObject())).andReturn(s.getBytes()).anyTimes();
        replay(network);
        
        PolicyRequester req = new PolicyRequester();
        req.network = network;
        Policies policies = req.getPolicies("");
        assertEquals(policies.getExpiration() - System.currentTimeMillis(), (long)86400*1000, 10000);
        assertEquals(policies.policies.size(), 2);
    }
    
    @Test
    public void testConvertPolicies() throws Exception
    {
        String s = "{\n"+
           "\"validityPeriod\":86400,\n"+
           "\"version\":\"1.0\",\n\n"+
        
           "\"CPolicy\":[\n"+
                "{\n"+
                 "\"enable\":\"false\",\n"+
                 "\"policyTypes\":[\n"+
                      "{\"main\":\"on\",\"wire\":\"off\",\"road\":\"off\"},\n"+
                      "{\"main\":\"on\",\"wire\":\"on\",\"road\":\"off\"},\n"+
                      "{\"main\":\"on\",\"wire\":\"off\",\"road\":\"on\"},\n"+
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
                     "{\"main\":\"off\",\"wire\":\"off\",\"road\":\"off\"},\n"+
                     "{\"main\":\"off\",\"wire\":\"on\",\"road\":\"off\"},\n"+
                     "{\"main\":\"off\",\"wire\":\"off\",\"road\":\"on\"},\n"+
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
        
        PolicyRequester req = new PolicyRequester();
        Policies policies = req.convertPolicies(new JSONObject(s));
        
        assertEquals(policies.getExpiration() - System.currentTimeMillis(), (long)86400*1000, 10000);
        assertEquals(policies.policies.size(), 2);
        
        Policy p1 = policies.getPolicy(0);
        assertEquals(p1.policyType, Policy.POLICY_TYPE_CELL);
        assertFalse(p1.isEnabled);
        assertArrayEquals(p1.policyModes, new int[]{4, 5, 12, 13});
        assertEquals(p1.sampleSize, 1);
        assertEquals(p1.sampleInterval, 300);
        assertEquals(p1.reportInterval, 300);
        assertEquals(p1.runningWhenStationary, true);
        assertEquals(p1.stationarySpeed, 5, 0.0001);
        assertEquals(p1.stationaryTime, 3600);
        assertEquals(p1.stationaryCheckInterval, 1800);
        
        Policy p2 = policies.getPolicy(1);
        assertEquals(p2.policyType, Policy.POLICY_TYPE_GPS);
        assertTrue(p2.isEnabled);
        assertArrayEquals(p2.policyModes, new int[]{0, 1, 8, 9});
        assertEquals(p2.sampleSize, 60);
        assertEquals(p2.sampleInterval, 60);
        assertEquals(p2.reportInterval, 60);
        assertEquals(p2.runningWhenStationary, false);
        assertEquals(p2.stationarySpeed, 2, 0.0001);
        assertEquals(p2.stationaryTime, 3600);
        assertEquals(p2.stationaryCheckInterval, 36000);
        assertEquals(p2.isAttachCellLocation, false);
    }
    
    @Test
    public void testConvertPolicies_NoValidityPeroid() throws Exception
    {
        String s = "{\n"+
           "\"version\":\"1.0\",\n\n"+
        
           "\"CPolicy\":[\n"+
                "{\n"+
                 "\"enable\":\"false\",\n"+
                 "\"policyTypes\":[\n"+
                      "{\"main\":\"on\",\"wire\":\"off\",\"road\":\"off\"},\n"+
                      "{\"main\":\"on\",\"wire\":\"on\",\"road\":\"off\"},\n"+
                      "{\"main\":\"on\",\"wire\":\"off\",\"road\":\"on\"},\n"+
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
                     "{\"main\":\"off\",\"wire\":\"off\",\"road\":\"off\"},\n"+
                     "{\"main\":\"off\",\"wire\":\"on\",\"road\":\"off\"},\n"+
                     "{\"main\":\"off\",\"wire\":\"off\",\"road\":\"on\"},\n"+
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
        
        PolicyRequester req = new PolicyRequester();
        Policies policies = req.convertPolicies(new JSONObject(s));
        
        assertEquals(policies.getExpiration() - System.currentTimeMillis(), (long)86400*1000, 10000);
        assertEquals(policies.policies.size(), 2);
    }
    
    @Test
    public void testConvertPolicies_CombinedPolicies() throws Exception
    {
        String s = "{\n"+
           "\"validityPeriod\":86400,\n"+
           "\"version\":\"1.0\",\n\n"+
        
           "\"CPolicy\":[\n"+
                "{\n"+
                 "\"enable\":\"false\",\n"+
                 "\"policyTypes\":[\n"+
                      "{\"main\":\"on\",\"wire\":\"off\",\"road\":\"off\"},\n"+
                      "{\"main\":\"on\",\"wire\":\"on\",\"road\":\"off\"}],\n"+
                 "\"sampleSize\":1,\n"+
                 "\"r-interval\":300,\n"+
                 "\"s-interval\":300,\n"+
                 "\"stop\":\"true\",\n"+
                 "\"maxStop\":5,\n"+
                 "\"stopTime\":3600,\n"+
                 "\"check\":1800,\n"+
                 "\"attachG\":\"false\"\n"+
                "},\n" +
                "{\n"+
                "\"enable\":\"false\",\n"+
                "\"policyTypes\":[\n"+
                     "{\"main\":\"on\",\"wire\":\"off\",\"road\":\"on\"},\n"+
                     "{\"main\":\"on\",\"wire\":\"on\",\"road\":\"on\"}],\n"+
                "\"sampleSize\":1,\n"+
                "\"r-interval\":200,\n"+
                "\"s-interval\":200,\n"+
                "\"stop\":\"true\",\n"+
                "\"maxStop\":4,\n"+
                "\"stopTime\":600,\n"+
                "\"check\":900,\n"+
                "\"attachG\":\"false\"\n"+
               "}\n" +
                "],\n"+
        
             "\"GPolicy\":[\n"+
                 "{\n"+
                 "\"enable\":\"true\",\n"+
                 "\"policyTypes\":[\n"+
                     "{\"main\":\"off\",\"wire\":\"off\",\"road\":\"off\"},\n"+
                     "{\"main\":\"off\",\"wire\":\"on\",\"road\":\"off\"}],\n"+
                 "\"sampleSize\":60,\n"+
                 "\"r-interval\":60,\n"+
                 "\"s-interval\":60,\n"+
                 "\"stop\":\"false\",\n"+
                 "\"maxStop\":2,\n"+
                 "\"stopTime\":3600,\n"+
                 "\"check\":36000,\n"+
                 "\"attachC\":\"true\"\n"+
                 "},\n" +
                 "{\n"+
                 "\"enable\":\"true\",\n"+
                 "\"policyTypes\":[\n"+
                     "{\"main\":\"off\",\"wire\":\"off\",\"road\":\"on\"},\n"+
                     "{\"main\":\"off\",\"wire\":\"on\",\"road\":\"on\"}],\n"+
                 "\"sampleSize\":20,\n"+
                 "\"r-interval\":20,\n"+
                 "\"s-interval\":20,\n"+
                 "\"stop\":\"true\",\n"+
                 "\"maxStop\":1,\n"+
                 "\"stopTime\":1800,\n"+
                 "\"check\":18000,\n"+
                 "\"attachC\":\"false\"\n"+
                 "}\n" +
                 "]\n"+
         "}";
        
        PolicyRequester req = new PolicyRequester();
        Policies policies = req.convertPolicies(new JSONObject(s));
        
        assertEquals(policies.getExpiration() - System.currentTimeMillis(), (long)86400*1000, 10000);
        assertEquals(policies.policies.size(), 4);
        
        Policy p1 = policies.getPolicy(0);
        assertEquals(p1.policyType, Policy.POLICY_TYPE_CELL);
        assertFalse(p1.isEnabled);
        assertArrayEquals(p1.policyModes, new int[]{4, 5});
        assertEquals(p1.sampleSize, 1);
        assertEquals(p1.sampleInterval, 300);
        assertEquals(p1.reportInterval, 300);
        assertEquals(p1.runningWhenStationary, true);
        assertEquals(p1.stationarySpeed, 5, 0.0001);
        assertEquals(p1.stationaryTime, 3600);
        assertEquals(p1.stationaryCheckInterval, 1800);
        
        p1 = policies.getPolicy(1);
        assertEquals(p1.policyType, Policy.POLICY_TYPE_CELL);
        assertFalse(p1.isEnabled);
        assertArrayEquals(p1.policyModes, new int[]{12, 13});
        assertEquals(p1.sampleSize, 1);
        assertEquals(p1.sampleInterval, 200);
        assertEquals(p1.reportInterval, 200);
        assertEquals(p1.runningWhenStationary, true);
        assertEquals(p1.stationarySpeed, 4, 0.0001);
        assertEquals(p1.stationaryTime, 600);
        assertEquals(p1.stationaryCheckInterval, 900);
        
        Policy p2 = policies.getPolicy(2);
        assertEquals(p2.policyType, Policy.POLICY_TYPE_GPS);
        assertTrue(p2.isEnabled);
        assertArrayEquals(p2.policyModes, new int[]{0, 1});
        assertEquals(p2.sampleSize, 60);
        assertEquals(p2.sampleInterval, 60);
        assertEquals(p2.reportInterval, 60);
        assertEquals(p2.runningWhenStationary, false);
        assertEquals(p2.stationarySpeed, 2, 0.0001);
        assertEquals(p2.stationaryTime, 3600);
        assertEquals(p2.stationaryCheckInterval, 36000);
        assertEquals(p2.isAttachCellLocation, true);

        p2 = policies.getPolicy(3);
        assertEquals(p2.policyType, Policy.POLICY_TYPE_GPS);
        assertTrue(p2.isEnabled);
        assertArrayEquals(p2.policyModes, new int[]{8, 9});
        assertEquals(p2.sampleSize, 20);
        assertEquals(p2.sampleInterval, 20);
        assertEquals(p2.reportInterval, 20);
        assertEquals(p2.runningWhenStationary, true);
        assertEquals(p2.stationarySpeed, 1, 0.0001);
        assertEquals(p2.stationaryTime, 1800);
        assertEquals(p2.stationaryCheckInterval, 18000);
        assertEquals(p2.isAttachCellLocation, false);
    }
}
