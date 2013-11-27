/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * RimFileConnection.java
 *
 */
package com.telenav.persistent.rim;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Vector;

import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;

import com.telenav.persistent.TnFileConnection;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2010-12-27
 */
class RimFileConnection implements TnFileConnection
{
    protected FileConnection fileConnection;
    protected String rootDirectory;
    protected String directory;
    
    public RimFileConnection(FileConnection fileConnection, String rootDirectory, String directory)
    {
        this.fileConnection = fileConnection;
        this.rootDirectory = rootDirectory;
        this.directory = directory;
    }
    
    public long availableSize()
    {
        return this.fileConnection.availableSize();
    }

    public boolean canRead()
    {
        return this.fileConnection.canRead();
    }

    public boolean canWrite()
    {
        return this.fileConnection.canWrite();
    }

    public void close() throws IOException
    {
        this.fileConnection.close();
    }

    public void create() throws IOException
    {
        this.fileConnection.create();
    }

    public void delete() throws IOException
    {
        this.fileConnection.delete();
    }

    public boolean exists()
    {
        return this.fileConnection.exists();
    }

    public long fileSize() throws IOException
    {
        return this.fileConnection.fileSize();
    }

    public String getName()
    {
        return this.fileConnection.getName();
    }

    public String getPath()
    {
        return this.fileConnection.getPath();
    }

    public String getURL()
    {
        return this.fileConnection.getURL();
    }

    public boolean isDirectory()
    {
        return this.fileConnection.isDirectory();
    }

    public boolean isHidden()
    {
        return this.fileConnection.isHidden();
    }

    public long lastModified()
    {
        return this.fileConnection.lastModified();
    }

    public String[] list() throws IOException
    {
        Enumeration listEnum = this.fileConnection.list();
        Vector names = new Vector();
        while(listEnum.hasMoreElements())
        {
            names.addElement(listEnum.nextElement());
        }
        
        String[] files = new String[names.size()];
        names.copyInto(files);
        
        return files;
    }

    public boolean mkdir() throws IOException
    {
        this.fileConnection.mkdir();
        
        return true;
    }
    
    public boolean mkdirs() throws IOException
    {
        mkDir(this.rootDirectory, this.directory);
        
        return true;
    }
    
    private void mkDir(String rootUrl, String directory) throws IOException
    {
        FileConnection directoryConnection = (FileConnection)Connector.open(rootUrl);
        
        if(directoryConnection == null)
        {
            throw new IOException("root storage is null. the path is: " + rootUrl);
        }
        
        if(!directoryConnection.exists())
        {
            directoryConnection.mkdir();
        }
        
        if(directory == null || directory.length() == 0)
            return;
        
        int firstDirIndex = directory.indexOf('/');
        if(firstDirIndex != -1 && firstDirIndex < directory.length() - 1)
        {
            String firstDir = directory.substring(0, firstDirIndex);
            directory = directory.substring(firstDirIndex + 1);
            mkDir(rootUrl + "/" + firstDir, directory);
        }
        else
        {
            directoryConnection.close();
            directoryConnection = (FileConnection)Connector.open(rootUrl + "/" + directory);
            if(!directoryConnection.exists())
            {
                directoryConnection.mkdir();
            }
        }
    }
    
    public InputStream openInputStream() throws IOException
    {
        return this.fileConnection.openInputStream();
    }

    public OutputStream openOutputStream() throws IOException
    {
        return this.fileConnection.openOutputStream();
    }

    public OutputStream openOutputStream(boolean append) throws IOException
    {
        return this.fileConnection.openOutputStream(this.fileConnection.fileSize());
    }

    public void rename(String newName) throws IOException
    {
        this.fileConnection.rename(newName);
    }

    public void setReadable() throws IOException
    {
        this.fileConnection.setReadable(true);
    }

    public long totalSize()
    {
        return this.fileConnection.totalSize();
    }

}
