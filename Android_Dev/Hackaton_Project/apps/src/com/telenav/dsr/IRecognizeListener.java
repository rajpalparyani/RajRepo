/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * IRecognizeListener.java
 *
 */
package com.telenav.dsr;

import com.telenav.data.serverproxy.impl.IDsrStreamProxy;


/**
 *@author bduan
 *@date 2011-1-6
 */
public interface IRecognizeListener
{
    public void recognizeStatusUpdate(int status, IDsrStreamProxy dsrStreamProxy);
}
