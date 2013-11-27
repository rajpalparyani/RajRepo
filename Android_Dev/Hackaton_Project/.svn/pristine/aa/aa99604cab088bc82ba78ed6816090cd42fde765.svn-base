package com.telenav.carconnect.provider.tnlink.module.nav.routeplan;

import java.util.Vector;

import android.util.Log;

import com.telenav.app.CommManager;
import com.telenav.carconnect.provider.tnlink.module.AbstractBaseModel;
import com.telenav.carconnect.provider.tnlink.module.MyLog;
import com.telenav.carconnect.provider.tnlink.module.nav.ErrorCode;
import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.dao.misc.PreferenceDao;
import com.telenav.data.dao.serverproxy.AddressDao;
import com.telenav.data.datatypes.address.Address;
import com.telenav.data.datatypes.address.Stop;
import com.telenav.data.datatypes.preference.Preference;
import com.telenav.data.serializable.SerializableManager;
import com.telenav.data.serverproxy.AbstractServerProxy;
import com.telenav.data.serverproxy.IServerProxyConstants;
import com.telenav.data.serverproxy.impl.IMissingAudioProxy;
import com.telenav.data.serverproxy.impl.INavigationProxy;
import com.telenav.data.serverproxy.impl.ServerProxyFactory;
import com.telenav.datatypes.audio.AudioDataFactory;
import com.telenav.datatypes.audio.AudioDataNode;
import com.telenav.datatypes.route.Route;
import com.telenav.location.TnLocation;
import com.telenav.module.location.LocationProvider;
import com.telenav.module.media.MediaManager;
import com.telenav.module.nav.routeplanning.IRoutePlanningConstants;
import com.telenav.navsdk.events.NavigationData.RouteComputationMode;
import com.telenav.res.IAudioRes;
import com.telenav.res.IStringCommon;
import com.telenav.res.ResourceManager;


public class RoutePlanModel extends AbstractBaseModel implements IRoutePlanningConstants,
            IRoutePlanConstants
{
    private INavigationProxy navigationProxy;
	
	public RoutePlanModel() {
		IMissingAudioProxy missingAudioProxy = ServerProxyFactory.getInstance().createMissingAudioProxy(null, CommManager.getInstance().getComm(), null);
		navigationProxy = ServerProxyFactory.getInstance().createNavigationProxy(null, CommManager.getInstance().getComm(), this, missingAudioProxy);
	}
	
	private void requestRouteChoices() {
	    clearRouteChoices();
        LocationProvider.getInstance().cancelAllRequests();
        CommManager.getInstance().getComm().cancelJob(IServerProxyConstants.ACT_GET_ROUTE_CHOICES);
        
        TnLocation loc = LocationProvider.getInstance().getCurrentLocation(
            LocationProvider.TYPE_GPS | LocationProvider.TYPE_NETWORK);
        Address origAddr = (Address)(get(KEY_O_ADDRESS_ORI));
        Address destAddr = (Address)(get(KEY_O_ADDRESS_DEST));
            
        Stop origStop = new Stop();
//        Stop destStop = new Stop();
        
        if (null==origAddr)
        {
            origStop.setLat(loc.getLatitude());
            origStop.setLon(loc.getLongitude());
        }
        else
        {
            origStop = origAddr.getStop();
        }
        Stop destStop = destAddr.getStop();
        
        
		TnLocation[] origGpsData = getStopLocation(origStop);
        // only when origin stop is current location, origGpsData may be null.
		
        if(origGpsData == null || destStop == null || origStop == null)
        {
//            throw new IllegalArgumentException(getClass().toString() + ": can't get GPS or stop is null !!");
    		return;
        }
        
        int heading = origGpsData[0].getHeading();
        put(KEY_B_IS_REQUEST_CANCELLED, false);

        put(IRoutePlanConstants.KEY_O_DEST_STOP, destStop);
        
       int routeStyle = this.getInt(KEY_I_ROUTE_STYLE);
       int routeSetting = this.getInt(KEY_I_ROUTE_SETTINGS);
        
        MyLog.setLog("Route", "dest stop: " + destStop.getLat() + " " + destStop.getLon());
        navigationProxy.requestRouteChoices(destStop, routeStyle, routeSetting, false, origGpsData, origGpsData.length, heading);
        playNavAudio();
        storeAddress(destAddr);
	}

    private TnLocation[] getStopLocation(Stop stop) {
        if (stop == null)
        {
            return null;
        }
        TnLocation[] data = new TnLocation[3];
        for (int i = 0; i < 3; i++)
        {
            data[i] = new TnLocation("");
        }
        int count = 0;
        
        TnLocation loc = LocationProvider.getInstance().getCurrentLocation(LocationProvider.TYPE_GPS | LocationProvider.TYPE_NETWORK);
        if (stop.getType() == Stop.STOP_CURRENT_LOCATION)
        {
            Object obj = loc;
            if (obj != null)
            {
                data[0] = (TnLocation) obj;
                count = 1;
            }
        }
        else
        {
            count = 1;
            data[0].setTime(System.currentTimeMillis());
            data[0].setLatitude(stop.getLat());
            data[0].setLongitude(stop.getLon());
            data[0].setSpeed(0);
            data[0].setHeading(0);
            data[0].setAltitude(5);
            data[0].setAccuracy(0);
            data[0].setValid(true);
        }

        if (count == 0)
        {
            return null;
        }
        else if (count < data.length)
        {
            // just copy to make fixes the same
            data[2] = data[1] = data[0];
        }
        return data;
    }
    
    protected void transactionFinishedDelegate(AbstractServerProxy proxy, String jobId)
    {
        if (proxy instanceof INavigationProxy)
        {

            INavigationProxy navProxy = (INavigationProxy) proxy;
            String action = proxy.getRequestAction();
            if (action.equals(IServerProxyConstants.ACT_GET_ROUTE_CHOICES))
            {
                Route[] choices = navProxy.getChoices();
                if (null==choices)
                	return;
                
                put(KEY_A_ROUTE_CHOICES, choices);

                int[] etas = navProxy.getRouteChoiceETA();
                put(KEY_A_ROUTE_CHOICES_ETA, etas);

                int[] trafficDelays = navProxy.getRouteChoicesTrafficDelay();
                put(KEY_A_ROUTE_CHOICES_TRAFFIC_DELAY, trafficDelays);

                int[] lengths = navProxy.getRouteChoicesLength();
                put(KEY_A_ROUTE_CHOICES_LENGTH, lengths);

                put(KEY_I_SELECTED_PLAN, 0);
                put(KEY_B_IS_UPDATE_SHOW_ROUTE, true);
                this.controller.postModelEvent(EVENT_MODEL_RESULT);
                
            }
        }
    }
    
    public void transactionError(AbstractServerProxy proxy)
    {
        String errorMessage = proxy.getErrorMsg();
        if (errorMessage == null || errorMessage.length() == 0)
        {
            errorMessage = ResourceManager.getInstance().getCurrentBundle().getString(IStringCommon.RES_SERVER_ERROR, IStringCommon.FAMILY_COMMON);
        }
        
        if (errorMessage.equals("Your origin and destination are too close."))
        {
            put(KEY_I_ERROR_CODE,ErrorCode.NavigationError_DestinationTooClose);
        }
        else
        {
            put(KEY_I_ERROR_CODE, ErrorCode.NavigationError_RouteGenerationFailed);
        }
        
        MyLog.setLog("error", "route error: " + errorMessage);
        put(KEY_S_ERROR_MSG, errorMessage);
        controller.postModelEvent(EVENT_MODEL_RESULT_ERROR);
    }
   
    
    private boolean isCurrentLocationExist()
    {
        Address address = (Address) get(KEY_O_ADDRESS_ORI);
        Stop stop = address.getStop();
        if (stop != null && stop.getType() == Stop.STOP_CURRENT_LOCATION)
        {
            return true;
        }
        address = (Address) get(KEY_O_ADDRESS_DEST);
        stop = address.getStop();
        if (stop != null && stop.getType() == Stop.STOP_CURRENT_LOCATION)
        {
            return true;
        }
        return false;
    }
    
    protected void addToRecent(Address address)
    {
        if (address != null)
        {
            Stop stop = address.getStop();
            if (stop != null && stop.getType() == Stop.STOP_CURRENT_LOCATION)
            {
                return;
            }
            byte[] data = SerializableManager.getInstance().getAddressSerializable().toBytes(address);
            Address newAddress = SerializableManager.getInstance().getAddressSerializable().createAddress(data);
            newAddress.setSource(Address.SOURCE_RECENT_PLACES);
            if (newAddress.getPoi() != null)
            {
                newAddress.setType(Address.TYPE_RECENT_POI);
            }
            else
            {
                newAddress.setType(Address.TYPE_RECENT_STOP);
            }
            AddressDao addressDao = DaoManager.getInstance().getAddressDao();
            if (addressDao != null)
            {
                newAddress.setCreateTime(System.currentTimeMillis());
                addressDao.addAddress(newAddress, true);
                addressDao.store();
            }
        }
    }
    
    protected void playNavAudio()
    {
        int routeStyle = this.getInt(KEY_I_ROUTE_STYLE);
        
        int audioId = 0;
        switch(routeStyle)
        {
            case Route.ROUTE_FASTEST:
            {
                audioId = IAudioRes.STATIC_AUDIO_ROUTESTYLE_FASTEST;
                break;
            }
            case Route.ROUTE_SHORTEST:
            {
                audioId = IAudioRes.STATIC_AUDIO_ROUTESTYLE_SHORTEST;
                break;
            }
            case Route.ROUTE_PEDESTRIAN:
            {
                audioId = IAudioRes.STATIC_AUDIO_ROUTESTYLE_PEDESTRIAN;
                break;
            }
            case Route.ROUTE_AVOID_HIGHWAY:
            {
                audioId = IAudioRes.STATIC_AUDIO_ROUTESTYLE_STREET;
                break;  
            }
            default:
            {
                audioId = IAudioRes.STATIC_AUDIO_ROUTESTYLE_FASTEST;
                break;
            }
        }

        Vector audioList=new Vector();
        
        
        AudioDataNode styleAudio = AudioDataFactory.getInstance().createAudioDataNode(
            AudioDataFactory.getInstance().createAudioData(audioId));
        audioList.insertElementAt(styleAudio, 0);
        
        AudioDataNode gettingAudio = AudioDataFactory.getInstance().createAudioDataNode(
            AudioDataFactory.getInstance().createAudioData(IAudioRes.STATIC_AUDIO_GETTING));
        audioList.insertElementAt(gettingAudio, 0);
        
        MediaManager.getInstance().playAudio(audioList);
    }
    
    public void networkError(AbstractServerProxy proxy, byte statusCode)
    {
        String errorMessage = proxy.getErrorMsg();
        if (errorMessage == null || errorMessage.length() == 0)
        {
            errorMessage = ResourceManager.getInstance().getCurrentBundle().getString(IStringCommon.RES_SERVER_ERROR, IStringCommon.FAMILY_COMMON);
        }
        
        MyLog.setLog("error", "route error: " + errorMessage);
        put(KEY_I_ERROR_CODE, ErrorCode.NavigationError_ServiceCommunicationProblem);
        put(KEY_S_ERROR_MSG, errorMessage);
        controller.postModelEvent(EVENT_MODEL_RESULT_ERROR);
    }
    
    private void clearRouteChoices()
    {
        this.remove(KEY_A_ROUTE_CHOICES);
        this.remove(KEY_A_ROUTE_CHOICES_ETA);
        this.remove(KEY_A_ROUTE_CHOICES_LENGTH);
        this.remove(KEY_A_ROUTE_CHOICES_TRAFFIC_DELAY);
        this.put(KEY_I_SELECTED_PLAN, 0);
    }
    
    protected void cancelCurrentRequest()
    {
        put(KEY_B_IS_REQUEST_CANCELLED, true);
        clearRouteChoices();
        LocationProvider.getInstance().cancelAllRequests();
        CommManager.getInstance().getComm().cancelJob(IServerProxyConstants.ACT_GET_ROUTE_CHOICES);
    }
    
    @Override
    protected void handleEvent(int actionId)
    {
        // TODO Auto-generated method stub
        cancelCurrentRequest();
        requestRouteChoices();
    }

    @Override
    protected void doActionDelegate(int actionId)
    {
        // TODO Auto-generated method stub
        
    }
    
    protected void storeAddress(Address destAddr)
    {
        if (destAddr!=null)
            addToRecent(destAddr);
    }

}
