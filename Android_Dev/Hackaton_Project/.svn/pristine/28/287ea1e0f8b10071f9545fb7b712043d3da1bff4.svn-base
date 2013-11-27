/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * MockImageUiArgAdapter.java
 *
 */
package com.telenav.ui.mock;

import com.telenav.tnui.core.TnUiArgs.ITnUiArgsDecorator;
import com.telenav.tnui.core.TnUiArgs.TnUiArgAdapter;
import com.telenav.tnui.graphics.AbstractTnImage;

/**
 *@author hchai
 *@date 2011-11-7
 */
public class MockImageUiArgAdapter extends TnUiArgAdapter
{

    public MockImageUiArgAdapter(Object key, ITnUiArgsDecorator uiArgsDecorator)
    {
        super(key, uiArgsDecorator);
    }

    public AbstractTnImage getImage()
    {
        return new MockTnImage();
    }

    public int getInt()
    {
        return 0;
    }
}
