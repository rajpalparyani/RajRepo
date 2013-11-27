/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * NavTrafficAlertContainer.java
 *
 */
package com.telenav.module.nav;

import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.datatypes.preference.Preference;
import com.telenav.module.nav.trafficengine.TrafficAlertEvent;
import com.telenav.res.ResourceManager;
import com.telenav.res.converter.StringConverter;
import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.graphics.AbstractTnFont;
import com.telenav.tnui.graphics.AbstractTnGraphics;
import com.telenav.tnui.graphics.AbstractTnImage;
import com.telenav.ui.ImageDecorator;
import com.telenav.ui.UiStyleManager;

/**
 * 
 * Traffic alert show box.
 * 
 *@author zhdong@telenav.cn
 *@date 2010-12-16
 */
public class NavTrafficAlertContainer extends AbstractTnComponent
{
    // private int severity;
    
    String distance;
    
    public NavTrafficAlertContainer(int id)
    {
        super(id);
    }

    public void update(TrafficAlertEvent alertEvent)
    {
        StringConverter converter = ResourceManager.getInstance().getStringConverter();
        // FIXME: preference is not ready yet.
        int systemUnits = ((DaoManager) DaoManager.getInstance()).getPreferenceDao().getIntValue(Preference.ID_PREFERENCE_DISTANCEUNIT);

        // severity = alertEvent.getSeverity();
        systemUnits = systemUnits > -1 ? systemUnits : 0;
        distance = converter.convertDistanceMeterToMile(alertEvent.getDistance(), systemUnits);
        
        this.requestPaint();
    }

    protected void paint(AbstractTnGraphics graphics)
    {
        int textColor = UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_WH);
        AbstractTnFont font = UiStyleManager.getInstance().getFont(UiStyleManager.FONT_NAV_SCREEN_TRAFFIC_BUTTON);
        graphics.setColor(textColor);
        graphics.setFont(font);

        AbstractTnImage alertImage = ImageDecorator.IMG_TRAFFIC_ALERT_SMALL_YELLOW_ICON_UNFOCUSED.getImage();

        int width = this.getWidth();
        int height = this.getHeight();

        int gap = 4;

        int imageX = (width - alertImage.getWidth()) / 2;
        int imageY = (height - alertImage.getHeight() - gap - font.getHeight()) / 2;

        int stringX = (width - font.stringWidth(distance)) / 2;
        int stringY = imageY + alertImage.getHeight() + gap;

        graphics.drawImage(alertImage, imageX, imageY, AbstractTnGraphics.LEFT | AbstractTnGraphics.TOP);
        graphics.drawString(distance, stringX, stringY, AbstractTnGraphics.LEFT | AbstractTnGraphics.TOP);
    }

    public int getStringWidth()
    {
    	AbstractTnFont font = UiStyleManager.getInstance().getFont(UiStyleManager.FONT_NAV_SCREEN_TRAFFIC_BUTTON);
    	return font.stringWidth(distance) + 16;
    }
}
