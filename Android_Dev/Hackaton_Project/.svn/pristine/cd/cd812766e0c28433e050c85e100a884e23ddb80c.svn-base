/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * Util.java
 *
 */
package com.telenav;

import java.io.IOException;
import java.io.InputStream;

/**
 *@author qli
 *@date 2011-4-13
 */
public class Util
{

    public static String read(InputStream is) throws IOException
    {
        if( is != null )
        {
            StringBuffer buffer = new StringBuffer();
            int i = 0;
            while( (i=is.read()) != -1 )
            {
                buffer.append((char)i);
            }
            return buffer.toString();
        }
        return null;
    }
    
    public static byte[] read(InputStream is, int size) throws IOException
    {
        byte[] buffer = new byte[0];
        if ( is != null )
        {
            if ( size > 0 )
            {
                buffer = new byte[size];
                int actualRead = 0;
                int bytesRead = 0;
                while (bytesRead < size && actualRead != -1)
                {
                    actualRead = is.read(buffer, bytesRead, size - bytesRead);
                    bytesRead += actualRead;
                }
            }
        }
        return buffer;
    }
}
