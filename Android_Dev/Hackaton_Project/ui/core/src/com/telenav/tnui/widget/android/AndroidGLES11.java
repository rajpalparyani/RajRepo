/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * AndroidGLES11.java
 *
 */
package com.telenav.tnui.widget.android;

import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.microedition.khronos.opengles.GL11;

import com.telenav.tnui.opengles.TnGL11;

import android.opengl.GLES11;

/**
 *@author fqming (fqming@telenav.cn)
 *@date Sep 8, 2010
 */
class AndroidGLES11 extends AndroidGLES10 implements TnGL11
{
    public void glBindBuffer(int target, int buffer)
    {
        GLES11.glBindBuffer(target, buffer);
    }

    public void glBufferData(int target, int size, Buffer data, int usage)
    {
        GLES11.glBufferData(target, size, data, usage);
    }

    public void glBufferSubData(int target, int offset, int size, Buffer data)
    {
        GLES11.glBufferSubData(target, offset, size, data);
    }

    public void glClipPlanef(int plane, float[] equation, int offset)
    {
        GLES11.glClipPlanef(plane, equation, offset);
    }

    public void glClipPlanef(int plane, FloatBuffer equation)
    {
        GLES11.glClipPlanef(plane, equation);
    }

    public void glClipPlanex(int plane, int[] equation, int offset)
    {
        GLES11.glClipPlanex(plane, equation, offset);
    }

    public void glClipPlanex(int plane, IntBuffer equation)
    {
        GLES11.glClipPlanex(plane, equation);
    }

    public void glColor4ub(byte red, byte green, byte blue, byte alpha)
    {
        GLES11.glColor4ub(red, green, blue, alpha);
    }

    public void glColorPointer(int size, int type, int stride, int offset)
    {
        GLES11.glColorPointer(size, type, stride, offset);
    }

    public void glDeleteBuffers(int n, int[] buffers, int offset)
    {
        GLES11.glDeleteBuffers(n, buffers, offset);
    }

    public void glDeleteBuffers(int n, IntBuffer buffers)
    {
        GLES11.glDeleteBuffers(n, buffers);
    }

    public void glDrawElements(int mode, int count, int type, int offset)
    {
        GLES11.glDrawElements(mode, count, type, offset);
    }

    public void glGenBuffers(int n, int[] buffers, int offset)
    {
        GLES11.glGenBuffers(n, buffers, offset);
    }

    public void glGenBuffers(int n, IntBuffer buffers)
    {
        GLES11.glGenBuffers(n, buffers);
    }

    public void glGetBooleanv(int pname, boolean[] params, int offset)
    {
        GLES11.glGetBooleanv(pname, params, offset);
    }

    public void glGetBooleanv(int pname, IntBuffer params)
    {
        GLES11.glGetBooleanv(pname, params);
    }

    public void glGetBufferParameteriv(int target, int pname, int[] params, int offset)
    {
        GLES11.glGetBufferParameteriv(target, pname, params, offset);
    }

    public void glGetBufferParameteriv(int target, int pname, IntBuffer params)
    {
        GLES11.glGetBufferParameteriv(target, pname, params);
    }

    public void glGetClipPlanef(int pname, float[] eqn, int offset)
    {
        GLES11.glGetClipPlanef(pname, eqn, offset);
    }

    public void glGetClipPlanef(int pname, FloatBuffer eqn)
    {
        GLES11.glGetClipPlanef(pname, eqn);
    }

    public void glGetClipPlanex(int pname, int[] eqn, int offset)
    {
        GLES11.glGetClipPlanex(pname, eqn, offset);
    }

    public void glGetClipPlanex(int pname, IntBuffer eqn)
    {
        GLES11.glGetClipPlanex(pname, eqn);
    }

    public void glGetFixedv(int pname, int[] params, int offset)
    {
        GLES11.glGetFixedv(pname, params, offset);
    }

    public void glGetFixedv(int pname, IntBuffer params)
    {
        GLES11.glGetFixedv(pname, params);
    }

    public void glGetFloatv(int pname, float[] params, int offset)
    {
        GLES11.glGetFloatv(pname, params, offset);
    }

    public void glGetFloatv(int pname, FloatBuffer params)
    {
        GLES11.glGetFloatv(pname, params);
    }

    public void glGetLightfv(int light, int pname, float[] params, int offset)
    {
        GLES11.glGetLightfv(light, pname, params, offset);
    }

    public void glGetLightfv(int light, int pname, FloatBuffer params)
    {
        GLES11.glGetLightfv(light, pname, params);
    }

    public void glGetLightxv(int light, int pname, int[] params, int offset)
    {
        GLES11.glGetLightxv(light, pname, params, offset);
    }

    public void glGetLightxv(int light, int pname, IntBuffer params)
    {
        GLES11.glGetLightxv(light, pname, params);
    }

    public void glGetMaterialfv(int face, int pname, float[] params, int offset)
    {
        GLES11.glGetMaterialfv(face, pname, params, offset);
    }

    public void glGetMaterialfv(int face, int pname, FloatBuffer params)
    {
        GLES11.glGetMaterialfv(face, pname, params);
    }

    public void glGetMaterialxv(int face, int pname, int[] params, int offset)
    {
        GLES11.glGetMaterialxv(face, pname, params, offset);
    }

    public void glGetMaterialxv(int face, int pname, IntBuffer params)
    {
        GLES11.glGetMaterialxv(face, pname, params);
    }

    public void glGetTexEnviv(int env, int pname, int[] params, int offset)
    {
        GLES11.glGetTexEnviv(env, pname, params, offset);
    }

    public void glGetTexEnviv(int env, int pname, IntBuffer params)
    {
        GLES11.glGetTexEnviv(env, pname, params);
    }

    public void glGetTexEnvxv(int env, int pname, int[] params, int offset)
    {
        GLES11.glGetTexEnvxv(env, pname, params, offset);
    }

    public void glGetTexEnvxv(int env, int pname, IntBuffer params)
    {
        GLES11.glGetTexEnvxv(env, pname, params);
    }

    public void glGetTexParameterfv(int target, int pname, float[] params, int offset)
    {
        GLES11.glGetTexParameterfv(target, pname, params, offset);
    }

    public void glGetTexParameterfv(int target, int pname, FloatBuffer params)
    {
        GLES11.glGetTexParameterfv(target, pname, params);
    }

    public void glGetTexParameteriv(int target, int pname, int[] params, int offset)
    {
        GLES11.glGetTexParameteriv(target, pname, params, offset);
    }

    public void glGetTexParameteriv(int target, int pname, IntBuffer params)
    {
        GLES11.glGetTexParameteriv(target, pname, params);
    }

    public void glGetTexParameterxv(int target, int pname, int[] params, int offset)
    {
        GLES11.glGetTexParameterxv(target, pname, params, offset);
    }

    public void glGetTexParameterxv(int target, int pname, IntBuffer params)
    {
        GLES11.glGetTexParameterxv(target, pname, params);
    }

    public boolean glIsBuffer(int buffer)
    {
        return GLES11.glIsBuffer(buffer);
    }

    public boolean glIsEnabled(int cap)
    {
        return GLES11.glIsEnabled(cap);
    }

    public boolean glIsTexture(int texture)
    {
        return GLES11.glIsTexture(texture);
    }

    public void glNormalPointer(int type, int stride, int offset)
    {
        GLES11.glNormalPointer(type, stride, offset);
    }

    public void glPointParameterf(int pname, float param)
    {
        GLES11.glPointParameterf(pname, param);
    }

    public void glPointParameterfv(int pname, float[] params, int offset)
    {
        GLES11.glPointParameterfv(pname, params, offset);
    }

    public void glPointParameterfv(int pname, FloatBuffer params)
    {
        GLES11.glPointParameterfv(pname, params);
    }

    public void glPointParameterx(int pname, int param)
    {
        GLES11.glPointParameterx(pname, param);
    }

    public void glPointParameterxv(int pname, int[] params, int offset)
    {
        GLES11.glPointParameterxv(pname, params, offset);
    }

    public void glPointParameterxv(int pname, IntBuffer params)
    {
        GLES11.glPointParameterxv(pname, params);
    }

    public void glPointSizePointerOES(int type, int stride, Buffer pointer)
    {
        GLES11.glPointSizePointerOES(type, stride, pointer);
    }

    public void glTexCoordPointer(int size, int type, int stride, int offset)
    {
        GLES11.glTexCoordPointer(size, type, stride, offset);
    }

    public void glTexEnvi(int target, int pname, int param)
    {
        GLES11.glTexEnvi(target, pname, param);
    }

    public void glTexEnviv(int target, int pname, int[] params, int offset)
    {
        GLES11.glTexEnviv(target, pname, params, offset);
    }

    public void glTexEnviv(int target, int pname, IntBuffer params)
    {
        GLES11.glTexEnviv(target, pname, params);
    }

    public void glTexParameterfv(int target, int pname, float[] params, int offset)
    {
        GLES11.glTexParameterfv(target, pname, params, offset);
    }

    public void glTexParameterfv(int target, int pname, FloatBuffer params)
    {
        GLES11.glTexParameterfv(target, pname, params);
    }

    public void glTexParameteri(int target, int pname, int param)
    {
        GLES11.glTexParameteri(target, pname, param);
    }

    public void glTexParameteriv(int target, int pname, int[] params, int offset)
    {
        GLES11.glTexParameteriv(target, pname, params, offset);
    }

    public void glTexParameteriv(int target, int pname, IntBuffer params)
    {
        GLES11.glTexParameteriv(target, pname, params);
    }

    public void glTexParameterxv(int target, int pname, int[] params, int offset)
    {
        GLES11.glTexParameterxv(target, pname, params, offset);
    }

    public void glTexParameterxv(int target, int pname, IntBuffer params)
    {
        GLES11.glTexParameterxv(target, pname, params);
    }

    public void glVertexPointer(int size, int type, int stride, int offset)
    {
        GLES11.glVertexPointer(size, type, stride, offset);
    }

    public void glGetPointerv(int pname, Buffer[] params)
    {
        ((GL11)this.gl).glGetPointerv(pname, params);
    }
}
