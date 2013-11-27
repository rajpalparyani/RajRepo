/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * PreferenceDao.java
 *
 */
package com.telenav.data.dao.misc;

import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import com.telenav.data.dao.AbstractDao;
import com.telenav.data.dao.serverproxy.ResourceBarDao;
import com.telenav.data.dao.serverproxy.TnRegionDependentStoreProvider;
import com.telenav.data.datatypes.preference.Preference;
import com.telenav.data.datatypes.preference.PreferenceGroup;
import com.telenav.logger.Logger;
import com.telenav.module.AppConfigHelper;
import com.telenav.module.upsell.FeaturesManager;
import com.telenav.persistent.TnStore;
import com.telenav.util.PrimitiveTypeCache;
import com.telenav.util.StringTokenizer;

/**
 * 
 *@author Albert Ma (byma@telenav.cn)
 *@date 2010-8-11
 */
public class PreferenceDao extends AbstractDao
{
	public final static int INVALID_INT_VALUE = -1;
	public final static int SYNC_PREFERENCE_SETTING_DATA_TIME = 10001;
	
    private final static int TN_PREFERENCES_INT_ID_OFFSET = 0x98989898;
    private final static int TN_PREFERENCES_STR_ID_OFFSET = 0x12121212;
    private final static int FILTER_PREFERENCES_HIDE_GROUPS_INDEX = 8000;
    
    private PreferenceGroup[] prefGroups;
    private PreferenceGroup[] filterPrefGroups;
    private Hashtable preferenceParam;
    private boolean isNeedUpload;
    private boolean isBackUpLoaded = false;
    private TnRegionDependentStoreProvider prefStoreProvider;
    
    /**
     * Constructor of preference DAO
     * @param prefStore
     */
    public PreferenceDao(TnStore prefStore)
    {
        prefStoreProvider = new TnRegionDependentStoreProvider(prefStore.getName(),prefStore.getType());
        this.preferenceParam = new Hashtable();
    }

    /**
     * Set the preference store to the DAO, need to call {@link #load(String)} to do the reload.
     * @param prefStore
     */
    public void setDataStore(TnStore prefStore)
    {
        prefStoreProvider = new TnRegionDependentStoreProvider(prefStore.getName(),prefStore.getType());
    }
    
    public String getSyncPreferenceSettingDataTime(String region)
    {
        byte[] versionData = prefStoreProvider.getStore(region).get(SYNC_PREFERENCE_SETTING_DATA_TIME);
        if(versionData != null)
        {
            return new String(versionData);
        }
        return "0";
    }
    
    public void setSyncPreferenceSettingDataTime(String timeStamp, String region)
    {
        if(timeStamp != null)
        {
            prefStoreProvider.getStore(region).put(SYNC_PREFERENCE_SETTING_DATA_TIME, timeStamp.getBytes());
        }
    }
    
    /**
     * remove all of the data from persistent object.
     */
    public void clear()
    {
    	clearPreferenceCache();
    	prefStoreProvider.clear();
    }

    public void clearPreferenceCache()
    {
        preferenceParam.clear();
    	prefGroups = null;
    }
    
    public void resetTempPreference()
    {
        if(preferenceParam != null && preferenceParam.size() > 0)
        {
            Enumeration prefEnum = this.preferenceParam.keys();
            while(prefEnum.hasMoreElements())
            {
                Preference pref = (Preference)this.preferenceParam.get(prefEnum.nextElement());
                pref.setIntValue(0);
            }
        }
    }
    
    /**
     * Need to store the cache to persistent data.
     */
    public void store(String region)
    {
        Enumeration prefEnum = this.preferenceParam.keys();
        while(prefEnum.hasMoreElements())
        {
            byte[] preIntValue = new byte[1];
            Preference pref = (Preference)this.preferenceParam.get(prefEnum.nextElement());
            if (pref.isUpdated())
            {
                // get the setting value of the pref , don't put the whole pref into store!!!
                Logger.log(Logger.INFO, this.getClass().getName(), "CR START " + pref.getId() + " : " + pref.getIntValue());          
                preIntValue[0] = (byte) pref.getIntValue();
                this.prefStoreProvider.getStore(region).put(
                    pref.getId() + TN_PREFERENCES_INT_ID_OFFSET, preIntValue);
                
                String strValue = pref.getStrValue();
                if(strValue != null)
                {
                    this.prefStoreProvider.getStore(region).put(
                        pref.getId() + TN_PREFERENCES_STR_ID_OFFSET , strValue.getBytes());
                }
                pref.setUpdated(false);
            }
        }
        
        Logger.log(Logger.INFO, this.getClass().getName(), "CR END ");    
        this.prefStoreProvider.save();
    }
    
    public Hashtable getSettingData()
    {
        Hashtable settingData = new Hashtable();
        
        Enumeration prefEnum = this.preferenceParam.keys();
        while(prefEnum.hasMoreElements())
        {
            Preference pref = (Preference)this.preferenceParam.get(prefEnum.nextElement());
            if(pref.getIntValue() != INVALID_INT_VALUE)
            {
                settingData.put(pref.getId()+"", pref.getIntValue()+"");
            }
        }
        
        return settingData;
    }
    
    /**
     * Retrieve the preference groups
     * @param region TODO
     * @return
     */
    public PreferenceGroup[] getPreferenceGroups(String region)
    {
        if ((prefGroups == null || prefGroups.length == 0) && !isBackUpLoaded)
        {
            loadBackUpPreference(region);
        }
        return this.prefGroups;
    }
    
    public PreferenceGroup[] getFilterPreferenceGroups(String region)
    {
        if ((filterPrefGroups == null || filterPrefGroups.length == 0) && !isBackUpLoaded)
        {
            loadBackUpPreference(region);
        }
        return this.filterPrefGroups;
    }
    
    public int[] getPreferenceIdsByGroup(int groupId, String region)
    {
        if (this.prefGroups != null)
        {
            for (int i = 0; i < this.prefGroups.length; i++)
            {
                if (this.prefGroups[i].getId() == groupId)
                {
                    return this.prefGroups[i].getPreferenceIds();
                }
            }
        }

        if (!this.isBackUpLoaded)
        {
            loadBackUpPreference(region);
            return getPreferenceIdsByGroup(groupId, region);
        }

        return null;
    }   
    
    /**
     * Retrieve preference from cache. 
     * @param id
     * @return
     */
    public Preference getPreference(int id)
    {
    	Preference pref = (Preference)preferenceParam.get(PrimitiveTypeCache.valueOf(id));
    	return pref;
    }
    
    /**
     * Retrieve integer value from preference cache.
     * @param id
     * @return
     */
    public int getIntValue(int id)
    {
    	Preference pref = (Preference)preferenceParam.get(PrimitiveTypeCache.valueOf(id));
    	int intVal = INVALID_INT_VALUE;
    	try
    	{
    	    if(pref != null)
    	    {
    	        intVal = pref.getIntValue();
    	        if (id == Preference.ID_PREFERENCE_ROUTE_SETTING)
    	        {
    	            int trafficEnableValue = FeaturesManager.getInstance().getStatus(FeaturesManager.FEATURE_CODE_TRAFFIC);
    	            boolean isTrafficEnabled = trafficEnableValue == FeaturesManager.FE_ENABLED || trafficEnableValue == FeaturesManager.FE_PURCHASED;
//    	            isTrafficEnabled = false;
    	            if (!isTrafficEnabled)
    	            {
                        if ((pref.getIntValue() & Preference.AVOID_TRAFFIC_DELAYS) != 0)
                        {
                            intVal = pref.getIntValue() - Preference.AVOID_TRAFFIC_DELAYS;
                        }
    	            }
    	        }
    	    }
    	}catch(Throwable t)
    	
    	{
    		Logger.log(this.getClass().getName(), t);
    	}
    	return intVal;
    	
    }
    
    /**
     * Set integer value to preference cache.
     * @param id
     * @param value
     */
    public void setIntValue(int id, int value)
    {
    	Preference pref = (Preference) preferenceParam.get(PrimitiveTypeCache.valueOf(id));
    	try
    	{
    		pref.setIntValue(value);
    	}catch(Throwable t)
    	{
    		Logger.log(this.getClass().getName(), t);
    	}
    }
    
    /**
     * Retrieve preference string value from cache.
     * @param id
     * @return
     */
    public String getStrValue(int id)
    {
    	Preference pref = (Preference)preferenceParam.get(PrimitiveTypeCache.valueOf(id));
    	String valStr = "";
    	try
    	{
    	    if(pref != null)
    	        valStr = pref.getStrValue();
    	}catch(Throwable t)
    	{
    		Logger.log(this.getClass().getName(), t);
    	}
    	return valStr;
    }
    
    /**
     * Set string value to the Preference cache.
     * @param id
     * @param valStr
     */
    public void setStrValue(int id, String valStr)
    {
    	Preference pref = (Preference) preferenceParam.get(PrimitiveTypeCache.valueOf(id));
    	try
    	{
    	    if(pref == null)
    	    {
    	        pref = new Preference(id, "");
    	        preferenceParam.put(PrimitiveTypeCache.valueOf(id), pref);
    	    }
    		pref.setStrValue(valStr);
    	}catch(Throwable t)
    	{
    		Logger.log(this.getClass().getName(), t);
    	}
    }
    
    /**
     * Load the data from store which was passed from constructor {@link #PreferenceDao(TnStore)}
     * @param region TODO
     * @param whether reload the preference
     * @return true, loading successfully; false, loading failed.
     */
    public boolean load(boolean isForce, String region)
    {
        if (isForce || preferenceParam.isEmpty() || prefGroups == null || prefGroups.length == 0)
        {
            ResourceBarDao resourceBarDao = DaoManager.getInstance().getResourceBarDao();
            if (resourceBarDao != null)
            {
                byte[] defaultPreferenceSettings = resourceBarDao.getPreferenceSetting(region);
                if (defaultPreferenceSettings != null && compareChecksum(getChecksum(defaultPreferenceSettings), region))
                {
                    this.loadPreferences(defaultPreferenceSettings);
                    this.isBackUpLoaded = false;
                }
                else
                {
                    // some error has happened in static preference data , need downLoad it again
                    if (!loadBackUpPreference(region))
                    {
                        return false;
                    }
                }
            }

            // merge the setting data.
            if (!preferenceParam.isEmpty() && this.prefStoreProvider != null)
            {
                mergeSettingData(region);
            }
            else if (preferenceParam.isEmpty() && this.prefStoreProvider != null && !this.isBackUpLoaded)
            {
                loadBackUpPreference(region);
                mergeSettingData(region);
            }
        }

        return true;
    }
    
    private void mergeSettingData(String region)
    {
        try
        {
            TnStore store = this.prefStoreProvider.getStore(region);
           
            Enumeration keys = store.keys();
            if (keys != null)
            {
                while (keys.hasMoreElements())
                {
                    String keyStr = (String) keys.nextElement();
                    int keyIdInStore = Integer.valueOf(keyStr).intValue();
                    if (Math.abs(keyIdInStore) > SYNC_PREFERENCE_SETTING_DATA_TIME)
                    {
                        if (this.preferenceParam.containsKey(keyIdInStore
                                - TN_PREFERENCES_INT_ID_OFFSET))
                        {
                            Preference pref = (Preference) this.preferenceParam
                                    .get(keyIdInStore - TN_PREFERENCES_INT_ID_OFFSET);
                            pref.setIntValue(store.get(keyIdInStore)[0]);
                        }
                        else if (this.preferenceParam.containsKey(keyIdInStore
                                - TN_PREFERENCES_STR_ID_OFFSET))
                        {
                            Preference pref = (Preference) this.preferenceParam
                                    .get(keyIdInStore - TN_PREFERENCES_STR_ID_OFFSET);
                            try
                            {
                                pref.setStrValue(new String(store.get(keyIdInStore),
                                        "UTF-8"));
                            }
                            catch (UnsupportedEncodingException e)
                            {
                                e.printStackTrace();
                            }
                        }
                        else
                        {
                            int tempStrId = Math.abs(keyIdInStore
                                    - TN_PREFERENCES_STR_ID_OFFSET);
                            int tempIntId = Math.abs(keyIdInStore
                                    - TN_PREFERENCES_INT_ID_OFFSET);
                            boolean isInt = tempIntId < tempStrId;
                            int tempPrefId = Math.min(tempStrId, tempIntId);
                            Preference pref = new Preference(tempPrefId, "");
                            if (isInt)
                            {
                                pref.setIntValue(store.get(keyStr)[0]);
                            }
                            else
                            {
                                try
                                {
                                    pref.setStrValue(new String(store.get(keyStr),
                                            "UTF-8"));
                                }
                                catch (UnsupportedEncodingException e)
                                {
                                    e.printStackTrace();
                                }
                            }
                            this.preferenceParam.put(tempPrefId, pref);
                        }
                    }
                }
            }
        }
        catch (NumberFormatException e)
        {

        }
    }
    
    private boolean loadBackUpPreference(String region)
    {
        if (Logger.DEBUG)
        {
            Logger.log(Logger.ERROR, this.getClass().getName(), "Preference-Corruption-error in static preference data , load backup!!!");
        }        
        this.isBackUpLoaded = true;        
        ResourceBarDao resourceBarDao = DaoManager.getInstance().getResourceBarDao();
        if (resourceBarDao != null)
        {
            resourceBarDao.setPreferenceSettingVersion("", region);
            resourceBarDao.store();
            byte[] backupDefaultPreferenceSettings = resourceBarDao.getBackupPreferenceSetting(region);
            if (backupDefaultPreferenceSettings != null)
            {
                this.clearPreferenceCache();
                this.loadPreferences(backupDefaultPreferenceSettings);
                return true;
            }
        }
        return false;
    }
    
    private boolean compareChecksum(String currChecksum, String region)
    {
        try
        {
            ResourceBarDao resourceBarDao = DaoManager.getInstance().getResourceBarDao();
            if (resourceBarDao != null)
            {
                String sumValueInDao = resourceBarDao.getPreferenceChecksum(region);
                return sumValueInDao == null ? false : sumValueInDao.equals(currChecksum);
            }
        }
        catch (Exception e)
        {
            if (Logger.DEBUG)
            {
                Logger.log(Logger.INFO, this.getClass().getName(), "Preference-Corruption-error check sum value!!!");
            }
        }
        return false;
    }
    
    /**
     * Load the data from store which was passed from constructor {@link #PreferenceDao(TnStore)}
     * @param region TODO
     * @param type the loading type of the data.
     * @return true, loading successfully; false, loading failed.
     */
    public boolean load(String region)
    {
        return this.load(false, region);
    }
    
    public static int index2Value(Preference pref, int index)
    {
        if(pref == null)
            return 0;
        int len = pref.getOptionValues().length;
        int optionVals[] = pref.getOptionValues();
        if(index >= 0 && index < len)
        {
            return optionVals[index];
        }else
        {
            return optionVals[0];
        }
    }
    
    public static int value2Index(Preference pref, int value)
    {
        if(pref == null)
            return 0;

        int len = pref.getOptionValues().length;
        int optionVals[] = pref.getOptionValues();
        for (int i = 0; i < len; i++)
        {
            if (optionVals[i] == value)
            {
                return i;
            }
        }
        return 0;
    }

    public void loadPreferences(byte[] data)
    {
        parsePreferenceNodeBuffer(data);
    }
    
    public void setIsNeedUpload(boolean isNeedUpload)
    {
        this.isNeedUpload = isNeedUpload;
    }
    
    public boolean isNeedUpload()
    {
        return isNeedUpload;
    }

    private void parsePreferenceNodeBuffer(byte[] buff)
    {
        try
        {
            int ind = 0;
            boolean first = true;
            for (int i = 0; i < buff.length; i++)
            {
                //10 here represents "\n"
                if (buff[i] == 10)
                {
                    String str = new String(buff, ind, (i - ind)).trim();

                    if (first)
                    {
                        firstAdd(str);
                        first = false;
                    }
                    else
                    {
                        otherAdd(str);
                    }
                    ind = i + 1;
                }
                else if (i == buff.length - 1)
                {
                    String str = new String(buff, ind, (i - ind) + 1).trim();
                    if (first)
                    {
                        firstAdd(str);
                        first = false;
                    }
                    else
                    {
                        otherAdd(str);
                        // node.setChildAt(otherAdd(str),index++);
                    }
                    ind = i + 1;
                }
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private void firstAdd(String str)
    {
        str = str.trim();
        int ind = str.indexOf('\t');
        if (ind == -1)
        {
            ind = str.indexOf(' ');
        }
        if (ind != -1)
        {
            String s2 = str.substring(ind).trim();
            StringTokenizer st = new StringTokenizer(s2, ",");
            Vector preferGroups = new Vector();
            while(st.hasMoreTokens())
            {
                String group = st.nextToken();
                int equalIndex = group.indexOf('=');
                PreferenceGroup prefGroup = new PreferenceGroup(Integer.parseInt(group.substring(equalIndex + 1)), group.substring(0, equalIndex));
                preferGroups.addElement(prefGroup);
            }
            
            this.prefGroups = new PreferenceGroup[preferGroups.size()];
            for(int i = 0 ; i < prefGroups.length ; i ++)
            {
                prefGroups[i] = (PreferenceGroup)preferGroups.elementAt(i);
            }
            filterPrefGroups();
        }
    }

    private void otherAdd(String str)
    {
        str = str.trim();
        int ind = str.indexOf('\t');
        if (ind == -1)
        {
            ind = str.indexOf(' ');
        }
        if (ind != -1)
        {
            String s1 = str.substring(0, ind).trim();
            String s2 = str.substring(ind).trim();
            int n = Integer.parseInt(s1);
            int equalIndex = s2.indexOf('=');
            Preference preference = new Preference(n, s2.substring(0, equalIndex));
            Preference defaultPreference = getPreference(preference.getId());
            if(defaultPreference != null)
            {
                preference.setIntValue(defaultPreference.getIntValue());
                preference.setStrValue(defaultPreference.getStrValue());
            } 
            preferenceParam.put(PrimitiveTypeCache.valueOf(preference.getId()), preference);
            
            String valueStr = s2.substring(equalIndex + 1);
            StringTokenizer st = new StringTokenizer(valueStr, ",");
            Vector values = new Vector();
            while(st.hasMoreTokens())
            {
                String value = st.nextToken();
                values.addElement(value);
            }
            int firstValue = Integer.parseInt((String)values.elementAt(0));
            if (values.size() > 1)
            {
                Vector preferLabelNames = new Vector();
                Vector preferLabelValues = new Vector();
                for (int i = 1; i < values.size(); i++)
                {
                    String group = (String)values.elementAt(i);
                    equalIndex = group.indexOf('#');
                    preferLabelNames.addElement(group.substring(0, equalIndex));
                    preferLabelValues.addElement(PrimitiveTypeCache.valueOf(Integer.parseInt(group.substring(equalIndex + 1))));
                }
                String[] labelNames = new String[preferLabelNames.size()];
                int[] labelValues = new int[labelNames.length];
                if(defaultPreference == null) 
                {
                    if (preference.getId() == Preference.ID_PREFERENCE_DISTANCEUNIT)
                    {
                        preference.setIntValue(Preference.UNIT_USCUSTOM);
                    }
                    else
                    {
                        preference.setIntValue(((Integer)preferLabelValues.elementAt(0)).intValue());
                    }
                }
                for(int i = 0; i < preferLabelNames.size(); i++)
                {
                    if(preference.getId() == Preference.ID_PREFERENCE_LANGUAGE)
                    {
                        labelNames[i] = (String)preferLabelNames.elementAt(i);
                    }
                    else
                    {
                        labelNames[i] = parseGuideToneName((String)preferLabelNames.elementAt(i));
                    }
                    
                    labelValues[i] = ((Integer)preferLabelValues.elementAt(i)).intValue();
                    if(labelValues[i] == preference.getIntValue())
                    {
                        if(preference.getId() == Preference.ID_PREFERENCE_LANGUAGE)
                        {
                            preference.setStrValue(getLocaleOrRegionDisplayString(labelNames[i]));
                        }
                        else
                        {
                            preference.setStrValue(labelNames[i]);
                        }
                    }
                }
                preference.setOptionNames(labelNames);
                preference.setOptionValues(labelValues);
            }
            for(int i = 0; i < prefGroups.length; i++)
            {
                if(prefGroups[i].getId() == firstValue)
                {
                    prefGroups[i].addPreferenceId(preference.getId());
                }
            }
        }
    }
    
    private String getLocaleOrRegionDisplayString(String fullname)
    {
        String shortName = fullname;
        if(fullname != null && fullname.length() > 0)
        {
            int index = fullname.indexOf('|');
            if (index != -1)
            {
                shortName = fullname.substring(0, index);
            }
        }
        return shortName;
    }
    
    private String parseGuideToneName(String labelName)
    {
        if(labelName != null && labelName.indexOf('|')>0)
        {
            String info = labelName;
            int index = 1;
            int indexTone = info.indexOf("|");
            if (indexTone > 0)
            {
                info = info.substring(indexTone + 1); // remove language
                indexTone = info.indexOf('|');
                if (index > 0)
                {
                    info = info.substring(indexTone + 1); // remove audio id
                    indexTone = info.indexOf("|");
                    if (index > 0)
                    {
                        info = info.substring(indexTone + 1); // remove image id
                    }
                    else
                    {
                        info = null;
                    }
                }
                else
                {
                    info = null;
                }
            }
            else
            {
                info = null;
            }

            
            String toneString = labelName.substring(0, labelName.indexOf("|"));
            if (info != null && info.length() > 0)
            {
                toneString +=" (" + info + ")";
            }
            
            return toneString;
        }
        return labelName;
    }
    
    //filter HIDE_GROUPS, don't show it when display
    private void filterPrefGroups()
    {
        int filterNum = 0;
        for (int i = 0; i < prefGroups.length; i++)
        {
            if ( prefGroups[i].getId() == FILTER_PREFERENCES_HIDE_GROUPS_INDEX )
            {
                filterNum ++;
            }
        }
        
        if( filterNum == 0 )
        {
            filterPrefGroups = prefGroups;
            return;
        }
        filterPrefGroups = new PreferenceGroup[prefGroups.length-filterNum];
        for (int i = 0, j = 0; i < prefGroups.length && j < filterPrefGroups.length; i++)
        {
            if ( prefGroups[i].getId() != FILTER_PREFERENCES_HIDE_GROUPS_INDEX )
            {
                filterPrefGroups[j] = prefGroups[i];
                j ++;
            }
        }
    }
    
    /**
     * Currently, use the easiest way to calculate checksum.
     * Can be replaced if necessary.
     */
    public static String getChecksum(byte[] data)
    {
        if(data == null)
            return null;
        
        return String.valueOf(data.length);
    }
    
    public String getCurrentLauguage()
    {
        String lauguage = "";
        Preference languagePref = getPreference(Preference.ID_PREFERENCE_LANGUAGE);
        if (languagePref != null)
        {
            int targetValue = getIntValue(Preference.ID_PREFERENCE_LANGUAGE);
            String[] optionNames = languagePref.getOptionNames();
            int[] optionValues = languagePref.getOptionValues();
            for (int optionIdx = 0; optionIdx < optionValues.length; optionIdx++)
            {
                if (optionValues[optionIdx] == targetValue)
                {
                    int pos = optionNames[optionIdx].indexOf('|');
                    if (pos != -1)
                    {
                        String locale = optionNames[optionIdx].substring(pos + 1);
                        lauguage = getLauguageCode(locale);
                    }
                }
            }
        }
        if ("".equalsIgnoreCase(lauguage))
        {
            lauguage = getLauguageCode(AppConfigHelper.defaultLocale);
        }
        return lauguage;
    }
    
    private String getLauguageCode(String locale)
    {
        int index = 0;
        String code = "";
        if (locale != null && !"".equalsIgnoreCase(locale))
        {
            index = locale.indexOf("_");
            code = locale.substring(0, index);
        }
        else
        {
            code = locale;
        }
        return code;
    }

    //This method is useless, preferenceDao should be region depentend, region should be parameter
    public void store()
    {
        // TODO Auto-generated method stub
        
    }
  
}
