/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * AudioPlayer.java
 *
 */
package com.telenav.audio;

import java.util.Vector;

import com.telenav.datatypes.audio.AudioData;
import com.telenav.datatypes.audio.AudioData.IAudioDataProvider;
import com.telenav.media.TnMediaManager;
import com.telenav.threadpool.ThreadPool;

/**
 * AudioPlayer class can be used to control playback of audiofiles and streams.
 * 
 *@author fqming (fqming@telenav.cn)
 *@date Jul 27, 2010
 */
public class AudioPlayer
{
    public static final String LOG_AUDIO                   = "marts_audio";
    
    protected TnMediaManager mediaManager;

    protected ThreadPool audioThreadPool;

    protected IAudioDataProvider defaultAudioDataProvider;
    
    protected String audioFormat;

    protected boolean isEnabled = true;
    
    /**
     * construct a audio player.
     * 
     * @param mediaManager platform's media manager.
     * @param audioThreadPool a thread pool to play audio.
     * @param defaultAudioDataProvider resource provider of audio.
     * @param audioFormat audio data's format
     */
    public AudioPlayer(TnMediaManager mediaManager, ThreadPool audioThreadPool, IAudioDataProvider defaultAudioDataProvider,
            String audioFormat)
    {
        if (mediaManager == null)
        {
            throw new IllegalArgumentException("The mediaManager is null.");
        }
        if (audioThreadPool == null)
        {
            throw new IllegalArgumentException("The audioPool is null.");
        }
        if (defaultAudioDataProvider == null)
        {
            throw new IllegalArgumentException("The dataProvider is null.");
        }
        if (audioFormat == null)
        {
            throw new IllegalArgumentException("The audioFormat is null.");
        }
        
        this.mediaManager = mediaManager;
        this.audioThreadPool = audioThreadPool;
        this.defaultAudioDataProvider = defaultAudioDataProvider;
        this.audioFormat = audioFormat;
    }

    /**
     * play the audio data with special play id.
     * 
     * @param playId the play id in the audio job pool.
     * @param audio the audio data.
     * @param timeout the time out when play the audio. will check the audio data's length and use the max value.
     * @return the play id.
     */
    public String play(String playId, AudioData audio, long timeout)
    {
        return this.play(playId, audio, timeout, null);
    }
    
    /**
     * play the audio data with special play id.
     * 
     * @param playId the play id in the audio job pool.
     * @param audio the audio data.
     * @param timeout the time out when play the audio. will check the audio data's length and use the max value.
     * @param listener player's listener
     * @return the play id.
     */
    public String play(String playId, AudioData audio, long timeout, IPlayerListener listener)
    {
        return this.play(playId, new AudioData[]{ audio }, null, timeout, listener);
    }
    
    /**
     * play the audio data with special play id and special synthesizer.
     * 
     * @param playId the play id in the audio job pool.
     * @param audio the audio data.
     * @param synthesizer the synthesizer for the audio data.
     * @param timeout the time out when play the audio. will check the audio data's length and use the max value.
     * @return the play id.
     */
    public String play(String playId, AudioData audio, Synthesizer synthesizer, long timeout)
    {
        if(audio.getDataProvider() == null)
        {
            audio.setDataProvider(this.defaultAudioDataProvider);
        }
        
        return play(playId, new AudioData[]{ audio }, synthesizer, timeout);
    }
    
    /**
     * play a sequence of audio data with special play id.
     * 
     * @param playId the play id in the audio job pool.
     * @param audios a sequence of audio data.
     * @param timeout the time out when play the audio. will check the audio data's length and use the max value.
     * @return the play id.
     */
    public String play(String playId, AudioData[] audios, long timeout)
    {
        return this.play(playId, audios, timeout, null);
    }
    
    /**
     * play a sequence of audio data with special play id.
     * 
     * @param playId the play id in the audio job pool.
     * @param audios a sequence of audio data.
     * @param timeout the time out when play the audio. will check the audio data's length and use the max value.
     * @param listener player's listener
     * @return the play id.
     */
    public String play(String playId, AudioData[] audios, long timeout, IPlayerListener listener)
    {
        return this.play(playId, audios, null, timeout, listener);
    }

    /**
     * play a sequence of audio data with special play id and special synthesizer.
     * 
     * @param playId playId the play id in the audio job pool.
     * @param audios a sequence of audio data.
     * @param synthesizer the synthesizer for the audio data.
     * @param timeout the time out when play the audio. will check the audio data's length and use the max value.
     * @return the play id.
     */
    public String play(String playId, AudioData[] audios, Synthesizer synthesizer, long timeout)
    {
        return this.play(playId, audios, synthesizer, timeout, null);
    }
    
    /**
     * play a sequence of audio data with special play id and special synthesizer.
     * 
     * @param playId playId the play id in the audio job pool.
     * @param audios a sequence of audio data.
     * @param synthesizer the synthesizer for the audio data.
     * @param timeout the time out when play the audio. will check the audio data's length and use the max value.
     * @param listener player's listener
     * @return the play id.
     */
    public String play(String playId, AudioData[] audios, Synthesizer synthesizer, long timeout, IPlayerListener listener)
    {
        if (audios == null)
        {
            return null;
        }
        
        if(timeout <= 0)
        {
            timeout = 30 * 1000;
        }
        
        if (playId == null || playId.length() == 0)
        {
            playId = "audio_" + System.currentTimeMillis();
        }

        for(int i = 0; i < audios.length; i++)
        {
            if(audios[i].getDataProvider() == null)
            {
                audios[i].setDataProvider(this.defaultAudioDataProvider);
            }
        }
        
        PlayerJob audioJob = new PlayerJob(playId, audios, synthesizer, timeout, listener, this);

        this.audioThreadPool.addJob(audioJob);

        return audioJob.playId;
    }

    /**
     * cancel the whole audio jobs in the thread pool.
     */
    public void cancelAll()
    {
        this.audioThreadPool.cancelAll();
    }

    /**
     * cancel the special audio job in the thread pool.
     * 
     * @param playId the audio job's id.
     */
    public void cancelJob(String playId)
    {
        Vector jobs = this.audioThreadPool.getPendingJobs();
        for (int i = 0; i < jobs.size(); i++)
        {
            PlayerJob job = (PlayerJob) jobs.elementAt(i);
            if (job.playId.equals(playId))
            {
                job.cancel();
            }
        }
    }
    
    /**
     * check currently if it is playing the audio.
     * 
     * @return true means is playing the audio, false otherwise.
     */
    public boolean isPlaying()
    {
        return !this.audioThreadPool.isIdle();
    }
    
    /**
     * check currently if enable the audio. Sometimes will disable the audio such as it's calling phone, record audio etc..
     * 
     * @return true means enable the audio, false otherwise.
     */
    public boolean isEnabled()
    {
        return this.isEnabled;
    }
    
    /**
     * enable/disable the audio play.
     * 
     * @param isEnabled  true means enable the audio, false otherwise.
     */
    public void enable(boolean isEnabled)
    {
        this.isEnabled  = isEnabled;
        
        if(!this.isEnabled)
        {
            this.cancelAll();
        }
    }
    
    /**
     * check if there's task waiting for play.
     * @return
     */
    public boolean isNeedAbandonFocus()
    {
        if (audioThreadPool != null)
        {
            Vector jobs = audioThreadPool.getPendingJobs();
            if (jobs != null && jobs.size() > 0)
            {
                return false;
            }
        }

        return true;
    }
}
