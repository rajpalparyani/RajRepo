/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * TnFileConnection.java
 *
 */
package com.telenav.persistent;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * This interface is intended to access files or directories that are located on removeable media and/or file systems on
 * a device.
 * 
 *@author fqming (fqming@telenav.cn)
 *@date 2010-12-23
 */
public interface TnFileConnection
{
    /**
     * Checks if the URL passed to the Connector.open() is a directory.
     * 
     * @return True if the connection's target exists, is accessible, and is a directory, otherwise false.
     */
    public boolean isDirectory();

    /**
     * Deletes the file or directory specified in the Connector.open() URL. The file or directory is deleted immediately
     * on the actual file system upon invocation of this method. All open input and output streams are automatically
     * flushed and closed. Attempts to further use those streams result in an IOException. The FileConnection instance
     * object remains open and available for use.
     * 
     * @throws IOException If the target is a directory and it is not empty, the connection target does not exist or is
     *             unaccessible, or an unspecified error occurs preventing deletion of the target.
     */
    public void delete() throws IOException;

    /**
     * Creates a directory corresponding to the directory string provided in the Connector.open() method. The directory
     * is created immediately on the actual file system upon invocation of this method. Directories in the specified
     * path are not recursively created and must be explicitly created before sub-directories can be created.
     * 
     * @return <code>true</code> if and only if the directory was created, along with all necessary parent directories;
     *         <code>false</code> otherwise
     * 
     * @throws IOException If the target is a directory and it is not empty, the connection target does not exist or is
     *             unaccessible, or an unspecified error occurs preventing deletion of the target.
     */
    public boolean mkdir() throws IOException;

    /**
     * Creates the directory named by this abstract pathname, including any necessary but nonexistent parent
     * directories. Note that if this operation fails it may have succeeded in creating some of the necessary parent
     * directories.
     * 
     * @return <code>true</code> if and only if the directory was created, along with all necessary parent directories;
     *         <code>false</code> otherwise
     * 
     *         throws IOException If the target is a directory and it is not empty, the connection target does not exist
     *         or is unaccessible, or an unspecified error occurs preventing deletion of the target.
     */
    public boolean mkdirs() throws IOException;
    
    /**
     * Open and return an input stream for a connection. The connection's target must already exist and be accessible
     * for the input stream to be created.
     * 
     * @return An open input stream
     * @throws IOException if an I/O error occurs, if the method is invoked on a directory, if the connection's target
     *             does not yet exist, or the connection's target is not accessible.
     */
    public InputStream openInputStream() throws IOException;

    /**
     * Open and return an output stream for a connection. The output stream is positioned at the start of the file.
     * Writing data to the output stream overwrites the contents of the files (i.e. does not insert data). Writing data
     * to output streams beyond the current end of file automatically extends the file size. The connection's target
     * must already exist and be accessible for the output stream to be created.
     * 
     * @return An open output stream
     * @throws IOException if an I/O error occurs, if the method is invoked on a directory, if the connection's target
     *             does not yet exist, or the connection's target is not accessible.
     */
    public OutputStream openOutputStream() throws IOException;
    
    /**
     * Constructs a new FileOutputStream that writes to file, creating it if necessary. 
     * If append is true and the file already exists, it will be appended to. Otherwise a new file will be created.
     *
     * @return An open output stream
     * 
     * @param append  true to append to an existing file.
     * @throws IOException if an I/O error occurs, if the method is invoked on a directory, if the connection's target
     *             does not yet exist, or the connection's target is not accessible.
     */
    public OutputStream openOutputStream(boolean append) throws IOException;

    /**
     * Determines the total size of the file system the connection's target resides on.
     * 
     * @return The total size of the file system in bytes, or -1 if the file system is not accessible.
     */
    public long totalSize();

    /**
     * Determines the free memory that is available on the file system the file or directory resides on. This may only
     * be an estimate and may vary based on platform-specific file system blocking and metadata information.
     * 
     * @return The available size in bytes on a file system, or -1 if the file system is not accessible.
     */
    public long availableSize();

    /**
     * Determines the size of a file on the file system. The size of a file always represents the number of bytes
     * contained in the file; there is no pre-allocated but empty space in a file.
     * 
     * fileSize() always returns size of the file on the file system, and not in any pending output stream. flush()
     * should be used before calling fileSize() to ensure the contents of the output streams opened to the file get
     * written to the file system.
     * 
     * @return The size in bytes of the selected file, or -1 if the file does not exist or is not accessible.
     * @throws IOException if the method is invoked on a directory.
     */
    public long fileSize() throws IOException;

    /**
     * Checks if the file or directory is readable. This method checks the attributes associated with a file or
     * directory by the underlying file system. Some file systems may not support associating attributes with a file, in
     * which case this method returns true.
     * 
     * @return true if the connection's target exists, is accessible, and is readable, otherwise false.
     */
    public boolean canRead();

    /**
     * Checks if the file or directory is writable. This method checks the attributes associated with a file or
     * directory by the underlying file system. Some file systems may not support associating attributes with a file, in
     * which case this method returns true.
     * 
     * @return true if the connection's target exists, is accessible, and is writable, otherwise false.
     */
    public boolean canWrite();

    /**
     * Checks if the file is hidden. The exact definition of hidden is system-dependent. For example, on UNIX systems a
     * file is considered to be hidden if its name begins with a period character ('.'). On Win32 and FAT file systems,
     * a file is considered to be hidden if it has been marked as such in the file's attributes. If hidden files are not
     * supported on the referenced file system, this method always returns false.
     * 
     * @return true if the file exists, is accessible, and is hidden, otherwise false.
     */
    public boolean isHidden();

    /**
     * Sets the file or directory readable attribute to the indicated value. The readable attribute for the file on the
     * actual file system is set immediately upon invocation of this method. If the file system doesn't support a
     * settable read attribute, this method is ignored and canRead() always returns true.
     * 
     * @throws IOException The new state of the readable flag of the selected file.
     */
    public void setReadable() throws IOException;

    /**
     * Gets a list of all files and directories contained in a directory.
     * 
     * @return An array of strings, denoting the files and directories in the directory.
     * @throws IOException if invoked on a file, the directory does not exist, the directory is not accessible, or an
     *             I/O error occurs.
     */
    public String[] list() throws IOException;

    /**
     * Creates a file corresponding to the file string.
     * 
     * @throws IOException if invoked on an existing file or on any directory (mkdir() is used to create directories),
     *             the connection's target has a trailing "/" to denote a directory, the target file system is not
     *             accessible, or an unspecified error occurs preventing creation of the file.
     */
    public void create() throws IOException;

    /**
     * Checks if the file or directory exists.
     * 
     * @return true if the connnection's target exists and is accessible, otherwise false.
     */
    public boolean exists();

    /**
     * Renames the selected file or directory to a new name in the same directory.
     * 
     * @param newName The new name of the file or directory.
     * @throws IOException if the connection's target does not exist, the connection's target is not accessible, a file
     *             or directory already exists by the newName, or newName is an invalid filename for the platform (e.g.
     *             contains characters invalid in a filename on the platform).
     */
    public void rename(String newName) throws IOException;

    /**
     * Returns the name of a file or directory excluding the URL schema and all paths.
     * 
     * @return The name of a file or directory.
     */
    public String getName();

    /**
     * Returns the path excluding the file or directory name and the "file" URL schema and host from where the file or
     * directory is opened.
     * 
     * @return The path of a file or directory in the format specified above.
     */
    public String getPath();

    /**
     * Returns the full file URL including the scheme, host, and path.
     * 
     * @return The URL of a file or directory in the format specified.
     */
    public String getURL();

    /**
     * Returns the time that the file denoted by the URL specified in the Connector.open() method was last modified.
     * 
     * @return A long value representing the time the file was last modified.
     */
    public long lastModified();

    /**
     * Close the connection.
     * 
     * @throws IOException If an I/O error occurs.
     */
    public void close() throws IOException;
}
