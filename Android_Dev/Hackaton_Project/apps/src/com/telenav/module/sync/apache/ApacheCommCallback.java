/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * ApacheCallback.java
 *
 */
package com.telenav.module.sync.apache;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;

import com.telenav.comm.ICommCallback;
import com.telenav.data.dao.misc.ApacheResouceDao;
import com.telenav.data.dao.misc.DaoManager;
import com.telenav.logger.Logger;

/**
 *@author wzhu (wzhu@telenav.cn)
 *@date 2011-4-18
 */
class ApacheCommCallback implements ICommCallback
{
    String path = "";
    ApacheCommCallback(String fileName)
    {
        this.path = fileName;
    }
	
    public void handleChild(CommResponse response)
    {

    }

    public boolean isAllowNetworkRequest(CommResponse response)
    {
        return true;
    }

    public void networkError(CommResponse response)
    {
        if(path.indexOf(I18nRequestManager.ROOT_INDEX_PATH) != -1)
        {
            I18nRequestManager.getInstance().finish(Ii18nRequestListener.FAILED);
        }
        else
        {
            I18nFile file = (I18nFile) I18nRequestManager.getInstance().getRootFile().searchChildByName(path);
            if (file != null)
            {
                file.setStatus(I18nFile.FAIL);
            }
        }
        
        if(isAllFilesSent(I18nRequestManager.getInstance().getRootFile()))
        {
            I18nRequestManager.getInstance().finish(isSuccessful(I18nRequestManager.getInstance().getRootFile()) ? Ii18nRequestListener.SUCCESSFUL : Ii18nRequestListener.FAILED);
        }
    }

    public void networkFinished(CommResponse response)
    {
        byte[] data = response.responseData;
        int indexTag = path.indexOf(I18nRequestManager.INDEX_TAG);
        I18nFile file = I18nRequestManager.getInstance().getRootFile().searchChildByName(path);
        if(indexTag != -1)
        {
            try
            {
                byte[] decompressData = I18nHelper.decompress(data);
                Hashtable serverIndexes = I18nHelper.getIndexes(decompressData);
                Hashtable localIndexes = I18nRequestManager.getInstance().getLocalIndexes(path.substring(0, indexTag) + I18nRequestManager.INDEX_TAG);
                
                compareIndexes(file, serverIndexes, localIndexes);
            }
            catch (IOException e)
            {
                Logger.log(this.getClass().getName(), e);
                
                file.setStatus(I18nFile.FAIL);
            }
            
            if(file.getMd5() != null)
            {
                DaoManager.getInstance().getApacheIndexBackupDao().put(file.getName(), file.getMd5());
            }
        }
        else
        {
            DaoManager.getInstance().getApacheResouceBackupDao().put(file.getName(), data);
            file.setStatus(I18nFile.SUCCESS);
        }
        
        if(isAllFilesSent(I18nRequestManager.getInstance().getRootFile()))
        {
            I18nRequestManager.getInstance().finish(isSuccessful(I18nRequestManager.getInstance().getRootFile()) ? Ii18nRequestListener.SUCCESSFUL : Ii18nRequestListener.FAILED);
        }
    }

    public void updateProgress(CommResponse response)
    {

    }
    
    private boolean isAllFilesSent(I18nFile parentFile)
    {
        if (parentFile.getStatus() == I18nFile.SENDING || parentFile.getStatus() == I18nFile.RECEIVED)
        {
            return false;
        }

        for (int i = 0; i < parentFile.getChildrenSize(); i++)
        {
            I18nFile file = parentFile.getChildAt(i);
            boolean isSent = isAllFilesSent(file);
            if (!isSent)
            {
                return isSent;
            }
        }

        return true;
    }
    
    private boolean isSuccessful(I18nFile parentFile)
    {
        if (parentFile.getStatus() != I18nFile.SUCCESS)
        {
            return false;
        }

        for (int i = 0; i < parentFile.getChildrenSize(); i++)
        {
            I18nFile file = parentFile.getChildAt(i);
            boolean isSuccess = isSuccessful(file);
            if (!isSuccess)
            {
                return isSuccess;
            }
        }

        return true;
    }
    
    private void compareIndexes(I18nFile parentFile, Hashtable serverIndexes, Hashtable localIndexes)
    {
        ApacheResouceDao resourceIndexDao = null;
        if(I18nRequestManager.getInstance().hasBackupIndex())
        {
            resourceIndexDao =  DaoManager.getInstance().getApacheIndexBackupDao();
        }
        else
        {
            if(I18nRequestManager.getInstance().hasStoredIndex())
            {
                resourceIndexDao =  DaoManager.getInstance().getApacheServerIndexDao();
            }
        }
        
        Enumeration serverKeys = serverIndexes.keys();
        while(serverKeys.hasMoreElements())
        {
            String serverKey = (String)serverKeys.nextElement();
            String name = I18nHelper.getFileName(serverKey, parentFile);
            
            byte[] serverMd5 = (byte[])serverIndexes.get(serverKey);
            byte[] expectedMd5 = resourceIndexDao == null ? null : resourceIndexDao.get(name);
            if(expectedMd5 == null)
            {
                expectedMd5 = (byte[])localIndexes.get(serverKey);
            }
            
            if(expectedMd5 == null || !new String(serverMd5).equals(new String(expectedMd5)))
            {
                sendRequest(name, serverMd5, parentFile);
            }
        }
        
        parentFile.setStatus(I18nFile.SUCCESS);
    }
    
    private void sendRequest(String name, byte[] md5, I18nFile parentFile)
    {
        if(name.indexOf(".") == -1) //if it's a directory
        {
            Logger.log(Logger.INFO, this.getClass().getName(), "----i18n---sendRequest: ingore directory file: " + name);
            return;
        }
        
        if (name.indexOf(I18nRequestManager.INDEX_TAG) != -1 && I18nRequestManager.getInstance().getLocale() != null && I18nRequestManager.getInstance().getLocale().length() > 0)
        {
            Logger.log(Logger.INFO, this.getClass().getName(), "----i18n---sendRequest: current sync locale: " + I18nRequestManager.getInstance().getLocale());

            if (name.indexOf(I18nRequestManager.GENERIC_TAG) == -1 && name.indexOf(I18nRequestManager.getInstance().getLocale()) == -1)
            {
                Logger.log(Logger.INFO, this.getClass().getName(), "----i18n---sendRequest: ingore index file: " + name);
                return;
            }
        }
        
        I18nFile file = new I18nFile(name, I18nFile.SENDING);
        file.setMd5(md5);
        
        if(file.getMd5() != null)
        {
            DaoManager.getInstance().getApacheIndexBackupDao().put(file.getName(), file.getMd5());
        }
        
        if(parentFile != null)
        {
            parentFile.addChild(file);
        }
        
        I18nRequestManager.getInstance().addRequest(file);
    }
}
