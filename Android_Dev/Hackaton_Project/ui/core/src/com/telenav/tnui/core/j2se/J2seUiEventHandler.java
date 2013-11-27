/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * J2seUiEventHandler.java
 *
 */
package com.telenav.tnui.core.j2se;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.core.TnCommandEvent;
import com.telenav.tnui.core.TnKeyEvent;
import com.telenav.tnui.core.TnMenu;
import com.telenav.tnui.core.TnUiEvent;

/**
 *@author fqming (fqming@telenav.cn)
 *@date Aug 3, 2010
 */
public class J2seUiEventHandler
{
    /**
     * handle click event.
     * 
     * @param component the owner of this event.
     */
    public static void onClick(AbstractTnComponent component)
    {
        TnMenu menu = component.getMenu(AbstractTnComponent.TYPE_CLICK);
        if(menu != null)
        {
            if(menu.size() == 1)
            {
                TnCommandEvent commandEvent = new TnCommandEvent(menu.getItem(0).getId());
                TnUiEvent uiEvent = new TnUiEvent(TnUiEvent.TYPE_COMMAND_EVENT, component);
                uiEvent.setCommandEvent(commandEvent);
                
                component.dispatchUiEvent(uiEvent);
            }
            else if(menu.size() > 1)
            {
            }
        }
        
    }

    /**
     * handle touch event.
     * 
     * @param component the owner of this event.
     * @param event the ui event.
     * @return if handle this event.
     */
    public static boolean onTouch(AbstractTnComponent component, MouseEvent event)
    {
        return false;
    }
    
    /**
     * handle key down event.
     * 
     * @param component the owner of this event.
     * @param keyCode the key code of this event.
     * @param event the key event.
     * @return if handle this event.
     */
    public static boolean onKeyDown(AbstractTnComponent component, KeyEvent event)
    {
        int action = TnKeyEvent.ACTION_DOWN;
        
        if ((System.currentTimeMillis() - event.getWhen()) > 1000)
        {
            action = TnKeyEvent.ACTION_REPEAT;
        }
        
        int tnKeyCode = getKeyCodeDelegate(event.getKeyCode());
        
        switch(tnKeyCode)
        {
            case TnKeyEvent.KEYCODE_BACK:
            {
                TnMenu menu = component.getMenu(AbstractTnComponent.TYPE_BACK);
                if(menu != null)
                {
                    if(menu.size() == 1)
                    {
                        TnCommandEvent commandEvent = new TnCommandEvent(menu.getItem(0).getId());
                        TnUiEvent uiEvent = new TnUiEvent(TnUiEvent.TYPE_COMMAND_EVENT, component);
                        uiEvent.setCommandEvent(commandEvent);
                        
                        return component.dispatchUiEvent(uiEvent);
                    }
                    else if(menu.size() > 1)
                    {
                        //TODO
                    }
                    
                    return true;
                }
                break;
            }
        }
        
        TnKeyEvent keyEvent = new TnKeyEvent(action, tnKeyCode);
        keyEvent.setChar(event.getKeyChar());
        TnUiEvent uiEvent = new TnUiEvent(TnUiEvent.TYPE_KEY_EVENT, component);
        uiEvent.setKeyEvent(keyEvent);
        
        return component.dispatchUiEvent(uiEvent);
    }
    
    /**
     * handle key up event.
     * 
     * @param component the owner of this event.
     * @param keyCode the key code of this event.
     * @param event the key event.
     * @return if handle this event.
     */
    public static boolean onKeyUp(AbstractTnComponent component, KeyEvent event)
    {
        int action = TnKeyEvent.ACTION_UP;

        int tnKeyCode = getKeyCodeDelegate(event.getKeyCode());

        switch (tnKeyCode)
        {
            case TnKeyEvent.KEYCODE_ENTER:
            {
                TnMenu menu = component.getMenu(AbstractTnComponent.TYPE_CLICK);
                if (menu != null)
                {
                    if (menu.size() == 1)
                    {
                        TnCommandEvent commandEvent = new TnCommandEvent(menu.getItem(0).getId());
                        TnUiEvent uiEvent = new TnUiEvent(TnUiEvent.TYPE_COMMAND_EVENT, component);
                        uiEvent.setCommandEvent(commandEvent);

                        return component.dispatchUiEvent(uiEvent);
                    }
                    else if (menu.size() > 1)
                    {
                        //TODO
//                        ((View) component.getNativeUiComponent()).showContextMenu();
                    }

                    return true;
                }
                break;
            }
        }

        TnKeyEvent keyEvent = new TnKeyEvent(action, tnKeyCode);
        keyEvent.setChar(event.getKeyChar());
        TnUiEvent uiEvent = new TnUiEvent(TnUiEvent.TYPE_KEY_EVENT, component);
        uiEvent.setKeyEvent(keyEvent);

        return component.dispatchUiEvent(uiEvent);
    }

    private static int getKeyCodeDelegate(int keyCode)
    {
        int key = keyCode;

        switch (keyCode)
        {
            case KeyEvent.VK_LEFT:
            {
                key = TnKeyEvent.KEYCODE_PAD_LEFT;
                break;
            }
            case KeyEvent.VK_RIGHT:
            {
                key = TnKeyEvent.KEYCODE_PAD_RIGHT;
                break;
            }
            case KeyEvent.VK_UP:
            {
                key = TnKeyEvent.KEYCODE_PAD_UP;
                break;
            }
            case KeyEvent.VK_DOWN:
            {
                key = TnKeyEvent.KEYCODE_PAD_DOWN;
                break;
            }
            case KeyEvent.VK_ESCAPE:
            {
                key = TnKeyEvent.KEYCODE_BACK;
                break;
            }
            case KeyEvent.VK_F2:
            {
                key = TnKeyEvent.KEYCODE_MENU;
                break;
            }
            case KeyEvent.VK_ENTER:
            {
                key = TnKeyEvent.KEYCODE_ENTER;
                break;
            }
            case KeyEvent.VK_F3:
            {
                key = TnKeyEvent.KEYCODE_SEARCH;
                break;
            }
            case KeyEvent.VK_0:
            {
                key = TnKeyEvent.KEYCODE_0;
                break;
            }
            case KeyEvent.VK_1:
            {
                key = TnKeyEvent.KEYCODE_1;
                break;
            }
            case KeyEvent.VK_2:
            {
                key = TnKeyEvent.KEYCODE_2;
                break;
            }
            case KeyEvent.VK_3:
            {
                key = TnKeyEvent.KEYCODE_3;
                break;
            }
            case KeyEvent.VK_4:
            {
                key = TnKeyEvent.KEYCODE_4;
                break;
            }
            case KeyEvent.VK_5:
            {
                key = TnKeyEvent.KEYCODE_5;
                break;
            }
            case KeyEvent.VK_6:
            {
                key = TnKeyEvent.KEYCODE_6;
                break;
            }
            case KeyEvent.VK_7:
            {
                key = TnKeyEvent.KEYCODE_7;
                break;
            }
            case KeyEvent.VK_8:
            {
                key = TnKeyEvent.KEYCODE_8;
                break;
            }
            case KeyEvent.VK_9:
            {
                key = TnKeyEvent.KEYCODE_9;
                break;
            }
            case KeyEvent.VK_DELETE:
            {
                key = TnKeyEvent.KEYCODE_DEL;
                break;
            }
            case KeyEvent.VK_SPACE:
            {
                key = TnKeyEvent.KEYCODE_SPACE;
                break;
            }
            case KeyEvent.VK_F5:
            {
                key = TnKeyEvent.KEYCODE_POUND;
                break;
            }
            case KeyEvent.VK_F6:
            {
                key = TnKeyEvent.KEYCODE_STAR;
                break;
            }
            case KeyEvent.VK_F7:
            {
                key = TnKeyEvent.KEYCODE_CALL;
                break;
            }
            case KeyEvent.VK_F8:
            {
                key = TnKeyEvent.KEYCODE_ENDCALL;
                break;
            }
        }
        return key;
    }
}
