package com.telenav.map.opengl.java;

import java.util.Vector;

import com.telenav.datatypes.map.MapTile;
import com.telenav.datatypes.route.RouteWrapper;
import com.telenav.map.AbstractMapEngine;
import com.telenav.map.IMapEngine;
import com.telenav.map.IMapView;
import com.telenav.map.ITnMapClientSupport;
import com.telenav.map.opengl.java.proxy.AbstractTileProvider;
import com.telenav.map.opengl.java.proxy.ITnMapProxyFactory;
import com.telenav.map.opengl.java.proxy.TnMapProxyFactory;
import com.telenav.tnui.graphics.AbstractTnImage;
import com.telenav.tnui.opengles.TnGL;
import com.telenav.tnui.opengles.TnGL10;

public class JavaGLMapEngine extends AbstractMapEngine
{
    protected TnMapEngine mapEngineImpl;
    
    protected IMapView mapView;
    
    protected AbstractTileProvider tileProvider;
    
    public JavaGLMapEngine(AbstractTileProvider tileProvider)
    {
        this.tileProvider = tileProvider;
    }
    
    public void init(int openGlVersion)
    {
        //do nothing
    }
    
    public void initCache(long cacheSize, String cachePath)
    {
        
    }
    
    public void setGl(TnGL gl)
    {
        if (this.mapEngineImpl == null)
        {
            ITnMapProxyFactory proxyFactory = new TnMapProxyFactory();
    
            // log_addString("after proxFactory()\n");
            this.mapEngineImpl = TnMapEngine.createMapEngine((TnGL10)gl, proxyFactory.getResourceProxy(), tileProvider);
        }
    }
    
    public long add2DAnnotation(long viewId, int layer, long graphicId, 
            int x, int y, int pixelWidth, int pixelHeight)
    {
        return mapEngineImpl.add2DAnnotation((int)viewId, layer, (int)graphicId, x, y, pixelWidth, pixelHeight);
    }

    public long addAnnotationGraphic(String source) 
    {
        return mapEngineImpl.addAnnotationGraphics(source);
    }

    public long addAnnotationGraphics(AbstractTnImage img)
    {
        return mapEngineImpl.addAnnotationGraphics(img);
    }
    
    public long addAnnotationGraphic(byte[] bytes, int width, int height)
    {
        throw new RuntimeException("Unsupported API");
    }

    public long addBillboardAnnotation(long viewId, int layer, long graphicId, double degreesLatitude, double degreesLongitude, double height,
            int pixelWidth, int pixelHeight, int pivotPointX, int pivotPointY)
    {
        return mapEngineImpl.addBillboardAnnotation((int)viewId, layer,
                (int)graphicId, degreesLatitude, degreesLongitude, height, pixelWidth, 
                pixelHeight, pivotPointX, pivotPointY);
    }

    public long addFixedAnnotation(long viewId, int layer, long graphicId, double degreesLatitude, double degreesLongitude, double height,
            int pixelWidth, int pixelHeight, int pivotPointX, int pivotPointY, float[] faceVector, float[] upVector)
    {
        return mapEngineImpl.addFixedAnnotation((int)viewId, layer, (int)graphicId, degreesLatitude, degreesLongitude,
                height, pixelWidth, pixelHeight, pivotPointX, pivotPointY, faceVector, upVector);
    }

    public long addSpriteAnnotation(long viewId, int layer, long graphicId, double degreesLatitude, double degreesLongitude, double height,
            int pixelWidth, int pixelHeight, int pivotPointX, int pivotPointY)
    {
        return mapEngineImpl.addSpriteAnnotation((int)viewId, layer, (int)graphicId, 
                degreesLatitude, degreesLongitude, height, pixelWidth, pixelHeight, pivotPointX, pivotPointY);
    }

    public void build(float deltaSeconds)
    {
        
    }

    public void clearAnnotationLayer(long viewId, int layer)
    {
        mapEngineImpl.clearAnnotationLayer((int)viewId, layer);
    }

    public long createMapEngine(int renderingAPI)
    {
        //we only support one map engine right now
        return 1;
    }

    public long createView(int x, int y, int width, int height)
    {
        return mapEngineImpl.createView(x, y, width, height);
    }

    public boolean deleteView(long viewId)
    {
        return mapEngineImpl.deleteView((int)viewId);
    }

    public void destroyMapEngine(long engineId)
    {
        mapEngineImpl.destroy();
        mapEngineImpl = null;
    }

    public boolean disableAllTurnArrows(long viewId)
    {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean disableAnnotation(long annotationId)
    {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean disableTurnArrow(long viewId, int segment)
    {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean enableAllTurnArrows(long viewId)
    {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean enableAnnotation(long annotationId)
    {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean enableTurnArrow(long viewId, int segment)
    {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean getBool(long viewId, int param)
    {
        boolean[] value = new boolean[1];
        mapEngineImpl.getBool((int)viewId, param, value);
        return value[0];
    }

    public double getDouble(long viewId, int param)
    {
        double[] value = new double[1];
        mapEngineImpl.getDouble((int)viewId, param, value);
        return value[0];
    }

    public int getInt(long viewId, int param)
    {
        int[] value = new int[1];
        mapEngineImpl.getInt((int)viewId, param, value);
        return value[0];
    }

    public int getInteractionMode(long viewId)
    {
        return mapEngineImpl.getInteractionMode((int)viewId);
    }

    public IMapView getMapView()
    {
        return mapView;
    }


    public IMapEngine.AnnotationSearchResult[] getNearestAnnotations(long viewId, int x, int y, boolean onlyEnabledAnnotations)

    {
        return mapEngineImpl.getNearestAnnotations((int)viewId, x, y, onlyEnabledAnnotations);
    }

    public int getOrientation(long viewId)
    {
        // TODO Auto-generated method stub
        return 0;
    }

    public int getRenderingMode(long viewId)
    {
        return mapEngineImpl.getRenderingMode((int)viewId);
    }

    public MapTile[] getSurroudingMapTiles(double degreesLatitude, double degreesLongitude, int radius)
    {
        // TODO Auto-generated method stub
        return null;
    }

    public float getZoomLevel(long viewId)
    {
        return mapEngineImpl.getZoomLevel((int)viewId);
    }

    public boolean handleTouchEvent(long viewId, TouchEvent event)
    {
        return mapEngineImpl.handleTouchEvent((int)viewId, event.event, event.x, event.y, event.pinch);
    }

    public boolean lookAt(long viewId, double latitude, double longitude, float heading)
    {
        return mapEngineImpl.lookAt((int)viewId, latitude, longitude, heading);
    }

    public boolean removeAnnotation(long viewId, long annotationId)
    {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean removeAnnotationGraphic(long id)
    {
        // TODO Auto-generated method stub
        return false;
    }

    public void render(float deltaSeconds)
    {
        mapEngineImpl.render();
    }

    public boolean resizeView(long viewId, int x, int y, int width, int height)
    {
        return mapEngineImpl.resizeView((int)viewId, x, y, width, height);
    }

    public boolean setBool(long viewId, int param, boolean value)
    {
        return mapEngineImpl.setBool((int)viewId, param, value);
    }

    public boolean setDouble(long viewId, int param, double value)
    {
        return mapEngineImpl.setDouble((int)viewId, param, value);
    }

    public boolean setInt(long viewId, int param, int value)
    {
        return mapEngineImpl.setInt((int)viewId, param, value);
    }

    public boolean setInteractionMode(long viewId, int mode)
    {
        if (listener != null)
        {
            listener.interactionModeChanged(mode);
        }
        return mapEngineImpl.setInteractionMode((int)viewId, mode);
    }

    public void setMapView(IMapView mapView)
    {
        this.mapView = mapView;
    }

    public void setOrientation(long viewId, int orientation)
    {
        // TODO Auto-generated method stub

    }

    public boolean setRenderingMode(long viewId, int mode)
    {
        return mapEngineImpl.setRenderingMode((int)viewId, mode);
    }

    public boolean setStringList(long viewId, int param, Vector strList)
    {
        return false;
    }

    public boolean setTransitionTime(long viewId, float seconds)
    {
        return mapEngineImpl.setTransitionTime((int)viewId, seconds);
    }

    public void setVehiclePosition(double degreesLatitude, double degreesLongitude, float heading, int meterAccuracy)
    {
        mapEngineImpl.setVehiclePosition(degreesLatitude, degreesLongitude, heading, meterAccuracy);
    }

    public boolean setZoomLevel(long viewId, float zoomLevel)
    {
        return mapEngineImpl.setZoomLevel((int)viewId, zoomLevel);
    }

    public boolean setZoomLevel(long viewId, float zoomLevel, int x, int y, double[] latitude, double[] longitude)
    {
        return mapEngineImpl.setZoomLevel((int)viewId, zoomLevel, x, y);
    }

    public boolean showRegion(long viewId, double northLat, double westLon, double southLat, double eastLon)
    {
        return mapEngineImpl.showRegion((int)viewId, northLat, westLon, southLat, eastLon);
    }

    public void update(float deltaSeconds)
    {
         mapEngineImpl.update(deltaSeconds);
    }

    public void updateAndRender(float deltaSeconds)
    {
        mapEngineImpl.updateAndRender(deltaSeconds);
    }

    public void updateAndRenderAndBuild(float deltaSeconds)
    {
        mapEngineImpl.updateAndRenderAndBuild(deltaSeconds);
    }

    public void setRoute(RouteWrapper route)
    {
        mapEngineImpl.setRoute(route);
    }
    
    public void setRouteColor(int routeId, int color)
    {
        
    }

    public void notify(int event) {
	        // TODO Auto-generated method stub
	        
	    }

	public IMapEngine.EngineState getViewState(long viewId) {
		// TODO Auto-generated method stub
		return null;
	}

	public void init(int openGlVersion, TnGL10 gl) {
		// TODO Auto-generated method stub
		
	}

	public boolean addConfig(long confId, String source) {
		// TODO Auto-generated method stub
		return false;
	}


	public boolean addConfig(long confId, byte[] bytes) {
		// TODO Auto-generated method stub
		return false;
	}


	public long getDefaultConfig() {
		// TODO Auto-generated method stub
		return 0;
	}


	public long createView(long confId, int x, int y, int width, int height, float dpi, float view_scale_factor) {
		// TODO Auto-generated method stub
		return 0;
	}


	public boolean setStringList(long viewId, int param, String[] strList) {
		// TODO Auto-generated method stub
		return false;
	}

	public ITnMapClientSupport getClientSupport() {
		// TODO Auto-generated method stub
		return null;
	}

	
	public boolean disableAnnotationLayer(long viewId, int layer) {
		// TODO Auto-generated method stub
		return false;
	}

	
	public boolean enableAnnotationLayer(long viewId, int layer) {
		// TODO Auto-generated method stub
		return false;
		
	}

	
	public boolean showRegionForRoutes(long viewId, String[] routeNames, int x,
			int y, int width, int height) {
		// TODO Auto-generated method stub
		return false;
	}

	public long addModel(String source) {
		// TODO Auto-generated method stub
		return 0;
	}

	public long addModelAnnotation(long viewId, int layer, long graphicId,
			double degreesLatitude, double degreesLongitude, double height) {
		// TODO Auto-generated method stub
		return 0;
	}

	public boolean removeModel(long modelId) {
		// TODO Auto-generated method stub
		return false;
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
        return 0;
    }

    public long getViewId()
    {
        return 0;
    }

    public void setViewId(long viewId)
    {
        
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

    public long createAnnotation(long viewId, String style, long graphicId,int annotationType)
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
        // TODO Auto-generated method stub
        return 0;
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
