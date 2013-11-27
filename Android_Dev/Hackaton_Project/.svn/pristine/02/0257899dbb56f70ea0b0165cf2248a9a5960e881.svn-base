/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * AndroidImageManager.java
 *
 */
package com.telenav.searchwidget.res.android;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Environment;


/**
 * @author xinrongl (xinrongl@telenav.com)
 * @date Aug 10, 2011
 */

public class AndroidImageManager
{
    private static final String TELENAV_DIR         = "TeleNav";
    private static final String SEARCHWIDGET_DIR    = "searchwidget";
    private static final String EXT_NAME            = ".tnbmp";
    
    private static AndroidImageManager instance;
    
    private String rootPath;
    
    private boolean isExternalStorageWriteable;
    
    public static synchronized AndroidImageManager getInstance()
    {
        if (instance == null)
        {
            instance = new AndroidImageManager();
        }
        
        return instance;
    }
    
    public void setRootPath(String rootPath)
    {
        this.rootPath = rootPath;
    }
    
    public BitmapUri saveBitmap(int widgetId, Bitmap bm)
    {
        checkExternalStorageAvailable();
        
        BitmapUri bmUri = new BitmapUri();
        bmUri.image = bm;
        
        if (isExternalStorageWriteable)
        {
            createDir();
            FileOutputStream fos = null;
            try
            {
                File f = new File(getPath() + "/" + widgetId + EXT_NAME);
                if (f.exists())
                {
                	if (!f.delete()) return bmUri;
                }
                if (f.createNewFile())
                {
                    fos = new FileOutputStream(f);
                    bm.compress(CompressFormat.PNG, 100, fos);
                    
                    bmUri.uri = Uri.fromFile(f);
                }
            }
            catch (FileNotFoundException e)
            {            	
            }
            catch (IOException e)
            {
            }
            finally
            {
            	if (fos != null)
            	{
            		try
            		{
            			fos.close();
            		}
            		catch (Exception e1)
            		{        			
            		}
            	}
            }
        }
        
        return bmUri;

    }
    
    public boolean removeBitmap(int widgetId)
    {
        File f = new File(getPath() + "/" + widgetId + EXT_NAME);
        return f.delete();
    }
    
    private void checkExternalStorageAvailable()
    {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) 
        {
            isExternalStorageWriteable = true;
        } 
        else 
        {
            isExternalStorageWriteable = false;
        }    
    }
    
    private boolean createDir()
    {
        String path = this.rootPath + "/" + TELENAV_DIR;
        File dir = new File(path);
        if (!dir.exists())
        {
        	if (!dir.mkdir()) return false;
        }
        
        path += "/" + SEARCHWIDGET_DIR;        
        dir = new File(path);
        if (!dir.exists())
        {
        	if (!dir.mkdir()) return false;
        }
        return true;
    }
    
    private String getPath()
    {
        return this.rootPath + "/" + TELENAV_DIR + "/" + SEARCHWIDGET_DIR;
    }
}
