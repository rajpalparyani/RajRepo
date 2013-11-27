/**
 *
 * Copyright 2013 TeleNav, Inc. All rights reserved.
 * VbbDetailModel.java
 *
 */
package com.telenav.module.vbb;

import org.json.tnme.JSONArray;
import org.json.tnme.JSONException;
import org.json.tnme.JSONObject;

import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.datatypes.address.Address;
import com.telenav.data.datatypes.preference.Preference;
import com.telenav.datatypes.DataUtil;
import com.telenav.htmlsdk.IHtmlSdkServiceHandler;
import com.telenav.htmlsdk.IHtmlSdkServiceListener;
import com.telenav.htmlsdk.ResultGenerator;
import com.telenav.location.TnLocation;
import com.telenav.module.browsersdk.BrowserSdkModel;
import com.telenav.module.location.LocationProvider;
import com.telenav.res.ResourceManager;
import com.telenav.ui.citizen.map.MapContainer;

/**
 *@author yning
 *@date 2013-1-6
 */
public class VbbDetailModel extends BrowserSdkModel implements IVbbDetailConstants
{
    @Override
    protected void activateDelegate(boolean isUpdateView)
    {
        MapContainer.getInstance().resume();
        super.activateDelegate(isUpdateView);
    }
    
    @Override
    public JSONObject doMap(JSONArray args, IHtmlSdkServiceListener listener)
    {
        int serviceId = ResultGenerator.SERVICE_ID_ZERO;
        JSONObject ret = null;
        
        try
        {
            serviceId = args.getInt(1);
            
            if (args.get(0) != null)
            {
                Address address = convertAddress(
                    args.getJSONObject(0).toString().getBytes(), (byte)-1);
                this.put(KEY_O_AD_ADDRESS, address);
                this.postModelEvent(EVENT_MODEL_MAP_AD);
            }
            
            ret = ResultGenerator.generateJsonResult(ResultGenerator.STATUS_SUCCESS, serviceId);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
            ret = ResultGenerator.generateJsonResult(ResultGenerator.STATUS_FAIL, serviceId);
        }

        return ret;
    }
    
    /**
     * The only case we invoke doNav is from billboard
     */
    public JSONObject doNav(JSONArray args, IHtmlSdkServiceListener listener)
    {
        int serviceId = ResultGenerator.SERVICE_ID_ZERO;
        JSONObject ret = null;
        
        try
        {
            serviceId = args.getInt(2);
            
            if (args.get(1) != null)
            {
                Address adAddress = convertAddress(args.getJSONObject(1).toString().getBytes(), Address.TYPE_RECENT_STOP);
                this.put(KEY_O_AD_ADDRESS, adAddress);
                this.postModelEvent(EVENT_MODEL_DRIVE_TO_AD);
                
                ret = ResultGenerator.generateJsonResult(ResultGenerator.STATUS_SUCCESS, serviceId);
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
            ret = ResultGenerator.generateJsonResult(ResultGenerator.STATUS_FAIL, serviceId);
        }        
        
        return ret;
    }
    
    @Override
    public JSONObject doNativeService(JSONObject request, IHtmlSdkServiceListener listener)
    {
        int serviceId = ResultGenerator.SERVICE_ID_ZERO;
        try
        {
            String serviceName = null; 
            if (request != null) 
            {    
                serviceId = request.getInt(IHtmlSdkServiceHandler.SERVICE_ID);
                serviceName = request.getString(IHtmlSdkServiceHandler.SERVICE_NAME);
            }
            else
            {
                return ResultGenerator.generateJsonResult(ResultGenerator.STATUS_FAIL, serviceId);
            }
            
            if ("GetBillboardDistance".equals(serviceName))
            {
                JSONObject serviceData = request.getJSONObject(IHtmlSdkServiceHandler.SERVICE_DATA);
                int lat = serviceData.getInt("lat");
                int lon = serviceData.getInt("lon");

                TnLocation location = LocationProvider.getInstance().getCurrentLocation(LocationProvider.TYPE_GPS);

                if (location != null)
                {

                    int currentLat = location.getLatitude();
                    int currentLon = location.getLongitude();

                    int cosLat = DataUtil.getCosLat(currentLat);
                    int distance = DataUtil.gpsDistance(currentLat - lat, currentLon - lon, cosLat);

                    int systemUnits = ((DaoManager) DaoManager.getInstance()).getPreferenceDao().getIntValue(
                        Preference.ID_PREFERENCE_DISTANCEUNIT);
                    String distanceStr = ResourceManager.getInstance().getStringConverter().convertDistance(distance, systemUnits);
                    
                    JSONObject result = new JSONObject();
                    result.put("distance", distanceStr);
                    
                    return ResultGenerator.generateJsonResult(ResultGenerator.STATUS_SUCCESS, serviceId, result.toString());
                }
                
                return ResultGenerator.generateJsonResult(ResultGenerator.STATUS_FAIL, serviceId);
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
            return ResultGenerator.generateJsonResult(ResultGenerator.STATUS_FAIL, serviceId);
        }

        return super.doNativeService(request, listener);
    }
}
