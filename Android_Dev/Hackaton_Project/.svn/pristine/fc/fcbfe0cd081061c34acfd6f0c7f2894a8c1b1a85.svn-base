/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * AudioJob.java
 *
 */
package com.telenav.audio;

import com.telenav.datatypes.audio.AudioData;
import com.telenav.logger.Logger;
import com.telenav.media.ITnMediaListener;
import com.telenav.media.TnMediaManager;
import com.telenav.media.TnMediaPlayer;
import com.telenav.threadpool.IJob;

/**
 *@author fqming (fqming@telenav.cn)
 *@date Jul 27, 2010
 */
class PlayerJob implements IJob, ITnMediaListener
{
    protected String playId;

    protected AudioData[] audios;

    protected IPlayerListener playerListener;
    
    protected AudioPlayer player;
    
    protected Synthesizer synthesizer;
    
    protected long playTimeout;
    
    protected boolean isCancelled = false;

    protected boolean isRunning = false;

    protected Object mutex = new Object();
    
    protected Object focusMutex = new Object();

    public PlayerJob(String playId, AudioData[] audios, Synthesizer synthesizer, long playTimeout, IPlayerListener playerListener, AudioPlayer player)
    {
        this.playId = playId;
        this.audios = audios;
        this.synthesizer = synthesizer;
        this.playTimeout = playTimeout;
        this.playerListener = playerListener;
        this.player = player;
    }

    public void cancel()
    {
        isCancelled = true;
        stop();
    }
    
    private void stop()
    {
        synchronized (mutex)
        {
            mutex.notify();
        }
    }

    public void execute(int handlerID)
    {
        if(!this.player.isEnabled())
        {
            if (Logger.DEBUG)
            {
                Logger.log(Logger.INFO, this.getClass().getName(), "Currently disable the audio.");
            }
            
            doFinishCallback();
            
            return;
        }
        
        this.isRunning = true;

        int audioPath = TnMediaManager.PATH_SPEAKER_PHONE;

        if (this.player.mediaManager.isWiredHeadsetOn())
        {
            audioPath = TnMediaManager.PATH_WIRED_HEADSET;
        }
        if (this.player.mediaManager.isBluetoothHeadsetOn())
        {
            audioPath = TnMediaManager.PATH_BLUETOOTH_HEADSET;
        }

        this.player.mediaManager.setAudioPath(audioPath);
        if (this.player.mediaManager.isBluetoothHeadsetOn()
                && this.player.mediaManager.getAudioPath() == TnMediaManager.PATH_BLUETOOTH_HEADSET)
        {            
            play(this.getAmrFakeData(), TnMediaManager.FORMAT_AMR, 1500, false, false);
        }
        else if (this.player.mediaManager.isWiredHeadsetOn())
        {
            audioPath = TnMediaManager.PATH_WIRED_HEADSET;
            this.player.mediaManager.setAudioPath(audioPath);
        }
        else
        {
            audioPath = TnMediaManager.PATH_SPEAKER_PHONE;
            this.player.mediaManager.setAudioPath(audioPath);
        }

        byte[] audioData = null;
        if(TnMediaManager.FORMAT_AMR.equals(this.player.audioFormat))
        {
            if(this.synthesizer == null)
            {
                this.synthesizer = new SynthesizerAmr();
            }
            
            audioData = this.synthesizer.synthesize(this.audios);
            
            if (audioData != null)
            	this.playTimeout = Math.max(this.playTimeout, (audioData.length << 2) + 1000);
        }
        else if(TnMediaManager.FORMAT_MP3.equals(this.player.audioFormat))
        {
            if(this.synthesizer == null)
            {
                this.synthesizer = new SynthesizerMp3();
            }
            
            audioData = this.synthesizer.synthesize(audios);
            
            if (audioData != null)
                this.playTimeout = Math.max(this.playTimeout + 1000, (audioData.length << 1) + 3000);
        }
        else if(TnMediaManager.FORMAT_3GPP.equals(this.player.audioFormat))
        {
            //TODO current don't support this audio format.
            Logger.log(Logger.ERROR, this.getClass().getName(), "Currently don't support 3GPP audio format.");
        }
        
        if(audioData != null)
        {
           play(audioData, this.player.audioFormat, this.playTimeout, true, true);
        }
        
        doFinishCallback();
        
        this.isRunning = false;
    }

    public boolean isCancelled()
    {
        return isCancelled;
    }

    public boolean isRunning()
    {
        return isRunning;
    }

    public void mediaUpdate(TnMediaPlayer player, String event, Object eventData)
    {
        if (ITnMediaListener.ON_ERROR.equals(event))
        {
            this.cancel();
        }
        else if(ITnMediaListener.ON_CLOSE.equals(event) || ITnMediaListener.ON_COMPLETION.equals(event))
        {
            this.stop();
        }
    }
    
    private void doFinishCallback()
    {
        if (this.playerListener != null)
        {
            try
            {
                this.playerListener.finishPlayer();
            }
            catch (Throwable ex)
            {
                Logger.log(this.getClass().getName(), ex);
            }
        }
    }
    
    private void play(byte[] audioData, String format, long timeout, boolean needListener, boolean needReleaseFocus)
    {
        TnMediaPlayer mediaPlayer = null;
        try
        {
            synchronized (mutex)
            {
                mediaPlayer = this.player.mediaManager.createPlayer(audioData, format);
                if(needListener)
                {
                    mediaPlayer.setListener(this);
                }
                
                if (isCancelled)
                    return;
                
                mediaPlayer.realize();
                
                if (isCancelled)
                    return;

                mediaPlayer.prefetch();
                
                if (isCancelled)
                    return;
                mediaPlayer.start();
                if (isCancelled)
                    return;
                mutex.wait(timeout);
                synchronized (focusMutex)
                {
                    focusMutex.wait(500);
                }
            }
        }
        catch (Exception e)
        {
            Logger.log(this.getClass().getName(), e);
        }
        finally
        {
            try
            {
                mediaPlayer.stop();
            }
            catch (Exception e)
            {
                Logger.log(this.getClass().getName(), e);
            }
            
            if (player.isNeedAbandonFocus())
            {
                try
                {
                    //Add needReleaseFocus fix for TNANDROID-4950
                    //When play fake blank audio of bluetooth, no need to abandonAudioFocus()
                    //if focus lost during audio playing, we should not abandon focus immediately here. 
                    //We need onAudioFocusChange to handle this case.
                    if(needReleaseFocus && mediaPlayer.isAllowAbandon())
                    {
                        mediaPlayer.abandonAudioFocus();
                    }
                    synchronized (focusMutex)
                    {
                        focusMutex.wait(500);
                    }
                }
                catch (Exception e)
                {
                    Logger.log(this.getClass().getName(), e);
                }
            }
            try
            {
                mediaPlayer.close();
            }
            catch (Exception e)
            {
                Logger.log(this.getClass().getName(), e);
            }
            finally
            {
                mediaPlayer = null;
            }
        }
    }
    
    private byte[] getAmrFakeData()
    {
        return new byte[]
        { 35, 33, 65, 77, 82, 10, 60, 8, 90, 95, -116, 68, 81, -63, -56, -51, 101, 47, -28, 87, 83, 51, -128, 0, 1, 25, 5, 20, 3, -128, 0,
                0, -34, 89, -59, -126, 21, -128, 60, -32, 113, 104, -107, -39, -128, 0, 96, 79, -118, 55, 4, 65, -31, 109, 20, -19, -17,
                39, -109, -58, 40, 60, 96, 112, -88, -110, 43, -100, -101, -80, 60, -111, 8, 116, -75, -91, 122, 1, 32, 75, -101, 95, -118,
                -121, -78, 69, -123, -40, -104, 15, 15, 95, -40, 0, 1, 121, 84, 99, -102, 42, 42, -96, 60, -32, 101, -42, -109, 66, 84, 16,
                -32, 91, 60, 122, 21, -119, -117, 41, -49, 49, -87, 12, 63, 37, 53, -127, 103, -43, -18, 124, -37, 114, -1, 32, 60, 85, 5,
                97, -73, -26, -28, 21, 32, -106, 40, 90, 67, -116, -101, 0, -116, 41, 108, -14, 53, -85, 23, -32, 22, -19, -85, -77, -98,
                67, 55, 64, 60, -40, -15, 26, -98, -84, -34, 1, 1, 45, -57, 26, 82, -68, 11, 32, -15, 57, 121, -68, 97, 43, -47, -126, -28,
                -35, 92, 122, 126, 122, 114, -48, 60, -34, -126, 121, 71, -29, -44, 1, 64, 31, -84, -113, -38, -103, -127, 70, -127, 121,
                115, -87, -121, 112, 17, 16, -71, -8, -64, -45, -26, -37, -23, -80, 60, 16, 1, -89, 72, 98, 7, 23, 81, 17, 109, -69, -60,
                27, 51, -20, 122, -21, 0, -40, 33, -73, 104, -103, -58, 0, 118, -42, 76, 114, 65, 64, 60, 14, 40, -24, -104, 97, -48, 89,
                97, 29, -92, 10, -71, 60, 66, -55, -59, 66, 56, -108, 84, -58, 13, -108, 106, -99, 70, -30, -21, -126, -45, 96, 60, 6, 5,
                -90, 64, 97, -69, -33, -63, -121, 46, -6, -12, -33, 3, 102, -40, -53, 21, 63, 6, 5, -100, 122, -24, 1, -6, 0, 13, -30, 125,
                -128, 60, 12, 17, -53, -112, 97, -99, 75, 96, 31, -103, -86, -77, -119, -25, 60, 37, 59, -85, -38, -66, 0, -85, 87, -45,
                -28, 108, -56, -118, -43, -118, 64, 60, 6, 8, -39, 32, 96, -67, -27, -64, 31, -122, 74, -100, 42, 71, 41, -39, -108, 102,
                -2, 27, 25, -13, -107, 68, 81, -125, 17, -101, 118, -128, -128, 60, 12, 9, 101, -112, 97, -71, -28, -31, 33, -109, 24, 110,
                -102, 74, 60, -45, -85, -35, -97, 38, -67, 31, -47, 53, 81, -2, 92, 64, -63, 94, -128, 60, 6, 9, -90, 48, 99, 33, -62, -32,
                -110, 125, 122, -86, 110, 53, 43, -35, 63, -113, -22, -49, -6, 79, 13, 123, -7, -39, 32, -113, -25, 44, -16, 60, 12, 8,
                -24, -112, 97, -67, -15, 0, 61, -103, -86, 60, 127, 67, 32, 60, -40, -12, -54, 122, -126, 4, 18, -46, 62, -46, -34, 97,
                -53, 53, 64, 60, 6, 20, 75, 64, 97, -3, -53, -64, 23, 42, -6, -35, -65, -25, 24, -49, 6, -108, 50, -4, -45, 85, -38, -62,
                52, 10, 51, 97, 2, 47, -96, 60, 12, 7, -54, -112, 102, 33, -65, -95, 14, 125, 24, -5, -106, 38, -71, 56, 54, 88, 78, 73,
                36, 76, 61, 120, 55, -73, -111, -54, 67, -81, -48, 60, 6, 21, 114, 120, 100, -23, -8, -64, 44, -46, 90, -99, 22, -52, 123,
                -22, -90, 109, 71, -125, -44, -93, -100, -12, 91, -102, -35, -73, -5, -63, -128, 60, 12, 7, -90, -104, 97, -7, -122, -32,
                31, -121, 10, 58, 31, -116, 38, -19, -93, 21, -1, -73, 25, -46, -104, -46, 127, -35, 104, 63, 67, -43, 64, 60, 6, 20, -24,
                64, 97, -88, -3, -96, -91, 106, -54, 123, -39, 21, -22, -22, 0, 68, 53, 122, 84, -121, 67, 79, 90, -42, -113, 122, -90,
                -39, 32, 60, 12, 7, -94, -104, 100, -21, -8, 64, 14, 127, -118, -100, -45, 126, 39, -6, -64, 2, -93, 108, -99, 8, 31, -49,
                -118, -3, 34, -46, -51, 53, 64, 60, 6, 21, -54, 120, 96, -71, -23, 0, -121, -59, 90, 92, 82, 90, 50, -57, -120, -17, -44,
                -104, -88, -94, 20, -2, -43, 67, -110, 97, -29, -97, 32, 60, 12, 6, 49, 72, 97, -41, -91, -63, 32, -35, -39, -106, -90,
                -45, 108, -48, 9, -99, -33, 114, 97, -55, -77, -76, 31, -1, 97, 20, -80, -99, -128, 60, 6, 20, -41, 64, 97, -85, -23, 32,
                31, 57, -38, 65, -72, -9, 45, -28, 29, 99, -17, -63, 14, 50, 94, -115, -80, 90, 0, -23, -25, -74, -128, 60, 12, 7, -90, 72,
                100, -24, -37, 32, 54, -41, 90, 32, -54, -49, -107, -57, -52, 28, 53, 112, -42, 107, 54, 31, -64, 42, 16, 107, -97, -14,
                -64, 60, 6, 12, 94, -112, 97, -115, -31, -128, 89, -120, -54, 113, -71, 87, 22, -58, 82, 21, -96, 68, -128, -81, -37, 56,
                76, 118, 91, 102, 3, 110, -128, 60, 12, 9, -90, -120, 102, 97, -46, -127, 36, 117, -38, -52, 18, 88, 101, -9, -4, -45, 39,
                3, -7, -24, 102, -107, 96, 25, 66, -110, 16, 32, 0, 60, 6, 9, 101, -120, 97, -7, -38, -32, 52, -41, 106, 118, -80, -52,
                -76, -64, 13, 46, 77, -115, -85, 110, -76, 7, 73, 79, 118, 105, 45, 82, 64, 60, 12, 21, -90, 72, 100, -21, -3, -64, -45,
                -122, 90, -29, -36, 78, -69, -31, -2, -78, -114, 118, -91, 95, -19, -16, 103, 122, 67, 81, 38, -65, -32, 60, 6, 7, 101,
                -120, 100, -53, -97, 96, 30, -61, 104, -10, -84, 103, -75, 33, 108, -125, 21, 75, 31, 13, -12, 88, -114, -1, -58, 22, -2,
                68, -128, 60, 12, 20, 75, -112, 103, 53, -7, -95, 36, 107, -38, 123, 52, -70, 124, -23, 36, 45, 82, -25, 22, 55, -64, -5,
                -6, 51, 11, -19, -22, 10, 0, 60, 6, 6, -24, -120, 99, 81, -98, -32, -108, 125, 106, 65, -78, -75, -81, -41, 95, 9, 63, -36,
                2, -101, -33, 101, -126, -86, -47, 73, 35, 28, -128 };
    }
}
