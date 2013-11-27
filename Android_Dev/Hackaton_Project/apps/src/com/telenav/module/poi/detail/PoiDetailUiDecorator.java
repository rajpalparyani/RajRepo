/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * PoiDetailUiDecorator.java
 *
 */
package com.telenav.module.poi.detail;

import com.telenav.module.AppConfigHelper;
import com.telenav.module.browsersdk.BrowserSdkUiDecorator;
import com.telenav.tnui.core.AbstractTnUiHelper;
import com.telenav.tnui.core.TnUiArgs.TnUiArgAdapter;
import com.telenav.util.PrimitiveTypeCache;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2011-1-24
 */
public class PoiDetailUiDecorator extends BrowserSdkUiDecorator
{
    public static final int POI_GALLERY_WIDTH_INDEX = 10;

    public static final int POI_GALLERY_HEIGHT_INDEX = 11;

    public static final int POI_GALLERY_TITLE_HEIGHT_INDEX = 12;
    
    public static final int POI_DETAIL_TITLE_H_PADDING_INDEX = 15;

    public TnUiArgAdapter POI_GALLERY_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(POI_GALLERY_WIDTH_INDEX), this);

    public TnUiArgAdapter POI_GALLERY_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(POI_GALLERY_HEIGHT_INDEX), this);

    public TnUiArgAdapter POI_GALLERY_TITLE_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(POI_GALLERY_TITLE_HEIGHT_INDEX), this);
    
    public static int getValue(int galleryKeyValue, int orientation)
    {
        int key = galleryKeyValue;
        int width = AppConfigHelper.getDisplayWidth();
        int height = AppConfigHelper.getDisplayHeight();
        int maxLength = Math.max(width, height);
        int minLength = Math.min(width, height);
        switch (key)
        {
            case POI_GALLERY_WIDTH_INDEX:
            {
                if (orientation == AbstractTnUiHelper.ORIENTATION_LANDSCAPE)
                {
                    return (maxLength * 1000 / 1000);
                }
                else
                {
                    return (minLength * 1000 / 1000);
                }
            }
            case POI_GALLERY_HEIGHT_INDEX:
            {
                if (orientation == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                {
                    return (maxLength * 10000 / 10000);
                }
                else
                {
                    return (minLength * 10000 / 10000);
                }
            }
            case POI_GALLERY_TITLE_HEIGHT_INDEX:
            {
                if (orientation == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                {
                    return (maxLength * 810 / 10000);
                }
                else
                {
                    return (minLength * 1259 / 10000);
                }
            }
        }
        return 0;
    }
    
    protected Object decorateDelegate(TnUiArgAdapter args)
    {
        int orientation = ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation();
        int key = ((Integer) args.getKey()).intValue();
        
        switch(key)
        {
            case POI_DETAIL_TITLE_H_PADDING_INDEX:
            {
                int hPadding;
                int galleryWidth = getValue(POI_GALLERY_WIDTH_INDEX, orientation);
                if(orientation == AbstractTnUiHelper.ORIENTATION_LANDSCAPE)
                {
                    hPadding = galleryWidth * 44 / 1000;
                }
                else
                {
                    hPadding = galleryWidth * 144 / 1000;
                }
                
                return PrimitiveTypeCache.valueOf(hPadding);
            }
        }
        return getValue(key, orientation);
    }

}
