/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * AutoSuggestListAdapter.java
 *
 */
package com.telenav.module.oneboxsearch;

import java.util.Hashtable;
import java.util.Vector;

import android.database.Cursor;
import android.provider.ContactsContract;

import com.telenav.app.AbstractContactProvider.TnContact;
import com.telenav.app.CommManager;
import com.telenav.app.android.AndroidPersistentContext;
import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.dao.serverproxy.AbstractDaoManager;
import com.telenav.data.dao.serverproxy.AddressDao;
import com.telenav.data.dao.serverproxy.ServerDrivenParamsDao;
import com.telenav.data.datatypes.address.Address;
import com.telenav.data.datatypes.address.Stop;
import com.telenav.data.datatypes.poi.OneBoxSearchBean;
import com.telenav.data.datatypes.poi.PoiCategory;
import com.telenav.data.datatypes.primitive.StringList;
import com.telenav.data.serverproxy.AbstractServerProxy;
import com.telenav.data.serverproxy.IServerProxyConstants;
import com.telenav.data.serverproxy.IServerProxyListener;
import com.telenav.data.serverproxy.IUserProfileProvider;
import com.telenav.data.serverproxy.impl.IAutoSuggestProxy;
import com.telenav.data.serverproxy.impl.ServerProxyFactory;
import com.telenav.data.serverproxy.impl.navsdk.helper.NavSdkPoiProxyHelper.NavSdkSearchProxy;
import com.telenav.location.TnLocation;
import com.telenav.module.AppConfigHelper;
import com.telenav.module.dashboard.DashboardManager;
import com.telenav.module.location.LocationProvider;
import com.telenav.module.region.RegionUtil;
import com.telenav.res.IStringAc;
import com.telenav.res.IStringCommon;
import com.telenav.res.ResourceManager;
import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.core.AbstractTnUiHelper;
import com.telenav.tnui.core.ITnTextChangeListener;
import com.telenav.tnui.core.TnUiArgs;
import com.telenav.tnui.core.TnUiArgs.ITnUiArgsDecorator;
import com.telenav.tnui.core.TnUiArgs.TnUiArgAdapter;
import com.telenav.tnui.graphics.AbstractTnGraphics;
import com.telenav.tnui.widget.TnLinearContainer;
import com.telenav.ui.ImageDecorator;
import com.telenav.ui.UiFactory;
import com.telenav.ui.UiStyleManager;
import com.telenav.ui.citizen.CitizenAddressListItem;
import com.telenav.ui.citizen.CitizenCircleAnimation;
import com.telenav.ui.citizen.CitizenTextField;
import com.telenav.ui.frogui.text.FrogTextHelper;
import com.telenav.ui.frogui.widget.FrogAdapter;
import com.telenav.ui.frogui.widget.FrogKeywordFilter;
import com.telenav.ui.frogui.widget.FrogList;
import com.telenav.util.PrimitiveTypeCache;

/**
 * @author byma
 * @date 2010-12-9
 */
class AutoSuggestListAdapter extends FrogKeywordFilter implements FrogAdapter, IServerProxyListener, ITnTextChangeListener
{
    protected final FrogList list;

    protected boolean isProgressShown = false;

    protected Hashtable remoteSearchCache;

    protected Vector localQueryList;

    protected Vector remoteQueryList;

    protected Vector suggestionsList;

    protected Vector recentFavList;

    protected Vector tempReceivedList;

    protected Object mutex = null;

    protected OneBoxSearchUiDecorator decorator;

    protected CitizenCircleAnimation progressAnimation;

    protected AbstractTnComponent progressItem;

    protected final static int DROPDOWN_ITEMS_COUNTS_MAX = 20;

    protected final static int LIST_COUNT_ONLINE_REQUEST_TIME = 2;

    protected final static int REMOTELIST_THRESHOLD = 3;

    protected final static int HANDLE_INPUT_DURATION_THRESHOLD = 800;

    protected final static int EMPTY_RECENT_ADDRESSES_LIST_SIZE = 100;

    private final static int CONTACT_NO_PHONE_NUMBER = 0;

    protected IAutoSuggestProxy autoSuggestProxy;

    protected Stop anchor;

    protected String transactionId;

    protected String requestJobId;

    protected CitizenTextField textField;

    protected Object suggestionsListGeneratorMutex = new Object();

    protected boolean isCancel = false;

    protected String queriedString = "";

    protected boolean isFavRecentMerged = false;

    protected boolean isOneboxSearch = true;

    protected boolean isNeedShow = true;

    protected IUserProfileProvider userProfileProvider;

    protected String region;

    protected boolean needCurrentAddress;

    protected static final int MAX_SIZE = 5;

    protected Address currentAnchorAddress;

    public AutoSuggestListAdapter(CitizenTextField textField, FrogList list, Stop anchor, OneBoxSearchUiDecorator decorator,
            IUserProfileProvider userProfileProvider, String region, boolean needCurrentAddress)
    {
        this.list = list;
        int delayTime = DaoManager.getInstance().getServerDrivenParamsDao()
                .getIntValue(ServerDrivenParamsDao.AUTO_COMPLETE_DELAY_TIME);
        if (delayTime < 0)
        {
            delayTime = HANDLE_INPUT_DURATION_THRESHOLD;
        }
        this.setMinInterval(delayTime);
        this.textField = textField;
        this.anchor = anchor;
        this.decorator = decorator;
        this.userProfileProvider = userProfileProvider;
        this.region = region;
        this.needCurrentAddress = needCurrentAddress;
        this.initialize();

    }

    protected void initialize()
    {
        localQueryList = new Vector();
        remoteQueryList = new Vector();
        suggestionsList = new Vector();
        recentFavList = new Vector();
        tempReceivedList = new Vector();
        remoteSearchCache = new Hashtable();
    }

    public void setNeedShow(boolean isNeedShow)
    {
        this.isNeedShow = isNeedShow;
    }

    public void setOneboxSearch(boolean isBusinessSearch)
    {
        this.isOneboxSearch = isBusinessSearch;
    }

    public void setAnchorStop(Stop stop)
    {
        this.anchor = stop;
    }

    public void setTransactionId(String transactionId)
    {
        this.transactionId = transactionId;
    }

    public String getTransactionId()
    {
        return this.transactionId;
    }

    public void setCurrentAnchorAddress(Address address)
    {
        this.currentAnchorAddress = address;
    }

    public Address getCurrentAnchorAddress()
    {
        return this.currentAnchorAddress;
    }

    public void cancel()
    {
        isCancel = true;
        isNeedShow = false;
        if (progressAnimation != null)
        {
            progressAnimation.enable(false);
        }
        updateList(false, null);
    }

    public Vector getSuggestions()
    {
        return this.suggestionsList;
    }

    protected FilterResults performFilter(FilterRequest constraint)
    {
        isCancel = false;
        isProgressShown = false;
        if (constraint == null || constraint.keyword == null)
            return null;

        FilterResults localResults = null;

        // If the constraint is zero length string, clear all cache.
        if (constraint.keyword.length() == 0)
        {
            this.clearAll();// can we cancel here?
        }

        int keywordLength = constraint.keyword.length();

        /* search local cache to get the list */
        Vector list = (Vector) this.remoteSearchCache.get(constraint.keyword);
        if (list != null && list.size() > 0)
        {
            updateNetworkData(constraint.keyword, list);
        }
        /* no cache */
        else if (isOneboxSearch)
        {
            if (autoSuggestProxy == null)
            {
                autoSuggestProxy = ServerProxyFactory.getInstance().createAutoSuggestProxy(null,
                    CommManager.getInstance().getComm(), this, userProfileProvider);
            }

            ((AbstractServerProxy) autoSuggestProxy).reset();

            /* Search remote list */
            if (autoSuggestProxy != null && constraint.keyword != null && keywordLength > 0)
            {
                if (keywordLength >= REMOTELIST_THRESHOLD && isNeedShow)
                {
                    isProgressShown = true;

                    int lat = anchor.getLat();
                    int lon = anchor.getLon();
                    if (transactionId == null)
                    {
                        transactionId = "";
                    }

                    requestJobId = autoSuggestProxy.requestAutoSuggestList(constraint.keyword, DROPDOWN_ITEMS_COUNTS_MAX, lat,
                        lon, transactionId);
                    synchronized (this.suggestionsListGeneratorMutex)
                    {
                        this.clearRemoteList();
                    }
                }
            }
        }
        /* Search local data */
        if (constraint.keyword != null && constraint.keyword.length() != 0)
        {
            localResults = getLocalSearchResult(constraint.keyword);
        }
        else
        {
            localResults = getEmptySearchData();
        }
        return localResults;
    }

    protected void publishResults(FilterResults results)
    {
        this.generateSuggestionsList(results.constraint);
        setListAdapter(results.constraint);
    }

    protected void updateList(boolean isShowProgress, String queriedString)
    {
        isProgressShown = isShowProgress;
        setListAdapter(queriedString);
    }

    protected void setListAdapter(String queriedString)
    {
        if (queriedString != null && queriedString.trim().length() != 0)
        {
            this.queriedString = queriedString;
        }
        ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).runOnUiThread(new Runnable()
        {
            public void run()
            {
                if (list != null)
                {
                    list.setAdapter(AutoSuggestListAdapter.this);
                }
            }
        });

    }

    protected synchronized void updateNetworkData(String queryString, Vector cacheList)
    {

        if (cacheList != null)
        {
            remoteQueryList.clear();
            remoteQueryList.addAll(cacheList);
        }
        else
        {
            Vector nodeResults = autoSuggestProxy.getResults();
            boolean hasSuggestionResult = autoSuggestProxy.hasSuggestionResult();

            Vector remoteCacheList = new Vector();
            for (int i = 0; i < nodeResults.size(); i++)
            {
                OneBoxSearchBean itemNode = (OneBoxSearchBean) nodeResults.elementAt(i);
                String keyString = itemNode.getKey();
                if (hasSuggestionResult || keyString.toLowerCase().indexOf(queryString.toLowerCase()) != -1)
                {
                    ListItemData content = new ListItemData(ListItemData.ONEBOX_NETWORK_TYPE, keyString, itemNode.getContent(),
                        null, -100, null);
                    this.remoteQueryList.addElement(content);
                    remoteCacheList.addElement(content);
                }
            }

            if (remoteCacheList.size() > 0)
            {
                remoteSearchCache.put(queryString, remoteCacheList);
            }
        }

        /*
         * if remote cache has no query string cache, update it, or it will be update after local searching
         */
        if (textField.getText().length() >= REMOTELIST_THRESHOLD && cacheList == null)
        {
            this.generateSuggestionsList(queryString);
            this.updateList(false, queryString);
        }
        else
        {
            isProgressShown = false;
        }
    }

    protected FilterResults getEmptySearchData()
    {
        this.clearLocalList();
        FilterResults filterResults = new FilterResults();

        if(this.needCurrentAddress)
        {
            Address currentAddress = getCurrentAnchorAddress();
            if (currentAddress != null && currentAddress.getStop() != null)
            {
                String label = ResourceManager.getInstance().getText(IStringCommon.RES_CURRENT_LOCATION, IStringCommon.FAMILY_COMMON);
                String content = ResourceManager.getInstance().getStringConverter()
                        .convertAddress(currentAddress.getStop(), true);
                ListItemData ltemData = new ListItemData(ListItemData.CURRENT_LOCATION, label, content, currentAddress,
                    -100, null);
                localQueryList.addElement(ltemData);
            }
        }

        searchHomeWorkAddresses("");
        searchRecentAddresses("", false);       
        searchRecentSearchList();

        filterResults.count = localQueryList.size();
        filterResults.values = localQueryList;
        filterResults.constraint = "";
        return filterResults;
    }

    private void searchRecentSearchList()
    {
        Vector recentSearchList = DaoManager.getInstance().getAddressDao().getRecentSearch();
        int size = Math.min(recentSearchList.size(), EMPTY_RECENT_ADDRESSES_LIST_SIZE);
        for (int i = 0; i < size; i++)
        {
            String labelName = "";
            ListItemData ltemData = null;
            Object obj = recentSearchList.elementAt(i);
            if (obj instanceof String)
            {
                labelName = (String) obj;
            }
            ltemData = new ListItemData(ListItemData.RECENT_SEARCH_STR_TYPE, labelName, labelName, null, -100, null);
            localQueryList.addElement(ltemData);
        }
    }

    protected FilterResults getLocalSearchResult(String keyword)
    {
        this.clearLocalList();
        FilterResults filterResults = new FilterResults();
        // TODO ALBERTMA wait for the reallyData,
        searchInLocalData(keyword);

        filterResults.count = localQueryList.size();
        filterResults.values = localQueryList;
        filterResults.constraint = keyword;
        return filterResults;
    }

    protected void searchInLocalData(String keyword)
    {
        if (keyword != null && keyword.trim().length() > 0)
        {
            if (isOneboxSearch)
            {
                searchContactsAddress(keyword);
                if (!isFavRecentMerged || this.recentFavList.size() == 0)
                {
                    isFavRecentMerged = true;
                    if (this.recentFavList.size() > 0)
                    {
                        this.recentFavList.removeAllElements();
                    }
                    searchFav(keyword);
                    searchRecentAddresses(keyword, true);
                }
                else
                {
                    searchMergedList(keyword);
                }
                searchHomeWorkAddresses(keyword);
                searchAirport(keyword);

            }
            // Fix bug: http://jira.telenav.com:8080/browse/TNANDROID-1589
            if (!isOneboxSearch || keyword.length() < REMOTELIST_THRESHOLD)
            {
                searchHotBrand(keyword);
                searchHotCategory(keyword);
            }
            searchRecentSearch(keyword);
        }
    }

    protected void searchContactsAddress(String queryString)
    {
        Vector ContactsVector = retireveContactsWithAddress(queryString);
        for (int i = 0; i < ContactsVector.size(); i++)
        {
            String labelName = "";
            ListItemData ltemData = null;
            Address addr = null;
            TnContact tnContact = (TnContact) ContactsVector.elementAt(i);
            addr = new Address();
            addr.setStop(tnContact.stop);
            labelName = tnContact.name;
            //            this.recentFavList.addElement(addr);
            if (labelName != null && labelName.length() > 0 && tnContact.stop != null)
            {
                ltemData = new ListItemData(ListItemData.CONTACT_TYPE, labelName, labelName, addr, -100, tnContact.phoneNumber);
                localQueryList.addElement(ltemData);
            }
        }
    }


    private Vector retireveContactsWithAddress(String keyword)
    {
        //Samyukta DEBUG 
        long beginTimestamp = System.currentTimeMillis();

        Vector<TnContact> matchContacts = new Vector<TnContact>();
        String[] projection = new String[]
                { ContactsContract.CommonDataKinds.Phone.CONTACT_ID, ContactsContract.CommonDataKinds.Phone.TYPE,
                ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME};
        String selection = "" + ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " LIKE ? OR "
                + ContactsContract.CommonDataKinds.Phone.NUMBER + " LIKE ?";
        String arg = "%" + keyword + "%";
        String phoneArg = arg;
        if(isNumeric(keyword))
        {
            if(keyword.length() > 3 && keyword.length() <= 6)
            {
                phoneArg = "%" + keyword.substring(0, 3) + "%" + keyword.substring(3) + "%";
            }
            if( keyword.length() > 6)
            {
                phoneArg = "%" + keyword.substring(0, 3) + "%" + keyword.substring(3, 6) + "%" + keyword.substring(6) + "%";
            }
        }
        String[] selectionArgs = new String[]
                { arg, phoneArg };
        Cursor cursor = AndroidPersistentContext.getInstance().getContext().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projection, selection,
            selectionArgs, ContactsContract.CommonDataKinds.Phone.CONTACT_ID);

        TnContact tnContact = null;
        Vector<TnContact> vContacts = new Vector<TnContact>();
        if (cursor != null)
        {
            while (cursor.moveToNext())
            {
                String id = cursor.getString(0);
                int phoneType = cursor.getInt(1);
                String phone = cursor.getString(2);
                String displayName = cursor.getString(3);

                if (tnContact == null || !tnContact.id.equals(id))
                {
                    tnContact = new TnContact();
                    vContacts.add(tnContact);
                }

                tnContact.id = id;
                tnContact.name = displayName;
                tnContact.phoneNumber = phone;
                if (ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE == phoneType)
                {
                    tnContact.phoneNumberType = tnContact.TYPE_MOBILE;
                }
                else if (ContactsContract.CommonDataKinds.Phone.TYPE_WORK == phoneType)
                {
                    tnContact.phoneNumberType = tnContact.TYPE_WORK;
                }
                else if (ContactsContract.CommonDataKinds.Phone.TYPE_HOME == phoneType)
                {
                    tnContact.phoneNumberType = tnContact.TYPE_HOME;
                }
            }
        }
        cursor.close();

        //get Contacts that do not have Phone Number
        String[] dataProjection = new String[]
                { ContactsContract.CommonDataKinds.Phone.CONTACT_ID, ContactsContract.Data.DISPLAY_NAME, ContactsContract.Data.HAS_PHONE_NUMBER};
        String dataSelection = ContactsContract.CommonDataKinds.StructuredPostal.DISPLAY_NAME + " LIKE ?";
        String[] dataSelectionArgs = new String[]
                { arg };
        Cursor dataCursor = AndroidPersistentContext.getInstance().getContext().getContentResolver().query(ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI, dataProjection, dataSelection,
            dataSelectionArgs, ContactsContract.CommonDataKinds.Phone.CONTACT_ID);
        tnContact = null;
        if (dataCursor != null)
        {
            while (dataCursor.moveToNext())
            {
                String id = dataCursor.getString(0);
                String displayName = dataCursor.getString(1);
                //If matching contact does not have Phone #, then add it to the vContacts
                if(dataCursor.getInt(2) == CONTACT_NO_PHONE_NUMBER)
                {
                    if (tnContact == null || !tnContact.id.equals(id))
                    {
                        tnContact = new TnContact();
                        vContacts.add(tnContact);
                    }
                    tnContact.id = id;
                    tnContact.name = displayName;
                }
            }
        }
        dataCursor.close();


        //get contacts with addr as search key.
        String[] postalProjection = new String[]
                { ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                ContactsContract.CommonDataKinds.StructuredPostal.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.StructuredPostal.STREET,
                ContactsContract.CommonDataKinds.StructuredPostal.CITY,
                ContactsContract.CommonDataKinds.StructuredPostal.REGION,
                ContactsContract.CommonDataKinds.StructuredPostal.POSTCODE,
                ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY };
        String postalSelection = ContactsContract.CommonDataKinds.StructuredPostal.STREET + " LIKE ?";
        String[] postalSelectionArgs = new String[]
                { arg };
        Cursor postalCursor = AndroidPersistentContext.getInstance().getContext().getContentResolver().query(ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI, postalProjection, postalSelection,
            postalSelectionArgs, ContactsContract.CommonDataKinds.Phone.CONTACT_ID);
        tnContact = null;
        if (postalCursor != null)
        {
            while (postalCursor.moveToNext())
            {
                String id = postalCursor.getString(0);
                String displayName = postalCursor.getString(1);

                if (tnContact == null || !tnContact.id.equals(id))
                {
                    tnContact = new TnContact();
                    vContacts.add(tnContact);
                }
                tnContact.id = id;
                tnContact.name = displayName;
            }
        }
        postalCursor.close();
        //DEBUG
        long intermediate = System.currentTimeMillis();
        System.out.println("Time cost for first 3 : "+ ( intermediate- beginTimestamp));

        for (TnContact tc : vContacts)
        {
            String[] addressProjection = new String[]
                    { ContactsContract.CommonDataKinds.StructuredPostal.TYPE, ContactsContract.CommonDataKinds.StructuredPostal.STREET,
                    ContactsContract.CommonDataKinds.StructuredPostal.CITY,
                    ContactsContract.CommonDataKinds.StructuredPostal.REGION,
                    ContactsContract.CommonDataKinds.StructuredPostal.POSTCODE,
                    ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY };
            String s = ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?";
            s += " and (";
            s += "(" + ContactsContract.CommonDataKinds.StructuredPostal.STREET + " is not null and " + ContactsContract.CommonDataKinds.StructuredPostal.STREET + " != '')";
            s += "or (" + ContactsContract.CommonDataKinds.StructuredPostal.CITY + " is not null and " + ContactsContract.CommonDataKinds.StructuredPostal.CITY + " != '')";
            s += "or (" + ContactsContract.CommonDataKinds.StructuredPostal.REGION + " is not null and " + ContactsContract.CommonDataKinds.StructuredPostal.REGION + " != '')";
            s += "or (" + ContactsContract.CommonDataKinds.StructuredPostal.POSTCODE + " is not null and " + ContactsContract.CommonDataKinds.StructuredPostal.POSTCODE + " != '')";
            s += ")";
            Cursor addressCursor =  AndroidPersistentContext.getInstance().getContext().getContentResolver().query(ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI,
                addressProjection, s, new String[]
                        { tc.id }, null);
            if (addressCursor != null)
            {
                while (addressCursor.moveToNext())
                {
                    TnContact newContact = new TnContact();
                    newContact.id = tc.id;
                    newContact.name = tc.name;
                    newContact.phoneNumber = tc.phoneNumber;
                    newContact.phoneNumberType = tc.phoneNumberType;

                    int addressType = addressCursor.getInt(0);
                    String street = addressCursor.getString(1);
                    String city = addressCursor.getString(2);
                    String region = addressCursor.getString(3);
                    String postalCode = addressCursor.getString(4);
                    // String country = addressCursor.getString(4);

                    Stop stop = new Stop();
                    if (street != null && street.length() > 0)
                    {
                        stop.setFirstLine(street);
                    }
                    if (city != null && city.length() > 0)
                    {
                        stop.setCity(city);
                    }
                    if (region != null && region.length() > 0)
                    {
                        stop.setProvince(region);
                    }
                    if (postalCode != null && postalCode.length() > 0)
                    {
                        stop.setPostalCode(postalCode);
                    }
                    newContact.stop = stop;
                    matchContacts.add(newContact);
                }
            }
            addressCursor.close();
        }
        //DEBUG
        long endTimestamp = System.currentTimeMillis();
        System.out.println("Time cost for getting postal : "+ (endTimestamp - intermediate));

        return matchContacts;
    }

    private static boolean isNumeric(String str)
    {
        if(str == null || str.trim().length() == 0)
            return false;

        return str.matches("[+-]?\\d*(\\.\\d+)?");
    }

    protected void searchMergedList(String keyword)
    {
        if (tempReceivedList.size() > 0)
        {
            tempReceivedList.clear();
        }

        for (int i = 0; i < recentFavList.size(); i++)
        {
            String labelName = "";
            Stop stop = null;
            ListItemData ltemData = null;
            Address addr = (Address) recentFavList.elementAt(i);
            int nlistItemType = ListItemData.RECENT_STOP_TYPE;
            switch (addr.getType())
            {
                case Address.TYPE_FAVORITE:
                case Address.TYPE_FAVORITE_POI:
                case Address.TYPE_FAVORITE_STOP:
                    nlistItemType = ListItemData.FAV_TYPE;
                    break;
                    //                case Address.TYPE_HOME:
                    //                    nlistItemType = ListItemData.HOME_TYPE;
                    //                    break;
                    //                case Address.TYPE_WORK:
                    //                    nlistItemType = ListItemData.WORK_TYPE;
                    //                    break;
                default:
                    break;
            }
            labelName = getAddressLabel(addr);
            stop = addr.getStop();
            String addressInfo = null;
            if (stop != null)
            {
                addressInfo = ResourceManager.getInstance().getStringConverter().convertAddress(stop, false);
            }

            if ( stop != null && (stringMatchQuery(labelName, keyword) || stringMatchQuery(addressInfo, keyword)) && !isDuplicateInReceivedAddress(addr) )
            {
                ltemData = new ListItemData(nlistItemType, labelName, labelName, addr, -100, null);
                localQueryList.addElement(ltemData);
            }
        }
    }

    protected boolean stringMatchQuery(String str, String query)
    {
        return (str != null && str.length() > 0 && str.toLowerCase().indexOf(query.toLowerCase()) != -1);
    }

    protected void searchHomeWorkAddresses(String queryString)
    {
        Stop home = DaoManager.getInstance().getAddressDao().getHomeAddress();
        Stop work = DaoManager.getInstance().getAddressDao().getOfficeAddress();
        String sHome = ResourceManager.getInstance().getCurrentBundle().getString(IStringAc.RES_BTTN_HOME, IStringAc.FAMILY_AC);
        String sWork = ResourceManager.getInstance().getCurrentBundle().getString(IStringAc.RES_BTN_WORK, IStringAc.FAMILY_AC);
        if(home != null)
        {
            String addressInfo = ResourceManager.getInstance().getStringConverter().convertAddress(home, false);
            if (stringMatchQuery(sHome, queryString) || stringMatchQuery(addressInfo, queryString))
            {
                Address addr = new Address();
                addr.setStop(home);
                ListItemData ltemData = new ListItemData(ListItemData.HOME_TYPE, sHome, sHome, addr, -100, null);
                localQueryList.addElement(ltemData);
            }
        }
        else
        {
            if(!this.needCurrentAddress)
            {
                if(sHome.toLowerCase().indexOf(queryString.toLowerCase()) != -1)
                {
                    ListItemData ltemData = new ListItemData(ListItemData.SET_HOME_TYPE, sHome, sHome, null, -100, null);
                    localQueryList.addElement(ltemData);
                }
            }
        }
        if(work != null)
        {
            String addressInfo = ResourceManager.getInstance().getStringConverter().convertAddress(work, false);
            if (stringMatchQuery(sWork, queryString) || stringMatchQuery(addressInfo, queryString))
            {
                Address addr = new Address();
                addr.setStop(work);
                ListItemData ltemData = new ListItemData(ListItemData.WORK_TYPE, sWork, sWork, addr, -100, null);
                localQueryList.addElement(ltemData);
            }
        }  
        else
        {
            if(!this.needCurrentAddress)
            {
                if(sWork.toLowerCase().indexOf(queryString.toLowerCase()) != -1)
                {
                    ListItemData ltemData = new ListItemData(ListItemData.SET_WORK_TYPE, sWork, sWork, null, -100, null);
                    localQueryList.addElement(ltemData);
                }
            }
        }
    }

    protected void searchRecentAddresses(String queryString, boolean isNeedPreload)
    {
        Vector recentVector = DaoManager.getInstance().getAddressDao().getRecentAddresses();
        for (int i = 0; i < recentVector.size(); i++)
        {
            String labelName = "";
            Stop stop = null;
            ListItemData ltemData = null;
            Address addr = null;
            String addressInfo = null;
            Object obj = recentVector.elementAt(i);
            if (obj instanceof Address)
            {
                addr = (Address) obj;
                labelName = getAddressLabel(addr);
                stop = addr.getStop();
                if (stop != null)
                {
                    addressInfo = ResourceManager.getInstance().getStringConverter().convertAddress(stop, false);
                }
                if (isNeedPreload)
                {
                    this.recentFavList.addElement(addr);
                }
            }

            if ( stop != null && (stringMatchQuery(labelName, queryString) || stringMatchQuery(addressInfo, queryString)))
            {
                ltemData = new ListItemData(ListItemData.RECENT_STOP_TYPE, labelName, labelName, addr, -100, null);
                localQueryList.addElement(ltemData);
            }
        }

    }

    protected void searchRecentSearch(String queryString)
    {
        Vector recentSearchVector = DaoManager.getInstance().getAddressDao().getRecentSearch();
        int size = Math.min(recentSearchVector.size(), EMPTY_RECENT_ADDRESSES_LIST_SIZE);
        for (int i = 0; i < size; i++)
        {
            Object obj = recentSearchVector.elementAt(i);
            if (obj instanceof String)
            {
                String labelName = (String) obj;
                if (stringMatchQuery(labelName, queryString) /*&& !isExistInlocalQuery(labelName) */)
                {
                    ListItemData ltemData = new ListItemData(ListItemData.RECENT_SEARCH_STR_TYPE, labelName, labelName, null, -100, null);
                    localQueryList.addElement(ltemData);
                }
            }

        }
    }


    public TnLocation getTnLocation()
    {
        TnLocation location;
        if (region.equalsIgnoreCase(RegionUtil.getInstance().getCurrentRegion()))
        {
            location = LocationProvider.getInstance().getLastKnownLocation(
                LocationProvider.TYPE_GPS | LocationProvider.TYPE_NETWORK);
            if (location == null)
                location = AbstractDaoManager.getInstance().getResourceBarDao()
                .getRegionAnchor(region);
        }
        else
        {
            location = AbstractDaoManager.getInstance().getResourceBarDao()
                    .getRegionAnchor(region);
        }
        if (location == null)
            location = LocationProvider.getInstance().getDefaultLocation();
        return location;
    }


    private void searchAirport(String queryString)
    {
        Vector addressVector = convertAirportStopToAddress(queryString);
        for (int i = 0; i < addressVector.size(); i++)
        {
            String labelName = "";
            Stop stop = null;
            ListItemData ltemData = null;
            Address addr = null;
            Object airportAddress = addressVector.elementAt(i);
            if (airportAddress instanceof Address)
            {
                addr = (Address) airportAddress;
                labelName = getAddressLabel(addr);
                stop = addr.getStop();
            }

            String addressInfo = null;
            if (stop != null)
            {
                addressInfo = ResourceManager.getInstance().getStringConverter().convertAddress(stop, false);
            }
            if (stop != null && (stringMatchQuery(labelName, queryString) || stringMatchQuery(addressInfo, queryString)))
            {
                ltemData = new ListItemData(ListItemData.AIRPORT_TYPE, labelName, labelName, addr, -100, null);
                localQueryList.addElement(ltemData);
            }
        }
    }

    private Vector convertAirportStopToAddress(String queryString)
    {
        Vector result = DaoManager.getInstance().getAddressDao().getAirports(queryString, MAX_SIZE, getTnLocation());
        Vector addressVector = new Vector();
        int size = result.size();
        if (size > 0)
        {
            for (int index = 0; index < size; index++)
            {
                Stop airport = (Stop) result.elementAt(index);
                String airportCode = AddressDao.getAirportShowLabel(airport);
                Stop airportStop = DaoManager.getInstance().getAddressDao().getAirportByName(airportCode);
                if (airportStop != null)
                {
                    Address airportAddress = new Address();
                    airportAddress.setLabel(airportStop.getLabel());
                    airportAddress.setStop(airportStop);
                    airportAddress.setType(Address.TYPE_AIRPORT);
                    addressVector.add(airportAddress);
                }
            }
        }
        return addressVector;
    }

    protected void searchFav(String queryString)
    {
        Vector favVector = DaoManager.getInstance().getAddressDao().getDisplayFavorateAddress();
        for (int i = 0; i < favVector.size(); i++)
        {
            String labelName = "";
            Stop stop = null;
            ListItemData ltemData = null;
            Address addr = null;
            String addressInfo = null;
            Object favAddrObj = favVector.elementAt(i);
            boolean isExist = false;
            if (favAddrObj instanceof Address)
            {
                addr = (Address) favAddrObj;
                labelName = getAddressLabel(addr);
                stop = addr.getStop();
                if (stop != null)
                {
                    addressInfo = ResourceManager.getInstance().getStringConverter().convertAddress(stop, false);
                }
                recentFavList.addElement(addr);
            }
            if (stop != null && (stringMatchQuery(labelName, queryString) || stringMatchQuery(addressInfo, queryString)) && !isDuplicateInReceivedAddress(addr))
            {
                ltemData = new ListItemData(ListItemData.FAV_TYPE, labelName, labelName, addr, -100, null);
                localQueryList.addElement(ltemData);
            }
        }
    }

    private String getAddressLabel(Address addr)
    {
        String label = "";
        switch (addr.getType())
        {
            case Address.TYPE_FAVORITE:
            case Address.TYPE_FAVORITE_POI:
            case Address.TYPE_FAVORITE_STOP:
            {
                label = addr.getLabel();
                break;
            }
            case Address.TYPE_RECENT:
            case Address.TYPE_RECENT_POI:
            case Address.TYPE_RECENT_STOP:
            case Address.TYPE_HOME:
            case Address.TYPE_WORK:
            {
                Stop home = DaoManager.getInstance().getAddressDao().getHomeAddress();
                Stop work = DaoManager.getInstance().getAddressDao().getOfficeAddress();
                String sHome = ResourceManager.getInstance().getCurrentBundle()
                        .getString(IStringAc.RES_BTTN_HOME, IStringAc.FAMILY_AC);
                String sWork = ResourceManager.getInstance().getCurrentBundle()
                        .getString(IStringAc.RES_BTN_WORK, IStringAc.FAMILY_AC);

                Stop stop = addr.getStop();

                if (stop != null)
                {
                    if (stop.equalsIgnoreCase(home))
                    {
                        label = sHome;
                    }
                    else if (stop.equalsIgnoreCase(work))
                    {
                        label = sWork;
                    }
                    else
                    {
                        label = addr.getLabel();
                    }
                }
                else
                {
                    label = addr.getLabel();
                }
                break;
            }
            default:
            {
                label = addr.getLabel();
                break;
            }
        }

        return label;
    }

    protected void searchFilter(PoiCategory node, String filterStr)
    {
        int size = node.getChildrenSize();
        for (int i = 0; i < size; i++)
        {
            PoiCategory currentNode = node.getChildAt(i);
            if (filterStr.length() == 0)
            {
                continue;
            }
            else
            {
                if (currentNode.getChildrenSize() == 0)
                {
                    String nodeName = currentNode.getName().toUpperCase();

                    if (filterCategory(currentNode))
                    {
                        continue;
                    }
                    else
                    {
                        if (nodeName.startsWith(filterStr.toUpperCase()))
                        {
                            if (!isDuplicateItem(currentNode))
                            {
                                ListItemData item = new ListItemData(ListItemData.CATEGORY_TYPE, currentNode.getName(),
                                    currentNode.getName(), null, currentNode.getCategoryId(), null);
                                localQueryList.addElement(item);
                            }
                        }
                        else
                        {
                            continue;
                        }
                    }
                }
                else
                {
                    searchFilter(currentNode, filterStr);
                }
            }
        }
    }

    /*
     * "More"-->0, "All categories" --> -1, "Any" and "most popular" have the same ID with their parent.
     */
    private boolean filterCategory(PoiCategory currentNode)
    {
        int id = currentNode.getCategoryId();
        return id == 0 || id == -1 || id == currentNode.getParent().getCategoryId();
    }

    protected void searchHotCategory(String filterString)
    {
        PoiCategory hotCategoryNode = DaoManager.getInstance().getResourceBarDao().getHotPoiNode(region);
        if (hotCategoryNode != null)
        {
            searchFilter(hotCategoryNode, filterString);
        }
    }

    protected void searchHotBrand(String filterString)
    {
        StringList hotBrandName = DaoManager.getInstance().getResourceBarDao().getBrandNameNode(region);
        if (hotBrandName != null)
        {
            int stringSize = hotBrandName.size();
            for (int i = 0; i < stringSize; i++)
            {
                String brandName = hotBrandName.elementAt(i);
                if (brandName.toLowerCase().startsWith(filterString))
                {
                    ListItemData item = new ListItemData(ListItemData.ONEBOX_LOCAL_TYPE, brandName, brandName, null, -1, null);
                    localQueryList.addElement(item);
                }
            }
        }
    }

    protected void generateSuggestionsList(final String queryString)
    {
        ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).runOnUiThread(new Runnable()
        {
            public void run()
            {
                synchronized (suggestionsListGeneratorMutex)
                {
                    suggestionsList.removeAllElements();

                    for (int i = 0; i < localQueryList.size(); i++ )
                    {
                        ListItemData itemData = (ListItemData)localQueryList.elementAt(i);
                        if(itemData.dataType != ListItemData.RECENT_SEARCH_STR_TYPE)
                        {
                            suggestionsList.addElement(itemData);
                        }
                    }

                    //                    suggestionsList.addAll(localQueryList);

                    boolean isShowRemoteList = queryString.length() >= REMOTELIST_THRESHOLD;
                    if (isShowRemoteList)
                    {
                        for (int i = suggestionsList.size() - 1; i >= 0; i--)
                        {
                            ListItemData localData = (ListItemData) suggestionsList.get(i);
                            String localName = localData.name;
                            if (localData.dataType == ListItemData.FAV_TYPE
                                    || localData.dataType == ListItemData.RECENT_STOP_TYPE)
                            {
                                continue;
                            }
                            for (int j = 0; j < remoteQueryList.size(); j++)
                            {
                                ListItemData remoteData = (ListItemData) remoteQueryList.get(j);
                                if (null != localName && localName.equalsIgnoreCase(remoteData.name))
                                {
                                    suggestionsList.remove(i);
                                    break;
                                }
                            }
                        }
                        suggestionsList.addAll(remoteQueryList);
                    }

                    for (int i = 0; i < localQueryList.size(); i++ )
                    {
                        ListItemData itemData = (ListItemData)localQueryList.elementAt(i);
                        if(itemData.dataType == ListItemData.RECENT_SEARCH_STR_TYPE)
                        {
                            suggestionsList.addElement(itemData);
                        }
                    }
                }
            }
        });
    }

    public void transactionFinished(AbstractServerProxy proxy, String jobId)
    {

        if (isCancel || !jobId.equals(requestJobId))
            return;

        String queryString = ((IAutoSuggestProxy) proxy).getQueryString();
        if (proxy.getStatus() == IServerProxyConstants.SUCCESS
                && proxy.getRequestAction().equals(IServerProxyConstants.ACT_AUTO_SUGGEST))
        {
            updateNetworkData(queryString, null);
        }
        else
        {
            this.updateList(false, queryString);
        }
    }

    public void updateTransactionStatus(AbstractServerProxy proxy, byte progress)
    {

    }

    public void networkError(AbstractServerProxy proxy, byte statusCode, String jobId)
    {
        if (isCancel)
            return;

        this.updateList(false, null);
    }

    public void transactionError(AbstractServerProxy proxy)
    {
        if (isCancel)
            return;

        this.updateList(false, null);
    }

    public boolean isAllowNetworkRequest(AbstractServerProxy proxy)
    {
        return true;
    }

    public AbstractTnComponent getComponent(int position, AbstractTnComponent convertComponent, AbstractTnComponent parent)
    {
        CitizenAddressListItem listItem = null;

        int maxCount = suggestionsList.size();
        if (isProgressShown)
        {
            maxCount = LIST_COUNT_ONLINE_REQUEST_TIME;
        }
        int showSize = Math.min(maxCount, suggestionsList.size());
        if (position < showSize)
        {
            if (convertComponent != null && convertComponent instanceof CitizenAddressListItem)
            {
                listItem = (CitizenAddressListItem) convertComponent;
                listItem.setId(position);
                listItem.setTitle(null);
                listItem.setAddress(null);
                listItem.getTnUiArgs().remove(CitizenAddressListItem.KEY_LEFT_ICON_FOCUS);
                listItem.getTnUiArgs().remove(CitizenAddressListItem.KEY_LEFT_ICON_UNFOCUS);
            }
            else
            {
                listItem = UiFactory.getInstance().createCitizenAddressListItem(position);
                listItem.setTitleColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_ME_GR), UiStyleManager
                    .getInstance().getColor(UiStyleManager.TEXT_COLOR_ME_GR));
                listItem.setGap(listItem.getLeftPadding(), 0);

            }

            listItem.setBoldFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_LIST_SINGLE));
            listItem.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_LABEL));

            Object obj = null;
            synchronized (suggestionsListGeneratorMutex)
            {
                if (position < suggestionsList.size())
                {
                    obj = suggestionsList.elementAt(position);
                }
            }

            if (obj instanceof ListItemData)
            {
                String text = ((ListItemData) obj).name;
                listItem.setTitle(boldQueryString(text));
                listItem.setForegroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_ME_GR), UiStyleManager
                    .getInstance().getColor(UiStyleManager.TEXT_COLOR_ME_GR));
                switch (((ListItemData) obj).dataType)
                {
                    case ListItemData.FAV_TYPE:
                    {
                        Address address = ((ListItemData) obj).address;
                        if (address != null)
                        {
                            listItem.setBoldFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_LIST_DUAL_LINE_TOP));
                            listItem.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_LIST_DUAL_LINE_BOTTOM));
                            listItem.setAddress(ResourceManager.getInstance().getStringConverter()
                                .convertAddress(address.getStop(), false));
                            if (address.getCatagories() != null
                                    && address.getCatagories().contains(AddressDao.RECEIVED_ADDRESSES_CATEGORY)
                                    && !address.isReadByUser())
                            {
                                listItem.getTnUiArgs().put(CitizenAddressListItem.KEY_LEFT_ICON_UNFOCUS,
                                    ImageDecorator.IMG_LIST_RECEIVED_ICON_UNFOCUSED);
                                listItem.getTnUiArgs().put(CitizenAddressListItem.KEY_LEFT_ICON_FOCUS,
                                    ImageDecorator.IMG_LIST_RECEIVED_ICON_UNFOCUSED);
                            }
                            else
                            {
                                listItem.getTnUiArgs().put(CitizenAddressListItem.KEY_LEFT_ICON_UNFOCUS,
                                    ImageDecorator.IMG_FAV_ICON_LIST_UNFOCUS);
                                listItem.getTnUiArgs().put(CitizenAddressListItem.KEY_LEFT_ICON_FOCUS,
                                    ImageDecorator.IMG_FAV_ICON_LIST_UNFOCUS);
                            }
                        }
                        break;
                    }
                    case ListItemData.RECENT_STOP_TYPE:
                    {
                        Address address = ((ListItemData) obj).address;
                        if (address != null)
                        {
                            listItem.setBoldFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_LIST_DUAL_LINE_TOP));
                            listItem.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_LIST_DUAL_LINE_BOTTOM));
                            listItem.setAddress(ResourceManager.getInstance().getStringConverter()
                                .convertAddress(address.getStop(), false));
                            listItem.getTnUiArgs().put(CitizenAddressListItem.KEY_LEFT_ICON_UNFOCUS,
                                ImageDecorator.IMG_LIST_HISTORY_ICON_UNFOCUSED);
                            listItem.getTnUiArgs().put(CitizenAddressListItem.KEY_LEFT_ICON_FOCUS,
                                ImageDecorator.IMG_LIST_HISTORY_ICON_UNFOCUSED);
                        }
                        break;
                    }
                    case ListItemData.AIRPORT_TYPE:
                    {
                        Address address = ((ListItemData) obj).address;
                        if (address != null)
                        {
                            listItem.setBoldFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_LIST_DUAL_LINE_TOP));
                            listItem.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_LIST_DUAL_LINE_BOTTOM));
                            listItem.setAddress(ResourceManager.getInstance().getStringConverter()
                                .convertAddress(address.getStop(), false));
                            listItem.getTnUiArgs().put(CitizenAddressListItem.KEY_LEFT_ICON_UNFOCUS, ImageDecorator.IMG_LIST_AIRPORT_ICON_UNFOCUSED);
                            listItem.getTnUiArgs().put(CitizenAddressListItem.KEY_LEFT_ICON_FOCUS, ImageDecorator.IMG_LIST_AIRPORT_ICON_UNFOCUSED);
                        }
                        break;
                    }
                    case ListItemData.CONTACT_TYPE:
                    {
                        Address address = ((ListItemData) obj).address;
                        if (address != null)
                        {
                            listItem.setBoldFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_LIST_DUAL_LINE_TOP));
                            listItem.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_LIST_DUAL_LINE_BOTTOM));
                            listItem.setAddress(ResourceManager.getInstance().getStringConverter()
                                .convertAddress(address.getStop(), false));
                            listItem.getTnUiArgs().put(CitizenAddressListItem.KEY_LEFT_ICON_UNFOCUS, ImageDecorator.IMG_LIST_CONTACTS_ICON_UNFOCUSED);
                            listItem.getTnUiArgs().put(CitizenAddressListItem.KEY_LEFT_ICON_FOCUS, ImageDecorator.IMG_LIST_CONTACTS_ICON_FOCUSED);
                        }
                        break;
                    }
                    case ListItemData.HOME_TYPE:
                    {
                        Address address = ((ListItemData) obj).address;
                        if (address != null)
                        {
                            listItem.setBoldFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_LIST_DUAL_LINE_TOP));
                            listItem.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_LIST_DUAL_LINE_BOTTOM));
                            listItem.setAddress(ResourceManager.getInstance().getStringConverter()
                                .convertAddress(address.getStop(), false));
                            listItem.getTnUiArgs().put(CitizenAddressListItem.KEY_LEFT_ICON_UNFOCUS,
                                ImageDecorator.IMG_LIST_HOME_ICON_UNFOCUSED);
                            // the unfocused and focused status use same picture now.
                            listItem.getTnUiArgs().put(CitizenAddressListItem.KEY_LEFT_ICON_FOCUS,
                                ImageDecorator.IMG_LIST_HOME_ICON_UNFOCUSED);
                        }
                        break;
                    }
                    case ListItemData.WORK_TYPE:
                    {
                        Address address = ((ListItemData) obj).address;
                        if (address != null)
                        {
                            listItem.setBoldFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_LIST_DUAL_LINE_TOP));
                            listItem.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_LIST_DUAL_LINE_BOTTOM));
                            listItem.setAddress(ResourceManager.getInstance().getStringConverter()
                                .convertAddress(address.getStop(), false));
                            listItem.getTnUiArgs().put(CitizenAddressListItem.KEY_LEFT_ICON_UNFOCUS,
                                ImageDecorator.IMG_LIST_WORK_ICON_UNFOCUSED);
                            listItem.getTnUiArgs().put(CitizenAddressListItem.KEY_LEFT_ICON_FOCUS,
                                ImageDecorator.IMG_LIST_WORK_ICON_UNFOCUSED);
                        }
                        break;
                    }
                    case ListItemData.CURRENT_LOCATION:
                    {
                        Address address = ((ListItemData) obj).address;
                        if (address != null)
                        {
                            listItem.setBoldFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_LIST_DUAL_LINE_TOP));
                            listItem.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_LIST_DUAL_LINE_BOTTOM));
                            listItem.setAddress(ResourceManager.getInstance().getStringConverter()
                                .convertAddress(address.getStop(), false));
                            listItem.getTnUiArgs().put(CitizenAddressListItem.KEY_LEFT_ICON_UNFOCUS,
                                ImageDecorator.IMG_LIST_CURRENT_ICON_UNFOCUSED);
                            listItem.getTnUiArgs().put(CitizenAddressListItem.KEY_LEFT_ICON_FOCUS,
                                ImageDecorator.IMG_LIST_CURRENT_ICON_UNFOCUSED);
                        }
                        break;
                    }
                    case ListItemData.SET_HOME_TYPE:
                    {
                        listItem.setBoldFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_LIST_DUAL_LINE_TOP));
                        listItem.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_LIST_DUAL_LINE_BOTTOM));
                        listItem.setAddress(ResourceManager.getInstance().getCurrentBundle().getString(IStringCommon.RES_SETUP_HOME, IStringCommon.FAMILY_COMMON));
                        listItem.getTnUiArgs().put(CitizenAddressListItem.KEY_LEFT_ICON_UNFOCUS,
                            ImageDecorator.IMG_LIST_HOME_ICON_UNFOCUSED);
                        listItem.getTnUiArgs().put(CitizenAddressListItem.KEY_LEFT_ICON_FOCUS,
                            ImageDecorator.IMG_LIST_HOME_ICON_UNFOCUSED);
                        break;
                    }
                    case ListItemData.SET_WORK_TYPE:
                    {
                        listItem.setBoldFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_LIST_DUAL_LINE_TOP));
                        listItem.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_LIST_DUAL_LINE_BOTTOM));
                        listItem.setAddress(ResourceManager.getInstance().getCurrentBundle().getString(IStringCommon.RES_SETUP_WORK, IStringCommon.FAMILY_COMMON));
                        listItem.getTnUiArgs().put(CitizenAddressListItem.KEY_LEFT_ICON_UNFOCUS,
                            ImageDecorator.IMG_LIST_WORK_ICON_UNFOCUSED);
                        listItem.getTnUiArgs().put(CitizenAddressListItem.KEY_LEFT_ICON_FOCUS,
                            ImageDecorator.IMG_LIST_WORK_ICON_UNFOCUSED);
                        break;
                    }
                }
            }

            return listItem;
        }
        else
        {
            if (isProgressShown)
            {
                progressItem = createProgressItem();
                progressAnimation.enable(true);
                return progressItem;
            }
            else
            {
                if (progressAnimation != null)
                {
                    progressAnimation.enable(false);
                }
            }
        }

        return null;
    }

    private String boldQueryString(String text)
    {
        String queryText = queriedString.toLowerCase();
        int index = text.toLowerCase().indexOf(queryText);
        if (index != -1 && text.length() >= index + queryText.length())
        {
            queryText = text.substring(index, index + queryText.length());
            text = text.replaceFirst(queryText, FrogTextHelper.BOLD_START + queryText + FrogTextHelper.BOLD_END);
        }
        return text;
    }

    public int getCount()
    {
        if (isProgressShown)
        {
            int count = Math.min(suggestionsList.size() + 1, LIST_COUNT_ONLINE_REQUEST_TIME + 1);

            return count;
        }
        else
        {
            return suggestionsList.size();
        }
    }

    public int getItemType(int position)
    {
        return 0;
    }

    public void clearAll()
    {
        synchronized (suggestionsListGeneratorMutex)
        {
            this.remoteQueryList.removeAllElements();
            this.localQueryList.removeAllElements();
            this.remoteSearchCache.clear();
            this.queriedString = "";
        }
    }

    public void onTextChange(AbstractTnComponent component, String text)
    {
        this.filter(text);
    }

    protected AbstractTnComponent createProgressItem()
    {
        TnLinearContainer progressItem = new TnLinearContainer(0, true, AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);
        progressAnimation = UiFactory.getInstance().createCircleAnimation(0, true);
        int minAnimationSize = AppConfigHelper.getMinDisplaySize() / 10;

        int maxDropSize = minAnimationSize * 3 / 40;
        progressAnimation.setDropSizes(new int[]
                { maxDropSize, maxDropSize - 1 });
        progressAnimation.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH,
            new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
            {
                public Object decorate(TnUiArgAdapter args)
                {
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getMinDisplaySize() / 8);
                }
            }));

        progressAnimation.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT,
            new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
            {
                public Object decorate(TnUiArgAdapter args)
                {
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getMinDisplaySize() / 8);
                }
            }));
        progressItem.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT,
            new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
            {
                public Object decorate(TnUiArgAdapter args)
                {
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getMinDisplaySize() / 5);
                }
            }));
        progressItem.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, decorator.SCREEN_WIDTH);
        progressItem.add(progressAnimation);
        progressItem.setFocusable(false);

        return progressItem;
    }

    protected void clearRemoteList()
    {
        this.remoteQueryList.removeAllElements();
    }

    protected void clearLocalList()
    {
        this.localQueryList.removeAllElements();
    }

    private boolean isExistInlocalQuery(String labelName)
    {
        for (int i = 0; i < localQueryList.size(); i++)
        {
            ListItemData itemData = (ListItemData) localQueryList.get(i);
            if (itemData.name.equalsIgnoreCase(labelName))
            {
                return true;
            }
        }
        return false;
    }

    protected boolean isDuplicateInReceivedAddress(Address address)
    {
        if (address.getCatagories() != null && address.getCatagories().contains(AddressDao.RECEIVED_ADDRESSES_CATEGORY))
        {
            if (tempReceivedList.contains(address.getLabel()))
            {
                return true;
            }

            tempReceivedList.add(address.getLabel());
        }

        return false;
    }

    protected boolean isDuplicateItem(PoiCategory currentNode)
    {
        for (int m = 0; m < localQueryList.size(); m++)
        {
            if (currentNode.getCategoryId() == (((ListItemData) localQueryList.get(m)).categoryId))
            {
                return true;
            }
        }
        return false;
    }
}
