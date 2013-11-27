/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * AbstractCommonEntryModel.java
 *
 */
package com.telenav.module.entry;

import com.telenav.mvc.AbstractCommonNetworkModel;

/**
 *@author qli
 *@date 2011-1-27
 */
public abstract class AbstractCommonEntryModel extends AbstractCommonNetworkModel implements ICommonEntryConstants
{

    protected void doActionDelegate(int actionId)
    {
        switch (actionId)
        {
            case ACTION_LINK_TO_MAP:
            {
                this.postModelEvent(EVENT_MODEL_LINK_TO_MAP);
                break;
            }
            case ACTION_LINK_TO_AC:
            {
                this.postModelEvent(EVENT_MODEL_LINK_TO_AC);
                break;
            }
            case ACTION_LINK_TO_POI:
            {
                this.postModelEvent(EVENT_MODEL_LINK_TO_POI);
                break;
            }
            case ACTION_LINK_TO_EXTRA:
            {
                this.postModelEvent(EVENT_MODEL_LINK_TO_EXTRA);
                break;
            }
        }
    }

}
