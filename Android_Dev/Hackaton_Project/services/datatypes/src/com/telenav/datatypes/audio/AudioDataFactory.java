/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * AudioDataFactory.java
 *
 */
package com.telenav.datatypes.audio;

import java.io.InputStream;

/**
 *@author fqming (fqming@telenav.cn)
 *@date Aug 20, 2010
 */
public class AudioDataFactory
{
    protected static AudioDataFactory instance = new AudioDataFactory();

    private static int initCount;

    protected AudioDataFactory()
    {

    }

    public static AudioDataFactory getInstance()
    {
        return instance;
    }

    public synchronized static void init(AudioDataFactory factory)
    {
        if (initCount >= 1)
            return;

        instance = factory;
        initCount++;
    }
    
    public AudioData createAudioData(int resourceId)
    {
        return new AudioData(resourceId);
    }
    
    public AudioData createAudioData(byte[] audioData)
    {
        return new AudioData(audioData);
    }
    
    public AudioData createAudioData(String resourceUri)
    {
        return new AudioData(resourceUri);
    }
    
    public RuleNode createRuleNode(InputStream is) throws Exception
    {
        return new RuleNode(is);
    }
    
    public AudioDataNode createAudioDataNode(AudioData data)
    {
        return new AudioDataNode(data);
    }
}
