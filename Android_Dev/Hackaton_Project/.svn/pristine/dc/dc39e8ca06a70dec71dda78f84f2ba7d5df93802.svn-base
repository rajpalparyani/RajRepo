/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * MandatorySerializable.java
 *
 */
package com.telenav.data.serializable.txnode;

import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.datatypes.mandatory.MandatoryProfile;
import com.telenav.data.datatypes.mandatory.MandatoryProfile.UserInfo;
import com.telenav.data.serializable.IMandatorySerializable;
import com.telenav.logger.Logger;
import com.telenav.module.AppConfigHelper;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2010-12-23
 */
class TxNodeMandatorySerializable implements IMandatorySerializable
{
    private final static byte MANDATORY_NODE_TYPE_USER_INFO = 86;

    private final static byte MANDATORY_NODE_TYPE_CLIENT_INFO = 82;

    private final static byte MANDATORY_NODE_TYPE_USER_PREFS = 83;
    
    private final static byte MANDATORY_NODE_TYPE_CREDENTIAL_INFO = 87;
    
    public MandatoryProfile createMandatoryProfile(byte[] data)
    {
        if (data == null)
            return null;

        Node mandatoryNode = new Node(data, 0);
        
        MandatoryProfile profile = new MandatoryProfile();
        
        Node userInfoNode = mandatoryNode.getChildAt(0);
        profile.getUserInfo().phoneNumber = userInfoNode.getStringAt(0);
        profile.getUserInfo().pin = userInfoNode.getStringAt(1);
        profile.getUserInfo().userId = userInfoNode.getStringAt(2);
        profile.getUserInfo().eqpin = userInfoNode.getStringAt(3) == null ? "" : userInfoNode.getStringAt(3);
        profile.getUserInfo().locale = userInfoNode.getStringAt(4);
        profile.getUserInfo().region = userInfoNode.getStringAt(5);
        profile.getUserInfo().ssoToken = userInfoNode.getStringAt(6);
        profile.getUserInfo().guideTone = userInfoNode.getStringAt(7);
        profile.getUserInfo().ptnType = userInfoNode.getStringAt(8);
        profile.getUserInfo().plainPhoneNumber = userInfoNode.getStringAt(9);
        
        Node clientInfoNode = mandatoryNode.getChildAt(1);
        profile.getClientInfo().programCode = clientInfoNode.getStringAt(0);
        profile.getClientInfo().platform = clientInfoNode.getStringAt(1);
        profile.getClientInfo().version = clientInfoNode.getStringAt(2);
        profile.getClientInfo().device = clientInfoNode.getStringAt(3);
        profile.getClientInfo().buildNumber = clientInfoNode.getStringAt(4);
        profile.getClientInfo().gpsTpye = clientInfoNode.getStringAt(5);
        profile.getClientInfo().productType = clientInfoNode.getStringAt(6);
        profile.getClientInfo().deviceCarrier = clientInfoNode.getStringAt(7);
        
        Node userPrefsNode = mandatoryNode.getChildAt(2);
        profile.getUserPrefers().audioFormat = userPrefsNode.getStringAt(0);
        profile.getUserPrefers().imageType = userPrefsNode.getStringAt(1);
        profile.getUserPrefers().audioLevel = userPrefsNode.getStringAt(2);
        
        
        Node credentialInfoNode = mandatoryNode.getChildAt(3);
        if (credentialInfoNode != null)
        {
            profile.getCredentialInfo().credentialType = credentialInfoNode
                    .getStringAt(0);
            profile.getCredentialInfo().credentialID = credentialInfoNode.getStringAt(1);
            profile.getCredentialInfo().credentialValue = credentialInfoNode
                    .getStringAt(2);
            profile.getCredentialInfo().credentialPassword = credentialInfoNode
                    .getStringAt(3);
        }
        
        return profile;
    }

    public byte[] toBytes(MandatoryProfile profile)
    {
        if(profile == null)
            return null;
        
        Node mandatoryNode = new Node();
        Node userInfoNode = new Node();
        userInfoNode.addValue(MANDATORY_NODE_TYPE_USER_INFO);
        userInfoNode.addString(profile.getUserInfo().phoneNumber);// phone number
        userInfoNode.addString(profile.getUserInfo().pin);// pin
        userInfoNode.addString(profile.getUserInfo().userId);// user id
        userInfoNode.addString(profile.getUserInfo().eqpin);// eqpin
        userInfoNode.addString(profile.getUserInfo().locale);// locale
        userInfoNode.addString(profile.getUserInfo().region);// region
        userInfoNode.addString(profile.getUserInfo().ssoToken);// sso token
        userInfoNode.addString(profile.getUserInfo().guideTone);// guide tone
        userInfoNode.addString(profile.getUserInfo().ptnType);// ptn Type
        userInfoNode.addString(profile.getUserInfo().plainPhoneNumber);// plain phone number
        mandatoryNode.addChild(userInfoNode);

        Node clientInfoNode = new Node();
        clientInfoNode.addValue(MANDATORY_NODE_TYPE_CLIENT_INFO);
        clientInfoNode.addString(profile.getClientInfo().programCode);// carrier
        clientInfoNode.addString(profile.getClientInfo().platform);// platform
        clientInfoNode.addString(profile.getClientInfo().version);// version
        clientInfoNode.addString(profile.getClientInfo().device);// device
        clientInfoNode.addString(profile.getClientInfo().buildNumber);// build number
        clientInfoNode.addString(profile.getClientInfo().gpsTpye);// gps type
        clientInfoNode.addString(profile.getClientInfo().productType);// product type
        clientInfoNode.addString(profile.getClientInfo().deviceCarrier);// simOperatorName
        mandatoryNode.addChild(clientInfoNode);

        Node userPrefsNode = new Node();
        userPrefsNode.addValue(MANDATORY_NODE_TYPE_USER_PREFS);
        userPrefsNode.addString(profile.getUserPrefers().audioFormat);// audio format
        userPrefsNode.addString(profile.getUserPrefers().imageType);// image type
        userPrefsNode.addString(profile.getUserPrefers().audioLevel);// audio level
        mandatoryNode.addChild(userPrefsNode);
        
        
        Node credentialnodeInfo = new Node();
        credentialnodeInfo.addValue(MANDATORY_NODE_TYPE_CREDENTIAL_INFO);
        credentialnodeInfo.addString(profile.getCredentialInfo().credentialType);// email
        credentialnodeInfo.addString(profile.getCredentialInfo().credentialID);//
        credentialnodeInfo.addString(profile.getCredentialInfo().credentialValue);// email account
        credentialnodeInfo.addString(profile.getCredentialInfo().credentialPassword); //831026
        mandatoryNode.addChild(credentialnodeInfo);
        
        if(AppConfigHelper.isLoggerEnable)
        {
            Logger.log(Logger.INFO, DaoManager.class.getName(), " ------- To Mandatory Bytes ---------  mandatoryNode HashId : " + profile);
            
            UserInfo userInfo = profile.getUserInfo();
            if(userInfo == null)
            {
                Logger.log(Logger.ERROR, DaoManager.class.getName(), " ------- To Mandatory Bytes Error ------------");
                Logger.log(Logger.ERROR, DaoManager.class.getName(), " userInfo == null ");
            }
            else if (userInfo.phoneNumber == null
                    || userInfo.phoneNumber.length() == 0
                    || userInfo.userId == null || userInfo.userId.length() == 0)
            {
                Logger.log(Logger.ERROR, DaoManager.class.getName(), " ------- To Mandatory Bytes Error ------------");
                Logger.log(Logger.ERROR, DaoManager.class.getName(),
                    " missing --> ptn ? ptn = " + userInfo.phoneNumber
                            + " , or userId? userId = " + userInfo.userId);
            }
        }
        
        return mandatoryNode.toBinary();
    }

}
