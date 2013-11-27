/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * CarConnectUpsellModel.java
 *
 */
package com.telenav.module.carconnect.upsell;

import com.telenav.mvc.AbstractCommonModel;

/**
 *@author chihlh
 *@date Mar 20, 2012
 */
class CarConnectUpsellModel extends AbstractCommonModel implements ICarConnectUpsellConstants
{

    @Override
    protected void doActionDelegate(int actionId)
    {
        switch (actionId)
        {
            case ACTION_EXIT:
                this.postModelEvent(EVENT_MODEL_EXIT);
                break;
        }
    }

}
