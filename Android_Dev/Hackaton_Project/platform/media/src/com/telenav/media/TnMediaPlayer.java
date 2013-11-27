/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * TnMediaPlayer.java
 *
 */
package com.telenav.media;

/**
 * TnMediaPlayer controls the rendering of time based media data. It provides the methods to manage the Player's life
 * cycle, controls the playback progress and obtains the presentation components.
 * 
 * @author fqming (fqming@telenav.cn)
 *@date Jul 26, 2010
 */
public abstract class TnMediaPlayer
{
    protected ITnMediaListener mediaListener;

    /**
     * Add a media listener for this player.
     * 
     * @param mediaListener the listener to add. If null is used, the request will be ignored.
     */
    public void setListener(ITnMediaListener mediaListener)
    {
        this.mediaListener = mediaListener;
    }

    /**
     * Retrieve the media listener for this player.
     * 
     * @return The media listener for this player.
     */
    public ITnMediaListener getListener()
    {
        return this.mediaListener;
    }

    /**
     * Constructs portions of the Player without acquiring the scarce and exclusive resources. This may include
     * examining media data and may take some time to complete.
     * 
     * @throws TnMediaException Thrown if the Player cannot be realized.
     */
    public abstract void realize() throws TnMediaException;

    /**
     * Acquires the scarce and exclusive resources and processes as much data as necessary to reduce the start latency.
     * 
     * @throws TnMediaException Thrown if the Player cannot be prefetched.
     */
    public abstract void prefetch() throws TnMediaException;

    /**
     * Starts the player as soon as possible. If the Player was previously stopped by calling stop, it will resume
     * playback from where it was previously stopped. If the Player has reached the end of media, calling start will
     * automatically start the playback from the start of the media.
     * 
     * @throws TnMediaException Thrown if the Player cannot be started.
     */
    public abstract void start() throws TnMediaException;

    /**
     * Stops the Player. It will pause the playback at the current media time.
     * 
     * @throws TnMediaException Thrown if the Player cannot be stopped.
     */
    public abstract void stop() throws TnMediaException;
    
    public abstract void abandonAudioFocus() throws TnMediaException;

    /**
     * Close the Player and release its resources. <br />
     * If close is called on a closed Player the request is ignored.
     * 
     * @throws TnMediaException Thrown if the Player cannot be closed.
     */
    public abstract void close() throws TnMediaException;

    /**
     * Resets the MediaPlayer to its uninitialized state. After calling this method, you will have to initialize it
     * again by setting the data source and calling prepare().
     * 
     * @throws TnMediaException Thrown if the Player cannot be reset.
     */
    public abstract void reset() throws TnMediaException;

    /**
     * Retrieve the content type of this player.
     * 
     * @return the content type of this player.
     */
    public abstract String getContentType();
    
    public abstract boolean isAllowAbandon();
}
