/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * AbstractBaseView.java
 *
 */
package com.telenav.mvc;

import java.util.Stack;

import com.telenav.app.TeleNavDelegate;
import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.dao.misc.SimpleConfigDao;
import com.telenav.dwf.aidl.DwfAidl;
import com.telenav.dwf.aidl.DwfServiceConnection;
import com.telenav.dwf.aidl.Friend.FriendStatus;
import com.telenav.module.dwf.DwfSliderPopup;
import com.telenav.module.nav.NavRunningStatusProvider;
import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.core.AbstractTnUiHelper;
import com.telenav.tnui.core.ITnSizeChangListener;
import com.telenav.tnui.core.ITnUiEventListener;
import com.telenav.tnui.core.TnKeyEvent;
import com.telenav.tnui.core.TnMenu;
import com.telenav.tnui.core.TnScreen;
import com.telenav.tnui.core.TnUiEvent;
import com.telenav.tnui.widget.TnPopupContainer;
import com.telenav.ui.citizen.CitizenScreen;

/**
 *@author fqming (fqming@telenav.cn)
 *@date Aug 26, 2010
 */
public abstract class AbstractBaseView extends AbstractView implements ITnUiEventListener, ICommonConstants, ITnSizeChangListener
{
    protected AbstractCommonUiDecorator uiDecorator;
    
    //below two variables should be private.
    private Stack screenStack;
    private static TnPopupContainer popupContainer;
    
    AbstractBaseView(AbstractCommonUiDecorator uiDecorator)
    {
        this.uiDecorator = uiDecorator;
        this.screenStack = new Stack();
    }
    
    public boolean handleUiEvent(TnUiEvent tnUiEvent)
    {
        int state = model.getState();
        if (popupContainer != null && popupContainer.isVisible() && tnUiEvent.getType() == TnUiEvent.TYPE_KEY_EVENT
                && tnUiEvent.getKeyEvent().getCode() == TnKeyEvent.KEYCODE_SEARCH)
        {
            return true;
        }
        if (preProcessUIEvent(tnUiEvent))
        {
            return true;
        }
        
        switch(tnUiEvent.getType())
        {
            case TnUiEvent.TYPE_COMMAND_EVENT:
            {
                if(tnUiEvent.getCommandEvent() != null)
                {
                    int command = tnUiEvent.getCommandEvent().getCommand();
                    
                    //check it here is to avoid state machine error.
                    if (command == ICommonConstants.CMD_COMMON_EXIT 
                            && !NavRunningStatusProvider.getInstance().isNavRunning())
                    {
                        
                        DwfAidl dwfAidl = DwfServiceConnection.getInstance().getConnection();

                        if (dwfAidl != null)
                        {
                            try
                            {
                                dwfAidl.updateStatus(-1, FriendStatus.END.name(), 0, 0);
                                dwfAidl.stopShareLocation();
                                DwfSliderPopup.getInstance().hide();
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        }
                        
                        TeleNavDelegate.getInstance().exitApp();
                    }
                    else if(command == ICommonConstants.CMD_COMMON_SWITCH_AIRPLANE)
                    {
                        TeleNavDelegate.getInstance().callAppNativeFeature(TeleNavDelegate.SWTICH_AIRPLANE_MODE, null);
                        CitizenScreen currentScreen = (CitizenScreen) ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getCurrentScreen();
                        TnMenu screenMenu = currentScreen.getRootContainer().getMenu(
                            AbstractTnComponent.TYPE_MENU);
                        screenMenu.remove(ICommonConstants.CMD_COMMON_SWITCH_AIRPLANE);
                        if (((DaoManager) DaoManager.getInstance()).getSimpleConfigDao().getBoolean(
                            SimpleConfigDao.KEY_SWITCH_AIRPLANE_MODE_ENABLE))
                        {
                            screenMenu.add(TeleNavDelegate.IS_START_CRAZY_SWITCH ? TeleNavDelegate.SWITCH_AIRPLANE_OFF : TeleNavDelegate.SWITCH_AIRPLANE_ON, 
                                    ICommonConstants.CMD_COMMON_SWITCH_AIRPLANE);
                        }
                    }
                    else
                    {
                        return this.handleViewEvent(command);
                    }
                }
                break;
            }
            default:
            {
                int commandId = transformCommand(state, tnUiEvent);
                return this.handleViewEvent(commandId);
            }
        }
        return false;
    }
    
    protected boolean preProcessUIEvent(TnUiEvent tnUiEvent)
    {
        return false;
    }

    /**
     * Get other command id besides IUIEvent.TYPE_COMMAND. <br>
     * 
     * For example, if we hold on "F" we will transform this UI event to CMD_COMMON_FEEDBACK.<br>
     * 
     * We should override this method according application needs.<br>
     * 
     * @param state
     * @param event
     * @return commandId
     */
    private final int transformCommand(int state, TnUiEvent tnUiEvent)
    {
        int cmdId = transformBaseCommandDelegate(state, tnUiEvent);
        
        if(cmdId == ICommonConstants.CMD_NONE)
        {
            if(tnUiEvent.getKeyEvent() != null && tnUiEvent.getKeyEvent().getAction() == TnKeyEvent.ACTION_DOWN && tnUiEvent.getKeyEvent().getCode()== TnKeyEvent.KEYCODE_BACK)
            {
                cmdId = ICommonConstants.CMD_COMMON_BACK;
            }
        }
        
        return cmdId;
    }
    
    protected abstract int transformBaseCommandDelegate(int state, TnUiEvent tnUiEvent);
    
    protected abstract TnPopupContainer createPopupDelegate(int state);
    
    protected abstract boolean updatePopup(int state, TnPopupContainer popup);
    
    protected abstract TnScreen createScreenDelegate(int state);
    
    protected abstract boolean updateScreenDelegate(int state, TnScreen screen);
    
    protected final boolean showTransientView(int state)
    {
        TnPopupContainer popup = null;
        
        if (popupContainer == null || popupContainer.getId() != state)
        {
            popup = createPopupDelegate(state);
            if (popup != null)
            {   
                popup.setId(state);
                popup.setCommandEventListener(this);
                popup.getContent().setCommandEventListener(this);
                popup.setKeyEventListener(this);
            }
        }
        else if(popupContainer.getId() == state)
        {
            popup = popupContainer;
            updatePopup(state, popup);
        }

        if(popup == null)
        {
            afterShowTransientView(state);
            
            return false;
        }
        
        if(model.isActivated())
        {
            if(popupContainer == null || !popupContainer.equals(popup) || !popup.isVisible())
            {
                this.hidePopup();
                popupContainer = popup; 
                popup.show();
            }
        }
        
        afterShowTransientView(state);
        
        return true;
    }
    
    protected void afterShowTransientView(int state)
    {
        
    }
    
    protected final boolean showView(int state)
    {
    	TnScreen previousScreen = ((AbstractTnUiHelper)AbstractTnUiHelper.getInstance()).getCurrentScreen();
    	
        TnScreen screen = (TnScreen)lookup(state);
        boolean needRepainted = false;
        
        if (screen == null)
        {
            screen = createScreenDelegate(state);
            if (screen != null)
            {
                screen.setId(state);
                screen.getRootContainer().setCommandEventListener(this);
                screen.getRootContainer().setKeyEventListener(this);
                screen.getRootContainer().setSizeChangeListener(this);
                if (!isTransientState(state))// we should not store transient state into memory
                {
                    this.screenStack.push(screen);
                }
            }
        }
        else
        {
            // already exist, update screen
            needRepainted = updateScreenDelegate(state, screen);
        }

        if (screen == null)
        {
            return false;
        }
        
        if(model.isActivated())
        {
            this.hidePopup();

            if(screen == previousScreen)
            {
                if(needRepainted)
                {
                    screen.getRootContainer().requestPaint();
                }
            }
            else
            {   
                screen.show();
            }
        }
        
        return true;
    }

    public TnScreen getScreenByState(int state)
    {
        int i;
        Object object = null;
        
        int size = this.screenStack.size();
        for (i = size - 1; i >= 0; i--)
        {
            Object tmpObject = this.screenStack.elementAt(i);
            if(tmpObject instanceof TnScreen)
            {
                if(((TnScreen)tmpObject).getId() == state)
                {
                    object = tmpObject;
                    break;
                }
            }
            if(tmpObject instanceof TnPopupContainer)
            {
                if(((TnPopupContainer)tmpObject).getId() == state)
                {
                    object = tmpObject;
                    break;
                }
            }
        }
        
        if(object instanceof TnScreen)
        {
            return (TnScreen)object;
        }
        
        return null;
    }
    
    protected Object lookup(int state)
    {
        int i;
        Object object = null;

        int size = this.screenStack.size();
        for (i = size - 1; i >= 0; i--)
        {
            Object tmpObject = this.screenStack.elementAt(i);
            if(tmpObject instanceof TnScreen)
            {
                if(((TnScreen)tmpObject).getId() == state)
                {
                    object = tmpObject;
                    break;
                }
            }
            if(tmpObject instanceof TnPopupContainer)
            {
                if(((TnPopupContainer)tmpObject).getId() == state)
                {
                    object = tmpObject;
                    break;
                }
            }
        }

        // remove all screens after this screen, both screenStack and stateScreens
        if (object != null)
        {
            for (int x = i + 1; x < size; x++)
            {
                this.screenStack.pop();
            }
        }

        return object;
    }
    
    protected void popAllViews()
    {
        this.screenStack.removeAllElements();
        this.hidePopup();
    }
    
    public void onSizeChanged(AbstractTnComponent tnComponent, int w, int h, int oldw, int oldh)
    {
        
    }
    
    protected TnPopupContainer getCurrentPopup()
    {
        return popupContainer;
    }
    
    private void hidePopup()
    {
        if(popupContainer != null)
        {
            ((TnPopupContainer) popupContainer).hide();
            popupContainer = null;
        }
    }

    protected void deactivateDelegate()
    {
        super.deactivateDelegate();
        ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).closeContextMenu();
        ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).closeOptionsMenu();
    }
}
