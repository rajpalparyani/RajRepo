/**
 *
 * Copyright 2013 TeleNav, Inc. All rights reserved.
 * TxNodeLoginInfoSerializable.java
 *
 */
package com.telenav.data.serializable.txnode;

import com.telenav.data.datatypes.mandatory.MandatoryProfile.CredentialInfo;
import com.telenav.data.serializable.ILoginInfoSerializable;
import com.telenav.module.login.LoginController.PtnData;
import com.telenav.module.login.LoginController.UserLoginData;

/**
 *@author yning
 *@date 2013-4-5
 */
public class TxNodeLoginInfoSerializable implements ILoginInfoSerializable
{

    @Override
    public byte[] toBytes(UserLoginData loginData)
    {
        if(loginData == null)
        {
            return null;
        }
        
        Node node = new Node();
        
        PtnData ptnData = loginData.ptnData;
        
        Node ptnDataNode = new Node();
        if(ptnData != null)
        {
            ptnDataNode.addString(ptnData.ptn);
            ptnDataNode.addString(ptnData.ptn_type);
            ptnDataNode.addString(ptnData.carrier);
        }
        
        CredentialInfo credentialInfo = loginData.credentialInfo;
        
        Node credentialInfoNode = new Node();
        if(credentialInfo != null)
        {
            credentialInfoNode.addString(credentialInfo.credentialID);
            credentialInfoNode.addString(credentialInfo.credentialPassword);
            credentialInfoNode.addString(credentialInfo.credentialType);
            credentialInfoNode.addString(credentialInfo.credentialValue);
        }
        
        
        node.addChild(ptnDataNode);
        node.addChild(credentialInfoNode);
        
        return node.toBinary();
    }

    @Override
    public UserLoginData createLoginInfo(byte[] data)
    {
        if(data == null)
        {
            return null;
        }
        
        UserLoginData loginData = new UserLoginData();
        
        PtnData ptnData = new PtnData();
        CredentialInfo credentialInfo = new CredentialInfo();
        
        Node node = new Node(data, 0);
        
        //even if there is no ptnData stored, there should be an empty node.
        //and there is protection so getString() method won't cause exception in this case.
        Node ptnDataNode = node.getChildAt(0);
        ptnData.ptn = ptnDataNode.getStringAt(0);
        ptnData.ptn_type = ptnDataNode.getStringAt(1);
        ptnData.carrier = ptnDataNode.getStringAt(2);
        
        Node credentialInfoNode = node.getChildAt(1);
        credentialInfo.credentialID = credentialInfoNode.getStringAt(0);
        credentialInfo.credentialPassword = credentialInfoNode.getStringAt(1);
        credentialInfo.credentialType = credentialInfoNode.getStringAt(2);
        credentialInfo.credentialValue = credentialInfoNode.getStringAt(3);
        
        loginData.ptnData = ptnData;
        loginData.credentialInfo = credentialInfo;
        
        return loginData;
    }

}
