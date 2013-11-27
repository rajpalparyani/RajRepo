/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * SplashScreenInflater.java
 *
 */
package com.telenav.module.entry;

import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.ImageView;

import com.telenav.app.android.scout_us.R;
import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.dao.misc.SimpleConfigDao;
import com.telenav.io.TnIoManager;
import com.telenav.module.AppConfigHelper;
import com.telenav.mvc.ICommonConstants;
import com.telenav.res.ISpecialImageRes;
import com.telenav.res.IStringCommon;
import com.telenav.res.ResourceManager;
import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.core.AbstractTnContainer;
import com.telenav.tnui.core.AbstractTnUiHelper;
import com.telenav.tnui.core.TnMenu;
import com.telenav.tnui.core.TnScreen;
import com.telenav.tnui.core.TnUiArgs;
import com.telenav.tnui.core.TnUiArgs.ITnUiArgsDecorator;
import com.telenav.tnui.core.TnUiArgs.TnUiArgAdapter;
import com.telenav.tnui.graphics.AbstractTnFont;
import com.telenav.tnui.graphics.AbstractTnGraphicsHelper;
import com.telenav.tnui.graphics.AbstractTnImage;
import com.telenav.tnui.widget.TnAbsoluteContainer;
import com.telenav.ui.UiFactory;
import com.telenav.ui.android.AssetsImageDrawable;
import com.telenav.ui.android.RatioSolidImageView;
import com.telenav.ui.citizen.android.AndroidCitizenUiHelper;
import com.telenav.ui.citizen.map.MapContainer;
import com.telenav.util.PrimitiveTypeCache;

/**
 *@author bduan
 *@date Dec 1, 2012
 */
public class SplashScreenInflater
{
    private static class InnterSplashScreenInflater
    {
        private static SplashScreenInflater inflater = new SplashScreenInflater();
    }
    
    protected int width = 0;
    protected int height = 0;
    
    protected AbstractTnImage graphicsImage;
    protected AbstractTnImage scaledImage;
    protected View fakeMapView;
    protected View fakeMapViewLandscape;
    protected boolean hasMiniMapSize = false;
    
    private SplashScreenInflater()
    {
        width = ((AbstractTnUiHelper)AbstractTnUiHelper.getInstance()).getDisplayWidth();
        height = ((AbstractTnUiHelper)AbstractTnUiHelper.getInstance()).getDisplayHeight();
    }
    
    public static SplashScreenInflater getInstance()
    {
        return InnterSplashScreenInflater.inflater;
    }
    
    public TnScreen getSplashScreenView()
    {
        TnScreen screen = getSplashScreen();
        return screen;
    }
    
    public void release()
    {
        if (graphicsImage != null)
        {
            graphicsImage.release();
            graphicsImage = null;
        }
        
        if (scaledImage != null)
        {
            scaledImage.release();
            scaledImage = null;
        }
        
        fakeMapView = null;
        fakeMapViewLandscape = null;
    }
    
    public int getMiniMapHeight()
    {
        if(fakeMapView != null)
        {
            return fakeMapView.getHeight();
        }
        
        return 0;
    }
    
    public int getMiniMapWidth()
    {
        if(fakeMapView != null)
        {
            return fakeMapView.getWidth();
        }
        
        return 0;
    }
    public int getLandscapeMiniMapHeight()
    {
        if(fakeMapViewLandscape != null)
        {
            return fakeMapViewLandscape.getHeight();
        }
        
        return 0;
    }
    
    public int getLandscapeMiniMapWidth()
    {
        if(fakeMapViewLandscape != null)
        {
            return fakeMapViewLandscape.getWidth();
        }
        
        return 0;
    }
    
    public boolean hasMiniMapSize()
    {
        return hasMiniMapSize;
    }
    
    protected AbstractTnImage getScaleImage()
    {
        byte[] imageData = TnIoManager.getInstance().openFileBytesFromAppBundle("i18n/generic/images/common/splash_graphic_unfocused_portrait.png");        
        
        graphicsImage = AbstractTnGraphicsHelper.getInstance().createImage(imageData);
        if(graphicsImage != null)
        {
            int graphicsWidth = graphicsImage.getWidth();
            int graphicsHeight = graphicsImage.getHeight();

            float compressedRatio = (float) graphicsHeight / graphicsWidth;

            if (graphicsImage.getWidth() > 0) // avoid graphicsImage.bitmap is null
            {
                height = (int) (width * compressedRatio);
                scaledImage = graphicsImage.createScaledImage(width, height);
            }
        }
        
        return scaledImage;
    }
    
    protected TnScreen getSplashScreen()
    {
        TnScreen simplySplashScreen = new TnScreen(0)
        {
            
            @Override
            protected AbstractTnContainer createRootContainer()
            {
                TnAbsoluteContainer absoluteContainer = new TnAbsoluteContainer(0);
                TnMenu screenMenu = UiFactory.getInstance().createMenu();
                
                String exitStr = ResourceManager.getInstance().getCurrentBundle().getString(IStringCommon.RES_BTTN_EXIT,
                    IStringCommon.FAMILY_COMMON);        
                screenMenu.add(exitStr, ICommonConstants.CMD_COMMON_EXIT); 
                absoluteContainer.setMenu(screenMenu, AbstractTnComponent.TYPE_MENU);

                return absoluteContainer;
            }
        };
        
        TnAbsoluteContainer simpleyAabsoluteContainer = (TnAbsoluteContainer)simplySplashScreen.getRootContainer();
        
        TnUiArgs contentUiArgs = simpleyAabsoluteContainer.getTnUiArgs();
        contentUiArgs.put(TnUiArgs.KEY_PREFER_HEIGHT, new TnUiArgAdapter(null, new ITnUiArgsDecorator()
        {
            
            @Override
            public Object decorate(TnUiArgAdapter args)
            {
                int contentHeight = AppConfigHelper.getDisplayHeight();
                return contentHeight;
            }
        }));
        contentUiArgs.put(TnUiArgs.KEY_PREFER_WIDTH, new TnUiArgAdapter(null, new ITnUiArgsDecorator()
        {
            
            @Override
            public Object decorate(TnUiArgAdapter args)
            {
                int contentHeight = AppConfigHelper.getDisplayWidth();
                return contentHeight;
            }
        }));
        
        final int realWidth = Math.min(width, height);
        final int realHeight = Math.max(width, height);
        
        SplashBackroundComopnent splashComponent = new SplashBackroundComopnent(getScaleImage(), getFont(realWidth, realHeight));
        splashComponent.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
        {
            public Object decorate(TnUiArgAdapter args)
            {
                return PrimitiveTypeCache.valueOf(realHeight);
            }
        }));
        splashComponent.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, new TnUiArgAdapter(PrimitiveTypeCache.valueOf(1), new ITnUiArgsDecorator()
        {
            public Object decorate(TnUiArgAdapter args)
            {
                return PrimitiveTypeCache.valueOf(realWidth);
            }
        }));
        splashComponent.getTnUiArgs().put(TnUiArgs.KEY_POSITION_X, new TnUiArgAdapter(PrimitiveTypeCache.valueOf(1), new ITnUiArgsDecorator()
        {
            public Object decorate(TnUiArgAdapter args)
            {
                return PrimitiveTypeCache.valueOf(0);
            }
        }));
        splashComponent.getTnUiArgs().put(TnUiArgs.KEY_POSITION_Y, new TnUiArgAdapter(PrimitiveTypeCache.valueOf(1), new ITnUiArgsDecorator()
        {
            public Object decorate(TnUiArgAdapter args)
            {
                return PrimitiveTypeCache.valueOf(0);
            }
        }));
        
        simpleyAabsoluteContainer.add(splashComponent);
        View mainView = AndroidCitizenUiHelper.addContentView(simpleyAabsoluteContainer, R.layout.ftue_welcome_empty);
        
        
        hasMiniMapSize = checkMiniMapSize();
        if (!hasMiniMapSize)
        {
            View mainView_dashboard = AndroidCitizenUiHelper.addContentView(simpleyAabsoluteContainer, R.layout.dashboard_empty);
            View mainView_dashboard_landscape = AndroidCitizenUiHelper.addContentView(simpleyAabsoluteContainer, R.layout.dashboard_empty_landscape);
            fakeMapView = mainView_dashboard.findViewById(R.id.fake_mapview);
            View fakeDashboardLandscape = mainView_dashboard_landscape.findViewById(R.id.fake_Dashboard_landscape);
            AbsoluteLayout.LayoutParams params = (AbsoluteLayout.LayoutParams)fakeDashboardLandscape.getLayoutParams();
            params.height = width;
            params.width = height;
            fakeDashboardLandscape.setLayoutParams(params);
            fakeMapViewLandscape = mainView.findViewById(R.id.fake_mapview_landscape);
            
        }
        initLogo(mainView);
        
        MapContainer mapContainer = MapContainer.getInstance();
        mapContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
        {
            public Object decorate(TnUiArgAdapter args)
            {
                return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth());
            }
        }));
        mapContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
        {
            public Object decorate(TnUiArgAdapter args)
            {
                return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() - AppConfigHelper.getStatusBarHeight());
            }
        }));
        simplySplashScreen.setBackgroundComponent(mapContainer);
        
        return simplySplashScreen;
    }
    
    protected boolean checkMiniMapSize()
    {
        int miniMapHeight = DaoManager.getInstance().getSimpleConfigDao().get(SimpleConfigDao.KEY_MINI_MAP_HEIGHT);
        if (miniMapHeight <= 0)
        {
            return false;
        }
        
        int miniMapWidth = DaoManager.getInstance().getSimpleConfigDao().get(SimpleConfigDao.KEY_MINI_MAP_WIDTH);
        if (miniMapWidth <= 0)
        {
            return false;
        }

        int miniMapHeightLandscape = DaoManager.getInstance().getSimpleConfigDao().get(SimpleConfigDao.KEY_MINI_MAP_HEIGHT_LAND);
        if (miniMapHeightLandscape <= 0)
        {
            return false;
        }
        
        int miniMapWidthLandscape = DaoManager.getInstance().getSimpleConfigDao().get(SimpleConfigDao.KEY_MINI_MAP_WIDTH_LAND);
        if (miniMapWidthLandscape <= 0)
        {
            return false;
        }
        
        return true;
    }

    protected void initLogo(View mainView)
    {
        RatioSolidImageView logoImageView = (RatioSolidImageView)mainView.findViewById(R.id.splashlogo);
        
        if(logoImageView != null)
        {
            byte[] imageData = TnIoManager.getInstance().openFileBytesFromAppBundle("i18n/generic/images/common/ftue_splash_scout_logo_unfocused.png");
            
            if(imageData != null)
            {
                AbstractTnImage tempMapProviderImage = AbstractTnGraphicsHelper.getInstance().createImage(imageData);
                logoImageView.setScaleType(ImageView.ScaleType.FIT_XY);
                AssetsImageDrawable drawable = new AssetsImageDrawable(tempMapProviderImage);
                logoImageView.setBackgroundDrawable(drawable);
            }
        }
    }
    
    protected AbstractTnFont getFont(int width, int height)
    {
        int realSize = this.calcFontSizeByDensity(13, width, height);
        return ((AbstractTnUiHelper)AbstractTnUiHelper.getInstance()).createFont(AbstractTnFont.FAMILY_SANS_SERIF, AbstractTnFont.FONT_STYLE_PLAIN, realSize);
    }
    
    protected int calcFontSizeByDensity(int hugeDensityFontSize, int width, int height)
    {
        
        float densityRatio =  ((AbstractTnUiHelper)AbstractTnUiHelper.getInstance()).getDensity();
        if(densityRatio <= 0)
        {
            densityRatio = 1;
        }
        
        float ratio = getVisionRatio(width, height);
        
        int targetFontSize = (int)(hugeDensityFontSize * ratio / 1.5);
        
        // example: for Xoom, visionRatio = 2.5 but density = 1.0 
        if (ratio > 2 * densityRatio)
        {
            targetFontSize *= 1 - (ratio - densityRatio) / 10;
        }
        
        return targetFontSize;   
    }
    
    protected float getVisionRatio(int width, int height)
    {
        float ratio1 = getVisionRatio(width);
        float ratio2 = getStretchRatio(width, height);
        return ratio1 > ratio2 ? ratio2 : ratio1;
    }
    
    protected float getVisionRatio(int width)
    {
        int w1 = width;
        
        int w2 = ISpecialImageRes.MEDIUM_WIDTH;
        
        return 1.0f * w1 / w2;
    }
    
    protected float getStretchRatio(int width, int height)
    {
        int w1 = width;
        int h1 = height;
        
        int w2 = ISpecialImageRes.MEDIUM_WIDTH;
        int h2 = ISpecialImageRes.MEDIUM_HEIGHT;
        
        float numerator = w1 * h2 + h1 * w2;
        float denominator = 2 * w2 * h2;
        return numerator / denominator;
    }
    
    
}
