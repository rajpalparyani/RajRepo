/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * DayNightService.java
 *
 */
package com.telenav.ui.citizen.map;

import java.util.Vector;

import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.datatypes.preference.Preference;
import com.telenav.logger.Logger;
import com.telenav.module.location.LocationDayModeChecker;

/**
 *@author wchshao
 *@date Dec 20, 2012
 */
public class DayNightService implements Runnable
{
    private static DayNightService daynightService;
    private long updateInterval = 5 * 60 * 1000;
    private boolean isStarted;
    protected Object serviceMutex = new Object();
    protected Thread callbackThread;
    protected Vector dayNightListeners;
    
    public synchronized static DayNightService getInstance()
    {
        if (daynightService == null)
        {
            daynightService = new DayNightService();
        }
        return daynightService;
    }

    public int getMapColor()
    {
        int mapColor;
        Preference mapColorPref = ((DaoManager) DaoManager.getInstance()).getPreferenceDao().getPreference(
            Preference.ID_PREFERENCE_MAP_COLORS);

        if (mapColorPref == null)
        {
            return IMapContainerConstants.MAP_DAY_COLOR;
        }
        int mapColorValue = mapColorPref.getIntValue();
        if (mapColorValue == Preference.MAP_COLORS_DAYTIME)
        {
            mapColor = IMapContainerConstants.MAP_DAY_COLOR;
        }
        else if (mapColorValue == Preference.MAP_COLORS_NIGHTTIME)
        {
            mapColor = IMapContainerConstants.MAP_NIGHT_COLOR;
        }
        else if (mapColorValue == Preference.MAP_COLORS_AUTO)
        {
            mapColor = LocationDayModeChecker.isDayMode() ? IMapContainerConstants.MAP_DAY_COLOR : IMapContainerConstants.MAP_NIGHT_COLOR;
        }
        else
        {
            mapColor = IMapContainerConstants.MAP_DAY_COLOR;
        }
        return mapColor;
    }
    
    public boolean isMapColorAutoMode()
    {
        Preference mapColorPref = ((DaoManager) DaoManager.getInstance()).getPreferenceDao().getPreference(
            Preference.ID_PREFERENCE_MAP_COLORS);
        if (mapColorPref == null)
        {
            return true;
        }
        int mapColorValue = mapColorPref.getIntValue();
        if (mapColorValue == Preference.MAP_COLORS_AUTO)
        {
            return true;
        }
        return false;
    }
    
    public int getMapColor(boolean isShowSatellite)
    {
        if (isShowSatellite) {
            return IMapContainerConstants.MAP_SAT_COLOR;
        }
        else
        {
            return getMapColor();
        }
    }
    
    public void start()
    {
        synchronized (serviceMutex)
        {
            if (isStarted)
            {
                return;
            }

            isStarted = true;

            if (this.callbackThread == null || !this.callbackThread.isAlive())
            {
                this.callbackThread = new Thread(this, "DayNightService-Thread");
                this.callbackThread.start();
            }
        }
    }
    
    public void stop()
    {
        synchronized (serviceMutex)
        {
            if(!isStarted)
            {
                return;
            }
            
            isStarted = false;
            serviceMutex.notifyAll();
            
            this.removeAllListeners();
        }
    }

    protected void restart(boolean quickly)
    {
        this.stop();

        if(!quickly)
        {
            try
            {
                Thread.sleep(1500);
            }
            catch (InterruptedException e)
            {
                Logger.log(this.getClass().getName(), e);
            }
        }

        this.start();
    }
    
    public void removeAllListeners()
    {
        if(this.dayNightListeners == null)
            return;
        
        this.dayNightListeners.removeAllElements();
    }
    
    private void notifyListeners()
    {
        if (dayNightListeners != null)
        {
            for (int i = 0; i < dayNightListeners.size(); i++)
            {
                try
                {
                    IDayNightListener dayNightListener = (IDayNightListener) dayNightListeners.elementAt(i);
                    dayNightListener.updateDayNightStatus();
                }
                catch (Throwable t)
                {
                    t.printStackTrace();
                }
            }
        }
    }
    
    public void addListener(IDayNightListener dayNightListener)
    {
        if (this.dayNightListeners == null)
        {
            this.dayNightListeners = new Vector();
        }

        this.dayNightListeners.addElement(dayNightListener);
    }
    
    public void removeListener(IDayNightListener dayNightListener)
    {
        if (this.dayNightListeners == null)
            return;

        this.dayNightListeners.removeElement(dayNightListener);
    }

    @Override
    public void run()
    {
        while (isStarted)
        {
            try
            {
                if (!isStarted)
                {
                    break;
                }
                // no need to update day/night status here, the listeners will update it
                notifyListeners();

                if (!isStarted)
                {
                    break;
                }
                synchronized (serviceMutex)
                {
                    serviceMutex.wait(updateInterval);
                }
            }
            catch (Exception e)
            {
                Logger.log(this.getClass().getName(), e);
            }
        }
    }
    
    public interface IDayNightListener
    {
        public void updateDayNightStatus();
    }
    
}
