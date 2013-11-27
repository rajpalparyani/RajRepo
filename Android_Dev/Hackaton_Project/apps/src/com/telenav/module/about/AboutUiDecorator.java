/**
 *  Copyright 2011 TeleNav, Inc. All rights reserved.
 *
 **/
package com.telenav.module.about;

/**
 *@author dfqin (dfqin@telenav.cn)
 *@date 2011-2-23
 */

import com.telenav.module.AppConfigHelper;
import com.telenav.mvc.AbstractCommonUiDecorator;
import com.telenav.tnui.core.AbstractTnUiHelper;
import com.telenav.tnui.core.TnUiArgs.TnUiArgAdapter;
import com.telenav.ui.citizen.android.AndroidCitizenUiHelper;
import com.telenav.util.PrimitiveTypeCache;

public class AboutUiDecorator extends AbstractCommonUiDecorator
{
	private final static int TITLE_HEIGHT_INDEX = 1;
	private final static int NULL_FIELD_HEIGHT_INDEX = 2;
	private final static int PANEL_WIDTH_INDEX = 3;
	private final static int PANEL_HEIGHT_INDEX = 4;
	
	
	TnUiArgAdapter TITLE_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(TITLE_HEIGHT_INDEX), this);
	TnUiArgAdapter NULL_FIELD_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(NULL_FIELD_HEIGHT_INDEX), this);
	TnUiArgAdapter PANEL_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(PANEL_WIDTH_INDEX), this);
	TnUiArgAdapter PANEL_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(PANEL_HEIGHT_INDEX), this);
	
	protected Object decorateDelegate(TnUiArgAdapter args)
	{
	    int orientation = ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation();
        int width = AppConfigHelper.getDisplayWidth();
        int height = AppConfigHelper.getDisplayHeight() - AppConfigHelper.getStatusBarHeight();
		int keyValue = ((Integer)args.getKey()).intValue();
		switch(keyValue)
		{
			case TITLE_HEIGHT_INDEX:
			    if(orientation == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                {
                    //unify the title height with native xml screen title height
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
                        return PrimitiveTypeCache.valueOf(height * 1270 / 10000);
                    }
                }
			case NULL_FIELD_HEIGHT_INDEX:
			    int max = width > height ? width : height;
				return PrimitiveTypeCache.valueOf(max * 20 / 1000);
			case PANEL_WIDTH_INDEX:
				return PrimitiveTypeCache.valueOf(width);
			case PANEL_HEIGHT_INDEX:
				return PrimitiveTypeCache.valueOf(height) - TITLE_HEIGHT.getInt();
		}
		return null;
	}
}
