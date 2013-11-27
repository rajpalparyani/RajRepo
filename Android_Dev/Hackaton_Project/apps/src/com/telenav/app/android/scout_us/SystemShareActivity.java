/**
 *
 * Copyright 2013 Telenav, Inc. All rights reserved.
 * SystemShareActivity.java
 *
 */
package com.telenav.app.android.scout_us;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.telenav.app.TeleNavDelegate;
import com.telenav.app.android.constant.IntentExtra;
import com.telenav.logger.Logger;
import com.telenav.sdk.kontagent.KontagentLogger;

/**
 * We query all the supported native apps from the system using {@link Intent#ACTION_SEND} intent and list all of them
 * in the result activity/view. User can choose one of them to share the message.
 * 
 * @author wchshao (wchshao@telenav.cn)
 * @date Jan 23, 2013
 */
public class SystemShareActivity extends Activity
{
    private static final String TAG = SystemShareActivity.class.getSimpleName();

    private StateHolder stateHolder;

    private ShareAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.share);
        setTitle(getString(R.string.commonShare));

        Object retained = getLastNonConfigurationInstance();
        if (retained != null && retained instanceof StateHolder)
        {
            stateHolder = (StateHolder) retained;
        }
        else
        {
            stateHolder = new StateHolder();
            Bundle bundle = getIntent().getExtras();
            if (bundle != null)
            {
                stateHolder.title = bundle.getCharSequence(IntentExtra.TITLE);
                stateHolder.subject = bundle.getCharSequence(IntentExtra.SUBJECT);
                stateHolder.content = bundle.getCharSequence(IntentExtra.CONTENT);
                stateHolder.htmlContent = bundle.getCharSequence(IntentExtra.HTML_CONTENT);
            }
            else
            {
                finish();
                Logger.log(Logger.EXCEPTION, TAG, "No intent params!");
                return;
            }
        }

        createUi();
    }

    @Override
    public Object onRetainNonConfigurationInstance()
    {
        return stateHolder;
    }

    @Override
    public void setTitle(CharSequence title)
    {
        TextView tvTitle = (TextView) findViewById(android.R.id.title);
        tvTitle.setText(title);
        super.setTitle(title);
    }

    private void createUi()
    {
        if (!TextUtils.isEmpty(stateHolder.title))
        {
            setTitle(stateHolder.title);
        }
        listAdapter = new ShareAdapter(this, findAppsForSharing());

        ListView listView = (ListView) findViewById(R.id.select_dialog_listview);
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                launchAppIntent(position);
            }
        });
    }

    private void launchAppIntent(int position)
    {
        ResolveInfo launchable = listAdapter.getItem(position);
        
        String appName = launchable.loadLabel(getPackageManager()).toString();
        logKtShareEvent(appName);
        
        ActivityInfo activity = launchable.activityInfo;
        ComponentName componentName = new ComponentName(activity.applicationInfo.packageName, activity.name);

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setComponent(componentName);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, !TextUtils.isEmpty(stateHolder.subject) ? stateHolder.subject
                : getString(R.string.commonShare));
        if(appName != null && appName.toLowerCase().contains("mail"))
        {
            intent.setType("message/rfc822");
            intent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(stateHolder.content.toString()));
        }
        else
        {
            intent.putExtra(Intent.EXTRA_TEXT, stateHolder.content);
        }
        try
        {
            startActivityForResult(intent, TeleNavDelegate.LAUNCH_SHARE_REQUESTCODE);
        }
        catch (Throwable t)
        {
            // there is no activity to handle this intent.
            Logger.log(Logger.EXCEPTION, TAG, "No activity to handle the share intent.");
            finish();
        }
    }

    private void logKtShareEvent(String launchableName)
    {
        if(launchableName == null || launchableName.length() == 0)
            return;
        if(launchableName.contains("Gmail"))
        {
            KontagentLogger.getInstance().getInstance().addCustomEvent(KontagentLogger.CATEGORY_SHARE, KontagentLogger.SHARE_GMAIL_CLICKED);
        }
        else if(launchableName.contains("Twitter"))
        {
            KontagentLogger.getInstance().getInstance().addCustomEvent(KontagentLogger.CATEGORY_SHARE, KontagentLogger.SHARE_TWITTER_CLICKED);
        }
        else if(launchableName.contains("Google+"))
        {
            KontagentLogger.getInstance().getInstance().addCustomEvent(KontagentLogger.CATEGORY_SHARE, KontagentLogger.SHARE_GOOGLEPLUS_CLICKED);
        }
        else if(launchableName.contains("Friend Stream"))
        {
            KontagentLogger.getInstance().getInstance().addCustomEvent(KontagentLogger.CATEGORY_SHARE, KontagentLogger.SHARE_FRIEND_STREAM_CLICKED);
        }
        else if(launchableName.contains("Notes"))
        {
            KontagentLogger.getInstance().getInstance().addCustomEvent(KontagentLogger.CATEGORY_SHARE, KontagentLogger.SHARE_NOTES_CLICKED);
        }
        else if(launchableName.contains("Keep"))
        {
            KontagentLogger.getInstance().getInstance().addCustomEvent(KontagentLogger.CATEGORY_SHARE, KontagentLogger.SHARE_KEEP_CLICKED);
        }
        else if(launchableName.contains("Drive"))
        {
            KontagentLogger.getInstance().getInstance().addCustomEvent(KontagentLogger.CATEGORY_SHARE, KontagentLogger.SHARE_GDRIVE_CLICKED);
        }
        else if(launchableName.contains("Bluetooth"))
        {
            KontagentLogger.getInstance().getInstance().addCustomEvent(KontagentLogger.CATEGORY_SHARE, KontagentLogger.SHARE_BLUETOOTH_CLICKED);
        }
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == TeleNavDelegate.LAUNCH_SHARE_REQUESTCODE)
        {
            setResult(resultCode, data);
            finish();
        }
        else
        {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    /**
     * Query the possible share items from the system. TODO: Could be a little laggy if there are too many items. Should
     * be put into another thread before release to avoid ANR problem.
     */
    private List<ResolveInfo> findAppsForSharing()
    {
        Intent intent = new Intent(Intent.ACTION_SEND, null);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setType("text/plain");
        List<ResolveInfo> activities = getPackageManager().queryIntentActivities(intent, 0);
        // put the items into a TreeMap to get them sorted using their names' natural order
        TreeMap<String, ResolveInfo> alpha = new TreeMap<String, ResolveInfo>();
        for (ResolveInfo it : activities)
        {
            alpha.put(it.loadLabel(getPackageManager()).toString(), it);
        }

        return new ArrayList<ResolveInfo>(alpha.values());
    }

    /** implement an ArrayAdapter to show the share items */
    private class ShareAdapter extends ArrayAdapter<ResolveInfo>
    {

        public ShareAdapter(Context context, List<ResolveInfo> apps)
        {
            super(context, R.layout.share_list_item, apps);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            if (convertView == null)
            {
                convertView = createShareItemView(parent);
            }
            bindView(position, convertView);
            return (convertView);
        }

        private View createShareItemView(ViewGroup parent)
        {
            return (getLayoutInflater().inflate(R.layout.share_list_item, parent, false));
        }

        private void bindView(int position, View view)
        {
            PackageManager packageManager = getPackageManager();
            ImageView icon = (ImageView) view.findViewById(R.id.share0ActivityIcon);
            icon.setImageDrawable(getItem(position).loadIcon(packageManager));
            TextView label = (TextView) view.findViewById(R.id.share0ActivityName);
            label.setText(getItem(position).loadLabel(packageManager));
        }
    }

    /** data to save and retrieve when the activity is destroyed and recreated by the system */
    private static class StateHolder
    {
        /** share subject */
        public CharSequence subject;

        /** share content */
        public CharSequence content;

        /** title of the this activity */
        public CharSequence title;
        
        /** html content of the this activity */
        public CharSequence htmlContent;
    }
    
    public void onBackPressed() 
    {
        KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_SHARE,
            KontagentLogger.SHARE_CANCEL_CLICKED);
        super.onBackPressed();
    }
}
