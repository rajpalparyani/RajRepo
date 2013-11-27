/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * AddFavoriteViewTouch.java
 *
 */
package com.telenav.module.ac.favorite.favoriteeditor;

import java.util.Vector;

import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.dao.serverproxy.AddressDao;
import com.telenav.data.datatypes.address.Address;
import com.telenav.data.datatypes.address.FavoriteCatalog;
import com.telenav.data.datatypes.address.Stop;
import com.telenav.module.AppConfigHelper;
import com.telenav.mvc.AbstractCommonUiDecorator;
import com.telenav.mvc.AbstractCommonView;
import com.telenav.res.INinePatchImageRes;
import com.telenav.res.IStringCommon;
import com.telenav.res.IStringEditCategory;
import com.telenav.res.IStringEditFavorite;
import com.telenav.res.IStringFavorite;
import com.telenav.res.ResourceManager;
import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.core.AbstractTnContainer;
import com.telenav.tnui.core.AbstractTnUiHelper;
import com.telenav.tnui.core.ITnUiEventListener;
import com.telenav.tnui.core.TnKeyEvent;
import com.telenav.tnui.core.TnMenu;
import com.telenav.tnui.core.TnScreen;
import com.telenav.tnui.core.TnUiArgs;
import com.telenav.tnui.core.TnUiArgs.ITnUiArgsDecorator;
import com.telenav.tnui.core.TnUiArgs.TnUiArgAdapter;
import com.telenav.tnui.core.TnUiEvent;
import com.telenav.tnui.graphics.AbstractTnGraphics;
import com.telenav.tnui.graphics.AbstractTnImage;
import com.telenav.tnui.widget.TnLinearContainer;
import com.telenav.tnui.widget.TnPopupContainer;
import com.telenav.tnui.widget.TnScrollPanel;
import com.telenav.tnui.widget.TnTextField;
import com.telenav.ui.ImageDecorator;
import com.telenav.ui.NinePatchImageDecorator;
import com.telenav.ui.UiFactory;
import com.telenav.ui.UiStyleManager;
import com.telenav.ui.citizen.CitizenAddressListItem;
import com.telenav.ui.citizen.CitizenCheckBox;
import com.telenav.ui.citizen.CitizenCheckItem;
import com.telenav.ui.citizen.CitizenScreen;
import com.telenav.ui.citizen.CitizenTextField;
import com.telenav.ui.frogui.text.FrogTextHelper;
import com.telenav.ui.frogui.widget.FrogButton;
import com.telenav.ui.frogui.widget.FrogDropDownField;
import com.telenav.ui.frogui.widget.FrogImageComponent;
import com.telenav.ui.frogui.widget.FrogLabel;
import com.telenav.ui.frogui.widget.FrogMessageBox;
import com.telenav.ui.frogui.widget.FrogMultiLine;
import com.telenav.ui.frogui.widget.FrogNullField;
import com.telenav.ui.frogui.widget.FrogTextField;
import com.telenav.util.PrimitiveTypeCache;

/**
 * @author wzhu (wzhu@telenav.cn)
 * @date 2010-10-28
 */
class FavoriteEditorViewTouch extends AbstractCommonView implements IFavoriteEditorConstants
{
    protected int lastOrientation = 0;
    
    public FavoriteEditorViewTouch(AbstractCommonUiDecorator uiDecorator)
    {
        super(uiDecorator);
    }


    protected int transformCommandDelegate(int state, TnUiEvent tnUiEvent)
    {
        int cmd = CMD_NONE;
        switch (state)
        {
            case STATE_MAIN:
            {
                if (tnUiEvent.getType() == TnUiEvent.TYPE_KEY_EVENT && tnUiEvent.getKeyEvent().getAction() == TnKeyEvent.ACTION_UP
                        && tnUiEvent.getKeyEvent().getCode() == TnKeyEvent.KEYCODE_ENTER)
                {
                    if (tnUiEvent.getComponent() instanceof FrogTextField)
                    {
                        return CMD_UPDATE_FAVORITE;
                    }
                }
                break;
            }
        }
        return cmd;
    }

    protected boolean preProcessUIEvent(TnUiEvent tnUiEvent)
    {
        return super.preProcessUIEvent(tnUiEvent);
    }

    protected TnPopupContainer createPopup(int state)
    {
        TnPopupContainer popup = null;
        switch (state)
        {
            case STATE_DELETE_FAVORITE:
            {
                popup = createDeleteFavoritePopup(state);
                break;
            }
            case STATE_FAVORITE_UPDATED:
            {
                String notificiation;
                notificiation = FrogTextHelper.WHITE_COLOR_START
                        + ResourceManager.getInstance().getCurrentBundle().getString(IStringEditFavorite.RES_LABEL_FAVORITE_UPDATED, IStringEditFavorite.FAMILY_EDIT_FAVORITE)
                        + FrogTextHelper.WHITE_COLOR_END;
                popup = createNotifactionBox(state, notificiation, 1);
                break;
            }
            case STATE_FAVORITE_DELETED:
            {
                String notificiation = FrogTextHelper.WHITE_COLOR_START
                        + ResourceManager.getInstance().getCurrentBundle()
                                .getString(IStringEditFavorite.RES_LABEL_FAVORITE_DELETED, IStringEditFavorite.FAMILY_EDIT_FAVORITE)
                        + FrogTextHelper.WHITE_COLOR_END;
                popup = createNotifactionBox(state, notificiation, 1);
                break;
            }
        }
        return popup;
    }

    private TnPopupContainer createNotifactionBox(int state, String notification, int timeout)
    {
        FrogMessageBox notificationBox = UiFactory.getInstance().createNotificationMessageBox(state, notification, timeout);
        notificationBox.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.SEMI_TRANSPARENT_BG_UNFOCUSED);
        notificationBox.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, NinePatchImageDecorator.SEMI_TRANSPARENT_BG_UNFOCUSED);
        notificationBox.setCommandEventListener(this);
        return notificationBox;
    }

    //TODO Casper to remove
    private TnPopupContainer createAddFavoritePopup(int state)
    {
        TnPopupContainer popup = UiFactory.getInstance().createPopupContainer(state);
        TnUiArgs popupArgs = popup.getTnUiArgs();
        popupArgs.put(TnUiArgs.KEY_POSITION_Y, ((FavoriteEditorUiDecorator) this.uiDecorator).POPUP_POSITION_Y);        
        
        TnLinearContainer panelContainer = UiFactory.getInstance().createLinearContainer(0, true, AbstractTnGraphics.VCENTER | AbstractTnGraphics.HCENTER);
        panelContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT,((FavoriteEditorUiDecorator) this.uiDecorator).PANEL_CONTAINER_HEIGHT);
        
        TnScrollPanel panel = UiFactory.getInstance().createScrollPanel(0, true);
        panelContainer.add(panel);
        
        TnLinearContainer container = UiFactory.getInstance().createLinearContainer(0, true);
        container.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((FavoriteEditorUiDecorator) this.uiDecorator).POPUP_WIDTH);
        panel.set(container);
        
        TnLinearContainer topContainer = UiFactory.getInstance().createLinearContainer(0, true,
            AbstractTnGraphics.VCENTER | AbstractTnGraphics.HCENTER);
        int max = AppConfigHelper.getMaxDisplaySize();
        int left = 20 * max / 480;
        int right = 20 * max / 480;
        int top = 10 * max / 480;
        int bottom = 10 * max / 480;
        topContainer.setPadding(left, top, right, bottom);
        topContainer.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.MESSAGE_BOX_TOP_BG);
        topContainer.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, NinePatchImageDecorator.MESSAGE_BOX_TOP_BG);
        Address address = (Address) model.get(KEY_O_SELECTED_ADDRESS);

        FrogLabel title = UiFactory.getInstance().createLabel(
            0,
            FrogTextHelper.BOLD_START
                    + ResourceManager.getInstance().getCurrentBundle()
                            .getString(IStringFavorite.RES_LABEL_NEW_FAVORITE, IStringFavorite.FAMILY_FAVORITE) + FrogTextHelper.BOLD_END);
        topContainer.add(title);
        title.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((FavoriteEditorUiDecorator) this.uiDecorator).POPUP_TITLE_HEIGHT);
        title.setForegroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_DA_GR), UiStyleManager.getInstance()
                .getColor(UiStyleManager.TEXT_COLOR_DA_GR));
        title.setBoldFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_POPUP_TITLE));

        String initText = "";
        if (model.getString(KEY_S_FAV_NAME) != null)
        {
            initText = model.getString(KEY_S_FAV_NAME);
        }
        else
        {
            initText = address.getDisplayedText();
        }
        
        CitizenTextField addFavoriteTextField = UiFactory.getInstance()
                .createCitizenTextField(
                    initText,
                    50,
                    TnTextField.ANY,
                    ResourceManager.getInstance().getCurrentBundle()
                            .getString(IStringFavorite.RES_LABEL_OPTIONAL, IStringFavorite.FAMILY_FAVORITE), ID_NEW_FAVORITE_TEXTFIELD,
                    ImageDecorator.IMG_AC_BACKSPACE.getImage());
        addFavoriteTextField.setBackgroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_WH));
        addFavoriteTextField.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT,
            ((FavoriteEditorUiDecorator) this.uiDecorator).TEXTFIELD_EDIT_FAVORITE_HEIGHT);
        addFavoriteTextField.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((FavoriteEditorUiDecorator) this.uiDecorator).TEXTFIELD_EDIT_FAVORITE_WIDTH);
        addFavoriteTextField.setHackVirtualKeyBoard(true);
        topContainer.add(addFavoriteTextField);
        
        FrogNullField nullfield = UiFactory.getInstance().createNullField(0);
        nullfield.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((FavoriteEditorUiDecorator) uiDecorator).NULLFIELD_HEIGHT);
        topContainer.add(nullfield);

        CitizenAddressListItem item = UiFactory.getInstance().createCitizenAddressListItem(ID_ADD_FAVORITE_ITEM);
        item.setGap(5, 0);
        item.setForegroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_ME_GR), UiStyleManager.getInstance()
                .getColor(UiStyleManager.TEXT_COLOR_ME_GR));
        item.setBoldFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_LINK_TRI_LINE_FIRST));
        item.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_LINK_TRI_LINE_SECOND));
        setItemInfo(address, item);
        item.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_LIST_DUAL_LINE_BOTTOM));
        if (isComponentNeeded(INinePatchImageRes.FAVORITE_PURE_WHITE_BG  + INinePatchImageRes.ID_UNFOCUSED))
        {
            item.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.FAVORITE_PURE_WHITE_BG);
            item.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, NinePatchImageDecorator.FAVORITE_PURE_WHITE_BG);
        }
        else
        {
            item.getTnUiArgs().remove(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS);
            item.getTnUiArgs().remove(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS);
        }
        item.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((FavoriteEditorUiDecorator) uiDecorator).PURE_WHITE_BG_HEIGHT);
        topContainer.add(item);
        
        FrogNullField nullfield2 = UiFactory.getInstance().createNullField(0);
        nullfield2.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((FavoriteEditorUiDecorator) uiDecorator).NULLFIELD_HEIGHT);
        topContainer.add(nullfield2);

        int topPadding = ((FavoriteEditorUiDecorator) uiDecorator).BUTTON_PADDING.getInt();
        int bottomPadding = ((FavoriteEditorUiDecorator) uiDecorator).BUTTON_PADDING.getInt() + 10;
        TnLinearContainer buttonContainer = UiFactory.getInstance().createLinearContainer(0, false,
            AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);
        buttonContainer.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, NinePatchImageDecorator.MESSAGE_BOX_BOTTOM_BG);
        buttonContainer.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.MESSAGE_BOX_BOTTOM_BG);
        buttonContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT,
            ((FavoriteEditorUiDecorator) this.uiDecorator).BUTTON_CONTAINER_HEIGHT);
        buttonContainer.setPadding(0, topPadding, 0, bottomPadding);

        FrogButton addButton = UiFactory.getInstance().createButton(ID_ADD_BUTTON,
            ResourceManager.getInstance().getCurrentBundle().getString(IStringFavorite.RES_BTN_ADD, IStringFavorite.FAMILY_FAVORITE));
        addButton.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((FavoriteEditorUiDecorator) uiDecorator).BUTTON_EDIT_CATEGORY_WIDTH);
        addButton.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((FavoriteEditorUiDecorator) uiDecorator).BUTON_HEIGHT);
        TnMenu menu = UiFactory.getInstance().createMenu();
        menu.add("", 1/*CMD_ADD_FAVORITE*/);
        addButton.setMenu(menu, AbstractTnComponent.TYPE_CLICK);
        addButton.setCommandEventListener(this);

        FrogNullField nullField1 = UiFactory.getInstance().createNullField(0);
        nullField1.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((FavoriteEditorUiDecorator) uiDecorator).NULLFIELD_EDIT_CATEGORY_WIDTH);

        FrogButton cancelButton = UiFactory.getInstance().createButton(
            0,
            ResourceManager.getInstance().getCurrentBundle()
                    .getString(IStringEditCategory.RES_BTTN_CANCEL, IStringEditCategory.FAMILY_EDIT_CATEGORY));
        cancelButton.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((FavoriteEditorUiDecorator) uiDecorator).BUTTON_EDIT_CATEGORY_WIDTH);
        cancelButton.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((FavoriteEditorUiDecorator) uiDecorator).BUTON_HEIGHT);
        menu = UiFactory.getInstance().createMenu();
        menu.add("", CMD_COMMON_CANCEL);
        cancelButton.setMenu(menu, AbstractTnComponent.TYPE_CLICK);
        cancelButton.setCommandEventListener(this);
        buttonContainer.add(addButton);
        buttonContainer.add(nullField1);
        buttonContainer.add(cancelButton);

        container.add(topContainer);
        container.add(buttonContainer);
        popup.setContent(panelContainer);
        popup.setSizeChangeListener(this);
        return popup;
    }

    protected String convertSecondLine(Stop address)
    {
        if(address == null)
            return null;
        StringBuffer buffer = new StringBuffer();
        if(address.getCity() != null && !"".equals(address.getCity()))
        {
            buffer.append(address.getCity());
        }
        if(address.getProvince() != null &&!"".equals(address.getProvince()))
        {
            if(buffer.length() > 0)
            {
                buffer.append(", ");
            }
            buffer.append(address.getProvince());
        }
        return buffer.toString();
      }
    
    private void setItemInfo(Address address, CitizenAddressListItem item)
    {
        if(address.getStop() == null)
        {
            return;
        }
        
        if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
        {
            if (address.getStop().getType() != Stop.STOP_CURRENT_LOCATION)
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
                item.setShareInfoColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_ME_GR), UiStyleManager.getInstance()
                        .getColor(UiStyleManager.TEXT_COLOR_ME_GR));
            }
            else
            {
                item.setAddress(ResourceManager.getInstance().getCurrentBundle()
                        .getString(IStringEditFavorite.RES_LABEL_LAT, IStringEditFavorite.FAMILY_EDIT_FAVORITE)
                        + " " + address.getStop().getLat() / 100000.0d);
                item.setShareInfo(ResourceManager.getInstance().getCurrentBundle()
                        .getString(IStringEditFavorite.RES_LABEL_LON, IStringEditFavorite.FAMILY_EDIT_FAVORITE)
                        + " " + address.getStop().getLon() / 100000.0d);
                item.setShareInfoColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_ME_GR), UiStyleManager.getInstance()
                        .getColor(UiStyleManager.TEXT_COLOR_ME_GR));
            }
        }
        else
        {
            if (address.getStop().getType() != Stop.STOP_CURRENT_LOCATION)
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
            else
            {
                item.setAddress(ResourceManager.getInstance().getCurrentBundle()
                        .getString(IStringEditFavorite.RES_LABEL_LAT, IStringEditFavorite.FAMILY_EDIT_FAVORITE)
                        + " "
                        + address.getStop().getLat()
                        / 100000.0d
                        + " "
                        + ResourceManager.getInstance().getCurrentBundle()
                                .getString(IStringEditFavorite.RES_LABEL_LON, IStringEditFavorite.FAMILY_EDIT_FAVORITE)
                        + " "
                        + address.getStop().getLon() / 100000.0d);
                item.setShareInfo(null);
            }
        }
    }

    private boolean isShareAddress(Address destAddress)
    {
        boolean isSharedAddress = destAddress != null
                && destAddress.getCatagories() != null
                && destAddress.getCatagories().contains(
                    AddressDao.RECEIVED_ADDRESSES_CATEGORY)
                && !destAddress.isReadByUser();
        return isSharedAddress;
    }
    
    
    private TnPopupContainer createDeleteFavoritePopup(int state)
    {
        TnPopupContainer popup = UiFactory.getInstance().createPopupContainer(state);
        TnLinearContainer container = UiFactory.getInstance().createLinearContainer(0, true);
        container.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((FavoriteEditorUiDecorator) this.uiDecorator).POPUP_WIDTH);

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
                    .getString(IStringEditFavorite.RES_LABEL_DELETE_FAVORITE_TITLE, IStringEditFavorite.FAMILY_EDIT_FAVORITE));
        title.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((FavoriteEditorUiDecorator) uiDecorator).MULTILINE_WIDTH);
        title.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((FavoriteEditorUiDecorator) uiDecorator).MULTILINE_HEIGHT);
        title.setTextAlign(FrogMultiLine.TEXT_ALIGN_CENTER);
        title.setForegroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_ME_GR));
        title.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_NAV_INFO_BAR));
        topContainer.add(title);

        FrogNullField nullfield = UiFactory.getInstance().createNullField(0);
        nullfield.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((FavoriteEditorUiDecorator) uiDecorator).NULLFIELD_HEIGHT);
        topContainer.add(nullfield);

        CitizenAddressListItem item = UiFactory.getInstance().createCitizenAddressListItem(ID_DELETE_FAVORITE_ITEM);
        item.setPadding(item.getLeftPadding(), 2 * item.getTopPadding(), item.getRightPadding(), 2 * item.getBottomPadding());
        item.setTitleColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_BLUE),
            UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_BLUE));
        item.setGap(5, 0);
        item.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((FavoriteEditorUiDecorator) this.uiDecorator).PURE_WHITE_BG_HEIGHT);
        if (isShareAddress(address))
        {
            item.getTnUiArgs().put(CitizenAddressListItem.KEY_LEFT_ICON_UNFOCUS,
                ImageDecorator.IMG_LIST_RECEIVED_ICON_UNFOCUSED);
            item.getTnUiArgs().put(CitizenAddressListItem.KEY_LEFT_ICON_FOCUS,
                ImageDecorator.IMG_LIST_RECEIVED_ICON_FOCUSED);
        }
        else
        {
            item.getTnUiArgs().put(CitizenAddressListItem.KEY_LEFT_ICON_UNFOCUS,
                ImageDecorator.IMG_FAV_ICON_LIST_UNFOCUS);
            item.getTnUiArgs().put(CitizenAddressListItem.KEY_LEFT_ICON_FOCUS,
                ImageDecorator.IMG_FAV_ICON_LIST_UNFOCUS);
        }
        item.setForegroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_ME_GR), UiStyleManager.getInstance()
                .getColor(UiStyleManager.TEXT_COLOR_ME_GR));
        item.setTitle(address.getDisplayedText());
        setItemInfo(address, item);
        if (isComponentNeeded(INinePatchImageRes.FAVORITE_PURE_WHITE_BG  + INinePatchImageRes.ID_UNFOCUSED))
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

//        FrogNullField nullfield2 = UiFactory.getInstance().createNullField(0);
//        nullfield2.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((FavoriteEditorUiDecorator) uiDecorator).NULLFIELD_HEIGHT);
//        topContainer.add(nullfield2);
        
        TnLinearContainer buttonContainer = UiFactory.getInstance().createLinearContainer(0, false,
            AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);
        buttonContainer.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, NinePatchImageDecorator.MESSAGE_BOX_BOTTOM_BG);
        buttonContainer.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.MESSAGE_BOX_BOTTOM_BG);
        buttonContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT,
            ((FavoriteEditorUiDecorator) this.uiDecorator).BUTTON_CONTAINER_HEIGHT);
        buttonContainer.setPadding(0, 8, 0, 18);

        FrogButton deleteButton = UiFactory.getInstance().createButton(
            0,
            ResourceManager.getInstance().getCurrentBundle()
                    .getString(IStringEditCategory.RES_BTTN_DELETE, IStringEditCategory.FAMILY_EDIT_CATEGORY));
        deleteButton.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((FavoriteEditorUiDecorator) uiDecorator).BUTTON_EDIT_CATEGORY_WIDTH);
        deleteButton.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((FavoriteEditorUiDecorator) uiDecorator).BUTON_HEIGHT);
        TnMenu menu = UiFactory.getInstance().createMenu();
        menu.add("", CMD_COMMON_OK);
        deleteButton.setMenu(menu, AbstractTnComponent.TYPE_CLICK);
        deleteButton.setCommandEventListener(this);

        FrogNullField nullField1 = UiFactory.getInstance().createNullField(0);
        nullField1.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((FavoriteEditorUiDecorator) uiDecorator).NULLFIELD_EDIT_CATEGORY_WIDTH);

        FrogButton cancelButton = UiFactory.getInstance().createButton(
            0,
            ResourceManager.getInstance().getCurrentBundle()
                    .getString(IStringEditCategory.RES_BTTN_CANCEL, IStringEditCategory.FAMILY_EDIT_CATEGORY));
        cancelButton.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((FavoriteEditorUiDecorator) uiDecorator).BUTTON_EDIT_CATEGORY_WIDTH);
        cancelButton.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((FavoriteEditorUiDecorator) uiDecorator).BUTON_HEIGHT);
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

    public void onSizeChanged(AbstractTnComponent tnComponent, int w, int h, int oldw, int oldh)
    {
        switch (model.getState())
        {
            case STATE_DELETE_FAVORITE:
            {
                TnPopupContainer popup = this.getCurrentPopup();
                CitizenAddressListItem item = (CitizenAddressListItem) popup.getContent().getComponentById(ID_DELETE_FAVORITE_ITEM);
                Address address = (Address) model.get(KEY_O_SELECTED_ADDRESS);
                setItemInfo(address, item);
                break;
            }
            case STATE_MAIN:
            {
                if (tnComponent != null && tnComponent.getRoot() instanceof TnScreen)
                {
                    final TnScreen screen = tnComponent.getRoot();
                
                    final int orientation = ((AbstractTnUiHelper) AbstractTnUiHelper
                            .getInstance()).getOrientation();
                    if (lastOrientation != orientation)
                    {
                        ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance())
                                .runOnUiThread(new Runnable()
                                {
                                    public void run()
                                    {

                                        TnLinearContainer addressContainer = (TnLinearContainer) screen
                                                .getComponentById(ID_EDIT_FAVORITE_ADDRESS_CONTAINER);

                                        if (addressContainer != null)
                                        {
                                            AbstractTnContainer parent = (AbstractTnContainer) addressContainer.getParent();
                                            if(parent != null)
                                            {
                                                int index = parent.indexOf(addressContainer);
                                                parent.remove(index);
                                                addressContainer = createAddressContainer(screen);
                                                parent.add(addressContainer, index);
                                            }
                                        }
                                        lastOrientation = orientation;
                                    }
                                });
                    }
                }
                break;
            }
        }
        super.onSizeChanged(tnComponent, w, h, oldw, oldh);
    }

    protected boolean prepareModelData(int state, int commandId)
    {
        switch (commandId)
        {
            case CMD_UPDATE_FAVORITE:
            {
                CitizenScreen currentScreen = (CitizenScreen) ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getCurrentScreen();
                CitizenTextField textField = (CitizenTextField) currentScreen.getComponentById(ID_EDIT_FAVORITE_TEXTFIELD);
                CitizenCheckBox radioBox = (CitizenCheckBox) currentScreen.getComponentById(ID_EDIT_FAVORITE_MULTIBOX);
                
                Address address = (Address) model.get(KEY_O_SELECTED_ADDRESS);
                if(address != null)
                {
                    Vector addressCategory = address.getCatagories();
                    String oldCategory = "";
                    for(int i = 0; i < addressCategory.size(); i++)
                    {
                        String temp = (String)addressCategory.elementAt(i);
                        if(!temp.equalsIgnoreCase(AddressDao.FAVORITE_ROOT_CATEGORY))
                        {
                            oldCategory = temp;
                        }
                    }
                    model.put(KEY_S_OLD_CATEGORY, oldCategory);
                }
                
                Vector selectedCategories = new Vector();
                Vector categories = model.getVector(KEY_V_CATEGORIES);;
                int checkboxItemIndex = radioBox.getSelectedIndex();
                if (checkboxItemIndex > 0)
                {
                    FavoriteCatalog categoty = (FavoriteCatalog) categories.elementAt(checkboxItemIndex - 1);
                    String name = categoty.getName();
                    selectedCategories.addElement(name);
                }
                model.put(KEY_V_SAVED_CATEGORIES, selectedCategories);
                String newName = textField.getText();
                model.put(KEY_S_FAV_NAME, newName);
                break;
            }
        }
        return super.prepareModelData(state, commandId);
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
                return createMainScreen(state);
            }
        }
        return null;
    }

    private TnScreen createMainScreen(int state)
    {
        final CitizenScreen screen = UiFactory.getInstance().createScreen(state);
        TnMenu menu = UiFactory.getInstance().createMenu();
        menu.add("", CMD_NONE);
        screen.getContentContainer().setMenu(menu, AbstractTnComponent.TYPE_CLICK);
        screen.getContentContainer().setTouchEventListener(new CloseKeyBoardListener());

        menu = UiFactory.getInstance().createMenu();
        menu.add("", CMD_NONE);
        screen.getTitleContainer().setMenu(menu, AbstractTnComponent.TYPE_CLICK);
        screen.getTitleContainer().setTouchEventListener(new CloseKeyBoardListener());

        Address address = (Address) model.get(KEY_O_SELECTED_ADDRESS);
        TnLinearContainer titleContainer = (TnLinearContainer) screen.getTitleContainer();
        titleContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((FavoriteEditorUiDecorator) uiDecorator).TITLE_CONTAINER_HEIGHT);
        titleContainer.setBackgroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.SCREEN_TITLE_BG_COLOR));
        String label = ResourceManager.getInstance().getCurrentBundle()
                .getString(IStringEditFavorite.RES_LABEL_EDIT_FAVORITE, IStringEditFavorite.FAMILY_EDIT_FAVORITE);
        FrogLabel titleLabel = UiFactory.getInstance().createLabel(0, label);
        titleLabel.setForegroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_WH), UiStyleManager.getInstance()
                .getColor(UiStyleManager.TEXT_COLOR_WH));
        titleLabel.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, this.uiDecorator.SCREEN_WIDTH);
        titleLabel.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((FavoriteEditorUiDecorator) uiDecorator).TITLE_CONTAINER_HEIGHT);
        titleLabel.setBoldFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_SCREEN_TITLE));
        titleContainer.add(titleLabel);

        TnLinearContainer topContainer = UiFactory.getInstance().createLinearContainer(0, false,
            AbstractTnGraphics.VCENTER | AbstractTnGraphics.HCENTER);
        topContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT,
            ((FavoriteEditorUiDecorator) uiDecorator).EDIT_FAVORITE_TOP_CONTAINER_HEIGHT);
        topContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH,
            ((FavoriteEditorUiDecorator) uiDecorator).EDIT_FAVORITE_TOP_CONTAINER_WIDTH);
        
        final TnLinearContainer iconContainer = UiFactory.getInstance().createLinearContainer(0, false,
            AbstractTnGraphics.TOP);
        iconContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT,
            ((FavoriteEditorUiDecorator) uiDecorator).EDIT_FAVORITE_ICON_CONTAINER_HEIGHT);
        
        FrogNullField leftGap = UiFactory.getInstance().createNullField(0);
        leftGap.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((FavoriteEditorUiDecorator) uiDecorator).EDIT_FAVORITE_ICON_NULL_FIELD_WIDTH);
        iconContainer.add(leftGap);
        
        AbstractTnImage iconImage = ImageDecorator.IMG_FAV_EDIT_FAVORITE_PIN.getImage();
        FrogImageComponent iconComponent = UiFactory.getInstance().createFrogImageComponent(0, iconImage);
        iconContainer.add(iconComponent);
        
        FrogNullField rightGap = UiFactory.getInstance().createNullField(0);
        rightGap.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((FavoriteEditorUiDecorator) uiDecorator).EDIT_FAVORITE_ICON_NULL_FIELD_WIDTH);
        iconContainer.add(rightGap);
        topContainer.add(iconContainer);
        
        TnLinearContainer infoContainer = UiFactory.getInstance().createLinearContainer(0, true, AbstractTnGraphics.VCENTER);
        infoContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH,
            new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator(){
                public Object decorate(TnUiArgAdapter args)
                {
                    
                    int width = ((FavoriteEditorUiDecorator) uiDecorator).EDIT_FAVORITE_TOP_CONTAINER_WIDTH.getInt() 
                            - iconContainer.getPreferredWidth();
                    return PrimitiveTypeCache.valueOf(width);
                }
            }));
        CitizenTextField editFavoriteTextField = UiFactory.getInstance().createCitizenTextField(address.getDisplayedText(), 100, TnTextField.ANY,
            "", ID_EDIT_FAVORITE_TEXTFIELD, ImageDecorator.IMG_AC_BACKSPACE.getImage());
        editFavoriteTextField.setTextColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_ME_GR));
        editFavoriteTextField.setBackgroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_WH));
        editFavoriteTextField.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT,
            ((FavoriteEditorUiDecorator) this.uiDecorator).TEXTFIELD_EDIT_FAVORITE_HEIGHT);
        editFavoriteTextField.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH,
            new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator(){
                public Object decorate(TnUiArgAdapter args)
                {
                    int width = ((FavoriteEditorUiDecorator) uiDecorator).EDIT_FAVORITE_TOP_CONTAINER_WIDTH
                            .getInt()
                            - iconContainer.getPreferredWidth()
                            - ((FavoriteEditorUiDecorator) uiDecorator).EDIT_FAVORITE_ICON_NULL_FIELD_WIDTH
                                    .getInt();
                    return PrimitiveTypeCache.valueOf(width);
                }
            }));
        infoContainer.add(editFavoriteTextField);
        
        TnLinearContainer addressContainer = createAddressContainer(screen);
        
        infoContainer.add(addressContainer);
        topContainer.add(infoContainer);

        TnLinearContainer contentContainer = UiFactory.getInstance().createLinearContainer(0, true, AbstractTnGraphics.HCENTER);
        contentContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT,
            new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator(){
                public Object decorate(TnUiArgAdapter args)
                {
                    int height = AppConfigHelper.getDisplayHeight() - ((FavoriteEditorUiDecorator) uiDecorator).TITLE_CONTAINER_HEIGHT.getInt()
                            - ((FavoriteEditorUiDecorator) uiDecorator).BOTTOMCONTAINER_EDIT_FAVORITE_HEIGHT.getInt();
                    return PrimitiveTypeCache.valueOf(height);
                }
            }));
        contentContainer.setBackgroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_WH));

        contentContainer.add(topContainer);

        FrogLabel subTitle = UiFactory.getInstance().createLabel(
            0, FrogTextHelper.BOLD_START + 
            ResourceManager.getInstance().getCurrentBundle()
                    .getString(IStringEditFavorite.RES_LABEL_CATEGOTIZE_FAVORITE, IStringEditFavorite.FAMILY_EDIT_FAVORITE) + FrogTextHelper.BOLD_END);
        subTitle.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, uiDecorator.SCREEN_WIDTH);
        subTitle.getTnUiArgs()
                .put(TnUiArgs.KEY_PREFER_HEIGHT, ((FavoriteEditorUiDecorator) this.uiDecorator).EDIT_FAVORITE_SUBTITLE_HEIGHT);
        subTitle.setBackgroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.EDIT_FAV_SUB_TITLE_COLOR));
        subTitle.getTnUiArgs().put(TnUiArgs.KEY_BOTTOM_SHADOW_IMAGE, ImageDecorator.IMG_SHADOW_BOTTOM_UNFOCUSED);
        subTitle.setBoldFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_SCREEN_TITLE));
        subTitle.setForegroundColor(UiStyleManager.getInstance().getColor(
            UiStyleManager.EDIT_FAV_SUB_TITLE_TEXT_COLOR), UiStyleManager.getInstance()
                .getColor(UiStyleManager.EDIT_FAV_SUB_TITLE_TEXT_COLOR));
        contentContainer.add(subTitle);

        CitizenAddressListItem item = UiFactory.getInstance().createCitizenAddressListItem(0);
        item.setTitleColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_WH),
            UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_ME_GR));
        item.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_FAVORITE_ADDRESS_ITEM));
        item.setGap(8, 0);
        item.setTitle(ResourceManager.getInstance().getCurrentBundle()
                .getString(IStringEditFavorite.RES_LABEL_NEW_CATEGORY, IStringEditFavorite.FAMILY_EDIT_FAVORITE));
        item.getTnUiArgs().put(CitizenAddressListItem.KEY_LEFT_ICON_FOCUS, ImageDecorator.IMG_LIST_CREATE_CATEGORY_ICON_FOCUSED);
        item.getTnUiArgs().put(CitizenAddressListItem.KEY_LEFT_ICON_UNFOCUS, ImageDecorator.IMG_LIST_CREATE_CATEGORY_ICON_UNFOCUSED);
        item.getTnUiArgs().put(CitizenAddressListItem.KEY_RIGHT_BTN_ICON_FOCUS, ImageDecorator.IMG_AC_ICON_ARROW_FOCUSED);
        item.getTnUiArgs().put(CitizenAddressListItem.KEY_RIGHT_BTN_ICON_UNFOCUS, ImageDecorator.IMG_AC_ICON_ARROW_UNFOCUS);
        menu = UiFactory.getInstance().createMenu();
        menu.add("", CMD_NEW_CATEGORY);
        item.setMenu(menu, AbstractTnComponent.TYPE_CLICK);
        item.setCommandEventListener(this);
        item.setForegroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_WH),
            UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_ME_GR));
        item.setBoldFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_LIST_SINGLE));
        //TODO should do it in a common way, wzhu
        item.setPadding(item.getLeftPadding() + 5, item.getTopPadding(), item.getRightPadding() + 10, item.getBottomPadding());

        TnLinearContainer boxContainer = UiFactory.getInstance().createLinearContainer(0, true, AbstractTnGraphics.HCENTER);
        boxContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((FavoriteEditorUiDecorator) this.uiDecorator).SCREEN_WIDTH);
        boxContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT,
            ((FavoriteEditorUiDecorator) this.uiDecorator).CONTAINER_EDIT_FAVORITE_HEIGHT);

        TnScrollPanel panel = UiFactory.getInstance().createScrollPanel(ID_EDIT_FAVORITE_PANEL, true);
        CitizenCheckBox radioBox = createRadioBox(address);
        
        TnLinearContainer panelContainer = UiFactory.getInstance().createLinearContainer(ID_CATEGORIZE_FAVORITE_PANEL_CONTAINER, true, AbstractTnGraphics.HCENTER);
        panelContainer.add(item);
        panelContainer.add(radioBox);
        panel.set(panelContainer);
        boxContainer.add(panel);
        contentContainer.add(boxContainer);
        TnLinearContainer bottomContainer = UiFactory.getInstance().createLinearContainer(0, false,
            AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);
        bottomContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT,
            ((FavoriteEditorUiDecorator) this.uiDecorator).BOTTOMCONTAINER_EDIT_FAVORITE_HEIGHT);
        bottomContainer.setBackgroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.SCREEN_BOTTOM_BG_COLOR));
        FrogButton deleteButton = UiFactory.getInstance().createButton(
            0,
            ResourceManager.getInstance().getCurrentBundle()
                    .getString(IStringEditFavorite.RES_BUTTON_EDIT_FAVORITE_DELETE, IStringEditFavorite.FAMILY_EDIT_FAVORITE));
        deleteButton.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH,
            ((FavoriteEditorUiDecorator) uiDecorator).BOTTOMBUTTON_EDIT_FAVORITE_WIDTH);
        menu = UiFactory.getInstance().createMenu();
        menu.add("", CMD_DELETE_FAVORITE);
        deleteButton.setMenu(menu, AbstractTnComponent.TYPE_CLICK);
        deleteButton.setCommandEventListener(this);

        FrogNullField bottomNullField = UiFactory.getInstance().createNullField(0);
        bottomNullField.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH,
            ((FavoriteEditorUiDecorator) uiDecorator).BOTTOMNULLFIELD_EDIT_FAVORITE_WIDTH);

        FrogButton doneButton = UiFactory.getInstance().createButton(
            0,
            ResourceManager.getInstance().getCurrentBundle()
                    .getString(IStringEditFavorite.RES_BUTTON_EDIT_FAVORITE_SAVE, IStringEditFavorite.FAMILY_EDIT_FAVORITE));
        doneButton.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((FavoriteEditorUiDecorator) uiDecorator).BOTTOMBUTTON_EDIT_FAVORITE_WIDTH);
        menu = UiFactory.getInstance().createMenu();
        menu.add("", CMD_UPDATE_FAVORITE);
        doneButton.setMenu(menu, AbstractTnComponent.TYPE_CLICK);
        doneButton.setCommandEventListener(this);

        bottomContainer.add(doneButton);
        bottomContainer.add(bottomNullField);
        bottomContainer.add(deleteButton);
        screen.getContentContainer().add(contentContainer);
        screen.getContentContainer().add(bottomContainer);
        return screen;
    }
    
    private TnLinearContainer createAddressContainer(TnScreen screen)
    {
        TnLinearContainer addressContainer = UiFactory.getInstance().createLinearContainer(ID_EDIT_FAVORITE_ADDRESS_CONTAINER, true, AbstractTnGraphics.VCENTER);
        Address address = (Address) model.get(KEY_O_SELECTED_ADDRESS);
        String firstLine = "";
        String secondLine = "";
        if(address != null)
        {
            if(address.getStop() != null)
            {
                firstLine = address.getStop().getFirstLine();
            }
            secondLine = ResourceManager.getInstance().getStringConverter().convertSecondLine(address.getStop());
        }
        int orientation = ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation();
        if (orientation == AbstractTnUiHelper.ORIENTATION_PORTRAIT && firstLine != null && firstLine.length() != 0)
        {
            FrogLabel streetLabel = UiFactory.getInstance().createLabel(0, firstLine);
            streetLabel.setForegroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_ME_GR), UiStyleManager.getInstance()
                    .getColor(UiStyleManager.TEXT_COLOR_ME_GR));
            streetLabel.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_TEXT_FIELD));
            streetLabel.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((FavoriteEditorUiDecorator) uiDecorator).INFO_LABEL_HEIGHT);
            FrogLabel cityLabel = UiFactory.getInstance().createLabel(0, secondLine);
            cityLabel.setForegroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_ME_GR), UiStyleManager.getInstance()
                    .getColor(UiStyleManager.TEXT_COLOR_ME_GR));
            cityLabel.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((FavoriteEditorUiDecorator) uiDecorator).INFO_LABEL_HEIGHT);
            cityLabel.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_TEXT_FIELD));
            addressContainer.add(streetLabel);
            addressContainer.add(cityLabel);
        }
        else
        {
            String fullAddress ="";
            if(firstLine != null && firstLine.length() != 0)
            {
                fullAddress = firstLine + ", " + secondLine;
            }
            else
            {
                fullAddress = secondLine;
            }
            FrogLabel stopLable = UiFactory.getInstance().createLabel(0, fullAddress);
            stopLable.setForegroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_ME_GR), UiStyleManager.getInstance()
                .getColor(UiStyleManager.TEXT_COLOR_ME_GR));
            stopLable.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((FavoriteEditorUiDecorator) uiDecorator).INFO_LABEL_HEIGHT);
            stopLable.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_TEXT_FIELD));
            
            FrogNullField nullField = UiFactory.getInstance().createNullField(0);
            nullField.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, 
                new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator(){
                    public Object decorate(TnUiArgAdapter args)
                    {
                        int height = ((FavoriteEditorUiDecorator) uiDecorator).INFO_LABEL_HEIGHT.getInt();
                        return PrimitiveTypeCache.valueOf(height / 2);
                    }
                }));
            addressContainer.add(nullField);
            addressContainer.add(stopLable);
        }
        return addressContainer;
    }

    private CitizenCheckBox createRadioBox(Address address)
    {
        Vector categories = model.getVector(KEY_V_CATEGORIES);
        CitizenCheckItem boxes[] = new CitizenCheckItem[categories.size() + 1];
        CitizenCheckItem defaultBox = UiFactory.getInstance().createCitizenCheckItem(
            0,
            ResourceManager.getInstance().getCurrentBundle()
                    .getString(IStringEditFavorite.RES_LABEL_DEFAULT_SELECTION, IStringEditFavorite.FAMILY_EDIT_FAVORITE),
            ImageDecorator.IMG_RADIO_ICON_FOCUSED.getImage(), ImageDecorator.IMG_RADIO_ICON_UNFOCUSED.getImage());
        defaultBox.setStyle(AbstractTnGraphics.RIGHT);
        defaultBox.setForegroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_WH), UiStyleManager.getInstance()
                .getColor(UiStyleManager.TEXT_COLOR_ME_GR));
        defaultBox.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_LIST_SINGLE));
        defaultBox.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, NinePatchImageDecorator.LIST_ITEM_UNFOCUS);
        defaultBox.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.LIST_ITEM_FOCUSED);
        defaultBox.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((FavoriteEditorUiDecorator) uiDecorator).SCREEN_WIDTH);
        defaultBox.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((FavoriteEditorUiDecorator) uiDecorator).CHECKBOX_EDIT_FAVORIVITE_HEIGHT);
        defaultBox.setPadding(defaultBox.getLeftPadding() + 10, defaultBox.getTopPadding(), defaultBox.getRightPadding() + 5,
            defaultBox.getBottomPadding());
        boxes[0] = defaultBox;
        boolean isSetDefault = false;
        String newCategory = model.fetchString(KEY_S_NEW_CATEGORY);
        for (int i = 0; i < categories.size(); i++)
        {
            FavoriteCatalog category = (FavoriteCatalog) categories.elementAt(i);
            String categoryName;
            if(category.isReceivedCatalog())
                categoryName=ResourceManager.getInstance() .getCurrentBundle().getString(IStringCommon.RES_RECEIVED_CATEGORY, IStringCommon.FAMILY_COMMON);
            else
               categoryName = category.getName();
            CitizenCheckItem box = UiFactory.getInstance().createCitizenCheckItem(0, categoryName,
                ImageDecorator.IMG_RADIO_ICON_FOCUSED.getImage(), ImageDecorator.IMG_RADIO_ICON_UNFOCUSED.getImage());
            box.setForegroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_WH), UiStyleManager.getInstance()
                    .getColor(UiStyleManager.TEXT_COLOR_ME_GR));
            box.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_LIST_SINGLE));
            box.setStyle(AbstractTnGraphics.RIGHT);
            box.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, NinePatchImageDecorator.LIST_ITEM_UNFOCUS);
            box.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.LIST_ITEM_FOCUSED);
            box.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((FavoriteEditorUiDecorator) uiDecorator).SCREEN_WIDTH);
            box.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((FavoriteEditorUiDecorator) uiDecorator).CHECKBOX_EDIT_FAVORIVITE_HEIGHT);
            box.setItemIcon(ImageDecorator.IMG_FAV_ICON_FOLDER_FOCUSED.getImage(), ImageDecorator.IMG_FAV_ICON_FOLDER_UNFOCUS.getImage());
            box.setPadding(box.getLeftPadding() + 5, box.getTopPadding(), box.getRightPadding() + 5, box.getBottomPadding());
            boxes[i + 1] = box;
        }
        CitizenCheckBox radioBox = UiFactory.getInstance().createCitizenCheckBox(ID_EDIT_FAVORITE_MULTIBOX, false);
        radioBox.setItems(boxes, false);
        if (newCategory != null && newCategory.trim().length() > 0)
        {
            for (int i = 0; i < categories.size(); i++)
            {
                FavoriteCatalog category = (FavoriteCatalog) categories.elementAt(i);
                String categoryName = category.getName();
                if (categoryName.equalsIgnoreCase(newCategory))
                {
                    radioBox.setSelectedIndex(i + 1);
                    isSetDefault = true;
                }
            }
        }
        else
        {
            for (int i = 0; i < categories.size(); i++)
            {
                FavoriteCatalog category = (FavoriteCatalog) categories.elementAt(i);
                String categoryName = category.getName();
                if (DaoManager.getInstance().getAddressDao().isFavoriteInCategory(address, categoryName))
                {
                    radioBox.setSelectedIndex(i + 1);
                    isSetDefault = true;
                }
            }
        }
        if (!isSetDefault)
            radioBox.setSelectedIndex(0);
        return radioBox;
    }

    protected boolean updateScreen(int state, TnScreen screen)
    {
        switch (state)
        {
            case STATE_MAIN:
            {
                String addedCategory = model.getString(KEY_S_NEW_CATEGORY);
                if (addedCategory != null)
                {
                    TnScrollPanel panel = (TnScrollPanel) getComponentById(screen, ID_EDIT_FAVORITE_PANEL);
                    Address address = (Address) model.get(KEY_O_SELECTED_ADDRESS);
                    AbstractTnContainer panelContainer = (AbstractTnContainer) getComponentById(screen, ID_CATEGORIZE_FAVORITE_PANEL_CONTAINER);
                    panelContainer.remove(1);
                    CitizenCheckBox radioBox = createRadioBox(address);
                    panelContainer.add(radioBox);
                    if (panel != null)
                        panel.set(panelContainer);
                }
                return true;
            }
        }
        return false;
    }

    protected AbstractTnComponent getComponentById(TnScreen screen, int id)
    {
        if (screen != null)
            return screen.getComponentById(id);
        return null;
    }
    
    private static class CloseKeyBoardListener implements ITnUiEventListener
    {
        
        public boolean handleUiEvent(TnUiEvent tnUiEvent)
        {
            switch (tnUiEvent.getType())
            {
                case TnUiEvent.TYPE_TOUCH_EVENT:
                {
                    if(!(tnUiEvent.getComponent() instanceof FrogDropDownField))
                    {
                        TnScreen screen = ((AbstractTnUiHelper)AbstractTnUiHelper.getInstance()).getCurrentScreen();
                        CitizenTextField editFavoriteTextField = (CitizenTextField) screen.getComponentById(ID_EDIT_FAVORITE_TEXTFIELD);
                        if (editFavoriteTextField != null)
                        {
                            editFavoriteTextField.closeVirtualKeyBoard();
                        }
                        break;
                    }
                }
            }
            return false;
        }
    }

}
