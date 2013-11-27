/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * StreamHandler.java
 *
 */
package com.telenav.comm;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.telenav.io.TnGZIPInputStream;
import com.telenav.io.TnIoManager;
import com.telenav.logger.Logger;

/**
 * Common stream handler.
 * 
 *@author fqming (fqming@telenav.cn)
 *@date Jul 12, 2010
 */
abstract class StreamHandler
{
    public static final int RESPONSE_STREAMING = 983276845;

    public static final int RESPONSE_STREAMING_COMPRESSED = 983276846;

    public static final int RESPONSE_COMPRESSED = 983276847;

    public static final int MAX_CHUNK = 4096 * 8;

    public static int readBytes(InputStream is, byte[] buff, int offset, int len) throws Exception
    {
        int bytesRead = 0;

        while (bytesRead < len)
        {
            int nextChunk = len - bytesRead;
            if (nextChunk > MAX_CHUNK)
                nextChunk = MAX_CHUNK;
            int count = is.read(buff, offset, nextChunk);

            if (count < 0)
                break;
            bytesRead += count;
            offset += count;
        }

        return bytesRead;
    }
    
    public static byte[] uncompress(byte[] compressData, int uncompressedLen, TnIoManager ioManager) throws Exception
    {
        byte[] uncompressData = new byte[uncompressedLen];
        ByteArrayInputStream bais = null;
        TnGZIPInputStream gis = null;

        bais = new ByteArrayInputStream(compressData);
        gis = ioManager.createGZIPInputStream(bais);

        int offset = 0;
        while (offset < uncompressedLen)
        {
            int l = gis.read(uncompressData, offset, uncompressedLen - offset);
            if (l < 0)
                break;
            offset += l;
        }

        if (gis != null)
        {
            gis.close();
            gis = null;
        }

        if (bais != null)
        {
            bais.close();
            bais = null;
        }

        return uncompressData;
    }
}
