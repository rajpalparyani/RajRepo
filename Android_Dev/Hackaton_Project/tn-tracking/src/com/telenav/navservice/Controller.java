package com.telenav.navservice;

import java.util.Vector;

import com.telenav.location.TnLocation;
import com.telenav.location.TnLocationManager;
import com.telenav.logger.Logger;
import com.telenav.navservice.location.CellRecorder;
import com.telenav.navservice.location.GpsRecorder;
import com.telenav.navservice.location.LocationBuffer;
import com.telenav.navservice.location.LocationSender;
import com.telenav.navservice.location.StationaryMonitor;
import com.telenav.navservice.location.LocationBuffer.ILocationListener;
import com.telenav.navservice.model.App;
import com.telenav.navservice.policy.Policies;
import com.telenav.navservice.policy.Policy;
import com.telenav.navservice.policy.PolicyRequester;
import com.telenav.navservice.util.TrackingLogger;

class ControllerJob
{
    public static final int SET_PARAM                           = 1;
    public static final int CHANGE_CHARGING                     = 2;
    public static final int DOWNLOAD_URLS                       = 3;
    public static final int DOWNLOAD_POLICIES                   = 4;
    public static final int SWITCH_TO_STATIONARY_MODE           = 5;
    public static final int SWITCH_BACK_FROM_STATIONARY_MODE    = 6;
    
    public static final int STOP_SERVICE                        = 100;
    
    public int type;
    public long scheduledTime;
    public NavServiceParameter param;
    public boolean isCharging;
}

public class Controller implements Runnable, INotify, ILocationListener
{
    protected static final long RETRY_INTERVAL = 30 * 1000;
    
    protected static final int STATIONARY_FIX_COUNT_THRESHOLD = 3;
    
    protected boolean isRunning, isStarted, isStopped;
    
    protected Vector jobQueue = new Vector();
    
    protected App app;
    
    protected LocationBuffer gpsBuffer, cellBuffer;
    protected GpsRecorder gpsRecorder;
    protected CellRecorder cellRecorder;
    
    protected LocationSender locationSender;
    
    protected ServiceLocator serviceLocator;
    protected PolicyRequester policyRequester;
    
    protected String serviceLocatorUrl;
    protected String policyUrl, ldlUrl;
    
    protected Policies policies;
    protected Policy currentGPolicy, currentCPolicy;
    
    protected IControllerListener ctrlListener;
    protected IClientStateMonitor clientStateMonitor;
    
    protected boolean isStationaryMode;
    protected StationaryMonitor stationaryMonitor;
    protected long stationaryCheckIntervalInMillis;
    protected float stationarySpeed;
    
    public static interface IControllerListener
    {
        public void stopService();
    }
    
    public Controller(App app, IControllerListener listener, IClientStateMonitor clientStateMonitor)
    {
        this.app = app;
        this.ctrlListener = listener;
        this.clientStateMonitor = clientStateMonitor;
        this.gpsBuffer = new LocationBuffer(100, this);
        this.cellBuffer = new LocationBuffer(30, null);
        serviceLocator = new ServiceLocator();
        policyRequester = new PolicyRequester();
        gpsRecorder = new GpsRecorder(TnLocationManager.GPS_179_PROVIDER, gpsBuffer);
        cellRecorder = new CellRecorder(TnLocationManager.NETWORK_PROVIDER, cellBuffer);
    }
    
    public void start()
    {
        if (isStarted)
        {
            Logger.log(Logger.INFO, this.getClass().getName(), "Trying to restart Controller, throwing exception");
            throw new IllegalStateException("Controller can not be reused, create a new instance to use");
        }
        isRunning = true;
        new Thread(this).start();
        isStarted = true;
        Logger.log(Logger.INFO, this.getClass().getName(), "Controller started");
    }
    
    protected void stop()
    {
        Logger.log(Logger.INFO, this.getClass().getName(), "Trying to stop Controller");
        isRunning = false;
        synchronized (jobQueue)
        {
            jobQueue.notify();
        }
    }
    
    protected void addJob(ControllerJob job)
    {
        synchronized(jobQueue)
        {
            if (jobQueue.size() == 0)
            {
                jobQueue.addElement(job);
            }
            else
            {
                int i;
                for (i=0; i<jobQueue.size(); i++)
                {
                    ControllerJob tempJob = (ControllerJob)jobQueue.elementAt(i);
                    if (job.scheduledTime < tempJob.scheduledTime)
                    {
                        jobQueue.insertElementAt(job, i);
                        break;
                    }
                }
                if (i == jobQueue.size())
                {
                    jobQueue.addElement(job);
                }
            }
            jobQueue.notify();
        }
    }
    
    public void setParameters(NavServiceParameter param)
    {
        Logger.log(Logger.INFO, this.getClass().getName(), "set params to Controller: "+param);
        
        ControllerJob job = new ControllerJob();
        job.type = ControllerJob.SET_PARAM;
        job.param = param;
        addJob(job);
    }
    
    public void setCharging(boolean isCharging)
    {
        Logger.log(Logger.INFO, this.getClass().getName(), "set charging: "+isCharging);
        ControllerJob job = new ControllerJob();
        job.type = ControllerJob.CHANGE_CHARGING;
        job.isCharging = isCharging;
        addJob(job);
    }
    
    public void stopService()
    {
        //clear job queue here so that if there is SET_PARAM job when handling STOP_SERVICE job,
        //we know the client is trying to start the service again
        jobQueue.removeAllElements();
        
        Logger.log(Logger.INFO, this.getClass().getName(), "stopService() called");
        ControllerJob job = new ControllerJob();
        job.type = ControllerJob.STOP_SERVICE;
        addJob(job);
    }
    
    public void run()
    {
        while(isRunning)
        {
            try{
                synchronized(jobQueue)
                {
                    long policyExpireTime = 86400L * 1000L; //default for one day
                    if (policies != null)
                    {
                        policyExpireTime = policies.getExpiration() - System.currentTimeMillis();
                    }
                    if (jobQueue.size() == 0)
                    {
                        Logger.log(Logger.INFO, this.getClass().getName(), "jobQueue is empty, wait for "+policyExpireTime+" ms");
                        jobQueue.wait(policyExpireTime);
                    }
                    else
                    {
                        ControllerJob job = (ControllerJob)jobQueue.elementAt(0);
                        long time = Math.min(policyExpireTime, job.scheduledTime - System.currentTimeMillis());
                        if (time > 0)
                        {
                            Logger.log(Logger.INFO, this.getClass().getName(), "job is scheduled, wait for "+time+" ms");
                            jobQueue.wait(time);
                        }
                    }
                }

                if (!isRunning)
                    break;
                
                ControllerJob job = null;
                synchronized(jobQueue)
                {
                    if (jobQueue.size() > 0)
                    {
                        ControllerJob tempJob = (ControllerJob)jobQueue.elementAt(0);
                        if (tempJob.scheduledTime <= System.currentTimeMillis())
                        {
                            jobQueue.removeElementAt(0);
                            job = tempJob;
                        }
                    }
                }
                
                if (job != null)
                {
                    handle(job);
                }
                
                if (!isRunning)
                    break;

                if (policies != null && policies.getExpiration() <= System.currentTimeMillis())
                {
                    Logger.log(Logger.INFO, this.getClass().getName(), "policy is expired, try to download again");
                    ControllerJob job1 = new ControllerJob();
                    job1.type = ControllerJob.DOWNLOAD_POLICIES;
                    addJob(job1);
                }
            }
            catch(Throwable t)
            {
                Logger.log(this.getClass().getName(), t);
            }
        }
        
        isStopped = true;
        Logger.log(Logger.INFO, this.getClass().getName(), "Controller stopped");
    }
    
    //it must be called in run()
    protected void handleRequestUrls()
    {
        if (serviceLocatorUrl == null || serviceLocatorUrl.trim().length() <= 0)
        {
            Logger.log(Logger.ERROR, this.getClass().getName(), "NavService --------- service locator url is null or empty!!!");
            return;
        }
        
        String[] urls = serviceLocator.getNavServiceUrls(serviceLocatorUrl, app);
        if (urls != null && urls.length == 2)
        {
            policyUrl = urls[0];
            ldlUrl = urls[1];
            Logger.log(Logger.INFO, this.getClass().getName(), "Got new urls, policyUrl = "+policyUrl+", ldlurl = "+ldlUrl);
            ControllerJob job = new ControllerJob();
            job.type = ControllerJob.DOWNLOAD_POLICIES;
            addJob(job);
        }
        else
        {
            Logger.log(Logger.INFO, this.getClass().getName(), "urls download error, retry later");
            ControllerJob job = new ControllerJob();
            job.type = ControllerJob.DOWNLOAD_URLS;
            job.scheduledTime = System.currentTimeMillis() + RETRY_INTERVAL;
            addJob(job);
        }
    }
    
    //it must be called in run()
    protected void handleRequestPolicies()
    {
        Policies p = policyRequester.getPolicies(policyUrl);
        if (p != null)
        {
            if (!p.equals(policies))
            {
                Logger.log(Logger.INFO, this.getClass().getName(), "policy changed, start using new policy");
                policies = p;
                handleNewPolicies(p);
            }
            else
            {
                Logger.log(Logger.INFO, this.getClass().getName(), "policy not changed, continue using old policy");
            }
        }
        else
        {
            Logger.log(Logger.INFO, this.getClass().getName(), "policy downloading failed, try again later");
            ControllerJob job = new ControllerJob();
            job.type = ControllerJob.DOWNLOAD_POLICIES;
            job.scheduledTime = System.currentTimeMillis() + RETRY_INTERVAL;
            addJob(job);
        }
    }
    
    protected void handle(ControllerJob job)
    {
        switch(job.type)
        {
            case ControllerJob.SET_PARAM:
            {
                handleSettingParam(job.param);
                break;
            }
            case ControllerJob.CHANGE_CHARGING:
            {
                handleChangeChargingStatus(job.isCharging);
                break;
            }
            case ControllerJob.DOWNLOAD_URLS:
            {
                handleRequestUrls();
                break;
            }
            case ControllerJob.DOWNLOAD_POLICIES:
            {
                handleRequestPolicies();
                break;
            }
            case ControllerJob.SWITCH_TO_STATIONARY_MODE:
            {
                switchToStationaryMode();
                break;
            }
            case ControllerJob.SWITCH_BACK_FROM_STATIONARY_MODE:
            {
                switchBackFromStationMode();
                break;
            }
            case ControllerJob.STOP_SERVICE:
            {
                stopServiceImpl();
                break;
            }
            default:
            {
                Logger.log(Logger.INFO, this.getClass().getName(), "ControllerJob not handled, job type = "+job.type);
                break;
            }
        }
    }
    
    //it must be called in run()
    protected void handleChangeChargingStatus(boolean isCharging)
    {
        if (app.isCharging() != isCharging)
        {
            Logger.log(Logger.INFO, this.getClass().getName(), "change charging status, isCharing = "+isCharging);
            app.setCharging(isCharging);
            handleModeChange();
        }
        else
        {
            Logger.log(Logger.INFO, this.getClass().getName(), "charging status not changed, ignore, current charging status = "+isCharging);
        }
    }
    
    //it must be called in run()
    protected void stopServiceImpl()
    {
        Logger.log(Logger.INFO, this.getClass().getName(), "stopServiceImpl() enter");
        if (locationSender != null)
        {
            locationSender.stop();
            locationSender = null;
        }
        if (gpsRecorder != null)
        {
            gpsRecorder.setParameters(0, 0, 0);
        }
        if (cellRecorder != null)
            cellRecorder.setParameters(0);
        
        try{
            Thread.sleep(2000); //wait for location sender to finish
        }catch(Exception e)
        {
            Logger.log(this.getClass().getName(), e);
        }

        if (jobQueue.size() > 0)
        {
            //the jobQueue has been cleared when stopService() called. 
            //If it has jobs now, then the client might be trying to start the service again.
            //If so, we should not stop it
            synchronized(jobQueue)
            {
                for (int i=0; i<jobQueue.size(); i++)
                {
                    ControllerJob job = (ControllerJob)jobQueue.elementAt(i);
                    if (job.type == ControllerJob.SET_PARAM)
                    {
                        //found SET_PARAM job, the client is trying to start the service again, no need to stop
                        return;
                    }
                }
            }
        }
        stop();
        ctrlListener.stopService();
    }
    
    //it must be called in run()
    protected void handleSettingParam(NavServiceParameter param)
    {
        Logger.log(Logger.INFO, this.getClass().getName(), "set param = " + param.toString());
        if (param.hasSetForceStop() && param.isForceStop())
        {
            stopService();
            return;
        }
        
        if (param.hasSetLogEnabled())
        {
            TrackingLogger.isLogEnabled = param.isLogEnabled();
        }
        
        if (param.hasSetPauseGpsTime())
        {
            gpsRecorder.pauseFor(param.getPauseGpsTime());
        }
        
        buildApp(app, param);
        
        if (param.hasSetServerUrl())
        {
            String url = param.getServerUrl();
            if (url != null && !url.equalsIgnoreCase(serviceLocatorUrl))
            {
                //service locator changed, we need to download service locator again
                Logger.log(Logger.INFO, this.getClass().getName(), "service locator url changed, new service locator url = "+url);
                serviceLocatorUrl = url;
                ControllerJob job = new ControllerJob();
                job.type = ControllerJob.DOWNLOAD_URLS;
                addJob(job);
            }
        }
    }
    
    //it must be called in run()
    protected void buildApp(App app, NavServiceParameter param)
    {
        if (param.hasSetCarrierName())
        {
            app.setCarrier(param.getCarrierName());
        }
        if (param.hasSetUserId())
        {
            app.setUserId(param.getUserId());
        }
        if (param.hasSetAppVersion())
        {
            app.setAppVersion(param.getAppVersion());
        }
        if (param.hasSetAppName())
        {
            app.setAppName(param.getAppName());
        }
        if (param.hasSetRouteId() || param.hasSetForeground())
        {
            if (param.hasSetRouteId())
                app.setRouteId(param.getRouteId());
            if (param.hasSetForeground())
                app.setForeground(param.isForeground());
            handleModeChange();
        }
    }
    
    //it must be called in run()
    protected void handleModeChange()
    {
        Logger.log(Logger.INFO, this.getClass().getName(), "handleModeChange enter");
        handlePolicies(this.policies);
    }
    
    //it must be called in run()
    protected void switchToStationaryMode()
    {
        this.isStationaryMode = true;
        if (gpsRecorder != null)
            gpsRecorder.setParameters(1, (int)(stationaryCheckIntervalInMillis / 1000), 5 * 60);
        if (cellRecorder != null)
            cellRecorder.setParameters(0);
        if (locationSender != null)
        {
            locationSender.stop();
            locationSender = null;
        }
        currentGPolicy = null;
        currentCPolicy = null;
    }
    
    //it must be called in run()
    protected void switchBackFromStationMode()
    {
        this.isStationaryMode = false;
        handlePolicies(this.policies);
    }
    
    //it must be called in run()
    protected void handleNewPolicies(Policies newPolicies)
    {
        Logger.log(Logger.INFO, this.getClass().getName(), "handleNewPolicies(), new policy = "+newPolicies.toString());
        handlePolicies(newPolicies);
    }
    
    //it must be called in run()
    protected void handlePolicies(Policies policies)
    {
        if (policies == null)
            return;
        
        boolean isGpsCollecting = false;
        boolean isCellCollecting = false;
        
        int currentMode = Policy.calcPolicyMode(app.isForeground(), app.isCharging(), app.isDriveTo());
        Vector gPolicies = policies.findPolicies(Policy.POLICY_TYPE_GPS, currentMode);
        if (gPolicies != null && gPolicies.size() > 0)
        {
            //pick up the first one
            Policy p = (Policy)gPolicies.elementAt(0);
            if (!p.equals(currentGPolicy))
            {
                Logger.log(Logger.INFO, this.getClass().getName(), "new GPS policy = "+p.toString());
                currentGPolicy = p;
                if (p.isEnabled)
                {
                    isGpsCollecting = true;
                    if (p.runningWhenStationary)
                    {
                        Logger.log(Logger.INFO, this.getClass().getName(), "No need stationaryMonitor");
                        this.stationaryMonitor = null;
                        this.isStationaryMode = false;
                    }
                    else
                    {
                        Logger.log(Logger.INFO, this.getClass().getName(), "start stationaryMonitor, speed = "+
                                p.stationarySpeed+", duration = "+p.stationaryTime+", check interval = "+p.stationaryCheckInterval);
                        this.stationarySpeed = p.stationarySpeed;
                        this.stationaryMonitor = new StationaryMonitor(STATIONARY_FIX_COUNT_THRESHOLD, p.stationaryTime * 1000L, stationarySpeed);
                        this.stationaryCheckIntervalInMillis = p.stationaryCheckInterval * 1000L;
                    }
                    
                    gpsRecorder.setParameters(p.sampleSize, p.sampleInterval, 15 * 60);
                    if (locationSender == null)
                    {
                        locationSender = new LocationSender(gpsBuffer, cellBuffer, this);
                        locationSender.start();
                    }
                    locationSender.setGpsSendingInterval(p.reportInterval, p.isAttachCellLocation);
                }
            }
            else
            {
                if (currentGPolicy != null && currentGPolicy.isEnabled)
                    isGpsCollecting = true;
                
                Logger.log(Logger.INFO, this.getClass().getName(), "GPS policy not changed");
            }
        }
        else
        {
            currentGPolicy = null;
        }
        
        if (!isGpsCollecting)
        {
            Logger.log(Logger.INFO, this.getClass().getName(), "No matched GPS policy, stop gps recorder");
            gpsRecorder.setParameters(0, 0, 0);
        }
        
        Vector cPolicies = policies.findPolicies(Policy.POLICY_TYPE_CELL, currentMode);
        if (cPolicies != null && cPolicies.size() > 0)
        {
            //pick up the first one
            Policy p = (Policy)cPolicies.elementAt(0);
            if (!p.equals(currentCPolicy))
            {
                Logger.log(Logger.INFO, this.getClass().getName(), "new Cell policy = "+p.toString());
                currentCPolicy = p;
                if (p.isEnabled)
                {
                    isCellCollecting = true;
                    cellRecorder.setParameters(p.sampleInterval);
                    if (locationSender == null)
                    {
                        locationSender = new LocationSender(gpsBuffer, cellBuffer, this);
                        locationSender.start();
                    }
                    locationSender.setCellSendingInterval(p.reportInterval);
                }
            }
            else
            {
                if (currentCPolicy != null && currentCPolicy.isEnabled)
                    isCellCollecting = true;
                
                Logger.log(Logger.INFO, this.getClass().getName(), "Cell policy not changed");
            }
        }
        else
        {
            Logger.log(Logger.INFO, this.getClass().getName(), "No matched cell policy, stop cell recorder");
            currentCPolicy = null;
            cellRecorder.setParameters(0);
        }
        
        if (isGpsCollecting || isCellCollecting)
        {
            if (locationSender == null)
            {
                locationSender = new LocationSender(gpsBuffer, cellBuffer, this);
                locationSender.start();
            }
            locationSender.setParameters(ldlUrl, app);
        }
        else
        {
            Logger.log(Logger.INFO, this.getClass().getName(), "No need to collect, stop location sender");
            if (locationSender != null)
            {
                //stop location sender
                locationSender.stop();
                locationSender = null;
            }
        }
    }

    public void notify(Object sender)
    {
        try{
            if (clientStateMonitor != null && !clientStateMonitor.isClientRunning())
            {
                //the client is not running, stop the service itself
                Logger.log(Logger.INFO, this.getClass().getName(), "the client is not running, stop the service itself");
                stopService();
            }
        }
        catch(Exception e)
        {
            Logger.log(this.getClass().getName(), e);
        }
    }

    public boolean locationArrived(TnLocation location)
    {
        try{
            if (this.isStationaryMode)
            {
                if (location != null && location.isValid())
                {
                    StationaryMonitor monitor = this.stationaryMonitor;
                    if (monitor != null && monitor.isMoving(location))
                    {
                        Logger.log(Logger.INFO, this.getClass().getName(), "The gps speed is bigger than stationary threshold in stationary mode, switch back to normal");
                        ControllerJob job = new ControllerJob();
                        job.type = ControllerJob.SWITCH_BACK_FROM_STATIONARY_MODE;
                        addJob(job);
                    }
                }
                
                //in stationary mode, we don't collect gps, so return false here
                return false;
            }
            else
            {
                StationaryMonitor monitor = this.stationaryMonitor;
                if (monitor != null)
                {
                    boolean isStationary = monitor.isStationary(location);
                    if (isStationary)
                    {
                        Logger.log(Logger.INFO, this.getClass().getName(), "The gps speed is less than stationary threshold for enough time, switch to stationary mode");
                        ControllerJob job = new ControllerJob();
                        job.type = ControllerJob.SWITCH_TO_STATIONARY_MODE;
                        addJob(job);
                    }
                }
                
                //if not in stationary mode, we need to return true to collect gps
                return true;
            }
        }
        catch(Exception e)
        {
            Logger.log(this.getClass().getName(), e);
        }
        return true;
    }
}
