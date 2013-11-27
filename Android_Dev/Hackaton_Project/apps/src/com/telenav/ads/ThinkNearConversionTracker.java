package com.telenav.ads;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings.Secure;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

public class ThinkNearConversionTracker
{
    private Context _context;

    private String _packageName;

    private String _token;

    private static String SHARED_PREFS_KEY = "thinknearPrefs";

    private static String TRACKING_URL = "http://static-conversions.thinknearhub.com/conversions";

    private static String LOGGING_TAG = "ThinkNear";

    public ThinkNearConversionTracker(String token)
    {
        _token = token;
    }

    public void reportAppOpen(Context appContext)
    {
        if (appContext == null)
        {
            return;
        }
        _context = appContext;
        _packageName = appContext.getPackageName();

        SharedPreferences settings = appContext.getSharedPreferences(SHARED_PREFS_KEY, 0);
        if (settings.getBoolean(_packageName + " tracked", false) == false)
        {
            new Thread(thinkNearTrackerThread).start();
        }
        else
        {
            Log.d(LOGGING_TAG, "Conversion already tracked");
        }
    }

    Runnable thinkNearTrackerThread = new Runnable()
    {
        public void run()
        {
            String androidId = Secure.getString(_context.getContentResolver(), Secure.ANDROID_ID);
            String androidIdMD5Digest = "";
            String androidIdSHA1Digest = "";

            try
            {
                androidIdMD5Digest = Utils.md5Hash(androidId);
                androidIdSHA1Digest = Utils.sha1Hash(androidId);
            }
            catch (Exception e)
            {
                Log.d(LOGGING_TAG, "Unable to hash android device id. Leaving blank.");
            }

            Log.d(LOGGING_TAG, "Tracking conversions: token=" + _token);
            Log.d(LOGGING_TAG, "SHA1=" + androidIdSHA1Digest);
            Log.d(LOGGING_TAG, "MD5=" + androidIdMD5Digest);

            DefaultHttpClient httpclient = new DefaultHttpClient();
            HttpResponse response;
            try
            {
                JSONObject object = new JSONObject();
                object.put("token", _token);
                object.put("tn_udid_md5", androidIdMD5Digest);
                object.put("tn_udid_sha1", androidIdSHA1Digest);
                object.put("tn_device_type", "android");

                HttpPost post = new HttpPost(TRACKING_URL);
                post.addHeader("Content-Type", "application/json");
                post.setEntity(new StringEntity(object.toString()));
                response = httpclient.execute(post);
            }
            catch (Exception e)
            {
                Log.d(LOGGING_TAG, e.toString());
                return;
            }

            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_CREATED)
            {
                Log.d(LOGGING_TAG, "Conversion tracking failed: non 201 code");
                return;
            }

            Log.d(LOGGING_TAG, "Conversion tracking successful");
            SharedPreferences.Editor editor = _context.getSharedPreferences(SHARED_PREFS_KEY, 0).edit();
            editor.putBoolean(_packageName + " tracked", true).commit();
        }
    };
}
