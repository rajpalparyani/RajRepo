/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * SplashBackroundComopnent.java
 *
 */
package com.telenav.module.entry;

import com.telenav.tnui.graphics.AbstractTnFont;
import com.telenav.tnui.graphics.AbstractTnGraphics;
import com.telenav.tnui.graphics.AbstractTnImage;
import com.telenav.tnui.graphics.TnRect;
import com.telenav.ui.citizen.android.AndroidCitizenUiHelper;
import com.telenav.ui.frogui.widget.FrogImageComponent;

/**
 *@author bduan
 *@date Dec 1, 2012
 */
public class SplashBackroundComopnent extends FrogImageComponent
{
    private static final int CAUTION_FONT_COLOR = 0xFF333333;
    private static final int VERSION_FONT_COLOR = 0xFFe6e6e6;

    private AbstractTnImage scaledImage;
    private AbstractTnFont font;
    
    public SplashBackroundComopnent(AbstractTnImage scaledImage, AbstractTnFont font)
    {
        super(0, null);
        this.scaledImage = scaledImage;
        this.font = font;
    }

    protected void paint(AbstractTnGraphics graphics)
    {
        try
        {
            paintSplashScreen(graphics);
        }
        catch (Throwable t)
        {
            
        }
    }
    
    private void paintSplashScreen(AbstractTnGraphics graphics)
    {
        int width = Math.min(this.getWidth(), this.getHeight());
        int height = Math.max(this.getWidth(), this.getHeight());

        int fontDecent = font.getDescent();
        int horizontalMargin = font.charWidth('o');
        int bottomMargin = fontDecent;
        int lineGap = 2;
        int fontHeight = font.getHeight();
        int lineHeight = fontHeight + lineGap;
        int baseY = height - bottomMargin - fontDecent;// use baseline as the bottom level

        // draw bg img.
        if (scaledImage != null)
        {
            graphics.drawImage(scaledImage, (width - scaledImage.getWidth()) / 2, (height - scaledImage.getHeight()) / 2,
                AbstractTnGraphics.LEFT | AbstractTnGraphics.TOP);
        }

        //draw semi-transparent area
        int heightPixels = AndroidCitizenUiHelper.getPixelsByDip(56);
        int horizontalpadding = AndroidCitizenUiHelper.getPixelsByDip(2);
        TnRect rect = new TnRect(horizontalpadding /* x */, 
                                (baseY - heightPixels) /* y */, 
                                horizontalpadding /* x */ + (width - horizontalpadding * 2) /* area_width */, 
                                (baseY - heightPixels) /* y */+ heightPixels /* area_height */);
        graphics.setColor(127, 255, 255, 255);
        graphics.fillRect(rect);
        
        //draw version
        String versionInfo = "version 1.6";

        graphics.setFont(font);
        graphics.setColor(VERSION_FONT_COLOR);
        graphics.drawString(versionInfo, width - horizontalMargin, rect.top - lineHeight / 2, AbstractTnGraphics.BASE_LINE
                | AbstractTnGraphics.RIGHT);

        // draw cautions.
        String caution1 = "You agree to comply with the following when using the Telenav Service.";
        String caution2 = "Observe all traffic laws and otherwise drive safely.";
        int top_down_gap = ( heightPixels - 2 * fontHeight - fontHeight / 3 ) / 2;
        graphics.setColor(CAUTION_FONT_COLOR);
        graphics.drawString(caution1, width / 2, rect.top + top_down_gap, AbstractTnGraphics.TOP
                | AbstractTnGraphics.HCENTER);
        graphics.drawString(caution2, width / 2, rect.bottom - top_down_gap, AbstractTnGraphics.BOTTOM
                | AbstractTnGraphics.HCENTER);
    }
    
    protected void onUndisplay()
    {
        super.onUndisplay();

        if (scaledImage != null)
        {
            scaledImage.release();
            scaledImage = null;
        }
    }

}
