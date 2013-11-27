/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * LocationDao.java
 *
 */
package com.telenav.searchwidget.gps.android;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.util.Hashtable;

import com.telenav.location.TnLocation;
import com.telenav.searchwidget.app.AppConfigHelper;

/**
 *@author XinRong Liu
 *@date 2011-8-22
 */
public class LocationDao
{
    private static final String DIRECTORY_PREFIX = "TN70_" + AppConfigHelper.brandName;
	private static final String ONE_BOX_STANDARD = "1";
    private static final String GPS_LOCATION_INDEX = "1";
    private static final String CELL_TOWER_INDEX = "3";
    private final static String ONEBOX_TYPE_INDEX = "400003";
    private final static String LOCALE_INDEX = "400004"; 
    private static String FILE_TN_LOCATION;
    private static String FILE_SEARCH_WIDGET_LOCATION;
    private static LocationDao inst = new LocationDao();
    
    private String rootPath;
	private String FILE_SERVERDRIVE;    
    private LocationDao()
    {
        FILE_TN_LOCATION = DIRECTORY_PREFIX + "_location_cache.bin";
        FILE_SEARCH_WIDGET_LOCATION = DIRECTORY_PREFIX + "_searchwidget_loc.bin";
        FILE_SERVERDRIVE = DIRECTORY_PREFIX + "_server_driven.bin";
    }
    
    public static LocationDao getInstance()
    {
        return inst;
    }
    
    public void setRootPath(String rootPath)
    {
        this.rootPath = rootPath;
    }
    
    public String getTnLocale()
    {
    	String tnLocale = null;
    	Hashtable crumbData = (Hashtable)loadObject(FILE_SERVERDRIVE);
        if (null != crumbData)
        {
        	byte[] data = (byte[]) crumbData.get(LOCALE_INDEX);
        	if (null != data)
        	{
        		tnLocale = new String(data);
        	}
        	crumbData.clear();
        }
        return tnLocale;
    }
    
    public boolean isOneBoxStandard()
    {
    	boolean isOneboxStandard = true;
    	Hashtable crumbData = (Hashtable)loadObject(FILE_SERVERDRIVE);
        if (null != crumbData)
        {
        	byte[] data = (byte[]) crumbData.get(ONEBOX_TYPE_INDEX);
        	if (null != data)
        	{
        		isOneboxStandard = ONE_BOX_STANDARD.equals(new String(data));
        	}
        	crumbData.clear();
        }
        return isOneboxStandard;
    }

	public TnLocation geTnLastCellLocation()
    {
        return  getTnLocation(CELL_TOWER_INDEX);
    }

    
    public TnLocation getTnLastGpsLocation()
    {
    	return  getTnLocation(GPS_LOCATION_INDEX);
    }    
    
    public TnLocation getSearchWidgetLastKnownLocation()
    {
        TnLocation loc = null;
        byte[] data = readData(FILE_SEARCH_WIDGET_LOCATION);
        if (data != null)
        {
            loc = AndroidLocationUtil.fromBytes(data);
        }
        
        return loc;        
    }
    
    public void setSearchWidgetLastKnownLocation(TnLocation loc)
    {
        byte[] data = AndroidLocationUtil.toBytes(loc);
        if (data != null)
        {            
            writeData(FILE_SEARCH_WIDGET_LOCATION, data);
        }
    }


    private synchronized byte[] readData(String name)
    {
        FileInputStream fis = null;
        byte[] data = null;
        try
        {
            File f = new File(this.rootPath + "/" + name);
            if(f.exists())
            {
                fis = new FileInputStream(f);
                int size = fis.available();
                data = new byte[size];
                fis.read(data);
            }
        }
        catch(Throwable e)
        {
        }
        finally
        {
            try
            {
                if (fis != null)
                {
                    fis.close();
                }
            }
            catch (Exception e)
            {
            }
        }
        
        return data;
    }
    
    private TnLocation getTnLocation(String index) {
    	TnLocation loc = null;
    	Hashtable crumbData = (Hashtable)loadObject(FILE_TN_LOCATION);
    	if (null != crumbData)
    	{
    		byte[] data = (byte[]) crumbData.get(index);
    		if (data != null)
    		{
    			loc = AndroidLocationUtil.fromBytes(data);
    		}
    		crumbData.clear();
    	}
    	return loc;
    }
    
    private synchronized void writeData(String name, byte[] buffer)
    {
        FileOutputStream fos = null;
        try
        {
            File dir = new File(this.rootPath + "/" + FILE_TN_LOCATION);
            if (!dir.exists())
            {
            	if (!dir.mkdir()) return;
            }
            
            File f = new File(this.rootPath + "/" + name);
            if (f.exists())
            {
            	if (!f.delete()) return;
            }
            if (f.createNewFile())
            {
                fos = new FileOutputStream(f);
                fos.write(buffer);
            }            
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if (fos != null)
                {
                    fos.close();
                }
            }
            catch (Exception e)
            {                    
            }
        }
    }
    
   private Object loadObject(String name)
    {
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try
        {
            File f = new File(this.rootPath + "/" + name);
            
            fis = new FileInputStream(f);
            ois = new ObjectInputStream(fis);
            Object obj = ois.readObject();
            return obj;
        }
        catch(Throwable e)
        {
            //don't need this log, for that currently there is not a easy way to check the file if exist in the system.
//            Logger.log(AndroidFilePersistentUtil.class.getName(), e);
        }
        finally
        {
            try
            {
                if (ois != null)
                    ois.close();
            }
            catch (Exception e)
            {
            	 e.printStackTrace();
            }
            try
            {
                if (fis != null)
                    fis.close();
            }
            catch (Exception e)
            {
            	 e.printStackTrace();
            }
        }
        return null;
    }
}
