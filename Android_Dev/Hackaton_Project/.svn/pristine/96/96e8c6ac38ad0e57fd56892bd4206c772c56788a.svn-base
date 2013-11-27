/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * NavTrafficButton.java
 *
 */
package com.telenav.module.nav;

import com.telenav.module.AppConfigHelper;
import com.telenav.mvc.AbstractCommonUiDecorator;
import com.telenav.res.IStringNav;
import com.telenav.res.ResourceManager;
import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.core.TnUiArgs.TnUiArgAdapter;
import com.telenav.tnui.graphics.AbstractTnFont;
import com.telenav.tnui.graphics.AbstractTnGraphics;
import com.telenav.tnui.graphics.AbstractTnImage;
import com.telenav.ui.ImageDecorator;
import com.telenav.ui.UiStyleManager;
import com.telenav.util.PrimitiveTypeCache;

/**
 *@author zhdong@telenav.cn
 *@date 2011-3-4
 */
public class NavTrafficButton extends AbstractTnComponent
{
    NavTrafficButtonUiDecorator uiDecorator = new NavTrafficButtonUiDecorator();

    public NavTrafficButton(int id)
    {
        super(id);
    }

    protected void paint(AbstractTnGraphics graphics)
    {
        int oldColor = graphics.getColor();
        AbstractTnFont oldFont = graphics.getFont();
        int width = this.getWidth();
        int height = this.getHeight();

        int topPadding = uiDecorator.TOP_PADDING.getInt();
//        int bottomPadding = uiDecorator.BOTTOM_PADDING.getInt();
        
        //FIXME: the unfocused image is incorrect.
        AbstractTnImage image = this.isFocused ? ImageDecorator.IMG_NAV_TRAFFIC_ICON_FOCUSED.getImage() : ImageDecorator.IMG_NAV_TRAFFIC_ICON_UNFOCUSED.getImage();
        int x = width / 2;
        int y = topPadding;
        graphics.drawImage(image, x, y, AbstractTnGraphics.HCENTER | AbstractTnGraphics.TOP);
        
        int color = UiStyleManager.getInstance().getColor(UiStyleManager.NAV_TRAFFIC_BUTTON_COLOR);
        AbstractTnFont trafficFont = UiStyleManager.getInstance().getFont(UiStyleManager.FONT_NAV_SCREEN_TRAFFIC_BUTTON);
        String traffic = ResourceManager.getInstance().getCurrentBundle().getString(IStringNav.RES_TAB_TRAFFIC, IStringNav.FAMILY_NAV);
        x = width / 2;
        y = image.getHeight() + topPadding + (height - image.getHeight() - topPadding >> 1);
        graphics.setColor(color);
        graphics.setFont(trafficFont);
        graphics.drawString(traffic, x, y, AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);
        
        graphics.setColor(oldColor);
        graphics.setFont(oldFont);
    }

    private static class NavTrafficButtonUiDecorator extends AbstractCommonUiDecorator
    {
        private final static int ID_TOP_PADDING = 1;
        private final static int ID_BOTTOM_PADDING = 3;
        
        public TnUiArgAdapter TOP_PADDING = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_TOP_PADDING), this);
//        public TnUiArgAdapter BOTTOM_PADDING = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_BOTTOM_PADDING), this);
        
        protected Object decorateDelegate(TnUiArgAdapter args)
        {
            int key = ((Integer)args.getKey()).intValue();
            switch(key)
            {
                case ID_TOP_PADDING:
                {
                    int padding;
                    if (AppConfigHelper.isTabletSize())
                    {
                       padding = AppConfigHelper.getMaxDisplaySize() * 110 / 10000;
                    }
                    else
                    {
                        padding = AppConfigHelper.getMaxDisplaySize() * 175 / 10000;
                    }
                    return PrimitiveTypeCache.valueOf(padding);
                }
                case ID_BOTTOM_PADDING:
                {
                    int padding = AppConfigHelper.getMaxDisplaySize() * 1125 / 100000;
                    return PrimitiveTypeCache.valueOf(padding);
                }
            }
            return null;
        }
    }
}
