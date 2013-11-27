/**
 *
 * Copyright 2013 TeleNav, Inc. All rights reserved.
 * JsonGobyProxy.java
 *
 */
package com.telenav.data.serverproxy.impl.json;

import java.util.Enumeration;
import java.util.Hashtable;

import org.json.tnme.JSONArray;
import org.json.tnme.JSONException;
import org.json.tnme.JSONObject;
import org.json.tnme.JSONTokener;

import com.telenav.comm.Comm;
import com.telenav.comm.Host;
import com.telenav.comm.ICommCallback;
import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.datatypes.address.Address;
import com.telenav.data.serializable.json.JsonSerializableManager;
import com.telenav.data.serverproxy.AbstractServerProxy;
import com.telenav.data.serverproxy.IServerProxyConstants;
import com.telenav.data.serverproxy.IServerProxyListener;
import com.telenav.data.serverproxy.IUserProfileProvider;
import com.telenav.data.serverproxy.impl.IGobyProxy;
import com.telenav.logger.Logger;

/**
 *@author yning
 *@date 2013-5-17
 */
public class JsonGobyProxy extends AbstractServerProxy implements IGobyProxy
{
    Hashtable recentTable;
    
    Hashtable favoriteTable;
    
    public JsonGobyProxy(Host host, Comm comm, IServerProxyListener serverProxyListener,
            IUserProfileProvider userProfileProvider)
    {
        super(host, comm, serverProxyListener, userProfileProvider);
    }

    @Override
    protected void handleResponse(byte[] response)
    {
        handleData(response);
    }

    protected void handleData(byte[] data)
    {
        try
        {
            if (data == null || data.length == 0)
            {
                this.status = ICommCallback.NO_DATA;
            }
            else
            {
                String response = new String(data);
                JSONTokener jsonTokener = new JSONTokener(response);
                JSONArray jsonArray = (JSONArray) jsonTokener.nextValue();
                parseGobyDetail(jsonArray);
                this.status = ICommCallback.SUCCESS;
            }
        }
        catch (Exception t)
        {
            this.status = ICommCallback.EXCEPTION_PARSE;
            Logger.log(this.getClass().getName(), t, "handleNode() -server proxy handle Node exception.");
        }
    }
    
    protected void parseGobyDetail(JSONArray jsonArray)
    {
        for(int i = 0; i < jsonArray.length(); i++)
        {
            try
            {
                JSONObject json = jsonArray.getJSONObject(i);
                if(json != null)
                {
                    Address address = JsonSerializableManager.getJsonInstance().getAddressSerializable()
                            .createAddress(json.toString().getBytes());
                    if (address != null)
                    {
                        long eventId = address.getEventId();
                        
                        if (eventId > 0)
                        {
                            if(favoriteTable != null && favoriteTable.containsKey(eventId))
                            {
                                Address favorite = (Address) favoriteTable.get(eventId);
                                if (favorite != null)
                                {
                                    DaoManager.getInstance().getAddressDao().checkEventInfo(favorite, address);
                                }
                            }
                            if(recentTable != null && recentTable.containsKey(eventId))
                            {
                                Address recent = (Address) recentTable.get(eventId);
                                if (recent != null)
                                {
                                    DaoManager.getInstance().getAddressDao().checkEventInfo(recent, address);
                                }
                            }
                        }
                    }
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
        
        DaoManager.getInstance().getAddressDao().store();
    }
    
    @Override
    public void requestGobyEventDetail(Hashtable favorite, Hashtable recent)
    {
        recentTable = recent;
        favoriteTable = favorite;
        
        sendData(null, IServerProxyConstants.ACT_GET_EVENT_DETAIL, (byte)1, 30000);
    }
    
    @Override
    protected Host getHostByAction(String action)
    {
        Host requestHost = null;
        if(IServerProxyConstants.ACT_GET_EVENT_DETAIL.equals(action))
        {
            Enumeration enum1 = favoriteTable.keys();
            Enumeration enum2 = recentTable.keys();
            
            StringBuilder sb = new StringBuilder();
            while(enum1.hasMoreElements())
            {
                sb.append(enum1.nextElement()).append(",");
            }
            
            while(enum2.hasMoreElements())
            {
                sb.append(enum2.nextElement()).append(",");
            }
            
            if(sb.length() > 0)
            {
                sb.deleteCharAt(sb.length() - 1);
            }
            
            String file = "v0/services/com/byledge/engine/services/FavoriteServices/getGobyFavoriteDetails?gobyIds=" + sb.toString() + "&responseType=JSON";
            requestHost = this.comm.getHostProvider().createHost("http", "api.scout.me", 0, file);
        }
        
        if (requestHost != null)
        {
            host = requestHost;
        }
        else
        {
            requestHost = this.host;
        }
        
        return requestHost;
    }
}
