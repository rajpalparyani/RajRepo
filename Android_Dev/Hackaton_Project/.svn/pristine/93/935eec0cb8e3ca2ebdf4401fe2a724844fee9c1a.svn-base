/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * IncidentAnnotation.java
 *
 */
package com.telenav.ui.citizen.map;

import com.telenav.data.cache.ImageCacheManager;
import com.telenav.datatypes.traffic.TrafficIncident;
import com.telenav.module.nav.trafficengine.TrafficAlertEvent;
import com.telenav.mvc.ICommonConstants;
import com.telenav.navsdk.events.MapViewEvents.MapViewAddAnnotationRequest;
import com.telenav.navsdk.events.MapViewEvents.MapViewAddAnnotationRequest.Builder;
import com.telenav.tnui.core.AbstractTnUiHelper;
import com.telenav.tnui.core.TnCommandEvent;
import com.telenav.tnui.core.TnMotionEvent;
import com.telenav.tnui.core.TnUiEvent;
import com.telenav.tnui.graphics.AbstractTnGraphics;
import com.telenav.tnui.graphics.AbstractTnImage;
import com.telenav.tnui.graphics.TnRect;
import com.telenav.ui.ImageDecorator;

/**
 *@author yren (yren@telenav.cn)
 *@date 2011-10-18
 */
public class IncidentAnnotation extends AbstractAnnotation implements IAnnotation
{

    long annotationId;

    int layer;

    long viewId;
    
    private TnRect incidentUnfocusedIconRect;
    
    private int iconWidth, iconHeight;
      
    private int incidentType = -1;
    
    private TrafficAlertEvent incident = null;
    
    private int unfocusedHeight;

    private int unfocusedWidth;
    
    private long lastClickTime;
    
    long pressTime;
    
    long graphicId = -1;
    
    public IncidentAnnotation(long id, TrafficAlertEvent incident)
    {
        super((int) (id + ICommonConstants.ANOTATION_COMPONENT_TRANSLATE));
        incidentType = incident.getIncidentType();
        this.iconWidth = getIncidentIcon(incidentType).getWidth();
        this.iconHeight = getIncidentIcon(incidentType).getHeight();

        unfocusedHeight = iconHeight;
        unfocusedWidth = iconWidth;
        this.viewId = MapContainer.getInstance().getViewId();
        incidentUnfocusedIconRect = new TnRect(0, 0, iconWidth, iconHeight);
        this.incident = incident;
        this.lat = incident.getIncidentLocation().getLatitude() / 100000.0;
        this.lon = incident.getIncidentLocation().getLongitude() / 100000.0;
    }

    AbstractTnImage getImage()
    {
        return createAnnotationImage();
    }
    
    
    public void setIncidentType(int incidentType)
    {
        this.incidentType = incidentType;
    }
    
    protected AbstractTnImage createAnnotationImage()
    {
        String key = getIncidentAnnotationImageKey();
        AbstractTnImage image;
        image = (AbstractTnImage) ImageCacheManager.getInstance().getMutableImageCache().get(key);
        if (image == null)
        {
            int width = getWidth();
            int height = getHeight();
            image = AbstractTnUiHelper.getInstance().createImage(width, height);
            ImageCacheManager.getInstance().getMutableImageCache().put(key, image);
        }
        image.clear(0x0);
        AbstractTnGraphics g = image.getGraphics();
        paint(g);

        return image;
    }
    
    public int getWidth()
    {
        return unfocusedWidth;
    }

    public int getHeight()
    {
        return unfocusedHeight;
    }

    
    protected String getIncidentAnnotationImageKey()
    {
        String incidentKey = "";
        if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
        {
            incidentKey = "incidentPopupImagePortait";
        }
        else
        {
            incidentKey = "incidentPopupImageLandscape";
        }
        return incidentKey;
    }

    protected void paint(AbstractTnGraphics g)
    {
        drawIcon(g);
    }
    
    protected void drawIcon(AbstractTnGraphics g)
    {
        AbstractTnImage image = null;
        image = getIncidentIcon(incidentType);

        g.drawImage(image, incidentUnfocusedIconRect.left, incidentUnfocusedIconRect.top, AbstractTnGraphics.LEFT | AbstractTnGraphics.TOP);
    }
    
    private AbstractTnImage getIncidentIcon(int incidentType)
    {
        AbstractTnImage image = null;
        switch (incidentType)
        {
            case TrafficIncident.TYPE_ACCIDENT:
            {
                image = ImageDecorator.IMG_INCIDENT_ACCIDENT.getImage();
                break;
            }
            case TrafficIncident.TYPE_CONGESTION:
            {
                image = ImageDecorator.IMG_INCIDENT_CONGESTION.getImage();
                break;
            }
            case TrafficIncident.TYPE_CONSTRUCTION:
            {
                image = ImageDecorator.IMG_INCIDENT_CONSTRUCTION.getImage();
                break;
            }
            case TrafficIncident.TYPE_CAMERA:
            {
                image = ImageDecorator.IMG_TRAFFIC_CAMERA_ON_NAV.getImage();
                break;
            }
            case TrafficIncident.TYPE_SPEED_TRAP:
            {
                image = ImageDecorator.IMG_SPEED_TRAP_ON_NAV.getImage();
                break;
            }
            default:
            {
                image = ImageDecorator.IMG_INCIDENT_DEFAULT.getImage();
                break;
            }
        }
        return image;
    }

    protected boolean handleUiEvent(TnUiEvent tnUiEvent)
    {
        if(tnUiEvent.getType() == TnUiEvent.TYPE_TOUCH_EVENT)
        {
            TnMotionEvent motion = tnUiEvent.getMotionEvent();
            int action = motion.getAction();
            switch(action)
            {
                case TnMotionEvent.ACTION_DOWN:
                {
                    return handleDownEvent(motion.getX(), motion.getY());
                }
                case TnMotionEvent.ACTION_MOVE:
                {
                    break;
                }
                case TnMotionEvent.ACTION_UP:
                {
                    return handleUpEvent(motion.getX(), motion.getY());
                }
                case TnMotionEvent.ACTION_OUTSIDE:
                {
                    return handleOutSideEvent();
                }
            }
        }
        return true;
    }

    private boolean handleOutSideEvent()
    {
        return true;
    }
    
    protected boolean isTapOnIcon(int x, int y)
    {
        return incidentUnfocusedIconRect.contains(x, y);
    }
    
    protected String getStyle()
    {
        String style = "";
        String header = "traffic_annotation.";
        String tile = "";
        switch (incidentType)
        {
            case TrafficIncident.TYPE_ACCIDENT:
            {
                tile = "accident_";
                break;
            }
            case TrafficIncident.TYPE_CONGESTION:
            {
                tile = "congestion_";
                break;
            }
            case TrafficIncident.TYPE_CONSTRUCTION:
            {
                tile = "construction_";
                break;
            }
            case TrafficIncident.TYPE_CAMERA:
            {
                tile = "speed camera";
                return header + tile;
            }
            case TrafficIncident.TYPE_SPEED_TRAP:
            {
                tile = "speed trap";
                return header + tile;
            }
            default:
            {
                tile = "accident_";
                break;
            }
        }
        switch (this.incident.getSeverity())
        {
            case TrafficIncident.SEVERITY_SEVERE:
            {
                tile = tile + "severe";
                break;
            }
            case TrafficIncident.SEVERITY_MAJOR:
            {
                tile = tile + "major";
                break;
            }
            case TrafficIncident.SEVERITY_MINOR:
            {
                tile = tile + "minor";
                break;
            }
            default:
            {
                tile = "severe";
                break;
            }
        }
        style = header + tile;
        return style;
    }
    
    public long addToMap()
    {
        MapContainer mapContainer = MapContainer.getInstance();
        AbstractTnImage image = createAnnotationImage();
        if (graphicId != -1)
        {
            mapContainer.unloadAsset("" + viewId, "" + graphicId);
        }
        graphicId = generateGraphicId();
        mapContainer.loadAsset("" + viewId, getWidth(), getHeight(), image, "" + graphicId);
        
        annotationId =  this.createAnnotation();
        return annotationId;
    }
    
    public boolean handleDownEvent(int x, int y)
    {
        pressTime = System.currentTimeMillis();
        return false;
    }

    public boolean handleUpEvent(int x, int y)
    {
        return false;
    }

    public boolean handleMoveEvent(int x, int y)
    {
        long currentTime = System.currentTimeMillis();
        if(currentTime - pressTime < 800)
        {
            handleClickEvent(x, y);
        }
        
        return true;
    }

    public boolean handleClickEvent(int x, int y)
    {
        if (System.currentTimeMillis() - lastClickTime < 300 || this.incident.getIncidentType() == TrafficIncident.TYPE_CAMERA
                || this.incident.getIncidentType() == TrafficIncident.TYPE_SPEED_TRAP)
        {
            return true;
        }
        else
        {
            lastClickTime = System.currentTimeMillis();
        }

        ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance())
                .runOnUiThread(new Runnable()
                {
                    public void run()
                    {
                        TnUiEvent tnUiEvent = new TnUiEvent(TnUiEvent.TYPE_COMMAND_EVENT,
                                IncidentAnnotation.this);
                        tnUiEvent.setCommandEvent(new TnCommandEvent(
                                ICommonConstants.CMD_SHOW_INCIDENT_DETAIL));
                        IncidentAnnotation.this.commandListener.handleUiEvent(tnUiEvent);
                    }
                });
        return true;
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

    Builder generaterBuilder()
    {
        layer = IMapContainerConstants.NAV_SDK_Z_ORDER_TRAFFIC_INCIDENT_CUSTOM;
        String style = this.getStyle();
        MapViewAddAnnotationRequest.Builder builder = MapViewAddAnnotationRequest
                .newBuilder();
        com.telenav.navsdk.events.PointOfInterestData.Location.Builder locationBuilder = com.telenav.navsdk.events.PointOfInterestData.Location
                .newBuilder();
        locationBuilder.setLatitude(lat);
        locationBuilder.setLongitude(lon);
        builder.setVisible(true);
        builder.setLocation(locationBuilder.build());
        builder.setPivotX(0.5f);
        builder.setPivotY(0);
        builder.setStyle(style);
        builder.setZorder(layer);
        builder.setClickable(true);
        builder.setType(com.telenav.navsdk.events.MapViewData.AnnotationType.AnnotationType_Sprite);
        builder.setAssetName("" + this.graphicId);
        
        return builder;
    }

    String getViewName()
    {
        return "" + this.viewId;
    }

    public com.telenav.navsdk.events.MapViewEvents.MapViewUpdateAnnotationRequest.Builder updateGraphic()
    {
        return null;
    }
}

