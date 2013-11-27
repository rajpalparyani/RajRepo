/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * TxNodeAndroidBillingRequest.java
 *
 */
package com.telenav.data.serializable.txnode;

import com.telenav.data.datatypes.billing.MarketPurchaseRequest;
import com.telenav.data.serializable.IAndroidBillingSerializable;

/**
 *@author gbwang
 *@date 2012-2-14
 */
public class TxNodeAndroidBillingSerializable implements IAndroidBillingSerializable
{
    public MarketPurchaseRequest createPurchaseRequest(byte[] data)
    {
        Node node = new Node(data, 0);
        MarketPurchaseRequest result = new MarketPurchaseRequest();
        if (node != null)
        {
            result.setProductId(node.getStringAt(0));
            result.setNotificationId(node.getStringAt(1));
            result.setBillingOrderId(node.getStringAt(2));
            result.setSignedData(node.getStringAt(3));
            result.setSignature(node.getStringAt(4));
            result.setDevelopPayload(node.getStringAt(5));
            result.setRequestId(node.getValueAt(0));
            result.setSynchronousRespCode((int) node.getValueAt(1));
            result.setAsynchronousRespCode((int) node.getValueAt(2));
            result.setStatus((int) node.getValueAt(3));
        }
        return result;
    }

    public byte[] toBytes(MarketPurchaseRequest request)
    {
        Node node = new Node();
        node.addString(request.getProductId());// [0]
        node.addString(request.getNotificationId());//[1]
        node.addString(request.getBillingOrderId());// [2]
        node.addString(request.getSignedData()); //[3]
        node.addString(request.getSignature()); //[4]
        node.addString(request.getDevelopPayload()); //[5]
        node.addValue(request.getRequestId());// [0]
        node.addValue(request.getSynchronousRespCode());// [1]
        node.addValue(request.getAsynchronousRespCode());// [2]
        node.addValue(request.getStatus());// [3]

        return node.toBinary();
    }

}
