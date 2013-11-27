/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * NavsdkAudioService.java
 *
 */
package com.telenav.data.serverproxy.impl.navsdk.service;

import com.google.protobuf.ByteString;
import com.telenav.audio.AudioPlayer;
import com.telenav.audio.IPlayerListener;
import com.telenav.datatypes.audio.AudioData;
import com.telenav.datatypes.audio.AudioDataFactory;
import com.telenav.dsr.DsrManager;
import com.telenav.module.media.MediaManager;
import com.telenav.navsdk.eventmanager.EventBus;
import com.telenav.navsdk.events.AudioPlayerData;
import com.telenav.navsdk.events.AudioPlayerData.AudioPlayState;
import com.telenav.navsdk.events.AudioPlayerEvents;
import com.telenav.navsdk.events.AudioPlayerEvents.OnPlayerStateChangeRequest;
import com.telenav.navsdk.events.AudioPlayerEvents.PlayAudio;
import com.telenav.navsdk.events.AudioPlayerEvents.StopPlaying;
import com.telenav.navsdk.services.AudioPlayerListener;
import com.telenav.navsdk.services.AudioPlayerServiceProxy;

/**
 * @author Casper(pwang@telenav.cn)
 * @date 2012-11-28
 */
public class NavsdkAudioService implements AudioPlayerListener,IPlayerListener
{
    private static NavsdkAudioService instance;

    private AudioPlayerServiceProxy serverProxy;

    private AudioPlayer audioPlayer;
    
    boolean isRunning = false;

    private NavsdkAudioService()
    {
        audioPlayer = MediaManager.getInstance().getAudioPlayer();
    }

    public synchronized static void init(EventBus bus)
    {
        if (instance == null)
        {
            instance = new NavsdkAudioService();
            instance.setEventBus(bus);
        }
    }

    public static NavsdkAudioService getInstance()
    {
        return instance;
    }

    private void setEventBus(EventBus bus)
    {
        serverProxy = new AudioPlayerServiceProxy(bus);
        serverProxy.addListener(this);
    }

    public void onPlayAudio(PlayAudio event)
    {
        handle(event);
    }

    private void handle(PlayAudio event)
    {
        if (audioPlayer == null || !isRunning || DsrManager.getInstance().isRunning())
            return;
        
        if (event.hasFileName())
        {
            playerStateChange(AudioPlayState.AudioPlayState_Playing);
            AudioData audioData = AudioDataFactory.getInstance().createAudioData(event.getFileName());
            MediaManager.getInstance().getAudioPlayer().play("", audioData, -1, this);
        }
        else if (event.hasByteStream())
        {
            playerStateChange(AudioPlayState.AudioPlayState_Playing);
            ByteString stream = event.getByteStream();
            AudioData audioData = AudioDataFactory.getInstance().createAudioData(stream.toByteArray());
            MediaManager.getInstance().getAudioPlayer().play("", audioData, -1, this);
        }
        else
        {
            sendPlayError();
        }
    }

    private void sendPlayError()
    {
        AudioPlayerEvents.PlayErrorRequest.Builder errorBuilder = AudioPlayerEvents.PlayErrorRequest.newBuilder();
        errorBuilder.setError(AudioPlayerData.PlayerError.PlayerError_InvalidResource);
        AudioPlayerEvents.PlayErrorRequest req = errorBuilder.build();
        serverProxy.playError(req);
    }

    public void onStopPlaying(StopPlaying event)
    {
        handle(event);
    }

    private void handle(StopPlaying event)
    {
        if(!isRunning)
        {
            return;
        }
        audioPlayer.cancelAll();
    }
    
    public synchronized void pause()
    {
        isRunning = false;
    }
    
    public synchronized void resume()
    {
        isRunning = true;
    }

    public void finishPlayer()
    {
        playerStateChange(AudioPlayState.AudioPlayState_Idle);
    }
    
    private void playerStateChange(AudioPlayState state)
    {
        OnPlayerStateChangeRequest.Builder builder = OnPlayerStateChangeRequest.newBuilder();
        builder.setState(state);
        serverProxy.onPlayerStateChange(builder.build());
    }
    
    

}
