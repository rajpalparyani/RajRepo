/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * Main.java
 *
 */
package com.telenav;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;

import org.json.tnme.JSONObject;

import com.telenav.app.HostList;
import com.telenav.logger.DefaultLoggerFilter;
import com.telenav.logger.Logger;
import com.telenav.network.TnNetworkManager;
import com.telenav.network.j2se.J2seNetworkManager;
import com.telenav.persistent.ITnPersistentContext;
import com.telenav.persistent.TnFileConnection;
import com.telenav.persistent.TnPersistentManager;
import com.telenav.persistent.j2se.J2sePersistentManager;

/**
 *@author qli
 *@date 2011-4-11
 */
public class Main
{
    protected static final String fileCache = ".cache_file";
    protected static final int ENVIRONMENT_FILE_INDEX = 0;
    
    protected static boolean printAll = false;
    protected static boolean printUrlName = false;
    protected static boolean execEnv = false;
    protected static boolean loadFile = false;
    protected static String fileName = null;
    protected static String url = null;
    protected static int urlGroupId = -1;

    /**
     * @param args
     */
    public static void main(String[] args)
    {
        // TODO Auto-generated method stub
        Logger.start();
        Logger.add(new ValidateTracking(), new DefaultLoggerFilter(Logger.INFO, "*"));
        
        TnNetworkManager.init(new J2seNetworkManager());
        TnPersistentManager.init(
            new J2sePersistentManager(System.getProperty("user.dir")), 
            new ITnPersistentContext(){
                public String getApplicationName()
                {
                    return "ValidateEnv";
                }
            });
        if ( parseArgs(args) )
        {
            Object env = loadEnv();
            
            if ( execEnv )
            {
                ValidateEnv test = new ValidateEnv(env, printAll);
                test.validateEnv();
            }
        }
        
        Logger.shutdown();
    }
    
    protected static boolean parseArgs(String[] args)
    {
        if ( args == null || args.length == 0 )
        {
            return false;
        }
        for ( int i=0 ; i<args.length ; i++ )
        {
            if ( "-v".equalsIgnoreCase(args[i]) )
            {
                printAll = true;
                execEnv = true;
                if ( ++i < args.length )
                {
                    urlGroupId = Integer.parseInt(args[i]);
                }
                else
                {
                    log("Please input id number!");
                    return false;
                }
            }
            else if( "-l".equalsIgnoreCase(args[i]) )
            {
                printUrlName = true;
            }
            else if( "-e".equalsIgnoreCase(args[i]) )
            {
                execEnv = true;
                if ( ++i < args.length )
                {
                    urlGroupId = Integer.parseInt(args[i]);
                }
                else
                {
                    log("Please input id number!");
                    return false;
                }
            }
        }
        return true;
    }
    
    protected static Object loadEnv()
    {
        String[][] server_urls = HostList.getHostUrls();
        String[] groups = new String[server_urls[0].length-1];
        
        if ( printUrlName )
        {
            for (int i = 0; i < server_urls.length; i++)
            {
                log("["+i+"]    "+server_urls[i][0]);
            }
        }
        
        if ( execEnv )
        {
            if( urlGroupId < 0 || urlGroupId >= groups.length )
            {
                log("Please select 0~"+(groups.length-1));
            }
            for (int i = 0; i < groups.length; i++)
            {
                groups[i] = server_urls[urlGroupId][i+1];
            }
        }
    
        return groups;
    }
    
    
    
    protected static Object loadFile()
    {
        if ( loadFile )
        {
            try
            {
                TnFileConnection file = TnPersistentManager.getInstance().openFileConnection(
                    "", fileName, TnPersistentManager.FILE_SYSTEM_INTERNAL);
                if ( file.exists() )
                {
                    TnFileConnection cache = TnPersistentManager.getInstance().openFileConnection(
                        "", fileCache, TnPersistentManager.FILE_SYSTEM_INTERNAL);
                    InputStream is = file.openInputStream();
                    OutputStream os = cache.openOutputStream();
                    int i = -1;
                    while ( (i=is.read()) != -1 )
                    {
                        os.write(i);
                    }
                    os.flush();
                    os.close();
                    is.close();
                    cache.close();
                    file.close();
                    
                }
                else
                {
                    log(fileName+" : file not exists!");
                }
            }
            catch (Exception e)
            {
                log("load file "+fileName+" fail!");
                Logger.log("com.telenav.Main", e);
            }
            return null;
        }
        try
        {
            TnFileConnection file = TnPersistentManager.getInstance().openFileConnection(
                "", fileCache, TnPersistentManager.FILE_SYSTEM_INTERNAL);
            if ( file.exists() )
            {
                InputStream is = file.openInputStream();
                JSONObject json = new JSONObject(Util.read(is));
                log("Environment numbers: "+json.length());
                String[] keys = new String[json.length()];
                Enumeration en = json.keys();
                for ( int i=0 ; en.hasMoreElements() ; i++ )
                {
                    keys[i] = (String)en.nextElement();
                    if ( printUrlName )
                    {
                        log("["+i+"]    "+keys[i]);
                    }
                }
                is.close();
                file.close();
                if ( execEnv )
                {
                    return json.get(keys[urlGroupId]);
                }
            }
            else
            {
                log(fileName+" : file not exists!");
            }
        }
        catch (Exception e)
        {
            log("load cache file fail!");
            Logger.log("com.telenav.Main", e);
        }
        return null;
    }
    
    protected static void log(Object o) {
        Logger.log(Logger.INFO, "com.telenav.Main", "" + o);
    }

}
