/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * AndroidFrogTextField.java
 *
 */
package com.telenav.tnui.widget.android;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputFilter.LengthFilter;
import android.text.InputType;
import android.text.Selection;
import android.text.Spanned;
import android.text.method.DigitsKeyListener;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TextKeyListener;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.telenav.logger.Logger;
import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.core.INativeUiComponent;
import com.telenav.tnui.core.TnPrivateEvent;
import com.telenav.tnui.core.TnUiEvent;
import com.telenav.tnui.core.android.AndroidUiEventHandler;
import com.telenav.tnui.core.android.AndroidUiHelper;
import com.telenav.tnui.core.android.AndroidUiMethodHandler;
import com.telenav.tnui.graphics.AbstractTnGraphics;
import com.telenav.tnui.widget.TnTextField;

/**
 * A textfield class which can input one line text.
 * 
 * @author jshjin (jshjin@telenav.cn)
 * @date 2010-7-8
 */
public class AndroidTextField extends EditText implements INativeUiComponent, OnClickListener, OnLongClickListener
{
    protected TnTextField textField;

    private boolean isMultiLine = false;

    /**
     * Constructor for TextField.
     * 
     * @param context the context.
     * @param textField the outer textField.
     */
    public AndroidTextField(Context context, TnTextField textField)
    {
        super(context);
        this.textField = textField;

        this.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        // we should not set OnClickListener, this will cause that will not show keyboard for the no-touch mode.
        // this.setOnClickListener(this);
        this.setOnLongClickListener(this);

        this.setTextColor(Color.BLACK);
    }

    public boolean onTouchEvent(MotionEvent event)
    {
        boolean isHandled = AndroidUiEventHandler
                .onTouch(this.textField, event);

        return isHandled ? true : super.onTouchEvent(event);
    }

    public Object callUiMethod(int eventMethod, final Object[] args)
    {
        Object obj = AndroidUiMethodHandler.callUiMethod(textField, this, eventMethod, args);

        if (!AndroidUiMethodHandler.NO_HANDLED.equals(obj))
            return obj;

        switch (eventMethod)
        {
            case TnTextField.METHOD_SET_CURSOR_VISIBLE:
            {
                this.setCursorVisible(((Boolean) args[0]).booleanValue());
                break;
            }
            case TnTextField.METHOD_SET_TEXT_SIZE:
            {
                int tmpTextSize = ((Integer) args[0]).intValue();
                this.setTypeface((Typeface) this.textField.getFont().getNativeFont());
                this.setTextSize(TypedValue.COMPLEX_UNIT_PX, tmpTextSize);
                break;
            }
            case TnTextField.METHOD_GET_CURSOR_INDEX:
            {
                return Integer.valueOf(this.getSelectionStart());
            }
            case TnTextField.METHOD_GET_SCROLLX:
            {
                return Integer.valueOf(this.getScrollX());
            }
            case TnTextField.METHOD_GET_TEXT:
            {
                return this.getText().toString();
            }
            case TnTextField.METHOD_SET_CURSOR_INDEX:
            {
                if (((Integer) args[0]).intValue() > this.getText().length())
                {
                    this.setSelection(this.getText().length());
                }
                else
                {
                    this.setSelection(((Integer) args[0]).intValue());
                }
                break;
            }
            case TnTextField.METHOD_SET_HINT:
            {
                this.setHint((String) args[0]);
                break;
            }
            case TnTextField.METHOD_SET_MAX_SIZE:
            {
                this.setFilters(new InputFilter[]{ new LengthFilter(((Integer)args[0]).intValue()) });
                break;
            }
            case TnTextField.METHOD_SET_TEXT:
            {
                ((AndroidUiHelper) AndroidUiHelper.getInstance()).runOnUiThread(new Runnable()
                {
                    public void run()
                    {
                        setText((String) args[0]);
                        // set cursor's position.
                        Editable etext = getText();
                        int position = etext.length();
                        Selection.setSelection(etext, position);
                    }
                });
                break;
            }
            case TnTextField.METHOD_SET_KEY_LISTENER:
            {
                int textInputType =InputType.TYPE_CLASS_TEXT
                        | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS|(isMultiLine ? InputType.TYPE_TEXT_FLAG_MULTI_LINE : 0) ;
                switch (((Integer) args[0]).intValue())
                {
                    case TnTextField.TEXT_TYPE_CAPITALIZED_WORD:
                        this.setInputType(textInputType|InputType.TYPE_TEXT_FLAG_CAP_WORDS);
                        break;
                    case TnTextField.TEXT_TYPE_LOWER_CASE:
                        this.setInputType(textInputType);
                        break;
                    case TnTextField.TEXT_TYPE_UPPER_CASE:
                        this.setInputType(textInputType|InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
                        break;
                    case TnTextField.NUMERIC:
                        this.setKeyListener(DigitsKeyListener.getInstance("1234567890 "));
                        break;
                    case TnTextField.ADDRESS:
                        this.setKeyListener(new AndroidFrogDigitsKeyListener());
                        break;
                    case TnTextField.PHONENUMBER:
                        this.setInputType(InputType.TYPE_CLASS_PHONE);
                        break;
                    case TnTextField.TEXT_TYPE_PASSWORD:
						this.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        this.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        break;
                    default:
					{
                        this.setKeyListener(new TextKeyListener(TextKeyListener.Capitalize.NONE, false));
						this.setInputType(InputType.TYPE_NULL|InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
					}
                }
                break;
            }
            case TnTextField.METHOD_SET_ENABLE:
            {
                this.setEnabled(((Boolean) args[0]).booleanValue());
                break;
            }
            case TnTextField.METHOD_SELECT_ALL:
            {
                this.selectAll();
                break;
            }
            case TnTextField.METHOD_SET_PADDING:
            {
                if (args != null && args.length > 3)
                {
                    int leftPadding = ((Integer) args[0]).intValue();
                    int topPadding = ((Integer) args[1]).intValue();
                    int rightPadding = ((Integer) args[2]).intValue();
                    int bottomPadding = ((Integer) args[3]).intValue();
                    this.setPadding(leftPadding, topPadding, rightPadding, bottomPadding);
                }
                break;
            }
            case TnTextField.METHOD_SET_SINGLELINE:
            {
                if (args != null && args.length > 0)
                {
                    boolean isSingleLine = ((Boolean) args[0]).booleanValue();
                    this.setSingleLine(isSingleLine);
                }
                break;
            }
            case TnTextField.METHOD_SET_INPUTBOX_WIDTH:
            {
                if (args != null && args.length > 0)
                {
                    this.setWidth(((Integer) args[0]).intValue());
                }
                break;
            }
            case TnTextField.METHOD_SET_INPUTBOX_HEIGHT:
            {
                if (args != null && args.length > 0)
                {
                    this.setHeight(((Integer) args[0]).intValue());
                }
                break;
            }
            case TnTextField.METHOD_CLOSE_VIRTUAL_KEYBOARD:
            {
                closeVirtualKeyBoard();
                break;
            }
            case TnTextField.METHOD_SHOW_VIRTUAL_KEYBOARD:
            {
                showVirtualKeyBoard();
                break;
            }
            case TnTextField.METHOD_SET_TEXT_GRAVITY:
            {
                int value = ((Integer) args[0]).intValue();
                int nativeGravity = Gravity.LEFT;
                if ((value & AbstractTnGraphics.LEFT) == AbstractTnGraphics.LEFT)
                    nativeGravity |= Gravity.LEFT;
                if ((value & AbstractTnGraphics.TOP) == AbstractTnGraphics.TOP)
                    nativeGravity |= Gravity.TOP;
                if ((value & AbstractTnGraphics.HCENTER) == AbstractTnGraphics.HCENTER)
                    nativeGravity |= Gravity.CENTER_HORIZONTAL;
                if ((value & AbstractTnGraphics.VCENTER) == AbstractTnGraphics.VCENTER)
                    nativeGravity |= Gravity.CENTER_VERTICAL;
                if ((value & AbstractTnGraphics.BOTTOM) == AbstractTnGraphics.BOTTOM)
                    nativeGravity |= Gravity.BOTTOM;
                if ((value & AbstractTnGraphics.RIGHT) == AbstractTnGraphics.RIGHT)
                    nativeGravity |= Gravity.RIGHT;
                this.setGravity(nativeGravity);
                break;
            }
            case TnTextField.METHOD_SET_LINES:
            {
                if (args != null && args.length > 0)
                {
                    if (args[0] instanceof Integer)
                    {
                        Integer num = (Integer) args[0];
                        if (num.intValue() > 0)
                            this.setLines(num.intValue());
                    }
                }
                break;
            }
            case TnTextField.METHOD_SET_BACKGROUNDDRAWABLE:
            {
                if (args[0] instanceof Integer)
                {
                    int color = ((Integer) args[0]).intValue();
                    this.setBackgroundDrawable(new ColorDrawable(color));
                }
                break;
            }
            case TnTextField.METHOD_GET_SCROLLY:
            {
                return Integer.valueOf(this.getScrollY());
            }
            case TnTextField.METHOD_SET_HINT_COLOR:
            {
                if (args[0] instanceof Integer)
                {
                    int color = ((Integer) args[0]).intValue();
                    this.setHintTextColor(color);
                }
                break;
            }
            case TnTextField.METHOD_SET_TEXT_COLOR:
            {
                if (args[0] instanceof Integer)
                {
                    int color = ((Integer) args[0]).intValue();
                    this.setTextColor(color);
                }
                break;
            }
            case TnTextField.METHOD_SET_NEXT_FOCUS_DOWN_ID:
            {
                if (args[0] instanceof Integer)
                {
                    this.setNextFocusDownId(((Integer) args[0]).intValue());
                }
                break;
            }

        }
        return null;
    }

    public int getNativeHeight()
    {
        return this.getHeight();
    }

    public int getNativeWidth()
    {
        return this.getWidth();
    }

    public int getNativeX()
    {
        return this.getLeft();
    }

    public int getNativeY()
    {
        return this.getTop();
    }

    public AbstractTnComponent getTnUiComponent()
    {
        return this.textField;
    }

    public boolean isNativeFocusable()
    {
        return this.isFocusable();
    }

    public boolean isNativeVisible()
    {
        return this.getVisibility() == VISIBLE;
    }

    public boolean requestNativeFocus()
    {
        return this.requestFocus();
    }

    public void requestNativePaint()
    {
        this.postInvalidate();
    }

    public void setNativeFocusable(boolean isFocusable)
    {
        this.setFocusable(isFocusable);
    }

    public void setNativeVisible(boolean isVisible)
    {
        this.setVisibility(isVisible ? VISIBLE : GONE);
    }

    public void onClick(View v)
    {
        if (!this.textField.isFocused())
        {
            this.textField.dispatchFocusChanged(true);
            invalidate();
        }

        AndroidUiEventHandler.onClick(this.textField);
    }

    public boolean onLongClick(View v)
    {
        return AndroidUiEventHandler.onLongClick(this.textField);
    }

    protected void onTextChanged(CharSequence text, int start, int before, int after)
    {
        if (textField != null)
        {
            textField.notifyTextChanged(false, text.toString());
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        boolean isHandled = AndroidUiEventHandler.onKeyDown(this.textField, keyCode, event);

        return isHandled ? true : super.onKeyDown(keyCode, event);
    }

    public boolean onKeyUp(int keyCode, KeyEvent event)
    {
        boolean isHandled = AndroidUiEventHandler.onKeyUp(this.textField, keyCode, event);

        return isHandled ? true : super.onKeyUp(keyCode, event);
    }

    public boolean onKeyPreIme(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            TnUiEvent uiEvent = new TnUiEvent(TnUiEvent.TYPE_PRIVATE_EVENT, textField);
            uiEvent.setPrivateEvent(new TnPrivateEvent(TnPrivateEvent.ACTION_TEXTFIELD_BACK));

            if (textField.dispatchUiEvent(uiEvent))
                return true;
        }
        return super.onKeyPreIme(keyCode, event);
    }

    private void closeVirtualKeyBoard()
    {
        InputMethodManager inputMethodManager = (InputMethodManager) getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        try
        {
            inputMethodManager.hideSoftInputFromWindow(getWindowToken(), 0);// InputMethodManager.HIDE_NOT_ALWAYS);
        }
        catch (Throwable t)
        {
            Logger.log(this.getClass().getName(), t);
        }
    }

    private void showVirtualKeyBoard()
    {
        InputMethodManager inputMethodManager = (InputMethodManager) getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        try
        {
            inputMethodManager.showSoftInput(AndroidTextField.this, 0);
        }
        catch (Throwable t)
        {
        }

    }

    public void setTextSize(float size)
    {
        float density = ((AndroidUiHelper) AndroidUiHelper.getInstance()).getDensity();
        super.setTextSize(size * density);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        this.textField.sublayout(widthSize, heightSize);

        int prefWidth = this.textField.getPreferredWidth();
        int prefHeight = this.textField.getPreferredHeight();
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        if (prefWidth > 0 && (widthMode == MeasureSpec.AT_MOST || widthMode == MeasureSpec.EXACTLY))
        {
            if (widthSize > 0 && prefWidth > widthSize)
                prefWidth = widthSize;

            widthMeasureSpec = MeasureSpec.makeMeasureSpec(prefWidth, MeasureSpec.EXACTLY);
        }

        if (prefHeight > 0 && (heightMode == MeasureSpec.AT_MOST || heightMode == MeasureSpec.EXACTLY))
        {
            if (heightSize > 0 && prefHeight > heightSize)
                prefHeight = heightSize;

            heightMeasureSpec = MeasureSpec.makeMeasureSpec(prefHeight, MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    static class AndroidFrogDigitsKeyListener extends DigitsKeyListener
    {
        TextKeyListener textListener = new TextKeyListener(TextKeyListener.Capitalize.NONE, false);

        public boolean onKeyDown(final View view, final Editable content,
                final int keyCode, final KeyEvent event) 
        {
            if (keyCode < KeyEvent.KEYCODE_0 || keyCode > KeyEvent.KEYCODE_9)
            {
                return textListener.onKeyDown(view, content, keyCode, event);
            }
            else
            {
                return super.onKeyDown(view, content, keyCode, event);
            }
        }

        public CharSequence filter(CharSequence source, int start, int end,
                Spanned dest, int dstart, int dend) 
        {
            return source;
        }
    }

    protected void onDraw(Canvas canvas)
    {
        if (AbstractTnGraphics.getInstance() != null)
        {
            AbstractTnGraphics.getInstance().setGraphics(canvas);

            canvas.save();

            textField.draw(AbstractTnGraphics.getInstance());

            canvas.restore();
        }

        super.onDraw(canvas);
    }

    public void setSelected(boolean selected)
    {
        super.setSelected(selected);
        dispatchSetSelected(selected);
    }

    protected void drawableStateChanged()
    {
        super.drawableStateChanged();

        boolean gainFocus = this.hasFocus();
        if (this.textField.isFocused() != gainFocus)
        {
            this.textField.dispatchFocusChanged(gainFocus);
            invalidate();
        }
    }

    protected void dispatchSetSelected(boolean selected)
    {
        super.dispatchSetSelected(selected);

        if (selected != textField.isFocused())
            this.textField.dispatchFocusChanged(selected);
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);

        this.textField.dispatchSizeChanged(w, h, oldw, oldh);
    }

    public void createContextMenu(ContextMenu menu)
    {
        super.createContextMenu(menu);

        AndroidUiEventHandler.onCreateContextMenu(this.textField, menu);
    }

    protected void onAttachedToWindow()
    {
        super.onAttachedToWindow();

        this.textField.dispatchDisplayChanged(true);

        int oriType = this.getInputType();
        if (oriType == InputType.TYPE_CLASS_TEXT)
        {
            if (isMultiLine)
            {
                this.setSingleLine(false);
            }
            else
            {
                this.setSingleLine(true);
            }
        }
    }

    protected void onDetachedFromWindow()
    {
        super.onDetachedFromWindow();

        this.textField.dispatchDisplayChanged(false);
    }

    public void setLines(int lines)
    {
        if (lines > 1)
        {
            isMultiLine = true;
        }
        else
        {
            isMultiLine = false;
        }
        super.setLines(lines);
    }

}
