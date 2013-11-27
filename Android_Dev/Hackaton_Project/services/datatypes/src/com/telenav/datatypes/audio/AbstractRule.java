package com.telenav.datatypes.audio;

import java.io.InputStream;
import java.util.Hashtable;

import com.telenav.datatypes.audio.AudioData;
import com.telenav.datatypes.audio.AudioDataFactory;
import com.telenav.datatypes.audio.RuleNode;
import com.telenav.logger.Logger;

public abstract class AbstractRule
{
    public static final int TYPE_MSG_AUDIO               = 33;
    
    final static private int NO_INDEX = -1;

    protected RuleNode ruleNode;

    protected int version;

    protected AbstractRule(InputStream is)
    {
        try
        {
            ruleNode = AudioDataFactory.getInstance().createRuleNode(is);
        }
        catch (Throwable e)
        {
            Logger.log(this.getClass().getName(), e);
        }
    }

    protected AudioData[] flattenSequence(AudioDataNode node)
    {
        int sequenceSize = getSequenceSize(node, 0);
        AudioData[] sequence = new AudioData[sequenceSize];
        return flattenSequence(node, sequence, new IntWrapper(0));
    }

    private AudioData[] flattenSequence(AudioDataNode node, AudioData[] seq, IntWrapper position)
    {
        if (node.getAudioData() != null)
        {
            seq[position.intValue] = node.getAudioData();
            position.intValue++;
        }

        for (int i = 0; i < node.getChildrenSize(); i++)
        {
            flattenSequence(node.getChild(i), seq, position);
        }

        return seq;
    }

    protected static int getSequenceSize(AudioDataNode node, int size)
    {
        if (node.getAudioData() != null)
        {
            size++;
        }

        for (int i = 0; i < node.getChildrenSize(); i++)
        {
            size = getSequenceSize(node.getChild(i), size);
        }

        return size;
    }

    public AudioData[] createAudioData(Hashtable hashtable)
    {
        AudioDataNode result = createAudioNode(hashtable);
        AudioData[] playList = flattenSequence(result);

        return playList;
    }

    public int getVersion()
    {
        return version;
    }

    public abstract AudioDataNode createAudioNode(Hashtable hashtable);

    static class IntWrapper
    {
        public IntWrapper(int i)
        {
            intValue = i;
        }

        public int intValue;
    }

}
