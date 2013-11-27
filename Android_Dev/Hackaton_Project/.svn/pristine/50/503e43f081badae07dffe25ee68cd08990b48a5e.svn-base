/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * ImageDecorator.java
 *
 */
package com.telenav.searchwidget.ui;

import java.util.Hashtable;

import com.telenav.i18n.ResourceBundle;
import com.telenav.searchwidget.data.cache.ImageCacheManager;
import com.telenav.searchwidget.res.ResourceManager;
import com.telenav.tnui.core.TnUiArgs.ITnUiArgsDecorator;
import com.telenav.tnui.core.TnUiArgs.TnUiArgAdapter;
import com.telenav.tnui.graphics.AbstractTnGraphicsHelper;
import com.telenav.tnui.graphics.AbstractTnImage;

/**
 *@author bduan
 *@date Aug 19, 2010
 */
public class ImageDecorator implements ITnUiArgsDecorator
{
    public static Hashtable iconMapping = new Hashtable();
    public static ImageDecorator instance = new ImageDecorator();
    
    private static final int TURN_ICON_BIG_INDEX = 100;
    
    public static TnUiArgAdapter IMG_CLOSE_BLUR = new TnUiArgAdapter(ISpecialImageRes.CLOSE_ICON_BLUE, instance);

    
    
    public Object decorate(TnUiArgAdapter args)
    {
        String key = (String) args.getKey();
        AbstractTnImage image = (AbstractTnImage) ImageCacheManager.getInstance().getImageCache().get(key);
        if (image == null)
        {
            ResourceBundle bundle = ResourceManager.getInstance().getCurrentBundle();
            byte[] imageData = bundle.getGenericImage(key, ISpecialImageRes.getSpecialImageFamily());
            if (imageData != null)
            {
                image = AbstractTnGraphicsHelper.getInstance().createImage(imageData);
                
                image = checkStretchImage(image);
                
                ImageCacheManager.getInstance().getImageCache().put(key, image);
            }
        }

        return image;
    }

    protected static AbstractTnImage checkStretchImage(AbstractTnImage image)
    {
        if (ISpecialImageRes.isNeedStretch && image != null)
        {
            int w = image.getWidth() * ISpecialImageRes.numerator / ISpecialImageRes.denominator;
            int h = image.getHeight() * ISpecialImageRes.numerator / ISpecialImageRes.denominator;
            
            image = image.createScaledImage(w > 0 ? w : 1, h > 0 ? h : 1);
        }
        
        return image;
    }

}
