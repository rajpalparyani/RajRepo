/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * TurnMapInfoContainer.java
 *
 */
package com.telenav.module.nav.turnmap;

import com.telenav.module.AppConfigHelper;
import com.telenav.mvc.AbstractCommonUiDecorator;
import com.telenav.mvc.ICommonConstants;
import com.telenav.res.IStringNav;
import com.telenav.res.ResourceManager;
import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.core.AbstractTnUiHelper;
import com.telenav.tnui.core.TnCommandEvent;
import com.telenav.tnui.core.TnMotionEvent;
import com.telenav.tnui.core.TnUiArgs.TnUiArgAdapter;
import com.telenav.tnui.core.TnUiEvent;
import com.telenav.tnui.graphics.AbstractTnFont;
import com.telenav.tnui.graphics.AbstractTnGraphics;
import com.telenav.tnui.graphics.AbstractTnImage;
import com.telenav.tnui.graphics.TnRect;
import com.telenav.ui.ImageDecorator;
import com.telenav.ui.UiFactory;
import com.telenav.ui.UiStyleManager;
import com.telenav.ui.frogui.text.FrogTextHelper;
import com.telenav.ui.tnui.text.TnTextLine;
import com.telenav.ui.tnui.text.TnTextParser;
import com.telenav.util.PrimitiveTypeCache;


/**
 *@author yning
 *@date 2011-1-30
 */
public class TurnMapInfoComponent extends AbstractTnComponent
{
    private TnRect leftArrowRect;
    private TnRect rightArrowRect;

    private AbstractTnFont infoFontPlain;
    private AbstractTnFont infoFontBold;

    private int allStepNum;
    private int currentStep;
    private String turnInfo;
    private String distToTurn;
    private String streetName;
    private int distToTurnValue;
    
    private int mWidth = 0;
    private int mHeight = 0;
    
    private boolean isLeftArrowOn = false;
    private boolean isRightArrowOn = false;
    
    private static final int PREV = 0;
    private static final int NEXT = 1;
    
    private int prevIconCmd = ICommonConstants.CMD_NONE;
    private int nextIconCmd = ICommonConstants.CMD_NONE;
    protected UiDecorator uiDecorator = new UiDecorator();
    
    public TurnMapInfoComponent(int id)
    {
        super(id);
        
        init();
    }
    
    private void clearFocus()
    {
        isLeftArrowOn = false;
        isRightArrowOn = false;
    }

    private void init()
    {
        infoFontPlain = UiStyleManager.getInstance().getFont(UiStyleManager.FONT_MAP_NAV_TURN_INFO);
        infoFontBold = UiStyleManager.getInstance().getFont(UiStyleManager.FONT_MAP_NAV_TURN_INFO_BOLD);
    }
    
    private void initSize()
    {
        final int width = getWidth();
        final int height = getHeight();
        
        if( width!= mWidth || height != mHeight)
        {
            mWidth = width;
            mHeight = height;
            
            int arrowPadding = uiDecorator.TURN_MAP_INFO_ARROW_PADDING.getInt();
            leftArrowRect = new TnRect();
            leftArrowRect.left = 0;
            leftArrowRect.top = 0;
            AbstractTnImage leftImage = ImageDecorator.IMG_MAP_POI_BAR_LEFT_ARROW_UNFOCUSED.getImage();
            leftArrowRect.right = leftArrowRect.left + leftImage.getWidth() + arrowPadding * 2;
            leftArrowRect.bottom = leftArrowRect.top + height;

            rightArrowRect = new TnRect();
            rightArrowRect.left = width - leftArrowRect.width();
            rightArrowRect.top = 0;
            rightArrowRect.right = width;
            rightArrowRect.bottom = leftArrowRect.bottom;
        }
    }

    protected void paint(AbstractTnGraphics g)
    {
        AbstractTnFont oldFont = g.getFont();
        int oldColor = g.getColor();
        initSize();
        drawTurnArrow(g);
        drawInfoText(g);

        g.setColor(oldColor);
        g.setFont(oldFont);
    }

    private void drawInfoText(AbstractTnGraphics g)
    {
        boolean isDistToTurnNeeded = distToTurnValue > 0;
        g.setColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_WH));
        int width = getWidth();
        int height = getHeight();
        int orientation = ((AbstractTnUiHelper)AbstractTnUiHelper.getInstance()).getOrientation();
        int totalFontHeight = 0;
        if(orientation == AbstractTnUiHelper.ORIENTATION_PORTRAIT || AppConfigHelper.isTabletSize())
        {
            if(!isDistToTurnNeeded)
            {
                totalFontHeight = infoFontBold.getHeight() * 2 + uiDecorator.TURN_MAP_INFO_TEXT_PADDING.getInt();
            }
            else
            {
                totalFontHeight = infoFontBold.getHeight() * 3 + (uiDecorator.TURN_MAP_INFO_TEXT_PADDING.getInt() * 2);
            }
        }
        else
        {
            totalFontHeight = infoFontBold.getHeight();
        }
        
        int textYOffset = (height - totalFontHeight) >> 1;
        if(textYOffset < 0)
        {
            textYOffset = 0;
        }
        
        int maxTextWidth = width - leftArrowRect.width() - rightArrowRect.width();
        int textAreaX = leftArrowRect.right;
        if(orientation == AbstractTnUiHelper.ORIENTATION_PORTRAIT || AppConfigHelper.isTabletSize())
        {
            //draw dist to turn.
            if(isDistToTurnNeeded)
            {
                if (distToTurn != null)
                {
                    TnTextLine textLine = TnTextParser.parse(distToTurn);
                    int xPos = textAreaX + (maxTextWidth - FrogTextHelper.getWidth(textLine, infoFontPlain, infoFontBold)) / 2;
                    xPos = xPos < textAreaX ? textAreaX : xPos;
                    int yPos = textYOffset;
                    yPos = yPos < 0 ? 0 : yPos;
                    
                    FrogTextHelper.paint(g, xPos, yPos, textLine, infoFontPlain, infoFontBold, maxTextWidth, true);
                }
            }
            
            //draw turn info.
            if(turnInfo != null)
            {
                TnTextLine textLine = TnTextParser.parse(turnInfo);
                int xPos = textAreaX + (maxTextWidth - FrogTextHelper.getWidth(textLine, infoFontPlain, infoFontBold)) / 2;
                xPos = xPos < textAreaX ? textAreaX : xPos;
                int yPos;
                if(!isDistToTurnNeeded)
                {
                    yPos = textYOffset;
                }
                else
                {
                    yPos = textYOffset + infoFontBold.getHeight() + uiDecorator.TURN_MAP_INFO_TEXT_PADDING.getInt();
                }
                yPos = yPos < 0 ? 0 : yPos;
                
                FrogTextHelper.paint(g, xPos, yPos, textLine, infoFontPlain, infoFontBold, maxTextWidth, true);
            }
            
            //draw street name.
            
            if(streetName != null)
            {
                TnTextLine textLine = TnTextParser.parse(streetName);
                int xPos = textAreaX + (maxTextWidth - FrogTextHelper.getWidth(textLine, infoFontPlain, infoFontBold)) / 2;
                xPos = xPos < textAreaX ? textAreaX : xPos;
                int yPos;
                if(!isDistToTurnNeeded)
                {
                    yPos = textYOffset + infoFontBold.getHeight() + uiDecorator.TURN_MAP_INFO_TEXT_PADDING.getInt();
                }
                else
                {
                    yPos = textYOffset + infoFontBold.getHeight() * 2 + uiDecorator.TURN_MAP_INFO_TEXT_PADDING.getInt() * 2;
                }
                yPos = yPos < 0 ? 0 : yPos;
                
                FrogTextHelper.paint(g, xPos, yPos, textLine, infoFontPlain, infoFontBold, maxTextWidth, true);
            }
        }
        else
        {
            StringBuffer info = new StringBuffer();
            if(distToTurn != null && distToTurn.length() > 0 && isDistToTurnNeeded)//don't draw dist to turn if it's 0.
            {
                info.append(distToTurn);
            }
            String and = ResourceManager.getInstance().getCurrentBundle().getString(IStringNav.RES_AND, IStringNav.FAMILY_NAV);
            if(turnInfo != null && turnInfo.length() > 0)
            {
                if(info.length() > 0)
                {
                    info.append(" ").append(and).append(" ");
                }
                info.append(turnInfo);
            }
            if(streetName != null && streetName.length() > 0)
            {
                info.append(" ").append(streetName);
            }
            
            TnTextLine textLine = TnTextParser.parse(info.toString());
            int xPos = textAreaX + (maxTextWidth - FrogTextHelper.getWidth(textLine, infoFontPlain, infoFontBold)) / 2;
            xPos = xPos < textAreaX ? textAreaX : xPos;
            int yPos = textYOffset;
            yPos = yPos < 0 ? 0 : yPos;
            
            FrogTextHelper.paint(g, xPos, yPos, textLine, infoFontPlain, infoFontBold, maxTextWidth, true);
        }
    }
    
    private void drawTurnArrow(AbstractTnGraphics g)
    {
        AbstractTnImage leftArrow = null;
        AbstractTnImage rightArrow = null;

        if (currentStep != 0)
        {
            if(this.isLeftArrowOn)
            {
                leftArrow = ImageDecorator.IMG_MAP_POI_BAR_LEFT_ARROW_FOCUSED.getImage();
            }
            else
            {
                leftArrow = ImageDecorator.IMG_MAP_POI_BAR_LEFT_ARROW_UNFOCUSED.getImage();
            }
        }
        else
        {
            leftArrow = ImageDecorator.IMG_MAP_POI_BAR_LEFT_ARROW_DISABLED.getImage();
        }
        if (currentStep != allStepNum - 1)
        {
            if(this.isRightArrowOn)
            {
                rightArrow = ImageDecorator.IMG_MAP_POI_BAR_RIGHT_ARROW_FOCUSED.getImage();
            }
            else
            {
                rightArrow = ImageDecorator.IMG_MAP_POI_BAR_RIGHT_ARROW_UNFOCUSED.getImage();
            }
        }
        else
        {
            rightArrow = ImageDecorator.IMG_MAP_POI_BAR_RIGHT_ARROW_DISABLED.getImage();
        }

        if (leftArrow != null)
        {
            g.translate(leftArrowRect.left, leftArrowRect.top);
            g.pushClip(0, 0, leftArrowRect.width(), leftArrowRect.height());

            int leftArrowX = (leftArrowRect.width() - leftArrow.getWidth()) / 2;
            int leftArrowY = (leftArrowRect.height() - leftArrow.getHeight()) / 2;
            g.drawImage(leftArrow, leftArrowX, leftArrowY, AbstractTnGraphics.LEFT | AbstractTnGraphics.TOP);

            g.popClip();
            g.translate(-leftArrowRect.left, -leftArrowRect.top);
        }
        if (rightArrow != null)
        {
            g.translate(rightArrowRect.left, rightArrowRect.top);
            g.pushClip(0, 0, rightArrowRect.width(), rightArrowRect.height());

            int rightArrowX = (rightArrowRect.width() - rightArrow.getWidth()) / 2;
            int rightArrowY = (rightArrowRect.height() - rightArrow.getHeight()) / 2;
            g.drawImage(rightArrow, rightArrowX, rightArrowY, AbstractTnGraphics.LEFT | AbstractTnGraphics.TOP);

            g.popClip();
            g.translate(-rightArrowRect.left, -rightArrowRect.top);
        }
    }
    
    private boolean inRect(int x, int y, TnRect rect)
    {
        return x >= rect.left && x < rect.right &&
                    y >= rect.top && y < rect.bottom;
    }
    
    protected boolean handleUiEvent(TnUiEvent tnUiEvent)
    {
        boolean isHandled = false;
        int type = tnUiEvent.getType();
        if(type == TnUiEvent.TYPE_TOUCH_EVENT)
        {
            isHandled = onTouchEvent(tnUiEvent.getMotionEvent());
        }
        if(!isHandled)
        {
            isHandled = super.handleUiEvent(tnUiEvent);
        }
        return isHandled;
    }
    
    public boolean onTouchEvent(TnMotionEvent event)
    {
        int x = (int) event.getX();
        int y = (int) event.getY();
        boolean isHandled = false;
        if(event.getAction() == TnMotionEvent.ACTION_DOWN)
        {
            if(currentStep != 0&& inRect(x, y, leftArrowRect))
            {
                isLeftArrowOn = true;
                isHandled = true;
            }
            else if(currentStep != allStepNum - 1 && inRect(x, y, rightArrowRect))
            {
                isRightArrowOn = true;
                isHandled = true;
            }
            
            if(isHandled)
            {
                this.requestPaint();
            }
        }
        else if(event.getAction() == TnMotionEvent.ACTION_UP)
        {
            this.clearFocus();
            if(currentStep != 0 && inRect(x, y, leftArrowRect))
            {
                changeRoute(PREV);
                isHandled = true;
            }
            else if(currentStep != allStepNum - 1 && inRect(x, y, rightArrowRect))
            {
                changeRoute(NEXT);
                isHandled = true;
            }
            this.requestPaint();
        }
        
        return isHandled;
    }

    private void changeRoute(int type)
    {
        int commandId;
        if(type == PREV)
        {
            commandId = prevIconCmd;
        }
        else
        {
            commandId = nextIconCmd;
        }
        TnUiEvent newEvent = new TnUiEvent(TnUiEvent.TYPE_COMMAND_EVENT, this);
        TnCommandEvent commandEvent = new TnCommandEvent(commandId);
        newEvent.setCommandEvent(commandEvent);
        if(commandListener != null)
        {
            commandListener.handleUiEvent(newEvent);
        }
    }

    public void updateValues(int allStepNum, int currentStep, String turnInfo, String distToTurn, String streetName, int distToTurnValue)
    {
        this.allStepNum = allStepNum;
        this.currentStep = currentStep;
        this.turnInfo = turnInfo;
        this.distToTurn = distToTurn;
        this.streetName = streetName;
        this.distToTurnValue = distToTurnValue;
    }

    public void setIconCommand(int prevIconCmd, int nextIconCmd)
    {
        this.prevIconCmd = prevIconCmd;
        this.nextIconCmd = nextIconCmd;
    }
    
    static class UiDecorator extends AbstractCommonUiDecorator
    {
        private final static int ID_TURN_MAP_INFO_ARROW_PADDING = 1;
        private final static int ID_TURN_MAP_INFO_TEXT_PADDING = 2;
        public TnUiArgAdapter TURN_MAP_INFO_ARROW_PADDING = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_TURN_MAP_INFO_ARROW_PADDING), this);
        public TnUiArgAdapter TURN_MAP_INFO_TEXT_PADDING = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_TURN_MAP_INFO_TEXT_PADDING), this);
        protected Object decorateDelegate(TnUiArgAdapter args)
        {
            int key = ((Integer)args.getKey()).intValue();
            switch(key)
            {
                case ID_TURN_MAP_INFO_ARROW_PADDING:
                {
                    int height = AppConfigHelper.getMaxDisplaySize() * 17 / 1000;
                    return PrimitiveTypeCache.valueOf(height);
                }
                case ID_TURN_MAP_INFO_TEXT_PADDING:
                {
                    int height = AppConfigHelper.getMaxDisplaySize() * 1 / 100;
                    
                    if (AppConfigHelper.isTabletSize())
                    {
                        height = UiFactory.getInstance().getCharWidth() / 5;
                    }
                    
                    return PrimitiveTypeCache.valueOf(height);
                }
            }
            return null;
        }
        
    }
}
