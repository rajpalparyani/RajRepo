/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * FrogTextPainter.java
 *
 */
package com.telenav.ui.frogui.text;

import java.util.Vector;

import com.telenav.logger.Logger;
import com.telenav.tnui.core.AbstractTnUiHelper;
import com.telenav.tnui.core.TnUiArgs.ITnUiArgsDecorator;
import com.telenav.tnui.core.TnUiArgs.TnUiArgAdapter;
import com.telenav.tnui.graphics.AbstractTnFont;
import com.telenav.tnui.graphics.AbstractTnGraphics;
import com.telenav.tnui.graphics.AbstractTnImage;
import com.telenav.ui.UiStyleManager;
import com.telenav.ui.tnui.text.TnTextGraphicsContext;
import com.telenav.ui.tnui.text.TnTextLine;
import com.telenav.ui.tnui.text.TnTextToken;
import com.telenav.util.PrimitiveTypeCache;

/**
 * A helper for parsing/drawing the text.
 * 
 *@author fqming (fqming@telenav.cn)
 *@date Jul 28, 2010
 */
public class FrogTextHelper
{
    /**
     * Bold tag start
     */
    public final static String BOLD_START = "<bold>";

    /**
     * Bold tag end
     */
    public final static String BOLD_END = "</bold>";
    
    /**
     * Italic tag start
     */
    public final static String ITALIC_START = "<italic>";
    
    /**
     * Italic tag end
     */
    public final static String ITALIC_END = "</italic>";
    
    /**
     * URL tag start
     */
    public final static String URL_START = "<url>";
    
    /**
     * URL tag end
     */
    public final static String URL_END = "</url>";
    
    /**
     * Telephone tag start
     */
    public final static String TEL_START = "<tel>";
    
    /**
     * Telephone tag end
     */
    public final static String TEL_END = "</tel>";
    
    /**
     * Red color tag start
     */
    public final static String RED_COLOR_START = "<red>";
    
    /**
     * Red color tag end
     */
    public final static String RED_COLOR_END = "</red>";
    
    /**
     * Blue color tag start
     */
    public final static String BLUE_COLOR_START = "<blue>";
    
    /**
     * Blue color tag end
     */
    public final static String BLUE_COLOR_END = "</blue>";

    /**
     * White color tag start
     */
    public final static String WHITE_COLOR_START = "<white>";
    
    /**
     * White color tag end
     */
    public final static String WHITE_COLOR_END = "</white>";

    /**
     * Grey color tag start 
     */
    public final static String GREY_COLOR_START = "<grey>";
    
    /**
     * Grey color tag end
     */
    public final static String GREY_COLOR_END = "</grey>";

    
    /**
     * Image object start
     */
    public final static String IMG_START  = "<img>";
    
    
    /**
     * Image object end
     */
    public final static String IMG_END   = "</img>";
    
    /**
     * Retrieve the width of the text line.
     * 
     * @param textLine the text line
     * @param normalFont the normal font
     * @param boldFont the bold font
     * @return the width.
     */
    public static int getWidth(TnTextLine textLine, AbstractTnFont normalFont, AbstractTnFont boldFont)
    {
        if (textLine == null)
            return 0;

        if (textLine.width > 0)
            return textLine.width;

        TnTextGraphicsContext backPainter = new TnTextGraphicsContext(normalFont, 0);
        TnTextGraphicsContext currentPainter = new TnTextGraphicsContext(normalFont, 0);
        int width = 0;
        Vector tokens = textLine.toVector();
        for(int n = 0; n < tokens.size(); n++)
        {
            TnTextToken token = (TnTextToken) tokens.elementAt(n);
            if (token.isTag)
            {
                if (BOLD_START.endsWith(token.token))
                {
                    currentPainter.font = boldFont;
                }
                else if (BOLD_END.endsWith(token.token))
                {
                    currentPainter.font = backPainter.font;
                }
                continue;
            }
            
            token.width = currentPainter.font.stringWidth(token.token);
            
            width += token.width;
        }

        textLine.width = width;
        return width;
    }

    /**
     * paint the text line.
     * 
     * @param g graphics object.
     * @param x x position.
     * @param y y position.
     * @param textLine the text line.
     * @param normalFont the normal font.
     * @param boldFont the bold font.
     * @param width the width of clip.
     * @param isEllipsis need ellipsis.
     */
    public static void paint(AbstractTnGraphics g, int x, int y, TnTextLine textLine, AbstractTnFont normalFont, AbstractTnFont boldFont,
            int width, boolean isEllipsis)
    {
        paint(g, x, y, textLine, normalFont, boldFont, width, isEllipsis, new TnTextGraphicsContext(normalFont, g.getColor()), new TnTextGraphicsContext(
                normalFont, g.getColor()), true);
    }
    
    public static void paint(AbstractTnGraphics g, int x, int y, TnTextLine textLine, AbstractTnFont normalFont, AbstractTnFont boldFont,
            int width, boolean isEllipsis, int verticalAnchor)
    {
        paint(g, x, y, textLine, normalFont, boldFont, width, verticalAnchor, isEllipsis, new TnTextGraphicsContext(normalFont, g.getColor()), new TnTextGraphicsContext(
                normalFont, g.getColor()), null, true);
    }
    
    /**
     * paint the text line.
     * 
     * @param g graphics object.
     * @param x x position.
     * @param y y position.
     * @param textLine the text line.
     * @param normalFont the normal font.
     * @param boldFont the bold font.
     * @param width the width of clip.
     * @param isEllipsis need ellipsis.
     * @param currentPainter  current painter
     * @param backPainter backup painter
     * @param needPaint flag of needing paint
     */
    public static void paint(AbstractTnGraphics g, int x, int y, TnTextLine textLine, AbstractTnFont normalFont, AbstractTnFont boldFont,
            int width, boolean isEllipsis, TnTextGraphicsContext currentPainter, TnTextGraphicsContext backPainter, boolean needPaint)
    {
        paint(g, x, y, textLine, normalFont, boldFont, width, isEllipsis, currentPainter, backPainter, null, needPaint);
    }
    
    public static void paint(AbstractTnGraphics g, int x, int y, TnTextLine textLine, AbstractTnFont normalFont, AbstractTnFont boldFont,
            int width, boolean isEllipsis, TnTextGraphicsContext currentPainter, TnTextGraphicsContext backPainter, ITnUiArgsDecorator resDecorator,  boolean needPaint)
    {
        paint(g, x, y, textLine, normalFont, boldFont, width, AbstractTnGraphics.TOP, isEllipsis, currentPainter, backPainter, null, needPaint);
    }
    
    /**
     * paint the text line with drawable resource
     * @param g graphics object.
     * @param x x position.
     * @param y y position.
     * @param textLine the text line.
     * @param normalFont the normal font.
     * @param boldFont the bold font.
     * @param width the width of clip.
     * @param verticalAnchor the vertical anchor.
     * @param isEllipsis need ellipsis.
     * @param currentPainter current painter.
     * @param backPainter backup painter.
     * @param resDecorator resource decorator.
     * @param needPaint flag of needing painted.
     */
    public static void paint(AbstractTnGraphics g, int x, int y, TnTextLine textLine, AbstractTnFont normalFont, AbstractTnFont boldFont,
            int width, int verticalAnchor, boolean isEllipsis, TnTextGraphicsContext currentPainter, TnTextGraphicsContext backPainter, ITnUiArgsDecorator resDecorator,  boolean needPaint)
    {
        if (textLine == null)
            return;
        
        int oldX = x;
        boolean isIMGStart = false;
        Vector tokens = textLine.toVector();
        textLine.yOffset = y;
        for(int n = 0; n < tokens.size(); n++)
        {
            TnTextToken token = (TnTextToken) tokens.elementAt(n);
            if (token.isTag)
            {
                if (resDecorator != null)
                {
                    if (IMG_START.equals(token.token))
                    {
                        isIMGStart = true;
                    }
                    else if (IMG_END.equals(token.token))
                    {
                        isIMGStart = false;
                    }
                }

                if (BOLD_START.equals(token.token))
                {
                    currentPainter.font = boldFont;
                }
                else if (BOLD_END.equals(token.token))
                {
                    currentPainter.font = backPainter.font;
                }
                else if (RED_COLOR_START.equals(token.token))
                {
                    currentPainter.color = UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_RED);
                }
                else if (RED_COLOR_END.equals(token.token))
                {
                    currentPainter.color = backPainter.color;
                }
                else if (BLUE_COLOR_START.equals(token.token))
                {
                    currentPainter.color = UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_BLUE);
                }
                else if (BLUE_COLOR_END.equals(token.token))
                {
                    currentPainter.color = backPainter.color;
                }
                else if (WHITE_COLOR_START.equals(token.token))
                {
                	currentPainter.color = UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_WH);
                }
                else if (WHITE_COLOR_END.equals(token.token))
                {
                    currentPainter.color = backPainter.color;
                }
                else if (GREY_COLOR_START.equals(token.token))
                {
                	currentPainter.color = UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_LI_GR);
                }
                else if (GREY_COLOR_END.equals(token.token))
                {
                    currentPainter.color = backPainter.color;
                }
                else if (URL_START.equals(token.token))
                {
                    currentPainter.font = getUnderlineFont(normalFont);
                }
                else if (URL_END.equals(token.token))
                {
                    currentPainter.font = backPainter.font;
                }
                else if (TEL_START.equals(token.token))
                {
                    currentPainter.font = getUnderlineFont(normalFont);
                }
                else if (TEL_END.equals(token.token))
                {
                    currentPainter.font = backPainter.font;
                }
                continue;
            }
           
            
            g.setFont(currentPainter.font);
            g.setColor(currentPainter.color);
            
            if (needPaint)
            {
                AbstractTnImage image = null;
                if(resDecorator != null && isIMGStart)
                {
                    String imageKeyStr = token.token;
                    token.width = 0;
                    int intKey = 0;
                    try
                    {
                        intKey = Integer.parseInt(imageKeyStr);
                    }
                    catch(Throwable t)
                    {
                        Logger.log(FrogTextHelper.class.getName(), t);
                    }
                    if(intKey > 0)
                    {
                        TnUiArgAdapter imageAdapter = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(intKey), resDecorator);
                        image= imageAdapter.getImage();
                        if(image != null)
                        {
                            token.width = image.getWidth();
                        }
                    }
                }

                if (token.width <= 0)
                {
                    token.width = g.getFont().stringWidth(token.token);
                }
                if(isEllipsis && (x - oldX + token.width) >= width - g.getFont().stringWidth("...") && (x - oldX + token.width) <= width)
                {
                    int leftLength = 0;
                    if(n < tokens.size() - 1)
                    {
                        for(int i = n + 1; i < tokens.size(); i++)
                        {
                            TnTextToken leftToken = (TnTextToken) tokens.elementAt(i);
                            if (leftToken.isTag)
                                continue;
                            if (leftToken.width <= 0)
                            {
                                leftToken.width = g.getFont().stringWidth(leftToken.token);
                            }
                            leftLength += leftToken.width;
                        }
                        if(leftLength >= g.getFont().stringWidth("..."))
                        {
                            int len = x - oldX + g.getFont().stringWidth("...");
                            int i;
                            for (i = 0; i < token.token.length(); i++)
                            {
                                len += g.getFont().charWidth(token.token.charAt(i));
                                if (len > width)
                                {
                                    break;
                                }
                            }
                            String tempLabel = token.token.substring(0, i);
                            tempLabel += "...";
                            
                            g.drawString(tempLabel, x, y, AbstractTnGraphics.LEFT | verticalAnchor);
                            break;
                        }
                    }
                }
                else if (isEllipsis && (x - oldX + token.width) > width)
                {
                    int len = x - oldX + g.getFont().stringWidth("...");
                    int i;
                    for (i = 0; i < token.token.length(); i++)
                    {
                        len += g.getFont().charWidth(token.token.charAt(i));
                        if (len > width)
                        {
                            break;
                        }
                    }
                    String tempLabel = token.token.substring(0, i);
                    tempLabel += "...";

                    g.drawString(tempLabel, x, y, AbstractTnGraphics.LEFT | verticalAnchor);
                    break;
                }
                token.xOffset = x;
                if(resDecorator != null && image != null)
                {
                    g.drawImage(image, x, y, AbstractTnGraphics.LEFT | verticalAnchor);
                }
                else
                {
                    g.drawString(token.token, x, y, AbstractTnGraphics.LEFT | verticalAnchor);
                }
                x += token.width; 
            }
            
        }
    }
    
    protected static AbstractTnFont getUnderlineFont(AbstractTnFont normalFont)
    {
        AbstractTnFont underLineFont = AbstractTnUiHelper.getInstance()
        .createFont(AbstractTnFont.FAMILY_DEFAULT,
                AbstractTnFont.FONT_STYLE_UNDERLINED,
                normalFont.getSize());
        
        return underLineFont;
    }
}
