/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * NavZoomButton.java
 *
 */
package com.telenav.module.nav;

import com.telenav.logger.Logger;
import com.telenav.map.MapConfig;
import com.telenav.module.AppConfigHelper;
import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.core.AbstractTnUiHelper;
import com.telenav.tnui.core.TnMotionEvent;
import com.telenav.tnui.core.TnUiEvent;
import com.telenav.tnui.graphics.AbstractTnGraphics;
import com.telenav.tnui.graphics.AbstractTnImage;
import com.telenav.tnui.widget.TnLinearContainer;
import com.telenav.ui.ImageDecorator;
import com.telenav.ui.NinePatchImageDecorator;
import com.telenav.ui.citizen.map.MapContainer;

/**
 *@author zhdong@telenav.cn
 *@date 2011-2-23
 */
public class NavZoomButton extends AbstractTnComponent
{

    boolean isZoomIn;    
    boolean isHorizonLayout = true;
    private boolean zoomInState;//zoom button's state 
    private boolean zoomOutState;//zoom button's state 
    AbstractTnImage image;
    IManualZoomLevelChangeListener listener;
    IZoomButtonListener zoomButtonListener;
    boolean needSync;
    
    public NavZoomButton(int id, boolean isZoomIn)
    {
        super(id);
        this.isZoomIn = isZoomIn;
        int zoomLevel = (int) (MapContainer.getInstance().getZoomLevel() + 0.5);  
        if(zoomLevel <= MapConfig.MOVING_MAP_MIN_ZOOM_LEVEL)
        {
        	zoomInState = false;
        }
        else
        {
        	zoomInState = true;
        }   
        if(zoomLevel >= MapConfig.MOVING_MAP_MAX_ZOOM_LEVEL)
        {
        	zoomOutState = false;
        }
        else
        {
        	zoomOutState = true;
        }
    }
    
    public boolean getZoomInState()
    {
    	return this.zoomInState;
    }
    public boolean getZoomOutState()
    {
    	return this.zoomOutState;
    }
    
    public void setZoomInState(boolean state)
    {
    	this.zoomInState = state;
    }
    public void setZoomOutState(boolean state)
    {
    	this.zoomOutState = state;
    }
    
    public void setIsHorizonLayout(boolean isHorizonLayout)
    {
        this.isHorizonLayout = isHorizonLayout;
    }
    
    protected void paint(AbstractTnGraphics graphics)
    {
        int width = this.getWidth();
        int height = this.getHeight();

        AbstractTnImage bgImage = this.getBgImage();
        bgImage.setWidth(width);
        bgImage.setHeight(height);
        graphics.drawImage(bgImage, 0, 0, AbstractTnGraphics.LEFT | AbstractTnGraphics.TOP);       
        
        int x = width / 2;
        int y = height / 2;
        this.image = this.getZoomImage();
        graphics.drawImage(image, x, y, AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);
    }
    
    private AbstractTnImage getZoomImage()
    {
        if (isZoomIn)
        {
        	if(zoomInState)
        	{
        	    if(this.isFocused)
        	    {
        	        this.image =  ImageDecorator.IMG_NAV_ZOOM_IN_ICON_FOCUSED.getImage();
        	    }
        	    else
        	    {
        	        this.image =  ImageDecorator.IMG_NAV_ZOOM_IN_ICON_UNFOCUSED.getImage();
        	    }
        	}
        	else
        	{
        		this.image =  ImageDecorator.IMG_NAV_ZOOM_IN_ICON_DISABLE.getImage();
        	}        	
        }
        else
        {
        	if(zoomOutState)
        	{
        	    if(this.isFocused)
                {
        	        this.image = ImageDecorator.IMG_NAV_ZOOM_OUT_ICON_FOCUSED.getImage();
                }
        	    else
        	    {
        	        this.image = ImageDecorator.IMG_NAV_ZOOM_OUT_ICON_UNFOCUSED.getImage();
        	    }
        	}
        	else
        	{
        		this.image =  ImageDecorator.IMG_NAV_ZOOM_OUT_ICON_DISABLE.getImage();
        	}         	
        }
        return this.image;
    }
    
    private AbstractTnImage getBgImage()
    {
        boolean isPortarit = ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT;
        boolean isFocused = this.isFocused();
        
        if (isHorizonLayout || isPortarit || AppConfigHelper.isTabletSize())
        {
            if (isZoomIn)
            {
                if (isFocused && zoomInState)
                {
                    return NinePatchImageDecorator.NAV_CONTROLS_BG_FOCUSED_RIGHT.getImage();
                }
                else
                {
                    return NinePatchImageDecorator.NAV_CONTROLS_BG_UNFOCUSED_RIGHT.getImage();
                }
            }
            else
            {
                if (isFocused && zoomOutState)
                {
                    return NinePatchImageDecorator.NAV_CONTROLS_BG_FOCUSED_LEFT.getImage();
                }
                else
                {
                    return NinePatchImageDecorator.NAV_CONTROLS_BG_UNFOCUSED_LEFT.getImage();
                }
            }
        }
        else
        {
            if (isZoomIn)
            {
                if (isFocused)
                {
                    return NinePatchImageDecorator.NAV_CONTROLS_BG_FOCUSED_TOP.getImage();
                }
                else
                {
                    return NinePatchImageDecorator.NAV_CONTROLS_BG_UNFOCUSED_TOP.getImage();
                }
            }
            else
            {
                if (isFocused)
                {
                    return NinePatchImageDecorator.NAV_CONTROLS_BG_FOCUSED_BOTTOM.getImage();
                }
                else
                {
                    return NinePatchImageDecorator.NAV_CONTROLS_BG_UNFOCUSED_BOTTOM.getImage();
                }
            }
        }
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
                        int zoomLevel;
                        if (needSync)
                        {
                        	zoomLevel = (int) (MapContainer.getInstance().getZoomLevel(true) + 0.5);
                        	needSync = false;
                        }
                        else
                        {
                            zoomLevel = (int) (MapContainer.getInstance().getZoomLevel() + 0.5);
                        }
                        if (isZoomIn)
                        {
                        	if (zoomLevel > MapConfig.MOVING_MAP_MIN_ZOOM_LEVEL)
                        	{
                        		MapContainer.getInstance().zoomInMap();
                        		if (zoomButtonListener != null)
                        		{
                        		    zoomButtonListener.onZoomIn();
                        		}
                        		if(listener != null)
                        		{
                        		    listener.onManualZoomLevelChanged();
                        		}
                        	}                            
                        }
                        else
                        {                  	                       
                        	if (zoomLevel < MapConfig.MOVING_MAP_MAX_ZOOM_LEVEL)
                            {
                        		MapContainer.getInstance().zoomOutMap();
                        		if (zoomButtonListener != null)
                        		{
                        		    zoomButtonListener.onZoomOut();
                        		}
                        		if(listener != null)
                                {
                                    listener.onManualZoomLevelChanged();
                                }
                            }
                        }
                    	
                        updateZoomButtonStatus();

                        float transactionTime = MapContainer.getInstance().getFasterTransitionTime();
                        MapContainer.getInstance().setMapTransitionTime(transactionTime);
                        
                    }
                }
            }
        }

        return super.handleUiEvent(tnUiEvent);
    }

    public void updateZoomBtnState(int zoomLevel)
    {
    	TnLinearContainer parent = (TnLinearContainer)this.getParent();
    	NavZoomButton zoomInButton = (NavZoomButton)parent.getComponentById(1012);//IMovingMapConstants.ID_ZOOM_IN_BUTTON=1012
    	NavZoomButton zoomOutButton = (NavZoomButton)parent.getComponentById(1013);//IMovingMapConstants.ID_ZOOM_OUT_BUTTON=1013
    	if(zoomInButton != null && zoomOutButton != null)
    	{
    		if( zoomLevel != MapConfig.MOVING_MAP_MIN_ZOOM_LEVEL && zoomLevel != MapConfig.MOVING_MAP_MAX_ZOOM_LEVEL 
    	    		&& zoomInButton.zoomInState == true && zoomOutButton.zoomOutState == true)   
			{
				return; // the most situations needn't update state
			}
    		if(zoomLevel == MapConfig.MOVING_MAP_MIN_ZOOM_LEVEL && zoomInButton.zoomInState == true)//disable zoomIn
    		{
    			zoomInButton.image =  ImageDecorator.IMG_NAV_ZOOM_IN_ICON_DISABLE.getImage();
    			zoomInButton.setZoomInState(false);
    			zoomInButton.requestPaint();
    			return;
    		}
    		if(zoomLevel == MapConfig.MOVING_MAP_MAX_ZOOM_LEVEL && zoomOutButton.zoomOutState == true)//disable zoomOut
    		{
    			zoomOutButton.image =  ImageDecorator.IMG_NAV_ZOOM_OUT_ICON_DISABLE.getImage();
    			zoomOutButton.setZoomOutState(false);
    			zoomOutButton.requestPaint();
    			return;
    		}
    		if(zoomLevel != MapConfig.MOVING_MAP_MIN_ZOOM_LEVEL && zoomInButton.zoomInState == false)//enable zoomIn
    		{
    			zoomInButton.image =   this.isFocused ? ImageDecorator.IMG_NAV_ZOOM_IN_ICON_FOCUSED.getImage() :ImageDecorator.IMG_NAV_ZOOM_IN_ICON_UNFOCUSED.getImage();
    			zoomInButton.setZoomInState(true);
    			zoomInButton.requestPaint();
    			return;
    		}
    		if(zoomLevel != MapConfig.MOVING_MAP_MAX_ZOOM_LEVEL && zoomOutButton.zoomOutState == false)//enable zoomOut
    		{
    			zoomOutButton.image =  this.isFocused ? ImageDecorator.IMG_NAV_ZOOM_OUT_ICON_FOCUSED.getImage() : ImageDecorator.IMG_NAV_ZOOM_OUT_ICON_UNFOCUSED.getImage();
    			zoomOutButton.setZoomOutState(true);
    			zoomOutButton.requestPaint();
    			return;
    		}
    		
    	}
    }
    
    public void updateZoomButtonStatus()
    {
        int zoomLevel = (int) (MapContainer.getInstance().getZoomLevel() + 0.5);
        if (Logger.DEBUG)
        {
            Logger.log(Logger.INFO, this.getClass().getName(), "update ZoomButton Status---active zoom level : " + zoomLevel);
        }
        
        TnLinearContainer parent = (TnLinearContainer)this.getParent();
        NavZoomButton zoomInButton = (NavZoomButton)parent.getComponentById(1012);//IMovingMapConstants.ID_ZOOM_IN_BUTTON=1012
        NavZoomButton zoomOutButton = (NavZoomButton)parent.getComponentById(1013);//IMovingMapConstants.ID_ZOOM_OUT_BUTTON=1013                        
        
        if(zoomInButton != null && zoomOutButton != null)
        {
            if (zoomLevel <= MapConfig.MOVING_MAP_MIN_ZOOM_LEVEL)//need disable the zoomin 
            {
                zoomInButton.updateZoomBtnState(MapConfig.MOVING_MAP_MIN_ZOOM_LEVEL);                                                                                                                       
            }
            else
            {  
                if(!zoomInButton.getZoomInState())//if the zoomIn is disabled, then need enable the zoomin
                {
                    zoomInButton.updateZoomBtnState(zoomLevel);
                }
            }
            if (zoomLevel >= MapConfig.MOVING_MAP_MAX_ZOOM_LEVEL)//need disable the zoomout
            {
                zoomOutButton.updateZoomBtnState(MapConfig.MOVING_MAP_MAX_ZOOM_LEVEL);                          
            } 
            else
            {
                if(!zoomOutButton.getZoomOutState())//if the zoomout is disabled, then need enable the zoomout
                {        
                    zoomOutButton.updateZoomBtnState(zoomLevel);
                }
            }
        }   
    }
    
    public void setManualZoomLevelChangeListener(IManualZoomLevelChangeListener listener)
    {
        this.listener = listener;
    }
    
    public void setZoomButtonListener(IZoomButtonListener listener)
    {
        this.zoomButtonListener = listener;
    }
    
    public boolean isNeedSync()
    {
        return needSync;
    }

    public void setNeedSync(boolean needSync)
    {
        this.needSync = needSync;
    }

    protected  interface IManualZoomLevelChangeListener
    {
        public void onManualZoomLevelChanged();
    }
    
    public interface IZoomButtonListener
    {
        public void onZoomIn();
        public void onZoomOut();
    }
}
