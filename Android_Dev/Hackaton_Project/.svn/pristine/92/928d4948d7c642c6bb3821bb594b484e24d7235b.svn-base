/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * PreferenceChangeMislog.java
 *
 */
package com.telenav.log.mis.log;

/**
 *@author Casper (pwang@telenav.cn)
 *@date 2010-12-27
 */
public class PreferenceChangeMisLog extends AbstractMisLog
{
    public PreferenceChangeMisLog(String id, int priority)
    {
        super(id, TYPE_PREFERENCE_CHANGE, priority);
    }

    public void setPreferenceId(String id)
    {
        this.setAttribute(ATTR_PREFERENCE_ID, id);
    }

    public void setPreferenceOldValue(String oldValue)
    {
        this.setAttribute(ATTR_PREF_OLD_VALUE, oldValue);
    }

    public void setPreferenceNewValue(String newValue)
    {
        this.setAttribute(ATTR_PREF_NEW_VALUE, newValue);
    }
}
