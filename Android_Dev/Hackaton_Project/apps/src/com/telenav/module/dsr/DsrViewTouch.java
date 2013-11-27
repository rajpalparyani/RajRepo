/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * DsrViewTouch.java
 *
 */
package com.telenav.module.dsr;

import com.telenav.data.datatypes.address.Address;
import com.telenav.data.serverproxy.impl.IPoiSearchProxy;
import com.telenav.dsr.DsrManager;
import com.telenav.module.AppConfigHelper;
import com.telenav.mvc.AbstractCommonUiDecorator;
import com.telenav.mvc.AbstractCommonView;
import com.telenav.res.IStringCommon;
import com.telenav.res.IStringDsr;
import com.telenav.res.ResourceManager;
import com.telenav.sdk.kontagent.KontagentLogger;
import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.core.AbstractTnContainer;
import com.telenav.tnui.core.AbstractTnUiHelper;
import com.telenav.tnui.core.TnKeyEvent;
import com.telenav.tnui.core.TnMenu;
import com.telenav.tnui.core.TnScreen;
import com.telenav.tnui.core.TnUiArgs;
import com.telenav.tnui.core.TnUiArgs.ITnUiArgsDecorator;
import com.telenav.tnui.core.TnUiArgs.TnUiArgAdapter;
import com.telenav.tnui.core.TnUiEvent;
import com.telenav.tnui.graphics.AbstractTnFont;
import com.telenav.tnui.graphics.AbstractTnGraphics;
import com.telenav.tnui.widget.TnLinearContainer;
import com.telenav.tnui.widget.TnPopupContainer;
import com.telenav.ui.NinePatchImageDecorator;
import com.telenav.ui.UiFactory;
import com.telenav.ui.UiStyleManager;
import com.telenav.ui.citizen.CitizenCircleAnimation;
import com.telenav.ui.citizen.map.MapContainer;
import com.telenav.ui.frogui.widget.FrogButton;
import com.telenav.ui.frogui.widget.FrogLabel;
import com.telenav.ui.frogui.widget.FrogMultiLine;
import com.telenav.ui.frogui.widget.FrogNullField;
import com.telenav.util.PrimitiveTypeCache;

/**
 *@author bduan
 *@date Aug 23, 2010
 */
class DsrViewTouch extends AbstractCommonView implements IDsrConstants
{
    protected int exampleLabelHeight;
    
    protected int errorLabelHeight;
    
    protected int lastOrientation;
    
    public DsrViewTouch(AbstractCommonUiDecorator uiDecorator)
    {
        super(uiDecorator);
    }

    protected TnPopupContainer createPopup(int state)
    {
        switch (state)
        {
            case STATE_INIT:
            {
                return createDsrPopup(state);
            }
            case STATE_TEST_THINKING:
            {
                String labelStr = ResourceManager.getInstance().getCurrentBundle().getString(IStringDsr.RES_LABEL_THINKING, IStringDsr.FAMILY_DSR);
                return UiFactory.getInstance().createProgressBox(0, labelStr);
            }
            case STATE_DO_SEARCH:
            {
                TnPopupContainer currentPopup = this.getCurrentPopup();
                updateSearchPopup(currentPopup);
                break;
            }
        }
        return null;
    }

    protected void updateSearchPopup(TnPopupContainer currentPopup)
    {
        if(currentPopup != null)
        {
            AbstractTnContainer container = currentPopup.getContent();
            String searchingActionStr = "";
            
            int searchType = this.model.getInt(KEY_I_SEARCH_TYPE);
            if(searchType == IPoiSearchProxy.TYPE_SEARCH_ALONG_ROUTE)
            {
                int alongRouteType = this.model.getInt(KEY_I_SEARCH_ALONG_TYPE);
                if(alongRouteType == IPoiSearchProxy.TYPE_SEARCH_ALONG_UPHEAD)
                {
                    searchingActionStr = ResourceManager.getInstance().getCurrentBundle().getString(IStringDsr.RES_LABEL_UP_AHEAD,
                        IStringDsr.FAMILY_DSR);
                }
                else
                {
                    searchingActionStr = ResourceManager.getInstance().getCurrentBundle().getString(IStringDsr.RES_LABEL_NEAR_DES,
                        IStringDsr.FAMILY_DSR);
                }
            }
            else
            {
                searchingActionStr = ResourceManager.getInstance().getCurrentBundle().getString(IStringDsr.RES_LABEL_SEARCHING_NEARBY,
                    IStringDsr.FAMILY_DSR);
            }
            AbstractTnComponent infoContainer = container.getComponentById(ID_INFO_CONTAINER);
            infoContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
            {
                public Object decorate(TnUiArgAdapter args)
                {
                    int lines = 3;
                    return PrimitiveTypeCache.valueOf(((DsrUiDecorator) uiDecorator).DES_LABEL_HEIGHT.getInt() * lines
                        + ((DsrUiDecorator) uiDecorator).THINKING_GAP_LINE_HEIGHT.getInt());
                }
            }));
            if (infoContainer instanceof AbstractTnContainer)
            {
                AbstractTnFont boldFont = UiStyleManager.getInstance().getFont(UiStyleManager.FONT_POPUP_TITLE);

                if (searchType != IPoiSearchProxy.TYPE_SEARCH_ALONG_ROUTE)
                {
                    Address anchorAddress = (Address) model.get(KEY_O_SELECTED_ADDRESS);
                    if (anchorAddress != null && anchorAddress.getStop() != null )
                    {
                        String anchorString = anchorAddress.getStop().getLabel();
                        if (anchorString != null && anchorString.length() > 0)
                        {
                            String aroundStr = ResourceManager.getInstance().getCurrentBundle().getString(IStringDsr.RES_LABEL_AROUND,
                                IStringDsr.FAMILY_DSR);
                            FrogLabel stopLabel = UiFactory.getInstance().createLabel(0, aroundStr + " " + anchorString);
                            stopLabel.setFont(boldFont);
                            stopLabel.setPadding(0, 0, 0, 0);
                            ((AbstractTnContainer) infoContainer).add(stopLabel, 0);
                            searchingActionStr = ResourceManager.getInstance().getCurrentBundle().getString(IStringDsr.RES_LABEL_SEARCHING,
                                IStringDsr.FAMILY_DSR);
                        }
                    }
                }
                
                String name =  model.getString(KEY_S_COMMON_SHOW_SEARCH_TEXT);
                if(name == null)
                {
                    name = model.getString(KEY_S_COMMON_SEARCH_TEXT);
                }
                FrogLabel searchWordLabel = UiFactory.getInstance().createLabel(0, name);
                searchWordLabel.setFont(boldFont);
                searchWordLabel.setPadding(0, 0, 0, 0);
                ((AbstractTnContainer) infoContainer).add(searchWordLabel, 0);
            }

            AbstractTnComponent label = container.getComponentById(ID_INFO_ACTION_LABEL);
            if (label instanceof FrogLabel)
            {
                ((FrogLabel) label).setText(searchingActionStr);
            }

            AbstractTnComponent button = container.getComponentById(ID_BUTTON_FINISH);
            if (button instanceof FrogButton)
            {
                button.setEnabled(true);
                String cancelStr = ResourceManager.getInstance().getCurrentBundle().getString(IStringCommon.RES_BTTN_CANCEL,
                    IStringCommon.FAMILY_COMMON);
                ((FrogButton) button).setText(cancelStr);

                TnMenu menu = UiFactory.getInstance().createMenu();
                menu.add("", CMD_COMMON_BACK);
                button.setMenu(menu, AbstractTnComponent.TYPE_CLICK);
            }
        }
    }

    protected TnPopupContainer createDsrPopup(int state)
    {
        TnLinearContainer root = UiFactory.getInstance().createLinearContainer(0, true, AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);;
        root.add(createInstructionContainer(model.getBool(KEY_B_IS_PLAY_ERROR_AUDIO)));
        root.add(createBottomContainer(false));
        
        TnPopupContainer popupContainer = UiFactory.getInstance().createPopupContainer(state);
        popupContainer.setContent(root);
        popupContainer.setSizeChangeListener(this);
        return popupContainer;
    }
    
    protected TnLinearContainer createBottomContainer(boolean isRecognizing)
    {
        TnLinearContainer bottomContainer = UiFactory.getInstance().createLinearContainer(0, true, AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);
        bottomContainer.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, NinePatchImageDecorator.MESSAGE_BOX_BOTTOM_SINGLE_LINE_BG);
        bottomContainer.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.MESSAGE_BOX_BOTTOM_SINGLE_LINE_BG);
        bottomContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((DsrUiDecorator) this.uiDecorator).BOTTOM_BAR_HEIGHT);
        bottomContainer.setPadding(30, 10, 30, 20);
        
        String str = ResourceManager.getInstance().getCurrentBundle().getString(IStringCommon.RES_BTTN_CANCEL, IStringCommon.FAMILY_COMMON);
       FrogButton btn = UiFactory.getInstance().createButton(ID_BUTTON_FINISH, str);
        TnMenu menu = UiFactory.getInstance().createMenu();
        menu.add("", CMD_COMMON_BACK);
        btn.setMenu(menu, AbstractTnComponent.TYPE_CLICK);
        
//        if(isRecognizing)
//        {
//            btn.setEnabled(false);
//        }
        
        btn.setCommandEventListener(this);
        bottomContainer.add(btn);
        
        return bottomContainer;
    }
    
    protected TnLinearContainer createInstructionContainer(boolean isErrorMsg)
    {
        final boolean isError = isErrorMsg;
        TnLinearContainer rootContainer = UiFactory.getInstance().createLinearContainer(ID_INSTRUCTION_CONTAINER, true, AbstractTnGraphics.HCENTER | AbstractTnGraphics.TOP);
        rootContainer.add(getDescriptionContainer(isErrorMsg));
        rootContainer.getComponentById(ID_MIC_CONTAINER).getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
        {
            public Object decorate(TnUiArgAdapter args)
            {
                int orientation = ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation();
                int topPadding = UiStyleManager.getInstance().getFont(UiStyleManager.FONT_LABEL).getHeight() + 8;
                int heightWidhtError = topPadding + ((DsrUiDecorator) uiDecorator).TITLE_LABEL_HEIGHT.getInt() + ((DsrUiDecorator) uiDecorator).GAP_LINE_HEIGHT.getInt();
                if (isError)
                {                    
                    return PrimitiveTypeCache.valueOf(((DsrUiDecorator)uiDecorator).TOP_CONTAINER_HEIGHT.getInt() - heightWidhtError - errorLabelHeight);
                }
                else
                {
                    int lines = orientation == AbstractTnUiHelper.ORIENTATION_PORTRAIT ? 3 : 2;
                    return PrimitiveTypeCache.valueOf(((DsrUiDecorator)uiDecorator).TOP_CONTAINER_HEIGHT.getInt() - heightWidhtError - exampleLabelHeight - ((DsrUiDecorator) uiDecorator).DES_LABEL_HEIGHT.getInt() * lines);
                }
            }
        }));
        rootContainer.requestPaint();
        return rootContainer;
    }

    protected AbstractTnComponent createMicButton()
    {
        TnLinearContainer micContainer = UiFactory.getInstance().createLinearContainer(ID_MIC_CONTAINER, true, AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);;
        
        MicrophoneComponent micComponent = new MicrophoneComponent(0);
        micComponent.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((DsrUiDecorator)uiDecorator).MIC_HEIGHT);
        micComponent.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((DsrUiDecorator)uiDecorator).TITLE_LABEL_WIDTH);
        micComponent.setCommandEventListener(this);
        micComponent.sublayout(0, 0);
        DsrManager.getInstance().setVolumeChangeListener(micComponent);
        micContainer.add(micComponent);
        return micContainer;
    } 

    protected AbstractTnContainer getDescriptionContainer(boolean isErrorMsg)
    {
        lastOrientation = ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation();
        boolean isPortrait = lastOrientation == AbstractTnUiHelper.ORIENTATION_PORTRAIT;
        
        TnLinearContainer desContainer = UiFactory.getInstance().createLinearContainer(0, true, AbstractTnGraphics.LEFT | AbstractTnGraphics.TOP);
        desContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((DsrUiDecorator) this.uiDecorator).TOP_CONTAINER_HEIGHT);
        desContainer.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, NinePatchImageDecorator.MESSAGE_BOX_TOP_BG);
        desContainer.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.MESSAGE_BOX_TOP_BG);
        int topPadding = UiStyleManager.getInstance().getFont(UiStyleManager.FONT_LABEL).getHeight() + 8;
        desContainer.setPadding(30, topPadding, 30, 0);

        final FrogLabel title = UiFactory.getInstance().createLabel(0,
            ResourceManager.getInstance().getCurrentBundle().getString(IStringDsr.RES_LABEL_SAY_COMMAND, IStringDsr.FAMILY_DSR));
        title.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_DSR_EXAMPLE_TITLE));
        title.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((DsrUiDecorator) this.uiDecorator).TITLE_LABEL_WIDTH);
        title.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((DsrUiDecorator) this.uiDecorator).TITLE_LABEL_HEIGHT);

        FrogNullField blank = new FrogNullField(0);
        blank.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((DsrUiDecorator) this.uiDecorator).GAP_LINE_WIDTH);
        blank.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((DsrUiDecorator) this.uiDecorator).GAP_LINE_HEIGHT);
        desContainer.add(title);
        desContainer.add(blank);

        String exampleLabelStr = null;
        String errorLabelStr = null;
        String ex0LabelStr = null;
        String ex1LabelStr = null;
        String ex2LabelStr = null;
        String ex3LabelStr = null;
        String ex23LabelStr = null;
        
        if(isErrorMsg)
        {
            String silenceStr = this.model.fetchString(KEY_S_DSR_SILENCE_MASSAGE);
            if (silenceStr != null)
            {
                errorLabelStr = silenceStr;
            }
            else
            {
                errorLabelStr = ResourceManager.getInstance().getCurrentBundle().getString(IStringDsr.RES_LABEL_ERROR_MSG, IStringDsr.FAMILY_DSR);
            }
            ex0LabelStr = " ";
            ex1LabelStr = " ";
            ex2LabelStr = " ";
            ex3LabelStr = " ";
			ex23LabelStr = " ";
        }
        else
        {
            exampleLabelStr = ResourceManager.getInstance().getCurrentBundle().getString(IStringDsr.RES_LABEL_EXAMPLES, IStringDsr.FAMILY_DSR);
            ex0LabelStr = ResourceManager.getInstance().getCurrentBundle().getString(IStringDsr.RES_LABEL_EX0, IStringDsr.FAMILY_DSR) + (isPortrait ? "" : ",");
            ex1LabelStr = ResourceManager.getInstance().getCurrentBundle().getString(IStringDsr.RES_LABEL_EX1, IStringDsr.FAMILY_DSR) + ( isPortrait ? "" : ",");
            ex2LabelStr = ResourceManager.getInstance().getCurrentBundle().getString(IStringDsr.RES_LABEL_EX2, IStringDsr.FAMILY_DSR);
            ex3LabelStr = ResourceManager.getInstance().getCurrentBundle().getString(IStringDsr.RES_LABEL_EX3, IStringDsr.FAMILY_DSR);
            ex23LabelStr = ex2LabelStr + " " + ex3LabelStr;
        }
        
        FrogMultiLine errorLabel = UiFactory.getInstance().createMultiLine(0, errorLabelStr);
        FrogLabel exampleLabel = UiFactory.getInstance().createLabel(0, exampleLabelStr);
        FrogLabel ex0Label = UiFactory.getInstance().createLabel(0, ex0LabelStr);
        FrogLabel ex1Label = UiFactory.getInstance().createLabel(0, ex1LabelStr);
        FrogLabel ex2Label = UiFactory.getInstance().createLabel(0, ex2LabelStr);
        FrogLabel ex3Label = UiFactory.getInstance().createLabel(0, ex3LabelStr);
        FrogLabel ex23Label = UiFactory.getInstance().createLabel(0, ex23LabelStr);

        AbstractTnFont boldFont = UiStyleManager.getInstance().getFont(UiStyleManager.FONT_DSR_EXAMPLE_TITLE);
        AbstractTnFont exampleFont = UiStyleManager.getInstance().getFont(UiStyleManager.FONT_DSR_EXAMPLE);
		errorLabel.setFont(exampleFont);
        exampleLabel.setFont(boldFont);
       
        ex0Label.setFont(exampleFont);
        ex1Label.setFont(exampleFont);
        ex2Label.setFont(exampleFont);
        ex3Label.setFont(exampleFont);
        ex23Label.setFont(exampleFont);
        
        if (isErrorMsg)
        {
            ex0Label.setVisible(false);
            ex1Label.setVisible(false);
            ex2Label.setVisible(false);
            ex3Label.setVisible(false);
            ex23Label.setVisible(false);
        }
        
        int color = UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_ME_GR);
        exampleLabel.setForegroundColor(color, color);
        errorLabel.setForegroundColor(color);
        ex0Label.setForegroundColor(color, color);
        ex1Label.setForegroundColor(color, color);
        ex2Label.setForegroundColor(color, color);
        ex3Label.setForegroundColor(color, color);
        ex23Label.setForegroundColor(color, color);
        
        errorLabel.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
        {
            public Object decorate(TnUiArgAdapter args)
            {
                return PrimitiveTypeCache.valueOf(((DsrUiDecorator) uiDecorator).TITLE_LABEL_WIDTH.getInt() - title.getLeftPadding() - title.getRightPadding());
            }
        }));
        ex0Label.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((DsrUiDecorator) this.uiDecorator).DES_LABEL_HEIGHT);
        ex1Label.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((DsrUiDecorator) this.uiDecorator).DES_LABEL_HEIGHT);
        ex2Label.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((DsrUiDecorator) this.uiDecorator).DES_LABEL_HEIGHT);
        ex3Label.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((DsrUiDecorator) this.uiDecorator).DES_LABEL_HEIGHT);
        ex23Label.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((DsrUiDecorator) this.uiDecorator).DES_LABEL_HEIGHT);

        exampleLabel.setPadding(title.getLeftPadding(), title.getTopPadding(), title.getRightPadding(), 0);
        errorLabel.setPadding(title.getLeftPadding(), title.getTopPadding(), title.getRightPadding(), 0);
        ex0Label.setPadding(title.getLeftPadding(), 0, title.getRightPadding(), 0);
        ex1Label.setPadding(title.getLeftPadding(), 0, title.getRightPadding(), 0);
        ex2Label.setPadding(title.getLeftPadding(), 0, title.getRightPadding(), 0);
        ex3Label.setPadding(title.getLeftPadding(), 0, title.getRightPadding(), 0);
        ex23Label.setPadding(title.getLeftPadding(), 0, title.getRightPadding(), 0);

        exampleLabel.sublayout(0, 0);
        errorLabel.sublayout(0, 0);
        exampleLabelHeight = exampleLabel.getPreferredHeight();
        errorLabelHeight = errorLabel.getPreferredHeight();        

        if (isErrorMsg)
        {
            desContainer.add(errorLabel);
        }
        else
        {
            desContainer.add(exampleLabel);
        }

        if (isPortrait)
        {
            desContainer.add(ex0Label);
            desContainer.add(ex1Label);
            desContainer.add(ex23Label);
        }
        else
        {
            TnLinearContainer topLabelContainer = UiFactory.getInstance().createLinearContainer(0, false);
            topLabelContainer.add(ex0Label);
            topLabelContainer.add(ex1Label);
            topLabelContainer.add(ex2Label);
            
            desContainer.add(topLabelContainer);
            desContainer.add(ex3Label);
        }
        
        desContainer.add(createMicButton());

        return desContainer;
    }

    protected boolean isShownTransientView(int state)
    {
        switch (state)
        {
            case STATE_INIT:
            {
                return true;
            }
        }

        return super.isShownTransientView(state);
    }
    
    protected int transformCommandDelegate(int state, TnUiEvent tnUiEvent)
    {
        int cmd = CMD_NONE;
        
        switch(state)
        {
            //TODO:
        }
        
        return cmd;
    }

    protected boolean updatePopup(int state, TnPopupContainer popup)
    {
        switch (state)
        {
            case STATE_INIT:
            {
                if(model.fetchBool(KEY_B_IS_DO_RECOGNIZE))
                {
                    TnLinearContainer root = UiFactory.getInstance().createLinearContainer(0, true, AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);;
                    root.add(createThinkingContainer());
                    root.add(createBottomContainer(true));
                    
                    popup.setContent(root);
                }
                else
                {
                    boolean isPlayErrorMsg = model.getBool(KEY_B_IS_PLAY_ERROR_AUDIO);
                    
                    TnLinearContainer root = UiFactory.getInstance().createLinearContainer(0, true, AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);;
                    root.add(createInstructionContainer(isPlayErrorMsg));
                    root.add(createBottomContainer(isPlayErrorMsg));
                    
                    popup.setContent(root);
                }
                break;
            }
        }
        return false;
    }
    
    protected TnLinearContainer createThinkingContainer()
    {
        
        TnLinearContainer rootContainer = UiFactory.getInstance().createLinearContainer(ID_THINKING_CONTAINER, true, AbstractTnGraphics.HCENTER | AbstractTnGraphics.TOP);
        rootContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((DsrUiDecorator) this.uiDecorator).TOP_CONTAINER_HEIGHT);
        rootContainer.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, NinePatchImageDecorator.MESSAGE_BOX_TOP_BG);
        rootContainer.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.MESSAGE_BOX_TOP_BG);

        int topPadding = UiStyleManager.getInstance().getFont(UiStyleManager.FONT_LABEL).getHeight() + 10;
        rootContainer.setPadding(30, topPadding, 30, 0);
        
        TnLinearContainer desContainer = UiFactory.getInstance().createLinearContainer(ID_INFO_CONTAINER, true, AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);
        desContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((DsrUiDecorator) this.uiDecorator).TITLE_LABEL_WIDTH);
        desContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
        {
            public Object decorate(TnUiArgAdapter args)
            {
                int orientation = ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation();
                int lines = 3;
                if (orientation == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                {
                    lines = 3;
                }
                else
                {
                    if (AppConfigHelper.isTabletSize())
                    {
                        lines = 3;
                    }
                    else
                    {
                        lines = 2;
                    }  
                }  
                return PrimitiveTypeCache.valueOf(((DsrUiDecorator) uiDecorator).DES_LABEL_HEIGHT.getInt() * lines
                    + ((DsrUiDecorator) uiDecorator).THINKING_GAP_LINE_HEIGHT.getInt());
            }
        }));



        String thinkingStr = ResourceManager.getInstance().getCurrentBundle().getString(IStringDsr.RES_LABEL_THINKING, IStringDsr.FAMILY_DSR);
        final FrogLabel thingkingLable = UiFactory.getInstance().createLabel(ID_INFO_ACTION_LABEL, thinkingStr);

        thingkingLable.sublayout(0, 0);

        desContainer.add(thingkingLable);
        
        
        rootContainer.add(desContainer);
        rootContainer.add(createCircleAnimation());
        
        return rootContainer;
    }

    protected CitizenCircleAnimation createCircleAnimation()
    {
        CitizenCircleAnimation  circleAnimation = (CitizenCircleAnimation)UiFactory.getInstance().createDefaultProgressAnimation();
            circleAnimation.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
            {
                public Object decorate(TnUiArgAdapter args)
                {
                    return PrimitiveTypeCache.valueOf(AppConfigHelper.getMinDisplaySize() / 3);
                }
            }));

            circleAnimation.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
            {
                public Object decorate(TnUiArgAdapter args)
                {
                    return PrimitiveTypeCache.valueOf(((DsrUiDecorator)uiDecorator).MIC_HEIGHT.getInt());
                }
            }));
            circleAnimation.setId(ID_DASHBOARD_CIRCLE_ANNIMATION);
            return circleAnimation;
    }
    
    public void onSizeChanged(AbstractTnComponent tnComponent, int w, int h,
            int oldw, int oldh)
    {
        switch(model.getState())
        {
            case STATE_INIT:
            case STATE_DO_SEARCH:
            {
                int orintation = ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance())
                        .getOrientation();
                if (orintation != lastOrientation)
                {
                    resetDsrPopup();
                    lastOrientation = orintation;
                }
                break;
            }
        }
    }

    protected void resetDsrPopup()
    {
        TnPopupContainer popupContainer = getCurrentPopup();
        AbstractTnContainer root = popupContainer.getContent();
        AbstractTnContainer thinkingContainer=(AbstractTnContainer)root.getComponentById(ID_THINKING_CONTAINER);
        if(thinkingContainer != null)
        {
            CitizenCircleAnimation circleAnimation = (CitizenCircleAnimation) thinkingContainer.getComponentById(ID_DASHBOARD_CIRCLE_ANNIMATION);
            if (circleAnimation != null)
            {
                circleAnimation.reset();
                thinkingContainer.remove(circleAnimation);
                thinkingContainer.add(createCircleAnimation(), 1);
            }
        }
        
        AbstractTnComponent component = root.getComponentById(ID_INSTRUCTION_CONTAINER);
        if(component != null)
        {
            AbstractTnContainer instructionContainer = createInstructionContainer(model.getBool(KEY_B_IS_PLAY_ERROR_AUDIO));
            root.remove(0);
            root.add(instructionContainer, 0);
        }
    }

    protected boolean updateScreen(int state, TnScreen screen)
    {
        return false;
    }

    protected void popAllViews()
    {
        super.popAllViews();
    }

    protected TnScreen createScreen(int state)
    {
        TnScreen screen = null;

        switch (state)
        {
            case STATE_UNIT_TEST:
            {
                //screen = createUnitTest();
                break;
            }
        }

        return screen;
    }
    
    protected void activate()
    {
        MapContainer.getInstance().resume();
    }

    protected void deactivateDelegate()
    {
        MapContainer.getInstance().pause();
    }
    
    public boolean handleUiEvent(TnUiEvent tnUiEvent)
    {
        logKtUiEvent(tnUiEvent);
        return super.handleUiEvent(tnUiEvent);
    }
    
    private void logKtUiEvent(TnUiEvent tnUiEvent)
    {
        if (tnUiEvent != null && tnUiEvent.getType() == TnUiEvent.TYPE_COMMAND_EVENT && (tnUiEvent.getCommandEvent() != null))
        {
            int command = tnUiEvent.getCommandEvent().getCommand();
            switch (command)
            {
                case CMD_COMMON_BACK:
                    KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_DSR,
                            KontagentLogger.DSR_MANUAL_CANCEL);
                    break;
            }
        }
        else if(tnUiEvent.getType() == TnUiEvent.TYPE_KEY_EVENT && tnUiEvent.getKeyEvent() != null && tnUiEvent.getKeyEvent().getAction() == TnKeyEvent.ACTION_DOWN
                && tnUiEvent.getKeyEvent().getCode() == TnKeyEvent.KEYCODE_BACK)
        {
            KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_DSR, KontagentLogger.DSR_MANUAL_CANCEL);
        }
    }
    
    /***********************************************************************************************/
    /***********************************************************************************************/
    /**************                                                                    *************/
    /**************          from here to the end of file is test codes for dsr        *************/
    /**************                                                                    *************/
    /***********************************************************************************************/
    /***********************************************************************************************/
    
    
//    protected boolean preProcessUIEvent(TnUiEvent tnUiEvent)
//    {
//        int type = tnUiEvent.getType();
//        switch (type)
//        {
//            case TnUiEvent.TYPE_COMMAND_EVENT:
//            {
//                int commandId = tnUiEvent.getCommandEvent().getCommand();
//                if (commandId == CMD_DO_TEST)
//                {
//                    AbstractTnComponent component = tnUiEvent.getComponent();
//                    model.put(KEY_I_RES_ID, component.getId());
//                }
//                break;
//            }
//        }
//        
//        return super.preProcessUIEvent(tnUiEvent);
//    }
//    
//    class UnitTestData
//    {
//        int resID;
//        String name;
//        
//        UnitTestData(int resID, String name)
//        {
//            this.resID = resID;
//            this.name = name;
//        }
//    }
//
//    private TnScreen createUnitTest()
//    {
//        final UnitTestData[] testData = new UnitTestData[]
//        {
//                new UnitTestData(IAudioRes.DSR_DRIVE_CITY, "Drive CITY"),
//                new UnitTestData(IAudioRes.DSR_DRIVE_STARBUCKS_NEARBY, "Drive Starbucks"),
//                new UnitTestData(IAudioRes.DSR_DRIVE_SFO, "Drive SFO"),
//                new UnitTestData(IAudioRes.DSR_DRIVE_FAV, "Drive Fav"),
//                new UnitTestData(IAudioRes.DSR_DRIVE_HOME, "Drive Home"),
//                new UnitTestData(IAudioRes.DSR_DRIVE_STARBUCKS, "Drive Starbucks nearby"),
//                new UnitTestData(IAudioRes.DSR_DRIVE_WALMART, "Drive Walmart"),
//                new UnitTestData(IAudioRes.DSR_MAP_CURRENT, "Map Current"),
//                new UnitTestData(IAudioRes.DSR_MAP_SUNNYVALE, "Map Sunnyvale"),
//                new UnitTestData(IAudioRes.DSR_RESUME_TRIP, "Resume Trip"),
//                new UnitTestData(IAudioRes.DSR_RESUME_TRIP_1, "Resume Trip0"),
//                new UnitTestData(IAudioRes.DSR_SEARCH_MACDONALD, "Search Macdonald"),
//                new UnitTestData(IAudioRes.DSR_SEARCH_STARBUCKS_AT_SAN_FRANCISCO, "Search starbucks sanf..."),
//                new UnitTestData(IAudioRes.DSR_SEARCH_STARBUCKS_AT_SUNNYVALE, "Search starbucks sunnyvale"),
//                new UnitTestData(IAudioRes.DSR_SEARCH_STARBUCKS_AT_HERE, "Search starbucks here"),
//                new UnitTestData(IAudioRes.DSR_SEARCH_WALMART, "Search walmart"),
//                new UnitTestData(IAudioRes.DSR_SEARCH, "Search"), 
//        };
//
//        CitizenScreen screen = UiFactory.getInstance().createScreen(0);
//
//        FrogList list = UiFactory.getInstance().createList(0);
//        list.setAdapter(new FrogAdapter()
//        {
//            
//            public int getItemType(int position)
//            {
//                return 0;
//            }
//            
//            public int getCount()
//            {
//                return testData.length;
//            }
//            
//            public AbstractTnComponent getComponent(int position,
//                    AbstractTnComponent convertComponent, AbstractTnComponent parent)
//            {
//                FrogListItem item = null;
//                if(convertComponent == null)
//                {
//                    item = UiFactory.getInstance().createListItem(testData[position].resID);
//                    item.setText(testData[position].name);
//                    
//                    TnMenu menu = UiFactory.getInstance().createMenu();
//                    menu.add("", CMD_DO_TEST);
//                    item.setMenu(menu, AbstractTnComponent.TYPE_CLICK);
//                }
//                else
//                {
//                    item = (FrogListItem)convertComponent;
//                    item.setId(testData[position].resID);
//                    item.setText(testData[position].name);
//                }
//                
//                return item;
//            }
//        });
//        
//        screen.getContentContainer().add(list);
//        return screen;
//    }

}
