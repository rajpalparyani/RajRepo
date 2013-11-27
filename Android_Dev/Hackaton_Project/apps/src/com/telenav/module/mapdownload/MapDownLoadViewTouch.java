/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * MapDownLoadViewTouch.java
 *
 */
package com.telenav.module.mapdownload;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.telenav.app.android.AndroidPersistentContext;
import com.telenav.app.android.scout_us.R;
import com.telenav.logger.Logger;
import com.telenav.mvc.AbstractCommonUiDecorator;
import com.telenav.mvc.AbstractCommonView;
import com.telenav.mvc.ICommonConstants;
import com.telenav.res.IStringCommon;
import com.telenav.res.IStringMapDownload;
import com.telenav.res.ResourceManager;
import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.core.AbstractTnUiHelper;
import com.telenav.tnui.core.TnMenu;
import com.telenav.tnui.core.TnScreen;
import com.telenav.tnui.core.TnUiArgs.TnUiArgAdapter;
import com.telenav.tnui.core.TnUiEvent;
import com.telenav.tnui.core.android.AndroidUiHelper;
import com.telenav.tnui.widget.TnPopupContainer;
import com.telenav.ui.ImageDecorator;
import com.telenav.ui.NinePatchImageDecorator;
import com.telenav.ui.UiFactory;
import com.telenav.ui.android.AssetsImageDrawable;
import com.telenav.ui.citizen.CitizenScreen;
import com.telenav.ui.citizen.android.AndroidCitizenUiHelper;

/**
 *@author yren
 *@date 2012-12-11
 */
class MapDownLoadViewTouch extends AbstractCommonView implements IMapDownLoadConstants
{
    enum TabButton
    {
        left, middle, right
    }

    private TextView mapDownloadManagerTitle;
    private LinearLayout tabContainer;
    private LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
    private TextView mapDownloadHint;
    private View mapDownloadManagerMap;
    private TextView mapDownloadManagerDescription;
    private TextView mapDownloadManagerSize;
    private View mainBtnTop;
    private Button mainBtnDown;
    
    private TextView mapDownloadingTitle;
    private TextView mapDownloadingHint;
    private TextView mapDownloadingName;
    private ProgressBar mapDownloadingProgress;
    private TextView mapDownloadingSpeed;
    private TextView mapDownloadingSuggestion;
    private Button mapDownloadingBtnLeft;
    private Button mapDownloadingBtnRight;
    private int oldOrientation = AbstractTnUiHelper.ORIENTATION_UNSPECIFIED;
    
    public MapDownLoadViewTouch(AbstractCommonUiDecorator uiDecorator)
    {
        super(uiDecorator);
    }
    
    protected void updateProgress()
    {
        mapDownloadingName.setText(getMapDownloadManager().getMapRegionId());
        int progress = (int) getMapDownloadManager().getPercentDownloaded();
        mapDownloadingProgress.setProgress(progress);
        mapDownloadingSpeed.setText(getDownloadingInfo());
        
        int suggestionKey = -1;
        
        if(getMapDownloadManager().isRunning())
        {
            if(getMapDownloadManager().isWifiLostDuringDownload())
            {
                suggestionKey = IStringMapDownload.RES_RETRY_DOWNLOADING;
            }
            else
            {
                suggestionKey = IStringMapDownload.RES_IN_PROGRESS;
            }
        }
        else
        {
            suggestionKey = IStringMapDownload.RES_PLEASE_RESUME;
        }
        
        mapDownloadingSuggestion.setText(ResourceManager.getInstance().getCurrentBundle()
                .getString(suggestionKey, IStringMapDownload.FAMILY_MAP_DOWNLOAD));
        int key = getMapDownloadManager().isRunning() ? IStringMapDownload.RES_PAUSE : IStringMapDownload.RES_CONTINUE;
        int cmd = getMapDownloadManager().isRunning() ? CMD_PAUSE_DOWNLOAD : CMD_RESUME_DOWNLOAD;
        String rightButtonText = ResourceManager.getInstance().getCurrentBundle()
                .getString(key, IStringMapDownload.FAMILY_MAP_DOWNLOAD);
        if (!mapDownloadingBtnRight.getText().toString().equalsIgnoreCase(rightButtonText))
        {
            mapDownloadingBtnRight.setText(rightButtonText);
            AndroidCitizenUiHelper.setOnClickCommand(this, mapDownloadingBtnRight, cmd);
        }
    }

    protected boolean updateScreen(int state, TnScreen screen)
    {
        switch (state)
        {
            case STATE_MAIN:
            {
                int currentOrientation = ((AbstractTnUiHelper) AndroidUiHelper.getInstance()).getOrientation();
                if (currentOrientation != oldOrientation)
                {
                    if (screen != null)
                    {
                        AndroidCitizenUiHelper.removeContentView((CitizenScreen) screen);
                        initMapDownloadScreen((CitizenScreen) screen);
                    }
                    oldOrientation = currentOrientation;
                }
                int downloadableRegionsCount = getMapDownloadManager().getRegionsCount();
                if (downloadableRegionsCount > 1) {
                    for (int i = 0; i < downloadableRegionsCount; i++)
                    {
                        boolean isFullyDownloaded = getMapDownloadManager().getFullyDownloaded(i);
                        updateFullyDownload(i, isFullyDownloaded);
                    }
                }
                boolean isNeedSetBackButton= this.model.getBool(KEY_B_IS_NEED_SET_BACK_BUTTON);
                if (isNeedSetBackButton)
                {
                    setButtonEnable(true);
                    mainBtnTop.setEnabled(true);
                    mainBtnDown.setEnabled(true);
                }
                updateButtonContainer();
                ensureTabs(getMapDownloadManager().getSelectedRegionIndex());
                break;
            }
            case STATE_DOWNLOADING:
            {
                boolean isNeedSetBackButton= this.model.fetchBool(KEY_B_IS_NEED_SET_BACK_BUTTON);
                if (isNeedSetBackButton)
                {
                    mapDownloadingBtnLeft.setEnabled(true);
                    mapDownloadingBtnRight.setEnabled(true);
                }
                updateProgress();
                break;
            }
            default:
            {
                break;
            }
        }
    
        return false;
    }
    
    protected String getDownloadingInfo()
    {
        StringBuilder sb = new StringBuilder();
        int downloadedSize = (int) (getMapDownloadManager().getPercentDownloaded() * getMapDownloadManager().getDownloadSize() / (1024 * 1024) / 100);
        sb.append(downloadedSize);
        sb.append("/");
        sb.append(getMapDownloadManager().getDownloadSize() / (1024 * 1024));
        sb.append(" MB (");
        sb.append(getDisplaySize(getMapDownloadManager().getDownloadSpeed()));
        sb.append("/sec)");
        return sb.toString();
    }

    protected int transformCommandDelegate(int state, TnUiEvent tnUiEvent)
    {
        return ICommonConstants.CMD_NONE;
    }

    protected TnPopupContainer createPopup(int state)
    {
        TnPopupContainer popup = null;
        switch(state)
        {
            case STATE_REQUEST_DOWNLOADABLE_REGION:
            {
                popup = UiFactory.getInstance().createProgressBox(0, "");
                break;
            }
            case STATE_CANCEL_MAP_DOWNLOAD_CONFIRM:
            {
                TnMenu comfirmMenu = UiFactory.getInstance().createMenu();
                comfirmMenu.add(
                    ResourceManager.getInstance().getCurrentBundle()
                            .getString(IStringCommon.RES_BTTN_OK, IStringCommon.FAMILY_COMMON), CMD_COMMON_OK);
                comfirmMenu.add(
                    ResourceManager.getInstance().getCurrentBundle()
                            .getString(IStringCommon.RES_BTTN_CANCEL, IStringCommon.FAMILY_COMMON), CMD_COMMON_CANCEL);
                popup = UiFactory.getInstance().createMessageBox(
                    0,
                    ResourceManager.getInstance().getCurrentBundle()
                            .getString(IStringMapDownload.RES_CANCEL_CONFIRM, IStringMapDownload.FAMILY_MAP_DOWNLOAD), comfirmMenu);
                break;
            }
            case STATE_DELETE_MAP_DOWNLOAD_CONFIRM:
            {
                TnMenu comfirmMenu = UiFactory.getInstance().createMenu();
                comfirmMenu.add(
                    ResourceManager.getInstance().getCurrentBundle()
                            .getString(IStringCommon.RES_BTTN_OK, IStringCommon.FAMILY_COMMON), CMD_COMMON_OK);
                comfirmMenu.add(
                    ResourceManager.getInstance().getCurrentBundle()
                            .getString(IStringCommon.RES_BTTN_CANCEL, IStringCommon.FAMILY_COMMON), CMD_COMMON_CANCEL);
                popup = UiFactory.getInstance().createMessageBox(0,
                    "Delete downloaded map?", comfirmMenu);
                break;
            }
            case STATE_REPLACE_MAP_DOWNLOAD_CONFIRM:
            {
                TnMenu comfirmMenu = UiFactory.getInstance().createMenu();
                comfirmMenu.add(
                    ResourceManager.getInstance().getCurrentBundle()
                            .getString(IStringCommon.RES_BTTN_OK, IStringCommon.FAMILY_COMMON), CMD_COMMON_OK);
                comfirmMenu.add(
                    ResourceManager.getInstance().getCurrentBundle()
                            .getString(IStringCommon.RES_BTTN_CANCEL, IStringCommon.FAMILY_COMMON), CMD_COMMON_CANCEL);
                
                String regionName = getMapDownloadManager().getMapRegionId(getMapDownloadManager().getFullyDownloadedRegionIndex());
                
                String pattern = ResourceManager.getInstance().getCurrentBundle().getString(IStringMapDownload.RES_REPLACE_CONFIRM, IStringMapDownload.FAMILY_MAP_DOWNLOAD);
                
                String message = ResourceManager.getInstance().getStringConverter().convert(pattern, new String[]{regionName});
                
                popup = UiFactory.getInstance().createMessageBox(0, message, comfirmMenu, false);
                break;
            }
            case STATE_WIFI_DISCONNECTED:
            {
                TnMenu menu = UiFactory.getInstance().createMenu();
                menu.add(
                    ResourceManager.getInstance().getCurrentBundle()
                            .getString(IStringCommon.RES_BTTN_OK, IStringCommon.FAMILY_COMMON), CMD_COMMON_OK);
                
                String message = "";
                int cmdId = model.getInt(KEY_I_CMD_TO_CHECK_WIFI);
                if(cmdId == CMD_START_DOWNLOAD || cmdId == CMD_UPGRADE
                        || cmdId == CMD_REPLACE_DOWNLOAD)
                {
                    message = ResourceManager.getInstance().getCurrentBundle().getString(IStringMapDownload.RES_ERROR_NO_WIFI_START, IStringMapDownload.FAMILY_MAP_DOWNLOAD);
                }
                else
                {
                    message = ResourceManager.getInstance().getCurrentBundle().getString(IStringMapDownload.RES_ERROR_NO_WIFI, IStringMapDownload.FAMILY_MAP_DOWNLOAD);
                }
                popup = UiFactory.getInstance().createMessageBox(0, message, menu);
                break;
            }
            case STATE_NOT_INITED_ERROR:
            {
                String errorMsg = model.getString(KEY_S_ERROR_MESSAGE);
                TnMenu menu = UiFactory.getInstance().createMenu();
                menu.add(ResourceManager.getInstance().getCurrentBundle().getString(IStringCommon.RES_BTTN_OK,
                    IStringCommon.FAMILY_COMMON), CMD_COMMON_OK);
                popup = UiFactory.getInstance().createMessageBox(state, errorMsg, menu);
                popup.setCommandEventListener(this);
                break;
            }
            case STATE_MAP_DOWNLOADED_STATUS_CHANGED:
            {
                popup = createMapDownloadedStatusChangePopup(state);
                break;
            }
            case STATE_STILL_DELETING:
            {
                String errorMsg = model.getString(KEY_S_ERROR_MESSAGE);
                TnMenu menu = UiFactory.getInstance().createMenu();
                menu.add(ResourceManager.getInstance().getCurrentBundle().getString(IStringCommon.RES_BTTN_OK,
                    IStringCommon.FAMILY_COMMON), CMD_COMMON_OK);
                popup = UiFactory.getInstance().createMessageBox(state, errorMsg, menu);
                popup.setCommandEventListener(this);
                break;
            }
            default:
            {
                break;
            }
        }
        return popup;
    }
    
    /**
     * the difference with super: only one button
     */
    protected TnPopupContainer createMapDownloadedStatusChangePopup(int state)
    {
        String message = MapDownLoadMessageHandler.getInstance().mapIncompatibleMessage;
        MapDownLoadMessageHandler.getInstance().setMapIncompatibleMessageDone();
        TnMenu comfirmMenu = UiFactory.getInstance().createMenu();
        comfirmMenu.add(
            ResourceManager.getInstance().getCurrentBundle()
                    .getString(IStringCommon.RES_BTTN_OK, IStringCommon.FAMILY_COMMON), CMD_COMMON_OK);
        String title = ResourceManager.getInstance().getCurrentBundle().getString(IStringMapDownload.RES_TITLE, IStringMapDownload.FAMILY_MAP_DOWNLOAD);
        TnPopupContainer popup = UiFactory.getInstance().createMessageBox(0, message, title, comfirmMenu, false);
        return popup;
    }

    protected TnScreen createScreen(int state)
    {
        TnScreen screen = null;
        switch (state)
        {
            case STATE_MAIN:
            {
                screen = createMainScreen(state);
                break;
            }
            case STATE_DOWNLOADING:
            {
                screen = createDownloadingScreen(state);
                break;
            }
            default:
            {
                break;
            }
        }
        
        if (screen != null)
        {
            removeMenuById(screen, ICommonConstants.CMD_COMMON_PREFERENCE);
        }
        return screen;
    }
    
    protected MapDownloadManager getMapDownloadManager()
    {
        return (MapDownloadManager) this.model.get(KEY_O_MAP_DOWNLOAD_MANAGER);
    }
    
    protected void updateButtonContainer()
    {
        TnScreen screen = this.getScreenByState(STATE_MAIN);
        // initialize top, down button on main page
        if (mainBtnTop == null)
        {
            if (screen == null || !(screen.getRootContainer().getNativeUiComponent() instanceof View))
            {
                return;
            }

            View mainView = (View) screen.getRootContainer().getNativeUiComponent();
            mainBtnTop = mainView.findViewById(R.id.mapDownload0BtnOne);
        }

        if (mainBtnDown == null)
        {
            if (screen == null || !(screen.getRootContainer().getNativeUiComponent() instanceof View))
            {
                return;
            }

            View mainView = (View) screen.getRootContainer().getNativeUiComponent();
            mainBtnTop = mainView.findViewById(R.id.mapDownload0BtnOne);
            mainBtnDown = (Button) mainView.findViewById(R.id.mapDownload0BtnTwo);
        }
        
        if (mainBtnTop == null || mainBtnDown == null)
        {
            return;
        }
        
        TextView topButtonTextView = (TextView)mainBtnTop.findViewById(R.id.mapDownload0TopButtonTextView);
        ImageView topButtonImageView = (ImageView)mainBtnTop.findViewById(R.id.mapDownload0tTopButtonImageView);
        
        boolean isFeatureEnabled = ((MapDownLoadModel)model).isFeatureEnabled();
        boolean isMapDataFullyDownloaded = (getMapDownloadManager().getFullyDownloadedRegionIndex() != -1);
        boolean isUpdateAvailable = getMapDownloadManager().getUpdateAvailable();
        boolean isCurrentFullyDownloaded = false;
        
        if(isMapDataFullyDownloaded 
                && getMapDownloadManager().getFullyDownloadedRegionIndex() == getMapDownloadManager().getSelectedRegionIndex())
        {
            isCurrentFullyDownloaded = true;
        }
        
        model.remove(KEY_I_CMD_TO_TRIGGER_UPSELL);
        
        //start judgment
        if(!isMapDataFullyDownloaded)
        {
            String startDownloadTemplate = ResourceManager.getInstance().getCurrentBundle()
                    .getString(IStringMapDownload.RES_START, IStringMapDownload.FAMILY_MAP_DOWNLOAD);
            
            String regionName = getMapDownloadManager().getMapRegionId();
            
            String startDownloadString = ResourceManager.getInstance().getStringConverter().convert(startDownloadTemplate, new String[]{regionName});
            
            mainBtnTop.setVisibility(View.VISIBLE);
            topButtonTextView.setText(startDownloadString);
            topButtonImageView.setVisibility(View.VISIBLE);
            
            if(isFeatureEnabled)
            {
                AndroidCitizenUiHelper.setOnClickCommand(this, mainBtnTop, CMD_START_DOWNLOAD);
            }
            else
            {
                AndroidCitizenUiHelper.setOnClickCommand(this, mainBtnTop, CMD_ACTIVATED);
                model.put(KEY_I_CMD_TO_TRIGGER_UPSELL, CMD_START_DOWNLOAD);
            }
            
            setActionButtonBg(mainBtnTop, BTN_TYPE_DOWNLOAD);
            
            mainBtnDown.setVisibility(View.GONE);
        }
        else
        {
            if(isCurrentFullyDownloaded)
            {
                if(!isFeatureEnabled)
                {
                    mainBtnTop.setVisibility(View.VISIBLE);
                    topButtonTextView.setText(ResourceManager.getInstance().getCurrentBundle()
                        .getString(IStringMapDownload.RES_REACTIVATE, IStringMapDownload.FAMILY_MAP_DOWNLOAD));
                    topButtonImageView.setVisibility(View.GONE);
                    
                    AndroidCitizenUiHelper.setOnClickCommand(this, mainBtnTop, CMD_ACTIVATED);
                    if(isUpdateAvailable)
                    {
                        model.put(KEY_I_CMD_TO_TRIGGER_UPSELL, CMD_UPGRADE);
                    }
                    
                    setActionButtonBg(mainBtnTop, BTN_TYPE_REACTIVE);
                }
                else if(isUpdateAvailable)
                {
                    mainBtnTop.setVisibility(View.VISIBLE);
                    topButtonTextView.setText(ResourceManager.getInstance().getCurrentBundle()
                            .getString(IStringMapDownload.RES_UPDATE, IStringMapDownload.FAMILY_MAP_DOWNLOAD));
                    topButtonImageView.setVisibility(View.GONE);
                    AndroidCitizenUiHelper.setOnClickCommand(this, mainBtnTop, CMD_UPGRADE);
                    
                    setActionButtonBg(mainBtnTop, BTN_TYPE_DOWNLOAD);
                }
                else
                {
                    mainBtnTop.setVisibility(View.GONE);
                }
                
                mainBtnDown.setVisibility(View.VISIBLE);
                mainBtnDown.setText(ResourceManager.getInstance().getCurrentBundle()
                        .getString(IStringMapDownload.RES_DELETE, IStringMapDownload.FAMILY_MAP_DOWNLOAD));
                AndroidCitizenUiHelper.setOnClickCommand(this, mainBtnDown, CMD_DELETE_DOWNLOAD);
                setActionButtonBg(mainBtnDown, BTN_TYPE_DELETE);
            }
            else
            {
                mainBtnTop.setVisibility(View.VISIBLE);
                topButtonTextView.setText(ResourceManager.getInstance().getCurrentBundle()
                        .getString(IStringMapDownload.RES_REPLACE, IStringMapDownload.FAMILY_MAP_DOWNLOAD)
                        + " "
                        + getMapDownloadManager().getMapRegionId(
                            getMapDownloadManager().getFullyDownloadedRegionIndex()));
                topButtonImageView.setVisibility(View.GONE);
                if(isFeatureEnabled)
                {
                    AndroidCitizenUiHelper.setOnClickCommand(this, mainBtnTop, CMD_REPLACE_DOWNLOAD);
                }
                else
                {
                    AndroidCitizenUiHelper.setOnClickCommand(this, mainBtnTop, CMD_ACTIVATED);
                    model.put(KEY_I_CMD_TO_TRIGGER_UPSELL, CMD_REPLACE_DOWNLOAD);
                }
                setActionButtonBg(mainBtnTop, BTN_TYPE_DOWNLOAD);
                
                mainBtnDown.setVisibility(View.GONE);
            }
        }
    }
    
    protected void setActionButtonBg(View button, int type)
    {
        if(button == null)
        {
            return;
        }
        
        int textAppearanceId = -1;
        
        switch(type)
        {
            case BTN_TYPE_DOWNLOAD:
            case BTN_TYPE_REACTIVE:
            {
                textAppearanceId = R.style.TextApperance_MapDownloadManager_Button;
                button.setBackgroundResource(R.drawable.big_radian_yellow_button);
                break;
            }
            case BTN_TYPE_DELETE:
            {
                textAppearanceId = R.style.TextApperance_MapDownloadManager_Button_Delete;
                button.setBackgroundResource(R.drawable.big_radian_red_button);
                break;
            }
        }

        if (button instanceof Button)
        {
            ((Button) button).setTextAppearance(AndroidPersistentContext.getInstance().getContext(), textAppearanceId);
        }
        else
        {
            TextView topButtonTextView = (TextView) button.findViewById(R.id.mapDownload0TopButtonTextView);
            if (topButtonTextView != null)
            {
                topButtonTextView.setTextAppearance(AndroidPersistentContext.getInstance().getContext(), textAppearanceId);
            }
        }
    }
    
    private void ensureTabs(int index)
    {
        int childCount = tabContainer.getChildCount();
        if (childCount > 0) {
            for (int i = 0; i < childCount; i++)
            {
                View tab = tabContainer.getChildAt(i);
                tab.findViewById(R.id.mapDownloadTab).setSelected(index == i);
            }
        }
        updateTabContent(index);
        updateButtonContainer();
    }
    
    private void setButtonEnable(boolean enable)
    {
        int childCount = tabContainer.getChildCount();
        if (childCount > 0) {
            for (int i = 0; i < childCount; i++)
            {
                View tab = tabContainer.getChildAt(i);
                tab.findViewById(R.id.mapDownloadTab).setEnabled(enable);
            }
        }
    }
    
    private void updateTabContent(int index)
    {
        String mapName = getMapDownloadManager().getMapName(index);
        if (!TextUtils.isEmpty(mapName))
        {
            TnUiArgAdapter adapter = new TnUiArgAdapter(mapName + ".png" , ImageDecorator.instance);
            if (adapter.getImage() != null)
            {
                Drawable map = new AssetsImageDrawable(adapter.getImage());
                mapDownloadManagerMap.setBackgroundDrawable(map);
            }
        }
        StringBuilder sb = new StringBuilder();
        sb.append(ResourceManager.getInstance().getCurrentBundle()
                .getString(IStringMapDownload.RES_DESCRIPTION_PREFIX, IStringMapDownload.FAMILY_MAP_DOWNLOAD));
        sb.append(getMapDownloadManager().getMapDataInfo(index));
        mapDownloadManagerDescription.setText(sb.toString());
        StringBuilder sbSize = new StringBuilder();
        sbSize.append(ResourceManager.getInstance().getCurrentBundle()
                .getString(IStringMapDownload.RES_DOWNLOAD_SIZE_PREFIX, IStringMapDownload.FAMILY_MAP_DOWNLOAD));
        long sizeInByte = getMapDownloadManager().getDownloadSize();
        sbSize.append(getDisplaySize(sizeInByte));
        mapDownloadManagerSize.setText(sbSize.toString());
    }
    
    private String getDisplaySize(long sizeInByte)
    {
        StringBuilder sb = new StringBuilder();
        if (sizeInByte <= 0)
        {
            return "0B";
        }
        if (sizeInByte < 1024)
        {
            sb.append(sizeInByte);
            sb.append("B");
        }
        else if (sizeInByte < 1048576)
        {
            sb.append(sizeInByte / 1024);
            sb.append("KB");
        }
        else if (sizeInByte < 10 * 1073741824L)
        {
            sb.append(sizeInByte / 1048576);
            sb.append("MB");
        }
        else
        {
            sb.append(sizeInByte / 1073741824);
            sb.append("GB");
        }
        return sb.toString();
    }

    protected void updateFullyDownload(int index, boolean isFullyDownloaded)
    {
        View tab = null;
        int childCount = tabContainer.getChildCount();
        if (childCount > 0 && index > -1 && index < childCount)
        {
            tab = tabContainer.getChildAt(index);
        }
        
        if(tab == null)
        {
            return;
        }
        
        View complete = tab.findViewById(R.id.mapDownloadTabComplete);
        if (isFullyDownloaded && complete.getVisibility() != View.VISIBLE)
        {
            complete.setVisibility(View.VISIBLE);
            Drawable finishIcon = new AssetsImageDrawable(ImageDecorator.TAB_FINISHED_ICON);
            complete.setBackgroundDrawable(finishIcon);
        }
        else if (!isFullyDownloaded && complete.getVisibility() == View.VISIBLE)
        {
            complete.setVisibility(View.GONE);
        }
    }
    
    protected TnScreen createMainScreen(int state)
    {
        oldOrientation = ((AbstractTnUiHelper) AndroidUiHelper.getInstance()).getOrientation();
        CitizenScreen screen = UiFactory.getInstance().createScreen(state);
        initMapDownloadScreen(screen);
        return screen;
    }
    
    private void initMapDownloadScreen(CitizenScreen screen)
    {
        View mainView;
        
        int orientation = ((AbstractTnUiHelper)AbstractTnUiHelper.getInstance()).getOrientation();
        
        if(orientation == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
        {
            mainView = AndroidCitizenUiHelper.addContentView(screen, R.layout.map_download_manager);
        }
        else
        {
            mainView = AndroidCitizenUiHelper.addContentView(screen, R.layout.map_download_manager_landscape);
        }
        // find view by id
        mapDownloadManagerTitle = (TextView) mainView.findViewById(R.id.commonTitle0TextView);
        mapDownloadManagerTitle.setText(ResourceManager.getInstance().getCurrentBundle()
                .getString(IStringMapDownload.RES_TITLE, IStringMapDownload.FAMILY_MAP_DOWNLOAD));
        mapDownloadHint = (TextView) mainView.findViewById(R.id.mapDownload0Hint);
        mapDownloadHint.setText(ResourceManager.getInstance().getCurrentBundle()
                .getString(IStringMapDownload.RES_HINT, IStringMapDownload.FAMILY_MAP_DOWNLOAD));
        tabContainer = (LinearLayout) mainView.findViewById(R.id.mapDownload0TabContainer);
        tabContainer.removeAllViews();
        int downloadableRegionsCount = getMapDownloadManager().getRegionsCount();
        if (downloadableRegionsCount > 1) {
            for (int i = 0; i < downloadableRegionsCount; i++)
            {
                String regionName = getMapDownloadManager().getMapRegionId(i);
                
                if(orientation == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                {
                    StringBuilder sb = new StringBuilder(regionName);
                    int index = sb.indexOf(" ");
                    if(index > 0)
                    {
                        sb.setCharAt(index, '\n');
                    }
                    
                    regionName = sb.toString();
                }
                boolean fullyDownloaded = getMapDownloadManager().getFullyDownloaded(i);
                View tab = AndroidCitizenUiHelper.inflateLayout(R.layout.item_map_download_tab);
                Button btn = (Button) tab.findViewById(R.id.mapDownloadTab);
                if (i == 0)
                {
                    setNativeButtonBg(btn, TabButton.left);
                }
                else if (i == downloadableRegionsCount - 1)
                {
                    setNativeButtonBg(btn, TabButton.right);
                }
                else
                {
                    setNativeButtonBg(btn, TabButton.middle);
                }
                btn.setText(regionName);
                AndroidCitizenUiHelper.setOnClickCommand(this, btn, CMD_DOWNLOADABLE_REGION_SELECTED_BASE + i);
                tabContainer.addView(tab, llp);
                updateFullyDownload(i, fullyDownloaded);
            }
        }
        else
        {
            // warn the developer, this tab container is designed to include at least two tabs
            Logger.log(Logger.ERROR, getClass().getSimpleName(), "tab container is designed to include at least two tabs");
        }
        mapDownloadManagerMap = mainView.findViewById(R.id.mapDownload0Map);
        mapDownloadManagerDescription = (TextView) mainView.findViewById(R.id.mapDownload0Description);
        mapDownloadManagerSize = (TextView) mainView.findViewById(R.id.mapDownload0Size);
        
        // save the button enable/disable state to recover
        boolean isTopButtonEnabled = true;
        if(mainBtnTop != null)
        {
            isTopButtonEnabled = mainBtnTop.isEnabled();
        }
        boolean isBottomButtonEnabled = true;
        if(mainBtnDown != null)
        {
            isBottomButtonEnabled = mainBtnDown.isEnabled();
        }
        mainBtnTop = mainView.findViewById(R.id.mapDownload0BtnOne);
        mainBtnDown = (Button) mainView.findViewById(R.id.mapDownload0BtnTwo);
        mainBtnTop.setEnabled(isTopButtonEnabled);
        mainBtnDown.setEnabled(isBottomButtonEnabled);
        updateButtonContainer();

        // set the first tab as default
        ensureTabs(getMapDownloadManager().getSelectedRegionIndex());
    }
    
    private TnScreen createDownloadingScreen(int state)
    {
        CitizenScreen screen = UiFactory.getInstance().createScreen(state);
        View mainView = AndroidCitizenUiHelper.addContentView(screen, R.layout.map_downloading);
        // find view by id
        mapDownloadingTitle = (TextView) mainView.findViewById(R.id.commonTitle0TextView);
        mapDownloadingTitle.setText(ResourceManager.getInstance().getCurrentBundle()
                .getString(IStringMapDownload.RES_TITLE, IStringMapDownload.FAMILY_MAP_DOWNLOAD));
        mapDownloadingHint = (TextView) mainView.findViewById(R.id.mapDownloadingHint);
        mapDownloadingHint.setText(ResourceManager.getInstance().getCurrentBundle()
                .getString(IStringMapDownload.RES_DOWNLOADING_HINT, IStringMapDownload.FAMILY_MAP_DOWNLOAD));
        mapDownloadingName = (TextView) mainView.findViewById(R.id.mapDownloadingName);
        mapDownloadingName.setText(getMapDownloadManager().getMapRegionId());
        mapDownloadingProgress = (ProgressBar) mainView.findViewById(R.id.mapDownloadingProgress);
        mapDownloadingSpeed = (TextView) mainView.findViewById(R.id.mapDownloadingSpeed);
        mapDownloadingSuggestion = (TextView) mainView.findViewById(R.id.mapDownloadingSuggestion);
        mapDownloadingSuggestion.setText(ResourceManager.getInstance().getCurrentBundle()
                .getString(IStringMapDownload.RES_IN_PROGRESS, IStringMapDownload.FAMILY_MAP_DOWNLOAD));
        mapDownloadingBtnLeft = (Button) mainView.findViewById(R.id.mapDownloadingBtnOne);
        mapDownloadingBtnLeft.setText(ResourceManager.getInstance().getCurrentBundle()
                .getString(IStringCommon.RES_BTTN_CANCEL, IStringCommon.FAMILY_COMMON));
        mapDownloadingBtnRight = (Button) mainView.findViewById(R.id.mapDownloadingBtnTwo);
        mapDownloadingBtnRight.setText(ResourceManager.getInstance().getCurrentBundle()
                .getString(IStringMapDownload.RES_PAUSE, IStringMapDownload.FAMILY_MAP_DOWNLOAD));
        AndroidCitizenUiHelper.setOnClickCommand(this, mapDownloadingBtnRight, CMD_PAUSE_DOWNLOAD);
        AndroidCitizenUiHelper.setOnClickCommand(this, mapDownloadingBtnLeft, CMD_CANCEL_DOWNLOAD);
        updateProgress();
        return screen;
    }
    
    private void setNativeButtonBg(Button button, TabButton tab)
    {
        AssetsImageDrawable normalDrawable;
        AssetsImageDrawable pressedDrawable;
        switch (tab)
        {
            case left:
            {
                normalDrawable = new AssetsImageDrawable(NinePatchImageDecorator.MAP_DOWNLOAD_TAB_BUTTON_LEFT_UNFOCUSED);
                pressedDrawable = new AssetsImageDrawable(NinePatchImageDecorator.MAP_DOWNLOAD_TAB_BUTTON_LEFT_FOCUSED);
                break;
            }
            case middle:
            {
                normalDrawable = new AssetsImageDrawable(NinePatchImageDecorator.MAP_DOWNLOAD_TAB_BUTTON_MIDDLE_UNFOCUSED);
                pressedDrawable = new AssetsImageDrawable(NinePatchImageDecorator.MAP_DOWNLOAD_TAB_BUTTON_MIDDLE_FOCUSED);
                break;
            }
            case right:
            {
                normalDrawable = new AssetsImageDrawable(NinePatchImageDecorator.MAP_DOWNLOAD_TAB_BUTTON_RIGHT_UNFOCUSED);
                pressedDrawable = new AssetsImageDrawable(NinePatchImageDecorator.MAP_DOWNLOAD_TAB_BUTTON_RIGHT_FOCUSED);
                break;
            }
            default:
            {
                return;
            }
        }
        StateListDrawable stateListDrawable = ((AndroidUiHelper) AbstractTnUiHelper.getInstance()).createStateListDrawable(normalDrawable,
            pressedDrawable, normalDrawable, normalDrawable, pressedDrawable);
        button.setBackgroundDrawable(stateListDrawable);
    }
    
    

    protected boolean updatePopup(int state, TnPopupContainer popup)
    {
        // TODO Auto-generated method stub
        return false;
    }
    
    protected boolean preProcessUIEvent(TnUiEvent tnUiEvent)
    {
        if (tnUiEvent.getType() == TnUiEvent.TYPE_COMMAND_EVENT)
        {
            int id = tnUiEvent.getCommandEvent().getCommand();
            if (id >= CMD_DOWNLOADABLE_REGION_SELECTED_BASE)
            {
                if(getMapDownloadManager().isRunning())
                {
                    return true;
                }
                
                int index = id - CMD_DOWNLOADABLE_REGION_SELECTED_BASE;
                getMapDownloadManager().setSelectedRegionIndex(index);
                ensureTabs(index);
                return true;
            }
            else if(id == CMD_PAUSE_DOWNLOAD || id == CMD_RESUME_DOWNLOAD)
            {
                mapDownloadingBtnLeft.setEnabled(false);
                mapDownloadingBtnRight.setEnabled(false);
                
                if(id == CMD_RESUME_DOWNLOAD)
                {
                    this.model.put(KEY_I_CMD_TO_CHECK_WIFI, id);
                }
            }
            else if (id == CMD_START_DOWNLOAD ||
                    id == CMD_UPGRADE || id == CMD_REPLACE_DOWNLOAD)
            {
                this.model.put(KEY_I_CMD_TO_CHECK_WIFI, id);
            }
            else if (model.getState() == STATE_DELETE_MAP_DOWNLOAD_CONFIRM && id == CMD_COMMON_OK)
            {
                setButtonEnable(false);
                mainBtnTop.setEnabled(false);
                mainBtnDown.setEnabled(false);
            }
            else if (model.getState() == STATE_CANCEL_MAP_DOWNLOAD_CONFIRM && id == CMD_COMMON_OK)
            {
                mapDownloadingBtnLeft.setEnabled(false);
                mapDownloadingBtnRight.setEnabled(false);
            }
        }
        return false;
    }
    
    @Override
    public void onSizeChanged(AbstractTnComponent tnComponent, int w, int h, int oldw, int oldh)
    {
        int currentOrientation = ((AbstractTnUiHelper) AndroidUiHelper.getInstance()).getOrientation();
        if (currentOrientation != oldOrientation)
        {
            CitizenScreen screen = (CitizenScreen) getScreenByState(STATE_MAIN);
            if (screen != null)
            {
                AndroidCitizenUiHelper.removeContentView(screen);
                initMapDownloadScreen(screen);
            }
            oldOrientation = currentOrientation;
        }
        super.onSizeChanged(tnComponent, w, h, oldw, oldh);
    }
}
