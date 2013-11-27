/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * ProtoBUfSyncCombinationResProxy.java
 *
 */
package com.telenav.data.serverproxy.impl.protobuf;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;

import com.telenav.comm.Comm;
import com.telenav.comm.Host;
import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.dao.misc.PreferenceDao;
import com.telenav.data.dao.serverproxy.AbstractDaoManager;
import com.telenav.data.dao.serverproxy.MandatoryNodeDao;
import com.telenav.data.dao.serverproxy.ResourceBarDao;
import com.telenav.data.dao.serverproxy.ServerDrivenParamsDao;
import com.telenav.data.datatypes.address.Stop;
import com.telenav.data.datatypes.map.MapDataUpgradeInfo;
import com.telenav.data.datatypes.poi.PoiCategory;
import com.telenav.data.datatypes.primitive.StringList;
import com.telenav.data.datatypes.primitive.StringMap;
import com.telenav.data.serverproxy.IServerProxyConstants;
import com.telenav.data.serverproxy.IServerProxyListener;
import com.telenav.data.serverproxy.IUserProfileProvider;
import com.telenav.data.serverproxy.RequestItem;
import com.telenav.data.serverproxy.impl.ISyncCombinationResProxy;
import com.telenav.j2me.datatypes.ProtocolBuffer;
import com.telenav.j2me.framework.protocol.ProtoAirPort;
import com.telenav.j2me.framework.protocol.ProtoAudioData;
import com.telenav.j2me.framework.protocol.ProtoAudioInventory;
import com.telenav.j2me.framework.protocol.ProtoBizCategory;
import com.telenav.j2me.framework.protocol.ProtoClientSetting;
import com.telenav.j2me.framework.protocol.ProtoCombinationSyncResourceReq;
import com.telenav.j2me.framework.protocol.ProtoDsrSdpParams;
import com.telenav.j2me.framework.protocol.ProtoGobyEventCategory;
import com.telenav.j2me.framework.protocol.ProtoGobyEventCategoryCollection;
import com.telenav.j2me.framework.protocol.ProtoHotBrand;
import com.telenav.j2me.framework.protocol.ProtoHotPoiCategory;
import com.telenav.j2me.framework.protocol.ProtoHotPoiCategoryCollection;
import com.telenav.j2me.framework.protocol.ProtoMapDataUpgradeInfo;
import com.telenav.j2me.framework.protocol.ProtoOtherItem;
import com.telenav.j2me.framework.protocol.ProtoProperty;
import com.telenav.j2me.framework.protocol.ProtoSdpMessage;
import com.telenav.j2me.framework.protocol.ProtoServiceMapping;
import com.telenav.j2me.framework.protocol.ProtoStop;
import com.telenav.j2me.framework.protocol.ProtoSyncResourceInfo;
import com.telenav.j2me.framework.protocol.ProtoSyncResourceResp;
import com.telenav.location.TnLocation;
import com.telenav.logger.Logger;
import com.telenav.module.preference.carmodel.CarModelManager;
import com.telenav.module.upsell.FeaturesManager;
import com.telenav.util.PrimitiveTypeCache;

/**
 *@author Casper (pwang@telenav.cn)
 *@date 2011-3-3
 */
public class ProtoBufSyncCombinationResProxy  extends AbstractProtobufServerProxy implements ISyncCombinationResProxy
{   
    protected int step;

    // SyncResource Request $value[0] request number. 1 ~ n
    protected int requestNumber;

    protected boolean hasRemainder;

    protected Vector combination;
    
    protected ResourceBarDao resourceBarDao;
    
    protected boolean hasAirport;
    protected boolean hasClientSetting;
    
    public ProtoBufSyncCombinationResProxy(Host host, Comm comm, IServerProxyListener serverProxyListener, IUserProfileProvider userProfileProvider)
    {
        super(host, comm, serverProxyListener, userProfileProvider);
    }
    
    public String syncCombinationRes(Vector combination)
    {
        return syncCombinationRes(combination, false);
    }
    
    public String syncCombinationRes(Vector combination, boolean isNeedBackup)
    {
        if( isNeedBackup )
        {
            resourceBarDao = AbstractDaoManager.getInstance().getResourceBackupDao();
        }
        else
        {
            resourceBarDao = AbstractDaoManager.getInstance().getResourceBarDao();
        }
        
        try
        {
            hasAirport = false;
            hasClientSetting = false;
            
            this.combination = combination;

            RequestItem requestItem = new RequestItem(IServerProxyConstants.ACT_COMBINATION_SYNC_RESOURCE, this);
            requestItem.params = new Vector();
            requestItem.params.addElement(combination);
            Vector list = createProtoBufReq(requestItem);

            return this.sendRequest(list, IServerProxyConstants.ACT_COMBINATION_SYNC_RESOURCE);
        }
        catch (Exception e)
        {
            Logger.log(this.getClass().getName(), e);
            
            if (listener != null)
                listener.networkError(this, IServerProxyListener.EXCEPTION_SEND, null);
            return "";
        }
    }
    
    private void sendRemainderResourceRequest()
    {
        step = STEP_SYNC_START;
        
        try
        {
            RequestItem requestItem = new RequestItem(IServerProxyConstants.ACT_COMBINATION_SYNC_RESOURCE, this);
            requestItem.params = new Vector();
            requestItem.params.addElement(combination);
            Vector list = createProtoBufReq(requestItem);
            this.sendRequest(list, IServerProxyConstants.ACT_COMBINATION_SYNC_RESOURCE);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            if (listener != null)
                listener.networkError(this, IServerProxyListener.EXCEPTION_SEND, null);
        }
    }

    protected void addRequestArgs(Vector requestVector,RequestItem requestItem) throws Exception
    {
        if(resourceBarDao == null)
        {
            resourceBarDao = AbstractDaoManager.getInstance().getResourceBarDao();
        }
        
        if (IServerProxyConstants.ACT_COMBINATION_SYNC_RESOURCE.equals(requestItem.action))
        {
            if (requestItem.params != null && requestItem.params.size() > 0)
            {
                
                Vector combination = (Vector) requestItem.params.elementAt(0);
                if (this.combination == null)
                    this.combination = combination;

                ProtoCombinationSyncResourceReq.Builder builder = ProtoCombinationSyncResourceReq.newBuilder();
                
                for(int i = 0; i < combination.size(); i++)
                {
                    int type = ((Integer) combination.elementAt(i)).intValue();
                    builder.addElementSyncResourceInfo(createSyncResourceInfo(type));
                }

                ProtocolBuffer pb = new ProtocolBuffer();
                pb.setBufferData(convertToByteArray(builder.build()));
                pb.setObjType(IServerProxyConstants.ACT_COMBINATION_SYNC_RESOURCE);
                requestVector.addElement(pb);
            }
            else
            {
                throw new IllegalArgumentException("request params is null or size is wrong.");
            }
        }
    }
    
    private ProtoSyncResourceInfo createSyncResourceInfo(int type)
    {
        ProtoSyncResourceInfo.Builder infoBuilder = ProtoSyncResourceInfo.newBuilder();
        infoBuilder.setType(getSyncResType(type));

        switch (type)
        {
            case TYPE_SERVICE_LOCATOR_INFO:
            {
                String serviceMappingVersion = AbstractDaoManager.getInstance().getServiceLocatorDao().getVersion();
                if (serviceMappingVersion == null)
                    serviceMappingVersion = "";
                infoBuilder.addElementVersion(serviceMappingVersion);
                break;
            }
            case TYPE_SERVER_DRIVEN_PARAMETERS:
            {
                String serverDrivenParametersVersion = AbstractDaoManager.getInstance().getServerDrivenParamsDao().getVersion(getRegion());
                if (serverDrivenParametersVersion == null)
                    serverDrivenParametersVersion = "";
                infoBuilder.addElementVersion(serverDrivenParametersVersion);
                break;
            }
            case TYPE_RESOURCE_FORMAT:
            {
                String resourceFormatVersion = this.resourceBarDao.getResourceFormatVersion();
                resourceFormatVersion = resourceFormatVersion == null ? "" : resourceFormatVersion;
                infoBuilder.addElementVersion(resourceFormatVersion);
                break;
            }
            case TYPE_POI_BRAND_NAME:
            {
                String brandNameVersion = this.resourceBarDao.getBrandNameVersion(getRegion());
                brandNameVersion = brandNameVersion == null ? "" : brandNameVersion;
                infoBuilder.addElementVersion(brandNameVersion);
                break;
            }
            case TYPE_HOT_POI_CATEGORY:
            {
                String hotPoiVersion = this.resourceBarDao.getHotPoiVersion(getRegion());
                hotPoiVersion = hotPoiVersion == null ? "" : hotPoiVersion;
                infoBuilder.addElementVersion(hotPoiVersion);
                break;
            }
            case TYPE_CATEGORY_TREE:
            {
                String categoryVersion = this.resourceBarDao.getCategoryVersion(getRegion());
                categoryVersion = categoryVersion == null ? "" : categoryVersion;
                infoBuilder.addElementVersion(categoryVersion);
                break;
            }
            case TYPE_AIRPORT:
            {
                String airportVersion = this.resourceBarDao.getAirportVersion();
                airportVersion = airportVersion == null ? "" : airportVersion;
                infoBuilder.addElementVersion(airportVersion);
                break;
            }
            case TYPE_AUDIO_AND_RULE:
            {
                String audioInv = this.resourceBarDao.getAudioInventory();
                audioInv = audioInv == null ? "" : audioInv;
                String audioTimeStamp = this.resourceBarDao.getAudioTimestamp();
                audioTimeStamp = audioTimeStamp == null ? "" : audioTimeStamp;
                String audioRuleInv = this.resourceBarDao.getAudioRuleInventory();
                audioRuleInv = audioRuleInv == null ? "" : audioRuleInv;
                String audioRuleTimeStamp = this.resourceBarDao.getAudioRuleTimestamp();
                audioRuleTimeStamp = audioRuleTimeStamp == null ? "" : audioRuleTimeStamp;
                infoBuilder.addElementVersion(audioInv + "," + audioTimeStamp);
                infoBuilder.addElementVersion(audioRuleInv + "," + audioRuleTimeStamp);
                break;
            }
            case TYPE_MAP_DATA_UPGRADE:
            {
                /**
                 * We can't get map data upgrade info version from response. CServer don't care the version, each time
                 * client add the action, CServer will send back the map data upgrade info
                 * 
                 */
                String mapUpgradeInfoVersion = "0";
                infoBuilder.addElementVersion(mapUpgradeInfoVersion);
                break;
            }
            case TYPE_DSR_SDP_NODE:
            {
                String dsrDspVersion = AbstractDaoManager.getInstance().getDsrServerDrivenParamsDao().getVersion();
                dsrDspVersion = dsrDspVersion == null ? "" : dsrDspVersion;
                infoBuilder.addElementVersion(dsrDspVersion);
                break;
            }
            case TYPE_CAR_MODEL:
            {
                String modName = CarModelManager.getInstance().getCarModelModName();
                String greyModName = CarModelManager.getInstance().getGreyCarModName();
                if (!"".equals(modName.trim()))
                {
                    infoBuilder.addElementVersion(modName + "=" + CarModelManager.getInstance().getCarModelVersion(modName));
                }
                else
                {
                    infoBuilder.addElementVersion(modName);
                }
                if (!"".equals(greyModName.trim()))
                {
                    infoBuilder.addElementVersion(greyModName + "=" + CarModelManager.getInstance().getCarModelVersion(greyModName));
                }
                else
                {
                    infoBuilder.addElementVersion(greyModName);
                }
                break;
            }
            case TYPE_PREFERENCE_SETTING:
            {
                String version = this.resourceBarDao.getPreferenceSettingVersion(getRegion());
                version = version == null ? "" : version;
                infoBuilder.addElementVersion(version);
                
                Vector featureList = getEnableFeatureList();
                
                if(featureList != null)
                {
                    int size = featureList.size();
                    for(int i = 0 ; i < size ; i ++)
                    {
                        infoBuilder.addElementVersion((String)featureList.elementAt(i));
                    }
                    featureList.removeAllElements();
                    featureList = null;
                }
                
                break;
            }
            case TYPE_REGION_CENTER_POINT:
            {
                TnLocation tnLocation = this.resourceBarDao.getRegionAnchor(this
                        .getRegion());
                if (tnLocation ==null)
                {
                    infoBuilder.addElementVersion("");
                }
                break;
            }
            case TYPE_GOBY_EVENT:
            {
                String gobyEventsVersion = this.resourceBarDao.getLocalEventsVersion(getRegion());
                gobyEventsVersion = gobyEventsVersion == null ? "" : gobyEventsVersion;
                infoBuilder.addElementVersion(gobyEventsVersion);
                break;
            }
        }
        
        return infoBuilder.build();
    }
    
    protected Vector getEnableFeatureList()
    {
        Vector featureList = null;
        
        StringMap featureMapping = ((DaoManager) DaoManager.getInstance()).getUpsellDao().getFeatureList();
        if(featureMapping != null)
        {
            featureList = new Vector();
            Enumeration keyEnum = featureMapping.keys();
            while(keyEnum.hasMoreElements())
            {
                String featureName = (String)keyEnum.nextElement();
                int featureValue = FeaturesManager.getInstance().getStatus(featureName);
                
                boolean isFeatureEnabled = (featureValue == FeaturesManager.FE_ENABLED || featureValue == FeaturesManager.FE_PURCHASED);
                if(isFeatureEnabled)
                {
                    featureList.addElement(featureName);
                }
            }
        }
        
        return featureList;
    }
    
    protected int parseRequestSpecificData(ProtocolBuffer protoBuf) throws IOException
    {
        if (IServerProxyConstants.ACT_COMBINATION_SYNC_RESOURCE.equals(protoBuf.getObjType()))
        {
            ProtoSyncResourceResp resp = ProtoSyncResourceResp.parseFrom(protoBuf.getBufferData());
            this.errorMessage = resp.getErrorMessage();
            this.status = resp.getStatus();
            Logger.log(Logger.INFO, this.getClass().getName(), "ACT_COMBINATION_SYNC_RESOURCE -- status: " + this.status);
            if (status == IServerProxyConstants.SUCCESS)
            {
                parseResourceFormat(resp, resourceBarDao);
                
                parseAudioInventory(resp, resourceBarDao);
                
                if(resp.hasServiceMapping())
                {
                    ProtoServiceMapping mapping = resp.getServiceMapping();
                    ProtoBufServiceLocatorProxy proxy = new ProtoBufServiceLocatorProxy(null,null,null,this.userProfileProvider);
                    proxy.parseServiceMapping(mapping);
                }

                if(resp.hasSdp())
                {
                    ProtoSdpMessage sdp = resp.getSdp();
                    parseServerDrivenParameters(sdp);
                }
                
                if(resp.hasDsrSdp())
                {
                    ProtoDsrSdpParams dsrSdp = resp.getDsrSdp();
                    parseDSRServerDrivenParameters(dsrSdp);
                }
                
                if(resp.hasAirport())
                {
                    hasAirport = true;
                    ProtoAirPort airport = resp.getAirport();
                    parseAirports(airport);
                }

                Vector mapDataUpgradeInfo = resp.getMapDataUpgradeInfo();
                if(mapDataUpgradeInfo != null && mapDataUpgradeInfo.size() > 0)
                {
                    parseMapDataUpgradeInfo(mapDataUpgradeInfo);
                }
                
                if(resp.hasBizCategory())
                {
                    ProtoBizCategory bizCate = resp.getBizCategory();
                    parseBizCate(bizCate, resp);
                }
                
                if(resp.hasHotCategory())
                {
                    ProtoHotPoiCategoryCollection hotCate = resp.getHotCategory();
                    parseHotCate(hotCate);
                }

                if(resp.hasHotBrand())
                {
                    ProtoHotBrand hotBrand = resp.getHotBrand();
                    parseHotBrand(hotBrand);
                }
                
                if(resp.hasGobyEventCategory())
                {
                    ProtoGobyEventCategoryCollection events = resp.getGobyEventCategory();
                    parseGobyEvents(events);
                }
                
                if(resp.hasClientSetting())
                {
                    hasClientSetting = true;
                    ProtoClientSetting clientSetting = resp.getClientSetting();
                    parseClientSetting(clientSetting);
                }
                
                if(resp.hasRegionCenterPoint())
                {
                    String centerPoint = resp.getRegionCenterPoint();
                    parseCenterPoint(centerPoint);
                }
                
                this.hasRemainder = resp.getHasRemainder();
            }
            Logger.log(Logger.INFO, this.getClass().getName(), "ACT_COMBINATION_SYNC_RESOURCE -- hasRemainder: " + this.hasRemainder);
            if (this.hasRemainder)
            {
                this.hasRemainder = false;
                sendRemainderResourceRequest();
            }
            else
            {
                step = STEP_SYNC_FINISH;
                
                try
                {
                    if (hasAirport)
                    {
                        DaoManager.getInstance().getAddressDao().parseAirports(DaoManager.getInstance().getResourceBarDao().getAirportNode());
                    }
                }
                catch(Exception e)
                {
                    Logger.log(this.getClass().getName(), e);
                }
                
                try
                {
                    if (hasClientSetting && this.resourceBarDao.getPreferenceSetting(this.getRegion()) != null)
                    {
                        DaoManager.getInstance().getPreferenceDao().loadPreferences(this.resourceBarDao.getPreferenceSetting(this.getRegion()));
                        DaoManager.getInstance().getPreferenceDao().store(this.getRegion());
                    }
                }
                catch(Exception e)
                {
                    Logger.log(this.getClass().getName(), e);
                }
            }
        }
        return this.status;
    }
    
    private void parseClientSetting(ProtoClientSetting clientSetting)
    {
        String prefVersion = clientSetting.getVersion();
        
        Logger.log(Logger.INFO, this.getClass().getName(), "---syncCserver--- parseClientSetting version : " + prefVersion);
        
        Vector details = clientSetting.getDetail();
        int size = details.size();
        StringBuffer buffer = new StringBuffer();
        StringBuffer firstLine = new StringBuffer();
        for(int i = 0 ; i < size; i ++)
        {
            ProtoProperty property = (ProtoProperty) details.elementAt(i);
            String key = property.getKey();
            String value = property.getValue();
            //hard code 1000 represents group. 
            if("1000".equals(key))
            {
                firstLine.append(key + " " + value + "\n");
            }
            else
            {
                buffer.append(key + " " + value + "\n");
            }
        }
        firstLine.append(buffer);
        
        Logger.log(Logger.INFO, this.getClass().getName(), "---syncCserver--- parseClientSetting : " + firstLine);
        
        this.resourceBarDao.setBackupPreferenceSetting(firstLine.toString().getBytes(), this.getRegion());
        this.resourceBarDao.setPreferenceSetting(firstLine.toString().getBytes(),this.getRegion());
        this.resourceBarDao.setPreferenceSettingVersion(prefVersion,this.getRegion());
        this.resourceBarDao.setPreferenceChecksum(PreferenceDao.getChecksum(firstLine.toString().getBytes()),this.getRegion());
        this.resourceBarDao.store();
    }

    private void parseResourceFormat(ProtoSyncResourceResp resp, ResourceBarDao resourceBarDao)
    {
        MandatoryNodeDao mNode = AbstractDaoManager.getInstance().getMandatoryNodeDao();
        if(resp.hasAudioFormat())
        {
            mNode.getMandatoryNode().getUserPrefers().audioFormat = resp.getAudioFormat();
            Logger.log(Logger.INFO, this.getClass().getName(), "ACT_COMBINATION_SYNC_RESOURCE -- AudioFormat: " + resp.getAudioFormat());
        }
        if(resp.hasImageType()) 
        {
            mNode.getMandatoryNode().getUserPrefers().imageType = resp.getImageType();
            Logger.log(Logger.INFO, this.getClass().getName(), "ACT_COMBINATION_SYNC_RESOURCE -- ImageType: " + resp.getImageType());
        }
        if(resp.hasParamsVersion())
        {
            resourceBarDao.setResourceFormatVersion(resp.getParamsVersion());      
            Logger.log(Logger.INFO, this.getClass().getName(), "ACT_COMBINATION_SYNC_RESOURCE -- ParamsVersion: " + resp.getParamsVersion());
        }
    }
    
    private void parseAudioInventory(ProtoSyncResourceResp resp, ResourceBarDao resourceBarDao)
    {
        Vector v = resp.getAudioInventory();
        if (v != null)
        {
            Logger.log(Logger.INFO, this.getClass().getName(), "ACT_COMBINATION_SYNC_RESOURCE -- AudioInventory size(): " + v.size());
            
            for (int i = 0; i < v.size(); i++)
            {
                ProtoAudioInventory inv = (ProtoAudioInventory) v.elementAt(i);

                int protoType = inv.getType();
                boolean isAudio = (protoType == IServerProxyConstants.TYPE_RESOURCE_STATIC_AUDIO);
                boolean isClearAll = inv.getIsClean();
                if (isClearAll)
                {
                    if (isAudio)
                    {
                        resourceBarDao.clearStaticAudio();
                    }
                    else
                    {
                        resourceBarDao.clearStaticAudioRule();
                    }
                }

                Vector audioDatas = inv.getAudioData();
                Logger.log(Logger.INFO, this.getClass().getName(), "ACT_COMBINATION_SYNC_RESOURCE -- AudioData size(): " + audioDatas.size());
                
                for (int j = 0; j < audioDatas.size(); j++)
                {
                    ProtoAudioData data = (ProtoAudioData) audioDatas.elementAt(j);
                    if (data == null)
                        continue;

                    boolean isClear = data.getIsClean();
                    if (data.getType() == protoType)
                    {
                        if (isClear)
                        {
                            if (isAudio)
                            {
                                resourceBarDao.removeStaticAudio(data.getId());
                            }
                            else
                            {
                                resourceBarDao.removeAudioRule(data.getId());
                            }
                        }
                        else if (data != null && data.getData() != null)
                        {
                            if (isAudio)
                            {
                                Logger.log(Logger.INFO, this.getClass().getName(), "ACT_COMBINATION_SYNC_RESOURCE -- Parse Audio ID: " + data.getId());
                                resourceBarDao.putStaticAudio(data.getId(),
                                    data.getData().toByteArray());
                            }
                            else
                            {
                                Logger.log(Logger.INFO, this.getClass().getName(), "ACT_COMBINATION_SYNC_RESOURCE -- Parse Audio Rule ID: " + data.getId());
                                resourceBarDao.putAudioRule(data.getId(),
                                    data.getData().toByteArray());
                            }
                        }
                    }
                }

                String timeStamp = inv.getVersion();
                if (isAudio)
                {
                    resourceBarDao.setAudioTimestamp(timeStamp);
                    resourceBarDao.refreshAudioInventory();
                }
                else
                {
                    resourceBarDao.setAudioRuleTimestamp(timeStamp);
                    resourceBarDao.refreshAudioRuleInventory();
                }
            }
        }
    }

    private void parseCenterPoint(String centerPoint)
    {
        if (centerPoint != null)
        {
            this.resourceBarDao.setCenterPoint(centerPoint, this.getRegion());
        }
    }
    
    private void parseHotCate(ProtoHotPoiCategoryCollection hotCate)
    {
        Logger.log(Logger.INFO, this.getClass().getName(), "---syncCserver---" + "parseHotCate: ");
        
        String hotPoiVersion = hotCate.getVersion();
        
        this.resourceBarDao.setHotPoiVersion(hotPoiVersion,this.getRegion());
        PoiCategory hotPoiCategory = new PoiCategory();
        swapHotCategory(hotCate.getHotCategories(), hotPoiCategory);
        this.resourceBarDao.setHotPoiNode(hotPoiCategory,this.getRegion());
        Logger.log(Logger.INFO, this.getClass().getName(), "ACT_COMBINATION_SYNC_RESOURCE -- hotPoiVersion: " + hotPoiVersion);
    }
    
    private void parseGobyEvents(ProtoGobyEventCategoryCollection events)
    {
        Logger.log(Logger.INFO, this.getClass().getName(), "---syncCserver---" + "parseGobyEvents: ");
        
        String eventVersion = events.getVersion();
        
        this.resourceBarDao.setLocalEventsVersion(eventVersion, this.getRegion());
        PoiCategory gobyEvents = new PoiCategory();
        swapGobyEvent(events.getGobyEventCategories(), gobyEvents);
        this.resourceBarDao.setLocalEvents(gobyEvents, this.getRegion());
        Logger.log(Logger.INFO, this.getClass().getName(), "ACT_COMBINATION_SYNC_RESOURCE -- gobyEventsVersion: " + eventVersion);
    }
    
    private void fakeLocalEvents()
    {
        String[] eventName = new String[]{
                "Weekend",
                "Live Music",
                "Bars",
                "Museums",
                "Theater",
                "Family Fun",
                "Kids Events",
                "Landmarks",
                "Sightseeing",
                "Sports",
                "Adventures",
                "Outdoors",
        };
        String[] imagePath = new String[]{
                "search_panel_icon_weekend_focused",
                "search_panel_icon_weekend_unfocused",
                "search_panel_icon_live_music_focused",
                "search_panel_icon_live_music_unfocused",
                "search_panel_icon_bars_focused",
                "search_panel_icon_bars_unfocused",
                "search_panel_icon_museums_focused",
                "search_panel_icon_museums_unfocused",
                "search_panel_icon_theater_focused",
                "search_panel_icon_theater_unfocused",
                "search_panel_icon_family_fun_focused",
                "search_panel_icon_family_fun_unfocused",
                "search_panel_icon_kids_events_focused",
                "search_panel_icon_kids_events_unfocused",
                "search_panel_icon_landmarks_focused",
                "search_panel_icon_landmarks_unfocused",
                "search_panel_icon_sightseeing_focused",
                "search_panel_icon_sightseeing_unfocused",
                "search_panel_icon_sports_focused",
                "search_panel_icon_sports_unfocused",
                "search_panel_icon_adventures_focused",
                "search_panel_icon_adventures_unfocused",
                "search_panel_icon_outdoors_focused",
                "search_panel_icon_outdoors_unfocused",
                };
        
        String[] fakeId = new String[]{
                "date-this-weekend",
                "live-music",
                "bars-and-pubs",
                "museums",
                "performing-arts-and-theater",
                "family-fun",
                "family-and-kids-events",
                "places-of-interest",
                "sightseeing-and-tours",
                "sports-events",
                "adventure-and-extreme",
                "outdoor-recreation",
        };
        
        PoiCategory events = new PoiCategory();
        
        int size = imagePath.length / 2;
        
        for (int i = 0; i < size; i++)
        {
            PoiCategory child = new PoiCategory();
            child.setName(eventName[i]);
            child.setFocusedImagePath(imagePath[i * 2]);
            child.setUnfocusedImagePath(imagePath[i * 2 + 1]);
            child.setEventId(fakeId[i]);
            events.addChild(child);
        }
        
        DaoManager.getInstance().getResourceBarDao().setLocalEvents(events, "");
    }
    
    private void parseBizCate(ProtoBizCategory bizCate, ProtoSyncResourceResp resp)
    {
        Logger.log(Logger.INFO, this.getClass().getName(), "---syncCserver---" + "parseBizCate: ");
        
        String categoryVersion = resp.getBizCategoryVersion();
        resourceBarDao.setCategoryVersion(categoryVersion,this.getRegion());
        
        PoiCategory hotPoiCategory = createBizCate(bizCate);
        resourceBarDao.setCategoryNode(hotPoiCategory,this.getRegion()); 
        Logger.log(Logger.INFO, this.getClass().getName(), "ACT_COMBINATION_SYNC_RESOURCE -- categoryVersion: " + categoryVersion);
    }
    
    private PoiCategory createBizCate(ProtoBizCategory bizCate)
    {
        PoiCategory category = new PoiCategory();
        category.setCategoryId((int) bizCate.getId());
        category.setName(bizCate.getName());
        if( bizCate.hasFlags() )
        {
        	category.setFlags(bizCate.getFlags());
        }
        Vector v = bizCate.getCategory();
        for (int i = 0; i < v.size(); i++)
        {
            ProtoBizCategory  subCate = (ProtoBizCategory)v.elementAt(i);
            category.addChild(createBizCate(subCate));
        }
        return category;
    }
    
    private void swapGobyEvent(Vector v, PoiCategory category)
    {
        for (int i = 0; i < v.size(); i++)
        {
            ProtoGobyEventCategory event = (ProtoGobyEventCategory)v.elementAt(i);
            PoiCategory tmpCategory = new PoiCategory();
            tmpCategory.setEventId(event.getEventId());
            tmpCategory.setName(event.getDisplayName());
            if(event.getImageId() != null && event.getImageId().trim().length() > 0)
            {
                String[] images = event.getImageId().split(",");
                if(images.length > 0)
                {
                    tmpCategory.setUnfocusedImagePath(images[0]);
                    if(images.length > 1)
                    {
                        tmpCategory.setFocusedImagePath(images[1]);
                    }
                    else
                    {
                        tmpCategory.setFocusedImagePath(images[0]);
                    }
                }
            }
            category.addChild(tmpCategory);
        }
    }
    
    private void swapHotCategory(Vector v, PoiCategory category)
    {
        for (int i = 0; i < v.size(); i++)
        {
            ProtoHotPoiCategory subCate = (ProtoHotPoiCategory)v.elementAt(i);
            PoiCategory tmpCategory = new PoiCategory();
            tmpCategory.setCategoryId((int) subCate.getId());
            tmpCategory.setName(subCate.getName());
            if(subCate.getImageId() != null && subCate.getImageId().trim().length() > 0)
            {
                String[] images = subCate.getImageId().split(",");
                if(images.length > 0)
                {
                    tmpCategory.setUnfocusedImagePath(images[0]);
                    if(images.length > 1)
                    {
                        tmpCategory.setFocusedImagePath(images[1]);
                    }
                    else
                    {
                        tmpCategory.setFocusedImagePath(images[0]);
                    }
                }
            }
            category.addChild(tmpCategory);
            swapHotCategory(subCate.getSubHotCategories(), tmpCategory);
        }
    }

    private void parseHotBrand(ProtoHotBrand hotBrand)
    {
        Logger.log(Logger.INFO, this.getClass().getName(), "---syncCserver---" + "hotBrand: ");
        
        String brandNameVersion = hotBrand.getVersion();
        resourceBarDao.setBrandNameVersion(brandNameVersion,this.getRegion());
        Vector nameList = hotBrand.getNameList();
        StringList list = new StringList();
        for (int i = 0; i < nameList.size(); i++)
        {
            list.add((String) nameList.elementAt(i));
        }
        resourceBarDao.setBrandNameNode(list,this.getRegion());
    }
    

    private void parseMapDataUpgradeInfo(Vector mapDataUpgradeInfo)
    {
        Logger.log(Logger.INFO, this.getClass().getName(), "---syncCserver---" + "parseMapDataUpgradeInfo: ");
        
        if(mapDataUpgradeInfo.size() < 1)
        {
            return;
        }
        
        Vector infos = new Vector();
        for(int i = 0; i < mapDataUpgradeInfo.size(); i++)
        {
            ProtoMapDataUpgradeInfo protoInfo = (ProtoMapDataUpgradeInfo) mapDataUpgradeInfo.elementAt(i);
            MapDataUpgradeInfo info = new MapDataUpgradeInfo();
            info.setName(protoInfo.getName());
            info.setUpgradeMode(protoInfo.getForceUpgrade() ? 1 : 0);// TODO how to set it
            info.setVersion(protoInfo.getDataVersion());
            info.setBuildNumber(protoInfo.getDataBuildNumber());
            info.setRegion(protoInfo.getRegion());
            info.setState(protoInfo.getState());
            info.setUrl(protoInfo.getUrl());
            info.setSummary(protoInfo.getMessage());
            info.setMapDataSize(protoInfo.getSize());
            infos.add(info);
            // Map Upgrade info will transfer to client each time client request it, server will do the version control check,
            // so if we received the data, we will remove it from the remaining requests.
            if (combination.contains(PrimitiveTypeCache.valueOf(ISyncCombinationResProxy.TYPE_MAP_DATA_UPGRADE)))
            {
                combination.removeElement(PrimitiveTypeCache.valueOf(ISyncCombinationResProxy.TYPE_MAP_DATA_UPGRADE));
            }
        }

        resourceBarDao.setMapDataUpgradeInfoVersion("0");
        resourceBarDao.setMapDataUpgradeInfo(infos);

    }

    private void parseAirports(ProtoAirPort airport)
    {
        if (airport != null)
        {
            Logger.log(Logger.INFO, this.getClass().getName(), "---syncCserver---" + "parseAirports: ");
            
            String airportVersion = airport.getVersion();
            resourceBarDao.setAirportVersion(airportVersion);
            
            Vector v = airport.getStop();
            if (v != null)
            {
                Vector airports = new Vector(v.size());
                for (int n = 0; n < v.size(); n++)
                {
                    ProtoStop protoStop = (ProtoStop)v.elementAt(n);
                    Stop stop = ProtoBufServerProxyUtil.convertStop(protoStop);
                    airports.addElement(stop);
                    Logger.log(Logger.INFO, this.getClass().getName(), "ACT_COMBINATION_SYNC_RESOURCE -- airport stop: i = " + n + " Stop: "+ stop.toString());
                }
                resourceBarDao.setAirportNode(airports);
            }
        }
    }
    
    private void parseDSRServerDrivenParameters(ProtoDsrSdpParams dsrSdp)
    {
        Logger.log(Logger.INFO, this.getClass().getName(), "---syncCserver---" + "parseDSRServerDrivenParameters:");
        
        ServerDrivenParamsDao serverDrivenDao = AbstractDaoManager.getInstance().getDsrServerDrivenParamsDao();

        StringMap stringMap = new StringMap();
        String version = dsrSdp.getVersion();
        Vector commonKeyValues = dsrSdp.getContents();
        Logger.log(Logger.INFO, this.getClass().getName(), "ACT_COMBINATION_SYNC_RESOURCE -- Dsr Sdp version: " + version);
        stringMap.put(ServerDrivenParamsDao.SERVER_DRIVEN_VERSION, version);

        for (int n = 0; n < commonKeyValues.size(); n++)
        {
            String mappingStr = (String) commonKeyValues.elementAt(n);
            int equalToken = mappingStr.indexOf('=');
            if (equalToken > 0)
            {
                String key = mappingStr.substring(0, equalToken);
                String value = mappingStr.substring(equalToken + 1);

                stringMap.put(key, value);
                Logger.log(Logger.INFO, this.getClass().getName(), "ACT_COMBINATION_SYNC_RESOURCE -- Dsr Sdp CommonKeyValue:(key, value) i = " + n + "( " + key + ", " + value + " )");
            }
        }

        serverDrivenDao.setServerParams(stringMap);
        serverDrivenDao.store();
    }
    
    private void parseServerDrivenParameters(ProtoSdpMessage sdp)
    {
        Logger.log(Logger.INFO, this.getClass().getName(), "---syncCserver---" + "parseServerDrivenParameters:");
        
        ServerDrivenParamsDao serverDrivenDao = AbstractDaoManager.getInstance().getServerDrivenParamsDao();
            
        StringMap stringMap = new StringMap();
        Vector commonKeyValues = sdp.getCommonKeyValue();
        String version =  null;
        if(sdp.hasVersion())
        {
            version = sdp.getVersion();
            Logger.log(Logger.INFO, this.getClass().getName(), "ACT_COMBINATION_SYNC_RESOURCE -- Sdp Version: " + version);
        }
            
        for(int n = 0; n < commonKeyValues.size(); n++)
        {
            String mappingStr = (String)commonKeyValues.elementAt(n);
            int equalToken = mappingStr.indexOf('=');
            if(equalToken > 0)
            {
                String key = mappingStr.substring(0, equalToken);
                String value = mappingStr.substring(equalToken + 1);
                stringMap.put(key, value);
                Logger.log(Logger.INFO, this.getClass().getName(),
                    "ACT_COMBINATION_SYNC_RESOURCE -- Sdp CommonKeyValue:(key, value) i = "
                            + n + "( " + key + ", " + value + " )");
            }
        }   
        Vector otherItems = sdp.getOtherItems();
        for(int i = 0; i < otherItems.size(); i++)
        {
            ProtoOtherItem item = (ProtoOtherItem) otherItems.elementAt(i);
        }
        serverDrivenDao.setServerParams(stringMap, this.getRegion(),version);
        serverDrivenDao.store();
    }
   
    public String getSyncResType(int type)
    {
        switch (type)
        {
            case TYPE_SERVICE_LOCATOR_INFO:
            {
                return SYNC_SERVICE_LOCATOR;
            }
            case TYPE_SERVER_DRIVEN_PARAMETERS:
            {
                return SYNC_SERVER_DRIVEN_PARAMETER;
            }
            case TYPE_RESOURCE_FORMAT:
            {
                return SYNC_RESOURCE_FORMAT;
            }
            case TYPE_POI_BRAND_NAME:
            {
                return SYNC_HOT_BRAND;
            }
            case TYPE_HOT_POI_CATEGORY:
            {
                return SYNC_HOT_POI_CATEGORY;
            }
            case TYPE_CATEGORY_TREE:
            {
                return SYNC_POI_CATEGORY_TREE;
            }
            case TYPE_AIRPORT:
            {
                return SYNC_AIR_PROT;
            }
            case TYPE_AUDIO_AND_RULE:
            {
                return SYNC_AUDIO_AND_RULE;
            }
            case TYPE_MAP_DATA_UPGRADE:
            {
                return SYNC_MAP_DATA_UPGRADE;
            }
            case TYPE_CAR_MODEL:
            {
                return SYNC_CAR_MODEL;
            }
            case TYPE_DSR_SDP_NODE:
            {
                return SYNC_DSR_SERVER_DRIVEN_PARAMETER;
            }
            case TYPE_PREFERENCE_SETTING:
            {
                return SYNC_PREFERENCE_SETTING;
            }
            case TYPE_REGION_CENTER_POINT:
            {
                return SYNC_REGION_CENTER_POINT;
            }
            case TYPE_GOBY_EVENT:
            {
                return SYNC_GOBY_EVENT;
            }
        }
        return null;
    }

    public int getStep()
    {
        return step;
    }
    
    public void cleanupSyncResourceRequest()
    {
        // reset requestNumber
        requestNumber = 0;
    }
}
