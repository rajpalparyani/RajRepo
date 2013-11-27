/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * NavBackToNavButton.java
 *
 */
package com.telenav.module.nav;

import com.telenav.sdk.kontagent.KontagentLogger;
import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.core.TnMotionEvent;
import com.telenav.tnui.core.TnUiEvent;
import com.telenav.tnui.graphics.AbstractTnGraphics;
import com.telenav.tnui.graphics.AbstractTnImage;
import com.telenav.ui.ImageDecorator;
import com.telenav.ui.citizen.map.IMapContainerConstants;
import com.telenav.ui.citizen.map.MapContainer;

/**
 *@author zhdong@telenav.cn
 *@date 2011-1-24
 */
public class NavBackToNavButton extends AbstractTnComponent
{
    MapContainer mapContainer;

    public NavBackToNavButton(int id, MapContainer mapContainer)
    {
        super(id);
        this.mapContainer = mapContainer;
    }

    protected void paint(AbstractTnGraphics graphics)
    {
        AbstractTnImage image = isFocused ? ImageDecorator.IMG_MOVING_MAP_ICON_FOCUSED.getImage() : ImageDecorator.IMG_MOVING_MAP_ICON_UNFOCUSED.getImage();
        int x = this.getWidth() / 2;
        int y = this.getHeight() / 2;
        graphics.drawImage(image, x, y, AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);
    }

    protected boolean handleUiEvent(TnUiEvent tnUiEvent)
    {
        switch (tnUiEvent.getType())
        {
            case TnUiEvent.TYPE_TOUCH_EVENT:
            {
                TnMotionEvent event = tnUiEvent.getMotionEvent();
                switch (event.getAction())
                {
                    case TnMotionEvent.ACTION_UP:
                    {
                        KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_NAVIGATION,
                            KontagentLogger.NAVIGATION_CURRENT_LOCATION_CLICKED);
                        if (mapContainer.getInteractionMode() != IMapContainerConstants.INTERACTION_MODE_FOLLOW_VEHICLE)
                        {
                            mapContainer.setMapTransitionTime(mapContainer.getFasterTransitionTime());
                            mapContainer.followVehicle();
                            mapContainer.setMapTransitionTime(mapContainer.getTransitionTime());
                        }
                        break;
                    }
                }
            }
        }

        return super.handleUiEvent(tnUiEvent);
    }
}
