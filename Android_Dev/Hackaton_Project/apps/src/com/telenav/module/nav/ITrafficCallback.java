/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * ITrafficCallback.java
 *
 */
package com.telenav.module.nav;

import com.telenav.data.datatypes.nav.NavSdkNavState;

/**
 * 
 * This callback provide a bridge between moving map module and traffic module.
 * 
 *@author zhdong@telenav.cn
 *@date 2010-12-14
 */
public interface ITrafficCallback
{
    public NavSdkNavState getCurrentNavState();
}
