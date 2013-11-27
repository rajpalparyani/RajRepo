/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * NavCompassContainer.java
 *
 */
package com.telenav.module.nav;

import com.telenav.data.serverproxy.impl.navsdk.NavSdkMapProxy;
import com.telenav.res.ResourceManager;
import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.core.TnMotionEvent;
import com.telenav.tnui.core.TnUiEvent;
import com.telenav.tnui.graphics.AbstractTnFont;
import com.telenav.tnui.graphics.AbstractTnGraphics;
import com.telenav.tnui.graphics.AbstractTnImage;
import com.telenav.ui.ImageDecorator;
import com.telenav.ui.UiStyleManager;
import com.telenav.ui.citizen.map.IMapContainerConstants;
import com.telenav.ui.citizen.map.MapContainer;

/**
 *@author zhdong@telenav.cn
 *@date 2010-12-21
 */
public class NavCompassComponent extends AbstractTnComponent implements NavZoomButton.IManualZoomLevelChangeListener
{
    private MapContainer mapContainer;
    int heading;
    IAdiModeHelper adiModeHelper;
    boolean isManual3dZoomLevelChanged;
    boolean isManual2dZoomLevelChanged;

    public NavCompassComponent(int id, MapContainer mapContainer,IAdiModeHelper adiModeHelper)
    {
        super(id);
        this.mapContainer = mapContainer;
        this.adiModeHelper = adiModeHelper;
        renderingMode = mapContainer.getRenderingModeFromTripDao();
    }

    public void update(int heading)
    {
        this.heading = heading;
        requestPaint();
    }

    protected void paint(AbstractTnGraphics graphics)
    {
        NavBottomStatusBarHelper.getInstance().drawBackground(graphics, this, true);
        
        int width = this.getWidth();
        int height = this.getHeight();
        
        AbstractTnImage image = null;
        if (renderingMode == NavSdkMapProxy.RENDERING_MODE_2D_HEAD_UP || adiModeHelper.isAdiMode())
        {
            image = ImageDecorator.IMG_COMPASS_2D.getImage();
        }
        else
        {
            image = convertImage(heading);
        }
        
        AbstractTnFont font = UiStyleManager.getInstance().getFont(UiStyleManager.FONT_NAV_INFO_BAR_BOLD);
        graphics.setFont(font);
        graphics.setColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_WH));
        String desc = ResourceManager.getInstance().getStringConverter().convertHeading(heading);
        
        int gap = 5;

        int descWidth = font.stringWidth(desc);
        int imageWidth = image.getWidth();
        int xImage = (width - imageWidth - descWidth - gap) / 2;
        int yImage = (height - image.getHeight()) / 2;

        graphics.drawImage(image, xImage, yImage, AbstractTnGraphics.LEFT | AbstractTnGraphics.TOP);

        int xDesc = xImage + imageWidth + gap;
        int yDesc = (height - font.getHeight()) / 2;
        graphics.drawString(desc, xDesc, yDesc, AbstractTnGraphics.LEFT | AbstractTnGraphics.TOP);
        
    }

    public AbstractTnImage convertImage(int heading)
    {
        // make sure heading angle > 0
        while (heading < 0)
        {
            heading += 360;
        }

        int index = (2 * heading + 45) / 90;
        index = index % 8;
        switch (index)
        {
            case 0:
                return ImageDecorator.IMG_COMPASS_N.getImage();
            case 1:
                return ImageDecorator.IMG_COMPASS_NE.getImage();
            case 2:
                return ImageDecorator.IMG_COMPASS_E.getImage();
            case 3:
                return ImageDecorator.IMG_COMPASS_SE.getImage();
            case 4:
                return ImageDecorator.IMG_COMPASS_S.getImage();
            case 5:
                return ImageDecorator.IMG_COMPASS_SW.getImage();
            case 6:
                return ImageDecorator.IMG_COMPASS_W.getImage();
            case 7:
                return ImageDecorator.IMG_COMPASS_NW.getImage();
        }
        return ImageDecorator.IMG_COMPASS_N.getImage();
    }
    
    int renderingMode = NavSdkMapProxy.RENDERING_MODE_3D_HEAD_UP;

    public boolean handleUiEvent(TnUiEvent tnUiEvent)
    {
        if (tnUiEvent.getType() == TnUiEvent.TYPE_TOUCH_EVENT)
        {
            TnMotionEvent motionEvent = tnUiEvent.getMotionEvent();
            switch (motionEvent.getAction())
            {
                case TnMotionEvent.ACTION_UP:
                {
                    if(adiModeHelper.isAdiMode())
                    {
                        return true;
                    }
                    switch (renderingMode)
                    {
                        case NavSdkMapProxy.RENDERING_MODE_3D_HEAD_UP:
                        {
                            renderingMode = NavSdkMapProxy.RENDERING_MODE_2D_HEAD_UP;
                            MapContainer.getInstance().saveRenderingModeInTripsDao(renderingMode);
                            MapContainer.getInstance().setRenderingMode(renderingMode);
                            if (!isManual2dZoomLevelChanged)
                            {
                                mapContainer.setZoomLevel((int) MapContainer.NAV_2D_DEFAULT_ZOOM_LEVEL, true);
                            }

                            return true;
                        }
                        case NavSdkMapProxy.RENDERING_MODE_2D_HEAD_UP:
                        {
                            renderingMode = NavSdkMapProxy.RENDERING_MODE_3D_HEAD_UP;
                            MapContainer.getInstance().saveRenderingModeInTripsDao(renderingMode);
                            MapContainer.getInstance().setRenderingMode(renderingMode);
                            if (!isManual3dZoomLevelChanged)
                            {
                                mapContainer.setZoomLevel(MapContainer.NAV_3D_DEFAULT_ZOOM_LEVEL, true);
                            }
                            return true;
                        }
                    }
                    break;
                }
            }
        }
        return super.handleUiEvent(tnUiEvent);
    }
    
    public void onManualZoomLevelChanged()
    {
        int renderingMode = MapContainer.getInstance().getRenderingMode();
        if(renderingMode == IMapContainerConstants.RENDERING_MODE_3D_HEADING_UP || renderingMode == IMapContainerConstants.RENDERING_MODE_3D_NORTH_UP)
        {
            isManual3dZoomLevelChanged = true;
        }
        else if (renderingMode == IMapContainerConstants.RENDERING_MODE_2D_HEADING_UP || renderingMode == IMapContainerConstants.RENDERING_MODE_2D_NORTH_UP)
        {
            isManual2dZoomLevelChanged = true;
        }
    }

}
