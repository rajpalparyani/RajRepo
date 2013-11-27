/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * CitizenDropDownComboBox.java
 *
 */
package com.telenav.ui.citizen;

import com.telenav.module.AppConfigHelper;
import com.telenav.tnui.core.TnUiArgs;
import com.telenav.tnui.core.TnUiArgs.TnUiArgAdapter;
import com.telenav.tnui.core.TnUiEvent;
import com.telenav.tnui.graphics.AbstractTnFont;
import com.telenav.tnui.graphics.AbstractTnGraphics;
import com.telenav.tnui.graphics.AbstractTnImage;
import com.telenav.tnui.widget.TnLinearContainer;
import com.telenav.ui.frogui.widget.FrogButton;
import com.telenav.ui.frogui.widget.FrogLabel;
import com.telenav.ui.frogui.widget.FrogNullField;

/**
 *@author Albert Ma (byma@telenav.cn)
 *@date 2010-9-14
 */
public class CitizenDropDownComboBox extends TnLinearContainer 
{
    protected String candidateItems[];
    protected int candidateValues[];
    protected int selectedIndex;
    protected FrogLabel titleLabel;
    protected FrogButton iconButton;
    protected FrogNullField seperatorComp;
    protected TnUiArgAdapter listItemFocusArgAdapter;
    protected TnUiArgAdapter listItemBlurArgAdapter;
    protected TnUiArgAdapter uiArgWidthAdapter;
    protected TnUiArgAdapter uiArgHeightAdapter;
    /**
     * Constructor of CitizenDropDownComboBox.
     * @param id
     */
    public CitizenDropDownComboBox(int id)
    {
        super(id, false);
        int min = AppConfigHelper.getMinDisplaySize();
        int left = 12 * min / 480;
        titleLabel = new FrogLabel(id, "");
        iconButton = new FrogButton(id, "");
        seperatorComp = new FrogNullField(id);
        iconButton.setPadding(0, 0, 0, 0);
        titleLabel.setPadding(left, 0, 0, 0);
        titleLabel.setFocusable(true);
        this.add(titleLabel);
        this.add(seperatorComp);
        this.add(iconButton);
        
    }
    
    /**
     * Set titles of the items.
     * @param items  names array of String
     */
    public void setItemTitles(String[] items)
    {
        if (items == null || items.length == 0)
        {
            return;
        }
        if (this.candidateValues != null && this.candidateValues.length > 0
                && this.candidateValues.length != items.length)
        {
            throw new RuntimeException(
                    "The array length of setItems() is not consistent with the setItemsValues()");
        }
        this.candidateItems = items;
        
    }
    
    public void setForegroundColor(int focusColor, int unFocusColor)
    {
        if(titleLabel != null)
        {
            titleLabel.setForegroundColor(focusColor, unFocusColor);
        }
    }
    
    public void setItemValues(int[] values)
    {
        if (values == null || values.length == 0)
        {
            return;
        }
        if (this.candidateItems != null && this.candidateItems.length > 0
                && this.candidateItems.length != values.length)
        {
            throw new RuntimeException(
                    "The array length of setItems() is not consistent with the setItemsValues()");
        }
        this.candidateValues = values;
    }
    
    /**
     * Set Selected index.
     * @param selectedIndex
     */
    public void setSelectedIndex(int selectedIndex)
    {
        this.selectedIndex = selectedIndex;
        titleLabel.setText(this.getSelectedString());
    }
   
    public int getSelectedIndex()
    {
        return this.selectedIndex;
    }
    
    public void setFont(AbstractTnFont font)
    {
        this.titleLabel.setFont(font);
    }
    
    public void setIndicateIcon (AbstractTnImage focusIcon, AbstractTnImage blurIcon)
    {
        this.iconButton.setIcon(focusIcon, blurIcon, AbstractTnGraphics.TOP | AbstractTnGraphics.HCENTER);
    }
    
    public TnUiArgs getTitlePartUiArgs()
    {
        return titleLabel.getTnUiArgs();
    }
    
    public TnUiArgs getIconPartUiArgs()
    {
        return iconButton.getTnUiArgs();
    }
    
    public TnUiArgs getSeperatorLineArgs()
    {
        return seperatorComp.getTnUiArgs();
    }
    
    public void setDropDownListItemBgImage(TnUiArgAdapter listItemFocusArgAdapter,
            TnUiArgAdapter listItemBlurArgAdapter)
    {
        this.listItemFocusArgAdapter = listItemFocusArgAdapter;
        this.listItemBlurArgAdapter = listItemBlurArgAdapter;
    }
    
    /**
     * Retrieve the selected String
     * @return selected item name string
     */
    public String getSelectedString()
    {
       if(selectedIndex < candidateItems.length)
       {
           return candidateItems[selectedIndex];
       }
       else
       {
           return "";
       }
    }

    /**
     * Retrieve the selected value
     * @return selected item value
     */
    public int getSelectedValue()
    {
        if(selectedIndex < candidateValues.length)
        {
            return candidateValues[selectedIndex];
        }
        else
        {
            return -1;
        }
    }
    
    
    public boolean handleUiEvent(TnUiEvent tnUiEvent)
    {
        return false;
    }
    
    public int id2Index(int realId)
    {
        for(int i = 0; i < this.candidateValues.length; i++)
        {
            if(realId == candidateValues[i])
            {
                return i;
            }
        }
        return 0;
    }
}
