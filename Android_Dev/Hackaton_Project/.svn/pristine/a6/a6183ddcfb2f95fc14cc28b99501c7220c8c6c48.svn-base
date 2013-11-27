/*
 * (c) Copyright 2010 by TeleNav, Inc.
 * All Rights Reserved.
 *
 */
package com.telenav.ui.citizen.map;

/**
 *@author JY Xu
 *@date Nov 25, 2010
 */

import com.telenav.data.cache.ImageCacheManager;
import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.datatypes.address.Address;
import com.telenav.data.serverproxy.NetworkStatusManager;
import com.telenav.i18n.ResourceBundle;
import com.telenav.logger.Logger;
import com.telenav.module.AppConfigHelper;
import com.telenav.module.map.IMapConstants;
import com.telenav.mvc.ICommonConstants;
import com.telenav.navsdk.events.MapViewEvents.MapViewAddAnnotationRequest;
import com.telenav.navsdk.events.MapViewEvents.MapViewUpdateAnnotationRequest;
import com.telenav.res.ISpecialImageRes;
import com.telenav.res.IStringMap;
import com.telenav.res.ResourceManager;
import com.telenav.tnui.core.AbstractTnUiHelper;
import com.telenav.tnui.core.TnCommandEvent;
import com.telenav.tnui.core.TnUiEvent;
import com.telenav.tnui.graphics.AbstractTnFont;
import com.telenav.tnui.graphics.AbstractTnGraphics;
import com.telenav.tnui.graphics.AbstractTnImage;
import com.telenav.tnui.graphics.TnNinePatchImage;
import com.telenav.tnui.graphics.TnRect;
import com.telenav.ui.ImageDecorator;
import com.telenav.ui.NinePatchImageDecorator;
import com.telenav.ui.UiStyleManager;

public class AddressAnnotation extends AbstractAnnotation
{
    public static final int ADDRESS_ANNOTATION_LAYER = IMapContainerConstants.NAV_SDK_Z_ORDER_USER_DEFINED;

    int layer;

    long viewId;

    private static final int TEXT_GAP_MARGIN = 4;

    private String brandName, firstLine; 

    private int index;
    
    private int iconWidth, iconHeight;
    
    private int iconFocusedWidth, iconFocusedHeight;

    private int arrowWidth = 0;

    private Address address;

    private AbstractTnFont titleFont = UiStyleManager.getInstance().getFont(UiStyleManager.FONT_POI_INFO_PANEL_TITLE);

    private AbstractTnFont contentFont = UiStyleManager.getInstance().getFont(UiStyleManager.FONT_POI_QUICK_FIND_BUTTON_TEXT);

    private int infoPanelWidth;

    private int infoPanelHeight;

    private int infoPanelLeft;

    private int infoPanelTop;

    private int innerBoxWidth;

    private int innerBoxHeight;

    private int innerBoxLeft;

    private int innerBoxTop;

    private int innerBox2Width;

    private int innerBox2Height;

    private int innerBox2Left;

    private int innerBox2Top;

    private int iconHeightInInfoPanel;

    private int infoCommandBarTop;

    public double lat;

    public double lon;

    private int poiIconTop;

    private TnRect infoPanelRect;

    private TnRect innerBoxRect;
    
    private TnRect moreInfoRect;

    private TnRect innerBox2Rect;

    private TnRect saveFavoriteRect;

    private TnRect nearByRect;

    private TnRect shareRect;

    private TnRect downArrowRect;

    private TnRect poiFocusedIconRect;

    private TnRect poiUnfocusedIconRect;
    
    private TnRect carIconRect;

    private int focusedHeight;

    private int focusedWidth;

    private int unfocusedHeight;

    private int unfocusedWidth;

    private boolean isAddressFocused;
    
    private boolean isFavorite;
    
    private long lastClickTime;
    
    private boolean isDrivetoFocused = false;
    private boolean isSaveFocused = false;
    private boolean isNearbyFocused = false;
    private boolean isShareFocused = false;
    
    private boolean titleNeedMoreInfo = true;
    private boolean titleNeedCarIcon = true;
    
    long graphicId = -1;
    
    final static int POI_BUBBLE_IMAGE_TOP_GAP = 2;
    
    final static String POPUP_ADDRESS_ANNOTATION_STYLE = "screen_annotations.address_screen_annotation";
    final static String PIN_ADDRESS_ANNOTATION_STYLE = "screen_annotations.addresspin_screen_annotation";
    
    public AddressAnnotation(Address address, int index, boolean isAddressFocused)
    {
        super((int) ICommonConstants.ANOTATION_COMPONENT_TRANSLATE);
        this.address = address;
        this.isAddressFocused = isAddressFocused;
        this.viewId = MapContainer.getInstance().getViewId();
        if(address.getStop() != null)
        {
            this.lat = address.getStop().getLat() / 100000.0;
            this.lon = address.getStop().getLon() / 100000.0;
        }        
        
        isFavorite = DaoManager.getInstance().getAddressDao().isExistInFavoriteAddress(this.address, true);
        
        //if address is favorite, use label, firstline; else use firstline, secondline
        if (isFavorite)
        {
            this.brandName = address.getLabel();
            this.firstLine = address.getStop().getFirstLine();
            if (this.brandName != null && this.brandName.equalsIgnoreCase(this.firstLine))
            {
                this.brandName = address.getStop().getFirstLine();
                this.firstLine = ResourceManager.getInstance().getStringConverter().convertSecondLine(address.getStop());
            }
            if (this.firstLine == null || this.firstLine.trim().length() == 0)
            {
                this.firstLine = ResourceManager.getInstance().getStringConverter().convertSecondLine(address.getStop());
            }
        }
        else
        {
            this.brandName = address.getStop().getFirstLine();
            
            //To fix bug TN-1888
            if (this.brandName == null || this.brandName.length() == 0)
            {
                this.brandName = address.getStop().getLabel();
            }
            this.firstLine = ResourceManager.getInstance().getStringConverter().convertSecondLine(address.getStop());
        }

        this.index = index;

        this.iconWidth = ImageDecorator.IMG_MAP_ADDRESS.getImage().getWidth();
        this.iconHeight = ImageDecorator.IMG_SELECTED_MAP_ADDRESS.getImage().getHeight();

        iconHeightInInfoPanel = ImageDecorator.IMG_POI_BUBBLE_SAVE_ICON_FOCUSED.getImage().getHeight();
        
        int refPanelWidth = getInfoPanelSize(true);
        int refPanelHeight = getInfoPanelSize(false);
        
        int shadowLen = 1;//hard code, there is no reference in VDD
        
        if(ISpecialImageRes.getOpenglImageFamily() == ISpecialImageRes.FAMILY_SMALL)
        {
            shadowLen = 4;
        }
        else if(ISpecialImageRes.getOpenglImageFamily() == ISpecialImageRes.FAMILY_MEDIUM)
        {
            shadowLen = 9;
        }
        else if(ISpecialImageRes.getOpenglImageFamily() == ISpecialImageRes.FAMILY_LARGE)
        {
            shadowLen = 9; 
        }
        
        int downArrowSpace = ImageDecorator.IMG_DOWN_ARROW.getImage().getHeight();
        
        infoPanelWidth = refPanelWidth;
        infoPanelHeight = refPanelHeight;
        infoPanelHeight = infoPanelHeight - ImageDecorator.IMG_SELECTED_MAP_ADDRESS.getImage().getHeight() - downArrowSpace - shadowLen;
        infoPanelLeft = 0;
        infoPanelTop = 0;

        infoPanelRect = new TnRect(infoPanelLeft, infoPanelTop, infoPanelLeft + infoPanelWidth, infoPanelTop + infoPanelHeight);

        innerBoxWidth = infoPanelWidth * 8946 / 10000;
        innerBoxHeight = infoPanelHeight * 3850 / 10000;

        int innerBoxGap = (infoPanelHeight - innerBoxHeight * 2 - downArrowSpace + shadowLen) / 6;
        
        innerBoxLeft = (infoPanelWidth - innerBoxWidth) / 2 + infoPanelLeft;
        innerBoxTop = infoPanelTop + innerBoxGap + POI_BUBBLE_IMAGE_TOP_GAP;
        innerBoxRect = new TnRect(innerBoxLeft, innerBoxTop, innerBoxLeft + innerBoxWidth, innerBoxTop + innerBoxHeight);
        
        int moreInfoWidth = 0;
        int moreInfoHeight = 0;
        int moreInfoLeft = 0;
        int moreInfoTop = 0;
        titleNeedMoreInfo = ImageDecorator.IMG_POI_BUBBLE_DETAIL_ARROW_UNFOCUS.getImage() != null;
        if (titleNeedMoreInfo)
        {
            moreInfoWidth = ImageDecorator.IMG_POI_BUBBLE_DETAIL_ARROW_UNFOCUS.getImage()
                    .getWidth();
            moreInfoHeight = ImageDecorator.IMG_POI_BUBBLE_DETAIL_ARROW_UNFOCUS
                    .getImage().getHeight();
            moreInfoLeft = innerBoxRect.left + innerBoxRect.width() - moreInfoWidth
                    - refPanelWidth * 8 / 187;
            moreInfoTop = innerBoxRect.top + (innerBoxHeight - moreInfoHeight) / 2;
        }
        
        moreInfoRect = new TnRect(moreInfoLeft, moreInfoTop, moreInfoLeft + moreInfoWidth, moreInfoTop + moreInfoHeight);
        
        innerBox2Width = innerBoxWidth;
        innerBox2Height = innerBoxHeight;
        innerBox2Left = innerBoxLeft;

        innerBox2Top = innerBoxRect.top + innerBoxHeight + innerBoxGap;
        innerBox2Rect = new TnRect(innerBox2Left, innerBox2Top, innerBox2Left + innerBox2Width, innerBox2Top + innerBox2Height);
        
        int downArrowWidth = ImageDecorator.IMG_DOWN_ARROW.getImage().getWidth();
        int downArrowHeight = ImageDecorator.IMG_DOWN_ARROW.getImage().getHeight();
        int downArrowLeft = infoPanelRect.left + infoPanelRect.width() / 2 - downArrowWidth / 2;
        
        int downArrowTop = infoPanelTop + infoPanelHeight - shadowLen;
        if(ISpecialImageRes.getOpenglImageFamily() == ISpecialImageRes.FAMILY_SMALL)
        {
            downArrowTop = downArrowTop - 5;
        }
        downArrowRect = new TnRect(downArrowLeft, downArrowTop, downArrowLeft + downArrowWidth, downArrowTop + downArrowHeight);

        int carIconWidth = 0;
        int carIconHeight = 0;
        int carIconLeft = 0;
        int carIconTop = 0;
        titleNeedCarIcon = ImageDecorator.IMG_POI_BUBBLE_TITLE_CAR_ICON_FOCUSED.getImage() != null;
        if (titleNeedCarIcon)
        {
            carIconWidth = ImageDecorator.IMG_POI_BUBBLE_TITLE_CAR_ICON_FOCUSED.getImage()
                    .getWidth();
            carIconHeight = ImageDecorator.IMG_POI_BUBBLE_TITLE_CAR_ICON_FOCUSED.getImage()
                    .getHeight();
            carIconLeft = innerBoxRect.left + refPanelWidth * 7 / 187;
            carIconTop = innerBoxRect.top + ((innerBoxRect.height() - carIconHeight) >> 1);
            carIconRect = new TnRect(carIconLeft, carIconTop, carIconLeft + carIconWidth, carIconTop + carIconHeight);
        } 

        int step = innerBox2Width / 3;
        int offset = innerBox2Width - step * 3;

        saveFavoriteRect = new TnRect(innerBox2Left, innerBox2Top, innerBox2Left + step, innerBox2Top + innerBox2Height);
        nearByRect = new TnRect(innerBox2Left + step, innerBox2Top, innerBox2Left + 2 * step + offset, innerBox2Top + innerBox2Height);
        shareRect = new TnRect(innerBox2Left + 2 * step, innerBox2Top, innerBox2Left + 3 * step + offset, innerBox2Top + innerBox2Height);
        
        iconFocusedWidth = ImageDecorator.IMG_SELECTED_MAP_ADDRESS.getImage().getWidth();
        iconFocusedHeight = ImageDecorator.IMG_SELECTED_MAP_ADDRESS.getImage().getHeight();
        
        poiIconTop = infoPanelRect.bottom + downArrowSpace - shadowLen;
        focusedWidth = infoPanelWidth + 2 * arrowWidth ;
        focusedHeight = poiIconTop + iconFocusedHeight;
        poiFocusedIconRect = new TnRect(refPanelWidth / 2 - iconWidth / 2, poiIconTop,
            infoPanelRect.left + infoPanelRect.width() / 2 + iconFocusedWidth / 2, poiIconTop + iconFocusedHeight);

        unfocusedHeight = iconHeight;
        unfocusedWidth = iconWidth;
        poiUnfocusedIconRect = new TnRect(0, 0, iconWidth, iconHeight);
    }

    protected void drawInfo(AbstractTnGraphics g)
    {
        // draw the whole info panel box background
        TnNinePatchImage infoPanelNinePatchImage = (TnNinePatchImage) NinePatchImageDecorator.instance
                .decorate(NinePatchImageDecorator.POI_BUBBLE_BG);
        infoPanelNinePatchImage.setWidth(infoPanelRect.width());
        infoPanelNinePatchImage.setHeight(infoPanelRect.height());
        g.drawImage(infoPanelNinePatchImage, infoPanelRect.left, infoPanelRect.top, AbstractTnGraphics.LEFT | AbstractTnGraphics.TOP);

        /***************************************************** Start of first inner box ****************************************************/
        // draw the first inner background
        TnNinePatchImage innerBoxNinePatchImage = null;
        int titleColor;
        if(this.isDrivetoFocused)
        {
            titleColor = UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_BUBBLE_ADDRESS_TITLE_FOCUSED);
            innerBoxNinePatchImage = (TnNinePatchImage) NinePatchImageDecorator.instance
                .decorate(NinePatchImageDecorator.POI_BUBBLE_ADDRESS_TITLE_FOCUSED);
        }
        else
        {
            titleColor = UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_BUBBLE_ADDRESS_TITLE_UNFOCUSED);
            innerBoxNinePatchImage = (TnNinePatchImage) NinePatchImageDecorator.instance
                .decorate(NinePatchImageDecorator.POI_BUBBLE_ADDRESS_TITLE_UNFOCUS);
        }
         
        if (innerBoxNinePatchImage != null)
        {
            innerBoxNinePatchImage.setWidth(innerBoxRect.width());
            innerBoxNinePatchImage.setHeight(innerBoxRect.height());
            g.drawImage(innerBoxNinePatchImage, innerBoxRect.left, innerBoxRect.top,
                AbstractTnGraphics.LEFT | AbstractTnGraphics.TOP);
        }

        // draw the content of the first inner box
        int textLeft = titleNeedCarIcon ?  carIconRect.left + carIconRect.width() : innerBoxRect.left + infoPanelWidth * 12 / 197;
        int strWidthLimited = (titleNeedMoreInfo ? moreInfoRect.left : innerBoxRect.right) - textLeft;
        int textTopGap = (innerBoxRect.height() - titleFont.getHeight() - titleFont.getHeight()) / 2 + 3;
        
        if (brandName != null && brandName.length() > 0)
        {
            if(titleNeedMoreInfo)
            {
              if(isDrivetoFocused)
              {
                  g.drawImage(ImageDecorator.IMG_POI_BUBBLE_DETAIL_ARROW_FOCUSED.getImage(), moreInfoRect.left, moreInfoRect.top, AbstractTnGraphics.LEFT
                    | AbstractTnGraphics.TOP);
              }
              else
              {
                  g.drawImage(ImageDecorator.IMG_POI_BUBBLE_DETAIL_ARROW_UNFOCUS.getImage(), moreInfoRect.left, moreInfoRect.top, AbstractTnGraphics.LEFT
                    | AbstractTnGraphics.TOP);
              }
            }
            if(titleNeedCarIcon)
            {
                if(isDrivetoFocused)
                {
                    g.drawImage(ImageDecorator.IMG_POI_BUBBLE_TITLE_CAR_ICON_FOCUSED.getImage(), carIconRect.left, carIconRect.top, AbstractTnGraphics.LEFT
                      | AbstractTnGraphics.TOP);
                }
                else
                {
                    g.drawImage(ImageDecorator.IMG_POI_BUBBLE_TITLE_CAR_ICON_UNFOCUSED.getImage(), carIconRect.left, carIconRect.top, AbstractTnGraphics.LEFT
                      | AbstractTnGraphics.TOP);
                }
            }
            
        	if(firstLine != null && firstLine.length() > 0)
        	{
        		g.setColor(titleColor);
                g.setFont(titleFont);
                String str = getLabelForPaint(brandName, strWidthLimited, titleFont, false);
                g.drawString(str, textLeft, innerBoxRect.top + textTopGap, AbstractTnGraphics.LEFT
                        | AbstractTnGraphics.TOP);
    			
                str = getLabelForPaint(firstLine, strWidthLimited, titleFont, false);
                g.drawString(str, textLeft, innerBoxRect.top + textTopGap
                        + titleFont.getHeight(), AbstractTnGraphics.LEFT
                        | AbstractTnGraphics.TOP);
        	}
        	else  //only have brand name, if can't be show in one line, wrap the brand name to two lines
        	{
        		String lines[] = wrapTwoLine(brandName, strWidthLimited, titleFont);
        		if(lines[1] == null)//can be show in one line
        		{
        			g.setColor(titleColor);
                    g.setFont(titleFont);
                    g.drawString(lines[0], textLeft, innerBoxRect.top
                            + (innerBoxRect.height() - contentFont.getHeight()) / 2,
                        AbstractTnGraphics.LEFT | AbstractTnGraphics.TOP);
        		}
        		else
        		{
        			g.setColor(titleColor);
                    g.setFont(titleFont);
                    g.drawString(lines[0], textLeft, innerBoxRect.top + textTopGap, AbstractTnGraphics.LEFT
                            | AbstractTnGraphics.TOP);      			
                    g.drawString(lines[1], textLeft, innerBoxRect.top + textTopGap
                            + titleFont.getHeight(), AbstractTnGraphics.LEFT
                            | AbstractTnGraphics.TOP);
        		}
        		
        	}
            
        }
        else if(firstLine != null && firstLine.length() > 0) // only have first line, if can't be show in one line, wrap to two lines
        {
    		String lines[] = wrapTwoLine(firstLine, strWidthLimited, titleFont);
    		if(lines[1] == null)//can be show in one line
    		{
    			g.setColor(titleColor);
                g.setFont(titleFont);
                g.drawString(lines[0], textLeft, innerBoxRect.top
                        + (innerBoxRect.height() - contentFont.getHeight()) / 2,
                    AbstractTnGraphics.LEFT | AbstractTnGraphics.TOP);
    		}
    		else
    		{
    			g.setColor(titleColor);
                g.setFont(titleFont);
                g.drawString(lines[0], textLeft, innerBoxRect.top + textTopGap, AbstractTnGraphics.LEFT
                        | AbstractTnGraphics.TOP);      			
                g.drawString(lines[1], textLeft, innerBoxRect.top + textTopGap
                        + titleFont.getHeight(), AbstractTnGraphics.LEFT
                        | AbstractTnGraphics.TOP);
    		}  
        }
        else
        {
            g.setColor(titleColor);
            g.setFont(titleFont);
            String latStr = "Lat="+this.lat;
            String latShortenStr = getLabelForPaint(latStr, strWidthLimited, titleFont, false);
            g.drawString(latShortenStr, textLeft, innerBoxRect.top + textTopGap, AbstractTnGraphics.LEFT
                    | AbstractTnGraphics.TOP);

            String lonStr = "Lon="+this.lon;
            String lonShortenStr = getLabelForPaint(lonStr, strWidthLimited, titleFont, false);
            g.drawString(lonShortenStr, textLeft, innerBoxRect.top + textTopGap+titleFont.getHeight()
                + TEXT_GAP_MARGIN, AbstractTnGraphics.LEFT
                    | AbstractTnGraphics.TOP);
            
        }
        // draw the down arrow image
        g.drawImage(ImageDecorator.IMG_DOWN_ARROW.getImage(), downArrowRect.left,
            downArrowRect.top, AbstractTnGraphics.LEFT | AbstractTnGraphics.TOP);

        /***************************************************** End of first inner box ****************************************************/

        /***************************************************** Start of second inner box ****************************************************/

        int commandIconTopMargin = (innerBox2Rect.height() - iconHeightInInfoPanel - contentFont.getHeight()) / 2;
        infoCommandBarTop = innerBox2Rect.top + commandIconTopMargin;
        
        g.setFont(contentFont);
        int commandTextTop = infoCommandBarTop + iconHeightInInfoPanel;
        
        ResourceBundle bundle = ResourceManager.getInstance().getCurrentBundle();
        int buttonFocusedColor = UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_BUBBLE_BUTTON_FOCUSED);
        int buttonUnfocusedColor = UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_BUBBLE_BUTTON_UNFOCUSED);
        int buttonDisabledColor = UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_BUBBLE_BUTTON_DISABLED);
          
        if(this.isSaveFocused)
        {
            g.drawImage(ImageDecorator.IMG_POI_BUBBLE_SAVE_ICON_FOCUSED.getImage(),
                (saveFavoriteRect.left + saveFavoriteRect.right) / 2, infoCommandBarTop,
                AbstractTnGraphics.HCENTER | AbstractTnGraphics.TOP);
            g.setColor(buttonFocusedColor);
            g.drawString(bundle.getString(IStringMap.RES_SAVE, IStringMap.FAMILY_MAP),
                    (saveFavoriteRect.left + saveFavoriteRect.right) / 2, commandTextTop,
                    AbstractTnGraphics.HCENTER | AbstractTnGraphics.TOP);
        }
        else
        {
        	if (isFavorite)
            {
                g.drawImage(ImageDecorator.IMG_POI_BUBBLE_SAVE_ADDED_ICON_UNFOCUSED.getImage(),
                    (saveFavoriteRect.left + saveFavoriteRect.right) / 2, infoCommandBarTop,
                    AbstractTnGraphics.HCENTER | AbstractTnGraphics.TOP);
            }
            else
            {
                g.drawImage(ImageDecorator.IMG_POI_BUBBLE_SAVE_ICON_UNFOCUSED.getImage(),
                    (saveFavoriteRect.left + saveFavoriteRect.right) / 2, infoCommandBarTop,
                    AbstractTnGraphics.HCENTER | AbstractTnGraphics.TOP);
            }
        	g.setColor(buttonUnfocusedColor);
        	g.drawString(bundle.getString(IStringMap.RES_SAVE, IStringMap.FAMILY_MAP),
                (saveFavoriteRect.left + saveFavoriteRect.right) / 2, commandTextTop,
                AbstractTnGraphics.HCENTER | AbstractTnGraphics.TOP);
        }
        
        AbstractTnImage nearByImage = null;
        int nearByFontColor = -1;
        if(isNearByEnabled())
        {
            if(this.isNearbyFocused)
            {
                nearByImage = ImageDecorator.IMG_POI_BUBBLE_NEARBY_FOCUSED.getImage();
                nearByFontColor = buttonFocusedColor;
            }
            else
            {
                nearByImage = ImageDecorator.IMG_POI_BUBBLE_NEARBY_UNFOCUSED.getImage();
                nearByFontColor = buttonUnfocusedColor;
            }
        }
        else
        {
            nearByImage = ImageDecorator.IMG_POI_BUBBLE_NEARBY_DISABLED.getImage();
            nearByFontColor = buttonDisabledColor;
        }
        
        g.drawImage(nearByImage,
            (nearByRect.left + nearByRect.right) / 2, infoCommandBarTop,
            AbstractTnGraphics.HCENTER | AbstractTnGraphics.TOP);
        g.setColor(nearByFontColor);
        g.drawString(bundle.getString(IStringMap.RES_NEARBY, IStringMap.FAMILY_MAP),
            (nearByRect.left + nearByRect.right) / 2, commandTextTop,
            AbstractTnGraphics.HCENTER | AbstractTnGraphics.TOP);
        
        AbstractTnImage shareImage = null;
        int shareFontColor = -1;
        
        if(this.isShareEnabled())
        {
            if(this.isShareFocused)
            {
                shareImage = ImageDecorator.IMG_POI_BUBBLE_SHARE_FOCUSED.getImage();
                shareFontColor = buttonFocusedColor;
            }
            else
            {
                shareImage = ImageDecorator.IMG_POI_BUBBLE_SHARE_UNFOCUS.getImage();
                shareFontColor = buttonUnfocusedColor;
            }
        }
        else
        {
            shareImage = ImageDecorator.IMG_POI_BUBBLE_SHARE_DISABLED.getImage();
            shareFontColor = buttonDisabledColor;
        }
        
        g.drawImage(shareImage,
            (shareRect.left + shareRect.right) / 2, infoCommandBarTop,
            AbstractTnGraphics.HCENTER | AbstractTnGraphics.TOP);
        g.setColor(shareFontColor);
        g.drawString(bundle.getString(IStringMap.RES_SHARE, IStringMap.FAMILY_MAP),
            (shareRect.left + shareRect.right) / 2, commandTextTop,
            AbstractTnGraphics.HCENTER | AbstractTnGraphics.TOP);
        /***************************************************** End of second inner box ****************************************************/


    }

    protected void drawIcon(AbstractTnGraphics g)
    {
        AbstractTnImage image = null;

        if (isAddressFocused)
        {
            image = ImageDecorator.IMG_SELECTED_MAP_ADDRESS.getImage();
            g.drawImage(image, poiFocusedIconRect.left, poiFocusedIconRect.top, AbstractTnGraphics.LEFT | AbstractTnGraphics.TOP);
        }
        else
        {
            image = ImageDecorator.IMG_MAP_ADDRESS.getImage();
            g.drawImage(image, poiUnfocusedIconRect.left, poiUnfocusedIconRect.top, AbstractTnGraphics.LEFT | AbstractTnGraphics.TOP);
        }
    }

    protected void paint(AbstractTnGraphics g)
    {
        if (isAddressFocused)
        {
            drawInfo(g);
            drawIcon(g);
        }
        else
        {
            drawIcon(g);
        }
    }
    
    /**
     * 
     * @param isWidth
     * @return the width and height of the panel
     */
    private int getInfoPanelSize(boolean isWidth)
    {
        int result = 0;

        if (isWidth)
        {
            result = Math.min(AppConfigHelper.getScreenHeight(),
                AppConfigHelper.getDisplayWidth()) * 6083 / 10000;
        }
        else
        {
            result = Math.max(AppConfigHelper.getScreenHeight(),
                AppConfigHelper.getDisplayWidth()) * (291+5) / 1000;
        }
        return result;
    }

    public boolean isInside(int x, int y)
    {
        if (isAddressFocused)
        {
            if (isTapOnIcon(x, y) || isTapOnInfo(x, y) /*|| isTapOnPrevious(x, y) || isTapOnNext(x, y) */|| isTapSaveFavoriteIcon(x, y)
                    || isTapOnNearByIcon(x, y) || isTapOnShareIcon(x, y))
                return true;
        }
        else
        {
            if (isTapOnIcon(x, y))
                return true;
        }
        return false;
    }

    protected boolean isTapOnIcon(int x, int y)
    {
        if (isAddressFocused)
        {
            return poiFocusedIconRect.contains(x, y);
        }
        else
        {
            return poiUnfocusedIconRect.contains(x, y);
        }
    }

    public boolean isTapOnInfo(int x, int y)
    {
        return innerBoxRect.contains(x, y);
    }
    
    public boolean isTapSaveFavoriteIcon(int x, int y)
    {
        return saveFavoriteRect.contains(x, y);
    }

    public boolean isTapOnNearByIcon(int x, int y)
    {
        return nearByRect.contains(x, y);
    }

    public boolean isTapOnShareIcon(int x, int y)
    {
        return shareRect.contains(x, y);
    }

    public int getIndex()
    {
        return index;
    }

    public String getLabelForPaint(String label, int labelW, AbstractTnFont font, boolean isScrolling)
    {
        String tempLabel = label;
        if (!isScrolling)
        {
            if (font.stringWidth(tempLabel) > labelW)
            {
                int len = font.stringWidth("...");
                int i;
                for (i = 0; i < label.length(); i++)
                {
                    len += font.charWidth(label.charAt(i));
                    if (len >= labelW)
                    {
                        break;
                    }
                }
                tempLabel = label.substring(0, i);
                tempLabel += "...";
            }
        }
        return tempLabel;
    }
    //wrap one line to two lines
    public String[] wrapTwoLine(String strAddr, int labelW, AbstractTnFont font)
    {
    	String[] strLines = new String[2];
    	if(font.stringWidth(strAddr) < labelW)//can be show in one line, needn't wrap
    	{
    		strLines[0] = strAddr;
    		strLines[1] = null;
    	}
    	else
    	{
    		int lastNum = 0;// the position of last char in first line.
    		int cutPos = 0;// the position of cut off the address
    		int len = 0;
    		for (int i = 0; i < strAddr.length(); i++)
            {
                len += font.charWidth(strAddr.charAt(i));
                if (len >= labelW)
                {
                	lastNum = i;
                    break;
                }
            }
    		for(int j=lastNum; j>=0; j--)
    		{
    			if(strAddr.charAt(j) == ' ')
    			{
    				cutPos = j;
    				break;
    			}
    		}
    		if(cutPos > 0)
    		{
    			strLines[0] = strAddr.substring(0, cutPos);
    			strLines[1] = getLabelForPaint(strAddr.substring(cutPos+1),labelW, font, false);
    		}
    		else // the first word can't be show in one line
    		{
    			strLines[0] = getLabelForPaint(strAddr, labelW, font, false);
    			strLines[1] = null;
    		}
    	}
    	return strLines;
    }

    public boolean handleDownEvent(int insideX, int insideY)
    {
        this.isDrivetoFocused = false;
        this.isSaveFocused = false;
        this.isNearbyFocused = false;
        this.isShareFocused = false; //avoid some button in focus state(currently press a button and move out of screen)
    	
        if (this.getHeight() < insideY)
        {
            isAddressFocused = true;
        }
        
        if(!isAddressFocused)
        {
        	return false;
        }
        
        int x = insideX;
        int y = this.getHeight() - insideY;
        
    	if (isTapOnInfo(x, y))
        {
    		this.isDrivetoFocused = true;
    		update(this.getId());
            return true;
        }
        else if (isTapSaveFavoriteIcon(x, y))
        {
            this.isSaveFocused = true;
            update(this.getId());
            return true;
        }
        else if (isTapOnNearByIcon(x, y))
        {
            if(isNearByEnabled())
            {
                this.isNearbyFocused = true;
                update(this.getId());
            }
            return true;
        }
        else if (isTapOnShareIcon(x, y))
        {
            if(isShareEnabled())
            {
                this.isShareFocused = true;
                update(this.getId());
            }
            return true;
        }
        return false;
    }
    
    private void postCommandEvent(final int commandId)
    {
        ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance())
                .runOnUiThread(new Runnable()
                {
                    public void run()
                    {
                        TnUiEvent tnUiEvent = new TnUiEvent(TnUiEvent.TYPE_COMMAND_EVENT,
                                AddressAnnotation.this);
                        tnUiEvent.setCommandEvent(new TnCommandEvent(commandId));
                        AddressAnnotation.this.commandListener.handleUiEvent(tnUiEvent);
                        switch (commandId)
                        {
                            case IMapConstants.CMD_MAP_ADDRESS_CHANGE_TO_UNFOCUSED:
                            {
                                break;
                            }
                            case IMapConstants.CMD_MAP_ADDRESS_GOTO_NAV:
                            {
                                AddressAnnotation.this.isDrivetoFocused = false;
                                break;
                            }
                            case IMapConstants.CMD_MAP_ADDRESS_GOTO_SAVE_FAVORITE:
                            {
                                AddressAnnotation.this.requestPaint();
                                AddressAnnotation.this.isSaveFocused = false;
                                break;
                            }
                            case IMapConstants.CMD_MAP_ADDRESS_GOTO_ONE_SEARCH_BOX:
                            {
                                AddressAnnotation.this.isNearbyFocused = false;
                                break;
                            }
                            case IMapConstants.CMD_MAP_ADDRESS_GOTO_SHARE:
                            {
                                AddressAnnotation.this.isShareFocused = false;
                                break;
                            }
                            default:
                            {
                                break;
                            }
                        }
                    }
                });
    }
    
    public boolean handleClickEvent(int insideX, int insideY)
    {
        
        Logger.log(Logger.INFO, this.getClass().getName(), "inside(x,y)=" + "(" + insideX + "," + insideY);
        if(System.currentTimeMillis() - lastClickTime < 300)
        {
            return true;
        }
        else
        {
            lastClickTime = System.currentTimeMillis();
        }
        int x = insideX;
        if (this.getHeight() < insideY)
        {
            isAddressFocused = true;
        }
        int y = this.getHeight() - insideY;
        Logger.log(Logger.INFO, this.getClass().getName(), "top down(x,y)=" + "(" + x + "," + y);
        if (isAddressFocused)
        {
            this.isDrivetoFocused = false;
            this.isSaveFocused = false;
            this.isNearbyFocused = false;
            this.isShareFocused = false;
            
            if (isTapOnIcon(x, y))
            {
                Logger.log(Logger.INFO, this.getClass().getName(), "isTapOnIcon(x, y)");
                postCommandEvent(IMapConstants.CMD_MAP_ADDRESS_CHANGE_TO_UNFOCUSED);
                return true;
            }
            else if (isTapOnInfo(x, y))
            {
                Logger.log(Logger.INFO, this.getClass().getName(), "isTapOnInfo(x, y)");
                postCommandEvent(IMapConstants.CMD_MAP_ADDRESS_GOTO_NAV);
                return true;
            }
            else if (isTapSaveFavoriteIcon(x, y))
            {
                Logger.log(Logger.INFO, this.getClass().getName(), "isTapSaveFavoriteIcon(x, y)");
                postCommandEvent(IMapConstants.CMD_MAP_ADDRESS_GOTO_SAVE_FAVORITE);
                return true;
            }
            else if (isTapOnNearByIcon(x, y))
            {
                if(isNearByEnabled())
                {
                    Logger.log(Logger.INFO, this.getClass().getName(), "isTapOnNearByIcon(x, y)");
                    postCommandEvent(IMapConstants.CMD_MAP_ADDRESS_GOTO_ONE_SEARCH_BOX);
                }
                return true;
            }
            else if (isTapOnShareIcon(x, y))
            {
                if(isShareEnabled())
                {
                    Logger.log(Logger.INFO, this.getClass().getName(), "isTapOnShareIcon(x, y)");
                    postCommandEvent(IMapConstants.CMD_MAP_ADDRESS_GOTO_SHARE);
                }
                return true;
            }
            else
            {
                return false;
            }
        }
        else
        {
            postCommandEvent(IMapConstants.CMD_MAP_ADDRESS_CHANGE_TO_FOCUSED_START + index);
            return true;
        }
    }

    public boolean handleUpEvent(int insideX, int insideY)
    {
        this.isDrivetoFocused = false;
        this.isSaveFocused = false;
        this.isNearbyFocused = false;
        this.isShareFocused = false;
        update(this.getId());

        return true;
    }

    public boolean handleMoveEvent(int x, int y)
    {
        return false;
    }

    public AbstractTnImage createAnnotationImage()
    {
        String key = getPoiAnnotationImageKey();
        AbstractTnImage image = (AbstractTnImage)ImageCacheManager.getInstance().getMutableImageCache().get(key);
        if(isAddressFocused)
        {
            if(image == null)
            {
                image = AbstractTnUiHelper.getInstance().createImage(getWidth(), getHeight());
                ImageCacheManager.getInstance().getMutableImageCache().put(key, image);
            }
            image.clear(0x0);
            AbstractTnGraphics g = image.getGraphics();
            paint(g);
        }
        else
        {
            if(image == null)
            {
                image = AbstractTnUiHelper.getInstance().createImage(getWidth(), getHeight());
                AbstractTnGraphics g = image.getGraphics();
                paint(g);
                ImageCacheManager.getInstance().getMutableImageCache().put(key, image);
            }
        }
            
        return image;
    }

    private String getPoiAnnotationImageKey()
    {
        String addressKey = "";
        if(this.isAddressFocused)
        {
           addressKey = "addressPopupImageLandscape";
        }
        else
        {
            addressKey = "address";
        }
        return addressKey;
    }
    
    public int getWidth()
    {
        if (isAddressFocused)
        {
            return focusedWidth;
        }
        else
        {
            return unfocusedWidth;
        }
    }

    public int getHeight()
    {
        if (isAddressFocused)
        {
            return focusedHeight;
        }
        else
        {
            return unfocusedHeight;
        }
    }
    

    public MapViewUpdateAnnotationRequest.Builder updateGraphic()
    {
        layer = isAddressFocused ? ADDRESS_ANNOTATION_LAYER
                : IMapContainerConstants.NAV_SDK_Z_ORDER_POI_LAYER;
        String style = isAddressFocused ? POPUP_ADDRESS_ANNOTATION_STYLE
                : PIN_ADDRESS_ANNOTATION_STYLE;
        MapViewUpdateAnnotationRequest.Builder builder = MapViewUpdateAnnotationRequest.newBuilder();
        MapContainer mapContainer = MapContainer.getInstance();
        AbstractTnImage image = createAnnotationImage();
        graphicId = generateGraphicId();
        mapContainer.loadAsset(getViewName(), getWidth(), getHeight(), image, "" + graphicId);
        builder.setViewName(getViewName());
        builder.setAnnotationName("" + this.annotationId);
        builder.setPivotX(0.5f);
        builder.setPivotY(0);
        builder.setStyle(style);
        builder.setType(com.telenav.navsdk.events.MapViewData.AnnotationType.AnnotationType_Screen);
        builder.setAssetName("" + this.graphicId);
        builder.setZorder(layer);
        return builder;
    }

    public long addToMap()
    {
        int iconWidth = isAddressFocused ? this.focusedWidth : this.unfocusedWidth;
        int iconHeight = isAddressFocused ? this.focusedHeight : this.unfocusedHeight;
        
        MapContainer mapContainer = MapContainer.getInstance();
        AbstractTnImage image = createAnnotationImage();
        if (graphicId != -1)
        {
            mapContainer.unloadAsset("" + viewId, "" + graphicId);
        }
        graphicId = generateGraphicId();
        mapContainer.loadAsset("" + viewId, iconWidth, iconHeight, image, "" + graphicId);
        
        annotationId =  this.createAnnotation();
                
        return annotationId;
    }

    protected void initDefaultStyle()
    {
        // TODO Auto-generated method stub

    }

    public void update(final long componentId)
    {
        layer =  isAddressFocused ? ADDRESS_ANNOTATION_LAYER : IMapContainerConstants.NAV_SDK_Z_ORDER_POI_LAYER;
        MapContainer.getInstance().updateFeature(AddressAnnotation.this);
    }

    public void setFocused(boolean isAddressFocused)
    {
        this.isFavorite = DaoManager.getInstance().getAddressDao().isExistInFavoriteAddress(this.address, true);
        this.isAddressFocused = isAddressFocused;
        update(this.getId());
    }
    
    MapViewAddAnnotationRequest.Builder generaterBuilder()
    {
        layer = isAddressFocused ? ADDRESS_ANNOTATION_LAYER
                : IMapContainerConstants.NAV_SDK_Z_ORDER_POI_LAYER;
        String style = isAddressFocused ? POPUP_ADDRESS_ANNOTATION_STYLE
                : PIN_ADDRESS_ANNOTATION_STYLE;
        MapViewAddAnnotationRequest.Builder builder = MapViewAddAnnotationRequest
                .newBuilder();
        com.telenav.navsdk.events.PointOfInterestData.Location.Builder locationBuilder = com.telenav.navsdk.events.PointOfInterestData.Location
                .newBuilder();
        locationBuilder.setLatitude(lat);
        locationBuilder.setLongitude(lon);
        builder.setVisible(true);
        builder.setLocation(locationBuilder.build());
        builder.setPivotX(0.5f);
        builder.setPivotY(0);
        builder.setStyle(style);
        builder.setZorder(layer);
        builder.setClickable(true);
        builder.setWidth(getWidth());
        builder.setHeight(getHeight());
        builder.setType(com.telenav.navsdk.events.MapViewData.AnnotationType.AnnotationType_Screen);
        builder.setAssetName("" + this.graphicId);
        return builder;
    }

    public Address getAddress()
    {
        return address;
    }

    public boolean isSetFocused()
    {
        return isAddressFocused;
    }

	public double getLat()
	{
		return lat;
	}
	
	public double getLon()
	{
		return lon;
	}

    public long getGraphicId()
    {
        return this.graphicId;
    }

    public long getPickableIdNum()
    {
        return this.hashCode();
    }

    AbstractTnImage getImage()
    {
        return createAnnotationImage();
    }

    String getViewName()
    {
        return "" + this.viewId;
    }
    
    boolean isNearByEnabled()
    {
        boolean isConnected = NetworkStatusManager.getInstance().isConnected();
        
        return isConnected;
    }
    
    boolean isShareEnabled()
    {
        boolean isConnected = NetworkStatusManager.getInstance().isConnected();
        
        return isConnected;
    }
}
