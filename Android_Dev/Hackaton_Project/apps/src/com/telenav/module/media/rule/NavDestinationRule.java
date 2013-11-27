/**
 *
 * Copyright 2009 TeleNav, Inc. All rights reserved.
 * NavDestinationRule.java
 *
 */
package com.telenav.module.media.rule;

import java.io.InputStream;
import java.util.Hashtable;

import com.telenav.datatypes.audio.AbstractRule;
import com.telenav.datatypes.audio.AudioDataNode;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2009-8-7
 */
public class NavDestinationRule extends AbstractRule
{
    public static final int TURN_TYPE_INT_INDEX = 0;

    public static final int STREET_NAME_NODE_INDEX = 0;

    NavDestinationRule(InputStream is)
    {
        super(is);
    }

    public AudioDataNode createAudioNode(Hashtable parameter)
    {
        Integer turnType = (Integer) parameter.get(IRuleParameter.TURN_TYPE);
        AudioDataNode streetName = (AudioDataNode) parameter.get(IRuleParameter.STREET_NAME);

        int[] intArgs = new int[1];
        intArgs[TURN_TYPE_INT_INDEX] = turnType.intValue();

        AudioDataNode[] nodeArgs = new AudioDataNode[1];
        nodeArgs[STREET_NAME_NODE_INDEX] = streetName;

        return ruleNode.eval(intArgs, nodeArgs);
    }
}
