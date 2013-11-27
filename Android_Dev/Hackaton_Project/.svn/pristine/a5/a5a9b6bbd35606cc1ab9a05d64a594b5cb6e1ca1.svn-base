/**
 *
 * Copyright 2013 TeleNav, Inc. All rights reserved.
 * DwfServiceTask.java
 *
 */
package com.telenav.dwf.task;

import android.os.AsyncTask;

import com.telenav.dwf.aidl.DwfAidl;
import com.telenav.dwf.aidl.DwfRequestAidl;
import com.telenav.dwf.aidl.DwfResponseAidl;
import com.telenav.dwf.aidl.DwfServiceConnection;
import com.telenav.dwf.vo.DwfRequest;
import com.telenav.dwf.vo.DwfResponse;
import com.telenav.dwf.vo.DwfResponseStatus;

/**
 * @author fangquanm
 * @date Jul 9, 2013
 */
public class DwfServiceTask extends AsyncTask<DwfRequest, Integer, DwfResponse>
{
    private int action;

    private IDwfServiceTaskCallback callback;

    private boolean isCancelled;

    private boolean onPreExecute;

    public DwfServiceTask(int action, IDwfServiceTaskCallback callback)
    {
        this.action = action;
        this.callback = callback;
    }

    @Override
    protected void onPreExecute()
    {
        onPreExecute = this.callback.onPreExecute(action);
    }

    @Override
    protected DwfResponse doInBackground(DwfRequest... params)
    {
        if (this.isCancelled)
            return null;

        DwfResponse resp = null;
        resp = new DwfResponse();

        try
        {
            DwfAidl dwfAidl = DwfServiceConnection.getInstance().getConnection();

            if (dwfAidl == null)
            {
                resp.setStatus(DwfResponseStatus.EXCEPTION);
            }
            else
            {
                if (onPreExecute)
                {
                    DwfRequestAidl requestAidl = new DwfRequestAidl();
                    requestAidl.setRequest(params[0]);
                    DwfResponseAidl responseAidl = dwfAidl.request(requestAidl);

                    if (responseAidl != null)
                    {
                        resp = responseAidl.getResponse();
                    }
                    else
                    {
                        resp.setStatus(DwfResponseStatus.EXCEPTION);
                    }
                }
                else
                {
                    resp.setStatus(DwfResponseStatus.EXCEPTION);
                }
            }
        }
        catch (Throwable e)
        {
            e.printStackTrace();

            resp.setStatus(DwfResponseStatus.EXCEPTION);
        }
        finally
        {
            if (!this.isCancelled)
            {
                this.callback.doInBackground(action, resp);
            }
        }

        return resp;
    }

    @Override
    protected void onPostExecute(DwfResponse result)
    {
        if (this.isCancelled)
            return;

        this.callback.onPostExecute(action, result);
    }

    public void cancel()
    {
        this.isCancelled = true;

        this.cancel(true);
    }
}
