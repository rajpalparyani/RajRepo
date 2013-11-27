/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * OneBoxSearchBean.java
 *
 */
package com.telenav.data.datatypes.poi;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2010-12-22
 */
public class OneBoxSearchBean
{
    private String key;
    private String content;
    
    public void setKey(String key)
    {
        this.key = key;
    }
    
    public String getKey()
    {
        return this.key;
    }
    
    public void setContent(String content)
    {
        this.content = content;
    }
    
    public String getContent()
    {
        return this.content;
    }
}
