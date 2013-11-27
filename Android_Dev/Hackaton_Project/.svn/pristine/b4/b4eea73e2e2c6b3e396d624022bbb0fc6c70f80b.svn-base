/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * FrogTextField.java
 *
 */
package com.telenav.tnui.widget;

import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.core.AbstractTnUiHelper;
import com.telenav.tnui.core.ITnTextChangeListener;
import com.telenav.tnui.core.TnPrivateEvent;
import com.telenav.tnui.core.TnUiArgs;
import com.telenav.tnui.core.TnUiEvent;
import com.telenav.tnui.core.TnUiArgs.TnUiArgAdapter;
import com.telenav.tnui.graphics.AbstractTnFont;
import com.telenav.tnui.graphics.AbstractTnGraphics;
import com.telenav.tnui.graphics.AbstractTnImage;
import com.telenav.tnui.graphics.TnColor;
import com.telenav.tnui.graphics.TnRect;

/**
 * Basic text field class, can be used to input one text line.
 * 
 * @author jshjin (jshjin@telenav.cn)
 * @date 2010-7-8
 */
public class TnTextField extends AbstractTnComponent
{
    /**
     * <b>Call Native Method</b> <br />
     * Set the text shows in the text field.
     */
    public final static int METHOD_SET_TEXT_SIZE = 10000001;

    /**
     * <b>Call Native Method</b> <br />
     * Retrieve current cursor position
     */
    public final static int METHOD_GET_CURSOR_INDEX = 10000002;

    /**
     * <b>Call Native Method</b> <br />
     * Retrieve text in text field
     */
    public final static int METHOD_GET_TEXT = 10000003;

    /**
     * <b>Call Native Method</b> <br />
     * Set current cursor index
     */
    public final static int METHOD_SET_CURSOR_INDEX = 10000004;

    /**
     * <b>Call Native Method</b> <br />
     * Set hint in text field.
     */
    public final static int METHOD_SET_HINT = 10000005;

    /**
     * <b>Call Native Method</b> <br />
     * Set max text size can be input in this text field
     */
    public final static int METHOD_SET_MAX_SIZE = 10000006;

    /**
     * <b>Call Native Method</b> <br />
     * Set text in this text field.
     */
    public final static int METHOD_SET_TEXT = 10000007;

    /**
     * <b>Call Native Method</b> <br />
     * Set key listener for current text field.
     */
    public final static int METHOD_SET_KEY_LISTENER = 10000009;

    /**
     * <b>Call Native Method</b> <br />
     * Set if current text field is enable
     */
    public final static int METHOD_SET_ENABLE = 10000011;

    /**
     * <b>Call Native Method</b> <br />
     * Select all text in current text field
     */
    public final static int METHOD_SELECT_ALL = 10000012;

    /**
     * <b>Call Native Method</b> <br />
     * Select all text in current text field
     */
    public final static int METHOD_SET_PADDING = 10000013;

    /**
     * <b>Call Native Method</b> <br />
     * Select all text in current text field
     */
    public final static int METHOD_SET_SINGLELINE = 10000014;

    /**
     * <b>Call Native Method</b> <br />
     * set the textfield width
     */
    public final static int METHOD_SET_INPUTBOX_WIDTH = 10000015;

    /**
     * <b>Call Native Method</b> <br />
     * set the textfield height
     */
    public final static int METHOD_SET_INPUTBOX_HEIGHT = 10000016;

    /**
     * <b>Call Native Method</b> <br />
     * close the virtual keyboard, for android only now
     */
    public final static int METHOD_CLOSE_VIRTUAL_KEYBOARD = 10000017;

    /**
     * <b>Call Native Method</b> <br />
     * set the lines of the input area
     */
    public final static int METHOD_SET_LINES = 10000018;

    /**
     * <b>Call Native Method</b> <br />
     * set the lines of the input area
     */
    public final static int METHOD_SET_IME = 10000019;

    /**
     * <b>Call Native Method</b>
     * <br />
     * get the scrollx 
     */
    public final static int METHOD_GET_SCROLLX = 10000020;
    
    /**
     * <b>Call Native Method</b>
     * <br />
     *  set background's color drawable
     */
    public final static int METHOD_SET_BACKGROUNDDRAWABLE = 10000021;
    
    /**
     * <b>Call Native Method</b>
     * <br />
     * get the scroll offset of Y dimension.
     */
    public final static int METHOD_GET_SCROLLY = 10000022;
    
    /**
     * <b>Call Native Method</b>
     * <br />
     * set hint text color.
     */
    public final static int METHOD_SET_HINT_COLOR = 10000023;
    
    /**
     * <b>Call Native Method</b>
     * <br />
     * set text color.
     */
    public final static int METHOD_SET_TEXT_COLOR = 10000024;
    
    /**
     * <b>Call Native Method</b> <br />
     * show the virtual keyboard, for android only now
     */
    public final static int METHOD_SHOW_VIRTUAL_KEYBOARD = 10000025;
    
    /**
     * <b>Call Native Method</b> <br />
     * show the virtual keyboard, for android only now
     */
    public final static int METHOD_SET_TEXT_GRAVITY = 10000026;
    
    /**
     * <b>Call Native Method</b> <br />
     * set cursor visible
     */
    public final static int METHOD_SET_CURSOR_VISIBLE = 10000027;
    
    /**
     * <b>Call Native Method</b> <br />
     * set nextFocusDownId
     */
    public final static int METHOD_SET_NEXT_FOCUS_DOWN_ID = 10000028;
    
    /**
     * A special constraint for frogtextfield, set keyboard to numeric initially and will be changed to default after
     * user pressed blank
     */
    public static final int ADDRESS = 0x800000;

    /**
     * constraint can input all letter, number and symbol
     */
    public static final int ANY = 0;

    /**
     * constraint used to input a email
     */
    public static final int EMAILADDR = 1;

    /**
     * constraint used to input number only
     */
    public static final int NUMERIC = 2;

    /**
     * constraint used to input phone number
     */
    public static final int PHONENUMBER = 3;

    /**
     * constraint used to disable input
     */
    public static final int UNEDITABLE = 4;

    /**
     * style no space
     */
    public static final int NO_SPACE = 100;

    /**
     * style all upper case letters
     */
    public static final int TEXT_TYPE_UPPER_CASE = 101;

    /**
     * style all lower case letters
     */
    public static final int TEXT_TYPE_LOWER_CASE = 102;

    /**
     * style capitalized for word
     */
    public static final int TEXT_TYPE_CAPITALIZED_WORD = 103;

    /**
     * style capitalized for sentences
     */
    public static final int TEXT_TYPE_CAPITALIZED_SENTENCES = 104;

	/**
     * style password
     */
    public static final int TEXT_TYPE_PASSWORD = 105;

    protected String initText;

    protected int maxSize;

    protected String hint;

    protected int constraints;

    protected String autoFillText;

    protected int textType;

    protected AbstractTnFont font;

    protected ITnTextChangeListener textChangeListener;

    protected int hintColor;
    
    protected int textColor;
    
    protected int hintKey;
    protected String hintFamily;
    
    protected int nextFocusDownId;

    /**
     * construct a TnTextField
     * 
     * @param id
     * @param initText
     * @param hint
     * @param maxSize
     * @param constraints
     */
    public TnTextField(int id, String initText, String hint, int maxSize, int constraints)
    {
        super(id);

        this.initText = initText;

        this.setHint(hint);
        this.setMaxSize(maxSize);
        this.setConstraints(constraints);

        if (initText != null && initText.trim().length() > 0)
        {
            this.setText(initText);
            nativeUiComponent.callUiMethod(METHOD_SELECT_ALL, null);
        }

        this.setSingleLine(true);
        initDefaultStyle();
    }

    /**
     * 
     * @param id
     * @param initText
     * @param hint
     * @param linesSize
     */
    protected TnTextField(int id, String initText, String hint, int linesSize)
    {
        super(id);
        this.initText = initText;
        this.setHint(hint);
        initDefaultStyle();
        nativeUiComponent.callUiMethod(METHOD_SET_TEXT_SIZE, new Object[]
        { new Integer(font.getSize()) });
        if (linesSize > 0)
        {
            Integer linesNum = new Integer(linesSize);
            Integer[] args = new Integer[]
            { linesNum };
            nativeUiComponent.callUiMethod(METHOD_SET_LINES, args);
        }
    }

    protected void initDefaultStyle()
    {
        this.font = ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).createDefaultFont();
        this.setHintTextColor(TnColor.DKGRAY);
        this.leftPadding = font.getMaxWidth()/4;
        this.rightPadding = leftPadding;
        this.topPadding = 0;
        this.bottomPadding = topPadding;
        this.setPadding(leftPadding, topPadding, rightPadding, bottomPadding);
    }

    public void setPadding(int leftPadding, int topPadding, int rightPadding, int bottomPadding)
    {
        super.setPadding(leftPadding, topPadding, rightPadding, bottomPadding);
        nativeUiComponent.callUiMethod(METHOD_SET_PADDING, new Integer[]
        { new Integer(leftPadding), new Integer(topPadding), new Integer(rightPadding), new Integer(bottomPadding) });
    }

    protected void paint(AbstractTnGraphics graphics)
    {

    }

    /**
     * notify ui listener, text has been changed in currently textfield
     * 
     * @param isUserInput
     */
    public void notifyTextChanged(boolean isUserInput, String text)
    {
        TnUiEvent event = new TnUiEvent(TnUiEvent.TYPE_PRIVATE_EVENT, this);
        TnPrivateEvent pEvent = new TnPrivateEvent(TnPrivateEvent.ACTION_TEXT_CHANGE);
        event.setPrivateEvent(pEvent);
        dispatchUiEvent(event);
    }

    /**
     * set font for currently text field.
     * 
     * @param font
     */
    public void setFont(AbstractTnFont font)
    {
        if (font != null)
        {
            this.font = font;
            nativeUiComponent.callUiMethod(METHOD_SET_TEXT_SIZE, new Object[]
            { new Integer(font.getSize()) });
        }
    }

    /**
     * Retrieve font for currently text field.
     * 
     * @return font
     */
    public AbstractTnFont getFont()
    {
        return this.font;
    }

    /**
     * get auto fill text in the text field.
     * 
     * @return autoFillText
     */
    public String getAutoFillText()
    {
        return autoFillText;
    }

    /**
     * get current cursor index
     * 
     * @return cursor index
     */
    public int getCursorIndex()
    {
        return ((Integer) nativeUiComponent.callUiMethod(METHOD_GET_CURSOR_INDEX, null)).intValue();
    }

    /**
     * get hint for this text field
     * 
     * @return hint
     */
    public String getHint()
    {
        return this.hint;
    }

    /**
     * get max size can be input in currently text field
     * 
     * @return maxSize
     */
    public int getMaxSize()
    {
        return this.maxSize;
    }

    /**
     * get text currently showing in the text field
     * 
     * @return text
     */
    public String getText()
    {
        return (String) nativeUiComponent.callUiMethod(METHOD_GET_TEXT, null);
    }

    /**
     * get text type for current text field
     * 
     * @return textType
     */
    public int getTextType()
    {
        return this.textType;
    }

    /**
     * set auto fill text
     * 
     * @param autoFillText
     */
    public void setAutoFillText(String autoFillText)
    {
        this.autoFillText = autoFillText;
    }

    /**
     * set cursor index
     * 
     * @param index
     */
    public void setCursorIndex(int index)
    {
        nativeUiComponent.callUiMethod(METHOD_SET_CURSOR_INDEX, new Object[]
        { new Integer(index) });
    }

    /**
     * set hint in text field.
     * 
     * @param hint the text.
     */
    public void setHint(String hint)
    {
        this.hint = hint;

        if (this.hint != null)
        {
            nativeUiComponent.callUiMethod(METHOD_SET_HINT, new Object[]
            { hint });
        }
    }
    
    /**
     * set hint in text field.
     * 
     * @param key Key for searched-for resource object.
     * @param familyName the group's name of the strings.
     */
    public void setHint(int key, String familyName)
    {
        this.hintKey = key;
        this.hintFamily = familyName;
        
        this.setHint(getText(key, familyName));
    }

    /**
     * Set hint text color
     * @param hintColor
     */
    public void setHintTextColor(int hintColor)
    {
        if(this.hintColor != hintColor)
        {
            this.hintColor = hintColor;
            nativeUiComponent.callUiMethod(METHOD_SET_HINT_COLOR, new Object[]{new Integer(hintColor)});
        }
    }
    
    /**
     * Set the text color
     * @param textColor
     */
    public void setTextColor(int textColor)
    {
        if(this.textColor != textColor)
        {
            this.textColor = textColor;
            nativeUiComponent.callUiMethod(METHOD_SET_TEXT_COLOR, new Object[]{new Integer(textColor)});
        }
    }
    
    /**
     * Set the nextFocusDownId
     * @param nextFocusDownId
     */
    public void setNextFocusDownId(int nextFocusDownId)
    {
        if(this.nextFocusDownId != nextFocusDownId)
        {
            this.nextFocusDownId = nextFocusDownId;
            nativeUiComponent.callUiMethod(METHOD_SET_NEXT_FOCUS_DOWN_ID, new Object[]{new Integer(nextFocusDownId)});
        }
    }
    
    /**
     * set max size can be input in this text field
     * 
     * @param maxSize
     */
    public void setMaxSize(int maxSize)
    {
        if (maxSize > 0)
        {
            this.maxSize = maxSize;
            nativeUiComponent.callUiMethod(METHOD_SET_MAX_SIZE, new Object[]
            { new Integer(maxSize) });
        }

        changeTextFilter();
    }

    /**
     * set text in current text field
     * 
     * @param text
     * @return TODO
     */
    public boolean setText(String text)
    {
        if (text == null)
            return false;

        String oldText = this.getText();

        if (!text.equals(oldText))
        {
            nativeUiComponent.callUiMethod(METHOD_SET_TEXT, new Object[]
            { text });
            return true;
        }
        else
        {
            return false;
        }

    }

    /**
     * set constraints for current text field.
     * 
     * @param constraints {@link #ANY},{@link #EMAILADDR},{@link #NUMERIC}, {@link #PHONENUMBER}, {@link #UNEDITABLE}
     */
    public void setConstraints(int constraints)
    {
        this.constraints = constraints;
        changeTextFilter();
    }

    /**
     * set isSingleLine for TextFiled.
     * 
     * @param isSingleLine
     */
    public void setSingleLine(boolean isSingleLine)
    {
        nativeUiComponent.callUiMethod(METHOD_SET_SINGLELINE, new Object[]
        { new Boolean(isSingleLine) });
    }

    /**
     * get constraints for current text field
     * 
     * @return constraints
     */
    public int getConstraints()
    {
        return constraints;
    }

    /**
     * set text type for current text field.
     * 
     * @param {@link #NO_SPACE},{@link #TEXT_TYPE_UPPER_CASE},{@link #TEXT_TYPE_LOWER_CASE},
     *        {@link #TEXT_TYPE_CAPITALIZED_WORD}, {@link #TEXT_TYPE_CAPITALIZED_SENTENCES}
     */
    public void setTextType(int textType)
    {
        this.textType = textType;
        changeTextFilter();
    }
    
    /*
     * @see com.telenav.tnui.core.AbstractTnComponent#setBackgroundColor(int)
     */
    public void setBackgroundColor(int backgroundColor)
    {
        this.nativeUiComponent.callUiMethod(METHOD_SET_BACKGROUNDDRAWABLE, new Object[]{new Integer(backgroundColor)});
    }
    
    /**
     * Set text change listener.
     * 
     * @param textChangeListener listener of text change.
     */
    public void setTextChangeListener(ITnTextChangeListener textChangeListener)
    {
        this.textChangeListener = textChangeListener;
    }
    
    /**
     * Retrieve text change listener.
     * 
     * @return listener of text change.
     */
    public ITnTextChangeListener getTextChangeListener()
    {
        return this.textChangeListener;
    }
    
    /**
     * set cursor visible
     * 
     * @param index
     */
    public void setCursorVisible(boolean visible)
    {
        nativeUiComponent.callUiMethod(METHOD_SET_CURSOR_VISIBLE, new Object[]
        { new Boolean(visible) });
    }
    
    protected void reloadI18nResource()
    {
        super.reloadI18nResource();
        
        if(this.hintFamily != null)
        {
            this.setHint(this.hintKey, this.hintFamily);
        }
    }

    protected int getScrollX()
    {
        return ((Integer) this.nativeUiComponent.callUiMethod(
                METHOD_GET_SCROLLX, null)).intValue();
    }
    
    protected int getScrollY()
    {
        return ((Integer) this.nativeUiComponent.callUiMethod(METHOD_GET_SCROLLY, null)).intValue();
    }
    
    protected void paintBackground(AbstractTnGraphics graphics)
    {
        int oldColor = graphics.getColor();
       
        if (this.backgroundDrawable != null)
        {
            if (this.backgroundDrawable.getBounds() == null)
            {
                this.backgroundDrawable.setBounds(new TnRect(0, 0, this.getScrollX() + this.getWidth(), this.getScrollY() + this.getHeight()));
            }
            this.backgroundDrawable.draw(graphics);
        }
        else if (this.tnUiArgs != null)
        {
            TnUiArgAdapter uiArgAdapter = null;
            if (isFocused())
                uiArgAdapter = this.tnUiArgs.get(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS);
            else
                uiArgAdapter = this.tnUiArgs.get(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS);
            if (uiArgAdapter != null)
            {
                AbstractTnImage bgImage = uiArgAdapter.getImage();
                if(bgImage != null)
                {
                    bgImage.setWidth(getWidth());
                    bgImage.setHeight(getHeight());
                    graphics.drawImage(bgImage, getScrollX(), getScrollY(), AbstractTnGraphics.LEFT | AbstractTnGraphics.TOP);
                }
            }
            else if (this.backgroundColor != TnColor.TRANSPARENT)
            {
                graphics.setColor(this.backgroundColor);
                graphics.fillRect(0, 0, this.getScrollX() + this.getWidth(), this.getScrollY() + this.getHeight());
                graphics.setColor(oldColor);
            }
        }
        else if (this.backgroundColor != TnColor.TRANSPARENT)
        {
            graphics.setColor(this.backgroundColor);
            
            graphics.fillRect(0, 0, this.getScrollX() + this.getWidth(), this.getScrollY() + this.getHeight());
            graphics.setColor(oldColor);
        }
    }
    
    protected void onUndisplay()
    {
        super.onUndisplay();
        nativeUiComponent.callUiMethod(METHOD_CLOSE_VIRTUAL_KEYBOARD, null);
    }
    
    protected void changeTextFilter()
    {
        this.nativeUiComponent.callUiMethod(METHOD_SET_KEY_LISTENER, new Object[]
        { new Integer(textType) });

        switch (constraints)
        {
            case ANY:
                break;
            case NUMERIC:
            {
                nativeUiComponent.callUiMethod(METHOD_SET_KEY_LISTENER, new Object[]
                { new Integer(NUMERIC) });
                break;
            }
            case PHONENUMBER:
            {
                nativeUiComponent.callUiMethod(METHOD_SET_KEY_LISTENER, new Object[]
                { new Integer(PHONENUMBER) });
                break;
            }
            case UNEDITABLE:
            {
                nativeUiComponent.callUiMethod(METHOD_SET_ENABLE, new Object[]
                { new Boolean(false) });
                break;
            }
        }
    }
}
