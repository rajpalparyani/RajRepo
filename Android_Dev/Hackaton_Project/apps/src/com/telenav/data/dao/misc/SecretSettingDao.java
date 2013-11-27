package com.telenav.data.dao.misc;

import com.telenav.data.dao.AbstractDao;
import com.telenav.persistent.TnStore;
/**
 *@author qyweng (qyweng@telenav.cn)
 *@date Aug 17, 2011
 */     
public class SecretSettingDao extends AbstractDao
{
    public static final int GPS_SOURCE_SELECT_INDEX = 1001;
    
    public static final int ONBOARD_MODE_INDEX = 1002;
    
    private TnStore secretSettingDao;

    public SecretSettingDao(TnStore gpsSourceStore)
    {
        this.secretSettingDao = gpsSourceStore;
    }
    
    public void remove(int key)
    {
        secretSettingDao.remove(key);
    }
    
    public void put(int key, int value)
    {
        secretSettingDao.put(key, ("" + value).getBytes());
    }
    
    public int get(int key)
    {
        byte[] data = secretSettingDao.get(key);
        
        if(data != null)
        {
            String s = new String(data);
            
            return Integer.parseInt(s);
        }
        
        return -1;
    }

    public void put(int key, String value)
    {
        secretSettingDao.put(key, value.getBytes());
    }
    
    public void put(int key, boolean value)
    {
        this.put(key, value ? "1" : "0");
    }
    
    public boolean getBoolean(int key)
    {
        String s = getString(key);
        if(s == null || s.length() == 0)
            return false;
        
        return Integer.parseInt(s) == 1 ? true : false;
    }
    
    public String getString(int key)
    {
        byte[] data = secretSettingDao.get(key);

        if (data != null)
        {
            String s = new String(data);
            return s;
        }

        return null;
    }
    
    public void store()
    {
        this.secretSettingDao.save();
    }
    
    public void clear()
    {
        this.secretSettingDao.clear();
    }
}
