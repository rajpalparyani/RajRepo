package com.telenav.navservice.policy;

public class Policy
{
    public static final int POLICY_TYPE_CELL            = 1;
    public static final int POLICY_TYPE_GPS             = 2;
    
    public static final int CHARGER                     = 1;
    public static final int FOREGROUND                  = 4;
    public static final int DRIVE_TO                    = 8;
    
    public int policyType;
    public int[] policyModes;
    public boolean isEnabled = false;
    public int sampleInterval = 30;
    public int reportInterval = 30;
    public int sampleSize = 5;
    public boolean runningWhenStationary = true;
    public float stationarySpeed = 1;
    public int stationaryTime = 3600;
    public int stationaryCheckInterval = 1800;
    public boolean isAttachCellLocation = true;
    
    //It's not supposed to be used as a key
    public int hashCode()
    {
        return super.hashCode();
    }
    
    public boolean equals(Object o)
    {
        if (!(o instanceof Policy))
            return false;
        
        Policy p = (Policy)o;
        if (policyType != p.policyType)
            return false;
        
        if (!arrayEqual(policyModes, p.policyModes))
            return false;
        
        if (isEnabled != p.isEnabled)
            return false;
        
        if (sampleInterval != p.sampleInterval)
            return false;
        
        if (reportInterval != p.reportInterval)
            return false;
        
        if (sampleSize != p.sampleSize)
            return false;
        
        if (runningWhenStationary != p.runningWhenStationary)
            return false;
        
        if (Math.abs(stationarySpeed - p.stationarySpeed) > 0.00001f)
            return false;
        
        if (stationaryTime != p.stationaryTime)
            return false;
        
        if (stationaryCheckInterval != p.stationaryCheckInterval)
            return false;
        
        if (isAttachCellLocation != p.isAttachCellLocation)
            return false;
        
        return true;
    }
    
    protected boolean arrayEqual(int[] a, int[] b)
    {
        if (a == null && b == null)
            return true;
        
        if (a != null && b != null && a.length == b.length)
        {
            for (int i=0; i<a.length; i++)
            {
                if (a[i] != b[i])
                    return false;
            }
            return true;
        }
        
        return false;
    }
    
    public static int calcPolicyMode(boolean isForeground, boolean isCharging, boolean isDriveTo)
    {
        int mode = 0;
        
        if (isForeground)
            mode |= FOREGROUND;
        
        if (isCharging)
            mode |= CHARGER;
        
        if (isDriveTo)
            mode |= DRIVE_TO;
        
        return mode;
    }
    
    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        if (policyType == POLICY_TYPE_CELL)
            sb.append("\"CPolicy\":[\n");
        else
            sb.append("\"GPolicy\":[\n");
        sb.append("\t{\n");
        sb.append("\t\"enabled\":\""+isEnabled+"\",\n");
        sb.append("\t\"policyTypes\":[\n");
        sb.append(convertPolicyModes(policyModes));
        sb.append("],\n");
        sb.append("\t\"sampleSize\":"+sampleSize+",\n");
        sb.append("\t\"r-interval\":"+reportInterval+",\n");
        sb.append("\t\"s-interval\":"+sampleInterval+",\n");
        sb.append("\t\"stop\":\""+runningWhenStationary+"\",\n");
        sb.append("\t\"maxStop\":"+stationarySpeed+",\n");
        sb.append("\t\"stopTime\":"+stationaryTime+",\n");
        sb.append("\t\"check\":"+stationaryCheckInterval);
        if (policyType == POLICY_TYPE_GPS)
        {
            sb.append(",\n");
            sb.append("\t\"attachC\":\""+isAttachCellLocation+"\"");
        }
        sb.append("\n}]");
            
        return sb.toString();
    }
    
    protected String convertPolicyModes(int[] policyModes)
    {
        if (policyModes == null || policyModes.length == 0)
            return "";
        
        StringBuffer sb = new StringBuffer();
        for (int i=0; i<policyModes.length; i++)
        {
            sb.append(convertPolicyMode(policyModes[i]));
            if (i < policyModes.length - 1)
            {
                sb.append(",");
                sb.append("\n");
            }
        }
        return sb.toString();
    }
    
    protected String convertPolicyMode(int policyMode)
    {
        String main, wire, road;
        if ((policyMode & CHARGER) == 0)
        {
            wire = "off";
        }
        else
        {
            wire = "on";
        }
        if ((policyMode & FOREGROUND) == 0)
        {
            main = "off";
        }
        else
        {
            main = "on";
        }
        if ((policyMode & DRIVE_TO) == 0)
        {
            road = "off";
        }
        else
        {
            road = "on";
        }
        
        return "\t\t{\"main\":\""+main+"\",\"wire\":\""+wire+"\",\"road\":\""+road+"\"}";
    }
}