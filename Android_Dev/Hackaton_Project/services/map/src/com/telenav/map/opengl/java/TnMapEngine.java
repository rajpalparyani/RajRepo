/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * TnMapEngine.java
 *
 */
package com.telenav.map.opengl.java;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import com.telenav.datatypes.route.RouteWrapper;
import com.telenav.map.EngineState;
import com.telenav.map.IMapEngine;
import com.telenav.map.IMapEngine.AnnotationSearchResult;
import com.telenav.map.opengl.java.TnMap.TnMapRect;
import com.telenav.map.opengl.java.proxy.AbstractTileProvider;
import com.telenav.map.opengl.java.proxy.ITnMapResourceProxy;
import com.telenav.map.opengl.java.proxy.TnMapConfigData;
import com.telenav.tnui.graphics.AbstractTnImage;
import com.telenav.tnui.opengles.TnGL10;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2010-10-8
 */
public class TnMapEngine
{
    public static class Pickable
    {

    }

    Vector canvases = new Vector();

    int    targetUpdateRate;     //!< Frames per second
    float       currentTimeSlice;
    boolean        forceRedraw;

    protected ITnMapResourceProxy resourceProxy;
    
    protected AbstractTileProvider tileProvider;
    
    // Engine-wide texture loader (needs to be declared after m_proxyFactory)
    protected TnMapTextureLoader textureLoader;

    //<ITnMapEngine::int, TnMapCanvas::wptr>
    protected Hashtable canvasIdDoubleMap = new Hashtable();
    
    protected TnMapCanvas currentCanvas;
    protected int currentCanvasId = -1;

    protected int canvas_id_counter = 0;
    
    protected int graphic_id_counter = 0;
    
    protected TnGL10 gl10;
    
    protected TnMapBuildManager m_buildManager;

    public TnMapEngine(TnGL10 gl10, ITnMapResourceProxy resourceProxy, AbstractTileProvider tileProvider)
    {
        this.gl10 = gl10;
        targetUpdateRate = 30;
        currentTimeSlice = 0.0f;
        forceRedraw = true;
        this.resourceProxy = resourceProxy;
        this.tileProvider = tileProvider;
        textureLoader = new TnMapTextureLoader(gl10, resourceProxy);
        m_buildManager = new TnMapBuildManager(gl10, textureLoader);
    }

    /*! This generates a unique ViewId. It should only be called once per
     *  creation of a CanvasPtr. */
    public int createViewId(TnMapCanvas canvas) 
    {
        int viewId = ++canvas_id_counter;

        currentCanvas = canvas;
        currentCanvasId = viewId;
        canvasIdDoubleMap.put(new Integer(viewId), canvas);
        return viewId;
    }

    public boolean destroyViewId(int viewId)
    {
        canvasIdDoubleMap.remove(new Integer(viewId));
        if (currentCanvasId == viewId)
        {
            currentCanvas = null;
            currentCanvasId = -1;
        }
        return true;
    }

    public TnMapCanvas viewIdToCanvas(int viewId)
    {
        if (currentCanvasId == viewId)
            return currentCanvas;
        
        TnMapCanvas canvas = (TnMapCanvas) canvasIdDoubleMap.get(new Integer(viewId));
        if(canvas != null)
        {
            currentCanvasId = viewId;
            currentCanvas = canvas;
            return canvas;
        }

        return null;
    }

    public int canvasToViewId(TnMapCanvas canvas)
    {
        if (currentCanvas == canvas && currentCanvas != null)
            return currentCanvasId;
        
        Enumeration e = canvasIdDoubleMap.keys();
        while(e.hasMoreElements())
        {
            Integer key = (Integer)e.nextElement();
            if(canvasIdDoubleMap.get(key) == canvas)
            {
                return key.intValue();
            }
        }

        return 0;
    }

    public static TnMapEngine createMapEngine(TnGL10 gl10, ITnMapResourceProxy resourceProxy, AbstractTileProvider tileProvider)
    {
        return new TnMapEngine(gl10, resourceProxy, tileProvider);
    }

    public static void destroyMapEngine(TnMapEngine engine)
    {
        engine.destroy();
    }

    public void destroy()
    {
        //            System.out.println("*****TnMapEngine destroy-----");
        release();
    }

    public boolean setBool(int viewId, int param, boolean value)
    {
        TnMapCanvas canvas = viewIdToCanvas(viewId);
        if(canvas != null)
        {
            return canvas.setBool(param, value);
        }

        // Invalid canvas
        return false;
    }

    public boolean setInt(int viewId, int param, int value)
    {
        TnMapCanvas canvas = viewIdToCanvas(viewId);
        if(canvas != null)
        {
            return canvas.setInt(param, value);
        }

        // Invalid canvas
        return false;
    }

    public boolean setDouble(int viewId, int param, double value)
    {
        TnMapCanvas canvas = viewIdToCanvas(viewId);
        if(canvas != null)
        {
            return canvas.setDouble(param, value);
        }

        // Invalid canvas
        return false;
    }

    public boolean getBool(int viewId, int param, boolean[] value)
    {
        TnMapCanvas canvas = viewIdToCanvas(viewId);
        if(canvas != null)
        {
            return canvas.getBool(param, value);
        }

        // Invalid canvas
        return false;
    }

    public boolean getInt(int viewId, int param, int[] value)
    {
        TnMapCanvas canvas = viewIdToCanvas(viewId);
        if(canvas != null)
        {
            return canvas.getInt(param, value);
        }

        // Invalid canvas
        return false;
    }

    public boolean getDouble(int viewId, int param, double[] value)
    {
        TnMapCanvas canvas = viewIdToCanvas(viewId);
        if(canvas != null)
        {
            return canvas.getDouble(param, value);
        }

        // Invalid canvas
        return false;
    }


    public void setOrientation(int viewId, int orientation)
    {
        TnMapCanvas canvas = viewIdToCanvas(viewId);
        if (canvas != null)
        {
            canvas.setOrientation(orientation);
        }
    }

    public int getOrientation(int viewId)
    {
        TnMapCanvas canvas = viewIdToCanvas(viewId);
        if (canvas != null)
        {
            return canvas.getOrientation();
        }
        return IMapEngine.ORIENTATION_INVALID;
    }

    public int createView(int x, 
            int y, 
            int width, 
            int height)
    {
        TnMapRect rc = new TnMapRect();
        rc.x = x;
        rc.y = y;
        rc.dx = width;
        rc.dy = height;

        TnMapCanvas canvas = new TnMapCanvas(gl10, this.tileProvider, textureLoader, this.m_buildManager, rc);
        canvases.addElement(canvas);

        TnMapConfigData config_data = new TnMapConfigData(canvas, ":configuration");
        resourceProxy.requestResource(config_data);

        return createViewId(canvas);
    }


    public int createView(int id ,
            int x, int y, 
            int width, int height)
    {
        // @TODO: Implement shared annotations.
        return createView(x, y, width, height);
    }


    public boolean deleteView(int viewId)
    {
        TnMapCanvas weak_ptr = viewIdToCanvas(viewId);

        if (weak_ptr != null) 
        {
            destroyViewId(viewId);

            return canvases.removeElement(weak_ptr);
        }
        return false;
    }


    public boolean resizeView(int viewId, 
            int x, 
            int y, 
            int width, 
            int height)
    {
        TnMapRect rc = new TnMapRect();
        rc.x = x;
        rc.y = y;
        rc.dx = width;
        rc.dy = height;

        TnMapCanvas canvas = viewIdToCanvas(viewId);
        if(canvas != null)
        { 
            canvas.resize(rc);
            return true;
        }
        return false;
    }

    public boolean handleTouchEvent(int viewId, 
            int event,
            int x, 
            int y)
    {
        return handleTouchEvent(viewId, event, x, y, 0);
    }

    public boolean handleTouchEvent(int viewId, 
            int event,
            int x, 
            int y, 
            float distance)
    {
        TnMapCanvas canvas = viewIdToCanvas(viewId);
        if(canvas != null)
        {
            canvas.handleTouchEvent(event, x, y, distance);
            return true;
        }
        return false;
    }

    public boolean setTransitionTime(int id, float seconds)
    {
        TnMapCanvas canvas = viewIdToCanvas(id);
        if(canvas != null)
        {
            canvas.setTransitionTime(seconds);
            return true;
        }
        return false;
    }

    public boolean setInteractionMode(int id, int mode)
    {
        TnMapCanvas canvas = viewIdToCanvas(id);
        if(canvas != null)
        {
            // Check to make sure we know about this canvas
            if(canvases.contains(canvas))
            {
                canvas.setInteractionMode(mode);
                return true;
            }
        }
        return false;
    }

    public int getInteractionMode(int id)
    {
        TnMapCanvas canvas = viewIdToCanvas(id);
        if(canvas != null)
        {
            return canvas.getInteractionMode();
        }
        return IMapEngine.INTERACTION_MODE_INVALID;
    }

    public boolean setRenderingMode(int viewId, int mode)
    {
        TnMapCanvas canvas = viewIdToCanvas(viewId);
        if(canvas != null)
        {
            canvas.setRenderingMode(mode);
            return true;
        }
        return false;
    }

    public int getRenderingMode(int viewId)
    {
        TnMapCanvas canvas = viewIdToCanvas(viewId);
        if(canvas != null)
        {
            return canvas.getRenderingMode();
        }
        return IMapEngine.RENDERING_MODE_INVALID;
    }

    public boolean lookAt(int viewId, double latitude, double longitude, float heading)
    {
        TnMapCanvas canvas = viewIdToCanvas(viewId);
        if(canvas != null)
        {
            canvas.lookAt(latitude, longitude, heading);
            return true;
        }
        return false;
    }

    public float getZoomLevel(int viewId)
    {
        TnMapCanvas canvas = viewIdToCanvas(viewId);
        if(canvas != null)
        {
            return canvas.getZoom();
        }

        return 0.0f;
    }

    public boolean beginSmoothZoomIn(int viewId)
    {
        TnMapCanvas canvas = viewIdToCanvas(viewId);
        if(canvas != null)
        {
            canvas.beginSmoothZoomIn();
            return true;
        }
        return false;
    }

    public boolean beginSmoothZoomOut(int viewId)
    {
        TnMapCanvas canvas = viewIdToCanvas(viewId);
        if(canvas != null)
        {
            canvas.beginSmoothZoomOut();
            return true;
        }
        return false;
    }

    public boolean endSmoothZoom(int viewId)
    {
        TnMapCanvas canvas = viewIdToCanvas(viewId);
        if(canvas != null)
        {
            canvas.endSmoothZoom();
            return true;
        }
        return false;

    }

    public boolean showRegion(int viewId,
            double northLat, 
            double westLon, 
            double southLat, 
            double eastLon)
    {
        TnMapCanvas canvas = viewIdToCanvas(viewId);
        if(canvas != null)
        {
            return canvas.showRegion(northLat, westLon, southLat, eastLon);
        }
        return false;
    }

    public Hashtable render()
    {
        Hashtable states = new Hashtable();

        // Iterate through and render each canvas
        for (int i=0; i<canvases.size(); i++)
        {
            TnMapCanvas canvas = (TnMapCanvas)canvases.elementAt(i);
            int id = canvasToViewId(canvas);
            EngineState es = new EngineState();
            canvas.render();

            canvas.getCanvasState(es);
            states.put(new Integer(id), es);
        }

        return states;
    }

    public boolean update(float dt)
    {    
        if(dt > 0)
            currentTimeSlice += dt;

        //float const updateSize = 1.0f / float(m_targetUpdateRate);
        boolean didUpdate = false;

        //while(m_currentTimeSlice >= updateSize)
        {
            didUpdate = true;
            //if(m_currentTimeSlice < updateSize * 2)
            {
                updateAll(currentTimeSlice); //Update exact time difference
                currentTimeSlice  = 0.0f;
            }
            //else if(m_currentTimeSlice < 1.0f)
            //{
            //    m_currentTimeSlice -= updateSize;
            //    UpdateAll(updateSize);       //Update in chunks of updateSize to catch up
            //}
            //else                                        //It would take too long to jump to desired update point
            //{
            //    m_currentTimeSlice =  0.0f;
            //    UpdateAll(0.0f);
            //}
        }

        if(forceRedraw && !didUpdate)
        {
            currentTimeSlice = 0;
            updateAll(0.0f);
            didUpdate = true;
        }

        return didUpdate;
    }

    public void updateAll(float dt)
    {
        // Iterate through and render each canvas
        for (int i=0; i<canvases.size(); i++)
        {
            TnMapCanvas canvas = (TnMapCanvas)canvases.elementAt(i);
            canvas.update(dt);
        }
    }

    public Hashtable updateAndRender(float dt)
    {
        if(update(dt))
            return render();
        return new Hashtable();
    }

    public Hashtable updateAndRenderAndBuild(float dt)
    {
        if(update(dt))
            return render();
        return new Hashtable();
    }

    public void release()
    {
        canvases.removeAllElements();
    }

    public void setRoute(RouteWrapper  route)
    {
        // Pass route to each canvas
        // Iterate through and render each canvas
        for (int i=0; i<canvases.size(); i++)
        {        
            TnMapCanvas canvas = (TnMapCanvas)canvases.elementAt(i);
            canvas.setRoute(route);
        }
    }

    public void clearRoute()
    {
        // Iterate through and render each canvas
        for (int i=0; i<canvases.size(); i++)
        {        
            TnMapCanvas canvas = (TnMapCanvas)canvases.elementAt(i);
            canvas.clearRoute();
        }
    }

    public void setVehiclePosition(double latitude, double longitude, float heading, int meterAccuracy)
    {
        // Iterate through and set vehicle position for each canvas
        // Clear route on each canvas
        // Iterate through and render each canvas
        for (int i=0; i<canvases.size(); i++)
        {        
            TnMapCanvas canvas = (TnMapCanvas)canvases.elementAt(i);
            canvas.setVehiclePosition(latitude, longitude, 0.0, heading, meterAccuracy);
        }
    }
    
    public int addAnnotationGraphics(String source)
    {
        TnMapTexture texture = textureLoader.syncLoad(source);
        if (texture == null || !texture.isLoaded())
            return 0;
        
        int graphicId = ++graphic_id_counter;
        m_buildManager.getAnnotationBuilder().addGraphic(graphicId, texture);
        return graphicId;
    }
    
    public int addAnnotationGraphics(AbstractTnImage img)
    {
        TnMapTexture texture = textureLoader.syncLoad(img, TnGL10.GL_RGBA);
        if (texture == null || !texture.isLoaded())
            return 0;
        
        int graphicId = ++graphic_id_counter;
        m_buildManager.getAnnotationBuilder().addGraphic(graphicId, texture);
        return graphicId;
    }
    
    public boolean removeAnnotationGraphic(int id)
    {
        // @TODO: One annotation graphic shared among all canvases
        // Iterate through and render each canvas
        return m_buildManager.getAnnotationBuilder().removeGraphic(id);
    }

    public int addSpriteAnnotation(int viewId, 
            int layer, 
            int graphicId, 
            double latitude, 
            double longitude, 
            double height, 
            int pixelWidth, int pixelHeight,
            int pivotPointX, int pivotPointY)

    {
        double[] g = new double[]{longitude, latitude, height};
        TnMapMagic.latLonToGlobal(g);

        // @TODO: One annotation graphic shared among all canvases
        TnMapCanvas canvas = viewIdToCanvas(viewId);
        if(canvas != null)
        {
            return canvas.addSpriteAnnotation(layer, graphicId,
                    g,
                    pixelWidth, pixelHeight,
                    pivotPointX, pivotPointY);
        }

        // @TODO: Proper error handling
        return 0;
    }

    public int addBillboardAnnotation(int viewId, int layer, int graphicId, 
            double latitude, double longitude, double height,
            int pixelWidth, int pixelHeight, 
            int pivotPointX, int pivotPointY)

    {
        // @TODO: One annotation graphic shared among all canvases
        double[] pos = new double[]{longitude, latitude, height};
        TnMapMagic.latLonToGlobal(pos);

        TnMapCanvas canvas = viewIdToCanvas(viewId);
        if(canvas != null)
        {
            return canvas.addBillboardAnnotation(layer, graphicId,
                    pos,
                    pixelWidth, pixelHeight, 
                    pivotPointX, pivotPointY);
        }

        // @TODO: Proper error handling
        return 0;
    }

    public int addFixedAnnotation(int viewId, int layer, int graphicId, double degreesLatitude, double degreesLongitude, double height,
            int pixelWidth, int pixelHeight, int pivotPointX, int pivotPointY, float[] faceVector, float[] upVector)
    {
        double[] pos = new double[]{degreesLongitude, degreesLatitude, height};
        TnMapMagic.latLonToGlobal(pos);

        TnMapCanvas canvas = viewIdToCanvas(viewId);
        if(canvas != null)
        {
            return canvas.addFixedAnnotation(layer, graphicId,
                    pos, pixelWidth, pixelHeight, 
                    pivotPointX, pivotPointY, faceVector, upVector);
        }
        
        return 0;
    }


    public int add2DAnnotation(int viewId, int layer, int graphicId,
            int x, int y, 
            int pixelWidth, int pixelHeight)
    {
        // @TODO: One annotation graphic shared among all canvases

        TnMapCanvas canvas = viewIdToCanvas(viewId);
        if(canvas != null)
        {
            return canvas.add2DAnnotation(layer,x,y,graphicId,pixelWidth,pixelHeight);
        }

        // @TODO: Proper error handling
        return 0;
    }


    public boolean reposition2DAnnotation(int annotationId,
            int x, 
            int y, 
            int pixelWidth, 
            int pixelHeight)
    {
        boolean found = false;
        // Iterate through and render each canvas
        for (int i=0; i<canvases.size(); i++)
        {
            TnMapCanvas canvas = (TnMapCanvas)canvases.elementAt(i);
            found = found | canvas.reposition2DAnnotation(annotationId, x, y, pixelWidth, pixelHeight);
        }
        return found;
    }

    public boolean removeAnnotation(int viewId, int annotationId)
    {
        TnMapCanvas canvas = viewIdToCanvas(viewId);
        if (canvas != null)
        {
            return canvas.removeAnnotation(annotationId);
        }
        return false;
    }

    public void clearAnnotationLayer(int  viewID , int  layer )
    {
        // @TODO: Implement ClearAnnotationLayer

    }

    public AnnotationSearchResult[] getNearestAnnotations(int viewId, int x, int y)
    {
        return getNearestAnnotations(viewId, x, y, true);
    }

    public AnnotationSearchResult[] getNearestAnnotations(int viewId, int x, int y, boolean onlyEnabledAnnotations)
    {
        // @TODO: One annotation graphic shared among all canvases
        TnMapCanvas canvas = viewIdToCanvas(viewId);

        if (canvas != null)
        {
            return canvas.getNearestAnnotations(x, y, onlyEnabledAnnotations);
        }

        return null;
    }


    public Hashtable getNearest(int viewId, int x, int y, boolean onlyEnabled)
    {
        // @TODO: One annotation graphic shared among all canvases
        TnMapCanvas canvas = viewIdToCanvas(viewId);

        if (canvas != null)
        {
            return canvas.getNearest(x, y, onlyEnabled);
        }

        return new Hashtable();
    }


    public boolean setZoomLevel(int viewId, float zoomLevel)
    {
        //std::cerr << "SetZoomLevel(" << zoomLevel << ")" << std::endl;

        TnMapCanvas canvas = viewIdToCanvas(viewId);

        if (canvas != null)
        {
            canvas.setZoom(zoomLevel);
            return true;
        }

        return false;
    }


    public boolean setZoomLevel(int viewId, float zoomLevel, int x, int y)
    {

        TnMapCanvas canvas = viewIdToCanvas(viewId);

        if (canvas != null)
        {
            canvas.setZoom(zoomLevel, x, y);
            return true;
        }

        return false;
    }



    public boolean enableAnnotation(int annotationId)
    {
        return TnMapCanvas.enableAnnotation(annotationId);
    }

    public boolean disableAnnotation(int annotationId)
    {
        return TnMapCanvas.disableAnnotation(annotationId);
    }

    public boolean enableTurnArrow(int viewId, int segment)
    {
        TnMapCanvas canvas = viewIdToCanvas(viewId);

        if (canvas != null)
        {
            return canvas.enableTurnArrow(segment);
        }

        return false;
    }

    public boolean enableAllTurnArrows(int viewId)
    {
        TnMapCanvas canvas = viewIdToCanvas(viewId);

        if (canvas != null)
        {
            return canvas.enableAllTurnArrows();
        }

        return false;
    }

    public boolean disableTurnArrow(int viewId, int segment)
    {
        TnMapCanvas canvas = viewIdToCanvas(viewId);

        if (canvas != null)
        {
            return canvas.disableTurnArrow(segment);
        }

        return false;
    }

    public boolean disableAllTurnArrows(int viewId)
    {
        TnMapCanvas canvas = viewIdToCanvas(viewId);

        if (canvas != null)
        {
            return canvas.disableAllTurnArrows();
        }

        return false;
    }
}
