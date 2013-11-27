/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * BrowserSdkModel.java
 *
 */
package com.telenav.module.browsersdk;

import java.util.Enumeration;
import java.util.Vector;

import org.json.tnme.JSONArray;
import org.json.tnme.JSONException;
import org.json.tnme.JSONObject;

import com.telenav.app.CommManager;
import com.telenav.app.TeleNavDelegate;
import com.telenav.app.TnSdLogCollector;
import com.telenav.data.dao.misc.AndroidBillingDao;
import com.telenav.data.dao.misc.BrowserSdkServiceDao;
import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.dao.misc.PreferenceDao;
import com.telenav.data.dao.misc.SimpleConfigDao;
import com.telenav.data.dao.serverproxy.AbstractDaoManager;
import com.telenav.data.dao.serverproxy.MandatoryNodeDao;
import com.telenav.data.dao.serverproxy.ServerDrivenParamsDao;
import com.telenav.data.datatypes.address.Address;
import com.telenav.data.datatypes.address.CommonDBdata;
import com.telenav.data.datatypes.address.Stop;
import com.telenav.data.datatypes.billing.MarketPurchaseRequest;
import com.telenav.data.datatypes.mandatory.MandatoryProfile.ClientInfo;
import com.telenav.data.datatypes.mandatory.MandatoryProfile.CredentialInfo;
import com.telenav.data.datatypes.mandatory.MandatoryProfile.UserInfo;
import com.telenav.data.datatypes.poi.BillboardAd;
import com.telenav.data.datatypes.poi.BizPoi;
import com.telenav.data.datatypes.poi.Poi;
import com.telenav.data.datatypes.preference.Preference;
import com.telenav.data.serializable.IAddressSerializable;
import com.telenav.data.serializable.SerializableManager;
import com.telenav.data.serializable.json.JsonSerializableManager;
import com.telenav.data.serverproxy.AbstractServerProxy;
import com.telenav.data.serverproxy.IUserProfileProvider;
import com.telenav.data.serverproxy.impl.IClientLoggingProxy;
import com.telenav.data.serverproxy.impl.ISettingDataProxy;
import com.telenav.data.serverproxy.impl.ISyncCombinationResProxy;
import com.telenav.data.serverproxy.impl.ISyncPurchaseProxy;
import com.telenav.data.serverproxy.impl.ServerProxyFactory;
import com.telenav.htmlsdk.IHtmlSdkServiceHandler;
import com.telenav.htmlsdk.IHtmlSdkServiceListener;
import com.telenav.htmlsdk.ResultGenerator;
import com.telenav.location.TnLocation;
import com.telenav.log.mis.IMisLogConstants;
import com.telenav.log.mis.MisLogManager;
import com.telenav.log.mis.helper.PoiMisLogHelper;
import com.telenav.log.mis.log.AbstractMisLog;
import com.telenav.logger.Logger;
import com.telenav.module.AppConfigHelper;
import com.telenav.module.location.LocationProvider;
import com.telenav.module.marketbilling.IMarketBillingConstants;
import com.telenav.module.marketbilling.IMarketBillingListener;
import com.telenav.module.marketbilling.MarketBillingHandler;
import com.telenav.module.nav.NavAdManager;
import com.telenav.module.poi.PoiDataRequester;
import com.telenav.module.poi.PoiDataWrapper;
import com.telenav.module.preference.guidetone.GuideToneManager;
import com.telenav.module.sync.SyncResExecutor;
import com.telenav.module.upsell.FeaturesManager;
import com.telenav.mvc.AbstractCommonNetworkModel;
import com.telenav.mvc.ICommonConstants;
import com.telenav.res.IStringCommon;
import com.telenav.res.ResourceManager;
import com.telenav.sdk.kontagent.KontagentLogger;
import com.telenav.ui.UiFactory;
import com.telenav.util.URLEncoder;

/**
 *@author qli
 *@date 2010-12-29
 */
public class BrowserSdkModel extends AbstractCommonNetworkModel implements IBrowserSdkConstants, IHtmlSdkServiceHandler, IMarketBillingListener
{
    private static final String KEY_INTERNAL_VERSION = "internalVersion";

    /** The Constant PRIVATE_SERVICE_RETRIEVE_CURRENT_ADDRESS. */
    private final static String PRIVATE_SERVICE_RETRIEVE_CURRENT_ADDRESS = "RetrieveCurrentAddress";
    
    private final static String GET_POI_INFO = "getPOIInfo";

    /** The Constant PRIVATE_SERVICE_SAVE_TO_FAVORITE_DIRECTLY. */
    private final static String PRIVATE_SERVICE_SAVE_TO_FAVORITE_DIRECTLY = "SaveToFavoriteDirectly";
    
    private final static String SAVE_TO_FAV = "saveToFav";

    /** The Constant PRIVATE_SERVICE_SAVE_TO_FAVORITE_DIRECTLY. */
    private final static String PRIVATE_SERVICE_REMOVE_FROM_FAVORITE_DIRECTLY = "RemoveFromFavoriteDirectly";
    
    private final static String REMOVE_FROM_FAV = "removeFromFav";
    
    private final static String LOG_BI_EVENT = "logBIEvent";
    
    /** The Constant PRIVATE_SERVICE_SHARE_ADDRESS. */
    private final static String PRIVATE_SERVICE_SHARE_ADDRESS = "ShareAddress";
    
    private final static String SHARE_ADDRESS = "shareAddress";

    /** The Constant PRIVATE_SERVICE_SEARCH_NEARBY. */
    private final static String PRIVATE_SERVICE_SEARCH_NEARBY = "SearchNearBy";
    
    private final static String SEARCH_NEARBY = "searchNearBy";
    
    /** The Constant PRIVATE_SERVICE_CLEAR_HISTORY. */
    protected final static String PRIVATE_SERVICE_CLEAR_HISTORY = "clearHistory";
    
    /** The Constant PRIVATE_SERVICE_REGISTRATE_FINISH. */
    private final static String PRIVATE_SERVICE_REGISTRATE_FINISH = "RegistrateFinish";
    
    /** The Constant PRIVATE_SERVICE_LAUNCH_LOCALAPP. */    
    private final static String PRIVATE_SERVICE_LAUNCH_LOCALAPP = "launchLocalApp";
    
    /** The Constant PRIVATE_SERVICE_IS_FAVORITE_ADDRESS. */
    private final static String PRIVATE_SERVICE_IS_FAVORITE_ADDRESS = "IsFavoriteAddress";
    
    private final static String IS_FAVORITE = "isFavorite";
    
    /** The Constant PRIVATE_SERVICE_LAUNCH_SETTING. */
    private final static String PRIVATE_SERVICE_LAUNCH_SETTING = "LaunchSetting";
    
    /** The Constant PRIVATE_SERVICE_EXIT_APP. */
    private final static String PRIVATE_SERVICE_EXIT_APP = "LaunchCancel";
    
    /** The Constant PRIVATE_SERVICE_GET_HOST. */
    private final static String PRIVATE_SERVICE_GET_HOST = "GetHost";
    
    /** The Constant PRIVATE_SERVICE_REFRESH. */
    private final static String PRIVATE_SERVICE_REFRESH = "Refresh";
    
    private final static String REFRESH = "refresh";
    
    /** The Constant PRIVATE_SERVICE_LOCK_ORIENTATION. */
    private final static String PRIVATE_SERVICE_LOCK_ORIENTATION = "LockOrientation";
    
    /** The Constant PRIVATE_SERVICE_GET_ADSID. */
    private final static String PRIVATE_SERVICE_GET_ADSID = "GetAdsId";
    
    private final static String PRIVATE_SERVICE_GET_GL_BITMAP_SNAPSHOT = "GetPoiDetailMap";
    
    protected final static String PRIVATE_SERVICE_UPGRADE = "Upgrade";
    
    protected final static String PRIVATE_SERVICE_GET_FEEDBACK_POI_DATA = "getPoiDataForFeedback";
    
    protected final static String PRIVATE_SERVICE_POPUP_SHOW = "NotifyPopupShow";
    
    protected final static String PRIVATE_SERVICE_POPUP_CLOSE = "NotifyPopupClose";
    
    protected final static String PRIVATE_SERVICE_SD_CARD_LOG = "submitSdCardLog";
    
    protected final static String PRIVATE_NOTIFY_CLIENT = "notifyClient";
      
    protected final static String LAUNCH_PAGES = "launchPages";     

    private final static String KEY_MARKET_PURCHASE_STATUS_CODE = "statusCode";
    
    private final static String KEY_GLOBAL_ID = "globalId";
    
    
    /** The address capture listener. */
    private IHtmlSdkServiceListener addressCaptureListener;
    
    protected Object purchaseMutex = new Object();
    protected Object settingMutex = new Object();
    protected Object userSettingsMutex = new Object();
    protected Object marketPurchaseMutex = new Object();
    
    protected boolean isSyncPurchaseFail = false;
    protected boolean isSyncSettingFail = false;
    protected boolean isSyncUserSettingsFail = false;
    private String marketPurchaseStatus = IMarketBillingConstants.MARKET_PURCHASE_STATUS_CODE_COMMON_ERROR;
    private long lastPurchaseTime = -1;
   
    private final static String SYNC_PURCHASE_FEATURES = "FEATURES"; // sync both purchase and setting.
    private final static String SYNC_PURCHASE_SETTING = "SETTING";//only sync setting
    private final static String SYNC_PURCHASE_CODE = "CODES";//only sync purchase
    private final static String SYNC_PURCHASE_CAR_ICONS = "CAR_ICONS";
    private final static String SYNC_PURCHASE_GUIDE_TONES = "GUIDE_TONES";
    private final static String SYNC_REGISTRATE_FINISH = "INIT";
    
    private final static String STOP_FIRST_LINE = "firstLine";
    
    private final static String STOP_HOUSE_NUMBER = "houseNumber";

    private final static String STOP_SUITE = "suite";

    private final static String STOP_STREET = "street";

    private final static String STOP_CROSS_STREET = "crossStreet";

    private final static String STOP_CITY = "city";

    private final static String STOP_COUNTY = "county";

    private final static String STOP_STATE = "state";

    private final static String STOP_COUNTRY = "country";

    private final static String STOP_POSTALCODE = "postalCode";

    private final static String STOP_SUBLOCALITY = "subLocality";

    private final static String STOP_LOCALITY = "locality";

    private final static String STOP_LOCALE = "locale";

    private final static String STOP_SUBSTREET = "subStreet";

    private final static String STOP_BUILDING_NAME = "buildingName";
    
    private final static String STOP_ADDRESS_ID = "addressId";
    
    private final static String STOP_LAT = "lat";
    
    private final static String STOP_LON = "lon";
    
    private final static String MODE_LANDSCAPE = "landscape";
    
    private final static String MODE_PORTRAIT = "portrait";
    
   public static final String  ENTITY_TYPE_POI = "POI";
    
    public static final String ENTITY_TYPE_ADDR = "ADDR";
    
    private static String[] ALL_FIELDS =
    { STOP_FIRST_LINE, STOP_HOUSE_NUMBER, STOP_SUITE, STOP_STREET, STOP_CROSS_STREET,
            STOP_CITY, STOP_COUNTY, STOP_STATE, STOP_COUNTRY, STOP_POSTALCODE,
            STOP_SUBLOCALITY, STOP_LOCALITY, STOP_LOCALE, STOP_SUBSTREET,
            STOP_BUILDING_NAME, STOP_ADDRESS_ID, STOP_LAT, STOP_LON};
    
    private final static String JSON_VALUE_FAV = "favorite";
    private final static String JSON_VALUE_RECENT = "recent";
    private final static String JSON_VALUE_RECEIVED = "received";
    
    
    private static int mock_poi_id = IAddressSerializable.MOCK_POI_ID_TOP;
    
    private boolean isFromPoiDetailFeedback = false;
    /**
     * Instantiates a new browser sdk model.
     */
    public BrowserSdkModel()
    {
    }

    /* (non-Javadoc)
     * @see com.telenav.mvc.AbstractCommonModel#doActionDelegate(int)
     */
    protected void doActionDelegate(int actionId)
    {
        switch (actionId)
        {
            case ACTION_INIT:
            {
                this.postModelEvent(EVENT_MODEL_LAUNCH_BROWSER);
                break;
            }
            case ACTION_AC_ADDRESS:
            {
                setAcAddress(this.fetch(KEY_O_SELECTED_ADDRESS));
                break;
            }
        }
    }

    /**
     * Sets the ac address.
     *
     * @param addr the new ac address
     */
    protected void setAcAddress(Object addr)
    {
        Address address = null;
        if (addr != null && addr instanceof Address)
        {
            address = (Address) addr;
            Logger.log(Logger.INFO, this.toString(), " " + address.getStop().getFirstLine());

            if (addressCaptureListener != null)
            {
                JSONObject result = new JSONObject();
                try
                {
                    result.put(IHtmlSdkServiceListener.CALLBACK_TYPE, IHtmlSdkServiceListener.CALLBACK_TYPE_ADDRESSCAPTURE);
                    JSONObject addressObject = new JSONObject(new String(JsonSerializableManager.getJsonInstance().getAddressSerializable().toBytes(address)));
                    result.put(IHtmlSdkServiceListener.CALLBACK_VALUE, addressObject);
                }
                catch (JSONException e)
                {
                    Logger.log(this.getClass().getName(), e);
                }

                addressCaptureListener.callbackHandlerResponse(result);
                addressCaptureListener = null;
            }
        }
        else
        {
            Logger.log(Logger.INFO, this.toString(), "address is null or not address !!!");
        }

    }

    /* (non-Javadoc)
     * @see com.telenav.htmlsdk.api.IHtmlSdkServiceHandler#doAddressBook(org.json.tnme.JSONArray, com.telenav.htmlsdk.api.IHtmlSdkServiceListener)
     */
    public JSONObject doAddressBook(JSONArray args, IHtmlSdkServiceListener listener)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.telenav.htmlsdk.api.IHtmlSdkServiceHandler#doAddressCapture(org.json.tnme.JSONArray, com.telenav.htmlsdk.api.IHtmlSdkServiceListener)
     */
    public JSONObject doAddressCapture(JSONArray args, IHtmlSdkServiceListener listener)
    {
        this.postModelEvent(EVENT_MODEL_GOTO_AC);
        addressCaptureListener = listener;
        return null;
    }

    /*
     * ssoToken is the final result
     * 
     * @see
     * com.telenav.htmlsdk.api.IHtmlSdkServiceHandler#getTeleNavToken(com.telenav.htmlsdk.api.IHtmlSdkServiceListener)
     */
    // FIXME listener is not used
    public JSONObject getTeleNavToken(JSONArray args, IHtmlSdkServiceListener listener)
    {        
        JSONObject ret;
        int serviceId = ResultGenerator.SERVICE_ID_ZERO;
        try 
        {
            serviceId = args.getInt(0);
            
            MandatoryNodeDao mandatoryNodeDao = AbstractDaoManager.getInstance().getMandatoryNodeDao();
            String tokenString = mandatoryNodeDao.getMandatoryNode().getUserInfo().ssoToken;
            if (tokenString != null)
            {
                ret = ResultGenerator.generateJsonResult(ResultGenerator.STATUS_SUCCESS, serviceId, tokenString);
            }
            else
            {
                ret = ResultGenerator.generateJsonResult(ResultGenerator.STATUS_FAIL, serviceId);
            }
        }
        catch (JSONException e) 
        {
            e.printStackTrace();
            ret = ResultGenerator.generateJsonResult(ResultGenerator.STATUS_FAIL, serviceId);
        }
        
        return ret;
    }

    /* (non-Javadoc)
     * @see com.telenav.htmlsdk.api.IHtmlSdkServiceHandler#syncPreference(com.telenav.htmlsdk.api.IHtmlSdkServiceListener)
     */
    public JSONObject syncPurchase(JSONArray args,IHtmlSdkServiceListener arg0)
    {
        this.postModelEvent(EVENT_MODEL_SYNC_PURCHASE_STATUS);
        
        JSONObject ret = new JSONObject();
		
        try
        {
            String purchaseType = args.getString(0);
            Logger.log(Logger.INFO, this.getClass().getName(), "MarketBilling: syncPurchase purchaseType " + purchaseType);
            if(SYNC_PURCHASE_CODE.equals(purchaseType))
            {
                syncPurchaseStatus();
                if ( isSyncPurchaseFail )
                {
                    this.postErrorMessage(ResourceManager.getInstance().getCurrentBundle()
                        .getString(IStringCommon.RES_SERVER_ERROR, IStringCommon.FAMILY_COMMON));
                    return ret;
                }
            }
            else if(SYNC_PURCHASE_SETTING.equals(purchaseType))
            {
                syncSettingData();
                
                if( isSyncSettingFail )
                {
                    this.postErrorMessage(ResourceManager.getInstance().getCurrentBundle()
                        .getString(IStringCommon.RES_SERVER_ERROR, IStringCommon.FAMILY_COMMON));
                    return ret;
                }
            }
            else if(SYNC_PURCHASE_FEATURES.equals(purchaseType))
            {
                syncPurchaseStatus();
                
                if ( isSyncPurchaseFail )
                {
                    this.postErrorMessage(ResourceManager.getInstance().getCurrentBundle()
                        .getString(IStringCommon.RES_SERVER_ERROR, IStringCommon.FAMILY_COMMON));
                    return ret;
                }
                
                syncSettingData();
                
                if( isSyncSettingFail )
                {
                    this.postErrorMessage(ResourceManager.getInstance().getCurrentBundle()
                        .getString(IStringCommon.RES_SERVER_ERROR, IStringCommon.FAMILY_COMMON));
                    return ret;
                }
                
                syncUserSettings();
                if( isSyncUserSettingsFail )
                {
                    this.postErrorMessage(ResourceManager.getInstance().getCurrentBundle()
                        .getString(IStringCommon.RES_SERVER_ERROR, IStringCommon.FAMILY_COMMON));
                    return ret;
                }
                
            }
            else if(SYNC_PURCHASE_CAR_ICONS.equals(purchaseType))
            {
                Logger.log(Logger.INFO, this.getClass().getName(), "sync purchase car icons...");
            }
            else if(SYNC_PURCHASE_GUIDE_TONES.equals(purchaseType))
            {
                Logger.log(Logger.INFO, this.getClass().getName(), "sync purchase guide tones...");
            }
            else if(SYNC_REGISTRATE_FINISH.equals(purchaseType))
            {
                boolean isRegistrateSucc = false;
                if(args.get(1) instanceof JSONObject)
                {
                    JSONObject jsonObject = args.getJSONObject(1);
                    
                    //FIXME: db, hard code here
                    if(jsonObject.get("isPurchase") != null && jsonObject.get("isPurchase").equals("true"))
                        isRegistrateSucc = true;
                }
                
                doRegisterFinish(isRegistrateSucc);
                ret.put(KEY_RETURN_RESULT, true);  
                return ret;
            }
//            //sync guide tone
//            GuideToneManager.getInstance().loadGuideTone();
//            if( GuideToneManager.getInstance().hasError() )
//            {
//                this.postErrorMessage(ResourceManager.getInstance().getCurrentBundle()
//                    .getString(IStringCommon.RES_SERVER_ERROR, IStringCommon.FAMILY_COMMON));
//                return ret;
//            }
//            
//            //sync car model
//            CarModelManager.getInstance().loadCarModel();
//            if( CarModelManager.getInstance().hasNetWorkError() )
//            {
//                this.postErrorMessage(ResourceManager.getInstance().getCurrentBundle()
//                    .getString(IStringCommon.RES_SERVER_ERROR, IStringCommon.FAMILY_COMMON));
//                return ret;
//            }
//            else if( CarModelManager.getInstance().hasDataError() )
//            {
//                this.postErrorMessage(ResourceManager.getInstance().getCurrentBundle()
//                    .getString(IStringCommon.RES_SERVER_ERROR, IStringCommon.FAMILY_COMMON));
//                return ret;
//            }
            
            ret.put(KEY_RETURN_RESULT, true);  
//          this.postModelEvent(EVENT_MODEL_SYNC_PURCHASE_STATUS_DONE);
//          return ret;
        } catch (JSONException e) {
		    Logger.log(this.getClass().getName(), e);
		}
        
        this.postModelEvent(EVENT_MODEL_SYNC_PURCHASE_STATUS_DONE);
        return ret;
    }

    protected void syncSettingData()
    {
        isSyncSettingFail = false;
        
        Vector combination = new Vector();
        combination.addElement(ISyncCombinationResProxy.TYPE_PREFERENCE_SETTING);
        IUserProfileProvider userProfileProvider = (IUserProfileProvider)get(KEY_O_USER_PROFILE_PROVIDER);
        SyncResExecutor.getInstance().syncCombinationResource(combination, this, userProfileProvider);
        
        synchronized (settingMutex)
        {
            try
            {
                settingMutex.wait(5 * 60 * 1000);
            }
            catch (Exception e)
            {
            }
        }        
    }
    
    protected void syncPurchaseStatus()
    {
        isSyncPurchaseFail = false;
        
        IUserProfileProvider userProfileProvider = (IUserProfileProvider)get(KEY_O_USER_PROFILE_PROVIDER);
        ISyncPurchaseProxy syncPurchaseProxy = ServerProxyFactory.getInstance().createSyncPurchaseProxy(null,
            CommManager.getInstance().getComm(), this, userProfileProvider);
        syncPurchaseProxy.sendSyncPurchaseRequest(FeaturesManager.APP_CODE);
        
        synchronized (purchaseMutex)
        {
            try
            {
                purchaseMutex.wait(5 * 60 * 1000);
            }
            catch (Exception e)
            {
            }
        }        
    }


    protected void syncUserSettings()
    {
    	isSyncUserSettingsFail = false;
        
    	IUserProfileProvider userProfileProvider = (IUserProfileProvider)get(KEY_O_USER_PROFILE_PROVIDER);
         SyncResExecutor.getInstance().syncSettingData(null, userProfileProvider, this, ISettingDataProxy.SYNC_TYPE_DOWNLOAD);
         
         synchronized (userSettingsMutex)
         {
             try
             {
            	 userSettingsMutex.wait(5 * 60 * 1000);
             }
             catch (Exception e)
             {
             }
         }        
    }
    /* (non-Javadoc)
     * @see com.telenav.htmlsdk.api.IHtmlSdkServiceHandler#getTNInfo(com.telenav.htmlsdk.api.IHtmlSdkServiceListener)
     */
    public JSONObject getTNInfo(JSONArray args, IHtmlSdkServiceListener listener)
    {
        int serviceId = ResultGenerator.SERVICE_ID_ZERO;
        try 
        {
            serviceId = args.getInt(0);
        }
        catch (JSONException e) 
        {
            e.printStackTrace();
        }
        
        return ResultGenerator.generateJsonResult(ResultGenerator.STATUS_SUCCESS, serviceId, getTNInfo("").toString());
    }
    
    public static String handleQuestionMark(String url)
    {
        if ( url != null && url.indexOf("?") < 0 )
        {
            url += "?";
        }
        return url;
    }
    
    public static String handleAndMark(String url)
    {
        if ( url != null && !url.endsWith("?") && !url.endsWith("&") )
        {
            url += "&";
        }
        return url;
    }
   
    
    private static String getEncodeTnInfo(String region)
    {
        String tnInfo = getTNInfo(region).toString();
        try
        {
            tnInfo = URLEncoder.encode(tnInfo, "UTF-8");
        }
        catch (Exception e)
        {
            tnInfo = getTNInfo(region).toString();
        }
        return tnInfo;
    }
    
    public static String addEncodeTnInfo(String url, String region)
    {
        url = handleQuestionMark(url);
        url = handleAndMark(url);
        
        String clientInfo = getEncodeTnInfo(region);
        url += "clientInfo=" + clientInfo;
        return url;
    }
    
    public static String appendWidthHeightToUrl(String url)
    {
        url = url + "&width=" + AppConfigHelper.getScreenWidthForBrowser();
        url = url + "&height=" + AppConfigHelper.getScreenHeightForBrowser();
        
        return url;
    }
    
    private static JSONObject getTNInfo(String region)
    {
        ClientInfo client = DaoManager.getInstance().getMandatoryNodeDao().getMandatoryNode().getClientInfo();
        UserInfo userInfo = DaoManager.getInstance().getMandatoryNodeDao().getMandatoryNode().getUserInfo();

        JSONObject tnInfo = new JSONObject();

        try
        {
            tnInfo.put(TNINFO_PROGRAMCODE, client.programCode);
            tnInfo.put(TNINFO_PLATFORM, client.platform);
            tnInfo.put(TNINFO_VERSION, client.version);
            tnInfo.put(TNINFO_BUILDNUMBER, client.buildNumber);
            tnInfo.put(TNINFO_DEVICE, client.device);
            tnInfo.put(TNINFO_GPSTYPE, client.gpsTpye);
            tnInfo.put(TNINFO_PRODUCTTYPE, client.productType);
            tnInfo.put(TNINFO_USER_LOCALE, userInfo.locale);
            tnInfo.put(TNINFO_USER_REGION, "".equalsIgnoreCase(region)?userInfo.region:region);
            
            String deviceCarrier = replaceSymbol(client.deviceCarrier, "&", "%26");
            deviceCarrier = replaceSymbol(client.deviceCarrier, "/", "%2F");
            tnInfo.put(TNINFO_DEVICECARRIER, deviceCarrier);
            
            int deviceWidth = AppConfigHelper.getScreenWidthForBrowser();
            int deviceHeight = AppConfigHelper.getScreenHeightForBrowser();
            tnInfo.put("deviceWidth", deviceWidth); 
            tnInfo.put("deviceHeight", deviceHeight); 

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        
        return tnInfo;
    }

    
    protected static String replaceSymbol(String deviceCarrier, String symbol, String replaceStr) 
    {
    	if(deviceCarrier == null || deviceCarrier.length() == 0 || symbol == null || symbol.length() == 0)
    		return "";
    	
    	int index = deviceCarrier.indexOf(symbol);
    	
    	String output = deviceCarrier;
    	
    	if(index >= 0)
    	{
    		String prefix = (String)deviceCarrier.subSequence(0, index);
    		
    		String suffix = (String)deviceCarrier.substring(index + 1);
    		
    		output = prefix + replaceStr;
    		
    		suffix = replaceSymbol(suffix, symbol, replaceStr);
    		
    		output += suffix;
    	}
    	
		return output;
	}

	/*
     * (non-Javadoc)
     * 
     * @see com.telenav.htmlsdk.api.IHtmlSdkServiceHandler#doNav(org.json.JSONArray,
     * com.telenav.htmlsdk.api.IHtmlSdkServiceListener)
     */
    public JSONObject doNav(JSONArray args, IHtmlSdkServiceListener listener)
    {
        int serviceId = ResultGenerator.SERVICE_ID_ZERO;
        JSONObject ret = null;
        
        try
        {
            serviceId = args.getInt(2);
            
            if (args.get(0) != null && !args.get(0).equals(""))
            {
                Address origAddress = JsonSerializableManager.getJsonInstance().getAddressSerializable().createAddress(args.getJSONObject(0).toString().getBytes());
                if(origAddress.getEventId() > 0)
                {
                    origAddress.setIsEventDataAvailable(true);
                }
                this.put(KEY_O_ADDRESS_ORI, origAddress);
            }
            if (args.get(1) != null)
            {
                Address destAddress = JsonSerializableManager.getJsonInstance().getAddressSerializable().createAddress(args.getJSONObject(1).toString().getBytes());
                if(destAddress.getEventId() > 0)
                {
                    destAddress.setIsEventDataAvailable(true);
                }
                this.put(KEY_O_ADDRESS_DEST, destAddress);
            }
                        
            this.postModelEvent(EVENT_MODEL_GOTO_NAV);
            ret = ResultGenerator.generateJsonResult(ResultGenerator.STATUS_SUCCESS, serviceId);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
            ret = ResultGenerator.generateJsonResult(ResultGenerator.STATUS_FAIL, serviceId);
        }
        
        return ret;
    }

    /*
     * @param args 0: address 1:callbackurl
     * 
     * @see com.telenav.htmlsdk.api.IHtmlSdkServiceHandler#doMap(org.json.JSONArray,
     * com.telenav.htmlsdk.api.IHtmlSdkServiceListener)
     */
    public JSONObject doMap(JSONArray args, IHtmlSdkServiceListener listener)
    {
        int serviceId = ResultGenerator.SERVICE_ID_ZERO;
        JSONObject ret = null;
        
        try
        {
            serviceId = args.getInt(1);
            
            if (args.get(0) != null)
            {
                Address address = JsonSerializableManager.getJsonInstance().getAddressSerializable().createAddress(args.getJSONObject(0).toString().getBytes());
                this.put(KEY_O_SELECTED_ADDRESS, address);
            }
                        
            this.postModelEvent(EVENT_MODEL_GOTO_MAP);
            
            ret = ResultGenerator.generateJsonResult(ResultGenerator.STATUS_SUCCESS, serviceId);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
            ret = ResultGenerator.generateJsonResult(ResultGenerator.STATUS_FAIL, serviceId);
        }

        return ret;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.telenav.htmlsdk.api.IHtmlSdkServiceHandler#getPersistentData(org.json.JSONArray,
     * com.telenav.htmlsdk.api.IHtmlSdkServiceListener)
     */
    // FIXME listener is not used
    public JSONObject getPersistentData(JSONArray args, IHtmlSdkServiceListener listener)
    {
    	
        BrowserSdkServiceDao browserDao = AbstractDaoManager.getInstance().getBrowserSdkServiceDao();
        JSONObject persistentData = new JSONObject();

        if (args.length() == 1)
        {
            try
            {
                String key = args.getString(0);
                Object data = browserDao.get(key);
                if (data != null && data instanceof byte[])
                {
                    String dataString = new String((byte[]) data, "UTF-8");
                    persistentData.put(KEY_RETURN_RESULT, dataString);
                }
            }
            catch (Throwable e)
            {
                return persistentData;
            }
        }
        return persistentData;
    }

    /*
     * @param args 0:key 1:value of persistent data 2:callbackurl
     * 
     * @see com.telenav.htmlsdk.api.IHtmlSdkServiceHandler#setPersistentData(org.json.JSONArray,
     * com.telenav.htmlsdk.api.IHtmlSdkServiceListener)
     */
    // FIXME listener is not used
    public JSONObject setPersistentData(JSONArray args, IHtmlSdkServiceListener listener)
    {

        // indicate the number of parameters passed by JavaScript
        int paramterNum = 3;

        BrowserSdkServiceDao browserDao = AbstractDaoManager.getInstance().getBrowserSdkServiceDao();
        JSONObject ret = new JSONObject();

        if (args.length() == paramterNum)
        {
            try
            {
                String key = args.getString(0);
                String persistentData = args.getString(1);
                byte[] byteValue = persistentData.getBytes(CHAR_SET);
                browserDao.set(key, byteValue);
                browserDao.store();
                ret.put(KEY_RETURN_RESULT, true);
            }
            catch (Exception e)
            {
                // FIXME handle the exception correctly
                e.printStackTrace();
                try
                {
                    ret.put(KEY_RETURN_RESULT, false);
                }
                catch (Exception e2)
                {
                    Logger.log(this.getClass().getName(), e2);
                }
            }
        }
        else
        {
            try
            {
                ret.put(KEY_RETURN_RESULT, false);
            }
            catch (Exception e2)
            {
                Logger.log(this.getClass().getName(), e2);
            }
        }

        return ret;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.telenav.htmlsdk.api.IHtmlSdkServiceHandler#deletePersistentData(org.json.JSONArray,
     * com.telenav.htmlsdk.api.IHtmlSdkServiceListener)
     */
    public JSONObject deletePersistentData(JSONArray args, IHtmlSdkServiceListener listener)
    {
        // TODO Auto-generated method stub
        // TODO Sample code
        BrowserSdkServiceDao browserDao = AbstractDaoManager.getInstance().getBrowserSdkServiceDao();
        JSONObject ret = new JSONObject();

        if (args.length() == 1)
        {
            try
            {
                String key = args.getString(0);
                browserDao.delete(key);
                browserDao.store();
                ret.put(KEY_RETURN_RESULT, true);
            }
            catch (JSONException e)
            {
                Logger.log(this.getClass().getName(), e);

                try
                {
                    ret.put(KEY_RETURN_RESULT, false);
                }
                catch (Exception e2)
                {
                    Logger.log(this.getClass().getName(), e2);
                }
            }
        }
        else
        {
            try
            {
                ret.put(KEY_RETURN_RESULT, false);
            }
            catch (Exception e2)
            {
                Logger.log(this.getClass().getName(), e2);
            }
        }
        return ret;
    }

    /*
     * @param args 0: preference type 1: preference value 2:callbackurl
     * 
     * @see com.telenav.htmlsdk.api.IHtmlSdkServiceHandler#setPreference(org.json.tnme.JSONArray,
     * com.telenav.htmlsdk.api.IHtmlSdkServiceListener)
     */
    public JSONObject setPreference(JSONArray args, IHtmlSdkServiceListener listener)
    {
        JSONObject ret = null;
        PreferenceDao prefDao = ((DaoManager) DaoManager.getInstance()).getPreferenceDao();
        // //////////set units////////////
        // int unit = 0;//FIXME set unit the correct value. It should be km or miles.
        // prefDao.setIntValue(Preference.ID_PREFERENCE_DISTANCEUNIT, unit);
        //        
        // ////////////set email////////////
        // String email = "";//FIXME set email the correct value.
        // prefDao.setStrValue(Preference.ID_PREFERENCE_TYPE_EMAIL, email);
        // indicate the number of parameters,
        int paramNum = 3;
        if (args.length() == paramNum)
        {
            int serviceId = ResultGenerator.SERVICE_ID_ZERO;
            try
            {
                serviceId = args.getInt(2);
                
                String preferenceType = args.getString(0);
                String preferenceValue = args.getString(1);
                
                if (preferenceType.equals(PREFERENCE_FIRSTNAME))
                {
                    prefDao.setStrValue(Preference.ID_PREFERENCE_TYPE_FIRSTNAME, preferenceValue);
                    prefDao.store(this.getRegion());
                    ret = ResultGenerator.generateJsonResult(ResultGenerator.STATUS_SUCCESS, serviceId);
                }
                else if (preferenceType.equals(PREFERENCE_LASTNAME))
                {
                    prefDao.setStrValue(Preference.ID_PREFERENCE_TYPE_LASTNAME, preferenceValue);
                    prefDao.store(this.getRegion());
                    ret = ResultGenerator.generateJsonResult(ResultGenerator.STATUS_SUCCESS, serviceId);
                }
                else if (preferenceType.equals(PREFERENCE_EMIAL))
                {
                    prefDao.setStrValue(Preference.ID_PREFERENCE_TYPE_EMAIL, preferenceValue);
                    prefDao.store(this.getRegion());
                    ret = ResultGenerator.generateJsonResult(ResultGenerator.STATUS_SUCCESS, serviceId);
                }
                else if (preferenceType.equals(PREFERENCE_NICKNAME))
                {
                    prefDao.setStrValue(Preference.ID_PREFERENCE_TYPE_USERNAME, preferenceValue);
                    prefDao.store(this.getRegion());
                    ret = ResultGenerator.generateJsonResult(ResultGenerator.STATUS_SUCCESS, serviceId);
                }
                else if (preferenceType.equals(PREFERENCE_GUIDETONE))
                {
                    if(GuideToneManager.getInstance().setWebGuideTone(preferenceValue))
                    {
                        this.postModelEvent(EVENT_MODEL_SYNC_PURCHASE_STATUS);
                        GuideToneManager.getInstance().loadGuideTone();
                        this.postModelEvent(EVENT_MODEL_SYNC_PURCHASE_STATUS_DONE);
                    }
                    ret = ResultGenerator.generateJsonResult(ResultGenerator.STATUS_SUCCESS, serviceId);
                }
                else if (preferenceType.equals(PREFERENCE_CARICON))
                {
                    if(this.isActivated())
                    {
//                        if(CarModelManager.getInstance().setWebCarModelName(preferenceValue))
//                        {
//                            this.postModelEvent(EVENT_MODEL_SYNC_PURCHASE_STATUS);
//                            CarModelManager.getInstance().loadCarModel();
//                            this.postModelEvent(EVENT_MODEL_SYNC_PURCHASE_STATUS_DONE);
//                            if(CarModelManager.getInstance().hasDataError() || CarModelManager.getInstance().hasNetWorkError())
//                            {
//                                ret = ResultGenerator.generateJsonResult(ResultGenerator.STATUS_FAIL, serviceId);
//                            }
//                            else
//                            {
//                                ret = ResultGenerator.generateJsonResult(ResultGenerator.STATUS_SUCCESS, serviceId);
//                            }
//                        }
//                        else
//                        {
                            ret = ResultGenerator.generateJsonResult(ResultGenerator.STATUS_SUCCESS, serviceId);
//                        }
                    }
                    else
                    {
                        ret = ResultGenerator.generateJsonResult(ResultGenerator.STATUS_FAIL, serviceId);
                    }
                }
                else if (preferenceType.equals(PREFERENCE_DIS_UNIT))
                {
                    if (preferenceValue.equals(PREFERENCE_DIS_UNIT_KM))
                    {
                        prefDao.setIntValue(Preference.ID_PREFERENCE_DISTANCEUNIT, IHtmlSdkServiceHandler.UNIT_KM);
                        prefDao.store(this.getRegion());
                        ret = ResultGenerator.generateJsonResult(ResultGenerator.STATUS_SUCCESS, serviceId);
                    }
                    else if (preferenceValue.equals(PREFERENCE_DIS_UNIT_MILE))
                    {
                        prefDao.setIntValue(Preference.ID_PREFERENCE_DISTANCEUNIT, IHtmlSdkServiceHandler.UNIT_MILE);
                        prefDao.store(this.getRegion());
                        ret = ResultGenerator.generateJsonResult(ResultGenerator.STATUS_SUCCESS, serviceId);
                    }
                }
            }
            catch (JSONException e)
            {
                Logger.log(this.getClass().getName(), e);
                ret = ResultGenerator.generateJsonResult(ResultGenerator.STATUS_FAIL, serviceId);
            }
        }
        
        return ret;
    }

    /*
     * @param args 0: preference type
     * 
     * @see com.telenav.htmlsdk.api.IHtmlSdkServiceHandler#getPreference(org.json.tnme.JSONArray,
     * com.telenav.htmlsdk.api.IHtmlSdkServiceListener)
     */
    public JSONObject getPreference(JSONArray args, IHtmlSdkServiceListener listener)
    {

        PreferenceDao prefDao = ((DaoManager) DaoManager.getInstance()).getPreferenceDao();
        JSONObject ret = null;
        if (args.length() == 2)
        {
            int serviceId = ResultGenerator.SERVICE_ID_ZERO;
            try
            {
                serviceId = args.getInt(1);
                String preferenceType = args.getString(0);
                
                if(preferenceType.equals(PREFERENCE_GUIDETONE))
                {
                    String guideToneValue = prefDao.getStrValue(Preference.ID_PREFERENCE_GUIDE_TONE);
                    if(guideToneValue == null || guideToneValue.length() <= 0)
                    {
                        guideToneValue = DaoManager.getInstance().getServerDrivenParamsDao().getValue(
                            ServerDrivenParamsDao.DEFAULT_GUIDE_TONE);
                    }
                    
                    ret = ResultGenerator.generateJsonResult(ResultGenerator.STATUS_SUCCESS, serviceId, guideToneValue);
                }
                else if (preferenceType.equals(PREFERENCE_CARICON))
                {
                    String carIcon = prefDao.getStrValue(Preference.ID_PREFERENCE_CAR_MODEL);
                    if(carIcon == null || carIcon.length() <= 0)
                    {
                        carIcon = DaoManager.getInstance().getServerDrivenParamsDao().getValue(
                            ServerDrivenParamsDao.DEFAULT_CAR_ICON);
                    }
                    
                    ret = ResultGenerator.generateJsonResult(ResultGenerator.STATUS_SUCCESS, serviceId, carIcon);
                }
                else if (preferenceType.equals(PREFERENCE_DIS_UNIT))
                {
                    int systemUnits = prefDao.getIntValue(Preference.ID_PREFERENCE_DISTANCEUNIT);
                    Logger.log(Logger.INFO, this.toString(), " getPreference\n systemUnits: " + systemUnits);
                    if (systemUnits == UNIT_KM)
                    {
                    	ret = ResultGenerator.generateJsonResult(ResultGenerator.STATUS_SUCCESS, serviceId, Integer.toString(UNIT_KM));
                    }
                    else if (systemUnits == UNIT_MILE)
                    {
                    	ret = ResultGenerator.generateJsonResult(ResultGenerator.STATUS_SUCCESS, serviceId, Integer.toString(UNIT_MILE));
                    }
                }
                else if (preferenceType.equals(PREFERENCE_EMIAL))
                {
                    String email = prefDao.getStrValue(Preference.ID_PREFERENCE_TYPE_EMAIL);
                    if(email == null || email.trim().length() == 0)
                    {
                        CredentialInfo credentialInfo = DaoManager.getInstance().getMandatoryNodeDao().getMandatoryNode().getCredentialInfo();
                        if(credentialInfo != null && credentialInfo.credentialValue != null && credentialInfo.credentialValue.trim().length() > 0)
                        {
                            email = credentialInfo.credentialValue;
                        }
                    }
                    
                    Logger.log(Logger.INFO, this.toString(), " getPreference\n email: " + email);                    
                    ret = ResultGenerator.generateJsonResult(ResultGenerator.STATUS_SUCCESS, serviceId, email);
                }
                else if (preferenceType.equals(PREFERENCE_NICKNAME))
                {
                    String username = prefDao.getStrValue(Preference.ID_PREFERENCE_TYPE_USERNAME);
                    ret = ResultGenerator.generateJsonResult(ResultGenerator.STATUS_SUCCESS, serviceId, username);
                }
                else if (preferenceType.equals(PREFERENCE_FIRSTNAME))
                {
                    String firstName = prefDao.getStrValue(Preference.ID_PREFERENCE_TYPE_FIRSTNAME);
                    ret = ResultGenerator.generateJsonResult(ResultGenerator.STATUS_SUCCESS, serviceId, firstName);
                }
                else if (preferenceType.equals(PREFERENCE_LASTNAME))
                {
                    String lastName = prefDao.getStrValue(Preference.ID_PREFERENCE_TYPE_LASTNAME);
                    ret = ResultGenerator.generateJsonResult(ResultGenerator.STATUS_SUCCESS, serviceId, lastName);
                }
                else if (preferenceType.equalsIgnoreCase(KEY_INTERNAL_VERSION))
                {
                    StringBuffer version=new StringBuffer();
                    version.append(AppConfigHelper.appVersion);
                    version.append(".");
                    version.append(AppConfigHelper.buildNumber);
                    ret = ResultGenerator.generateJsonResult(ResultGenerator.STATUS_SUCCESS, serviceId, version.toString());
                }
                else if(preferenceType.equalsIgnoreCase(KEY_GLOBAL_ID))
                {
                    CredentialInfo credentialInfo = DaoManager.getInstance().getMandatoryNodeDao().getMandatoryNode().getCredentialInfo();
                    String globalId = "";
                    if(credentialInfo != null)
                    {
                        globalId = credentialInfo.credentialID;
                    }
                    ret = ResultGenerator.generateJsonResult(ResultGenerator.STATUS_SUCCESS, serviceId, globalId);
                 }
            }
            catch (Exception e)
            {
                e.printStackTrace();
                ResultGenerator.generateJsonResult(ResultGenerator.STATUS_FAIL, serviceId);
            }
        }
        
        return ret;
    }

    /*
     * @param args index 0: mode value, there are two candidate values now: appstore, app. The window with appstore mode
     * will have the nav bar. app mode indicates the full screen mode.
     * 
     * @see com.telenav.htmlsdk.api.IHtmlSdkServiceHandler#setWindowMode(org.json.tnme.JSONArray,
     * com.telenav.htmlsdk.api.IHtmlSdkServiceListener)
     */
    public JSONObject setWindowMode(JSONArray args, IHtmlSdkServiceListener listener)
    {
        String mode = null;

        try
        {
            mode = args.getString(0);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }

        if (mode != null)
        {
            this.put(KEY_S_WINDOW_MODE, mode);
            this.postModelEvent(EVENT_MODEL_UPDATE_MAIN);
        }

        return null;
    }

    /*
     * args value 0: {long}event type 1: {JSONObject string}attributes
     * 
     * @see com.telenav.htmlsdk.api.IHtmlSdkServiceHandler#logEvent(org.json.tnme.JSONArray,
     * com.telenav.htmlsdk.api.IHtmlSdkServiceListener)
     */
    public JSONObject logEvent(JSONArray args, IHtmlSdkServiceListener listener)
    {
        JSONObject ret = null;
        
        if (args != null && args.length() == 3)
        {            
            int serviceId = ResultGenerator.SERVICE_ID_ZERO;
            try
            {
                serviceId = args.getInt(2);
                int eventType = args.getInt(0);
                JSONObject eventAttributes = args.getJSONObject(1);                

                if (MisLogManager.getInstance().getFilter().isTypeEnable(eventType))
                {
                    AbstractMisLog log = MisLogManager.getInstance().getFactory().createMisLog(eventType);

                    if (log != null)
                    {
                        Enumeration keys = eventAttributes.keys();
                        while (keys.hasMoreElements())
                        {
                            String eventKey = keys.nextElement().toString().trim();
                            Long key  = Long.parseLong(eventKey);
                            String value = eventAttributes.getString(eventKey);
                            if(value != null)
                            {
                                log.setAttribute(key, value);
                            }
                        }
                        Logger.log(Logger.INFO, this.getClass().getName(), IMisLogConstants.PROCESS_MISLOG, new Object[]
                        { log });
                        
                        ret = ResultGenerator.generateJsonResult(ResultGenerator.STATUS_SUCCESS, serviceId);
                    }
                    else
                    {
                        ret = ResultGenerator.generateJsonResult(ResultGenerator.STATUS_FAIL, serviceId);
                    }
                }
                else
                {
                    ret = ResultGenerator.generateJsonResult(ResultGenerator.STATUS_FAIL, serviceId);
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
                ret = ResultGenerator.generateJsonResult(ResultGenerator.STATUS_FAIL, serviceId);
            }
        }
        
        return ret;
    }

    /*
     * Empty method as this is deprecated for HTML 2.0 and above
     * This function is to provide some private services that would never be exposed to any 3rd party SDK user. It can
     * be synchronous and asynchronous implement mechanism.
     * 
     * @param args value 0: {String}service name 1: {JSONObject} parameters
     * 
     * @param listener
     * 
     * @return
     * 
     * @see com.telenav.htmlsdk.api.IHtmlSdkServiceHandler#doPrivateService(org.json.tnme.JSONArray,
     * com.telenav.htmlsdk.api.IHtmlSdkServiceListener)
     */
    public JSONObject doPrivateService(JSONArray args, IHtmlSdkServiceListener listener)
    {
        return null;
    }
    
    private void doSendClientLogging(byte[] clientLog)
    {
        if (clientLog == null || clientLog.length == 0)
        {
            return;
        }
        IUserProfileProvider userProfileProvider = (IUserProfileProvider)get(KEY_O_USER_PROFILE_PROVIDER);
        IClientLoggingProxy clientLoggingProxy = ServerProxyFactory
                .getInstance()
                .createClientLoggingProxy(null, CommManager.getInstance().getComm(), this, userProfileProvider);
        String fileName = DaoManager.getInstance().getMandatoryNodeDao()
                .getMandatoryNode().getClientInfo().device
                + "_"
                + DaoManager.getInstance().getMandatoryNodeDao().getMandatoryNode()
                        .getClientInfo().buildNumber;
        clientLoggingProxy.sendClientLogging(clientLog, fileName, "zip");
    }
    
    private String convertIllegalJsonString(String origin)
    {
        if (origin == null || origin.trim().length() == 0)
        {
            return "";
        }
        String result = "";
        result = replace("'", "%27", origin);
        result = replace("\\\"", "&quot;", result);
        result = replace("\\r\\n", "<br>", result);
        result = replace("\\n", "<br>", result);
        result = replace("\\/", "/", result);
        result = replace("\\t", "        ", result);
        result = replace("\\", " ", result);
        return result;
    }
    
    static String replace(String from, String to, String source)
    {
        if (source == null || from == null || to == null)
        {
            return null;
        }
        StringBuffer bf = new StringBuffer();
        int index = -1;
        while ((index = source.indexOf(from)) != -1)
        {
            bf.append(source.substring(0, index)).append(to);
            source = source.substring(index + from.length());
            index = -1;
        }
        bf.append(source);
        return bf.toString();
    }

    protected void doRegisterFinish(boolean isRegistrateSucc)
    {
        this.put(KEY_B_IS_REGISTRATE_SUCC, isRegistrateSucc);
        this.postModelEvent(EVENT_MODEL_UPSELL_FINISH);
    }

    protected Address getAddress(int index)
    {
        return null;
    }

    /*
     * This function returns home address set on the native client side
     * 
     * @param listener
     * 
     * @return
     * 
     * @see
     * com.telenav.htmlsdk.api.IHtmlSdkServiceHandler#getHomeAddress(com.telenav.htmlsdk.api.IHtmlSdkServiceListener)
     */
    public JSONObject getHomeAddress(IHtmlSdkServiceListener listener)
    {
        JSONObject homeAddr = new JSONObject();
        
    	try 
    	{
    		Stop home = DaoManager.getInstance().getAddressDao().getHomeAddress();
	        if( home != null )
	        {
	            Logger.log(Logger.INFO, this.toString(), " getHomeAddress: "+home.getFirstLine());
	            homeAddr = new JSONObject(new String(JsonSerializableManager.getJsonInstance().getAddressSerializable().toBytes(home)));
	        }
		} catch (JSONException e) {
				e.printStackTrace();
		}
        return homeAddr;
    }

	/**
	 * This function is used to exit local application
	 * @param args Boolean whether back to home screen, "true" or "false"
	 */
	public void exitBrowser(JSONArray args, IHtmlSdkServiceListener listener) 
	{
	    if(isFromPoiDetailFeedback)
	    {
	        KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_POI_DETAILS, KontagentLogger.POIINFO_FEEDBACK_SENT);
	        isFromPoiDetailFeedback = false;
	    }
	    this.postModelEvent(EVENT_MODEL_EXIT_BROWSER);
	}
	
	/**
	 * This function is used to launch settings 
	 */
	public void launchSettings(IHtmlSdkServiceListener listener)
	{
	    this.postModelEvent(EVENT_MODEL_LAUNCH_SETTING);
		Logger.log(Logger.INFO, this.toString(),"SDK Called LaunchSettings!");
	}
	
	/**
	 * Client native code will be called and passed the url.
	 * It is up to the client native code to close any open views and then launch the local app in the browser of it's choice.
	 * Note: if client wants to launch a local app in the same browser window, it just loads the URL.
	 * @param args the url to launch
	 */
    public void launchLocalApp(JSONArray args, IHtmlSdkServiceListener listener)
    {
        try
        {
            String url = args.getString(0);
            this.put(KEY_O_NEW_WINDOW_URL, url);
            this.postModelEvent(EVENT_MODEL_LAUNCH_LOCALAPP);
        }
        catch (JSONException e)
        {
            return;
        }
    }
    
	public void networkError(AbstractServerProxy proxy, byte statusCode, String jobId)
	{
	    if(proxy instanceof ISyncPurchaseProxy)
        {
	        isSyncPurchaseFail = true;
	        
            synchronized (purchaseMutex)
            {
                try
                {
                    purchaseMutex.notifyAll();
                }
                catch (Exception e)
                {
                }
            }
        }
	    else if(proxy instanceof ISyncCombinationResProxy)
	    {
	        isSyncSettingFail = true;
            
            synchronized (settingMutex)
            {
                try
                {
                    settingMutex.notifyAll();
                }
                catch (Exception e)
                {
                }
            }
	    }
	    else if (proxy instanceof ISettingDataProxy)
        {
	    	isSyncUserSettingsFail = true;
	    	
        	synchronized (userSettingsMutex)
            {
                try
                {
                	userSettingsMutex.notifyAll();
                }
                catch (Exception e)
                {
                }
            }
        }
	    else if (proxy instanceof IClientLoggingProxy)
        {
	        Logger.log(Logger.INFO, TnSdLogCollector.class.getName(), " Sending client network error!");
        }
	    else
	    {
	        super.networkError(proxy, statusCode, jobId);
	    }
	}
	
	public void transactionError(AbstractServerProxy proxy)
	{
	    if(proxy instanceof ISyncPurchaseProxy)
        {
            ISyncPurchaseProxy syncPurchaseProxy = (ISyncPurchaseProxy)proxy;
            if(syncPurchaseProxy.isNeedLogin())
            {
                handleAccountFatalError();
            }
	        
	        isSyncPurchaseFail = true;
	        
            synchronized (purchaseMutex)
            {
                try
                {
                    purchaseMutex.notifyAll();
                }
                catch (Exception e)
                {
                }
            }
        }
	    else if(proxy instanceof ISyncCombinationResProxy)
        {
            isSyncSettingFail = true;
            
            synchronized (settingMutex)
            {
                try
                {
                    settingMutex.notifyAll();
                }
                catch (Exception e)
                {
                }
            }
        }
	    else if (proxy instanceof ISettingDataProxy)
        {
	    	isSyncUserSettingsFail = true;
        	synchronized (userSettingsMutex)
            {
                try
                {
                	userSettingsMutex.notifyAll();
                }
                catch (Exception e)
                {
                }
            }
        }
	    else if (proxy instanceof IClientLoggingProxy)
	    {
	        Logger.log(Logger.INFO, TnSdLogCollector.class.getName(), " Sending client log failed!");
	    }
	    else
	    {
	        super.transactionError(proxy);
	    }
	}
	
    protected void transactionFinishedDelegate(AbstractServerProxy proxy , String jobId)
    {
        if(proxy instanceof ISyncPurchaseProxy)
        {
            ISyncPurchaseProxy syncPurchaseProxy = (ISyncPurchaseProxy)proxy;
            if(syncPurchaseProxy.isNeedLogin())
            {
                handleAccountFatalError();
            }
            
            synchronized (purchaseMutex)
            {
                try
                {
                    purchaseMutex.notifyAll();
                }
                catch (Exception e)
                {
                }
            }
        }
        else if(proxy instanceof ISyncCombinationResProxy)
        {
            synchronized (settingMutex)
            {
                try
                {
                    settingMutex.notifyAll();
                }
                catch (Exception e)
                {
                }
            }
        }
        else if (proxy instanceof ISettingDataProxy)
        {
        	synchronized (userSettingsMutex)
            {
                try
                {
                	userSettingsMutex.notifyAll();
                }
                catch (Exception e)
                {
                }
            }
        }
        else if (proxy instanceof IClientLoggingProxy)
        {
            Logger.log(Logger.INFO, TnSdLogCollector.class.getName(), " Sending client log succeed!");
        }
    }
    
	/* (non-Javadoc)
	 * @see com.telenav.htmlsdk.api.IHtmlSdkServiceHandler#getAddressonType(org.json.tnme.JSONArray, com.telenav.htmlsdk.api.IHtmlSdkServiceListener)
	 */
	public JSONObject getAddressByType(JSONArray args,
			IHtmlSdkServiceListener listener) {
		JSONObject addr = new JSONObject();
        
    	try 
    	{
    		if(args.get(0) instanceof String){
    			String addrType = args.getString(0);
    			
    			if(addrType != null && addrType.equals(TYPE_ADDRESS_HOME)){
		    		Stop home = DaoManager.getInstance().getAddressDao().getHomeAddress();
			        if( home != null && isValidAddress(home))
			        {
			            Logger.log(Logger.INFO, this.toString(), " getHomeAddress: "+home.getFirstLine());
			            addr = new JSONObject(new String(JsonSerializableManager.getJsonInstance().getAddressSerializable().toBytes(home)));
			        }
			    }
    			else if(addrType != null && addrType.equals(TYPE_ADDRESS_WORK)){
    				Stop work = DaoManager.getInstance().getAddressDao().getOfficeAddress();
			        if( work != null && isValidAddress(work))
			        {
			            Logger.log(Logger.INFO, this.toString(), " getWorkAddress: "+work.getFirstLine());
			            addr = new JSONObject(new String(JsonSerializableManager.getJsonInstance().getAddressSerializable().toBytes(work)));
			        }
    				
    			}
                else if (addrType != null && addrType.equals(TYPE_ADDRESS_HOME_ACE))
                {
                    Stop home = DaoManager.getInstance().getAddressDao().getHomeAddress();
                    if ( home != null && isValidAddress(home))
                    {
                        Logger.log(Logger.INFO, this.toString(), " getHomeAddress: "
                                + home.getFirstLine());
                        addr = convertStop(home, ALL_FIELDS);
                    }
                }
                else if (addrType != null && addrType.equals(TYPE_ADDRESS_WORK_ACE))
                {
                    Stop work = DaoManager.getInstance().getAddressDao()
                            .getOfficeAddress();
                    if ( work != null && isValidAddress(work) )
                    {
                        Logger.log(Logger.INFO, this.toString(), " getWorkAddress: "
                                + work.getFirstLine());
                        addr = convertStop(work, ALL_FIELDS);
                    }
                }
    		}
		} catch (JSONException e) {
				e.printStackTrace();
		}
    	
        return addr;
	}

	/* (non-Javadoc)
	 * @see com.telenav.htmlsdk.api.IHtmlSdkServiceHandler#setAddress(org.json.tnme.JSONArray, com.telenav.htmlsdk.api.IHtmlSdkServiceListener)
	 */
	public JSONObject setAddressByType(JSONArray args,
			IHtmlSdkServiceListener listener) {
		JSONObject ret = new JSONObject();
		
		try{
			if(args.get(0) instanceof String){
				String addrType = args.getString(0);
				
				if(addrType != null && addrType.equals(TYPE_ADDRESS_HOME)){
					if(args.get(1) instanceof JSONObject)
			        {
			            JSONObject jsonObject = args.getJSONObject(1);
			            Address address = JsonSerializableManager.getJsonInstance().getAddressSerializable().createAddress(jsonObject.toString().getBytes());
			            DaoManager.getInstance().getAddressDao().setHomeAddress(address.getStop());
			            DaoManager.getInstance().getAddressDao().store();
			            
		                TeleNavDelegate.getInstance().callAppNativeFeature(
		                		TeleNavDelegate.FEATURE_NOTIFY_REFRESH_WIDGET, null);

			            ret.put(KEY_RETURN_RESULT, true);
			            return ret;
			        }
				}
				else if(addrType != null && addrType.equals(TYPE_ADDRESS_WORK)){
					{
			            JSONObject jsonObject = args.getJSONObject(1);
			            Address address = JsonSerializableManager.getJsonInstance().getAddressSerializable().createAddress(jsonObject.toString().getBytes());
			            DaoManager.getInstance().getAddressDao().setOfficeAddress(address.getStop());
			            DaoManager.getInstance().getAddressDao().store();
			            
		                TeleNavDelegate.getInstance().callAppNativeFeature(
		                		TeleNavDelegate.FEATURE_NOTIFY_REFRESH_WIDGET, null);

			            ret.put(KEY_RETURN_RESULT, true);
			            return ret;
			        }
				}
			}
			
			ret.put(KEY_RETURN_RESULT, false);
		} catch(JSONException e){
			e.printStackTrace();
		}		
		
        return ret;
	}

	/* (non-Javadoc)
	 * @see com.telenav.htmlsdk.api.IHtmlSdkServiceHandler#getSyncPurchaseStatus(org.json.tnme.JSONArray, com.telenav.htmlsdk.api.IHtmlSdkServiceListener)
	 */
	public int getSyncPurchaseStatus(JSONArray args,
			IHtmlSdkServiceListener listener) {
		
		
		// TODO Auto-generated method stub
		return 0;
	}
	
	protected void releaseDelegate()
	{
	    super.releaseDelegate();
	    TeleNavDelegate.getInstance().setOrientation(TeleNavDelegate.ORIENTATION_UNSPECIFIED);
	}

    public JSONObject completeAppstorePurchase(JSONArray args,
            IHtmlSdkServiceListener listener)
    {
        JSONObject ret = new JSONObject();
        JSONObject statusCode = new JSONObject();
        
        try
        {
            long interval = System.currentTimeMillis() - lastPurchaseTime;
            if(interval >= 0 && interval < 3000)
            {
                statusCode.put(KEY_MARKET_PURCHASE_STATUS_CODE, IMarketBillingConstants.MARKET_PURCHASE_STATUS_CODE_USER_CANCLE);
                ret.put(KEY_RETURN_RESULT, statusCode);
                
                Logger.log(Logger.INFO, this.getClass().getName(), "MarketBilling: Click too frequently!");
                return ret;
            }
            
            JSONObject jElement = args.getJSONObject(0);
            final String productId = jElement.getString("productID");
            final String orderId = jElement.getString("orderID");
            Logger.log(Logger.INFO, this.getClass().getName(),
                "MarketBilling: completeAppstorePurchase Start"
                        + " productId: " + productId + " orderId: " + orderId);
            boolean lastPurchaseUncompleted = false;
            AndroidBillingDao androidBillingDao = DaoManager.getInstance().getAndroidBillingDao();
            if (androidBillingDao != null)
            {
                Vector pendingPurchases = androidBillingDao.getPurchaseRequests();
                if(pendingPurchases != null)
                {
                    for(int i = 0; i < pendingPurchases.size(); i++)
                    {
                        MarketPurchaseRequest request = (MarketPurchaseRequest) pendingPurchases.elementAt(i);
                        if (request != null && request.getStatus() == -1)
                        {
                            lastPurchaseUncompleted = true;
                            Logger.log(Logger.INFO, this.getClass().getName(), "MarketBilling: completeAppstorePurchase lastPurchaseUncompleted " + lastPurchaseUncompleted);
                            break;
                        }
                    }
                }
            }
            
            MarketBillingHandler handler = TeleNavDelegate.getInstance().getBillingHandler();
            if(handler != null && !lastPurchaseUncompleted)
            {
                handler.addMarketListener(this);
                handler.requestPurchase(productId, orderId);
                lastPurchaseTime = System.currentTimeMillis();
                
                synchronized (marketPurchaseMutex)
                {
                    try
                    {
                        marketPurchaseMutex.wait(10 * 60 * 1000);
                    }
                    catch (Exception e)
                    {
                    }
                }
            }
            
            statusCode.put(KEY_MARKET_PURCHASE_STATUS_CODE, marketPurchaseStatus);
            ret.put(KEY_RETURN_RESULT, statusCode);
            
            Logger.log(Logger.INFO, this.getClass().getName(),
                "MarketBilling: completeAppstorePurchase finished"
                        + " marketPurchaseStatus: " + marketPurchaseStatus);
            return ret;
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }
        
        return ret;
    }

    public void purchaseFinished(String statusCode)
    {
        marketPurchaseStatus = statusCode;
        
        synchronized (marketPurchaseMutex)
        {
            try
            {
                marketPurchaseMutex.notifyAll();
            }
            catch (Exception e)
            {
            }
        }
    }
    
    public JSONObject syncResource(IHtmlSdkServiceListener listener)
    {
        return null;
    }

	
	public JSONObject setCredentials(JSONArray args,
			IHtmlSdkServiceListener listener) 
	{
		
		return null;
	}

    public JSONObject getPOIDetailMap(JSONArray args, IHtmlSdkServiceListener listener)
    {
        return null;
    }

    public JSONObject doAddressAutoComplete(JSONArray args,
            IHtmlSdkServiceListener listener)
    {
        final JSONArray jsonArgs = args;
        final IHtmlSdkServiceListener serviceListener = listener;
        
        Thread t = new Thread()
        {
            public void run()
            {
                JSONObject result = new JSONObject();
                JSONObject callbackData = new JSONObject();
                try
                {
                    String addressType = jsonArgs.getString(0);
                    JSONObject jsonObject = jsonArgs.getJSONObject(1);
                    JSONArray fields = jsonObject.getJSONArray("fields");
                    String region = jsonObject.getString("region");
                    
                    callbackData.put("addressType", addressType);
                    callbackData.put("region", region);
                    
                    String[] fieldsFilter = null;
                    
                    boolean isAllFieldsNeeded = (fields == null || fields.length() == 0);
                    if(isAllFieldsNeeded)
                    {
                        fieldsFilter = ALL_FIELDS;
                    }
                    else
                    {
                        int fieldsLength = fields.length();
                        fieldsFilter = new String[fieldsLength];
                        for(int i = 0; i < fieldsLength; i++)
                        {
                            fieldsFilter[i] = fields.getString(i);
                        }
                    }
                    
                    boolean isAddressCache = "addressCache".equals(addressType);
                    boolean isFavRecent = "recentAndFav".equals(addressType);
                    
                    
                    JSONArray addressList = new JSONArray();
                    Vector tempReceivedList = new Vector();
                    Vector tempRootFavoriteList = new Vector();
                    Vector tempRecentList = new Vector();
                    
                    if(isAddressCache)
                    {
                        Vector nearCities = DaoManager.getInstance().getNearCitiesDao().getNearCities(getRegion());
                        if(nearCities != null)
                        {
                            for(int i = 0; i < nearCities.size(); i++)
                            {
                                Stop stop = (Stop)nearCities.elementAt(i);
                                if(stop != null)
                                {
                                    JSONObject obj = convertStop(stop, fieldsFilter);
                                    if(obj != null)
                                    {
                                        addressList.put(obj);
                                    }
                                }
                            }
                        }
                    }
                    else if(isFavRecent)
                    {
                        Vector favorites = DaoManager.getInstance().getAddressDao().getFavorateAddresses();
                        if(favorites != null)
                        {
                            for(int i = 0; i < favorites.size(); i++)
                            {
                                Address address = (Address)favorites.elementAt(i);
                                if(address != null)
                                {
                                    Stop stop = address.getStop();

                                    if (stop != null)
                                    {
                                        JSONObject jsonObj = convertStop(stop,fieldsFilter);
                                        if (isDuplicateItem(tempRootFavoriteList,jsonObj.toString()))
                                        {
                                            continue;
                                        }
                                        tempRootFavoriteList.add(jsonObj.toString());
                                        jsonObj.put("source", "favorite");
                                        jsonObj.put("index", i);
                                        addressList.put(jsonObj);
                                    }
                                }
                            }
                        }
                        
                        Vector receives = DaoManager.getInstance().getAddressDao().getReceivedAddresses();
                        if(receives != null)
                        {
                            for(int i = 0; i < receives.size(); i++)
                            {
                                Address address = (Address)receives.elementAt(i);
                                if(address != null)
                                {
                                    Stop stop = address.getStop();
                                    if (stop != null)
                                    {
                                        JSONObject jsonObj = convertStop(stop,fieldsFilter);
                                        if(isDuplicateItem(tempReceivedList,jsonObj.toString()))
                                        {
										    continue;
                                        }
                                        tempReceivedList.add(jsonObj.toString());
                                        jsonObj.put("source", "received");
                                        jsonObj.put("index", i);
                                        addressList.put(jsonObj);
                                    }
                                }
                            }
                        }
                        
                        Vector recents = DaoManager.getInstance().getAddressDao().getRecentAddresses();
                        if(recents != null)
                        {
                            for(int i = 0; i < recents.size(); i++)
                            {
                                Address address = (Address)recents.elementAt(i);
                                if(DaoManager.getInstance().getAddressDao().isExistInFavoriteAddress(address, false))
                                {
                                    continue;
                                }

                                if (address != null)
                                {
                                    Stop stop = address.getStop();
                                    if (stop != null)
                                    {
                                        JSONObject jsonObj = convertStop(stop,fieldsFilter);
                                        if (isDuplicateItem(tempRecentList,jsonObj.toString()))
                                        {
                                            continue;
                                        }
                                        tempRecentList.add(jsonObj.toString());
                                        jsonObj.put("source", "recent");
                                        jsonObj.put("index", i);
                                        addressList.put(jsonObj);
                                    }
                                }
                            }
                        }
                    }
                    
                    callbackData.put("addressList", addressList);
                    result.put(IHtmlSdkServiceListener.CALLBACK_TYPE, IHtmlSdkServiceListener.CALLBACK_TYPE_ADDRESSAUTOCOMPLETE);
                    result.put(IHtmlSdkServiceListener.CALLBACK_VALUE, callbackData);
                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                }
                
                serviceListener.callbackHandlerResponse(result);
            }
        };
        
        t.start();
        
        return null;
    }

    public JSONObject doAddressValidation(JSONArray args, IHtmlSdkServiceListener listener)
    {
        try
        {
            String addressType = args.getString(0);
            JSONObject jsonObject = args.getJSONObject(1);

            boolean isUserEntered = "userEntered".equals(addressType);
            boolean isFavRecent = "recentAndFav".equals(addressType);

            if (isUserEntered)
            {
                String uiAddress = jsonObject.getString("uiAddress");
                String aceAddress = jsonObject.getString("aceAddress");

                put(KEY_S_ADDRESS_LINE, aceAddress);
                put(KEY_S_MESSAGE_ADDRESS, uiAddress);
                // do your logic here
                postModelEvent(EVENT_MODEL_DO_VALIDATE_ADDRESS);
            }
            else if (isFavRecent)
            {
                String source = jsonObject.getString("source");
                int index = jsonObject.getInt("index");

                Vector addresses = null;
                if (JSON_VALUE_FAV.equals(source))
                {
                    addresses = DaoManager.getInstance().getAddressDao().getFavorateAddresses();
                }
                else if (JSON_VALUE_RECENT.equals(source))
                {
                    addresses = DaoManager.getInstance().getAddressDao().getRecentAddresses();
                }
                else if (JSON_VALUE_RECEIVED.equals(source))
                {
                    addresses = DaoManager.getInstance().getAddressDao().getReceivedAddresses();
                }

                if (addresses != null)
                {
                    Address selectedAddress = (Address) addresses.elementAt(index);
                    put(KEY_O_VALIDATED_ADDRESS, selectedAddress);
                    postModelEvent(EVENT_MODEL_SELECT_ADDRESS);
                }
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    private JSONObject convertStop(Stop stop, String[] fields)
    {
        JSONObject obj = new JSONObject();
        if(fields == null)
        {
            fields = ALL_FIELDS;
        }
        
        int size = fields.length;
        for(int i = 0; i < size; i++)
        {
            try
            {
                String fieldName = fields[i];
                String value = getValue(fieldName, stop);
                obj.put(fieldName, value);
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
        
        return obj;
    }
    
    private String getValue(String fieldName, Stop stop)
    {
        String value = "";
        if (STOP_FIRST_LINE.equals(fieldName))
        {
            value = stop.getFirstLine();
        }
        else if (STOP_HOUSE_NUMBER.equals(fieldName))
        {
            value = stop.getHouseNumber();
        }
        else if (STOP_SUITE.equals(fieldName))
        {
            value = stop.getSuite();
        }
        else if (STOP_STREET.equals(fieldName))
        {
            value = stop.getStreetName();
        }
        else if (STOP_CROSS_STREET.equals(fieldName))
        {
            value = stop.getCrossStreetName();
        }
        else if (STOP_CITY.equals(fieldName))
        {
            value = stop.getCity();
        }
        else if (STOP_COUNTY.equals(fieldName))
        {
            value = stop.getCounty();
        }
        else if (STOP_STATE.equals(fieldName))
        {
            value = stop.getProvince();
        }
        else if (STOP_COUNTRY.equals(fieldName))
        {
            value = stop.getCountry();
        }
        else if (STOP_POSTALCODE.equals(fieldName))
        {
            value = stop.getPostalCode();
        }
        else if (STOP_SUBLOCALITY.equals(fieldName))
        {
            value = stop.getSubLocality();
        }
        else if (STOP_LOCALITY.equals(fieldName))
        {
            value = stop.getLocality();
        }
        else if (STOP_LOCALE.equals(fieldName))
        {
            value = stop.getLocale();
        }
        else if (STOP_SUBSTREET.equals(fieldName))
        {
            value = stop.getSubStreet();
        }
        else if (STOP_BUILDING_NAME.equals(fieldName))
        {
            value = stop.getBuildingName();
        }
        else if (STOP_ADDRESS_ID.equals(fieldName))
        {
            value = stop.getAddressId();
        }
        else if (STOP_LAT.equals(fieldName))
        {
            value = stop.getLat() + "";
        }
        else if (STOP_LON.equals(fieldName))
        {
            value = stop.getLon() + "";
        }
        
        return value;
    }

    public JSONObject getMapRegionList(JSONArray args, IHtmlSdkServiceListener listener)
    {
        // TODO Auto-generated method stub
        return null;
    }

    public JSONObject startMapDownload(JSONArray args, IHtmlSdkServiceListener listener)
    {
        // TODO Auto-generated method stub
        return null;
    }

    public JSONObject stopMapDownload(JSONArray args, IHtmlSdkServiceListener listener)
    {
        // TODO Auto-generated method stub
        return null;
    }

    public JSONObject pauseMapDownload(JSONArray args, IHtmlSdkServiceListener listener)
    {
        // TODO Auto-generated method stub
        return null;
    }

    public JSONObject deleteMapDownload(JSONArray args, IHtmlSdkServiceListener listener)
    {
        // TODO Auto-generated method stub
        return null;
    }

    public JSONObject getPointOfInterest(JSONArray args, IHtmlSdkServiceListener listener)
    {
        // TODO Auto-generated method stub
        return null;
    }

    public void loadWithAnimation(JSONArray args, IHtmlSdkServiceListener listener)
    {
        // TODO Auto-generated method stub
        
    }
    
    protected void returnCurrentPosition(TnLocation currentLocation, final int serviceId, IHtmlSdkServiceListener listener)
    {
        JSONObject retPosition = new JSONObject();
        JSONObject retCoords = new JSONObject();
        JSONObject ret = null;
        
        try
        {
            if (currentLocation != null)
            {
                retCoords.put("speed", currentLocation.getSpeed());
                retCoords.put("accuracy", currentLocation.getAccuracy());
                retCoords.put("altitude", currentLocation.getAltitude());
                retCoords.put("longitude", currentLocation.getLongitude() / 100000.0f);
                retCoords.put("heading", currentLocation.getHeading());
                retCoords.put("latitude", currentLocation.getLatitude() / 100000.0f);
                retCoords.put("altitudeAccuracy", "");
                retPosition.put("coords", retCoords);
            }
            else
            {
                retPosition.put("coords", "");
            }
            retPosition.put("timestamp", System.currentTimeMillis());        
            
            ret = ResultGenerator.generateJsonResult(ResultGenerator.STATUS_SUCCESS, serviceId, retPosition.toString());
        }
        catch (JSONException e)
        {
            e.printStackTrace();
            ret = ResultGenerator.generateJsonResult(ResultGenerator.STATUS_FAIL, serviceId);
        }

        listener.callbackHandlerResponse(ret);
    }

    public void getCurrentPosition(JSONArray args, final IHtmlSdkServiceListener listener)
    {
        int serviceId = ResultGenerator.SERVICE_ID_ZERO;
        try
        {
            serviceId = args.getInt(0);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        
        final int finalServiceId = serviceId;
        
        final TnLocation currentLocation = LocationProvider.getInstance().getCurrentLocation(
            LocationProvider.GPS_VALID_TIME,
            LocationProvider.NETWORK_LOCATION_VALID_TIME,
            LocationProvider.TYPE_GPS | LocationProvider.TYPE_NETWORK, false);
        
        Thread t = new Thread(new Runnable()
        {
            public void run()
            {
                returnCurrentPosition(currentLocation, finalServiceId, listener);
            }
        });
        t.start();
    }

    public void validateApplicationInstalled(JSONArray args,
            IHtmlSdkServiceListener listener)
    {
        // TODO Auto-generated method stub
        
    }

    public void launchApps(JSONArray args, IHtmlSdkServiceListener listener)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setOrientation(JSONArray args, IHtmlSdkServiceListener listener)
    {
        if (args != null)
        {
            try
            {
                JSONObject jsonObject = args.getJSONObject(0);
                String orientation = jsonObject.getString("orientation");
                if ("portrait".equalsIgnoreCase(orientation))
                {
                    TeleNavDelegate.getInstance().setOrientation(TeleNavDelegate.ORIENTATION_PORTRAIT);
                }
                else if ("landscape".equalsIgnoreCase(orientation))
                {
                    TeleNavDelegate.getInstance().setOrientation(TeleNavDelegate.ORIENTATION_LANDSCAPE);
                }
                else if ("unspecified".equalsIgnoreCase(orientation))
                {
                    TeleNavDelegate.getInstance().setOrientation(TeleNavDelegate.ORIENTATION_UNSPECIFIED);
                }
            }
            catch (Exception e)
            {
            }
        }
    }

    @Override
    public JSONObject getOrientation(JSONArray args, IHtmlSdkServiceListener listener)
    {
        return null;
    }
    
    protected boolean isValidAddress(Stop stop)
    {
        //verify the stop is valid.
        if ( stop != null && stop.getLat() != 0 && stop.getLon() != 0 )
        {
            return true;
        }
        return false;
    }
    
    protected boolean isDuplicateItem(Vector items, String jsonObj)
    {
        if (items.contains(jsonObj))
        {
            return true;
        }
        return false;
    }

    @Override
    public JSONObject doNativeService(JSONObject request, IHtmlSdkServiceListener listener)
    {
        int serviceId = ResultGenerator.SERVICE_ID_ZERO;
        try
        {
            
            String serviceName = null; 
            if (request != null) 
            {            
                serviceId = request.getInt(IHtmlSdkServiceHandler.SERVICE_ID);
                serviceName = request.getString(IHtmlSdkServiceHandler.SERVICE_NAME);
            }
            else
            {
                return ResultGenerator.generateJsonResult(ResultGenerator.STATUS_FAIL, serviceId);
            }
                        
            if(GET_POI_INFO.equals(serviceName))
            {               
                JSONObject serviceData = request.getJSONObject(IHtmlSdkServiceHandler.SERVICE_DATA);
                int selectedIndex = serviceData.getInt("poikey");
                Address temp = getAddress(selectedIndex);
                Address address = null;
                
                if (temp != null)
                {
                    byte[] tempData = SerializableManager.getInstance().getAddressSerializable().toBytes(temp);
                    address = SerializableManager.getInstance().getAddressSerializable().createAddress(tempData);
                }
                
                int detailFromType = this.getInt(KEY_I_TYPE_DETAIL_FROM);
                if (detailFromType == TYPE_DETAIL_FROM_RECENT || detailFromType == TYPE_DETAIL_FROM_FAVORITE || detailFromType == TYPE_DETAIL_FROM_CONTACT)
                {
                    if (address != null)
                    {
                        Stop home = DaoManager.getInstance().getAddressDao().getHomeAddress();
                        Stop work = DaoManager.getInstance().getAddressDao().getOfficeAddress();
                        String addressLabel = address.getLabel();
                        Stop stop = address.getStop();
                        if (home != null && home.getLabel().equalsIgnoreCase(addressLabel) )
                        {
                            if(stop != null && home.getLat() == stop.getLat() && home.getLon() == stop.getLon())
                            {
                                address.setLabel("Home");                                
                            }                            
                        }
                        else if (work != null && work.getLabel().equalsIgnoreCase(addressLabel))
                        {
                            if(stop != null && work.getLat() == stop.getLat() && work.getLon() == stop.getLon())
                            {
                                address.setLabel("Work");
                            }
                        }
                    }                    
                }                

                if (address != null)
                {
                    
                    if (address.getPoi() == null)
                    {
                        address.setEntityType(ENTITY_TYPE_ADDR);
                        
                        if (address.getId() == 0)
                        {
                            rotateMockId();
                            address.setId(mock_poi_id);
                        }
                        
                        Poi addressPoi = new Poi();
                        BizPoi addressBizPoi = new BizPoi();
                        if(address.getPhoneNumber() != null && !address.getPhoneNumber().trim().isEmpty())
                            addressBizPoi.setPhoneNumber(address.getPhoneNumber());
                        addressBizPoi.setBrand(address.getLabel());
                        addressBizPoi.setPoiId(mock_poi_id + "");
                        addressBizPoi.setStop(address.getStop());
                        addressPoi.setBizPoi(addressBizPoi);
                        addressPoi.setStop(address.getStop());
                        address.setPoi(addressPoi);
                    }
                    else
                    {
                        address.setEntityType(ENTITY_TYPE_POI);
                    }
                
                    if(address.getPoi() != null && address.getPoi().getBizPoi() != null)
                    {
                        if (address.getId() == 0)
                        {
                            rotateMockId();
                            long poiId = mock_poi_id;
                            try
                            {
                                poiId = Long.parseLong(address.getPoi().getBizPoi().getPoiId());
                            }
                            catch (Exception e)
                            {
                                Logger.log(this.getClass().getName(), e);
                            }
                            address.setId(poiId);
                        }
                        String distString = address.getPoi().getBizPoi().getDistance();
                        if(distString != null && distString.trim().length() > 0 && Integer.parseInt(distString) != 0)
                        {
                            int distInt = 0;
                            try
                            {
                                distInt = Integer.parseInt(address.getPoi().getBizPoi().getDistance());
                            }
                            catch (Exception e)
                            {
                                Logger.log(this.getClass().getName(), e);
                            }
                            if(distInt > 0)
                            {
                                Preference distanceUnit = ((DaoManager) DaoManager.getInstance()).getPreferenceDao().getPreference(Preference.ID_PREFERENCE_DISTANCEUNIT);
                                int distanceUnitValue = Preference.UNIT_METRIC;
                                if (distanceUnit != null)
                                {
                                    distanceUnitValue = distanceUnit.getIntValue();
                                }
                                String distStr = ResourceManager.getInstance().getStringConverter().convertDistanceMeterToMile(distInt, distanceUnitValue);
                                address.getPoi().getBizPoi().setDistanceWithUnit(distStr);
                            }
                        }
                        else
                        {
                            PoiDataWrapper poiDataWrapper = (PoiDataWrapper) this.get(KEY_O_POI_DATA_WRAPPER);

                            if (poiDataWrapper != null)
                            {
                                String distStr = UiFactory.getInstance().getDisplayDistance(address,
                                    poiDataWrapper.getAnchorStop(), false);
                                address.getPoi().getBizPoi().setDistanceWithUnit(distStr);
                            }
                        }
                    }
                    address.setExistedInFavorite(DaoManager.getInstance().getAddressDao().isExistInFavoriteAddress(address, true));
                    byte[] data = JsonSerializableManager.getJsonInstance().getAddressSerializable().toBytes(address);
                    String addressInfo = new String(data);
                    String convertedAddressInfo = replace("'", "%27", addressInfo);
                    convertedAddressInfo = replace("\\\"", "&quot;", convertedAddressInfo);
                    convertedAddressInfo = replace("\\r\\n", "<br>", convertedAddressInfo);
                    convertedAddressInfo = replace("\\n", "<br>", convertedAddressInfo);
                    convertedAddressInfo = replace("\\/", "/", convertedAddressInfo);
                    convertedAddressInfo = replace("\\t", "        ", convertedAddressInfo);
                    convertedAddressInfo = replace("\\", " ", convertedAddressInfo);
                    
                    return ResultGenerator.generateJsonResult(ResultGenerator.STATUS_SUCCESS, serviceId, convertedAddressInfo);
                }
                else
                {
                    return ResultGenerator.generateJsonResult(ResultGenerator.STATUS_FAIL, serviceId);
                }
            }
            else if(SAVE_TO_FAV.equals(serviceName))
            {
                KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_POI_DETAILS, KontagentLogger.POIINFO_MY_PLACES_ADD);
                JSONObject serviceData = request.getJSONObject(IHtmlSdkServiceHandler.SERVICE_DATA);
                Address address = convertAddress(serviceData.toString().getBytes(), Address.TYPE_FAVORITE_POI);
                address.setStatus(CommonDBdata.ADDED);
                if(address.getPoi() == null)
                {
                    address.setType(Address.TYPE_FAVORITE_STOP);
                }
                
                if(address.getEventId() > 0)
                {
                    address.setIsEventDataAvailable(true);
                }
                DaoManager.getInstance().getAddressDao().addAddress(address, false);
                DaoManager.getInstance().getAddressDao().store();
                logAddPlaceMisLog(address);
                
                return ResultGenerator.generateJsonResult(ResultGenerator.STATUS_SUCCESS, serviceId);
            }
            else if(REMOVE_FROM_FAV.equals(serviceName))
            {
                KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_POI_DETAILS, KontagentLogger.POIINFO_MY_PLACES_DELETE);
                JSONObject serviceData = request.getJSONObject(IHtmlSdkServiceHandler.SERVICE_DATA);
                Address address = convertAddress(serviceData.toString().getBytes(), Address.TYPE_FAVORITE_POI);
                if(address.getPoi() == null)
                {
                    address.setType(Address.TYPE_FAVORITE_STOP);
                }
                if(address.getEventId() > 0)
                {
                    address.setIsEventDataAvailable(true);
                }
                DaoManager.getInstance().getAddressDao().deleteAddress(address);
                DaoManager.getInstance().getAddressDao().store();
                
                return ResultGenerator.generateJsonResult(ResultGenerator.STATUS_SUCCESS, serviceId);
            }
            else if(SHARE_ADDRESS.equals(serviceName))
            {     
                JSONObject serviceData = request.getJSONObject(IHtmlSdkServiceHandler.SERVICE_DATA);
                Address address = JsonSerializableManager.getJsonInstance().getAddressSerializable().createAddress(serviceData.toString().getBytes());
                this.put(KEY_O_SELECTED_ADDRESS, address);
                this.postModelEvent(EVENT_MODEL_GOTO_SHARE_ADDR);
                
                return ResultGenerator.generateJsonResult(ResultGenerator.STATUS_SUCCESS, serviceId);
            }
            else if(SEARCH_NEARBY.equals(serviceName))
            {
                JSONObject serviceData = request.getJSONObject("poi");
                Address address = JsonSerializableManager.getJsonInstance().getAddressSerializable().createAddress(serviceData.toString().getBytes());
                this.put(KEY_O_SELECTED_ADDRESS, address);
                this.postModelEvent(EVENT_MODEL_GOTO_SEARCH_NEARBY);
                
                return ResultGenerator.generateJsonResult(ResultGenerator.STATUS_SUCCESS, serviceId);
            }
            else if(REFRESH.equals(serviceName))
            {
                this.put(KEY_B_IS_NEED_REFRESH, true);
                
                return ResultGenerator.generateJsonResult(ResultGenerator.STATUS_SUCCESS, serviceId);
            }
            else if (PRIVATE_SERVICE_GET_FEEDBACK_POI_DATA.equals(serviceName))
            {
                JSONObject serviceData = request.getJSONObject(IHtmlSdkServiceHandler.SERVICE_DATA);
                String type = serviceData.getString("type");
                JSONObject returnJson = new JSONObject();
                JSONObject latLonJson = new JSONObject();
                Address anchorAddr = null;
                if ("poidetail".equals(type) )
                {
                    int index = getInt(KEY_I_POI_SELECTED_INDEX);
                    PoiDataWrapper poiDataWrapper = (PoiDataWrapper)get(KEY_O_POI_DATA_WRAPPER);
                    Address address = poiDataWrapper.getAddress(index);
                    String poiId = "";
                    String poiName = "";
                    String poiPhoneNum = "";
                    if (address != null && address.getPoi() != null
                            && address.getPoi().getBizPoi() != null)
                    {
                        poiId = address.getPoi().getBizPoi().getPoiId();
                        poiName = address.getPoi().getBizPoi().getBrand();
                        poiPhoneNum = address.getPoi().getBizPoi().getPhoneNumber();
                        anchorAddr = address;          
                    }
                    else
                    {
                        poiName = address.getLabel();
                    }
                    returnJson.put("poiId", convertIllegalJsonString(poiId));
                    returnJson.put("poiName", convertIllegalJsonString(poiName));
                    returnJson.put("poiPhoneNumber", convertIllegalJsonString(poiPhoneNum));
                    isFromPoiDetailFeedback = true;
                }
                else if("poilist".equals(type) )
                {
                    PoiDataWrapper poiDataWrapper = (PoiDataWrapper)get(KEY_O_POI_DATA_WRAPPER);
                    String pageIndex = String.valueOf(poiDataWrapper.getPageNumber());//String.valueOf(poiDataWrapper.getPageIndex(index));
                    String searchUid = poiDataWrapper.getSearchUid();
                    String searchText = getString(ICommonConstants.KEY_S_COMMON_SHOW_SEARCH_TEXT);
                    String searchCatName = "";
                    int categoryId = getInt(ICommonConstants.KEY_I_CATEGORY_ID);
                    if (categoryId != PoiDataRequester.TYPE_ONE_BOX_SEARCH)
                    {
                        searchCatName = searchText;
                    }      
                    String searchKeyword = searchText;
                    returnJson.put("keyword", convertIllegalJsonString(searchKeyword));
                    returnJson.put("categoryName", convertIllegalJsonString(searchCatName));
                    returnJson.put("pageIndex", convertIllegalJsonString(pageIndex));
                    returnJson.put("searchId", convertIllegalJsonString(searchUid));
                    anchorAddr = (Address) get(KEY_O_ADDRESS_ORI);          
                    if(anchorAddr == null)
                    {
                        anchorAddr = (Address) get(KEY_O_ADDRESS_DEST);  
                    }
                    if(anchorAddr == null)
                    {
                        anchorAddr = (Address) get(KEY_O_CURRENT_ADDRESS);  
                    }
                }

                if(anchorAddr != null)
                {
                    latLonJson.put("lat", anchorAddr.getStop().getLat());
                    latLonJson.put("lon", anchorAddr.getStop().getLon());
                    latLonJson.put("address", ResourceManager.getInstance().getStringConverter().convertAddress(anchorAddr.getStop(), false));
                } 
                returnJson.put("stop", latLonJson);
                
                return ResultGenerator.generateJsonResult(ResultGenerator.STATUS_SUCCESS, serviceId, returnJson.toString());
            }
            else if (PRIVATE_SERVICE_SD_CARD_LOG.equals(serviceName))
            {
                new Thread(new Runnable()
                {
                    public void run()
                    {
                        byte[] logBytes = TnSdLogCollector.getInstance().collectClientLogger();
                        doSendClientLogging(logBytes);
                    }
                }).start();
                
                return ResultGenerator.generateJsonResult(ResultGenerator.STATUS_SUCCESS, serviceId);
            }
            else if (PRIVATE_NOTIFY_CLIENT.equals(serviceName))
            {
                String type = request.getString("eventName");
                if (type.equals("loveApp"))
                {                    
                    ((DaoManager) DaoManager.getInstance()).getSimpleConfigDao().put(SimpleConfigDao.KEY_IS_LOVE_APP_IN_FEEDBACK, true);
                    ((DaoManager) DaoManager.getInstance()).getSimpleConfigDao().store();
                }
            }
              
            else if(PRIVATE_SERVICE_GET_ADSID.equals(serviceName))
            {
                BillboardAd currentAd = NavAdManager.getInstance().getCurrentAd();
                if (currentAd != null)
                {
                    int initHeight = currentAd.getInitHeight();
                    int contentHeight = currentAd.getContentHeight();
                    JSONObject ret = new JSONObject();
                    ret.put("adsId", currentAd.getAdsId());
                    ret.put("initHeight", initHeight);
                    ret.put("contentHeight", contentHeight);
                    
                    return ResultGenerator.generateJsonResult(ResultGenerator.STATUS_SUCCESS, serviceId, ret.toString());
                }
                else
                {
                    return ResultGenerator.generateJsonResult(ResultGenerator.STATUS_FAIL, serviceId);
                }
            }
            else if(IS_FAVORITE.equals(serviceName))
            {
                JSONObject serviceData = request.getJSONObject(IHtmlSdkServiceHandler.SERVICE_DATA);
                Address address = convertAddress(serviceData.toString().getBytes(), Address.TYPE_FAVORITE_POI);
                
                if (address.getPoi() == null)
                {
                    address.setType(Address.TYPE_FAVORITE_STOP);
                }
                else
                {
                    address.setType(Address.TYPE_FAVORITE_POI);
                }
                address.setSource(Address.SOURCE_FAVORITES);
                
                boolean isFav =  DaoManager.getInstance().getAddressDao().isExistInFavoriteAddress(address, true);
                int flag = isFav? 1: 0;
                JSONObject retCoords = new JSONObject();
                retCoords.put("isFav", flag);
                return ResultGenerator.generateJsonResult(ResultGenerator.STATUS_SUCCESS, serviceId, retCoords.toString());
            }
            else if (PRIVATE_SERVICE_IS_FAVORITE_ADDRESS.equals(serviceName))
            {
                JSONObject serviceData = request.getJSONObject(IHtmlSdkServiceHandler.SERVICE_DATA);
                Address address = JsonSerializableManager.getJsonInstance().getAddressSerializable().createAddress(
                    serviceData.toString().getBytes());
                
                if (address.getPoi() == null)
                {
                    address.setType(Address.TYPE_FAVORITE_STOP);
                }
                else
                {
                    address.setType(Address.TYPE_FAVORITE_POI);
                }
                address.setSource(Address.SOURCE_FAVORITES);
                address.setLabel(address.getDisplayedText());
                if (address.getPoi() != null && address.getPoi().getBizPoi() != null
                        && address.getPoi().getBizPoi().getPoiId() == null
                        && address.getPoi().getBizPoi().getStop() == null)
                {
                    Stop stop = address.getStop();
                    if (stop != null)
                    {
                        address.getPoi().getBizPoi().setStop(stop);
                    }
                }
                
                JSONObject return_result = new JSONObject();
                if (return_result != null)
                {
                    if (DaoManager.getInstance().getAddressDao().isExistInFavoriteAddress(address, true))
                    {
                        return_result.put(KEY_RETURN_RESULT, true);
                    }
                    else
                    {
                        return_result.put(KEY_RETURN_RESULT, false);
                    }
                }
                
                return ResultGenerator.generateJsonResult(ResultGenerator.STATUS_SUCCESS, serviceId, return_result.toString());
            }
            else if (LAUNCH_PAGES.equals(serviceName))
            {
                String pageName = request.getString("pageName");                
                String status = ResultGenerator.STATUS_SUCCESS;
                
                if (pageName.equals("upsell"))
                {                  
                    this.postModelEvent(EVENT_CONTROLLER_GOTO_UPSELL);     
                }
                else if (pageName.equals("account"))
                {
                    this.postModelEvent(EVENT_CONTROLLER_GOTO_FTUE_MAIN);                    
                }
                else
                {
                    status = ResultGenerator.STATUS_FAIL;
                }
                
                return ResultGenerator.generateJsonResult(status, serviceId);
            }
            else if(LOG_BI_EVENT.equals(serviceName))
            {
                String eventId = request.getString("eventId");  
                JSONObject serviceData = request.getJSONObject("eventData");
                String pageName = serviceData.getString("pageName");
                String status = ResultGenerator.STATUS_SUCCESS;
                if( "POI_DETAIL".equalsIgnoreCase(pageName))
                {
                    KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_POI_DETAILS,
                        eventId);
                }
                return ResultGenerator.generateJsonResult(status, serviceId);
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();            
        }
        
        return ResultGenerator.generateJsonResult(ResultGenerator.STATUS_FAIL, serviceId);
    }
    
    protected void rotateMockId()
    {
        mock_poi_id --;                    
        if (mock_poi_id < -20)
        {
            mock_poi_id = IAddressSerializable.MOCK_POI_ID_TOP;
        }
    }
    
    private void logAddPlaceMisLog(Address address)
    {
        PoiDataWrapper poiDataWrapper = (PoiDataWrapper) this.get(KEY_O_POI_DATA_WRAPPER);
        if(address.getPoi() != null)
        {
            int index = -1;
            if(address.getPoi().getBizPoi() != null && poiDataWrapper != null)
            {
                index= poiDataWrapper.getIndexOfMixedListByPoiId(address.getPoi().getBizPoi().getPoiId());
            }
            if(index >= 0)
            {
                PoiMisLogHelper.getInstance().recordPoiMisLog(IMisLogConstants.TYPE_POI_ADD_PLACE, index);
            }
            else
            {
                PoiMisLogHelper.getInstance().recordPoiMisLog(IMisLogConstants.TYPE_POI_ADD_PLACE, address.getPoi());
            }
        }        
    }

    protected Address convertAddress(byte[] jsonData, byte addressType)
    {
        Address address = JsonSerializableManager.getJsonInstance().getAddressSerializable().createAddress(jsonData);
        address.setLabel(address.getDisplayedText());
        if(addressType != -1)
        {
            address.setType(addressType);
        }
        address.setCreateTime(System.currentTimeMillis());
        
        Stop stop = address.getStop();
        if (stop != null)
        {
            if (stop.getFirstLine() != null
                    && (stop.getStreetName() == null || 
                        stop.getStreetName().trim().length() == 0))
            {
                stop.setStreetName(stop.getFirstLine());
            }

            if (address.getPoi() != null && address.getPoi().getBizPoi() != null)
            {
                String poiIdStr = address.getPoi().getBizPoi().getPoiId();
                
                if (poiIdStr != null && poiIdStr.length() < 4)
                {
                    try
                    {
                        int id = Integer.valueOf(poiIdStr).intValue();
                        if (id <= IAddressSerializable.MOCK_POI_ID_TOP)
                        {
                            address.getPoi().setBizPoi(null);
                            address.setPoi(null);
                            return address;
                        }
                    }
                    catch (Exception e)
                    {
                        
                    }
                }
            }
            
            if(address.getPoi() != null && address.getPoi().getStop() == null)
            {
                address.getPoi().setStop(stop);
            }

            if (address.getPoi() != null && address.getPoi().getBizPoi() != null
                    && address.getPoi().getBizPoi().getStop() == null)
            {
                address.getPoi().getBizPoi().setStop(stop);
            }
        }
        
        return address;
    }
}
