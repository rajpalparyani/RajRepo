/**
 *
 * Copyright 2013 TeleNav, Inc. All rights reserved.
 * CommonBottomButton.java
 *
 */
package com.telenav.ui.android;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.telenav.app.android.scout_us.R;
import com.telenav.ui.citizen.android.AndroidCitizenUiHelper;

/**
 * @author jpwang
 * @date 2013-3-29
 */
public class CommonBottomButton extends LinearLayout
{
    private TextView leftTextView;

    private TextView rightTextView;

    public CommonBottomButton(Context context)
    {
        super(context);
        initWithContext(context);
    }

    public CommonBottomButton(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        initWithContext(context);
    }

    private void initWithContext(Context context)
    {
        leftTextView = (TextView) this.findViewById(R.id.commonBottomButton0LeftTextView);
        rightTextView = (TextView) this.findViewById(R.id.commonBottomButton0RightTextView);
    }

    public void setButton(String text)
    {
        if (leftTextView == null)
        {
            initWithContext(this.getContext());
        }
        rightTextView.setVisibility(View.GONE);
        leftTextView.setText(text);
        leftTextView.setVisibility(View.VISIBLE);
    }

    public void setButton(String text, int imageRes)
    {
        setButton(text, imageRes, true);
    }

    public void setButton(String text, int imageRes, boolean isDrawableLeft)
    {
        if (leftTextView == null)
        {
            initWithContext(this.getContext());
        }
        if (isDrawableLeft)
        {
            leftTextView.setCompoundDrawablesWithIntrinsicBounds(imageRes, 0, 0, 0);
            rightTextView.setText(text);
            leftTextView.setVisibility(View.VISIBLE);
            rightTextView.setVisibility(View.VISIBLE);
        }
        else
        {
            rightTextView.setCompoundDrawablesWithIntrinsicBounds(imageRes, 0, 0, 0);
            leftTextView.setText(text);
            rightTextView.setVisibility(View.VISIBLE);
            leftTextView.setVisibility(View.VISIBLE);
        }
    }
    
    public void setButton(int textRes, int imageRes, boolean isDrawableLeft)
    {
        setButton((String) this.getResources().getText(textRes), imageRes, isDrawableLeft);
    }

    @Override
    public void setEnabled(boolean enabled)
    {
        super.setEnabled(enabled);
        leftTextView.setEnabled(enabled);
        rightTextView.setEnabled(enabled);
        leftTextView.setTextColor(AndroidCitizenUiHelper.getResourceColor(enabled ? R.color.bottom_control_text_color
                : R.color.bottom_control_disabled_text_color));
        rightTextView.setTextColor(AndroidCitizenUiHelper.getResourceColor(enabled ? R.color.bottom_control_text_color
                : R.color.bottom_control_disabled_text_color));
    }

}
