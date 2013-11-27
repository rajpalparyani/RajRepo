/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * AddFavoriteViewTouch.java
 *
 */
package com.telenav.module.ac.favorite.editcategory;

import com.telenav.app.TeleNavDelegate;
import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.datatypes.address.FavoriteCatalog;
import com.telenav.module.AppConfigHelper;
import com.telenav.mvc.AbstractCommonUiDecorator;
import com.telenav.mvc.AbstractCommonView;
import com.telenav.res.INinePatchImageRes;
import com.telenav.res.IStringEditCategory;
import com.telenav.res.ResourceManager;
import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.core.TnKeyEvent;
import com.telenav.tnui.core.TnMenu;
import com.telenav.tnui.core.TnScreen;
import com.telenav.tnui.core.TnUiArgs;
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
import com.telenav.ui.citizen.CitizenTextField;
import com.telenav.ui.frogui.text.FrogTextHelper;
import com.telenav.ui.frogui.widget.FrogButton;
import com.telenav.ui.frogui.widget.FrogLabel;
import com.telenav.ui.frogui.widget.FrogMessageBox;
import com.telenav.ui.frogui.widget.FrogMultiLine;
import com.telenav.ui.frogui.widget.FrogNullField;

/**
 * @author wzhu (wzhu@telenav.cn)
 * @date 2010-10-28
 */
class EditCategoryViewTouch extends AbstractCommonView implements IEditCategoryConstants
{

    public EditCategoryViewTouch(AbstractCommonUiDecorator uiDecorator)
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
                    if (this.getCurrentPopup() != null)
                    {
                        CitizenTextField textField = (CitizenTextField) this.getCurrentPopup().getContent()
                                .getComponentById(ID_EDIT_CATEGORY_TEXTFIELD);
                        if (textField != null)
                        {
                            model.put(KEY_S_RENAMED_CATEGORY, textField.getText());
                        }
                        return CMD_RENAME_CATEGORY;
                    }
                }
                break;
            }
            case STATE_NEW_CATEGORY:
            {
                if( tnUiEvent.getType() == TnUiEvent.TYPE_KEY_EVENT
                        &&tnUiEvent.getKeyEvent().getAction() == TnKeyEvent.ACTION_UP
                        && tnUiEvent.getKeyEvent().getCode() == TnKeyEvent.KEYCODE_ENTER)
                {
                    if(this.getCurrentPopup() != null)
                    {
                        CitizenTextField textField = (CitizenTextField)this.getCurrentPopup().getContent().getComponentById(ID_ADD_CATEGORY_TEXTFIELD);
                        if (textField != null)
                        {
                            model.put(KEY_S_NEW_CATEGROY_NAME, textField.getText());
                        }
                        return CMD_ADD_CATEGORY;
                    }
                }
                break;
            }
        }
        return cmd;
    }

    protected TnPopupContainer createPopup(int state)
    {
        switch (state)
        {
            case STATE_MAIN:
            {
                return createEditCategoryPopup(state);
            }
            case STATE_DELETE_CATEGORY:
            {
                return createDeleteCategoryMessageBox(state);
            }
            case STATE_DELETE_CATEGORY_NOTIFICATION:
            {
                return createNotifactionBox(state, FrogTextHelper.WHITE_COLOR_START + ResourceManager.getInstance().getCurrentBundle().getString(IStringEditCategory.RES_CATEGORY_DELETED_NOTIFICATION, IStringEditCategory.FAMILY_EDIT_CATEGORY) + FrogTextHelper.WHITE_COLOR_END ,1);
            }
            case STATE_UPDATE_CATEGORY:
            {
                return createNotifactionBox(state, FrogTextHelper.WHITE_COLOR_START  + ResourceManager.getInstance().getCurrentBundle().getString(IStringEditCategory.RES_CATEGORY_UPDATED_NOTIFICATION, IStringEditCategory.FAMILY_EDIT_CATEGORY) + FrogTextHelper.WHITE_COLOR_END ,1);
            }
            case STATE_NEW_CATEGORY:
            {
                return createAddCategoryPopup(state);
            }
            case STATE_ADD_CATEGORY:
            {
                return createNotifactionBox(state,FrogTextHelper.WHITE_COLOR_START + ResourceManager.getInstance().getCurrentBundle().getString(IStringEditCategory.RES_CATEGORY_ADDED_NOTIFICATION, IStringEditCategory.FAMILY_EDIT_CATEGORY) + FrogTextHelper.WHITE_COLOR_END,1);
            }
        }
        return null;
    }

    
    private TnPopupContainer createAddCategoryPopup(int state)
    {
        TnPopupContainer popup = UiFactory.getInstance().createPopupContainer(state);
        TnLinearContainer contentContainer = UiFactory.getInstance().createLinearContainer(0, true);
        contentContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((EditCategoryUiDecorator) this.uiDecorator).POPUP_WIDTH);

        TnLinearContainer topContainer = UiFactory.getInstance().createLinearContainer(0, true, AbstractTnGraphics.VCENTER | AbstractTnGraphics.HCENTER);
        int max = AppConfigHelper.getMaxDisplaySize();
        int left = 20 * max / 480;
        int right = 20 * max / 480;
        int top = 20 * max / 480;
        int bottom = 10 * max / 480;
        topContainer.setPadding(left, top, right, bottom);
        topContainer.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, NinePatchImageDecorator.MESSAGE_BOX_TOP_BG);
        topContainer.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.MESSAGE_BOX_TOP_BG);
        topContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((EditCategoryUiDecorator) this.uiDecorator).TOP_CONTAINER_HEIGHT);
        FrogLabel titleLabel = UiFactory.getInstance().createLabel(
            0,
            ResourceManager.getInstance().getCurrentBundle()
                    .getString(IStringEditCategory.RES_LABEL_NEW_CATEGORY_TITLE, IStringEditCategory.FAMILY_EDIT_CATEGORY));
        titleLabel.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((EditCategoryUiDecorator) this.uiDecorator).POPUP_TITLE_HEIGHT);
        titleLabel.setBoldFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_POPUP_TITLE));
        
        FrogNullField nullfield = UiFactory.getInstance().createNullField(0);
        nullfield.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((EditCategoryUiDecorator) this.uiDecorator).NULLFIELD_HEIGHT);
        CitizenTextField addCategoryInputField = UiFactory.getInstance().createCitizenTextField(
            "",
            50,
            TnTextField.ANY,
            ResourceManager.getInstance().getCurrentBundle()
                    .getString(IStringEditCategory.RES_DROPDOWN_NEW_CATEGORY_HINT, IStringEditCategory.FAMILY_EDIT_CATEGORY), ID_ADD_CATEGORY_TEXTFIELD, ImageDecorator.IMG_AC_BACKSPACE.getImage());
        addCategoryInputField.setBackgroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_WH));
        addCategoryInputField.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((EditCategoryUiDecorator) this.uiDecorator).TEXT_FIELD_HEIGHT);
        addCategoryInputField.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((EditCategoryUiDecorator) this.uiDecorator).TEXT_FIELD_WIDTH);
        addCategoryInputField.setHackVirtualKeyBoard(true);
        FrogButton buttonAddNew = UiFactory.getInstance().createButton(ID_ADD_CATEGORY_BUTTON,
            ResourceManager.getInstance().getCurrentBundle().getString(IStringEditCategory.RES_BTTN_ADDNEW, IStringEditCategory.FAMILY_EDIT_CATEGORY));
        buttonAddNew.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((EditCategoryUiDecorator) this.uiDecorator).BUTTON_EDIT_CATEGORY_WIDTH);
        TnMenu menu = UiFactory.getInstance().createMenu();
        menu.add("", CMD_ADD_CATEGORY);
        buttonAddNew.setMenu(menu, AbstractTnComponent.TYPE_CLICK);
        buttonAddNew.setCommandEventListener(this);
        FrogButton buttonCancel = UiFactory.getInstance().createButton(ID_ADD_CATEGORY_CANCEL_BUTTON,
            ResourceManager.getInstance().getCurrentBundle().getString(IStringEditCategory.RES_BTTN_CANCEL, IStringEditCategory.FAMILY_EDIT_CATEGORY));
        menu = UiFactory.getInstance().createMenu();
        menu.add("", CMD_COMMON_CANCEL);
        buttonCancel.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((EditCategoryUiDecorator) this.uiDecorator).BUTTON_EDIT_CATEGORY_WIDTH);
        buttonCancel.setMenu(menu, AbstractTnComponent.TYPE_CLICK);
        buttonCancel.setCommandEventListener(this);
        FrogNullField nullField = UiFactory.getInstance().createNullField(0);
        nullField.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((EditCategoryUiDecorator) this.uiDecorator).NULLFIELD_EDIT_CATEGORY_WIDTH);
        TnLinearContainer buttonContainer = UiFactory.getInstance().createLinearContainer(0, false, AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);
        buttonContainer.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, NinePatchImageDecorator.MESSAGE_BOX_BOTTOM_SINGLE_LINE_BG);
        buttonContainer.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.MESSAGE_BOX_BOTTOM_SINGLE_LINE_BG);
        buttonContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((EditCategoryUiDecorator) this.uiDecorator).BUTTON_CONTAINER_HEIGHT);
        buttonContainer.setPadding(0, 8, 0, 18);
        buttonContainer.add(buttonAddNew);
        buttonContainer.add(nullField);
        buttonContainer.add(buttonCancel);
        topContainer.add(titleLabel);
        topContainer.add(nullfield);
        topContainer.add(addCategoryInputField);
        contentContainer.add(topContainer);
        contentContainer.add(buttonContainer);
        popup.setContent(contentContainer);
        return popup;
    }
    
    private TnPopupContainer createNotifactionBox(int state, String notification, int timeout)
    {
        FrogMessageBox notificationBox = UiFactory.getInstance().createNotificationMessageBox(
            state,
            notification, timeout);
        notificationBox.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.SEMI_TRANSPARENT_BG_UNFOCUSED);
        notificationBox.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, NinePatchImageDecorator.SEMI_TRANSPARENT_BG_UNFOCUSED);
        notificationBox.setCommandEventListener(this);
        return notificationBox;
    }
    
    protected boolean isShownTransientView(int state)
    {
        switch (state)
        {
            case STATE_MAIN:
            case STATE_NEW_CATEGORY:
                return true;
        }
        return super.isShownTransientView(state);
    }

    private TnPopupContainer createEditCategoryPopup(int state)
    {
        TnPopupContainer popup = UiFactory.getInstance().createPopupContainer(state);

        TnLinearContainer contentContainer = UiFactory.getInstance().createLinearContainer(0, true);
        contentContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((EditCategoryUiDecorator) this.uiDecorator).POPUP_WIDTH);

        TnLinearContainer topContainer = UiFactory.getInstance().createLinearContainer(0, true,
            AbstractTnGraphics.VCENTER | AbstractTnGraphics.HCENTER);
        topContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((EditCategoryUiDecorator) this.uiDecorator).TOP_CONTAINER_HEIGHT);
        int max = AppConfigHelper.getMaxDisplaySize();
        int left = 20 * max / 480;
        int right = 20 * max / 480;
        int top = 20 * max / 480;
        int bottom = 10 * max / 480;
        topContainer.setPadding(left, top, right, bottom);
        topContainer.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, NinePatchImageDecorator.MESSAGE_BOX_TOP_BG);
        topContainer.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.MESSAGE_BOX_TOP_BG);
        // title label
        FrogLabel titleLabel = UiFactory.getInstance().createLabel(
            0,
            ResourceManager.getInstance().getCurrentBundle()
                    .getString(IStringEditCategory.RES_LABEL_EDIT_CATEGORY_TITLE, IStringEditCategory.FAMILY_EDIT_CATEGORY));
        titleLabel.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((EditCategoryUiDecorator) this.uiDecorator).POPUP_TITLE_HEIGHT);
        titleLabel.setForegroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_DA_GR), UiStyleManager.getInstance()
                .getColor(UiStyleManager.TEXT_COLOR_DA_GR));
        titleLabel.setBoldFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_POPUP_TITLE));
        topContainer.add(titleLabel);

        FrogNullField nullfield = UiFactory.getInstance().createNullField(0);
        nullfield.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((EditCategoryUiDecorator) this.uiDecorator).NULLFIELD_HEIGHT);
        topContainer.add(nullfield);
        // rename
        FavoriteCatalog category = (FavoriteCatalog) model.get(KEY_O_CATEGORY);
        String oldName = category.getName();
        CitizenTextField editCategoryInputField = UiFactory.getInstance().createCitizenTextField(oldName, 50, TnTextField.ANY, "",
            ID_EDIT_CATEGORY_TEXTFIELD, ImageDecorator.IMG_AC_BACKSPACE.getImage());
//        editCategoryInputField.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_TEXT_FIELD));
        editCategoryInputField.setBackgroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_WH));
        editCategoryInputField.getTnUiArgs()
                .put(TnUiArgs.KEY_PREFER_HEIGHT, ((EditCategoryUiDecorator) this.uiDecorator).TEXT_FIELD_HEIGHT);
        editCategoryInputField.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((EditCategoryUiDecorator) this.uiDecorator).TEXT_FIELD_WIDTH);
        editCategoryInputField.setHackVirtualKeyBoard(true);
        topContainer.add(editCategoryInputField);

        TnLinearContainer buttonContainer = UiFactory.getInstance().createLinearContainer(0, false,
            AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);
        buttonContainer.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, NinePatchImageDecorator.MESSAGE_BOX_BOTTOM_BG);
        buttonContainer.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.MESSAGE_BOX_BOTTOM_BG);
        buttonContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((EditCategoryUiDecorator) this.uiDecorator).BUTTON_CONTAINER_HEIGHT);
        buttonContainer.setPadding(0, 8, 0, 18);

        FrogButton doneButton = UiFactory.getInstance().createButton(
            0,
            ResourceManager.getInstance().getCurrentBundle()
                    .getString(IStringEditCategory.RES_BTTN_RENAME_CATEGORY, IStringEditCategory.FAMILY_EDIT_CATEGORY));
        doneButton.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((EditCategoryUiDecorator) uiDecorator).BUTTON_EDIT_CATEGORY_WIDTH);
        doneButton.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((EditCategoryUiDecorator) uiDecorator).BUTON_HEIGHT);
        TnMenu menu = UiFactory.getInstance().createMenu();
        menu.add("", CMD_RENAME_CATEGORY);
        doneButton.setMenu(menu, AbstractTnComponent.TYPE_CLICK);
        doneButton.setCommandEventListener(this);

        FrogButton deleteButton = UiFactory.getInstance().createButton(
            0,
            ResourceManager.getInstance().getCurrentBundle()
                    .getString(IStringEditCategory.RES_BTTN_DELETE, IStringEditCategory.FAMILY_EDIT_CATEGORY));
        deleteButton.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((EditCategoryUiDecorator) uiDecorator).BUTTON_EDIT_CATEGORY_WIDTH);
        deleteButton.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((EditCategoryUiDecorator) uiDecorator).BUTON_HEIGHT);
        menu = UiFactory.getInstance().createMenu();
        menu.add("", CMD_DELETE_CATEGORY);
        deleteButton.setMenu(menu, AbstractTnComponent.TYPE_CLICK);
        deleteButton.setCommandEventListener(this);

        FrogNullField nullField1 = UiFactory.getInstance().createNullField(0);
        nullField1.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((EditCategoryUiDecorator) uiDecorator).NULLFIELD_EDIT_CATEGORY_WIDTH);

        buttonContainer.add(doneButton);
        buttonContainer.add(nullField1);
        buttonContainer.add(deleteButton);
        contentContainer.add(topContainer);
        contentContainer.add(buttonContainer);

        popup.setContent(contentContainer);
        return popup;
    }

    protected boolean prepareModelData(int state, int commandId)
    {
        switch (commandId)
        {
            case CMD_RENAME_CATEGORY:
            {
                TnLinearContainer contentContainer = (TnLinearContainer) getCurrentPopup().getContent();
                CitizenTextField field = (CitizenTextField) contentContainer.getComponentById(ID_EDIT_CATEGORY_TEXTFIELD);
                String name = field.getText();
                model.put(KEY_S_RENAMED_CATEGORY, name);
                break;
            }
            case CMD_ADD_CATEGORY:
            {
                TnLinearContainer contentContainer = (TnLinearContainer) getCurrentPopup().getContent();
                CitizenTextField field = (CitizenTextField) contentContainer.getComponentById(ID_ADD_CATEGORY_TEXTFIELD);
                String categoryName = field.getText();
                model.put(KEY_S_NEW_CATEGROY_NAME, categoryName);
                break;
            }
            case CMD_COMMON_CANCEL:
            {
                 if(state == STATE_NEW_CATEGORY)
                 {
                     TnLinearContainer contentContainer = (TnLinearContainer) getCurrentPopup().getContent();
                     TeleNavDelegate.getInstance().closeVirtualKeyBoard(contentContainer);
                 }
            }
        }
        return super.prepareModelData(state, commandId);
    }

    private TnPopupContainer createDeleteCategoryMessageBox(int state)
    {
        TnPopupContainer popup = UiFactory.getInstance().createPopupContainer(state);
        TnLinearContainer container = UiFactory.getInstance().createLinearContainer(0, true);
        container.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((EditCategoryUiDecorator) this.uiDecorator).POPUP_WIDTH);

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
        FavoriteCatalog category = (FavoriteCatalog) model.get(KEY_O_CATEGORY);

        FrogMultiLine title = UiFactory.getInstance().createMultiLine(
            0,
            ResourceManager.getInstance().getCurrentBundle()
                    .getString(IStringEditCategory.RES_MESSAGEBOX_DELETE_CATEGORY, IStringEditCategory.FAMILY_EDIT_CATEGORY));
        title.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((EditCategoryUiDecorator) uiDecorator).MULTILINE_WIDTH);
        title.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((EditCategoryUiDecorator) uiDecorator).MULTILINE_HEIGHT);
        title.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_NAV_INFO_BAR));
        title.setForegroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_ME_GR));
        title.setTextAlign(FrogMultiLine.TEXT_ALIGN_CENTER);
        topContainer.add(title);

        FrogMultiLine hintLabel = UiFactory.getInstance().createMultiLine(
            0,
            ResourceManager.getInstance().getCurrentBundle()
                    .getString(IStringEditCategory.RES_MESSAGEBOX_DELETE_CATEGORY_HINT, IStringEditCategory.FAMILY_EDIT_CATEGORY));
        hintLabel.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_NAV_INFO_BAR));
        hintLabel.setForegroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_OR));
        hintLabel.setTextAlign(FrogMultiLine.TEXT_ALIGN_CENTER);
        hintLabel.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_NAV_INFO_BAR));
        hintLabel.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((EditCategoryUiDecorator) uiDecorator).MULTILINE_WIDTH);
        topContainer.add(hintLabel);
        
        FrogNullField nullfield = UiFactory.getInstance().createNullField(0);
        nullfield.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((EditCategoryUiDecorator) uiDecorator).NULLFIELD_HEIGHT);
        topContainer.add(nullfield);
        
        CitizenAddressListItem item = UiFactory.getInstance().createCitizenAddressListItem(0);
        item.setGap(5, 0);
        item.setTitleColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_ME_GR), UiStyleManager
                .getInstance().getColor(UiStyleManager.TEXT_COLOR_ME_GR));
        item.getTnUiArgs().put(CitizenAddressListItem.KEY_LEFT_ICON_UNFOCUS, ImageDecorator.IMG_FAV_ICON_FOLDER_UNFOCUS);
        item.getTnUiArgs().put(CitizenAddressListItem.KEY_LEFT_ICON_FOCUS, ImageDecorator.IMG_FAV_ICON_FOLDER_UNFOCUS);
        item.setTitleColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_ME_GR), UiStyleManager
            .getInstance().getColor(UiStyleManager.TEXT_COLOR_ME_GR));
        item.setTitleBadgeColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_LI_GR), UiStyleManager
            .getInstance().getColor(UiStyleManager.TEXT_COLOR_LI_GR));
        item.setForegroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_ME_GR), UiStyleManager
                .getInstance().getColor(UiStyleManager.TEXT_COLOR_ME_GR));
        item.setTitle(FrogTextHelper.BOLD_START + category.getName() + FrogTextHelper.BOLD_END);
        item.setTitleBadge(" (" + DaoManager.getInstance().getAddressDao().getFavorateAddresses(category.getName(), false).size() + ")");
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
        item.setBoldFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_LIST_SINGLE));
        item.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_FAV_CATEGORY_NUMBER));
        topContainer.add(item);
        
        FrogNullField nullfield2 = UiFactory.getInstance().createNullField(0);
        nullfield2.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((EditCategoryUiDecorator) uiDecorator).NULLFIELD_HEIGHT);
        topContainer.add(nullfield2);
        
        TnLinearContainer buttonContainer = UiFactory.getInstance().createLinearContainer(0, false,
            AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);
        buttonContainer.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, NinePatchImageDecorator.MESSAGE_BOX_BOTTOM_BG);
        buttonContainer.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.MESSAGE_BOX_BOTTOM_BG);
        buttonContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((EditCategoryUiDecorator) this.uiDecorator).BUTTON_CONTAINER_HEIGHT);
        buttonContainer.setPadding(0, 8, 0, 18);

        FrogButton deleteButton = UiFactory.getInstance().createButton(
            0,
            ResourceManager.getInstance().getCurrentBundle()
                    .getString(IStringEditCategory.RES_BTTN_DELETE, IStringEditCategory.FAMILY_EDIT_CATEGORY));
        deleteButton.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((EditCategoryUiDecorator) uiDecorator).BUTTON_EDIT_CATEGORY_WIDTH);
        deleteButton.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((EditCategoryUiDecorator) uiDecorator).BUTON_HEIGHT);
        TnMenu menu = UiFactory.getInstance().createMenu();
        menu.add("", CMD_COMMON_OK);
        deleteButton.setMenu(menu, AbstractTnComponent.TYPE_CLICK);
        deleteButton.setCommandEventListener(this);

        FrogNullField nullField1 = UiFactory.getInstance().createNullField(0);
        nullField1.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((EditCategoryUiDecorator) uiDecorator).NULLFIELD_EDIT_CATEGORY_WIDTH);

        
        FrogButton cancelButton = UiFactory.getInstance().createButton(
            0,
            ResourceManager.getInstance().getCurrentBundle()
                    .getString(IStringEditCategory.RES_BTTN_CANCEL, IStringEditCategory.FAMILY_EDIT_CATEGORY));
        cancelButton.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((EditCategoryUiDecorator) uiDecorator).BUTTON_EDIT_CATEGORY_WIDTH);
        cancelButton.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((EditCategoryUiDecorator) uiDecorator).BUTON_HEIGHT);
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
        return popup;
    }

    protected boolean updatePopup(int state, TnPopupContainer popup)
    {
        return false;
    }

    protected TnScreen createScreen(int state)
    {
        return null;
    }

    protected boolean updateScreen(int state, TnScreen screen)
    {
        return false;
    }

}
