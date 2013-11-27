/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * AbstractAnnotation.java
 *
 */
package com.telenav.ui.citizen.map;

import com.telenav.navsdk.events.MapViewEvents.MapViewAddAnnotationRequest;
import com.telenav.navsdk.events.MapViewEvents.MapViewUpdateAnnotationRequest;
import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.graphics.AbstractTnImage;

/**
 *@author yren
 *@date Oct 1, 2012
 */
public abstract class AbstractAnnotation extends AbstractTnComponent implements IAnnotation
{    
    public final static float POI_TRANSITION_TIME = 2.0f;
    
    public double lat;

    public double lon;
    
    private static int annotationCounterId;
    
    private static int graphicId;
    
    protected int annotationId;
    
    AbstractAnnotation(int id)
    {
        super(id, true);
    }
    
    abstract AbstractTnImage getImage();
    
    abstract MapViewAddAnnotationRequest.Builder generaterBuilder();
    
    abstract String getViewName();
    
    public abstract boolean handleDownEvent(int x, int y);

    public abstract boolean handleUpEvent(int x, int y);

    public abstract boolean handleMoveEvent(int x, int y);
    
    public abstract boolean handleClickEvent(int x, int y);

    public abstract long addToMap();
    
    public abstract MapViewUpdateAnnotationRequest.Builder updateGraphic();

    public abstract int getWidth();

    public abstract int getHeight();

    public abstract boolean isSetFocused();
    
    public abstract double getLat();
    
    public abstract double getLon();
    
    public abstract long getGraphicId();
    
    public void requestPaint()
    {
        MapContainer.getInstance().updateFeature(this);
    }
    
    private int generateAnnotationId()
    {
        annotationCounterId++;
        if(annotationCounterId > Integer.MAX_VALUE - 1)
        {
            annotationCounterId = 0;
        }
        return annotationCounterId;
    }
    
    protected int generateGraphicId()
    {
        graphicId++;
        if(graphicId > Integer.MAX_VALUE - 1)
        {
            graphicId = 0;
        }
        return graphicId;
    }
    
    protected int createAnnotation()
    {
        this.annotationId = generateAnnotationId();
        MapContainer.getInstance().addAnnotation(getViewName(), "" + annotationId,
            generaterBuilder());
        return annotationId;
    }
    
    public long getAnnotationId()
    {
        return annotationId;
    }
}
