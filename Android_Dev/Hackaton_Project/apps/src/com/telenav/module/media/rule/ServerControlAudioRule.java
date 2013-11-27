/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * SpecialRule.java
 *
 */
package com.telenav.module.media.rule;

import java.util.Hashtable;

import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.dao.serverproxy.AbstractDaoManager;
import com.telenav.datatypes.audio.AbstractRule;
import com.telenav.datatypes.audio.AudioData;
import com.telenav.datatypes.audio.AudioDataFactory;
import com.telenav.datatypes.audio.AudioDataNode;

/**
 *@author yning
 *@date 2011-8-31
 */
public class ServerControlAudioRule extends AbstractRule
{
    AudioDataNode audioDataNode;
    public static final int AUDIO_RESOURCE_ID_DISTANCE = -2;
    public ServerControlAudioRule(AudioDataNode audioDataNode)
    {
        
        super(null);
        this.audioDataNode = audioDataNode;
    }

    /**
     * this method is not used in SpecialRule
     */
    public AudioDataNode createAudioNode(Hashtable hashtable)
    {
        return null;
    }
    
    /**
     * hashtable is not used in this method
     */
    public AudioData[] createAudioData(Hashtable hashtable)
    {
        if(audioDataNode == null)
        {
            return null;
        }
        AudioDataNode checkedNode = AudioDataFactory.getInstance().createAudioDataNode(null);
        checkDistanceNode(hashtable, audioDataNode, checkedNode);
        return flattenSequence(checkedNode);
    }

    protected void checkDistanceNode(Hashtable hashtable, AudioDataNode node, AudioDataNode checkedNode)
    {
        if(node.getAudioData() != null)
        {
            AudioData audioData = node.getAudioData();
            int id = audioData.getResourceId();
            if(id == AUDIO_RESOURCE_ID_DISTANCE)
            {
                DaoManager daoManager = (DaoManager) AbstractDaoManager.getInstance();
                AudioRuleManager ruleManager = daoManager.getRuleManager();
                AbstractRule distRule = ruleManager.getAudioRule(IRuleParameter.RULE_TYPE_DISTANCE);
                AudioDataNode distNode = distRule.createAudioNode(hashtable);
                
                checkedNode.addChild(distNode);
            }
            else
            {
                AudioDataNode tmpNode = AudioDataFactory.getInstance().createAudioDataNode(audioData);
                checkedNode.addChild(tmpNode);
            }
        }
        
        if(node.getChildrenSize() > 0)
        {
            for(int i = 0; i < node.getChildrenSize(); i++)
            {
                checkDistanceNode(hashtable, node.getChild(i), checkedNode);
            }
        }
    }
}
