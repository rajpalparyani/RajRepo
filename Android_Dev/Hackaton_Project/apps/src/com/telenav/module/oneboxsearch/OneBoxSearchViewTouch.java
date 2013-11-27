/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * OneBoxSearchViewTouch.java
 *
 */
package com.telenav.module.oneboxsearch;

import java.util.Vector;

import com.telenav.app.CommManager;
import com.telenav.data.dao.misc.CitiesDao;
import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.dao.serverproxy.AddressDao;
import com.telenav.data.datatypes.address.Address;
import com.telenav.data.datatypes.address.Stop;
import com.telenav.data.datatypes.browser.BrowserSessionArgs;
import com.telenav.data.datatypes.poi.OneBoxSearchBean;
import com.telenav.data.datatypes.poi.PoiCategory;
import com.telenav.data.serverproxy.IUserProfileProvider;
import com.telenav.data.serverproxy.impl.IOneBoxSearchProxy;
import com.telenav.data.serverproxy.impl.IPoiSearchProxy;
import com.telenav.i18n.ResourceBundle;
import com.telenav.location.TnLocation;
import com.telenav.log.mis.IMisLogConstants;
import com.telenav.log.mis.MisLogManager;
import com.telenav.log.mis.log.AddressMisLog;
import com.telenav.logger.Logger;
import com.telenav.module.AppConfigHelper;
import com.telenav.module.ac.AddressListFilter;
import com.telenav.module.browsersdk.BrowserSdkModel;
import com.telenav.module.location.LocationProvider;
import com.telenav.module.nav.NavRunningStatusProvider;
import com.telenav.module.poi.PoiDataWrapper;
import com.telenav.mvc.AbstractCommonUiDecorator;
import com.telenav.mvc.AbstractCommonView;
import com.telenav.mvc.ICommonConstants;
import com.telenav.res.IStringCommon;
import com.telenav.res.IStringPoi;
import com.telenav.res.IStringSetHome;
import com.telenav.res.ResourceManager;
import com.telenav.res.converter.StringConverter;
import com.telenav.sdk.kontagent.KontagentLogger;
import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.core.AbstractTnContainer;
import com.telenav.tnui.core.AbstractTnUiHelper;
import com.telenav.tnui.core.ITnFocusChangeListener;
import com.telenav.tnui.core.ITnTextChangeListener;
import com.telenav.tnui.core.TnKeyEvent;
import com.telenav.tnui.core.TnMenu;
import com.telenav.tnui.core.TnMenuItem;
import com.telenav.tnui.core.TnMotionEvent;
import com.telenav.tnui.core.TnScreen;
import com.telenav.tnui.core.TnUiArgs;
import com.telenav.tnui.core.TnUiArgs.ITnUiArgsDecorator;
import com.telenav.tnui.core.TnUiArgs.TnUiArgAdapter;
import com.telenav.tnui.core.TnUiEvent;
import com.telenav.tnui.graphics.AbstractTnGraphics;
import com.telenav.tnui.widget.TnLinearContainer;
import com.telenav.tnui.widget.TnPopupContainer;
import com.telenav.tnui.widget.TnTextField;
import com.telenav.ui.ImageDecorator;
import com.telenav.ui.NinePatchImageDecorator;
import com.telenav.ui.UiFactory;
import com.telenav.ui.UiStyleManager;
import com.telenav.ui.citizen.CitizenAddressListItem;
import com.telenav.ui.citizen.CitizenScreen;
import com.telenav.ui.citizen.CitizenTextField;
import com.telenav.ui.citizen.CitizenWebComponent;
import com.telenav.ui.frogui.widget.FrogAdapter;
import com.telenav.ui.frogui.widget.FrogButton;
import com.telenav.ui.frogui.widget.FrogKeywordFilter;
import com.telenav.ui.frogui.widget.FrogLabel;
import com.telenav.ui.frogui.widget.FrogList;
import com.telenav.ui.frogui.widget.FrogListItem;
import com.telenav.ui.frogui.widget.FrogMessageBox;
import com.telenav.ui.frogui.widget.FrogNullField;
import com.telenav.ui.frogui.widget.FrogProgressBox;
import com.telenav.ui.frogui.widget.FrogTextField;
import com.telenav.util.PrimitiveTypeCache;
/**
 * @author Albert Ma (byma@telenav.cn)
 * @date 2010-12-3
 */
public class OneBoxSearchViewTouch extends AbstractCommonView implements IOneBoxSearchConstants, ITnTextChangeListener, ITnFocusChangeListener
{
 
    protected CityFilter cityFilter = null;
    protected AddressFilter addressFilter = null;
    
    protected FrogList cityList = null;
    protected FrogList addressList = null;
    
    protected Vector EMPTY_VECTOR = new Vector();
    
    public OneBoxSearchViewTouch(AbstractCommonUiDecorator uiDecorator)
    { 
        super(uiDecorator);
    }

    protected TnPopupContainer createPopup(int state)
    {
        TnPopupContainer popup = null;
        switch(state)
        {
            case STATE_DOING_ONEBOX_SEARCH:
            {
                popup = createProgressBar();
                break;
            }
            case STATE_QUERY_CLEAR_HISTORY:
            {
                popup = createQueryClearHistory();
                break;
            }
            case STATE_NO_GPS_WARNING:
            {
                popup = createNoGPSWarning();
                break;
            }
               
        }
        return popup;
    }

    private TnPopupContainer createNoGPSWarning()
    {
        ResourceBundle bundle = ResourceManager.getInstance().getCurrentBundle();
        String noGps = bundle.getString(IStringCommon.RES_GPS_ERROR, IStringCommon.FAMILY_COMMON);
        
        TnMenu menu = UiFactory.getInstance().createMenu();
        
        String cancel = bundle.getString(IStringCommon.RES_BTTN_OK, IStringCommon.FAMILY_COMMON);
        menu.add(cancel, CMD_COMMON_BACK);
        
        FrogMessageBox messageBox = UiFactory.getInstance().createMessageBox(STATE_NO_GPS_WARNING, noGps, menu);
        return messageBox;
    }
    
    private TnPopupContainer createQueryClearHistory()
    {
        String message = ResourceManager.getInstance().getCurrentBundle()
                .getString(IStringPoi.LABEL_QUERY_CLEAR_ALL_HISTORY, IStringPoi.FAMILY_POI);
        String clearAll = ResourceManager.getInstance().getCurrentBundle()
                .getString(IStringCommon.RES_BTTN_YES, IStringCommon.FAMILY_COMMON);
        String cancel = ResourceManager.getInstance().getCurrentBundle()
                .getString(IStringCommon.RES_BTTN_NO, IStringCommon.FAMILY_COMMON);
        TnMenu menu = UiFactory.getInstance().createMenu();
        message = (message == null ? "" : message);
        clearAll = (clearAll == null ? "" : clearAll);
        cancel = (cancel == null ? "" : cancel);
        menu.add(clearAll, CMD_COMMON_OK);
        menu.add(cancel, CMD_COMMON_BACK);
        TnPopupContainer popup = UiFactory.getInstance().createMessageBox(
            STATE_QUERY_CLEAR_HISTORY, message, menu);
        return popup;
    }

    protected TnPopupContainer createProgressBar()
    {
        String search = ResourceManager.getInstance().getCurrentBundle().getString(IStringCommon.RES_SEARCHING, IStringCommon.FAMILY_COMMON);
        String name =  model.getString(KEY_S_COMMON_SHOW_SEARCH_TEXT);
        if(name == null)
        {
            name = model.getString(KEY_S_COMMON_SEARCH_TEXT);
        }
        
        StringConverter converter = ResourceManager.getInstance().getStringConverter();
        search = converter.convert(search, new String[]{name});
        
        FrogProgressBox progressBox  = UiFactory.getInstance().createProgressBox(0, search);
        
        return progressBox;
    }

    protected TnScreen createScreen(int state)
    {
        CitizenScreen screen = null;
        switch(state)
        {
            case STATE_ONE_BOX_MAIN:
            {
                screen = createOneBoxSearchScreen();
                break;
            }
            case STATE_DID_U_MEAN:
            {
                screen = createDidUMeanScreen();
                break;
            }
            case STATE_DID_U_MEAN_MULTISTOP:
            {
                screen = createMultiStops();
                break;
            }
        }
        
        if (screen != null)
        {
            removeMenuById(screen, ICommonConstants.CMD_COMMON_PREFERENCE);
        }
        
        return screen;
    }

    protected CitizenScreen createMultiStops()
    {
        CitizenScreen screen = UiFactory.getInstance().createScreen(STATE_DID_U_MEAN_MULTISTOP);
       
        TnLinearContainer subTitleContainer = UiFactory.getInstance()
                .createLinearContainer(0, false,
                    AbstractTnGraphics.VCENTER | AbstractTnGraphics.HCENTER);
        screen.getContentContainer().add(subTitleContainer);
        subTitleContainer.setBackgroundColor(UiStyleManager.getInstance().getColor(
            UiStyleManager.TEXT_COLOR_ME_GR));
        subTitleContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH,
            uiDecorator.SCREEN_WIDTH);
        subTitleContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT,
            ((OneBoxSearchUiDecorator) uiDecorator).ONEBOX_TITLE_BAR_HEIGHT);
        String didUMean = ResourceManager.getInstance().getCurrentBundle().getString(
            IStringPoi.LABEL_DID_YOU_MEAN, IStringPoi.FAMILY_POI);
        FrogLabel titleLabel = UiFactory.getInstance().createLabel(0, didUMean);
        titleLabel.setFont(UiStyleManager.getInstance().getFont(
            UiStyleManager.FONT_LIST_SINGLE));
        titleLabel.setForegroundColor(
            UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_WH),
            UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_WH));
        subTitleContainer.add(titleLabel);
        AbstractTnContainer contentContainer = screen.getContentContainer();
        contentContainer.setBackgroundColor(UiStyleManager.getInstance().getColor(
            UiStyleManager.TEXT_COLOR_WH));
        FrogList suggestList = UiFactory.getInstance().createList(0);
        contentContainer.add(suggestList);
        MultiStopListAdapter adapter = new MultiStopListAdapter();
        suggestList.setAdapter(adapter);

        return screen;
    }

    protected CitizenScreen createDidUMeanScreen()
    {
        CitizenScreen screen = UiFactory.getInstance().createScreen(
                STATE_DID_U_MEAN);
  
        TnLinearContainer subTitleContainer = UiFactory
                .getInstance()
                .createLinearContainer(0, false,
                        AbstractTnGraphics.VCENTER | AbstractTnGraphics.HCENTER);
        screen.getContentContainer().add(subTitleContainer);
        subTitleContainer.setBackgroundColor(UiStyleManager.getInstance()
                .getColor(UiStyleManager.TEXT_COLOR_ME_GR));
        subTitleContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH,
                uiDecorator.SCREEN_WIDTH);
        subTitleContainer
                .getTnUiArgs()
                .put(
                        TnUiArgs.KEY_PREFER_HEIGHT,
                        ((OneBoxSearchUiDecorator) uiDecorator).ONEBOX_TITLE_BAR_HEIGHT);
        String didUMean = ResourceManager
                .getInstance()
                .getCurrentBundle()
                .getString(IStringPoi.LABEL_DID_YOU_MEAN, IStringPoi.FAMILY_POI);
        FrogLabel titleLabel = UiFactory.getInstance().createLabel(0, didUMean);
        titleLabel.setForegroundColor(UiStyleManager.getInstance().getColor(
                UiStyleManager.TEXT_COLOR_WH), UiStyleManager.getInstance()
                .getColor(UiStyleManager.TEXT_COLOR_WH));
        titleLabel.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_LIST_SINGLE));
        subTitleContainer.add(titleLabel);
        AbstractTnContainer contentContainer = screen.getContentContainer();
        contentContainer.setBackgroundColor(UiStyleManager.getInstance()
                .getColor(UiStyleManager.TEXT_COLOR_WH));
        FrogList suggestList = UiFactory.getInstance().createList(0);
        contentContainer.add(suggestList);
        SuggestListAdapter adapter = new SuggestListAdapter();
        suggestList.setAdapter(adapter);
        

        return screen;
    }
    
    protected boolean prepareModelData(int state, int commandId)
    {
        switch (state)
        {
            case STATE_ONE_BOX_MAIN:
            {
                switch (commandId)
                {
                    case CMD_DO_ONEBOX_SEARCH:
                    {
                        model.put(KEY_B_IS_SAVE_TEXT_RECENT_SEARCH, true);
                        break;
                    }
                    case CMD_SELECT_CITY:
                    case CMD_SAVE:
                    {
                        prepareAddressData();
                        break;
                    }
                }
                break;
            }
        }
        return super.prepareModelData(state, commandId);
    }
    
    
    protected void prepareAddressData()
    {
        CitizenTextField addressTextField = (CitizenTextField) getComponentInScreen(ID_ADDRESS_INPUT_FIELD);
        CitizenTextField cityTextField = (CitizenTextField) getComponentInScreen(ID_CITY_INPUT_FIELD);
        
        model.put(KEY_S_ADDRESS_LINE, addressTextField.getText());
        model.put(KEY_S_CITY, cityTextField.getText());
    }
    
    
    protected boolean preProcessUIEvent(TnUiEvent tnUiEvent)
    {
        switch(tnUiEvent.getType())
        {
            case TnUiEvent.TYPE_COMMAND_EVENT:
            {
                switch(tnUiEvent.getCommandEvent().getCommand())
                {
                    case CMD_DO_ONEBOX_SEARCH:
                    {
                        model.remove(KEY_I_CATEGORY_ID);
                        cancelSuggestionSearch();
                        CitizenScreen screen = (CitizenScreen) ((AbstractTnUiHelper) AbstractTnUiHelper
                                .getInstance()).getCurrentScreen();
                        AbstractTnComponent tnComponent = screen
                                .getComponentById(ID_ONEBOX_INPUT_FIELD);
                        if (tnComponent instanceof CitizenTextField)
                        {
                            CitizenTextField textField = (CitizenTextField) tnComponent;
                            String text = textField.getText();
                            if(text == null || text.trim().length() == 0)
                            {
                                //per pm requirement, we need to do nothing.
                                return true;
                            }
                            model.put(KEY_S_COMMON_SEARCH_TEXT, text);
                            model.put(KEY_S_COMMON_SHOW_SEARCH_TEXT, text);
                            model.put(KEY_B_IS_SAVE_TEXT_RECENT_SEARCH, true);
                        }
                        break;
                    }
                    case CMD_POI_SUGGEST_SELECT:
                    {
                        AbstractTnComponent tnComponent = tnUiEvent.getComponent();
                        if(tnComponent instanceof FrogListItem)
                        {
                            int id = tnComponent.getId();
                            Vector suggestions = model.getVector(KEY_V_RESULT_SUGGESTIONS);
                            if(suggestions != null && id >= 0 && id < suggestions.size())
                            {
                                OneBoxSearchBean suggestion = (OneBoxSearchBean)suggestions.elementAt(id);
                                String searchText = suggestion.getContent();
                                String searchingShowString = suggestion.getKey();
                                model.put(KEY_S_COMMON_SEARCH_TEXT, searchText);
                                model.put(KEY_S_COMMON_SHOW_SEARCH_TEXT, searchingShowString);
                                
                                PoiDataWrapper poiDataWrapper = (PoiDataWrapper) model.get(KEY_O_POI_DATA_WRAPPER);
                                this.model.put(KEY_S_TRANSACTION_ID, poiDataWrapper.getSearchUid());
                                this.model.put(KEY_B_IS_FROM_SUGGEST_SELECTION, true);
                                
                                MisLogManager misLogManager = MisLogManager.getInstance();
                                if (misLogManager.getFilter().isTypeEnable(IMisLogConstants.TYPE_ONEBOX_SUGGESTIONS_CLICK))
                                {
                                    AddressMisLog log = misLogManager.getFactory().createOneBoxSuggestionsClickMisLog();
                                    log.setSuggestionKeyword(suggestion.getContent());
                                    log.setTimestamp(System.currentTimeMillis());
                                    log.setSearchUid(poiDataWrapper.getSearchUid());
                                    log.setPageNumber(0);
                                    log.setPageIndex(id);
                                    Logger.log(Logger.INFO, this.getClass().getName(), IMisLogConstants.PROCESS_MISLOG, new Object[]
                                    { log });
                                }
                            }
                        }
                        break;
                    }
                    case CMD_COMMON_GOTO_ONEBOX:
                    {
                        if(tnUiEvent.getComponent() != null && tnUiEvent.getComponent() instanceof FrogTextField)
                        {
                            FrogTextField textField = (FrogTextField)tnUiEvent.getComponent();
                            String text = textField.getText();
                            this.model.put(KEY_S_COMMON_SEARCH_TEXT, text);
                            textField.setText("");
                        }
                        break;
                    }
                    case CMD_STOP_SELECT:
                    {
                        AbstractTnComponent tnComponent = tnUiEvent.getComponent();
                        if(tnComponent instanceof FrogListItem)
                        {
                            int id = tnComponent.getId();
                            
                            MisLogManager misLogManager = MisLogManager.getInstance();
                            if (misLogManager.getFilter().isTypeEnable(IMisLogConstants.TYPE_ONEBOX_ADDRESS_SELECTION))
                            {
                                AddressMisLog log = misLogManager.getFactory().createOneBoxAddressSelectionMisLog();
                                PoiDataWrapper poiDataWrapper = (PoiDataWrapper) model.get(KEY_O_POI_DATA_WRAPPER);
                                Address address = poiDataWrapper.getAddress(id);
                                if (address.getStop() != null)
                                {
                                    log.setzEndLat(address.getStop().getLat());
                                    log.setzEndLon(address.getStop().getLon());
                                }
                                log.setzDisplayString(((FrogListItem)tnComponent).getText());
                                log.setTimestamp(System.currentTimeMillis());
                                log.setSearchUid(poiDataWrapper.getSearchUid());
                                log.setPageNumber(0);
                                log.setPageIndex(id);
                                Logger.log(Logger.INFO, this.getClass().getName(), IMisLogConstants.PROCESS_MISLOG, new Object[]
                                { log });
                            }
                            
                            this.model.put(KEY_I_POI_SELECTED_INDEX, id);
                        }
                        break;
                    }
                    case CMD_REMOVE_ALL:
                    {
                        model.put(KEY_S_COMMON_SEARCH_TEXT, "");
                        model.put(KEY_S_COMMON_SHOW_SEARCH_TEXT, "");
                        break;
                    }
                    case CMD_GOTO_BUSINESS:
                    {
                        freshTabButton(ID_TAB_BUTTON_BUSINESS, ID_TAB_BUTTON_ADDRESS);
                        break;
                    }
                    case CMD_GOTO_ADDRESS:
                    {
                        freshTabButton(ID_TAB_BUTTON_ADDRESS, ID_TAB_BUTTON_BUSINESS);
                        break;
                    }
                    case CMD_SELECT_ADDRESS:
                    {
                        CitizenAddressListItem item = ((CitizenAddressListItem) tnUiEvent.getComponent());
                        Object obj = this.model.get(KEY_O_TEXTFIELD_FILTER);
                        if ( obj instanceof Filter4TextField )
                        {
                            Filter4TextField filter = (Filter4TextField)obj;
                            Vector filterData = filter.getFilterData();
                            
                            Address selectedAddress = (Address) filterData.elementAt(item.getId());

                            model.put(KEY_O_VALIDATED_ADDRESS, selectedAddress);
                        }
                        else
                        {
                            Address address = item.getAddressData();
                            model.put(KEY_O_VALIDATED_ADDRESS, address);
                            setupTextField(address);
                        }
                        break;
                    }
                    case CMD_SELECT_CITY:
                    {
                        AbstractTnComponent component = tnUiEvent.getComponent();
                        CitizenAddressListItem item = (CitizenAddressListItem) component;
                        
                        CitizenTextField addressTextField = (CitizenTextField) getComponentInScreen(ID_ADDRESS_INPUT_FIELD);
                        CitizenTextField cityTextField = (CitizenTextField) getComponentInScreen(ID_CITY_INPUT_FIELD);
                        
                        String str = addressTextField.getText();
                        if (str != null && str.length() > 0)
                        {
                            cityTextField.setText(item.getAddress());

                            model.remove(KEY_O_VALIDATED_ADDRESS);
                            model.remove(KEY_B_FROM_LIST);
                            model.put(KEY_S_ADDRESS_LINE, str);
                            model.put(KEY_S_CITY, item.getAddress());
                        }
                        else
                        {
                            Address address = item.getAddressData();
                            model.put(KEY_O_VALIDATED_ADDRESS, address);
                            cityTextField.setText(item.getAddress());
                        }

                        removeList();
                        break;
                    }
                }
                break;
            }
        }
        return super.preProcessUIEvent(tnUiEvent);
    }
    
    
    protected void removeList()
    {
        AbstractTnContainer container = (AbstractTnContainer)getComponentInScreen(ID_ONEBOX_SEARCH_CONTAINER);
        if(container != null)
        {
            if (container.getChildrenSize() > 0
                    && container.get(container.getChildrenSize() - 1) instanceof FrogList)
            {
                container.remove(container.getChildrenSize() - 1);
            }
        }
    }
    
    protected void setupTextField(Address address)
    {
        if (address == null || address.getStop() == null)
        {
            return;
        }

        String firstLine = address.getStop().getFirstLine();
        String secondLine = ResourceManager.getInstance().getStringConverter()
                .convertSecondLine(createDisplayStop(address.getStop()));
        CitizenTextField addressField = (CitizenTextField) getComponentInScreen(ID_ADDRESS_INPUT_FIELD);
        if (addressField != null)
        {
            addressField.setText(firstLine);
        }
        
        CitizenTextField cityField = (CitizenTextField) getComponentInScreen(ID_CITY_INPUT_FIELD);
        if (cityField != null)
        {
            cityField.setText(secondLine);
        }
    }
    
    protected Stop createDisplayStop(Stop stop)
    {
        if (null == stop) return null;
        Stop displayStop = new Stop();
        displayStop.setCity(stop.getCity());
        displayStop.setProvince(stop.getProvince());
        return displayStop;
    }
    
    protected void cancelSuggestionSearch()
    {
        Object listAdapter = this.model.get(KEY_O_LIST_ADAPTER);
        if(listAdapter instanceof AutoSuggestListAdapter)
        {
            AutoSuggestListAdapter autoSuggestListAdapter = (AutoSuggestListAdapter)listAdapter;
            autoSuggestListAdapter.cancel();
        }
    }
    
    protected void freshTabButton(int focusId, int unfocusId)
    {
        CitizenScreen screen = (CitizenScreen) ((AbstractTnUiHelper) AbstractTnUiHelper
                .getInstance()).getCurrentScreen();
        AbstractTnComponent component = screen.getComponentById(unfocusId);
        setTabFocus((FrogButton)component, false);
        component.requestPaint();
        component = screen.getComponentById(focusId);
        setTabFocus((FrogButton)component, true);
        component.requestPaint();
        
        screen.getComponentById(focusId + ID_TAB_CONTAINER).setVisible(true);
        screen.getComponentById(unfocusId + ID_TAB_CONTAINER).setVisible(false);
        switch (focusId)
        {
            case ID_TAB_BUTTON_BUSINESS:
            {
                screen.getComponentById(ID_SUGGEST_LIST_COMP).setVisible(true);
                screen.getComponentById(ID_INPUT_AREA_CONTAINER).setVisible(false);
                break;
            }
            case ID_TAB_BUTTON_ADDRESS:
            {
                screen.getComponentById(ID_SUGGEST_LIST_COMP).setVisible(false);
                screen.getComponentById(ID_INPUT_AREA_CONTAINER).setVisible(true);
                break;
            }
        }
    }

    protected int getFocusTabId(TnScreen screen)
    {
        AbstractTnComponent businessSearchContentContainer = screen.getComponentById(ID_TAB_BUTTON_BUSINESS + ID_TAB_CONTAINER);
        if(businessSearchContentContainer == null)
        {
            return -1;
        }
        if(businessSearchContentContainer.isVisible())
        {
            return ID_TAB_BUTTON_BUSINESS;
        }
        else
        {
            return ID_TAB_BUTTON_ADDRESS;
        }
    }

    private void handleSelectSuggest(AbstractTnComponent abstractTnComponent, int id)
    {
        AbstractTnComponent listComp = findListComponent(abstractTnComponent);
        if(listComp != null)
        {
            FrogList list = (FrogList)listComp;
            AutoSuggestListAdapter adapter = (AutoSuggestListAdapter)list.getAdapter();
            Vector showList = adapter.getSuggestions();
            if(id < showList.size() && id >= 0)
            {
                ListItemData itemData = (ListItemData)showList.elementAt(id);
                model.put(KEY_I_DATA_TYPE, itemData.dataType);
                switch(itemData.dataType)
                {
                    case ListItemData.CATEGORY_TYPE:
                    {
                        int searchType = this.model.getInt(KEY_I_SEARCH_TYPE);
                        //will use name as search string when search city,use category id when other search.
                        if(searchType == IOneBoxSearchProxy.TYPE_SEARCH_CITY)
                        {
                            model.put(KEY_S_COMMON_SEARCH_TEXT, itemData.name);
                            model.remove(KEY_I_CATEGORY_ID);
                        }
                        else
                        {
                            model.put(KEY_I_CATEGORY_ID, itemData.categoryId);
                            model.remove(KEY_S_COMMON_SEARCH_TEXT);
                        }
                        model.put(KEY_S_COMMON_SHOW_SEARCH_TEXT, itemData.name);
                        break;
                    }
                    case ListItemData.FAV_TYPE:
                    {
                        Address addr = new Address(Address.SOURCE_FAVORITES);
                        if(itemData.address.getPoi() != null)
                        {
                            addr.setPoi(itemData.address.getPoi());
                            addr.setType(Address.TYPE_FAVORITE_POI);
                        }
                        else
                        {
                            addr.setStop(itemData.address.getStop());
                            addr.setType(Address.TYPE_FAVORITE_STOP);
                        }
                        model.put(KEY_S_COMMON_SHOW_SEARCH_TEXT, itemData.name);
                        model.put(KEY_O_SELECTED_ADDRESS, addr);
                        break;
                    }
                    case ListItemData.RECENT_STOP_TYPE:
                    {
                        Address addr = new Address(Address.SOURCE_RECENT_PLACES);
                        if(itemData.address.getPoi() != null)
                        {
                            addr.setPoi(itemData.address.getPoi());
                            addr.setType(Address.TYPE_RECENT_POI);
                        }
                        else
                        {
                            addr.setStop(itemData.address.getStop());
                            addr.setType(Address.TYPE_RECENT_STOP);
                        }
                        model.put(KEY_S_COMMON_SHOW_SEARCH_TEXT, itemData.name);
                        model.put(KEY_O_SELECTED_ADDRESS, addr);
                        setToReaded(adapter, itemData);
                        break;
                    }
                    case ListItemData.ONEBOX_LOCAL_TYPE:
                    case ListItemData.RECENT_SEARCH_STR_TYPE:
                    {
                        model.remove(KEY_I_CATEGORY_ID);
                        model.put(KEY_S_COMMON_SEARCH_TEXT, itemData.contentString);
                        model.put(KEY_S_COMMON_SHOW_SEARCH_TEXT, itemData.name);
                        break;
                    }
                    case ListItemData.ONEBOX_NETWORK_TYPE:
                    {
                        model.remove(KEY_I_CATEGORY_ID);
                        model.put(KEY_S_COMMON_SEARCH_TEXT, itemData.contentString);
                        model.put(KEY_S_COMMON_SHOW_SEARCH_TEXT, itemData.name);
                        break;
                    }
                    case ListItemData.AIRPORT_TYPE:
                    case ListItemData.HOME_TYPE:
                    case ListItemData.WORK_TYPE:
                    case ListItemData.CURRENT_LOCATION:
                    {
                        model.put(KEY_S_COMMON_SHOW_SEARCH_TEXT, itemData.name);
                        model.put(KEY_O_SELECTED_ADDRESS, itemData.address);
                        break;
                    }
                    case ListItemData.CONTACT_TYPE:
                    {
                        model.put(KEY_S_ADDRESS_LINE, itemData.address.getStop().getFirstLine());
                        model.put(KEY_S_CITY, itemData.address.getStop().getCity());
                        model.put(KEY_B_DETAIL_FROM_CONTACT, true);
                        model.put(KEY_S_LABEL_FROM_CONTACT, itemData.name);
                        model.put(KEY_S_PTN_FROM_CONTACT, itemData.ptnString);
                        break;
                    }
                    default:
                    {
                        model.put(KEY_S_COMMON_SEARCH_TEXT, itemData.name);
                        model.put(KEY_S_COMMON_SHOW_SEARCH_TEXT, itemData.name);
                        break;
                    }
                }
            }
        }
    }

    private AbstractTnComponent findListComponent(AbstractTnComponent abstractTnComponent)
    {
        AbstractTnComponent parentComp = abstractTnComponent.getParent();
        
        while(!(parentComp instanceof FrogList))
        {
            parentComp = parentComp.getParent();
        }
        return parentComp;
    }

    private CitizenScreen createOneBoxSearchScreen()
    {
        CitizenScreen screen = UiFactory.getInstance().createScreen(STATE_ONE_BOX_MAIN);
        
        TnMenu screenMenu = screen.getRootContainer().getMenu(AbstractTnComponent.TYPE_MENU);
        String clearMenuStr = ResourceManager.getInstance().getCurrentBundle()
                .getString(IStringPoi.MENU_CLEAR_SEARCH_HISTORY, IStringPoi.FAMILY_POI);        
        if(DaoManager.getInstance().getAddressDao().getRecentSearch().size() > 0)
        {
            screenMenu.add(clearMenuStr, CMD_CLEAR_SEARCH_HISTORY, 0);
        }
        
        AbstractTnContainer searchContainer = UiFactory.getInstance().createLinearContainer(ID_SEARCH_CONTAINER, true);
        getSearchContainer(searchContainer);
        screen.getContentContainer().add(searchContainer);
        
        screen.getContentContainer().setBackgroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_WH));
        
        return screen;
    }
    
    private void getSearchContainer(AbstractTnContainer searchContainer)
    {
        String hint = ResourceManager.getInstance().getCurrentBundle()
                .getString(IStringCommon.RES_INPUT_HINT, IStringCommon.FAMILY_COMMON);
        boolean isFromSetLocation = this.model.getBool(KEY_B_IS_CHOOSING_LOCATION);
        boolean isStaticNavRunning = NavRunningStatusProvider.getInstance().isNavRunning() && NavRunningStatusProvider.getInstance().getNavType() == NavRunningStatusProvider.NAV_TYPE_STATIC_ROUTE;
        boolean isChangeAnchorDisabled = ((this.model.getInt(KEY_I_SEARCH_TYPE) == IPoiSearchProxy.TYPE_SEARCH_ALONG_ROUTE)
                || (isStaticNavRunning && model.get(KEY_O_ADDRESS_DEST) != null))
                || model.getBool(KEY_B_IS_SEARCH_NEAR_BY);
        boolean isDWF = this.model.getBool(KEY_B_NEED_CURRENT_LOCATION);
        
        //TextField
        TnLinearContainer container = UiFactory.getInstance().createLinearContainer(ID_ONEBOX_SEARCH_CONTAINER, true,
            AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);
        container.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, uiDecorator.SCREEN_WIDTH);
        container.setBackgroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.BG_NAV_TITLE));
        
        if (this.model.getBool(KEY_B_ONEBOX_SEARCH))
        {
            TnLinearContainer inputAreaContainer = UiFactory.getInstance().createLinearContainer(0, false, AbstractTnGraphics.VCENTER | AbstractTnGraphics.HCENTER);
            inputAreaContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((OneBoxSearchUiDecorator) uiDecorator).ONEBOX_SEARCH_CONTAINER_WIDTH);
            inputAreaContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((OneBoxSearchUiDecorator) uiDecorator).ONEBOX_SEARCH_CONTAINER_HEIGHT);
            container.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((OneBoxSearchUiDecorator) uiDecorator).ONEBOX_SEARCH_CONTAINER_HEIGHT);
            final CitizenTextField inputTextField = UiFactory.getInstance().createCitizenTextField("", 100, 0, hint, ID_ONEBOX_INPUT_FIELD, 
                ImageDecorator.IMG_AC_BACKSPACE.getImage());


            if((isFromSetLocation & !isDWF) || isChangeAnchorDisabled)
            {
                inputTextField.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((OneBoxSearchUiDecorator) uiDecorator).ONEBOX_TAB_ADDRESS_TEXTBOX_WIDTH);
            }
            else
            {
                inputTextField.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((OneBoxSearchUiDecorator) uiDecorator).ONEBOX_TAB_BUSINESS_TEXTBOX_WIDTH_SHORT);

                
                FrogNullField buttonGapField = UiFactory.getInstance().createNullField(0);
                buttonGapField.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((OneBoxSearchUiDecorator) uiDecorator).ONEBOX_BUTTON_GAP_WIDTH);

                inputAreaContainer.add(createLocationButton());
                inputAreaContainer.add(buttonGapField);
            }
            
            inputTextField.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT,
                ((OneBoxSearchUiDecorator) uiDecorator).ONEBOX_SEARCH_TEXTBOX_HEIGHT);
            inputTextField.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_TEXT_FIELD));
            inputTextField.setCommandEventListener(this);
            inputTextField.setBackspaceCommand(100);
            
            FrogButton searchButton = UiFactory.getInstance().createButton(0, "");
            searchButton.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, NinePatchImageDecorator.SEARCH_BOX_BUTTON_UNFOCUS);
            searchButton.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.SEARCH_BOX_BUTTON_FOCUSED);
            searchButton.setPadding(0, 0, 0, 0);
            searchButton.setIcon(ImageDecorator.IMG_SEARCHBOX_ICON_CONTACT_FOCUSED.getImage(),
                ImageDecorator.IMG_SEARCHBOX_ICON_CONTACT_UNFOCUSED.getImage(), AbstractTnGraphics.TOP
                        | AbstractTnGraphics.HCENTER);
            TnMenu menuSearch = UiFactory.getInstance().createMenu();
            menuSearch.add("", CMD_CONTACTS);
            searchButton.setMenu(menuSearch, AbstractTnComponent.TYPE_CLICK);
            searchButton.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH,  ((OneBoxSearchUiDecorator) uiDecorator).ONEBOX_SEARCH_BUTTON_WIDTH);
            searchButton.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((OneBoxSearchUiDecorator) uiDecorator).ONEBOX_SEARCH_BUTTON_HEIGHT);
            
            FrogNullField buttonGapField2 = UiFactory.getInstance().createNullField(0);
            buttonGapField2.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((OneBoxSearchUiDecorator) uiDecorator).ONEBOX_BUTTON_GAP_WIDTH);
            
            
            inputAreaContainer.add(inputTextField);
            inputAreaContainer.add(buttonGapField2);
            inputAreaContainer.add(searchButton);
            
            container.add(inputAreaContainer);
            searchContainer.add(container);
            searchContainer.add(createFrogList(true, inputTextField));
            
            inputTextField.requestTextFieldFocus();
            inputTextField.setIsNeedShowInputMethod(true);
        }
        else
        {
            addBusinessAndAddressTab(container);
            searchContainer.add(container);
//            FrogList bussinessList = createFrogList(false, (CitizenTextField)searchContainer.getComponentById(ID_ONEBOX_INPUT_FIELD));
//            bussinessList.setVisible(searchContainer.getComponentById(ID_TAB_CONTAINER + ID_TAB_BUTTON_BUSINESS).isVisible());
//            searchContainer.add(bussinessList);
            TnLinearContainer addressList = UiFactory.getInstance().createLinearContainer(ID_INPUT_AREA_CONTAINER, true,
                AbstractTnGraphics.HCENTER);
//            addressList.setVisible(!searchContainer.getComponentById(ID_TAB_CONTAINER + ID_TAB_BUTTON_BUSINESS).isVisible());
            addressList.setVisible(true);
            searchContainer.add(addressList);
            addressList.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, uiDecorator.SCREEN_HEIGHT);
        }
    }
    
    protected void addBusinessAndAddressTab(TnLinearContainer container)
    {
        container.setBackgroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.ONEBOX_TAB_CONTAINER_BG_COLOR));
//        TnLinearContainer tabContainer = UiFactory.getInstance().createLinearContainer(0, false, AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);
//        tabContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((OneBoxSearchUiDecorator) uiDecorator).ONEBOX_SEARCH_CONTAINER_WIDTH);
//        tabContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((OneBoxSearchUiDecorator) uiDecorator).TAB_HEIGHT);
        
//        int unfocusedColor = UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_AC_TAB_BUTTON_UNFOCUSED);
//        String label = ResourceManager.getInstance().getCurrentBundle().getString(IStringCommon.RES_ONEBOX_TAB_BUSINESS, IStringCommon.FAMILY_COMMON);
//        FrogButton businessButton = createTabButton(ID_TAB_BUTTON_BUSINESS, label, unfocusedColor, CMD_GOTO_BUSINESS);
//        setTabFocus(businessButton, true);
        
//        label = ResourceManager.getInstance().getCurrentBundle().getString(IStringCommon.RES_ONEBOX_TAB_ADDRESS, IStringCommon.FAMILY_COMMON);
//        FrogButton addressButton = createTabButton(ID_TAB_BUTTON_ADDRESS, label, unfocusedColor, CMD_GOTO_ADDRESS);
//        setTabFocus(addressButton, false);
        
//        FrogNullField placeHolder = UiFactory.getInstance().createNullField(0);
//        placeHolder.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((OneBoxSearchUiDecorator) uiDecorator).TAB_BUTTON_GAP_WIDTH);
//        
//        FrogNullField placeHolder2 = UiFactory.getInstance().createNullField(0);
//        placeHolder2.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((OneBoxSearchUiDecorator) uiDecorator).TAB_BUTTON_GAP_WIDTH);

//        FrogButton searchbutton = createLocationButton();
//        TnLinearContainer buttonContainer = UiFactory.getInstance().createLinearContainer(0, false, AbstractTnGraphics.HCENTER | AbstractTnGraphics.TOP);
//        buttonContainer.add(searchbutton);
//        buttonContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((OneBoxSearchUiDecorator) uiDecorator).ONEBOX_SEARCH_BUTTON_WIDTH);
//        buttonContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((OneBoxSearchUiDecorator) uiDecorator).SEARCH_BUTTON_CONTAINER_HEIGT);
//        tabContainer.add(buttonContainer);
//        tabContainer.add(placeHolder);
//        tabContainer.add(businessButton);
//        tabContainer.add(placeHolder2);
//        tabContainer.add(addressButton);
//        container.add(tabContainer);
//        addTabContent(container, ID_TAB_BUTTON_BUSINESS);
        addTabContent(container, ID_TAB_BUTTON_ADDRESS);
    }
    
    private void setTabFocus(FrogButton tab, boolean isFocused)
    {
        int unfocusedColor = UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_AC_TAB_BUTTON_UNFOCUSED);
        int focusedColor = UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_AC_TAB_BUTTON_FOCUSED);
        if(isFocused)
        {
            tab.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, NinePatchImageDecorator.ONEBOX_TAB_BUTTON_FOCUSED);
            tab.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.ONEBOX_TAB_BUTTON_FOCUSED);
            tab.setForegroundColor(focusedColor, focusedColor);
        }
        else
        {
            tab.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, NinePatchImageDecorator.ONEBOX_TAB_BUTTON_UNFOCUSED);
            tab.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.ONEBOX_TAB_BUTTON_UNFOCUSED);
            tab.setForegroundColor(unfocusedColor, unfocusedColor);
        }
    }
    
    protected FrogButton createTabButton(int id, String label, int color, int cmd)
    {
        FrogButton tabButton = UiFactory.getInstance().createButton(id, label);
        tabButton.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.ONEBOX_TAB_BUTTON_FOCUSED);
        tabButton.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((OneBoxSearchUiDecorator)uiDecorator).TAB_BUTTON_WIDTH);
        tabButton.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((OneBoxSearchUiDecorator)uiDecorator).TAB_BUTTON_HEIGHT);
        tabButton.setForegroundColor(color, color);
        tabButton.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_TAB_FONT));
        TnMenu menu = UiFactory.getInstance().createMenu();
        menu.add("", cmd);
        tabButton.setMenu(menu, AbstractTnComponent.TYPE_CLICK);
        tabButton.setPadding(0, 0, 0, ((OneBoxSearchUiDecorator)uiDecorator).TAB_BUTTON_ARROW_HEIGHT.getInt());
        return tabButton;
    }
    
    protected void addTabContent(TnLinearContainer container, int tabBtnId)
    {
        TnLinearContainer inputAreaContainer = UiFactory.getInstance().createLinearContainer(ID_TAB_CONTAINER + tabBtnId, true, AbstractTnGraphics.VCENTER | AbstractTnGraphics.HCENTER);
        TnLinearContainer dropdownContainer = UiFactory.getInstance().createLinearContainer(0, false,
            AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);
        dropdownContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((OneBoxSearchUiDecorator) uiDecorator).ONEBOX_SEARCH_CONTAINER_WIDTH);
        inputAreaContainer.add(dropdownContainer);
        inputAreaContainer.setBackgroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.ONEBOX_TAB_CONTENT_BG_COLOR));
        inputAreaContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((OneBoxSearchUiDecorator) uiDecorator).ONEBOX_SEARCH_CONTAINER_WIDTH);
        
        CitizenTextField inputTextField = null;
        
        FrogButton searchButton = null;
        
        FrogNullField buttonGapField = UiFactory.getInstance().createNullField(0);
        buttonGapField.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((OneBoxSearchUiDecorator) uiDecorator).ONEBOX_BUTTON_GAP_WIDTH);

        
        switch (tabBtnId)
        {
            case ID_TAB_BUTTON_BUSINESS:
            {
                inputTextField = UiFactory.getInstance().createCitizenTextField("", 100, 0, ResourceManager.getInstance().getCurrentBundle()
                    .getString(IStringCommon.RES_ONEBOX_TAB_BUSINESS_HINT, IStringCommon.FAMILY_COMMON), ID_ONEBOX_INPUT_FIELD, 
                    ImageDecorator.IMG_AC_BACKSPACE.getImage());
                inputTextField.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((OneBoxSearchUiDecorator) uiDecorator).ONEBOX_TAB_BUSINESS_TEXTBOX_WIDTH);
                
                searchButton = UiFactory.getInstance().createButton(0, "");
                TnMenu btnMenu = UiFactory.getInstance().createMenu();
                btnMenu.add("", CMD_DO_ONEBOX_SEARCH);
                searchButton.setMenu(btnMenu, AbstractTnComponent.TYPE_CLICK);

                inputTextField.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((OneBoxSearchUiDecorator) uiDecorator).ONEBOX_SEARCH_TEXTBOX_HEIGHT);
                inputTextField.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_TEXT_FIELD));
                inputTextField.setCommandEventListener(this);
                inputTextField.setBackspaceCommand(100);

                searchButton.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, NinePatchImageDecorator.SEARCH_BOX_BUTTON_UNFOCUS);
                searchButton.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.SEARCH_BOX_BUTTON_FOCUSED);
                searchButton.setIcon(ImageDecorator.IMG_AC_FIND_FOCUSED.getImage(), ImageDecorator.IMG_AC_FIND_UNFOCUSED.getImage(), AbstractTnGraphics.TOP | AbstractTnGraphics.HCENTER);
                searchButton.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((OneBoxSearchUiDecorator) uiDecorator).ONEBOX_SEARCH_BUTTON_WIDTH);
                searchButton.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((OneBoxSearchUiDecorator) uiDecorator).ONEBOX_SEARCH_BUTTON_HEIGHT);
                searchButton.setPadding(0, 0, 0, 0);

                dropdownContainer.add(inputTextField);
                dropdownContainer.add(buttonGapField);
                dropdownContainer.add(searchButton);
                inputAreaContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((OneBoxSearchUiDecorator) uiDecorator).ONEBOX_BUSINESS_SEARCH_CONTAINER_HEIGHT);

                inputTextField.requestTextFieldFocus();
                inputTextField.setIsNeedShowInputMethod(true);
                break;
            }
            case ID_TAB_BUTTON_ADDRESS:
            {
//                inputAreaContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, 
//                    ((OneBoxSearchUiDecorator) uiDecorator).ONEBOX_SEARCH_AREA_HEIGHT);
//                inputAreaContainer.setVisible(false);
                TnLinearContainer contentContaienr = UiFactory.getInstance().createLinearContainer(0, true,
                    AbstractTnGraphics.HCENTER);
//                inputAreaContainer.setVisible(false);
                inputAreaContainer.setVisible(true);
                inputAreaContainer.add(contentContaienr);
                initInputArea(contentContaienr);
                if(contentContaienr.getChildrenSize() > 1)
                {
                    contentContaienr.get(0).getTnUiArgs().remove(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS);
                    contentContaienr.get(0).getTnUiArgs().remove(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS);
                    contentContaienr.get(1).getTnUiArgs().remove(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS);
                    contentContaienr.get(1).getTnUiArgs().remove(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS);
                    ((AbstractTnContainer)contentContaienr.get(1)).get(0).getTnUiArgs().remove(CitizenTextField.KEY_PREFER_TEXTFIELD_HEIGHT);
                    if(contentContaienr.get(0) instanceof AbstractTnContainer && ((AbstractTnContainer)contentContaienr.get(0)).getChildrenSize() > 0)
                    {
                        ((AbstractTnContainer)contentContaienr.get(0)).get(0).getTnUiArgs().remove(CitizenTextField.KEY_PREFER_TEXTFIELD_HEIGHT);
                        ((AbstractTnContainer)contentContaienr.get(0)).get(0).getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT,
                            ((OneBoxSearchUiDecorator) uiDecorator).ONEBOX_SEARCH_TEXTBOX_HEIGHT);
                    }
                    if(contentContaienr.get(1) instanceof AbstractTnContainer && ((AbstractTnContainer)contentContaienr.get(1)).getChildrenSize() > 0)
                    {
                        ((AbstractTnContainer)contentContaienr.get(1)).get(0).getTnUiArgs().remove(CitizenTextField.KEY_PREFER_TEXTFIELD_HEIGHT);
                        ((AbstractTnContainer)contentContaienr.get(1)).get(0).getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT,
                            ((OneBoxSearchUiDecorator) uiDecorator).ONEBOX_SEARCH_TEXTBOX_HEIGHT);
                    }
                }
                initSearchList();
                break;
            }
        }
        container.add(inputAreaContainer);
        
        String region = getRegion();
        model.put(KEY_S_LAST_INTERNATIONAL_SEARCH_REGION, region);
    }

    private FrogButton createLocationButton()
    {
        FrogButton locationButton = UiFactory.getInstance().createButton(0, "");
        locationButton.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, NinePatchImageDecorator.SEARCH_BOX_BUTTON_UNFOCUS);
        locationButton.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.SEARCH_BOX_BUTTON_FOCUSED);
        locationButton.setPadding(0, 0, 0, 0);
        locationButton.setIcon(ImageDecorator.SEARCHBOX_ICON_SELECT_LOCATION_FOCUSED.getImage(), ImageDecorator.SEARCHBOX_ICON_SELECT_LOCATION_UNFOCUSED.getImage(), AbstractTnGraphics.TOP | AbstractTnGraphics.HCENTER);
        TnMenu menu = UiFactory.getInstance().createMenu();
        menu.add("", CMD_GOTO_CHANGE_LOCATION);
        locationButton.setMenu(menu, AbstractTnComponent.TYPE_CLICK);
        locationButton.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((OneBoxSearchUiDecorator) uiDecorator).ONEBOX_SEARCH_BUTTON_WIDTH);
        locationButton.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((OneBoxSearchUiDecorator) uiDecorator).ONEBOX_SEARCH_BUTTON_HEIGHT);
        return locationButton;
    }

    protected FrogList createFrogList(boolean isOneboxSearch, CitizenTextField inputTextField)
    {
        FrogList list = UiFactory.getInstance().createList(ID_SUGGEST_LIST_COMP);
        list.setTouchEventListener(this);

        Stop stop = null;
        if(model.get(KEY_O_ADDRESS_ORI) instanceof Address)
        {
            Address origin = (Address)model.get(KEY_O_ADDRESS_ORI);
            stop = origin.getStop(); 
        }
        else
        {
            stop = this.getAnchor();
        }

        boolean needCurrentLocation = this.model.getBool(KEY_B_NEED_CURRENT_LOCATION);
        IUserProfileProvider userProfileProvider = (IUserProfileProvider)model.get(KEY_O_USER_PROFILE_PROVIDER);
        AutoSuggestListAdapter autoSuggestFilter = new AutoSuggestListAdapter(
                inputTextField, list, stop, (OneBoxSearchUiDecorator) uiDecorator,
                userProfileProvider, this.getRegion(), needCurrentLocation);
        autoSuggestFilter.setOneboxSearch(isOneboxSearch);
        autoSuggestFilter.setCurrentAnchorAddress((Address) this.model.get(KEY_O_ANCHOR_CURRENT_ADDRESS));
        
        String transactionId = System.currentTimeMillis() + "";
        this.model.put(KEY_S_TRANSACTION_ID, transactionId);
        autoSuggestFilter.setTransactionId(transactionId);
        
        list.setAdapter(autoSuggestFilter);
        inputTextField.setTextChangeListener(autoSuggestFilter);
        model.put(KEY_O_LIST_ADAPTER, autoSuggestFilter);
        
        PoiCategory categoryTreeNodes = DaoManager.getInstance().getResourceBarDao().getCategoryNode(this.getRegion());
        if(categoryTreeNodes != null && categoryTreeNodes.getChildrenSize() > 0)
        {
            int size = categoryTreeNodes.getChildrenSize();
            PoiCategory node = null;
            for(int i = 0; i < size; i++)
            {
                node = categoryTreeNodes.getChildAt(i);
                String complete = node.getName();
                if(complete.toLowerCase().startsWith("complete"))//TODO: remove hard code.
                {
                    break;
                }
            }
        }
        autoSuggestFilter.filter("");
        
        return list;
    }

    protected int transformCommandDelegate(int state, TnUiEvent tnUiEvent)
    {
        boolean isOneboxMode = model.getBool(KEY_B_ONEBOX_SEARCH);
        if(isOneboxMode)
        {
            switch(state)
            {
                case STATE_ONE_BOX_MAIN:
                {

                    if (tnUiEvent.getComponent() instanceof CitizenAddressListItem
                            && ((tnUiEvent.getType() == TnUiEvent.TYPE_TOUCH_EVENT && tnUiEvent
                                    .getMotionEvent().getAction() == TnMotionEvent.ACTION_UP)
                                    || (tnUiEvent.getType() == TnUiEvent.TYPE_KEY_EVENT
                                    && tnUiEvent.getKeyEvent().getAction() == TnKeyEvent.ACTION_UP && tnUiEvent
                                    .getKeyEvent().getCode() == TnKeyEvent.KEYCODE_PAD_CENTER)))
                    {
                        cancelSuggestionSearch();
                        handleSelectSuggest(tnUiEvent.getComponent(), tnUiEvent
                                .getComponent().getId());
                        int dataType = model.getInt(KEY_I_DATA_TYPE);
                        switch (dataType)
                        {
                            case ListItemData.CATEGORY_TYPE:
                            {
                                return CMD_DO_CATELOG_SEARCH;
                            }
                            case ListItemData.ONEBOX_NETWORK_TYPE:
                            {
                                return CMD_DO_ONEBOX_SEARCH;
                            }
                            case ListItemData.ONEBOX_LOCAL_TYPE:
                            case ListItemData.RECENT_SEARCH_STR_TYPE:
                            {
                                return CMD_DO_ONEBOX_SEARCH;
                            }
                            case ListItemData.FAV_TYPE:
                            case ListItemData.RECENT_STOP_TYPE:
                            case ListItemData.AIRPORT_TYPE:
                            case ListItemData.HOME_TYPE:
                            case ListItemData.WORK_TYPE:
                            case ListItemData.CURRENT_LOCATION:
                            {
                                return CMD_SELECT_RECENT_FAV;
                            }
                            case ListItemData.SET_HOME_TYPE:
                            {
                                return CMD_ONEBOX_GO_TO_SET_HOME;
                            }
                            case ListItemData.SET_WORK_TYPE:
                            {
                                return CMD_ONEBOX_GO_TO_SET_WORK;
                            }
                            case ListItemData.CONTACT_TYPE:
                            {
                                return CMD_VALIDATE_CONTACT_ADDRESS;
                            }
                        }

                    }
                    else if( tnUiEvent.getType() == TnUiEvent.TYPE_KEY_EVENT
                            &&tnUiEvent.getKeyEvent().getAction() == TnKeyEvent.ACTION_UP
                            &&tnUiEvent.getKeyEvent().getCode() == TnKeyEvent.KEYCODE_ENTER)
                    {
                        
                        AbstractTnComponent textFieldComp = ((AbstractTnUiHelper)AbstractTnUiHelper.getInstance()).getCurrentScreen().getComponentById(ID_ONEBOX_INPUT_FIELD);
                        if(textFieldComp instanceof CitizenTextField)
                        {
                            cancelSuggestionSearch();
                            String text =((CitizenTextField)textFieldComp).getText();
                            if(text != null && text.trim().length() > 0)
                            {
                                model.put(KEY_S_COMMON_SEARCH_TEXT, text);
                                model.put(KEY_S_COMMON_SHOW_SEARCH_TEXT, text);
                                model.put(KEY_B_IS_SAVE_TEXT_RECENT_SEARCH, true);
                               
                                return CMD_DO_ONEBOX_SEARCH;
                            }
                        }
                        
                    }
                }
            }
        }
        else
        {
            switch (state)
            {
                case STATE_ONE_BOX_MAIN:
                {
                    if (tnUiEvent.getType() == TnUiEvent.TYPE_KEY_EVENT
                            && tnUiEvent.getKeyEvent().getAction() == TnKeyEvent.ACTION_UP
                            && tnUiEvent.getKeyEvent().getCode() == TnKeyEvent.KEYCODE_ENTER)
                    {
                        AbstractTnComponent component = tnUiEvent.getComponent();
                        if (component != null)
                        {
                            int id = component.getId() - CitizenTextField.KEY_ID_OPERATOR_NUMBER;
                            if(id == ID_CITY_INPUT_FIELD)
                            {
                                CitizenTextField addressTextField = (CitizenTextField) getComponentInScreen(ID_ADDRESS_INPUT_FIELD);
                                CitizenTextField cityTextField = (CitizenTextField) getComponentInScreen(ID_CITY_INPUT_FIELD);
                                
                                model.put(KEY_S_ADDRESS_LINE, addressTextField.getText());
                                model.put(KEY_S_CITY, cityTextField.getText());
                                model.remove(KEY_O_VALIDATED_ADDRESS);
                                model.remove(KEY_B_FROM_LIST);
                                return CMD_SAVE;
                            }
                        }
                    }
                    break;
                }
            }
        }
        return CMD_NONE;
    }

    protected boolean updatePopup(int state, TnPopupContainer popup)
    {
        return false;
    }

    
    private boolean isOneBoxTypeChanged(boolean isOneBoxSupport)
    {
        boolean oldValue = model.getBool(KEY_B_ONEBOX_SEARCH);
        boolean oneBoxTypeChange = (isOneBoxSupport != oldValue);
        return oneBoxTypeChange;
    }
    
    private boolean isAddressFocused(TnScreen screen, boolean isOneBoxSupport)
    {
        if (!isOneBoxSupport)
        {
            int focusTabId = getFocusTabId(screen);
            if (focusTabId == -1)
            {
                return false;
            }
            if (focusTabId == ID_TAB_BUTTON_ADDRESS)
            {
                return true;
            }
        }
        return false;
    }
    
    private boolean isNeedReload()
    {
        String lastInternationalSearchRegion = model.getString(KEY_S_LAST_INTERNATIONAL_SEARCH_REGION);
        String currentRegion = getRegion();
        if(lastInternationalSearchRegion != null && !lastInternationalSearchRegion.equals(currentRegion))
        {
            return true;
        }
        return false;
    }
    
    private void reloadAddressPage(TnScreen screen)
    {
        CitizenWebComponent addressWebComponent = (CitizenWebComponent) screen
                .getComponentById(ID_ADDRESS_WEB_COMPONENT);
        BrowserSessionArgs args = new BrowserSessionArgs(CommManager.ACE_URL_DOMAIN_ALIAS);
        String url = args.getUrl();     
        url = BrowserSdkModel.addEncodeTnInfo(url, this.getRegion());
        url = BrowserSdkModel.appendWidthHeightToUrl(url);
        Logger.log(Logger.INFO, this.getClass().getName(), "ACE url " + url);
        
        addressWebComponent.loadUrl(url);
        model.put(KEY_S_LAST_INTERNATIONAL_SEARCH_REGION, getRegion());
    }
    
    protected boolean updateScreen(int state, TnScreen screen)
    {
        switch(state)
        {
            case STATE_ONE_BOX_MAIN:
            {
                boolean isOneboxSupport = ((OneBoxSearchModel)model).isOneBoxSupported();
                boolean isNeedRelayout = isOneBoxTypeChanged(isOneboxSupport);
                boolean isNeedReloadWebView = isAddressFocused(screen, isOneboxSupport) && isNeedReload();
                if (isNeedRelayout)
                {
                    model.put(KEY_B_ONEBOX_SEARCH, isOneboxSupport);
                    AbstractTnContainer searchContainer = (AbstractTnContainer) screen
                            .getComponentById(ID_SEARCH_CONTAINER);
                    if (searchContainer != null)
                    {
                        searchContainer.removeAll();
                        getSearchContainer(searchContainer);
                        return true;
                    }
                }
                else if (isNeedReloadWebView)
                {
                    reloadAddressPage(screen);
                }
                else
                {
                    AbstractTnComponent listComp = ((CitizenScreen) screen).getComponentById(ID_SUGGEST_LIST_COMP);
                    if (listComp instanceof FrogList)
                    {
                        FrogAdapter filter = ((FrogList) listComp).getAdapter();
                        if (filter instanceof AutoSuggestListAdapter)
                        {
                            AbstractTnComponent changeLocComp = ((CitizenScreen)screen).getComponentById(ID_CHANGE_LOCATION_COMP);
                            if (changeLocComp instanceof FrogLabel)
                            {
                                Object addrObj = this.model.get(KEY_O_ADDRESS_ORI);
                                if (addrObj instanceof Address)
                                {
                                    Stop stop = ((Address) addrObj).getStop();

                                    if (stop != null)
                                    {
                                        String label = getSearchLocationLabel(stop);
                                        ((FrogLabel) changeLocComp).setText(label);
                                        ((AutoSuggestListAdapter) filter)
                                                .setAnchorStop(((Address) addrObj).getStop());
                                    }
                                }
                            }
                            if(this.model.fetchBool(KEY_B_CLEAR_SEARCH_HISTORY) || this.model.fetchBool(KEY_B_NEED_REFRESH_AUTO_SUGGESTION))
                            {
                                AbstractTnComponent textFieldComp = ((CitizenScreen)screen).getComponentById(ID_ONEBOX_INPUT_FIELD);
                                if(textFieldComp instanceof CitizenTextField)
                                {
                                    String input = ((CitizenTextField)textFieldComp).getText();
                                    ((AutoSuggestListAdapter)filter).filter(input);
                                }
                                if (this.model.fetchBool(KEY_B_CLEAR_SEARCH_HISTORY))
                                {
                                    TnMenu menu = screen.getRootContainer().getMenu(AbstractTnComponent.TYPE_MENU);
                                    menu.remove(CMD_CLEAR_SEARCH_HISTORY);
                                    menu.add(ResourceManager.getInstance().getCurrentBundle().getString(IStringCommon.RES_BTTN_SEARCH,
                                        IStringCommon.FAMILY_COMMON), CMD_DO_ONEBOX_SEARCH, 0);
                                    menu.remove(ICommonConstants.CMD_COMMON_EXIT);
                                }
                            }
                            else
                            {
                                TnMenu menu = screen.getRootContainer().getMenu(AbstractTnComponent.TYPE_MENU);
                                int size = menu.size();
                                boolean hasHistory = false;
                                for(int i = 0; i < size; i++)
                                {
                                    TnMenuItem menuItem = menu.getItem(i);
                                    int id = menuItem.getId();
                                    if(id == CMD_CLEAR_SEARCH_HISTORY)
                                    {
                                        hasHistory = true;
                                        break;
                                    }
                                }
                                if(!hasHistory && DaoManager.getInstance().getAddressDao().getRecentSearch().size() > 0)
                                {
                                    menu.add(ResourceManager.getInstance().getCurrentBundle()
                                        .getString(IStringPoi.MENU_CLEAR_SEARCH_HISTORY, IStringPoi.FAMILY_POI), CMD_CLEAR_SEARCH_HISTORY,0);
                                    menu.remove(CMD_DO_ONEBOX_SEARCH);
                                }
                            }

                        }
                    }
                    
                    AbstractTnComponent cityBtn = screen.getComponentById(ID_BUTTON_CITY);
                    if(cityBtn instanceof FrogButton){
                        ((FrogButton)cityBtn).setText(((CitiesDao) DaoManager.getInstance().getNearCitiesDao()).getDefaultCity().getCity());
                    }
                }
                
                break;
            }
            default:
                break;
        }
        return false;
    }
    
    private String getSearchLocationLabel(Stop stop)
    {
        String label = "";
        if (stop.getFirstLine() != null && stop.getFirstLine().trim().length() > 0)
        {
            label = stop.getFirstLine();
        }
        else if (stop.getLabel() != null && stop.getLabel().trim().length() > 0)
        {
            label = stop.getLabel();
        }

        if (label == null || label.trim().length() == 0)
        {
            label = this.getAddressText(stop);
        }

        if (label == null || label.length() == 0)
        {
            label = ResourceManager
                    .getInstance()
                    .getCurrentBundle()
                    .getString(IStringCommon.RES_CURRENT_LOCATION,
                        IStringCommon.FAMILY_COMMON);
        }

        return label;
    }
    
    private String getAddressName(Address addr)
    {
        String addrName = "";
        if(addr != null && addr.getStop() != null)
        {
            addrName = getAddressText(addr.getStop());
            
        }
        return addrName;
    }
    
    private String getAddressText(Stop stop)
    {
        return ResourceManager.getInstance().getStringConverter().convertAddress(stop, false);
    }
    private Stop getAnchor()
    {
        Stop anchor = null;
        // 1. Get precisely gps fix
        TnLocation location = LocationProvider.getInstance().getCurrentLocation(
            LocationProvider.TYPE_GPS);
        // 2. Get network fix
        if (location == null)
        {
            location = LocationProvider.getInstance().getCurrentLocation(
                LocationProvider.TYPE_NETWORK);
        }

        if (location == null)
        {
            // 3. Get last know gps fix
            location = LocationProvider.getInstance().getLastKnownLocation(
                LocationProvider.TYPE_GPS);
            // 4. Get last know network fix
            if (location == null)
            {
                location = LocationProvider.getInstance().getLastKnownLocation(
                    LocationProvider.TYPE_NETWORK);
            }
            // check last know
            if (location != null)
            {
                anchor = new Stop();
                anchor.setLat(location.getLatitude());
                anchor.setLon(location.getLongitude());
            }
        }
        else
        {
            anchor = new Stop();
            anchor.setLat(location.getLatitude());
            anchor.setLon(location.getLongitude());
        }

        if (anchor == null)
        {
            anchor = new Stop();
            anchor.setLat(3737220);
            anchor.setLon(-12200021);
        }
        return anchor;
    }
    
    private void setToReaded(AutoSuggestListAdapter adapter, ListItemData itemData)
    {
        Vector recentVector = DaoManager.getInstance().getAddressDao().getRecentAddresses();
        for(int i = 0; i < recentVector.size(); i++)
        {
            Address address = (Address)recentVector.get(i);
            if(address.getId() == itemData.address.getId())
            {
                address.setReadByUser(true);
                break;
            }
        }
    }
    
    class SuggestListAdapter implements FrogAdapter
    {
        public AbstractTnComponent getComponent(int position,
                AbstractTnComponent convertComponent, AbstractTnComponent parent)
        {
            FrogListItem item = null;
            Vector suggestions = model.getVector(KEY_V_RESULT_SUGGESTIONS);
            if(suggestions == null)
            {
                return null;
            }
            OneBoxSearchBean suggestion = (OneBoxSearchBean)suggestions.get(position);
            
            if (suggestion == null)
            {
                return null;
            }

            if (convertComponent instanceof FrogListItem)
            {
                item = (FrogListItem) convertComponent;
                item.setText(suggestion.getKey());
                item.setId(position);
               
            }
            else
            {
                item = UiFactory.getInstance().createListItem(position);
                item.setText(suggestion.getKey());
                item.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.LIST_ITEM_FOCUSED);
                item.getTnUiArgs()
                        .put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, NinePatchImageDecorator.LIST_ITEM_UNFOCUS);
                item.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT,
                    ((OneBoxSearchUiDecorator) uiDecorator).ONEBOX_SUGGESTION_LIST_ITEM_HEIGHT);
                item.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, uiDecorator.SCREEN_WIDTH);
                TnMenu menu = UiFactory.getInstance().createMenu();
                item.setMenu(menu, AbstractTnComponent.TYPE_CLICK);
                menu.add("", CMD_POI_SUGGEST_SELECT);
                item.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_LIST_SINGLE));
                item.setForegroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_ME_GR), UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_ME_GR));
            }
            return item;
        }

        public int getCount()
        {
            int size = 0;
            
            Vector suggestions = model.getVector(KEY_V_RESULT_SUGGESTIONS);
            if(suggestions != null)
                size = suggestions.size();
            
            return size;
        }

        public int getItemType(int position)
        {
            return 0;
        }

    }
    class MultiStopListAdapter implements FrogAdapter
    {
        public AbstractTnComponent getComponent(int position,
                AbstractTnComponent convertComponent, AbstractTnComponent parent)
        {
            FrogListItem item = null;
            PoiDataWrapper poiDataWrapper = (PoiDataWrapper)model.get(KEY_O_POI_DATA_WRAPPER);
            Address addr = poiDataWrapper.getAddress(position);
            String text = getAddressName(addr);    
            if (addr == null)
            {
                return null;
            }

            if (convertComponent instanceof FrogListItem)
            {
                item = (FrogListItem) convertComponent;
                item.setText(text);
                item.setId(position);
            }
            else
            {
                item = UiFactory.getInstance().createListItem(position);
                item.setText(text);
                item.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.LIST_ITEM_FOCUSED);
                item.getTnUiArgs()
                        .put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, NinePatchImageDecorator.LIST_ITEM_UNFOCUS);
                item.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT,
                    ((OneBoxSearchUiDecorator) uiDecorator).ONEBOX_SUGGESTION_LIST_ITEM_HEIGHT);
                item.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, uiDecorator.SCREEN_WIDTH);
                item.setFont(UiStyleManager.getInstance().getFont(
                    UiStyleManager.FONT_LIST_SINGLE));
                item.setForegroundColor(
                    UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_ME_GR),
                    UiStyleManager.getInstance()
                            .getColor(UiStyleManager.TEXT_COLOR_ME_GR));
                TnMenu menu = UiFactory.getInstance().createMenu();
                item.setMenu(menu, AbstractTnComponent.TYPE_CLICK);
                menu.add("", CMD_STOP_SELECT);
            }
            return item;
        }

        public int getCount()
        {
            PoiDataWrapper poiDataWrapper = (PoiDataWrapper)model.get(KEY_O_POI_DATA_WRAPPER);
            int size = poiDataWrapper.getAddressSize();
            return size;
        }

        public int getItemType(int position)
        {
            return 0;
        }

    }

    
    protected void initSearchList()
    {
        addressList = UiFactory.getInstance().createList(0);
        addressList.setTouchEventListener(this);
        addressList.setAdapter(new AddressAdapter(EMPTY_VECTOR));
        addressFilter = new AddressFilter(addressList);
        addressFilter.setMinInterval(200);
        
        cityList = UiFactory.getInstance().createList(0);
        cityList.setTouchEventListener(this);
        cityList.setAdapter(new CityAdapter(model.getVector(KEY_V_NEAR_CITIES)));
        cityFilter = new CityFilter(cityList);
        cityFilter.setMinInterval(200);
    }
    
    protected void initInputArea(AbstractTnContainer container)
    {
        String streetHint = ResourceManager.getInstance().getCurrentBundle()
                .getString(IStringSetHome.RES_TEXTFIELD_STREET, IStringSetHome.FAMILY_SET_HOME);

        String cityHint = ResourceManager.getInstance().getCurrentBundle()
                .getString(IStringSetHome.RES_TEXTFIELD_CITY, IStringSetHome.FAMILY_SET_HOME);

        String streetLine = model.getString(KEY_S_STREET_INIT_TEXT);
        String cityLine = model.getString(KEY_S_CITY_INIT_TEXT);

        if (streetLine == null)
        {
            streetLine = "";
        }

        if (cityLine == null)
        {
            cityLine = "";
        }

        CitizenTextField streetField = UiFactory.getInstance().createCitizenTextField(streetLine, 50, TnTextField.ANY,
            streetHint, ID_ADDRESS_INPUT_FIELD, ImageDecorator.IMG_AC_BACKSPACE.getImage());

        CitizenTextField cityField = UiFactory.getInstance().createCitizenTextField(cityLine, 50, TnTextField.ANY, cityHint,
            ID_CITY_INPUT_FIELD, ImageDecorator.IMG_AC_BACKSPACE.getImage());

        TnLinearContainer adderssItem = UiFactory.getInstance().createLinearContainer(0, true,
            AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);
        adderssItem.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, NinePatchImageDecorator.LIST_ITEM_UNFOCUS);
        adderssItem.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.LIST_ITEM_UNFOCUS);
        adderssItem.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, uiDecorator.SCREEN_WIDTH);
        adderssItem.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((OneBoxSearchUiDecorator) this.uiDecorator).ADDRESS_ITEM_HEIGHT);

        streetField.setPadding(5, 5, 5, 5);
        streetField.setTextPadding(10, 0, 10, 0);
        streetField.setCommandEventListener(this);
        streetField.setTextChangeListener(this);
        streetField.setBackspaceCommand(CMD_BACKSPACE);
        streetField.setFocusChangeListener(this);
        streetField.getTnUiArgs().put(CitizenTextField.KEY_PREFER_TEXTFIELD_HEIGHT,
            ((OneBoxSearchUiDecorator) this.uiDecorator).TEXTFIELD_HOME_HEIGHT);
        streetField.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((OneBoxSearchUiDecorator) this.uiDecorator).ADDRESS_FIELD_WIDTH);
        streetField.requestTextFieldFocus();
        streetField.setIsNeedShowInputMethod(true);
        adderssItem.add(streetField);

        TnLinearContainer cityItem = UiFactory.getInstance().createLinearContainer(0, true,
            AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);
        cityItem.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, NinePatchImageDecorator.LIST_ITEM_UNFOCUS);
        cityItem.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.LIST_ITEM_UNFOCUS);
        cityItem.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, uiDecorator.SCREEN_WIDTH);
        cityItem.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((OneBoxSearchUiDecorator) this.uiDecorator).ADDRESS_ITEM_HEIGHT);

        cityField.setPadding(5, 5, 5, 5);
        cityField.setTextPadding(10, 0, 10, 0);
        cityField.setCommandEventListener(this);
        cityField.setTextChangeListener(this);
        cityField.setFocusChangeListener(this);
        cityField.setBackspaceCommand(CMD_BACKSPACE);
        cityField.getTnUiArgs().put(CitizenTextField.KEY_PREFER_TEXTFIELD_HEIGHT,
            ((OneBoxSearchUiDecorator) this.uiDecorator).TEXTFIELD_HOME_HEIGHT);
        cityField.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((OneBoxSearchUiDecorator) this.uiDecorator).ADDRESS_FIELD_WIDTH);
        cityItem.add(cityField);

        container.add(adderssItem);
        container.add(cityItem);
        container.setTouchEventListener(this);
    }

    protected void initTitleContiner(AbstractTnContainer titleContainer)
    {
        if (titleContainer == null)
        {
            return;
        }
        
        String title = model.getString(KEY_S_TITLE);
        
        if(title == null)
        {
            title = "";
        }
        
        titleContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((OneBoxSearchUiDecorator) this.uiDecorator).LABEL_TITLE_HEIGHT);
        titleContainer.setBackgroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_ME_GR));
        
        FrogLabel titleLabel = UiFactory.getInstance().createLabel(0, title);
        titleLabel.setForegroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_WH), UiStyleManager.getInstance()
            .getColor(UiStyleManager.TEXT_COLOR_WH));
        titleLabel.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, uiDecorator.SCREEN_WIDTH);
        titleLabel.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((OneBoxSearchUiDecorator) this.uiDecorator).LABEL_TITLE_HEIGHT);
        titleLabel.setStyle(AbstractTnGraphics.HCENTER);
        titleLabel.setBoldFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_SCREEN_TITLE));
        titleContainer.add(titleLabel);
    }
    
    class CityAdapter implements FrogAdapter
    {
        private Vector data;

        public CityAdapter(Vector data)
        {
            this.data = data;
        }

        public AbstractTnComponent getComponent(int position, AbstractTnComponent convertComponent, AbstractTnComponent parent)
        {
            Stop city = (Stop) data.elementAt(position);
            CitizenAddressListItem item = null;
            if (convertComponent == null)
            {
                item = UiFactory.getInstance().createCitizenAddressListItem(position);
                item.setGap(5, 0);
                item.setStyle(AbstractTnGraphics.VCENTER);
                TnMenu menu = UiFactory.getInstance().createMenu();
                menu.add("", CMD_SELECT_CITY);
                item.setMenu(menu, AbstractTnComponent.TYPE_CLICK);
                item.setTitleColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_ME_GR), UiStyleManager
                        .getInstance().getColor(UiStyleManager.TEXT_COLOR_ME_GR));
                item.setForegroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_ME_GR), UiStyleManager
                        .getInstance().getColor(UiStyleManager.TEXT_COLOR_ME_GR));
                item.setBoldFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_TEXT_FIELD));
                item.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_TEXT_FIELD));
                item.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT,
                    ((OneBoxSearchUiDecorator)uiDecorator).DROPDOWN_LIST_HEIGHT);
            }
            else
            {
                item = (CitizenAddressListItem) convertComponent;
                item.setId(position);
            }
            item.setAddress(ResourceManager.getInstance().getStringConverter().convertSecondLine(city));
            item.setPadding(30, 0, 30, 0);
            
            Address address = new Address(Address.SOURCE_PREDEFINED);
            address.setStop(city);
            item.setAddressData(address);

            return item;
        }

        public int getCount()
        {
            return data.size();
        }

        public int getItemType(int position)
        {
            return 0;
        }
    }
    
    class CityFilter extends FrogKeywordFilter
    {
        private FrogList list;
        
        public CityFilter(FrogList list)
        {
            this.list = list;
        }
        
        protected FilterResults performFilter(FilterRequest constraint)
        {
            FilterResults results = new FilterResults();
            Vector searchList = null;
            Vector nearCityList = model.getVector(KEY_V_NEAR_CITIES);
            if (constraint.keyword == null || constraint.keyword.length() == 0)
            {
                searchList = nearCityList;
            }
            else
            {
                searchList = AddressListFilter.getFilteredList(constraint.keyword, nearCityList, false);
            }
            results.constraint = constraint.keyword;
            results.values = searchList;
            results.count = searchList.size();
            return results;
        }

        protected void publishResults(FilterResults results)
        {
            final Vector data = (Vector) results.values;
            ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).runOnUiThread(new Runnable()
            {
                public void run()
                {
                    if (list != null)
                    {
                        list.setAdapter(new CityAdapter(data));
                    }
                }
            });
        }
    }

    class AddressAdapter implements FrogAdapter
    {
        private Vector data;
        
        public AddressAdapter(Vector data)
        {
            this.data = data;
        }
        
        public AbstractTnComponent getComponent(int position, AbstractTnComponent convertComponent, AbstractTnComponent parent)
        {
            if(data == null)
            {
                return null;
            }
            
            Address address = (Address) data.elementAt(position);
            
            AbstractTnComponent item = getAddressComponent(address, position, convertComponent);
            
            return item;
        }
        
        public int getCount()
        {
            return data.size();
        }
        
        public int getItemType(int position)
        {
            return 0;
        }
        
        protected AbstractTnComponent getAddressComponent(Address address, int componentId, AbstractTnComponent convertComponent)
        {
            if(address == null)
            {
                return null;
            }
            
            String label = address.getDisplayedText();
            CitizenAddressListItem addressListItem = null;
            if (convertComponent == null)
            {
                addressListItem = UiFactory.getInstance().createCitizenAddressListItem(componentId);
            }
            else
            {
                addressListItem = (CitizenAddressListItem) convertComponent;
                addressListItem.setId(componentId);
            }

            initAddressListItem(addressListItem, isShareAddress(address), address);
            addressListItem.setTitle(label);
            addressListItem.setTitleBadge(null);
            addressListItem.setAddress(ResourceManager.getInstance().getStringConverter().convertAddress(address.getStop(), false));
            addressListItem.setAddressData(address);
            
            return addressListItem;
        }
        protected void initAddressListItem(CitizenAddressListItem addressListItem, boolean isSharedAddress, Address address)
        {
            int padding = (uiDecorator.SCREEN_WIDTH.getInt() - ((OneBoxSearchUiDecorator) uiDecorator).ADDRESS_FIELD_WIDTH.getInt()) / 2;
            addressListItem.setPadding(padding, 7, padding, 7);
            addressListItem.setGap(15, 0);
            addressListItem.setTitleColor(UiStyleManager.getInstance().getColor(UiStyleManager.LIST_ITEM_TITLE_UNFOCUS_COLOR), UiStyleManager
                .getInstance().getColor(UiStyleManager.LIST_ITEM_TITLE_UNFOCUS_COLOR));
           
            
            addressListItem.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT,
                new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
                {
                    public Object decorate(TnUiArgAdapter args)
                    {
                        return PrimitiveTypeCache.valueOf(AppConfigHelper.getMaxDisplaySize() * 85 / 1000);
                    }
                }));

            if (isSharedAddress)
            {
                addressListItem.getTnUiArgs().put(
                    CitizenAddressListItem.KEY_LEFT_ICON_UNFOCUS,
                    ImageDecorator.IMG_LIST_RECEIVED_ICON_UNFOCUSED);
                addressListItem.getTnUiArgs().put(CitizenAddressListItem.KEY_LEFT_ICON_FOCUS,
                    ImageDecorator.IMG_LIST_RECEIVED_ICON_UNFOCUSED);
            }
            else
            {
                int type = address.getType();
                if(type == Address.TYPE_FAVORITE_POI || type == Address.TYPE_FAVORITE_STOP && address.getCatagories() != null 
                        && !address.getCatagories().contains(AddressDao.RECEIVED_ADDRESSES_CATEGORY))
                {
                    addressListItem.getTnUiArgs().put(CitizenAddressListItem.KEY_LEFT_ICON_UNFOCUS, ImageDecorator.IMG_FAV_ICON_LIST_UNFOCUS);
                    addressListItem.getTnUiArgs().put(CitizenAddressListItem.KEY_LEFT_ICON_FOCUS, ImageDecorator.IMG_FAV_ICON_LIST_UNFOCUS);
                }
                else
                {
                    addressListItem.getTnUiArgs().put(CitizenAddressListItem.KEY_LEFT_ICON_UNFOCUS, ImageDecorator.IMG_LIST_HISTORY_ICON_UNFOCUSED);
                    addressListItem.getTnUiArgs().put(CitizenAddressListItem.KEY_LEFT_ICON_FOCUS, ImageDecorator.IMG_LIST_HISTORY_ICON_UNFOCUSED);
                }
            }
            addressListItem.setForegroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_ME_GR), UiStyleManager
                    .getInstance().getColor(UiStyleManager.TEXT_COLOR_ME_GR));
            addressListItem.setBoldFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_LIST_DUAL_LINE_TOP));
            addressListItem.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_LIST_DUAL_LINE_BOTTOM));
            
            TnMenu menu = UiFactory.getInstance().createMenu();
            menu.add("", CMD_SELECT_ADDRESS);
            addressListItem.setMenu(menu, AbstractTnComponent.TYPE_CLICK);
            
            addressListItem.setCommandEventListener(OneBoxSearchViewTouch.this);
        }
        
        protected boolean isShareAddress(Address destAddress)
        {
            boolean isSharedAddress = destAddress != null
                    && destAddress.getCatagories() != null
                    && destAddress.getCatagories().contains(
                        AddressDao.RECEIVED_ADDRESSES_CATEGORY)
                    && !destAddress.isReadByUser();
            return isSharedAddress;
        }
    }

    class AddressFilter extends FrogKeywordFilter
    {
        private FrogList list;
        
        public AddressFilter(FrogList list)
        {
            this.list = list;
        }
        
        protected FilterResults performFilter(FilterRequest constraint)
        {
            FilterResults results = new FilterResults();
            Vector searchList = null;
            if (constraint.keyword == null || constraint.keyword.length() == 0)
            {
                searchList = EMPTY_VECTOR;
            }
            else
            {
                searchList = AddressListFilter.getFilteredList(constraint.keyword,model.getVector(KEY_V_SEARCH_LIST), false, false);
            }
            results.constraint = constraint.keyword;
            results.values = searchList;
            results.count = searchList.size();
           model.put(KEY_V_FILTER_SEARCH_LIST, searchList);
            return results;
        }

        protected void publishResults(FilterResults results)
        {
            final Vector data = (Vector) results.values;
            ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).runOnUiThread(new Runnable()
            {
                public void run()
                {
                    if (list != null)
                    {
                        list.setAdapter(new AddressAdapter(data));
                    }
                }
            });
        }
    }

    @Override
    public void focusChange(AbstractTnComponent component, boolean hasFocus)
    {
        if(hasFocus)
        {
            int id = component.getId() - CitizenTextField.KEY_ID_OPERATOR_NUMBER;
            switch (id)
            {
                case ID_ADDRESS_INPUT_FIELD:
                {
                    setAddressList();
                    
                    CitizenTextField textField = (CitizenTextField)component.getParent();
                    if(addressFilter != null)
                    {
                        addressFilter.filter(textField.getText());
                    }
                    break;
                }
                case ID_CITY_INPUT_FIELD:
                {
                    setCityList();
                    
                    CitizenTextField textField = (CitizenTextField)component.getParent();
                    if(cityFilter != null)
                    {
                        cityFilter.filter(textField.getText());
                    }
                    break;
                }
                default:
                    break;
            }
        }
        
    }

    @Override
    public void onTextChange(AbstractTnComponent component, String text)
    {
        if(component != null)
        {
            int id = component.getId() - CitizenTextField.KEY_ID_OPERATOR_NUMBER;
            switch (id)
            {
                case ID_ADDRESS_INPUT_FIELD:
                {
                    if(addressFilter != null)
                    {
                        setAddressList();
                        addressFilter.filter(text);
                        model.put(KEY_B_FROM_LIST, false);
                        break;
                    }
                }
                case ID_CITY_INPUT_FIELD:
                {
                    if(cityFilter != null)
                    {
                        setCityList();
                        cityFilter.filter(text);
                        model.put(KEY_B_FROM_LIST, false);
                        break;
                    }
                }
                default:
                    break;
            }
        }
        
    }
    
    protected void setCityList()
    {
        if(cityList == null)
        {
            return;
        }

        AbstractTnContainer container = (AbstractTnContainer)this.getComponentInScreen(ID_ONEBOX_SEARCH_CONTAINER);
        if(container != null)
        {
            if (container.getChildrenSize() > 0 && (container.get(container.getChildrenSize() - 1) instanceof FrogList))
            {
                FrogList list = (FrogList) (container.get(container.getChildrenSize() - 1));
                if (list.getId() != ID_CITY_INPUT_FIELD)
                {
                    container.remove(list);
                    container.add(cityList);
                }
            }
            else
            {
                container.add(cityList);
            }
        }
    }

    protected void setAddressList()
    {
        if(addressList == null)
        {
            return;
        }
        
        AbstractTnContainer container = (AbstractTnContainer)getComponentInScreen(ID_ONEBOX_SEARCH_CONTAINER);
        if(container != null)
        {
            if (container.getChildrenSize() > 0 && (container.get(container.getChildrenSize() - 1) instanceof FrogList))
            {
                FrogList list = (FrogList) (container.get(container.getChildrenSize() - 1));
                if (list.getId() != ID_ADDRESS_INPUT_FIELD)
                {
                    container.remove(list);
                    container.add(addressList);
                }
            }
            else
            {
                container.add(addressList);
            }
        }
    }
    
    protected AbstractTnComponent getComponentInScreen(int id)
    {
        CitizenScreen currentScreen =  (CitizenScreen) ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()) .getCurrentScreen();
        AbstractTnComponent component = null;
        if (currentScreen != null)
        {
            component = currentScreen.getComponentById(id);
        }
        return component;
    }
    
    public boolean handleUiEvent(TnUiEvent tnUiEvent)
    {
        logKtUiEvent(tnUiEvent);
        return super.handleUiEvent(tnUiEvent);
    }
    
    private void logKtUiEvent(TnUiEvent tnUiEvent)
    {
        if (tnUiEvent != null && tnUiEvent.getType() == TnUiEvent.TYPE_TOUCH_EVENT && (tnUiEvent.getMotionEvent() != null && tnUiEvent.getMotionEvent().getAction() == TnMotionEvent.ACTION_UP))
        {
            if(tnUiEvent.getComponent() instanceof CitizenAddressListItem)
            {
                CitizenAddressListItem item = (CitizenAddressListItem)tnUiEvent.getComponent();
                if("Current Location".equals(item.getTitle()))
                {
                    KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_DWF,
                        KontagentLogger.DWF_CURRENT_LOCATION_CLICKED);
                }
                
            }
        }
    }
}

class ListItemData
{
    public final static int FAV_TYPE = 0;

    public final static int RECENT_STOP_TYPE = 1;

    public final static int ONEBOX_LOCAL_TYPE = 2;
    
    public final static int ONEBOX_NETWORK_TYPE = 3;

    public final static int CATEGORY_TYPE = 4;
    
    public final static int HOME_TYPE = 5;
    
    public final static int WORK_TYPE = 6;

    public final static int AIRPORT_TYPE = 7;
    
    public final static int CONTACT_TYPE = 8;
	
	public final static int CURRENT_LOCATION = 9;
    
    public final static int SET_HOME_TYPE = 10;
    
    public final static int SET_WORK_TYPE = 11;
    
    public final static int RECENT_SEARCH_STR_TYPE = 12;
    
    public Address address;

    public int dataType;

    public int categoryId;

    public String name;

    public String contentString;
    
    public String ptnString;

    public ListItemData(int dataType, String name, String contentString, Address addr, int categoryId, String ptn)
    {
        this.dataType = dataType;
        this.name = name;
        this.address = addr;
        this.categoryId = categoryId;
        this.contentString = contentString;
        this.ptnString = ptn;
    }
}
