/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * TnlinkDsrModel.java
 *
 */
package com.telenav.carconnect.provider.tnlink.module.dsr;

import android.util.Log;

import com.telenav.carconnect.CarConnectManager;
import com.telenav.carconnect.LogUtil;
import com.telenav.carconnect.provider.ProtocolConvertor;
import com.telenav.data.datatypes.address.Address;
import com.telenav.data.datatypes.address.Stop;
import com.telenav.data.serverproxy.impl.IPoiSearchProxy;
import com.telenav.datatypes.nav.NavState;
import com.telenav.dsr.DsrManager;
import com.telenav.location.TnLocation;
import com.telenav.module.dsr.DsrModel;
import com.telenav.module.location.LocationProvider;
import com.telenav.navsdk.events.DigitalSpeechRecognitionEvents.DsrCommandSearch;
import com.telenav.navsdk.events.DigitalSpeechRecognitionEvents.DsrError;
import com.telenav.navsdk.events.PointOfInterestData.PointOfInterest;
import com.telenav.navsdk.events.PointOfInterestEvents.PoiSearchRequest;
import com.telenav.res.IStringPoi;
import com.telenav.res.ResourceManager;

/**
 *@author chihlh
 *@date Aug 20, 2012
 */
public class TnlinkDsrModel extends DsrModel
{
    protected void doActionDelegate(int actionId)
    {
        switch (actionId)
        {
            case ACTION_PLAY_ERROR_AUDIO:
            {
                Log.i("Dsr","DsrModel ACTION_PLAY_ERROR_AUDIO");
                playErrorAudio();
                Log.i("AppLink","dsr error");
                Log.i("AppLink","set dsr request to false!");
                CarConnectManager.setDsrRequestFromCar(false);
                DsrError.Builder dsrErrorBuilder=DsrError.newBuilder();
                CarConnectManager.getEventBus().broadcast("DsrError", dsrErrorBuilder.build());
                return;
            }
            case ACTION_DO_SEARCH:
            {
                Log.i("Dsr","DsrModel ACTION_DO_SEARCH");
                //Added By tpeng
                LogUtil.logInfo("ACTION_DO_SEARCH");
                doTnlinkCategorySearch();
                postModelEvent(EVENT_MODEL_COMMON_BACK);
                return;            
            }
        }
        super.doActionDelegate(actionId);
    }
    
    protected void doTnlinkCategorySearch()
    {
        Log.i("Dsr","DsrModel doCategorySearch");
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
        int sortType = IPoiSearchProxy.TYPE_SORT_BY_RELEVANCE;
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
        
        Log.i("Dsr","poi search command!");
        PoiSearchRequest.Builder poiSearchRequestBuilder=PoiSearchRequest.newBuilder();
        if(searchText!=null) {
            Log.i("Dsr","searchText:"+searchText);
            poiSearchRequestBuilder.setQueryString(searchText);
        }
        poiSearchRequestBuilder.setCategoryToSearch(categoryId);
        if (anchorAddress != null)
        {
            PointOfInterest.Builder b = ProtocolConvertor.convertAddressToPointOfInterest(anchorAddress);
            poiSearchRequestBuilder.setNearPoi(b);
        }
        DsrCommandSearch.Builder dsrCommandSearchBuilder=DsrCommandSearch.newBuilder();
        dsrCommandSearchBuilder.setRequest(poiSearchRequestBuilder);
        CarConnectManager.getEventBus().broadcast("DsrCommandSearch", dsrCommandSearchBuilder.build());
        CarConnectManager.setDsrRequestFromCar(false);
        return;
    }
}
