/**
 *
 * Copyright 2013 TeleNav, Inc. All rights reserved.
 * AudioDataProvider.java
 *
 */
package com.telenav.module.media;

import com.telenav.data.dao.misc.DaoManager;
import com.telenav.datatypes.audio.AudioData;
import com.telenav.datatypes.audio.AudioData.IAudioDataProvider;

/**
 *@author bduan
 *@date 2013-3-27
 */
public class AudioDataProvider implements IAudioDataProvider
{
    private static class InnerAudioDataProvider
    {
        private static AudioDataProvider instance = new AudioDataProvider();
    }
    
    private AudioDataProvider()
    {
        
    }
    
    public static AudioDataProvider getInstance()
    {
        return InnerAudioDataProvider.instance;
    }
    
    @Override
    public byte[] getData(AudioData clipData) throws Throwable
    {
        return DaoManager.getInstance().getResourceBarDao().getData(clipData);
    }

}
