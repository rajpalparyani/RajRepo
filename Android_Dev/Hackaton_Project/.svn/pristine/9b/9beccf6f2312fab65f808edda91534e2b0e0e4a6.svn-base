/*
 * (c) Copyright 2010 by TeleNav, Inc.
 * All Rights Reserved.
 *
 */
package com.telenav.ui.citizen.map;

/**
 *@author JY Xu
 *@date Nov 25, 2010
 */

import com.telenav.mvc.ICommonConstants;
import com.telenav.navsdk.events.MapViewEvents.MapViewAddAnnotationRequest;
import com.telenav.navsdk.events.MapViewEvents.MapViewAddAnnotationRequest.Builder;
import com.telenav.navsdk.events.MapViewEvents.MapViewUpdateAnnotationRequest;
import com.telenav.res.ISpecialImageRes;
import com.telenav.tnui.graphics.AbstractTnGraphics;
import com.telenav.tnui.graphics.AbstractTnImage;

public class ImageAnnotation extends AbstractAnnotation implements IAnnotation
{
    AbstractTnImage image;
    double lat;
    double lon;
    float pivotPointX;
//    float pivotPointY;
//    int annotationType;
    long graphicId = -1;
    long viewId;
    //True will use json size setting
    boolean byJsonDefaultSize = true;
    
    public static final String STYLE_POIPOPUP_SCREEN_ANNOTATION = "screen_annotations.poipopup_screen_annotation";
    public static final String STYLE_POIPIN_SCREEN_ANNOTATION = "screen_annotations.poipin_screen_annotation";
    public static final String STYLE_ADDRESSPIN_SCREEN_ANNOTATION = "screen_annotations.addresspin_screen_annotation";
    public static final String STYLE_FLAG_SCREEN_ANNOTATION = "screen_annotations.flag_screen_annotation";
    public static final String STYLE_ADDRESS_SCREEN_ANNOTATION = "screen_annotations.address_screen_annotation";
    public static final String STYLE_DOT_SCREEN_ANNOTATION = "screen_annotations.dot_screen_annotation";
    
    String style = STYLE_FLAG_SCREEN_ANNOTATION;
    
    public ImageAnnotation(AbstractTnImage image, double lat, double lon,
            float pivotPointX, float pivotPointY, String style)
    {
        this(image, lat, lon, pivotPointX, pivotPointY, style,
                IMapContainerConstants.ANNOTATION_SCREEN);
    }
    
    public ImageAnnotation(AbstractTnImage image, double lat, double lon, float pivotPointX, float pivotPointY, String style,int annotationType)
    {
        super((int) ICommonConstants.ANOTATION_COMPONENT_TRANSLATE);
        this.image = image;
        this.lat = lat;
        this.lon = lon;
        this.pivotPointX = pivotPointX;
//        this.pivotPointY = pivotPointY;
        this.style = style;
//        this.annotationType = annotationType;
        this.viewId = MapContainer.getInstance().getViewId();
    }
    
    public ImageAnnotation(AbstractTnImage image, double lat, double lon, float pivotPointX, float pivotPointY, String style,
            int annotationType, boolean byDefaultSize)
    {
        this(image, lat, lon, pivotPointX, pivotPointY, style, annotationType);
        this.byJsonDefaultSize = byDefaultSize;
    }
    
    protected void paint(AbstractTnGraphics graphics)
    {
        
        
    }

    public long addToMap()
    {
        int iconWidth = getWidth();
        int iconHeight = getHeight();
        
        MapContainer mapContainer = MapContainer.getInstance();
        if (graphicId != -1)
        {
            mapContainer.unloadAsset("" + viewId, "" + graphicId);
        }
        else
        {
            graphicId = this.hashCode();
        }
        mapContainer.loadAsset("" + viewId, iconWidth, iconHeight, image, "" + graphicId);
        
        annotationId =  this.createAnnotation();
                
        return annotationId;
    }
    
    private float getScale()
    {
        float result = 0.0f;
        if(ISpecialImageRes.getOpenglImageFamily() == ISpecialImageRes.FAMILY_TINY)
        {
            result = 1.2f;
        }
        else if(ISpecialImageRes.getOpenglImageFamily() == ISpecialImageRes.FAMILY_SMALL)
        {
            result = 1.2f;
        }
        else if(ISpecialImageRes.getOpenglImageFamily() == ISpecialImageRes.FAMILY_MEDIUM)
        {
            result = 1.3f;
        }
        else if(ISpecialImageRes.getOpenglImageFamily() == ISpecialImageRes.FAMILY_LARGE)
        {
            result = 1.5f; 
        }
        
        return result;
    }

    public boolean handleDownEvent(int x, int y)
    {
        return false;
    }

    public boolean handleMoveEvent(int x, int y)
    {
        return false;
    }

    public boolean handleUpEvent(int x, int y)
    {
        return false;
    }

    public boolean handleClickEvent(int x, int y)
    {
        return false;
    }
    
    public int getWidth()
    {
        return image.getWidth();
    }

    public int getHeight()
    {
        return image.getHeight();
    }
    
    public boolean isSetFocused()
    {
        return false;
    }

	public double getLat()
	{
		return lat;
	}

	public double getLon()
	{
		return lon;
	}

    public long getGraphicId()
    {
        return this.graphicId;
    }

    public long getPickableIdNum()
    {
        return this.hashCode();
    }

    AbstractTnImage getImage()
    {
        return image;
    }

    Builder generaterBuilder()
    {
        MapViewAddAnnotationRequest.Builder builder = MapViewAddAnnotationRequest.newBuilder();
        com.telenav.navsdk.events.PointOfInterestData.Location.Builder locationBuilder = com.telenav.navsdk.events.PointOfInterestData.Location.newBuilder();
        locationBuilder.setLatitude(lat);
        locationBuilder.setLongitude(lon);
        builder.setLocation(locationBuilder.build());
        builder.setPivotX(pivotPointX);
        builder.setPivotY(0);
        builder.setStyle(style);
        builder.setZorder(IMapContainerConstants.NAV_SDK_Z_ORDER_USER_DEFINED);
        builder.setClickable(false);
        if (!byJsonDefaultSize)
        {
            builder.setWidth(getWidth());
            builder.setHeight(getHeight());
        }
        builder.setAssetName("" + this.graphicId);
        return builder;
    }

    String getViewName()
    {
        return "" + viewId;
    }

    public MapViewUpdateAnnotationRequest.Builder updateGraphic()
    {
        return null;
    }
    
}
