/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * CurrentStreetNameComponent.java
 *
 */
package com.telenav.module.nav;

import java.util.Vector;

import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.datatypes.preference.Preference;
import com.telenav.datatypes.route.Route;
import com.telenav.navsdk.events.NavigationData.RouteTurnType;
import com.telenav.res.IStringNav;
import com.telenav.res.ResourceManager;
import com.telenav.threadpool.INotifierListener;
import com.telenav.threadpool.Notifier;
import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.graphics.AbstractTnFont;
import com.telenav.tnui.graphics.AbstractTnGraphics;
import com.telenav.tnui.graphics.AbstractTnImage;
import com.telenav.ui.ImageDecorator;
import com.telenav.ui.UiStyleManager;
import com.telenav.ui.frogui.text.FrogTextHelper;
import com.telenav.ui.tnui.text.TnTextLine;
import com.telenav.ui.tnui.text.TnTextParser;

/**
 *@author zhdong@telenav.cn
 *@date 2011-1-25
 */
public class CurrentStreetNameComponent extends AbstractTnComponent implements INotifierListener
{

    private static final String DEFAULT_ADI_STRING = "...";

    public final static int LANE_TYPE_CONTINUE = 0;

    public final static int LANE_TYPE_CONTINUE_LEFT = 1;

    public final static int LANE_TYPE_CONTINUE_RIGHT = 2;

    public final static int LANE_TYPE_TURN_LEFT = 3;

    public final static int LANE_TYPE_TURN_RIGHT = 4;

    public final static int LANE_TYPE_UTURN_LEFT = 5;

    public final static int LANE_TYPE_UTURN_RIGHT = 6;
    
    private static int ANIMATION_STEPS = 10;

    private String streetName;

    private TnTextLine streetNameLine;
    
    private int[] laneInfos = null;

    RouteTurnType[] laneTypes = null;

    /**
     * From ANIMATION_STEPS to 0
     */
    private int timerStep = ANIMATION_STEPS;

    private boolean isDisappearing;
    
    private boolean isLaneAssistShown;

    private long lastNotifyTimestamp;

    public CurrentStreetNameComponent(int id)
    {
        super(id);
    }

    protected void paint(AbstractTnGraphics graphics)
    {
        NavBottomStatusBarHelper.getInstance().drawBackground(graphics, this, true);
        int[] laneInfos = null;
        RouteTurnType[] laneTypes = null;
        synchronized (this) 
        {
            laneInfos = this.laneInfos;
            laneTypes = this.laneTypes;
        }
        if (laneInfos != null && laneTypes != null)
        {
            int width = this.getWidth();
            int height = this.getHeight();
            int imageHeight = ImageDecorator.IMG_LANE_ASSIST_AHEAD_FOCUSED.getImage().getHeight();
            int imageWidth = ImageDecorator.IMG_LANE_ASSIST_AHEAD_FOCUSED.getImage().getWidth();
            int imageX = (width - imageWidth * laneInfos.length) / 2;
            int imageY = (height - imageHeight) / 2;
            
            if (timerStep > 0)
            {
                int offset = timerStep * height / ANIMATION_STEPS;
                imageY += offset;
                graphics.pushClip(0, 0, width, offset);
                drawStreetName(graphics);
                graphics.popClip();
            }

            for (int i = 0; i < laneInfos.length; i++)
            {
                AbstractTnImage turnImage = null;
                switch (laneTypes[i])
                {
                    case RouteTurnType_L2L_TURN_LEFT:
                    case RouteTurnType_L2L_UTURN_LEFT:
                    case RouteTurnType_L2L_TURN_HARD_LEFT:
                    {
                        if (laneInfos[i] == 1)
                        {
                            turnImage = ImageDecorator.IMG_LANE_ASSIST_LEFT_TURN_FOCUSED.getImage();
                        }
                        else
                        {
                            turnImage = ImageDecorator.IMG_LANE_ASSIST_LEFT_TURN_UNFOCUSED.getImage();
                        }
                    }
                        break;
                    case RouteTurnType_L2L_TURN_SLIGHT_LEFT:
                    {
                        if (laneInfos[i] == 1)
                        {
                            turnImage = ImageDecorator.IMG_LANE_ASSIST_LEFT_TURN_FOCUSED.getImage();
                        }
                        else
                        {
                            turnImage = ImageDecorator.IMG_LANE_ASSIST_LEFT_TURN_UNFOCUSED.getImage();
                        }
                    }
                        break;
                    case RouteTurnType_L2L_TURN_RIGHT:
                    case RouteTurnType_L2L_UTURN_RIGHT:
                    case RouteTurnType_L2L_TURN_HARD_RIGHT:
                    {
                        if (laneInfos[i] == 1)
                        {
                            turnImage = ImageDecorator.IMG_LANE_ASSIST_RIGHT_TURN_FOCUSED.getImage();
                        }
                        else
                        {
                            turnImage = ImageDecorator.IMG_LANE_ASSIST_RIGHT_TURN_UNFOCUSED.getImage();
                        }
                    }
                        break;
                    case RouteTurnType_L2L_TURN_SLIGHT_RIGHT:
                    {
                        if (laneInfos[i] == 1)
                        {
                            turnImage = ImageDecorator.IMG_LANE_ASSIST_RIGHT_TURN_FOCUSED.getImage();
                        }
                        else
                        {
                            turnImage = ImageDecorator.IMG_LANE_ASSIST_RIGHT_TURN_UNFOCUSED.getImage();
                        }
                    }
                        break;
                    case RouteTurnType_L2L_CONTINUE:
                    {
                        if (laneInfos[i] == 1)
                        {
                            turnImage = ImageDecorator.IMG_LANE_ASSIST_AHEAD_FOCUSED.getImage();
                        }
                        else
                        {
                            turnImage = ImageDecorator.IMG_LANE_ASSIST_AHEAD_UNFOCUSED.getImage();
                        }
                    }
                        break;
                    default:
                        break;
                }

                if (turnImage != null)
                {
                    graphics.drawImage(turnImage, imageX, imageY, AbstractTnGraphics.LEFT | AbstractTnGraphics.TOP);
                    imageX += turnImage.getWidth();
                }
            }
        }
        else
        {
            drawStreetName(graphics);
        }
        
    }
    
    private void drawStreetName(AbstractTnGraphics graphics)
    {
        String extraInfo = "";

        if (NavBottomStatusBarHelper.getInstance().isNoGps)
        {
            extraInfo = ResourceManager.getInstance().getCurrentBundle().getString(IStringNav.RES_WEAK_GPS, IStringNav.FAMILY_NAV);
        }
        else if (NavBottomStatusBarHelper.getInstance().isOverSpeedLimit)
        {
            extraInfo = ResourceManager.getInstance().getCurrentBundle().getString(IStringNav.RES_OVER_SPEED_LIMIT, IStringNav.FAMILY_NAV);
        }
        else if (NavBottomStatusBarHelper.getInstance().isOutofCoverage)
        {
            extraInfo = ResourceManager.getInstance().getCurrentBundle().getString(IStringNav.RES_NO_NETWORK, IStringNav.FAMILY_NAV);
        }

        int width = getWidth();
        int height = getHeight();
        AbstractTnFont streetNameFont = UiStyleManager.getInstance().getFont(UiStyleManager.FONT_NAV_SCREEN_BOTTOM_STREET_NAME);
        AbstractTnFont extraInfoFont = UiStyleManager.getInstance().getFont(UiStyleManager.FONT_NAV_SCREEN_BOTTOM_STREET_NAME_EXTRA_INFO);
        graphics.setColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_WH));

        int gap = extraInfoFont.getHeight() / 2;

        // draw street name
        int streetNameWidth = streetNameFont.stringWidth(streetName);
        if (streetNameWidth > width)
        {
            streetNameWidth = width;
        }
        int streetNameX = (width - streetNameWidth) / 2;
        int streetNameY = (height - streetNameFont.getHeight()) / 2;
        if (!extraInfo.equals(""))
        {
            gap = (height - streetNameFont.getHeight() - extraInfoFont.getHeight()) / 3;
            streetNameY -= (extraInfoFont.getHeight() + gap) / 2;
        }

        FrogTextHelper.paint(graphics, streetNameX, streetNameY, streetNameLine, streetNameFont, streetNameFont, streetNameWidth, true);

        if (!extraInfo.equals(""))
        {
            // draw extra info
            int extraInfoX = (width - extraInfoFont.stringWidth(extraInfo)) / 2;
            int extraInfoY = streetNameY + streetNameFont.getHeight() + gap;
            graphics.setFont(extraInfoFont);
            graphics.drawString(extraInfo, extraInfoX, extraInfoY, AbstractTnGraphics.LEFT | AbstractTnGraphics.TOP);
        }
    }
    
    public void update(String streetName, int[] laneInfos, RouteTurnType[] laneTypes, boolean isAdi)
    {
        if (streetName == null)
        {
            streetName = "";
        }
        if (!streetName.equals(this.streetName))
        {
            this.streetName = streetName;
            this.streetNameLine = TnTextParser.parse(streetName);
        }
        
        if(isAdi)
        {
            this.streetName = DEFAULT_ADI_STRING;
            this.streetNameLine = TnTextParser.parse(DEFAULT_ADI_STRING);
        }
        
        Preference routeStylePref = DaoManager.getInstance().getTripsDao().getPreference(Preference.ID_PREFERENCE_ROUTETYPE);
        int routeStyle = -1;
        if (routeStylePref != null)
        {
            routeStyle = routeStylePref.getIntValue();
        }
        boolean isPedestrian = (routeStyle & Route.ROUTE_PEDESTRIAN) == Route.ROUTE_PEDESTRIAN;
        if (laneInfos != null && laneTypes != null && !isPedestrian)
        {
            synchronized (this) 
            {
                this.laneInfos = laneInfos;
                this.laneTypes = laneTypes;
            }
            
            if (!isLaneAssistShown)
            {
                isLaneAssistShown = true;
                timerStep = ANIMATION_STEPS;
                isDisappearing = false;
                Vector listeners = Notifier.getInstance().getAllListeners();
                for (int i = listeners.size() - 1; i > 0; i--)
                {
                    Object listener = listeners.elementAt(i);
                    if (listener instanceof CurrentStreetNameComponent)
                    {
                        listeners.removeElement(listener);
                    }
                }
                Notifier.getInstance().addListener(this);
            }            
        }
        else
        {
            isDisappearing = true;
        }
        this.requestLayout();
    }

    public long getLastNotifyTimestamp()
    {
        return lastNotifyTimestamp;
    }

    public long getNotifyInterval()
    {
        if (timerStep <= 0)
        {
            // The popup animation is stopped, we don't have to notify too frequent.
            return 1000;
        }
        return 100;
    }

    public void notify(long timestamp)
    {
        if (isDisappearing)
        {
            if (timerStep >= ANIMATION_STEPS)
            {
                Notifier.getInstance().removeListener(this);
                synchronized (this) {
                    this.laneInfos = null;
                    this.laneTypes = null;
                }
                this.isLaneAssistShown = false;
                return;
            }
            timerStep++;
        }
        else
        {
            if (timerStep <= 0)
            {
                return;
            }
            timerStep--;
        }
        requestLayout();
    }

    public void setLastNotifyTimestamp(long timestamp)
    {
        lastNotifyTimestamp = timestamp;
    }
    
    protected void onUndisplay()
    {
        Notifier.getInstance().removeListener(this);
        super.onUndisplay();
    }
}
