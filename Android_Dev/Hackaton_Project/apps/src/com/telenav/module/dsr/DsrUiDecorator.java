/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * DsrUiDecorator.java
 *
 */
package com.telenav.module.dsr;

import com.telenav.module.AppConfigHelper;
import com.telenav.mvc.AbstractCommonUiDecorator;
import com.telenav.res.IStringDsr;
import com.telenav.res.ResourceManager;
import com.telenav.tnui.core.AbstractTnUiHelper;
import com.telenav.tnui.core.TnUiArgs.TnUiArgAdapter;
import com.telenav.tnui.graphics.AbstractTnFont;
import com.telenav.ui.ImageDecorator;
import com.telenav.ui.UiStyleManager;
import com.telenav.util.PrimitiveTypeCache;

/**
 *@author bduan
 *@date Aug 23, 2010
 */
class DsrUiDecorator extends AbstractCommonUiDecorator
{
    private final static int ID_LABEL_TITLE_WIDTH = 1;

    private final static int ID_LABEL_TITLE_HEIGHT = 2;

    private final static int ID_GAP_LINE_WIDTH = 3;
    
    private final static int ID_GAP_LINE_HEIGHT = 4;
    
    private final static int ID_THINKING_GAP_LINE_HEIGHT = 5;

    private final static int ID_DES_LABEL_HEIGHT = 6;
    
    private final static int ID_MIC_HEIGHT = 7;
    
    private final static int ID_TOP_CONTAINER_HEIGHT = 8;
    
    private final static int ID_BOTTOM_CONTAINER_HEIGHT = 9;
    
    public TnUiArgAdapter TITLE_LABEL_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_LABEL_TITLE_WIDTH), this);

    public TnUiArgAdapter TITLE_LABEL_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_LABEL_TITLE_HEIGHT), this);

    public TnUiArgAdapter GAP_LINE_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_GAP_LINE_WIDTH), this);
    
    public TnUiArgAdapter GAP_LINE_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_GAP_LINE_HEIGHT), this);
    
    public TnUiArgAdapter THINKING_GAP_LINE_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_THINKING_GAP_LINE_HEIGHT), this);

    public TnUiArgAdapter DES_LABEL_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_DES_LABEL_HEIGHT), this);
    
    public TnUiArgAdapter MIC_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_MIC_HEIGHT), this);
    
    public TnUiArgAdapter TOP_CONTAINER_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_TOP_CONTAINER_HEIGHT), this);
    
    public TnUiArgAdapter BOTTOM_CONTAINER_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_BOTTOM_CONTAINER_HEIGHT), this);
    
    protected Object decorateDelegate(TnUiArgAdapter args)
    {
        int key = ((Integer) args.getKey()).intValue();
        switch (key)
        {
            case ID_GAP_LINE_WIDTH:
            {
                String sayCmdStr = ResourceManager.getInstance().getCurrentBundle()
                        .getString(IStringDsr.RES_LABEL_SAY_COMMAND, IStringDsr.FAMILY_DSR);
                AbstractTnFont font = UiStyleManager.getInstance().getFont(UiStyleManager.FONT_LABEL);
                int padding = font.getMaxWidth() / 2;
                int strLength = font.stringWidth(sayCmdStr);
                return PrimitiveTypeCache.valueOf(padding * 2 + strLength);
            }
            case ID_LABEL_TITLE_WIDTH:
            {
                return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth() * 4 / 5);
            }
            case ID_GAP_LINE_HEIGHT:
            {
                boolean isPortrait = ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT;
                if(isPortrait)
                {
                    return PrimitiveTypeCache.valueOf(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_LABEL).getHeight() * 2 / 3);
                }
                else
                {
                    return PrimitiveTypeCache.valueOf(0);
                }
            }
            case ID_THINKING_GAP_LINE_HEIGHT:
            {
                boolean isPortrait = ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT;
                if(isPortrait)
                {
                    return PrimitiveTypeCache.valueOf(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_LABEL).getHeight() * 2 / 3);
                }
                else
                {
                    return PrimitiveTypeCache.valueOf(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_LABEL).getHeight() * 3 / 8);
                }
            }
            case ID_LABEL_TITLE_HEIGHT:
            {
                return PrimitiveTypeCache.valueOf(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_LABEL).getHeight());
            }
            case ID_DES_LABEL_HEIGHT:
            {
                //For we use the ascent part to anchor center, so we should add extra descent to calculate the height.
                AbstractTnFont font = UiStyleManager.getInstance().getFont(UiStyleManager.FONT_DSR_EXAMPLE);
                return PrimitiveTypeCache.valueOf(font.getHeight() + font.getDescent());
            }
            case ID_MIC_HEIGHT:
            {
                int orientation = ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation();
                boolean isVertical = orientation == AbstractTnUiHelper.ORIENTATION_PORTRAIT;
                
                int mic_height = ImageDecorator.IMG_MIC_ICON_UNFOCUS.getImage().getHeight();
                if(!isVertical)
                {
                    mic_height = ImageDecorator.IMG_MIC_ICON_LANDSCAPE_UNFOCUS.getImage().getHeight();
                }
                
                return PrimitiveTypeCache.valueOf(mic_height);
            }
            case ID_TOP_CONTAINER_HEIGHT:
            {
                int orientation = ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation();
                boolean isVertical = orientation == AbstractTnUiHelper.ORIENTATION_PORTRAIT;
                
                if(isVertical)
                {
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 2 / 3);
                }
                else
                {
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 68 / 100);
                }
            }
            case ID_BOTTOM_CONTAINER_HEIGHT:
            {
                return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() / 8);
            }
        }
        return null;
    }

}
