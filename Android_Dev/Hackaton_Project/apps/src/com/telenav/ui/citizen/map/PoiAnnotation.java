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
import com.telenav.data.datatypes.address.Address;
import com.telenav.data.datatypes.poi.Poi;
import com.telenav.data.serverproxy.NetworkStatusManager;
import com.telenav.i18n.ResourceBundle;
import com.telenav.logger.Logger;
import com.telenav.module.AppConfigHelper;
import com.telenav.module.map.IMapConstants;
import com.telenav.module.poi.PoiDataRequester;
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

public class PoiAnnotation extends AbstractAnnotation implements IAnnotation
{
    public static final int POI_POPUP_ANNOTATION_LAYER = IMapContainerConstants.NAV_SDK_Z_ORDER_USER_DEFINED;

    private static final String AD_ICON = "A";
    
    long annotationId;

    int layer;

    long viewId;

    private String brandName, firstLine, number;
    
    public String secondLine;//while street name, number is null, show city, province etc.

    private int index;

    private boolean hasPrevArrow, hasNextArrow;

    private int iconWidth, iconHeight;
    
//    private int graphicWidth , graphicHeight;
    
    private int iconFocusedWidth, iconFocusedHeight;

    private int arrowWidth;

    private int arrowHeight;

    private Address address;

    private boolean isSponsoredPoi = false;

    private int transparency = 0xFF000000;

    private AbstractTnFont titleFont = UiStyleManager.getInstance().getFont(UiStyleManager.FONT_POI_INFO_PANEL_TITLE);

    private AbstractTnFont contentFont = UiStyleManager.getInstance().getFont(UiStyleManager.FONT_POI_QUICK_FIND_BUTTON_TEXT);
    
    private AbstractTnFont iconNumFont = UiStyleManager.getInstance().getFont(UiStyleManager.FONT_POI_ICON_NUM);

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

    private TnRect prevArrowRect;

    private TnRect nextArrowRect;

    private TnRect infoPanelRect;

    private TnRect innerBoxRect;

    private TnRect moreInfoRect;

    private TnRect innerBox2Rect;

    private TnRect driveToRect;

    private TnRect callRect;

    private TnRect shareRect;

    private TnRect downArrowRect;

    private TnRect poiFocusedIconRect;

    private TnRect poiUnfocusedIconRect;

    private int focusedHeight;

    private int focusedWidth;

    private int unfocusedHeight;

    private int unfocusedWidth;

    private boolean isPoiFocused;

    private boolean hasAdPoi;
    
    private long lastClickTime;
    
    private boolean isGoDetailFocus = false;
    private boolean isDriveToFocus = false;
    private boolean isCallFocus = false;
    private boolean isShareFocus = false;
    private boolean isPreviousFocus = false;
    private boolean isNextFocus = false;
    
    private boolean isNumNeeded = true;
    
    boolean needMoreInfo = true;
    
    long graphicId = -1;
    
    final static int POI_BUBBLE_IMAGE_TOP_GAP = 2;
    
    final static String POPUP_POI_ANNOTATION_STYLE = "screen_annotations.poipopup_screen_annotation";
    final static String PIN_POI_ANNOTATION_STYLE = "screen_annotations.poipin_screen_annotation";
    boolean isPhoneNumberAvailable;
    
    public PoiAnnotation(Address address, int indexInSeq, boolean isPoiFocused, boolean hasAdPoi, int totalCount , boolean isNumNeeded)
    {
        super((int) ICommonConstants.ANOTATION_COMPONENT_TRANSLATE);
        this.address = address;
        this.isPoiFocused = isPoiFocused;
        this.hasAdPoi = hasAdPoi;
        this.viewId = MapContainer.getInstance().getViewId();
        if(address.getStop() != null)
        {
            this.lat = address.getStop().getLat() / 100000.0;
            this.lon = address.getStop().getLon() / 100000.0;
        }
        if (address.getType() == Address.TYPE_FAVORITE_POI || address.getType() == Address.TYPE_RECENT_POI)
        {
            if(address.getPoi() != null && address.getPoi().getBizPoi() != null)
            {
                this.brandName = address.getPoi().getBizPoi().getBrand();
            }
            if (address.getPoi() != null && address.getPoi().getType() == Poi.TYPE_SPONSOR_POI)
                this.isSponsoredPoi = true;
            else
                this.isSponsoredPoi = false;
        }
        if(address.getType() == Address.TYPE_RECENT_STOP || address.getType() == Address.TYPE_FAVORITE_STOP)
        {
            if(address.getStop() != null && address.getStop().getLabel() != null)
            {
                this.brandName = address.getLabel();
            }
        }
        
        this.firstLine = address.getStop().getFirstLine();
        this.secondLine = ResourceManager.getInstance().getStringConverter().convertSecondLine(address.getStop());

        this.number = "" + (indexInSeq + 1);
        
        this.index = indexInSeq % PoiDataRequester.DEFAULT_PAGE_SIZE;

        this.hasPrevArrow = index > 0 || (index == 0 && hasAdPoi && !isSponsoredPoi);
        this.hasNextArrow = (index < PoiDataRequester.DEFAULT_PAGE_SIZE - 1 && (indexInSeq < (totalCount - 1)))
                || (indexInSeq == 0 && hasAdPoi && isSponsoredPoi && totalCount > 0);// bug fix for TNANDROID-2693
        
        if(this.isSponsoredPoi)
        {
            this.number = AD_ICON;
        }
        
        this.iconWidth = ImageDecorator.IMG_MAP_POI.getImage().getWidth();
        this.iconHeight = ImageDecorator.IMG_MAP_POI.getImage().getHeight();
        this.iconFocusedWidth = ImageDecorator.IMG_SELECTED_MAP_POI.getImage().getWidth();
        this.iconFocusedHeight = ImageDecorator.IMG_SELECTED_MAP_POI.getImage().getHeight();
       
        iconHeightInInfoPanel = ImageDecorator.IMG_POI_BUBBLE_DRIVETO_UNFOCUSED.getImage().getHeight();
  
        int refPanelWidth = getInfoPanelSize(true);
        int refPanelHeight = getInfoPanelSize(false);
        
        int preMagicGap = 1;//hard code, to modify gap between pre and bubble container 
        int nextMagicGap = 0;//hard code, to modify gap between next and bubble container 
        
        
        //down arrow cover the bottom line of panel border.
        int downArrowCoverPanelBottomNum = 1;
        
        if(ISpecialImageRes.getOpenglImageFamily() == ISpecialImageRes.FAMILY_SMALL)
        {
            downArrowCoverPanelBottomNum = 4;
        }
        else if(ISpecialImageRes.getOpenglImageFamily() == ISpecialImageRes.FAMILY_MEDIUM)
        {
            downArrowCoverPanelBottomNum = 9;
        }
        else if(ISpecialImageRes.getOpenglImageFamily() == ISpecialImageRes.FAMILY_LARGE)
        {
            downArrowCoverPanelBottomNum = 9; 
        }

        
        arrowWidth = ImageDecorator.IMG_PREV_POI_UNFOCUS.getImage().getWidth();
        arrowHeight = ImageDecorator.IMG_PREV_POI_UNFOCUS.getImage().getHeight();
        int arrowLeft = preMagicGap;        

        int downArrowSpace = ImageDecorator.IMG_DOWN_ARROW.getImage().getHeight();
        
        //the info panel
        infoPanelWidth = refPanelWidth - arrowWidth * 2;
        infoPanelHeight = refPanelHeight - downArrowSpace - downArrowCoverPanelBottomNum;
        
        infoPanelHeight = infoPanelHeight - ImageDecorator.IMG_SELECTED_MAP_POI.getImage().getHeight();
        
        infoPanelLeft = arrowLeft + arrowWidth - preMagicGap;
        int panelTopGap = Math.max(AppConfigHelper.getScreenHeight(),
            AppConfigHelper.getDisplayWidth()) * 3 / 1000;
        infoPanelTop = downArrowCoverPanelBottomNum + panelTopGap;

        infoPanelRect = new TnRect(infoPanelLeft, infoPanelTop, infoPanelLeft + infoPanelWidth, infoPanelTop + infoPanelHeight);

        innerBoxWidth = infoPanelWidth * 8946 / 10000;
        innerBoxHeight = infoPanelHeight * 3850 / 10000;
        
        int innerBoxGap = (infoPanelHeight - innerBoxHeight * 2 - downArrowSpace + downArrowCoverPanelBottomNum) / 6;
        
        innerBoxLeft = (infoPanelWidth - innerBoxWidth) / 2 + infoPanelLeft;
        innerBoxTop = infoPanelTop + innerBoxGap + POI_BUBBLE_IMAGE_TOP_GAP;
        
        int leftRightArrowTop = (infoPanelTop + infoPanelHeight - arrowHeight) / 2;
        
        // the previous poi and next poi ear
        prevArrowRect = new TnRect(arrowLeft, leftRightArrowTop, arrowLeft+arrowWidth, leftRightArrowTop + arrowHeight);
        int nextArrowLeft = infoPanelLeft + infoPanelWidth - nextMagicGap;
        nextArrowRect = new TnRect(nextArrowLeft, leftRightArrowTop, nextArrowLeft + arrowWidth, leftRightArrowTop + arrowHeight);

        innerBoxLeft = (infoPanelWidth -  innerBoxWidth) / 2 + infoPanelLeft;
        innerBoxRect = new TnRect(innerBoxLeft, innerBoxTop, innerBoxLeft + innerBoxWidth, innerBoxTop + innerBoxHeight);

        int moreInfoWidth = 0;
        int moreInfoHeight = 0;
        int moreInfoLeft = 0;
        int moreInfoTop = 0;
        needMoreInfo = ImageDecorator.IMG_POI_BUBBLE_DETAIL_ARROW_UNFOCUS.getImage() != null;
        if (needMoreInfo)
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

        int downArrowTop = infoPanelTop + infoPanelHeight - downArrowCoverPanelBottomNum;
        if(ISpecialImageRes.getOpenglImageFamily() == ISpecialImageRes.FAMILY_SMALL)
        {
            downArrowTop = downArrowTop - 5;
        }
        downArrowRect = new TnRect(downArrowLeft, downArrowTop, downArrowLeft + downArrowWidth, downArrowTop + downArrowHeight);
        
        String phoneNumber = address.getPhoneNumber();
        isPhoneNumberAvailable = true;
        if(phoneNumber == null || phoneNumber.trim().length() == 0)
        {
            isPhoneNumberAvailable = false;
        }
        
        int iconCount = 3;//always 3 because phone is always there
        int step = innerBox2Width / iconCount;
        int offset = innerBox2Width - step * iconCount;

        callRect = new TnRect(innerBox2Left, innerBox2Top, innerBox2Left + step, innerBox2Top + innerBox2Height);
    
        shareRect = new TnRect(innerBox2Left + step, innerBox2Top, innerBox2Left + 2 * step + offset, innerBox2Top + innerBox2Height);
        
        driveToRect = new TnRect(innerBox2Left + 2 * step, innerBox2Top, innerBox2Left + 3 * step + offset, innerBox2Top + innerBox2Height);

        poiIconTop = infoPanelRect.bottom + downArrowSpace - downArrowCoverPanelBottomNum;

        focusedWidth = infoPanelWidth + 2 * arrowWidth ;
        focusedHeight = poiIconTop + iconFocusedHeight;
        poiFocusedIconRect = new TnRect(refPanelWidth / 2 - iconWidth / 2, poiIconTop,
            infoPanelRect.left + infoPanelRect.width() / 2 + iconFocusedWidth / 2, poiIconTop + iconFocusedHeight);

        unfocusedHeight = iconHeight;
        unfocusedWidth = iconWidth;
        poiUnfocusedIconRect = new TnRect(0, 0, iconWidth, iconHeight);
        this.isNumNeeded = isNumNeeded;
    }
    
    public PoiAnnotation(Address address, int indexInSeq, boolean isPoiFocused,
            boolean hasAdPoi, int totalCount)
    {
        this(address, indexInSeq, isPoiFocused, hasAdPoi, totalCount, true);
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
                AppConfigHelper.getDisplayWidth()) * 8375 / 10000;
        }
        else
        {
            result = Math.max(AppConfigHelper.getScreenHeight(),
                AppConfigHelper.getDisplayWidth()) * (288+6) / 1000;
        }
        return result;
    }

    protected void drawInfo(AbstractTnGraphics g)
    {
        // draw the whole info panel box background
        TnNinePatchImage infoPanelNinePatchImage = (TnNinePatchImage) NinePatchImageDecorator.instance
                .decorate(NinePatchImageDecorator.POI_BUBBLE_BG);
        if (infoPanelNinePatchImage != null)
        {
            infoPanelNinePatchImage.setWidth(infoPanelRect.width());
            infoPanelNinePatchImage.setHeight(infoPanelRect.height());
            g.drawImage(infoPanelNinePatchImage, infoPanelRect.left, infoPanelRect.top,
                AbstractTnGraphics.LEFT | AbstractTnGraphics.TOP);
        }
        
        /***************************************************** Start of first inner box ****************************************************/
        if(this.isGoDetailFocus)//when first inner box on focus
        {
        	// draw the first inner background
            TnNinePatchImage innerBoxNinePatchImage = (TnNinePatchImage) NinePatchImageDecorator.instance
                    .decorate(NinePatchImageDecorator.POI_BUBBLE_POI_TITLE_FOCUSED);
            innerBoxNinePatchImage.setWidth(innerBoxRect.width());
            innerBoxNinePatchImage.setHeight(innerBoxRect.height());
            g.drawImage(innerBoxNinePatchImage, innerBoxRect.left, innerBoxRect.top, AbstractTnGraphics.LEFT | AbstractTnGraphics.TOP);

            // draw the content of the first inner box
            g.setColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_BUBBLE_TITLE_FOCUSED));
            g.setFont(titleFont);
            int textLeft = innerBoxRect.left + infoPanelWidth * 10 / 197;

            int strWidthLimited = (needMoreInfo ? moreInfoRect.left : innerBoxRect.right) - textLeft;
            int textTopGap = (innerBoxRect.height() - titleFont.getHeight() - contentFont.getHeight()) / 3 + 4;
            
            if (brandName != null && brandName.length() > 0)
            {
                String str = getLabelForPaint(brandName, strWidthLimited, titleFont, false);
                g.drawString(str, textLeft, innerBoxRect.top + textTopGap, AbstractTnGraphics.LEFT
                        | AbstractTnGraphics.TOP);

                // draw the small "more" icon in innerbox
                if (needMoreInfo)
                {
                    g.drawImage(ImageDecorator.IMG_POI_BUBBLE_DETAIL_ARROW_FOCUSED.getImage(), moreInfoRect.left, moreInfoRect.top, AbstractTnGraphics.LEFT
                        | AbstractTnGraphics.TOP);
                }

                //g.setColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_BUBBLE_CONTENT));
                
                g.setColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_BUBBLE_CONTENT_FOCUSED));
                g.setFont(contentFont);
                if(firstLine.length() > 0)//show street name.
                {                
                    str = getLabelForPaint(firstLine, strWidthLimited, contentFont, false);
                }
                else //no street name, show city and province.
                {
                    str = getLabelForPaint(secondLine, strWidthLimited, contentFont, false);
                }
                g.drawString(str, textLeft, innerBoxRect.top + textTopGap * 2
                        + titleFont.getHeight(), AbstractTnGraphics.LEFT
                        | AbstractTnGraphics.TOP);
            }
            else
            {
                g.setFont(titleFont);
                String str = null;
                if(firstLine.length() > 0)
                {
                    str = getLabelForPaint(firstLine, strWidthLimited, titleFont, false);
                }
                else
                {
                    str = getLabelForPaint(secondLine, strWidthLimited, titleFont, false);
                }          
                g.drawString(str, textLeft, innerBoxRect.top + innerBoxRect.height() / 2
                        - contentFont.getHeight() / 2, AbstractTnGraphics.LEFT
                        | AbstractTnGraphics.TOP);
            }
        }
        else  ////when first inner box unfocus
        {
            TnNinePatchImage innerBoxNinePatchImage = (TnNinePatchImage) NinePatchImageDecorator.instance
                    .decorate(NinePatchImageDecorator.POI_BUBBLE_POI_TITLE_UNFOCUS);
            if (innerBoxNinePatchImage != null)
            {
                innerBoxNinePatchImage.setWidth(innerBoxRect.width());
                innerBoxNinePatchImage.setHeight(innerBoxRect.height());
                g.drawImage(innerBoxNinePatchImage, innerBoxRect.left, innerBoxRect.top,
                    AbstractTnGraphics.LEFT | AbstractTnGraphics.TOP);
            }
            // draw the content of the first inner box
            g.setColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_BUBBLE_TITLE_UNFOCUSED));
            g.setFont(titleFont);
            int textLeft = innerBoxRect.left + infoPanelWidth * 10 / 197;
            int strWidthLimited = (needMoreInfo ? moreInfoRect.left : innerBoxRect.right) - textLeft;

            int textTopGap = (innerBoxRect.height() - titleFont.getHeight() - contentFont.getHeight()) / 3 + 4;
            
            if (brandName != null && brandName.length() > 0)
            {
                String str = getLabelForPaint(brandName, strWidthLimited, titleFont, false);
                g.drawString(str, textLeft, innerBoxRect.top + textTopGap, AbstractTnGraphics.LEFT
                        | AbstractTnGraphics.TOP);

                // draw the small "more" icon in innerbox
                if (needMoreInfo)
                {
                    g.drawImage(ImageDecorator.IMG_POI_BUBBLE_DETAIL_ARROW_UNFOCUS.getImage(), moreInfoRect.left, moreInfoRect.top, AbstractTnGraphics.LEFT
                            | AbstractTnGraphics.TOP);
                }
                g.setColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_BUBBLE_CONTENT_UNFOCUSED));
                g.setFont(contentFont);
                if(firstLine.length() > 0)//show street name.
                {                
                    str = getLabelForPaint(firstLine, strWidthLimited, contentFont, false);
                }
                else //no street name, show city and province.
                {
                    str = getLabelForPaint(secondLine, strWidthLimited, contentFont, false);
                }
                g.drawString(str, textLeft, innerBoxRect.top + textTopGap * 2
                        + titleFont.getHeight(), AbstractTnGraphics.LEFT
                        | AbstractTnGraphics.TOP);
            }
            else
            {
                g.setColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_BUBBLE_TITLE_UNFOCUSED));
                g.setFont(titleFont);
                String str = null;
                if(firstLine.length() > 0)
                {
                    str = getLabelForPaint(firstLine, strWidthLimited, titleFont, false);
                }
                else
                {
                    str = getLabelForPaint(secondLine, strWidthLimited, titleFont, false);
                }          
                g.drawString(str, textLeft, innerBoxRect.top + innerBoxRect.height() / 2
                        - contentFont.getHeight() / 2, AbstractTnGraphics.LEFT
                        | AbstractTnGraphics.TOP);
            }
        }
        

        // draw the down arrow image
        g.drawImage(ImageDecorator.IMG_DOWN_ARROW.getImage(), downArrowRect.left, downArrowRect.top, AbstractTnGraphics.LEFT
                | AbstractTnGraphics.TOP);

        /***************************************************** End of first inner box ****************************************************/

        /***************************************************** Start of second inner box ****************************************************/
        // draw the second-first inner background
        TnNinePatchImage innerBox2NinePatchImage = null;
        int commandIconTopMargin = (innerBox2Rect.height() - iconHeightInInfoPanel -  contentFont.getHeight()) / 2;
        infoCommandBarTop = innerBox2Rect.top + commandIconTopMargin;
        g.setFont(contentFont);
        int commandTextTop =  infoCommandBarTop + iconHeightInInfoPanel  ;
        ResourceBundle bundle = ResourceManager.getInstance().getCurrentBundle();
        
        int buttonFocusedColor = UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_BUBBLE_BUTTON_FOCUSED);
        int buttonUnfocusedColor = UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_BUBBLE_BUTTON_UNFOCUSED);
        int buttonDisableColor = UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_BUBBLE_BUTTON_DISABLED);
     
        if(this.isDriveToFocus)//TODO it's focus VDD
        {
        	g.drawImage(ImageDecorator.IMG_POI_BUBBLE_DRIVETO_FOCUSED.getImage(), (driveToRect.left + driveToRect.right) / 2, infoCommandBarTop,
                    AbstractTnGraphics.HCENTER | AbstractTnGraphics.TOP);
        	g.setColor(buttonFocusedColor);
        	g.drawString(bundle.getString(IStringMap.RES_DRIVE, IStringMap.FAMILY_MAP), (driveToRect.left + driveToRect.right) / 2,
        	            commandTextTop, AbstractTnGraphics.HCENTER | AbstractTnGraphics.TOP);
        }
        else
        {
        	g.drawImage(ImageDecorator.IMG_POI_BUBBLE_DRIVETO_UNFOCUSED.getImage(), (driveToRect.left + driveToRect.right) / 2, infoCommandBarTop,
                    AbstractTnGraphics.HCENTER | AbstractTnGraphics.TOP);
        	g.setColor(buttonUnfocusedColor);
        	g.drawString(bundle.getString(IStringMap.RES_DRIVE, IStringMap.FAMILY_MAP), (driveToRect.left + driveToRect.right) / 2,
        	            commandTextTop, AbstractTnGraphics.HCENTER | AbstractTnGraphics.TOP);
        }
        	
        AbstractTnImage shareImage = null;
        int shareFontColor = -1;
        
        if(isShareEnabled())
        {
            if(this.isShareFocus)
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
            shareFontColor = buttonDisableColor;
        }
        
        g.drawImage(shareImage, (shareRect.left + shareRect.right) / 2,
            infoCommandBarTop, AbstractTnGraphics.HCENTER | AbstractTnGraphics.TOP);
        g.setColor(shareFontColor);
        g.drawString(bundle.getString(IStringMap.RES_SHARE, IStringMap.FAMILY_MAP), (shareRect.left + shareRect.right) / 2,
            commandTextTop, AbstractTnGraphics.HCENTER | AbstractTnGraphics.TOP);
        
        if(isPhoneNumberAvailable)
        {
            if(this.isCallFocus)
            {
                g.drawImage(ImageDecorator.IMG_POI_BUBBLE_CALL_FOCUSED.getImage(), (callRect.left + callRect.right) / 2, infoCommandBarTop,
                        AbstractTnGraphics.HCENTER | AbstractTnGraphics.TOP);
                g.setColor(buttonFocusedColor);
                g.drawString(bundle.getString(IStringMap.RES_CALL, IStringMap.FAMILY_MAP), (callRect.left + callRect.right) / 2, commandTextTop,
                        AbstractTnGraphics.HCENTER | AbstractTnGraphics.TOP);
            }
            else
            {
                g.drawImage(ImageDecorator.IMG_POI_BUBBLE_CALL_UNFOCUS.getImage(), (callRect.left + callRect.right) / 2, infoCommandBarTop,
                        AbstractTnGraphics.HCENTER | AbstractTnGraphics.TOP);
                g.setColor(buttonUnfocusedColor);
                g.drawString(bundle.getString(IStringMap.RES_CALL, IStringMap.FAMILY_MAP), (callRect.left + callRect.right) / 2, commandTextTop,
                        AbstractTnGraphics.HCENTER | AbstractTnGraphics.TOP);
            }
        }
        else //disabled phone number
        {
            g.drawImage(ImageDecorator.IMG_POI_BUBBLE_CALL_DISABLED.getImage(), (callRect.left + callRect.right) / 2, infoCommandBarTop,
                AbstractTnGraphics.HCENTER | AbstractTnGraphics.TOP);
            g.setColor(UiStyleManager.getInstance().getColor(
                UiStyleManager.TEXT_ITEM_DISABLE_COLOR));
            g.drawString(bundle.getString(IStringMap.RES_CALL, IStringMap.FAMILY_MAP), (callRect.left + callRect.right) / 2, commandTextTop,
                AbstractTnGraphics.HCENTER | AbstractTnGraphics.TOP);
        }

        /***************************************************** End of second inner box ****************************************************/

        if (hasPrevArrow)
        {
    		 g.drawImage(isPreviousFocus ? ImageDecorator.IMG_PREV_POI_FOCUSED.getImage():ImageDecorator.IMG_PREV_POI_UNFOCUS.getImage(), prevArrowRect.left, prevArrowRect.top, AbstractTnGraphics.LEFT
                     | AbstractTnGraphics.TOP);
        }
        if (hasNextArrow)
        {
    		g.drawImage(isNextFocus ? ImageDecorator.IMG_NEXT_POI_FOCUSED.getImage():ImageDecorator.IMG_NEXT_POI_UNFOCUS.getImage(), nextArrowRect.left, nextArrowRect.top, AbstractTnGraphics.LEFT
                    | AbstractTnGraphics.TOP);
        }
    }

    protected void drawIcon(AbstractTnGraphics g)
    {
        AbstractTnImage image = null;

        int iconCenterY = 13;
        int magicNumX = 0;
        if(ISpecialImageRes.getSpecialImageFamily() == ISpecialImageRes.FAMILY_MEDIUM)
        {
            iconCenterY = 13;
            magicNumX = 1; // the medium one number icon isn't symmetry.

        }
        else if(ISpecialImageRes.getSpecialImageFamily() == ISpecialImageRes.FAMILY_VAST)
        {
            iconCenterY = 24;
            magicNumX = 1; // the medium one number icon isn't symmetry.
        }
        
        if (ISpecialImageRes.isNeedStretch)
        {
        	iconCenterY = iconCenterY * ISpecialImageRes.numerator / ISpecialImageRes.denominator;
        }
        int iconNumX = 0;
        if (isPoiFocused)
        {
            if (!isNumNeeded)
            {
                image = ImageDecorator.IMG_SELECTED_MAP_ADDRESS.getImage();
                iconNumX = poiUnfocusedIconRect.left + (poiUnfocusedIconRect.width()) / 2;
            }
            else
            {
                if(AD_ICON.equalsIgnoreCase(number))
                {
                    image = ImageDecorator.IMG_MAP_AD_POI.getImage();
                }
                else
                { 
                    image = ImageDecorator.IMG_SELECTED_MAP_POI.getImage();
                }
                iconNumX = poiFocusedIconRect.left + iconWidth / 2 - magicNumX;
            }
            g.drawImage(image, poiFocusedIconRect.left, poiFocusedIconRect.top, AbstractTnGraphics.LEFT | AbstractTnGraphics.TOP);

            g.setColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_MAP_POI_ANNOTATION_COLOR) | transparency);
            g.setFont(iconNumFont);
            if (isNumNeeded)
            {
                if(AD_ICON.equalsIgnoreCase(number))
                {
                    g.setColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_WH));
                }
                g.drawString(number, iconNumX, poiFocusedIconRect.top + iconCenterY,
                    AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);
            }
        }
        else
        {
            
            if (!isNumNeeded)
            {
                
                image = ImageDecorator.IMG_MAP_ADDRESS.getImage();
                iconNumX = poiUnfocusedIconRect.left + (poiUnfocusedIconRect.width()) / 2;
            }
            else
            {
                if(AD_ICON.equalsIgnoreCase(number))
                {
                    image = ImageDecorator.IMG_MAP_AD_POI.getImage();
                }
                else
                { 
                    image = ImageDecorator.IMG_MAP_POI.getImage();
                }
                iconNumX = poiUnfocusedIconRect.left + (poiUnfocusedIconRect.width()) / 2 - magicNumX;
            }

            g.drawImage(image, poiUnfocusedIconRect.left, poiUnfocusedIconRect.top, AbstractTnGraphics.LEFT | AbstractTnGraphics.TOP);
            g.setColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_WH));
            g.setFont(iconNumFont);
            if (isNumNeeded)
            {
                g.drawString(number, iconNumX, poiUnfocusedIconRect.top + iconCenterY,
                    AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);
            }
        }
    }

    protected void paint(AbstractTnGraphics g)
    {
        if (isPoiFocused)
        {
            drawInfo(g);
        }    
        drawIcon(g);     
    }

    public boolean isInside(int x, int y)
    {
        if (isPoiFocused)
        {
            if (isTapOnIcon(x, y) || isTapOnInfo(x, y) || isTapOnPrevious(x, y) || isTapOnNext(x, y) || isTapOnDriveToIcon(x, y)
                    || isTapOnCallIcon(x, y) || isTapOnShareIcon(x, y))
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
        if (isPoiFocused)
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

    public boolean isTapOnPrevious(int x, int y)
    {
        return hasPrevArrow && prevArrowRect.contains(x, y);
    }

    public boolean isTapOnNext(int x, int y)
    {
        return hasNextArrow && nextArrowRect.contains(x, y);
    }

    public boolean isTapOnDriveToIcon(int x, int y)
    {
        return driveToRect.contains(x, y);
    }

    public boolean isTapOnCallIcon(int x, int y)
    {
        if(!this.isPhoneNumberAvailable)
        {
            return false;
        }
        return callRect.contains(x, y);
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

    public boolean handleDownEvent(int insideX, int insideY)
    {
        this.isGoDetailFocus = false;
        this.isDriveToFocus = false;
        this.isCallFocus = false;
        this.isShareFocus = false;
        this.isPreviousFocus = false;
        this.isNextFocus = false;//avoid some button in focus state(currently press a button and move out of screen)
    	
    	if (this.getHeight() >= insideY)
        {
    		isPoiFocused = true;
        }
    	
    	if(!isPoiFocused)
    	{
    		return false;
    	}
    	
        int x = insideX;
        int y = this.getHeight() - insideY;
        
    	if (isTapOnInfo(x, y))
		{
			this.isGoDetailFocus = true;
			update(this.getId());
			return true;
		} 
		else if (isTapOnDriveToIcon(x, y)) 
		{
			this.isDriveToFocus = true;
			update(this.getId());
			return true;
		} 
		else if (isTapOnCallIcon(x, y)) 
		{
			this.isCallFocus = true;
			update(this.getId());
			return true;
		} 
		else if (isTapOnShareIcon(x, y)) 
		{
		    if(isShareEnabled())
		    {
		        this.isShareFocus = true;
		        update(this.getId());
		    }
			return true;
		} 
		else if (isTapOnPrevious(x, y)) 
		{
			this.isPreviousFocus = true;
			update(this.getId());
			return true;
		} 
		else if (isTapOnNext(x, y)) 
		{
			this.isNextFocus = true;
			update(this.getId());
			return true;
		}
		return false;
    }

    public boolean handleUpEvent(int insideX, int insideY)
    {
	
        this.isGoDetailFocus = false;
        this.isDriveToFocus = false;
        this.isCallFocus = false;
        this.isShareFocus = false;
        this.isPreviousFocus = false;
        this.isNextFocus = false;
        update(this.getId());
		return true;
    }

    public boolean handleMoveEvent(int x, int y)
    {
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
                                PoiAnnotation.this);
                        tnUiEvent.setCommandEvent(new TnCommandEvent(commandId));
                        PoiAnnotation.this.commandListener.handleUiEvent(tnUiEvent);
                        
                        switch (commandId)
                        {
                            case IMapConstants.CMD_MAP_POI_CHANGE_TO_UNFOCUSED:
                            {
                                break;
                            }
                            case IMapConstants.CMD_MAP_POI_GOTO_POI_DETAIL:
                            {
                            	PoiAnnotation.this.isGoDetailFocus = false;
                                break;
                            }
                            case IMapConstants.CMD_MAP_POI_GOTO_NAV:
                            {
                            	PoiAnnotation.this.isDriveToFocus = false;
                                break;
                            }
                            case IMapConstants.CMD_MAP_POI_GOTO_CALL:
                            {
                            	PoiAnnotation.this.isCallFocus = false;
                                break;
                            }
                            case IMapConstants.CMD_MAP_POI_GOTO_SHARE:
                            {
                            	PoiAnnotation.this.isShareFocus = false;
                                break;
                            }
                            case IMapConstants.CMD_MAP_POI_PREV:
                            {
                                PoiAnnotation.this.isPreviousFocus = false;
                                break;
                            }
                            case IMapConstants.CMD_MAP_POI_NEXT:
                            {
                                PoiAnnotation.this.isNextFocus = false;
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
            isPoiFocused = true;
        }
        int y = this.getHeight() - insideY;
        Logger.log(Logger.INFO, this.getClass().getName(), "top down(x,y)=" + "(" + x + "," + y);
        if (isPoiFocused)
        {
            this.isGoDetailFocus = false;
            this.isDriveToFocus = false;
            this.isCallFocus = false;
            this.isShareFocus = false;
            this.isPreviousFocus = false;
            this.isNextFocus = false;
            
            if (isTapOnIcon(x, y))
            {
                Logger.log(Logger.INFO, this.getClass().getName(), "isTapOnIcon(x, y)");
                postCommandEvent(IMapConstants.CMD_MAP_POI_CHANGE_TO_UNFOCUSED);
                return true;
            }
            else if (isTapOnInfo(x, y))
            {
                Logger.log(Logger.INFO, this.getClass().getName(), "isTapOnInfo(x, y)");
                postCommandEvent(IMapConstants.CMD_MAP_POI_GOTO_POI_DETAIL);
                this.isGoDetailFocus = false;
                return true;
            }
            else if (isTapOnDriveToIcon(x, y))
            {
                Logger.log(Logger.INFO, this.getClass().getName(), "isTapOnDriveToIcon(x, y)");
                postCommandEvent(IMapConstants.CMD_MAP_POI_GOTO_NAV);
                this.isDriveToFocus = false;
                return true;
            }
            else if (isTapOnCallIcon(x, y))
            {
                Logger.log(Logger.INFO, this.getClass().getName(), "isTapOnCallIcon(x, y)");
                postCommandEvent(IMapConstants.CMD_MAP_POI_GOTO_CALL);
                this.isCallFocus = false;
                return true;
            }
            else if (isTapOnShareIcon(x, y))
            {
                if(isShareEnabled())
                {
                    Logger.log(Logger.INFO, this.getClass().getName(), "isTapOnShareIcon(x, y)");
                    postCommandEvent(IMapConstants.CMD_MAP_POI_GOTO_SHARE);
                    this.isShareFocus = false;
                }
                return true;
            }
            else if (isTapOnPrevious(x, y))
            {
                Logger.log(Logger.INFO, this.getClass().getName(), "isTapOnPrevious(x, y)");
                postCommandEvent(IMapConstants.CMD_MAP_POI_PREV);
                this.isPreviousFocus = false;
                return true;
            }
            else if (isTapOnNext(x, y))
            {
                Logger.log(Logger.INFO, this.getClass().getName(), "isTapOnNext(x, y)");
                postCommandEvent(IMapConstants.CMD_MAP_POI_NEXT);
                this.isNextFocus = false;
                return true;
            }
            else
            {
                return false;
            }
        }
        else
        {
            if(this.hasAdPoi)
            {
                if(this.isSponsoredPoi)
                	postCommandEvent(IMapConstants.CMD_MAP_POI_CHANGE_TO_FOCUSED_START + 0);
                else
                	postCommandEvent(IMapConstants.CMD_MAP_POI_CHANGE_TO_FOCUSED_START + index + 1);
            }
            else
            {
            	postCommandEvent(IMapConstants.CMD_MAP_POI_CHANGE_TO_FOCUSED_START + index);
            }
            return true;
        }
    }
    
    public void setTransparency(int t)
    {
        this.transparency = t;
    }

    public AbstractTnImage createAnnotationImage()
    {
        String key = getPoiAnnotationImageKey();
        AbstractTnImage image;
        image = (AbstractTnImage)ImageCacheManager.getInstance().getMutableImageCache().get(key);
        if(isPoiFocused)
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
                Logger.log(Logger.INFO, this.getClass().getName(), "width=" + getWidth() + ";height=" + getHeight());
                image = AbstractTnUiHelper.getInstance().createImage(getWidth(), getHeight());
                ImageCacheManager.getInstance().getMutableImageCache().put(key, image);
            }
            image.clear(0x0);
            AbstractTnGraphics g = image.getGraphics();
            paint(g);
        }
        return image;
    }

    private String getPoiAnnotationImageKey()
    {
        String poiKey = "";
        if(isPoiFocused)
        {
            if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
            {
                poiKey = "poiPopupImagePortait";
            }
            else
            {
                poiKey = "poiPopupImageLandscape";
            }
            
        }
        else
        {
            poiKey = "poi-"+ (isSponsoredPoi ? 11:index);
        }
        return poiKey;
    }
    
    public boolean isSponsoredPoi()
    {
        return isSponsoredPoi;
    }
    
    public int getWidth()
    {
        if (isPoiFocused)
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
        if (isPoiFocused)
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
        layer = isPoiFocused ? POI_POPUP_ANNOTATION_LAYER
                : IMapContainerConstants.NAV_SDK_Z_ORDER_POI_LAYER;
        String style = isPoiFocused ? POPUP_POI_ANNOTATION_STYLE
                : PIN_POI_ANNOTATION_STYLE;
        
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
        builder.setZorder(layer);
        builder.setAssetName("" + this.graphicId);
        return builder;
    }

    public long addToMap()
    {
        MapContainer mapContainer = MapContainer.getInstance();
        AbstractTnImage image = createAnnotationImage();
        if (graphicId != -1)
        {
            mapContainer.unloadAsset("" + viewId, "" + graphicId);
        }
        graphicId = generateGraphicId();
        mapContainer.loadAsset("" + viewId, getWidth(), getHeight(), image, "" + graphicId);
        
        annotationId =  this.createAnnotation();
        return annotationId;
    }

    protected void initDefaultStyle()
    {
        // TODO Auto-generated method stub

    }

    public void update(final long componentId)
    {
        if (componentId <= 0)
        {
            return;
        }
        layer = isPoiFocused ? POI_POPUP_ANNOTATION_LAYER
                : IMapContainerConstants.NAV_SDK_Z_ORDER_POI_LAYER;
        MapContainer.getInstance().updateFeature(PoiAnnotation.this);
    }

    public void setFocused(boolean isPoiFocused)
    {
        if (this.isPoiFocused == isPoiFocused)
        {
            return;
        }
        else
        {
            this.isPoiFocused = isPoiFocused;
            update(this.getId());
        }
    }

    public Address getAddress()
    {
        return address;
    }

    public boolean isSetFocused()
    {
        return isPoiFocused;
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

    MapViewAddAnnotationRequest.Builder generaterBuilder()
    {
        layer = isPoiFocused ? POI_POPUP_ANNOTATION_LAYER
                : IMapContainerConstants.NAV_SDK_Z_ORDER_POI_LAYER;
        String style = isPoiFocused ? POPUP_POI_ANNOTATION_STYLE
                : PIN_POI_ANNOTATION_STYLE;
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

    String getViewName()
    {
        return "" + this.viewId;
    }
    
    boolean isShareEnabled()
    {
        boolean isConnected = NetworkStatusManager.getInstance().isConnected();
        
        return isConnected;
    }
}
