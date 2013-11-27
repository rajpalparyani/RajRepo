/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * AbstractCommonNetworkModel.java
 *
 */
package com.telenav.mvc;

import com.telenav.app.ThreadManager;
import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.serverproxy.AbstractServerProxy;
import com.telenav.data.serverproxy.IServerProxyConstants;
import com.telenav.data.serverproxy.IServerProxyListener;
import com.telenav.data.serverproxy.IUserProfileProvider;
import com.telenav.data.serverproxy.NetworkStatusManager;
import com.telenav.data.serverproxy.impl.IToolsProxy;
import com.telenav.module.ac.SyncStopsExecutor;
import com.telenav.module.region.IRegionDetectMvcListener;
import com.telenav.module.region.RegionDetectResultHandler;
import com.telenav.module.region.RegionResSwitcher;
import com.telenav.module.region.RegionUtil;
import com.telenav.module.sync.IMigrationListener;
import com.telenav.module.sync.MigrationExecutor;
import com.telenav.module.sync.SyncResExecutor;
import com.telenav.res.IStringCommon;
import com.telenav.res.ResourceManager;
import com.telenav.threadpool.IJob;
import com.telenav.threadpool.ThreadPool;

/**
 *@author fqming (fqming@telenav.cn)
 *@date Aug 11, 2010
 */
public abstract class AbstractCommonNetworkModel extends AbstractCommonModel implements IServerProxyListener, IRegionDetectMvcListener, IMigrationListener
{

    public boolean isAllowNetworkRequest(AbstractServerProxy proxy)
    {
        if (!NetworkStatusManager.getInstance().isConnected())
        {
            String errorMessage = ResourceManager.getInstance().getCurrentBundle().getString(IStringCommon.RES_NO_CELL_COVERAGE, IStringCommon.FAMILY_COMMON);
            proxy.setErrorMsg(errorMessage);
            try
            {
                Thread.sleep(2000);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            return false;
        }
        return true;
    }

    public void networkError(AbstractServerProxy proxy, byte statusCode, String jobId)
    {
        String errorMessage = proxy.getErrorMsg();
        if (errorMessage == null || errorMessage.length() == 0)
        {
            errorMessage = ResourceManager.getInstance().getCurrentBundle().getString(IStringCommon.RES_SERVER_ERROR, IStringCommon.FAMILY_COMMON);
        }
        postErrorMessage(errorMessage);
    }
    
    //TODO need to add the jobid for transactinError by yren
    public void transactionError(AbstractServerProxy proxy)
    {
        String errorMessage = proxy.getErrorMsg();
        if (errorMessage == null || errorMessage.length() == 0)
        {
            errorMessage = ResourceManager.getInstance().getCurrentBundle().getString(IStringCommon.RES_SERVER_ERROR, IStringCommon.FAMILY_COMMON);
        }
        postErrorMessage(errorMessage);
    }
    
    public final void transactionFinished(AbstractServerProxy proxy, String jobId)
    {
        transactionFinishedDelegate(proxy, jobId);
    }
    
    protected abstract void transactionFinishedDelegate(AbstractServerProxy proxy, String jobId);
    
    public void updateTransactionStatus(AbstractServerProxy proxy, byte progress)
    {
        
    }

    protected void postErrorMessage(String errorMessage) 
    {
        this.put(ICommonConstants.KEY_S_ERROR_MESSAGE, errorMessage);
        this.postModelEvent(ICommonConstants.EVENT_MODEL_POST_ERROR);
    }
    
    protected void checkRegionChange()
    {
        final String region = RegionUtil.getInstance().getTargetRegion();
        if(RegionUtil.getInstance().isRegionSupported(region) && RegionUtil.getInstance().isNewRegion(region))
        {
            RegionResSwitcher regionSwitcher = new RegionResSwitcher(new RegionResSwitcher.IRegionResSwitcherListener()
            {
                public void onRegionSwitched(String message, int resultType)
                {
                    if (resultType == RegionResSwitcher.SUCCESS_FROM_SERVER)
                    {
                        RegionUtil.getInstance().setCurrentRegion(region);
                        RegionUtil.getInstance().clearTargetRegion(region);
                        postModelEvent(ICommonConstants.EVENT_MODEL_SWITCHING_SUCC_FROM_SERVER);
                    }
                    else if(resultType == RegionResSwitcher.FAIL)
                    {
                        postModelEvent(ICommonConstants.EVENT_MODEL_SWITCHING_FAIL);
                    }
                }
            });
            
            int resultType = regionSwitcher.switchRegion(region);
            if(resultType == RegionResSwitcher.SUCCESS_FROM_CACHE)
            {
                RegionUtil.getInstance().setCurrentRegion(region);
                RegionUtil.getInstance().clearTargetRegion(region);
                postModelEvent(ICommonConstants.EVENT_MODEL_CONTINUE);
            }
            else if (resultType == RegionResSwitcher.SUCCESS_FROM_SERVER)
            {
                postModelEvent(ICommonConstants.EVENT_MODEL_SWITCHING_REGION);
            }
        }
        else
        {
            RegionUtil.getInstance().clearTargetRegion(region);
            postModelEvent(ICommonConstants.EVENT_MODEL_CONTINUE);
        }
        
    }
    
    public synchronized void checkRegionDetectStatus()
    {
        RegionDetectResultHandler resultHandler = RegionDetectResultHandler.getInstance();
        resultHandler.checkRegionStatus(this);
    }
    
    protected void handleRegionDetected(String detectedRegion)
    {
        if (RegionUtil.getInstance().isLegalRegion(detectedRegion))
        {
            RegionUtil.getInstance().setTargetRegion(detectedRegion);
        }
        
        postModelEvent(ICommonConstants.EVENT_MODEL_CHECK_REGION);
    }
    
    public void regionNotDetected()
    {
        postModelEvent(ICommonConstants.EVENT_MODEL_DETECTING_REGION);
    }
    
    public void regionAlreadyDetected(String region)
    {
        handleRegionDetected(region);
    }
    
    protected void syncStops()
    {
        boolean isSyncEnabled = MigrationExecutor.getInstance().isSyncEnabled();
        boolean isnetworkAvailable = NetworkStatusManager.getInstance().isConnected();
        if(isSyncEnabled && isnetworkAvailable)
        {
            final IUserProfileProvider userProfileProvider = (IUserProfileProvider)get(ICommonConstants.KEY_O_USER_PROFILE_PROVIDER);
            
            IJob syncJob = new IJob()
            {
                
                public void execute(int handlerID)
                {
                    
                    //don't do sync before migration succ.
                    if(MigrationExecutor.getInstance().isInProgress())
                    {
                        MigrationExecutor.getInstance().setListener(AbstractCommonNetworkModel.this);
                        return;
                    }
                    else
                    {
                        boolean isMigrationSucc = DaoManager.getInstance().getAddressDao().isMigrationSucc();
                        
                        //don't do normal sync before migration succ.
                        if(isMigrationSucc)
                        {
                            SyncStopsExecutor.getInstance().syncStop(AbstractCommonNetworkModel.this, userProfileProvider);
                            IServerProxyListener listener = new IServerProxyListener()
                            {
                                @Override
                                public void updateTransactionStatus(AbstractServerProxy proxy, byte progress)
                                {
                                    
                                }
                                
                                @Override
                                public void transactionFinished(AbstractServerProxy proxy, String jobId)
                                {
                                    String action = proxy.getRequestAction();
                                    if(proxy instanceof IToolsProxy && IServerProxyConstants.ACT_SYNC_PREFERENCE.equals(action))
                                    {
                                        SyncResExecutor.getInstance().handlePreferenceResp((IToolsProxy)proxy);
                                    }
                                }
                                
                                @Override
                                public void transactionError(AbstractServerProxy proxy)
                                {
                                    
                                }
                                
                                @Override
                                public void networkError(AbstractServerProxy proxy, byte statusCode, String jobId)
                                {
                                    
                                }
                                
                                @Override
                                public boolean isAllowNetworkRequest(AbstractServerProxy proxy)
                                {
                                    return AbstractCommonNetworkModel.this.isAllowNetworkRequest(proxy);
                                }
                            };
                            SyncResExecutor.getInstance().syncPreference(null, IToolsProxy.SYNC_TYPE_DOWNLOAD, listener, null, -1);
                        }
                        else
                        {
                            MigrationExecutor.getInstance().doMigration(AbstractCommonNetworkModel.this);
                        }
                    }
                }

                public void cancel()
                {
                    
                }

                public boolean isCancelled()
                {
                    return false;
                }

                public boolean isRunning()
                {
                    return true;
                }
            };
            ThreadPool pool = ThreadManager.getPool(ThreadManager.TYPE_APP_ACTION);
            pool.addJob(syncJob);
        }
    }
    
    public void onMigrationFinished()
    {
        
    }
}
