/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * SimpleConfigDao.java
 *
 */
package com.telenav.data.dao.misc;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.Enumeration;
import java.util.Vector;

import com.telenav.app.AbstractContactProvider.TnContact;
import com.telenav.data.dao.AbstractDao;
import com.telenav.data.datatypes.primitive.BytesList;
import com.telenav.data.datatypes.primitive.StringMap;
import com.telenav.data.serializable.SerializableManager;
import com.telenav.module.upsell.FeaturesManager;
import com.telenav.persistent.TnStore;

/**
 *@author fqming (fqming@telenav.cn)
 *@date Jul 21, 2010
 */
public class SimpleConfigDao extends AbstractDao
{
   //public static final int GPS_SOURCE_SELECT_INDEX = 1001; moved to GPSSourceDao
   //public static final int URL_GROUP_SELECT_INDEX = 1008;
    public static final int KEY_NETWORK_SELECTED_INDEX = 1022;
    public static final int KEY_LOCATION_SOURCE = 1023;
    public static final int C2DM_SENDER_GOOGLE_ACCOUNT = 1025;
    public static final int KEY_MVIEWER_HOST_IP = 1026;
    public static final int KEY_LOGGER_ENABLE = 1027;
    public static final int C2DM_SEND_FINISH_FLAG = 1028;
    public static final int HAS_UPDATED_OPENGL_RES = 1029;
    public static final int KEY_LOCAL_UNREVIEWED_ADDRESS_NUMBER = 1030;
    public static final int KEY_MAP_LAYER_SETTING = 1031;
    public static final int KEY_SDCARD_OUTPUT_LOGGER_ENABLE = 1033;
    public static final int KEY_SET_RESOURCE_PATH_ENABLE = 1034;
    public static final int KEY_IS_HOME_LAUCHED = 1035;
    public static final int KEY_NETWORK_UNREVIEWED_ADDRESS_NUMBER = 1036;
    public static final int KEY_BILLBOARD_HOST = 1037;
    public static final int KEY_REGION_SETTING = 1038;
    public static final int KEY_CACHED_REGION = 1039;
    public static final int KEY_SWITCHED_DATASET = 1040;
    public static final int KEY_MANUAL_SERVER_URL = 1041;
    public static final int KEY_MAP_COPYRIGHT = 1042;
    public static final int KEY_VELTI_TRACK_FLAG = 1043;
    public static final int KEY_TIME_VECTOR = 1044;
    public static final int KEY_IS_CURRENT_RATED = 1045;
    public static final int KEY_NET_ERROR_STAMP = 1046;
    public static final int KEY_TANS_ERROR_STAMP = 1047;
    public static final int KEY_LOST_GPS_STAMP = 1048;
    public static final int KEY_NAV_EXIT_ABNORMAL_STAMP = 1049;
    public static final int KEY_IS_PREVIOUS_RATED = 1050;
    
    public static final int KEY_SET_MAP_DOWNLOAD_CN_ENABLE = 1051;
    public static final int KEY_SETTING_DATA_NEED_UPLOAD = 1052;
    public static final int KEY_SHARE_ETA = 1053;
    public static final int KEY_MINI_MAP_WIDTH = 1054;
    public static final int KEY_MINI_MAP_HEIGHT = 1055;
    public static final int KEY_MINI_MAP_WIDTH_LAND = 1056;
    public static final int KEY_MINI_MAP_HEIGHT_LAND = 1057;
    public static final int KEY_SWITCH_AIRPLANE_MODE_ENABLE = 1058;
    
    public final static int KEY_USED_SHARE_SCOUT = 1059;
    public final static int KEY_USED_RATE_SCOUT  = 1060;
    
    //public static final int KEY_MAP_DOWNLOADED_STATUS_CHANGED = 1059;
    //public static final int KEY_MAP_DOWNLOADED_STATUS_CHANGED_MESSAGE = 1060;
    
    public static final int KEY_MAP_DOWNLOAD_DIR = 1061;
    public static final int KEY_CRASH_TIMES = 1062;
    
    public static final int KEY_IS_LOVE_APP_IN_FEEDBACK = 1063;
    
    public static final int KEY_HAS_SHOWN_ADD_SHARE_PANEL = 1064;
    public static final int KEY_SET_STUCK_MONITOR_ENABLE = 1065;

    public static final int KEY_KONTAGENT_GENERATE_EVENT_LOG_FILE = 1066;
    
    public static final int KEY_IS_HOME_AUTO_SHARE_ENABLED = 1067;
    
    public static final int KEY_IS_WORK_AUTO_SHARE_ENABLED = 1068;

    // preference for share ETA
    public static final short KEY_AUTO_SHARE_HOME_RECIPIENTS = 1069;

    public static final short KEY_AUTO_SHARE_WORK_RECIPIENTS = 1070;
    
    public static final int KEY_IS_REAL_TIME_SHARE_ENABLED = 1071;
    
    public static final int KEY_IS_REAL_TIME_SHARE_SETTED = 1072;
    
    public static final int KEY_USING_KONTAGENT_TEST_SERVER = 1073;
    
    public static final int KEY_HOME_WORK_NEED_UPLOAD = 1074;
    
    public static final int KEY_USER_PROFILE_NEED_UPLOAD = 1075;
    
    public static final int KEY_SET_MAP_DOWNLOAD_CN_URL = 1076;

    public static final int KEY_SET_MAP_DISK_CACHE_DISABLE = 1077;
    
    public static final int KEY_CARRIER_MAPPING = 1077;
    
    public static final int KEY_CONVERSION_TRACKED = 1078;
	
	public static final int KEY_RUNTIME_STATUS_LOG_ENABLE = 1079;
	
    public static final int KEY_RUNTIME_STATUS_LOG_INTERVAL = 1080;

    public static final int KEY_DISABLE_ROUTE_IN_SATELLITE = 1081;
    
    public static final int KEY_ALONG_ROUTE_SPEED = 1082;
    public static final int KEY_GCM_ENABLE = 1083;
    public static final int KEY_SMS_DISABLE = 1084;
    
    public static final int KEY_EXIT_APP_NORMALLY = 1085;
    public static final int KEY_LOCAL_MISLOG_ENABLE = 1086;
    
    public static final int KEY_IS_NAVSDK_POPUP_DELAY = 1087;
    public static final int KEY_NAVSDK_POPUP_ERROR_MSG = 1088;
    
    private TnStore simpleConfigStore;
    private TnStore carrierMappingStore;

    public SimpleConfigDao(TnStore simpleConfigStore, TnStore carrierMappingStore)
    {
        this.simpleConfigStore = simpleConfigStore;
        this.carrierMappingStore = carrierMappingStore;
    }
    
    public void remove(int key)
    {
        simpleConfigStore.remove(key);
    }
    
    public void put(int key, int value)
    {
        simpleConfigStore.put(key, ("" + value).getBytes());
    }
    
    public int get(int key)
    {
        byte[] data = simpleConfigStore.get(key);
        
        if(data != null)
        {
            String s = new String(data);
            
            return Integer.parseInt(s);
        }
        
        return -1;
    }

    public void put(int key, String value)
    {
        if (value != null)
        {
            simpleConfigStore.put(key, value.getBytes());
        }
    }
    
    public void putVector(int key, Vector<String> regions)
    {
        int size = regions.size();
        BytesList list = new BytesList();
        for (int i = 0; i < size; i++)
        {
            String region = (String) regions.elementAt(i);
            list.add(region.getBytes());
        }
        simpleConfigStore.put(key, SerializableManager.getInstance()
                .getPrimitiveSerializable().toBytes(list));
    }
    
    public Vector<String> getVector(int key)
    {
        Vector<String> regions = new Vector<String>();
        byte[] data = simpleConfigStore.get(key);
        if(data != null)
        {
            BytesList list = SerializableManager.getInstance().getPrimitiveSerializable()
            .createBytesList(data);
            if (list != null)
            {
                
                for (int i = 0; i < list.size(); i++)
                {
                    String region = new String(list.elementAt(i));
                    regions.addElement(region);
                }
            }
        }
        return regions;
    }
    
    public void putInteger(int key, int value)
    {
        this.put(key, value);
    }
    
    public int getInteger(int key)
    {
        String s = getString(key);
        if(s == null || s.length() == 0)
            return -1;
        
        return Integer.parseInt(s);
    }
    
    public void put(int key, boolean value)
    {
        this.put(key, value ? "1" : "0");
    }
    
    public boolean getBoolean(int key)
    {
        String s = getString(key);
        if(s == null || s.length() == 0)
            return false;
        
        return Integer.parseInt(s) == 1 ? true : false;
    }
    
    public String getString(int key)
    {
        byte[] data = simpleConfigStore.get(key);

        if (data != null)
        {
            String s = new String(data);
            return s;
        }

        return null;
    }
    
    public void store()
    {
        this.simpleConfigStore.save();
    }
    
    public void clear()
    {
        this.simpleConfigStore.clear();
    }
    
    public void setContactVecValue(int keyVRecipient, Vector<TnContact> valContact)
    {
        int size = valContact.size();
        BytesList list = new BytesList();
        for (int i = 0; i < size; i++)
        {
            TnContact tnContact = (TnContact) valContact.elementAt(i);
            if(getByteArray(tnContact) != null)
            {
                list.add(getByteArray(tnContact));
            }
        }
        simpleConfigStore.put(keyVRecipient, SerializableManager.getInstance()
                .getPrimitiveSerializable().toBytes(list));
        
    }

    public Vector<TnContact> getContactVecValue(int keyVRecipient)
    {
        Vector<TnContact> shareContacts = new Vector<TnContact>();
        byte[] data = simpleConfigStore.get(keyVRecipient);
        if(data != null)
        {
            BytesList list = SerializableManager.getInstance().getPrimitiveSerializable().createBytesList(data);
            if (list != null)
            {
                
                for (int i = 0; i < list.size(); i++)
                {
                    TnContact tnContact = (TnContact) readFromByteArray(list.elementAt(i));
                    if(tnContact != null)
                    {
                        shareContacts.addElement(tnContact);
                    }
                }
            }
        }
        return shareContacts;
    }
    
    private byte[] getByteArray(TnContact tnContact)
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = null;
        try
        {
            out = new ObjectOutputStream(bos);
            out.writeObject(tnContact);
            byte[] yourBytes = bos.toByteArray();
            return yourBytes;
        }
        catch (IOException e)
        {
            return null;
        }
        finally
        {
            try
            {
                out.close();
                bos.close();
            }
            catch (IOException e)
            {
               
            }

        }
    }
    
    private Object readFromByteArray(byte[] data)
    {
        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        ObjectInput in = null;
        try
        {
            in = new ObjectInputStream(bis);
            Object o = in.readObject();
            return o;
        }
        catch (Exception e)
        {
            return null;
        }
        finally
        {
            try
            {
                bis.close();
                in.close();
            }
            catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }
    
    // Per PRD, traffic flow should be default on
    public void initTrafficLayerDaoSetting()
    {
        int layerSetting = ((DaoManager) DaoManager.getInstance()).getSimpleConfigDao().get(
            SimpleConfigDao.KEY_MAP_LAYER_SETTING);

        if (layerSetting < 0)
        {
            int trafficFeature = FeaturesManager.getInstance().getStatus(FeaturesManager.FEATURE_CODE_MAP_LAYER_TRAFFIC_FLOW);
            if (trafficFeature == FeaturesManager.FE_ENABLED || trafficFeature == FeaturesManager.FE_PURCHASED)
            {
                layerSetting = 1;
                put(SimpleConfigDao.KEY_MAP_LAYER_SETTING, layerSetting);
                store();
            }
        }
    }
    
    public StringMap getCarrierMapping()
    {
        byte[] data = carrierMappingStore.get(KEY_CARRIER_MAPPING);
        
        if(data == null)
        {
            return null;
        }
        
        StringMap map = SerializableManager.getInstance().getPrimitiveSerializable().createStringMap(data);
        
        return map;
    }
    
    public void initCarrierMapping(TnStore mappingStore)
    {
        Enumeration<String> enums = carrierMappingStore.keys();
        while(enums.hasMoreElements())
        {
            String key = enums.nextElement();
            byte[] data = carrierMappingStore.get(key);
            
            mappingStore.put(key, data);
        }
        
        mappingStore.save();
        
        carrierMappingStore.clear();
        
        StringMap map = new StringMap();
        //from Android API
        map.put("SPRINT", "SPRINT");
        map.put("CHAMELEON", "SPRINT");
        map.put("VIRGIN MOBILE", "VIRGIN_MOBILE_US");
        map.put("BOOST MOBILE", "BOOST");
        map.put("310260", "TMOBILE");
        map.put("T-MOBILE", "TMOBILE");
        map.put("T-MOBILE@", "TMOBILE");
        map.put("46002", "TMOBILE");
        map.put("46000", "TMOBILE");
        map.put("310410", "ATT");
        map.put("HOME", "ATT");
        map.put("METROPCS", "METROPCS");
        map.put("311660", "METROPCS");
        map.put("US CELLULAR", "US_CELLULAR");
        map.put("311480", "VERIZON");
        map.put("VERIZON", "VERIZON");
        map.put("VERIZON WIRELESS", "VERIZON");
        map.put("VZW", "VERIZON");
        //from 3rd party, for back up
        map.put("AIRTEL", "AIRTEL"); 
        map.put("ALLTEL", "ALLTEL"); 
        map.put("AT&", "ATT"); 
        map.put("CINGUL", "ATT"); 
        map.put("ATT", "ATT"); 
        map.put("CINGULAR", "ATT"); 
        map.put("CINGULA", "ATT"); 
        map.put("AT&T", "ATT"); 
        map.put("CINGUL'R", "ATT"); 
        map.put("ATTNAVPROG", "ATT"); 
        map.put("CINGULAR-GSM", "ATT"); 
        map.put("ATT IPHONE OFF_BOARD", "ATT"); 
        map.put("ATT IPHONE ON_BOARD", "ATT"); 
        map.put("BELL_C", "BELL_CANADA"); 
        map.put("VMC", "BELL_CANADA"); 
        map.put("VIRGIN", "BELL_CANADA"); 
        map.put("BELL_ALIANT", "BELL_CANADA"); 
        map.put("BELLMOB", "BELL_CANADA"); 
        map.put("BELL", "BELL_CANADA"); 
        map.put("BELL MOBILITY", "BELL_CANADA"); 
        map.put("BOOST", "BOOST"); 
        map.put("C SPIRE", "C_SPIRE"); 
        map.put("CINCIN", "CINCINATI_BELL"); 
        map.put("CIN_BELL", "CINCINATI_BELL"); 
        map.put("CINCINNATIBELL", "CINCINATI_BELL"); 
        map.put("CMCC", "CMCC"); 
        map.put("H3G", "HUTCHISON3G"); 
        map.put("HUTCHI", "HUTCHISON3G"); 
        map.put("HUTCHISON3G", "HUTCHISON3G"); 
        map.put("MMI", "MMI"); 
        map.put("MMI_DEMO", "MMI_DEMO"); 
        map.put("NII_BRAZI", "NEXTEL_BRAZIL"); 
        map.put("NIIBR", "NEXTEL_BRAZIL"); 
        map.put("NII-BRAZIL", "NEXTEL_BRAZIL"); 
        map.put("NII BRAZIL", "NEXTEL_BRAZIL"); 
        map.put("NII_MEXIC", "NEXTEL_MEXICO"); 
        map.put("NII_MEXICO", "NEXTEL_MEXICO"); 
        map.put("NII", "NEXTEL_MEXICO"); 
        map.put("NII-MEXICO", "NEXTEL_MEXICO"); 
        map.put("NII MEXICO", "NEXTEL_MEXICO"); 
        map.put("ROGER", "ROGERS"); 
        map.put("ROGERS", "ROGERS"); 
        map.put("FIDO", "ROGERS"); 
        map.put("SOUTH", "SOUTHERN_LINC"); 
        map.put("LINC", "SOUTHERN_LINC"); 
        map.put("SLINC", "SOUTHERN_LINC"); 
        map.put("NEXTEL", "SPRINT"); 
        map.put("SPRINTPCS", "SPRINT"); 
        map.put("SNNAVPROG", "SPRINT"); 
        map.put("SPRINTMVNO", "SPRINT"); 
        map.put("SPRINT PCS", "SPRINT"); 
        map.put("MOBILE-UK", "TMOBILE_UK"); 
        map.put("T_MOBILE_UK", "TMOBILE_UK"); 
        map.put("TMOBILE UK", "TMOBILE_UK"); 
        map.put("TMOUKNAVPROG", "TMOBILE_UK"); 
        map.put("T-MOBILE-UK", "TMOBILE_UK"); 
        map.put("T-MOBILE UK", "TMOBILE_UK"); 
        map.put("T-MOBILE", "TMOBILE"); 
        map.put("TMOUSNAVPROG", "TMOBILE"); 
        map.put("TELCEL", "TELCEL"); 
        map.put("TELCEL GSM", "TELCEL"); 
        map.put("TELEFONIC", "TELEFONICA"); 
        map.put("TELEFONICA", "TELEFONICA"); 
        map.put("USCC", "US_CELLULAR"); 
        map.put("USCCNAVPROG", "US_CELLULAR"); 
        map.put("U.S. CELLULAR", "US_CELLULAR"); 
        map.put("VERIZO", "VERIZON"); 
        map.put("VERIZON", "VERIZON"); 
        map.put("VERIZON WIRELESS", "VERIZON"); 
        map.put("VIVO", "VIVO"); 
        map.put("VIVO_BRAZIL", "VIVO"); 
        map.put("VIVOGSM", "VIVO"); 
        map.put("VODAFONE", "VODAFONE"); 
        map.put("VODAFONE INDIA", "VODAFONE_INDIA"); 

        carrierMappingStore.put(KEY_CARRIER_MAPPING, SerializableManager.getInstance().getPrimitiveSerializable().toBytes(map));
        carrierMappingStore.save();
    }
    
    public void restoreKeyMapping(TnStore backupStore)
    {
        carrierMappingStore.clear();
        
        Enumeration<String> enums = backupStore.keys();
        while(enums.hasMoreElements())
        {
            String key = enums.nextElement();
            byte[] data = backupStore.get(key);
            
            carrierMappingStore.put(key, data);
        }
        
        carrierMappingStore.save();
    }
    
    public void load()
    {
        simpleConfigStore.load();
        carrierMappingStore.load();
    }
    
    public void loadCarrierMapping()
    {
        carrierMappingStore.load();
    }
}
