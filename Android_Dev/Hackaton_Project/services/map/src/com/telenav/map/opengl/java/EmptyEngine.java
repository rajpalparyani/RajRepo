package com.telenav.map.opengl.java;

import com.telenav.datatypes.map.MapTile;
import com.telenav.datatypes.route.RouteWrapper;
import com.telenav.map.AbstractMapEngine;
import com.telenav.map.IMapEngine;
import com.telenav.map.IMapView;
import com.telenav.map.ITnMapClientSupport;
import com.telenav.tnui.graphics.AbstractTnImage;
import com.telenav.tnui.opengles.TnGL;

public class EmptyEngine extends AbstractMapEngine
{
    IMapView mapView;
    ITnMapClientSupport clientSupport = new EmptyClientSupport();
    
    public IMapView getMapView() 
    {
        return mapView;
    }

    public void setMapView(IMapView mapView) 
    {
        this.mapView = mapView;        
    }

    public long add2DAnnotation(long viewId, int layer, long graphicId, int x,
            int y, int pixelWidth, int pixelHeight) 
    {
        return 0;
    }

    public long addAnnotationGraphic(String source) 
    {
        return 0;
    }

    public long addAnnotationGraphic(byte[] bytes, int width, int height) 
    {
        return 0;
    }

    public long addAnnotationGraphics(AbstractTnImage img) 
    {
        return 0;
    }

    public long addBillboardAnnotation(long viewId, int layer, long graphicId,
            double degreesLatitude, double degreesLongitude, double height,
            int pixelWidth, int pixelHeight, int pivotPointX, int pivotPointY) 
    {
        return 0;
    }

    public boolean addConfig(long confId, String source) 
    {
        return false;
    }

    public boolean addConfig(long confId, byte[] bytes) 
    {
        return false;
    }

    public long addFixedAnnotation(long viewId, int layer, long graphicId,
            double degreesLatitude, double degreesLongitude, double height,
            int pixelWidth, int pixelHeight, int pivotPointX, int pivotPointY,
            float[] faceVector, float[] upVector) 
    {
        return 0;
    }

    public long addModel(String source) 
    {
        return 0;
    }

    public long addModelAnnotation(long viewId, int layer, long graphicId,
            double degreesLatitude, double degreesLongitude, double height)
    {
        return 0;
    }

    public long addSpriteAnnotation(long viewId, int layer, long graphicId,
            double degreesLatitude, double degreesLongitude, double height,
            int pixelWidth, int pixelHeight, int pivotPointX, int pivotPointY) 
    {
        return 0;
    }

    public void build(float deltaSeconds) 
    {
        
    }

    public void clearAnnotationLayer(long viewId, int layer) 
    {
    }

    public long createView(long confId, int x, int y, int width, int height, float dpi, float view_scale_factor) 
    {
        return 0;
    }

    public boolean deleteView(long viewId) 
    {
        return false;
    }

    public boolean disableAllTurnArrows(long viewId) 
    {
        return false;
    }

    public boolean disableAnnotation(long annotationId) 
    {
        return false;
    }

    public boolean disableAnnotationLayer(long viewId, int layer) 
    {
        return false;
    }

    public boolean disableTurnArrow(long viewId, int segment) 
    {
        return false;
    }

    public boolean enableAllTurnArrows(long viewId) 
    {
        return false;
    }

    public boolean enableAnnotation(long annotationId) 
    {
        return false;
    }

    public boolean enableAnnotationLayer(long viewId, int layer) 
    {
        return false;
    }

    public boolean enableTurnArrow(long viewId, int segment) 
    {
        return false;
    }

    public boolean getBool(long viewId, int param) 
    {
        return false;
    }

    public ITnMapClientSupport getClientSupport() 
    {
        return clientSupport;
    }

    public long getDefaultConfig() 
    {
        return 0;
    }

    public double getDouble(long viewId, int param) 
    {
        return 0;
    }

    public int getInteractionMode(long viewId) 
    {
        return 0;
    }

    public AnnotationSearchResult[] getNearestAnnotations(long viewId, int x,
            int y, boolean onlyEnabledAnnotations) 
    {
        return null;
    }

    public int getOrientation(long viewId) 
    {
        return 0;
    }

    public int getRenderingMode(long viewId) 
    {
        return 0;
    }

    public MapTile[] getSurroudingMapTiles(double degreesLatitude,
            double degreesLongitude, int radius) 
    {
        return null;
    }

    public EngineState getViewState(long viewId) 
    {
        return null;
    }

    public float getZoomLevel(long viewId) 
    {
        return 0;
    }

    public boolean handleTouchEvent(long viewId, TouchEvent event) 
    {
        return false;
    }

    public void init(int openGlVersion) 
    {
    }

    public void initCache(long cacheSize, String cachePath)
    {
        
    }
    
    public boolean lookAt(long viewId, double latitude, double longitude,
            float heading) 
    {
        return false;
    }

    public boolean removeAnnotation(long viewId, long annotationId) 
    {
        return false;
    }

    public boolean removeAnnotationGraphic(long id) 
    {
        return false;
    }

    public boolean removeModel(long modelId) 
    {
        return false;
    }

    public void render(float deltaSeconds) 
    {
    }

    public boolean resizeView(long viewId, int x, int y, int width, int height) 
    {
        return false;
    }

    public boolean setBool(long viewId, int param, boolean value) 
    {
        return false;
    }

    public boolean setDouble(long viewId, int param, double value) 
    {
        return false;
    }

    public void setGl(TnGL gl) 
    {
        
    }

    public boolean setInteractionMode(long viewId, int mode) 
    {
        return false;
    }

    public void setOrientation(long viewId, int orientation) 
    {
    }

    public boolean setRenderingMode(long viewId, int mode) 
    {
        return false;
    }

    public void setRoute(RouteWrapper routeWrapper) 
    {
    }

    public void setRouteColor(int routeId, int color) 
    {
    }

    public boolean setStringList(long viewId, int param, String[] strList) 
    {
        return false;
    }

    public boolean setTransitionTime(long viewId, float seconds) 
    {
        return false;
    }

    public void setVehiclePosition(double degreesLatitude,
            double degreesLongitude, float heading, int meterAccuracy) 
    {
        
    }

    public boolean setZoomLevel(long viewId, float zoomLevel) 
    {
        return false;
    }

    public boolean setZoomLevel(long viewId, float zoomLevel, int x, int y,
            double[] latitude, double[] longitude) 
    {
        return false;
    }

    public boolean showRegion(long viewId, double northLat, double westLon,
            double southLat, double eastLon) 
    {
        return false;
    }

    public boolean showRegionForRoutes(long viewId, String[] routeNames, int x,
            int y, int width, int height) 
    {
        return false;
    }

    public void update(float deltaSeconds) 
    {
    }

    public void updateAndRender(float deltaSeconds) 
    {
    }

    public void updateAndRenderAndBuild(float deltaSeconds) 
    {
    }
    
    public void destroyMapEngine()
    {
        
    }

    public boolean getBinary(long viewId, int param, byte[][] value)
    {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean setBinary(long viewId, int param, byte[] value)
    {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean showRegion(long viewId, double northLat, double westLon, double southLat, double eastLon, int x, int y, int width,
            int height)
    {
        // TODO Auto-generated method stub
        return false;
    }

    public int reorderRoutes(long viewId, String[] routeNames)
    {
        // TODO Auto-generated method stub
        return 0;
    }

    public long getViewId()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    public void setViewId(long viewId)
    {
        // TODO Auto-generated method stub
        
    }

    public long addModel(byte[] data, int size)
    {
        return 0;
    }

    public void startPreloader()
    {
        // TODO Auto-generated method stub
  
    }

    public void stopPreloader()
    {
        // TODO Auto-generated method stub
        
    }

    public long addScreenAnnotation(long viewId, int layer, long graphicID, double degreesLatitude, double degreesLongitude, double metersHeight, String style,
            String text)
    {
        // TODO Auto-generated method stub
        return 0;
    }

    public boolean disableRoute(long viewId, String name)
    {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean enableRoute(long viewId, String name)
    {
        // TODO Auto-generated method stub
        return false;
    }

    
    public void calcRegion(long viewID, double northLat, double westLon, double southLat, double eastLon, int x, int y, int width,
            int height, float[] zoomArray, double[] latArray, double[] lonArray, boolean gridAligned)
    {
        // TODO Auto-generated method stub
        
    }
    
    public TnBitmap getBitmapSnapshot(int xOrigin, int yOrigin, int width, int height)
    {
        // TODO Auto-generated method stub
        return null;
    }

    
    public boolean resizeView(long viewID, int x, int y, int width, int height, float dpi, float viewScaleFactor)
    {
        // TODO Auto-generated method stub
        return false;
    }

    public long createAnnotation(long viewId, String style, TnMapAnnotationParams params, long graphicId)
    {
        // TODO Auto-generated method stub
        return 0;
    }

    public long createAnnotation(long viewId, String style, long graphicId, int annotationType)
    {
        // TODO Auto-generated method stub
        return 0;
    }

    public TnMapAnnotationParams getDefaultAnnotationParams(long viewId, String style, int zoomLevel)
    {
        // TODO Auto-generated method stub
        return null;
    }

    public TnMapAnnotationParams getDefaultAnnotationParams(long viewId, String style)
    {
        // TODO Auto-generated method stub
        return null;
    }

    public long createAnnotation(long viewId, String style, TnMapAnnotationParams params,
            long graphicId, long pickableIdNumber)
    {
        return 0;
    }

    public void notify(int event)
    {
        // TODO Auto-generated method stub
        
    }

    public void setAllTopicLevels(int logLevel)
    {
        // TODO Auto-generated method stub
        
    }

    public String[] getLogTopics()
    {
        // TODO Auto-generated method stub
        return null;
    }

    public void setTopicLevel(String topic, int logLevel)
    {
        // TODO Auto-generated method stub
        
    }

    public void enableRasterType(long viewId, int type)
    {
        // TODO Auto-generated method stub
        
    }

    public void calcRegion(long viewID, double northLat, double westLon, double southLat,
            double eastLon, int x, int y, int width, int height, float[] zoomArray,
            double[] latArray, double[] lonArray)
    {
        // TODO Auto-generated method stub
        
    }

}
