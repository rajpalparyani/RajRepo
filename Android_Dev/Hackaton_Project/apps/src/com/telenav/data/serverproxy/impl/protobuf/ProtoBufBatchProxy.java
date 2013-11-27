/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * ProtoBufBatchProxy.java
 *
 */
package com.telenav.data.serverproxy.impl.protobuf;

import java.io.IOException;
import java.util.Vector;

import com.telenav.comm.Comm;
import com.telenav.comm.Host;
import com.telenav.data.serverproxy.IServerProxyConstants;
import com.telenav.data.serverproxy.IServerProxyListener;
import com.telenav.data.serverproxy.IUserProfileProvider;
import com.telenav.data.serverproxy.RequestItem;
import com.telenav.data.serverproxy.impl.IBatchProxy;
import com.telenav.data.serverproxy.impl.protobuf.log.ProtoBufConverter;
import com.telenav.j2me.datatypes.ProtocolBuffer;
import com.telenav.j2me.framework.util.ProtocolBufferUtil;
import com.telenav.logger.Logger;

/**
 *@author Casper (pwang@telenav.cn)
 *@date 2011-3-7
 */
public class ProtoBufBatchProxy extends AbstractProtobufServerProxy implements IBatchProxy
{

    public ProtoBufBatchProxy(Host host, Comm comm, IServerProxyListener serverProxyListener, IUserProfileProvider userProfileProvider)
    {
        super(host, comm, serverProxyListener, userProfileProvider);
    }

    private Vector groupRequest;
    

    public void addBatchItem(RequestItem requestItem)
    {
        if(requestItem == null || requestItem.action == null || requestItem.action.length() == 0)
            return;
        
        if(groupRequest == null)
        {
            groupRequest = new Vector();
        }
        
        boolean needCreateNewBatch = true;
        String requestItemUrl = comm.getHostProvider().getUrl(this.getHostByAction(requestItem.action));
        for (int i = 0; i < groupRequest.size(); i++)
        {
            Vector batchRequest = (Vector) groupRequest.elementAt(i);
            RequestItem batchItem = (RequestItem) batchRequest.elementAt(0);
            String batchUrl = comm.getHostProvider().getUrl(this.getHostByAction(batchItem.action));
            if(requestItemUrl != null && requestItemUrl.equals(batchUrl))
            {
                needCreateNewBatch = false;
                batchRequest.addElement(requestItem);
                break;
            }
        }
        
        if(needCreateNewBatch)
        {
            Vector batchRequest = new Vector();
            batchRequest.addElement(requestItem);
            groupRequest.addElement(batchRequest);
        }
    }
    

    public Vector getBatchItems()
    {
        return groupRequest;
    }

    public void send(int timeout)
    {
        if(groupRequest == null)
            return;
        
        for(int i = 0; i < groupRequest.size(); i++)
        {
            Vector requestVector = new Vector();

            Vector batchRequest = (Vector) groupRequest.elementAt(i);

            //create a new host based on action
            String action = null;

            for (int j = 0; j < batchRequest.size(); j++)
            {
                RequestItem batchItem = (RequestItem) batchRequest.elementAt(j);
                Vector rv = ((AbstractProtobufServerProxy)batchItem.serverProxy).createProtoBufReq(batchItem);;
                for(int m = 0; m < rv.size(); m++)
                {
                    ProtocolBuffer pb = (ProtocolBuffer) (rv.elementAt(m));
                    if(IServerProxyConstants.MANDATORY_NODE_MSG.equals(pb.getObjType()))
                    {
                        continue;
                    }
                    requestVector.addElement(pb);
                }
                
                action = batchItem.action;
            }

            try
            {
                addMandatoryNode(requestVector);
            }
            catch (Throwable e)
            {
                Logger.log(this.getClass().getName(), e);
            }
            ProtoBufConverter.convertToString(requestVector, true);
            byte[] request = ProtocolBufferUtil.listToByteArray(requestVector);

            this.actionId = "batch";
            this.errorMessage = null;

            if(timeout <= 0)
                timeout = 45000;
            
            this.sendData(request, action, (byte)1, timeout);
        }
    }
    
    protected int parseRequestSpecificData(ProtocolBuffer protoBuf) throws IOException
    {
        String actionType = protoBuf.getObjType();

        for (int i = 0; i < groupRequest.size(); i++)
        {
            Vector batchRequest = (Vector) groupRequest.elementAt(i);
            for (int j = 0; j < batchRequest.size(); j++)
            {
                RequestItem batchItem = (RequestItem) batchRequest.elementAt(j);
                if (batchItem.action.equals(actionType))
                {
                    ((AbstractProtobufServerProxy) batchItem.serverProxy).parseRequestSpecificData(protoBuf);

                    return this.status;
                }
            }
        }
        return this.status;

    }
    
}
