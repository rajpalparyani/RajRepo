/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * TnNinePatchPainter.java
 *
 */
package com.telenav.tnui.graphics;

/**
 * 
 *@author bduan (bduan@telenav.cn)
 *@date 2010-7-13
 */
abstract class TnNinePatchPainter
{
    /**
     * Draw NinePach image to specific rect.
     * 
     * @param graphics the graphics with which to draw.
     * @param ninePatchImage the ninepatch image
     * @param rect the specific rect.
     */
    public static void drawNinePatchBitmap(AbstractTnGraphics graphics, TnNinePatchImage ninePatchImage, TnRect rect)
    {
        int width = rect.right - rect.left;
        int height = rect.bottom - rect.top;

        if (width <= 0 || height <= 0)
            return;

        graphics.translate(rect.left, rect.top);
        drawNinePatchBitmap(graphics, ninePatchImage, width, height);
        graphics.translate(-rect.left, -rect.top);
    }

    private static void drawNinePatchBitmap(AbstractTnGraphics graphics, TnNinePatchImage ninePatchImage, int width, int height)
    {
        int oldColor = graphics.getColor();
        graphics.setColor(TnColor.WHITE);

        if ((ninePatchImage.getDrawMode() & TnNinePatchImage.DRAW_ROUNDCORNER) != 0)
        {
            // draw four corners
            drawCorners(graphics, ninePatchImage, width, height);
        }

        // draw four edges
        drawEdges(graphics, ninePatchImage, width, height);

        // draw certer
        drawCenter(graphics, ninePatchImage, width, height);

        // draw top light
        if (ninePatchImage.getRadiant(TnNinePatchImage.TOP) != null || ninePatchImage.getRadiant(TnNinePatchImage.LEFT_TOP) != null
                || ninePatchImage.getRadiant(TnNinePatchImage.RIGHT_TOP) != null)
        {
            drawLight(graphics, ninePatchImage, width, height, TnNinePatchImage.TOP, ninePatchImage.getPatch(TnNinePatchImage.TOP).getHeight());
        }

        // draw bottom light
        if (ninePatchImage.getRadiant(TnNinePatchImage.LEFT_BOTTOM) != null || ninePatchImage.getRadiant(TnNinePatchImage.BOTTOM) != null
                || ninePatchImage.getRadiant(TnNinePatchImage.RIGHT_BOTTOM) != null)
        {
            int y = height - ninePatchImage.getRadiant(TnNinePatchImage.BOTTOM).getHeight()
                    - ninePatchImage.getPatch(TnNinePatchImage.BOTTOM).getHeight();
            if (y < ninePatchImage.getPatch(TnNinePatchImage.TOP).getHeight())
            {
                y = ninePatchImage.getPatch(TnNinePatchImage.TOP).getHeight();
            }
            drawLight(graphics, ninePatchImage, width, height, TnNinePatchImage.BOTTOM, y);
        }

        // draw center light
        if (ninePatchImage.getRadiant(TnNinePatchImage.LEFT) != null || ninePatchImage.getRadiant(TnNinePatchImage.CENTER) != null
                || ninePatchImage.getRadiant(TnNinePatchImage.RIGHT) != null)
        {
            int topHeight = 0;
            if(ninePatchImage.getPatch(TnNinePatchImage.TOP) != null)
            {
                topHeight = ninePatchImage.getPatch(TnNinePatchImage.TOP).getHeight();
            }
            else if(ninePatchImage.getPatch(TnNinePatchImage.LEFT_TOP) != null)
            {
                topHeight = ninePatchImage.getPatch(TnNinePatchImage.LEFT_TOP).getHeight();
            }
            else if(ninePatchImage.getPatch(TnNinePatchImage.RIGHT_TOP) != null)
            {
                topHeight = ninePatchImage.getPatch(TnNinePatchImage.RIGHT_TOP).getHeight();
            }
            
            drawLight(graphics, ninePatchImage, width, height, TnNinePatchImage.CENTER, topHeight);
        }
        
        graphics.setColor(oldColor);
    }

    private static void drawLight(AbstractTnGraphics graphic, TnNinePatchImage ninePatchImage, int width, int height, int type, int y)
    {
        AbstractTnImage leftLight = null;
        AbstractTnImage centerLight = null;
        AbstractTnImage rightLight = null;
        if (type == TnNinePatchImage.TOP)
        {
            leftLight = ninePatchImage.getRadiant(TnNinePatchImage.LEFT_TOP);
            centerLight = ninePatchImage.getRadiant(TnNinePatchImage.TOP);
            rightLight = ninePatchImage.getRadiant(TnNinePatchImage.RIGHT_TOP);
        }
        else if (type == TnNinePatchImage.BOTTOM)
        {
            leftLight = ninePatchImage.getRadiant(TnNinePatchImage.LEFT_BOTTOM);
            centerLight = ninePatchImage.getRadiant(TnNinePatchImage.BOTTOM);
            rightLight = ninePatchImage.getRadiant(TnNinePatchImage.RIGHT_BOTTOM);
        }
        else
        {
            leftLight = ninePatchImage.getRadiant(TnNinePatchImage.LEFT);
            centerLight = ninePatchImage.getRadiant(TnNinePatchImage.CENTER);
            rightLight = ninePatchImage.getRadiant(TnNinePatchImage.RIGHT);
        }

        TnRect tempRect = new TnRect();
        
        tempRect.top = y;
        tempRect.bottom = y + (centerLight == null ? 0 : centerLight.getHeight());
        int bottom = height;
        if(ninePatchImage.getPatch(TnNinePatchImage.BOTTOM) != null)
            bottom = height - ninePatchImage.getPatch(TnNinePatchImage.BOTTOM).getHeight(); 
        if (tempRect.bottom > bottom || type == TnNinePatchImage.CENTER)
        {
            tempRect.bottom = bottom;
        }

        // drawLeft
        if(leftLight != null)
        {
            tempRect.left = 0;
            tempRect.right = leftLight.getWidth();
            graphic.drawImage(leftLight, null, tempRect);
        }

        if(centerLight != null)
        {
            // drawCenter
            tempRect.left = ninePatchImage.getPatch(TnNinePatchImage.LEFT) == null ? 0 : ninePatchImage.getPatch(TnNinePatchImage.LEFT).getWidth();
            tempRect.right = ninePatchImage.getPatch(TnNinePatchImage.RIGHT) == null ? width : (width - ninePatchImage.getPatch(TnNinePatchImage.RIGHT).getWidth());
            
            TnRect centerRect = new TnRect(tempRect);
            centerRect.top = tempRect.top;
            if ((ninePatchImage.getDrawMode() & TnNinePatchImage.DRAW_SCALE) != 0)
            {
                drawImage(centerLight, null, centerRect, graphic);
            }
            else
            {
                drawEdge(graphic, centerLight, centerRect, false);
            }
        }


        if(rightLight != null)
        {
            // drawRight
            tempRect.left = width - ninePatchImage.getPatch(TnNinePatchImage.RIGHT).getWidth();
            tempRect.right = width;
            graphic.drawImage(rightLight, null, tempRect);
        }

        leftLight = null;
        centerLight = null;
        rightLight = null;
    }

    private static void drawCenter(AbstractTnGraphics graphics, TnNinePatchImage ninePatchImage, int width, int height)
    {
        TnRect tempRect = new TnRect();

        if ((ninePatchImage.getDrawMode() & TnNinePatchImage.DRAW_ROUNDCORNER) != 0)
        {
            AbstractTnImage leftImage = ninePatchImage.getPatch(TnNinePatchImage.LEFT);
            AbstractTnImage leftTopImage = ninePatchImage.getPatch(TnNinePatchImage.LEFT_TOP);
            AbstractTnImage leftBottomImage = ninePatchImage.getPatch(TnNinePatchImage.LEFT_BOTTOM);
            AbstractTnImage rightTopImage = ninePatchImage.getPatch(TnNinePatchImage.RIGHT_TOP);
            AbstractTnImage topImage = ninePatchImage.getPatch(TnNinePatchImage.TOP);
            AbstractTnImage rightBottomImage = ninePatchImage.getPatch(TnNinePatchImage.RIGHT_BOTTOM);
            AbstractTnImage rightImage = ninePatchImage.getPatch(TnNinePatchImage.RIGHT);
            AbstractTnImage bottomImage = ninePatchImage.getPatch(TnNinePatchImage.BOTTOM);

            tempRect.left = leftTopImage != null ? leftTopImage.getWidth() : (leftBottomImage != null ? leftBottomImage.getWidth()
                    : (leftImage != null ? leftImage.getWidth() : 0));
            tempRect.right = width
                    - (rightTopImage != null ? rightTopImage.getWidth() : (rightBottomImage != null ? rightBottomImage.getWidth()
                            : (rightImage != null ? rightImage.getWidth() : 0)));
            tempRect.top = topImage != null ? topImage.getHeight() : (leftTopImage != null ? leftTopImage.getHeight()
                    : (rightTopImage != null ? rightTopImage.getHeight() : 0));
            tempRect.bottom = height
                    - (bottomImage != null ? bottomImage.getHeight() : (leftBottomImage != null ? leftBottomImage.getHeight()
                            : (rightBottomImage != null ? rightBottomImage.getHeight() : 0)));
        }
        else
        {
            AbstractTnImage leftImage = ninePatchImage.getPatch(TnNinePatchImage.LEFT);
            AbstractTnImage rightImage = ninePatchImage.getPatch(TnNinePatchImage.RIGHT);
            AbstractTnImage topImage = ninePatchImage.getPatch(TnNinePatchImage.TOP);
            AbstractTnImage bottomImage = ninePatchImage.getPatch(TnNinePatchImage.BOTTOM);
            
            if(leftImage == null)
                tempRect.left = 0;
            else
                tempRect.left = leftImage.getWidth();
            
            if(rightImage == null)
                tempRect.right = width;
            else
                tempRect.right = width - rightImage.getWidth();
            
            if(topImage == null)
                tempRect.top = 0;
            else
                tempRect.top = topImage.getHeight();
            
            if(bottomImage == null)
                tempRect.bottom = height;
            else
                tempRect.bottom = height - bottomImage.getHeight();
        }

        AbstractTnImage centerImage = ninePatchImage.getPatch(TnNinePatchImage.CENTER);
        if ((ninePatchImage.getDrawMode() & TnNinePatchImage.DRAW_SCALE) != 0)
        {
            drawImage(centerImage, null, tempRect, graphics);
        }
        else
        {
            if (centerImage.getHeight() > tempRect.height())
            {
                // in this case, the center is higher then custom. so just draw a edge can fill center.
                drawEdge(graphics, centerImage, tempRect, true);
            }
            else
            {
                int bottom = tempRect.bottom;
                tempRect.bottom = tempRect.top + centerImage.getHeight();
                drawEdge(graphics, centerImage, tempRect, false);
                do
                {
                    // and then use above edge draw overall center.
                    tempRect.top += centerImage.getHeight();
                    tempRect.bottom += centerImage.getHeight();
                    if (tempRect.bottom > bottom)
                    {
                        tempRect.bottom = bottom;
                    }

                    drawEdge(graphics, centerImage, tempRect, false);
                } while (tempRect.bottom < bottom);
            }
        }
    }

    private static void drawEdges(AbstractTnGraphics graphics, TnNinePatchImage ninePatchImage, int width, int height)
    {
        boolean isRoundCorner = (ninePatchImage.getDrawMode() & TnNinePatchImage.DRAW_ROUNDCORNER) != 0;
        boolean isScale = (ninePatchImage.getDrawMode() & TnNinePatchImage.DRAW_SCALE) != 0;
        TnRect tempRect = null;
        if (isRoundCorner)
        {
            AbstractTnImage leftImage = ninePatchImage.getPatch(TnNinePatchImage.LEFT);
            AbstractTnImage leftTopImage = ninePatchImage.getPatch(TnNinePatchImage.LEFT_TOP);
            AbstractTnImage leftBottomImage = ninePatchImage.getPatch(TnNinePatchImage.LEFT_BOTTOM);
            AbstractTnImage rightTopImage = ninePatchImage.getPatch(TnNinePatchImage.RIGHT_TOP);
            AbstractTnImage topImage = ninePatchImage.getPatch(TnNinePatchImage.TOP);
            AbstractTnImage rightBottomImage = ninePatchImage.getPatch(TnNinePatchImage.RIGHT_BOTTOM);
            AbstractTnImage rightImage = ninePatchImage.getPatch(TnNinePatchImage.RIGHT);
            AbstractTnImage bottomImage = ninePatchImage.getPatch(TnNinePatchImage.BOTTOM);
            
            if(leftImage != null)
            {
                tempRect = new TnRect();
                tempRect.left = 0;
                tempRect.top = leftTopImage != null ? leftTopImage.getHeight() : (rightTopImage != null ? rightTopImage.getHeight() : (topImage != null ? topImage.getHeight() : 0));
                tempRect.right = leftTopImage != null ? leftTopImage.getWidth(): (leftBottomImage != null ? leftBottomImage.getWidth() : leftImage.getWidth());
                tempRect.bottom = height - (leftBottomImage != null ? leftBottomImage.getHeight() : (rightBottomImage != null ? rightBottomImage.getHeight() : (bottomImage != null ? bottomImage.getHeight() : 0)));

                if (isScale)
                {
                    drawImage(leftImage, null, tempRect, graphics);
                }
                else
                {
                    drawEdge(graphics, leftImage, tempRect, true);
                }
            }

            if(topImage != null)
            {
                tempRect = new TnRect();
                tempRect.left = leftTopImage != null ? leftTopImage.getWidth() : (leftBottomImage != null ? leftBottomImage.getWidth() : (leftImage != null ? leftImage.getWidth() : 0));
                tempRect.top = 0;
                tempRect.right = width - (rightTopImage != null ? rightTopImage.getWidth() : (rightBottomImage != null ? rightBottomImage.getWidth() : (rightImage != null ? rightImage.getWidth() : 0)));
                tempRect.bottom = leftTopImage != null ? leftTopImage.getHeight() : (rightTopImage != null ? rightTopImage.getHeight() : topImage.getHeight());

                if (isScale)
                {
                    drawImage(topImage, null, tempRect, graphics);
                }
                else
                {
                    drawEdge(graphics, topImage, tempRect, false);
                }
            }
            
            if(rightImage != null)
            {
                tempRect = new TnRect();
                tempRect.left = width - (rightTopImage != null ? rightTopImage.getWidth() : (rightBottomImage != null ? rightBottomImage.getWidth() : rightImage.getWidth()));
                tempRect.top = rightTopImage != null ? rightTopImage.getHeight() : (leftTopImage != null ? leftTopImage.getHeight() : topImage != null ? topImage.getHeight() : 0);
                tempRect.right = width;
                tempRect.bottom = height - (rightBottomImage != null ? rightBottomImage.getHeight() : (leftBottomImage != null ? leftBottomImage.getHeight() : (bottomImage != null ? bottomImage.getHeight() : 0)));

                if (isScale)
                {
                    drawImage(rightImage, null, tempRect, graphics);
                }
                else
                {
                    drawEdge(graphics, rightImage, tempRect, true);
                }
            }
            
            if(bottomImage != null)
            {
                tempRect = new TnRect();
                tempRect.left = leftBottomImage != null ? leftBottomImage.getWidth() : (leftTopImage != null ? leftTopImage.getWidth() : (leftImage != null ? leftImage.getWidth() : 0));
                tempRect.top = height - (leftBottomImage != null ? leftBottomImage.getHeight() : (rightBottomImage != null ? rightBottomImage.getHeight() : bottomImage.getHeight()));
                tempRect.right = width - (rightBottomImage != null ? rightBottomImage.getWidth() : (rightTopImage != null ? rightTopImage.getWidth() : (rightImage != null ? rightImage.getWidth() : 0)));
                tempRect.bottom = height;

                if (isScale)
                {
                    drawImage(bottomImage, null, tempRect, graphics);
                }
                else
                {
                    drawEdge(graphics, bottomImage, tempRect, false);
                }
            }
        }
        else
        {
            AbstractTnImage leftImage = ninePatchImage.getPatch(TnNinePatchImage.LEFT);
            AbstractTnImage topImage = ninePatchImage.getPatch(TnNinePatchImage.TOP);
            AbstractTnImage rightImage = ninePatchImage.getPatch(TnNinePatchImage.RIGHT);
            AbstractTnImage bottomImage = ninePatchImage.getPatch(TnNinePatchImage.BOTTOM);
            
            // draw left edge.
            if(leftImage != null)
            {
                tempRect = new TnRect(0, 0, leftImage.getWidth(), height);
        
                if (isScale)
                {
                    drawImage(leftImage, null, tempRect, graphics);
                }
                else
                {
                    drawEdge(graphics, leftImage, tempRect, true);
                }
            }
            
            if(rightImage != null)
            {
                // draw right edge.
                tempRect = new TnRect(width - rightImage.getWidth(), 0, width, height);
        
                if (isScale)
                {
                    drawImage(rightImage, null, tempRect, graphics);
                }
                else
                {
                    drawEdge(graphics, rightImage, tempRect, true);
                }
            }
            
            //draw top edge
            if(topImage != null)
            {
                tempRect = new TnRect(0, 0, width, topImage.getHeight());
                if (isScale)
                {
                    drawImage(topImage, null, tempRect, graphics);
                }
                else
                {
                    drawEdge(graphics, topImage, tempRect, false);
                }
            }
    
            // draw bottom edge.
            if(bottomImage != null)
            {
                tempRect = new TnRect(0, height - bottomImage.getHeight(), width, height);
                if (isScale)
                {
                    drawImage(bottomImage, null, tempRect, graphics);
                }
                else
                {
                    drawEdge(graphics, bottomImage, tempRect, false);
                }
            }
        }
    }

    private static void drawEdge(AbstractTnGraphics graphics, AbstractTnImage source, TnRect rect, boolean isVertical)
    {
        if (source == null || rect == null)
            return;

        TnRect tempRect = new TnRect();
        TnRect srcRect = new TnRect(0, 0, source.getWidth(), source.getHeight());
        int i = 0;
        int step = 0;
        int gap = 0;

        srcRect.left = 0;
        srcRect.top = 0;
        srcRect.right = source.getWidth();
        srcRect.bottom = source.getHeight();

        srcRect.top = rect.top;

        if (source.getWidth() > (rect.right - rect.left))
        {
            srcRect.right = rect.right - rect.left;
        }
        if (source.getHeight() > (rect.bottom - rect.top))
        {
            srcRect.bottom = rect.bottom - rect.top;
        }

        if (isVertical)
        {
            step = srcRect.bottom;
            tempRect.left = rect.left;
            tempRect.right = rect.right;

            while (step > 0 && rect.top + step * (i + 1) <= rect.bottom)
            {
                tempRect.top = rect.top + i * step;
                tempRect.bottom = rect.top + (i + 1) * step;
                graphics.drawImage(source, srcRect, tempRect);
                i++;
            }

            gap = rect.bottom - (rect.top + step * i);
            if (gap > 0)
            {
                tempRect.top = rect.top + i * step - (step - gap);
                tempRect.bottom = rect.top + (i + 1) * step - (step - gap);
                graphics.drawImage(source, srcRect, tempRect);
            }

        }
        else
        {
            step = srcRect.right;
            tempRect.top = rect.top;
            tempRect.bottom = rect.bottom;

            while (step > 0 && rect.left + step * (i + 1) <= rect.right)
            {
                tempRect.left = rect.left + i * step;
                tempRect.right = rect.left + (i + 1) * step;
                graphics.drawImage(source, srcRect, tempRect);
                i++;
            }

            gap = rect.right - (rect.left + step * i);
            if (gap > 0)
            {
                tempRect.left = rect.left + i * step - (step - gap);
                tempRect.right = rect.left + (i + 1) * step - (step - gap);
                graphics.drawImage(source, srcRect, tempRect);
            }

        }
    }

    private static void drawImage(AbstractTnImage image, TnRect src, TnRect dest, AbstractTnGraphics graphics)
    {
        graphics.drawImage(image, src, dest);
    }

    private static void drawCorners(AbstractTnGraphics graphics, TnNinePatchImage ninePatchImage, int width, int height)
    {
        AbstractTnImage leftTopImage = ninePatchImage.getPatch(TnNinePatchImage.LEFT_TOP);
        if(leftTopImage != null)
        {
            graphics.drawImage(leftTopImage, null, new TnRect(0, 0, leftTopImage.getWidth(), leftTopImage.getHeight()));
        }
        AbstractTnImage rightTopImage = ninePatchImage.getPatch(TnNinePatchImage.RIGHT_TOP);
        if(rightTopImage != null)
        {
            graphics.drawImage(rightTopImage, null, new TnRect(width - rightTopImage.getWidth(), 0, width, rightTopImage.getHeight()));
        }
        AbstractTnImage leftBottom = ninePatchImage.getPatch(TnNinePatchImage.LEFT_BOTTOM);
        if(leftBottom != null)
        {
            graphics.drawImage(leftBottom, null, new TnRect(0, height - leftBottom.getHeight(), leftBottom.getWidth(), height));
        }
        AbstractTnImage rightBottom = ninePatchImage.getPatch(TnNinePatchImage.RIGHT_BOTTOM);
        if(rightBottom != null)
        {
            graphics.drawImage(rightBottom, null, new TnRect(width - rightBottom.getWidth(), height - rightBottom.getHeight(), width,
                    height));
        }
    }
}
