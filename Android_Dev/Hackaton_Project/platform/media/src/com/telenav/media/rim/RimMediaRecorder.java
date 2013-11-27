/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * RimMediaRecorder.java
 *
 */
package com.telenav.media.rim;

import javax.microedition.media.Manager;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;
import javax.microedition.media.PlayerListener;
import javax.microedition.media.control.RecordControl;

import com.telenav.logger.Logger;
import com.telenav.media.ITnMediaListener;
import com.telenav.media.TnMediaException;
import com.telenav.media.TnMediaRecorder;

/**
 *@author fqming (fqming@telenav.cn)
 *@date Aug 17, 2010
 */
class RimMediaRecorder extends TnMediaRecorder implements PlayerListener
{
    protected Player player;
    protected RecordControl recordControl;
    
    public RimMediaRecorder(String format) throws TnMediaException
    {
        super(format);

        try
        {
            this.player = Manager.createPlayer("capture://audio");
            this.player.addPlayerListener(this);
        }
        catch (Exception e)
        {
            Logger.log(this.getClass().getName(), e);

            throw new TnMediaException(e.getMessage());
        }
    }

    public void commit() throws TnMediaException
    {
        try
        {
            this.recordControl.commit();
        }
        catch (Exception e)
        {
            Logger.log(this.getClass().getName(), e);

            throw new TnMediaException(e.getMessage());
        }
    }

    protected void setRecordSizeLimitDelegate() throws TnMediaException
    {
        try
        {
            if(this.recordControl != null)
            {
                this.recordControl.setRecordSizeLimit(this.recordSizeLimit);
            }
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
            this.recordControl = (RecordControl)player.getControl("RecordControl");
            this.recordControl.setRecordSizeLimit(this.recordSizeLimit);
            this.recordControl.setRecordStream(this.os);
            this.recordControl.startRecord();
            
            this.player.start();
        }
        catch (Exception e)
        {
            Logger.log(this.getClass().getName(), e);

            throw new TnMediaException(e.getMessage());
        }
    }

    public void stop() throws TnMediaException
    {
        try
        {
            this.recordControl.stopRecord();
            
            this.player.stop();
        }
        catch (Exception e)
        {
            Logger.log(this.getClass().getName(), e);

            throw new TnMediaException(e.getMessage());
        }
    }

    public void playerUpdate(Player player, String event, Object eventData)
    {
        if (this.mediaListener != null)
        {
            if (PlayerListener.ERROR.equals(event) || PlayerListener.RECORD_ERROR.equals(event)
                    || PlayerListener.DEVICE_UNAVAILABLE.equals(event))
            {
                this.mediaListener.mediaUpdate(this, ITnMediaListener.ON_ERROR, eventData);
            }
            else if (PlayerListener.END_OF_MEDIA.equals(event) || PlayerListener.RECORD_STOPPED.equals(event)
                    || PlayerListener.STOPPED.equals(event) || PlayerListener.STOPPED_AT_TIME.equals(event))
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
