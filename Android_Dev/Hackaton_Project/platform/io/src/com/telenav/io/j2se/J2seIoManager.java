/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * J2seIoManager.java
 *
 */
package com.telenav.io.j2se;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.telenav.io.TnBase64InputStream;
import com.telenav.io.TnBase64OutputStream;
import com.telenav.io.TnGZIPInputStream;
import com.telenav.io.TnGZIPOutputStream;
import com.telenav.io.TnIoManager;
import com.telenav.io.TnProperties;
import com.telenav.io.base64.Base64InputStream;
import com.telenav.io.base64.Base64OutputStream;

/**
 * Provides access to the device's IO operation at j2se platform.
 * 
 *@author fqming (fqming@telenav.cn)
 *@date Aug 25, 2010
 */
public class J2seIoManager extends TnIoManager
{
    protected String baseDir;
    
    /**
     * Construct the location manager at j2se platform.
     * <br />
     * <br />
     * 
     * @param baseDir The base directory of whole application.
     */
    public J2seIoManager(String baseDir)
    {
        this.baseDir = baseDir;
    }
    
    public TnBase64InputStream createBase64InputStream(InputStream is) throws IOException
    {
        return new Base64InputStream(is);
    }

    public TnBase64OutputStream createBase64OutputStream(OutputStream os) throws IOException
    {
        return new Base64OutputStream(os);
    }

    public TnGZIPInputStream createGZIPInputStream(InputStream is) throws IOException
    {
        return new J2seGZIPInputStream(is);
    }

    public TnGZIPInputStream createGZIPInputStream(InputStream is, int size) throws IOException
    {
        return new J2seGZIPInputStream(is, size);
    }

    public TnGZIPOutputStream createGZIPOutputStream(OutputStream os) throws IOException
    {
        return new J2seGZIPOutputStream(os);
    }

    public TnGZIPOutputStream createGZIPOutputStream(OutputStream os, int size) throws IOException
    {
        return new J2seGZIPOutputStream(os, size);
    }

    public TnProperties createProperties()
    {
        return new J2seProperties();
    }

    public InputStream openFileFromAppBundle(String fileName) throws IOException
    {
        return new FileInputStream(this.baseDir + "/" + fileName);
    }
	
	public String[] listFileFromAppBundle(String path) throws IOException
	{
		return null;
	}
}
