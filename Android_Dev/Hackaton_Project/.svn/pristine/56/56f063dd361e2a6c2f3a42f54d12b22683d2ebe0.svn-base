/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * RimMediaManager.java
 *
 */
package com.telenav.media.rim;

import java.io.ByteArrayInputStream;

import javax.microedition.media.Manager;
import javax.microedition.media.Player;

import net.rim.device.api.system.Audio;

import com.telenav.logger.Logger;
import com.telenav.media.TnMediaException;
import com.telenav.media.TnMediaManager;
import com.telenav.media.TnMediaPlayer;
import com.telenav.media.TnMediaRecorder;

/**
 * This class provides access to the system media services at rim platform.
 * 
 *@author fqming (fqming@telenav.cn)
 *@date Aug 17, 2010
 */
public class RimMediaManager extends TnMediaManager
{
    protected int volume = 100;
    protected int audioPath = PATH_SPEAKER_PHONE;
    protected Player btCheckPlayer;
    
    /**
     * Construct the media manager at rim platform.
     * <br />
     * <br />
     * Please make sure that grant below class's permission.
     * <br />
     * ApplicationPermissions#PERMISSION_BLUETOOTH;#PERMISSION_MEDIA;#PERMISSION_RECORDING;
     * <br />
     * 
     */
    public RimMediaManager()
    {
        
    }
    
    public TnMediaPlayer createPlayer(byte[] audios, String format) throws TnMediaException
    {
        return new RimMediaPlayer(audios, format);
    }

    public TnMediaPlayer createPlayer(String url, String format) throws TnMediaException
    {
        return new RimMediaPlayer(url, format);
    }

    public TnMediaRecorder createRecorder(String format) throws TnMediaException
    {
        return new RimMediaRecorder(format);
    }

    public int getAudioPath()
    {
        return this.audioPath;
    }

    public int getMediaVolume()
    {
        return this.volume;
    }

    public boolean isBluetoothHeadsetOn()
    {
        try
        {
            if (btCheckPlayer == null)
            {
                ByteArrayInputStream audioFakeData = new ByteArrayInputStream(new byte[]{ 1 });
                btCheckPlayer = Manager.createPlayer(audioFakeData, FORMAT_AMR);
                btCheckPlayer.realize();
            }
            
            RimMediaUtil.setAudioPath(btCheckPlayer, PATH_BLUETOOTH_HEADSET);
            
            return true;
        }
        catch (Exception e)
        {
            Logger.log(this.getClass().getName(), e, "check bluetooth headset is on...");

            return false;
        }
    }

    public boolean isWiredHeadsetOn()
    {
        return Audio.isHeadsetConnected();
    }

    public void setAudioPath(int path)
    {
        this.audioPath = path;
    }

    public void setMediaVolume(int volume, ITnVolumeUi volumeUi)
    {
        if(volume < 0)
            volume = 0;
        else if(volume > 100)
            volume = 100;
        
        this.volume = volume;
        
        //TODO UI
    }

}
