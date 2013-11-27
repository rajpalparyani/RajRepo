/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * AndroidMediaPlayer.java
 *
 */
package com.telenav.media.android;

import java.io.FileOutputStream;

import android.content.Context;
import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;

import com.telenav.logger.Logger;
import com.telenav.media.ITnMediaListener;
import com.telenav.media.TnMediaException;
import com.telenav.media.TnMediaPlayer;

/**
 *@author fqming (fqming@telenav.cn)
 *@date Jul 26, 2010
 *@history add compatibility to sdk which can not support audio focus.

 */
class AndroidMediaPlayer extends TnMediaPlayer implements OnCompletionListener, OnErrorListener, OnInfoListener, OnPreparedListener
{
    protected Context context;
    protected String audioformat;
    protected byte[] audioData;
    protected String url;
    
    protected String fileName;
    
    protected MediaPlayer mediaPlayer;
    
    private static OnAudioFocusChangeListener audioFocusListener;
    
    private static boolean isSupportAudioFocus;
    
    private boolean isAllowAbandon;
    
    static {
        isSupportAudioFocus = true;
        try
        {
           
            Class.forName("android.media.AudioManager$OnAudioFocusChangeListener");
        }
        catch (Exception e) {
            isSupportAudioFocus = false;
        }
    }
    
    public AndroidMediaPlayer(final Context context, byte[] data, String audioformat) throws TnMediaException
    {
        this.context = context;
        this.audioformat = audioformat;
        this.audioData = data;
        isAllowAbandon = true;
        this.mediaPlayer = new MediaPlayer();
        if(isSupportAudioFocus && audioFocusListener == null)
        {
            audioFocusListener = new AudioManager.OnAudioFocusChangeListener()
            {
                public void onAudioFocusChange(int focusChange)
                {
                    AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                    if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT)
                    {
                        // loss focus temporally
                        isAllowAbandon = false;
                        Logger.log(Logger.INFO, this.getClass().getName(), "player focusChange: AUDIOFOCUS_LOSS_TRANSIENT");
                    }
                    else if (focusChange == AudioManager.AUDIOFOCUS_LOSS)
                    {
                        //loss focus forever.
                        Logger.log(Logger.INFO, this.getClass().getName(), "player focusChange: AUDIOFOCUS_LOSS");
                        audioManager.abandonAudioFocus((OnAudioFocusChangeListener) audioFocusListener);
                    }
                    else if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK)
                    {
                        //if support duck, reduce volume here.
                        Logger.log(Logger.INFO, this.getClass().getName(), "player focusChange: AUDIOFOCUS_LOSS");
                    }
                    else if (focusChange == AudioManager.AUDIOFOCUS_GAIN)
                    {
                        //scout get focus again. should give up immediately.
                        Logger.log(Logger.INFO, this.getClass().getName(), "player focusChange: AUDIOFOCUS_GAIN");
                        audioManager.requestAudioFocus(null, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
                        audioManager.abandonAudioFocus((OnAudioFocusChangeListener) audioFocusListener);
                    }
                }
            };
        }
    }
    
    public AndroidMediaPlayer(Context context, String url, String audioformat) throws TnMediaException
    {
        this.context = context;
        this.audioformat = audioformat;
        this.url = url;
        
        this.mediaPlayer = new MediaPlayer();
        if(isSupportAudioFocus && audioFocusListener == null)
        {
            audioFocusListener = new AudioManager.OnAudioFocusChangeListener()
            {
                public void onAudioFocusChange(int arg0)
                {
                }
            };
        }
    }

    public void close() throws TnMediaException
    {
        try
        {
            this.mediaPlayer.release();

            // should be unsynchronized here
            Thread t = new Thread(new Runnable()
            {
                public void run()
                {
                    if (mediaListener != null)
                    {
                        mediaListener.mediaUpdate(AndroidMediaPlayer.this, ITnMediaListener.ON_CLOSE, null);
                    }
                }
            });
            t.start();
        }
        catch (Throwable e)
        {
            Logger.log(this.getClass().getName(), e);

            throw new TnMediaException(e.getMessage());
        }
    }

    public String getContentType()
    {
        return this.audioformat;
    }

    public void prefetch() throws TnMediaException
    {
        try
        {
            this.mediaPlayer.setOnCompletionListener(this);
            this.mediaPlayer.setOnErrorListener(this);
            this.mediaPlayer.setOnInfoListener(this);
            this.mediaPlayer.setOnPreparedListener(this);

            if (this.fileName != null)
            {
                this.mediaPlayer.setDataSource(this.fileName);
            }
            else
            {
                this.mediaPlayer.setDataSource(this.context, Uri.parse(this.url));
            }

            this.mediaPlayer.prepare();
        }
        catch (Throwable e)
        {
            Logger.log(this.getClass().getName(), e);
            
            throw new TnMediaException(e.getMessage());
        }
    }

    public void realize() throws TnMediaException
    {        
        if (this.audioData != null)
        {
            FileOutputStream fos = null;
            
            try
            {
                requestAudioFocus();

                this.fileName = "temp_media" + ".audio";
                this.context.deleteFile(this.fileName);
                fos = this.context.openFileOutput(this.fileName, Context.MODE_WORLD_READABLE);

                if (fos != null)
                {
                    fos.write(this.audioData);
                    
                    fos.close();
                    fos = null;
                }

                this.fileName = this.context.getFilesDir().getAbsolutePath() + "/" + this.fileName;
                
                this.audioData = null;
            }
            catch (Throwable e)
            {
                Logger.log(this.getClass().getName(), e);
                
                throw new TnMediaException(e.getMessage());
            }
        }
    }

    public void reset() throws TnMediaException
    {
        try
        {
            this.mediaPlayer.reset();
        }
        catch (Throwable e)
        {
            Logger.log(this.getClass().getName(), e);

            throw new TnMediaException(e.getMessage());
        }
    }

    public void start() throws TnMediaException
    {
        try
        {
            this.mediaPlayer.start();
        }
        catch (Throwable e)
        {
            Logger.log(this.getClass().getName(), e);

            throw new TnMediaException(e.getMessage());
        }
    }

    public void stop() throws TnMediaException
    {
        try
        {
            this.mediaPlayer.stop();
        }
        catch (Throwable e)
        {
            Logger.log(this.getClass().getName(), e);

            throw new TnMediaException(e.getMessage());
        }
        
    }
    
    public void onCompletion(MediaPlayer mp)
    {
        if(this.mediaListener != null)
        {
            this.mediaListener.mediaUpdate(this, ITnMediaListener.ON_COMPLETION, null);
        }
    }

    public boolean onError(MediaPlayer mp, int what, int extra)
    {
        if(this.mediaListener != null)
        {
            this.mediaListener.mediaUpdate(this, ITnMediaListener.ON_ERROR, what + "," + extra);
        }
        
        return true;
    }

    public boolean onInfo(MediaPlayer mp, int what, int extra)
    {
        if(this.mediaListener != null)
        {
            this.mediaListener.mediaUpdate(this, ITnMediaListener.ON_INFO, what + "," + extra);
        }
        
        return true;
    }

    public void onPrepared(MediaPlayer mp)
    {
        if(this.mediaListener != null)
        {
            this.mediaListener.mediaUpdate(this, ITnMediaListener.ON_PREPARE, null);
        }
    }
    
  //This function is only supported since 2.2 or above.
    public void abandonAudioFocus()
    {
        if (isSupportAudioFocus)
        {
            AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            audioManager.abandonAudioFocus((OnAudioFocusChangeListener) audioFocusListener);
        }
    }
    
    //This function is only supported since 2.2 or above.
    private void requestAudioFocus()
    {
        if(isSupportAudioFocus)
        {
            AudioManager audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
            audioManager.requestAudioFocus(
                    (OnAudioFocusChangeListener)audioFocusListener,
                    AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
        }
    }

    @Override
    public boolean isAllowAbandon()
    {
        return isAllowAbandon;
    }
}
