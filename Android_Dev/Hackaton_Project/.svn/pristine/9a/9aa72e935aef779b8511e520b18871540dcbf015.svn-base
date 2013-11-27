/**
 *
 * Copyright 2013 TeleNav, Inc. All rights reserved.
 * DriveWithFriendsUiDecorator.java
 *
 */
package com.telenav.module.dwf;

import android.content.Context;

import com.telenav.app.android.AndroidPersistentContext;
import com.telenav.app.android.scout_us.R;
import com.telenav.module.AppConfigHelper;
import com.telenav.mvc.AbstractCommonUiDecorator;
import com.telenav.mvc.AbstractModel;
import com.telenav.tnui.core.AbstractTnUiHelper;
import com.telenav.tnui.core.TnUiArgs.TnUiArgAdapter;
import com.telenav.tnui.graphics.AbstractTnFont;
import com.telenav.ui.ImageDecorator;
import com.telenav.ui.UiStyleManager;
import com.telenav.ui.citizen.android.AndroidCitizenUiHelper;
import com.telenav.util.PrimitiveTypeCache;

/**
 * @author fangquanm
 * @date Jul 1, 2013
 */
class DriveWithFriendsUiDecorator extends AbstractCommonUiDecorator
{
    private final static int ID_LABEL_TITLE_HEIGHT = 1;

    private static final int ID_LABEL_TITLE_WIDTH = 2;

    private static final int ID_ZERO_VALUE = 3;

    private static final int ID_TITLE_LEFT_GAP = 4;

    private static final int ID_TITLE_RIGHT_GAP = 5;

    private final static int ID_TITLE_ICON_WIDTH = 6;

    private final static int ID_TITLE_ICON_HEIGHT = 7;

    private final static int ID_STATIC_MAP_WIDTH = 8;

    private final static int ID_STATIC_MAP_HEIGHT = 9;

    private final static int ID_STATIC_MAP_X = 10;

    private final static int ID_STATIC_MAP_Y = 11;

    private final static int ID_DRIVE_INFO_WIDTH = 12;

    private final static int ID_DRIVE_INFO_HEIGHT = 13;

    private final static int ID_DRIVE_INFO_Y = 14;

    private final static int ID_LIST_INFO_WIDTH = 15;

    private final static int ID_LIST_INFO_HEIGHT = 16;

    private final static int ID_LIST_INFO_Y = 17;

    private final static int ID_MAP_LOGO_WITH_BOTTOM_CONTAINER_Y = 18;

    private final static int ID_DROP_BUTTON_WIDTH = 19;

    private final static int ID_DROP_BUTTON_HEIGHT = 20;

    private final static int ID_DROP_BUTTON_X = 21;

    private final static int ID_DROP_BUTTON_Y = 22;
    
    private final static int ID_DRIVE_INFO_X = 23;

    public TnUiArgAdapter LABEL_TITLE_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_LABEL_TITLE_HEIGHT), this);

    public TnUiArgAdapter LABEL_TITLE_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_LABEL_TITLE_WIDTH), this);

    public TnUiArgAdapter ZERO_VALUE = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_ZERO_VALUE), this);

    public TnUiArgAdapter TITLE_LEFT_GAP = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_TITLE_LEFT_GAP), this);

    public TnUiArgAdapter TITLE_RIGHT_GAP = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_TITLE_RIGHT_GAP), this);

    public TnUiArgAdapter TITLE_ICON_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_TITLE_ICON_WIDTH), this);

    public TnUiArgAdapter TITLE_ICON_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_TITLE_ICON_HEIGHT), this);

    public TnUiArgAdapter STATIC_MAP_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_STATIC_MAP_WIDTH), this);

    public TnUiArgAdapter STATIC_MAP_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_STATIC_MAP_HEIGHT), this);

    public TnUiArgAdapter STATIC_MAP_X = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_STATIC_MAP_X), this);

    public TnUiArgAdapter STATIC_MAP_Y = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_STATIC_MAP_Y), this);

    public TnUiArgAdapter DRIVE_INFO_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_DRIVE_INFO_WIDTH), this);

    public TnUiArgAdapter DRIVE_INFO_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_DRIVE_INFO_HEIGHT), this);

    public TnUiArgAdapter DRIVE_INFO_Y = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_DRIVE_INFO_Y), this);

    public TnUiArgAdapter LIST_INFO_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_LIST_INFO_WIDTH), this);

    public TnUiArgAdapter LIST_INFO_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_LIST_INFO_HEIGHT), this);

    public TnUiArgAdapter LIST_INFO_Y = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_LIST_INFO_Y), this);

    public TnUiArgAdapter MAP_LOGO_WITH_BOTTOM_CONTAINER_Y = new TnUiArgAdapter(
            PrimitiveTypeCache.valueOf(ID_MAP_LOGO_WITH_BOTTOM_CONTAINER_Y), this);

    public TnUiArgAdapter DROP_BUTTON_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_DROP_BUTTON_WIDTH), this);

    public TnUiArgAdapter DROP_BUTTON_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_DROP_BUTTON_HEIGHT), this);

    public TnUiArgAdapter DROP_BUTTON_X = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_DROP_BUTTON_X), this);

    public TnUiArgAdapter DROP_BUTTON_Y = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_DROP_BUTTON_Y), this);
    
    public TnUiArgAdapter DRIVE_INFO_X = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_DRIVE_INFO_X), this);

    private AbstractModel model;

    public DriveWithFriendsUiDecorator(AbstractModel model)
    {
        this.model = model;
    }

    @Override
    protected Object decorateDelegate(TnUiArgAdapter args)
    {
        int key = ((Integer) args.getKey()).intValue();
        int orientation = ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation();
        switch (key)
        {
            case ID_LABEL_TITLE_HEIGHT:
            {
                if (orientation == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                {
                    // unify the title height with native xml screen title height
                    return PrimitiveTypeCache.valueOf(AndroidCitizenUiHelper.getPixelsByDip(53));
                }
                else
                {
                    if (AppConfigHelper.isTabletSize())
                    {
                        return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 938 / 10000);
                    }
                    else
                    {
                        return PrimitiveTypeCache.valueOf(AndroidCitizenUiHelper.getPixelsByDip(38));
                    }
                }
            }
            case ID_LABEL_TITLE_WIDTH:
            {
                Context context = AndroidPersistentContext.getInstance().getContext();
                String title = context.getString(R.string.dwfTitle);

                AbstractTnFont font = UiStyleManager.getInstance().getFont(UiStyleManager.FONT_SCREEN_TITLE);
                return PrimitiveTypeCache.valueOf(font.stringWidth(title) + 16);
            }
            case ID_ZERO_VALUE:
            {
                return PrimitiveTypeCache.valueOf(0);
            }
            case ID_TITLE_LEFT_GAP:
            {
                return PrimitiveTypeCache.valueOf((AppConfigHelper.getDisplayWidth() - LABEL_TITLE_WIDTH.getInt()) / 2);
            }
            case ID_TITLE_RIGHT_GAP:
            {
                return PrimitiveTypeCache.valueOf(TITLE_LEFT_GAP.getInt() - 20 * AppConfigHelper.getMinDisplaySize() / 720
                        - TITLE_BUTTON_WIDTH.getInt());
            }
            case ID_TITLE_ICON_WIDTH:
            {
                return PrimitiveTypeCache.valueOf(AndroidCitizenUiHelper.getPixelsByDip(43));
            }
            case ID_TITLE_ICON_HEIGHT:
            {
                return PrimitiveTypeCache.valueOf(AndroidCitizenUiHelper.getPixelsByDip(32));
            }
            case ID_STATIC_MAP_WIDTH:
            {
                if (orientation == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                {
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth());
                }
                else
                {
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth() - LIST_INFO_WIDTH.getInt());
                }
            }
            case ID_STATIC_MAP_HEIGHT:
            {
                if (orientation == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                {
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() - AppConfigHelper.getStatusBarHeight()
                            - LABEL_TITLE_HEIGHT.getInt() - DRIVE_INFO_HEIGHT.getInt() - LIST_INFO_HEIGHT.getInt());
                }
                else
                {
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() - AppConfigHelper.getStatusBarHeight()
                            - LABEL_TITLE_HEIGHT.getInt() - DRIVE_INFO_HEIGHT.getInt());
                }
            }
            case ID_STATIC_MAP_X:
            {
                if (orientation == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                {
                    return PrimitiveTypeCache.valueOf(0);
                }
                else
                {
                    return LIST_INFO_WIDTH.getInt();
                }
            }
            case ID_STATIC_MAP_Y:
            {
                if (orientation == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                {
                    return PrimitiveTypeCache.valueOf(LABEL_TITLE_HEIGHT.getInt());
                }
                {
                    return LIST_INFO_Y.getInt();
                }
            }
            case ID_DRIVE_INFO_WIDTH:
            {
                if (orientation == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                {
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth());
                }
                else
                {
                    boolean isFullMap = this.model.getBool(IDriveWithFriendsConstants.KEY_B_IS_FULL_MAP);
                    if (isFullMap)
                        return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth());
                    
                    return PrimitiveTypeCache
                            .valueOf(STATIC_MAP_WIDTH.getInt()); 
                }
            }
            case ID_DRIVE_INFO_HEIGHT:
            {
                return PrimitiveTypeCache.valueOf(AndroidCitizenUiHelper.getPixelsByDip(86));
            }
            case ID_DRIVE_INFO_Y:
            {
                if (orientation == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                {
                    return PrimitiveTypeCache.valueOf(LABEL_TITLE_HEIGHT.getInt() + STATIC_MAP_HEIGHT.getInt());
                }
                else
                {
                    return AppConfigHelper.getDisplayHeight() - DRIVE_INFO_HEIGHT.getInt();
                }
            }
            case ID_DRIVE_INFO_X:
            {
                if (orientation == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                {
                    return PrimitiveTypeCache.valueOf(ZERO_VALUE.getInt());
                }
                else
                {
                    boolean isFullMap = this.model.getBool(IDriveWithFriendsConstants.KEY_B_IS_FULL_MAP);
                    if (isFullMap)
                        return 0;
                    
                    return PrimitiveTypeCache
                            .valueOf(STATIC_MAP_X.getInt()); 
                }
            }
            case ID_LIST_INFO_WIDTH:
            {
                if (orientation == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                {
                    boolean isFullMap = this.model.getBool(IDriveWithFriendsConstants.KEY_B_IS_FULL_MAP);
                    if (isFullMap)
                        return 0;

                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth());
                }
                else
                {
                    boolean isFullMap = this.model.getBool(IDriveWithFriendsConstants.KEY_B_IS_FULL_MAP);
                    if (isFullMap)
                        return 0;

                    return PrimitiveTypeCache
                            .valueOf(AppConfigHelper.getDisplayWidth() * 560 / 1280);
                }
            }
            case ID_LIST_INFO_HEIGHT:
            {
                if (orientation == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                {
                    boolean isFullMap = this.model.getBool(IDriveWithFriendsConstants.KEY_B_IS_FULL_MAP);
                    if (isFullMap)
                        return 0;

                    return PrimitiveTypeCache
                            .valueOf((AppConfigHelper.getDisplayHeight() - AppConfigHelper.getStatusBarHeight()
                                    - LABEL_TITLE_HEIGHT.getInt() - DRIVE_INFO_HEIGHT.getInt()) / 2);
                }
                else
                {
                    boolean isFullMap = this.model.getBool(IDriveWithFriendsConstants.KEY_B_IS_FULL_MAP);
                    if (isFullMap)
                        return 0;

                    return AppConfigHelper.getDisplayHeight() - AppConfigHelper.getStatusBarHeight()
                            - LABEL_TITLE_HEIGHT.getInt();
                }
            }
            case ID_LIST_INFO_Y:
            {
                if (orientation == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                {
                    return PrimitiveTypeCache.valueOf(DRIVE_INFO_Y.getInt() + DRIVE_INFO_HEIGHT.getInt());
                }
                else
                {
                    return LABEL_TITLE_HEIGHT.getInt();
                }
            }
            case ID_MAP_LOGO_WITH_BOTTOM_CONTAINER_Y:
            {
                int padding = 0;
                if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                {
                    padding = AppConfigHelper.getDisplayHeight() * 73 / 10000;
                }
                else
                {
                    padding = AppConfigHelper.getDisplayHeight() * 37 / 10000;
                }
                return STATIC_MAP_Y.getInt() + STATIC_MAP_HEIGHT.getInt()
                        - ImageDecorator.IMG_DAY_LOGO_ON_MAP.getImage().getHeight() - padding;
            }
            case ID_DROP_BUTTON_WIDTH:
            {
                return PrimitiveTypeCache.valueOf(ImageDecorator.DWF_MAP_OPEN_UNFOCUSED.getImage().getWidth() + 8);
            }
            case ID_DROP_BUTTON_HEIGHT:
            {
                return PrimitiveTypeCache.valueOf(ImageDecorator.DWF_MAP_OPEN_UNFOCUSED.getImage().getHeight() + 8);
            }
            case ID_DROP_BUTTON_X:
            {
                int padding = PrimitiveTypeCache.valueOf(AndroidCitizenUiHelper.getPixelsByDip(12));
                if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                {
                    return STATIC_MAP_X.getInt() + STATIC_MAP_WIDTH.getInt() - padding - DROP_BUTTON_WIDTH.getInt();
                }
                else
                {
                    return STATIC_MAP_X.getInt() + padding;
                }
            }
            case ID_DROP_BUTTON_Y:
            {
                if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                {
                    return MAP_LOGO_WITH_BOTTOM_CONTAINER_Y.getInt() - DROP_BUTTON_HEIGHT.getInt();
                }
                else
                {
                    int padding = PrimitiveTypeCache.valueOf(AndroidCitizenUiHelper.getPixelsByDip(5));
                    return MAP_LOGO_WITH_BOTTOM_CONTAINER_Y.getInt() - DROP_BUTTON_HEIGHT.getInt() - padding;
                }
            }
        }
        return null;
    }
}
