/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * TnNetworkInfo.java
 *
 */
package com.telenav.comm;

/**
 *@author yxyao
 *@date 2011-10-21
 */
public class TnNetworkInfo
{

    public static final int TYPE_MOBILE = 0;
    
    public static final int TYPE_WIFI = 1;
    
    public static final int TYPE_WIMAX = 2;
    
    private static final byte BIT_CONNECTED = 1;
    
    private static final byte BIT_AVAILALBE = 2;
    
    private static final byte BIT_ROAMING = 4;
    
    private static final byte BIT_FAILOVER = 8;
    
    private int type;
    
    private byte privateFlag;
    
    /**
     * 
     */
    public TnNetworkInfo()
    {
    }

    /**
     * Most case will be MOBILE, WIFI or WIMAX, but not limited.
     * <p>There will be special value for custom network type.</p> 
     * @return the type
     */
    public int getType()
    {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(int type)
    {
        this.type = type;
    }
    
    public boolean isConnected()
    {
        return (privateFlag & BIT_CONNECTED) == BIT_CONNECTED;
    }
    
    public boolean isAvailable()
    {
        return (privateFlag & BIT_AVAILALBE) == BIT_AVAILALBE;
    }
    
    public boolean isRoaming()
    {
        return (privateFlag & BIT_ROAMING) == BIT_ROAMING;
    }
    
    public boolean isFailOver()
    {
        return (privateFlag & BIT_FAILOVER) == BIT_FAILOVER;
    }
    
    public void setConnected(boolean isConnected)
    {
        writeBit(BIT_CONNECTED, isConnected);
    }
    
    public void setRoaming(boolean isRoaming)
    {
        writeBit(BIT_ROAMING, isRoaming);
    }
    
    public void setFailOver(boolean isFailOver)
    {
        writeBit(BIT_FAILOVER, isFailOver);
    }
    
    public void setAvailable(boolean isAvailable)
    {
        writeBit(BIT_AVAILALBE, isAvailable);
    }
    
    private void writeBit(byte flag, boolean setOrClear)
    {
        if(setOrClear)
        {
            privateFlag |= flag;
        }
        else
        {
            privateFlag &= ~flag;
        }
    }
    
}
