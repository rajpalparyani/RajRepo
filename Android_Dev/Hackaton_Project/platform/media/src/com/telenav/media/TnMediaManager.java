/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * TnMediaManager.java
 *
 */
package com.telenav.media;

/**
 * This class provides access to the system media services.
 * <br />
 * For RIM platform, currently seems that don't support {@link #FORMAT_3GPP}. <br />
 * 
 *@author fqming (fqming@telenav.cn)
 *@date Jul 26, 2010
 */
public abstract class TnMediaManager
{
    /**
     * audio path from Bluetooth headset.
     */
    public final static int PATH_BLUETOOTH_HEADSET = 0;

    /**
     * audio path from speaker phone.
     */
    public final static int PATH_SPEAKER_PHONE = 1;

    /**
     * audio path from wired headset.
     */
    public final static int PATH_WIRED_HEADSET = 2;
    
    /**
     * audio format: 3GPP
     */
    public final static String FORMAT_3GPP = "audio/3gpp";

    /**
     * audio format: MP3
     */
    public final static String FORMAT_MP3 = "audio/mpeg";
    
    /**
     * audio format: AMR
     */
    public final static String FORMAT_AMR = "audio/amr";
    
    /**
     * audio format: PCM
     */
    public final static String FORMAT_PCM = "audio/wav";

    private static TnMediaManager mediaManager;
    private static int initCount;
    
    /**
     * Retrieve the instance of media manager.
     * 
     * @return {@link TnMediaManager}
     */
    public static TnMediaManager getInstance()
    {
        return mediaManager;
    }

    /**
     * Before invoke the methods of this manager, need init the native manager of the platform first.
     * 
     * @param mediaMngr This manager is native manager of platforms. Such as {@link AndroidMediaManager} etc.
     */
    public synchronized static void init(TnMediaManager mediaMngr)
    {
        if(initCount >= 1)
            return;
        
        mediaManager = mediaMngr;
        initCount++;
    }

    /**
     * Create a Player to play back media from a byte array.<br />
     * 
     * <br />
     * The type argument specifies the content-type of the input media. If null is given, Manager will attempt to
     * determine the type. However, since determining the media type is non-trivial for some media types, it may not be
     * feasible in some cases. The Manager may throw a {@link TnMediaException} to indicate that.
     * 
     * @param audios the audio data.
     * @param format the audio data's format.
     * @return A new Player.
     * 
     * @throws TnMediaException Thrown if the Player cannot be created.
     */
    public abstract TnMediaPlayer createPlayer(byte[] audios, String format) throws TnMediaException;
    
    /**
     * Create a Player from an input locator.<br />
     * 
     * <br />
     * The type argument specifies the content-type of the input media. If null is given, Manager will attempt to
     * determine the type. However, since determining the media type is non-trivial for some media types, it may not be
     * feasible in some cases. The Manager may throw a {@link TnMediaException} to indicate that.
     * 
     * @param url A locator string in URI syntax that describes the media content.
     * @param format the audio data's format.
     * @return A new Player.
     * 
     * @throws TnMediaException Thrown if the Player cannot be created.
     */
    public abstract TnMediaPlayer createPlayer(String url, String format) throws TnMediaException;
    
    /**
     * Create a recorder with special audio format. <br />
     * 
     * @param format the audio data's format.
     * @return A new recorder.
     * 
     * @throws TnMediaException Thrown if the recorder cannot be created.
     */
    public abstract TnMediaRecorder createRecorder(String format) throws TnMediaException;
    
    /**
     * Checks whether audio routing to the Bluetooth headset is on or off.
     * 
     * @return true if audio is being routed to/from Bluetooth headset; false if otherwise
     */
    public abstract boolean isBluetoothHeadsetOn();
    
    /**
     * Checks whether audio routing to the wired headset is on or off.
     * 
     * @return true if audio is being routed to/from wired headset; false if otherwise
     */
    public abstract boolean isWiredHeadsetOn();
    
    /**
     * Set the audio routing to the special path.
     * 
     * @param path the routing path.
     */
    public abstract void setAudioPath(int path);

    /**
     * Retrieve the audio's routing path.
     * 
     * @return the routing path.
     */
    public abstract int getAudioPath();
    
    /**
     * Sets the volume for the media player and recorder.
     * 
     * @param volume The volume to set.
     * @param volumeUi The volume Ui will be shown. if the parameter is not null, For android, will use native ui; For
     *            rim, need implement the interface{@link ITnVolumeUi}.
     */
    public abstract void setMediaVolume(int volume, ITnVolumeUi volumeUi);
    
    /**
     * Returns the current volume for the media player and recorder.
     * 
     * @return the current volume.
     */
    public abstract int getMediaVolume();
    
    /**
     * Volume UI change interface. <br />
     * <br />
     * At android platform, will use the native ui, not need implement the detail.
     * 
     * @author fqming
     *
     */
    public interface ITnVolumeUi
    {
        /**
         * show the volume's ui.
         * 
         * @param volume the current volume
         */
        public void show(int volume);
        
        /**
         * hide the volume's ui.
         */
        public void hide();
    }
}
