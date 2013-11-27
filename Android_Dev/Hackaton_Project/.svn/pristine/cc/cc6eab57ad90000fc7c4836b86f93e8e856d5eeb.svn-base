package com.telenav.mvc;

/**
 * Basic state constant definition interface.
 * 
 * @author zhdong
 * 
 */
public interface IStateConstants
{

    /**
     * Stable state mask. Stable state are always positive.<br>
     * Basically, stable state must have relative screen.
     */
    public static final int MASK_STATE_STABLE = 0x00000000;

    /**
     * Transient state mask. Transient states are always negative.<br>
     * Basically, if a state has no relative screen, it must be a transient state.
     */
    public static final int MASK_STATE_TRANSIENT = 0x80000000;

    
    /**
     * the state will show the state popup
     */
    public static final int MASK_STATE_SHOW_STATE_POPUP = 0xC0000000;
    
    /**
     * It is a reserved state which stand for any state.
     */
    public static final int STATE_ANY = 1;

    /**
     * It is a reserved state which stand for previous state
     */
    public static final int STATE_PREV = 2;

    /**
     * It is a reserved state which stand for invalid state
     */
    public static final int STATE_INVALID = 4;

    /**
     * It is a reserved state. A state machine MUST start with STATE_VIRGIN.
     */
    public static final int STATE_VIRGIN = 5;    
    
    /**
     * User defined common state must be increased based on STATE_COMMON_BASE
     */
    public static final int STATE_COMMON_BASE = 10;

    /**
     * User defined state must be increased based on STATE_USER_BASE
     */
    public static final int STATE_USER_BASE = 1000;
    
}
