/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * PreferenceGroup.java
 *
 */
package com.telenav.data.datatypes.preference;

import java.util.Vector;

import com.telenav.util.PrimitiveTypeCache;

/**
 *@author Albert Ma (byma@telenav.cn)
 *@date 2010-8-11
 */
public class PreferenceGroup
{
	public static final short PREFERENCE_TYPE_GROUP = 1000;

	//--- for preference group ---
	public static final short ID_PREFERENCE_GROUP_ROOT = 0;
	public static final short ID_PREFERENCE_GROUP_GENERAL = 1;
	public static final short ID_PREFERENCE_GROUP_NAVIGATION = 2;
	public static final short ID_PREFERENCE_GROUP_NAV_AUDIO = 3;
	public static final short ID_PREFERENCE_GROUP_SPEECH_RCOG = 4;
	public static final short ID_PREFERENCE_GROUP_ADS_OPTIONS = 5;
	public static final short ID_PREFERENCE_GROUP_SHARING = 6;

	private int id;
    private String name;
    private int[] preferenceIds ;
    private Vector preferenceIdsVector;
    
    public PreferenceGroup(int id, String name)
    {
        this.id = id;
        this.name = name;
        
    } 
	public int getId()
	{
		return id;
	}

	public String getName()
	{
		return name;
	}

	public int[] getPreferenceIds()
    {
        if (preferenceIds != null)
            return preferenceIds;
        
        if(preferenceIdsVector != null)
        {
            preferenceIds = new int[preferenceIdsVector.size()];
            for(int i = 0; i < preferenceIdsVector.size(); i++)
            {
                preferenceIds[i] = ((Integer)preferenceIdsVector.elementAt(i)).intValue();
            }
        }
        
        return preferenceIds;
    }

	public void setPreferenceIds(int[] preIds)
	{
		this.preferenceIds = preIds;
	}

	public void addPreferenceId(int id)
	{
	    if(preferenceIdsVector == null)
	    {
	        preferenceIdsVector = new Vector();
	    }
	    
        if (!preferenceIdsVector.contains(PrimitiveTypeCache.valueOf(id)))
        {
            preferenceIdsVector.addElement(PrimitiveTypeCache.valueOf(id));
        }
    }
}
