/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * IGLSnapBitmapCallBack.java
 *
 */
package com.telenav.tnui.widget.android;


/**
 *@author yren
 *@date 2012-2-13
 */
public interface IGLSnapBitmapCallBack
{
    public void snapBitmapCompleted(int buffer[], int width, int height, long transactionId);    
    
    public void snapBitmapCanceled(long transactionId);
}
