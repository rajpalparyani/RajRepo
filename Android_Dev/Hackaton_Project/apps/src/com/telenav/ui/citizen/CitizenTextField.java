/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * CitizenTextField.java
 *
 */
package com.telenav.ui.citizen;

import com.telenav.telephony.TnTelephonyManager;
import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.core.ITnFocusChangeListener;
import com.telenav.tnui.core.ITnTextChangeListener;
import com.telenav.tnui.core.ITnUiEventListener;
import com.telenav.tnui.core.TnCommandEvent;
import com.telenav.tnui.core.TnMenu;
import com.telenav.tnui.core.TnMotionEvent;
import com.telenav.tnui.core.TnUiArgs;
import com.telenav.tnui.core.TnUiArgs.ITnUiArgsDecorator;
import com.telenav.tnui.core.TnUiArgs.TnUiArgAdapter;
import com.telenav.tnui.core.TnUiEvent;
import com.telenav.tnui.graphics.AbstractTnFont;
import com.telenav.tnui.graphics.AbstractTnGraphics;
import com.telenav.tnui.graphics.AbstractTnImage;
import com.telenav.tnui.widget.TnLinearContainer;
import com.telenav.ui.NinePatchImageDecorator;
import com.telenav.ui.UiFactory;
import com.telenav.ui.UiStyleManager;
import com.telenav.ui.frogui.widget.FrogButton;
import com.telenav.ui.frogui.widget.FrogDropDownField;
import com.telenav.ui.frogui.widget.FrogImageComponent;
import com.telenav.ui.frogui.widget.FrogKeywordFilter;
import com.telenav.ui.frogui.widget.FrogList;
import com.telenav.ui.frogui.widget.FrogListItem;
import com.telenav.ui.frogui.widget.FrogTextField;
import com.telenav.util.PrimitiveTypeCache;

/**
 * A component for citizen style. This is a textField with two icons.
 * 
 * @author wzhu (wzhu@telenav.cn)
 * @date 2010-8-25
 */
public class CitizenTextField extends TnLinearContainer implements ITnUiEventListener, ITnTextChangeListener
{
    public static final int KEY_PREFER_TEXTFIELD_HEIGHT = 10000;

    public static final int KEY_ID_OPERATOR_NUMBER = 1000;
    
    protected ITnTextChangeListener textChangeListener;
    
    protected boolean isDefaultHandling = true;

    /**
     * command for backSpace button
     */
    private int commandBackspace = Integer.MIN_VALUE;
    
    /**
     * command for search button
     */
    private int commandSearch = Integer.MIN_VALUE;

    /**
     * DropDownField
     */
    private FrogDropDownField dropDownField;

    /**
     * backspace button
     */
    private FrogButton buttonBackspace;

    private FrogImageComponent findIconComponent;

    private boolean isNeedShowInputMethod;

    private boolean needHack = false;

    private boolean isTriggerBySetText;
    
    public CitizenTextField(String initText, int maxSize, int keypadConstraints, String hint, int id, AbstractTnImage iconFind, AbstractTnImage iconBackSpace)
    {
        super(id, false, AbstractTnGraphics.VCENTER);
        
        dropDownField = new DropdownTextField(initText, maxSize, keypadConstraints, hint, id + KEY_ID_OPERATOR_NUMBER);
        if (initText != null && initText.trim().length() != 0)
        {
            dropDownField.setCursorIndex(initText.length());
        }
        if( keypadConstraints != FrogTextField.UNEDITABLE )
        {
            dropDownField.setTextChangeListener(this);
            
            buttonBackspace = new FrogButton(0);
            buttonBackspace.setPadding(5, 0, 5, 0);
            buttonBackspace.setCommandEventListener(this);
            TnMenu menu = UiFactory.getInstance().createMenu();
            menu.add("", commandBackspace);
            buttonBackspace.setMenu(menu, AbstractTnComponent.TYPE_CLICK);
            buttonBackspace.setIcon(iconBackSpace, iconBackSpace, AbstractTnGraphics.LEFT);
        }

        initDropDownField();
        initButtonHeight();
        
        if (iconFind != null)
        {
            final int imageWidth = iconFind.getWidth();
            findIconComponent = UiFactory.getInstance().createFrogImageComponent(id + 1, iconFind, AbstractTnGraphics.VCENTER | AbstractTnGraphics.HCENTER);
            TnUiArgAdapter iconSizeAdpater = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(1), new ITnUiArgsDecorator()
                {
                    public Object decorate(TnUiArgAdapter args)
                    {
                        int iconSize = 0;
                        if (tnUiArgs.get(KEY_PREFER_TEXTFIELD_HEIGHT) == null)
                        {
                            iconSize = CitizenTextField.this.getHeight()*8748/10000;
                        }
                        else
                        {
                            iconSize = tnUiArgs.get(KEY_PREFER_TEXTFIELD_HEIGHT).getInt()*8748/10000;
                        }
                        if(iconSize ==  0)
                        {
                            iconSize = imageWidth;
                        }
                        return PrimitiveTypeCache.valueOf(iconSize);
                    }
                });
            findIconComponent.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH,  iconSizeAdpater);
            findIconComponent.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, iconSizeAdpater);
            this.add(findIconComponent);
        }

        add(dropDownField);
        if(buttonBackspace != null )
        {
            add(buttonBackspace);
            if(initText != null && initText.trim().length() > 0)
            {
                buttonBackspace.setVisible(true);
            }
            else
            {
                buttonBackspace.setVisible(false);
            }
        }
    }
    
    public void setFindIcon(AbstractTnImage image)
    {
        this.findIconComponent.setImage(image);
    }
    
    public void setFocusChangeListener(ITnFocusChangeListener focusChangeListener)
    {
        dropDownField.setFocusChangeListener(focusChangeListener);
    }
    
    protected void onDisplay()
    {
        final int minHeight = this.getDropDownField().getFont().getHeight() + 20;
        TnUiArgAdapter preferHeight = this.getTnUiArgs().get(TnUiArgs.KEY_PREFER_HEIGHT);
        if(preferHeight != null && preferHeight.getInt() < minHeight) 
        {
            this.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, new TnUiArgs.TnUiArgAdapter(null, new TnUiArgs.ITnUiArgsDecorator()
            {
                
                public Object decorate(TnUiArgAdapter args)
                {
                    return PrimitiveTypeCache.valueOf(minHeight);
                }
            }));
        }
        super.onDisplay();
    }

    public CitizenTextField(String initText, int maxSize, int keypadConstraints, String hint, int id, AbstractTnImage iconBackSpace)
    {
        this(initText, maxSize, keypadConstraints, hint, id, null, iconBackSpace);
    }
    
    public void setCursorVisible(boolean visible)
    {
        dropDownField.setCursorVisible(visible);
    }
    
    public FrogDropDownField getDropDownField()
    {
        return dropDownField;
    }
    
    public void setTextFieldBackgroundImage(TnUiArgAdapter foucus, TnUiArgAdapter unfoucus)
    {
        dropDownField.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, foucus);
        dropDownField.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, unfoucus);
        if (nativeUiComponent != null)
        {
            requestLayout();
            requestPaint();
        }
    }
    
    public void setTextPadding(int leftPadding, int topPadding, int rightPadding, int bottomPadding)
    {
        dropDownField.setPadding(leftPadding, topPadding, rightPadding, bottomPadding);
        if (nativeUiComponent != null)
        {
            requestLayout();
            requestPaint();
        }
    }

    private void initButtonHeight()
    {
        if( buttonBackspace != null )
        {
            buttonBackspace.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT,
                new TnUiArgAdapter(PrimitiveTypeCache.valueOf(1), new ITnUiArgsDecorator()
                {
                    public Object decorate(TnUiArgAdapter args)
                    {
                        if (tnUiArgs.get(KEY_PREFER_TEXTFIELD_HEIGHT) == null)
                        {
                            return PrimitiveTypeCache.valueOf(CitizenTextField.this.getHeight());
                        }
                        else
                        {
                            return PrimitiveTypeCache.valueOf(tnUiArgs.get(KEY_PREFER_TEXTFIELD_HEIGHT).getInt());
                        }
                    }
                }));
        }
    }

    private void initDropDownField()
    {
        dropDownField.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH,
            new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
            {
                public Object decorate(TnUiArgAdapter args)
                {
                    int boxWidth = 0;
                    if (tnUiArgs.get(TnUiArgs.KEY_PREFER_WIDTH) == null)
                    {
                        boxWidth = CitizenTextField.this.getWidth();
                    }
                    else
                    {
                        boxWidth = tnUiArgs.get(TnUiArgs.KEY_PREFER_WIDTH).getInt();
                    }

                    int buttonHeight = 0;
                    if( buttonBackspace != null && buttonBackspace.isVisible())//backspace button have added or not added, the dropDownField's width should different
                    {
                        buttonBackspace.sublayout(0, 0);
                        buttonHeight = buttonBackspace.getPreferredWidth();
                    }
                    
                    int findIconWithMarginWidth = 0;
                    if (findIconComponent != null)
                    {
                        findIconWithMarginWidth = findIconComponent.getPreferredWidth();
                    }
                    return PrimitiveTypeCache.valueOf(boxWidth - buttonHeight - findIconWithMarginWidth - CitizenTextField.this.leftPadding
                            - CitizenTextField.this.rightPadding);

                }
            }));

        dropDownField.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT,
            new TnUiArgAdapter(PrimitiveTypeCache.valueOf(1), new ITnUiArgsDecorator()
            {
                public Object decorate(TnUiArgAdapter args)
                {
                    if (tnUiArgs.get(KEY_PREFER_TEXTFIELD_HEIGHT) == null)
                    {
                        return PrimitiveTypeCache.valueOf(CitizenTextField.this.getHeight());
                    }
                    else
                    {
                        return PrimitiveTypeCache.valueOf(tnUiArgs.get(KEY_PREFER_TEXTFIELD_HEIGHT).getInt());
                    }
                }
            }));

        dropDownField.getTnUiArgs().put(FrogDropDownField.KEY_LIST_COLOR_ODD,
            new TnUiArgAdapter(PrimitiveTypeCache.valueOf(1), new ITnUiArgsDecorator()
            {
                public Object decorate(TnUiArgAdapter args)
                {
                    return PrimitiveTypeCache.valueOf(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_LI_GR));
                }
            }));

        dropDownField.getTnUiArgs().put(FrogDropDownField.KEY_LIST_COLOR_EVEN,
            new TnUiArgAdapter(PrimitiveTypeCache.valueOf(1), new ITnUiArgsDecorator()
            {
                public Object decorate(TnUiArgAdapter args)
                {
                    return PrimitiveTypeCache.valueOf(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_ME_GR));
                }
            }));
    }

    /**
     * set texts in DropDownFiled
     * 
     * @param texts
     */
    public void setDropDownList(FrogList list)
    {
        dropDownField.setDropDownList(list);
    }

    /**
     * set font of DropDownFiled
     * 
     * @param font
     */
    public void setFont(AbstractTnFont font)
    {
        dropDownField.setFont(font);
        if (nativeUiComponent != null)
        {
            requestLayout();
            requestPaint();
        }

    }

    /**
     * set textchange listener
     * 
     * @param listener
     */
    public void setTextChangeListener(ITnTextChangeListener listener)
    {
        textChangeListener = listener;
    }

    /**
     * set background color of the dropdownfield
     * 
     * @param color
     */
    public void setBackgroundColor(int color)
    {
        dropDownField.setBackgroundColor(color);
        if( buttonBackspace != null)
        {
            this.buttonBackspace.setBackgroundColor(color);
        }
        if (nativeUiComponent != null)
        {
            requestLayout();
            requestPaint();
        }
    }

    public void setBackspaceCommand(int backSpaceCommand)
    {
        TnMenu menu = UiFactory.getInstance().createMenu();
        commandBackspace = backSpaceCommand;
        menu.add("", backSpaceCommand);
        if( buttonBackspace != null )
        {
            buttonBackspace.setMenu(menu, AbstractTnComponent.TYPE_CLICK);
        }
    }

    public void setSearchCommand(int searchCommand)
    {
        TnMenu menu = UiFactory.getInstance().createMenu();
        commandSearch = searchCommand;
        menu.add("", searchCommand);
        if( findIconComponent != null )
        {
            findIconComponent.setMenu(menu, AbstractTnComponent.TYPE_CLICK);
        }
    }
    /**
     * handle ui event
     */
    public boolean handleUiEvent(TnUiEvent tnUiEvent)
    {
        AbstractTnComponent component = tnUiEvent.getComponent();
        
        boolean isHandled = false;
        switch (tnUiEvent.getType())
        {
            case TnUiEvent.TYPE_COMMAND_EVENT:
            {
                if (buttonBackspace != null && component == buttonBackspace)
                {
                    dropDownField.setText("");
                    dropDownField.hideDropDownList();
                    dropDownField.requestFocus();
                    if ((commandBackspace != Integer.MIN_VALUE && commandBackspace == tnUiEvent.getCommandEvent().getCommand()))
                    {
                        if (commandListener != null)
                        {
                            commandListener.handleUiEvent(tnUiEvent);
                        }
                    }
                    isHandled = true;
                }
                break;
            }
            case TnUiEvent.TYPE_TOUCH_EVENT:
            {
                if((component instanceof FrogImageComponent)
                        && tnUiEvent.getMotionEvent().getAction() == TnMotionEvent.ACTION_UP)
                {
                    if (commandListener != null)
                    {                
                        TnUiEvent uiEvent = new TnUiEvent(TnUiEvent.TYPE_COMMAND_EVENT, this);
                        uiEvent.setCommandEvent(new TnCommandEvent(commandSearch));
                        commandListener.handleUiEvent(uiEvent);
                    }
                    isHandled = true;
                }
                else if((component instanceof FrogImageComponent)
                        && tnUiEvent.getMotionEvent().getAction() == TnMotionEvent.ACTION_DOWN)
                {
                    isHandled = true;
                }
               
                break;
            }
        }
        
        if(!isHandled)
        {
            String platformVersion = TnTelephonyManager.getInstance().getDeviceInfo().platformVersion;
            /**
             * for 8 version of android firmware, there is a bug when show editfield at popup dialog.
             * see: http://code.google.com/p/android/issues/detail?id=14384
             */
            if (needHack && "8".equals(platformVersion) && this.dropDownField != null && component != buttonBackspace && tnUiEvent.getType() == TnUiEvent.TYPE_TOUCH_EVENT
                    && tnUiEvent.getMotionEvent().getAction() == TnMotionEvent.ACTION_DOWN)
            {
                this.requestFocus();
                this.dropDownField.showVirtualKeyBoard();
                return true;
            }
        }
        
        return isHandled;
    }
    
    public void setHackVirtualKeyBoard(boolean needHack)
    {
        this.needHack = needHack;
    }

    /**
     * Retrieve the string text
     * 
     * @return
     */
    public String getText()
    {
        return this.dropDownField.getText();
    }

    /**
     * Set keyword filter
     * 
     * @param keywordFilter {@link FrogKeywordFilter}
     */
    public void setKeyworkFilter(FrogKeywordFilter keywordFilter)
    {
        this.dropDownField.setKeyWordFilter(keywordFilter);
    }

    /**
     * Retrieve filter
     * 
     * @return {@link FrogKeywordFilter}
     */
    public FrogKeywordFilter getKeyworkFilter()
    {
        return this.dropDownField.getKeyWordFilter();
    }

    /**
     * Set filter interval
     * 
     * @param interval
     */
    public void setFilterInterval(long interval)
    {
        this.dropDownField.setFilterInterval(interval);
    }

    /**
     * Retrieve filter interval
     * 
     * @return long
     */
    public long getFilterInterval()
    {
        return this.dropDownField.getFilterInterval();
    }

    /**
     * Query if dropDown list shown or not.
     * 
     * @return boolean
     */
    public boolean isDropDownListVisible()
    {
        return this.dropDownField.isDropDownListVisible();
    }

    public void setText(String text)
    {
        isTriggerBySetText = this.dropDownField.setText(text);
    }

    public boolean isTriggerBySetText()
    {
        return this.isTriggerBySetText;
    }
    
    /**
     * call back for text change event
     */
    public void onTextChange(AbstractTnComponent component, String text)
    {
        if(text.equals(""))
        {
            if( buttonBackspace != null && buttonBackspace.isVisible())
            {
                buttonBackspace.setVisible(false);
                dropDownField.requestLayout(); //Fix TN-1120, when removed, it didn't re-layout.why?
            }
        }
        else
        {
            if(buttonBackspace != null && !buttonBackspace.isVisible() )
            {
                buttonBackspace.setVisible(true);
                dropDownField.requestLayout();
            }
        }

        if (textChangeListener != null)
        {
            textChangeListener.onTextChange(component, text);
        }
        this.isTriggerBySetText = false;
    }

    /**
     * Set hint text.
     * 
     * @param hint
     */
    public void setHintText(String hint)
    {
        this.dropDownField.setHint(hint);
    }

    /**
     * Set hint text color
     * 
     * @param color
     */
    public void setHintTextColor(int color)
    {
        this.dropDownField.setHintTextColor(color);
    }

    /**
     * Hide drop down list.
     */
    public void hideDropDown()
    {
        this.dropDownField.hideDropDownList();
    }

    /**
     * Show drop down list
     */
    public void showDropDown()
    {
        this.dropDownField.showDropDownList();
    }

    /**
     * Set dropdownlist position
     * 
     * @param argAdapterX
     * @param argAdapterY
     */
    public void setDropDownListPostionArgs(TnUiArgs args)
    {
        if (args != null)
        {
            TnUiArgAdapter adapterPosX = args.get(FrogDropDownField.KEY_DROPDOWNLIST_POS_X);
            TnUiArgAdapter adapterPosY = args.get(FrogDropDownField.KEY_DROPDOWNLIST_POS_Y);
            if (adapterPosX != null)
            {
                this.dropDownField.getTnUiArgs().put(FrogDropDownField.KEY_DROPDOWNLIST_POS_X, adapterPosX);
            }
            if (adapterPosY != null)
            {
                this.dropDownField.getTnUiArgs().put(FrogDropDownField.KEY_DROPDOWNLIST_POS_Y, adapterPosY);
            }
        }
    }

    public void requestTextFieldFocus()
    {
        this.dropDownField.requestFocus();
    }
    
    /**
     * set the text color of the dropdownfield
     * @param textColor
     */
    public void setTextColor(int textColor)
    {
        this.dropDownField.setTextColor(textColor);
    }
    
    class DropdownTextField extends FrogDropDownField
    {

        public DropdownTextField(String initText, int maxSize, int keypadConstraints, String hint, int id)
        {
            super(initText, maxSize, keypadConstraints, hint, id);
        }
        
        protected void onFocusChanged(boolean gainFocus)
        {
            super.onFocusChanged(gainFocus);
            if( this.constraints == FrogTextField.UNEDITABLE )
            {
                return;
            }
            if(gainFocus)
            {
                CitizenTextField.this.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.SEARCH_BOX_INPUT_BG_FOCUSED);
                CitizenTextField.this.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, NinePatchImageDecorator.SEARCH_BOX_INPUT_BG_FOCUSED);
            }
            else
            {
                CitizenTextField.this.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.SEARCH_BOX_INPUT_BG_UNFOCUS);
                CitizenTextField.this.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, NinePatchImageDecorator.SEARCH_BOX_INPUT_BG_UNFOCUS);
            }
            CitizenTextField.this.requestPaint();
        }
        
        protected void onSizeChanged(int w, int h, int oldw, int oldh)
        {
            super.onSizeChanged(w, h, oldw, oldh);
            if(isNeedShowInputMethod)
            {
                isNeedShowInputMethod = false;
                this.showVirtualKeyBoard();
            }
        }
        
        protected void handleDropItemSelected(AbstractTnComponent selectedItem)
        {
            if (selectedItem instanceof FrogListItem)
            {
                if(isDefaultHandling)
                {
                   super.handleDropItemSelected(selectedItem); 
                }
                else
                {
                    hideDropDownList();
                }
            }
        }
    }

    
    public void setIsNeedShowInputMethod(boolean isNeedShow)
    {
        this.isNeedShowInputMethod = isNeedShow;
    }
    public void closeVirtualKeyBoard()
    {
        this.dropDownField.closeVirtualKeyBoard();
    }
    
    public void setDefaultHandling(boolean isDefaultHandling)
    {
        this.isDefaultHandling = isDefaultHandling;
    }
    
    public void setNextFocusDownId(int nextFocusDownId)
    {
        this.dropDownField.setNextFocusDownId(nextFocusDownId);
    }
}
