/**
 *
 * Copyright 2013 TeleNav, Inc. All rights reserved.
 * IMapEGLEventListener.java
 *
 */
package com.telenav.ui.citizen.map;

/**
 *@author yren
 *@date 2013-4-22
 *Be used to notify Surfaceview when EGL surface size has been changed
 */
public interface IMapEGLEventListener
{
  public void eglSizeChanged();
}
