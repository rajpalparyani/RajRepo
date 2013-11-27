/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * CitiesDaoTest.java
 *
 */
package com.telenav.data.dao.misc;

import java.util.Vector;

import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.easymock.EasyMock;
import org.junit.runner.RunWith;

import com.telenav.data.dao.misc.CitiesDao.CenterMark;
import com.telenav.data.database.IDatabase;
import com.telenav.data.database.IRow;
import com.telenav.data.database.TnDatabaseManager;
import com.telenav.datatypes.DataUtil;
import com.telenav.persistent.TnStore;
import junit.framework.TestCase;

/**
 * @author yhzhou
 * @date 2011-9-23
 */
@RunWith(PowerMockRunner.class)
public class CitiesDaoTest extends TestCase
{

    TnStore store = null;

    protected void setUp() throws Exception
    {
        super.setUp();
    }

    protected void tearDown() throws Exception
    {

        super.tearDown();
        PowerMock.verifyAll();
    }

    public void testIsCityFar()
    {
        CenterMark centerMark = new CenterMark(351738, 1005468, 1000);
        int lat = 351840;
        int lon = 1006666;
        long distance = DataUtil.distance((int) Math.abs(lat - centerMark.lat), (int) Math.abs(lon - centerMark.lon));
        assertTrue(distance > centerMark.radius);
        
        int lat1 = 351740;
        int lon1 = 1005466;
        long distance1 = DataUtil.distance((int) Math.abs(lat - centerMark.lat), (int) Math.abs(lon - centerMark.lon));
        assertTrue(distance1 > centerMark.radius);
    }

//    public void testGetNearCities()
//    {
//        CitiesDao citiesDao = PowerMock.createPartialMock(CitiesDao.class, "initTable", "loadPersistData", "getAll");
//        Vector vector = new Vector();
//        EasyMock.expect(citiesDao.getNearCities()).andReturn(vector);
//        PowerMock.replayAll();
//        assertEquals(vector, citiesDao.getNearCities());
//    }

}
