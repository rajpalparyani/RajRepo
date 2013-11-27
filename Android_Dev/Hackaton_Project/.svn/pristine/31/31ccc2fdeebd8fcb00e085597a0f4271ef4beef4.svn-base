package com.telenav.map.opengl.java;

import java.util.Vector;

import com.telenav.map.opengl.java.TnMap.TnMapRect;

public abstract class AbstractTnMapESRender
{
    abstract public void enableTraffic(boolean bTraffic);

    abstract public void render(Vector vecMapTiles, Vector vecTrafficTiles, TnMapCameraManager cameraManager, 
            Vector layers2D, Vector layers3D, TnMapRect screenRect,
            TnMapVehicle vehicle, TnMapRoute route, TnMapScale mapScale, 
            TnMapSkyDome skyDome, float constantSizeFactor,
            boolean useLameSky, float lameSkyAmount);

    abstract public void renderTiles(Vector vecMapTiles, Vector vecTrafficTiles, TnMapCamera camera);

    abstract public void renderText2D(Vector vecMapTiles, TnMapCamera camera);

    abstract public void renderText3D(Vector vecMapTiles, TnMapCameraManager cameraManager, 
            TnMapCamera camera);

}
