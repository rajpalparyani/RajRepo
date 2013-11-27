/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * MockMvcUiThreadHelper.java
 *
 */
package com.telenav.mvc;

/**
 *@author yning
 *@date 2011-6-21
 */
public class MvcUiThreadHelperMock implements IMvcUiThreadHelper
{

    public void runInUiThread(Runnable run)
    {
        run.run();
    }

}
