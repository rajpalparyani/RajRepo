/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * NinePatchImageDecorator.java
 *
 */
package com.telenav.searchwidget.ui;

import com.telenav.datatypes.DataUtil;
import com.telenav.i18n.ResourceBundle;
import com.telenav.searchwidget.data.cache.ImageCacheManager;
import com.telenav.searchwidget.res.INinePatchImageRes;
import com.telenav.searchwidget.res.ResourceManager;
import com.telenav.tnui.core.TnUiArgs.ITnUiArgsDecorator;
import com.telenav.tnui.core.TnUiArgs.TnUiArgAdapter;
import com.telenav.tnui.graphics.AbstractTnGraphicsHelper;
import com.telenav.tnui.graphics.AbstractTnImage;
import com.telenav.tnui.graphics.TnNinePatchImage;
import com.telenav.util.PrimitiveTypeCache;

/**
 * @author fqming (fqming@telenav.cn)
 * @date Aug 16, 2010
 */
public class NinePatchImageDecorator implements ITnUiArgsDecorator
{
    public static NinePatchImageDecorator instance = new NinePatchImageDecorator();

    public static TnUiArgAdapter COMMON_BUTTON = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(INinePatchImageRes.BUTTON_ID
            + INinePatchImageRes.ID_UNFOCUSED), instance);

    private final static String[] FILTER =
    { "_unfocused", "_focused" };
    
    private final static String WRADIANT = "wradiant";

    public Object decorate(TnUiArgAdapter args)
    {
        int key = ((Integer) args.getKey()).intValue();
        ResourceBundle bundle = ResourceManager.getInstance().getCurrentBundle();
        AbstractTnImage wholeImage = (AbstractTnImage) ImageCacheManager.getInstance().getNinePatchImageCache()
                .get(PrimitiveTypeCache.valueOf(key - 1));
        if (wholeImage == null)
        {
            AbstractTnImage[] ninepatchImages = null;
            AbstractTnImage[] radiantImages = null;
            String lastPrefix = null;
            byte[] lastImageData = null;
            for (int i = 0; i < 18; i++)
            {
                int id = key + i;
                // such as button_unfocused_wradiant_top.png
                String pngName = (String) INinePatchImageRes.NINE_PATCH_IMAGES.get(PrimitiveTypeCache.valueOf(id));
                if (pngName == null || pngName.equals(""))
                    continue;
                
                int index = pngName.indexOf(FILTER[0]);
                if (index != -1)
                {
                    index += FILTER[0].length();
                }
                else
                {
                    index = pngName.indexOf(FILTER[1]);
                    index += FILTER[1].length();
                }
                String prefix = pngName.substring(0, index);

                if (lastPrefix == null || !prefix.equals(lastPrefix))
                {
                    byte[] imageData = bundle.getGenericImage(prefix + ".image", INinePatchImageRes.FAMILY_NINE_PATCH);
                    // byte[] imageData = bundle.getBinary(key, prefix + ".bin", null);
                    lastImageData = imageData;
                    lastPrefix = prefix;
                }

                String positionStr = pngName.substring(pngName.indexOf('_', index) + 1, pngName.lastIndexOf('.'));
                int wrandiantIndex = positionStr.indexOf(WRADIANT);
                if(wrandiantIndex != -1)
                {
                    positionStr = positionStr.substring(positionStr.indexOf('_', wrandiantIndex) + 1);
                }
                int position = ((Integer)INinePatchImageRes.NINE_PATCH_POSITIONS.get(positionStr)).intValue();
                int tag = i % 9;
                int wradiant = 0;
                if (i <= TnNinePatchImage.LEFT_BOTTOM)
                {
                    if (ninepatchImages == null)
                    {
                        ninepatchImages = new AbstractTnImage[9];
                    }
                    
                    AbstractTnImage image = createNinePatchImageFromBinary(position, wradiant, lastImageData);
                    
//                    if(NinePatchImageDecorator.QUICK_FIND_BUTTON_UNFOCUSED.equals(args) ||
//                            NinePatchImageDecorator.QUICK_FIND_BUTTON_FOCUSED.equals(args))
//                    {
//                        image = ImageDecorator.checkStretchImage(image);
//                    }
                    
                    ninepatchImages[tag] = image;
                }
                else
                {
                    wradiant = 1;
                    if (radiantImages == null)
                    {
                        radiantImages = new AbstractTnImage[9];
                    }
                    
                    AbstractTnImage image = createNinePatchImageFromBinary(position, wradiant, lastImageData);
                    
//                    if(NinePatchImageDecorator.QUICK_FIND_BUTTON_UNFOCUSED.equals(args) ||
//                            NinePatchImageDecorator.QUICK_FIND_BUTTON_FOCUSED.equals(args))
//                    {
//                        image = ImageDecorator.checkStretchImage(image);
//                    }
                    
                    radiantImages[tag] = image;
                }
            }
            int drawMode = TnNinePatchImage.DRAW_SCALE;
            if (ninepatchImages[TnNinePatchImage.LEFT] != null || ninepatchImages[TnNinePatchImage.LEFT_TOP] != null
                    || ninepatchImages[TnNinePatchImage.LEFT_BOTTOM] != null || ninepatchImages[TnNinePatchImage.RIGHT] != null
                    || ninepatchImages[TnNinePatchImage.RIGHT_BOTTOM] != null || ninepatchImages[TnNinePatchImage.RIGHT_TOP] != null)
                drawMode = drawMode | TnNinePatchImage.DRAW_ROUNDCORNER;
            TnNinePatchImage tnNinePatchImage = new TnNinePatchImage(ninepatchImages[TnNinePatchImage.LEFT],
                    ninepatchImages[TnNinePatchImage.TOP], ninepatchImages[TnNinePatchImage.RIGHT],
                    ninepatchImages[TnNinePatchImage.BOTTOM], ninepatchImages[TnNinePatchImage.CENTER],
                    ninepatchImages[TnNinePatchImage.LEFT_TOP], ninepatchImages[TnNinePatchImage.RIGHT_TOP],
                    ninepatchImages[TnNinePatchImage.RIGHT_BOTTOM], ninepatchImages[TnNinePatchImage.LEFT_BOTTOM]);
            if (radiantImages != null)
            {
                for (int i = 0; i < radiantImages.length; i++)
                {
                    tnNinePatchImage.setRadiant(i, radiantImages[i]);
                }
            }
            tnNinePatchImage.setDrawMode(drawMode);
            ImageCacheManager.getInstance().getNinePatchImageCache().put(PrimitiveTypeCache.valueOf(key - 1), tnNinePatchImage);
            return tnNinePatchImage;

        }
        else
            return wholeImage;

    }

    private AbstractTnImage createNinePatchImageFromBinary(int tag, int wradiant, byte[] imageData)
    {
        int index = 0;
        while (index < imageData.length && imageData[index] != (byte) 0xff)
        {
            int position = imageData[index++];
            if (position == tag)
            {
                int wradiantTag = imageData[index++];
                if (wradiantTag == wradiant)
                {
                    long offset = DataUtil.readLong(imageData, index);
                    index += 8;
                    long length = DataUtil.readLong(imageData, index);
                    // System.out.println(length);
                    byte[] data = new byte[(int) length];
                    System.arraycopy(imageData, (int) offset, data, 0, (int) length);
                    AbstractTnImage image = AbstractTnGraphicsHelper.getInstance().createImage(data);
                    return image;
                }
                index--;
            }
            
            
            
            
            
            
            
            
            index += 17;
        }
        return null;
    }

}
