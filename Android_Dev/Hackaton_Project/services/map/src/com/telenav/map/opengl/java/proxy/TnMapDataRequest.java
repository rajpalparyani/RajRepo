/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * TnMapDataRequest.java
 *
 */
package com.telenav.map.opengl.java.proxy;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2010-9-28
 */
public class TnMapDataRequest
{
    public final static int TN_MAP_REQUEST_RESULT_COMPLETE = 0;
    public final static int TN_MAP_REQUEST_RESULT_FAIL = 1;
    public final static int TN_MAP_REQUEST_RESULT_TIMEOUT = 2;
    public final static int TN_MAP_REQUEST_RESULT_NOT_FOUND = 3;
    
    public int result;
    
    public void setRequestResult(int result)
    {
        this.result = result;
    }
}
