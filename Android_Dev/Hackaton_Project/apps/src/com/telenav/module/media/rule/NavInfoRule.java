package com.telenav.module.media.rule;

import java.io.InputStream;
import java.util.Hashtable;

import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.dao.serverproxy.AbstractDaoManager;
import com.telenav.datatypes.audio.AbstractRule;
import com.telenav.datatypes.audio.AudioDataNode;

public class NavInfoRule extends AbstractRule
{
    public static final int DISTANCE_NODE_INDEX = 0;

    public static final int TURN_NODE_INDEX = 1;

    NavInfoRule(InputStream is)
    {
        super(is);
    }

    public AudioDataNode createAudioNode(Hashtable parameter)
    {
        AudioRuleManager ruleManager = ((DaoManager) AbstractDaoManager.getInstance()).getRuleManager();

        // The nav info rule takes the following parameters:
        // node[0] = distance audio
        // node[1] = turn audio

        // get distance rule
        AbstractRule distanceRule = (AbstractRule) ruleManager.getAudioRule(IRuleParameter.RULE_TYPE_DISTANCE);

        AbstractRule actionRule = null;

        boolean isDestination = ((Boolean) parameter.get(IRuleParameter.IS_DESTINATION)).booleanValue();
        if (isDestination)
        {
            actionRule = (AbstractRule) ruleManager.getAudioRule(IRuleParameter.RULE_TYPE_NAV_DESTINATION);
        }
        else
        {
            // get action rule
            actionRule = (AbstractRule) ruleManager.getAudioRule(IRuleParameter.RULE_TYPE_NAV_ACTION);
        }

        AudioDataNode[] nodeArgs = new AudioDataNode[2];
        nodeArgs[DISTANCE_NODE_INDEX] = distanceRule.createAudioNode(parameter);
        nodeArgs[TURN_NODE_INDEX] = actionRule.createAudioNode(parameter);

        return ruleNode.eval(null, nodeArgs);

    }

}
