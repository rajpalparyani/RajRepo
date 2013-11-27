/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * MockTnGraphics.java
 *
 */
package com.telenav.ui.mock;

import com.telenav.tnui.graphics.AbstractTnFont;
import com.telenav.tnui.graphics.AbstractTnGraphics;
import com.telenav.tnui.graphics.AbstractTnImage;
import com.telenav.tnui.graphics.TnPoint;
import com.telenav.tnui.graphics.TnRect;

/**
 * @author hchai
 * @date 2011-11-4
 */
public class MockTnGraphics extends AbstractTnGraphics
{
    AbstractTnFont tnFont = new MockTnFont(0, 0, 0);

    public void pushClip(TnRect rect)
    {

    }

    public void drawChar(char character, int x, int y, int anchor)
    {

    }

    public void drawChar(char character, int x, int y, int anchor, int angle)
    {

    }

    public void drawImage(AbstractTnImage img, int x, int y, int anchor)
    {

    }

    public void drawImage(AbstractTnImage img, TnRect src, TnRect dst)
    {

    }

    public void drawLine(int x1, int y1, int x2, int y2)
    {

    }

    public void drawLine(TnPoint p1, TnPoint p2)
    {

    }

    public void drawRect(int x, int y, int width, int height)
    {

    }

    public void drawRect(TnRect rect)
    {

    }

    public void drawRGB(int[] rgbData, int offset, int scanlength, int x, int y,
            int width, int height, boolean hasAlpha)
    {

    }

    public void drawString(String str, int x, int y, int anchor)
    {

    }

    public void fillArcs(int xC, int yC, int d, float startAngle, float sweepAngle)
    {

    }

    public void fillRect(int x, int y, int width, int height)
    {

    }

    public void fillRect(TnRect rect)
    {

    }

    public void fillTriangle(int x1, int y1, int x2, int y2, int x3, int y3)
    {

    }

    public void fillCircle(int xC, int yC, int d, boolean isClear)
    {

    }

    public void drawCircle(int xC, int yC, int d)
    {

    }

    public int getColor()
    {
        return 0;
    }

    public AbstractTnFont getFont()
    {
        return tnFont;
    }

    public int getStrokeWidth()
    {
        return 0;
    }

    public int getTranslateX()
    {
        return 0;
    }

    public int getTranslateY()
    {
        return 0;
    }

    public void pushClip(int x, int y, int width, int height)
    {

    }

    public void popClip()
    {

    }

    public void setColor(int RGB)
    {

    }

    public void setColor(int alpha, int red, int green, int blue)
    {

    }

    public void setFont(AbstractTnFont font)
    {

    }

    public void setStrokeWidth(int width)
    {

    }

    public void translate(int x, int y)
    {

    }

    public boolean isIntersectWithClip(TnRect clipBounds, int x, int y, int width,
            int height)
    {
        return false;
    }

    public boolean isIntersectWithClip(int x, int y, int width, int height)
    {
        return false;
    }

    public void getClipBounds(TnRect clipBounds)
    {

    }

    public void drawPathOutline(int[] xx, int[] yy)
    {

    }

    public void drawPathOutline(int[] points, int numPoints, int qFactor)
    {

    }

    public void drawFilledPath(int[] xx, int[] yy)
    {

    }

    public void drawFilledPath(int[] points, int numPoints, int qFactor)
    {

    }

    public void drawPoint(int x0, int y0)
    {

    }

    public void fillRoundRect(int x, int y, int width, int height, int arcWidth,
            int arcHeight)
    {

    }

    public void drawRoundRect(int x, int y, int width, int height, int arcWidth,
            int arcHeight)
    {

    }

    public void drawRoundTriangle(int x1, int y1, int x2, int y2, int x3, int y3,
            int radius)
    {

    }

    public void fillRoundTriangle(int x1, int y1, int x2, int y2, int x3, int y3,
            int radius)
    {

    }

    public void drawPolyline(int[] points, int numPoints, int w)
    {

    }

    public void setStrokeCap(int strokeCap)
    {

    }

    public int getStrokeCap()
    {
        return 0;
    }

    public void setStrokeJoin(int strokeJoin)
    {

    }

    public int getStrokeJoin()
    {
        return 0;
    }

    public void setAntiAlias(boolean isAntiAlias)
    {

    }

    public boolean isAntiAlias()
    {
        return false;
    }

    public Object getNativeGraphics()
    {
        return null;
    }

    public void setGraphics(Object graphics)
    {

    }

    public void rotate(int degree)
    {

    }

	public void rotate(int degree, int x, int y) 
	{
		
	}

}
