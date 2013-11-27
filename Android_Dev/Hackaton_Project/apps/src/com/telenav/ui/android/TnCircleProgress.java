/**
 *
 * Copyright 2013 TeleNav, Inc. All rights reserved.
 * TnCircleProgress.java
 *
 */
package com.telenav.ui.android;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.telenav.module.AppConfigHelper;
import com.telenav.tnui.core.TnUiArgs;
import com.telenav.tnui.core.TnUiArgs.ITnUiArgsDecorator;
import com.telenav.tnui.core.TnUiArgs.TnUiArgAdapter;
import com.telenav.ui.UiFactory;
import com.telenav.ui.citizen.CitizenCircleAnimation;
import com.telenav.util.PrimitiveTypeCache;

/**
 * @author jpwang
 * @date 2013-3-14
 */
public class TnCircleProgress extends LinearLayout
{
    public TnCircleProgress(Context context)
    {
        super(context);
        init();
    }

    public TnCircleProgress(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    private void init()
    {
        CitizenCircleAnimation circle =  UiFactory.getInstance()
                .createCircleAnimation(0, true);
        int minAnimationSize = AppConfigHelper.getMinDisplaySize()/10;
        int maxDropSize = minAnimationSize / 10;
        circle.setDropSizes(new int[]
        { maxDropSize,maxDropSize-1 });
        
        circle.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH,
            new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
            {
                public Object decorate(TnUiArgAdapter args)
                {
                    return PrimitiveTypeCache.valueOf(TnCircleProgress.this.getLayoutParams().width);
                }
            }));

        circle.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT,
            new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
            {
                public Object decorate(TnUiArgAdapter args)
                {
                    return PrimitiveTypeCache.valueOf(TnCircleProgress.this.getLayoutParams().height);
                }
            }));
        
        this.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        
        this.addView((View)circle.getNativeUiComponent());
    }
}
