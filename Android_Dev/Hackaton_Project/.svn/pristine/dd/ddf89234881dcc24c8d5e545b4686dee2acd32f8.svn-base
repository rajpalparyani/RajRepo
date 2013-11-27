/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * AndroidGraphics.java
 *
 */
package com.telenav.tnui.core.android;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.CornerPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Xfermode;

import com.telenav.logger.Logger;
import com.telenav.tnui.core.AbstractTnUiHelper;
import com.telenav.tnui.graphics.AbstractTnFont;
import com.telenav.tnui.graphics.AbstractTnGraphics;
import com.telenav.tnui.graphics.AbstractTnImage;
import com.telenav.tnui.graphics.TnPoint;
import com.telenav.tnui.graphics.TnRect;

/**
 * Provides a surface on which applications can draw at android platform.
 * 
 *@author fqming (fqming@telenav.cn)
 *@date 2010-6-2
 */
class AndroidGraphics extends AbstractTnGraphics
{
    protected int translateX;

    protected int translateY;

    protected Canvas canvas;

    protected Paint paint;

    protected Path path;

    protected AbstractTnFont font;

    protected Rect nativeClipBounds;
    
    public AndroidGraphics()
    {
        this.font = ((AbstractTnUiHelper)AbstractTnUiHelper.getInstance()).createFont(AndroidFont.FAMILY_DEFAULT, AndroidFont.FONT_STYLE_PLAIN, 0);
        this.path = new Path();
        this.paint = new Paint();
        this.nativeClipBounds = new Rect();
    }

    public void pushClip(TnRect rect)
    {
        this.pushClip(rect.left, rect.top, rect.width(), rect.height());
    }

    public void drawChar(char character, int x, int y, int anchor)
    {
        drawChar(character, x, y, anchor, 0);
    }

    public void drawCircle(int xC, int yC, int d)
    {
        Paint.Style oldStyle = this.paint.getStyle();
        this.paint.setStyle(Paint.Style.STROKE);
        this.canvas.drawCircle(xC, yC, d, this.paint);
        this.paint.setStyle(oldStyle);
    }

    public void drawFilledPath(int[] xx, int[] yy)
    {
        drawPathOutline(xx, yy, true);
    }
    
    public void drawPathOutline(int[] xx, int[] yy)
    {
        drawPathOutline(xx, yy, false);
    }
    
    private void drawPathOutline(int[] xx, int[] yy, boolean isFilled)
    {
        path.reset();

        path.moveTo(xx[0], yy[0]);
        if (xx.length <= 1)
            return;

        for (int i = 1; i < xx.length; i++)
        {
            path.lineTo(xx[i], yy[i]);
        }

        path.close();

        Paint.Style oldStyle = this.paint.getStyle();
        this.paint.setStyle(isFilled ? Paint.Style.FILL : Paint.Style.STROKE);
        this.canvas.drawPath(path, this.paint);
        this.paint.setStyle(oldStyle);
    }

    public void drawImage(AbstractTnImage img, int x, int y, int anchor)
    {
        if (img == null)
        {
            return;
        }
        
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

        if(img.getDrawable() != null)
        {
            img.getDrawable().setBounds(new TnRect(x, y, x + img.getWidth(), y + img.getHeight()));
            img.getDrawable().draw(this);
            return;
        }
        
        Bitmap bitmap = (Bitmap) img.getNativeImage();
        
        try
        {
            if (bitmap.isRecycled())
            {
                if(Logger.DEBUG)
                {
                    String log = "drawImage() - bitmap is recycled";
                    
                    Logger.log(Logger.ERROR, this.getClass().getName(), log);
                }
                return;
            }

            this.canvas.drawBitmap(bitmap, x, y, this.paint);
        }
        catch (Throwable e)
        {
            Logger.log(this.getClass().getName(), e);
        }
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

        Bitmap bitmap = (Bitmap) img.getNativeImage();

        Rect srcRect = src == null ? null : new Rect(src.left, src.top, src.right, src.bottom);

        Rect srcDst = new Rect(dst.left, dst.top, dst.right, dst.bottom);

        Paint p = new Paint();
//        p.setAntiAlias(true);
        p.setFilterBitmap(true);
        p.setDither(true);
        
        this.canvas.drawBitmap(bitmap, srcRect, srcDst, p);
    }

    public void drawLine(int x1, int y1, int x2, int y2)
    {
        this.canvas.drawLine(x1, y1, x2, y2, this.paint);
    }

    public void drawLine(TnPoint p1, TnPoint p2)
    {
        this.canvas.drawLine(p1.x, p1.y, p2.x, p2.y, this.paint);
    }

    public void drawPathOutline(int[] points, int numPoints, int qFactor)
    {
        drawFilledPath(points, numPoints, qFactor, false);
    }
    
    public void drawFilledPath(int[] points, int numPoints, int qFactor)
    {
        drawFilledPath(points, numPoints, qFactor, true);
    }
    
    private void drawFilledPath(int[] points, int numPoints, int qFactor, boolean isFilled)
    {
        path.reset();

        if (numPoints <= 1)
            return;

        if (qFactor <= 0)
        {
            path.moveTo(points[0], points[1]);
            for (int i = 2; i < (numPoints << 1) - 1; i += 2)
            {
                path.lineTo(points[i], points[i + 1]);
            }
        }
        else
        {
            float scale = 1 << qFactor;
            path.moveTo(points[0]/scale, points[1]/scale);
            for (int i = 2; i < (numPoints << 1) - 1; i += 2)
            {
                path.lineTo(points[i]/scale, points[i + 1]/scale);
            }
        }

        path.close();

        Paint.Style oldStyle = this.paint.getStyle();
        this.paint.setStyle(isFilled ? Paint.Style.FILL : Paint.Style.STROKE);
        this.canvas.drawPath(path, this.paint);

        this.paint.setStyle(oldStyle);
    }

    public void drawPoint(int x0, int y0)
    {
        this.canvas.drawPoint(x0, y0, this.paint);
    }

    public void drawRoundTriangle(int x1, int y1, int x2, int y2, int x3, int y3, int radius)
    {
        paintRoundTriangle(false, x1, y1, x2, y2, x3, y3, radius);
    }
    
    public void fillRoundTriangle(int x1, int y1, int x2, int y2, int x3, int y3, int radius)
    {
        paintRoundTriangle(true, x1, y1, x2, y2, x3, y3, radius);
    }
    
    private void paintRoundTriangle(boolean isFilled, int x1, int y1, int x2, int y2, int x3, int y3, int radius)
    {
        path.reset();

        path.moveTo(x1, y1);
        path.lineTo(x2, y2);
        path.lineTo(x3, y3);
        path.close();

        this.paint.setPathEffect(new CornerPathEffect(radius));
        Paint.Style oldStyle = this.paint.getStyle();
        this.paint.setStyle(isFilled ? Paint.Style.FILL : Paint.Style.STROKE);
        this.canvas.drawPath(path, this.paint);
        this.paint.setStyle(oldStyle);
        this.paint.setPathEffect(null);
    }
    
    public void drawPolyline(int[] points, int numPoints, int w)
    {
        path.reset();

        for (int i = 0; i < numPoints; i++)
        {
            if (i == 0)
                path.moveTo((float) points[i << 1], points[(i << 1) + 1]);
            else
                path.lineTo((float) points[i << 1], points[(i << 1) + 1]);
        }

        Paint paint = this.paint;
        Join oldStrokeJoin = paint.getStrokeJoin();
        Cap oldStrokeCap = paint.getStrokeCap();
        Paint.Style oldStyle = paint.getStyle();
        float strokeWidth = paint.getStrokeWidth();
        
        paint.setStrokeJoin(Paint.Join.BEVEL);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(w);

        this.canvas.drawPath(path, paint);

        paint.setStrokeJoin(oldStrokeJoin);
        paint.setStrokeCap(oldStrokeCap);
        paint.setStyle(oldStyle);
        paint.setStrokeWidth(strokeWidth);
    }

    public void drawRGB(int[] rgbData, int offset, int scanlength, int x, int y, int width, int height, boolean processAlpha)
    {
        this.canvas.drawBitmap(rgbData, offset, width, x, y, width, height, processAlpha, this.paint);
    }

    public void drawRect(int x, int y, int width, int height)
    {
        Paint.Style oldStyle = this.paint.getStyle();
        this.paint.setStyle(Paint.Style.STROKE);
        this.canvas.drawRect(x, y, x + width - 1, y + height - 1, this.paint);
        this.paint.setStyle(oldStyle);
    }

    public void drawRect(TnRect rect)
    {
        Paint.Style oldStyle = this.paint.getStyle();
        this.paint.setStyle(Paint.Style.STROKE);
        this.canvas.drawRect(rect.left, rect.top, rect.right - 1, rect.bottom - 1, this.paint);
        this.paint.setStyle(oldStyle);
    }

    public void drawRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight)
    {
        Paint.Style oldStyle = this.paint.getStyle();
        this.paint.setStyle(Paint.Style.STROKE);
        RectF rect = new RectF(x, y, x + width, y + height);
        this.canvas.drawRoundRect(rect, arcWidth, arcHeight, this.paint);
        this.paint.setStyle(oldStyle);
    }

    public void drawString(String str, int x, int y, int anchor)
    {
        drawText(str, x, y, anchor, 0);
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
        if ((anchor & FONT_ABSOLUTE_VCENTER) != 0)
        {
            y += font.getHeightOfCenterAboveBaseline(str);
            anchor = (anchor | BASE_LINE);
        }
        if ((anchor & FONT_VISUAL_VCENTER) != 0)
        {
            y += font.getHeightOfCenterAboveBaseline(((AbstractTnUiHelper)AbstractTnUiHelper.getInstance()).getCommonBaseLineAnchor());
            anchor = (anchor | BASE_LINE);
        }

        if ((anchor & RIGHT) != 0)
        {
            x -= font.stringWidth(str);
        }

        if ((anchor & BOTTOM) != 0)
        {
            y -= font.getHeight();
        }

        Paint nativePaint = (Paint)font.getNativePaint();
        nativePaint.setColor(this.paint.getColor());
        nativePaint.setAntiAlias(true);
        
        this.canvas.save();
        float cX = x;
        float cY = (anchor & BASE_LINE) != 0 ? y : y - nativePaint.ascent();
        if (angle != 0)
        {
            Matrix matrix = new Matrix();
            matrix.postRotate(angle, cX, cY);
            this.canvas.concat(matrix);
        }
        this.canvas.drawText(str, cX, cY, nativePaint);
        this.canvas.restore();
    }

    public void fillArcs(int xC, int yC, int d, float startAngle, float sweepAngle)
    {
        RectF rectF = new RectF(xC - d, yC - d, xC + d, yC + d);
        Paint.Style oldStyle = this.paint.getStyle();
        this.paint.setStyle(Paint.Style.FILL);
        this.canvas.drawArc(rectF, startAngle, sweepAngle, true, this.paint);
        this.paint.setStyle(oldStyle);
    }
    
    public void fillCircle(int xC, int yC, int d, boolean isClear)
    {
        Paint.Style oldStyle = this.paint.getStyle();
        Xfermode xmode = this.paint.getXfermode();
        this.paint.setStyle(Paint.Style.FILL);
        if (isClear)
        {
            this.paint.setXfermode(new PorterDuffXfermode(android.graphics.PorterDuff.Mode.CLEAR));           
        }
        this.canvas.drawCircle(xC, yC, d, this.paint);
        this.paint.setStyle(oldStyle);
        this.paint.setXfermode(xmode);
    }

    public void fillRect(int x, int y, int width, int height)
    {
        Paint.Style oldStyle = this.paint.getStyle();
        this.paint.setStyle(Paint.Style.FILL);
        this.canvas.drawRect(x, y, x + width, y + height, this.paint);
        this.paint.setStyle(oldStyle);
    }

    public void fillRect(TnRect rect)
    {
        fillRect(rect.left, rect.top, rect.width(), rect.height());
    }

    public void fillRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight)
    {
        Paint.Style oldStyle = this.paint.getStyle();
        this.paint.setStyle(Paint.Style.FILL);
        RectF rect = new RectF(x, y, x + width, y + height);
        this.canvas.drawRoundRect(rect, arcWidth, arcHeight, this.paint);
        this.paint.setStyle(oldStyle);
    }

    public void fillTriangle(int x1, int y1, int x2, int y2, int x3, int y3)
    {
        path.reset();
        path.moveTo(x1, y1);
        path.lineTo(x2, y2);
        path.lineTo(x3, y3);
        Paint.Style oldStyle = this.paint.getStyle();
        this.paint.setStyle(Paint.Style.FILL);
        this.canvas.drawPath(path, this.paint);
        this.paint.setStyle(oldStyle);
    }

    public int getColor()
    {
        return this.paint.getColor();
    }

    public AbstractTnFont getFont()
    {
        return font;
    }

    public Object getGraphics()
    {
        return this.canvas;
    }

    public int getStrokeWidth()
    {
        return (int)this.paint.getStrokeWidth();
    }

    public int getTranslateX()
    {
        return this.translateX;
    }

    public int getTranslateY()
    {
        return this.translateY;
    }

    public void getClipBounds(TnRect clipBounds)
    {
        this.canvas.getClipBounds(nativeClipBounds);
        clipBounds.left = nativeClipBounds.left;
        clipBounds.top = nativeClipBounds.top;
        clipBounds.right = nativeClipBounds.right;
        clipBounds.bottom = nativeClipBounds.bottom;
    }
    
    public boolean isIntersectWithClip(TnRect clipBounds, int x, int y, int width, int height)
    {
        int clipX = clipBounds.left;
        int clipY = clipBounds.top;
        int clipWidth = clipBounds.width();
        int clipHeight = clipBounds.height();

        return isIntersectWithClip(clipX, clipY, clipWidth, clipHeight, x, y, width, height);
    }
    
    public boolean isIntersectWithClip(int x, int y, int width, int height)
    {
        this.canvas.getClipBounds(nativeClipBounds);
        int clipX = nativeClipBounds.left;
        int clipY = nativeClipBounds.top;
        int clipWidth = nativeClipBounds.width();
        int clipHeight = nativeClipBounds.height();
        
        return isIntersectWithClip(clipX, clipY, clipWidth, clipHeight, x, y, width, height);
    }
    
    private boolean isIntersectWithClip(int clipX, int clipY, int clipWidth, int clipHeight, int x, int y, int width, int height)
    {
        int xIntersection = Math.max(clipX, x);
        int yIntersection = Math.max(clipY, y);

        int xMaxInter = Math.min(clipX + clipWidth, x + width);
        int yMaxInter = Math.min(clipY + clipHeight, y + height);
        int wIntersection = xMaxInter - xIntersection;
        int hIntersection = yMaxInter - yIntersection;

        if (wIntersection > 0 && hIntersection > 0)
            return true;

        return false;
    }

    public int getClipHeight()
    {
        return this.canvas.getClipBounds().height();
    }

    public int getClipWidth()
    {
        return this.canvas.getClipBounds().width();
    }

    public int getClipX()
    {
        return this.canvas.getClipBounds().left;
    }

    public int getClipY()
    {
        return this.canvas.getClipBounds().top;
    }

    public void pushClip(int x, int y, int width, int height)
    {
        this.canvas.save();
        this.canvas.clipRect(x, y, x + width, y + height);
    }

    public void popClip()
    {
        this.canvas.restore();
    }

    public void setColor(int RGB)
    {
        this.paint.setColor(RGB | 0xFF000000);
    }

    public void setColor(int alpha, int red, int green, int blue)
    {
        this.paint.setARGB(alpha, red, green, blue);
    }

    public void setFont(AbstractTnFont font)
    {
        this.font = font;
    }

    public void setGraphics(Object graphics)
    {
        this.canvas = (Canvas) graphics;
    }

    public void setStrokeWidth(int width)
    {
        this.paint.setStrokeWidth(width);
    }

    public void translate(int x, int y)
    {
        this.translateX += x;
        this.translateY += y;
        this.canvas.translate(x, y);
    }
    
    public void rotate(int degree)
    {
        this.canvas.rotate(degree);
    }
    
    public void rotate(int degree, int x, int y)
    {
        this.canvas.rotate(degree, x, y);
    }

    public void setStrokeCap(int strokeCap)
    {
        switch (strokeCap)
        {
            case CAP_BUTT:
                paint.setStrokeCap(Cap.BUTT);
                break;
            case CAP_ROUND:
                paint.setStrokeCap(Cap.ROUND);
                break;
            case CAP_SQUARE:
                paint.setStrokeCap(Cap.SQUARE);
                break;
        }
    }

    public int getStrokeCap()
    {
        Cap cap = paint.getStrokeCap();
        if(Cap.BUTT.equals(cap))
            return CAP_BUTT;
        else if(Cap.ROUND.equals(cap))
            return CAP_ROUND;
        else if(Cap.SQUARE.equals(cap))
            return CAP_SQUARE;
        
        return CAP_BUTT;
    }

    public void setStrokeJoin(int strokeJoin)
    {
        switch (strokeJoin)
        {
            case JOIN_BEVEL:
                paint.setStrokeJoin(Join.BEVEL);
                break;
            case JOIN_MITER:
                paint.setStrokeJoin(Join.MITER);
                break;
            case JOIN_ROUND:
                paint.setStrokeJoin(Join.ROUND);
                break;
        }
    }

    public int getStrokeJoin()
    {
        Join join = paint.getStrokeJoin();
        if(Join.BEVEL.equals(join))
            return JOIN_BEVEL;
        else if(Join.MITER.equals(join))
            return JOIN_MITER;
        else if(Join.ROUND.equals(join))
            return JOIN_ROUND;
        
        return JOIN_MITER;
    }

    public Object getNativeGraphics()
    {
        return this.canvas;
    }
    
    public void setAntiAlias(boolean aa)
    {
        this.paint.setAntiAlias(aa);
    }
    
    public boolean isAntiAlias()
    {
        return this.paint.isAntiAlias();
    }
}
