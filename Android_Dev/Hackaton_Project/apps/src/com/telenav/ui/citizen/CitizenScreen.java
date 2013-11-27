/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * CitizenScreen.java
 *
 */
package com.telenav.ui.citizen;

import com.telenav.module.AppConfigHelper;
import com.telenav.mvc.AbstractCommonUiDecorator;
import com.telenav.tnui.core.AbstractTnContainer;
import com.telenav.tnui.core.TnUiArgs;
import com.telenav.tnui.core.TnUiArgs.TnUiArgAdapter;
import com.telenav.tnui.graphics.AbstractTnGraphics;
import com.telenav.tnui.widget.TnLinearContainer;
import com.telenav.ui.frogui.FrogScreen;
import com.telenav.util.PrimitiveTypeCache;

/**
 *@author fqming (fqming@telenav.cn)
 *@date Aug 10, 2010
 */
public class CitizenScreen extends FrogScreen
{
    protected TnLinearContainer titleContainer;
    protected TnLinearContainer contentContainer;
    protected TnLinearContainer contextContainer;
    
    protected ContainerDecoration containerDecoration;
    protected TnUiArgAdapter TITLE_HEIGHT; 
    protected TnUiArgAdapter CONTEXT_HEIGHT;
    protected TnUiArgAdapter CONTENT_HEIGHT;
    
    private final static int ID_TITLE_HEIGHT = -2001;
    private final static int ID_CONTENT_HEIGHT = -2002;
    private final static int ID_CONTEXT_HEIGHT = -2003;
    
    public CitizenScreen(int screenId)
    {
        super(screenId);
        init();
    }
    
    public int getStatusBarHeight()
    {
        return AppConfigHelper.getStatusBarHeight();
    }
    
    protected void init()
    {
        titleContainer = new TnLinearContainer(-2, false, AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);
        contentContainer = new TnLinearContainer(-3, true);
        contextContainer = new TnLinearContainer(-4, false, AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);

        rootContainer.add(titleContainer);
        rootContainer.add(contentContainer);
        rootContainer.add(contextContainer);

        // need add to right place.
        addScreenContainerDecorator();
    }
    
    /**
     * Retrieve the title section's container.
     * 
     * @return the title section's container.
     */
    public AbstractTnContainer getTitleContainer()
    {
        return titleContainer;
    }
    
    /**
     * Retrieve the content section's container.
     * 
     * @return the content section's container.
     */
    public AbstractTnContainer getContentContainer()
    {
        return contentContainer;
    }
    
    /**
     * Retrieve the context section's container.
     * 
     * @return the context section's container.
     */
    public AbstractTnContainer getContextContainer()
    {
        return contextContainer;
    }
    
    protected void addScreenContainerDecorator()
    {
        if(containerDecoration == null)
            containerDecoration = new ContainerDecoration();
        
        this.TITLE_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_TITLE_HEIGHT), containerDecoration);
        this.CONTEXT_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_CONTEXT_HEIGHT), containerDecoration);
        this.CONTENT_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_CONTENT_HEIGHT), containerDecoration);
        
        TnUiArgs titleContainerUiArgs = this.titleContainer.getTnUiArgs();
        titleContainerUiArgs.put(TnUiArgs.KEY_PREFER_HEIGHT, TITLE_HEIGHT);

        TnUiArgs contextUiArgs = this.contextContainer.getTnUiArgs();
        contextUiArgs.put(TnUiArgs.KEY_PREFER_HEIGHT, CONTEXT_HEIGHT);

        TnUiArgs contentUiArgs = this.contentContainer.getTnUiArgs();
        contentUiArgs.put(TnUiArgs.KEY_PREFER_HEIGHT, CONTENT_HEIGHT);
    }
    
    /*
     * ContainerDecoration is for height of the containers of CitizenScreen.
     */
    class ContainerDecoration extends AbstractCommonUiDecorator
    {
        protected Object decorateDelegate(TnUiArgAdapter args)
        {
            int key = ((Integer)args.getKey()).intValue();
            int contentHeight = AppConfigHelper.getDisplayHeight() - getStatusBarHeight();
            int visibleScreenHeight = contentHeight;
            
            switch(key)
            {
                case ID_CONTENT_HEIGHT:
                {
                    if (titleContainer.getChildrenSize() > 0)
                    {
                        contentHeight -= visibleScreenHeight * 60 / 1000;
                    }
                    if (contextContainer.getChildrenSize() > 0)
                    {
                        contentHeight -= visibleScreenHeight * 60 / 1000;
                    }
                    return PrimitiveTypeCache.valueOf(contentHeight);
                }
                case ID_CONTEXT_HEIGHT:
                {
                    if (contextContainer.getChildrenSize() > 0)
                    {
                        return PrimitiveTypeCache.valueOf(visibleScreenHeight * 60 / 1000);
                    }
                    else
                    {
                        return PrimitiveTypeCache.valueOf(0);
                    }
                }
                case ID_TITLE_HEIGHT:
                {
                    if (titleContainer.getChildrenSize() > 0)
                    {
                        return PrimitiveTypeCache.valueOf(visibleScreenHeight * 60 / 1000);
                    }
                    else
                    {
                        return PrimitiveTypeCache.valueOf(0);
                    }
                }
            }

            return null;
        }
    }
}
