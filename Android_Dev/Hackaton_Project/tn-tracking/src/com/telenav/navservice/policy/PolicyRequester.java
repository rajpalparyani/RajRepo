package com.telenav.navservice.policy;

import java.util.Enumeration;

import org.json.tnme.JSONArray;
import org.json.tnme.JSONObject;

import com.telenav.logger.Logger;
import com.telenav.navservice.network.TnNetwork;

public class PolicyRequester
{
    public static final String KEY_COLLECTING = "enable";
    public static final String KEY_REPORTING_INTERVAL = "r-interval";
    public static final String KEY_SAMPLE_SIZE = "sampleSize";
    public static final String KEY_SAMPLING_INTERVAL = "s-interval";
    public static final String KEY_COLLECT_STATIONARY = "stop";
    // stationary speed in m/s, that how it is defined in policy doku
    private static final String KEY_STATIONARY_SPEED = "maxStop";
    private static final double MIN_SPEED_MS = 1.0;
    private static final String KEY_STATIONARY_STOP_TIME = "stopTime";
    private static final String KEY_CHECK_FREQUENCY = "check";
    private static final String KEY_ATTACH_CELLID = "attachC";
    private static final String KEY_ATTACH_GPS = "attachG";
    private static final String KEY_POLICY_TYPES = "policyTypes";
    
    // policy type related constants
    private static final String ON = "on"; 
    private static final String OFF = "off"; 
    private static final String CHARGER = "wire"; 
    private static final String FOREGROUND = "main"; 
    private static final String DRIVE_TO = "road";
    
    private static final String POLICY = "Policy";
    public static final String KEY_VALIDITY_PERIOD = "validityPeriod";
    public static final String KEY_VERSION = "version";
    private static final long ONE_DAY = 24 * 60 * 60;
    
    protected TnNetwork network = new TnNetwork();
    
    public Policies getPolicies(String policyUrl)
    {
        try{
            byte[] bytes = network.sendHttpGet(policyUrl);
            if (bytes == null || bytes.length == 0)
                return null;
            
            String fromServer = new String(bytes);
            
            //for debug only
//            fromServer = "{\"validityPeriod\":86400,\n\"version\":\"1.1\",\n"+
//                "\"CPolicy\":[\n{\n    \"enable\":\"false\",\n"+
//                "    \"policyTypes\":[\n"+
//                "        {\"main\":\"on\",\"wire\":\"off\",\"road\":\"off\"},\n"+
//                "        {\"main\":\"on\",\"wire\":\"on\",\"road\":\"off\"},\n"+
//                "        {\"main\":\"on\",\"wire\":\"off\",\"road\":\"on\"},\n"+
//                "        {\"main\":\"on\",\"wire\":\"on\",\"road\":\"on\"}],\n"+
//                "    \"sampleSize\":1,\n    \"r-interval\":300,\n    \"s-interval\":300,\n    \"stop\":\"true\",\n    \"maxStop\":5,\n    \"stopTime\":3600,\n    \"check\":1800,\n    \"attachG\":\"false\"\n}],\n"+
//                "\"GPolicy\":[\n{\n    \"enable\":\"true\",\n"+
//                "    \"policyTypes\":[\n"+
//                "      {\"main\":\"on\",\"wire\":\"on\",\"road\":\"on\"},\n"+
//                "      {\"main\":\"on\",\"wire\":\"off\",\"road\":\"on\"}],\n"+
//                "    \"sampleSize\":5,\n\"r-interval\":60,\n\"s-interval\":60,\n\"stop\":\"true\",\n\"maxStop\":1,\n\"stopTime\":60,\n\"check\":30,\n\"attachC\":\"true\"\n}\n]\n}";
            
            JSONObject result = new JSONObject(fromServer);
            return convertPolicies(result);
        }
        catch(Exception e)
        {
            Logger.log(this.getClass().getName(), e);
        }
        return null;
    }
    
    protected Policies convertPolicies(JSONObject json) throws Exception
    {
        Policies policies = new Policies();
        if (json.has(KEY_VALIDITY_PERIOD)) 
        {
            long validityPeriod = json.optLong(KEY_VALIDITY_PERIOD);
            policies.setValidityPeriod(validityPeriod);
        }
        else
        {
            policies.setValidityPeriod(ONE_DAY);
        }
        
        for (Enumeration keys = json.keys(); keys.hasMoreElements();) 
        {
            String key = (String) keys.nextElement();
            if (key.endsWith(POLICY)) 
            {
                JSONArray pArr = json.getJSONArray(key);
                for(int i=0; i<pArr.length(); i++)
                {
                    Policy policy = convertPolicy(key, pArr.getJSONObject(i));
                    policies.addPolicy(policy);
                }
            }
        }
        
        return policies;
    }
    
    protected Policy convertPolicy(String key, JSONObject json) throws Exception
    {
        Policy policy = new Policy();
        
        if (key.startsWith("C"))
            policy.policyType = Policy.POLICY_TYPE_CELL;
        else 
            policy.policyType = Policy.POLICY_TYPE_GPS;
            
        if (json.has(KEY_POLICY_TYPES))
        {
            JSONArray types = json.getJSONArray(KEY_POLICY_TYPES);
            int[] pArr = new int[types.length()];
            for(int i=0; i< types.length(); i++)
            {
                JSONObject type = types.getJSONObject(i);
                pArr[i] = Policy.calcPolicyMode(
                        ON.equals(type.get(FOREGROUND)), 
                        ON.equals(type.get(CHARGER)), 
                        ON.equals(type.get(DRIVE_TO)));
            }
            policy.policyModes = pArr;
        }
        
        policy.isEnabled = json.optBoolean(KEY_COLLECTING, true);
        policy.sampleSize = json.optInt(KEY_SAMPLE_SIZE, 5);
        policy.sampleInterval = json.optInt(KEY_SAMPLING_INTERVAL, 30);
        policy.reportInterval = json.optInt(KEY_REPORTING_INTERVAL, 30);
        policy.runningWhenStationary = json.optBoolean(KEY_COLLECT_STATIONARY, true);
        policy.stationarySpeed = json.optInt(KEY_STATIONARY_SPEED, 1);
        policy.stationaryTime = json.optInt(KEY_STATIONARY_STOP_TIME, 3600);
        policy.stationaryCheckInterval = json.optInt(KEY_CHECK_FREQUENCY, 1800);
        policy.isAttachCellLocation = json.optBoolean(KEY_ATTACH_CELLID, true);
        
        return policy;
    }
    
}
