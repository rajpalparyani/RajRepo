/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * FrogComBoxComponent.java
 *
 */
package com.telenav.ui.frogui.widget;

import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.core.ITnUiEventListener;
import com.telenav.tnui.core.TnMenu;
import com.telenav.tnui.core.TnUiArgs;
import com.telenav.tnui.core.TnUiEvent;
import com.telenav.tnui.graphics.AbstractTnFont;
import com.telenav.tnui.graphics.AbstractTnGraphics;
import com.telenav.tnui.graphics.TnColor;
import com.telenav.tnui.widget.TnLinearContainer;
import com.telenav.tnui.widget.TnPopupContainer;
import com.telenav.ui.frogui.text.FrogTextHelper;

/**
 * It requires caller to use {@link FrogAdapter} to create combo box selecting list.
 * <br>e.g.</br>
 * FrogComboBox comboBox = new FrogComboBox(0, "<bold>Color:</bold>");
 * comboBox.setAdapter(new FrogAdapter()
 * {
 *      ....
 * }
 * );
 * @author jwchen (jwchen@telenav.cn)
 * @date 2010-7-26
 */
public class FrogComboBox extends FrogLabel
{
    public final static int COMMOND_COMBO_LIST = 1000;
    
    public final static int COMBOBOX_STYLE_LEFT     = 1;
    public final static int COMBOBOX_STYLE_RIGHT    = 2;
    /**
     * the selectedIndex is the selected value
     */
    protected int selectedIndex = 0;
    protected TnUiArgs itemArgs;
    protected TnPopupContainer popup;
    protected FrogAdapter comboBoxAdapter;
    
    private int valueStrUnfocusedColor = TnColor.BLACK;
    private int valueStrFocusedColor = TnColor.BLACK;
    private int styleLayout = COMBOBOX_STYLE_LEFT;
    private AbstractTnFont valueStrFont = null;
    
    /**
     * Constructor of FrogComboBox
     * @param id
     * @param text comboBox title text.
     */
    public FrogComboBox(int id, String text)
    {
        super(id, text);
        
        this.setFocusable(true);
        
        valueStrFont = font;
    }
    
    /**
     * Set {@link FrogAdapter}
     * 
     * @param adapter it is responsible for the creating {@link FrogListItem} or its subclass.
     */
    public void setAdapter(FrogAdapter adapter)
    {
        this.comboBoxAdapter = adapter;
    }
    
    public FrogAdapter getAdapter()
    {
        return this.comboBoxAdapter;
    }
    
    public void setSelectedColor(int focus, int unfocus)
    {
        valueStrFocusedColor = focus;
        valueStrUnfocusedColor = unfocus;
    }
    
    public int getSelectedColor(boolean isFocus)
    {
        return isFocus ? this.valueStrFocusedColor : this.valueStrUnfocusedColor;
    }
    
    public void setSelectedFont(AbstractTnFont font)
    {
        this.valueStrFont = font;
    }
    
    public AbstractTnFont getSelectedFont()
    {
        return this.valueStrFont;
    }
    
    protected void paint(AbstractTnGraphics graphics)
    {
        graphics.setColor(getForegroundColor(isFocused()));
        int labelTextY = 0;
        graphics.setFont(font);
        FrogListItem selectedItem = this.getListItem(selectedIndex);
        if (selectedItem != null)
        {
            switch(styleLayout)
            {
                case COMBOBOX_STYLE_RIGHT:
                {
                    graphics.drawString(selectedItem.getText(), this.getWidth() - rightPadding, (this.getHeight() - font.getHeight()) / 2, AbstractTnGraphics.RIGHT
                        | AbstractTnGraphics.TOP);
                    labelTextY = (this.getHeight() - boldFont.getHeight()) / 2;
                    break;
                }
                case COMBOBOX_STYLE_LEFT:
                {
                    graphics.setColor(getSelectedColor(isFocused()));
                    graphics.setFont(valueStrFont);
                    labelTextY = (this.getPreferredHeight() - font.getHeight() - graphics.getFont().getHeight()) / 5;
                    graphics.drawString(selectedItem.getText(), leftPadding, labelTextY*3+font.getHeight(), AbstractTnGraphics.LEFT
                        | AbstractTnGraphics.TOP);
                    labelTextY = labelTextY * 2;
                    graphics.setColor(getForegroundColor(isFocused()));
                    break;
                }
            }
        }
        graphics.setFont(boldFont);
        FrogTextHelper.paint(graphics, leftPadding, labelTextY, textLine, font, boldFont, -1, false);
    }

    protected boolean handleUiEvent(TnUiEvent tnUiEvent)
    {
        switch (tnUiEvent.getType())
        {
            case TnUiEvent.TYPE_COMMAND_EVENT:
            {
                showDialog();
                return true;
            }
        }
        return false;
    }

    /**
     * set current select index.
     * @param index
     */
    public void setSelectedIndex(int index)
    {
        this.selectedIndex = index;
        if(comboBoxAdapter != null)
        {
            this.requestPaint();
        }
    }

    /**
     * get select index.
     * 
     * @return index
     */
    public int getSelectedIndex()
    {
        return this.selectedIndex;
    }

    protected void showDialog()
    {
        if (popup == null)
        {
            popup = new TnPopupContainer(0);
            TnLinearContainer popupContainer = new TnLinearContainer(0, true);
            popup.setContent(popupContainer);
            
            final FrogList list = new FrogList(0);
            list.setCommandEventListener(new ITnUiEventListener()
            {
                public boolean handleUiEvent(TnUiEvent tnUiEvent)
                {
                    if (tnUiEvent.getCommandEvent().getCommand() == COMMOND_COMBO_LIST && tnUiEvent.getComponent() instanceof FrogListItem
                            && popup != null)
                    {
                        popup.hide();
                        if(tnUiEvent.getComponent() != null 
                                && tnUiEvent.getComponent() instanceof FrogListItem)
                        {
                            int selectedIndex = tnUiEvent.getComponent().getId();
                            FrogComboBox.this.setSelectedIndex(selectedIndex);
                            tnUiEvent.setComponent(FrogComboBox.this);
                            if(FrogComboBox.this.commandListener != null)
                            {
                                FrogComboBox.this.commandListener.handleUiEvent(tnUiEvent);
                            }
                            return true;
                        } 
						
                    }
                    return false;
                }
            });

           list.setAdapter(new FrogAdapter()
            {
                
                public int getItemType(int position)
                {
                    return 0;
                }
                
                public int getCount()
                {
                    return comboBoxAdapter.getCount();
                }
                
                public AbstractTnComponent getComponent(int position, AbstractTnComponent convertComponent, AbstractTnComponent parent)
                {
                    AbstractTnComponent component = comboBoxAdapter.getComponent(position, convertComponent, parent);
                    component.setId(position);
                    TnMenu menu = new TnMenu();
                    menu.add("", COMMOND_COMBO_LIST);
                    component.setMenu(menu, TYPE_CLICK);
                    return component;
                }
            });

            if (selectedIndex >= 0 && selectedIndex < this.comboBoxAdapter.getCount())
            {
                list.setSelectedIndex(selectedIndex);
            }

            popupContainer.add(list);
        }
        popup.show();
    }

  
    protected FrogListItem getListItem(int index)
    {
        FrogListItem comboBoxItem = null;
        if (comboBoxAdapter != null)
        {
            Object listItemObj = this.comboBoxAdapter.getComponent(index,
                null, popup);
            if (listItemObj instanceof FrogListItem)
            {
                comboBoxItem = (FrogListItem) listItemObj;
            }
            else
            {
                throw new RuntimeException(
                        "FrogAdapter.getComponent should create class of FrogListItem or its subclass.");
            }
        }
        else
        {
            throw new RuntimeException("Should set comboBoxAdapter first");
        }
        return comboBoxItem;
    }
    
    public void sublayout(int width, int height)
    {
        int lineWidth = FrogTextHelper.getWidth(this.textLine, font, boldFont);
        preferWidth =  (lineWidth << 1) + leftPadding + rightPadding;
        preferHeight = font.getHeight() + font.getHeight() / 2 + topPadding + bottomPadding;
    }
}
