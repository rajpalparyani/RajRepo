/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * IPreferenceConstants.java
 *
 */
package com.telenav.module.poi.search;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.telenav.app.TeleNavDelegate;
import com.telenav.app.android.scout_us.R;
import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.datatypes.poi.PoiCategory;
import com.telenav.data.serverproxy.NetworkStatusManager;
import com.telenav.data.serverproxy.impl.IPoiSearchProxy;
import com.telenav.i18n.ResourceBundle;
import com.telenav.module.AppConfigHelper;
import com.telenav.module.nav.NavRunningStatusProvider;
import com.telenav.mvc.AbstractCommonUiDecorator;
import com.telenav.mvc.AbstractCommonView;
import com.telenav.res.ISpecialImageRes;
import com.telenav.res.IStringCommon;
import com.telenav.res.IStringDsr;
import com.telenav.res.IStringPoi;
import com.telenav.res.ResourceManager;
import com.telenav.res.converter.StringConverter;
import com.telenav.sdk.kontagent.KontagentLogger;
import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.core.AbstractTnContainer;
import com.telenav.tnui.core.AbstractTnUiHelper;
import com.telenav.tnui.core.ITnScreenAttachedListener;
import com.telenav.tnui.core.ITnUiEventListener;
import com.telenav.tnui.core.TnKeyEvent;
import com.telenav.tnui.core.TnMenu;
import com.telenav.tnui.core.TnScreen;
import com.telenav.tnui.core.TnUiArgs;
import com.telenav.tnui.core.TnUiArgs.ITnUiArgsDecorator;
import com.telenav.tnui.core.TnUiArgs.TnUiArgAdapter;
import com.telenav.tnui.core.TnUiEvent;
import com.telenav.tnui.core.android.AndroidUiHelper;
import com.telenav.tnui.graphics.AbstractTnFont;
import com.telenav.tnui.graphics.AbstractTnGraphics;
import com.telenav.tnui.graphics.AbstractTnImage;
import com.telenav.tnui.widget.TnLinearContainer;
import com.telenav.tnui.widget.TnPopupContainer;
import com.telenav.tnui.widget.TnScrollPanel;
import com.telenav.tnui.widget.TnTextField;
import com.telenav.ui.ImageDecorator;
import com.telenav.ui.NinePatchImageDecorator;
import com.telenav.ui.UiFactory;
import com.telenav.ui.UiStyleManager;
import com.telenav.ui.citizen.CitizenCategoryListItem;
import com.telenav.ui.citizen.CitizenQuickFindButton;
import com.telenav.ui.citizen.CitizenScreen;
import com.telenav.ui.citizen.CitizenTextField;
import com.telenav.ui.citizen.android.AndroidCitizenUiHelper;
import com.telenav.ui.frogui.widget.FrogAdapter;
import com.telenav.ui.frogui.widget.FrogKeywordFilter;
import com.telenav.ui.frogui.widget.FrogLabel;
import com.telenav.ui.frogui.widget.FrogList;
import com.telenav.ui.frogui.widget.FrogListItem;
import com.telenav.ui.frogui.widget.FrogMessageBox;
import com.telenav.ui.frogui.widget.FrogNullField;
import com.telenav.ui.frogui.widget.FrogProgressBox;
import com.telenav.ui.frogui.widget.FrogTextField;
import com.telenav.util.PrimitiveTypeCache;

/**
 *@author Albert Ma (byma@telenav.cn)
 *@modified by bduan.
 *@date 2010-8-18
 */
class PoiSearchViewTouch extends AbstractCommonView implements IPoiSearchConstants, ITnUiEventListener
{
    protected TnLinearContainer iconContainer;
    protected TnLinearContainer portraintQuickFindContainer;
    protected TnLinearContainer landscapeQuickFindContainer;
    protected TnLinearContainer portraintLocalEventContainer;
    protected TnLinearContainer landscapeLocalEventContainer;
    protected TnLinearContainer contentContainer;
    protected int oldOrientation = 0;
    private CategoryListAdapter categoryListAdapter;
    
    /**
     * it's for cast that items in last row of QuickFind panel is lesser than normal.
     * This value instruct the layout style of these items.
     * can be AbstractTnGraphics.HCENTER or AbstractTnGraphics.LEFT
     * 
     * if AbstractTnGraphics.LEFT, it will layout like below:
     * 
     * 0  1  2
     * 3  4  5
     * 6  7
     * 
     * else if AbstractTnGraphics.HCENTER
     * 
     * 0  1  2 
     * 3  4  5
     *  6   7
     */
    protected int lastItemsAnchor = AbstractTnGraphics.LEFT;
    private View titlebarView;
    private View searchbarView;
    
    public PoiSearchViewTouch(AbstractCommonUiDecorator uiDecorator)
    {
        super(uiDecorator);
        
    }

    protected int transformCommandDelegate(int state, TnUiEvent tnUiEvent)
    {
        
        return CMD_NONE;
    }

    protected TnPopupContainer createPopup(int state)
    {
        TnPopupContainer popup = null;
        switch (state)
        {
            case STATE_POI_SEARCHING:
            {
                popup = createSearchProgressPopup();
                break;
            }
            case STATE_NO_GPS_WARNING:
            {
                popup = createNoGPSWarning();
                break;
            }
        }
        return popup;
    }

    private TnPopupContainer createNoGPSWarning()
    {
        ResourceBundle bundle = ResourceManager.getInstance().getCurrentBundle();
        String noGps = bundle.getString(IStringCommon.RES_GPS_ERROR, IStringCommon.FAMILY_COMMON);
        
        TnMenu menu = UiFactory.getInstance().createMenu();
        
        String cancel = bundle.getString(IStringCommon.RES_BTTN_OK, IStringCommon.FAMILY_COMMON);
        menu.add(cancel, CMD_COMMON_BACK);
        
        FrogMessageBox messageBox = UiFactory.getInstance().createMessageBox(STATE_NO_GPS_WARNING, noGps, menu);
        return messageBox;
    }

    protected TnPopupContainer createSearchProgressPopup()
    {
        String search = ResourceManager.getInstance().getCurrentBundle().getString(IStringCommon.RES_SEARCHING, IStringCommon.FAMILY_COMMON);
        String name =  model.getString(KEY_S_COMMON_SHOW_SEARCH_TEXT);
        if(name == null)
        {
            name = model.getString(KEY_S_COMMON_SEARCH_TEXT);
        }
        
        if (name == null)
        {
            search = ResourceManager.getInstance().getCurrentBundle().getString(IStringDsr.RES_LABEL_SEARCHING, IStringDsr.FAMILY_DSR);            
        }
        else
        {
            StringConverter converter = ResourceManager.getInstance().getStringConverter();
            search = converter.convert(search, new String[]{name});
        }
        
        FrogProgressBox progressBox  = UiFactory.getInstance().createProgressBox(0, search);
        
        return progressBox;
    }

    protected boolean updatePopup(int state, TnPopupContainer popup)
    {
        return false;
    }

    protected TnScreen createScreen(int state)
    {
        TnScreen screen = null;
        switch(state)
        {
            case STATE_SEARCH_QUICK_FIND:
            {
                screen = createQuickFindScreen();
                break;
            }
            case STATE_SEARCH_CATEGORIES:
            {
                screen = createCategoriesSearchScreen();
                break;
            }
            case STATE_SUB_CATELOG:
            {
                screen = createSubCategoriesScreen();
                break;
            }
            
        }
        return screen;
    }

    private TnScreen createSubCategoriesScreen()
    {
        UiFactory uiFactory = UiFactory.getInstance();
        UiStyleManager uiStyleManager = UiStyleManager.getInstance();
        CitizenScreen screen = uiFactory.createScreen(STATE_SUB_CATELOG);
        AbstractTnContainer container = screen.getContentContainer();
       
        int categoryId = this.model.getInt(KEY_I_CATEGORY_ID);
        PoiCategory totalCategory = DaoManager.getInstance().getResourceBarDao().getHotPoiNode(this.getRegion());
        PoiCategory poiCategory = null;
        if(categoryId >= 0)
        {
            poiCategory = findPoiCategory(totalCategory, categoryId);
        }
       
        String strTitle = null;
        if(poiCategory != null)
        {
            strTitle = poiCategory.getName();
        }
         
        //add title     
        addTitle(screen.getTitleContainer(),strTitle);
        
        container.setBackgroundColor(uiStyleManager.getColor(UiStyleManager.TEXT_COLOR_WH));

        //add input field container
        TnLinearContainer inputFieldContainer = uiFactory.createLinearContainer(0, true,AbstractTnGraphics.VCENTER | AbstractTnGraphics.HCENTER);
        inputFieldContainer.setBackgroundColor(uiStyleManager.getColor(UiStyleManager.SCREEN_FILTER_BG_COLOR));
        inputFieldContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, uiDecorator.SCREEN_WIDTH);
        inputFieldContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, uiDecorator.COMMON_ONEBOX_HEIGHT);
        inputFieldContainer.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, NinePatchImageDecorator.SEARCH_BOX_BG_UNFOCUS);
        inputFieldContainer.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS,NinePatchImageDecorator.SEARCH_BOX_BG_UNFOCUS);
        addPadding(inputFieldContainer);
        String hint = ResourceManager.getInstance().getCurrentBundle().getString(IStringPoi.MSG_CATEGORY_FILTER_HINT, IStringPoi.FAMILY_POI);
       
        CitizenTextField textField = uiFactory.createCitizenTextField("", 100, TnTextField.ANY, hint, ID_TEXTFIELD_FILTER, ImageDecorator.SEARCHBOX_ICON_HINT_UNFOCUSED.getImage(),ImageDecorator.IMG_AC_BACKSPACE.getImage());
        textField.setPadding(4, 4, 4, 4);
        textField.setBackgroundColor(uiStyleManager.getColor(UiStyleManager.TEXT_COLOR_WH));
        textField.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((PoiSearchUiDecorator) uiDecorator).TEXTFIELD_WIDTH);
        textField.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((PoiSearchUiDecorator) uiDecorator).TEXTFIELD_HEIGHT);
        textField.getTnUiArgs().put(CitizenTextField.KEY_PREFER_TEXTFIELD_HEIGHT, ((PoiSearchUiDecorator) uiDecorator).TEXTFIELD_HEIGHT);
        textField.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_TEXT_FIELD));
        textField.setCommandEventListener(this);
        textField.setBackspaceCommand(CMD_CLEAR_TEXT);
        inputFieldContainer.add(textField);
        container.add(inputFieldContainer);
        
        if(poiCategory != null && poiCategory.getChildrenSize() > 0)
        {
            FrogList list = uiFactory.createList(ID_SUB_CATEGORY_LIST);
            container.add(list);
            categoryListAdapter = new CategoryListAdapter(list, (PoiCategory)poiCategory, totalCategory);
            textField.setKeyworkFilter(categoryListAdapter);
            textField.setCommandEventListener(categoryListAdapter);
            list.setAdapter(categoryListAdapter);
        }
        return screen;
    }

    protected TnScreen createCategoriesSearchScreen()
    {
        CitizenScreen screen = UiFactory.getInstance().createScreen(STATE_SEARCH_CATEGORIES);
        AbstractTnContainer container = screen.getContentContainer();
        
        // add title
        String titleStr = this.model.getString(KEY_S_MORE_TITLE_NAME);
//        initTitleContiner(ID_LABEL_TITLE, screen.getTitleContainer(), "<bold>" + titleStr + "</bold>");
        addTitle(screen.getTitleContainer(),titleStr);
        container.setBackgroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_WH));
        
        //add input field container
        TnLinearContainer inputFieldContainer = UiFactory.getInstance().createLinearContainer(0, true,
            AbstractTnGraphics.VCENTER | AbstractTnGraphics.HCENTER);
        inputFieldContainer.setBackgroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.SCREEN_FILTER_BG_COLOR));
        inputFieldContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, uiDecorator.SCREEN_WIDTH);
        inputFieldContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT,
            uiDecorator.COMMON_ONEBOX_HEIGHT);
        inputFieldContainer.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS,
            NinePatchImageDecorator.SEARCH_BOX_BG_UNFOCUS);
        inputFieldContainer.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS,
            NinePatchImageDecorator.SEARCH_BOX_BG_UNFOCUS);
        addPadding(inputFieldContainer);
       // inputFieldContainer.add(screenTopMargin);
        String hint = ResourceManager.getInstance().getCurrentBundle().getString(IStringPoi.MSG_CATEGORY_FILTER_HINT, IStringPoi.FAMILY_POI);
       
        CitizenTextField textField = UiFactory.getInstance().createCitizenTextField("", 100, TnTextField.ANY, hint, ID_TEXTFIELD_FILTER, ImageDecorator.SEARCHBOX_ICON_HINT_UNFOCUSED.getImage(),
                ImageDecorator.IMG_AC_BACKSPACE.getImage());
        textField.setPadding(4, 4, 4, 4);
        textField.setBackgroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_WH));
        textField.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((PoiSearchUiDecorator) uiDecorator).TEXTFIELD_WIDTH);
        textField.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((PoiSearchUiDecorator) uiDecorator).TEXTFIELD_HEIGHT);
        textField.getTnUiArgs().put(CitizenTextField.KEY_PREFER_TEXTFIELD_HEIGHT, ((PoiSearchUiDecorator) uiDecorator).TEXTFIELD_HEIGHT);
        textField.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_TEXT_FIELD));
        textField.setCommandEventListener(this);
        textField.setBackspaceCommand(CMD_CLEAR_TEXT);
       
        inputFieldContainer.add(textField);
        container.add(inputFieldContainer);
        
        PoiCategory searchCategory = null;
        PoiCategory node = DaoManager.getInstance().getResourceBarDao().getCategoryNode(this.getRegion());
        if(node != null && node.getChildrenSize() > 0)
        {
            int size = node.getChildrenSize();
            for(int i = 0; i < size; i++)
            {
                PoiCategory current = node.getChildAt(i);
                if(current.getCategoryId() == COMPLETE_LIST_ID && current.getChildrenSize() > 0)
                {
                    searchCategory = current;
                    break;
                }
            }
            FrogList list = UiFactory.getInstance().createList(ID_CATEGORY_LIST);
            list.setVerticalFadingEdgeEnabled(false);
            container.add(list);
            categoryListAdapter = new CategoryListAdapter(list, searchCategory, null);
            textField.setKeyworkFilter(categoryListAdapter);
            textField.setCommandEventListener(categoryListAdapter);
            list.setAdapter(categoryListAdapter);
        }
       
        return screen;
    }
   
    private void addPadding(AbstractTnComponent component)
    {
        if (component != null)
        {
            if (titlebarView != null)
            {
                int horizentalPadding = (int) titlebarView.getResources().getDimension(R.dimen.common_side_margin);
                int verticalPadding = (int) titlebarView.getResources().getDimension(
                    R.dimen.placeList0BottomButtonMarginTopBottom);
                component.setPadding(horizentalPadding, verticalPadding, horizentalPadding, verticalPadding);
            }
        }
    }
    
    private TnScreen createQuickFindScreen()
    {
        CitizenScreen screen = UiFactory.getInstance().createScreen(STATE_SEARCH_QUICK_FIND);
        screen.getRootContainer().setBackgroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.POI_ICON_PANEL_COLOR));
        screen.getRootContainer().setSizeChangeListener(this);
        
        String titleStr = ResourceManager.getInstance().getCurrentBundle().getString(IStringCommon.RES_NEARBY, IStringCommon.FAMILY_COMMON);
        addTitle(screen.getTitleContainer(),titleStr);

        
        AbstractTnContainer searchbarContainer =createOneboxSearchBar();
        screen.getContentContainer().add(searchbarContainer);
        
        screen.getContentContainer().setBackgroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.POI_CATEGORY_BG_COLOR));
 
        TnScrollPanel scrollPanel = UiFactory.getInstance().createScrollPanel(0, true);
        screen.getContentContainer().add(scrollPanel);
        scrollPanel.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, this.uiDecorator.SCREEN_WIDTH);
        scrollPanel.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
        {
            public Object decorate(TnUiArgAdapter args)
            {
                int currOrientation = ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation();
                int height = 0;
                if(currOrientation == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                {
                    height = getQuickFindHeight(true);
                }
                else
                {
                    height = getQuickFindHeight(false);
                }
                
                return PrimitiveTypeCache.valueOf(height);
            }
        }));

        int orientation = ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation();
        iconContainer = UiFactory.getInstance().createLinearContainer(0, true, AbstractTnGraphics.HCENTER);
        iconContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((PoiSearchUiDecorator) uiDecorator).SCREEN_WIDTH);
        scrollPanel.set(iconContainer);
        scrollPanel.setVerticalFadingEdgeEnabled(false);
        PoiCategory hotCategoryNode = DaoManager.getInstance().getResourceBarDao().getHotPoiNode(this.getRegion());
        PoiCategory localEvent = DaoManager.getInstance().getResourceBarDao().getLocalEventsNode(this.getRegion());
        
        boolean isEventAvailable = (localEvent != null);
        boolean isHotCategoryAvailable = (hotCategoryNode != null);
        if (orientation == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
        {
            if (isHotCategoryAvailable)
            {
                this.portraintQuickFindContainer = createQuickFindPanel(true, true);
                if(this.portraintQuickFindContainer != null)
                {
                    iconContainer.add(this.portraintQuickFindContainer);
                }
            }
            
            if (isEventAvailable)
            {
                this.portraintLocalEventContainer = createQuickFindPanel(true, false);
                if(this.portraintLocalEventContainer != null)
                {
                    iconContainer.add(this.portraintLocalEventContainer);
                }
            }
        }
        else
        {
            if (isHotCategoryAvailable)
            {
                this.landscapeQuickFindContainer = createQuickFindPanel(false, true);
                if(this.landscapeQuickFindContainer != null)
                {
                    iconContainer.add(this.landscapeQuickFindContainer);
                }
            }
            
            if (isEventAvailable)
            {
                this.landscapeLocalEventContainer = createQuickFindPanel(false, false);
                if(this.landscapeLocalEventContainer != null)
                {
                    iconContainer.add(this.landscapeLocalEventContainer);
                }
            }
        }
        
        if(isHotCategoryAvailable || isEventAvailable)
        {
            oldOrientation = orientation;
        }

        this.contentContainer = (TnLinearContainer) screen.getContentContainer();
        
        if(isPoiDuringNavigation())
        {
            addBottomContainer(screen, getNavigationBottomBarArgs(CMD_STAY_IN_SEARCH));
        }
        else
        {
//            addBottomContainer(screen, getDefaultBottomBarArgs(CMD_COMMON_LINK_TO_SEARCH));
        }
           
        return screen;
    }

    private void addTitle(AbstractTnContainer titleBarContainer,String titleName)
    {
        titleBarContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, uiDecorator.COMMON_TITLE_BUTTON_HEIGHT);
        titleBarContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, uiDecorator.SCREEN_WIDTH);
        titlebarView = AndroidCitizenUiHelper.addContentView(titleBarContainer, R.layout.common_title);       
        TextView tosTextView = (TextView) titlebarView.findViewById(R.id.commonTitle0TextView);
        tosTextView.setText(titleName);
    }
    
    private AbstractTnContainer createOneboxSearchBar()
    {
        AbstractTnContainer searchbarContainer = UiFactory.getInstance().createLinearContainer(0, true,
            AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);
        searchbarContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, uiDecorator.COMMON_ONEBOX_HEIGHT);
        searchbarContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, uiDecorator.SCREEN_WIDTH);
        searchbarContainer.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS,
            NinePatchImageDecorator.SEARCH_BOX_BG_UNFOCUS);
        searchbarContainer.getTnUiArgs()
                .put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.SEARCH_BOX_BG_UNFOCUS);
        addPadding(searchbarContainer);
        searchbarView = AndroidCitizenUiHelper.addContentView(searchbarContainer, R.layout.common_onebox);
        updateOneBoxView();
        return searchbarContainer;
    }

    private void updateOneBoxView()
    {
        if (searchbarView != null)
        {
            ResourceBundle bundle = ResourceManager.getInstance().getCurrentBundle();
            TextView searchBoxHint = (TextView) searchbarView.findViewById(R.id.commonOneboxTextView);
            ImageView DSRimage = (ImageView) searchbarView.findViewById(R.id.commonOneboxDsrButton);
            View oneBoxDSR = (View) searchbarView.findViewById(R.id.commonOneboxDsrButton);
            boolean isOnboard = !NetworkStatusManager.getInstance().isConnected();
            this.model.put(KEY_B_IS_ONBOARD, isOnboard);
            int DSRImageId = isOnboard ? R.drawable.inputbox_mic_icon_disabled : R.drawable.inputbox_mic_icon;
            DSRimage.setImageResource(DSRImageId);
            if (isOnboard)
            {
                searchBoxHint.setText(bundle.getString(IStringCommon.RES_INPUT_HINT_OFFLINE, IStringCommon.FAMILY_COMMON));
            }
            else
            {
                searchBoxHint.setText(bundle.getString(IStringCommon.RES_INPUT_HINT, IStringCommon.FAMILY_COMMON));
            }

            AndroidCitizenUiHelper.setOnClickCommand(this, oneBoxDSR, isOnboard ? CMD_NONE : CMD_COMMON_DSR);
            AndroidCitizenUiHelper.setOnClickCommand(this, searchBoxHint, CMD_COMMON_GOTO_ONEBOX);
        }
    }
    
    private boolean isPoiDuringNavigation()
    {
        boolean isPoiDuringNavigation = NavRunningStatusProvider.getInstance().isNavRunning();
        return isPoiDuringNavigation;
    }
    
    private TnLinearContainer createQuickFindPanel(boolean isPortrait, boolean isCategory)
    {
        if(AppConfigHelper.isTabletSize())
            PoiSearchUiDecorator.QUICK_SEARCHING_LINE_ICON_NUM_L = 6;
        
        PoiCategory nearByNode = null;
        
        if(isCategory)
        {
            nearByNode = DaoManager.getInstance().getResourceBarDao().getHotPoiNode(this.getRegion());
        }
        else
        {
            nearByNode = DaoManager.getInstance().getResourceBarDao().getLocalEventsNode(this.getRegion());
        }
        
        if(nearByNode == null || nearByNode.getChildrenSize() <= 0)
            return null;
        
        int sizePerRow = PoiSearchUiDecorator.QUICK_SEARCHING_LINE_ICON_NUM_L;
        if(isPortrait)
            sizePerRow = PoiSearchUiDecorator.QUICK_SEARCHING_LINE_ICON_NUM_P;
        
        int preferRowNumber = 0;
        if(isPortrait)
        {
            preferRowNumber = QUICK_SEARCHING_PREFER_ROW_NUM_P;
        }
        else
        {
            preferRowNumber = QUICK_SEARCHING_PREFER_ROW_NUM_L;
        }
        
        int buttonSize = nearByNode.getChildrenSize();
//        label.setFont(font)
        TnLinearContainer quickFindPanel = createQuickFindPanel(sizePerRow, buttonSize, preferRowNumber, getQuickFindWidth(isPortrait), getQuickFindHeight(isPortrait), isCategory);
     
        return quickFindPanel;
    }
    
    private TnLinearContainer createQuickFindPanel(int iconSizePerRow, int iconsSize, final int preferRowNumber, final int containerWidth,
            final int containerHeight, final boolean isCategory)
    {
        TnLinearContainer tableContainer = UiFactory.getInstance().createLinearContainer(0, true, AbstractTnGraphics.HCENTER);
        
        FrogLabel labelTitle = UiFactory.getInstance().createLabel(0, "");
        labelTitle.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_POI_CATEGORY_TITLE));
        
        PoiCategory node = null;
        
        if(isCategory)
        {
            labelTitle.setForegroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_WH), UiStyleManager
                    .getInstance().getColor(UiStyleManager.TEXT_COLOR_WH));
            labelTitle.setText(ResourceManager.getInstance().getText(IStringPoi.LABEL_PLACE_TO_GO, IStringPoi.FAMILY_POI));
            tableContainer.setBackgroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.BG_CATEGORY_PANEL));
            node = ((DaoManager)DaoManager.getInstance()).getResourceBarDao().getHotPoiNode(this.getRegion());
        }
        else
        {
            labelTitle.setForegroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.POI_LOCAL_EVENT_TEXT_COLOR), UiStyleManager
                .getInstance().getColor(UiStyleManager.POI_LOCAL_EVENT_TEXT_COLOR));
            labelTitle.setText(ResourceManager.getInstance().getText(IStringPoi.LABEL_THINGS_TO_DO, IStringPoi.FAMILY_POI));
            tableContainer.setBackgroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.BG_LOCAL_EVENT_PANEL));
            node = ((DaoManager)DaoManager.getInstance()).getResourceBarDao().getLocalEventsNode(this.getRegion());
        }
        
        if(node == null || node.getChildrenSize() == 0)
        {
            return tableContainer;
        }
        
        AbstractTnImage icon = getSampleCategoryIcon(node.getChildAt(0));
        if(icon == null)
        {
            return tableContainer;
        }
        
        FrogNullField nullFieldTopMargin = UiFactory.getInstance().createNullField(0);
        nullFieldTopMargin.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT,
            ((PoiSearchUiDecorator) uiDecorator).PANEL_TOP_NULLFIELD_HEIGHT);
        
        tableContainer.add(nullFieldTopMargin);
        tableContainer.add(labelTitle);
        final int iconWidth = Math.max(icon.getWidth(), PrimitiveTypeCache.valueOf(AppConfigHelper.getMaxDisplaySize() * 1000 / 10000));
        final int iconHeight = Math.max(icon.getHeight(), PrimitiveTypeCache.valueOf(AppConfigHelper.getMaxDisplaySize() * 1000 / 10000));
        
        int num = iconsSize / iconSizePerRow;
        if(iconsSize % iconSizePerRow != 0)
            num ++;
        final int rowSize = num;
        final int fontHeight = UiStyleManager.getInstance().getFont(UiStyleManager.FONT_POI_CATEGORY_BUTTON_TEXT).getHeight();
        boolean isNeedRowGap = (iconHeight + fontHeight) * rowSize < containerHeight;
        boolean isNeedIconsGap = ((PoiSearchUiDecorator) uiDecorator).QUICK_FIND_ICON_BTN_WIDTH.getInt() * iconSizePerRow < containerWidth;
        TnUiArgAdapter gapAdapter = null;
        if (isNeedRowGap)
        {
            gapAdapter = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
            {
                public Object decorate(TnUiArgAdapter args)
                {
                    int gapHeight = 0;
                    gapHeight = AppConfigHelper.getMaxDisplaySize() * 1796 / 100000;
                    return PrimitiveTypeCache.valueOf(gapHeight);
                }
            });
        }
        else if(preferRowNumber > 0) // if the rows user can firstly see is setting by VDD, we can custom it by add gap.
        {
            gapAdapter = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
            {
                public Object decorate(TnUiArgAdapter args)
                {
                    int blankHeight = containerHeight
                            - (iconHeight + fontHeight) * preferRowNumber
                            + fontHeight / 3 /* for protection */;
                    int gapHeight = blankHeight / (rowSize + 1);
                    return PrimitiveTypeCache.valueOf(gapHeight);
                }
            });
        }
        
        if(lastItemsAnchor == AbstractTnGraphics.LEFT)
        {
            if(iconsSize % iconSizePerRow > 0)
            {
                iconsSize = ( (iconsSize / iconSizePerRow) + 1 ) * iconSizePerRow; 
            }
        }
        
        TnLinearContainer line = null;
        

        for(int i = 0; i < iconsSize; i++)
        {
            boolean isFirstElementInLine = (i % iconSizePerRow == 0);
            boolean isLastElementInLine = (i % iconSizePerRow == iconSizePerRow - 1);
            boolean isInFirstLine =i / iconSizePerRow == 0;
            if(isFirstElementInLine)
            {
                if(isNeedRowGap || preferRowNumber > 0)
                {
                    if (!isInFirstLine)
                    {
                        FrogNullField lineSpacing = UiFactory.getInstance().createNullField(0);
                        TnUiArgs lineSpaceArgs = lineSpacing.getTnUiArgs();
                        lineSpaceArgs.put(TnUiArgs.KEY_PREFER_HEIGHT, gapAdapter);
                        tableContainer.add(lineSpacing);
                        tableContainer.add(addSeperateline(isCategory));
                    }
                }
                
                line = UiFactory.getInstance().createLinearContainer(0, false, AbstractTnGraphics.HCENTER);
                line.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, uiDecorator.SCREEN_WIDTH);
                tableContainer.add(line);
            }
            
            boolean isLastRow = (i + 1) / iconSizePerRow == ( rowSize - 1 ) && (i + 1) % iconSizePerRow > 0;
            if(isLastRow)
            {
                isLastElementInLine = i == (iconsSize - 1);
            }
            
            if(line != null)
            {
                line.add(getIconCommand(i, false, node.getChildAt(i), iconWidth, iconHeight, isCategory, isInFirstLine));
                
                if(isNeedIconsGap && !isLastElementInLine)
                {
                    int size = iconSizePerRow;
                    
                    if(isLastRow)
                    {
                        size = iconsSize - iconSizePerRow * (rowSize - 1);
                    }
                    
                    line.add(getGapField(containerWidth, size));
                }
            }
            
            if (i == iconsSize - 1)
            {
                if (isCategory)
                {
                    this.model.put(KEY_S_MORE_TITLE_NAME, node.getChildAt(i).getName());
                }
            }
        }
        
        FrogNullField bottomGap = UiFactory.getInstance().createNullField(0);
        bottomGap.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, gapAdapter);
        bottomGap.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, uiDecorator.SCREEN_WIDTH);
        tableContainer.add(bottomGap);
        
        return tableContainer;
    }
    
    private AbstractTnComponent addSeperateline(boolean isCategory)
    {
        AbstractTnComponent poiCategorySeperateLineComponent = UiFactory.getInstance().createLabel(0, "");
        if (isCategory)
        {
            poiCategorySeperateLineComponent.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS,
                NinePatchImageDecorator.POI_CATEGORY_SEPERATE_LINE_UNFOCUSED);
            poiCategorySeperateLineComponent.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS,
                NinePatchImageDecorator.POI_CATEGORY_SEPERATE_LINE_UNFOCUSED);
        }
        else
        {
            poiCategorySeperateLineComponent.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS,
                NinePatchImageDecorator.POI_GOBY_LOCAL_EVENT_SEPERATE_LINE_UNFOCUSED);
            poiCategorySeperateLineComponent.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS,
                NinePatchImageDecorator.POI_GOBY_LOCAL_EVENT_SEPERATE_LINE_UNFOCUSED);
        }
        poiCategorySeperateLineComponent.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((PoiSearchUiDecorator)uiDecorator).POI_LIST_SEPERATE_LINE_HEIGHT);
        poiCategorySeperateLineComponent.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, uiDecorator.SCREEN_WIDTH);
        return poiCategorySeperateLineComponent;
    }
    
    
    private AbstractTnImage getSampleCategoryIcon(PoiCategory cate)
    {
        AbstractTnImage icon;
        icon = new TnUiArgAdapter(cate.getUnfocusedImagePath() + ".png", ImageDecorator.instance).getImage();
        if(icon != null)
        {
            return icon;
        }
        icon = new TnUiArgAdapter(cate.getFocusedImagePath() + ".png", ImageDecorator.instance).getImage();
        if(icon != null)
        {
            return icon;
        }
        icon = new TnUiArgAdapter(ISpecialImageRes.POI_QUICK_SEARCH_PARKING_UNFOCUSED_ICON, ImageDecorator.instance).getImage();
        if(icon != null)
        {
            return icon;
        }
        icon = new TnUiArgAdapter(ISpecialImageRes.POI_QUICK_SEARCH_PARKING_FOCUSED_ICON, ImageDecorator.instance).getImage();
        if(icon != null)
        {
            return icon;
        }
        return null;
    }
    
    private int getQuickFindHeight(boolean isPortrait)
    {
        int screenHeight = AppConfigHelper.getDisplayHeight() > AppConfigHelper.getDisplayWidth() ? AppConfigHelper.getDisplayHeight()
                : AppConfigHelper.getDisplayWidth();
        if(!isPortrait)
        {
            screenHeight = AppConfigHelper.getDisplayHeight() < AppConfigHelper.getDisplayWidth() ? AppConfigHelper.getDisplayHeight()
                    : AppConfigHelper.getDisplayWidth();

        }
        
        int bottomBarHeight = ((PoiSearchUiDecorator) uiDecorator).BOTTOM_BAR_REAL_HEIGHT.getInt();
        if(!isPoiDuringNavigation())
        {
            bottomBarHeight = 0;
        }
        int containerHeight = screenHeight - AppConfigHelper.getStatusBarHeight() 
                - uiDecorator.COMMON_TITLE_BUTTON_HEIGHT.getInt()
                -  uiDecorator.COMMON_ONEBOX_HEIGHT.getInt()
//                - ((PoiSearchUiDecorator) uiDecorator).QUICK_FIND_TOPMARGIN_HEIGHT.getInt()
                - bottomBarHeight;
        return containerHeight;
    }
    
    private int getQuickFindWidth(boolean isPortrait)
    {
        if(isPortrait)
        {
            int screenWidth = AppConfigHelper.getDisplayHeight() < AppConfigHelper.getDisplayWidth() ? AppConfigHelper.getDisplayHeight()
                    : AppConfigHelper.getDisplayWidth();

            return screenWidth;
        }
        else
        {
            int screenWidth = AppConfigHelper.getDisplayHeight() > AppConfigHelper.getDisplayWidth() ? AppConfigHelper.getDisplayHeight()
                    : AppConfigHelper.getDisplayWidth();

            return screenWidth;
        }
    }

    private AbstractTnComponent getGapField(final int containerWidth, final int iconSizePerRow)
    {
        FrogNullField field = UiFactory.getInstance().createNullField(0);
        
        field.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
        {
            public Object decorate(TnUiArgAdapter args)
            {
                int btnWidth = ((PoiSearchUiDecorator) uiDecorator).QUICK_FIND_ICON_BTN_WIDTH.getInt() * iconSizePerRow;
                int gap = ( containerWidth - btnWidth ) / ( iconSizePerRow + 1);
                return PrimitiveTypeCache.valueOf(gap);
            }
        }));
        
        return field;
    }

    private String getFocusImageByUnfocusImage(String unFocusImage)
    {
        String focusImage = null;
        if(unFocusImage !=null && unFocusImage.length()>0)
        {
            int index=unFocusImage.indexOf("unfocused");
            if (index > 0)
            {
                focusImage = unFocusImage.substring(0, index) + "focused";
            }
        }
        return focusImage;
    }
    
    private AbstractTnComponent getIconCommand(int i, boolean isLandscape, PoiCategory currentCategoryNode, int iconWidth, int iconHeight, boolean isCategory, boolean isInFirstLine)
    {
        String label = currentCategoryNode.getName();
        int categoryId = currentCategoryNode.getCategoryId();
        String eventId = currentCategoryNode.getEventId();
        String unfocusedImageResString = currentCategoryNode.getUnfocusedImagePath();
        String focusedImageResString = getFocusImageByUnfocusImage(unfocusedImageResString);      
        CitizenQuickFindButton iconButton = UiFactory.getInstance().createQuickFindButton(categoryId, label);
        TnUiArgs uiArgs = iconButton.getTnUiArgs();
        uiArgs.put(TnUiArgs.KEY_PREFER_WIDTH,
            ((PoiSearchUiDecorator) this.uiDecorator).QUICK_FIND_ICON_BTN_WIDTH);
        
        uiArgs.remove(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS);
        uiArgs.remove(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS);
        
        TnUiArgAdapter unfocusedImageAdapter = new TnUiArgAdapter(unfocusedImageResString + ".png", ImageDecorator.instance);
        if (focusedImageResString != null)
        {
            TnUiArgAdapter focusedImageAdapter = new TnUiArgAdapter(focusedImageResString + ".png", ImageDecorator.instance);
            iconButton.setIcon(focusedImageAdapter.getImage(), unfocusedImageAdapter.getImage(), AbstractTnGraphics.TOP);
        }
        else
        {
            iconButton.setIcon(unfocusedImageAdapter.getImage(), unfocusedImageAdapter.getImage(), AbstractTnGraphics.TOP);
        }
//        unfocusedImageAdapter.getImage().setWidth(iconWidth);
//        unfocusedImageAdapter.getImage().setHeight(iconHeight);
//        
//        focusedImageAdapter.getImage().setWidth(iconWidth);
//        focusedImageAdapter.getImage().setHeight(iconHeight);
        
//        iconButton.setSympolImage(unfocusedImageAdapter.getImage(), focusedImageAdapter.getImage());

//        TnNinePatchImage backBgImageUnFocused = (TnNinePatchImage) NinePatchImageDecorator.instance.decorate(NinePatchImageDecorator.QUICK_FIND_BUTTON_UNFOCUSED);
//        TnNinePatchImage backBgImageFocused = (TnNinePatchImage) NinePatchImageDecorator.instance.decorate(NinePatchImageDecorator.QUICK_FIND_BUTTON_FOCUSED);
//        backBgImageUnFocused.setWidth(iconWidth);
//        backBgImageUnFocused.setHeight(iconHeight);
//        backBgImageFocused.setWidth(iconWidth);
//        backBgImageFocused.setHeight(iconHeight);
               
        int topPadding = 0;
       
        if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_LANDSCAPE)
        {
            if (isInFirstLine)
            {
                topPadding = Math.round(AppConfigHelper.getMaxDisplaySize() * 234 / 100000f);
            }
            else
            {
                topPadding = Math.round(AppConfigHelper.getMaxDisplaySize() * 937 / 100000f);
            }
        }
        else
        {
            topPadding = Math.round(AppConfigHelper.getMaxDisplaySize() * 1562 / 100000f);
        }
        
        iconButton.setPadding(0, topPadding, 0, 1);
        iconButton.setShadow(3);
        iconButton.setCommandEventListener(this);
            
        TnMenu menu = UiFactory.getInstance().createMenu();
        if(isCategory)
        {
            if (categoryId == ID_MORE_CMD)
            {
                menu.add("", CMD_GOTO_SEARCH_CATEGORIES);
            }
            else
            {
                if(currentCategoryNode.getChildrenSize() > 0)
                {
                    menu.add("", CMD_GOTO_SUB_CATELOG);
                }
                else
                {
                    menu.add("", CMD_QUICK_BUTTON_SELECTED);
                }
            }
        }
        else
        {
            iconButton.setIsEvent(true);
            iconButton.setEventId(eventId);
            menu.add("", CMD_EVENT_SELECTED);
        }
        
        iconButton.setMenu(menu, AbstractTnComponent.TYPE_CLICK);
        iconButton.setBackgroundCircleFocusedColor(UiStyleManager.getInstance().getColor(
            UiStyleManager.DASHBOARD_HIGHLIGHT_FOCUSED_COLOR));
        int poiCategoryColor = isCategory ? UiStyleManager.getInstance().getColor(UiStyleManager.POI_CATEGORY_TEXT_COLOR)
                : UiStyleManager.getInstance().getColor(UiStyleManager.POI_LOCAL_EVENT_TEXT_COLOR);
        AbstractTnFont buttonFont = isCategory ? UiStyleManager.getInstance().getFont(
            UiStyleManager.FONT_POI_CATEGORY_BUTTON_TEXT) : UiStyleManager.getInstance().getFont(
            UiStyleManager.FONT_POI_LOCAL_EVENT_BUTTON_TEXT);
        iconButton.setForegroundColor(poiCategoryColor, poiCategoryColor);
        iconButton.setFont(buttonFont);
        
        return iconButton;
//        else if(lastItemsAnchor == AbstractTnGraphics.LEFT)//return NullField for AbstractTnGraphic.LEFT layout handling.
//        {
//            FrogNullField field = UiFactory.getInstance().createNullField(0);
//            field.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((PoiSearchUiDecorator) this.uiDecorator).QUICK_FIND_ICON_BTN_WIDTH);
//            return field;
//        }
    }
    
    protected boolean updateScreen(int state, TnScreen screen)
    {
        if (state == STATE_SEARCH_QUICK_FIND )
        {
//            CitizenQuickSearchBar quickSearchBar = (CitizenQuickSearchBar) screen.getComponentById(ID_QUICK_SEARCH_BAR);
//            if (quickSearchBar != null)
//            {
//                boolean isOnboard = this.model.getBool(KEY_B_IS_ONBOARD);
//                String hint = isOnboard ? ResourceManager.getInstance().getCurrentBundle().getString(IStringCommon.RES_INPUT_HINT_OFFLINE, IStringCommon.FAMILY_COMMON)
//                        :ResourceManager.getInstance().getCurrentBundle().getString(IStringCommon.RES_INPUT_HINT, IStringCommon.FAMILY_COMMON);
//                if (isOnboard)
//                {
//                    if (quickSearchBar.isLeftButtonEnabled())
//                    {
//                        quickSearchBar.disableLeftButton(ImageDecorator.SEARCHBOX_MIC_DISABLED.getImage());
//                        quickSearchBar.setHintText(hint);
//                    }
//                }
//                else
//                {
//                    if (!quickSearchBar.isLeftButtonEnabled())
//                    {
//                        quickSearchBar.enableLeftButton(ImageDecorator.SEARCHBOX_MIC_UNFOCUSED.getImage());
//                        quickSearchBar.setHintText(hint);
//                    }
//                }
//            }
            updateOneBoxView();
            if (this.iconContainer != null)
            {
                int orintation = ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance())
                        .getOrientation();
                AbstractTnComponent component = this.iconContainer.get(0);
                
                if (orintation == oldOrientation && !(component instanceof FrogList))
                {
                    return false;
                }
                oldOrientation = orintation;

                if (orintation == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                {
                    if (portraintQuickFindContainer == null)
                    {
                        this.portraintQuickFindContainer = createQuickFindPanel(true, true);
                    }
                    if (portraintLocalEventContainer == null)
                    {
                        this.portraintLocalEventContainer = createQuickFindPanel(true, false);
                    }

                    iconContainer.removeAll();
                    if(portraintQuickFindContainer != null)
                    {
                        iconContainer.add(portraintQuickFindContainer);
                    }
                    if(portraintQuickFindContainer != null)
                    {
                        iconContainer.add(portraintLocalEventContainer);
                    }
                }
                else
                {
                    if (landscapeQuickFindContainer == null)
                    {
                        landscapeQuickFindContainer = createQuickFindPanel(false, true);
                    }
                    if (landscapeLocalEventContainer == null)
                    {
                        landscapeLocalEventContainer = createQuickFindPanel(false, false);
                    }

                    iconContainer.removeAll();
                    if(landscapeQuickFindContainer != null )
                    {
                        iconContainer.add(landscapeQuickFindContainer);
                    }
                    if(landscapeLocalEventContainer != null )
                    {
                        iconContainer.add(landscapeLocalEventContainer);
                    }
                }
                return true;
            }
        }
        return false;
    }
    
    public void onSizeChanged(AbstractTnComponent tnComponent, int w, int h,
            int oldw, int oldh)
    {
        switch(model.getState())
        {
            case STATE_SEARCH_QUICK_FIND:
            case STATE_POI_SEARCHING:
            {
                int orintation = ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation();
                if (orintation == oldOrientation)
                    return;

                oldOrientation = orintation;
                if (this.iconContainer != null)
                {
                    if (orintation == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                    {
                        if(portraintQuickFindContainer == null)
                        {
                            this.portraintQuickFindContainer = createQuickFindPanel(true, true);
                        }
                        if(portraintLocalEventContainer == null)
                        {
                            this.portraintLocalEventContainer = createQuickFindPanel(true, false);
                        }
                        if(landscapeQuickFindContainer != null)
                        {
                            iconContainer.remove(landscapeQuickFindContainer);
                        }
                        if(landscapeLocalEventContainer != null)
                        {
                            iconContainer.remove(landscapeLocalEventContainer);
                        }
                        if(portraintQuickFindContainer != null)
                        {
                            iconContainer.add(portraintQuickFindContainer);
                        }
                        if(portraintLocalEventContainer != null)
                        {
                            iconContainer.add(portraintLocalEventContainer);
                        }
                    }
                    else
                    {
                        if(landscapeQuickFindContainer == null)
                        {
                            landscapeQuickFindContainer = createQuickFindPanel(false, true);
                        }
                        if(landscapeLocalEventContainer == null)
                        {
                            landscapeLocalEventContainer = createQuickFindPanel(false, false);
                        }
                        if(portraintQuickFindContainer != null)
                        {
                            iconContainer.remove(portraintQuickFindContainer);
                        }
                        if(portraintLocalEventContainer != null)
                        {
                            iconContainer.remove(portraintLocalEventContainer);
                        }
                        if(landscapeQuickFindContainer != null)
                        {
                            iconContainer.add(landscapeQuickFindContainer);
                        }
                        if(landscapeLocalEventContainer != null)
                        {
                            iconContainer.add(landscapeLocalEventContainer);
                        }
                    }
                    
                    if (isPoiDuringNavigation())
                    {
                        if(this.absoluteContainer == null )
                        {
                            if(this.contentContainer != null)
                            {
                                TnScreen screen = ((AbstractTnUiHelper)AbstractTnUiHelper.getInstance()).getCurrentScreen();
                                addBottomContainer((CitizenScreen)screen, getDefaultBottomBarArgs(CMD_COMMON_LINK_TO_SEARCH));
                            }
                        }
                        else
                        {
                            showBottomContainer();
                        }
                    }
                    
                }
                break;
            }
            case STATE_SEARCH_CATEGORIES:
            case STATE_SUB_CATELOG:
            {
                if(categoryListAdapter != null)
                    categoryListAdapter.reset();
                break;
            }
        }
        ((AndroidUiHelper) AbstractTnUiHelper.getInstance()).setFontDensity(false);
        super.onSizeChanged(tnComponent, w, h, oldw, oldh);
    }
    
    public boolean handleUiEvent(TnUiEvent tnUiEvent)
    {
        int currState = this.model.getState();
        logKtUIEvent(tnUiEvent);
        AbstractTnComponent textField = ((AbstractTnUiHelper)AbstractTnUiHelper.getInstance()).getCurrentScreen().getComponentById(ID_TEXTFIELD_FILTER);
        if(currState == STATE_SEARCH_CATEGORIES && textField instanceof CitizenTextField)
        {
            AbstractTnComponent categoryListComponent = ((AbstractTnUiHelper)AbstractTnUiHelper.getInstance()).getCurrentScreen().getComponentById(ID_CATEGORY_LIST);
            if(!(categoryListComponent instanceof FrogList))
            {
                return super.handleUiEvent(tnUiEvent);
            }
            FrogList frogList = (FrogList)categoryListComponent;
            CategoryListAdapter categoryListAdapter = (CategoryListAdapter)frogList.getAdapter();
            switch(tnUiEvent.getType())
            {
                case TnUiEvent.TYPE_COMMAND_EVENT:
                {
                    int id = tnUiEvent.getComponent().getId();
                    if(categoryListAdapter!= null )
                    {
                        if(tnUiEvent.getCommandEvent().getCommand() == CMD_SELECT_CATEGORY_LIST)
                        {
                            if(handCategorySelected(id, categoryListAdapter, categoryListAdapter.getCurrentCategory()))
                            {
                                return true;
                            }
                        }
                    }
                    
                    break;
                }
                case  TnUiEvent.TYPE_KEY_EVENT:
                {
                    if (categoryListAdapter != null
                            && tnUiEvent.getKeyEvent().getAction() == TnKeyEvent.ACTION_DOWN
                            && tnUiEvent.getKeyEvent().getCode() == TnKeyEvent.KEYCODE_BACK)
                    {
                        PoiCategory currNode = categoryListAdapter.getCurrentCategory();
                        if (currNode != null)
                        {
                            PoiCategory parentNode = currNode.getParent();
                            if (parentNode != null && parentNode.getParent() !=null)
                            {
//                                if (parentNode.getParent() == null)// root category, set title "select a category"
//                                {
//                                    String strCategory = ResourceManager.getInstance().getCurrentBundle()
//                                            .getString(IStringPoi.LABEL_SELECT_A_CATEGORY, IStringPoi.FAMILY_POI);
//                                    TnScreen screen = (TnScreen) lookup(this.model.getState());
//                                    AbstractFrogLabel label = (AbstractFrogLabel) screen.getComponentById(ID_LABEL_TITLE);
//                                    label.setText("<bold>" + strCategory + "</bold>");
//                                    categoryListAdapter.setCurrentNode(parentNode);
//                                }
//                                TnScreen screen = (TnScreen)lookup(this.model.getState());
                                String titleName;
                                if (parentNode.getCategoryId() == COMPLETE_LIST_ID)
                                    titleName = this.model.getString(KEY_S_MORE_TITLE_NAME);
                                else
                                    titleName = parentNode.getName();
                                if(titlebarView !=null)
                                {
                                    TextView tosTextView = (TextView) titlebarView.findViewById(R.id.commonTitle0TextView);
                                    tosTextView.setText(titleName);
                                }
                                categoryListAdapter.setCurrentNode(parentNode);
                                return true;
                            }
                        }

                    }
                    break; 
                }
            }
        }
        return super.handleUiEvent(tnUiEvent);
    }
    
    private void logKtUIEvent(TnUiEvent tnUiEvent)
    {
        int currState = this.model.getState();
        if(currState ==  STATE_SEARCH_QUICK_FIND && tnUiEvent != null && (tnUiEvent.getType() == TnUiEvent.TYPE_COMMAND_EVENT))
        {
            int command = tnUiEvent.getCommandEvent().getCommand();
            switch (command)
            {
                case CMD_GOTO_SEARCH_CATEGORIES:
                {
                    KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_NEARBY, KontagentLogger.NEARBY_SEEALL_CLICKED);
                    break;
                }
                case CMD_QUICK_BUTTON_SELECTED:
                case CMD_GOTO_SUB_CATELOG:
                {
                    CitizenQuickFindButton button = (CitizenQuickFindButton)tnUiEvent.getComponent();
                    if(button != null)
                    {
                        String poiCategory = button.getText();
                        poiCategory = poiCategory.replace(" ", "");
                        KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_NEARBY, poiCategory+"_Clicked");
                    }
                    break;
                }
                case CMD_EVENT_SELECTED:
                {
                    CitizenQuickFindButton button = (CitizenQuickFindButton)tnUiEvent.getComponent();
                    if(button != null)
                    {
                        String poiCategory = button.getText();
                        poiCategory = poiCategory.replace(" ", "");
                        KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_EVENTS, poiCategory+"_Clicked");
                    }
                    break;
                }
                case CMD_GOTO_ROUTE_SUMMARY:
                {
                    KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_NAVIGATION,
                        KontagentLogger.NAVIGATION_ROUTE_CLICKED);
                    break;
                }
                case CMD_GOTO_NAV:
                {
                    KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_NAVIGATION,
                        KontagentLogger.NAVIGATION_NAVIGATION_CLICKED);
                    break;
                }
            }
        }
//        else if(tnUiEvent.getType() == TnUiEvent.TYPE_KEY_EVENT && tnUiEvent.getKeyEvent() != null && tnUiEvent.getKeyEvent().getAction() == TnKeyEvent.ACTION_DOWN
//                && tnUiEvent.getKeyEvent().getCode() == TnKeyEvent.KEYCODE_BACK)
//        {
//            KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_NEARBY, KontagentLogger.NEARBY_BACK_CLICKED);
//        }
    }
    
    private boolean handCategorySelected(int id, CategoryListAdapter categoryListAdapter,  PoiCategory currentNode)
    {
        if(id >=0 && id <= currentNode.getChildrenSize())
        {
            PoiCategory currNode = currentNode.getChildAt(id);
            if(currNode.getChildrenSize() == 0)
            {
                //return false let state machine to handle it.
                return false;
            }
            else
            {
				//change title's string
                if(titlebarView !=null)
                {
                    TextView tosTextView = (TextView) titlebarView.findViewById(R.id.commonTitle0TextView);
                    tosTextView.setText(currNode.getName());
                }
                categoryListAdapter.setCurrentNode(currNode);
            }
            return true;
        }
        return false;
    }

    protected boolean preProcessUIEvent(TnUiEvent tnUiEvent)
    {
    	this.model.remove(KEY_B_IS_MOST_PUPOLAR_SEARCH);
        int state = model.getState();
        switch(state)
        {
            case STATE_SEARCH_CATEGORIES:
            {
                AbstractTnComponent categoryListComponent = ((AbstractTnUiHelper)AbstractTnUiHelper.getInstance()).getCurrentScreen().getComponentById(ID_CATEGORY_LIST);
                AbstractTnComponent filterInputBox = ((AbstractTnUiHelper)AbstractTnUiHelper.getInstance()).getCurrentScreen().getComponentById(ID_TEXTFIELD_FILTER);
                if(tnUiEvent.getType() == TnUiEvent.TYPE_COMMAND_EVENT
                        && tnUiEvent.getComponent() instanceof FrogListItem
                        && categoryListComponent instanceof FrogList
                        && filterInputBox instanceof CitizenTextField)
                {
                    CitizenTextField textField = (CitizenTextField)filterInputBox;
                    FrogList list = (FrogList)categoryListComponent;
                    CategoryListAdapter listAdapter = (CategoryListAdapter)list.getAdapter();
                   
                    int categoryIndex = tnUiEvent.getComponent().getId();
                    if(listAdapter != null && listAdapter.getCurrentCategory() != null)
                    {
                        if (textField != null && textField.getText() != null)
                        {
                            PoiCategory currentNode = listAdapter.getCurrentCategory();
                            String currentNodeName = null;
                            if(currentNode != null)
                            {
                                currentNodeName = currentNode.getName();
                                int currentNodeId = currentNode.getCategoryId();
                                if (categoryIndex >= 0
                                        && categoryIndex < currentNode.getChildrenSize())
                                {
                                    PoiCategory selectedCategory = currentNode
                                            .getChildAt(categoryIndex);
                                    int categoryId = (int) selectedCategory.getCategoryId();
                                    String categoryText = selectedCategory.getName();
                                    boolean isMostPopular = selectedCategory.isMostPopular();
//                                    if(currentNodeId > DIRECTORY_ID && currentNodeName != null && currentNodeName.trim().length() > 0)
//                                    {
//                                        String resString = ResourceManager.getInstance().getCurrentBundle().getString(IStringPoi.SUB_CATEGORY, IStringPoi.FAMILY_POI);
//                                        StringConverter converter = ResourceManager.getInstance().getStringConverter();
//                                        categoryText = converter.convert(resString, new String[]{categoryText}); 
//                                        categoryText = currentNodeName + " " + categoryText;
//                                    }
                                    this.model.put(KEY_I_CATEGORY_ID, categoryId);
                                    this.model.put(KEY_S_COMMON_SHOW_SEARCH_TEXT,
                                        categoryText);
                                    this.model.put(KEY_B_IS_MOST_PUPOLAR_SEARCH, isMostPopular);
                                    KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_NEARBY, categoryText + "_Clicked");
                                }
                            }
                        }
                        
                    }
                    
                }
                break;
            }

            case STATE_SEARCH_QUICK_FIND:
            {
                if(tnUiEvent.getType() == TnUiEvent.TYPE_COMMAND_EVENT
                        && tnUiEvent.getComponent() instanceof CitizenQuickFindButton)
                {
                    CitizenQuickFindButton quickFindButton = (CitizenQuickFindButton)tnUiEvent.getComponent();
                    boolean isEvent = quickFindButton.isEvent();
                    
                    if(isEvent)
                    {
                        String eventId = quickFindButton.getEventId();
                        model.put(KEY_S_LOCAL_EVENT_ID, eventId);
                        model.put(KEY_S_LOCAL_EVENT_NAME, 
                            ((CitizenQuickFindButton) tnUiEvent.getComponent()).getText());
                    }
                    else
                    {
                        int categoryId = tnUiEvent.getComponent().getId();
                        
                        this.model.put(KEY_I_CATEGORY_ID, categoryId);
                        this.model.put(KEY_S_COMMON_SHOW_SEARCH_TEXT,
                            ((CitizenQuickFindButton) tnUiEvent.getComponent()).getText());
                    }
                }
                else if (tnUiEvent.getType() == TnUiEvent.TYPE_COMMAND_EVENT
                        && tnUiEvent.getCommandEvent().getCommand() == CMD_COMMON_GOTO_ONEBOX)
                {
                    if (tnUiEvent.getComponent() instanceof FrogTextField)
                    {
                        FrogTextField textField = (FrogTextField) tnUiEvent
                                .getComponent();
                        String text = textField.getText();
                        this.model.put(KEY_S_COMMON_SEARCH_TEXT, text);
                        textField.setText("");
                    }
                }
                break;
            }
            case STATE_SUB_CATELOG:
            {
                AbstractTnComponent categoryListComponent = ((AbstractTnUiHelper)AbstractTnUiHelper.getInstance()).getCurrentScreen().getComponentById(ID_SUB_CATEGORY_LIST);
                if(tnUiEvent.getType() == TnUiEvent.TYPE_COMMAND_EVENT
                        && tnUiEvent.getComponent() instanceof FrogListItem
                        && categoryListComponent instanceof FrogList)
                {
                    FrogAdapter frogAdapter = ((FrogList)categoryListComponent).getAdapter();
                    if(frogAdapter instanceof CategoryListAdapter)
                    {
                        PoiCategory poiCategory = ((CategoryListAdapter)frogAdapter).getCurrentCategory();
                        if(poiCategory != null)
                        {
                            int index =  tnUiEvent.getComponent().getId();
                            PoiCategory selectedCategory = poiCategory.getChildAt(index);
                            if(selectedCategory != null)
                            {
                                int categoryId = selectedCategory.getCategoryId();
                                String categoryText = "";
                                if(selectedCategory.getParent() != null && selectedCategory.getParent().getName() != null
                                        && selectedCategory.getParent().getName().trim().length() != 0)
                                {
                                    categoryText = selectedCategory.getParent().getName() ;
                                }
                                String categoryName = selectedCategory.getName();
                                if(categoryText.length() > 0)
                                {
                                    if (categoryName.indexOf('(') < 0
                                            || categoryName.indexOf(')') < 0)
                                    {
                                        String resString = ResourceManager
                                                .getInstance()
                                                .getCurrentBundle()
                                                .getString(IStringPoi.SUB_CATEGORY,
                                                    IStringPoi.FAMILY_POI);
                                        StringConverter converter = ResourceManager
                                                .getInstance().getStringConverter();
                                        categoryName = converter.convert(resString,
                                            new String[]
                                            { categoryName });
                                        categoryText += " " + categoryName;
                                    }
                                    else
                                    {
                                        categoryText += " " + categoryName;
                                    }
                                    
                                }else
                                {
                                    categoryText = categoryName;
                                }
                                this.model.put(KEY_I_CATEGORY_ID, categoryId);
                                this.model.put(KEY_S_COMMON_SHOW_SEARCH_TEXT,
                                    categoryText);
                            }
                        }
                    }
                }
                
                break;
            }
            default:
            {
                break;
            }
        }
        
        return super.preProcessUIEvent(tnUiEvent);
    }
    
    protected PoiCategory findPoiCategory(PoiCategory poiCategory, int id)
    {
        PoiCategory currentCategory = null;
        int size = 0;
        if(poiCategory != null)
        {
            size = poiCategory.getChildrenSize();
        }
        for(int i = 0; i < size; i++)
        {
            currentCategory = poiCategory.getChildAt(i);
            if(currentCategory.getCategoryId() == id)
            {
                break;
            }
        }
        return currentCategory;
    }

    protected BottomContainerArgs getNavigationBottomBarArgs(int moduleCmdId)
    {
        BottomContainerArgs args = new BottomContainerArgs(moduleCmdId);

        boolean isDynamicRoute = NavRunningStatusProvider.getInstance().getNavType() == NavRunningStatusProvider.NAV_TYPE_DYNAMIC_ROUTE;
        args.cmdIds = new int[5];
        if(isDynamicRoute)
        {
            args.cmdIds[0] = CMD_GOTO_NAV;
        }
        else
        {
            args.cmdIds[0] = CMD_GOTO_TURN_MAP;
        }
        args.cmdIds[1] = CMD_GOTO_ROUTE_SUMMARY;
        args.cmdIds[2] = CMD_COMMON_DSR;
        args.cmdIds[3] = CMD_STAY_IN_SEARCH;
        args.cmdIds[4] = CMD_COMMON_END_TRIP;
        args.displayStrResIds = new int[5];
        if(isDynamicRoute)
        {
            args.displayStrResIds[0] = IStringCommon.RES_NAVIGATION;
        }
        else
        {
            args.displayStrResIds[0] = IStringCommon.RES_DIRECTIONS;
        }
        args.displayStrResIds[1] = IStringCommon.RES_ROUTE;
        args.displayStrResIds[3] = IStringCommon.RES_NEARBY;
        args.displayStrResIds[4] = IStringCommon.RES_BTTN_EXIT;

        args.unfocusImageAdapters = new TnUiArgAdapter[5];
        if(isDynamicRoute)
        {
            args.unfocusImageAdapters[0] = ImageDecorator.IMG_BOTTOM_BAR_DRIVE_UNFOCUS;
        }
        else
        {
            args.unfocusImageAdapters[0] = ImageDecorator.IMG_BOTTOM_BAR_ICON_DIRECTIONS_UNFOCUSED;
        }
        args.unfocusImageAdapters[1] = ImageDecorator.IMG_TOP_BAR_LIST_UNFOCUS;
        args.unfocusImageAdapters[2] = ImageDecorator.IMG_BUTTON_MIC_ICON;
        args.unfocusImageAdapters[3] = ImageDecorator.IMG_BOTTOM_BAR_PLACES_UNFOCUS;
        args.unfocusImageAdapters[4] = ImageDecorator.IMG_BOTTOM_BAR_END_TRIP_UNFOCUS;
        
        args.disableImageAdapters = new TnUiArgAdapter[5];
        args.disableImageAdapters[3] = ImageDecorator.IMG_BOTTOM_BAR_PLACES_DISABLE;

        args.focusImageAdapters = new TnUiArgAdapter[5];
        if(isDynamicRoute)
        {
            args.focusImageAdapters[0] = ImageDecorator.IMG_BOTTOM_BAR_DRIVE_FOCUS;
        }
        else
        {
            args.focusImageAdapters[0] = ImageDecorator.IMG_BOTTOM_BAR_ICON_DIRECTIONS_FOCUSED;
        }
        args.focusImageAdapters[1] = ImageDecorator.IMG_TOP_BAR_LIST_FOCUS;
        args.focusImageAdapters[2] = ImageDecorator.IMG_BUTTON_MIC_ICON;
        args.focusImageAdapters[3] = ImageDecorator.IMG_BOTTOM_BAR_PLACES_FOCUS;
        args.focusImageAdapters[4] = ImageDecorator.IMG_BOTTOM_BAR_END_TRIP_FOCUS;
        
        args.displayStrs = new String[5];
        for(int i = 0; i < 5; i++)
        {
            String str = ResourceManager.getInstance().getCurrentBundle()
            .getString(args.displayStrResIds[i], IStringCommon.FAMILY_COMMON);
            args.displayStrs[i] = str;
        }
        return args;
    }
    
    
    protected AbstractTnContainer createBottomContainer(final AbstractTnContainer titleContainer, final BottomContainerArgs bottomBarArgs)
    {
        AbstractTnContainer container = super.createBottomContainer(titleContainer, bottomBarArgs);
        if(this.model.getInt(KEY_I_SEARCH_TYPE) == IPoiSearchProxy.TYPE_SEARCH_ALONG_ROUTE
                || this.model.getInt(KEY_I_AC_TYPE) == TYPE_AC_FROM_TURN_MAP)
        {
            container.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.BOTTOM_NAV_BAR);
            container.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, NinePatchImageDecorator.BOTTOM_NAV_BAR);
        }
        return container;
    }
    
    public static class CategoryListAdapter extends FrogKeywordFilter implements FrogAdapter, ITnUiEventListener
    {
        private PoiCategory currentCategory;
        private PoiCategory totalCatory;
        private FrogList list;
        private String filterStr;
        private PoiCategory searchNode;
        private String anyString;
        public CategoryListAdapter(FrogList list, PoiCategory defaultCategory, PoiCategory searchCategory)
        {
           this.list = list;
           this.totalCatory = defaultCategory;
           this.currentCategory = defaultCategory;
           this.searchNode = searchCategory;
           if(searchNode == null)
           {
               searchNode = defaultCategory;
           }
           anyString = ResourceManager.getInstance().getCurrentBundle().getString(IStringPoi.LABEL_POI_ANY_STRING, IStringPoi.FAMILY_POI);
           filterStr = "";
        }
        
        public void setCurrentNode(PoiCategory node)
        {
            if (currentCategory != null && (!currentCategory.equals(node)))
            {
                currentCategory = node;
                setAdapterOnUiThread(list, this);
            }
        }
        
        public PoiCategory getCurrentCategory()
        {
            return currentCategory;
        }
        
        public AbstractTnComponent getComponent(int position,
                AbstractTnComponent convertComponent, AbstractTnComponent parent)
        {
            CitizenCategoryListItem item = null;
            if(convertComponent == null)
            {
                item = UiFactory.getInstance().createCategoryListItem(position);
                item.setRightIndicator(ImageDecorator.IMG_LIST_ICON_ARROW_UNFOCUSED
                        .getImage(), ImageDecorator.IMG_LIST_ICON_ARROW_UNFOCUSED.getImage());
                item.setForegroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_LABEL), UiStyleManager.getInstance()
                        .getColor(UiStyleManager.TEXT_COLOR_LABEL));
                TnMenu menu = UiFactory.getInstance().createMenu();
                menu.add("", CMD_SELECT_CATEGORY_LIST);
                item.setMenu(menu, AbstractTnComponent.TYPE_CLICK);
            }
            else
            {
                if(convertComponent instanceof CitizenCategoryListItem)
                {
                    item = (CitizenCategoryListItem)convertComponent;
                }
            }
            if (currentCategory != null && position < currentCategory.getChildrenSize()
                    && position >= 0)
            {
                PoiCategory node = currentCategory.getChildAt(position);
                item.setText(node.getName());
                if (node.getChildrenSize() > 0)
                {
                    item.setIsShowRightIndicator(true);
                }
                else
                {
                    item.setIsShowRightIndicator(false);
                }
                item.setId(position);
            }
            if (item != null)
            {
                item.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_LIST_SINGLE));
                PoiSearchUiDecorator poiSearchUiDecorator = new PoiSearchUiDecorator();
                int leftPadding = poiSearchUiDecorator.POI_LIST_PADDING.getInt();
                item.setPadding(leftPadding, 0, leftPadding, 0);
            }
            return item;
        }

        public int getCount()
        {
            int size = 0;
            if(currentCategory != null)
            {
                size = currentCategory.getChildrenSize();
            }
            return size;
        }

        public int getItemType(int position)
        {
            return 0;
        }
      
        protected FilterResults performFilter(FilterRequest constraint)
        {
            if(constraint.keyword == null || constraint.keyword.length() == 0)
            {
                this.reset();
                return null;
            }
            currentCategory = new PoiCategory();
            this.filterStr = constraint.keyword;
            
            searchFilter(this.searchNode, this.filterStr);
            
            FilterResults results = new FilterResults();
            results.constraint = constraint.keyword;
            results.count = currentCategory.getChildrenSize();
            results.values = currentCategory;
            return results;
        }

        private void searchFilter(PoiCategory parent, String filterStr)
        {
            int size = parent.getChildrenSize();
            for(int i = 0; i < size; i++)
            {
                PoiCategory child = parent.getChildAt(i);
                if (filterStr.length() == 0 )
                {
                    continue;
                }
                else
                {
                    String nodeName = child.getName().toUpperCase();
                    
                    if (nodeName.startsWith(anyString.toUpperCase()))
                    {
                        continue;
                    }
                    else
                    {
                        if (nodeName.indexOf(filterStr.toUpperCase()) >= 0)
                        {
                            if(currentCategory.getChilds() == null || !currentCategory.getChilds().contains(child))
                            {
                                currentCategory.addChild(child);
                            }
                        }
                        if (child.getChildrenSize() > 0)
                        {
                            searchFilter(child, filterStr);
                        }

                    }
                }
            }
        }

        protected void publishResults(FilterResults results)
        {
            if(results == null)
                return;
            setAdapterOnUiThread(list, this);
        }
        public void reset()
        {
            currentCategory = totalCatory;
            
            setAdapterOnUiThread(list, this);
        }

        public void setAdapterOnUiThread(final FrogList list, final FrogAdapter adapter)
        {
            ((AbstractTnUiHelper)AbstractTnUiHelper.getInstance()).runOnUiThread(new Runnable()
            {

                public void run()
                {
                    list.setAdapter(adapter);
                }
                
            });
        }

        
		public boolean handleUiEvent(TnUiEvent tnUiEvent) {
			if(tnUiEvent.getType() == TnUiEvent.TYPE_COMMAND_EVENT
					&& tnUiEvent.getCommandEvent().getCommand() == CMD_CLEAR_TEXT)
			{
				 this.reset();
			}
			return false;
		}
    }
   
    public void onScreenUiEngineAttached(final TnScreen screen, int attached)
    {
        if (screen != null && attached == ITnScreenAttachedListener.AFTER_ATTACHED)
        {
            TeleNavDelegate.getInstance().setOrientation(TeleNavDelegate.ORIENTATION_UNSPECIFIED);
        }
    }
    
    protected void activate()
    {
        super.activate();
        ((AndroidUiHelper) AbstractTnUiHelper.getInstance()).setFontDensity(false);
    }
    
    protected void deactivateDelegate()
    {
        super.deactivateDelegate();
        ((AndroidUiHelper) AbstractTnUiHelper.getInstance()).setFontDensity(true);
    }
}


