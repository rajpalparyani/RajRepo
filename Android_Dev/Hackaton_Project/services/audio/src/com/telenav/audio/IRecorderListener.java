/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * IRecorderListener.java
 *
 */
package com.telenav.audio;

/**
 * A call back for recorder.
 * 
 *@author fqming (fqming@telenav.cn)
 *@date Jul 27, 2010
 */
public interface IRecorderListener
{
    /**
     * This function will be invoked when recorder is prepared. We usually playing beep or vibrate here to indicate
     * recorder is ready.
     */
    public void beforeRecorder();

    /**
     * This function will be invoked when recorder finished.
     */
    public void finishRecorder();
}
