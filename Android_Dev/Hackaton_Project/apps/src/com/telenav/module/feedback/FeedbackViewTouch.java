/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * ShareView.java
 *
 */
package com.telenav.module.feedback;

import com.telenav.mvc.AbstractCommonUiDecorator;
import com.telenav.mvc.AbstractCommonView;
import com.telenav.mvc.ICommonConstants;
import com.telenav.res.IStringCommon;
import com.telenav.res.IStringFeedback;
import com.telenav.res.ResourceManager;
import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.core.AbstractTnUiHelper;
import com.telenav.tnui.core.ITnTextChangeListener;
import com.telenav.tnui.core.TnMenu;
import com.telenav.tnui.core.TnScreen;
import com.telenav.tnui.core.TnUiArgs;
import com.telenav.tnui.core.TnUiEvent;
import com.telenav.tnui.graphics.AbstractTnGraphics;
import com.telenav.tnui.widget.TnLinearContainer;
import com.telenav.tnui.widget.TnPopupContainer;
import com.telenav.ui.NinePatchImageDecorator;
import com.telenav.ui.UiFactory;
import com.telenav.ui.UiStyleManager;
import com.telenav.ui.citizen.CitizenScreen;
import com.telenav.ui.frogui.widget.FrogButton;
import com.telenav.ui.frogui.widget.FrogLabel;
import com.telenav.ui.frogui.widget.FrogNullField;
import com.telenav.ui.frogui.widget.FrogTextArea;

/**
 * @author wzhu (wzhu@telenav.cn)
 * @date 2010-10-20
 */
class FeedbackViewTouch extends AbstractCommonView implements IFeedbackConstants
{

    protected boolean prepareModelData(int state, int commandId)
    {
        switch (state)
        {
            case STATE_MAIN:
            {
                CitizenScreen currentScreen = (CitizenScreen) ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getCurrentScreen();
                FrogTextArea textArea = null;
                if (currentScreen != null)
                {
                    textArea = (FrogTextArea) currentScreen.getComponentById(ID_FEEDBACK_TEXTAREA);
                    model.put(KEY_S_FEEDBACK, textArea.getText());
                }
                return true;
            }
        }
        return super.prepareModelData(state, commandId);
    }

    public FeedbackViewTouch(AbstractCommonUiDecorator uiDecorator)
    {
        super(uiDecorator);
    }

    protected int transformCommandDelegate(int state, TnUiEvent tnUiEvent)
    {
        int cmd = CMD_NONE;
        return cmd;
    }

    protected TnPopupContainer createPopup(int state)
    {
        switch (state)
        {
            case STATE_SUBMIT_FEEDBACK:
            {
                return createSubmitPopup(state);
            }
        }
        return null;
    }

    private TnPopupContainer createSubmitPopup(int state)
    {
        TnMenu menu = UiFactory.getInstance().createMenu();
        menu.add(ResourceManager.getInstance().getCurrentBundle().getString(IStringCommon.RES_BTTN_OK, IStringCommon.FAMILY_COMMON), CMD_COMMON_OK);
        return UiFactory.getInstance().createMessageBox(
            state,
            ResourceManager.getInstance().getCurrentBundle()
                    .getString(IStringFeedback.RES_MESSAGE_BOX_SUCCESS, IStringFeedback.FAMILY_FEEDBACK), menu);
    }

    protected boolean updatePopup(int state, TnPopupContainer popup)
    {
        return false;
    }

    protected TnScreen createScreen(int state)
    {
        switch (state)
        {
            case STATE_MAIN:
            {
                return createFeedbackMainScreen(state);
            }
        }
        return null;
    }

    private TnScreen createFeedbackMainScreen(int state)
    {
        CitizenScreen screen = UiFactory.getInstance().createScreen(state);
//        TnMenu screenMenu = screen.getRootContainer().getMenu(
//            AbstractTnComponent.TYPE_MENU);        
//        screenMenu.remove(ICommonConstants.CMD_COMMON_FEEDBACK);
        TnLinearContainer container = UiFactory.getInstance().createLinearContainer(0, true,
            AbstractTnGraphics.VCENTER | AbstractTnGraphics.HCENTER);
        FrogLabel title = UiFactory.getInstance().createLabel(0,
            ResourceManager.getInstance().getCurrentBundle().getString(IStringFeedback.RES_LABEL_TITLE, IStringFeedback.FAMILY_FEEDBACK));
        title.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, uiDecorator.SCREEN_WIDTH);
        title.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((FeedbackUiDecorator) uiDecorator).TITLE_HEIGHT);
        title.setBackgroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_ME_GR));
        title.setForegroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_WH), UiStyleManager.getInstance()
                .getColor(UiStyleManager.TEXT_COLOR_WH));
        title.setBoldFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_SCREEN_TITLE));
        title.setStyle(AbstractTnGraphics.HCENTER);
        container.add(title);

        FrogNullField titleNullField = UiFactory.getInstance().createNullField(0);
        titleNullField.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((FeedbackUiDecorator) uiDecorator).TITLE_NULLFIELD_HEIGHT);
        container.add(titleNullField);

        FrogTextArea textArea = UiFactory.getInstance()
                .createTextArea(
                    ID_FEEDBACK_TEXTAREA,
                    "",
                    ResourceManager.getInstance().getCurrentBundle()
                            .getString(IStringFeedback.RES_TEXTFIELD_HINT, IStringFeedback.FAMILY_FEEDBACK), 5);
        textArea.setHintTextColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_LI_GR));
        textArea.setBackgroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_WH));
        textArea.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((FeedbackUiDecorator) uiDecorator).TEXTAREA_WIDTH);
        textArea.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((FeedbackUiDecorator) uiDecorator).TEXTAREA_HEIGHT);
        textArea.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.SEARCH_BOX_INPUT_BG_FOCUSED);
        textArea.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, NinePatchImageDecorator.SEARCH_BOX_INPUT_BG_UNFOCUS);
        textArea.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_TEXT_FIELD));
        textArea.setPadding(10, 5, 10, 5);
        container.add(textArea);

        TnLinearContainer buttonContainer = UiFactory.getInstance().createLinearContainer(0, true, AbstractTnGraphics.HCENTER);
        buttonContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((FeedbackUiDecorator) uiDecorator).BOTTOM_CONTAINER_HEIGHT);
        FrogNullField nullField = UiFactory.getInstance().createNullField(0);
        nullField.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, uiDecorator.SCREEN_WIDTH);
        nullField.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((FeedbackUiDecorator) uiDecorator).NULLFIELD_HEIGHT);

        final String strSubmit = ResourceManager.getInstance().getCurrentBundle().getString(IStringFeedback.RES_BUTTON_SUBMIT, IStringFeedback.FAMILY_FEEDBACK);
        final FrogButton buttonSubmit = UiFactory.getInstance().createButton(ID_FEEDBACK_SUBMIT_BUTTON, strSubmit);
        buttonSubmit.setEnabled(false);
        buttonSubmit.setForegroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_WH), UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_LI_GR));
        buttonSubmit.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((FeedbackUiDecorator) uiDecorator).SUBMIT_BUTTON_WIDTH);
        // buttonSubmit.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((FeedbackUiDecorator)
        // uiDecorator).SUBMIT_BUTTON_HEIGHT);
        TnMenu menu = UiFactory.getInstance().createMenu();
        menu.add("", CMD_SUMBIT);
        buttonSubmit.setMenu(menu, AbstractTnComponent.TYPE_CLICK);
        buttonSubmit.setCommandEventListener(this);
        buttonContainer.add(nullField);
        buttonContainer.add(buttonSubmit);
        
        textArea.setTextChangeListener(new ITnTextChangeListener()
        {
        	public void onTextChange(AbstractTnComponent component, String text)
        	{
        		CitizenScreen currentScreen = (CitizenScreen) ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getCurrentScreen();
        		TnMenu screenMenu = currentScreen.getRootContainer().getMenu(
                    AbstractTnComponent.TYPE_MENU);         		
                int size = screenMenu.size();
                boolean hasSubmit = false;
                boolean hasExit = false;
                for (int i = 0; i < size; i++)
                {
                    int id = screenMenu.getItem(i).getId();
                    if (id == CMD_SUMBIT)
                    {
                        hasSubmit = true;
                    }                    
                    if (id == ICommonConstants.CMD_COMMON_EXIT)
                    {
                        hasExit = true;
                    }
                }
    			
        		FrogButton doneButton = (FrogButton) currentScreen.getComponentById(ID_FEEDBACK_SUBMIT_BUTTON);
    			if(doneButton != null)
                {
                    if (text != null && text.trim().length() > 0)
                    {
                        if (!hasSubmit)
                        {
                            screenMenu.add(strSubmit, CMD_SUMBIT, 0);
                            screenMenu.remove(ICommonConstants.CMD_COMMON_EXIT);
                        }
                        doneButton.setEnabled(true);
                        doneButton.setForegroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_WH), UiStyleManager
                                .getInstance().getColor(UiStyleManager.TEXT_COLOR_DA_GR));
                    }
                    else
					{
                        if (!hasExit)
                        {
                            screenMenu.add(ResourceManager.getInstance().getCurrentBundle().getString(IStringCommon.RES_BTTN_EXIT,
                                IStringCommon.FAMILY_COMMON), ICommonConstants.CMD_COMMON_EXIT);
                            screenMenu.remove(CMD_SUMBIT);
                        }
						doneButton.setEnabled(false);
						doneButton.setForegroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_WH), UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_LI_GR));
					}
				}
        	}
        });
        textArea.setCommandEventListener(this);
        
        screen.getContentContainer().add(container);
        screen.getContentContainer().add(buttonContainer);
        screen.getContentContainer().setBackgroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_WH));
        return screen;
    }

    protected boolean updateScreen(int state, TnScreen screen)
    {
        return false;
    }

}
