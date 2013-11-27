/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * PoiDetailModel.java
 *
 */
package com.telenav.module.poi.detail;

import org.json.tnme.JSONArray;
import org.json.tnme.JSONException;
import org.json.tnme.JSONObject;

import com.telenav.data.datatypes.address.Address;
import com.telenav.data.serializable.json.JsonSerializableManager;
import com.telenav.data.serverproxy.IUserProfileProvider;
import com.telenav.htmlsdk.IHtmlSdkServiceListener;
import com.telenav.htmlsdk.ResultGenerator;
import com.telenav.logger.Logger;
import com.telenav.module.browsersdk.BrowserSdkModel;
import com.telenav.module.poi.PoiDataListener;
import com.telenav.module.poi.PoiDataRequester;
import com.telenav.module.poi.PoiDataWrapper;
import com.telenav.mvc.ICommonConstants;
import com.telenav.res.IStringCommon;
import com.telenav.res.IStringPoi;
import com.telenav.res.ResourceManager;
import com.telenav.res.converter.StringConverter;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2011-1-24
 */
public class PoiDetailModel extends BrowserSdkModel implements IPoiDetailConstants,PoiDataListener
{
    private PoiDataRequester poiDataRequester;
    protected void doActionDelegate(int actionId)
    {
        switch(actionId)
        {
            case ACTION_INIT:
            {
                break;
            }
            case ACTION_RELOAD_GALLERY:
            {
                this.put(KEY_IS_NEED_RELOAD_GALLERY, true);
                break;
            }
            case ACTION_GETTING_MORE_POIS:
            {
                requestMorePois();
                break;
            }
            case ACTION_CANCEL_GETTING_MORE_POIS:
            {
                cancelMorePoisRequest();
                break;
            }
            default:
                break;
        }
        super.doActionDelegate(actionId);
    }
    
    protected void requestMorePois()
    {
        PoiDataWrapper poiDataWrapper = (PoiDataWrapper) this.get(KEY_O_POI_DATA_WRAPPER);
        if (poiDataWrapper.isHasMorePoi())
        {
            if (!poiDataWrapper.isDoingRequest())
            {
                int size = poiDataWrapper.getNormalAddressSize();
                poiDataWrapper.setNormalPoiStartIndex(size);
                IUserProfileProvider userProfileProvider = (IUserProfileProvider) get(KEY_O_USER_PROFILE_PROVIDER);
                poiDataRequester = new PoiDataRequester(userProfileProvider);
                poiDataRequester.doRequestPoi(poiDataWrapper, this);
            }
        }
        else
        {
            postModelEvent(EVENT_MODEL_BACK_TO_MAIN);
        }
    }
    
    private void cancelMorePoisRequest()
    {
        if (poiDataRequester != null)
        {
            poiDataRequester.cancelSearchRequest();
        }

        PoiDataWrapper poiDataWrapper = (PoiDataWrapper) this.get(KEY_O_POI_DATA_WRAPPER);
        if (poiDataWrapper.isHasMorePoi())
        {
            poiDataWrapper.setIsDoingRequest(false);
        }

    }
    
    public void poiResultUpdate(int resultValue, int resultType, String msg, PoiDataWrapper poiDataWrapper)
    {
        switch (resultValue)
        {
            case PoiDataRequester.TYPE_NETWORK_ERROR:
            {
                this.put(KEY_B_IS_UPDATE_SUCCESS_FROM_GET_MORE_POIS, false);
                if (msg == null || msg.trim().length() == 0)
                {
                    msg = ResourceManager.getInstance()
                            .getCurrentBundle().getString(
                                    IStringCommon.RES_SERVER_ERROR,
                                    IStringCommon.FAMILY_COMMON);
                }
                postErrorMessage(msg);
                break;
            }
            case PoiDataRequester.TYPE_SUCCESS_NO_RESULTS:
            {
                this.put(KEY_B_IS_UPDATE_SUCCESS_FROM_GET_MORE_POIS, false);
                String errorMessage = ResourceManager.getInstance().getCurrentBundle().getString(IStringPoi.LABEL_NO_POIS,
                    IStringPoi.FAMILY_POI);
                String name = getString(KEY_S_COMMON_SHOW_SEARCH_TEXT);
                if (name == null)
                {
                    name = getString(KEY_S_COMMON_SEARCH_TEXT);
                }

                StringConverter converter = ResourceManager.getInstance().getStringConverter();
                errorMessage = converter.convert(errorMessage, new String[]
                { name });
                postErrorMessage(errorMessage);
                break;
            }
            case PoiDataRequester.TYPE_BAD_ARGS:
            {
                this.put(KEY_B_IS_UPDATE_SUCCESS_FROM_GET_MORE_POIS, false);
                String errorMessage = ResourceManager.getInstance().getCurrentBundle().getString(IStringPoi.LABEL_NO_POIS,
                    IStringPoi.FAMILY_POI);
                String name = getString(KEY_S_COMMON_SHOW_SEARCH_TEXT);
                if (name == null)
                {
                    name = getString(KEY_S_COMMON_SEARCH_TEXT);
                }

                StringConverter converter = ResourceManager.getInstance().getStringConverter();
                errorMessage = converter.convert(errorMessage, new String[]
                { name });
                postErrorMessage(errorMessage);
                break;
            }
            case PoiDataRequester.TYPE_SUCCESS:
            {
                this.put(KEY_B_IS_UPDATE_SUCCESS_FROM_GET_MORE_POIS, true);
                this.put(KEY_O_POI_DATA_WRAPPER, poiDataWrapper);
                this.postModelEvent(EVENT_MODEL_GET_MORE_POIS);
                this.put(KEY_IS_NEED_RELOAD_GALLERY, true);
                break;
            }
            
        }
    }
    
    public JSONObject doMap(JSONArray args, IHtmlSdkServiceListener listener)
    {
        int serviceId = ResultGenerator.SERVICE_ID_ZERO;
        JSONObject ret = null;
        try
        {
            serviceId = args.getInt(1);
            PoiDataWrapper dataWrapper = (PoiDataWrapper)this.get(KEY_O_POI_DATA_WRAPPER);
            if(dataWrapper != null)
            {
                if (args.get(0) != null)
                {
                    Address address = JsonSerializableManager.getJsonInstance().getAddressSerializable().createAddress(args.getJSONObject(0).toString().getBytes());
                    address.setSource(Address.SOURCE_SEARCH);
                    
                    int selectedIndex = address.getSelectedIndex();
                    if(selectedIndex < 0)
                    {
                        selectedIndex = 0;
                    }
                    dataWrapper.setSelectedIndex(selectedIndex);
                    if (dataWrapper.getShowText() != null)
                        this.put(ICommonConstants.KEY_S_COMMON_SHOW_SEARCH_TEXT, dataWrapper.getShowText());
                    this.put(ICommonConstants.KEY_O_SELECTED_ADDRESS, dataWrapper.getAddress(selectedIndex));
                    
                }
                this.postModelEvent(EVENT_MODEL_GOTO_MAP);
                
                ret = ResultGenerator.generateJsonResult(ResultGenerator.STATUS_SUCCESS, serviceId);
            }
            else
            {
                ret = ResultGenerator.generateJsonResult(ResultGenerator.STATUS_FAIL, serviceId);
            }
        }
        catch (JSONException e)
        {
            Logger.log(this.getClass().getName(), e);
            ret = ResultGenerator.generateJsonResult(ResultGenerator.STATUS_FAIL, serviceId);
        }

        return ret;
    }
    
   
    protected Address getAddress(int index)
    {
        if(index < 0)
        {
            return null;
        }
        Address addr = null;
        PoiDataWrapper poiDataWrapper = (PoiDataWrapper)this.get(KEY_O_POI_DATA_WRAPPER);
        if(poiDataWrapper != null)
        {
            if(index >= poiDataWrapper.getAddressSize())
            {
                addr = poiDataWrapper.getAddress(0);
            }
            else
            {
                addr = poiDataWrapper.getAddress(index);
                addr.setSelectedIndex(index);
            }
        }
        return addr;
    }
}
