/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * TnKeyEvent.java
 *
 */
package com.telenav.tnui.core;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2010-6-22
 */
public class TnKeyEvent
{
    /**
     * Action code for the key has been pressed down
     */
    public final static int ACTION_DOWN = 1;
    /**
     * Action code for the key has been released
     */
    public final static int ACTION_UP = 2;
    /**
     * Action code for the key has been pressed down and released repeatedly
     */
    public final static int ACTION_REPEAT = 3;
    /**
     * Key code for number '0'
     */
    public final static int KEYCODE_0 = 10;
    /**
     * Key code for number '1'
     */
    public final static int KEYCODE_1 = 11;
    /**
     * Key code for number '2'
     */
    public final static int KEYCODE_2 = 12;
    /**
     * Key code for number '3'
     */
    public final static int KEYCODE_3 = 13;
    /**
     * Key code for number '4'
     */
    public final static int KEYCODE_4 = 14;
    /**
     * Key code for number '5'
     */
    public final static int KEYCODE_5 = 15;
    /**
     * Key code for number '6'
     */
    public final static int KEYCODE_6 = 16;
    /**
     * Key code for number '7'
     */
    public final static int KEYCODE_7 = 17;
    /**
     * Key code for number '8'
     */
    public final static int KEYCODE_8 = 18;
    /**
     * Key code for number '9'
     */
    public final static int KEYCODE_9 = 19;
    
    /**
     * Key code for pad-left
     */
    public final static int KEYCODE_PAD_LEFT = 20;
    /**
     * Key code for pad-right
     */
    public final static int KEYCODE_PAD_RIGHT = 21;
    /**
     * Key code for pad-down
     */
    public final static int KEYCODE_PAD_DOWN = 22;
    /**
     * Key code for pad-up
     */
    public final static int KEYCODE_PAD_UP = 23;
    
    /**
     * Key code for pad-center
     */
    public final static int KEYCODE_PAD_CENTER = 24;
    
    /**
     * Key code for 'back'
     */
    public final static int KEYCODE_BACK = 29;
    /**
     * Key code for 'delete'
     */
    public final static int KEYCODE_DEL = 30;
    /**
     * Key code for 'enter'
     */
    public final static int KEYCODE_ENTER = 31;
    /**
     * Key code for 'space'
     */
    public final static int KEYCODE_SPACE = 32;
    /**
     * Key code for 'menu'
     */
    public final static int KEYCODE_MENU = 33;
    /**
     * Key code for 'search'
     */
    public final static int KEYCODE_SEARCH = 34;
    /**
     * Key code for the 'pound'
     */
    public final static int KEYCODE_POUND = 35;
    /**
     * Key code for 'star'
     */
    public final static int KEYCODE_STAR = 36;
    
    /**
     * Key code for 'call'
     */
    public final static int KEYCODE_CALL = 50;
    /**
     * Key code for 'end call'
     */
    public final static int KEYCODE_ENDCALL = 51;
    
    /**
     * key code for 'volume down'
     */
    public final static int KEYCODE_VOLUME_DOWN = 51;
    
    /**
     * key code for 'volume up'
     */
    public final static int KEYCODE_VOLUME_UP = 52;
    
    private int action;
    private char keyChar;
    private int keyCode;
    
    /**
     * Constructs a TnKeyEvent with specified action and key code
     * 
     * @param action
     * @param keyCode
     */
    public TnKeyEvent(int action, int keyCode)
    {
        this.action = action;
        this.keyCode = keyCode;
    }
    
    /**
     * Retrieve the action of the TnKeyEvent
     * 
     * @return action
     */
    public int getAction()
    {
        return action;
    }
    
    /**
     * Retrieve the char of the TnKeyEvent
     * 
     * @return keyChar
     */
    public int getChar()
    {
        return keyChar;
    }
    
    /**
     * set the char of the TnKeyEvent
     * 
     * @param keyChar
     */
    public void setChar(char keyChar)
    {
        this.keyChar = keyChar;
    }
    
    /**
     * Retrieve the key code of the TnKeyEvent
     * 
     * @return keyCode
     */
    public int getCode()
    {
        return keyCode;
    }
}
