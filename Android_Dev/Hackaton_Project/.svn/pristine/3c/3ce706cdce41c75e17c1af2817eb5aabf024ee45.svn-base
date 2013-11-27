/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * ShareUiDecorator.java
 *
 */
package com.telenav.module.feedback;

import com.telenav.module.AppConfigHelper;
import com.telenav.mvc.AbstractCommonUiDecorator;
import com.telenav.tnui.core.AbstractTnUiHelper;
import com.telenav.tnui.core.TnUiArgs.TnUiArgAdapter;
import com.telenav.util.PrimitiveTypeCache;

/**
 * @author wzhu (wzhu@telenav.cn)
 * @date 2010-10-20
 */
class FeedbackUiDecorator extends AbstractCommonUiDecorator
{

    private final static int ID_TITLE_HEIGHT = 1;

    private final static int ID_TEXTAREA_HEIGHT = 2;
    
    private final static int ID_BOTTOM_CONTAINER_HEIGHT = 3;
    
    private final static int ID_NULLFIELD_HEIGHT = 4;
    
    private final static int ID_SUBMIT_BUTTON_WIDTH = 5;
    
    private final static int ID_TITLE_NULLFIELD_HEIGHT = 6;
    
    private final static int ID_TEXTAREA_WIDTH = 7;
    
    public TnUiArgAdapter TEXTAREA_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_TEXTAREA_HEIGHT), this);
    
    public TnUiArgAdapter TITLE_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_TITLE_HEIGHT), this);
    
    public TnUiArgAdapter BOTTOM_CONTAINER_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_BOTTOM_CONTAINER_HEIGHT), this);
    
    public TnUiArgAdapter NULLFIELD_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_NULLFIELD_HEIGHT), this);
    
    public TnUiArgAdapter SUBMIT_BUTTON_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_SUBMIT_BUTTON_WIDTH), this);
    
    public TnUiArgAdapter TITLE_NULLFIELD_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_TITLE_NULLFIELD_HEIGHT), this);
    
    public TnUiArgAdapter TEXTAREA_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_TEXTAREA_WIDTH), this);
    
    public static final int SCALE = 100;

    public FeedbackUiDecorator()
    {
    }

    protected Object decorateDelegate(TnUiArgAdapter args)
    {
        int key = ((Integer) args.getKey()).intValue();
        switch (key)
        {
            case ID_TITLE_HEIGHT:
            {
                if(((AbstractTnUiHelper)AbstractTnUiHelper.getInstance()).getOrientation()==AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 10 / SCALE);
                else 
                {
                    if (AppConfigHelper.isTabletSize())
                    {
                        return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 938 / 10000);
                    }
                    else
                    {
                        return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 12 / SCALE);
                    }
                }
            }
            case ID_TEXTAREA_HEIGHT:
            {
                if(((AbstractTnUiHelper)AbstractTnUiHelper.getInstance()).getOrientation()==AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 20 / SCALE);
                else 
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 30/ SCALE);
            }
            case ID_NULLFIELD_HEIGHT:
            {
                if(((AbstractTnUiHelper)AbstractTnUiHelper.getInstance()).getOrientation()==AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 4 / SCALE);
                else 
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 5/ SCALE);
            }
            case ID_BOTTOM_CONTAINER_HEIGHT:
            {
                if(((AbstractTnUiHelper)AbstractTnUiHelper.getInstance()).getOrientation()==AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 70 / SCALE);
                else 
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 58/ SCALE);
            }
            case ID_SUBMIT_BUTTON_WIDTH:
            {
                if(((AbstractTnUiHelper)AbstractTnUiHelper.getInstance()).getOrientation()==AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth() * 60 / SCALE);
                else
                {
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth() * 55 / SCALE);
                } 
            }
            case ID_TITLE_NULLFIELD_HEIGHT:
            {
                if(((AbstractTnUiHelper)AbstractTnUiHelper.getInstance()).getOrientation()==AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 1 / SCALE);
                else 
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 2/ SCALE);
            }
            case ID_TEXTAREA_WIDTH:
            {
                if(((AbstractTnUiHelper)AbstractTnUiHelper.getInstance()).getOrientation()==AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth() * 92 / SCALE);
                else 
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth() * 94/ SCALE);
            }
        }
        return null;
    }
}
