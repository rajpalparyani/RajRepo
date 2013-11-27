/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * PoiDataListener.java
 *
 */
package com.telenav.module.poi;

/**
 *@author Albert Ma (byma@telenav.cn)
 *@date 2010-12-15
 */
public interface PoiDataListener
{
    public void poiResultUpdate(int status, int resultType, String msg, PoiDataWrapper poiDataWrapper);
}
