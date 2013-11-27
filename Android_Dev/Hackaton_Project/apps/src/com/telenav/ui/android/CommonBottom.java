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
 *@author chrbu
 *@date 2013-4-11
 */
public class CommonBottom extends LinearLayout
{
    int portraintHeight, landscapeHeight;
    
    public CommonBottom(Context context)
    {
        super(context);
    }
    
    public CommonBottom(Context context, AttributeSet attrs)
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
        android.view.ViewGroup.LayoutParams bottombarContainer = this.getLayoutParams();
        if (orientation == AbstractTnUiHelper.ORIENTATION_LANDSCAPE)
        {
            if (landscapeHeight > 0)
                bottombarContainer.height = landscapeHeight;
            else
                bottombarContainer.height = (int) this.getResources().getDimension(
                    R.dimen.commonBottom0ContainerlandscapeLayoutHeight);
        }
        else
        {
            if (portraintHeight > 0)
                bottombarContainer.height = portraintHeight;
            else
                bottombarContainer.height = (int) this.getResources().getDimension(
                    R.dimen.commonBottom0ContainerPortraitLayoutHeight);
        }
    }
    
    public void setHeight(int portraitHeight, int landscapeHeight)
    {
        this.portraintHeight = portraitHeight;
        this.landscapeHeight = landscapeHeight;
    }
   
    public int getRealHeight()
    {
        int height = 0;
        if (portraintHeight > 0)
            height = portraintHeight;
        else
        {
            int orientation = ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation();
            if (orientation == AbstractTnUiHelper.ORIENTATION_LANDSCAPE)
            {
                height = (int) this.getResources().getDimension(R.dimen.commonBottom0ContainerlandscapeLayoutHeight);
            }
            else
            {
                height = (int) this.getResources().getDimension(R.dimen.commonBottom0ContainerPortraitLayoutHeight);
            }
        }
        return height;
    }

}
