/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * IStringFeedback.java
 *
 */
package com.telenav.res;

/**
 *@author wzhu (wzhu@telenav.cn)
 *@date 2010-11-30
 */
public interface IStringFeedback
{
  //=====================================feedback family=====================================//
    public final static String FAMILY_FEEDBACK = "feedback";
    
    public final static int FEEDBACK_STR_BASE        = 90000;
    
    //extras res id:
    public final static int RES_LABEL_TITLE = FEEDBACK_STR_BASE + 1;
    public final static int RES_TEXTFIELD_HINT = FEEDBACK_STR_BASE + 2;
    public final static int RES_BUTTON_SUBMIT = FEEDBACK_STR_BASE + 3;
    public final static int RES_MESSAGE_BOX_ERROR = FEEDBACK_STR_BASE + 4;
    public final static int RES_MESSAGE_BOX_SUCCESS = FEEDBACK_STR_BASE + 5;
}
