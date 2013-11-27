/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * TnDatagram.java
 *
 */
package com.telenav.network;

/**
 * This is the generic datagram interface. It represents an object that will act as the holder of data to be sent or
 * received from a datagram connection.
 * 
 *@author fqming (fqming@telenav.cn)
 *@date Jul 13, 2010
 */
public interface TnDatagram
{
    /**
     * Gets the data of this datagram packet.
     * 
     * @return the received data or the data to be sent. 
     */
    public byte[] getData();

    /**
     * Gets the length of the data stored in this datagram packet.
     * 
     * @return the length of the received data or the data to be sent. 
     */
    public int getLength();

    /**
     * Gets the offset of the data stored in this datagram packet.
     * 
     * @return the position of the received data or the data to be sent. 
     */
    public int getOffset();

    /**
     * Sets the data buffer for this datagram packet.
     * 
     * @param buffer the buffer to store the data.
     * @param offset the buffer offset where the data is stored.
     * @param len the length of the data to be sent or the length of buffer to store the received data. 
     */
    public void setData(byte[] buffer, int offset, int len);

    /**
     * Sets the length of the datagram packet. This length plus the offset must be lesser than or equal to the buffer size.
     * 
     * @param len the length of this datagram packet. 
     */
    public void setLength(int len);
}
