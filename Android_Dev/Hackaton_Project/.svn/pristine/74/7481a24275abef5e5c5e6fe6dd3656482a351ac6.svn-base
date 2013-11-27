/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * NavSpeedLimitContainer.java
 *
 */
package com.telenav.module.nav;

import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.datatypes.preference.Preference;
import com.telenav.datatypes.DataUtil;
import com.telenav.datatypes.route.Route;
import com.telenav.module.upsell.FeaturesManager;
import com.telenav.res.IStringNav;
import com.telenav.res.ResourceManager;
import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.graphics.AbstractTnFont;
import com.telenav.tnui.graphics.AbstractTnGraphics;
import com.telenav.ui.UiStyleManager;

/**
 * 
 * Speed limit board
 * 
 *@author zhdong@telenav.cn
 *@date 2010-12-13
 */
public class NavSpeedLimitComponent extends AbstractTnComponent
{
    
    /**
     * Unit: percent
     */
//    private int speedLimitThreshold = 15;
    
    private int currentSpeedLimit;

    private String currentUnit;

    private String speedStr;

    private String unitStr;
    
    private boolean isDisabled;
    
//    int overSpeedLimitCount;

    public NavSpeedLimitComponent(int id)
    {
        super(id);
//        speedLimitThreshold = DaoManager.getInstance().getServerDrivenParamsDao().getIntValue(
//            ServerDrivenParamsDao.NAV_SPEED_LIMIT_THRESHOLD);
    }

    public void updateSpeedLimit(boolean isSpeedLimitExceeded, int speedLimit)
    {
        Preference speedLimits = ((DaoManager) DaoManager.getInstance()).getPreferenceDao().getPreference(
            Preference.ID_PREFERENCE_SPEED_LIMITS);
        isDisabled = speedLimits != null && speedLimits.getIntValue() == Preference.SPEED_LIMIT_OFF;

        int speedLimitFeature = FeaturesManager.getInstance().getStatus(FeaturesManager.FEATURE_CODE_NAV_SPEED_LIMIT);
        boolean isEnabled = speedLimitFeature == FeaturesManager.FE_ENABLED || speedLimitFeature == FeaturesManager.FE_PURCHASED;
        isDisabled = isDisabled || !isEnabled;
        int routeStyle = DaoManager.getInstance().getTripsDao().getIntValue(Preference.ID_PREFERENCE_ROUTETYPE);
        boolean isInPedestrian=routeStyle == Route.ROUTE_PEDESTRIAN?true:false;
        isDisabled = isDisabled || isInPedestrian;
        if (isDisabled)
        {
            NavBottomStatusBarHelper.getInstance().setIsOverSpeedLimit(false);
            requestPaint();
            return;
        }
        
        Preference preference = ((DaoManager) DaoManager.getInstance()).getPreferenceDao().getPreference(
            Preference.ID_PREFERENCE_DISTANCEUNIT);

        if (preference != null && preference.getIntValue() == Preference.UNIT_USCUSTOM)
        {
            // mph
            currentSpeedLimit = DataUtil.kmToMileInHighAccuracy(speedLimit);
            // currentUnit = "MPH";
            currentUnit = ResourceManager.getInstance().getCurrentBundle().getString(IStringNav.RES_SPEED_LIMIT, IStringNav.FAMILY_NAV);
        }
        else
        {
            // kph
            currentSpeedLimit = speedLimit;
            // currentUnit = "KPH";
            currentUnit = ResourceManager.getInstance().getCurrentBundle().getString(IStringNav.RES_SPEED_LIMIT, IStringNav.FAMILY_NAV);
        }

        if (speedLimit <= 0)
        {
            speedStr = "---";
            unitStr = currentUnit;
            NavBottomStatusBarHelper.getInstance().setIsOverSpeedLimit(false);
            requestPaint();
            return;
        }

        if (isSpeedLimitExceeded)
        {
            NavBottomStatusBarHelper.getInstance().setIsOverSpeedLimit(true);
        }
        else
        {
            NavBottomStatusBarHelper.getInstance().setIsOverSpeedLimit(false);
        }
        
        speedStr = currentSpeedLimit + "";
        unitStr = currentUnit;
        
        requestPaint();
    }


    protected void paint(AbstractTnGraphics graphics)
    {
        NavBottomStatusBarHelper.getInstance().drawBackground(graphics, this, false);

        if (isDisabled)
        {
            return;
        }

        int width = this.getWidth();
        int height = this.getHeight();

        AbstractTnFont fontNumber = UiStyleManager.getInstance().getFont(UiStyleManager.FONT_SPEED_LIMIT_NUMBER);
        AbstractTnFont fontUnit = UiStyleManager.getInstance().getFont(UiStyleManager.FONT_SPEED_LIMIT);

        int gap = (height - fontNumber.getHeight() - fontUnit.getHeight()) / 3;
        int xNumber = (width - fontNumber.stringWidth(speedStr)) / 2;
        int yNumber = (height - fontNumber.getHeight() - fontUnit.getHeight() - gap) / 2;

        int xUnit = (width - fontUnit.stringWidth(unitStr)) / 2;
        int yUnit = yNumber + fontNumber.getHeight() + gap;

        graphics.setColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_WH));
        graphics.setFont(fontNumber);
        graphics.drawString(speedStr, xNumber, yNumber, AbstractTnGraphics.LEFT | AbstractTnGraphics.TOP);
        graphics.setFont(fontUnit);
        graphics.drawString(unitStr, xUnit, yUnit, AbstractTnGraphics.LEFT | AbstractTnGraphics.TOP);

    }
}
