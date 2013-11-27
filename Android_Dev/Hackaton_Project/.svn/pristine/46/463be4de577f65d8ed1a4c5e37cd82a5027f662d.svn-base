/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * AddFavoriteViewTouch.java
 *
 */
package com.telenav.module.ac.contacts;

import java.util.Vector;

import com.telenav.app.AbstractContactProvider.TnContact;
import com.telenav.mvc.AbstractCommonUiDecorator;
import com.telenav.mvc.AbstractCommonView;
import com.telenav.res.IStringContacts;
import com.telenav.res.ResourceManager;
import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.core.TnMenu;
import com.telenav.tnui.core.TnScreen;
import com.telenav.tnui.core.TnUiArgs;
import com.telenav.tnui.core.TnUiEvent;
import com.telenav.tnui.widget.TnPopupContainer;
import com.telenav.ui.NinePatchImageDecorator;
import com.telenav.ui.UiFactory;
import com.telenav.ui.UiStyleManager;
import com.telenav.ui.citizen.CitizenAddressListItem;
import com.telenav.ui.citizen.CitizenScreen;
import com.telenav.ui.frogui.text.FrogTextHelper;
import com.telenav.ui.frogui.widget.FrogAdapter;
import com.telenav.ui.frogui.widget.FrogLabel;
import com.telenav.ui.frogui.widget.FrogList;

/**
 * @author wzhu (wzhu@telenav.cn)
 * @date 2010-12-2
 */
class ContactsViewTouch extends AbstractCommonView implements IContactsConstants
{

    public ContactsViewTouch(AbstractCommonUiDecorator uiDecorator)
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
        return null;
    }

    protected boolean updatePopup(int state, TnPopupContainer popup)
    {
        return false;
    }

    protected TnScreen createScreen(int state)
    {
        switch (state)
        {
            case STATE_CHOOSE_NATIVE_CONTACT:
            {
                int type = model.getInt(KEY_I_AC_TYPE);
                return createChooseNativeContactScreen(state, type);
            }
        }
        return null;
    }

    private TnScreen createChooseNativeContactScreen(int state, int type)
    {
        CitizenScreen chooseContactScreen = UiFactory.getInstance().createScreen(state);
        chooseContactScreen.getTitleContainer().setBackgroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_ME_GR));
        String title = null;
        switch (type)
        {
            case TYPE_AC_FROM_SHARE:
            {
                title = FrogTextHelper.BOLD_START + ResourceManager.getInstance().getCurrentBundle()
                .getString(IStringContacts.RES_MESSAGE_WHICH_NUMBER, IStringContacts.FAMILY_CONTACTS) + FrogTextHelper.BOLD_END;
                break;
            }
            default:
            {
                title = FrogTextHelper.BOLD_START + ResourceManager.getInstance().getCurrentBundle()
                        .getString(IStringContacts.RES_MESSAGE_WHICH_ADDRESS, IStringContacts.FAMILY_CONTACTS) + FrogTextHelper.BOLD_END;
				break;
            }
        }
        FrogLabel label = UiFactory.getInstance().createLabel(0, title);
        label.setForegroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_WH), UiStyleManager.getInstance()
                .getColor(UiStyleManager.TEXT_COLOR_WH));
        label.setBoldFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_SCREEN_TITLE));
        chooseContactScreen.getTitleContainer().getTnUiArgs()
                .put(TnUiArgs.KEY_PREFER_HEIGHT, ((ContactsUiDecorator) uiDecorator).CHOOSE_ADDRESS_TITLE_HEIGHT);
        final Vector choices = model.getVector(KEY_V_ALTERNATIVE_CHOICES);
        FrogList list = UiFactory.getInstance().createList(0);
        list.setAdapter(new FrogAdapter()
        {

            public int getItemType(int position)
            {
                return 0;
            }

            public int getCount()
            {
                return choices.size();
            }

            public String getTitle(TnContact contact)
            {
                String title = null;
                int type = model.getInt(KEY_I_AC_TYPE);
                switch (type)
                {
                    case TYPE_AC_FROM_SHARE:
                    {
                        int phoneType = contact.phoneNumberType;
                        switch (phoneType)
                        {
                            case TnContact.TYPE_HOME:
                            {
                                title = ResourceManager.getInstance().getCurrentBundle()
                                        .getString(IStringContacts.RES_MESSAGE_HOME, IStringContacts.FAMILY_CONTACTS)
                                        + " " + ResourceManager.getInstance().getStringConverter().convertPhoneNumber(contact.phoneNumber);
                                break;
                            }
                            case TnContact.TYPE_MOBILE:
                            {
                                title = ResourceManager.getInstance().getCurrentBundle()
                                        .getString(IStringContacts.RES_MESSAGE_MOBILE, IStringContacts.FAMILY_CONTACTS)
                                        + " " + ResourceManager.getInstance().getStringConverter().convertPhoneNumber(contact.phoneNumber);
                                break;
                            }
                            case TnContact.TYPE_FAX_HOME:
                            {
                                title = ResourceManager.getInstance().getCurrentBundle()
                                        .getString(IStringContacts.RES_MESSAGE_FAX_HOME, IStringContacts.FAMILY_CONTACTS)
                                        + " " + ResourceManager.getInstance().getStringConverter().convertPhoneNumber(contact.phoneNumber);
                                break;
                            }
                            case TnContact.TYPE_FAX_WORK:
                            {
                                title = ResourceManager.getInstance().getCurrentBundle()
                                        .getString(IStringContacts.RES_MESSAGE_FAX_WORK, IStringContacts.FAMILY_CONTACTS)
                                        + " " + ResourceManager.getInstance().getStringConverter().convertPhoneNumber(contact.phoneNumber);
                                break;
                            }
                            case TnContact.TYPE_WORK:
                            {
                                title = ResourceManager.getInstance().getCurrentBundle()
                                        .getString(IStringContacts.RES_MESSAGE_WORK, IStringContacts.FAMILY_CONTACTS)
                                        + " " + ResourceManager.getInstance().getStringConverter().convertPhoneNumber(contact.phoneNumber);
                                break;
                            }
                            default:
                            {
                                title = ResourceManager.getInstance().getStringConverter().convertPhoneNumber(contact.phoneNumber);
                            }
                        }
                        break;
                    }
                    default:
                    {
                        int addressType = contact.addressType;
                        String address = contact.address;
                        address = address.replaceAll("\n", ", ");
						if (addressType == TnContact.ADDRESS_HOME)
                        {
                            title = ResourceManager.getInstance().getCurrentBundle()
                                    .getString(IStringContacts.RES_MESSAGE_HOME, IStringContacts.FAMILY_CONTACTS)
                                    + " " + address;
                        }
                        else if (addressType == TnContact.ADDRESS_OFFICE)
                        {
                            title = ResourceManager.getInstance().getCurrentBundle()
                                    .getString(IStringContacts.RES_MESSAGE_WORK, IStringContacts.FAMILY_CONTACTS)
                                    + " " + address;
                        }
                        else
                        {
                            title = address;
                        }
						break;
                    }
                }
                return title;
            }

            public AbstractTnComponent getComponent(int position, AbstractTnComponent convertComponent, AbstractTnComponent parent)
            {
                TnContact contact = (TnContact) choices.elementAt(position);
                if (convertComponent == null)
                {
                    CitizenAddressListItem item = UiFactory.getInstance().createCitizenAddressListItem(position);
                    item.setTitle(getTitle(contact));
                    item.setTitleColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_ME_GR), 
                        UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_ME_GR));
                    item.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.LIST_ITEM_FOCUSED);
                    item.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, NinePatchImageDecorator.LIST_ITEM_UNFOCUS);
                    item.setBoldFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_LIST_SINGLE));
                    item.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_LABEL));
                    TnMenu menu = UiFactory.getInstance().createMenu();
                    if(model.getInt(KEY_I_AC_TYPE) == TYPE_AC_FROM_SHARE)
                    {
                        menu.add("", CMD_SELECT_PHONE);
                    }
                    else
                    {
                        menu.add("", CMD_SELECT_ADDRESS);
                    }
                    item.setMenu(menu, AbstractTnComponent.TYPE_CLICK);
                    item.setCommandEventListener(ContactsViewTouch.this);
                    convertComponent = item;
                }
                else
                {
                    convertComponent.setId(position);
                    ((CitizenAddressListItem) convertComponent).setTitle(getTitle(contact));
                }
                return convertComponent;
            }
        });
        chooseContactScreen.getTitleContainer().add(label);
        chooseContactScreen.getContentContainer().add(list);
        chooseContactScreen.getContentContainer().setBackgroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_WH));
        return chooseContactScreen;
    }

    protected boolean preProcessUIEvent(TnUiEvent tnUiEvent)
    {
        int type = tnUiEvent.getType();
        switch (type)
        {
            case TnUiEvent.TYPE_COMMAND_EVENT:
            {
                if (tnUiEvent.getComponent() instanceof CitizenAddressListItem)
                {
                    CitizenAddressListItem item = ((CitizenAddressListItem) tnUiEvent.getComponent());
                    model.put(KEY_I_INDEX, item.getId());
                    break;
                }
            }
        }
        return super.preProcessUIEvent(tnUiEvent);
    }

    protected boolean prepareModelData(int state, int commandId)
    {
        switch (commandId)
        {
            case CMD_SELECT_ADDRESS:
            {
                switch (state)
                {
                    case STATE_CHOOSE_NATIVE_CONTACT:
                    {
                        model.put(KEY_O_CONTACT, model.getVector(KEY_V_ALTERNATIVE_CHOICES).elementAt(model.getInt(KEY_I_INDEX)));
                        break;
                    }
                }
                break;
            }
            case CMD_SELECT_PHONE:
            {
                model.put(KEY_O_CONTACT, model.getVector(KEY_V_ALTERNATIVE_CHOICES).elementAt(model.getInt(KEY_I_INDEX)));
                break;
            }
        }
        return super.prepareModelData(state, commandId);
    }

    protected boolean updateScreen(int state, TnScreen screen)
    {
        return false;
    }

}
