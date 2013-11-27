/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * WidgetManager.java
 *
 */
package com.telenav.searchwidget.framework.android;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;

import com.telenav.util.PrimitiveTypeCache;

/**
 * @author xinrongl (xinrongl@telenav.com)
 * @date Jul 20, 2011
 */

public class WidgetManager
{
    private static final String SEARCHWIDGET_DIR    = "searchwidget";
    private static final String FILE_NAME           = "widgetsList.dat";
    private static final String COMMENTS            = "==== widget layout mapping ====";
    
    private static WidgetManager instance;
    private static int _initCount = 0;
    
    private String rootPath;

    private Hashtable widgets = new Hashtable();
    private Properties properties;
    
    private WidgetManager()
    {
    }
    
    public static WidgetManager getInstance()
    {
        if (_initCount == 0)
        {
            _initCount = 1;
            instance = new WidgetManager();
        }
        
        return instance;
    }
    
    public boolean setRootPath(String rootPath)
    {
        this.rootPath = rootPath + "/" + SEARCHWIDGET_DIR;
        File dir = new File(this.rootPath);
        return dir.mkdir();
    }
    
    public void registerWidget(int widgetId, WidgetViewStub stub)
    {
        widgets.put(PrimitiveTypeCache.valueOf(widgetId), stub);
        
        if (properties == null)
        {
            properties = loadProperties();
        }
        
        properties.setProperty("" + widgetId, "" + stub.layoutId);
        
        storeProperties(properties);
    }
    
    public void unregisterWidget(int widgetId)
    {
        widgets.remove(PrimitiveTypeCache.valueOf(widgetId));
        
        if (properties == null)
        {
            properties = loadProperties();
        }
        
        properties.remove("" + widgetId);
        
        storeProperties(properties);
    }
    
    public void unregisterAllWidgets()
    {
        widgets.clear();
        
        properties = null;
        removeProperties();
    }
    
    public int[] getAppWidgets()
    {
        if (properties == null)
        {
            properties = loadProperties();
        }
        Enumeration keys = properties.keys();
        ArrayList list = Collections.list(keys);
        
        int size = list != null ? list.size() : 0;
        if (size > 0)
        {
            int[] widgets = new int[size];
            for (int i = 0; i < size; i ++)
            {
                widgets[i] = Integer.parseInt((String)list.get(i));
            }
            return widgets;
        }
        
        return null;
    }
    
    public int getLayoutId(int widgetId)
    {
        if (properties == null)
        {
            properties = loadProperties();
        }

        String str = properties.getProperty("" + widgetId);
        if (str != null)
        {   
            return Integer.parseInt(str);            
        }
        
        return -1;
    }
    
    public WidgetViewStub getViewStub(int widgetId)
    {
        WidgetViewStub stub = (WidgetViewStub)widgets.get(PrimitiveTypeCache.valueOf(widgetId));

        return stub;
    }
    
    private Properties loadProperties()
    {
        Properties p = new Properties();
        FileInputStream fis = null;
        try
        {
            File f = new File(this.rootPath + "/" + FILE_NAME);
            boolean success = true;
            if (!f.exists())
            {
                success = f.createNewFile();
            }
            if (!success) return p;
            
            fis = new FileInputStream(f);
            p.load(fis);
        }
        catch (FileNotFoundException e)
        {        	
        }
        catch (SecurityException e)
        {        	
        }
        catch (IOException e)
        {        	
        }
        finally
        {
        	if (fis != null)
        	{
        		try
        		{
        			fis.close();
        		}
        		catch (Exception e1)
        		{        			
        		}
        	}
        }
        return p;
    }
    
    private void storeProperties(Properties p)
    {
        if (p == null) return;
        
        FileOutputStream fos = null;
        try
        {
            File f = new File(this.rootPath + "/" + FILE_NAME);
            
            fos = new FileOutputStream(f);                
            p.store(fos, COMMENTS);
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
    
    private boolean removeProperties()
    {
        File f = new File(this.rootPath + "/" + FILE_NAME);
        return f.delete();
    }
}
