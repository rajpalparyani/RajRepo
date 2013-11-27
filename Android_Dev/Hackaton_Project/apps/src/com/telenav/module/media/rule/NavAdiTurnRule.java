package com.telenav.module.media.rule;

import java.io.InputStream;
import java.util.Hashtable;

import com.telenav.datatypes.audio.AbstractRule;
import com.telenav.datatypes.audio.AudioDataNode;

public class NavAdiTurnRule extends AbstractRule
{
    public static final int STREET_NODE_INDEX = 0;

    public static final int TURN_TYPE_INT_INDEX = 0;

    NavAdiTurnRule(InputStream is)
    {
        super(is);
    }

    public AudioDataNode createAudioNode(Hashtable parameter)
    {
        int[] intArgs = new int[1];
        intArgs[TURN_TYPE_INT_INDEX] = ((Integer) parameter.get(IRuleParameter.TURN_TYPE)).intValue();

        AudioDataNode[] nodeArgs = new AudioDataNode[1];
        nodeArgs[STREET_NODE_INDEX] = (AudioDataNode) parameter.get(IRuleParameter.STREET_NAME);

        return ruleNode.eval(intArgs, nodeArgs);
    }

}
