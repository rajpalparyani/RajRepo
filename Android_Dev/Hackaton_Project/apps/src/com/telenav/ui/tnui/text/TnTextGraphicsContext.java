/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * TnGraphicsContext.java
 *
 */
package com.telenav.ui.tnui.text;

import com.telenav.tnui.graphics.AbstractTnFont;

/**
 * Text graphic's context when parsing text or drawing text.
 * 
 *@author fqming (fqming@telenav.cn)
 *@date Jul 30, 2010
 */
public class TnTextGraphicsContext
{
    /**
     * text's font.
     */
    public AbstractTnFont font;

    /**
     * text's color.
     */
    public int color;

    /**
     * construct text graphic's context.
     * 
     * @param font text's font
     * @param color text's color
     */
    public TnTextGraphicsContext(AbstractTnFont font, int color)
    {
        this.font = font;
        this.color = color;
    }
}
