/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * ILoginProxy.java
 *
 */
package com.telenav.data.serverproxy.impl;


/**
 *@author fqming (fqming@telenav.cn)
 *@date 2010-12-21
 */
public interface ILoginProxy
{
    public final static int FUTE_CREATE_ACCOUNT = 1;
    public final static int FUTE_SIGN_IN = 0;
    
    public final static String FUTE_CREDENTIAL_EMAIL = "EMAIL";
    
    public String sendLogin(boolean isEncrypted, String verificationCode);
    
    public String sendLogin(int loginType, String verificationCode, boolean isEncrypted);
    
    public String sendPurchase(String productType);
    
    public String sendForgetPin();
    
    public String requestVerificationCode();
    
    public String getSsoToken();
    
    public String getPtn();
    
    public String getUserId();
    
    public String getPin();
    
    public String getEqPin();
    
    public String getSoc_code();
    
    public String getAccountType();
    
    public int getAccountStatus();
    
    public boolean isNeedPurchase();
    
    public String getCredentialId();
}
