package com.telenav.navservice.policy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.json.tnme.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.telenav.navservice.TestUtil;

public class PolicyTest
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
    public void testEquals() throws Exception
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
         Policy p1 = policies.getPolicy(0);
         Policy p2 = policies.getPolicy(1);
         assertFalse(p1.equals(p2));
         
         s = "{\n"+
             "\"validityPeriod\":86400,\n"+
             "\"version\":\"1.0\",\n\n"+
          
             "\"CPolicy\":[\n"+
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
         
         req = new PolicyRequester();
         policies = req.convertPolicies(new JSONObject(s));
         p1 = policies.getPolicy(0);
         p1.policyType = Policy.POLICY_TYPE_GPS;
         p2 = policies.getPolicy(1);
         assertTrue(p1.equals(p2));
         
         assertFalse(p1.equals(new Object()));
         assertFalse(p1.equals(null));
    }
    
    @Test
    public void testArrayEqual() throws Exception
    {
        Policy p = new Policy();
        assertTrue(p.arrayEqual(null, null));
        assertFalse(p.arrayEqual(null, new int[]{}));
        assertFalse(p.arrayEqual(new int[]{}, null));
        assertTrue(p.arrayEqual(new int[]{}, new int[]{}));
        assertTrue(p.arrayEqual(new int[]{1, 2}, new int[]{1, 2}));
        assertFalse(p.arrayEqual(new int[]{1}, new int[]{1, 2}));
        assertFalse(p.arrayEqual(new int[]{1, 2}, new int[]{1, 3}));
    }
    
    @Test
    public void testCalcPolicyMode() throws Exception
    {
        assertEquals(Policy.calcPolicyMode(false, false, false), 0);
        assertEquals(Policy.calcPolicyMode(true, true, true), 13);
        assertEquals(Policy.calcPolicyMode(true, false, true), 12);
    }
    
    @Test
    public void testConvertPolicyMode() throws Exception
    {
        Policy p = new Policy();
        
        String s = p.convertPolicyMode(0);
        assertEquals(s, "\t\t{\"main\":\"off\",\"wire\":\"off\",\"road\":\"off\"}");
        
        s = p.convertPolicyMode(1);
        assertEquals(s, "\t\t{\"main\":\"off\",\"wire\":\"on\",\"road\":\"off\"}");

        s = p.convertPolicyMode(4);
        assertEquals(s, "\t\t{\"main\":\"on\",\"wire\":\"off\",\"road\":\"off\"}");
        
        s = p.convertPolicyMode(5);
        assertEquals(s, "\t\t{\"main\":\"on\",\"wire\":\"on\",\"road\":\"off\"}");
        
        s = p.convertPolicyMode(13);
        assertEquals(s, "\t\t{\"main\":\"on\",\"wire\":\"on\",\"road\":\"on\"}");
    }
    
    @Test
    public void testConvertPolicyModes() throws Exception
    {
        Policy p = new Policy();
        String s = p.convertPolicyModes(new int[]{1, 13});
        assertEquals(s, "\t\t{\"main\":\"off\",\"wire\":\"on\",\"road\":\"off\"},\n\t\t{\"main\":\"on\",\"wire\":\"on\",\"road\":\"on\"}");
    }
    
    @Test
    public void testConvertPolicyModes_Null() throws Exception
    {
        Policy p = new Policy();
        String s = p.convertPolicyModes(null);
        assertEquals(s, "");
    }
}
