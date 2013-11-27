/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * TnScreenStack.java
 *
 */
package com.telenav.tnui.core;

import java.util.Stack;

/**
 *  This maintains a stack of Screen objects.
 *  <br />
 *  Each screen may appear only once in the display stack. The application throws a runtime exception 
 *  if you attempt to push a single screen onto the stack more than once. 
 *  
 *@author fqming (fqming@telenav.cn)
 *@date 2010-6-29
 */
public class TnScreenStack
{
    protected Stack stack;
    
    /**
     * Construct a screen stack.
     */
    public TnScreenStack()
    {
        stack = new Stack();
    }
    
    /**
     * Returns the screen at the specified index.
     * 
     * @param location an index into this stack.
     * @return the screen at the specified index.
     */
    public synchronized TnScreen elementAt(int location)
    {
        return (TnScreen)stack.elementAt(location);
    }
    
    /**
     * Deletes the screen at the specified index. Each screen in this stack with an index greater or equal to the
     * specified index is shifted downward to have an index one smaller than the value it had previously.
     * 
     * The index must be a value greater than or equal to 0 and less than the current size of the stack.
     * 
     * @param location the index of the object to remove.
     */
    public synchronized void removeAt(int location)
    {
        stack.removeElementAt(location);
    }
    
    /**
     * Pushes a screen onto the top of the stack.
     * 
     * @param screen 
     * @return screen {@link TnScreen}
     */
    public synchronized TnScreen push(TnScreen screen)
    {
        if(stack.contains(screen))
        {
            throw new IllegalStateException("The screen is in the stack.");
        }
        
        return (TnScreen)stack.push(screen);
    }
    
    /**
     * Returns the screen at the top of the stack and removes it.
     * 
     * @return screen {@link TnScreen}
     */
    public synchronized TnScreen pop()
    {
        if(size() == 0)
            return null;
        
        return (TnScreen)stack.pop();
    }
    
    /**
     * Returns the screen at the top of the stack without removing it.
     * 
     * @return screen {@link TnScreen}
     */
    public synchronized TnScreen peek()
    {
        if(size() == 0)
            return null;
        
        return (TnScreen)stack.peek();
    }
    
    /**
     * Returns the number of screens in this vector.
     * 
     * @return int
     */
    public synchronized int size()
    {
        return stack.size();
    }
    
    /**
     * Removes all screens from this stack, leaving the size zero and the
     * capacity unchanged.
     */
    public synchronized void clear()
    {
        stack.removeAllElements();
    }
}
