/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * J2seFilePersistentUtil.java
 *
 */
package com.telenav.persistent.j2se;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.telenav.logger.Logger;

/**
 *@author fqming (fqming@telenav.cn)
 *@date Aug 25, 2010
 */
class J2seFilePersistentUtil
{
    static void deleteData(String name)
    {
        File file = null;
        try
        {
            file = new File(name);
            if (file.exists())
            {
                boolean isDeleted = file.delete();
                if(!isDeleted)
                {
                    Logger.log(Logger.WARNING, J2seFilePersistentUtil.class.getName(), "deleted failed: " + name);
                }
            }
        }
        catch (Exception e)
        {
            Logger.log(J2seFilePersistentUtil.class.getName(), e);
        }
        finally
        {
            file = null;
        }
    }

    static byte[] readData(String name)
    {
        File file = null;
        InputStream is = null;
        byte[] data = null;
        try
        {
            file = new File(name);
            if (file.exists())
            {
                is = new FileInputStream(file);
                int size = is.available();
                data = new byte[size];
                is.read(data);
            }
        }
        catch (IOException e)
        {
            Logger.log(J2seFilePersistentUtil.class.getName(), e);
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
                Logger.log(J2seFilePersistentUtil.class.getName(), e);
            }
            finally
            {
                is = null;
                file = null;
            }
        }
        
        return data;
    }
    
    static void deleteDir(String name)
    {
    	deleteFile(new File(name));
    }
    
    private static boolean deleteFile(File file)
    {
    	try
    	{
    		if (!file.exists()) return false;
    		
    		if (file.isDirectory())
    		{
    			File[] files = file.listFiles();
    			for (int i = 0; i < files.length; i++)
    			{
    				deleteFile(files[i]);
    			}
    		}
    		
    	    if (file.delete()) return true;
    	}
    	catch (Exception ex)
    	{
    		Logger.log(J2seFilePersistentUtil.class.getName(), ex);
    	}
    	
    	return false;
    }
    
    static void mkDir(String name)
    {
        File file = null;
        try
        {
            file = new File(name);
            if (!file.exists())
            {
                boolean isOk = file.mkdir();
                if(!isOk)
                {
                    Logger.log(Logger.WARNING, J2seFilePersistentUtil.class.getName(), "mkdir failed: " + name);
                }
            }
        }
        catch (Exception e)
        {
            Logger.log(J2seFilePersistentUtil.class.getName(), e);
        }
        finally
        {
            file = null;
        }
    }

    static void saveData(String name, byte[] data)
    {
        File file = null;
        OutputStream os = null;
        try
        {
            file = new File(name);
            boolean fileExisted = true;
            
            if (!file.exists())
            {
            	fileExisted = false;
                if (file.createNewFile())
                {
                    fileExisted = true;	
                }
            }
            
            if (fileExisted)
            {	
	            os = new FileOutputStream(file);
	            os.write(data);
            }
        }
        catch (IOException e)
        {
            Logger.log(J2seFilePersistentUtil.class.getName(), e);
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
                Logger.log(J2seFilePersistentUtil.class.getName(), e);
            }
            finally
            {
                os = null;
                file = null;
            }
        }
    }
}
