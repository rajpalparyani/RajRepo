/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * INavEngineListener.java
 *
 */
package com.telenav.module.nav;

import com.telenav.navsdk.nav.event.NavSdkNavEvent;

/**
 * Nav engine callback.
 * 
 *@author fqming (fqming@telenav.cn)
 *@date Aug 13, 2010
 */
public interface INavEngineListener
{
    /**
     * callback from nav engine.
     * 
     * @param navEvent
     */
    public void eventUpdate(NavSdkNavEvent navEvent);
}
