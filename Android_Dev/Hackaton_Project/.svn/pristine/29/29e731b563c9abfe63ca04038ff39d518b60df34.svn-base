/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * HttpRequestJob.java
 *
 */
package com.telenav.module.entry;

import java.io.IOException;
import java.io.InputStream;

import com.telenav.logger.Logger;
import com.telenav.network.TnHttpConnection;
import com.telenav.network.TnNetworkManager;

/**
 *@author dzhao
 *@date Nov 6, 2012
 */
public class AdsTracking
{
    private static String RESPONSE_SUCCESSFUL = "success";
    public static String VELTI_REQUEST_TYPE = "Velti";
    
    public static boolean sendHttpRequest(String url, String type)
    {
        try
        {
            String response = connect(url);
            if (type.equalsIgnoreCase(VELTI_REQUEST_TYPE))
            {
                return parseVeltiResponse(response);
            }
        }
        catch(Exception e)
        {
            Logger.log(AdsTracking.class.getName(), e);
        }
        return false;
    }
        
    private static boolean parseVeltiResponse(String response)
    {
        if(RESPONSE_SUCCESSFUL.equalsIgnoreCase(response))
        {
            Logger.log(Logger.INFO, AdsTracking.class.getName(), "====Velti response successful ====");
            return true;
        }
        Logger.log(Logger.INFO, AdsTracking.class.getName(), "====Velti response failed ====");
        return false;
    }

    private static TnHttpConnection connect(String url, int mode, boolean timeout) throws IOException
    {
        return TnNetworkManager.getInstance() == null ? 
                null : (TnHttpConnection) TnNetworkManager.getInstance().openConnection(url, mode, timeout);
    }

    private static String connect(String uri) throws Exception
    {
        InputStream is = null;
        TnHttpConnection con = null;
        StringBuffer sb = new StringBuffer();
        try
        {
            con = connect(uri, TnNetworkManager.READ, true);
            if (con != null)
            {
                con.setRequestMethod("GET");
                con.setRequestProperty("responseDataFormat", "JSON");

                if (con.getResponseCode() == TnHttpConnection.HTTP_OK)
                {
                    Logger.log(Logger.INFO, AdsTracking.class.getName(), "HttpRequestJob send request successful ,HttpRequestJob responseCode:" + TnHttpConnection.HTTP_OK);
                    is = con.openInputStream();
                    int c = is.read();
                    while (c != -1)
                    {
                        sb.append((char)c);
                        c = is.read();
                    }
                }
                else
                {
                   int responseCode = con.getResponseCode();
                   Logger.log(Logger.INFO, AdsTracking.class.getName(), "HttpRequestJob send request not successful ,HttpRequestJob responseCode:" + responseCode);
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
}
