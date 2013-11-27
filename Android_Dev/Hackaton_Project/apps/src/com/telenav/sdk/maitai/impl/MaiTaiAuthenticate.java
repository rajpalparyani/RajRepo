/**
 * 
 */
package com.telenav.sdk.maitai.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.telenav.app.CommManager;
import com.telenav.comm.Host;
import com.telenav.data.serverproxy.IServerProxyConstants;
import com.telenav.logger.Logger;
import com.telenav.network.TnHttpConnection;
import com.telenav.network.TnNetworkManager;
import com.telenav.sdk.maitai.IMaiTaiStatusConstants;

/**
 * @author xinrongl
 * @modifier qli
 */
public class MaiTaiAuthenticate
{
    
    private int lastError;
    private String errorMessage;
    private String developerKey;
    
    protected MaiTaiAuthenticate()
    {
    }
    
    /**
     * verify if the developer key is validate or not
     * @param developerKey
     */
    protected boolean validate(String developerKey)
    {
        this.developerKey = developerKey;
        this.lastError = IMaiTaiStatusConstants.STATUS_UNAUTHORIZED;
        
        String response = null;
        try
        {
            response = connect();
            if (!parseResponse(response))
                return false;
        }
        catch(Exception e)
        {
            Logger.log(this.getClass().getName(), e);
            return false;
        }
        
        return true;
    }


    protected int getLastError()
    {
        return lastError;
    }
    
    protected String getErrorMessage()
    {
        return this.errorMessage;
    }

    private String connect() throws Exception
    {
        OutputStream os = null;
        InputStream is = null;
        TnHttpConnection con = null;
        StringBuffer sb = new StringBuffer();
        try
        {
            if( MaiTaiHandler.getInstance().isCancelled() )
            {
                return null;
            }
            
            
            String url = getUrl();
            if( url != null && url.length() > 0 )
            {
                con = connect(url, TnNetworkManager.READ_WRITE, true);
            }
            if (con != null &&  !MaiTaiHandler.getInstance().isCancelled() )
            {
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded"); // required

                os = con.openOutputStream();
                
                String content = "realm=maitai" + "&apiKey=" + this.developerKey;
                dump(os, content.getBytes());
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
        } catch (Exception e)
        {
            throw new Exception(e.toString());
        } finally
        {
            if( os != null )
            {
                os.close();
            }
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
    
    private TnHttpConnection connect(String url, int mode, boolean timeout) throws IOException
    {
        return TnNetworkManager.getInstance() == null ? 
                null : (TnHttpConnection) TnNetworkManager.getInstance().openConnection(url, mode, timeout);
    }
    
    private void dump(OutputStream os, byte[] packet) throws IOException
    {
        if (os != null)
        {
            os.write(packet, 0, packet.length);
            try {
                os.flush();
            } catch (IOException ioe) {
                throw new IOException("Omg, flush failed: " + ioe);
            }
        }
    }
    
    private String getUrl()
    {
        Host host = CommManager.getInstance().getComm().
            getHostProvider().createHost(IServerProxyConstants.ACT_MAITAI_AUTHENTICATE);
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
    
    
    private boolean parseResponse(String response) throws Exception
    {
        if( response == null || response.trim().length() <= 0 )
        {
            return false;
        }
        String status = null;
        String errMessage = null;
        String code = "<code>";
        String message = "<message>";

        String temp = null;
        int index = response.indexOf(code);
        if ( index >= 0 )
        {
            temp = response.substring(index+code.length());
            index = temp.indexOf("</");
            if( index >= 0 )
            {
                status = temp.substring(0, index);
            }
        }
        index = response.indexOf(message);
        if ( index >= 0 )
        {
            temp = response.substring(index+message.length());
            index = temp.indexOf("</");
            if( index >= 0 )
            {
                errMessage = temp.substring(0, index);
            }
        }
        this.errorMessage = errMessage;
        if ("OK".equalsIgnoreCase(status))
        {
            this.lastError = IMaiTaiStatusConstants.STATUS_SUCCESS;
            return true;
        }
        else
        {
            return false;
        }        
    }

}