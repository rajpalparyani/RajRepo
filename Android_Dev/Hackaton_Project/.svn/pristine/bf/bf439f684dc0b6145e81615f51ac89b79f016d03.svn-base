/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * MigrationExecutor.java
 *
 */
package com.telenav.module.sync;

import java.util.Vector;

import com.telenav.app.CommManager;
import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.dao.serverproxy.AddressBackupDao;
import com.telenav.data.dao.serverproxy.AddressDao;
import com.telenav.data.dao.serverproxy.BillingAccountDao;
import com.telenav.data.datatypes.address.Address;
import com.telenav.data.datatypes.address.FavoriteCatalog;
import com.telenav.data.datatypes.address.Stop;
import com.telenav.data.datatypes.mandatory.MandatoryProfile.CredentialInfo;
import com.telenav.data.serverproxy.AbstractServerProxy;
import com.telenav.data.serverproxy.IServerProxyConstants;
import com.telenav.data.serverproxy.IServerProxyListener;
import com.telenav.data.serverproxy.NetworkStatusManager;
import com.telenav.data.serverproxy.impl.IAddressProxy;
import com.telenav.data.serverproxy.impl.IToolsProxy;
import com.telenav.data.serverproxy.impl.ServerProxyFactory;
import com.telenav.logger.Logger;
import com.telenav.module.AppConfigHelper;
import com.telenav.module.UserProfileProvider;
import com.telenav.module.ac.SyncStopsExecutor;

/**
 *@author yning
 *@date 2012-11-12
 */
public class MigrationExecutor implements IServerProxyListener
{
    protected IMigrationListener listener = null;
    
    private static class InnterMigrationExecutor
    {
        private static MigrationExecutor instance = new MigrationExecutor();
    }
    
    private MigrationExecutor()
    {
        
    }
    
    public static MigrationExecutor getInstance()
    {
        return InnterMigrationExecutor.instance;
    }
    
    /**
     * 
     * @param listener
     * @return false -- no need to send
     *         true -- request is sent
     */
    public synchronized boolean doMigration(IMigrationListener listener)
    {
        //that means the migration is done. No need to do it again.
        boolean isMigrationSucc = isSuccessful();
        if(isMigrationSucc)
        {
            return false;
        }
        
        boolean isMigrationInProgress = isInProgress();
        if(isMigrationInProgress)
        {
            return false;
        }
        
        if(AppConfigHelper.isLoggerEnable)
        {
            Logger.log(Logger.INFO, this.getClass().toString(), " Migration --> start fetchAll : " + System.currentTimeMillis());
        }
        
        this.listener = listener;
        
        if(isSignUp())
        {
            migrationSucc();
        }
        else
        {
            DaoManager.getInstance().getBackupAddressDao().clear();
            DaoManager.getInstance().getBackupAddressDao().store();
            
            DaoManager.getInstance().getAddressDao().setIsMigrationInProgress(true);
            DaoManager.getInstance().getAddressDao().setIsMigrationSucc(false);
            DaoManager.getInstance().getAddressDao().store();
            
            sendFetchAllStopsRequest();
        }
        
        return true;
    }
    
    /**
     * Allow invoker to set listener. 
     * Because MigrationExecutor is a singleton, module A starts migration, but it can jump to B during migration.
     * But the migration won't be interrupt. So B also need to be informed if it's finished then it can do something.
     * e.g., refresh UI.
     * @param listener
     */
    public void setListener(IMigrationListener listener)
    {
        this.listener = listener;
    }
    
    /**
     * set all the local fav/recent status to be added.
     */
    public void setAddedStatus()
    {
        Vector recentAddresses = DaoManager.getInstance().getAddressDao().getRecentAddresses();
        Vector favoriteAddresses = DaoManager.getInstance().getAddressDao().getFavorateAddresses();
        Vector favoriteCatelog = DaoManager.getInstance().getAddressDao().getFavoriteCatalog();
        Vector receivedAddresses = DaoManager.getInstance().getAddressDao().getReceivedAddresses();
        
        setAddedStatus(recentAddresses);
        setAddedStatus(favoriteAddresses);
        setAddedStatus(favoriteCatelog);
        setAddedStatus(receivedAddresses);
        
        DaoManager.getInstance().getAddressDao().setHomeWorkNeedSync(true);
    }
    
    public boolean isInProgress()
    {
        return DaoManager.getInstance().getAddressDao().isMigrationInProgress();
    }
    
    public boolean isSuccessful()
    {
        return DaoManager.getInstance().getAddressDao().isMigrationSucc();
    }
    
    public boolean isSyncEnabled()
    {
        boolean hasSignInScoutDotMe = false;
        
        CredentialInfo credentialInfo = DaoManager.getInstance().getMandatoryNodeDao().getMandatoryNode().getCredentialInfo();
        if(credentialInfo != null && credentialInfo.credentialValue != null && credentialInfo.credentialValue.trim().length() > 0)
        {
            hasSignInScoutDotMe = true;
        }
        return hasSignInScoutDotMe;
    }
    
    public void transactionFinished(AbstractServerProxy proxy, String jobId)
    {
        if (proxy instanceof IAddressProxy && IServerProxyConstants.ACT_FETCH_ALL_STOPS.equals(proxy.getRequestAction()))
        {
            migrationSucc();
            return;
        }
    }

    public void updateTransactionStatus(AbstractServerProxy proxy, byte progress)
    {
        
    }
    
    public void networkError(AbstractServerProxy proxy, byte statusCode, String jobId)
    {
        processError(proxy);
    }

    public void transactionError(AbstractServerProxy proxy)
    {
        processError(proxy);
    }

    public boolean isAllowNetworkRequest(AbstractServerProxy proxy)
    {
        if (!NetworkStatusManager.getInstance().isConnected())
        {
            return false;
        }
        return true;
    }
    
    protected boolean isSignUp()
    {
        return DaoManager.getInstance().getBillingAccountDao().getLoginType() == BillingAccountDao.ACCOUNT_ENTRY_TYPE_SIGN_UP;
    }
    
    protected void sendFetchAllStopsRequest()
    {
        UserProfileProvider userProfileProvider = new UserProfileProvider();
        
        if(isSignUp())
        {
            userProfileProvider.getMandatoryNode().getCredentialInfo().credentialID = "";
            userProfileProvider.getMandatoryNode().getCredentialInfo().credentialPassword = "";
            userProfileProvider.getMandatoryNode().getCredentialInfo().credentialType = "";
            userProfileProvider.getMandatoryNode().getCredentialInfo().credentialValue = "";
        }
        
        IAddressProxy proxy = ServerProxyFactory.getInstance().createAddressProxy(null, CommManager.getInstance().getComm(),
            this, userProfileProvider, true);
        proxy.fetchAllStops(false);
    }
    
    protected void setAddedStatus(Vector addresses)
    {
        if(addresses == null)
        {
            return;
        }
        
        for ( int i = 0; i < addresses.size(); i++ )
        {
            Object obj = addresses.elementAt(i);
            
            if (obj instanceof FavoriteCatalog)
            {
                FavoriteCatalog catalog = (FavoriteCatalog)obj;
                if(catalog.getName().equalsIgnoreCase(AddressDao.RECEIVED_ADDRESSES_CATEGORY)
                        || catalog.getStatus() == FavoriteCatalog.CATEGORY_DELETED_WITHOUT_CHILDREN
                        || catalog.getStatus() == FavoriteCatalog.CATEGORY_DELETED_WITH_CHILDREN)
                {
                    continue;
                }
                
                catalog.setId(0);
                catalog.setStatus(FavoriteCatalog.ADDED);
            }
            else if (obj instanceof Address)
            {
                Address address = (Address)obj;
                if(address.getStatus() == Address.DELETED)
                {
                    continue;
                }
                
                address.setId(0);
                address.setStatus(Address.ADDED);
            }
        }
    }

    protected void mergeStops()
    {
        AddressDao addressDao = DaoManager.getInstance().getAddressDao();
        AddressBackupDao backupAddressDao = DaoManager.getInstance().getBackupAddressDao();
                
        Vector recentAddresses = addressDao.getRecentAddresses();
        Vector favoriteAddresses = addressDao.getFavorateAddresses();
        Vector favoriteCatelog = addressDao.getFavoriteCatalog();
        Vector receivedAddresses = addressDao.getReceivedAddresses();
        Stop homeStop = addressDao.getHomeAddress();
        Stop officeStop = addressDao.getOfficeAddress();
        
        int recentSize = recentAddresses.size();
        for (int i = recentSize - 1; i >= 0; i--)
        {
            Address address = (Address)recentAddresses.elementAt(i);
            
            if(address.getStatus() == Address.DELETED)
            {
                continue;
            }
            
            // If there is same address, it will just reposition the one in the list to the first position.
            // In that case it won't use this one we pass into the method.
            // So always set status to ADDED is safe.
            address.setStatus(Address.ADDED);
            backupAddressDao.addAddress(address, false);
        }
        
        for(int i = 0; i < favoriteCatelog.size(); i++)
        {
            FavoriteCatalog catalog = (FavoriteCatalog)favoriteCatelog.elementAt(i);
            if(catalog.getStatus() == FavoriteCatalog.CATEGORY_DELETED_WITHOUT_CHILDREN
                    || catalog.getStatus() == FavoriteCatalog.CATEGORY_DELETED_WITH_CHILDREN
                    || backupAddressDao.isExistInFavoriteCatalog(catalog.getName()))
            {
                continue;
            }
            
            catalog.setStatus(FavoriteCatalog.ADDED);
            backupAddressDao.addFavoriteCatalog(catalog);
        }
        
        for(int i = 0; i < favoriteAddresses.size(); i++)
        {
            Address address = (Address)favoriteAddresses.elementAt(i);
            
            if(address.getStatus() == Address.DELETED)
            {
                continue;
            }
            
            if(backupAddressDao.isExistInFavoriteAddress(address, true))
            {
                continue;
            }
            
            address.setStatus(Address.ADDED);
            backupAddressDao.addAddress(address, false);
        }
        
        Vector newReceivedAddresses = backupAddressDao.getReceivedAddresses();
        int receiveSize = receivedAddresses.size();
        for (int i = receiveSize - 1; i >= 0; i--)
        {
            Address address = (Address)receivedAddresses.elementAt(i);
            
            if(address.getStatus() == Address.DELETED)
            {
                continue;
            }
            
            boolean isFind = false;
            for(int j = 0; j < newReceivedAddresses.size(); j++)
            {
                Address newAddress = (Address)newReceivedAddresses.elementAt(j);
                if(AddressDao.isSameAddress(address, newAddress, true))
                {
                    isFind = true;
                    break;
                }
            }
            
            if(isFind)
            {
                continue;
            }
                
            address.setStatus(Address.ADDED);
            backupAddressDao.addAddress(address, true);
        }
        
        if(backupAddressDao.getHomeAddress() == null)
        {
            backupAddressDao.setHomeAddress(homeStop, false);
        }
        
        if(backupAddressDao.getOfficeAddress() == null)
        {
            backupAddressDao.setOfficeAddress(officeStop, false);
        }
        
        backupAddressDao.sortRecentAddresses();
        backupAddressDao.store();
        backupAddressDao.cloneToAddressDao();
        addressDao.store();
    }
    
    protected void processError(AbstractServerProxy proxy)
    {
        if (proxy instanceof IAddressProxy && IServerProxyConstants.ACT_FETCH_ALL_STOPS.equals(proxy.getRequestAction()))
        {
            
            if(AppConfigHelper.isLoggerEnable)
            {
                Logger.log(Logger.INFO, this.getClass().toString(), " Migration --> merge fail : " + System.currentTimeMillis());
            }
            
            migrationFail();
        }
    }
    
    protected synchronized void migrationFail()
    {
        DaoManager.getInstance().getBackupAddressDao().clear();
        DaoManager.getInstance().getBackupAddressDao().store();
        
        DaoManager.getInstance().getAddressDao().setIsMigrationSucc(false);
        DaoManager.getInstance().getAddressDao().setIsMigrationInProgress(false);
        DaoManager.getInstance().getAddressDao().store();
    
        if(listener != null)
        {
            listener.onMigrationFinished();
        }
        listener = null;
    }
    
    protected synchronized void migrationSucc()
    {
        if(AppConfigHelper.isLoggerEnable)
        {
            Logger.log(Logger.INFO, this.getClass().toString(), " Migration --> start fetchAll : " + System.currentTimeMillis());
        }
        
        if(AppConfigHelper.isLoggerEnable)
        {
            Logger.log(Logger.INFO, this.getClass().toString(), " Migration --> merge stop : " + System.currentTimeMillis());
        }
        
        if(isSignUp())
        {
            if(AppConfigHelper.isLoggerEnable)
            {
                Logger.log(Logger.INFO, this.getClass().toString(), " Migration --> sign up, setStatus : " + System.currentTimeMillis());
            }
            setAddedStatus();
        }
        else
        {
            mergeStops();
        }
        
        SyncResExecutor.getInstance().syncPreference(null, IToolsProxy.SYNC_TYPE_UPLOAD, null, null, IToolsProxy.UPLOAD_TYPE_HOME_WORK);
        
        DaoManager.getInstance().getAddressDao().setIsMigrationSucc(true);
        DaoManager.getInstance().getAddressDao().setIsMigrationInProgress(false);
        DaoManager.getInstance().getAddressDao().store();
        
        if(listener != null)
        {
            listener.onMigrationFinished();
        }
        
        if(AppConfigHelper.isLoggerEnable)
        {
            Logger.log(Logger.INFO, this.getClass().toString(), " Migration --> do sync new stop : " + System.currentTimeMillis());
        }
        
        //once fetchAll is done, do common sync here.
        IServerProxyListener serverProxyListener = null;
        if(listener instanceof IServerProxyListener)
        {
            serverProxyListener = (IServerProxyListener)listener;
        }
        SyncStopsExecutor.getInstance().syncStop(serverProxyListener, null);
        
        listener = null;
    }
}
