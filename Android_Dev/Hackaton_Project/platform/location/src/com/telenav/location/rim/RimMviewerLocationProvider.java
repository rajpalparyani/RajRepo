/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * RimMviewerLocationProvider.java
 *
 */
package com.telenav.location.rim;

import java.io.InputStream;

import javax.microedition.io.Connector;
import javax.microedition.io.SocketConnection;

import com.telenav.location.AbstractTnMviewerLocationProvider;
import com.telenav.location.TnLocationException;
import com.telenav.logger.Logger;

/**
 *@author fqming (fqming@telenav.cn)
 *@date Aug 13, 2010
 */
class RimMviewerLocationProvider extends AbstractTnMviewerLocationProvider
{
    public RimMviewerLocationProvider(String name)
    {
        super(name);
        
        this.socketHost = "127.0.0.1";
        this.socketPort = 11159;
    }

    protected String getMviewerData(int timeout) throws TnLocationException
    {
        String mviewerSocketddress = "socket://" + this.socketHost + ":" + this.socketPort + ";deviceside=true";
        
        StringBuffer sb = new StringBuffer();
        SocketConnection conn = null;
        InputStream is = null;
        try
        {
            conn = (SocketConnection) Connector.open(mviewerSocketddress);
            is = conn.openInputStream();
            byte[] buffer = new byte[512];
            int count = is.read(buffer);
            while (count != -1)
            {
                sb.append(new String(buffer, 0, count));
                count = is.read(buffer);
            }
        }
        catch (Throwable ex)
        {
            Logger.log(this.getClass().getName(), ex);

            throw new TnLocationException(ex.getMessage());
        }
        finally
        {
            if (is != null)
            {
                try
                {
                    is.close();
                }
                catch (Throwable ex)
                {
                    Logger.log(this.getClass().getName(), ex);
                }
                finally
                {
                    is = null;
                }
            }
            if (conn != null)
            {
                try
                {
                    conn.close();
                }
                catch (Throwable ex)
                {
                    Logger.log(this.getClass().getName(), ex);
                }
                finally
                {
                    conn = null;
                }
            }
        }

        return sb.toString();
    }

}
