/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * Synthesizer.java
 *
 */
package com.telenav.audio;

import com.telenav.datatypes.audio.AudioData;

/**
 * A synthesizer that will synthesize the audio data in the queue, such as remove the header, footer of the audio data
 * etc.
 * 
 *@author fqming (fqming@telenav.cn)
 *@date Jul 27, 2010
 */
public abstract class Synthesizer
{
    /**
     * synthesize the audio data in the queue, such as remove the header, footer of the audio data
     * @param audios the audio data in the queue
     * @return the new binary data.
     */
    public abstract byte[] synthesize(AudioData[] audios);
}
