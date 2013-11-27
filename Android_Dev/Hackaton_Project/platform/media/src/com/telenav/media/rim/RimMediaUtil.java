/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * RimMediaUtil.java
 *
 */
package com.telenav.media.rim;

import javax.microedition.media.MediaException;
import javax.microedition.media.Player;
import javax.microedition.media.control.VolumeControl;

import net.rim.device.api.media.control.AudioPathControl;

import com.telenav.logger.Logger;
import com.telenav.media.TnMediaManager;

/**
 *@author fqming (fqming@telenav.cn)
 *@date Aug 17, 2010
 */
class RimMediaUtil
{
    public static void setVolume(Player player)
    {
        try
        {
            VolumeControl volumeControl = (VolumeControl)player.getControl("VolumeControl");
            volumeControl.setLevel(TnMediaManager.getInstance().getMediaVolume());
        }
        catch (Exception e)
        {
            Logger.log(RimMediaUtil.class.getName(), e, "set volume..." + TnMediaManager.getInstance().getMediaVolume());
        }
    }
    
    public static void setAudioPath(Player player, int tnAudioPath) throws MediaException
    {
        AudioPathControl audioPathControl = (AudioPathControl) player.getControl("net.rim.device.api.media.control.AudioPathControl");
        
        audioPathControl.setAudioPath(convertToSystemPath(tnAudioPath));
    }
    
    public static void setAudioPath(Player player)
    {
        try
        {
            setAudioPath(player, TnMediaManager.getInstance().getAudioPath());
        }
        catch (Exception e)
        {
            Logger.log(RimMediaUtil.class.getName(), e, "set audio path..." + TnMediaManager.getInstance().getAudioPath());

            try
            {
                AudioPathControl audioPathControl = (AudioPathControl) player
                        .getControl("net.rim.device.api.media.control.AudioPathControl");
                if (TnMediaManager.getInstance().isWiredHeadsetOn())
                {
                    audioPathControl.setAudioPath(convertToSystemPath(TnMediaManager.PATH_WIRED_HEADSET));
                }
                else
                {
                    audioPathControl.setAudioPath(convertToSystemPath(TnMediaManager.PATH_SPEAKER_PHONE));
                }
            }
            catch (Exception e1)
            {
                Logger.log(RimMediaUtil.class.getName(), e1, "set audio path..." + TnMediaManager.getInstance().getAudioPath());
            }
        }
    }
    
    public static int convertToSystemPath(int tnPath)
    {
        switch(tnPath)
        {
            case TnMediaManager.PATH_BLUETOOTH_HEADSET:
                return AudioPathControl.AUDIO_PATH_BLUETOOTH;
            case TnMediaManager.PATH_SPEAKER_PHONE:
                return AudioPathControl.AUDIO_PATH_HANDSFREE;
            case TnMediaManager.PATH_WIRED_HEADSET:
                return AudioPathControl.AUDIO_PATH_HEADSET;
        }
        
        return AudioPathControl.AUDIO_PATH_HANDSFREE;
    }
}
