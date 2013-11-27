/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * LocationCallback.java
 *
 */
package com.telenav.carconnect.provider;

import com.telenav.location.TnLocation;

/**
 * @author chihlh
 * @date Sep 18, 2012
 */
public interface ILocationCallback
{
    public void onSuccess(TnLocation loc);

    public void onError();
}
