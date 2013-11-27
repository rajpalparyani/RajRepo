/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * J2seRmsPersistentUtil.java
 *
 */
package com.telenav.persistent.j2se;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.telenav.logger.Logger;
import com.telenav.persistent.j2se.IJ2sePersistentContext.IJ2sePersistentObject;

/**
 *@author fqming (fqming@telenav.cn)
 *@date Aug 25, 2010
 */
class J2seRmsPersistentUtil
{
    public static String baseRmsDir;
    
    static void setBaseRmsDir(String dir)
    {
        baseRmsDir = dir;
    }
    
    static void deleteData(String storeKey)
    {
        try
        {
            File f = new File(baseRmsDir + "/" + storeKey);
            
            if (f.exists())
            {
                boolean isOk = f.delete();
                if(!isOk)
                {
                    Logger.log(Logger.WARNING, J2seFilePersistentUtil.class.getName(), "delete failed: " + storeKey);
                }
            }
        }
        catch (Throwable e)
        {
            Logger.log(J2seRmsPersistentUtil.class.getName(), e);
        }
    }
    
    static IJ2sePersistentObject loadData(String storeKey)
    {
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try
        {
            File f = new File(baseRmsDir + "/" + storeKey);

            if (!f.exists())
                return null;

            fis = new FileInputStream(f);
            ois = new ObjectInputStream(fis);
            Object obj = ois.readObject();
            return (IJ2sePersistentObject) obj;
        }
        catch (Throwable e)
        {
            Logger.log(J2seRmsPersistentUtil.class.getName(), e);
        }
        finally
        {
            try
            {
                if (fis != null)
                    fis.close();
            }
            catch (Exception e)
            {
                Logger.log(J2seRmsPersistentUtil.class.getName(), e);
            }
            try
            {
                if (ois != null)
                    ois.close();
            }
            catch (Exception e)
            {
                Logger.log(J2seRmsPersistentUtil.class.getName(), e);
            }
        }
        return null;
    }
    
    static void storeData(String storeKey, IJ2sePersistentObject persistentObject)
    {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try
        {
            File f = new File(baseRmsDir + "/" + storeKey);

            fos = new FileOutputStream(f);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(persistentObject);
        }
        catch (Throwable e)
        {
            Logger.log(J2seRmsPersistentUtil.class.getName(), e);
        }
        finally
        {
            try
            {
                if (fos != null)
                    fos.close();
            }
            catch (Exception e)
            {
                Logger.log(J2seRmsPersistentUtil.class.getName(), e);
            }
            try
            {
                if (oos != null)
                    oos.close();
            }
            catch (Exception e)
            {
                Logger.log(J2seRmsPersistentUtil.class.getName(), e);
            }
        }
    }
}
