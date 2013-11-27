/**
 *
 * Copyright 2013 Telenav, Inc. All rights reserved.
 * MixedShareActivity.java
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
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;
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
 * Activity to show several share methods manually. If the {@link IntentExtra#MIXED} parameter is false, i.e. in
 * filtered mode, two kinds of native share intent are listed: SMS and Email. Facebook and other necessary methods to
 * share could be added to the list according to the needs of our typical users. In mixed mode, an additional item will
 * be at the end of the list that will show the system share list in a new activity/view if clicked.
 * 
 * @author wchshao (wchshao@telenav.cn)
 * @date Jan 23, 2013
 */
public class MixedShareActivity extends Activity
{
    private static final String TAG = MixedShareActivity.class.getSimpleName();

    private StateHolder stateHolder;

    private ShareAdapter listAdapter;
    
    private ResolveInfo facebookInfo;
    private ResolveInfo twitterInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.share);
        setTitle(getString(R.string.commonShare));

        // Get share metadata from the retained StateHolder if exists. Parse from the intent otherwise.
        Object retained = getLastNonConfigurationInstance();
        if (retained instanceof StateHolder)
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
                stateHolder.shareList =  (ArrayList<NativeShareItem>)bundle.getSerializable(IntentExtra.NATIVE_SHARE_ITEM);
                stateHolder.isMixed = bundle.getBoolean(IntentExtra.MIXED);
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
        listAdapter = new ShareAdapter(this, findAppsForSharing(stateHolder.shareList));

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
        ShareListItem launchable = listAdapter.getItem(position);
        if (launchable != null && launchable.shareIntent != null)
        {
            try
            {
                if(launchable.name != null && launchable.name.length() > 0)
                {
                    if(launchable.name.equalsIgnoreCase(getString(R.string.share_email)))
                    {
                        KontagentLogger.getInstance().getInstance().addCustomEvent(KontagentLogger.CATEGORY_SHARE, KontagentLogger.SHARE_EMAIL_CLICKED);
                    }
                    else if(launchable.name.equalsIgnoreCase(getString(R.string.share_twitter)))
                    {
                        KontagentLogger.getInstance().getInstance().addCustomEvent(KontagentLogger.CATEGORY_SHARE, KontagentLogger.SHARE_TWITTER_CLICKED);
                    }
                    else if(launchable.name.equalsIgnoreCase(getString(R.string.share_facebook)))
                    {
                        KontagentLogger.getInstance().getInstance().addCustomEvent(KontagentLogger.CATEGORY_SHARE, KontagentLogger.SHARE_FACEBOOK_CLICKED);
                    }
                    else if(launchable.name.equalsIgnoreCase(getString(R.string.share_sms)))
                    {
                        KontagentLogger.getInstance().getInstance().addCustomEvent(KontagentLogger.CATEGORY_SHARE, KontagentLogger.SHARE_SMS_CLICKED);
                    }
                    else if(launchable.name.equalsIgnoreCase(getString(R.string.share_more)))
                    {
                        KontagentLogger.getInstance().getInstance().addCustomEvent(KontagentLogger.CATEGORY_SHARE, KontagentLogger.SHARE_MORE_CLICKED);
                    }
                   
                }
                startActivityForResult(launchable.shareIntent, TeleNavDelegate.LAUNCH_SHARE_REQUESTCODE);
            }
            catch (Throwable t)
            {
                // there is no activity to handle this intent.
                Logger.log(Logger.EXCEPTION, TAG, "No activity to handle the share intent.");
                finish();
            }
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
     * Add the filtered share item manually into the list. The "More" item is added if this activity is launched in this
     * mode. The {@link SystemShareActivity} is reused here to show the system share item list.
     */
    private List<ShareListItem> findAppsForSharing(ArrayList<NativeShareItem> shareList)
    {
        ArrayList<ShareListItem> shareArray = new ArrayList<MixedShareActivity.ShareListItem>();
        findFacebookTwitter();
        for (NativeShareItem shareItem : shareList)
        {
            switch (shareItem.getItemType())
            {
                case NativeShareItem.NATIVE_SHARE_FACEBOOK:
                {
                    if (facebookInfo != null)
                    {
                        // facebook is installed
                        shareArray.add(createFacebookItem(facebookInfo, shareItem));
                    }
                    break;
                }
                case NativeShareItem.NATIVE_SHARE_ITEM_TWITTER:
                {
                    if (twitterInfo != null)
                    {
                        // twitter is installed
                        shareArray.add(createTwitterItem(twitterInfo, shareItem));
                    }
                    break;
                }
                case NativeShareItem.NATIVE_SHARE_ITEM_SMS:
                {
                    // add send sms share item
                    shareArray.add(createSMSItem(shareItem));
                    break;
                }
                case NativeShareItem.NATIVE_SHARE_ITEM_EMAIL:
                {
                    // add send mail share item
                    shareArray.add(createMailItem(shareItem));
                    break;
                }
                case NativeShareItem.NATIVE_SHARE_ITEM_MORE:
                {
                    if (stateHolder.isMixed)
                    {
                        // add system share item, whose name is more
                        shareArray.add(createMoreItem(shareItem));
                    }
                    break;
                }
                default:
                    break;
            }
        }
        return shareArray;
    }
    
    /**
     * Query the possible share items from the system. TODO: Could be a little laggy if there are too many items. Should
     * be put into another thread before release to avoid ANR problem.
     */
    private List<ResolveInfo> findAppsForSharingFromSystem()
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
    
    private void findFacebookTwitter()
    {
        facebookInfo = null;
        twitterInfo = null;
        List<ResolveInfo> activities = findAppsForSharingFromSystem();
        if (activities == null || activities.isEmpty())
        {
            return;
        }
        for (ResolveInfo launchable : activities)
        {
            ActivityInfo activity = launchable.activityInfo;
            if (activity.applicationInfo.packageName.equalsIgnoreCase("com.facebook.katana"))
            {
                facebookInfo = launchable;
            }
            else if (activity.applicationInfo.packageName.equalsIgnoreCase("com.twitter.android"))
            {
                twitterInfo = launchable;
            }

        }
    }

    /** create the facebook share item */
    private ShareListItem createFacebookItem(ResolveInfo facebookInfo, NativeShareItem shareItem)
    {
        ShareListItem itemFacebook = new ShareListItem();
        itemFacebook.loadedDrawable = facebookInfo.loadIcon(getPackageManager());
        itemFacebook.name = getString(R.string.share_facebook);
        Intent facebookIntent = new Intent();
        ActivityInfo activity = facebookInfo.activityInfo;
        ComponentName componentName = new ComponentName(
            activity.applicationInfo.packageName, activity.name);
        facebookIntent.setComponent(componentName);
        facebookIntent.setAction(Intent.ACTION_SEND);
        facebookIntent.setType("text/plain");
        facebookIntent.putExtra(Intent.EXTRA_SUBJECT, !TextUtils.isEmpty(shareItem.getSubject()) ? shareItem.getSubject()
                : getString(R.string.commonShare));
        facebookIntent.putExtra(Intent.EXTRA_TEXT, shareItem.getContent());
        itemFacebook.shareIntent = facebookIntent;
        return itemFacebook;
    }
    
    /** create the twitter share item */
    private ShareListItem createTwitterItem(ResolveInfo twitterInfo, NativeShareItem shareItem)
    {
        ShareListItem itemTwitter = new ShareListItem();
        itemTwitter.loadedDrawable = twitterInfo.loadIcon(getPackageManager());
        itemTwitter.name = getString(R.string.share_twitter);
        Intent twitterIntent = new Intent();
        ActivityInfo activity = twitterInfo.activityInfo;
        ComponentName componentName = new ComponentName(
            activity.applicationInfo.packageName, activity.name);
        twitterIntent.setComponent(componentName);
        twitterIntent.setAction(Intent.ACTION_SEND);
        twitterIntent.putExtra(Intent.EXTRA_SUBJECT, !TextUtils.isEmpty(shareItem.getSubject()) ? shareItem.getSubject()
                : getString(R.string.commonShare));
        twitterIntent.putExtra(Intent.EXTRA_TEXT, shareItem.getContent());
        itemTwitter.shareIntent = twitterIntent;
        return itemTwitter;
    }
    
    /** create the SMS share item */
    private ShareListItem createSMSItem(NativeShareItem shareItem)
    {
        ShareListItem itemSms = new ShareListItem();
        itemSms.iconRes = R.drawable.share_popup_icon_message_unfocused;
        itemSms.name = getString(R.string.share_sms);
        Intent smsIntent = new Intent();
        smsIntent.setAction(Intent.ACTION_SENDTO);
        // content
        smsIntent.setData(Uri.parse("smsto:" + ""));
        smsIntent.putExtra("sms_body", shareItem.getContent());
        itemSms.shareIntent = smsIntent;
        return itemSms;
    }

    /** create the Email share item */
    private ShareListItem createMailItem(NativeShareItem shareItem)
    {
        ShareListItem itemMail = new ShareListItem();
        itemMail.iconRes = R.drawable.share_popup_icon_mail_unfocused;
        itemMail.name = getString(R.string.share_email);
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, !TextUtils.isEmpty(shareItem.getSubject()) ? shareItem.getSubject()
                : getString(R.string.commonShare));
        if(shareItem.getHtmlContent() != null && shareItem.getHtmlContent().length() > 0)
        {
            emailIntent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(shareItem.getHtmlContent()));
        }
        else
        {
            emailIntent.putExtra(Intent.EXTRA_TEXT, shareItem.getContent());
        }
        emailIntent.setType("message/rfc822");
        itemMail.shareIntent = emailIntent;
        return itemMail;
    }

    /** create the more share item */
    private ShareListItem createMoreItem(NativeShareItem shareItem)
    {
        ShareListItem itemSystem = new ShareListItem();
        itemSystem.iconRes = R.drawable.share_popup_icon_more_unfocused;
        itemSystem.name = getString(R.string.share_more);
        Intent moreIntent = new Intent(this, SystemShareActivity.class);
        moreIntent.putExtra(IntentExtra.TITLE, stateHolder.title);
        moreIntent.putExtra(IntentExtra.SUBJECT, shareItem.getSubject());
        moreIntent.putExtra(IntentExtra.CONTENT, shareItem.getContent());
        moreIntent.putExtra(IntentExtra.HTML_CONTENT, shareItem.getHtmlContent());
        itemSystem.shareIntent = moreIntent;
        return itemSystem;
    }

    /** implement an ArrayAdapter to show the share items */
    private class ShareAdapter extends ArrayAdapter<ShareListItem>
    {

        public ShareAdapter(Context context, List<ShareListItem> apps)
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
            ImageView icon = (ImageView) view.findViewById(R.id.share0ActivityIcon);
            ShareListItem item = getItem(position);
            if (item.loadedDrawable != null)
            {
                icon.setImageDrawable(item.loadedDrawable);
            }
            else
            {
                icon.setImageDrawable(getResources().getDrawable(item.iconRes));
            }
            TextView label = (TextView) view.findViewById(R.id.share0ActivityName);
            label.setText(item.name);
        }
    }

    /** data to save and retrieve when the activity is destroyed and recreated by the system */
    private static class StateHolder
    {
        /** share item list of the this activity */
        public ArrayList<NativeShareItem> shareList;

        /** title of the this activity */
        public CharSequence title;

        /** indicate whether it's the mixed mode */
        public boolean isMixed;
    }

    /** custom data structure indicates a share item */
    private static class ShareListItem
    {
        public String name;

        public int iconRes;

        public Drawable loadedDrawable;

        public Intent shareIntent;
    }

    public void onBackPressed() 
    {
        KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_SHARE,
            KontagentLogger.SHARE_CANCEL_CLICKED);
        super.onBackPressed();
    }
}
