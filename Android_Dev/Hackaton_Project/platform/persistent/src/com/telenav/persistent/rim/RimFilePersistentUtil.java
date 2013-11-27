/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * RimFilePersistentUtil.java
 *
 */
package com.telenav.persistent.rim;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;

import com.telenav.logger.Logger;

/**
 *@author fqming (fqming@telenav.cn)
 *@date Aug 17, 2010
 */
class RimFilePersistentUtil
{
    static String baseDir = "file:///store/home/";
    static void setBaseDir(String dir)
    {
        baseDir = dir;
    }
    
    static void deleteData(String name)
    {
        FileConnection fconn = null;
        try
        {
            fconn = (FileConnection) Connector.open(baseDir + name);
            if (fconn.exists())
            {
                fconn.delete();
            }
        }
        catch (IOException e)
        {
            Logger.log(RimFilePersistentUtil.class.getName(), e);
        }
        finally
        {
            try
            {
                if (fconn != null)
                    fconn.close();
            }
            catch (IOException e)
            {
                Logger.log(RimFilePersistentUtil.class.getName(), e);
            }
            finally
            {
                fconn = null;
            }
        }
    }

    static byte[] readData(String name)
    {
        FileConnection fconn = null;
        InputStream is = null;
        byte[] data = null;
        try
        {
            fconn = (FileConnection) Connector.open(baseDir + name);
            if (fconn.exists())
            {
                is = fconn.openInputStream();
                int size = is.available();
                data = new byte[size];
                is.read(data);
            }
        }
        catch (IOException e)
        {
            Logger.log(RimFilePersistentUtil.class.getName(), e);
        }
        finally
        {
            try
            {
                if (is != null)
                    is.close();
            }
            catch (IOException e)
            {
                Logger.log(RimFilePersistentUtil.class.getName(), e);
            }
            finally
            {
                is = null;
            }
            
            try
            {
                if (fconn != null)
                    fconn.close();
            }
            catch (IOException e)
            {
                Logger.log(RimFilePersistentUtil.class.getName(), e);
            }
            finally
            {
                fconn = null;
            }
        }
        
        return data;
    }

    static void mkDir(String name)
    {
        FileConnection fconn = null;
        try
        {
            fconn = (FileConnection) Connector.open(baseDir + name);
            if (!fconn.exists())
            {
                fconn.mkdir();
            }
        }
        catch (IOException e)
        {
            Logger.log(RimFilePersistentUtil.class.getName(), e);
        }
        finally
        {
            try
            {
                if (fconn != null)
                    fconn.close();
            }
            catch (IOException e)
            {
                Logger.log(RimFilePersistentUtil.class.getName(), e);
            }
            finally
            {
                fconn = null;
            }
        }
    }

    static void saveData(String name, byte[] data)
    {
        FileConnection fconn = null;
        OutputStream os = null;
        try
        {
            fconn = (FileConnection) Connector.open(baseDir + name);
            if (fconn.exists())
            {
                os = fconn.openOutputStream();
                os.write(data);
            }
        }
        catch (IOException e)
        {
            Logger.log(RimFilePersistentUtil.class.getName(), e);
        }
        finally
        {
            try
            {
                if (os != null)
                    os.close();
            }
            catch (IOException e)
            {
                Logger.log(RimFilePersistentUtil.class.getName(), e);
            }
            finally
            {
                os = null;
            }
            
            try
            {
                if (fconn != null)
                    fconn.close();
            }
            catch (IOException e)
            {
                Logger.log(RimFilePersistentUtil.class.getName(), e);
            }
            finally
            {
                fconn = null;
            }
        }
    }
}
