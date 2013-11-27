/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * TnMediaRecorder.java
 *
 */
package com.telenav.media;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

/**
 * Used to record audio.
 * 
 *@author fqming (fqming@telenav.cn)
 *@date Jul 26, 2010
 */
public abstract class TnMediaRecorder extends TnMediaPlayer
{
    protected int recordSizeLimit = Integer.MAX_VALUE;

    protected OutputStream os;

    protected String audioFormat;

    /**
     * construct a recorder with special audio format.
     * 
     * @param format the audio format.
     */
    public TnMediaRecorder(String format)
    {
        this.audioFormat = format;

        this.os = new ByteArrayOutputStream();
    }
    
    public abstract void commit() throws TnMediaException;

    /**
     * Set the record size limit. This limits the size of the recorded media to the number of bytes specified.
     * 
     * @param size The record size limit in number of bytes.
     * @throws TnMediaException Thrown if setting the record size limit is not supported.
     */
    public final void setRecordSizeLimit(int size) throws TnMediaException
    {
        if (size <= 0)
        {
            throw new IllegalArgumentException("size limit is <= 0.");
        }

        this.recordSizeLimit = size;

        setRecordSizeLimitDelegate();
    }

    /**
     * the delegate of {@link #setRecordSizeLimit(int)}.
     * 
     * @throws TnMediaException Thrown if setting the record size limit is not supported.
     */
    protected abstract void setRecordSizeLimitDelegate() throws TnMediaException;

    /**
     * Set the output stream where the data will be recorded.
     * 
     * Whenever possible, the recording format is the same as the format of the input media. In some cases, the
     * recording format may be different from the input format if the input format is not a recordable format, e.g.
     * streaming media data. An application can query the recorded format by calling the getContentType method.
     * 
     * @param os The output stream where the data will be recorded.
     */
    public void setRecordStream(OutputStream os)
    {
        if (os == null)
        {
            throw new IllegalArgumentException("the output stream is null.");
        }

        this.os = os;
    }

    /**
     * Retrieve the output stream where the data will be recorded.
     * 
     * @return The output stream where the data will be recorded.
     */
    public OutputStream getRecordStream()
    {
        return this.os;
    }

    /**
     * Retrieve the content type of this player.
     * 
     * @return the content type of this player.
     */
    public String getContentType()
    {
        return this.audioFormat;
    }
}
