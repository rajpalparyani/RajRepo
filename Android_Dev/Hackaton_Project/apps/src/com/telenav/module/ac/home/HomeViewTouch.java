/**
 *
 * Copyright 2013 TeleNav, Inc. All rights reserved.
 * HomeViewTouchListStyle.java
 *
 */
package com.telenav.module.ac.home;

import java.util.Vector;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.telenav.app.TeleNavDelegate;
import com.telenav.app.android.AndroidPersistentContext;
import com.telenav.data.dao.serverproxy.AddressDao;
import com.telenav.data.datatypes.address.Address;
import com.telenav.data.datatypes.address.Stop;
import com.telenav.module.AppConfigHelper;
import com.telenav.module.ac.AddressListFilter;
import com.telenav.mvc.AbstractCommonUiDecorator;
import com.telenav.mvc.AbstractCommonView;
import com.telenav.mvc.ICommonConstants;
import com.telenav.res.IStringSetHome;
import com.telenav.res.ResourceManager;
import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.core.AbstractTnContainer;
import com.telenav.tnui.core.AbstractTnUiHelper;
import com.telenav.tnui.core.ITnFocusChangeListener;
import com.telenav.tnui.core.ITnScreenAttachedListener;
import com.telenav.tnui.core.ITnTextChangeListener;
import com.telenav.tnui.core.TnKeyEvent;
import com.telenav.tnui.core.TnMenu;
import com.telenav.tnui.core.TnScreen;
import com.telenav.tnui.core.TnUiArgs;
import com.telenav.tnui.core.TnUiArgs.ITnUiArgsDecorator;
import com.telenav.tnui.core.TnUiArgs.TnUiArgAdapter;
import com.telenav.tnui.core.TnUiEvent;
import com.telenav.tnui.core.android.AndroidActivity;
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
import com.telenav.ui.frogui.widget.FrogAdapter;
import com.telenav.ui.frogui.widget.FrogKeywordFilter;
import com.telenav.ui.frogui.widget.FrogLabel;
import com.telenav.ui.frogui.widget.FrogList;
import com.telenav.util.PrimitiveTypeCache;


/**
 *@author bduan
 *@date 2013-1-6
 */
public class HomeViewTouch extends AbstractCommonView implements IHomeConstants, ITnTextChangeListener, ITnFocusChangeListener
{
    protected CityFilter cityFilter = null;
    protected AddressFilter addressFilter = null;
    
    protected FrogList cityList = null;
    protected FrogList addressList = null;
    
    protected Vector EMPTY_VECTOR = new Vector();
    
    public HomeViewTouch(AbstractCommonUiDecorator uiDecorator)
    {
        super(uiDecorator);
    }
    

    @Override
    protected boolean updateScreen(int state, TnScreen screen)
    {
        return false;
    }
    
    protected boolean prepareModelData(int state, int commandId)
    {
        switch (state)
        {
            case STATE_MAIN:
            {
                switch (commandId)
                {
                    case CMD_SELECT_CITY:
                    {
                        CitizenTextField addressTextField = (CitizenTextField) getComponentInScreen(ID_ADDRESS_INPUT_FIELD);
                        CitizenTextField cityTextField = (CitizenTextField) getComponentInScreen(ID_CITY_INPUT_FIELD);
                        
                        model.put(KEY_S_ADDRESS_LINE, addressTextField.getText());
                        model.put(KEY_S_CITY, cityTextField.getText());
                        break;
                    }
                }
                break;
            }
        }
        return super.prepareModelData(state, commandId);
    }

    protected boolean preProcessUIEvent(TnUiEvent tnUiEvent)
    {
        int type = tnUiEvent.getType();
        switch (type)
        {
            case TnUiEvent.TYPE_COMMAND_EVENT:
            {
                int command = tnUiEvent.getCommandEvent().getCommand();
                switch (command)
                {
                    case CMD_SELECT_ADDRESS:
                    {
                        AbstractTnComponent component = tnUiEvent.getComponent();
                        if(component != null)
                        {
                            CitizenAddressListItem item = (CitizenAddressListItem)component;
                            Address address = item.getAddressData();
                            model.put(KEY_O_VALIDATED_ADDRESS, address);
                            setupTextField(address);
                            
                            removeList();
                        }
                        break;
                    }                        
                    case CMD_SELECT_CITY:
                    {
                        AbstractTnComponent component = tnUiEvent.getComponent();
                        CitizenAddressListItem item = (CitizenAddressListItem)component;
                        
                        CitizenTextField addressTextField = (CitizenTextField)getComponentInScreen(ID_ADDRESS_INPUT_FIELD);
                        CitizenTextField cityTextField = (CitizenTextField)getComponentInScreen(ID_CITY_INPUT_FIELD);
                        
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
                    default:
                        break;
                }
                break;
            }
        }
        return super.preProcessUIEvent(tnUiEvent);
    }
    
    protected void removeList()
    {
        AbstractTnContainer container = (AbstractTnContainer)getComponentInScreen(ID_CONTENT_CONTAINER);
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

    @Override
    protected int transformCommandDelegate(int state, TnUiEvent tnUiEvent)
    {
        int cmd = CMD_NONE;
        switch (state)
        {
            case STATE_MAIN:
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
                            return CMD_SAVE;
                        }
                    }
                }
                break;
            }
        }
        return cmd;
    }

    protected void setCityList()
    {
        if(cityList == null)
        {
            return;
        }

        AbstractTnContainer container = (AbstractTnContainer)getComponentInScreen(ID_CONTENT_CONTAINER);
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
        
        AbstractTnContainer container = (AbstractTnContainer)getComponentInScreen(ID_CONTENT_CONTAINER);
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

    @Override
    protected TnPopupContainer createPopup(int state)
    {
        return null;
    }

    @Override
    protected TnScreen createScreen(int state)
    {
        switch (state)
        {
            case STATE_MAIN:
            {
                return createMainScreen(state);
            }
        }
        return null;
    }

    protected TnScreen createMainScreen(int state)
    {
        CitizenScreen mainScreen = UiFactory.getInstance().createScreen(state);
        mainScreen.getContentContainer().setBackgroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_WH));
        boolean needPreferenceMenu = this.model.fetchBool(KEY_B_NEED_PREFERENCE_MENU);
        if (!needPreferenceMenu)
        {
            removeMenuById(mainScreen, ICommonConstants.CMD_COMMON_PREFERENCE);
        }

        initTitleContiner(mainScreen.getTitleContainer());
        
        TnLinearContainer input_list_constainer = UiFactory.getInstance().createLinearContainer(ID_CONTENT_CONTAINER, true,
            AbstractTnGraphics.HCENTER);
        input_list_constainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, uiDecorator.SCREEN_HEIGHT);
        
        initInputArea(input_list_constainer);
        
        initSearchList();
        
        input_list_constainer.add(addressList);
        
        mainScreen.getContentContainer().add(input_list_constainer);
        
        return mainScreen;
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
        adderssItem.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((HomeUiDecorator) this.uiDecorator).ADDRESS_ITEM_HEIGHT);

        streetField.setPadding(5, 5, 5, 5);
        streetField.setTextPadding(10, 0, 10, 0);
        streetField.setCommandEventListener(this);
        streetField.setTextChangeListener(this);
        streetField.setBackspaceCommand(CMD_BACKSPACE);
        streetField.setFocusChangeListener(this);
        streetField.getTnUiArgs().put(CitizenTextField.KEY_PREFER_TEXTFIELD_HEIGHT,
            ((HomeUiDecorator) this.uiDecorator).TEXTFIELD_HOME_HEIGHT);
        streetField.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((HomeUiDecorator) this.uiDecorator).ADDRESS_FIELD_WIDTH);
        adderssItem.add(streetField);
        streetField.requestTextFieldFocus();
        streetField.setIsNeedShowInputMethod(true);

        TnLinearContainer cityItem = UiFactory.getInstance().createLinearContainer(0, true,
            AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);
        cityItem.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, NinePatchImageDecorator.LIST_ITEM_UNFOCUS);
        cityItem.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.LIST_ITEM_UNFOCUS);
        cityItem.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, uiDecorator.SCREEN_WIDTH);
        cityItem.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((HomeUiDecorator) this.uiDecorator).ADDRESS_ITEM_HEIGHT);

        cityField.setPadding(5, 5, 5, 5);
        cityField.setTextPadding(10, 0, 10, 0);
        cityField.setCommandEventListener(this);
        cityField.setTextChangeListener(this);
        cityField.setFocusChangeListener(this);
        cityField.setBackspaceCommand(CMD_BACKSPACE);
        cityField.getTnUiArgs().put(CitizenTextField.KEY_PREFER_TEXTFIELD_HEIGHT,
            ((HomeUiDecorator) this.uiDecorator).TEXTFIELD_HOME_HEIGHT);
        cityField.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((HomeUiDecorator) this.uiDecorator).ADDRESS_FIELD_WIDTH);
        cityItem.add(cityField);

        streetField.requestTextFieldFocus();
        
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
        
        titleContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((HomeUiDecorator) this.uiDecorator).LABEL_TITLE_HEIGHT);
        titleContainer.setBackgroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_ME_GR));
        
        FrogLabel titleLabel = UiFactory.getInstance().createLabel(0, title);
        titleLabel.setForegroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_WH), UiStyleManager.getInstance()
            .getColor(UiStyleManager.TEXT_COLOR_WH));
        titleLabel.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, uiDecorator.SCREEN_WIDTH);
        titleLabel.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((HomeUiDecorator) this.uiDecorator).LABEL_TITLE_HEIGHT);
        titleLabel.setStyle(AbstractTnGraphics.HCENTER);
        titleLabel.setBoldFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_SCREEN_TITLE));
        titleContainer.add(titleLabel);
    }
    
    @Override
    protected boolean updatePopup(int state, TnPopupContainer popup)
    {
        return false;
    }
    
    protected Stop createDisplayStop(Stop stop)
    {
        if (null == stop) return null;
        Stop displayStop = new Stop();
        displayStop.setCity(stop.getCity());
        displayStop.setProvince(stop.getProvince());
        return displayStop;
    }

    protected AbstractTnComponent getComponentInScreen(int id)
    {
        CitizenScreen currentScreen = (CitizenScreen) ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getCurrentScreen();
        AbstractTnComponent component = null;
        if (currentScreen != null)
        {
            component = currentScreen.getComponentById(id);
        }
        return component;
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
                    ((HomeUiDecorator) HomeViewTouch.this.uiDecorator).DROPDOWN_LIST_HEIGHT);
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
            Vector nearCityList = HomeViewTouch.this.model.getVector(KEY_V_NEAR_CITIES);
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
            int padding = (uiDecorator.SCREEN_WIDTH.getInt() - ((HomeUiDecorator) HomeViewTouch.this.uiDecorator).ADDRESS_FIELD_WIDTH.getInt()) / 2;
            addressListItem.setPadding(padding, 7, padding, 7);
            addressListItem.setGap(15, 0);
            addressListItem.setTitleColor(UiStyleManager.getInstance().getColor(UiStyleManager.LIST_ITEM_TITLE_UNFOCUS_COLOR), UiStyleManager
                .getInstance().getColor(UiStyleManager.LIST_ITEM_TITLE_UNFOCUS_COLOR));
            
            addressListItem.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
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
            
            addressListItem.setCommandEventListener(HomeViewTouch.this);
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
                searchList = AddressListFilter.getFilteredList(constraint.keyword,
                    HomeViewTouch.this.model.getVector(KEY_V_SEARCH_LIST), false, false);
            }
            results.constraint = constraint.keyword;
            results.values = searchList;
            results.count = searchList.size();
            HomeViewTouch.this.model.put(KEY_V_FILTER_SEARCH_LIST, searchList);
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
}
