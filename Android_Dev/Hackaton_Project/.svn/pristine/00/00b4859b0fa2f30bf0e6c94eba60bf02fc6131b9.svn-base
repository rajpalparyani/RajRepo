package com.telenav.mvc;

/**
 * Basic trigger constants definition interface.<br>
 * 
 * There are 3 types of triggers.<br>
 * 
 * 1) Command - Must start with "CMD_"<br>
 * 
 * 2) Model event - Must start with "EVENT_MODEL_"<br>
 * 
 * 3) Controller event - Must start with "EVENT_CONTROLLER_"<br>
 * 
 * @author zhdong
 * 
 */
public interface ITriggerConstants
{

    // ------------------------------------------------------------
    // The command id definition
    // ------------------------------------------------------------

    /**
     * CMD_NONE means there is no command at all.
     */
    public static final int CMD_NONE = -1;

    /**
     * User defined command must be increased based on CMD_BASE
     */
    public static final int CMD_BASE = 1000000;

    /**
     * Common command id must be increased based on CMD_COMMON_BASE, such as back, home etc. These commands will be used
     * for all modules.
     */
    public static final int CMD_COMMON_BASE = CMD_BASE + 1000;

    /**
     * Module specific command id will be increased based on CMD_USER_BASE.
     */
    public static final int CMD_USER_BASE = CMD_BASE + 2000;

    // ------------------------------------------------------------
    // The model event id definition
    // ------------------------------------------------------------

    /**
     * The base model event id
     */
    public static final int EVENT_MODEL_BASE = 2000000;

    /**
     * This is a reserved id which will be posted any time model state changes.
     * 
     * It will only be used inside framework. And it is actually the same as EVENT_MODEL_UPDATE_VIEW.
     */
    public static final int EVENT_MODEL_STATE_CHANGE = EVENT_MODEL_BASE + 1;
    
    /**
     * This is a reserved id which will be posted any time if you want update view ONLY. When you post this model event,
     * the state machine will not change.
     */
    public static final int EVENT_MODEL_UPDATE_VIEW = EVENT_MODEL_STATE_CHANGE;

    /**
     * This is a reserved event model id. <br>
     * 
     * Q: When will we use it?<br>
     * 
     * A: If we want to release all module resources except current module during action, we should post this event.<br>
     * 
     */
    public static final int EVENT_MODEL_RELEASE_ALL_PREVIOUS_MODULES = EVENT_MODEL_BASE + 2;
    
    /**
     * All common model event must be based on this value.
     */
    public static final int EVENT_MODEL_COMMON_BASE = EVENT_MODEL_BASE + 1000;

    /**
     * All user defined model event must be based on this value.
     */
    public static final int EVENT_MODEL_USER_BASE = EVENT_MODEL_BASE + 2000;

    // ------------------------------------------------------------
    // The controller event id definition
    // ------------------------------------------------------------
    public static final int EVENT_CONTROLLER_BASE = 3000000;

    /**
     * This is a reserved id which will be used for default controller start event
     */
    public static final int EVENT_CONTROLLER_START = EVENT_CONTROLLER_BASE + 1;

    /**
     * All common controller event must be based on this value
     */
    public static final int EVENT_CONTROLLER_COMMON_BASE = EVENT_CONTROLLER_BASE + 1000;
    
    /**
     * All user defined controller event must be based on this value
     */
    public static final int EVENT_CONTROLLER_USER_BASE = EVENT_CONTROLLER_BASE + 2000;

}
