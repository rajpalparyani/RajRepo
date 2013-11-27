/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * FrogScreen.java
 *
 */
package com.telenav.ui.frogui;

import com.telenav.tnui.core.AbstractTnContainer;
import com.telenav.tnui.core.TnScreen;
import com.telenav.tnui.widget.TnLinearContainer;

/**
 * providing features common to these applications will used frog ui. <br />
 * <b>Behaviour</b> <br />
 * Frog screen objects contain root container.
 * 
 *@author fqming (fqming@telenav.cn)
 *@date 2010-6-29
 */
public class FrogScreen extends TnScreen
{
    /**
     * construct a frog screen with special screen's id.
     * 
     * @param screenId the screen's id.
     */
    public FrogScreen(int screenId)
    {
        super(screenId);
    }

    protected AbstractTnContainer createRootContainer()
    {
        TnLinearContainer linearContainer = new TnLinearContainer(-1, true);

        return linearContainer;
    }
}
