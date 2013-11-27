/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * WidgetAdapterActivity.java
 *
 */
package com.telenav.searchwidget.android;

import android.content.ComponentName;

import com.telenav.sdk.maitai.android.MaiTaiAdapterActivity;

/**
 * @author xinrongl (xinrongl@telenav.com)
 * @date Aug 3, 2011
 */

public class WidgetAdapterActivity extends MaiTaiAdapterActivity
{
	@Override
    public ComponentName getTargetComponentName()
    {
        ComponentName componentName = new ComponentName(getApplicationContext(), getApplicationContext().getPackageName() + ".MaiTaiListener");
        return componentName;
    }
}
