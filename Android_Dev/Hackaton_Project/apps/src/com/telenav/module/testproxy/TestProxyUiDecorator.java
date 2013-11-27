/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * TestProxyUiDecorator.java
 *
 */
package com.telenav.module.testproxy;

import com.telenav.module.AppConfigHelper;
import com.telenav.mvc.AbstractCommonUiDecorator;
import com.telenav.tnui.core.AbstractTnUiHelper;
import com.telenav.tnui.core.TnUiArgs.TnUiArgAdapter;
import com.telenav.util.PrimitiveTypeCache;

/**
 *@author yning
 *@date 2012-11-21
 */
public class TestProxyUiDecorator extends AbstractCommonUiDecorator
{
    private static final int ID_PTN_INPUT_WIDTH = 1;
    
    private static final int ID_PTN_INPUT_HEIGHT = 2;
    
    private static final int ID_NULL_FIELD_HEIGHT = 3;
    
    public TnUiArgAdapter PTN_INPUT_WIDTH = new TnUiArgAdapter(ID_PTN_INPUT_WIDTH, this);
    
    public TnUiArgAdapter PTN_INPUT_HEIGHT = new TnUiArgAdapter(ID_PTN_INPUT_HEIGHT, this);
    
    public TnUiArgAdapter NULL_FIELD_HEIGHT = new TnUiArgAdapter(ID_NULL_FIELD_HEIGHT, this);
    
    
    
    @Override
    protected Object decorateDelegate(TnUiArgAdapter args)
    {
        int key = ((Integer) args.getKey()).intValue();
        
        switch(key)
        {
            case ID_PTN_INPUT_WIDTH:
            {
                return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth() * 76 / 100);
            }
            case ID_PTN_INPUT_HEIGHT:
            {
                if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                {
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getMaxDisplaySize() * 62 / 1000);
                }
                else
                {
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getMaxDisplaySize() * 55 / 1000);
                }
            }
            case ID_NULL_FIELD_HEIGHT:
            {
                return PrimitiveTypeCache.valueOf(20);
            }
        }
        return null;
    }

}
