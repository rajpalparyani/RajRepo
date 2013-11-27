/**
 *
 * Copyright 2013 TeleNav, Inc. All rights reserved.
 * NativeShareModel.java
 *
 */
package com.telenav.module.nativeshare;

import android.content.Context;

import com.telenav.app.CommManager;
import com.telenav.app.TeleNavDelegate;
import com.telenav.app.android.AndroidPersistentContext;
import com.telenav.app.android.scout_us.R;
import com.telenav.comm.ICommCallback;
import com.telenav.data.datatypes.address.Address;
import com.telenav.data.serverproxy.AbstractServerProxy;
import com.telenav.data.serverproxy.IServerProxyConstants;
import com.telenav.data.serverproxy.IServerProxyListener;
import com.telenav.data.serverproxy.impl.IS4AProxy;
import com.telenav.data.serverproxy.impl.ServerProxyFactory;
import com.telenav.data.serverproxy.impl.json.JsonS4AProxy;
import com.telenav.module.nav.NavRunningStatusProvider;
import com.telenav.module.share.ShareManager;
import com.telenav.module.share.ShareManager.IShareCallback;
import com.telenav.mvc.AbstractCommonNetworkModel;
import com.telenav.res.IStringCommon;
import com.telenav.res.ResourceManager;
import com.telenav.tnui.core.AbstractTnUiHelper;
import com.telenav.util.PrimitiveTypeCache;

/**
 * @author wchshao
 * @date Mar 18, 2013
 */
class NativeShareModel extends AbstractCommonNetworkModel implements IShareCallback, IServerProxyListener, INativeShareConstants
{
    @Override
    protected void doActionDelegate(int actionId)
    {
        switch (actionId)
        {
            case ACTION_INIT:
            {
                if (this.get(KEY_O_SELECTED_ADDRESS) != null)
                {
                    this.postModelEvent(EVENT_MODEL_NATIVE_SHARE_REQUEST_TINY_URL);
                }
                else
                {
                    this.postModelEvent(EVENT_MODEL_START_NATIVE_SHARE);
                }
                break;
            }
            case ACTION_NATIVE_SHARE:
            {
                ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).runOnUiThread(new Runnable()
                {
                    public void run()
                    {
                        TeleNavDelegate.getInstance().callAppNativeFeature(TeleNavDelegate.FEATURE_UPDATE_WINDOW_SOFT_INPUT_MODE,
                            new Object[]{PrimitiveTypeCache.valueOf(false), PrimitiveTypeCache.valueOf(false), PrimitiveTypeCache.valueOf(true)});
                    }
                });
                Context context = AndroidPersistentContext.getInstance().getContext();
                if (this.get(KEY_O_SELECTED_ADDRESS) != null)
                {
                    String title = context.getString(R.string.share_location_title);
                    String tinyURL = this.getString(KEY_S_TINY_URL);
                    ShareManager.getInstance().startShareLocation(title, (Address) this.get(KEY_O_SELECTED_ADDRESS), tinyURL, this);
                }
                else
                {
                    String title = context.getString(R.string.share_scout_title);
                    ShareManager.getInstance().startShareScout(title, this);
                }
                break;
            }
            case ACTION_NATIVE_SHARE_REQUEST_TINY_URL:
            {
                requestTinyURL();
                break;
            }
            default:
                break;
        }
    }
    
    protected void requestTinyURL()
    {
        Address currentAddress = (Address) this.get(KEY_O_SELECTED_ADDRESS);
        if (currentAddress != null)
        {
            IS4AProxy s4Is4aProxy = ServerProxyFactory.getInstance().createS4AProxy(null,
                CommManager.getInstance().getComm(), this, null);

            float lat = (float) currentAddress.getStop().getLat() / 100000;
            float lon = (float) currentAddress.getStop().getLon() / 100000;

            String address = ResourceManager.getInstance().getStringConverter().convertAddress(currentAddress.getStop(), false);
            s4Is4aProxy.requestTinyUrl(address, String.valueOf(lat), String.valueOf(lon),
                currentAddress.getLabel(), NavRunningStatusProvider.getInstance().isNavRunning());
        }
    }

    @Override
    public void onShareResult(boolean success)
    {
        ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).runOnUiThread(new Runnable()
        {
            public void run()
            {
                TeleNavDelegate.getInstance().callAppNativeFeature(
                    TeleNavDelegate.FEATURE_UPDATE_WINDOW_SOFT_INPUT_MODE, new Object[]{PrimitiveTypeCache.valueOf(false), PrimitiveTypeCache.valueOf(false),PrimitiveTypeCache.valueOf(true)});
            }
        });
        if (success)
        {
            Address address = (Address) this.get(KEY_O_SELECTED_ADDRESS);
            if(address != null && address.getStop() != null)
            {
                Context context = AndroidPersistentContext.getInstance().getContext();
                this.put(KEY_S_COMMON_MESSAGE, context.getString(R.string.addplace_palce_shared_prompt, address.getStop().getFirstLine()));
            }
            this.postModelEvent(EVENT_MODEL_NATIVE_SHARE_FINISH);
        }
        else
        {
            this.postModelEvent(EVENT_MODEL_NATIVE_SHARE_CANCEL);
        }
    }

    @Override
    protected void transactionFinishedDelegate(AbstractServerProxy proxy, String jobId)
    {
        String action = proxy.getRequestAction();
        if (IServerProxyConstants.ACT_GET_TINY_URL.equals(action))
        {
            int status = ((JsonS4AProxy) proxy).getStatus();
            if (status == ICommCallback.SUCCESS)
            {
                // got tiny url
                String tinyUrl = ((JsonS4AProxy) proxy).getTinyUrl();
                this.put(KEY_S_TINY_URL, tinyUrl);
                this.postModelEvent(EVENT_MODEL_START_NATIVE_SHARE);
            }
            else
            {
                String errorMessage = proxy.getErrorMsg();
                if (errorMessage == null || errorMessage.length() == 0)
                {
                    errorMessage = ResourceManager.getInstance().getCurrentBundle().getString(IStringCommon.RES_SERVER_ERROR, IStringCommon.FAMILY_COMMON);
                }
                postErrorMessage(errorMessage);
            }
        }
    }

}
