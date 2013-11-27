/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * RecentModel.java
 *
 */
package com.telenav.module.ac.home;

import java.util.Vector;

import com.telenav.app.TeleNavDelegate;
import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.datatypes.address.Address;
import com.telenav.data.datatypes.address.Stop;
import com.telenav.data.serverproxy.AbstractServerProxy;
import com.telenav.data.serverproxy.IUserProfileProvider;
import com.telenav.data.serverproxy.impl.IToolsProxy;
import com.telenav.log.mis.IMisLogConstants;
import com.telenav.log.mis.MisLogManager;
import com.telenav.log.mis.log.AppStatusMisLog;
import com.telenav.module.preference.PreferenceController;
import com.telenav.module.sync.SyncResExecutor;
import com.telenav.mvc.AbstractCommonNetworkModel;
import com.telenav.mvc.AbstractController;
import com.telenav.res.IStringSetHome;
import com.telenav.res.ResourceManager;
import com.telenav.sdk.kontagent.KontagentLogger;
import com.telenav.ui.frogui.text.FrogTextHelper;

/**
 * @author wzhu (wzhu@telenav.cn)
 * @date 2010-12-3
 */
class HomeModel extends AbstractCommonNetworkModel implements IHomeConstants
{
    protected void doActionDelegate(int actionId)
    {
        switch (actionId)
        {
            case ACTION_INIT:
            {
                init();
                this.postModelEvent(EVENT_MODEL_LAUNCH_MAIN);
                break;
            }
            case ACTION_VALIDATE_INPUT:
            {
                doValidate();
                break;
            }
            case ACTION_SAVE_HOME:
            {
                if(DaoManager.getInstance().getAddressDao().getHomeAddress() == null)
                {
                    KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_SETUP_HOME, KontagentLogger.SETUP_HOMEWORK_SET_UP_FINISHED);
                }
                DaoManager.getInstance().getAddressDao().setHomeAddress(((Address)get(KEY_O_VALIDATED_ADDRESS)).getStop());
                DaoManager.getInstance().getAddressDao().store();
                backgroundSync();
                put(KEY_S_COMMON_MESSAGE,ResourceManager.getInstance().getCurrentBundle()
                    .getString(IStringSetHome.RES_LABEL_SUCCESS_MESSAGE, IStringSetHome.FAMILY_SET_HOME));
                postModelEvent(EVENT_MODEL_SHOW_TIMEOUT_MESSAGE);
                TeleNavDelegate.getInstance().callAppNativeFeature(
                		TeleNavDelegate.FEATURE_NOTIFY_REFRESH_WIDGET, null);
                break;
            }
            case ACTION_SAVE_WORK:
            {
                if(DaoManager.getInstance().getAddressDao().getOfficeAddress() == null)
                {
                    KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_SETUP_WORK, KontagentLogger.SETUP_HOMEWORK_SET_UP_FINISHED);
                }
                DaoManager.getInstance().getAddressDao().setOfficeAddress(((Address) get(KEY_O_VALIDATED_ADDRESS)).getStop());
                DaoManager.getInstance().getAddressDao().store();
                backgroundSync();
                put(KEY_S_COMMON_MESSAGE,ResourceManager.getInstance().getCurrentBundle()
                    .getString(IStringSetHome.RES_LABEL_SET_WORK_SUCCESS, IStringSetHome.FAMILY_SET_HOME));
                postModelEvent(EVENT_MODEL_SHOW_TIMEOUT_MESSAGE);
                TeleNavDelegate.getInstance().callAppNativeFeature(
                		TeleNavDelegate.FEATURE_NOTIFY_REFRESH_WIDGET, null);
                break;
            }
            case ACTION_SAVE_HOME_FROM_ONEBOX:
            {
                Address addr = (Address) get(KEY_O_SELECTED_ADDRESS);
                //Fix Cannon-171
                Stop stop = null;
                if(addr.getType() == Address.TYPE_RECENT_STOP)
                {
                    stop = addr.getStop();
                }
                else if(addr.getType() == Address.TYPE_RECENT_POI)
                {
                    stop = addr.getPoi().getStop();
                }
                if(DaoManager.getInstance().getAddressDao().getHomeAddress() == null)
                {
                    KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_SETUP_HOME, KontagentLogger.SETUP_HOMEWORK_SET_UP_FINISHED);
                }
                DaoManager.getInstance().getAddressDao().setHomeAddress(stop);
                DaoManager.getInstance().getAddressDao().store();
                backgroundSync();
                put(KEY_S_COMMON_MESSAGE,ResourceManager.getInstance().getCurrentBundle()
                    .getString(IStringSetHome.RES_LABEL_SUCCESS_MESSAGE, IStringSetHome.FAMILY_SET_HOME));
                postModelEvent(EVENT_MODEL_SHOW_TIMEOUT_MESSAGE);
                break;
            }
            case ACTION_SAVE_WORK_FROM_ONEBOX:
            {
                Address addr = (Address) get(KEY_O_SELECTED_ADDRESS);
                //Fix Cannon-171
                Stop stop = null;
                if(addr.getType() == Address.TYPE_RECENT_STOP)
                {
                    stop = addr.getStop();
                }
                else if(addr.getType() == Address.TYPE_RECENT_POI)
                {
                    stop = addr.getPoi().getStop();
                }
                if(DaoManager.getInstance().getAddressDao().getOfficeAddress() == null)
                {
                    KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_SETUP_WORK, KontagentLogger.SETUP_HOMEWORK_SET_UP_FINISHED);
                }
                DaoManager.getInstance().getAddressDao().setOfficeAddress(stop);
                DaoManager.getInstance().getAddressDao().store();
                backgroundSync();
                put(KEY_S_COMMON_MESSAGE,ResourceManager.getInstance().getCurrentBundle()
                    .getString(IStringSetHome.RES_LABEL_SET_WORK_SUCCESS, IStringSetHome.FAMILY_SET_HOME));
                postModelEvent(EVENT_MODEL_SHOW_TIMEOUT_MESSAGE);
                break;
            }
            case ACTION_CHECK_ADDRESS_TYPE:
            {
                int type = getInt(KEY_I_ADDRESS_TYPE);
                switch(type)
                {
                    case TYPE_HOME_ADDRESS:
                    {
                        postModelEvent(EVENT_MODEL_SAVE_HOME);
                        break;
                    }
                    case TYPE_WORK_ADDRESS:
                    {
                        postModelEvent(EVENT_MODEL_SAVE_WORK);
                        break;
                    }
                    default:
                    {
                        postModelEvent(EVENT_RETURN_ADDRESS);
                    }
                }
                break;
            }
            case ACTION_CHECK_CITY:
            {
                if (this.get(KEY_O_VALIDATED_ADDRESS) == null)
                {
                    this.postModelEvent(EVENT_MODEL_GO_TO_VALIDATE_ADDRESS);
                }
                else
                {
                    this.postModelEvent(EVENT_MODEL_RETURN_CITY_ADDRESS);
                }
                
                break;
            }
        }
    }

    private void backgroundSync()
    {
        IUserProfileProvider userProfileProvider = (IUserProfileProvider)get(KEY_O_USER_PROFILE_PROVIDER);
        SyncResExecutor.getInstance().syncPreference(null, IToolsProxy.SYNC_TYPE_UPLOAD, this, userProfileProvider, IToolsProxy.UPLOAD_TYPE_HOME_WORK);
    }
    
    private void init()
    {
       setupSearchList();
       
       setupHint();
       
       setPreferenceFlag();
    }

    private void setPreferenceFlag()
    {
        boolean needProfile = true;
        AbstractController controller = AbstractController.getCurrentController();
        if (controller != null)
        {
            AbstractController supperController = controller.getSuperController();
            if (supperController instanceof PreferenceController)
            {
                needProfile = false;
            }
        }
        this.put(KEY_B_NEED_PREFERENCE_MENU, needProfile);
    }
    
    private void setupSearchList()
    {
        Vector favorites = DaoManager.getInstance().getAddressDao().getFavorateAddresses();
        Vector recents = DaoManager.getInstance().getAddressDao().getRecentAddresses();
        Vector searchList = new Vector();
        Vector helpVector = new Vector();
        for(int i = 0; i < favorites.size(); i++)
        {
            Address favorite = (Address) favorites.elementAt(i);
            if(!helpVector.contains(ResourceManager.getInstance().getStringConverter().convertAddress(favorite.getStop(), false)))
            {
                searchList.addElement(favorite);
                helpVector.addElement(ResourceManager.getInstance().getStringConverter().convertAddress(favorite.getStop(), false));
            }
        }

        for(int i = 0 ; i < recents.size(); i++)
        {
            Address recent = (Address) recents.elementAt(i);
            if(!helpVector.contains(ResourceManager.getInstance().getStringConverter().convertAddress(recent.getStop(), false)))
            {
                searchList.addElement(recent);
                helpVector.addElement(ResourceManager.getInstance().getStringConverter().convertAddress(recent.getStop(), false));
            }
        }
        put(KEY_V_SEARCH_LIST, searchList);
        put(KEY_V_FILTER_SEARCH_LIST, searchList);
        put(KEY_V_NEAR_CITIES, DaoManager.getInstance().getNearCitiesDao().getNearCities(getRegion()));        
    }

    private void setupHint()
    {
        int type = this.getInt(KEY_I_ADDRESS_TYPE);
        
        String title = "";
        String streetLine = "";
        String cityLine = "";
        
        if (type == TYPE_HOME_ADDRESS)
        {
            title = FrogTextHelper.BOLD_START
                    + ResourceManager.getInstance().getCurrentBundle()
                            .getString(IStringSetHome.RES_LABEL_HOME_ADDRESS, IStringSetHome.FAMILY_SET_HOME) + FrogTextHelper.BOLD_END;
            if (DaoManager.getInstance().getAddressDao().getHomeAddress() != null)
            {
                Stop homeStop = DaoManager.getInstance().getAddressDao().getHomeAddress();
                streetLine = homeStop.getFirstLine();
                cityLine = ResourceManager.getInstance().getStringConverter().convertSecondLine(createDisplayStop(homeStop));
                this.put(KEY_B_FROM_LIST, true);
            }
        }
        else if (type == TYPE_WORK_ADDRESS)
        {
            title = FrogTextHelper.BOLD_START
                    + ResourceManager.getInstance().getCurrentBundle()
                            .getString(IStringSetHome.RES_LABEL_WORK_ADDRESS, IStringSetHome.FAMILY_SET_HOME) + FrogTextHelper.BOLD_END;
            if (DaoManager.getInstance().getAddressDao().getOfficeAddress() != null)
            {
                Stop workStop = DaoManager.getInstance().getAddressDao().getOfficeAddress();
                streetLine = workStop.getFirstLine();
                cityLine = ResourceManager.getInstance().getStringConverter().convertSecondLine(createDisplayStop(workStop));
                this.put(KEY_B_FROM_LIST, true);
            }
        }
        else
        {
            title = FrogTextHelper.BOLD_START
            + ResourceManager.getInstance().getCurrentBundle()
                    .getString(IStringSetHome.RES_LABEL_ADDRESS, IStringSetHome.FAMILY_SET_HOME) + FrogTextHelper.BOLD_END;
        }
        
        this.put(KEY_S_TITLE, title);
        this.put(KEY_S_STREET_INIT_TEXT, streetLine);
        this.put(KEY_S_CITY_INIT_TEXT, cityLine);
    }
    
    protected Stop createDisplayStop(Stop stop)
    {
        if (null == stop)
        {
            return null;
        }
        
        Stop displayStop = new Stop();
        displayStop.setCity(stop.getCity());
        displayStop.setProvince(stop.getProvince());
        return displayStop;
    }

    private void doValidate()
    {
        boolean fromFroglist = this.fetchBool(KEY_B_FROM_LIST);
        if(fromFroglist)
        {
            postModelEvent(EVENT_MODEL_VALIDATE_LIST_HOME);
        }
        else
        {
        	String address = getString(KEY_S_ADDRESS_LINE);
        	String district = getString(KEY_S_CITY);
        	
            if ((address == null || address.trim().length() == 0)
            		&& (district == null || district.trim().length() == 0))
            {
                put(KEY_S_ERROR_MESSAGE,
                    ResourceManager.getInstance().getCurrentBundle()
                            .getString(IStringSetHome.RES_LABEL_ADDRESS_NOT_SET, IStringSetHome.FAMILY_SET_HOME));
                postModelEvent(EVENT_MODEL_POST_ERROR);
            }
            else if(district == null || district.trim().length() == 0)
            {
                put(KEY_S_ERROR_MESSAGE,
                    ResourceManager.getInstance().getCurrentBundle()
                            .getString(IStringSetHome.RES_LABEL_CITY_NOT_SET, IStringSetHome.FAMILY_SET_HOME));
                postModelEvent(EVENT_MODEL_POST_ERROR);
            }
            else
            {
                postModelEvent(EVENT_MODEL_VALIDATE_HOME);
            }
        }
        AppStatusMisLog appStatusMisLog = (AppStatusMisLog) MisLogManager.getInstance().getMisLog(
            IMisLogConstants.TYPE_INNER_APP_STATUS);
        appStatusMisLog.setRouteRequestBy(IMisLogConstants.VALUE_ADDRESS_INPUT_TYPE_TYPE_IN);
    }

    protected void transactionFinishedDelegate(AbstractServerProxy proxy, String jobId)
    {
    
    }

    public void networkError(AbstractServerProxy proxy, byte statusCode, String jobId)
    {
        if(!(proxy instanceof IToolsProxy))
        {
            super.networkError(proxy, statusCode, jobId);
        }
    }

    public void transactionError(AbstractServerProxy proxy)
    {
        if(!(proxy instanceof IToolsProxy))
        {
            super.transactionError(proxy);
        }
    }

    
}
