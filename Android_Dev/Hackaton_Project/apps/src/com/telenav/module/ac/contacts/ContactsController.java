/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * AddFavoriteController.java
 *
 */
package com.telenav.module.ac.contacts;

import com.telenav.app.AbstractContactProvider.TnContact;
import com.telenav.data.datatypes.address.Address;
import com.telenav.data.serverproxy.IUserProfileProvider;
import com.telenav.module.ModuleFactory;
import com.telenav.mvc.AbstractCommonController;
import com.telenav.mvc.AbstractController;
import com.telenav.mvc.AbstractModel;
import com.telenav.mvc.AbstractView;
import com.telenav.mvc.Parameter;
import com.telenav.mvc.StateMachine;

/**
 * @author wzhu (wzhu@telenav.cn)
 * @date 2010-12-2
 */
public class ContactsController extends AbstractCommonController implements IContactsConstants
{

    private final static int[][] STATE_TABLE = new int[][]
    {

    { STATE_VIRGIN, EVENT_CONTROLLER_START, STATE_MAIN, ACTION_INIT },

    { STATE_MAIN, EVENT_MODEL_RETURN_CONTACT, STATE_RETURN_CONTACT, ACTION_NONE },

    { STATE_MAIN, EVENT_MODEL_VALIDATE_ADDRESS, STATE_VALIDATE_ADDRESS, ACTION_NONE },

    { STATE_MAIN, EVENT_MODEL_CHOOSE_NATIVE_ADDRESS, STATE_CHOOSE_NATIVE_CONTACT, ACTION_NONE },
    
    { STATE_MAIN, EVENT_MODEL_CHOOSE_NATIVE_PHONE, STATE_CHOOSE_NATIVE_CONTACT, ACTION_NONE },

    { STATE_CHOOSE_NATIVE_CONTACT, CMD_SELECT_ADDRESS, STATE_VALIDATE_ADDRESS, ACTION_NONE },
    
    { STATE_CHOOSE_NATIVE_CONTACT, CMD_SELECT_PHONE, STATE_RETURN_CONTACT, ACTION_NONE },

    { STATE_COMMON_ERROR, CMD_COMMON_OK, STATE_VIRGIN, ACTION_NONE },

    { STATE_VALIDATE_ADDRESS, EVENT_CONTROLLER_ADDRESS_VALIDATED, STATE_RETURN_ADDRESS, ACTION_NONE },

    };

    public ContactsController(AbstractController superController)
    {
        super(superController);
    }

    protected void postStateChangeDelegate(int currentState, int nextState)
    {
        switch (nextState)
        {
            case STATE_RETURN_CONTACT:
            {
                Parameter data = new Parameter();
                data.put(KEY_O_CONTACT, model.get(KEY_O_CONTACT));
                postControllerEvent(EVENT_CONTROLLER_CONTACT_SELECTED, data);
                break;
            }
            case STATE_RETURN_ADDRESS:
            {
                Parameter data = new Parameter();
                Address contactAddress = (Address) model.fetch(KEY_O_VALIDATED_ADDRESS);
                contactAddress.setSource(Address.SOURCE_CONTACTS);
                data.put(KEY_O_SELECTED_ADDRESS, contactAddress);
                data.put(KEY_I_TYPE_ADDRESS_VALIDATOR_FROM, TYPE_FROM_CONTACTS);
                postControllerEvent(EVENT_CONTROLLER_ADDRESS_SELECTED, data);
                break;
            }
            case STATE_VALIDATE_ADDRESS:
            {
                TnContact contact = (TnContact) model.fetch(KEY_O_CONTACT);
                String address = contact.address;
                if (address != null)
                {
                    address = address.trim().replaceAll("\n", ",");
                }
                String country = contact.country;
                IUserProfileProvider userProfileProvider = (IUserProfileProvider)model.get(KEY_O_USER_PROFILE_PROVIDER);
                ModuleFactory.getInstance().startAddressValidatorController(this, address, "", null, userProfileProvider, country);
                break;
            }
        }
    }

    protected AbstractView createView()
    {
        return new ContactsViewTouch(new ContactsUiDecorator());
    }

    protected AbstractModel createModel()
    {
        return new ContactsModel();
    }

    protected StateMachine createStateMachine()
    {
        return new StateMachine(STATE_TABLE_COMMON, STATE_TABLE );
    }

}
