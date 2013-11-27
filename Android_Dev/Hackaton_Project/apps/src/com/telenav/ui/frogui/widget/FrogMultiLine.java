/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * TestFrogMultiLine.java
 *
 */
package com.telenav.ui.frogui.widget;

import java.util.Vector;

import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.core.AbstractTnUiHelper;
import com.telenav.tnui.core.TnMotionEvent;
import com.telenav.tnui.core.TnUiArgs.ITnUiArgsDecorator;
import com.telenav.tnui.core.TnUiArgs.TnUiArgAdapter;
import com.telenav.tnui.core.TnUiEvent;
import com.telenav.tnui.graphics.AbstractTnFont;
import com.telenav.tnui.graphics.AbstractTnGraphics;
import com.telenav.tnui.graphics.AbstractTnImage;
import com.telenav.tnui.graphics.TnRect;
import com.telenav.tnui.widget.TnScrollPanel;
import com.telenav.ui.frogui.text.FrogTextHelper;
import com.telenav.ui.tnui.text.TnTextGraphicsContext;
import com.telenav.ui.tnui.text.TnTextLine;
import com.telenav.ui.tnui.text.TnTextParser;
import com.telenav.ui.tnui.text.TnTextToken;
import com.telenav.util.PrimitiveTypeCache;

/**
 * Provide fundamental {@link FrogMultiLine} for the UI component. It implement {@link TnScrollPanel} inside. The
 * {@link FrogMultiLine} can accept both for the regular string and mark-up string. <br />
 * <br />
 * 
 *@author fqming (fqming@telenav.cn)
 *@date Jul 30, 2010
 */
public class FrogMultiLine extends AbstractTnComponent
{
    /**
     * align text to left side of the container
     */
    public final static byte TEXT_ALIGN_LEFT = AbstractTnGraphics.LEFT;

    /**
     * align text to horizontal center of the container
     */
    public final static byte TEXT_ALIGN_CENTER = AbstractTnGraphics.HCENTER;

    /**
     * align text to right side of the container
     */
    public final static byte TEXT_ALIGN_RIGHT = AbstractTnGraphics.RIGHT;

    protected String text;

    protected AbstractTnFont boldFont;

    protected AbstractTnFont font;

    protected int gap = 0;

    protected int lineHeight;

    protected int foregroundColor;

    /**
     * alignType {@link #TEXT_ALIGN_CENTER, #TEXT_ALIGN_LEFT, #TEXT_ALIGN_RIGHT}
     */
    protected byte alignType = TEXT_ALIGN_LEFT;

    protected int selectedLinkStringIndex;

    protected Vector actionlinks;

    protected ITnUiArgsDecorator resourceDecorator;

    /**
     * Constructor a FrogMultiLine component.
     * 
     * @param id the id of component.
     * @param text multi-line text string.
     */
    public FrogMultiLine(int id, String text)
    {
        super(id);
        this.text = "";
        clipBounds = new TnRect();

        initDefaultStyle();

        this.setText(text);
    }

    /**
     * Initialize the attributes as default values.
     */
    protected void initDefaultStyle()
    {
        // set default font
        setFont(AbstractTnUiHelper.getInstance().createDefaultFont());
        setBoldFont(AbstractTnUiHelper.getInstance().createDefaultBoldFont());

        leftPadding = 2;
        rightPadding = leftPadding;
        topPadding = 2;
        bottomPadding = topPadding;
        gap = 2;
    }

    /**
     * set text of FrogMultiLine.
     * 
     * @param text text of FrogMultiLine.
     */
    public void setText(String text)
    {
        if (text != null)
        {

            this.text = text;
            this.remainderText = this.text;

            if (this.measureWidth > 0)
            {
                this.requestLayout();
            }
        }
    }

    /**
     * Retrieve the text of FrogMultiLine.
     * 
     * @return the text of FrogMultiLine.
     */
    public String getText()
    {
        return this.text;
    }

    /**
     * set foreground color of FrogMultiLine.
     * 
     * @param color color of FrogMultiLine.
     */
    public void setForegroundColor(int color)
    {
        if (this.foregroundColor != color)
        {
            this.foregroundColor = color;
            this.requestPaint();
        }
    }

    /**
     * Retrieve the foreground color.
     * 
     * @return the foreground color.
     */
    public int getForegroundColor()
    {
        return this.foregroundColor;
    }

    /**
     * Set the text's font.
     * 
     * @param font the text's font.
     */
    public void setFont(AbstractTnFont font)
    {
        if (this.font == null || !this.font.equals(font))
        {
            this.font = font;
            this.requestLayout();
        }
    }

    /**
     * Retrieve the text's font.
     * 
     * @return the text's font.
     */
    public AbstractTnFont getFont()
    {
        return this.font;
    }

    /**
     * set the text's bold font.
     * 
     * @param boldFont the text's bold font.
     */
    public void setBoldFont(AbstractTnFont boldFont)
    {
        if (this.boldFont == null || !this.boldFont.equals(boldFont))
        {
            this.boldFont = boldFont;
            this.requestLayout();
        }
    }

    /**
     * Retrieve the text's bold font.
     * 
     * @return the text's bold font.
     */
    public AbstractTnFont getBoldFont()
    {
        return this.font;
    }

    /**
     * set the text's gap between line and line.
     * 
     * @param gap the pixel of gap.
     */
    public void setTextGap(int gap)
    {
        if (this.gap != gap)
        {
            this.gap = gap;
            this.requestLayout();
        }
    }

    /**
     * Retrieve the text's gap.
     * 
     * @return the text's gap.
     */
    public int getTextGap()
    {
        return this.gap;
    }

    /**
     * Set the align type, for the value refer to FrogMultiLine
     * 
     * @param alignType TEXT_ALIGN_LEFT,TEXT_ALIGN_CENTER, TEXT_ALIGN_RIGHT
     */
    public void setTextAlign(byte alignType)
    {
        if (this.alignType != alignType)
        {
            this.alignType = alignType;
            this.requestPaint();
        }

    }

    /**
     * Retrieve the type value of text align.
     * 
     * @return alignType
     */
    public byte getTextAlign()
    {
        return this.alignType;
    }

    /**
     * Scroll changed event.
     * 
     * @param l Current horizontal scroll origin.
     * @param t Current vertical scroll origin.
     * @param oldl Previous horizontal scroll origin.
     * @param oldt Previous vertical scroll origin.
     */
    public void onScrollChanged(int l, int t, int oldl, int oldt)
    {
        if (!this.needParsing())
            return;

        this.requestLayout();
    }

    /**
     * Retrieve the clicked string of URL or telephone number.
     * 
     * @return null the clicked string is common string, or the clicked string is mark up string of "url" or "tel"
     */
    public String getClickedLinkText()
    {
        if (selectedLinkStringIndex >= 0 && selectedLinkStringIndex < this.actionlinks.size())
        {
            return (String) actionlinks.elementAt(selectedLinkStringIndex);
        }
        else
        {
            return null;
        }
    }

    /**
     * Set Resource Decorator.
     * 
     * @param decorator
     */
    public void setResDecorator(ITnUiArgsDecorator decorator)
    {
        this.resourceDecorator = decorator;
    }

    protected int getLineHeight()
    {
        this.lineHeight = font.getHeight() + gap;
        return lineHeight;
    }

    // ===================================For inner field ==================================//
    protected TnRect clipBounds;

    protected boolean needLayoutAfterPaint = false;

    protected TnTextGraphicsContext nativeBackPainter;

    protected TnTextGraphicsContext nativeCurrentPainter;

    // Below is for parsing
    protected String remainderText;

    protected int maxParseLineCount;

    protected int maxParseTokenCount;

    protected TnTextGraphicsContext backPainter;

    protected TnTextGraphicsContext currentPainter;

    protected Vector lines = new Vector();

    protected int measureWidth;

    protected int lastMeasuredWidth;

    protected TnTextLine currentParsingTokens;

    protected int currentParseLineIndex;

    protected int currentTextLineIndex;

    protected int realTextHeight;

    protected TnTextLine currentLine;

    protected int currentLineWidth;

    protected int lineVisibleStartIndex = -1;

    protected int lineVisibleEndIndex;

    protected boolean isLinkStart;

    protected StringBuffer actionlinker;

    protected int linkIndex;

    protected boolean isImageStart;

    public void sublayout(int width, int height)
    {
        // for the portrait and landscape switch.
        this.preferWidth = 0;//Anyway, clear previous value.
        this.measureWidth = FrogMultiLine.this.getPreferredWidth();
        
        if (this.measureWidth <= 0)
        {
            this.measureWidth = width;
        }
        
        if (lastMeasuredWidth != this.measureWidth)
        {
            if (lastMeasuredWidth != 0)
            {
                needLayoutAfterPaint = true;
            }

            super.setPreferredWidth(0);
            super.setPreferredHeight(0);

            reset();
            
            this.lastMeasuredWidth = this.measureWidth;
        }

        if (this.maxParseLineCount <= 0)
        {
            if (FrogMultiLine.this.text.length() < 1500)
            {
                this.maxParseLineCount = Integer.MAX_VALUE;
                this.maxParseTokenCount = Integer.MAX_VALUE;
            }
            else
            {
                int maxDisplayValue = Math.max(((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getDisplayHeight(),
                    ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getDisplayWidth());
                this.maxParseLineCount = (maxDisplayValue / getLineHeight()) + 2;
                this.maxParseTokenCount = (maxDisplayValue / FrogMultiLine.this.font.getMaxWidth() * 10);
                if (this.maxParseTokenCount < 450)
                {
                    this.maxParseTokenCount = 450;
                }
            }
        }

        if (this.currentParseLineIndex <= 0)
        {
            this.currentParseLineIndex = this.maxParseLineCount;
        }

        parseLines();

        super.setPreferredWidth(this.measureWidth);
        super.setPreferredHeight(this.realTextHeight);
    }

    public boolean handleUiEvent(TnUiEvent tnUiEvent)
    {
        TnMotionEvent tnMotionEvent = tnUiEvent.getMotionEvent();
        switch (tnUiEvent.getType())
        {
            case TnUiEvent.TYPE_TOUCH_EVENT:
            {
                if (tnMotionEvent.getAction() == TnMotionEvent.ACTION_UP)
                {
                    int lineIndex = this.findLinesIndex(tnMotionEvent.getY());
                    if (lineIndex >= 0 && lineIndex < this.lines.size())
                    {
                        selectedLinkStringIndex = getClickedToken(tnMotionEvent.getX(), (TnTextLine) this.lines.elementAt(lineIndex));
                    }

                    if (touchEventListener != null)
                    {
                        touchEventListener.handleUiEvent(tnUiEvent);
                    }
                }
                break;
            }

        }

        // return false, because of not blocking App layer need get onClickEvent.
        return false;
    }

    protected void paint(AbstractTnGraphics graphics)
    {
        paintText(graphics, 0, FrogMultiLine.this.topPadding);
    }

	protected void paintText(AbstractTnGraphics graphics, int xOffSet, int yOffSet) {
		this.lineVisibleEndIndex = -1;
        this.lineVisibleStartIndex = -1;
        int oldColor = graphics.getColor();
        AbstractTnFont oldFont = graphics.getFont();

        graphics.getClipBounds(clipBounds);

        if (nativeBackPainter == null)
        {
            nativeBackPainter = new TnTextGraphicsContext(font, FrogMultiLine.this.foregroundColor);
        }
        else
        {
            nativeBackPainter.color = FrogMultiLine.this.foregroundColor;
            nativeBackPainter.font = font;
        }

        if (nativeCurrentPainter == null)
        {
            nativeCurrentPainter = new TnTextGraphicsContext(font, FrogMultiLine.this.foregroundColor);
        }
        else
        {
            nativeCurrentPainter.color = FrogMultiLine.this.foregroundColor;
            nativeCurrentPainter.font = font;
        }

        int x = 0;
        int y = yOffSet;
        int lineHeight = getLineHeight();

        for (int i = 0; i < lines.size(); i++)
        {
            TnTextLine textLine = (TnTextLine) lines.elementAt(i);
            boolean needPaint = graphics.isIntersectWithClip(clipBounds, FrogMultiLine.this.leftPadding, y, this.measureWidth, lineHeight);

            switch (FrogMultiLine.this.alignType)
            {
                case FrogMultiLine.TEXT_ALIGN_CENTER:
                    x = (this.measureWidth - FrogTextHelper.getWidth(textLine, FrogMultiLine.this.font, FrogMultiLine.this.boldFont)) / 2;
                    break;
                case FrogMultiLine.TEXT_ALIGN_LEFT:
                    x = FrogMultiLine.this.leftPadding;
                    break;
                case FrogMultiLine.TEXT_ALIGN_RIGHT:
                    x = this.measureWidth - FrogMultiLine.this.rightPadding
                            - FrogTextHelper.getWidth(textLine, FrogMultiLine.this.font, FrogMultiLine.this.boldFont);
                    break;
            }
            if (needPaint)
            {
                if (this.lineVisibleStartIndex < 0)
                {
                    lineVisibleStartIndex = i;
                }
                this.lineVisibleEndIndex = i;

            }
            FrogTextHelper.paint(graphics, x + xOffSet, y, textLine, FrogMultiLine.this.font, FrogMultiLine.this.boldFont, this.measureWidth, false,
                nativeCurrentPainter, nativeBackPainter, resourceDecorator, needPaint);
            if (!textLine.isEmpty)
            {
                y += lineHeight;
                textLine.lineHeight = lineHeight;
            }
        }

        graphics.setColor(oldColor);
        graphics.setFont(oldFont);

        if (needLayoutAfterPaint)
        {
            needLayoutAfterPaint = false;
            this.requestLayout();
        }
    }

    private void parseLines()
    {
        if (currentPainter == null)
        {
            currentPainter = new TnTextGraphicsContext(font, 0);
        }
        if (backPainter == null)
        {
            backPainter = new TnTextGraphicsContext(font, 0);
        }

        if (!needParsing())
            return;

        if (currentParsingTokens == null || currentTextLineIndex >= this.currentParsingTokens.toVector().size() - 1)
        {
            currentTextLineIndex = 0;
            currentParsingTokens = TnTextParser.parse(this.remainderText, maxParseTokenCount);

            if (remainderText.length() > currentParsingTokens.cursorPosition)
            {
                remainderText = this.remainderText.substring(currentParsingTokens.cursorPosition);
            }
            else
            {
                remainderText = "";
            }
        }

        if (currentLine == null)
        {
            currentLine = new TnTextLine();
            currentLineWidth = FrogMultiLine.this.leftPadding + FrogMultiLine.this.rightPadding;
        }

        for (; currentTextLineIndex < this.currentParsingTokens.toVector().size(); currentTextLineIndex++)
        {
            TnTextToken token = (TnTextToken) this.currentParsingTokens.toVector().elementAt(currentTextLineIndex);
            if (token.isTag)
            {
                if (FrogTextHelper.BOLD_START.endsWith(token.token))
                {
                    currentPainter.font = boldFont;
                }
                else if (FrogTextHelper.BOLD_END.endsWith(token.token))
                {
                    currentPainter.font = backPainter.font;
                }
                else if (FrogTextHelper.IMG_START.endsWith(token.token))
                {
                    isImageStart = true;
                }
                else if (FrogTextHelper.IMG_END.endsWith(token.token))
                {
                    isImageStart = false;
                }
                token.token = token.token.trim();
                this.addToken(currentLine, token);
                continue;
            }
            if (isImageStart && !token.isTag)
            {
                String imageKeyStr = token.token;
                token.width = 0;
                int intKey = 0;
                try
                {
                    intKey = Integer.parseInt(imageKeyStr);
                }
                catch (Throwable t)
                {

                }

                if (intKey > 0)
                {
                    TnUiArgAdapter imageAdapter = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(intKey), resourceDecorator);
                    AbstractTnImage image = imageAdapter.getImage();
                    if (image != null)
                    {
                        token.width = image.getWidth();
                    }
                }
            }

            int lineTagIndex = token.token.indexOf("\n");
            if (lineTagIndex != -1)
            {
                if (lineTagIndex < token.token.length() - 1)
                {
                    if(lineTagIndex == 0)
                    {
                        token.token = token.token.substring(1);
                    }
                    else
                    {
                        String tokenText = token.token;
                        token.token = tokenText.substring(0, lineTagIndex);

                        TnTextToken newToken = new TnTextToken(tokenText.substring(lineTagIndex), false);
                        this.currentParsingTokens.toVector().insertElementAt(newToken, currentTextLineIndex + 1);
                        lineTagIndex = -1;
                    }
                }
                else
                {
                    token.token = token.token.substring(0, lineTagIndex);
                }
            }
            
            if (token.width <= 0)
            {
                token.width = currentPainter.font.stringWidth(token.token);
            }
            currentLineWidth += token.width;
            
            if (currentLineWidth > this.measureWidth || lineTagIndex != -1)
            {
                lines.addElement(currentLine);
                realTextHeight += getLineHeight();

                if ((FrogMultiLine.this.leftPadding + FrogMultiLine.this.rightPadding + token.width) > this.measureWidth)
                {
                    Vector tokens = new Vector();
                    splitLongToken(tokens, token.token);
                    for (int n = 0; n < tokens.size(); n++)
                    {
                        currentLine = new TnTextLine();
                        currentLineWidth = FrogMultiLine.this.leftPadding + FrogMultiLine.this.rightPadding + token.width;

                        token = (TnTextToken) tokens.elementAt(n);
                        this.addToken(currentLine, token);
                        if (n != tokens.size() - 1)
                        {
                            lines.addElement(currentLine);
                            realTextHeight += getLineHeight();
                        }
                    }
                    continue;
                }

                currentLine = new TnTextLine();
                currentLineWidth = FrogMultiLine.this.leftPadding + FrogMultiLine.this.rightPadding + token.width;
            }

            if (currentLine.toVector().size() == 0)
            {
                if (token.token.trim().length() == 0)
                    continue;
            }
            addToken(currentLine, token);
            if (lines.size() >= currentParseLineIndex)
            {

                break;
            }
        }
        currentTextLineIndex++;
        if ((remainderText == null || remainderText.length() == 0) && currentTextLineIndex >= this.currentParsingTokens.toVector().size())
        {
            lines.addElement(currentLine);
            realTextHeight += getLineHeight();
        }

        currentParseLineIndex += maxParseLineCount;

    }

    private void addToken(TnTextLine textLine, TnTextToken token)
    {
        if (token.isTag)
        {
            if (token.token.startsWith(FrogTextHelper.TEL_START) || token.token.startsWith(FrogTextHelper.URL_START))
            {
                this.isLinkStart = true;
            }
            else if (token.token.startsWith(FrogTextHelper.TEL_END) || token.token.startsWith(FrogTextHelper.URL_END))
            {
                if (isLinkStart)
                {
                    if (actionlinks == null)
                    {
                        actionlinks = new Vector();
                    }
                    if (actionlinker != null && actionlinker.length() > 0)
                    {
                        actionlinks.addElement(actionlinker.toString());
                        this.linkIndex++;
                        actionlinker.delete(0, actionlinker.length());
                    }
                    isLinkStart = false;
                }
            }
        }
        if (this.isLinkStart && !token.isTag)
        {
            if (this.actionlinker == null)
            {
                this.actionlinker = new StringBuffer();
            }
            this.actionlinker.append(token.token);
            token.correspondingLinkIndex = linkIndex;

        }
        textLine.addToken(token);
    }

    private void reset()
    {
        lines.removeAllElements();
        remainderText = FrogMultiLine.this.text;
        currentParsingTokens = null;
        this.currentLine = null;
        actionlinks = null;
        currentTextLineIndex = 0;
        maxParseLineCount = 0;
        currentParseLineIndex = 0;
        linkIndex = 0;
        realTextHeight = FrogMultiLine.this.topPadding + FrogMultiLine.this.bottomPadding;
    }

    private void splitLongToken(Vector tokens, String text)
    {
        if (measureWidth <= 0)
            return;

        int len = FrogMultiLine.this.leftPadding + FrogMultiLine.this.rightPadding;
        for (int next = 0; next < text.length(); next++)
        {
            len += this.currentPainter.font.charWidth(text.charAt(next));
            if (len >= this.measureWidth)
            {
                TnTextToken newToken = new TnTextToken(text.substring(0, next), false);
                newToken.width = this.currentPainter.font.stringWidth(newToken.token);
                tokens.addElement(newToken);
                splitLongToken(tokens, text.substring(next));
                return;
            }
        }

        TnTextToken newToken = new TnTextToken(text, false);
        newToken.width = this.currentPainter.font.stringWidth(newToken.token);
        tokens.addElement(newToken);
    }

    private boolean needParsing()
    {
        if ((this.remainderText == null || this.remainderText.length() == 0)
                && (this.currentParsingTokens == null || this.currentParsingTokens.toVector() == null || currentTextLineIndex >= this.currentParsingTokens
                        .toVector().size()))
            return false;

        return true;
    }

    private int findLinesIndex(int yClicked)
    {
        int lineBeginIndex = this.lineVisibleStartIndex;
        int lineEndIndex = this.lineVisibleEndIndex;
        int lineMiddleIndex = (lineBeginIndex + lineEndIndex) / 2;
        int result = onLineChecking(lineMiddleIndex, yClicked);
        while (result != 0 && result != -2)
        {
            if (lineBeginIndex > lineEndIndex)
            {
                break;
            }
            if (result > 0)
            {
                lineBeginIndex = lineMiddleIndex + 1;
            }
            else
            {
                lineEndIndex = lineMiddleIndex - 1;
            }
            lineMiddleIndex = (lineBeginIndex + lineEndIndex) >> 1;
            result = onLineChecking(lineMiddleIndex, yClicked);

        }
        if (result == 0)
        {
            return lineMiddleIndex;
        }
        return -1;

    }

    /**
     * 
     * @return 0 on the middle line, -1 clicked point is up of the middle line, 1 clicked point is below of the line, -2
     *         clicked point is invalid parameter;
     */
    private int onLineChecking(int lineIndex, int yClicked)
    {
        if (lineIndex < 0 || lineIndex > lines.size() - 1)
        {
            return -2;
        }
        TnTextLine textLine = (TnTextLine) lines.elementAt(lineIndex);
        if (yClicked < textLine.yOffset)
        {
            return -1;
        }
        else if (yClicked > textLine.yOffset + textLine.lineHeight)
        {
            return 1;
        }
        else
            return 0;

    }

    private int getClickedToken(int xOffSet, TnTextLine line)
    {
        Vector tokens = line.toVector();
        for (int i = 0; i < tokens.size(); i++)
        {
            TnTextToken token = (TnTextToken) tokens.elementAt(i);
            if (token.xOffset <= xOffSet && xOffSet <= token.xOffset + token.width)
            {
                return token.correspondingLinkIndex;
            }
        }
        return -1;

    }
}
