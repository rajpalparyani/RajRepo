/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * TnSocketConnection.java
 *
 */
package com.telenav.network;

import java.io.IOException;

/**
 * This interface defines the socket stream connection. <br />
 * <br />
 * A socket is accessed using a generic connection string with an explicit host and port number. The host may be
 * specified as a fully qualified host name or IPv4 number. e.g. socket://host.com:79 defines a target socket on the
 * host.com system at port 79. <br />
 * <br />
 * Every StreamConnection provides a Connection object as well as an InputStream and OutputStream to handle the I/O
 * associated with the connection. Each of these interfaces has its own close() method. For systems that support duplex
 * communication over the socket connection, closing of the input or output stream SHOULD shutdown just that side of the
 * connection. e.g. closing the InputStream will permit the OutputStream to continue sending data.
 * 
 * @author fqming (fqming@telenav.cn)
 *@date Jul 10, 2010
 */
public interface TnSocketConnection extends TnStreamConnection
{
    /**
     * Socket option for the small buffer writing delay. Set to zero to disable Nagle algorithm for small buffer
     * operations. Set to a non-zero value to enable.
     */
    public static final byte DELAY = 0;

    /**
     * Socket option for the linger time to wait in seconds before closing a connection with pending data output.
     * Setting the linger time to zero disables the linger wait interval.
     */
    public static final byte LINGER = 1;
    
    /**
     * Socket option for the keep alive feature (2). Setting KEEPALIVE to zero will disable the feature. Setting
     * KEEPALIVE to a non-zero value will enable the feature.
     */
    public static final byte KEEPALIVE = 2;

    /**
     * Socket option for the size of the receiving buffer.
     */
    public static final byte RCVBUF = 3;
    
    /**
     * Socket option for the size of the sending buffer.
     */
    public static final byte SNDBUF = 4;
    
    /**
     * Gets the remote address to which the socket is bound. The address can be either the remote host name or the IP
     * address(if available).
     * 
     * @return the remote address to which the socket is bound.
     * @throws IOException if the connection was closed.
     */
    public String getAddress() throws IOException;

    /**
     * Gets the local address to which the socket is bound. <br />
     * The host address(IP number) that can be used to connect to this end of the socket connection from an external
     * system. Since IP addresses may be dynamically assigned, a remote application will need to be robust in the face
     * of IP number reasssignment.
     * 
     * @return the local address to which the socket is bound.
     * @throws IOException if the connection was closed.
     */
    public String getLocalAddress() throws IOException;

    /**
     * Returns the local port to which this socket is bound.
     * 
     * @return the local port number to which this socket is connected.
     * @throws IOException if the connection was closed.
     */
    public int getLocalPort() throws IOException;

    /**
     * Returns the remote port to which this socket is bound.
     * 
     * @return the remote port number to which this socket is connected.
     * @throws IOException if the connection was closed.
     */
    public int getPort() throws IOException;

    /**
     * Get a socket option for the connection.
     * 
     * @param option socket option identifier (KEEPALIVE, LINGER, SNDBUF, RCVBUF, or DELAY)
     * @return numeric value for specified option or -1 if the value is not available.
     * @throws IllegalArgumentException
     * @throws IOException
     */
    public int getSocketOption(byte option) throws IllegalArgumentException, IOException;

    /**
     * Set a socket option for the connection. <br />
     * Options inform the low level networking code about intended usage patterns that the application will use in
     * dealing with the socket connection. <br />
     * Calling setSocketOption to assign buffer sizes is a hint to the platform of the sizes to set the underlying
     * network I/O buffers. Calling getSocketOption can be used to see what sizes the system is using. The system MAY
     * adjust the buffer sizes to account for better throughput available from Maximum Transmission Unit (MTU) and
     * Maximum Segment Size (MSS) data available from current network information.
     * 
     * @param option socket option identifier (KEEPALIVE, LINGER, SNDBUF, RCVBUF, or DELAY)
     * @param value numeric value for specified option
     * @throws IllegalArgumentException if the value is not valid (e.g. negative value) or if the option identifier is
     *             not valid
     * @throws IOException if the connection was closed
     */
    public void setSocketOption(byte option, int value) throws IllegalArgumentException, IOException;
}
