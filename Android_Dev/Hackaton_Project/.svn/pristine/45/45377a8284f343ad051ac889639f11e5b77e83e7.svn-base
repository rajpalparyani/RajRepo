package com.telenav.tnui.widget.j2se;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;

import javax.swing.JDialog;

import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.core.INativeUiComponent;
import com.telenav.tnui.core.j2se.J2seRootPaneContainer;
import com.telenav.tnui.core.j2se.J2seUiHelper;
import com.telenav.tnui.graphics.AbstractTnGraphics;
import com.telenav.tnui.graphics.TnColor;
import com.telenav.tnui.widget.TnLinearContainer;
import com.telenav.tnui.widget.TnPopupContainer;

public class J2seDialog extends JDialog implements INativeUiComponent
{

    /**
     * 
     */
    private static final long serialVersionUID = 3750220242248813163L;

    protected TnPopupContainer popupContainer;
    
    private J2seRootPaneContainer rootPaneContainer;
    
    public J2seDialog(J2seRootPaneContainer rootPaneContainer, TnPopupContainer popupContainer)
    {
    	super((Frame)(rootPaneContainer.getRootContainer()),true);
        
        this.setUndecorated(true);
        
        this.rootPaneContainer = rootPaneContainer;
        this.popupContainer = popupContainer;
        this.initBackGroundDrawable();
        
        Frame frame = (Frame)(rootPaneContainer.getRootContainer()); 
        setBounds(frame.getX(),frame.getY(),frame.getWidth(),frame.getHeight());
    }
    
    private void initBackGroundDrawable()
    {
        setBackground(new Color(TnColor.TRANSPARENT, true));
    }
    
    public Object callUiMethod(int eventMethod, Object[] args)
    {
        switch (eventMethod)
        {
            case TnPopupContainer.METHOD_SHOW:
            {
                ((J2seUiHelper)J2seUiHelper.getInstance()).runOnUiThread(new Runnable()
                {
                    public void run()
                    {
                    	setVisible(true);
                    }
                });
                break;
            }
            case TnPopupContainer.METHOD_HIDE:
            {
                if(!this.isShowing())
                    return null;
                ((J2seUiHelper)J2seUiHelper.getInstance()).runOnUiThread(new Runnable()
                {
                    public void run()
                    {
                        setVisible(false);
                    }
                });
                break;
            }
            case TnPopupContainer.METHOD_SET_CONTENT:
            {
            	Container container = (Container)((AbstractTnComponent)args[0]).getNativeUiComponent();  
                setContentPane(container);
                
                break;
            }
        }

        return null;
    }

    public int getNativeHeight()
    {
        return getHeight();
    }

    public int getNativeWidth()
    {
        return getWidth();
    }

    public int getNativeX()
    {
    	return getX();
    }

    public int getNativeY()
    {
    	return getY();
    }

    public AbstractTnComponent getTnUiComponent()
    {
        return this.popupContainer;
    }

    public boolean isNativeFocusable()
    {
        return isFocusable();
    }

    public boolean isNativeVisible()
    {
        return isVisible();
    }

    public boolean requestNativeFocus()
    {
        return requestFocus(false);
    }

    public void invalidate()
    {
    	Dimension d = getContentPane().getPreferredSize();
    	int pwidth = (int)d.getWidth();
    	int pheight = (int)d.getHeight();
    	
    	int cwidth = getWidth();
    	int cheight = getHeight();
    	
    	if ((pwidth > 0 || pheight > 0)  && (cwidth != pwidth || cheight != pheight))
    	{
    		Frame frame = (Frame)(rootPaneContainer.getRootContainer()); 
    		
    		int width  = Math.min(cwidth, pwidth);
    		int height = Math.min(cheight,pheight);
    		
    	    setBounds((int)(frame.getX() +(frame.getWidth() - width) / 2) ,
            		  (int)(frame.getY() + (frame.getHeight() - height) /2),
            		  (int)width, (int)height);
    	}
    		
        super.invalidate();	
    }
    
    public void requestNativePaint()
    {
    	repaint();
    }

    public void setNativeFocusable(boolean isFocusable)
    {
        setFocusable(isFocusable);      
    }

    public void setNativeVisible(boolean isVisible)
    {
        setVisible(isVisible);
    }

}
