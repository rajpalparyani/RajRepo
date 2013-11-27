/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * ProtoBufConverter.java
 *
 */
package com.telenav.data.serverproxy.impl.protobuf.log;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import com.telenav.j2me.datatypes.ProtocolBuffer;

/**
 * @author chrbu (chrbu@telenav.cn)
 * @date Dec 30, 2011
 */
public class ProtoBufConverter
{
//    private static final String METHOD_NAME = "parseFrom";
//
//    private static final String CLASS_PACKAGE = "com.telenav.j2me.framework.protocol.";
//
//    private static final String COMMA = ",";
//
//    private static final int RESPONSEINDEX = 1;
//
//    private static final int REQUESTINDEX = 0;

    private static Map<String, String> converterMap = new HashMap<String, String>();
    static
    {
        converterMap.put("SyncResource", "ProtoSyncResourceReq,ProtoSyncResourceResp");
        converterMap.put("GetServiceLocator",
            "ProtoSyncServiceLocatorReq,ProtoSyncServiceLocatorResp");
        converterMap.put("Startup", "ProtoStartupReq,ProtoStartupResp");
        converterMap.put("LocationSearch",
            "ProtoLocationSearchReq,ProtoLocationSearchResp");
        converterMap.put("CacheCities", "ProtoCacheCitiesReq,ProtoCacheCitiesResp");
        converterMap.put("MISReporting", "ProtoMISReportingReq,ProtoMISReportingResp");
        converterMap.put("SyncServiceLocator",
            "ProtoSyncServiceLocatorReq,ProtoSyncServiceLocatorResp");
        converterMap.put("CombinationSyncResource",
            "ProtoCombinationSyncResourceReq,ProtoSyncResourceResp");
        converterMap.put("SyncClientSettingData",
            "ProtoSyncClientSettingDataReq,ProtoSyncClientSettingDataResp");
        converterMap.put("Map", "ProtoMapReq,ProtoMapResp");
        converterMap.put("TrafficMap", "ProtoTrafficTileReq,ProtoTrafficTileResp");
        converterMap.put("Dynamic_Route", "ProtoDynamicRouteReq,ProtoRoutesResp");
        converterMap.put("Get_Extra_Edge", "ProtoExtraEdgeReq,ProtoExtraEdgeResp");
        converterMap.put("Chunk_CheckSelected_Deviation",
            "ProtoCheckSelectedDeviationReq,ProtoMapTileResp");
        converterMap.put("Static_Route", "ProtoStaticRouteReq,ProtoRoutesResp");
        converterMap.put("Chunk_Selection_Route",
            "ProtoSelectionRouteReq,ProtoMapTileResp");
        converterMap.put("Decimated_Route",
            "ProtoDecimatedRouteReq,ProtoDecimatedRouteResp");
        converterMap.put("Decimated_MultiRoute",
            "ProtoDecimatedMultiRouteReq,ProtoDecimatedMultiRouteResp");
        converterMap.put("Traffic_Summary",
            "ProtoTrafficSummaryReq,ProtoTrafficSummaryResp");
        converterMap.put("Traffic_Selected_Reroute",
            "ProtoTrafficSelectedRerouteReq,ProtoRoutesResp");
        converterMap.put("Traffic_Avoid_Incidents_Reroute",
            "ProtoTrafficAvoidIncidentsRerouteReq,ProtoRoutesResp");
        converterMap.put("Traffic_Incident_Report",
            "ProtoTrafficIncidentReportReq,ProtoTrafficIncidentReportResp");
        converterMap.put("Traffic_Avoid_Selected_Seg_Deci_Reroute",
            "ProtoTrafficAvoidSelectedSegRerouteReq,ProtoDecimatedMultiRouteResp");
        converterMap.put("Traffic_Min_Delay_Deci_Reroute",
            "ProtoTrafficMinDelayRerouteReq,ProtoDecimatedMultiRouteResp");
        converterMap.put("Traffic_Static_Avoid_Selected_Seg_Deci_Reroute",
            "ProtoTrafficStaticAvoidSelectedSegRerouteReq,ProtoDecimatedMultiRouteResp");
        converterMap.put("Traffic_Static_Min_Delay_Deci_Reroute",
            "ProtoTrafficStaticMinDelayRerouteReq,ProtoDecimatedMultiRouteResp");
        converterMap.put("Reverse_Geocoding",
            "ProtoReverseGeocodingReq,ProtoReverseGeocodingResp");
        converterMap
                .put("Chunked_Dynamic_Route", "ProtoDynamicRouteReq,ProtoMapTileResp");
        converterMap.put("GET_ETA", "ProtoETAReq,ProtoETAResp");
        converterMap.put("SyncNewStops_Global", "ProtoSyncNewStopsReq,ProtoSyncNewStopsResp");
        converterMap.put("SyncPreference",
            "ProtoSyncPreferenceReq,ProtoSyncPreferenceResp");
        converterMap.put("ConfirmRecentStops",
            "ProtoConfirmRecentStopsReq,ProtoConfirmRecentStopsResp");
        converterMap.put("Logging", "ProtoLoggingReq,ProtoLoggingResp");
        converterMap.put("Feedback", "ProtoFeedbackReq,ProtoFeedbackResp");
        converterMap.put("FetchAllStops_Global", "ProtoFetchAllStopsReq,ProtoFetchAllStopsResp");
        converterMap.put("Dsm", "ProtoDsmReq,ProtoDsmResp");
        converterMap.put("Login", "ProtoLoginReq,ProtoLoginResp");
        converterMap.put("forgetPin", "ProtoForgetPinReq,ProtoForgetPinResp");
        converterMap.put("SyncPurchased", "ProtoSyncPurchasedReq,ProtoSyncPurchasedResp");
        converterMap.put("SendPTNVerificationCode", "ProtoSendPTNVerificationCodeReq,ProtoSendPTNVerificationCodeResp");
        converterMap.put("ValidateAddress",
            "ProtoValidateAddressReq,ProtoValidateAddressResp");
        converterMap.put("ShareAddress20", "ProtoOneBoxSearchReq,ProtoOneBoxSearchResp");
        converterMap.put("SearchPoi20", "ProtoPoiSearchReq,ProtoPoiSearchResp");
        converterMap.put("ValidateAirport20",
            "ProtoValidateAirportReq,ProtoValidateAirportResp");
        converterMap.put("bannerAds", "ProtoBannerAdsReq,ProtoBannerAdsResp");
        converterMap.put("SentAddress", "ProtoSentAddressReq,ProtoSentAddressResp");
        converterMap.put("BillBoardAds", "ProtoBillBoardAdsReq,ProtoBillBoardAdsResp");
        converterMap.put("Proto_Multi_Route", "ProtoMultiRouteReq,ProtoMultiRouteResp");
        converterMap.put("Proto_Route_Audio", "ProtoRouteAudio,ProtoRouteAudio");
        converterMap.put("Proto_MapTile", "ProtoMapTileReq,ProtoMapTileResp");
        converterMap.put("Proto_SPT", "ProtoSPT,ProtoSPT");
        converterMap.put("gps", "ProtoGpsList,ProtoGpsList");
        converterMap.put("ssoToken", "ProtoSsoToken,ProtoSsoToken");
        converterMap.put("profile", "ProtoUserProfile,");
        converterMap.put("I18NWeather", "ProtoWeatherReq,ProtoWeatherResp");
        converterMap.put("DeclinePurchase", "ProtoDeclinePurchaseReq,ProtoDeclinePurchaseResp");
        converterMap.put("MessageProcess", "ProtoMessageProcessReq,ProtoMessageProcessResp");
        converterMap.put("ClientLogging", "ProtoClientLoggingReq,ProtoClientLoggingResp");
    }

    public static void convertToString(Vector<ProtocolBuffer> list, boolean isRequest)
    {
//        if (Logger.DEBUG)
//        {
//            String className, networkCommuteStr;
//            Class clazz;
//            String[] networkCommutes;
//            ProtocolBuffer pb;
//            Object targetObject;
//            int size = list.size();
//            StringBuffer sb = new StringBuffer();
//            for (int start = 0; start < size; start++)
//            {
//                pb = list.get(start);
//                networkCommuteStr = converterMap.get(pb.getObjType());
//                if (null == networkCommuteStr)
//                {
//                    Logger.log(Logger.INFO, ProtoBufConverter.class.getName(),
//                        "protobuf : please add request and response mapping for: "
//                                + pb.getObjType());
//                    continue;
//                }
//                try
//                {
//                    networkCommutes = networkCommuteStr.split(COMMA);
//                    className = isRequest ? networkCommutes[REQUESTINDEX]
//                            : networkCommutes[RESPONSEINDEX];
//                    clazz = Class.forName(CLASS_PACKAGE + className);
//                    Method method = clazz.getMethod(METHOD_NAME, byte[].class);
//                    targetObject = method.invoke(clazz, pb.getBufferData());
//                    sb.append(ToStringUtils.toString(targetObject));
//                }
//                catch (Exception e)
//                {
//                    e.printStackTrace();
//                }
//            }
//            Logger.log(Logger.INFO, ProtoBufConverter.class.getName(),
//                "protobuf : " + sb);
//        }
    }

}
