/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * TnMapES1Render.java
 *
 */
package com.telenav.map.opengl.java.es10render;

import java.util.Vector;

import com.telenav.map.IMapEngine;
import com.telenav.map.opengl.java.AbstractTnMapESRender;
import com.telenav.map.opengl.java.TnMapCamera;
import com.telenav.map.opengl.java.TnMapCameraManager;
import com.telenav.map.opengl.java.TnMapConf;
import com.telenav.map.opengl.java.TnMapRoute;
import com.telenav.map.opengl.java.TnMapScale;
import com.telenav.map.opengl.java.TnMapSkyDome;
import com.telenav.map.opengl.java.TnMapTexture;
import com.telenav.map.opengl.java.TnMapTextureLoader;
import com.telenav.map.opengl.java.TnMapVehicle;
import com.telenav.map.opengl.java.TnMap.TnMapColor;
import com.telenav.map.opengl.java.TnMap.TnMapRect;
import com.telenav.map.opengl.java.math.Matrixf;
import com.telenav.tnui.opengles.TnGL10;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2010-10-1
 */
public class TnMapES10Render extends AbstractTnMapESRender
{
    TnMapES10RoadRender m_pRoadRender;

    TnMapES10PolygonRender m_pPolygonRender;

    TnMapES10MarkRender m_pMarkRender;

    TnMapES10TextFlagRender m_pTextFlagRender;

    TnMapES10VehicleRenderer m_pVehicleRenderer;

    TnMapES10RouteRender m_pRouteRender;

    TnMapES10AnnotationRender m_pAnnotationRender;

    TnMapES10TrafficRender m_pTrafficRender;

    TnMapES10OnewayArrowRender m_pOnewayArrowRender;

    TnMapES10IncidentRender m_pIncidentRender;

    TnMapES10ScaleRender m_scaleRender;

    TnMapES10SkyDomeRender m_pSkyDomeRender;

    boolean m_bTraffic;

    TnGL10 gl10;

    public TnMapES10Render(TnGL10 gl10, TnMapTextureLoader loader)
    {
        this.gl10 = gl10;
        m_pRoadRender = new TnMapES10RoadRender(gl10, loader);
        m_pPolygonRender = new TnMapES10PolygonRender(gl10);
        m_pVehicleRenderer = new TnMapES10VehicleRenderer(gl10, loader);
        m_pRouteRender = new TnMapES10RouteRender(gl10, loader);
        m_pAnnotationRender = new TnMapES10AnnotationRender(gl10);
        m_pTrafficRender = new TnMapES10TrafficRender(gl10, loader);
        m_pOnewayArrowRender = new TnMapES10OnewayArrowRender(gl10, loader);
        m_pIncidentRender = new TnMapES10IncidentRender(gl10, loader);
        m_pSkyDomeRender = new TnMapES10SkyDomeRender(gl10);
        m_bTraffic = false;

        TnMapTexture texture = loader.syncLoad("ArialBlack");
        m_pMarkRender = new TnMapES10MarkRender(gl10, texture);
        m_scaleRender = new TnMapES10ScaleRender(gl10, loader, texture);
        m_pTextFlagRender = new TnMapES10TextFlagRender(gl10, loader, texture);
    }

    public void enableTraffic(boolean bTraffic)
    {
        m_bTraffic = bTraffic;
    }

    public void render(Vector vecMapTiles, Vector vecTrafficTiles, TnMapCameraManager cameraManager, 
            Vector layers2D, Vector layers3D, TnMapRect screenRect,
            TnMapVehicle vehicle, TnMapRoute route, TnMapScale mapScale, TnMapSkyDome skyDome, float constantSizeFactor,
            boolean useLameSky, float lameSkyAmount)
    {
        // log_addString("TnMapES1Renderer----------before Render()");
        // TnMapCheckGL("TnMapES1Render::Render start");

        TnMapCamera camera = cameraManager.getCurrentCamera();
        Matrixf projectionMatrix = camera.GetProjectionMatrix();

        float declination = cameraManager.getTargetDeclination(IMapEngine.RENDERING_MODE_3D);
        Matrixf skyMatrix = useLameSky ? camera.GetLameSkyMatrix(lameSkyAmount, declination) : camera.GetSkyMatrix();

        TnMapColor background = TnMapConf.getBackgroundColor();
        float r = background.r / 255.0f;
        float g = background.g / 255.0f;
        float b = background.b / 255.0f;
        float a = background.a / 255.0f;

        gl10.glViewport(screenRect.x, screenRect.y, screenRect.dx, screenRect.dy);
        gl10.glScissor(screenRect.x, screenRect.y, screenRect.dx, screenRect.dy);

        // Set up default OpenGL state
        gl10.glHint(TnGL10.GL_PERSPECTIVE_CORRECTION_HINT, TnGL10.GL_NICEST);
        gl10.glEnable(TnGL10.GL_SCISSOR_TEST);
        gl10.glEnable(TnGL10.GL_TEXTURE_2D);
        gl10.glEnable(TnGL10.GL_BLEND);
        gl10.glDisable(TnGL10.GL_DEPTH_TEST);
        gl10.glDisable(TnGL10.GL_CULL_FACE);
        gl10.glBlendFunc(TnGL10.GL_SRC_ALPHA, TnGL10.GL_ONE_MINUS_SRC_ALPHA);
        gl10.glTexEnvf(TnGL10.GL_TEXTURE_ENV, TnGL10.GL_TEXTURE_ENV_MODE, TnGL10.GL_MODULATE);
        gl10.glClearColor(r, g, b, a);
        gl10.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        gl10.glEnableClientState(TnGL10.GL_VERTEX_ARRAY);
        gl10.glEnableClientState(TnGL10.GL_TEXTURE_COORD_ARRAY);
        gl10.glEnableClientState(TnGL10.GL_COLOR_ARRAY);

        // Clear
        gl10.glClear(TnGL10.GL_COLOR_BUFFER_BIT | TnGL10.GL_DEPTH_BUFFER_BIT);

        gl10.glMatrixMode(TnGL10.GL_PROJECTION);
        float[] matrixArray = projectionMatrix.getArray();
        gl10.glLoadMatrixf(matrixArray, 0);
        gl10.glMatrixMode(TnGL10.GL_MODELVIEW);

        // log_addString("TnMapES1Renderer----------RenderTiles----------before Render()");
        renderTiles(vecMapTiles, vecTrafficTiles, camera);
        m_pRouteRender.render(route, camera);
        renderText2D(vecMapTiles, camera);
        m_pRouteRender.renderArrows(route, camera);

        if (m_bTraffic)
        {
            m_pIncidentRender.render(vecTrafficTiles, camera);
        }
        renderText3D(vecMapTiles, cameraManager, camera);

        m_pVehicleRenderer.render(vehicle, camera, constantSizeFactor);
        m_pAnnotationRender.render3D(layers3D, camera, constantSizeFactor);

        m_pSkyDomeRender.render(skyDome, skyMatrix);

        // Render 2D Annotations
        gl10.glMatrixMode(TnGL10.GL_PROJECTION);
        gl10.glLoadIdentity();
        gl10.glMatrixMode(TnGL10.GL_MODELVIEW);

        Matrixf ortho = camera.GetAnnotation2DMatrix();

        matrixArray = ortho.getArray();
        gl10.glLoadMatrixf(matrixArray, 0);
        m_pAnnotationRender.render2D(layers2D);
//        if(mapScale)
//        {
//            float y = camera.GetScreenHeight();
//            float x = camera.GetScreenWidth() - mapScale->GetWidth();
//            Matrixf barMat = ortho * Matrixf(Vec3<float>(x, y, 0.0f));
//            glLoadMatrixf(&barMat[0][0]);
//            log_addString("TnMapES1Renderer----------mapScale----------before Render()");
//            m_scaleRender->Render(mapScale, camera);
//            log_addString("TnMapES1Renderer----------mapScale----------end Render()");
//        }
//        log_addString("TnMapES1Renderer----------end Render()");
    }

    public void renderTiles(Vector vecMapTiles, Vector vecTrafficTiles, TnMapCamera camera)
    {

        // TnMapCheckGL("TnMapES1Render::RenderTiles polygon");
        m_pPolygonRender.render(vecMapTiles);

        // TnMapCheckGL("TnMapES1Render::RenderTiles nonfreeways");

        m_pRoadRender.renderNonFreeways(vecMapTiles);
        if (m_bTraffic)
        {
            // TnMapCheckGL("TnMapES1Render::RenderTiles traffic");
            m_pTrafficRender.render(vecTrafficTiles);
        }
        // TnMapCheckGL("TnMapES1Render::RenderTiles freeways");
        m_pRoadRender.renderFreeways(vecMapTiles);

        m_pOnewayArrowRender.render(vecMapTiles);
        // TnMapCheckGL("TnMapES1Render::RenderTiles end");
    }

    public void renderText2D(Vector vecMapTiles, TnMapCamera camera)
    {

        // Render marks and text
        m_pMarkRender.render(vecMapTiles, camera);
    }

    public void renderText3D(Vector vecMapTiles, TnMapCameraManager cameraManager, TnMapCamera camera)
    {
        // Render marks and text
        m_pTextFlagRender.render(vecMapTiles, cameraManager, camera);
    }

}
