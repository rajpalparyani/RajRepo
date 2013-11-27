/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * AbstractTnComponent.java
 *
 */
package com.telenav.tnui.core;

import com.telenav.tnui.core.TnUiArgs.TnUiArgAdapter;
import com.telenav.tnui.graphics.AbstractTnFont;
import com.telenav.tnui.graphics.AbstractTnGraphics;
import com.telenav.tnui.graphics.AbstractTnImage;
import com.telenav.tnui.graphics.TnColor;
import com.telenav.tnui.graphics.TnDrawable;
import com.telenav.tnui.graphics.TnRect;

/**
 * Provides fundamental functionality for all ui components. <br />
 * <br />
 * 
 * A component represents a rectangular region contained by a container. The component sizes itself according to its
 * needs in layout. The container, rather than the contained fields, completely handles scrolling. <br />
 * <br />
 * 
 * <b>Creating your own custom components</b><br />
 * To design your own custom component, you must (at least) paint abstract methods to provide the behavior needed by
 * your component. You should also override the {@link #getPreferredWidth()} and {@link #getPreferredHeight()} methods,
 * to ensure proper layout within some of the layout managers. <br />
 * <br />
 * 
 * <b>Forcing component repainting</b><br />
 * To force a field to update or redraw itself, you should invoke its {@link #requestPaint()} method. <br />
 * <br />
 * 
 * <b>Event Handling</b><br />
 * You can override the {@link #handleUiEvent} method, and also you can register the listener {@link ITnUiEventListener}
 * . <br />
 * <br />
 * 
 * <b>Focus Handling</b><br />
 * You can override the {@link #onFocusChanged} method, and also you can register the listener
 * {@link ITnFocusChangeListener}.
 * 
 * @author fqming (fqming@telenav.cn)
 * @date 2010-6-2
 */
public abstract class AbstractTnComponent
{
    /**
     * Show the menu by clicking menu key.
     */
    public final static int TYPE_MENU = 0;

    /**
     * Show the menu by clicking the component. When the size == 1, it will not show a menu, and execute the action of
     * the menu item directly.
     */
    public final static int TYPE_CLICK = 1;

    /**
     * Show the menu by clicking back key. When the size == 1, it will not show a menu, and execute the action of
     * the menu item directly.
     */
    public final static int TYPE_BACK = 2;

    /**
     * Show the context menu by long clicking the component. This menu is revealed with a "right-click" on a PC.
     */
    public final static int TYPE_CONTEXT = 3;
    
    /**
     * <b>Call Native Method</b> <br />
     * call setFocusListener of native ui component.
     */
    public final static int METHOD_FOCUS_LISTENER = -10000001;
    
    /**
     * <b>Call Native Method</b> <br />
     * Request re-layout for the UI component.
     */
    public final static int METHOD_REQUEST_RELAYOUT = -10000002;
    
    /**
     * <b>Call Native Method</b> <br />
     * set enabled.
     */
    public final static int METHOD_SET_ENABLED = -10000003;
    
    /**
     * <b>Call Native Method</b> <br />
     * is enabled.
     */
    public final static int METHOD_IS_ENABLED = -10000004;
    
    /**
     * <b>Call Native Method</b> <br />
     * is enabled.
     */
    public final static int METHOD_SET_ANIMATION = -10000005;
    
    /**
     * <b>Call Native Method</b> <br />
     * is enabled.
     */
    public final static int METHOD_SET_ID = -10000006;
    
    public final static int METHOD_SET_CONTENT_DESCRIPTION = -10000007;
    
    protected INativeUiComponent nativeUiComponent;

    protected ITnFocusChangeListener focusChangeListener;

    protected ITnUiEventListener keyEventListener;

    protected ITnUiEventListener touchEventListener;

    protected ITnUiEventListener trackballEventListener;

    protected ITnUiEventListener commandListener;
    
    protected ITnUiEventListener gestureEventListener;
    
    protected ITnSizeChangListener sizeChangeListener;

    protected int id;

    protected TnUiArgs tnUiArgs;

    protected int preferWidth = -1;

    protected int preferHeight = -1;

    protected boolean isFocused;

    protected int leftPosition = -1;

    protected int topPosition = -1;
    
    protected int leftPadding, rightPadding, topPadding, bottomPadding;

    protected int backgroundColor = TnColor.TRANSPARENT;

    protected TnMenu[] menus;

    protected AbstractTnComponent parent;

    protected TnScreen root;

    protected TnDrawable backgroundDrawable;
    
    protected String currentLocale;
    
    protected TnUiAnimationContext animationContext;
    
    protected Object cookie;
    
    //=============================scroller start===========================================//
    /**
     * the scrolling step
     */
    protected static final int SCROLL_STEP = 2;

    /**
     * distance between the text and the margin when finishing scrolling
     */
    protected static final int OVER_SCROLL = 0;

    /**
     * times for the text to wait
     */
    protected static final int WAIT_COUNT = 6;
    
    /**
     * if the text is scrolling back
     */
    protected boolean isScrollingBack;

    /**
     * the scrolling distance of a text
     */
    protected int scrollX;

    /**
     * times that the text has waited
     */
    protected int waitCount = -1;
    //=============================end===========================================//
    
    /**
     * Constructs a new component instance with special id without lazy bind with native component.
     * 
     * @param id
     */
    public AbstractTnComponent(int id)
    {
        this(id, false);
    }
    
    /**
     * Constructs a new component instance with special id, which will bind to native component lazily. <br />
     * This constructor should not be visible as public.
     * 
     * @param id
     * @param lazyBind
     */
    protected AbstractTnComponent(int id, boolean lazyBind)
    {
        this.id = id;

        menus = new TnMenu[TYPE_CONTEXT + 1];

        if (!lazyBind)
        {
            bind();
        }
        
        AbstractTnI18nProvider i18nProvider = ((AbstractTnUiHelper)AbstractTnUiHelper.getInstance()).getI18nProvider();
        if(i18nProvider != null)
        {
            this.currentLocale = i18nProvider.getCurrentLocale();
        }
    }

    /**
     * bind the component with native ui component.
     */
    protected final void bind()
    {
        nativeUiComponent = AbstractTnUiBinder.instance.bindNativeUiComponent(this);
        
        this.nativeUiComponent.callUiMethod(METHOD_SET_ID, null);
    }

    /**
     * Implements custom layout features for this component.
     * 
     * @param width
     * @param height
     */
    public void sublayout(int width, int height)
    {

    }

    /**
     * Retrieves the native ui component.
     * 
     * @return {@link INativeUiComponent}
     */
    public final INativeUiComponent getNativeUiComponent()
    {
        return this.nativeUiComponent;
    }

    /**
     * Invoked by the framework to redraw a portion of this component.
     * 
     * @param graphics {@link AbstractTnGraphics}
     */
    public final void draw(AbstractTnGraphics graphics)
    {
        int oldColor = graphics.getColor();
        AbstractTnFont oldFont = graphics.getFont();

        loadI18nResource();
        
        paintBackground(graphics);

        paint(graphics);
        
        paintShadow(graphics);

        graphics.setColor(oldColor);
        graphics.setFont(oldFont);
    }

    protected void paintShadow(AbstractTnGraphics graphics)
    {
        if (this.tnUiArgs != null)
        {
            TnUiArgAdapter uiArgAdapter = null;
            
            //draw top shade
            uiArgAdapter = this.tnUiArgs.get(TnUiArgs.KEY_TOP_SHADOW_IMAGE);
            if (uiArgAdapter != null)
            {
                AbstractTnImage bgImage = uiArgAdapter.getImage();
                if (bgImage != null)
                {
                    if(bgImage.getDrawable() != null)
					{
                        bgImage.setWidth(getWidth());
                        bgImage.setHeight(bgImage.getHeight());
                        graphics.drawImage(bgImage, 0, 0, AbstractTnGraphics.LEFT | AbstractTnGraphics.TOP);
					}
                    else
                    {
                        graphics.drawImage(bgImage, new TnRect(0, 0, bgImage.getWidth(), bgImage.getHeight()), new TnRect(
                            0, 0, getWidth(), bgImage.getHeight()));
                    }
                }
            }
            
            //draw bottom shade
            uiArgAdapter = this.tnUiArgs.get(TnUiArgs.KEY_BOTTOM_SHADOW_IMAGE);
            if (uiArgAdapter != null)
            {
                AbstractTnImage bgImage = uiArgAdapter.getImage();
                if (bgImage != null)
                {
                    if(bgImage.getDrawable() != null)
					{
                        bgImage.setWidth(getWidth());
                        bgImage.setHeight(bgImage.getHeight());
                        graphics.drawImage(bgImage, 0, getHeight() - bgImage.getHeight(), AbstractTnGraphics.LEFT | AbstractTnGraphics.TOP);
					}
                    else
                    {
                        graphics.drawImage(bgImage, new TnRect(0, 0, bgImage.getWidth(), bgImage.getHeight()), new TnRect(
                            0, getHeight() - bgImage.getHeight(), getWidth(), getHeight()));
                    }
                }
            }
            //draw left
            uiArgAdapter = this.tnUiArgs.get(TnUiArgs.KEY_LEFT_SHADOW_IMAGE);
            if (uiArgAdapter != null)
            {
                AbstractTnImage bgImage = uiArgAdapter.getImage();
                if (bgImage != null)
                {
                    if(bgImage.getDrawable() != null)
					{
                        bgImage.setWidth(bgImage.getWidth());
                        bgImage.setHeight(getHeight());
                        graphics.drawImage(bgImage, 0, 0, AbstractTnGraphics.LEFT | AbstractTnGraphics.TOP);
					}
                    else
                    {
                        graphics.drawImage(bgImage, new TnRect(0, 0, bgImage.getWidth(), bgImage.getHeight()), new TnRect(
                            0, 0, bgImage.getWidth(), getHeight()));
                    }
                }
            }
            
            //draw right
            uiArgAdapter = this.tnUiArgs.get(TnUiArgs.KEY_RIGHT_SHADOW_IMAGE);
            if (uiArgAdapter != null)
            {
                AbstractTnImage bgImage = uiArgAdapter.getImage();
                if (bgImage != null)
                {
                    if(bgImage.getDrawable() != null)
					{
                        bgImage.setWidth(bgImage.getWidth());
                        bgImage.setHeight(getHeight());
                        graphics.drawImage(bgImage, getWidth() - bgImage.getWidth(), 0, AbstractTnGraphics.LEFT | AbstractTnGraphics.TOP);
					}
                    else
                    {
                        graphics.drawImage(bgImage, new TnRect(0, 0, bgImage.getWidth(), bgImage.getHeight()), new TnRect(
                            getWidth() - bgImage.getWidth(), 0, getWidth(), getHeight()));
                    }
                }
            }
        }
    }

    /**
     * Paint the background of this component. <br />
     * You can set the background color by {@link #setBackgroundColor}, or set the image by {@link #setTnUiArgs}.
     * 
     * @param graphics {@link AbstractTnGraphics}
     */
    protected void paintBackground(AbstractTnGraphics graphics)
    {
        int oldColor = graphics.getColor();

        if (this.backgroundDrawable != null)
        {
            TnRect oldBounds = this.backgroundDrawable.getBounds();
            if (oldBounds == null)
            {
                this.backgroundDrawable.setBounds(new TnRect(0, 0, this.getWidth(), this.getHeight()));
            }
            //we are not expecting drawable decide its size during paint.
            this.backgroundDrawable.draw(graphics);
            this.backgroundDrawable.setBounds(oldBounds);
        }
        else if (this.tnUiArgs != null)
        {
            TnUiArgAdapter uiArgAdapter = null;
            if (isFocused())
                uiArgAdapter = this.tnUiArgs.get(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS);
            else
                uiArgAdapter = this.tnUiArgs.get(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS);
            if (uiArgAdapter != null)
            {
                AbstractTnImage bgImage = uiArgAdapter.getImage();
                if(bgImage != null)
                {
                    bgImage.setWidth(getWidth());
                    bgImage.setHeight(getHeight());
                    graphics.drawImage(bgImage, 0, 0, AbstractTnGraphics.LEFT | AbstractTnGraphics.TOP);
                }
            }
            else if (this.backgroundColor != TnColor.TRANSPARENT)
            {
                graphics.setColor(this.backgroundColor);
                graphics.fillRect(0, 0, this.getWidth(), this.getHeight());
                graphics.setColor(oldColor);
            }
        }
        else if (this.backgroundColor != TnColor.TRANSPARENT)
        {
            graphics.setColor(this.backgroundColor);
            graphics.fillRect(0, 0, this.getWidth(), this.getHeight());
            graphics.setColor(oldColor);
        }
    }

    /**
     * Paint the body of this component.
     * 
     * @param graphics {@link AbstractTnGraphics}
     */
    protected abstract void paint(AbstractTnGraphics graphics);

    /**
     * Pass the display change event to this component.
     * 
     * @param isDisplayed true if this component is attached, otherwise false.
     */
    public final void dispatchDisplayChanged(boolean isDisplayed)
    {
        if(isDisplayed)
        {
            onDisplay();
        }
        else
        {
            onUndisplay();
        }
    }
    
    /**
     * Invoked when the screen this field is attached to is pushed onto the display stack.
     * <br/>
     * This method is invoked by the system after the screen is pushed onto the stack and layout has been done, but
     * before any painting occurs.
     */
    protected void onDisplay()
    {
        
    }
    
    /**
     * Invoked when the screen this field is attached to is popped off the display stack. 
     */
    protected void onUndisplay()
    {
        
    }
    
    /**
     * Pass the ui focus event to this component.
     * 
     * @param gainFocus true if has the focus, otherwise false.
     */
    public final void dispatchFocusChanged(boolean gainFocus)
    {
        if(this.isFocused == gainFocus)//avoid invoke this method many times.
        {
            return;
        }
        
        this.isFocused = gainFocus;

        handleUiTimerReceiver();
        
        onFocusChanged(gainFocus);
    }
    
    /**
     * handle the ui timer receiver logic.
     */
    protected void handleUiTimerReceiver()
    {
        if (this.isFocused)
        {
            TnUiTimer.getInstance().addReceiver(this, getTimerInterval());
        }
        else
        {
            TnUiTimer.getInstance().removeReceiver(this);
            resetScrollEvent();
        }
    }

    /**
     * Retrieve the ui timer interval.
     * 
     * @return timer interval
     */
    protected int getTimerInterval()
    {
        return TnUiTimer.DEFAULT_INTERVAL;
    }

    /**
     * Invoked when this component's focus change.
     * 
     * @param gainFocus
     */
    protected void onFocusChanged(boolean gainFocus)
    {
        
    }

    /**
     * Pass size changed event to this component.
     * 
     * @param w Current width of this component.
     * @param h Current height of this component.
     * @param oldw Old width of this component.
     * @param oldh Old height of this component. 
     */
    public final void dispatchSizeChanged(int w, int h, int oldw, int oldh)
    {
        resetScrollEvent();
        
        onSizeChanged(w, h, oldw, oldh);
        
    }
    /**
     * Invoked when this component's size change.
     * 
     * @param w Current width of this component.
     * @param h Current height of this component.
     * @param oldw Old width of this component.
     * @param oldh Old height of this component. 
     */
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        
    }
    
    /**
     * Pass the ui event down to this component.
     * 
     * @param tnUiEvent {@link TnUiEvent}
     * @return isHandle
     */
    public final boolean dispatchUiEvent(TnUiEvent tnUiEvent)
    {
        boolean isHandled = false;

        isHandled = handleUiEvent(tnUiEvent);

        if (!isHandled)
        {
            switch (tnUiEvent.getType())
            {
                case TnUiEvent.TYPE_COMMAND_EVENT:
                {
                    isHandled = this.commandListener == null ? false : this.commandListener.handleUiEvent(tnUiEvent);
                    break;
                }
                case TnUiEvent.TYPE_KEY_EVENT:
                {
                    isHandled = this.keyEventListener == null ? false : this.keyEventListener.handleUiEvent(tnUiEvent);
                    break;
                }
                case TnUiEvent.TYPE_PRIVATE_EVENT:
                {
                    switch(tnUiEvent.getPrivateEvent().getAction())
                    {
                        case TnPrivateEvent.ACTION_TIMER: //timer event should not go to the parent, or it will reduce the performance seriously.
                        {
                            isHandled = true;
                            break;
                        }
                    }
                    break;
                }
                case TnUiEvent.TYPE_TOUCH_EVENT:
                {
                    isHandled = this.touchEventListener == null ? false : this.touchEventListener.handleUiEvent(tnUiEvent);
                    break;
                }
                case TnUiEvent.TYPE_TRACKBALL_EVENT:
                {
                    isHandled = this.trackballEventListener == null ? false : this.trackballEventListener.handleUiEvent(tnUiEvent);
                    break;
                }
                case TnUiEvent.TYPE_GESTURE_SCALE:
                {
                    isHandled = this.gestureEventListener == null ? false : this.gestureEventListener.handleUiEvent(tnUiEvent);
                    break;
                }

            }
        }

        if (!isHandled && tnUiEvent.getType() != TnUiEvent.TYPE_PRIVATE_EVENT && parent != null)// FIXME not sure if need send the ui event to the parent currently.
        {
            isHandled = parent.dispatchUiEvent(tnUiEvent);
        }

        return isHandled;
    }

    /**
     * Override this method to provide the special ui event for the component.
     * 
     * @param tnUiEvent {@link TnUiEvent}
     * @return
     */
    protected boolean handleUiEvent(TnUiEvent tnUiEvent)
    {
        return false;
    }

    /**
     * Retrieves the relative parameters of this component.
     * 
     * @return {@link TnUiArgs}
     */
    public TnUiArgs getTnUiArgs()
    {
        if(this.tnUiArgs == null)
        {
            this.tnUiArgs = new TnUiArgs();
        }
        
        return this.tnUiArgs;
    }

    /**
     * set the menu of this component.
     * 
     * @param menu {@link TnMenu}
     * @param type {@link #TYPE_BACK, #TYPE_CLICK, #TYPE_LONG_CLICK, #TYPE_MENU}
     */
    public void setMenu(TnMenu menu, int type)
    {
        if (type >= this.menus.length)
        {
            throw new IllegalArgumentException("the menu type is wrong.");
        }

        this.menus[type] = menu;
    }

    /**
     * Retrieve the menu of this component.
     * 
     * @param type {@link #TYPE_BACK, #TYPE_CLICK, #TYPE_LONG_CLICK, #TYPE_MENU}
     * @return menu {@link TnMenu}
     */
    public TnMenu getMenu(int type)
    {
        if (type >= this.menus.length)
        {
            throw new IllegalArgumentException("the menu type is wrong.");
        }

        return this.menus[type];
    }

    /**
     * set this component's preferred width.
     * 
     * @param preferWidth
     */
    protected final void setPreferredWidth(int preferWidth)
    {
        this.preferWidth = preferWidth;
    }

    /**
     * Retrieves this component's preferred width.
     * 
     * @return int
     */
    public final int getPreferredWidth()
    {
        if (this.tnUiArgs != null)
        {
            TnUiArgs.TnUiArgAdapter argAdapter = this.tnUiArgs.get(TnUiArgs.KEY_PREFER_WIDTH);
            if (argAdapter != null)
            {
                preferWidth = argAdapter.getInt();
            }
        }

        return preferWidth;
    }

    /**
     * set this component's preferred height.
     * 
     * @param preferHeight
     */
    protected final void setPreferredHeight(int preferHeight)
    {
        this.preferHeight = preferHeight;
    }

    /**
     * Retrieves this component's preferred height.
     * 
     * @return int
     */
    public final int getPreferredHeight()
    {
        if (this.tnUiArgs != null)
        {
            TnUiArgs.TnUiArgAdapter argAdapter = this.tnUiArgs.get(TnUiArgs.KEY_PREFER_HEIGHT);
            if (argAdapter != null)
            {
                preferHeight = argAdapter.getInt();
            }
        }

        return preferHeight;
    }


    /**
     * Retrieves this component's left-offset position.
     * 
     * @return int
     */
    public final int getX()
    {
        if (this.tnUiArgs != null)
        {
            TnUiArgs.TnUiArgAdapter argAdapter = this.tnUiArgs.get(TnUiArgs.KEY_POSITION_X);
            if (argAdapter != null)
            {
                this.leftPosition = argAdapter.getInt();
            }
        }

        return this.leftPosition < 0 ? nativeUiComponent.getNativeX() : this.leftPosition;
    }

    /**
     * this component's top-offset position.
     * 
     * @return int
     */
    public final int getY()
    {
        if (this.tnUiArgs != null)
        {
            TnUiArgs.TnUiArgAdapter argAdapter = this.tnUiArgs.get(TnUiArgs.KEY_POSITION_Y);
            if (argAdapter != null)
            {
                this.topPosition = argAdapter.getInt();
            }
        }

        return this.topPosition < 0 ? nativeUiComponent.getNativeY() : this.topPosition;
    }

    /**
     * Retrieves this component's width.
     * 
     * @return int
     */
    public int getWidth()
    {
        return nativeUiComponent.getNativeWidth();
    }

    /**
     * Retrieves this component's height.
     * 
     * @return int
     */
    public int getHeight()
    {
        return nativeUiComponent.getNativeHeight();
    }

    /**
     * Set the enabled state of this component.
     * 
     * @param isVisible
     */
    public final void setVisible(boolean isVisible)
    {
        if(nativeUiComponent != null)
        {
            nativeUiComponent.setNativeVisible(isVisible);
        }
    }

    /**
     * Retrieves the enabled state of this component.
     * 
     * @return boolean
     */
    public final boolean isVisible()
    {
        return nativeUiComponent.isNativeVisible();
    }

    /**
     * Set whether this view can receive the focus.
     * 
     * @param isFocusable
     */
    public final void setFocusable(boolean isFocusable)
    {
        if(nativeUiComponent != null)
        {
            nativeUiComponent.setNativeFocusable(isFocusable);
        }
    }

    /**
     * Retrieves whether this view can receive the focus.
     * 
     * @return boolean
     */
    public final boolean isFocusable()
    {
        return nativeUiComponent.isNativeFocusable();
    }

    /**
     * Set the this control to be enabled or disabled. 
     * 
     * @param enabled if True, make this control enabled; otherwise, make this control disabled.
     */
    public final void setEnabled(boolean enabled)
    {
        nativeUiComponent.callUiMethod(METHOD_SET_ENABLED, new Object[]{new Boolean(enabled)});
    }
    
    /**
     * Determines if this control is enabled. 
     * 
     * @return True if this control is enabled
     */
    public final boolean isEnabled()
    {
        return ((Boolean)nativeUiComponent.callUiMethod(METHOD_IS_ENABLED, null)).booleanValue();
    }
    
    /**
     * Set the background color of this component.
     * 
     * @param backgroundColor
     */
    public void setBackgroundColor(int backgroundColor)
    {
        if(this.backgroundColor != backgroundColor)
            this.requestPaint();
        
        this.backgroundColor = backgroundColor;
    }

    /**
     * Retrieves the background color of this component.
     * 
     * @return int
     */
    public int getBackgroundColor()
    {
        return this.backgroundColor;
    }

    /**
     * Set the focus change listener of this component.
     * 
     * @param focusChangeListener
     */
    public void setFocusChangeListener(ITnFocusChangeListener focusChangeListener)
    {
        this.focusChangeListener = focusChangeListener;

        if(nativeUiComponent != null)
        {
            nativeUiComponent.callUiMethod(METHOD_FOCUS_LISTENER, new Object[]
            { focusChangeListener });
        }
    }

    /**
     * Retrieves the focus change listener of this component.
     * 
     * @return {@link ITnFocusChangeListener}
     */
    public final ITnFocusChangeListener getFocusChangeListener()
    {
        return this.focusChangeListener;
    }
    
    /**
     * Set the size change listener of this component.
     * 
     * @param sizeChangeListener
     */
    public final void setSizeChangeListener(ITnSizeChangListener sizeChangeListener)
    {
        this.sizeChangeListener = sizeChangeListener;
    }

    /**
     * Retrieves the size change listener of this component.
     * 
     * @return {@link ITnSizeChangListener}
     */
    public final ITnSizeChangListener getSizeChangeListener()
    {
        return this.sizeChangeListener;
    }

    /**
     * set the key change listener of this component.
     * 
     * @param keyEventListener
     */
    public final void setKeyEventListener(ITnUiEventListener keyEventListener)
    {
        this.keyEventListener = keyEventListener;
    }

    /**
     * Retrieves the key event change listener.
     * 
     * @return {@link ITnUiEventListener}
     */
    public final ITnUiEventListener getKeyEventListener()
    {
        return this.keyEventListener;
    }

    /**
     * set the touch event listener of this component.
     * 
     * @param touchEventListener
     */
    public final void setTouchEventListener(ITnUiEventListener touchEventListener)
    {
        this.touchEventListener = touchEventListener;
    }

    /**
     * Retrieves the touch event listener of this component.
     * 
     * @return {@link ITnUiEventListener}
     */
    public final ITnUiEventListener getTouchEventListener()
    {
        return this.touchEventListener;
    }

    /**
     * set trackball event listener of this component.
     * 
     * @param trackballEventListener
     */
    public final void setTrackballEventListener(ITnUiEventListener trackballEventListener)
    {
        this.trackballEventListener = trackballEventListener;
    }

    /**
     * Retrieves the trackball event listener of this component.
     * 
     * @return {@link ITnUiEventListener}
     */
    public final ITnUiEventListener getTrackballEventListener()
    {
        return this.trackballEventListener;
    }
    
    public final void setGestureEventListener(ITnUiEventListener gestureEventListener)
    {
        this.gestureEventListener = gestureEventListener;
    }
    
    public final ITnUiEventListener getGestureEventListener()
    {
        return this.gestureEventListener;
    }

    /**
     * set command event listener of this component.
     * 
     * @param commandListener
     */
    public final void setCommandEventListener(ITnUiEventListener commandListener)
    {
        this.commandListener = commandListener;
    }

    /**
     * Retrieves the command event listener of this component.
     * 
     * @return {@link ITnUiEventListener}
     */
    public final ITnUiEventListener getCommandEventListener()
    {
        return this.commandListener;
    }

    /**
     * Retrieves id of this component
     * 
     * @return id
     */
    public int getId()
    {
        return this.id;
    }

    /**
     * Set the component's id.
     * 
     * @param id the component's id
     */
    public void setId(int id)
    {
        this.id = id;
        
        if(this.nativeUiComponent != null)
        {
            this.nativeUiComponent.callUiMethod(METHOD_SET_ID, null);
        }
    }
    
    /**
     * Call this to try to give focus to a specific component or to one of its descendants. A component will not
     * actually take focus if it is not focusable {@link #isFocusable()}.
     * 
     * @return boolean
     */
    public final boolean requestFocus()
    {
        return nativeUiComponent.requestNativeFocus();
    }

    /**
     * Invalidate the whole component. If the component is visible, native's paint method will be called at some point in the
     * future.
     */
    public void requestPaint()
    {
        nativeUiComponent.requestNativePaint();
    }

    /**
     * Returns true if this component has focus.
     * 
     * @return True if this component has focus, false otherwise.
     */
    public final boolean isFocused()
    {
        return isFocused;
    }

    /**
     * Gets the parent of this component. Note that the parent is a AbstractTnComponent and not necessarily a container.
     * 
     * @return Parent of this component.
     */
    public final AbstractTnComponent getParent()
    {
        return this.parent;
    }

    /**
     * Sets the parent of this component.
     * 
     * @param parent Parent of this component.
     */
    public final void setParent(AbstractTnComponent parent)
    {
        this.parent = parent;
    }

    /**
     * Finds the topmost component in the current component hierarchy. Notice that the topmost is a screen which is not
     * a component. get the container by {@link TnScreen#getRootContainer()}.
     * 
     * @return the topmost component containing this component.
     */
    public final TnScreen getRoot()
    {
        if (this.root != null)
        {
            return this.root;
        }
        else if (this.parent != null)
        {
            return this.parent.getRoot();
        }

        return null;
    }

    /**
     * Gets the topmost component in the current component hierarchy.
     * 
     * @param root the topmost component containing this component.
     */
    public final void setRoot(TnScreen root)
    {
        this.root = root;
    }

    /**
     * Set a background drawable for this component. if set this drawable, the background style will be controlled in
     * drawble, not in this component.
     * 
     * @param drawable a background drawble for this component.
     */
    public final void setBackgroundDrawable(TnDrawable drawable)
    {
        this.backgroundDrawable = drawable;
    }

    /**
     * Retrieve the background drawable for this component.
     * 
     * @return the background drawble for this component.
     */
    public final TnDrawable getBackgroundDrawable()
    {
        return this.backgroundDrawable;
    }
    
    /**
     * Call this when something has changed which has invalidated the layout of this component. This will schedule a layout
     * pass of the component tree.
     */
    public final void requestLayout()
    {
        this.nativeUiComponent.callUiMethod(METHOD_REQUEST_RELAYOUT, null);
    }
    
    /**
     * Sets the next animation to play for this UI component. 
     * 
     * @param context animation's context.
     */
    public void setAnimationContext(TnUiAnimationContext context)
    {
        this.animationContext = context;
        
        if(this.animationContext != null)
        {
            context.setNativeAnimation(AbstractTnUiBinder.instance.bindNativeUiAnimation(context));

            this.nativeUiComponent.callUiMethod(METHOD_SET_ANIMATION, new Object[]{ context });
        }
    }
    
    /**
     * Retrieve the next animation of this UI component.
     * 
     * @return the next animation.
     */
    public TnUiAnimationContext getAnimationContext()
    {
        return this.animationContext;
    }
    
    /**
     * 
     * Set padding for component.
     * 
     * @param leftPadding
     * @param rightPadding
     * @param topPadding
     * @param bottomPadding
     */
    public void setPadding(int leftPadding, int topPadding, int rightPadding, int bottomPadding)
    {
    	boolean isRepaint = false;
    	if(this.leftPadding != leftPadding || this.rightPadding != rightPadding || this.topPadding != topPadding || this.bottomPadding != bottomPadding)
    		isRepaint = true;

    	if(leftPadding >= 0)
    	    this.leftPadding = leftPadding;
    	
    	if(rightPadding >= 0)
    	    this.rightPadding = rightPadding;
    	
    	if(topPadding >= 0)
    	    this.topPadding = topPadding;
    	
    	if(bottomPadding >= 0)
    	    this.bottomPadding = bottomPadding;
    	
    	if(isRepaint && this.getNativeUiComponent() != null)
    	{
    	    this.getNativeUiComponent().requestNativePaint();
    	}
    }
    
    /**
     * get left padding for component.
     * 
     * @return left padding.
     */
    public final int getLeftPadding()
    {
    	return this.leftPadding;
    }
    
    /**
     * get right padding for component.
     * 
     * @return right padding.
     */
    public final int getRightPadding()
    {
    	return this.rightPadding;
    }
    
    /**TUP 
     * get top padding for component.
     * 
     * @return top padding.
     */
    public final int getTopPadding()
    {
    	return this.topPadding;
    }
    
    /**
     * get bottom padding for component.
     * 
     * @return bottom padding.
     */
    public final int getBottomPadding()
    {
    	return this.bottomPadding;
    }
    
    /**
     * Sets the cookie associated with this ui component. A cookie can be used to mark a ui component in its hierarchy
     * and does not have to be unique within the hierarchy. Tags can also be used to store data within a ui component
     * without resorting to another data structure.
     * 
     * @param cookie an Object to tag the view with
     */
    public final void setCookie(Object cookie)
    {
        this.cookie = cookie;
    }
    
    /**
     * Returns this view's cookie.
     * 
     * @return the Object stored in this view as a tag
     */
    public final Object getCookie()
    {
        return this.cookie;
    }
    
    /**
     * load i18n texts
     */
    private final void loadI18nResource()
    {
        AbstractTnI18nProvider i18nProvider = ((AbstractTnUiHelper)AbstractTnUiHelper.getInstance()).getI18nProvider();
        if(i18nProvider != null && !i18nProvider.getCurrentLocale().equals(this.currentLocale))
        {
            this.currentLocale = i18nProvider.getCurrentLocale();
            
            reloadI18nResource();
        }
    }
    
    /**
     * reload i18n texts
     */
    protected void reloadI18nResource()
    {
        
    }
    
    /**
     * Retrieve text from i18n.
     * 
     * @param key the key of string.
     * @param familyName the family of string.
     * @return a text
     */
    protected String getText(int key, String familyName)
    {
        AbstractTnI18nProvider i18nProvider = ((AbstractTnUiHelper)AbstractTnUiHelper.getInstance()).getI18nProvider();
        
        return i18nProvider.getText(key, familyName);
    }
    
    /**
     * handle scroll function.
     * 
     * @param component
     * @param textWidth
     * @param isScrollNeeded
     */
    protected void handleScrollEvent(int textWidth, boolean isScrollNeeded)
    {
        if (isScrollNeeded)
        {
            int scrollingWidth = this.getWidth() - this.getLeftPadding() - this.getRightPadding();
            if (scrollingWidth > 0 && textWidth > scrollingWidth)
            {
                if (waitCount >= WAIT_COUNT)
                {
                    if (!isScrollingBack)
                    {
                        if (textWidth + OVER_SCROLL > scrollingWidth + scrollX)
                        {
                            scrollX += SCROLL_STEP;
                        }
                        else
                        {
                            isScrollingBack = true;
                            waitCount = 0;
                        }
                    }
                    else
                    {
                        if (scrollX + OVER_SCROLL > 0)
                        {
                            scrollX -= SCROLL_STEP;
                        }
                        else
                        {
                            isScrollingBack = false;
                            waitCount = 0;
                        }
                    }
                    this.requestPaint();
                }
                else
                {
                    if (waitCount == -1)
                    {
                        this.requestPaint();
                    }
                    waitCount++;
                }
            }
        }
    }
    
    protected void resetScrollEvent()
    {
        isScrollingBack = false;
        scrollX = 0;
        waitCount = -1;
    }
    

    public void setContentDescription(String name)
    {
        if(nativeUiComponent !=null)
        {
            this.nativeUiComponent.callUiMethod(METHOD_SET_CONTENT_DESCRIPTION, new Object[]{name});
        }
    }
}
