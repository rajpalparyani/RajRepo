/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * CommServiceLocator.java
 *
 */
package com.telenav.app;

import com.telenav.comm.Host;
import com.telenav.data.dao.serverproxy.ServiceLocator;

/**
 *@author fqming (fqming@telenav.cn)
 *@date Aug 13, 2010
 */
public class CommServiceLocator extends ServiceLocator
{
    public String getUrl(Host host)
    {
        String url = host.protocol + "://" + host.host;
        if(host.port > 0)
        {
            url += ":" + host.port;
        }
        if(host.file != null && host.file.length() > 0)
        {
            if(host.file.startsWith("/"))
            {
                url += host.file;
            }
            else
            {
                url += "/" + host.file;
            }
        }
        
        //Remove it since there's no below logic for this any more.
//        if((Host.HTTPS.equals(host.protocol) || Host.HTTP.equals(host.protocol)))
//        {
//            url += ";?flag=1";
//        }
        
        return url;
    }
}
