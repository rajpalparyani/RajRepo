/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * ResourceBackupDao.java
 *
 */
package com.telenav.data.dao.serverproxy;


import java.util.Enumeration;

import com.telenav.data.datatypes.address.Stop;
import com.telenav.persistent.TnStore;

/**
 *@author chrbu
 *@date 2012-8-6
 */
public class AddressBackupDao extends AddressDao
{
    private Stop homeAddress;
    private Stop officeAddress;
    private boolean isHomeUpdate = false;
    private boolean isOfficeUpdate = false;
    
    public AddressBackupDao(TnStore cache)
    {
        super(cache);
    }
    
    public synchronized void copyToBackup()
    {
        AddressDao addressDao = AbstractDaoManager.getInstance().getAddressDao();
        this.clear();
        copyStore(addressDao.cache,this.cache);
        this.load();
        backupHomeWork();
        addressDao.clear();
        addressDao.store();
    }
    
    
    public synchronized void cloneToAddressDao()
    {
        boolean isBackupDao = true;
        AddressDao addressDao = AbstractDaoManager.getInstance().getAddressDao();
        addressDao.clear();
        copyStore(this.cache, addressDao.cache);
        addressDao.load();
        addressDao.setHomeOfficeAddress(homeAddress, !isHomeUpdate, officeAddress, !isOfficeUpdate, isBackupDao);
        this.clear();
        this.store();
    }
    
    protected synchronized void copyStore(TnStore origStore, TnStore destStore)
    {
        if( origStore == null || destStore == null )
        {
            return;
        }
        
        Enumeration keys = origStore.keys();
        while( keys.hasMoreElements() )
        {
            String key = (String)keys.nextElement();
            destStore.put(key, origStore.get(key));
        }
    }
    
    @Override
    public void setHomeAddress(Stop stop, boolean isFromCloud)
    {
        this.homeAddress = stop;
        this.isHomeUpdate = !isFromCloud;
    }
    
    @Override
    public void setOfficeAddress(Stop stop, boolean isFromCloud)
    {
        this.officeAddress = stop;
        this.isOfficeUpdate = !isFromCloud;
    }
    
    @Override
    public Stop getHomeAddress()
    {
        return homeAddress;
    }
    
    @Override
    public Stop getOfficeAddress()
    {
        return officeAddress;
    }
    
    public void clearHomeWork()
    {
        this.homeAddress = null;
        this.officeAddress = null;
        this.isHomeUpdate = false;
        this.isOfficeUpdate = false;
    }
    
    public synchronized void backupHomeWork()
    {
        AddressDao addressDao = AbstractDaoManager.getInstance().getAddressDao();
        this.homeAddress = addressDao.getHomeAddress();
        this.officeAddress = addressDao.getOfficeAddress();
        this.isHomeUpdate = addressDao.isHomeUpdated();
        this.isOfficeUpdate = addressDao.isOfficeUpdated();
    }
    
    @Override
    public void clear()
    {
        clear(false);
        clearHomeWork();
    }
}
