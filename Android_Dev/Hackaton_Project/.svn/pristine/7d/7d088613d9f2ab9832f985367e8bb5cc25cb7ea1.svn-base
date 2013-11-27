/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * MarketBilling.java
 *
 */
package com.telenav.data.datatypes.billing;

import com.telenav.logger.Logger;

/**
 *@author gbwang
 *@date 2012-2-11
 */
public class MarketPurchaseRequest
{
    private String productId;//[0]

    private String notificationId;//[1]
    
    private String billingOrderId;//[2]
    
    private long requestId = -1;//[0]

    private int synchronousResponseCode = -1;//[1]

    private int asynchronousResponseCode = -1;//[2]

    private int status = -1;//[3]
    
    private String signedData;
    private String signature;
    private String developPayload;

    public void setSignedData(String signedData)
    {
        this.signedData = signedData;
    }
    
    public String getSignedData()
    {
        return this.signedData;
    }
    
    public void setSignature(String signature)
    {
        this.signature = signature;
    }
    
    public String getSignature()
    {
        return this.signature;
    }
    
    public void setDevelopPayload(String developPayload)
    {
        this.developPayload = developPayload;
    }
    
    public String getDevelopPayload()
    {
        return this.developPayload;
    }
    
    public void setProductId(String productId)
    {
        this.productId = productId;
    }

    public String getProductId()
    {
        return this.productId;
    }
    
    public void setNotificationId(String notificationId)
    {
        this.notificationId = notificationId;
        Logger.log(Logger.INFO, this.getClass().getName(), "MarketBilling: setNotificationId " + notificationId);
    }

    public String getNotificationId()
    {
        return this.notificationId;
    }
    
    public void setRequestId(long requestId)
    {
        this.requestId = requestId;
    }

    public long getRequestId()
    {
        return this.requestId;
    }

    public void setSynchronousRespCode(int synchronousResponseCode)
    {
        this.synchronousResponseCode = synchronousResponseCode;
    }

    public int getSynchronousRespCode()
    {
        return this.synchronousResponseCode;
    }

    public void setAsynchronousRespCode(int asynchronousResponseCode)
    {
        this.asynchronousResponseCode = asynchronousResponseCode;
    }

    public int getAsynchronousRespCode()
    {
        return this.asynchronousResponseCode;
    }

    public void setStatus(int status)
    {
        this.status = status;
        Logger.log(Logger.INFO, this.getClass().getName(), "MarketBilling: setStatus " + status);
    }

    public int getStatus()
    {
        return this.status;
    }
    
    public void setBillingOrderId(String billingOrderId)
    {
        this.billingOrderId = billingOrderId;
    }

    public String getBillingOrderId()
    {
        return this.billingOrderId;
    }
}
