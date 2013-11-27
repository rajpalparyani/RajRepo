/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * CitizenCheckBox.java
 *
 */
package com.telenav.ui.citizen;

import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.core.ITnUiEventListener;
import com.telenav.tnui.core.TnUiArgs;
import com.telenav.tnui.core.TnUiEvent;
import com.telenav.tnui.graphics.AbstractTnGraphics;
import com.telenav.tnui.graphics.AbstractTnImage;
import com.telenav.tnui.widget.TnLinearContainer;
import com.telenav.ui.NinePatchImageDecorator;
import com.telenav.ui.UiFactory;
import com.telenav.ui.frogui.widget.FrogLabel;

/**
 *@author Casper(pwang@telenav.cn)
 *@date 2012-3-7
 */
public class CitizenCheckBox extends TnLinearContainer implements ITnUiEventListener
{
    private ITnUiEventListener itemCommandListener = null;
    protected boolean isMultiSelect;
    protected CitizenCheckItem[] boxes;
    protected int selectedIndex = 0;

    /**
     * Constructor, constructs a MultiCheckBox from a array of CitizenCheckBox
     * 
     * @param id
     * @param isMultiSelect
     */
    public CitizenCheckBox(int id, boolean isMultiSelect)
    {
        super(id, true, AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);
        this.isMultiSelect = isMultiSelect;
    }
    
    public void setItemEnabled(int index, boolean enabled)
    {
        if(boxes != null && index >= 0 && index < boxes.length)
        {
            boxes[index].setEnabled(enabled);
        }
    }
    
    public int getItemsCount()
    {
        if(boxes != null)
        {
            return boxes.length;
        }
        else
        {
            return 0;
        }
    }
    
    
    public boolean getIsMultiSelect()
    {
        return this.isMultiSelect;
    }
    
    public void setItems(CitizenCheckItem[] boxes, boolean needBackgroudImage)
    {
        if (boxes != null)
        {
            this.boxes = boxes;
            for (int i = 0; i < boxes.length; i++)
            {
                if(boxes[i].getCommandEventListener() != null)
                {
                    itemCommandListener = boxes[i].getCommandEventListener();
                }
                boxes[i].setCommandEventListener(this);
            }
            layoutItems(needBackgroudImage);
        }
    }
    
    public synchronized void layoutItems(boolean needBackgroudImage)
    {
        if (boxes == null || boxes.length == 0)
        {
            return;
        }

        int maxOneRowNumber = 1;

        if (this.getTnUiArgs().get(TnUiArgs.KEY_PREFER_WIDTH) != null  && boxes[0].getTnUiArgs().get(tnUiArgs.KEY_PREFER_WIDTH) != null)
        {
            maxOneRowNumber = this.getTnUiArgs().get(TnUiArgs.KEY_PREFER_WIDTH).getInt()   / boxes[0].getTnUiArgs().get(tnUiArgs.KEY_PREFER_WIDTH).getInt();
        }
        if(maxOneRowNumber < 1)
        {
            maxOneRowNumber = 1;
        }

        this.removeAll();
        TnLinearContainer oneRowContainer = null;

        for (int i = 0; i < boxes.length; i++)
        {
            if (boxes[i].getParent() != null)
            {
                ((TnLinearContainer) boxes[i].getParent()).remove(boxes[i]);
            }
            if(needBackgroudImage)
            {
                updateCheckItemStatus(boxes[i], i % maxOneRowNumber, i / maxOneRowNumber, maxOneRowNumber, (boxes.length  +  maxOneRowNumber  - 1)/ maxOneRowNumber);
            }
            if (maxOneRowNumber == 1)
            {
                this.add(boxes[i]);
            }
            else
            {
                if (boxes[i].getParent() != null)
                {
                    ((TnLinearContainer) boxes[i].getParent()).remove(boxes[i]);
                }
                if (i % maxOneRowNumber == 0)
                {
                    if (oneRowContainer != null)
                    {
                        this.add(oneRowContainer);
                    }
                    oneRowContainer = UiFactory.getInstance().createLinearContainer(0, false, AbstractTnGraphics.LEFT | AbstractTnGraphics.HCENTER);
                    oneRowContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, this.getTnUiArgs().get(tnUiArgs.KEY_PREFER_WIDTH));
                    oneRowContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, this.boxes[i].getTnUiArgs().get(tnUiArgs.KEY_PREFER_HEIGHT));
                }
                oneRowContainer.add(boxes[i]);
            }
        }
        if (oneRowContainer != null && oneRowContainer.getChildrenSize() > 0 && oneRowContainer.getChildrenSize() < maxOneRowNumber)
        {
            int count = oneRowContainer.getChildrenSize();
            for(int i = 0; i < maxOneRowNumber - count; i++)
            {
                FrogLabel label = new FrogLabel(i, "");
                label.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, this.boxes[i].getTnUiArgs().get(tnUiArgs.KEY_PREFER_WIDTH));
                label.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, this.boxes[i].getTnUiArgs().get(tnUiArgs.KEY_PREFER_HEIGHT));
                if(needBackgroudImage)
                {
                    updateCheckItemStatus(label, count + i, (boxes.length  +  maxOneRowNumber  - 1)/ maxOneRowNumber - 1, maxOneRowNumber, (boxes.length  +  maxOneRowNumber  - 1)/ maxOneRowNumber);
                }
                oneRowContainer.add(label);
            }
            this.add(oneRowContainer);
        }
    }
    
    
    protected void updateCheckItemStatus(AbstractTnComponent box, int xIndex, int yIndex, int maxOneRowNumber, int rowCount)
    {
        if((yIndex == 0 || yIndex == rowCount - 1) && (xIndex == 0 || xIndex == maxOneRowNumber - 1))
        {
            if(yIndex == 0)
            {
                if(maxOneRowNumber == 1)
                {
                    box.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, NinePatchImageDecorator.COMBO_BOX_BG_UNFOCUSED_TOP);
                    box.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.COMBO_BOX_BG_FOCUSED_TOP);
                }
                else if(xIndex == 0)
                {
                    box.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, NinePatchImageDecorator.COMBO_BOX_BG_LEFT_TOP_UNFOCUSED);
                    box.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.COMBO_BOX_BG_LEFT_TOP_FOCUSED);
                }
                else if(xIndex == maxOneRowNumber - 1)
                {
                    box.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, NinePatchImageDecorator.COMBO_BOX_BG_RIGHT_TOP_UNFOCUSED);
                    box.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.COMBO_BOX_BG_RIGHT_TOP_FOCUSED);
                }
            }
            else if(yIndex == rowCount - 1)
            {
                if(maxOneRowNumber == 1)
                {
                    box.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, NinePatchImageDecorator.COMBO_BOX_BG_UNFOCUSED_BOTTOM);
                    box.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.COMBO_BOX_BG_FOCUSED_BOTTOM);
                }
                else if(xIndex == 0)
                {
                    box.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, NinePatchImageDecorator.COMBO_BOX_BG_LEFT_BOTTOM_UNFOCUSED);
                    box.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.COMBO_BOX_BG_RIGHT_BOTTOM_FOCUSED);
                }
                else if(xIndex == rowCount - 1)
                {
                    box.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, NinePatchImageDecorator.COMBO_BOX_BG_RIGHT_BOTTOM_UNFOCUSED);
                    box.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.COMBO_BOX_BG_RIGHT_BOTTOM_FOCUSED);
                }
            }
        }
        else
        {
            box.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, NinePatchImageDecorator.COMBO_BOX_BG_UNFOCUSED_MIDDLE);
            box.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.COMBO_BOX_BG_FOCUSED_MIDDLE);
        }
    }
    
    
    public boolean handleUiEvent(TnUiEvent tnUiEvent)
    {
        boolean isHandled = false;
        if (tnUiEvent.getType() == TnUiEvent.TYPE_COMMAND_EVENT && tnUiEvent.getCommandEvent() != null)
        {
            if(tnUiEvent.getCommandEvent().getCommand() == CitizenCheckItem.CMD_ID_ITEM_CHECKED)
            {
                AbstractTnComponent selectedItem = tnUiEvent.getComponent();
                if (itemCommandListener != null)
                {
                    isHandled = itemCommandListener.handleUiEvent(tnUiEvent);
                }
                
                if (selectedItem instanceof CitizenCheckItem)
                {
                    int index = -1;
                    for(int i = 0; i < boxes.length; i++)
                    {
                        if(boxes[i]  == selectedItem)
                        {
                            index = i;
                            break;
                        }
                    }
                    if(index != -1 && !isMultiSelect)
                    {
                        if (index >= 0 && index < boxes.length)
                        {
                            boxes[index].setSelected(true);
                            if (index != selectedIndex)
                            {
                                boxes[selectedIndex].setSelected(false);
                            }
                            selectedIndex = index;
                        }
                    }                  
                }
            }
        }
        return isHandled;
    }

    
    public void setSelectedIndex(int index)
    {
        if(isMultiSelect)
        {
            if (index >= 0 && index < boxes.length)
            {
                boolean isSelected = boxes[index].isSelected();
                boxes[index].setSelected(!isSelected);
            }
        }
        else
        {
            if(index >= 0 && index < boxes.length)
            {
                boxes[index].setSelected(true);
                if(index != selectedIndex)
                {
                    boxes[selectedIndex].setSelected(false);
                }
                selectedIndex = index;
            }
        }
        this.requestLayout();
        this.requestPaint();
    }
    
    
    /**
     * get the status of the items of the component
     * 
     * @return a array to show which items are selected. In the array, "1" means the selected status, "0" not selected.
     */
    public int getSelectedIndex()
    {
        return selectedIndex;
    }
    
    
    public boolean[] getItemStatus()
    {
        if (boxes != null)
        {
            boolean[] status = new boolean[boxes.length];
            for (int i = 0; i < boxes.length; i++)
            {
                CitizenCheckItem checkBox = boxes[i];
                if (checkBox.isSelected())
                    status[i] = true;
                else
                    status[i] = false;
            }
            return status;
        }
        return null;
    }
    
    
    /**
     * set the itemIcon of the item of the the component
     * 
     * @param itemIconOn
     * @param itemIconOff
     */
    public void setItemIcon(AbstractTnImage itemIconOn, AbstractTnImage itemIconOff)
    {
        if (boxes != null)
        {
            for (int i = 0; i < boxes.length; i++)
            {
                boxes[i].setItemIcon(itemIconOn, itemIconOff);
            }
            requestLayout();
            requestPaint();
        }
    }

    

}
