/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * StreamHandler.java
 *
 */
package com.telenav.comm;

/**
 * Common stream handler.
 * 
 *@author jyxu 
 *@date Jul 5, 2011
 */

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.telenav.io.TnGZIPInputStream;
import com.telenav.io.TnIoManager;



@RunWith(PowerMockRunner.class)
@PrepareForTest({StreamHandler.class,TnIoManager.class})
public class StreamHandlerTest extends TestCase
{
    public void testReadBytes()  throws Exception
    {
        String expText = "It is a test from StreamHandlerTest just to test StreamHandler readBytes().";
        byte[] data = expText.getBytes();
        int expLen = data.length;
        int offset = 0;
        byte[] actualData = new byte[expLen];
        InputStream is = new ByteArrayInputStream(data);
        int actLen = StreamHandler.readBytes(is, actualData, offset, expLen);
        assertEquals(expLen, actLen);
        assertEquals(expText, new String(actualData));
    }

    public void testUncompress()  throws Exception
    {
        String expText = "It is a test from StreamHandlerTest just to test StreamHandler readBytes().";
        byte[] data = expText.getBytes();
        int expLen = data.length;
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        GZIPOutputStream out = new GZIPOutputStream(os);
        out.write(data);
        out.flush();
        out.close();

        byte[] readData = os.toByteArray();
        
        ByteArrayInputStream is = new ByteArrayInputStream(readData);
        TnGZIPInputStream in = new J2seGZIPInputStream(is);
        
        TnIoManager ioManager = PowerMock.createMock(TnIoManager.class);
        TnIoManager.init(ioManager);
        EasyMock.expect(ioManager.createGZIPInputStream(EasyMock.isA(InputStream.class))).andReturn(in);
        PowerMock.replayAll();

        byte[] actualData = StreamHandler.uncompress(readData, expLen, ioManager);
        assertEquals(expText, new String(actualData));
        PowerMock.verifyAll();
    }
    
    static class J2seGZIPInputStream extends GZIPInputStream implements TnGZIPInputStream
    {
        public J2seGZIPInputStream(InputStream is) throws IOException
        {
            super(is);
        }
        
        public J2seGZIPInputStream(InputStream is, int size) throws IOException
        {
            super(is, size);
        }

    }
}
