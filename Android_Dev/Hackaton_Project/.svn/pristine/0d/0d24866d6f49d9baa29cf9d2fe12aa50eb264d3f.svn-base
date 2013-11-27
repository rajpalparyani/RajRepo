/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * TnConnection.java
 *
 */
package com.telenav.network;

import java.io.IOException;

/**
 * This is the most basic type of generic connection. Only the close method is defined. The open method defined here
 * because opening is always done by the {@link TnNetworkManager#openConnection(String)} methods.
 * 
 * @author fqming (fqming@telenav.cn)
 *@date Jul 10, 2010
 */
public interface TnConnection
{
    /**
     * Close the connection. <br />
     * <br />
     * When a connection has been closed, access to any of its methods except this close() will cause an an IOException
     * to be thrown. Closing an already closed connection has no effect. Streams derived from the connection may be open
     * when method is called. Any open streams will cause the connection to be held open until they themselves are
     * closed. In this latter case access to the open streams is permitted, but access to the connection is not. <br />
     * <br />
     * <b>Note:</b> Note that the stream must be
     * flushed or closed before the program exits, otherwise all pending data will be lost.
     */
    public void close() throws IOException;
}
