/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * AudioComposer.java
 *
 */
package com.telenav.module.media;

import java.util.Hashtable;

import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.dao.serverproxy.AbstractDaoManager;
import com.telenav.data.datatypes.preference.Preference;
import com.telenav.datatypes.audio.AbstractRule;
import com.telenav.datatypes.audio.AudioData;
import com.telenav.datatypes.audio.AudioDataNode;
import com.telenav.datatypes.route.Route;
import com.telenav.datatypes.route.Segment;
import com.telenav.module.media.rule.AudioRuleManager;
import com.telenav.module.media.rule.IRuleParameter;
import com.telenav.module.media.rule.ServerControlAudioRule;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2010-11-25
 */
public class AudioComposer
{
    private static AudioComposer instance = new AudioComposer();

    private AudioComposer()
    {

    }

    public static AudioComposer getInstance()
    {
        return instance;
    }

    public AudioData[] createAdiPrompt(AudioDataNode streetAudio, int headingType)
    {
        DaoManager daoManager = (DaoManager) AbstractDaoManager.getInstance();
        AudioRuleManager ruleManager = daoManager.getRuleManager();
        AbstractRule rule = ruleManager.getAudioRule(IRuleParameter.RULE_TYPE_NAV_ADI);
        if (rule == null)
            return null;

        Hashtable ruleParameter = ruleManager.createAdiRuleParameter(streetAudio, headingType, 0);

        return rule.createAudioData(ruleParameter);
    }

    public AudioData[] createIncidentPrompt(AudioDataNode stAudio, long distance, int laneClosed)
    {
        return this.createIncidentPrompt(distance, 1, stAudio, null, laneClosed);
    }

    public AudioData[] createIncidentPrompt(long distance, int incidentType, AudioDataNode streetName, AudioDataNode xStreetName, int lanesClosed)
    {
        DaoManager daoManager = (DaoManager) AbstractDaoManager.getInstance();
        AudioRuleManager ruleManager = daoManager.getRuleManager();

        int sysOfUnits = daoManager.getPreferenceDao().getIntValue(Preference.ID_PREFERENCE_DISTANCEUNIT);

        AbstractRule rule = ruleManager.getAudioRule(IRuleParameter.RULE_TYPE_TRAFFIC_INCIDENT);
        Hashtable ruleParameter = ruleManager.createTrafficRuleParameter((int) distance, sysOfUnits, incidentType, streetName, xStreetName, (int) lanesClosed);

        return rule.createAudioData(ruleParameter);
    }

    public AudioData[] createSpeicalPrompt(Hashtable hashtable, Segment seg, byte promptType)
    {
        AudioDataNode node = null;
        if(promptType == Route.AUDIO_TYPE_PREP)
        {
            node = seg.getPrepAudio();
        }
        else if(promptType == Route.AUDIO_TYPE_INFO)
        {
            node = seg.getInfoAudio();
        }
        else if(promptType == Route.AUDIO_TYPE_ACTION)
        {
            node = seg.getActionAudio();
        }
        
        if(node != null)
        {
            ServerControlAudioRule serverControlAudioRule = new ServerControlAudioRule(node);
            return serverControlAudioRule.createAudioData(hashtable);
        }
        return null;
    }
}
