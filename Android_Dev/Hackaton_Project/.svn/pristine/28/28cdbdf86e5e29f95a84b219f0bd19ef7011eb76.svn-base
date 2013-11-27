/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * ResourceManager.java
 *
 */
package com.telenav.searchwidget.res;

import com.telenav.i18n.Locale;
import com.telenav.i18n.ResourceBundle;
import com.telenav.io.TnIoManager;
import com.telenav.persistent.TnPersistentManager;
import com.telenav.searchwidget.app.AppConfigHelper;
import com.telenav.tnui.core.AbstractTnI18nProvider;

/**
 *@author fqming (fqming@telenav.cn)
 *@date Aug 9, 2010
 */
public class ResourceManager extends AbstractTnI18nProvider
{
    private static ResourceManager instance = new ResourceManager();
    
    private String currentLocale = AppConfigHelper.defaultLocale;
    private ResourceBundle currentBundle;
    
    public static ResourceManager getInstance()
    {
        return instance;
    }
    
    /**
     * Retrieve the current resource bundle.
     * 
     * @return ResourceBundle
     */
    public synchronized ResourceBundle getCurrentBundle()
    {
        if (currentBundle == null)
        {
            currentBundle = new ResourceBundle(this.currentLocale, TnIoManager.getInstance(), TnPersistentManager.getInstance());
        }

        return currentBundle;
    }
    
    /**
     * set current locale.
     * 
     * @param locale
     */
    public synchronized void setLocale(String locale)
    {
        this.currentLocale = locale;
        
        this.currentBundle = getCurrentBundle();
        
        if(this.currentBundle != null)
        {
            this.currentBundle.setLocale(locale);
        }
    }

    public String getCurrentLocale()
    {
        return this.currentLocale;
    }
    
    protected String getTextDelegate(int key, String familyName)
    {
        return getCurrentBundle().getString(key, familyName);
    }
}
