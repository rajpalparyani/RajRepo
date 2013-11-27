/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * RecentModel.java
 *
 */
package com.telenav.module.ac.recent;

import java.util.Vector;

import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.dao.serverproxy.AddressDao;
import com.telenav.data.datatypes.address.Address;
import com.telenav.data.datatypes.address.CommonDBdata;
import com.telenav.data.serializable.SerializableManager;
import com.telenav.data.serverproxy.AbstractServerProxy;
import com.telenav.data.serverproxy.INetworkStatusListener;
import com.telenav.data.serverproxy.IServerProxyConstants;
import com.telenav.data.serverproxy.NetworkStatusManager;
import com.telenav.data.sort.util.SortUtil;
import com.telenav.module.ac.AddressListFilter;
import com.telenav.module.ac.SyncStopsExecutor;
import com.telenav.module.mapdownload.IOnBoardDataAvailabilityListener;
import com.telenav.module.mapdownload.MapDownloadOnBoardDataStatusManager;
import com.telenav.module.poi.localevent.LocalEventDetailSyncExecutor;
import com.telenav.module.sync.IMigrationListener;
import com.telenav.module.sync.MigrationExecutor;
import com.telenav.mvc.AbstractCommonNetworkModel;
import com.telenav.res.IStringCommon;
import com.telenav.res.ResourceManager;
import com.telenav.telephony.TnTelephonyManager;
import com.telenav.tnui.core.AbstractTnUiHelper;

/**
 * @author wzhu (wzhu@telenav.cn)
 * @date 2010-10-27
 */
class RecentModel extends AbstractCommonNetworkModel implements IRecentConstants, IMigrationListener, IOnBoardDataAvailabilityListener, INetworkStatusListener
{

    protected boolean isForegroundSync = false;
    
    protected void activateDelegate(boolean isUpdateView)
    {
        super.activateDelegate(isUpdateView);
        LocalEventDetailSyncExecutor.getInstance().addGobyEventRefreshListener(this);
        if (isUpdateView)
        {
            put(KEY_V_CURRENT_RECENT_ADDRESSES, getRecentPlaces(true));
            put(KEY_B_REFRESH_LIST, true);
            postModelEvent(EVENT_MODEL_UPDATE_VIEW);
        }
    }

    @Override
    protected void deactivateDelegate()
    {
        LocalEventDetailSyncExecutor.getInstance().removeGobyEventRefreshListener(this);
    }
    
    protected void doActionDelegate(int actionId)
    {
        switch (actionId)
        {
            case ACTION_INIT:
            {
                MapDownloadOnBoardDataStatusManager.getInstance().addStatusChangeListener(this);
                NetworkStatusManager.getInstance().addStatusListener(this);
                put(KEY_V_CURRENT_RECENT_ADDRESSES, getRecentPlaces(false));
                put(KEY_S_SEARCH_TEXT, "");
                DaoManager.getInstance().getStartupDao().store();
                this.postModelEvent(EVENT_MODEL_LAUNCH_MAIN);
                break;
            }
            case ACTION_SEARCH_RECENT:
            {
                put(KEY_B_REFRESH_LIST, true);
                put(KEY_V_CURRENT_RECENT_ADDRESSES, getRecentPlaces(true));
                break;
            }
            case ACTION_SYN_RECENT:
            {
                isForegroundSync = true;
                syncStops();
                break;
            }
            case ACTION_DELETE_ALL_RECENT:
            {
                Vector recentAddresses = getRecentPlaces(false);
                int recentSize = recentAddresses.size();
                for (int i = 0; i < recentSize; i++)
                {
                    DaoManager.getInstance().getAddressDao().deleteAddress((Address) recentAddresses.elementAt(i));
                }
                DaoManager.getInstance().getAddressDao().store();
                put(KEY_V_CURRENT_RECENT_ADDRESSES, new Vector());
                isForegroundSync = false;
                this.put(KEY_B_REFRESH_LIST, true);
                syncStops();
                break;
            }
            case ACTION_DELETE_RECENT:
            {
                Address address = (Address) get(KEY_O_SELECTED_ADDRESS);
                DaoManager.getInstance().getAddressDao().deleteAddress(address);
                DaoManager.getInstance().getAddressDao().store();
                this.remove(KEY_I_INDEX);
                this.put(KEY_B_REFRESH_LIST, true);
                syncStops();
                put(KEY_V_CURRENT_RECENT_ADDRESSES, getRecentPlaces(true));
                break;
            }
            case ACTION_CANCEL_VALIDATING:
            {
                SyncStopsExecutor.getInstance().cancel();
                break;
            }
            case ACTION_CALL:
            {
                TnTelephonyManager.getInstance().startCall(getString(KEY_S_POI_PHONENUMBER));
                break;
            }
            case ACTION_BACKGROUND_SYNC:
            {
                isForegroundSync = false;
                syncStops();
                break;
            }
            case ACTION_CHECK_MIGRATION:
            {
                if(MigrationExecutor.getInstance().isSyncEnabled())
                {
                    MigrationExecutor.getInstance().doMigration(this);
                }
                break;
            }
            case ACTION_DELETE_CHECK:
            {
                int index = this.getInt(KEY_I_INDEX);
                if (index > -1)
                {
                    postModelEvent(EVENT_MODEL_DELETE_SINGLE_ADDRESS);
                }
                else
                {
                    postModelEvent(EVENT_MODEL_DELETE_ALL_ADDRESS);
                }
                break;
            }
        }
    }

    protected void init()
    {
        Vector receivedAddresses = DaoManager.getInstance().getAddressDao()
        .getFavorateAddresses(AddressDao.RECEIVED_ADDRESSES_CATEGORY, false);
        int size = receivedAddresses.size();
        for (int i = 0; i < size; i++)
        {
            Address address = (Address) receivedAddresses.elementAt(i);
            if (address.getStatus() == CommonDBdata.DELETED)
                continue;
            if(address.getType() == Address.TYPE_FAVORITE_POI || address.getType() == Address.TYPE_FAVORITE_STOP)
            {
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
                newAddress.setCreateTime(System.currentTimeMillis());
                DaoManager.getInstance().getAddressDao().addAddress(newAddress, true); 
                DaoManager.getInstance().getAddressDao().deleteAddress(address);
            }
        }
    }
    
    protected Vector getRecentPlaces(boolean needFilter)
    {
        Vector v = new Vector();
        Vector recentPlaces = DaoManager.getInstance().getAddressDao().getRecentAddresses();
        for (int i = 0; i < recentPlaces.size(); i++)
        {
            Address address = (Address) recentPlaces.elementAt(i);
            if (address.getStatus() == CommonDBdata.DELETED)
                continue;
            v.addElement(address);
        }
        
        if(needFilter)
        {
            v = AddressListFilter.getFilteredList(getString(KEY_S_SEARCH_TEXT),v, true);
        }
        
        SortUtil.getInstance().sortByTimeStamp(v, false);
        return v;
    }

    protected void transactionFinishedDelegate(AbstractServerProxy proxy, String jobId)
    {
        String action = proxy.getRequestAction();
        
        if(IServerProxyConstants.ACT_GET_EVENT_DETAIL.equals(action))
        {
            processGobyDetailFinished();
        }
        else
        {
            processSyncStopsFinished();
        }
    }

    protected void processGobyDetailFinished()
    {
        this.put(KEY_B_REFRESH_LIST, true);
        if (this.isActivated())
        {
            postModelEvent(EVENT_MODEL_UPDATE_VIEW);
        }
    }
    
    protected void processSyncStopsFinished()
    {
        this.put(KEY_B_REFRESH_LIST, true);
        put(KEY_V_CURRENT_RECENT_ADDRESSES, getRecentPlaces(true));
        if (this.isActivated())
        {
            postModelEvent(EVENT_MODEL_UPDATE_VIEW);
        }
    }
    
    public void transactionError(AbstractServerProxy proxy)
    {
        processSyncStopsError(proxy, (byte) -1, "", false);
    }

    public void networkError(AbstractServerProxy proxy, byte statusCode, String jobId)
    {
        processSyncStopsError(proxy, statusCode, jobId, true);
    }
    
    protected void processSyncStopsError(AbstractServerProxy proxy, byte statusCode, String jobId, boolean isNetworkError)
    {
        this.put(KEY_B_REFRESH_LIST, true);
        if (isForegroundSync)
        {
            String errorMessage = proxy == null ? "" : proxy.getErrorMsg();
            if (errorMessage == null || errorMessage.length() == 0)
            {
                errorMessage = ResourceManager.getInstance().getCurrentBundle().getString(IStringCommon.RES_SERVER_ERROR, IStringCommon.FAMILY_COMMON);
            }
            postErrorMessage(errorMessage);
        }
        else
        {
            postModelEvent(EVENT_MODEL_UPDATE_VIEW);
        }
    }
    
    protected void releaseDelegate()
    {
        ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).closeContextMenu();
        SyncStopsExecutor.getInstance().removeListener(this);
        NetworkStatusManager.getInstance().removeStatusListener(this);
        MapDownloadOnBoardDataStatusManager.getInstance().removeStatusChangeListener(this);
        super.releaseDelegate();
    }

    public void onMigrationFinished()
    {
        boolean isMigrationSucc = DaoManager.getInstance().getAddressDao().isMigrationSucc();
        
        if(isMigrationSucc)
        {
            processSyncStopsFinished();
        }
        else
        {
            processSyncStopsError(null, (byte)-1, "", false);
        }
    }

    @Override
    public void onLocalMapDataAvailabilityChanged(boolean isAvailable)
    {
        put(KEY_B_IS_NEED_RECREATE, true);
        postModelEvent(EVENT_MODEL_UPDATE_VIEW);
    }

    @Override
    public void statusUpdate(boolean isConnected)
    {
        put(KEY_B_IS_NEED_RECREATE, true);
        postModelEvent(EVENT_MODEL_UPDATE_VIEW);
    }

}
