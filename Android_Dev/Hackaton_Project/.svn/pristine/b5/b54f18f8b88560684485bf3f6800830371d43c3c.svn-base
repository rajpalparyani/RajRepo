package com.telenav.module.media.rule;

import java.io.InputStream;
import java.util.Hashtable;

import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.dao.serverproxy.AbstractDaoManager;
import com.telenav.datatypes.audio.AbstractRule;
import com.telenav.datatypes.audio.AudioDataNode;

public class TrafficInfoRule extends AbstractRule
{
    public static final int DISTANCE_NODE_INDEX = 0;

    public static final int STREET_NAME_NODE_INDEX = 1;

    public static final int X_STREET_NAME_NODE_INDEX = 2;

    public static final int INCIDENT_TYPE_INT_INDEX = 0;

    public static final int LANES_CLOSED_INT_INDEX = 1;

    TrafficInfoRule(InputStream is)
    {
        super(is);
    }

    public AudioDataNode createAudioNode(Hashtable parameter)
    {
        AudioRuleManager ruleManager = ((DaoManager) AbstractDaoManager.getInstance()).getRuleManager();
        // The traffic rule takes the following parameters:
        // int[0] = number of lanes closed
        // node[0] = distance audio
        // node[1] = incident info audio, e.g. "Accident on San Tomas expr"
        // get distance rule
        AbstractRule distanceRule = (AbstractRule) ruleManager.getAudioRule(IRuleParameter.RULE_TYPE_DISTANCE);

        int[] intArgs = new int[2];
        intArgs[INCIDENT_TYPE_INT_INDEX] = ((Integer) parameter.get(IRuleParameter.INCIDENT_TYPE)).intValue();
        intArgs[LANES_CLOSED_INT_INDEX] = ((Integer) parameter.get(IRuleParameter.LANES_CLOSED)).intValue();

        AudioDataNode[] nodeArgs = new AudioDataNode[3];
        nodeArgs[DISTANCE_NODE_INDEX] = distanceRule.createAudioNode(parameter);
        nodeArgs[STREET_NAME_NODE_INDEX] = (AudioDataNode) parameter.get(IRuleParameter.STREET_NAME);
        nodeArgs[X_STREET_NAME_NODE_INDEX] = (AudioDataNode) parameter.get(IRuleParameter.X_STREET_NAME);

        return ruleNode.eval(intArgs, nodeArgs);

    }

}
