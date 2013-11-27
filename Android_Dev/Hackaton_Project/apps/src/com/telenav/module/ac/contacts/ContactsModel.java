/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * AddFavoriteModel.java
 *
 */
package com.telenav.module.ac.contacts;

import java.util.Vector;

import com.telenav.app.AbstractContactProvider;
import com.telenav.app.AbstractContactProvider.IContactProviderCallback;
import com.telenav.app.AbstractContactProvider.TnContact;
import com.telenav.data.serverproxy.AbstractServerProxy;
import com.telenav.logger.Logger;
import com.telenav.mvc.AbstractCommonNetworkModel;
import com.telenav.mvc.ICommonConstants;
import com.telenav.res.IStringContacts;
import com.telenav.res.ResourceManager;

/**
 * @author wzhu (wzhu@telenav.cn)
 * @date 2010-12-2
 */
class ContactsModel extends AbstractCommonNetworkModel implements IContactsConstants, IContactProviderCallback
{

    public ContactsModel()
    {
    }

    protected void doActionDelegate(int actionId)
    {
        switch (actionId)
        {
            case ACTION_INIT:
            {
                AbstractContactProvider.getInstance().lookup(this);
                break;
            }
        }
    }

    protected void transactionFinishedDelegate(AbstractServerProxy proxy, String jobId)
    {
    }

    public void onError(String errorMessage)
    {
        String errorMsg = "";
        int type = getInt(KEY_I_AC_TYPE);
        switch (type)
        {
            case ICommonConstants.TYPE_AC_FROM_SHARE:
            {
                errorMsg = ResourceManager.getInstance().getCurrentBundle().getString(
                    IStringContacts.RES_MESSAGE_NO_NUMBER,
                    IStringContacts.FAMILY_CONTACTS);
                break;
            }
            default:
            {
                errorMsg = ResourceManager.getInstance().getCurrentBundle().getString(
                    IStringContacts.RES_MESSAGE_NO_ADDRESS,
                    IStringContacts.FAMILY_CONTACTS);
                break;
            }
        }
        put(KEY_S_ERROR_MESSAGE, errorMsg);
        postModelEvent(EVENT_MODEL_POST_ERROR);
        
        Logger.log(Logger.ERROR, getClass().getName(), errorMessage, null);
    }

    public void onResult(Vector results)
    {
        if(results == null)
        {
            postModelEvent(EVENT_MODEL_COMMON_BACK);
        }
        else
        {
            int type = getInt(KEY_I_AC_TYPE);
            switch (type)
            {
                case ICommonConstants.TYPE_AC_FROM_SHARE:
                {
                    Vector phoneContact = getPhonerContact(results);
                    if (phoneContact.size() == 0)
                    {
                        put(KEY_S_ERROR_MESSAGE,
                            ResourceManager.getInstance().getCurrentBundle()
                                    .getString(IStringContacts.RES_LABEL_PHONE_NUMBER_NOT_FOUND, IStringContacts.FAMILY_CONTACTS));
                        postModelEvent(EVENT_MODEL_POST_ERROR);
                    }
                    else if (phoneContact.size() == 1)
                    {
                        TnContact contact = (TnContact) phoneContact.elementAt(0);
                        put(KEY_O_CONTACT, contact);
                        postModelEvent(EVENT_MODEL_RETURN_CONTACT);
                    }
                    else if (phoneContact.size() > 1)
                    {
                        put(KEY_V_ALTERNATIVE_CHOICES, phoneContact);
                        postModelEvent(EVENT_MODEL_CHOOSE_NATIVE_PHONE);
                    }
                    break;
                }
               default:
                {
                    Vector addressContact = getAddressContact(results);
                    if (addressContact.size() == 0)
                    {
                        put(KEY_S_ERROR_MESSAGE,
                            ResourceManager.getInstance().getCurrentBundle()
                                    .getString(IStringContacts.RES_LABEL_ADDRESS_NOT_FOUND, IStringContacts.FAMILY_CONTACTS));
                        postModelEvent(EVENT_MODEL_POST_ERROR);
                    }
                    else if (addressContact.size() == 1)
                    {
                        TnContact contact = (TnContact) addressContact.elementAt(0);
                        put(KEY_O_CONTACT, contact);
                        postModelEvent(EVENT_MODEL_VALIDATE_ADDRESS);
                    }
                    else
                    {
                        put(KEY_V_ALTERNATIVE_CHOICES, addressContact);
                        postModelEvent(EVENT_MODEL_CHOOSE_NATIVE_ADDRESS);
                    }
                    break;
                }
            }
        }
    }

    public Vector getAddressContact(Vector contacts)
    {
        if (contacts == null || contacts.size() == 0)
            return contacts;
        Vector addressContact = new Vector();
        for (int i = 0; i < contacts.size(); i++)
        {
            TnContact contact = (TnContact) contacts.elementAt(i);
            if (contact.address != null && !"".equals(contact.address))
            {
                addressContact.addElement(contact);
            }
        }
        return addressContact;
    }

    public Vector getPhonerContact(Vector contacts)
    {
        if (contacts == null || contacts.size() == 0)
            return contacts;
        Vector phoneContact = new Vector();
        for (int i = 0; i < contacts.size(); i++)
        {
            TnContact contact = (TnContact) contacts.elementAt(i);
            if (contact.phoneNumber != null && !"".equals(contact.phoneNumber))
            {
                phoneContact.addElement(contact);
            }
        }
        return phoneContact;
    }
}
