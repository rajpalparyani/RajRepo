/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * BillingService.java
 *
 */
package com.telenav.app.android.scout_us;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;

import com.android.vending.billing.IMarketBillingService;
import com.telenav.app.AbstractPlatformIniter;
import com.telenav.app.TeleNavDelegate;
import com.telenav.app.android.AndroidPersistentContext;
import com.telenav.app.android.AndroidPlatformIniter;
import com.telenav.data.dao.misc.AndroidBillingDao;
import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.dao.serverproxy.AbstractDaoManager;
import com.telenav.data.datatypes.billing.MarketPurchaseRequest;
import com.telenav.data.serializable.SerializableManager;
import com.telenav.data.serializable.txnode.TxNodeSerializableManager;
import com.telenav.logger.Logger;
import com.telenav.module.marketbilling.IMarketBillingConstants;
import com.telenav.module.marketbilling.MarketBillingHandler;
import com.telenav.persistent.TnPersistentManager;
import com.telenav.persistent.android.AndroidPersistentManager;
import com.telenav.res.IStringCommon;
import com.telenav.res.ResourceManager;

/**
 *@author gbwang
 *@date 2012-2-6
 */
public class BillingService extends Service implements ServiceConnection
{
    private static IMarketBillingService mService;

    private Method mStartIntentSender;

    private Object[] mStartIntentSenderArgs = new Object[5];

    private static final Class[] START_INTENT_SENDER_SIG = new Class[]
    { IntentSender.class, Intent.class, int.class, int.class, int.class };

    private static LinkedList<BillingRequest> mPendingRequests = new LinkedList<BillingRequest>();

    private static HashMap<Long, BillingRequest> mSentRequests = new HashMap<Long, BillingRequest>();

    private boolean hasBind = false;
    
    public BillingService()
    {
        super();
    }
    
    public void onDestroy()
    {
        super.onDestroy();
        try
        {
            if(hasBind)
            {
                unbindService(this);
                if (Logger.DEBUG)
                {
                    Logger.log(Logger.INFO, this.getClass().getName(), "MarketBilling: onDestroy unbindService");
                }
            }
        }
        catch (IllegalArgumentException e)
        {

        }
        
    }
    
    public void setContext(Context context)
    {
        attachBaseContext(context);
    }

    public IBinder onBind(Intent arg0)
    {
        return null;
    }

    private boolean bindToMarketBillingService()
    {
        try
        {
            hasBind = bindService(new Intent(
                    IMarketBillingConstants.MARKET_BILLING_SERVICE_ACTION), this,
                Context.BIND_AUTO_CREATE);
            if(hasBind)
            {
                return true;
            }
        }
        catch (SecurityException e)
        {

        }
        return false;
    }

    public void onServiceConnected(ComponentName name, IBinder service)
    {
        if (Logger.DEBUG)
        {
            Logger.log(Logger.INFO, this.getClass().getName(), "MarketBilling: onServiceConnected");
        }
        setService(IMarketBillingService.Stub.asInterface(service));
        runPendingRequest();
    }

    public void onServiceDisconnected(ComponentName arg0)
    {
        setService(null);
        MarketBillingHandler handler = TeleNavDelegate.getInstance().getBillingHandler();
        if(handler != null)
        {
            handler.handlePurchaseCommonFail();
        }
        
        if (Logger.DEBUG)
        {
            Logger.log(Logger.INFO, this.getClass().getName(), "MarketBilling: onServiceDisconnected");
        }
    }
    
    private static void setService(IMarketBillingService service)
    {
        mService = service;
    }
    
    private void startBuyPageActivity(PendingIntent pendingIntent, Intent intent)
    {
        Activity mainActivity = (Activity) AndroidPersistentContext.getInstance().getContext();
        try
        {
            mStartIntentSender = mainActivity.getClass().getMethod("startIntentSender",
                START_INTENT_SENDER_SIG);
        }
        catch (SecurityException e)
        {
            mStartIntentSender = null;
        }
        catch (NoSuchMethodException e)
        {
            mStartIntentSender = null;
        }
        
        if (mStartIntentSender != null)
        {
            try
            {
                mStartIntentSenderArgs[0] = pendingIntent.getIntentSender();
                mStartIntentSenderArgs[1] = intent;
                mStartIntentSenderArgs[2] = Integer.valueOf(0);
                mStartIntentSenderArgs[3] = Integer.valueOf(0);
                mStartIntentSenderArgs[4] = Integer.valueOf(0);

                mStartIntentSender.invoke(mainActivity, mStartIntentSenderArgs);
            }
            catch (IllegalArgumentException e)
            {
                e.printStackTrace();
            }
            catch (IllegalAccessException e)
            {
                e.printStackTrace();
            }
            catch (InvocationTargetException e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            try
            {
                pendingIntent.send(mainActivity, 0, intent);
            }
            catch (CanceledException e)
            {
                if (Logger.DEBUG)
                {
                    Logger.log(Logger.INFO, this.getClass().getName(),
                        "MarketBilling: startBuyPageActivity Exception");
                }
            }
        }
        
    }

    public int onStartCommand(Intent intent, int flags, int startId)
    {
        if(intent != null)
        {
            handleCommand(intent, startId);
        }

        return START_STICKY;
    }

    private void handleCommand(Intent intent, int startId)
    {
        String action = intent.getAction();
        if (IMarketBillingConstants.ACTION_GET_PURCHASE_INFORMATION.equals(action))
        {
            String notifyId = intent
                    .getStringExtra(IMarketBillingConstants.EXTRA_NOTIFICATION_ID);
            
            if (Logger.DEBUG)
            {
                Logger.log(Logger.INFO, this.getClass().getName(), "MarketBilling: handleCommand action: "
                        + action + ", " + "notifyId: " + notifyId);
            }
            
            if (notifyId != null)
            {
                if (TeleNavDelegate.getInstance().isStarted())
                {
                    getPurchaseInformation(startId, new String[]
                    { notifyId });
                }
                else
                {
                    initContext();
                    storeNotification(notifyId);
                    
                    String lable = ResourceManager.getInstance().getCurrentBundle()
                            .getString(IStringCommon.RES_MARKET_BILLING_NOTIFICATION, IStringCommon.FAMILY_COMMON);
                    com.telenav.app.NotificationManager.getInstance()
                            .showMarketBillingNotification(lable, null, IMarketBillingConstants.ACTION_BILLING_SHOW_NOTIFY_RECEIVED);
                }
            }
        }
        else if (IMarketBillingConstants.ACTION_PURCHASE_STATE_CHANGED.equals(action))
        {
            String signedData = intent
                    .getStringExtra(IMarketBillingConstants.EXTRA_SIGNED_DATA);
            String signature = intent.getStringExtra(IMarketBillingConstants.EXTRA_SIGNATURE);
            
            if (Logger.DEBUG)
            {
                Logger.log(Logger.INFO, this.getClass().getName(), "MarketBilling: handleCommand action: "
                        + action + ", " + "signedData: " + signedData + ", " + "signature: "
                        + signature);
            }
            
            if(signedData != null && signature != null)
            {
                purchaseStateChanged(startId, signedData, signature);
            }
        }
        else if (IMarketBillingConstants.ACTION_RESPONSE_CODE.equals(action))
        {
            long requestId = intent.getLongExtra(IMarketBillingConstants.EXTRA_REQUEST_ID, -1);
            int responseCode = intent.getIntExtra(IMarketBillingConstants.EXTRA_RESPONSE_CODE,
                IMarketBillingConstants.RESULT_ERROR);
            
            if (Logger.DEBUG)
            {
                Logger.log(Logger.INFO, this.getClass().getName(), "MarketBilling: handleCommand action: "
                        + action + ", " + "requestId: " + requestId + ", " + "responseCode: "
                        + responseCode);
            }

            checkResponseCode(requestId, responseCode);
        }
        else if(IMarketBillingConstants.ACTION_REQUEST_PURCHASE.equals(action))
        {
            String productId = intent.getStringExtra(IMarketBillingConstants.EXTRA_PRODUCT_ID);
            String developPayload = intent.getStringExtra(IMarketBillingConstants.EXTRA_DEVELOPER_PAYLOAD);
            Logger.log(Logger.INFO, this.getClass().getName(),
                "MarketBilling: handleCommand action: " + action + ", " + "productId: "
                        + productId + ", " + "developPayload: " + developPayload);
            checkBillingSupported(productId, developPayload);
        }
        else if (IMarketBillingConstants.ACTION_CONFIRM_NOTIFICATION.equals(action))
        {
            String notifyId = intent
                    .getStringExtra(IMarketBillingConstants.EXTRA_NOTIFICATION_ID);
            
            if (Logger.DEBUG)
            {
                Logger.log(Logger.INFO, this.getClass().getName(), "MarketBilling: handleCommand action: " + action + " notifyId: " + notifyId);
            }
            
            if (notifyId != null)
            {
                confirmNotifications(startId, new String[]
                { notifyId });
            }
        }
    }

    private void checkResponseCode(long requestId, int responseCode)
    {
        BillingRequest request = mSentRequests.get(requestId);
        if (request != null)
        {
            request.responseCodeReceived(responseCode);
            mSentRequests.remove(requestId);
        }        
    }
    
    private void initContext()
    {
        AndroidPersistentContext.getInstance().init(this);
        TnPersistentManager.init(new AndroidPersistentManager(), AndroidPersistentContext.getInstance());
        if (AndroidPersistentContext.getInstance().getApplicationSQLiteDatabase() == null)
        {
            AndroidPersistentContext.getInstance().openSQLiteDatabase();
        }
        
        AbstractDaoManager.init(new DaoManager());
        if(SerializableManager.getInstance() == null)
        {
            SerializableManager.init(new TxNodeSerializableManager());
        }
        if (com.telenav.app.NotificationManager.getInstance() == null)
        {
            com.telenav.app.NotificationManager.init(new AndroidNotificationManager(this));
        }
        if(AbstractPlatformIniter.getInstance() == null)
        {
            AbstractPlatformIniter.init(new AndroidPlatformIniter());
        }
        AbstractPlatformIniter.getInstance().initIo();
    }
    
    private void storeNotification(String notifyId)
    {
        AndroidBillingDao androidBillingDao = DaoManager.getInstance()
                .getAndroidBillingDao();
        if (androidBillingDao != null)
        {
            androidBillingDao.addBackupNotification(notifyId);
            androidBillingDao.store();
        }
    }

    abstract class BillingRequest
    {
        private final int mStartId;
        
        protected long requestId = -1;

        protected int synchronousRespCode = -1;

        protected int asynchronousRespCode = -1;

        public BillingRequest(int startId)
        {
            mStartId = startId;
        }

        public int getStartId()
        {
            return mStartId;
        }

        public boolean runRequest()
        {
            if (mService != null)
            {
                try
                {
                    this.requestId = run();
                    if (this.requestId >= 0)
                    {
                        mSentRequests.put(this.requestId, this);
                    }
                    return true;
                }
                catch (RemoteException e)
                {
                    onRemoteException(e);
                }
            }
            
            if (bindToMarketBillingService())
            {
                mPendingRequests.add(this);
                return true;
            }

            return false;
        }

        protected void onRemoteException(RemoteException e)
        {
            mService = null;
            
            if (Logger.DEBUG)
            {
                Logger.log(Logger.INFO, this.getClass().getSimpleName(),
                    "MarketBilling: onRemoteException");
            }
        }

        abstract protected long run() throws RemoteException;

        protected Bundle makeRequestBundle(String method)
        {
            Bundle request = new Bundle();
            request.putString(IMarketBillingConstants.BILLING_REQUEST_METHOD, method);
            request.putInt(IMarketBillingConstants.BILLING_REQUEST_API_VERSION, 1);
            request.putString(IMarketBillingConstants.BILLING_REQUEST_PACKAGE_NAME,
                getPackageName());

            return request;
        }

        // use this to handle the onReceive() response code. this will be called when this request has been sent.
        protected void responseCodeReceived(int responseCode)
        {
            this.asynchronousRespCode = responseCode;
        }
    }

    class CheckBillingSupported extends BillingRequest
    {
        final String itemId;

        final String mDevelopPayload;
        
        public CheckBillingSupported(String productId, String developPayload)
        {
            super(-1);
            this.itemId = productId;
            this.mDevelopPayload = developPayload;
        }

        protected long run() throws RemoteException
        {
            Bundle request = makeRequestBundle("CHECK_BILLING_SUPPORTED");
            Bundle response = mService.sendBillingRequest(request);
            synchronousRespCode = response
                    .getInt(IMarketBillingConstants.BILLING_RESPONSE_RESPONSE_CODE);
            if(synchronousRespCode == IMarketBillingConstants.RESULT_OK)
            {
                if (Logger.DEBUG)
                {
                    Logger.log(Logger.INFO, this.getClass().getName(),
                        "MarketBilling: CheckBillingSupported SynchronousRespCode " + synchronousRespCode);
                }
                requestPurchase(itemId, mDevelopPayload);
            }
            else
            {
                MarketBillingHandler handler = TeleNavDelegate.getInstance().getBillingHandler();
                if(handler != null)
                {
                    handler.handleBillingNotSupported();
                    handler.declinePurchase(mDevelopPayload);
                }
            }

            return -1;
        }
    }

    class RequestPurchase extends BillingRequest
    {
        final String itemId;

        final String mDevelopPayload;

        public RequestPurchase(String productId, String developPayload)
        {
            super(-1);
            this.itemId = productId;
            this.mDevelopPayload = developPayload;
        }

        protected long run() throws RemoteException
        {
            Bundle request = makeRequestBundle("REQUEST_PURCHASE");
            request.putString(IMarketBillingConstants.BILLING_REQUEST_ITEM_ID, itemId);
            request.putString(IMarketBillingConstants.BILLING_REQUEST_DEVELOPER_PAYLOAD, mDevelopPayload);
            
            Bundle response = mService.sendBillingRequest(request);
            
            this.synchronousRespCode = response
                    .getInt(IMarketBillingConstants.BILLING_RESPONSE_RESPONSE_CODE);
            this.requestId = response.getLong(
                IMarketBillingConstants.BILLING_RESPONSE_REQUEST_ID, -1);

            PendingIntent pendingIntent = response
                    .getParcelable(IMarketBillingConstants.BILLING_RESPONSE_PURCHASE_INTENT);
            if (pendingIntent == null)
            {
                return -1;
            }

            Intent intent = new Intent();
            startBuyPageActivity(pendingIntent, intent);

            if (Logger.DEBUG)
            {
                Logger.log(Logger.INFO, this.getClass().getName(),
                    "MarketBilling: RequestPurchase SynchronousRespCode "
                            + synchronousRespCode);
            }
            
            if(synchronousRespCode != IMarketBillingConstants.RESULT_OK)
            {
                MarketBillingHandler handler = TeleNavDelegate.getInstance().getBillingHandler();
                if(handler != null)
                {
                    handler.handleResponseCode(synchronousRespCode);
                    handler.declinePurchase(mDevelopPayload);
                }
            }
            
            return this.requestId;
        }

        protected void responseCodeReceived(int responseCode)
        {
            super.responseCodeReceived(responseCode);
            
            if (Logger.DEBUG)
            {
                Logger.log(Logger.INFO, this.getClass().getName(),
                    "MarketBilling: RequestPurchase AsynchronousRespCode "
                            + responseCode);
            }
            
            if (responseCode != IMarketBillingConstants.RESULT_OK)
            {
                MarketBillingHandler handler = TeleNavDelegate.getInstance().getBillingHandler();
                if(handler != null)
                {
                    handler.handleResponseCode(responseCode);
                    handler.declinePurchase(mDevelopPayload);
                }
            }
        }
    }

    class GetPurchaseInformation extends BillingRequest
    {
        long mNonce;

        final String[] mNotifyIds;

        public GetPurchaseInformation(int startId, String[] notifyIds)
        {
            super(startId);
            mNotifyIds = notifyIds;
        }

        protected long run() throws RemoteException
        {
            mNonce = Security.generateNonce();

            Bundle request = makeRequestBundle("GET_PURCHASE_INFORMATION");
            request.putLong(IMarketBillingConstants.BILLING_REQUEST_NONCE, mNonce);
            request.putStringArray(IMarketBillingConstants.BILLING_REQUEST_NOTIFY_IDS,
                mNotifyIds);

            Bundle response = mService.sendBillingRequest(request);
            this.synchronousRespCode = response
                    .getInt(IMarketBillingConstants.BILLING_RESPONSE_RESPONSE_CODE);
            this.requestId = response.getLong(
                IMarketBillingConstants.BILLING_RESPONSE_REQUEST_ID, -1);

            if (Logger.DEBUG)
            {
                Logger.log(Logger.INFO, this.getClass().getName(),
                    "MarketBilling: GetPurchaseInformation SynchronousRespCode "
                            + synchronousRespCode);
            }
            
            return this.requestId;
        }

        protected void responseCodeReceived(int responseCode)
        {
            super.responseCodeReceived(responseCode);
            
            if (Logger.DEBUG)
            {
                Logger.log(Logger.INFO, this.getClass().getName(),
                    "MarketBilling: GetPurchaseInformation AsynchronousRespCode "
                            + responseCode);
            }
        }

        protected void onRemoteException(RemoteException e)
        {
            super.onRemoteException(e);
            Security.removeNonce(mNonce);
        }
    }

    class ConfirmNotifications extends BillingRequest
    {
        final String[] mNotifyIds;

        public ConfirmNotifications(int startId, String[] notifyIds)
        {
            super(startId);
            mNotifyIds = notifyIds;
        }

        protected long run() throws RemoteException
        {
            Bundle request = makeRequestBundle("CONFIRM_NOTIFICATIONS");
            request.putStringArray(IMarketBillingConstants.BILLING_REQUEST_NOTIFY_IDS,
                mNotifyIds);

            Bundle response = mService.sendBillingRequest(request);
            this.synchronousRespCode = response
                    .getInt(IMarketBillingConstants.BILLING_RESPONSE_RESPONSE_CODE);
            this.requestId = response.getLong(
                IMarketBillingConstants.BILLING_RESPONSE_REQUEST_ID, -1);

            if (Logger.DEBUG)
            {
                Logger.log(Logger.INFO, this.getClass().getName(),
                    "MarketBilling: ConfirmNotifications SynchronousRespCode "
                            + synchronousRespCode);
            }
            
            return this.requestId;
        }

        protected void responseCodeReceived(int responseCode)
        {
            super.responseCodeReceived(responseCode);
            
            if (Logger.DEBUG)
            {
                Logger.log(Logger.INFO, this.getClass().getName(),
                    "MarketBilling: ConfirmNotifications AsynchronousRespCode "
                            + responseCode);
            }
            
            AndroidBillingDao androidBillingDao = DaoManager.getInstance()
                    .getAndroidBillingDao();
            if (androidBillingDao != null)
            {
                for(int i = 0; i < mNotifyIds.length; i++)
                {
                    androidBillingDao.removeRequestByNotification(mNotifyIds[i]);
                    androidBillingDao.removeBackupNotification(mNotifyIds[i]);
                }
                androidBillingDao.store();
            }
        }
    }

    private void checkBillingSupported(final String productId, final String developPayload)
    {
        boolean isServiceConnected = new CheckBillingSupported(productId, developPayload)
                .runRequest();
        if (!isServiceConnected)
        {
            MarketBillingHandler handler = TeleNavDelegate.getInstance()
                    .getBillingHandler();
            if (handler != null)
            {
                handler.handleResponseCode(IMarketBillingConstants.RESULT_SERVICE_UNAVAILABLE);
                handler.declinePurchase(developPayload);
            }
        }
    }

    private void requestPurchase(final String productId, final String developPayload)
    {
        boolean requested = false;
        for (int i = 0; i < mPendingRequests.size(); i++)
        {
            BillingRequest request = mPendingRequests.get(i);
            if (request instanceof RequestPurchase)
            {
                String id = ((RequestPurchase) request).itemId;
                if (productId.equals(id))
                {
                    requested = true;
                    break;
                }
            }
        }
        if (!requested)
        {
            boolean isServiceConnected = new RequestPurchase(productId, developPayload)
                    .runRequest();
            if (!isServiceConnected)
            {
                MarketBillingHandler handler = TeleNavDelegate.getInstance()
                        .getBillingHandler();
                if (handler != null)
                {
                    handler.handleResponseCode(IMarketBillingConstants.RESULT_SERVICE_UNAVAILABLE);
                    handler.declinePurchase(developPayload);
                }
            }
        }
        else
        {
            if (Logger.DEBUG)
            {
                Logger.log(Logger.INFO, this.getClass().getSimpleName(),
                    "MarketBilling: The product is already in purchase progress.");
            }
        }
    }

    public void getPurchaseInformation(int startId, String[] notifyIds)
    {
        new GetPurchaseInformation(startId, notifyIds).runRequest();
    }

    public void confirmNotifications(int startId, String[] notifyIds)
    {
       new ConfirmNotifications(startId, notifyIds).runRequest();
    }

    private void runPendingRequest()
    {
        int maxStartId = -1;
        BillingRequest request;
        while ((request = mPendingRequests.peek()) != null)
        {
            if (mService != null)
            {
                try
                {
                    request.run();
                }
                catch (RemoteException e)
                {
                    request.onRemoteException(e);
                }
                mPendingRequests.remove();
                
                if (maxStartId < request.getStartId())
                {
                    maxStartId = request.getStartId();
                }
            }
            else
            {
                bindToMarketBillingService();
                return;
            }
        }
        
        if (maxStartId >= 0)
        {
            stopSelf(maxStartId);
            if (Logger.DEBUG)
            {
                Logger.log(Logger.INFO, this.getClass().getSimpleName(),
                    "MarketBilling: stopping service, startId: " + maxStartId);
            }
        }
    }

    public void purchaseStateChanged(int startId, String signedData, String signature)
    {
        JSONObject jObject;
        JSONArray jTransactionsArray = null;
        try
        {
            jObject = new JSONObject(signedData);
            long nonce = jObject.optLong("nonce");
            if (!Security.isKnownNonce(nonce))
            {
                return;
            }

            jTransactionsArray = jObject.getJSONArray("orders");
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        boolean needVerify = false;
        try
        {
            if (jTransactionsArray != null)
            {
                for (int i = 0; i < jTransactionsArray.length(); i++)
                {
                    JSONObject jElement = jTransactionsArray.getJSONObject(i);
                    String productId = jElement.getString(IMarketBillingConstants.ORDER_INFO_PRODUCT_ID);
                    int purchaseState = jElement.getInt(IMarketBillingConstants.ORDER_INFO_PURCHASE_STATE);
                    String developPayload = jElement.getString(IMarketBillingConstants.ORDER_INFO_DEVELOPER_PAYLOAD);
                    String notifyId = null;
                    if (jElement.has(IMarketBillingConstants.ORDER_INFO_NOTIFICATION_ID))
                    {
                        notifyId = jElement.getString(IMarketBillingConstants.ORDER_INFO_NOTIFICATION_ID);
                    }
                    
                    if (Logger.DEBUG)
                    {
                        Logger.log(Logger.INFO, this.getClass().getName(), "MarketBilling: purchaseStateChanged, productId : " + productId
                            + " purchaseState : " + purchaseState + " developPayload : " + developPayload + " notifyId : " + notifyId);
                    }
                    
                    if(purchaseState != IMarketBillingConstants.PURCHASED)
                    {
                        confirmNotifications(startId, new String[]
                        { notifyId });
                        continue;
                    }
                    
                    AndroidBillingDao androidBillingDao = DaoManager.getInstance()
                            .getAndroidBillingDao();
                    if (androidBillingDao != null)
                    {
                        MarketPurchaseRequest request = androidBillingDao.getPurchaseRequest(productId);
                        
                        if (request != null)
                        {
                            if (Logger.DEBUG)
                            {
                                Logger.log(Logger.INFO, this.getClass().getName(), "MarketBilling: pending request : " + request.toString());
                            }
                            
                            int status = request.getStatus();
                            String current_developPayload = request.getDevelopPayload();
                            if (status != -1 && status == purchaseState && developPayload != null && developPayload.equals(current_developPayload))
                            {
                                if (notifyId != null)
                                {
                                    if (Logger.DEBUG)
                                    {
                                        Logger.log(Logger.INFO, this.getClass().getName(), "MarketBilling: handled before, just confirm, developPayload: " + developPayload);
                                    }
                                    
                                    confirmNotifications(startId, new String[]
                                    { notifyId });
                                    
                                    MarketBillingHandler handler = TeleNavDelegate.getInstance().getBillingHandler();
                                    if (handler != null)
                                    {
                                        handler.handleBillingSuccess();
                                    }
                                }
                            }
                            else
                            {
                                needVerify = true;
                                if (notifyId != null)
                                {
                                    androidBillingDao.updateNotificationId(productId, notifyId, signedData, signature, developPayload);
                                    androidBillingDao.store();
                                }
                            }
                        }
                        else
                        {
                            needVerify = true;
                            request = new MarketPurchaseRequest();
                            request.setProductId(productId);
                            request.setSignature(signature);
                            request.setSignedData(signedData);
                            request.setDevelopPayload(developPayload);
                            if (notifyId != null)
                            {
                                request.setNotificationId(notifyId);
                            }
                            
                            androidBillingDao.addRequest(request);
                            androidBillingDao.store();
                        }
                    }
                    else
                    {
                        needVerify = true;
                    }
                }
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (Logger.DEBUG)
            {
                Logger.log(Logger.INFO, this.getClass().getName(), "MarketBilling: verify ? : " + needVerify );         
            }
            
            if (needVerify)
            {
                MarketBillingHandler handler = TeleNavDelegate.getInstance().getBillingHandler();
                if (handler != null)
                {
                    handler.doVerifyPurchase(signedData, signature);
                }
            }
        }
    }
}

class Security
{
    private static final SecureRandom RANDOM = new SecureRandom();
    
    private static HashSet<Long> knownNonces = new HashSet<Long>();
    
    public static long generateNonce()
    {
        long nonce = RANDOM.nextLong();
        knownNonces.add(nonce);
        return nonce;
    }
    
    public static void removeNonce(long nonce)
    {
        knownNonces.remove(nonce);
    }
    
    public static boolean isKnownNonce(long nonce)
    {
        return knownNonces.contains(nonce);
    }
}
