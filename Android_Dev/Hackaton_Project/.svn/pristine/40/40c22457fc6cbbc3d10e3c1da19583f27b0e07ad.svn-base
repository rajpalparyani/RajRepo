package com.telenav.navservice.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.telenav.logger.Logger;
import com.telenav.network.TnDatagram;
import com.telenav.network.TnDatagramConnection;
import com.telenav.network.TnHttpConnection;
import com.telenav.network.TnNetworkManager;

public class TnNetwork
{
    protected TnHttpConnection connect(String url, int mode, boolean timeout) throws IOException
    {
        if (TnNetworkManager.getInstance() == null)
            return null;
        
        return (TnHttpConnection) TnNetworkManager.getInstance().openConnection(url, mode, timeout);
    }
    
    public byte[] sendHttpPost(String url, byte[] packet) 
    {
        if (packet != null && packet.length > 0)
        {
            Logger.log(Logger.INFO, this.getClass().getName(), "sending http post: url = "+url+", packet length = "+packet.length);
            OutputStream os = null;
            InputStream is = null;
            TnHttpConnection con = null;
            try
            {
                con = connect(url, TnNetworkManager.READ_WRITE, true);
                if (con != null)
                {
                    con.setRequestMethod("POST");
                    con.setRequestProperty("Content-Type", "text/html"); // required

                    os = con.openOutputStream();
                    os.write(packet);
                    os.flush();
                    if (con.getResponseCode() == TnHttpConnection.HTTP_OK)
                    {
                        is = con.openInputStream();
                        return read(is, (int)con.getLength());
                    }
                }
            } 
            catch (Exception e)
            {
                Logger.log(this.getClass().getName(), e);
            } 
            finally
            {
                close(is);
                close(os);
                close(con);
            }
        }
        return null;
    }
    
    public byte[] sendHttpGet(String url)
    {
        InputStream is = null;
        TnHttpConnection con = null;
        try
        {
            Logger.log(Logger.INFO, this.getClass().getName(), "sending http get, url = "+url);
            con = connect(url, TnNetworkManager.READ, true);
            if (con != null)
            {
                con.setRequestMethod("GET");

                if (con.getResponseCode() == TnHttpConnection.HTTP_OK)
                {
                    is = con.openInputStream();
                    int len = (int)con.getLength();
                    
                    // con.getContentLength() returns -1 if the length is not available.
                    if( len < 0 )
                    {
                        len = 10240; // use 10k
                    }
                    return read(is, len);
                }
            }
        } 
        catch (Exception e)
        {
            Logger.log(this.getClass().getName(), e);
        } 
        finally
        {
            close(is);
            close(con);
        }
        return null;
    }
    
    protected byte[] read(InputStream is, int len) throws IOException
    {
        byte[] result = new byte[len];
        if (is != null)
        {
            if (len > 0)
            {
                result = new byte[len];
                if (result != null)
                {
                    int actualRead = 0;
                    int bytesRead = 0;
                    while (bytesRead < len && actualRead != -1)
                    {
                        actualRead = is.read(result, bytesRead, len - bytesRead);
                        bytesRead += actualRead;
                    }
                }
            }
        }
        return result;
    }
    
    public boolean sendUdp(String url, byte[] payload)
    {
        try
        {
            Logger.log(Logger.INFO, this.getClass().getName(), "sending udp, url = "+url+", packet length = "+payload.length);
            if (url == null)
                return false;
            
            if (url.indexOf("://") < 0)
            {
                url = "datagram://" + url;
            }
            
            if (TnNetworkManager.getInstance() == null)
                return false;
            
            TnDatagramConnection socket = 
                (TnDatagramConnection)TnNetworkManager.getInstance().openConnection(url, TnNetworkManager.WRITE, true);
            try {
                TnDatagram datagram = socket.newDatagram(payload, payload.length);

                socket.send(datagram);
                return true;
            }
            catch(IOException e)
            {
                Logger.log(this.getClass().getName(), e);
            }
            finally
            {
                socket.close();
            }
        }
        catch(Exception e)
        {
            Logger.log(this.getClass().getName(), e);
            e.printStackTrace();
        }
        return false;
    }
    
    protected void close(InputStream is)
    {
        try
        {
            if (is != null)
            {
                is.close();
            }
        } catch (Exception e)
        {
    
        }
    }

    protected void close(OutputStream os)
    {
        try
        {
            if (os != null)
            {
                os.close();
            }
        } catch (Exception e)
        {
    
        }
    }
    
    protected void close(TnHttpConnection os)
    {
        try
        {
            if (os != null)
            {
                os.close();
            }
        } catch (Exception e)
        {
    
        }
    }

}
