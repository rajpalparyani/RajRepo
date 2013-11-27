/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * SyncApacheResourceRunner.java
 *
 */
package com.telenav.module.sync.apache;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;

import com.telenav.data.cache.ImageCacheManager;
import com.telenav.data.dao.misc.ApacheResouceDao;
import com.telenav.data.dao.misc.ApacheResourceSaver;
import com.telenav.data.dao.misc.DaoManager;
import com.telenav.logger.Logger;
import com.telenav.res.ResourceManager;

/**
 *@author wzhu (wzhu@telenav.cn)
 *@date 2011-4-15
 */
public class I18nRequestManager
{
    public static final String GENERIC_TAG = "generic";
    public static final String INDEX_TAG = ".index";
    
    public static final String INDEX_SUFFIX = ".gz";
    
    public static final String ROOT_PATH = "i18n";
    
    public static final String ROOT_INDEX_PATH = I18nRequestManager.ROOT_PATH + "/" + "i18n" + INDEX_TAG + INDEX_SUFFIX;
    
    public static final I18nRequestManager INSTANCE = new I18nRequestManager();
    
    private Ii18nRequestListener listener;
    private Thread i18nRequestThread;
    private ApacheCommSender sender;
    private I18nFile rootFile;
    private boolean hasBackupIndex;
    private boolean hasStoredIndex;
    private Hashtable localeIndexCache;
    private String locale;
    
    private I18nRequestManager()
    {
    }
    
    public static I18nRequestManager getInstance()
    {
        return INSTANCE;
    }
    
    public synchronized void start(String locale, Ii18nRequestListener listener)
    {
        if(i18nRequestThread == null || !i18nRequestThread.isAlive())
        {
            Logger.log(Logger.INFO, this.getClass().getName(), "I18nRequestManager start()");
            this.locale = locale;
            this.listener = listener;
            this.localeIndexCache = new Hashtable();
            
            hasBackupIndex = DaoManager.getInstance().getApacheIndexBackupDao().size() > 0;
            hasStoredIndex = DaoManager.getInstance().getApacheServerIndexDao().size() > 0;
            
            this.sender = new ApacheCommSender();

            this.rootFile = new I18nFile(I18nHelper.getFileName(ROOT_INDEX_PATH, null), I18nFile.SENDING);
            addRequest(rootFile);

            i18nRequestThread = new Thread(this.sender);
            i18nRequestThread.start();
        }
    }
    
    public synchronized void stop()
    {
        if(this.sender != null)
        {
            this.sender.cancel();
            this.sender = null;
        }
        
        if(this.localeIndexCache != null)
        {
            this.localeIndexCache.clear();
        }
        
        i18nRequestThread = null;
        this.rootFile = new I18nFile(I18nHelper.getFileName(ROOT_INDEX_PATH, null), I18nFile.SENDING);
        
        this.listener = null;
        Logger.log(Logger.INFO, this.getClass().getName(), "I18nRequestManager stop()");
    }
    
    public I18nFile getRootFile()
    {
        return this.rootFile;
    }
    
    public Ii18nRequestListener getListener()
    {
        return this.listener;
    }
    
    void addRequest(I18nFile file)
    {
        if(this.sender != null)
        {
            this.sender.addRequest(file);
        }
    }
    
    boolean hasBackupIndex()
    {
        return hasBackupIndex;
    }
    
    boolean hasStoredIndex()
    {
        return hasStoredIndex;
    }
    
    String getLocale()
    {
        return this.locale;
    }
    
    synchronized Hashtable getLocalIndexes(String name)
    {
        Object o = this.localeIndexCache.get(name);
        if(o instanceof Hashtable)
        {
            return (Hashtable)o;
        }
        else
        {
            Hashtable localIndexes;
            try
            {
                byte[] localData = I18nHelper.getIndexesFromLocaleFile(name);
                localIndexes = I18nHelper.getIndexes(localData);
                this.localeIndexCache.put(name, localIndexes);
            }
            catch(IOException e)
            {
                Logger.log(this.getClass().getName(), e);
                
                localIndexes = new Hashtable();
            }
            
            return localIndexes;
        }
    }
    
    void finish(int status)
    {
        Logger.log(Logger.INFO, this.getClass().getName(), "I18nRequestManager finish1: " + status);
        
        DaoManager.getInstance().getApacheIndexBackupDao().store();
        if(status == Ii18nRequestListener.SUCCESSFUL)
        {
            if(this.sender.getCount() < 2)
            {
                Logger.log(Logger.INFO, this.getClass().getName(), "I18nRequestManager finish2: "  + status + ", count: " + this.sender.getCount());
                
                Enumeration ids = DaoManager.getInstance().getApacheIndexBackupDao().keys();
                if(ids.hasMoreElements())
                {
                    String id = (String) ids.nextElement();
                    byte[] md5 = (byte[]) DaoManager.getInstance().getApacheIndexBackupDao().get(id);
                    ApacheResouceDao resourceDao = DaoManager.getInstance().getApacheResouceBackupDao();
                    byte[] value = resourceDao.get(id);
                    if (md5 != null && value == null)
                    {
                        I18nFile file = new I18nFile(id, I18nFile.SENDING);
                        file.setMd5(md5);
                        if (this.getRootFile() != null)
                        {
                            this.getRootFile().addChild(file);
                        }

                        Logger.log(Logger.INFO, this.getClass().getName(), "I18nRequestManager finish2: " + status + ", continue file: " + file.getName());
                        I18nRequestManager.getInstance().addRequest(file);
                    }
                }
            }
            
            if(ResourceManager.getInstance().getCurrentLocale().equals(this.locale))
            {
                ApacheResourceSaver.getInstance().saveFiles();
                Logger.log(Logger.INFO, this.getClass().getName(), "I18nRequestManager finish sync locale: "  + this.locale + " are the same with current locale.");
            }
            else
            {
                Logger.log(Logger.INFO, this.getClass().getName(), "I18nRequestManager finish sync locale: " + this.locale + ", current locale:"
                        + ResourceManager.getInstance().getCurrentLocale() + " are different with current locale.");
            }
            
            ApacheResourceSaver.getInstance().clear();
        }
        
        Logger.log(Logger.INFO, this.getClass().getName(), "I18nRequestManager finish3: "  + status);
        
        ImageCacheManager.getInstance().clearI18nImage();
        
        if(listener != null)
        {
            Logger.log(Logger.INFO, this.getClass().getName(), "I18nRequestManager update listener");
            listener.update(status, this.sender.getCount());
        }
        
        stop();
    }
}
