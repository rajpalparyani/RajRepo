/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * AbstractDaoManager.java
 *
 */
package com.telenav.data.dao.serverproxy;

import com.telenav.data.dao.misc.AndroidBillingDao;
import com.telenav.data.dao.misc.ApacheResouceDao;
import com.telenav.data.dao.misc.BrowserSdkServiceDao;
import com.telenav.data.dao.misc.NavExitAbnormalDao;
import com.telenav.data.dao.misc.PreferenceDao;
import com.telenav.data.dao.misc.SimpleConfigDao;
import com.telenav.data.dao.misc.TripsDao;

/**
 *@author fqming (fqming@telenav.cn)
 *@date Jul 19, 2010
 */
public abstract class AbstractDaoManager
{
    protected static AbstractDaoManager instance;
    
    public AbstractDaoManager()
    {
        
    }
    
    public static void init(AbstractDaoManager instance)
    {
        AbstractDaoManager.instance = instance;
    }
    
    public static AbstractDaoManager getInstance()
    {
        return instance;
    }
    
    public abstract MandatoryNodeDao getMandatoryNodeDao();
    
    public abstract ResourceBarDao getResourceBarDao();
    
    public abstract ResourceBackupDao getResourceBackupDao();
    
    public abstract ServerDrivenParamsDao getServerDrivenParamsDao();
    
    public abstract ServerDrivenParamsDao getDsrServerDrivenParamsDao();
    
    public abstract ServiceLocatorDao getServiceLocatorDao();
    
    public abstract StartupDao getStartupDao();
    
    public abstract NearCitiesDao getNearCitiesDao();
    
    public abstract AddressDao getAddressDao();
    
    public abstract AddressBackupDao getBackupAddressDao();
    
    public abstract ExpressAddressDao getExpressAddressDao();
    
    public abstract BillingAccountDao getBillingAccountDao();
    
    public abstract BrowserSdkServiceDao getBrowserSdkServiceDao();
    
    public abstract TripsDao getTripsDao();
    
    public abstract NavExitAbnormalDao getNavExitAbnormalDao();
    
    public abstract PreferenceDao getPreferenceDao();
    
    public abstract SimpleConfigDao getSimpleConfigDao();
    
    public abstract ApacheResouceDao getApacheResouceBackupDao();
    
    public abstract ApacheResouceDao getApacheServerIndexDao();
    
    public abstract ApacheResouceDao getApacheIndexBackupDao();
    
    public abstract AndroidBillingDao getAndroidBillingDao();
    
    public abstract void clearUserPersonalData();
    
    public abstract void clearInternalRMS();
    
    public abstract void clearInternalRMS(boolean needClearBrowserData);
}
