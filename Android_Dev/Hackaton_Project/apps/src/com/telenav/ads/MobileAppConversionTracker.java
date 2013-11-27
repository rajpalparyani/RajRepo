package com.telenav.ads;

import java.util.HashMap;

import android.content.Context;

import com.mobileapptracker.MobileAppTracker;
import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.dao.misc.SimpleConfigDao;
import com.telenav.logger.Logger;

public class MobileAppConversionTracker
{

    private static MobileAppConversionTracker trackerInstance = null;

    private static final boolean DEBUG_MODE = false;

    public MobileAppTracker mobileAppTracker = null;

    private final static String SHARED_PREFS_KEY = "mobileAppTrackingPrefs"; // Not needed yet, could remove this

    private final static String ADVERTISER_ID = "7940";

    private final static String TRACKER_KEY = "841edccade5d9760d5626f79e79cfd94";

    private final static String LOGGING_TAG = "MobileAppConversionTracker";

    public final static String ACTION_REGISTRATION = "Registration";

    public final static String ACTION_OPEN = "open";

    public final static String ACTION_PREMIUM_UPGRADE = "PremiumUpgrade";

    public static final String UPGRADE_MONTHLY_UPGRADE = "Monthly_Upgrade";

    public static final String UPGRADE_YEARLY_UPGRADE = "Yearly_Upgrade";

    public static String clientVersion = "";

    private MobileAppConversionTracker()
    {

    }

    public static MobileAppConversionTracker getInstance()
    {
        if (trackerInstance == null)
            trackerInstance = new MobileAppConversionTracker();
        return trackerInstance;
    }

    public void initMobileAppTracking(Context context)
    {
        mobileAppTracker = new MobileAppTracker(context, ADVERTISER_ID, TRACKER_KEY);

        mobileAppTracker.setSiteId("33630");// this is the QA site id.
        if (DEBUG_MODE)
        {
            mobileAppTracker.setAllowDuplicates(true);// ONLY DEBUG MODE
            mobileAppTracker.setDebugMode(true);// ONLY DEBUG MODE
        }
    }

    public void mobileAppTrackInstall(Context context, String currentVersion)
    {
        // TODO, API has not details on the return codes, updated when receive from the MobileAppTracking
        int responseCode = 0;
        // SharedPreferences settings = context.getSharedPreferences(SHARED_PREFS_KEY, 0);
        boolean isTracked = ((DaoManager) DaoManager.getInstance()).getSimpleConfigDao().getBoolean(
            SimpleConfigDao.KEY_CONVERSION_TRACKED);
        if (!isTracked)
        {

            if (currentVersion == null)
            {
                responseCode = mobileAppTracker.trackInstall();// TODO, create logic to track update.
                Logger.log(Logger.INFO, this.getClass().getName(), LOGGING_TAG + " : Track Install");
            }
            else
            {
                responseCode = mobileAppTracker.trackUpdate();
                Logger.log(Logger.INFO, this.getClass().getName(), LOGGING_TAG + " : Track Update");
            }

            if (responseCode != -1)
            {
                ((DaoManager) DaoManager.getInstance()).getSimpleConfigDao().put(SimpleConfigDao.KEY_CONVERSION_TRACKED, true);
                ((DaoManager) DaoManager.getInstance()).getSimpleConfigDao().store();
                Logger.log(Logger.INFO, this.getClass().getName(), LOGGING_TAG + " : Tracked Successfully");
            }

        }
    }

    public void trackAction(String action)
    {
        mobileAppTracker.setEventReferrer(null);// Calling package is set to null
        if (mobileAppTracker.trackAction(action) != -1)
            Logger.log(Logger.INFO, this.getClass().getName(), LOGGING_TAG + " : Tracked Successfully action= " + action);
        else
            Logger.log(Logger.INFO, this.getClass().getName(), LOGGING_TAG + " : Error tracking action = " + action);
    }

    public void trackActionDetail(String action, double revenue, String currency)
    {
        mobileAppTracker.setEventReferrer(null);// Calling package is set to null
        if (mobileAppTracker.trackAction(action, revenue, currency) != -1)
            Logger.log(Logger.INFO, this.getClass().getName(), LOGGING_TAG + " : Tracked Successfully action= " + action);
        else
            Logger.log(Logger.INFO, this.getClass().getName(), LOGGING_TAG + " : Error tracking action = " + action);
    }

}
