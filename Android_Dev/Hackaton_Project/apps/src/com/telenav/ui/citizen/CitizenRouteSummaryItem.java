/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * CitizenRouteSummaryItem.java
 *
 */
package com.telenav.ui.citizen;

import com.telenav.module.nav.NavIconProvider;
import com.telenav.tnui.core.TnUiArgs.TnUiArgAdapter;
import com.telenav.tnui.graphics.AbstractTnFont;
import com.telenav.tnui.graphics.AbstractTnGraphics;
import com.telenav.tnui.graphics.AbstractTnImage;
import com.telenav.ui.ImageDecorator;
import com.telenav.ui.NinePatchImageDecorator;
import com.telenav.ui.UiStyleManager;

/**
 *@author yning
 *@date 2010-12-2
 */
public class CitizenRouteSummaryItem extends CitizenSummaryItem
{
    protected int turnType;
    
    protected String streetName;
    
    protected String distance;
    
    protected TnUiArgAdapter turnAreaWidthAdapter;
    
    protected TnUiArgAdapter turnIconSizeAdapter;
    
    protected TnUiArgAdapter distAreaWidthAdapter;
    
    protected TnUiArgAdapter streetPaddingAdapter;
    
    public CitizenRouteSummaryItem(int id, int turnType, String streetName, String dist, TnUiArgAdapter turnAreaWidthAdapter,
            TnUiArgAdapter turnIconSizeAdapter, TnUiArgAdapter distAreaWidthAdapter, TnUiArgAdapter streetPaddingAdapter)
    {
        super(id);
        this.turnType = turnType;
        this.distance = dist;
        this.streetName = streetName;
        this.turnAreaWidthAdapter = turnAreaWidthAdapter;
        this.turnIconSizeAdapter = turnIconSizeAdapter;
        this.distAreaWidthAdapter = distAreaWidthAdapter;
        this.streetPaddingAdapter = streetPaddingAdapter;
    }
    
    protected void paint(AbstractTnGraphics g)
    {
        int oldColor = g.getColor();
        AbstractTnFont oldFont = g.getFont();
        drawTurnIcon(g);
        drawStreetName(g);
        drawStreetLength(g);
        g.setFont(oldFont);
        g.setColor(oldColor);
    }
    
    private void drawTurnIcon(AbstractTnGraphics g)
    {
        int oldColor = g.getColor();
        int turnAreaWidth = turnAreaWidthAdapter.getInt();
        int turnIconWidth = turnIconSizeAdapter.getInt();
        int x = turnAreaWidth / 2;
        int y = getPreferredHeight() / 2;
        
        TnUiArgAdapter bg = NinePatchImageDecorator.ROUTE_ITEM_TURN_BG;
        if(isCarShown)
        {
            bg = NinePatchImageDecorator.ROUTE_ITEM_TURN_CURRENT_BG;
        }
        AbstractTnImage bgImage = bg.getImage();
        bgImage.setWidth(turnIconWidth);
        bgImage.setHeight(turnIconWidth);
        g.drawImage(bg.getImage(), x, y, AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);
        
        TnUiArgAdapter icon = NavIconProvider.getTurnIcon(turnType, false);
        if(icon != null)
        {
            g.drawImage(icon.getImage(), x, y, AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);
        }
        
        g.setColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_LI_GR));
        g.setStrokeWidth(0);
        g.drawLine(turnAreaWidth, 0, turnAreaWidth, getPreferredHeight());
        g.setColor(oldColor);
    }
    
    private void drawStreetName(AbstractTnGraphics g)
    {
        int oldColor = g.getColor();
        int prefH = getPreferredHeight();
        int carWidth = ImageDecorator.SUMMARY_CURRENT_SEGMENT_UNFOCUSED.getImage().getWidth();
        int padding = streetPaddingAdapter.getInt();
        int maxWidth = getPreferredWidth() - turnAreaWidthAdapter.getInt() - distAreaWidthAdapter.getInt() - padding * 2;
        
        AbstractTnFont streetNameFont = UiStyleManager.getInstance().getFont(UiStyleManager.FONT_ROUTE_SUMMARY_STREET_NAME);
        
        if(isCarShown)
        {
            maxWidth -= carWidth + padding;
        }
        
        int streetX = turnAreaWidthAdapter.getInt() + padding;
        int streetY = prefH / 2;
        
        g.setFont(streetNameFont);
        if(this.isFocused)
        {
            g.setColor(focusColor);
        }
        else
        {
            g.setColor(unfocusColor);
        }
        String displayedStr = getLabelForPaint(streetName, maxWidth, streetNameFont, false);
        g.drawString(displayedStr, streetX, streetY, AbstractTnGraphics.LEFT | AbstractTnGraphics.VCENTER);
        
        //draw the car.
        if(isCarShown)
        {
            int carX = getPreferredWidth() - distAreaWidthAdapter.getInt() - padding - carWidth;
            int carY = prefH / 2;
            g.drawImage(ImageDecorator.SUMMARY_CURRENT_SEGMENT_UNFOCUSED.getImage(), carX, carY, AbstractTnGraphics.LEFT | AbstractTnGraphics.VCENTER);
        }
        
        //draw line.
        g.setColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_LI_GR));
        int lineX = getPreferredWidth() - distAreaWidthAdapter.getInt();
        g.setStrokeWidth(0);
        g.drawLine(lineX, 0, lineX, getPreferredHeight());
        
        g.setColor(oldColor);
    }
    
    private void drawStreetLength(AbstractTnGraphics g)
    {
        int oldColor = g.getColor();
        int prefW = getPreferredWidth();
        int prefH = getPreferredHeight();
        AbstractTnFont streetLengthFont = UiStyleManager.getInstance().getFont(UiStyleManager.FONT_ROUTE_SUMMARY_STREET_LENGTH);
        int distX = prefW - distAreaWidthAdapter.getInt() / 2;
        int distY = prefH / 2;
        
        g.setFont(streetLengthFont);
        if(this.isFocused)
        {
            g.setColor(focusColor);
        }
        else
        {
            g.setColor(unfocusColor);
        }
        g.drawString(distance, distX, distY, AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);
        g.setColor(oldColor);
    }
    
    protected void initDefaultStyle()
    {
        
    }
    
    
}
