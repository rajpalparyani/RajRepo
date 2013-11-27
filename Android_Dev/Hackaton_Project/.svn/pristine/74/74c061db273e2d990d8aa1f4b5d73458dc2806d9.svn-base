/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * MaiTaiApp.java
 *
 */
package com.telenav.sdk.maitai.impl;

import java.util.Hashtable;

import android.content.Context;

import com.telenav.app.IApplicationListener;
import com.telenav.app.TeleNavDelegate;
import com.telenav.app.android.AndroidPersistentContext;
import com.telenav.app.android.ExitNavSessionConfirmer;
import com.telenav.app.android.ExitNavSessionConfirmer.INavSessionEndListener;
import com.telenav.data.dao.misc.DaoManager;
import com.telenav.log.mis.IMisLogConstants;
import com.telenav.module.ModuleFactory;
import com.telenav.module.nav.NavRunningStatusProvider;
import com.telenav.module.nav.NavSdkNavEngine;
import com.telenav.mvc.AbstractCommonController;
import com.telenav.mvc.AbstractController;
import com.telenav.mvc.ITriggerConstants;
import com.telenav.sdk.maitai.IMaiTaiParameter;
import com.telenav.sdk.maitai.IMaiTaiStatusConstants;

/**
 * MaiTai Entry
 *@author qli (qli@telenav.cn)
 *@date 2010-12-15
 */
public class MaiTaiManager implements IApplicationListener
{
    public static final String PARAM_URI   = "REQUEST_URI";
    private static MaiTaiManager instance = new MaiTaiManager();
    private boolean isFromMaiTai = false;
    private boolean isMaiTaiExit = false;
    private MaiTaiParameter maitaiParameter = null;
    
    private MaiTaiManager()
    {
        TeleNavDelegate.getInstance().registerApplicationListener(this);
    }
    
    private void initMaiTai()
    {
        maitaiParameter = new MaiTaiParameter();
    }
    
    
    public static MaiTaiManager getInstance()
    {
        return instance;
    }

    
    public MaiTaiParameter getMaiTaiParameter()
    {
        return maitaiParameter;
    }
    
    public void setUri(String uri)
    {
        MaiTaiHandler.getInstance().setUri(uri);
    }

    public void setBackupUri(String uri)
    {
        MaiTaiHandler.getInstance().setBackupUri(uri);
    }

    public void setFromMaiTai(boolean maitai)
    {
        isFromMaiTai = maitai;
        isMaiTaiExit = maitai;
        if( isFromMaiTai )
        {
            initMaiTai();
        }
    }
    
    public void setNeedValidate(boolean needValidate)
    {
        MaiTaiHandler.getInstance().setNeedValidata(needValidate);
    }

    public void appDeactivated(String[] params)
    {
        // TODO Auto-generated method stub
    }

    public void appActivated(String[] params)
    {
        if( isFromMaiTai )
        {
            if( DaoManager.getInstance().getBillingAccountDao().isLogin() )
            {
                startMaiTai();
            }
        }
    }

    public void startMaiTai()
    {
        boolean isNavRunning = NavSdkNavEngine.getInstance().isRunning();
        if(isNavRunning)
        {
            Context context = AndroidPersistentContext.getInstance().getContext();
            ExitNavSessionConfirmer.getInstance().showExitNavSessionConfirm(context, new INavSessionEndListener()
            {
                @Override
                public void onNavSessionEnd()
                {
                    startMaiTaiDelegate();
                }
                
                @Override
                public void onCancel()
                {
                    // TODO Auto-generated method stub
                    MaiTaiHandler.getInstance().clear();
                    isFromMaiTai = false;
                }
            });
        }
        else
        {
            startMaiTaiDelegate();
        }
        
    }
    
    protected void startMaiTaiDelegate()
    {
        AbstractController controller = AbstractController.getCurrentController();
        controller.handleModelEvent(ITriggerConstants.EVENT_MODEL_RELEASE_ALL_PREVIOUS_MODULES);
        controller.release();
        
        NavRunningStatusProvider.getInstance().setNavRunningEnd();
        
        ModuleFactory.getInstance().startMain();
        isFromMaiTai = false;
    }
    
    public void launchMaiTai(AbstractCommonController controller)
    {
        ModuleFactory.getInstance().startMaiTaiController(controller);
        isFromMaiTai = false;
    }

    public boolean isFromMaiTai()
    {
        return isFromMaiTai;
    }
    
    public boolean isMaiTaiExit()
    {
        return isMaiTaiExit;
    }
    
    
    public void finish()
    {
        finish(null);
    }

    protected void finish(Hashtable response)
    {
        if (response == null)
        {
            response = new Hashtable();
        }
        if (response.size() == 0 || !response.containsKey(IMaiTaiParameter.KEY_STATUS))
        {
            response.put(IMaiTaiParameter.KEY_STATUS, "" + IMaiTaiStatusConstants.STATUS_SUCCESS);
        }
        
        MaiTaiHandler.getInstance().clear();
        maitaiParameter = null;

        TeleNavDelegate.getInstance().exitApp(true, response);
    }
    
    public String getStartByInMaiTai()
    {
        if (getMaiTaiParameter().getWidgetType() != null)
        {
            return getMaiTaiParameter().getWidgetType();
        }
        else
        {
            String actioName = getMaiTaiParameter().getAction();
            if (IMaiTaiParameter.ACTION_MAP.equalsIgnoreCase(actioName))
            {
                return IMisLogConstants.VALUE_BY_MAPS_MAITAI;
            }
            else if (IMaiTaiParameter.ACTION_DIRECTIONS.equalsIgnoreCase(actioName))
            {
                return IMisLogConstants.VALUE_BY_DIRECTION_MAITAI;
            }
            else if (IMaiTaiParameter.ACTION_NAVTO.equalsIgnoreCase(actioName))
            {
                return IMisLogConstants.VALUE_BY_DRIVE_MAITAI;
            }
            else
                return IMisLogConstants.VALUE_BY_MANUAL;
        }
    }
}
