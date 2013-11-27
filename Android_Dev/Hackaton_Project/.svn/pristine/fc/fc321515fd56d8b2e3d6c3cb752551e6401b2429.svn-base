/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * ApacheServerSender.java
 *
 */
package com.telenav.module.sync.apache;

import com.telenav.app.CommManager;
import com.telenav.comm.Comm;
import com.telenav.comm.Host;
import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.datatypes.mandatory.MandatoryProfile;
import com.telenav.data.datatypes.mandatory.MandatoryProfile.ClientInfo;
import com.telenav.data.serverproxy.IServerProxyConstants;
import com.telenav.logger.Logger;
import com.telenav.util.Queue;

/**
 * @author wzhu (wzhu@telenav.cn)
 * @date 2011-1-16
 */
class ApacheCommSender implements Runnable 
{
    private boolean isCancelled;
    private Queue requestQueue;
    private int count;
    private long lastJobExecTime;
    
    public ApacheCommSender()
    {
        requestQueue = new Queue();
    }
    
    public void addRequest(I18nFile file)
    {
        count++;
        requestQueue.push(file);
    }
    
    public int getCount()
    {
        return this.count;
    }
    
    public void cancel()
    {
        requestQueue.reset();
        
        isCancelled = true;
    }

    public void run()
    {
        while(!isCancelled)
        {
            if(requestQueue.isEmpty() && lastJobExecTime == -1)
            {
                lastJobExecTime = System.currentTimeMillis();
            }
            else
            {
                lastJobExecTime = -1;
            }
            
            I18nFile file = (I18nFile) requestQueue.pop(500);
            sendSyncResRequest(file);
            
            if(requestQueue.isEmpty() && lastJobExecTime != -1 && System.currentTimeMillis() - lastJobExecTime > 45000)
            {
                Logger.log(Logger.INFO, this.getClass().getName(), "----18n---some thing is wrong: run()");
                
                I18nRequestManager.getInstance().finish(Ii18nRequestListener.FAILED);
            }
        }
    }
    
    private void sendSyncResRequest(I18nFile file)
    {
        if(file == null)
        {
            return;
        }
        
        Comm comm = CommManager.getInstance().getApacheComm();
        MandatoryProfile profile = DaoManager.getInstance().getMandatoryNodeDao().getMandatoryNode();
        ClientInfo clientInfo = profile.getClientInfo();
        Host host = CommManager.getInstance().getComm().getHostProvider().createHost(IServerProxyConstants.ACT_SYNC_APACHE);
        host.file += createFilePath(file, clientInfo);
        
//        host.protocol = Host.HTTP;
//        host.host = "172.16.10.25";
//        host.port = 8080;
//        host.file = "/ota/" + file.getName();
        Logger.log(Logger.INFO, this.getClass().getName(), "----i18n---sendSyncResRequest: " + file.getName());
        
        comm.sendData("", host, new ApacheCommStreamHandler(file.getName()), 2, 45000, new ApacheCommCallback(file.getName()));
    }

	private String createFilePath(I18nFile file, ClientInfo clientInfo) 
	{
		StringBuilder builder = new StringBuilder("/");
		builder.append(clientInfo.version);
		builder.append("/");
		builder.append(clientInfo.platform);
		builder.append("/");
		builder.append(clientInfo.programCode);
		builder.append("/");
		builder.append(file.getName());
		return builder.toString();
	}
}
