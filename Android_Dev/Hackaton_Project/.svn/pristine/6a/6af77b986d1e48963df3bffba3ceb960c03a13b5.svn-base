/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * CarModelManager.java
 *
 */
package com.telenav.module.preference.carmodel;


import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.dao.misc.PreferenceDao;
import com.telenav.data.datatypes.preference.Preference;
import com.telenav.logger.Logger;
import com.telenav.persistent.TnPersistentManager;
import com.telenav.res.IBinaryRes;
import com.telenav.ui.citizen.map.GlResManager;
import com.telenav.ui.citizen.map.MapContainer;
/**
 *@author qli
 *@date 2011-2-14
 */
public class CarModelManager
{
    protected static final String CARMODEL_VERSION     = "_version";
    protected static final String CARMODEL_FILE        = "_file";
    protected static final String CARMODEL_GREY    = "_grey";
    protected static final String CARMODEL_MOD         = ".mod";
    protected static final String DEFAULT_MOD_NAME         = "arrow";
    
    protected String currentCarModelName    = null;
    protected boolean isGrey = false;
    
    private static class InnerCarModelManager
    {
        private static CarModelManager instance = new CarModelManager();
    }
    
    private CarModelManager()
    {
    }
    
    public static CarModelManager getInstance()
    {
        return InnerCarModelManager.instance;
    }
    
    /**
     * Indicate that whether need to set to default carModel.
     * 
     * @return
     */
    public boolean needDefaultCarModel()
    {
        String carName = "";
        try
        {
            carName = ((DaoManager) DaoManager.getInstance()).getPreferenceDao()
                    .getStrValue(Preference.ID_PREFERENCE_CAR_MODEL);
        }
        catch (Exception e)
        {
            Logger.log(this.getClass().getName(), e);
        }

        if (carName.trim().length() > 0)
        {
            isGrey = false;
            MapContainer.getInstance().addCarModel(null, carName);
            return false;
        }
        return true;
    }
    
    public boolean needDefaultGreyCarModel()
    {
        String carName = "";
        try
        {
            carName = ((DaoManager) DaoManager.getInstance()).getPreferenceDao()
                    .getStrValue(Preference.ID_PREFERENCE_CAR_MODEL);
        }
        catch(Exception e)
        {
            Logger.log(this.getClass().getName(), e);
        }
        
        if (carName != null && carName.trim().length() > 0)
        {
            isGrey = true;
            MapContainer.getInstance().addCarModel(null, carName);
            return false;
        }
        
        return true;
    }
    
    public void loadCarModel()
    {
        PreferenceDao preferenceDao = ((DaoManager) DaoManager.getInstance()).getPreferenceDao();
        Preference carModelPreference = preferenceDao.getPreference(Preference.ID_PREFERENCE_CAR_MODEL);
        String modelName = "";
        if (carModelPreference == null)
        {
            modelName = DEFAULT_MOD_NAME;
        }
        else
        {
            modelName = carModelPreference.getStrValue();
            if (!isModelFileExisted(modelName))
            {
                modelName = DEFAULT_MOD_NAME;
            }
        }
        MapContainer.getInstance().addCarModel(null, modelName + CARMODEL_MOD);
    }

    public void loadGreyCarModel()
    {
        PreferenceDao preferenceDao = ((DaoManager) DaoManager.getInstance()).getPreferenceDao();
        Preference carModelPreference = preferenceDao.getPreference(Preference.ID_PREFERENCE_CAR_MODEL);
        String modelName = "";
        if (carModelPreference == null)
        {
            modelName = DEFAULT_MOD_NAME;
        }
        else
        {
            modelName = carModelPreference.getStrValue();
            if (!isModelFileExisted(modelName))
            {
                modelName = DEFAULT_MOD_NAME;
            }
        }
        MapContainer.getInstance().addCarModel(null, modelName + CARMODEL_GREY + CARMODEL_MOD);
    }
    
    public void loadCarModel(String carModelName)
    {
        if (!isModelFileExisted(carModelName))
        {
            carModelName = DEFAULT_MOD_NAME;
        }
        MapContainer.getInstance().addCarModel(null, carModelName + CARMODEL_MOD);
    }

    public void loadGreyCarModel(String carModelName)
    {
        if (!isModelFileExisted(carModelName))
        {
            carModelName = DEFAULT_MOD_NAME;
        }
        MapContainer.getInstance().addCarModel(null, carModelName + CARMODEL_GREY + CARMODEL_MOD);
    }

    protected void storeCar(String modelName)
    {
        String region = DaoManager.getInstance().getMandatoryNodeDao().getMandatoryNode().getUserInfo().region;
        PreferenceDao preferenceDao = ((DaoManager) DaoManager.getInstance()).getPreferenceDao();
        preferenceDao.setStrValue(Preference.ID_PREFERENCE_CAR_MODEL, modelName);
        preferenceDao.store(region);
        loadCarModel();
    }
    
    protected void storeGreyCar(String modelName)
    {
        String region = DaoManager.getInstance().getMandatoryNodeDao().getMandatoryNode().getUserInfo().region;
        PreferenceDao preferenceDao = ((DaoManager) DaoManager.getInstance()).getPreferenceDao();
        preferenceDao.setStrValue(Preference.ID_PREFERENCE_CAR_MODEL, modelName);
        preferenceDao.store(region);
        loadGreyCarModel();
    }
    
    public String getCarModelModName()
    {
        return "";
    }
    
    public String getGreyCarModName()
    {
        return "";
    }
    
    public String getCarModelVersion(String name)
    {
        String carModelVersion = "";
        return carModelVersion;
    }
    
    private boolean isModelFileExisted(String fileName)
    {
        return GlResManager.getInstance().isOpenGlResourceExist("", fileName + CARMODEL_MOD, TnPersistentManager.FILE_SYSTEM_INTERNAL, IBinaryRes.FAMILY_BINARY_MODELS);
    }
}
