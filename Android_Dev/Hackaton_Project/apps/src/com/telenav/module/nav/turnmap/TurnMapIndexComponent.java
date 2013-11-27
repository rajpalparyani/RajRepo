/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * CurrentStreetNameComponent.java
 *
 */
package com.telenav.module.nav.turnmap;

import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.graphics.AbstractTnFont;
import com.telenav.tnui.graphics.AbstractTnGraphics;
import com.telenav.ui.UiStyleManager;
import com.telenav.ui.tnui.text.TnTextLine;
import com.telenav.ui.tnui.text.TnTextParser;

/**
 *@author zhdong@telenav.cn
 *@date 2011-1-25
 */
public class TurnMapIndexComponent extends AbstractTnComponent
{
    String indexInfo;
    TnTextLine indexInfoLine;

    int alpha = 0x7E;

    public TurnMapIndexComponent(int id)
    {
        super(id);
    }

    protected void paint(AbstractTnGraphics graphics)
    {
        int oldColor = graphics.getColor();

        int width = this.getPreferredWidth();
        int height = this.getPreferredHeight();

        AbstractTnFont font = UiStyleManager.getInstance().getFont(UiStyleManager.FONT_MAP_NAV_TURN_INFO);
        int stringWidth = font.stringWidth(indexInfo);
        if (stringWidth > width)
        {
            stringWidth = width;
        }

        int arcWidth = height / 2;
        graphics.setAntiAlias(true);
        graphics.setColor(alpha, 0x00, 0x00, 0x00);
        graphics.fillRoundRect(0, 0, width, height, arcWidth, arcWidth);

        int x = (width - stringWidth) / 2;
        graphics.setColor(UiStyleManager.getInstance().getColor(UiStyleManager.TURN_MAP_INFO_COLOR));
        graphics.setFont(font);
        graphics.drawString(indexInfoLine.getText(), x, (height + 1) / 2, AbstractTnGraphics.FONT_VISUAL_VCENTER);
        graphics.setColor(oldColor);
    }

    public void update(String streetName)
    {
        if (!streetName.equals(this.indexInfo))
        {
            this.indexInfo = streetName;
            this.indexInfoLine = TnTextParser.parse(streetName);
            this.requestPaint();
        }
    }

    public void setAlpha(int alpha)
    {
        this.alpha = alpha;
    }

}
