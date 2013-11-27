/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * BrowserSession.java
 *
 */
package com.telenav.data.datatypes.browser;

import com.telenav.app.CommManager;
import com.telenav.comm.Host;
import com.telenav.comm.HostProvider;
import com.telenav.data.dao.serverproxy.ServiceLocator;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2011-1-24
 */
public class BrowserSessionArgs
{
    private String domainAlias = null;

    private Host host = null;

    public BrowserSessionArgs(Host host)
    {
        this.host = host;
    }
    
    public BrowserSessionArgs(String domainAlias)
    {
        this.domainAlias = domainAlias;
    }
	
	public BrowserSessionArgs(String action, String file)
    {
        if(action != null)
        {
            host = CommManager.getInstance().getComm().getHostProvider().createHost(action);
        }
        
        if(file != null && file.trim().length() > 0)
        {
            host.file = file;
        }
    }

    public String getUrl()
    {
        if(domainAlias != null)
        {
            HostProvider hostProvider = CommManager.getInstance().getComm().getHostProvider();
            ServiceLocator serviceLocator = (ServiceLocator)hostProvider;
            
            return serviceLocator.getDomainUrl(domainAlias);
        }
        else if(host != null)
        {
            String url = host.protocol + "://" + host.host;
            if (host.port > 0)
            {
                url += ":" + host.port;
            }
            if (host.file != null && host.file.length() > 0)
            {
                if (host.file.startsWith("/"))
                {
                    url += host.file;
                }
                else
                {
                    url += "/" + host.file;
                }
            }
            return url;
        }
        else
        {
            return "";
        }
    }
}
