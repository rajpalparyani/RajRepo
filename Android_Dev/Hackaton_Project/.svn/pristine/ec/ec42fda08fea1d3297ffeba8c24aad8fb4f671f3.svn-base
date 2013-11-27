/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * ApacheResourceSaver.java
 *
 */
package com.telenav.data.dao.misc;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;

import com.telenav.app.TeleNavDelegate;
import com.telenav.app.android.AndroidPersistentContext;
import com.telenav.data.datatypes.primitive.StringMap;
import com.telenav.io.TnIoManager;
import com.telenav.io.TnProperties;
import com.telenav.logger.Logger;
import com.telenav.res.ResourceManager;

/**
 *@author wzhu (wzhu@telenav.cn)
 *@date 2011-1-27
 */
public class ApacheResourceSaver
{

    public static final String FILE_STORE = "file_store";
    
    static final String TEXT = "strings";

    static final String IMAGE = "images";

    static final String BINARY = "binaries";

    static final String CARRIER_MAPPING = "carrierMapping";

    static final String GENERIC = "generic";
    
    private static final ApacheResourceSaver INSTANCE = new ApacheResourceSaver();
    
    private ApacheResourceSaver(){}
    
    public static final ApacheResourceSaver getInstance()
    {
        return INSTANCE;
    }
    
    public void saveFiles()
    {
        ApacheResouceDao indexBackupDao = DaoManager.getInstance().getApacheIndexBackupDao();
        ApacheResouceDao indexDao = DaoManager.getInstance().getApacheServerIndexDao();
        for (Enumeration ids = indexBackupDao.keys(); ids.hasMoreElements();)
        {
            String id = (String) ids.nextElement();
            byte[] value = indexBackupDao.get(id);
            indexDao.put(id, value);
        }
        indexBackupDao.clear();
        indexBackupDao.store();
        indexDao.store();

        ApacheResouceDao resourceDao = DaoManager.getInstance().getApacheResouceBackupDao();
        for (Enumeration ids = resourceDao.keys(); ids.hasMoreElements();)
        {
            String id = (String) ids.nextElement();
            byte[] value = resourceDao.get(id);
            int index = id.lastIndexOf('/');
            String dirName = id.substring(0, index);
            String filename = id.substring(index + 1);
            if(!isGeneric(id))
            {
                if (dirName.indexOf(TEXT) != -1)
                {
                    index = filename.indexOf('.');
                    String familyName = filename.substring(0, index);
                    TnProperties properties = TnIoManager.getInstance().createProperties();
                    ByteArrayInputStream in = new ByteArrayInputStream(value);
                    try
                    {
                        properties.load(in);
                    }
                    catch (IOException e)
                    {
                        Logger.log(this.getClass().getName(), e);
                    }
                    
                    Logger.log(Logger.INFO, this.getClass().getName(), "----i18n---file: " + filename + ", string count: " + properties.size());
                    
                    for (Enumeration keys = properties.propertyNames(); keys.hasMoreElements();)
                    {
                        try
                        {
                            String key = (String) keys.nextElement();
                            String text = properties.getProperty(key);
                            ResourceManager.getInstance().getCurrentBundle().getPersistentBundle().putString(Integer.parseInt(key), familyName, text);
                        }
                        catch (Exception e)
                        {
                            Logger.log(this.getClass().getName(), e);
                        }
                    }
                }
                else if (dirName.indexOf(IMAGE) != -1)
                {
                    index = dirName.lastIndexOf('/');
                    String familyName = dirName.substring(index + 1);
                    ResourceManager.getInstance().getCurrentBundle().getPersistentBundle().putImage(filename, familyName, value);
                }
                else if (dirName.indexOf(BINARY) != -1)
                {
                    ResourceManager.getInstance().getCurrentBundle().getPersistentBundle().putBinary(filename, "", value);
                }
            }
            else
            {
                index = dirName.lastIndexOf('/');
                String familyName = dirName.substring(index + 1);
                if (dirName.indexOf(IMAGE) != -1)
                {
                    ((DaoManager)DaoManager.getInstance()).getSimpleConfigDao().put(SimpleConfigDao.HAS_UPDATED_OPENGL_RES, false);
                    ResourceManager.getInstance().getCurrentBundle().getPersistentBundle().putGenericImage(filename, familyName, value);
                }
                else if (dirName.indexOf(BINARY) != -1)
                {
                    ((DaoManager)DaoManager.getInstance()).getSimpleConfigDao().put(SimpleConfigDao.HAS_UPDATED_OPENGL_RES, false);
                    ResourceManager.getInstance().getCurrentBundle().getPersistentBundle().putGenericBinary(filename, "", value);
                }
                else if (dirName.indexOf(CARRIER_MAPPING) != -1)
                {
                    saveCarrierMapping(value);
                }
            }
        }
        resourceDao.clear();
        resourceDao.store();
        ((DaoManager)DaoManager.getInstance()).getSimpleConfigDao().store();
        ResourceManager.getInstance().getCurrentBundle().getPersistentBundle().store();
        TeleNavDelegate.getInstance().retrieveCarrier();
        Logger.log(Logger.INFO, this.getClass().getName(), "Finished");
    }
    
    public void saveCarrierMapping(byte[] data)
    {
        Logger.log(Logger.INFO, this.getClass().getName(), "saveCarrierMapping");
        String path = AndroidPersistentContext.getInstance().getContext().getFilesDir().getAbsolutePath();

        String destPath = "TN70_scout_us_carrier_mapping.bin";

        path = path + "/" + destPath;

        File file = new File(path);

        try
        {
            if (file.getParentFile() != null)
            {
                file.getParentFile().mkdirs();
            }
            
            if (file.exists())
            {
                file.delete();
            }

            file.createNewFile();
            
            FileOutputStream fos = null;

            try
            {
                fos = new FileOutputStream(file);
                fos.write(data);
                fos.flush();
                fos.close();
                
                DaoManager.getInstance().getSimpleConfigDao().loadCarrierMapping();
                
                StringMap map = DaoManager.getInstance().getSimpleConfigDao().getCarrierMapping();
                
                Enumeration<String> keys = map.keys();
                StringBuilder sb = new StringBuilder();
                while(keys.hasMoreElements())
                {
                    String key = keys.nextElement();
                    String value = map.get(key);
                    sb.append(key).append(", ").append(value);
                }
                Logger.log(Logger.INFO, this.getClass().getName(), "after sync, carrier mapping change to \n" + sb);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            finally
            {
                try
                {
                    if (fos != null)
                        fos.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    
    private boolean isGeneric(String filename)
    {
        int index = filename.indexOf(GENERIC);
        return index == -1 ? false : true;
    }
    
    public void clear()
    {
        DaoManager.getInstance().getApacheIndexBackupDao().clear();
        DaoManager.getInstance().getApacheResouceBackupDao().clear();
    }
}
