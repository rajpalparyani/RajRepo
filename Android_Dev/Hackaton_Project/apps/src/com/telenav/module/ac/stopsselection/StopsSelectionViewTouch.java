/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * RecentViewTouch.java
 *
 */
package com.telenav.module.ac.stopsselection;

import java.util.Vector;

import com.telenav.data.datatypes.address.Address;
import com.telenav.data.datatypes.address.Stop;
import com.telenav.log.mis.IMisLogConstants;
import com.telenav.log.mis.MisLogManager;
import com.telenav.log.mis.log.AddressMisLog;
import com.telenav.logger.Logger;
import com.telenav.mvc.AbstractCommonUiDecorator;
import com.telenav.mvc.AbstractCommonView;
import com.telenav.res.IStringAirport;
import com.telenav.res.ResourceManager;
import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.core.TnMenu;
import com.telenav.tnui.core.TnScreen;
import com.telenav.tnui.core.TnUiArgs;
import com.telenav.tnui.core.TnUiEvent;
import com.telenav.tnui.widget.TnPopupContainer;
import com.telenav.ui.NinePatchImageDecorator;
import com.telenav.ui.UiFactory;
import com.telenav.ui.UiStyleManager;
import com.telenav.ui.citizen.CitizenAddressListItem;
import com.telenav.ui.citizen.CitizenScreen;
import com.telenav.ui.frogui.text.FrogTextHelper;
import com.telenav.ui.frogui.widget.FrogAdapter;
import com.telenav.ui.frogui.widget.FrogLabel;
import com.telenav.ui.frogui.widget.FrogList;

/**
 * @author wzhu (wzhu@telenav.cn)
 * @date 2010-12-26
 */
class StopsSelectionViewTouch extends AbstractCommonView implements IStopsSelectionConstants
{

    public StopsSelectionViewTouch(AbstractCommonUiDecorator uiDecorator)
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
        return null;
    }

    protected boolean isShownTransientView(int state)
    {
        switch (state)
        {
            case STATE_CHOOSE_ADDRESS:
            {
                return false;
            }
        }
        return super.isShownTransientView(state);
    }

    protected boolean updatePopup(int state, TnPopupContainer popup)
    {
        return false;
    }

    protected TnScreen createScreen(int state)
    {
        switch (state)
        {
            case STATE_CHOOSE_ADDRESS:
            {
                return createChooseStopScreen(state);
            }
        }
        return null;
    }

    private TnScreen createChooseStopScreen(int state)
    {
        CitizenScreen chooseStopScreen = UiFactory.getInstance().createScreen(state);
        chooseStopScreen.getTitleContainer().setBackgroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_ME_GR));
        FrogLabel label = UiFactory.getInstance().createLabel(
            0,
            FrogTextHelper.BOLD_START + 
            ResourceManager.getInstance().getCurrentBundle()
                    .getString(IStringAirport.RES_LABEL_CHOOSE_AIRPORT_MESSAGE, IStringAirport.FAMILY_AIRPROT)
                    + FrogTextHelper.BOLD_END);
        label.setForegroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_WH), UiStyleManager.getInstance()
                .getColor(UiStyleManager.TEXT_COLOR_WH));
        label.setBoldFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_SCREEN_TITLE));
        chooseStopScreen.getTitleContainer().getTnUiArgs()
                .put(TnUiArgs.KEY_PREFER_HEIGHT, ((StopsSelectionUiDecorator) uiDecorator).CHOOSE_ADDRESS_TITLE_HEIGHT);
        final Vector result = model.getVector(KEY_V_ALTERNATIVE_ADDRESSES);
        FrogList list = UiFactory.getInstance().createList(0);
        list.setAdapter(new FrogAdapter()
        {

            public int getItemType(int position)
            {
                return 0;
            }

            public int getCount()
            {
                return result.size();
            }

            public AbstractTnComponent getComponent(int position, AbstractTnComponent convertComponent, AbstractTnComponent parent)
            {
                Address address = (Address) result.elementAt(position);
                CitizenAddressListItem item;
                if (convertComponent == null)
                {
                    item = UiFactory.getInstance().createCitizenAddressListItem(position);
                    item.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.LIST_ITEM_FOCUSED);
                    item.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, NinePatchImageDecorator.LIST_ITEM_UNFOCUS);
                    item.setTitleColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_ME_GR), UiStyleManager.getInstance()
                        .getColor(UiStyleManager.TEXT_COLOR_ME_GR));
                    item.setForegroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_ME_GR), UiStyleManager.getInstance()
                        .getColor(UiStyleManager.TEXT_COLOR_ME_GR));
                    item.setBoldFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_LIST_SINGLE));
                    item.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_LIST_SINGLE));
                    TnMenu menu = UiFactory.getInstance().createMenu();
                    menu.add("", CMD_SELECT_ADDRESS);
                    item.setMenu(menu, AbstractTnComponent.TYPE_CLICK);
                    item.setCommandEventListener(StopsSelectionViewTouch.this);
                }
                else
                {
                    item = (CitizenAddressListItem)convertComponent;
                    item.setId(position);
                }
                item.setTitle(convertStop(address.getStop()));
                return item;
            }
        });
        chooseStopScreen.getTitleContainer().add(label);
        chooseStopScreen.getContentContainer().add(list);
        chooseStopScreen.getContentContainer().setBackgroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_WH));
        return chooseStopScreen;
    }

    private String convertStop(Stop stop)
    {
        String title;
        switch(stop.getType())
        {
            case Stop.STOP_AIRPORT:
            {
                title = ResourceManager.getInstance().getStringConverter().converAirportLabel(stop.getLabel());
                break;
            }
            default:
            {
                title = ResourceManager.getInstance().getStringConverter().convertAddress(stop, true);
            }
        }
        return title;
    }
    
    protected boolean updateScreen(int state, TnScreen screen)
    {
        return false;
    }

    protected boolean preProcessUIEvent(TnUiEvent tnUiEvent)
    {
        int type = tnUiEvent.getType();
        switch (type)
        {
            case TnUiEvent.TYPE_COMMAND_EVENT:
            {
                if (tnUiEvent.getComponent() instanceof CitizenAddressListItem && tnUiEvent.getCommandEvent().getCommand() == CMD_SELECT_ADDRESS)
                {
                    CitizenAddressListItem item = ((CitizenAddressListItem) tnUiEvent.getComponent());
                    
                    MisLogManager misLogManager = MisLogManager.getInstance();
                 
                    Address address = (Address) model.getVector(KEY_V_ALTERNATIVE_ADDRESSES).elementAt(item.getId());
                    String transactionId = this.model.getString(KEY_S_TRANSACTION_ID);
                    if (transactionId != null && transactionId.length() > 0
                            && misLogManager.getFilter().isTypeEnable(IMisLogConstants.TYPE_TWOBOX_ADDRESS_SELECTION))
                    {
                        AddressMisLog log = misLogManager.getFactory().createTwoBoxAddressSelectionMisLog();
                        if(address != null && address.getStop() != null)
                        {
                            log.setzEndLat(address.getStop().getLat());
                            log.setzEndLon(address.getStop().getLon());
                        }
                        log.setzDisplayString(item.getTitle());
                        log.setTimestamp(System.currentTimeMillis());
                        log.setSearchUid(transactionId);
                        log.setPageNumber(0);
                        log.setPageIndex(item.getId());
                        Logger.log(Logger.INFO, this.getClass().getName(), IMisLogConstants.PROCESS_MISLOG, new Object[]
                        { log });
                    }
                    model.put(KEY_O_SELECTED_ADDRESS, address);
                }
            }
        }
        return super.preProcessUIEvent(tnUiEvent);
    }

}
