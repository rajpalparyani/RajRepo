/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * TnIoManager.java
 *
 */
package com.telenav.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.telenav.logger.Logger;

/**
 * Provides access to the device's IO operation.
 * 
 *@author fqming (fqming@telenav.cn)
 *@date Jul 12, 2010
 */
public abstract class TnIoManager
{
    private static final int MAX_CHUNK = 4096 * 8;
    
    private static TnIoManager ioManager;
    private static int initCount;
    
    /**
     * Retrieve the instance of io manager.
     * 
     * @return {@link TnIoManager}
     */
    public static TnIoManager getInstance()
    {
        return ioManager;
    }
    
    /**
     * Before invoke the methods of this manager, need init the native manager of the platform first.
     * 
     * @param ioMngr This manager is native manager of platforms. Such as {@link AndroidIoManager} etc.
     */
    public synchronized static void init(TnIoManager ioMngr)
    {
        if(initCount >= 1)
            return;
        
        ioManager = ioMngr;
        initCount++;
    }
    
    /**
     * read bytes from the stream with max buffer section. every max section is 4096 * 8.
     * 
     * @param is the input stream contains the data
     * @return the total bytes read from the input stream
     * @throws IOException if an I/O error occurs.
     */
    public static byte[] readBytes(InputStream is) throws IOException
    {
        ByteArrayOutputStream baos = null;

        try
        {
            baos = new ByteArrayOutputStream();

            byte[] buffer = new byte[MAX_CHUNK];
            
            int l = 0;
            while ((l = is.read(buffer)) > 0)
            {
                baos.write(buffer, 0, l);
            }
            
            return baos.toByteArray();
        }
        catch (IOException e)
        {
            throw e;
        }
        finally
        {
            try
            {
                if (baos != null)
                {
                    baos.close();
                }
            }
            catch (IOException e)
            {
                throw e;
            }
            finally
            {
                baos = null;
            }
        }
    }
    
    /**
     * Open a file from the binary application package.
     * <br />
     * At android platform,this provides access to files that have been bundled with an
     * application as assets -- that is, files placed in to the "assets" directory.
     * <br />
     * At rim platform, this provides access to the class stream in '/res' directory.
     * 
     * @param fileName The name of the asset to open. This name can be hierarchical.
     * @return the input stream.
     * @throws IOException
     */
    public abstract String[] listFileFromAppBundle(String path) throws IOException;
    
    /**
     * Open a file from the binary application package.
     * <br />
     * At android platform,this provides access to files that have been bundled with an
     * application as assets -- that is, files placed in to the "assets" directory.
     * <br />
     * At rim platform, this provides access to the class stream in '/res' directory.
     * 
     * @param fileName The name of the asset to open. This name can be hierarchical.
     * @return the input stream.
     * @throws IOException
     */
    public abstract InputStream openFileFromAppBundle(String fileName) throws IOException;

    /**
     * @see #openFile(String)
     * 
     * @param fileName The name of the asset to open. This name can be hierarchical.
     * @return the byte's data.
     */
    public byte[] openFileBytesFromAppBundle(String fileName)
    {
        InputStream is = null;
        try
        {
            is = openFileFromAppBundle(fileName);
            
            byte[] data = readBytes(is);
            
            return data;
        }
        catch (Throwable e)
        {
            Logger.log(this.getClass().getName(), e);
        }
        finally
        {
            try
            {
                if (is != null)
                {
                    is.close();
                }
            }
            catch (Throwable e)
            {
                Logger.log(this.getClass().getName(), e);
            }
            finally
            {
                is = null;
            }
        }

        return null;
    }
    
    /**
     * create a properties object to read the inputstream into a hashtable.
     * 
     * @return {@link TnProperties}
     */
    public abstract TnProperties createProperties();
    
    /**
     * Construct a TnGZIPInputStream to read from GZIP data from the underlying stream.
     * 
     * @param is the InputStream to read data from.
     * @return TnGZIPInputStream
     * @throws IOException an I/O error occurs.
     */
    public abstract TnGZIPInputStream createGZIPInputStream(InputStream is) throws IOException;

    /**
     * Construct a TnGZIPInputStream to read from GZIP data from the underlying stream.
     * 
     * @param is the InputStream to read data from.
     * @param size the internal read buffer size.
     * @return TnGZIPInputStream
     * @throws IOException an I/O error occurs.
     */
    public abstract TnGZIPInputStream createGZIPInputStream(InputStream is, int size) throws IOException;

    /**
     * Construct a new TnGZIPOutputStream to write data in GZIP format to the underlying stream.
     * 
     * @param os the OutputStream to write data to.
     * @return TnGZIPOutputStream
     * @throws IOException an I/O error occurs.
     */
    public abstract TnGZIPOutputStream createGZIPOutputStream(OutputStream os) throws IOException;

    /**
     * Construct a new TnGZIPOutputStream to write data in GZIP format to the underlying stream.
     * 
     * @param os the OutputStream to write data to.
     * @param size the internal buffer size.
     * @return TnGZIPOutputStream
     * @throws IOException an I/O error occurs.
     */
    public abstract TnGZIPOutputStream createGZIPOutputStream(OutputStream os, int size) throws IOException;

    /**
     * Create an InputStream that performs Base64 decoding on the data read from the wrapped stream.
     * 
     * @param is the InputStream to read the source data from
     * @return TnBase64InputStream
     * @throws IOException an I/O error occurs.
     */
    public abstract TnBase64InputStream createBase64InputStream(InputStream is) throws IOException;

    /**
     * Create a OutputStream that performs Base64 encoding on the data written to the stream, writing the encoded data to another OutputStream.
     * 
     * @param os the OutputStream to write the encoded data to
     * @return TnBase64OutputStream
     * @throws IOException an I/O error occurs.
     */
    public abstract TnBase64OutputStream createBase64OutputStream(OutputStream os) throws IOException;
}
