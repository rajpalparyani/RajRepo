/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * RouteSettingUiDecorator.java
 *
 */
package com.telenav.module.preference.routesetting;

import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.datatypes.preference.Preference;
import com.telenav.module.AppConfigHelper;
import com.telenav.mvc.AbstractCommonUiDecorator;
import com.telenav.tnui.core.AbstractTnUiHelper;
import com.telenav.tnui.core.TnUiArgs.TnUiArgAdapter;
import com.telenav.util.PrimitiveTypeCache;

/**
 *@author yning
 *@date 2011-3-3
 */
public class RouteSettingUiDecorator extends AbstractCommonUiDecorator
{
    private final static int ID_ROUTE_SETTING_POPUP_WIDTH = 1;
    private final static int ID_ROUTE_SETTING_SELECTION_CONTAINER_WIDTH = 2;
    private final static int ID_ROUTE_SETTING_SELECTION_BUTTON_WIDTH = 3;
    private final static int ID_ROUTE_SETTING_SELECTION_BUTTON_HEIGHT = 4;
    private final static int ID_ROUTE_SETTING_CHECKBOX_ITEM_WIDTH = 5;
    private final static int ID_ROUTE_SETTING_TOP_COMPONENT_HEIGHT = 7;
    private final static int ID_ROUTE_SETTING_TOP_AREA_HEIGHT = 8;
    
    public TnUiArgAdapter ROUTE_SETTING_POPUP_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_ROUTE_SETTING_POPUP_WIDTH), this);
    public TnUiArgAdapter ROUTE_SETTING_SELECTION_CONTAINER_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_ROUTE_SETTING_SELECTION_CONTAINER_WIDTH), this);
    public TnUiArgAdapter ROUTE_SETTING_SELECTION_BUTTON_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_ROUTE_SETTING_SELECTION_BUTTON_WIDTH), this);
    public TnUiArgAdapter ROUTE_SETTING_SELECTION_BUTTON_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_ROUTE_SETTING_SELECTION_BUTTON_HEIGHT), this);
    public TnUiArgAdapter ROUTE_SETTING_CHECKBOX_ITEM_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_ROUTE_SETTING_CHECKBOX_ITEM_WIDTH), this);
    public TnUiArgAdapter ROUTE_SETTING_TOP_COMPONENT_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_ROUTE_SETTING_TOP_COMPONENT_HEIGHT), this);
    public TnUiArgAdapter ROUTE_SETTING_TOP_AREA_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_ROUTE_SETTING_TOP_AREA_HEIGHT), this);
    
    boolean isRouteSession;
    RouteSettingUiDecorator(boolean isRouteSession)
    {
        super();
        this.isRouteSession = isRouteSession;
    }
    
    protected Object decorateDelegate(TnUiArgAdapter args)
    {
        int key = ((Integer)args.getKey()).intValue();
        switch(key)
        {
            case ID_ROUTE_SETTING_POPUP_WIDTH:
            {
                int orientation = ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation();
                int screen_width = AppConfigHelper.getDisplayWidth();
                if (orientation == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                {
                    return PrimitiveTypeCache.valueOf(screen_width * 968 / 1000);
                }
                else
                {
                    return PrimitiveTypeCache.valueOf(screen_width * 867 / 1000);
                }
            }
            case ID_ROUTE_SETTING_SELECTION_CONTAINER_WIDTH:
            {
                int screen_height = AppConfigHelper.getDisplayWidth();
                if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                {
                    return PrimitiveTypeCache.valueOf(screen_height * 768 / 1000);
                }
                else
                {
                    return PrimitiveTypeCache.valueOf(screen_height * 729 / 1000);
                }
            }
            case ID_ROUTE_SETTING_SELECTION_BUTTON_WIDTH:
            {
                int orientation = ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation();
                int containerWidth = ROUTE_SETTING_SELECTION_CONTAINER_WIDTH.getInt();
                if (orientation == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                {
                    int buttonWidth = containerWidth / 2;
                    return PrimitiveTypeCache.valueOf(buttonWidth);
                }
                else
                {
                    int buttonWidth = containerWidth / 4;
                    return PrimitiveTypeCache.valueOf(buttonWidth);
                }
            }
            case ID_ROUTE_SETTING_SELECTION_BUTTON_HEIGHT:
            {
                int screen_height = AppConfigHelper.getDisplayHeight();
                if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                {
                    return PrimitiveTypeCache.valueOf(screen_height * 96 / 1000);
                }
                else
                {
                    return PrimitiveTypeCache.valueOf(screen_height * 162 / 1000);
                }
            }
            case ID_ROUTE_SETTING_CHECKBOX_ITEM_WIDTH:
            {
                int containerWidth = ROUTE_SETTING_SELECTION_CONTAINER_WIDTH.getInt();
                if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                {
                    return PrimitiveTypeCache.valueOf(containerWidth);
                }
                else
                {
                    return PrimitiveTypeCache.valueOf(containerWidth / 2);
                }
            }
            
            case ID_ROUTE_SETTING_TOP_COMPONENT_HEIGHT: //including selection container
            {
                int height = 0;
                int maxOneRowNumber = ROUTE_SETTING_SELECTION_CONTAINER_WIDTH.getInt() / ROUTE_SETTING_SELECTION_BUTTON_WIDTH.getInt();
                if(maxOneRowNumber < 1)
                {
                    maxOneRowNumber = 1;
                }
                
                if(maxOneRowNumber > 0)
                {
                    Preference routeTypePref;
                    
                    if(isRouteSession)
                    {
                        routeTypePref = DaoManager.getInstance().getTripsDao().getPreference(Preference.ID_PREFERENCE_ROUTETYPE);
                    }
                    else
                    {
                        routeTypePref = DaoManager.getInstance().getPreferenceDao().getPreference(Preference.ID_PREFERENCE_ROUTETYPE);
                    }
                    
                    if(routeTypePref != null)
                    {
                        String[] routeTypeOptions = routeTypePref.getOptionNames();
                        if(routeTypeOptions != null)
                        {
                            int lineCount = routeTypeOptions.length / maxOneRowNumber;
                            height = ROUTE_SETTING_SELECTION_BUTTON_HEIGHT.getInt() * lineCount;
                        }
                    }
                }
                height += (2 * ROUTE_SETTING_TITLE_STYLE_GAP.getInt());
                height += ROUTE_SETTING_TITLE_CONTAINER_HEIGHT.getInt();
                return PrimitiveTypeCache.valueOf(height);
            }
            case ID_ROUTE_SETTING_TOP_AREA_HEIGHT: //including top container top/bottom padding
            {
                return PrimitiveTypeCache.valueOf(ROUTE_SETTING_TOP_COMPONENT_HEIGHT.getInt() + ROUTE_SETTING_TOP_AREA_TOP_PADDING.getInt() + ROUTE_SETTING_TOP_AREA_BOTTOM_PADDING.getInt());
            }
         
        }
        return null;
    }

}
