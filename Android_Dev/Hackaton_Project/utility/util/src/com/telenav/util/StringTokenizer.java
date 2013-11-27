/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * StringTokenizer.java
 *
 */
package com.telenav.util;

/**
 * string tokenizer class for java
 */
public class StringTokenizer
{
    private String  sStr    = null;
    private String  sDelim  = null;
    private int     nPos    = -1;
    
    private boolean returnDelim = false;
    
    /**
     * constructor
     * @param   sStrP   [in]    string to tokenize
     * @param   sDelimP [in]    token delimiter
     */
    public StringTokenizer(String sStrP, String sDelimP)
    {
        this.sStr = sStrP;
        this.sDelim = sDelimP;
        this.nPos = 0;
        
    } // constructor
    
    public StringTokenizer(String sStrP, String sDelimP, boolean returnDelim)
    {
        this.sStr = sStrP;
        this.sDelim = sDelimP;
        this.nPos = 0;
        
        this.returnDelim = returnDelim;
        
    } // constructor
    
    /**
     * indicates whether or not there are more tokens
     * @return  true iff there are more tokens
     */
    public boolean hasMoreTokens()
    {
        return (nPos != -1 && nPos != sStr.length());
    } // hasMoreTokens

    /**
     * returns next token
     * @param   next token.  null if there are no more tokens
     */
    public String nextToken()
    {
        // return null if there are no more tokens
        if (!hasMoreTokens())
            { return null; }
            
        // if index is equal to string length, then the string ended with a token
        // therefore, return an empty string
        if (this.nPos == this.sStr.length())
        {
            nPos = -1;
            return "";
        } // if
        
        if (returnDelim && sStr.substring(nPos, nPos+sDelim.length()).equals(sDelim))
        {
            nPos += sDelim.length();
            return sDelim;
        }
        
        int nNextPos = sStr.indexOf(sDelim, nPos);
        String sToken = null;
        
        // if position is -1, we've reached the last token
        if (-1 == nNextPos)
        {           
            sToken = this.sStr.substring(nPos);
            nPos = -1;
        } // if
        else
        {           
            sToken = this.sStr.substring(nPos, nNextPos);
            
            if (!returnDelim)
            {    
                nPos = nNextPos + sDelim.length();
            }
            else
            {
                nPos = nNextPos;
            }
        } // else
            
        return sToken;
    } // nextToken

} // class StringTokenizer
