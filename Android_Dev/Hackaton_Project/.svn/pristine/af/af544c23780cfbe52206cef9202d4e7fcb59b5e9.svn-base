/**
 * 
 */
package com.telenav.sdk.maitai.impl;

import java.io.IOException;
import java.io.InputStream;

import org.json.tnme.JSONObject;
import org.json.tnme.JSONTokener;

import com.telenav.app.CommManager;
import com.telenav.comm.Host;
import com.telenav.data.serverproxy.IServerProxyConstants;
import com.telenav.logger.Logger;
import com.telenav.network.TnHttpConnection;
import com.telenav.network.TnNetworkManager;


/**
 * @author xinrongl
 *
 */
public class LookupAabUri
{
    
    protected static final String OK_CODE = "OK";

    public static String lookup(String url)
    {
        try
        {
            String response = connect(url);
            return parseResponse(response);
        }
        catch(Exception e)
        {
            Logger.log(LookupAabUri.class.getName(), e);
            return null;
        }
        
    }
    
    private static String connect(String uri) throws Exception
    {
        if( MaiTaiHandler.getInstance().isCancelled() )
        {
            return null;
        }
        InputStream is = null;
        TnHttpConnection con = null;
        StringBuffer sb = new StringBuffer();
        try
        {
            String url = getUrl() + "?url=" + uri;
            con = connect(url, TnNetworkManager.READ, true);
            if (con != null &&  !MaiTaiHandler.getInstance().isCancelled() )
            {
                con.setRequestMethod("GET");
                con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");        
                con.setRequestProperty("responseDataFormat", "JSON");

                if (con.getResponseCode() == TnHttpConnection.HTTP_OK 
                        && !MaiTaiHandler.getInstance().isCancelled() )
                {
                    is = con.openInputStream();
                    
                    int c = is.read();
                    while (c != -1)
                    {
                        sb.append((char)c);
                        c = is.read();
                    }
                }
            }
        } 
        catch (Exception e)
        {
        } 
        finally
        {
            if( is != null )
            {
                is.close();
            }
            if( con != null )
            {
                con.close();
            }
        }
        return sb.toString();

    }
    
    private static TnHttpConnection connect(String url, int mode, boolean timeout) throws IOException
    {
        return TnNetworkManager.getInstance() == null ? 
                null : (TnHttpConnection) TnNetworkManager.getInstance().openConnection(url, mode, timeout);
    }
    
    private static String parseResponse(String response) throws Exception
    {
        if( response == null || response.trim().length() <= 0 )
        {
            return null;
        }
        JSONTokener jt = new JSONTokener(response);
        if (jt.more())
        {
            Object object = jt.nextValue();
            if (object instanceof JSONObject)
            {
                return parseJson((JSONObject)object);
            }
            else
            {
                return parseXml(response);
            }
        }
        
        return null;
    }
    
    protected static String parseJson(JSONObject o) throws Exception
    {
        if (o != null)
        {
            o = (JSONObject)o.getJSONObject("LookupUrlResponse");
            JSONObject statusObj = (JSONObject)o.get("status");
            if (statusObj != null)
            {
                String status = statusObj.getString("code");
                if (OK_CODE.equalsIgnoreCase(status))
                    return o.getString("url");
            }
        }
        return null;
    }
    
    protected static String parseXml(String xml)
    {
        if (xml != null)
        {
            String status = null;
            String code = "<code>";
            String url = "<url>";
            int index = xml.indexOf(code);
            if (index >= 0)
            {
                String subxml = xml.substring(index + code.length());
                index = subxml.indexOf("</");
                if (index >= 0)
                {
                    status = subxml.substring(0, index);
                }
                index = xml.indexOf(url);
                if (OK_CODE.equalsIgnoreCase(status) && index >= 0)
                {
                    subxml = xml.substring(index + url.length());
                    index = subxml.indexOf("</");
                    if (index >= 0)
                    {
                        url = subxml.substring(0, index);
                        return url;
                    }
                }
            }
        }
        return null;
    }
    
    private static String getUrl()
    {
        Host host = CommManager.getInstance().getComm().
            getHostProvider().createHost(IServerProxyConstants.ACT_MAITAI_LOOKUPURL);
        if( host == null )
        {
            return null;
        }
        String url = host.protocol + "://" + host.host;
        if(host.port > 0)
        {
            url += ":" + host.port;
        }
        if(host.file != null && host.file.length() > 0)
        {
            if( host.file.startsWith("/") )
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

}
