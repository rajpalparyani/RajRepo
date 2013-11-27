/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * ServerProxyFactory.java
 *
 */
package com.telenav.data.serverproxy.impl;

import com.telenav.comm.Comm;
import com.telenav.comm.Host;
import com.telenav.data.serverproxy.IServerProxyListener;
import com.telenav.data.serverproxy.IUserProfileProvider;
import com.telenav.data.serverproxy.impl.json.JsonDimProxy;
import com.telenav.data.serverproxy.impl.json.JsonS4AProxy;
import com.telenav.data.serverproxy.impl.navsdk.NavSdkAutoSuggestProxy;
import com.telenav.data.serverproxy.impl.navsdk.NavSdkNavigationProxy;
import com.telenav.data.serverproxy.impl.navsdk.NavSdkOneboxProxy;
import com.telenav.data.serverproxy.impl.navsdk.NavSdkPoiProxy;
import com.telenav.data.serverproxy.impl.navsdk.NavSdkRgcProxy;
import com.telenav.data.serverproxy.impl.navsdk.NavSdkValidateAddressProxy;
import com.telenav.data.serverproxy.impl.navsdk.NavSdkValidateAirportProxy;
import com.telenav.data.serverproxy.impl.protobuf.ProtoBufAddressProxy;
import com.telenav.data.serverproxy.impl.protobuf.ProtoBufAndroidBillingProxy;
import com.telenav.data.serverproxy.impl.protobuf.ProtoBufBatchProxy;
import com.telenav.data.serverproxy.impl.protobuf.ProtoBufFeedbackProxy;
import com.telenav.data.serverproxy.impl.protobuf.ProtoBufLoginProxy;
import com.telenav.data.serverproxy.impl.protobuf.ProtoBufServiceLocatorProxy;
import com.telenav.data.serverproxy.impl.protobuf.ProtoBufSettingDataProxy;
import com.telenav.data.serverproxy.impl.protobuf.ProtoBufStartupProxy;
import com.telenav.data.serverproxy.impl.protobuf.ProtoBufSyncCombinationResProxy;
import com.telenav.data.serverproxy.impl.protobuf.ProtoBufSyncPurchaseProxy;
import com.telenav.data.serverproxy.impl.protobuf.ProtoBufToolsProxy;
import com.telenav.data.serverproxy.impl.protobuf.ProtoBufTrafficIncidentReportProxy;
import com.telenav.data.serverproxy.impl.protobuf.ProtoBufUpsellProxy;
import com.telenav.data.serverproxy.impl.protobuf.ProtoBufWeatherProxy;
import com.telenav.data.serverproxy.impl.protobuf.ProtoClientLoggingProxy;
import com.telenav.data.serverproxy.impl.txnode.TxNodeDsrStreamProxy;
import com.telenav.threadpool.Notifier;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2010-12-21
 */
public class ServerProxyFactory
{
    private static ServerProxyFactory instance = new ServerProxyFactory();
    
    private ServerProxyFactory()
    {
        
    }
    
    public static ServerProxyFactory getInstance()
    {
        return instance;
    }
    
    public IAddressProxy createAddressProxy(Host host, Comm comm, IServerProxyListener serverProxyListener, IUserProfileProvider userProfileProvider, boolean isUseBackupDao) 
    {
        return new ProtoBufAddressProxy(host, comm, serverProxyListener, userProfileProvider, isUseBackupDao);
    }
    
    public IAutoSuggestProxy createAutoSuggestProxy(Host host, Comm comm, IServerProxyListener serverProxyListener, IUserProfileProvider userProfileProvider) 
    {
        return new NavSdkAutoSuggestProxy(serverProxyListener);
    }
    
    public IBatchProxy createBatchProxy(Host host, Comm comm, IServerProxyListener serverProxyListener, IUserProfileProvider userProfileProvider) 
    {
        return new ProtoBufBatchProxy(host, comm, serverProxyListener, userProfileProvider);
    }
    
    public IFeedbackProxy createFeedbackProxy(Host host, Comm comm, IServerProxyListener serverProxyListener, IUserProfileProvider userProfileProvider) 
    {
        return new ProtoBufFeedbackProxy(host, comm, serverProxyListener, userProfileProvider);
    }
    
    public ILoginProxy createLoginProxy(Host host, Comm comm, IServerProxyListener serverProxyListener, IUserProfileProvider userProfileProvider) 
    {
        return new ProtoBufLoginProxy(host, comm, serverProxyListener, userProfileProvider);
    }
    
    public IMissingAudioProxy createMissingAudioProxy(Host host, Comm comm, IServerProxyListener serverProxyListener) 
    {
        return null;
    }
    
//    public INavigationProxy createNavigationProxy(Host host, Comm comm, IServerProxyListener serverProxyListener, IMissingAudioProxy missingAudioProxy) 
//    {
//        return new ProtoBufNavigationProxy(host, comm, serverProxyListener, missingAudioProxy, null);
//    }
    
    public NavSdkNavigationProxy createNavigationProxy(IServerProxyListener serverProxyListener) 
    {
        return new NavSdkNavigationProxy(serverProxyListener);
    }
    
    public IOneBoxSearchProxy createOneBoxSearchProxy(Host host, Comm comm, IServerProxyListener serverProxyListener, IUserProfileProvider userProfileProvider) 
    {
        return new NavSdkOneboxProxy(serverProxyListener);
    }
    
    public IPoiSearchProxy createPoiSearchProxy(Host host, Comm comm, IServerProxyListener serverProxyListener, IUserProfileProvider userProfileProvider) 
    {
        return new NavSdkPoiProxy(serverProxyListener);
    }
    
    public IRgcProxy createRgcProxy(Host host, Comm comm, IServerProxyListener serverProxyListener) 
    {
        return new NavSdkRgcProxy(serverProxyListener);
    }
    
    public IServiceLocatorProxy createServiceLocatorProxy(Host host, Comm comm, IServerProxyListener serverProxyListener, IUserProfileProvider userProfileProvider) 
    {
        return new ProtoBufServiceLocatorProxy(host, comm, serverProxyListener, userProfileProvider);
    }
    
//    public IShareAddressProxy createShareAddressProxy(Host host, Comm comm, IServerProxyListener serverProxyListener, IUserProfileProvider userProfileProvider) 
//    {
//        return new ProtoBufShareAddressProxy(host, comm, serverProxyListener, userProfileProvider);
//    }
    
    public IStartupProxy createStartupProxy(Host host, Comm comm, IServerProxyListener serverProxyListener) 
    {
        return new ProtoBufStartupProxy(host, comm, serverProxyListener, null);
    }
    
    public ISyncPurchaseProxy createSyncPurchaseProxy(Host host, Comm comm, IServerProxyListener serverProxyListener, IUserProfileProvider userProfileProvider)
    {
        return new ProtoBufSyncPurchaseProxy(host, comm, serverProxyListener, userProfileProvider);
    }
    
    public IToolsProxy createToolsProxy(Host host, Comm comm, IServerProxyListener serverProxyListener, IUserProfileProvider userProfileProvider) 
    {
        return new ProtoBufToolsProxy(host, comm, serverProxyListener, userProfileProvider);
    }
    
    public ITrafficIncidentReportProxy createTrafficIncidentReportProxy(Host host, Comm comm, IServerProxyListener serverProxyListener) 
    {
        return new ProtoBufTrafficIncidentReportProxy(host, comm, serverProxyListener, null);
    }
    
    public IValidateAddressProxy createValidateAddressProxy(Host host, Comm comm, IServerProxyListener serverProxyListener, IUserProfileProvider userProfileProvider) 
    {
        return new NavSdkValidateAddressProxy(serverProxyListener);
    }
    
    public IValidateAirportProxy createValidateAirportProxy(Host host, Comm comm, IServerProxyListener serverProxyListener, IUserProfileProvider userProfileProvider) 
    {
        return new NavSdkValidateAirportProxy(serverProxyListener);
    }
    
    public IDsrStreamProxy createDsrStreamProxy(Host host, Comm comm, String type, IServerProxyListener serverProxyListener, IUserProfileProvider userProfileProvider)
    {
        return new TxNodeDsrStreamProxy(host, comm, type, serverProxyListener, userProfileProvider);
    }
    
    public IDimProxy createDimProxy(Host host, Comm comm, IServerProxyListener serverProxyListener, IUserProfileProvider userProfileProvider) 
    {
        return new JsonDimProxy(host, comm, serverProxyListener, userProfileProvider);
    }
    
    public IS4AProxy createS4AProxy(Host host, Comm comm, IServerProxyListener serverProxyListener, IUserProfileProvider userProfileProvider) 
    {
        return new JsonS4AProxy(host, comm, serverProxyListener, userProfileProvider);
    }
    
    public TrafficMissingAudioProxy createTrafficMissingAudioProxy(Notifier notifier, IMissingAudioProxy missingAudioProxy)
    {
        return new TrafficMissingAudioProxy(notifier, missingAudioProxy);
    }
    
//    public ISyncSentAddressProxy createSyncSentAddressProxy(Host host, Comm comm, IServerProxyListener serverProxyListener, IUserProfileProvider userProfileProvider)
//    {
//        return new ProtoBufSyncSentAddressProxy(host, comm, serverProxyListener, userProfileProvider);
//    }
    
    public ISyncCombinationResProxy createSyncCombinationResProxy(Host host, Comm comm, IServerProxyListener serverProxyListener, IUserProfileProvider userProfileProvider)
    {
        return new ProtoBufSyncCombinationResProxy(host, comm, serverProxyListener, userProfileProvider);
    }
    
    public ISettingDataProxy createSettingDataProxy(Host host, Comm comm, IServerProxyListener serverProxyListener, IUserProfileProvider userProfileProvider)
    {
        return new ProtoBufSettingDataProxy(host, comm, serverProxyListener, userProfileProvider);
    }
    
    public IWeatherProxy createWeatherProxy(Host host, Comm comm, IServerProxyListener serverProxyListener, IUserProfileProvider userProfileProvider) 
    {
        return new ProtoBufWeatherProxy(host, comm, serverProxyListener, userProfileProvider);
    }
    
    public IAndroidBillingProxy createAndroidBillingProxy(Host host, Comm comm, IServerProxyListener serverProxyListener, IUserProfileProvider userProfileProvider) 
    {
        return new ProtoBufAndroidBillingProxy(host, comm, serverProxyListener, userProfileProvider);
    }
    
    public IClientLoggingProxy createClientLoggingProxy(Host host, Comm comm, IServerProxyListener serverProxyListener, IUserProfileProvider userProfileProvider) 
    {
        return new ProtoClientLoggingProxy(host, comm, serverProxyListener, userProfileProvider);
    }
    
    public IUpsellProxy createUpsellProxy(Host host, Comm comm, IServerProxyListener serverProxyListener, IUserProfileProvider userProfileProvider) 
    {
        return new ProtoBufUpsellProxy(host, comm, serverProxyListener, userProfileProvider);
    }
    
}
