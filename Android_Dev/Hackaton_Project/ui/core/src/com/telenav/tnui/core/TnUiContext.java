/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * AbstractTnUiContext.java
 *
 */
package com.telenav.tnui.core;

import com.telenav.tnui.graphics.AbstractTnGraphicsHelper;

/**
 * This is the entry point of ui framework.
 * <br />
 * Please invoke this class before use this ui framework.
 * 
 *@author fqming (fqming@telenav.cn)
 *@date 2010-6-17
 */
public abstract class TnUiContext
{
    private static int initCount;
    
    /**
     * init the ui content.
     * 
     * @param context
     * @param uiBinder
     * @param uiHelper
     * @param i18nProvider
     */
    public synchronized static void init(Object context, AbstractTnUiBinder uiBinder, AbstractTnGraphicsHelper uiHelper, AbstractTnI18nProvider i18nProvider)
    {
        if(initCount >= 1)
            return;
        
        AbstractTnUiBinder.getInstance().init(context);
        AbstractTnUiHelper.getInstance().init(context);
        ((AbstractTnUiHelper)AbstractTnUiHelper.getInstance()).setI18nProvider(i18nProvider);
        
        initCount++;
    }
}
