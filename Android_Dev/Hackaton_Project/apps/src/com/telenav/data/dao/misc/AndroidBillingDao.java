/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * MarketBillingDao.java
 *
 */
package com.telenav.data.dao.misc;

import java.util.Vector;

import com.telenav.data.dao.AbstractDao;
import com.telenav.data.datatypes.billing.MarketPurchaseRequest;
import com.telenav.data.datatypes.primitive.BytesList;
import com.telenav.data.datatypes.primitive.StringList;
import com.telenav.data.serializable.SerializableManager;
import com.telenav.logger.Logger;
import com.telenav.persistent.TnStore;

/**
 *@author gbwang
 *@date 2012-2-11
 */
public class AndroidBillingDao extends AbstractDao
{
    private static final int KEY_CURRENT_PURCHASE_REQUEST = 1;
    
    private static final int KEY_BILLING_BACKUP_NOTIFICATION = 2;

    protected TnStore androidBillingStore;

    private Vector purchaseRequests = new Vector();
    
    private StringList notifications = new StringList();

    public AndroidBillingDao(TnStore store)
    {
        this.androidBillingStore = store;
    }

    public boolean addRequest(MarketPurchaseRequest request)
    {
        this.purchaseRequests.addElement(request);
        if (request != null)
        {
            Logger.log(Logger.INFO, this.getClass().getName(),
                "MarketBilling: addRequest productId= " + request.getProductId()
                        + " NotificationId " + request.getNotificationId() + " Status "
                        + request.getStatus());
        }
        return true;
    }

    public boolean removeRequest(String productId)
    {
        Logger.log(Logger.INFO, this.getClass().getName(), "removeRequest " + productId);
        for (int i = 0; i < this.purchaseRequests.size(); i++)
        {
            MarketPurchaseRequest temp = (MarketPurchaseRequest) this.purchaseRequests
                    .elementAt(i);
            if (productId.equals(temp.getProductId()))
            {
                this.purchaseRequests.removeElementAt(i);
                return true;
            }
        }
        return false;
    }
    
    public void removeRequestByNotification(String notifyId)
    {
        for (int i = 0; i < this.purchaseRequests.size(); i++)
        {
            MarketPurchaseRequest temp = (MarketPurchaseRequest) this.purchaseRequests
                    .elementAt(i);
            if (notifyId.equals(temp.getNotificationId()))
            {
                this.purchaseRequests.removeElementAt(i);
                break;
            }
        }
    }

    public MarketPurchaseRequest getPurchaseRequest(long requestId)
    {
        for (int i = 0; i < this.purchaseRequests.size(); i++)
        {
            MarketPurchaseRequest temp = (MarketPurchaseRequest) this.purchaseRequests
                    .elementAt(i);
            if (temp.getRequestId() == requestId)
            {
                return (MarketPurchaseRequest) this.purchaseRequests.elementAt(i);
            }
        }
        return null;
    }
    
    public MarketPurchaseRequest getPurchaseRequest(String productId)
    {
        for (int i = 0; i < this.purchaseRequests.size(); i++)
        {
            MarketPurchaseRequest temp = (MarketPurchaseRequest) this.purchaseRequests
                    .elementAt(i);
            if (productId.equals(temp.getProductId()))
            {
                return (MarketPurchaseRequest) this.purchaseRequests.elementAt(i);
            }
        }
        return null;
    }
    
    public Vector getPurchaseRequests()
    {
        return this.purchaseRequests;
    }

    public boolean isRequestExist(String productId)
    {
        for (int i = 0; i < this.purchaseRequests.size(); i++)
        {
            MarketPurchaseRequest temp = (MarketPurchaseRequest) this.purchaseRequests
                    .elementAt(i);
            if (productId.equals(temp.getProductId()))
            {
                return true;
            }
        }
        return false;
    }

    public void updateStatus(String productId, int status)
    {
        for (int i = 0; i < this.purchaseRequests.size(); i++)
        {
            MarketPurchaseRequest temp = (MarketPurchaseRequest) this.purchaseRequests
                    .elementAt(i);
            if (productId.equals(temp.getProductId()))
            {
                temp.setStatus(status);
                break;
            }
        }
    }

    public void updateNotificationId(String productId, String notificationId, String signedData, String signature, String developPayload)
    {
        for (int i = 0; i < this.purchaseRequests.size(); i++)
        {
            MarketPurchaseRequest temp = (MarketPurchaseRequest) this.purchaseRequests
                    .elementAt(i);
            if (productId.equals(temp.getProductId()))
            {
                temp.setNotificationId(notificationId);
                temp.setSignature(signature);
                temp.setSignedData(signedData);
                temp.setDevelopPayload(developPayload);
                break;
            }
        }
    }

    public StringList getBackupNotifications()
    {
        return this.notifications;
    }
    
    public void addBackupNotification(String notification)
    {
        if(notification != null)
        {
            this.notifications.add(notification);
            Logger.log(Logger.INFO, this.getClass().getName(),
                "MarketBilling: BackupBillingService addBackupNotification notification = " + notification);
        }
    }
    
    public void removeBackupNotification(String notification)
    {
        if(notification != null)
        {
            notifications.remove(notification);
        }
    }
    
    
    public void load()
    {
        byte[] data = this.androidBillingStore.get(KEY_CURRENT_PURCHASE_REQUEST);
        if (data != null)
        {
            BytesList list = SerializableManager.getInstance().getPrimitiveSerializable()
                    .createBytesList(data);
            Vector v = new Vector();
            for (int i = 0; i < list.size(); i++)
            {
                byte[] child = list.elementAt(i);
                MarketPurchaseRequest request = SerializableManager.getInstance()
                        .getAndroidBillingSerializable().createPurchaseRequest(child);
                v.addElement(request);
            }
            this.purchaseRequests = v;
        }
        
        data = this.androidBillingStore.get(KEY_BILLING_BACKUP_NOTIFICATION);
        if (data != null)
        {
            this.notifications = SerializableManager.getInstance()
                    .getPrimitiveSerializable().createStringList(data);
        }
    }

    public void clear()
    {
        if (this.purchaseRequests != null)
        {
            this.purchaseRequests.removeAllElements();
        }
        this.androidBillingStore.clear();
    }

    public void store()
    {
        if (this.purchaseRequests != null)
        {
            BytesList list = new BytesList();
            for (int i = 0; i < this.purchaseRequests.size(); i++)
            {
                MarketPurchaseRequest request = (MarketPurchaseRequest) this.purchaseRequests
                        .elementAt(i);
                list.add(SerializableManager.getInstance()
                        .getAndroidBillingSerializable().toBytes(request));
            }
            byte[] data = SerializableManager.getInstance().getPrimitiveSerializable()
                    .toBytes(list);
            this.androidBillingStore.put(KEY_CURRENT_PURCHASE_REQUEST, data);
        }
        
        if (this.notifications != null)
        {
            this.androidBillingStore.put(KEY_BILLING_BACKUP_NOTIFICATION,
                SerializableManager.getInstance().getPrimitiveSerializable().toBytes(
                    this.notifications));
        }

        this.androidBillingStore.save();
    }
}
