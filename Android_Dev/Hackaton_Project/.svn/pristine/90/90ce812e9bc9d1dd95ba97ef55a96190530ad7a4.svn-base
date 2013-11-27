package com.telenav.comm;

import com.telenav.comm.ICommCallback.CommResponse;

/**
 *  The interface is used to replay recorded network response in client regression test framework 
 *   
 * @author Sean Xia
 */

public interface IRequestJobHandler 
{
    public boolean execute(String requestId, Host host, 
                           CommResponse reponse, ICommStreamHandler streamHandler, 
                           int retryTimes, long timeout, 
                           ICommCallback commCallBack, Comm comm);
}
