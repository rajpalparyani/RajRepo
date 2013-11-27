/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * RimUiEventHandler.java
 *
 */
package com.telenav.tnui.core.rim;

import net.rim.device.api.system.KeypadListener;
import net.rim.device.api.ui.Keypad;
import net.rim.device.api.ui.TouchEvent;

import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.core.TnCommandEvent;
import com.telenav.tnui.core.TnKeyEvent;
import com.telenav.tnui.core.TnMenu;
import com.telenav.tnui.core.TnMotionEvent;
import com.telenav.tnui.core.TnUiEvent;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2010-11-10
 */
public class RimUiEventHandler
{
    public static final int KEY_EVENT_DOWN = 0;
    public static final int KEY_EVENT_REPEAT = 1;
    public static final int KEY_EVENT_UP = 2;
    
    public static final int TRACKBALL_EVENT_CLICK = 0;
    public static final int TRACKBALL_EVENT_UNCLICK = 1;
    public static final int TRACKBALL_EVENT_MOVE = 2;
    
    public static boolean onKeyCodeEvent(AbstractTnComponent component, int eventType, int keyCode, int time)
    {
        int action = TnKeyEvent.ACTION_DOWN;
        int tnKeyCode = getKeyCodeDelegate(keyCode);
        
        switch (eventType)
        {
            case KEY_EVENT_DOWN:
            {
                action = TnKeyEvent.ACTION_DOWN;
                switch (tnKeyCode)
                {
                    case TnKeyEvent.KEYCODE_BACK:
                    {
                        TnMenu menu = component.getMenu(AbstractTnComponent.TYPE_BACK);
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
                                //TODO show Menu
                            }

                            return true;
                        }
                        break;
                    }
                }
                break;
            }
            case KEY_EVENT_REPEAT:
            {
                action = TnKeyEvent.ACTION_REPEAT;
                break;
            }
            case KEY_EVENT_UP:
            {
                action = TnKeyEvent.ACTION_UP;
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
                                //TODO show Menu
                            }

                            return true;
                        }
                        break;
                    }
                }
                break;
            }
        }

        TnKeyEvent keyEvent = new TnKeyEvent(action, tnKeyCode);
        keyEvent.setChar(Keypad.map(keyCode));
        TnUiEvent uiEvent = new TnUiEvent(TnUiEvent.TYPE_KEY_EVENT, component);
        uiEvent.setKeyEvent(keyEvent);

        return component.dispatchUiEvent(uiEvent);
    }
    
    public static boolean onTrackballEvent(AbstractTnComponent component, int eventType, int dx, int dy, int status, int time)
    {
        switch (eventType)
        {
            case TRACKBALL_EVENT_CLICK:
            {
                break;
            }
            case TRACKBALL_EVENT_UNCLICK:
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
                        //TODO show Menu
                    }

                    return true;
                }
                break;
            }
            case TRACKBALL_EVENT_MOVE:
            {
                TnUiEvent uiEvent = new TnUiEvent(TnUiEvent.TYPE_TOUCH_EVENT, component);
                int tnAction = TnMotionEvent.ACTION_MOVE;
                TnMotionEvent tnMotionEvent = new TnMotionEvent(tnAction);
                tnMotionEvent.setLocation(dx, dy);
                tnMotionEvent.setEventTime(time);
                uiEvent.setMotionEvent(tnMotionEvent);
                
                return component.dispatchUiEvent(uiEvent);
            }
        }
        return false;
    }
    
    public static boolean onTouchEvent(AbstractTnComponent component, TouchEvent event)
    {
        return false;
    }
    
    private static int getKeyCodeDelegate(int keyCode)
    {
        int nativeKey = Keypad.key(keyCode);
        char c = Keypad.map(keyCode);
        char altChar = Keypad.map(nativeKey, KeypadListener.STATUS_ALT);
        int key = -1;

        switch (nativeKey)
        {
            case Keypad.KEY_ESCAPE:
            {
                key = TnKeyEvent.KEYCODE_BACK;
                break;
            }
            case Keypad.KEY_MENU:
            {
                key = TnKeyEvent.KEYCODE_MENU;
                break;
            }
            case Keypad.KEY_ENTER:
            {
                key = TnKeyEvent.KEYCODE_ENTER;
                break;
            }
            case Keypad.KEY_BACKSPACE:
            {
                key = TnKeyEvent.KEYCODE_DEL;
                break;
            }
            case Keypad.KEY_SPACE:
            {
                key = TnKeyEvent.KEYCODE_SPACE;
                break;
            }
            case Keypad.KEY_SEND:
            {
                key = TnKeyEvent.KEYCODE_CALL;
                break;
            }
            case Keypad.KEY_END:
            {
                key = TnKeyEvent.KEYCODE_ENDCALL;
                break;
            }
        }
        
        if(key == -1)
        {
            if (c == '0' || altChar == '0')
            {
                key = TnKeyEvent.KEYCODE_0;
            }
            else if (c == '1' || altChar == '1')
            {
                key = TnKeyEvent.KEYCODE_1;
            }
            else if (c == '2' || altChar == '2')
            {
                key = TnKeyEvent.KEYCODE_2;
            }
            else if (c == '3' || altChar == '3')
            {
                key = TnKeyEvent.KEYCODE_3;
            }
            else if (c == '4' || altChar == '4')
            {
                key = TnKeyEvent.KEYCODE_4;
            }
            else if (c == '5' || altChar == '5')
            {
                key = TnKeyEvent.KEYCODE_5;
            }
            else if (c == '6' || altChar == '6')
            {
                key = TnKeyEvent.KEYCODE_6;
            }
            else if (c == '7' || altChar == '7')
            {
                key = TnKeyEvent.KEYCODE_7;
            }
            else if (c == '8' || altChar == '8')
            {
                key = TnKeyEvent.KEYCODE_8;
            }
            else if (c == '9' || altChar == '9')
            {
                key = TnKeyEvent.KEYCODE_9;
            }
            else if (c == '#' || altChar == '#')
            {
                key = TnKeyEvent.KEYCODE_POUND;
            }
            else if (c == '*' || altChar == '*')
            {
                key = TnKeyEvent.KEYCODE_STAR;
            }
        }
        
        return key;
    }
}
