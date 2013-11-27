/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * TnTextLine.java
 *
 */
package com.telenav.ui.tnui.text;

import java.util.Enumeration;
import java.util.Vector;

/**
 * a string buffer's wrapper.
 * 
 *@author fqming (fqming@telenav.cn)
 *@date Jul 28, 2010
 */
public class TnTextLine
{
    /**
     * cache the string's width.
     */
    public int width;
    
    /**
     * cache the cursor position for current text line.
     */
    public int cursorPosition;
    
    /**
     * if'ts empty?
     */
    public boolean isEmpty = true;
    
    /**
     * y orientation offset
     */
    public int yOffset;
    
    /**
     * line height
     */
    public int lineHeight;
    
    /**
     * the tokens generated.
     */
    protected Vector tokens;
    
    /**
     * construct a text line.
     */
    public TnTextLine()
    {
        tokens = new Vector(4);
    }
    
    /**
     * add text token.
     * 
     * @param token a token.
     */
    public void addToken(TnTextToken token)
    {
        if(!token.isTag && token.token != null &&token.token.trim().length() > 0)
            isEmpty = false;
        
        tokens.addElement(token);
    }
    
    /**
     * last text token.
     * 
     * @return last text token.
     */
    public TnTextToken lastToken()
    {
        return (TnTextToken)tokens.lastElement();
    }
    
    /**
     * Retrieve the tokens.
     * 
     * @return the tokens.
     */
    public Enumeration elements()
    {
        return tokens.elements();
    }
    
    /**
     * Retrieve the tokens.
     * 
     * @return the tokens.
     */
    public Vector toVector()
    {
        return this.tokens;
    }
    
    /**
     * Retrieve the string buffer.
     * 
     * @return the string buffer.
     */
    public String getText()
    {
        StringBuffer text = new StringBuffer();
        for(int i = 0; i < tokens.size(); i++)
        {
            TnTextToken token = (TnTextToken)tokens.elementAt(i);
            if(token.isTag)
                continue;
            
            text.append(token.token);
        }
        
        return text.toString();
    }
}
