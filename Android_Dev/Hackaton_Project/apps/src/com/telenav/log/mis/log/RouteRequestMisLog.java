/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * RouteRequestMislog.java
 *
 */
package com.telenav.log.mis.log;

import com.telenav.log.mis.IMisLogConstants;
import com.telenav.log.mis.MisLogManager;
import com.telenav.logger.Logger;

/**
 *@author Casper (pwang@telenav.cn)
 *@date 2010-12-27
 */
public class RouteRequestMisLog extends AbstractMisLog
{

    public RouteRequestMisLog(String id, int priority)
    {
        super(id, TYPE_ROUTE_REQUEST, priority);
    }
    
    public void processLog()
    {
        super.processLog();
        processGenericAttr();
        Logger.log(Logger.INFO, this.getClass().getName(), "Mislog : " +getAttribute(ATTR_ADDRESS_INPUT) );
        resetRequestRouteBy();
    }

    private void resetRequestRouteBy()
    {
        AppStatusMisLog appStatusMisLog = (AppStatusMisLog) MisLogManager.getInstance().getMisLog(
            IMisLogConstants.TYPE_INNER_APP_STATUS);
        appStatusMisLog.setRouteRequestBy(IMisLogConstants.VALUE_ADDRESS_INPUT_TYPE_SEARCH);
    }

    public void setAddressInput(String addressInput)
    {
        this.setAttribute(ATTR_ADDRESS_INPUT, addressInput);
    }

}
