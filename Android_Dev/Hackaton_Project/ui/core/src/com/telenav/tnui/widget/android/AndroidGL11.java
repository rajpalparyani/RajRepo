/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * AndroidGL11.java
 *
 */
package com.telenav.tnui.widget.android;

import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import com.telenav.tnui.opengles.TnGL11;

/**
 *@author fqming (fqming@telenav.cn)
 *@date Sep 8, 2010
 */
class AndroidGL11 extends AndroidGL10 implements TnGL11
{
    private GL11 gl11;
    
    public void setGL(GL10 gl)
    {
        super.setGL(gl);
        this.gl11 = (GL11)gl;
    }
    
    public void glBindBuffer(int target, int buffer)
    {
        gl11.glBindBuffer(target, buffer);
    }

    public void glBufferData(int target, int size, Buffer data, int usage)
    {
        gl11.glBufferData(target, size, data, usage);
    }

    public void glBufferSubData(int target, int offset, int size, Buffer data)
    {
        gl11.glBufferSubData(target, offset, size, data);
    }

    public void glClipPlanef(int plane, float[] equation, int offset)
    {
        gl11.glClipPlanef(plane, equation, offset);
    }

    public void glClipPlanef(int plane, FloatBuffer equation)
    {
        gl11.glClipPlanef(plane, equation);
    }

    public void glClipPlanex(int plane, int[] equation, int offset)
    {
        gl11.glClipPlanex(plane, equation, offset);
    }

    public void glClipPlanex(int plane, IntBuffer equation)
    {
        gl11.glClipPlanex(plane, equation);
    }

    public void glColor4ub(byte red, byte green, byte blue, byte alpha)
    {
        gl11.glColor4ub(red, green, blue, alpha);
    }

    public void glColorPointer(int size, int type, int stride, int offset)
    {
        gl11.glColorPointer(size, type, stride, offset);
    }

    public void glDeleteBuffers(int n, int[] buffers, int offset)
    {
        gl11.glDeleteBuffers(n, buffers, offset);
    }

    public void glDeleteBuffers(int n, IntBuffer buffers)
    {
        gl11.glDeleteBuffers(n, buffers);
    }

    public void glDrawElements(int mode, int count, int type, int offset)
    {
        gl11.glDrawElements(mode, count, type, offset);
    }

    public void glGenBuffers(int n, int[] buffers, int offset)
    {
        gl11.glGenBuffers(n, buffers, offset);
    }

    public void glGenBuffers(int n, IntBuffer buffers)
    {
        gl11.glGenBuffers(n, buffers);
    }

    public void glGetBooleanv(int pname, boolean[] params, int offset)
    {
        gl11.glGetBooleanv(pname, params, offset);
    }

    public void glGetBooleanv(int pname, IntBuffer params)
    {
        gl11.glGetBooleanv(pname, params);
    }

    public void glGetBufferParameteriv(int target, int pname, int[] params, int offset)
    {
        gl11.glGetBufferParameteriv(target, pname, params, offset);
    }

    public void glGetBufferParameteriv(int target, int pname, IntBuffer params)
    {
        gl11.glGetBufferParameteriv(target, pname, params);
    }

    public void glGetClipPlanef(int pname, float[] eqn, int offset)
    {
        gl11.glGetClipPlanef(pname, eqn, offset);
    }

    public void glGetClipPlanef(int pname, FloatBuffer eqn)
    {
        gl11.glGetClipPlanef(pname, eqn);
    }

    public void glGetClipPlanex(int pname, int[] eqn, int offset)
    {
        gl11.glGetClipPlanex(pname, eqn, offset);
    }

    public void glGetClipPlanex(int pname, IntBuffer eqn)
    {
        gl11.glGetClipPlanex(pname, eqn);
    }

    public void glGetFixedv(int pname, int[] params, int offset)
    {
        gl11.glGetFixedv(pname, params, offset);
    }

    public void glGetFixedv(int pname, IntBuffer params)
    {
        gl11.glGetFixedv(pname, params);
    }

    public void glGetFloatv(int pname, float[] params, int offset)
    {
        gl11.glGetFloatv(pname, params, offset);
    }

    public void glGetFloatv(int pname, FloatBuffer params)
    {
        gl11.glGetFloatv(pname, params);
    }

    public void glGetLightfv(int light, int pname, float[] params, int offset)
    {
        gl11.glGetLightfv(light, pname, params, offset);
    }

    public void glGetLightfv(int light, int pname, FloatBuffer params)
    {
        gl11.glGetLightfv(light, pname, params);
    }

    public void glGetLightxv(int light, int pname, int[] params, int offset)
    {
        gl11.glGetLightxv(light, pname, params, offset);
    }

    public void glGetLightxv(int light, int pname, IntBuffer params)
    {
        gl11.glGetLightxv(light, pname, params);
    }

    public void glGetMaterialfv(int face, int pname, float[] params, int offset)
    {
        gl11.glGetMaterialfv(face, pname, params, offset);
    }

    public void glGetMaterialfv(int face, int pname, FloatBuffer params)
    {
        gl11.glGetMaterialfv(face, pname, params);
    }

    public void glGetMaterialxv(int face, int pname, int[] params, int offset)
    {
        gl11.glGetMaterialxv(face, pname, params, offset);
    }

    public void glGetMaterialxv(int face, int pname, IntBuffer params)
    {
        gl11.glGetMaterialxv(face, pname, params);
    }

    public void glGetPointerv(int pname, Buffer[] params)
    {
        gl11.glGetPointerv(pname, params);
    }

    public void glGetTexEnviv(int env, int pname, int[] params, int offset)
    {
        gl11.glGetTexEnviv(env, pname, params, offset);
    }

    public void glGetTexEnviv(int env, int pname, IntBuffer params)
    {
        gl11.glGetTexEnviv(env, pname, params);
    }

    public void glGetTexEnvxv(int env, int pname, int[] params, int offset)
    {
        gl11.glGetTexEnvxv(env, pname, params, offset);
    }

    public void glGetTexEnvxv(int env, int pname, IntBuffer params)
    {
        gl11.glGetTexEnvxv(env, pname, params);
    }

    public void glGetTexParameterfv(int target, int pname, float[] params, int offset)
    {
        gl11.glGetTexParameterfv(target, pname, params, offset);
    }

    public void glGetTexParameterfv(int target, int pname, FloatBuffer params)
    {
        gl11.glGetTexParameterfv(target, pname, params);
    }

    public void glGetTexParameteriv(int target, int pname, int[] params, int offset)
    {
        gl11.glGetTexParameteriv(target, pname, params, offset);
    }

    public void glGetTexParameteriv(int target, int pname, IntBuffer params)
    {
        gl11.glGetTexParameteriv(target, pname, params);
    }

    public void glGetTexParameterxv(int target, int pname, int[] params, int offset)
    {
        gl11.glGetTexParameterxv(target, pname, params, offset);
    }

    public void glGetTexParameterxv(int target, int pname, IntBuffer params)
    {
        gl11.glGetTexParameterxv(target, pname, params);
    }

    public boolean glIsBuffer(int buffer)
    {
        return gl11.glIsBuffer(buffer);
    }

    public boolean glIsEnabled(int cap)
    {
        return gl11.glIsEnabled(cap);
    }

    public boolean glIsTexture(int texture)
    {
        return gl11.glIsTexture(texture);
    }

    public void glNormalPointer(int type, int stride, int offset)
    {
        gl11.glNormalPointer(type, stride, offset);
    }

    public void glPointParameterf(int pname, float param)
    {
        gl11.glPointParameterf(pname, param);
    }

    public void glPointParameterfv(int pname, float[] params, int offset)
    {
        gl11.glPointParameterfv(pname, params, offset);
    }

    public void glPointParameterfv(int pname, FloatBuffer params)
    {
        gl11.glPointParameterfv(pname, params);
    }

    public void glPointParameterx(int pname, int param)
    {
        gl11.glPointParameterx(pname, param);
    }

    public void glPointParameterxv(int pname, int[] params, int offset)
    {
        gl11.glPointParameterxv(pname, params, offset);
    }

    public void glPointParameterxv(int pname, IntBuffer params)
    {
        gl11.glPointParameterxv(pname, params);
    }

    public void glPointSizePointerOES(int type, int stride, Buffer pointer)
    {
        gl11.glPointSizePointerOES(type, stride, pointer);
    }

    public void glTexCoordPointer(int size, int type, int stride, int offset)
    {
        gl11.glTexCoordPointer(size, type, stride, offset);
    }

    public void glTexEnvi(int target, int pname, int param)
    {
        gl11.glTexEnvi(target, pname, param);
    }

    public void glTexEnviv(int target, int pname, int[] params, int offset)
    {
        gl11.glTexEnviv(target, pname, params, offset);
    }

    public void glTexEnviv(int target, int pname, IntBuffer params)
    {
        gl11.glTexEnviv(target, pname, params);
    }

    public void glTexParameterfv(int target, int pname, float[] params, int offset)
    {
        gl11.glTexParameterfv(target, pname, params, offset);
    }

    public void glTexParameterfv(int target, int pname, FloatBuffer params)
    {
        gl11.glTexParameterfv(target, pname, params);
    }

    public void glTexParameteri(int target, int pname, int param)
    {
        gl11.glTexParameteri(target, pname, param);
    }

    public void glTexParameteriv(int target, int pname, int[] params, int offset)
    {
        gl11.glTexParameteriv(target, pname, params, offset);
    }

    public void glTexParameteriv(int target, int pname, IntBuffer params)
    {
        gl11.glTexParameteriv(target, pname, params);
    }

    public void glTexParameterxv(int target, int pname, int[] params, int offset)
    {
        gl11.glTexParameterxv(target, pname, params, offset);
    }

    public void glTexParameterxv(int target, int pname, IntBuffer params)
    {
        gl11.glTexParameterxv(target, pname, params);
    }

    public void glVertexPointer(int size, int type, int stride, int offset)
    {
        gl11.glVertexPointer(size, type, stride, offset);
    }
}
