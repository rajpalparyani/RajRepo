/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * FrogDropDownField.java
 *
 */
package com.telenav.ui.frogui.widget;


import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.core.AbstractTnUiHelper;
import com.telenav.tnui.core.ITnUiEventListener;
import com.telenav.tnui.core.TnKeyEvent;
import com.telenav.tnui.core.TnMotionEvent;
import com.telenav.tnui.core.TnPrivateEvent;
import com.telenav.tnui.core.TnUiArgs;
import com.telenav.tnui.core.TnUiArgs.ITnUiArgsDecorator;
import com.telenav.tnui.core.TnUiArgs.TnUiArgAdapter;
import com.telenav.tnui.core.TnUiEvent;
import com.telenav.util.PrimitiveTypeCache;

/**
 *@author duanbo
 *@date 2010-7-15
 */
public class FrogDropDownField extends FrogTextField 
{
    public final static int KEY_DROPDOWNLIST_POS_Y = 200000;
    public final static int KEY_DROPDOWNLIST_POS_X = 200001;
    public final static int KEY_LIST_COLOR_ODD = 2000; 
    public final static int KEY_LIST_COLOR_EVEN = 2001; 
    public final static int DROP_DOWN_ITEM_SELECTED = 20000;
    protected int maxHintNumber = DEFAULT_MAX_HINT_NUMBER;

    protected FrogList list;
    protected FrogFloatPopup popup;
    protected long interval;
    protected FrogKeywordFilter keywordFilter;
    protected ITnUiEventListener listCommandListener;
    protected boolean isShowDropDownAfterLayout = false;
    protected boolean isTriggerBySet = false;
    protected static final int DEFAULT_MAX_HINT_NUMBER = 3;

    /**
     * constructor of FrogDropDown field.
     * 
     * @param initText
     * @param maxSize
     * @param keypadConstraints
     * @param keyboardContraints
     * @param hint
     * @param id
     */
    public FrogDropDownField(String initText, int maxSize,
            int keypadConstraints,  String hint, int id)
    {
        super(initText, maxSize, keypadConstraints, hint, id);
        
    }
    
    
    /**
     * Set drop down list to the text field.
     * If user set the command listener {@link ITnUiEventListener} to list, 
     * the list will be handled the selecting event first, then handle user's listener
     * @param list {@link FrogList}
     */
    public void setDropDownList(FrogList list)
    {
        this.list = list;
        if(this.list.getTnUiArgs().get(TnUiArgs.KEY_PREFER_WIDTH) == null)
        {
            this.list.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
            {
                public Object decorate(TnUiArgAdapter args)
                {
                    return PrimitiveTypeCache.valueOf(FrogDropDownField.this.getPreferredWidth());
                }
            }));
        }
        
        this.listCommandListener = list.getCommandEventListener();
        this.list.setCommandEventListener(new ITnUiEventListener()
        {
            public boolean handleUiEvent(TnUiEvent tnUiEvent)
            {
                boolean isHandled = false;
                if (tnUiEvent.getType() == TnUiEvent.TYPE_COMMAND_EVENT)
                {
                    if (listCommandListener != null)
                    {
                        isHandled = listCommandListener.handleUiEvent(tnUiEvent);
                    }
                    
                    AbstractTnComponent selectedItem = tnUiEvent.getComponent();
                    handleDropItemSelected(selectedItem);
                }
                return isHandled;
            }
        });
    }
    
    /**
     * Retrieve drop-down list.
     * 
     * @return list
     */
    public FrogList getDropDownList()
    {
        return this.list;
    }

    protected void onUndisplay()
    {
        super.onUndisplay();
        this.hideDropDownList();
    }
    
    /**
     * Set {@link FrogKeywordFilter}
     * @param keywordFilter
     */
    public void setKeyWordFilter(FrogKeywordFilter keywordFilter)
    {
        this.keywordFilter = keywordFilter;
    }
    
    /**
     * Retrieve {@link FrogKeywordFilter}
     * @return {@link FrogKeywordFilter}
     */
    public FrogKeywordFilter getKeyWordFilter()
    {
        return this.keywordFilter;
    }
    
    protected boolean handleUiEvent(TnUiEvent tnUiEvent)
    {
        switch (tnUiEvent.getType())
        {
            case TnUiEvent.TYPE_KEY_EVENT:
            {
                switch (tnUiEvent.getKeyEvent().getCode())
                {
                    case TnKeyEvent.KEYCODE_BACK:
                    {
                        if (tnUiEvent.getKeyEvent().getAction()== TnKeyEvent.ACTION_UP)
                        {
                            if (popup != null && popup.isVisible())
                            {
                                hideDropDownList();
                                return true;
                            }
                            else
                            {
                                return false;
                            }
                        }
                        break;
                    }
                    case TnKeyEvent.KEYCODE_PAD_CENTER:
                    {
                        if (tnUiEvent.getKeyEvent().getAction() == TnKeyEvent.ACTION_UP)
                        {
                            if (list == null)
                                return false;

                            if (this.keywordFilter != null)
                            {
                                keywordFilter.filter(this.getText());
                            }

                            if (popup == null || !popup.isVisible())
                            {
                                showDropDownList();
                            }
                            else if (popup != null && popup.isVisible())
                            {
                                if (listCommandListener != null)
                                {
                                    listCommandListener.handleUiEvent(tnUiEvent);
                                }

                                AbstractTnComponent component = tnUiEvent.getComponent();

                                if (component instanceof FrogListItem)
                                {
                                    FrogDropDownField.this.setText(((FrogListItem) component).getText());
                                    hideDropDownList();
                                }
                            }
                        }
                        break;
                    }
                }
                break;
            }
            case TnUiEvent.TYPE_PRIVATE_EVENT:
            {
                switch (tnUiEvent.getPrivateEvent().getAction())
                {
                    case TnPrivateEvent.ACTION_TEXT_CHANGE:
                    {
                        if (textChangeListener != null)
                            textChangeListener.onTextChange(this, this.getText());
                        if(isTriggerBySet && !isHandleTextChangeBySet())
                        {
                            isTriggerBySet = false;
                            return true;
                        }
                        if (this.list != null && (popup == null || !popup.isVisible()))
                        {
                            showDropDownList();
                        }
                        String currString = this.getText();
                        if (keywordFilter != null)
                        {
                            keywordFilter.filter(currString);
                        }
                        return true;
                    }
                    case TnPrivateEvent.ACTION_TEXTFIELD_BACK:
                    {
                        if (popup != null && popup.isVisible())
                        {
                            hideDropDownList();
                            return true;
                        }
                        else
                        {
                            return false;
                        }
                 
                    }
                }
                break;
            }
            case TnUiEvent.TYPE_TOUCH_EVENT:
            {
                if (tnUiEvent.getMotionEvent().getAction() == TnMotionEvent.ACTION_UP)
                {
                    if (list == null)
                        return false;

                    if (this.keywordFilter != null)
                    {
                        keywordFilter.filter(this.getText());
                    }

                    if (popup == null || !popup.isVisible())
                    {
                        showDropDownList();
                    }
                    else if (popup != null && popup.isVisible())
                    {
                        if (listCommandListener != null)
                        {
                            listCommandListener.handleUiEvent(tnUiEvent);
                        }

                        AbstractTnComponent component = tnUiEvent.getComponent();

                        if (component instanceof FrogListItem)
                        {
                            FrogDropDownField.this.setText(((FrogListItem) component)
                                    .getText());
                            hideDropDownList();
                        }
                    }
                }
                break;
            }
        }
        return false;
    }

    protected boolean isHandleTextChangeBySet()
    {
        return false;
    }
    
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        if(popup != null && popup.isVisible())
        {
            TnUiArgAdapter posYAdapter = tnUiArgs.get(KEY_DROPDOWNLIST_POS_Y);
            TnUiArgAdapter posXAdapter = tnUiArgs.get(KEY_DROPDOWNLIST_POS_X);
            
            int y = 0;
            int x = 0;
            
            if (posYAdapter != null)
            {
                y = posYAdapter.getInt();
            }
            if (posXAdapter != null)
            {
                x = posXAdapter.getInt();
            }
            int width;
            Object listWidthArgAdapter = list.getTnUiArgs().get(TnUiArgs.KEY_PREFER_WIDTH);
            if(listWidthArgAdapter != null)
            {
                width = ((TnUiArgAdapter) listWidthArgAdapter).getInt();
            }
            else
            {
                width = FrogDropDownField.this.getWidth();
            }
            popup.update(FrogDropDownField.this, x, y, width, list.getPreferredHeight());
        }
        
        if(isShowDropDownAfterLayout && w > 0)
        {
            isShowDropDownAfterLayout = false;
            showDropDownList();
        }
    }
    
    /**
     * Show the drop down list.
     */
    public void showDropDownList()
    {
        if(FrogDropDownField.this.getWidth() <= 0)
        {
            isShowDropDownAfterLayout = true;
            return;
        }
        
        ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance())
                .runOnUiThread(new Runnable()
        {
            public void run()
            {
                if (list == null || list.getAdapter().getCount() <= 0)
                {
                    return;
                }
                if (popup == null && list != null)
                {
                    popup = new FrogFloatPopup(0);
                    popup.setContent(list);
                }

                if (popup != null && popup.isVisible())
                {
                    list.requestLayout();

                }
                else if (popup != null)
                {
                    Object yUiArgAdapter = tnUiArgs.get(KEY_DROPDOWNLIST_POS_Y);
                    Object xUiArgAdapter = tnUiArgs.get(KEY_DROPDOWNLIST_POS_X);
                    int y = 0;
                    int x = 0;
                    if (yUiArgAdapter != null)
                    {
                        y = ((TnUiArgAdapter) yUiArgAdapter).getInt();
                    }
                    if (xUiArgAdapter != null)
                    {
                        x = ((TnUiArgAdapter) xUiArgAdapter).getInt();
                    }
                    int width;
                    Object listWidthArgAdapter = list.getTnUiArgs().get(TnUiArgs.KEY_PREFER_WIDTH);
                    if(listWidthArgAdapter != null)
                    {
                        width = ((TnUiArgAdapter) listWidthArgAdapter).getInt();
                    }
                    else
                    {
                        width = FrogDropDownField.this.getWidth();
                    }
                    popup.showAt(FrogDropDownField.this, x, y, width, list
                            .getPreferredHeight(), true);

                }
            }
        });

    }
 
    /**
     * hide dropDownList if isShowing.
     * @return true if the dropdownlist hides, or false. 
     */
    public void hideDropDownList()
    {
        ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance())
                .runOnUiThread(new Runnable()
                {
                    public void run()
                    {
                        if (popup != null && popup.isVisible())
                        {
                            popup.hide();
                        }
                    }
                });
    }
    
    /**
     * Set filter interval
     * @param interval
     */
    public void setFilterInterval(long interval)
    {
        this.interval = interval;
    }
    
    /**
     * Retrieve filter interval
     * @return long
     */
    public long getFilterInterval()
    {
        return this.interval;
    }
    
    /**
     * Get if the drop down list is shown or not.
     * 
     * @return boolean true shown, or false hide.
     */
    public boolean isDropDownListVisible()
    {
        return popup.isVisible();
    }
    
    protected void handleDropItemSelected(AbstractTnComponent selectedItem)
    {
        if (selectedItem instanceof FrogListItem)
        {
            FrogDropDownField.this.setText(((FrogListItem) selectedItem)
                    .getText());
            hideDropDownList();
        }
    }
    
    public boolean setText(String text)
    {
        isTriggerBySet = super.setText(text);
        return isTriggerBySet;
    }
    
    protected void onFocusChanged(boolean gainFocus)
    {
        if(!gainFocus)
        {
            hideDropDownList();
        }
    }
}
