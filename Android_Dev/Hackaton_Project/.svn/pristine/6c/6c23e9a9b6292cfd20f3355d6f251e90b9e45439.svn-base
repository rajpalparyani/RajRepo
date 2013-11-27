/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * TurnMapUiDecorator.java
 *
 */
package com.telenav.module.nav.turnmap;

import com.telenav.module.AppConfigHelper;
import com.telenav.mvc.AbstractCommonUiDecorator;
import com.telenav.tnui.core.AbstractTnUiHelper;
import com.telenav.tnui.core.TnUiArgs.TnUiArgAdapter;
import com.telenav.util.PrimitiveTypeCache;

/**
 *@author yning
 *@date 2010-12-13
 */
public class TurnMapUiDecorator extends AbstractCommonUiDecorator
{
    private final static int ID_TURN_MAP_INFO_WIDTH = 11;
    private final static int ID_TURN_MAP_INFO_HEIGHT = 12;
    private final static int ID_TURN_MAP_INFO_X = 13;
    private final static int ID_TURN_MAP_INFO_Y = 14;
    private final static int ID_TURN_MAP_INDEX_WIDTH = 15;
    private final static int ID_TURN_MAP_INDEX_HEIGHT = 16;
    private final static int ID_TURN_MAP_INDEX_X = 17;
    private final static int ID_TURN_MAP_INDEX_Y = 18;
    private final static int ID_TURN_MAP_ZOOM_IN_X = 20;
    private final static int ID_TURN_MAP_ZOOM_IN_Y = 21;
    private final static int ID_TURN_MAP_FIT_MAP_SIZE = 25;
    private final static int ID_TURN_MAP_ZOOM_ICON_WIDTH = 26;
    private final static int ID_TURN_MAP_ZOOM_ICON_HEIGHT = 27;
    
    public TnUiArgAdapter TURN_MAP_INFO_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_TURN_MAP_INFO_WIDTH), this);
    public TnUiArgAdapter TURN_MAP_INFO_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_TURN_MAP_INFO_HEIGHT), this);
    public TnUiArgAdapter TURN_MAP_INFO_X = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_TURN_MAP_INFO_X), this);
    public TnUiArgAdapter TURN_MAP_INFO_Y = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_TURN_MAP_INFO_Y), this);
    public TnUiArgAdapter TURN_MAP_INDEX_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_TURN_MAP_INDEX_WIDTH), this);
    public TnUiArgAdapter TURN_MAP_INDEX_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_TURN_MAP_INDEX_HEIGHT), this);
    public TnUiArgAdapter TURN_MAP_INDEX_X = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_TURN_MAP_INDEX_X), this);
    public TnUiArgAdapter TURN_MAP_INDEX_Y = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_TURN_MAP_INDEX_Y), this);
    public TnUiArgAdapter TURN_MAP_ZOOM_IN_X = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_TURN_MAP_ZOOM_IN_X), this);
    public TnUiArgAdapter TURN_MAP_ZOOM_IN_Y = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_TURN_MAP_ZOOM_IN_Y), this);
    public TnUiArgAdapter TURN_MAP_FIT_MAP_SIZE = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_TURN_MAP_FIT_MAP_SIZE), this);
    public TnUiArgAdapter TURN_MAP_ZOOM_ICON_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_TURN_MAP_ZOOM_ICON_WIDTH), this);
    public TnUiArgAdapter TURN_MAP_ZOOM_ICON_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_TURN_MAP_ZOOM_ICON_HEIGHT), this);
   
    protected Object decorateDelegate(TnUiArgAdapter args)
    {
        int key = ((Integer) args.getKey()).intValue();
        switch(key)
        {
            case ID_TURN_MAP_INFO_WIDTH:
            {
                int screenWidth = SCREEN_WIDTH.getInt();
                int orientation = ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation();
                int width;
                if(orientation == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                {
                    width = screenWidth * 9625 / 10000;
                }
                else
                {
                    width = screenWidth * 975 / 1000;
                }
                return PrimitiveTypeCache.valueOf(width);
            }
            case ID_TURN_MAP_INFO_HEIGHT:
            {
                int screenHeight = SCREEN_HEIGHT.getInt();
                int orientation = ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation();
                int height;
                if(orientation == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                {
                    height = screenHeight * 167 / 1000;
                }
                else
                {
                    height = screenHeight * 1875 / 10000;
                }
                return PrimitiveTypeCache.valueOf(height);
            }
            case ID_TURN_MAP_INFO_X:
            {
                int x = (SCREEN_WIDTH.getInt() - TURN_MAP_INFO_WIDTH.getInt()) / 2;
                return PrimitiveTypeCache.valueOf(x);
            }
            case ID_TURN_MAP_INFO_Y:
            {
                int y = AppConfigHelper.getMaxDisplaySize() * 1 / 100;
                return PrimitiveTypeCache.valueOf(y);
            }
            case ID_TURN_MAP_INDEX_X:
            {
                int x = (SCREEN_WIDTH.getInt() - TURN_MAP_INDEX_WIDTH.getInt()) / 2;
                return PrimitiveTypeCache.valueOf(x);
            }
            case ID_TURN_MAP_INDEX_Y:
            {
                int topPadding = AppConfigHelper.getMinDisplaySize() * 16 / 1000;
                int y = TURN_MAP_INFO_Y.getInt() + TURN_MAP_INFO_HEIGHT.getInt() + topPadding;
                return PrimitiveTypeCache.valueOf(y);
            }
            case ID_TURN_MAP_INDEX_WIDTH:
            {
                int width = AppConfigHelper.getMinDisplaySize() * 281 / 1000;
                return PrimitiveTypeCache.valueOf(width);
            }
            case ID_TURN_MAP_INDEX_HEIGHT:
            {
                int height = AppConfigHelper.getMinDisplaySize() * 69 / 1000;
                return PrimitiveTypeCache.valueOf(height);
            }
            case ID_TURN_MAP_ZOOM_IN_X:
            {
                return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth() - 20* AppConfigHelper.getMinDisplaySize()/320 - TURN_MAP_ZOOM_ICON_WIDTH.getInt());               
            }
            case ID_TURN_MAP_ZOOM_IN_Y:
            {            	
                int orientation = ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation();
                int y;
                if (orientation == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                {
                    y = (AppConfigHelper.getDisplayHeight() - TURN_MAP_ZOOM_ICON_HEIGHT.getInt()) / 2;
                }
                else
                {
                    y = AppConfigHelper.getDisplayHeight() / 2 - 2 * TURN_MAP_ZOOM_ICON_HEIGHT.getInt();
                }
                return PrimitiveTypeCache.valueOf(y);
            }
            case ID_TURN_MAP_FIT_MAP_SIZE:
            {
                return TURN_MAP_ZOOM_ICON_WIDTH.getInt();
            }
            case ID_TURN_MAP_ZOOM_ICON_WIDTH:
            {
                return PrimitiveTypeCache.valueOf(AppConfigHelper.getMinDisplaySize() * 46 / 320);
            }
            case ID_TURN_MAP_ZOOM_ICON_HEIGHT:
            {
                int shadow = 6;
                return PrimitiveTypeCache.valueOf(TURN_MAP_ZOOM_ICON_WIDTH.getInt() - shadow);
            }
        }
        return null;
    }
    

}
