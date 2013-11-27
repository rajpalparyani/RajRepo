/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * AndroidResourceProxy.java
 *
 */
package com.telenav.map.opengl.java.proxy;

import com.telenav.map.opengl.java.proxy.ITnMapProxyFactory;
import com.telenav.map.opengl.java.proxy.ITnMapResourceProxy;


/**
 *@author jyxu (jyxu@telenav.cn)
 *@date 2010-10-08
 */

public class TnMapProxyFactory implements ITnMapProxyFactory
{
    private ITnMapResourceProxy resourceProxy;
		    
	public ITnMapResourceProxy getResourceProxy()
	{
	    if (resourceProxy == null)
	    {
	        resourceProxy =  new TnMapResourceProxy();
	    }
	    return resourceProxy;
	}

}
