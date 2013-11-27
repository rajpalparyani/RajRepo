/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * AddFavoriteUiDecorator.java
 *
 */
package com.telenav.module.ac.contacts;

import com.telenav.module.AppConfigHelper;
import com.telenav.mvc.AbstractCommonUiDecorator;
import com.telenav.tnui.core.AbstractTnUiHelper;
import com.telenav.tnui.core.TnUiArgs.TnUiArgAdapter;
import com.telenav.util.PrimitiveTypeCache;

/**
 * @author wzhu (wzhu@telenav.cn)
 * @date 2010-12-2
 */
class ContactsUiDecorator extends AbstractCommonUiDecorator
{

    private final static int ID_BUTTON_WIDTH = 1;

    private final static int ID_BUTTON_HEIGHT = 2;

    private final static int ID_MESSAGE_BOX_CONTAINER_WIDTH = 3;

    private final static int ID_MESSAGE_BOX_CONTAINER_HEIGHT = 4;

    private final static int ID_BUTTON_CONTAINER_HEIGHT = 5;

    private final static int ID_CHOOSE_ADDRESS_TITLE_HEIGHT = 6;

    private final static int ID_NULLFEIELD_WIDTH = 7;

    public static final int SCALE = 10000;

    public TnUiArgAdapter BUTTON_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_BUTTON_WIDTH), this);

    public TnUiArgAdapter MESSAGE_BOX_CONTAINER_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_MESSAGE_BOX_CONTAINER_WIDTH), this);

    public TnUiArgAdapter MESSAGE_BOX_CONTAINER_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_MESSAGE_BOX_CONTAINER_HEIGHT),
            this);

    public TnUiArgAdapter BUTTON_CONTAINER_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_BUTTON_CONTAINER_HEIGHT), this);

    public TnUiArgAdapter BUTTON_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_BUTTON_HEIGHT), this);

    public TnUiArgAdapter CHOOSE_ADDRESS_TITLE_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_CHOOSE_ADDRESS_TITLE_HEIGHT), this);

    public TnUiArgAdapter NULLFEIELD_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_NULLFEIELD_WIDTH), this);

    protected Object decorateDelegate(TnUiArgAdapter args)
    {
        int key = ((Integer) args.getKey()).intValue();
        switch (key)
        {
            case ID_BUTTON_WIDTH:
            {
                return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth() * 9600 / SCALE);
            }
            case ID_MESSAGE_BOX_CONTAINER_WIDTH:
            {
                if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 600 / SCALE);
                else
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 800 / SCALE);
            }
            case ID_MESSAGE_BOX_CONTAINER_HEIGHT:
            {
                if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 200 / SCALE);
                else
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 300 / SCALE);
            }
            case ID_BUTTON_CONTAINER_HEIGHT:
            {
                if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 1300 / SCALE);
                else
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 1800 / SCALE);
            }
            case ID_BUTTON_HEIGHT:
            {
                if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 670 / SCALE);
                else
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 1000 / SCALE);
            }
            case ID_CHOOSE_ADDRESS_TITLE_HEIGHT:
            {
                if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                    return PrimitiveTypeCache.valueOf(((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getDisplayHeight() * 1200
                            / SCALE);
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
            case ID_NULLFEIELD_WIDTH:
            {
                if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth() * 600 / SCALE);
                else
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth() * 800 / SCALE);
            }
        }
        return null;
    }

}
