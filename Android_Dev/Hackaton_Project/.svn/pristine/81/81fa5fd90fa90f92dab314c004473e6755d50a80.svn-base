/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * J2seMviewerLocationProvider.java
 *
 */
package com.telenav.location.j2se;

import java.io.InputStream;
import java.net.Socket;

import com.telenav.location.AbstractTnMviewerLocationProvider;
import com.telenav.location.TnLocationException;
import com.telenav.logger.Logger;

/**
 *@author fqming (fqming@telenav.cn)
 *@date Aug 25, 2010
 */
class J2seMviewerLocationProvider extends AbstractTnMviewerLocationProvider
{
    public J2seMviewerLocationProvider(String name)
    {
        super(name);
        
        this.socketHost = "127.0.0.1";
        this.socketPort = 11159;
    }

    protected String getMviewerData(int timeout) throws TnLocationException
    {
        Socket socket = null;
        InputStream is = null;
        try
        {
            socket = new Socket(this.socketHost, this.socketPort);
            socket.setSoTimeout(timeout);
            is = socket.getInputStream();
            byte[] buffer = new byte[512];
            int count = is.read(buffer);

            StringBuffer sb = new StringBuffer();

            while (count != -1)
            {
                sb.append(new String(buffer, 0, count));
                count = is.read(buffer);
            }
            return sb.toString();
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
            if (socket != null)
            {
                try
                {
                    socket.close();
                }
                catch (Throwable ex)
                {
                    Logger.log(this.getClass().getName(), ex);
                }
                finally
                {
                    socket = null;
                }
            }
        }
    }
}
