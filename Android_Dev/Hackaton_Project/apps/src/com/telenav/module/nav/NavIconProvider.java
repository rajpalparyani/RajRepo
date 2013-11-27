/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * NavIconProvider.java
 *
 */
package com.telenav.module.nav;

import com.telenav.datatypes.route.Route;
import com.telenav.i18n.ResourceBundle;
import com.telenav.res.IStringNav;
import com.telenav.res.ResourceManager;
import com.telenav.res.converter.StringConverter;
import com.telenav.tnui.core.TnUiArgs.TnUiArgAdapter;
import com.telenav.ui.ImageDecorator;

/**
 * @author yning (yning@telenav.cn)
 * @date 2010-11-30
 */
public class NavIconProvider
{
    public static TnUiArgAdapter getTurnIcon(int turnType, boolean isBig)
    {
        switch (turnType)
        {
            case Route.TURN_TYPE_CONTINUE:
            {
                if (isBig)
                {
                    return ImageDecorator.IMG_TURN_ICON_BIG_CONTINUE_UNFOCUSED;
                }
                else
                {
                    return ImageDecorator.IMG_TURN_ICON_SMALL_CONTINUE_UNFOCUSED;
                }
            }
            case Route.TURN_TYPE_TURN_SLIGHT_RIGHT:
            {
                if (isBig)
                {
                    return ImageDecorator.IMG_TURN_ICON_BIG_SLIDE_RIGHT_UNFOCUSED;
                }
                else
                {
                    return ImageDecorator.IMG_TURN_ICON_SMALL_SLIDE_RIGHT_UNFOCUSED;
                }
            }
            case Route.TURN_TYPE_TURN_RIGHT:
            {
                if (isBig)
                {
                    return ImageDecorator.IMG_TURN_ICON_BIG_RIGHT_UNFOCUSED;
                }
                else
                {
                    return ImageDecorator.IMG_TURN_ICON_SMALL_RIGHT_UNFOCUSED;
                }
            }
            case Route.TURN_TYPE_TURN_HARD_RIGHT:
            {
                if (isBig)
                {
                    return ImageDecorator.IMG_TURN_ICON_BIG_HARD_RIGHT_UNFOCUSED;
                }
                else
                {
                    return ImageDecorator.IMG_TURN_ICON_SMALL_HARD_RIGHT_UNFOCUSED;
                }
            }
            case Route.TURN_TYPE_U_TURN:
            case Route.TURN_TYPE_LEFT_U_TURN:
            {
                // Fix bug TN-1185.
                if (isBig)
                {
                    return ImageDecorator.IMG_TURN_ICON_BIG_UTURN_LEFT_UNFOCUSED;
                }
                else
                {
                    return ImageDecorator.IMG_TURN_ICON_SMALL_UTURN_LEFT_UNFOCUSED;
                }
            }
            case Route.TURN_TYPE_RIGHT_U_TURN:
            {
                if(isBig)
                {
                    return ImageDecorator.IMG_TURN_ICON_BIG_UTURN_RIGHT_UNFOCUSED;
                }
                else
                {
                    return ImageDecorator.IMG_TURN_ICON_SMALL_UTURN_RIGHT_UNFOCUSED;
                }
            }
            case Route.TURN_TYPE_TURN_HARD_LEFT:
            {
                if (isBig)
                {
                    return ImageDecorator.IMG_TURN_ICON_BIG_HARD_LEFT_UNFOCUSED;
                }
                else
                {
                    return ImageDecorator.IMG_TURN_ICON_SMALL_HARD_LEFT_UNFOCUSED;
                }
            }

            case Route.TURN_TYPE_TURN_LEFT:
            {
                if (isBig)
                {
                    return ImageDecorator.IMG_TURN_ICON_BIG_LEFT_UNFOCUSED;
                }
                else
                {
                    return ImageDecorator.IMG_TURN_ICON_SMALL_LEFT_UNFOCUSED;
                }
            }

            case Route.TURN_TYPE_TURN_SLIGHT_LEFT:
            {
                if (isBig)
                {
                    return ImageDecorator.IMG_TURN_ICON_BIG_SLIDE_LEFT_UNFOCUSED;
                }
                else
                {
                    return ImageDecorator.IMG_TURN_ICON_SMALL_SLIDE_LEFT_UNFOCUSED;
                }
            }
            case Route.TURN_TYPE_F2Z_ENTER_LEFT:
            case Route.TURN_TYPE_ENTER_RIGHT:
            {
                if (isBig)
                {
                    return ImageDecorator.IMG_TURN_ICON_BIG_MERGE_LEFT_UNFOCUSED;
                }
                else
                {
                    return ImageDecorator.IMG_TURN_ICON_SMALL_MERGE_LEFT_UNFOCUSED;
                }
            }
            case Route.TURN_TYPE_ENTER_LEFT:
            case Route.TURN_TYPE_F2Z_ENTER_RIGHT:
            case Route.TURN_TYPE_L2H_ENTER:
            {
                if (isBig)
                {
                    return ImageDecorator.IMG_TURN_ICON_BIG_MERGE_RIGHT_UNFOCUSED;
                }
                else
                {
                    return ImageDecorator.IMG_TURN_ICON_SMALL_MERGE_RIGHT_UNFOCUSED;
                }
            }
            case Route.TURN_TYPE_EXIT_LEFT:
            case Route.TURN_TYPE_Z2F_EXIT_LEFT:
            case Route.TURN_TYPE_H2H_EXIT_LEFT:
            {
                if (isBig)
                {
                    return ImageDecorator.IMG_TURN_ICON_BIG_EXIT_LEFT_UNFOCUSED;
                }
                else
                {
                    return ImageDecorator.IMG_TURN_ICON_SMALL_EXIT_LEFT_UNFOCUSED;
                }
            }
            case Route.TURN_TYPE_EXIT_RIGHT:
            case Route.TURN_TYPE_Z2F_EXIT_RIGHT:
            case Route.TURN_TYPE_H2H_EXIT_RIGHT:
            {
                if (isBig)
                {
                    return ImageDecorator.IMG_TURN_ICON_BIG_EXIT_RIGHT_UNFOCUSED;
                }
                else
                {
                    return ImageDecorator.IMG_TURN_ICON_SMALL_EXIT_RIGHT_UNFOCUSED;
                }
            }
            case Route.TURN_TYPE_MERGE_LEFT:
            {
                if (isBig)
                {
                    return ImageDecorator.IMG_TURN_ICON_BIG_MERGE_LEFT_UNFOCUSED;
                }
                else
                {
                    return ImageDecorator.IMG_TURN_ICON_SMALL_MERGE_LEFT_UNFOCUSED;
                }
            }
            case Route.TURN_TYPE_MERGE_RIGHT:
            {
                if (isBig)
                {
                    return ImageDecorator.IMG_TURN_ICON_BIG_MERGE_RIGHT_UNFOCUSED;
                }
                else
                {
                    return ImageDecorator.IMG_TURN_ICON_SMALL_MERGE_RIGHT_UNFOCUSED;
                }
            }
            case Route.TURN_TYPE_MERGE_AHEAD:
            {
                // FIXME: do not have icon for this type and so do Tn62.
                return null;
            }
            case Route.TURN_TYPE_DESTINATION_LEFT:
            {
                if (isBig)
                {
                    return ImageDecorator.IMG_TURN_ICON_BIG_ON_LEFT_UNFOCUSED;
                }
                else
                {
                    return ImageDecorator.IMG_TURN_ICON_SMALL_ON_LEFT_UNFOCUSED;
                }
            }
            case Route.TURN_TYPE_DESTINATION_RIGHT:
            {
                if (isBig)
                {
                    return ImageDecorator.IMG_TURN_ICON_BIG_ON_RIGHT_UNFOCUSED;
                }
                else
                {
                    return ImageDecorator.IMG_TURN_ICON_SMALL_ON_RIGHT_UNFOCUSED;
                }
            }
            case Route.TURN_TYPE_DESTINATION_AHEAD:
            {
                if (isBig)
                {
                    return ImageDecorator.IMG_TURN_ICON_BIG_AHEAD_UNFOCUSED;
                }
                else
                {
                    return ImageDecorator.IMG_TURN_ICON_SMALL_AHEAD_UNFOCUSED;
                }
            }
            case Route.TURN_TYPE_ROUNDABOUT_ENTER:
            {
                if (isBig)
                {
                    return ImageDecorator.IMG_TURN_ICON_BIG_ROUNDABOUT_ENTERTORIGHT_UNFOCUSED;
                }
                else
                {
                    return ImageDecorator.IMG_TURN_ICON_SMALL_ROUNDABOUT_ENTERTORIGHT_UNFOCUSED;
                }
            }
            case Route.TURN_TYPE_ROUNDABOUT_EXIT:
            {
                if (isBig)
                {
                    return ImageDecorator.IMG_TURN_ICON_BIG_EXIT_ROUNDABOUT_TOPRIGHT_UNFOCUSED;
                }
                else
                {
                    return ImageDecorator.IMG_TURN_ICON_SMALL_EXIT_ROUNDABOUT_TOPRIGHT_UNFOCUSED;
                }
            }
            case Route.TURN_TYPE_STAY_LEFT:
            case Route.TURN_TYPE_MULIT_FORK_STAY_LEFT:
            {
                if (isBig)
                {
                    return ImageDecorator.IMG_TURN_ICON_BIG_BEAR_LEFT_UNFOCUSED;
                }
                else
                {
                    return ImageDecorator.IMG_TURN_ICON_SMALL_BEAR_LEFT_UNFOCUSED;
                }
            }
            case Route.TURN_TYPE_STAY_RIGHT:
            case Route.TURN_TYPE_MULIT_FORK_STAY_RIGHT:
            {
                if (isBig)
                {
                    return ImageDecorator.IMG_TURN_ICON_BIG_BEAR_RIGHT_UNFOCUSED;
                }
                else
                {
                    return ImageDecorator.IMG_TURN_ICON_SMALL_BEAR_RIGHT_UNFOCUSED;
                }
            }
            case Route.TURN_TYPE_START:
            {
                if (isBig)
                {
                    return ImageDecorator.IMG_TURN_ICON_BIG_START_UNFOCUSED;
                }
                else
                {
                    return ImageDecorator.IMG_TURN_ICON_SMALL_START_UNFOCUSED;
                }
            }
            case Route.TURN_TYPE_U_TURN_L:
            {
                if (isBig)
                {
                    return ImageDecorator.IMG_TURN_ICON_BIG_UTURN_LEFT_UNFOCUSED;
                }
                else
                {
                    return ImageDecorator.IMG_TURN_ICON_SMALL_UTURN_LEFT_UNFOCUSED;
                }
            }
            case Route.TURN_TYPE_ROUNDABOUT_ENTER_L:
            {
                if (isBig)
                {
                    return ImageDecorator.IMG_TURN_ICON_BIG_ROUNDABOUT_ENTERTOLEFT_UNFOCUSED;
                }
                else
                {
                    return ImageDecorator.IMG_TURN_ICON_SMALL_ROUNDABOUT_ENTERTOLEFT_UNFOCUSED;
                }
            }
            case Route.TURN_TYPE_ROUNDABOUT_EXIT_L:
            {
                if (isBig)
                {
                    return ImageDecorator.IMG_TURN_ICON_BIG_EXIT_ROUNDABOUT_TOPLEFT_UNFOCUSED;
                }
                else
                {
                    return ImageDecorator.IMG_TURN_ICON_SMALL_EXIT_ROUNDABOUT_TOPLEFT_UNFOCUSED;
                }
            }
            case Route.TURN_TYPE_STAY_MIDDLE:
            case Route.TURN_TYPE_H2R_EXIT_MIDDLE:
            {
                if (isBig)
                {
                    return ImageDecorator.IMG_TURN_ICON_BIG_STAY_MIDDLE_UNFOCUSED;
                }
                else
                {
                    return ImageDecorator.IMG_TURN_ICON_SMALL_STAY_MIDDLE_UNFOCUSED;
                }
            }
            case Route.TURN_TYPE_WAYPOINT_AHEAD:
            {
                if (isBig)
                {
                    return ImageDecorator.IMG_TURN_ICON_BIG_VIA_POINT_AHEAD_UNFOCUSED;
                }
                else
                {
                    return ImageDecorator.IMG_TURN_ICON_SMALL_VIA_POINT_AHEAD_UNFOCUSED;
                }
            }
            case Route.TURN_TYPE_WAYPOINT_LEFT:
            {
                if (isBig)
                {
                    return ImageDecorator.IMG_TURN_ICON_BIG_VIA_POINT_LEFT_UNFOCUSED;
                }
                else
                {
                    return ImageDecorator.IMG_TURN_ICON_SMALL_VIA_POINT_LEFT_UNFOCUSED;
                }
            }
            case Route.TURN_TYPE_WAYPOINT_RIGHT:
            {
                if (isBig)
                {
                    return ImageDecorator.IMG_TURN_ICON_BIG_VIA_POINT_RIGHT_UNFOCUSED;
                }
                else
                {
                    return ImageDecorator.IMG_TURN_ICON_SMALL_VIA_POINT_RIGHT_UNFOCUSED;
                }
            }
        }
        return null;
    }

    public static TnUiArgAdapter getTightTurnIcon(int turnType)
    {
        switch (turnType)
        {
            case Route.TURN_TYPE_CONTINUE:
            {
                return ImageDecorator.IMG_TIGHT_TURN_ICON_CONTINUE_UNFOCUSED;
            }
            case Route.TURN_TYPE_TURN_SLIGHT_RIGHT:
            {
                return ImageDecorator.IMG_TIGHT_TURN_ICON_SLIDE_RIGHT_UNFOCUSED;
            }
            case Route.TURN_TYPE_TURN_RIGHT:
            {
                return ImageDecorator.IMG_TIGHT_TURN_ICON_RIGHT_UNFOCUSED;
            }
            case Route.TURN_TYPE_TURN_HARD_RIGHT:
            {
                return ImageDecorator.IMG_TIGHT_TURN_ICON_HARD_RIGHT_UNFOCUSED;
            }
            case Route.TURN_TYPE_U_TURN:
            case Route.TURN_TYPE_LEFT_U_TURN:
            {
                return ImageDecorator.IMG_TIGHT_TURN_ICON_UTURN_LEFT_UNFOCUSED;
            }
            case Route.TURN_TYPE_RIGHT_U_TURN:
            {
                return ImageDecorator.IMG_TIGHT_TURN_ICON_UTURN_RIGHT_UNFOCUSED;
            }
            case Route.TURN_TYPE_TURN_HARD_LEFT:
            {
                return ImageDecorator.IMG_TIGHT_TURN_ICON_HARD_LEFT_UNFOCUSED;
            }

            case Route.TURN_TYPE_TURN_LEFT:
            {
                return ImageDecorator.IMG_TIGHT_TURN_ICON_LEFT_UNFOCUSED;
            }

            case Route.TURN_TYPE_TURN_SLIGHT_LEFT:
            {
                return ImageDecorator.IMG_TIGHT_TURN_ICON_SLIDE_LEFT_UNFOCUSED;
            }
            case Route.TURN_TYPE_F2Z_ENTER_LEFT:
            case Route.TURN_TYPE_ENTER_RIGHT:
            {
                return ImageDecorator.IMG_TIGHT_TURN_ICON_MERGE_LEFT_UNFOCUSED;
            }
            case Route.TURN_TYPE_ENTER_LEFT:
            case Route.TURN_TYPE_F2Z_ENTER_RIGHT:
            case Route.TURN_TYPE_L2H_ENTER:
            {
                return ImageDecorator.IMG_TIGHT_TURN_ICON_MERGE_RIGHT_UNFOCUSED;
            }
            case Route.TURN_TYPE_EXIT_LEFT:
            case Route.TURN_TYPE_Z2F_EXIT_LEFT:
            case Route.TURN_TYPE_H2H_EXIT_LEFT:
            {
                return ImageDecorator.IMG_TIGHT_TURN_ICON_EXIT_LEFT_UNFOCUSED;
            }
            case Route.TURN_TYPE_EXIT_RIGHT:
            case Route.TURN_TYPE_Z2F_EXIT_RIGHT:
            case Route.TURN_TYPE_H2H_EXIT_RIGHT:
            {
                return ImageDecorator.IMG_TIGHT_TURN_ICON_EXIT_RIGHT_UNFOCUSED;
            }
            case Route.TURN_TYPE_MERGE_LEFT:
            {
                return ImageDecorator.IMG_TIGHT_TURN_ICON_MERGE_LEFT_UNFOCUSED;
            }
            case Route.TURN_TYPE_MERGE_RIGHT:
            {
                return ImageDecorator.IMG_TIGHT_TURN_ICON_MERGE_RIGHT_UNFOCUSED;
            }
            case Route.TURN_TYPE_MERGE_AHEAD:
            {
                // FIXME: do not have icon for this type and so do Tn62.
                return null;
            }
            case Route.TURN_TYPE_DESTINATION_LEFT:
            {
                return ImageDecorator.IMG_TIGHT_TURN_ICON_ON_LEFT_UNFOCUSED;
            }
            case Route.TURN_TYPE_DESTINATION_RIGHT:
            {
                return ImageDecorator.IMG_TIGHT_TURN_ICON_ON_RIGHT_UNFOCUSED;
            }
            case Route.TURN_TYPE_DESTINATION_AHEAD:
            {
                return ImageDecorator.IMG_TIGHT_TURN_ICON_AHEAD_UNFOCUSED;
            }
            case Route.TURN_TYPE_ROUNDABOUT_ENTER:
            {
                return ImageDecorator.IMG_TIGHT_TURN_ICON_ROUNDABOUT_ENTERTORIGHT_UNFOCUSED;
            }
            case Route.TURN_TYPE_ROUNDABOUT_EXIT:
            {
                return ImageDecorator.IMG_TIGHT_TURN_ICON_EXIT_ROUNDABOUT_TOPRIGHT_UNFOCUSED;
            }
            case Route.TURN_TYPE_STAY_LEFT:
            case Route.TURN_TYPE_MULIT_FORK_STAY_LEFT:
            {
                return ImageDecorator.IMG_TIGHT_TURN_ICON_BEAR_LEFT_UNFOCUSED;
            }
            case Route.TURN_TYPE_STAY_RIGHT:
            case Route.TURN_TYPE_MULIT_FORK_STAY_RIGHT:
            {
                return ImageDecorator.IMG_TIGHT_TURN_ICON_BEAR_RIGHT_UNFOCUSED;
            }
            case Route.TURN_TYPE_START:
            {
                return ImageDecorator.IMG_TIGHT_TURN_ICON_START_UNFOCUSED;
            }
            case Route.TURN_TYPE_U_TURN_L:
            {
                return ImageDecorator.IMG_TIGHT_TURN_ICON_UTURN_LEFT_UNFOCUSED;
            }
            case Route.TURN_TYPE_ROUNDABOUT_ENTER_L:
            {
                return ImageDecorator.IMG_TIGHT_TURN_ICON_ROUNDABOUT_ENTERTOLEFT_UNFOCUSED;
            }
            case Route.TURN_TYPE_ROUNDABOUT_EXIT_L:
            {
                return ImageDecorator.IMG_TIGHT_TURN_ICON_EXIT_ROUNDABOUT_TOPLEFT_UNFOCUSED;
            }
//            case Route.TURN_TYPE_STAY_MIDDLE:
//            case Route.TURN_TYPE_H2R_EXIT_MIDDLE:
//            {
//                return ImageDecorator.IMG_TIGHT_TURN_ICON_STAY_MIDDLE_UNFOCUSED;
//            }
//            case Route.TURN_TYPE_WAYPOINT_AHEAD:
//            {
//                return ImageDecorator.IMG_TIGHT_TURN_ICON_VIA_POINT_AHEAD_UNFOCUSED;
//            }
//            case Route.TURN_TYPE_WAYPOINT_LEFT:
//            {
//                return ImageDecorator.IMG_TIGHT_TURN_ICON_VIA_POINT_LEFT_UNFOCUSED;
//            }
//            case Route.TURN_TYPE_WAYPOINT_RIGHT:
//            {
//                return ImageDecorator.IMG_TIGHT_TURN_ICON_VIA_POINT_RIGHT_UNFOCUSED;
//            }
        }
        return null;
    }
    
    public static String getDestinationLocate(int turnType)
    {
        StringConverter converter = ResourceManager.getInstance().getStringConverter();
        ResourceBundle bundle = ResourceManager.getInstance().getCurrentBundle();
        switch (turnType)
        {
            case Route.TURN_TYPE_DESTINATION_AHEAD :
            {
                return converter.convert(
                    bundle.getString(IStringNav.RES_DESTINATION_ON, IStringNav.FAMILY_NAV), 
                    new String[]{ bundle.getString(IStringNav.RES_AHEAD, IStringNav.FAMILY_NAV) });
            }
            case Route.TURN_TYPE_DESTINATION_LEFT :
            {
                return converter.convert(
                    bundle.getString(IStringNav.RES_DESTINATION_ON, IStringNav.FAMILY_NAV), 
                    new String[]{ bundle.getString(IStringNav.RES_LEFT, IStringNav.FAMILY_NAV) });
            }
            case Route.TURN_TYPE_DESTINATION_RIGHT :
            {
                return converter.convert(
                    bundle.getString(IStringNav.RES_DESTINATION_ON, IStringNav.FAMILY_NAV), 
                    new String[]{ bundle.getString(IStringNav.RES_RIGHT, IStringNav.FAMILY_NAV) });
            }
        }
        return null;
    }
}
