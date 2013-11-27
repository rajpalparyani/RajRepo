/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * ApacheStreamHanlder.java
 *
 */
package com.telenav.module.sync.apache;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.telenav.comm.ICommCallback;
import com.telenav.comm.ICommCallback.CommResponse;
import com.telenav.comm.ICommStreamHandler;
import com.telenav.io.TnIoManager;
import com.telenav.logger.Logger;

/**
 *@author wzhu (wzhu@telenav.cn)
 *@date 2011-1-16
 */
class ApacheCommStreamHandler implements ICommStreamHandler
{
    String path = "";
    ApacheCommStreamHandler(String fileName)
    {
        this.path = fileName;
    }
    public boolean receive(InputStream inputstream, long l, CommResponse commresponse, ICommCallback icommcallback) throws IOException
    {
        byte[] data = TnIoManager.readBytes(inputstream);

        commresponse.responseData = data;
        Logger.log(Logger.INFO, this.getClass().getName(), "----i18n--- received: " + path);
        
        I18nFile file = (I18nFile) I18nRequestManager.getInstance().getRootFile().searchChildByName(path);
        if (file != null)
        {
            file.setStatus(I18nFile.RECEIVED);
        }
        return true;
    }

    public boolean send(OutputStream outputstream, CommResponse commresponse, ICommCallback icommcallback) throws IOException
    {
        return true;
    }
}
