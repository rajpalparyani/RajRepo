/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * AndroidGLES10.java
 *
 */
package com.telenav.tnui.widget.android;

import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import android.opengl.GLES10;

/**
 *@author fqming (fqming@telenav.cn)
 *@date Sep 7, 2010
 */
class AndroidGLES10 extends AndroidGL10
{
    
    public void glActiveTexture(int texture)
    {
        GLES10.glActiveTexture(texture);
    }

    public void glAlphaFunc(int func, float ref)
    {
        GLES10.glAlphaFunc(func, ref);
    }

    public void glAlphaFuncx(int func, int ref)
    {
        GLES10.glAlphaFuncx(func, ref);
    }

    public void glBindTexture(int target, int texture)
    {
        GLES10.glBindTexture(target, texture);
    }

    public void glBlendFunc(int sfactor, int dfactor)
    {
        GLES10.glBlendFunc(sfactor, dfactor);
    }

    public void glClear(int mask)
    {
        GLES10.glClear(mask);
    }

    public void glClearColor(float red, float green, float blue, float alpha)
    {
        GLES10.glClearColor(red, green, blue, alpha);
    }

    public void glClearColorx(int red, int green, int blue, int alpha)
    {
        GLES10.glClearColorx(red, green, blue, alpha);
    }

    public void glClearDepthf(float depth)
    {
        GLES10.glClearDepthf(depth);
    }

    public void glClearDepthx(int depth)
    {
        GLES10.glClearDepthx(depth);
    }

    public void glClearStencil(int s)
    {
        GLES10.glClearStencil(s);
    }

    public void glClientActiveTexture(int texture)
    {
        GLES10.glClientActiveTexture(texture);
    }

    public void glColor4f(float red, float green, float blue, float alpha)
    {
        GLES10.glColor4f(red, green, blue, alpha);
    }

    public void glColor4x(int red, int green, int blue, int alpha)
    {
        GLES10.glColor4x(red, green, blue, alpha);
    }

    public void glColorMask(boolean red, boolean green, boolean blue, boolean alpha)
    {
        GLES10.glColorMask(red, green, blue, alpha);
    }

    public void glColorPointer(int size, int type, int stride, Buffer pointer)
    {
        GLES10.glColorPointer(size, type, stride, pointer);
    }

    public void glCompressedTexImage2D(int target, int level, int internalformat, int width, int height, int border, int imageSize,
            Buffer data)
    {
        GLES10.glCompressedTexImage2D(target, level, internalformat, width, height, border, imageSize, data);
    }

    public void glCompressedTexSubImage2D(int target, int level, int xoffset, int yoffset, int width, int height, int format,
            int imageSize, Buffer data)
    {
        GLES10.glCompressedTexSubImage2D(target, level, xoffset, yoffset, width, height, format, imageSize, data);
    }

    public void glCopyTexImage2D(int target, int level, int internalformat, int x, int y, int width, int height, int border)
    {
        GLES10.glCopyTexImage2D(target, level, internalformat, x, y, width, height, border);
    }

    public void glCopyTexSubImage2D(int target, int level, int xoffset, int yoffset, int x, int y, int width, int height)
    {
        GLES10.glCopyTexSubImage2D(target, level, xoffset, yoffset, x, y, width, height);
    }

    public void glCullFace(int mode)
    {
        GLES10.glCullFace(mode);
    }

    public void glDeleteTextures(int n, int[] textures, int offset)
    {
        GLES10.glDeleteTextures(n, textures, offset);
    }

    public void glDeleteTextures(int n, IntBuffer textures)
    {
        GLES10.glDeleteTextures(n, textures);
    }

    public void glDepthFunc(int func)
    {
        GLES10.glDepthFunc(func);
    }

    public void glDepthMask(boolean flag)
    {
        GLES10.glDepthMask(flag);
    }

    public void glDepthRangef(float zNear, float zFar)
    {
        GLES10.glDepthRangef(zNear, zFar);
    }

    public void glDepthRangex(int zNear, int zFar)
    {
        GLES10.glDepthRangex(zNear, zFar);
    }

    public void glDisable(int cap)
    {
        GLES10.glDisable(cap);
    }

    public void glDisableClientState(int array)
    {
        GLES10.glDisableClientState(array);
    }

    public void glDrawArrays(int mode, int first, int count)
    {
        GLES10.glDrawArrays(mode, first, count);
    }

    public void glDrawElements(int mode, int count, int type, Buffer indices)
    {
        GLES10.glDrawElements(mode, count, type, indices);
    }

    public void glEnable(int cap)
    {
        GLES10.glEnable(cap);
    }

    public void glEnableClientState(int array)
    {
        GLES10.glEnableClientState(array);
    }

    public void glFinish()
    {
        GLES10.glFinish();
    }

    public void glFlush()
    {
        GLES10.glFlush();
    }

    public void glFogf(int pname, float param)
    {
        GLES10.glFogf(pname, param);
    }

    public void glFogfv(int pname, float[] params, int offset)
    {
        GLES10.glFogfv(pname, params, offset);
    }

    public void glFogfv(int pname, FloatBuffer params)
    {
        GLES10.glFogfv(pname, params);
    }

    public void glFogx(int pname, int param)
    {
        GLES10.glFogx(pname, param);
    }

    public void glFogxv(int pname, int[] params, int offset)
    {
        GLES10.glFogxv(pname, params, offset);
    }

    public void glFogxv(int pname, IntBuffer params)
    {
        GLES10.glFogxv(pname, params);
    }

    public void glFrontFace(int mode)
    {
        GLES10.glFrontFace(mode);
    }

    public void glFrustumf(float left, float right, float bottom, float top, float zNear, float zFar)
    {
        GLES10.glFrustumf(left, right, bottom, top, zNear, zFar);
    }

    public void glFrustumx(int left, int right, int bottom, int top, int zNear, int zFar)
    {
        GLES10.glFrustumx(left, right, bottom, top, zNear, zFar);
    }

    public void glGenTextures(int n, int[] textures, int offset)
    {
        GLES10.glGenTextures(n, textures, offset);
    }

    public void glGenTextures(int n, IntBuffer textures)
    {
        GLES10.glGenTextures(n, textures);
    }

    public int glGetError()
    {
        return GLES10.glGetError();
    }

    public void glGetIntegerv(int pname, int[] params, int offset)
    {
        GLES10.glGetIntegerv(pname, params, offset);
    }

    public void glGetIntegerv(int pname, IntBuffer params)
    {
        GLES10.glGetIntegerv(pname, params);
    }

    public String glGetString(int name)
    {
        return GLES10.glGetString(name);
    }

    public void glHint(int target, int mode)
    {
        GLES10.glHint(target, mode);
    }

    public void glLightModelf(int pname, float param)
    {
        GLES10.glLightModelf(pname, param);
    }

    public void glLightModelfv(int pname, float[] params, int offset)
    {
        GLES10.glLightModelfv(pname, params, offset);
    }

    public void glLightModelfv(int pname, FloatBuffer params)
    {
        GLES10.glLightModelfv(pname, params);
    }

    public void glLightModelx(int pname, int param)
    {
        GLES10.glLightModelx(pname, param);
    }

    public void glLightModelxv(int pname, int[] params, int offset)
    {
        GLES10.glLightModelxv(pname, params, offset);
    }

    public void glLightModelxv(int pname, IntBuffer params)
    {
        GLES10.glLightModelxv(pname, params);
    }

    public void glLightf(int light, int pname, float param)
    {
        GLES10.glLightf(light, pname, param);
    }

    public void glLightfv(int light, int pname, float[] params, int offset)
    {
        GLES10.glLightfv(light, pname, params, offset);
    }

    public void glLightfv(int light, int pname, FloatBuffer params)
    {
        GLES10.glLightfv(light, pname, params);
    }

    public void glLightx(int light, int pname, int param)
    {
        GLES10.glLightx(light, pname, param);
    }

    public void glLightxv(int light, int pname, int[] params, int offset)
    {
        GLES10.glLightxv(light, pname, params, offset);
    }

    public void glLightxv(int light, int pname, IntBuffer params)
    {
        GLES10.glLightxv(light, pname, params);
    }

    public void glLineWidth(float width)
    {
        GLES10.glLineWidth(width);
    }

    public void glLineWidthx(int width)
    {
        GLES10.glLineWidthx(width);
    }

    public void glLoadIdentity()
    {
        GLES10.glLoadIdentity();
    }

    public void glLoadMatrixf(float[] m, int offset)
    {
        GLES10.glLoadMatrixf(m, offset);
    }

    public void glLoadMatrixf(FloatBuffer m)
    {
        GLES10.glLoadMatrixf(m);
    }

    public void glLoadMatrixx(int[] m, int offset)
    {
        GLES10.glLoadMatrixx(m, offset);
    }

    public void glLoadMatrixx(IntBuffer m)
    {
        GLES10.glLoadMatrixx(m);
    }

    public void glLogicOp(int opcode)
    {
        GLES10.glLogicOp(opcode);
    }

    public void glMaterialf(int face, int pname, float param)
    {
        GLES10.glMaterialf(face, pname, param);
    }

    public void glMaterialfv(int face, int pname, float[] params, int offset)
    {
        GLES10.glMaterialfv(face, pname, params, offset);
    }

    public void glMaterialfv(int face, int pname, FloatBuffer params)
    {
        GLES10.glMaterialfv(face, pname, params);
    }

    public void glMaterialx(int face, int pname, int param)
    {
        GLES10.glMaterialx(face, pname, param);
    }

    public void glMaterialxv(int face, int pname, int[] params, int offset)
    {
        GLES10.glMaterialxv(face, pname, params, offset);
    }

    public void glMaterialxv(int face, int pname, IntBuffer params)
    {
        GLES10.glMaterialxv(face, pname, params);
    }

    public void glMatrixMode(int mode)
    {
        GLES10.glMatrixMode(mode);
    }

    public void glMultMatrixf(float[] m, int offset)
    {
        GLES10.glMultMatrixf(m, offset);
    }

    public void glMultMatrixf(FloatBuffer m)
    {
        GLES10.glMultMatrixf(m);
    }

    public void glMultMatrixx(int[] m, int offset)
    {
        GLES10.glMultMatrixx(m, offset);
    }

    public void glMultMatrixx(IntBuffer m)
    {
        GLES10.glMultMatrixx(m);
    }

    public void glMultiTexCoord4f(int target, float s, float t, float r, float q)
    {
        GLES10.glMultiTexCoord4f(target, s, t, r, q);
    }

    public void glMultiTexCoord4x(int target, int s, int t, int r, int q)
    {
        GLES10.glMultiTexCoord4x(target, s, t, r, q);
    }

    public void glNormal3f(float nx, float ny, float nz)
    {
        GLES10.glNormal3f(nx, ny, nz);
    }

    public void glNormal3x(int nx, int ny, int nz)
    {
        GLES10.glNormal3x(nx, ny, nz);
    }

    public void glNormalPointer(int type, int stride, Buffer pointer)
    {
        GLES10.glNormalPointer(type, stride, pointer);
    }

    public void glOrthof(float left, float right, float bottom, float top, float zNear, float zFar)
    {
        GLES10.glOrthof(left, right, bottom, top, zNear, zFar);
    }

    public void glOrthox(int left, int right, int bottom, int top, int zNear, int zFar)
    {
        GLES10.glOrthox(left, right, bottom, top, zNear, zFar);
    }

    public void glPixelStorei(int pname, int param)
    {
        GLES10.glPixelStorei(pname, param);
    }

    public void glPointSize(float size)
    {
        GLES10.glPointSize(size);
    }

    public void glPointSizex(int size)
    {
        GLES10.glPointSizex(size);
    }

    public void glPolygonOffset(float factor, float units)
    {
        GLES10.glPolygonOffset(factor, units);
    }

    public void glPolygonOffsetx(int factor, int units)
    {
        GLES10.glPolygonOffsetx(factor, units);
    }

    public void glPopMatrix()
    {
        GLES10.glPopMatrix();
    }

    public void glPushMatrix()
    {
        GLES10.glPushMatrix();
    }

    public void glReadPixels(int x, int y, int width, int height, int format, int type, Buffer pixels)
    {
        GLES10.glReadPixels(x, y, width, height, format, type, pixels);
    }

    public void glRotatef(float angle, float x, float y, float z)
    {
        GLES10.glRotatef(angle, x, y, z);
    }

    public void glRotatex(int angle, int x, int y, int z)
    {
        GLES10.glRotatex(angle, x, y, z);
    }

    public void glSampleCoverage(float value, boolean invert)
    {
        GLES10.glSampleCoverage(value, invert);
    }

    public void glSampleCoveragex(int value, boolean invert)
    {
        GLES10.glSampleCoveragex(value, invert);
    }

    public void glScalef(float x, float y, float z)
    {
        GLES10.glScalef(x, y, z);
    }

    public void glScalex(int x, int y, int z)
    {
        GLES10.glScalex(x, y, z);
    }

    public void glScissor(int x, int y, int width, int height)
    {
        GLES10.glScissor(x, y, width, height);
    }

    public void glShadeModel(int mode)
    {
        GLES10.glShadeModel(mode);
    }

    public void glStencilFunc(int func, int ref, int mask)
    {
        GLES10.glStencilFunc(func, ref, mask);
    }

    public void glStencilMask(int mask)
    {
        GLES10.glStencilMask(mask);
    }

    public void glStencilOp(int fail, int zfail, int zpass)
    {
        GLES10.glStencilOp(fail, zfail, zpass);
    }

    public void glTexCoordPointer(int size, int type, int stride, Buffer pointer)
    {
        GLES10.glTexCoordPointer(size, type, stride, pointer);
    }

    public void glTexEnvf(int target, int pname, float param)
    {
        GLES10.glTexEnvf(target, pname, param);
    }

    public void glTexEnvfv(int target, int pname, float[] params, int offset)
    {
        GLES10.glTexEnvfv(target, pname, params, offset);
    }

    public void glTexEnvfv(int target, int pname, FloatBuffer params)
    {
        GLES10.glTexEnvfv(target, pname, params);
    }

    public void glTexEnvx(int target, int pname, int param)
    {
        GLES10.glTexEnvx(target, pname, param);
    }

    public void glTexEnvxv(int target, int pname, int[] params, int offset)
    {
        GLES10.glTexEnvxv(target, pname, params, offset);
    }

    public void glTexEnvxv(int target, int pname, IntBuffer params)
    {
        GLES10.glTexEnvxv(target, pname, params);
    }

    public void glTexImage2D(int target, int level, int internalformat, int width, int height, int border, int format, int type,
            Buffer pixels)
    {
        GLES10.glTexImage2D(target, level, internalformat, width, height, border, format, type, pixels);
    }

    public void glTexParameterf(int target, int pname, float param)
    {
        GLES10.glTexParameterf(target, pname, param);
    }

    public void glTexParameterx(int target, int pname, int param)
    {
        GLES10.glTexParameterx(target, pname, param);
    }

    public void glTexSubImage2D(int target, int level, int xoffset, int yoffset, int width, int height, int format, int type, Buffer pixels)
    {
        GLES10.glTexSubImage2D(target, level, xoffset, yoffset, width, height, format, type, pixels);
    }

    public void glTranslatef(float x, float y, float z)
    {
        GLES10.glTranslatef(x, y, z);
    }

    public void glTranslatex(int x, int y, int z)
    {
        GLES10.glTranslatex(x, y, z);
    }

    public void glVertexPointer(int size, int type, int stride, Buffer pointer)
    {
        GLES10.glVertexPointer(size, type, stride, pointer);
    }

    public void glViewport(int x, int y, int width, int height)
    {
        GLES10.glViewport(x, y, width, height);
    }

}
