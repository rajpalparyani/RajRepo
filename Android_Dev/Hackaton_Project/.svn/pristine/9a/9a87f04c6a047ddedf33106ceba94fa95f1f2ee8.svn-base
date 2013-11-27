/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * StartUpTouchView.java
 *
 */
package com.telenav.module.sync;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.telenav.app.TeleNavDelegate;
import com.telenav.app.android.scout_us.R;
import com.telenav.data.serverproxy.impl.navsdk.NavSdkLocationProviderProxy;
import com.telenav.io.TnIoManager;
import com.telenav.location.TnLocation;
import com.telenav.map.MapConfig;
import com.telenav.module.AppConfigHelper;
import com.telenav.module.location.LocationProvider;
import com.telenav.mvc.AbstractCommonMapView;
import com.telenav.mvc.AbstractCommonUiDecorator;
import com.telenav.mvc.ICommonConstants;
import com.telenav.res.INinePatchImageRes;
import com.telenav.res.IStringCommon;
import com.telenav.res.IStringLogin;
import com.telenav.res.IStringSyncRes;
import com.telenav.res.ResourceManager;
import com.telenav.sdk.kontagent.KontagentLogger;
import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.core.AbstractTnUiHelper;
import com.telenav.tnui.core.TnMenu;
import com.telenav.tnui.core.TnMotionEvent;
import com.telenav.tnui.core.TnScreen;
import com.telenav.tnui.core.TnUiArgs;
import com.telenav.tnui.core.TnUiArgs.ITnUiArgsDecorator;
import com.telenav.tnui.core.TnUiArgs.TnUiArgAdapter;
import com.telenav.tnui.core.TnUiEvent;
import com.telenav.tnui.core.TnUiTimer;
import com.telenav.tnui.graphics.AbstractTnGraphics;
import com.telenav.tnui.graphics.AbstractTnGraphicsHelper;
import com.telenav.tnui.graphics.AbstractTnImage;
import com.telenav.tnui.graphics.TnColor;
import com.telenav.tnui.graphics.TnRect;
import com.telenav.tnui.widget.TnAbsoluteContainer;
import com.telenav.tnui.widget.TnLinearContainer;
import com.telenav.tnui.widget.TnPopupContainer;
import com.telenav.ui.NinePatchImageDecorator;
import com.telenav.ui.UiFactory;
import com.telenav.ui.UiStyleManager;
import com.telenav.ui.android.AssetsImageDrawable;
import com.telenav.ui.android.XmlDrawable;
import com.telenav.ui.citizen.CitizenProgressBar;
import com.telenav.ui.citizen.CitizenScreen;
import com.telenav.ui.citizen.android.AndroidCitizenUiHelper;
import com.telenav.ui.citizen.map.IMapContainerConstants;
import com.telenav.ui.citizen.map.MapContainer;
import com.telenav.ui.frogui.widget.FrogLabel;
import com.telenav.util.PrimitiveTypeCache;

/**
 *@author bduan
 *@date 2010-12-2
 */
class SyncResViewTouch extends AbstractCommonMapView implements ISyncResConstants
{
    private static final int ABSOLUTE_CONTAINER_ID = 1234;
    
    private View scoutGoView;
    
    public static final int BTN_TOUCH_UP_COLOR = 0xffffffff;
    public static final int BTN_TOUCH_DOWN_COLOR = 0xFF241F21;
    
    public SyncResViewTouch(AbstractCommonUiDecorator uiDecorator)
    {
        super(uiDecorator);
    }

    protected TnPopupContainer createPopup(int state)
    {
        return null;
    }

    protected TnScreen createScreen(int state)
    {
        TnScreen screen = null;
        switch(state)
        {
            case STATE_FRESH_SYNC:
            {
                screen = createSetupScreen(state);
                break;
            }
            case STATE_SCOUT_GO:
            {
                screen = createScoutGo(state);
                KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_FTUE, KontagentLogger.FTUE_CONFIRMATION_SCREEN_DISPLAYED);
            }
        }
        return screen;
    }
    
    protected TnScreen createScoutGo(int state)
    {
        TeleNavDelegate.getInstance().setOrientation(TeleNavDelegate.ORIENTATION_PORTRAIT);
        CitizenScreen screen = UiFactory.getInstance().createScreen(state);
        scoutGoView = AndroidCitizenUiHelper.addContentView(screen, R.layout.ftue_success);
        Drawable bgImage = new AssetsImageDrawable(NinePatchImageDecorator.FTUE_BG_UNFOCUSED);
        scoutGoView.setBackgroundDrawable(bgImage);
        
        TnMenu screenMenu = UiFactory.getInstance().createMenu();
        String exitStr = ResourceManager.getInstance().getCurrentBundle().getString(IStringCommon.RES_BTTN_EXIT,
            IStringCommon.FAMILY_COMMON);
        screenMenu.add(exitStr, ICommonConstants.CMD_COMMON_EXIT);
        
        screen.getRootContainer().setMenu(screenMenu, AbstractTnComponent.TYPE_MENU);
        
        LinearLayout successLayout=(LinearLayout)scoutGoView.findViewById(R.id.successlayout);
        
        
        //First, get a scale image,but when it is set to background, system will stretch it.
        //So we need to create a blank image which with is display width and height is display height
        //and this will avoid the stretch.
        AbstractTnImage scaleImage = getScaleImage();
        AbstractTnImage destImage = AbstractTnUiHelper.getInstance().createImage(AppConfigHelper.getDisplayWidth(),AppConfigHelper.getDisplayHeight());
        AbstractTnGraphics g = destImage.getGraphics();
        g.drawImage(scaleImage, AppConfigHelper.getDisplayWidth() / 2, AppConfigHelper.getDisplayHeight() / 2, AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);
        successLayout.setBackgroundDrawable(new AssetsImageDrawable(destImage));
        scaleImage.release();
        
        TextView thankyouTextview=(TextView)scoutGoView.findViewById(R.id.thankyou);
        String thankyouStr =ResourceManager.getInstance().getCurrentBundle().getString(IStringLogin.RES_LABEL_THANKYOU, IStringLogin.FAMILY_LOGIN);    
        thankyouTextview.setText(thankyouStr);
        
        
        TextView descriptionTextview=(TextView)scoutGoView.findViewById(R.id.description);
        String firstline =ResourceManager.getInstance().getCurrentBundle().getString(IStringLogin.RES_LABEL_SUCCESS_FIRST_CAUTION, IStringLogin.FAMILY_LOGIN);   
        String secondLine =ResourceManager.getInstance().getCurrentBundle().getString(IStringLogin.RES_LABEL_SUCCESS_SECOND_CAUTION, IStringLogin.FAMILY_LOGIN);    
        String caution=firstline+"\n"+secondLine;
        descriptionTextview.setText(caution);
        
        
        Button doneBtn=(Button)scoutGoView.findViewById(R.id.go);
        String finishStr =ResourceManager.getInstance().getCurrentBundle().getString(IStringLogin.RES_BTN_GO, IStringLogin.FAMILY_LOGIN);    
        doneBtn.setText(finishStr);
        doneBtn.setTextColor(getColorState(R.color.login_button_font));
        
        AndroidCitizenUiHelper.setOnClickCommand(this, doneBtn,  CMD_SCOUT_GO);
        return screen;
    }

    protected TnScreen createSetupScreen(int state)
    {
        CitizenScreen setupScreen = UiFactory.getInstance().createScreen(state);

        TnLinearContainer titleContainer = UiFactory.getInstance().createLinearContainer(0, true);
        titleContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, (this.uiDecorator).SCREEN_WIDTH);
        titleContainer.setBackgroundDrawable(new XmlDrawable(R.drawable.background_shelf));
        

        FrogLabel titleLabel = UiFactory.getInstance().createLabel(0,
            ResourceManager.getInstance().getCurrentBundle().getString(IStringSyncRes.RES_SETUP_UP_TITLE, IStringSyncRes.FAMILY_SYNC_RES));
        titleLabel.setForegroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_WH), UiStyleManager.getInstance()
                .getColor(UiStyleManager.TEXT_COLOR_WH));
        titleLabel.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, uiDecorator.SCREEN_WIDTH);
        titleLabel.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((SyncResUiDecorator) this.uiDecorator).TITLE_LABEL_HEIGHT);
        titleLabel.setStyle(AbstractTnGraphics.HCENTER);
        titleLabel.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_SCREEN_TITLE));
        titleContainer.add(titleLabel);
        
        if (isComponentNeeded(INinePatchImageRes.SPLITE_LINE  + INinePatchImageRes.ID_UNFOCUSED))
        {
            FrogLabel screenSplitLine = UiFactory.getInstance().createLabel(0, "");
            screenSplitLine.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.SCREEN_SPLITE_LINE_UNFOCUSED);
            screenSplitLine.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, NinePatchImageDecorator.SCREEN_SPLITE_LINE_UNFOCUSED);
            screenSplitLine.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, uiDecorator.SCREEN_SPLIT_LINE_HEIGHT);
            screenSplitLine.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, uiDecorator.SCREEN_SPLIT_LINE_WIDTH);
            titleContainer.add(screenSplitLine);
        }
        
        
        TnMenu screenMenu = UiFactory.getInstance().createMenu();
        String exitStr = ResourceManager.getInstance().getCurrentBundle().getString(IStringCommon.RES_BTTN_EXIT,
            IStringCommon.FAMILY_COMMON);
        screenMenu.add(exitStr, ICommonConstants.CMD_COMMON_EXIT);
        
        setupScreen.getRootContainer().setMenu(screenMenu, AbstractTnComponent.TYPE_MENU);
        
        MapContainer mapContainer = UiFactory.getInstance().getCleanMapContainer(setupScreen, ID_MAP_CONTAINER);

        mapContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
        {
            public Object decorate(TnUiArgAdapter args)
            {
                return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() - AppConfigHelper.getStatusBarHeight() /*- label.getPreferredHeight() - ((SyncResUiDecorator)uiDecorator).PROGRESS_BAR_HEIGHT.getInt()*/);
            }
        }));

        mapContainer.addFeature(titleContainer);
        initMapLayer();
        setMapLoc();
        addProgressContainer(setupScreen);
        return setupScreen;
    }
    
    private void setMapLoc()
    {
        TnLocation gpsLocation = LocationProvider.getInstance().getCurrentLocation(
            LocationProvider.TYPE_GPS | LocationProvider.TYPE_NETWORK, true);
        if (gpsLocation == null)
        {
            TnLocation defaultLocation = LocationProvider.getInstance().getDefaultLocation();

            double US_LAT = (double) defaultLocation.getLatitude() / 100000.0d;
            double US_LON = (double) defaultLocation.getLongitude() / 100000.0d;

            MapContainer.getInstance().setZoomLevel(MapConfig.MAP_MAX_ZOOM_LEVEL);
            MapContainer.getInstance().disableVehicleAnnotation();
            MapContainer.getInstance().moveMapTo(US_LAT, US_LON, 0, 0);
        }
        else
        {
            double dLat = (double) gpsLocation.getLatitude() / 100000.0d;
            double dLon = (double) gpsLocation.getLongitude() / 100000.0d;

            NavSdkLocationProviderProxy navSdkLocationProvider = new NavSdkLocationProviderProxy(null);
            navSdkLocationProvider.updateLocation(gpsLocation);
            
            MapContainer.getInstance().setZoomLevel(MapConfig.MAP_DEFAULT_ZOOM_LEVEL);
            MapContainer.getInstance().moveMapTo(dLat, dLon, 0, 0);
            MapContainer.getInstance().enableCar();
        }
    }

    protected void addProgressContainer(CitizenScreen setupScreen)
    {
        TnLinearContainer statusContainer = new CitizenTnLinearContainer(0, true, AbstractTnGraphics.HCENTER);
        statusContainer.setBackgroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.SYNCRES_PROGRESS_CONTAINER_BACKGROUND));
        
        
        final FrogLabel label = UiFactory.getInstance().createLabel(0, ResourceManager.getInstance().getCurrentBundle().getString(IStringSyncRes.RES_SETUP_FIRST_TIME, IStringSyncRes.FAMILY_SYNC_RES));
        label.setStyle(AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);
        label.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((SyncResUiDecorator)uiDecorator).PROGRESS_BAR_WIDTH);
        label.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((SyncResUiDecorator)uiDecorator).PROGRESS_BAR_HEIGHT);
        label.setForegroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_WH), UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_WH));
        label.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_LABEL));
        label.sublayout(0, 0);
        statusContainer.add(label);
        
        CitizenProgressBar progressBar = UiFactory.getInstance().createProgressBar(ID_PROGRESS_BAR);
        progressBar.setPadding(10, 2, 10, 12);
        progressBar.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((SyncResUiDecorator)uiDecorator).PROGRESS_BAR_HEIGHT);
        progressBar.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((SyncResUiDecorator)uiDecorator).PROGRESS_BAR_WIDTH);
        
        statusContainer.getTnUiArgs().put(TnUiArgs.KEY_POSITION_X, new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
        {
            public Object decorate(TnUiArgAdapter args)
            {
                return PrimitiveTypeCache.valueOf(0);
            }
        }));
        statusContainer.getTnUiArgs().put(TnUiArgs.KEY_POSITION_Y, ((SyncResUiDecorator)uiDecorator).BOTTOM_PROGRESS_CONTAINER_Y);
        
        
        TnUiTimer.getInstance().addReceiver(progressBar, 300);
        statusContainer.add(progressBar);
        
        TnLinearContainer contentContainer = (TnLinearContainer) setupScreen.getContentContainer();
        TnLinearContainer linearContainer = new TnLinearContainer(0, true);
        if(contentContainer.getChildrenSize() > 0)
        {
            int size = contentContainer.getChildrenSize();
            for(int i = 0 ; i < size ; i ++)
            {
                AbstractTnComponent component = contentContainer.get(0);
                contentContainer.remove(component);
                linearContainer.add(component);
            }
        }
        
        
        absoluteContainer = new TnAbsoluteContainer(ABSOLUTE_CONTAINER_ID);
        linearContainer.getTnUiArgs().put(TnUiArgs.KEY_POSITION_X, new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
        {
            public Object decorate(TnUiArgAdapter args)
            {
                return PrimitiveTypeCache.valueOf(0);
            }
        }));
        linearContainer.getTnUiArgs().put(TnUiArgs.KEY_POSITION_Y, new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
        {
            public Object decorate(TnUiArgAdapter args)
            {
                return PrimitiveTypeCache.valueOf(0);
            }
        }));
        absoluteContainer.add(linearContainer);
        absoluteContainer.add(statusContainer);
        MapContainer.getInstance().addFeature(createMapCompanyLogo(((SyncResUiDecorator) uiDecorator).MAP_LOGO_WITH_BOTTOM_CONTAINER_Y));
        MapContainer.getInstance().addFeature(createMapProvider(((SyncResUiDecorator) uiDecorator).MAP_LOGO_WITH_BOTTOM_CONTAINER_Y));
        MapContainer.getInstance().addFeature(absoluteContainer);
    }
    
    protected int transformCommandDelegate(int state, TnUiEvent tnUiEvent)
    {
        int cmd = CMD_NONE;
        
        //TODO:
        
        return cmd;
    }

    protected boolean updatePopup(int state, TnPopupContainer popup)
    {
        return false;
    }

    protected boolean updateScreen(int state, TnScreen screen)
    {
        boolean isNeedRepaint = false;
        switch(state)
        {
            case STATE_FRESH_SYNC:
            {
            	boolean syncOccurError = model.getBool(KEY_B_SYNCRES_OCCUR_ERROR);
            	if (syncOccurError)
            	{
	            	MapContainer mapContainer = MapContainer.getInstance();
	            	TnAbsoluteContainer absoluteContainer = (TnAbsoluteContainer)mapContainer.getFeature(ABSOLUTE_CONTAINER_ID);            	
	            	if(absoluteContainer != null)
	            	{            		
	            		CitizenProgressBar progressBar = (CitizenProgressBar)absoluteContainer.getComponentById(ID_PROGRESS_BAR);
	            		if(progressBar != null)
	            		{ 
	            			TnUiTimer.getInstance().removeReceiver(progressBar);
	            		}
	            	}
            	}

                isNeedRepaint = updateSetup(screen);
            }
        }
        return isNeedRepaint;
    }

    protected boolean updateSetup(TnScreen screen)
    {
        return true;
    }
    
    private static class CitizenTnLinearContainer extends TnLinearContainer
    {
        public CitizenTnLinearContainer(int id, boolean isVertical, int anchor)
        {
            super(id, isVertical, anchor);
        }
        
        protected void paintBackground(AbstractTnGraphics graphics)
        {
            int oldColor = graphics.getColor();

            if (this.backgroundDrawable != null)
            {
                if (this.backgroundDrawable.getBounds() == null)
                {
                    this.backgroundDrawable.setBounds(new TnRect(0, 0, this.getWidth(), this.getHeight()));
                }
                this.backgroundDrawable.draw(graphics);
            }
            else if (this.tnUiArgs != null)
            {
                TnUiArgAdapter uiArgAdapter = null;
                if (isFocused())
                    uiArgAdapter = this.tnUiArgs.get(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS);
                else
                    uiArgAdapter = this.tnUiArgs.get(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS);
                if (uiArgAdapter != null)
                {
                    AbstractTnImage bgImage = uiArgAdapter.getImage();
                    if(bgImage != null)
                    {
                        bgImage.setWidth(getWidth());
                        bgImage.setHeight(getHeight());
                        graphics.drawImage(bgImage, 0, 0, AbstractTnGraphics.LEFT | AbstractTnGraphics.TOP);
                    }
                }
                else if (this.backgroundColor != TnColor.TRANSPARENT)
                {
                    graphics.setColor(TnColor.alpha(backgroundColor), TnColor.red(backgroundColor), TnColor.green(backgroundColor), TnColor.blue(backgroundColor));
                    graphics.fillRect(0, 0, this.getWidth(), this.getHeight());
                    graphics.setColor(oldColor);
                }
            }
            else if (this.backgroundColor != TnColor.TRANSPARENT)
            {
                graphics.setColor(TnColor.alpha(backgroundColor), TnColor.red(backgroundColor), TnColor.green(backgroundColor), TnColor.blue(backgroundColor));
                graphics.fillRect(0, 0, this.getWidth(), this.getHeight());
                graphics.setColor(oldColor);
            }
        }
    }
    
    protected void popAllViews()
    {
        super.popAllViews();
    }

    protected void startFollowVehicleMode(MapContainer mapContainer)
    {
        mapContainer.setRenderingMode(IMapContainerConstants.RENDERING_MODE_2D_NORTH_UP);
    }
    
    public void handleTouchEventOnMap(MapContainer container, TnUiEvent uiEvent)
    {
        final TnMotionEvent motionEvent = uiEvent.getMotionEvent();
        int action = motionEvent.getAction();
        final float transitionTime = container.getTransitionTime();
        if(action == TnMotionEvent.ACTION_DOWN)
        {
            MapContainer.getInstance().setMapTransitionTime(0);
        }
        else if (action == TnMotionEvent.ACTION_MOVE)
        {
            if (IMapContainerConstants.INTERACTION_MODE_FOLLOW_VEHICLE == container.getInteractionMode())
            {
                container.interactionModeChanged(IMapContainerConstants.INTERACTION_MODE_PAN_AND_ZOOM);
            }
        }
        else if(action == TnMotionEvent.ACTION_UP)
        {
            MapContainer.getInstance().setMapTransitionTime(transitionTime);
        }
    }
    
    protected void updateZoomForCellLocation(final TnLocation location)
    {
//        Runnable runnable = new Runnable()
//        {
//            public void run()
//            {
//                IMapEngine mapEngine = MapEngineManager.getInstance().getMapEngine();
//                long viewId = MapContainer.getInstance().getViewId();
//                int lat = location.getLatitude();
//                int lon = location.getLongitude();
//                int meterAccuracy = location.getAccuracy();
//                double minLat = (lat - meterAccuracy) / 100000.0d;
//                double maxLat = (lat + meterAccuracy) / 100000.0d;
//                double minLon = (lon - meterAccuracy) / 100000.0d;
//                double maxLon = (lon + meterAccuracy) / 100000.0d;
//                
//                int x = 0;
//                int y = 0;
//                int width = uiDecorator.SCREEN_WIDTH.getInt();
//                int height = uiDecorator.SCREEN_HEIGHT.getInt() - AppConfigHelper.getStatusBarHeight() - uiDecorator.BOTTOM_BAR_HEIGHT.getInt();
//                mapEngine.showRegion(viewId, maxLat, minLon, minLat, maxLon, x, y, width, height);
//            }
//        };

//        MapContainer.getInstance().postRenderEvent(runnable);
    }
    
    protected void updateZoomForGpsLocation()
    {
        MapContainer.getInstance().setZoomLevel(MapConfig.MAP_DEFAULT_ZOOM_LEVEL);
    }

    @Override
    protected void activate()
    {
       TeleNavDelegate.getInstance().closeVirtualKeyBoard(null);
        super.activate();
    }

    protected AbstractTnImage getScaleImage()
    {
        byte[] imageData = TnIoManager.getInstance().openFileBytesFromAppBundle("i18n/generic/images/common/native_ftue_thankyou_unfocused.png");        
        
        AbstractTnImage graphicsImage = AbstractTnGraphicsHelper.getInstance().createImage(imageData);
        AbstractTnImage scaledImage = null;
        if(graphicsImage != null)
        {
            int graphicsWidth = graphicsImage.getWidth();
            int graphicsHeight = graphicsImage.getHeight();
            
            float compressedRatio = (float) graphicsHeight / graphicsWidth;

            if (graphicsImage.getWidth() > 0) // avoid graphicsImage.bitmap is null
            {
                int width = AppConfigHelper.getDisplayWidth();
                int height = (int) (width * compressedRatio);
                scaledImage = graphicsImage.createScaledImage(width, height);
            }
        }
        
        return scaledImage;
    }
}
