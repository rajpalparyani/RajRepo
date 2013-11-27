/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * AddressMisLog.java
 *
 */
package com.telenav.log.mis.log;

/**
 *@author Casper (pwang@telenav.cn)
 *@date 2011-4-11
 */
public class AddressMisLog  extends AbstractMisLog
{
    public AddressMisLog(String id, int eventType, int priority)
    {
        super(id, eventType, priority);
    }
    
    public void processLog()
    {
        super.processLog();
    }
    
    public void setSearchUid(String uid)
    {
        this.setAttribute(ATTR_SEARCH_UID, uid);
    }
    
    public void setResultNumber(int number)
    {
        this.setAttribute(ATTR_RESULT_NUMBER, String.valueOf(number));
    }
    
    public void setPageIndex(int index)
    {
        this.setAttribute(ATTR_PAGE_INDEX, String.valueOf(index));
    }
    
    public void setPageNumber(int number)
    {
        this.setAttribute(ATTR_PAGE_NUMBER, String.valueOf(number));
    }
    
    public void setSuggestionKeyword(String keyword)
    {
        this.setAttribute(ATTR_ONEBOX_SUGGESTION_KEYWORD, keyword);
    }
}
