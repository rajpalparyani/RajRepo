package com.telenav.navservice;

import java.io.IOException;
import java.util.Vector;

import com.telenav.j2me.datatypes.ProtocolBuffer;
import com.telenav.j2me.framework.protocol.ProtoServiceItem;
import com.telenav.j2me.framework.protocol.ProtoServiceMapping;
import com.telenav.j2me.framework.protocol.ProtoSyncServiceLocatorReq;
import com.telenav.j2me.framework.protocol.ProtoSyncServiceLocatorResp;
import com.telenav.j2me.framework.protocol.ProtoUserProfile;
import com.telenav.j2me.framework.util.ProtocolBufferUtil;
import com.telenav.logger.Logger;
import com.telenav.navservice.model.App;
import com.telenav.navservice.network.TnNetwork;

public class ServiceLocator
{
    private static String SYNC_SERVICE_LOCATOR = "SyncServiceLocator";
    private static String PROFILE = "profile";
    private static String POLICY_ALIAS = "trackingpolicy.http";
    private static String LDL_ALIAS = "ldl.udp";
    
    protected TnNetwork network = new TnNetwork();
    
    public String[] getNavServiceUrls(String url, App app) 
    {
        try
        {
            Logger.log(Logger.INFO, this.getClass().getName(), "Start getting nav service urls from server: "+url);
            Vector request = createRequest(app);
            byte[] requestBytes = ProtocolBufferUtil.listToByteArray(request);
            byte[] responseBytes = network.sendHttpPost(url, requestBytes);
            return parseResponse(responseBytes);
        } catch (Exception e)
        {
            Logger.log(this.getClass().getName(), e);
        }
        return null;
    }

    private String[] parseResponse(byte[] resp)
    {
        ProtocolBuffer[] bufs = ProtocolBufferUtil.toBufArray(resp);
        if (bufs == null)
        {
            Logger.log(Logger.INFO, this.getClass().getName(), "No service locator response");
            return null;
        }
     
        String[] urls = new String[2]; //first one is policy server, second one is ldl server
        for (int i=0; i<bufs.length; i++)
            handleProtocolBuffer(bufs[i], urls);
        
        return urls;
    }
    
    private void handleProtocolBuffer(ProtocolBuffer protoBuf, String[] urls)
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
                Logger.log(Logger.INFO, this.getClass().getName(), "got service mapping from server: "+mapping);
                parseServiceMapping(mapping, urls);
            }
        }
    }
    
    private void parseServiceMapping(ProtoServiceMapping mapping, String[] urls)
    {
        if (mapping != null)
        {
            Vector items = mapping.getServiceItems();
            for (int i = 0; items != null && i < items.size(); i++)
            {
                ProtoServiceItem item = (ProtoServiceItem) items.elementAt(i);
                String serviceURL = item.getServiceDomainName();
                Vector urlEntries = item.getUrlEntry();
                Vector urlItems = item.getUrlItem();
                if (urlEntries != null && urlEntries.size() > 0 && urlItems != null && urlItems.size() > 0)
                {
                    String domainAlias = (String) urlEntries.elementAt(0);
                    String domainURL = (String) urlItems.elementAt(0);
                    String actionURL = domainURL + serviceURL;
                    if (POLICY_ALIAS.equals(domainAlias))
                    {
                        urls[0] = actionURL;
                        Logger.log(Logger.INFO, this.getClass().getName(), "policy url in service mapping: "+urls[0]);
                        continue;
                    }
                    else if ( LDL_ALIAS.equals(domainAlias))
                    {
                        urls[1] = actionURL;
                        Logger.log(Logger.INFO, this.getClass().getName(), "ldl url in service mapping: "+urls[1]);
                        continue;
                    }
                }
            }
        }
    }
    
    private Vector createRequest(App app)
    {
        try
        {
            Vector reqList = new Vector();
            ProtoSyncServiceLocatorReq.Builder syncBuilder = ProtoSyncServiceLocatorReq.newBuilder();
            syncBuilder.setServiceMappingVersion("");
            ProtocolBuffer pbSync = new ProtocolBuffer();
            pbSync.setBufferData(syncBuilder.build().toByteArray());
            pbSync.setObjType(SYNC_SERVICE_LOCATOR);
            reqList.addElement(pbSync);
            
            String device = app.getPlatformString();
            ProtoUserProfile.Builder profileBuilder = ProtoUserProfile.newBuilder();
            profileBuilder.setProgramCode("ALL");
            profileBuilder.setPlatform(device.toUpperCase());
            profileBuilder.setVersion("1.0");
            profileBuilder.setDevice(app.getCarrier() + "_" + app.getDeviceName());
            profileBuilder.setBuildNumber(app.getAppVersion());
            profileBuilder.setGpsType("AGPS");
            profileBuilder.setProduct(app.getAppName());
            ProtocolBuffer pbProfile = new ProtocolBuffer();
            pbProfile.setBufferData(profileBuilder.build().toByteArray());
            pbProfile.setObjType(PROFILE);
            reqList.addElement(pbProfile);

            return reqList;
        }
        catch( Exception e )
        {
            Logger.log(this.getClass().getName(), e);
        }
        return null;
    }
    
}
