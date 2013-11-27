/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * Ray.java
 *
 */
package com.telenav.map.opengl.java.math;


/**
 *@author fqming (fqming@telenav.cn)
 *@date 2010-9-28
 */
public class Ray
{
    public static class Rayf extends Ray
    {
        public float originX, originY, originZ;
        public float directionX, directionY, directionZ;
        
        public Rayf()
        {
            
        }
        
        public Rayf(float oX, float oY, float oZ, float dX, float dY, float dZ)
        {
            this.originX = oX;
            this.originY = oY;
            this.originZ = oZ;
            this.directionX = dX;
            this.directionY = dY;
            this.directionZ = dZ;
        }
        
//        public static Rayf normalize(Rayf ray) {
//            return new Rayf(ray.originX, , Pe.normalize(ray.direction));
//        }
    }
    
    public static class Rayd extends Ray
    {
        public double[] origin;
        public double[] direction;
        public Rayd(double[] o, double[] d)
        {
            this.origin = o;
            this.direction = d;
        }
        
        public static Rayd normalize(Rayd ray) {
            return new Rayd(ray.origin, Pe.normalize(ray.direction));
        }
    }
}
