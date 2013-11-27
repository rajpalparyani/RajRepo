/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * EntryView.java
 *
 */
package com.telenav.module.entry;

import android.graphics.Color;

import com.telenav.app.CommManager;
import com.telenav.app.TeleNavDelegate;
import com.telenav.data.datatypes.browser.BrowserSessionArgs;
import com.telenav.module.browsersdk.BrowserSdkModel;
import com.telenav.mvc.AbstractCommonMapView;
import com.telenav.mvc.AbstractCommonUiDecorator;
import com.telenav.res.IStringCommon;
import com.telenav.res.IStringEntry;
import com.telenav.res.IStringLogin;
import com.telenav.res.ResourceManager;
import com.telenav.tnui.core.AbstractTnContainer;
import com.telenav.tnui.core.TnMenu;
import com.telenav.tnui.core.TnScreen;
import com.telenav.tnui.core.TnUiEvent;
import com.telenav.tnui.widget.TnPopupContainer;
import com.telenav.ui.UiFactory;
import com.telenav.ui.citizen.CitizenScreen;
import com.telenav.ui.citizen.CitizenWebComponent;

/**
 *@author fqming (fqming@telenav.cn)
 *@date Jul 21, 2010
 */
public class EntryViewTouch extends AbstractCommonMapView implements IEntryConstants
{
    public EntryViewTouch(AbstractCommonUiDecorator uiDecorator)
    {
        super(uiDecorator);
    }

    protected TnPopupContainer createPopup(int state)
    {
        TnPopupContainer popupContainer = null;
        switch (state)
        {
            case STATE_SYNC_SERVICELOCATOR_SDP:
            case STATE_DATASET_SWITCH_RESYNC:
            {
                popupContainer = UiFactory.getInstance().createProgressBox(state,
                    ResourceManager.getInstance().getCurrentBundle().getString(IStringCommon.RES_LABEL_LOADING, IStringCommon.FAMILY_COMMON));
                popupContainer.setTouchEventListener(new SecretKeyListener(this, CMD_SECRET_KEY));
                break;
            }
            case STATE_SYNC_SERVICELOCATOR_SDP_ERROR_MSG:
            {
                TnMenu menu = UiFactory.getInstance().createMenu();
                menu.add(ResourceManager.getInstance().getCurrentBundle().getString(
                    IStringCommon.RES_BTTN_OK, IStringCommon.FAMILY_COMMON), CMD_COMMON_OK);
                
                String str = model.fetchString(KEY_S_ERROR_MESSAGE);
                
                if(str == null || str.trim().length() == 0)
                    str = ResourceManager.getInstance().getCurrentBundle().getString(IStringLogin.RES_LABEL_DIM_ERROR_MSG,
                        IStringLogin.FAMILY_LOGIN);
                
                popupContainer = UiFactory.getInstance().createMessageBox(state, str, menu);
                break;
            }
            case STATE_SYNC_PURCHASE_FAIL_MSG:
            {
                TnMenu menu = UiFactory.getInstance().createMenu();
                menu.add(ResourceManager.getInstance().getCurrentBundle().getString(
                    IStringCommon.RES_BTTN_OK, IStringCommon.FAMILY_COMMON), CMD_COMMON_OK);
                
                String str = model.fetchString(KEY_S_ERROR_MESSAGE);
                popupContainer = UiFactory.getInstance().createMessageBox(state, str, menu);
                break;
            }
            case STATE_GOTO_LOCATION_SETTING:
            {
                TnMenu menu = UiFactory.getInstance().createMenu();
                menu.add(ResourceManager.getInstance().getCurrentBundle().getString(
                    IStringCommon.RES_BTTN_OK, IStringCommon.FAMILY_COMMON), CMD_COMMON_OK);
                
                String str = ResourceManager.getInstance().getCurrentBundle().getString(IStringEntry.RES_LABEL_LOCATION_SETTING, IStringEntry.FAMILY_ENTRY);
                
                popupContainer = UiFactory.getInstance().createMessageBox(state, str, menu);
                break;
            }
            default:
                break;
        }
        
        return popupContainer;
    }
    
    protected TnScreen createScreen(int state)
    {
        switch(state)
        {
            case STATE_UPGRADE_SELECTION:
            {
                return createUpgradeScreen(state);
            }
        }
        return null;
    }

    protected TnScreen createUpgradeScreen(int state)
    {
        TeleNavDelegate.getInstance().setOrientation(TeleNavDelegate.ORIENTATION_PORTRAIT); 
        CitizenScreen upgradeScreen = UiFactory.getInstance().createScreen(state);
        upgradeScreen.getRootContainer().setBackgroundColor(Color.BLACK);
        final AbstractTnContainer container = upgradeScreen.getContentContainer();
        CitizenWebComponent upgradeComponent = UiFactory.getInstance().createCitizenWebComponent(this, 0, null, false);
        BrowserSdkModel listener=(BrowserSdkModel)model.fetch(KEY_O_IS_UPGRADE_LISTENER);        
        upgradeComponent.setHtmlSdkServiceHandler(listener);
        
        BrowserSessionArgs args = new BrowserSessionArgs(CommManager.UPGRADE_URL_DOMAIN_ALIAS);
        String url = args.getUrl();
        url = BrowserSdkModel.addEncodeTnInfo(url, "");       
        String upgradeType = getUpgradeType();
        url = url + "&upgradeType=" + upgradeType;
        //per Zhangpan, we fix the height be the max
        url = BrowserSdkModel.appendWidthHeightToUrl(url);
        upgradeComponent.setBackgroundColor(Color.BLACK);
        upgradeComponent.loadUrl(url);
        container.add(upgradeComponent);
        return upgradeScreen;
    }

    private String getUpgradeType()
    {
        final boolean isForceUpgrade = model.getBool(KEY_B_IS_FORCE_UPGRADE);
        String upgradeType="";
        if(isForceUpgrade)
            upgradeType = "force";
        else
            upgradeType = "optional";
        return upgradeType;
    }

    protected int transformCommandDelegate(int state, TnUiEvent tnUiEvent)
    {
        int cmd = CMD_NONE;
        return cmd;
    }

    protected boolean updatePopup(int state, TnPopupContainer popup)
    {
        return false;
    }

    protected boolean updateScreen(int state, TnScreen screen)
    {
        return true;
    }
}


