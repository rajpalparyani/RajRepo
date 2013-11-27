/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * AddFavoriteUiDecorator.java
 *
 */
package com.telenav.module.ac.favorite.favoriteeditor;

import com.telenav.module.AppConfigHelper;
import com.telenav.mvc.AbstractCommonUiDecorator;
import com.telenav.tnui.core.AbstractTnUiHelper;
import com.telenav.tnui.core.TnUiArgs.TnUiArgAdapter;
import com.telenav.ui.UiFactory;
import com.telenav.util.PrimitiveTypeCache;

/**
 * @author wzhu (wzhu@telenav.cn)
 * @date 2010-10-28
 */
class FavoriteEditorUiDecorator extends AbstractCommonUiDecorator
{

    private final static int ID_TITLE_CONTAINER_HEIGHT = 1;

    private final static int ID_TEXTFIELD_EDIT_FAVORITE_WIDTH = 2;

    private final static int ID_TEXTFIELD_EDIT_FAVORITE_HEIGHT = 3;

    private final static int ID_CONTENTCONTAINER_EDIT_FAVORITE_HEIGHT = 4;

    private final static int ID_CONTAINER_EDIT_FAVORITE_WIDTH = 6;

    private final static int ID_CONTAINER_EDIT_FAVORITE_HEIGHT = 7;

    private final static int ID_CHECKBOX_EDIT_FAVORIVITE_HEIGHT = 8;

    private final static int ID_LABEL_EDIT_FAVORITE_SUBTITLE_WIDTH = 9;

    private final static int ID_BOTTOMCONTAINER_EDIT_FAVORITE_HEIGHT = 10;

    private final static int ID_BOTTOMBUTTON_EDIT_FAVORITE_WIDTH = 11;

    private final static int ID_BOTTOMNULLFIELD_EDIT_FAVORITE_WIDTH = 12;

    private final static int ID_MESSAGEBOX_TOP_HEIGHT = 13;

    private final static int ID_MESSAGEBOX_BOTTOM_HEIGHT = 14;

    private final static int ID_POPUP_BUTTON_WIDTH = 15;
    
    private final static int ID_NULLFIELD_WIDTH = 16;
    
    private final static int ID_POPUP_WIDTH = 17;
    
    private final static int ID_POPUP_TOP_CONTAINER_HEIGHT = 18;
    
    private final static int ID_POPUP_BOTTOM_CONTAINER_HEIGHT = 19;
    
    private final static int ID_POPUP_BUTTON_HEIGHT = 20;
    
    private final static int ID_EDIT_FAV_BUTTON_HEIGHT = 21;
    
    private final static int ID_ADDRESS_INFO_HEIGHT = 22;
    
    private final static int ID_NULLFIELD_HEIGHT = 24;
    
    private final static int ID_BUTON_HEIGHT = 25;
    
    private final static int ID_BUTTON_CONTAINER_HEIGHT = 26;
    
    private final static int ID_BUTTON_EDIT_CATEGORY_WIDTH = 27;
    
    private final static int ID_NULLFIELD_EDIT_CATEGORY_WIDTH = 28;
    
    private final static int ID_POPUP_TITLE_HEIGHT = 29;
    
    private final static int ID_EDIT_FAVORITE_IMAGE_CONTAINER_WIDTH = 31;
    
    private final static int ID_EDIT_FAVORITE_SUBTITLE_HEIGHT = 33;
    
    private final static int ID_EDIT_FAVORITE_TOP_CONTAINER_HEIGHT = 34;
    
    private final static int ID_MULTILINE_WIDTH = 35;
    
    private final static int ID_MULTILINE_HEIGHT = 36; 
    
    private final static int ID_PURE_WHITE_BG_HEIGHT = 37;     
    
    private final static int ID_POPUP_POSITION_Y = 38;
    
    private final static int ID_INFO_LABEL_HEIGHT = 39;
    
    private final static int ID_TOP_CONTAINER_NULLFIEDL_WIDTH = 40;
    
    private final static int ID_BUTTON_PADDING = 41;
    
    private final static int ID_PANEL_CONTAINER_HEIGHT = 42;
    
    private final static int ID_EDIT_FAVORITE_TOP_CONTAINER_WIDTH = 43;
    
    private final static int ID_EDIT_FAVORITE_ICON_CONTAINER_HEIGHT = 44;
    
    private final static int ID_EDIT_FAVORITE_ICON_NULL_FIELD_WIDTH = 45;
    
    public TnUiArgAdapter TOP_CONTAINER_NULLFIEDL_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_TOP_CONTAINER_NULLFIEDL_WIDTH), this);
    
    public TnUiArgAdapter TITLE_CONTAINER_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_TITLE_CONTAINER_HEIGHT), this);

    public TnUiArgAdapter TEXTFIELD_EDIT_FAVORITE_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_TEXTFIELD_EDIT_FAVORITE_WIDTH), this);

    public TnUiArgAdapter TEXTFIELD_EDIT_FAVORITE_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_TEXTFIELD_EDIT_FAVORITE_HEIGHT), this);

    public TnUiArgAdapter CONTENTCONTAINER_EDIT_FAVORITE_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_CONTENTCONTAINER_EDIT_FAVORITE_HEIGHT),
            this);

    public TnUiArgAdapter CONTAINER_EDIT_FAVORITE_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_CONTAINER_EDIT_FAVORITE_WIDTH), this);

    public TnUiArgAdapter CONTAINER_EDIT_FAVORITE_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_CONTAINER_EDIT_FAVORITE_HEIGHT), this);

    public TnUiArgAdapter CHECKBOX_EDIT_FAVORIVITE_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_CHECKBOX_EDIT_FAVORIVITE_HEIGHT), this);

    public TnUiArgAdapter LABEL_EDIT_FAVORITE_SUBTITLE_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_LABEL_EDIT_FAVORITE_SUBTITLE_WIDTH), this);

    public TnUiArgAdapter BOTTOMCONTAINER_EDIT_FAVORITE_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_BOTTOMCONTAINER_EDIT_FAVORITE_HEIGHT),
            this);

    public TnUiArgAdapter BOTTOMBUTTON_EDIT_FAVORITE_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_BOTTOMBUTTON_EDIT_FAVORITE_WIDTH), this);

    public TnUiArgAdapter BOTTOMNULLFIELD_EDIT_FAVORITE_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_BOTTOMNULLFIELD_EDIT_FAVORITE_WIDTH),
            this);

    public TnUiArgAdapter MESSAGEBOX_TOP_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_MESSAGEBOX_TOP_HEIGHT), this);

    public TnUiArgAdapter MESSAGEBOX_BOTTOM_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_MESSAGEBOX_BOTTOM_HEIGHT), this);

    public TnUiArgAdapter POPUP_BUTTON_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_POPUP_BUTTON_WIDTH), this);
    
    public TnUiArgAdapter NULLFIELD_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_NULLFIELD_WIDTH), this);
    
    public TnUiArgAdapter POPUP_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_POPUP_WIDTH), this);
    
    public TnUiArgAdapter POPUP_TOP_CONTAINER_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_POPUP_TOP_CONTAINER_HEIGHT), this);
    
    public TnUiArgAdapter POPUP_BOTTOM_CONTAINER_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_POPUP_BOTTOM_CONTAINER_HEIGHT), this);
    
    public TnUiArgAdapter POPUP_BUTTON_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_POPUP_BUTTON_HEIGHT), this);
    
    public TnUiArgAdapter EDIT_FAV_BUTTON_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_EDIT_FAV_BUTTON_HEIGHT), this);
    
    public TnUiArgAdapter ADDRESS_INFO_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_ADDRESS_INFO_HEIGHT), this);
    
    public TnUiArgAdapter NULLFIELD_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_NULLFIELD_HEIGHT), this);
    
    public TnUiArgAdapter BUTON_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_BUTON_HEIGHT), this);
    
    public TnUiArgAdapter BUTTON_CONTAINER_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_BUTTON_CONTAINER_HEIGHT), this);
    
    public TnUiArgAdapter BUTTON_EDIT_CATEGORY_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_BUTTON_EDIT_CATEGORY_WIDTH), this);
    
    public TnUiArgAdapter NULLFIELD_EDIT_CATEGORY_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_NULLFIELD_EDIT_CATEGORY_WIDTH), this);
    
    public TnUiArgAdapter POPUP_TITLE_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_POPUP_TITLE_HEIGHT), this);
    
    public TnUiArgAdapter EDIT_FAVORITE_IMAGE_CONTAINER_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_EDIT_FAVORITE_IMAGE_CONTAINER_WIDTH), this);
    
    public TnUiArgAdapter EDIT_FAVORITE_SUBTITLE_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_EDIT_FAVORITE_SUBTITLE_HEIGHT), this);
    
    public TnUiArgAdapter EDIT_FAVORITE_TOP_CONTAINER_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_EDIT_FAVORITE_TOP_CONTAINER_HEIGHT), this);
    
    public TnUiArgAdapter MULTILINE_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_MULTILINE_WIDTH), this);
    
    public TnUiArgAdapter MULTILINE_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_MULTILINE_HEIGHT), this);
    
	public TnUiArgAdapter PURE_WHITE_BG_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_PURE_WHITE_BG_HEIGHT), this);
    
    public TnUiArgAdapter POPUP_POSITION_Y = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_POPUP_POSITION_Y), this);
    
    public TnUiArgAdapter INFO_LABEL_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_INFO_LABEL_HEIGHT), this);
    
    public TnUiArgAdapter BUTTON_PADDING = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_BUTTON_PADDING), this);
    
    public TnUiArgAdapter PANEL_CONTAINER_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_PANEL_CONTAINER_HEIGHT), this);
    
    public TnUiArgAdapter EDIT_FAVORITE_TOP_CONTAINER_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_EDIT_FAVORITE_TOP_CONTAINER_WIDTH), this);
    
    public TnUiArgAdapter EDIT_FAVORITE_ICON_CONTAINER_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_EDIT_FAVORITE_ICON_CONTAINER_HEIGHT), this);

    public TnUiArgAdapter EDIT_FAVORITE_ICON_NULL_FIELD_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_EDIT_FAVORITE_ICON_NULL_FIELD_WIDTH), this);
    
    private final static int SCALE = 1000;
    
    public FavoriteEditorUiDecorator()
    {
    }

    protected Object decorateDelegate(TnUiArgAdapter args)
    {
        int key = ((Integer) args.getKey()).intValue();
        switch (key)
        {
            case ID_TITLE_CONTAINER_HEIGHT:
            {
                if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 100 / SCALE);
                else
                {
                    if (AppConfigHelper.isTabletSize())
                    {
                        return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 938 / 10000);
                    }
                    else
                    {
                        return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 130 / SCALE);
                    }
                }
            }
            case ID_TEXTFIELD_EDIT_FAVORITE_WIDTH:
            {
                int max = AppConfigHelper.getMaxDisplaySize();
                int padding = 40 * max / 480;
                if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth() * 950 / SCALE - padding);
                else
                {
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth() * 850 / SCALE - padding);
                }
            }
            case ID_TEXTFIELD_EDIT_FAVORITE_HEIGHT:
            {
                if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 44 / 800);
                else
                {
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 44 / 480);
                }
                    
            }
            case ID_CONTAINER_EDIT_FAVORITE_WIDTH:
            {
                return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth() * 900 / SCALE);
            }
            case ID_LABEL_EDIT_FAVORITE_SUBTITLE_WIDTH:
            {
                if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth() * 790 / SCALE);
                else
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth() * 825 / SCALE);
            }
            case ID_CONTAINER_EDIT_FAVORITE_HEIGHT:
            {
                if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                {
                    int height = AppConfigHelper.getDisplayHeight() - TITLE_CONTAINER_HEIGHT.getInt() - BOTTOMCONTAINER_EDIT_FAVORITE_HEIGHT.getInt() - EDIT_FAVORITE_TOP_CONTAINER_HEIGHT.getInt() - EDIT_FAVORITE_SUBTITLE_HEIGHT.getInt();
                    return PrimitiveTypeCache.valueOf(height);
                }
                else
                {
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 360 / SCALE);
                }
            }
            case ID_CONTENTCONTAINER_EDIT_FAVORITE_HEIGHT:
            {
                if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 750 / SCALE);
                else
                {
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 670 / SCALE);
                }
            }
            case ID_BOTTOMCONTAINER_EDIT_FAVORITE_HEIGHT:
            {
                if (AppConfigHelper.isTabletSize())
                {
                    if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                    {
                        return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 100 / SCALE);
                    }
                    else
                    {
                        return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 150 / SCALE);
                    }
                }
                else
                {
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getMaxDisplaySize() / 10);
                }
            }
            case ID_BOTTOMNULLFIELD_EDIT_FAVORITE_WIDTH:
            {
                return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth() * 50 / SCALE);
            }
            case ID_BOTTOMBUTTON_EDIT_FAVORITE_WIDTH:
            {
                return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth() * 430 / SCALE);
            }
            case ID_CHECKBOX_EDIT_FAVORIVITE_HEIGHT:
            {
                int max = AppConfigHelper.getMaxDisplaySize();
                return PrimitiveTypeCache.valueOf(max * 105 / 1000);
            }
            case ID_MESSAGEBOX_TOP_HEIGHT:
            {
                if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 200 / SCALE);
                else
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 250 / SCALE);
            }
            case ID_MESSAGEBOX_BOTTOM_HEIGHT:
            {
                if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 200 / SCALE);
                else
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 250 / SCALE);
            }
            case ID_POPUP_BUTTON_WIDTH:
            {
                if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth() * 300 / SCALE);
                else
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth() * 210 / SCALE);
            }
            case ID_NULLFIELD_WIDTH:
            {
                return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth() * 30 / SCALE);
            }
            case ID_POPUP_WIDTH:
            {
                if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth() * 464 / 480);
                else
                {
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth() * 680 / 800);
                }
            }
            case ID_POPUP_TOP_CONTAINER_HEIGHT:
            {
                if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 200 / SCALE);
                else
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 250 / SCALE);
            }
            case ID_POPUP_BOTTOM_CONTAINER_HEIGHT:
            {
                if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 130 / SCALE);
                else
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 190 / SCALE);
            }
            case ID_POPUP_BUTTON_HEIGHT:
            {
                if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 60 / SCALE);
                else
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 100 / SCALE);
            }
            case ID_EDIT_FAV_BUTTON_HEIGHT:
            {
                if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 60 / SCALE);
                else
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 100 / SCALE);
            }
            case ID_ADDRESS_INFO_HEIGHT:
            {
                if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 200 / SCALE);
                else
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 280 / SCALE);
            }
            case ID_NULLFIELD_HEIGHT:
            {
                if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 20 / SCALE);
                else
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 30 / SCALE);
            }
            case ID_BUTON_HEIGHT:
            {
                if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 65 / SCALE);
                else
                {
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 105 / SCALE);
                }
            }
            case ID_BUTTON_CONTAINER_HEIGHT:
            {
                if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 130 / SCALE);
                else
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 190 / SCALE);
            }
            case ID_BUTTON_EDIT_CATEGORY_WIDTH:
            {
                if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth() * 320 / SCALE);
                else
                {
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth() * 320 / SCALE);
                }
            }
            case ID_NULLFIELD_EDIT_CATEGORY_WIDTH:
            {
                return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth() * 50 / SCALE);
            }
            case ID_POPUP_TITLE_HEIGHT:
            {
                if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 85 / 800);
                else
                {
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 75 / 480);
                }
            }
            case ID_EDIT_FAVORITE_IMAGE_CONTAINER_WIDTH:
            {
                if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth() * 250 / SCALE);
                else
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth() * 165 / SCALE);
            }
            case ID_EDIT_FAVORITE_SUBTITLE_HEIGHT:
            {
                if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 70 / SCALE);
                else
                {
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 105 / SCALE);
                }
            }
            case ID_EDIT_FAVORITE_TOP_CONTAINER_HEIGHT:
            {
                if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 170 / SCALE);
                else
                {
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 250 / SCALE);
                }
            }
            case ID_EDIT_FAVORITE_ICON_CONTAINER_HEIGHT:
            {
                return PrimitiveTypeCache.valueOf(EDIT_FAVORITE_TOP_CONTAINER_HEIGHT.getInt() * 13 / 16);
            }
            case ID_EDIT_FAVORITE_TOP_CONTAINER_WIDTH:
            {
                if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth() * 950 / SCALE);
                else
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth() * 960 / SCALE);
            }
            case ID_EDIT_FAVORITE_ICON_NULL_FIELD_WIDTH:
            {
                return PrimitiveTypeCache.valueOf(AppConfigHelper.getMinDisplaySize() * 32 / SCALE);
            }
            case ID_MULTILINE_WIDTH:
            {
                if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth() * 650 / SCALE);
                else
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth() * 700/ SCALE);
            }
            case ID_MULTILINE_HEIGHT:
            {
                if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 100 / SCALE);
                else
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 130 / SCALE);
            }
            case ID_PURE_WHITE_BG_HEIGHT:
            {
                if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 130 / SCALE);
                else
                {
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 180 / SCALE);
                }
            }
            case ID_POPUP_POSITION_Y:
            {
            	if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
            		return PrimitiveTypeCache.valueOf(AppConfigHelper.getStatusBarHeight() + AppConfigHelper.getDisplayHeight() * 15 / 800);
                else
                	return PrimitiveTypeCache.valueOf(AppConfigHelper.getStatusBarHeight() + AppConfigHelper.getDisplayHeight() * 38 / 480);           	
            } 
            case ID_INFO_LABEL_HEIGHT:
            {
                if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 40 / SCALE);
                else
                {
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 60 / SCALE);
                }
            }
            case ID_TOP_CONTAINER_NULLFIEDL_WIDTH:
            {
                if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth() * 650 / SCALE);
                else
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth() * 700/ SCALE);
            }
            case ID_BUTTON_PADDING:
            {
                if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 16 / SCALE);
                else
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 25/ SCALE);
            }
            case ID_PANEL_CONTAINER_HEIGHT:
            {
                int max = AppConfigHelper.getMaxDisplaySize();
                return POPUP_TITLE_HEIGHT.getInt() + TEXTFIELD_EDIT_FAVORITE_HEIGHT.getInt() + NULLFIELD_HEIGHT.getInt() + PURE_WHITE_BG_HEIGHT.getInt() + BUTTON_CONTAINER_HEIGHT.getInt() + 2 * UiFactory.getInstance().getCharWidth()/2 + 15 * max / 480 + 10 * max / 480 + 2 * BUTTON_PADDING.getInt() + 10 ;
            }
            
        }
        return null;
    }
}
