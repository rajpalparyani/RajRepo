/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * TnMapES1VehicleRenderer.java
 *
 */
package com.telenav.map.opengl.java.es10render;

import java.nio.ByteBuffer;

import com.telenav.map.IMapEngine;
import com.telenav.map.opengl.java.TnMapCamera;
import com.telenav.map.opengl.java.TnMapMagic;
import com.telenav.map.opengl.java.TnMapMath;
import com.telenav.map.opengl.java.TnMapTexture;
import com.telenav.map.opengl.java.TnMapTextureLoader;
import com.telenav.map.opengl.java.TnMapVehicle;
import com.telenav.map.opengl.java.TnMap.TnMapVertex5f;
import com.telenav.map.opengl.java.TnMapRenderingData.BufferElement;
import com.telenav.map.opengl.java.math.Matrixf;
import com.telenav.tnui.opengles.TnGL10;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2010-10-9
 */
public class TnMapES10VehicleRenderer
{
    int m_modelType;
    TnMapTexture m_carTexture;
    TnMapTexture m_pedTexture;
    TnMapTexture m_dotTexture;
    TnMapTexture m_radiusTexture;
    TnMapTexture m_adiTexture;
    TnGL10 gl10;

    public TnMapES10VehicleRenderer(TnGL10 gl10, TnMapTextureLoader loader)
    {
        this.gl10 = gl10;
        m_carTexture = loader.syncLoad("UV1024");
        m_pedTexture = loader.syncLoad("nav_pedestrian");
        m_dotTexture = loader.syncLoad("nav_dot");
        m_radiusTexture = loader.syncLoad("nav_circle");
        m_adiTexture = loader.syncLoad("roadtexture_local");
    }

    public void render(TnMapVehicle vehicle,
            TnMapCamera camera, 
            float scale)
    {
        //    TnMapCheckGL("TnMapES1VehicleRender::Render begin");
        if (vehicle != null)
        {
            //Setup State
            gl10.glDisableClientState(TnGL10.GL_COLOR_ARRAY);

            // Vehicle position
            double[] vehiclePos = vehicle.getPosition();

            // Draw ADI line first
            if(vehicle.getADIState() &&
                    m_adiTexture != null &&
                    m_adiTexture.isLoaded())
            {
                m_adiTexture.startUsing();
                gl10.glColor4f(0.75f, 0.2f, 0.1f, 1.0f);

                int zoom = TnMapMagic.zoomSpaceToZoomLevel(camera.getZoom());

                // ADI line drawn relative to vehicle
                Matrixf viewMatrix = camera.GetViewMatrix(vehiclePos);
                gl10.glLoadMatrixf(viewMatrix.getArray(), 0);

                BufferElement buffer = vehicle.getADIGeometry(zoom);
                buffer.pointerBuffer.position(0);
                buffer.texCoordBuffer.position(0);
                gl10.glVertexPointer(3, TnGL10.GL_FLOAT, 0, buffer.pointerBuffer);
                gl10.glTexCoordPointer(2, TnGL10.GL_FLOAT, 0, buffer.texCoordBuffer);

                //Draw
                gl10.glDrawArrays(TnGL10.GL_TRIANGLE_STRIP, 0, buffer.layers[0]);

                gl10.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            }

            Matrixf rotationMatrix = new Matrixf();
            Matrixf scaleMatrix = new Matrixf();
            Matrixf viewMatrix = null;
            Matrixf translationMatrix = new Matrixf();

            TnMapTexture texture = null;
            
            switch(vehicle.getVehicleType())
            {
                case IMapEngine.EVENT_SHOW_CAR_MODEL:
                {
                    texture = m_carTexture;
                    scaleMatrix.buildScale(scale*0.4f);
                    TnMapMath.translate(translationMatrix.m, 0, 0, 0.15f);
                    rotationMatrix.buildRotateZDeg(-vehicle.getHeading() - 180.0f);
                    viewMatrix = camera.GetViewMatrix(vehiclePos);
                    break;
                }
                case IMapEngine.EVENT_SHOW_PEDESTRIAN_MODEL:
                {
                    texture = m_pedTexture;
                    scaleMatrix.buildScale(0.5f);
                    rotationMatrix.buildRotateXDeg(-90.0f);
                    viewMatrix = Matrixf.multiply(camera.GetCameraMatrix(), camera.GetSpriteMatrix(vehiclePos));
                    break;
                }
                case IMapEngine.EVENT_SHOW_DOT_MODEL:
                {
                    texture = m_dotTexture;
                    scaleMatrix.buildScale(0.25f);
                    rotationMatrix.buildRotateXDeg(-90.0f);
                    viewMatrix = Matrixf.multiply(camera.GetCameraMatrix(), camera.GetSpriteMatrix(vehiclePos));
                    break;
                }
                default:
                    break;
            }

            // TODO: Ben says this is bad:
            vehiclePos[2] = scale/25.0f;

            //Draw Accuracy Radius
            float radius = vehicle.getAccuracyRadius();
            if (radius > 0)
            {
                Matrixf tScaleM = new Matrixf();
                tScaleM.buildScale(4 * radius);   //@TODO: Magic Scale Number BAD!
                double[] location = new double[]{vehiclePos[0], vehiclePos[1], 0};
                Matrixf tViewM = camera.GetViewMatrix(location);
                Matrixf output = Matrixf.multiply(tViewM, tScaleM);
                gl10.glLoadMatrixf(output.getArray(), 0);
                
                m_radiusTexture.startUsing();
                
                ByteBuffer vbb = vehicle.getVertexID(true);
                vbb.position(0);
                gl10.glVertexPointer(3, TnGL10.GL_FLOAT, TnMapVertex5f.size(), vbb);
                vbb.position(3 * 4);
                gl10.glTexCoordPointer(2, TnGL10.GL_FLOAT,  TnMapVertex5f.size(), vbb);
                gl10.glDrawArrays(TnGL10.GL_TRIANGLES, 0, vehicle.getVertexCount(true));

                m_radiusTexture.stopUsing();
                
            }

//            if (texture != null && viewMatrix != null)
//            {
//                gl10.glEnable(TnGL10.GL_DEPTH_TEST);
//    
//                texture.startUsing();
//    
//                //Draw Vehicle
//                Matrixf output = Matrixf.multiply(Matrixf.multiply(Matrixf.multiply(viewMatrix, rotationMatrix),
//                        scaleMatrix), translationMatrix);
//                gl10.glLoadMatrixf(output.getArray(), 0);
//                
//                //Set Vertex Data Pointers
//                ByteBuffer vbb = vehicle.getVertexID(false);
//                
//                vbb.position(0);
//                gl10.glVertexPointer(3, TnGL10.GL_FLOAT, TnMapVertex5f.size(), vbb);
//                vbb.position(3 * 4);
//                gl10.glTexCoordPointer(2, TnGL10.GL_FLOAT, TnMapVertex5f.size(), vbb);
//    
//                //Draw
//                gl10.glDrawArrays(TnGL10.GL_TRIANGLES, 0, vehicle.getVertexCount(false));
//    
//                //Shutdown
//                texture.stopUsing();
//            }
            
            
            
            gl10.glDisable(TnGL10.GL_DEPTH_TEST);
            gl10.glEnableClientState(TnGL10.GL_COLOR_ARRAY);
        }

        //    TnMapCheckGL("TnMapES1VehicleRender::Render end");

    }
}
