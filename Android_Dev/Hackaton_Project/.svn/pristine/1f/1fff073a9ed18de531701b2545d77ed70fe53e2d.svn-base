/**
 *
 * Copyright 2013 TeleNav, Inc. All rights reserved.
 * ZipUtility.java
 *
 */
package com.telenav.app.android.scout_us;

/**
 *@author bduan
 *@date 2013-3-3
 */
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import com.telenav.logger.Logger;

public class ZipUtility
{

    public static final void zipDirectory(File directory, File zip) throws IOException
    {
        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zip));
        zip(directory, directory, zos);
        zos.close();
    }

    private static final void zip(File directory, File base, ZipOutputStream zos)
            throws IOException
    {
        File[] files = directory.listFiles();
        byte[] buffer = new byte[8192];
        int read = 0;
        for (int i = 0, n = files.length; i < n; i++)
        {
            if (files[i].isDirectory())
            {
                zip(files[i], base, zos);
            }
            else
            {
                try
                {
                    FileInputStream in = new FileInputStream(files[i]);
                    ZipEntry entry = new ZipEntry(files[i].getPath().substring(
                        base.getPath().length() + 1));
                    zos.putNextEntry(entry);
                    while (-1 != (read = in.read(buffer)))
                    {
                        zos.write(buffer, 0, read);
                    }         
                    in.close();
                }
                catch(IOException ex)
                {
                    Logger.log("ZipUtility", ex);                    
                }
            }
        }
    }

    public static final void unzip(File zip, File extractTo) throws IOException
    {
        ZipFile archive = new ZipFile(zip);
        Enumeration e = archive.entries();
        while (e.hasMoreElements())
        {
            ZipEntry entry = (ZipEntry) e.nextElement();
            File file = new File(extractTo, entry.getName());
            if (entry.isDirectory() && !file.exists())
            {
                boolean isCreated = file.mkdirs();
                if(!isCreated)
                {
                    throw new IOException("created failed.");
                }
            }
            else
            {
                if (!file.getParentFile().exists())
                {
                    boolean isCreated = file.getParentFile().mkdirs();
                    if(!isCreated)
                    {
                        throw new IOException("created failed.");                        
                    }
                }

                InputStream in = archive.getInputStream(entry);
                BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(
                        file));

                byte[] buffer = new byte[8192];
                int read;

                while (-1 != (read = in.read(buffer)))
                {
                    out.write(buffer, 0, read);
                }

                in.close();
                out.close();
            }
        }
    }
}