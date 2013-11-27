/**
 *
 * Copyright 2013 TeleNav, Inc. All rights reserved.
 * SnapShotHandler.java
 *
 */
package com.telenav.tnui.widget.android;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.Hashtable;

import javax.microedition.khronos.opengles.GL10;

import com.telenav.util.PrimitiveTypeCache;


/**
 *@author bduan
 *@date 2013-3-3
 */
public class SnapShotHandler
{
    private IGLSnapBitmapCallBack callback;
    private int x, y, height, width;
    private long transactionId;
    private static Hashtable screenShotCache; //ideally, should be only two value inside, portrait/landscape.
    
    public SnapShotHandler(int x, int y, int width, int height, IGLSnapBitmapCallBack callback, long transactionId)
    {
        this.x = x;
        this.y = y;
        this.height = height;
        this.width = width;
        this.callback = callback;
        this.transactionId = transactionId;
        
        if (screenShotCache == null)
        {
            screenShotCache = new Hashtable();
        }
    }
    
    public void doSnapShot(GL10 gl)
    {
        if (callback == null)
        {
            return;
        }

        int screenshotSize = width * height;
        int pixelsBuffer[] = null;
        pixelsBuffer = (int [])screenShotCache.get(PrimitiveTypeCache.valueOf(screenshotSize));
        
        if (pixelsBuffer == null)
        {
            pixelsBuffer = new int[screenshotSize];
            screenShotCache.put(PrimitiveTypeCache.valueOf(screenshotSize), pixelsBuffer);
        }
        else
        {
            Arrays.fill(pixelsBuffer, 0);
        }
        
        ByteBuffer bb = null;
        bb = ByteBuffer.allocateDirect(screenshotSize * 4);
        bb.order(ByteOrder.nativeOrder());
        gl.glReadPixels(x, y, width, height, GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, bb);
        bb.asIntBuffer().get(pixelsBuffer);
        bb = null;
        
        callback.snapBitmapCompleted(pixelsBuffer, width, height, transactionId);
    }
}
