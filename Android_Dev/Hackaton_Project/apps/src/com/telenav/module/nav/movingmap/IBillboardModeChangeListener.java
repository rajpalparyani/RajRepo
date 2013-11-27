/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * IBillboardModeChangeListener.java
 *
 */
package com.telenav.module.nav.movingmap;

/**
 * @author zhdong@telenav.cn
 * @date 2011-3-9
 */
public interface IBillboardModeChangeListener
{
    static int MODE_TITLE = 0;

    static int MODE_FULL = 1;

    public void billboardModeChanged(int mode);
}
