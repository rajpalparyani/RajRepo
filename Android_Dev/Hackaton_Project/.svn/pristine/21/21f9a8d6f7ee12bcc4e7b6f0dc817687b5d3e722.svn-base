/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * MockController.java
 *
 */
package com.telenav.mvc;

/**
 *@author yning
 *@date 2011-6-21
 */
public class AbstractControllerMock extends AbstractController
{
    int[][] state_table =
    {
            {IStateConstants.STATE_VIRGIN, ITriggerConstants.EVENT_CONTROLLER_START, 9, IActionConstants.ACTION_NONE}, 
    };

    public AbstractControllerMock(AbstractController superController)
    {
        super(superController);
    }

    protected AbstractModel createModel()
    {
        return new AbstractModelMock();
    }

    protected StateMachine createStateMachine()
    {
        return new StateMachine(state_table);
    }

    protected AbstractView createView()
    {
        return new AbstractViewMock();
    }

    protected void postStateChange(int currentState, int nextState)
    {

    }

    public void setModel(AbstractModel model)
    {
        this.model = model;
    }

    public void setView(AbstractView view)
    {
        this.view = view;
    }
    
    public void setStateMachine(int[][] stateTable)
    {
        this.stateMachine = new StateMachine(stateTable);
        if(model != null)
        {
            model.setState(stateMachine.getCurrentState());
        }
    }
}
