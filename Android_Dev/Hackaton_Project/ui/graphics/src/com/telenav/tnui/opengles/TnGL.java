/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * TnGL.java
 *
 */
package com.telenav.tnui.opengles;

/**
 * The GL interface is the parent interface for the Java(TM) programming language bindings for OpenGL(R) ES 1.0, 1.1,
 * and extensions.<br />
 * <br />
 * The documentation in this interface and its subinterfaces is normative with respect to instance variable names and
 * values, method names and signatures, and exception behavior. The remaining documentation is placed here for
 * convenience and does not replace the normative documentation found in the OpenGL ES 1.0 and 1.1 specifications,
 * relevant extension specifications, and the OpenGL specification versions referenced by any of the preceding
 * specifications.<br />
 * <br />
 * A GL object is obtained by calling EGLContext.getGL(). The returned object will implement either GL10 or GL11, plus
 * any available extension interfaces (such as GL10Ext, GL11Ext, or GL11ExtensionPack). The returned object must be cast
 * to the appropriate interface (possibly following an instanceof check) in order to call GL methods.<br />
 * <br />
 * A common superinterface is used for OpenGL ES 1.0, OpenGL ES 1.1, and Khronos-defined core extensions. In order to
 * determine if the implementation supports GL 1.1, call glGetString(GL.GL_VERSION).<br />
 * <br />
 * Some methods defined in subinterfaces are available only on OpenGL ES 1.1. The descriptions of these functions are
 * marked "(1.1 only)." Similarly, some methods behave slightly differently across OpenGL ES versions. The sections that
 * differ are marked "(1.0 only)" and "(1.1 only)" as appropriate. Some methods have an additional section marked
 * "1.0 Notes" or "1.1 Notes" that applies to the corresponding engine version.<br />
 * <br />
 * Some extensions are defined as a core part of the OpenGL ES specification (they are extensions relative to desktop
 * OpenGL). These functions are treated as normal portions of OpenGL ES, although they may still be queried as
 * extensions using the normal OpenGL ES query mechanisms.<br />
 * <br />
 * Extensions may allow some arguments to take on values other than those listed in this specification. Implementations
 * that provide a given extension may pass such values to the underlying engine.<br />
 * <br />
 * Optional profile extensions defined as of the creation of this specification may be found in the GL10Ext, GL11Ext,
 * and GL11ExtensionPack interfaces.
 * 
 *@author fqming (fqming@telenav.cn)
 *@date 2010-11-18
 */
public interface TnGL
{
    /**
     * Retrieve the native gl object.
     * 
     * @return Object
     */
    public Object getNativeGL();
}
