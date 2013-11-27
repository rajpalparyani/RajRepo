package com.telenav.module.nav.movingmap;

import com.telenav.data.datatypes.address.Stop;
import com.telenav.datatypes.route.Route;
import com.telenav.res.IStringNav;
import com.telenav.res.ResourceManager;

public class MovingMapHelper implements IStringNav
{
    final static int[] textIdForType = new int[]
    { RES_TEXT_CONTINUE, /* 0 */
    RES_TEXT_TURN_SLIGHT_RIGHT, /* 1 */
    RES_TEXT_TURN_RIGHT, /* 2 */
    RES_TEXT_TURN_HARD_RIGHT, /* 3 */
    RES_TEXT_U_TURN, /* 4 */
    RES_TEXT_TURN_HARD_LEFT, /* 5 */
    RES_TEXT_TURN_LEFT, /* 6 */
    RES_TEXT_TURN_SLIGHT_LEFT, /* 7 */
    -8, -8, RES_TEXT_ENTER, /* 10 */
    RES_TEXT_ENTER, /* 11 */
    RES_TEXT_EXIT, /* 12 */
    RES_TEXT_EXIT, /* 13 */
    RES_TEXT_MERGE, /* 14 */
    RES_TEXT_MERGE, /* 15 */
    RES_TEXT_CONTINUE, /* 16 */
    RES_TEXT_ARRIVE, /* 17 */
    RES_TEXT_ARRIVE, /* 18 */
    RES_TEXT_ARRIVE, /* 19 */
    RES_TEXT_ROUND_ABOUT, /* 20 */
    RES_TEXT_EXIT_ROUND_ABOUT, /* 21 */
    -22, -23, -24, -25, RES_TEXT_BEAR_LEFT, /* 26 */
    RES_TEXT_BEAR_RIGHT, /* 27 */
    };

    public static String getTurnDescription(int turnType, int[] textIdForTypes)
    {
        int textId = turnType >= 0 && turnType < textIdForTypes.length ? textIdForTypes[turnType] : -1;
        if (textId < 0)
        {
            textId = getSpecialTurnTextId(turnType);
        }
        if (textId < 0)
        {
            textId = IStringNav.RES_START_AT;
        }
        return ResourceManager.getInstance().getCurrentBundle().getString(textId, IStringNav.FAMILY_NAV);
    }

    protected static int getSpecialTurnTextId(int turnType)
    {
        switch (turnType)
        {
            case Route.TURN_TYPE_U_TURN_L:
                return IStringNav.RES_U_TURN;
            case Route.TURN_TYPE_ROUNDABOUT_ENTER_L:
                return IStringNav.RES_ROUND_ABOUT;
            case Route.TURN_TYPE_ROUNDABOUT_EXIT_L:
                return IStringNav.RES_EXIT_ROUND_ABOUT;
            case Route.TURN_TYPE_STAY_MIDDLE:
                return IStringNav.RES_STAY_MIDDLE;
            case Route.TURN_TYPE_F2Z_ENTER_LEFT:
                return IStringNav.RES_F2Z_ENTER_LEFT;
            case Route.TURN_TYPE_Z2F_EXIT_LEFT:
                return IStringNav.RES_Z2F_EXIT_LEFT;
            case Route.TURN_TYPE_MULIT_FORK_STAY_RIGHT:
                return IStringNav.RES_MULIT_FORK_STAY_RIGHT;
            case Route.TURN_TYPE_MULIT_FORK_STAY_LEFT:
                return IStringNav.RES_MULIT_FORK_STAY_LEFT;
            case Route.TURN_TYPE_H2R_EXIT_MIDDLE:
                return IStringNav.RES_H2R_EXIT_MIDDLE;
            case Route.TURN_TYPE_H2H_EXIT_LEFT:
            case Route.TURN_TYPE_H2H_EXIT_RIGHT:
                return IStringNav.RES_H2H_EXIT;
            case Route.TURN_TYPE_L2H_ENTER:
                return IStringNav.RES_ENTER;
            case Route.TURN_TYPE_F2Z_ENTER_RIGHT:
                return IStringNav.RES_F2Z_ENTER_RIGHT;
            case Route.TURN_TYPE_Z2F_EXIT_RIGHT:
                return IStringNav.RES_Z2F_EXIT_RIGHT;
            case Route.TURN_TYPE_LEFT_U_TURN:
            case Route.TURN_TYPE_RIGHT_U_TURN:
                return IStringNav.RES_U_TURN;
            case Route.TURN_TYPE_WAYPOINT_LEFT:
            case Route.TURN_TYPE_WAYPOINT_RIGHT:
            case Route.TURN_TYPE_WAYPOINT_AHEAD:
                return IStringNav.RES_WAYPOINT;
            default:
                return -1;
        }
    }

    protected static String getDestAddressStr(Stop destStop)
    {
        String address = "";
        String addr = destStop.getFirstLine();
        String poiName = destStop.getLabel();
        String city = destStop.getCity();
        if (addr != null && addr.length() != 0)
        {
            address = addr;
        }
        else if (poiName != null && poiName.length() != 0 && !poiName.equals(city))
        {
            address = poiName;
        }
        else if (city != null && city.length() != 0)
        {
            address = city;
        }

        return address;
    }

}