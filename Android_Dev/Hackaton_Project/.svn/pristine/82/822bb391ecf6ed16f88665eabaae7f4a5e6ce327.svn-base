/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * AddressProxy.java
 *
 */
package com.telenav.data.serverproxy.impl.protobuf;

import java.io.IOException;
import java.util.Vector;

import com.telenav.app.ThreadManager;
import com.telenav.comm.Comm;
import com.telenav.comm.Host;
import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.dao.serverproxy.AbstractDaoManager;
import com.telenav.data.dao.serverproxy.AddressDao;
import com.telenav.data.datatypes.address.Address;
import com.telenav.data.datatypes.address.CommonDBdata;
import com.telenav.data.datatypes.address.FavoriteCatalog;
import com.telenav.data.datatypes.address.Stop;
import com.telenav.data.serverproxy.IServerProxyConstants;
import com.telenav.data.serverproxy.IServerProxyListener;
import com.telenav.data.serverproxy.IUserProfileProvider;
import com.telenav.data.serverproxy.RequestItem;
import com.telenav.data.serverproxy.impl.IAddressProxy;
import com.telenav.datatypes.DataUtil;
import com.telenav.j2me.datatypes.ProtocolBuffer;
import com.telenav.j2me.framework.protocol.ProtoCacheCitiesReq;
import com.telenav.j2me.framework.protocol.ProtoCacheCitiesResp;
import com.telenav.j2me.framework.protocol.ProtoConfirmRecentStopsReq;
import com.telenav.j2me.framework.protocol.ProtoConfirmRecentStopsResp;
import com.telenav.j2me.framework.protocol.ProtoFavoriteCategory;
import com.telenav.j2me.framework.protocol.ProtoFetchAllStopsReq;
import com.telenav.j2me.framework.protocol.ProtoFetchAllStopsResp;
import com.telenav.j2me.framework.protocol.ProtoStopPoiWrapper;
import com.telenav.j2me.framework.protocol.ProtoSyncNewStops;
import com.telenav.j2me.framework.protocol.ProtoSyncNewStopsById;
import com.telenav.j2me.framework.protocol.ProtoSyncNewStopsByIdReq;
import com.telenav.j2me.framework.protocol.ProtoSyncNewStopsByIdResp;
import com.telenav.j2me.framework.protocol.ProtoSyncNewStopsInfo;
import com.telenav.j2me.framework.protocol.ProtoSyncNewStopsReq;
import com.telenav.j2me.framework.protocol.ProtoSyncNewStopsResp;
import com.telenav.j2me.framework.util.ToStringUtils;
import com.telenav.logger.Logger;
import com.telenav.module.AppConfigHelper;
import com.telenav.module.poi.localevent.LocalEventDetailSyncExecutor;
import com.telenav.threadpool.IJob;
import com.telenav.threadpool.ThreadPool;
import com.telenav.util.PrimitiveTypeCache;

/**
 *@author fqming (fqming@telenav.cn)
 *@date Jul 19, 2010
 */
public class ProtoBufAddressProxy extends AbstractProtobufServerProxy implements IAddressProxy
{
    private Vector addedFavoriteCategories = new Vector();
    private Vector deletedFavoriteCategories = new Vector();
    private Vector updatedFavoriteCategories = new Vector();

    private Vector addedRecentStops = new Vector();
    private Vector deletedRecentStops = new Vector();

    private Vector addedFavorites = new Vector();
    private Vector deletedFavorites = new Vector();
    private Vector updatedFavorites = new Vector();
    private Vector notificationVector = new Vector();

    private long lastFavoriteSyncTime;
    private long lastRecentStopSyncTime;
    
    private boolean isUseBackupDao = false;
    private boolean isParseFetchAllInThread = true; //Default to be true here.
    
    public ProtoBufAddressProxy(Host host, Comm comm, IServerProxyListener serverProxyListener, IUserProfileProvider userProfileProvider, boolean isUseBackupDao)
    {
        super(host, comm, serverProxyListener, userProfileProvider);
        this.isUseBackupDao = isUseBackupDao;
    }

    protected int parseRequestSpecificData(ProtocolBuffer protoBuf) throws IOException
    {
        if (IServerProxyConstants.ACT_CACHE_CITIES.equals(protoBuf.getObjType()))
        {
            ProtoCacheCitiesResp resp = ProtoCacheCitiesResp.parseFrom(protoBuf.getBufferData());
            this.status = resp.getStatus();
            this.errorMessage = resp.getErrorMessage();
            if (status == IServerProxyConstants.SUCCESS)
            {
                parseNearCities(resp);
            }
        }
        else if (IServerProxyConstants.ACT_FETCH_ALL_STOPS.equals(protoBuf.getObjType()))
        {
            ProtoFetchAllStopsResp resp = ProtoFetchAllStopsResp.parseFrom(protoBuf.getBufferData());
            this.status = resp.getStatus();
            this.errorMessage = resp.getErrorMessage();
            if (status == IServerProxyConstants.SUCCESS)
            {
                if(isParseFetchAllInThread)
                {
                    //it's a heavy operation and will block the network receiving.
                    parseFetchAllInThread(resp);
                }
                else
                {
                    parseFetchAllStops(resp);
                }
            }
        }
        else if (IServerProxyConstants.ACT_SYNC_STOPS.equals(protoBuf.getObjType()))
        {
            ProtoSyncNewStopsResp resp = ProtoSyncNewStopsResp.parseFrom(protoBuf.getBufferData());
            
            if(AppConfigHelper.isLoggerEnable)
            {
                Logger.log(Logger.INFO, AbstractProtobufServerProxy.class.getName(), ToStringUtils.toString(resp));
            }
            
            this.status = resp.getStatus();
            this.errorMessage = resp.getErrorMessage();
            if (status == IServerProxyConstants.SUCCESS)
            {
                if(AppConfigHelper.isLoggerEnable)
                {
                    Logger.log(Logger.INFO, AbstractProtobufServerProxy.class.getName(), "SyncNewStops: ========= resp.hasSyncNewStops() = " + resp.hasSyncNewStops());
                }
                if (resp.hasSyncNewStops())
                {
                    parseSyncStops(resp.getSyncNewStops());
                }
            }
        }
        else if (IServerProxyConstants.ACT_SYNC_NEW_STOPS_BY_ID.equals(protoBuf.getObjType()))
        {
            ProtoSyncNewStopsByIdResp resp = ProtoSyncNewStopsByIdResp.parseFrom(protoBuf.getBufferData());

            if (AppConfigHelper.isLoggerEnable)
            {
                Logger.log(Logger.INFO, AbstractProtobufServerProxy.class.getName(), ToStringUtils.toString(resp));
            }

            this.status = resp.getStatus();
            this.errorMessage = resp.getErrorMessage();
            if (status == IServerProxyConstants.SUCCESS)
            {
                if (resp.hasSyncNewStopsById())
                {
                    parseFetchStopsById(resp.getSyncNewStopsById());
                }
            }
        }
        else if (IServerProxyConstants.ACT_CONFIRMSTOPS.equals(protoBuf.getObjType()))
        {
            ProtoConfirmRecentStopsResp resp = ProtoConfirmRecentStopsResp
                    .parseFrom(protoBuf.getBufferData());
            this.status = resp.getStatus();
            this.errorMessage = resp.getErrorMessage();
        }
        return status;
    }
    
    private void parseNearCities(ProtoCacheCitiesResp resp)
    {
        String lat = resp.getLat();
        String lon = resp.getLon();
        String radius = resp.getRadius();

        byte[] data = null;
        if (resp.getCityData() != null)
            data = resp.getCityData().toByteArray();

        Vector vecCity = new Vector();

        try
        {
            if (data != null && data.length > 0)
            {
                int offset = 0;

                int len;
                do
                {
                    Stop stop = new Stop();
                    // country
                    len = data[offset];
                    offset += 1;
                    try
                    {
                        stop.setCountry(new String(data, offset, len, "UTF-8"));
                    }
                    catch (Exception e)
                    {
                        Logger.log(this.getClass().getName(), e);
                    }
                    offset += len;

                    // state
                    len = data[offset];
                    offset += 1;
                    try
                    {
                        stop.setProvince(new String(data, offset, len, "UTF-8"));
                    }
                    catch (Throwable e)
                    {
                        Logger.log(this.getClass().getName(), e);
                    }
                    offset += len;

                    // zip
                    len = data[offset];
                    offset += 1;
                    try
                    {
                        stop.setPostalCode(new String(data, offset, len, "UTF-8"));
                    }
                    catch (Exception e)
                    {
                        Logger.log(this.getClass().getName(), e);
                    }
                    offset += len;

                    // lat
                    stop.setLat(DataUtil.readInt(data, offset));
                    offset += 4;

                    // lon
                    stop.setLon(DataUtil.readInt(data, offset));
                    offset += 4;

                    // city
                    len = data[offset];
                    offset += 1;
                    try
                    {
                        stop.setCity(new String(data, offset, len, "UTF-8"));
                    }
                    catch (Throwable e)
                    {
                        Logger.log(this.getClass().getName(), e);
                    }
                    offset += len;

                    stop.setFirstLine("");
                    vecCity.addElement(stop);
                } while (offset < data.length - 1);
            }
        }
        catch (Exception e)
        {
            Logger.log(this.getClass().getName(), e, "Exception: Parse Near Cities Fail");
        }

        AbstractDaoManager.getInstance().getNearCitiesDao().putNearCities(lat, lon,
            radius, vecCity, this.getRegion());
        AbstractDaoManager.getInstance().getNearCitiesDao().store();
    }
    
    private void parseFetchAllInThread(final ProtoFetchAllStopsResp resp)
    {
        ThreadPool threadPool = ThreadManager.getPool(ThreadManager.TYPE_APP_ACTION);
        threadPool.addJob(new IJob()
        {
            boolean isRunning = false;
            public boolean isRunning()
            {
                return isRunning;
            }
            
            public boolean isCancelled()
            {
                return false;
            }
            
            public void execute(int handlerID)
            {
                isRunning = true;
                try
                {
                    parseFetchAllStops(resp);
                }catch(Exception e)
                {
                    Logger.log(this.getClass().getName(), e);
                }
                isRunning = false;
            }
            
            public void cancel()
            {
                
            }
        });
    }

    public String syncStops(long lastSyncTime, Vector recentAddress, Vector favoritesAddress, Vector favCatalogs, Vector receivedAddresses)
    {
        try
        {
            RequestItem requestItem = new RequestItem(IServerProxyConstants.ACT_SYNC_STOPS, this);
            requestItem.params = new Vector();
            requestItem.params.addElement(PrimitiveTypeCache.valueOf(lastSyncTime));
            requestItem.params.addElement(recentAddress);
            requestItem.params.addElement(favoritesAddress);
            requestItem.params.addElement(favCatalogs);
            requestItem.params.addElement(receivedAddresses);
            Vector list = createProtoBufReq(requestItem);
            return this.sendRequest(list, IServerProxyConstants.ACT_SYNC_STOPS);

        }
        catch (Exception e)
        {
            e.printStackTrace();
            if (listener != null)
                listener.networkError(this, IServerProxyListener.EXCEPTION_SEND, null);
            return "";
        }
    }
    
    public String fetchStopsById(String type, String addressId)
    {
        notificationVector.clear();
        try
        {
            RequestItem requestItem = new RequestItem(IServerProxyConstants.ACT_SYNC_NEW_STOPS_BY_ID, this);
            requestItem.params = new Vector();
            requestItem.params.addElement(type);
            requestItem.params.addElement(addressId);
            Vector list = createProtoBufReq(requestItem);
            return this.sendRequest(list, IServerProxyConstants.ACT_SYNC_NEW_STOPS_BY_ID);

        }
        catch (Exception e)
        {
            e.printStackTrace();
            if (listener != null)
                listener.networkError(this, IServerProxyListener.EXCEPTION_SEND, null);
            return "";
        }
    }
    
    
    public long getLastFavoriteSyncTime()
    {
        return this.lastFavoriteSyncTime;
    }
    
    public long getLastRecentStopSyncTime()
    {
        return this.lastRecentStopSyncTime;
    }
    
    public String updateCities(int lat, int lon)
    {
        try{
            Vector list = new Vector();
            RequestItem req = new RequestItem(IServerProxyConstants.ACT_CACHE_CITIES, this);
            req.params = new Vector();
            req.params.addElement(Integer.valueOf(lat));
            req.params.addElement(Integer.valueOf(lon));
            addRequestArgs(list, req);
            return this.sendRequest(list, IServerProxyConstants.ACT_CACHE_CITIES);
        }
        catch(Exception e)
        {
            Logger.log(this.getClass().getName(), e);
            if (listener != null)
                listener.networkError(this, IServerProxyListener.EXCEPTION_SEND, null);
            return "";
        }
    }
    
    protected void addRequestArgs(Vector requestVector,RequestItem requestItem) throws Exception
    {
        if (IServerProxyConstants.ACT_FETCH_ALL_STOPS.equals(requestItem.action))
        {
            ProtoFetchAllStopsReq.Builder builder = ProtoFetchAllStopsReq.newBuilder();

            if (requestItem.params != null && requestItem.params.size() > 0)
            {
                Boolean param = (Boolean)requestItem.params.elementAt(0);
                if(param != null)
                {
                    this.isParseFetchAllInThread = param.booleanValue();
                }
            }
            ProtocolBuffer pb = new ProtocolBuffer();
            pb.setBufferData(convertToByteArray(builder.build()));
            pb.setObjType(IServerProxyConstants.ACT_FETCH_ALL_STOPS);
            requestVector.addElement(pb);
        }
        else if (IServerProxyConstants.ACT_SYNC_STOPS.equals(requestItem.action))
        {
            if (requestItem.params != null && requestItem.params.size() > 3)
            {
                ProtoSyncNewStopsReq.Builder builder = ProtoSyncNewStopsReq.newBuilder();

                Long lastSyncTimej = (Long) requestItem.params.elementAt(0);
                Vector recentStops = (Vector) requestItem.params.elementAt(1);
                Vector favorites = (Vector) requestItem.params.elementAt(2);
                Vector favCatalogs = (Vector) requestItem.params.elementAt(3);
                Vector receivedAddresses = (Vector) requestItem.params.elementAt(4);
                builder.setLastFavCategoriesSynTime(lastSyncTimej.longValue());
                builder.setLastFavoritesSyncTime(lastSyncTimej.longValue());
                if (recentStops != null)
                {
                    for (int i = 0; i < recentStops.size(); i++)
                    {
                        Address address = (Address) recentStops.elementAt(i);
                        ProtoStopPoiWrapper protoStopPoiWrapper = ProtoBufServerProxyUtil.convertProtoStopPoiWrapper(address);
                        if (address.getStatus() == CommonDBdata.ADDED)
                            builder.addElementAddedRecentStops(protoStopPoiWrapper);
                        else if (address.getStatus() == CommonDBdata.DELETED)
                            builder.addElementDeletedRecentStops(protoStopPoiWrapper);
                        else if (address.getStatus() == CommonDBdata.UPDATED)
                            builder.addElementUpdatedRecentStops(protoStopPoiWrapper);
                    }
                }

                if (favorites != null)
                {
                    for (int i = 0; i < favorites.size(); i++)
                    {
                        Address address = (Address) favorites.elementAt(i);
                        ProtoStopPoiWrapper protoStopPoiWrapper = ProtoBufServerProxyUtil.convertProtoStopPoiWrapper(address);
                        if (address.getStatus() == CommonDBdata.ADDED)
                            builder.addElementAddedFavorites(protoStopPoiWrapper);
                        else if (address.getStatus() == CommonDBdata.DELETED)
                            builder.addElementDeletedFavorites(protoStopPoiWrapper);
                        else if (address.getStatus() == CommonDBdata.UPDATED)
                            builder.addElementUpdatedFavorites(protoStopPoiWrapper);
                    }
                }

                if (favCatalogs != null)
                {
                    for (int i = 0; i < favCatalogs.size(); i++)
                    {
                        FavoriteCatalog cat = (FavoriteCatalog) favCatalogs.elementAt(i);
                        ProtoFavoriteCategory protoFc = ProtoBufServerProxyUtil.convertFavoriteCategory(cat);
                        if (cat.getStatus() == CommonDBdata.ADDED)
                            builder.addElementAddedFavoriteCategories(protoFc);
                        else if (cat.getStatus() == CommonDBdata.DELETED
                                || cat.getStatus() == FavoriteCatalog.CATEGORY_DELETED_WITH_CHILDREN
                                || cat.getStatus() == FavoriteCatalog.CATEGORY_DELETED_WITHOUT_CHILDREN)
                            builder.addElementDeletedFavoriteCategories(protoFc);
                        else if (cat.getStatus() == CommonDBdata.UPDATED)
                            builder.addElementUpdatedFavoriteCategories(protoFc);
                    }
                }
                
                if (receivedAddresses != null)
                {
                    for (int i = 0; i < receivedAddresses.size(); i++)
                    {
                        Address address = (Address) receivedAddresses.elementAt(i);
                        ProtoStopPoiWrapper protoStopPoiWrapper = ProtoBufServerProxyUtil.convertProtoStopPoiWrapper(address);
                        if (address.getStatus() == CommonDBdata.ADDED)
                            builder.addElementAddedFavorites(protoStopPoiWrapper);
                        else if (address.getStatus() == CommonDBdata.DELETED)
                            builder.addElementDeletedFavorites(protoStopPoiWrapper);
                        else if (address.getStatus() == CommonDBdata.UPDATED)
                            builder.addElementUpdatedFavorites(protoStopPoiWrapper);
                    }
                }

                ProtocolBuffer pb = new ProtocolBuffer();
                pb.setBufferData(convertToByteArray(builder.build()));
                pb.setObjType(IServerProxyConstants.ACT_SYNC_STOPS);
                requestVector.addElement(pb);
            }
            else
            {
                throw new IllegalArgumentException("request params is null or size is wrong.");
            }
        }
        
        else if (IServerProxyConstants.ACT_SYNC_NEW_STOPS_BY_ID.equals(requestItem.action))
        {
            ProtoSyncNewStopsInfo.Builder infoBuilder = ProtoSyncNewStopsInfo.newBuilder();
            infoBuilder.setType(requestItem.params.elementAt(0).toString());
            infoBuilder.setAddressId(requestItem.params.elementAt(1).toString());
            
            ProtoSyncNewStopsByIdReq.Builder builder = ProtoSyncNewStopsByIdReq.newBuilder();
            builder.addElementSyncNewStopsInfo(infoBuilder.build());
            
            ProtocolBuffer pb = new ProtocolBuffer();
            pb.setBufferData(convertToByteArray(builder.build()));
            pb.setObjType(IServerProxyConstants.ACT_SYNC_NEW_STOPS_BY_ID);
            requestVector.addElement(pb);
        }
        
        else if(IServerProxyConstants.ACT_CACHE_CITIES.equals(requestItem.action))
        {
            ProtoCacheCitiesReq.Builder builder = ProtoCacheCitiesReq.newBuilder();
            builder.setLat(requestItem.params.elementAt(0).toString());
            builder.setLon(requestItem.params.elementAt(1).toString());
            ProtocolBuffer pb = new ProtocolBuffer();
            pb.setBufferData(convertToByteArray(builder.build()));
            pb.setObjType(IServerProxyConstants.ACT_CACHE_CITIES);
            requestVector.addElement(pb);
        }
        else if(IServerProxyConstants.ACT_CONFIRMSTOPS.equals(requestItem.action))
        {
            ProtoConfirmRecentStopsReq.Builder builder = ProtoConfirmRecentStopsReq.newBuilder();
            if (requestItem.params != null && requestItem.params.size() > 5)
            {
                Vector insertRecentStops = (Vector) requestItem.params.elementAt(0);
                Vector deletedRecentStops = (Vector) requestItem.params.elementAt(1);
                Vector addFavorites = (Vector) requestItem.params.elementAt(2);
                Vector deleteFavorites = (Vector) requestItem.params.elementAt(3);
                Vector addedFavoriteCategories = (Vector) requestItem.params.elementAt(4);
                Vector deletedFavoriteCategories = (Vector) requestItem.params
                        .elementAt(5);
                builder.setInsertRecentStops(insertRecentStops);
                builder.setDeletedRecentStops(deletedRecentStops);
                builder.setAddFavorites(addFavorites);
                builder.setDeleteFavorites(deleteFavorites);
                builder.setAddedFavoriteCategories(addedFavoriteCategories);
                builder.setDeletedFavoriteCategories(deletedFavoriteCategories);
                ProtocolBuffer pb = new ProtocolBuffer();
                pb.setBufferData(convertToByteArray(builder.build()));
                pb.setObjType(requestItem.action);
                requestVector.add(pb);
            }
           
        }
    }
    
    public String fetchAllStops(boolean isParseInThread)
    {
        try
        {
            RequestItem requestItem = new RequestItem(IServerProxyConstants.ACT_FETCH_ALL_STOPS, this);
            requestItem.params = new Vector();
            requestItem.params.addElement(PrimitiveTypeCache.valueOf(isParseInThread));
            Vector list = createProtoBufReq(requestItem);
            return this.sendRequest(list, IServerProxyConstants.ACT_FETCH_ALL_STOPS);
        }
        catch(Exception e)
        {
            Logger.log(this.getClass().getName(), e);
            if (listener != null)
                listener.networkError(this, IServerProxyListener.EXCEPTION_SEND, null);
            return "";
        }
    }
    
    private void parseFetchAllStops(ProtoFetchAllStopsResp resp)
    {
        AddressDao addressDao;
        
        if(isUseBackupDao)
        {
            addressDao = DaoManager.getInstance().getBackupAddressDao();
        }
        else
        {
            addressDao = DaoManager.getInstance().getAddressDao();
        }
        Vector v = resp.getFavoriteCategory();
        if (v != null)
        {
            for (int i = 0; i < v.size(); i++)
            {
                FavoriteCatalog cat = ProtoBufServerProxyUtil.convertFavoriteCategory((ProtoFavoriteCategory) v.elementAt(i));
                long catId = cat.getId();
                addressDao.addFavoriteCatalog(cat);
                cat.setId(catId);
                cat.setStatus(CommonDBdata.UNCHANGED);
            }
        }

        v = resp.getFavoriteStops();
        if (v != null)
        {
            for (int i = 0; i < v.size(); i++)
            {
                Address addr = ProtoBufServerProxyUtil
                        .convertAddress((ProtoStopPoiWrapper) v.elementAt(i));                    
                long addrId = addr.getId();
                addressDao.addAddress(addr, true);
                addr.setId(addrId);
                addr.setStatus(CommonDBdata.UNCHANGED);
                addr.setSource(Address.SOURCE_FAVORITES);        
            }
        }

        v = resp.getRecentStops();
        if (v != null)
        {
            for (int i = 0; i < v.size(); i++)
            {
                Address addr = ProtoBufServerProxyUtil.convertAddress((ProtoStopPoiWrapper) v.elementAt(i));
                long addrId = addr.getId();
                addressDao.addAddress(addr, true);
                addr.setId(addrId);
                addr.setStatus(CommonDBdata.UNCHANGED);
                addr.setSource(Address.SOURCE_RECENT_PLACES);
            }
        }

        this.lastFavoriteSyncTime = resp.getFavoriteLastSyncTime();
        addressDao.storeSyncTime(true, this.lastFavoriteSyncTime);

       
        if(resp.hasHomeStops() || resp.hasWorkStops())
        {
            Stop homeStop = ProtoBufServerProxyUtil.convertStop(resp.getHomeStops());
            boolean isHomeFromCloud = (homeStop != null);
            Stop workStop = ProtoBufServerProxyUtil.convertStop(resp.getWorkStops());
            boolean isWorkFromCloud = (workStop != null);
            addressDao.setHomeOfficeAddress(homeStop, isHomeFromCloud, workStop, isWorkFromCloud, this.isUseBackupDao);
        }

        addressDao.store();
        
        LocalEventDetailSyncExecutor.getInstance().requestLocalEventDetail();
    }

    private void parseSyncStops(ProtoSyncNewStops newStops)
    {
        Vector v;
        long insertRecentStopsIds[] = null,deletedRecentStopsIds[]=null,
        addFavoritesIds[]=null,deleteFavoritesIds[]=null,
        addedFavoriteCategoriesIds[]=null,deletedFavoriteCategoriesIds[]=null;
        v = newStops.getAddedFavoriteCategories();
        if(AppConfigHelper.isLoggerEnable)
        {
            Logger.log(Logger.INFO, AbstractProtobufServerProxy.class.getName(), "SyncNewStops: ========= Added categories in resp = " + v);
        }
        if (v != null)
        {
            if(AppConfigHelper.isLoggerEnable)
            {
                Logger.log(Logger.INFO, AbstractProtobufServerProxy.class.getName(), "SyncNewStops: ========= Added categories count = " + v.size());
            }
            addedFavoriteCategoriesIds = new long[v.size()];
            for (int i=0; i<v.size(); i++)
            {
                FavoriteCatalog fc = ProtoBufServerProxyUtil.convertFavoriteCategory((ProtoFavoriteCategory)v.elementAt(i));
                addedFavoriteCategories.addElement(fc);
                addedFavoriteCategoriesIds[i]=fc.getId();
            }
            
        }
        
        v = newStops.getDeletedFavoriteCategories();
        if(AppConfigHelper.isLoggerEnable)
        {
            Logger.log(Logger.INFO, AbstractProtobufServerProxy.class.getName(), "SyncNewStops: ========= Deleted categories in resp = " + v);
        }
        if (v != null)
        {
            if(AppConfigHelper.isLoggerEnable)
            {
                Logger.log(Logger.INFO, AbstractProtobufServerProxy.class.getName(), "SyncNewStops: ========= Deleted categories count = " + v.size());
            }
            deletedFavoriteCategoriesIds = new long[v.size()];
            for (int i=0; i<v.size(); i++)
            {
                FavoriteCatalog fc = ProtoBufServerProxyUtil.convertFavoriteCategory((ProtoFavoriteCategory)v.elementAt(i));
                deletedFavoriteCategories.addElement(fc);
                deletedFavoriteCategoriesIds[i]=fc.getId();
            }
           
        }
        
        v = newStops.getUpdatedFavoriteCategories();
        if(AppConfigHelper.isLoggerEnable)
        {
            Logger.log(Logger.INFO, AbstractProtobufServerProxy.class.getName(), "SyncNewStops: ========= Updated categories in resp = " + v);
        }
        if (v != null)
        {
            if(AppConfigHelper.isLoggerEnable)
            {
                Logger.log(Logger.INFO, AbstractProtobufServerProxy.class.getName(), "SyncNewStops: ========= Updated categories count = " + v.size());
            }
            for (int i=0; i<v.size(); i++)
            {
                FavoriteCatalog fc = ProtoBufServerProxyUtil.convertFavoriteCategory((ProtoFavoriteCategory)v.elementAt(i));
                updatedFavoriteCategories.addElement(fc);
            }
        }
        
        v = newStops.getAddedFavorites();
        if(AppConfigHelper.isLoggerEnable)
        {
            Logger.log(Logger.INFO, AbstractProtobufServerProxy.class.getName(), "SyncNewStops: ========= Added Favorites in resp = " + v);
        }
        if (v != null)
        {
            if(AppConfigHelper.isLoggerEnable)
            {
                Logger.log(Logger.INFO, AbstractProtobufServerProxy.class.getName(), "SyncNewStops: ========= Added Favorites count = " + v.size());
            }
            addFavoritesIds = new long[v.size()];
            for (int i=0; i<v.size(); i++)
            {
                Address address = ProtoBufServerProxyUtil.convertAddress((ProtoStopPoiWrapper)v.elementAt(i));
                address.setSource(Address.SOURCE_FAVORITES);
                address.setStatus(CommonDBdata.UNCHANGED);
                addedFavorites.addElement(address);
                addFavoritesIds[i]=address.getId();
            }
           
        }
        
        v = newStops.getDeletedFavorites();
        if(AppConfigHelper.isLoggerEnable)
        {
            Logger.log(Logger.INFO, AbstractProtobufServerProxy.class.getName(), "SyncNewStops: ========= Deleted Favorites in resp = " + v);
        }
        if (v != null)
        {
            if(AppConfigHelper.isLoggerEnable)
            {
                Logger.log(Logger.INFO, AbstractProtobufServerProxy.class.getName(), "SyncNewStops: ========= Deleted Favorites count = " + v.size());
            }
            deleteFavoritesIds = new long[v.size()];
            for (int i = 0; i < v.size(); i++)
            {
                Address address = ProtoBufServerProxyUtil
                        .convertAddress((ProtoStopPoiWrapper) v.elementAt(i));
                address.setSource(Address.SOURCE_FAVORITES);
                deletedFavorites.addElement(address);
                deleteFavoritesIds[i] = address.getId();
            }
           
        }
        
        v = newStops.getUpdatedFavorites();
        if(AppConfigHelper.isLoggerEnable)
        {
            Logger.log(Logger.INFO, AbstractProtobufServerProxy.class.getName(), "SyncNewStops: ========= Updated Favorites in resp = " + v);
        }
        if (v != null)
        {
            if(AppConfigHelper.isLoggerEnable)
            {
                Logger.log(Logger.INFO, AbstractProtobufServerProxy.class.getName(), "SyncNewStops: ========= Updated Favorites count = " + v.size());
            }
            for (int i=0; i<v.size(); i++)
            {
                Address address = ProtoBufServerProxyUtil.convertAddress((ProtoStopPoiWrapper)v.elementAt(i));
                address.setSource(Address.SOURCE_FAVORITES);
                updatedFavorites.addElement(address);
            }
        }
        
        v = newStops.getAddedRecentStops();
        if(AppConfigHelper.isLoggerEnable)
        {
            Logger.log(Logger.INFO, AbstractProtobufServerProxy.class.getName(), "SyncNewStops: ========= Added Recent in resp = " + v);
        }
        if (v != null)
        {
            if(AppConfigHelper.isLoggerEnable)
            {
                Logger.log(Logger.INFO, AbstractProtobufServerProxy.class.getName(), "SyncNewStops: ========= Added Recent count = " + v.size());
            }
            insertRecentStopsIds = new long[v.size()];
            for (int i=0; i<v.size(); i++)
            {
                Address address = ProtoBufServerProxyUtil.convertAddress((ProtoStopPoiWrapper)v.elementAt(i));
                address.setStatus(CommonDBdata.UNCHANGED);
                address.setSource(Address.SOURCE_RECENT_PLACES);
                addedRecentStops.addElement(address);
                insertRecentStopsIds[i]=address.getId();
            }
        }
        
        v = newStops.getDeletedRecentStops();
        if(AppConfigHelper.isLoggerEnable)
        {
            Logger.log(Logger.INFO, AbstractProtobufServerProxy.class.getName(), "SyncNewStops: ========= Deleted Recent in resp = " + v);
        }
        if (v != null)
        {
            if(AppConfigHelper.isLoggerEnable)
            {
                Logger.log(Logger.INFO, AbstractProtobufServerProxy.class.getName(), "SyncNewStops: ========= Deleted Recent count = " + v.size());
            }
            deletedRecentStopsIds = new long[v.size()];
            for (int i=0; i<v.size(); i++)
            {
                Address address = ProtoBufServerProxyUtil.convertAddress((ProtoStopPoiWrapper)v.elementAt(i));
                address.setSource(Address.SOURCE_RECENT_PLACES);
                deletedRecentStops.addElement(address);
                deletedRecentStopsIds[i]=address.getId();
            }
        }
        
        ((DaoManager) DaoManager.getInstance()).getAddressDao().setNetworkUnrevieweAddressSize(0);
        int sharedAddrNumber = newStops.getISharedAddressNumber();
        if(AppConfigHelper.isLoggerEnable)
        {
            Logger.log(Logger.INFO, AbstractProtobufServerProxy.class.getName(), "SyncNewStops: ========= sharedAddrNumber in resp = " + sharedAddrNumber);
        }
        if(sharedAddrNumber > 0)
        {
            int oldUnreviewedNumber = ((DaoManager) DaoManager.getInstance()).getAddressDao().getUnreviewedAddressSize();
            ((DaoManager) DaoManager.getInstance()).getAddressDao().setLocalUnreviewedAddressSize(oldUnreviewedNumber + sharedAddrNumber);
        }
        
        this.lastFavoriteSyncTime = newStops.getLastFavoritesSyncTime();
        
        AbstractDaoManager.getInstance().getAddressDao().storeSyncTime(true, this.lastFavoriteSyncTime);

        boolean isStore = false;
        if ((addedFavorites != null && addedFavorites.size() > 0)
                || (deletedFavorites != null && deletedFavorites.size() > 0))
        {
            if(AppConfigHelper.isLoggerEnable)
            {
                Logger.log(Logger.INFO, AbstractProtobufServerProxy.class.getName(), "SyncNewStops: ========= synchronizeAddresses for favorite");
            }
            AbstractDaoManager.getInstance().getAddressDao().synchronizeAddresses(
                    addedFavorites, deletedFavorites, true);
            isStore = true;
        }
        if ((addedRecentStops != null && addedRecentStops.size() > 0)
                || (deletedRecentStops != null && deletedRecentStops.size() > 0))
        {
            if(AppConfigHelper.isLoggerEnable)
            {
                Logger.log(Logger.INFO, AbstractProtobufServerProxy.class.getName(), "SyncNewStops: ========= synchronizeAddresses for recent");
            }
            AbstractDaoManager.getInstance().getAddressDao().synchronizeAddresses(
                    addedRecentStops, deletedRecentStops, false);
            isStore = true;
        }
        if ((addedFavoriteCategories != null && addedFavoriteCategories.size() > 0)
                || (deletedFavoriteCategories != null && deletedFavoriteCategories
                        .size() > 0))
        {
            if(AppConfigHelper.isLoggerEnable)
            {
                Logger.log(Logger.INFO, AbstractProtobufServerProxy.class.getName(), "SyncNewStops: ========= synchronizeFavoriteCatalog");
            }
            AbstractDaoManager.getInstance().getAddressDao().synchronizeFavoriteCatalog(
                    addedFavoriteCategories, deletedFavoriteCategories);
            isStore = true;
        }
        
        if (isStore == true)
        {
            AbstractDaoManager.getInstance().getAddressDao().store();
            
            if(AppConfigHelper.isLoggerEnable)
            {
                Logger.log(Logger.INFO, AbstractProtobufServerProxy.class.getName(), "SyncNewStops: ========= send confirmStops");
            }
            ProtoBufAddressProxy anotherProxy = new ProtoBufAddressProxy(host, comm, null, userProfileProvider, false);
            anotherProxy.confirmStops(insertRecentStopsIds,deletedRecentStopsIds,addFavoritesIds,
                deleteFavoritesIds,addedFavoriteCategoriesIds,deletedFavoriteCategoriesIds);
        }
        
        LocalEventDetailSyncExecutor.getInstance().requestLocalEventDetail();
    }
    
    private void parseFetchStopsById(ProtoSyncNewStopsById newStopsById)
    {
        Vector v = newStopsById.getGetFavorites();
        if (v != null)
        {
            for (int i=0; i<v.size(); i++)
            {
                Address address = ProtoBufServerProxyUtil.convertAddress((ProtoStopPoiWrapper)v.elementAt(i));
                notificationVector.addElement(address);
            }
        }
    }
    
    public Vector getNotificationVector()
    {
        return this.notificationVector;
    }
      
    
    protected String confirmStops(long[] insertRecentStops, long[] deletedRecentStops,
            long[] addFavorites, long[] deleteFavorites, long[] addedFavoriteCategories,
            long[] deletedFavoriteCategories)
    {
        try
        {
            RequestItem requestItem = new RequestItem(
                    IServerProxyConstants.ACT_CONFIRMSTOPS, this);
            requestItem.params = new Vector();
            requestItem.params.addElement(getVector(insertRecentStops));
            requestItem.params.addElement(getVector(deletedRecentStops));
            requestItem.params.addElement(getVector(addFavorites));
            requestItem.params.addElement(getVector(deleteFavorites));
            requestItem.params.addElement(getVector(addedFavoriteCategories));
            requestItem.params.addElement(getVector(deletedFavoriteCategories));

            Vector list = createProtoBufReq(requestItem);
            return this.sendRequest(list, IServerProxyConstants.ACT_CONFIRMSTOPS);
        }
        catch (Exception e)
        {
            Logger.log(this.getClass().getName(), e);
            if (listener != null)
                listener.networkError(this, IServerProxyListener.EXCEPTION_SEND, null);
            return "";
        }
    }
    
    private Vector getVector(long[] source)
    {
        int length = 0;
        Vector ids = new Vector();
        if (source != null)
        {
            for (int start = 0; start < source.length; start++)
            {
                ids.add(source[start]);
                Logger.log(Logger.INFO, this.getClass().getName(), "---getVector : "
                        + "index" + start + "number" + source[start]);
            }
        }
        return ids;
    }
}
