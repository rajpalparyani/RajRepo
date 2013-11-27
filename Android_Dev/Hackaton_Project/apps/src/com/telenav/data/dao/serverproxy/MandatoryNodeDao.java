/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * MandatoryNode.java
 *
 */
package com.telenav.data.dao.serverproxy;

import com.telenav.data.dao.AbstractDao;
import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.datatypes.mandatory.MandatoryProfile;
import com.telenav.data.datatypes.mandatory.MandatoryProfile.UserInfo;
import com.telenav.data.serializable.SerializableManager;
import com.telenav.logger.Logger;
import com.telenav.module.AppConfigHelper;
import com.telenav.persistent.TnPersistentManager;
import com.telenav.persistent.TnStore;

/**
 *@author fqming (fqming@telenav.cn)
 *@date Jul 16, 2010
 */
public class MandatoryNodeDao extends AbstractDao
{
    private final static int STORE_INDEX = 1000;

    private MandatoryProfile mandatoryNode;

    private boolean isLoaded;
    
    private TnStore mandatoryNodeStore;

    private static class SingletonHolder 
    {
        public static final MandatoryNodeDao INSTANCE = new MandatoryNodeDao();
    }

    public static MandatoryNodeDao getInstance() 
    {
        return SingletonHolder.INSTANCE;
    }
    
    /*
     * this method is for unit test only!
     */
    void setStore(TnStore startupStore)
    {
    	this.mandatoryNodeStore = startupStore;
    }
    
    private MandatoryNodeDao()
    {
    	Logger.log(Logger.INFO, DaoManager.class.getName(), " ------- getMandatoryNodeDao ---------  ");
        //use database because it's small and critical
        mandatoryNodeStore = TnPersistentManager.getInstance().createStore(TnPersistentManager.DATABASE, "mandatory_node");
        mandatoryNodeStore.load();
        if(AppConfigHelper.isLoggerEnable)
        {
            Logger.log(Logger.INFO, DaoManager.class.getName(), " ********* MandatoryNodeDao Constructor ********* " + mandatoryNodeStore);
        }
        
        this.load();
    }
    
    public MandatoryProfile getMandatoryNode()
    {
        return mandatoryNode;
    }

    public synchronized void store()
    {
        TnStore store = this.getStore();
        
        if(mandatoryNode != null)
        {
            byte[] data = SerializableManager.getInstance().getMandatorySerializable().toBytes(mandatoryNode);
            store.put(STORE_INDEX, data);
        }
        store.save();
    }

    public synchronized void clear()
    {
        if(AppConfigHelper.isLoggerEnable)
        {
            Logger.log(Logger.INFO, DaoManager.class.getName(), " ********* MandatoryNodeDao Clearing ... ********* ");
        }

        isLoaded = false;
        mandatoryNodeStore.clear();
    }

    private void load()
    {
        if (isLoaded)
            return;

        if(AppConfigHelper.isLoggerEnable)
        {
            Logger.log(Logger.INFO, DaoManager.class.getName(), " xxxxxxxxx MandatoryNodeDao Loading ... xxxxxxxx ");
        }
        
        byte[] mandatoryNodeData = null;

        TnStore store = this.getStore();
        mandatoryNodeData = store.get(STORE_INDEX);
        if (mandatoryNodeData != null)
        {
            mandatoryNode = SerializableManager.getInstance().getMandatorySerializable().createMandatoryProfile(mandatoryNodeData);
            
            if(AppConfigHelper.isLoggerEnable)
            {
                Logger.log(Logger.INFO, DaoManager.class.getName(), " ********* MandatoryNodeDao Loading ... *********  mandatoryNode HashId : " + mandatoryNode);
                
                if(mandatoryNode == null)
                {
                    Logger.log(Logger.ERROR, DaoManager.class.getName(), " ------- Load Mandatory Node Error ------------");
                    Logger.log(Logger.ERROR, DaoManager.class.getName(), " mandatoryNode == null ");
                }
                else
                {
                    UserInfo userInfo = mandatoryNode.getUserInfo();
                    if(userInfo == null)
                    {
                        Logger.log(Logger.ERROR, DaoManager.class.getName(), " ------- Load Mandatory Node Error ------------");
                        Logger.log(Logger.ERROR, DaoManager.class.getName(), " userInfo == null ");
                    }
                    else if (userInfo.phoneNumber == null
                            || userInfo.phoneNumber.length() == 0
                            || userInfo.userId == null || userInfo.userId.length() == 0)
                    {
                        Logger.log(Logger.ERROR, DaoManager.class.getName(), " ------- Load Mandatory Node Error ------------");
                        Logger.log(Logger.ERROR, DaoManager.class.getName(),
                            " missing --> ptn ? ptn = " + userInfo.phoneNumber
                                    + " , or userId? userId = " + userInfo.userId);
                    }
                }
            }
        }
        else
        {
            Logger.log(Logger.ERROR, DaoManager.class.getName(), " ------- Load Mandatory Node Error ------------");
            Logger.log(Logger.ERROR, DaoManager.class.getName(), "store.size = "
                    + store.size());
            Logger.log(Logger.ERROR, DaoManager.class.getName(),
                "mandatoryNodeData = null");
            mandatoryNode = new MandatoryProfile();
        }

        isLoaded = true;
    }

    private TnStore getStore()
    {
        return this.mandatoryNodeStore;
    }
}
