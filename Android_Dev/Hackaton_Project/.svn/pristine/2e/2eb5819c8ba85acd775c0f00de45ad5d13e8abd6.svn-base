/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * AcUiDecorator.java
 *
 */
package com.telenav.module.ac;

import com.telenav.module.AppConfigHelper;
import com.telenav.mvc.AbstractCommonUiDecorator;
import com.telenav.tnui.core.AbstractTnUiHelper;
import com.telenav.tnui.core.TnUiArgs.TnUiArgAdapter;
import com.telenav.ui.UiStyleManager;
import com.telenav.ui.citizen.android.AndroidCitizenUiHelper;
import com.telenav.util.PrimitiveTypeCache;

/**
 * @author wzhu (wzhu@telenav.cn)
 * @date 2010-8-17
 */
class AcUiDecorator extends AbstractCommonUiDecorator
{
    private final static int SCALE = 10000;
    
    private static final int ID_LABEL_TITLE_HEIGHT = 1;  
    private final static int ID_ONE_BOX_EDIT_TEXT_WIDTH = 2;
    private final static int ID_CHOOSELOCATION_SPLIT_LINE = 3;
    private final static int ID_REGION_POPUP_TITLE_CONTAINER_HEIGHT = 4;
    
    private final static int ID_AC_BUTTON_WIDTH = 5;
    private final static int ID_AC_BUTTON_HEIGHT = 6;
    private final static int ID_AC_BUTTON_HORIZONTAL_GAP = 7;
    private final static int ID_BUTTON_CONTAINER_HEIGHT = 8;
    private final static int ID_CHOOSELOCATION_CONTAINER_UPDATES_HEIGHT = 10;
    private final static int ID_BUTTON_RIGHT_PADDING = 11;  
    
    private final static int ID_REGION_CONTAINER_WIDTH = 12;
    private final static int ID_REGION_CONTAINER_HEIGHT = 13;
    private final static int ID_AC_REGION_ICON_WIDTH = 14;
    private final static int ID_AC_REGION_COMBOBOX_WIDTH = 15;
    private final static int ID_AC_REGION_TITLE_WIDTH = 16;
    private final static int ID_CHANGE_REGION_DOWN_ARROW_WIDTH = 17;
    private final static int ID_AC_REGION_SEPARATOR_WIDTH = 18;
    private final static int ID_AC_REGION_COMBOBOX_HEIGHT = 19;
    
    public TnUiArgAdapter LABEL_TITLE_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_LABEL_TITLE_HEIGHT), this);
    public TnUiArgAdapter ONE_BOX_EDIT_TEXT_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_ONE_BOX_EDIT_TEXT_WIDTH), this);
    public TnUiArgAdapter CHOOSELOCATION_SPLIT_LINE = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_CHOOSELOCATION_SPLIT_LINE), this);
    public TnUiArgAdapter REGION_POPUP_TITLE_CONTAINER_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_REGION_POPUP_TITLE_CONTAINER_HEIGHT), this);

    public TnUiArgAdapter AC_BUTTON_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_AC_BUTTON_WIDTH), this);
    public TnUiArgAdapter AC_BUTTON_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_AC_BUTTON_HEIGHT), this);
    public TnUiArgAdapter AC_BUTTON_HORIZONTAL_GAP = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_AC_BUTTON_HORIZONTAL_GAP), this);
    public TnUiArgAdapter BUTTON_CONTAINER_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_BUTTON_CONTAINER_HEIGHT), this);
    public TnUiArgAdapter CHOOSELOCATION_CONTAINER_UPDATES_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_CHOOSELOCATION_CONTAINER_UPDATES_HEIGHT), this);    
    public TnUiArgAdapter BUTTON_RIGHT_PADDING = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_BUTTON_RIGHT_PADDING), this);
    
    public TnUiArgAdapter REGION_CONTAINER_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_REGION_CONTAINER_WIDTH), this);
    public TnUiArgAdapter REGION_CONTAINER_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_REGION_CONTAINER_HEIGHT), this);
    public TnUiArgAdapter AC_REGION_ICON_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_AC_REGION_ICON_WIDTH), this);
    public TnUiArgAdapter AC_REGION_COMBOBOX_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_AC_REGION_COMBOBOX_WIDTH), this);
    public TnUiArgAdapter AC_REGION_TITLE_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_AC_REGION_TITLE_WIDTH), this);
    public TnUiArgAdapter CHANGE_REGION_DOWN_ARROW_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_CHANGE_REGION_DOWN_ARROW_WIDTH), this);
    public TnUiArgAdapter AC_REGION_SEPARATOR_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_AC_REGION_SEPARATOR_WIDTH), this);
    public TnUiArgAdapter AC_REGION_COMBOBOX_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_AC_REGION_COMBOBOX_HEIGHT), this);

    
    protected Object decorateDelegate(TnUiArgAdapter args)
    {
        int key = ((Integer) args.getKey()).intValue();
        switch (key)
        {
            case ID_LABEL_TITLE_HEIGHT:
            {
                if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                {
                    //unify the title height with native xml screen title height
                    return PrimitiveTypeCache.valueOf(AndroidCitizenUiHelper.getPixelsByDip(53)); 
                }
                else
                {
                    if (AppConfigHelper.isTabletSize())
                    {
                        return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 938 / SCALE);
                    }
                    else
                    {
                        return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 1270 / SCALE);
                    }
                }
            }
            case ID_ONE_BOX_EDIT_TEXT_WIDTH:
            {
                return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth() - AC_BUTTON_HORIZONTAL_GAP.getInt() * 2);
            }
            case ID_CHOOSELOCATION_SPLIT_LINE:
            {
                return PrimitiveTypeCache.valueOf(1);
            }
            case ID_REGION_POPUP_TITLE_CONTAINER_HEIGHT:
            {
                int gapHeight = AppConfigHelper.getMaxDisplaySize() * 1 / 100;
                int fontHeight = UiStyleManager.getInstance().getFont(UiStyleManager.FONT_POPUP_TITLE).getHeight();
                return PrimitiveTypeCache.valueOf(gapHeight*2 + fontHeight);
            }
            
            
            case ID_AC_BUTTON_WIDTH:
            {
                if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                {
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth() * 4530 / SCALE);
                }
                else
                {
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth() * 2797 / SCALE);
                }
            }
            case ID_AC_BUTTON_HEIGHT:
            {
                return PrimitiveTypeCache.valueOf(AppConfigHelper.getMaxDisplaySize() * 875 / SCALE);
            }
            case ID_AC_BUTTON_HORIZONTAL_GAP:
            {
                if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                {
                    return PrimitiveTypeCache.valueOf((AppConfigHelper.getDisplayWidth() - AC_BUTTON_WIDTH.getInt() * 2) / 3);
                }
                else
                {
                    return PrimitiveTypeCache.valueOf((AppConfigHelper.getDisplayWidth() - AC_BUTTON_WIDTH.getInt() * 3) / 4);
                }
            }
            case ID_BUTTON_CONTAINER_HEIGHT:
            {
                return PrimitiveTypeCache.valueOf(AppConfigHelper.getMaxDisplaySize() * 1125 / SCALE);
            }
            case ID_CHOOSELOCATION_CONTAINER_UPDATES_HEIGHT:
            {
                int height = AppConfigHelper.getDisplayHeight() - AppConfigHelper.getStatusBarHeight() - LABEL_TITLE_HEIGHT.getInt()
                        - REGION_CONTAINER_HEIGHT.getInt();
                return PrimitiveTypeCache.valueOf(height);
            }
            case ID_BUTTON_RIGHT_PADDING:
            {
                if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                {
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 208 / SCALE);
                }
                else
                {
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 282 / SCALE);
                }
            }
            
            case ID_REGION_CONTAINER_WIDTH:
            {
                return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth());
            }
            case ID_REGION_CONTAINER_HEIGHT:
            {
                return PrimitiveTypeCache.valueOf(AppConfigHelper.getMaxDisplaySize() * 1042 / SCALE);
            }
            case ID_AC_REGION_ICON_WIDTH:
            {
                return PrimitiveTypeCache.valueOf(AC_REGION_COMBOBOX_HEIGHT.getInt());
            }
            case ID_AC_REGION_COMBOBOX_WIDTH:
            {
                int width = SCREEN_WIDTH.getInt() - AC_BUTTON_HORIZONTAL_GAP.getInt() * 2 - AC_REGION_ICON_WIDTH.getInt();
                return PrimitiveTypeCache.valueOf(width);
            }
            case ID_AC_REGION_TITLE_WIDTH:
            {
                int totalWidth = AC_REGION_COMBOBOX_WIDTH.getInt() - AC_REGION_SEPARATOR_WIDTH.getInt();
                if(((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                {
                    return PrimitiveTypeCache.valueOf(totalWidth * 870/ 1000);
                }
                else
                {
                    return PrimitiveTypeCache.valueOf(totalWidth * 925/ 1000);
                }            
            }
            case ID_CHANGE_REGION_DOWN_ARROW_WIDTH://The down arrow should keep align as those button icons.
            {
                int totalWidth = AC_REGION_COMBOBOX_WIDTH.getInt() - AC_REGION_SEPARATOR_WIDTH.getInt();
                if(((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                {
                    return PrimitiveTypeCache.valueOf(totalWidth * 130/ 1000);
                }
                else
                {
                    return PrimitiveTypeCache.valueOf(totalWidth * 75/ 1000);
                }
            }
            case ID_AC_REGION_SEPARATOR_WIDTH:
            {
                return PrimitiveTypeCache.valueOf(0);
            }
            case ID_AC_REGION_COMBOBOX_HEIGHT:
            {
                return PrimitiveTypeCache.valueOf(AppConfigHelper.getMaxDisplaySize() * 646 / SCALE);
            }

        }
        return null;
    }

}
