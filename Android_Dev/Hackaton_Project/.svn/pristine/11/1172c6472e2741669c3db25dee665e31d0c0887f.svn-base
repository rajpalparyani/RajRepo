/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * AbstractEntryController.java
 *
 */
package com.telenav.module.entry;

import com.telenav.app.CommManager;
import com.telenav.data.datatypes.address.Address;
import com.telenav.data.datatypes.browser.BrowserSessionArgs;
import com.telenav.data.serverproxy.IUserProfileProvider;
import com.telenav.module.ModuleFactory;
import com.telenav.mvc.AbstractCommonController;
import com.telenav.mvc.AbstractController;
import com.telenav.mvc.HomeScreenManager;
import com.telenav.mvc.ICommonConstants;

/**
 *@author qli
 *@date 2011-1-27
 */
public abstract class AbstractCommonEntryController extends AbstractCommonController implements ICommonEntryConstants
{

    public AbstractCommonEntryController(AbstractController superController)
    {
        super(superController);
    }

    protected void postStateChangeDelegate(int currentState, int nextState)
    {
        switch( nextState )
        {
            case STATE_LINK_TO_AC:
            {
            	ModuleFactory.getInstance().startDashboardController(this, false);
                break;
            }
            case STATE_LINK_TO_POI:
            {
                Address centerAnchor = (Address) model.get(KEY_O_SELECTED_ADDRESS);
                IUserProfileProvider userProfileProvider = (IUserProfileProvider)model.get(ICommonConstants.KEY_O_USER_PROFILE_PROVIDER);
                ModuleFactory.getInstance().startCategorySearchController(this, ICommonConstants.TYPE_AC_FROM_ENTRY, -1, -1, -1, -1,
                    centerAnchor, null, null, userProfileProvider, false);
                break;
            }
            case STATE_LINK_TO_MAP:
            {
                IUserProfileProvider userProfileProvider = (IUserProfileProvider)model.get(KEY_O_USER_PROFILE_PROVIDER);
                ModuleFactory.getInstance().startMapController(this, HomeScreenManager.getHomeMapFromType(), null, null, userProfileProvider);
                break;
            }
            case STATE_LINK_TO_EXTRA:
            {
                BrowserSessionArgs args = new BrowserSessionArgs(CommManager.APP_STORE_DOMAIN_ALIAS);
                IUserProfileProvider userProfileProvider = (IUserProfileProvider)model.get(KEY_O_USER_PROFILE_PROVIDER);
                ModuleFactory.getInstance().startBrowserSdkController(this, args, false, userProfileProvider);
                break;
            }
        }
    }

}
