/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * TnMapCameraManager.java
 *
 */
package com.telenav.map.opengl.java;

import java.util.Hashtable;

import com.telenav.map.IMapEngine;
import com.telenav.map.opengl.java.math.Pe;
import com.telenav.map.opengl.java.math.Ray.Rayf;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2010-9-29
 */
public class TnMapCameraManager
{
    public TnMapCamera         m_currentCamera;
    public TnMapCamera         m_newCamera;
    public TnMapCamera         m_oldCamera;
    public float               m_blendFactor;
    public boolean                m_isBlending;
    public float               m_blendTime;
    public boolean                m_instaBlend;

    public boolean                m_touchDown;

    public float       m_lastTouchPointX, m_lastTouchPointY;
    public  Rayf         m_lastTouchRay;
    public float               m_lastPinch;

    public float               m_smoothZoom;

    public float               m_vehicleHeading;

    public double[]         m_panFrameMomentum;
    public double[]         m_panMomentum;
    public double[]         m_panFriction;
    public float               m_headingFrameMomentum;
    public float               m_headingMomentum;
    public float               m_headingFriction;
    public float               m_declinationFrameMomentum;
    public float               m_declinationMomentum;
    public float               m_declinationFriction;
    public float               m_complexityBias;

    public int      m_interactionMode;
    public int        m_renderingMode;
    public int          m_orientation;

    //Target declination for each mode
    public Hashtable m_targetDeclination = new Hashtable(); //ITnMapEngine::eRenderingMode,float

    public TnMapCamera getCurrentCamera() {
        return m_currentCamera;

    }

    public void setZoomBias(float complexityBias)
    {
        m_complexityBias = complexityBias;
    }

    public int getZoomLevel()
    {
        return TnMapMagic.zoomSpaceToZoomLevel(m_currentCamera.getZoom()+m_complexityBias);
    }

    public float getZoom()
    {
        return m_newCamera.getZoom();
    }

    public double[] getPosition()
    {
        return m_currentCamera.getOrigin();
    }


    public void setZoom(float zoom)
    {
        float clampedZoom = (float)Pe.clamp(zoom,
                TnMapConf.DEFAULT_ZOOM_LOWER_BOUND, 
                TnMapConf.DEFAULT_ZOOM_UPPER_BOUND);
        m_newCamera.SetZoom(clampedZoom);

        startAnimation();
    }


    public void setZoom(float zoom, int x, int y)
    {
        float clampedZoom = (float)Pe.clamp(zoom,
                TnMapConf.DEFAULT_ZOOM_LOWER_BOUND, 
                TnMapConf.DEFAULT_ZOOM_UPPER_BOUND);
        m_newCamera.SetZoom(clampedZoom);

        //Planef ground = new Planef(0.0f, 0.0f, 1.0f, 0.0f);

        Rayf touchRay = new Rayf();
        m_currentCamera.GetTouchRay(x, y, touchRay);
        float[] result = new float[3]; 
        boolean succeeded = Pe.intersect(0, 0, 1, 0, touchRay, result);
        if (succeeded)
        {
            double[] offset = new double[]{result[0],
                    result[1],
                    result[2]};
            double[] origin = m_currentCamera.getOrigin();

            m_newCamera.SetOrigin(origin[0] + offset[0], origin[1] + offset[1], origin[2] + offset[2]);
        }

        startAnimation();

        //    #if 0
        //        {
        //            std::ostringstream out;
        //            out << "Meters Horizontal: " << m_newCamera.GetMetersHorizontal() << std::endl;
        //            TnMapLogError(out.str().c_str());
        //        }
        //    #endif
    }


    public TnMapCameraManager()
    {
        double start_lat = 37.82444444444;
        double start_lon = -122.370277777777;
        double[] start_origin = new double[]{start_lon, start_lat, 0};
        TnMapMagic.latLonToGlobal(start_origin);

        m_blendFactor= 0.0f;
        m_isBlending = false;
        m_blendTime = TnMapConf.DEFAULT_BLEND_TIME;
        m_instaBlend = false;
        m_touchDown = false;
        m_smoothZoom = 0.0f;
        m_vehicleHeading = 0.0f;
        m_headingFrameMomentum= 0.0f;
        m_headingMomentum=0.0f;
        m_headingFriction=0.0f;
        m_declinationFrameMomentum=0.0f;
        m_declinationMomentum=0.0f;
        m_declinationFriction=0.0f;
        m_interactionMode=TnMapConf.DEFAULT_INTERACTION_MODE;
        m_renderingMode=TnMapConf.DEFAULT_RENDERING_MODE;
        m_orientation=TnMapConf.DEFAULT_ORIENTATION;

        m_newCamera = new TnMapCamera();
        m_newCamera.SetOrigin(start_origin[0], start_origin[1], start_origin[2]);

        m_newCamera.SetFov(TnMapConf.DEFAULT_FOV);
        m_newCamera.SetRange(TnMapConf.DEFAULT_RANGE);
        m_newCamera.SetHeading(TnMapConf.DEFAULT_HEADING);
        m_newCamera.SetDeclination(TnMapConf.DEFAULT_CAMERA_DECLINATION_3D);
        m_newCamera.SetZoom(TnMapConf.DEFAULT_ZOOM);
        m_newCamera.SetDistance(TnMapConf.DEFAULT_EYE_DISTANCE);
        m_newCamera.SetVerticalOffset(0.0f);
        m_newCamera.SetOrientation(TnMapConf.DEFAULT_ORIENTATION);

        m_panFrameMomentum = new double[3];
        m_panMomentum = new double[3];
        m_panFriction = new double[3];

        m_targetDeclination.put(new Integer(IMapEngine.RENDERING_MODE_3D),         new Float(TnMapConf.DEFAULT_CAMERA_DECLINATION_3D));
        m_targetDeclination.put(new Integer(IMapEngine.RENDERING_MODE_3D_NORTH_UP),         new Float(TnMapConf.DEFAULT_CAMERA_DECLINATION_3D));
        m_targetDeclination.put(new Integer(IMapEngine.RENDERING_MODE_3D_HEADING_UP),         new Float(TnMapConf.DEFAULT_CAMERA_DECLINATION_3D));
        m_targetDeclination.put(new Integer(IMapEngine.RENDERING_MODE_2D),         new Float(TnMapConf.DEFAULT_DECLINATION_2D));
        m_targetDeclination.put(new Integer(IMapEngine.RENDERING_MODE_2D_NORTH_UP),         new Float(TnMapConf.DEFAULT_DECLINATION_2D));
        m_targetDeclination.put(new Integer(IMapEngine.RENDERING_MODE_2D_HEADING_UP),         new Float(TnMapConf.DEFAULT_DECLINATION_2D));

        //float[] eye = new float[]{0.0f, 0.0f, 0.0f};
        //float[] dir = new float[]{0.0f, 0.0f, 0.0f};
        m_lastTouchRay = new Rayf(0, 0, 0, 0, 0, 0);
    }


    public void setPosition(double[] pos)
    {
        m_newCamera.SetOrigin(pos[0], pos[1], pos[2]);
    }


    public void setScreenSize(int w, int h)
    {
        m_newCamera.SetScreenSize(w, h);
    }

    public void startAnimation()
    {
        startAnimation(true);
    }

    public void startAnimation(boolean resetBlendFactor)
    {
        m_isBlending = true;
        if(resetBlendFactor)
            m_blendFactor = 0;
        m_oldCamera = m_currentCamera;
    }


    public void setInteractionMode(int interactionMode)
    {
        if (m_interactionMode != interactionMode)
        {
            m_interactionMode = interactionMode;  
            startAnimation();
        }
    }


    public void setRange(float range)
    {
        m_currentCamera.SetRange(range);
    }


    public float getRange()
    {
        return m_currentCamera.getRange();
    }


    public void update(float dt, TnMapVehicle vehicle)
    {
        m_vehicleHeading = vehicle.getHeading();

        if (m_smoothZoom != 0.0f)
        {
            if (dt != 0.0f)
            {
                float dz = m_smoothZoom * dt;
                float zoom = (float)Pe.clamp(m_currentCamera.getZoom() + dz,
                        TnMapConf.DEFAULT_ZOOM_LOWER_BOUND, 
                        TnMapConf.DEFAULT_ZOOM_UPPER_BOUND);
                m_newCamera.SetZoom(zoom);
            }
        }

        if(m_interactionMode == IMapEngine.INTERACTION_MODE_FOLLOW_VEHICLE)
        {
            // Lock camera onto vehicle
            setPosition(vehicle.getPosition());

            if(!m_touchDown)
            {
                if((m_renderingMode == IMapEngine.RENDERING_MODE_2D_HEADING_UP) ||
                        (m_renderingMode == IMapEngine.RENDERING_MODE_2D_NORTH_UP) ||
                        (m_renderingMode == IMapEngine.RENDERING_MODE_3D_HEADING_UP) ||
                        (m_renderingMode == IMapEngine.RENDERING_MODE_3D_NORTH_UP))
                {
                    m_newCamera.SetHeading(getTargetHeading());
                }
                m_newCamera.SetDeclination(getTargetDeclination());
                //StartAnimation();
            }
        }
        else if(!m_touchDown)
        {
            // Handle momentum after touch release

            // Handle pan momentum
            if(Pe.magnitude(m_panFrameMomentum) > Pe.magnitude(m_panFriction) * (double)dt)
            {
                m_panFrameMomentum[0] = m_panFrameMomentum[0] - m_panFriction[0] * dt;
                m_panFrameMomentum[1] = m_panFrameMomentum[1] - m_panFriction[1] * dt;
                m_panFrameMomentum[2] = m_panFrameMomentum[2] - m_panFriction[2] * dt;
                
                m_newCamera.SetOrigin(m_newCamera.getOrigin()[0] - m_panFrameMomentum[0], 
                        m_newCamera.getOrigin()[1] - m_panFrameMomentum[1], 
                        m_newCamera.getOrigin()[2] - m_panFrameMomentum[2]);
            }

            // Handle heading momentum
            if(Math.abs(m_headingFrameMomentum) > Math.abs(m_headingFriction * (double)dt))
            {
                m_headingFrameMomentum -= m_headingFriction * (double)dt;
                m_newCamera.SetHeading(m_newCamera.getHeading() - m_headingFrameMomentum);
            }

            // Handle declination momentum
            if(Math.abs(m_declinationFrameMomentum) > Math.abs(m_declinationFriction * (double)dt))
            {
                m_declinationFrameMomentum -= m_declinationFriction * (double)dt;
                m_newCamera.SetDeclination(m_newCamera.getDeclination() - m_declinationFrameMomentum);
            }    
        }
        else
        {
            // Latch last frame's accumumated momentum
            m_panFrameMomentum = m_panMomentum;
            m_headingFrameMomentum = m_headingMomentum;
            m_declinationFrameMomentum = m_declinationMomentum;

            // Clear current frame's accumumated momentum
            Pe.zero(m_panMomentum);
            m_headingMomentum = 0.0f;
            m_declinationMomentum = 0.0f;
        }


        if(m_isBlending)
        {
            if((m_renderingMode == IMapEngine.RENDERING_MODE_2D_HEADING_UP) ||
                    (m_renderingMode == IMapEngine.RENDERING_MODE_2D_NORTH_UP) ||
                    (m_renderingMode == IMapEngine.RENDERING_MODE_3D_HEADING_UP) ||
                    (m_renderingMode == IMapEngine.RENDERING_MODE_3D_NORTH_UP))
            {
                m_newCamera.SetHeading(getTargetHeading());
            }
            if(m_renderingMode == IMapEngine.RENDERING_MODE_2D_HEADING_UP || m_renderingMode == IMapEngine.RENDERING_MODE_3D_NORTH_UP)
                m_newCamera.SetHeading(getTargetHeading());
            m_newCamera.SetDeclination(getTargetDeclination());

            if((m_blendFactor >= 1.0) || m_instaBlend)
            {
                m_isBlending = false;
                m_blendFactor = 0;
                m_currentCamera = m_newCamera;
            }
            else
            {
                m_currentCamera = TnMapCamera.blend(m_oldCamera,m_newCamera,Pe.ease(m_blendFactor));
                m_blendFactor += m_blendTime * dt;
            }
        }
        else
        {
            m_currentCamera = m_newCamera;
        }


    }

    public int getInteractionMode()
    {
        return m_interactionMode;
    }


    public void handleTouchEvent(int event, 
            float touchX, float touchY, 
            float pinch)
    {
        if (pinch < 10)
            pinch = 0;
        
        Rayf touchRay = new Rayf();
        m_currentCamera.GetTouchRay(touchX, touchY, touchRay);

        if (event == IMapEngine.TOUCH_EVENT_BEGIN)
        {
            m_touchDown = true;

            if(m_isBlending)
            {
                m_isBlending = false;
                m_blendFactor = 0;
            }
        }
        else if (event == IMapEngine.TOUCH_EVENT_END)
        {
            m_touchDown = false;
        }
        else if (event == IMapEngine.TOUCH_EVENT_POINTER_DOWN && pinch > 0)
        {
            m_lastPinch = pinch;
        }

        switch (m_interactionMode)
        {
            case IMapEngine.INTERACTION_MODE_PAN_AND_ZOOM:
                panCamera(event, touchRay, pinch);
                break;
            case IMapEngine.INTERACTION_MODE_ROTATE_AROUND_POINT:
                orbitCamera(event, touchX, touchY);
                break;
            case IMapEngine.INTERACTION_MODE_FOLLOW_VEHICLE:
                orbitCamera(event, touchX, touchY);
                break;
            case IMapEngine.INTERACTION_MODE_NONE:
                break;
            default:
                // Do nothing
                break;
        }

        if((event == IMapEngine.TOUCH_EVENT_END) && 
                (m_interactionMode == IMapEngine.INTERACTION_MODE_FOLLOW_VEHICLE))
        {
            m_isBlending = true;
            m_blendFactor = 0;
            m_oldCamera = m_currentCamera;
        }

        m_lastTouchPointX = touchX;
        m_lastTouchPointY = touchY;
        
        m_lastTouchRay = touchRay;
    }

    public void panCamera(int event, Rayf touchRay, float pinch)
    {
        //Planef ground = new Planef(0.0f, 0.0f, 1.0f, 0.0f);

        float[] result1 = new float[3];
        boolean b1 = Pe.intersect(0, 0, 1, 0, touchRay, result1);
        float[] result0 = new float[3];
        boolean b0 = Pe.intersect(0, 0, 1, 0, m_lastTouchRay, result0);

        // @TODO: This might not be right if we still want to support pinch.
        if (!b0 || !b1) 
            return;       // ignore if ground not touched

        float v0 = result1[0] - result0[0];
        float v1 = result1[1] - result0[1];
        float v2 = result1[2] - result0[2];

        float dp = pinch - m_lastPinch;

        switch (event) {

            case IMapEngine.TOUCH_EVENT_BEGIN:
            {
                break;
            }
            case IMapEngine.TOUCH_EVENT_MOVE:
            {
                if (pinch > 0)
                {
                    if (Math.abs(dp) > 3)
                    {
                        float zoom = (float)Pe.clamp(m_currentCamera.getZoom() - dp * TnMapConf.TOUCH_ZOOM_SENSITIVITY,
                                TnMapConf.DEFAULT_ZOOM_LOWER_BOUND, 
                                TnMapConf.DEFAULT_ZOOM_UPPER_BOUND);
                        m_newCamera.SetZoom(zoom);
                        m_lastPinch = pinch;

                        m_newCamera.SetOrigin(m_newCamera.getOrigin()[0] - v0, m_newCamera.getOrigin()[1] - v1,
                                m_newCamera.getOrigin()[2] - v2);
                        
                        // Accumulate momentum
                        m_panMomentum[0] = m_panMomentum[0] + v0;
                        m_panMomentum[1] = m_panMomentum[1] + v1;
                        m_panMomentum[2] = m_panMomentum[2] + v2;
                    }
                }
                else
                {
                    m_newCamera.SetOrigin(m_newCamera.getOrigin()[0] - v0, m_newCamera.getOrigin()[1] - v1,
                            m_newCamera.getOrigin()[2] - v2);
                    
                    // Accumulate momentum
                    m_panMomentum[0] = m_panMomentum[0] + v0;
                    m_panMomentum[1] = m_panMomentum[1] + v1;
                    m_panMomentum[2] = m_panMomentum[2] + v2;
                }
                break;
            }
            case IMapEngine.TOUCH_EVENT_END:
            {
//                float zoom = (float)Pe.clamp(m_currentCamera.GetZoom() - dp * TnMapConf.TOUCH_ZOOM_SENSITIVITY,
//                        TnMapConf.DEFAULT_ZOOM_LOWER_BOUND, 
//                        TnMapConf.DEFAULT_ZOOM_UPPER_BOUND);
//                m_newCamera.SetZoom(zoom);
//                m_newCamera.SetOrigin(Pe.sub(m_newCamera.GetOrigin() , vd));

                // Set friction to last frame's accumulated momentum (per second)
                m_panFriction[0] = m_panFrameMomentum[0] * TnMapConf.FRICTION_FACTOR;
                m_panFriction[1] = m_panFrameMomentum[1] * TnMapConf.FRICTION_FACTOR;
                m_panFriction[2] = m_panFrameMomentum[2] * TnMapConf.FRICTION_FACTOR;
                break;
            }
            default:
                break;
        }
    }


    public void orbitCamera(int event, float touchX, float touchY)
    {
        float v0 = touchX - m_lastTouchPointX;
        float v1 = touchY - m_lastTouchPointY;

        switch (event) {

            case IMapEngine.TOUCH_EVENT_BEGIN:
                m_newCamera = m_currentCamera;
                break;

            case IMapEngine.TOUCH_EVENT_MOVE:
            {
                float heading = 
                    m_currentCamera.getHeading() - v0 * TnMapConf.TOUCH_HEADING_SENSITIVITY;
                float declination =
                    m_currentCamera.getDeclination() - v1 * TnMapConf.TOUCH_DECLINATION_SENSITIVITY;
                if (declination > -32)
                    declination = -32;
                if (declination < - 148)
                    declination = -148;

                m_newCamera.SetHeading(heading);
                m_newCamera.SetDeclination(declination);

                // Set pan momentum to be last recorded change
                m_headingMomentum += v0 * TnMapConf.TOUCH_HEADING_SENSITIVITY;
                m_declinationMomentum += v1 * TnMapConf.TOUCH_DECLINATION_SENSITIVITY;
                break;
            }

            case IMapEngine.TOUCH_EVENT_END:
            {
                float heading = 
                    m_currentCamera.getHeading() - v0 * TnMapConf.TOUCH_HEADING_SENSITIVITY;
                float declination =
                    m_currentCamera.getDeclination() - v1 * TnMapConf.TOUCH_DECLINATION_SENSITIVITY;
                m_newCamera.SetHeading(heading);
                m_newCamera.SetDeclination(declination);

                // Set friction to last frame's accumulated momentum (per second)
                m_headingFriction = m_headingFrameMomentum;        
                m_declinationFriction = m_declinationFrameMomentum;        
                break;
            }

            default:
                break;
        }
    }


    public void beginSmoothZoomIn()
    {
        m_smoothZoom = -TnMapConf.DEFAULT_ZOOM_SPEED;
    }


    public void beginSmoothZoomOut()
    {
        m_smoothZoom = TnMapConf.DEFAULT_ZOOM_SPEED;
    }


    public void endSmoothZoom()
    {
        m_smoothZoom = 0.0f;
    }

    public void setTransitionTime(float seconds)
    {
        if(seconds > Float.MIN_VALUE)
        {
            m_blendTime = 1.0f / seconds;
            m_instaBlend = false;
        }
        else
        {
            m_blendTime = 0.0f;
            m_instaBlend = true;
        }
    }


    public void setRenderingMode(int mode)
    {
        /* @TODO: Orthogonalize 2D/3D mode from North/Heading up. Then fix
         * this. */
        if(true || mode != m_renderingMode)
        {
            m_renderingMode = mode;
            startAnimation(false);
        }
    }


    public int getRenderingMode()
    {
        return m_renderingMode;
    }


    public void setOrientation(int orientation)
    {
        m_orientation = orientation;
        m_newCamera.SetOrientation(orientation);

        startAnimation();
    }


    public int getOrientation()
    {
        return m_orientation;
    }

    public void setLookAtPoint(double[] pos, float heading)
    {
        m_newCamera.SetOrigin(pos[0], pos[1], pos[2]);
        m_newCamera.SetHeading(heading);
        startAnimation();
    }

    public double[] getLookAtPoint()
    {
        return m_currentCamera.getOrigin();
    }


    // animate to a view that encompases area specified by the diagonal
    public void setLookAtDiagonal(double[] sw, double[] ne)
    {
        // Get midpoint between corners
        double[] midpoint = Pe.add(sw, Pe.multiply(Pe.sub(ne, sw), 0.5));

        // Set the look-at point
        setLookAtPoint(midpoint,0);

        // Get half distance between corners
        double halfD = Pe.magnitude(Pe.sub(ne, sw)) * 0.5 * 1.2;

        // Tangent of half the vertical and horizontal FOV
        double tanHalfHFov = Math.tan(Pe.degToRad(m_currentCamera.GetHorizontalFOV() * 0.5));
        double tanHalfVFov = Math.tan(Pe.degToRad(m_currentCamera.GetVerticalFOV() * 0.5));

        // Calculate needed eye distance to target given cross-map distance and FOV
        // Do it once using vertical FOV and once using horizontal FOV
        double fh = halfD / tanHalfHFov;
        double fv = halfD / tanHalfVFov;

        // Take the maximum

        //    #if defined ( _WIN32 )
        //        float realDistance = static_cast<float>(max(fh,fv));
        //    #else
        float realDistance = (float)Math.max(fh,fv);
        //    #endif

        // Calculate zoom given length to target and our camera's magic "distance" value
        float zoom = (float)TnMapMagic.realToZoomSpace(realDistance, m_currentCamera.getDistance());
        setZoom(zoom);  
    }


    public void setVehiclePosition(double[] pos)
    {
        if(getInteractionMode() == IMapEngine.INTERACTION_MODE_FOLLOW_VEHICLE)
        {
            // May need to do something here to animate to the next car position,
            // but StartAnimation() will not cut it. Results in overall animation
            // constantly starting and stopping.
        }
    }


    // offset look-at point up or down the screen
    public void setVerticalOffset(double offset)
    {
        if(Math.abs((float)offset - m_newCamera.getVerticalOffset()) > Float.MIN_VALUE)
        {
            m_newCamera.SetVerticalOffset((float)offset);
        }
    }


    public float getTargetHeading()
    {
        switch (m_renderingMode)
        {

            case IMapEngine.RENDERING_MODE_2D_HEADING_UP:
            case IMapEngine.RENDERING_MODE_3D_HEADING_UP:
                return m_vehicleHeading;
            default:
                return 0.0f;
        }
    }


    public void setTargetDeclination(int mode, float declination)
    {
        m_targetDeclination.put(new Integer(mode), new Float(declination));
    }


    // gets the currently set declination value for given eRenderingMode
    public float getTargetDeclination(int mode)
    {
        return ((Float)m_targetDeclination.get(new Integer(mode))).floatValue();
    }


    public float getTargetDeclination()
    {
        return getTargetDeclination(m_renderingMode);
    }
}
