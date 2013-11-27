/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * FrogTnPoiListItem.java
 *
 */
package com.telenav.ui.citizen;

import com.telenav.tnui.core.AbstractTnUiHelper;
import com.telenav.tnui.graphics.AbstractTnFont;
import com.telenav.tnui.graphics.AbstractTnGraphics;
import com.telenav.tnui.graphics.AbstractTnImage;
import com.telenav.tnui.graphics.TnColor;
import com.telenav.ui.frogui.text.FrogTextHelper;
import com.telenav.ui.frogui.widget.FrogListItem;
import com.telenav.ui.tnui.text.TnTextLine;
import com.telenav.ui.tnui.text.TnTextParser;

/**
 *@author jwchen (jwchen@telenav.cn)
 *@date 2010-7-29
 */
public class CitizenPoiListItem extends FrogListItem
{
    protected TnTextLine titleTextLine;
    protected TnTextLine addressTextLine;
    protected TnTextLine shortMessageTextLine;
    protected int titleFocusedColor = TnColor.WHITE;
    protected int titleUnFocuedColor = TnColor.BLACK;
    private int shortMessageTextFocusedColor = TnColor.WHITE;
    private int shortMessageTextUnFocusedColor = TnColor.BLACK;
    
    protected int rate = 0;
    protected int rateGap = 0;
    protected AbstractTnImage brightRateImage = null;
    protected AbstractTnImage darkRateImage = null;
    protected AbstractTnImage indexImage = null;
    protected AbstractTnImage dealImage = null;
    protected AbstractTnImage menuImage = null;
    protected String distance = null;
    protected String price = null;
    protected AbstractTnFont distanceFont;
    protected AbstractTnFont indexFont;
    protected boolean hasDeals;
    protected boolean hasMenu;
    protected int indexFontForegroundColor = TnColor.WHITE;
    protected int indexFontBlurColor = TnColor.WHITE;
    protected boolean isSponsored;
    protected int poiIndex;
    private AbstractTnImage adsCornerImage;
    
    
    
    /**
     * Create a FrogPoiListItem with specific Id.
     * <br>
     * <b>This method won't realize the item instantly.</b>
     * 
     * @param id
     */
    public CitizenPoiListItem(int id)
    {
        super(id);
    }
    
    
    /**
     * Create a FrogPoiListItem with a existing item.
     * <br>
     * <b>It will be realize immediately.</b>
     * 
     * @param item
     */
    protected CitizenPoiListItem(CitizenPoiListItem item)
    {
        this(item.getId());
        this.initItemData(item);
        this.mItem = item;
    }
    
    /**
     * Set title for poiListItem.
     * 
     * @param title
     */
    public void setTitle(String title)
    {
        if (title != null && title.equals(getTitle()))
            return;

        titleTextLine = TnTextParser.parse(title);
    }
    
    /**
     * set title text color
     * @param focusedColor
     * @param unFocusedColor
     */
    public void setTitleColor(int focusedColor, int unFocusedColor)
    {
        this.titleFocusedColor = focusedColor;
        this.titleUnFocuedColor = unFocusedColor;
    }
    
    /**
     * Retrieve the title text color
     * @param isFocused
     * @return
     */
    public int getTitleColor(final boolean isFocused)
    {
        return isFocused? titleFocusedColor : titleUnFocuedColor;
    }
    /**
     * Set coupon text color.
     * @param focusedColor
     * @param unFocusedColor
     */
    public void setShortMessageTextColor(int focusedColor, int unFocusedColor)
    {
        this.shortMessageTextFocusedColor = focusedColor;
        this.shortMessageTextUnFocusedColor = unFocusedColor;
    }
    
    /**
     * Retrieve the coupon text color
     * @param isFocused
     * @return
     */
    public int getShortMessageTextColor(final boolean isFocused)
    {
        return isFocused? this.shortMessageTextFocusedColor: this.shortMessageTextUnFocusedColor;
    }
    
    /**
     * Get title for poiListItem.
     * 
     * @return
     */
    public String getTitle()
    {
        return titleTextLine == null ? null : titleTextLine.getText();
    }
    
    /**
     * Set Address for poiListItem.
     * 
     * @param address
     */
    public void setAddress(String address)
    {
        if (address != null && address.equals(getAddress()))
            return;

        addressTextLine = TnTextParser.parse(address);
    }

    /**
     * get Address for poiListItem.
     * 
     * @return
     */
    public String getAddress()
    {
        return addressTextLine == null ? null : addressTextLine.getText();
    }

    /**
     *  setText() is not support for poiListItem
     *  <br>Please use <b>setText(), setRate()... etc</b> to control the display content.
     *  It will throws exceptions while calling this.
     */
    public void setText(String text)
    {
        throw new UnsupportedOperationException("setText() is not support for poiListItem, please use setTitle()/setAddress()/setShareInfo to control the display content.");
    }

    public String getText()
    {
        StringBuffer text = new StringBuffer();
        if(this.titleTextLine != null)
        {
            text.append(titleTextLine.getText());
            text.append(" ");
        }
        if(this.addressTextLine != null)
        {
            text.append(addressTextLine.getText());
            text.append(" ");
        }
        return text.toString();
    }
    
    /**
     * Set rate.
     * 
     * @param rate
     */
    public void setRate(int rate)
    {
        this.rate = rate;
    }
    
    /**
     * Get rate.
     * 
     * @return
     */
    public int getRate()
    {
        return rate;
    }
    
    /**
     * Set rate gap.
     * 
     * @param rateGap
     */
    public void setRateGap(int rateGap)
    {
        this.rateGap = rateGap;
    }

    /**
     * Get rate gap.
     * 
     * @return
     */
    public int getRateGap()
    {
        return rateGap;
    }
    
    /**
     * Set rate images.
     * 
     * @param darkRateImage
     * @param brightRateImage
     */
    public void setRateImage(AbstractTnImage brightRateImage, AbstractTnImage darkRateImage)
    {
        this.darkRateImage = darkRateImage;
        this.brightRateImage = brightRateImage;
    }

    /**
     * Get rate image.
     * 
     * @param isBright, true for brightRateImage, false for darkRateImage.
     * @return
     */
    public AbstractTnImage getRateImage(boolean isBright)
    {
        if(isBright)
            return brightRateImage;
        else
            return darkRateImage;
    }
    
    /**
     * Get index image.
     * 
     * @param indexImage
     */
    public void setIndexImage(AbstractTnImage indexImage)
    {
        this.indexImage = indexImage;
    }

    /**
     * Get index image.
     * 
     * @return
     */
    public AbstractTnImage getIndexImage()
    {
        return indexImage;
    }
    
    /**
     * Set deal image.
     * 
     * @param dealImage
     */
    public void setDealImage(AbstractTnImage dealImage)
    {
        this.dealImage = dealImage;
    }

    /**
     * Get deal image.
     * 
     * @return
     */
    public AbstractTnImage getDealImage()
    {
        return dealImage;
    }
    
    /**
     * Return ads conner image
     * @return
     */
    public AbstractTnImage getAdsCornerIcon()
    {
        return adsCornerImage;
    }
    
    public void setAdsCornerIcon(AbstractTnImage adsCornerImage)
    {
        this.adsCornerImage = adsCornerImage;
    }
    /**
     * Set menuImage.
     * 
     * @param menuImage
     */
    public void setMenuImage(AbstractTnImage menuImage)
    {
        this.menuImage = menuImage;
    }

    /**
     * Get menu image.
     * 
     * @return
     */
    public AbstractTnImage getMenuImage()
    {
        return menuImage;
    }
    
    /**
     * Set distance.
     * 
     * @param distance
     */
    public void setDistance(String distance)
    {
        this.distance = distance;
    }

    /**
     * Get distance.
     * 
     * @return
     */
    public String getDistance()
    {
        return distance;
    }
    
    public String getPrice()
    {
        return price;
    }
    
    public void setPrice(String price)
    {
        this.price = price;
    }
    
    /**
     * Set hasDeal.
     * 
     * @param hasDeals
     */
    public void setHasDeals(boolean hasDeals)
    {
        this.hasDeals = hasDeals;
    }

    /**
     * Get hasDeal.
     * 
     * @return
     */
    public boolean hasDeals()
    {
        return hasDeals;
    }
    
    /**
     * Set isHasMenu.
     * 
     * @param hasMenu
     */
    public void setHasMenu(boolean hasMenu)
    {
        this.hasMenu = hasMenu;
    }

    /**
     * Get hasMenu.
     * 
     * @return
     */
    public boolean hasMenu()
    {
        return hasMenu;
    }
    
    /**
     * Set distanceFont for PoiListItem.
     * 
     * @param distanceFont
     */
    public void setDistanceFont(AbstractTnFont distanceFont)
    {
        this.distanceFont = distanceFont;
    }

    /**
     * Get distanceFont for PoiListItem.
     * 
     * @return
     */
    public AbstractTnFont getDistanceFont()
    {
        return distanceFont;
    }
    
    /**
     * 
     * @param font
     */
    public void setIndexFont(AbstractTnFont font)
    {
        this.indexFont = font;
    }
    
    /**
     * Implements custom layout features for this component. 
     * 
     * @param width the width of the component
     * @param height the height of the component
     */
    public void sublayout(int width, int height)
    {
        AbstractTnImage darkRateImage = ((CitizenPoiListItem)this.mItem).getRateImage(false);
        this.setPreferredWidth(width);
        if (font != null && boldFont != null && darkRateImage != null)
        {
            this.setPreferredHeight(font.getHeight() + boldFont.getHeight() + darkRateImage.getHeight() + topPadding + bottomPadding);
        }
    }
    
    public void setIndexFontColor(int foregroundColor, int blurColor)
    {
        this.indexFontForegroundColor = foregroundColor;
        this.indexFontBlurColor = blurColor;
    }
    
    public int getIndexFontColor(boolean isFocused)
    {
        if(isFocused)
        {
            return this.indexFontForegroundColor;
        }
        else
        {
            return this.indexFontBlurColor;
        }
    }
    
    public void setIsSponsored(boolean isSponsored)
    {
        this.isSponsored = isSponsored;
    }
    
    public void setPoiIndex(int index)
    {
        this.poiIndex = index;
    }
    
    public void setShortMessageContent(String shortMessage)
    {
        if (shortMessage == null || shortMessage.trim().length() == 0)
        {
            shortMessageTextLine = null;
        }
        else
        {
            shortMessageTextLine = TnTextParser.parse(shortMessage);
        }
    }
    
    protected void paint(AbstractTnGraphics graphics)
    {
        CitizenPoiListItem item = (CitizenPoiListItem) this.mItem;
        AbstractTnFont distanceFont = item.getDistanceFont();
        if (distanceFont == null)
        {
            distanceFont = font;
            item.setDistanceFont(distanceFont);
        }
        AbstractTnFont boldFont = this.boldFont;
        AbstractTnFont font = this.font;
        int indexImageHeight = 0;
        if(this.getIndexImage() != null)
        {
            indexImageHeight = this.getIndexImage().getHeight();
        }
        int maxTopHeight = indexImageHeight > boldFont.getAscent() ? indexImageHeight : boldFont.getAscent();
        int firstLineHeight = maxTopHeight + boldFont.getDescent();//To make the index circle and the part of first line above the base line align center. 
        int lineGap = (this.getHeight() - firstLineHeight - font.getHeight() - brightRateImage .getHeight()) / 4;
        int indexOffSetX = leftPadding;
        int indexOffSetY = lineGap;
        
        int indexImageWidth = 0;
        if(this.getIndexImage() != null)
        {
            this.drawIndexNCornerIcon(indexOffSetX, indexOffSetY + (maxTopHeight + 1) / 2, graphics);
            indexImageWidth = this.getIndexImage().getWidth();
        }
        int contentOffSetX = indexOffSetX + indexImageWidth + indexOffSetX ;
        int contentWidth = this.getWidth() - contentOffSetX - rightPadding;
        this.drawPoiNameNDist( contentOffSetX, indexOffSetY + (maxTopHeight + 1) / 2, contentWidth, graphics);
        
        //if there is short Message, we need to use new method to compute the line gap.
        if (this.shortMessageTextLine != null && !this.shortMessageTextLine.isEmpty)
        {
            lineGap = (this.getHeight() - boldFont.getHeight() - font.getHeight() * 2) / 4;
        }
        
        int addressOffSetY = indexOffSetY + this.boldFont.getHeight() + lineGap;
        this.drawPoiAddress(contentOffSetX, addressOffSetY, contentWidth, graphics);
        
        int ratingOffSetY = addressOffSetY + this.font.getHeight() + lineGap;
        this.drawRatingIconCoupon(contentOffSetX, ratingOffSetY, contentWidth, graphics);
    
        this.drawPrice(contentOffSetX, ratingOffSetY, contentWidth, graphics);
    }
    
    protected FrogListItem getUiComponent()
    {
        return new CitizenPoiListItem(this);
    }
    
    protected void initItemData(FrogListItem item)
    {
        super.initItemData(item);
        
        this.titleTextLine = ((CitizenPoiListItem)item).titleTextLine;
        this.addressTextLine = ((CitizenPoiListItem)item).addressTextLine;
    }

    protected void reset()
    {
        super.reset();
        
        this.titleTextLine = null;
        this.addressTextLine = null;
    }
    
    private void drawRatingStar(AbstractTnGraphics g, int x, int y)
    {
        AbstractTnImage star = null;
        int xOffset = x;
        CitizenPoiListItem item = (CitizenPoiListItem)this.mItem;
        int rate = item.getRate();
        int rateGap = item.getRateGap();
        AbstractTnImage brightRateImage = item.getRateImage(true);
        AbstractTnImage darkRateImage = item.getRateImage(false);
        
        for(int i = 0; i < 5; i ++)
        {
            int tmp = rate - ((i + 1) * 10);
            if(tmp >= 0)
            {
                star = brightRateImage;
            }
            else
            {
                star = darkRateImage;
            }
            g.drawImage(star, xOffset, y, AbstractTnGraphics.LEFT | AbstractTnGraphics.TOP);
            
            xOffset += brightRateImage.getWidth() + rateGap;
        }
    }
    
    protected void drawIndexNCornerIcon(int x, int y, AbstractTnGraphics graphics)
    {
        AbstractTnFont oldFont = graphics.getFont();
        int oldColor = graphics.getColor();
        AbstractTnImage indexImage = this.getIndexImage();
        
        graphics.drawImage(indexImage, x, y, AbstractTnGraphics.LEFT
                | AbstractTnGraphics.VCENTER);
        int indexImageWidth = indexImage.getWidth();
        int indexCenterX = x + indexImageWidth / 2;
        int indexCenterY = y;
        if (!isSponsored)
        {
            graphics.setColor(this.getIndexFontColor(this.isFocused));
            if(indexFont != null)
            {
                graphics.setFont(indexFont);
            }
            graphics.drawString("" + poiIndex, indexCenterX, indexCenterY, AbstractTnGraphics.HCENTER
                    | AbstractTnGraphics.FONT_ABSOLUTE_VCENTER);
        }
        else
        {
            if(this.adsCornerImage != null)
            {
                int xRightCornerImage = this.getWidth() - this.adsCornerImage.getWidth();
                graphics.drawImage(this.adsCornerImage, xRightCornerImage, 0, AbstractTnGraphics.LEFT | AbstractTnGraphics.TOP);
            }
        }
        graphics.setFont(oldFont);
        graphics.setColor(oldColor);
    }
    
    protected void drawPoiNameNDist(int x, int y, int width, AbstractTnGraphics graphics)
    {
        AbstractTnFont oldFont = graphics.getFont();
        int oldColor = graphics.getColor();
        AbstractTnFont currentDistanceFont = this.getDistanceFont();
        if (currentDistanceFont == null)
        {
            currentDistanceFont = font;
            this.setDistanceFont(currentDistanceFont);
        }
        int distanceWidth = currentDistanceFont.stringWidth(distance);
        int nameMaxWidth = width ;
        if (distance != null && !this.isSponsored)
        {
            int rightInfoOff = x + width - distanceWidth;
            graphics.setFont(currentDistanceFont);
            graphics.setColor(this.getForegroundColor(isFocused()));
            graphics.drawString(distance, rightInfoOff, y + boldFont.getHeightOfCenterAboveBaseline(((AbstractTnUiHelper)AbstractTnUiHelper.getInstance()).getCommonBaseLineAnchor()), 
                AbstractTnGraphics.LEFT | AbstractTnGraphics.BASE_LINE);
            nameMaxWidth = width - distanceWidth - 10;
        }
        else if(this.isSponsored && this.adsCornerImage != null)
        {
            nameMaxWidth -= this.adsCornerImage.getWidth();
        }
        graphics.setColor(this.getTitleColor(isFocused()));
        FrogTextHelper.paint(graphics, x, y, this.titleTextLine, boldFont,
            boldFont, nameMaxWidth, true, AbstractTnGraphics.FONT_VISUAL_VCENTER);
        
        graphics.setFont(oldFont);
        graphics.setColor(oldColor);
    }
   
    protected void drawPoiAddress(int x, int y, int width, AbstractTnGraphics graphics)
    {
        AbstractTnFont oldFont = graphics.getFont();
        int oldColor = graphics.getColor();
        if (this.addressTextLine != null)
        {
            graphics.setColor(this.getForegroundColor(isFocused()));
            FrogTextHelper.paint(graphics, x,
                y, this.addressTextLine, font, boldFont,
                width, true);
        }
        graphics.setFont(oldFont);
        graphics.setColor(oldColor);
    }
    
    protected void drawRatingIconCoupon(int x, int y, int width, AbstractTnGraphics graphics)
    {
        AbstractTnFont oldFont = graphics.getFont();
        int oldColor = graphics.getColor();
        
        AbstractTnImage currDealImage = this.getDealImage();
        AbstractTnImage currMenuImage = this.getMenuImage();
        int xOffset = x + width;
        if (currMenuImage != null )
        {
            graphics.drawImage(currMenuImage, xOffset, y, AbstractTnGraphics.RIGHT
                | AbstractTnGraphics.TOP);
            xOffset -= (currMenuImage.getWidth() + currMenuImage.getWidth() / 4);
        }
        if (currDealImage != null )
        {
            graphics.drawImage(currDealImage, xOffset, y, AbstractTnGraphics.RIGHT
                    | AbstractTnGraphics.TOP);
            xOffset -= (currDealImage.getWidth() + currDealImage.getWidth() / 4);
        }
        
        if (shortMessageTextLine != null && !shortMessageTextLine.isEmpty)
        {
            graphics.setColor(this.getShortMessageTextColor(isFocused()));
            FrogTextHelper.paint(graphics, x, y, this.shortMessageTextLine, font,
                boldFont, xOffset - x, true);
        }
        else if (!this.isSponsored)                                                  
        {
            //Only draw rating for non sponsored poi
            drawRatingStar(graphics, x, y);
        } 
        graphics.setFont(oldFont);
        graphics.setColor(oldColor);
    }
    
    protected void drawPrice(int x, int y, int width, AbstractTnGraphics graphics)
    {
        if (price != null && !this.isSponsored)
        {
            AbstractTnFont oldFont = graphics.getFont();
            int oldColor = graphics.getColor();
            
            //the font for price is same with distance.
            //so here reuse the font of distance.
            AbstractTnFont currentDistanceFont = this.getDistanceFont();
            if (currentDistanceFont == null)
            {
                currentDistanceFont = font;
                this.setDistanceFont(currentDistanceFont);
            }
            
            int priceWidth = currentDistanceFont.stringWidth(price);
            int rightInfoOff = x + width - priceWidth;
            graphics.setFont(currentDistanceFont);
            graphics.setColor(this.getForegroundColor(isFocused()));
            graphics.drawString(price, rightInfoOff, y, AbstractTnGraphics.LEFT
                    | AbstractTnGraphics.TOP);
            graphics.setFont(oldFont);
            graphics.setColor(oldColor);
        }
        
    }
}
