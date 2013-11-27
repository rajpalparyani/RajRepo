/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * FileManager.java
 *
 */
package com.telenav.module.entry.secretkey;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import android.content.Context;

import com.telenav.app.android.AndroidPersistentContext;
import com.telenav.data.dao.misc.FileStoreManager;

/**
 * @author chrbu
 * @date 2012-1-9
 */
public class DumpFileManager
{
    private static final String SLASH = "/";

    private static final String SIZE = "  size:";

    private static final String BYTES = "Bytes";

    private static final String DATEFORMAT = "yyyyMMddHHmmss";

    private static DumpFileManager manager;

    private final static String APPPATHPREFIX = "/data/data/";

    private final static String SDCARD = "/sdcard/";

    private static final String DUMPFILE = "/dumpfile/";
    
    private static final String PRELOAD = "/preload/";

    public String getSdcardPath(boolean needFileName)
    {
        StringBuffer dumpFilePath = new StringBuffer();
        dumpFilePath.append(SDCARD);
        dumpFilePath.append(FileStoreManager.TELENAV_DIRECTORY_PATH);
        dumpFilePath.append(DUMPFILE);
        if (needFileName)
        {
            dumpFilePath.append(getFileName());
        }
        return dumpFilePath.toString();
    }

    public String getSdcardPreloadFilePath(boolean needFileName)
    {
        StringBuffer dumpFilePath = new StringBuffer();
        dumpFilePath.append(SDCARD);
        dumpFilePath.append(FileStoreManager.TELENAV_DIRECTORY_PATH);
        dumpFilePath.append(PRELOAD);
        if (needFileName)
        {
            dumpFilePath.append(getFileName());
        }
        return dumpFilePath.toString();
    }
    
    private String getFileName()
    {
        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat(DATEFORMAT);
        String time = df.format(date);
        return time;
    }

    public static synchronized DumpFileManager getInstance()
    {
        if (manager == null)
            manager = new DumpFileManager();
        return manager;
    }

    private DumpFileManager()
    {
        super();
    }

    public String getAppPath()
    {
        String appPath = "";
        Context context = AndroidPersistentContext.getInstance().getContext();
        String packageName = context.getPackageName();
        appPath = APPPATHPREFIX + packageName;
        return appPath;
    }

    public String[][] getDirectoryInfo(String path)
    {
        String[][] directoryInfo = null;
        File file = new File(path);
        if (file.isDirectory())
        {
            File[] files = file.listFiles();
            int size = files.length;
            directoryInfo = new String[2][size];
            for (int start = 0; start < size; start++)
            {
                File targetFile = files[start];
                directoryInfo[0][start] = getFileDescription(targetFile);
                directoryInfo[1][start] = targetFile.getPath();
            }
        }
        return directoryInfo;
    }

    public String getFileDescription(File targetFile)
    {
        StringBuffer desc = new StringBuffer();
        String name = targetFile.getName();
        String size = getFileSize(targetFile) + BYTES;
        desc.append(name);
        desc.append(SIZE + size);
        return desc.toString();

    }

    public String getFileContent(String path)
    {
        StringBuffer content = new StringBuffer();
        File myFile = new File(path);
        if (myFile.exists())
        {
            try
            {
                {
                    BufferedReader in = new BufferedReader(new FileReader(myFile));
                    String str;
                    while ((str = in.readLine()) != null)
                    {
                        content.append(str);
                    }
                    in.close();
                }
            }
            catch (IOException e)
            {
                e.getStackTrace();
            }
        }
        return content.toString();
    }

    public void copyDirectory(File source, File target)
    {
        boolean isCreateSuccess = false;
        File targetDire, sourceFile, destFile, sourceDirectory, destDirectory;
        File[] file = source.listFiles();
        targetDire = new File(target.getAbsolutePath());
        if (!targetDire.exists())
            isCreateSuccess = targetDire.mkdirs();
        else
            isCreateSuccess = true;
        if (isCreateSuccess)
        {
            for (int i = 0; i < file.length; i++)
            {
                if (file[i].isFile())
                {
                    sourceFile = new File(source.getAbsolutePath() + SLASH
                            + file[i].getName());
                    destFile = new File(target.getAbsolutePath() + SLASH
                            + file[i].getName());
                    this.copyFile(sourceFile, destFile);
                }
                if (file[i].isDirectory())
                {
                    sourceDirectory = new File(source.getAbsolutePath() + SLASH
                            + file[i].getName());
                    destDirectory = new File(target.getAbsolutePath() + SLASH
                            + file[i].getName());
                    isCreateSuccess = destDirectory.mkdir();
                    if (isCreateSuccess)
                    {
                        this.copyDirectory(sourceDirectory, destDirectory);
                    }
                }
            }
        }
    }

    public void copyDirectoryWithSpecificFileTable(File source, File target, Vector filterFileNames)
    {
        boolean isCreateSuccess = false;
        File targetDire, sourceFile, destFile;
        File[] file = source.listFiles();
        targetDire = new File(target.getAbsolutePath());
        if (!targetDire.exists())
            isCreateSuccess = targetDire.mkdirs();
        else
            isCreateSuccess = true;
        if (isCreateSuccess)
        {
            for (int i = 0; i < file.length; i++)
            {
                if (file[i].isFile() && filterFileNames.contains(file[i].getName()))
                {
                    sourceFile = new File(source.getAbsolutePath() + SLASH
                            + file[i].getName());
                    destFile = new File(target.getAbsolutePath() + SLASH
                            + file[i].getName());
                    this.copyFile(sourceFile, destFile);
                }
            }
        }
    }
    
    public void copyFile(File source, File target)
    {
        FileInputStream inFile = null;
        FileOutputStream outFile = null;
        try
        {
            inFile = new FileInputStream(source);
            outFile = new FileOutputStream(target);
            byte[] buffer = new byte[1024];
            int i = 0;
            while ((i = inFile.read(buffer)) != -1)
            {
                outFile.write(buffer, 0, i);
            }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if (outFile != null)
                {
                    outFile.close();
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            try
            {
                if (inFile != null)
                {
                    inFile.close();
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

        }
    }

    public long getFileSize(File file)
    {
        long foldersize = 0;
        if (file.isDirectory())
        {
            File[] filelist = file.listFiles();
            {
                for (int i = 0; i < filelist.length; i++)
                {
                    if (filelist[i].isDirectory())
                    {
                        foldersize += getFileSize(filelist[i]);
                    }
                    else
                    {
                        foldersize += filelist[i].length();
                    }
                }
            }
        }
        else
        {
            if (file.isFile())
                foldersize = file.length();
        }
        return foldersize;
    }

}
