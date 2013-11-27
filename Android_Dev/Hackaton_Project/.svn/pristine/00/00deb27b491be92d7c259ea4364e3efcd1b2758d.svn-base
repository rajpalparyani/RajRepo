/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * TnTextToken.java
 *
 */
package com.telenav.ui.tnui.text;

/**
 * Text token which is split from the text with space for latin, or two characters for east asia characters.
 * 
 *@author fqming (fqming@telenav.cn)
 *@date Jul 28, 2010
 */
public class TnTextToken
{
    /**
     * the text.
     */
    public String token;
    
    /**
     * if it's tag.
     */
    public boolean isTag;
    
    /**
     * cache of token's width.
     */
    public int width;
    
    /**
     * cache of token's x coordinate
     */
    public int xOffset;
    
    /**
     * cache of corresponding link index
     */
    public int correspondingLinkIndex;
    
  
    /**
     * construct the text token.
     * 
     * @param token the string in the token.
     * @param isTag check if it's tag. such as '<bold>'.
     */
    public TnTextToken(String token, boolean isTag)
    {
        this.token = token;
        this.isTag = isTag;
        this.correspondingLinkIndex = -1;
    }
    
    public String toString()
    {
        return this.token;
    }
}
