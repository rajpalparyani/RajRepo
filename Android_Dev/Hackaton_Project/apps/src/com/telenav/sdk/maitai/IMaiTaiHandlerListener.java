/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * IMaiTaiHandlerCallBack.java
 *
 */
package com.telenav.sdk.maitai;

/**
 * IMaiTaiHandler will invoke this interface, after finish maitai request flow
 *@author qli
 *@date 2010-12-13
 */
public interface IMaiTaiHandlerListener
{
    /** 
     * call this method after finish maitai request flow.
     */
    public void maitaiHandleFinished();
}
