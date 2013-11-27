/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * J2seFileConnection.java
 *
 */
package com.telenav.persistent.j2se;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;

import com.telenav.persistent.TnFileConnection;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2010-12-27
 */
class J2seFileConnection implements TnFileConnection
{
    private File file;
    
    public J2seFileConnection(File file)
    {
        this.file = file;
    }

    public long availableSize()
    {
        return this.file.getFreeSpace();
    }

    public boolean canRead()
    {
        return this.file.canRead();
    }

    public boolean canWrite()
    {
        return this.file.canWrite();
    }

    public void create() throws IOException
    {
        boolean isCreated = this.file.createNewFile();
        if(!isCreated)
        {
            throw new IOException("create failed.");
        }
    }

    public void delete() throws IOException
    {
        boolean isDeleted = this.file.delete();
        if(!isDeleted)
        {
            throw new IOException("create failed.");
        }
    }

    public boolean exists()
    {
        return this.file.exists();
    }

    public long fileSize() throws IOException
    {
        return this.file.length();
    }

    public String getName()
    {
        return this.file.getName();
    }

    public String getPath()
    {
        return this.file.getPath();
    }

    public String getURL()
    {
        try
        {
            return this.file.toURI().toURL().toString();
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        
        return "";
    }

    public boolean isDirectory()
    {
        return this.file.isDirectory();
    }

    public boolean isHidden()
    {
        return this.file.isHidden();
    }

    public long lastModified()
    {
        return this.file.lastModified();
    }

    public String[] list() throws IOException
    {
        return this.file.list();
    }
    
    public boolean mkdir() throws IOException
    {
        return this.file.mkdir();
    }
    
    public boolean mkdirs() throws IOException
    {
        return this.file.mkdirs();
    }

    public InputStream openInputStream() throws IOException
    {
        return new FileInputStream(this.file);
    }

    public OutputStream openOutputStream() throws IOException
    {
        return new FileOutputStream(this.file);
    }

    public OutputStream openOutputStream(boolean append) throws IOException
    {
        return new FileOutputStream(this.file, append);
    }

    public void rename(String newName) throws IOException
    {
        boolean isReNamed = this.file.renameTo(new File(this.file.getParentFile(), newName));
        if(!isReNamed)
        {
            throw new IOException("create failed.");
        }
    }

    public void setReadable() throws IOException
    {
        boolean isReadOnly = this.file.setReadOnly();
        if(!isReadOnly)
        {
            throw new IOException("create failed.");
        }
    }

    public long totalSize()
    {
        return this.file.getTotalSpace();
    }
    
    public void close() throws IOException
    {
    }
}
