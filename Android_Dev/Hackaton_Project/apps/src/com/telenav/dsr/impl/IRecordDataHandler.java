/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * IRecordDataHandler.java
 *
 */
package com.telenav.dsr.impl;

/**
 *@author bduan
 *@date 2010-10-28
 */
public interface IRecordDataHandler
{
    /**
     * Note that the action should be fast, there
     * should be no heavy operation in this impl. 
     * @param b
     */
    public void writeHeader(int b);
    
    /**
     * Impl this method to handle record data.
     * Note that the action should be fast, there
     * should be no heavy operation in this impl. 
     * @param b
     */
    public void writeData(byte[] bytes);
}
