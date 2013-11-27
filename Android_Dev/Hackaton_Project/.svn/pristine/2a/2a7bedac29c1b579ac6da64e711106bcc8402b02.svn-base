/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * DsrModel.java
 *
 */
package com.telenav.module.dsr;

import java.util.Vector;

import com.telenav.app.ThreadManager;
import com.telenav.audio.IPlayerListener;
import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.dao.misc.PreferenceDao;
import com.telenav.data.dao.serverproxy.AbstractDaoManager;
import com.telenav.data.dao.serverproxy.DsrServerDrivenParamsDao;
import com.telenav.data.dao.serverproxy.MandatoryNodeDao;
import com.telenav.data.dao.serverproxy.ServerDrivenParamsDao;
import com.telenav.data.datatypes.address.Address;
import com.telenav.data.datatypes.address.Stop;
import com.telenav.data.datatypes.mandatory.MandatoryProfile;
import com.telenav.data.datatypes.preference.Preference;
import com.telenav.data.serializable.SerializableManager;
import com.telenav.data.serverproxy.AbstractServerProxy;
import com.telenav.data.serverproxy.IUserProfileProvider;
import com.telenav.data.serverproxy.NetworkStatusManager;
import com.telenav.data.serverproxy.impl.IDsrStreamProxy;
import com.telenav.data.serverproxy.impl.IPoiSearchProxy;
import com.telenav.data.serverproxy.impl.ServerProxyFactory;
import com.telenav.datatypes.audio.AudioDataFactory;
import com.telenav.datatypes.audio.AudioDataNode;
import com.telenav.datatypes.nav.NavState;
import com.telenav.datatypes.route.Route;
import com.telenav.dsr.DsrManager;
import com.telenav.dsr.IRecognizeListener;
import com.telenav.dsr.IRecordEventListener;
import com.telenav.location.TnLocation;
import com.telenav.log.mis.IMisLogConstants;
import com.telenav.log.mis.MisLogManager;
import com.telenav.log.mis.helper.PoiMisLogHelper;
import com.telenav.log.mis.log.AppStatusMisLog;
import com.telenav.media.TnMediaManager;
import com.telenav.module.location.LocationProvider;
import com.telenav.module.media.IAudioConstants;
import com.telenav.module.media.MediaManager;
import com.telenav.module.nav.NavSdkNavEngine;
import com.telenav.module.poi.PoiDataListener;
import com.telenav.module.poi.PoiDataRequester;
import com.telenav.module.poi.PoiDataWrapper;
import com.telenav.module.poi.result.PoiResultHelper;
import com.telenav.mvc.AbstractCommonNetworkModel;
import com.telenav.mvc.ICommonConstants;
import com.telenav.res.IAudioRes;
import com.telenav.res.IStringCommon;
import com.telenav.res.IStringDsr;
import com.telenav.res.IStringLogin;
import com.telenav.res.IStringPoi;
import com.telenav.res.ResourceManager;
import com.telenav.res.converter.StringConverter;
import com.telenav.telephony.TnTelephonyManager;

/**
 *@author bduan
 *@date Aug 23, 2010
 */
public class DsrModel extends AbstractCommonNetworkModel implements IDsrConstants, IRecognizeListener, PoiDataListener, IPlayerListener
{
    protected int errorTimes = 0;
    protected boolean isThinking = false;
    protected boolean isCancel = false;
    protected String dsrType = "";
    
    protected void doActionDelegate(int actionId)
    {
        switch (actionId)
        {
            case ACTION_CHECK_NETWORK:
            {
                if (!NetworkStatusManager.getInstance().isConnected())
                {
                    String errorMessage = ResourceManager.getInstance().getCurrentBundle()
                            .getString(IStringLogin.RES_LABEL_SYNC_SERVICE_LOCATOR_ERROR, IStringLogin.FAMILY_LOGIN);
                    this.postErrorMessage(errorMessage);
                }
                else
                {
                    this.postModelEvent(EVENT_MODEL_DO_INIT);
                }
                break;
            }
            case ACTION_INIT:
            {
                isCancel = false;
                
                int format;
                
                ServerDrivenParamsDao serverDrivenParamsDao = DaoManager.getInstance().getDsrServerDrivenParamsDao();
                String recordFormat = serverDrivenParamsDao.getValue(DsrServerDrivenParamsDao.DSR_RECORD_FORMAT);
                if ("AMR".equalsIgnoreCase(recordFormat))
                {
                    format = IAudioConstants.RECORD_FORMAT_AMR;
                    MediaManager.getInstance().audioRecorderFormat = TnMediaManager.FORMAT_AMR;
                    MediaManager.getInstance().getRecordPlayer().setAudioFormat(TnMediaManager.FORMAT_AMR);
                }
                else
                {
                    format = IAudioConstants.RECORD_FORMAT_PCM;
                    MediaManager.getInstance().audioRecorderFormat = TnMediaManager.FORMAT_PCM;
                    MediaManager.getInstance().getRecordPlayer().setAudioFormat(TnMediaManager.FORMAT_PCM);                    
                }
                
                initDsrManger(format);
                setRouteRequestBy();
                break;
            }
            case ACTION_MANNUAL_STOP:
            {
                isThinking = true;
                Vector listeners = DsrManager.getInstance().getRecordStatusListeners();
                for (int i = 0; i < listeners.size(); i++)
                {
                    IRecordEventListener listener = (IRecordEventListener) listeners.elementAt(i);
                    listener.recordStatusUpdate(IRecordEventListener.EVENT_TYPE_SPEECH_MANNUAL_STOP, null);
                }
                this.put(KEY_B_IS_DO_RECOGNIZE, true);
                break;
            }
            case ACTION_AUTO_STOP:
            {
                isThinking = true;
                this.put(KEY_B_IS_DO_RECOGNIZE, true);
                break;
            }
            case ACTION_PREPARE_AUDIO:
            {
                //stop current audio play
                MediaManager.getInstance().stopPlayAudio();
                
                Address address = (Address)this.get(KEY_O_SELECTED_ADDRESS);
                Vector matchAddresses = this.getVector(KEY_V_ALTERNATIVE_ADDRESSES);
                int index = 0;
                if(matchAddresses != null)
                {
                    index = matchAddresses.indexOf(address);
                }
                
                Vector audioList = this.getVector(KEY_V_MULTI_MATCH_AUDIOS);
                
                if(audioList != null && index >= 0 && index < audioList.size())
                {
                    Vector currentAudio = (Vector)audioList.elementAt(index);
                    this.put(KEY_V_STOP_AUDIOS, currentAudio);
                }
                
                break;
            }
            case ACTION_PLAY_ERROR_AUDIO:
            {
                playErrorAudio();
                break;
            }
            case ACTION_DO_SEARCH:
            {
                doCategorySearch();
                break;
            }
//            case ACTION_SEND_DATA:
//            {
//                sendMockData();
//                break;
//            }
            default:    
                break;
        }
    }

    private void setRouteRequestBy()
    {
        AppStatusMisLog appStatusMisLog = (AppStatusMisLog) MisLogManager.getInstance().getMisLog(
            IMisLogConstants.TYPE_INNER_APP_STATUS);
        appStatusMisLog.setRouteRequestBy(IMisLogConstants.VALUE_ADDRESS_INPUT_TYPE_SPEAK_IN);
    }
    
//    protected void sendMockData()
//    {
//        int resId = this.getInt(KEY_I_RES_ID);
//        IRecordDataOutputStream recordOutputStream = new TestDsrOutputStream(resId);
//        int format = IAudioConstants.RECORD_FORMAT_WAV;// for wav
//        
//        initDsrManger(format, recordOutputStream);
//    }

    protected void releaseDelegate()
    {
        DsrManager.getInstance().cancel();
    }

    protected void initDsrManger(int format)
    {
        DsrManager.getInstance().init(ThreadManager.getPool(ThreadManager.TYPE_APP_ACTION));

        String dsrType = getDsrType();
        IUserProfileProvider userProfileProvider = (IUserProfileProvider)get(KEY_O_USER_PROFILE_PROVIDER);
        IDsrStreamProxy dsrStreamProxy = ServerProxyFactory.getInstance().createDsrStreamProxy(null,
            DsrManager.getInstance().getDsrComm(), dsrType, this, userProfileProvider);
        dsrStreamProxy.setAnchorStop(getStop());
        dsrStreamProxy.setAudioFormat(format);

        TnTelephonyManager.getInstance().vibrate(200);
        DsrManager.getInstance().start(dsrType, format, dsrStreamProxy, 10000, 30000, false, this);
    }
    
    protected String getDsrType()
    {
        int acType = this.getInt(KEY_I_AC_TYPE);
        
        initSearchParameter(acType);
        
        dsrType = IAudioConstants.DSR_RECOGNIZE_COMMAND_CONTROL;
        
        if (acType == TYPE_AC_FROM_NAV /*|| acType == TYPE_AC_FROM_TURN_MAP*/ || -1 != this.getInt(KEY_I_SEARCH_ALONG_TYPE))
        {
            //Just let it keep searching poi, but block other cmd return.
            /**
             * TN-2758 bug, we need up ahead audio when dsr in nav. dsrType is only one variable to tell Dsr Server
             * to response the up ahead audio. So I uncomment it.
             */
            dsrType = IAudioConstants.DSR_RECOGNIZE_POI;
        }
        //fix bug TN-3649, we need to send COMMAND_CONTROL when doing dsr in main screen
//        else if(acType == TYPE_AC_FROM_AC_MAIN)
//        {
//            dsrType = IAudioConstants.DSR_RECOGNIZE_ONE_SHOT_ADDRESS;
//        }
        
        return dsrType;
    }

    private void initSearchParameter(int acType)
    {
        if(acType == TYPE_AC_FROM_NAV/* || acType == TYPE_AC_FROM_TURN_MAP*/)
        {
            int searchType = IPoiSearchProxy.TYPE_SEARCH_ALONG_ROUTE;
            int searchAlongType = IPoiSearchProxy.TYPE_SEARCH_ALONG_UPHEAD;
            
            this.put(ICommonConstants.KEY_I_SEARCH_TYPE, searchType);
            this.put(ICommonConstants.KEY_I_SEARCH_ALONG_TYPE, searchAlongType);
            
            this.put(KEY_O_NAVSTATE, NavSdkNavEngine.getInstance().getCurrentNavState());
        }
    }

    protected void deactivateDelegate()
    {
        clear();
    }

    protected void clear()
    {
        isCancel = true;
        MediaManager.getInstance().getRecordPlayer().cancel();
    }

    public byte[] getMadantoryData()
    {
        MandatoryNodeDao mandatoryNodeDao = DaoManager.getInstance().getMandatoryNodeDao();
        MandatoryProfile profile = mandatoryNodeDao.getMandatoryNode();
        return SerializableManager.getInstance().getMandatorySerializable().toBytes(profile);
    }

    protected void doCategorySearch()
    {
        DsrManager.getInstance().cancel();
        
        put(KEY_I_AC_TYPE, TYPE_AC_FROM_DSR);
        String searchText = getString(KEY_S_COMMON_SEARCH_TEXT);
        
        String showText =  getString(KEY_S_COMMON_SHOW_SEARCH_TEXT);
        if(showText == null)
        {
            showText = getString(KEY_S_COMMON_SEARCH_TEXT);
            put(KEY_S_COMMON_SHOW_SEARCH_TEXT, showText);
        }
        
        Address anchorAddress = (Address)get(KEY_O_SELECTED_ADDRESS);
        
        int categoryId = this.getInt(KEY_I_CATEGORY_ID);
        int searchType = IPoiSearchProxy.TYPE_SEARCH_ADDRESS;
        if (-1 != this.getInt(KEY_I_SEARCH_TYPE))
        {
            searchType = this.getInt(KEY_I_SEARCH_TYPE);
        }
        
        int searchFromType = IPoiSearchProxy.TYPE_SEARCH_FROM_SPEAKIN;
        int sortType = PoiResultHelper.getDefaultSortType(categoryId);
        this.put(KEY_I_SEARCH_SORT_TYPE, sortType);
        
        int pageNumber = 0;
        int pageSize = MAX_NUM_PER_PAGE;
        
        Stop anchorStop = null;
        NavState navState = null;
        int searchAlongRouteType = -1;
        if (this.get(KEY_O_ADDRESS_ORI) instanceof Address)
        {
            Address oriAddr = (Address)this.get(KEY_O_ADDRESS_ORI);
            anchorStop = oriAddr.getStop();
        }
        else if(anchorAddress != null)
        {
            anchorStop = anchorAddress.getStop();
        }
        else
        {
            anchorStop = this.getAnchor();
        }
        
        if (searchType == IPoiSearchProxy.TYPE_SEARCH_ALONG_ROUTE)
        {
            searchFromType = IPoiSearchProxy.TYPE_SEARCH_FROM_SPEAKIN_ALONG;
            if (this.get(KEY_O_NAVSTATE) instanceof NavState)
            {
                navState =  (NavState)this.get(KEY_O_NAVSTATE);
                searchAlongRouteType = IPoiSearchProxy.TYPE_SEARCH_ALONG_UPHEAD;
                if(-1 != this.getInt(KEY_I_SEARCH_ALONG_TYPE))
                {
                    searchAlongRouteType = this.getInt(KEY_I_SEARCH_ALONG_TYPE);
                }
            }
            else
            {
                String errorMessage = ResourceManager.getInstance().getCurrentBundle().getString(
                    IStringPoi.MSG_SEARCH_PARAM_ERR, IStringPoi.FAMILY_POI);
                postErrorMessage(errorMessage);
                return;
            }
        }
        Stop destStop = getDestStop();
        PoiDataWrapper dataWrapper = new PoiDataWrapper(System.currentTimeMillis() + "");
        dataWrapper.setSearchArgs(0, searchType, searchFromType, searchAlongRouteType, sortType, categoryId,
            pageNumber, pageSize, searchText, showText, anchorStop, destStop, navState);
        IUserProfileProvider userProfileProvider = (IUserProfileProvider)get(KEY_O_USER_PROFILE_PROVIDER);
        PoiDataRequester poiDataRequester = new PoiDataRequester(userProfileProvider);
        poiDataRequester.doRequestPoi(dataWrapper, this);
    }

    protected Stop getDestStop()
    {
        Stop destStop = null;
        Address destAddr = (Address) this.get(KEY_O_ADDRESS_DEST);
        if (destAddr != null)
            destStop = destAddr.getStop();
        return destStop;
    }

    protected Stop getAnchor()
    {
        Stop anchor = null;
        // 1. Get precisely gps fix
        TnLocation location = LocationProvider.getInstance().getCurrentLocation(
            LocationProvider.TYPE_GPS);
        // 2. Get network fix
        if (location == null)
        {
            location = LocationProvider.getInstance().getCurrentLocation(
                LocationProvider.TYPE_NETWORK);
        }

        if (location == null)
        {
            // 3. Get last know gps fix
            location = LocationProvider.getInstance().getLastKnownLocation(
                LocationProvider.TYPE_GPS);
            // 4. Get last know network fix
            if (location == null)
            {
                location = LocationProvider.getInstance().getLastKnownLocation(
                    LocationProvider.TYPE_NETWORK);
            }
            // check last know
            if (location != null)
            {
                anchor = new Stop();
                anchor.setType(Stop.STOP_CURRENT_LOCATION);
                anchor.setLat(location.getLatitude());
                anchor.setLon(location.getLongitude());
            }
        }
        else
        {
            anchor = new Stop();
            anchor.setType(Stop.STOP_CURRENT_LOCATION);
            anchor.setLat(location.getLatitude());
            anchor.setLon(location.getLongitude());
        }

        if(anchor == null)
        {
            //TODO: DB, do we need popup error msg when there's no gps.
            TnLocation defaultLocation = LocationProvider.getInstance().getDefaultLocation();
            anchor = new Stop();
            anchor.setType(Stop.STOP_CURRENT_LOCATION);
            anchor.setLat(defaultLocation.getLatitude());
            anchor.setLon(defaultLocation.getLongitude());
        }
        
        return anchor;
    }

    

    public void poiResultUpdate(int resultValue, int resultType, String errorMsg, PoiDataWrapper poiDataWrapper)
    {
        String errorMessage = errorMsg;
        switch(resultValue)
        {
            case PoiDataRequester.TYPE_NETWORK_ERROR:
            {
                
                if (errorMessage == null || errorMessage.length() == 0)
                {
                    errorMessage = ResourceManager.getInstance().getCurrentBundle().getString(IStringCommon.RES_SERVER_ERROR, IStringCommon.FAMILY_COMMON);
                }
                
                postErrorMessage(errorMessage);
                break;
            }
            case PoiDataRequester.TYPE_SUCCESS_NO_RESULTS:
            {
                errorMessage = ResourceManager.getInstance()
                .getCurrentBundle().getString(IStringPoi.LABEL_NO_POIS,
                    IStringPoi.FAMILY_POI);
                String name =  getString(KEY_S_COMMON_SHOW_SEARCH_TEXT);
                if(name == null)
                {
                    name = getString(KEY_S_COMMON_SEARCH_TEXT);
                    put(KEY_S_COMMON_SHOW_SEARCH_TEXT, name);
                }
                StringConverter converter = ResourceManager.getInstance().getStringConverter();
                errorMessage = converter.convert(errorMessage, new String[]{name});
                postErrorMessage(errorMessage);
                break;
            }
            case PoiDataRequester.TYPE_BAD_ARGS:
            {
                errorMessage = ResourceManager.getInstance()
                .getCurrentBundle().getString(IStringPoi.LABEL_NO_POIS,
                    IStringPoi.FAMILY_POI);
                String name =  getString(KEY_S_COMMON_SHOW_SEARCH_TEXT);
                if(name == null)
                {
                    name = getString(KEY_S_COMMON_SEARCH_TEXT);
                    put(KEY_S_COMMON_SHOW_SEARCH_TEXT, name);
                }
                StringConverter converter = ResourceManager.getInstance().getStringConverter();
                errorMessage = converter.convert(errorMessage, new String[]{name});
                postErrorMessage(errorMessage);
                break;
            }
            case PoiDataRequester.TYPE_SUCCESS:
            {
                this.put(KEY_O_POI_DATA_WRAPPER, poiDataWrapper);
                
                if(this.fetchBool(KEY_B_IS_MAP_POI))
                {
                	PoiMisLogHelper.getInstance().setIsMapResulstMode(true);
                	postModelEvent(EVENT_MODEL_MAP_POI_LIST);
                }
                else
                {
                	postModelEvent(EVENT_MODEL_GOTO_POI_LIST);
                }
                break;
            }
        }
    }

    
    protected Stop getStop()
    {
        TnLocation location = LocationProvider.getInstance().getLastKnownLocation(LocationProvider.TYPE_GPS | LocationProvider.TYPE_NETWORK);
        if(location == null)
            location = LocationProvider.getInstance().getDefaultLocation();
        
        Stop stop = new Stop();
        stop.setLat(location.getLatitude());
        stop.setLon(location.getLongitude());
        stop.setType(Stop.STOP_CURRENT_LOCATION);
        return stop;
    }
    
    protected void transactionFinishedDelegate(AbstractServerProxy proxy , String jobId)
    {
        
    }

    protected void handleCommand(String cmd, int commandType, String[] messages, Vector audioList)
    {
        this.put(KEY_V_STOP_AUDIOS, audioList);
        if(this.getInt(KEY_I_AC_TYPE) == TYPE_AC_FROM_NAV || this.getInt(KEY_I_AC_TYPE) == TYPE_AC_FROM_TURN_MAP)
        {
            if(!( cmd.equals(IAudioConstants.DSR_COMMAND_TEXT_SEARCH) || dsrType.equals(IAudioConstants.DSR_RECOGNIZE_POI )) )
            {
                handleError(false);
                return;
            }
        }
        
        
        String searchText = null;
        if(messages != null && messages.length > 0)
        {
            searchText = messages[0];
        }
        if(searchText != null && searchText.trim().length() > 0)
        {
            searchText = searchText.substring(0, 1).toUpperCase() + searchText.substring(1);
        }
        if(cmd == null || cmd.length() == 0)
        {
            if (dsrType.equals(IAudioConstants.DSR_RECOGNIZE_POI))
            {
                MediaManager.getInstance().playAudio(audioList);
                this.put(KEY_S_COMMON_SEARCH_TEXT, searchText);
                
                this.postModelEvent(EVENT_MODEL_GO_TO_SEARCH);
            }
            else if (dsrType.equals(IAudioConstants.DSR_RECOGNIZE_ONE_SHOT_ADDRESS))
            {                
                this.postModelEvent(EVENT_MODEL_ADDRESS_GOT);
            }
            else
            {
                handleError(false);
            }
        }
        else if(cmd.equals(IAudioConstants.DSR_COMMAND_TEXT_MAPIT))
        {
        	if(audioList == null)
        		audioList = new Vector();
			if (commandType == IAudioConstants.CMD_TYPE_MAP_POI) 
			{
				this.put(KEY_B_IS_MAP_POI, true);
				this.put(KEY_S_COMMON_SEARCH_TEXT, searchText);
				
				this.postModelEvent(EVENT_MODEL_GO_TO_SEARCH);
			} 
			else
			{
			    if(commandType == IAudioConstants.CMD_TYPE_LOCATE_MY_HOME)
	            {
	                Stop homeStop = AbstractDaoManager.getInstance().getAddressDao().getHomeAddress();
	                
	                if(homeStop == null)
	                {
	                    this.postModelEvent(EVENT_MODEL_SET_HOME);
	                    return;
	                }
	                else
	                {
	                    Address address = new Address(Address.SOURCE_PREDEFINED);
	                    address.setStop(homeStop);
	                    this.put(KEY_O_SELECTED_ADDRESS, address);
	                }
	            }
	            else if (commandType == IAudioConstants.CMD_TYPE_LOCATE_MY_OFFICE)
	            {
	                Stop officeStop = AbstractDaoManager.getInstance().getAddressDao().getOfficeAddress();
	                
	                if(officeStop == null)
	                {
	                    this.postModelEvent(EVENT_MODEL_SET_OFFICE);
	                    return;
	                }
	                else
	                {
	                    Address address = new Address(Address.SOURCE_PREDEFINED);
	                    address.setStop(officeStop);
	                    this.put(KEY_O_SELECTED_ADDRESS, address);
	                }
	            } 
                if (audioList.size() == 0)
                {
                    AudioDataNode currentAudio = AudioDataFactory.getInstance().createAudioDataNode(
                            AudioDataFactory.getInstance().createAudioData(IAudioRes.STATIC_AUDIO_CURRENT_LOCATION));
                    audioList.insertElementAt(currentAudio, 0);
                }
                AudioDataNode mapAudio = AudioDataFactory.getInstance().createAudioDataNode(
                        AudioDataFactory.getInstance().createAudioData(IAudioRes.STATIC_AUDIO_GETTING_MAP));
                audioList.insertElementAt(mapAudio, 0);
                
                this.postModelEvent(EVENT_MODEL_GO_TO_MAP);
			}
			            
            if(audioList != null && audioList.size() > 0)
                MediaManager.getInstance().playAudio(audioList);
        }
        else if(cmd.equals(IAudioConstants.DSR_COMMAND_TEXT_COMMUTE))
        {
            //TODO: integrate with browser.
        }
        else if(cmd.equals(IAudioConstants.DSR_COMMAND_TEXT_CURRENTLOCATION))
        {
            this.postModelEvent(EVENT_MODEL_GO_TO_MAP);
        }
        else if(cmd.equals(IAudioConstants.DSR_COMMAND_TEXT_DRIVETO))
        {
            if(commandType == IAudioConstants.CMD_TYPE_LOCATE_MY_HOME)
            {
                Stop homeStop = AbstractDaoManager.getInstance().getAddressDao().getHomeAddress();
                if(homeStop == null)
                {
                    this.postModelEvent(EVENT_MODEL_SET_HOME);
                }
                else
                {
                    Address address = new Address(Address.SOURCE_PREDEFINED);
                    address.setStop(homeStop);
                    this.put(KEY_O_SELECTED_ADDRESS, address);
                    
                    this.postModelEvent(EVENT_MODEL_GO_TO_NAV);
                }
                return;
            }
            else if (commandType == IAudioConstants.CMD_TYPE_LOCATE_MY_OFFICE)
            {
                Stop officeStop = AbstractDaoManager.getInstance().getAddressDao().getOfficeAddress();
                if(officeStop == null)
                {
                    this.postModelEvent(EVENT_MODEL_SET_OFFICE);
                }
                else
                {
                    Address address = new Address(Address.SOURCE_PREDEFINED);
                    address.setStop(officeStop);
                    this.put(KEY_O_SELECTED_ADDRESS, address);
                    
                    this.postModelEvent(EVENT_MODEL_GO_TO_NAV);
                }
                return;
            }
            else if (commandType == -1) //just go to drive to.
            {
                this.postModelEvent(EVENT_MODEL_GO_TO_AC);
                return;
            }
            else
            {
                //Fix bug TN-1725.
                if(this.get(KEY_O_SELECTED_ADDRESS) == null)
                {
                    this.postModelEvent(EVENT_MODEL_GO_TO_AC);
                }
                else
                {
                    this.postModelEvent(EVENT_MODEL_GO_TO_NAV);
                }
                return;
            }
        }
        else if(cmd.equals(IAudioConstants.DSR_COMMAND_TEXT_MOVIE))
        {
            //TODO: integrate with browser.
        }
        else if(cmd.equals(IAudioConstants.DSR_COMMAND_TEXT_RESUME))
        {
            //if no trip, just go to drive to.
            if(DaoManager.getInstance().getTripsDao().getLastTrip() == null)
            {
                handleError(false);
//                this.postModelEvent(EVENT_MODEL_GO_TO_AC);
            }
            else
            {
                this.postModelEvent(EVENT_MODEL_RESUME_TRIP);
            }
        }
        else if(cmd.equals(IAudioConstants.DSR_COMMAND_TEXT_SEARCH))
        {
            MediaManager.getInstance().playAudio(audioList);
            if (searchText != null)
            {
                this.put(KEY_S_COMMON_SEARCH_TEXT, searchText);
                
                DaoManager.getInstance().getAddressDao().addRecentSearch(searchText);
                DaoManager.getInstance().getAddressDao().store();
            }
            
            this.postModelEvent(EVENT_MODEL_GO_TO_SEARCH);
        }
        else if(cmd.equals(IAudioConstants.DSR_COMMAND_TEXT_SHOW))
        {
            this.postModelEvent(EVENT_MODEL_GO_TO_MAP);
        }
        else if(cmd.equals(IAudioConstants.DSR_COMMAND_TEXT_THEATER))
        {
            //TODO: integrate with browser.
        }
        else if(cmd.equals(IAudioConstants.DSR_COMMAND_TEXT_TRAFFIC))
        {
            this.postModelEvent(EVENT_MODEL_GO_TO_MAP);
        }
        else if(cmd.equals(IAudioConstants.DSR_COMMAND_TEXT_WEATHER))
        {
            
        }
    }
    
    protected void addNavAudio(Vector audioList)
    {
        PreferenceDao preferenceDao = ((DaoManager)DaoManager.getInstance()).getPreferenceDao();
        Preference routeStylePref = preferenceDao.getPreference(Preference.ID_PREFERENCE_ROUTETYPE);
        
        int routeStyle = Route.ROUTE_FASTEST;
        if(routeStylePref != null)
        {
            routeStyle = routeStylePref.getIntValue();
        }
        
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
            default:
            {
                audioId = IAudioRes.STATIC_AUDIO_ROUTESTYLE_FASTEST;
                break;
            }
        }

        if(audioList == null)
            audioList = new Vector();
        
        AudioDataNode toAudio = AudioDataFactory.getInstance().createAudioDataNode(
            AudioDataFactory.getInstance().createAudioData(IAudioRes.STATIC_AUDIO_TO));
        audioList.insertElementAt(toAudio, 0);
        
        AudioDataNode styleAudio = AudioDataFactory.getInstance().createAudioDataNode(
            AudioDataFactory.getInstance().createAudioData(audioId));
        audioList.insertElementAt(styleAudio, 0);
        
        AudioDataNode gettingAudio = AudioDataFactory.getInstance().createAudioDataNode(
            AudioDataFactory.getInstance().createAudioData(IAudioRes.STATIC_AUDIO_GETTING));
        audioList.insertElementAt(gettingAudio, 0);
    }

    public void recognizeStatusUpdate(int status, IDsrStreamProxy dsrStreamProxy)
    {
        String command = null;
        int commandType = -1;
        String[] messages = null;
        Vector addresses = null;
        Vector audioList = null;
        
        if(dsrStreamProxy != null)
        {
            command = dsrStreamProxy.getCommand();
            commandType = dsrStreamProxy.getCommandType();
            if (commandType != -1 || getDsrType() == IAudioConstants.DSR_RECOGNIZE_POI
                    || getDsrType() == IAudioConstants.DSR_RECOGNIZE_ONE_SHOT_ADDRESS)// Fix bug TN-1588
            {
                messages = dsrStreamProxy.getMessages();
                addresses = dsrStreamProxy.getAddresses();
                audioList = dsrStreamProxy.getAudio();
                if(audioList == null)
                    audioList = new Vector();
                
                if(dsrStreamProxy.getMultiMatchAudios() != null)
                {
                    this.put(KEY_V_MULTI_MATCH_AUDIOS, dsrStreamProxy.getMultiMatchAudios());
                }
            }
        }
        
        if(status == IRecordEventListener.EVENT_TYPE_SPEECH_STOP || status == IRecordEventListener.EVENT_TYPE_STOP)
        {
            if(isCancel)
                return;
            
            if(!isThinking)
            {
                isThinking = true;
                TnTelephonyManager.getInstance().vibrate(200);
                this.postModelEvent(EVENT_MODEL_DO_THINKING);
            }
        }
        else if(status == IRecordEventListener.EVENT_TYPE_RECO_SUCCESSFUL && command != null)
        {
            if(addresses != null)
            {
                if(addresses.size() == 1)
                {
                    this.put(KEY_O_SELECTED_ADDRESS, addresses.elementAt(0));
                }
                else // for multimatch case
                {
                    this.put(KEY_V_ALTERNATIVE_ADDRESSES, addresses);
                    this.postModelEvent(EVENT_MODEL_MULTI_MATCH);
                    
                    //Fix TN-2395. No need to play audio here.
                    //if(audioList != null)
                    //{
                    //    MediaManager.getInstance().playAudio(audioList);
                    //}
                    return;
                }
            }
            
            handleCommand(command, commandType, messages, audioList);
            isThinking = false;
        }
        else/* if(status == IRecordEventListener.EVENT_TYPE_RECO_FAIL)*/
        {
            if(isCancel)
                return;
            
            handleError(dsrStreamProxy == null);
            
            isCancel = true;
            isThinking = false;
        }
    }

    protected void handleError(boolean isNoSpeech)
    {
        if(isNoSpeech)
        {
            String labelStr = ResourceManager.getInstance().getCurrentBundle().getString(IStringDsr.RES_LABEL_LONG_SILENCE, IStringDsr.FAMILY_DSR);
            this.put(KEY_S_DSR_SILENCE_MASSAGE, labelStr);
            errorTimes ++;
            if(errorTimes > 2)
            {
                errorTimes = 0;
                this.postModelEvent(EVENT_MODEL_ERROR_TIMES_EXCEED);
                return;
            }
        }
        
        this.put(KEY_B_IS_PLAY_ERROR_AUDIO, true);
        this.postModelEvent(EVENT_MODEL_RECO_ERROR);
    }

    public void playErrorAudio()
    {
        AudioDataNode didnt = AudioDataFactory.getInstance().createAudioDataNode(
            AudioDataFactory.getInstance().createAudioData(IAudioRes.DSR_DONT_GET));
        AudioDataNode saycommand = AudioDataFactory.getInstance().createAudioDataNode(
            AudioDataFactory.getInstance().createAudioData(IAudioRes.DSR_PPT_SAY_COMMAND));
        
        Vector audioList = new Vector();
        audioList.addElement(didnt);
        audioList.addElement(saycommand);
        
        MediaManager.getInstance().playAudio(audioList, this);
    }
    
    public void finishPlayer()
    {
        this.remove(KEY_B_IS_PLAY_ERROR_AUDIO);
        this.remove(KEY_B_IS_DO_RECOGNIZE);

        this.postModelEvent(EVENT_MODEL_PLAY_ERROR_FINISH);
    }

}
