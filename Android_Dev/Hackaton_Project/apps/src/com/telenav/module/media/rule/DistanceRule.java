package com.telenav.module.media.rule;

import java.io.InputStream;
import java.util.Hashtable;

import com.telenav.datatypes.audio.AbstractRule;
import com.telenav.datatypes.audio.AudioDataNode;

public class DistanceRule extends AbstractRule
{
    private static final int DISTANCE_INT_INDEX = 0;

    private static final int UNITS_INT_INDEX = 1;

    private static final int MILE = 405;

    private static final int KILOMETER = 407;

    private static final int FOOT = 409;

    private static final int METER = 411;

    private static final int MAX_LONG_DETAILED = 200; // 20km, 20mi

    public static final int MAX_SHORT_DISPLAYED = 500;

    public static final int[] LONG_UNIT_AUDIO =
    { KILOMETER, MILE };

    public static final int[] SHORT_UNIT_AUDIO =
    { METER, FOOT };

    // --------------------------------------------------------------
    // conversion from deg^-5 to metric and us customary units
    // to round up to 0.1 km, add 1000/10/2 meters
    // to round up to 0.1 mi, add 5280/10/2 (feet)
    private static final int[] ROUND =
    { 50, 264 };

    // to convert 1e-5 to meters, multiply by 9119/8192
    // to convert 1e-5 to feet, multiply by 29919/8192
    private static final int[] DEG2SHORT =
    { 9119, 29919 };

    // to convert short units (meters/feet) to long units (tenths of km/miles)
    // metric - multiply by 8192 * 2 / (1000/10) divided by 8192 * 2 --- (164)
    // miles - multiply by 8192 * 2 / (5280/10) divided by 8192 * 2 --- (31)
    private static final int[] SHORT2LONG =
    { 164, 31 };

    /** SCALE */
    public static final long SCALE = 8192;

    /** corresponding SHIFT value */
    public static final int SHIFT = 13; // log base 2 of SCALE

    DistanceRule(InputStream is)
    {
        super(is);
    }

    public AudioDataNode createAudioNode(Hashtable parameter)
    {
        // The action rule takes the following parameters:
        // int[0] = distance
        // int[1] = distance unit

        // prepare the parameters
        Integer distance = (Integer) parameter.get(IRuleParameter.DISTANCE);
        Integer distUnit = (Integer) parameter.get(IRuleParameter.DIST_UNIT);

        // convert 1e-5 degrees to short units
        int sysOfUnits = distUnit.intValue();

        long dist = distance.intValue();
        dist *= DEG2SHORT[sysOfUnits];
        dist = dist >> SHIFT;

        boolean useLongUnits = false;
        if (dist < MAX_SHORT_DISPLAYED)
        {
            // don't play fractions of short units
            // round down to the nearest whole number.
            dist = dist * 10;
        }
        else
        {
            dist += ROUND[sysOfUnits];
            dist *= SHORT2LONG[sysOfUnits];
            dist = dist >> (SHIFT + 1);
            if (dist == 0)
                dist = 1;

            useLongUnits = true;

            if (dist > MAX_LONG_DETAILED)
            {
                // if the distance is greater than MAX_LONG_DETAILED
                // round down to the nearest whole number.
                dist = (dist / 10) * 10;
            }
        }

        int[] intArgs = new int[2];
        intArgs[DISTANCE_INT_INDEX] = (int) dist;

        if (useLongUnits)
            intArgs[UNITS_INT_INDEX] = LONG_UNIT_AUDIO[sysOfUnits];
        else
            intArgs[UNITS_INT_INDEX] = SHORT_UNIT_AUDIO[sysOfUnits];

        return ruleNode.eval(intArgs, null);

    }

}
