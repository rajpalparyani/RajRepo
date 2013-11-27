/**
 *
 * Copyright 2013 TeleNav, Inc. All rights reserved.
 * DwfReceiverGridItem.java
 *
 */
package com.telenav.ui.android;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.telenav.app.android.scout_us.R;
import com.telenav.tnui.core.AbstractTnUiHelper;

/**
 * @author fangquanm
 * @date Jul 1, 2013
 */
public class DwfReceiverGridItem extends FrameLayout implements OnClickListener
{
    private View gridItemView;

    private View gridItemPopupView;

    private PopupWindow popupWindow;

    private Bitmap res;

    public DwfReceiverGridItem(Context context)
    {
        super(context);

        init(context);
    }

    public DwfReceiverGridItem(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        init(context);
    }

    private void init(Context context)
    {
        this.gridItemView = LayoutInflater.from(context).inflate(R.layout.dwf_receiver_grid_item, null);
        this.gridItemPopupView = LayoutInflater.from(context).inflate(R.layout.dwf_receiver_grid_item_popup, null);
        this.addView(this.gridItemView);
    }

    public void setItem(String label, String desc, Bitmap res)
    {
        this.res = res;

        TextView textView = (TextView) this.gridItemView.findViewById(R.id.dwfReceiverGridItemText);
        textView.setText(label);
        textView.setOnClickListener(this);

        TextView popupTextView = (TextView) this.gridItemPopupView.findViewById(R.id.dwfReceiverGridItemText);
        popupTextView.setText(label);
        popupTextView.setOnClickListener(this);
        TextView popupDescView = (TextView) this.gridItemPopupView.findViewById(R.id.dwfReceiverGridItemDesc);
        popupDescView.setText(desc);
        popupDescView.setOnClickListener(this);

        ImageView enableImageView = (ImageView) this.gridItemView.findViewById(R.id.dwfReceiverGridItemEnableImage);
        enableImageView.setOnClickListener(this);
        ImageView disableImageView = (ImageView) this.gridItemView.findViewById(R.id.dwfReceiverGridItemDisableImage);
        disableImageView.setOnClickListener(this);
        ImageView popupEnableImageView = (ImageView) this.gridItemPopupView.findViewById(R.id.dwfReceiverGridItemEnableImage);
        popupEnableImageView.setOnClickListener(this);
        if (res != null)
        {
            enableImageView.setImageBitmap(res);
            popupEnableImageView.setImageBitmap(res);
        }
    }

    public String getLabel()
    {
        TextView textView = (TextView) this.gridItemView.findViewById(R.id.dwfReceiverGridItemText);

        return textView.getText().toString();
    }

    public String getDesc()
    {
        TextView popupDescView = (TextView) this.gridItemPopupView.findViewById(R.id.dwfReceiverGridItemDesc);

        return popupDescView.getText().toString();
    }

    public Bitmap getBitmap()
    {
        return this.res;
    }

    @Override
    protected void onDetachedFromWindow()
    {
        super.onDetachedFromWindow();

        hidePopup();
    }

    @Override
    public void onClick(View v)
    {
        ImageView enableImageView = (ImageView) this.gridItemView.findViewById(R.id.dwfReceiverGridItemEnableImage);
        ImageView disableImageView = (ImageView) this.gridItemView.findViewById(R.id.dwfReceiverGridItemDisableImage);

        if (popupWindow == null || !popupWindow.isShowing())
        {
            enableImageView.setVisibility(View.GONE);
            disableImageView.setVisibility(View.VISIBLE);

            this.gridItemPopupView.measure(0, 0);
            int width = this.gridItemPopupView.getMeasuredWidth();
            if (width >= ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getDisplayWidth())
            {
                width = width - width / 10;
            }

            int height = this.gridItemPopupView.getMeasuredHeight();
            this.setSelected(true);
            this.gridItemPopupView.setSelected(true);
            this.popupWindow = new PopupWindow(this.gridItemPopupView, width, height, false);
            this.popupWindow.setOutsideTouchable(true);
            this.popupWindow.setBackgroundDrawable(this.gridItemPopupView.getBackground());
            this.popupWindow.setTouchInterceptor(new OnTouchListener()
            {
                public boolean onTouch(View v, MotionEvent event)
                {

                    if (event.getAction() == MotionEvent.ACTION_OUTSIDE)
                    {
                        if (event.getX() > 0 && event.getX() < gridItemView.getWidth() && event.getY() < 0
                                && event.getY() > -gridItemView.getHeight())
                        {
                            return true;
                        }

                        hidePopup();

                        return true;
                    }

                    return false;
                }
            });
            this.popupWindow.showAsDropDown(this);
        }
        else
        {
            hidePopup();
        }

        if (v.getId() == R.id.dwfReceiverGridItemDisableImage)
        {
            ((ViewGroup) this.getParent()).removeView(this);
        }
    }

    public boolean isShowPopup()
    {
        return this.popupWindow != null && this.popupWindow.isShowing();
    }

    public void hidePopup()
    {
        ImageView enableImageView = (ImageView) this.gridItemView.findViewById(R.id.dwfReceiverGridItemEnableImage);
        ImageView disableImageView = (ImageView) this.gridItemView.findViewById(R.id.dwfReceiverGridItemDisableImage);
        enableImageView.setVisibility(View.VISIBLE);
        disableImageView.setVisibility(View.GONE);

        if (this.popupWindow != null)
        {
            this.setSelected(false);
            this.popupWindow.dismiss();
            popupWindow = null;
        }
    }
}
