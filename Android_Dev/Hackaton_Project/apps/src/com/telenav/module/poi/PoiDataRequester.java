/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * PoiDataRequester.java
 *
 */
package com.telenav.module.poi;

import java.util.Vector;

import com.telenav.app.CommManager;
import com.telenav.carconnect.host.CarConnectHostManager;
import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.datatypes.address.Address;
import com.telenav.data.datatypes.address.Stop;
import com.telenav.data.datatypes.poi.Poi;
import com.telenav.data.serverproxy.AbstractServerProxy;
import com.telenav.data.serverproxy.IServerProxyListener;
import com.telenav.data.serverproxy.IUserProfileProvider;
import com.telenav.data.serverproxy.NetworkStatusManager;
import com.telenav.data.serverproxy.impl.IOneBoxSearchProxy;
import com.telenav.data.serverproxy.impl.IPoiSearchProxy;
import com.telenav.data.serverproxy.impl.ServerProxyFactory;
import com.telenav.datatypes.nav.NavState;
import com.telenav.location.TnLocation;
import com.telenav.log.mis.IMisLogConstants;
import com.telenav.log.mis.MisLogManager;
import com.telenav.log.mis.helper.PoiMisLogHelper;
import com.telenav.log.mis.log.AppStatusMisLog;
import com.telenav.log.mis.log.SearchRequestMisLog;
import com.telenav.logger.Logger;
import com.telenav.module.location.LocationProvider;
import com.telenav.res.IStringCommon;
import com.telenav.res.ResourceManager;

/**
 *@author Albert Ma (byma@telenav.cn)
 *@date 2010-12-28
 */
public class PoiDataRequester implements IServerProxyListener
{
    public static final int TYPE_SUCCESS = 0;
    public static final int TYPE_SUCCESS_NO_RESULTS = 1;
    public static final int TYPE_NETWORK_ERROR = 2;
    public static final int TYPE_BAD_ARGS = 3;
    
    public static final int TYPE_RESULT_ADDRESS = 1;
    public static final int TYPE_RESULT_SUGGESTION_DID_U_MEAN = 2;
    public static final int TYPE_RESULT_NO_RESULT = 3;
    
    //FIXME: should be server driven.
    public static final int DEFAULT_PAGE_SIZE = 10;
    
    public static final int TYPE_NO_CATEGORY_ID = -1;
    public static final int TYPE_ONE_BOX_SEARCH = -2;
    protected static final int AVALIABLE_SIZE = 99;
    protected PoiDataWrapper poiDataWrapper;
    
    //proxy
    protected IPoiSearchProxy poiSearchProxy;
    protected IOneBoxSearchProxy oneBoxSearchProxy;
    protected PoiDataListener poiDataListener;
    
    protected int status;

    protected IUserProfileProvider userProfileProvider;
    
    public PoiDataRequester(IUserProfileProvider userProfileProvider)
    {
        this.userProfileProvider = userProfileProvider;
    }
    
    public void cancel()
    {
        this.poiDataListener = null;
        if (poiSearchProxy != null)
        {
            ((AbstractServerProxy) poiSearchProxy).cancel();
        }
        if (oneBoxSearchProxy != null)
        {
            ((AbstractServerProxy) oneBoxSearchProxy).cancel();
        }
    }
    
    public void transactionFinished(AbstractServerProxy proxy, String jobId)
    {
        int size = 0;
        int resultType = TYPE_RESULT_NO_RESULT;
        boolean isPoi = false;
        int totalSize = 0;
        
        boolean isPremiumAccount = DaoManager.getInstance().getBillingAccountDao().isPremiumAccount();
        
        if(proxy instanceof IPoiSearchProxy)
        {
            isPoi = true;
            resultType = TYPE_RESULT_ADDRESS;
            IPoiSearchProxy poiSearchProxy = (IPoiSearchProxy)proxy;
            Vector currPois = poiSearchProxy.getPois();
            size = currPois.size();   
            poiDataWrapper.setTotalCountServerReturned(poiSearchProxy.getTotalCount());
            totalSize = AVALIABLE_SIZE < poiSearchProxy.getTotalCount() ? AVALIABLE_SIZE :poiSearchProxy.getTotalCount();
            for(int i = 0; i < size; i ++)
            {
                Address addr =  (Address)currPois.elementAt(i);
                addr.setSource(Address.SOURCE_SEARCH);
                addr.setType(Address.TYPE_RECENT_POI);
                this.poiDataWrapper.addNormalAddr(addr);
                if(this.poiDataWrapper.getNormalAddressSize() >= AVALIABLE_SIZE)
                {
                    break;
                }
            }
            
            Vector currSponsoredPois = poiSearchProxy.getSponsoredPois();
            int sponsoredSize = 0;
            
            if (!isPremiumAccount)
            {
                if(currSponsoredPois != null)
                {
                    sponsoredSize = currSponsoredPois.size();
                }
            }
            sponsoredSize = Math.min(sponsoredSize, this.poiDataWrapper.sponsorNum);

            
            if (sponsoredSize > 0)
            {
                for (int i = 0; i < sponsoredSize; i++)
                {
                    Address addr = (Address)currSponsoredPois.elementAt(i);
                    addr.setSource(Address.SOURCE_SEARCH);
                    addr.setType(Address.TYPE_RECENT_POI);
                    this.poiDataWrapper.addSponsoredAddr(addr);
                }
            }
            else
            {   
                Address addr = new Address(Address.SOURCE_UNKOWN);
               Poi poi = new Poi();
               poi.setType(Poi.TYPE_DUMMY_POI);
               addr.setPoi(poi);
               this.poiDataWrapper.addSponsoredAddr(addr); 
            }
            this.poiDataWrapper.mixAddress();
            if(size != 0 && poiDataWrapper.getPageNumber() == 0)
            {
                PoiMisLogHelper.newInstance(poiDataWrapper);
            }
            
        }
        else if(proxy instanceof IOneBoxSearchProxy)
        {
            IOneBoxSearchProxy oneBoxSearchProxy = (IOneBoxSearchProxy)proxy;
            int proxyResultType = oneBoxSearchProxy.getResultType();
            poiDataWrapper.setTotalCountServerReturned(oneBoxSearchProxy.getTotalCount());
            totalSize = AVALIABLE_SIZE < oneBoxSearchProxy.getTotalCount() ? AVALIABLE_SIZE :oneBoxSearchProxy.getTotalCount();
            switch (proxyResultType)
            {
                case IOneBoxSearchProxy.TYPE_POI_RESULTS:
                {
                    isPoi = true;
                    Vector currPois= oneBoxSearchProxy.getPois();
                    if(currPois != null)
                    {
                        size = currPois.size();
                        
                        for(int i = 0; i < size; i ++)
                        {
                            Address addr = (Address)currPois.elementAt(i);
                            addr.setSource(Address.SOURCE_SEARCH);
                            addr.setType(Address.TYPE_RECENT_POI);
                            this.poiDataWrapper.addNormalAddr(addr);
                            if(this.poiDataWrapper.getNormalAddressSize() >= AVALIABLE_SIZE)
                            {
                                break;
                            }
                        }
                        Vector currSponsoredPois = oneBoxSearchProxy.getSponsoredPoi();
                        int sponsoredSize = 0;
                        
                        if(!isPremiumAccount)
                        {
                            if(currSponsoredPois != null)
                            {
                                sponsoredSize = currSponsoredPois.size();
                            }
                        }
                        
                        sponsoredSize = Math.min(sponsoredSize, this.poiDataWrapper.sponsorNum);
                        for(int i = 0; i < sponsoredSize; i ++ )
                        {
                            Address addr = (Address)currSponsoredPois.elementAt(i);
                            addr.setType(Address.SOURCE_SEARCH);
                            addr.setType(Address.TYPE_RECENT_POI);
                            this.poiDataWrapper.addSponsoredAddr(addr);
                        }
                        this.poiDataWrapper.mixAddress();
                        
                        if(size != 0 && poiDataWrapper.getPageNumber() == 0)
                        {
                            PoiMisLogHelper.newInstance(poiDataWrapper);
                        }
                        resultType = TYPE_RESULT_ADDRESS;
                    }
                    break;
                }
                case IOneBoxSearchProxy.TYPE_STOPS_RESULTS:
                {
                    this.poiDataWrapper.setIsNeedUserSelection(oneBoxSearchProxy.isNeedUserSelection());
                    Vector addresses = oneBoxSearchProxy.getAddresses();
                    if(addresses != null)
                    {
                        size = addresses.size();
                        for (int i = 0; i < addresses.size(); i ++)
                        {
                            Address addr = (Address)addresses.elementAt(i);
                            addr.setSource(Address.SOURCE_TYPE_IN);
                            //fix carconnect issue: http://jira.telenav.com:8080/browse/TNANDROID-5272
                            //poi should be null for normal address in onebox search result.
                            addr.setPoi(null);
                            addr.setType(Address.TYPE_RECENT_STOP);
                            this.poiDataWrapper.addStopAddress(addr);
                        }
                        resultType = TYPE_RESULT_ADDRESS;
                    }
                    break;
                }
                case IOneBoxSearchProxy.TYPE_SUGGESTS_RESULTS:
                {
                    Vector currSuggestions = oneBoxSearchProxy.getSuggestions();
                    if(currSuggestions != null)
                    {
                        size = currSuggestions.size();
                        for (int i = 0; i < size; i ++)
                        {
                            this.poiDataWrapper.getMultiMatchResults().addElement(currSuggestions.elementAt(i));
                        }
                        resultType = TYPE_RESULT_SUGGESTION_DID_U_MEAN;
                    }
                    break;
                }
            }
        }
        
        poiDataWrapper.setIsDoingRequest(false);
        if (size == 0)
        {
            status = TYPE_SUCCESS_NO_RESULTS;
        }
        else
        {
            if (resultType == TYPE_RESULT_ADDRESS)
            {
                int remainPoiCount = totalSize - poiDataWrapper.getNormalAddressSize();
                this.poiDataWrapper.setTotalNormalCount(totalSize);
                if (remainPoiCount > 0 && isPoi )
                {
                    this.poiDataWrapper.setIsHasMorePoi(true);
                }
                else
                {
                    this.poiDataWrapper.setIsHasMorePoi(false);
                }

            }
            status = TYPE_SUCCESS;
        }
        if (poiDataListener != null)
        {
            this.poiDataListener.poiResultUpdate(status, resultType, "", poiDataWrapper);
        }
    }

   

    public void updateTransactionStatus(AbstractServerProxy proxy, byte progress)
    {
        
    }

    public void networkError(AbstractServerProxy proxy, byte statusCode, String jobId)
    {
        poiDataWrapper.setIsDoingRequest(false);
        status = TYPE_NETWORK_ERROR;
        if (poiDataListener != null)
        {
            poiDataListener.poiResultUpdate(status, TYPE_RESULT_NO_RESULT, null/* proxy.getErrorMsg() */, null);
        }
    }

    public void transactionError(AbstractServerProxy proxy)
    {
        poiDataWrapper.setIsDoingRequest(false);
        status = TYPE_NETWORK_ERROR;
        if (poiDataListener != null)
        {
            poiDataListener.poiResultUpdate(status, TYPE_RESULT_NO_RESULT, null/* proxy.getErrorMsg() */, null);
        }
    }

    public boolean isAllowNetworkRequest(AbstractServerProxy proxy)
    {
        if (!NetworkStatusManager.getInstance().isConnected())
        {
            String errorMessage = ResourceManager.getInstance().getCurrentBundle().getString(IStringCommon.RES_NO_CELL_COVERAGE, IStringCommon.FAMILY_COMMON);
            proxy.setErrorMsg(errorMessage);
            try
            {
                Thread.sleep(2000);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            return false;
        }
        return true;
    }
    
    public void doRequestPoi(PoiDataWrapper poiDataWrapper, PoiDataListener listener)
    {
        if (poiDataWrapper != null && listener != null)
        {
            TnLocation[] locations = new TnLocation[3];
            poiDataWrapper.setIsNeedUserSelection(false);
            for(int i = 0; i < locations.length; i ++)
            {
                //FIXME why we have to give out the location providers.
                locations[i] = new TnLocation("");
            }
            LocationProvider.getInstance().getCurrentLocation(locations, 3,
                LocationProvider.TYPE_GPS | LocationProvider.TYPE_NETWORK);
            Stop anchorStop = poiDataWrapper.getAnchorStop();
            int radius = IOneBoxSearchProxy.DEFAULT_RADIOUS;
            int categoryId = poiDataWrapper.getCategoryId();
            int sortType = poiDataWrapper.getSortType();
            int alongRouteType = poiDataWrapper.getAlongRouteType();
            int searchType = poiDataWrapper.getSearchType();
            int searchFromType = poiDataWrapper.getSearchFromType();
            int pageNumber = poiDataWrapper.getPageNumber();
            int pageSize = poiDataWrapper.getPageSize();
            String searchKeyWord = poiDataWrapper.getQueryWord();
            String transactionId = poiDataWrapper.getSearchUid();
            Stop destStop = poiDataWrapper.getDestStop();
            NavState navState = poiDataWrapper.getNavState();
            int inputType = poiDataWrapper.getInputType();
            int sponsorNum = poiDataWrapper.sponsorNum;
            this.poiDataWrapper = poiDataWrapper;
            this.poiDataListener = listener;
            this.poiDataWrapper.backupData();
            this.poiDataWrapper.setIsDoingRequest(true);
            if(pageNumber == 0)
            {
                startNewPoiMisLogSession(poiDataWrapper);
//                WebViewCacheManager.getInstance().clearAll();
            }
            
            if (categoryId == TYPE_NO_CATEGORY_ID || categoryId > 0)
            {
                if (poiSearchProxy != null)
                {
                    ((AbstractServerProxy) poiSearchProxy).cancel();
                }
                poiSearchProxy = ServerProxyFactory.getInstance().createPoiSearchProxy(
                    null, CommManager.getInstance().getComm(), this, userProfileProvider);
                boolean isMostPopular = false;
                if (sortType == IPoiSearchProxy.TYPE_SORT_BY_POPULAR ||
                		poiDataWrapper.isMostPopularSearch())
                {
                    isMostPopular = true;
                }
                poiSearchProxy.requestPoiByCategory(transactionId, categoryId, searchType,
                    searchFromType, sortType, pageNumber, pageSize, isMostPopular,
                    radius, alongRouteType, searchKeyWord, anchorStop, locations, locations.length, sponsorNum, navState);
            }
            else
            {
                if (oneBoxSearchProxy != null)
                {
                    ((AbstractServerProxy) oneBoxSearchProxy).cancel();
                }
                oneBoxSearchProxy = ServerProxyFactory.getInstance()
                        .createOneBoxSearchProxy(null,
                            CommManager.getInstance().getComm(), this, userProfileProvider);
                if (searchType == IOneBoxSearchProxy.TYPE_SEARCH_ALONGROUTE)
                {
                    oneBoxSearchProxy.oneBoxSearch(transactionId, searchKeyWord, pageNumber, pageSize,
                        radius, searchType, alongRouteType, anchorStop, locations, locations.length, destStop, navState,inputType);
                }
                else
                {
                    oneBoxSearchProxy.oneBoxSearch(transactionId, searchKeyWord, pageNumber, pageSize,
                        radius, searchType, searchType, anchorStop, locations, locations.length, destStop, navState,inputType);
                }
            }
        }
    }
    
    protected void startNewPoiMisLogSession(PoiDataWrapper poiDataWrapper)
    {
        MisLogManager misLogManager = MisLogManager.getInstance();

        AppStatusMisLog appStatusMisLog = (AppStatusMisLog) MisLogManager.getInstance().getMisLog(IMisLogConstants.TYPE_INNER_APP_STATUS);
        appStatusMisLog.increaseSearchCount();

        if (misLogManager.getFilter().isTypeEnable(IMisLogConstants.TYPE_POI_SEARCH_REQUEST))
        {
            SearchRequestMisLog log = misLogManager.getFactory().createSearchRequestMisLog();
            log.setTimestamp(System.currentTimeMillis());
            if (poiDataWrapper.getSearchType() == IOneBoxSearchProxy.TYPE_SEARCH_ALONGROUTE)
            {
                log.setSearchArea(IMisLogConstants.VALUE_SEARCH_AREA_ALONG_ROUTE);
                log.setzAttrAddresType(IMisLogConstants.VALUE_AT_ALONG_ROUTE);
            }
            else if (poiDataWrapper.getAnchorStop() != null)
            {
                log.setAddressType(poiDataWrapper.getAnchorStop().getType());
            }

            Logger.log(Logger.INFO, this.getClass().getName(), IMisLogConstants.PROCESS_MISLOG, new Object[]
            { log });
        }
        
        //Log for car connect
       CarConnectHostManager.getInstance().logPoiSearch(poiDataWrapper.getSearchUid());
    }
    
    public void cancelSearchRequest()
    {
        if (poiSearchProxy != null)
        {
            poiSearchProxy.cancelSearchRequest();
        }
        
        if (oneBoxSearchProxy != null)
        {
            oneBoxSearchProxy.canceSearchRequest();
        }
    }
}
