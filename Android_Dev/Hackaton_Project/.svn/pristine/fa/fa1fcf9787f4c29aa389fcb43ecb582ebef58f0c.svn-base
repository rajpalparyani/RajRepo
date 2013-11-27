/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * AbstractTnI18nProvider.java
 *
 */
package com.telenav.tnui.core;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2010-12-15
 */
public abstract class AbstractTnI18nProvider
{
    public final String getText(int key, String familyName)
    {
        return getTextDelegate(key, familyName);
    }

    public abstract String getCurrentLocale();
    
    protected abstract String getTextDelegate(int key, String familyName);
}
