/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * TnStretchPatchPainter.java
 *
 */
package com.telenav.tnui.graphics;

import java.util.Vector;

import com.telenav.tnui.graphics.AbstractTnGraphics;
import com.telenav.tnui.graphics.AbstractTnImage;
import com.telenav.tnui.graphics.TnNinePatchImage;
import com.telenav.tnui.graphics.TnRect;
import com.telenav.tnui.graphics.StretchImageSpliter;
import com.telenav.tnui.graphics.TnStretchRect;
import com.telenav.logger.Logger;

/**
 * @author yren (yren@telenav.cn)
 * @date 2011-8-5
 */
abstract class TnStretchPatchPainter
{
    public static void drawStretchBitmap(AbstractTnGraphics graphics, TnNinePatchImage stretchPatchImage, TnRect rect,StretchImageSpliter spliter)
    {
        int width = rect.right - rect.left;
        int height = rect.bottom - rect.top;

        if (width <= 0 || height <= 0)
            return;
        
        graphics.translate(rect.left, rect.top);
        try
        {
            draw(graphics, stretchPatchImage, width, height,spliter);
        }
        catch(Exception e)
        {
            Logger.log(TnStretchPatchPainter.class.getName(), e);
        }
        graphics.translate(-rect.left, -rect.top);
    }

    private static void draw(AbstractTnGraphics graphics, TnNinePatchImage stretchPatchImage, int width, int height,StretchImageSpliter spliter)
    {
        AbstractTnImage image = stretchPatchImage.getPatch(0);
        if(image == null || image.getWidth() == 0 || image.getHeight()==0)
        {
        	return;
        }
        if (stretchPatchImage.splitedRects.size() < 1)
        {
            int horizonTiledCount = 0;
            int verticalTiledCount = 0;
            if ((width - image.getWidth() * 2) % image.getWidth() != 0)
            {
                horizonTiledCount = (width - image.getWidth()) / image.getWidth() + 1;
            }
            else
            {
                horizonTiledCount = (width - image.getWidth()) / image.getWidth();
            }

            if ((height - image.getHeight()) % image.getHeight() != 0)
            {
                verticalTiledCount = (height - image.getHeight()) / image.getHeight() + 1;
            }
            else
            {
                verticalTiledCount = (height - image.getHeight()) / image.getHeight();
            }

            graphics.pushClip(0, 0, width, height);
            int tileX = 0;
            int tileY = 0;
            for (int i = 0; i < horizonTiledCount + 1; i++)
            {
                tileY = 0;
                if (i != 0)
                {
                    tileX += image.getWidth();
                }
                for (int j = 0; j < verticalTiledCount + 1; j++)
                {
                    graphics.drawImage(stretchPatchImage.getPatch(0), tileX, tileY, AbstractTnGraphics.LEFT | AbstractTnGraphics.TOP);
                    tileY += image.getHeight();
                }
            }
        }
        else
        {
            float totalHorizenStrechableAreaWidth = spliter.getTotalHorizonStrechableAreaWidth();
            float totalVerticalStrechableAreaHeight = spliter.getTotalVerticalStrechableAreaHeight();
            int horizonAreaIndex = 0;
            int verticalAreaIndex = 0;

            TnStretchRect srcRect;
            TnRect distRect;
            int leftOffSet = 0;
            int topOffSet = 0;
            if (width <= image.getWidth() - totalHorizenStrechableAreaWidth)
            {
                width = (int) (image.getWidth() - totalHorizenStrechableAreaWidth);
            }
            if (height <= image.getHeight() - totalVerticalStrechableAreaHeight)
            {
                height = (int) (image.getHeight() - totalVerticalStrechableAreaHeight);
            }

            for (int i = 0; i < stretchPatchImage.splitedRects.size(); i++)
            {
                srcRect = ((TnStretchRect) stretchPatchImage.splitedRects.elementAt(i));
                if (srcRect.right - srcRect.left < 1)
                {
                    continue;
                }

                distRect = new TnRect(srcRect.left + leftOffSet, srcRect.top + topOffSet, srcRect.right + leftOffSet, srcRect.bottom
                        + topOffSet);
                if (srcRect.getHorizonStretchable())
                {
                    int tempDistRectRight = distRect.right;
                    float rectWidth = spliter.getMainHorizonStretechableRect(horizonAreaIndex) == null ? 0 : spliter
                            .getMainHorizonStretechableRect(horizonAreaIndex).width();
                    float tempRight = (float) (rectWidth / totalHorizenStrechableAreaWidth);
                    distRect.right += Math.round(((width - image.getWidth()) * tempRight));
                    if (i % spliter.getVerticalSplitedAreaCount() == spliter.getVerticalSplitedAreaCount() - 1)
                    {
                        leftOffSet += (distRect.right - tempDistRectRight);
                        horizonAreaIndex += 1;
                    }
                }
                if (srcRect.getVerticalStretchable() && srcRect.bottom - srcRect.top > 0)
                {
                    int tempDistRectBottom = distRect.bottom;
                    float rectHeight = spliter.getMainVerticalStretechableRect(verticalAreaIndex) == null ? 0 : spliter
                            .getMainVerticalStretechableRect(verticalAreaIndex).height();
                    float tempBottom = 0.0f;
                    tempBottom = (float) (rectHeight / totalVerticalStrechableAreaHeight);
                    distRect.bottom += Math.round((height - image.getHeight()) * tempBottom);
                    topOffSet += (distRect.bottom - tempDistRectBottom);
                    verticalAreaIndex += 1;
                }

                if (i % spliter.getVerticalSplitedAreaCount() == spliter.getVerticalSplitedAreaCount() - 1)
                {
                    topOffSet = 0;
                    verticalAreaIndex = 0;
                }
                
                if (srcRect.bottom - srcRect.top < 1)
                {
                    continue;
                }
                
                if (stretchPatchImage.getDrawMode() == TnNinePatchImage.DRAW_CARVEL)
                {
                    int horizonTiledCount = (distRect.right - distRect.left) % (srcRect.right - srcRect.left) == 0 ? (distRect.right - distRect.left)
                            / (srcRect.right - srcRect.left)
                            : (distRect.right - distRect.left) / (srcRect.right - srcRect.left) + 1;

                    int verticalTiledCount = (distRect.bottom - distRect.top) % (srcRect.bottom - srcRect.top) == 0 ? (distRect.bottom - distRect.top)
                            / (srcRect.bottom - srcRect.top)
                            : (distRect.bottom - distRect.top) / (srcRect.bottom - srcRect.top) + 1;

                    graphics.pushClip(distRect);
                    int tileX = distRect.left;
                    int tileY = distRect.top;

                    //TODO need to be improved later to use pixels[] instead of create a new bitmap by YRen
                    AbstractTnImage tempImage = AbstractTnGraphicsHelper.getInstance().createImage(stretchPatchImage.getPatch(0),
                        srcRect.left, srcRect.top, srcRect.right - srcRect.left, srcRect.bottom - srcRect.top);
                    for (int m = 0; m < horizonTiledCount; m++)
                    {
                        tileY = distRect.top;
                        if (m != 0)
                        {
                            tileX += (srcRect.right - srcRect.left);
                        }
                        for (int n = 0; n < verticalTiledCount; n++)
                        {
                            graphics.drawImage(tempImage, tileX, tileY, AbstractTnGraphics.LEFT | AbstractTnGraphics.TOP);
                            tileY += (srcRect.bottom - srcRect.top);
                        }
                    }
                    graphics.popClip();
                }
                else if(stretchPatchImage.getDrawMode() == TnNinePatchImage.DRAW_SCALE)
                {
                    graphics.drawImage(stretchPatchImage.getPatch(0), (TnRect) srcRect, (TnRect) distRect);
                }
            }
        }
    }
}
