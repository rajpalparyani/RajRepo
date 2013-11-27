/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * MisLogManager.java
 *
 */
package com.telenav.log.mis;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import com.telenav.app.CommManager;
import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.dao.misc.SimpleConfigDao;
import com.telenav.data.dao.serverproxy.MisLogDao;
import com.telenav.data.dao.serverproxy.ServerDrivenParamsDao;
import com.telenav.data.datatypes.primitive.StringMap;
import com.telenav.data.serverproxy.AbstractServerProxy;
import com.telenav.data.serverproxy.IServerProxyConstants;
import com.telenav.data.serverproxy.IServerProxyListener;
import com.telenav.data.serverproxy.RequestItem;
import com.telenav.data.serverproxy.impl.IBatchProxy;
import com.telenav.data.serverproxy.impl.IToolsProxy;
import com.telenav.data.serverproxy.impl.ServerProxyFactory;
import com.telenav.log.mis.log.AbstractMisLog;
import com.telenav.logger.ILoggerListener;
import com.telenav.logger.Logger;
import com.telenav.module.AppConfigHelper;
import com.telenav.threadpool.INotifierListener;
import com.telenav.threadpool.Notifier;
import com.telenav.util.PrimitiveTypeCache;
import com.telenav.util.StringTokenizer;

/**
 *@author Casper (pwang@telenav.cn)
 *@date 2010-12-1
 */
public class MisLogManager implements INotifierListener, IServerProxyListener
{
    public static String EXCEPTION_PROCESS_ERROR = "MisLog Exception: Process error";

    private static final String EVENT_DELIM = ";";

    private static MisLogManager instance = new MisLogManager();

    private MisLogFilter filter = new MisLogFilter();

    private MisLogFactory factory = new MisLogFactory();
    
    private Hashtable logType;

    private String sessionID = "Default";

    private MisLogDao misLogDao;

    private AbstractMisLog[] sent;

    private Vector preparation = new Vector();

    private Hashtable logTable = new Hashtable();

    private final static int SEND_INTERVAL = 10 * 1000; // 10s till send time

    protected long lastNotifyTimestamp = -1;

    private boolean hasP1 = false;

    private boolean sending = false;
    
    private boolean isLoggerReady = false;
    
    private int countBeforeStore = 0;
    // to minimize impact on RMS save once in three messages
    // though we have chance to loose three of them but it is
    // better then performance problem and screw up of RMS

    private MisLogListener misLogListener;

    private LocalMislogLogger misLocalLogger;
    
    private MisLogManager()
    {
        initLogTypes();
        misLogDao = ((DaoManager) DaoManager.getInstance()).getMisLogDao();
        getFilter().enableTypes(IMisLogConstants.defaultEvents);
        misLogListener = new MisLogListener();
      
    }

    public static MisLogManager getInstance()
    {
        return instance;
    }
    
    public MisLogFactory getFactory()
    {
        return factory;
    }
    
    public MisLogFilter getFilter()
    {
        return filter;
    }
    
    public String getSessionId()
    {
        return sessionID;
    }
    
    public void start(String ptn)
    {
        AbstractMisLog[] temp = misLogDao.getMislog();
        if(temp != null && temp.length > 0)
        {
            hasP1 = true;
            this.setLastNotifyTimestamp(System.currentTimeMillis() - getNotifyInterval());
            
            for(int i = 0; i < temp.length ; i ++)
            {
                addToPreparation(temp[i]);
            }
        }
        sessionID = ptn + "_" + System.currentTimeMillis();
        misLogDao.setSessionId(sessionID);
        misLogDao.store();
        boolean enableLocalMislog = DaoManager.getInstance().getSimpleConfigDao().getBoolean(SimpleConfigDao.KEY_LOCAL_MISLOG_ENABLE);
        if(enableLocalMislog) 
        {
            misLocalLogger = new LocalMislogLogger();
        }
        Logger.log(Logger.INFO, this.getClass().getName(), "Mislog : Mislog Manager start with session id: " + sessionID);
        Notifier.getInstance().addListener(this);
    }
    
    public void initFilter(String productType, ServerDrivenParamsDao serverDrivenParamsDao)
    {
        String key = ServerDrivenParamsDao.MISLOG_ENABLE_TYPES + "_" + productType;
        String logTypes = serverDrivenParamsDao.getValue(key);
        if (logTypes != null && logTypes.length() > 0)
        {
            getFilter().reset();
            getFilter().enableTypes(parseEnabledEvents(logTypes));
        }
    }

    public void initHelper(ServerDrivenParamsDao serverDrivenParamsDao)
    {
        StringMap params = serverDrivenParamsDao.getServerParams();
        if (params == null || params.size() == 0)
        {
            return;
        }

        boolean hasReset = false;

        Enumeration keyEnum = params.keys();
        while (keyEnum.hasMoreElements())
        {
            if (!hasReset)
            {
                getFactory().resetAllMisLogHelper();
                hasReset = true;
            }
            String key = (String) keyEnum.nextElement();
            String rule = params.get(key);
            if (key.startsWith(ServerDrivenParamsDao.MISLOG_HELPER))
            {
                try
                {
                    StringTokenizer tokenizer = new StringTokenizer(key, ".");
                    tokenizer.nextToken();
                    int type = Integer.valueOf(tokenizer.nextToken()).intValue();
                    this.getFactory().getMisLogHelper(type).addRule(rule);
                }

                catch (Exception ex)
                {
                    Logger.log(this.getClass().getName(), new Exception("MIS for RTT. Incorrect entry"));
                }
            }
        }

    }
    
    /**
     * Parses events ids received as server driven parameters
     * @param events array of integers separated by EVENT_DELIM
     * @return array of enabled events
     */
    public int[] parseEnabledEvents(String events)
    {
        if (events != null && events.length() > 0)
        {
            StringTokenizer tk = new StringTokenizer(events, EVENT_DELIM);
            Vector vec = new Vector();
            while (tk.hasMoreTokens())
            {
                try
                {
                    vec.addElement(Integer.valueOf(tk.nextToken()));
                }
                catch (NumberFormatException ex)
                {
                    Logger.log(this.getClass().getName(), ex);
                }
            }
            int[] result = new int[vec.size()];
            for (int i = 0; i < vec.size(); i++)
            {
                result[i] = ((Integer) vec.elementAt(i)).intValue();
            }
            return result;
        }
        return null;
    }

    public void logExitInfo(int level, String clazz, String message, Throwable t, Object[] params)
    {
        misLogListener.log(level, clazz, message, t, params);
        if(misLocalLogger != null)
        {
            misLocalLogger.close();
        }
    }

    public void storeMisLog(AbstractMisLog log)
    {
        if (logTable.containsKey(PrimitiveTypeCache.valueOf(log.getType())))
        {
            Vector v = (Vector) logTable.get(PrimitiveTypeCache.valueOf(log.getType()));
            if (!v.contains(log))
            {
                v.addElement(log);
            }
        }
        else
        {
            Vector v = new Vector();
            v.addElement(log);
            logTable.put(PrimitiveTypeCache.valueOf(log.getType()), v);
        }
    }

    public AbstractMisLog getMisLog(int type)
    {
        Object value = logTable.get(PrimitiveTypeCache.valueOf(type));
        if (value != null)
        {
            Vector v = (Vector) value;
            if (v.size() > 0)
            {
                return (AbstractMisLog) v.lastElement();
            }
        }
        return null;
    }
    
    public void clearStoredMislog(int type)
    {
        Object value = logTable.get(PrimitiveTypeCache.valueOf(type));
        if (value != null)
        {
            Vector v = (Vector) value;
            v.removeAllElements();
        }
    }
    
    public void removeStoredLog(AbstractMisLog log)
    {
        Object obj = logTable.get(PrimitiveTypeCache.valueOf(log.getType()));
        if(obj != null)
        {
            Vector logs = (Vector) obj;
            logs.removeElement(log);
        }
    }

    private void addToPreparation(AbstractMisLog log)
    {
        preparation.addElement(log);
        if (needBackup(log.getPriority()))
        {
            synchronized (misLogDao)
            {
                misLogDao.addMisLog(log);
                if (countBeforeStore == 3)
                {
                    countBeforeStore = 0;
                    misLogDao.store();
                }
                else
                {
                    countBeforeStore++;
                }
            }
            hasP1 = true;
        }

    }

    public int getOutgoingEventCount()
    {
        if (sent != null)
            return sent.length;
        return preparation.size();
    }

    public AbstractMisLog[] getPreparationLog()
    {
        // sent is not null in case if getOutgoing() was called
        // but not acknowledged, sent attempt failed
        if (sent != null)
            return sent; // TODO probably we need to add events from outgoing queue ???

        if (preparation.size() > 0)
        {
            Vector tmp = preparation;
            preparation = new Vector();
            int size = tmp.size();
            sent = new AbstractMisLog[size];
            for (int i = 0; i < size; i++)
                sent[i] = (AbstractMisLog) tmp.elementAt(i);
            
            return sent;
        }
        return null;
    }
    
    public void shutdown()
    {
        Notifier.getInstance().removeListener(this);
    }

    public void storeToRms()
    {
        synchronized (misLogDao)
        {
            if (preparation.size() > 0)
            {
                AbstractMisLog mislog;
                for (int i = 0; i < preparation.size(); i++)
                {
                    mislog = (AbstractMisLog) preparation.elementAt(i);
                    if (needBackup(mislog.getPriority()))
                    {
                        misLogDao.addMisLog(mislog);
                    }
                }
            }
            misLogDao.store();
        }
    }
    
    public void acknowledge()
    {
        Logger.log(Logger.INFO, this.getClass().getName(), "Mislog : Log Sent Successfully");
        
        synchronized (misLogDao)
        {
            boolean changed = false;
            for (int i = 0; sent != null && i < sent.length; i++)
            {
                if (needBackup(sent[i].getPriority()))
                {
                    if (misLogDao.removeMisLog(sent[i]))
                    {
                        changed = true;
                    }
                }
            }
            if (changed)
            {
                misLogDao.store();
            }
        }
        sent = null;
        this.setLastNotifyTimestamp(System.currentTimeMillis());
        sending = false;
    }

    public void rollback()
    {
        StringBuffer misLogIds = new StringBuffer();
        misLogIds.append("Mislog : Meet Error Rollback : ");
        
        // store events with persistence priority P2 in case of failure
        synchronized (misLogDao)
        {
            boolean changed = false;
            for (int i = 0; sent != null && i < sent.length; i++)
            {
                if (needBackup(sent[i].getPriority()))
                {
                    //misLogDao.addMisLog(sent[i]);
                    addToPreparation(sent[i]);
                    misLogIds.append(sent[i].getType() + ",");
                    changed = true;
                }
            }
            if (changed)
            {
                misLogDao.store();
            }
        }
        
        Logger.log(Logger.INFO, this.getClass().getName(), misLogIds.toString());
        // // add 2 minutes for next attempt
        this.setLastNotifyTimestamp(this.getLastNotifyTimestamp() + 2 * 60 * 1000);
        
        sent = null;
        sending = false;
    }

    public void notify(long timestamp)
    {
        if (hasP1 && !sending)
        {
            requestSendMisLog(this, null);
        }
        
    }
    
    public boolean requestSendMisLog(IServerProxyListener listener, IBatchProxy batchProxy)
    {
        AbstractMisLog[] reports = getPreparationLog();
        if (reports != null && reports.length > 0)
        {
            sendMisLog(reports, listener, batchProxy);
            return true;
        }
        return false;
    }

    protected void sendMisLog(AbstractMisLog[] logs, IServerProxyListener listener, IBatchProxy batchProxy)
    {
        if (logs != null)
        {
            if(AppConfigHelper.isLoggerEnable)
            {
                StringBuffer misLogIds = new StringBuffer();
                misLogIds.append("Mislog : Sending Log : ");
                for(int i = 0; i < logs.length; i ++)
                {
                    boolean needSessionId = logs[i].getAttribute(IMisLogConstants.ATTR_SESSION_ID) != null && !sessionID.equals(logs[i].getAttribute(IMisLogConstants.ATTR_SESSION_ID));
                    misLogIds.append(logs[i].getType());
                    if(needSessionId)
                    {
                        misLogIds.append("(" + logs[i].getAttribute(IMisLogConstants.ATTR_SESSION_ID) + "),");
                    }
                    else
                    {
                        misLogIds.append(",");
                    }
                }
                
                Logger.log(Logger.INFO, this.getClass().getName(), misLogIds.toString());
            }
            sending = true;
            if(batchProxy == null)
            {
                IToolsProxy toolsProxy = ServerProxyFactory.getInstance().createToolsProxy(null, CommManager.getInstance().getComm(), listener, null);
                toolsProxy.sendMISReports(logs, sessionID);
            }
            else
            {
                RequestItem requestItem = new RequestItem(IServerProxyConstants.ACT_SEND_MIS_REPORTS, 
                    (AbstractServerProxy)ServerProxyFactory.getInstance().createToolsProxy(null, CommManager.getInstance().getComm(), listener, null));
                requestItem.params = new Vector();
                requestItem.params.addElement(logs);
                requestItem.params.addElement(sessionID);
                batchProxy.addBatchItem(requestItem);
            }

            hasP1 = false;
        }
    }


    public void setLastNotifyTimestamp(long timestamp)
    {
        this.lastNotifyTimestamp = timestamp;
    }

    public long getLastNotifyTimestamp()
    {
        return lastNotifyTimestamp;
    }

    public long getNotifyInterval()
    {
        return SEND_INTERVAL;
    }
    
    public boolean needBackup(int priority)
    {
        if(priority == IMisLogConstants.PRIORITY_1 || priority == IMisLogConstants.PRIORITY_2)
        {
            return true;
        }
        
        return false;
    }
    
    protected void initLogTypes()
    {
        if(!AppConfigHelper.isLoggerEnable)
            return;
            
        logType = new Hashtable();
        
        logType.put(IMisLogConstants.TYPE_ONEBOX_ADDRESS_IMPRESSION, "addr_impr");
        logType.put(IMisLogConstants.TYPE_ONEBOX_ADDRESS_SELECTION, "addr_selec");
        logType.put(IMisLogConstants.TYPE_ONEBOX_ADDRESS_ONE_RESULT, "addr_one");
        logType.put(IMisLogConstants.TYPE_ONEBOX_SUGGESTIONS_IMPRESSION, "sugg_impr");
        logType.put(IMisLogConstants.TYPE_ONEBOX_SUGGESTIONS_CLICK, "sugg_click");
        logType.put(IMisLogConstants.TYPE_TWOBOX_ADDRESS_IMPRESSION, "twoaddr_impr");
        logType.put(IMisLogConstants.TYPE_TWOBOX_ADDRESS_SELECTION, "twoaddr_selec");
        logType.put(IMisLogConstants.TYPE_TWOBOX_ADDRESS_ONE_RESULT, "twoaddr_one");
        logType.put(IMisLogConstants.TYPE_POI_IMPRESSION, "poi_impr");
        logType.put(IMisLogConstants.TYPE_POI_DETAILS, "poi_detail");
        logType.put(IMisLogConstants.TYPE_POI_VIEW_MAP, "poi_map");
        logType.put(IMisLogConstants.TYPE_POI_DRIVE_TO, "poi_drive");
        logType.put(IMisLogConstants.TYPE_POI_SHARE, "poi_share");
        logType.put(IMisLogConstants.TYPE_POI_CALL_TO, "poi_call");
        logType.put(IMisLogConstants.TYPE_POI_VIEW_MERCHANT, "poi_merchant");
        logType.put(IMisLogConstants.TYPE_POI_VIEW_COUPON, "poi_coupon");
        logType.put(IMisLogConstants.TYPE_POI_VIEW_MENU, "poi_menu");
        logType.put(IMisLogConstants.TYPE_POI_MAP_ALL, "poi_all");
        logType.put(IMisLogConstants.TYPE_POI_ADD_PLACE, "poi_add_place");
        logType.put(IMisLogConstants.TYPE_RTT, "rtt");
        logType.put(IMisLogConstants.TYPE_POI_REVIEW_TAB_CLICK, "poi_review_tab_click");
        logType.put(IMisLogConstants.TYPE_POI_REVIEW_LINK_OUT_IMPRESSION, "poi_review_link_out_impression");
        logType.put(IMisLogConstants.TYPE_POI_REVIEW_LINK_OUT_CLICK, "poi_review_link_out_click");
        logType.put(IMisLogConstants.TYPE_SESSION_STARTUP, "seesion_startup");
        logType.put(IMisLogConstants.TYPE_STARTUP_INFO, "startup_info");
        logType.put(IMisLogConstants.TYPE_ON_BOARD_STARTUP_INFO, "on_board_startup_info");
        logType.put(IMisLogConstants.TYPE_ON_BOARD_MAP_DISPLAY, "on_board_map_display");
        logType.put(IMisLogConstants.TYPE_APP_SESSION_SUMMARY, "session_summary");
        logType.put(IMisLogConstants.TYPE_PREFERENCE_CHANGE, "preference_change");
        logType.put(IMisLogConstants.TYPE_FEEDBACK, "feedback");
        logType.put(IMisLogConstants.TYPE_ROUTE_REQUEST, "route_request");
        logType.put(IMisLogConstants.TYPE_TRIP_SUMMARY, "trip_summary");
        logType.put(IMisLogConstants.TYPE_MAP_UPDATE_TIME, "map_update");
        logType.put(IMisLogConstants.TYPE_MAP_DISPLAY_TIME, "map_display");
        logType.put(IMisLogConstants.TYPE_SPEED_CAMERA_IMPRESSION, "speed_camera");
        logType.put(IMisLogConstants.TYPE_SPEED_LIMIT_IMPRESSION, "speed_limit");
        logType.put(IMisLogConstants.TYPE_ARIVALl_CONFIRMATION, "arrival_confirmation");
        logType.put(IMisLogConstants.TYPE_HOME_SCREEN_TIME, "home_sreen");
        logType.put(IMisLogConstants.TYPE_CLICK_STREAM, "click_stream");
        logType.put(IMisLogConstants.TYPE_UI_USAGE_REPORT, "ui_usage");
        logType.put(IMisLogConstants.TYPE_FIRST_TIME_LOGIN, "first_time_login");
        logType.put(IMisLogConstants.TYPE_DSR_GENERIC, "dsr");
        logType.put(IMisLogConstants.TYPE_POI_SEARCH_REQUEST, "search_request");
        logType.put(IMisLogConstants.TYPE_POI_SORT_REQUEST, "sort_request");
        logType.put(IMisLogConstants.TYPE_BILLBOARD_INITIAL_VIEW_START, "bill_iniview_start");
        logType.put(IMisLogConstants.TYPE_BILLBOARD_INITIAL_VIEW_END, "bill_iniview_end");
        logType.put(IMisLogConstants.TYPE_BILLBOARD_INITIAL_VIEW_CLICK, "bill_iniview_click");
        logType.put(IMisLogConstants.TYPE_BILLBOARD_DETAIL_VIEW_START, "bill_detview_start");
        logType.put(IMisLogConstants.TYPE_BILLBOARD_DETAIL_VIEW_END, "bill_detview_end");
        logType.put(IMisLogConstants.TYPE_BILLBOARD_DETAIL_VIEW_DRIVE_TO, "bill_detview_drive");
        logType.put(IMisLogConstants.TYPE_BILLBOARD_DETAIL_MORE, "bill_detview_more");
        logType.put(IMisLogConstants.TYPE_BILLBOARD_POI_VIEW_START, "bill_poiview_start");
        logType.put(IMisLogConstants.TYPE_BILLBOARD_POI_VIEW_END, "bill_poiview_end");
        logType.put(IMisLogConstants.TYPE_BILLBOARD_POI_VIEW_DRIVE_TO, "bill_poiview_drive");
        logType.put(IMisLogConstants.TYPE_BILLBOARD_POI_VIEW_CALL_TO, "bill_poiview_call");
        logType.put(IMisLogConstants.TYPE_BILLBOARD_POI_VIEW_DEAL_TAB, "bill_poiview_deal");
        logType.put(IMisLogConstants.TYPE_BILLBOARD_POI_VIEW_MENU_TAB, "bill_poiview_tab");
        logType.put(IMisLogConstants.TYPE_BILLBOARD_POI_VIEW_MAP, "bill_poiview_map");
        logType.put(IMisLogConstants.TYPE_HEAD_UNIT_CAR_CONNECT, "car_connect_start");
        logType.put(IMisLogConstants.TYPE_HEAD_UNIT_CAR_DISCONNECT, "car_connect_end");
        logType.put(IMisLogConstants.TYPE_HEAD_UNIT_CONNECTED_DRIVE_TO, "car_connect_driveto");
        logType.put(IMisLogConstants.TYPE_HEAD_UNIT_CONNECTED_POI_SEARCH, "car_connect_poisearch");
        logType.put(IMisLogConstants.TYPE_MAP_DOWNLOAD_START, "map_download_start");
        logType.put(IMisLogConstants.TYPE_MAP_DOWNLOAD_COMPLETE, "map_download_complete");

    }

    public boolean isAllowNetworkRequest(AbstractServerProxy proxy)
    {
        return true;
    }

    public void networkError(AbstractServerProxy proxy, byte statusCode, String jobId)
    {
        MisLogManager.getInstance().rollback();
    }

    public void transactionError(AbstractServerProxy proxy)
    {
        MisLogManager.getInstance().rollback();
    }

    public void transactionFinished(AbstractServerProxy proxy , String jobId)
    {
        //no implementation
    }

    public void updateTransactionStatus(AbstractServerProxy proxy, byte progress)
    {
        //no implementation
    }
    
    public void registerMisLogListener ()
    {
        Logger.add(misLogListener, MisLogManager.getInstance().getFilter());
        isLoggerReady = true;
    }
    
    public boolean isLoggerReady()
    {
        return isLoggerReady;
    }
    
    private class MisLogListener implements ILoggerListener
    {
        public  void log(int level, String clazz, String message, Throwable t, Object[] params)
        {
            AbstractMisLog log = (AbstractMisLog) params[0];
            
            String logTypeStr = logType == null ? "" : (String)logType.get(log.getType());
            Logger.log(Logger.INFO, this.getClass().getName(), "Mislog : " + log.getType() + "(" + logTypeStr + ")");
            
            if(misLocalLogger != null)
            {
                misLocalLogger.logMisLogMessage(log, logTypeStr);
            }
            try
            {
                log.processLog();
            }
            catch (Exception e)
            {
                Logger.log(this.getClass().getName(), new Exception(EXCEPTION_PROCESS_ERROR));
                return;
            }
            
            addToPreparation((AbstractMisLog) log);
            if (hasP1 && !sending && (sent == null && preparation.size() > 20))
            {
                MisLogManager.getInstance().setLastNotifyTimestamp(System.currentTimeMillis() - getNotifyInterval());
            }
        }
    }
}
