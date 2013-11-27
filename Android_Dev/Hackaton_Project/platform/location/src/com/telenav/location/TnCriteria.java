/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * TnCriteria.java
 *
 */
package com.telenav.location;

/**
 * A class indicating the application criteria for selecting a location provider. Providers maybe ordered according to
 * accuracy, power usage, ability to report altitude, speed, and bearing, and monetary cost.
 * 
 *@author fqming (fqming@telenav.cn)
 *@date 2010-7-1
 */
public class TnCriteria
{
    /**
     * A constant indicating that the application does not choose to place requirement on a particular feature.
     */
    public static final int NO_REQUIREMENT = 0;

    /**
     * A constant indicating a low power requirement.
     */
    public static final int POWER_LOW = 1;

    /**
     * A constant indicating a medium power requirement.
     */
    public static final int POWER_MEDIUM = 2;

    /**
     * A constant indicating a high power requirement.
     */
    public static final int POWER_HIGH = 3;

    /**
     * A constant indicating a finer location accuracy requirement
     */
    public static final int ACCURACY_FINE = 1;

    /**
     * A constant indicating an approximate accuracy requirement
     */
    public static final int ACCURACY_COARSE = 2;

    private int mAccuracy = NO_REQUIREMENT;

    private int mPowerRequirement = NO_REQUIREMENT;

    private boolean mAltitudeRequired = false;

    private boolean mBearingRequired = false;

    private boolean mSpeedRequired = false;

    private boolean mCostAllowed = false;

    private int horizontalAccuracy = NO_REQUIREMENT;

    private int verticalAccuracy = NO_REQUIREMENT;

    /**
     * Constructs a new Criteria object. The new object will have no requirements on accuracy, power, or response time;
     * will not require altitude, speed, or bearing; and will not allow monetary cost.
     */
    public TnCriteria()
    {
    }

    /**
     * Indicates the desired accuracy for latitude and longitude. Accuracy may be {@link #ACCURACY_FINE} if desired
     * location is fine, else it can be {@link #ACCURACY_COARSE}. More accurate location usually consumes more power and
     * may take longer. <br />
     * <b>This is for android platform.</b>
     * 
     * @throws IllegalArgumentException if accuracy is negative
     */
    public void setAccuracy(int accuracy)
    {
        if (accuracy < NO_REQUIREMENT || accuracy > ACCURACY_COARSE)
        {
            throw new IllegalArgumentException("accuracy=" + accuracy);
        }
        mAccuracy = accuracy;
    }

    /**
     * Returns a constant indicating desired accuracy of location Accuracy may be {@link #ACCURACY_FINE} if desired
     * location is fine, else it can be {@link #ACCURACY_COARSE}. <br />
     * <b>This is for android platform.</b>
     * 
     */
    public int getAccuracy()
    {
        return mAccuracy;
    }

    /**
     * Sets the desired horizontal accuracy preference. Accuracy is measured in meters. The preference indicates maximum
     * allowed typical 1-sigma standard deviation for the location method. Default is NO_REQUIREMENT, meaning no
     * preference on horizontal accuracy. * <br />
     * <b>This is for RIM/j2me platform.</b>
     * 
     * @param accuracy - the preferred horizontal accuracy in meters
     */
    public void setHorizontalAccuracy(int accuracy)
    {
        this.horizontalAccuracy = accuracy;
    }

    /**
     * Returns the horizontal accuracy value set in this Criteria.
     * 
     * @return int
     */
    public int getHorizontalAccuracy()
    {
        return this.horizontalAccuracy;
    }

    /**
     * Sets the desired vertical accuracy preference. Accuracy is measured in meters. The preference indicates maximum
     * allowed typical 1-sigma standard deviation for the location method. Default is NO_REQUIREMENT, meaning no
     * preference on vertical accuracy. <br />
     * <b>This is for RIM/j2me platform.</b>
     * 
     * @param accuracy - the preferred vertical accuracy in meters
     */
    public void setVerticalAccuracy(int accuracy)
    {
        this.verticalAccuracy = accuracy;
    }

    /**
     * Returns the vertical accuracy value set in this Criteria.
     * 
     * @return int
     */
    public int getVerticalAccuracy()
    {
        return this.verticalAccuracy;
    }

    /**
     * Indicates the desired maximum power level. The level parameter must be one of NO_REQUIREMENT, POWER_LOW,
     * POWER_MEDIUM, or POWER_HIGH.
     */
    public void setPowerRequirement(int level)
    {
        if (level < NO_REQUIREMENT || level > POWER_HIGH)
        {
            throw new IllegalArgumentException("level=" + level);
        }
        mPowerRequirement = level;
    }

    /**
     * Returns a constant indicating the desired power requirement. The returned
     */
    public int getPowerRequirement()
    {
        return mPowerRequirement;
    }

    /**
     * Indicates whether the provider is allowed to incur monetary cost.
     */
    public void setCostAllowed(boolean costAllowed)
    {
        mCostAllowed = costAllowed;
    }

    /**
     * Returns whether the provider is allowed to incur monetary cost.
     */
    public boolean isCostAllowed()
    {
        return mCostAllowed;
    }

    /**
     * Indicates whether the provider must provide altitude information. Not all fixes are guaranteed to contain such
     * information.
     */
    public void setAltitudeRequired(boolean altitudeRequired)
    {
        mAltitudeRequired = altitudeRequired;
    }

    /**
     * Returns whether the provider must provide altitude information. Not all fixes are guaranteed to contain such
     * information.
     */
    public boolean isAltitudeRequired()
    {
        return mAltitudeRequired;
    }

    /**
     * Indicates whether the provider must provide speed information. Not all fixes are guaranteed to contain such
     * information.
     */
    public void setSpeedRequired(boolean speedRequired)
    {
        mSpeedRequired = speedRequired;
    }

    /**
     * Returns whether the provider must provide speed information. Not all fixes are guaranteed to contain such
     * information.
     */
    public boolean isSpeedRequired()
    {
        return mSpeedRequired;
    }

    /**
     * Indicates whether the provider must provide bearing information. Not all fixes are guaranteed to contain such
     * information.
     * <br />
     * <b>This is for android platform.</b>
     */
    public void setBearingRequired(boolean bearingRequired)
    {
        mBearingRequired = bearingRequired;
    }

    /**
     * Returns whether the provider must provide bearing information. Not all fixes are guaranteed to contain such
     * information.
     * <br />
     * <b>This is for android platform.</b>
     */
    public boolean isBearingRequired()
    {
        return mBearingRequired;
    }
}
