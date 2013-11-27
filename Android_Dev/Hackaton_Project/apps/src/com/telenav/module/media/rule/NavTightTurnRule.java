package com.telenav.module.media.rule;

import java.io.InputStream;
import java.util.Hashtable;

import com.telenav.datatypes.audio.AbstractRule;
import com.telenav.datatypes.audio.AudioDataNode;

public class NavTightTurnRule extends AbstractRule
{
    public static final int CURRENT_NODE_INDEX = 0;

    public static final int NEXT_NODE_INDEX = 1;

    NavTightTurnRule(InputStream is)
    {
        super(is);
    }

    public AudioDataNode createAudioNode(Hashtable parameter)
    {
        // The tight turn rule takes the following parameters:
        // node[0] = current street
        // node[1] = next street
        AudioDataNode[] nodeArgs = new AudioDataNode[2];
        nodeArgs[CURRENT_NODE_INDEX] = (AudioDataNode) parameter.get(IRuleParameter.CURRENT_TURN_PROMPT);
        nodeArgs[NEXT_NODE_INDEX] = (AudioDataNode) parameter.get(IRuleParameter.NEXT_TURN_PROMPT);

//        System.out.println("Executing NavTightTurnRule");
        return ruleNode.eval(null, nodeArgs);

    }

}
