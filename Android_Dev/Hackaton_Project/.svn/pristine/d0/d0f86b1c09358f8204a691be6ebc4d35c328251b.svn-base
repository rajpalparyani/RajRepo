/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * TnTextParser.java
 *
 */
package com.telenav.ui.tnui.text;


/**
 * The text parser.
 * 
 *@author fqming (fqming@telenav.cn)
 *@date Jul 28, 2010
 */
public class TnTextParser
{
    /**
     * parse a string buffer.
     * 
     * @param text the string buffer.
     * @return a text line.
     */
    public static TnTextLine parse(String text)
    {
        return parse(text, -1);
    }
    
    /**
     * parse a string buffer with max token's count.
     * 
     * @param text the string buffer.
     * @param maxParseTokenCount max token's count.
     * @return a text line.
     */
    public static TnTextLine parse(String text, int maxParseTokenCount)
    {
        TnTextLine textLine = new TnTextLine();
        
        if(text == null || text.length() == 0)
        {
            TnTextToken token = new TnTextToken(text, false);
            textLine.addToken(token);
            return textLine;
        }
        
        // FIXME we need check if it's latin character
        TnTextSplitterLatin splitter = new TnTextSplitterLatin();
        splitter.split(text);
        int size = 0;
        while (splitter.hasNext())
        {
            String next = splitter.next();
            getToken(next, textLine);
            size++;
            if(maxParseTokenCount > 0 && size > maxParseTokenCount)
            {
                break;
            }
        }

        TnTextToken lastToken = textLine.lastToken();
        lastToken.token = lastToken.token.startsWith(" ") ? " " + lastToken.token.trim() : lastToken.token.trim();
        textLine.cursorPosition = splitter.getCursorPosition();
        
        return textLine;
    }

    private static void getToken(String text, TnTextLine textLine)
    {
        int leftTag = text.indexOf("<");

        if (leftTag != -1)
        {
            if (leftTag > 0)
            {
                addToken(textLine, text.substring(0, leftTag), false);
            }

            int rightIndex = text.indexOf(">", leftTag);
            if (rightIndex != -1)
            {
                addToken(textLine, text.substring(leftTag, rightIndex + 1), true);
                if(rightIndex + 1 < text.length())
                {
                    getToken(text.substring(rightIndex + 1), textLine);
                }
            }
            else
            {
                addToken(textLine, text, false);
            }
        }
        else
        {
            addToken(textLine, text, false);
        }
    }
    
    private static void addToken(TnTextLine textLine, String tokenText, boolean isTag)
    {
        TnTextToken token = new TnTextToken(tokenText, isTag);
        
        textLine.addToken(token);
    }
}
