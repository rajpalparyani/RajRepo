/**
 *
 * Copyright 2013 TeleNav, Inc. All rights reserved.
 * UpgradeDao.java
 *
 */
package com.telenav.data.dao.misc;

import com.telenav.data.dao.AbstractDao;
import com.telenav.data.serializable.SerializableManager;
import com.telenav.module.login.LoginController.UserLoginData;
import com.telenav.persistent.TnStore;

/**
 *@author bduan
 *@date 2013-4-4
 */
public class UpgradeDao extends AbstractDao
{
    private static final int KEY_USER_LOGIN_DATA = 1;
    
    protected TnStore store;
    
    public UpgradeDao(TnStore store)
    {
        this.store = store;
    }
    
    @Override
    public void store()
    {
        this.store.save();
    }

    @Override
    public void clear()
    {
        this.store.clear();
    }
    
    public void setUserLoginData(UserLoginData loginData)
    {
        if (loginData != null)
        {
            byte[] data = SerializableManager.getInstance().getLoginInfoSerializable().toBytes(loginData);
            store.put(KEY_USER_LOGIN_DATA, data);
        }
    }
    
    public UserLoginData getUserLoginData()
    {
        UserLoginData userLoginData = null;
        
        byte[] data = store.get(KEY_USER_LOGIN_DATA);
        if (data != null)
        {
            userLoginData = SerializableManager.getInstance().getLoginInfoSerializable().createLoginInfo(data);
        }
        
        return userLoginData;
    }

}
