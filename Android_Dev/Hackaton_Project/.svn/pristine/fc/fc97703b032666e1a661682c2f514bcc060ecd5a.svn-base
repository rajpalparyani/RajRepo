/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * ValidateEnv.java
 *
 */
package com.telenav;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Hashtable;
import java.util.Vector;

import org.json.tnme.JSONArray;
import org.json.tnme.JSONException;

import com.telenav.j2me.datatypes.ProtocolBuffer;
import com.telenav.j2me.framework.protocol.ProtoServiceItem;
import com.telenav.j2me.framework.protocol.ProtoServiceMapping;
import com.telenav.j2me.framework.protocol.ProtoSyncServiceLocatorReq;
import com.telenav.j2me.framework.protocol.ProtoSyncServiceLocatorResp;
import com.telenav.j2me.framework.protocol.ProtoUserProfile;
import com.telenav.j2me.framework.util.ProtocolBufferUtil;
import com.telenav.logger.Logger;
import com.telenav.network.TnConnection;
import com.telenav.network.TnHttpConnection;
import com.telenav.network.TnNetworkManager;
import com.telenav.network.TnSocketConnection;

/**
 *@author qli
 *@date 2011-4-11
 */
public class ValidateEnv
{
    
    private static String SYNC_SERVICE_LOCATOR = "SyncServiceLocator";
    private static String PROTOCOL_SOCKET = "socket://";
    private static String PROTOCOL_HTTP = "http://";
    private Vector serviceList = null;
    private Vector reqList = null;
    private Vector serverDownList = null;
    private byte[] responseBytes = null;
    private boolean debug = false;//TODO, control debug log.
    private boolean isGroup = false;
    private boolean printAll = false;
    private JSONArray json = null;
    private String[] groupUrls = null;
    private String url = null;
    //"http://qa-unit03.telenav.com:8080/resource-cserver/telenav-server-pb";//FP internet
    //"http://192.168.117.216:8080/resource-cserver/telenav-server-pb";//FP intranet

    
    public ValidateEnv(String url, boolean printAll)
    {
        this.url = url;
        this.printAll = printAll;
        serviceList = new Vector();
        serverDownList = new Vector();
    }
    
    public ValidateEnv(JSONArray json, boolean printAll)
    {
        this.json = json;
        this.printAll = printAll;
        serviceList = new Vector();
        serverDownList = new Vector();
        isGroup = true;
    }
    
    public ValidateEnv(Object param, boolean printAll)
    {
        serviceList = new Vector();
        serverDownList = new Vector();
        if ( param instanceof JSONArray )
        {
            this.json = (JSONArray)param;
            this.printAll = printAll;
            isGroup = true;
            try
            {
                for ( int i=json.length()-1 ; i>=0 ; i-- )
                {
                    serviceList.addElement(json.getString(i));
                }
            }
            catch (JSONException e)
            {
                Logger.log(this.getClass().getName(), e);
            }
        }
        else if ( param instanceof String[] )
        {
            this.groupUrls = (String[])param;
            this.url = groupUrls[1];
//            isGroup = true;
//            for ( int i=0 ; i<groupUrls.length ; i++ )
//            {
//                serviceList.addElement(groupUrls[i]);
//            }
        }
        else if ( param instanceof String )
        {
            this.url = (String)param;
        }
    }
    
    protected void validateEnv()
    {
        log("start validating...");
        if ( isGroup )
        {
            validGroup();
        }
        else
        {
            validUrl();
        }
    }
    
    protected void validGroup()
    {
        if ( serviceList == null || serviceList.size() <= 0 )
        {
            log("validating over... environment is null or empty!");
            return;
        }
        
        if ( validate() )
        {
            printResult();
        }
    }
    
    protected void validUrl()
    {
        if ( url == null || url.length() <= 0 )
        {
            log("validating over... url is null or empty!");
            return;
        }
        else if( !checkUrlFormat() )
        {
            log("validating over... url should be startWith \"http://\"");
            return;
        }
        logDebug(url, debug);
        
        request();
        sendRequest();
        if ( validate() )
        {
            printResult();
        }
    }
    
    protected void printResult()
    {
        if( serverDownList == null || serverDownList.size() <= 0 )
        {
            log("\nvalidate success, no service down!");
        }
        else
        {
            log("\nvalidate success, following service down!");
            for( int i=serverDownList.size()-1 ; i>=0 ; i--)
            {
                log(serverDownList.elementAt(i).toString());
            }
        }
    }
    
    protected void request()
    {
        reqList = new Vector();
        try
        {
            ProtoSyncServiceLocatorReq.Builder syncBuilder = ProtoSyncServiceLocatorReq.newBuilder();
            syncBuilder.setServiceMappingVersion("");
            ProtocolBuffer pbSync = new ProtocolBuffer();
            pbSync.setBufferData(syncBuilder.build().toByteArray());
            pbSync.setObjType(SYNC_SERVICE_LOCATOR);
            reqList.addElement(pbSync);
            
            addMandatoryNode(reqList);
        }
        catch (Exception e)
        {
            Logger.log(this.getClass().getName(), e);
        }
        
    }
    
    //FIXME, mandatory node is hard code here
    protected void addMandatoryNode(Vector list) throws Exception
    {
        ProtoUserProfile.Builder userProfileBuilder = ProtoUserProfile.newBuilder();
        userProfileBuilder.setMin("3661704590");//userInfo.phoneNumber
        userProfileBuilder.setPassword("");//userInfo.pin
        userProfileBuilder.setUserId("");//userInfo.userId
        userProfileBuilder.setEqpin("");//userInfo.eqpin
        userProfileBuilder.setLocale("");//userInfo.locale
        userProfileBuilder.setRegion("NA");//userInfo.region
        userProfileBuilder.setSsoToken("");//userInfo.ssoToken
        userProfileBuilder.setGuideTone("203");//userInfo.guideTone
        
        userProfileBuilder.setProgramCode("ATT");//clientInfo.carrier
        userProfileBuilder.setPlatform("ANDROID");//clientInfo.platform
        userProfileBuilder.setVersion("7.0.01");//clientInfo.version
        userProfileBuilder.setDevice("generic");//clientInfo.device
        userProfileBuilder.setBuildNumber("3000");//clientInfo.buildNumber
        userProfileBuilder.setGpsType("AGPS");//clientInfo.gpsTpye
        userProfileBuilder.setProduct("ATT_NAV");//clientInfo.productType
        userProfileBuilder.setDeviceCarrier("Android");//clientInfo.deviceCarrier
        
        userProfileBuilder.setAudioFormat("mp3hi");//userPref.audioFormat
        userProfileBuilder.setImageType("");//userPref.imageType
        userProfileBuilder.setAudioLevel("3");//userPref.audioLevel
        
        userProfileBuilder.setScreenWidth("480");
        userProfileBuilder.setScreenHeight("800");

        userProfileBuilder.setPtnSource(2);
        
        ProtocolBuffer buf = new ProtocolBuffer();
        buf.setBufferData(userProfileBuilder.build().toByteArray());
        buf.setObjType("Profile");
        list.addElement(buf);
    }
    
    protected void sendRequest()
    {
        try
        {
            byte[] requestBytes = ProtocolBufferUtil.listToByteArray(reqList);
            log("prepare service url...");
            logDebug("sending " + reqList + "\n" + toHex(requestBytes), debug);
            responseBytes = null;
            responseBytes = requestService(requestBytes);
            logDebug(responseBytes == null ? "got nothing" : ("got bytes" + toHex(responseBytes)) , debug);
            parseResponse();
            log("prepare service url done!");
        } catch (Exception e1)
        {
            Logger.log(this.getClass().getName(), e1);
        }
    }
    
    protected byte[] requestService(byte[] packet) 
    {
        if (packet != null)
        {
            OutputStream os = null;
            InputStream is = null;
            TnHttpConnection con = null;
            try
            {
                con = (TnHttpConnection)connect(url, TnNetworkManager.READ_WRITE, true);
                if (con != null)
                {
                    con.setRequestMethod("POST");
                    con.setRequestProperty("Content-Type", "text/html"); // required

                    os = con.openOutputStream();
                    dump(os, packet);
                    log("service locator...  status code:"+con.getResponseCode());
                    if (con.getResponseCode() == TnHttpConnection.HTTP_OK)
                    {
                        is = con.openInputStream();
                        return read(is, (int)con.getLength());
                    }
                }
            } catch (Exception e)
            {
                log(url+"  "+e.toString()+"    exception!");
                Logger.log(this.getClass().getName(), e);
            } finally
            {
                close(is);
                close(os);
                close(con);
            }
        }
        return null;
    }
    
    private TnConnection connect(String url, int mode, boolean timeout) throws IOException
    {
        System.setProperty("http.keepAlive", "false");
        return TnNetworkManager.getInstance() == null ? 
                null : TnNetworkManager.getInstance().openConnection(url, mode, timeout);
    }
    
    protected void dump(OutputStream os, byte[] packet) throws IOException
    {
        if (os != null)
        {
            os.write(packet, 0, packet.length);
            try {
                os.flush();
            } catch (IOException ioe) {
                Logger.log(this.getClass().getName(), ioe);
                throw ioe;
            }
        }
    }
    
    protected byte[] read(InputStream is, int len) throws IOException
    {
        byte[] result = new byte[len];
        if (is != null)
        {
            logDebug("expecting " + len + " bytes" , debug);
            if (len > 0)
            {
                result = new byte[len];
                if (result != null)
                {
                    int actualRead = 0;
                    int bytesRead = 0;
                    while (bytesRead < len && actualRead != -1)
                    {
                        actualRead = is.read(result, bytesRead, len - bytesRead);
                        bytesRead += actualRead;
                    }
                }
            }
        }
        return result;
    }
    
    protected void parseResponse()
    {
        ProtocolBuffer[] bufs = ProtocolBufferUtil.toBufArray(responseBytes);
        if (bufs == null)
            return;
        
        for (int i=0; i<bufs.length; i++)
            handleProtocolBuffer(bufs[i]);
        
    }
    
    protected void handleProtocolBuffer(ProtocolBuffer protoBuf)
    {
        if (SYNC_SERVICE_LOCATOR.equals(protoBuf.getObjType()))
        {
            ProtoSyncServiceLocatorResp resp = null;
            int status = -1;
            try
            {
                resp = ProtoSyncServiceLocatorResp.parseFrom(protoBuf.getBufferData());
                status = resp.getStatus();
            }
            catch (IOException e)
            {
            }
            if (status == 0)
            {
                ProtoServiceMapping mapping = resp.getServiceMapping();
                parseServiceMapping(mapping);
            }
        }
    }
    
    //FIXME: duplicate code with parseServiceMapping in ProtoBufServiceLocatorProxy
    protected void parseServiceMapping(ProtoServiceMapping mapping)
    {
        if (mapping != null)
        {
            Vector items = mapping.getServiceItems();
            String version = mapping.getServiceVersion();
            Hashtable domainMap = new Hashtable();
            Hashtable actionMap = new Hashtable();
            for (int i = 0; items != null && i < items.size(); i++)
            {
                ProtoServiceItem item = (ProtoServiceItem) items.elementAt(i);
                String serviceURL = item.getServiceDomainName();
                Vector actions = item.getActions();
                Vector urlEntries = item.getUrlEntry();
                Vector urlItems = item.getUrlItem();
                if (urlEntries != null && urlEntries.size() > 0 && urlItems != null && urlItems.size() > 0)
                {
                    String domainAlias = (String) urlEntries.elementAt(0);
                    String domainURL = (String) urlItems.elementAt(0);
                    domainMap.put(domainAlias, domainURL);
                    String actionURL = domainURL + serviceURL;
                    
                    if (actions != null)
                    {
                        for (int j = 0; j < actions.size(); j++)
                        {
                            String action = (String) actions.elementAt(j);
                            actionMap.put(action, actionURL);
                            serviceList.addElement(actionURL);
                            log("action: "+action+" | url: "+actionURL);
                        }
                    }
                }
            }
        }
    }
    
    protected void close(InputStream is)
    {
        try
        {
            if (is != null)
            {
                is.close();
            }
        } catch (Exception e)
        {
        }
    }

    protected void close(OutputStream os)
    {
        try
        {
            if (os != null)
            {
                os.close();
            }
        } catch (Exception e)
        {
        }
    }
    
    protected void close(TnConnection os)
    {
        try
        {
            if (os != null)
            {
                os.close();
            }
        } catch (Exception e)
        {
        }
    }
    
    
    protected boolean validate()
    {
        if( serviceList == null || serviceList.size() <= 0 )
        {
            log("\nwarning, service list is null\nvalidation stop...");
            return false;
        }
        log("\nvalidating...");
        Vector list = new Vector();
        for ( int i=serviceList.size()-1 ; i>=0 ; i-- )
        {
            String url = (String)serviceList.elementAt(i);
            if( !list.contains(url) )
            {
                validateService(url);
                list.addElement(url);
            }
        }
        return true;
    }
    
    protected void validateService(String url) 
    {
        if ( url.startsWith(PROTOCOL_HTTP) )
        {
            validateHttp(url);
        }
        else if ( url.startsWith(PROTOCOL_SOCKET) )
        {
            validateSocket(url);
        }
    }
    
    protected void validateHttp(String url)
    {
        OutputStream os = null;
        InputStream is = null;
        TnHttpConnection con = null;
        try
        {
            con = (TnHttpConnection)connect(url, TnNetworkManager.READ_WRITE, true);
            if (con != null)
            {
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type", "text/html"); // required

                if (con.getResponseCode() == TnHttpConnection.HTTP_OK)
                {
                    if(printAll)
                    {
                        log(url+"    work!");
                    }
                }
                else
                {
                    if(printAll)
                    {
                        log(url+"   status code:"+con.getResponseCode()+"    error!");
                    }
                    ServiceDown server = new ServiceDown();
                    server.ip = url;
                    server.port = con.getPort();
                    serverDownList.addElement(server);
                }
            }
        } catch (Exception e)
        {
            ServiceDown server = new ServiceDown();
            server.ip = url;
            serverDownList.addElement(server);
            log(url+"  "+e.toString()+"    exception!");
        } finally
        {
            close(is);
            close(os);
            close(con);
        }
    }
    
    protected void validateSocket(String url)
    {
        OutputStream os = null;
        InputStream is = null;
        TnSocketConnection con = null;
        try
        {
            con = (TnSocketConnection)connect(url, TnNetworkManager.READ_WRITE, true);
            if (con != null)
            {
                is = con.openInputStream();
                os = con.openOutputStream();

                if(printAll)
                {
                    log(url+"    work!");
                }
            }
        } catch (Exception e)
        {
            ServiceDown server = new ServiceDown();
            server.ip = url;
            serverDownList.addElement(server);
            log(url+"  "+e.toString()+"    exception!");
        } finally
        {
            close(is);
            close(os);
            close(con);
        }
    }
    
    protected class ServiceDown
    {
        protected String ip;
        protected int port;
        protected String dns;
        
        public String toString()
        {
            return "service url: "+ip;
        }
    }
    
    
    protected String toHex(byte[] bytes)
    {
        if (bytes == null)
            return "null";
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < bytes.length; i++)
        {
            String hex = Integer.toHexString(0xff & bytes[i]);
            if (hex.length() < 2)
                buf.append("0");
            buf.append(hex);
        }
        return buf.toString();
    }
    
    /**
     * get real ip address
     * @param url
     */
    protected void getIp(String host)
    {
        try
        {
            //FIXME, run commandline "ping host" , need parse strings to get real ip
            //for example: ping qa-unit03.telenav.com
            //Pinging qa-unit03.telenav.com [192.168.117.216] with 32 bytes of data:
            //
            //Reply from 192.168.117.216: bytes=32 time=164ms TTL=60
            //Reply from 192.168.117.216: bytes=32 time=149ms TTL=60
            //Reply from 192.168.117.216: bytes=32 time=155ms TTL=60
            //Reply from 192.168.117.216: bytes=32 time=148ms TTL=60
            //
            //Ping statistics for 192.168.117.216:
            //    Packets: Sent = 4, Received = 4, Lost = 0 (0% loss),
            //Approximate round trip times in milli-seconds:
            //    Minimum = 148ms, Maximum = 164ms, Average = 154ms
            //
            //Note: maybe it's not a good method to get real ip, try to improve it.
            Process process = Runtime.getRuntime().exec("ping "+host);
            InputStream is = process.getInputStream();
            String result = Util.read(is);
            log("=======Real IP======\n"+result);
        }
        catch (IOException e)
        {
            Logger.log(this.getClass().getName(), e);
        }
    }
    
    protected boolean checkUrlFormat()
    {
        if ( url.startsWith("http://") )
        {
            return true;
        }
        return false;
    }
    
    protected void logDebug(Object o, boolean debug)
    {
        if( debug )
        {
            log(o);
        }
    }
    
    protected void log(Object o) {
        Logger.log(Logger.INFO, this.toString(), "" + o);
    }

}
