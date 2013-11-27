/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * AbstractMislogHelper.java
 *
 */
package com.telenav.log.mis.helper;

import com.telenav.log.mis.log.AbstractMisLog;

/**
 *@author Casper (pwang@telenav.cn)
 *@date 2010-12-16
 */
public abstract class AbstractMisLogHelper
{
    /**
     * Sets server controlled parameter for event processing
     * 
     * @param server controlled parameters string
     */
    public abstract void addRule(String rule);

    /** Based on context judges if event is necessary to log. */

    public abstract boolean isLogEnable(AbstractMisLog  log);

    /**
     * Fills in attributes that can not be processed by execution context. These missing attributes can not be filled by
     * executor because of extensive processing or unnecessary dependencies.
     * 
     * @return true - if event was successfully processed, false if necessary to skip event from loggin
     */
    public abstract void fillAttrbute(AbstractMisLog log);
    
    public abstract void reset();

}
