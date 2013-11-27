/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * J2seGraphics.java
 *
 */
package com.telenav.tnui.core.j2se;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.Stack;

import com.telenav.tnui.graphics.AbstractTnFont;
import com.telenav.tnui.graphics.AbstractTnGraphics;
import com.telenav.tnui.graphics.AbstractTnImage;
import com.telenav.tnui.graphics.TnPoint;
import com.telenav.tnui.graphics.TnRect;

/**
 * Provides a surface on which applications can draw at j2se platform.
 * 
 *@author fqming (fqming@telenav.cn)
 *@date Aug 3, 2010
 */
class J2seGraphics extends AbstractTnGraphics
{
    protected Graphics2D graphics;
    
    protected int translateX;

    protected int translateY;

    protected AbstractTnFont font;

    protected Rectangle nativeClipBounds;

    protected Stack clips;
    
    public J2seGraphics(Graphics2D graphics)
    {
        this.font = new J2seFont(graphics, AbstractTnFont.FAMILY_DEFAULT, AbstractTnFont.FONT_STYLE_PLAIN, 0);
        
        this.nativeClipBounds = new Rectangle();
        this.clips = new Stack();
    }
    
    public void drawChar(char character, int x, int y, int anchor)
    {
        drawChar(character, x, y, anchor, 0);
    }

    public void drawChar(char character, int x, int y, int anchor, int angle)
    {
        drawText(String.valueOf(character), x, y, anchor, angle);
    }

    private void drawText(String str, int x, int y, int anchor, int angle)
    {
        if (str == null)
            return;

        if (this.font == null)
        {
            return;
        }

        if ((anchor & HCENTER) != 0)
        {
            x -= font.stringWidth(str) / 2;
        }

        if ((anchor & VCENTER) != 0)
        {
            y -= font.getHeight() / 2;
        }

        if ((anchor & RIGHT) != 0)
        {
            x -= font.stringWidth(str);
        }

        if ((anchor & BOTTOM) != 0)
        {
            y -= font.getHeight();
        }

        if(angle != 0)
        {
            //TODO
        }
        
        
        this.graphics.setFont((Font)font.getNativeFont());
        this.graphics.drawString(str, x, y + this.graphics.getFontMetrics().getAscent());
    }
    
    public void drawCircle(int xC, int yC, int d)
    {
        this.graphics.drawOval(xC - d, yC - d, d * 2, d * 2);
    }

    public void drawFilledPath(int[] xx, int[] yy)
    {
        this.graphics.fillPolygon(xx, yy, yy.length);
    }

    public void drawFilledPath(int[] points, int numPoints, int qFactor)
    {
        drawFilledPath(points, numPoints, qFactor, true);
    }

    private void drawFilledPath(int[] points, int numPoints, int qFactor, boolean isFilled)
    {
        if (numPoints <= 1)
            return;

        int xx[] = new int[numPoints << 1];
        int yy[] = new int[numPoints << 1];
        if (qFactor <= 0)
        {
            for (int i = 0; i < (numPoints << 1) - 1; i += 2)
            {
                xx[i] = points[i];
                yy[i] = points[i + 1];
            }
        }
        else
        {
            int scale = 1 << qFactor;
            for (int i = 0; i < (numPoints << 1) - 1; i += 2)
            {
                xx[i] = points[i] / scale;
                yy[i] = points[i + 1] / scale;
            }
        }

        if(isFilled)
        {
            this.drawFilledPath(xx, yy);
        }
        else
        {
            this.drawPathOutline(xx, yy);
        }
    }
    
    public void drawImage(AbstractTnImage img, int x, int y, int anchor)
    {
        if (img == null)
        {
            return;
        }
        
        if(img.getDrawable() != null)
        {
            img.getDrawable().draw(this);
            return;
        }

        Image bitmap = (Image) img.getNativeImage();

        if ((anchor & HCENTER) != 0)
        {
            x -= img.getWidth() / 2;
        }
        if ((anchor & VCENTER) != 0)
        {
            y -= img.getHeight() / 2;
        }
        if ((anchor & RIGHT) != 0)
        {
            x -= img.getWidth();
        }
        if ((anchor & BOTTOM) != 0)
        {
            y -= img.getHeight();
        }

        this.graphics.drawImage(bitmap, x, y, img.getWidth(), img.getHeight(), null);
    }

    public void drawImage(AbstractTnImage img, TnRect src, TnRect dst)
    {
        if (img == null || img.getNativeImage() == null)
        {
            return;
        }

        if (dst == null)
        {
            throw new IllegalArgumentException("the dst is empty.");
        }

        Image bitmap = (Image) img.getNativeImage();

        if(src == null)
        {
            this.graphics.drawImage(bitmap, dst.left, dst.top, dst.width(), dst.height(), null);
        }
        else
        {
            this.graphics.drawImage(bitmap, dst.left, dst.top, dst.right, dst.bottom, src.left, src.top, src.right, src.bottom, null);
        }
    }

    public void drawLine(int x1, int y1, int x2, int y2)
    {
        this.graphics.drawLine(x1, y1, x2, y2);
    }

    public void drawLine(TnPoint p1, TnPoint p2)
    {
        this.drawLine(p1.x, p1.y, p2.x, p2.y);
    }

    public void drawPathOutline(int[] xx, int[] yy)
    {
        this.graphics.drawPolygon(xx, yy, yy.length);
    }

    public void drawPathOutline(int[] points, int numPoints, int qFactor)
    {
        drawFilledPath(points, numPoints, qFactor, false);
    }

    public void drawPoint(int x0, int y0)
    {
        this.graphics.drawLine(x0, y0, x0, y0);
    }

    public void drawPolyline(int[] points, int numPoints, int w)
    {
        int strokeWidth = this.getStrokeWidth();
        this.setStrokeWidth(w);
        this.drawPathOutline(points, numPoints, 0);
        this.setStrokeWidth(strokeWidth);
    }

    public void drawRGB(int[] rgbData, int offset, int scanlength, int x, int y, int width, int height, boolean hasAlpha)
    {
        BufferedImage bufferedImg = new BufferedImage(width, height, hasAlpha ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB);
        bufferedImg.setRGB(0, 0, width, height, rgbData, 0, scanlength);

        this.graphics.drawImage(bufferedImg, x, y, null);
    }

    public void drawRect(int x, int y, int width, int height)
    {
        this.graphics.drawRect(x, y, width, height);
    }

    public void drawRect(TnRect rect)
    {
        this.drawRect(rect.left, rect.top, rect.width(), rect.height());
    }

    public void drawRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight)
    {
        this.graphics.drawRoundRect(x, y, width, height, arcWidth, arcHeight);
    }

    public void drawRoundTriangle(int x1, int y1, int x2, int y2, int x3, int y3, int radius)
    {
        this.graphics.drawPolygon(new int[]{x1, x2, x3}, new int[]{y1, y2, y3}, 3);
    }

    public void drawString(String str, int x, int y, int anchor)
    {
        drawText(str, x, y, anchor, 0);        
    }

    public void fillArcs(int xC, int yC, int d, float startAngle, float sweepAngle)
    {
        this.graphics.fillArc(xC - d, yC - d, d * 2, d * 2, (int)startAngle, (int)sweepAngle);
    }

    public void fillCircle(int xC, int yC, int d, boolean isClear)
    {
        this.graphics.fillOval(xC - d, yC - d, d * 2, d * 2);
    }

    public void fillRect(int x, int y, int width, int height)
    {
        this.graphics.fillRect(x, y, width, height);
    }

    public void fillRect(TnRect rect)
    {
        this.fillRect(rect.left, rect.top, rect.width(), rect.height());
    }

    public void fillRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight)
    {
        this.graphics.fillRoundRect(x, y, width, height, arcWidth, arcHeight);
    }

    public void fillRoundTriangle(int x1, int y1, int x2, int y2, int x3, int y3, int radius)
    {
        this.graphics.fillPolygon(new int[]{x1, x2, x3}, new int[]{y1, y2, y3}, 3);
    }

    public void fillTriangle(int x1, int y1, int x2, int y2, int x3, int y3)
    {
        this.graphics.fillPolygon(new int[]{x1, x2, x3}, new int[]{y1, y2, y3}, 3);
    }

    public void getClipBounds(TnRect clipBounds)
    {
        this.graphics.getClipBounds(nativeClipBounds);
        clipBounds.left = nativeClipBounds.x;
        clipBounds.top = nativeClipBounds.y;
        clipBounds.right = clipBounds.left + nativeClipBounds.width;
        clipBounds.bottom = clipBounds.top + nativeClipBounds.height;
    }

    public int getColor()
    {
        return this.graphics.getColor().getRGB();
    }

    public AbstractTnFont getFont()
    {
        return this.font;
    }

    public Object getNativeGraphics()
    {
        return this.graphics;
    }

    public int getStrokeCap()
    {
        BasicStroke oldStroke = (BasicStroke)this.graphics.getStroke();
        if(oldStroke == null)
        {
            oldStroke = new BasicStroke();
        }
        
        return oldStroke.getEndCap();
    }

    public int getStrokeJoin()
    {
        BasicStroke oldStroke = (BasicStroke)this.graphics.getStroke();
        if(oldStroke == null)
        {
            oldStroke = new BasicStroke();
        }
        
        return oldStroke.getLineJoin();
    }

    public int getStrokeWidth()
    {
        BasicStroke oldStroke = (BasicStroke)this.graphics.getStroke();
        if(oldStroke == null)
        {
            oldStroke = new BasicStroke();
        }

        return (int)oldStroke.getLineWidth();
    }

    public int getTranslateX()
    {
        return this.translateX;
    }

    public int getTranslateY()
    {
        return this.translateY;
    }

    public boolean isAntiAlias()
    {
        return this.graphics.getRenderingHint(RenderingHints.KEY_ANTIALIASING) == RenderingHints.VALUE_ANTIALIAS_ON;
    }

    public boolean isIntersectWithClip(TnRect clipBounds, int x, int y, int width, int height)
    {
        int clipX = clipBounds.left;
        int clipY = clipBounds.top;
        int clipWidth = clipBounds.width();
        int clipHeight = clipBounds.height();

        return isIntersectWithClip(clipX, clipY, clipWidth, clipHeight, x, y, width, height, null);
    }

    public boolean isIntersectWithClip(int x, int y, int width, int height)
    {
        this.graphics.getClipBounds(nativeClipBounds);
        int clipX = nativeClipBounds.x;
        int clipY = nativeClipBounds.y;
        int clipWidth = nativeClipBounds.width;
        int clipHeight = nativeClipBounds.height;
        
        return isIntersectWithClip(clipX, clipY, clipWidth, clipHeight, x, y, width, height, null);
    }

    private boolean isIntersectWithClip(int clipX, int clipY, int clipWidth, int clipHeight, int x, int y, int width, int height, TnRect rect)
    {
        int xIntersection = Math.max(clipX, x);
        int yIntersection = Math.max(clipY, y);

        int xMaxInter = Math.min(clipX + clipWidth, x + width);
        int yMaxInter = Math.min(clipY + clipHeight, y + height);
        int wIntersection = xMaxInter - xIntersection;
        int hIntersection = yMaxInter - yIntersection;

        if(rect != null)
        {
            rect.left = xIntersection;
            rect.top = yIntersection;
            rect.right = rect.left + wIntersection;
            rect.bottom = rect.top + hIntersection;
        }
        
        if (wIntersection > 0 && hIntersection > 0)
            return true;

        return false;
    }
    
    public void popClip()
    {
        if (!clips.isEmpty())  
        {
            TnRect clip = (TnRect) clips.pop();
            this.graphics.setClip(clip.left, clip.top, clip.width(), clip.height());
        }
    }

    public void pushClip(TnRect rect)
    {
        this.pushClip(rect.left, rect.top, rect.width(), rect.height());
    }

    public void pushClip(int x, int y, int width, int height)
    {
        TnRect tmpClipRect = new TnRect();
        this.getClipBounds(tmpClipRect);

        clips.push(tmpClipRect);

        TnRect clip = new TnRect();
        isIntersectWithClip(tmpClipRect.left, tmpClipRect.top, tmpClipRect.width(), tmpClipRect.height(), x, y, width, height, clip);
        if (clip.width() > 0 && clip.height() > 0)
        {
            this.graphics.setClip(clip.left, clip.top, clip.width(), clip.height());
        }
        else
        {
            this.graphics.setClip(clip.left, clip.top, 0, 0);
        }
    }

    public void setAntiAlias(boolean aa)
    {
        if(aa)
        {
            this.graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        }
        else
        {
            this.graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        }
    }

    public void setColor(int RGB)
    {
        this.graphics.setColor(new Color(RGB));
    }

    public void setColor(int alpha, int red, int green, int blue)
    {
        this.graphics.setColor(new Color(red, green,  blue, alpha));
    }

    public void setFont(AbstractTnFont font)
    {
        this.font = font;
    }

    public void setGraphics(Object graphics)
    {
        this.graphics = (Graphics2D)graphics;
    }

    public void setStrokeCap(int strokeCap)
    {
        BasicStroke oldStroke = (BasicStroke)this.graphics.getStroke();
        if(oldStroke == null)
        {
            oldStroke = new BasicStroke();
        }
        BasicStroke stroke = oldStroke;
        switch (strokeCap)
        {
            case CAP_BUTT:
                stroke = new BasicStroke(oldStroke.getLineWidth(), BasicStroke.CAP_BUTT, oldStroke.getLineJoin());
                break;
            case CAP_ROUND:
                stroke = new BasicStroke(oldStroke.getLineWidth(), BasicStroke.CAP_ROUND, oldStroke.getLineJoin());
                break;
            case CAP_SQUARE:
                stroke = new BasicStroke(oldStroke.getLineWidth(), BasicStroke.CAP_SQUARE, oldStroke.getLineJoin());
                break;
        }
        
        this.graphics.setStroke(stroke);
    }

    public void setStrokeJoin(int strokeJoin)
    {
        BasicStroke oldStroke = (BasicStroke)this.graphics.getStroke();
        if(oldStroke == null)
        {
            oldStroke = new BasicStroke();
        }
        BasicStroke stroke = oldStroke;
        switch (strokeJoin)
        {
            case JOIN_BEVEL:
                stroke = new BasicStroke(oldStroke.getLineWidth(), oldStroke.getEndCap(), BasicStroke.JOIN_BEVEL);
                break;
            case JOIN_MITER:
                stroke = new BasicStroke(oldStroke.getLineWidth(), oldStroke.getEndCap(), BasicStroke.JOIN_MITER);
                break;
            case JOIN_ROUND:
                stroke = new BasicStroke(oldStroke.getLineWidth(), oldStroke.getEndCap(), BasicStroke.JOIN_ROUND);
                break;
        }
        
        this.graphics.setStroke(stroke);
    }

    public void setStrokeWidth(int width)
    {
        BasicStroke oldStroke = (BasicStroke)this.graphics.getStroke();
        if(oldStroke == null)
        {
            oldStroke = new BasicStroke();
        }
        BasicStroke stroke = new BasicStroke(width, oldStroke.getEndCap(), oldStroke.getLineJoin());
        this.graphics.setStroke(stroke);
    }

    public void translate(int x, int y)
    {
        this.translateX += x;
        this.translateY += y;
        
        this.graphics.translate(x, y);
    }

    public void rotate(int degree)
    {
        this.graphics.rotate(180*degree/Math.PI);
    }
    
    public void rotate(int degree, int x, int y)
    {
        this.graphics.rotate(180*degree/Math.PI, x, y);
    }
}
