/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * MapUiEventDelegate.java
 *
 */
package com.telenav.ui.citizen.map;

import com.telenav.map.IMapEngine.AnnotationSearchResult.TrafficPickable;
import com.telenav.tnui.core.AbstractTnUiHelper;
import com.telenav.tnui.core.TnUiEvent;

/**
 *@author bduan
 *@date 2011-6-16
 */
public class MapUiEventDelegate
{
    public static void handleMapRgc(final IMapUIEventListener listener, final double latitude, final double longitude)
    {
        if (listener != null)
        {
            ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).runOnUiThread(new Runnable()
            {
                public void run()
                {
                    listener.handleMapRgc(latitude, longitude);
                }
            });
        }
    }

    public static void handleTouchEventOnMap(final IMapUIEventListener listener, final MapContainer container, final TnUiEvent uiEvent)
    {
        if (listener != null)
        {
            ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).runOnUiThread(new Runnable()
            {
                public void run()
                {
                    container.interactionModeChanged(IMapContainerConstants.INTERACTION_MODE_PAN_AND_ZOOM);
                    listener.handleTouchEventOnMap(container, uiEvent);
                }
            });
        }
    }

    public static void handleTrafficIndcident(final IMapUIEventListener listener, final TrafficPickable pickable)
    {
        if(listener != null)
        {
//            ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).runOnUiThread(new Runnable()
//            {
//                public void run()
//                {
//                    //we need to do with traffic incident first
//                    listener.handleTrafficIndcident(pickable);
//                }
//            });
        }
    }

    public static void mapViewSizeChanged(final IMapUIEventListener listener)
    {
        if (listener != null)
        {
            ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).runOnUiThread(new Runnable()
            {
                public void run()
                {
                    listener.mapViewSizeChanged();
                }
            });
        }
    }

    public static void tapNoAnnotation(final IMapUIEventListener listener, final MapContainer container)
    {
        if (listener != null)
        {
            listener.tapNoAnnotation();
        }
    }
    
    public static void onPinchEvent(final IMapUIEventListener listener)
    {
        if(listener != null)
        {
            ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).runOnUiThread(new Runnable()
            {
                public void run()
                {
                    listener.onPinchEvent();
                }
            });
        }
    }
    
    public static void handleZoomOut(final IMapUIEventListener listener)
    {
        if(listener != null)
        {
            ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).runOnUiThread(new Runnable()
            {
                public void run()
                {
                    listener.handleZoomOut();
                }
            });
        }
    }
}
