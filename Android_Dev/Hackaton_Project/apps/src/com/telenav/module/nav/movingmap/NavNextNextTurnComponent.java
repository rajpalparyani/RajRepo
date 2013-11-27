/**
 *
 * Copyright 2013 TeleNav, Inc. All rights reserved.
 * NavNextNextTurnComponent.java
 *
 */
package com.telenav.module.nav.movingmap;

import com.telenav.module.nav.NavIconProvider;
import com.telenav.module.nav.NavParameter;
import com.telenav.res.IStringNav;
import com.telenav.res.ResourceManager;
import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.core.TnPrivateEvent;
import com.telenav.tnui.core.TnUiArgs;
import com.telenav.tnui.core.TnUiArgs.TnUiArgAdapter;
import com.telenav.tnui.core.TnUiEvent;
import com.telenav.tnui.core.TnUiTimer;
import com.telenav.tnui.graphics.AbstractTnFont;
import com.telenav.tnui.graphics.AbstractTnGraphics;
import com.telenav.tnui.graphics.AbstractTnImage;
import com.telenav.ui.NinePatchImageDecorator;
import com.telenav.ui.UiStyleManager;

/**
 * @author wchshao
 * @date Mar 25, 2013
 */
public class NavNextNextTurnComponent extends AbstractTnComponent
{
    private int width;

    private int height;
    private MovingMapUiDecorator uiDecorator;

    private AbstractTnFont streetFont;
    private AbstractTnFont tightStreetFont;
    private int horizontalGap;
    
    private String[] ordinalNumbers;
    private AbstractTnImage turnImage;
    private String nextStreetName;

    private boolean needScroll;

    public NavNextNextTurnComponent(int id)
    {
        super(id);
        uiDecorator = new MovingMapUiDecorator();

        this.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, uiDecorator.NEXT_NEXT_TURN_WIDTH);
        this.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, uiDecorator.NEXT_NEXT_TURN_HEIGHT);
        
        int bgColor = UiStyleManager.getInstance().getColor(UiStyleManager.BG_COLOR_TIGHT_TURN);
        this.setBackgroundColor(bgColor);

        streetFont = UiStyleManager.getInstance().getFont(UiStyleManager.FONT_NAV_STREET_NAME);
        tightStreetFont = UiStyleManager.getInstance().getFont(UiStyleManager.FONT_TIGHT_TURN_NAME);
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

    @Override
    protected void paint(AbstractTnGraphics graphics)
    {
        width = this.getWidth();
        height = this.getHeight();

        // draw trun icon
        int infoX = drawTurnIcon(graphics);

        // draw the nav dashboard info
        graphics.setColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_TIGHT_STREET_NAME));
        drawNextNextStreet(infoX, graphics);
    }
    
    private void drawNextNextStreet(int infoX, AbstractTnGraphics g)
    {
        infoX += horizontalGap;
        
        // draw street name, if the length is less than the draw area, then make it be scrollable.
        int streetNameWidth = width - infoX - horizontalGap;
        g.pushClip(infoX, 0, streetNameWidth, height);
        if (tightStreetFont.stringWidth(nextStreetName) > streetNameWidth)
        {
            infoX -= (getNextStreetOffset(tightStreetFont.stringWidth(nextStreetName) - streetNameWidth));
            this.needScroll = true;
        }
        else
        {
            this.needScroll = false;
        }

        g.setFont(tightStreetFont);
        g.drawString(nextStreetName, infoX, height / 2, AbstractTnGraphics.LEFT | AbstractTnGraphics.VCENTER);
        g.popClip();
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
    
    private int drawTurnIcon(AbstractTnGraphics g)
    {
        int leftPadding = uiDecorator.NAV_TITLE_LEFT_PADDING.getInt();
        int turnIconWidth = uiDecorator.TURN_ICON_WIDTH.getInt();
        int turnIconHeight = uiDecorator.NEXT_NEXT_TURN_HEIGHT.getInt();
        AbstractTnImage turnImageBg = NinePatchImageDecorator.NAV_TIGHT_TURN_BG.getImage();
        turnImageBg.setWidth(turnIconWidth);
        turnImageBg.setHeight(turnIconHeight);
        g.drawImage(turnImageBg, leftPadding + turnIconWidth / 2, height / 2, AbstractTnGraphics.HCENTER
                | AbstractTnGraphics.VCENTER);
        g.drawImage(turnImage, leftPadding + turnIconWidth / 2, height / 2, AbstractTnGraphics.HCENTER
                | AbstractTnGraphics.VCENTER);
        return leftPadding + turnIconWidth;
    }

    public void updateStatus(NavParameter navParam)
    {
        int turnType = navParam.nextNextTurnType;
        TnUiArgAdapter iconArg = NavIconProvider.getTightTurnIcon(turnType);
        turnImage = iconArg != null ? iconArg.getImage() : null;
        String nextStreet = NavIconProvider.getDestinationLocate(turnType);
        if (nextStreet == null)
        {
            nextStreet = navParam.nextNextSteetName;
            if (navParam.nextNextExitName != null && navParam.nextNextExitName.length() > 0)
            {
                String exitToExitName = ResourceManager.getInstance().getCurrentBundle()
                        .getString(IStringNav.RES_EXIT_TO_EXIT_NAME, IStringNav.FAMILY_NAV);
                String composedStr = ResourceManager.getInstance().getStringConverter().convert(exitToExitName, new String[]
                { navParam.nextNextExitName, nextStreet });
                nextStreet = composedStr;
            }
            else if (navParam.nextNextExitNumber != 0)
            {
                String ordinalNumber;
                if (ordinalNumbers != null && navParam.nextNextExitNumber > 0 && navParam.nextNextExitNumber - 1 < ordinalNumbers.length)
                {
                    ordinalNumber = ordinalNumbers[navParam.nextNextExitNumber - 1];
                }
                else
                {
                    ordinalNumber = navParam.nextNextExitNumber + "";
                }

                String exitToExitNum = ResourceManager.getInstance().getCurrentBundle()
                        .getString(IStringNav.RES_EXIT_TO_EXIT_NUMBER, IStringNav.FAMILY_NAV);
                String composedStr = ResourceManager.getInstance().getStringConverter().convert(exitToExitNum, new String[]
                { ordinalNumber, nextStreet });
                nextStreet = composedStr;
            }
        }
        nextStreetName = nextStreet;
        this.requestPaint();
    }

    protected boolean handleUiEvent(TnUiEvent tnUiEvent)
    {
        switch (tnUiEvent.getType())
        {
            case TnUiEvent.TYPE_PRIVATE_EVENT:
            {
                if (tnUiEvent.getPrivateEvent().getAction() == TnPrivateEvent.ACTION_TIMER)
                {
                    if (needScroll)
                    {
                        this.requestPaint();
                    }
                }
                break;
            }
        }

        return super.handleUiEvent(tnUiEvent);
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
