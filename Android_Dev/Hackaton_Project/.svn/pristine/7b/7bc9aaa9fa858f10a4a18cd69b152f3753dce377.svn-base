/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * ShortIconMapActivity.java
 *
 */
package com.telenav.app.android.scout_us;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import com.telenav.app.android.ShortcutManager;
import com.telenav.log.mis.IMisLogConstants;
import com.telenav.sdk.plugin.PluginDataProvider;

/**
 *@author htli
 *@date 2012-9-12
 */
public class ShortcutActivity extends Activity
{
    static final private String EXTRA_DUPLICATE = "duplicate";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
                
        if (Intent.ACTION_CREATE_SHORTCUT.equals(getIntent().getAction()))
        {
            setupShortcut();
        }
        else
        {
            startTelenav(); 
        }
        
        finish();
    }
    
    
    private void startTelenav()
    {
        Intent intent = new Intent(this, TeleNav.class);
        intent.setAction(ShortcutManager.ACTION_SHORTCUT);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(ShortcutManager.SHORTCUT_REQUEST, getAction());
            
        startActivity(intent);
    }
    
    private String getAction()
    {
        String action = "";
        String activityName = getIntent().getComponent().getShortClassName();
        
        if (ShortcutManager.ACTIVITY_NAME_MYMAP.equals(activityName))
        {
            action = ShortcutManager.SHORTCUT_MYMAP;
            PluginDataProvider.getInstance().setFromApp(IMisLogConstants.VALUE_BY_MAPS_SHORTCUT);
        }
        else if (ShortcutManager.ACTIVITY_NAME_MYPLACE.equals(activityName))
        {
            action = ShortcutManager.SHORTCUT_MYPLACE;
            PluginDataProvider.getInstance().setFromApp(IMisLogConstants.VALUE_BY_PLACES_SHORTCUT);
        }
        else if (ShortcutManager.ACTIVITY_NAME_MYDRIVE.equals(activityName))
        {
            action = ShortcutManager.SHORTCUT_MYDRIVE;
            PluginDataProvider.getInstance().setFromApp(IMisLogConstants.VALUE_BY_DRIVE_SHORTCUT);
        }
        
        return action;
    }
    
    private void setupShortcut() 
    {
        
        String activityName = getIntent().getComponent().getShortClassName();
        String shortcutName = "";
        Parcelable shortcutIcon = null;
        
        if (ShortcutManager.ACTIVITY_NAME_MYMAP.equals(activityName))
        {
            shortcutName = getString(R.string.maps_shortcut_name);
            shortcutIcon = Intent.ShortcutIconResource.fromContext(this,  R.drawable.app_icon_map);
        }
        else if (ShortcutManager.ACTIVITY_NAME_MYPLACE.equals(activityName))
        {
            shortcutName = getString(R.string.places_shortcut_name);
            shortcutIcon = Intent.ShortcutIconResource.fromContext(this,  R.drawable.app_icon_place);
        }
        else if (ShortcutManager.ACTIVITY_NAME_MYDRIVE.equals(activityName))
        {
            shortcutName = getString(R.string.nav_shortcut_name);
            shortcutIcon = Intent.ShortcutIconResource.fromContext(this,  R.drawable.app_icon_drive);
        }
        
        Intent shortcutIntent = new Intent(Intent.ACTION_MAIN);
        String className = this.getPackageName() + activityName;
        shortcutIntent.setClassName(this, className);
        shortcutIntent.putExtra(EXTRA_DUPLICATE, false);
        
        Intent intent = new Intent();
        intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, shortcutName);
        intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, shortcutIcon);
        intent.putExtra(EXTRA_DUPLICATE, false);
        
        setResult(RESULT_OK, intent);
    }
    
}
