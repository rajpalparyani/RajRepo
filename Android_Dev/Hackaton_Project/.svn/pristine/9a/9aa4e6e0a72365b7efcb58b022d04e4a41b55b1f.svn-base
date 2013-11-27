/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 *
 */
package com.telenav.module.about;

import com.telenav.mvc.AbstractCommonController;
import com.telenav.mvc.AbstractController;
import com.telenav.mvc.AbstractModel;
import com.telenav.mvc.AbstractView;
import com.telenav.mvc.StateMachine;
/**
 *@author dfqin (dfqin@telenav.cn)
 *@date 2011-1-20
 */
public class AboutController extends AbstractCommonController implements IAboutConstants
{
	private final static int[][] STATE_TABLE = new int[][]
    {
		{STATE_VIRGIN, EVENT_CONTROLLER_START, STATE_ROOT, ACTION_ABOUT_INIT },
		
        { STATE_VIRGIN, EVENT_CONTROLLER_GOTO_ABOUT, STATE_ABOUT_PAGE, ACTION_NONE },
        { STATE_VIRGIN, EVENT_CONTROLLER_GOTO_SUPPORT, STATE_SUPPORT_INFO, ACTION_NONE },
        { STATE_VIRGIN, EVENT_CONTROLLER_GOTO_TOS, STATE_TOS, ACTION_NONE },
        
		{STATE_ROOT, CMD_MENU_DIAGNOSTIC, STATE_INFO_COLLECTING, ACTION_GETINFO},
		{STATE_INFO_COLLECTING, EVENT_MODEL_GOTO_TOOLS ,STATE_TOOLS, ACTION_NONE},
		{STATE_ROOT, CMD_MENU_ABOUT_ATT, STATE_ABOUT_PAGE, ACTION_NONE},
		{STATE_ROOT, CMD_MENU_SUPPORT_INFO, STATE_SUPPORT_INFO, ACTION_NONE},
		
		{STATE_TOOLS, CMD_MENU_REFRESH, STATE_TOOLS_REFRESH, ACTION_REFRESH},
		{STATE_TOOLS_REFRESH, EVENT_MODEL_GOTO_TOOLS, STATE_TOOLS, ACTION_NONE},
		
		{ STATE_ROOT, CMD_MENU_PIN_NUM, STATE_GETTING_PIN, ACTION_FORGET_PIN},
        { STATE_GETTING_PIN, EVENT_MODEL_FORGET_PIN, STATE_PIN_GOT, ACTION_NONE},
        { STATE_PIN_GOT, CMD_COMMON_OK, STATE_PREV, ACTION_NONE},
	};
	
	public AboutController(AbstractController superController)
	{
		super(superController);
	}
	
	protected void postStateChangeDelegate(int currentState, int nextState)
	{

	}
	
	protected AbstractView createView()
	{
		AboutUiDecorator uiDecorator = new AboutUiDecorator();
		return new AboutViewTouch(uiDecorator);
	}
	
	protected AbstractModel createModel()
	{
		return new AboutModel();
	}
	
	protected StateMachine createStateMachine()
	{
		return new StateMachine(STATE_TABLE_COMMON, STATE_TABLE);
	}
}
