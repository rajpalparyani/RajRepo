/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * NavBottomStatusBarHelper.java
 *
 */
package com.telenav.module.nav;

import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.graphics.AbstractTnGraphics;
import com.telenav.ui.UiStyleManager;

/**
 *@author zhdong@telenav.cn
 *@date 2011-2-22
 */
public class NavBottomStatusBarHelper
{
    static int STATUS_NORMAL = 0;
    static int STATUS_SPEED_LIMIT = 1;
    static int STATUS_WEAK_GPS = 2;
    static int STATUS_OUT_OF_COVERAGE = 3;
    
    boolean isNoGps;
    boolean isOverSpeedLimit;
    boolean isOutofCoverage;
    
    int[] colorBg = new int[]
    { UiStyleManager.getInstance().getColor(UiStyleManager.MOVING_MAP_BUTTOM_BAR_BG_COLOR), 0xFF990000, 0xFF808080,0xFF808080 };
    int[] colorLine = new int[]
    { UiStyleManager.getInstance().getColor(UiStyleManager.MOVING_MAP_BUTTOM_BAR_LINE_COLOR), 0xFF660000, 0xFF666666,0xFF666666 };

    private static NavBottomStatusBarHelper helper = new NavBottomStatusBarHelper();

    private NavBottomStatusBarHelper()
    {

    }

    public static NavBottomStatusBarHelper getInstance()
    {
        return helper;
    }

    public void setIsNoGps(boolean isNoGps)
    {
        this.isNoGps = isNoGps;
    }

    public void setIsOutofCoverage(boolean isOutofCoverage)
    {
        this.isOutofCoverage = isOutofCoverage;
    }
    
    public void setIsOverSpeedLimit(boolean isOverSpeedLimit)
    {
        this.isOverSpeedLimit = isOverSpeedLimit;
    }

    public void drawBackground(AbstractTnGraphics graphics, AbstractTnComponent component, boolean isSeparatorNeeded)
    {
        int width = component.getWidth();
        int height = component.getHeight();

        int status = STATUS_NORMAL;

        if (isNoGps)
        {
            status = STATUS_WEAK_GPS;
        }
        else if (isOverSpeedLimit)
        {
            status = STATUS_SPEED_LIMIT;
        }
        else if (isOutofCoverage)
        {
            status = STATUS_OUT_OF_COVERAGE;
        }

        graphics.setColor(colorBg[status]);
        graphics.fillRect(0, 0, width, height);

        if (isSeparatorNeeded)
        {
            graphics.setColor(colorLine[status]);
            graphics.drawLine(width - 1, 0, width - 1, height - 1);
        }
    }

}
