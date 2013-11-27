/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * RimProperties.java
 *
 */
package com.telenav.io.rim;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Hashtable;

import net.rim.device.api.io.LineReader;

import com.telenav.io.TnProperties;
import com.telenav.logger.Logger;

/**
 *@author fqming (fqming@telenav.cn)
 *@date Aug 13, 2010
 */
class RimProperties extends Hashtable implements TnProperties
{

    public String getProperty(String name)
    {
        return (String) this.get(name);
    }

    public void load(InputStream is) throws IOException
    {
        LineReader lineReader = new LineReader(is);

        String line = null;
        try
        {
            line = new String(lineReader.readLine());
        }
        catch (EOFException eofe)
        {
            Logger.log(this.getClass().getName(), eofe);
        }

        for (; line != null; line = new String(lineReader.readLine()))
        {

            int equalIndex = line.indexOf("=");
            if (equalIndex != -1)
            {
                this.put(line.substring(0, equalIndex), line.substring(equalIndex + 1));
            }
            if (lineReader.lengthUnreadData() <= 0)
            {
                break;
            }
        }
    }

    public Enumeration propertyNames()
    {
        return this.keys();
    }
}
