/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * GlResManager.java
 *
 */
package com.telenav.ui.citizen.map;

import java.io.IOException;
import java.io.OutputStream;

import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.dao.misc.FileStoreManager;
import com.telenav.data.dao.misc.SimpleConfigDao;
import com.telenav.i18n.ResourceBundle;
import com.telenav.io.TnIoManager;
import com.telenav.logger.Logger;
import com.telenav.persistent.TnFileConnection;
import com.telenav.persistent.TnPersistentManager;
import com.telenav.res.IBinaryRes;
import com.telenav.res.ICommonImageRes;
import com.telenav.res.ISpecialImageRes;
import com.telenav.res.ResourceManager;

/**
 * @author bduan
 * @date Dec 11, 2012
 */
public class GlResManager
{
    private static class InnerGlResManger
    {
        private static GlResManager resManger = new GlResManager();
    }

    private static final int RESOURCE_FROM_BINARY = 0;

    private static final int RESOURCE_FROM_GENERIC_IMAGE = 1;

    private static final int RESOURCE_FROM_GENERIC_COMMON_IMAGE = 2;

    private static final int RESOURCE_FROM_LOCALE_COMMON_IMAGE = 3;

    protected static String OPENGL_RESOURCE_PATH = "/"
            + TnPersistentManager.getInstance().getPersistentContext().getApplicationName() + "_opengl_map_resource/";

    protected static String MAP_CACHE_PATH = "/"
            + TnPersistentManager.getInstance().getPersistentContext().getApplicationName() + "_opengl_map_cache/";

    protected long cache_max_size = 0L;

    public String filePath = "";

    private GlResManager()
    {

    }

    public static GlResManager getInstance()
    {
        return InnerGlResManger.resManger;
    }

    public void initOpenglResource()
    {
        String openglResourcePath = "";

        // check if app has opengl resource/
        if (isOpenGlResourceExist(openglResourcePath, TnPersistentManager.FILE_SYSTEM_INTERNAL,
            IBinaryRes.FAMILY_BINARY_TEXTURES)
                && ((DaoManager) DaoManager.getInstance()).getSimpleConfigDao().getBoolean(
                    SimpleConfigDao.HAS_UPDATED_OPENGL_RES))
        {
            return;
        }

        // copyOpenGlResourceFiles(openglResourcePath, commonImageFiles, TnPersistentManager.FILE_SYSTEM_INTERNAL,
        // RESOURCE_FROM_GWENERIC_COMMON_IMAGE);
        copyOpenGlResourceFiles(openglResourcePath, TnPersistentManager.FILE_SYSTEM_INTERNAL, RESOURCE_FROM_BINARY,
            IBinaryRes.FAMILY_BINARY_TEXTURES);
        copyOpenGlResourceFiles(openglResourcePath, TnPersistentManager.FILE_SYSTEM_INTERNAL, RESOURCE_FROM_BINARY,
            IBinaryRes.FAMILY_BINARY_CONFIG);
        copyOpenGlResourceFiles(openglResourcePath, TnPersistentManager.FILE_SYSTEM_INTERNAL, RESOURCE_FROM_BINARY,
            IBinaryRes.FAMILY_BINARY_FONTS);
        copyOpenGlResourceFiles(openglResourcePath, TnPersistentManager.FILE_SYSTEM_INTERNAL, RESOURCE_FROM_BINARY,
            IBinaryRes.FAMILY_BINARY_MODELS);
        copyOpenGlResourceFiles(openglResourcePath, TnPersistentManager.FILE_SYSTEM_INTERNAL, RESOURCE_FROM_BINARY,
            IBinaryRes.FAMILY_BINARY_JSONSCHEMA);
        copyOpenGlResourceFiles(openglResourcePath, TnPersistentManager.FILE_SYSTEM_INTERNAL, RESOURCE_FROM_BINARY,
            IBinaryRes.FAMILY_BINARY_SHADERS);

        ((DaoManager) DaoManager.getInstance()).getSimpleConfigDao().put(SimpleConfigDao.HAS_UPDATED_OPENGL_RES, true);
        ((DaoManager) DaoManager.getInstance()).getSimpleConfigDao().store();
    }

    public TnFileConnection createMapCacheFolder()
    {
        try
        {
            TnFileConnection conn = TnPersistentManager.getInstance().openFileConnection(MAP_CACHE_PATH, null,
                TnPersistentManager.FILE_SYSTEM_INTERNAL);

            if (conn != null)
            {
                if (!conn.exists())
                {
                    if (conn.mkdirs())
                    {
                        return conn;
                    }
                }
                else
                {
                    return conn;
                }
            }
            else
            {
                return null;
            }
        }
        catch (Exception e)
        {
            Logger.log(FileStoreManager.class.getName(), e);
        }
        return null;
    }

    public void clearMapCache()
    {
        try
        {
            FileStoreManager.delDirectory(MAP_CACHE_PATH, TnPersistentManager.FILE_SYSTEM_INTERNAL);
        }
        catch (IOException e)
        {
            Logger.log(FileStoreManager.class.getName(), e);
        }
    }

    public void clearOpenglResource()
    {
        try
        {
            FileStoreManager.delDirectory(OPENGL_RESOURCE_PATH, TnPersistentManager.FILE_SYSTEM_INTERNAL);
        }
        catch (IOException e)
        {
            Logger.log(FileStoreManager.class.getName(), e);
        }
    }

    public long getCacheSize()
    {
        return cache_max_size;
    }

    protected String ensureCacheFolder(long cacheSize)
    {
        TnFileConnection conn = FileStoreManager.createMapCacheFolder();
        if (conn != null)
        {
            // long maxCacheSize = (long)(conn.totalSize() * CACHE_SIZE_PERCENT);
            // per issue TN-2533, we need to limit the cache to 10 MB,
            // but the opengl engine is not accurate in cache size control, so we use 9 MB here to give it a buffer
            long maxCacheSize = 9 * 1024 * 1024;
            cacheSize = Math.min(maxCacheSize, conn.availableSize());
            // make size 1024 integer times
            cacheSize = cacheSize >> 10 << 10;
            cache_max_size = cacheSize;
            return conn.getPath();
        }
        return null;
    }

    private void copyOpenGlResourceFiles(String filePath, int storageType, int resourceType,
            String familyName)
    {
        String[] tempList = null;
        try
        {
            tempList = TnIoManager.getInstance().listFileFromAppBundle(
                ResourceManager.getInstance().getCurrentBundle().getDataPath("binaries", true) + familyName);
        }
        catch (IOException e)
        {
            Logger.log(this.getClass().getName(), e);
        }
        if (tempList == null)
        {
            return;
        }
        copyOpenGlResourceFiles(filePath, tempList, storageType, resourceType, familyName);
    }

    private void copyOpenGlResourceFiles(String filePath, String[] fileList, int storageType, int resourceType,
            String familyName)
    {
        OutputStream os = null;
        TnFileConnection fileConnection = null;
        ResourceBundle bundle = ResourceManager.getInstance().getCurrentBundle();
        byte[] data = null;
        String specialFamily = ISpecialImageRes.getSpecialImageFamily();
        for (int i = 0; i < fileList.length; i++)
        {
            if (resourceType == RESOURCE_FROM_BINARY)
            {
                data = bundle.getGenericBinary(fileList[i], familyName);
            }
            else if (resourceType == RESOURCE_FROM_GENERIC_IMAGE)
            {
                data = bundle.getGenericImage(fileList[i], specialFamily);
            }
            else if (resourceType == RESOURCE_FROM_GENERIC_COMMON_IMAGE)
            {
                data = bundle.getGenericImage(fileList[i], ICommonImageRes.FAMILY_COMMON);
            }
            else if (resourceType == RESOURCE_FROM_LOCALE_COMMON_IMAGE)
            {
                data = bundle.getImage(fileList[i], ICommonImageRes.FAMILY_COMMON);
            }
            if (data == null)
            {
                Logger.log(Logger.INFO, this.getClass().getName(), "Missing Reading File -------" + fileList[i]);
                continue;
            }
            try
            {

                fileConnection = TnPersistentManager.getInstance().openFileConnection(filePath + familyName, fileList[i],
                    storageType);

                if (!fileConnection.exists())
                {
                    fileConnection.create();
                }

                os = fileConnection.openOutputStream();
                os.write(data, 0, data.length);
                os.flush();
            }
            catch (IOException exception)
            {
                Logger.log(Logger.EXCEPTION, this.getClass().getName(), exception.toString());
                try
                {
                    fileConnection = TnPersistentManager.getInstance().openFileConnection(filePath + familyName, fileList[i],
                        storageType);
                    os = fileConnection.openOutputStream();
                    os.write(data, 0, data.length);
                    os.flush();
                }
                catch (IOException ex)
                {
                    Logger.log(Logger.INFO, this.getClass().getName(), "Missing Writing File -------" + fileList[i]);
                    try
                    {
                        if (os != null)
                            os.close();
                        fileConnection.close();
                    }
                    catch (IOException ex1)
                    {
                        Logger.log(this.getClass().getName(), ex1);
                    }
                }
            }
            finally
            {
                try
                {
                    if (os != null)
                        os.close();
                    fileConnection.close();
                }
                catch (IOException exception)
                {
                    Logger.log(this.getClass().getName(), exception);
                }
            }
        }
    }

    public boolean isOpenGlResourceExist(String filePath, int storageType, String familyName)
    {
        TnFileConnection fileConnection = null;
        boolean hasCopiedOpenglResource = false;
        try
        {
            fileConnection = TnPersistentManager.getInstance().openFileConnection(filePath + familyName, "", storageType);
            if (fileConnection != null && fileConnection.exists() && fileConnection.list() != null
                    && fileConnection.list().length > 0)
            {
                hasCopiedOpenglResource = true;
            }
        }
        catch (Exception exception)
        {
            Logger.log(this.getClass().getName(), exception);
            hasCopiedOpenglResource = false;
        }
        finally
        {
            try
            {
                if (fileConnection != null)
                {
                    fileConnection.close();
                }
            }
            catch (Exception e)
            {
                Logger.log(this.getClass().getName(), e);
            }
        }

        ensureFolder(filePath, storageType);

        return hasCopiedOpenglResource;
    }

    public boolean isOpenGlResourceExist(String filePath, String fileName, int storageType, String familyName)
    {
        TnFileConnection fileConnection = null;
        boolean hasCopiedOpenglResource = false;
        try
        {
            fileConnection = TnPersistentManager.getInstance().openFileConnection(filePath + familyName, fileName, storageType);
            if (this.filePath.length() == 0)
            {
                this.filePath = fileConnection.getPath();
                this.filePath = this.filePath.substring(0, this.filePath.lastIndexOf('/') + 1);
            }
            if (fileConnection != null && fileConnection.exists())
            {
                hasCopiedOpenglResource = true;
            }

            if (fileConnection != null)
                fileConnection.close();
        }
        catch (Exception exception)
        {
            Logger.log(this.getClass().getName(), exception);
            hasCopiedOpenglResource = false;
        }

        ensureFolder(filePath, storageType);

        return hasCopiedOpenglResource;
    }

    private boolean ensureFolder(String filePath, int storageType)
    {
        boolean isMkdir = false;
        try
        {
            TnFileConnection fileConnection = TnPersistentManager.getInstance().openFileConnection(filePath, null, storageType);
            if (fileConnection != null && !fileConnection.exists())
            {
                isMkdir = fileConnection.mkdir();
                Logger.log(Logger.INFO, this.getClass().getName(), "create opengl resource directory: " + isMkdir);
                fileConnection.close();
            }
        }
        catch (Exception e)
        {
            Logger.log(this.getClass().getName(), e);
        }
        return isMkdir;
    }

}
