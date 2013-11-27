/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * EntryUiDecorator.java
 *
 */
package com.telenav.module.entry;

import com.telenav.module.AppConfigHelper;
import com.telenav.mvc.AbstractCommonUiDecorator;
import com.telenav.tnui.core.AbstractTnUiHelper;
import com.telenav.tnui.core.TnUiArgs.TnUiArgAdapter;
import com.telenav.util.PrimitiveTypeCache;

/**
 *@author fqming (fqming@telenav.cn)
 *@date Aug 16, 2010
 */
class EntryUiDecorator extends AbstractCommonUiDecorator
{
    private final static int ID_BUTTON_WIDTH = 1;
    private final static int ID_LOGIN_POPUP_WIDTH = 2;
    private final static int ID_LOGIN_POPUP_HEIGH = 3;
    private final static int ID_TITLE_CONTAINER_HEIGHT = 4;
    private final static int ID_LABEL_TITLE_WIDTH = 5;
    private final static int ID_LABEL_TITLE_HEIGHT = 6;
    private final static int ID_UPGRADE_BOTTOM_VGAP = 7;
    private final static int ID_UPGRADE_BOTTOM_HGAP = 8;
    private final static int ID_UPGRADE_BOTTOM_BUTTON_WIDTH = 9;
    private final static int ID_UPGRADE_SECONDLINE_BUTTON_WIDTH = 10;
    
    private final static int ID_NOTFORCE_UPGRADE_BUTTON_WIDTH = 11;
    private final static int ID_NOTFORCE_REMIND_BUTTON_WIDTH = 12;
    private final static int ID_NOTFORCE_NOASK_BUTTON_WIDTH = 13;
    private final static int ID_SECONDLINE_CONTAINER_WIDTH = 14;
    

    public TnUiArgAdapter TITLE_LABEL_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_LABEL_TITLE_WIDTH), this);
    public TnUiArgAdapter LABEL_TITLE_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_LABEL_TITLE_HEIGHT), this);
    public TnUiArgAdapter TITLE_CONTAINER_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_TITLE_CONTAINER_HEIGHT), this);
    public TnUiArgAdapter BUTTON_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_BUTTON_WIDTH), this);
    public TnUiArgAdapter LOGIN_POPUP_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_LOGIN_POPUP_WIDTH), this);
    public TnUiArgAdapter LOGIN_POPUP_HEIGH= new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_LOGIN_POPUP_HEIGH), this);
    public TnUiArgAdapter UPGRADE_BOTTOM_VGAP= new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_UPGRADE_BOTTOM_VGAP), this);
    public TnUiArgAdapter UPGRADE_BOTTOM_HGAP= new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_UPGRADE_BOTTOM_HGAP), this);
    public TnUiArgAdapter UPGRADE_BOTTOM_BUTTON_WIDTH= new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_UPGRADE_BOTTOM_BUTTON_WIDTH), this);
    public TnUiArgAdapter UPGRADE_SECONDLINE_BUTTON_WIDTH= new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_UPGRADE_SECONDLINE_BUTTON_WIDTH), this);
    
    public TnUiArgAdapter NOTFORCE_UPGRADE_BUTTON_WIDTH= new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_NOTFORCE_UPGRADE_BUTTON_WIDTH), this);
    public TnUiArgAdapter NOTFORCE_REMIND_BUTTON_WIDTH= new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_NOTFORCE_REMIND_BUTTON_WIDTH), this);
    public TnUiArgAdapter NOTFORCE_NOASK_BUTTON_WIDTH= new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_NOTFORCE_NOASK_BUTTON_WIDTH), this);
    public TnUiArgAdapter SECONDLINE_CONTAINER_WIDTH= new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_SECONDLINE_CONTAINER_WIDTH), this);
    
    protected Object decorateDelegate(TnUiArgAdapter args)
    {
        int key = ((Integer)args.getKey()).intValue();
        int orientation = ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation();
        int screenWidth = AppConfigHelper.getDisplayWidth();
        
        switch(key)
        {
            case ID_BUTTON_WIDTH:
            {
                return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth() / 2);
            }
            case ID_LOGIN_POPUP_WIDTH:
            {
                return PrimitiveTypeCache.valueOf(AppConfigHelper.getMinDisplaySize() / 2);
            }
            case ID_LOGIN_POPUP_HEIGH:
            {
                return PrimitiveTypeCache.valueOf(AppConfigHelper.getMinDisplaySize() * 618 / 2000);
            }
            case ID_TITLE_CONTAINER_HEIGHT:
            {
                if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 100 / 1000);
                else
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 130 / 1000);
            }
            case ID_LABEL_TITLE_WIDTH:
            {
                return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth());
            }
            case ID_LABEL_TITLE_HEIGHT:
            {
                return PrimitiveTypeCache.valueOf(AppConfigHelper.getMaxDisplaySize() * 100 / 1000);
            }
            case ID_UPGRADE_BOTTOM_VGAP:
            {
                return PrimitiveTypeCache.valueOf(AppConfigHelper.getMaxDisplaySize() * 20 / 1000);
            }
            case ID_UPGRADE_BOTTOM_HGAP:
            {
                return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth() * 20 / 1000);
            }
            case ID_UPGRADE_BOTTOM_BUTTON_WIDTH:
            {
                return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth() * 70 / 100);
            }
            case ID_UPGRADE_SECONDLINE_BUTTON_WIDTH:
            {
                int secondLineButtonWidth = ( UPGRADE_BOTTOM_BUTTON_WIDTH.getInt() - UPGRADE_BOTTOM_HGAP.getInt() ) / 2;
                return PrimitiveTypeCache.valueOf(secondLineButtonWidth);
            }
            case ID_NOTFORCE_UPGRADE_BUTTON_WIDTH:
            {
            	if(orientation == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
            	{
            		return PrimitiveTypeCache.valueOf(screenWidth * 47 / 100);
            	}
            	else
            	{
            		return PrimitiveTypeCache.valueOf(screenWidth * 475 / 1000);
            	}
            }
            case ID_NOTFORCE_REMIND_BUTTON_WIDTH:
            {
            	if(orientation == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
            	{
            		return PrimitiveTypeCache.valueOf(screenWidth * 47 / 100);
            	}
            	else
            	{
            		return PrimitiveTypeCache.valueOf(screenWidth * 475 / 1000);
            	}
            }
//            case ID_NOTFORCE_NOASK_BUTTON_WIDTH:
//            {
//            	if(orientation == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
//            	{
//            		return PrimitiveTypeCache.valueOf(screenWidth * 47 / 100);
//            	}
//            	else
//            	{
//            		return PrimitiveTypeCache.valueOf(screenWidth * 31 / 100);
//            	}
//            }
            case ID_SECONDLINE_CONTAINER_WIDTH:
            {
            	return PrimitiveTypeCache.valueOf(screenWidth * 96 / 100);
            }
        }
        return null;
    }

}
