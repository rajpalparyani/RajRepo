/**
 *
 * Copyright 2013 TeleNav, Inc. All rights reserved.
 * FriendlyUserRatingUtil.java
 *
 */
package com.telenav.module;

import java.util.Vector;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.telenav.app.android.AndroidPersistentContext;
import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.dao.misc.SimpleConfigDao;
import com.telenav.i18n.ResourceBundle;
import com.telenav.mvc.ICommonConstants;
import com.telenav.res.IStringCommon;
import com.telenav.res.ResourceManager;
import com.telenav.res.converter.StringConverter;
import com.telenav.tnui.core.AbstractTnContainer;
import com.telenav.tnui.core.TnMenu;
import com.telenav.tnui.core.TnUiArgs;
import com.telenav.tnui.core.TnUiArgs.TnUiArgAdapter;
import com.telenav.tnui.graphics.AbstractTnGraphics;
import com.telenav.tnui.widget.TnPopupContainer;
import com.telenav.ui.UiFactory;
import com.telenav.ui.UiStyleManager;
import com.telenav.ui.citizen.CitizenMessageBox;
import com.telenav.ui.frogui.widget.FrogLabel;
import com.telenav.ui.frogui.widget.FrogMessageBox;

/**
 *@author hbjia
 *@date 2013-1-8
 */
public class FriendlyUserRatingUtil implements ICommonConstants
{
    private static final int CHECKING_NUMBERS = 3;

    private static final long CHECKING_DAYS = 7 * 24 * 60 * 60 * 1000; // 7 days

    private static final long GAP_TIME = 60 * 60 * 1000; // 1 hours
    
    public static boolean handleFriendlyUser()
    {
        long currentTime = System.currentTimeMillis();
        boolean isPreviousRated = ((DaoManager)DaoManager.getInstance()).getSimpleConfigDao().getBoolean(SimpleConfigDao.KEY_IS_PREVIOUS_RATED);
        boolean isCurrentRated = ((DaoManager)DaoManager.getInstance()).getSimpleConfigDao().getBoolean(SimpleConfigDao.KEY_IS_CURRENT_RATED);
        boolean isLoveAppInFeedback = ((DaoManager)DaoManager.getInstance()).getSimpleConfigDao().getBoolean(SimpleConfigDao.KEY_IS_LOVE_APP_IN_FEEDBACK);
        int crashTimes = DaoManager.getInstance().getSimpleConfigDao().get(SimpleConfigDao.KEY_CRASH_TIMES);
        if(!isPreviousRated && !isCurrentRated)
        {
            if(isLoveAppInFeedback || (crashTimes <= 0 && threeTimesLaunchScoutCheck(currentTime) && gpsAndNetworkCheck(currentTime)))
            {
                return true;
            }
        }
        return false;
    }
    
    public static void recordHittedCurrentTime()
    {
        long currentTime = System.currentTimeMillis();
        Vector<String> timeVector = ((DaoManager) DaoManager.getInstance()).getSimpleConfigDao().getVector(SimpleConfigDao.KEY_TIME_VECTOR);
        if (timeVector.size() == 0)
        {
            timeVector.add(String.valueOf(currentTime));
        }
        else
        {
            Long latestTime = Long.parseLong(timeVector.elementAt(timeVector.size() - 1));
            if (currentTime - latestTime <= GAP_TIME)
            {
                return;
            }
            else
            {
                if (timeVector.size() < CHECKING_NUMBERS)
                {
                     timeVector.add(String.valueOf(currentTime));
                }
                else
                {
                    for (int i = 0; i < CHECKING_NUMBERS - 1; i++)
                    {
                        timeVector.setElementAt(timeVector.elementAt(i + 1), i);
                    }
                    timeVector.setElementAt(String.valueOf(currentTime), CHECKING_NUMBERS - 1);
                }
            }
        }
        ((DaoManager) DaoManager.getInstance()).getSimpleConfigDao().putVector(SimpleConfigDao.KEY_TIME_VECTOR, timeVector);
        ((DaoManager) DaoManager.getInstance()).getSimpleConfigDao().store();
    }
    
    private static boolean threeTimesLaunchScoutCheck(long currentTime)
    {
        int hitNumbers = 0;
        Vector<String> timeVector = ((DaoManager) DaoManager.getInstance()).getSimpleConfigDao().getVector(SimpleConfigDao.KEY_TIME_VECTOR);
        for (int i = 0; i < timeVector.size(); i++ )
        {
            long time =  Long.parseLong(timeVector.elementAt(i));
            if (currentTime - time <= CHECKING_DAYS)
            {
                hitNumbers ++;
            }
        }
        return (hitNumbers == CHECKING_NUMBERS);
    }
    
    public static TnPopupContainer createFriendlyRating()
    {
        ResourceBundle bundle = ResourceManager.getInstance().getCurrentBundle();
        StringConverter converter = ResourceManager.getInstance().getStringConverter();
        String appName = AppConfigHelper.appName;
        String message = bundle.getString(IStringCommon.RES_RATE_MESSAGE, IStringCommon.FAMILY_COMMON);
        message = converter.convert(message, new String[]{appName});
        String rateApp = bundle.getString(IStringCommon.RES_BTTN_RATE_APP, IStringCommon.FAMILY_COMMON);
        String remindMe = bundle.getString(IStringCommon.RES_BTTN_REMIDER, IStringCommon.FAMILY_COMMON);
        String noThanks = bundle.getString(IStringCommon.RES_BTTN_NO_THANKS, IStringCommon.FAMILY_COMMON);
        String rateTitle = bundle.getString(IStringCommon.RES_RATE_TITLE, IStringCommon.FAMILY_COMMON);
        rateTitle = converter.convert(rateTitle, new String[]{appName});
        TnMenu menu = UiFactory.getInstance().createMenu();
        message = (message == null ? "" : message);
        rateApp = (rateApp == null ? "" : rateApp);
        remindMe = (remindMe == null ? "" : remindMe);
        noThanks = (noThanks == null ? "" : noThanks);
        rateTitle = (rateTitle == null ? "" : rateTitle);
        
        menu.add(rateApp, CMD_RATE_APP);
        menu.add(remindMe, CMD_REMIND_ME_LATER);
        menu.add(noThanks, CMD_NO_THANKS);

        FrogMessageBox popup = UiFactory.getInstance().createMessageBox(STATE_SHOW_FRIEND_USER_POPUP, message, menu);
        TnUiArgAdapter widthAdapter = null;
        AbstractTnContainer content = popup.getContent();
        ((CitizenMessageBox)popup).setContentFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_DETOUR_CONFIRM_CONTENT));
        if(content != null)
        {
            widthAdapter = content.getTnUiArgs().get(TnUiArgs.KEY_PREFER_WIDTH);
        }
        
        FrogLabel titleLabel = UiFactory.getInstance().createLabel(0, rateTitle);
        titleLabel.setForegroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_DA_GR), UiStyleManager.getInstance()
                .getColor(UiStyleManager.TEXT_COLOR_DA_GR));
        titleLabel.setBoldFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_POPUP_TITLE));
        titleLabel.setStyle(AbstractTnGraphics.HCENTER);
        if(widthAdapter != null)
        {
            titleLabel.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, widthAdapter);
        }
        
        popup.setTitle(titleLabel);
        return popup;
    }
    
    private static boolean gpsAndNetworkCheck(long currTime)
    {
        String lastLostGPSStamp = ((DaoManager)DaoManager.getInstance()).getSimpleConfigDao().getString(SimpleConfigDao.KEY_LOST_GPS_STAMP);
        String lastTransErrorStamp = ((DaoManager)DaoManager.getInstance()).getSimpleConfigDao().getString(SimpleConfigDao.KEY_TANS_ERROR_STAMP);
        String lastNetworkErrorStamp = ((DaoManager)DaoManager.getInstance()).getSimpleConfigDao().getString(SimpleConfigDao.KEY_NET_ERROR_STAMP);
        String lastNavExitAbnormalStamp = ((DaoManager)DaoManager.getInstance()).getSimpleConfigDao().getString(SimpleConfigDao.KEY_NAV_EXIT_ABNORMAL_STAMP);
        if((lastLostGPSStamp != null && currTime - Long.parseLong(lastLostGPSStamp) <= CHECKING_DAYS)
                || (lastTransErrorStamp != null && currTime - Long.parseLong(lastTransErrorStamp) <= CHECKING_DAYS)
                || (lastNetworkErrorStamp != null && currTime - Long.parseLong(lastNetworkErrorStamp) <= CHECKING_DAYS)
                || (lastNavExitAbnormalStamp != null && currTime - Long.parseLong(lastNavExitAbnormalStamp) <= CHECKING_DAYS))
        {
            return false;
        }
        return true;
    }
    
    public static void doRateApp()
    {
        ((DaoManager)DaoManager.getInstance()).getSimpleConfigDao().put(SimpleConfigDao.KEY_IS_CURRENT_RATED, true);
        ((DaoManager)DaoManager.getInstance()).getSimpleConfigDao().put(SimpleConfigDao.KEY_IS_PREVIOUS_RATED, true);
        ((DaoManager)DaoManager.getInstance()).getSimpleConfigDao().remove(SimpleConfigDao.KEY_IS_LOVE_APP_IN_FEEDBACK);
        ((DaoManager)DaoManager.getInstance()).getSimpleConfigDao().store();
        Context context = AndroidPersistentContext.getInstance().getContext();
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+context.getPackageName()));
        browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(browserIntent);
    }
    
    public static void doRemindLater()
    {
        ((DaoManager)DaoManager.getInstance()).getSimpleConfigDao().remove(SimpleConfigDao.KEY_IS_CURRENT_RATED);
        ((DaoManager)DaoManager.getInstance()).getSimpleConfigDao().remove(SimpleConfigDao.KEY_TIME_VECTOR);
        ((DaoManager)DaoManager.getInstance()).getSimpleConfigDao().remove(SimpleConfigDao.KEY_LOST_GPS_STAMP);
        ((DaoManager)DaoManager.getInstance()).getSimpleConfigDao().remove(SimpleConfigDao.KEY_TANS_ERROR_STAMP);
        ((DaoManager)DaoManager.getInstance()).getSimpleConfigDao().remove(SimpleConfigDao.KEY_NET_ERROR_STAMP);
        ((DaoManager)DaoManager.getInstance()).getSimpleConfigDao().remove(SimpleConfigDao.KEY_IS_LOVE_APP_IN_FEEDBACK);
        ((DaoManager)DaoManager.getInstance()).getSimpleConfigDao().store();
    }
    
    public static void doNoThanks()
    {
        ((DaoManager)DaoManager.getInstance()).getSimpleConfigDao().remove(SimpleConfigDao.KEY_TIME_VECTOR);
        ((DaoManager)DaoManager.getInstance()).getSimpleConfigDao().remove(SimpleConfigDao.KEY_LOST_GPS_STAMP);
        ((DaoManager)DaoManager.getInstance()).getSimpleConfigDao().remove(SimpleConfigDao.KEY_TANS_ERROR_STAMP);
        ((DaoManager)DaoManager.getInstance()).getSimpleConfigDao().remove(SimpleConfigDao.KEY_NET_ERROR_STAMP);
        ((DaoManager)DaoManager.getInstance()).getSimpleConfigDao().put(SimpleConfigDao.KEY_IS_CURRENT_RATED, true);
        ((DaoManager)DaoManager.getInstance()).getSimpleConfigDao().put(SimpleConfigDao.KEY_IS_PREVIOUS_RATED, true);
        ((DaoManager)DaoManager.getInstance()).getSimpleConfigDao().remove(SimpleConfigDao.KEY_IS_LOVE_APP_IN_FEEDBACK);
        ((DaoManager)DaoManager.getInstance()).getSimpleConfigDao().store();
    }
}
