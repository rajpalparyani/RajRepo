/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * IMissingAudioProxy.java
 *
 */
package com.telenav.data.serverproxy.impl;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2010-12-21
 */
public interface IMissingAudioProxy
{
    public void sendMissingAudioRequest(int[] audioIds);
    
    public void addExtraMissingAudio(int audioId);
    
    public boolean sendExtraMissingAudioRequest();
    
    public void clear();
}
