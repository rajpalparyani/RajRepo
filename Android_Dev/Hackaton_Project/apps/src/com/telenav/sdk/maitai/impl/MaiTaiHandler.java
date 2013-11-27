/**
 * 
 */
package com.telenav.sdk.maitai.impl;

import java.util.Hashtable;

import com.telenav.logger.Logger;
import com.telenav.sdk.maitai.IMaiTai;
import com.telenav.sdk.maitai.IMaiTaiHandlerListener;
import com.telenav.sdk.maitai.IMaiTaiParameter;
import com.telenav.sdk.plugin.PluginDataProvider;
import com.telenav.threadpool.IJob;


/**
 * @author xinrongl
 *
 * @modifier qli  2010-12-01
 * porting from MaiTaiHandler.java in tn6.2
 */

public class MaiTaiHandler implements IJob
{
    private static final String AAB_SCHEME   = "http://tln.me";
    private IMaiTaiHandlerListener handlerListener;
    
    private Hashtable results = new Hashtable();
    
    private String uri = null;
    private String backupUri = null;
    
    private boolean isCancelled = false;
    private boolean isRunning = false;
    
    private boolean isNeedValidate = true;
    
    private MaiTaiHandler()
    {
    }
    
    private static MaiTaiHandler instance = new MaiTaiHandler();
    
    public static MaiTaiHandler getInstance()
    {
        return instance;
    }

    public void addResult(String key, String value)
    {
        results.put(key, value);
    }
    
    public void clear()
    {
        PluginDataProvider.getInstance().clear();
        isRunning = false;
        isCancelled = false;
        uri = null;
        backupUri = null;
    }

    protected void finish()
    {
        MaiTaiManager.getInstance().finish(results);
    }

    protected void getRequest()
    {
        results.clear();
        String uri = this.uri;
        if (uri != null && uri.toLowerCase().startsWith(AAB_SCHEME))
        {
            uri = LookupAabUri.lookup(uri);
        }
        if (uri == null || uri.trim().length() == 0)
        {
            uri = this.backupUri;
        }
        
        if (uri == null || uri.trim().length() == 0 || isCancelled )
        {
            isCancelled = true;
            return;
        }
        else
        {
            MaiTaiManager.getInstance().getMaiTaiParameter().notify(uri);
            if( isCancelled )
            {
                return;
            }
            String action = MaiTaiManager.getInstance().getMaiTaiParameter().getAction();
            Hashtable data = new Hashtable();
            if (IMaiTaiParameter.ACTION_NAVTO.equalsIgnoreCase(action) 
                    || IMaiTaiParameter.ACTION_DIRECTIONS.equalsIgnoreCase(action)
                    || IMaiTaiParameter.ACTION_MAP.equalsIgnoreCase(action)
                    || IMaiTaiParameter.ACTION_SEARCH.equalsIgnoreCase(action)
                    || IMaiTaiParameter.ACTION_VIEW.equalsIgnoreCase(action))
            {
                data.put(IMaiTai.KEY_MAITAI_NEED_LOGIN, "true");
            }
            data.put(IMaiTai.KEY_IS_MAITAI_CALL, "true");
            PluginDataProvider.getInstance().initialize(data);
        }
    }


    public void cancel()
    {
        isCancelled = true;
    }

    public void execute(int arg0)
    {
        isRunning = true;
        try
        {
            getRequest();
        }
        catch(Exception e)
        {
            Logger.log(this.getClass().getName(), e);
            isCancelled = true;
        }
        
//        if( !isCancelled )
//        {
            handlerListener.maitaiHandleFinished();
//        }else{
//            this.finish();
//        }
        isRunning = false;
    }

    public boolean isCancelled()
    {
        return isCancelled;
    }

    public boolean isRunning()
    {
        return isRunning;
    }

    public void setMaiTaiHandlerListener(IMaiTaiHandlerListener listener)
    {
        handlerListener = listener;
    }
    
    protected void setUri(String uri)
    {
        this.uri = uri;
    }
    
    protected void setBackupUri(String uri)
    {
        this.backupUri = uri;
    }
    
    public Hashtable getResult()
    {
        return results;
    }
    
    public boolean isNeedValidate()
    {
        return isNeedValidate;
    }
    
    public void setNeedValidata(boolean needValidate)
    {
        this.isNeedValidate = needValidate;
    }
}
