/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * TestProxyModel.java
 *
 */
package com.telenav.module.testproxy;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;

import com.telenav.app.CommManager;
import com.telenav.comm.Comm;
import com.telenav.comm.Host;
import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.dao.serverproxy.BillingAccountDao;
import com.telenav.data.serverproxy.AbstractServerProxy;
import com.telenav.data.serverproxy.IServerProxyConstants;
import com.telenav.data.serverproxy.IServerProxyListener;
import com.telenav.data.serverproxy.IUserProfileProvider;
import com.telenav.data.serverproxy.impl.ILoginProxy;
import com.telenav.data.serverproxy.impl.ISettingDataProxy;
import com.telenav.data.serverproxy.impl.ISyncPurchaseProxy;
import com.telenav.data.serverproxy.impl.IToolsProxy;
import com.telenav.data.serverproxy.impl.IUpsellProxy;
import com.telenav.data.serverproxy.impl.protobuf.ProtoBufLoginProxy;
import com.telenav.data.serverproxy.impl.protobuf.ProtoBufSettingDataProxy;
import com.telenav.data.serverproxy.impl.protobuf.ProtoBufSyncPurchaseProxy;
import com.telenav.data.serverproxy.impl.protobuf.ProtoBufToolsProxy;
import com.telenav.data.serverproxy.impl.protobuf.ProtoBufUpsellProxy;
import com.telenav.j2me.datatypes.ProtocolBuffer;
import com.telenav.j2me.framework.protocol.ProtoLoginResp;
import com.telenav.j2me.framework.protocol.ProtoPurchaseResp;
import com.telenav.j2me.framework.protocol.ProtoSyncClientSettingDataResp;
import com.telenav.j2me.framework.protocol.ProtoSyncPreferenceResp;
import com.telenav.j2me.framework.protocol.ProtoSyncPurchasedResp;
import com.telenav.j2me.framework.protocol.ProtoUpsellResp;
import com.telenav.module.UserProfileProvider;
import com.telenav.module.sync.SyncResExecutor;
import com.telenav.module.upsell.FeaturesManager;
import com.telenav.module.upsell.UpsellOption;
import com.telenav.mvc.AbstractCommonNetworkModel;

/**
 *@author yning
 *@date 2012-11-21
 */
public class TestProxyModel extends AbstractCommonNetworkModel implements ITestProxyConstants
{
    
    String[] options = new String[]{"Login", "Sync Purchase", "Sync Preference", "SyncSettingData", "Upsell function"};
    int[] events = new int[]{CMD_LOGIN, CMD_SYNC_PURCHASE, CMD_SYNC_PREFERENCE, CMD_SYNC_SETTING_DATA, CMD_SHOW_UPSELL_MAIN};
    IUserProfileProvider upsellUserProfile;
    
    public TestProxyModel()
    {
        put(KEY_A_INT_CMDS, events);
        put(KEY_A_STRING_OPTIONS, options);
    }
    
    @Override
    protected void transactionFinishedDelegate(AbstractServerProxy proxy, String jobId)
    {
        processTransaction(proxy);
    }

    @Override
    protected void doActionDelegate(int actionId)
    {
        switch(actionId)
        {
            case ACTION_DO_LOGIN:
            {
                doLogin(false);
                break;
            }
            case ACTION_DO_SYNC_PURCHASED:
            {
                doSyncPurchased();
                break;
            }
            case ACTION_DO_SYNC_PREFERENCE:
            {
                doSyncPreference();
                break;
            }
            case ACTION_DO_SYNC_SETTING_DATA:
            {
                doSyncSettingData();
                break;
            }
            case ACTION_DO_GET_UPSELL_INFO:
            {
                doLogin(true);
                
                break;
            }
            case ACTION_DO_PURCHASE:
            {
                purchase();
                break;
            }
        }
    }

    protected void doLogin(boolean isUpsell)
    {
        IUserProfileProvider userProfileProvider;
        if(isUpsell)
        {
            upsellUserProfile = new UserProfileProvider();
            String ptn = getString(KEY_S_PTN);
            upsellUserProfile = new UserProfileProvider();
            upsellUserProfile.getMandatoryNode().getUserInfo().phoneNumber = ptn;
            upsellUserProfile.getMandatoryNode().getUserInfo().ptnType = BillingAccountDao.PTN_TYPE_TYPEIN;
            upsellUserProfile.getMandatoryNode().getUserInfo().userId = "";
            upsellUserProfile.getMandatoryNode().getUserInfo().pin = "";
            upsellUserProfile.getMandatoryNode().getUserInfo().eqpin = "";
            upsellUserProfile.getMandatoryNode().getCredentialInfo().credentialID = "";
            upsellUserProfile.getMandatoryNode().getCredentialInfo().credentialPassword = "";
            upsellUserProfile.getMandatoryNode().getCredentialInfo().credentialType = "";
            upsellUserProfile.getMandatoryNode().getCredentialInfo().credentialValue = "";
            
            userProfileProvider = upsellUserProfile;
        }
        else
        {
            userProfileProvider = null;
        }
        
        
        String ptnType = DaoManager.getInstance().getMandatoryNodeDao().getMandatoryNode().getUserInfo().ptnType;
        boolean isEncrypted = false;
        if(isUpsell)
        {
            isEncrypted = false;
        }
        else if(BillingAccountDao.PTN_TYPE_ENCRYPTED.equals(ptnType))
        {
            isEncrypted = true;
        }
        
        int loginType = ILoginProxy.FUTE_SIGN_IN;
        
        ILoginProxy loginProxy = new MyLoginProxy(null, CommManager.getInstance().getComm(), this, userProfileProvider);
        loginProxy.sendLogin(loginType, "", isEncrypted);
    }
    
    protected void doSyncPurchased()
    {
        ISyncPurchaseProxy syncPurchaseProxy = new MySyncPurchaseProxy(null, CommManager.getInstance().getComm(), this, null);
        syncPurchaseProxy.sendSyncPurchaseRequest(FeaturesManager.APP_CODE);
    }
    
    protected void doSyncPreference()
    {
        IToolsProxy toolsProxy = new MyToolsProxy(null, CommManager.getInstance().getComm(), this, null);
        toolsProxy.syncPreference(IToolsProxy.SYNC_TYPE_DOWNLOAD, new Hashtable(), -1);
    }
    
    protected void doSyncSettingData()
    {
        SyncResExecutor.getInstance().syncSettingData(null, null, this, ISettingDataProxy.SYNC_TYPE_FORCE_DOWNLOAD);
    }
    
    protected void getUpsellInfo(boolean isNeedActiveOffer)
    {
        IUpsellProxy upsellProxy = new MyUpsellProxy(null, CommManager.getInstance().getComm(), this, upsellUserProfile);
        upsellProxy.requestUpsellInfo(isNeedActiveOffer);
    }
    
    protected void purchase()
    {
        int index = getInt(KEY_I_OPTION_INDEX);
        Vector options = getVector(KEY_V_UPSELL_OPTIONS);
        UpsellOption op = null;
        if(index > -1 && index < options.size())
        {
            op = (UpsellOption)options.elementAt(index);
            IUpsellProxy upsellProxy = new MyUpsellProxy(null, CommManager.getInstance().getComm(), this, upsellUserProfile);
            upsellProxy.submitUpsellSelection(op.getOfferCode());
        }
    }
    
    public void transactionError(AbstractServerProxy proxy)
    {
        processTransaction(proxy);
    }
    
    protected void processTransaction(AbstractServerProxy proxy)
    {
        String action = proxy.getRequestAction();
        if(getBool(KEY_B_IS_IN_UPSELL_SESSION))
        {
            if(IServerProxyConstants.ACT_UPSELL.equals(action))
            {
                Vector options = ((IUpsellProxy)proxy).getUpsellOptions();
                put(KEY_V_UPSELL_OPTIONS, options);
                postModelEvent(EVENT_MODEL_SHOW_UPSELL_OPTIONS);
            }
            else if(IServerProxyConstants.ACT_LOGIN.equals(action))
            {
                getUpsellInfo(true);
            }
            else if(IServerProxyConstants.ACT_SINGLE_PURCHASE.equals(action))
            {
                printResult(proxy);
            }
        }
        else if(proxy instanceof IMyProxy)
        {
            printResult(proxy);
        }
        
    }
    
    protected void printResult(AbstractServerProxy proxy)
    {
        IMyProxy myProxy = ((IMyProxy)proxy);
        Vector result = myProxy.getResult();
        
        put(KEY_V_REQUEST_RESULT, result);
        postModelEvent(EVENT_MODEL_SHOW_RESULT);
    }
    
    interface IMyProxy
    {
        public Vector getResult();
    }
    
    static class MyLoginProxy extends ProtoBufLoginProxy implements IMyProxy
    {
        Vector result = new Vector();
        public MyLoginProxy(Host host, Comm comm,
                IServerProxyListener serverProxyListener,
                IUserProfileProvider userProfileProvider)
        {
            super(host, comm, serverProxyListener, userProfileProvider);
        }
        
        protected int parseRequestSpecificData(ProtocolBuffer protoBuf)
                throws IOException
        {
            if (IServerProxyConstants.ACT_LOGIN.equals(protoBuf.getObjType()))
            {
                ProtoLoginResp resp = ProtoLoginResp.parseFrom(protoBuf.getBufferData());
                result.addElement(resp);
            }
            return super.parseRequestSpecificData(protoBuf);
        }
        
        public Vector getResult()
        {
            return result;
        }
        
    }
    
    static class MyToolsProxy extends ProtoBufToolsProxy implements IMyProxy
    {
        Vector result = new Vector();
        public MyToolsProxy(Host host, Comm comm,
                IServerProxyListener serverProxyListener,
                IUserProfileProvider userProfileProvider)
        {
            super(host, comm, serverProxyListener, userProfileProvider);
        }

        @Override
        protected int parseRequestSpecificData(ProtocolBuffer protoBuf)
                throws IOException
        {
            if (IServerProxyConstants.ACT_SYNC_PREFERENCE.equals(protoBuf.getObjType()))
            {
                ProtoSyncPreferenceResp resp = ProtoSyncPreferenceResp.parseFrom(protoBuf.getBufferData());
                result.addElement(resp);
            }
            return super.parseRequestSpecificData(protoBuf);
        }
        
        @Override
        public Vector getResult()
        {
            return result;
        }
        
    }
    
    static class MySyncPurchaseProxy extends ProtoBufSyncPurchaseProxy implements IMyProxy
    {
        Vector result = new Vector();
        public MySyncPurchaseProxy(Host host, Comm comm,
                IServerProxyListener serverProxyListener,
                IUserProfileProvider userProfileProvider)
        {
            super(host, comm, serverProxyListener, userProfileProvider);
        }
        
        @Override
        protected int parseRequestSpecificData(ProtocolBuffer protoBuf)
                throws IOException
        {
            if (IServerProxyConstants.ACT_SYNC_PURCHASE.equals(protoBuf.getObjType()))
            {
                ProtoSyncPurchasedResp resp = ProtoSyncPurchasedResp.parseFrom(protoBuf.getBufferData());
                result.addElement(resp);
            }
            return super.parseRequestSpecificData(protoBuf);
        }
        
        @Override
        public Vector getResult()
        {
            return result;
        }
        
    }
    
    static class MyClientSettingProxy extends ProtoBufSettingDataProxy implements IMyProxy
    {
        Vector result = new Vector();
        public MyClientSettingProxy(Host host, Comm comm,
                IServerProxyListener serverProxyListener,
                IUserProfileProvider userProfileProvider)
        {
            super(host, comm, serverProxyListener, userProfileProvider);
        }

        @Override
        protected int parseRequestSpecificData(ProtocolBuffer protoBuf)
                throws IOException
        {
            if (IServerProxyConstants.ACT_SYNC_CLIENT_SETTING_DATA.equals(protoBuf.getObjType()))
            {
                ProtoSyncClientSettingDataResp resp = ProtoSyncClientSettingDataResp.parseFrom(protoBuf.getBufferData());
                result.addElement(resp);
            }
            
            return super.parseRequestSpecificData(protoBuf);
        }
        
        @Override
        public Vector getResult()
        {
            return result;
        }
        
    }
    
    static class MyUpsellProxy extends ProtoBufUpsellProxy implements IMyProxy
    {
        Vector result = new Vector();
        public MyUpsellProxy(Host host, Comm comm,
                IServerProxyListener serverProxyListener,
                IUserProfileProvider userProfileProvider)
        {
            super(host, comm, serverProxyListener, userProfileProvider);
        }

        protected int parseRequestSpecificData(ProtocolBuffer protoBuf)
                throws IOException
        {
            if(IServerProxyConstants.ACT_UPSELL.equals(protoBuf.getObjType()))
            {
                ProtoUpsellResp resp = ProtoUpsellResp.parseFrom(protoBuf.getBufferData());
                result.addElement(resp);
            }
            else if(IServerProxyConstants.ACT_SINGLE_PURCHASE.equals(protoBuf.getObjType()))
            {
                ProtoPurchaseResp resp = ProtoPurchaseResp.parseFrom(protoBuf.getBufferData());
                result.addElement(resp);
            }
            return super.parseRequestSpecificData(protoBuf);
        }
        
        public Vector getResult()
        {
            return result;
        }
    }
}
