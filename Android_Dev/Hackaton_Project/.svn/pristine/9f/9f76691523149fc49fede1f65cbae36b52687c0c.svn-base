/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * ResourceManager.java
 *
 */
package com.telenav.res;

import com.telenav.i18n.Locale;
import com.telenav.i18n.ResourceBundle;
import com.telenav.io.TnIoManager;
import com.telenav.persistent.TnPersistentManager;
import com.telenav.res.converter.StringConverter;
import com.telenav.res.converter.StringConverter_en_US;
import com.telenav.tnui.core.AbstractTnI18nProvider;

/**
 *@author fqming (fqming@telenav.cn)
 *@date Aug 9, 2010
 */
public class ResourceManager extends AbstractTnI18nProvider
{
    private static ResourceManager instance = new ResourceManager();
    
    private String currentLocale = Locale.en_US;
    private ResourceBundle currentBundle;
    private StringConverter stringConverter;
    
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
            currentBundle = new AppResourceBundle(this.currentLocale, TnIoManager.getInstance(), TnPersistentManager.getInstance());
        }

        return currentBundle;
    }
    
    /**
     * Retrieve the string converter.
     * 
     * @return StringConverter.java
     */
    public synchronized StringConverter getStringConverter()
    {
        if (stringConverter == null)
        {
//            if(Locale.en_US.equals(this.currentLocale))
//            {
//                stringConverter = new StringConverter_en_US();
//            }
//            else if(Locale.es_MX.equals(this.currentLocale))
//            {
//                stringConverter = new StringConverter_es_MX();
//            }
//            else
//            {
                stringConverter = new StringConverter_en_US();
//            }
        }

        return stringConverter;
    }
    
    /**
     * set current locale.
     * 
     * @param locale
     */
    public synchronized void setLocale(String locale)
    {
        this.currentLocale = locale;
        
        this.stringConverter = null;
        this.stringConverter = getStringConverter();
        
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
