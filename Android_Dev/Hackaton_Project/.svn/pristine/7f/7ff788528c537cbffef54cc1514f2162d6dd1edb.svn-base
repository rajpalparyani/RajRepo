/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * TxNodePreferenceSerializable.java
 *
 */
package com.telenav.data.serializable.txnode;

import com.telenav.data.datatypes.preference.Preference;
import com.telenav.data.datatypes.preference.PreferenceGroup;
import com.telenav.data.serializable.IPreferenceSerializable;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2010-12-23
 */
class TxNodePreferenceSerializable implements IPreferenceSerializable
{

    public Preference createPreference(byte[] data)
    {
        if (data == null)
            return null;
        
        Node node = new Node(data, 0);
        
        Preference preference = new Preference((int)node.getValueAt(0), node.getStringAt(0));
        preference.setIntValue((int)node.getValueAt(1));
        preference.setStrValue(node.getStringAt(1));
        
        int[] optionValues = new int[node.getValuesSize() - 2];
        String[] optionNames = new String[optionValues.length];
        for(int i = 2; i < node.getValuesSize(); i++)
        {
            optionValues[i - 2] = (int)node.getValueAt(i);
            optionNames[i - 2] = node.getStringAt(i);
        }
        preference.setOptionValues(optionValues);
        preference.setOptionNames(optionNames);
        
        return preference;
    }

    public PreferenceGroup createPreferenceGroup(byte[] data)
    {
        if(data == null)
            return null;
        
        Node node = new Node(data, 0);
        
        PreferenceGroup group = new PreferenceGroup((int)node.getValueAt(0), node.getStringAt(0));
        int[] preferIds = new int[node.getValuesSize() - 1];
        for(int i = 1; i < node.getValuesSize(); i++)
        {
            preferIds[i - 1] = (int)node.getValueAt(i);
        }
        group.setPreferenceIds(preferIds);
        
        return group;
    }

    public byte[] toBytes(PreferenceGroup group)
    {
        if(group == null)
            return null;
        
        Node node = new Node();
        node.addValue(group.getId());
        node.addString(group.getName());
        
        if(group.getPreferenceIds() != null)
        {
            for(int i = 0; i < group.getPreferenceIds().length; i++)
            {
                node.addValue(group.getPreferenceIds()[i]);
            }
        }
        
        return node.toBinary();
    }

    public byte[] toBytes(Preference preference)
    {
        if(preference == null)
            return null;
        
        Node node = new Node();
        node.addValue(preference.getId());
        node.addValue(preference.getIntValue());
        node.addString(preference.getName());
        node.addString(preference.getStrValue());
        
        if (preference.getOptionValues() != null)
        {
            for(int i = 0; i < preference.getOptionValues().length; i++)
            {
                node.addValue(preference.getOptionValues()[i]);
                node.addString(preference.getOptionNames()[i]);
            }
        }
        
        return node.toBinary();
    }

}
