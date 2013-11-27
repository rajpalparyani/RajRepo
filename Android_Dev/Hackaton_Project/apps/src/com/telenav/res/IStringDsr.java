/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * IStringDsr.java
 *
 */
package com.telenav.res;

/**
 *@author fqming (fqming@telenav.cn)
 *@date Aug 9, 2010
 */
public interface IStringDsr
{
    //=====================================dsr family=====================================//
    public final static String FAMILY_DSR = "dsr";
    
    public final static int DSR_STR_BASE        = 15000;
    
    //dsr res id:
    public final static int RES_LABEL_SAY_COMMAND = DSR_STR_BASE + 1;
    public final static int RES_LABEL_EXAMPLES = DSR_STR_BASE + 2;
    public final static int RES_LABEL_EX0 = DSR_STR_BASE + 3;
    public final static int RES_LABEL_EX1 = DSR_STR_BASE + 4;
    public final static int RES_LABEL_EX2 = DSR_STR_BASE + 5;
    public final static int RES_LABEL_THINKING = DSR_STR_BASE + 6;
    public final static int RES_LABEL_FINISH = DSR_STR_BASE + 7;
    public final static int RES_LABEL_SEARCHING = DSR_STR_BASE + 8;
    public final static int RES_LABEL_SEARCHING_NEARBY = DSR_STR_BASE + 9;
    public final static int RES_LABEL_AROUND = DSR_STR_BASE + 10;
    public final static int RES_LABEL_UP_AHEAD = DSR_STR_BASE + 11;
    public final static int RES_LABEL_NEAR_DES = DSR_STR_BASE + 12;
    public final static int RES_LABEL_ERROR_MSG = DSR_STR_BASE + 13;
    public final static int RES_LABEL_EX3 = DSR_STR_BASE + 14;
    public final static int RES_LABEL_LONG_SILENCE = DSR_STR_BASE + 15;
}
