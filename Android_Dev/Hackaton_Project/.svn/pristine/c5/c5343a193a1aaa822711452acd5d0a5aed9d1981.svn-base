/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * NearCitiesDao.java
 *
 */
package com.telenav.data.dao.serverproxy;

import java.util.Vector;

import com.telenav.data.dao.AbstractDao;
import com.telenav.data.datatypes.address.Stop;
import com.telenav.data.datatypes.primitive.BytesList;
import com.telenav.data.datatypes.primitive.StringList;
import com.telenav.data.serializable.SerializableManager;
import com.telenav.datatypes.DataUtil;
import com.telenav.persistent.TnStore;

/**
 *@author fqming (fqming@telenav.cn)
 *@date Jul 19, 2010
 */
public class NearCitiesDao extends AbstractDao
{
    private final static int NEAR_CITIY_INDEX = 200001;

    private final static int CENTER_CITIY_ARGS_INDEX = 200002;
    
    private final static int CENTER_CITIY_ARGS_CHANGED_INDEX = 200003;

    private TnRegionDependentStoreProvider nearCitiesProvider;
    public NearCitiesDao(TnStore nearCitiesStore)
    {
        if (nearCitiesStore != null)
        {
            this.nearCitiesProvider = new TnRegionDependentStoreProvider(nearCitiesStore.getName(),nearCitiesStore.getType());
        }
    }

    public boolean isCityFar(int lat, int lon, String region)
    {
        byte[] centerCityArgsData = this.nearCitiesProvider.getStore(region).get(CENTER_CITIY_ARGS_INDEX);
        if (centerCityArgsData != null)
        {
            StringList centerCityArgsNode = SerializableManager.getInstance().getPrimitiveSerializable().createStringList(centerCityArgsData);
            int clat = Integer.parseInt(centerCityArgsNode.elementAt(0));
            int clon = Integer.parseInt(centerCityArgsNode.elementAt(1));
            int cradius = Integer.parseInt(centerCityArgsNode.elementAt(2));

            long distance = DataUtil.distance((int) Math.abs(lat - clat), (int) Math.abs(lon - clon));
            if (distance < cradius / 3) // temp hack to trigger more city updates. Need to improve by resorting the available list on client.
            {
                return false;
            }
        }

        return true;
    }

    public synchronized Vector getNearCities(String region)
    {
        byte[] data = this.nearCitiesProvider.getStore(region).get(NEAR_CITIY_INDEX);

        if (data == null)
            return null;

        Vector vecCity = new Vector();
        BytesList centerCitysNode = SerializableManager.getInstance().getPrimitiveSerializable().createBytesList(data);
        for(int i = 0; i < centerCitysNode.size(); i++)
        {
            byte[] stopData = centerCitysNode.elementAt(i);
            Stop stop = SerializableManager.getInstance().getAddressSerializable().createStop(stopData);
            vecCity.addElement(stop);
        }
        this.nearCitiesProvider.getStore(region).put(CENTER_CITIY_ARGS_CHANGED_INDEX, new byte[]{0});
        return vecCity;
    }
    
    public boolean isNearCityDownloaded(String region)
    {
        boolean flag = false;
        byte[] data = this.nearCitiesProvider.getStore(region).get(NEAR_CITIY_INDEX);
        if (data != null)
            flag = true;
        return flag;
    }


    public synchronized void putNearCities(String lat, String lon, String radius, Vector cities, String region)
    {
        StringList list = new StringList();
        list.add(lat);
        list.add(lon);
        list.add(radius);

        BytesList cityList = new BytesList();
        if(cities != null)
        {
            for(int i = 0; i < cities.size(); i++)
            {
                Stop stop = (Stop)cities.elementAt(i);
                cityList.add(SerializableManager.getInstance().getAddressSerializable().toBytes(stop));
            }
        }
        this.nearCitiesProvider.getStore(region).put(CENTER_CITIY_ARGS_INDEX, SerializableManager.getInstance().getPrimitiveSerializable().toBytes(list));
        this.nearCitiesProvider.getStore(region).put(NEAR_CITIY_INDEX, SerializableManager.getInstance().getPrimitiveSerializable().toBytes(cityList));
        this.nearCitiesProvider.getStore(region).put(CENTER_CITIY_ARGS_CHANGED_INDEX, new byte[]{1});
    }
    
    public synchronized void store()
    {
        this.nearCitiesProvider.save();
    }
    
    public synchronized void clear()
    {
        this.nearCitiesProvider.clear();
    }
    
    public boolean getNearCityChanged(String region)
    {
        byte[] city = this.nearCitiesProvider.getStore(region).get(CENTER_CITIY_ARGS_CHANGED_INDEX);
        if (city != null && city.length ==1 && city[0] == 1)
        {
            return true;
        }
        return false;
    }
}
