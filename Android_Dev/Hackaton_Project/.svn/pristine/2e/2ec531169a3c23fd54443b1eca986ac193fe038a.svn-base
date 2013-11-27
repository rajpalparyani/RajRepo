package com.telenav.navservice.policy;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Vector;

import org.json.tnme.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.telenav.navservice.TestUtil;

public class PoliciesTest
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
    public void testFindPolicies() throws Exception
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
         
         Vector v = policies.findPolicies(Policy.POLICY_TYPE_GPS, 8);
         assertEquals(v.size(), 1);
         Policy p = (Policy)v.elementAt(0);
         assertEquals(p.sampleSize, 20);
         
         v = policies.findPolicies(Policy.POLICY_TYPE_GPS, 9);
         assertEquals(v.size(), 1);
         p = (Policy)v.elementAt(0);
         assertEquals(p.sampleSize, 20);
         
         v = policies.findPolicies(Policy.POLICY_TYPE_GPS, 1);
         assertEquals(v.size(), 1);
         p = (Policy)v.elementAt(0);
         assertEquals(p.sampleSize, 60);

         v = policies.findPolicies(Policy.POLICY_TYPE_GPS, 4);
         assertEquals(v.size(), 0);

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
        Policies p1 = req.convertPolicies(new JSONObject(s));
        Policies p2 = req.convertPolicies(new JSONObject(s));
        //Because the expiration depends on the current time, we can't test if two policies are same using this way.
        //assertTrue(p1.equals(p2));
        assertFalse(p1.equals(null));
        
        p2.setValidityPeriod(100);
        assertFalse(p1.equals(p2));
        
        s = "{\n"+
        "\"validityPeriod\":86400,\n"+
        "\"version\":\"1.0\",\n\n"+
     
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
        
        Policies p3 = req.convertPolicies(new JSONObject(s));
        assertFalse(p1.equals(p3));
        
        s = "{\n"+
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
        Policies p4 = req.convertPolicies(new JSONObject(s));
        assertFalse(p1.equals(p4));
    }
}
