/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * AddFavoriteViewTouch.java
 *
 */
package com.telenav.module.ac.airport;

import java.util.Vector;

import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.dao.serverproxy.AddressDao;
import com.telenav.data.datatypes.address.Address;
import com.telenav.data.datatypes.address.Stop;
import com.telenav.location.TnLocation;
import com.telenav.mvc.AbstractCommonUiDecorator;
import com.telenav.mvc.AbstractCommonView;
import com.telenav.res.IStringAc;
import com.telenav.res.IStringAirport;
import com.telenav.res.ResourceManager;
import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.core.AbstractTnUiHelper;
import com.telenav.tnui.core.ITnTextChangeListener;
import com.telenav.tnui.core.TnKeyEvent;
import com.telenav.tnui.core.TnMenu;
import com.telenav.tnui.core.TnScreen;
import com.telenav.tnui.core.TnUiArgs;
import com.telenav.tnui.core.TnUiEvent;
import com.telenav.tnui.graphics.AbstractTnGraphics;
import com.telenav.tnui.widget.TnLinearContainer;
import com.telenav.tnui.widget.TnPopupContainer;
import com.telenav.ui.ImageDecorator;
import com.telenav.ui.NinePatchImageDecorator;
import com.telenav.ui.UiFactory;
import com.telenav.ui.UiStyleManager;
import com.telenav.ui.citizen.CitizenAddressListItem;
import com.telenav.ui.citizen.CitizenScreen;
import com.telenav.ui.citizen.CitizenTextField;
import com.telenav.ui.frogui.widget.FrogAdapter;
import com.telenav.ui.frogui.widget.FrogButton;
import com.telenav.ui.frogui.widget.FrogDropDownField;
import com.telenav.ui.frogui.widget.FrogKeywordFilter;
import com.telenav.ui.frogui.widget.FrogList;
import com.telenav.ui.frogui.widget.FrogListItem;
import com.telenav.ui.frogui.widget.FrogNullField;
import com.telenav.ui.frogui.widget.FrogProgressBox;

/**
 * @author wzhu (wzhu@telenav.cn)
 * @date 2010-12-2
 */
class AirportViewTouch extends AbstractCommonView implements IAirportConstants, ITnTextChangeListener
{
    protected static final int MAX_SIZE = 5;

    public AirportViewTouch(AbstractCommonUiDecorator uiDecorator)
    {
        super(uiDecorator);
    }

    protected int transformCommandDelegate(int state, TnUiEvent tnUiEvent)
    {
        int cmd = CMD_NONE;
        switch(state)
        {
            case STATE_MAIN:
            {
                if( tnUiEvent.getType() == TnUiEvent.TYPE_KEY_EVENT
                        && tnUiEvent.getKeyEvent().getAction() == TnKeyEvent.ACTION_UP
                        && tnUiEvent.getKeyEvent().getCode() == TnKeyEvent.KEYCODE_ENTER)
                {
                    if(tnUiEvent.getComponent() instanceof FrogDropDownField)
                    {
                        FrogDropDownField airportInputField = (FrogDropDownField) tnUiEvent.getComponent();
                        if (airportInputField.getText().trim().length() > 0)
                        {
                            this.model.put(KEY_S_AIRPORT, airportInputField.getText());
                            return CMD_SUBMIT;
                        }
                    }
                }
                break;
            }
        }
        return cmd;
    }

    protected TnPopupContainer createPopup(int state)
    {
        switch (state)
        {
            case STATE_VALIDATE_AIRPORT:
            {
                return createValidateAirportPopup(state);
            }
        }
        return null;
    }

    private TnPopupContainer createValidateAirportPopup(int state)
    {
        FrogProgressBox progressBox = UiFactory.getInstance().createProgressBox(
            state,
            ResourceManager.getInstance().getCurrentBundle()
                    .getString(IStringAirport.RES_LABLE_VALIDATING_AIRPORT, IStringAirport.FAMILY_AIRPROT));
        return progressBox;
    }


    protected boolean preProcessUIEvent(TnUiEvent tnUiEvent)
    {
        int type = tnUiEvent.getType();
        switch (type)
        {
            case TnUiEvent.TYPE_COMMAND_EVENT:
            {
                if (tnUiEvent.getComponent() instanceof FrogListItem && tnUiEvent.getCommandEvent().getCommand() == CMD_SELECT_AIRPORT)
                {
                    getTextField().closeVirtualKeyBoard();
                    Address address = new Address(Address.SOURCE_PREDEFINED);
                    address.setStop( (Stop) model.getVector(KEY_V_MATCHED_STOPS).elementAt(((FrogListItem) tnUiEvent.getComponent()).getId()));
                    model.put(KEY_O_SELECTED_ADDRESS, address);
                    
                }
            }
        }
        return super.preProcessUIEvent(tnUiEvent);
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
                return createAirportMainScreen(state);
            }
        }
        return null;
    }


    private TnScreen createAirportMainScreen(int state)
    {
        CitizenScreen airportScreen = UiFactory.getInstance().createScreen(state);
        airportScreen.getContentContainer().setBackgroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_WH));
        TnLinearContainer topContainer = UiFactory.getInstance().createLinearContainer(0, true,
            AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);
        topContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, uiDecorator.SCREEN_WIDTH);
        topContainer.setBackgroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_ME_GR));

        TnLinearContainer textFieldContainer = UiFactory.getInstance().createLinearContainer(0, false,
            AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);
        textFieldContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, uiDecorator.SCREEN_WIDTH);
        textFieldContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((AirportUiDecorator) uiDecorator).CONTAINER_DROPDOWN_HEIGHT);
        
        String hint = ResourceManager.getInstance().getCurrentBundle().getString(IStringAc.RES_HINT_AIRPORT, IStringAc.FAMILY_AC);
        
        CitizenTextField textField = UiFactory.getInstance().createCitizenTextField("", 100, 0, hint, ID_AIRPORT_TEXTFIELD,
           ImageDecorator.IMG_AC_BACKSPACE.getImage());
        textField.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((AirportUiDecorator) uiDecorator).DROPDOWNFIELD_WIDTH);
        textField.getTnUiArgs().put(CitizenTextField.KEY_PREFER_TEXTFIELD_HEIGHT, ((AirportUiDecorator) uiDecorator).DROPDOWNFIELD_HEIGHT);
        textField.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((AirportUiDecorator) uiDecorator).SEARCH_FIELD_HEIGHT);
        textField.setCommandEventListener(this);
        textField.setBackspaceCommand(CMD_BACKSPACE);
        textField.setTextChangeListener(this);
        textFieldContainer.add(textField);
        
        FrogButton button = UiFactory.getInstance().createButton(ID_SEARCH_BUTTON, "");
        button.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((AirportUiDecorator)uiDecorator).SEARCH_BUTTON_WIDTH);
        button.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((AirportUiDecorator)uiDecorator).SEARCH_BUTTON_WIDTH);
        button.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, NinePatchImageDecorator.SEARCH_BOX_BUTTON_UNFOCUS);
        button.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.SEARCH_BOX_BUTTON_FOCUSED);
        button.setIcon(ImageDecorator.IMG_AC_FIND_FOCUSED.getImage(), ImageDecorator.IMG_AC_FIND_UNFOCUSED.getImage(), AbstractTnGraphics.TOP | AbstractTnGraphics.HCENTER);
   
        button.setPadding(0, 0, 0, 0);
        TnMenu btnMenu = UiFactory.getInstance().createMenu();
        btnMenu.add("", CMD_SUBMIT);
        button.setMenu(btnMenu, AbstractTnComponent.TYPE_CLICK);
        button.setCommandEventListener(this);
          
        FrogNullField nullfield = UiFactory.getInstance().createNullField(0);
        nullfield.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((AirportUiDecorator)uiDecorator).SEARCH_FIELD_NULLFIELD_WIDTH);
        textFieldContainer.add(nullfield);
        textFieldContainer.add(button);
        topContainer.add(textFieldContainer);
        
        airportScreen.getContentContainer().add(topContainer);
        FrogList list = UiFactory.getInstance().createList(ID_AIRPORT_LIST);
        list.setCommandEventListener(this);
        list.setAdapter(new AirportAdapter(model.getVector(KEY_V_MATCHED_STOPS)));
        airportScreen.getContentContainer().add(list);
        return airportScreen;
    }

    private TnLinearContainer createButtonContainer()
    {
        TnLinearContainer buttonContainer = UiFactory.getInstance().createLinearContainer(ID_AIRPORT_BUTTON_CONTAINER, true,
            AbstractTnGraphics.HCENTER);
        FrogNullField nullField = UiFactory.getInstance().createNullField(0);
        nullField.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((AirportUiDecorator) uiDecorator).NULLFIELD_AIRPORT_INTEVAL_HEIGHT);
        nullField.setBackgroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_WH));
        buttonContainer.add(nullField);
        buttonContainer.setBackgroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_WH));

        FrogButton submitButton = UiFactory.getInstance().createButton(0,
            ResourceManager.getInstance().getCurrentBundle().getString(IStringAc.RES_BTTN_AIRPORTS_SUBMIT, IStringAc.FAMILY_AC));
        submitButton.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((AirportUiDecorator) uiDecorator).BUTTON_SUBMIT_WIDTH);

        TnMenu menu = UiFactory.getInstance().createMenu();
        menu.add("", CMD_SUBMIT);
        submitButton.setMenu(menu, AbstractTnComponent.TYPE_CLICK);
        submitButton.setCommandEventListener(this);
        buttonContainer.add(submitButton);
        nullField = UiFactory.getInstance().createNullField(0);
        nullField.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((AirportUiDecorator) uiDecorator).NULLFIELD_AIRPORT_HEIGHT);
        nullField.setBackgroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_WH));
        buttonContainer.add(nullField);
        return buttonContainer;
    }

    protected boolean updateScreen(int state, TnScreen screen)
    {
        switch (state)
        {
            case STATE_MAIN:
            {
                if (model.getPreState() == STATE_CHOOSE_AIRPORT)
                {
                    CitizenTextField textField = (CitizenTextField) screen.getComponentById(ID_AIRPORT_TEXTFIELD);
                    if (textField != null)
                    {
                        textField.setText("");
                    }
                }
                else
                {
                    Vector searchResults = model.getVector(KEY_V_MATCHED_STOPS);
                    if (searchResults.size() > 0)
                    {
                        FrogList list = (FrogList) screen.getComponentById(ID_AIRPORT_LIST);
                        if (list != null)
                        {
                            list.setAdapter(new AirportAdapter(searchResults));
                        }
                        else
                        {
                            TnLinearContainer container = (TnLinearContainer) screen.getComponentById(ID_AIRPORT_BUTTON_CONTAINER);
                            ((CitizenScreen) screen).getContentContainer().remove(container);
                            list = UiFactory.getInstance().createList(ID_AIRPORT_LIST);
                            list.setAdapter(new AirportAdapter(searchResults));
                            ((CitizenScreen) screen).getContentContainer().add(list);
                        }
                    }
                    else
                    {
                        FrogList list = (FrogList) screen.getComponentById(ID_AIRPORT_LIST);
                        if (list != null)
                        {
                            ((CitizenScreen) screen).getContentContainer().remove(list);
                            ((CitizenScreen) screen).getContentContainer().add(createButtonContainer());
                        }
                    }
                }
                return true;
            }
        }
        return false;
    }

    class AirportKeyWordFilter extends FrogKeywordFilter
    {

        private FrogList list;

        public AirportKeyWordFilter(FrogList list)
        {
            this.list = list;
        }

        protected FilterResults performFilter(FilterRequest constraint)
        {
            FilterResults results = new FilterResults();
            Vector airports = DaoManager.getInstance().getAddressDao()
                    .getAirports(constraint.keyword, MAX_SIZE,(TnLocation) model.get(KEY_O_TNLOCATION));
            results.constraint = constraint.keyword;
            results.values = airports;
            results.count = airports.size();
            return results;
        }

        protected void publishResults(FilterResults results)
        {
            final Vector data = (Vector) results.values;
            ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).runOnUiThread(new Runnable()
            {
                public void run()
                {
                    if (list != null)
                    {
                        list.setAdapter(new AirportAdapter(data));
                        if (getTextField() != null)
                        {
                            if (data.size() > 0)
                                getTextField().showDropDown();
                            else
                                getTextField().hideDropDown();
                        }
                    }
                }
            });
        }
    }

    static class AirportAdapter implements FrogAdapter
    {

        private Vector data;

        public AirportAdapter(Vector data)
        {
            this.data = data;
        }

        public int getItemType(int position)
        {
            return 0;
        }

        public int getCount()
        {
            return data.size();
        }

        public AbstractTnComponent getComponent(int position, AbstractTnComponent convertComponent, AbstractTnComponent parent)
        {
            Stop airport = (Stop) data.elementAt(position);
            String airportCode = AddressDao.getAirportShowLabel(airport);
            Stop airportStop = DaoManager.getInstance().getAddressDao().getAirportByName(airportCode);
            CitizenAddressListItem item ;
            if (convertComponent == null)
            {
                item = UiFactory.getInstance().createCitizenAddressListItem(position);
                item.setGap(5, 0);
                item.setTitleColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_WH), UiStyleManager.getInstance()
                    .getColor(UiStyleManager.TEXT_COLOR_ME_GR));
                item.setForegroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_WH), UiStyleManager.getInstance()
                    .getColor(UiStyleManager.TEXT_COLOR_ME_GR));
                TnMenu menu = UiFactory.getInstance().createMenu();
                menu.add("", CMD_SELECT_AIRPORT);
                item.setMenu(menu, AbstractTnComponent.TYPE_CLICK);
                item.setTitleColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_WH), UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_ME_GR)); 
                item.getTnUiArgs().put(CitizenAddressListItem.KEY_LEFT_ICON_UNFOCUS, ImageDecorator.IMG_LIST_AIRPORT_ICON_UNFOCUSED);
                item.getTnUiArgs().put(CitizenAddressListItem.KEY_LEFT_ICON_FOCUS, ImageDecorator.IMG_LIST_AIRPORT_ICON_FOCUSED);
            } 
            else
            {
                item = (CitizenAddressListItem)convertComponent;
                item.setId(position);
            }  
            
            item.setBoldFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_LIST_DUAL_LINE_TOP));
            item.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_LIST_DUAL_LINE_BOTTOM));
                
            item.setTitle(airportStop.getLabel()); 
            item.setAddress(airportStop.getFirstLine());    
               
            return item;
            
        }

    }

    public CitizenTextField getTextField()
    {
        CitizenScreen currentScreen = (CitizenScreen) ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getCurrentScreen();
        CitizenTextField textField = null;
        if (currentScreen != null)
        {
            textField = (CitizenTextField) currentScreen.getComponentById(ID_AIRPORT_TEXTFIELD);
        }
        return textField;
    }

    public void onTextChange(AbstractTnComponent component, String text)
    {
        int cmd = CMD_SEARCH_AIRPORT;
        this.model.put(KEY_S_AIRPORT, text);
        this.handleViewEvent(cmd);
    }
}
