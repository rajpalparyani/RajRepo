/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * LocationListener.java
 *
 */
package com.telenav.module.location;

import com.telenav.location.TnLocation;

/**
 *@author bduan
 *@date 2010-11-23
 */
public interface LocationListener
{
    public void requestCompleted(TnLocation[] locations, int statusCode);
}
