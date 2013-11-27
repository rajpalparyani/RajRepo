/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * AndroidUiBinder.java
 *
 */
package com.telenav.tnui.widget.android;

import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.core.INativeUiAnimation;
import com.telenav.tnui.core.INativeUiComponent;
import com.telenav.tnui.core.TnUiAnimationContext;
import com.telenav.tnui.core.android.AbstractAndroidUiBinder;
import com.telenav.tnui.opengles.TnGLUtils;
import com.telenav.tnui.opengles.TnMatrix4f;
import com.telenav.tnui.widget.TnAbsoluteContainer;
import com.telenav.tnui.widget.TnGLSurfaceField;
import com.telenav.tnui.widget.TnLinearContainer;
import com.telenav.tnui.widget.TnPopupContainer;
import com.telenav.tnui.widget.TnScrollPanel;
import com.telenav.tnui.widget.TnTextField;
import com.telenav.tnui.widget.TnWebBrowserField;

/**
 * This class will bind tn ui with native ui of android.
 * 
 *@author fqming (fqming@telenav.cn)
 *@date 2010-6-17
 */
public class AndroidUiBinder extends AbstractAndroidUiBinder
{
    protected AndroidUiBinder()
    {
    }

    public void init(Object context)
    {
        super.init(context);
        
        if(TnMatrix4f.getInstace() == null)
        {
            TnMatrix4f.init(new AndroidMatrix4f());
        }
        if(TnGLUtils.getInstance() == null)
        {
            TnGLUtils.init(new AndroidGLUtils());
        }
    }
    
    /**
     * FIXME for Android platform, we can use reflection here!!!
     */
    public INativeUiComponent bindNativeUiComponent(AbstractTnComponent component)
    {
        if (component instanceof TnLinearContainer)
        {
            AndroidLinearLayout linearLayout = new AndroidLinearLayout(context, (TnLinearContainer) component);

            return linearLayout;
        }
        else if(component instanceof TnAbsoluteContainer)
        {
            AndroidAbsoluteLayout linearLayout = new AndroidAbsoluteLayout(context, (TnAbsoluteContainer) component);

            return linearLayout;
        }
        else if (component instanceof TnScrollPanel)
        {
            TnScrollPanel tnScrollPanel = (TnScrollPanel)component;
            if(tnScrollPanel.isVertical())
            {
                AndroidVerticalScrollPanel verticalScrollPanel = new AndroidVerticalScrollPanel(context, tnScrollPanel);
                
                return verticalScrollPanel;
            }
            else
            {
                AndroidHorizontalScrollPanel horizontalScrollPanel = new AndroidHorizontalScrollPanel(context, tnScrollPanel);
                
                return horizontalScrollPanel;
            }
        }
        else if (component instanceof TnTextField)
        {
            return new AndroidTextField(context, (TnTextField)component);
        }
        else if (component instanceof TnPopupContainer)
        {
            return new AndroidDialog(context, (TnPopupContainer) component);
        }
        else if (component instanceof TnGLSurfaceField)
        {
            int osVersion = android.os.Build.VERSION.SDK_INT;
            //14 :ICE_CREAM_SANDWICH 
            //15 :ICE_CREAM_SANDWICH_MR1
            //Sdk version < 16, we will overwrite onPause and onResume
            //Sdk version >= 16, we will use native GLSurfaceView and setPreserveEGLContextOnPause by true
            //TN OpenGL Map engine request client to keep EGLContext when surface destroyed
            if (osVersion < 16)
            {
                return new AndroidGLSurfaceField(context, (TnGLSurfaceField) component);
            }
            else
            {
                return new AndroidGLSurfaceField16(context, (TnGLSurfaceField) component);
            }
        }
        else if(component instanceof TnWebBrowserField)
        {
            return new AndroidWebView(context, (TnWebBrowserField)component);
        }
        
        return super.bindNativeUiComponent(component);
    }
}
