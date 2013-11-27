/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * IStringContacts.java
 *
 */
package com.telenav.res;

/**
 * @author wzhu (wzhu@telenav.cn)
 * @date 2010-12-10
 */
public interface IStringContacts
{
    // =====================================contacts family=====================================//
    public final static String FAMILY_CONTACTS = "contacts";

    public final static int CONTACTS_STR_BASE = 10000;

    // contacts res id:

    public final static int RES_LABEL_PHONE_NUMBER_NOT_FOUND = CONTACTS_STR_BASE + 1;

    public final static int RES_LABEL_ADDRESS_NOT_FOUND = CONTACTS_STR_BASE + 2;

    public final static int RES_LABEL_MESSAGE_PART1 = CONTACTS_STR_BASE + 3;

    public final static int RES_LABEL_MESSAGE_PART2 = CONTACTS_STR_BASE + 4;
    
    public final static int RES_BTN_YES = CONTACTS_STR_BASE + 5;
    
    public final static int RES_BTN_NO = CONTACTS_STR_BASE + 6;
    
    public final static int RES_MESSAGE_NOT_FOUND = CONTACTS_STR_BASE + 7;
    
    public final static int RES_MESSAGE_WHICH_ADDRESS = CONTACTS_STR_BASE + 8;
    
    public final static int RES_MESSAGE_WHICH_NUMBER = CONTACTS_STR_BASE + 9;
    
    public final static int RES_MESSAGE_HOME = CONTACTS_STR_BASE + 10;
    
    public final static int RES_MESSAGE_WORK = CONTACTS_STR_BASE + 11;
    
    public final static int RES_MESSAGE_FAX_HOME = CONTACTS_STR_BASE + 12;
    
    public final static int RES_MESSAGE_FAX_WORK = CONTACTS_STR_BASE + 13;
    
    public final static int RES_MESSAGE_MOBILE = CONTACTS_STR_BASE + 14;
    
    public final static int RES_MESSAGE_NO_ADDRESS = CONTACTS_STR_BASE + 15;
    
    public final static int RES_MESSAGE_NO_NUMBER = CONTACTS_STR_BASE + 16;
}
