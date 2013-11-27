/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * AndroidMediaManager.java
 *
 */
package com.telenav.media.android;

import android.content.Context;
import android.media.AudioManager;

import com.telenav.media.TnMediaException;
import com.telenav.media.TnMediaManager;
import com.telenav.media.TnMediaPlayer;
import com.telenav.media.TnMediaRecorder;

/**
 * This class provides access to the system media services at android platform.
 * 
 *@author fqming (fqming@telenav.cn)
 *@date Jul 26, 2010
 */
public class AndroidMediaManager extends TnMediaManager
{
    protected Context context;
    
    private static final int PCM_RECORD_FREQUENCY_16K     = 16000;

    /**
     * Construct the media manager at android platform.
     * <br />
     * <br />
     * Please make sure that grant below class's permission.
     * <br />
     * AudioManager am = (AudioManager) context.getSystemService(AUDIO_SERVICE);
     * 
     * @param context
     */
    public AndroidMediaManager(Context context)
    {
        this.context = context;
    }

    public TnMediaPlayer createPlayer(byte[] audios, String format) throws TnMediaException
    {
        return new AndroidMediaPlayer(this.context, audios, format);
    }

    public TnMediaPlayer createPlayer(String url, String format) throws TnMediaException
    {
        return new AndroidMediaPlayer(this.context, url, format);
    }

    public TnMediaRecorder createRecorder(String format) throws TnMediaException
    {
        if (FORMAT_PCM.equalsIgnoreCase(format))
        {
            return new AndroidPcmMediaRecorder(context, format, PCM_RECORD_FREQUENCY_16K);
        }
        else
        {
            return new AndroidMediaRecorder(context, format);
        }
    }

    public int getMediaVolume()
    {
        AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        return am.getStreamVolume(AudioManager.STREAM_MUSIC);
    }

    public boolean isBluetoothHeadsetOn()
    {
        AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        return am.isBluetoothA2dpOn();
    }

    public boolean isWiredHeadsetOn()
    {
        AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        if (am.isBluetoothA2dpOn())
            return false;
        if (am.isBluetoothScoOn())
            return false;
        if (am.isSpeakerphoneOn())
            return false;

        return true;
    }
    
    public void setAudioPath(int path)
    {
        AudioManager am = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        switch(path)
        {
            case PATH_BLUETOOTH_HEADSET:
            {
                am.setSpeakerphoneOn(false);
                am.setBluetoothA2dpOn(true);
                break;
            }
            case PATH_SPEAKER_PHONE:
            {
                am.setBluetoothA2dpOn(false);
                am.setSpeakerphoneOn(true);
                break;
            }
            case PATH_WIRED_HEADSET:
            {
                am.setBluetoothA2dpOn(false);
                am.setSpeakerphoneOn(false);
                break;
            }
        }
    }
    
    public int getAudioPath()
    {
        AudioManager am = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        if (am.isBluetoothA2dpOn())
        {
            return PATH_BLUETOOTH_HEADSET;
        }
        else
        {
            if (am.isSpeakerphoneOn())
            {
                return PATH_SPEAKER_PHONE;
            }
            else
            {
                return PATH_WIRED_HEADSET;
            }
        }
    }

    public void setMediaVolume(int volume, ITnVolumeUi volumeUi)
    {
        if(volume < 0)
            volume = 0;
        
        AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        if (volume > am.getStreamMaxVolume(AudioManager.STREAM_MUSIC))
        {
            volume = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        }

        am.setStreamVolume(AudioManager.STREAM_MUSIC, volume, volumeUi != null ? AudioManager.FLAG_SHOW_UI : 0);
    }
}
