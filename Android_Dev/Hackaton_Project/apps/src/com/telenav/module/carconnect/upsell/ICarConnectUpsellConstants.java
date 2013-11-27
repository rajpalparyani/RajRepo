/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * ICarConnectUpsellConstants.java
 *
 */
package com.telenav.module.carconnect.upsell;

import com.telenav.mvc.ICommonConstants;

/**
 *@author chihlh
 *@date Mar 20, 2012
 */
interface ICarConnectUpsellConstants extends ICommonConstants
{
    public static final int STATE_WAIT_UPSELL_RETURN = (STATE_USER_BASE + USER_BASE_CARCONNECTUPSELL + 1);
    
    public static final int STATE_UPSELL_PURCHASE_FINISH = (STATE_USER_BASE + USER_BASE_CARCONNECTUPSELL + 2) | MASK_STATE_TRANSIENT;
    
    // ------------------------------------------------------------
    // Model event id definition
    // ------------------------------------------------------------
    
    public static final int EVENT_MODEL_EXIT = (EVENT_MODEL_USER_BASE + USER_BASE_CARCONNECTUPSELL + 1);
    // ------------------------------------------------------------
    // Action id definition
    // ------------------------------------------------------------
    
    public static final int ACTION_EXIT = (EVENT_MODEL_USER_BASE + USER_BASE_CARCONNECTUPSELL + 1);

}
