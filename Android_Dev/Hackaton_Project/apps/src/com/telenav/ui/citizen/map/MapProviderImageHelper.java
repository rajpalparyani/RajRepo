/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * MapProviderImageHelper.java
 *
 */
package com.telenav.ui.citizen.map;

import com.telenav.data.dao.misc.DaoManager;
import com.telenav.tnui.graphics.AbstractTnFont;
import com.telenav.tnui.graphics.AbstractTnGraphics;
import com.telenav.tnui.graphics.AbstractTnGraphicsHelper;
import com.telenav.tnui.graphics.AbstractTnImage;
import com.telenav.tnui.graphics.TnRect;
import com.telenav.ui.ImageDecorator;
import com.telenav.ui.UiFactory;
import com.telenav.ui.UiStyleManager;
import com.telenav.ui.citizen.android.AndroidCitizenUiHelper;

/**
 * @author yren
 * @date 2012-9-13
 */
public class MapProviderImageHelper
{
    private static final int RECT_HEIGHT = 27;
    private static final int RECT_WIDTH = 68;
    static String widthIndex = "o";
    public static AbstractTnImage creatImage(int[] argbData, int width, int height, int anchor, boolean needLogo)
    {
        if (argbData == null || argbData.length < 1 || width < 1 || height < 1)
        {
            return null;
        }

        AbstractTnImage image = AbstractTnGraphicsHelper.getInstance().createImage(width, height);
        image.clear(0x0);
        AbstractTnGraphics g = image.getGraphics();
        g.drawRGB(argbData, 0, 0, 0, 0, width, height, false);
        String mapCopyright = DaoManager.getInstance().getStartupDao().getMapCopyright();
        AbstractTnFont font = UiStyleManager.getInstance().getFont(UiStyleManager.FONT_MAP_PROVIDER);
        AbstractTnFont suitableFont = UiFactory.getInstance().getSuitableFont(4 * width / 5, font, mapCopyright);
        int x = 0;
        int y = 0;
        int strLen = suitableFont.stringWidth(mapCopyright);
        int strHeight = suitableFont.getHeight();
        int padding = suitableFont.stringWidth(widthIndex);
        if ((anchor & AbstractTnGraphics.HCENTER) != 0)
        {
            x = (width - strLen) >> 1;
        }

        if ((anchor & AbstractTnGraphics.VCENTER) != 0)
        {
            y = (height - strHeight) >> 1;
        }

        if ((anchor & AbstractTnGraphics.RIGHT) != 0)
        {
            x = width - strLen - padding;
        }

        if ((anchor & AbstractTnGraphics.BOTTOM) != 0)
        {
            y = height - padding;
        }

        if ((anchor & AbstractTnGraphics.LEFT) != 0)
        {
            x = padding;
        }

        if ((anchor & AbstractTnGraphics.TOP) != 0)
        {
            y = padding;
        }
        int color =  DayNightService.getInstance().getMapColor() == IMapContainerConstants.MAP_NIGHT_COLOR ? UiStyleManager.getInstance().getColor(
            UiStyleManager.MAP_PROVIDER_TEXT_NIGHT_COLOR): UiStyleManager.getInstance().getColor(
                UiStyleManager.MAP_PROVIDER_TEXT_DAY_COLOR);
        g.setColor(color);
        g.setFont(suitableFont);
        g.drawString(mapCopyright, x, y, AbstractTnGraphics.LEFT | AbstractTnGraphics.BASE_LINE);
        
        if(needLogo)
        {
            x=padding;
            //based on VDD
            int rectWidth=   AndroidCitizenUiHelper.getPixelsByDip(RECT_WIDTH);
            int rectHeight=  AndroidCitizenUiHelper.getPixelsByDip(RECT_HEIGHT);
            AbstractTnImage logo = DayNightService.getInstance().getMapColor() == IMapContainerConstants.MAP_NIGHT_COLOR ? ImageDecorator.IMG_NIGHT_LOGO_ON_MAP
                    .getImage() : ImageDecorator.IMG_DAY_LOGO_ON_MAP.getImage();
            g.drawImage(logo, null, new TnRect(x , y - rectHeight, x+rectWidth, y));

        }
        return image;
    }

}
