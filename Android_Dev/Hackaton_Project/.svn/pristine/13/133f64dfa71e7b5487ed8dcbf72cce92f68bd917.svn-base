package com.telenav.mvc;

import java.util.Vector;

/**
 * State machine for AbstractModel
 * 
 * @author zhdong
 * 
 */
public class StateMachine
{

    private int[][] stateTable;

    private int[][] commonStateTable;

    private State currentState;

    /**
     * 
     * Constructor state machine with given state table<br>
     * 
     * @param stateTable <br>
     * 
     * State table must be an int[][4] array , each line contains 4 integers which stand for a transform rule.<br>
     * 
     * int 1 : current state<br>
     * 
     * int 2 : trigger (command id/model event id/controller event id)<br>
     * 
     * int 3 : next state<br>
     * 
     * int 4 : action id<br>
     * 
     * 
     */
    public StateMachine(int[][] stateTable)
    {
        init(stateTable);
    }

    /**
     * Constructor state machine with given 2 state tables<br>
     * 
     * Q: Why add this constructor?<br>
     * 
     * A: Most project have 2 types of state table. One is the common flow such as back/home etc. The other is module
     * specific flows. We separate these 2 types just for convenience.<br>
     * 
     * @param commonStateTable
     * @param stateTable
     */
    public StateMachine(int[][] commonStateTable, int[][] stateTable)
    {

        Vector stateTableV = new Vector();

        //user defined table has higher priority
        for (int i = 0; i < stateTable.length; i++)
        {
            stateTableV.addElement(stateTable[i]);
        }

        //common table has lower priority
        for (int i = 0; i < commonStateTable.length; i++)
        {
            stateTableV.addElement(commonStateTable[i]);
        }

        int[][] allTable = new int[stateTableV.size()][4];

        copy(stateTableV, allTable);
        init(allTable);
    }

    private void init(int[][] stateTable)
    {
        Vector stateTableV = new Vector();

        Vector commonStateTableV = new Vector();

        for (int i = 0; i < stateTable.length; i++)
        {
            int currentState = stateTable[i][0];

            if (currentState == IStateConstants.STATE_ANY)
            {
                commonStateTableV.addElement(stateTable[i]);
            }
            else
            {
                stateTableV.addElement(stateTable[i]);
            }
        }

        this.stateTable = new int[stateTableV.size()][4];
        copy(stateTableV, this.stateTable);

        this.commonStateTable = new int[commonStateTableV.size()][4];
        copy(commonStateTableV, this.commonStateTable);

        currentState = new State();
    }

    private void copy(Vector orig, int[][] dest)
    {
        for (int i = 0; i < orig.size(); i++)
        {
            int[] temp = (int[]) orig.elementAt(i);
            dest[i] = temp;
        }
    }
    
    /**
     * Check if trigger exists.
     * 
     * @param trigger
     * @return
     */
    boolean isTriggerExist(int trigger)
    {
        for (int i = 0; i < stateTable.length; i++)
        {
            if (stateTable[i][1] == trigger)
            {
                return true;
            }
        }

        for (int i = 0; i < commonStateTable.length; i++)
        {
            if (commonStateTable[i][1] == trigger)
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Get current state
     * 
     * @return
     */
    synchronized int getCurrentState()
    {
        if (currentState == null)
        {
            return IStateConstants.STATE_INVALID;
        }
        return currentState.get();
    }
    
    /**
     * Back to last stable state
     */
    synchronized void backToLastStableState()
    {
        while (currentState != null
                && (currentState.get() & IStateConstants.MASK_STATE_TRANSIENT) == IStateConstants.MASK_STATE_TRANSIENT)
        {
            currentState = currentState.previousState;
            if (currentState != null)
            {
                currentState.nextState = null;
            }
        }
    }
    

    /**
     * Get next action by trigger. Meanwhile, change current state.
     * 
     * @param trigger
     * @return
     */
    synchronized int fetchNextAction(int trigger)
    {
        if (currentState == null)
        {
            return IActionConstants.ACTION_NONE;
        }

        // Check stateTable first

        for (int i = 0; i < stateTable.length; i++)
        {
            int[] temp = stateTable[i];

            if (temp[0] == currentState.get() && temp[1] == trigger)
            {
                // Yes, we got it.
                int nextStateId = temp[2];
                int actionId = temp[3];
                changeStateChain(nextStateId);
                return actionId;
            }
        }

        // If not found, check commonStateTable

        for (int i = 0; i < commonStateTable.length; i++)
        {
            int[] temp = commonStateTable[i];

            if (temp[1] == trigger)
            {
                // Yes, we got it.
                int nextStateId = temp[2];
                int actionId = temp[3];
                changeStateChain(nextStateId);
                return actionId;
            }

        }

        return IActionConstants.ACTION_NONE;
    }

    private void changeStateChain(int nextStateId)
    {
        if (nextStateId == IStateConstants.STATE_PREV)
        {
            if (currentState != null)
            {
                currentState = currentState.previousState;
                if (currentState != null)
                {
                    currentState.nextState = null;
                }
            }
        }
        else if (nextStateId == IStateConstants.STATE_VIRGIN)
        {
            // if next state id is STATE_VIRGIN, it means user want to exit current controller.
            currentState = new State();
        }
        else if (((currentState.get() & IStateConstants.MASK_STATE_TRANSIENT) == IStateConstants.MASK_STATE_TRANSIENT)
                && nextStateId != IStateConstants.STATE_ANY)// if state does not changed, do nothing.
        {
            // replace transient state to next state
            if (!stateExist(nextStateId))
            {
                currentState.set(nextStateId);
            }
        }
        else if (nextStateId != currentState.get() &&
                 nextStateId != IStateConstants.STATE_ANY) // if state does not changed, do nothing.
        {
            if (!stateExist(nextStateId))
            {
                State nextState = new State();
                nextState.set(nextStateId);
                nextState.previousState = currentState;
                currentState.nextState = nextState;
                currentState = nextState;
            }            
        }
    }
    
    
    private boolean stateExist(int state)
    {
        if (currentState != null)
        {
            State previousState = currentState.previousState;

            while (previousState != null)
            {
                if (previousState.get() == state)
                {
                    // find the state, remove all state after it.
                    previousState.nextState.previousState = null;
                    previousState.nextState = null;

                    currentState = previousState;

                    return true;
                }
                previousState = previousState.previousState;
            }
        }

        return false;
    }

    public String toString()
    {
        StringBuffer sb = new StringBuffer();

        if (currentState == null)
        {
            return "Current state is null. We must be existing current controller.";
        }

        sb.append("End, " + toReadable(currentState.get()) + " , ");
        State previousState;

        previousState = currentState.previousState;

        while (previousState != null)
        {
            sb.append(toReadable(previousState.get()) + " , ");

            previousState = previousState.previousState;
        }

        sb.append("Start");

        return sb.toString();
    }
    
    public static String toReadable(int state)
    {
        if ((IStateConstants.MASK_STATE_TRANSIENT & state) == IStateConstants.MASK_STATE_TRANSIENT)
        {
            return "~" + (state & (~IStateConstants.MASK_STATE_TRANSIENT));
        }
        else
        {
            return state + "";
        }
    }

    /**
     * State class which contains the state chain.
     * 
     * @author zhdong
     * 
     */
    static class State
    {
        /**
         * Current state
         */
        private int myState = IStateConstants.STATE_VIRGIN;

        /**
         * Next state
         */
        State nextState;

        /**
         * Previous state
         */
        State previousState;
        
        int get()
        {
            return myState;
        }
        
        void set(int newState)
        {
            myState = newState;
        }
    }
}
