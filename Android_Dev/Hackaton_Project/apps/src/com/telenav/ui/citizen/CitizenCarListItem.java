/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * CitizenCategoryListItem.java
 *
 */
package com.telenav.ui.citizen;

import com.telenav.module.AppConfigHelper;
import com.telenav.tnui.graphics.AbstractTnFont;
import com.telenav.tnui.graphics.AbstractTnGraphics;
import com.telenav.tnui.graphics.AbstractTnImage;
import com.telenav.tnui.graphics.TnRect;
import com.telenav.ui.ImageDecorator;
import com.telenav.ui.frogui.widget.FrogListItem;


/**
 *@author jyxu (jyxu@telenav.cn)
 *@date 2012-11-22
 */
public class CitizenCarListItem extends FrogListItem
{
    private AbstractTnImage carImage;
    private static AbstractTnImage selectedRadioImage = ImageDecorator.IMG_RADIO_ICON_FOCUSED.getImage();
    private static AbstractTnImage unselectedRadioImage = ImageDecorator.IMG_RADIO_ICON_UNFOCUSED.getImage();
    private boolean selected;
    private String carName;
   
    private int focusedColor;
    private int unfocusedColor;
    private boolean needRadioImage = true;
    
    private static final int SCALE = 720;
    private static final int CAR_IMAGE_WIDTH = 50;
    private static final int CAR_IMAGE_HEIGHT = 57;
    private static final int CAR_TEXT_GAP = 20;
    private static final int ITEM_PADDING = 16;
    
    private int carImageWidth = 50;
    private int carImageHeight = 57;
    private int carTextGap = 20;
    private int padding = 16;
    
    public CitizenCarListItem(int id)
    {
        super(id);
        selected = false;
    }

    public void setSelected(boolean selected)
    {
        this.selected = selected;
    }
    
    public void setCarImage(AbstractTnImage carImage)
    {
        this.carImage = carImage;
    }
    
    public void setCarName(String carName)
    {
        this.carName = carName;
    }
    
    public void setFont(AbstractTnFont font)
    {
        this.font = font;
    }

    public void setNeedRadioImage(boolean needRadioImage)
    {
        this.needRadioImage = needRadioImage;
    }
    
    public void setForegroundColor(int focusedColor, int unfocusedColor)
    {
        this.focusedColor = focusedColor;
        this.unfocusedColor = unfocusedColor;
    }
    
    public void sublayout(int width, int height)
    {
        int min = AppConfigHelper.getMinDisplaySize();
        carImageWidth = CAR_IMAGE_WIDTH * min/SCALE;
        carImageHeight =  CAR_IMAGE_HEIGHT * min/SCALE; 
        padding = ITEM_PADDING * min/SCALE;
        carTextGap = CAR_TEXT_GAP * min/SCALE;
    }
    
    protected void paint(AbstractTnGraphics graphics)
    {
        graphics.drawImage(carImage, null, new TnRect(padding, (this.getPreferredHeight() - carImageHeight)/2, padding + carImageWidth, (this.getPreferredHeight() + carImageHeight)/2));
        graphics.setColor(this.isFocused ? focusedColor: unfocusedColor);
        graphics.setFont(font);
        int carNameX = padding + carImage.getWidth() + carTextGap;
        graphics.drawString(carName, carNameX, this.getPreferredHeight()/2, AbstractTnGraphics.VCENTER | AbstractTnGraphics.LEFT);            
        if(needRadioImage)
        {
            graphics.drawImage(selected?selectedRadioImage:unselectedRadioImage , this.getPreferredWidth() -  padding, this.getPreferredHeight()/2, AbstractTnGraphics.VCENTER | AbstractTnGraphics.RIGHT); 
        }
    }
}
