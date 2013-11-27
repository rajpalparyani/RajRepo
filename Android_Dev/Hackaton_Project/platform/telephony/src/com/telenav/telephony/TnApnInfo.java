/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * TnApnInfo.java
 *
 */
package com.telenav.telephony;

/**
 * All fields of apn.
 *@author yxyao
 *@date 2011-10-20
 */
public class TnApnInfo
{
    private String id;
    
    private String name;
    
    private String apn;
    
    private String apType;

    /*Mobile Network Code*/
    private String mnc;
    
    /*Mobile Country Code*/
    private String mcc;
    
    private String user;
    
    private String password;
    
    private String server;
    
    private String mmsproxy;
    
    private String mmsport;
    
    /*mms center*/
    private String mmsc;
    
    private String proxy;
    
    private String port;
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getApn() {
        return apn;
    }

    public void setApn(String apn) {
        this.apn = apn;
    }

    public String getApType() {
        return apType;
    }

    public void setApType(String apType) {
        this.apType = apType;
    }

    public String getMnc() {
        return mnc;
    }

    public void setMnc(String mnc) {
        this.mnc = mnc;
    }

    public String getMcc() {
        return mcc;
    }

    public void setMcc(String mcc) {
        this.mcc = mcc;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getMmsproxy() {
        return mmsproxy;
    }

    public void setMmsproxy(String mmsproxy) {
        this.mmsproxy = mmsproxy;
    }

    public String getMmsport() {
        return mmsport;
    }

    public void setMmsport(String mmsport) {
        this.mmsport = mmsport;
    }

    public String getMmsc() {
        return mmsc;
    }

    public void setMmsc(String mmsc) {
        this.mmsc = mmsc;
    }

    public String getProxy() {
        return proxy;
    }

    public void setProxy(String proxy) {
        this.proxy = proxy;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }
}
