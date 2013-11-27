/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * ICommStreamHandler.java
 *
 */
package com.telenav.comm;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.telenav.comm.ICommCallback.CommResponse;

/**
 * Network stream data handler, including send data and receive data.
 * 
 *@author fqming (fqming@telenav.cn)
 *@date Aug 17, 2010
 */
public interface ICommStreamHandler
{
    /**
     * send the data.
     * 
     * @param os the output stream.
     * @param response comm response object.
     * @param callback comm's callback.
     * 
     * @return true means is successful, otherwise false.
     * 
     * @throws IOException io exception when send fail.
     */
    public boolean send(OutputStream os, CommResponse response, ICommCallback callback) throws IOException;
    
    /**
     * receive the data.
     * 
     * @param is the input stream.
     * @param length -1 if can't get the length from the header.
     * @param response comm response object.
     * @param callback comm's callback.
     * 
     * @return true means is successful, otherwise false.
     * @throws IOException io exception when receive fail.
     */
    public boolean receive(InputStream is, long length, CommResponse response, ICommCallback callback) throws IOException;
}
