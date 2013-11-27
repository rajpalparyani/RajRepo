/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * TestStateMachine.java
 *
 */
package com.telenav.mvc;

import junit.framework.TestCase;

/**
 *@author zhdong@telenav.cn
 *@date 2011-5-24
 */
public class StateMachineTest extends TestCase implements IStateConstants
{
    int STATE_TEMP = 2 | MASK_STATE_TRANSIENT;

    private int[][] stateTable = new int[][]
    {
    { STATE_VIRGIN, 1001, 101, 0 },
    { 101, 1002, STATE_TEMP, 123 },
    { STATE_TEMP, 1003, 102, 0 },
    { 102, 1004, 103, 0 },
    { STATE_ANY, 1005, 101, 0 },
    { STATE_ANY, 1006, STATE_PREV, 0 },
    { STATE_ANY, 1007, STATE_VIRGIN, 0 } };

    public void testIsTriggerExist()
    {
        StateMachine sm = new StateMachine(stateTable);
        assertEquals(true, sm.isTriggerExist(1004));
        assertEquals(false, sm.isTriggerExist(1104));
        assertEquals(true, sm.isTriggerExist(1005));
    }

    public void testNormalFlow()
    {
        StateMachine sm = new StateMachine(stateTable);
        assertEquals("End, 5 , Start", sm.toString());
        sm.fetchNextAction(1001);
        assertEquals("End, 101 , 5 , Start", sm.toString());
        int action = sm.fetchNextAction(1002);
        assertEquals(123, action);
        assertEquals("End, ~2 , 101 , 5 , Start", sm.toString());
        sm.fetchNextAction(1007);
        assertEquals("End, 5 , Start", sm.toString());
        sm.fetchNextAction(1001);
        assertEquals("End, 101 , 5 , Start", sm.toString());
        action = sm.fetchNextAction(1002);
        assertEquals(123, action);
        assertEquals("End, ~2 , 101 , 5 , Start", sm.toString());
        action = sm.fetchNextAction(9999);
        assertEquals(IActionConstants.ACTION_NONE, action);
        sm.fetchNextAction(1003);
        assertEquals("End, 102 , 101 , 5 , Start", sm.toString());
        sm.fetchNextAction(1004);
        assertEquals("End, 103 , 102 , 101 , 5 , Start", sm.toString());
        sm.fetchNextAction(1005);
        assertEquals("End, 101 , 5 , Start", sm.toString());
        sm.fetchNextAction(1002);
        assertEquals("End, ~2 , 101 , 5 , Start", sm.toString());
        assertEquals(STATE_TEMP, sm.getCurrentState());
        sm.fetchNextAction(1003);
        assertEquals("End, 102 , 101 , 5 , Start", sm.toString());
        sm.fetchNextAction(1006);
        assertEquals("End, 101 , 5 , Start", sm.toString());
        sm.fetchNextAction(1006);
        assertEquals("End, 5 , Start", sm.toString());
        sm.fetchNextAction(1006);
        assertEquals("Current state is null. We must be existing current controller.", sm.toString());
        sm.fetchNextAction(1006);
        assertEquals(IStateConstants.STATE_INVALID, sm.getCurrentState());
    }

    public void testBackToLastStableState()
    {
        StateMachine sm = new StateMachine(stateTable);
        sm.fetchNextAction(1001);
        sm.fetchNextAction(1002);
        sm.backToLastStableState();
        assertEquals(101, sm.getCurrentState());
    }

    public void testStateMachineOverride()
    {
        int[][] specialTable = new int[][]
        {
        { STATE_VIRGIN, 1001, 101, 0 }, };

        int[][] commonTable = new int[][]
        {
        { STATE_VIRGIN, 1001, 102, 0 }, };

        // specialTable has high priority to commonTable
        StateMachine sm = new StateMachine(commonTable, specialTable);
        assertEquals("End, 5 , Start", sm.toString());
        sm.fetchNextAction(1001);
        assertEquals(101, sm.getCurrentState());
    }

}
