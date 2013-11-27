/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * RimGraphics.java
 *
 */
package com.telenav.tnui.core.rim;

import net.rim.device.api.math.Fixed32;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.UnsupportedOperationException;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Ui;
import net.rim.device.api.ui.XYRect;

import com.telenav.logger.Logger;
import com.telenav.tnui.graphics.AbstractTnFont;
import com.telenav.tnui.graphics.AbstractTnGraphics;
import com.telenav.tnui.graphics.AbstractTnImage;
import com.telenav.tnui.graphics.TnPoint;
import com.telenav.tnui.graphics.TnRect;

/**
 * Provides a surface on which applications can draw at rim platform.
 * 
 *@author fqming (fqming@telenav.cn)
 *@date 2010-11-8
 */
class RimGraphics extends AbstractTnGraphics
{
    protected AbstractTnFont font;
    protected Graphics graphics;
    
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
        this.graphics.drawArc(xC-d, yC-d, d<<1, d<<1, 0,360);
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
        if (isFilled)
        {
            this.graphics.drawFilledPath(xx, yy, null, null);
        }
        else
        {
            this.graphics.drawOutlinedPath(xx, yy, null, null, true);
        }
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
            this.graphics.drawBitmap(x, y, bitmap.getWidth(), bitmap.getHeight(), bitmap, 0,
                0);
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
        Bitmap dstBitmap = new Bitmap(dst.width(), dst.height());
        int[] alphaC = new int[dst.width() * dst.height()];
        dstBitmap.setARGB(alphaC, 0, dst.width(), 0, 0, dst.width(), dst.height());

        bitmap.scaleInto(src == null ? 0 : src.left, src == null ? 0 : src.top, src == null ? img.getWidth() : src.width(), src == null ? img.getHeight() : src.height(),
            dstBitmap, 0, 0, dst.width(), dst.height(), Bitmap.FILTER_BOX);

        this.graphics.drawBitmap(dst.left, dst.top, dstBitmap.getWidth(), dstBitmap.getHeight(), dstBitmap, 0, 0);
    }

    public void drawLine(int x1, int y1, int x2, int y2)
    {
        this.graphics.drawLine(x1, y1, x2, y2);
    }

    public void drawLine(TnPoint p1, TnPoint p2)
    {
        this.graphics.drawLine(p1.x, p1.y, p2.x, p2.y);
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
        if (numPoints <= 1)
            return;

        int[] xx = new int[numPoints / 2];
        int[] yy = new int[numPoints / 2];
        if (qFactor <= 0)
        {
            xx[0] = points[0];
            yy[0] = points[1];
            for (int i = 2; i < (numPoints << 1) - 1; i += 2)
            {
                xx[i / 2] = points[i];
                yy[i / 2] = points[1 + 1];
            }
        }
        else
        {
            int scale = 1 << qFactor;
            xx[0] = points[0]/scale;
            yy[0] = points[1]/scale;
            for (int i = 2; i < (numPoints << 1) - 1; i += 2)
            {
                xx[i / 2] = points[i]/scale;
                yy[i / 2] = points[1 + 1]/scale;
            }
        }

        this.drawPathOutline(xx, yy, isFilled);
    }

    public void drawPoint(int x0, int y0)
    {
        this.graphics.drawPoint(x0, y0);
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
        int[] xPositions = new int[]
        { x1, x2, x3 };
        int[] yPositions = new int[]
        { y1, y2, y3 };
        this.drawPathOutline(xPositions, yPositions, isFilled);
    }
    
    public void drawPolyline(int[] points, int numPoints, int w)
    {
        //it's right?
        for (int i = 0; i < numPoints; i+= 4)
        {
            drawLine(points[i], points[1], points[2], points[3], w);
        }
    }
    
    int[] xPts = new int[10];
    int[] yPts = new int[10];
    int temp;
    int delX;
    int delY;
    int angleIndex;
    
    public void drawLine(int x0, int y0, int x1, int y1, int w)
    {
        
        // make sure y0 > y1
        if (y0 < y1)
        {
            temp = y0;
            y0 = y1;
            y1 = temp;

            temp = x0;
            x0 = x1;
            x1 = temp;
        }

        delX = x0 - x1;
        //delY always >= 0
        delY = y0 - y1;

        if (delX > 0)
        {
            /**
             *   ---------------------> X
             *   |
             *   |  (x1,y1)
             *   |     *
             *   |      *
             *   |       *
             *   |        *
             *   |         *
             *   |          *
             *   |         (x0,y0) 
             *   |
             *   v  Y
             * 
             */
            
            if (delX > (delY << 1))
            {
                angleIndex = 0;
            }
            else if (delY > (delX << 1))
            {
                angleIndex = 2;
            }
            else
            {
                angleIndex = 1;
            }
            
        }
        else if (delX < 0)
        {
            /**
             *   ---------------------> X
             *   |
             *   |        (x1,y1)
             *   |          *
             *   |         *
             *   |        *
             *   |       *
             *   |      *
             *   |     *
             *   |   (x0,y0) 
             *   |
             *   v  Y
             * 
             */     
            
            
            
            if ((-delX) > (delY << 1))
            {
                angleIndex = 4;
            }
            else if (delY > ((-delX) << 1))
            {
                angleIndex = 2;
            }
            else
            {
                angleIndex = 3;
            }           
        }else{
            //delX == 0
            if(delY == 0){
                //the same point, return
                return;
            }else{
                angleIndex = 2;
            }           
        }       
        
        int[][] circleTable;
        switch (w)
        {
            case 1:
            case 2:
                // only draw a thin line
                // ((Graphics) g.getGraphics()).drawLine(x0, y0, x1, y1);
                drawLine(x0, y0, x1, y1);
                return;
            case 3:
                circleTable = CIRCLE_TABLE_WIDTH_3;
                break;
            case 4:
                circleTable = CIRCLE_TABLE_WIDTH_4;
                break;
            case 5:
                circleTable = CIRCLE_TABLE_WIDTH_5;
                break;
            case 6:
                circleTable = CIRCLE_TABLE_WIDTH_6;
                break;
            case 7:
                circleTable = CIRCLE_TABLE_WIDTH_7;
                break;
            case 8:
                circleTable = CIRCLE_TABLE_WIDTH_8;
                break;
            case 9:
                circleTable = CIRCLE_TABLE_WIDTH_9;
                break;
            case 10:
                circleTable = CIRCLE_TABLE_WIDTH_10;
                break;
            case 11:
                circleTable = CIRCLE_TABLE_WIDTH_11;
                break;
            default:
                circleTable = CIRCLE_TABLE_WIDTH_12;

        }       
        
        //now we get the two half circle        
        
        for (int i = 0; i < 5; i++, angleIndex++)
        {
            xPts[i] = x0 + circleTable[angleIndex][0];
            yPts[i] = y0 + circleTable[angleIndex][1];
        }
        
        angleIndex--;
        
        for (int i = 5; i < 10; i++, angleIndex++)
        {
            xPts[i] = x1 + circleTable[angleIndex][0];
            yPts[i] = y1 + circleTable[angleIndex][1];
        }
        
        // ((Graphics)g.getGraphics()).drawFilledPath(xPts, yPts, null, null);      
        
        drawFilledPath(xPts, yPts);
    }

    //3 pixel circle table
    private static final int[][]    CIRCLE_TABLE_WIDTH_3    =
                                                            {
                                                            { 0, -1 },
                                                            { 1, -1 },
                                                            { 1, 0 },
                                                            { 1, 1 },
                                                            { 0, 1 },
                                                            { -1, 1 },
                                                            { -1, 0 },
                                                            { -1, -1 },
                                                            { 0, -1 },
                                                            { 1, -1 },
                                                            { 1, 0 },
                                                            { 1, 1 },
                                                            { 0, 1 }, 
                                                            };
    
    
    //4 pixel circle table
    private static final int[][]    CIRCLE_TABLE_WIDTH_4    =
                                                            {
                                                            { 0, -1 },
                                                            { 1, -1 },
                                                            { 2, 0 },
                                                            { 2, 1 },
                                                            { 0, 2 },
                                                            { -1, 2 },
                                                            { -1, 0 },
                                                            { -1, -1 },
                                                            { 0, -1 },
                                                            { 1, -1 },
                                                            { 2, 0 },
                                                            { 2, 1 },
                                                            { 0, 2 }, 
                                                            };
    
    
    //5 pixel circle table
    private static final int[][]    CIRCLE_TABLE_WIDTH_5    =
                                                            {       
                                                            { 0, -2 },
                                                            { 1, -1 },
                                                            { 2, 0 },
                                                            { 1, 1 },
                                                            { 0, 2 },
                                                            { -1, 1 },
                                                            { -2, 0 },
                                                            { -1, -1 },                                                         
                                                            { 0, -2 },
                                                            { 1, -1 },
                                                            { 2, 0 },
                                                            { 1, 1 },
                                                            { 0, 2 },
                                                            };
    
    
    
    //6 pixel circle table
    private static final int[][]    CIRCLE_TABLE_WIDTH_6    =
                                                            {       
                                                            { 0, -2 },                                                          
                                                            { 1, -2 },
                                                            { 3, 0 },                                                           
                                                            { 2, 2 },                                                           
                                                            { 0, 3 },                                                           
                                                            { -1, 2 },                                                          
                                                            { -2, 0 },                                                          
                                                            { -1, -1 },                                                         
                                                            { 0, -2 },                                                          
                                                            { 1, -2 },
                                                            { 3, 0 },                                                           
                                                            { 2, 2 },                                                           
                                                            { 0, 3 },
                                                            };
    
    
    
    // 7 pixel circle table
    private static final int[][]    CIRCLE_TABLE_WIDTH_7    =
                                                            {       
                                                            { 0, -3 },
                                                            { 2, -2 },
                                                            { 3, 0 },
                                                            { 2, 2 },
                                                            { 0, 3 },
                                                            { -2, 2 },
                                                            { -3, 0 },
                                                            { -2, -2 },                                                         
                                                            { 0, -3 },
                                                            { 2, -2 },
                                                            { 3, 0 },
                                                            { 2, 2 },
                                                            { 0, 3 },
                                                            };
    
    
    //8 pixel circle table
    private static final int[][]    CIRCLE_TABLE_WIDTH_8    =
                                                            {       
                                                            { 0, -3 },
                                                            { 3, -2 },                                                          
                                                            { 4, 0 },                                                           
                                                            { 3, 3 },                                                           
                                                            { 0, 4 },
                                                            { -2, 3 },
                                                            { -3, 0 },
                                                            { -2, -2 },
                                                            { 0, -3 },
                                                            { 3, -2 },                                                          
                                                            { 4, 0 },                                                           
                                                            { 3, 3 },                                                           
                                                            { 0, 4 },
                                                            };
    
    
                                                
    // 9 pixel circle table
    private static final int[][]    CIRCLE_TABLE_WIDTH_9    =
                                                            {       
                                                            { 0, -4 },
                                                            { 3, -3 },
                                                            { 4, 0 },
                                                            { 3, 3 },
                                                            { 0, 4 },
                                                            { -3, 3 },
                                                            { -4, 0 },
                                                            { -3, -3 },                                                         
                                                            { 0, -4 },
                                                            { 3, -3 },
                                                            { 4, 0 },
                                                            { 3, 3 },
                                                            { 0, 4 },
                                                            };
    
    
    
    //10 pixel circle table
    private static final int[][]    CIRCLE_TABLE_WIDTH_10   =
                                                            {       
                                                            { 0, -4 },                                                          
                                                            { 4, -3 },
                                                            { 5, 0 },
                                                            { 4, 4 },
                                                            { 0, 5 },
                                                            { -3, 4 },
                                                            { -4, 0 },
                                                            { -3, -3 }, 
                                                            { 0, -4 },                                                          
                                                            { 4, -3 },
                                                            { 5, 0 },
                                                            { 4, 4 },
                                                            { 0, 5 },
                                                            };
    
    
    
    // 11 pixel circle table
    private static final int[][]    CIRCLE_TABLE_WIDTH_11   =
                                                            {       
                                                            { 0, -5 },
                                                            { 4, -4 },
                                                            { 5, 0 },
                                                            { 4, 4 },
                                                            { 0, 5 },
                                                            { -4, 4 },
                                                            { -5, 0 },
                                                            { -4, -4 },                                                         
                                                            { 0, -5 },
                                                            { 4, -4 },
                                                            { 5, 0 },
                                                            { 4, 4 },
                                                            { 0, 5 },
                                                            };
    
    // 12 pixel circle table
    private static final int[][]    CIRCLE_TABLE_WIDTH_12   =
                                                            {       
                                                            { 0, -5 },                                                          
                                                            { 4, -4 },                                                          
                                                            { 6, 0 },
                                                            { 5, 5 },                                                           
                                                            { 0, 6 },
                                                            { -4, 4 },                                                          
                                                            { -5, 0 },
                                                            { -4, -4 },
                                                            { 0, -5 },                                                          
                                                            { 4, -4 },                                                          
                                                            { 6, 0 },
                                                            { 5, 5 },                                                           
                                                            { 0, 6 },
                                                            };  

    public void drawRGB(int[] rgbData, int offset, int scanlength, int x, int y, int width, int height, boolean processAlpha)
    {
        this.graphics.drawARGB(rgbData, offset, scanlength, x, y, width, height);
    }

    public void drawRect(int x, int y, int width, int height)
    {
        this.graphics.drawRect(x, y, x + width - 1, y + height - 1);
    }

    public void drawRect(TnRect rect)
    {
        this.graphics.drawRect(rect.left, rect.top, rect.right - 1, rect.bottom - 1);
    }

    public void drawRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight)
    {
        this.graphics.drawRoundRect(x,y,width,height, arcWidth, arcHeight);
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

        if ((anchor & RIGHT) != 0)
        {
            x -= font.stringWidth(str);
        }

        if ((anchor & BOTTOM) != 0)
        {
            y -= font.getHeight();
        }

        Font nativeFont = (Font)this.font.getNativeFont();
        if (angle != 0)
        {
            int thetaFixed = Fixed32.toFP(angle);

            int cell_11 = Fixed32.cosd(thetaFixed);
            int cell_12 = -Fixed32.sind(thetaFixed);

            int cell_21 = Fixed32.sind(thetaFixed);
            int cell_22 = Fixed32.cosd(thetaFixed);

            int[] transform = new int[]
            { cell_11, cell_12, cell_21, cell_22, 0, 0 };

            nativeFont = Font.getDefault().derive(Font.PLAIN, nativeFont.getHeight(), Ui.UNITS_px, Font.ANTIALIAS_STANDARD, 0, transform);
        }
        this.graphics.setFont(nativeFont);
        this.graphics.drawText(str, x, y);
    }

    public void fillArcs(int xC, int yC, int d, float startAngle, float sweepAngle)
    {
        this.graphics.fillArc(xC - d, yC - d, 2 * d, 2 * d, (int)startAngle, (int)sweepAngle);
    }
    
    public void fillCircle(int xC, int yC, int d, boolean isClear)
    {
        this.graphics.fillArc(xC - d, yC - d, 2 * d, 2 * d, 0, 360);
    }

    public void fillRect(int x, int y, int width, int height)
    {
        this.graphics.fillRect(x, y, width, height);
    }

    public void fillRect(TnRect rect)
    {
        fillRect(rect.left, rect.top, rect.width(), rect.height());
    }

    public void fillRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight)
    {
        this.graphics.fillRoundRect(x,y,width,height, arcWidth, arcHeight);
    }

    public void fillTriangle(int x1, int y1, int x2, int y2, int x3, int y3)
    {
        int[] xPositions = new int[]
        { x1, x2, x3 };
        int[] yPositions = new int[]
        { y1, y2, y3 };
        this.graphics.drawFilledPath(xPositions, yPositions, null, null);
    }

    public int getColor()
    {
        return this.graphics.getColor();
    }

    public AbstractTnFont getFont()
    {
        return font;
    }

    public Object getGraphics()
    {
        return this.graphics;
    }

    public int getStrokeWidth()
    {
        return 0;
    }

    public int getTranslateX()
    {
        return this.graphics.getTranslateX();
    }

    public int getTranslateY()
    {
        return this.graphics.getTranslateY();
    }

    public void getClipBounds(TnRect clipBounds)
    {
        XYRect nativeClipBounds = this.graphics.getClippingRect();
        clipBounds.left = nativeClipBounds.x;
        clipBounds.top = nativeClipBounds.y;
        clipBounds.right = nativeClipBounds.x + nativeClipBounds.width;
        clipBounds.bottom = nativeClipBounds.y + nativeClipBounds.height;
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
        XYRect nativeClipBounds = this.graphics.getClippingRect();
        int clipX = nativeClipBounds.x;
        int clipY = nativeClipBounds.y;
        int clipWidth = nativeClipBounds.width;
        int clipHeight = nativeClipBounds.height;
        
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
        XYRect nativeClipBounds = this.graphics.getClippingRect();
        return nativeClipBounds.height;
    }

    public int getClipWidth()
    {
        XYRect nativeClipBounds = this.graphics.getClippingRect();
        return nativeClipBounds.width;
    }

    public int getClipX()
    {
        XYRect nativeClipBounds = this.graphics.getClippingRect();
        return nativeClipBounds.x;
    }

    public int getClipY()
    {
        XYRect nativeClipBounds = this.graphics.getClippingRect();
        return nativeClipBounds.y;
    }

    public void pushClip(int x, int y, int width, int height)
    {
        this.graphics.pushContext(x, y, width, height, 0, 0);
    }

    public void popClip()
    {
        this.graphics.popContext();
    }

    public void setColor(int RGB)
    {
        this.graphics.setGlobalAlpha(0xFF);
        this.graphics.setColor(RGB);
    }

    public void setColor(int alpha, int red, int green, int blue)
    {
        this.graphics.setGlobalAlpha(alpha);
        this.graphics.setColor((red << 16) | (green << 8) | blue);
    }

    public void setFont(AbstractTnFont font)
    {
        this.font = font;
    }

    public void setGraphics(Object graphics)
    {
        this.graphics = (Graphics) graphics;
    }

    public void setStrokeWidth(int width)
    {
        //TODO
//        this.paint.setStrokeWidth(width);
    }

    public void translate(int x, int y)
    {
        this.graphics.translate(x, y);
    }
    
    public void rotate(int degree)
    {
        throw new UnsupportedOperationException();
    }
    
    public void rotate(int degree, int x, int y)
    {
        throw new UnsupportedOperationException();
    }

    public void setStrokeCap(int strokeCap)
    {
//        switch (strokeCap)
//        {
//            case CAP_BUTT:
//                paint.setStrokeCap(Cap.BUTT);
//                break;
//            case CAP_ROUND:
//                paint.setStrokeCap(Cap.ROUND);
//                break;
//            case CAP_SQUARE:
//                paint.setStrokeCap(Cap.SQUARE);
//                break;
//        }
    }

    public int getStrokeCap()
    {
//        Cap cap = paint.getStrokeCap();
//        if(Cap.BUTT.equals(cap))
//            return CAP_BUTT;
//        else if(Cap.ROUND.equals(cap))
//            return CAP_ROUND;
//        else if(Cap.SQUARE.equals(cap))
//            return CAP_SQUARE;
        
        return CAP_BUTT;
    }

    public void setStrokeJoin(int strokeJoin)
    {
//        switch (strokeJoin)
//        {
//            case JOIN_BEVEL:
//                paint.setStrokeJoin(Join.BEVEL);
//                break;
//            case JOIN_MITER:
//                paint.setStrokeJoin(Join.MITER);
//                break;
//            case JOIN_ROUND:
//                paint.setStrokeJoin(Join.ROUND);
//                break;
//        }
    }

    public int getStrokeJoin()
    {
//        Join join = paint.getStrokeJoin();
//        if(Join.BEVEL.equals(join))
//            return JOIN_BEVEL;
//        else if(Join.MITER.equals(join))
//            return JOIN_MITER;
//        else if(Join.ROUND.equals(join))
//            return JOIN_ROUND;
        
        return JOIN_MITER;
    }

    public Object getNativeGraphics()
    {
        return this.graphics;
    }
    
    public void setAntiAlias(boolean aa)
    {
        this.graphics.setDrawingStyle(Graphics.DRAWSTYLE_ANTIALIASED, true);
    }
    
    public boolean isAntiAlias()
    {
        return this.graphics.isDrawingStyleSet(Graphics.DRAWSTYLE_ANTIALIASED);
    }

}
