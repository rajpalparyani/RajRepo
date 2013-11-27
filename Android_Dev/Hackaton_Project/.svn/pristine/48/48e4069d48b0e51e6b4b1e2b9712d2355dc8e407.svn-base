/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * FavoriteViewTouch.java
 *
 */
package com.telenav.module.ac.favorite;

import java.text.DateFormat;
import java.util.Date;
import java.util.Vector;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.AdapterView;
import android.widget.TextView;

import com.telenav.app.TeleNavDelegate;
import com.telenav.app.android.AndroidPersistentContext;
import com.telenav.app.android.scout_us.R;
import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.datatypes.address.Address;
import com.telenav.data.datatypes.address.FavoriteCatalog;
import com.telenav.data.datatypes.poi.BizPoi;
import com.telenav.data.datatypes.poi.Poi;
import com.telenav.data.serverproxy.NetworkStatusManager;
import com.telenav.logger.Logger;
import com.telenav.module.mapdownload.MapDownloadStatusManager;
import com.telenav.module.sync.MigrationExecutor;
import com.telenav.mvc.AbstractCommonUiDecorator;
import com.telenav.mvc.AbstractCommonView;
import com.telenav.res.IStringCommon;
import com.telenav.res.IStringFavorite;
import com.telenav.res.IStringLogin;
import com.telenav.res.ResourceManager;
import com.telenav.sdk.kontagent.KontagentLogger;
import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.core.AbstractTnUiHelper;
import com.telenav.tnui.core.INativeUiComponent;
import com.telenav.tnui.core.ITnTextChangeListener;
import com.telenav.tnui.core.TnKeyEvent;
import com.telenav.tnui.core.TnMenu;
import com.telenav.tnui.core.TnScreen;
import com.telenav.tnui.core.TnUiArgs;
import com.telenav.tnui.core.TnUiEvent;
import com.telenav.tnui.core.android.AndroidUiEventHandler;
import com.telenav.tnui.core.android.AndroidUiHelper;
import com.telenav.tnui.graphics.AbstractTnGraphics;
import com.telenav.tnui.widget.TnLinearContainer;
import com.telenav.tnui.widget.TnPopupContainer;
import com.telenav.tnui.widget.TnTextField;
import com.telenav.ui.ImageDecorator;
import com.telenav.ui.UiFactory;
import com.telenav.ui.UiStyleManager;
import com.telenav.ui.android.OnTnItemLongClickListener;
import com.telenav.ui.android.PlaceListItem;
import com.telenav.ui.android.TnListAdapter;
import com.telenav.ui.android.TnListView;
import com.telenav.ui.android.TnListView.ITnListViewListener;
import com.telenav.ui.android.TnListView.OnTnItemSelectedChangeListener;
import com.telenav.ui.citizen.CitizenScreen;
import com.telenav.ui.citizen.CitizenSlidableContainer;
import com.telenav.ui.citizen.CitizenTextField;
import com.telenav.ui.citizen.android.AndroidCitizenUiHelper;
import com.telenav.ui.frogui.widget.FrogLabel;
import com.telenav.ui.frogui.widget.FrogNullField;
import com.telenav.util.PrimitiveTypeCache;

/**
 * @author wzhu (wzhu@telenav.cn)
 * @date 2010-8-26
 */
class FavoriteViewTouch extends AbstractCommonView implements IFavoriteConstants, ITnTextChangeListener, ITnListViewListener, OnClickListener, OnTnItemSelectedChangeListener
{
    protected Vector result = new Vector();
    
    protected View myplaceRootView;
    
    protected MyPlaceListAdapter myPlaceListAdapter;
    
    FrogNullField loginContextMenu;
    
    Vector categories;
    Vector favorites;
    
    public FavoriteViewTouch(AbstractCommonUiDecorator uiDecorator)
    {
        super(uiDecorator);
    }

    protected int transformCommandDelegate(int state, TnUiEvent tnUiEvent)
    {
        int cmd = CMD_NONE;
        return cmd;
    }

    protected TnPopupContainer createPopup(int state)
    {
        switch(state)
        {
            case STATE_SORT_SELECT:
            {
                return createSortSelectPopup(state);
            }
        }
        return null;
    }
    
    protected TnPopupContainer createSortSelectPopup(int state)
    {
        TnMenu menu = UiFactory.getInstance().createMenu();
        String byName = ResourceManager.getInstance().getCurrentBundle()
                .getString(IStringFavorite.RES_SORT_BY_NAME, IStringFavorite.FAMILY_FAVORITE);
        String byDate = ResourceManager.getInstance().getCurrentBundle()
                .getString(IStringFavorite.RES_SORT_BY_DATE, IStringFavorite.FAMILY_FAVORITE);
        String byDistance = ResourceManager.getInstance().getCurrentBundle()
                .getString(IStringFavorite.RES_SORT_BY_DISTANCE, IStringFavorite.FAMILY_FAVORITE);
        menu.add(byName, CMD_SORT_BY_ALPHABET);
        menu.add(byDate, CMD_SORT_BY_DATE);
        menu.add(byDistance, CMD_SORT_BY_DISTANCE);
        String title = ResourceManager.getInstance().getCurrentBundle()
                .getString(IStringFavorite.RES_SORT_BY, IStringFavorite.FAMILY_FAVORITE);
        TnPopupContainer popup = UiFactory.getInstance().createMessageBox(state, title, menu, false);
        return popup;
    }

    protected boolean updatePopup(int state, TnPopupContainer popup)
    {
        return false;
    }

    protected TnScreen createScreen(int state)
    {
        switch (state)
        {
            case STATE_MAIN:
            {
                return createFavoriteMainScreen(state);
            }
            case STATE_SUBCATEGORY:
            {
                return createSubCategoryMainScreen(state);
            }
            case STATE_MAIN_EDIT_MODE:
            {
                return createEditFavoriteMainScreen(state);
            }
        }
        return null;
    }
    
    private TnScreen createEditFavoriteMainScreen(int state)
    {
        CitizenScreen screen = UiFactory.getInstance().createScreen(state);
        myplaceRootView = AndroidCitizenUiHelper.addContentView(screen, R.layout.myplace_list);
        updateTitle(state);
        ViewGroup inputFilterContainer = (ViewGroup) myplaceRootView.findViewById(R.id.myplaceList0searchFilterContainer);
        addAddressFilter(inputFilterContainer, false);

        TnListView myplaceListView = (TnListView) myplaceRootView.findViewById(R.id.myplaceList0ListView);
        if (myPlaceListAdapter == null)
        {
            myPlaceListAdapter = new MyPlaceListAdapter(myplaceListView.getContext());
        }
        myPlaceListAdapter.setNeedMiniBar(false);
        updateData();
        if (model.getBool(KEY_B_IS_SUBCATEGORY))
        {
            initResult(null, favorites);
            myPlaceListAdapter.setIsSubCategory(true);
        }
        else
        {
            initResult(categories, favorites);
            myPlaceListAdapter.setIsSubCategory(false);
        }
        myPlaceListAdapter.setEditMode(true);
        myplaceListView.setAdapter(myPlaceListAdapter);
        myplaceListView.setPullRefreshEnable(false);
        myplaceListView.setTnListViewListener(this);
        myplaceListView.setPullLoadEnable(false);
        myplaceListView.removeAllViewsInLayout();

        return screen;
    }

    private TnScreen createSubCategoryMainScreen(int state)
    {
        CitizenScreen screen = UiFactory.getInstance().createScreen(state);
        myplaceRootView = AndroidCitizenUiHelper.addContentView(screen, R.layout.myplace_list);
        updateTitle(state);
        ViewGroup inputFilterContainer = (ViewGroup) myplaceRootView.findViewById(R.id.myplaceList0searchFilterContainer);
        addAddressFilter(inputFilterContainer, true);
        
        favorites = (Vector) model.getVector(KEY_V_SUB_CATEGORY_FAVORITES).clone();
        initResult(null, favorites);
        
        TnListView myplaceListView = (TnListView) myplaceRootView.findViewById(R.id.myplaceList0ListView);
        
        if (myPlaceListAdapter == null)
        {
            myPlaceListAdapter = new MyPlaceListAdapter(myplaceListView.getContext());
        }
        myPlaceListAdapter.setNeedMiniBar(isNeedMiniBar());
        myPlaceListAdapter.setIsSubCategory(true);
        myPlaceListAdapter.setEditMode(false);
        
        myplaceListView.setAdapter(myPlaceListAdapter);
        myplaceListView.setPullRefreshEnable(true);
        myplaceListView.setTnListViewListener(this);
        myplaceListView.setPullLoadEnable(false);
        myplaceListView.removeAllViewsInLayout();
        myplaceListView.setOnTnItemLongClickListener(new OnTnItemLongClickListener()
        {
            protected boolean onTnItemLongClick(AdapterView<?> parent, View view, int position, long id)
            {
                model.put(KEY_I_INDEX, position);
                return view.showContextMenu();
            }
        });
        myplaceListView.setOnTnItemSelectedChangeListener(this);
        long lastSyncTime = DaoManager.getInstance().getAddressDao().getSyncTime(true);
        if (lastSyncTime > 0)
        {
            myplaceListView.setRefreshTime(new Date(lastSyncTime));
        }

        updateBottomButtonContainer((ViewGroup) screen.getRootContainer().getNativeUiComponent(), true);
        
        loginContextMenu = UiFactory.getInstance().createNullField(0);
        updateLoginContextMenu();
        loginContextMenu.setCommandEventListener(this);
        screen.getRootContainer().add(loginContextMenu);
        
        model.put(KEY_B_IS_LAST_TIME_DATA_AVAILABLE, isNeedMiniBar());
        return screen;
    }

    protected boolean updateScreen(int state, TnScreen screen)
    {
        boolean isBackToMyProfile = this.model.fetchBool(KEY_B_IS_START_BY_OTHER_CONTROLLER);
        this.myplaceRootView = (View) screen.getRootContainer().getNativeUiComponent();
        switch (state)
        {
            case STATE_MAIN:
            case STATE_SUBCATEGORY:
            {
                if (isBackToMyProfile && isCredentialInfoExisted())
                {
                    showNotificationContainer(STATE_MAIN);
                }
                
                if (myPlaceListAdapter != null)
                {
                    if (state == STATE_MAIN)
                    {
                        myPlaceListAdapter.setIsSubCategory(false);
                    }
                    else
                    {
                        myPlaceListAdapter.setIsSubCategory(true);
                    }
                    
                    myPlaceListAdapter.setNeedMiniBar(isNeedMiniBar());
                    myPlaceListAdapter.setEditMode(false);
                }
                
                ViewGroup screenRoot = (ViewGroup) screen.getRootContainer().getNativeUiComponent();
                final TnListView listView = (TnListView) screenRoot.findViewById(R.id.myplaceList0ListView);
                boolean needRefreshList = model.fetchBool(KEY_B_NEED_REFRESH_LIST);
                
                boolean lastTimeDataAvailable = model.getBool(KEY_B_IS_LAST_TIME_DATA_AVAILABLE);
                boolean needCloseContextMenu = (lastTimeDataAvailable != isNeedMiniBar());
                
                if(Logger.DEBUG)
                {
                    Logger.log(Logger.INFO, "FavoriteViewTouch", "=== Update List ===: needRefreshList " + needRefreshList);
                }
              // save index and top position
				View v = (View) listView.getTag(R.id.TAG_LAST_SELECTED_ITEM_VIEW);
				final int top = (v == null) ? 0 : v.getTop();
				final int index = this.model.getInt(KEY_I_INDEX);
              
                if (needRefreshList || (model.getPreState() == STATE_MAIN && state == STATE_SUBCATEGORY)
                        || (model.getPreState() == STATE_SUBCATEGORY && state == STATE_MAIN))
                {
                    if (model.getState() == STATE_MAIN)
                    {
                        updateFavoriteMainScreenList(listView);
                    }
                    else
                    {
                        updateSubCategoryScreenList(listView);
                    }
                    if ("".equals(model.getString(KEY_S_SEARCH_TEXT)))
                    {
                        ViewGroup filterContainer = (ViewGroup) screenRoot
                                .findViewById(R.id.myplaceList0searchFilterContainer);
                        if (filterContainer.getChildAt(0) instanceof INativeUiComponent)
                        {
                            ((CitizenTextField) (((INativeUiComponent) filterContainer.getChildAt(0)).getTnUiComponent()))
                                    .setText("");
                        }
                    }
                }
                long lastSyncTime = DaoManager.getInstance().getAddressDao().getSyncTime(true);
                if (lastSyncTime > 0)
                {
                    listView.setRefreshTime(new Date(lastSyncTime));
                }
                boolean keepMiniBar = this.model.fetchBool(KEY_B_KEEP_MINI_BAR);
                if (keepMiniBar)
                {
                    keepMiniBar = false;
                    if(index > -1 && index < result.size() && result.get(index) instanceof Address)
                    {
                        Address oldAddress = (Address) this.model.fetch(KEY_O_CURRENT_ADDRESS);
                        Address newAddress = (Address) result.get(index);
                        if (newAddress != null && oldAddress != null)
                        {
                            keepMiniBar = newAddress.isSameAddress(oldAddress);
                        }
                    }
                }
                if((model.fetchBool(KEY_B_REFRESH_FROM_NETWORK) || needRefreshList) && !keepMiniBar)
                {
                    listView.stopRefresh();
                }
                else
                {
                    listView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener()
                    {
                        @Override
                        public void onGlobalLayout()
                        {
                            listView.setSelectionFromTop(index + listView.getHeaderViewsCount(), top);
                            listView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        }
                    });
                }
                if (needCloseContextMenu)
                {
                    model.put(KEY_B_IS_LAST_TIME_DATA_AVAILABLE, isNeedMiniBar());
                    ((AndroidUiHelper)AndroidUiHelper.getInstance()).closeContextMenu();
                }
                loginContextMenu = UiFactory.getInstance().createNullField(0);
                updateLoginContextMenu();
                loginContextMenu.setCommandEventListener(this);
                screen.getRootContainer().add(loginContextMenu);
                updateBottomButtonContainer(screenRoot, false);
                return true;
            }
            case STATE_MAIN_EDIT_MODE:
            {
                ViewGroup screenRoot = (ViewGroup) screen.getRootContainer().getNativeUiComponent();
                TnListView listView =(TnListView) screenRoot.findViewById(R.id.myplaceList0ListView);
                if(myPlaceListAdapter!=null)
                {
                    myPlaceListAdapter.setNeedMiniBar(false);
                    myPlaceListAdapter.setEditMode(true);
                    if (myPlaceListAdapter.isSubCategoryShown())
                    {
                        updateSubCategoryScreenList(listView);
                    }
                    else
                    {
                        updateFavoriteMainScreenList(listView);
                    }
                }
                return true;
            }
        }
        return false;
    }
    
    protected boolean isNeedMiniBar()
    {
        boolean isNeedMiniBar = true;
        
        if(model.getInt(KEY_I_AC_TYPE) == TYPE_AC_FROM_ONE_BOX || (!NetworkStatusManager.getInstance().isConnected()
                && !MapDownloadStatusManager.getInstance().isOnBoardDataAvailable()))
        {
            isNeedMiniBar = false;
        }
        
        return isNeedMiniBar;
    }
    
    public void onSizeChanged(AbstractTnComponent tnComponent, int w, int h, int oldw, int oldh)
    {
        int screenState = model.getState();
        switch (screenState)
        {
            case STATE_MAIN:
            case STATE_SUBCATEGORY:
            {
                if (notificationContainer != null && notificationContainer.isShown())
                {
                    showNotificationContainer(screenState);
                }
                break;
            }
        }
        ((AndroidUiHelper) AbstractTnUiHelper.getInstance()).setFontDensity(false);
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

    private FrogLabel createEmptyLable(int emptyLabelId)
    {
        FrogLabel emptyLabel = UiFactory.getInstance().createLabel(emptyLabelId,
        		ResourceManager.getInstance().getCurrentBundle().getString(IStringFavorite.RES_EMPTY_LABEL_IN_FAVORITE, IStringFavorite.FAMILY_FAVORITE));
        emptyLabel.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, uiDecorator.SCREEN_WIDTH);
        emptyLabel.setStyle(AbstractTnGraphics.HCENTER);
        return emptyLabel;
    }

    private void updateFavoriteMainScreenList(TnListView itemList)
    {
        updateData();
        initResult(categories, favorites);
        updateAllList(itemList);
    }

    private void updateSubCategoryScreenList(TnListView itemList)
    {
        updateData();
        initResult(null, favorites);
        updateAllList(itemList);
    }
    
    private void updateTitle(int state)
    {
        String subTitle = "";
        switch(state)
        {
            case STATE_MAIN:
            {
                subTitle = ResourceManager.getInstance().getCurrentBundle()
                        .getString(IStringFavorite.RES_FAV_TITLE, IStringFavorite.FAMILY_FAVORITE);
                break;
            }
            case STATE_SUBCATEGORY:
            {
                if(model.get(KEY_O_CATEGORY) instanceof FavoriteCatalog)
                {
                    FavoriteCatalog categoryLog = (FavoriteCatalog) model.get(KEY_O_CATEGORY);
                    subTitle = categoryLog.getName();
                }
                break;
            }
            case STATE_MAIN_EDIT_MODE:
            case STATE_SUBCATEGORY_EDIT_MODE:
            {
                subTitle = ResourceManager.getInstance().getCurrentBundle()
                        .getString(IStringFavorite.RES_EDIT_FAVORITE, IStringFavorite.FAMILY_FAVORITE);
                TextView titleBtn = (TextView) myplaceRootView.findViewById(R.id.commonTitle0TextButton);
                titleBtn.setVisibility(View.VISIBLE);
                titleBtn.setText(ResourceManager.getInstance().getCurrentBundle()
                        .getString(IStringFavorite.RES_BTN_DONE, IStringFavorite.FAMILY_FAVORITE));
                AndroidCitizenUiHelper.setOnClickCommand(this, titleBtn, CMD_COMMON_BACK);
                break;
            }
            default:
            {
                subTitle = ResourceManager.getInstance().getCurrentBundle()
                        .getString(IStringFavorite.RES_FAV_TITLE, IStringFavorite.FAMILY_FAVORITE);
                break;
            }
        }
        String titleString = subTitle;
        TextView titleView = (TextView) myplaceRootView.findViewById(R.id.commonTitle0TextView);
        titleView.setText(titleString);
    }
    
    protected void addAddressFilter(ViewGroup filterContainer, boolean needClear)
    {
        CitizenTextField dropDownField = UiFactory.getInstance().createCitizenTextField("", 100, TnTextField.ANY,
            ResourceManager.getInstance().getCurrentBundle().getString(IStringFavorite.RES_FAV_HINT, IStringFavorite.FAMILY_FAVORITE),
            ID_FAVORITE_MAIN_SCREEN_TEXTFIELD, ImageDecorator.SEARCHBOX_ICON_HINT_UNFOCUSED.getImage(), ImageDecorator.IMG_AC_BACKSPACE.getImage());
        if(needClear)
        {
            dropDownField.setText("");
            model.put(KEY_S_SEARCH_TEXT, "");
        }
        else
        {
            String strFilter = model.getString(KEY_S_SEARCH_TEXT) == null ? "" : model.getString(KEY_S_SEARCH_TEXT) ;
            dropDownField.setText(strFilter);
        }
        dropDownField.setBackgroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_WH));
        dropDownField.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((FavoriteUiDecorator) uiDecorator).TEXTFIELD_WIDTH);
        dropDownField.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((FavoriteUiDecorator) uiDecorator).TEXTFIELD_HEIGHT);
        dropDownField.getTnUiArgs().put(CitizenTextField.KEY_PREFER_TEXTFIELD_HEIGHT, ((FavoriteUiDecorator) uiDecorator).TEXTFIELD_HEIGHT);
        dropDownField.setHintTextColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_FILTER));
        dropDownField.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_TEXT_FIELD));
        dropDownField.setTextChangeListener(this);
        dropDownField.setKeyEventListener(this);
        filterContainer.addView((View) dropDownField.getNativeUiComponent());
    }
    
    private TnScreen createFavoriteMainScreen(int state)
    {
        CitizenScreen screen = UiFactory.getInstance().createScreen(state);
        myplaceRootView = AndroidCitizenUiHelper.addContentView(screen, R.layout.myplace_list);
        updateTitle(state);
        ViewGroup inputFilterContainer = (ViewGroup) myplaceRootView.findViewById(R.id.myplaceList0searchFilterContainer);
        addAddressFilter(inputFilterContainer, false);
        
        categories = (Vector) model.getVector(KEY_V_ALL_CATEGORIES).clone();
        favorites = (Vector) model.getVector(KEY_V_ALL_FAVORITES).clone();
        initResult(categories, favorites);
        
        TnListView myplaceListView = (TnListView) myplaceRootView.findViewById(R.id.myplaceList0ListView);
        myPlaceListAdapter = new MyPlaceListAdapter(myplaceListView.getContext());
        myPlaceListAdapter.setNeedMiniBar(isNeedMiniBar());
        myPlaceListAdapter.setIsSubCategory(false);
        myPlaceListAdapter.setEditMode(false);
        myplaceListView.setAdapter(myPlaceListAdapter);
        myplaceListView.setPullRefreshEnable(true);
        myplaceListView.setTnListViewListener(this);
        myplaceListView.setPullLoadEnable(false);
        myplaceListView.removeAllViewsInLayout();
        myplaceListView.setOnTnItemLongClickListener(new OnTnItemLongClickListener()
        {

            protected boolean onTnItemLongClick(AdapterView<?> parent, View view, int position, long id)
            {
                model.put(KEY_I_INDEX, position);
                return view.showContextMenu();
            }
        });
        myplaceListView.setOnTnItemSelectedChangeListener(this);
        
        long lastSyncTime = DaoManager.getInstance().getAddressDao().getSyncTime(true);
        if (lastSyncTime > 0)
        {
            myplaceListView.setRefreshTime(new Date(lastSyncTime));
        }

        updateBottomButtonContainer((ViewGroup) screen.getRootContainer().getNativeUiComponent(), true);
        loginContextMenu = UiFactory.getInstance().createNullField(0);
        updateLoginContextMenu();
        loginContextMenu.setCommandEventListener(this);
        screen.getRootContainer().add(loginContextMenu);
        
        model.put(KEY_B_IS_LAST_TIME_DATA_AVAILABLE, isNeedMiniBar());
        return screen;
    }
    
    private void updateBottomButtonContainer(ViewGroup viewGroup, boolean needBindClickListener)
    {
        View bottomButtonViewGroup = viewGroup.findViewById(R.id.myplaceList0BottomButtonGroup);
        if (bottomButtonViewGroup.getVisibility() != View.VISIBLE)
        {
            bottomButtonViewGroup.setVisibility(View.VISIBLE);
        }

        if (needBindClickListener)
        {
            AndroidCitizenUiHelper.setOnClickCommand(this, bottomButtonViewGroup, BOTTOM_BUTTON_COMMAND_TABLE);
        }
        
        int categorySize = model.getBool(KEY_B_IS_SUBCATEGORY) || categories == null ? 0 : categories.size();
        int favoriteSize = favorites == null ? 0 : favorites.size();
        //i == 0 means Add button
        for (int i = 1; i < BOTTOM_BUTTON_COMMAND_TABLE.length; i++)
        {
            ViewGroup tempView = (ViewGroup) bottomButtonViewGroup.findViewById(BOTTOM_BUTTON_COMMAND_TABLE[i][0]);
            if (tempView != null)
            {
                TextView imageView = (TextView) tempView.getChildAt(0);
                TextView textView = (TextView) tempView.getChildAt(1);
                if(imageView == null || textView == null)
                {
                    return;
                }
                if (categorySize == 0 && favoriteSize == 0 && tempView.isEnabled())
                {
                    tempView.setEnabled(false);
                    imageView.setCompoundDrawablesWithIntrinsicBounds(BOTTOM_BUTTON_DISATBLED_IMAGE_RES_TABLE[i], 0, 0, 0);
                    textView.setTextColor(AndroidCitizenUiHelper.getResourceColor(R.color.bottom_control_disabled_text_color));
                }
                else if ((categorySize != 0 || favoriteSize != 0) && !tempView.isEnabled())
                {
                    tempView.setEnabled(true);
                    imageView.setCompoundDrawablesWithIntrinsicBounds(BOTTOM_BUTTON_ENABLED_IMAGE_RES_TABLE[i], 0, 0, 0);
                    textView.setTextColor(AndroidCitizenUiHelper.getResourceColor(R.color.bottom_control_text_color));
                }
            }
        }
    }

    private void updateAllList(TnListView list)
    {
        myPlaceListAdapter.notifyDataSetChanged();
        list.removeAllViewsInLayout();
        list.requestLayout();
    }
    
    private TnMenu createListItemPoiContextMenu(int componentId, Poi poi)
    {
        Context context = AndroidPersistentContext.getInstance().getContext();
        TnMenu contextMenu = UiFactory.getInstance().createMenu();
        
        if(isNeedMiniBar())
        {
            contextMenu.add(
                ResourceManager.getInstance().getCurrentBundle().getString(IStringCommon.RES_BTTN_DRIVETO, IStringCommon.FAMILY_COMMON), CMD_DRIVETO_ADDRESS);
            
            if (poi.getBizPoi() != null)
            {
                String phoneNum = poi.getBizPoi().getPhoneNumber();
                if (phoneNum != null && phoneNum.trim().length() > 0)
                {
                    contextMenu.add(ResourceManager.getInstance().getCurrentBundle().getString(IStringCommon.RES_MENU_CALL,
                        IStringCommon.FAMILY_COMMON)
                        + " " + phoneNum, CMD_COMMON_CALL);
                }
            }
            
            contextMenu.add(
                ResourceManager.getInstance().getCurrentBundle().getString(IStringCommon.RES_MENU_MAP, IStringCommon.FAMILY_COMMON),CMD_MAP_ADDRESS);
            
            contextMenu.add(ResourceManager.getInstance().getCurrentBundle().getString(IStringCommon.RES_SHARE, IStringCommon.FAMILY_COMMON),CMD_COMMON_SHARE);
        }
        
        contextMenu.add(ResourceManager.getInstance().getCurrentBundle()
            .getString(IStringCommon.RES_BTTN_EDIT, IStringCommon.FAMILY_COMMON), CMD_COMMON_EDIT);
        contextMenu.add(
            ResourceManager.getInstance().getCurrentBundle().getString(IStringCommon.RES_BTTN_DELETE, IStringCommon.FAMILY_COMMON), CMD_COMMON_DELETE);
        contextMenu.add(context.getString(R.string.myplaceCommandAddToFolder), CMD_ADD_TO_FOLDER);
        return contextMenu;
    }
    
    private TnMenu createListItemAddressContextMenu(int componentId)
    {
        Context context = AndroidPersistentContext.getInstance().getContext();
        TnMenu contextMenu = UiFactory.getInstance().createMenu();
        
        if(isNeedMiniBar())
        {
            contextMenu.add(
                ResourceManager.getInstance().getCurrentBundle()
                .getString(IStringCommon.RES_BTTN_DRIVETO, IStringCommon.FAMILY_COMMON), CMD_DRIVETO_ADDRESS);
            
            contextMenu
            .add(
                ResourceManager.getInstance().getCurrentBundle()
                .getString(IStringCommon.RES_MENU_MAP, IStringCommon.FAMILY_COMMON), CMD_MAP_ADDRESS);
            
            contextMenu.add(
                ResourceManager.getInstance().getCurrentBundle().getString(IStringCommon.RES_SHARE, IStringCommon.FAMILY_COMMON),
                CMD_COMMON_SHARE);
        }
        
        contextMenu.add(
            ResourceManager.getInstance().getCurrentBundle()
                    .getString(IStringCommon.RES_BTTN_EDIT, IStringCommon.FAMILY_COMMON), CMD_COMMON_EDIT);
        contextMenu.add(
            ResourceManager.getInstance().getCurrentBundle()
                    .getString(IStringCommon.RES_BTTN_DELETE, IStringCommon.FAMILY_COMMON), CMD_COMMON_DELETE);
        contextMenu.add(
            context.getString(R.string.myplaceCommandAddToFolder), CMD_ADD_TO_FOLDER);
        return contextMenu;
    }
    
    private TnMenu createCategoryContextMenu(int componentId)
    {
        TnMenu contextMenu = UiFactory.getInstance().createMenu();
        contextMenu.add(
            ResourceManager.getInstance().getCurrentBundle()
                    .getString(IStringCommon.RES_BTTN_EDIT, IStringCommon.FAMILY_COMMON), CMD_RENAME_CATEGORY);
        contextMenu.add(
            ResourceManager.getInstance().getCurrentBundle()
                    .getString(IStringCommon.RES_BTTN_DELETE, IStringCommon.FAMILY_COMMON), CMD_DELETE_CATEGORY);
        return contextMenu;
    }

    private void initResult(Vector categories, Vector favorites)
    {
        result.removeAllElements();
        if (categories != null)
        {
            for (int i = 0; i < categories.size(); i++)
            {
                result.addElement(categories.elementAt(i));
            }
        }
        if (favorites != null)
        {
            for (int i = 0; i < favorites.size(); i++)
            {
                result.addElement(favorites.elementAt(i));
            }
        }
    }
    
    private void updateData()
    {
        String text = model.getString(KEY_S_SEARCH_TEXT);
        if ("".equals(text))
        {
            if (this.myPlaceListAdapter == null || !this.myPlaceListAdapter.isSubCategory)
            {
                categories = (Vector) model.getVector(KEY_V_ALL_CATEGORIES).clone();
                favorites = (Vector) model.getVector(KEY_V_ALL_FAVORITES).clone();
            }
            else
            {
                favorites = (Vector) model.getVector(KEY_V_SUB_CATEGORY_FAVORITES).clone();
            }
        }
        else
        {
            if (this.myPlaceListAdapter == null || !this.myPlaceListAdapter.isSubCategory)
            {
                categories = (Vector) model.getVector(KEY_V_SEARCH_CATEGORY_RESULT).clone();
                favorites = (Vector) model.getVector(KEY_V_SEARCH_FAVORITE_RESULT).clone();
            }
            else
            {
                favorites = (Vector) model.getVector(KEY_V_SEARCH_FAVORITE_RESULT).clone();
            }
        }
    }

    protected boolean preProcessUIEvent(TnUiEvent tnUiEvent)
    {
        if (!this.model.isActivated())
            return super.preProcessUIEvent(tnUiEvent);
        switch (tnUiEvent.getType())
        {
            case TnUiEvent.TYPE_COMMAND_EVENT:
            {
                AbstractTnComponent component = tnUiEvent.getComponent();
                if (component instanceof CitizenSlidableContainer)
                {
                    if (tnUiEvent.getCommandEvent() != null
                            && tnUiEvent.getCommandEvent().getCommand() == CMD_HIDE_NOTIFICATION)
                    {
                        notificationContainer = null;
                        return true;
                    }
                }
                else if ((component.getCookie() instanceof TextView && ((TextView) component.getCookie()).getTag() != null)
                        || component instanceof TnLinearContainer)
                {
                    if (handleListCommonCommand(tnUiEvent))
                    {
                        return true;
                    }
                }

                break;
            }
        }
        return super.preProcessUIEvent(tnUiEvent);
    }
    
    protected boolean handleListCommonCommand(TnUiEvent tnUiEvent)
    {
        if (tnUiEvent.getCommandEvent() != null)
        {
            int commandId = tnUiEvent.getCommandEvent().getCommand();
            if (commandId != CMD_COMMON_EXIT)
            {
                if (result == null || result.size() == 0)
                {
                    this.handleViewEvent(commandId);
                    return true;
                }
                int index = model.getInt(KEY_I_INDEX);
                if (index > -1)
                {
                    Object obj = result.elementAt(index);
                    if (obj instanceof Address)
                    {
                        Address selectedAddress = (Address) obj;
                        if (commandId == CMD_COMMON_SHARE)
                        {
                            model.put(KEY_O_SHARED_ADDRESS, selectedAddress);
                        }
                        else if (commandId == CMD_COMMON_CALL)
                        {
                            Poi poi = selectedAddress.getPoi();
                            if (poi != null && poi.getBizPoi() != null)
                                model.put(KEY_S_POI_PHONENUMBER, poi.getBizPoi().getPhoneNumber());
                        }
                    }
                    else if (obj instanceof FavoriteCatalog)
                    {
                        FavoriteCatalog category = (FavoriteCatalog) obj;
                        model.put(KEY_O_CATEGORY, category);
                    }
                }
                this.handleViewEvent(commandId);
                return true;
            }
        }
        return false;
    }

    protected boolean prepareModelData(int state, int commandId)
    {
        if(!this.model.isActivated())
            return super.prepareModelData(state, commandId);
        switch (commandId)
        {
            case CMD_SELECT_ADDRESS:
            case CMD_FAVORITE_SELECTED:
            case CMD_DRIVETO_ADDRESS:
            case CMD_COMMON_EDIT:
            case CMD_ADD_TO_FOLDER:
            case CMD_MAP_ADDRESS:
            case CMD_COMMON_DELETE:
            case CMD_COMMON_SHARE:
            {
                switch (state)
                {
                    case STATE_MAIN:
                    case STATE_SUBCATEGORY:
                    case STATE_MAIN_EDIT_MODE:
                    case STATE_SUBCATEGORY_EDIT_MODE:
                    {
                        int index = model.getInt(KEY_I_INDEX);
                        Address selectedAddress = (Address) result.elementAt(index);
                        model.put(KEY_O_SELECTED_ADDRESS, selectedAddress);
                        model.put(KEY_O_CURRENT_ADDRESS, selectedAddress);
                        model.put(KEY_B_KEEP_MINI_BAR, true);
                        break;
                    }
                }
                break;
            }
            case CMD_EDIT_CATEGORY:
            case CMD_NEXT_CATEGORY:
            case CMD_RENAME_CATEGORY:
            case CMD_DELETE_CATEGORY:
            {
                switch (state)
                {
                    case STATE_MAIN:
                    case STATE_MAIN_EDIT_MODE:
                    {
                        int index = model.getInt(KEY_I_INDEX);
                        FavoriteCatalog category = (FavoriteCatalog) result.elementAt(index);
                        model.put(KEY_O_CATEGORY, category);
                        break;
                    }
                }
                break;
            }
        }

        return super.prepareModelData(state, commandId);
    }

    public void onTextChange(AbstractTnComponent component, String text)
    {
        int cmd = CMD_SEARCH_FAVORITE;
        this.model.put(KEY_S_SEARCH_TEXT, text);
        this.handleViewEvent(cmd);
    }
    
    private void updateLoginContextMenu()
    {
        boolean isSyncEnabled = MigrationExecutor.getInstance().isSyncEnabled();
        TnMenu menu = UiFactory.getInstance().createMenu();
        if(isSyncEnabled)
        {
            menu.add("", CMD_SYNC);
        }
        else
        {
            menu.setHeaderTitle(ResourceManager.getInstance().getCurrentBundle().getString(IStringFavorite.RES_PROMPT_ENABLE_SYNC, IStringFavorite.FAMILY_FAVORITE));
            menu.add(ResourceManager.getInstance().getCurrentBundle().getString(IStringLogin.RES_BTN_CREATE_ACCOUNT, IStringLogin.FAMILY_LOGIN), CMD_COMMON_CREATE_ACCOUNT);
            menu.add(ResourceManager.getInstance().getCurrentBundle().getString(IStringLogin.RES_BTN_LOGIN, IStringLogin.FAMILY_LOGIN), CMD_COMMON_LOGIN);
        }
        loginContextMenu.setMenu(menu, AbstractTnComponent.TYPE_CLICK);
    }

    public void onRefresh()
    {
        if (!MigrationExecutor.getInstance().isSyncEnabled())
        {
            if (myplaceRootView.findViewById(R.id.myplaceList0ListView) instanceof TnListView)
            {
                TnListView tempListView = (TnListView) myplaceRootView.findViewById(R.id.myplaceList0ListView);
                tempListView.stopRefresh();
            }
        }
        AndroidUiEventHandler.onClick(loginContextMenu);
        TnMenu menu = loginContextMenu.getMenu(AbstractTnComponent.TYPE_CLICK);
        if(menu != null)
        {
            if(menu.size() == 1) //SYNC
            {
                KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_MY_PLACES,
                    KontagentLogger.MY_PLACES_DRAG_TO_SYNC);
            }
            else if(menu.size() > 1) //Keep it for future differ
            {
                KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_MY_PLACES,
                    KontagentLogger.MY_PLACES_DRAG_TO_SYNC);
            }
        }
    }

    public void onLoadMore()
    {
        // TODO Auto-generated method stub
        
    }

    
    class MyPlaceListAdapter extends TnListAdapter
    {
        
        private LayoutInflater mInflater;
        private final static int ITEM_TYPE_CATEGORY = 0;
        private final static int ITEM_TYPE_GROUP = 1;
        private final static int ITEM_TYPE_NORMAL = 2;
        private final static int ITEM_TYPE_EDITOR = 3;
        private final static int ITEM_TYPE_EMPTY = 4;
        private final int[] LIST_ITEM_TYPE = {ITEM_TYPE_CATEGORY,ITEM_TYPE_GROUP,ITEM_TYPE_NORMAL,ITEM_TYPE_EDITOR, ITEM_TYPE_EMPTY};
        private boolean needMiniBar = false;
        private boolean isSubCategory = false;
        private boolean isEditMode = false;
        private boolean isEmptyAdapter = false;

        public MyPlaceListAdapter(Context context)
        {
            super(context);
            mInflater = LayoutInflater.from(context);
        }
        
        public boolean isEditMode()
        {
            return isEditMode;
        }
        
        public void setEditMode(boolean isEditMode)
        {
            this.isEditMode = isEditMode;
        }
        
        public void setNeedMiniBar(boolean needMiniBar)
        {
            this.needMiniBar = needMiniBar;
        }
        
        public void setIsSubCategory(boolean isSubCategory)
        {
            this.isSubCategory = isSubCategory;
        }
        
        public boolean isSubCategoryShown()
        {
            return this.isSubCategory;
        }

        public int getCount()
        {
            int size = 0;
            if (isSubCategory)
            {
                size = favorites.size();
                isEmptyAdapter = (size == 0);
                return isEmptyAdapter? 1 : size;
            }
            else
            {
                size = categories.size() + favorites.size();
                isEmptyAdapter = (size == 0);
                return isEmptyAdapter? 1 : size;
            }
        }
        
        private boolean isCategory(int position)
        {
            return isSubCategory? false : position < categories.size();
        }
        
        public boolean isGroupItem(int position)
        {
            int positionOff = getPositionOff(position);
            Address address = (Address) favorites.elementAt(positionOff);
            return address.getType() == Address.TYPE_GROUP;
        }
        
        private int getPositionOff(int position)
        {
            int positionOff = isSubCategory ? position : position - categories.size();
            positionOff = positionOff < 0 ? 0 : positionOff;
            return positionOff;
        }
        
        public int getItemViewType(int position)
        {
            if (isEmptyAdapter)
            {
                return ITEM_TYPE_EMPTY;
            }
            else if (isCategory(position))
            {
                return ITEM_TYPE_CATEGORY;
            }
            else if (isGroupItem(position))
            {
                return ITEM_TYPE_GROUP;
            }
            else if(FavoriteViewTouch.this.model.getState() == STATE_MAIN_EDIT_MODE)
            {
                return ITEM_TYPE_EDITOR;
            }
            else
            {
                return ITEM_TYPE_NORMAL;
            }
        }
        
        public int getViewTypeCount()
        {
            return LIST_ITEM_TYPE.length;
        }

        public Object getItem(int position)
        {
            return null;
        }

        public long getItemId(int position)
        {
            return 0;
        }

        protected View getItemView(int position, View convertView, ViewGroup parent)
        {
            if (isEmptyAdapter)
            {
                String searchText = FavoriteViewTouch.this.model.getString(KEY_S_SEARCH_TEXT);
                if (searchText != null && searchText.trim().length() > 0)
                    return getNoResultComponent(position, convertView, parent);
                else
                    return getEmptyComponent(position, convertView, parent);
                
            }
            else if (isSubCategory)
            {
                if (isGroupItem(position))
                {
                    return getGroupComponent(position, convertView, parent);
                }
                else
                {
                    return getAddressComponent(position, convertView, parent);
                }
            }
            else
            {
                if (isCategory(position))
                {
                    return getCategoryComponent(position, convertView, parent);
                }
                else if (isGroupItem(position))
                {
                    return getGroupComponent(position, convertView, parent);
                }
                else
                {
                    return getAddressComponent(position, convertView, parent);
                }
            }
        }

        protected View getEmptyComponent(int position, View convertView, ViewGroup parent)
        {
            convertView = mInflater.inflate(R.layout.my_places_emtpy, null);
            convertView.setPadding(0, ((FavoriteUiDecorator)FavoriteViewTouch.this.uiDecorator).FAVORITE_EMPTY_PADDING_TOP.getInt(), 0, 0);
            return convertView;
        }
        
        protected View getNoResultComponent(int position, View convertView, ViewGroup parent)
        {
            convertView = mInflater.inflate(R.layout.common_no_result, null);
            convertView.setPadding(0, ((FavoriteUiDecorator)FavoriteViewTouch.this.uiDecorator).FAVORITE_EMPTY_PADDING_TOP.getInt()/2, 0, 0);
            
            Context context = AndroidPersistentContext.getInstance().getContext();
            String searchText = model.getString(KEY_S_SEARCH_TEXT);
            TextView secondLine = (TextView) convertView.findViewById(R.id.commonNoResultSecondLine);
            String secondLineText = context.getString(R.string.commonNoResultsSecondLine, searchText);
            secondLine.setText(secondLineText);
            return convertView;
        }
        
        protected View getGroupComponent(int position, View convertView, ViewGroup parent)
        {
            if (favorites.size() == 0)
            {
                throw new RuntimeException("the count of favorites is not syncronized with list!!!");
            }

            int positionOff = getPositionOff(position);
            Address address = null;
            
            if (favorites.elementAt(positionOff) instanceof Address)
            {
                address = (Address) favorites.elementAt(positionOff);
            }
            
            if (convertView == null)
            {
                convertView = mInflater.inflate(R.layout.place_list0groupitem, null);
            }

            if (address == null)
            {
                convertView.setVisibility(View.GONE);
                return convertView;
            }

            PlaceListItem item = (PlaceListItem) convertView;
            item.init(PlaceListItem.TYPE_GROUP);
            item.setBrandName(address.getDisplayedText());

            return convertView;
        }
        
        protected View getAddressComponent(int position, View convertView, ViewGroup parent)
        {
            boolean isStrictValidTime = true;
            if (favorites.size() == 0)
            {
                throw new RuntimeException("the count of favorites is not syncronized with list!!!");
            }

            int positionOff = getPositionOff(position);
            Address address = null;
            
            if (favorites.elementAt(positionOff) instanceof Address)
            {
                address = (Address) favorites.elementAt(positionOff);
            }
            
            if (convertView == null)
            {
                int id = !isEditMode ? R.layout.place_list0normalitem
                        : R.layout.place_list0editlitem;
                convertView = mInflater.inflate(id, null);
            }

            if (address == null)
            {
                convertView.setVisibility(View.GONE);
                return convertView;
            }

            PlaceListItem item;
            if (convertView instanceof PlaceListItem)
            {
                item = (PlaceListItem) convertView;
            }
            else
            {
                item = (PlaceListItem) convertView.findViewById(R.id.placeListItem);
            }
            
            if(item == null)
            {
                return convertView;
            }
            
            item.init(PlaceListItem.TYPE_NORMAL);
            String label = address.getDisplayedText();
            item.setBrandName(label);
            
            if(address.getEventVenue() != null && address.getEventVenue().trim().length() > 0)
            {
                item.setAddress(address.getEventVenue());
            }
            else
            {
                item.setAddress(ResourceManager.getInstance().getStringConverter().convertAddress(address.getStop(), false));
            }
            item.setIndicatorIcon(R.drawable.list_icon_see_all_unfocused);
            
            Poi poi = address.getPoi();
            Context context = AndroidPersistentContext.getInstance().getContext();
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
            item.setGravity(Gravity.CENTER_VERTICAL);
            
            String lastLine = "";
            
            String category = ResourceManager.getInstance().getCurrentBundle()
                    .getString(IStringFavorite.RES_CATEGORY_OTHERS, IStringFavorite.FAMILY_FAVORITE);
            
            if (address.getEventId() > 0)
            {
                if (address.getEventStartTime() > 0)
                {
                    long timeMillions = address.getEventStartTime() * 1000;
                    lastLine = ResourceManager.getInstance().getStringConverter().getDateString(timeMillions, DateFormat.FULL);
                }
                else
                {
                    lastLine = ResourceManager.getInstance().getCurrentBundle()
                            .getString(IStringFavorite.RES_CATEGORY_EVENT, IStringFavorite.FAMILY_FAVORITE);
                }
            }
            else if (address.getPoi() != null && address.getPoi().getBizPoi() != null
                    && address.getPoi().getBizPoi().getCategoryName() != null
                    && address.getPoi().getBizPoi().getCategoryName().trim().length() > 0)
            {
                lastLine = address.getPoi().getBizPoi().getCategoryName();
            }
            else
            {
                lastLine = category;
            }
            
            address.setDistance(UiFactory.getInstance().getDistance(address.getStop(), null));
            
            if (!isEditMode)
            {
                item.setLastLine(lastLine);
                item.setDistance(UiFactory.getInstance().getDisplayDistance(address.getDistance()));
                TnMenu menu= null;
                if (address.getPoi() != null)
                {
                    menu = createListItemPoiContextMenu(position,address.getPoi());
                }
                else
                {
                    menu = createListItemAddressContextMenu(position);
                }
                menu.setHeaderTitle(label);
                item.setTnMenu(menu);
                //Fix bug TNANDROID-5306
                if (model.getInt(KEY_I_AC_TYPE) == TYPE_AC_FROM_ONE_BOX)
                {
                    View itemView = convertView.findViewById(R.id.placeList0NormalItem);
                    if (itemView != null)
                    {
                        itemView.setTag(position);
                        itemView.setOnClickListener(FavoriteViewTouch.this);
                    }
                }
            }
            else
            {
                item.findViewById(R.id.placeList0NormalItemCategoryViewGroup).setVisibility(View.GONE);
                View editButton = convertView.findViewById(R.id.placeList0EditItemEditButton);
                editButton.setTag(PrimitiveTypeCache.valueOf(position));
                if (editButton != null)
                {
                    editButton.setOnClickListener(FavoriteViewTouch.this);
                }
                View deleteButton = convertView.findViewById(R.id.placeList0EditItemDeleteButton);
                deleteButton.setTag(PrimitiveTypeCache.valueOf(position));
                if (deleteButton != null)
                {
                    deleteButton.setOnClickListener(FavoriteViewTouch.this);
                }
                item.findViewById(R.id.placeList0NormalItem).setBackgroundResource(0);
            }
            return convertView;
        }
        
        protected View getCategoryComponent(int position, View convertView, ViewGroup parent)
        {
            if (categories.size() == 0)
            {
                throw new RuntimeException("the count of categories is not syncronized with list!!!");
            }
            
            FavoriteCatalog category = (FavoriteCatalog) categories.elementAt(position);
            
            if (convertView == null)
            {
                if (isEditMode)
                {
                    convertView = mInflater.inflate(R.layout.place_list0editcategoryitem, null);
                }
                else
                {
                    convertView = mInflater.inflate(R.layout.place_list0categoryitem, null);
                }
            }

            boolean isReceivedCategory = category.isReceivedCatalog();

            PlaceListItem item = (PlaceListItem) convertView;
            item.init(PlaceListItem.TYPE_CATEGORY);
            item.setIndicatorIcon(R.drawable.list_icon_folder_unfocused);
            item.setGravity(Gravity.CENTER_VERTICAL);
            String badge = " ("
                    + DaoManager.getInstance().getAddressDao().getFavorateAddresses(category.getName(), false).size() + ")";
            if (isReceivedCategory)
                item.setBrandName(ResourceManager.getInstance().getCurrentBundle()
                        .getString(IStringCommon.RES_RECEIVED_CATEGORY, IStringCommon.FAMILY_COMMON)
                        + badge);
            else
                item.setBrandName(category.getName() + badge);
                
            
            if (!isEditMode)
            {
                TnMenu menu= createCategoryContextMenu(position);
                menu.setHeaderTitle(category.getName());
                item.setTnMenu(menu);
                View itemView = convertView.findViewById(R.id.placeList0CategoryItem);
                if (itemView != null)
                {
                    itemView.setTag(position);
                    itemView.setOnClickListener(FavoriteViewTouch.this);
                }
            }
            else
            {
                View editButton = convertView.findViewById(R.id.placeList0EditItemEditButton);
                View deleteButton = convertView.findViewById(R.id.placeList0EditItemDeleteButton);
                if (isReceivedCategory)
                {
                    editButton.setVisibility(View.INVISIBLE);
                    deleteButton.setVisibility(View.INVISIBLE);
                }
                else
                {
                    editButton.setTag(PrimitiveTypeCache.valueOf(position));
                    if (editButton != null)
                    {
                        editButton.setOnClickListener(FavoriteViewTouch.this);
                    }

                    deleteButton.setTag(PrimitiveTypeCache.valueOf(position));
                    if (deleteButton != null)
                    {
                        deleteButton.setOnClickListener(FavoriteViewTouch.this);
                    }
                }
            }
            
            return convertView;
        }

        protected View getItemViewBeforeSelected(int position, View convertView, ViewGroup parent)
        {
            if (needMiniBar)
            {
                return convertView.findViewById(R.id.placeList0NormalItem);
            }
            return null;
        }

        protected View getItemViewAfterSelected(int position, View convertView, ViewGroup parent)
        {
            if (needMiniBar)
            {
                View normalSelectedItem = convertView.findViewById(R.id.placeList0NormalSelectedItem);
                if (normalSelectedItem != null)
                {
                    View miniPoiBarDrive = normalSelectedItem.findViewById(R.id.commonMiniPoiBar0DriveView);
                    View miniPoiBarInfo = normalSelectedItem.findViewById(R.id.commonMiniPoiBar0InfoView);
                    View miniPoiBarMap = normalSelectedItem.findViewById(R.id.commonMiniPoiBar0MapView);
                    miniPoiBarDrive.setTag(position);
                    miniPoiBarDrive.setOnClickListener(FavoriteViewTouch.this);
                    miniPoiBarInfo.setTag(position);
                    miniPoiBarInfo.setOnClickListener(FavoriteViewTouch.this);
                    miniPoiBarMap.setTag(position);
                    miniPoiBarMap.setOnClickListener(FavoriteViewTouch.this);
                    
                    if (!NetworkStatusManager.getInstance().isConnected())
                    {
                        View infoTextPart = miniPoiBarInfo.findViewById(R.id.commonMiniPoiBar0InfoTextPart);
                        View infoIconPart = miniPoiBarInfo.findViewById(R.id.commonMiniPoiBar0InfoIconPart);
                        miniPoiBarInfo.setEnabled(false);
                        infoTextPart.setEnabled(false);
                        infoIconPart.setEnabled(false);
                    }
                }
                return normalSelectedItem;
            }
            return null;
        }
    }
    
    public void onClick(View v)
    {
        int index = -1;
        index = (Integer) v.getTag();
        if (index < 0)
        {
            index = 0;
        }
        
        int componentId = v.getId();
        int cmdId = -1;
        
        Object obj = result.get(index);
        switch (componentId)
        {
            case R.id.commonMiniPoiBar0DriveView:
            {
                cmdId = CMD_DRIVETO_ADDRESS;
                break;
            }
            case R.id.commonMiniPoiBar0InfoView:
            {
                cmdId = CMD_SELECT_ADDRESS;
                break;
            }
            case R.id.commonMiniPoiBar0MapView:
            {
                cmdId = CMD_MAP_ADDRESS;
                break;
            }
            case R.id.placeList0EditItemEditButton:
            {
                if (obj instanceof Address)
                {
                    cmdId = CMD_COMMON_EDIT;
                }
                else
                {
                    cmdId = CMD_EDIT_CATEGORY;
                }
                break;
            }
            case R.id.placeList0EditItemDeleteButton:
            {
                if (obj instanceof Address)
                {
                    cmdId = CMD_COMMON_DELETE;
                }
                else
                {
                    cmdId = CMD_DELETE_CATEGORY;
                }
                break;
            }
            case R.id.placeList0CategoryItem:
            {
                cmdId = CMD_NEXT_CATEGORY;
                break;
            }
            case R.id.placeList0NormalItem:
            {
                cmdId = CMD_FAVORITE_SELECTED;
                break;
            }
            default:
            {
                cmdId = -1;
                break;
            }
        }

        if (cmdId > -1)
        {
            model.put(KEY_I_INDEX, ((Integer) v.getTag()).intValue());
            model.put(KEY_O_SELECTED_ADDRESS, obj);
            AndroidCitizenUiHelper.triggerCommand(this, v, cmdId);
        }
    }
    
    public boolean handleUiEvent(TnUiEvent tnUiEvent)
    {
        logKtUiEvent(tnUiEvent);
        if (tnUiEvent != null && tnUiEvent.getType() == TnUiEvent.TYPE_KEY_EVENT && (tnUiEvent.getKeyEvent() != null))
        {
            TnKeyEvent event = tnUiEvent.getKeyEvent();
            int keyCode = event.getCode();
            if (event.getAction() == TnKeyEvent.ACTION_DOWN)
            {
               switch (keyCode)
               {
                   case TnKeyEvent.KEYCODE_ENTER:
                   {
                       if(tnUiEvent.getComponent() != null)
                       {
                           TeleNavDelegate.getInstance().closeVirtualKeyBoard(tnUiEvent.getComponent());
                       }
                       break;
                   }
                   default:
                   {
                       break;
                   }
               }
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
                case CMD_MAP_ADDRESS:
                {
                    KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_MY_PLACES,
                        KontagentLogger.MY_PLACES_MAP_CLICKED);
                    break;
                }
                case CMD_DRIVETO_ADDRESS:
                {
                    KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_MY_PLACES,
                        KontagentLogger.MY_PLACES_DRIVE_CLICKED);
                    break;
                }
                case CMD_SELECT_ADDRESS:
                {
                    View v = (View)tnUiEvent.getComponent().getNativeUiComponent();
                    if(v != null)
                    {
                        if(v.getId() == R.id.commonMiniPoiBar0InfoView)
                        {
                            KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_MY_PLACES,
                                KontagentLogger.MY_PLACES_INFO_CLICKED);
                        }
                    }
                    break;
                }
                case CMD_ADD_FAVORITE:
                {
                    KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_MY_PLACES,
                        KontagentLogger.MY_PLACES_ADD_CLICKED);
                    break;
                }
                case CMD_SORT :
                {
                    KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_MY_PLACES,
                        KontagentLogger.MY_PLACES_SORT_CLICKED);
                    break;
                }
                case CMD_ENTER_EDIT_MODE:
                {
                    KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_MY_PLACES,
                        KontagentLogger.MY_PLACES_EDIT_CLICKED);
                    break;
                }   
           }
        }
//        else if(tnUiEvent != null && tnUiEvent.getType() == TnUiEvent.TYPE_KEY_EVENT)
//        {
//            if (tnUiEvent.getKeyEvent()!= null && tnUiEvent.getKeyEvent().getAction() == TnKeyEvent.ACTION_DOWN
//                    && tnUiEvent.getKeyEvent().getCode() == TnKeyEvent.KEYCODE_BACK && this.model.getState() == STATE_MAIN)
//            {
//                KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_MY_PLACES, KontagentLogger.MY_PLACES_BACK_CLICKED);
//            }
//        }
    }
    
    @Override
    public void OnTnItemSelectedChange(int oldItemIndex, int newItemIndex)
    {
        if(newItemIndex >= 0)
        {
            if(newItemIndex < result.size())
            {
                Object obj = result.get(newItemIndex);
                if(obj instanceof Address)
                {
                    Address addr = (Address)obj;
                    if(addr.getPoi() == null && addr.getEventId() <= 0)
                    {
                        KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_MY_PLACES, KontagentLogger.MY_PLACES_ADDRESS_CLICKED);
                    }
                    else
                    {
                        if(addr.getEventId() > 0)
                        {
                            KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_MY_PLACES, KontagentLogger.MY_PLACES_EVENT_CLICKED);
                        }
                        else
                        {
                            KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_MY_PLACES, KontagentLogger.MY_PLACES_POI_CLICKED);
                        }
                    }
                }
            }
        }
    }
}
