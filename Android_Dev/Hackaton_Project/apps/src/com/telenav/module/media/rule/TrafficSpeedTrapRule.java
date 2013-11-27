/**
 *
 * Copyright 2009 TeleNav, Inc. All rights reserved.
 * TrafficCameraSpeedRule.java
 *
 */
package com.telenav.module.media.rule;

import java.io.InputStream;
import java.util.Hashtable;

import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.dao.serverproxy.AbstractDaoManager;
import com.telenav.datatypes.audio.AbstractRule;
import com.telenav.datatypes.audio.AudioDataNode;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2009-9-2
 */
public class TrafficSpeedTrapRule extends AbstractRule
{
    public static final int DISTANCE_NODE_INDEX = 0;

    TrafficSpeedTrapRule(InputStream is)
    {
        super(is);
    }

    public AudioDataNode createAudioNode(Hashtable parameter)
    {
        AudioRuleManager ruleManager = ((DaoManager) AbstractDaoManager.getInstance()).getRuleManager();

        AbstractRule distanceRule = (AbstractRule) ruleManager.getAudioRule(IRuleParameter.RULE_TYPE_DISTANCE);

        AudioDataNode[] nodeArgs = new AudioDataNode[1];
        nodeArgs[DISTANCE_NODE_INDEX] = distanceRule.createAudioNode(parameter);

        return ruleNode.eval(null, nodeArgs);
    }
}
