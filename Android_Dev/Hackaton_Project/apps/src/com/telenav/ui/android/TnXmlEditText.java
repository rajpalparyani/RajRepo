/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * AndroidFrogTextField.java
 *
 */
package com.telenav.ui.android;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.telenav.app.android.scout_us.R;
import com.telenav.i18n.ResourceBundle;
import com.telenav.logger.Logger;
import com.telenav.res.INinePatchImageRes;
import com.telenav.res.ResourceManager;
import com.telenav.tnui.core.TnUiArgs.TnUiArgAdapter;
import com.telenav.tnui.graphics.AbstractTnGraphicsHelper;
import com.telenav.tnui.graphics.TnNinePatchImage;
import com.telenav.ui.ImageDecorator;

/**
 * A textfield class which can input one line text.
 * 
 * @author jyxu
 * @date 2012-11-5
 */
public class TnXmlEditText extends EditText
{
    boolean autoFocus = false;
    public TnXmlEditText(Context context)
    {
        this(context, null);
    }

    public TnXmlEditText(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init(context, attrs);
    }

    public TnXmlEditText(Context context, AttributeSet attrs, int defStyle)
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
    
    public void setAutoFocus(boolean autoFocus)
    {
        this.autoFocus = autoFocus;
    }
    
    
    public InputConnection onCreateInputConnection(EditorInfo outAttrs)
    {
        if(this.autoFocus)
        {
            showVirtualKeyBoard();
        }
        return super.onCreateInputConnection(outAttrs);
    }

    private void showVirtualKeyBoard()
    {
        InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        try
        {
            inputMethodManager.showSoftInput(this,0); 
        }
        catch (Throwable t)
        {
            Logger.log(this.getClass().getName(), t);
        }
    }

}
