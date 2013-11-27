/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * TrafficMissingAudioProxy.java
 *
 */
package com.telenav.data.serverproxy.impl;

import com.telenav.data.dao.serverproxy.AbstractDaoManager;
import com.telenav.datatypes.audio.AudioDataNode;
import com.telenav.threadpool.INotifierListener;
import com.telenav.threadpool.Notifier;

/**
 *@author fqming (fqming@telenav.cn)
 *@date Jul 20, 2010
 */
public class TrafficMissingAudioProxy implements INotifierListener
{
    private IMissingAudioProxy missingAudioProxy;
    
    private long notifyInterval = 3000;
    private long lastNotifyTimestamp = -1L;
    private Notifier notifier;
    
    public TrafficMissingAudioProxy(Notifier notifier, IMissingAudioProxy missingAudioProxy)
    {
        this.notifier = notifier;
        this.missingAudioProxy = missingAudioProxy;
    }
    
    public void startFetching()
    {
        lastNotifyTimestamp = -1L;
        this.notifier.removeListener(this);
        this.notifier.addListener(this);
    }
    
    public void putAudioIntoCache(AudioDataNode audioNodes)
    {
        for (int i = 0; i < audioNodes.getChildrenSize(); i++)
        {
            AudioDataNode audioNode = audioNodes.getChild(i);
            if(audioNode.getAudioData() != null)
            {
                int audioId = (int) audioNode.getAudioData().getResourceId();
                if (audioNode.getAudioData().getData() != null)
                {
                    AbstractDaoManager.getInstance().getResourceBarDao().putDynamicAudio(audioId, audioNode.getAudioData().getData());
                }
                else
                {
                    this.addExtraMissingAudio(audioId);
                }
            }
        }
    }
    
    private void addExtraMissingAudio(int audioId)
    {
        if(AbstractDaoManager.getInstance().getResourceBarDao().getAudio(audioId) == null)
        {
            missingAudioProxy.addExtraMissingAudio(audioId);
        }
    }

    public long getLastNotifyTimestamp()
    {
        return this.lastNotifyTimestamp;
    }

    public long getNotifyInterval()
    {
        return notifyInterval;
    }

    public void notify(long timestamp)
    {
        if(missingAudioProxy != null && !missingAudioProxy.sendExtraMissingAudioRequest())
        {
            this.notifier.removeListener(this);
        }
    }

    public void setLastNotifyTimestamp(long timestamp)
    {
        this.lastNotifyTimestamp = timestamp;
    }
}
