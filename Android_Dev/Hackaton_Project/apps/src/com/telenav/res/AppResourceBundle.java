/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * AppResourceBundle.java
 *
 */
package com.telenav.res;

import com.telenav.i18n.ResourceBundle;
import com.telenav.io.TnIoManager;
import com.telenav.logger.Logger;
import com.telenav.module.AppConfigHelper;
import com.telenav.persistent.TnPersistentManager;

/**
 *@author bmyang
 *@date 2012-2-20
 */
public class AppResourceBundle extends ResourceBundle
{
    
    public AppResourceBundle(String locale, TnIoManager ioManager,
            TnPersistentManager persistentManager)
    {
        super(locale, ioManager, persistentManager);
    }
    
    protected byte[] getImageFromFile(String name, String family)
    {
        if (!ResourceUtil.isImageFileExist(name, family, getLocale()))
        {
            Logger.log(Logger.INFO, this.getClass().getName(), "can't find file for: " + name);
            return null;
        }
        return super.getImageFromFile(name, family);
    }

    protected byte[] getGenericImageFromFile(String name, String family)
    {
        if (!ResourceUtil.isImageFileExist(name, family, GENERIC_PATH))
        {
            Logger.log(Logger.INFO, this.getClass().getName(), "can't find file for: " + name);
            return null;
        }
        return super.getGenericImageFromFile(name, family);
    }
    
    public String getString(int key, String familyName)
    {
        String str = super.getString(key, familyName);
        if (str == null || str.trim().length() == 0)
        {
            int productBase = getProductBase();
            str = super.getString(key + productBase, IStringXProduct.FAMILY_XPRODUCT);
        }
        return str;
    }
    
    protected int getProductBase()
    {
        String brandName = AppConfigHelper.brandName;
        int productBase = 0;
        if(AppConfigHelper.BRAND_ATT.equals(brandName))
        {
            productBase = IStringXProduct.XPRODUCT_ATT_BASE;
        }
        else if(AppConfigHelper.BRAND_MMI.equals(brandName))
        {
            productBase = IStringXProduct.XPRODUCT_MMI_BASE;
        }
        else if(AppConfigHelper.BRAND_SCOUT_EU.equals(brandName))
        {
            productBase = IStringXProduct.XPRODUCT_SCOUTEU_BASE;
        }
        else if(AppConfigHelper.BRAND_SCOUT_US.equals(brandName))
        {
            productBase = IStringXProduct.XPRODUCT_SCOUTUS_BASE;
        }
        else if(AppConfigHelper.BRAND_SPRINT.equals(brandName))
        {
            productBase = IStringXProduct.XPRODUCT_SPRINT_BASE;
        }
        else if(AppConfigHelper.BRAND_TMOBILEUK.equals(brandName))
        {
            productBase = IStringXProduct.XPRODUCT_TMOUK_BASE;
        }
        else if(AppConfigHelper.BRAND_TMOBILEUS.equals(brandName))
        {
            productBase = IStringXProduct.XPRODUCT_TMOUS_BASE;
        }
        else if(AppConfigHelper.BRAND_USCC.equals(brandName))
        {
            productBase = IStringXProduct.XPRODUCT_USCC_BASE;
        }
        else if(AppConfigHelper.BRAND_VIVO.equals(brandName))
        {
            productBase = IStringXProduct.XPRODUCT_VIVO_BASE;
        }
        else if(AppConfigHelper.BRAND_TELCEL.equals(brandName))
        {
            productBase = IStringXProduct.XPRODUCT_TELCEL_BASE;
        }
        else if(AppConfigHelper.BRAND_BELL.equals(brandName))
        {
            productBase = IStringXProduct.XPRODUCT_BELL_BASE;
        }
        else if(AppConfigHelper.BRAND_ROGERS.equals(brandName))
        {
            productBase = IStringXProduct.XPRODUCT_ROGERS_BASE;
        }
        return productBase;
    }
}
