/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * RTTMisLoghelper.java
 *
 */
package com.telenav.log.mis.helper;

import java.util.Hashtable;

import com.telenav.log.mis.RadioStatusLogger;
import com.telenav.log.mis.log.AbstractMisLog;
import com.telenav.log.mis.log.RttMisLog;
import com.telenav.radio.TnCellInfo;
import com.telenav.radio.TnRadioManager;
import com.telenav.util.StringTokenizer;

/**
 *@author Casper (pwang@telenav.cn)
 *@date 2010-12-16
 */
public class RTTMisLogHelper extends AbstractMisLogHelper
{
    private static String RTTID = "RTTID";

    private static String M_ALL = "#A";

    private static String M_INSIDE = "#I";

    private static String M_UNIQUE = "#U";

    private static String delimeter = "/";

    private static String paramDelimeter = ";";

    private Hashtable ruleTable = new Hashtable();

    
    
    /*******************************************************************
     * String format from server side:
     * entry=ActionId;[MisActionId|MisActionName];[#A|#I|#U];AppName;URL 
     *         int             int|String            String  String String 
     * example=91929394;705;#U;poi_service;/touch/queryPoi.do
     *      unique entry, log poi_search only 
     * example=2;RouteRequest;#U;map_mp_cserver;/cserver
     *      unique entry, log route requests only
     * example=91929394;;#A;movie;
     *      log all movie requests 
     * example=91929394;;#I;poi_service 
     *      log all poi requests that has "RTTID" in the URL
     ********************************************************************/
    
    public void addRule(String rule)
    {
        StringTokenizer params = new StringTokenizer(rule, paramDelimeter);
        String actionId = params.nextToken();
        String misAction = params.nextToken();
        String modifier = params.nextToken();
        String appName = params.nextToken(); //if something missing it will through NPE and then skipped this entry
        String url = params.nextToken();// optinal info

        String key = actionId + "_" + appName;
        Hashtable table = (Hashtable) ruleTable.get(key);

        if (table == null)
        {
            table = new Hashtable();
            ruleTable.put(key, table);
        }

        if (M_ALL.equals(modifier))
        {
            table.put(M_ALL, misAction);
        }
        else if (M_INSIDE.equals(modifier))
        {
            table.put(M_INSIDE, misAction);
        }
        else if (M_UNIQUE.equals(modifier))
        {
            table.put(url, misAction);
        }
        else
        {
            // just skip entry don't know what to do
            return;
        }
    }

    public boolean isLogEnable(AbstractMisLog mislog)
    {
        if(!(mislog instanceof RttMisLog))
        {
            return false;
        }
        RttMisLog log = (RttMisLog) mislog;
        if(log.getRequestFile() == null || log.getRequestFile().length() == 0 || log.getRequestID() == null || log.getRequestID().length() == 0)
        {
            return false;
        }
        String actionId =getRealActionId(log.getRequestID());
        String appInfo = log.getRequestFile();
        if (appInfo.startsWith("/"))
        {
            appInfo = appInfo.substring(1);
        }
        String appName = appInfo;// webApp
        String url = "";// ServletName
        int index = appInfo.indexOf("/");
        if (index != -1)
        {
            appName = appInfo.substring(0, index);
            url = appInfo.substring(index + 1);
        }

        String key = actionId + "_" + appName;
        try
        {
            Hashtable table = (Hashtable) ruleTable.get(key);
            if (table == null)
            {
                return false;
            }
            if (table.containsKey(M_ALL))
            {
                return true;
            }
            if (table.containsKey(url))
            {
                return true;
            }
            if (table.containsKey(M_INSIDE))
            {
                // need to check if URL has RTTID inside of URL
                int idx = url.indexOf(RTTID);
                // so if URL has RTTID then should be logged
                if (idx != -1)
                    return true;
            }
        }
        catch (Exception ex)
        {
        }
        return false;
    }

    // Right now action has appended timestamp, please refer svn:285479 like Decimated_MultiRoute_1356157126814 
    // should remove timestamp before match.
    
    private String getRealActionId(String actionId)
    {
        int index = actionId.lastIndexOf("_");
        if (index > 0)
        {
            String timestamp = actionId.substring(index + 1);
            if (IsNumber(timestamp))
                actionId = actionId.substring(0, index);
        }
        return actionId;
    }

    private boolean IsNumber(String input)
    {
        boolean isNumber = false;
        isNumber = input.matches("[0-9]+"); 
        return isNumber;
    }
    
    public void fillAttrbute(AbstractMisLog mislog)
    {
        if(!(mislog instanceof RttMisLog))
        {
            return;
        }
        RttMisLog log = (RttMisLog) mislog;
        String actionId = log.getRequestID();
        String appInfo = log.getRequestFile();
        if (appInfo.startsWith("/"))
        {
            appInfo = appInfo.substring(1);
        }
        String appName = appInfo;// webApp
        String url = "";// ServletName
        int index = appInfo.indexOf("/");
        if (index != -1)
        {
            appName = appInfo.substring(0, index);
            url = appInfo.substring(index + 1);
        }

        String key = actionId + "_" + appName;
        try
        {
            Hashtable table = (Hashtable) ruleTable.get(key);
            if (table == null)
            {
                return;
            }
            String misAction;
            if (table.containsKey(url))
            {
                misAction = (String) table.get(url);
                try
                {
                    Integer.valueOf(misAction);
                    log.setMisActionID(misAction);
                }
                catch (NumberFormatException e)
                {
                    if (misAction != null && misAction.length() > 0)
                    {
                        log.setMisActionName(misAction);

                    }
                    else
                    {
                        log.setMisActionName(appName + delimeter + url);

                    }
                }
                addExtras(log);
                return;
            }

            if (table.containsKey(M_INSIDE))
            {
                int idx = url.indexOf(RTTID);
                // so if URL has RTTID then should be logged
                if (idx != -1)
                {
                    // "RTTID=" - 6 chars
                    boolean isDigitsOnly = true;
                    String rttId = url.substring(idx + 6);
                    for (int i = 0; i < rttId.length(); i++)
                    {
                        char currChar = rttId.charAt(i);
                        if (isDigitsOnly)
                            isDigitsOnly = Character.isDigit(currChar);
                        if (currChar == '&' || currChar == ';')
                        {
                            rttId = rttId.substring(0, i);
                            break;
                        }
                    }
                    if (isDigitsOnly)
                        log.setMisActionID(rttId);
                    else
                        log.setMisActionName(rttId);
                    addExtras(log);
                }
            }

            if (table.containsKey(M_ALL))
            {
                log.setMisActionName(appName + delimeter + url);
                addExtras(log);
                return;
            }

        }
        catch (Exception ex)
        {
            return;
        }
    }
    
    private void addExtras(AbstractMisLog log)
    {
        TnCellInfo info = TnRadioManager.getInstance().getCellInfo();
        if(info != null)
        {
            log.setzNetworkType(info.networkType);
        }
        log.setzSignalLevel(RadioStatusLogger.getInstance().getSignalStrengh());
    }
    
    public void reset()
    {
        // check if we have new version of parameters coming as SDP with new version
        // then clear... potentially there is gap in time while all params is going to
        // be set.. but for RTT it is ok these events are not high priority
        ruleTable.clear();
    }

}
