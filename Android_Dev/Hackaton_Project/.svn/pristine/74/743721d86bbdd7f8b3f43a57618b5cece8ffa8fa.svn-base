/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * AndroidMediaUtil.java
 *
 */
package com.telenav.media.android;

import android.content.Context;
import android.media.AudioManager;

/**
 *@author hchai
 *@date 2011-7-12
 */
class AndroidMediaUtil
{
    public static void requestAudioFocus(Context context)
    {
        if (context != null)
        {
            AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            audioManager.requestAudioFocus(null, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
        }
    }
    
    public static void abandonAudioFocus(Context context)
    {
        if (context != null)
        {
            AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            audioManager.abandonAudioFocus(null); 
        }
    }
}
