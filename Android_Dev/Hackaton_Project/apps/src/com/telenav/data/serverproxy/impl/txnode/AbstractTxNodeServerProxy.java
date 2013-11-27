/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * AbstractTxNodeServerProxy.java
 *
 */
package com.telenav.data.serverproxy.impl.txnode;

import com.telenav.comm.Comm;
import com.telenav.comm.Host;
import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.dao.serverproxy.MandatoryNodeDao;
import com.telenav.data.datatypes.mandatory.MandatoryProfile;
import com.telenav.data.serializable.SerializableManager;
import com.telenav.data.serverproxy.AbstractServerProxy;
import com.telenav.data.serverproxy.IServerProxyConstants;
import com.telenav.data.serverproxy.IServerProxyListener;
import com.telenav.data.serverproxy.IUserProfileProvider;
import com.telenav.data.serverproxy.RequestItem;
import com.telenav.location.TnLocation;
import com.telenav.logger.Logger;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2010-12-21
 */
public abstract class AbstractTxNodeServerProxy extends AbstractServerProxy
{
    public AbstractTxNodeServerProxy(Host host, Comm comm, IServerProxyListener serverProxyListener, IUserProfileProvider userProfileProvider)
    {
        super(host, comm, serverProxyListener, userProfileProvider);
    }
    
    protected void handleResponse(byte[] response)
    {
        Node node = new Node(response, 0);
        
        for (int i = 0; i < node.getChildrenSize(); i++)
        {
            Node child = node.getChildAt(i);
            handleNode(child);
        }
    }
    
    protected abstract int parseRequestSpecificData(Node node);

    protected abstract void addRequestArgs(Node requestNode, Node argNode, RequestItem requestItem);

    protected Node createRequestNode(RequestItem requestItem)
    {
        if (requestItem.action == null || requestItem.action.length() == 0)
        {
            Logger.log(Logger.ERROR, this.getClass().getName(), "createRequestNode() - ERROR: request action is null or empty.");
            
            throw new IllegalArgumentException("request action is null or empty.");
        }

        Node requestNode = new Node();

        Node argNode = new Node();
        argNode.addString(requestItem.action);

        try
        {
            addRequestArgs(requestNode, argNode, requestItem);
        }
        catch (Throwable e)
        {
            Logger.log(this.getClass().getName(), e);
        }

        requestNode.addChild(argNode);

        addMandatoryNode(requestNode);

        return requestNode;
    }
    
    protected void addGPSData(Node n, TnLocation[] data, int numFixes)
    {
        if (data != null && data.length > 0)
        {
            Node nData = new Node();
            n.addChild(nData);

            nData.addValue(IServerProxyConstants.TYPE_GPS_DATA);

            nData.addString("gps");

            TnLocation gpsData = data[0];
            if (gpsData != null && gpsData.isValid() && !gpsData.isEncrypt())
            {
                // need enc
                nData.addValue(1);
            }

            for (int i = 0; i < data.length && i < numFixes; i++)
            {
            	if (data[i] == null) continue;
            	
                Node node = new Node(SerializableManager.getInstance().getLocationSerializable().toBytes((data[i])), 0);
                nData.addChild(node);
            }
        }
    }
    
    protected void addMandatoryNode(Node requestNode)
    {
        MandatoryProfile profile;
        if(userProfileProvider != null)
        {
            profile = userProfileProvider.getMandatoryNode();
        }
        else
        {
            MandatoryNodeDao mandatoryNodeDao = DaoManager.getInstance().getMandatoryNodeDao();
            profile = mandatoryNodeDao.getMandatoryNode();
        }
        Node mandatoryNode = new Node(SerializableManager.getInstance().getMandatorySerializable().toBytes(profile), 0);
        
        mandatoryNode.addString(IServerProxyConstants.MANDATORY_NODE_MSG);

        requestNode.addChild(mandatoryNode);
    }
    
    private void handleNode(Node node)
    {
        try
        {
            if (!parseCommonObject(node) && status != IServerProxyConstants.FAILED
                    && this.status != IServerProxyConstants.EXCEPTION)
            {
                this.status = parseRequestSpecificData(node);
            }
        }
        catch (Exception t)
        {
            Logger.log(this.getClass().getName(), t, "handleNode() -server proxy handle Node exception.");
        }
    }

    private boolean parseCommonObject(Node node)
    {
        if (node == null)
            return false;

        int nodeType = (int) node.getValueAt(0);

        this.status = (int) node.getValueAt(1);

        // if action node failed, notify user right now
        if (nodeType == IServerProxyConstants.TYPE_SERVER_RESPONSE)
        {
            if (node.getStringsSize() > 0)
            {
                this.actionId = node.getStringAt(0);

                if (node.getStringsSize() > 1)
                    errorMessage = node.getStringAt(1);
            }

            if (this.status == IServerProxyConstants.FAILED || this.status == IServerProxyConstants.EXCEPTION)
            {
                if (this.listener != null)
                    this.listener.transactionError(this);

                return false;
            }
        }

        boolean isCommonObjects = true;

        switch (nodeType)
        {
            default:
                isCommonObjects = false;
        }

        return isCommonObjects;
    }
}
