/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * RestartService.java
 *
 */
package com.telenav.app.android.scout_us;

import android.app.IntentService;
import android.content.Intent;

/**
 * @author xinrongl (xinrongl@telenav.com)
 * @date Apr 13, 2011
 */

public class RestartService extends IntentService
{

    public RestartService()
    {
        super("RestartService");
    }

    protected void onHandleIntent(Intent intent)
    {
        Intent exitAppIntent = new Intent();
        exitAppIntent.setAction("com.telenav.intent.action.EXIT_APP");
        this.sendBroadcast(exitAppIntent);

        try
        {
            Thread.sleep(2300);
        }
        catch(Exception e)
        {}
        
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.setClassName(this, TeleNav.class.getName());
        this.startActivity(intent);
    }

}
