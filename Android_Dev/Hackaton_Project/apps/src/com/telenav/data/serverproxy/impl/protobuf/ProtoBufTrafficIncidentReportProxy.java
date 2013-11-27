/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * TrafficIncidentReportProxy.java
 *
 */
package com.telenav.data.serverproxy.impl.protobuf;

import java.io.IOException;
import java.util.Vector;

import com.telenav.comm.Comm;
import com.telenav.comm.Host;
import com.telenav.data.serverproxy.IServerProxyConstants;
import com.telenav.data.serverproxy.IServerProxyListener;
import com.telenav.data.serverproxy.IUserProfileProvider;
import com.telenav.data.serverproxy.RequestItem;
import com.telenav.data.serverproxy.impl.ITrafficIncidentReportProxy;
import com.telenav.j2me.datatypes.ProtocolBuffer;
import com.telenav.j2me.framework.protocol.ProtoTrafficIncidentReportReq;
import com.telenav.j2me.framework.protocol.ProtoTrafficIncidentReportResp;
import com.telenav.location.TnLocation;
import com.telenav.util.PrimitiveTypeCache;

/**
 *@author fqming (fqming@telenav.cn)
 *@date Jul 20, 2010
 */
public class ProtoBufTrafficIncidentReportProxy extends AbstractProtobufServerProxy implements ITrafficIncidentReportProxy
{
    private static final int SERVICE_TYPE_REPORT_NAV = 1;
    private static final int SERVICE_TYPE_REPORT_MAP = 2;
    
    public ProtoBufTrafficIncidentReportProxy(Host host, Comm comm, IServerProxyListener serverProxyListener, IUserProfileProvider userProfileProvider)
    {
        super(host, comm, serverProxyListener, userProfileProvider);
    }

    public String sendIncidentMapReport(int incidentType, TnLocation gpsFix)
    {
        try
        {
            RequestItem requestItem = new RequestItem(IServerProxyConstants.ACT_INCIDENT_REPORT, this);
            requestItem.params = new Vector();
            requestItem.params.addElement(PrimitiveTypeCache.valueOf(incidentType));
            requestItem.params.addElement(PrimitiveTypeCache.valueOf(SERVICE_TYPE_REPORT_MAP));
            requestItem.params.addElement(gpsFix);

            Vector list = createProtoBufReq(requestItem);
            return this.sendRequest(list, IServerProxyConstants.ACT_INCIDENT_REPORT);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            if (listener != null)
                listener.networkError(this, IServerProxyListener.EXCEPTION_SEND, null);
            return "";
        }
    }

    public String sendIncidentNavReport(int incidentType, TnLocation gpsFix)
    {
        try
        {
            RequestItem requestItem = new RequestItem(IServerProxyConstants.ACT_INCIDENT_REPORT, this);
            requestItem.params = new Vector();
            requestItem.params.addElement(PrimitiveTypeCache.valueOf(incidentType));
            requestItem.params.addElement(PrimitiveTypeCache.valueOf(SERVICE_TYPE_REPORT_NAV));
            requestItem.params.addElement(gpsFix);

            Vector list = createProtoBufReq(requestItem);
            return this.sendRequest(list, IServerProxyConstants.ACT_INCIDENT_REPORT);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            if (listener != null)
                listener.networkError(this, IServerProxyListener.EXCEPTION_SEND, null);
            return "";
        }
    }

    protected void addRequestArgs(Vector requestVector,RequestItem requestItem) throws Exception
    {
        if (IServerProxyConstants.ACT_INCIDENT_REPORT.equals(requestItem.action))
        {
            if (requestItem.params != null && requestItem.params.size() >= 3)
            {
                Object incidentTypeObj = requestItem.params.elementAt(0);
                Object reportFromObj = requestItem.params.elementAt(1);
                Object gpsFixObj = requestItem.params.elementAt(2);
                addReportInfo(requestVector, ((Integer) incidentTypeObj).intValue(), ((Integer) reportFromObj).intValue(),
                    (TnLocation) gpsFixObj);
            }
            else
            {
                throw new IllegalArgumentException("request params is null or size is wrong.");
            }
        }
    }
    
    private void addReportInfo(Vector requestVector, int incidentType, int reportFrom, TnLocation gpsFix) throws Exception
    {
        ProtoTrafficIncidentReportReq.Builder builder = ProtoTrafficIncidentReportReq.newBuilder();
        builder.setIncidentType(incidentType+"");
        builder.setReportFrom(reportFrom+"");
        builder.setLat(gpsFix.getLatitude());
        builder.setLon(gpsFix.getLongitude());
        
        ProtocolBuffer pb = new ProtocolBuffer();
        pb.setBufferData(convertToByteArray(builder.build()));
        pb.setObjType(IServerProxyConstants.ACT_INCIDENT_REPORT);
        requestVector.addElement(pb);
    }

    protected int parseRequestSpecificData(ProtocolBuffer protoBuf) throws IOException
    {
        if (IServerProxyConstants.ACT_INCIDENT_REPORT.equals(protoBuf.getObjType()))
        {
            ProtoTrafficIncidentReportResp resp = ProtoTrafficIncidentReportResp.parseFrom(protoBuf.getBufferData());
            this.status = resp.getStatus();
            errorMessage = resp.getErrorMessage();
        }

        return this.status;
    }

}
