/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * INinePatchImageRes.java
 *
 */
package com.telenav.searchwidget.res;

import java.util.Hashtable;

import com.telenav.tnui.graphics.TnNinePatchImage;
import com.telenav.util.PrimitiveTypeCache;

/**
 *@author fqming (fqming@telenav.cn)
 *@date Aug 10, 2010
 */
public class INinePatchImageRes
{
    //=====================================nine patch family=====================================//
    public final static String FAMILY_NINE_PATCH = "common";
    
   //=====================================below are nine patch's id constraint=====================================//
    public final static int BASE_ID_NINE_PATCH = 1000000;
    
    public final static int ID_UNFOCUSED = 10;

    public final static int ID_UNFOCUSED_RADIANT = ID_UNFOCUSED + TnNinePatchImage.LEFT_BOTTOM + 1;

    public final static int ID_FOCUSED = 30;

    public final static int ID_FOCUSED_RADIANT = ID_FOCUSED + TnNinePatchImage.LEFT_BOTTOM + 1;

    //=====================================below are nine patch component's id=====================================//
    public final static int BUTTON_ID = BASE_ID_NINE_PATCH + 100;
    
    //=====================================below are nine patch array=====================================//
    public final static Hashtable NINE_PATCH_IMAGES = new Hashtable();
    
    public final static Hashtable NINE_PATCH_POSITIONS = new Hashtable();
    
    //init buttons
    static
    {
        //Below are positions
        {
            NINE_PATCH_POSITIONS.put("left", PrimitiveTypeCache.valueOf(TnNinePatchImage.LEFT));
            NINE_PATCH_POSITIONS.put("top", PrimitiveTypeCache.valueOf(TnNinePatchImage.TOP));
            NINE_PATCH_POSITIONS.put("right", PrimitiveTypeCache.valueOf(TnNinePatchImage.RIGHT));
            NINE_PATCH_POSITIONS.put("bottom", PrimitiveTypeCache.valueOf(TnNinePatchImage.BOTTOM));
            NINE_PATCH_POSITIONS.put("center", PrimitiveTypeCache.valueOf(TnNinePatchImage.CENTER));
            NINE_PATCH_POSITIONS.put("left_top", PrimitiveTypeCache.valueOf(TnNinePatchImage.LEFT_TOP));
            NINE_PATCH_POSITIONS.put("right_top", PrimitiveTypeCache.valueOf(TnNinePatchImage.RIGHT_TOP));
            NINE_PATCH_POSITIONS.put("right_bottom", PrimitiveTypeCache.valueOf(TnNinePatchImage.RIGHT_BOTTOM));
            NINE_PATCH_POSITIONS.put("left_bottom", PrimitiveTypeCache.valueOf(TnNinePatchImage.LEFT_BOTTOM));
        }
        //Below are unfocused images
        {
            NINE_PATCH_IMAGES.put(PrimitiveTypeCache.valueOf(BUTTON_ID + ID_UNFOCUSED + TnNinePatchImage.LEFT), "widgets_bg_unfocused_left.png");
            NINE_PATCH_IMAGES.put(PrimitiveTypeCache.valueOf(BUTTON_ID + ID_UNFOCUSED + TnNinePatchImage.TOP), "widgets_bg_unfocused_top.png");
            NINE_PATCH_IMAGES.put(PrimitiveTypeCache.valueOf(BUTTON_ID + ID_UNFOCUSED + TnNinePatchImage.RIGHT), "widgets_bg_unfocused_right.png");
            NINE_PATCH_IMAGES.put(PrimitiveTypeCache.valueOf(BUTTON_ID + ID_UNFOCUSED + TnNinePatchImage.BOTTOM), "widgets_bg_unfocused_bottom.png");
            NINE_PATCH_IMAGES.put(PrimitiveTypeCache.valueOf(BUTTON_ID + ID_UNFOCUSED + TnNinePatchImage.CENTER), "widgets_bg_unfocused_center.png");
            NINE_PATCH_IMAGES.put(PrimitiveTypeCache.valueOf(BUTTON_ID + ID_UNFOCUSED + TnNinePatchImage.LEFT_TOP), "widgets_bg_unfocused_left_top.png");
            NINE_PATCH_IMAGES.put(PrimitiveTypeCache.valueOf(BUTTON_ID + ID_UNFOCUSED + TnNinePatchImage.RIGHT_TOP), "widgets_bg_unfocused_right_top.png");
            NINE_PATCH_IMAGES.put(PrimitiveTypeCache.valueOf(BUTTON_ID + ID_UNFOCUSED + TnNinePatchImage.RIGHT_BOTTOM), "widgets_bg_unfocused_right_bottom.png");
            NINE_PATCH_IMAGES.put(PrimitiveTypeCache.valueOf(BUTTON_ID + ID_UNFOCUSED + TnNinePatchImage.LEFT_BOTTOM), "widgets_bg_unfocused_left_bottom.png");
        }
    }
}
