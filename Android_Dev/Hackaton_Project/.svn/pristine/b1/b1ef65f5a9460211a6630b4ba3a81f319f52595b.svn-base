package com.telenav.tnui.widget.j2se;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.JComponent;
import javax.swing.JPanel;

import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.core.INativeUiComponent;
import com.telenav.tnui.core.INativeUiContainer;
import com.telenav.tnui.core.TnUiArgs;
import com.telenav.tnui.core.j2se.J2seBasicListener;
import com.telenav.tnui.core.j2se.J2seUiMethodHandler;
import com.telenav.tnui.graphics.AbstractTnGraphics;
import com.telenav.tnui.widget.TnAbsoluteContainer;

public class J2seAbsoluteLayout extends JPanel implements INativeUiContainer
{
    private static final long serialVersionUID = 3252039665242120360L;
    
    protected TnAbsoluteContainer tnContainer;

    protected float anchorX;
    protected float anchorY;
    
    /**
     * The constructor of this class.
     * 
     * @param context
     * @param tnContainer
     */
    public J2seAbsoluteLayout(TnAbsoluteContainer tnContainer)
    {
        super();

        this.tnContainer = tnContainer;
        
        J2seBasicListener listener = new J2seBasicListener(this.tnContainer);
        this.addFocusListener(listener);
        this.addMouseListener(listener);
        this.addMouseMotionListener(listener);
        this.addKeyListener(listener);
        
        this.setLayout(new AbsoluteLayoutManager());
    }

    public void invalidate()
    {
        this.tnContainer.sublayout(this.getWidth(), this.getHeight());
        
        int prefWidth = this.tnContainer.getPreferredWidth();
        int prefHeight = this.tnContainer.getPreferredHeight();
        
        if(prefWidth > 0 && prefHeight > 0)
        {
            setSize(prefWidth, prefHeight);
        }
    	
        super.invalidate();
    }
   
    public void setBounds(int x, int y, int width, int height)
    {
        if(this.getWidth() != width || this.getHeight() != height)
        {
            tnContainer.dispatchSizeChanged(width, height, this.getWidth(), this.getHeight());
            
            //setPreferredSize(new Dimension(width, height));
        }
        
        super.setBounds(x, y, width, height);
    }
    
    public void paint(Graphics g)
    {
        Font font = g.getFont();
        Color color = g.getColor();
        
        AbstractTnGraphics.getInstance().setGraphics(g);
        this.tnContainer.draw(AbstractTnGraphics.getInstance());
        
        g.setFont(font);
        g.setColor(color);
        
        super.paint(g);
    }
    
    public void addNativeComponent(AbstractTnComponent uiComponent)
    {
        JComponent component = (JComponent)uiComponent.getNativeUiComponent();
        add(component);
    }

    public void addNativeComponent(AbstractTnComponent uiComponent, int index)
    {
        JComponent component = (JComponent)uiComponent.getNativeUiComponent();
        this.add(component, index);
    }
    
    public int getChildrenSize()
    {
        return this.getComponentCount();
    }

    public AbstractTnComponent getFocusedComponent()
    {
        for(int i = 0; i < this.getComponentCount(); i++)
        {
            if(this.getComponents()[i].isFocusOwner())
            {
                return ((INativeUiComponent)this.getComponents()[i]).getTnUiComponent();
            }
        }
        
        return null;
    }

    public AbstractTnComponent getNativeComponent(int index)
    {
        Component component = this.getComponent(index);
        if(component instanceof INativeUiComponent)
        {
            return ((INativeUiComponent)component).getTnUiComponent();
        }
        
        return null;
    }

    public void removeAllNativeComponents()
    {
        this.removeAll();
    }

    public void removeNativeComponent(AbstractTnComponent uiComponent)
    {
        this.remove((Component)uiComponent.getNativeUiComponent());
    }

	public void removeNativeComponentInLayout(AbstractTnComponent uiComponent)
    {

    }

    public void removeNativeComponent(int index)
    {
        this.remove(index);
    }

    public Object callUiMethod(int eventMethod, Object[] args)
    {
        Object obj = J2seUiMethodHandler.callUiMethod(tnContainer, this, eventMethod, args);

        if (!J2seUiMethodHandler.NO_HANDLED.equals(obj))
            return obj;
        
        switch (eventMethod)
        {
           
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
        return this.tnContainer;
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

    public int indexOfComponent(AbstractTnComponent uiComponent)
    {
        for(int i = 0; i < this.getComponentCount(); i++)
        {
            if(this.getComponents()[i].equals((Component)uiComponent.getNativeUiComponent()))
            {
                return i;
            }
        }
        
        return -1;
    }
}
