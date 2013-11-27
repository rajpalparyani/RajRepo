/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * AndroidIoManager.java
 *
 */
package com.telenav.io.android;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;

import com.telenav.io.TnBase64InputStream;
import com.telenav.io.TnBase64OutputStream;
import com.telenav.io.TnGZIPInputStream;
import com.telenav.io.TnGZIPOutputStream;
import com.telenav.io.TnIoManager;
import com.telenav.io.TnProperties;
import com.telenav.io.base64.Base64InputStream;
import com.telenav.io.base64.Base64OutputStream;

/**
 * Provides access to the device's IO operation at android platform.
 * 
 *@author fqming (fqming@telenav.cn)
 *@date Jul 12, 2010
 */
public class AndroidIoManager extends TnIoManager
{
    protected Context context;
    
    /**
     * construct a AndroidIoManager.
     */
    public AndroidIoManager(Context context)
    {
        this.context = context;
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
        return new AndroidGZIPInputStream(is);
    }

    public TnGZIPInputStream createGZIPInputStream(InputStream is, int size) throws IOException
    {
        return new AndroidGZIPInputStream(is, size);
    }

    public TnGZIPOutputStream createGZIPOutputStream(OutputStream os) throws IOException
    {
        return new AndroidGZIPOutputStream(os);
    }

    public TnGZIPOutputStream createGZIPOutputStream(OutputStream os, int size) throws IOException
    {
        return new AndroidGZIPOutputStream(os, size);
    }

    public TnProperties createProperties()
    {
        return new AndroidProperties();
    }

    public String[] listFileFromAppBundle(String path) throws IOException
    {
        return this.context.getAssets().list(path);
    }
    
    public InputStream openFileFromAppBundle(String fileName) throws IOException
    {
        return this.context.getAssets().open(fileName);
    }
}
