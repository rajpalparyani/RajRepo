/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * ITnPersistentContext.java
 *
 */
package com.telenav.persistent;

/**
 * This class will provider the necessary information of persistent system.
 * <br />
 * <b>Must be implemented at application layer.</b>
 * <br />
 * For different platform, maybe you need implement different interface at application layer.
 * For example: {@link IAndroidDatabasePersistentContext} etc.
 * 
 *@author fqming (fqming@telenav.cn)
 *@date 2010-7-2
 */
public interface ITnPersistentContext
{
    /**
     * Get the application name in the store system.
     * <b>The application name should not include any space char.</b>
     * 
     * @return string
     */
    public String getApplicationName();
}
