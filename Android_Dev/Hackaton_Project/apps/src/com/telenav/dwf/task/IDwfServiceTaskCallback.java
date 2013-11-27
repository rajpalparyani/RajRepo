/**
 *
 * Copyright 2013 TeleNav, Inc. All rights reserved.
 * IDwfServiceTaskCallback.java
 *
 */
package com.telenav.dwf.task;

import com.telenav.dwf.vo.DwfResponse;

/**
 * @author fangquanm
 * @date Jul 9, 2013
 */
public interface IDwfServiceTaskCallback
{
    public boolean onPreExecute(int action);

    public void doInBackground(int action, DwfResponse response);

    public void onPostExecute(int action, DwfResponse response);
}
