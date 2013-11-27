/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * TnMapVehicleBuilder.java
 *
 */
package com.telenav.map.opengl.java;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import com.telenav.map.opengl.java.TnMap.TnMapVertex5f;
import com.telenav.nio.TnNioManager;
import com.telenav.tnui.opengles.TnGL10;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2010-9-29
 */
public class TnMapVehicleBuilder
{
    TnGL10 gl10;
    
    public TnMapVehicleBuilder(TnGL10 gl10)
    {
        this.gl10 = gl10;
    }
    
    public boolean buildVehicle(TnMapVehicle vehicle)
    {
        ByteBuffer vbb = TnNioManager.getInstance().allocateDirect(WaveFront2f1.modelNumVerts * TnMapVertex5f.size());
        FloatBuffer data = vbb.asFloatBuffer();
        data.put(WaveFront3f1.modelVerts);
        data.put(WaveFront3f2.modelVerts);
        data.put(WaveFront3f3.modelVerts);
        data.put(WaveFront3f4.modelVerts);
        data.put(WaveFront2f1.modelTexCoords);
        data.put(WaveFront2f2.modelTexCoords);
        //Build interleaved Array
//        for(int x = 0; x < WaveFront2f1.modelNumVerts; x++)
//        {
//            data.put((float)WaveFront3f4.modelVerts[x * 3]);
//            data.put((float)WaveFront3f4.modelVerts[x * 3 + 1]);
//            data.put((float)WaveFront3f4.modelVerts[x * 3 + 2]);
//            data.put((float)WaveFront2f2.modelTexCoords[x * 2]);
//            data.put((float)WaveFront2f2.modelTexCoords[x * 2 + 1]);
//        }
        data.position(0);
        
        int sizeOfData2 = 30;
        ByteBuffer vbb2 = TnNioManager.getInstance().allocateDirect(sizeOfData2 * 4);
        FloatBuffer data2 = vbb2.asFloatBuffer();
        
        data2.put(-0.5f);
        data2.put(0.5f);
        data2.put(0f);
        data2.put(0f);
        data2.put(0f);
        
        data2.put(-0.5f);
        data2.put(-0.5f);
        data2.put(0f);
        data2.put(0f);
        data2.put(1f);
        
        data2.put(0.5f);
        data2.put(-0.5f);
        data2.put(0f);
        data2.put(1f);
        data2.put(1f);
        
        data2.put(-0.5f);
        data2.put(0.5f);
        data2.put(0f);
        data2.put(0f);
        data2.put(0f);
        
        data2.put(0.5f);
        data2.put(0.5f);
        data2.put(0f);
        data2.put(1f);
        data2.put(0f);
        
        data2.put(0.5f);
        data2.put(-0.5f);
        data2.put(0f);
        data2.put(1f);
        data2.put(1f);
        data2.position(0);

        vehicle.setVertexID(vbb, WaveFront2f1.modelNumVerts, vbb2, sizeOfData2);

        return true;
    }
}
