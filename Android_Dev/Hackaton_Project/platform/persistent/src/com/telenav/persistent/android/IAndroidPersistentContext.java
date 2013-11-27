/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * IAndroidPersistentContext.java
 *
 */
package com.telenav.persistent.android;

import android.content.Context;

import com.telenav.persistent.ITnPersistentContext;

/**
 * This class provider some necessary persistent information at android platform.
 * 
 *@author fqming (fqming@telenav.cn)
 *@date 2010-7-2
 */
public interface IAndroidPersistentContext extends ITnPersistentContext
{
    /**
     * Get the context object of this application.
     * 
     * @return Context Interface to global information about an application environment.
     */
    public Context getContext();
}
