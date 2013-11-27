/**
 *
 * Copyright 2013 TeleNav, Inc. All rights reserved.
 * TnXmlAutoCompleteTextView.java
 *
 */
package com.telenav.ui.android;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.AutoCompleteTextView;

import com.telenav.app.android.scout_us.R;
import com.telenav.i18n.ResourceBundle;
import com.telenav.res.INinePatchImageRes;
import com.telenav.res.ResourceManager;
import com.telenav.tnui.core.TnUiArgs.TnUiArgAdapter;
import com.telenav.tnui.graphics.AbstractTnGraphicsHelper;
import com.telenav.tnui.graphics.TnNinePatchImage;
import com.telenav.ui.ImageDecorator;

/**
 *@author fangquanm
 *@date Mar 11, 2013
 */
public class TnXmlAutoCompleteTextView extends AutoCompleteTextView
{
    public TnXmlAutoCompleteTextView(Context context)
    {
        this(context, null);
    }

    public TnXmlAutoCompleteTextView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init(context, attrs);
    }

    public TnXmlAutoCompleteTextView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init(context, attrs);

    }

    private void init(Context context, AttributeSet attrs)
    {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TnXmlEditText);
        String fileName = a.getString(R.styleable.TnXmlEditText_tnBackground);
        if (fileName != null && fileName.length() > 0)
        {
            ResourceBundle bundle = ResourceManager.getInstance().getCurrentBundle();
            Drawable assetsImage = null;
            if (fileName.contains(".9.png"))
            {
                byte[] imageData = bundle.getGenericImage(fileName,
                    INinePatchImageRes.FAMILY_NINE_PATCH);
                if (imageData != null)
                {
                    TnNinePatchImage image = new TnNinePatchImage(
                            AbstractTnGraphicsHelper.getInstance().createImage(imageData));
                    assetsImage = new AssetsImageDrawable(image);
                }
            }
            else
            {
                TnUiArgAdapter specialImageAdapter = new TnUiArgAdapter(fileName,
                        ImageDecorator.instance);
                assetsImage = new AssetsImageDrawable(specialImageAdapter);
            }
            this.setBackgroundDrawable(assetsImage);
        }
        a.recycle();
    }
}