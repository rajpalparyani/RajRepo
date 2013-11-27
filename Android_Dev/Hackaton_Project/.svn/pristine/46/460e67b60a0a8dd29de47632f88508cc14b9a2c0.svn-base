/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * J2seRadioManager.java
 *
 */
package com.telenav.radio.j2se;

import java.util.Hashtable;

import com.telenav.radio.ITnCoverageListener;
import com.telenav.radio.TnCellInfo;
import com.telenav.radio.TnRadioManager;
import com.telenav.radio.TnWifiInfo;

/**
 * Provides access to information about the radio services on the device at j2se platform.
 * 
 *@author fqming (fqming@telenav.cn)
 *@date Aug 25, 2010
 */
public class J2seRadioManager extends TnRadioManager
{
    protected Hashtable listeners;
    
    /**
     * Construct the radio manager at j2se platform.
     */
    public J2seRadioManager()
    {
        
    }
    
    public void addListener(ITnCoverageListener coverageListener)
    {
        if(coverageListener == null)
            return;
        
        if(listeners == null)
        {
            listeners = new Hashtable();
        }
        
        listeners.put(coverageListener, coverageListener);
    }

    public TnCellInfo getCellInfo()
    {
        TnCellInfo tnCellInfo = new TnCellInfo();
        
        //TODO
        
        return tnCellInfo;
    }

    public TnWifiInfo getWifiInfo()
    {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean isRoaming()
    {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean isWifiConnected()
    {
        // TODO Auto-generated method stub
        return false;
    }

    public void removeListener(ITnCoverageListener coverageListener)
    {
        if(coverageListener == null || listeners == null)
            return;
        
        listeners.remove(coverageListener);
    }

    public boolean isNetworkConnected()
    {
        // TODO Auto-generated method stub
        return true;
    }

    public boolean isAirportMode()
    {
        return false;
    }
}
