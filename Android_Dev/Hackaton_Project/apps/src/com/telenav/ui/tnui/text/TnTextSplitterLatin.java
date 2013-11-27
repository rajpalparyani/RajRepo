/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * StringSplitterLatin.java
 *
 */
package com.telenav.ui.tnui.text;

/**
 * A text splitter for latin characters.
 * 
 *@author fqming (fqming@telenav.cn)
 *@date Jul 30, 2010
 */
class TnTextSplitterLatin
{
    private String delimiter = " ";

    private String mString;

    private int mPosition;

    private int mLength;

    TnTextSplitterLatin()
    {
    }

    public boolean hasNext()
    {
        return mPosition < mLength;
    }

    public String next()
    {
        int end = mString.indexOf(delimiter, mPosition);
        if (end == -1)
        {
            end = mLength - 1;
        }
        String nextString = mString.substring(mPosition, end + 1);
        mPosition = end + 1; // Skip the delimiter.
        return nextString;
    }

    public int getCursorPosition()
    {
        return mPosition;
    }

    public void split(String string)
    {
        mString = string;
        mPosition = 0;
        mLength = mString.length();
    }

    public String getDelimiter()
    {
        return delimiter;
    }
}
