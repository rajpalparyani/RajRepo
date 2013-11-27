/**
 *
 * Copyright 2013 TeleNav, Inc. All rights reserved.
 * CommonTitle.java
 *
 */
package com.telenav.ui.android;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.telenav.app.android.scout_us.R;
import com.telenav.tnui.core.AbstractTnUiHelper;

/**
 *@author jpwang
 *@date 2013-3-13
 */
public class CommonTitle extends LinearLayout
{   
    public CommonTitle(Context context)
    {
        super(context);
    }
    
    public CommonTitle(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        updateHeight();
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
    
    private void updateHeight()
    {
        int orientation = ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation();
        android.view.ViewGroup.LayoutParams titleLayoutParams = this.findViewById(R.id.commonTitle0Container).getLayoutParams();
        android.view.ViewGroup.LayoutParams mapIconParams = this.findViewById(R.id.commonTitle0IconButton).getLayoutParams();
        if (orientation == AbstractTnUiHelper.ORIENTATION_LANDSCAPE)
        {
            titleLayoutParams.height = (int) this.getResources().getDimension(
                R.dimen.commonTitle0ContainerLayoutHorizontalHeight);
            mapIconParams.height = (int) this.getResources().getDimension(R.dimen.commonTitle0IconButtonLayoutHorizontalHeight);
        }
        else
        {
            titleLayoutParams.height = (int) this.getResources().getDimension(R.dimen.commonTitle0ContainerLayoutHeight);
            mapIconParams.height = (int) this.getResources().getDimension(R.dimen.commonTitle0IconButtonLayoutHeight);
        }
    }
}
