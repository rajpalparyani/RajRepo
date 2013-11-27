/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * FileManager.java
 *
 */
package com.telenav.data.dao.misc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;

import com.telenav.app.android.AndroidPersistentContext;
import com.telenav.io.TnIoManager;
import com.telenav.logger.Logger;
import com.telenav.persistent.TnFileConnection;
import com.telenav.persistent.TnPersistentManager;
import com.telenav.ui.citizen.map.GlResManager;

/**
 *@author bduan
 *@date 2011-3-21
 */
public class FileStoreManager
{
    public static final String TELENAV_DIRECTORY_PATH = "telenav70";
    
    //move the cache to internal memory to fix issue TN-2533
    //private static final String MAP_CACHE_FILE_DIR         = "/mapcache";
    
    /**
     * clear all files TeleNav create in Specific file System.
     */
    public static void clearFileSystem(int fileSystem)
    {
        String path = "";
        if(fileSystem == TnPersistentManager.FILE_SYSTEM_EXTERNAL)
        {
            path = TELENAV_DIRECTORY_PATH;
        }
        
        try
        {
            delDirectory(path, fileSystem);
        }catch(IOException e)
        {
            Logger.log(FileStoreManager.class.getName(), e);
        }
    }
    
    public static TnFileConnection createMapCacheFolder()
    {
        return GlResManager.getInstance().createMapCacheFolder();
    }
    
    public static void clearMapCache()
    {
        GlResManager.getInstance().clearMapCache();
    }
    
    public static void clearOpenglResource()
    {
        GlResManager.getInstance().clearOpenglResource();
    }
    
    public static void delDirectory(String currentRelativePath, int fileSystem) throws IOException
    {
        TnPersistentManager manager = TnPersistentManager.getInstance();
        TnFileConnection directory = manager.openFileConnection(currentRelativePath, "", fileSystem);
        if(directory == null)
            return;
        
        try
        {
            delFilesInDirectory(currentRelativePath, fileSystem); //delete all files in folder
            if (directory.exists())
                directory.delete(); // delete the empty directory.
        }
        catch (Exception e)
        {
            Logger.log(FileStoreManager.class.getName(), e);
        }
    }

    protected static void delFilesInDirectory(String currentRelativePath, int fileSystem) throws IOException
    {
        TnPersistentManager manager = TnPersistentManager.getInstance();
        TnFileConnection directory = manager.openFileConnection(currentRelativePath, "", fileSystem);
        
        if (directory == null)
            return;

        if (!directory.exists())
        {
            return;
        }

        if (!directory.isDirectory())
        {
            return;
        }

        String[] tempList = null;
        try
        {
            tempList = directory.list();
        }
        catch (Exception e)
        {
            Logger.log(FileStoreManager.class.getName(), e);
        }
        
        if (tempList == null)
            return;

        TnFileConnection temp = null;
        for (int i = 0; i < tempList.length; i++)
        {
            try
            {
                temp = manager.openFileConnection(currentRelativePath, tempList[i], fileSystem);
            }
            catch (Exception e)
            {
                Logger.log(FileStoreManager.class.getName(), e);
            }
                
            if (temp.isDirectory())
            {
                String relativePath = tempList[i];
                if(currentRelativePath.length() > 0)
                    relativePath = currentRelativePath + "/" + tempList[i];
                
                delDirectory(relativePath, fileSystem); // delete files in directory.
            }
            else
            {
                try
                {
                    temp.delete();
                }
                catch (Exception e)
                {
                    Logger.log(FileStoreManager.class.getName(), e);
                }
            }
        }

        return;
    }
    
    public static boolean copyFileFromAssetToApp(String fileName)
    {
        InputStream is = null;
        FileOutputStream os = null;
        boolean isSuccess = false;
        Context mContext = AndroidPersistentContext.getInstance().getContext();
        String path = mContext.getFilesDir().getPath() + "/" + fileName;
        try
        {
            File file = new File(path);
            if (!file.exists())
            {
                is = TnIoManager.getInstance().openFileFromAppBundle(fileName);
                os = new FileOutputStream(file);
                byte[] buffer = new byte[1024];
                int length = -1;
                while ((length = is.read(buffer)) > -1)
                {
                    os.write(buffer, 0, length);
                }
                os.flush();
                os.close();
                is.close();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
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
                e.printStackTrace();
            }
            try
            {
                if (is != null)
                    is.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            File targetFile = new File(path);
            if (targetFile.exists())
                isSuccess = true;
            else
                isSuccess = false;
            return isSuccess;
        }
    }
    
    public static boolean copyFileFromAssetToApp(String srcPathInAsset, String destPath)
    {
        InputStream is = null;
        FileOutputStream os = null;
        boolean isSuccess = false;
        Context mContext = AndroidPersistentContext.getInstance().getContext();
        String path = mContext.getFilesDir().getPath() + "/" + destPath;
        try
        {
            File file = new File(path);
            if (!file.exists())
            {
                is = TnIoManager.getInstance().openFileFromAppBundle(srcPathInAsset);
                
                if(file.getParent() != null)
                {
                    File dir = new File(file.getParent());
                    dir.mkdirs();
                }
                
                file.createNewFile();
                os = new FileOutputStream(file);
                byte[] buffer = new byte[1024];
                int length = -1;
                while ((length = is.read(buffer)) > -1)
                {
                    os.write(buffer, 0, length);
                }
                os.flush();
                os.close();
                is.close();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
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
                e.printStackTrace();
            }
            try
            {
                if (is != null)
                    is.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            File targetFile = new File(path);
            if (targetFile.exists())
                isSuccess = true;
            else
                isSuccess = false;
            return isSuccess;
        }
    }
}
