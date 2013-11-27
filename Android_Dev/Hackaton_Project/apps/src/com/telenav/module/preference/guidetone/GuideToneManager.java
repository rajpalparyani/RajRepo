/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * GuideToneManager.java
 *
 */
package com.telenav.module.preference.guidetone;

import java.util.Enumeration;

import com.telenav.data.dao.misc.AppStoreDao;
import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.dao.misc.PreferenceDao;
import com.telenav.data.dao.serverproxy.ResourceBarDao;
import com.telenav.data.datatypes.preference.Preference;
import com.telenav.data.serverproxy.AbstractServerProxy;
import com.telenav.data.serverproxy.IServerProxyListener;
import com.telenav.data.serverproxy.impl.ISyncCombinationResProxy;
import com.telenav.i18n.ResourceBundle;
import com.telenav.io.TnIoManager;
import com.telenav.logger.Logger;
import com.telenav.module.AppConfigHelper;
import com.telenav.module.region.RegionUtil;
import com.telenav.res.IAudioRes;
import com.telenav.res.ResourceManager;

/**
 *@author qli (qli@telenav.cn)
 *@date 2011-1-24
 */
public class GuideToneManager implements IServerProxyListener
{
    protected String currentGuideToneValue      = null;
    protected String webGuideToneValue          = null;//used for web set guide tone value.
    protected Object mutex                      = new Object();
    protected Object loadMutex                  = new Object();
    protected boolean hasError                  = false;
    protected boolean isLoadingAllStaticAudio   = false; 
    protected boolean isReadingAudioFile        = false;
    
    
    protected String currentKey = null;
    protected String defaultAudioFamily = IAudioRes.FAMILY_MP3HI;
    
    private static class InnerGuideToneManager
    {
        private static GuideToneManager instance = new GuideToneManager();
    }
    
    protected GuideToneManager()
    {
    }
    
    public static GuideToneManager getInstance()
    {
        return InnerGuideToneManager.instance;
    }

    public static void init(GuideToneManager impl)
    {
        InnerGuideToneManager.instance = impl;
    }
    
    public boolean setWebGuideTone(String value)
    {
        webGuideToneValue = value;
        if (webGuideToneValue != null
                && (currentGuideToneValue == null || !currentGuideToneValue.equals(webGuideToneValue)))
        {
            return true;
        }
        
        return false;
    }
    
    public boolean hasError()
    {
        return hasError;
    }
    
    public void loadDefaultGuideTone()
    {
        prepareDefaultAudio(true);
    }
    
    public void loadGuideTone()
    {
        hasError = false;
        if( webGuideToneValue != null && 
                ( currentGuideToneValue == null || !currentGuideToneValue.equals(webGuideToneValue) ))
        {
            prepareStaticAudioData();
        }
        
        if(!hasError)
        {
            storeInPreferenceDao();
        }
    }
    
    public void setDefaultAudioFamily(String defaultAudioFamily)
    {
        this.defaultAudioFamily = defaultAudioFamily;
    }
    
    public void transactionFinished(AbstractServerProxy serverProxy, String jobId)
    {
        AbstractServerProxy proxy = serverProxy;
        if (proxy instanceof ISyncCombinationResProxy)
        {
            ISyncCombinationResProxy syncProxy = (ISyncCombinationResProxy)proxy;
            Logger.log(Logger.INFO, this.getClass().getName(), "step: " + syncProxy.getStep());
            if( syncProxy.getStep() == ISyncCombinationResProxy.STEP_SYNC_FINISH )
            {
                storeAudio();
                //release the mutex after download finished.
                synchronized (mutex)
                {
                    try
                    {
                        mutex.notifyAll();
                    }
                    catch( Exception e )
                    {
                        Logger.log(this.getClass().getName(), e);
                    }
                }
            }
        }
      
    }

    public void updateTransactionStatus(AbstractServerProxy proxy, byte progress)
    {
        
    }

    public void networkError(AbstractServerProxy proxy, byte statusCode, String jobId)
    {
        hasError = true;
        synchronized (mutex)
        {
            try
            {
                mutex.notifyAll();
            }
            catch( Exception e )
            {
                Logger.log(this.getClass().getName(), e);
            }
        }
    }

    public void transactionError(AbstractServerProxy proxy)
    {
        hasError = true;
        synchronized (mutex)
        {
            try
            {
                mutex.notifyAll();
            }
            catch( Exception e )
            {
                Logger.log(this.getClass().getName(), e);
            }
        }
    }

    public boolean isAllowNetworkRequest(AbstractServerProxy proxy)
    {
        return true;
    }
    
    public void notifyGuideTone()
    {
        synchronized (mutex)
        {
            try
            {
                mutex.notifyAll();
            }
            catch( Exception e )
            {
                Logger.log(this.getClass().getName(), e);
            }
        }
    }
    
    public byte[] getStaticAudioData(int audioId)
    {
        long start = 0;
        if(Logger.DEBUG)
        {
            start = System.currentTimeMillis();
            Logger.log(Logger.INFO, this.getClass().getName(), " -- Audio Profile -- Audio files missing -- audio Id : " + audioId);
        }
        
        String familyName = defaultAudioFamily;
        ResourceBundle bundle = ResourceManager.getInstance().getCurrentBundle();
        byte[] data = null;
        
        boolean isDefaultLocale = bundle.getLocale().equalsIgnoreCase(AppConfigHelper.defaultLocale);
        if(!isDefaultLocale)
        {
            return null;
        }
        
        isReadingAudioFile = true;
        try
        {
            data = bundle.getAudio(audioId, familyName);
        }
        catch (Exception e) 
        {
            Logger.log(getClass().getName(), e);
        }
        finally
        {
            isReadingAudioFile = false;
        
            if(!isLoadingAllStaticAudio)
            {
                isLoadingAllStaticAudio = true;
                Thread t = new Thread(new Runnable()
                {
                    public void run()
                    {
                        try
                        {
                            prepareDefaultAudio(false);
                        }
                        catch (Exception e) 
                        {
                            Logger.log(getClass().getName(), e);
                        }
                        finally
                        {
                            isLoadingAllStaticAudio = false;
                        }
                    }
                });
                t.start();
            }
        }
        
        if(Logger.DEBUG)
        {
            Logger.log(Logger.INFO, this.getClass().getName(),
                " -- Audio Profile -- Audio files missing -- got Data : "
                        + (data == null ? 0 : data.length) + " , elapse time : "
                        + (System.currentTimeMillis() - start));
        }
        
        return data;
    }
    
    protected void prepareStaticAudioData()
    {
        if(!DaoManager.getInstance().getMandatoryNodeDao().getMandatoryNode()
                .getUserInfo().guideTone.equals(webGuideToneValue))
        {
            DaoManager.getInstance().getMandatoryNodeDao().getMandatoryNode()
                .getUserInfo().guideTone = webGuideToneValue;
            DaoManager.getInstance().getMandatoryNodeDao().store();
        }
        prepareStoredAudio();
        //sendSyncResourceRequest();
    }
    
    protected void prepareStoredAudio()
    {
        if (webGuideToneValue == null)
            return;
        
        isLoadingAllStaticAudio   = false; 
        isReadingAudioFile        = false;
        
        AppStoreDao appStoreDao = ((DaoManager)DaoManager.getInstance()).getAppStoreDao();
        ResourceBarDao resourceBarDao = DaoManager.getInstance().getResourceBarDao();
        resourceBarDao.clearStaticAudio();
        Enumeration e = appStoreDao.keys();
        
        clearStoreIndexKey();
        
        while(e.hasMoreElements())
        {
            String key = (String)e.nextElement();
            
            if (key.startsWith(getStoreIndexKey()))
            {
                try
                {
                    int audioId = Integer.parseInt(key.substring(getStoreIndexKey().length()));
                    if (audioId <= ResourceBarDao.MAX_STATIC_AUDIO_ID)
                    {
                        byte[] data = appStoreDao.get(key);
                        if (data != null)
                        {
                            resourceBarDao.putStaticAudio(audioId, data);
                        }
                    }
                }
                catch (Exception ex)
                {
                    Logger.log(this.getClass().getName(), ex);
                }
            }
        }
        resourceBarDao.store();
        resourceBarDao.refreshAudioInventory();
    }

    private String getAudioDir(String familyName)
    {
        return "i18n/" + ResourceManager.getInstance().getCurrentLocale() + "/audios/" + familyName; 
    }
    
    private static int getAudioIdFromFileName(String fileName)
    {
        int extIndex = fileName.lastIndexOf('.');
        int separatorIndex = fileName.lastIndexOf('/');
        if(extIndex > separatorIndex+1)
        {
            String keyStr; 
            if(separatorIndex >= 0)
            {
                keyStr = fileName.substring(separatorIndex + 1,extIndex);
            }
            else
            {
                keyStr = fileName.substring(0,extIndex);
            }
            return Integer.parseInt(keyStr);
        }
        return -1;
    }
    
    protected void prepareDefaultAudio(boolean isNeedClearExistingFiles)
    {
        String familyName = defaultAudioFamily;
        TnIoManager ioManager = TnIoManager.getInstance();
        ResourceBundle bundle = ResourceManager.getInstance().getCurrentBundle();
        ResourceBarDao resourceBarDao = DaoManager.getInstance().getResourceBarDao();
        
        if(isNeedClearExistingFiles)
        {
            resourceBarDao.clearStaticAudio();
        }
        
        try
        {
            String[] files =  ioManager.listFileFromAppBundle(getAudioDir(familyName));
            if (files != null)
            {
                for(int i = 0; i < files.length; i++)
                {
                    int audioId = getAudioIdFromFileName(files[i]);
                    byte[] data = bundle.getAudio(audioId, familyName);
                    
                                       
                    if (data != null)
                    {
                        resourceBarDao.putStaticAudio(audioId, data);
                    }
                    
                    if(Logger.DEBUG)
                    {
                        Logger.log(Logger.INFO, this.getClass().getName(),
                            " -- Audio Profile -- Load default == got Data audioId : "
                                    + audioId + " , length : "
                                    + (data == null ? 0 : data.length));
                    }
                    
                    if(isReadingAudioFile)
                    {
                        if(Logger.DEBUG)
                        {
                            Logger.log(Logger.INFO, this.getClass().getName(),
                                " -- Audio Profile -- Load default == isReadingAudioFile, wait");
                        }
                        
                        synchronized (loadMutex)
                        {
                            try
                            {
                                loadMutex.wait(300);
                            }
                            catch (Exception e)
                            {
                            }
                        }
                    }
                }
            }
        }
        catch (Exception e) 
        {
            Logger.log(getClass().getName(), e);
        }
        resourceBarDao.store();
        resourceBarDao.refreshAudioInventory();
    }
    
    protected void storeAudio()
    {
        AppStoreDao appStore = ((DaoManager)DaoManager.getInstance()).getAppStoreDao();
        ResourceBarDao resourceBarDao = DaoManager.getInstance().getResourceBarDao();
        int[] audioIds = resourceBarDao.getStaticAudioIds();
        
        clearStoreIndexKey();
        
        if (audioIds != null)
        {
            for(int i = 0; i < audioIds.length; i++)
            {
                int audioId = audioIds[i];
                byte[] data = resourceBarDao.getAudio(audioId);
                if (data != null)
                {
                    appStore.set(getStoreIndexKey() + audioId, data);
                }
            }
        }
        
        appStore.store();
    }
    
    protected void clearStoreIndexKey()
    {
        currentKey = null;
    }
    
    protected String getStoreIndexKey()
    {
        if(currentKey == null || currentKey.trim().length() == 0)
        {
            currentKey = ResourceManager.getInstance().getCurrentLocale() + "_" + webGuideToneValue + "_";
        }
        return currentKey;
    }
    
    /*protected void sendSyncResourceRequest()
    {
        Vector combination = new Vector();
        combination.addElement(PrimitiveTypeCache.valueOf(ISyncCombinationResProxy.TYPE_AUDIO_AND_RULE));
        SyncResExecutor.getInstance().syncCombinationResource(combination, this, null);
        synchronized (mutex)
        {
            try
            {
                mutex.wait(5*60*1000);
            }
            catch (InterruptedException e)
            {
            }
        }
    }*/
    
    protected void storeInPreferenceDao()
    {
        currentGuideToneValue = webGuideToneValue;
        PreferenceDao prefDao = ((DaoManager)DaoManager.getInstance()).getPreferenceDao();
        prefDao.setStrValue(Preference.ID_PREFERENCE_GUIDE_TONE, webGuideToneValue);
        prefDao.store(RegionUtil.getInstance().getCurrentRegion());
    }
    
}
