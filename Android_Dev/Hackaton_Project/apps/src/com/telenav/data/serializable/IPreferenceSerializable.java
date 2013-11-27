/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * IPreferenceSerializable.java
 *
 */
package com.telenav.data.serializable;

import com.telenav.data.datatypes.preference.Preference;
import com.telenav.data.datatypes.preference.PreferenceGroup;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2010-12-23
 */
public interface IPreferenceSerializable
{
    public PreferenceGroup createPreferenceGroup(byte[] data);

    public byte[] toBytes(PreferenceGroup preference);
    
    public Preference createPreference(byte[] data);

    public byte[] toBytes(Preference preference);
}
