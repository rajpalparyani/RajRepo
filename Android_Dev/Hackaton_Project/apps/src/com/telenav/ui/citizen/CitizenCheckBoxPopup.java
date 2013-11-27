/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * CitizenCheckBoxPopup.java
 *
 */
package com.telenav.ui.citizen;

import com.telenav.module.AppConfigHelper;
import com.telenav.tnui.core.TnUiArgs;
import com.telenav.tnui.core.TnUiArgs.ITnUiArgsDecorator;
import com.telenav.tnui.core.TnUiArgs.TnUiArgAdapter;
import com.telenav.tnui.graphics.AbstractTnGraphics;
import com.telenav.tnui.graphics.AbstractTnImage;
import com.telenav.tnui.widget.TnLinearContainer;
import com.telenav.tnui.widget.TnScrollPanel;
import com.telenav.ui.ImageDecorator;
import com.telenav.ui.UiFactory;
import com.telenav.ui.UiStyleManager;
import com.telenav.util.PrimitiveTypeCache;

/**
 *@author Casper(pwang@telenav.cn)
 *@date 2012-2-29
 */
public class CitizenCheckBoxPopup extends CitizenMessageBox
{
    public static final int KEY_CHECK_BOX_MAX_HEIGHT = 505051;
    public static final int KEY_CHECK_BOX_CONTAINER_WIDTH = 505052;
    public static final int KEY_CHECK_ITEM_WIDTH = 505053;
    public static final int KEY_CHECK_ITEM_HEIGHT = 505054;
    private CitizenCheckBox checkBox;
    private TnLinearContainer boxContainer;
    
    public CitizenCheckBoxPopup(int id, String message)
    {
        super(id, message);
    }
    
    protected void setUpContentContainer()
    {
        super.setUpContentContainer();
        TnLinearContainer container = new TnLinearContainer(0, true, AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);//To Make boxConter center align.
        this.topContainer.add(container);
        boxContainer = new TnLinearContainer(0, true, AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);
        this.messageMultiLine.setPadding(0, 0, 0, 0); //initDefaultStyle();
        container.add(boxContainer);
    }
    
    public void addCheckBox(String[] options, boolean isMultipleChoices, int defaultSelectedIndex)
    {
        addCheckBox(options,  isMultipleChoices,  defaultSelectedIndex,  null);
    }
    
    public void addCheckBox(String[] options, boolean isMultipleChoices, int defaultSelectedIndex, boolean[] disables)
    {
        if(options == null)
        {
            return;
        }
        
        final TnUiArgAdapter containerWidthAdapter = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
        {
            public Object decorate(TnUiArgAdapter args)
            {
                if (CitizenCheckBoxPopup.this.getTnUiArgs().get(KEY_CHECK_BOX_CONTAINER_WIDTH) != null)
                {
                    return PrimitiveTypeCache.valueOf(CitizenCheckBoxPopup.this.getTnUiArgs().get(KEY_CHECK_BOX_CONTAINER_WIDTH).getInt());
                }
                else
                {
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getMinDisplaySize() * 75 / 100);
                }
            }
        });
        TnUiArgAdapter checkItemWidthAdapter = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
        {
            public Object decorate(TnUiArgAdapter args)
            {
                if (CitizenCheckBoxPopup.this.getTnUiArgs().get(KEY_CHECK_ITEM_WIDTH) != null)
                {
                    return PrimitiveTypeCache.valueOf(CitizenCheckBoxPopup.this.getTnUiArgs().get(KEY_CHECK_ITEM_WIDTH).getInt());
                }
                else
                {
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getMinDisplaySize() * 75 / 100);
                }
            }
        });
        TnUiArgAdapter buttonWidthAdapter = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
        {
            public Object decorate(TnUiArgAdapter args)
            {
                return PrimitiveTypeCache.valueOf(containerWidthAdapter.getInt() * 3 / 7);
            }
        });
        TnUiArgAdapter checkItemHeightAdapter = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
        {
            public Object decorate(TnUiArgAdapter args)
            {
                if (CitizenCheckBoxPopup.this.getTnUiArgs().get(KEY_CHECK_ITEM_HEIGHT) != null)
                {
                    return PrimitiveTypeCache.valueOf(CitizenCheckBoxPopup.this.getTnUiArgs().get(KEY_CHECK_ITEM_HEIGHT).getInt());
                }
                else
                {
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getMaxDisplaySize() * 830 / 10000);
                }
            }
        });
        if(checkBox == null)
        {
            this.checkBox = new CitizenCheckBox(id, isMultipleChoices);
            TnScrollPanel scrollPanel = new TnScrollPanel(0, true);
            scrollPanel.set(checkBox);
            this.boxContainer.add(scrollPanel);
            this.boxContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, containerWidthAdapter);
            this.checkBox.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, containerWidthAdapter);
            this.getTnUiArgs().put(CitizenMessageBox.KEY_MESSAGE_BOX_BUTTON_WIDTH, buttonWidthAdapter);
            this.boxContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
            {
                public Object decorate(TnUiArgAdapter args)
                {
                    int height = 0;
                    if(CitizenCheckBoxPopup.this.checkBox.getChildrenSize() > 0)
                    {
                        height = CitizenCheckBoxPopup.this.checkBox.get(0).getTnUiArgs().get(TnUiArgs.KEY_PREFER_HEIGHT).getInt() * CitizenCheckBoxPopup.this.checkBox.getChildrenSize();
                    }
                    if (CitizenCheckBoxPopup.this.getTnUiArgs().get(KEY_CHECK_BOX_MAX_HEIGHT) != null)
                    {
                        if(height > CitizenCheckBoxPopup.this.getTnUiArgs().get(KEY_CHECK_BOX_MAX_HEIGHT).getInt())
                        {
                            height = CitizenCheckBoxPopup.this.getTnUiArgs().get(KEY_CHECK_BOX_MAX_HEIGHT).getInt();
                        }
                    }
                    return PrimitiveTypeCache.valueOf(height);
                }
            }));
            
            topContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, 
                new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
                {
                    public Object decorate(TnUiArgAdapter args)
                    {
                        int height = 0;
                        if(getTnUiArgs().get(KEY_MESSAGE_BOX_TOP_HEIGHT) != null)
                        {
                            height += getTnUiArgs().get(KEY_MESSAGE_BOX_TOP_HEIGHT).getInt();
                        }
                        height += CitizenCheckBoxPopup.this.boxContainer.getTnUiArgs().get(TnUiArgs.KEY_PREFER_HEIGHT).getInt();
                        return PrimitiveTypeCache.valueOf(height);
                    }
                }));
        }
        int count = options.length;
        
        AbstractTnImage checkedIcon = isMultipleChoices ?  ImageDecorator.IMG_CHECK_BOX_ON.getImage():ImageDecorator.IMG_RADIO_ICON_FOCUSED.getImage(); 
        AbstractTnImage uncheckedIcon = isMultipleChoices ?  ImageDecorator.IMG_CHECK_BOX_OFF.getImage():ImageDecorator.IMG_RADIO_ICON_UNFOCUSED.getImage(); 
        
        CitizenCheckItem[] choices = new CitizenCheckItem[count];
        for(int i = 0; i < count ; i++)
        {
            CitizenCheckItem checkbox = UiFactory.getInstance().createCitizenCheckItem(i, options[i], checkedIcon, uncheckedIcon);
            checkbox.setFocusable(true);
            checkbox.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_CHECK_BOX));
            checkbox.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, checkItemWidthAdapter);
            checkbox.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, checkItemHeightAdapter);
            checkbox.setForegroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_DA_GR), UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_DA_GR));
            checkbox.setStyle(AbstractTnGraphics.RIGHT);
            checkbox.setUnCheckableIcon(ImageDecorator.IMG_CHECK_BOX_DISABLE.getImage());
            choices[i] = checkbox;
            if(disables != null && i < disables.length)
            {
                choices[i].setUnCheckableIcon(ImageDecorator.IMG_RADIO_ICON_UNFOCUSED.getImage());
                choices[i].setEnabled(!disables[i]);
            }
        }
        
        this.checkBox.setItems(choices, true);
        if(!isMultipleChoices)
        {
            this.checkBox.setSelectedIndex(defaultSelectedIndex);
        }

    }
    
    public void setMessage(String message)
    {
        super.setMessage(message);
        if(message != null && message.length() > 0)
        {          
            this.messageMultiLine.setPadding(2, 2, 2, 2); //initDefaultStyle();
        }
    }
    
    public CitizenCheckBox getCheckBox()
    {
        return this.checkBox;
    }
    
    public int getSelectedIndex()
    {
        //Fix bug: http://jira.telenav.com:8080/browse/TNANDROID-1651
        return this.checkBox != null? this.checkBox.getSelectedIndex() : -1;
    }
    
    public void setSelectedIndex(int index)
    {
        this.checkBox.setSelectedIndex(index);
    }

}
