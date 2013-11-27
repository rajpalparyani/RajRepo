/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * RimUiBinder.java
 *
 */
package com.telenav.tnui.widget.rim;

import net.rim.device.api.ui.Manager;

import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.core.INativeUiComponent;
import com.telenav.tnui.core.rim.AbstractRimUiBinder;
import com.telenav.tnui.graphics.AbstractTnGraphics;
import com.telenav.tnui.opengles.TnGLUtils;
import com.telenav.tnui.opengles.TnMatrix4f;
import com.telenav.tnui.widget.TnAbsoluteContainer;
import com.telenav.tnui.widget.TnGLSurfaceField;
import com.telenav.tnui.widget.TnLinearContainer;
import com.telenav.tnui.widget.TnPopupContainer;
import com.telenav.tnui.widget.TnScrollPanel;
import com.telenav.tnui.widget.TnTextField;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2010-11-11
 */
public class RimUiBinder extends AbstractRimUiBinder
{
    protected RimUiBinder()
    {
    }

    public void init(Object context)
    {
        super.init(context);
        
        if(TnMatrix4f.getInstace() == null)
        {
            TnMatrix4f.init(new RimMatrix4f());
        }
        if(TnGLUtils.getInstance() == null)
        {
            TnGLUtils.init(new RimGLUtils());
        }
    }
    
    public INativeUiComponent bindNativeUiComponent(AbstractTnComponent component)
    {
        if (component instanceof TnLinearContainer)
        {
            TnLinearContainer tnLinearContainer = (TnLinearContainer)component;
            long gravity = Manager.NO_HORIZONTAL_SCROLL | Manager.NO_HORIZONTAL_SCROLLBAR | Manager.NO_VERTICAL_SCROLL | Manager.NO_VERTICAL_SCROLLBAR;
            int arg = tnLinearContainer.getAnchor();
            if( (arg & AbstractTnGraphics.LEFT) != 0)
            {
                gravity |= Manager.FIELD_LEFT;
            }
            
            if( (arg & AbstractTnGraphics.RIGHT) != 0)
            {
                gravity |= Manager.FIELD_RIGHT;
            }
            
            if( (arg & AbstractTnGraphics.TOP) != 0)
            {
                gravity |= Manager.FIELD_TOP;
            }
            
            if( (arg & AbstractTnGraphics.BOTTOM) != 0)
            {
                gravity |= Manager.FIELD_BOTTOM;
            }
            
            if( (arg & AbstractTnGraphics.HCENTER) != 0)
            {
                gravity |= Manager.FIELD_HCENTER;
            }
            
            if( (arg & AbstractTnGraphics.VCENTER) != 0)
            {
                gravity |= Manager.FIELD_VCENTER;
            }
            
            if(tnLinearContainer.isVertical())
            {
                RimVerticalFieldManager verticalManager = new RimVerticalFieldManager(tnLinearContainer, gravity);
                
                return verticalManager;
            }
            else
            {
                RimHorizontalFieldManager horizontalManager = new RimHorizontalFieldManager(tnLinearContainer, gravity);
                
                return horizontalManager;
            }
        }
        else if(component instanceof TnAbsoluteContainer)
        {
            RimAbsoluteFieldManager linearLayout = new RimAbsoluteFieldManager((TnAbsoluteContainer) component);

            return linearLayout;
        }
        else if (component instanceof TnScrollPanel)
        {
            TnScrollPanel tnScrollPanel = (TnScrollPanel)component;
            if(tnScrollPanel.isVertical())
            {
                long gravity = Manager.NO_HORIZONTAL_SCROLL | Manager.NO_HORIZONTAL_SCROLLBAR | Manager.VERTICAL_SCROLL | Manager.VERTICAL_SCROLLBAR;
                RimVerticalFieldManager verticalManager = new RimVerticalFieldManager(tnScrollPanel, gravity);

                return verticalManager;
            }
            else
            {
                long gravity = Manager.HORIZONTAL_SCROLL | Manager.HORIZONTAL_SCROLLBAR | Manager.NO_VERTICAL_SCROLL | Manager.NO_VERTICAL_SCROLLBAR;
                RimHorizontalFieldManager verticalManager = new RimHorizontalFieldManager(tnScrollPanel, gravity);

                return verticalManager;
            }
        }
        else if (component instanceof TnTextField)
        {
            return new RimTextField((TnTextField)component);
        }
        else if (component instanceof TnPopupContainer)
        {
            return new RimDialog(context,(TnPopupContainer) component);
        }
        else if(component instanceof TnGLSurfaceField)
        {
            return new RimGLField((TnGLSurfaceField)component);
        }
        
        return super.bindNativeUiComponent(component);
    }
}
