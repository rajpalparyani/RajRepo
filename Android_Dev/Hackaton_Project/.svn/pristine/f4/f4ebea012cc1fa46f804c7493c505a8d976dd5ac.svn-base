/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * CitizenTrafficItem.java
 *
 */
package com.telenav.ui.citizen;

import com.telenav.tnui.core.TnCommandEvent;
import com.telenav.tnui.core.TnMotionEvent;
import com.telenav.tnui.core.TnUiArgs.TnUiArgAdapter;
import com.telenav.tnui.core.TnUiEvent;
import com.telenav.tnui.graphics.AbstractTnFont;
import com.telenav.tnui.graphics.AbstractTnGraphics;
import com.telenav.tnui.graphics.AbstractTnImage;
import com.telenav.tnui.graphics.TnColor;
import com.telenav.tnui.graphics.TnRect;
import com.telenav.ui.ImageDecorator;
import com.telenav.ui.NinePatchImageDecorator;
import com.telenav.ui.UiStyleManager;
import com.telenav.ui.frogui.widget.FrogButton;

/**
 *@author yning (yning@telenav.cn)
 *@date 2010-12-02
 */
public class CitizenTrafficItem extends CitizenSummaryItem
{
    protected String streetLength;
    protected String streetName;
    protected String speed;
    protected int speedColor;
    protected boolean isNA;
    protected TnUiArgAdapter iconSizeAdapter;
    protected TnUiArgAdapter firstPartWidthAdapter;
    protected TnUiArgAdapter firstPartLeftPaddingAdapter;
    protected TnUiArgAdapter secondPartPaddingAdapter;
    protected FrogButton triangleButton;
    protected int firstCellCommand = -1;
    protected int secondCellCommand = -1;
    protected TnRect firstPartRect = null;
    protected TnRect secondPartRect = null;
    
    public CitizenTrafficItem(int id, String streetLength, String streetName, String speed, int speedColor, boolean isNA, TnUiArgAdapter iconSize, TnUiArgAdapter firstPartWidth, TnUiArgAdapter firstPartLeftPadding, TnUiArgAdapter secondPartPadding, FrogButton triangleButton)
    {
        super(id);
        this.streetLength = streetLength;
        this.streetName = streetName;
        this.speed = speed;
        this.speedColor = speedColor;
        this.isNA = isNA;
        this.iconSizeAdapter = iconSize;
        this.firstPartWidthAdapter = firstPartWidth;
        this.firstPartLeftPaddingAdapter = firstPartLeftPadding;
        this.secondPartPaddingAdapter = secondPartPadding;
        this.triangleButton = triangleButton;
    }

    public void setCommand(int firstCellCommand, int secondCellCommand)
    {
        this.firstCellCommand = firstCellCommand;
        this.secondCellCommand = secondCellCommand;
    }
    
    protected void paint(AbstractTnGraphics g)
    {
        AbstractTnFont oldFont = g.getFont();
        int oldColor = g.getColor();

        drawSpeedRect(g);
        drawStreetRect(g);
        
        g.setFont(oldFont);
        g.setColor(oldColor);

    }
    
    protected void drawSpeedRect(AbstractTnGraphics g)
    {
        int oldColor = g.getColor();
        AbstractTnFont oldFont = g.getFont();
        
        AbstractTnFont speedFont = UiStyleManager.getInstance().getFont(UiStyleManager.FONT_SUMMARY_SPEED_DIGIT);
        AbstractTnFont speedUnitFont = UiStyleManager.getInstance().getFont(UiStyleManager.FONT_SUMMARY_SPEED_UNIT);
        AbstractTnFont speedNAFont = UiStyleManager.getInstance().getFont(UiStyleManager.FONT_SUMMARY_SPEED_NA);
        g.setColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_WH));
        
        int firstPartWidth = firstPartWidthAdapter.getInt();
        int firstPartLeftPadding = firstPartLeftPaddingAdapter.getInt();
        int iconSize = iconSizeAdapter.getInt();
        
        TnUiArgAdapter currentColorImageId = null;
        //FIXME: should not use TnColor here.
        if (speedColor == TnColor.RED)
        {
            currentColorImageId = NinePatchImageDecorator.SUMMARY_TRAFFIC_RED;
        }
        else if (speedColor == UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_OR))
        {
            currentColorImageId = NinePatchImageDecorator.SUMMARY_TRAFFIC_ORANGE;
            g.setColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_DA_GR));
        }
        else if (speedColor == TnColor.YELLOW)
        {
            currentColorImageId = NinePatchImageDecorator.SUMMARY_TRAFFIC_YELLOW;
            g.setColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_DA_GR));
        }
        else
        {
            currentColorImageId = NinePatchImageDecorator.SUMMARY_TRAFFIC_GREEN;
        }

        AbstractTnImage speedBgImage = null;
        
        if (isNA)
        {
            currentColorImageId = NinePatchImageDecorator.SUMMARY_TRAFFIC_GRAY;
        }
        
        speedBgImage = currentColorImageId.getImage();
        
        int imageX = -1;
        int imageY = -1;
        int shadowHeight = 5;
        int rightShadowWidth = 5;
        if (speedBgImage != null)
        {
            speedBgImage.setWidth(iconSize);
            speedBgImage.setHeight(iconSize);
            imageX = firstPartLeftPadding;
            imageY = (getPreferredHeight() - (iconSize - shadowHeight)) / 2;
            g.drawImage(speedBgImage, imageX, imageY, AbstractTnGraphics.LEFT | AbstractTnGraphics.TOP);
        }
        
        if(isNA)
        {
            g.setFont(speedNAFont);
            
            int stringX = imageX + (iconSize - rightShadowWidth - speedNAFont.stringWidth(speed)) / 2;
            g.drawString(speed, stringX, getPreferredHeight() / 2, AbstractTnGraphics.LEFT | AbstractTnGraphics.VCENTER);
        }
        else
        {
            String[] speedAndUnit = splitNumAndUnit(speed);
            String speedDigit = speedAndUnit[0];
            String speedUnit = speedAndUnit[1];
            g.setFont(speedFont);
            int stringX;
            if(imageX != -1)
            {
                stringX = imageX + (iconSize - rightShadowWidth - speedFont.stringWidth(speedDigit)) / 2;
            }
            else
            {
                stringX = (firstPartWidth - speedFont.stringWidth(speedDigit)) / 2;
            }
            int speedY;
            int stringYOffset = (speedBgImage.getHeight() - shadowHeight - speedFont.getHeight() - speedUnitFont.getHeight()) / 2 + 2;
            if(imageY != -1)
            {
                speedY = imageY + stringYOffset;
            }
            else
            {
                speedY = (getPreferredHeight() - speedFont.getHeight() - speedUnitFont.getHeight()) / 2;
            }
            g.drawString(speedDigit, stringX, speedY, AbstractTnGraphics.LEFT | AbstractTnGraphics.TOP);
            g.setFont(speedUnitFont);
            
            speedUnit = speedUnit.toUpperCase();
            if(imageX != -1)
            {
                stringX = imageX + (iconSize - rightShadowWidth - speedUnitFont.stringWidth(speedUnit)) / 2 - 1;
            }
            else
            {
                stringX = (firstPartWidth - speedUnitFont.stringWidth(speedUnit)) / 2;
            }
            
            int unitY = imageY + speedBgImage.getHeight() - shadowHeight - stringYOffset;
            g.drawString(speedUnit, stringX, unitY, AbstractTnGraphics.LEFT | AbstractTnGraphics.BOTTOM);
        }
        
        //draw speaker icon.
        if(triangleButton != null)
        {
            AbstractTnImage speakerIcon = ImageDecorator.IMG_SUMMARY_ITEM_SOUND_ICON_UNFOCUSED.getImage();
            if(speakerIcon != null)
            {
                int maxWidth = firstPartWidth - (firstPartLeftPadding + iconSize);
                int speakerX = firstPartLeftPadding + iconSize + (maxWidth - speakerIcon.getWidth()) / 2;
                int speakerY = (getPreferredHeight() - speakerIcon.getHeight()) / 2;
                if(speakerX < firstPartLeftPadding + iconSize)
                {
                    speakerX = firstPartLeftPadding + iconSize;
                }
                g.drawImage(speakerIcon, speakerX, speakerY, AbstractTnGraphics.TOP | AbstractTnGraphics.LEFT);
            }
        }
        
        g.setStrokeWidth(0);
        g.setColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_LI_GR));
        g.drawLine(firstPartWidth, 0, firstPartWidth, getPreferredHeight());
        
        g.setColor(oldColor);
        g.setFont(oldFont);
        
        firstPartRect = new TnRect(0, 0, firstPartWidth, getPreferredHeight());
    }
    
    protected void drawStreetRect(AbstractTnGraphics g)
    {
        int oldColor = g.getColor();
        AbstractTnFont oldFont = g.getFont();
        
        AbstractTnFont streetNameFont = UiStyleManager.getInstance().getFont(UiStyleManager.FONT_TRAFFIC_SUMMARY_STREET_NAME);
        AbstractTnFont streetLengthFont = UiStyleManager.getInstance().getFont(UiStyleManager.FONT_TRAFFIC_SUMMARY_STREET_LENGTH);
        AbstractTnFont incidentNumFont = UiStyleManager.getInstance().getFont(UiStyleManager.FONT_TRAFFIC_INCIDENT_NUMBER);
        if(isFocused())
        {
            g.setColor(focusColor);
        }
        else
        {
            g.setColor(unfocusColor);
        }
        
        int prefWidth = getPreferredWidth();
        int prefHeight = getPreferredHeight();
        int verticalPadding = (prefHeight - streetNameFont.getHeight() - streetLengthFont.getHeight()) / 2;
        int horizontalPadding = secondPartPaddingAdapter.getInt();
        int speedAreaWidth = firstPartWidthAdapter.getInt();
        int carWidth = ImageDecorator.SUMMARY_CURRENT_SEGMENT_UNFOCUSED.getImage().getWidth();
        int incidentImageWidth = ImageDecorator.IMG_TRAFFIC_ALERT_SMALL_RED_EMPTY_ICON_UNFOCUSED.getImage().getWidth();
        int x = speedAreaWidth + horizontalPadding;
        int y = verticalPadding;
        int maxLength = prefWidth - speedAreaWidth - horizontalPadding * 2;
        AbstractTnImage incidentImage = null;
        if(triangleButton != null)
        {
            incidentImage = triangleButton.getUnfocusedIcon();
        }
        if(isCarShown && incidentImage != null)
        {
        	maxLength -= carWidth + 2 * horizontalPadding + Math.max(carWidth, incidentImageWidth);
        }
        else if(isCarShown || incidentImage != null)
        {
        	maxLength -= Math.max(carWidth, incidentImageWidth) + horizontalPadding;
        }       
        
        g.setFont(streetNameFont);
        String displayedName = getLabelForPaint(streetName, maxLength, streetNameFont, false);
        g.drawString(displayedName, x, y, AbstractTnGraphics.LEFT | AbstractTnGraphics.TOP);
        
        g.setFont(streetLengthFont);
        String displayedLengthStr = getLabelForPaint(streetLength, maxLength, streetLengthFont, false);
        g.drawString(displayedLengthStr, x, prefHeight - verticalPadding, AbstractTnGraphics.LEFT | AbstractTnGraphics.BOTTOM);
        
        if(incidentImage != null)
        {
            int incidentX = getPreferredWidth() - Math.max(carWidth, incidentImageWidth) / 2 - horizontalPadding;
            int incidentY = (getPreferredHeight() - incidentImage.getHeight()) / 2;
            g.drawImage(incidentImage, incidentX, incidentY, AbstractTnGraphics.TOP | AbstractTnGraphics.HCENTER);
            
            String incidentNumStr = triangleButton.getText();
            int incidentColor = triangleButton.getForegroundColor(false);
            if(incidentNumStr != null && incidentNumStr.trim().length() > 0)
            {
                int numX = incidentX;
                int numY = incidentY + (incidentImage.getHeight() - incidentNumFont.getHeight()) / 2;
                
                //to make it really center
                numX += 1;
                numY += 2;
                
                g.setFont(incidentNumFont);
                g.setColor(incidentColor);
                g.drawString(incidentNumStr, numX, numY, AbstractTnGraphics.TOP | AbstractTnGraphics.HCENTER);
            }
        }
            
        if(isCarShown)
        {
            int carX = prefWidth - horizontalPadding - Math.max(carWidth, incidentImageWidth) / 2;
            if(incidentImage != null)
            {
                carX = prefWidth - 2 * horizontalPadding - Math.max(carWidth, incidentImageWidth) - carWidth / 2;
            }
            int carY = prefHeight / 2;
            g.drawImage(ImageDecorator.SUMMARY_CURRENT_SEGMENT_UNFOCUSED.getImage(), carX, carY, AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);
        }
        
        g.setColor(oldColor);
        g.setFont(oldFont);
        
        secondPartRect = new TnRect(speedAreaWidth, 0, getPreferredWidth(), getPreferredHeight());
    }
    
    private String[] splitNumAndUnit(String target)
    {
        if(target == null)
        {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        String unit = "";
        for(int i = 0; i < target.length(); i++)
        {
            char c = target.charAt(i);
            if(Character.isDigit(c) || c == '.')
            {
                sb.append(c);
            }
            else
            {
                unit = target.substring(i);
                break;
            }
        }
        return new String[]{sb.toString(), unit};
    }
    
    protected void initDefaultStyle()
    {
        // TODO Auto-generated method stub
        
    }
    
    protected boolean handleUiEvent(TnUiEvent tnUiEvent)
    {
        int type = tnUiEvent.getType();
        switch(type)
        {
            case TnUiEvent.TYPE_TOUCH_EVENT:
            {
                TnMotionEvent motionEvent = tnUiEvent.getMotionEvent();
                int action = motionEvent.getAction();
                if(action == TnMotionEvent.ACTION_UP)
                {
                    int commandId = -1;
                    int x = motionEvent.getX();
                    int y = motionEvent.getY();
                    
                    if(firstPartRect != null && firstPartRect.contains(x, y))
                    {
                        commandId = firstCellCommand;
                    }
                    else if(secondPartRect != null && secondPartRect.contains(x, y))
                    {
                        commandId = secondCellCommand;
                    }
                    
                    if(commandId != -1 && commandListener != null)
                    {
                        TnUiEvent newEvent = new TnUiEvent(TnUiEvent.TYPE_COMMAND_EVENT, this);
                        TnCommandEvent commandEvent = new TnCommandEvent(commandId);
                        newEvent.setCommandEvent(commandEvent);
                        commandListener.handleUiEvent(newEvent);
                    }
                }
                
                break;
            }
        }
        return false;
    }
}
