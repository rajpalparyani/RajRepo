/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * FrogTnAddressListItem.java
 *
 */
package com.telenav.ui.citizen;

import com.telenav.data.datatypes.address.Address;
import com.telenav.tnui.core.TnCommandEvent;
import com.telenav.tnui.core.TnMotionEvent;
import com.telenav.tnui.core.TnUiArgs;
import com.telenav.tnui.core.TnUiEvent;
import com.telenav.tnui.graphics.AbstractTnFont;
import com.telenav.tnui.graphics.AbstractTnGraphics;
import com.telenav.tnui.graphics.AbstractTnImage;
import com.telenav.tnui.graphics.TnColor;
import com.telenav.ui.frogui.text.FrogTextHelper;
import com.telenav.ui.frogui.widget.FrogListItem;
import com.telenav.ui.tnui.text.TnTextLine;
import com.telenav.ui.tnui.text.TnTextParser;

/**
 * @author jwchen (jwchen@telenav.cn)
 * @modifier wzhu (wzhu@telenav.cn)
 * @date 2010-9-7
 */
public class CitizenAddressListItem extends FrogListItem
{
    public static final int KEY_PREFER_RIGHT_BACKGROUND_SIZE = 10000;
    
    public static final int KEY_LEFT_ICON_FOCUS = 10001;
    
    public static final int KEY_LEFT_ICON_UNFOCUS = 10002;
    
    public static final int KEY_RIGHT_BTN_BG_FOCUS = 10003;
    
    public static final int KEY_RIGHT_BTN_BG_UNFOCUS = 10004;
    
    public static final int KEY_RIGHT_BTN_ICON_FOCUS = 10005;
    
    public static final int KEY_RIGHT_BTN_ICON_UNFOCUS = 10006;

    protected int focusedShareInfoColor = TnColor.WHITE;

    protected int blurShareInfoColor = TnColor.BLACK;
    
    protected int focusedTitleBadgeColor = TnColor.WHITE;

    protected int blurTitleBadgeColor = unfocusColor;
    
    protected int focusedTitleColor = TnColor.WHITE;

    protected int blurTitleColor = AbstractTnGraphics.getInstance().getColor();

    protected int[] gap = new int[2];

    protected AbstractTnFont shareInfoFont;

    protected TnTextLine titleTextLine;

    protected TnTextLine addressTextLine;

    protected TnTextLine shareInfoTextLine;
    
    protected TnTextLine titleBadgeTextLine;
    
    protected Address data;

    private int command;
    
    private boolean isRightIconPressed = false;

    /**
     * Constructor 
     * 
     * @param id
     */
    public CitizenAddressListItem(int id)
    {
        super(id);
    }

    /**
     * Create a AddressListItem with a existing item. <br>
     * <b>It will be realize immediately.</b>
     * 
     * @param item
     */
    protected CitizenAddressListItem(CitizenAddressListItem item)
    {
        this(item.getId());
        this.initItemData(item);
        this.mItem = item;
    }

    /**
     * Set title for addressListItem.
     * 
     * @param title
     */
    public void setTitle(String title)
    {
        if(title == null)
        {
            titleTextLine = null;
        }
        else if(title.equals(getTitle()))
            return;
        else
            titleTextLine = TnTextParser.parse(title);
    }

    /**
     * Get title of the addressListItem.
     * 
     * @return title
     */
    public String getTitle()
    {
        return titleTextLine == null ? null : titleTextLine.getText();
    }

    public void setTitleBadge(String titleBadge)
    {
        if(titleBadge == null)
        {
            titleBadgeTextLine = null;
        }
        else if (titleBadge.equals(getTitle()))
            return;
        else
            titleBadgeTextLine = TnTextParser.parse(titleBadge);
    }
    
    /**
     * Set address for addressListItem.
     * 
     * @param address
     */
    public void setAddress(String address)
    {
        if(address == null || address.trim().length() == 0)
        {
            addressTextLine = null;
        }
        else if (address.equals(getAddress()))
        {
            return;
        }
        else
        {
            addressTextLine = TnTextParser.parse(address);
        }
    }
    
    public void setAddressData(Address address)
    {
        this.data = address;
    }
    
    public Address getAddressData()
    {
        return data;
    }

    /**
     * Get address for addressListItem.
     * 
     * @return addressStr
     */
    public String getAddress()
    {
        return addressTextLine == null ? null : addressTextLine.getText();
    }

    /**
     * Set shareInfo for addressListItem.
     * 
     * @param shareInfo
     */
    public void setShareInfo(String shareInfo)
    {
        if(shareInfo == null)
            shareInfoTextLine = null;
        else if (shareInfo.equals(getShareInfo()))
            return;
        else
            shareInfoTextLine = TnTextParser.parse(shareInfo);
    }

    /**
     * Get shareInfo for addressListItem.
     * 
     * @return
     */
    public String getShareInfo()
    {
        return shareInfoTextLine == null ? null : shareInfoTextLine.getText();
    }

    /**
     * setText() is not support for addressListItem <br>
     * Please use <b>setTitle(), setAddress(), setShareInfo</b> to control the display content. It will throws
     * exceptions while calling this.
     */
    public void setText(String text)
    {
        throw new UnsupportedOperationException(
                "setText() is not support for addressListItem, please use setTitle()/setAddress()/setShareInfo to control the display content.");
    }

    /**
     * This method will combine Title/Address/ShareInfo into a string and return.
     */
    public String getText()
    {
        StringBuffer text = new StringBuffer();
        if (this.titleTextLine != null)
        {
            text.append(titleTextLine.getText());
            text.append(" ");
        }
        if (this.addressTextLine != null)
        {
            text.append(addressTextLine.getText());
            text.append(" ");
        }
        if (this.shareInfoTextLine != null)
        {
            text.append(shareInfoTextLine.getText());
            text.append(" ");
        }

        return text.toString();
    }


    /**
     * Set font for shareInfo.
     * 
     * @param inforFont
     */
    public void setShareInfoFont(AbstractTnFont inforFont)
    {
        this.shareInfoFont = inforFont;
    }

    /**
     * Get font for shareInfo.
     * 
     * @return font
     */
    public AbstractTnFont getShareInfoFont()
    {
        return shareInfoFont;
    }

    /**
     * Set the color for the received information line.
     * 
     * @param focusedThirdColor the focus color of the sender's information
     * @param blurThirdColor the blur color of the the sender's information
     */
    public void setShareInfoColor(int focusedInfoColor, int blurInfoColor)
    {
        this.focusedShareInfoColor = focusedInfoColor;
        this.blurShareInfoColor = blurInfoColor;
    }

    /**
     * set the color of the titleBadge
     * @param focusedInfoColor
     * @param blurInfoColor
     */
    public void setTitleBadgeColor(int focusedInfoColor, int blurInfoColor)
    {
        this.focusedTitleBadgeColor = focusedInfoColor;
        this.blurTitleBadgeColor = blurInfoColor;
    }
    
    /**
     * set the color of title
     * @param focusedInfoColor
     * @param blurInfoColor
     */
    public void setTitleColor(int focusedInfoColor, int blurInfoColor)
    {
        this.focusedTitleColor = focusedInfoColor;
        this.blurTitleColor = blurInfoColor;
    }
    
    /**
     * Set gap for addressListItem.
     * 
     * @param vGap
     * @param hGap
     */
    public void setGap(int gap0, int gap1)
    {
        this.gap[0] = gap0;
        this.gap[1] = gap1;
    }
    
    /**
     * set the command for this component
     * @param command
     */
    public void setRightButtonCommand(int command)
    {
        this.command = command;
    }

    /**
     * retrieve the command for this component
     * @return the command for this component
     */
    public int getRightButtonCommand()
    {
        return command;
    }
    
    /**
     * retrieve the gap of this component
     * @return the gap of this component
     */
    public int[] getGap()
    {
        return gap;
    }


    protected FrogListItem getUiComponent()
    {
        return new CitizenAddressListItem(this);
    }

    public void sublayout(int width, int height)
    {
        CitizenAddressListItem item = (CitizenAddressListItem) this.mItem;

        int lineNumer = getTextLineNumber();
        if (lineNumer == 1)
        {
            super.sublayout(width, height);
        }
        else
        {
            preferWidth = width;
            preferHeight = font.getHeight() + boldFont.getHeight() + topPadding + bottomPadding;
            if (lineNumer == 3)
            {
                shareInfoFont = item.getShareInfoFont();
                if (shareInfoFont == null)
                {
                    shareInfoFont = font;
                    item.setShareInfoFont(shareInfoFont);
                }
                preferHeight += shareInfoFont.getHeight();
            }
        }
    }

    protected void paint(AbstractTnGraphics graphics)
    {
        CitizenAddressListItem item = (CitizenAddressListItem) this.mItem;
        int width = this.getWidth() - leftPadding - rightPadding;
        int LeftInfoMargin = leftPadding;

        AbstractTnImage leftIcon = item.getLeftIcon(isFocused);
        if (leftIcon != null)
        {
            graphics.drawImage(leftIcon, LeftInfoMargin, (this.getHeight() - leftIcon.getHeight()) / 2, AbstractTnGraphics.LEFT
                    | AbstractTnGraphics.TOP);
            LeftInfoMargin += leftIcon.getWidth() + item.getGap()[0];
            width -= leftIcon.getWidth() + item.getGap()[0];
        }

        AbstractTnImage rightBackgroundIcon = item.getRightBackground(isRightIconPressed);
        AbstractTnImage rightImageIcon = item.getRightIcon(isRightIconPressed);
        int rightInfoMargin = this.getWidth() - rightPadding;
        
        if (rightBackgroundIcon != null)
        {
            if (getTnUiArgs().get(KEY_PREFER_RIGHT_BACKGROUND_SIZE) != null)
            {
                rightBackgroundIcon.setHeight(getTnUiArgs().get(KEY_PREFER_RIGHT_BACKGROUND_SIZE).getInt());
                rightBackgroundIcon.setWidth(getTnUiArgs().get(KEY_PREFER_RIGHT_BACKGROUND_SIZE).getInt());
            }
            graphics.drawImage(rightBackgroundIcon, rightInfoMargin, (this.getHeight() - rightBackgroundIcon.getHeight()) / 2,
                AbstractTnGraphics.RIGHT | AbstractTnGraphics.TOP);
            width -= rightBackgroundIcon.getWidth() + item.getGap()[1];
            if (rightImageIcon != null)
            {
                graphics.drawImage(rightImageIcon, rightInfoMargin - (rightBackgroundIcon.getWidth() - rightImageIcon.getWidth()) / 2, (this
                        .getHeight() - rightImageIcon.getHeight()) / 2, AbstractTnGraphics.RIGHT | AbstractTnGraphics.TOP);
            }
        }
        else if (rightImageIcon != null)
        {
            rightImageIcon = item.getRightIcon(isRightIconPressed | isFocused);
            graphics.drawImage(rightImageIcon, rightInfoMargin, (this.getHeight() - rightImageIcon.getHeight()) / 2,
                AbstractTnGraphics.RIGHT | AbstractTnGraphics.TOP);
            width -= rightImageIcon.getWidth() + item.getGap()[1];
        }

        graphics.setColor(getForegroundColor(isFocused()));

        int lineNumber = getTextLineNumber();

        AbstractTnFont font = this.font;
        AbstractTnFont boldFont = this.boldFont;

        switch (lineNumber)
        {
            case 1:
            {
                TnTextLine currentLine = null;
                if (titleTextLine != null)
                    currentLine = titleTextLine;
                if (addressTextLine != null)
                    currentLine = addressTextLine;
                if (shareInfoTextLine != null)
                    currentLine = shareInfoTextLine;

                if(titleTextLine != null)
                {
                    int color = graphics.getColor();
                    if (this.isFocused())
                    {
                        graphics.setColor(this.focusedTitleColor);
                    }
                    else
                    {
                        graphics.setColor(this.blurTitleColor);
                    }
                    
                    int offset = boldFont.stringWidth(getTitle());//this is the default offset for drawing badge text
                    if(titleBadgeTextLine != null)
                    {
                        //if the total width is greater than the max width, use the max width and update the offset for badge text.
                        if (boldFont.stringWidth(titleTextLine.getText() + titleBadgeTextLine.getText()) > width)
                        {
                            width -= boldFont.stringWidth(titleBadgeTextLine.getText()); 
                            offset = width;
                        }
                    }
                    
                    FrogTextHelper.paint(graphics, LeftInfoMargin, (this.getHeight() - boldFont.getHeight()) / 2, currentLine, font, boldFont, width, true);
                    graphics.setColor(color);
                    if(titleBadgeTextLine != null)
                    {
                        int oldColor = graphics.getColor();
                        if (this.isFocused())
                        {
                            graphics.setColor(this.focusedTitleBadgeColor);
                        }
                        else
                        {
                            graphics.setColor(this.blurTitleBadgeColor);
                        }
                                               
                    	FrogTextHelper.paint(graphics, LeftInfoMargin + offset, (this.getHeight() - font.getHeight()) / 2, titleBadgeTextLine, font, boldFont, width, true);
                        
                        graphics.setColor(oldColor);
                    }
                }
                else
                {
                    FrogTextHelper.paint(graphics, LeftInfoMargin, (this.getHeight() - boldFont.getHeight()) / 2, currentLine, font, boldFont,
                        width, true);
                }
                break;
            }
            case 2:
            {
                int gap = (this.getHeight() - boldFont.getHeight() - font.getHeight() - topPadding - bottomPadding) / 3;
                TnTextLine currentLine = null;
                TnTextLine secondLine = null;
                if (titleTextLine != null)
                {
                    currentLine = titleTextLine;
                    if (addressTextLine != null)
                    {
                        // currentLine = addressTextLine;
                        secondLine = addressTextLine;
                    }
                    if (shareInfoTextLine != null)
                    {
                        secondLine = shareInfoTextLine;
                    }
                }
                else
                {
                    if (addressTextLine != null)
                    {
                        currentLine = addressTextLine;
                    }
                    if (shareInfoTextLine != null)
                    {
                        secondLine = shareInfoTextLine;
                    }
                }
                if(titleTextLine != null)
                {
                    int color = graphics.getColor();
                    if (this.isFocused())
                    {
                        graphics.setColor(this.focusedTitleColor);
                    }
                    else
                    {
                        graphics.setColor(this.blurTitleColor);
                    }
                    FrogTextHelper.paint(graphics, LeftInfoMargin, topPadding + gap, currentLine, boldFont, boldFont, width, true);
                    graphics.setColor(color);
                    if(titleBadgeTextLine != null)
                    {
                        int oldColor = graphics.getColor();
                        if (this.isFocused())
                        {
                            graphics.setColor(this.focusedTitleBadgeColor);
                        }
                        else
                        {
                            graphics.setColor(this.blurTitleBadgeColor);
                        }
                        FrogTextHelper.paint(graphics, LeftInfoMargin + boldFont.stringWidth(getTitle()), topPadding + gap + (gap - font.getHeight()) / 2, titleBadgeTextLine, font, boldFont, width, true);
                        graphics.setColor(oldColor);
                    }
                }
                else
                    FrogTextHelper.paint(graphics, LeftInfoMargin, topPadding + gap, currentLine, font, boldFont, width, true);
                FrogTextHelper.paint(graphics, LeftInfoMargin, topPadding + gap * 2 + boldFont.getHeight(), secondLine, font, boldFont,
                    width, true);
                break;
            }
            case 3:
            {
                if (shareInfoFont == null)
                {
                    shareInfoFont = font;
                    item.setShareInfoFont(shareInfoFont);
                }

                int gap = (this.getHeight() - boldFont.getHeight() - font.getHeight() - shareInfoFont.getHeight() - topPadding - bottomPadding) / 2;
                /**
                 * add gap<=0 protection for 320x240 devices
                 * use tempPadding instead of topPadding, 
                 * the topPadding can't be changed for switch screen orientation cause.
                 **/
                int tempPadding = topPadding;
                if ( gap <= 0 )
                {
                    gap = (this.getHeight() - (boldFont.getHeight() + font.getHeight() * 2)) / 4;
                    tempPadding = gap;
                }
                int color = graphics.getColor();
                if (this.isFocused())
                {
                    graphics.setColor(this.focusedTitleColor);
                }
                else
                {
                    graphics.setColor(this.blurTitleColor);
                }
                FrogTextHelper.paint(graphics, LeftInfoMargin, tempPadding, titleTextLine, boldFont, boldFont, width, true);
                graphics.setColor(color);
                if(titleBadgeTextLine != null)
                {
                    int oldColor = graphics.getColor();
                    if (this.isFocused())
                    {
                        graphics.setColor(this.focusedTitleBadgeColor);
                    }
                    else
                    {
                        graphics.setColor(this.blurTitleBadgeColor);
                    }
                    FrogTextHelper.paint(graphics, LeftInfoMargin + boldFont.stringWidth(getTitle()), tempPadding + gap, titleBadgeTextLine, font, boldFont, width, true);
                    graphics.setColor(oldColor);
                }
                FrogTextHelper.paint(graphics, LeftInfoMargin, tempPadding + boldFont.getHeight() + gap, addressTextLine, font, boldFont,
                    width, true);
                if (this.isFocused())
                {
                    graphics.setColor(this.focusedShareInfoColor);
                }
                else
                {
                    graphics.setColor(this.blurShareInfoColor);
                }
                FrogTextHelper.paint(graphics, LeftInfoMargin, tempPadding + boldFont.getHeight() + gap * 2 + font.getHeight(),
                    shareInfoTextLine, font, boldFont, width, true);
                break;
            }
            default:
            {
                break;
            }
        }
    }

    protected void initDefaultStyle()
    {
        super.initDefaultStyle();

        if (gap != null)
        {
            gap[0] = font.getMaxWidth() / 2;;
            gap[1] = font.getMaxWidth() / 2;
        }
    }

    protected void initItemData(FrogListItem item)
    {
        super.initItemData(item);

        this.titleTextLine = ((CitizenAddressListItem) item).titleTextLine;
        this.addressTextLine = ((CitizenAddressListItem) item).addressTextLine;
        this.shareInfoTextLine = ((CitizenAddressListItem) item).shareInfoTextLine;
        this.shareInfoFont = ((CitizenAddressListItem) item).shareInfoFont;
    }

    protected void reset()
    {
        super.reset();

        this.titleTextLine = null;
        this.addressTextLine = null;
        this.shareInfoTextLine = null;
        this.shareInfoFont = null;
    }
    
    protected AbstractTnImage getLeftIcon(boolean isFocus)
    {
        AbstractTnImage leftIcon = null;
        if (this.tnUiArgs != null)
        {
            TnUiArgs.TnUiArgAdapter argAdapter = null;
            if(isFocus)
                argAdapter = this.tnUiArgs.get(KEY_LEFT_ICON_FOCUS);
            else
                argAdapter = this.tnUiArgs.get(KEY_LEFT_ICON_UNFOCUS);
            
            if (argAdapter != null)
            {
                leftIcon = argAdapter.getImage();
            }
        }

        return leftIcon;
    }
    
    protected AbstractTnImage getRightBackground(boolean isFocused)
    {
        AbstractTnImage rightBg = null;
        if (this.tnUiArgs != null)
        {
            TnUiArgs.TnUiArgAdapter argAdapter = null;
            if(isFocused)
                argAdapter = this.tnUiArgs.get(KEY_RIGHT_BTN_BG_FOCUS);
            else
                argAdapter = this.tnUiArgs.get(KEY_RIGHT_BTN_BG_UNFOCUS);
            
            if (argAdapter != null)
            {
                rightBg = argAdapter.getImage();
            }
        }
        
        return rightBg;
    }
    
    protected AbstractTnImage getRightIcon(boolean isFocused)
    {
        AbstractTnImage rightIcon = null;
        if (this.tnUiArgs != null)
        {
            TnUiArgs.TnUiArgAdapter argAdapter = null;
            if(isFocused)
                argAdapter = this.tnUiArgs.get(KEY_RIGHT_BTN_ICON_FOCUS);
            else
                argAdapter = this.tnUiArgs.get(KEY_RIGHT_BTN_ICON_UNFOCUS);
            
            if (argAdapter != null)
            {
                rightIcon = argAdapter.getImage();
            }
        }
        
        return rightIcon;
    }

    private int getTextLineNumber()
    {
        int lineNumber = 0;
        if (titleTextLine != null && !titleTextLine.isEmpty)
            lineNumber++;
        if (addressTextLine != null && !addressTextLine.isEmpty)
            lineNumber++;
        if (shareInfoTextLine != null && !shareInfoTextLine.isEmpty)
            lineNumber++;

        return lineNumber;
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
                    case TnMotionEvent.ACTION_DOWN:
                    {
                        CitizenAddressListItem item = (CitizenAddressListItem) this.mItem;
                        if (item.getRightBackground(false) != null)
                        {
                            int xPos = event.getX();
                            int yPos = event.getY();
                            if (isRightIconClicked(xPos, yPos))
                            {
                                isRightIconPressed = true;
                                this.requestPaint();
                                return true;
                            }
                        }
                        break;
                    }
                    case TnMotionEvent.ACTION_UP:
                    {
                        if(isRightIconPressed)
                        {
                            isRightIconPressed = false;
                            this.requestPaint();
                        }
                        CitizenAddressListItem item = (CitizenAddressListItem) this.mItem;
                        if (item.getRightBackground(false) != null)
                        {
                            int xPos = event.getX();
                            int yPos = event.getY();
                            if (isRightIconClicked(xPos, yPos))
                            {
                                if(this.commandListener != null)
                                {
                                    TnUiEvent uiEvent = new TnUiEvent(TnUiEvent.TYPE_COMMAND_EVENT, this);
                                    uiEvent.setCommandEvent(new TnCommandEvent(item.getRightButtonCommand()));
                                    this.commandListener.handleUiEvent(uiEvent);
                                }
                                return true;
                            }
                        }
                        break;
                    }
                    case TnMotionEvent.ACTION_MOVE:
                    {
                        if(isRightIconPressed)
                        {
                            isRightIconPressed = false;
                            this.requestPaint();
                        }
                        break;
                    }
                }
            }
        }
        return super.handleUiEvent(tnUiEvent);
    }

    protected boolean isRightIconClicked(int x, int y)
    {
        int left = this.getWidth() - rightPadding - getTnUiArgs().get(KEY_PREFER_RIGHT_BACKGROUND_SIZE).getInt();
        int right = left + getTnUiArgs().get(KEY_PREFER_RIGHT_BACKGROUND_SIZE).getInt();
        int top = (this.getHeight() - getTnUiArgs().get(KEY_PREFER_RIGHT_BACKGROUND_SIZE).getInt()) / 2;
        int bottom = top + getTnUiArgs().get(KEY_PREFER_RIGHT_BACKGROUND_SIZE).getInt();
        if (x > left && x < right && y > top && y < bottom)
            return true;
        else
            return false;
    }

}
