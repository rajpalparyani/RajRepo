/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * RimGL10.java
 *
 */
package com.telenav.tnui.widget.rim;

import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.microedition.khronos.opengles.GL10;

import com.telenav.tnui.opengles.TnGL10;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2010-11-10
 */
class RimGL10 implements TnGL10
{
    protected GL10 gl;
    
    public void setGL(GL10 gl)
    {
        this.gl = gl;
    }
    
    public void glActiveTexture(int texture)
    {
        gl.glActiveTexture(texture);
    }

    public void glAlphaFunc(int func, float ref)
    {
        gl.glAlphaFunc(func, ref);
    }

    public void glAlphaFuncx(int func, int ref)
    {
        gl.glAlphaFuncx(func, ref);
    }

    public void glBindTexture(int target, int texture)
    {
        gl.glBindTexture(target, texture);
    }

    public void glBlendFunc(int sfactor, int dfactor)
    {
        gl.glBlendFunc(sfactor, dfactor);
    }

    public void glClear(int mask)
    {
        gl.glClear(mask);
    }

    public void glClearColor(float red, float green, float blue, float alpha)
    {
        gl.glClearColor(red, green, blue, alpha);
    }

    public void glClearColorx(int red, int green, int blue, int alpha)
    {
        gl.glClearColorx(red, green, blue, alpha);
    }

    public void glClearDepthf(float depth)
    {
        gl.glClearDepthf(depth);
    }

    public void glClearDepthx(int depth)
    {
        gl.glClearDepthx(depth);
    }

    public void glClearStencil(int s)
    {
        gl.glClearStencil(s);
    }

    public void glClientActiveTexture(int texture)
    {
        gl.glClientActiveTexture(texture);
    }

    public void glColor4f(float red, float green, float blue, float alpha)
    {
        gl.glColor4f(red, green, blue, alpha);
    }

    public void glColor4x(int red, int green, int blue, int alpha)
    {
        gl.glColor4x(red, green, blue, alpha);
    }

    public void glColorMask(boolean red, boolean green, boolean blue, boolean alpha)
    {
        gl.glColorMask(red, green, blue, alpha);
    }

    public void glColorPointer(int size, int type, int stride, Buffer pointer)
    {
        gl.glColorPointer(size, type, stride, pointer);
    }

    public void glCompressedTexImage2D(int target, int level, int internalformat, int width, int height, int border, int imageSize,
            Buffer data)
    {
        gl.glCompressedTexImage2D(target, level, internalformat, width, height, border, imageSize, data);
    }

    public void glCompressedTexSubImage2D(int target, int level, int xoffset, int yoffset, int width, int height, int format,
            int imageSize, Buffer data)
    {
        gl.glCompressedTexSubImage2D(target, level, xoffset, yoffset, width, height, format, imageSize, data);
    }

    public void glCopyTexImage2D(int target, int level, int internalformat, int x, int y, int width, int height, int border)
    {
        gl.glCopyTexImage2D(target, level, internalformat, x, y, width, height, border);
    }

    public void glCopyTexSubImage2D(int target, int level, int xoffset, int yoffset, int x, int y, int width, int height)
    {
        gl.glCopyTexSubImage2D(target, level, xoffset, yoffset, x, y, width, height);
    }

    public void glCullFace(int mode)
    {
        gl.glCullFace(mode);
    }

    public void glDeleteTextures(int n, int[] textures, int offset)
    {
        gl.glDeleteTextures(n, textures, offset);
    }

    public void glDeleteTextures(int n, IntBuffer textures)
    {
        gl.glDeleteTextures(n, textures);
    }

    public void glDepthFunc(int func)
    {
        gl.glDepthFunc(func);
    }

    public void glDepthMask(boolean flag)
    {
        gl.glDepthMask(flag);
    }

    public void glDepthRangef(float zNear, float zFar)
    {
        gl.glDepthRangef(zNear, zFar);
    }

    public void glDepthRangex(int zNear, int zFar)
    {
        gl.glDepthRangex(zNear, zFar);
    }

    public void glDisable(int cap)
    {
        gl.glDisable(cap);
    }

    public void glDisableClientState(int array)
    {
        gl.glDisableClientState(array);
    }

    public void glDrawArrays(int mode, int first, int count)
    {
        gl.glDrawArrays(mode, first, count);
    }

    public void glDrawElements(int mode, int count, int type, Buffer indices)
    {
        gl.glDrawElements(mode, count, type, indices);
    }

    public void glEnable(int cap)
    {
        gl.glEnable(cap);
    }

    public void glEnableClientState(int array)
    {
        gl.glEnableClientState(array);
    }

    public void glFinish()
    {
        gl.glFinish();
    }

    public void glFlush()
    {
        gl.glFlush();
    }

    public void glFogf(int pname, float param)
    {
        gl.glFogf(pname, param);
    }

    public void glFogfv(int pname, float[] params, int offset)
    {
        gl.glFogfv(pname, params, offset);
    }

    public void glFogfv(int pname, FloatBuffer params)
    {
        gl.glFogfv(pname, params);
    }

    public void glFogx(int pname, int param)
    {
        gl.glFogx(pname, param);
    }

    public void glFogxv(int pname, int[] params, int offset)
    {
        gl.glFogxv(pname, params, offset);
    }

    public void glFogxv(int pname, IntBuffer params)
    {
        gl.glFogxv(pname, params);
    }

    public void glFrontFace(int mode)
    {
        gl.glFrontFace(mode);
    }

    public void glFrustumf(float left, float right, float bottom, float top, float zNear, float zFar)
    {
        gl.glFrustumf(left, right, bottom, top, zNear, zFar);
    }

    public void glFrustumx(int left, int right, int bottom, int top, int zNear, int zFar)
    {
        gl.glFrustumx(left, right, bottom, top, zNear, zFar);
    }

    public void glGenTextures(int n, int[] textures, int offset)
    {
        gl.glGenTextures(n, textures, offset);
    }

    public void glGenTextures(int n, IntBuffer textures)
    {
        gl.glGenTextures(n, textures);
    }

    public int glGetError()
    {
        return gl.glGetError();
    }

    public void glGetIntegerv(int pname, int[] params, int offset)
    {
        gl.glGetIntegerv(pname, params, offset);
    }

    public void glGetIntegerv(int pname, IntBuffer params)
    {
        gl.glGetIntegerv(pname, params);
    }

    public String glGetString(int name)
    {
        return gl.glGetString(name);
    }

    public void glHint(int target, int mode)
    {
        gl.glHint(target, mode);
    }

    public void glLightModelf(int pname, float param)
    {
        gl.glLightModelf(pname, param);
    }

    public void glLightModelfv(int pname, float[] params, int offset)
    {
        gl.glLightModelfv(pname, params, offset);
    }

    public void glLightModelfv(int pname, FloatBuffer params)
    {
        gl.glLightModelfv(pname, params);
    }

    public void glLightModelx(int pname, int param)
    {
        gl.glLightModelx(pname, param);
    }

    public void glLightModelxv(int pname, int[] params, int offset)
    {
        gl.glLightModelxv(pname, params, offset);
    }

    public void glLightModelxv(int pname, IntBuffer params)
    {
        gl.glLightModelxv(pname, params);
    }

    public void glLightf(int light, int pname, float param)
    {
        gl.glLightf(light, pname, param);
    }

    public void glLightfv(int light, int pname, float[] params, int offset)
    {
        gl.glLightfv(light, pname, params, offset);
    }

    public void glLightfv(int light, int pname, FloatBuffer params)
    {
        gl.glLightfv(light, pname, params);
    }

    public void glLightx(int light, int pname, int param)
    {
        gl.glLightx(light, pname, param);
    }

    public void glLightxv(int light, int pname, int[] params, int offset)
    {
        gl.glLightxv(light, pname, params, offset);
    }

    public void glLightxv(int light, int pname, IntBuffer params)
    {
        gl.glLightxv(light, pname, params);
    }

    public void glLineWidth(float width)
    {
        gl.glLineWidth(width);
    }

    public void glLineWidthx(int width)
    {
        gl.glLineWidthx(width);
    }

    public void glLoadIdentity()
    {
        gl.glLoadIdentity();
    }

    public void glLoadMatrixf(float[] m, int offset)
    {
        gl.glLoadMatrixf(m, offset);
    }

    public void glLoadMatrixf(FloatBuffer m)
    {
        gl.glLoadMatrixf(m);
    }

    public void glLoadMatrixx(int[] m, int offset)
    {
        gl.glLoadMatrixx(m, offset);
    }

    public void glLoadMatrixx(IntBuffer m)
    {
        gl.glLoadMatrixx(m);
    }

    public void glLogicOp(int opcode)
    {
        gl.glLogicOp(opcode);
    }

    public void glMaterialf(int face, int pname, float param)
    {
        gl.glMaterialf(face, pname, param);
    }

    public void glMaterialfv(int face, int pname, float[] params, int offset)
    {
        gl.glMaterialfv(face, pname, params, offset);
    }

    public void glMaterialfv(int face, int pname, FloatBuffer params)
    {
        gl.glMaterialfv(face, pname, params);
    }

    public void glMaterialx(int face, int pname, int param)
    {
        gl.glMaterialx(face, pname, param);
    }

    public void glMaterialxv(int face, int pname, int[] params, int offset)
    {
        gl.glMaterialxv(face, pname, params, offset);
    }

    public void glMaterialxv(int face, int pname, IntBuffer params)
    {
        gl.glMaterialxv(face, pname, params);
    }

    public void glMatrixMode(int mode)
    {
        gl.glMatrixMode(mode);
    }

    public void glMultMatrixf(float[] m, int offset)
    {
        gl.glMultMatrixf(m, offset);
    }

    public void glMultMatrixf(FloatBuffer m)
    {
        gl.glMultMatrixf(m);
    }

    public void glMultMatrixx(int[] m, int offset)
    {
        gl.glMultMatrixx(m, offset);
    }

    public void glMultMatrixx(IntBuffer m)
    {
        gl.glMultMatrixx(m);
    }

    public void glMultiTexCoord4f(int target, float s, float t, float r, float q)
    {
        gl.glMultiTexCoord4f(target, s, t, r, q);
    }

    public void glMultiTexCoord4x(int target, int s, int t, int r, int q)
    {
        gl.glMultiTexCoord4x(target, s, t, r, q);
    }

    public void glNormal3f(float nx, float ny, float nz)
    {
        gl.glNormal3f(nx, ny, nz);
    }

    public void glNormal3x(int nx, int ny, int nz)
    {
        gl.glNormal3x(nx, ny, nz);
    }

    public void glNormalPointer(int type, int stride, Buffer pointer)
    {
        gl.glNormalPointer(type, stride, pointer);
    }

    public void glOrthof(float left, float right, float bottom, float top, float zNear, float zFar)
    {
        gl.glOrthof(left, right, bottom, top, zNear, zFar);
    }

    public void glOrthox(int left, int right, int bottom, int top, int zNear, int zFar)
    {
        gl.glOrthox(left, right, bottom, top, zNear, zFar);
    }

    public void glPixelStorei(int pname, int param)
    {
        gl.glPixelStorei(pname, param);
    }

    public void glPointSize(float size)
    {
        gl.glPointSize(size);
    }

    public void glPointSizex(int size)
    {
        gl.glPointSizex(size);
    }

    public void glPolygonOffset(float factor, float units)
    {
        gl.glPolygonOffset(factor, units);
    }

    public void glPolygonOffsetx(int factor, int units)
    {
        gl.glPolygonOffsetx(factor, units);
    }

    public void glPopMatrix()
    {
        gl.glPopMatrix();
    }

    public void glPushMatrix()
    {
        gl.glPushMatrix();
    }

    public void glReadPixels(int x, int y, int width, int height, int format, int type, Buffer pixels)
    {
        gl.glReadPixels(x, y, width, height, format, type, pixels);
    }

    public void glRotatef(float angle, float x, float y, float z)
    {
        gl.glRotatef(angle, x, y, z);
    }

    public void glRotatex(int angle, int x, int y, int z)
    {
        gl.glRotatex(angle, x, y, z);
    }

    public void glSampleCoverage(float value, boolean invert)
    {
        gl.glSampleCoverage(value, invert);
    }

    public void glSampleCoveragex(int value, boolean invert)
    {
        gl.glSampleCoveragex(value, invert);
    }

    public void glScalef(float x, float y, float z)
    {
        gl.glScalef(x, y, z);
    }

    public void glScalex(int x, int y, int z)
    {
        gl.glScalex(x, y, z);
    }

    public void glScissor(int x, int y, int width, int height)
    {
        gl.glScissor(x, y, width, height);
    }

    public void glShadeModel(int mode)
    {
        gl.glShadeModel(mode);
    }

    public void glStencilFunc(int func, int ref, int mask)
    {
        gl.glStencilFunc(func, ref, mask);
    }

    public void glStencilMask(int mask)
    {
        gl.glStencilMask(mask);
    }

    public void glStencilOp(int fail, int zfail, int zpass)
    {
        gl.glStencilOp(fail, zfail, zpass);
    }

    public void glTexCoordPointer(int size, int type, int stride, Buffer pointer)
    {
        gl.glTexCoordPointer(size, type, stride, pointer);
    }

    public void glTexEnvf(int target, int pname, float param)
    {
        gl.glTexEnvf(target, pname, param);
    }

    public void glTexEnvfv(int target, int pname, float[] params, int offset)
    {
        gl.glTexEnvfv(target, pname, params, offset);
    }

    public void glTexEnvfv(int target, int pname, FloatBuffer params)
    {
        gl.glTexEnvfv(target, pname, params);
    }

    public void glTexEnvx(int target, int pname, int param)
    {
        gl.glTexEnvx(target, pname, param);
    }

    public void glTexEnvxv(int target, int pname, int[] params, int offset)
    {
        gl.glTexEnvxv(target, pname, params, offset);
    }

    public void glTexEnvxv(int target, int pname, IntBuffer params)
    {
        gl.glTexEnvxv(target, pname, params);
    }

    public void glTexImage2D(int target, int level, int internalformat, int width, int height, int border, int format, int type,
            Buffer pixels)
    {
        gl.glTexImage2D(target, level, internalformat, width, height, border, format, type, pixels);
    }

    public void glTexParameterf(int target, int pname, float param)
    {
        gl.glTexParameterf(target, pname, param);
    }

    public void glTexParameterx(int target, int pname, int param)
    {
        gl.glTexParameterx(target, pname, param);
    }

    public void glTexSubImage2D(int target, int level, int xoffset, int yoffset, int width, int height, int format, int type, Buffer pixels)
    {
        gl.glTexSubImage2D(target, level, xoffset, yoffset, width, height, format, type, pixels);
    }

    public void glTranslatef(float x, float y, float z)
    {
        gl.glTranslatef(x, y, z);
    }

    public void glTranslatex(int x, int y, int z)
    {
        gl.glTranslatex(x, y, z);
    }

    public void glVertexPointer(int size, int type, int stride, Buffer pointer)
    {
        gl.glVertexPointer(size, type, stride, pointer);
    }

    public void glViewport(int x, int y, int width, int height)
    {
        gl.glViewport(x, y, width, height);
    }

    public Object getNativeGL()
    {
        return gl;
    }
}
