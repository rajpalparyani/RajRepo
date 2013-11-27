package com.telenav.mvc;

/**
 * The AbstractModelParameter. It is a Parameter with model state.<br>
 * 
 * Q: Why add this abstract class between Parameter and AbstractModel?<br>
 * 
 * A: To make sure user can ONLY access parameters and query state in AbstractView.<br>
 * 
 * @author zhdong
 * 
 */
public abstract class AbstractModelParameter extends Parameter
{
    /**
     * Current model state
     */
    int state;

    /**
     * Previous state
     */
    int preState = IStateConstants.STATE_VIRGIN;

    /**
     * Is activated?
     */
    private boolean isActivated = true;

    /**
     * @return current state
     */
    public int getState()
    {
        return state;
    }
    
    void setState(int state)
    {
        this.state = state;
    }
    

    /**
     * 
     * Query previous model state
     * 
     * @return preState
     */
    public int getPreState()
    {
        return preState;
    }
    
    /**
     * Sets isActivated flag to true
     */
    void activate()
    {
        isActivated = true;
    }
    
    /**
     * Sets isActivated flag to true
     */
    void activate(int state)
    {
        this.state = state;
        isActivated = true;
    }
    
    /**
     * Sets isActivated flag to false
     */
    void deactivate()
    {
        isActivated = false;
    }

    /**
     * Checks whether this is activated.
     * @return the values if isActivated flag
     */
    public boolean isActivated()
    {
        return isActivated;
    }
}
