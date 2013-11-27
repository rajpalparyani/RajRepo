/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * TnContentConnection.java
 *
 */
package com.telenav.network;

/**
 * This interface defines the stream connection over which content is passed. <br />
 * <br />
 * <b>Blocking Operations</b> <br />
 * This interface performs blocking Input and Output operations. An application will lock if an implementation of this
 * interface opens a connection from within the main event thread. Prevent an application from locking by opening a
 * connection from within a thread that is separate from the main event thread.
 * 
 * @author fqming (fqming@telenav.cn)
 *@date Jul 10, 2010
 */
public interface TnContentConnection extends TnStreamConnection
{
    /**
     * Returns a string describing the encoding of the content which the resource connected to is providing. E.g. if the
     * connection is via HTTP, the value of the content-encoding header field is returned.
     * 
     * @return the content encoding of the resource that the URL references, or null if not known.
     */
    public String getEncoding();

    /**
     * Returns the length of the content which is being provided. E.g. if the connection is via HTTP, then the value of
     * the content-length header field is returned.
     * 
     * @return the content length of the resource that this connection's URL references, or -1 if the content length is
     *         not known.
     */
    public long getLength();

    /**
     * Returns the type of content that the resource connected to is providing. E.g. if the connection is via HTTP, then
     * the value of the content-type header field is returned.
     * 
     * @return the content type of the resource that the URL references, or null if not known.
     */
    public String getType();
}
