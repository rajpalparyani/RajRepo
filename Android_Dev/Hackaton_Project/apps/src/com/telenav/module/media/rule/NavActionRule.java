package com.telenav.module.media.rule;

import java.io.InputStream;
import java.util.Hashtable;

import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.dao.serverproxy.AbstractDaoManager;
import com.telenav.datatypes.audio.AbstractRule;
import com.telenav.datatypes.audio.AudioDataNode;

public class NavActionRule extends AbstractRule
{
    // turn prompt indexes
    private static final int TURN_TYPE_INT_INDEX = 0;

    private static final int EXIT_NUM_INT_INDEX = 1;

    private static final int STREET_NAME_NODE_INDEX = 0;

    NavActionRule(InputStream is)
    {
        super(is);
    }

    private AudioDataNode createTurnAudioNode(int turnType, int exitNum, AudioDataNode streetName)
    {
        // The action rule takes the following parameters:
        // int[0] = turn type
        // int[1] = round about exit number
        // node[0] = street audio
        // get distance rule
        int[] intArgs = new int[2];
        intArgs[TURN_TYPE_INT_INDEX] = turnType;
        intArgs[EXIT_NUM_INT_INDEX] = exitNum;

        AudioDataNode[] nodeArgs = new AudioDataNode[1];
        nodeArgs[STREET_NAME_NODE_INDEX] = streetName;

//        System.out.println("Executing NavActionRule");
        return ruleNode.eval(intArgs, nodeArgs);
    }

    public AudioDataNode createAudioNode(Hashtable parameter)
    {
        AudioRuleManager ruleManager = ((DaoManager) AbstractDaoManager.getInstance()).getRuleManager();

        // prepare the parameters
        Integer turnType = (Integer) parameter.get(IRuleParameter.TURN_TYPE);
        Integer exitNum = (Integer) parameter.get(IRuleParameter.EXIT_NUM);
        AudioDataNode streetName = (AudioDataNode) parameter.get(IRuleParameter.STREET_NAME);
        AudioDataNode currentTurn = createTurnAudioNode(turnType.intValue(), exitNum.intValue(), streetName);

        // check to see if this is a tight turn
        boolean isTightTurn = ((Boolean) parameter.get(IRuleParameter.IS_TIGHT_TURN)).booleanValue();

        if (!isTightTurn)
            return currentTurn; // there is no tight turn, so return only the current turn
        else
        {
            // if this is a tight turn, include the next turn also
            // prepare the parameters
            Integer nextTurnType = (Integer) parameter.get(IRuleParameter.NEXT_TURN_TYPE);
            Integer nextExitNum = (Integer) parameter.get(IRuleParameter.NEXT_EXIT_NUM);
            AudioDataNode nextStreetName = (AudioDataNode) parameter.get(IRuleParameter.NEXT_STREET_NAME);
            AudioDataNode nextTurn = createTurnAudioNode(nextTurnType.intValue(), nextExitNum.intValue(), nextStreetName);

            // get the tight turn rule
            AbstractRule tightTurnRule = (AbstractRule) ruleManager.getAudioRule(IRuleParameter.RULE_TYPE_NAV_TIGHT_TURN);

            Hashtable param = new Hashtable();
            param.put(IRuleParameter.CURRENT_TURN_PROMPT, currentTurn);
            param.put(IRuleParameter.NEXT_TURN_PROMPT, nextTurn);

            parameter = param;

            return tightTurnRule.createAudioNode(parameter);
        }

    }

}
