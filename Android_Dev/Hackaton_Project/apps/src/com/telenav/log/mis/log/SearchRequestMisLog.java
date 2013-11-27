/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * SearchRequestMisLog.java
 *
 */
package com.telenav.log.mis.log;

import com.telenav.data.datatypes.address.Stop;
import com.telenav.log.mis.IMisLogConstants;

/**
 *@author Casper (pwang@telenav.cn)
 *@date 2010-12-27
 */
public class SearchRequestMisLog extends AbstractMisLog
{
    public SearchRequestMisLog(String id, int priority)
    {
        super(id, TYPE_POI_SEARCH_REQUEST, priority);
    }
    
    public void processLog()
    {
        super.processLog();
        processGenericAttr();
        
        if(optAttributeByte(INNER_ATTR_ADDRESS_TYPE, (byte)-1) != -1)
        {
            byte type = optAttributeByte(INNER_ATTR_ADDRESS_TYPE, (byte)-1);
            
            if (type == Stop.STOP_CURRENT_LOCATION)
            {
                this.setSearchArea(IMisLogConstants.VALUE_SEARCH_AREA_CURRENT_LOCATION);
            }
            else
            {
                this.setSearchArea(IMisLogConstants.VALUE_SEARCH_AREA_ADDRESS);
            }
        }
    }
    
    public void setSearchArea(String searhArea)
    {
        this.setAttribute(ATTR_SEARCH_AREA, searhArea);
    }

}
