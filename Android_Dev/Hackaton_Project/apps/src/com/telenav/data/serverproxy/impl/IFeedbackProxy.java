/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * IFeedbackProxy.java
 *
 */
package com.telenav.data.serverproxy.impl;

import com.telenav.location.TnLocation;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2010-12-21
 */
public interface IFeedbackProxy
{
//    public void sendFeedback(int feedbackType, String screenTitle, String userComment, long timeStamp, Node navigationNode, TnLocation[] gpsFixes, int fixes);

    public String sendFeedback(int feedbackType, String screenTitle, String userComment, long timeStamp, TnLocation[] gpsFixes, int fixes);
}
