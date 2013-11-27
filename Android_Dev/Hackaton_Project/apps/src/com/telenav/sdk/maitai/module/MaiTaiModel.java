/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * MaiTaiModel.java
 *
 */
package com.telenav.sdk.maitai.module;


import java.util.Hashtable;
import java.util.Vector;

import com.telenav.app.CommManager;
import com.telenav.app.ThreadManager;
import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.datatypes.address.Address;
import com.telenav.data.datatypes.address.Stop;
import com.telenav.data.serverproxy.AbstractServerProxy;
import com.telenav.data.serverproxy.IServerProxyConstants;
import com.telenav.data.serverproxy.IUserProfileProvider;
import com.telenav.data.serverproxy.impl.IAddressProxy;
import com.telenav.data.serverproxy.impl.IRgcProxy;
import com.telenav.data.serverproxy.impl.ServerProxyFactory;
import com.telenav.logger.Logger;
import com.telenav.module.entry.AbstractCommonEntryModel;
import com.telenav.sdk.maitai.IMaiTaiHandlerListener;
import com.telenav.sdk.maitai.IMaiTaiParameter;
import com.telenav.sdk.maitai.impl.MaiTaiHandler;
import com.telenav.sdk.maitai.impl.MaiTaiManager;
import com.telenav.sdk.plugin.PluginDataProvider;

/**
 *@author qli
 *@date 2010-12-2
 */
public class MaiTaiModel extends AbstractCommonEntryModel implements IMaiTaiConstants, IMaiTaiHandlerListener
{
    private Hashtable categoryTable = null;
    
    protected void doActionDelegate(int actionId)
    {
        super.doActionDelegate(actionId);
        switch (actionId)
        {
            case ACTION_CHECK_REGION_DETECTION_STATUS:
            {
                checkRegionDetectStatus();
                break;
            }
            case ACTION_CHECK_REGION_CHANGE:
            {
                checkRegionChange();
                break;
            }
            case ACTION_START_MAITAI:
            {
                doInit();
                break;
            }
            case ACTION_GOTO_MODULE:
            {
                boolean validate_directions = this.fetchBool(KEY_B_VALIDATE_TWO_ADDRESSES);
                boolean directions_orig = this.fetchBool(KEY_B_VALIDATE_DIRECTIONS_ORIG);
                Object addr = this.fetch(KEY_O_VALIDATED_ADDRESS);
                if( addr != null && addr instanceof Address )
                {
                    Address address = (Address)addr;
                    address.setSource(Address.SOURCE_API);
                    address.setLabel(MaiTaiManager.getInstance().getMaiTaiParameter().getLabel());
                    //XXX Fix bug TNANDROID-179
                    //XXX cserver just send stop data to backend when do SyncNewStop, so we need set label to stop
                    if(address.getStop() != null)
                    {
                        address.getStop().setLabel(address.getLabel());
                    }
                    if( validate_directions )
                    {
                        this.put(KEY_O_ADDRESS_ORI, address);
                        this.put(KEY_S_VALIDATE_ADDRESS, (String)this.fetchString(KEY_S_VALIDATE_DIRECTIONS_DEST));
                        this.postModelEvent(EVENT_MODEL_VALIDATE_ADDRESS);
                    }
                    else
                    {
                        String action = (String) get(KEY_S_MAITAI_ACTION);
                        if( directions_orig )
                        {
                            this.put(KEY_O_ADDRESS_ORI, address);
                        }
                        else if(IMaiTaiParameter.ACTION_DIRECTIONS.equals(action))
                        {
                            this.put(KEY_O_ADDRESS_DEST, address);
                        }
                        else
                        {
                            this.put(KEY_O_SELECTED_ADDRESS, address);
                        }
                        this.postModelEvent(EVENT_MODEL_HANDLE_MAITAI);
                    }
                    break;
                }
                MaiTaiManager.getInstance().finish();
                break;
            }
            case ACTION_JUMP_BACKGROUND:
            {
                doCancel();
                break;
            }
            case ACTION_RGC_ADDRESS:
            {            	
                Address address = (Address)get(KEY_O_MAITAI_RGC_ADDRESS);
                Stop stop = address.getStop();
        		IRgcProxy proxy = (IRgcProxy) ServerProxyFactory.getInstance().createRgcProxy(null, CommManager.getInstance().getComm(), this);
        		proxy.requestRgc(stop.getLat(), stop.getLon());
            	break;
            }
            case ACTION_GOTO_GET_ADDRESS_BY_ID:
            {
                String notificationAddressId = this.getString(KEY_S_NOTIFICATION_ADDRESS_ID);
                if( notificationAddressId != null && notificationAddressId.trim().length() > 0 )
                {
                    IUserProfileProvider userProfileProvider = (IUserProfileProvider)this.get(KEY_O_USER_PROFILE_PROVIDER);
                    IAddressProxy proxy = ServerProxyFactory.getInstance().createAddressProxy(null, CommManager.getInstance().getComm(), this, userProfileProvider, false);
                    proxy.fetchStopsById(IAddressProxy.NOTIFICATION_GET_STOP_BY_ID_TYPE, notificationAddressId);
                }
                break;
            }
        }
    }
    
    protected void activateDelegate(boolean isUpdateView)
    {
        boolean isHandle = this.fetchBool(KEY_B_MAITAI_GOTO_MODULE);
        if( isHandle && isUpdateView)
        {
            MaiTaiManager.getInstance().finish();
            return;
        }
    }
    
    protected void doInit()
    {
        MaiTaiHandler maitaiHandler = MaiTaiHandler.getInstance();
        maitaiHandler.setMaiTaiHandlerListener(this);
        ThreadManager.getPool(ThreadManager.TYPE_APP_ACTION).insertJobInFront(maitaiHandler);
    }
    
    
    protected void prepareMaiTaiData(boolean checkAddressId)
    {
        IMaiTaiParameter param = MaiTaiManager.getInstance().getMaiTaiParameter(); 
        String action = param.getAction();
        this.put(KEY_S_MAITAI_ACTION, action);
        boolean validate_address = false;
        boolean needRrgcAaddress = false;
        String addressLine = null;
        String directions_dest = null;
        
        boolean isFromNotification = false;
        String notificationAddressId = param.getAddrId();
        
        if(checkAddressId)
        {
            if (notificationAddressId != null && notificationAddressId.trim().length() != 0)
            {
                isFromNotification = true;
            }
        }
        
        if( IMaiTaiParameter.ACTION_NAVTO.equals(action) )
        {
            Stop dest = PluginDataProvider.getInstance().getStop()[0];
            Address addr = new Address(Address.SOURCE_API);
            addr.setStop(dest);
            if( dest.getLat() == 0 && dest.getLon() == 0 )
            {
                validate_address = true;
                addressLine = dest.getFirstLine();
            }
            else if ((dest.getFirstLine() == null || dest.getFirstLine().trim().length() == 0)
                    && (dest.getCity() == null || dest.getCity().trim().length() == 0)
                    && (dest.getProvince() == null || dest.getProvince().length() == 0))
            {
            	//if user hasn't given the firstline, we will send RGC request to get the firstline according to the lat and lon.
                put(KEY_O_MAITAI_RGC_ADDRESS, addr);
            	needRrgcAaddress = true;
            }
            else
            {
                this.put(KEY_O_SELECTED_ADDRESS, addr);
            }
        }
        else if( IMaiTaiParameter.ACTION_DIRECTIONS.equals(action) )
        {
            Stop origStop = PluginDataProvider.getInstance().getOrigStop();
            Address origAddr = new Address(Address.SOURCE_API);
            Stop destStop = PluginDataProvider.getInstance().getStop()[0];
            Address destAddr = new Address(Address.SOURCE_API);
            origAddr.setStop(origStop);
            destAddr.setStop(destStop);
            if( ( origStop.getLat() == 0 && origStop.getLon() == 0 ) )
            {
                addressLine = origStop.getFirstLine();
                this.put(KEY_B_VALIDATE_DIRECTIONS_ORIG, true);
                validate_address = true;
            }
            else
            {
                this.put(KEY_O_ADDRESS_ORI, origAddr);
            }
            if( ( destStop.getLat() == 0 && destStop.getLon() == 0 ) )
            {
                if( validate_address )
                {
                    directions_dest = destStop.getFirstLine();
                    this.put(KEY_B_VALIDATE_TWO_ADDRESSES, true);
                }
                else
                {
                    addressLine = destStop.getFirstLine();
                }
                validate_address = true;
            }
            else
            {
                this.put(KEY_O_ADDRESS_DEST, destAddr);
            }
        }
        else if( IMaiTaiParameter.ACTION_MAP.equals(action) )
        {
            if (isFromNotification)
            {
                this.put(KEY_S_NOTIFICATION_ADDRESS_ID, notificationAddressId);
            }
            else
            {
                Stop orig = PluginDataProvider.getInstance().getOrigStop();
                
                if( isValidAddress(orig.getFirstLine(), orig.getLat(), orig.getLon()) )
                {
                    Address addr = new Address(Address.SOURCE_API);
                    addr.setStop(orig);
                    if( orig.getLat() == 0 && orig.getLon() == 0 )
                    {
                        validate_address = true;
                        addressLine = orig.getFirstLine();
                    }
                    else
                    {
                        if(orig.getFirstLine() != null && orig.getFirstLine().trim().length() > 0)
                        {
                            this.put(KEY_O_SELECTED_ADDRESS, addr);
                        }
                        else
                        {
                            put(KEY_O_MAITAI_RGC_ADDRESS, addr);
                            needRrgcAaddress = true;
                        }
                    }
                }
                else
                {
                    Stop[] stops = PluginDataProvider.getInstance().getStop();
                    Address[] addrList = null;
                    if( stops != null )
                    {
                        addrList = new Address[stops.length];
                        for( int i=0 ; i<stops.length ; i++ )
                        {
                            addrList[i] = new Address(Address.SOURCE_API);
                            addrList[i].setStop(stops[i]);
                        }
                    }
                    this.put(KEY_O_SELECTED_ADDRESS, addrList);
                }
                
            }
            
        }
        else if( IMaiTaiParameter.ACTION_SEARCH.equals(action) )
        {
            this.put(KEY_S_COMMON_SEARCH_TEXT, PluginDataProvider.getInstance().getSearchTerm());
            Stop orig = PluginDataProvider.getInstance().getOrigStop();
            Address addr = new Address(Address.SOURCE_API);
            addr.setStop(orig);
            if( orig.getLat() == 0 && orig.getLon() == 0 )
            {
                validate_address = true;
                addressLine = orig.getFirstLine();
            }
            else
            {
                this.put(KEY_O_SELECTED_ADDRESS, addr);
            }
            
            String category = PluginDataProvider.getInstance().getSearchCat();
            if (category != null && category.length() > 0)
            {
                int catId = getCategoryIdByName(category);
                this.put(KEY_I_CATEGORY_ID, catId);
            }
        }
        else if( IMaiTaiParameter.ACTION_SET_ADDRESS.equals(action))
        {
            this.put(KEY_B_IS_SET_HOME, MaiTaiManager.getInstance().getMaiTaiParameter().isHomeAddress());
        }
        
        if( !MaiTaiHandler.getInstance().isCancelled() )
        {
            if (isFromNotification)
            {
                this.postModelEvent(EVENT_MODEL_GET_ADDRESS_BY_ID);
                return;
            }            
            if( validate_address )
            {
                if( addressLine != null )
                {
                    this.put(KEY_S_VALIDATE_ADDRESS, addressLine);
                }
                if( directions_dest != null )
                {
                    this.put(KEY_S_VALIDATE_DIRECTIONS_DEST, directions_dest);
                }
                this.postModelEvent(EVENT_MODEL_VALIDATE_ADDRESS);
            }
            else
            {
            	if(needRrgcAaddress)
            	{
            		this.postModelEvent(EVENT_MODEL_RGC_ADDRESS);
            	}
            	else
            	{
            		this.postModelEvent(EVENT_MODEL_HANDLE_MAITAI);
            	}
            }
        }
    }
    
    
    protected boolean isValidAddress(String address, int lat, int lon)
    {
        return address != null && address.length() > 0? true : (lat != 0 && lon != 0)? true : false;
    }
    
    
    protected void doCancel()
    {
        if( MaiTaiHandler.getInstance().isRunning() )
        {
            ThreadManager.getPool(ThreadManager.TYPE_APP_ACTION).cancelAll();
        }
        else
        {
            this.put(KEY_B_MAITAI_GOTO_MODULE, true);
        }
    }
    

    public void maitaiHandleFinished()
    {
        if( !MaiTaiHandler.getInstance().isCancelled() )
        {
            prepareMaiTaiData(true);
        }
        else
        {
            handleMaiTaiError();
        }
    }

    protected void handleMaiTaiError()
    {
        postModelEvent(EVENT_MODEL_START_MAITAI_ERROR);
    }
    
    protected void transactionFinishedDelegate(AbstractServerProxy proxy , String jobId)
    {
        if(proxy instanceof IRgcProxy)
        {
            Address address = ((IRgcProxy) proxy).getAddress();
            address.setSource(Address.SOURCE_API);
            put(KEY_O_SELECTED_ADDRESS, address);
            this.postModelEvent(EVENT_MODEL_HANDLE_MAITAI);
        }
        else if (proxy instanceof IAddressProxy)
        {
            if (IServerProxyConstants.ACT_SYNC_NEW_STOPS_BY_ID.equals(proxy.getRequestAction()))
            {
                Vector result = ((IAddressProxy) proxy).getNotificationVector();
                if (result.size() > 0)
                {
                    put(KEY_O_SELECTED_ADDRESS, result.elementAt(0));
                    postModelEvent(EVENT_MODEL_HANDLE_MAITAI);
                }
                else
                {
                    prepareMaiTaiData(false);
                }
            }
        }
    }

    public void transactionError(AbstractServerProxy proxy)
    {
        if (proxy instanceof IRgcProxy)
        {
        	Address address = (Address)get(KEY_O_MAITAI_RGC_ADDRESS);
        	put(KEY_O_SELECTED_ADDRESS, address);
        	this.postModelEvent(EVENT_MODEL_HANDLE_MAITAI);
        }
        else if (proxy instanceof IAddressProxy)
        {
            if (IServerProxyConstants.ACT_SYNC_NEW_STOPS_BY_ID.equals(proxy.getRequestAction()))
            {
                prepareMaiTaiData(false);
            }
        }
        else
        {
            super.transactionError(proxy);
        }
    }
    
    public int getCategoryIdByName(String name)
    {
        if (categoryTable == null)
        {
            int[] catIds = {2041,2040,241,374,50500,221,595,181,163,600,641,601,601};
            String[] catNames = {"food_coffee", "food", "coffee","atm","gas","grocery","lodging","movies","nightlife","parking","shopping","transportation","taxi"};
            
            categoryTable = new Hashtable();
            
            for (int i = 0; i<catNames.length; i++)
            {
                categoryTable.put(catNames[i].toLowerCase(), catIds[i]);
            }
            //update catIds from server driven for the following 5 categories.
            String[] catNames_server = {"coffee", "food", "gas", "parking","atm"};
            String poiQuickSearchList = DaoManager.getInstance().getServerDrivenParamsDao().getQuickSearchList(getRegion());
            if (poiQuickSearchList != null)
            {
                String[] pois = poiQuickSearchList.split(";");
                int size = Math.min(pois.length, catNames_server.length);
                if (size > 0)
                {
                    try
                    {
                        for (int index = 0; index < size; index++)
                        {
                            String[] poi = pois[index].split(",");
                            categoryTable.put(catNames_server[index].toLowerCase(), Integer.parseInt(poi[0]));
                        }
                    }
                    catch(Exception e)
                    {
                        Logger.log(this.getClass().getName(), e);
                    }
                    
                }
            }
        }
        
        Object objId = categoryTable.get(name.toLowerCase());
        if (objId instanceof Integer)
        {
            return ((Integer) objId).intValue();
        }
        
        return -1;
    }
}
