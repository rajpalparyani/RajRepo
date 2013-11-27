/*
 * (c) Copyright 2010 by TeleNav, Inc.
 * All Rights Reserved.
 *
 */
package com.telenav.ui.citizen.map;

import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.dao.misc.SimpleConfigDao;
import com.telenav.data.serverproxy.NetworkStatusManager;
import com.telenav.logger.Logger;
import com.telenav.module.AppConfigHelper;
import com.telenav.module.map.IMapConstants;
import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.core.AbstractTnUiHelper;
import com.telenav.tnui.core.TnCommandEvent;
import com.telenav.tnui.core.TnMotionEvent;
import com.telenav.tnui.core.TnUiArgs;
import com.telenav.tnui.core.TnUiArgs.ITnUiArgsDecorator;
import com.telenav.tnui.core.TnUiArgs.TnUiArgAdapter;
import com.telenav.tnui.core.TnUiEvent;
import com.telenav.tnui.graphics.AbstractTnGraphics;
import com.telenav.tnui.graphics.TnNinePatchImage;
import com.telenav.ui.ImageDecorator;
import com.telenav.ui.NinePatchImageDecorator;
import com.telenav.util.PrimitiveTypeCache;

/**
 * 
 * @author JY Xu, created on Aug 27, 2010
 */
public class MapViewListComponent extends AbstractTnComponent
{
    private boolean isShowTrafficOverlay;

    private boolean isShowSatelliteOverlay;

    private boolean isShowTrafficCameraOverlay;

    private boolean isShowLandMarkOverlay;
    
    private boolean hasTrafficOverlay;

    private boolean hasSatelliteOverlay;

    private boolean hasTrafficCameraOverlay;

//    private boolean hasLandMarkOverlay;
    
//    private int indexTrafficOverlay;

    private int indexTrafficCameraOverlay;
    
    private int indexSatelliteOverlay;

//    private int indexLandMarkOverlay;
    
    private int leftMargin;

    private int rigthMargin;

    private int topMargin;

    private int bottomMargin;

    private int gap;

    private int iconWidth;

    private int iconHeight;

    private int bigTrayIconWidth;

    private int bigTrayIconHeight;

    private int smallTrayIconWidth;

    private int smallTrayIconHeight;
    
    private int iconOffset;

    private boolean isListVisible = false;

    /******* List is not visible *******/
    private int oldTrayLeft;

    private int oldTrayTop;

    private int oldTrayWidth;

    private int oldTrayHeight;

    private int totalOverLayers;
    
    private boolean isDayColor = true;
    
    private long lastCurrentPressSatelliteTime;
    
    public MapViewListComponent(int id, boolean hasTrafficOverlay, boolean hasTrafficCameraOverlay, boolean hasSatelliteOverlay)
    {
        super(id);
        int layerSetting = ((DaoManager)DaoManager.getInstance()).getSimpleConfigDao().get(SimpleConfigDao.KEY_MAP_LAYER_SETTING);
        if(layerSetting < 0)
        {
            layerSetting = 0;
        }
        initMapLayerSettings(layerSetting);

        iconWidth = ImageDecorator.MAP_CAMERA_ICON_FOCUSED.getImage().getWidth();
        iconHeight = iconWidth;
        
        int width = AppConfigHelper.getMinDisplaySize();

        bigTrayIconWidth = width * 48 / 320;
        bigTrayIconHeight = bigTrayIconWidth;
        
        iconOffset = width * 10 / 480;
        
        int smallMapIconHeight = ImageDecorator.MAP_SMALL_MAP_LAYER_ICON_UNFOCUSED.getImage().getHeight();
        smallTrayIconWidth = smallMapIconHeight + smallMapIconHeight * 1/4;
        if(smallMapIconHeight * 1/4 < 10)//this if for 320x480
        {
            smallTrayIconWidth = smallTrayIconWidth + 6;
        }
        smallTrayIconHeight = smallTrayIconWidth;

        leftMargin = iconWidth * 1/4;
        rigthMargin =  topMargin = leftMargin;
        gap = iconWidth * 7/36;
        
        bottomMargin = iconWidth * (14 + 14) / 36;
        
        this.hasTrafficOverlay =  hasTrafficOverlay;
        this.hasTrafficCameraOverlay =  hasTrafficCameraOverlay;
        this.hasSatelliteOverlay =  hasSatelliteOverlay;

        int index = 0;
        if(hasTrafficOverlay) 
        {
//            indexTrafficOverlay = index;
            index ++;
        }
        if(hasTrafficCameraOverlay) 
        {
            indexTrafficCameraOverlay = index;
            index ++;
        }
        if(hasSatelliteOverlay) 
        {
            indexSatelliteOverlay = index;
            index ++;
        }
        totalOverLayers = index;
        setFocusable(true);
    }

    public void setMapColor(boolean isDayColor)
    {
        this.isDayColor = isDayColor;
    }
    
    public boolean isInsideShowTrafficIcon(int x, int y)
    {
        if(!hasTrafficOverlay) return false;
        if (isPortraitMode())
            return isInsideBoundingBox(leftMargin, topMargin, leftMargin + iconWidth, topMargin + iconHeight, x, y);
        else
            return isInsideBoundingBox(smallTrayIconWidth /2 + bottomMargin + (totalOverLayers-1) * (gap + iconWidth), leftMargin, smallTrayIconWidth /2 + bottomMargin +  (totalOverLayers-1) * (gap + iconWidth) + iconWidth, leftMargin + iconHeight,  x, y);
    }

    public boolean isInsideShowTrafficCameraIcon(int x, int y)
    {
        if(!hasTrafficCameraOverlay) return false;
        if (isPortraitMode())
            return isInsideBoundingBox(leftMargin, topMargin + indexTrafficCameraOverlay*(gap + iconHeight), leftMargin + iconWidth, topMargin + indexTrafficCameraOverlay*(gap + iconHeight)
                    + iconHeight, x, y);
        else
            return isInsideBoundingBox(smallTrayIconWidth /2 + bottomMargin + (totalOverLayers-1-indexTrafficCameraOverlay)*(gap + iconWidth), leftMargin, smallTrayIconWidth /2 + bottomMargin + (totalOverLayers-1-indexTrafficCameraOverlay)*(gap + iconWidth) + iconWidth, leftMargin + iconHeight, x, y);
    }

    public boolean isInsideShowSatelliteIcon(int x, int y)
    {
        if(!hasSatelliteOverlay) return false;
        if (isPortraitMode())
            return isInsideBoundingBox(leftMargin, topMargin + (totalOverLayers-1) * (gap + iconWidth), leftMargin + iconWidth, topMargin + (totalOverLayers-1)  * (gap + iconHeight) + iconHeight, x, y);
        else
            return isInsideBoundingBox(smallTrayIconWidth /2 + bottomMargin, leftMargin,  smallTrayIconWidth /2 + bottomMargin + iconWidth, leftMargin + iconHeight, x, y);
    }

    public boolean isInsideTray(int x, int y)
    {
        if (isPortraitMode())
        {
            if (isListVisible)
            {
                return isInsideBoundingBox(this.getWidth() / 2 - smallTrayIconWidth / 2, this.getHeight() - smallTrayIconWidth, this.getWidth()
                        / 2 + smallTrayIconWidth / 2, this.getHeight(), x, y);
            }
            else
            {
                return true;
            }
        }
        else
        {
            if (isListVisible)
            {
                return isInsideBoundingBox(0, this.getHeight() / 2 - smallTrayIconHeight / 2,  smallTrayIconWidth
                        , this.getHeight() / 2 + smallTrayIconHeight / 2, x, y);
            }
            else
            {
                return true;
            }
        }
    }

    public int getWidth()
    {
        if (isPortraitMode())
        {
            if (isListVisible)
                return leftMargin + iconWidth + rigthMargin;
            else
                return bigTrayIconWidth;
        }
        else
        {
            if (isListVisible)
                return topMargin + iconWidth * totalOverLayers + gap * (totalOverLayers -1) + bottomMargin + smallTrayIconHeight / 2;
            else
                return bigTrayIconWidth;
        }
    }

    public int getHeight()
    {
        if (isPortraitMode())
        {
            if (isListVisible)
                return topMargin + iconHeight * totalOverLayers + gap * (totalOverLayers-1) + bottomMargin + smallTrayIconHeight / 2;
            else
                return bigTrayIconHeight;
        }
        else
        {
            if (isListVisible)
                return leftMargin + iconWidth + rigthMargin;
            else
                return bigTrayIconHeight;
        }

    }

    protected boolean handleUiEvent(TnUiEvent tnUiEvent)
    {
        Logger.log(Logger.INFO, this.getClass().getName(), "handleUiEvent()");

        switch (tnUiEvent.getType())
        {
            case TnUiEvent.TYPE_TOUCH_EVENT:
            {
                TnMotionEvent motionEvent = tnUiEvent.getMotionEvent();

                int action = motionEvent.getAction();
                int x = motionEvent.getX();
                int y = motionEvent.getY();
                switch (action)
                {
                    case TnMotionEvent.ACTION_DOWN:
                        handleDownEvent(x, y);
                        break;
                    case TnMotionEvent.ACTION_MOVE:
                        break;
                    case TnMotionEvent.ACTION_UP:
                        break;
                }
                return true;
            }
        }
        return false;
    }

    public void setLayerSetting(int layerSetting)
    {
        isShowTrafficOverlay = ((layerSetting & 0x01) != 0); 
        isShowSatelliteOverlay = ((layerSetting & 0x02) != 0);  
        isShowTrafficCameraOverlay = ((layerSetting & 0x04) != 0);  
        isShowLandMarkOverlay = ((layerSetting & 0x08) != 0);
        
        requestPaint();
    }
    
    public int getLayerSetting()
    {
        int layerSettings = createLayerSettings(isShowTrafficOverlay, isShowSatelliteOverlay, isShowTrafficCameraOverlay, isShowLandMarkOverlay);
        return layerSettings;
    }
    
    public void handleDownEvent(int x, int y)
    {
        boolean isOnboard = !NetworkStatusManager.getInstance().isConnected();
        if (isListVisible)
        {
            if (isInsideShowTrafficIcon(x, y))
            {
                if(isOnboard)
                {
                    return;
                }
                Logger.log(Logger.INFO, this.getClass().getName(), "isInsideShowTrafficIcon");
                
                int currentSetting = createLayerSettings(!isShowTrafficOverlay, isShowSatelliteOverlay, isShowTrafficCameraOverlay, isShowLandMarkOverlay);
                
                TnUiEvent tnUiEvent = new TnUiEvent(TnUiEvent.TYPE_COMMAND_EVENT, this);
                tnUiEvent.setCommandEvent(new TnCommandEvent(IMapConstants.CMD_SHOW_MAP_TRAFFIC_LAYER_START  + currentSetting));
                this.commandListener.handleUiEvent(tnUiEvent);
            }
            else if (isInsideShowTrafficCameraIcon(x, y))
            {
                if(isOnboard)
                {
                    return;
                }
                Logger.log(Logger.INFO, this.getClass().getName(), "isInsideShowTrafficCameraIcon");
                
                int currentSetting = createLayerSettings(isShowTrafficOverlay, isShowSatelliteOverlay, !isShowTrafficCameraOverlay, isShowLandMarkOverlay);
                TnUiEvent tnUiEvent = new TnUiEvent(TnUiEvent.TYPE_COMMAND_EVENT, this);
                tnUiEvent.setCommandEvent(new TnCommandEvent(IMapConstants.CMD_SHOW_MAP_CAMERA_LAYER_START  + currentSetting));
                this.commandListener.handleUiEvent(tnUiEvent);
            }
            
            //FIXBUG: Add some delay for click satellite button because if we click many times in a short time , map engine will crash
            else if (isInsideShowSatelliteIcon(x, y) && (System.currentTimeMillis() - lastCurrentPressSatelliteTime) > 1000)
            {
                if(isOnboard)
                {
                    return;
                }
                Logger.log(Logger.INFO, this.getClass().getName(), "isInsideShowSatelliteIcon");
                
                int currentSetting = createLayerSettings(isShowTrafficOverlay, !isShowSatelliteOverlay, isShowTrafficCameraOverlay, isShowLandMarkOverlay);
                lastCurrentPressSatelliteTime = System.currentTimeMillis();
                
                TnUiEvent tnUiEvent = new TnUiEvent(TnUiEvent.TYPE_COMMAND_EVENT, this);
                tnUiEvent.setCommandEvent(new TnCommandEvent(IMapConstants.CMD_SHOW_MAP_SATELLITE_LAYER_START  + currentSetting));
                this.commandListener.handleUiEvent(tnUiEvent);
                
            }
            else if (isInsideTray(x, y))
            {
                isListVisible = false;
                this.getTnUiArgs().put(TnUiArgs.KEY_POSITION_X, new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
                {
                    public Object decorate(TnUiArgAdapter args)
                    {
                        return PrimitiveTypeCache.valueOf(oldTrayLeft);
                    }
                }));
                this.getTnUiArgs().put(TnUiArgs.KEY_POSITION_Y, new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
                {
                    public Object decorate(TnUiArgAdapter args)
                    {
                        return PrimitiveTypeCache.valueOf(oldTrayTop);
                    }
                }));
                this.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH,
                    new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
                    {
                        public Object decorate(TnUiArgAdapter args)
                        {
                            return PrimitiveTypeCache.valueOf(oldTrayWidth);
                        }
                    }));
                this.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT,
                    new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
                    {
                        public Object decorate(TnUiArgAdapter args)
                        {
                            return PrimitiveTypeCache.valueOf(oldTrayHeight);
                        }
                    }));
                this.requestLayout();
            }

        }
        else
        {
            isListVisible = true;

            oldTrayLeft = getTnUiArgs().get(TnUiArgs.KEY_POSITION_X).getInt();
            oldTrayTop = getTnUiArgs().get(TnUiArgs.KEY_POSITION_Y).getInt();
            oldTrayWidth = getTnUiArgs().get(TnUiArgs.KEY_PREFER_WIDTH).getInt();
            oldTrayHeight = getTnUiArgs().get(TnUiArgs.KEY_PREFER_HEIGHT).getInt();

            this.getTnUiArgs().put(TnUiArgs.KEY_POSITION_X,
                new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
                {
                    public Object decorate(TnUiArgAdapter args)
                    {
                        if (isPortraitMode())
                            return PrimitiveTypeCache.valueOf(oldTrayLeft + oldTrayWidth / 2 - getWidth() / 2);
                        else
                            return PrimitiveTypeCache.valueOf(oldTrayLeft + oldTrayHeight / 2 - smallTrayIconHeight / 2);
                    }
                }));
            this.getTnUiArgs().put(TnUiArgs.KEY_POSITION_Y,
                new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
                {
                    public Object decorate(TnUiArgAdapter args)
                    {
                        if (isPortraitMode())
                            return PrimitiveTypeCache.valueOf(oldTrayTop + oldTrayHeight / 2 + smallTrayIconHeight / 2
                                    - getHeight() + iconOffset);
                        else
                            return PrimitiveTypeCache.valueOf(oldTrayTop + oldTrayHeight / 2 - getHeight() / 2);
                    }
                }));
            this.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH,
                new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
                {
                    public Object decorate(TnUiArgAdapter args)
                    {
                        return PrimitiveTypeCache.valueOf(getWidth());
                    }
                }));
            this.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT,
                new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
                {
                    public Object decorate(TnUiArgAdapter args)
                    {
                        return PrimitiveTypeCache.valueOf(getHeight());
                    }
                }));

            this.requestLayout();
        }
    }

    private int createLayerSettings(boolean isShowTrafficOverlay, boolean isShowSatelliteOverlay, boolean isShowTrafficCameraOverlay, boolean isShowLandMarkOverlay)
    {
        int layerSettings = 0;
        if(isShowTrafficOverlay) layerSettings = layerSettings | 0x01;
        if(isShowSatelliteOverlay) layerSettings = layerSettings | 0x02;
        if(isShowTrafficCameraOverlay) layerSettings = layerSettings | 0x04;
        if(isShowLandMarkOverlay) layerSettings = layerSettings | 0x08;
        return layerSettings;
    }
    
    public void initMapLayerSettings(int layerSetting)
    {
        isShowTrafficOverlay = ((layerSetting & 0x01) != 0); 
        isShowSatelliteOverlay = ((layerSetting & 0x02) != 0);  
        isShowTrafficCameraOverlay = ((layerSetting & 0x04) != 0);  
        isShowLandMarkOverlay = ((layerSetting & 0x08) != 0); 
    }
    
    public void handleUpEvent(int x, int y)
    {

    }

    public void handleMoveEvent(int x, int y)
    {

    }

    protected void initDefaultStyle()
    {
        // TODO Auto-generated method stub

    }

    protected void paintBackground(AbstractTnGraphics graphics)
    {
        if (this.isListVisible)
        {

        }
        else
        {
            super.paintBackground(graphics);
        }
    }

    protected void paint(AbstractTnGraphics g)
    {
        if (isPortraitMode())
        {
            paintPortraitMode(g);
        }
        else
        {
            paintLandscapeMode(g);
        }
      

    }

    private void paintPortraitMode(AbstractTnGraphics g)
    {
        boolean isOnboard = !NetworkStatusManager.getInstance().isConnected();
        if (this.isListVisible)
        {
            TnNinePatchImage innerBoxNinePatchImage = (TnNinePatchImage) NinePatchImageDecorator.instance
                    .decorate(NinePatchImageDecorator.MAP_DROPUP_BG_UNFOCUSED);
            innerBoxNinePatchImage.setWidth(getWidth());
            innerBoxNinePatchImage.setHeight(getHeight() - smallTrayIconHeight / 2);
            g.drawImage(innerBoxNinePatchImage, 0, 0, g.LEFT | g.TOP);

            TnNinePatchImage focusedMenuItemBg = (TnNinePatchImage) NinePatchImageDecorator.instance
                    .decorate(NinePatchImageDecorator.MAP_LAYER_ICON_BG_FOCUSED);
            focusedMenuItemBg.setWidth(iconWidth);
            focusedMenuItemBg.setHeight(iconHeight);
            
            TnNinePatchImage disabledMenuItemBg = (TnNinePatchImage) NinePatchImageDecorator.instance
                    .decorate(NinePatchImageDecorator.MAP_LAYER_ICON_DISABLED_BG_UNFOCUSED);
            disabledMenuItemBg.setWidth(iconWidth);
            disabledMenuItemBg.setHeight(iconHeight);

            TnNinePatchImage unfocusedMenuItemBg = (TnNinePatchImage) NinePatchImageDecorator.instance
                    .decorate(NinePatchImageDecorator.MAP_LAYER_ICON_BG_UNFOCUSED);
            unfocusedMenuItemBg.setWidth(iconWidth);
            unfocusedMenuItemBg.setHeight(iconHeight);
            
            TnNinePatchImage focusedIconBg = (TnNinePatchImage) NinePatchImageDecorator.instance
                    .decorate(NinePatchImageDecorator.MAP_SMALL_BUTTON_ICON_BG);
            focusedIconBg.setWidth(smallTrayIconWidth);
            focusedIconBg.setHeight(smallTrayIconWidth);

            int iconCenterX = leftMargin + iconWidth / 2;
            if(hasTrafficOverlay)
            {
                if (isOnboard)
                {
                    g.drawImage(disabledMenuItemBg, leftMargin, topMargin, g.LEFT | g.TOP);
                    g.drawImage(ImageDecorator.MAP_TRAFFIC_ICON_DISABLED.getImage(), iconCenterX, topMargin + iconHeight / 2,
                        AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);
                }
                else if (isShowTrafficOverlay)
                {
                    g.drawImage(focusedMenuItemBg, leftMargin, topMargin, g.LEFT | g.TOP);
                    g.drawImage(ImageDecorator.MAP_TRAFFIC_ICON_FOCUSED.getImage(), iconCenterX, topMargin + iconHeight / 2,
                        AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);
                }
                else
                {
                    g.drawImage(unfocusedMenuItemBg, leftMargin, topMargin, g.LEFT | g.TOP);
                    g.drawImage(ImageDecorator.MAP_TRAFFIC_ICON_UNFOCUSED.getImage(), iconCenterX, topMargin + iconHeight / 2,
                        AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);
                }
            }
            
            if(hasTrafficCameraOverlay)
            {
                if (isOnboard)
                {
                    g.drawImage(disabledMenuItemBg, leftMargin, topMargin + indexTrafficCameraOverlay*(iconHeight + gap), g.LEFT | g.TOP);
                    g.drawImage(ImageDecorator.MAP_CAMERA_ICON_DISABLED.getImage(), iconCenterX, topMargin + indexTrafficCameraOverlay*(iconHeight + gap) + iconHeight / 2,
                        AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);
                }
                else if (isShowTrafficCameraOverlay)
                {
                    g.drawImage(focusedMenuItemBg, leftMargin, topMargin + indexTrafficCameraOverlay*(iconHeight + gap), g.LEFT | g.TOP);
                    g.drawImage(ImageDecorator.MAP_CAMERA_ICON_FOCUSED.getImage(), iconCenterX, topMargin + indexTrafficCameraOverlay*(iconHeight + gap) + iconHeight / 2,
                        AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);
                }
                else
                {
                    g.drawImage(unfocusedMenuItemBg, leftMargin, topMargin + indexTrafficCameraOverlay*(iconHeight + gap), g.LEFT | g.TOP);
                    g.drawImage(ImageDecorator.MAP_CAMERA_ICON_UNFOCUSED.getImage(), iconCenterX,
                        topMargin + indexTrafficCameraOverlay*(iconHeight + gap) + iconHeight / 2, AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);
                }
            }
            
            if(hasSatelliteOverlay)
            {
                if (isOnboard)
                {
                    g.drawImage(disabledMenuItemBg, leftMargin, topMargin + indexSatelliteOverlay * (iconHeight + gap), g.LEFT | g.TOP);
                    g.drawImage(ImageDecorator.MAP_SATELLITE_ICON_DISABLED.getImage(), iconCenterX, topMargin + indexSatelliteOverlay * (iconHeight + gap)
                            + iconHeight / 2, AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);
                }
                else if (isShowSatelliteOverlay)
                {
                    g.drawImage(focusedMenuItemBg, leftMargin, topMargin + indexSatelliteOverlay * (iconHeight + gap), g.LEFT | g.TOP);
                    g.drawImage(ImageDecorator.MAP_SATELLITE_ICON_FOCUSED.getImage(), iconCenterX, topMargin + indexSatelliteOverlay * (iconHeight + gap)
                            + iconHeight / 2, AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);
                }
                else
                {
                    g.drawImage(unfocusedMenuItemBg, leftMargin, topMargin + indexSatelliteOverlay * (iconHeight + gap), g.LEFT | g.TOP);
                    g.drawImage(ImageDecorator.MAP_SATELLITE_ICON_UNFOCUSED.getImage(), iconCenterX, topMargin + indexSatelliteOverlay * (iconHeight + gap)
                            + iconHeight / 2, AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);
                }
            }
            
            int heightCenter = topMargin + iconHeight * totalOverLayers + gap * (totalOverLayers -1)+ bottomMargin - iconOffset;

            g.drawImage(focusedIconBg, this.getWidth() / 2, heightCenter, AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);
            if (isShowTrafficOverlay || isShowSatelliteOverlay || isShowTrafficCameraOverlay)
            {
                g.drawImage(ImageDecorator.MAP_SMALL_MAP_LAYER_ICON_FOCUSED.getImage(), this.getWidth() / 2, heightCenter,
                    AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);
            }
            else
            {
                g.drawImage(ImageDecorator.MAP_SMALL_MAP_LAYER_ICON_UNFOCUSED.getImage(), this.getWidth() / 2, heightCenter,
                    AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);
            }
        }
        else
        {
            if (this.isFocused)
            {
                TnNinePatchImage focusedMenuItemBg = (TnNinePatchImage) NinePatchImageDecorator.instance
                        .decorate(NinePatchImageDecorator.MAP_DROPUP_BG_UNFOCUSED);
                int bgWidth = this.getTnUiArgs().get(TnUiArgs.KEY_PREFER_WIDTH).getInt();
                int bgHeight = this.getTnUiArgs().get(TnUiArgs.KEY_PREFER_HEIGHT).getInt();
                focusedMenuItemBg.setWidth(bgWidth);
                focusedMenuItemBg.setHeight(bgHeight);
                g.drawImage(focusedMenuItemBg, this.getWidth() / 2, this.getHeight() - bigTrayIconHeight / 2,
                    AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);
                g.drawImage(ImageDecorator.IMG_MAP_LAYER_ICON_FOCUSED.getImage(), this.getWidth() / 2, this.getHeight()
                        - bigTrayIconHeight / 2, AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);

            }
            else
            {
                if (isDayColor)
                {
                    TnNinePatchImage unfocusedMenuItemBg = (TnNinePatchImage) NinePatchImageDecorator.instance
                            .decorate(NinePatchImageDecorator.MAP_BUTTON_UNFOCUSED);
                    int bgWidth = this.getTnUiArgs().get(TnUiArgs.KEY_PREFER_WIDTH).getInt();
                    int bgHeight = this.getTnUiArgs().get(TnUiArgs.KEY_PREFER_HEIGHT).getInt();
                    unfocusedMenuItemBg.setWidth(bgWidth);
                    unfocusedMenuItemBg.setHeight(bgHeight);

                    g.drawImage(unfocusedMenuItemBg, this.getWidth() / 2, this.getHeight() - bigTrayIconHeight / 2,
                        AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);
                    g.drawImage(ImageDecorator.IMG_MAP_LAYER_ICON_UNFOCUSED.getImage(), this.getWidth() / 2, this.getHeight()
                            - bigTrayIconHeight / 2, AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);
                }
                else
                {
                    TnNinePatchImage unfocusedMenuItemBg = (TnNinePatchImage) NinePatchImageDecorator.instance
                            .decorate(NinePatchImageDecorator.MAP_BUTTON_NIGHT_UNFOCUSED);
                    int bgWidth = this.getTnUiArgs().get(TnUiArgs.KEY_PREFER_WIDTH).getInt();
                    int bgHeight = this.getTnUiArgs().get(TnUiArgs.KEY_PREFER_HEIGHT).getInt();
                    unfocusedMenuItemBg.setWidth(bgWidth);
                    unfocusedMenuItemBg.setHeight(bgHeight);
                    g.drawImage(unfocusedMenuItemBg, this.getWidth() / 2, this.getHeight() - bigTrayIconHeight / 2,
                        AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);
                    g.drawImage(ImageDecorator.IMG_MAP_LAYER_ICON_NIGHT_UNFOCUSED.getImage(), this.getWidth() / 2,
                        this.getHeight() - bigTrayIconHeight / 2, AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);
                }
            }
        }
    }
    
    private void paintLandscapeMode(AbstractTnGraphics g)
    {
        boolean isOnboard = !NetworkStatusManager.getInstance().isConnected();
        if (this.isListVisible)
        {
            TnNinePatchImage innerBoxNinePatchImage = (TnNinePatchImage) NinePatchImageDecorator.instance
                    .decorate(NinePatchImageDecorator.MAP_DROPUP_BG_UNFOCUSED);
            innerBoxNinePatchImage.setWidth(getWidth() - smallTrayIconHeight / 2);
            innerBoxNinePatchImage.setHeight(getHeight());
            g.drawImage(innerBoxNinePatchImage, smallTrayIconHeight / 2 - iconOffset, 0, g.LEFT | g.TOP);

            TnNinePatchImage focusedMenuItemBg = (TnNinePatchImage) NinePatchImageDecorator.instance
                    .decorate(NinePatchImageDecorator.MAP_LAYER_ICON_BG_FOCUSED);
            focusedMenuItemBg.setWidth(iconWidth);
            focusedMenuItemBg.setHeight(iconHeight);

            TnNinePatchImage unfocusedMenuItemBg = (TnNinePatchImage) NinePatchImageDecorator.instance
                    .decorate(NinePatchImageDecorator.MAP_LAYER_ICON_BG_UNFOCUSED);
            unfocusedMenuItemBg.setWidth(iconWidth);
            unfocusedMenuItemBg.setHeight(iconHeight);
            
            TnNinePatchImage disabledMenuItemBg = (TnNinePatchImage) NinePatchImageDecorator.instance
                    .decorate(NinePatchImageDecorator.MAP_LAYER_ICON_DISABLED_BG_UNFOCUSED);
            disabledMenuItemBg.setWidth(iconWidth);
            disabledMenuItemBg.setHeight(iconHeight);

            TnNinePatchImage focusedIconBg = (TnNinePatchImage) NinePatchImageDecorator.instance
                    .decorate(NinePatchImageDecorator.MAP_SMALL_BUTTON_ICON_BG);
            focusedIconBg.setWidth(smallTrayIconWidth);
            focusedIconBg.setHeight(smallTrayIconWidth);

            int iconCenterX = leftMargin + iconWidth / 2;

            if(hasTrafficOverlay)
            {
                if (isOnboard)
                {
                    g.drawImage(disabledMenuItemBg, smallTrayIconWidth /2 + bottomMargin + (totalOverLayers - 1)*(iconHeight + gap) - iconOffset, leftMargin,  g.LEFT | g.TOP);
                    g.drawImage(ImageDecorator.MAP_TRAFFIC_ICON_DISABLED.getImage(), smallTrayIconWidth /2 + bottomMargin + (totalOverLayers - 1) * (iconHeight + gap)+ iconHeight / 2 - iconOffset,iconCenterX,
                        AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);
                }
                else if (isShowTrafficOverlay)
                {
                    g.drawImage(focusedMenuItemBg, smallTrayIconWidth /2 + bottomMargin + (totalOverLayers - 1)*(iconHeight + gap) - iconOffset, leftMargin,  g.LEFT | g.TOP);
                    g.drawImage(ImageDecorator.MAP_TRAFFIC_ICON_FOCUSED.getImage(), smallTrayIconWidth /2 + bottomMargin + (totalOverLayers - 1) * (iconHeight + gap)+ iconHeight / 2 - iconOffset,iconCenterX,
                        AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);
                }
                else
                {
                    g.drawImage(unfocusedMenuItemBg, smallTrayIconWidth /2 + bottomMargin + (totalOverLayers - 1)*(iconHeight + gap) - iconOffset, leftMargin,  g.LEFT | g.TOP);
                    g.drawImage(ImageDecorator.MAP_TRAFFIC_ICON_UNFOCUSED.getImage(), smallTrayIconWidth /2 + bottomMargin + (totalOverLayers - 1) * (iconHeight + gap)+ iconHeight / 2 - iconOffset,iconCenterX, 
                        AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);
                }
            }

            if(hasTrafficCameraOverlay)
            {
                if (isOnboard)
                {
                    g.drawImage(disabledMenuItemBg, smallTrayIconWidth /2 + bottomMargin + (totalOverLayers-1-indexTrafficCameraOverlay)*(iconHeight + gap) - iconOffset, leftMargin,  g.LEFT | g.TOP);
                    g.drawImage(ImageDecorator.MAP_CAMERA_ICON_DISABLED.getImage(), smallTrayIconWidth /2 + bottomMargin + (totalOverLayers-1-indexTrafficCameraOverlay)*(iconHeight + gap) + iconHeight / 2 - iconOffset,iconCenterX, 
                        AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);
                }
                else if (isShowTrafficCameraOverlay)
                {
                    g.drawImage(focusedMenuItemBg, smallTrayIconWidth /2 + bottomMargin + (totalOverLayers-1-indexTrafficCameraOverlay)*(iconHeight + gap) - iconOffset, leftMargin,  g.LEFT | g.TOP);
                    g.drawImage(ImageDecorator.MAP_CAMERA_ICON_FOCUSED.getImage(), smallTrayIconWidth /2 + bottomMargin + (totalOverLayers-1-indexTrafficCameraOverlay)*(iconHeight + gap) + iconHeight / 2 - iconOffset,iconCenterX, 
                        AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);
                }
                else
                {
                    g.drawImage(unfocusedMenuItemBg, smallTrayIconWidth /2 + bottomMargin + (totalOverLayers-1-indexTrafficCameraOverlay)*(iconHeight + gap) - iconOffset, leftMargin,  g.LEFT | g.TOP);
                    g.drawImage(ImageDecorator.MAP_CAMERA_ICON_UNFOCUSED.getImage(), 
                        smallTrayIconWidth /2 + bottomMargin + (totalOverLayers-1-indexTrafficCameraOverlay)*(iconHeight + gap) + iconHeight / 2 - iconOffset, iconCenterX, AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);
                }
            }

            if(hasSatelliteOverlay)
            {
                if (isOnboard)
                {
                    g.drawImage(disabledMenuItemBg,  smallTrayIconWidth /2 + bottomMargin + 0 * (iconHeight + gap) - iconOffset, leftMargin, g.LEFT | g.TOP);
                    g.drawImage(ImageDecorator.MAP_SATELLITE_ICON_DISABLED.getImage(), smallTrayIconWidth /2 + bottomMargin + 0 * (iconHeight + gap)
                        + iconHeight / 2 - iconOffset, iconCenterX, AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);
                }
                else if (isShowSatelliteOverlay)
                {
                    g.drawImage(focusedMenuItemBg,  smallTrayIconWidth /2 + bottomMargin + 0 * (iconHeight + gap) - iconOffset, leftMargin, g.LEFT | g.TOP);
                    g.drawImage(ImageDecorator.MAP_SATELLITE_ICON_FOCUSED.getImage(), smallTrayIconWidth /2 + bottomMargin + 0 * (iconHeight + gap)
                        + iconHeight / 2 - iconOffset, iconCenterX, AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);
                }
                else
                {
                    g.drawImage(unfocusedMenuItemBg, smallTrayIconWidth /2 + bottomMargin + 0 * (iconHeight + gap) - iconOffset,leftMargin,  g.LEFT | g.TOP);
                    g.drawImage(ImageDecorator.MAP_SATELLITE_ICON_UNFOCUSED.getImage(), smallTrayIconWidth /2 + bottomMargin + 0 * (iconHeight + gap)
                        + iconHeight / 2 - iconOffset, iconCenterX, AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);
                }
            }

            int heightCenter = smallTrayIconWidth/2;

            g.drawImage(focusedIconBg, heightCenter, this.getHeight() / 2,  AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);
            if (isShowTrafficOverlay || isShowSatelliteOverlay || isShowTrafficCameraOverlay)
            {
                g.drawImage(ImageDecorator.MAP_SMALL_MAP_LAYER_ICON_FOCUSED.getImage(), heightCenter,this.getHeight() / 2,
                    AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);
            }
            else
            {
                g.drawImage(ImageDecorator.MAP_SMALL_MAP_LAYER_ICON_UNFOCUSED.getImage(), heightCenter,this.getHeight() / 2,
                    AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);
            }
        }
        else
        {
            if (this.isFocused)
            {
                TnNinePatchImage unfocusedMenuItemBg = (TnNinePatchImage) NinePatchImageDecorator.instance
                        .decorate(NinePatchImageDecorator.MAP_DROPUP_BG_UNFOCUSED);
                int bgWidth = this.getTnUiArgs().get(TnUiArgs.KEY_PREFER_WIDTH).getInt();
                int bgHeight = this.getTnUiArgs().get(TnUiArgs.KEY_PREFER_HEIGHT).getInt();
                unfocusedMenuItemBg.setWidth(bgWidth);
                unfocusedMenuItemBg.setHeight(bgHeight);
                g.drawImage(unfocusedMenuItemBg, bigTrayIconWidth / 2, this.getHeight() / 2, AbstractTnGraphics.HCENTER
                        | AbstractTnGraphics.VCENTER);
                g.drawImage(ImageDecorator.IMG_MAP_LAYER_ICON_FOCUSED.getImage(), bigTrayIconWidth / 2, this.getHeight() / 2,
                    AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);
            }
            else
            {
                if (isDayColor)
                {
                    TnNinePatchImage unfocusedMenuItemBg = (TnNinePatchImage) NinePatchImageDecorator.instance
                            .decorate(NinePatchImageDecorator.MAP_BUTTON_UNFOCUSED);
                    int bgWidth = this.getTnUiArgs().get(TnUiArgs.KEY_PREFER_WIDTH).getInt();
                    int bgHeight = this.getTnUiArgs().get(TnUiArgs.KEY_PREFER_HEIGHT).getInt();
                    unfocusedMenuItemBg.setWidth(bgWidth);
                    unfocusedMenuItemBg.setHeight(bgHeight);
                    g.drawImage(unfocusedMenuItemBg, bigTrayIconWidth / 2, this.getHeight() / 2, AbstractTnGraphics.HCENTER
                            | AbstractTnGraphics.VCENTER);
                    g.drawImage(ImageDecorator.IMG_MAP_LAYER_ICON_UNFOCUSED.getImage(), bigTrayIconWidth / 2,
                        this.getHeight() / 2, AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);
                }
                else
                {
                    TnNinePatchImage unfocusedMenuItemBg = (TnNinePatchImage) NinePatchImageDecorator.instance
                            .decorate(NinePatchImageDecorator.MAP_BUTTON_NIGHT_UNFOCUSED);
                    int bgWidth = this.getTnUiArgs().get(TnUiArgs.KEY_PREFER_WIDTH).getInt();
                    int bgHeight = this.getTnUiArgs().get(TnUiArgs.KEY_PREFER_HEIGHT).getInt();
                    unfocusedMenuItemBg.setWidth(bgWidth);
                    unfocusedMenuItemBg.setHeight(bgHeight);
                    g.drawImage(unfocusedMenuItemBg, bigTrayIconWidth / 2, this.getHeight() / 2, AbstractTnGraphics.HCENTER
                            | AbstractTnGraphics.VCENTER);
                    g.drawImage(ImageDecorator.IMG_MAP_LAYER_ICON_NIGHT_UNFOCUSED.getImage(), bigTrayIconWidth / 2,
                        this.getHeight() / 2, AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);
                }
            }
        }
    }
    
    public boolean isInsideBoundingBox(int x0, int y0, int x1, int y1, int x, int y)
    {
        return x >= x0 && x < x1 && y >= y0 && y < y1;
    }

    public boolean isListvisible()
    {
        return isListVisible;
    }

    public void hideList()
    {
        isListVisible = false;
        this.getTnUiArgs().put(TnUiArgs.KEY_POSITION_X, new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
        {
            public Object decorate(TnUiArgAdapter args)
            {
                return PrimitiveTypeCache.valueOf(oldTrayLeft);
            }
        }));
        this.getTnUiArgs().put(TnUiArgs.KEY_POSITION_Y, new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
        {
            public Object decorate(TnUiArgAdapter args)
            {
                return PrimitiveTypeCache.valueOf(oldTrayTop);
            }
        }));
        this.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
        {
            public Object decorate(TnUiArgAdapter args)
            {
                return PrimitiveTypeCache.valueOf(oldTrayWidth);
            }
        }));
        this.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
        {
            public Object decorate(TnUiArgAdapter args)
            {
                return PrimitiveTypeCache.valueOf(oldTrayHeight);
            }
        }));
        this.requestLayout();
    }
    
    public boolean isPortraitMode()
    {
        return ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT;
    }
}
