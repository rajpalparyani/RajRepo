/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * TrafficProxy.java
 *
 */
package com.telenav.carconnect.provider.tnlink.module.traffic;

import java.io.IOException;
import java.util.Vector;

import android.util.Log;

import com.telenav.carconnect.CarConnectManager;
import com.telenav.carconnect.provider.tnlink.module.MyLog;
import com.telenav.comm.Comm;
import com.telenav.comm.Host;
import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.dao.misc.PreferenceDao;
import com.telenav.data.datatypes.preference.Preference;
import com.telenav.data.serverproxy.IServerProxyConstants;
import com.telenav.data.serverproxy.IServerProxyListener;
import com.telenav.data.serverproxy.IUserProfileProvider;
import com.telenav.data.serverproxy.impl.ITrafficProxy;
import com.telenav.data.serverproxy.impl.TrafficMissingAudioProxy;
import com.telenav.data.serverproxy.impl.protobuf.AbstractProtobufServerProxy;
import com.telenav.datatypes.traffic.TrafficIncident;
import com.telenav.datatypes.traffic.TrafficSegment;
import com.telenav.i18n.ResourceBundle;
import com.telenav.j2me.datatypes.ProtocolBuffer;
import com.telenav.j2me.framework.protocol.ProtoTrafficAlertsMovingMapReq;
import com.telenav.j2me.framework.protocol.ProtoTrafficAlertsMovingMapResp;
import com.telenav.j2me.framework.protocol.ProtoTrafficIncident;
import com.telenav.j2me.framework.protocol.ProtoTrafficSegment;
import com.telenav.j2me.framework.protocol.ProtoTrafficSummary;
import com.telenav.j2me.framework.protocol.ProtoTrafficSummaryReq;
import com.telenav.j2me.framework.protocol.ProtoTrafficSummaryResp;
import com.telenav.location.TnLocation;
import com.telenav.logger.Logger;
import com.telenav.navsdk.eventmanager.EventBus;
import com.telenav.navsdk.events.NavigationData;
import com.telenav.navsdk.events.NavigationEvents;
import com.telenav.navsdk.events.NavigationData.TrafficIncidentSeverity;
import com.telenav.navsdk.events.NavigationData.TrafficIncidentType;
import com.telenav.navsdk.events.NavigationEvents.TrafficDetailError;
import com.telenav.navsdk.events.NavigationEvents.TrafficDetailResponse;
import com.telenav.res.IStringCommon;
import com.telenav.res.ResourceManager;
import com.telenav.res.converter.StringConverter;

/**
 *@author fqming (fqming@telenav.cn)
 *@date Jul 20, 2010
 */
public class ProtoBufTrafficProxy extends AbstractProtobufServerProxy implements ITrafficProxy
{
    private static final int FAKE_DATA = 0; // 0 means don't use fake data, 1 means use fake data.

    protected TrafficSegment[] segments = null;
    
    public static final int[] DM6TOSPEED = { 12976, 8064 };
    
    public static final int DM6TOSPEED_SHIFT = 15;

    protected int severity;

    protected int numAlerts;

    protected long timeStamp;

    protected int totalDelay;

    protected long lastUpdateDurationTime;

    protected Vector vIncidents = new Vector();

    protected boolean isAvoidIncidentsResultInFasterRoute = true;

    protected boolean isCameraAlertOn;

    protected boolean isSpeedTrapOn;

    protected TrafficMissingAudioProxy trafficMissingAudioProxy;

    public ProtoBufTrafficProxy(Host host, Comm comm, IServerProxyListener serverProxyListener, boolean isCameraAlertOn, boolean isSpeedTrapOn,
            TrafficMissingAudioProxy trafficMissingAudioProxy, IUserProfileProvider userProfileProvider)
    {
        super(host, comm, serverProxyListener, userProfileProvider);

        this.isCameraAlertOn = isCameraAlertOn;
        this.isSpeedTrapOn = isSpeedTrapOn;
        this.trafficMissingAudioProxy = trafficMissingAudioProxy;
    }

    public Vector getIncidents()
    {
        return this.vIncidents;
    }

    public long getReportTime()
    {
        return this.timeStamp;
    }

    public long getLastUpdateDurationTime()
    {
        return lastUpdateDurationTime;
    }

    public TrafficSegment[] getSegments()
    {
        return segments;
    }

    public int getSeverity()
    {
        return this.severity;
    }

    public int getTotalDelay()
    {
        return this.totalDelay;
    }

    public int getTotalIncident()
    {
        return this.numAlerts;
    }

    /**
     * For traffic alert response, this flag indicate client should disable or enable avoiding traffic incidents
     * 
     */
    public boolean isAvoidIncidentsResultInFasterRoute()
    {
        return isAvoidIncidentsResultInFasterRoute;
    }

    public void requestAlertsMovingMap(int routeID, int segmentIndexID, int edgeID, String[] alertIDs, TnLocation[] fixes)
    {
        try{
            // reset before sending request
            isAvoidIncidentsResultInFasterRoute = true;
    
            Vector list = new Vector();
            
            ProtoTrafficAlertsMovingMapReq.Builder builder = ProtoTrafficAlertsMovingMapReq.newBuilder();
            builder.setRoutePathId(routeID);
            builder.setSegmentIndex(segmentIndexID);
            builder.setEdgeIndex(edgeID);
            builder.setShowSpeedTrap(true);
            builder.setShowTrafficCamera(true);
            if (alertIDs != null)
            {
                Vector v = new Vector();
                for (int i=0; i<alertIDs.length; i++)
                    v.addElement(alertIDs[i]);
                builder.setAlertID(v);
            }
            ProtocolBuffer pb = new ProtocolBuffer();
            pb.setBufferData(convertToByteArray(builder.build()));
            pb.setObjType(IServerProxyConstants.ACT_TRAFFIC_ALERT_MOVING_MAP);
            list.addElement(pb);
            
            this.sendRequest(list, IServerProxyConstants.ACT_TRAFFIC_ALERT_MOVING_MAP, fixes, fixes.length);
        }
        catch(Exception e)
        {
            Logger.log(this.getClass().getName(), e);
            if (listener != null)
                listener.networkError(this, IServerProxyListener.EXCEPTION_SEND, null);
        }
    }

    public void requestAlertsMovingMap(int routeID, int segmentIndexID, int edgeID, TnLocation[] fixes)
    {
        requestAlertsMovingMap(routeID, segmentIndexID, edgeID, null, fixes);
    }

    public void requestTrafficSummary(int routeID, int routeSegmentIndex, int edgeIndex, String[] alertIDs)
    {
        try{
            Vector list = new Vector();
            
            ProtoTrafficSummaryReq.Builder builder = ProtoTrafficSummaryReq.newBuilder();
            builder.setRoutePathId(routeID);
            builder.setSegmentIndex(routeSegmentIndex);
            builder.setEdgeIndex(edgeIndex);
            
            if (alertIDs != null)
            {
                Vector v = new Vector();
                for (int i = 0; i < alertIDs.length; i++)
                {
                    v.addElement(alertIDs[i]);
                }
                builder.setAlertID(v);
            }
            ProtocolBuffer pb = new ProtocolBuffer();
            pb.setBufferData(convertToByteArray(builder.build()));
            pb.setObjType(IServerProxyConstants.ACT_TRAFFIC_SUMMARY);
            list.addElement(pb);
            
            this.sendRequest(list, IServerProxyConstants.ACT_TRAFFIC_SUMMARY);
        }
        catch(Exception e)
        {
            Logger.log(this.getClass().getName(), e);
            if (listener != null)
                listener.networkError(this, IServerProxyListener.EXCEPTION_SEND, null);
        }
    }

    protected int parseRequestSpecificData(ProtocolBuffer protoBuf) throws IOException
    {
        if (IServerProxyConstants.ACT_TRAFFIC_SUMMARY.equals(protoBuf.getObjType()))
        {
            ProtoTrafficSummaryResp resp = ProtoTrafficSummaryResp.parseFrom(protoBuf.getBufferData());
            this.status = resp.getStatus();
            this.errorMessage = resp.getErrorMessage();
            if (this.status == IServerProxyConstants.SUCCESS)
            {
                parseTrafficSummary(resp.getTrafficSummary());
            }
        }
        else if (IServerProxyConstants.ACT_TRAFFIC_ALERT_MOVING_MAP.equals(protoBuf.getObjType()))
        {
            ProtoTrafficAlertsMovingMapResp resp = ProtoTrafficAlertsMovingMapResp.parseFrom(protoBuf.getBufferData());
            this.status = resp.getStatus();
            this.errorMessage = resp.getErrorMessage();
            if (this.status == IServerProxyConstants.SUCCESS)
            {
                parseMovingMap(resp);
            }
        }
        this.trafficMissingAudioProxy.startFetching();

        return this.status;
    }

    private void parseTrafficSummary(ProtoTrafficSummary summary)
    {
        severity = 99;
        numAlerts = 0;
        timeStamp = 0;
        totalDelay = 0;

        numAlerts = summary.getNumAlerts();
        totalDelay = (int)summary.getTotalDelay();
        timeStamp = summary.getTimeStamp();
//        lastUpdateDurationTime = summaryNode.getValueAt(3);

        // traffic segment node
        Vector v = summary.getTrafficSegments();
        if (v != null)
        {
            int size = v.size();
            segments = new TrafficSegment[size];
            for (int i = 0; i < size; i++)
            {
                ProtoTrafficSegment protoSeg = (ProtoTrafficSegment)v.elementAt(i);
                TrafficSegment seg = ProtoBufServerProxyUtil.convertTrafficSegment(protoSeg);
                if(seg.getIncidents() != null)
                {
                    for (int n = 0; n < seg.getIncidents().length; n++)
                    {
                        TrafficIncident incident = seg.getIncidents()[n];
                        if (incident.getStreetAudio() != null)
                        {
                            this.trafficMissingAudioProxy.putAudioIntoCache(incident.getStreetAudio());
                        }
                        if (incident.getLocationAudio() != null)
                        {
                            this.trafficMissingAudioProxy.putAudioIntoCache(incident.getLocationAudio());
                        }
                    }
                }
                segments[i] = seg;

                severity = Math.min(severity, getSeverity(seg.getIncidents()));
            }
            TrafficDetailResponse.Builder builder=convertResponse(segments);
            builder.setTrafficDelayInSeconds(totalDelay);
            CarConnectManager.getEventBus().broadcast("TrafficDetailResponse", builder.build());
        }else{
            TrafficDetailError.Builder builder=TrafficDetailError.newBuilder();
            EventBus bus=CarConnectManager.getEventBus();
            if(bus!=null) bus.broadcast("TrafficDetailError", builder.build());
        }
    }

    private int getSeverity(TrafficIncident[] incidents)
    {
        // The larger the severity number, the less severity the incident.
        int severity = 99;

        if (incidents != null)
        {
            for (int i = 0; i < incidents.length; i++)
            {
                if (incidents[i] == null)
                    continue;
                severity = Math.min(severity, incidents[i].getSeverity());
            }
        }
        return severity;
    }

    private void parseMovingMap(ProtoTrafficAlertsMovingMapResp resp)
    {
        this.totalDelay = resp.getTotalDelay();

        isAvoidIncidentsResultInFasterRoute = resp.getIsIncidentAvoidable();

        vIncidents.removeAllElements();

        Vector v= resp.getIncidents();
        for (int k = 0; v!=null && k<v.size(); k++)
        {
            TrafficIncident incident = ProtoBufServerProxyUtil.convertTrafficIncident((ProtoTrafficIncident)v.elementAt(k));
            if (incident.getStreetAudio() != null)
            {
                this.trafficMissingAudioProxy.putAudioIntoCache(incident.getStreetAudio());
            }
            if (incident.getLocationAudio() != null)
            {
                this.trafficMissingAudioProxy.putAudioIntoCache(incident.getLocationAudio());
            }

            vIncidents.addElement(incident);
        }
    }
    
    private TrafficDetailResponse.Builder convertResponse(TrafficSegment[] segments)
    {
        TrafficDetailResponse.Builder builder = TrafficDetailResponse.newBuilder() ;
        if(segments==null) return builder;
        
        StringConverter converter = ResourceManager.getInstance().getStringConverter();
        PreferenceDao prefDao = ((DaoManager) DaoManager.getInstance())
                .getPreferenceDao();
        int systemUnits = prefDao.getIntValue(Preference.ID_PREFERENCE_DISTANCEUNIT);

        for (int i = 0; i < segments.length; ++i)
        {
            TrafficSegment segment = segments[i];
            if(segment==null) continue;
            
            int avgSpeed=segment.getAvgSpeed();
            int postedSpeed=segment.getPostedSpeed();
            
            NavigationData.TrafficDetail.Builder detailBuilder = NavigationData.TrafficDetail.newBuilder();
            String name=segment.getName();
            if(name!=null)
            {
                detailBuilder.setDisplayStreetName(name);
            }
            
            if( postedSpeed>0 ){
                float rate= (float) avgSpeed/ (float) postedSpeed;
                
                if(rate>=0.75) detailBuilder.setCurrentLeg(false);
                else detailBuilder.setCurrentLeg(true);
            }
            
            avgSpeed=convertSpeed(avgSpeed);
            postedSpeed=convertSpeed(postedSpeed);
            
            detailBuilder.setLegSpeed(avgSpeed);
            detailBuilder.setLegFreeFlowSpeed(postedSpeed);

            TrafficIncident[] incidents = segment.getIncidents();
            int incidentNum = incidents == null ? 0 : incidents.length;

            for (int j = 0; j < incidentNum; ++j)
            {
                TrafficIncident incident = incidents[j];
                NavigationData.TrafficIncident.Builder incidentBuilder = NavigationData.TrafficIncident.newBuilder();
                incidentBuilder.setIncidentSeverity(TrafficIncidentSeverity.valueOf(incident.getSeverity()));
                incidentBuilder.setIncidentType(TrafficIncidentType.valueOf(incident.getIncidentType()));
                if(incident.getMsg()!=null){
                    incidentBuilder.setShortSummary(incident.getMsg());
                    MyLog.setLog("traffic", incident.getMsg());
                }
                detailBuilder.addIncidents(incidentBuilder);
            }
            builder.addTrafficDetails(detailBuilder);
        }
        return builder;
    }
    
    private void onTrafficError(int errorCode, String errorMsg)
    {
        NavigationEvents.TrafficDetailError.Builder builder = NavigationEvents.TrafficDetailError.newBuilder();
        if (errorMsg!=null)
            builder.setErrorString(errorMsg);
        builder.setError(NavigationData.NavigationErrorCode.valueOf(errorCode));
        EventBus bus=CarConnectManager.getEventBus();
        if(bus!=null) bus.broadcast("TrafficDetailError", builder.build());
    }
    
    public int convertSpeed(int speed)
    {
        if (speed < 0)
        {
            return speed;
        }

        int displaySpeed = speed;
        displaySpeed *= DM6TOSPEED[0];
        displaySpeed >>= DM6TOSPEED_SHIFT;

        return displaySpeed;
    }
}
