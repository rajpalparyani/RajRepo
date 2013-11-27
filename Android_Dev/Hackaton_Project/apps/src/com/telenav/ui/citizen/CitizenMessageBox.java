/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * CitizenMessageBox.java
 *
 */
package com.telenav.ui.citizen;


import com.telenav.module.AppConfigHelper;
import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.core.TnUiArgs;
import com.telenav.tnui.core.TnUiArgs.ITnUiArgsDecorator;
import com.telenav.tnui.core.TnUiArgs.TnUiArgAdapter;
import com.telenav.tnui.graphics.AbstractTnFont;
import com.telenav.tnui.graphics.AbstractTnGraphics;
import com.telenav.tnui.widget.TnLinearContainer;
import com.telenav.tnui.widget.TnScrollPanel;
import com.telenav.ui.NinePatchImageDecorator;
import com.telenav.ui.UiStyleManager;
import com.telenav.ui.frogui.widget.FrogMessageBox;
import com.telenav.ui.frogui.widget.FrogMultiLine;
import com.telenav.ui.frogui.widget.FrogNullField;
import com.telenav.util.PrimitiveTypeCache;

/**
 * Message box of Citizen style.
 * 
 * @author wzhu (wzhu@telenav.cn)
 * @date 2010-9-17
 */
public class CitizenMessageBox extends FrogMessageBox
{
    public static final int KEY_MESSAGE_BOX_TOP_HEIGHT = 10000;

    public static final int KEY_MESSAGE_BOX_BOTTOM_HEIGHT = 10001;
    
    public static final int KEY_MESSAGE_BOX_BUTTON_WIDTH = 10002;
    
    protected int BOTTOM_RADIANT_PADDING = 12;
    
    protected static final int MIN_BOTTOM_HEIGHT = 60;
    
    protected boolean isInit = false;
    
    protected TnLinearContainer topContainer;

    /**
     * construct a message box, usually with message and buttons
     * 
     * @param id
     * @param message
     */
    public CitizenMessageBox(int id, String message)
    {
        this(id, message, false);
    }
    
    /**
     * construct a message box, usually with message and buttons
     * 
     * @param id
     * @param message
     */
    public CitizenMessageBox(int id, String message, boolean isButtonContainerVertical)
    {
        super(id, message, isButtonContainerVertical);
        initMessageContainer();
    }
    
    protected void setUpContentContainer()
    {
        topContainer = new TnLinearContainer(0, true);

        topContainer.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, NinePatchImageDecorator.MESSAGE_BOX_TOP_BG);
        topContainer.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.MESSAGE_BOX_TOP_BG);
        topContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, 
            new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
            {
                public Object decorate(TnUiArgAdapter args)
                {
                    if(getTnUiArgs().get(KEY_MESSAGE_BOX_TOP_HEIGHT) != null)
                    {
                        return PrimitiveTypeCache.valueOf(getTnUiArgs().get(KEY_MESSAGE_BOX_TOP_HEIGHT).getInt());
                    }
                    return PrimitiveTypeCache.valueOf(0);
                }
            }));
        initGap();
        
        messageContainer = new TnLinearContainer(0, true, AbstractTnGraphics.VCENTER);
        this.messageMultiLine.setTextAlign(FrogMultiLine.TEXT_ALIGN_CENTER);
        this.messageMultiLine.setForegroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_ME_GR));
        
        TnScrollPanel scrollPanel = new TnScrollPanel(0, true);
        scrollPanel.set(messageMultiLine);
        messageContainer.add(scrollPanel);
        
        topContainer.add(this.messageContainer);
        this.contentContainer.add(topContainer);
    }
    
    protected void initGap()
    {
        int max = AppConfigHelper.getMaxDisplaySize();
        
        int left = 20 * max / 480;
        int right = 20 * max / 480;
        int top = 20 * max / 480;
        int bottom = 10 * max / 480;
        topContainer.setPadding(left, top, right, bottom);
    }

    public void setTitle(AbstractTnComponent label)
    {
        if(label == null )
            return;
        
        if(title == null)
        {
            title = label;
        }
        else
        {
            this.topContainer.remove(0);
        }
        
        this.topContainer.add(label, 0);
        requestLayout();
        requestPaint();
    }

    protected void initMessageContainer()
    {
        messageContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT,
            new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
            {
                public Object decorate(TnUiArgAdapter args)
                {
                    if (getTnUiArgs().get(KEY_MESSAGE_BOX_TOP_HEIGHT) != null
                            && CitizenMessageBox.this.messageMultiLine.getText().trim().length() > 0)
                    {
                        int topHeight = getTnUiArgs().get(KEY_MESSAGE_BOX_TOP_HEIGHT).getInt();
                        topHeight -= CitizenMessageBox.this.topContainer.getTopPadding();
                        topHeight -= CitizenMessageBox.this.topContainer.getBottomPadding();
                        if (title != null)
                        {
                            topHeight -= title.getPreferredHeight();
                        }
                        return PrimitiveTypeCache.valueOf(topHeight);
                    }
                    return PrimitiveTypeCache.valueOf(0);
                }
            }));
    }

    /**
     * override upper impl to support gap.
     */
    public void addButton(AbstractTnComponent button)
    {
        if (button == null)
            return;
        button.setCommandEventListener(this);
        if(buttonContainer == null)
        {
            buttonContainer = new TnLinearContainer(0, isButtonContainerVertical, AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);
            this.contentContainer.add(buttonContainer);
        }
        
        if(buttonCount > 0)
        {
            addGapField(button, buttonContainer);
        }
        
        buttonContainer.add(button);
        buttonCount ++;
        
        if(!isInit)
        {
            int padding = button.getPreferredHeight() / 3;
            buttonContainer.setPadding(0, padding, 0, padding + BOTTOM_RADIANT_PADDING);
            initButtonContainer();
            isInit = true;
        }
    }
    
    public TnLinearContainer getTopContainer()
    {
        return this.topContainer;
    }
    
    public void addGapField(final AbstractTnComponent button, TnLinearContainer buttonContainer)
    {
        FrogNullField gapField = new FrogNullField(0);
        if(buttonContainer.isVertical())
        {
            gapField.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
            {
                public Object decorate(TnUiArgAdapter args)
                {
                    return PrimitiveTypeCache.valueOf(button.getPreferredHeight() / 2);
                }
            }));
        }
        else
        {
            gapField.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
            {
                public Object decorate(TnUiArgAdapter args)
                {
                    return PrimitiveTypeCache.valueOf((button.getPreferredWidth() / 3));
                }
            }));
        }
        buttonContainer.add(gapField);
    }

    protected void initButtonContainer()
    {
        buttonContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, 
            new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
            {
                public Object decorate(TnUiArgAdapter args)
                {
                    if(getTnUiArgs().get(KEY_MESSAGE_BOX_BOTTOM_HEIGHT) != null)
                    {
                        return PrimitiveTypeCache.valueOf(getTnUiArgs().get(KEY_MESSAGE_BOX_BOTTOM_HEIGHT).getInt());
                    }
                    return PrimitiveTypeCache.valueOf(0);
                }
            }));

        if (buttonContainer.getPreferredHeight() < MIN_BOTTOM_HEIGHT)
        {
            buttonContainer.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS,
                NinePatchImageDecorator.MESSAGE_BOX_BOTTOM_SINGLE_LINE_BG);
            buttonContainer.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS,
                NinePatchImageDecorator.MESSAGE_BOX_BOTTOM_SINGLE_LINE_BG);
        }
        else
        {
            buttonContainer.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, NinePatchImageDecorator.MESSAGE_BOX_BOTTOM_BG);
            buttonContainer.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.MESSAGE_BOX_BOTTOM_BG);
        }
    }
    
    public void setContentFont(AbstractTnFont font)
    {
        if(this.messageMultiLine != null)
        {
            messageMultiLine.setFont(font);
            this.requestLayout();
            this.requestPaint();
        }
    }
}
