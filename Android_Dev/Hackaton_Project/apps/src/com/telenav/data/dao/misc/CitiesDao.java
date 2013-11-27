/**
 * 
 */
package com.telenav.data.dao.misc;

import java.util.Vector;

import com.telenav.data.dao.serverproxy.NearCitiesDao;
import com.telenav.data.database.ICursor;
import com.telenav.data.database.IDatabase;
import com.telenav.data.database.IRow;
import com.telenav.data.datatypes.address.Stop;
import com.telenav.datatypes.DataUtil;

/**
 * @author yxyao
 * 
 */
public class CitiesDao extends NearCitiesDao
{

    static final String TABLE_CITY = "TN_CITIES";

    static final String TABLE_INDEX = "TN_CITIES_INDEX";

    static final String TABLE_CENTER = "TN_CENTER_MARK";

    public static final String COLUMN_PROVINCE = "PROVINCE";

    public static final String COLUMN_CITY = "CITY";

    public static final String COLUMN_COUNTRY = "COUNTRY";

    public static final String COLUMN_LAT = "LAT";

    public static final String COLUMN_LON = "LON";

    public static final String COLUMN_DEFAULT_CITY_ID = "CITY_ID";

    public static final String COLUMN_CENTER_LAT = "LAT";

    public static final String COLUMN_CENTER_LON = "LON";

    public static final String COLUMN_RADIUS = "RADIUS";

    private IDatabase mCityDb;

    // for quick access
    private int mDefaultCityId = -1;

    private CenterMark mCenter;

    /**
     * 
     */
    public CitiesDao(IDatabase cityDb)
    {
        super(null);
        this.mCityDb = cityDb;
        initTable();
        loadPersistData();
    }

    public boolean isCityFar(int lat, int lon, String region)
    {
        CenterMark cm = getCenter();
        if (cm == null)
        {
            return false;
        }
        long distance = DataUtil.distance((int) Math.abs(lat - cm.lat), (int) Math.abs(lon - cm.lon));
        return distance > cm.radius;
    }

    public Vector getNearCities(String region)
    {
        // XXX keep the behavior with super.
        return getAll();
    }

    public void putNearCities(String lat, String lon, String radius, Vector cities, String region)
    {
        int iLat = Integer.parseInt(lat);
        int iLon = Integer.parseInt(lon);
        int iRadius = Integer.parseInt(radius);
        clear();
        putAll(cities);
        setCenter(iLat, iLon, iRadius);
    }

    public String[] getProvinceNames(String country)
    {
        ICursor cursor = null;
        try
        {
            cursor = mCityDb.query(true, TABLE_CITY, new String[]
            { COLUMN_PROVINCE }, COLUMN_COUNTRY + "=?", new String[]
            { country }, null, "_id asc");
            return extractStrings(cursor);
        }
        finally
        {
            if (cursor != null)
            {
                cursor.close();
            }
        }
    }

    public String[] getCityNames(String province)
    {
        ICursor cursor = null;
        try
        {
            cursor = mCityDb.query(false, TABLE_CITY, new String[]
            { COLUMN_CITY }, COLUMN_PROVINCE + "=?", new String[]
            { province }, null, "_id asc");
            return extractStrings(cursor);
        }
        finally
        {
            if (cursor != null)
            {
                cursor.close();
            }
        }
    }

    public Stop getDefaultCity()
    {
        return getCity(getDefaultCityId());
    }

    public void setDefaultCity(String province, String city)
    {
        setDefaultCityId(getCityId(province, city));
    }

    public void clear()
    {
        clearTable(TABLE_INDEX);
        clearTable(TABLE_CITY);
        clearTable(TABLE_CENTER);
    }

    public void putAll(Vector cities)
    {
        try
        {
            mCityDb.beginTransaction();
            for (int i = 0; i < cities.size(); i++)
            {
                insertCity((Stop) cities.elementAt(i));
            }
            mCityDb.commit();
        }
        catch (Exception e)
        {
            mCityDb.rollback();
        }

    }

    public Vector getAll()
    {
        ICursor cursor = null;
        Vector allCities = new Vector();
        try
        {
            cursor = mCityDb.query(true, TABLE_CITY, new String[]
            { COLUMN_COUNTRY, COLUMN_PROVINCE, COLUMN_CITY, COLUMN_LAT, COLUMN_LON }, null, null, null, null);
            int count = cursor.getRowCount();
            for (int i = 0; i < count; i++)
            {
                cursor.moveTo(i);
                Stop stop = new Stop();
                stop.setCountry(cursor.getString(0));
                stop.setProvince(cursor.getString(1));
                stop.setCity(cursor.getString(2));
                stop.setLat((int) cursor.getNumber(3));
                stop.setLon((int) cursor.getNumber(4));
                allCities.addElement(stop);
            }
        }
        finally
        {
            if (cursor != null)
            {
                cursor.close();
            }
        }
        return allCities;
    }

    public CenterMark getCenter()
    {
        return mCenter;
    }

    public void setCenter(int lat, int lon, int radius)
    {
        boolean isUpdate = getRowCount(TABLE_CENTER) > 0;
        IRow row = mCityDb.createPendingRow();
        row.put(COLUMN_CENTER_LAT, lat);
        row.put(COLUMN_CENTER_LON, lon);
        row.put(COLUMN_RADIUS, radius);
        if (isUpdate)
        {
            mCityDb.update(TABLE_CENTER, row, null, null);
        }
        else
        {
            mCityDb.insert(TABLE_CENTER, row);
        }
        mCenter = new CenterMark(lat, lon, radius);
    }

    private void clearTable(String tableName)
    {
        mCityDb.execSQL("delete from " + tableName + " where 1=1", null);
    }

    private String[] extractStrings(ICursor cursor)
    {
        int rowCount = cursor.getRowCount();
        String[] result = new String[rowCount];
        for (int i = 0; i < rowCount; i++)
        {
            cursor.moveTo(i);
            result[i] = cursor.getString(0);
        }
        return result;
    }

    private void insertCity(Stop city)
    {
        IRow row = mCityDb.createPendingRow();
        row.put(COLUMN_COUNTRY, city.getCountry());
        row.put(COLUMN_PROVINCE, city.getProvince());
        row.put(COLUMN_CITY, city.getCity());
        row.put(COLUMN_LAT, city.getLat());
        row.put(COLUMN_LON, city.getLon());
        mCityDb.insert(TABLE_CITY, row);
    }

    private Stop getCity(int id)
    {
        ICursor cursor = null;
        try
        {
            cursor = mCityDb.query(false, TABLE_CITY, new String[]
            { COLUMN_COUNTRY, COLUMN_PROVINCE, COLUMN_CITY, COLUMN_LAT, COLUMN_LON }, "_id=?", new String[]
            { id + "" }, null, null);
            if (cursor.getRowCount() > 0)
            {
                cursor.moveTo(0);
                Stop stop = new Stop();
                stop.setCountry(cursor.getString(0));
                stop.setProvince(cursor.getString(1));
                stop.setCity(cursor.getString(2));
                stop.setLat((int) cursor.getNumber(3));
                stop.setLon((int) cursor.getNumber(4));
                return stop;
            }
            return null;
        }
        finally
        {
            if (cursor != null)
            {
                cursor.close();
            }
        }
    }

    private int getCityId(String province, String city)
    {
        ICursor cursor = null;
        try
        {
            cursor = mCityDb.query(false, TABLE_CITY, new String[]
            { "_id" }, COLUMN_PROVINCE + "=? and " + COLUMN_CITY + "=?", new String[]
            { province, city }, null, null);
            if (cursor.getRowCount() > 0)
            {
                cursor.moveTo(0);
                return (int) cursor.getNumber(0);
            }
            return 0;
        }
        finally
        {
            if (cursor != null)
            {
                cursor.close();
            }
        }
    }

    private int getDefaultCityId()
    {
        return mDefaultCityId;
    }

    private void setDefaultCityId(int index)
    {
        boolean isUpdate = getRowCount(TABLE_INDEX) > 0;
        IRow row = mCityDb.createPendingRow();
        row.put(COLUMN_DEFAULT_CITY_ID, index);
        if (isUpdate)
        {
            mCityDb.update(TABLE_INDEX, row, null, null);
        }
        else
        {
            mCityDb.insert(TABLE_INDEX, row);
        }
        mDefaultCityId = index;
    }

    private void initTable()
    {
        createTable(TABLE_CITY, new String[]
        { "_id INTEGER PRIMARY KEY", COLUMN_COUNTRY + " TEXT", COLUMN_PROVINCE + " TEXT", COLUMN_CITY + " TEXT", COLUMN_LAT + " INTEGER",
                COLUMN_LON + " INTEGER" });
        createTable(TABLE_INDEX, new String[]
        { "_id INTEGER PRIMARY KEY", COLUMN_DEFAULT_CITY_ID + " INTEGER" });
        createTable(TABLE_CENTER, new String[]
        { "_id INTEGER PRIMARY KEY", COLUMN_CENTER_LAT + " INTEGER", COLUMN_CENTER_LON + " INTEGER", COLUMN_RADIUS + " INTEGER" });
    }

    private void loadPersistData()
    {
        ICursor cursor = null;
        try
        {
            cursor = mCityDb.query(false, TABLE_INDEX, new String[]
            { COLUMN_DEFAULT_CITY_ID }, null, null, null, null);
            if (cursor.getRowCount() > 0)
            {
                cursor.moveTo(0);
                mDefaultCityId = (int) cursor.getNumber(0);
            }
            else
            {
                mDefaultCityId = 1;// XXX return 1 as default (primary key
                                   // start from 1)
            }
        }
        finally
        {
            if (cursor != null)
            {
                cursor.close();
            }
        }
        cursor = null;
        try
        {
            cursor = mCityDb.query(false, TABLE_CENTER, new String[]
            { COLUMN_CENTER_LAT, COLUMN_CENTER_LON, COLUMN_RADIUS }, null, null, null, null);
            if (cursor.getRowCount() > 0)
            {
                cursor.moveTo(0);
                mCenter = new CenterMark((int) cursor.getNumber(0), (int) cursor.getNumber(1), (int) cursor.getNumber(2));
            }
        }
        finally
        {
            if (cursor != null)
            {
                cursor.close();
            }
        }

    }

    private int getRowCount(String tableName)
    {
        ICursor cursor = null;
        try
        {
            cursor = mCityDb.query(false, tableName, new String[]
            { "_id" }, null, null, null, null);
            return cursor.getRowCount();
        }
        finally
        {
            if (cursor != null)
            {
                cursor.close();
            }
        }
    }

    private void createTable(String tableName, String[] columnsWithAttr)
    {
        if (columnsWithAttr.length == 0)
        {
            return;
        }
        StringBuffer sb = new StringBuffer("CREATE TABLE IF NOT EXISTS ");
        sb.append(tableName).append("(");
        for (int i = 0; i < columnsWithAttr.length; i++)
        {
            sb.append(columnsWithAttr[i]).append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(")");
        mCityDb.execSQL(sb.toString(), new String[0]);
    }

    public void store()
    {
    }

    static class CenterMark
    {
        CenterMark(int lat, int lon, int radius)
        {
            this.lat = lat;
            this.lon = lon;
            this.radius = radius;
        }

        final int lat;

        final int lon;

        final int radius;
    }
}
