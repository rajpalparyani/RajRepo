/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * ServiceLocator.java
 *
 */
package com.telenav.data.dao.serverproxy;

import java.util.Enumeration;
import java.util.Vector;

import com.telenav.comm.Host;
import com.telenav.comm.HostProvider;
import com.telenav.data.datatypes.primitive.StringMap;

public class ServiceLocator extends HostProvider
{
    //for secret setting.
    StringMap secretActionUrlMap = new StringMap();
    StringMap secretAliasUrlMap = new StringMap();
    
    //for server driven.
    StringMap aliasUrlMap = new StringMap();
    StringMap aliasSuffixMap = new StringMap();
    StringMap actionAliasMap = new StringMap();
    
    //for default.
    StringMap defaultActionAliasMap = new StringMap();
    StringMap defaultActionUrlMap = new StringMap();
    
    public Host createHost(String action)
    {
    	if (null == action) return null;
        String actionURL = getActionUrl(action);
        
        if (actionURL == null)
        {
            if(action.indexOf(Host.HTTP) != -1 || action.indexOf(Host.DATAGRAM) != -1 || action.indexOf(Host.HTTPS) != -1 || action.indexOf(Host.SOCKET) != -1)
            {
                Host host = createHostByURL(action); 
                return host;
            }
            
            return null;
        }
        
        Host host = createHostByURL(actionURL); 
        return host;
    }
    
    public String getDomainUrl(String domainAlias)
    {
    	String domainUrl = secretAliasUrlMap.get(domainAlias);
    	
    	if(domainUrl == null || domainUrl.trim().length() == 0)
    	{
    		domainUrl = aliasUrlMap.get(domainAlias);
    	}
    	
    	return domainUrl;
    }
    
	public void setSetretServiceLocator(StringMap secretAliasUrlMap, StringMap secretActionUrlMap)
    {
	    if(secretAliasUrlMap != null)
	    {
	        this.secretAliasUrlMap.copy(secretAliasUrlMap);
	    }
	    
	    if(secretActionUrlMap != null)
	    {
	        this.secretActionUrlMap.copy(secretActionUrlMap);
	    }
    }
    
    public void setDefaultServiceLocator(StringMap defaultActionAliasMap, StringMap defaultActionUrlMap)
    {
        if(defaultActionAliasMap != null)
        {
            this.defaultActionAliasMap.copy(defaultActionAliasMap);
        }
        
        if(defaultActionUrlMap != null)
        {
            this.defaultActionUrlMap.copy(defaultActionUrlMap);
        }
    }
    
    public void setServerConfigServiceLocator(StringMap aliasUrlMap, StringMap aliasSuffixMap, StringMap actionAliasMap)
    {
        if (aliasUrlMap != null)
        {
            this.aliasUrlMap.copy(aliasUrlMap);
        }
        
        if (aliasSuffixMap != null)
        {
            this.aliasSuffixMap.copy(aliasSuffixMap);
        }
        
        if(actionAliasMap != null)
        {
            this.actionAliasMap.copy(actionAliasMap);
        }
    }
    
    public void clear()
    {
        secretActionUrlMap.clear();
        secretAliasUrlMap.clear();
        aliasUrlMap.clear();
        aliasSuffixMap.clear();
        actionAliasMap.clear();
        defaultActionAliasMap.clear();
        defaultActionUrlMap.clear();
    }
    
    public String getActionUrl(String action)
    {
        String url = getSecretUrl(action);
        
        if(url == null || url.trim().length() == 0)
        {
            String alias = getAlias(action);
            if(alias != null)
            {
                String domainUrl = getDomainUrl(alias);
                
                if(domainUrl != null)
                {
                    url = domainUrl;

                    String suffix = aliasSuffixMap.get(alias);

                    if (suffix != null)
                    {
                        url += suffix;
                    }
                }
            }
        }
        
        if(url == null || url.trim().length() == 0)
        {
            url = getDefaultUrl(action);
        }
        
        return url;
    }
    
    protected Host createHostByURL(String url)
    {
        if (url == null) return null;
        
        Host host = null;
        
        try
        {
            // replacement from service domain alias to service domain URL
            int bracketPreIndex  = url.indexOf("{");
            int bracketPostIndex = url.indexOf("}");
            if (bracketPreIndex != -1 && bracketPostIndex != -1)
            {
                String domainAlias = url.substring(bracketPreIndex + 1, bracketPostIndex).trim();
                String domainURL = (String)aliasUrlMap.get(domainAlias);
                url = url.substring(0,bracketPreIndex) + domainURL + url.substring(bracketPostIndex+1);
            }
            
            // parse network type
            int index = url.indexOf("://");
            String networkType = url.substring(0, index).trim();
           
            // parse server IP (or DNS name) and port
            int indexServer = url.indexOf("/", index + 3);
            String serverInfo;
            if (indexServer != -1)
            {
                serverInfo = url.substring(index + 3, indexServer);
            }
            else
            {
                serverInfo = url.substring(index + 3);
            }   
          
            int indexPort = serverInfo.indexOf(":");
            String server = serverInfo;
            String port   = null;
            
            if (indexPort != -1)
            {
                server = serverInfo.substring(0,indexPort).trim();
                port   = serverInfo.substring(indexPort+1).trim();
                
                indexPort = port.indexOf(":");
                if (indexPort != -1)
                {
                    //secondPort = port.substring(indexPort + 1).trim();
                    port = port.substring(0,indexPort).trim();
                }
            }
            
            String webApp = "";
            String servletName = "";
            String file = "";
            if (indexServer != -1)
            { 
                // parse web app and servlet info
                String appInfo = url.substring(indexServer + 1);
                
                servletName = appInfo;
                index = appInfo.indexOf("/");
                if (index != -1)
                {
                    webApp = appInfo.substring(0,index);
                    servletName = appInfo.substring(index + 1);
                }
                
                if(webApp != null && webApp.length() > 0)
                {
                    file = "/" + webApp + "/" + servletName;
                }
                else
                {
                    file = "/" + servletName;
                }
            }
    
            if (networkType.equalsIgnoreCase("UDP"))
            {
                host = this.createHost(Host.DATAGRAM, server, Integer.parseInt(port));
            }
            else
            {
                //boolean isStandAlone = false;
                String protocol = Host.HTTP;
                if(networkType.equalsIgnoreCase("HTTPS"))
                {
                    protocol = Host.HTTPS;
                }
                else if(networkType.equalsIgnoreCase("SOCKET"))
                {
                    protocol = Host.SOCKET;
                }
                
                host = this.createHost(protocol, server, port != null && port.length() > 0 ? Integer.parseInt(port) : 0, file);
            }            
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        
        return host;
        
    }      

    protected String getSecretUrl(String action) 
    {
        if(secretActionUrlMap == null || secretActionUrlMap.size() == 0)
            return null;
        
        String sercetUrl = secretActionUrlMap.get(action);
        
        return sercetUrl;
    }
    
    protected String getDefaultUrl(String action) 
    {
        if(defaultActionUrlMap == null || defaultActionUrlMap.size() == 0)
            return null;
        
        String defaultUrl = defaultActionUrlMap.get(action);
        
        return defaultUrl;
    }

    protected String getAlias(String action)
    {
        String alias = actionAliasMap.get(action);
        
        if(alias == null)
        {
            alias = defaultActionAliasMap.get(action);
        }
        
        return alias;
    }
    
    
    public Vector getAllActions()
    {
        Vector vector = new Vector();
        Enumeration actions = this.actionAliasMap.keys();
        while (actions.hasMoreElements())
        {
            String action = (String) actions.nextElement();
            vector.add(action);
        }
        actions = this.defaultActionAliasMap.keys();
        while (actions.hasMoreElements())
        {
            String action = (String) actions.nextElement();
            if (!vector.contains(action))
            {
                vector.add(action);
            }
        }
        return vector;
    }
}
