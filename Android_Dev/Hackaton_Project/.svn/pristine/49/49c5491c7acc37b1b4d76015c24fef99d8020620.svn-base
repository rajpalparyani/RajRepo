/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * NavsdkBacklightService.java
 *
 */
package com.telenav.data.serverproxy.impl.navsdk.service;

import com.telenav.app.TnBacklightManagerImpl;
import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.datatypes.preference.Preference;
import com.telenav.navsdk.eventmanager.EventBus;
import com.telenav.navsdk.events.BacklightData.BacklightStatus;
import com.telenav.navsdk.events.BacklightEvents.SwitchBacklight;
import com.telenav.navsdk.services.BacklightListener;
import com.telenav.navsdk.services.BacklightServiceProxy;

/**
 * @author pwang
 * @date 2012-12-5
 */
public class NavsdkBacklightService implements BacklightListener
{
    private static NavsdkBacklightService instance;

    private BacklightServiceProxy serverProxy;
    
    private ITurnListener turnListener;

    private TnBacklightManagerImpl backlighService;

    private NavsdkBacklightService()
    {
        backlighService = TnBacklightManagerImpl.getInstance();
    }
    private void setEventBus(EventBus bus)
    {
        serverProxy = new BacklightServiceProxy(bus);
        serverProxy.addListener(this);
    }
    
    public synchronized static void init(EventBus bus)
    {
        if (instance == null)
        {
            instance = new NavsdkBacklightService();
            instance.setEventBus(bus);
        }
    }

    public static NavsdkBacklightService getInstance()
    {
        return instance;
    }

    public void setTurnListener(ITurnListener turnListener)
    {
        this.turnListener = turnListener;
    }

    public void onSwitchBacklight(SwitchBacklight event)
    {
        handle(event);
    }

    // Currently NavSDK only send 'always on' and 'off'
    // So the client behavior is different from previous:
    // Previously when enter turn, client will light the backlight, and the backlight will turn off automaticlly by
    // system default.
    // Currently when enter turn, navsdk will send 'always on' and client will turn on; then leave the turn, navsdk will
    // send 'off', and client should turn off the backlight immediately.
    private void handle(SwitchBacklight event)
    {
        //System.out.println("DB-Test --> backlight event : " + event.getStatus().getNumber());
        int backlightPref = ((DaoManager) DaoManager.getInstance()).getPreferenceDao().getIntValue(
            Preference.ID_PREFERENCE_BACKLIGHT);
        switch (event.getStatus().getNumber())
        {
            case BacklightStatus.BacklightStatus_AlwaysOn_VALUE:
            {
                if (backlightPref != Preference.BACKLIGHT_OFF)
                {
                    backlighService.enable();
                    backlighService.start();
                }
                if (turnListener != null)
                {
                    turnListener.enterTurn();
                }
                break;
            }
            case BacklightStatus.BacklightStatus_On_VALUE:
            {
                //actually, NavSDK never call this situation.
                break;
            }
            case BacklightStatus.BacklightStatus_Off_VALUE:
            {
                if (backlightPref != Preference.BACKLIGHT_ON)
                {
                    backlighService.stop();
                }
                if (turnListener != null)
                {
                    turnListener.exitTurn();
                }
                break;
            }
            default:
            {
                break;
            }
        }
    }

}
