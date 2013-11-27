/**
 *
 * Copyright 2013 TeleNav, Inc. All rights reserved.
 * ShareManager.java
 *
 */
package com.telenav.module.share;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Vector;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import com.telenav.app.AbstractContactProvider.TnContact;
import com.telenav.app.TeleNavDelegate;
import com.telenav.app.android.AndroidPersistentContext;
import com.telenav.app.android.constant.IntentExtra;
import com.telenav.app.android.scout_us.MixedShareActivity;
import com.telenav.app.android.scout_us.NativeShareItem;
import com.telenav.app.android.scout_us.R;
import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.dao.misc.SimpleConfigDao;
import com.telenav.data.datatypes.address.Address;
import com.telenav.data.datatypes.address.Stop;
import com.telenav.mvc.ICommonConstants;
import com.telenav.res.ResourceManager;
import com.telenav.tnui.core.android.AndroidActivity;
import com.telenav.tnui.core.android.AndroidActivity.IAndroidActivityResultCallback;

/**
 * @author wchshao
 * @date 2013-3-7
 */
public class ShareManager implements IAndroidActivityResultCallback, ICommonConstants
{

    public interface IShareCallback
    {
        public void onShareResult(boolean success);
    }

    private static ShareManager instance = new ShareManager();

    private IShareCallback callback;
    
    private boolean isSharing = false;
    
    private static int VIRGIN = -1;
    
    private static int NONVIRGIN = 0;

    public static ShareManager getInstance()
    {
        return instance;
    }

    /**
     * Start a location/address share request. Input a callback if share result (success or not) is needed.
     * @param title the title to show in the share popup
     * @param address the raw address to share
     * @param S4ATinyURL the Scount for Apps tiny url
     * @param callback the callback to get informed the sharing is successful or not
     */
    public void startShareLocation(String title, Address address, String S4ATinyURL, IShareCallback callback)
    {
        isSharing = true;
        this.callback = callback;
        if (S4ATinyURL == null)
        {
            S4ATinyURL = "";
        }
        AndroidActivity context = (AndroidActivity) AndroidPersistentContext.getInstance().getContext();
        Intent intent = new Intent(context, MixedShareActivity.class);
        intent.putExtra(IntentExtra.TITLE, title);
        intent.putExtra(IntentExtra.NATIVE_SHARE_ITEM, createShareLocationList(address, S4ATinyURL));
        intent.putExtra(IntentExtra.MIXED, true);
        if (callback != null)
        {
            context.setActivityResultCallback(this);
            context.startActivityForResult(intent, TeleNavDelegate.LAUNCH_SHARE_REQUESTCODE);
        }
        else
        {
            context.startActivity(intent);
        }
    }
    
    public Serializable createShareLocationList(Address address, String S4ATinyURL)
    {
        ArrayList<NativeShareItem> shareList = new ArrayList<NativeShareItem>();
        
        String subject = getSubject(address);
        String htmlContent = getLocationContent(address, S4ATinyURL);
        String shortContent = getLocationShortContent(address, S4ATinyURL);
        String twitterShortContent = getLocationShortTwitterContent(address, S4ATinyURL);
        
        NativeShareItem facebook = new NativeShareItem(NativeShareItem.NATIVE_SHARE_FACEBOOK, subject, shortContent);
        NativeShareItem twitter = new NativeShareItem(NativeShareItem.NATIVE_SHARE_ITEM_TWITTER, subject, twitterShortContent);
        NativeShareItem sms = new NativeShareItem(NativeShareItem.NATIVE_SHARE_ITEM_SMS, subject, shortContent);
        NativeShareItem email = new NativeShareItem(NativeShareItem.NATIVE_SHARE_ITEM_EMAIL, subject, "", shortContent);
        NativeShareItem more = new NativeShareItem(NativeShareItem.NATIVE_SHARE_ITEM_MORE, subject, shortContent, htmlContent);
        
        shareList.add(facebook);
        shareList.add(twitter);
        shareList.add(sms);
        shareList.add(email);
        shareList.add(more);
        
        return (Serializable)shareList;
    }
    
    public void startShareScout(String title, IShareCallback callback)
    {
        isSharing = true;
        this.callback = callback;
        AndroidActivity context = (AndroidActivity) AndroidPersistentContext.getInstance().getContext();
        Intent intent = new Intent(context, MixedShareActivity.class);
        intent.putExtra(IntentExtra.TITLE, title);
        intent.putExtra(IntentExtra.NATIVE_SHARE_ITEM, createShareScoutList(context));
        intent.putExtra(IntentExtra.MIXED, false);
        if (callback != null)
        {
            context.setActivityResultCallback(this);
            context.startActivityForResult(intent, TeleNavDelegate.LAUNCH_SHARE_REQUESTCODE);
        }
        else
        {
            context.startActivity(intent);
        }
    }
    
    public Serializable createShareScoutList(Context context)
    {
        ArrayList<NativeShareItem> shareList = new ArrayList<NativeShareItem>();
        
        NativeShareItem facebook = new NativeShareItem(NativeShareItem.NATIVE_SHARE_FACEBOOK, context.getString(R.string.share_scout_facebook_title), context.getString(R.string.share_scout_facebook_detail) + " " + context.getString(R.string.share_scout_facebook_URL));
        NativeShareItem twitter = new NativeShareItem(NativeShareItem.NATIVE_SHARE_ITEM_TWITTER, "", context.getString(R.string.share_scout_twitter_content));
        NativeShareItem sms = new NativeShareItem(NativeShareItem.NATIVE_SHARE_ITEM_SMS, "", context.getString(R.string.share_scout_sms_content));
        NativeShareItem email = new NativeShareItem(NativeShareItem.NATIVE_SHARE_ITEM_EMAIL, context.getString(R.string.share_scout_email_subject), context.getString(R.string.share_scout_email_content));
        
        shareList.add(facebook);
        shareList.add(twitter);
        shareList.add(sms);
        shareList.add(email);
        
        return (Serializable)shareList;
    }
    /**
     * Start an ETA share request. Input a callback if share result (success or not) is needed.
     * Try and catch the exception to process the case there is no SMS/Messages application on the device.
     * @param destinationName the destination name, using home if the destination is the home.
     * @param S4ATinyURL the Scount for Apps tiny url
     * @param destType the destination type, which will be used in the SMS default receiver. 
     * destType = 0 : normal address; 
     * destType = 1 : home address; 
     * destType = 2 : work address
     * @param callback the callback to get informed the sharing is successful or not
     * @param recipients TODO
     */
    public void startShareETA(String destinationName, String S4ATinyURL, int destType, IShareCallback callback, Vector recipients, String eta)
    {
        isSharing = true;
        this.callback = callback;
        
        AndroidActivity context = (AndroidActivity) AndroidPersistentContext.getInstance().getContext();

        StringBuffer ptnSb = new StringBuffer();
        String separator = "; ";
        if(android.os.Build.MANUFACTURER.toLowerCase().contains("samsung"))
        {
            separator = ", ";
        }

        for (int i = 0; i < recipients.size(); i++)
        {
            TnContact contact = (TnContact) recipients.elementAt(i);
            String tempStr = contact.phoneNumber;
            ptnSb.append(tempStr);
            if (i < recipients.size() - 1)
            {
                ptnSb.append(separator);
            }
        }
        
        Intent smsIntent = new Intent();
        smsIntent.setAction(Intent.ACTION_SENDTO);
        // content
       /* if address is Work/Home, receipts will be the default
        * if address from contact list, receipt will be the mobile of that contact
        * */
        smsIntent.setData(Uri.parse("smsto:" + ptnSb));
        smsIntent.putExtra("sms_body", getETAContent(destinationName, S4ATinyURL, eta));
        if (callback != null)
        {
            context.setActivityResultCallback(this);
            context.startActivityForResult(smsIntent, TeleNavDelegate.LAUNCH_SHARE_REQUESTCODE);
        }
        else
        {
            context.startActivity(smsIntent);
        }
    }
    
    /**
     * Pull out the share message subject from the raw address data
     * @param address
     * @return
     */
    private String getSubject(Address address)
    {
        Context context = AndroidPersistentContext.getInstance().getContext();
//        String name = address.getDisplayedText();
//        if (TextUtils.isEmpty(name))
//        {
//            name = ResourceManager.getInstance().getStringConverter().convertAddress(address.getStop(), false);
//        }
        String name = address.getName();
        if (name == null)
        {
            name = "";
        }
       
        if (name.trim().length() > 0)
        {
            String cityState = getCityAndProvince(address.getStop());
            return context.getString(R.string.share_location_subject_format, name+", "+cityState);
           
        }
        else
        {
            String detailAddress = ResourceManager.getInstance().getStringConverter().convertAddress(address.getStop(), false);
            return context.getString(R.string.share_location_subject_format, detailAddress);
        }
    }
    
    private String getLocationShortContent(Address address, String S4ATinyURL)
    {
        Context context = AndroidPersistentContext.getInstance().getContext();
        String name = address.getName();
        if (name == null)
        {
            name = "";
        }
       
        if (name.trim().length() > 0)
        {
            String cityState = getCityAndProvince(address.getStop());
            return context.getString(R.string.share_location_short_content_format, name+", ", cityState, S4ATinyURL);
           
        }
        else
        {
            String detailAddress = ResourceManager.getInstance().getStringConverter().convertAddress(address.getStop(), false);
            return context.getString(R.string.share_location_short_content_format, "", detailAddress, S4ATinyURL);
        }
    }
    
    private String getLocationShortTwitterContent(Address address, String S4ATinyURL)
    {
        Context context = AndroidPersistentContext.getInstance().getContext();
        String name = address.getName();
        if (name == null)
        {
            name = "";
        }
       
        if (name.trim().length() > 0)
        {
            return context.getString(R.string.share_location_short_content_twitter_format, name, S4ATinyURL);
        }
        else
        {
            String firstline = ResourceManager.getInstance().getStringConverter().convertStopFirstLine(address.getStop());
            return context.getString(R.string.share_location_short_content_twitter_format, firstline, S4ATinyURL);
        }
    }

    private String getLocationContent(Address address, String S4ATinyURL)
    {
        Context context = AndroidPersistentContext.getInstance().getContext();
        String name = address.getName();
        if (name == null)
        {
            name = "";
        }
        String detailAddress = ResourceManager.getInstance().getStringConverter().convertAddress(address.getStop(), false);
        if (name.trim().length() > 0)
        {
            if (detailAddress.contains(name))
            {
                name = "";
            }
        }
        StringBuilder sb = new StringBuilder();
        sb.append("<p><b>" + context.getString(R.string.share_location_content_prefix) + " ");
        if (name.trim().length() > 0)
        {
            sb.append(name);
            sb.append("<br />");
        }
        sb.append("</b>");
        String firstLine = getFirstline(address.getStop());
        if (!TextUtils.isEmpty(firstLine))
        {
            sb.append("<a href=\"" + S4ATinyURL + "\">" + firstLine + "</a><br />");
        }
        String cityProvince = getCityAndProvince(address.getStop());
        if (!TextUtils.isEmpty(cityProvince))
        {
            sb.append("<a href=\"" + S4ATinyURL + "\">" + cityProvince + "</a><br />");
        }
        sb.append("</p>");
        sb.append("<p>");
        sb.append(S4ATinyURL);
        sb.append("</p>");
        sb.append("<p>");
        sb.append(context.getString(R.string.share_location_content_suffix_1) + " ");
        String androidUrl = context.getString(R.string.android_download_url);
        sb.append("<a href=\"" + androidUrl + "\">" + context.getString(R.string.share_location_content_suffix_2) + "</a>");
        sb.append(context.getString(R.string.share_location_content_suffix_3) + " ");
        String iphoneUrl = context.getString(R.string.iphone_download_url);
        sb.append("<a href=\"" + iphoneUrl + "\">" + context.getString(R.string.share_location_content_suffix_4) + "</a>");
        sb.append(" " + context.getString(R.string.share_location_content_suffix_5));
        sb.append("</p>");
        return sb.toString();
//        return context.getString(R.string.share_location_content_format, name, detailAddress, S4ATinyURL);
    }

    public String getFirstline(Stop stop)
    {
        if(stop == null)
        {
            return "";
        }
        StringBuffer displayString = new StringBuffer(32);

        if (stop.getFirstLine() != null && stop.getFirstLine().length() > 0)
        {
            displayString.append(stop.getFirstLine());
        }
        return displayString.toString();
    }

    public String getCityAndProvince(Stop stop)
    {
        if(stop == null)
        {
            return "";
        }
        StringBuffer displayString = new StringBuffer(32);
        String city = stop.getCity();
        if (city != null)
        {
            city = city.trim();
            if (city.length() > 0)
            {
                displayString.append(city);
            }
        }
        String province = stop.getProvince();
        if (province != null)
        {
            province = province.trim();
            if (province.length() > 0)
            {
                if (displayString.length() > 0) // fix 8594
                {
                    displayString.append(", "); // fix 8924 zdong
                }
                displayString.append(province);
            }
        }
        return displayString.toString();
    }
    
    private String getETAContent(String destinationName, String tinyUrl, String eta)
    {
        SimpleConfigDao simpleConfigDao = ((DaoManager) DaoManager.getInstance()).getSimpleConfigDao();
        if(simpleConfigDao.getInteger(SimpleConfigDao.KEY_IS_REAL_TIME_SHARE_SETTED) == VIRGIN)
        {
            simpleConfigDao.put(SimpleConfigDao.KEY_IS_REAL_TIME_SHARE_ENABLED, true);
            simpleConfigDao.put(SimpleConfigDao.KEY_IS_REAL_TIME_SHARE_SETTED, true);
            simpleConfigDao.store();
        }
        boolean isShareOn = DaoManager.getInstance().getSimpleConfigDao().getBoolean(SimpleConfigDao.KEY_IS_REAL_TIME_SHARE_ENABLED);
        Context context = AndroidPersistentContext.getInstance().getContext();
        if(isShareOn)
        {
            return context.getString(R.string.share_eta_on_content_format, destinationName, eta, tinyUrl);
        }
        else
        {
            return context.getString(R.string.share_eta_off_content_format, destinationName, eta);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == TeleNavDelegate.LAUNCH_SHARE_REQUESTCODE)
        {
            isSharing = false;
            if (callback != null)
            {
                callback.onShareResult(resultCode == Activity.RESULT_OK);
            }
        }
    }

    public boolean isSharing()
    {
        return isSharing;
    }
}
