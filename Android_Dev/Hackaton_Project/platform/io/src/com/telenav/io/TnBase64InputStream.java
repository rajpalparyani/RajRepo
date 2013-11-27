/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * TnBase64InputStream.java
 *
 */
package com.telenav.io;

import java.io.IOException;

/**
 * An InputStream that does Base64 decoding on the data read through it. 
 * 
 *@author fqming (fqming@telenav.cn)
 *@date Jul 12, 2010
 */
public interface TnBase64InputStream
{
    /**
     * Reads the next byte of data from the input stream. If no byte is available because the end of the stream has been
     * reached, the value -1 is returned. This method blocks until input data is available, the end of the stream is
     * detected, or an exception is thrown.
     * 
     * @return the next byte of data, or -1 if the end of the stream is reached.
     * @throws IOException if an I/O error occurs.
     */
    public int read() throws IOException;

    /**
     * Reads some number of bytes from the input stream and stores them into the buffer array b. The number of bytes
     * actually read is returned as an integer. This method blocks until input data is available, end of file is
     * detected, or an exception is thrown.
     * <br />
     * If b is null, a NullPointerException is thrown. If the length of b is zero, then no bytes are read and 0 is
     * returned; otherwise, there is an attempt to read at least one byte. If no byte is available because the stream is
     * at end of file, the value -1 is returned; otherwise, at least one byte is read and stored into b.
     * <br />
     * The first byte read is stored into element b[0], the next one into b[1], and so on. The number of bytes read is,
     * at most, equal to the length of b. Let k be the number of bytes actually read; these bytes will be stored in
     * elements b[0] through b[k-1], leaving elements b[k] through b[b.length-1] unaffected.
     * <br />
     * If the first byte cannot be read for any reason other than end of file, then an IOException is thrown. In
     * particular, an IOException is thrown if the input stream has been closed.
     * 
     * @param buffer the buffer into which the data is read.
     * @return the total number of bytes read into the buffer, or -1 is there is no more data because the end of the stream has been reached.
     * @throws IOException if an I/O error occurs.
     */
    public int read(byte[] buffer) throws IOException;

    /**
     * Reads up to len bytes of data from the input stream into an array of bytes. An attempt is made to read as many as
     * len bytes, but a smaller number may be read, possibly zero. The number of bytes actually read is returned as an
     * integer.
     * <br />
     * This method blocks until input data is available, end of file is detected, or an exception is thrown.
     * <br />
     * If b is null, a NullPointerException is thrown.
     * <br />
     * If off is negative, or len is negative, or off+len is greater than the length of the array b, then an
     * IndexOutOfBoundsException is thrown.
     * <br />
     * If len is zero, then no bytes are read and 0 is returned; otherwise, there is an attempt to read at least one
     * byte. If no byte is available because the stream is at end of file, the value -1 is returned; otherwise, at least
     * one byte is read and stored into b.
     * <br />
     * The first byte read is stored into element b[off], the next one into b[off+1], and so on. The number of bytes
     * read is, at most, equal to len. Let k be the number of bytes actually read; these bytes will be stored in
     * elements b[off] through b[off+k-1], leaving elements b[off+k] through b[off+len-1] unaffected.
     * <br />
     * In every case, elements b[0] through b[off] and elements b[off+len] through b[b.length-1] are unaffected.
     * <br />
     * If the first byte cannot be read for any reason other than end of file, then an IOException is thrown. In
     * particular, an IOException is thrown if the input stream has been closed.
     * <br />
     * The read(b, off, len) method for class InputStream simply calls the method read() repeatedly. If the first such
     * call results in an IOException, that exception is returned from the call to the read(b, off, len) method. If any
     * subsequent call to read() results in a IOException, the exception is caught and treated as if it were end of
     * file; the bytes read up to that point are stored into b and the number of bytes read before the exception
     * occurred is returned. Subclasses are encouraged to provide a more efficient implementation of this method.
     * 
     * @param buffer the buffer into which the data is read.
     * @param bufferOffset the start offset in array b  at which the data is written.
     * @param bufferLength the maximum number of bytes to read.
     * @return the total number of bytes read into the buffer, or -1 if there is no more data because the end of the stream has been reached.
     * @throws IOException if an I/O error occurs.
     */
    public int read(byte[] buffer, int bufferOffset, int bufferLength) throws IOException;

    /**
     * Returns the number of bytes that can be read (or skipped over) from this input stream without blocking by the
     * next caller of a method for this input stream. The next caller might be the same thread or or another thread.
     * <br />
     * The available method for class InputStream always returns 0.
     * <br />
     * This method should be overridden by subclasses.
     * 
     * @return the number of bytes that can be read from this input stream without blocking.
     * @throws IOException if an I/O error occurs.
     */
    public int available() throws IOException;

    /**
     * Closes this input stream and releases any system resources associated with the stream. 
     * 
     * @throws IOException if an I/O error occurs.
     */
    public void close() throws IOException;

    /**
     * Repositions this stream to the position at the time the mark method was last called on this input stream.
     *  
     * @throws IOException if this stream has not been marked or if the mark has been invalidated.
     */
    public void reset() throws IOException;
}