/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * NavTopComponent.java
 *
 */
package com.telenav.module.nav.movingmap;

import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.datatypes.preference.Preference;
import com.telenav.datatypes.DataUtil;
import com.telenav.i18n.ResourceBundle;
import com.telenav.module.nav.NavIconProvider;
import com.telenav.module.nav.NavParameter;
import com.telenav.res.IStringNav;
import com.telenav.res.ResourceManager;
import com.telenav.res.converter.StringConverter;
import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.core.AbstractTnUiHelper;
import com.telenav.tnui.core.ITnUiEventListener;
import com.telenav.tnui.core.TnCommandEvent;
import com.telenav.tnui.core.TnMotionEvent;
import com.telenav.tnui.core.TnPrivateEvent;
import com.telenav.tnui.core.TnUiArgs;
import com.telenav.tnui.core.TnUiArgs.TnUiArgAdapter;
import com.telenav.tnui.core.TnUiEvent;
import com.telenav.tnui.core.TnUiTimer;
import com.telenav.tnui.graphics.AbstractTnFont;
import com.telenav.tnui.graphics.AbstractTnGraphics;
import com.telenav.tnui.graphics.AbstractTnImage;
import com.telenav.tnui.graphics.TnRect;
import com.telenav.ui.NinePatchImageDecorator;
import com.telenav.ui.UiStyleManager;

/**
 * @author zhdong@telenav.cn
 * @date 2011-2-28
 * 
 *       clean and refactor by Casper(pwang@telenav.cn)
 * @date 2012-4-26
 */
public class NavTopComponent extends AbstractTnComponent
{
    private int width;

    private int height;

    private int baseLine;

    private int percent;

    private int horizontalGap;

    private int nextStreetCmdId;

    private boolean needScroll;

    private String nextStreetName;

    private String distToTurnDigit = "";

    private String distToTurnUnit = "";

    private String distance;

    private String arrival;

    private AbstractTnImage turnImage;

    private MovingMapUiDecorator uiDecorator;

    private AbstractTnFont streetFont;

    private AbstractTnFont distToTurnDigitFont;

    private AbstractTnFont distToTurnUnitFont;

    private AbstractTnFont distAndEtaUnitFont;

    private AbstractTnFont distAndEtaDigitFont;

    private String[] ordinalNumbers;

    public NavTopComponent(int id)
    {
        super(id);
        uiDecorator = new MovingMapUiDecorator();

        this.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, uiDecorator.NAV_TITLE_WIDTH);
        this.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, uiDecorator.NAV_TITLE_HEIGHT);

        int bgColor = UiStyleManager.getInstance().getColor(UiStyleManager.BG_NAV_TITLE);
        this.setBackgroundColor(bgColor);

        streetFont = UiStyleManager.getInstance().getFont(UiStyleManager.FONT_NAV_STREET_NAME);
        distToTurnDigitFont = UiStyleManager.getInstance().getFont(UiStyleManager.FONT_NAV_TURN_DIST_DIGIT);
        distToTurnUnitFont = UiStyleManager.getInstance().getFont(UiStyleManager.FONT_NAV_TRUN_DIST_UNIT);
        distAndEtaDigitFont = UiStyleManager.getInstance().getFont(UiStyleManager.FONT_NAV_DIST_DIGIT);
        distAndEtaUnitFont = UiStyleManager.getInstance().getFont(UiStyleManager.FONT_NAV_DIST_UNIT);

        horizontalGap = streetFont.getMaxWidth() / 3;

        String ordinalNumberStr = ResourceManager.getInstance().getCurrentBundle()
                .getString(IStringNav.RES_ORDINAL_NUMBERS, IStringNav.FAMILY_NAV);
        if (ordinalNumberStr != null && ordinalNumberStr.trim().length() > 0)
        {
            ordinalNumbers = ordinalNumberStr.trim().split(",");
            if (ordinalNumbers != null && ordinalNumbers.length > 0)
            {
                for (int i = 0; i < ordinalNumbers.length; i++)
                {
                    ordinalNumbers[i] = ordinalNumbers[i].trim();
                }
            }
        }
    }

    protected void paint(AbstractTnGraphics graphics)
    {
        width = this.getWidth();
        height = this.getHeight();

        // draw percent progress bar
        if (percent > 0)
        {
            drawTurnPercent(graphics);
        }

        // draw trun icon
        int infoX = drawTurnIcon(graphics);

        // draw the nav dashboard info
        graphics.setColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_WH));
        if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
        {
            drawInfoPortrait(infoX, graphics);
        }
        else
        {
            drawInfoLandscape(infoX, graphics);
        }
    }

    private int drawTurnPercent(AbstractTnGraphics g)
    {
        int progressBarColor = UiStyleManager.getInstance().getColor(UiStyleManager.BG_NAV_PROGRESS);
        int progressBarWidth = width * percent / 100;
        int progressBarHeight = height;
        g.setColor(progressBarColor);
        g.fillRect(0, 0, progressBarWidth, progressBarHeight);
        return 0;
    }

    private int drawTurnIcon(AbstractTnGraphics g)
    {
        int leftPadding = uiDecorator.NAV_TITLE_LEFT_PADDING.getInt();
        int turnIconWidth = uiDecorator.TURN_ICON_WIDTH.getInt();
        int turnIconHeight = uiDecorator.TURN_ICON_HEIGHT.getInt();
        AbstractTnImage turnImageBg = NinePatchImageDecorator.NAV_TURN_BG.getImage();
        turnImageBg.setWidth(turnIconWidth);
        turnImageBg.setHeight(turnIconHeight);
        g.drawImage(turnImageBg, leftPadding + turnIconWidth / 2, height / 2, AbstractTnGraphics.HCENTER
                | AbstractTnGraphics.VCENTER);
        g.drawImage(turnImage, leftPadding + turnIconWidth / 2, height / 2, AbstractTnGraphics.HCENTER
                | AbstractTnGraphics.VCENTER);
        return leftPadding + turnIconWidth;
    }

    private void drawInfoPortrait(int infoX, AbstractTnGraphics g)
    {
        infoX += horizontalGap;

        // Top and and center will take up one, and bottom will take up two, for ascent will a bit bigger that the
        // actually heigh
        int verticalPadding = (height - streetFont.getAscent() - distToTurnDigitFont.getAscent()) / 4;
        baseLine = height - 2 * verticalPadding;

        drawNextStreet(infoX, streetFont.getAscent() + verticalPadding, width - infoX - horizontalGap, g);
        drawDistance(infoX, baseLine, g);
        if (arrival != null && distance != null)
        {
            drawEtaAndDistance(true, g);
        }
    }

    private void drawInfoLandscape(int infoX, AbstractTnGraphics g)
    {
        int distToTurnWidth = width * 1927 / 10000;
        int nextStreetWidth = width * 4667 / 10000;
        int distAndEtaTwoLineGap = distAndEtaUnitFont.getAscent() * 50 / 100;
        baseLine = height - (height - distAndEtaDigitFont.getAscent() - distAndEtaUnitFont.getAscent() - distAndEtaTwoLineGap)
                / 2;

        g.setColor(UiStyleManager.getInstance().getColor(UiStyleManager.NAV_TOP_SEPARATER_LINE_COLOR));
        int lineX = infoX + distToTurnWidth;
        int lineY = baseLine + streetFont.getDescent();
        g.drawLine(lineX, lineY, lineX, height - lineY);
        lineX += nextStreetWidth;
        g.drawLine(lineX, lineY, lineX, height - lineY);

        g.setColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_WH));
        int nextStreetX = infoX + distToTurnWidth + horizontalGap;
        int streetNameOffset = (nextStreetWidth - 2 * horizontalGap - streetFont.stringWidth(nextStreetName)) / 2;
        if (streetNameOffset > 0)
        {
            nextStreetX += streetNameOffset;
        }
        int nextStreetY = baseLine;
        drawNextStreet(nextStreetX, nextStreetY, nextStreetWidth - 2 * horizontalGap, g);

        int distToTurnX = infoX
                + (distToTurnWidth - distToTurnDigitFont.stringWidth(distToTurnDigit) - distToTurnUnitFont
                        .stringWidth(distToTurnUnit)) / 2;
        int distToTurnY = baseLine;
        drawDistance(distToTurnX, distToTurnY, g);

        if (arrival != null && distance != null)
        {
            drawEtaAndDistance(false, g);
        }
    }

    private void drawNextStreet(int nextStreetX, int nextStreetY, int nextStreetWidth, AbstractTnGraphics g)
    {
        // draw nextStreetName, if the length is less than the draw area, then make it be scrollable.
        g.pushClip(nextStreetX, nextStreetY - streetFont.getAscent(), nextStreetWidth, streetFont.getHeight());
        if (streetFont.stringWidth(nextStreetName) > nextStreetWidth)
        {
            nextStreetX -= (getNextStreetOffset(streetFont.stringWidth(nextStreetName) - nextStreetWidth));
            this.needScroll = true;
        }
        else
        {
            this.needScroll = false;
        }

        g.setFont(streetFont);
        g.drawString(nextStreetName, nextStreetX, nextStreetY, AbstractTnGraphics.LEFT | AbstractTnGraphics.BASE_LINE);
        g.popClip();
    }

    private void drawDistance(int distToTurnX, int distToTurnY, AbstractTnGraphics g)
    {
        g.setFont(distToTurnDigitFont);
        g.drawString(distToTurnDigit, distToTurnX, distToTurnY, AbstractTnGraphics.LEFT | AbstractTnGraphics.BASE_LINE);
        g.setFont(distToTurnUnitFont);
        g.drawString(distToTurnUnit, distToTurnX + distToTurnDigitFont.stringWidth(distToTurnDigit), distToTurnY,
            AbstractTnGraphics.LEFT | AbstractTnGraphics.BASE_LINE);
    }

    public void drawEtaAndDistance(boolean isPortrait, AbstractTnGraphics g)
    {
        ResourceBundle bundle = ResourceManager.getInstance().getCurrentBundle();
        String arrivalPrefix = bundle.getString(IStringNav.RES_ETA_PREFIX, IStringNav.FAMILY_NAV) + " ";
        String distancePrefix = bundle.getString(IStringNav.RES_DIST_PREFIX, IStringNav.FAMILY_NAV) + " ";
        StringBuffer arrivalDigit = new StringBuffer();
        StringBuffer distanceDigit = new StringBuffer();
        String arrivalSuffix = "";
        String distanceSuffix = "";
        for (int i = 0; i < arrival.length(); i++)
        {
            char c = arrival.charAt(i);
            if (Character.isDigit(c) || c == ':')
            {
                arrivalDigit.append(c);
            }
            else
            {
                arrivalSuffix = arrival.substring(i);
                break;
            }
        }
        for (int i = 0; i < distance.length(); i++)
        {
            char c = distance.charAt(i);
            if (Character.isDigit(c) || c == '.')
            {
                distanceDigit.append(c);
            }
            else
            {
                distanceSuffix = distance.substring(i);
                break;
            }
        }

        int prefixY = isPortrait ? baseLine
                : (baseLine - distAndEtaDigitFont.getAscent() - distAndEtaUnitFont.getAscent() * 50 / 100);
        int arrivalUnitWidth = distAndEtaUnitFont.stringWidth(arrivalSuffix);
        int distUnitWidth = distAndEtaUnitFont.stringWidth(distanceSuffix);
        int rightBorder = isPortrait ? horizontalGap : 0;

        int arrivalPrefixX, arrivalSuffixX;
        int distPrefixX, distSuffixX;
        TnRect distAndEtaRegionRect;
        if (isPortrait)
        {
            int prefixGap = 2;
            int etaWidth = distAndEtaDigitFont.stringWidth(arrivalDigit.toString())
                    + distAndEtaUnitFont.stringWidth(arrivalSuffix) + distAndEtaUnitFont.stringWidth(arrivalPrefix);
            int distWidth = distAndEtaDigitFont.stringWidth(distanceDigit.toString())
                    + distAndEtaUnitFont.stringWidth(distanceSuffix) + distAndEtaUnitFont.stringWidth(distancePrefix);
            int distAndEtaRegionWidth = (etaWidth > distWidth ? etaWidth : distWidth) + prefixGap;
            distAndEtaRegionRect = new TnRect(width - rightBorder - distAndEtaRegionWidth, baseLine
                    - distAndEtaDigitFont.getAscent(), width - rightBorder, baseLine + distAndEtaDigitFont.getDescent());

            arrivalSuffixX = rightBorder;
            arrivalPrefixX = rightBorder + arrivalUnitWidth + distAndEtaDigitFont.stringWidth(arrivalDigit.toString())
                    + prefixGap;

            distSuffixX = rightBorder;
            distPrefixX = rightBorder + distUnitWidth + distAndEtaDigitFont.stringWidth(distanceDigit.toString()) + prefixGap;
        }
        else
        {
            int distAndEtaRegionWidth = width * 1927 / 10000 - rightBorder;
            distAndEtaRegionRect = new TnRect(width - width * 1927 / 10000, 0, width - rightBorder, height);

            int borderGap = (distAndEtaRegionWidth - distAndEtaDigitFont.stringWidth(arrivalDigit.toString()) - distAndEtaUnitFont
                    .stringWidth(arrivalSuffix)) / 2;
            arrivalSuffixX = borderGap;
            arrivalPrefixX = (distAndEtaRegionWidth - distAndEtaUnitFont.stringWidth(arrivalPrefix)) / 2;

            borderGap = (distAndEtaRegionWidth - distAndEtaDigitFont.stringWidth(distanceDigit.toString()) - distAndEtaUnitFont
                    .stringWidth(distanceSuffix)) / 2;
            distSuffixX = borderGap;
            distPrefixX = (distAndEtaRegionWidth - distAndEtaUnitFont.stringWidth(distancePrefix)) / 2;
        }

        int arrivalX = width - getArrivalOffset(distAndEtaRegionRect.width());
        int distanceX = width - getDistanceOffset(distAndEtaRegionRect.width());

        g.pushClip(distAndEtaRegionRect.left, distAndEtaRegionRect.top, distAndEtaRegionRect.width(),
            distAndEtaRegionRect.height());
        g.setFont(distAndEtaUnitFont);
        g.drawString(arrivalSuffix, arrivalX - arrivalSuffixX, baseLine, AbstractTnGraphics.RIGHT
                | AbstractTnGraphics.BASE_LINE);
        g.setFont(distAndEtaDigitFont);
        g.drawString(arrivalDigit.toString(), arrivalX - arrivalSuffixX - arrivalUnitWidth, baseLine, AbstractTnGraphics.RIGHT
                | AbstractTnGraphics.BASE_LINE);
        g.setFont(distAndEtaUnitFont);
        g.drawString(arrivalPrefix, arrivalX - arrivalPrefixX, prefixY, AbstractTnGraphics.RIGHT | AbstractTnGraphics.BASE_LINE);
        g.setFont(distAndEtaUnitFont);
        g.drawString(distanceSuffix, distanceX - distSuffixX, baseLine, AbstractTnGraphics.RIGHT | AbstractTnGraphics.BASE_LINE);
        g.setFont(distAndEtaDigitFont);
        g.drawString(distanceDigit.toString(), distanceX - distSuffixX - distUnitWidth, baseLine, AbstractTnGraphics.RIGHT
                | AbstractTnGraphics.BASE_LINE);
        g.setFont(distAndEtaUnitFont);
        g.drawString(distancePrefix, distanceX - distPrefixX, prefixY, AbstractTnGraphics.RIGHT | AbstractTnGraphics.BASE_LINE);
        g.popClip();
    }

    public void updateNavStatus(NavParameter param)
    {
        int systemUnits = ((DaoManager) DaoManager.getInstance()).getPreferenceDao().getIntValue(
            Preference.ID_PREFERENCE_DISTANCEUNIT);
        int turnType = param.turnType;
        TnUiArgAdapter iconArg = NavIconProvider.getTurnIcon(turnType, true);
        turnImage = iconArg != null ? iconArg.getImage() : null;
        String nextStreet = NavIconProvider.getDestinationLocate(turnType);
        if (nextStreet == null)
        {
            nextStreet = param.nextStreetName;
            if (param.exitName != null && param.exitName.length() > 0)
            {
                String exitToExitName = ResourceManager.getInstance().getCurrentBundle()
                        .getString(IStringNav.RES_EXIT_TO_EXIT_NAME, IStringNav.FAMILY_NAV);
                String composedStr = ResourceManager.getInstance().getStringConverter().convert(exitToExitName, new String[]
                { param.exitName, nextStreet });
                nextStreet = composedStr;
            }
            else if (param.exitNumber != 0)
            {
                String ordinalNumber;
                if (ordinalNumbers != null && param.exitNumber > 0 && param.exitNumber - 1 < ordinalNumbers.length)
                {
                    ordinalNumber = ordinalNumbers[param.exitNumber - 1];
                }
                else
                {
                    ordinalNumber = param.exitNumber + "";
                }

                String exitToExitNum = ResourceManager.getInstance().getCurrentBundle()
                        .getString(IStringNav.RES_EXIT_TO_EXIT_NUMBER, IStringNav.FAMILY_NAV);
                String composedStr = ResourceManager.getInstance().getStringConverter().convert(exitToExitNum, new String[]
                { ordinalNumber, nextStreet });
                nextStreet = composedStr;
            }
            else if (param.nextStreetAlias != null && param.nextStreetAlias.trim().length() > 0)
            {
                nextStreet = nextStreet + " / " + param.nextStreetAlias;
            }
        }
        nextStreetName = nextStreet;
        calculatePercentAtTurn(param.distanceToTurn);

        StringConverter converter = ResourceManager.getInstance().getStringConverter();

        // Because distance-to-turn information has different font sizes for its digit part and unit part,
        // we need to split them and display those two parts in two separate labels.
        String distToTurnString = converter.convertDistanceMeterToMile(param.distanceToTurn, systemUnits);
        StringBuffer digit = new StringBuffer();

        if (distToTurnString != null)
        {
            for (int i = 0; i < distToTurnString.length(); i++)
            {
                char c = distToTurnString.charAt(i);
                if (Character.isDigit(c) || c == '.')
                {
                    digit.append(c);
                }
                else
                {
                    distToTurnUnit = distToTurnString.substring(i);
                    break;
                }
            }
        }

        getDistEtaString(param, systemUnits);
        distToTurnDigit = digit.toString();

        this.requestPaint();
    }

    private void calculatePercentAtTurn(int distanceToTurn)
    {
        int systemUnits = 1; // feet
        distanceToTurn *= StringConverter.DEG2SHORT[systemUnits];
        distanceToTurn = distanceToTurn >> DataUtil.SHIFT;
        int startDist = 500;
        int mixDist = 5;
        if (distanceToTurn <= 500 && distanceToTurn > mixDist)
        {
            percent = 100 - distanceToTurn * 100 / startDist;
        }
        else if (distanceToTurn <= mixDist)
        {
            percent = 100;
        }
        else
        {
            percent = 0;
        }
    }

    private void getDistEtaString(NavParameter param, int systemUnits)
    {
        StringConverter converter = ResourceManager.getInstance().getStringConverter();

        distance = converter.convertDistanceMeterToMile(param.totalToDest, systemUnits);
        arrival = converter.convertTime(param.eta);
    }

    /**
     * Step 0~9 arrival -> distance
     * 
     * Step 10~69 wait
     * 
     * Step 70~79 distance -> arrival
     * 
     * Step 80~149 wait
     * 
     */
    private int timerStep = 10;

    private int getArrivalOffset(int clipWidth)
    {
        if (timerStep < 10)
        {
            // return timerStep * clipWidth / 10;
            return timerStep * clipWidth / 5; // div 5 while not 10, the moving speed will be faster
        }
        else if (timerStep < 70)
        {
            return clipWidth;
        }
        else if (timerStep < 80)
        {
            return (timerStep % 10) * clipWidth / 10 - clipWidth;
        }
        else
        {
            return 0;
        }
    }

    private int getDistanceOffset(int clipWidth)
    {
        if (timerStep < 10)
        {
            return timerStep * clipWidth / 10 - clipWidth;
        }
        else if (timerStep < 70)
        {
            return 0;
        }
        else if (timerStep < 80)
        {
            // return (timerStep % 10) * clipWidth / 10;
            return (timerStep % 10) * clipWidth / 5; // div 5 while not 10, the moving speed will be faster
        }
        else
        {
            return clipWidth;
        }
    }

    private int getNextStreetOffset(int textWidth)
    {
        if (waitCount >= WAIT_COUNT)
        {
            if (!isScrollingBack)
            {
                if (textWidth + OVER_SCROLL > scrollX)
                {
                    scrollX += SCROLL_STEP;
                }
                else
                {
                    isScrollingBack = true;
                    waitCount = 0;
                }
            }
            else
            {
                if (scrollX + OVER_SCROLL > 0)
                {
                    scrollX -= SCROLL_STEP;
                }
                else
                {
                    isScrollingBack = false;
                    waitCount = 0;
                }
            }
        }
        else
        {
            waitCount++;
        }
        return scrollX;
    }

    protected boolean handleUiEvent(TnUiEvent tnUiEvent)
    {
        switch (tnUiEvent.getType())
        {
            case TnUiEvent.TYPE_TOUCH_EVENT:
            {
                TnMotionEvent event = tnUiEvent.getMotionEvent();
                switch (event.getAction())
                {
                    case TnMotionEvent.ACTION_UP:
                    {
                        TnUiEvent newEvent = new TnUiEvent(TnUiEvent.TYPE_COMMAND_EVENT, this);
                        TnCommandEvent commandEvent = new TnCommandEvent(nextStreetCmdId);
                        newEvent.setCommandEvent(commandEvent);
                        return commandListener.handleUiEvent(newEvent);
                    }
                }
                break;
            }
            case TnUiEvent.TYPE_PRIVATE_EVENT:
            {
                if (tnUiEvent.getPrivateEvent().getAction() == TnPrivateEvent.ACTION_TIMER)
                {
                    timerStep++;
                    timerStep %= 150;
                    if (needScroll)
                    {
                        this.requestPaint();
                    }
                    else if (timerStep > 0 && timerStep <= 10)
                    {
                        this.requestPaint();
                    }
                    else if (timerStep > 70 && timerStep <= 80)
                    {
                        this.requestPaint();
                    }
                }
                break;
            }
        }

        return super.handleUiEvent(tnUiEvent);
    }

    public void setNextStreetCmdId(int command, ITnUiEventListener commandListener)
    {
        nextStreetCmdId = command;
        this.commandListener = commandListener;
    }

    protected void handleUiTimerReceiver()
    {
        // override parent implementation.
    }

    protected void onDisplay()
    {
        TnUiTimer.getInstance().addReceiver(this, 80);
        super.onDisplay();
    }

    protected void onUndisplay()
    {
        TnUiTimer.getInstance().removeReceiver(this);
        super.onUndisplay();
    }
}
