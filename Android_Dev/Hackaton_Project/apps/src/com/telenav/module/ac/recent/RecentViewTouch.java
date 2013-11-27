/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * RecentViewTouch.java
 *
 */
package com.telenav.module.ac.recent;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Vector;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.telenav.app.TeleNavDelegate;
import com.telenav.app.android.AndroidPersistentContext;
import com.telenav.app.android.scout_us.R;
import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.dao.serverproxy.AddressDao;
import com.telenav.data.datatypes.address.Address;
import com.telenav.data.datatypes.address.Stop;
import com.telenav.data.datatypes.poi.Poi;
import com.telenav.data.serverproxy.NetworkStatusManager;
import com.telenav.location.TnLocation;
import com.telenav.module.AppConfigHelper;
import com.telenav.module.ac.AddressListFilter;
import com.telenav.module.location.LocationProvider;
import com.telenav.module.mapdownload.MapDownloadStatusManager;
import com.telenav.module.sync.MigrationExecutor;
import com.telenav.mvc.AbstractCommonUiDecorator;
import com.telenav.mvc.AbstractCommonView;
import com.telenav.res.INinePatchImageRes;
import com.telenav.res.IStringAc;
import com.telenav.res.IStringCommon;
import com.telenav.res.IStringEditCategory;
import com.telenav.res.IStringFavorite;
import com.telenav.res.IStringLogin;
import com.telenav.res.IStringRecent;
import com.telenav.res.ResourceManager;
import com.telenav.sdk.kontagent.KontagentLogger;
import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.core.AbstractTnUiHelper;
import com.telenav.tnui.core.ITnScreenAttachedListener;
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
import com.telenav.ui.NinePatchImageDecorator;
import com.telenav.ui.UiFactory;
import com.telenav.ui.UiStyleManager;
import com.telenav.ui.android.OnTnItemLongClickListener;
import com.telenav.ui.android.PlaceListItem;
import com.telenav.ui.android.TnListAdapter;
import com.telenav.ui.android.TnListView;
import com.telenav.ui.android.TnListView.ITnListViewListener;
import com.telenav.ui.android.TnListView.OnTnItemSelectedChangeListener;
import com.telenav.ui.citizen.CitizenAddressListItem;
import com.telenav.ui.citizen.CitizenScreen;
import com.telenav.ui.citizen.CitizenSlidableContainer;
import com.telenav.ui.citizen.CitizenTextField;
import com.telenav.ui.citizen.android.AndroidCitizenUiHelper;
import com.telenav.ui.frogui.widget.FrogButton;
import com.telenav.ui.frogui.widget.FrogMultiLine;
import com.telenav.ui.frogui.widget.FrogNullField;

/**
 * @author wzhu (wzhu@telenav.cn)
 * @date 2010-10-27
 */
class RecentViewTouch extends AbstractCommonView implements IRecentConstants, ITnListViewListener, OnClickListener, OnTnItemSelectedChangeListener
{
    TnListView recentList;

    ImageView deleteAllButton;

    RecentListAdapter recentListAdapter;

    FrogNullField loginContextMenu;
    
    public RecentViewTouch(AbstractCommonUiDecorator uiDecorator)
    {
        super(uiDecorator);
    }

    protected int transformCommandDelegate(int state, TnUiEvent tnUiEvent)
    {
        int cmd = CMD_NONE;
        switch (state)
        {
            case STATE_COMMON_ERROR:
            {
                cmd = CMD_TIME_OUT;
                break;
            }
            case STATE_MAIN:
            {
                if (tnUiEvent.getType() == TnUiEvent.TYPE_KEY_EVENT
                        && tnUiEvent.getKeyEvent().getAction() == TnKeyEvent.ACTION_UP
                        && tnUiEvent.getKeyEvent().getCode() == TnKeyEvent.KEYCODE_DEL)
                {
                    if (tnUiEvent.getComponent() instanceof CitizenAddressListItem)
                    {
                        int id = tnUiEvent.getComponent().getId();
                        model.put(KEY_O_SELECTED_ADDRESS, model.getVector(KEY_V_CURRENT_RECENT_ADDRESSES).elementAt(id));
                        cmd = CMD_COMMON_DELETE;
                    }
                }
                break;
            }
        }
        return cmd;
    }

    public void onScreenUiEngineAttached(final TnScreen screen, int attached)
    {
        if (screen != null && attached == ITnScreenAttachedListener.AFTER_ATTACHED)
        {
            TeleNavDelegate.getInstance().setOrientation(TeleNavDelegate.ORIENTATION_UNSPECIFIED);
        }
        else if (attached == DETTACHED)
        {
            TeleNavDelegate.getInstance().callAppNativeFeature(TeleNavDelegate.FEATURE_UPDATE_WINDOW_SOFT_INPUT_MODE, null);
        }
    }

    protected void activate()
    {
        // FIXME: adapter the font size of native XML configuration
//        TeleNavDelegate.getInstance().callAppNativeFeature(TeleNavDelegate.FEATURE_UPDATE_WINDOW_SOFT_INPUT_MODE,
//          new Object[]
//          { PrimitiveTypeCache.valueOf(false), PrimitiveTypeCache.valueOf(true) });
        ((AndroidUiHelper) AbstractTnUiHelper.getInstance()).setFontDensity(false);
        
        super.activate();
    }

    protected void deactivateDelegate()
    {
        ((AndroidUiHelper) AbstractTnUiHelper.getInstance()).setFontDensity(true);
        super.deactivateDelegate();
    }

    protected TnPopupContainer createPopup(int state)
    {
        switch (state)
        {
            case STATE_DELETE_ALL_CONFIRM:
            {
                return createConfirmPopup(state);
            }
            case STATE_DELETE_RECENT:
            {
                return createDeleteRecentPopup(state);
            }
        }
        return null;
    }

    private TnPopupContainer createDeleteRecentPopup(int state)
    {
        TnPopupContainer popup = UiFactory.getInstance().createPopupContainer(state);
        TnLinearContainer container = UiFactory.getInstance().createLinearContainer(0, true);
        container.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((RecentUiDecorator) this.uiDecorator).POPUP_WIDTH);

        TnLinearContainer topContainer = UiFactory.getInstance().createLinearContainer(0, true,
            AbstractTnGraphics.VCENTER | AbstractTnGraphics.HCENTER);
        int max = AppConfigHelper.getMaxDisplaySize();
        int left = 20 * max / 480;
        int right = 20 * max / 480;
        int top = 20 * max / 480;
        int bottom = 10 * max / 480;
        topContainer.setPadding(left, top, right, bottom);
        topContainer.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.MESSAGE_BOX_TOP_BG);
        topContainer.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, NinePatchImageDecorator.MESSAGE_BOX_TOP_BG);
        Address address = (Address) model.get(KEY_O_SELECTED_ADDRESS);

        FrogMultiLine title = UiFactory.getInstance().createMultiLine(
            0,
            ResourceManager.getInstance().getCurrentBundle()
                    .getString(IStringRecent.RES_DELETE_RECENT, IStringRecent.FAMILY_RECENT));
        title.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((RecentUiDecorator) uiDecorator).MULTILINE_WIDTH);
        title.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((RecentUiDecorator) uiDecorator).MULTILINE_HEIGHT);
        title.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_NAV_INFO_BAR));
        title.setTextAlign(FrogMultiLine.TEXT_ALIGN_CENTER);
        title.setForegroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_ME_GR));
        topContainer.add(title);

        FrogNullField nullfield = UiFactory.getInstance().createNullField(0);
        nullfield.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((RecentUiDecorator) uiDecorator).NULLFIELD_HEIGHT);
        topContainer.add(nullfield);

        CitizenAddressListItem item = UiFactory.getInstance().createCitizenAddressListItem(ID_DELETE_RECENT_ITEM);
        item.setPadding(item.getLeftPadding(), 2 * item.getTopPadding(), item.getRightPadding(), 2 * item.getBottomPadding());
        item.setTitleColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_BLUE), UiStyleManager.getInstance()
                .getColor(UiStyleManager.TEXT_COLOR_BLUE));
        item.setGap(5, 0);
        item.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((RecentUiDecorator) this.uiDecorator).PURE_WHITE_BG_HEIGHT);
        Vector categories = address.getCatagories();
        boolean isReceivedAddress = false;
        if (categories != null)
        {
            for (int i = 0; i < categories.size(); i++)
            {
                String category = (String) categories.elementAt(i);
                if (category.equals(AddressDao.RECEIVED_ADDRESSES_CATEGORY))
                {
                    isReceivedAddress = true;
                    break;
                }
            }
        }

        if (isReceivedAddress)
        {
            item.getTnUiArgs().put(CitizenAddressListItem.KEY_LEFT_ICON_UNFOCUS,
                ImageDecorator.IMG_LIST_RECEIVED_ICON_UNFOCUSED);
            item.getTnUiArgs().put(CitizenAddressListItem.KEY_LEFT_ICON_FOCUS, ImageDecorator.IMG_LIST_RECEIVED_ICON_UNFOCUSED);
        }
        else
        {
            item.getTnUiArgs()
                    .put(CitizenAddressListItem.KEY_LEFT_ICON_UNFOCUS, ImageDecorator.IMG_LIST_HISTORY_ICON_UNFOCUSED);
            item.getTnUiArgs().put(CitizenAddressListItem.KEY_LEFT_ICON_FOCUS, ImageDecorator.IMG_LIST_HISTORY_ICON_UNFOCUSED);
        }

        item.setForegroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_ME_GR), UiStyleManager
                .getInstance().getColor(UiStyleManager.TEXT_COLOR_ME_GR));
        
        String sHome = ResourceManager.getInstance().getCurrentBundle().getString(IStringAc.RES_BTTN_HOME, IStringAc.FAMILY_AC);
        String sWork = ResourceManager.getInstance().getCurrentBundle().getString(IStringAc.RES_BTN_WORK, IStringAc.FAMILY_AC);
        if (AddressListFilter.isHome(address))
        {
            item.setTitle(sHome);
        }
        else if (AddressListFilter.isWork(address))
        {
            item.setTitle(sWork);
        }
        else if (address.getLabel() != null && !"".equals(address.getLabel().trim()))
        {
            item.setTitle(address.getLabel());
        }
        else
        {
            item.setTitle(ResourceManager.getInstance().getStringConverter().convertAddress(address.getStop(), false));
        }
        setItemInfo(address, item);
        if (isComponentNeeded(INinePatchImageRes.FAVORITE_PURE_WHITE_BG + INinePatchImageRes.ID_UNFOCUSED))
        {
            item.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.FAVORITE_PURE_WHITE_BG);
            item.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, NinePatchImageDecorator.FAVORITE_PURE_WHITE_BG);
        }
        else
        {
            item.getTnUiArgs().remove(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS);
            item.getTnUiArgs().remove(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS);
        }
        item.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_LIST_DUAL_LINE_BOTTOM));
        item.setBoldFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_LIST_DUAL_LINE_TOP));
        topContainer.add(item);

        TnLinearContainer buttonContainer = UiFactory.getInstance().createLinearContainer(0, false,
            AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);
        buttonContainer.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, NinePatchImageDecorator.MESSAGE_BOX_BOTTOM_BG);
        buttonContainer.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.MESSAGE_BOX_BOTTOM_BG);
        buttonContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT,
            ((RecentUiDecorator) this.uiDecorator).BUTTON_CONTAINER_HEIGHT);
        buttonContainer.setPadding(0, 8, 0, 18);

        FrogButton deleteButton = UiFactory.getInstance().createButton(
            0,
            ResourceManager.getInstance().getCurrentBundle()
                    .getString(IStringEditCategory.RES_BTTN_DELETE, IStringEditCategory.FAMILY_EDIT_CATEGORY));
        deleteButton.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((RecentUiDecorator) uiDecorator).BUTTON_EDIT_CATEGORY_WIDTH);
        deleteButton.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((RecentUiDecorator) uiDecorator).BUTON_HEIGHT);
        TnMenu menu = UiFactory.getInstance().createMenu();
        menu.add("", CMD_COMMON_OK);
        deleteButton.setMenu(menu, AbstractTnComponent.TYPE_CLICK);
        deleteButton.setCommandEventListener(this);

        FrogNullField nullField1 = UiFactory.getInstance().createNullField(0);
        nullField1.getTnUiArgs()
                .put(TnUiArgs.KEY_PREFER_WIDTH, ((RecentUiDecorator) uiDecorator).NULLFIELD_EDIT_CATEGORY_WIDTH);

        FrogButton cancelButton = UiFactory.getInstance().createButton(
            0,
            ResourceManager.getInstance().getCurrentBundle()
                    .getString(IStringEditCategory.RES_BTTN_CANCEL, IStringEditCategory.FAMILY_EDIT_CATEGORY));
        cancelButton.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((RecentUiDecorator) uiDecorator).BUTTON_EDIT_CATEGORY_WIDTH);
        cancelButton.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((RecentUiDecorator) uiDecorator).BUTON_HEIGHT);
        menu = UiFactory.getInstance().createMenu();
        menu.add("", CMD_COMMON_CANCEL);
        cancelButton.setMenu(menu, AbstractTnComponent.TYPE_CLICK);
        cancelButton.setCommandEventListener(this);
        buttonContainer.add(deleteButton);
        buttonContainer.add(nullField1);
        buttonContainer.add(cancelButton);

        container.add(topContainer);
        container.add(buttonContainer);
        popup.setContent(container);
        popup.setSizeChangeListener(this);
        return popup;
    }

    private void setItemInfo(Address address, CitizenAddressListItem item)
    {
        if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
        {
            String firstLine = address.getStop().getFirstLine();
            if (firstLine == null || firstLine.length() == 0)
            {
                item.setAddress(ResourceManager.getInstance().getStringConverter().convertAddress(address.getStop(), false));
                item.setShareInfo(null);
            }
            else
            {
                item.setAddress(firstLine);
                item.setShareInfo(convertSecondLine(address.getStop()));
            }
            item.setShareInfoColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_ME_GR), UiStyleManager
                    .getInstance().getColor(UiStyleManager.TEXT_COLOR_ME_GR));
        }
        else
        {
            String firstLine = address.getStop().getFirstLine();
            if (firstLine == null || firstLine.length() == 0)
            {
                item.setAddress(ResourceManager.getInstance().getStringConverter().convertAddress(address.getStop(), false));
            }
            else
            {
                item.setAddress(firstLine + " " + convertSecondLine(address.getStop()));
            }
            item.setShareInfo(null);
        }
    }

    protected String convertSecondLine(Stop address)
    {
        if (address == null)
            return null;
        StringBuffer buffer = new StringBuffer();
        if (address.getCity() != null && !"".equals(address.getCity()))
        {
            buffer.append(address.getCity());
        }
        if (address.getProvince() != null && !"".equals(address.getProvince()))
        {
            if (buffer.length() > 0)
            {
                buffer.append(", ");
            }
            buffer.append(address.getProvince());
        }
        return buffer.toString();
    }

    private TnPopupContainer createConfirmPopup(int state)
    {
        TnMenu menus = UiFactory.getInstance().createMenu();
        menus.add(
            ResourceManager.getInstance().getCurrentBundle()
                    .getString(IStringRecent.RES_BTN_DELETE, IStringRecent.FAMILY_RECENT), CMD_COMMON_OK);
        menus.add(
            ResourceManager.getInstance().getCurrentBundle()
                    .getString(IStringRecent.RES_BTN_CANCEL, IStringRecent.FAMILY_RECENT), CMD_COMMON_CANCEL);
        String text = ResourceManager.getInstance().getCurrentBundle()
                .getString(IStringRecent.RES_MULTILINE_MESSAGE_BOX, IStringRecent.FAMILY_RECENT);
        return UiFactory.getInstance().createMessageBox(state, text, menus);
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
                return createRecentMainScreen(state);
            }
        }
        return null;
    }

    private TnScreen createRecentMainScreen(int state)
    {
        CitizenScreen recentScreen = UiFactory.getInstance().createScreen(state);
        View mainView = AndroidCitizenUiHelper.addContentView(recentScreen, R.layout.recent);

        TextView titleView = (TextView) mainView.findViewById(R.id.commonTitle0TextView);
        titleView.setText(ResourceManager.getInstance().getCurrentBundle()
                .getString(IStringRecent.RES_LABEL_RECENT_PLACES, IStringRecent.FAMILY_RECENT));

        ViewGroup filerContainer = (ViewGroup) mainView.findViewById(R.id.recent0FilterContainer);
        if (filerContainer != null)
        {
            String hint = ResourceManager.getInstance().getCurrentBundle()
                    .getString(IStringRecent.RES_HINT_RECENT_PLACES, IStringRecent.FAMILY_RECENT);
            CitizenTextField dropDownField = UiFactory.getInstance().createCitizenTextField("", 100, TnTextField.ANY, hint, 0,
                ImageDecorator.SEARCHBOX_ICON_HINT_UNFOCUSED.getImage(), ImageDecorator.IMG_AC_BACKSPACE.getImage());
            dropDownField.setBackgroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_WH));

            dropDownField.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((RecentUiDecorator) uiDecorator).TEXTFIELD_HEIGHT);
            dropDownField.getTnUiArgs()
                    .put(TnUiArgs.KEY_PREFER_WIDTH, ((RecentUiDecorator) uiDecorator).TEXTFIELD_COMMON_WIDTH);
            dropDownField.getTnUiArgs().put(CitizenTextField.KEY_PREFER_TEXTFIELD_HEIGHT, ((RecentUiDecorator) uiDecorator).TEXTFIELD_HEIGHT);
            dropDownField.setHintTextColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_FILTER));
            dropDownField.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_TEXT_FIELD));
            dropDownField.setKeyEventListener(this);
            dropDownField.setTextChangeListener(new ITnTextChangeListener()
            {
                public void onTextChange(AbstractTnComponent component, String text)
                {
                    int cmd = CMD_SEARCH_ADDRESS;
                    RecentViewTouch.this.model.put(KEY_S_SEARCH_TEXT, text);
                    RecentViewTouch.this.handleViewEvent(cmd);
                }
            });
            filerContainer.addView((View) dropDownField.getNativeUiComponent());
        }
        deleteAllButton = (ImageView) mainView.findViewById(R.id.commonTitle0IconButton);
        deleteAllButton.setImageResource(R.drawable.title_icon_delete_unfocused);
        deleteAllButton.setVisibility(View.VISIBLE);
        AndroidCitizenUiHelper.setOnClickCommand(this, deleteAllButton, CMD_DELETE_RECENT);

        Vector recentPlaces = model.getVector(KEY_V_CURRENT_RECENT_ADDRESSES);

        if (recentPlaces == null || recentPlaces.size() == 0)
        {
            deleteAllButton.setVisibility(View.GONE);
        }

        recentList = (TnListView) mainView.findViewById(R.id.recent0ListView);
        recentList.setPullLoadEnable(false);
        recentListAdapter = new RecentListAdapter(mainView.getContext(), recentPlaces);
        recentList.setAdapter(recentListAdapter);
        recentList.setTnListViewListener(this);
        recentList.setOnTnItemSelectedChangeListener(this);
        
        long lastSyncTime = DaoManager.getInstance().getAddressDao().getSyncTime(true);
        if (lastSyncTime > 0)
        {
            recentList.setRefreshTime(new Date(lastSyncTime));
        }

        recentList.setOnTnItemLongClickListener(new OnTnItemLongClickListener()
        {

            protected boolean onTnItemLongClick(AdapterView<?> parent, View view, int position, long id)
            {
                Address address = (Address) model.getVector(KEY_V_CURRENT_RECENT_ADDRESSES).get(position);
                model.put(KEY_O_CURRENT_ADDRESS, address);
                model.put(KEY_O_SELECTED_ADDRESS, address);
                model.put(KEY_B_KEEP_MINI_BAR, true);
                return view.showContextMenu();
            }
        });

        loginContextMenu = UiFactory.getInstance().createNullField(0);
        updateLoginContextMenu();
        loginContextMenu.setCommandEventListener(this);
        recentScreen.getRootContainer().add(loginContextMenu);

        return recentScreen;
    }

    private void updateLoginContextMenu()
    {
        boolean isSyncEnabled = MigrationExecutor.getInstance().isSyncEnabled();
        TnMenu menu = UiFactory.getInstance().createMenu();
        if (isSyncEnabled)
        {
            menu.add("", CMD_SYNC_RECENT);
        }
        else
        {
            menu.setHeaderTitle(ResourceManager.getInstance().getCurrentBundle()
                    .getString(IStringFavorite.RES_PROMPT_ENABLE_SYNC, IStringFavorite.FAMILY_FAVORITE));
            menu.add(
                ResourceManager.getInstance().getCurrentBundle()
                        .getString(IStringLogin.RES_BTN_CREATE_ACCOUNT, IStringLogin.FAMILY_LOGIN), CMD_COMMON_CREATE_ACCOUNT);
            menu.add(
                ResourceManager.getInstance().getCurrentBundle()
                        .getString(IStringLogin.RES_BTN_LOGIN, IStringLogin.FAMILY_LOGIN), CMD_COMMON_LOGIN);
        }
        loginContextMenu.setMenu(menu, AbstractTnComponent.TYPE_CLICK);
    }

    private TnMenu createListItemAddressContextMenu()
    {
        TnMenu contextMenu = UiFactory.getInstance().createMenu();

        if (isNeedMiniBar())
        {
            contextMenu.add(
                ResourceManager.getInstance().getCurrentBundle()
                        .getString(IStringCommon.RES_BTTN_DRIVETO, IStringCommon.FAMILY_COMMON), CMD_DRIVETO_ADDRESS);
            contextMenu.add(
                ResourceManager.getInstance().getCurrentBundle()
                        .getString(IStringCommon.RES_MENU_MAP, IStringCommon.FAMILY_COMMON), CMD_MAP_ADDRESS);
            contextMenu.add(
                ResourceManager.getInstance().getCurrentBundle()
                        .getString(IStringCommon.RES_SHARE, IStringCommon.FAMILY_COMMON), CMD_COMMON_SHARE);
        }
        contextMenu.add(
            ResourceManager.getInstance().getCurrentBundle()
                    .getString(IStringCommon.RES_ADD_TO_FAVORITE, IStringCommon.FAMILY_COMMON), CMD_COMMON_ADD_TO_FAVORITES);
        contextMenu.add(
            ResourceManager.getInstance().getCurrentBundle()
                    .getString(IStringCommon.RES_BTTN_DELETE, IStringCommon.FAMILY_COMMON), CMD_COMMON_DELETE);
        return contextMenu;
    }

    private TnMenu createListItemPoiContextMenu(Poi poi)
    {
        TnMenu contextMenu = UiFactory.getInstance().createMenu();

        if (isNeedMiniBar())
        {
            contextMenu.add(
                ResourceManager.getInstance().getCurrentBundle()
                        .getString(IStringCommon.RES_BTTN_DRIVETO, IStringCommon.FAMILY_COMMON), CMD_DRIVETO_ADDRESS);
            if (poi.getBizPoi() != null && poi.getBizPoi().getPhoneNumber() != null
                    && !"".equals(poi.getBizPoi().getPhoneNumber()))
            {
                contextMenu.add(
                    ResourceManager.getInstance().getCurrentBundle()
                            .getString(IStringCommon.RES_MENU_CALL, IStringCommon.FAMILY_COMMON)
                            + " " + poi.getBizPoi().getPhoneNumber(), CMD_COMMON_CALL);
            }
            contextMenu.add(
                ResourceManager.getInstance().getCurrentBundle()
                        .getString(IStringCommon.RES_MENU_MAP, IStringCommon.FAMILY_COMMON), CMD_MAP_ADDRESS);
            contextMenu.add(
                ResourceManager.getInstance().getCurrentBundle()
                        .getString(IStringCommon.RES_SHARE, IStringCommon.FAMILY_COMMON), CMD_COMMON_SHARE);
        }
        contextMenu.add(
            ResourceManager.getInstance().getCurrentBundle()
                    .getString(IStringCommon.RES_ADD_TO_FAVORITE, IStringCommon.FAMILY_COMMON), CMD_COMMON_ADD_TO_FAVORITES);
        contextMenu.add(
            ResourceManager.getInstance().getCurrentBundle()
                    .getString(IStringCommon.RES_BTTN_DELETE, IStringCommon.FAMILY_COMMON), CMD_COMMON_DELETE);
        return contextMenu;
    }

    protected boolean updateScreen(int state, TnScreen screen)
    {
        switch (state)
        {
            case STATE_MAIN:
            {
                boolean isBackToMyProfile = this.model.fetchBool(KEY_B_IS_START_BY_OTHER_CONTROLLER);
                if (isBackToMyProfile && isCredentialInfoExisted())
                {
                    showNotificationContainer(STATE_MAIN);
                }
                boolean isNeedRecreate = model.fetchBool(KEY_B_IS_NEED_RECREATE);

                if (recentList != null)
                {
                    long lastSyncTime = DaoManager.getInstance().getAddressDao().getSyncTime(true);
                    if (lastSyncTime > 0)
                    {
                        recentList.setRefreshTime(new Date(lastSyncTime));
                    }
                    if (model.fetchBool(KEY_B_REFRESH_LIST) || isNeedRecreate)
                    {
                        Vector recentPlaces = (Vector) model.get(KEY_V_CURRENT_RECENT_ADDRESSES);
                        if (recentPlaces == null || recentPlaces.size() == 0)
                        {
                            deleteAllButton.setVisibility(View.GONE);
                        }
                        else
                        {
                            deleteAllButton.setVisibility(View.VISIBLE);
                        }
                        recentListAdapter.setAddresses(recentPlaces);
                        recentListAdapter.notifyDataSetChanged();
                        boolean keepMiniBar = this.model.fetchBool(KEY_B_KEEP_MINI_BAR);
                        if(keepMiniBar)
                        {
                            keepMiniBar = false;
                            Address oldAddress = (Address) this.model.fetch(KEY_O_CURRENT_ADDRESS);
                            final int index = this.model.getInt(KEY_I_INDEX);
                            if(index > -1 && model.getVector(KEY_V_CURRENT_RECENT_ADDRESSES).get(index) instanceof Address)
                            {
                                Address newAddress = (Address) model.getVector(KEY_V_CURRENT_RECENT_ADDRESSES).get(index);
                                if(newAddress != null && oldAddress != null)
                                {
                                    keepMiniBar = newAddress.isSameAddress(oldAddress);
                                }
                            }
                        }
                        if(!keepMiniBar)
                        {
                            recentList.stopRefresh();
                            this.model.remove(KEY_I_INDEX);
                        }
                    }
                    recentList.requestLayout();
                }
                updateLoginContextMenu();

                if (isNeedRecreate)
                {
                    recentList.removeAllViewsInLayout();
                    recentList.requestLayout();

                    ((AndroidUiHelper) AndroidUiHelper.getInstance()).closeContextMenu();
                }
                return true;
            }
        }
        return false;
    }

    protected boolean preProcessUIEvent(TnUiEvent tnUiEvent)
    {
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
                int cmdId = tnUiEvent.getCommandEvent().getCommand();
                switch (cmdId)
                {
                    case CMD_COMMON_SHARE:
                    {
                        model.put(KEY_O_SHARED_ADDRESS, model.get(KEY_O_SELECTED_ADDRESS));
                        break;
                    }
                    case CMD_COMMON_CALL:
                    {
                        Poi poi = ((Address) model.get(KEY_O_SELECTED_ADDRESS)).getPoi();
                        if (poi != null && poi.getBizPoi() != null)
                        {
                            model.put(KEY_S_POI_PHONENUMBER, poi.getBizPoi().getPhoneNumber());
                        }
                    }
                }
                break;
            }
        }
        return super.preProcessUIEvent(tnUiEvent);
    }

    protected boolean prepareModelData(int state, int commandId)
    {
        switch (state)
        {
            case STATE_MAIN:
            {
                if (commandId == CMD_DELETE_RECENT)
                {
                    int selectedIndex = this.model.getInt(KEY_I_INDEX);
                    if (selectedIndex > -1 && selectedIndex < model.getVector(KEY_V_CURRENT_RECENT_ADDRESSES).size())
                    {
                        model.put(KEY_O_SELECTED_ADDRESS,
                            model.getVector(KEY_V_CURRENT_RECENT_ADDRESSES).elementAt(selectedIndex));
                    }
                    else
                    {
                        model.remove(KEY_I_INDEX);
                    }
                }
                break;
            }
        }
        return super.prepareModelData(state, commandId);
    }

    public void onSizeChanged(AbstractTnComponent tnComponent, int w, int h, int oldw, int oldh)
    {
        switch (model.getState())
        {
            case STATE_DELETE_RECENT:
            {
                TnPopupContainer popup = this.getCurrentPopup();
                CitizenAddressListItem item = (CitizenAddressListItem) popup.getContent().getComponentById(
                    ID_DELETE_RECENT_ITEM);
                Address address = (Address) model.get(KEY_O_SELECTED_ADDRESS);
                setItemInfo(address, item);
                break;
            }
            case STATE_MAIN:
            {
                if (notificationContainer != null && notificationContainer.isShown())
                {
                    showNotificationContainer(STATE_MAIN);
                }
                break;
            }
        }
        ((AndroidUiHelper) AbstractTnUiHelper.getInstance()).setFontDensity(false);
    }

    
    class RecentListAdapter extends TnListAdapter
    {
        private Vector addresses;

        private SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d", Locale.US);

        private final int[] ITEM_VIEW_TYPES = new int[]
        { 0, 1, 2 };

        public RecentListAdapter(Context context, Vector addresses)
        {
            super(context);
            this.addresses = addresses;
        }

        public void setAddresses(Vector addresses)
        {
            this.addresses = addresses;
        }

        public int getCount()
        {
            int count = addresses.size();
            return count == 0 ? 1 : count;
        }

        public Object getItem(int position)
        {
            return null;
        }

        public long getItemId(int position)
        {
            return position;
        }

        protected View getItemView(int position, View convertView, ViewGroup parent)
        {
            if (addresses.size() == 0)
            {
                String searchText = RecentViewTouch.this.model.getString(KEY_S_SEARCH_TEXT);
                if (searchText != null && searchText.trim().length() > 0)
                {
                    convertView = mInflater.inflate(R.layout.common_no_result, null);
                    convertView.setPadding(0,
                        ((RecentUiDecorator) RecentViewTouch.this.uiDecorator).RECENT_EMPTY_PADDING_TOP.getInt() / 2, 0, 0);
                    
                    Context context = AndroidPersistentContext.getInstance().getContext();
                    TextView secondLine = (TextView) convertView.findViewById(R.id.commonNoResultSecondLine);
                    String secondLineText = context.getString(R.string.commonNoResultsSecondLine, searchText);
                    secondLine.setText(secondLineText);
                    return convertView;
                }
                else
                {
                    convertView = mInflater.inflate(R.layout.recent_empty, null);
                    convertView.setPadding(0,
                        ((RecentUiDecorator) RecentViewTouch.this.uiDecorator).RECENT_EMPTY_PADDING_TOP.getInt(), 0, 0);
                    return convertView;
                }
             }
            int type = 0;
            Address address = (Address) addresses.elementAt(position);
            if (address != null)
            {
                type = address.getType();
            }
            PlaceListItem convertItem = null;
            if (convertView == null)
            {
                convertView = mInflater.inflate(type == Address.TYPE_GROUP ? R.layout.place_list0groupitem
                        : R.layout.place_list0normalitem, null);
                convertItem = (PlaceListItem) convertView;
                convertItem.init(type == Address.TYPE_GROUP ? PlaceListItem.TYPE_GROUP : PlaceListItem.TYPE_NORMAL);
            }
            else
            {
                convertItem = (PlaceListItem) convertView;
            }
            if (address == null)
            {
                convertView.setVisibility(View.GONE);
                return convertView;
            }

            if (type == Address.TYPE_GROUP)
            {
                convertItem.setBrandName(address.getLabel());
            }
            else
            {
                initRecentListItem(convertItem, address);

                //Fix bug TNANDROID-5306
                if (model.getInt(KEY_I_AC_TYPE) == TYPE_AC_FROM_ONE_BOX)
                {
                    View itemView = convertItem.findViewById(R.id.placeList0NormalItem);
                    itemView.setTag(position);
                    itemView.setOnClickListener(RecentViewTouch.this);
                }
            }
            return convertView;
        }

        private void initRecentListItem(PlaceListItem convertItem, Address address)
        {
            TnMenu contextMenu;
            String sHome = ResourceManager.getInstance().getCurrentBundle()
                    .getString(IStringAc.RES_BTTN_HOME, IStringAc.FAMILY_AC);
            String sWork = ResourceManager.getInstance().getCurrentBundle()
                    .getString(IStringAc.RES_BTN_WORK, IStringAc.FAMILY_AC);
            if (address.getPoi() != null)
            {
                contextMenu = createListItemPoiContextMenu(address.getPoi());
            }
            else
            {
                contextMenu = createListItemAddressContextMenu();
            }
            if (AddressListFilter.isHome(address))
            {
                convertItem.setBrandName(sHome);
                convertItem.setAddress(ResourceManager.getInstance().getStringConverter()
                        .convertAddress(address.getStop(), false));
                contextMenu.setHeaderTitle(sHome);
            }
            else if (AddressListFilter.isWork(address))
            {
                convertItem.setBrandName(sWork);
                convertItem.setAddress(ResourceManager.getInstance().getStringConverter()
                        .convertAddress(address.getStop(), false));
                contextMenu.setHeaderTitle(sWork);
            }
            else if (address.getLabel() != null && !"".equals(address.getLabel().trim()))
            {
                convertItem.setBrandName(address.getLabel());
                convertItem.setAddress(ResourceManager.getInstance().getStringConverter()
                        .convertAddress(address.getStop(), false));
                contextMenu.setHeaderTitle(address.getLabel());
            }
            else
            {
                convertItem.setBrandName(ResourceManager.getInstance().getStringConverter()
                        .convertAddress(address.getStop(), false));
                contextMenu.setHeaderTitle(address.getDisplayedText());
            }

            String category = ResourceManager.getInstance().getCurrentBundle()
                    .getString(IStringFavorite.RES_CATEGORY_OTHERS, IStringFavorite.FAMILY_FAVORITE);
            if (address.getEventId() > 0)
            {
                category = ResourceManager.getInstance().getCurrentBundle()
                        .getString(IStringFavorite.RES_CATEGORY_EVENT, IStringFavorite.FAMILY_FAVORITE);
            }
            else if (address.getPoi() != null && address.getPoi().getBizPoi() != null
                    && address.getPoi().getBizPoi().getCategoryName() != null
                    && address.getPoi().getBizPoi().getCategoryName().trim().length() > 0)
            {
                category = address.getPoi().getBizPoi().getCategoryName();
            }
            convertItem.setLastLine(category);
            convertItem.setDistance(dateFormat.format(new Date(address.getUpdateTime())));

            convertItem.setTnMenu(contextMenu);
        }

        private Stop getAnchor()
        {
            Stop anchor = null;
            // 1. Get precisely gps fix
            TnLocation location = LocationProvider.getInstance().getCurrentLocation(LocationProvider.TYPE_GPS);
            // 2. Get network fix
            if (location == null)
            {
                location = LocationProvider.getInstance().getCurrentLocation(LocationProvider.TYPE_NETWORK);
            }

            if (location == null)
            {
                // 3. Get last know gps fix
                location = LocationProvider.getInstance().getLastKnownLocation(LocationProvider.TYPE_GPS);
                // 4. Get last know network fix
                if (location == null)
                {
                    location = LocationProvider.getInstance().getLastKnownLocation(LocationProvider.TYPE_NETWORK);
                }
                // check last know
                if (location != null)
                {
                    anchor = new Stop();
                    anchor.setType(Stop.STOP_CURRENT_LOCATION);
                    anchor.setLat(location.getLatitude());
                    anchor.setLon(location.getLongitude());
                }
            }
            else
            {
                anchor = new Stop();
                anchor.setType(Stop.STOP_CURRENT_LOCATION);
                anchor.setLat(location.getLatitude());
                anchor.setLon(location.getLongitude());
            }

            return anchor;
        }

        protected View getItemViewBeforeSelected(int position, View convertView, ViewGroup parent)
        {
            if (isNeedMiniBar())
            {
                return convertView.findViewById(R.id.placeList0NormalItem);
            }
            else
            {
                return null;
            }
        }

        protected View getItemViewAfterSelected(int position, View convertView, ViewGroup parent)
        {
            if (isNeedMiniBar())
            {
                View normalSelectedItem = convertView.findViewById(R.id.placeList0NormalSelectedItem);
                if (normalSelectedItem != null)
                {
                    View miniPoiBarDrive = normalSelectedItem.findViewById(R.id.commonMiniPoiBar0DriveView);
                    View miniPoiBarInfo = normalSelectedItem.findViewById(R.id.commonMiniPoiBar0InfoView);
                    View miniPoiBarMap = normalSelectedItem.findViewById(R.id.commonMiniPoiBar0MapView);
                    miniPoiBarDrive.setTag(position);
                    miniPoiBarDrive.setOnClickListener(RecentViewTouch.this);
                    miniPoiBarInfo.setTag(position);
                    miniPoiBarInfo.setOnClickListener(RecentViewTouch.this);
                    miniPoiBarMap.setTag(position);
                    miniPoiBarMap.setOnClickListener(RecentViewTouch.this);
                    
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
            else
            {
                return null;
            }
        }

        public int getItemViewType(int position)
        {
            int type = ITEM_VIEW_TYPES[2];
            if (addresses.size() > 0)
            {
                Address address = (Address) addresses.get(position);
                type = (address.getType() == Address.TYPE_GROUP ? ITEM_VIEW_TYPES[0] : ITEM_VIEW_TYPES[1]);
            }
            return type;
        }

        public int getViewTypeCount()
        {
            return ITEM_VIEW_TYPES.length;
        }

    }

    public void onRefresh()
    {
        if (!MigrationExecutor.getInstance().isSyncEnabled())
        {
            recentList.stopRefresh();
            this.model.remove(KEY_I_INDEX);
        }
        AndroidUiEventHandler.onClick(loginContextMenu);
        TnMenu menu = loginContextMenu.getMenu(AbstractTnComponent.TYPE_CLICK);
        if(menu != null)
        {
            if(menu.size() == 1) //SYNC
            {
                KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_RECENTS,
                    KontagentLogger.RECENTS_DRAG_TO_SYNC);
            }
            else if(menu.size() > 1) //Keep it for future differ
            {
                KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_RECENTS,
                    KontagentLogger.RECENTS_DRAG_TO_SYNC);
            }
        }
    }

    protected boolean isNeedMiniBar()
    {
        boolean isNeedMiniBar = true;

        if (model.getInt(KEY_I_AC_TYPE) == TYPE_AC_FROM_ONE_BOX || (!NetworkStatusManager.getInstance().isConnected()
                && !MapDownloadStatusManager.getInstance().isOnBoardDataAvailable()))
        {
            isNeedMiniBar = false;
        }

        return isNeedMiniBar;
    }

    public void onLoadMore()
    {
    }

    @Override
    public void onClick(View v)
    {
        Vector result = model.getVector(KEY_V_CURRENT_RECENT_ADDRESSES);
        int index = (Integer) v.getTag();
        Address address = (Address) result.get(index);
        if (v.getId() == R.id.commonMiniPoiBar0DriveView && index > -1)
        {
            model.put(KEY_O_SELECTED_ADDRESS, address);
            model.put(KEY_O_CURRENT_ADDRESS, address);
            model.put(KEY_B_KEEP_MINI_BAR, true);
            AndroidCitizenUiHelper.triggerCommand(this, v, CMD_DRIVETO_ADDRESS);
        }
        else if (v.getId() == R.id.commonMiniPoiBar0InfoView && index > -1)
        {
            model.put(KEY_O_SELECTED_ADDRESS, address);
            model.put(KEY_O_CURRENT_ADDRESS, address);
            model.put(KEY_B_KEEP_MINI_BAR, true);
            AndroidCitizenUiHelper.triggerCommand(this, v, CMD_SELECT_ADDRESS);
        }
        else if (v.getId() == R.id.commonMiniPoiBar0MapView && index > -1)
        {
            model.put(KEY_O_SELECTED_ADDRESS, address);
            model.put(KEY_O_CURRENT_ADDRESS, address);
            model.put(KEY_B_KEEP_MINI_BAR, true);
            AndroidCitizenUiHelper.triggerCommand(this, v, CMD_MAP_ADDRESS);
        }
        else if (v.getId() == R.id.placeList0NormalItem && index > -1)
        {
            model.put(KEY_O_SELECTED_ADDRESS, address);
            model.put(KEY_O_CURRENT_ADDRESS, address);
            model.put(KEY_B_KEEP_MINI_BAR, true);
            AndroidCitizenUiHelper.triggerCommand(this, v, CMD_SELECT_ADDRESS);
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
                    KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_RECENTS,
                        KontagentLogger.RECENTS_MAP_CLICKED);
                    break;
                }
                case CMD_DRIVETO_ADDRESS:
                {
                    KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_RECENTS,
                        KontagentLogger.RECENTS_DRIVE_CLICKED);
                    break;
                }
                case CMD_SELECT_ADDRESS:
                {
                    View v = (View)tnUiEvent.getComponent().getNativeUiComponent();
                    if(v != null)
                    {
                        if(v.getId() == R.id.commonMiniPoiBar0InfoView)
                        {
                            KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_RECENTS,
                                KontagentLogger.RECENTS_INFO_CLICKED);
                        }
                    }
                    break;
                }
            }
        }
//        else if(tnUiEvent != null && tnUiEvent.getType() == TnUiEvent.TYPE_KEY_EVENT)
//        {
//            if (tnUiEvent.getKeyEvent()!= null && tnUiEvent.getKeyEvent().getAction() == TnKeyEvent.ACTION_DOWN
//                    && tnUiEvent.getKeyEvent().getCode() == TnKeyEvent.KEYCODE_BACK)
//            {
//                KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_RECENTS, KontagentLogger.RECENTS_BACK_CLICKED);
//            }
//        }
    }
    
    @Override
    public void OnTnItemSelectedChange(int oldItemIndex, int newItemIndex)
    {
        model.put(KEY_I_INDEX, newItemIndex);
        if(newItemIndex >= 0)
        {
            Vector results = model.getVector(KEY_V_CURRENT_RECENT_ADDRESSES);
            if(newItemIndex < results.size())
            {
                Object obj = results.get(newItemIndex);
                if(obj instanceof Address)
                {
                    Address addr = (Address)obj;
                    if(addr.getPoi() == null && addr.getEventId() <= 0)
                    {
                        KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_RECENTS, KontagentLogger.RECENTS_ADDRESS_CLICKED);
                    }
                    else
                    {
                        if(addr.getEventId() > 0)
                        {
                            KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_RECENTS, KontagentLogger.RECENTS_EVENT_CLICKED);
                        }
                        else
                        {
                            KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_RECENTS, KontagentLogger.RECENTS_POI_CLICKED);
                        }
                    }
                }
            }
        }
    }
}
