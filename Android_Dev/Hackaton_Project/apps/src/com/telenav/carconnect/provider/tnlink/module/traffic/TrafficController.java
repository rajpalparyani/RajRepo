/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * TrafficController.java
 *
 */
package com.telenav.carconnect.provider.tnlink.module.traffic;

import java.util.Vector;

import android.util.Log;

import com.telenav.carconnect.CarConnectEvent;
import com.telenav.carconnect.CarConnectManager;
import com.telenav.carconnect.provider.tnlink.module.AbstractBaseModel;
import com.telenav.carconnect.provider.tnlink.module.AbstractBaseView;
import com.telenav.carconnect.provider.tnlink.module.AbstractController;
import com.telenav.carconnect.provider.tnlink.module.MyLog;
import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.dao.misc.PreferenceDao;
import com.telenav.data.datatypes.preference.Preference;
import com.telenav.datatypes.traffic.TrafficIncident;
import com.telenav.datatypes.traffic.TrafficSegment;
import com.telenav.navsdk.events.NavigationData;
import com.telenav.navsdk.events.NavigationEvents;
import com.telenav.navsdk.events.NavigationData.TrafficIncidentSeverity;
import com.telenav.navsdk.events.NavigationData.TrafficIncidentType;
import com.telenav.navsdk.events.NavigationEvents.TrafficDetailResponse;
import com.telenav.res.ResourceManager;
import com.telenav.res.converter.StringConverter;

/**
 *@author xiangli
 *@date 2012-3-1
 */
public class TrafficController extends AbstractController implements ITrafficConstants
{

    @Override
    protected AbstractBaseModel createModel()
    {
        // TODO Auto-generated method stub
        return new TrafficModel();
    }

    @Override
    protected AbstractBaseView createView()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void postModelEvent(int eventType)
    {
        // TODO Auto-generated method stub
        Log.i("Traffic","postModelEvent");
        switch (eventType)
        {
            case EVENT_MODEL_RESPONSE_TRAFFIC_SEGMENT:
            {
                Log.i("Traffic","EVENT_MODEL_RESPONSE_TRAFFIC_SEGMENT");
                TrafficSegment[] segments = (TrafficSegment[])model.get(KEY_O_TRAFFICINFOR);
                if (null==segments)
                    return;
                
                TrafficDetailResponse response = convertResponse(segments);
                CarConnectManager.getEventBus().broadcast("TrafficDetailResponse", response);
                break;
            }
            case EVENT_MODEL_TRAFFIC_SEGMENT_ERROR:
            {
                Log.i("Traffic","EVENT_MODEL_TRAFFIC_SEGMENT_ERROR");
                int errorCode = model.getInt(KEY_I_ERROR_CODE);
                String errorMsg = model.getString(KEY_S_ERROR_MSG);
                NavigationEvents.TrafficDetailError response = constructTrafficError(errorCode, errorMsg);
                CarConnectManager.getEventBus().broadcast(CarConnectEvent.TRAFFIC_DETAIL_ERROR, response);
                break;
            }
            default:
            {
                Log.i("Traffic","Default");
                break;
            }
        }
    }
    
    private TrafficDetailResponse convertResponse(TrafficSegment[] segments)
    {
        StringConverter converter = ResourceManager.getInstance().getStringConverter();
        PreferenceDao prefDao = ((DaoManager) DaoManager.getInstance())
                .getPreferenceDao();
        int systemUnits = prefDao.getIntValue(Preference.ID_PREFERENCE_DISTANCEUNIT);

        TrafficDetailResponse.Builder builder = TrafficDetailResponse.newBuilder() ;
        int delay = model.getInt(KEY_I_TOTAL_DELAY);
        builder.setTrafficDelayInSeconds(delay);
        
        for (int i = 0; i < segments.length; ++i)
        {
            TrafficSegment segment = segments[i];
            NavigationData.TrafficDetail.Builder detailBuilder = NavigationData.TrafficDetail.newBuilder();
            detailBuilder.setDescription(segment.getName());
            
            MyLog.setLog("traffic", segment.getName());
            
            String speed = converter.convertSpeed(segment.getAvgSpeed(), systemUnits);
            String length = converter.convertDistance(segment.getLength(), systemUnits);
            
            detailBuilder.setLegSpeed(segment.getAvgSpeed());

            TrafficIncident[] incidents = segment.getIncidents();
            int incidentNum = incidents == null ? 0 : incidents.length;

            for (int j = 0; j < incidentNum; ++j)
            {
                TrafficIncident incident = incidents[j];
                NavigationData.TrafficIncident.Builder incidentBuilder = NavigationData.TrafficIncident.newBuilder();
                incidentBuilder.setIncidentSeverity(TrafficIncidentSeverity.valueOf(incident.getSeverity()));
                incidentBuilder.setIncidentType(TrafficIncidentType.valueOf(incident.getIncidentType()));
                incidentBuilder.setShortSummary(incident.getMsg());
                MyLog.setLog("traffic", incident.getMsg());
                detailBuilder.addIncidents(incidentBuilder);
            }
            
            builder.addTrafficDetails(detailBuilder);

        }
        
        return builder.build();
    }
    
    private NavigationEvents.TrafficDetailError constructTrafficError(int errorCode, String errorMsg)
    {
        NavigationEvents.TrafficDetailError.Builder builder = NavigationEvents.TrafficDetailError.newBuilder();
        if (errorMsg!=null)
            builder.setErrorString(errorMsg);
        builder.setError(NavigationData.NavigationErrorCode.valueOf(errorCode));
        return builder.build();
    }

}
