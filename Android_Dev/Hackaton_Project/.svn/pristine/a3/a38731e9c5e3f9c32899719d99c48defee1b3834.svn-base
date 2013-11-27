/**
 *
 * Copyright 2013 TeleNav, Inc. All rights reserved.
 * ShareItem.java
 *
 */
package com.telenav.app.android.scout_us;

import java.io.Serializable;

/**
 *@author pwang
 *@date 2013-5-16
 */
public class NativeShareItem implements Serializable 
{
    public static final int NATIVE_SHARE_FACEBOOK = 1;
    public static final int NATIVE_SHARE_ITEM_TWITTER = 2;
    public static final int NATIVE_SHARE_ITEM_SMS = 3;
    public static final int NATIVE_SHARE_ITEM_EMAIL = 4;
    public static final int NATIVE_SHARE_ITEM_MORE = 5;
    
    private int itemType;
    
    private String subject;

    private String content;

    private String htmlContent;

    public NativeShareItem()
    {
    }

    public NativeShareItem(int itemType, String subject, String content)
    {
        this(itemType, subject, content, null);
    }
    
    public NativeShareItem(int itemType, String subject, String content, String htmlContent)
    {
        this.itemType = itemType;
        this.subject = subject;
        this.content = content;
        this.htmlContent = htmlContent;
    }

    public int getItemType()
    {
        return itemType;
    }

    public void setItemType(int itemType)
    {
        this.itemType = itemType;
    }

    public String getSubject()
    {
        return subject;
    }

    public void setSubject(String subject)
    {
        this.subject = subject;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }
    
    public String getHtmlContent()
    {
        return htmlContent;
    }
    
    public void setHtmlContent(String htmlContent)
    {
        this.htmlContent = htmlContent;
    }

}
