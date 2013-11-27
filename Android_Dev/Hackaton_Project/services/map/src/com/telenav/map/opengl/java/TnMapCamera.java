/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * TnMapCamera.java
 *
 */
package com.telenav.map.opengl.java;

import com.telenav.map.IMapEngine;
import com.telenav.map.opengl.java.math.Matrixf;
import com.telenav.map.opengl.java.math.Pe;
import com.telenav.map.opengl.java.math.Transform;
import com.telenav.map.opengl.java.math.Ray.Rayf;
import com.telenav.tnui.opengles.TnGLUtils;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2010-9-28
 */
public class TnMapCamera
{
    float m_width;
    float m_height;

    float m_fov;
    float m_zoom;
    float m_distance;
    float m_range;
    float m_heading;
    float m_declination;
    float m_verticalOffsetFactor;
    float m_orientation;

    double[] m_origin = new double[3];

    Matrixf       m_projMat;
    boolean                m_projMatDirty;

    Matrixf       m_cameraMat;
    Matrixf       m_cameraSpriteMat;
    boolean                m_cameraMatDirty;

    Matrixf       m_invProjMat;
    boolean                m_invProjMatDirty;

    Matrixf       m_invCameraMat;
    boolean                m_invCameraMatDirty;

    Matrixf       m_skyMat;
    boolean                m_skyMatDirty;

    Matrixf       m_billboardMat;
    boolean               m_billboardMatDirty;

    // These are always dirty. We have a local copy just to keep the
    // get matrix API consistent about returning const refs.

    Matrixf       m_spriteMat;
    Matrixf       m_translateMat;
    Matrixf       m_viewMat;
    Matrixf       m_invViewMat;
    
    float[] nearAndFar = new float[2];
    boolean nearAndFarDirty;
    
    public TnMapCamera(float width,
        float height,
        double[] origin,
        float fov,
        float zoom,
        float distance,
        float range,
        float heading,
        float declination,
        float verticalOffset,
        float orientation)
    {
        this.m_width = width;
        this.m_height = height;
        this.m_fov = fov;
        this.m_zoom = zoom;
        this.m_distance = distance;
        this.m_range = range;
        this.m_heading = heading;
        this.m_declination = declination;
        this.m_verticalOffsetFactor = verticalOffset;
        this.m_orientation = orientation;
        this.m_origin = origin;
        this.m_projMatDirty = true;
        this.m_cameraMatDirty = true;
        this.m_invProjMatDirty = true;
        this.m_invCameraMatDirty = true;
        this.m_skyMatDirty = true;
        this.m_billboardMatDirty = true;
    }
    
    public TnMapCamera()
    {
        m_width = 0;
        m_height = 0;
        m_fov = 0f;
        m_zoom = 0f;
        m_distance = 0f;
        m_range = 0f;
        m_heading = 0f;
        m_declination = 0f;
        m_verticalOffsetFactor = 0f;
        m_orientation = 0f;
        m_origin = new double[3];
        m_projMatDirty = true;
        m_cameraMatDirty = true;
        m_invProjMatDirty = true;
        m_invCameraMatDirty = true;
        m_skyMatDirty = true;
        m_billboardMatDirty = true;
    }
    
    public TnMapCamera(TnMapCamera  that)
    {
            m_width = that.m_width;
            m_height = that.m_height;
            m_fov = that.m_fov;
            m_zoom = that.m_zoom;
            m_distance = that.m_distance;
            m_range = that.m_range;
            m_heading = that.m_heading;
            m_declination = that.m_declination;
            m_verticalOffsetFactor = that.m_verticalOffsetFactor;
            m_orientation = that.m_orientation;
            m_origin = that.m_origin;
            m_projMatDirty = true;
            m_cameraMatDirty = true;
            m_invProjMatDirty = true;
            m_invCameraMatDirty = true;
            m_skyMatDirty = true;
            m_billboardMatDirty = true;
    }
    
    public float getOrientation()
    {
        return m_orientation;
    }

    public float getFov()
    {
        return m_fov;
    }

    public float getZoom()
    {
        return m_zoom;
    }

    public float getDistance()
    {
        return m_distance;
    }

    public float getRange()
    {
        return m_range;
    }

    public float getHeading()
    {
        return m_heading;
    }

    public float getDeclination()
    {
        return m_declination;
    }

    public float getVerticalOffset()
    {
        return m_verticalOffsetFactor;
    }

    public double[] getOrigin()
    {
        return m_origin;
    }
    
    public static TnMapCamera blend(TnMapCamera  a, TnMapCamera  b, float s)
    {
        double[] v = Pe.multiply(Pe.sub(b.m_origin, a.m_origin), s);

        return new TnMapCamera(Pe.lerp(a.m_width, b.m_width, s),
            Pe.lerp(a.m_height, b.m_height, s),
                           Pe.add(a.m_origin, v),
                           Pe.lerp(a.m_fov, b.m_fov, s),
                           Pe.lerp(a.m_zoom, b.m_zoom, s),
                           Pe.lerp(a.m_distance, b.m_distance, s),
                           Pe.lerp(a.m_range, b.m_range, s),
                           Pe.lerpDeg(a.m_heading, b.m_heading, s),
                           Pe.lerp(a.m_declination, b.m_declination, s),
                           Pe.lerp(a.m_verticalOffsetFactor, b.m_verticalOffsetFactor, s),
                           Pe.lerpDeg(a.m_orientation, b.m_orientation, s));
    }
    
    public void MarkCameraDirty()
    {
        m_cameraMatDirty = true;
        m_invCameraMatDirty = true;
        m_skyMatDirty = true;
        m_billboardMatDirty = true;
        nearAndFarDirty = true;
    }

    public void MarkProjectionDirty()
    {
        m_projMatDirty = true;
        m_invProjMatDirty = true;
    }
    
    public void SetScreenSize(int width, int height)
    {
        m_width = width;
        m_height = height;
        MarkProjectionDirty();
    }


    public void SetOrigin(double x, double y, double z)
    {
        m_origin[0] = x;
        m_origin[1] = y;
        m_origin[2] = z;
        
        // Handle dateline
        TnMapMagic.globalWrapAndClamp(m_origin);
    }


    public void SetFov(float fov)
    {
        m_fov = fov;
        MarkProjectionDirty();
    }


    public float GetVerticalFOV()
    {
        float[] size = GetProjSize();
        return Pe.radToDeg(size[1]) * 2.0f;
    }


    public float GetHorizontalFOV() 
    {
        float[] size = GetProjSize();
        return Pe.radToDeg(size[0]) * 2.0f;
    }


    public float[] GetProjSize() {

        float screen_diag = (float)Math.sqrt(m_width * m_width + m_height * m_height);

        float diag = (float)Math.tan(Pe.degToRad(m_fov / 2.0f));
        
        float height = diag * m_height / screen_diag;
        float width = diag * m_width / screen_diag;

        return new float[]{width, height};
    }    


    public void SetZoom(float zoom)
    {
        m_zoom = zoom;
        MarkProjectionDirty();
        MarkCameraDirty();
    }


    public void SetHeading(float heading)
    {
        m_heading = heading;
        MarkCameraDirty();
    }


    public void SetDistance(float distance)
    {
        m_distance = distance;
        MarkCameraDirty();
    }


    public void SetRange(float range)
    {
        m_range = range;
        MarkProjectionDirty();
    }


    public void SetDeclination(float declination)
    {
        m_declination = declination;
        MarkCameraDirty();
    }


    public void SetVerticalOffset(float offset)
    {
        m_verticalOffsetFactor = offset;
        MarkProjectionDirty();
    }


    public void SetOrientation(int orientation)
    {
        switch(orientation)
        {
        case IMapEngine.ORIENTATION_PORTRAIT_BOTTOM:
            m_orientation = 0.0f;
            break;

        case IMapEngine.ORIENTATION_PORTRAIT_TOP:
            m_orientation = 180.0f;
            break;

        case IMapEngine.ORIENTATION_LANDSCAPE_LEFT:
            m_orientation = -90.0f;
            break;

        case IMapEngine.ORIENTATION_LANDSCAPE_RIGHT:
            m_orientation = 90.0f;
            break;

        case IMapEngine.ORIENTATION_INVALID:
            // ignore, but make compiler warning go away
            break;

        }

    }

    public void SetOrientation(float orientation)
    {
        m_orientation = Pe.wrap(orientation, -180.0f, 180.0f);
    }
    
    public float[] getNearFar()
    {
        if (nearAndFarDirty)
        {
            CalcNearFar();
            nearAndFarDirty = false;
        }
        return nearAndFar;
    }
    
    private void CalcNearFar()
    {
        float tileSize = TnMapMagic.getTileSize(TnMapMagic.zoomSpaceToZoomLevel(m_zoom));
        float realDistance = TnMapMagic.zoomSpaceToReal(m_zoom, m_distance);
        float znear = realDistance * 0.05f;
        float realRange = m_range * tileSize;
        float zfar = (realRange + realDistance);

        nearAndFar[0] = znear;
        nearAndFar[1] = zfar;
    }


    public float GetScreenWidth()
    {
        float q = m_orientation / 90.0f;
        float n = (float)Math.floor(q);
        float s = q - n;
        float t = ((int) n) % 2 > 0 ? (1.0f - s) : s;

        return Pe.lerp(m_width, m_height, t);
    }


    public float[] CalcSkewOffset()
    {
        float[] nearAndFar = getNearFar();
        float znear = nearAndFar[0];

        float[] proj_size = GetProjSize();

        float height = znear * proj_size[1];
        Matrixf orientMat = new Matrixf();
        orientMat.buildRotateZDeg(m_orientation);
        
        float v_off = height * m_verticalOffsetFactor * GetScreenHeight() / m_height;
        float[] offset = Transform.transform(orientMat, new float[]{0.0f, v_off, 0.0f});
        return offset;
    }



    public float GetScreenHeight()
    {
        float q = m_orientation / 90.0f;
        float n = (float)Math.floor(q);
        float s = q - n;
        float t = ((int) n) % 2 > 0 ? (1.0f - s) : s;

        return Pe.lerp(m_height, m_width, t);
    }


    public Matrixf GetAnnotation2DMatrix()
    {
        float w = GetScreenWidth();
        float h = GetScreenHeight();
        
        Matrixf a = new Matrixf();
        a.buildOrtho2(0, m_width, m_height, 0);
        
        Matrixf b = new Matrixf();
        b.buildTranslate(m_width/2.0f, m_height/2.0f, 0.0f);
        
        Matrixf c = new Matrixf();
        c.buildRotateZDeg(-m_orientation);
        
        Matrixf d = new Matrixf();
        d.buildTranslate(-w/2.0f, -h/2.0f, 0.0f);
        
        Matrixf mat = 
            Matrixf.multiply(Matrixf.multiply(a, b), Matrixf.multiply(c, d));
        
        return mat;
    }


    public Matrixf GetProjectionMatrix()
    {
        if (m_projMatDirty)
        {
            float[] nearAndFar = getNearFar();
            float znear = nearAndFar[0];
            float zfar = nearAndFar[1];

            float[] proj_size = GetProjSize();

            float height = znear * proj_size[1];
            float width = znear * proj_size[0];

            Matrixf orientMat = new Matrixf();
            orientMat.buildRotateZDeg(m_orientation);

            float[] offset = CalcSkewOffset();
            
            Matrixf a = new Matrixf();
            a.buildFrustum(-width-offset[0], width-offset[0], 
                    -height-offset[1], height-offset[1], 
                    znear, zfar);
            
            Matrixf b = new Matrixf(orientMat);
            m_projMat = 
                Matrixf.multiply(a, b);

            m_projMatDirty = false;
        }

        return m_projMat;
    }


    public Matrixf GetInverseProjectionMatrix()
    {
        if (m_invProjMatDirty)
        {
            m_invProjMat = Matrixf.invert(GetProjectionMatrix());
            m_invProjMatDirty = false;
        }

        return m_invProjMat;
    }


    public Matrixf GetCameraMatrix()
    {
        if (m_cameraMatDirty)
        {
            float tileSize0 = TnMapMagic.getTileSize(0);
            float realDistance = (float)(TnGLUtils.getInstance().pow(2.0f, m_zoom) * tileSize0 * m_distance);
            
            Matrixf a = new Matrixf();
            a.buildTranslate(0.0f, 0.0f, -realDistance);
            
            Matrixf b = new Matrixf();
            b.buildRotateXDeg(-m_declination);
            
            Matrixf c = new Matrixf();
            c.buildRotateYDeg(m_heading);
            
            Matrixf d = new Matrixf();
            d.buildRotateXNeg90();
            
            m_cameraMat = Matrixf.multiply(Matrixf.multiply(a, b), Matrixf.multiply(c, d));
            m_cameraMatDirty = false;
            
            // Pre-calculate most of sprite matrix

            // For now, eye is just down neg z axis camera space.
            float[] e = new float[]{m_cameraMat.m[2], m_cameraMat.m[6], m_cameraMat.m[10]};
            
            // Find rotation around z axis to bring local y toward eye in the x-y plane.
            float z_rot = (float)TnGLUtils.getInstance().atan2(e[0], -e[1]);
            
            // Find rotation around x axis to bring y axis up toward eye.
            float[] xy = new float[]{e[0], e[1]};
            float x_rot = -(float)TnGLUtils.getInstance().atan2(e[2], Pe.magnitude(xy));
            
            // Find scale factor
            float s = TnMapMagic.zoomSpaceToReal(m_zoom, 1.0f);

            // The rest of the sprite matrix needs the actual sprite position.
            Matrixf ex = new Matrixf();
            ex.buildRotateZRad(z_rot);
            
            Matrixf fx = new Matrixf();
            fx.buildRotateXRad(x_rot);
            
            Matrixf gx = new Matrixf();
            gx.buildScale(s);
            
            m_cameraSpriteMat = Matrixf.multiply(Matrixf.multiply(ex, fx), gx);
        }

        return m_cameraMat;
    }


    public Matrixf GetInverseCameraMatrix()
    {
        if (m_invCameraMatDirty)
        {
            m_invCameraMat = Matrixf.invert(GetCameraMatrix());
            m_invCameraMatDirty = false;
        }

        return m_invCameraMat;
    }


    public Matrixf GetSkyMatrix()
    {
        if (m_skyMatDirty)
        {
            float[] nearAndFar = getNearFar();
            float znear = nearAndFar[0];
            float zfar = nearAndFar[1];

            // Scooch sky down as declination increases to bring the
            // horizon down.

            float declination = (float)Math.sin(Pe.degToRad(m_declination));
            float z = 0.21f * declination;

            Matrixf m = GetCameraMatrix();

            Matrixf m1 = new Matrixf();
            m1.buildScale((zfar + znear) / 2.0f);
            
            Matrixf m2 = new Matrixf();
            m2.buildTranslate(0.0f, 0.0f, z);
            
            m_skyMat = 
                Matrixf.multiply(Matrixf.multiply(new Matrixf(m.m[0], m.m[1], m.m[2],
                        m.m[4], m.m[5], m.m[6],
                        m.m[8], m.m[9], m.m[10], false), m1),
                m2);
            
            m_skyMatDirty = false;
        }

        return m_skyMat;
    }


    public Matrixf GetLameSkyMatrix(float lameSkyAmount, float camDeclination)
    {
        if (m_skyMatDirty)
        {
            float[] nearAndFar = getNearFar();
            float znear = nearAndFar[0];
            float zfar = nearAndFar[1];
            float[] proj_size = GetProjSize();
            float h = (proj_size[1] - proj_size[1] * m_verticalOffsetFactor) * GetScreenHeight() / m_height;

            float top_screen_declination = (float)(Pe.radToDeg(TnGLUtils.getInstance().atan(h)) - lameSkyAmount);
            float ideal_declination = -m_declination + (30.0f/90.0f) * m_declination;

            float diff = (camDeclination - m_declination);
//            float s = diff / (90 + camDeclination);

            float dec = (top_screen_declination > ideal_declination)
                ? 
                ideal_declination
                : 
                Math.min(top_screen_declination + diff, ideal_declination);

            Matrixf a = new Matrixf();
            a.buildRotateXDeg(dec);
            
            Matrixf b = new Matrixf();
            b.buildRotateYDeg(m_heading);
            
            Matrixf c = new Matrixf();
            c.buildRotateXNeg90();
            
            Matrixf d = new Matrixf();
            d.buildScale((zfar + znear) / 2.0f);
            
            m_skyMat = Matrixf.multiply(Matrixf.multiply(a, b), Matrixf.multiply(c, d));

//    #if 0
//            {
//                static float old_top_screen_declination = 0;
//                static float old_ideal_declination = 0;
//
//                if ((old_top_screen_declination != top_screen_declination) ||
//                    (old_ideal_declination != ideal_declination))
//                {
//                    std::ostringstream out;
//                    out << "dec: " << dec
//                        << " \tdiff: " << diff
//                        << " \tdecl: " << m_declination
//                        << " \tdefault: " << camDeclination
//                        << " \ts: " << s
//                        << std::endl;
//                    TnMapLogError(out.str().c_str());
//                    old_top_screen_declination = top_screen_declination;
//                    old_ideal_declination = ideal_declination;
//                }
//            }
//    #endif
            
            m_skyMatDirty = false;
        }

        return m_skyMat;
    }



    public Matrixf GetSpriteMatrix(double[] spriteOrigin)
    {
        // This ensures precomputed part of sprite matrix has been set
        GetCameraMatrix();
        
        m_spriteMat = Matrixf.multiply(GetTranslationMatrix(spriteOrigin), m_cameraSpriteMat);
        return m_spriteMat;
    }

    public void GetTouchRay(float touchX, float touchY, Rayf ray) 
    {
        Matrixf  projInv = GetInverseProjectionMatrix();
        Matrixf  cameraInv = GetInverseCameraMatrix();

        float[] nearAndFar = getNearFar();
        float znear = nearAndFar[0];

        // Get normalized viewport x, y touch coordinates. (They seem to
        // need to be normalized to -0.5 to 0.5 instead of -1 to 1 like I
        // expected. Not sure why.)
        float tx = (float)((touchX / m_width) - 0.5f);
        float ty = (float)(-((touchY / m_height) - 0.5f));

        //float[] vp_xy = Transform.transform(projInv, new float[]{tx, ty, -znear});
        Matrixf m = projInv;
        float w = m.m[3] * tx + m.m[7] * ty + m.m[11] * (-znear) + m.m[15];
        //float inv_w = Pe.invert(w);
        float vp_xy0 = (m.m[0] * tx + m.m[4] * ty + m.m[8] * (-znear) + m.m[12]) / w;
        float vp_xy1 = (m.m[1] * tx + m.m[5] * ty + m.m[9] * (-znear) + m.m[13]) / w;
        //result[2] = (m.m[2] * tx + m.m[6] * ty + m.m[10] * (-znear) + m.m[14]) * inv_w;

        // Find touch ray in camera space.
        
        float[] eye = new float[]{0.0f, 0.0f, 0.0f};
        //float[] dir = new float[]{vp_xy[0], vp_xy[1], -1.0f};

//        float[] new_eye = Transform.transform(cameraInv, eye);
        m = cameraInv;
        w = m.m[15];
        //inv_w = Pe.invert(w);
        float new_eye0 = m.m[12] / w;
        float new_eye1 = m.m[13] / w;
        float new_eye2 = m.m[14] / w;
  
        //float[] new_dir = Transform.transformVec(cameraInv, dir);
        float new_dir0 = m.m[0] * vp_xy0 + m.m[4] * vp_xy1 - m.m[8];
        float new_dir1 = m.m[1] * vp_xy0 + m.m[5] * vp_xy1 - m.m[9];
        float new_dir2 = m.m[2] * vp_xy0 + m.m[6] * vp_xy1 - m.m[10];

        float sqrt = (float)Math.sqrt(new_dir0 * new_dir0 + new_dir1 * new_dir1 + new_dir2 * new_dir2);
        new_dir0 /= sqrt;
        new_dir1 /= sqrt;
        new_dir2 /= sqrt;
        
        //Rayf ray = new Rayf(new_eye0, new_eye1, new_eye2, new_dir0, new_dir1, new_dir2);
        ray.originX = new_eye0;
        ray.originY = new_eye1;
        ray.originZ = new_eye2;
        ray.directionX = new_dir0;
        ray.directionY = new_dir1;
        ray.directionZ = new_dir2;
    }
//
//
//    #if 0
//    // This is for picking relative to a tile. Not sure we need it and I
//    // don't like having two versions to maintain, so it's currently
//    // ifdef'd out. If you un-ifdef this, make sure it matches the math in
//    // the other GetTouchRay.
//    tngm::Ray3f TnMapCamera::GetTouchRay(tngm::Point2i const & touchPoint, tngm::Point3d const & position) const {
//
//        using namespace tngm;
//
//        Matrixf const & projInv = GetInverseProjectionMatrix();
//        Matrixf const & viewInv = GetInverseViewMatrix(position);
//
//        // Get normalized viewport x, y touch coordinates.
//        std::pair<float,float> nearAndFar = CalcNearFar();
//        float znear = nearAndFar.first;
//
//        float tx = (touchPoint[0] / m_width) - 0.5f;
//        float ty = -((touchPoint[1] / m_height) - 0.5f);
//
//        Point3f vp_xy = transform(projInv, Point3<float>(tx, ty, -znear));
//
//        // Find touch ray in camera space.
//        
//        Point3f eye = Point3<float>(0.0f, 0.0f, 0.0f);
//        Vec3f dir = Vec3<float>(vp_xy[0], vp_xy[1], -1.0f);
//
//        Point3f new_eye = transform(viewInv, eye);
//        Vec3f new_dir = transform(viewInv, dir);
//
//        Ray3f ray = Ray3f(new_eye, normalize(new_dir));
//
//        return ray;
//    }
//    #endif


    public Matrixf GetTranslationMatrix(double[] position)
    {
        
        double[] offset = Pe.sub(position, m_origin); // tile offset given origin
        m_translateMat = new Matrixf();
        m_translateMat.buildTranslate((float)offset[0], 
                (float)offset[1],
                (float)offset[2]);
        return m_translateMat;
    }


    public float[] GetScreenSize()
    {
        return new float[]{m_width,m_height};
    }


    public float Get2DScaleFactor()
    {
        return (float)(TnMapConf.SCALE_VALUE_FOR_2D_STUFF/Math.sqrt(m_height * m_height + m_width * m_width));
    }


    public Matrixf GetViewMatrix(double[] position) 
    {
        Matrixf camera = GetCameraMatrix();
        Matrixf  xlate = GetTranslationMatrix(position);
        m_viewMat = Matrixf.multiply(camera , xlate);
        return m_viewMat;
    }


    public Matrixf GetInverseViewMatrix(double[] position)
    {

//    #if 0 
//        // fill this in
//        //    m_viewMat = camera * xlate;
//    #else
        Matrixf inv_camera = GetInverseCameraMatrix();
        Matrixf inv_xlate = Matrixf.invert(GetTranslationMatrix(position));
        m_invViewMat = Matrixf.multiply(inv_xlate , inv_camera);
//    #endif
        return m_invViewMat;
    }


    public double GetMetersHorizontal()
    {
        float[] proj_size = GetProjSize();

        double realEyeDistance = TnMapMagic.zoomSpaceToReal(m_zoom, m_distance);

        double span = realEyeDistance * proj_size[0];

        double[] p0 = Pe.add(m_origin, new double[]{span, 0.0, 0.0});
        double[] p1 = Pe.sub(m_origin, new double[]{span, 0.0, 0.0});


        double lat = TnMapMagic.globalToLat(m_origin[1]);
        double lon0 = TnMapMagic.globalToLon(p0[0]);
        double lon1 = TnMapMagic.globalToLon(p1[0]);

//    #if 0
//        {
//            std::ostringstream out;
//            out << "lat: " << lat
//                << "\nlon0: " << lon0
//                << "\nlon1: " << lon1
//                << std::endl;
//            TnMapLogError(out.str().c_str());
//        }
//    #endif

        double phi = Pe.degToRad(lat);
        double theta0 = Pe.degToRad(lon0);
        double theta1 = Pe.degToRad(lon1);

        // This is based on the Haversine formula, but simplified given
        // the assumption of a constant latitude.

        double d = TnGLUtils.getInstance().acos(Math.sin(phi) * Math.sin(phi) + Math.cos(phi) * Math.cos(phi) * Math.cos(theta1-theta0));

        double r = 6378100.0;
        return d * r;
    }
}
