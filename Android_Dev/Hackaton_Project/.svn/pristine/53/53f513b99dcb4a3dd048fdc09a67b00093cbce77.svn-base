package com.telenav.carconnect.provider;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.telenav.carconnect.CarConnectEvent;
import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.datatypes.address.Address;
import com.telenav.data.datatypes.address.Stop;
import com.telenav.data.datatypes.preference.Preference;
import com.telenav.data.serverproxy.AbstractServerProxy;
import com.telenav.data.serverproxy.IServerProxyListener;
import com.telenav.data.serverproxy.NetworkStatusManager;
import com.telenav.data.serverproxy.impl.INavigationProxy;
import com.telenav.data.serverproxy.impl.ServerProxyFactory;
import com.telenav.data.serverproxy.impl.navsdk.NavSdkNavigationProxy;
import com.telenav.datatypes.route.Route;
import com.telenav.location.TnLocation;
import com.telenav.logger.Logger;
import com.telenav.navsdk.events.NavigationData.EtaResult;
import com.telenav.navsdk.events.NavigationData.NavigationParameters;
import com.telenav.navsdk.events.NavigationEvents.EtaError;
import com.telenav.navsdk.events.NavigationEvents.EtaRequest;
import com.telenav.navsdk.events.NavigationEvents.EtaResponse;
import com.telenav.navsdk.events.PointOfInterestData.PointOfInterest;

public class CarConnectEtaProvider extends AbstractProvider implements IServerProxyListener
{
    private static final int DEFALT_ROUTE_TYPE = Route.ROUTE_FASTEST;

    @Override
    public void handle(String eventType, Object eventData)
    {
        if (eventType.equals(CarConnectEvent.ETA_REQUEST))
        {
            EtaRequest etaRequest = (EtaRequest) eventData;
            if (etaRequest == null || etaRequest.getNavCount() == 0)
            {
                sendErrorResponse();
                return;
            }
            int navCount = etaRequest.getNavCount();
            List<PointOfInterest> pois = new ArrayList<PointOfInterest>();
            boolean isFirstPoiIsOrigin = false;
            for (int index = 0; index < navCount; ++index)
            {
                try
                {
                    NavigationParameters navParameters = etaRequest.getNavList().get(index);
                    if (index == 0)
                        isFirstPoiIsOrigin = navParameters.getIsStaticNavigation();
                    if (!isFirstPoiIsOrigin)
                    {
                        pois.add(navParameters.getWaypoints(0));
                    }
                    else
                    {
                        pois.add(navParameters.getWaypoints(1));
                    }
                }
                catch (Exception e)
                {

                    sendErrorResponse();
                    return;
                }
            }
            if (pois.size() == 0)
            {
                sendErrorResponse();
                return;
            }

            LocationRequester lr = new LocationRequester(new EtaLC(pois));
            lr.requestLocation();
        }
    }

    private class EtaLC implements ILocationCallback
    {
        private List<PointOfInterest> dest;

        private EtaLC(List<PointOfInterest> dest)
        {
            this.dest = dest;
        }

        @Override
        public void onSuccess(TnLocation loc)
        {
            if (dest == null || dest.size() == 0)
            {
                sendErrorResponse();
                return;
            }
            NavSdkNavigationProxy navigationProxy = ServerProxyFactory.getInstance().createNavigationProxy(CarConnectEtaProvider.this);

            int size = dest.size();
            Address[] orgAddressArray = new Address[size]; 
            Address[] destAddressArray = new Address[size];
            for (int i = 0; i < size; ++i)
            {
                Address orgAddress = new Address();
                Stop orgStop = new Stop();
                orgStop.setLat(loc.getLatitude());
                orgStop.setLon(loc.getLongitude());
                orgAddress.setStop(orgStop);
                orgAddressArray[i] = orgAddress;

                PointOfInterest poi = dest.get(i);
                Address destAddress = new Address();
                Stop destStop = new Stop();
                destStop.setLat((int) (poi.getLocation().getLatitude() * ProtocolConvertor.LAT_AND_LON_CONVERT_RATE));
                destStop.setLon((int) (poi.getLocation().getLongitude() * ProtocolConvertor.LAT_AND_LON_CONVERT_RATE));
                destAddress.setStop(destStop);
                destAddressArray[i] = destAddress;
            }
            
            int routeType = ((DaoManager) DaoManager.getInstance()).getPreferenceDao().getIntValue(
                Preference.ID_PREFERENCE_ROUTETYPE);
            if (routeType < 0 || routeType == Route.ROUTE_PEDESTRIAN)
            {// for route pedestrian, it is hard to display
                routeType = Route.ROUTE_FASTEST;
            }
            int avoidSettings =  ((DaoManager) DaoManager.getInstance()).getPreferenceDao().getIntValue(
                Preference.ID_PREFERENCE_ROUTE_SETTING);
            boolean isOnboard = !NetworkStatusManager.getInstance().isConnected();
            if(isOnboard)
            {
                avoidSettings = -1;
            }
            int requestId = (int)(Math.random() * 10000);
            navigationProxy.requestEta(orgAddressArray, destAddressArray, routeType, avoidSettings, requestId);
        }

        @Override
        public void onError()
        {
            sendErrorResponse();
        }
    }

    @Override
    public void register()
    {
        getEventBus().subscribe(CarConnectEvent.ETA_REQUEST, this);

    }

    @Override
    public void unregister()
    {
        getEventBus().unsubscribe(CarConnectEvent.ETA_REQUEST, this);

    }

    @Override
    public void transactionFinished(AbstractServerProxy proxy, String jobId)
    {
        INavigationProxy navProxy = (INavigationProxy) proxy;
        Vector staticEta = navProxy.getStaticEta();
        Vector dynmicEta = navProxy.getDynmicEta();
        if (staticEta != null && dynmicEta != null)
        {
            EtaResponse.Builder etaResponseBuilder = EtaResponse.newBuilder();
            int size = staticEta.size();
            for (int index = 0; index < size; ++index)
            {
                long se = ((Long) staticEta.elementAt(index)).longValue();
                long de = ((Long) dynmicEta.elementAt(index)).longValue();

                EtaResult.Builder etaResultBuilder = EtaResult.newBuilder();
                etaResultBuilder.setDynamicEta(de);
                etaResultBuilder.setStaticEta(se);
                etaResponseBuilder.addEtaResults(etaResultBuilder);
            }
            EtaResponse resp = etaResponseBuilder.build();
            getEventBus().broadcast(CarConnectEvent.ETA_RESPONSE, resp);
        }
        else
        {
            sendErrorResponse();
        }
    }

    @Override
    public void updateTransactionStatus(AbstractServerProxy proxy, byte progress)
    {
        // Do nothing
    }

    @Override
    public void networkError(AbstractServerProxy proxy, byte statusCode, String jobId)
    {
        String errorMessage = proxy.getErrorMsg();

        Logger.log(Logger.INFO, this.getClass().getName(), "CarConnect: EtaProvider- network error code = " + statusCode
                + ", message = " + errorMessage);

        sendErrorResponse();

    }

    @Override
    public void transactionError(AbstractServerProxy proxy)
    {
        String errorMessage = proxy.getErrorMsg();

        Logger.log(Logger.INFO, this.getClass().getName(), "CarConnect: EtaProvider- transaction error - " + errorMessage);

        sendErrorResponse();
    }

    @Override
    public boolean isAllowNetworkRequest(AbstractServerProxy proxy)
    {
        // always allow network request
        return true;
    }

    private void sendErrorResponse()
    {
        EtaError.Builder errorBuilder = EtaError.newBuilder();
        getEventBus().broadcast(CarConnectEvent.ETA_ERROR, errorBuilder.build());
    }
}
