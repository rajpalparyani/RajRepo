/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * TnDatagramConnection.java
 *
 */
package com.telenav.network;

import java.io.IOException;

/**
 * This interface defines the capabilities that a datagram connection must have. <br />
 * <br />
 * The syntax described here for the datagram URL connection string is also valid for the Datagram.setAddress() method
 * used to assign a destination address to a Datagram to be sent. e.g., datagram://host:port
 * 
 *@author fqming (fqming@telenav.cn)
 *@date Jul 13, 2010
 */
public interface TnDatagramConnection extends TnConnection
{
    /**
     * Make a new datagram object.
     * 
     * @param buf The buffer to be used in the datagram.
     * @param size The length of the buffer to be allocated for the datagram.
     * @return A new datagram
     * @throws IOException If an I/O error occurs.
     */
    public TnDatagram newDatagram(byte[] buf, int size) throws IOException;

    /**
     * Make a new datagram object automatically allocating a buffer.
     * 
     * @param size The length of the buffer to be allocated for the datagram.
     * @return A new datagram
     * @throws IOException If an I/O error occurs.
     */
    public TnDatagram newDatagram(int size) throws IOException;

    /**
     * Receives a packet from this socket and stores it in the argument pack. All fields of pack must be set according
     * to the data received. If the received data is longer than the packet buffer size it is truncated. This method
     * blocks until a packet is received or a timeout has expired.
     * 
     * @param pack the TnDatagram to store the received data.
     * @throws IOException if an error occurs while receiving the packet.
     */
    public void receive(TnDatagram pack) throws IOException;

    /**
     * Sends a packet over this socket. The packet must satisfy the security policy before it may be sent. If a security
     * manager is installed, this method checks whether it is allowed to send this packet to the specified address.
     * 
     * @param pack the TnDatagram which has to be sent.
     * @throws IOException if an error occurs while receiving the packet.
     */
    public void send(TnDatagram pack) throws IOException;

    /**
     * Gets the local address to which the datagram connection is bound.
     * 
     * The host address(IP number) that can be used to connect to this end of the datagram connection from an external
     * system. Since IP addresses may be dynamically assigned, a remote application will need to be robust in the face
     * of IP number reassignment.
     * 
     * @return the local address to which the datagram connection is bound.
     * @throws IOException if the connection was closed.
     */
    public String getLocalAddress() throws IOException;

    /**
     * Returns the local port to which this datagram connection is bound.
     * 
     * @return the local port number to which this datagram connection is connected.
     * @throws IOException if the connection was closed.
     */
    public int getLocalPort() throws IOException;
}
