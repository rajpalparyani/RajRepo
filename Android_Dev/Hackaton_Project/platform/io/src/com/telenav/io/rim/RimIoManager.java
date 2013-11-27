/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * RimIoManager.java
 *
 */
package com.telenav.io.rim;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.telenav.io.TnBase64InputStream;
import com.telenav.io.TnBase64OutputStream;
import com.telenav.io.TnGZIPInputStream;
import com.telenav.io.TnGZIPOutputStream;
import com.telenav.io.TnIoManager;
import com.telenav.io.TnProperties;

/**
 * Provides access to the device's IO operation at rim platform.
 * 
 *@author fqming (fqming@telenav.cn)
 *@date Aug 13, 2010
 */
public class RimIoManager extends TnIoManager
{
    /**
     * Construct the io manager at rim platform.
     * <br />
     * <br />
     * Please make sure that grant below class's permission.
     * <br />
     * ApplicationPermissions#PERMISSION_FILE_API;
     * <br />
     * 
     */
    public RimIoManager()
    {
        
    }
    
    public TnBase64InputStream createBase64InputStream(InputStream is) throws IOException
    {
        return new RimBase64InputStream(is);
    }

    public TnBase64OutputStream createBase64OutputStream(OutputStream os) throws IOException
    {
        return new RimBase64OutputStream(os);
    }

    public TnGZIPInputStream createGZIPInputStream(InputStream is) throws IOException
    {
        return new RimGZIPInputStream(is);
    }

    public TnGZIPInputStream createGZIPInputStream(InputStream is, int size) throws IOException
    {
        return new RimGZIPInputStream(is, size);
    }

    public TnGZIPOutputStream createGZIPOutputStream(OutputStream os) throws IOException
    {
        return new RimGZIPOutputStream(os);
    }

    public TnGZIPOutputStream createGZIPOutputStream(OutputStream os, int size) throws IOException
    {
        return new RimGZIPOutputStream(os, size);
    }

    public TnProperties createProperties()
    {
        return new RimProperties();
    }

    public InputStream openFileFromAppBundle(String fileName) throws IOException
    {
        return this.getClass().getResourceAsStream("/" + fileName);
    }

	public String[] listFileFromAppBundle(String path) throws IOException
	{
		return null;
	}
}
