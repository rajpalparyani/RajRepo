/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * SecretKeyViewTouch.java
 *
 */
package com.telenav.module.entry.secretkey;

import java.util.Enumeration;
import java.util.Hashtable;

import com.telenav.app.CommManager;
import com.telenav.app.android.RuntimeStatusLogger;
import com.telenav.comm.Comm;
import com.telenav.comm.Host;
import com.telenav.comm.HostProvider;
import com.telenav.data.dao.misc.CommHostDao;
import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.dao.misc.SecretSettingDao;
import com.telenav.data.dao.misc.SimpleConfigDao;
import com.telenav.data.dao.serverproxy.AbstractDaoManager;
import com.telenav.data.dao.serverproxy.ServiceLocator;
import com.telenav.data.datatypes.browser.BrowserSessionArgs;
import com.telenav.data.datatypes.primitive.StringMap;
import com.telenav.data.serverproxy.IServerProxyConstants;
import com.telenav.location.TnLocation;
import com.telenav.module.location.LocationProvider;
import com.telenav.mvc.AbstractCommonUiDecorator;
import com.telenav.mvc.AbstractCommonView;
import com.telenav.mvc.ICommonConstants;
import com.telenav.persistent.TnPersistentManager;
import com.telenav.sdk.kontagent.KontagentLogger;
import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.core.AbstractTnContainer;
import com.telenav.tnui.core.AbstractTnUiHelper;
import com.telenav.tnui.core.ITnTextChangeListener;
import com.telenav.tnui.core.TnKeyEvent;
import com.telenav.tnui.core.TnMenu;
import com.telenav.tnui.core.TnMotionEvent;
import com.telenav.tnui.core.TnScreen;
import com.telenav.tnui.core.TnUiArgs;
import com.telenav.tnui.core.TnUiArgs.ITnUiArgsDecorator;
import com.telenav.tnui.core.TnUiArgs.TnUiArgAdapter;
import com.telenav.tnui.core.TnUiEvent;
import com.telenav.tnui.graphics.AbstractTnFont;
import com.telenav.tnui.graphics.AbstractTnGraphics;
import com.telenav.tnui.graphics.TnColor;
import com.telenav.tnui.widget.TnLinearContainer;
import com.telenav.tnui.widget.TnPopupContainer;
import com.telenav.tnui.widget.TnScrollPanel;
import com.telenav.ui.ImageDecorator;
import com.telenav.ui.NinePatchImageDecorator;
import com.telenav.ui.UiFactory;
import com.telenav.ui.UiStyleManager;
import com.telenav.ui.citizen.CitizenCheckItem;
import com.telenav.ui.citizen.CitizenProfileListItem;
import com.telenav.ui.citizen.CitizenScreen;
import com.telenav.ui.frogui.text.FrogTextHelper;
import com.telenav.ui.frogui.widget.FrogAdapter;
import com.telenav.ui.frogui.widget.FrogButton;
import com.telenav.ui.frogui.widget.FrogLabel;
import com.telenav.ui.frogui.widget.FrogList;
import com.telenav.ui.frogui.widget.FrogListItem;
import com.telenav.ui.frogui.widget.FrogMultiLine;
import com.telenav.ui.frogui.widget.FrogNullField;
import com.telenav.ui.frogui.widget.FrogProgressBox;
import com.telenav.ui.frogui.widget.FrogTextField;
import com.telenav.util.PrimitiveTypeCache;

/**
 *@author jshchen 
 *@date 2010-8-19
 */
class SecretKeyViewTouch extends AbstractCommonView implements ISecretKeyConstants
{
    private TnScrollPanel urlContainer;
    private FrogTextField textField;
    private FrogTextField speedTextField;
    private CitizenCheckItem checkbox;
    private CitizenCheckItem sdCardCheckBox;
    private CitizenCheckItem runtimeStatusCheckBox;
    private CitizenCheckItem generateKTLogFileCheckBox;
    private CitizenCheckItem usingTestServerCheckBox;
    private CitizenCheckItem showSatelliteDuringNavCheckBox;
    private CitizenCheckItem localMisLogCheckBox;
    
    private AbstractTnFont font;

    
    private static String[] highPriorityActions = {
        IServerProxyConstants.ACT_COMBINATION_SYNC_RESOURCE,
        IServerProxyConstants.ACT_SYNC_SERVICE_LOCATOR,
      };
      
    public SecretKeyViewTouch(AbstractCommonUiDecorator uiDecorator)
    {
        super(uiDecorator);
        font = UiStyleManager.getInstance().getFont(UiStyleManager.FONT_SECRET_KEY_LABEL);
    }
    

    protected TnPopupContainer createPopup(int state)
    {
    	switch(state)
    	{
	    	case STATE_GETTING_LOCATOR:
	    	{
	    		FrogProgressBox progressBox = UiFactory.getInstance().createProgressBox(0, "Getting new Service Locator ... ");
				return progressBox;
	    	}
	    	case STATE_GETTING_PRELOAD_RES:
	    	{
	    	    FrogProgressBox progressBox = UiFactory.getInstance().createProgressBox(0, "Fetching preload res ... ");
	    	    return progressBox;
	    	}
    		
    	}
        return null;
    }

    protected TnScreen createScreen(int state)
    {
        TnScreen screen = null;
        switch(state)
        {
            case STATE_INIT:
            {
                screen = createMainListScreen(state);
                break;
            }
            case STATE_URL_GROUP:
            {
                screen= createUrlGroupScreen(state);
                break;
            }
            case STATE_URL_SETTING:
            {
                screen= createChangeHostScreen(state);
                break;
            }
            case STATE_SET_ALIAS_URL:
            {
                screen= createSetAliasUrlScreen(state);
                break;
            }
            case STATE_SET_ACTION_URL:
            {
                screen= createSetActionUrlScreen(state);
                break;
            }
            case STATE_GPS:
            {
                screen= createChangeGpsScreen(state);
                break;
            }
            case STATE_DATASOURCE_MODE:
            {
                screen= createChangeDataSourceModeScreen(state);
                break;
            }
            case STATE_RMS:
            {
                screen= createClearRmsScreen(state);
                break;
            }
            case STATE_NETWORK:
            {
                screen= createChangeNetworkScreen(state);
                break;
            }
            case STATE_CHANGE_DONE:
            {
                screen= createChangeDoneScreen(state);
                break;
            }
            case STATE_CHANGE_CELL:
            {
                screen= createChangeCellScreen(state);
                break;
            }
            case STATE_SET_PTN:
            {
                screen= createSetPtnScreen(state);
                break;
            }
            case STATE_SET_DONE:
            {
                screen= createSetDoneScreen(state);
                break;
            }
            case STATE_SET_MVIEWER_IP:
            {
                screen= createSetMviewerIp(state);
                break;
            }
            case STATE_SET_ALONGROUTE_SPEED:
            {
                screen= createSetAlongrouteSpeed(state);
                break;
            }
            case STATE_SET_MAP_DATASET:
            {
                screen= createSetMapDataset(state);
                break;
            }
            case STATE_SET_LOGGER:
            {
                screen= createSetLogger(state);
                break;
            }
            case STATE_DWF_SEND_SETTING:
            {
                screen= createDwfSetting(state);
                break;
            }
            case STATE_SET_RESOURCE_PATH_ENABLE:
            {
                screen= createSetResourcesPathEnable(state);
                break;
            }
            case STATE_PTN_USERID:
            {
                screen= createPtnUserId(state);
                break;
            }
            case STATE_SET_BILLBOARD:
            {
                screen= createSetBillboardHost(state);
                break;
            }
			case STATE_CLEAR_STATIC_AUDIO:
			{
			    screen= createClearStaticAudio(state);
			    break;
			}
            case STATE_SHOW_DIRECTORY:
            {
                screen= createDirectoryScreen(state);
                break;
            }
            case STATE_SHOW_FILE_CONTENT:
            {
                screen= createOpenFileScreen(state);
                break;
            }
            case STATE_DUMP_LOCAL_FILE:
            {
                screen= createDirectoryScreen(state);
                break;
            }
            case STATE_SHOW_REGION:
            {
                screen= createShowRegion(state);
                break;
            }
            case STATE_SHOW_SET_URL_SCREEN:
            {
                screen= createSetUrlScreen(state);
                break;
            }
            case STATE_MAP_DOWNLOAD_CN:
            {
                screen= createMapDownloadCNEnable(state);
                break;
            }
            case STATE_STUCK_MONITOR:
            {
                screen= createStuckMonitorScreen(state);
                break;
            }
            case STATE_MAP_DISK_CACHE:
            {
                screen= createMapDiskCacheScreen(state);
                break;
            }
            case STATE_SWITCH_AIRPLANE_MODE:
            {
                screen= createSwitchAirplaneModeEnable(state);
                break;
            }
            case STATE_FETCH_PRELOAD_RES:
            {
                screen= createFetchPreloadRes(state);
                break;
            }
            case STATE_KONTAGENT_INFO:
            {
                screen= createKontagentScreen(state);
                break;
            }
            case STATE_PRELOAD_FILES:
            {
                screen= createPreloadFilesScreen(state);
                break;
            }
            case STATE_SHOW_SATELLITE_DURING_NAV:
            {
                screen= createShowSatelliteDuringNav(state);
                break;
            }
        }
        
        if (screen != null)
        {
            removeMenuById(screen, ICommonConstants.CMD_COMMON_PREFERENCE);
        }
        return screen;
    }
    
    
    private TnScreen createDwfSetting(int state)
    {
        CitizenScreen setLoggerScreen = UiFactory.getInstance().createScreen(state);

        final FrogLabel titleLabel = UiFactory.getInstance().createLabel(0, "Set Dwf Send profile");
        TnUiArgs args = titleLabel.getTnUiArgs();
        args.put(TnUiArgs.KEY_PREFER_WIDTH, this.uiDecorator.SCREEN_WIDTH);
        titleLabel.setStyle(AbstractTnGraphics.HCENTER);
        titleLabel.setFont(font);
        titleLabel.setForegroundColor(TnColor.WHITE, TnColor.WHITE);
        setLoggerScreen.getTitleContainer().add(titleLabel);
        setLoggerScreen.getTitleContainer().setBackgroundColor(TnColor.GRAY);
        
        checkbox = UiFactory.getInstance().createCitizenCheckItem(ID_DWF_GCM_ENABLE, "GCM Enabled", ImageDecorator.IMG_CHECK_BOX_ON.getImage(), ImageDecorator.IMG_CHECK_BOX_OFF.getImage());
        checkbox.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((SecretKeyUiDecorator) this.uiDecorator).SETTING_POPUP_COMBO_ITEM_WIDTH);
        checkbox.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((SecretKeyUiDecorator) this.uiDecorator).SETTING_POPUP_COMBO_ITEM_HEIGHT);
        checkbox.setStyle(AbstractTnGraphics.RIGHT);
        checkbox.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_CHECK_BOX));
        int blackColor = UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_BL);
        checkbox.setForegroundColor(blackColor, blackColor);
        checkbox.setUnCheckableIcon(ImageDecorator.IMG_CHECK_BOX_DISABLE.getImage());
        boolean isGcmEnable = ((DaoManager) DaoManager.getInstance()).getSimpleConfigDao().getBoolean(SimpleConfigDao.KEY_GCM_ENABLE);
        checkbox.setSelected(isGcmEnable);
        //Close the log output by default
        
        checkbox.setTouchEventListener(this);
        setLoggerScreen.getContentContainer().add(checkbox);
        
        sdCardCheckBox = UiFactory.getInstance().createCitizenCheckItem(ID_DWF_DISABLE_SMS, "Disable SMS", ImageDecorator.IMG_CHECK_BOX_ON.getImage(), ImageDecorator.IMG_CHECK_BOX_OFF.getImage());
        sdCardCheckBox.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((SecretKeyUiDecorator) this.uiDecorator).SETTING_POPUP_COMBO_ITEM_WIDTH);
        sdCardCheckBox.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((SecretKeyUiDecorator) this.uiDecorator).SETTING_POPUP_COMBO_ITEM_HEIGHT);
        sdCardCheckBox.setStyle(AbstractTnGraphics.RIGHT);
        sdCardCheckBox.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_CHECK_BOX));
        sdCardCheckBox.setForegroundColor(blackColor, blackColor);
        sdCardCheckBox.setUnCheckableIcon(ImageDecorator.IMG_CHECK_BOX_DISABLE.getImage());
        boolean isSmsDisable = ((DaoManager) DaoManager.getInstance()).getSimpleConfigDao().getString(
            SimpleConfigDao.KEY_SMS_DISABLE) != null;
        sdCardCheckBox.setSelected(isSmsDisable);
        if(!checkbox.isSelected())
        {
            sdCardCheckBox.setEnabled(false);
        }
        setLoggerScreen.getContentContainer().add(sdCardCheckBox);
        
        FrogButton saveBttn = UiFactory.getInstance().createButton(CMD_CHANGE_DONE, "Save");
        TnMenu menu = UiFactory.getInstance().createMenu();
        menu.add("", CMD_CHANGE_DONE);
        saveBttn.setMenu(menu, AbstractTnComponent.TYPE_CLICK);
        setLoggerScreen.getContentContainer().add(saveBttn);
        
        return setLoggerScreen;
    }


    private TnScreen createFetchPreloadRes(int state)
    {
        CitizenScreen preloadResScreen = UiFactory.getInstance().createScreen(state);
        
        final FrogLabel titleLabel = UiFactory.getInstance().createLabel(0, "Fetch Preload Res");
        TnUiArgs args = titleLabel.getTnUiArgs();
        args.put(TnUiArgs.KEY_PREFER_WIDTH, this.uiDecorator.SCREEN_WIDTH);
        titleLabel.setStyle(AbstractTnGraphics.HCENTER);
        titleLabel.setFont(font);
        titleLabel.setForegroundColor(TnColor.WHITE, TnColor.WHITE);
        preloadResScreen.getTitleContainer().add(titleLabel);
        preloadResScreen.getTitleContainer().setBackgroundColor(TnColor.GRAY);
        
        StringBuffer msg = new StringBuffer();
        msg.append("Notes: clear RMS and sync request later");
        FrogMultiLine multiLine = UiFactory.getInstance().createMultiLine(0, msg.toString());
        preloadResScreen.getContentContainer().add(multiLine);
        preloadResScreen.getContentContainer().setBackgroundColor(TnColor.WHITE);
        
        FrogButton prefetchBttn = UiFactory.getInstance().createButton(CMD_CHANGE_DONE, "Prefetch");
        TnMenu menu = UiFactory.getInstance().createMenu();
        menu.add("", CMD_CHANGE_DONE);
        prefetchBttn.setMenu(menu, AbstractTnComponent.TYPE_CLICK);
        preloadResScreen.getContentContainer().add(prefetchBttn);
        
        return preloadResScreen;
    }

    private TnScreen createKontagentScreen(int state)
    {
        CitizenScreen kontagentScreen = UiFactory.getInstance().createScreen(state);
        
        final FrogLabel titleLabel = UiFactory.getInstance().createLabel(0, "Kontagent");
        TnUiArgs args = titleLabel.getTnUiArgs();
        args.put(TnUiArgs.KEY_PREFER_WIDTH, this.uiDecorator.SCREEN_WIDTH);
        titleLabel.setStyle(AbstractTnGraphics.HCENTER);
        titleLabel.setFont(font);
        titleLabel.setForegroundColor(TnColor.WHITE, TnColor.WHITE);
        kontagentScreen.getTitleContainer().add(titleLabel);
        kontagentScreen.getTitleContainer().setBackgroundColor(TnColor.GRAY);
        
        addDoubleLinesItem(kontagentScreen.getContentContainer(), "KT ID:",  KontagentLogger.getInstance().getSenderId());
        addDoubleLinesItem(kontagentScreen.getContentContainer(), "KT App:",  KontagentLogger.getInstance().getApiKey());
        kontagentScreen.getContentContainer().setBackgroundColor(TnColor.WHITE);
        
        generateKTLogFileCheckBox = UiFactory.getInstance().createCitizenCheckItem(ID_KONTAGENT_LOGGER_ENABLED_GENERATE_FILE_LOG, "Enable Generate Log File:", ImageDecorator.IMG_CHECK_BOX_ON.getImage(), ImageDecorator.IMG_CHECK_BOX_OFF.getImage());
        generateKTLogFileCheckBox.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((SecretKeyUiDecorator) this.uiDecorator).SETTING_POPUP_COMBO_ITEM_WIDTH);
        generateKTLogFileCheckBox.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((SecretKeyUiDecorator) this.uiDecorator).SETTING_POPUP_COMBO_ITEM_HEIGHT);
        generateKTLogFileCheckBox.setStyle(AbstractTnGraphics.RIGHT);
        generateKTLogFileCheckBox.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_CHECK_BOX));
        int blackColor = UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_BL);
        generateKTLogFileCheckBox.setForegroundColor(blackColor, blackColor);
        generateKTLogFileCheckBox.setUnCheckableIcon(ImageDecorator.IMG_CHECK_BOX_DISABLE.getImage());
        boolean isEnable = ((DaoManager) DaoManager.getInstance()).getSimpleConfigDao().getBoolean(SimpleConfigDao.KEY_KONTAGENT_GENERATE_EVENT_LOG_FILE);
        generateKTLogFileCheckBox.setSelected(isEnable);
        generateKTLogFileCheckBox.setTouchEventListener(this);
        kontagentScreen.getContentContainer().add(generateKTLogFileCheckBox);
        
        usingTestServerCheckBox = UiFactory.getInstance().createCitizenCheckItem(ID_KONTAGENT_USING_TEST_SERVER, "Using Test Server:", ImageDecorator.IMG_CHECK_BOX_ON.getImage(), ImageDecorator.IMG_CHECK_BOX_OFF.getImage());
        usingTestServerCheckBox.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((SecretKeyUiDecorator) this.uiDecorator).SETTING_POPUP_COMBO_ITEM_WIDTH);
        usingTestServerCheckBox.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((SecretKeyUiDecorator) this.uiDecorator).SETTING_POPUP_COMBO_ITEM_HEIGHT);
        usingTestServerCheckBox.setStyle(AbstractTnGraphics.RIGHT);
        usingTestServerCheckBox.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_CHECK_BOX));
        usingTestServerCheckBox.setForegroundColor(blackColor, blackColor);
        usingTestServerCheckBox.setUnCheckableIcon(ImageDecorator.IMG_CHECK_BOX_DISABLE.getImage());
        boolean isTestEnabled = ((DaoManager) DaoManager.getInstance()).getSimpleConfigDao().getBoolean(SimpleConfigDao.KEY_USING_KONTAGENT_TEST_SERVER);
        usingTestServerCheckBox.setSelected(isTestEnabled);
        usingTestServerCheckBox.setTouchEventListener(this);
        kontagentScreen.getContentContainer().add(usingTestServerCheckBox);
        return kontagentScreen;
    }
    
    private TnScreen createPreloadFilesScreen(int state)
    {
        CitizenScreen preloadFileScreen = UiFactory.getInstance().createScreen(state);
        String result = (String) this.model.get(KEY_S_DUMPFILE_RESULT);
        FrogMultiLine resultLabel = UiFactory.getInstance().createMultiLine(ID_DUMP_FILE_VIEW, result);
        resultLabel.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_CHECK_BOX));
        resultLabel.setForegroundColor(0x0);
        preloadFileScreen.getContentContainer().add(resultLabel);
        return preloadFileScreen;
    }
    
    private void addDoubleLinesItem(AbstractTnContainer container, String itemName,  String value)
    {
        CitizenProfileListItem prefItem = new CitizenProfileListItem(0);
        prefItem.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((SecretKeyUiDecorator) uiDecorator).SETTING_POPUP_COMBO_ITEM_HEIGHT);
        prefItem.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, uiDecorator.SCREEN_WIDTH);
        prefItem.setBoldFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_MY_PROFILE_ITEM));
        prefItem.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_MY_PROFILE_VALUE));

        prefItem.setTitleColor(UiStyleManager.getInstance().getColor(
            UiStyleManager.TEXT_COLOR_BL), 
            UiStyleManager.getInstance().getColor(
                UiStyleManager.TEXT_COLOR_BL));
        prefItem.setValueColor(UiStyleManager.getInstance().getColor(
            UiStyleManager.TEXT_COLOR_BL), 
            UiStyleManager.getInstance().getColor(
                UiStyleManager.TEXT_COLOR_BL));
        prefItem.setEnabled(false);
        prefItem.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.PROFILE_LIST_ITEM);
        prefItem.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, null);
     
        prefItem.setTitle(itemName);
        prefItem.setValue(value);
        container.add(prefItem);
    }
    
    private TnScreen createSwitchAirplaneModeEnable(int state)
    {
        CitizenScreen setResourcePathEnableScreen = UiFactory.getInstance().createScreen(state);

        final FrogLabel titleLabel = UiFactory.getInstance().createLabel(0, "Enable SwitchAirplaneMode");
        TnUiArgs args = titleLabel.getTnUiArgs();
        args.put(TnUiArgs.KEY_PREFER_WIDTH, this.uiDecorator.SCREEN_WIDTH);
        titleLabel.setStyle(AbstractTnGraphics.HCENTER);
        titleLabel.setFont(font);
        titleLabel.setForegroundColor(TnColor.WHITE, TnColor.WHITE);
        setResourcePathEnableScreen.getTitleContainer().add(titleLabel);
        setResourcePathEnableScreen.getTitleContainer().setBackgroundColor(TnColor.GRAY);
        
        checkbox = UiFactory.getInstance().createCitizenCheckItem(ID_LOGGER_ENABLED_CHECKBOX, "Enable SwitchAirplaneMode:", ImageDecorator.IMG_CHECK_BOX_ON.getImage(), ImageDecorator.IMG_CHECK_BOX_OFF.getImage());
        checkbox.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((SecretKeyUiDecorator) this.uiDecorator).SETTING_POPUP_COMBO_ITEM_WIDTH);
        checkbox.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((SecretKeyUiDecorator) this.uiDecorator).SETTING_POPUP_COMBO_ITEM_HEIGHT);
        checkbox.setStyle(AbstractTnGraphics.RIGHT);
        checkbox.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_CHECK_BOX));
        int blackColor = UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_BL);
        checkbox.setForegroundColor(blackColor, blackColor);
        checkbox.setUnCheckableIcon(ImageDecorator.IMG_CHECK_BOX_DISABLE.getImage());
        boolean isEnable = ((DaoManager) DaoManager.getInstance()).getSimpleConfigDao().getBoolean(SimpleConfigDao.KEY_SWITCH_AIRPLANE_MODE_ENABLE);
        checkbox.setSelected(isEnable);
        checkbox.setTouchEventListener(this);
        setResourcePathEnableScreen.getContentContainer().add(checkbox);

        FrogButton saveBttn = UiFactory.getInstance().createButton(CMD_CHANGE_DONE, "Save");
        TnMenu menu = UiFactory.getInstance().createMenu();
        menu.add("", CMD_CHANGE_DONE);
        saveBttn.setMenu(menu, AbstractTnComponent.TYPE_CLICK);
        setResourcePathEnableScreen.getContentContainer().add(saveBttn);
        
        return setResourcePathEnableScreen;
    }


    private TnScreen createChangeDataSourceModeScreen(int state)
    {
        // TODO Auto-generated method stub
        CitizenScreen changeGpsScreen = UiFactory.getInstance().createScreen(state);
        final FrogLabel titleLabel = UiFactory.getInstance().createLabel(0, "Change Data Source Mode");
        TnUiArgs args = titleLabel.getTnUiArgs();
        args.put(TnUiArgs.KEY_PREFER_WIDTH, this.uiDecorator.SCREEN_WIDTH);
        titleLabel.setStyle(AbstractTnGraphics.HCENTER);
        titleLabel.setFont(font);
        titleLabel.setForegroundColor(TnColor.WHITE, TnColor.WHITE);
        changeGpsScreen.getTitleContainer().add(titleLabel);
        changeGpsScreen.getTitleContainer().setBackgroundColor(TnColor.GRAY);
        
        FrogList dataSourceModeList = UiFactory.getInstance().createList(0);
        changeGpsScreen.getContentContainer().add(dataSourceModeList);
        
        int dataSourceModeIndex = ((DaoManager)DaoManager.getInstance()).getSecretSettingDao().get(SecretSettingDao.ONBOARD_MODE_INDEX);
        String[] labels = (String[])this.model.get(KEY_ARRAY_DATASOURCE_MODE_LABELS);
        dataSourceModeList.setAdapter(new DefaultListAdapter(labels, null, dataSourceModeIndex));
        
        return changeGpsScreen;    }


    private TnScreen createSetUrlScreen(int state)
    {
        CitizenScreen setUrlScreen = UiFactory.getInstance().createScreen(state);
        
        final FrogLabel titleLabel = UiFactory.getInstance().createLabel(0, "Set Url");
        TnUiArgs args = titleLabel.getTnUiArgs();
        args.put(TnUiArgs.KEY_PREFER_WIDTH, this.uiDecorator.SCREEN_WIDTH);
        titleLabel.setStyle(AbstractTnGraphics.HCENTER);
        titleLabel.setFont(font);
        titleLabel.setForegroundColor(TnColor.WHITE, TnColor.WHITE);
        setUrlScreen.getTitleContainer().add(titleLabel);
        setUrlScreen.getTitleContainer().setBackgroundColor(TnColor.GRAY);
        
        String host = ((DaoManager) DaoManager.getInstance()).getSimpleConfigDao().getString(SimpleConfigDao.KEY_MANUAL_SERVER_URL);
        if(host == null)
            host = "";
        
        textField = UiFactory.getInstance().createTextField(host, -1, FrogTextField.ANY,  "", 0);
        setUrlScreen.getContentContainer().add(textField);
        setUrlScreen.getContentContainer().setBackgroundColor(TnColor.WHITE);
        
        FrogButton saveBttn = UiFactory.getInstance().createButton(CMD_COMMON_OK, "Save");
        TnMenu menu = UiFactory.getInstance().createMenu();
        menu.add("", CMD_COMMON_OK);
        saveBttn.setMenu(menu, AbstractTnComponent.TYPE_CLICK);
        setUrlScreen.getContentContainer().add(saveBttn);
        this.model.put(KEY_B_GET_LOCATOR, true);
        
        return setUrlScreen;
    }


    private TnScreen createShowRegion(int state)
    {
        CitizenScreen regionSettingScreen = UiFactory.getInstance().createScreen(state);
        
        final FrogLabel titleLabel = UiFactory.getInstance().createLabel(0, "Set Region");
        TnUiArgs args = titleLabel.getTnUiArgs();
        args.put(TnUiArgs.KEY_PREFER_WIDTH, this.uiDecorator.SCREEN_WIDTH);
        titleLabel.setStyle(AbstractTnGraphics.HCENTER);
        titleLabel.setFont(font);
        titleLabel.setForegroundColor(TnColor.WHITE, TnColor.WHITE);
        regionSettingScreen.getTitleContainer().add(titleLabel);
        regionSettingScreen.getTitleContainer().setBackgroundColor(TnColor.GRAY);
        
        FrogMultiLine multiLine = UiFactory.getInstance().createMultiLine(0, "Notes: if keep null and save, it will use default region. Furthermore, you can select city from the list, it will fill in the Lat/lon automatically.");
        regionSettingScreen.getContentContainer().add(multiLine);
        regionSettingScreen.getContentContainer().setBackgroundColor(TnColor.WHITE);
        
        TnLinearContainer upContainer = new TnLinearContainer(0, false);
        TnLinearContainer latLonContainer = new TnLinearContainer(0, true);
        TnLinearContainer saveContainer = new TnLinearContainer(0, true, AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);
        saveContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((SecretKeyUiDecorator) this.uiDecorator).HALF_SCREEN_WIDTH);
       
        final FrogLabel latLabel = UiFactory.getInstance().createLabel(0, "Lat");
        String defaultLat=String.valueOf(LocationProvider.getInstance().getInitialLocation().getLatitude());
        FrogTextField regionLat = UiFactory.getInstance().createTextField(defaultLat, -1, FrogTextField.PHONENUMBER,  "", ID_REGION_LAT);
        regionLat.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((SecretKeyUiDecorator) this.uiDecorator).HALF_SCREEN_WIDTH);
        regionSettingScreen.getContentContainer().setBackgroundColor(TnColor.WHITE);
        
        final FrogLabel lonLabel = UiFactory.getInstance().createLabel(0, "Lon");
        String defaultLon=String.valueOf(LocationProvider.getInstance().getInitialLocation().getLongitude());
        FrogTextField regionLon = UiFactory.getInstance().createTextField(defaultLon, -1, FrogTextField.PHONENUMBER,  "", ID_REGION_LON);
        regionLon.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((SecretKeyUiDecorator) this.uiDecorator).HALF_SCREEN_WIDTH);
        regionSettingScreen.getContentContainer().setBackgroundColor(TnColor.WHITE);
        
        latLonContainer.add(latLabel);
        latLonContainer.add(regionLat);
        latLonContainer.add(lonLabel);
        latLonContainer.add(regionLon);
        
        upContainer.add(latLonContainer);
        
        FrogButton saveBttn = UiFactory.getInstance().createButton(CMD_SET_REGION, "Save");
        TnMenu menu = UiFactory.getInstance().createMenu();
        menu.add("", CMD_SET_REGION);
        saveBttn.setMenu(menu, AbstractTnComponent.TYPE_CLICK);
        final FrogLabel cityLable = UiFactory.getInstance().createLabel(ID_REGION_LABLE, "");
        saveContainer.add(cityLable);
        saveContainer.add(saveBttn);
        
        regionLat.setTextChangeListener(new ITnTextChangeListener()
        {
            public void onTextChange(AbstractTnComponent component, String text)
            {
                cityLable.setText("");
            }
        });
        regionLon.setTextChangeListener(new ITnTextChangeListener()
        {
            public void onTextChange(AbstractTnComponent component, String text)
            {
                cityLable.setText("");
            }
        });
        upContainer.add(saveContainer);
        regionSettingScreen.getContentContainer().add(upContainer);
        
        FrogList regionList = UiFactory.getInstance().createList(0);
        regionSettingScreen.getContentContainer().add(regionList);
        
        String[] labelAnchors = (String[]) this.model.get(KEY_ARRAY_REGION_LABLES);
        regionList.setAdapter(new DefaultListAdapter(labelAnchors, null, -1));
        
        return regionSettingScreen;
    }
    
    private TnScreen createSetBillboardHost(int state)
    {
        CitizenScreen setBillboardScreen = UiFactory.getInstance().createScreen(state);
        
        final FrogLabel titleLabel = UiFactory.getInstance().createLabel(0, "Set Billboard Host");
        TnUiArgs args = titleLabel.getTnUiArgs();
        args.put(TnUiArgs.KEY_PREFER_WIDTH, this.uiDecorator.SCREEN_WIDTH);
        titleLabel.setStyle(AbstractTnGraphics.HCENTER);
        titleLabel.setFont(font);
        titleLabel.setForegroundColor(TnColor.WHITE, TnColor.WHITE);
        setBillboardScreen.getTitleContainer().add(titleLabel);
        setBillboardScreen.getTitleContainer().setBackgroundColor(TnColor.GRAY);
        
        FrogMultiLine multiLine = UiFactory.getInstance().createMultiLine(0, "Notes: if keep null and save, it will use default host");
        setBillboardScreen.getContentContainer().add(multiLine);
        setBillboardScreen.getContentContainer().setBackgroundColor(TnColor.WHITE);
        
        String hostUrl = null;
        hostUrl = DaoManager.getInstance().getSimpleConfigDao().getString(SimpleConfigDao.KEY_BILLBOARD_HOST);
        Host host = CommManager.getInstance().getComm().getHostProvider().createHost(IServerProxyConstants.ACT_BILLBOARD_ADS);
        
        if (hostUrl == null || hostUrl.trim().length() <= 0)
        {
            if (host != null)
            {
                hostUrl = host.protocol + "://" + host.host;
                if ( host.port > 0 )
                {
                    hostUrl = hostUrl + ":" + host.port;
                }
            }
        }
        
        textField = UiFactory.getInstance().createTextField(hostUrl, -1, FrogTextField.ANY,  "", 0);
        setBillboardScreen.getContentContainer().add(textField);
        setBillboardScreen.getContentContainer().setBackgroundColor(TnColor.WHITE);
        
        FrogButton saveBttn = UiFactory.getInstance().createButton(CMD_CHANGE_DONE, "Save");
        TnMenu menu = UiFactory.getInstance().createMenu();
        menu.add("", CMD_CHANGE_DONE);
        saveBttn.setMenu(menu, AbstractTnComponent.TYPE_CLICK);
        setBillboardScreen.getContentContainer().add(saveBttn);
        
        return setBillboardScreen;
    }
    
	private TnScreen createClearStaticAudio(int state)
    {
        CitizenScreen clearStaticAudioScreen = UiFactory.getInstance().createScreen(state);
        
        final FrogLabel titleLabel = UiFactory.getInstance().createLabel(0, "Clear Static Audio");
        TnUiArgs args = titleLabel.getTnUiArgs();
        args.put(TnUiArgs.KEY_PREFER_WIDTH, this.uiDecorator.SCREEN_WIDTH);
        titleLabel.setStyle(AbstractTnGraphics.HCENTER);
        titleLabel.setFont(font);
        titleLabel.setForegroundColor(TnColor.WHITE, TnColor.WHITE);
        clearStaticAudioScreen.getTitleContainer().add(titleLabel);
        clearStaticAudioScreen.getTitleContainer().setBackgroundColor(TnColor.GRAY);
        
        FrogMultiLine multiLine = UiFactory.getInstance().createMultiLine(0, "Clear Static Audio successfully!");
        clearStaticAudioScreen.getContentContainer().add(multiLine);
        clearStaticAudioScreen.getContentContainer().setBackgroundColor(TnColor.WHITE);
        
        return clearStaticAudioScreen;
    }
	
    private TnScreen createPtnUserId(int state)
    {
        CitizenScreen ptnUserIdScreen = UiFactory.getInstance().createScreen(state);
        
        final FrogLabel titleLabel = UiFactory.getInstance().createLabel(0, "Ptn & UserId");
        TnUiArgs args = titleLabel.getTnUiArgs();
        args.put(TnUiArgs.KEY_PREFER_WIDTH, this.uiDecorator.SCREEN_WIDTH);
        titleLabel.setStyle(AbstractTnGraphics.HCENTER);
        titleLabel.setFont(font);
        titleLabel.setForegroundColor(TnColor.WHITE, TnColor.WHITE);
        ptnUserIdScreen.getTitleContainer().add(titleLabel);
        ptnUserIdScreen.getTitleContainer().setBackgroundColor(TnColor.GRAY);
        
        String ptn = DaoManager.getInstance().getBillingAccountDao().getPtn();
        if ( ptn != null )
        {
            ptn = "PTN: " + ptn;
        }
        FrogMultiLine ptnLine = UiFactory.getInstance().createMultiLine(0, ptn);
        ptnUserIdScreen.getContentContainer().add(ptnLine);
        ptnUserIdScreen.getContentContainer().setBackgroundColor(TnColor.WHITE);
        
        String userId = DaoManager.getInstance().getMandatoryNodeDao().getMandatoryNode().getUserInfo().userId;
        if ( userId != null )
        {
            userId = "UserId: " + userId;
        }
        FrogMultiLine userIdLine = UiFactory.getInstance().createMultiLine(0, userId);
        ptnUserIdScreen.getContentContainer().add(userIdLine);
        ptnUserIdScreen.getContentContainer().setBackgroundColor(TnColor.WHITE);
        
        return ptnUserIdScreen;
    }
    
    private TnScreen createSetMapDataset(int state)
    {
        CitizenScreen testMapDataset = UiFactory.getInstance().createScreen(state);
        
        String prevDataSet = AbstractDaoManager.getInstance().getStartupDao()
                .getMapDataset();
        
        if(prevDataSet == null)
            prevDataSet = "";
        
        final FrogLabel titleLabel = UiFactory.getInstance().createLabel(0, "Test Map Dataset - " + prevDataSet);
        TnUiArgs args = titleLabel.getTnUiArgs();
        args.put(TnUiArgs.KEY_PREFER_WIDTH, this.uiDecorator.SCREEN_WIDTH);
        titleLabel.setStyle(AbstractTnGraphics.HCENTER);
        titleLabel.setFont(font);
        titleLabel.setForegroundColor(TnColor.WHITE, TnColor.WHITE);
        testMapDataset.getTitleContainer().add(titleLabel);
        testMapDataset.getTitleContainer().setBackgroundColor(TnColor.GRAY);
        
        FrogMultiLine multiLine = UiFactory.getInstance().createMultiLine(0, "Notes: if keep null and save, it will back to default dataset");
        testMapDataset.getContentContainer().add(multiLine);
        testMapDataset.getContentContainer().setBackgroundColor(TnColor.WHITE);
        
        String latestDataSet = DaoManager.getInstance().getSimpleConfigDao()
                .getString(SimpleConfigDao.KEY_SWITCHED_DATASET);
        
        textField = UiFactory.getInstance().createTextField(latestDataSet == null ? "" : latestDataSet, -1, FrogTextField.ANY,  "", 0);
        testMapDataset.getContentContainer().add(textField);
        testMapDataset.getContentContainer().setBackgroundColor(TnColor.WHITE);
        
        FrogButton saveBttn = UiFactory.getInstance().createButton(CMD_CHANGE_DONE, "Save");
        TnMenu menu = UiFactory.getInstance().createMenu();
        menu.add("", CMD_CHANGE_DONE);
        saveBttn.setMenu(menu, AbstractTnComponent.TYPE_CLICK);
        testMapDataset.getContentContainer().add(saveBttn);
        
        return testMapDataset;
    }

    private TnScreen createSetMviewerIp(int state)
    {
        CitizenScreen setMviewerIpScreen = UiFactory.getInstance().createScreen(state);
        
        final FrogLabel titleLabel = UiFactory.getInstance().createLabel(0, "Set MViewer Ip");
        TnUiArgs args = titleLabel.getTnUiArgs();
        args.put(TnUiArgs.KEY_PREFER_WIDTH, this.uiDecorator.SCREEN_WIDTH);
        titleLabel.setStyle(AbstractTnGraphics.HCENTER);
        titleLabel.setFont(font);
        titleLabel.setForegroundColor(TnColor.WHITE, TnColor.WHITE);
        setMviewerIpScreen.getTitleContainer().add(titleLabel);
        setMviewerIpScreen.getTitleContainer().setBackgroundColor(TnColor.GRAY);
        
        FrogMultiLine multiLine = UiFactory.getInstance().createMultiLine(0, "Notes: if keep null and save, it will use default Mviewer IP");
        setMviewerIpScreen.getContentContainer().add(multiLine);
        setMviewerIpScreen.getContentContainer().setBackgroundColor(TnColor.WHITE);
        
        String host = ((DaoManager) DaoManager.getInstance()).getSimpleConfigDao().getString(SimpleConfigDao.KEY_MVIEWER_HOST_IP);
        if(host == null)
            host = "";
        
        textField = UiFactory.getInstance().createTextField(host, -1, FrogTextField.ANY,  "", 0);
        setMviewerIpScreen.getContentContainer().add(textField);
        setMviewerIpScreen.getContentContainer().setBackgroundColor(TnColor.WHITE);
        
        FrogButton saveBttn = UiFactory.getInstance().createButton(CMD_CHANGE_DONE, "Save");
        TnMenu menu = UiFactory.getInstance().createMenu();
        menu.add("", CMD_CHANGE_DONE);
        saveBttn.setMenu(menu, AbstractTnComponent.TYPE_CLICK);
        setMviewerIpScreen.getContentContainer().add(saveBttn);
        
        return setMviewerIpScreen;
    }

    private TnScreen createSetAlongrouteSpeed(int state)
    {
        CitizenScreen screen = UiFactory.getInstance().createScreen(state);
        
        final FrogLabel titleLabel = UiFactory.getInstance().createLabel(0, "Set Along Route Speed");
        TnUiArgs args = titleLabel.getTnUiArgs();
        args.put(TnUiArgs.KEY_PREFER_WIDTH, this.uiDecorator.SCREEN_WIDTH);
        titleLabel.setStyle(AbstractTnGraphics.HCENTER);
        titleLabel.setFont(font);
        titleLabel.setForegroundColor(TnColor.WHITE, TnColor.WHITE);
        screen.getTitleContainer().add(titleLabel);
        screen.getTitleContainer().setBackgroundColor(TnColor.GRAY);
        
        FrogMultiLine multiLine = UiFactory.getInstance().createMultiLine(0, "Notes: if keep null or 0 and save, it will use default 30m/s. The unit is m/s");
        screen.getContentContainer().add(multiLine);
        screen.getContentContainer().setBackgroundColor(TnColor.WHITE);
        
        String speed = ((DaoManager) DaoManager.getInstance()).getSimpleConfigDao().getString(SimpleConfigDao.KEY_ALONG_ROUTE_SPEED);
        if(speed == null || speed.trim().length() == 0)
        {
            speed = "30";
        }
        speedTextField = UiFactory.getInstance().createTextField(speed, 3, FrogTextField.NUMERIC,  "m/s", 0);
        screen.getContentContainer().add(speedTextField);
        screen.getContentContainer().setBackgroundColor(TnColor.WHITE);
        
        FrogButton saveBttn = UiFactory.getInstance().createButton(CMD_CHANGE_DONE, "Save");
        TnMenu menu = UiFactory.getInstance().createMenu();
        menu.add("", CMD_CHANGE_DONE);
        saveBttn.setMenu(menu, AbstractTnComponent.TYPE_CLICK);
        screen.getContentContainer().add(saveBttn);
        
        return screen;
    }

    private TnScreen createSetLogger(int state)
    {
        CitizenScreen setLoggerScreen = UiFactory.getInstance().createScreen(state);

        final FrogLabel titleLabel = UiFactory.getInstance().createLabel(0, "Set Logger");
        TnUiArgs args = titleLabel.getTnUiArgs();
        args.put(TnUiArgs.KEY_PREFER_WIDTH, this.uiDecorator.SCREEN_WIDTH);
        titleLabel.setStyle(AbstractTnGraphics.HCENTER);
        titleLabel.setFont(font);
        titleLabel.setForegroundColor(TnColor.WHITE, TnColor.WHITE);
        setLoggerScreen.getTitleContainer().add(titleLabel);
        setLoggerScreen.getTitleContainer().setBackgroundColor(TnColor.GRAY);
        
        checkbox = UiFactory.getInstance().createCitizenCheckItem(ID_LOGGER_ENABLED_CHECKBOX, "Logger Enabled:", ImageDecorator.IMG_CHECK_BOX_ON.getImage(), ImageDecorator.IMG_CHECK_BOX_OFF.getImage());
        checkbox.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((SecretKeyUiDecorator) this.uiDecorator).SETTING_POPUP_COMBO_ITEM_WIDTH);
        checkbox.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((SecretKeyUiDecorator) this.uiDecorator).SETTING_POPUP_COMBO_ITEM_HEIGHT);
        checkbox.setStyle(AbstractTnGraphics.RIGHT);
        checkbox.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_CHECK_BOX));
        int blackColor = UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_BL);
        checkbox.setForegroundColor(blackColor, blackColor);
        checkbox.setUnCheckableIcon(ImageDecorator.IMG_CHECK_BOX_DISABLE.getImage());
        boolean isLoggerEnable = ((DaoManager) DaoManager.getInstance()).getSimpleConfigDao().getBoolean(SimpleConfigDao.KEY_LOGGER_ENABLE);
        //Close the log output by default
        boolean isLoggerSetted = ((DaoManager) DaoManager.getInstance()).getSimpleConfigDao().getString(
            SimpleConfigDao.KEY_LOGGER_ENABLE) != null;
        checkbox.setSelected(isLoggerEnable || !isLoggerSetted);
        
        checkbox.setTouchEventListener(this);
        setLoggerScreen.getContentContainer().add(checkbox);
        
        sdCardCheckBox = UiFactory.getInstance().createCitizenCheckItem(ID_SDCARD_OUTPUT_ENABLEED_CHECKBOX, "Output to sd card Enabled:", ImageDecorator.IMG_CHECK_BOX_ON.getImage(), ImageDecorator.IMG_CHECK_BOX_OFF.getImage());
        sdCardCheckBox.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((SecretKeyUiDecorator) this.uiDecorator).SETTING_POPUP_COMBO_ITEM_WIDTH);
        sdCardCheckBox.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((SecretKeyUiDecorator) this.uiDecorator).SETTING_POPUP_COMBO_ITEM_HEIGHT);
        sdCardCheckBox.setStyle(AbstractTnGraphics.RIGHT);
        sdCardCheckBox.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_CHECK_BOX));
        sdCardCheckBox.setForegroundColor(blackColor, blackColor);
        sdCardCheckBox.setUnCheckableIcon(ImageDecorator.IMG_CHECK_BOX_DISABLE.getImage());
        boolean isSdCardOuptEnable = ((DaoManager) DaoManager.getInstance()).getSimpleConfigDao().getBoolean(SimpleConfigDao.KEY_SDCARD_OUTPUT_LOGGER_ENABLE);
        sdCardCheckBox.setSelected(isSdCardOuptEnable);
        if(!checkbox.isSelected())
        {
            sdCardCheckBox.setEnabled(false);
        }
        setLoggerScreen.getContentContainer().add(sdCardCheckBox);
        
        localMisLogCheckBox = UiFactory.getInstance().createCitizenCheckItem(ID_SDCARD_OUTPUT_ENABLEED_CHECKBOX, "Local Mislog Enabled:", ImageDecorator.IMG_CHECK_BOX_ON.getImage(), ImageDecorator.IMG_CHECK_BOX_OFF.getImage());
        localMisLogCheckBox.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((SecretKeyUiDecorator) this.uiDecorator).SETTING_POPUP_COMBO_ITEM_WIDTH);
        localMisLogCheckBox.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((SecretKeyUiDecorator) this.uiDecorator).SETTING_POPUP_COMBO_ITEM_HEIGHT);
        localMisLogCheckBox.setStyle(AbstractTnGraphics.RIGHT);
        localMisLogCheckBox.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_CHECK_BOX));
        localMisLogCheckBox.setForegroundColor(blackColor, blackColor);
        localMisLogCheckBox.setUnCheckableIcon(ImageDecorator.IMG_CHECK_BOX_DISABLE.getImage());
        boolean isLocalMislogEnable = ((DaoManager) DaoManager.getInstance()).getSimpleConfigDao().getBoolean(SimpleConfigDao.KEY_LOCAL_MISLOG_ENABLE);
        localMisLogCheckBox.setSelected(isLocalMislogEnable);
        if(!checkbox.isSelected())
        {
            localMisLogCheckBox.setEnabled(false);
        }
        setLoggerScreen.getContentContainer().add(localMisLogCheckBox);
        
        runtimeStatusCheckBox = UiFactory.getInstance().createCitizenCheckItem(ID_RUNTIME_STATUS_CHECKBOX, "Log runtime status enabled:", ImageDecorator.IMG_CHECK_BOX_ON.getImage(), ImageDecorator.IMG_CHECK_BOX_OFF.getImage());
        runtimeStatusCheckBox.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((SecretKeyUiDecorator) this.uiDecorator).SETTING_POPUP_COMBO_ITEM_WIDTH);
        runtimeStatusCheckBox.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((SecretKeyUiDecorator) this.uiDecorator).SETTING_POPUP_COMBO_ITEM_HEIGHT);
        runtimeStatusCheckBox.setStyle(AbstractTnGraphics.RIGHT);
        runtimeStatusCheckBox.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_CHECK_BOX));
        runtimeStatusCheckBox.setForegroundColor(blackColor, blackColor);
        runtimeStatusCheckBox.setUnCheckableIcon(ImageDecorator.IMG_CHECK_BOX_DISABLE.getImage());
        boolean isRuntimeStatusLogEnable = ((DaoManager) DaoManager.getInstance()).getSimpleConfigDao().getBoolean(SimpleConfigDao.KEY_RUNTIME_STATUS_LOG_ENABLE);
        runtimeStatusCheckBox.setSelected(isRuntimeStatusLogEnable);
        if(!checkbox.isSelected())
        {
            runtimeStatusCheckBox.setEnabled(false);
        }
        runtimeStatusCheckBox .setTouchEventListener(this);
        setLoggerScreen.getContentContainer().add(runtimeStatusCheckBox);
        
        FrogMultiLine multiLine = UiFactory.getInstance().createMultiLine(0, "Set runtimestatus Log interval(milliseconds):");
        setLoggerScreen.getContentContainer().add(multiLine);
        
        textField = UiFactory.getInstance().createTextField("", -1, FrogTextField.ANY, "", ID_RUNTIEM_STATUS_LOG_INTERVAL);
        setLoggerScreen.getContentContainer().add(textField);
        int runtimeStatusLogInterval = ((DaoManager) DaoManager.getInstance()).getSimpleConfigDao().getInteger(SimpleConfigDao.KEY_RUNTIME_STATUS_LOG_INTERVAL);
        if(runtimeStatusLogInterval == -1)
        {
            textField.setText(RuntimeStatusLogger.DEFAULT_RUNTIME_LOG_INTERVAL + "");
        }
        else
        {
            textField.setText(runtimeStatusLogInterval + "");
        }
        if(!runtimeStatusCheckBox.isEnabled() || !runtimeStatusCheckBox.isSelected())
        {
            textField.setEnabled(false);
        }
        
        FrogButton saveBttn = UiFactory.getInstance().createButton(CMD_CHANGE_DONE, "Save");
        TnMenu menu = UiFactory.getInstance().createMenu();
        menu.add("", CMD_CHANGE_DONE);
        saveBttn.setMenu(menu, AbstractTnComponent.TYPE_CLICK);
        setLoggerScreen.getContentContainer().add(saveBttn);
        
        return setLoggerScreen;
    }
    
    private TnScreen createSetResourcesPathEnable(int state)
    {
        CitizenScreen setResourcePathEnableScreen = UiFactory.getInstance().createScreen(state);

        final FrogLabel titleLabel = UiFactory.getInstance().createLabel(0, "Enable SetResourcePath");
        TnUiArgs args = titleLabel.getTnUiArgs();
        args.put(TnUiArgs.KEY_PREFER_WIDTH, this.uiDecorator.SCREEN_WIDTH);
        titleLabel.setStyle(AbstractTnGraphics.HCENTER);
        titleLabel.setFont(font);
        titleLabel.setForegroundColor(TnColor.WHITE, TnColor.WHITE);
        setResourcePathEnableScreen.getTitleContainer().add(titleLabel);
        setResourcePathEnableScreen.getTitleContainer().setBackgroundColor(TnColor.GRAY);
        
        checkbox = UiFactory.getInstance().createCitizenCheckItem(ID_LOGGER_ENABLED_CHECKBOX, "Enable SetResourcePath:", ImageDecorator.IMG_CHECK_BOX_ON.getImage(), ImageDecorator.IMG_CHECK_BOX_OFF.getImage());
        checkbox.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((SecretKeyUiDecorator) this.uiDecorator).SETTING_POPUP_COMBO_ITEM_WIDTH);
        checkbox.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((SecretKeyUiDecorator) this.uiDecorator).SETTING_POPUP_COMBO_ITEM_HEIGHT);
        checkbox.setStyle(AbstractTnGraphics.RIGHT);
        checkbox.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_CHECK_BOX));
        int blackColor = UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_BL);
        checkbox.setForegroundColor(blackColor, blackColor);
        checkbox.setUnCheckableIcon(ImageDecorator.IMG_CHECK_BOX_DISABLE.getImage());
        boolean isSetResourcePathEnable = ((DaoManager) DaoManager.getInstance()).getSimpleConfigDao().getBoolean(SimpleConfigDao.KEY_SET_RESOURCE_PATH_ENABLE);
        checkbox.setSelected(isSetResourcePathEnable);
        checkbox.setTouchEventListener(this);
        setResourcePathEnableScreen.getContentContainer().add(checkbox);

        FrogButton saveBttn = UiFactory.getInstance().createButton(CMD_CHANGE_DONE, "Save");
        TnMenu menu = UiFactory.getInstance().createMenu();
        menu.add("", CMD_CHANGE_DONE);
        saveBttn.setMenu(menu, AbstractTnComponent.TYPE_CLICK);
        setResourcePathEnableScreen.getContentContainer().add(saveBttn);
        
        return setResourcePathEnableScreen;
    }
    
    private TnScreen createMapDownloadCNEnable(int state)
    {
        CitizenScreen mapDownloadCNEnable = UiFactory.getInstance().createScreen(state);

        final FrogLabel titleLabel = UiFactory.getInstance().createLabel(0, "Enable MapDownload CN");
        TnUiArgs args = titleLabel.getTnUiArgs();
        args.put(TnUiArgs.KEY_PREFER_WIDTH, this.uiDecorator.SCREEN_WIDTH);
        titleLabel.setStyle(AbstractTnGraphics.HCENTER);
        titleLabel.setFont(font);
        titleLabel.setForegroundColor(TnColor.WHITE, TnColor.WHITE);
        mapDownloadCNEnable.getTitleContainer().add(titleLabel);
        mapDownloadCNEnable.getTitleContainer().setBackgroundColor(TnColor.GRAY);
        
        checkbox = UiFactory.getInstance().createCitizenCheckItem(ID_LOGGER_ENABLED_CHECKBOX, "Enable MapDownload CN:", ImageDecorator.IMG_CHECK_BOX_ON.getImage(), ImageDecorator.IMG_CHECK_BOX_OFF.getImage());
        checkbox.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((SecretKeyUiDecorator) this.uiDecorator).SETTING_POPUP_COMBO_ITEM_WIDTH);
        checkbox.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((SecretKeyUiDecorator) this.uiDecorator).SETTING_POPUP_COMBO_ITEM_HEIGHT);
        checkbox.setStyle(AbstractTnGraphics.RIGHT);
        checkbox.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_CHECK_BOX));
        int blackColor = UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_BL);
        checkbox.setForegroundColor(blackColor, blackColor);
        checkbox.setUnCheckableIcon(ImageDecorator.IMG_CHECK_BOX_DISABLE.getImage());
        boolean isMapDownloadCNEnabled = ((DaoManager) DaoManager.getInstance()).getSimpleConfigDao().getBoolean(SimpleConfigDao.KEY_SET_MAP_DOWNLOAD_CN_ENABLE);
        checkbox.setSelected(isMapDownloadCNEnabled);
        checkbox.setTouchEventListener(this);
        mapDownloadCNEnable.getContentContainer().add(checkbox);
        
        String cnUrl = DaoManager.getInstance().getSimpleConfigDao().getString(SimpleConfigDao.KEY_SET_MAP_DOWNLOAD_CN_URL);
        
        if(cnUrl == null || cnUrl.trim().length() == 0)
        {
            DaoManager.getInstance().getSimpleConfigDao().put(SimpleConfigDao.KEY_SET_MAP_DOWNLOAD_CN_URL, "172.16.102.47");
            DaoManager.getInstance().getSimpleConfigDao().store();
            cnUrl = "172.16.102.47";
        }
        
        String init = cnUrl;
        
        textField = UiFactory.getInstance().createTextField(init, 100, 0, "", ID_MAP_DOWNLOAD_CN_IP_INPUT);
        textField.setEnabled(isMapDownloadCNEnabled);
        mapDownloadCNEnable.getContentContainer().add(textField);

        FrogButton saveBttn = UiFactory.getInstance().createButton(CMD_CHANGE_DONE, "Save");
        TnMenu menu = UiFactory.getInstance().createMenu();
        menu.add("", CMD_CHANGE_DONE);
        saveBttn.setMenu(menu, AbstractTnComponent.TYPE_CLICK);
        mapDownloadCNEnable.getContentContainer().add(saveBttn);
        
        return mapDownloadCNEnable;
    }
    
    private TnScreen createStuckMonitorScreen(int state)
    {
        CitizenScreen stuckMonitorScreen = UiFactory.getInstance().createScreen(state);

        final FrogLabel titleLabel = UiFactory.getInstance().createLabel(0, "Enable Stuck Monitor");
        TnUiArgs args = titleLabel.getTnUiArgs();
        args.put(TnUiArgs.KEY_PREFER_WIDTH, this.uiDecorator.SCREEN_WIDTH);
        titleLabel.setStyle(AbstractTnGraphics.HCENTER);
        titleLabel.setFont(font);
        titleLabel.setForegroundColor(TnColor.WHITE, TnColor.WHITE);
        stuckMonitorScreen.getTitleContainer().add(titleLabel);
        stuckMonitorScreen.getTitleContainer().setBackgroundColor(TnColor.GRAY);
        
        checkbox = UiFactory.getInstance().createCitizenCheckItem(ID_LOGGER_ENABLED_CHECKBOX, "Enable Stuck Monitor:", ImageDecorator.IMG_CHECK_BOX_ON.getImage(), ImageDecorator.IMG_CHECK_BOX_OFF.getImage());
        checkbox.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((SecretKeyUiDecorator) this.uiDecorator).SETTING_POPUP_COMBO_ITEM_WIDTH);
        checkbox.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((SecretKeyUiDecorator) this.uiDecorator).SETTING_POPUP_COMBO_ITEM_HEIGHT);
        checkbox.setStyle(AbstractTnGraphics.RIGHT);
        checkbox.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_CHECK_BOX));
        int blackColor = UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_BL);
        checkbox.setForegroundColor(blackColor, blackColor);
        checkbox.setUnCheckableIcon(ImageDecorator.IMG_CHECK_BOX_DISABLE.getImage());
        boolean isStuckMonitorEnabled = ((DaoManager) DaoManager.getInstance()).getSimpleConfigDao().getBoolean(SimpleConfigDao.KEY_SET_STUCK_MONITOR_ENABLE);
        checkbox.setSelected(isStuckMonitorEnabled);
        checkbox.setTouchEventListener(this);
        stuckMonitorScreen.getContentContainer().add(checkbox);

        FrogButton saveBttn = UiFactory.getInstance().createButton(CMD_CHANGE_DONE, "Save");
        TnMenu menu = UiFactory.getInstance().createMenu();
        menu.add("", CMD_CHANGE_DONE);
        saveBttn.setMenu(menu, AbstractTnComponent.TYPE_CLICK);
        stuckMonitorScreen.getContentContainer().add(saveBttn);
        
        return stuckMonitorScreen;
    }
    
    private TnScreen createMapDiskCacheScreen(int state)
    {
        CitizenScreen mapDiskCacheScreen = UiFactory.getInstance().createScreen(state);

        final FrogLabel titleLabel = UiFactory.getInstance().createLabel(0, "Disable Map Disk Cache");
        TnUiArgs args = titleLabel.getTnUiArgs();
        args.put(TnUiArgs.KEY_PREFER_WIDTH, this.uiDecorator.SCREEN_WIDTH);
        titleLabel.setStyle(AbstractTnGraphics.HCENTER);
        titleLabel.setFont(font);
        titleLabel.setForegroundColor(TnColor.WHITE, TnColor.WHITE);
        mapDiskCacheScreen.getTitleContainer().add(titleLabel);
        mapDiskCacheScreen.getTitleContainer().setBackgroundColor(TnColor.GRAY);
        
        checkbox = UiFactory.getInstance().createCitizenCheckItem(ID_LOGGER_ENABLED_CHECKBOX, "Disable Map Disk Cache:", ImageDecorator.IMG_CHECK_BOX_ON.getImage(), ImageDecorator.IMG_CHECK_BOX_OFF.getImage());
        checkbox.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((SecretKeyUiDecorator) this.uiDecorator).SETTING_POPUP_COMBO_ITEM_WIDTH);
        checkbox.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((SecretKeyUiDecorator) this.uiDecorator).SETTING_POPUP_COMBO_ITEM_HEIGHT);
        checkbox.setStyle(AbstractTnGraphics.RIGHT);
        checkbox.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_CHECK_BOX));
        int blackColor = UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_BL);
        checkbox.setForegroundColor(blackColor, blackColor);
        checkbox.setUnCheckableIcon(ImageDecorator.IMG_CHECK_BOX_DISABLE.getImage());
        boolean isMapDiskCacheDisabled = ((DaoManager) DaoManager.getInstance()).getSimpleConfigDao().getBoolean(SimpleConfigDao.KEY_SET_MAP_DISK_CACHE_DISABLE);
        checkbox.setSelected(isMapDiskCacheDisabled);
        checkbox.setTouchEventListener(this);
        mapDiskCacheScreen.getContentContainer().add(checkbox);

        FrogButton saveBttn = UiFactory.getInstance().createButton(CMD_CHANGE_DONE, "Save");
        TnMenu menu = UiFactory.getInstance().createMenu();
        menu.add("", CMD_CHANGE_DONE);
        saveBttn.setMenu(menu, AbstractTnComponent.TYPE_CLICK);
        mapDiskCacheScreen.getContentContainer().add(saveBttn);
        
        return mapDiskCacheScreen;
    }

    private TnScreen createShowSatelliteDuringNav(int state)
    {
        CitizenScreen screen = UiFactory.getInstance().createScreen(state);

        final FrogLabel titleLabel = UiFactory.getInstance().createLabel(0, "Show Satellite During Nav");
        TnUiArgs args = titleLabel.getTnUiArgs();
        args.put(TnUiArgs.KEY_PREFER_WIDTH, this.uiDecorator.SCREEN_WIDTH);
        titleLabel.setStyle(AbstractTnGraphics.HCENTER);
        titleLabel.setFont(font);
        titleLabel.setForegroundColor(TnColor.WHITE, TnColor.WHITE);
        screen.getTitleContainer().add(titleLabel);
        screen.getTitleContainer().setBackgroundColor(TnColor.GRAY);
        
        showSatelliteDuringNavCheckBox = UiFactory.getInstance().createCitizenCheckItem(ID_SHOW_SAT_DURING_NAV, "Show Satellite During Nav:", ImageDecorator.IMG_CHECK_BOX_ON.getImage(), ImageDecorator.IMG_CHECK_BOX_OFF.getImage());
        showSatelliteDuringNavCheckBox.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((SecretKeyUiDecorator) this.uiDecorator).SETTING_POPUP_COMBO_ITEM_WIDTH);
        showSatelliteDuringNavCheckBox.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((SecretKeyUiDecorator) this.uiDecorator).SETTING_POPUP_COMBO_ITEM_HEIGHT);
        showSatelliteDuringNavCheckBox.setStyle(AbstractTnGraphics.RIGHT);
        showSatelliteDuringNavCheckBox.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_CHECK_BOX));
        int blackColor = UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_BL);
        showSatelliteDuringNavCheckBox.setForegroundColor(blackColor, blackColor);
        showSatelliteDuringNavCheckBox.setUnCheckableIcon(ImageDecorator.IMG_CHECK_BOX_DISABLE.getImage());
        boolean isShowSatDuingNav = ((DaoManager) DaoManager.getInstance()).getSimpleConfigDao().getBoolean(SimpleConfigDao.KEY_DISABLE_ROUTE_IN_SATELLITE);
        showSatelliteDuringNavCheckBox.setSelected(isShowSatDuingNav);
        showSatelliteDuringNavCheckBox.setTouchEventListener(this);
        screen.getContentContainer().add(showSatelliteDuringNavCheckBox);
        return screen;
    }
    
    private TnScreen createSetDoneScreen(int state)
    {
        CitizenScreen setPtnDoneScreen = UiFactory.getInstance().createScreen(state);
        
        final FrogLabel titleLabel = UiFactory.getInstance().createLabel(0, model.getString(KEY_S_SET_DONE_TITLE));
        TnUiArgs args = titleLabel.getTnUiArgs();
        args.put(TnUiArgs.KEY_PREFER_WIDTH, this.uiDecorator.SCREEN_WIDTH);
        titleLabel.setStyle(AbstractTnGraphics.HCENTER);
        titleLabel.setFont(font);
        titleLabel.setForegroundColor(TnColor.WHITE, TnColor.WHITE);
        setPtnDoneScreen.getTitleContainer().add(titleLabel);
        setPtnDoneScreen.getTitleContainer().setBackgroundColor(TnColor.GRAY);
        
        FrogMultiLine multiLine = UiFactory.getInstance().createMultiLine(0, model.getString(KEY_S_SET_DONE_INFO_MSG));
        setPtnDoneScreen.getContentContainer().add(multiLine);
        setPtnDoneScreen.getContentContainer().setBackgroundColor(TnColor.WHITE);
        
        return setPtnDoneScreen;
    }


    private TnScreen createSetPtnScreen(int state)
    {
        CitizenScreen setPtnScreen = UiFactory.getInstance().createScreen(state);
        
        final FrogLabel titleLabel = UiFactory.getInstance().createLabel(0, "Set PTN");
        TnUiArgs args = titleLabel.getTnUiArgs();
        args.put(TnUiArgs.KEY_PREFER_WIDTH, this.uiDecorator.SCREEN_WIDTH);
        titleLabel.setStyle(AbstractTnGraphics.HCENTER);
        titleLabel.setFont(font);
        titleLabel.setForegroundColor(TnColor.WHITE, TnColor.WHITE);
        setPtnScreen.getTitleContainer().add(titleLabel);
        setPtnScreen.getTitleContainer().setBackgroundColor(TnColor.GRAY);
        
        StringBuffer msg = new StringBuffer();
        msg.append("Notes: if keep null and save, it will clean PTN stored before");
        msg.append(" \n");
        msg.append("<bold><red>if you have login, please clear RMS firstly, then retype for safe</red></bold>");
        FrogMultiLine multiLine = UiFactory.getInstance().createMultiLine(0, msg.toString());
        setPtnScreen.getContentContainer().add(multiLine);
        setPtnScreen.getContentContainer().setBackgroundColor(TnColor.WHITE);
        
        String ptn = DaoManager.getInstance().getBillingAccountDao().getPtn();
        if(ptn == null)
            ptn = "";
        
        textField = UiFactory.getInstance().createTextField(ptn, -1, FrogTextField.NUMERIC,  "", 0);
        setPtnScreen.getContentContainer().add(textField);
        setPtnScreen.getContentContainer().setBackgroundColor(TnColor.WHITE);
        
        FrogButton saveBttn = UiFactory.getInstance().createButton(CMD_CHANGE_DONE, "Save");
        TnMenu menu = UiFactory.getInstance().createMenu();
        menu.add("", CMD_CHANGE_DONE);
        saveBttn.setMenu(menu, AbstractTnComponent.TYPE_CLICK);
        setPtnScreen.getContentContainer().add(saveBttn);
        
        return setPtnScreen;
    }


    protected boolean updateScreen(int state, TnScreen preScreen)
    {
        TnScreen currentScreen = ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getCurrentScreen();
        AbstractTnComponent dumpView;
        AbstractTnComponent removeOutput;
        switch(state)
        {
            case STATE_SHOW_DIRECTORY:
            {
                FrogList directory = (FrogList) currentScreen.getComponentById(ID_DIRECORY_FROGLIST);
                String[] names = (String[]) this.model.get(KEY_ARRAY_FILE_NAMES);
                if (names != null && directory !=null)
                {
                    int size = names.length;
                    int[] action = new int[size];
                    for (int start = 0; start < size; start++)
                    {
                        action[start] = CMD_SHOW_DIRECTORY;
                    }
                    directory.setAdapter(new DefaultListAdapter(names, action));
                }
                dumpView = currentScreen.getComponentById(ID_DUMP_FILE_VIEW);
                removeOutput = currentScreen.getComponentById(ID_REMOVE_OUTPUT);
                if (!this.model.fetchBool(KEY_B_NEED_KEEP_BUTTON))
                {
                    if (dumpView != null)
                    {
                        ((CitizenScreen) currentScreen).getContentContainer().remove(dumpView);
                    }
                    if (removeOutput != null)
                    {
                        ((CitizenScreen) currentScreen).getContentContainer().remove(removeOutput);
                    }
                }
                return true;
            }
            case STATE_SHOW_REGION:
            {
                FrogTextField latView,lonView;
                latView = (FrogTextField)currentScreen.getComponentById(ID_REGION_LAT);
                lonView = (FrogTextField)currentScreen.getComponentById(ID_REGION_LON);
                String latStr = this.model.getString(KEY_S_REGION_LAT);
                String lonStr = this.model.getString(KEY_S_REGION_LON);
                latView.setText(latStr + "");
                lonView.setText(lonStr + "");
                FrogLabel lable = (FrogLabel)currentScreen.getComponentById(ID_REGION_LABLE);
                String selectedCity = this.model.getString(KEY_S_SELECTED_LABLE);
                lable.setText(selectedCity);
                return true;
            }
        }
        return false;
    }

    protected boolean updatePopup(int state, TnPopupContainer popup)
    {
        return false;
    }
    
    protected int transformCommandDelegate(int state, TnUiEvent event)
    {
        int cmd = CMD_NONE;
        switch (state)
        {
            case STATE_SET_ALIAS_URL:
            {
                if (event.getType() == TnUiEvent.TYPE_KEY_EVENT && event.getKeyEvent().getCode() == TnKeyEvent.KEYCODE_BACK)
                {
                    TnLinearContainer container = (TnLinearContainer) urlContainer.get();
                    int childrenSize = container.getChildrenSize();
                    
                    StringMap aliasUrlMap = DaoManager.getInstance().getServiceLocatorDao().getAliasUrlMap();
                    
                    StringMap secretAliasUrlMap = null;
                    for (int i = 0; i < childrenSize; i++)
                    {
                        TnLinearContainer textFieldContainrt = (TnLinearContainer) container.get(i);
                        if(textFieldContainrt.getChildrenSize() > 1 && textFieldContainrt.get(1) instanceof FrogTextField)
                        {
                            FrogTextField textField = (FrogTextField) textFieldContainrt.get(1);
                            String url = textField.getText();
                            
                            FrogLabel label = (FrogLabel) textFieldContainrt.get(0);
                            String alias = label.getText();
                            
                            if(!aliasUrlMap.get(alias.trim()).equalsIgnoreCase(url.trim()))
                            {
                                if(secretAliasUrlMap == null)
                                {
                                    secretAliasUrlMap = new StringMap();
                                }
                                
                                secretAliasUrlMap.put(alias, url);
                            }
                        }
                    }
                    
                    if(secretAliasUrlMap != null && secretAliasUrlMap.size() > 0)
                    {
                        DaoManager.getInstance().getServiceLocatorDao().setSecretAliasUrlMap(secretAliasUrlMap);
                        DaoManager.getInstance().getServiceLocatorDao().store();
                        
                        ServiceLocator serviceLocator = this.getServiceLocator();
                        serviceLocator.setSetretServiceLocator(secretAliasUrlMap, null);
                    }
                }
                break;
            }
            case STATE_SET_ACTION_URL:
            {
                if (event.getType() == TnUiEvent.TYPE_KEY_EVENT && event.getKeyEvent().getCode() == TnKeyEvent.KEYCODE_BACK)
                {
                    TnLinearContainer container = (TnLinearContainer) urlContainer.get();
                    int childrenSize = container.getChildrenSize();
                    
                    StringMap secretActionUrlMap = null;
                    for (int i = 0; i < childrenSize; i++)
                    {
                        TnLinearContainer textFieldContainrt = (TnLinearContainer) container.get(i);
                        if(textFieldContainrt.getChildrenSize() > 1 && textFieldContainrt.get(1) instanceof FrogTextField)
                        {
                            FrogTextField textField = (FrogTextField) textFieldContainrt.get(1);
                            String url = textField.getText();
                            
                            FrogLabel label = (FrogLabel) textFieldContainrt.get(0);
                            String action = label.getText();
                            
                            if(!getActioUrl(action).equalsIgnoreCase(url.trim()))
                            {
                                if(secretActionUrlMap == null)
                                {
                                    secretActionUrlMap = new StringMap();
                                }
                                
                                secretActionUrlMap.put(action, url);
                            }
                        }
                    }
                    
                    if(secretActionUrlMap != null && secretActionUrlMap.size() > 0)
                    {
                        DaoManager.getInstance().getServiceLocatorDao().setSecretActionUrlMap(secretActionUrlMap);
                        DaoManager.getInstance().getServiceLocatorDao().store();
                        
                        ServiceLocator serviceLocator = this.getServiceLocator();
                        serviceLocator.setSetretServiceLocator(null, secretActionUrlMap);
                    }
                }
                break;
            }
        }
        return cmd;
    }

    protected boolean prepareModelData(int state, int commandId)
    {

        switch (state)
        {
            case STATE_CHANGE_CELL:
            {
                if (commandId == CMD_CHANGE_DONE)
                {
                    if (textField != null)
                    {
                        int type = Integer.parseInt(textField.getText());
                        type = 7; //jy for test
                        this.model.put(KEY_INT_LOCATION_CELL, type);
                    }
                }
                break;
            }
            case STATE_SET_PTN:
            {
                if (commandId == CMD_CHANGE_DONE)
                {
                    if (textField != null)
                    {
                        this.model.put(KEY_S_PTN, textField.getText());
                    }
                }
                break;
            }
            case STATE_SET_MVIEWER_IP:
            {
                if (commandId == CMD_CHANGE_DONE)
                {
                    if (textField != null)
                    {
                        this.model.put(KEY_S_MVIEWER_IP, textField.getText());
                    }
                }
                break;
            }
            case STATE_SET_ALONGROUTE_SPEED:
            {
                if (commandId == CMD_CHANGE_DONE)
                {
                    if (speedTextField != null)
                    {
                        this.model.put(KEY_S_ALONGROUTE_SPEED, speedTextField.getText());
                    }
                }
                break;
            }
            case STATE_SET_MAP_DATASET:
            {
                if (commandId == CMD_CHANGE_DONE)
                {
                    if (textField != null)
                    {
                        this.model.put(KEY_S_NEW_DATASET, textField.getText());
                    }
                }
                break;
            }
            case STATE_DWF_SEND_SETTING:
            {
                if (commandId == CMD_CHANGE_DONE)
                {
                    if (checkbox != null)
                    {
                        this.model.put(KEY_B_SET_LOGGER, checkbox.isSelected());
                    }
                    if (sdCardCheckBox != null)
                    {
                        this.model.put(KEY_B_SET_SDCARD_OUTPUT_LOGGER, sdCardCheckBox.isSelected());
                    }
                }
                break;
            }
            case STATE_SET_LOGGER:
            {
                if (commandId == CMD_CHANGE_DONE)
                {
                    if (checkbox != null)
                    {
                        this.model.put(KEY_B_SET_LOGGER, checkbox.isSelected());
                    }
                    if (sdCardCheckBox != null)
                    {
                        this.model.put(KEY_B_SET_SDCARD_OUTPUT_LOGGER, sdCardCheckBox.isSelected());
                    }
                    if (localMisLogCheckBox != null)
                    {
                        this.model.put(KEY_B_LOCAL_MISLOG_LOGGER, localMisLogCheckBox.isSelected());
                    }
                    if (runtimeStatusCheckBox != null)
                    {
                        this.model.put(KEY_B_RUNTIME_STATUS_ENABLE, runtimeStatusCheckBox.isSelected());
                    }
                    if (textField != null)
                    {
                        this.model.put(KEY_S_RUNTIME_STATUS_INTERVAL, textField.getText());
                    }
                }
				break;
            }
            case STATE_SET_RESOURCE_PATH_ENABLE:
            {
                if (commandId == CMD_CHANGE_DONE)
                {
                    if (checkbox != null)
                    {
                        this.model.put(KEY_B_SET_RESOURCE_PATH_ENABLE, checkbox.isSelected());
                    }
                }
                break;
            }
            case STATE_SWITCH_AIRPLANE_MODE:
            {
                if (commandId == CMD_CHANGE_DONE)
                {
                    if (checkbox != null)
                    {
                        this.model.put(KEY_B_SWITCH_AIRPLANE_MODE_ENABLE, checkbox.isSelected());
                    }
                }
                break;
            }
            case STATE_SET_BILLBOARD:
            {
                if (commandId == CMD_CHANGE_DONE)
                {
                    if (textField != null)
                    {
                        this.model.put(KEY_S_BILLBOARD_HOST, textField.getText());
                    }
                }
                break;
            }
            case STATE_SHOW_REGION:
            {
                if (commandId == CMD_SET_REGION)
                {
                    TnScreen currentScreen = ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getCurrentScreen();
                    FrogTextField latView,lonView;
                    latView = (FrogTextField)currentScreen.getComponentById(ID_REGION_LAT);
                    lonView = (FrogTextField)currentScreen.getComponentById(ID_REGION_LON);
                    if (latView != null)
                    {
                        this.model.put(KEY_S_REGION_LAT, latView.getText());
                    }
                    if (lonView != null)
                    {
                        this.model.put(KEY_S_REGION_LON, lonView.getText());
                    }
                }
                break;
            }
            case STATE_SHOW_SET_URL_SCREEN:
            {
                if (commandId == CMD_COMMON_OK)
                {
                    if (textField != null)
                    {
                        this.model.put(KEY_S_MANUAL_SERVER_URL, textField.getText());
                    }
                }
                break;
            }
            case STATE_MAP_DOWNLOAD_CN:
            {
                if (commandId == CMD_CHANGE_DONE)
                {
                    if (checkbox != null)
                    {
                        this.model.put(KEY_B_SET_MAPDOWNLOAD_CN_ENABLE, checkbox.isSelected());
                    }
                    
                    if (textField != null)
                    {
                        String text = textField.getText();
                        
                        this.model.put(KEY_S_SET_MAP_DOWNLOAD_CN_URL, text);
                    }
                }
                break;
            }
            case STATE_STUCK_MONITOR:
            {
                if (commandId == CMD_CHANGE_DONE)
                {
                    if (checkbox != null)
                    {
                        this.model.put(KEY_B_SET_STUCK_MONITOR_ENABLE, checkbox.isSelected());
                    }
                }
                break;
            }
            case STATE_MAP_DISK_CACHE:
            {
                if (commandId == CMD_CHANGE_DONE)
                {
                    if (checkbox != null)
                    {
                        this.model.put(KEY_B_SET_MAP_DISK_CACHE_DISABLE, checkbox.isSelected());
                    }
                }
                break;
            }
        }

        return super.prepareModelData(state, commandId);
    }
    
    protected boolean preProcessUIEvent(TnUiEvent tnUiEvent)
    {
        int state = model.getState();
        
        switch (state)
        {
            case STATE_URL_GROUP:
            {
                if (tnUiEvent.getType() == TnUiEvent.TYPE_COMMAND_EVENT)
                {
                    int commandId = tnUiEvent.getComponent().getId();
                    CommHostDao commHostDao = ((DaoManager) DaoManager.getInstance()).getCommHostDao();
                    int oldIndex = commHostDao.getDefaultSelectedIndex();
                    if (commandId != oldIndex && commandId != -1)
                    {
                        this.model.put(KEY_B_GET_LOCATOR, true); // change URL group, need request from server
                        this.model.put(KEY_I_SELECTHOST_INDEX, commandId);
                    }
                    else
                    {
                    	this.model.put(KEY_B_GET_LOCATOR, false); // not change URL group
                    }
                }
                break;
            }
            case STATE_GPS:
            {
                if (tnUiEvent.getType() == TnUiEvent.TYPE_COMMAND_EVENT)
                {
                    int commandId = tnUiEvent.getComponent().getId();
                    ((DaoManager) DaoManager.getInstance()).getSecretSettingDao().put(SecretSettingDao.GPS_SOURCE_SELECT_INDEX, transferGpsIndexToSource(commandId));
                    ((DaoManager) DaoManager.getInstance()).getSecretSettingDao().store();
                }
                break;
            }
            case STATE_DATASOURCE_MODE:
            {
                if (tnUiEvent.getType() == TnUiEvent.TYPE_COMMAND_EVENT)
                {
                    int commandId = tnUiEvent.getComponent().getId();
                    ((DaoManager) DaoManager.getInstance()).getSecretSettingDao().put(SecretSettingDao.ONBOARD_MODE_INDEX, transferDataSourceModeIndexToSource(commandId));
                    ((DaoManager) DaoManager.getInstance()).getSecretSettingDao().store();
                }
                break;
            }
            case STATE_SHOW_REGION:
            {
                if (tnUiEvent.getType() == TnUiEvent.TYPE_COMMAND_EVENT)
                {
                    int commandId = tnUiEvent.getComponent().getId();
                    String[] labels = (String[]) this.model.get(KEY_ARRAY_REGION_LABLES);
                    if (commandId >= 0 && commandId < labels.length)
                    {
                        String key = labels[commandId];
                        
                        Hashtable defaultRegions = (Hashtable) this.model.get(KEY_O_DEFAULT_REGIONS);
                        TnLocation tnLocation = (TnLocation) defaultRegions.get(key);
                        int latStr = tnLocation.getLatitude();
                        int lonStr = tnLocation.getLongitude();
                        
                        this.model.put(KEY_S_SELECTED_LABLE, key);
                        this.model.put(KEY_S_REGION_LAT, latStr + "");
                        this.model.put(KEY_S_REGION_LON, lonStr + "");
                    }
                }
                break;
            }
            case STATE_NETWORK:
            {
                if (tnUiEvent.getType() == TnUiEvent.TYPE_COMMAND_EVENT)
                {
                    int commandId = tnUiEvent.getComponent().getId();
                    ((DaoManager) DaoManager.getInstance()).getSimpleConfigDao().put(SimpleConfigDao.KEY_NETWORK_SELECTED_INDEX, transferNetworkIndexToSource(commandId));
                    ((DaoManager) DaoManager.getInstance()).getSimpleConfigDao().store();
                }
                break;
            }
            case STATE_DWF_SEND_SETTING:
            {
                if (tnUiEvent.getType() == TnUiEvent.TYPE_TOUCH_EVENT)
                {
                    AbstractTnComponent component = tnUiEvent.getComponent();
                    TnMotionEvent event = tnUiEvent.getMotionEvent();
                    if (component != null && event.getAction() == TnMotionEvent.ACTION_UP)
                    {
                        int id = component.getId();
                        if (id == ID_DWF_GCM_ENABLE)
                        {
                            if (!checkbox.isSelected())
                            {
                                sdCardCheckBox.setEnabled(false);
                                sdCardCheckBox.setSelected(false);
                            }
                            else 
                            {
                                sdCardCheckBox.setEnabled(true);
                            }
        
                        }
                    }
                }
            }
            case STATE_SET_LOGGER:
            {
                if (tnUiEvent.getType() == TnUiEvent.TYPE_TOUCH_EVENT)
                {
                    AbstractTnComponent component = tnUiEvent.getComponent();
                    TnMotionEvent event = tnUiEvent.getMotionEvent();
                    if (component != null && event.getAction() == TnMotionEvent.ACTION_UP)
                    {
                        int id = component.getId();
                        if (id == ID_LOGGER_ENABLED_CHECKBOX)
                        {
                            if (!checkbox.isSelected())
                            {
                                sdCardCheckBox.setEnabled(false);
                                sdCardCheckBox.setSelected(false);
                                ((DaoManager) DaoManager.getInstance()).getSimpleConfigDao().put(SimpleConfigDao.KEY_SDCARD_OUTPUT_LOGGER_ENABLE, 0);
                                ((DaoManager) DaoManager.getInstance()).getSimpleConfigDao().store();
                                
                                localMisLogCheckBox.setEnabled(false);
                                localMisLogCheckBox.setSelected(false);
                                ((DaoManager) DaoManager.getInstance()).getSimpleConfigDao().put(SimpleConfigDao.KEY_LOCAL_MISLOG_ENABLE, 0);
                                ((DaoManager) DaoManager.getInstance()).getSimpleConfigDao().store();
                                
                                runtimeStatusCheckBox.setEnabled(false);
                                runtimeStatusCheckBox.setSelected(false);
                                ((DaoManager) DaoManager.getInstance()).getSimpleConfigDao().put(SimpleConfigDao.KEY_RUNTIME_STATUS_LOG_ENABLE, 0);
                                ((DaoManager) DaoManager.getInstance()).getSimpleConfigDao().store();
                                
                                textField.setEnabled(false);

                            }
                            else 
                            {
                                sdCardCheckBox.setEnabled(true);
                                runtimeStatusCheckBox.setEnabled(true);
                                localMisLogCheckBox.setEnabled(true);
                                runtimeStatusCheckBox.setSelected(false);
                                ((DaoManager) DaoManager.getInstance()).getSimpleConfigDao().put(SimpleConfigDao.KEY_RUNTIME_STATUS_LOG_ENABLE, 0);
                                ((DaoManager) DaoManager.getInstance()).getSimpleConfigDao().store();
                                
                                textField.setEnabled(false);
                            }

                        }
                        else if(id == ID_RUNTIME_STATUS_CHECKBOX)
                        {
                            if(!runtimeStatusCheckBox.isSelected())
                            {
                                textField.setEnabled(false);
                            }
                            else
                            {
                                textField.setEnabled(true);
                            }
                        }
                        else if (id == ID_DWF_GCM_ENABLE)
                        {
                            if (!checkbox.isSelected())
                            {
                                sdCardCheckBox.setEnabled(false);
                                sdCardCheckBox.setSelected(false);
                            }
                            else 
                            {
                                sdCardCheckBox.setEnabled(true);
                                sdCardCheckBox.setSelected(false);
                            }

                        }
                    }
                }
                break;
            }
            case STATE_MAP_DOWNLOAD_CN:
            {
                if (tnUiEvent.getType() == TnUiEvent.TYPE_TOUCH_EVENT)
                {
                    AbstractTnComponent component = tnUiEvent.getComponent();
                    TnMotionEvent event = tnUiEvent.getMotionEvent();
                    if (component != null && event.getAction() == TnMotionEvent.ACTION_UP)
                    {
                        int id = component.getId();
                        if (id == ID_LOGGER_ENABLED_CHECKBOX)
                        {
                            if (!checkbox.isSelected())
                            {
                                textField.setEnabled(false);
                            }
                            else 
                            {
                                textField.setEnabled(true);
                            }
                        }
                    }
                }
                break;
            }
            case STATE_SHOW_DIRECTORY:
            case STATE_SHOW_FILE_CONTENT:
            case STATE_DUMP_LOCAL_FILE:
            {
                if (tnUiEvent.getType() == TnUiEvent.TYPE_COMMAND_EVENT)
                {
                    int commandId = tnUiEvent.getComponent().getId();
                    String[] paths = (String[]) this.model.get(KEY_ARRAY_FILE_PATHS);
                    int size = paths!=null?paths.length:-1;
                    if (commandId < size)
                    {
                        this.model.put(KEY_S_SELECTED_FILE_PATH, paths[commandId]);
                    }
                }
                break;
            }
            case STATE_KONTAGENT_INFO:
            {
                if (tnUiEvent.getType() == TnUiEvent.TYPE_TOUCH_EVENT)
                {
                    AbstractTnComponent component = tnUiEvent.getComponent();
                    TnMotionEvent event = tnUiEvent.getMotionEvent();
                    if (component != null && event.getAction() == TnMotionEvent.ACTION_UP)
                    {
                        int id = component.getId();
                        if (id == ID_KONTAGENT_LOGGER_ENABLED_GENERATE_FILE_LOG)
                        {
                            generateKTLogFileCheckBox.setEnabled(true);
                            int enableGenerateFile = generateKTLogFileCheckBox.isSelected() ? 1:0;
                            ((DaoManager) DaoManager.getInstance()).getSimpleConfigDao().put(SimpleConfigDao.KEY_KONTAGENT_GENERATE_EVENT_LOG_FILE, enableGenerateFile);
                            ((DaoManager) DaoManager.getInstance()).getSimpleConfigDao().store();
                        }
                        else if(id == ID_KONTAGENT_USING_TEST_SERVER)
                        {
                            usingTestServerCheckBox.setEnabled(true);
                            int enableTestServer = usingTestServerCheckBox.isSelected() ? 1:0;
                            ((DaoManager) DaoManager.getInstance()).getSimpleConfigDao().put(SimpleConfigDao.KEY_USING_KONTAGENT_TEST_SERVER, enableTestServer);
                            ((DaoManager) DaoManager.getInstance()).getSimpleConfigDao().store();
                        }
                    }
                    
                }
                break;
            }
            case STATE_SHOW_SATELLITE_DURING_NAV:
            {
                if (tnUiEvent.getType() == TnUiEvent.TYPE_TOUCH_EVENT)
                {
                    AbstractTnComponent component = tnUiEvent.getComponent();
                    TnMotionEvent event = tnUiEvent.getMotionEvent();
                    if (component != null && event.getAction() == TnMotionEvent.ACTION_UP)
                    {
                        int id = component.getId();
                        if (id == ID_SHOW_SAT_DURING_NAV)
                        {
                            showSatelliteDuringNavCheckBox.setEnabled(true);
                            int isShowSatDuringNav = showSatelliteDuringNavCheckBox.isSelected() ? 1:0;
                            ((DaoManager) DaoManager.getInstance()).getSimpleConfigDao().put(SimpleConfigDao.KEY_DISABLE_ROUTE_IN_SATELLITE, isShowSatDuringNav);
                            ((DaoManager) DaoManager.getInstance()).getSimpleConfigDao().store();
                        }
                    }
                }
                break;
            }
        }
        return super.preProcessUIEvent(tnUiEvent);
    }
    
    
    private TnScreen createMainListScreen(int state)
    {
        
        CitizenScreen mainListScreen = UiFactory.getInstance().createScreen(state);
        
        removeMenuById(mainListScreen, ICommonConstants.CMD_COMMON_PREFERENCE);
        
        final FrogLabel titleLabel = UiFactory.getInstance().createLabel(0, "Secret Functions");
        TnUiArgs args = titleLabel.getTnUiArgs();
        args.put(TnUiArgs.KEY_PREFER_WIDTH, this.uiDecorator.SCREEN_WIDTH);
        titleLabel.setStyle(AbstractTnGraphics.HCENTER);
        titleLabel.setFont(font);
        titleLabel.setForegroundColor(TnColor.WHITE, TnColor.WHITE);
        mainListScreen.getTitleContainer().add(titleLabel);
        mainListScreen.getTitleContainer().setBackgroundColor(TnColor.GRAY);
        
        FrogList mainList = UiFactory.getInstance().createList(0);
        mainListScreen.getContentContainer().add(mainList);
        
        String[] labels = (String[])this.model.get(KEY_ARRAY_MAIN_LABELS);
        int[] actions = (int[])this.model.get(KEY_ARRAY_MAIN_ACTIONS);
        mainList.setAdapter(new DefaultListAdapter(labels, actions));
        
        return mainListScreen;
    }
    
    private TnScreen createUrlGroupScreen(int state)
    {
        CitizenScreen urlGroupScreen = UiFactory.getInstance().createScreen(state);
        final FrogLabel titleLabel = UiFactory.getInstance().createLabel(0, "Url Group List");
        TnUiArgs args = titleLabel.getTnUiArgs();
        args.put(TnUiArgs.KEY_PREFER_WIDTH, this.uiDecorator.SCREEN_WIDTH);
        titleLabel.setStyle(AbstractTnGraphics.HCENTER);
        titleLabel.setFont(font);
        titleLabel.setForegroundColor(TnColor.WHITE, TnColor.WHITE);
        urlGroupScreen.getTitleContainer().add(titleLabel);
        urlGroupScreen.getTitleContainer().setBackgroundColor(TnColor.GRAY);
        
        FrogList urlGroupList = UiFactory.getInstance().createList(0);
        urlGroupScreen.getContentContainer().add(urlGroupList);
        
        String[] labels = (String[])this.model.get(KEY_ARRAY_URL_GROUP_LABELS);
        int[] actions = new int[labels.length];
        for(int i = 0; i < actions.length - 1; i++)
        {
            actions[i] = CMD_COMMON_OK;
        }
        actions[actions.length - 1] = CMD_SHOW_SET_URL_SCREEN;
        int index = ((DaoManager)DaoManager.getInstance()).getCommHostDao().getDefaultSelectedIndex();
        urlGroupList.setAdapter(new DefaultListAdapter(labels, actions, index));
        
        return urlGroupScreen;
    }
    
    private boolean isInStringList(String s, String[] list)
    {
        if (s == null || list == null)
            return false;
        
        for (int i=0; i<list.length; i++)
        {
            if (s.equals(list[i]))
                return true;
        }
        return false;
    }
    
    private ServiceLocator getServiceLocator()
    {
        Comm comm = CommManager.getInstance().getComm();
        HostProvider hostProvider = comm.getHostProvider();
        ServiceLocator serviceLocator = (ServiceLocator)hostProvider;
        return serviceLocator;
    }
    
    private String getActioUrl(String action)
    {
        if(action == null || action.length() == 0)
            return null;
        
        ServiceLocator serviceLocator = getServiceLocator();
        Host host = serviceLocator.createHost(action);
        BrowserSessionArgs args = new BrowserSessionArgs(host);
        return args.getUrl();
    }
    
    private TnScreen createChangeHostScreen(int state)
    {
        boolean needRequest = model.fetchBool(KEY_B_GET_LOCATOR);
        String titleStr = "Host List";
        if (needRequest)
        {
            titleStr = titleStr + FrogTextHelper.RED_COLOR_START
                    + " ****** (RMS has been cleared)" + FrogTextHelper.RED_COLOR_END;
        }

        CitizenScreen changeGpsScreen = UiFactory.getInstance().createScreen(state);
        final FrogLabel titleLabel = UiFactory.getInstance().createLabel(0, titleStr);
        TnUiArgs args = titleLabel.getTnUiArgs();
        args.put(TnUiArgs.KEY_PREFER_WIDTH, this.uiDecorator.SCREEN_WIDTH);
        titleLabel.setStyle(AbstractTnGraphics.HCENTER);
        titleLabel.setFont(font);
        titleLabel.setForegroundColor(TnColor.WHITE, TnColor.WHITE);
        changeGpsScreen.getTitleContainer().add(titleLabel);
        changeGpsScreen.getTitleContainer().setBackgroundColor(TnColor.GRAY);

        FrogList gpsList = UiFactory.getInstance().createList(0);
        changeGpsScreen.getContentContainer().add(gpsList);
        
        int[] action = new int[]{CMD_ALIAS_URL_SETTING, CMD_ACTION_URL_SETTING};

        String[] labels = new String[]{"Set Server Alias URL (all actions bundle with it will be updated)", "Set Single Action URL (only specific action's url will be updated)"};
        gpsList.setAdapter(new DefaultListAdapter(labels, action, 0));

        return changeGpsScreen;
    }
    
    private TnScreen createSetAliasUrlScreen(int state)
    {
        CitizenScreen hostListScreen = UiFactory.getInstance().createScreen(state);
        final FrogLabel titleLabel = UiFactory.getInstance().createLabel(0, "ChangeHost");
        TnUiArgs args = titleLabel.getTnUiArgs();
        args.put(TnUiArgs.KEY_PREFER_WIDTH, this.uiDecorator.SCREEN_WIDTH);
        titleLabel.setStyle(AbstractTnGraphics.HCENTER);
        titleLabel.setFont(font);
        titleLabel.setForegroundColor(TnColor.WHITE, TnColor.WHITE);
        hostListScreen.getTitleContainer().add(titleLabel);
        hostListScreen.getTitleContainer().setBackgroundColor(TnColor.GRAY);

        urlContainer = UiFactory.getInstance().createScrollPanel(0, true);
        TnLinearContainer container = UiFactory.getInstance().createLinearContainer(0,
            true);
        container.setBackgroundColor(TnColor.WHITE);
        
        StringMap aliasUrl = DaoManager.getInstance().getServiceLocatorDao().getAliasUrlMap();
        ServiceLocator serviceLocator = this.getServiceLocator();
        
        // add alias
        
        Enumeration keys = aliasUrl.keys();
        int i = 0;
        while (keys.hasMoreElements())
        {
            String alias = (String) keys.nextElement();
            String url = serviceLocator.getDomainUrl(alias);

            TnLinearContainer textFieldContainer = UiFactory.getInstance()
                    .createLinearContainer(0, false);
            FrogLabel label = UiFactory.getInstance().createLabel(i, alias);
            FrogTextField textField = UiFactory.getInstance().createTextField(url,
                -1, FrogTextField.ANY, "", i);
            textFieldContainer.add(label);
            textFieldContainer.add(textField);
            container.add(textFieldContainer);
            i ++;
        }
        urlContainer.set(container);
        hostListScreen.getContentContainer().add(urlContainer);

        TnLinearContainer mapContainer = UiFactory.getInstance().createLinearContainer(0,
            true);
        FrogNullField nullField = UiFactory.getInstance().createNullField(0);
        TnUiArgs containerArgs = nullField.getTnUiArgs();
        containerArgs.put(TnUiArgs.KEY_PREFER_HEIGHT, new TnUiArgAdapter(
                PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
                {
                    public Object decorate(TnUiArgAdapter args)
                    {
                        return PrimitiveTypeCache.valueOf(5);
                    }
                }));
        mapContainer.getTnUiArgs().copy(containerArgs);
        mapContainer.add(nullField);
        mapContainer.setBackgroundColor(TnColor.WHITE);
        hostListScreen.getContentContainer().add(mapContainer);

        FrogButton imageButton = UiFactory.getInstance().createButton(0, "Submit");

        TnMenu menu = UiFactory.getInstance().createMenu();
        menu.add("", CMD_COMMON_OK);
        imageButton.setMenu(menu, AbstractTnComponent.TYPE_CLICK);
        imageButton.setCommandEventListener(this);
        hostListScreen.getContentContainer().add(imageButton);

        return hostListScreen;
    }
    
    private TnScreen createSetActionUrlScreen(int state)
    {
        CitizenScreen hostListScreen = UiFactory.getInstance().createScreen(state);
        final FrogLabel titleLabel = UiFactory.getInstance().createLabel(0, "Set Specific Action URL");
        TnUiArgs args = titleLabel.getTnUiArgs();
        args.put(TnUiArgs.KEY_PREFER_WIDTH, this.uiDecorator.SCREEN_WIDTH);
        titleLabel.setStyle(AbstractTnGraphics.HCENTER);
        titleLabel.setFont(font);
        titleLabel.setForegroundColor(TnColor.WHITE, TnColor.WHITE);
        hostListScreen.getTitleContainer().add(titleLabel);
        hostListScreen.getTitleContainer().setBackgroundColor(TnColor.GRAY);
        
        urlContainer = UiFactory.getInstance().createScrollPanel(0, true);
        TnLinearContainer container = UiFactory.getInstance().createLinearContainer(0, true);
        container.setBackgroundColor(TnColor.WHITE);
        
        StringMap actionAlias = DaoManager.getInstance().getServiceLocatorDao().getActionAliasMap();
        
        int id = 0;
        //add most important actions first
        if (highPriorityActions != null)
        {
            for (int i = 0; i < highPriorityActions.length; i++)
            {
                Enumeration keys = actionAlias.keys();
                while (keys.hasMoreElements())
                {
                    String action = (String) keys.nextElement();
                    if (action.equals(highPriorityActions[i]))
                    {
                        TnLinearContainer textFieldContainer = UiFactory.getInstance().createLinearContainer(id ++, true);
                        FrogLabel label = UiFactory.getInstance().createLabel(id ++, action);
                        FrogTextField textField = UiFactory.getInstance().createTextField(getActioUrl(action), -1, FrogTextField.ANY,  "", id ++);
                        textFieldContainer.add(label);
                        textFieldContainer.add(textField);
                        container.add(textFieldContainer);
                    }
                }
            }
        }
        
        //add remain actions
        Enumeration keys = actionAlias.keys();
        while (keys.hasMoreElements())
        {
            String action = (String) keys.nextElement();
            if (isInStringList(action, highPriorityActions))
                continue;
            
            TnLinearContainer textFieldContainer = UiFactory.getInstance().createLinearContainer(id ++, true);
            FrogLabel label = UiFactory.getInstance().createLabel(id ++, action);
            FrogTextField textField = UiFactory.getInstance().createTextField(getActioUrl(action), -1, FrogTextField.ANY,  "", id ++);
            textFieldContainer.add(label);
            textFieldContainer.add(textField);
            container.add(textFieldContainer);
        
        }
        
        urlContainer.set(container);
        hostListScreen.getContentContainer().add(urlContainer);
        
        TnLinearContainer mapContainer = UiFactory.getInstance().createLinearContainer(0, true);
        FrogNullField nullField = UiFactory.getInstance().createNullField(0);
        TnUiArgs containerArgs = nullField.getTnUiArgs();
        containerArgs.put(TnUiArgs.KEY_PREFER_HEIGHT, new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
        {
            public Object decorate(TnUiArgAdapter args)
            {
                return PrimitiveTypeCache.valueOf(5);
            }
        }));
        mapContainer.getTnUiArgs().copy(containerArgs);
        mapContainer.add(nullField);
        mapContainer.setBackgroundColor(TnColor.WHITE);
        hostListScreen.getContentContainer().add(mapContainer);
        
        FrogButton imageButton = UiFactory.getInstance().createButton(0, "Dump Local File");   
        TnMenu menu = UiFactory.getInstance().createMenu();
        menu.add("", CMD_DUMP_LOCAL_FILE);
        imageButton.setMenu(menu, AbstractTnComponent.TYPE_CLICK);
        imageButton.setCommandEventListener(this);
        hostListScreen.getContentContainer().add(imageButton);
        return hostListScreen;
    }
    
    private TnScreen createChangeGpsScreen(int state)
    {
        CitizenScreen changeGpsScreen = UiFactory.getInstance().createScreen(state);
        final FrogLabel titleLabel = UiFactory.getInstance().createLabel(0, "Gps List");
        TnUiArgs args = titleLabel.getTnUiArgs();
        args.put(TnUiArgs.KEY_PREFER_WIDTH, this.uiDecorator.SCREEN_WIDTH);
        titleLabel.setStyle(AbstractTnGraphics.HCENTER);
        titleLabel.setFont(font);
        titleLabel.setForegroundColor(TnColor.WHITE, TnColor.WHITE);
        changeGpsScreen.getTitleContainer().add(titleLabel);
        changeGpsScreen.getTitleContainer().setBackgroundColor(TnColor.GRAY);
        
        FrogList gpsList = UiFactory.getInstance().createList(0);
        changeGpsScreen.getContentContainer().add(gpsList);
        
        int gpsSource = ((DaoManager)DaoManager.getInstance()).getSecretSettingDao().get(SecretSettingDao.GPS_SOURCE_SELECT_INDEX);
        int index = gpsSource;//transferGpsSourceToIndex(gpsSource);
        String[] labels = (String[])this.model.get(KEY_ARRAY_GPS_LABELS);
        gpsList.setAdapter(new DefaultListAdapter(labels, null, index));
        
        return changeGpsScreen;
    }
    
    private FrogButton createRemoveOutputButton()
    {
        FrogButton removeOutputBttn = UiFactory.getInstance().createButton(ID_REMOVE_OUTPUT, "Remove Output Folder");
        TnMenu removeOutputmenu = UiFactory.getInstance().createMenu();
        removeOutputmenu.add("", CMD_REMOVE_OUT_PUT);
        removeOutputBttn.setMenu(removeOutputmenu, AbstractTnComponent.TYPE_CLICK);
        removeOutputBttn.setCommandEventListener(this);
        AbstractTnFont defaultFont = removeOutputBttn.getFont();
        final int buttonWidth = defaultFont.stringWidth("Remove Output Folder") + removeOutputBttn.getLeftPadding()
                + removeOutputBttn.getRightPadding();
        removeOutputBttn.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH,
            new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
            {
                public Object decorate(TnUiArgAdapter args)
                {
                    return PrimitiveTypeCache.valueOf(buttonWidth);
                }
            }));

        return removeOutputBttn;
    }
    
    private TnScreen createDirectoryScreen(int state)
    {
        CitizenScreen fileListScreen = createDirectoryContainer(state,"Dump File");
        String[] names = (String[]) this.model.get(KEY_ARRAY_FILE_NAMES);
        
        if (names != null)
        {
            FrogList fileList = UiFactory.getInstance().createList(ID_DIRECORY_FROGLIST);
            fileListScreen.getContentContainer().add(fileList);
            int size = names.length;
            int[] action = new int[size];
            for (int start = 0; start < size; start++)
            {
                action[start] = CMD_SHOW_DIRECTORY;
            }
            fileList.setAdapter(new DefaultListAdapter(names, action));
        }
        if (state == STATE_SHOW_DIRECTORY)
        {
            TnPersistentManager manager = TnPersistentManager.getInstance();
            if (manager.getExternalStorageState() == TnPersistentManager.MEDIA_MOUNTED)
            {
                FrogButton dumpFile = UiFactory.getInstance().createButton(
                    ID_DUMP_FILE_VIEW, "Dump File");
                TnMenu menu = UiFactory.getInstance().createMenu();
                menu.add("", CMD_DUMP_LOCAL_FILE);
                dumpFile.setMenu(menu, AbstractTnComponent.TYPE_CLICK);
                dumpFile.setCommandEventListener(this);
                fileListScreen.getContentContainer().add(dumpFile);
            }
        }
        
        if (state == STATE_DUMP_LOCAL_FILE)
        {
            String result = (String) this.model.get(KEY_S_DUMPFILE_RESULT);
            FrogMultiLine resultLabel = UiFactory.getInstance().createMultiLine(ID_DUMP_FILE_VIEW, result);
            fileListScreen.getContentContainer().add(resultLabel);
        }
       
        
        fileListScreen.getContentContainer().add(createRemoveOutputButton());
        
        return fileListScreen;
    }

    private CitizenScreen createDirectoryContainer(int state,String headName)
    {
        CitizenScreen fileListScreen = UiFactory.getInstance().createScreen(state);
        final FrogLabel titleLabel = UiFactory.getInstance().createLabel(0, headName);
        TnUiArgs args = titleLabel.getTnUiArgs();
        args.put(TnUiArgs.KEY_PREFER_WIDTH, this.uiDecorator.SCREEN_WIDTH);
        titleLabel.setStyle(AbstractTnGraphics.HCENTER);
        titleLabel.setFont(font);
        titleLabel.setForegroundColor(TnColor.WHITE, TnColor.WHITE);
        fileListScreen.getTitleContainer().add(titleLabel);
        fileListScreen.getTitleContainer().setBackgroundColor(TnColor.GRAY);
        return fileListScreen;
    }
    
    private TnScreen createOpenFileScreen(int state)
    {
        CitizenScreen fileContentScreen = createDirectoryContainer(state,"File Content");
        String fileContent = (String) this.model.get(KEY_S_SELECTED_FILE_CONTENT);
        FrogMultiLine multiLine = UiFactory.getInstance().createMultiLine(ID_FILE_CONTENT_LABEL, fileContent);
        fileContentScreen.getContentContainer().add(multiLine);
        return fileContentScreen;
    }
    
    
    private TnScreen createChangeNetworkScreen(int state)
    {
        CitizenScreen changeNetworkScreen = UiFactory.getInstance().createScreen(state);
        final FrogLabel titleLabel = UiFactory.getInstance().createLabel(0, "Network Type List");
        TnUiArgs args = titleLabel.getTnUiArgs();
        args.put(TnUiArgs.KEY_PREFER_WIDTH, this.uiDecorator.SCREEN_WIDTH);
        titleLabel.setStyle(AbstractTnGraphics.HCENTER);
        titleLabel.setFont(font);
        titleLabel.setForegroundColor(TnColor.WHITE, TnColor.WHITE);
        changeNetworkScreen.getTitleContainer().add(titleLabel);
        changeNetworkScreen.getTitleContainer().setBackgroundColor(TnColor.GRAY);
        
        FrogList networkList = UiFactory.getInstance().createList(0);
        changeNetworkScreen.getContentContainer().add(networkList);
        
        String[] labels = (String[])this.model.get(KEY_ARRAY_NETWORK_LABELS);
        int networkType = ((DaoManager)DaoManager.getInstance()).getSimpleConfigDao().get(SimpleConfigDao.KEY_NETWORK_SELECTED_INDEX);
        int index = transferNetworkTypeToIndex(networkType);
        
        networkList.setAdapter(new DefaultListAdapter(labels, null, index));
        
        return changeNetworkScreen;
    }
    
    private TnScreen createClearRmsScreen(int state)
    {
        CitizenScreen clearRmsScreen = UiFactory.getInstance().createScreen(state);
        
        final FrogLabel titleLabel = UiFactory.getInstance().createLabel(0, "Clear RMS");
        TnUiArgs args = titleLabel.getTnUiArgs();
        args.put(TnUiArgs.KEY_PREFER_WIDTH, this.uiDecorator.SCREEN_WIDTH);
        titleLabel.setStyle(AbstractTnGraphics.HCENTER);
        titleLabel.setFont(font);
        titleLabel.setForegroundColor(TnColor.WHITE, TnColor.WHITE);
        clearRmsScreen.getTitleContainer().add(titleLabel);
        clearRmsScreen.getTitleContainer().setBackgroundColor(TnColor.GRAY);
        
        FrogMultiLine multiLine = UiFactory.getInstance().createMultiLine(0, "Clear RMS successfully! To clear all application data, " +
        		"please touch 'Clear data' in the App Info screen. " +
        		"You can get the App info screen by selecting the application from the phone's settings option.");
        clearRmsScreen.getContentContainer().add(multiLine);
        clearRmsScreen.getContentContainer().setBackgroundColor(TnColor.WHITE);
        
        return clearRmsScreen;
    }
    private TnScreen createChangeDoneScreen(int state)
    {
        CitizenScreen changeCellScreen = UiFactory.getInstance().createScreen(state);
        
        final FrogLabel titleLabel = UiFactory.getInstance().createLabel(0, "change location");
        TnUiArgs args = titleLabel.getTnUiArgs();
        args.put(TnUiArgs.KEY_PREFER_WIDTH, this.uiDecorator.SCREEN_WIDTH);
        titleLabel.setStyle(AbstractTnGraphics.HCENTER);
        titleLabel.setFont(font);
        titleLabel.setForegroundColor(TnColor.WHITE, TnColor.WHITE);
        changeCellScreen.getTitleContainer().add(titleLabel);
        changeCellScreen.getTitleContainer().setBackgroundColor(TnColor.GRAY);
        
        int source = 0;
        if (((DaoManager)DaoManager.getInstance()).getSimpleConfigDao() != null)
            source = ((DaoManager)DaoManager.getInstance()).getSimpleConfigDao().get(
                SimpleConfigDao.KEY_LOCATION_SOURCE);
        String location = "All location sources are not disable";
        switch(source)
        {
            case 0:
                break;
            case 1:
                location = "Disable CarrierNetwork";
                break;
            case 2:
                location = "Disable CellID";
                break;
            case 3:
                location = "Disable CarrierNetwork and CellID";
                break;
            case 4:
                location = "Disable Wifi";
                break;
            case 5:
                location = "Disable CarrierNetwork and Wifi";
                break;
            case 6:
                location = "Disable CellID and Wifi";
                break;
            case 7:
                location = "Disable CarrierNetwork, CellID and Wifi";
                break;
            default:
                location = "The type your input is wrong.";
                break;
        }
        FrogMultiLine multiLine = UiFactory.getInstance().createMultiLine(0, "CHANGE_DONE: " + location);
        changeCellScreen.getContentContainer().add(multiLine);
        changeCellScreen.getContentContainer().setBackgroundColor(TnColor.WHITE);
        
        return changeCellScreen;
    }
    
    private TnScreen createChangeCellScreen(int state)
    {
        
        CitizenScreen changeCellScreen = UiFactory.getInstance().createScreen(state);
        
        final FrogLabel titleLabel = UiFactory.getInstance().createLabel(0, "change location");
        TnUiArgs args = titleLabel.getTnUiArgs();
        args.put(TnUiArgs.KEY_PREFER_WIDTH, this.uiDecorator.SCREEN_WIDTH);
        titleLabel.setStyle(AbstractTnGraphics.HCENTER);
        titleLabel.setFont(font);
        titleLabel.setForegroundColor(TnColor.WHITE, TnColor.WHITE);
        changeCellScreen.getTitleContainer().add(titleLabel);
        changeCellScreen.getTitleContainer().setBackgroundColor(TnColor.GRAY);
        
        textField = UiFactory.getInstance().createTextField("", -1, FrogTextField.NUMERIC,  "", 0);
        changeCellScreen.getContentContainer().add(textField);
        changeCellScreen.getContentContainer().setBackgroundColor(TnColor.WHITE);
        
        FrogMultiLine multiLine = UiFactory.getInstance().createMultiLine(0, " 0: nothing be disable" );
        multiLine.setFocusable(false);
        changeCellScreen.getContentContainer().add(multiLine);
        FrogMultiLine multiLine1 = UiFactory.getInstance().createMultiLine(1, "1: Disable CarrierNetwork" );
        multiLine1.setFocusable(false);
        changeCellScreen.getContentContainer().add(multiLine1);
        FrogMultiLine multiLine2 = UiFactory.getInstance().createMultiLine(2, "2: Disable CellID" );
        changeCellScreen.getContentContainer().add(multiLine2);
        multiLine2.setFocusable(false);
        FrogMultiLine multiLine3 = UiFactory.getInstance().createMultiLine(3, "3: Disable CarrierNetwork and CellID" );
        changeCellScreen.getContentContainer().add(multiLine3);
        multiLine3.setFocusable(false);
        FrogMultiLine multiLine4 = UiFactory.getInstance().createMultiLine(4, "4: Disable Wifi" );
        changeCellScreen.getContentContainer().add(multiLine4);
        multiLine4.setFocusable(false);
        FrogMultiLine multiLine5 = UiFactory.getInstance().createMultiLine(5, "5: Disable CarrierNetwork and Wifi" );
        changeCellScreen.getContentContainer().add(multiLine5);
        multiLine5.setFocusable(false);
        FrogMultiLine multiLine6 = UiFactory.getInstance().createMultiLine(6, "6: Disable CellID and Wifi" );
        changeCellScreen.getContentContainer().add(multiLine6);
        multiLine6.setFocusable(false);
        FrogMultiLine multiLine7 = UiFactory.getInstance().createMultiLine(7, "7: Disable CarrierNetwork, CellID and Wifi" );
        changeCellScreen.getContentContainer().add(multiLine7);
        multiLine7.setFocusable(false);
        
        FrogButton saveBttn = UiFactory.getInstance().createButton(CMD_CHANGE_DONE, "Save");
        TnMenu menu = UiFactory.getInstance().createMenu();
        menu.add("", CMD_CHANGE_DONE);
        saveBttn.setMenu(menu, AbstractTnComponent.TYPE_CLICK);
        changeCellScreen.getContentContainer().add(saveBttn);
        
        return changeCellScreen;
    }
    
    private int transferGpsIndexToSource(int index)
    {
        switch(index)
        {
            case 0:
                return GPS_BLUETOOTH_PUSH;
            case 1:
                return GPS_JSR179;
            case 2:
                return GPS_ALONG_ROUTE;
            case 3:
                return GPS_MVIEWER_TOOL;
        }
        
        return GPS_JSR179;
    }
  
    private int transferDataSourceModeIndexToSource(int index)
    {
        switch(index)
        {
            case 0:
                return ICommonConstants.DATASOURCE_MODE_AUTO;
            case 1:
                return ICommonConstants.DATASOURCE_MODE_ONBOARD;
            case 2:
                return ICommonConstants.DATASOURCE_MODE_OFFBOARD;
        }
        return GPS_JSR179;
    }
    
    //TODO: the hard code for network.
    private int transferNetworkTypeToIndex(int networkType)
    {
        switch(networkType)
        {
            case 0/*IHost.NETWORK_BIS*/:
                return 2;
            case 2/*IHost.NETWORK_DIRECT_TCP*/:
                return 3;
            case 1/*IHost.NETWORK_MDS*/:
                return 1;
        }
        
        return 0;
    }
    
    //TODO: the hard code for network.
    private int transferNetworkIndexToSource(int index)
    {
        switch(index)
        {
            case 1:
                return 1/*Host.NETWORK_MDS*/;
            case 2:
                return 0/*IHost.NETWORK_BIS*/;
            case 3:
                return 2/*IHost.NETWORK_DIRECT_TCP*/;
        }
        
        return -1;
    }
    
    private class DefaultListAdapter implements FrogAdapter
    {
        String[] labels;
        int[] actions;
        int selectIndex = -1;
        
        DefaultListAdapter(String[] labels, int[] actions)
        {
            this(labels, actions, -1);
        }
        
        DefaultListAdapter(String[] labels, int[] actions, int selectIndex)
        {
            this.labels = labels;
            this.actions = actions;
            this.selectIndex = selectIndex;
        }

        public AbstractTnComponent getComponent(int position, AbstractTnComponent convertComponent, AbstractTnComponent parent)
        {
            FrogListItem item = null;
            if(convertComponent == null)
            {
                item = UiFactory.getInstance().createListItem(position);
                item.setText(labels[position]);
                int command = getCommand(position);
                TnMenu menu = UiFactory.getInstance().createMenu();
                menu.add("", command);
                item.setMenu(menu, FrogLabel.TYPE_CLICK);
                item.setCommandEventListener(SecretKeyViewTouch.this);
            }
            else
            {
                item = (FrogListItem)convertComponent;
                item.setText(labels[position]);
                int command = getCommand(position);
                
                TnMenu menu = UiFactory.getInstance().createMenu();
                menu.add("", command);
                item.setMenu(menu, FrogLabel.TYPE_CLICK);
                
            }
            
            if (selectIndex == position)
            {
                item.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_SECRET_KEY_INFO_LABEL_SELECTED));
            }
            else
            {
                item.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_SECRET_KEY_INFO_LABEL));
            }
            
            return item;
        }

        public int getCount()
        {
            return labels == null ? 0 : labels.length;
        }

        private int getCommand(int position)
        {
            int command = CMD_COMMON_OK;
            if(actions != null && position >= 0 && position < actions.length)
            {
                command =  actions[position];
            }
            
            return command;
        }

        public int getItemType(int position)
        {
            return 0;
        }
    }
    
}
