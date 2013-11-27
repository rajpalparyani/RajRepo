/**
 * @author alexg
 */
package com.telenav.module.media.rule;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;

import com.telenav.cache.AbstractCache;
import com.telenav.datatypes.DataUtil;
import com.telenav.datatypes.audio.AbstractRule;
import com.telenav.datatypes.audio.AudioDataNode;
import com.telenav.logger.Logger;
import com.telenav.persistent.TnStore;
import com.telenav.util.PrimitiveTypeCache;

/**
 * @author alexg
 * 
 */
public class AudioRuleManager
{
    private TnStore rulesStore;

    private AbstractCache rulesCache;

    private long timestamp;

    public AudioRuleManager(TnStore rulesStore, AbstractCache rulesCache)
    {
        this.rulesStore = rulesStore;
        this.rulesCache = rulesCache;

        if (Logger.DEBUG)
        {
            Logger.log(Logger.INFO, this.getClass().getName(), "RuleManager<init>: rules store size: " + rulesStore.size());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.telenav.framework.audio.rule.IRuleManager#getAudioRule(int)
     */
    public AbstractRule getAudioRule(byte type)
    {
        AbstractRule rule = null;

        // First check to see if this rule is already in the cache
        if (rulesCache.containsKey(PrimitiveTypeCache.valueOf(type)))
        {
            rule = (AbstractRule) rulesCache.get(PrimitiveTypeCache.valueOf(type));
        }
        else
        {
            byte[] buff = rulesStore.get(type);
            if (buff == null)
                return rule;

            InputStream is = null;
            try
            {
                is = new ByteArrayInputStream(buff);
                switch (type)
                {
                    case IRuleParameter.RULE_TYPE_DISTANCE:
                        rule = new DistanceRule(is);
                        break;
                    case IRuleParameter.RULE_TYPE_NAV_ACTION:
                        rule = new NavActionRule(is);
                        break;
                    case IRuleParameter.RULE_TYPE_NAV_INFO:
                        rule = new NavInfoRule(is);
                        break;
                    case IRuleParameter.RULE_TYPE_NAV_TIGHT_TURN:
                        rule = new NavTightTurnRule(is);
                        break;
                    case IRuleParameter.RULE_TYPE_TRAFFIC_INCIDENT:
                        rule = new TrafficInfoRule(is);
                        break;
                    case IRuleParameter.RULE_TYPE_NAV_ADI:
                        rule = new NavAdiRule(is);
                        break;
                    case IRuleParameter.RULE_TYPE_NAV_DESTINATION:
                        rule = new NavDestinationRule(is);
                        break;
                    case IRuleParameter.RULE_TYPE_SPEED_TRAP_WARNING_AUDIO:
                        rule = new TrafficSpeedTrapRule(is);
                        break;
                }

                if (rule != null)
                {
                    // add it to the cache
                    try
                    {
                        rulesCache.put(PrimitiveTypeCache.valueOf(type), rule);
                    }
                    catch (Throwable e)
                    {
                        Logger.log(this.getClass().getName(), e);
                    }
                }
            }
            finally
            {
                if (is != null)
                {
                    try
                    {
                        is.close();
                    }
                    catch (IOException e)
                    {
                        Logger.log(this.getClass().getName(), e);
                    }
                    finally
                    {
                        is = null;
                    }
                }
            }
        }

        return rule;
    }

    public long getTimestamp()
    {
        if (timestamp == 0)
        {
            byte[] buff = rulesStore.get(IRuleParameter.KEY_RULE_TIMESTAMP);
            if (buff != null)
            {
                timestamp = DataUtil.readLong(buff, 0);
            }
        }
        return timestamp;
    }

    public void setTimestamp(long timestamp)
    {
        this.timestamp = timestamp;
        byte[] buff = new byte[8];
        DataUtil.writeLong(buff, timestamp, 0);
        rulesStore.put(IRuleParameter.KEY_RULE_TIMESTAMP, buff);
    }

    public Hashtable createTrafficRuleParameter(int distance, int distance_unit, int incidentType, AudioDataNode streetName, AudioDataNode xStreetName, int lanesClosed)
    {
        Hashtable param = new Hashtable();
        param.put(IRuleParameter.DISTANCE, PrimitiveTypeCache.valueOf(distance));
        param.put(IRuleParameter.DIST_UNIT, PrimitiveTypeCache.valueOf(distance_unit));
        param.put(IRuleParameter.INCIDENT_TYPE, PrimitiveTypeCache.valueOf(incidentType));
        param.put(IRuleParameter.LANES_CLOSED, PrimitiveTypeCache.valueOf(lanesClosed));
        if(streetName != null)
        {
            param.put(IRuleParameter.STREET_NAME, streetName);
        }
        if(xStreetName != null)
        {
            param.put(IRuleParameter.X_STREET_NAME, xStreetName);
        }

        return param;
    }

    public Hashtable createAdiRuleParameter(AudioDataNode streetName, int headingType, int turnType)
    {
        Hashtable param = new Hashtable();
        if(streetName != null)
        {
            param.put(IRuleParameter.STREET_NAME, streetName);
        }
        param.put(IRuleParameter.DIRECTION, PrimitiveTypeCache.valueOf(headingType));
        param.put(IRuleParameter.TURN_TYPE, PrimitiveTypeCache.valueOf(turnType));

        return param;
    }

    public Hashtable createTightTurnRuleParameter(AudioDataNode currentTurn, AudioDataNode nextTurn)
    {
        Hashtable param = new Hashtable();
        param.put(IRuleParameter.CURRENT_TURN_PROMPT, currentTurn);
        param.put(IRuleParameter.NEXT_TURN_PROMPT, nextTurn);

        return param;
    }

    public Hashtable createNavRuleParameter(int distance, int distance_unit, int turnType, int exitNum, AudioDataNode streetName, boolean isTightTurn, int nextTurnType, int nextExitNum,
            AudioDataNode nextStreetName, boolean isDestination)
    {
        // we may need some extra slots in this parameter object later on
        Hashtable param = new Hashtable();
        param.put(IRuleParameter.DISTANCE, PrimitiveTypeCache.valueOf(distance));
        param.put(IRuleParameter.DIST_UNIT, PrimitiveTypeCache.valueOf(distance_unit));
        param.put(IRuleParameter.TURN_TYPE, PrimitiveTypeCache.valueOf(turnType));
        param.put(IRuleParameter.EXIT_NUM, PrimitiveTypeCache.valueOf(exitNum));
        if(streetName != null)
        {
            param.put(IRuleParameter.STREET_NAME, streetName);
        }
        param.put(IRuleParameter.IS_TIGHT_TURN, PrimitiveTypeCache.valueOf(isTightTurn));
        param.put(IRuleParameter.NEXT_TURN_TYPE, PrimitiveTypeCache.valueOf(nextTurnType));
        param.put(IRuleParameter.NEXT_EXIT_NUM, PrimitiveTypeCache.valueOf(nextExitNum));
        if(nextStreetName != null)
        {
            param.put(IRuleParameter.NEXT_STREET_NAME, nextStreetName);
        }
        param.put(IRuleParameter.IS_DESTINATION, PrimitiveTypeCache.valueOf(isDestination));

        return param;
    }
    
    public void clearCache()
    {
    	rulesCache.clear();
    }
}
