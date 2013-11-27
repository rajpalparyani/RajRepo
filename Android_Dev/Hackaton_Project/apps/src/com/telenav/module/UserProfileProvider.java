/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * UserProfileProvider.java
 *
 */
package com.telenav.module;

import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.datatypes.mandatory.MandatoryProfile;
import com.telenav.data.serializable.SerializableManager;
import com.telenav.data.serverproxy.IUserProfileProvider;

/**
 * @author yning
 * @date 2012-3-2
 */
public class UserProfileProvider implements IUserProfileProvider
{
    MandatoryProfile mandatoryProfile = null;

    public UserProfileProvider()
    {
        MandatoryProfile mandatoryNode = DaoManager.getInstance().getMandatoryNodeDao()
                .getMandatoryNode();
        byte[] data = SerializableManager.getInstance().getMandatorySerializable()
                .toBytes(mandatoryNode);
        mandatoryProfile = SerializableManager.getInstance().getMandatorySerializable()
                .createMandatoryProfile(data);

    }

    public MandatoryProfile getMandatoryNode()
    {
        return mandatoryProfile;
    }

    public void setRegion(String region)
    {
        mandatoryProfile.getUserInfo().region = region;
    }

    public String getRegion()
    {
        return mandatoryProfile.getUserInfo().region;
    }
}
