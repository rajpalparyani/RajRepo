/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * RimMediaPlayer.java
 *
 */
package com.telenav.media.rim;

import java.io.ByteArrayInputStream;

import javax.microedition.media.Manager;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;
import javax.microedition.media.PlayerListener;

import com.telenav.logger.Logger;
import com.telenav.media.ITnMediaListener;
import com.telenav.media.TnMediaException;
import com.telenav.media.TnMediaPlayer;

/**
 *@author fqming (fqming@telenav.cn)
 *@date Aug 17, 2010
 */
class RimMediaPlayer extends TnMediaPlayer implements PlayerListener
{
    protected String audioformat;
    protected byte[] audioData;
    protected String url;
    
    protected Player player;
    
    public RimMediaPlayer(byte[] data, String audioformat) throws TnMediaException
    {
        this.audioformat = audioformat;
        this.audioData = data;
        
        try
        {
            this.player = Manager.createPlayer(new ByteArrayInputStream(data), audioformat);
            this.player.addPlayerListener(this);
        }
        catch (Exception e)
        {
            Logger.log(this.getClass().getName(), e);
            
            throw new TnMediaException(e.getMessage());
        }
    }
    
    public RimMediaPlayer(String url, String audioformat) throws TnMediaException
    {
        this.audioformat = audioformat;
        this.url = url;
        
        try
        {
            this.player = Manager.createPlayer(url);
            this.player.addPlayerListener(this);
        }
        catch (Exception e)
        {
            Logger.log(this.getClass().getName(), e);

            throw new TnMediaException(e.getMessage());
        }
    }
    
    public void close() throws TnMediaException
    {
        try
        {
            this.player.close();
        }
        catch (Exception e)
        {
            Logger.log(this.getClass().getName(), e);

            throw new TnMediaException(e.getMessage());
        }
    }

    public String getContentType()
    {
        return this.player.getContentType();
    }

    public void prefetch() throws TnMediaException
    {
        try
        {
            this.player.prefetch();
        }
        catch (MediaException e)
        {
            Logger.log(this.getClass().getName(), e);

            throw new TnMediaException(e.getMessage());
        }
    }

    public void realize() throws TnMediaException
    {
        try
        {
            this.player.realize();
        }
        catch (MediaException e)
        {
            Logger.log(this.getClass().getName(), e);

            throw new TnMediaException(e.getMessage());
        }
    }

    public void reset() throws TnMediaException
    {
        try
        {
            this.player.deallocate();
        }
        catch (Exception e)
        {
            Logger.log(this.getClass().getName(), e);

            throw new TnMediaException(e.getMessage());
        }
    }

    public void start() throws TnMediaException
    {
        RimMediaUtil.setVolume(this.player);
        RimMediaUtil.setAudioPath(this.player);
        
        try
        {
            this.player.start();
        }
        catch (MediaException e)
        {
            Logger.log(this.getClass().getName(), e);

            throw new TnMediaException(e.getMessage());
        }
    }

    public void stop() throws TnMediaException
    {
        try
        {
            this.player.stop();
        }
        catch (MediaException e)
        {
            Logger.log(this.getClass().getName(), e);

            throw new TnMediaException(e.getMessage());
        }
    }

    public void playerUpdate(Player player, String event, Object eventData)
    {
        if(this.mediaListener != null)
        {
            if (PlayerListener.ERROR.equals(event) || PlayerListener.DEVICE_UNAVAILABLE.equals(event))
            {
                this.mediaListener.mediaUpdate(this, ITnMediaListener.ON_ERROR, eventData);
            }
            else if (PlayerListener.END_OF_MEDIA.equals(event) || PlayerListener.STOPPED.equals(event)
                    || PlayerListener.STOPPED_AT_TIME.equals(event))
            {
                this.mediaListener.mediaUpdate(this, ITnMediaListener.ON_COMPLETION, eventData);
            }
            else if (PlayerListener.CLOSED.equals(event))
            {
                this.mediaListener.mediaUpdate(this, ITnMediaListener.ON_CLOSE, eventData);
            }
            else if (PlayerListener.BUFFERING_STARTED.equals(event))
            {
                this.mediaListener.mediaUpdate(this, ITnMediaListener.ON_PREPARE, eventData);
            }
            else
            {
                this.mediaListener.mediaUpdate(this, ITnMediaListener.ON_INFO, eventData);
            }
        }
    }
	
	public void abandonAudioFocus() throws TnMediaException
    {
        // TODO Auto-generated method stub
        
    }

}
