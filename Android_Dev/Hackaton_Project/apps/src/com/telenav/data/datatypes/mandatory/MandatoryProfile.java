/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * UserProfile.java
 *
 */
package com.telenav.data.datatypes.mandatory;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2010-12-23
 */
public class MandatoryProfile
{
    private UserInfo userInfo = new UserInfo();
    private ClientInfo clientInfo = new ClientInfo();
    private UserPrefers userPrefers = new UserPrefers();
    private CredentialInfo credentialInfo = new CredentialInfo();
    
    public UserInfo getUserInfo()
    {
        return userInfo;
    }
    
    public ClientInfo getClientInfo()
    {
        return clientInfo;
    }
    
    public UserPrefers getUserPrefers()
    {
        return userPrefers;
    }
    
    public CredentialInfo getCredentialInfo()
    {
        return credentialInfo;
    }
    
    public void clear()
    {
        userInfo = new UserInfo();
        clientInfo = new ClientInfo();
        userPrefers = new UserPrefers();
    }
    
    public static class UserInfo
    {
        public String phoneNumber = "";
        public String pin = "";
        public String userId = "";
        public String eqpin = "";
        public String locale = "";
        public String region = "";
        public String ssoToken = "";
        public String guideTone = "";
        public String ptnType = "";
        public String plainPhoneNumber = "";
    }
    
    public static class ClientInfo
    {
        public String programCode = "";
        public String platform = "";
        public String version = "";
        public String device = "";
        public String buildNumber = "";
        public String gpsTpye = "";
        public String productType = "";
        public String deviceCarrier = "";
    }
    
    public static class UserPrefers
    {
        public String audioFormat = "";
        public String imageType = "";
        public String audioLevel = "3";
    }
    
    public static class CredentialInfo
    {
        public String credentialType = "";
        public String credentialID = "";
        public String credentialValue = "";
        public String credentialPassword = "";
    }
}
