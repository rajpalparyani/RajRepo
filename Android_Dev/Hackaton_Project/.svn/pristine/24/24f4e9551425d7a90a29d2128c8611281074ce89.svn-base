/**
 * 
 */
package com.telenav.app.android.scout_us;

import java.util.HashMap;
import java.util.Hashtable;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;

import com.telenav.logger.Logger;
import com.telenav.sdk.maitai.IMaiTaiParameter;
import com.telenav.sdk.maitai.IMaiTaiStatusConstants;
import com.telenav.sdk.maitai.impl.MaiTaiManager;

/**
 * @author qli
 * @date 2010-12-22
 *
 */
public class MaiTaiListener extends Activity
{
    static final String ACTION_MAITAI = "com.telenav.intent.action.maitai";
    static final String ACTION_MAITAI_ADAPTER = "com.telenav.intent.action.maitai.adapter";
    static final String CATEGORY_DEFAULT = "android.intent.category.DEFAULT";
    static final String CATEGORY_BROWSABLE = "android.intent.category.BROWSABLE";
    
    
    private BroadcastReceiver receiver = new BroadcastReceiver()
    {
        public void onReceive(Context context, Intent intent)
        {
            if ( intent.getAction().equals(TeleNav.ACTION_MAITAI_RESPONSE) )
            {
                Object response = null;
                try
                {
                    if (intent.getExtras() != null)
                    {
                        response = intent.getExtras().get(TeleNav.MAITAI_RESPONSE);
                    }
                }
                catch(Exception e)
                {
                    Logger.log(this.getClass().getName(), e);
                }
                catchResponse(response);
                return;
            }
        }
    };
    
    
    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        IntentFilter filter = new IntentFilter(TeleNav.ACTION_MAITAI_RESPONSE);
        filter.addCategory(TeleNav.CATEGORY_MAITAI);
        this.registerReceiver(receiver, filter);
        catchMaiTai();
    }
    
    public void onRestart()
    {
        super.onRestart();
        catchResponse(null);
    }
    
    public void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);
    }
    
    public void onDestroy()
    {
        unregisterReceiver(receiver);
        super.onDestroy();
        android.os.Process.killProcess(android.os.Process.myPid());
    }
    
    private void catchMaiTai()
    {
        Intent action = this.getIntent();
        if ( action.getAction().equalsIgnoreCase(ACTION_MAITAI) )
        {
            String uri = action.getExtras().getString(MaiTaiManager.PARAM_URI);
            Intent intent = new Intent();
            intent.setAction(TeleNav.ACTION_MAITAI);
            intent.addCategory(TeleNav.CATEGORY_TELENAV);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(MaiTaiManager.PARAM_URI, uri);
            this.startActivity(intent);
        }
        else if (action.hasCategory(CATEGORY_BROWSABLE))
        {
            //Shorten Url.
            Uri uri = action.getData();
            Intent intent = new Intent();
            intent.setAction(TeleNav.ACTION_MAITAI);
            intent.addCategory(TeleNav.CATEGORY_TELENAV);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setData(uri);
            this.startActivity(intent);
        }
    }
    
    void catchResponse(Object response)
    {
        Intent intent = new Intent();
        intent.setAction(ACTION_MAITAI_ADAPTER);
        intent.addCategory(CATEGORY_DEFAULT);
        if( response == null )
        {
            Hashtable results = new Hashtable();
            results.put(IMaiTaiParameter.KEY_STATUS, "" + IMaiTaiStatusConstants.STATUS_SUCCESS);
            intent.putExtra(TeleNav.MAITAI_RESPONSE, results);
        }
        else if( response instanceof HashMap )
        {
            intent.putExtra(TeleNav.MAITAI_RESPONSE, (HashMap)response);
        }
        this.setResult(0, intent);
        this.finish();
    }
    
}
