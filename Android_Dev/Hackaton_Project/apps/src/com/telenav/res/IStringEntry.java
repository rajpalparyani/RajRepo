/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * IStringEntry.java
 *
 */
package com.telenav.res;

/**
 *@author bduan
 *@date 2011-1-11
 */
public interface IStringEntry
{
    // =====================================extras family=====================================//
    public final static String FAMILY_ENTRY = "entry";

    public final static int ENTRY_STR_BASE = 95000;

    public final static int RES_LABEL_UPGRADE = ENTRY_STR_BASE + 1;

    public final static int RES_LABEL_UPGRADE_NOW = ENTRY_STR_BASE + 2;

    public final static int RES_LABEL_REMIND_ME_LATER = ENTRY_STR_BASE + 3;

    public final static int RES_LABEL_DONOT_ASK = ENTRY_STR_BASE + 4;

    public final static int RES_UPGRADE_SUMMARY_OPTIONAL = ENTRY_STR_BASE + 5;
    
    public final static int RES_LABEL_LOCATION_SETTING = ENTRY_STR_BASE + 6;
    
    public final static int RES_LABEL_SYNC_PURCHASE_ERROR_MESSAGE = ENTRY_STR_BASE + 7;

}
