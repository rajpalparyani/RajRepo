/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * MaiTaiModel.java
 *
 */
package com.telenav.sdk.maitai.view;


import com.telenav.data.serverproxy.AbstractServerProxy;
import com.telenav.module.browsersdk.BrowserSdkModel;

/**
 *@author gbwang
 *@date 2011-8-5
 */
public class MaiTaiViewModel extends BrowserSdkModel implements IMaiTaiViewConstants
{

    protected void doActionDelegate(int actionId)
    {
        switch (actionId)
        {
            case IMaiTaiViewConstants.ACTION_INIT:
            {
                this.postModelEvent(EVENT_MODEL_LAUCH_MAITAI_VIEW);
                break;
            }   
            default:
                break;
        }
    }

	protected void transactionFinishedDelegate(AbstractServerProxy proxy, String jobId)
	{
		// TODO Auto-generated method stub
		
	}
}
