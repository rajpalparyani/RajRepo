/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * TnBase64OutputStream.java
 *
 */
package com.telenav.io;

import java.io.IOException;

/**
 * An OutputStream that does Base64 encoding on the data written to it, writing the resulting data to another OutputStream. 
 * 
 *@author fqming (fqming@telenav.cn)
 *@date Jul 12, 2010
 */
public interface TnBase64OutputStream
{
    /**
     * Writes the specified byte to this output stream. The general contract for write is that one byte is written to the output stream. The byte to be written is the eight low-order bits of the argument b. The 24 high-order bits of b are ignored. 
     * 
     * @param i the byte.
     * @throws IOException if an I/O error occurs. In particular, an IOException may be thrown if the output stream has been closed.
     */
    public void write(int i) throws IOException;

    /**
     * Writes b.length bytes from the specified byte array to this output stream. The general contract for write(b) is
     * that it should have exactly the same effect as the call write(b, 0, b.length).
     * 
     * @param data the data.
     * @throws IOException if an I/O error occurs.
     */
    public void write(byte[] data) throws IOException;

    /**
     * Writes len bytes from the specified byte array starting at offset off to this output stream. The general contract
     * for write(b, off, len) is that some of the bytes in the array b are written to the output stream in order;
     * element b[off] is the first byte written and b[off+len-1] is the last byte written by this operation.
     * <br />
     * The write method of OutputStream calls the write method of one argument on each of the bytes to be written out.
     * Subclasses are encouraged to override this method and provide a more efficient implementation.
     * <br />
     * If b is null, a NullPointerException is thrown.
     * <br />
     * If off is negative, or len is negative, or off+len is greater than the length of the array b, then an
     * IndexOutOfBoundsException is thrown.
     * 
     * @param data the data.
     * @param dataOffset the start offset in the data.
     * @param dataLength the number of bytes to write.
     * @throws IOException if an I/O error occurs. In particular, an IOException is thrown if the output stream is closed.
     */
    public void write(byte[] data, int dataOffset, int dataLength) throws IOException;

    /**
     * Flushes this output stream and forces any buffered output bytes to be written out. The general contract of flush
     * is that calling it is an indication that, if any bytes previously written have been buffered by the
     * implementation of the output stream, such bytes should immediately be written to their intended destination.
     * <br />
     * The flush method of OutputStream does nothing.
     * 
     * @throws IOException if an I/O error occurs.
     */
    public void flush() throws IOException;

    /**
     * Closes this output stream and releases any system resources associated with this stream. The general contract of
     * close is that it closes the output stream. A closed stream cannot perform output operations and cannot be
     * reopened.
     * 
     * @throws IOException if an I/O error occurs.
     */
    public void close() throws IOException;
}
