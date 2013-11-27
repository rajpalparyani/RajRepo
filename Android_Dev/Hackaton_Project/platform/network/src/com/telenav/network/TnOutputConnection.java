/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * TnOutputConnection.java
 *
 */
package com.telenav.network;

import java.io.IOException;
import java.io.OutputStream;

/**
 * This interface defines the capabilities that an output stream connection must have.
 * 
 * @author fqming (fqming@telenav.cn)
 *@date Jul 10, 2010
 */
public interface TnOutputConnection extends TnConnection
{
    /**
     * Open and return an output stream for a connection.
     * 
     * @return An output stream
     * @throws IOException If an I/O error occurs
     */
    public OutputStream openOutputStream() throws IOException;
}
