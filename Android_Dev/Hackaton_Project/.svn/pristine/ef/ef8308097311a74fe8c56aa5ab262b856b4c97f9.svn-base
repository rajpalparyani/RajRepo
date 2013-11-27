/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * ProtoBufLoginProxy.java
 *
 */
package com.telenav.data.serverproxy.impl.protobuf;

import java.io.IOException;
import java.util.Vector;

import com.telenav.comm.Comm;
import com.telenav.comm.Host;
import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.dao.serverproxy.BillingAccountDao;
import com.telenav.data.datatypes.primitive.StringMap;
import com.telenav.data.serverproxy.IServerProxyConstants;
import com.telenav.data.serverproxy.IServerProxyListener;
import com.telenav.data.serverproxy.IUserProfileProvider;
import com.telenav.data.serverproxy.RequestItem;
import com.telenav.data.serverproxy.ServerProxyConfig;
import com.telenav.data.serverproxy.impl.ILoginProxy;
import com.telenav.data.serverproxy.impl.navsdk.service.NavsdkUserManagementService;
import com.telenav.j2me.datatypes.ProtocolBuffer;
import com.telenav.j2me.framework.protocol.ProtoAccountInfo;
import com.telenav.j2me.framework.protocol.ProtoForgetPinReq;
import com.telenav.j2me.framework.protocol.ProtoForgetPinResp;
import com.telenav.j2me.framework.protocol.ProtoLoginReq;
import com.telenav.j2me.framework.protocol.ProtoLoginResp;
import com.telenav.j2me.framework.protocol.ProtoProperty;
import com.telenav.j2me.framework.protocol.ProtoSendPTNVerificationCodeReq;
import com.telenav.j2me.framework.protocol.ProtoSendPTNVerificationCodeResp;
import com.telenav.j2me.framework.util.ToStringUtils;
import com.telenav.logger.Logger;
import com.telenav.module.AppConfigHelper;
import com.telenav.module.dashboard.AccountChangeListener;

/**
 *@author Casper (pwang@telenav.cn)
 *@date 2011-3-3
 */
public class ProtoBufLoginProxy extends AbstractProtobufServerProxy implements ILoginProxy
{

    public ProtoBufLoginProxy(Host host, Comm comm, IServerProxyListener serverProxyListener, IUserProfileProvider userProfileProvider)
    {
        super(host, comm, serverProxyListener, userProfileProvider);
    }
    
//    protected String sso_token = null;
    protected String userId = null;
    protected String ptn = null;
    protected String pin = null;
    protected String eqPin = null;
    protected String soc_code = null;
    protected String credentialId = null;
    
    protected boolean isNeedPurchase = false;
    
    protected int account_status = -1;
    protected String account_type = null;
    
    /**
     * Send login request, for the ptn please set into madantoryNode.
     */
    public String sendLogin(boolean isEncrypted, String verificationCode)
    {
        try
        {
            String action = IServerProxyConstants.ACT_LOGIN;
            if(isEncrypted)
            {
                action = IServerProxyConstants.ACT_ENCRYPT_LOGIN;
            }
            
            RequestItem requestItem = new RequestItem(action, this);
            
            if(verificationCode != null && verificationCode.trim().length() > 0)
            {
                if(requestItem.params == null)
                    requestItem.params = new Vector();
                
                requestItem.params.addElement(verificationCode);
            }
            
            Vector list = createProtoBufReq(requestItem);
            return this.sendRequest(list, action, 1, ServerProxyConfig.defaultTimeout);

        }
        catch (Exception e)
        {
            e.printStackTrace();
            if (listener != null)
                listener.networkError(this, IServerProxyListener.EXCEPTION_SEND, null);
            return "";
        }

    }

    /**
     * Send login request, for the ptn please set into madantoryNode.
     */
    public String sendLogin(int loginType, String verificationCode, boolean isEncrypted)
    {
        try
        {
            String action = IServerProxyConstants.ACT_LOGIN;
            if(isEncrypted)
            {
                action = IServerProxyConstants.ACT_ENCRYPT_LOGIN;
            }
            
            RequestItem requestItem = new RequestItem(action, this);
            
            requestItem.params = new Vector();
      
            if(verificationCode != null && verificationCode.trim().length() > 0)
            {
                requestItem.params.addElement(verificationCode);
            }
            else
            {
                requestItem.params.addElement("");
            }
            
            requestItem.params.addElement(loginType);
            
            Vector list = createProtoBufReq(requestItem);
            return this.sendRequest(list, action, 1, ServerProxyConfig.loginDefaultTimeout);

        }
        catch (Exception e)
        {
            e.printStackTrace();
            if (listener != null)
                listener.networkError(this, IServerProxyListener.EXCEPTION_SEND, null);
            return "";
        }

    }
    
    /**
     * Send login request, for the ptn please set into madantoryNode.
     */
    public String requestVerificationCode()
    {
        try
        {
            String action = IServerProxyConstants.ACT_REQUEST_VERIFICATION_CODE;
            RequestItem requestItem = new RequestItem(action, this);
            Vector list = createProtoBufReq(requestItem);
            return this.sendRequest(list, action, 1, ServerProxyConfig.loginDefaultTimeout);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            if (listener != null)
                listener.networkError(this, IServerProxyListener.EXCEPTION_SEND, null);
            return "";
        }
        
    }
    
    public String sendForgetPin()
    {
        try
        {
            RequestItem requestItem = new RequestItem(IServerProxyConstants.ACT_FORGET_PIN, this);

            Vector list = createProtoBufReq(requestItem);
            return this.sendRequest(list, IServerProxyConstants.ACT_FORGET_PIN);

        }
        catch (Exception e)
        {
            e.printStackTrace();
            if (listener != null)
                listener.networkError(this, IServerProxyListener.EXCEPTION_SEND, null);
            return "";
        }
    }
    
    public String sendPurchase(String productType)
    {
        try
        {
            RequestItem requestItem = new RequestItem(IServerProxyConstants.ACT_PURCHASE, this);
            requestItem.params = new Vector();
            requestItem.params.addElement(productType);

            Vector list = createProtoBufReq(requestItem);
            return this.sendRequest(list, IServerProxyConstants.ACT_PURCHASE);

        }
        catch (Exception e)
        {
            e.printStackTrace();
            if (listener != null)
                listener.networkError(this, IServerProxyListener.EXCEPTION_SEND, null);
            return "";
        }
    }
    
    protected void addRequestArgs(Vector requestVector,RequestItem requestItem) throws Exception
    {
        if (IServerProxyConstants.ACT_LOGIN.equals(requestItem.action)
                || IServerProxyConstants.ACT_ENCRYPT_LOGIN.equals(requestItem.action))
        {
            ProtoLoginReq.Builder builder = ProtoLoginReq.newBuilder();
            builder.setExecutorType(IServerProxyConstants.ACT_LOGIN);
            
            if(requestItem.params != null)
            {
                if(requestItem.params.size() > 0 && requestItem.params.elementAt(0) instanceof String)
                {
                    String verificationCode = (String)requestItem.params.elementAt(0);
                    builder.setVerificationCode(verificationCode);
                }
                
                if(requestItem.params.size() > 1 && requestItem.params.elementAt(1) instanceof Integer)
                {
                    Integer loginType=(Integer)requestItem.params.get(1);
                    builder.setCreateAccount(loginType);
                }
            }

            ProtocolBuffer pb = new ProtocolBuffer();
            pb.setBufferData(convertToByteArray(builder.build()));
            pb.setObjType(IServerProxyConstants.ACT_LOGIN);
            requestVector.addElement(pb);
        }
        else if(IServerProxyConstants.ACT_REQUEST_VERIFICATION_CODE.equals(requestItem.action))
        {
            ProtoSendPTNVerificationCodeReq.Builder builder = ProtoSendPTNVerificationCodeReq.newBuilder();
            //no use, just for cserver's compatible with tn6.x
            builder.setInValidField("");
            
            ProtocolBuffer pb = new ProtocolBuffer();
            pb.setBufferData(convertToByteArray(builder.build()));
            pb.setObjType(IServerProxyConstants.ACT_REQUEST_VERIFICATION_CODE);
            requestVector.addElement(pb);
        }
        else if (IServerProxyConstants.ACT_FORGET_PIN.equals(requestItem.action))
        {
            ProtoForgetPinReq.Builder builder = ProtoForgetPinReq.newBuilder();

            //no use, just for cserver's compatible with tn6.x
            builder.setInValidField("");
            
            ProtocolBuffer pb = new ProtocolBuffer();
            pb.setBufferData(convertToByteArray(builder.build()));
            pb.setObjType(IServerProxyConstants.ACT_FORGET_PIN);
            requestVector.addElement(pb);
        }
        else if (IServerProxyConstants.ACT_PURCHASE.equals(requestItem.action))
        {
            if (requestItem.params != null && requestItem.params.size() > 0)
            {
                String productType = (String) requestItem.params.elementAt(0);
                productType = "";
            }
            else
            {
                throw new IllegalArgumentException("request params is null or size is wrong.");
            }
        }
    }
    
    /**
     * get Sso_Token
     * @return ssoToken
     */
    public String getSsoToken()
    {
        return /*this.sso_token*/"";
    }
    
    /**
     * Get Ptn got from c-server
     * @return ptn
     */
    public String getPtn()
    {
        return this.ptn;
    }
    
    /**
     * Get User Id.
     * 
     * @return userId
     */
    public String getUserId()
    {
        return this.userId;
    }
    
    /**
     * Get user pin
     * 
     * @return pin
     */
    public String getPin()
    {
        return this.pin;
    }
    
    /**
     * get EQPin
     * 
     * @return EQPin
     */
    public String getEqPin()
    {
        return this.eqPin;
    }
    
    /**
     * Get socCode
     * 
     * @return socCode.
     */
    public String getSoc_code()
    {
        return this.soc_code;
    }
    
    /**
     * when login fail, it will return account type.
     * 
     * @return
     */
    public String getAccountType()
    {
        return this.account_type;
    }
    
    public boolean isNeedPurchase()
    {
        return this.isNeedPurchase;
    }
    
    /**
     * when login fail, it will return status.
     * 
     * @return
     */
    public int getAccountStatus()
    {
        return this.account_status;
    }
    
    /**
     * Get credentialId
     * @return
     */
    public String getCredentialId()
    {
        return credentialId;
    }
    
    protected int parseRequestSpecificData(ProtocolBuffer protoBuf) throws IOException
    {
        if (IServerProxyConstants.ACT_LOGIN.equals(protoBuf.getObjType()))
        {
            ProtoLoginResp resp = ProtoLoginResp.parseFrom(protoBuf.getBufferData());
            
            if(AppConfigHelper.isLoggerEnable)
            {
                Logger.log(Logger.INFO, this.getClass().getName(), "ACT_LOGIN -- resp : \n" + ToStringUtils.toString(resp));
            }

            this.status = resp.getStatus();
            this.errorMessage = resp.getErrorMessage();
            
            if (resp.hasAccountInfo())
            {
                if (resp.getAccountInfo().hasAccountStatus())
                {
                    account_status = Integer.valueOf(resp.getAccountInfo().getAccountStatus()).intValue();
                }
            }
            
            boolean isValidResponse = isValidResponse(resp);
            
            if(isValidResponse)
            {
                parsePurchaseData(resp);
                
                if(resp.hasPtn())
                {
                    ptn = resp.getPtn();
                    
                    if (ptn != null && ptn.trim().length() >=1)
                    {
                        if (AppConfigHelper.isLoggerEnable
                                && (ptn == null || ptn.trim().length() == 0))
                        {
                            Logger.log(Logger.ERROR, this.getClass().getName(),
                                "ACT_LOGIN -- Empty ptn : " + resp.getPtn());
                        }

                        DaoManager.getInstance().getMandatoryNodeDao().getMandatoryNode()
                                .getUserInfo().phoneNumber = ptn;
                        DaoManager.getInstance().getMandatoryNodeDao().getMandatoryNode()
                                .getUserInfo().ptnType = BillingAccountDao.PTN_TYPE_ENCRYPTED;
                        DaoManager.getInstance().getBillingAccountDao().setPtn(ptn);
                    }else
                    {
                        Logger.log(Logger.ERROR, this.getClass().getName(),
                            "ptn is null, need server check");
                    }
                }
                
                if (resp.hasUserId())
                {
                    userId = resp.getUserId();
                    
                    if(AppConfigHelper.isLoggerEnable && ( userId == null || userId.trim().length() == 0 ))
                    {
                        Logger.log(Logger.ERROR, this.getClass().getName(), "ACT_LOGIN -- Empty userId : " + resp.getUserId());
                    }
                    
                    DaoManager.getInstance().getMandatoryNodeDao().getMandatoryNode().getUserInfo().userId = userId;
                }
                
                if (resp.hasCredentialId())
                {
                    credentialId = resp.getCredentialId();
                }
                
                if (resp.hasPin())
                {
                    pin = resp.getPin();
                    DaoManager.getInstance().getMandatoryNodeDao().getMandatoryNode().getUserInfo().pin = pin;
                }
                
                if (resp.hasEqPin())
                {
                    eqPin = resp.getEqPin();
                    if(eqPin == null)
                    {
                        eqPin = "";
                    }
                    DaoManager.getInstance().getMandatoryNodeDao().getMandatoryNode().getUserInfo().eqpin = eqPin;
                }
                
                if (resp.hasSocCodeOfCurrentProduct())
                {
                    soc_code = resp.getSocCodeOfCurrentProduct();
                    DaoManager.getInstance().getBillingAccountDao().setSocType(soc_code);
                }
                     
                if (status != IServerProxyConstants.SUCCESS)
                {
                    if(resp.hasAccountInfo())
                    {
                        if(resp.getAccountInfo().hasAccountType())
                        {
                            account_type = resp.getAccountInfo().getAccountType();
                        }
                    }
                }
            }
        }
        else if (IServerProxyConstants.ACT_FORGET_PIN.equals(protoBuf.getObjType()))
        {
            ProtoForgetPinResp resp = ProtoForgetPinResp.parseFrom(protoBuf.getBufferData());
            this.status = resp.getStatus();
            this.errorMessage = resp.getErrorMessage();
        }
        else if(IServerProxyConstants.ACT_REQUEST_VERIFICATION_CODE.equals(protoBuf.getObjType()))
        {
            ProtoSendPTNVerificationCodeResp resp = ProtoSendPTNVerificationCodeResp.parseFrom(protoBuf.getBufferData());
            this.status = resp.getStatus();
            this.errorMessage = resp.getErrorMessage();
        }
        
        return this.status;
    }

    protected boolean isValidResponse(ProtoLoginResp resp)
    {
        boolean isValidResponse = false;

        if (resp != null)
        {
            if (resp.getAccountInfo() != null && resp.getAccountInfo().hasAccountStatus())
            {
                int account_status = Integer.valueOf(resp.getAccountInfo().getAccountStatus()).intValue();
                if (account_status > -1 && resp.getAccountInfo().getFeatureList() != null
                        && resp.getAccountInfo().getFeatureList().size() > 0)
                {
                    isValidResponse = true;
                }
            }
        }

        return isValidResponse;
    }
    
    private StringMap createStringMap(Vector vector)
    {
        int size = vector.size();
        StringMap stringMap = new StringMap();
        if (size > 0)
        {
            for (int i = 0; i < size; i++)
            {
                ProtoProperty feature = (ProtoProperty) vector.elementAt(i);
                if (feature.hasKey())
                {
                    stringMap.put(feature.getKey(), feature.getValue());
                }
            }
        }
        return stringMap;
    }
    
    protected void parsePurchaseData(ProtoLoginResp resp)
    {
        if(resp == null)
            return;
        
        if(resp.hasAccountInfo())
        {
            ProtoAccountInfo protoAccountInfo = resp.getAccountInfo();
            if(protoAccountInfo.hasNeedPurchase())
            {
                isNeedPurchase = protoAccountInfo.getNeedPurchase();
                Logger.log(Logger.INFO, this.getClass().getName(), "ACT_LOGIN -- isNeedPurchase: " + isNeedPurchase); 
                ((DaoManager)DaoManager.getInstance()).getBillingAccountDao().setNeedPurchase(isNeedPurchase);
            }
            if(protoAccountInfo.hasAccountType())
            {
                account_type = protoAccountInfo.getAccountType();
                if(account_type != null && account_type.trim().length() > 0)
                {
                    boolean accountTypeChanged = !account_type
                            .equalsIgnoreCase(DaoManager.getInstance()
                                    .getMandatoryNodeDao().getMandatoryNode()
                                    .getClientInfo().productType);
                    
                    DaoManager.getInstance().getMandatoryNodeDao().getMandatoryNode().getClientInfo().productType = account_type;
                    DaoManager.getInstance().getBillingAccountDao().setAccountType(account_type);
                    
                    if(accountTypeChanged)
                    {
                        AccountChangeListener.getInstance().accountChanged();
                    }
                }
            }

            if(protoAccountInfo.hasPurchasedOfferName())
            {
                DaoManager.getInstance().getBillingAccountDao().setPurchaseOrderName(protoAccountInfo.getPurchasedOfferName());
            }
            
            if(protoAccountInfo.hasOfferPrice())
            {
                DaoManager.getInstance().getBillingAccountDao().setOfferPrize(String.valueOf(protoAccountInfo.getOfferPrice()));
            }
           
            if(protoAccountInfo.hasOfferCurrency())
            {
                DaoManager.getInstance().getBillingAccountDao().setOfferCurrrency(protoAccountInfo.getOfferCurrency());
            }
            
            if(protoAccountInfo.hasExpiredTime())
            {
                DaoManager.getInstance().getBillingAccountDao().setOfferExpireDate(protoAccountInfo.getExpiredTime());
            }
            
            Vector features = protoAccountInfo.getFeatureList();
            int featureSize = features.size();
           
            if(featureSize > 0)
            {
                StringMap stringMap = createStringMap(features);
                ((DaoManager)DaoManager.getInstance()).getUpsellDao().setUpsellData(stringMap);
                ((DaoManager)DaoManager.getInstance()).getUpsellDao().store();
                ((DaoManager) DaoManager.getInstance()).getSimpleConfigDao().initTrafficLayerDaoSetting();
                NavsdkUserManagementService.getInstance().featureListUpdate();
            }
            
            Vector regions =  protoAccountInfo.getSupportedRegions();
            if(regions !=null && regions.size()>0)
            {
                StringMap stringMap = createStringMap(regions);
                if(stringMap.size() > 0)
                {
                    DaoManager.getInstance().getBillingAccountDao().setSupportedRegion(stringMap);
                    DaoManager.getInstance().getBillingAccountDao().store();
                }

            }
     
        }        
    }
}
