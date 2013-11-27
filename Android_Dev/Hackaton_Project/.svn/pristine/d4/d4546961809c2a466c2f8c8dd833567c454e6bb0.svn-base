/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * TnMapBuildManager.java
 *
 */
package com.telenav.map.opengl.java;

import com.telenav.tnui.opengles.TnGL10;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2010-9-29
 */
public class TnMapBuildManager
{
    public TnMapAnnotationBuilder annotationBuilder;
    public TnMapVehicleBuilder vehicleBuilder;
    
    public TnMapBuildManager(TnGL10 gl10, TnMapTextureLoader loader)
    {
        annotationBuilder = new TnMapAnnotationBuilder(gl10, loader);
        vehicleBuilder = new TnMapVehicleBuilder(gl10);
    }
    
    public boolean build()
    {
        return true;
    }

    public TnMapAnnotationBuilder getAnnotationBuilder()
    {
        return annotationBuilder;
    }

    public TnMapVehicleBuilder getVehicleBuilder()
    {
        return vehicleBuilder;
    }
}
