/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * IUserProfileProvider.java
 *
 */
package com.telenav.data.serverproxy;

import com.telenav.data.datatypes.mandatory.MandatoryProfile;

/**
 *@author yning
 *@date 2012-3-1
 */
public interface IUserProfileProvider
{
    public MandatoryProfile getMandatoryNode();
}
