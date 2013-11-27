package com.telenav.tnui.widget.j2se;

import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.core.INativeUiComponent;
import com.telenav.tnui.core.j2se.AbstractJ2seUiBinder;
import com.telenav.tnui.widget.TnAbsoluteContainer;
import com.telenav.tnui.widget.TnGLSurfaceField;
import com.telenav.tnui.widget.TnLinearContainer;
import com.telenav.tnui.widget.TnPopupContainer;
import com.telenav.tnui.widget.TnScrollPanel;
import com.telenav.tnui.widget.TnTextField;
import com.telenav.tnui.widget.TnWebBrowserField;

public class J2seUiBinder extends AbstractJ2seUiBinder
{
    protected J2seUiBinder()
    {
    }

    public INativeUiComponent bindNativeUiComponent(AbstractTnComponent component)
    {
        if (component instanceof TnLinearContainer)
        {
            return new J2seLinearLayout((TnLinearContainer) component);
        }
        else if (component instanceof TnScrollPanel)
        {
            return new J2seScrollPanel((TnScrollPanel)component);
        }
        else if (component instanceof TnTextField)
        {
            return new J2seTextField((TnTextField)component);
        }
        else if (component instanceof TnPopupContainer)
        {
            return new J2seDialog(this.rootPaneContainer, (TnPopupContainer)component);
        }
        else if(component instanceof TnAbsoluteContainer)
        {
            J2seAbsoluteLayout layout = new J2seAbsoluteLayout((TnAbsoluteContainer) component);
            return layout;
        }
        else if(component instanceof TnGLSurfaceField)
        {
            //return new J2seSurfaceField((TnGLSurfaceField)component);
        }
        else if(component instanceof TnWebBrowserField)
        {
            //return new J2seWebView((TnWebBrowserField)component);
        }
        
        
        return super.bindNativeUiComponent(component);
    }
}
