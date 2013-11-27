/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * EntryUiDecorator.java
 *
 */
package com.telenav.module.map;

import com.telenav.app.android.scout_us.R;
import com.telenav.module.AppConfigHelper;
import com.telenav.module.nav.NavRunningStatusProvider;
import com.telenav.mvc.AbstractCommonUiDecorator;
import com.telenav.tnui.core.AbstractTnUiHelper;
import com.telenav.tnui.core.TnUiArgs.TnUiArgAdapter;
import com.telenav.ui.ImageDecorator;
import com.telenav.ui.citizen.android.AndroidCitizenUiHelper;
import com.telenav.util.PrimitiveTypeCache;

/**
 *@author JY Xu
 *@date Aug 21, 2010
 */
class MapUiDecorator extends AbstractCommonUiDecorator
{
    private final static int ID_CURRENT_LOCATION_X = 0;

    private final static int ID_CURRENT_LOCATION_Y = 1;

    private final static int ID_MAP_VIEW_LIST_X = 2;

    private final static int ID_MAP_VIEW_LIST_Y = 3;

    private final static int ID_MAP_ICON_WIDTH = 4;

    private final static int ID_MAP_ICON_HEIGHT = 5;

    private final static int ID_SEARCH_INPUT_FIELD_GAP_WIDTH = 7;

    private final static int ID_SEARCH_INPUT_FIELD_WIDTH = 8;

    private final static int ID_SEARCH_INPUT_FIELD_HEIGHT = 9;

    private final static int ID_SEARCH_TEXTFIELD_HEIGHT = 10;

    private final static int ID_SEARCH_TEXTFIELD_WIDTH = 11;

    private final static int ID_SEARCH_BAR_X = 12;

    private final static int ID_SEARCH_BAR_Y = 13;

    private final static int ID_ZOOM_IN_X = 14;

    private final static int ID_ZOOM_IN_Y = 15;

    private final static int ID_ZOOM_OUT_X = 16;

    private final static int ID_ZOOM_OUT_Y = 17;

    private final static int ID_CONTROLLER_ICON_MARGIN = 19;

    /**
     * TODO: remove it later
     * @deprecated
     */
    private final static int ID_MAP_CONTAINER_HEIGHT = 23;

    private static final int ID_POI_LIST_BUTTON_WIDTH = 24;

    private static final int ID_POI_LIST_BUTTON_HEIGHT = 25;

    private static final int ID_POI_LIST_GAP = 26;

    private static final int ID_INPUT_FIELD_WITH_POI_LIST_BUTTON_WIDTH = 27;
    
    private static final int ID_STATUS_X = 28;
    
    private static final int ID_STATUS_Y = 29;
    
    private static final int ID_STATUS_WIDTH = 30;
    
    private static final int ID_STATUS_HEIGHT = 31;
    
    private static final int ID_MAP_SUMMARY_STATUS_Y = 32;
    
    private static final int ID_STATUS_LABEL_H_PADDING = 33;
    
    private static final int ID_MAP_POI_INDICATOR_WIDTH = 35;
    
    private static final int ID_MAP_POI_INDICATOR_HEIGHT = 36;
    
    private static final int ID_MAP_POI_INDICATOR_X = 37;
    
    private static final int ID_MAP_POI_INDICATOR_Y = 38;
    
    private final static int ID_TAB_BUTTON_WIDTH = 39;
    
    private final static int ID_TAB_BUTTON_HEIGHT = 40;
    
    private final static int ID_TAB_OUTER_CONTAINER_HEIGHT = 41;
    
    private final static int ID_TAB_OUTER_CONTAINER_X = 42;
    
    private final static int ID_TAB_OUTER_CONTAINER_Y = 43;
    
    private final static int ID_NOTIFY_BOX_WIDTH = 46;
    
    private final static int ID_NOTIFY_BOX_HEIGHT = 47;
    
    
	private final static int ID_MAP_X = 53;
    private final static int ID_MAP_Y = 54;
    private final static int ID_MAP_WIDTH = 55;
    private final static int ID_MAP_HEIGHT = 56;
    
    //traffic alert detail
    private final static int ID_TRAFFIC_ALERT_DETAIL_WIDTH = 57;
    private final static int ID_TRAFFIC_ALERT_DETAIL_TOP_HEIGHT = 58;
    private final static int ID_POPUP_TOP_PADDING = 59;
    private final static int ID_TRAFFIC_ALERT_DETAIL_CONTENT_WIDTH = 60;
    private final static int ID_TRAFFIC_ALERT_DETAIL_CONTENT_HEIGHT = 61;
    private final static int ID_POPUP_BOTTOM_PART_HEIGHT = 62;
    private final static int ID_POPUP_BOTTOM_BUTTON_WIDTH = 63;
    private final static int ID_POPUP_BOTTOM_BUTTON_HEIGHT = 64;
    private final static int ID_POPUP_TITLE_PADDING = 65;
    private final static int ID_POPUP_BUTTON_PADDING = 66;
    
    private final static int ID_ZOOM_ICON_WIDTH = 67;
    private final static int ID_ZOOM_ICON_HEIGHT = 68;
       
    private final static int ID_MAP_LOGO_Y_WITH_POI_PAGINATION = 69;
    private final static int ID_MAP_VIEW_LIST_Y_WITH_POI_PAGINATION = 70;
    private final static int ID_ZOOM_IN_Y_WITH_POI_PAGINATION = 71;
    private final static int ID_MAP_VIEW_LIST_Y_WITH_BOTTOM_BAR = 72;
    private final static int ID_ZOOM_IN_Y_WITH_WITH_BOTTOM_BAR = 73;
    
    private final static int ID_MAP_Y_WITHOUT_SEARCHBAR = 74;
    private final static int ID_MAP_HEIGHT_WITHOUT_SEARCHBAR = 75;
    
	
    public TnUiArgAdapter CURRENT_LOCATION_X = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_CURRENT_LOCATION_X), this);

    public TnUiArgAdapter CURRENT_LOCATION_Y = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_CURRENT_LOCATION_Y), this);

    public TnUiArgAdapter MAP_VIEW_LIST_X = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_MAP_VIEW_LIST_X), this);

    public TnUiArgAdapter MAP_VIEW_LIST_Y = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_MAP_VIEW_LIST_Y), this);

    public TnUiArgAdapter MAP_ICON_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_MAP_ICON_WIDTH), this);

    public TnUiArgAdapter MAP_ICON_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_MAP_ICON_HEIGHT), this);

    public TnUiArgAdapter ZOOM_ICON_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_ZOOM_ICON_WIDTH), this);
    public TnUiArgAdapter ZOOM_ICON_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_ZOOM_ICON_HEIGHT), this);

    public TnUiArgAdapter SEARCH_INPUT_FIELD_GAP_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_SEARCH_INPUT_FIELD_GAP_WIDTH),
            this);

    public TnUiArgAdapter SEARCH_BAR_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_SEARCH_INPUT_FIELD_WIDTH), this);

    public TnUiArgAdapter SEARCH_BAR_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_SEARCH_INPUT_FIELD_HEIGHT), this);

    public TnUiArgAdapter SEARCH_TEXTFIELD_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_SEARCH_TEXTFIELD_HEIGHT), this);

    public TnUiArgAdapter SEARCH_TEXTFIELD_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_SEARCH_TEXTFIELD_WIDTH), this);

    public TnUiArgAdapter SEARCH_BAR_X = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_SEARCH_BAR_X), this);

    public TnUiArgAdapter SEARCH_BAR_Y = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_SEARCH_BAR_Y), this);

    public TnUiArgAdapter ZOOM_IN_X = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_ZOOM_IN_X), this);

    public TnUiArgAdapter ZOOM_IN_Y = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_ZOOM_IN_Y), this);

    public TnUiArgAdapter ZOOM_OUT_X = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_ZOOM_OUT_X), this);

    public TnUiArgAdapter ZOOM_OUT_Y = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_ZOOM_OUT_Y), this);

    public TnUiArgAdapter CONTROLLER_ICON_MARGIN = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_CONTROLLER_ICON_MARGIN), this);

    public TnUiArgAdapter NOTIFY_BOX_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_NOTIFY_BOX_WIDTH), this);

    public TnUiArgAdapter NOTIFY_BOX_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_NOTIFY_BOX_HEIGHT), this);
    /**
     * TODO: remove it later
     * @deprecated
     */
    public TnUiArgAdapter MAP_CONTAINER_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_MAP_CONTAINER_HEIGHT), this);

    public TnUiArgAdapter POI_LIST_BUTTON_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_POI_LIST_BUTTON_WIDTH), this);

    public TnUiArgAdapter POI_LIST_BUTTON_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_POI_LIST_BUTTON_HEIGHT), this);

    public TnUiArgAdapter POI_LIST_GAP = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_POI_LIST_GAP), this);

    public TnUiArgAdapter INPUT_FIELD_WITH_POI_LIST_BUTTON_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache
            .valueOf(ID_INPUT_FIELD_WITH_POI_LIST_BUTTON_WIDTH), this);
    
    public TnUiArgAdapter STATUS_X = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_STATUS_X), this);
    
    public TnUiArgAdapter STATUS_Y = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_STATUS_Y), this);
    
    public TnUiArgAdapter STATUS_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_STATUS_WIDTH), this);
    
    public TnUiArgAdapter STATUS_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_STATUS_HEIGHT), this);
    
    public TnUiArgAdapter STATUS_LABEL_H_PADDING = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_STATUS_LABEL_H_PADDING), this);
    
    public TnUiArgAdapter MAP_POI_INDICATOR_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_MAP_POI_INDICATOR_WIDTH), this);
    
    public TnUiArgAdapter MAP_POI_INDICATOR_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_MAP_POI_INDICATOR_HEIGHT), this);
    
    public TnUiArgAdapter MAP_POI_INDICATOR_X = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_MAP_POI_INDICATOR_X), this);
    
    public TnUiArgAdapter MAP_POI_INDICATOR_Y = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_MAP_POI_INDICATOR_Y), this);

    
    public TnUiArgAdapter TAB_BUTTON_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_TAB_BUTTON_WIDTH), this);
    
    public TnUiArgAdapter TAB_BUTTON_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_TAB_BUTTON_HEIGHT), this);
    
    public TnUiArgAdapter TAB_OUTER_CONTAINER_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_TAB_OUTER_CONTAINER_HEIGHT), this);
    
    public TnUiArgAdapter TAB_OUTER_CONTAINER_X = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_TAB_OUTER_CONTAINER_X), this);
    
    public TnUiArgAdapter TAB_OUTER_CONTAINER_Y = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_TAB_OUTER_CONTAINER_Y), this);


    
    //the background map's x,y,width,height. set the openGL.
 	public TnUiArgAdapter MAP_X = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_MAP_X), this);
    public TnUiArgAdapter MAP_Y = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_MAP_Y), this);
    public TnUiArgAdapter MAP_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_MAP_WIDTH), this);
    public TnUiArgAdapter MAP_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_MAP_HEIGHT), this);

    
    public TnUiArgAdapter TRAFFIC_ALERT_DETAIL_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_TRAFFIC_ALERT_DETAIL_WIDTH), this);
    public TnUiArgAdapter TRAFFIC_ALERT_DETAIL_TOP_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache
            .valueOf(ID_TRAFFIC_ALERT_DETAIL_TOP_HEIGHT), this);
    public TnUiArgAdapter POPUP_TOP_PADDING = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_POPUP_TOP_PADDING), this);
    public TnUiArgAdapter POPUP_BUTTON_PADDING = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_POPUP_BUTTON_PADDING), this);
    public TnUiArgAdapter POPUP_TITLE_PADDING = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_POPUP_TITLE_PADDING), this);
    public TnUiArgAdapter TRAFFIC_ALERT_DETAIL_CONTENT_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache
        .valueOf(ID_TRAFFIC_ALERT_DETAIL_CONTENT_WIDTH), this);
    public TnUiArgAdapter TRAFFIC_ALERT_DETAIL_CONTENT_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache
        .valueOf(ID_TRAFFIC_ALERT_DETAIL_CONTENT_HEIGHT), this);
    public TnUiArgAdapter POPUP_BOTTOM_PART_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_POPUP_BOTTOM_PART_HEIGHT), this);
    public TnUiArgAdapter POPUP_BOTTOM_BUTTON_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_POPUP_BOTTOM_BUTTON_WIDTH), this);
    public TnUiArgAdapter POPUP_BOTTOM_BUTTON_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_POPUP_BOTTOM_BUTTON_HEIGHT), this);

    public TnUiArgAdapter MAP_LOGO_Y_WITH_POI_PAGINATION = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_MAP_LOGO_Y_WITH_POI_PAGINATION), this);
    public TnUiArgAdapter MAP_VIEW_LIST_Y_WITH_POI_PAGINATION = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_MAP_VIEW_LIST_Y_WITH_POI_PAGINATION), this);
    public TnUiArgAdapter ZOOM_IN_Y_WITH_POI_PAGINATION = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_ZOOM_IN_Y_WITH_POI_PAGINATION), this);
    public TnUiArgAdapter MAP_VIEW_LIST_Y_WITH_BOTTOM_BAR = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_MAP_VIEW_LIST_Y_WITH_BOTTOM_BAR), this);
    public TnUiArgAdapter ZOOM_IN_Y_WITH_WITH_BOTTOM_BAR = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_ZOOM_IN_Y_WITH_WITH_BOTTOM_BAR), this);
    
    public TnUiArgAdapter MAP_Y_WITHOUT_SEARCHBAR = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_MAP_Y_WITHOUT_SEARCHBAR), this);
    public TnUiArgAdapter MAP_HEIGHT_WITHOUT_SEARCHBAR = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_MAP_HEIGHT_WITHOUT_SEARCHBAR), this);

    
    
    final public int TRAFFIC_ALERT_GAP = AppConfigHelper.getMaxDisplaySize() * 100 / 1000;   
    final public int TRAFFIC_ALERT_MULTILINE_GAP = AppConfigHelper.getMaxDisplaySize() * 40 / 1000;
    
    
    protected Object decorateDelegate(TnUiArgAdapter args)
    {
        int key = ((Integer) args.getKey()).intValue();
        boolean isNavRunning = NavRunningStatusProvider.getInstance().isNavRunning();

        switch (key)
        {
            case ID_MAP_CONTAINER_HEIGHT:
            {
                if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() - BOTTOM_BAR_REAL_HEIGHT.getInt());
                else
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() - BOTTOM_BAR_REAL_HEIGHT.getInt());
            }
            case ID_CURRENT_LOCATION_X:
            {
                return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth() - AppConfigHelper.getMinDisplaySize()* 16/320 - MAP_ICON_WIDTH.getInt());
             }
            case ID_CURRENT_LOCATION_Y:
            {
//                if (isNavRunning)
//                {
//                    return PrimitiveTypeCache.valueOf(MAP_LOGO_Y_WITH_BOTTOM_BAR.getInt() - MAP_ICON_HEIGHT.getInt() - MAP_BUTTON_BOTTOM_GAP.getInt());
//                }
//                else
                {
                    return PrimitiveTypeCache.valueOf(MAP_LOGO_Y.getInt() - MAP_ICON_HEIGHT.getInt() - MAP_BUTTON_BOTTOM_GAP.getInt());
                }
            }
            case ID_MAP_VIEW_LIST_X:
            {
                return PrimitiveTypeCache.valueOf(AppConfigHelper.getMinDisplaySize() * 16/320);
            }
            case ID_MAP_VIEW_LIST_Y:
            {
                return CURRENT_LOCATION_Y.getInt();
            }
            case ID_MAP_ICON_WIDTH:
            {
                return ZOOM_ICON_WIDTH.getInt();
            }
            case ID_MAP_ICON_HEIGHT:
            {
                return MAP_ICON_WIDTH.getInt();
            }
            case ID_ZOOM_ICON_WIDTH:
            {
                return PrimitiveTypeCache.valueOf(AppConfigHelper.getMinDisplaySize() * 46 / 320);
            }
            case ID_ZOOM_ICON_HEIGHT:
            {
                int shadow = 6;
                return PrimitiveTypeCache.valueOf(AppConfigHelper.getMinDisplaySize() * 46 / 320 - shadow);
            }
            case ID_SEARCH_INPUT_FIELD_GAP_WIDTH:
            {
                return PrimitiveTypeCache.valueOf(5);
            }
            case ID_SEARCH_INPUT_FIELD_WIDTH:
            {
                if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth() * 9600 / 10000);
                else
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth() * 9800 / 10000);
            }
            case ID_SEARCH_INPUT_FIELD_HEIGHT:
            {
                if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 1200 / 10000);
                else
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 1800 / 10000);
            }
            case ID_SEARCH_TEXTFIELD_WIDTH:
            {
                if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth() * 8750 / 10000);
                else
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth() * 9200 / 10000);
            }
            case ID_SEARCH_TEXTFIELD_HEIGHT:
            {
                if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 520 / 10000);
                else
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 1050 / 10000);
            }
            case ID_SEARCH_BAR_X:
            {
                return PrimitiveTypeCache.valueOf(0);
            }
            case ID_SEARCH_BAR_Y:
            {
                return PrimitiveTypeCache.valueOf(COMMON_TITLE_BUTTON_HEIGHT.getInt());
            }
            case ID_ZOOM_IN_X:
            {
                return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth() - 16* AppConfigHelper.getMinDisplaySize()/320 - ZOOM_ICON_WIDTH.getInt());
            }
            case ID_ZOOM_IN_Y:
            {
                if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                    return PrimitiveTypeCache.valueOf((AppConfigHelper.getDisplayHeight()  - ZOOM_ICON_HEIGHT.getInt()) / 2);
                else
                    return PrimitiveTypeCache.valueOf(ZOOM_OUT_Y.getInt() - ZOOM_ICON_HEIGHT.getInt());
            }
            case ID_ZOOM_OUT_X:
            {
                return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth() - 16*AppConfigHelper.getMinDisplaySize()/320 - ImageDecorator.IMG_MAP_ZOOM_OUT_ICON_FOCUSED.getImage().getWidth());
            }
            case ID_ZOOM_OUT_Y:
            {
                if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                    return PrimitiveTypeCache.valueOf(CURRENT_LOCATION_Y.getInt() - AppConfigHelper.getDisplayHeight() * 1875/10000);
                else
                    return PrimitiveTypeCache.valueOf(CURRENT_LOCATION_Y.getInt() - AppConfigHelper.getDisplayHeight() *700/10000 - ImageDecorator.IMG_MAP_ZOOM_OUT_ICON_FOCUSED.getImage().getHeight() );
            }
            case ID_CONTROLLER_ICON_MARGIN:
            {
                if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth() * 5 / 100);
                else
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 720 / 10000);
            }
            case ID_POI_LIST_BUTTON_WIDTH:
            {
                if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                {
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 660 / 10000);
                }
                else
                {
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 800 / 10000);
                }
            }
            case ID_POI_LIST_BUTTON_HEIGHT:
            {
                if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 660 / 10000);
                else
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 800 / 10000);
            }
            case ID_POI_LIST_GAP:
            {
                if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                {
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth() * 15 / 1000);
                }
                else
                {
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth() * 13 / 1000);
                }
            }
            case ID_INPUT_FIELD_WITH_POI_LIST_BUTTON_WIDTH:
            {
                if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth() * 7880 / 10000);
                else
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth() * 8300 / 10000);
            }
            case ID_STATUS_WIDTH:
            {
                int maxHeight = AppConfigHelper.getMaxDisplaySize();

                return PrimitiveTypeCache.valueOf(maxHeight * 266 / 1000);
            }
            case ID_STATUS_HEIGHT:
            {
                int maxHeight = AppConfigHelper.getMaxDisplaySize();

                return PrimitiveTypeCache.valueOf(maxHeight * 62 / 1000);
            }
            case ID_STATUS_X:
            {
                int screenWidth = AppConfigHelper.getDisplayWidth();
                int x = (screenWidth - STATUS_WIDTH.getInt()) / 2;
                return PrimitiveTypeCache.valueOf(x);
            }
            case ID_STATUS_Y:
            {
                if (isNavRunning)
                {
                    return PrimitiveTypeCache.valueOf(MAP_LOGO_Y_WITH_BOTTOM_BAR.getInt() - STATUS_HEIGHT.getInt() - MAP_BUTTON_BOTTOM_GAP.getInt());
                }
                else
                {
                    return PrimitiveTypeCache.valueOf(MAP_LOGO_Y.getInt() - STATUS_HEIGHT.getInt() - MAP_BUTTON_BOTTOM_GAP.getInt());
                }
            }
            case ID_MAP_SUMMARY_STATUS_Y:
            {
                return PrimitiveTypeCache.valueOf(0);
            }
            case ID_STATUS_LABEL_H_PADDING:
            {
                int gap = AppConfigHelper.getMaxDisplaySize() * 1 / 100;
                return PrimitiveTypeCache.valueOf(gap);
            }
            
            case ID_MAP_POI_INDICATOR_WIDTH:
            {
                return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth());
            }
            case ID_MAP_POI_INDICATOR_HEIGHT:
            {
                if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                    return PrimitiveTypeCache.valueOf((int)AndroidCitizenUiHelper.getResources().getDimension(R.dimen.commonBottom0ContainerPortraitLayoutHeight));
                else
                {
                    return PrimitiveTypeCache.valueOf((int)AndroidCitizenUiHelper.getResources().getDimension(R.dimen.commonBottom0ContainerlandscapeLayoutHeight));
                }
            }
            case ID_MAP_POI_INDICATOR_X:
            {
                return 0;
            }
            case ID_MAP_POI_INDICATOR_Y:
            {
                return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() - MAP_POI_INDICATOR_HEIGHT.getInt());
            }
            case ID_TAB_BUTTON_WIDTH:
            {
                int width = SCREEN_WIDTH.getInt() * 2632 / 10000;
                return PrimitiveTypeCache.valueOf(width);
            }
            case ID_TAB_BUTTON_HEIGHT:
            {
                int height = AppConfigHelper.getMaxDisplaySize() * 60 / 1000;
                return PrimitiveTypeCache.valueOf(height);
            }
            case ID_TAB_OUTER_CONTAINER_HEIGHT:
            {
                int height = AppConfigHelper.getMaxDisplaySize() * 90 / 1000;
                return PrimitiveTypeCache.valueOf(height);
            }
            case ID_TAB_OUTER_CONTAINER_X:
            {
                return PrimitiveTypeCache.valueOf(0);
            }
            case ID_TAB_OUTER_CONTAINER_Y:
            {
                return PrimitiveTypeCache.valueOf(0);
            }
            case ID_NOTIFY_BOX_WIDTH:
            {
                int width = AppConfigHelper.getMaxDisplaySize() * 300 / 1000;
                return PrimitiveTypeCache.valueOf(width);
            }
            case ID_NOTIFY_BOX_HEIGHT:
            {
                int height = AppConfigHelper.getMaxDisplaySize() * 200 / 1000;
                return PrimitiveTypeCache.valueOf(height);
            }
			case ID_MAP_X:
            {
                return PrimitiveTypeCache.valueOf(0);
            }
            case ID_MAP_Y:
            {
                return PrimitiveTypeCache.valueOf(COMMON_TITLE_BUTTON_HEIGHT.getInt()+COMMON_ONEBOX_HEIGHT.getInt());
            }
            case ID_MAP_WIDTH:
            {
                return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth());
            }
            case ID_MAP_HEIGHT:
            {
                return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() - AppConfigHelper.getStatusBarHeight()-this.COMMON_TITLE_BUTTON_HEIGHT.getInt()-this.COMMON_ONEBOX_HEIGHT.getInt());
            }
            case ID_TRAFFIC_ALERT_DETAIL_WIDTH:
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
            case ID_TRAFFIC_ALERT_DETAIL_TOP_HEIGHT:
            {
                int shadowHeight = 12;
                int height = AppConfigHelper.getMaxDisplaySize() * 350 / 1000 + shadowHeight;
                return PrimitiveTypeCache.valueOf(height);
            }
            case ID_POPUP_TOP_PADDING:
            {
                int shadowHeight = 12;
                int height = AppConfigHelper.getMaxDisplaySize() * 31 / 1000 + shadowHeight;
                return PrimitiveTypeCache.valueOf(height);
            }
            case ID_POPUP_TITLE_PADDING:
            {
                int whiteBgTransparentWid = 12;
                return PrimitiveTypeCache.valueOf(POPUP_TOP_PADDING.getInt() - whiteBgTransparentWid);
            }
            case ID_TRAFFIC_ALERT_DETAIL_CONTENT_WIDTH:
            {
                int shadowHeight = 12;
                return PrimitiveTypeCache.valueOf( TRAFFIC_ALERT_DETAIL_WIDTH.getInt() - shadowHeight - TRAFFIC_ALERT_GAP );
            }
            case ID_TRAFFIC_ALERT_DETAIL_CONTENT_HEIGHT:
            {
                int maxDisplaySize = AppConfigHelper.getMaxDisplaySize();
                int height = maxDisplaySize * 200 / 1000;
                return PrimitiveTypeCache.valueOf(height);
            }
            case ID_POPUP_BOTTOM_PART_HEIGHT:
            {
                int shadowHeight = 12;
                int height = AppConfigHelper.getMaxDisplaySize() * 110 / 1000 + shadowHeight;
                return PrimitiveTypeCache.valueOf(height);
            }
            case ID_POPUP_BOTTOM_BUTTON_WIDTH:
            {
                int orientation = ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation();
                int screen_width = AppConfigHelper.getDisplayWidth();
                if (orientation == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                {
                    return PrimitiveTypeCache.valueOf(screen_width * 369 / 1000);
                }
                else
                {
                    return PrimitiveTypeCache.valueOf(screen_width * 323 / 1000);
                }
            }
            case ID_POPUP_BOTTOM_BUTTON_HEIGHT:
            {
                int height = AppConfigHelper.getMaxDisplaySize() * 70 / 1000;
                return PrimitiveTypeCache.valueOf(height);
            }
            case ID_POPUP_BUTTON_PADDING:
            {
                int shadowHeight = 12;
                int paddingHeight = (POPUP_BOTTOM_PART_HEIGHT.getInt() - shadowHeight - POPUP_BOTTOM_BUTTON_HEIGHT.getInt()) / 2;
                return PrimitiveTypeCache.valueOf(paddingHeight);
            }
            case ID_MAP_LOGO_Y_WITH_POI_PAGINATION:
            {
                return PrimitiveTypeCache.valueOf(MAP_LOGO_Y.getInt() - MAP_POI_INDICATOR_HEIGHT.getInt());
            }
            case ID_MAP_VIEW_LIST_Y_WITH_POI_PAGINATION:
            {
                return PrimitiveTypeCache.valueOf(CURRENT_LOCATION_Y.getInt() - MAP_POI_INDICATOR_HEIGHT.getInt());
            }
            case ID_ZOOM_IN_Y_WITH_POI_PAGINATION:
            {
                if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                    return PrimitiveTypeCache.valueOf(ZOOM_IN_Y.getInt());
                else
                    return PrimitiveTypeCache.valueOf(ZOOM_IN_Y.getInt() - MAP_POI_INDICATOR_HEIGHT.getInt());
            }
            case ID_MAP_VIEW_LIST_Y_WITH_BOTTOM_BAR:
            {
                return PrimitiveTypeCache.valueOf(CURRENT_LOCATION_Y.getInt() - BOTTOM_BAR_HEIGHT.getInt());
            }
            case ID_ZOOM_IN_Y_WITH_WITH_BOTTOM_BAR:
            {
                if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                    return PrimitiveTypeCache.valueOf(ZOOM_IN_Y.getInt());
                else
                    return PrimitiveTypeCache.valueOf(ZOOM_IN_Y.getInt() - BOTTOM_BAR_HEIGHT.getInt());
            }
            case ID_MAP_Y_WITHOUT_SEARCHBAR:
            {
                return PrimitiveTypeCache.valueOf(MAP_Y.getInt() - COMMON_ONEBOX_HEIGHT.getInt());
            }
            case ID_MAP_HEIGHT_WITHOUT_SEARCHBAR:
            {
                return PrimitiveTypeCache.valueOf(MAP_HEIGHT.getInt() + COMMON_ONEBOX_HEIGHT.getInt());
            }
        }
        return null;
    }

}
