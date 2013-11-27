/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * CitizenRouteChoiceItem.java
 *
 */
package com.telenav.ui.citizen;

import java.util.Hashtable;

import com.telenav.module.AppConfigHelper;
import com.telenav.tnui.core.TnUiArgs;
import com.telenav.tnui.core.TnUiArgs.ITnUiArgsDecorator;
import com.telenav.tnui.core.TnUiArgs.TnUiArgAdapter;
import com.telenav.tnui.graphics.AbstractTnFont;
import com.telenav.tnui.graphics.AbstractTnGraphics;
import com.telenav.tnui.widget.TnLinearContainer;
import com.telenav.ui.UiFactory;
import com.telenav.ui.frogui.widget.FrogLabel;
import com.telenav.util.PrimitiveTypeCache;

/**
 *@author pwang
 *@date 2012-2-17
 */
public class CitizenRouteChoiceItem extends TnLinearContainer
{
    public static final int ID_ROUTE_CHOICE_ITEM_TITLE = 11011;
    public static final int ID_ROUTE_CHOICE_ITEM_BODY = 11012;
    
    public static final int KEY_ROUTE_CHOICE_TITLE_HEIGHT = 11013;
    public static final int KEY_ROUTE_CHOICE_BODY_HEIGHT = 11014;
    
    public final int portraitIndex = 1; 
    public final int portraitSelectedIndex = 2; 
    public final int landscapeIndex = 3; 
    public final int landscapeSelectedIndex = 4; 
    
    private boolean isPortrait = true;
    private boolean isSelected = false;
    
    private String routeLength;
    private String costTime;
    private Hashtable choiceItemStyles;
    
    
    public CitizenRouteChoiceItem(int id, String routeLength, String costTime)
    {
        super(id, true, AbstractTnGraphics.HCENTER | AbstractTnGraphics.TOP);
        this.routeLength = routeLength;
        this.costTime = costTime;
        this.choiceItemStyles = new Hashtable();
        initChoiceItem();
    }
    
    public boolean isPortrait()
    {
        return this.isPortrait;
    }
    
    public boolean isSelected()
    {
        return this.isSelected;
    }

    public void update(boolean isPortrait, boolean isSelected)
    {
        this.isPortrait = isPortrait;
        this.isSelected = isSelected;
        int index = isPortrait ? portraitIndex : landscapeIndex;
        index = isSelected ? index + 1 : index;
        
        ChoiceItemStyle currentStyle = (ChoiceItemStyle) choiceItemStyles.get(index);

        FrogLabel itemTitle = (FrogLabel) this.getComponentById(ID_ROUTE_CHOICE_ITEM_TITLE);
        itemTitle.setFont(currentStyle.titleFont);
        itemTitle.setForegroundColor(currentStyle.titleFgColorFocus, currentStyle.titleFgColorUnfocus);
        
        FrogLabel itemBody = (FrogLabel) this.getComponentById(ID_ROUTE_CHOICE_ITEM_BODY);
        itemBody.setFont(currentStyle.bodyFont);
        itemBody.setForegroundColor(currentStyle.bodyFgColorFocus, currentStyle.bodyFgColorUnfocus);
        
        if(currentStyle.titleBgUnfocus == null && currentStyle.titleBgFocused == null)
        {
            this.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, currentStyle.bodyBgUnfocus);
            this.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, currentStyle.bodyBgFocused);
        }
        else
        {
            itemTitle.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, currentStyle.titleBgUnfocus);
            itemTitle.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, currentStyle.titleBgFocused);
            itemBody.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, currentStyle.bodyBgUnfocus);
            itemBody.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, currentStyle.bodyBgFocused);
        }
    }
    
    private void initChoiceItem()
    {
        TnUiArgAdapter title_vertical_padding = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
        {
            public Object decorate(TnUiArgAdapter args)
            {
                int shadowHeight = 6;
                return PrimitiveTypeCache.valueOf(shadowHeight);
            }
        });
        
        TnUiArgAdapter bottomPadding = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
        {
            public Object decorate(TnUiArgAdapter args)
            {
                return PrimitiveTypeCache.valueOf(AppConfigHelper.getMaxDisplaySize() * 3 / 480);
            }
        });
        
        TnUiArgAdapter widthAdapter = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
        {
            public Object decorate(TnUiArgAdapter args)
            {
                return PrimitiveTypeCache.valueOf(CitizenRouteChoiceItem.this.getTnUiArgs().get(TnUiArgs.KEY_PREFER_WIDTH).getInt());
            }
        });
        TnUiArgAdapter titleAdapter = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
        {
            public Object decorate(TnUiArgAdapter args)
            {
                return PrimitiveTypeCache.valueOf(CitizenRouteChoiceItem.this.getTnUiArgs().get(KEY_ROUTE_CHOICE_TITLE_HEIGHT).getInt());
            }
        });
        TnUiArgAdapter bodyAdapter = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
        {
            public Object decorate(TnUiArgAdapter args)
            {
                return PrimitiveTypeCache.valueOf(CitizenRouteChoiceItem.this.getTnUiArgs().get(KEY_ROUTE_CHOICE_BODY_HEIGHT).getInt());
            }
        });
        
        FrogLabel itemTitle = UiFactory.getInstance().createLabel(ID_ROUTE_CHOICE_ITEM_TITLE, routeLength);
        itemTitle.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, widthAdapter);
        itemTitle.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, titleAdapter);
        itemTitle.setStyle(AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);
        itemTitle.setPadding(0, title_vertical_padding.getInt(), 0, 0);
        this.add(itemTitle);

        FrogLabel itemBody = UiFactory.getInstance().createLabel(ID_ROUTE_CHOICE_ITEM_BODY, costTime);
        itemBody.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, widthAdapter);
        itemBody.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, bodyAdapter);
        itemBody.setStyle(AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);
        itemBody.setPadding(0, 0, 0, bottomPadding.getInt());
        itemBody.setFocusable(true);
        this.add(itemBody);        
    }

    public void setChoiceItemPortraitStyle(TnUiArgAdapter titleBgUnfocus,
            TnUiArgAdapter titleBgFocused, TnUiArgAdapter bodyBgUnfocus,
            TnUiArgAdapter bodyBgFocused, int titleFgColorUnfocus, int titleFgColorFocus,
            int bodyFgColorUnfocus, int bodyFgColorFocus, AbstractTnFont titleFont,
            AbstractTnFont bodyFont)
    {
        addChoiceItemStyle(portraitIndex, titleBgUnfocus, titleBgFocused, bodyBgUnfocus,
            bodyBgFocused, titleFgColorUnfocus, titleFgColorFocus, bodyFgColorUnfocus,
            bodyFgColorFocus, titleFont, bodyFont);
    }
    
    public void setChoiceItemPortraitSelectedStyle(TnUiArgAdapter titleBgUnfocus,
            TnUiArgAdapter titleBgFocused, TnUiArgAdapter bodyBgUnfocus,
            TnUiArgAdapter bodyBgFocused, int titleFgColorUnfocus, int titleFgColorFocus,
            int bodyFgColorUnfocus, int bodyFgColorFocus, AbstractTnFont titleFont,
            AbstractTnFont bodyFont)
    {
        addChoiceItemStyle(portraitSelectedIndex, titleBgUnfocus, titleBgFocused, bodyBgUnfocus,
            bodyBgFocused, titleFgColorUnfocus, titleFgColorFocus, bodyFgColorUnfocus,
            bodyFgColorFocus, titleFont, bodyFont);
    }
    
    public void setChoiceItemLandscapeStyle(TnUiArgAdapter titleBgUnfocus,
            TnUiArgAdapter titleBgFocused, TnUiArgAdapter bodyBgUnfocus,
            TnUiArgAdapter bodyBgFocused, int titleFgColorUnfocus, int titleFgColorFocus,
            int bodyFgColorUnfocus, int bodyFgColorFocus, AbstractTnFont titleFont,
            AbstractTnFont bodyFont)
    {
        addChoiceItemStyle(landscapeIndex, titleBgUnfocus, titleBgFocused, bodyBgUnfocus,
            bodyBgFocused, titleFgColorUnfocus, titleFgColorFocus, bodyFgColorUnfocus,
            bodyFgColorFocus, titleFont, bodyFont);
    }
    
    public void setChoiceItemLandscapeSelectedStyle(TnUiArgAdapter titleBgUnfocus,
            TnUiArgAdapter titleBgFocused, TnUiArgAdapter bodyBgUnfocus,
            TnUiArgAdapter bodyBgFocused, int titleFgColorUnfocus, int titleFgColorFocus,
            int bodyFgColorUnfocus, int bodyFgColorFocus, AbstractTnFont titleFont,
            AbstractTnFont bodyFont)
    {
        addChoiceItemStyle(landscapeSelectedIndex, titleBgUnfocus, titleBgFocused, bodyBgUnfocus,
            bodyBgFocused, titleFgColorUnfocus, titleFgColorFocus, bodyFgColorUnfocus,
            bodyFgColorFocus, titleFont, bodyFont);
    }
    
    private void addChoiceItemStyle(int index, TnUiArgAdapter titleBgUnfocus,
            TnUiArgAdapter titleBgFocused, TnUiArgAdapter bodyBgUnfocus,
            TnUiArgAdapter bodyBgFocused, int titleFgColorUnfocus, int titleFgColorFocus,
            int bodyFgColorUnfocus, int bodyFgColorFocus, AbstractTnFont titleFont,
            AbstractTnFont bodyFont)
    {
        ChoiceItemStyle style = new ChoiceItemStyle();
        
        style.titleBgUnfocus = titleBgUnfocus;
        style.titleBgFocused = titleBgFocused;
        style.bodyBgUnfocus = bodyBgUnfocus;
        style.bodyBgFocused = bodyBgFocused;
        style.titleFgColorUnfocus = titleFgColorUnfocus;
        style.titleFgColorFocus = titleFgColorFocus;
        style.bodyFgColorUnfocus = bodyFgColorUnfocus;
        style.bodyFgColorFocus = bodyFgColorFocus;
        style.titleFont = titleFont;
        style.bodyFont = bodyFont;
        
        choiceItemStyles.put(index, style);
    }
}

class ChoiceItemStyle
{
    public TnUiArgAdapter titleBgUnfocus;
    public TnUiArgAdapter titleBgFocused;
    public TnUiArgAdapter bodyBgUnfocus;
    public TnUiArgAdapter bodyBgFocused;
    
    public int titleFgColorUnfocus;
    public int titleFgColorFocus;
    public int bodyFgColorUnfocus;
    public int bodyFgColorFocus;
    
    public AbstractTnFont titleFont;
    public AbstractTnFont bodyFont;
}