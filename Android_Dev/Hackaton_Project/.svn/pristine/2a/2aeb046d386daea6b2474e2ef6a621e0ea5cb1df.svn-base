/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * LockoutViewTouch.java
 *
 */
package com.telenav.module.carconnect.lockout;

import com.telenav.mvc.AbstractCommonUiDecorator;
import com.telenav.mvc.AbstractCommonView;
import com.telenav.res.IStringCarConnect;
import com.telenav.res.ResourceManager;
import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.core.AbstractTnContainer;
import com.telenav.tnui.core.AbstractTnUiHelper;
import com.telenav.tnui.core.TnKeyEvent;
import com.telenav.tnui.core.TnScreen;
import com.telenav.tnui.core.TnUiArgs;
import com.telenav.tnui.core.TnUiEvent;
import com.telenav.tnui.graphics.AbstractTnFont;
import com.telenav.tnui.graphics.AbstractTnGraphics;
import com.telenav.tnui.graphics.AbstractTnImage;
import com.telenav.tnui.graphics.TnNinePatchImage;
import com.telenav.tnui.widget.TnPopupContainer;
import com.telenav.ui.ImageDecorator;
import com.telenav.ui.UiFactory;
import com.telenav.ui.UiStyleManager;
import com.telenav.ui.citizen.CitizenScreen;
import com.telenav.ui.frogui.widget.FrogImageComponent;
import com.telenav.ui.frogui.widget.FrogLabel;
import com.telenav.ui.frogui.widget.FrogNullField;

/**
 *@author chihlh
 *@date Mar 15, 2012
 */
class LockoutViewTouch extends AbstractCommonView implements ILockoutConstants
{

    public LockoutViewTouch(AbstractCommonUiDecorator uiDecorator)
    {
        super(uiDecorator);
    }

    @Override
    protected boolean updateScreen(int state, TnScreen screen)
    {
        return true;
    }

    @Override
    protected int transformCommandDelegate(int state, TnUiEvent tnUiEvent)
    {
        return CMD_NONE;
    }

    @Override
    protected TnPopupContainer createPopup(int state)
    {
        return null;
    }

    @Override
    protected TnScreen createScreen(int state)
    {
        boolean isLandscape = ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_LANDSCAPE;
        
        CitizenScreen screen = UiFactory.getInstance().createScreen(state);
        AbstractTnContainer rootContainer = screen.getRootContainer();
        rootContainer.setMenu(null, AbstractTnComponent.TYPE_MENU);
        rootContainer.setBackgroundColor(
            UiStyleManager.getInstance().getColor(UiStyleManager.LOCKOUT_BACKGROUND_COLOR));
       
        
        AbstractTnContainer contentContainer = screen.getContentContainer();
        
        AbstractTnImage bgImage = null;
        if (isLandscape)
            bgImage = ImageDecorator.LOCKOUT_BG_IMAGE_LANDSCAPE.getImage();
        else
            bgImage = ImageDecorator.LOCKOUT_BG_IMAGE.getImage();
        if (bgImage != null)
        {
            TnNinePatchImage img = new TnNinePatchImage(null,null,null,null,bgImage,null,null,null,null);
            contentContainer.setBackgroundDrawable(img.getDrawable());
        }
        
        FrogNullField nullField = UiFactory.getInstance().createNullField(0);
        TnUiArgs args = nullField.getTnUiArgs();
        args.put(TnUiArgs.KEY_PREFER_WIDTH, uiDecorator.SCREEN_WIDTH);
        args.put(TnUiArgs.KEY_PREFER_HEIGHT, ((LockoutUiDecorator) uiDecorator).LOCKOUT_TITLE_HEIGHT);
        contentContainer.add(nullField);
        
        AbstractTnImage img = ImageDecorator.LOCKOUT_IMAGE.getImage(); 
        FrogImageComponent imgComp = UiFactory.getInstance().createFrogImageComponent(0, img);
        imgComp.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, uiDecorator.SCREEN_WIDTH);
        imgComp.setAnchor(AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);
        contentContainer.add(imgComp);
        
        nullField = UiFactory.getInstance().createNullField(0);
        nullField.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, uiDecorator.SCREEN_WIDTH);
        nullField.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((LockoutUiDecorator) uiDecorator).LOCKOUT_TEXT_GAP);
        contentContainer.add(nullField);
        
        AbstractTnFont textFont = UiStyleManager.getInstance().getFont(UiStyleManager.FONT_LOCKOUT_TEXT);
        FrogLabel label = UiFactory.getInstance().createLabel(0,
            ResourceManager.getInstance().getCurrentBundle().getString(IStringCarConnect.CONNECTED_TO_CAR, IStringCarConnect.FAMILY_CARCONNECT));
        label.setStyle(AbstractTnGraphics.HCENTER);
        label.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, uiDecorator.SCREEN_WIDTH);
        label.setPadding(0, 0, 0, 0);
        label.setFont(textFont);
        int color = UiStyleManager.getInstance().getColor(UiStyleManager.LOCKOUT_TEXT_COLOR);
        label.setForegroundColor(color, color);
        contentContainer.add(label);
        
        nullField = UiFactory.getInstance().createNullField(0);
        nullField.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, uiDecorator.SCREEN_WIDTH);
        nullField.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((LockoutUiDecorator) uiDecorator).LOCKOUT_LOGO_GAP_Y);
        contentContainer.add(nullField);
        
        AbstractTnContainer logoContainer = UiFactory.getInstance().createLinearContainer(0, false);
        logoContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, uiDecorator.SCREEN_WIDTH);
        
        nullField = UiFactory.getInstance().createNullField(0);
        nullField.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((LockoutUiDecorator) uiDecorator).LOCKOUT_LOGO_GAP_X);
        logoContainer.add(nullField);
        
        img = ImageDecorator.LOCKOUT_LOGO.getImage(); 
        imgComp = UiFactory.getInstance().createFrogImageComponent(0, img);
        //imgComp.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, uiDecorator.SCREEN_WIDTH);
        //imgComp.setAnchor(AbstractTnGraphics.LEFT | AbstractTnGraphics.VCENTER);
        logoContainer.add(imgComp);
        
        contentContainer.add(logoContainer);
                
        screen.getContentContainer().setKeyEventListener(this);
        screen.getContentContainer().setTouchEventListener(this);
                
        return screen;
    }

    @Override
    protected boolean updatePopup(int state, TnPopupContainer popup)
    {
        return false;
    }
    
    @Override
    public boolean handleUiEvent(TnUiEvent tnUiEvent)
    {
        if (tnUiEvent.getType() == TnUiEvent.TYPE_KEY_EVENT)
        {
            if (tnUiEvent.getKeyEvent().getCode() == TnKeyEvent.KEYCODE_BACK)
            {
                // block back key
                return true;
            }
        }
        return super.handleUiEvent(tnUiEvent);
    }

}
