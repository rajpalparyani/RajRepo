/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * AbstractCommonMapView.java
 *
 */
package com.telenav.mvc;

import java.util.Timer;
import java.util.TimerTask;

import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.dao.misc.SimpleConfigDao;
import com.telenav.data.serverproxy.NetworkStatusManager;
import com.telenav.log.mis.IMisLogConstants;
import com.telenav.log.mis.MisLogManager;
import com.telenav.log.mis.log.OnboardMapDisplayMisLog;
import com.telenav.logger.Logger;
import com.telenav.map.IMapEngine;
import com.telenav.module.AppConfigHelper;
import com.telenav.module.mapdownload.MapDownloadStatusManager;
import com.telenav.navsdk.events.MapViewData.SelectedTrafficIncident;
import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.core.TnUiArgs;
import com.telenav.tnui.core.TnUiArgs.ITnUiArgsDecorator;
import com.telenav.tnui.core.TnUiArgs.TnUiArgAdapter;
import com.telenav.tnui.core.TnUiEvent;
import com.telenav.tnui.graphics.AbstractTnFont;
import com.telenav.tnui.graphics.AbstractTnGraphics;
import com.telenav.tnui.graphics.AbstractTnImage;
import com.telenav.ui.ImageDecorator;
import com.telenav.ui.UiFactory;
import com.telenav.ui.UiStyleManager;
import com.telenav.ui.citizen.map.DayNightService;
import com.telenav.ui.citizen.map.IMapContainerConstants;
import com.telenav.ui.citizen.map.IMapUIEventListener;
import com.telenav.ui.citizen.map.MapContainer;
import com.telenav.ui.frogui.widget.FrogLabel;
import com.telenav.util.PrimitiveTypeCache;

/**
 *@author zhdong@telenav.cn
 *@date 2011-2-17
 */
public abstract class AbstractCommonMapView extends AbstractCommonView implements IMapUIEventListener
{
    
    protected static final long SYNC_ZOOM_LEVEL_DELAY = 500;
    protected Timer syncZoomLevelTimer;
    public AbstractCommonMapView(AbstractCommonUiDecorator uiDecorator)
    {
        super(uiDecorator);
        MapContainer.getInstance().setMapUIEventListener(this);
        
        if (!NetworkStatusManager.getInstance().isConnected()
                && MapDownloadStatusManager.getInstance().isOnBoardDataAvailable())
        {
            OnboardMapDisplayMisLog log = MisLogManager.getInstance().getFactory().createOnboardMapDisplayMisLog();
            log.setTimestamp(System.currentTimeMillis());
            Logger.log(Logger.INFO, this.getClass().getName(), IMisLogConstants.PROCESS_MISLOG, new Object[]
                    { log });
        }

    }
    
    protected void popAllViews()
    {
        MapContainer.getInstance().cleanAll(false);
        MapContainer.getInstance().removeMapUIEventListener();
        super.popAllViews();
    }  
    
    public void tapNoAnnotation()
    {

    }
    
    protected void activate()
    {
        MapContainer.getInstance().setMapUIEventListener(this);
        super.activate();
    }

    protected void deactivateDelegate()
    {
        MapContainer.getInstance().removeMapUIEventListener();
        super.deactivateDelegate();
    }
    

    public void handleTouchEventOnMap(MapContainer container, TnUiEvent uiEvent)
    {

    }
    
    public void mapViewSizeChanged()
    {
        
    }
    
    public void handleTrafficIndcident(IMapEngine.AnnotationSearchResult.TrafficPickable pickable)
    {
        
    }
    
    public void handleMapRgc(double latitude, double longitude)
    {
        
    }
    
    public void onPinchEvent()
    {
        
    }
    
    public void onPinchEnd()
    {
        MapContainer.getInstance().updateZoomLevel();
    }
    
    public void handleZoomOut()
    {
        
    }
    
    protected void initMapLayer()
    {
        boolean isConnected = NetworkStatusManager.getInstance().isConnected();
        
        int layerSetting = ((DaoManager)DaoManager.getInstance()).getSimpleConfigDao().get(SimpleConfigDao.KEY_MAP_LAYER_SETTING);
        
        if (!isConnected)
        {
            layerSetting = 0;
        }
        
        if(layerSetting < 0)
        {
            layerSetting = 0;
        }
        
        MapContainer.getInstance().showMapLayer(layerSetting);
        
        MapContainer.getInstance().changeToSpriteVehicleAnnotation();
        // update day/night/satellite configuration
        MapContainer.getInstance().updateMapColor();
        MapContainer.getInstance().updateMapColorMode();
    }
    
    public AbstractTnComponent createMapCompanyLogo()
    {
        return createMapCompanyLogo(uiDecorator.MAP_LOGO_Y_WITH_BOTTOM_BAR);
    }
    
    public AbstractTnComponent createMapCompanyLogo(final TnUiArgAdapter yAdapter)
    {
        AbstractTnImage logo = DayNightService.getInstance().getMapColor() == IMapContainerConstants.MAP_NIGHT_COLOR ? ImageDecorator.IMG_NIGHT_LOGO_ON_MAP.getImage() : ImageDecorator.IMG_DAY_LOGO_ON_MAP.getImage();
        AbstractTnComponent mapCompanyLogo = UiFactory.getInstance().createFrogImageComponent(ICommonConstants.ID_MAP_COMPANY_LOGO, logo);
        
        mapCompanyLogo.getTnUiArgs().put(TnUiArgs.KEY_POSITION_X, new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
        {
            public Object decorate(TnUiArgAdapter args)
            {
                int padding = AppConfigHelper.getMinDisplaySize() * 315 / 10000;;
                return PrimitiveTypeCache.valueOf(padding);
            }
        }));
        mapCompanyLogo.getTnUiArgs().put(TnUiArgs.KEY_POSITION_Y, yAdapter);
        return mapCompanyLogo;
    }
    
    public AbstractTnComponent createMapProvider()
    {
        return createMapProvider(uiDecorator.MAP_LOGO_Y_WITH_BOTTOM_BAR);
    }
    
    public AbstractTnComponent createMapProvider(final TnUiArgAdapter yAdapter)
    {
//		String providerName = DaoManager.getInstance().getStartupDao().getMapDataset().toUpperCase();
//        if(providerName == null || providerName.trim().length() == 0)
//        {
//            return null;
//        }
//        String mapProvider = "\u00A9 " + Calendar.getInstance().get(Calendar.YEAR) + " " + providerName;
        String mapCopyright = DaoManager.getInstance().getStartupDao().getMapCopyright();
        if(mapCopyright == null || mapCopyright.trim().length() == 0)
        {
            return null;
        }
        FrogLabel mapProviderLabel = UiFactory.getInstance().createLabel(ICommonConstants.ID_MAP_PROVIDER, mapCopyright);
        mapProviderLabel.setPadding(0, 0, 0, 0);
        AbstractTnFont  font = UiStyleManager.getInstance().getFont(UiStyleManager.FONT_MAP_PROVIDER);
        mapProviderLabel.setFont(font);
    
        mapProviderLabel.setStyle(AbstractTnGraphics.HCENTER | AbstractTnGraphics.TOP);
        
        int color =  DayNightService.getInstance().getMapColor() == IMapContainerConstants.MAP_NIGHT_COLOR ? UiStyleManager.getInstance().getColor(
            UiStyleManager.MAP_PROVIDER_TEXT_NIGHT_COLOR): UiStyleManager.getInstance().getColor(
                UiStyleManager.MAP_PROVIDER_TEXT_DAY_COLOR);
        mapProviderLabel.setForegroundColor(color, color);
        final int strLen = font.stringWidth(mapCopyright);
        final int strHeight = font.getHeight();
        
        mapProviderLabel.getTnUiArgs().put(TnUiArgs.KEY_POSITION_X, new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
        {
            public Object decorate(TnUiArgAdapter args)
            {         
                int padding = AppConfigHelper.getMinDisplaySize() * 315 / 10000;;
                return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth() - strLen - padding); 
            }
        }));
        mapProviderLabel.getTnUiArgs().put(TnUiArgs.KEY_POSITION_Y, new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
        {
            public Object decorate(TnUiArgAdapter args)
            {         
                int companyLogoY = yAdapter.getInt();
                return PrimitiveTypeCache.valueOf(companyLogoY + (ImageDecorator.IMG_DAY_LOGO_ON_MAP.getImage().getHeight()*4/5 - strHeight) ); 
            }
        }));
        mapProviderLabel.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
        {
            public Object decorate(TnUiArgAdapter args)
            {         
                return PrimitiveTypeCache.valueOf(strLen); 
            }
        }));
        mapProviderLabel.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
        {
            public Object decorate(TnUiArgAdapter args)
            {         
                return PrimitiveTypeCache.valueOf(strHeight); 
            }
        }));
        return mapProviderLabel;
    }
    
    public void onDoubleTap()
    {
        
    }
    
    protected void relocateMapLogoProvider(final TnUiArgAdapter yAdapter)
    {
        final AbstractTnComponent companyLogo = MapContainer.getInstance().getFeature(ICommonConstants.ID_MAP_COMPANY_LOGO);
        final AbstractTnComponent mapProvider = MapContainer.getInstance().getFeature(ICommonConstants.ID_MAP_PROVIDER);
        boolean needRequestLayout = false;
        
        if (companyLogo != null && companyLogo.getTnUiArgs() != null)
        {
            needRequestLayout = true;
            companyLogo.getTnUiArgs().put(TnUiArgs.KEY_POSITION_Y, yAdapter);
        }
        if (mapProvider != null && mapProvider.getTnUiArgs() != null)
        {
            needRequestLayout = true;
            TnUiArgAdapter providerYAdapter = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
            {
                public Object decorate(TnUiArgAdapter args)
                { 
                    int companyLogoY = yAdapter.getInt() ;
                    int companyLogoHeight = ImageDecorator.IMG_DAY_LOGO_ON_MAP.getImage().getHeight();
                    int strHeight = UiStyleManager.getInstance().getFont(UiStyleManager.FONT_MAP_PROVIDER).getHeight(); 
                    return PrimitiveTypeCache.valueOf(companyLogoY  + (companyLogoHeight - strHeight) / 2); 
                }
            });
            mapProvider.getTnUiArgs().put(TnUiArgs.KEY_POSITION_Y, providerYAdapter);
        }
        
        if (needRequestLayout)
        {
            Thread t = new Thread(new Runnable()
            {
                public void run()
                {
                    MapContainer.getInstance().reLayoutContentContainer();
                }
            });
            t.start();
        }
    }
    
    public void handleClickTrafficIncident(SelectedTrafficIncident incident)
    {
        // TODO Auto-generated method stub
        
    }

    public void eglSizeChanged()
    {
        
    }
    
    protected void syncZoomLevel()
    {
        syncZoomLevel(SYNC_ZOOM_LEVEL_DELAY);
    }
    
    protected void syncZoomLevel(long delay)
    {
        if (syncZoomLevelTimer != null)
        {
            syncZoomLevelTimer.cancel();
            syncZoomLevelTimer = null;
        }

        syncZoomLevelTimer = new Timer();

        SyncZoomLevelTask task = new SyncZoomLevelTask();

        syncZoomLevelTimer.schedule(task, delay);
    }
    
    protected void syncZoomLevel(long delay, TimerTask task)
    {
        if (syncZoomLevelTimer != null)
        {
            syncZoomLevelTimer.cancel();
            syncZoomLevelTimer = null;
        }

        syncZoomLevelTimer = new Timer();

        syncZoomLevelTimer.schedule(task, delay);
    }
    
    protected class SyncZoomLevelTask extends TimerTask
    {
        public void run()
        {
            MapContainer.getInstance().updateZoomLevel();
            MapContainer.getInstance().updateZoomBtnState(MapContainer.getInstance().getZoomLevel(), false);
        }
    }
}
