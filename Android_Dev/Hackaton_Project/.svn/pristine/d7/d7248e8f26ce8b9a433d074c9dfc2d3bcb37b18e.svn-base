package com.telenav.navservice.policy;

import java.util.Vector;

public class Policies
{
    protected long expiration, validityPeriod;
    
    protected Vector policies = new Vector();
    
    
    public long getExpiration()
    {
        return expiration;
    }

    public void setValidityPeriod(long validityPeriod)
    {
        this.validityPeriod = validityPeriod;
        this.expiration = System.currentTimeMillis() + validityPeriod * 1000;
    }

    public void addPolicy(Policy policy)
    {
        policies.addElement(policy);
    }
    
    public Policy getPolicy(int index)
    {
        return (Policy)policies.elementAt(index);
    }
    
    public Vector findPolicies(int policyType, int policyMode)
    {
        Vector v = new Vector();
        synchronized(policies)
        {
            for (int i=0; i<policies.size(); i++)
            {
                Policy p = (Policy)policies.elementAt(i);
                if (p.policyType == policyType && p.policyModes != null)
                {
                    for (int j=0; j<p.policyModes.length; j++)
                    {
                        if (policyMode == p.policyModes[j])
                        {
                            v.addElement(p);
                            break;
                        }
                    }
                }
            }
        }
        return v;
    }
    
    public int size()
    {
        return policies.size();
    }
    
    public void clear()
    {
        policies.removeAllElements();
    }
    
    //It's not supposed to be used as a key
    public int hashCode()
    {
        return super.hashCode();
    }
    
    public boolean equals(Object o)
    {
        if (o == null || !(o instanceof Policies))
            return false;
        
        Policies po = (Policies)o;
        if (this.expiration != po.expiration)
            return false;
        
        if (this.policies != null && po.policies == null
                || this.policies == null && po.policies != null)
            return false;
        
        if (this.policies != null && po.policies != null)
        {
            if (this.policies.size() != po.policies.size())
                return false;
            
            for (int i=0; i<this.policies.size(); i++)
            {
                Policy p1 = (Policy)this.policies.elementAt(i);
                Policy p2 = (Policy)po.policies.elementAt(i);
                if (!p1.equals(p2))
                    return false;
            }
        }
        
        return true;
    }
    
    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        sb.append("{\n");
        sb.append("\t\"validityPeroid\":"+validityPeriod+",\n\n");
        for (int i=0; i<policies.size(); i++)
        {
            Policy p = (Policy)policies.elementAt(i);
            sb.append(p.toString());
            if (i < policies.size() - 1)
                sb.append(",");
            sb.append("\n");
        }
        sb.append("}");
        return sb.toString();
    }
}
