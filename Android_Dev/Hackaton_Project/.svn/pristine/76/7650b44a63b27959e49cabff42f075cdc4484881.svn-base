/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * TrafficAlternateRouteTitile.java
 *
 */
package com.telenav.module.nav.traffic;

import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.datatypes.address.Address;
import com.telenav.data.datatypes.address.Stop;
import com.telenav.data.datatypes.preference.Preference;
import com.telenav.i18n.ResourceBundle;
import com.telenav.module.AppConfigHelper;
import com.telenav.res.IStringCommon;
import com.telenav.res.IStringDashboard;
import com.telenav.res.IStringNav;
import com.telenav.res.ResourceManager;
import com.telenav.res.converter.StringConverter;
import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.core.TnUiArgs;
import com.telenav.tnui.graphics.AbstractTnFont;
import com.telenav.tnui.graphics.AbstractTnGraphics;
import com.telenav.ui.UiStyleManager;
import com.telenav.ui.frogui.text.FrogTextHelper;
import com.telenav.ui.tnui.text.TnTextLine;
import com.telenav.ui.tnui.text.TnTextParser;

/**
 *@author zhdong@telenav.cn
 *@date 2011-1-12
 */
public class TrafficAlternateRouteTitile extends AbstractTnComponent
{

    AbstractTnFont newRouteFont;
    AbstractTnFont infoFont;
    AbstractTnFont addressTitleFont;

    String distanceDesc;
    String etaDesc;

    String addressTitle;
    String addressDetail;
    String latLine;
    String lonLine;

    protected TrafficUiDecorator uiDecorator;

    /**
     * 
     * @param id
     * @param address
     * @param eta
     * @param deltaDistance
     * 
     *            newRouteLength - currentRouteLength
     */
    public TrafficAlternateRouteTitile(int id, Address address, int newEta, int deltaDistance)
    {
        super(id);

        this.uiDecorator = new TrafficUiDecorator();

        this.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, uiDecorator.SCREEN_WIDTH);
        this.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, uiDecorator.ALTERNATE_ROUTE_TITLE_HEIGHT);

        int bgColor = UiStyleManager.getInstance().getColor(UiStyleManager.NAV_ALTERNATE_ROUTE_TITLE_COLOR);
        this.setBackgroundColor(bgColor);

        newRouteFont = UiStyleManager.getInstance().getFont(UiStyleManager.FONT_NAV_STREET_NAME);
        infoFont = UiStyleManager.getInstance().getFont(UiStyleManager.FONT_ALTERNATE_ROUTE_TITLE_INFO);
        addressTitleFont = UiStyleManager.getInstance().getFont(UiStyleManager.FONT_ALTERNATE_ROUTE_ADDRESS_LABEL);

        StringConverter converter = ResourceManager.getInstance().getStringConverter();
        int systemUnits = ((DaoManager) DaoManager.getInstance()).getPreferenceDao().getIntValue(Preference.ID_PREFERENCE_DISTANCEUNIT);

        if (deltaDistance > 0)
        {
            distanceDesc = ResourceManager.getInstance().getCurrentBundle().getString(IStringNav.RES_ADDED_DISTANCE, IStringNav.FAMILY_NAV)
                    + " " + converter.convertDistanceMeterToMile(deltaDistance, systemUnits);
        }
        else
        {
            distanceDesc = ResourceManager.getInstance().getCurrentBundle().getString(IStringNav.RES_REDUCED_DISTANCE,
                IStringNav.FAMILY_NAV)
                    + " " + converter.convertDistanceMeterToMile(-deltaDistance, systemUnits);
        }

        etaDesc = ResourceManager.getInstance().getCurrentBundle().getString(IStringNav.RES_DETOUR_ETA, IStringNav.FAMILY_NAV) + " "
                + converter.convertTime(newEta * 1000 + System.currentTimeMillis());

        if (address == null)
        {
            return;
        }
        
        Stop destStop = address.getStop();
        if (destStop != null)
        {
            Stop work = DaoManager.getInstance().getAddressDao().getOfficeAddress();
            Stop home = DaoManager.getInstance().getAddressDao().getHomeAddress();

            if (DaoManager.getInstance().getAddressDao().isSameStop(destStop, work))
            {
                addressTitle = ResourceManager.getInstance().getCurrentBundle()
                        .getString(IStringDashboard.RES_STRING_WORK, IStringDashboard.FAMILY_DASHBOARD);
            }
            else if (DaoManager.getInstance().getAddressDao().isSameStop(destStop, home))
            {
                addressTitle = ResourceManager.getInstance().getCurrentBundle()
                        .getString(IStringDashboard.RES_STRING_HOME, IStringDashboard.FAMILY_DASHBOARD);
            }
            else
            {
                addressTitle = address.getLabel();
            }
        }

        if (address != null && address.getStop() != null)
        {
            addressDetail = ResourceManager.getInstance().getStringConverter().convertAddress(address.getStop(), false);
        }

        // TODO there should be more address formatter in StringConverter
        if (address != null && address.getStop() != null)
        {
            Stop stop = address.getStop();
            if (stop.getLat() != 0 && stop.getLon() != 0)
            {
                ResourceBundle bundle = ResourceManager.getInstance().getCurrentBundle();
                String lat = ResourceManager.getInstance().getStringConverter().convertLatLon(stop.getLat());
                String lon = ResourceManager.getInstance().getStringConverter().convertLatLon(stop.getLon());
                latLine = bundle.getString(IStringCommon.RES_LAT, IStringCommon.FAMILY_COMMON);
                latLine += ": " + lat ;
                lonLine = bundle.getString(IStringCommon.RES_LON, IStringCommon.FAMILY_COMMON);
                lonLine += ": " + lon;
            }
        }
    }

    protected void paint(AbstractTnGraphics graphics)
    {
        String newRoute = ResourceManager.getInstance().getCurrentBundle().getString(IStringNav.RES_NEW_ROUTE, IStringNav.FAMILY_NAV);
        
        int width = this.getWidth();
        int newRouteY = ((TrafficUiDecorator)uiDecorator).SUGGEST_ROUTE_HEADER_ARC.getInt();

        int newRouteX;
        if (AppConfigHelper.isTabletSize())
        {
            newRouteX = AppConfigHelper.getDisplayWidth() * 328 / 10000;
        }
        else
        {
            newRouteX = newRouteFont.getMaxWidth()/4;
        }

        // draw New Route
        graphics.setColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_WH));
        graphics.setFont(newRouteFont);
        graphics.drawString(newRoute, newRouteX, newRouteY, AbstractTnGraphics.LEFT | AbstractTnGraphics.TOP);

        int distanceAndEtaX = width -newRouteX;
        int distanceAndEtaY = newRouteY + newRouteFont.getDescent();
        // draw distanceDesc
        graphics.setFont(infoFont);
        graphics.drawString(distanceDesc, distanceAndEtaX, distanceAndEtaY, AbstractTnGraphics.RIGHT | AbstractTnGraphics.TOP);

        // draw etaDesc
        graphics.setFont(infoFont);
        graphics.drawString(etaDesc, distanceAndEtaX, distanceAndEtaY + infoFont.getHeight(), AbstractTnGraphics.RIGHT | AbstractTnGraphics.TOP);

        // draw background board for address
        int addressMaxWidth = width - 2 * newRouteX;
        if (addressTitle != null && addressTitle.length() > 0)
        {
            // draw addressTitle
            int addressTitleY = newRouteY + newRouteFont.getHeight();

            graphics.setColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_SUGGESTED_ROUTE_DEST_ABBR_COLOR));
            graphics.setFont(addressTitleFont);
            TnTextLine addressTitleLine = TnTextParser.parse(addressTitle);
            FrogTextHelper.paint(graphics, newRouteX, newRouteY + newRouteFont.getHeight(), addressTitleLine, addressTitleFont, addressTitleFont,
                addressMaxWidth, true);

            // draw firstLine
            int firstLineY = addressTitleY + addressTitleFont.getHeight();
            graphics.setColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_SUGGESTED_ROUTE_DEST_DETAIL_COLOR));
            graphics.setFont(infoFont);
            TnTextLine firstLineLine = TnTextParser.parse(addressDetail);
            FrogTextHelper.paint(graphics, newRouteX, firstLineY, firstLineLine, infoFont, infoFont, addressMaxWidth, true);
        }
        else
        {
            if(addressDetail != null && addressDetail.trim().length() > 0)
            {
                paintDetail(graphics, newRouteX, addressMaxWidth, addressDetail);
            }
            else
            {
                paintDetail(graphics, newRouteX, addressMaxWidth, latLine + ", " + lonLine);
            }
        }

    }

    private void paintDetail(AbstractTnGraphics graphics, int addressLineStartX, int addressMaxWidth, String line)
    {
        AbstractTnFont detailFont = addressTitleFont;
        int addressLineStartY = this.getHeight() - newRouteFont.getHeight();
        graphics.setColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_SUGGESTED_ROUTE_DEST_DETAIL_COLOR));
        graphics.setFont(detailFont);

        TnTextLine lineLine = TnTextParser.parse(line);
        FrogTextHelper.paint(graphics, addressLineStartX, addressLineStartY, lineLine, detailFont, detailFont, addressMaxWidth, true);

    }

}
