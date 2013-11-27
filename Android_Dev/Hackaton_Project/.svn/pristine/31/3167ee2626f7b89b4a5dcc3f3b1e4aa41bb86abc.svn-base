/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * AndroidNetworkManager.java
 *
 */
package com.telenav.network.android;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.net.URL;
import java.net.URLConnection;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import com.telenav.logger.Logger;
import com.telenav.network.TnConnection;
import com.telenav.network.TnConnectionNotFoundException;
import com.telenav.network.TnNetworkManager;
import com.telenav.network.TnProxy;

/**
 * Factory class for creating new Connection objects at android platform.
 * 
 *@author fqming (fqming@telenav.cn)
 *@date Jul 10, 2010
 */
public class AndroidNetworkManager extends TnNetworkManager
{
    /**
     * construct a new network manager at android platform.
     */
    public AndroidNetworkManager()
    {
    }

    public TnConnection openConnection(String name) throws IOException
    {
        return openConnection(name, READ_WRITE);
    }

    public TnConnection openConnection(String name, int mode) throws IOException
    {
        return openConnection(name, mode, false);
    }
   
    public TnConnection openConnection(String name, int mode, boolean timeout) throws IOException
    {
        return openConnection(name, mode, timeout,networkRouter==null?null:networkRouter.getProxy(name));
    }
    
    public TnConnection openConnection(String name, int mode, boolean timeout, TnProxy proxy) throws IOException
    {
        Logger.log(Logger.INFO, this.getClass().getName(), "connect to "+name+",  mode = "+mode+", timeout = "+timeout);
        if (name == null || name.trim().length() == 0)
        {
            throw new IllegalArgumentException("The url is empty.");
        }
        if (name.startsWith("socket://"))
        {
            try
            {
                int lastIndex = name.lastIndexOf(":");
                String dstName = name.substring("socket://".length(), lastIndex);
                int dstPort = Integer.parseInt(name.substring(lastIndex + 1).trim());
                InetSocketAddress address =  new InetSocketAddress(dstName, dstPort);
                if(proxy != null && proxy.getType() == TnProxy.SOCKS)
                {
                    return new AndroidSocketConnection(address, timeout, new Proxy(Type.SOCKS, new InetSocketAddress(proxy.getAddress(),proxy.getPort())));
                }
                else
                {
                    return new AndroidSocketConnection(address, timeout);
                }
            }
            catch(Exception e)
            {
                throw new TnConnectionNotFoundException(e);
            }
        }
        else if(name.startsWith("datagram://"))
        {
            try
            {
                int lastIndex = name.lastIndexOf(":");
                String dstName = name.substring("datagram://".length(), lastIndex);
                int dstPort = Integer.parseInt(name.substring(lastIndex + 1).trim());
                InetSocketAddress address =  new InetSocketAddress(dstName, dstPort);

                return new AndroidDatagramConnection(address, timeout);
            }
            catch(Exception e)
            {
                throw new TnConnectionNotFoundException(e);
            }
        }
        else
        {
            try
            {
                if(name.startsWith("https://"))
                {
                    SSLContext sc = SSLContext.getInstance("TLS");
                    sc.init(null, new TrustManager[]
                    { new X509TrustManager()
                    {
                        public X509Certificate[] getAcceptedIssuers()
                        {
                            return null;
                        }

                        public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException
                        {

                        }

                        public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException
                        {

                        }
                    } }, new SecureRandom());
                    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
                    HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier()
                    {
                        public boolean verify(String arg0, SSLSession arg1)
                        {
                            return true;
                        }
                    });
                }
                
                name = name.replaceAll(" ", "%20");
                URL url = new URL(name);
                URLConnection urlConnection = null;
                if(proxy!= null)
                {
                    urlConnection = openHttpConnection(url, proxy);
                }
                else
                {
                    urlConnection = url.openConnection();
                }
                if (urlConnection instanceof HttpURLConnection)
                {
                    HttpURLConnection httpUrlConnection = (HttpURLConnection) urlConnection;
                    if (mode == READ)
                    {
                        httpUrlConnection.setDoInput(true);
                    }
                    else if (mode == WRITE)
                    {
                        httpUrlConnection.setDoOutput(true);
                    }
                    else
                    {
                        httpUrlConnection.setDoInput(true);
                        httpUrlConnection.setDoOutput(true);
                    }

                    if (timeout)
                    {
                        httpUrlConnection.setConnectTimeout(30 * 1000);
                        httpUrlConnection.setReadTimeout(30 * 1000);
                    }

                    return new AndroidHttpConnection(httpUrlConnection);
                }
                else
                {
                    throw new IOException("Currently don't support this protocol.");
                }
            }
            catch(Exception e)
            {
                throw new TnConnectionNotFoundException(e);
            }
        }
    }
    
    private static HttpURLConnection openHttpConnection(URL url, TnProxy proxy) throws IOException
    {
        switch (proxy.getType())
        {
            case TnProxy.SOCKS:
                return (HttpURLConnection) url.openConnection(new Proxy(Type.SOCKS, new InetSocketAddress(proxy.getAddress(), proxy
                        .getPort())));
            case TnProxy.REDIRECT:
                URL redirectedURL = new URL(url.getProtocol(), proxy.getAddress(), proxy.getPort(), url.getFile());
                HttpURLConnection connection = (HttpURLConnection) redirectedURL.openConnection();
                // XXX For cmwap gateway compatible.
                connection.setRequestProperty("X-Online-Host", url.getHost() + ":" + url.getPort());
                return connection;
            default:
                return (HttpURLConnection) url.openConnection(new Proxy(Type.HTTP, new InetSocketAddress(proxy.getAddress(), proxy
                        .getPort())));
        }
    }
    
}
