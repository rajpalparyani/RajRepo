/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * MaiTaiViewTouch.java
 *
 */
package com.telenav.sdk.maitai.module;

import java.util.Hashtable;

import com.telenav.mvc.AbstractCommonUiDecorator;
import com.telenav.mvc.AbstractCommonView;
import com.telenav.res.IStringCommon;
import com.telenav.res.ResourceManager;
import com.telenav.sdk.maitai.IMaiTaiParameter;
import com.telenav.sdk.maitai.IMaiTaiStatusConstants;
import com.telenav.sdk.maitai.impl.MaiTaiHandler;
import com.telenav.tnui.core.AbstractTnUiHelper;
import com.telenav.tnui.core.TnMenu;
import com.telenav.tnui.core.TnScreen;
import com.telenav.tnui.core.TnUiEvent;
import com.telenav.tnui.widget.TnPopupContainer;
import com.telenav.ui.UiFactory;
import com.telenav.ui.frogui.widget.FrogMessageBox;

/**
 *@author qli
 *@date 2010-12-2
 */
class MaiTaiViewTouch extends AbstractCommonView implements IMaiTaiConstants
{
    boolean isFirstTimeShown = true;
    public MaiTaiViewTouch(AbstractCommonUiDecorator uiDecorator)
    {
        super(uiDecorator);
    }

    protected TnPopupContainer createPopup(int state)
    {
        switch (state)
        {
            case STATE_START_MAITAI:
            {
                return createMaiTaiProgressPopup(state);
            }
            case STATE_START_MAITAI_ERROR:
            {
                showMaiTaiErrorPopup();
                return null;
            }
        }
        return null;
    }

    protected void showMaiTaiErrorPopup()
    {
        if(!isFirstTimeShown)
        {
            return;
        }
        
        isFirstTimeShown = false;
        
        ((AbstractTnUiHelper)AbstractTnUiHelper.getInstance()).runOnUiThread(new Runnable()
        {
            public void run()
            {
                Hashtable result = MaiTaiHandler.getInstance().getResult();
                String errorMessage = "";
                int status = -1;
                if(result != null)
                {
                    String o = (String)result.get(IMaiTaiParameter.KEY_STATUS);
                    if(o != null)
                    {
                        try
                        {
                            status = Integer.valueOf(o);
                        }
                        catch(Exception e)
                        {
                            status = -1;
                        }
                    }
                    
                    errorMessage = (String)result.get(IMaiTaiParameter.KEY_STATUS_MSG);
                }
                
                if(status == IMaiTaiStatusConstants.STATUS_UNAUTHORIZED)
                {
                    if(errorMessage == null || errorMessage.trim().length() == 0)
                    {
                        errorMessage = ResourceManager.getInstance().getCurrentBundle().getString(IStringCommon.RES_NO_INTERNET_CONNECTION, IStringCommon.FAMILY_COMMON);
                    }
                }
                else
                {
                    if(errorMessage == null || errorMessage.trim().length() == 0)
                    {
                        errorMessage = ResourceManager.getInstance().getCurrentBundle().getString(IStringCommon.RES_ERROR_START_MAITAI, IStringCommon.FAMILY_COMMON);
                    }
                }

                TnMenu menu = UiFactory.getInstance().createMenu();
                menu.add(ResourceManager.getInstance().getCurrentBundle().getString(IStringCommon.RES_BTTN_OK,
                    IStringCommon.FAMILY_COMMON), CMD_COMMON_OK);
                FrogMessageBox messageBox = UiFactory.getInstance().createMessageBox(STATE_START_MAITAI_ERROR, errorMessage, menu);
                messageBox.setCommandEventListener(MaiTaiViewTouch.this);
                messageBox.show();
                
            }
        });
    }
    protected TnScreen createScreen(int state)
    {
        switch(state)
        {
            case STATE_INIT:
            {
//                return createMaiTaiScreen(state);
            }
        }
        return null;
        
    }

    protected int transformCommandDelegate(int state, TnUiEvent tnUiEvent)
    {
        int cmd = CMD_NONE;
        
        //TODO:
        
        return cmd;
    }

    protected boolean updatePopup(int state, TnPopupContainer popup)
    {
        return false;
    }

    protected boolean updateScreen(int state, TnScreen screen)
    {
        return false;
    }

    protected TnPopupContainer createMaiTaiProgressPopup(int state)
    {
        return UiFactory.getInstance().createProgressBox(state, "");
    }
}
