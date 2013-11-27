/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * TnBacklightManagerTest.java
 *
 */
/**
 * 
 */
package com.telenav.backlight;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.powermock.reflect.Whitebox;


/**
 *@author Albert Ma (byma@telenav.cn)
 *@date 2011-6-28
 */
public class TnBacklightManagerTest
{
    TnBacklightManager tnBacklightManager;
    long timeOut;
    @Before
    public void setUp() throws Exception
    {
        tnBacklightManager = new TnBacklightManager()
        {

            
           
            /* (non-Javadoc)
             * @see com.telenav.backlight.TnBacklightManager#turnOnDelegate(long)
             */
            @Override
            protected void turnOnDelegate(long timeout)
            {
                TnBacklightManagerTest.this.timeOut = timeout;
            }

            /* (non-Javadoc)
             * @see com.telenav.backlight.TnBacklightManager#turnOff()
             */
            @Override
            public void turnOff()
            {
                // TODO Auto-generated method stub
                
            }

            /* (non-Javadoc)
             * @see com.telenav.backlight.TnBacklightManager#isOn()
             */
            @Override
            public boolean isOn()
            {
                // TODO Auto-generated method stub
                return false;
            }

            /* (non-Javadoc)
             * @see com.telenav.backlight.TnBacklightManager#enableKeyguard(boolean)
             */
            @Override
            public void enableKeyguard(boolean isOn)
            {
                // TODO Auto-generated method stub
                
            }
            
        };
        Whitebox.setInternalState(TnBacklightManager.class, "initCount", 0);
        TnBacklightManager.init(tnBacklightManager);
    }

    @After
    public void tearDown() throws Exception
    {
        
    }

    @Test
    public void testGetInstance()
    {
        assertEquals(tnBacklightManager, TnBacklightManager.getInstance());
    }

    @Test
    public void testInit()
    {
        assertEquals(tnBacklightManager, TnBacklightManager.getInstance());
    }

    @Test
    public void testTurnOn()
    {
        TnBacklightManager.init(tnBacklightManager);
        TnBacklightManager.getInstance().turnOn(500);
        assertTrue( timeOut > 0);
    }

}
