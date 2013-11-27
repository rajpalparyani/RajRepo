package com.telenav.tnui.widget.j2se;

import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.FocusEvent;

import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.core.INativeUiComponent;
import com.telenav.tnui.core.j2se.J2seBasicListener;
import com.telenav.tnui.core.j2se.J2seUiMethodHandler;
import com.telenav.tnui.graphics.TnColor;
import com.telenav.tnui.widget.TnScrollPanel;

public class J2seScrollPanel extends JScrollPane implements INativeUiComponent, ChangeListener
{
    /**
     * 
     */
    private static final long serialVersionUID = 8881888296945507035L;
    
    protected TnScrollPanel tnComponent;

    public J2seScrollPanel(TnScrollPanel tnComponent)
    {
        super(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

//        this.setBackground(new Color(TnColor.TRANSPARENT, true));
        this.getViewport().addChangeListener(this);
        this.tnComponent = tnComponent;
        
        J2seBasicListener listener = new J2seBasicListener(this.tnComponent);
        this.addFocusListener(listener);
        this.addMouseListener(listener);
        this.addMouseMotionListener(listener);
        this.addKeyListener(listener);
    }
    
    public void stateChanged(ChangeEvent e)
    {
        Point viewPosition = this.getViewport().getViewPosition();
        this.tnComponent.dispatchScrollChanged(viewPosition.x, viewPosition.y, 0, 0);
    }
    
    public void invalidate()
    {
        this.tnComponent.sublayout(this.getWidth(), this.getHeight());
        
        int prefWidth = this.tnComponent.getPreferredWidth();
        int prefHeight = this.tnComponent.getPreferredHeight();
        
        if(prefWidth > 0 && prefHeight > 0)
        {
            this.setSize(prefWidth, prefHeight);
        }
        
        super.invalidate();
    }

    public void setBounds(int x, int y, int width, int height)
    {
        if(this.getWidth() != width || this.getHeight() != height)
        {
            this.tnComponent.dispatchSizeChanged(width, height, this.getWidth(), this.getHeight());
        }
        
        super.setBounds(x, y, width, height);
    }
    
   /* public void paint(Graphics graphics)
    {
        Graphics g = graphics.create();
        Font font = g.getFont();
        Color color = g.getColor();
        
        AbstractTnGraphics.getInstance().setGraphics(g);
        this.tnComponent.draw(AbstractTnGraphics.getInstance());
        
        g.setFont(font);
        g.setColor(color);
        
        g.dispose();
        
        super.paint(graphics);
    }*/
    
    protected void processFocusEvent(FocusEvent e)
    {
        super.processFocusEvent(e);
        
        this.tnComponent.dispatchFocusChanged(e.getID() == FocusEvent.FOCUS_GAINED);
    }

    public Object callUiMethod(int eventMethod, Object[] args)
    {
        Object obj = J2seUiMethodHandler.callUiMethod(tnComponent, this, eventMethod, args);

        if (!J2seUiMethodHandler.NO_HANDLED.equals(obj))
            return obj;

        switch (eventMethod)
        {
            case TnScrollPanel.METHOD_GET:
            {
                Component component = this.getViewport().getView();
                if (component instanceof INativeUiComponent)
                {
                    return ((INativeUiComponent) component).getTnUiComponent();
                }
                break;
            }
            case TnScrollPanel.METHOD_SET:
            {
                AbstractTnComponent uiComponent = (AbstractTnComponent) args[0];
                this.setViewportView((Component) uiComponent.getNativeUiComponent());
                break;
            }
            case TnScrollPanel.METHOD_REMOVE:
            {
                this.getViewport().removeAll();
                break;
            }
            case TnScrollPanel.METHOD_SCROLL_TO:
            {
                this.getViewport().scrollRectToVisible(new Rectangle(((Integer)args[0]).intValue(), ((Integer)args[1]).intValue(), 60, 60));
                break;
            }
        }

        return null;
    }

    public int getNativeHeight()
    {
        return this.getHeight();
    }

    public int getNativeWidth()
    {
        return this.getWidth();
    }

    public int getNativeX()
    {
        return this.getX();
    }

    public int getNativeY()
    {
        return this.getY();
    }

    public AbstractTnComponent getTnUiComponent()
    {
        return this.tnComponent;
    }

    public boolean isNativeFocusable()
    {
        return this.isFocusable();
    }

    public boolean isNativeVisible()
    {
        return this.isVisible();
    }

    public boolean requestNativeFocus()
    {
        return this.requestFocusInWindow();
    }

    public void requestNativePaint()
    {
        this.repaint();
    }

    public void setNativeFocusable(boolean isFocusable)
    {
        this.setFocusable(isFocusable);
    }

    public void setNativeVisible(boolean isVisible)
    {
        this.setVisible(isVisible);
    }

}
