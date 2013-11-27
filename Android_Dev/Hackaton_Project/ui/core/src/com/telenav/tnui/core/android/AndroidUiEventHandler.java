/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * AndroidUiEventHandler.java
 *
 */
package com.telenav.tnui.core.android;

import android.view.ContextMenu;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import com.telenav.logger.Logger;
import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.core.ITnSizeChangListener;
import com.telenav.tnui.core.TnCommandEvent;
import com.telenav.tnui.core.TnKeyEvent;
import com.telenav.tnui.core.TnMenu;
import com.telenav.tnui.core.TnMenuItem;
import com.telenav.tnui.core.TnMotionEvent;
import com.telenav.tnui.core.TnUiEvent;
import com.telenav.tnui.core.android.AndroidBitmapDrawable;

/**
 * The ui event handler at android platform. for all ui events, we should pass to this class at android platform.
 * 
 *@author fqming (fqming@telenav.cn)
 *@date 2010-6-29
 */
public class AndroidUiEventHandler
{
    private final static int MENU_TYPE = -1001111111;
    
    private final static int SCALE_GESTURE_BEGIN = 0;
    private final static int SCALE_GESTURE_ON = 1;
    private final static int SCALE_GESTURE_END = 2;
    
    private static MotionEvent lastMotionEvent;
    
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
                ((View)component.getNativeUiComponent()).setTag(MENU_TYPE, new Integer(AbstractTnComponent.TYPE_CLICK));
                ((View)component.getNativeUiComponent()).showContextMenu();
            }
        }
        
    }
    
    /**
     * handle long click event.
     * 
     * @param component the owner of this event.
     * @return true is handled, otherwise false.
     */
    public static boolean onLongClick(AbstractTnComponent component)
    {
        TnUiEvent uiEvent = new TnUiEvent(TnUiEvent.TYPE_TOUCH_EVENT, component);
        TnMotionEvent motionEvent = new TnMotionEvent(TnMotionEvent.ACTION_LONG_TOUCH);
        motionEvent.setLocation(lastMotionEvent != null ? (int)lastMotionEvent.getX() : 0, lastMotionEvent != null ? (int)lastMotionEvent.getY() : 0);
        uiEvent.setMotionEvent(motionEvent);
        component.dispatchUiEvent(uiEvent);
        
        TnMenu menu = component.getMenu(AbstractTnComponent.TYPE_CONTEXT);
        if(menu != null && menu.size() > 0)
        {
            ((View)component.getNativeUiComponent()).setTag(MENU_TYPE, new Integer(AbstractTnComponent.TYPE_CONTEXT));
            ((View)component.getNativeUiComponent()).showContextMenu();
            
            return true;
        }
        
        return false;
    }
    
    /**
     * called when landscape/portrait switch.
     * 
     * @param component rootContainer.
     * @return
     */
    public static boolean onPreSizeChanged(AbstractTnComponent component)
    {
        ITnSizeChangListener sizeChangeListener = component.getSizeChangeListener();
        if(sizeChangeListener != null)
        {
            sizeChangeListener.onSizeChanged(component, component.getPreferredWidth(), component.getPreferredHeight(), component.getWidth(), component.getHeight());
            return true;
        }
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
    public static boolean onKeyDown(AbstractTnComponent component, int keyCode, KeyEvent event)
    {
        int action = TnKeyEvent.ACTION_DOWN;
        
        if (event.getRepeatCount() > 0)
        {
            action = TnKeyEvent.ACTION_REPEAT;
        }
        
        int tnKeyCode = getKeyCodeDelegate(keyCode);
        
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
                        ((View)component.getNativeUiComponent()).setTag(MENU_TYPE, new Integer(AbstractTnComponent.TYPE_BACK));
                        ((View)component.getNativeUiComponent()).showContextMenu();
                    }
                    
                    return true;
                }
                break;
            }
        }
        
        TnKeyEvent keyEvent = new TnKeyEvent(action, tnKeyCode);
        keyEvent.setChar(getkeyChar(event.getDeviceId(), keyCode));
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
    public static boolean onKeyUp(AbstractTnComponent component, int keyCode, KeyEvent event)
    {
        int action = TnKeyEvent.ACTION_UP;

        int tnKeyCode = getKeyCodeDelegate(keyCode);

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
                        ((View)component.getNativeUiComponent()).setTag(MENU_TYPE, new Integer(AbstractTnComponent.TYPE_CLICK));
                        ((View) component.getNativeUiComponent()).showContextMenu();
                    }

                    return true;
                }
                break;
            }
        }

        TnKeyEvent keyEvent = new TnKeyEvent(action, tnKeyCode);
        keyEvent.setChar(getkeyChar(event.getDeviceId(), keyCode));
        TnUiEvent uiEvent = new TnUiEvent(TnUiEvent.TYPE_KEY_EVENT, component);
        uiEvent.setKeyEvent(keyEvent);

        return component.dispatchUiEvent(uiEvent);
    }
    
    public static boolean scaleGestureEventConverter(AbstractTnComponent component, ScaleGestureDetector detector, TnUiEvent uiEvent, int scaleGestureType)
    {
        int tnAction = TnMotionEvent.ACTION_DOWN;
        switch(scaleGestureType)
        {
            case SCALE_GESTURE_BEGIN:
            {
                tnAction = TnMotionEvent.ACTION_BEGIN_SCALE;
                break;
            }
            case SCALE_GESTURE_ON:
            {
                tnAction = TnMotionEvent.ACTION_ON_SCALE;
                break;
            }
            case SCALE_GESTURE_END:
            {
                tnAction = TnMotionEvent.ACTION_END_SCALE;
                break;
            }
            default:
            {
                break;
            }
        }
        
        TnMotionEvent tnMotionEvent = new TnMotionEvent(tnAction);
        tnMotionEvent.setIsScaleGesture(true);
        tnMotionEvent.setPointerCount(2);
        tnMotionEvent.setLocation((int)detector.getFocusX(), (int)detector.getFocusY());
        tnMotionEvent.setDistance((int)detector.getCurrentSpan());
        tnMotionEvent.setEventTime(detector.getEventTime());
        uiEvent.setMotionEvent(tnMotionEvent);
        component.dispatchUiEvent(uiEvent);
        //if return false, onscale and onscale end will never be invoked.
        return true;
    }
    
    public static boolean onScaleBegin(AbstractTnComponent component, ScaleGestureDetector detector)
    {
        TnUiEvent uiEvent = new TnUiEvent(TnUiEvent.TYPE_GESTURE_SCALE, component);
        return scaleGestureEventConverter(component, detector, uiEvent, SCALE_GESTURE_BEGIN);    
    }
    
    public static boolean onScale(AbstractTnComponent component, ScaleGestureDetector detector)
    {
        TnUiEvent uiEvent = new TnUiEvent(TnUiEvent.TYPE_GESTURE_SCALE, component);
        return scaleGestureEventConverter(component, detector, uiEvent, SCALE_GESTURE_ON);
    }
    
    public static boolean onScaleEnd(AbstractTnComponent component, ScaleGestureDetector detector)
    {
        TnUiEvent uiEvent = new TnUiEvent(TnUiEvent.TYPE_GESTURE_SCALE, component);
        return scaleGestureEventConverter(component, detector, uiEvent, SCALE_GESTURE_END);
    }

    /**
     * handle touch event.
     * 
     * @param component the owner of this event.
     * @param event the ui event.
     * @return if handle this event.
     */
    public static boolean onTouch(AbstractTnComponent component, MotionEvent event)
    {
        //for the android sdk later than 3.2 , the touchX and touchY of native event will be changed at some time,
        //it will cause the touchX and touchY of tnMotionEvent changed too.
        //clone the event to lastmotionevent.
        lastMotionEvent = MotionEvent.obtain(event);
        
        TnUiEvent uiEvent = new TnUiEvent(TnUiEvent.TYPE_TOUCH_EVENT, component);
        int tnAction = TnMotionEvent.ACTION_DOWN;
        
        int action = 0 ;
        
        if(android.os.Build.VERSION.SDK_INT < 8)
        {
            action = event.getAction();
        }
        else
        {
            action = event.getActionMasked();
        }
        
        switch (action)
        {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                tnAction = TnMotionEvent.ACTION_DOWN;
                break;
            case MotionEvent.ACTION_MOVE:
                tnAction = TnMotionEvent.ACTION_MOVE;
                break;
            case MotionEvent.ACTION_CANCEL:
                tnAction = TnMotionEvent.ACTION_CANCEL;
                break;
            case MotionEvent.ACTION_OUTSIDE:
                tnAction = TnMotionEvent.ACTION_OUTSIDE;
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                tnAction = TnMotionEvent.ACTION_UP;
                break;
        }
        
        TnMotionEvent tnMotionEvent = new TnMotionEvent(tnAction);
        tnMotionEvent.setIsScaleGesture(false);
        //for lawsuit issue, we should use gesture to get span.
        //for touch event, all the distance between 2 fingers are 0.
        tnMotionEvent.setDistance(0);
        tnMotionEvent.setPointerCount(event.getPointerCount());
        for(int i = 0; i < event.getPointerCount(); i++)
        {
            tnMotionEvent.setLocation((int)event.getX(i), (int)event.getY(i), i);
        }

        tnMotionEvent.setEventTime(event.getEventTime());
        uiEvent.setMotionEvent(tnMotionEvent);
        
        return component.dispatchUiEvent(uiEvent);
    }
    
    /**
     * handle trackball event.
     * 
     * @param component the owner of this event.
     * @param event the ui event.
     * @return if handle this event.
     */
    public static boolean onTrackball(AbstractTnComponent component, MotionEvent event)
    {
        TnUiEvent uiEvent = new TnUiEvent(TnUiEvent.TYPE_TOUCH_EVENT, component);
        int tnAction = TnMotionEvent.ACTION_MOVE;
        TnMotionEvent tnMotionEvent = new TnMotionEvent(tnAction);
        tnMotionEvent.setLocation((int) event.getX(), (int) event.getY());
        tnMotionEvent.setEventTime(event.getEventTime());
        uiEvent.setMotionEvent(tnMotionEvent);

        return component.dispatchUiEvent(uiEvent);
    }
    
    /**
     * Initialize the contents of the Activity's standard options menu.  You
     * should place your menu items in to menu.
     * 
     * @param component the owner of this event.
     * @param native menu
     * @return true is handled, otherwise false.
     */
    public static boolean onCreateOptionsMenu(AbstractTnComponent component, Menu menu)
    {
        TnMenu optionMenu = component.getMenu(AbstractTnComponent.TYPE_MENU);
        return createMenu(optionMenu, menu);
    }
    
    /**
     * This hook is called whenever an item in your options menu is selected.
     * The default implementation simply returns false to have the normal
     * processing happen (calling the item's Runnable or sending a message to
     * its Handler as appropriate).  You can use this method for any items
     * for which you would like to do processing without those other
     * facilities.
     * 
     * @param component the owner of this event.
     * @param item select item
     * @return true is handled, otherwise false.
     */
    public static boolean onOptionsItemSelected(AbstractTnComponent component, MenuItem item)
    {
        TnCommandEvent commandEvent = new TnCommandEvent(item.getItemId());
        TnUiEvent uiEvent = new TnUiEvent(TnUiEvent.TYPE_COMMAND_EVENT, component);
        uiEvent.setCommandEvent(commandEvent);

        return component.dispatchUiEvent(uiEvent);
    }
    
    /**
     * Initialize the contents of the Activity's standard context menu.  You
     * should place your menu items in to menu.
     * 
     * @param component the owner of this event.
     * @param native menu
     * @return true is handled, otherwise false.
     */
    public static boolean onCreateContextMenu(AbstractTnComponent component, Menu menu)
    {
        Object o = ((View)component.getNativeUiComponent()).getTag(MENU_TYPE);
        if(!(o instanceof Integer))
            return false;
        
        int type = ((Integer)o).intValue();
        
        ((View)component.getNativeUiComponent()).setTag(MENU_TYPE, new Object());
        TnMenu optionMenu = component.getMenu(type);
        return createMenu(optionMenu, menu);
    }
    
    public static boolean createMenu(TnMenu optionMenu, Menu menu)
    {
        if (optionMenu != null && optionMenu.size() > 0)
        {
            if(menu instanceof ContextMenu)
            {
                ContextMenu contextMenu = (ContextMenu)menu;
                if(optionMenu.getHeaderIcon() != null)
                {
                    contextMenu.setHeaderIcon(new AndroidBitmapDrawable(optionMenu.getHeaderIcon()));
                }
                if(optionMenu.getHeaderTitle() != null)
                {
                    contextMenu.setHeaderTitle(optionMenu.getHeaderTitle());
                }
            }
            
            for(int i = 0; i < optionMenu.size(); i++)
            {
                TnMenuItem tnItem = optionMenu.getItem(i);
                MenuItem item = menu.add(0, tnItem.getId(), i, tnItem.getTitle());
                if(tnItem.getIcon() != null)
                {
                    item.setIcon(new AndroidBitmapDrawable(tnItem.getIcon()));
                }
            }
            return true;
        }
        return false;
    }
    
    private static char[] chars = new char[]
    { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', ' ' };

    private static char getkeyChar(int keyboard, int keycode)
    {
        return KeyCharacterMap.load(keyboard).getMatch(keycode, chars);
    }

    private static int getKeyCodeDelegate(int keyCode)
    {
        int key = -1;

        switch (keyCode)
        {
            case KeyEvent.KEYCODE_DPAD_LEFT:
            {
                key = TnKeyEvent.KEYCODE_PAD_LEFT;
                break;
            }
            case KeyEvent.KEYCODE_DPAD_RIGHT:
            {
                key = TnKeyEvent.KEYCODE_PAD_RIGHT;
                break;
            }
            case KeyEvent.KEYCODE_DPAD_UP:
            {
                key = TnKeyEvent.KEYCODE_PAD_UP;
                break;
            }
            case KeyEvent.KEYCODE_DPAD_DOWN:
            {
                key = TnKeyEvent.KEYCODE_PAD_DOWN;
                break;
            }
            case KeyEvent.KEYCODE_DPAD_CENTER:
            {
                key = TnKeyEvent.KEYCODE_PAD_CENTER;
                break;
            }
            case KeyEvent.KEYCODE_BACK:
            {
                key = TnKeyEvent.KEYCODE_BACK;
                break;
            }
            case KeyEvent.KEYCODE_MENU:
            {
                key = TnKeyEvent.KEYCODE_MENU;
                break;
            }
            case KeyEvent.KEYCODE_ENTER:
            {
                key = TnKeyEvent.KEYCODE_ENTER;
                break;
            }
            case KeyEvent.KEYCODE_SEARCH:
            {
                key = TnKeyEvent.KEYCODE_SEARCH;
                break;
            }
            case KeyEvent.KEYCODE_0:
            {
                key = TnKeyEvent.KEYCODE_0;
                break;
            }
            case KeyEvent.KEYCODE_1:
            {
                key = TnKeyEvent.KEYCODE_1;
                break;
            }
            case KeyEvent.KEYCODE_2:
            {
                key = TnKeyEvent.KEYCODE_2;
                break;
            }
            case KeyEvent.KEYCODE_3:
            {
                key = TnKeyEvent.KEYCODE_3;
                break;
            }
            case KeyEvent.KEYCODE_4:
            {
                key = TnKeyEvent.KEYCODE_4;
                break;
            }
            case KeyEvent.KEYCODE_5:
            {
                key = TnKeyEvent.KEYCODE_5;
                break;
            }
            case KeyEvent.KEYCODE_6:
            {
                key = TnKeyEvent.KEYCODE_6;
                break;
            }
            case KeyEvent.KEYCODE_7:
            {
                key = TnKeyEvent.KEYCODE_7;
                break;
            }
            case KeyEvent.KEYCODE_8:
            {
                key = TnKeyEvent.KEYCODE_8;
                break;
            }
            case KeyEvent.KEYCODE_9:
            {
                key = TnKeyEvent.KEYCODE_9;
                break;
            }
            case KeyEvent.KEYCODE_DEL:
            {
                key = TnKeyEvent.KEYCODE_DEL;
                break;
            }
            case KeyEvent.KEYCODE_SPACE:
            {
                key = TnKeyEvent.KEYCODE_SPACE;
                break;
            }
            case KeyEvent.KEYCODE_POUND:
            {
                key = TnKeyEvent.KEYCODE_POUND;
                break;
            }
            case KeyEvent.KEYCODE_STAR:
            {
                key = TnKeyEvent.KEYCODE_STAR;
                break;
            }
            case KeyEvent.KEYCODE_CALL:
            {
                key = TnKeyEvent.KEYCODE_CALL;
                break;
            }
            case KeyEvent.KEYCODE_ENDCALL:
            {
                key = TnKeyEvent.KEYCODE_ENDCALL;
                break;
            }
            case KeyEvent.KEYCODE_VOLUME_DOWN:
            {
                key = TnKeyEvent.KEYCODE_VOLUME_DOWN;
                break;
            }
            case KeyEvent.KEYCODE_VOLUME_UP:
            {
                key = TnKeyEvent.KEYCODE_VOLUME_UP;
                break;
            }
        }
        
        return key;
    }
}
