/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * UiFactory.java
 *
 */
package com.telenav.ui;


import com.telenav.app.TeleNavDelegate;
import com.telenav.app.android.scout_us.TeleNav;
import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.dao.misc.SimpleConfigDao;
import com.telenav.data.datatypes.address.Address;
import com.telenav.data.datatypes.address.Stop;
import com.telenav.data.datatypes.preference.Preference;
import com.telenav.datatypes.DataUtil;
import com.telenav.location.TnLocation;
import com.telenav.module.AppConfigHelper;
import com.telenav.module.dashboard.ScoutMultiLineButton;
import com.telenav.module.location.LocationProvider;
import com.telenav.module.nav.NavRunningStatusProvider;
import com.telenav.mvc.ICommonConstants;
import com.telenav.res.IStringCommon;
import com.telenav.res.IStringPreference;
import com.telenav.res.ResourceManager;
import com.telenav.res.converter.StringConverter;
import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.core.AbstractTnContainer;
import com.telenav.tnui.core.AbstractTnUiHelper;
import com.telenav.tnui.core.ITnUiEventListener;
import com.telenav.tnui.core.TnMenu;
import com.telenav.tnui.core.TnMenuItem;
import com.telenav.tnui.core.TnUiArgs;
import com.telenav.tnui.core.TnUiArgs.ITnUiArgsDecorator;
import com.telenav.tnui.core.TnUiArgs.TnUiArgAdapter;
import com.telenav.tnui.graphics.AbstractTnFont;
import com.telenav.tnui.graphics.AbstractTnGraphics;
import com.telenav.tnui.graphics.AbstractTnImage;
import com.telenav.tnui.graphics.TnColor;
import com.telenav.tnui.widget.TnLinearContainer;
import com.telenav.tnui.widget.TnPopupContainer;
import com.telenav.tnui.widget.TnScrollPanel;
import com.telenav.tnui.widget.TnWebBrowserField;
import com.telenav.ui.citizen.CitizenAddressListItem;
import com.telenav.ui.citizen.CitizenButton;
import com.telenav.ui.citizen.CitizenCategoryListItem;
import com.telenav.ui.citizen.CitizenCheckBox;
import com.telenav.ui.citizen.CitizenCheckBoxPopup;
import com.telenav.ui.citizen.CitizenCheckItem;
import com.telenav.ui.citizen.CitizenCircleAnimation;
import com.telenav.ui.citizen.CitizenClockCircleAnimation;
import com.telenav.ui.citizen.CitizenDropDownComboBox;
import com.telenav.ui.citizen.CitizenGallery;
import com.telenav.ui.citizen.CitizenMessageBox;
import com.telenav.ui.citizen.CitizenPoiListItem;
import com.telenav.ui.citizen.CitizenProfileSwitcher;
import com.telenav.ui.citizen.CitizenProgressBar;
import com.telenav.ui.citizen.CitizenQuickFindButton;
import com.telenav.ui.citizen.CitizenQuickSearchBar;
import com.telenav.ui.citizen.CitizenRadioButton;
import com.telenav.ui.citizen.CitizenRadioButtonContainer;
import com.telenav.ui.citizen.CitizenRouteChoiceItem;
import com.telenav.ui.citizen.CitizenRouteSummaryItem;
import com.telenav.ui.citizen.CitizenScreen;
import com.telenav.ui.citizen.CitizenSlidableContainer;
import com.telenav.ui.citizen.CitizenTextField;
import com.telenav.ui.citizen.CitizenTrafficItem;
import com.telenav.ui.citizen.CitizenWebComponent;
import com.telenav.ui.citizen.map.MapContainer;
import com.telenav.ui.frogui.FrogScreen;
import com.telenav.ui.frogui.widget.AbstractFrogAnimation;
import com.telenav.ui.frogui.widget.FrogButton;
import com.telenav.ui.frogui.widget.FrogComboBox;
import com.telenav.ui.frogui.widget.FrogDropDownField;
import com.telenav.ui.frogui.widget.FrogFloatPopup;
import com.telenav.ui.frogui.widget.FrogImageComponent;
import com.telenav.ui.frogui.widget.FrogLabel;
import com.telenav.ui.frogui.widget.FrogList;
import com.telenav.ui.frogui.widget.FrogListItem;
import com.telenav.ui.frogui.widget.FrogMessageBox;
import com.telenav.ui.frogui.widget.FrogMultiLine;
import com.telenav.ui.frogui.widget.FrogMultiLineButton;
import com.telenav.ui.frogui.widget.FrogNullField;
import com.telenav.ui.frogui.widget.FrogProgressBox;
import com.telenav.ui.frogui.widget.FrogTextArea;
import com.telenav.ui.frogui.widget.FrogTextField;
import com.telenav.ui.frogui.widget.FrogTree;
import com.telenav.util.PrimitiveTypeCache;

/**
 * @author fqming (fqming@telenav.cn)
 * @date Aug 9, 2010
 */
public class UiFactory
{
    
    private static UiFactory instance = new UiFactory();
    
    private int charWidth = 0;
    
    private UiFactory()
    {

    }

    public static UiFactory getInstance()
    {
        return instance;
    }
    
    public int getCharWidth()
    {
        if (charWidth == 0)
        {
            AbstractTnFont f = UiStyleManager.getInstance().getFont(UiStyleManager.FONT_LABEL);
            charWidth = f.stringWidth("o");
        }
        
        return charWidth;
    }
    
    public AbstractTnFont getSuitableFont(int width, AbstractTnFont font, String text)
    {
        return getSuitableFont(width, font, text, 1);
    }
    
    public AbstractTnFont getSuitableFont(int width, AbstractTnFont font, String text, int minSize)
    {
        AbstractTnFont tmpFont = font;
        while (tmpFont.stringWidth(text) > width && tmpFont.getSize() > minSize)
        {
            tmpFont = ((AbstractTnUiHelper)AbstractTnUiHelper.getInstance()).createFont(font.getFamily(), font.getStyle(), tmpFont.getSize() - 1);
        }
        return tmpFont;
    }

    /**
     * Create Label
     * 
     * @param id componentId
     * @param text text on Label
     * @return label.
     */
    public FrogLabel createLabel(int id, String text)
    {
        FrogLabel label = new FrogLabel(id, text);

        label.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_LABEL));
        label.setForegroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_DA_GR),
            UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_DA_GR));

        return label;
    }

    /**
     * Create button
     * 
     * @param id componentId
     * @param text text on button
     * @return button.
     */
    public FrogButton createButton(int id, String text)
    {
        FrogButton button = new FrogButton(id, text);

        button.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_BUTTON));
        button.setForegroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.BUTTON_DEFAULT_FOCUSED_COLOR),
            UiStyleManager.getInstance().getColor(UiStyleManager.BUTTON_DEFAULT_UNFOCUSED_COLOR));
        
        if(text != null && text.trim().length() > 0)
        {
            button.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
            {
                public Object decorate(TnUiArgAdapter args)
                {
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getMaxDisplaySize() * 65 / 1000);
                }
            }));
            
            button.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
            {
                public Object decorate(TnUiArgAdapter args)
                {
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth() * 33 / 100);
                }
            }));
        }

        button.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, NinePatchImageDecorator.BOTTOM_BUTTON_UNFOCUSED);
        button.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.BOTTOM_BUTTON_FOCUSED);

        return button;
    }

    /**
     * Create multi-line button
     * 
     * @param id componentId
     * @param text text on button
     * @return button.
     */
    public FrogMultiLineButton createMultiLineButton(int id, String text)
    {
        FrogMultiLineButton button = new FrogMultiLineButton(id, text);

        button.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_BUTTON));
        button.setForegroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.BUTTON_DEFAULT_FOCUSED_COLOR),
            UiStyleManager.getInstance().getColor(UiStyleManager.BUTTON_DEFAULT_UNFOCUSED_COLOR));
        
        if(text != null && text.trim().length() > 0)
        {
            button.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
            {
                public Object decorate(TnUiArgAdapter args)
                {
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getMaxDisplaySize() * 65 / 1000);
                }
            }));
            
            button.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
            {
                public Object decorate(TnUiArgAdapter args)
                {
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth() * 33 / 100);
                }
            }));
        }

        button.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, NinePatchImageDecorator.BIG_RADIAN_BUTTON_UNFOCUS);
        button.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.BIG_RADIAN_BUTTON_FOCUS);

        return button;
    }
    
    /**
     * Create citizen radio button
     * 
     * @param id componentId
     * @param text text on button
     * @return button.
     */
    public CitizenRadioButton createCitizenRadioButton(int id, String text)
    {
        CitizenRadioButton button = new CitizenRadioButton(id, text);

        button.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_BUTTON));
        button.setForegroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_TAB_BUTTON_FOCUSED),
            UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_TAB_BUTTON_UNFOCUSED));
        
        if(text != null && text.trim().length() > 0)
        {
            button.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
            {
                public Object decorate(TnUiArgAdapter args)
                {
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getMaxDisplaySize() * 65 / 1000);
                }
            }));
            
            button.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
            {
                public Object decorate(TnUiArgAdapter args)
                {
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth() * 33 / 100);
                }
            }));
        }

       

        return button;
    }
    
    public CitizenRadioButtonContainer createCitizenRadioButtonContainer(int id, boolean isVertical, int anchor)
    {
        CitizenRadioButtonContainer linearContainer = new CitizenRadioButtonContainer(id, isVertical, anchor);

        return linearContainer;
    }
    
    /**
     * Create FrogNullField
     * 
     * @param id componentId
     * @return nullField.
     */
    public FrogNullField createNullField(int id)
    {
        FrogNullField nullField = new FrogNullField(id);

        return nullField;
    }

    
    /**
     * the screen's id will be replaced by the status id of mvc
     * 
     * @return a screen object
     */
    public CitizenScreen createScreen(int screenId)
    {
        CitizenScreen citizenScreen = new CitizenScreen(screenId);
        TnMenu screenMenu = UiFactory.getInstance().createMenu();
            
        if (!NavRunningStatusProvider.getInstance().isNavRunning() && DaoManager.getInstance().getBillingAccountDao().isLogin())
        {
            String preferenceStr = ResourceManager.getInstance().getCurrentBundle()
                    .getString(IStringPreference.PREFERENCE_STR_TITLE, IStringPreference.FAMILY_PREFERENCE);
            screenMenu.add(preferenceStr, ICommonConstants.CMD_COMMON_PREFERENCE);
        }
        
        String exitStr = ResourceManager.getInstance().getCurrentBundle().getString(
            NavRunningStatusProvider.getInstance().isNavRunning()? IStringCommon.RES_END_TRIP : IStringCommon.RES_BTTN_EXIT,
            IStringCommon.FAMILY_COMMON);        
        screenMenu.add(exitStr, ICommonConstants.CMD_COMMON_EXIT); 

        //disable it for RC build.
        if(TeleNav.isDebugVerison)
        {
            String autoFeedbackStr = "Report an issue";        
            screenMenu.add(autoFeedbackStr, ICommonConstants.CMD_COMMON_REPORT_AN_ISSUE); 
        }
        if (((DaoManager) DaoManager.getInstance()).getSimpleConfigDao().getBoolean(
            SimpleConfigDao.KEY_SWITCH_AIRPLANE_MODE_ENABLE))
        {
            screenMenu.add(TeleNavDelegate.IS_START_CRAZY_SWITCH ? TeleNavDelegate.SWITCH_AIRPLANE_OFF : TeleNavDelegate.SWITCH_AIRPLANE_ON, 
                    ICommonConstants.CMD_COMMON_SWITCH_AIRPLANE);
        }
        //DB, comment is as PM's comment for TN-481.
//        int audioPath = TnMediaManager.getInstance().getAudioPath();
//        String btStr = null;
//        if(audioPath == TnMediaManager.PATH_BLUETOOTH_HEADSET)
//        {
//            btStr = ResourceManager.getInstance().getCurrentBundle().getString(IStringCommon.RES_BT_OFF,
//                IStringCommon.FAMILY_COMMON);
//        }
//        else
//        {
//            btStr = ResourceManager.getInstance().getCurrentBundle().getString(IStringCommon.RES_BT_ON,
//                IStringCommon.FAMILY_COMMON);
//        }
//        screenMenu.add(btStr, ICommonConstants.CMD_COMMON_SWITCH_AUDIO);
        
        citizenScreen.getRootContainer().setMenu(screenMenu, AbstractTnComponent.TYPE_MENU);

        citizenScreen.getRootContainer().setBackgroundColor(
            UiStyleManager.getInstance().getColor(UiStyleManager.BG_COLOR_WH));
        
        MapContainer mapContainer = UiFactory.getInstance().getCleanMapContainer(null, ICommonConstants.ID_MAP_CONTAINER, false);
        citizenScreen.setBackgroundComponent(mapContainer);
        
        AbstractTnContainer titleContainer = citizenScreen.getTitleContainer();
        titleContainer.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.FTUE_BG_UNFOCUSED);
        titleContainer.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, NinePatchImageDecorator.FTUE_BG_UNFOCUSED);
        return citizenScreen;
    }

    /**
     * Create LinearContainer
     * 
     * @param id container Id
     * @param isVertical isVertical
     * @return LinearContainer.
     */
    public TnLinearContainer createLinearContainer(int id, boolean isVertical)
    {
        TnLinearContainer linearContainer = new TnLinearContainer(id, isVertical);

        return linearContainer;
    }

    /**
     * Create LinearContainer
     * 
     * @param id id
     * @param isVertical isVertical
     * @param anchor anchor
     * @return TnLinearContainer
     */
    public TnLinearContainer createLinearContainer(int id, boolean isVertical, int anchor)
    {
        TnLinearContainer linearContainer = new TnLinearContainer(id, isVertical, anchor);

        return linearContainer;
    }

    /**
     * Create scrollPane
     * 
     * @param id component Id
     * @param isVertical set isVertical.
     * @return TnScrollPanel
     */
    public TnScrollPanel createScrollPanel(int id, boolean isVertical)
    {
        TnScrollPanel scrollPanel = new TnScrollPanel(id, isVertical);

        return scrollPanel;
    }
    
    /**
     * Create timeout message Box.
     * 
     * @param id id
     * @param message message to display.
     * @param timeout timeout(unit is second)
     * @return
     */
    public FrogMessageBox createTimeoutMessageBox(int id, String message, int timeout, int timeoutCommand) 
	{
        if(timeout <= 0)
            throw new IllegalArgumentException("Timeout should > 0");
        
        FrogMessageBox msgBox = new FrogMessageBox(id, message);
        msgBox.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT,
            new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
            {
                public Object decorate(TnUiArgAdapter args)
                {
                    if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                        return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 35 / 100);
                    else
                        return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 45 / 100);
                }
            }));
        msgBox.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, NinePatchImageDecorator.MESSAGE_BOX_BG);
        msgBox.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.MESSAGE_BOX_BG);
        msgBox.setTimeout(timeout, timeoutCommand);
        msgBox.setPadding(20, 10, 20, 10);
        msgBox.setMessageFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_LABEL));
        return msgBox;
    }
    
    /**
     * Create timeout message with default 3s.
     * 
     * @param id id
     * @param message message to display.
     * @return
     */
    public FrogMessageBox createTimeoutMessageBox(int id, String message, int timeoutCommand)
    {
        return createTimeoutMessageBox(id, message, 3, timeoutCommand);
    }

    /**
     * Create MultiLine
     * 
     * @param id component id.
     * @param text displayed text
     * @return FrogMultiLine
     */
    public FrogMultiLine createMultiLine(int id, String text)
    {
        FrogMultiLine multiLine = new FrogMultiLine(id, text);

        return multiLine;
    }

    /**
     * create TnMenu
     * 
     * @return menu
     */
    public TnMenu createMenu()
    {
        TnMenu menu = new TnMenu();
        return menu;
    }

    /**
     * create popup container.
     * 
     * @param id component id
     * @return PopupContainer.
     */
    public TnPopupContainer createPopupContainer(int id)
    {
        TnPopupContainer popupContainer = new TnPopupContainer(id);

        return popupContainer;
    }

    /**
     * Create combo box
     * 
     * @param id id of component.
     * @param text combo box text
     * @return
     */
    public FrogComboBox createComboBox(int id, String text)
    {
        FrogComboBox comboBox = new FrogComboBox(id, text);
        TnUiArgs args = comboBox.getTnUiArgs();
        args.put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, NinePatchImageDecorator.LIST_ITEM_UNFOCUS);
        args.put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.LIST_ITEM_FOCUSED);
        
        return comboBox;
    }

    /**
     * Create text field component.
     * 
     * @param initText initial text of text field.
     * @param maxSize maximum size of text field.
     * @param keypadConstraints keypad constraint of text field.
     * @param keyboardContraints keyboard constraint of text field.
     * @param hint hint of text field.
     * @param id id of text field.
     * @return
     */
    public FrogTextField createTextField(String initText, int maxSize, int keypadConstraints, String hint, int id)
    {
        FrogTextField textField = new FrogTextField(initText, maxSize, keypadConstraints, hint, id);
        
        textField.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_TEXT_FIELD));

        return textField;
    }

    /**
     * Create list container
     * 
     * @param id container id.
     * @return
     */
    public FrogList createList(int id)
    {
        FrogList list = new FrogList(id);

        return list;
    }

    /**
     * Create list item component
     * 
     * @param id list item component id.
     * @return
     */
    public FrogListItem createListItem(int id)
    {
        FrogListItem listItem = new FrogListItem(id);
        TnUiArgs args = listItem.getTnUiArgs();
        
        args.put(TnUiArgs.KEY_PREFER_HEIGHT, new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
        {
            public Object decorate(TnUiArgAdapter args)
            {
                return PrimitiveTypeCache.valueOf(AppConfigHelper.getMaxDisplaySize() * 105 / 1000);
            }
        }));
        args.put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, NinePatchImageDecorator.LIST_ITEM_UNFOCUS);
        args.put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.LIST_ITEM_FOCUSED);
        return listItem;
    }

    /**
     * Create scrollPanel component
     * 
     * @param id
     * @param isVertical
     * @return
     */
    public TnScrollPanel createTnScrollPanel(int id, boolean isVertical)
    {
        TnScrollPanel panel = new TnScrollPanel(id, isVertical);

        return panel;
    }

    /**
     * create web browser.
     * 
     * @param id
     * @return
     */
    public TnWebBrowserField createWebBrowserField(int id)
    {
        TnWebBrowserField browserField = new TnWebBrowserField(id);
        
        return browserField;
    }
    
    /**
     * Create DropDownField component
     * 
     * @param initText
     * @param maxSize
     * @param keypadConstraints
     * @param keyboardContraints
     * @param hint
     * @param id
     * @return
     */
    public FrogDropDownField createDropDownField(String initText, int maxSize, int keypadConstraints, String hint,
            int id)
    {
        FrogDropDownField dropDownField = new FrogDropDownField(initText, maxSize, keypadConstraints,  hint, id);
        
        dropDownField.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_TEXT_FIELD));

        return dropDownField;
    }

    /**
     * Create AddressListItem component
     * 
     * @param id
     * @param lazyBind
     * @return
     */
    public CitizenAddressListItem createCitizenAddressListItem(int id)
    {
        CitizenAddressListItem addressListItem = new CitizenAddressListItem(id);

        TnUiArgs args = addressListItem.getTnUiArgs();
        
        args.put(TnUiArgs.KEY_PREFER_HEIGHT, new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
        {
            public Object decorate(TnUiArgAdapter args)
            {
                return PrimitiveTypeCache.valueOf(AppConfigHelper.getMaxDisplaySize() * 105 / 1000);
            }
        }));
        args.put(TnUiArgs.KEY_PREFER_WIDTH, new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
        {
            public Object decorate(TnUiArgAdapter args)
            {
                return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth());
            }
        }));
        
        args.put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, NinePatchImageDecorator.LIST_ITEM_UNFOCUS);
        args.put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.LIST_ITEM_FOCUSED);
        return addressListItem;
    }

    /**
     * create Map Component
     * 
     * @param id id
     * @param navMapProxy proxy
     * @return MapComponent
     */
//    public IMapView createCitizenMapComponent(int id, int version)
//    {
//        IMapView mapComponent = new GlMapComponent(id, version); 
//        return mapComponent;
//    }

    public MapContainer getCleanMapContainer(FrogScreen screen, int id)
    {
        return getCleanMapContainer(screen, id, true);
    }
    
    public MapContainer getCleanMapContainer(FrogScreen screen, int id, boolean needResetZoomLevel)
    {
        if (screen != null)
        {
            screen.getRootContainer().setBackgroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.BG_COLOR_TRANSPARENT));
        }
        MapContainer container = MapContainer.getInstance();

        TnUiArgAdapter mapX = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
        {
            public Object decorate(TnUiArgAdapter args)
            {
                return PrimitiveTypeCache.valueOf(0);
            }
        });

        TnUiArgAdapter mapY = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
        {
            public Object decorate(TnUiArgAdapter args)
            {
                return PrimitiveTypeCache.valueOf(0);
            }
        });
        
        
        TnUiArgAdapter mapWidth = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
        {
            public Object decorate(TnUiArgAdapter args)
            {
                return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth());
            }
        });

        TnUiArgAdapter mapHeight = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
        {
            public Object decorate(TnUiArgAdapter args)
            {
                return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() - AppConfigHelper.getStatusBarHeight());
            }
        });

   
        container.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH,mapWidth);
        container.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT,mapHeight); 
        container.cleanAll(needResetZoomLevel);
        container.setMapRect(mapX, mapY, mapWidth,mapHeight);
        return container;
    }
    
    /**
     * create float popup
     * 
     * @param id id
     * @return floatPopup
     */
    public FrogFloatPopup createFloatPopup(int id)
    {
        FrogFloatPopup floatPopup = new FrogFloatPopup(id);

        return floatPopup;
    }

    /**
     * create CitizenTextField
     * 
     * @param initText
     * @param maxSize
     * @param keypadConstraints
     * @param hint
     * @param id
     * @param iconBackSpace
     * @return
     */
    public CitizenTextField createCitizenTextField(String initText, int maxSize, int keypadConstraints,
            String hint, int id, AbstractTnImage iconFind, AbstractTnImage iconBackSpace)
    {
        CitizenTextField textField = new CitizenTextField(initText, maxSize, keypadConstraints,hint, id,
            iconFind, iconBackSpace);
        TnUiArgs args = textField.getTnUiArgs();
        args.put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, NinePatchImageDecorator.SEARCH_BOX_INPUT_BG_UNFOCUS);
        args.put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.SEARCH_BOX_INPUT_BG_UNFOCUS);
        textField.setBackgroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_WH));
        textField.setHintTextColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_LI_GR));
        textField.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_TEXT_FIELD));
        textField.setTextColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_TEXTFIELD));
        textField.setPadding(8, 8, 8, 8);
        return textField;
    }

    public CitizenTextField createCitizenTextField(String initText, int maxSize, int keypadConstraints,
            String hint, int id, AbstractTnImage iconBackSpace)
    {
        return createCitizenTextField(initText, maxSize, keypadConstraints, hint, id, null, iconBackSpace);
    }
    
    public CitizenCheckItem createCitizenCheckItem(int id, String text, AbstractTnImage selectedIconOn, AbstractTnImage selectedIconOff)
    {
        CitizenCheckItem checkBox = new CitizenCheckItem(id, text, selectedIconOn, selectedIconOff);
        checkBox.setGap(new int[]{5,0});
        TnUiArgs args = checkBox.getTnUiArgs();
        int padding = AppConfigHelper.getMinDisplaySize() * 3125 / 100000;
        checkBox.setPadding(padding, 0, padding, 0);
        args.put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, NinePatchImageDecorator.DROPDOWN_NORMAL_ITEM_UNFOCUS);
        args.put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.DROPDOWN_NORMAL_ITEM_UNFOCUS);
        return checkBox;
    }

    public CitizenCheckBox createCitizenCheckBox(int id, boolean isMultiSelect)
    {
        CitizenCheckBox checkbox = new CitizenCheckBox(id, isMultiSelect);
        return checkbox;
    }
    
    public CitizenCheckBoxPopup createCitizenCheckBoxPopup(int id, String message, TnMenu commands)
    {
        CitizenCheckBoxPopup  citizenRadioBoxPopup = new CitizenCheckBoxPopup(id, message);
        initCommandButton(citizenRadioBoxPopup, commands);
        citizenRadioBoxPopup.getTnUiArgs().put(CitizenMessageBox.KEY_MESSAGE_BOX_TOP_HEIGHT,
            new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
            {
                public Object decorate(TnUiArgAdapter args)
                {
                    if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                        return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 15 / 100);
                    else
                        return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 25 / 100);
                }
            }));
        citizenRadioBoxPopup.getTnUiArgs().put(CitizenCheckBoxPopup.KEY_CHECK_BOX_MAX_HEIGHT,
            new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
            {
                public Object decorate(TnUiArgAdapter args)
                {
                    int itemHeight = AppConfigHelper.getMaxDisplaySize() * 830 / 10000;//Represent the height of each CitizenCheckItem.
                    
                    if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                    {
                        return PrimitiveTypeCache.valueOf(itemHeight * 4);
                    }
                    else
                    {
                        return PrimitiveTypeCache.valueOf(itemHeight * 3);
                    }
                }
            }));
        return citizenRadioBoxPopup;
    }
    
    public FrogMessageBox createMessageBox(int id, String message, TnMenu commands)
    {
        return createMessageBox(id,  message,  commands,  true);
    }
    
    public FrogMessageBox createMessageBox(int id, String message, TnMenu commands, boolean needSetHeight)
    {
        if(commands == null || commands.size() <= 0)
        {
            throw new IllegalArgumentException("Should have 1 cmd at least");
        }
        
        CitizenMessageBox messageBox = new CitizenMessageBox(id, message, commands.size() > 2);
        messageBox.setContentFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_LABEL));
        initCommandButton(messageBox, commands, needSetHeight);
        return messageBox;
    }
    
    public FrogMessageBox createMessageBox(int id, String message, String title, TnMenu commands, boolean needSetHeight)
    {
        FrogMessageBox messageBox = createMessageBox(id, message, commands, needSetHeight);
        
        if(title != null && title.length() > 0)
        {
            FrogLabel titleLabel = UiFactory.getInstance().createLabel(0, title);
            titleLabel.setForegroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_DA_GR), UiStyleManager.getInstance()
                .getColor(UiStyleManager.TEXT_COLOR_DA_GR));
            titleLabel.setBoldFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_POPUP_TITLE));
            titleLabel.setStyle(AbstractTnGraphics.HCENTER);
            AbstractTnContainer content = messageBox.getContent();
            TnUiArgAdapter widthAdapter = null;
            if(content != null)
            {
                widthAdapter = content.getTnUiArgs().get(TnUiArgs.KEY_PREFER_WIDTH);
            }
            if(widthAdapter != null)
            {
                titleLabel.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, widthAdapter);
            }
            
            messageBox.setTitle(titleLabel);
        }
        
        return messageBox;
    }
    
    private void initCommandButton(final CitizenMessageBox messageBox, TnMenu commands)
    {
        initCommandButton(messageBox, commands, true);
    }
    
    private void initCommandButton(final CitizenMessageBox messageBox, TnMenu commands, boolean needSetHeight)
    {
        messageBox.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH,
            new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
            {
                public Object decorate(TnUiArgAdapter args)
                {
                    int min = AppConfigHelper.getMinDisplaySize() * 95 / 100;
                    return PrimitiveTypeCache.valueOf( min );
                }
            }));
        if(needSetHeight)
        {
            messageBox.getTnUiArgs().put(CitizenMessageBox.KEY_MESSAGE_BOX_TOP_HEIGHT,
                new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
                {
                    public Object decorate(TnUiArgAdapter args)
                    {
                        if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                            return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 25 / 100);
                        else
                            return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 37 / 100);
                    }
                }));
        }
        final int size = commands.size();
        
        for(int i = 0 ; i < size ; i ++)
        {
            TnMenuItem menuItem = commands.getItem(i);
            FrogButton button = UiFactory.getInstance().createButton(0, menuItem.getTitle());
            button.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, NinePatchImageDecorator.BOTTOM_BUTTON_UNFOCUSED);
            button.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.BOTTOM_BUTTON_FOCUSED);
            button.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
            {
                public Object decorate(TnUiArgAdapter args)
                {
                    if (size == 2)
                    {
                        if(messageBox.getTnUiArgs().get(CitizenMessageBox.KEY_MESSAGE_BOX_BUTTON_WIDTH) != null)
                        {
                            return messageBox.getTnUiArgs().get(CitizenMessageBox.KEY_MESSAGE_BOX_BUTTON_WIDTH).getInt();
                        }
                        else
                        {
                            return PrimitiveTypeCache.valueOf(AppConfigHelper.getMinDisplaySize() * 33 / 100);
                        }
                        
                    }
                    else
                    {
                        return PrimitiveTypeCache.valueOf(AppConfigHelper.getMaxDisplaySize() * 33 / 100);
                    }
                }
            }));
            
            TnMenu btnMenu = UiFactory.getInstance().createMenu();
            btnMenu.add("", menuItem.getId());
            button.setMenu(btnMenu, AbstractTnComponent.TYPE_CLICK);
            messageBox.addButton(button);
        }        
    }
    
    public CitizenRouteChoiceItem createCitizenRouteChoiceItem(int id, String routeLength, String costTime)
    {
        CitizenRouteChoiceItem citizenRouteChoiceItem = new CitizenRouteChoiceItem(id, routeLength, costTime);
        return citizenRouteChoiceItem;
    }
    
    public CitizenPoiListItem createCitizenPoiListItem(int id)
    {
        CitizenPoiListItem poiListItem = new CitizenPoiListItem(id);
        TnUiArgs args = poiListItem.getTnUiArgs();
        
        args.put(TnUiArgs.KEY_PREFER_HEIGHT, new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
        {
            public Object decorate(TnUiArgAdapter args)
            {
                return PrimitiveTypeCache.valueOf(AppConfigHelper.getMaxDisplaySize() * 118 / 1000);
            }
        }));
        args.put(TnUiArgs.KEY_PREFER_WIDTH, new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
        {
            public Object decorate(TnUiArgAdapter args)
            {
                return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth());
            }
        }));
        args.put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, NinePatchImageDecorator.LIST_ITEM_UNFOCUS);
        args.put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.LIST_ITEM_FOCUSED);
        return poiListItem;
    }
    
    public CitizenTrafficItem createCitizenTrafficItem(int id, String streetLength, String streetName, String speed, int speedColor, boolean isNA, TnUiArgAdapter iconSize, TnUiArgAdapter firstPartWidth, TnUiArgAdapter firstPartLeftPadding, TnUiArgAdapter secondPartPadding, FrogButton triangleButton)
    {
        CitizenTrafficItem trafficItem = new CitizenTrafficItem(id, streetLength, streetName, speed, speedColor, isNA, iconSize, firstPartWidth, firstPartLeftPadding, secondPartPadding, triangleButton);
        TnUiArgs args = trafficItem.getTnUiArgs();
        args.put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, NinePatchImageDecorator.LIST_ITEM_UNFOCUS);
        args.put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.LIST_ITEM_FOCUSED);
        return trafficItem;
    }

    public CitizenRouteSummaryItem createCitizenRouteItem(int id, int turnType, String streetName, String dist, TnUiArgAdapter turnAreaWidthAdapter,
            TnUiArgAdapter turnIconSizeAdapter, TnUiArgAdapter distAreaWidthAdapter, TnUiArgAdapter streetPaddingAdapter)
    {
        CitizenRouteSummaryItem routeSummaryItem = new CitizenRouteSummaryItem(id, turnType, streetName, dist, turnAreaWidthAdapter,
                turnIconSizeAdapter, distAreaWidthAdapter, streetPaddingAdapter);
        TnUiArgs args = routeSummaryItem.getTnUiArgs();
        args.put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, NinePatchImageDecorator.LIST_ITEM_UNFOCUS);
        args.put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.LIST_ITEM_FOCUSED);
        return routeSummaryItem;
    }
    
    public CitizenDropDownComboBox createCitizenDropDownComboBox(int id)
    {
        CitizenDropDownComboBox dropDownComboBox = new CitizenDropDownComboBox(id);

        dropDownComboBox.setDropDownListItemBgImage(NinePatchImageDecorator.LIST_ITEM_FOCUSED, NinePatchImageDecorator.LIST_ITEM_UNFOCUS);
        dropDownComboBox.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, NinePatchImageDecorator.SORT_BY_BUTTON_ICON_UNFOCUS);
        dropDownComboBox.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.SORT_BY_BUTTON_ICON_FOCUS);

        return dropDownComboBox;
    }
    
    public FrogImageComponent createFrogImageComponent(int id, AbstractTnImage iconImage)
    {
        FrogImageComponent imageComponent = new FrogImageComponent(id, iconImage);
        return imageComponent;
    }
    
    public FrogImageComponent createFrogImageComponent(int id, AbstractTnImage iconImage, int anchor)
    {
        FrogImageComponent imageComponent = new FrogImageComponent(id, iconImage, anchor);
        return imageComponent;
    }
    
    public CitizenButton createCitizenButton(int id, String text, AbstractTnImage buttonIconFocus, AbstractTnImage buttonIconBlur)
    {
        CitizenButton button = new CitizenButton(id, text, buttonIconFocus,buttonIconBlur);
        button.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_BUTTON));
        button.setForegroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.BUTTON_DEFAULT_FOCUSED_COLOR),
            UiStyleManager.getInstance().getColor(UiStyleManager.BUTTON_DEFAULT_UNFOCUSED_COLOR));

        TnUiArgs args = button.getTnUiArgs();
        args.put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, NinePatchImageDecorator.BIG_RADIAN_BUTTON_UNFOCUS);
        args.put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.BIG_RADIAN_BUTTON_FOCUS);
        return button;
    }
    
    public FrogProgressBox createProgressBox(int id, String msg, AbstractFrogAnimation animationComponent)
    {
        FrogProgressBox progressBox = new FrogProgressBox(id, animationComponent);
        
        if(msg != null && msg.length() > 0)
        {
            progressBox.getTnUiArgs().put(FrogProgressBox.KEY_MULTILINE_WIDTH, new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
            {
                public Object decorate(TnUiArgAdapter args)
                {
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth());
                }
            }));
           
            progressBox.setMessage(msg, UiStyleManager.getInstance().getFont(UiStyleManager.FONT_LABEL),
                UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_WH),
                AbstractTnGraphics.HCENTER);
            
            
        }
        
        return progressBox;
    }
    
    public FrogProgressBox createProgressBox(int id, String msg)
    {
        AbstractFrogAnimation defaultAnimation = createDefaultProgressAnimation();
        
        return createProgressBox(id, msg, defaultAnimation);
    }
    
    public CitizenProgressBar createProgressBar(int id)
    {
        CitizenProgressBar progressBar = new CitizenProgressBar(id);
        
        progressBar.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_LABEL));
        progressBar.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.PROGRESS_BAR_BG_FOCUS);
        progressBar.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, NinePatchImageDecorator.PROGRESS_BAR_BG_UNFOCUS);
        progressBar.getTnUiArgs().put(CitizenProgressBar.KEY_PROGRESSBAR_IMAGE_FOCUS_END, NinePatchImageDecorator.PROGRESS_BAR_BG_FOCUS_END);
        return progressBar;
    }
    
    public CitizenQuickSearchBar createQuickSearchBar(int id, int buttonId,
            int keypadConstraints, String hint, AbstractTnImage iconSearch,
            AbstractTnImage iconBackSpace, ITnUiEventListener listener)
    {
        return createQuickSearchBar(id, buttonId, keypadConstraints, hint, iconSearch, iconBackSpace, listener, true);
    }
    
    public CitizenQuickSearchBar createQuickSearchBar(int id, int buttonId,
            int keypadConstraints, String hint, AbstractTnImage iconSearch,
            AbstractTnImage iconBackSpace, ITnUiEventListener listener, boolean needFrostedBorder)
    {
        CitizenQuickSearchBar quickSearchBar = new CitizenQuickSearchBar(
                id, keypadConstraints, hint, iconSearch, iconBackSpace, listener);
        quickSearchBar.setCursorVisible(false);
        if (needFrostedBorder)
        {
            quickSearchBar.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS,
                NinePatchImageDecorator.SEARCH_BOX_BG_UNFOCUS);
            quickSearchBar.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS,
                NinePatchImageDecorator.SEARCH_BOX_BG_UNFOCUS);
        }
        quickSearchBar.setTextFieldCommandId(ICommonConstants.CMD_COMMON_LINK_TO_SEARCH,
            ICommonConstants.CMD_COMMON_BACK);
        quickSearchBar.setDropDownNormalItemBG(
            NinePatchImageDecorator.DROPDOWN_NORMAL_ITEM_UNFOCUS,
            NinePatchImageDecorator.DROPDOWN_NORMAL_ITEM_UNFOCUS);
        quickSearchBar.getTnUiArgs().put(CitizenQuickSearchBar.KEY_TEXT_FIELD_BG_UNFOCUS,
            NinePatchImageDecorator.SEARCH_BOX_INPUT_BG_UNFOCUS);
        quickSearchBar.getTnUiArgs().put(CitizenQuickSearchBar.KEY_TEXT_FIELD_BG_FOCUS,
            NinePatchImageDecorator.SEARCH_BOX_INPUT_BG_FOCUSED);
        quickSearchBar.setDropDownBottomItemBG(
            NinePatchImageDecorator.DROPDOWN_BOTTOM_ITEM_UNFOCUS,
            NinePatchImageDecorator.DROPDOWN_BOTTOM_ITEM_UNFOCUS);
        quickSearchBar.setTextFieldFont(UiStyleManager.getInstance().getFont(
            UiStyleManager.FONT_TEXT_FIELD));
        quickSearchBar.setTextFieldBackgroundColor(UiStyleManager.getInstance().getColor(
            UiStyleManager.TEXT_COLOR_WH));
        quickSearchBar.setHintTextColor(UiStyleManager.getInstance().getColor(
            UiStyleManager.TEXT_COLOR_LI_GR));
        quickSearchBar.setTextColor(UiStyleManager.getInstance().getColor(
            UiStyleManager.TEXT_COLOR_TEXTFIELD));
        quickSearchBar.setListForegroundColor(
            UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_DA_GR),
            UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_DA_GR));
        quickSearchBar.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT,
            new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
            {
                public Object decorate(TnUiArgAdapter args)
                {
                    int maxLength = AppConfigHelper.getMaxDisplaySize();
                    if (AppConfigHelper.isTabletSize())
                    {
                        return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 1000 / 10000 + getCharWidth());
                    }
                    else
                    {
                        return PrimitiveTypeCache.valueOf(maxLength * 677 / 10000 + getCharWidth());
                    }
                }
            }));
        quickSearchBar.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH,
            new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
            {
                public Object decorate(TnUiArgAdapter args)
                {
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth() - 2 * getCharWidth());
                }
            }));
        quickSearchBar.getTnUiArgs().put(CitizenQuickSearchBar.KEY_TEXT_FIELD_WIDTH,
            new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
            {
                public Object decorate(TnUiArgAdapter args)
                {
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayWidth() - 4 * getCharWidth());
                }
            }));
        quickSearchBar.getTnUiArgs().put(CitizenQuickSearchBar.KEY_TEXT_FIELD_HEIGHT,
            new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
            {
                public Object decorate(TnUiArgAdapter args)
                {
                    int maxLength = AppConfigHelper.getMaxDisplaySize();
                    if (AppConfigHelper.isTabletSize())
                    {
                        return PrimitiveTypeCache.valueOf(AppConfigHelper.getDisplayHeight() * 1000 / 10000);
                    }
                    else
                    {
                        return PrimitiveTypeCache.valueOf(maxLength * 660 / 10000);
                    }
                }
            }));
        
        TnUiArgs posArgs = new TnUiArgs();
        posArgs.put(FrogDropDownField.KEY_DROPDOWNLIST_POS_Y, new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
        {
            public Object decorate(TnUiArgAdapter args)
            {
                return PrimitiveTypeCache.valueOf(4);
            }
        }));
        quickSearchBar.setDropDownPosition(posArgs);
        quickSearchBar.setTextFieldFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_TEXT_FIELD));
        quickSearchBar.setListSelectedListener(listener);
        quickSearchBar.setTextChangeCommandId(ICommonConstants.CMD_COMMON_GOTO_ONEBOX);
        quickSearchBar.setTextFieldFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_TEXT_FIELD));
        quickSearchBar.setTextFieldBackgroundColor(TnColor.TRANSPARENT);
        
        return quickSearchBar;
    }
    
    public FrogTree createFrogTree(int id)
    {
        FrogTree frogTree = new FrogTree(id);
        
        return frogTree;
    }
    
    public CitizenQuickFindButton createQuickFindButton(int id, String text)
    {
        CitizenQuickFindButton button = new CitizenQuickFindButton(id, text);
        return button;
    }
    
    public FrogTextArea createTextArea(int id, String initText, String hint, int linesSize)
    {
        FrogTextArea textArea = new FrogTextArea(id, initText, hint, linesSize);
        TnUiArgs args = textArea.getTnUiArgs();
        args.put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, NinePatchImageDecorator.SEARCH_BOX_INPUT_BG_UNFOCUS);
        args.put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.SEARCH_BOX_INPUT_BG_UNFOCUS);
        textArea.setBackgroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_WH));
        return textArea;
    }
    
    public CitizenGallery createCitizenGalleryContainer(int id, int selectedIndex)
    {
        CitizenGallery gallery = new CitizenGallery(id, selectedIndex);
        return gallery;
    }
    
    public CitizenCircleAnimation createCircleAnimation(int id, boolean isMiniMode)
    {
        CitizenCircleAnimation circleAnimation = null;
        if (AppConfigHelper.BRAND_SCOUT_US.equals(AppConfigHelper.brandName) || AppConfigHelper.BRAND_SCOUT_EU.equals(AppConfigHelper.brandName))
        {
            circleAnimation = new CitizenClockCircleAnimation(id, isMiniMode);
        }
        else
        {
            circleAnimation = new CitizenCircleAnimation(id, isMiniMode);
        }
        return circleAnimation;
    }
    
    public AbstractFrogAnimation createDefaultProgressAnimation()
    {
        final int min = AppConfigHelper.getMinDisplaySize();
        CitizenCircleAnimation circleAnimation = UiFactory.getInstance().createCircleAnimation(0, false);
        circleAnimation.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
        {
            public Object decorate(TnUiArgAdapter args)
            {
                int pw = PrimitiveTypeCache.valueOf(min * 50 / 100) / 2;
                return pw;
            }
        }));

        circleAnimation.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
        {
            public Object decorate(TnUiArgAdapter args)
            {
                int ph = PrimitiveTypeCache.valueOf(min * 60 / 100) / 2;
                return ph;
            }
        }));
        
        int minAnimationValue = Math.min(circleAnimation.getPreferredWidth(), circleAnimation.getPreferredHeight());

        int[] dropSizes = new int[4];
        int maxDropSize = minAnimationValue * 3 / 40;
        int dropDifference = minAnimationValue > 190 ? 2 : 1;
        dropSizes[0] = maxDropSize;
        dropSizes[1] = maxDropSize - dropDifference > 0 ? maxDropSize - dropDifference : 1;
        dropSizes[2] = maxDropSize - 2 * dropDifference > 0 ? maxDropSize - 2 * dropDifference : 1;
        dropSizes[3] = maxDropSize - 3 * dropDifference > 0 ? maxDropSize - 3 * dropDifference : 1;
        
        //float stretchRatio = ISpecialImageRes.denominator == 0?1:(float)ISpecialImageRes.numerator / ISpecialImageRes.denominator;
      /*  if(minAnimationValue <= 80)
        {
            dropSizes[0] = 6(int)Math.floor(6 * stretchRatio);;
            dropSizes[1] = 5(int)Math.floor(5 * stretchRatio);;
            dropSizes[2] = 4(int)Math.floor(4 * stretchRatio);;
            dropSizes[3] = 3(int)Math.floor(3 * stretchRatio);;
        }
        else if(minAnimationValue > 190)//for tablet
        {
            dropSizes[0] = 14;
            dropSizes[1] = 12;
            dropSizes[2] = 10;
            dropSizes[3] = 8;
        }
        else
        {
            dropSizes[0] = (int)Math.floor(9 * stretchRatio);
            dropSizes[1] = (int)Math.floor(8 * stretchRatio);
            dropSizes[2] = (int)Math.floor(7 * stretchRatio);
            dropSizes[3] = (int)Math.floor(6 * stretchRatio);
        }*/
        circleAnimation.setDropSizes(dropSizes);
        return circleAnimation;
    }
    
    public CitizenCategoryListItem createCategoryListItem(int id)
    {
        CitizenCategoryListItem listItem = new CitizenCategoryListItem(id);
        TnUiArgs args = listItem.getTnUiArgs();
        listItem.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_POI_SEARCH_SINGLE_CATEGORY));
        args.put(TnUiArgs.KEY_PREFER_HEIGHT, new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
        {
            public Object decorate(TnUiArgAdapter args)
            {
                int max = AppConfigHelper.getMaxDisplaySize();
                return PrimitiveTypeCache.valueOf(max * 86 / 1000);
            }
        }));
        args.put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, NinePatchImageDecorator.LIST_ITEM_UNFOCUS);
        args.put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.LIST_ITEM_FOCUSED);
        return listItem;
    }
    
    public CitizenWebComponent createCitizenWebComponent(IWebViewContainer container, int id)
    {
        return createCitizenWebComponent(container, id, true);
    }
    
    public CitizenWebComponent createCitizenWebComponent(IWebViewContainer container,int id, boolean needWaitProgress)
    {
        CitizenCircleAnimation animation = createCircleAnimation(0, false);
        
        return createCitizenWebComponent(container, id, animation, needWaitProgress);
    }
    
    public CitizenWebComponent createCitizenWebComponent(IWebViewContainer container, int id, CitizenCircleAnimation animation, boolean needWaitProgress)
    {
        CitizenWebComponent webComponent = new CitizenWebComponent(id, animation, needWaitProgress);
        
        if(container != null)
        {
            container.addWebView(webComponent);
        }
        
        return webComponent;
    }
    
    public CitizenSlidableContainer createSlidableContainer(int id)
    {
        CitizenSlidableContainer container = new CitizenSlidableContainer(id);
        return container;
    }
    
    public FrogMessageBox createNotificationMessageBox(int id, String message, int timeout)
    {
        FrogMessageBox notificationMessageBox = new FrogMessageBox(id , message);
        notificationMessageBox.setTimeout(timeout, ICommonConstants.CMD_COMMON_OK);
        TnUiArgs args = notificationMessageBox.getTnUiArgs();
        notificationMessageBox.setMessageFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_MESSAGE_BOX));
        
        args.put(TnUiArgs.KEY_PREFER_HEIGHT, new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
        {
            public Object decorate(TnUiArgAdapter args)
            {
                int max = AppConfigHelper.getMaxDisplaySize();
                return PrimitiveTypeCache.valueOf(max * 80 / 1000);
            }
        }));
        args.put(TnUiArgs.KEY_PREFER_WIDTH, new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
        {
            public Object decorate(TnUiArgAdapter args)
            {
                int max = AppConfigHelper.getMaxDisplaySize();
                return PrimitiveTypeCache.valueOf(max * 350 / 1000);
            }
        }));
        return notificationMessageBox;
    }
    
    public ScoutMultiLineButton createScoutMultiLineButton(int id, String title, AbstractTnImage buttonIconFocus,
        AbstractTnImage buttonIconBlur, String content, String time)
    {
        ScoutMultiLineButton button = new ScoutMultiLineButton(id, title, buttonIconFocus, buttonIconBlur, content, time);
        return button;
    }
    
    public String getDisplayDistance(Address address, Stop anchor, boolean useServerSideDistance)
    {
        String distStr = null;
        int distInt = 0;
        if (useServerSideDistance && address.getPoi() != null && address.getPoi().getBizPoi() != null && address.getPoi().getBizPoi().getDistance() != null)
        {
            distInt = Integer.parseInt(address.getPoi().getBizPoi().getDistance());
        }
        if (distInt <= 0)
        {
            distInt = getDistance(address.getStop(), anchor);
        }
        if (distInt < 0)
        {
            return null;
        }

        Preference distanceUnit = ((DaoManager) DaoManager.getInstance()).getPreferenceDao().getPreference(
            Preference.ID_PREFERENCE_DISTANCEUNIT);
        int distanceUnitValue = 0;
        if (distanceUnit != null)
        {
            distanceUnitValue = distanceUnit.getIntValue();
        }
        else
        {
            //Hardcode for test
            distanceUnitValue = StringConverter.UNIT_MILE;
        }
        distStr = ResourceManager.getInstance().getStringConverter().convertDistanceMeterToMile(distInt, distanceUnitValue);
        return distStr;
    }
    
    public String getDisplayDistance(int distInt)
    {
        String distStr = null;
        Preference distanceUnit = ((DaoManager) DaoManager.getInstance()).getPreferenceDao().getPreference(
            Preference.ID_PREFERENCE_DISTANCEUNIT);
        int distanceUnitValue = 0;
        if (distanceUnit != null)
        {
            distanceUnitValue = distanceUnit.getIntValue();
        }
        else
        {
            // Hardcode for test
            distanceUnitValue = StringConverter.UNIT_MILE;
        }
        distStr = ResourceManager.getInstance().getStringConverter().convertDistanceMeterToMile(distInt, distanceUnitValue);
        return distStr;
    }
    
    public int getDistance(Stop stop, Stop anchor)
    {
        if (stop == null)
        {
            return -1;
        }

        int anchorLat = 0;
        int anchorLon = 0;
        if (anchor == null || anchor.getType() == Stop.STOP_CURRENT_LOCATION)
        {
            TnLocation currentLocation = LocationProvider.getInstance().getCurrentLocation(
                LocationProvider.TYPE_GPS | LocationProvider.TYPE_NETWORK);
            if (currentLocation == null)
            {
                return -1;
            }
            else
            {
                anchorLat = currentLocation.getLatitude();
                anchorLon = currentLocation.getLongitude();
            }
        }
        else
        {
            anchorLat = anchor.getLat();
            anchorLon = anchor.getLon();
        }

        int distance = (int) GeodesicLine(DataUtil.degreeToRadian(anchorLat / 100000d), DataUtil.degreeToRadian(anchorLon / 100000d),
            DataUtil.degreeToRadian(stop.getLat() / 100000d), DataUtil.degreeToRadian(stop.getLon() / 100000d));

        return distance;
    }
    
    public CitizenProfileSwitcher createCitizenProfileSwitcher(int id, boolean isSwitchOn, String itemName)
    {
        CitizenProfileSwitcher profileSwitcher = new CitizenProfileSwitcher(id);
        profileSwitcher.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.LIST_ITEM_UNFOCUS);
        profileSwitcher.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, NinePatchImageDecorator.LIST_ITEM_UNFOCUS);
        profileSwitcher.setSwitchOn(isSwitchOn);
        profileSwitcher.setItemName(itemName);
        profileSwitcher.setForegroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_BL), UiStyleManager
                .getInstance().getColor(UiStyleManager.TEXT_COLOR_BL));
        profileSwitcher.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_MY_PROFILE_ITEM));
        profileSwitcher.setFocusable(true);
        return profileSwitcher;
    }
    

    private double GeodesicLine(double latStart, double lonStart, double latEnd, double lonEnd)
    {
        // Input£º170181690 millseconds(47.272691666666667degree)£¬475273606, 170181698, 475273509£¬result should be 2.05
        // meter
        // WGS-84 ellipsphere
        double e2 = 0.0066943799013;
        double e = Math.pow(e2 / (1 - e2), 0.5);
        double a = 6378137.0;

        double Bm = 0.5 * (latStart + latEnd);
        double deltaL = lonEnd - lonStart;
        double deltaB = latEnd - latStart;
        double t = Math.tan(Bm);
        double p = e * Math.cos(Bm);
        double W = Math.pow((1 - e2 * Math.pow(Math.sin(Bm), 2)), 0.5);
        double Nm = a / W;
        double Vm = Math.pow(W * (1 + e * e), 0.5);
        //
        double r01 = Nm * Math.cos(Bm);
        double r21 = r01 * (1 - p * p - 9 * p * p * t * t) / 24;
        double r03 = Nm * Math.pow(Math.cos(Bm), 3) * t * t / 24;
        double S10 = Nm / (Vm * Vm);
        double S12 = Nm * Math.pow(Math.cos(Bm), 2) * (-2 - 3 * t * t + 3 * t * t * p * p) / 24;
        double S30 = Nm * (p * p - t * t * p * p) / 8;
        //
        double T1 = r01 * deltaL + r21 * deltaB * deltaB * deltaL + r03 * Math.pow(deltaL, 3);
        double T2 = S10 * deltaB + S12 * deltaB * deltaL * deltaL + S30 * Math.pow(deltaB, 3);
        //
//        double t01 = t * Math.cos(Bm);
//        double t21 = Math.cos(Bm) * t * (3 + 2 * p * p - 2 * Math.pow(p, 4)) / 24;
//        double t03 = Math.pow(Math.cos(Bm), 3) * t * (1 + p * p) / 12;
//        double deltaA = t01 * deltaL + t21 * deltaB * deltaB * deltaL + t03 * Math.pow(deltaL, 3);
        //
        double Am = Math.atan2(T1, T2);
        double dist = T2 / Math.cos(Am); // distance
        // double angle = Am - 0.5 * deltaA; // azimuth £¬radian (reserved)

        return dist;
    }
}
