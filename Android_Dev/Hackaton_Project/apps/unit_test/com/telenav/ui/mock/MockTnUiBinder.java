/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * A.java
 *
 */
package com.telenav.ui.mock;

import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.core.AbstractTnUiBinder;
import com.telenav.tnui.core.INativeUiAnimation;
import com.telenav.tnui.core.INativeUiComponent;
import com.telenav.tnui.core.TnUiAnimationContext;

/**
 *@author hchai
 *@date 2011-11-7
 */
public class MockTnUiBinder extends AbstractTnUiBinder
{

    public void init(Object context)
    {

    }

    public INativeUiComponent bindNativeUiComponent(AbstractTnComponent component)
    {
        return new MockNativeUiComponent();
    }

    public INativeUiAnimation bindNativeUiAnimation(TnUiAnimationContext animationContext)
    {
        return null;
    }

}
