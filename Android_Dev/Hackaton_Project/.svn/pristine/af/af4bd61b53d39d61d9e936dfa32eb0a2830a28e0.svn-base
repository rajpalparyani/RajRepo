/**
 *
 * Copyright 2013 TeleNav, Inc. All rights reserved.
 * AddPlaceViewTouch.java
 *
 */
package com.telenav.module.ac.place.addplace;

import java.util.Date;
import java.util.Vector;
import java.util.regex.Pattern;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.telenav.app.TeleNavDelegate;
import com.telenav.app.android.AndroidPersistentContext;
import com.telenav.app.android.scout_us.R;
import com.telenav.data.datatypes.address.Address;
import com.telenav.data.datatypes.address.FavoriteCatalog;
import com.telenav.data.datatypes.address.Stop;
import com.telenav.data.datatypes.poi.BizPoi;
import com.telenav.data.datatypes.poi.Poi;
import com.telenav.i18n.ResourceBundle;
import com.telenav.module.poi.PoiDataWrapper;
import com.telenav.module.poi.PoiDataWrapper.PoiDataPublisher;
import com.telenav.mvc.AbstractCommonUiDecorator;
import com.telenav.mvc.AbstractCommonView;
import com.telenav.mvc.ICommonConstants;
import com.telenav.res.IStringCommon;
import com.telenav.res.IStringDashboard;
import com.telenav.res.ResourceManager;
import com.telenav.sdk.kontagent.KontagentLogger;
import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.core.AbstractTnUiHelper;
import com.telenav.tnui.core.ITnScreenAttachedListener;
import com.telenav.tnui.core.TnMenu;
import com.telenav.tnui.core.TnScreen;
import com.telenav.tnui.core.TnUiArgs;
import com.telenav.tnui.core.TnUiEvent;
import com.telenav.tnui.core.android.AndroidActivity;
import com.telenav.tnui.core.android.AndroidUiHelper;
import com.telenav.tnui.widget.TnPopupContainer;
import com.telenav.tnui.widget.TnTextField;
import com.telenav.ui.ImageDecorator;
import com.telenav.ui.NinePatchImageDecorator;
import com.telenav.ui.UiFactory;
import com.telenav.ui.UiStyleManager;
import com.telenav.ui.android.AssetsImageDrawable;
import com.telenav.ui.android.PlaceListItem;
import com.telenav.ui.android.TnListView;
import com.telenav.ui.android.TnListView.ITnListViewListener;
import com.telenav.ui.citizen.CitizenScreen;
import com.telenav.ui.citizen.CitizenTextField;
import com.telenav.ui.citizen.android.AndroidCitizenUiHelper;
import com.telenav.ui.frogui.widget.FrogMessageBox;
import com.telenav.util.PrimitiveTypeCache;

/**
 * @author Casper(pwang@telenav.cn)
 * @date 2013-2-21
 */
public class AddPlaceViewTouch extends AbstractCommonView implements IAddPlaceConstants, ITnListViewListener
{

    private View addPlaceMainView;

    private View addPlaceCustomView;

    private PlaceListItem currentLocationItem;
    
    private TnListView placeList = null;
    
    private ListView categoryList = null;
    
    private PlaceListAdapter placeListAdapter = null;
    
    private CustomPlaceCategoryAdapter categoryListAdapter = null;
    
    CitizenTextField addressEditView;
    
    public AddPlaceViewTouch(AbstractCommonUiDecorator uiDecorator)
    {
        super(uiDecorator);
    }

    protected int transformCommandDelegate(int state, TnUiEvent tnUiEvent)
    {
        int cmd = CMD_NONE;
        return cmd;
    }
    
    protected void activate()
    {
        // FIXME: adapter the font size of native XML configuration
        ((AndroidUiHelper) AbstractTnUiHelper.getInstance()).setFontDensity(false);
        super.activate();
    }

    protected void deactivateDelegate()
    {
        ((AndroidUiHelper) AbstractTnUiHelper.getInstance()).setFontDensity(true);
        super.deactivateDelegate();
    }
    
    public void onSizeChanged(AbstractTnComponent tnComponent, int w, int h, int oldw, int oldh)
    {
        ((AndroidUiHelper) AbstractTnUiHelper.getInstance()).setFontDensity(false);
    }

    protected TnPopupContainer createPopup(int state)
    {
        switch (state)
        {
            case STATE_NO_GPS_WARNING:
            {
                return createNoGPSWarning(state);
            }
        }
        return null;
    }

    private TnPopupContainer createNoGPSWarning(int state)
    {
        ResourceBundle bundle = ResourceManager.getInstance().getCurrentBundle();
        String noGps = bundle.getString(IStringCommon.RES_GPS_ERROR, IStringCommon.FAMILY_COMMON);

        TnMenu menu = UiFactory.getInstance().createMenu();

        String cancel = bundle.getString(IStringCommon.RES_BTTN_OK, IStringCommon.FAMILY_COMMON);
        menu.add(cancel, CMD_COMMON_BACK);

        FrogMessageBox messageBox = UiFactory.getInstance().createMessageBox(state, noGps, menu);
        return messageBox;
    }


    protected boolean updatePopup(int state, TnPopupContainer popup)
    {
        return false;
    }

    protected TnScreen createScreen(int state)
    {
        TnScreen screen = null;

        switch (state)
        {
            case STATE_MAIN:
            {
                screen = createAddPlaceMainScreen(state);
                break;
            }
            case STATE_CUSTOM_PLACE:
            {
                screen = createCustomAddressScreen(state);
            }
        }
        return screen;
    }

    private int getScreenMainResource(int state, int mode)
    {
        int title = -1;
        if (state == STATE_MAIN)
        {
            if (mode == PLACE_OPERATION_TYPE_ADD)
            {
                title = R.string.addplace_title_add;
            }
            else if (mode == PLACE_OPERATION_TYPE_SHARE)
            {
                title = R.string.addplace_title_share;
            }
        }
        else if (state == STATE_CUSTOM_PLACE)
        {
            if (mode == PLACE_OPERATION_TYPE_ADD)
            {
                title = R.string.addplace_title_add;
            }
            else if (mode == PLACE_OPERATION_TYPE_EDIT || mode == PLACE_OPERATION_TYPE_ADD_TO_FOLDER)
            {
                title = R.string.addplace_title_edit;
            }
        }
        return title;
    }

    private TnScreen createCustomAddressScreen(int state)
    {
        int type = this.model.getInt(KEY_I_PLACE_OPERATION_TYPE);
        Address address = (Address) this.model.get(KEY_O_SELECTED_ADDRESS);
        
        final Context context = AndroidPersistentContext.getInstance().getContext();
        CitizenScreen screen = UiFactory.getInstance().createScreen(state);
        addPlaceCustomView = AndroidCitizenUiHelper.addContentView(screen, R.layout.add_place_custom);
        if(address == null)
        {
            return screen;
        }


        TextView title = (TextView) addPlaceCustomView.findViewById(R.id.commonTitle0TextView);
        title.setText(getScreenMainResource(state, type));
        Button doneButton = (Button) addPlaceCustomView.findViewById(R.id.commonTitle0TextButton);
        doneButton.setText(R.string.addplace_custom_done);
        doneButton.setVisibility(View.VISIBLE);
        AndroidCitizenUiHelper.setOnClickCommand(this, doneButton, CMD_CUSTOM_PLACE_DONE);

        ImageView icon = (ImageView) addPlaceCustomView.findViewById(R.id.addplaceCustomIcon);
        icon.setBackgroundResource(R.drawable.list_icon_see_all_unfocused);
        Poi poi = address.getPoi();
        if (poi != null && poi.getBizPoi() != null)
        {
            BizPoi bizPoi = poi.getBizPoi();
            if(bizPoi.getCategoryLogo() != null && bizPoi.getCategoryLogo().length() > 0)
            {
                int iconId = context.getResources().getIdentifier(bizPoi.getCategoryLogo(), "drawable",
                    AndroidPersistentContext.getInstance().getContext().getPackageName());
                if (iconId != 0)
                {
                    icon.setBackgroundResource(iconId);
                }
            }
        }
        
        this.model.put(KEY_S_PLACE_LABEL,address.getLabel());
        ViewGroup editAddressViewGroup = (ViewGroup) addPlaceCustomView.findViewById(R.id.addplaceCustomFilterContainer);
        addAddressEditView(editAddressViewGroup);
        if(type == ICommonConstants.PLACE_OPERATION_TYPE_EDIT || type == ICommonConstants.PLACE_OPERATION_TYPE_ADD)
        {
            addressEditView.requestFocus();
        }

        TextView firstLineView = (TextView) addPlaceCustomView.findViewById(R.id.addplaceCustomFirstLine);
        TextView secondLineView = (TextView) addPlaceCustomView.findViewById(R.id.addplaceCustomSecondLine);
        String displayText = address.getDisplayedText();
        
        if(displayText == null || displayText.length() == 0 || containsLatLon(displayText))//Unkown address just with lat/lon
        {
            ResourceBundle bundle = ResourceManager.getInstance().getCurrentBundle();
            addressEditView.setText(bundle.getString(IStringDashboard.RES_STRING_UNKNOWN, IStringDashboard.FAMILY_DASHBOARD));
            firstLineView.setText(context.getString(R.string.addplace_location_info, address.getStop().getLat() / 100000.0d, address.getStop().getLon() / 100000.0d));
        }
        else
        {
            addressEditView.setText(address.getLabel());
            firstLineView.setText(address.getStop().getFirstLine());
        }
        secondLineView.setText(ResourceManager.getInstance().getStringConverter().convertSecondLine(address.getStop()));

        categoryListAdapter = new CustomPlaceCategoryAdapter(addPlaceCustomView.getContext());
        categoryList = (ListView) addPlaceCustomView.findViewById(R.id.addplaceCustomCategoryList);
        categoryList.setOnScrollListener(new OnScrollListener()
        {           
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState)
            {
               
            }
            
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
            {
                resetKeyboard();                
            }
        });
        categoryList.setItemsCanFocus(false);
        categoryList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        categoryList.addFooterView(CreateNewCategoryItem());
        categoryList.setAdapter(categoryListAdapter);
        categoryList.setDivider(null);
        int selectedIndex = this.model.getInt(KEY_I_SELECTED_CATEGORY_INDEX);
        categoryList.setItemChecked(selectedIndex + categoryList.getHeaderViewsCount() + 1, true);
        return screen;
    }

    private void resetKeyboard()
    {
        Context context = AndroidPersistentContext.getInstance().getContext();
        InputMethodManager imm = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);
        if (imm.isActive())
        {
            TeleNavDelegate.getInstance().callAppNativeFeature(TeleNavDelegate.FEATURE_UPDATE_WINDOW_SOFT_INPUT_MODE, null);
        }
    }
    
    private boolean containsLatLon(String text)
    {
        boolean containLatlon = false;
        if (text != null && text.length() > 0)
        {
            int indexComma = text.indexOf(",");
            if (indexComma > 0)
            {
                boolean haslon = Pattern.matches(".*\\d.*", text.substring(indexComma));
                if (haslon)
                {
                    containLatlon = true;
                }
            }
        }
        return containLatlon;
    }
    
    protected void addAddressEditView(ViewGroup filterContainer)
   {
       addressEditView = UiFactory.getInstance().createCitizenTextField("", 100, TnTextField.ANY,"",
           0, null, ImageDecorator.IMG_AC_BACKSPACE.getImage());
       addressEditView.setBackgroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_WH));
       addressEditView.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((AddPlaceUiDecorator) uiDecorator).TEXTFIELD_WIDTH);
       addressEditView.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((AddPlaceUiDecorator) uiDecorator).TEXTFIELD_HEIGHT);
       addressEditView.getTnUiArgs().put(CitizenTextField.KEY_PREFER_TEXTFIELD_HEIGHT, ((AddPlaceUiDecorator) uiDecorator).TEXTFIELD_HEIGHT);
       addressEditView.setHintTextColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_FILTER));
       addressEditView.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_TEXT_FIELD));
       addressEditView.setKeyEventListener(this);
       filterContainer.addView((View) addressEditView.getNativeUiComponent());
   }
    
    @SuppressWarnings("deprecation")
    protected TnScreen createAddPlaceMainScreen(int state)
    {
        int type = this.model.getInt(KEY_I_PLACE_OPERATION_TYPE);
        CitizenScreen screen = UiFactory.getInstance().createScreen(state);
        addPlaceMainView = AndroidCitizenUiHelper.addContentView(screen, R.layout.add_place_main);

        Drawable bgImage = new AssetsImageDrawable(NinePatchImageDecorator.FTUE_BG_UNFOCUSED);
        addPlaceMainView.setBackgroundDrawable(bgImage);

        TextView title = (TextView) addPlaceMainView.findViewById(R.id.commonTitle0TextView);
        title.setText(getScreenMainResource(state, type));

        TextView promptLabel = (TextView) addPlaceMainView.findViewById(R.id.addplace_prompt_label);
        promptLabel.setText(getPromptLableResource(type));

        final PoiDataWrapper poiDataWrapper = (PoiDataWrapper) model.get(KEY_O_POI_DATA_WRAPPER);
        placeListAdapter = new PlaceListAdapter(addPlaceMainView.getContext(), poiDataWrapper.getPublisher());
        placeList = (TnListView) addPlaceMainView.findViewById(R.id.addplaceListView);
        placeList.addHeaderView(createCurrentLocationItem());
        placeList.setPullRefreshEnable(false);
        placeList.setHitBottomLoadEnable(poiDataWrapper.getAnchorStop() != null);
        placeList.setAdapter(placeListAdapter);
        placeList.setTnListViewListener(this);

        placeList.setOnItemClickListener(new OnItemClickListener()
        {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Address address = null;
                Context context = AndroidPersistentContext.getInstance().getContext();
                if (view == currentLocationItem
                        && !currentLocationItem.getLastLine().equals(
                            context.getString(R.string.addplace_current_location_loading)))
                {
                    address = (Address) AddPlaceViewTouch.this.model.get(KEY_O_CURRENT_ADDRESS);
                    if(address == null)
                    {
                        address = new Address();
                        address.setStop(poiDataWrapper.getAnchorStop());
                    }
                    KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_CHOOSE_PLACE,
                      KontagentLogger.CHOOSEPLACE_CURRENT_LOCATION_CLICKED);
                }
                else if (position >= placeList.getHeaderViewsCount()
                        && position < placeList.getHeaderViewsCount() + poiDataWrapper.getAddressSize())
                {
                    poiDataWrapper.setSelectedIndex(position - placeList.getHeaderViewsCount());
                    address = poiDataWrapper.getSelectedAddress();
                    KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_CHOOSE_PLACE,
                        KontagentLogger.CHOOSEPLACE_OTHER_PLACE_CLICKED);
                }
                if(address != null)
                {
                    AddPlaceViewTouch.this.model.put(KEY_O_SELECTED_ADDRESS, address);
                    AndroidCitizenUiHelper.triggerCommand(AddPlaceViewTouch.this, view, CMD_SELECT_ITEM);
                }
            }
        });
        return screen;
    }

    private int getPromptLableResource(int mode)
    {
        int prompt = -1;
        if (mode == PLACE_OPERATION_TYPE_ADD)
        {
            prompt = R.string.addplace_prompt_add;
        }
        else if (mode == PLACE_OPERATION_TYPE_SHARE)
        {
            prompt = R.string.addplace_prompt_share;
        }
        return prompt;
    }

    private View CreateNewCategoryItem()
    {
        Context context = AndroidPersistentContext.getInstance().getContext();
        View newCategoryButton = LayoutInflater.from(context).inflate(R.layout.add_place_new_category, null);
        AndroidCitizenUiHelper.setOnClickCommand(this, newCategoryButton , CMD_ADD_CATEGORY);
        return newCategoryButton;
    }

    private View createCurrentLocationItem()
    {
        Context context = AndroidPersistentContext.getInstance().getContext();
        currentLocationItem = (PlaceListItem) LayoutInflater.from(context).inflate(R.layout.place_list0normalitem, null);
        currentLocationItem.init(PlaceListItem.TYPE_NORMAL);
        currentLocationItem.setIndicatorIcon(R.drawable.list_icon_current_unfocused);
        currentLocationItem.setAddressVisibility(View.GONE);
        currentLocationItem.setBrandName(context.getString(R.string.addplace_current_location_label));
        
        View background = currentLocationItem.findViewById(R.id.placeList0NormalItem);
        background.setBackgroundResource(R.drawable.add_place_list_item_background);

        updateCurrentLocationItem();
        
        customizeAddPlaceListItem(currentLocationItem);
        return currentLocationItem;
    }

    private void updateCurrentLocationItem()
    {
        Context context = AndroidPersistentContext.getInstance().getContext();
        PoiDataWrapper poiDataWrapper = (PoiDataWrapper) this.model.get(KEY_O_POI_DATA_WRAPPER);
        Stop anchor = null;
        Address currentAddress = (Address) this.model.get(KEY_O_CURRENT_ADDRESS);
        String lastline = null;
        if(currentAddress != null)
        {
            anchor = currentAddress.getStop(); 
            //If this anchor have no firstline this is an address with unknown label.
        }
        if(anchor == null)
        {
            anchor = poiDataWrapper.getAnchorStop();
        }
        if (anchor == null)
        {
            lastline = context.getString(R.string.addplace_current_location_loading);
        }
        else
        {
            lastline = anchor.getFirstLine();
            if(lastline == null || lastline.length() == 0)
            {
                lastline = context.getString(R.string.addplace_location_info, anchor.getLat() / 100000.0d, anchor.getLon() / 100000.0d);
            }
        }        
        
        if (!lastline.equals(currentLocationItem.getLastLine()))
        {
            currentLocationItem.setLastLine(lastline);
        }
    }

    private void customizeAddPlaceListItem(PlaceListItem item)
    {
        Context context = AndroidPersistentContext.getInstance().getContext();

        LinearLayout indicatorViewGroup = (LinearLayout) (item.findViewById(R.id.placeList0NormalItemNumberIndicatorViewGroup));
        indicatorViewGroup.setGravity(Gravity.CENTER_VERTICAL);
        ((LinearLayout.LayoutParams) indicatorViewGroup.getLayoutParams()).topMargin = 0;
    }

    protected boolean prepareModelData(int state, int commandId)
    {
        switch (state)
        {
            case STATE_CUSTOM_PLACE:
            {
                if (commandId == CMD_CUSTOM_PLACE_DONE)
                {
                    if (addressEditView != null)
                    {
                        String label = addressEditView.getText().trim().toString();
                        model.put(KEY_S_PLACE_LABEL, label);
                    }
                }
                if (commandId == CMD_CUSTOM_PLACE_DONE || commandId == CMD_ADD_CATEGORY)
                {
                    if(categoryList != null)
                    {
                        Vector categories = AddPlaceViewTouch.this.model.getVector(KEY_V_ALL_CATEGORIES);
                        int position = categoryList.getCheckedItemPosition();
                        if (position >= categoryList.getHeaderViewsCount()
                                && position < categoryList.getHeaderViewsCount() + categories.size() + 1)
                        {
                            int selectedCategoryIndex = position - categoryList.getHeaderViewsCount() - 1;
                            AddPlaceViewTouch.this.model.put(KEY_I_SELECTED_CATEGORY_INDEX, selectedCategoryIndex);
                        }
                    }
                }

                break;
            }
            default:
                break;
        }

        return true;
    }

    public boolean handleUiEvent(TnUiEvent tnUiEvent)
    {
        logKtUiEvent(tnUiEvent);
        switch (tnUiEvent.getType())
        {
            case TnUiEvent.TYPE_COMMAND_EVENT:
            {
                if (tnUiEvent.getCommandEvent() != null)
                {
                    int command = tnUiEvent.getCommandEvent().getCommand();
                    if (command == CMD_CUSTOM_PLACE_DONE)
                    {
                        TeleNavDelegate.getInstance().closeVirtualKeyBoard(getKeyBoardAttachedComponent());
                    }
                }
                break;
            }
        }
        return super.handleUiEvent(tnUiEvent);
    }

      private void logKtUiEvent(TnUiEvent tnUiEvent)
      {
          if (tnUiEvent != null && tnUiEvent.getType() == TnUiEvent.TYPE_COMMAND_EVENT && (tnUiEvent.getCommandEvent() != null))
          {
              int command = tnUiEvent.getCommandEvent().getCommand();
              switch (command)
              {
                  case CMD_GETTING_MORE:
                      final PoiDataWrapper poiDataWrapper = (PoiDataWrapper) model.get(KEY_O_POI_DATA_WRAPPER);
                      if(poiDataWrapper != null && poiDataWrapper.getAddressSize() > 0)
                      {
                          KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_CHOOSE_PLACE,
                              KontagentLogger.CHOOSEPLACE_SCROLLED_TO_BOTTOM);
                      }
                      break;
              }
          }
      }
    
    private AbstractTnComponent getKeyBoardAttachedComponent()
    {
        return ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getCurrentScreen().getRootContainer();
    }

    protected boolean updateScreen(int state, TnScreen screen)
    {
        switch (state)
        {
            case STATE_MAIN:
            {
                PoiDataWrapper poiDataManager = (PoiDataWrapper) model.get(KEY_O_POI_DATA_WRAPPER);
                updateCurrentLocationItem();
                if (!poiDataManager.isDoingRequest())
                {

                    if (placeList != null && placeListAdapter != null)
                    {
                        if( poiDataManager.getAddressSize() > 0)
                        {
                            placeListAdapter.setPoiDataWrapper(poiDataManager.getPublisher());
                            (placeListAdapter).notifyDataSetChanged();
                            stopLoad(placeList);
                        }
                        if (model.getBool(KEY_B_FORBID_LIST_HITBOTTOM))
                        {
                            placeList.setHitBottomLoadEnable(false);
                            placeList.setPullLoadEnable(true);
                        }
                        else
                        {
                            placeList.setHitBottomLoadEnable(poiDataManager.isHasMorePoi());
                        }
                    }
                }
                else
                {
                    if (placeList != null && placeListAdapter != null)
                    {
                        placeList.setHitBottomLoadEnable(true);
                    }
                }
                break;
            }
            case STATE_CUSTOM_PLACE:
            {
                ListView categoryListView = (ListView) ((View) ((CitizenScreen) screen).getContentContainer()
                        .getNativeUiComponent()).findViewById(R.id.addplaceCustomCategoryList);
                int selectedIndex = this.model.getInt(KEY_I_SELECTED_CATEGORY_INDEX);
                categoryListView.setItemChecked(selectedIndex + categoryListView.getHeaderViewsCount() + 1, true);
                if (categoryListAdapter != null)
                {
                    categoryListAdapter.notifyDataSetChanged();
                }
                return true;
            }
        }
        return false;
    }

    private void stopLoad(TnListView placeListView)
    {
        placeListView.stopRefresh();
        placeListView.stopLoadMore();
        placeListView.setRefreshTime(new Date());
    }

    @Override
    public void onRefresh()
    {
    }

    @Override
    public void onLoadMore()
    {
        if(placeList != null && this.model.getState() == STATE_MAIN)
        {
            AndroidCitizenUiHelper.triggerCommand(AddPlaceViewTouch.this, placeList, IAddPlaceConstants.CMD_GETTING_MORE);
        }
    }

    class PlaceListAdapter extends BaseAdapter
    {
        private LayoutInflater mInflater;
        private PoiDataPublisher poiDataPublisher;

        public PlaceListAdapter(Context context, PoiDataPublisher poiDataPublisher)
        {
            this.mInflater = LayoutInflater.from(context);
            this.poiDataPublisher = poiDataPublisher;
        }
        
        public void setPoiDataWrapper(PoiDataPublisher poiDataPublisher)
        {
            this.poiDataPublisher = poiDataPublisher;
        }

        public int getCount()
        {
            int size = poiDataPublisher != null ? poiDataPublisher.getAddressSize() : 0;
            return size;
        }

        public Object getItem(int position)
        {
            return null;
        }

        public long getItemId(int position)
        {
            return 0;
        }

        public View getView(int position, View convertView, ViewGroup parent)
        {
            Address addr = poiDataPublisher.getAddress(position);
            if (addr == null || addr.getType() != Address.TYPE_RECENT_POI || addr.getPoi() == null)
            {
                return convertView;
            }
            Poi poi = addr.getPoi();
            Stop stop = addr.getStop();

            if (convertView == null)
            {
                convertView = mInflater.inflate(R.layout.place_list0normalitem, null);
            }

            Context context = AndroidPersistentContext.getInstance().getContext();
            
            PlaceListItem item = (PlaceListItem) convertView;
            item.init(PlaceListItem.TYPE_NORMAL);
            item.setIndicatorIcon(R.drawable.list_icon_see_all_unfocused);
            if (poi != null && poi.getBizPoi() != null)
            {
                BizPoi bizPoi = poi.getBizPoi();
                if(bizPoi.getCategoryLogo() != null && bizPoi.getCategoryLogo().length() > 0)
                {
                    int iconId = context.getResources().getIdentifier(bizPoi.getCategoryLogo(), "drawable",  AndroidPersistentContext.getInstance().getContext().getPackageName());
                    if(iconId != 0)
                    {
                        item.setIndicatorIcon(iconId);
                    }
                }
            }

            customizeAddPlaceListItem(item);
                
            if (poi.getBizPoi() != null)
            {
                String title = poi.getBizPoi().getBrand();
                item.setAddressVisibility(View.GONE);
                item.setBrandName(title);
                String distStr = UiFactory.getInstance().getDisplayDistance(addr, poiDataPublisher.getAnchorStop(), true);
                item.setDistance(distStr);
            }

            String addrText = "";
            if (stop != null && addr.isValid())
            {
                addrText = stop.getFirstLine();
            }
            item.setLastLine(addrText);
            return item;
        }
    }

    class CustomPlaceCategoryAdapter extends BaseAdapter
    {
        private LayoutInflater mInflater;

        public CustomPlaceCategoryAdapter(Context context)
        {
            this.mInflater = LayoutInflater.from(context);
        }

        public int getCount()
        {
            Vector categories = model.getVector(KEY_V_ALL_CATEGORIES);
            int size = categories != null ? categories.size() : 0;
            size++; // For none category
            return size;
        }

        public Object getItem(int position)
        {
            return null;
        }

        public long getItemId(int position)
        {
            return 0;
        }

        public View getView(int position, View convertView, ViewGroup parent)
        {
            Vector categories = model.getVector(KEY_V_ALL_CATEGORIES);
            if (categories == null || position >= categories.size() + 1)
            {
                return convertView;
            }
            
            if (convertView == null)
            {
                convertView = mInflater.inflate(R.layout.add_place_category_item, null);
            }
            CheckedTextView categoryItem = (CheckedTextView) convertView;
            
            if (position == 0)
            {
                categoryItem.setText(R.string.addplace_category_label_none);
                categoryItem.setCompoundDrawablesWithIntrinsicBounds(R.drawable.list_icon_folder_disabled, 0, 0, 0);
            }
            else
            {
                FavoriteCatalog category = (FavoriteCatalog) categories.elementAt(position - 1);
                categoryItem.setCompoundDrawablesWithIntrinsicBounds(R.drawable.list_icon_folder_unfocused, 0, 0, 0);
                String categoryName;
                if (category.isReceivedCatalog())
                {
                    categoryName = ResourceManager.getInstance().getCurrentBundle()
                            .getString(IStringCommon.RES_RECEIVED_CATEGORY, IStringCommon.FAMILY_COMMON);
                }
                else
                {
                    categoryName = category.getName();
                }
                categoryItem.setText(categoryName);
            }
            return convertView;
        }
    }

    public void onScreenUiEngineAttached(final TnScreen screen, int attached)
    {
        int type = this.model.getInt(KEY_I_PLACE_OPERATION_TYPE);
        int state = this.model.getState();
        if ((type == ICommonConstants.PLACE_OPERATION_TYPE_EDIT || type == ICommonConstants.PLACE_OPERATION_TYPE_ADD)&& state==STATE_CUSTOM_PLACE)
        {
            if (screen != null && attached == ITnScreenAttachedListener.AFTER_ATTACHED)
            {
                TeleNavDelegate.getInstance().setOrientation(TeleNavDelegate.ORIENTATION_UNSPECIFIED);
                TeleNavDelegate.getInstance().callAppNativeFeature(TeleNavDelegate.FEATURE_UPDATE_WINDOW_SOFT_INPUT_MODE,
                    new Object[]
                    { PrimitiveTypeCache.valueOf(false), PrimitiveTypeCache.valueOf(true) });

                try
                {
                    if (addressEditView != null)
                    {
                        EditText editText = (EditText)addressEditView.getDropDownField().getNativeUiComponent();                        
                        AndroidActivity androidActivity = (AndroidActivity)AndroidPersistentContext.getInstance().getContext();
                        InputMethodManager imm = (InputMethodManager) androidActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
                    }
                }
                catch (Exception e)
                {

                }
            }
            else if (attached == DETTACHED)
            {
                TeleNavDelegate.getInstance().callAppNativeFeature(TeleNavDelegate.FEATURE_UPDATE_WINDOW_SOFT_INPUT_MODE, null);
            }
        }
    }
}
