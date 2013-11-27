/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * MockNinePatchImageDecorator.java
 *
 */
package com.telenav.ui.mock;

import com.telenav.tnui.core.TnUiArgs.TnUiArgAdapter;
import com.telenav.tnui.graphics.TnNinePatchImage;
import com.telenav.ui.NinePatchImageDecorator;

/**
 *@author hchai
 *@date 2011-11-10
 */
public class MockNinePatchImageDecorator extends NinePatchImageDecorator
{
    private MockTnImage mockTnImage = new MockTnImage();
    
    public Object decorate(TnUiArgAdapter args)
    {
        return new TnNinePatchImage(mockTnImage,mockTnImage,mockTnImage,mockTnImage,mockTnImage,mockTnImage,mockTnImage,mockTnImage,mockTnImage);
    }
    
}