/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * IAndroidBaseActivity.java
 *
 */
package com.telenav.tnui.core.android;

import com.telenav.tnui.core.TnScreen;

/**
 * The base interface for the whole android application
 * 
 *@author fqming (fqming@telenav.cn)
 *@date 2011-8-30
 */
public interface IAndroidBaseActivity
{
    /**
     * Set the screen content to an explicit screen. This screen is placed directly into the activity's view hierarchy.
     * It can itself be a complex view hierarchy.
     * 
     * @param screen
     */
    public void showScreen(TnScreen screen);

    /**
     * Retrieve current screen.
     * 
     * @return TnScreen
     */
    public TnScreen getCurrentScreen();

    /**
     * Retrieve status's bar height
     * 
     * @param position
     * @return int
     */
    public int getStatusBarHeight(int position);
}
