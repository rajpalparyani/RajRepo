/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * PologonManager.java
 *
 */
package com.telenav.data.polygon;

import java.io.File;

import android.content.Context;

import com.telenav.app.android.AndroidPersistentContext;
import com.telenav.data.dao.misc.FileStoreManager;
import com.telenav.location.TnLocation;

/**
 * @author chrbu
 * @date 2012-3-6
 */
public class PolygonManager
{
    private static final String fileName = "D2_L11_0.mmd";

    private static PolygonManager pologanManager = new PolygonManager();

    private TTree tree;


    private PolygonManager()
    {
        super();
        init();
    }


    private void init()
    {
        try
        {
            boolean isSuccess = FileStoreManager.copyFileFromAssetToApp(fileName);
            if (isSuccess)
            {
                Context mContext = AndroidPersistentContext.getInstance().getContext();
                File targetFile = mContext.getFileStreamPath(fileName);
                MmdFileReader reader = new MmdFileReader(targetFile);
                tree = reader.getTree();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static PolygonManager getInstance()
    {
        return pologanManager;
    }

    public String getRegion(TnLocation tnlocation)
    {
        String countryCode = "";
        if (tnlocation != null && tree != null)
        {
            countryCode = retrieveRegion(tree.getRegion(
                convertMd5ToMd6(tnlocation.getLatitude()),
                convertMd5ToMd6(tnlocation.getLongitude())));
        }
        return countryCode;
    }

    private int convertMd5ToMd6(int md5)
    {
        return md5 * 10;
    }

    private String retrieveRegion(String regions)
    {
        String country = regions;
        if (!"".equalsIgnoreCase(country))
        {
            String[] countryCodes = country.split("\\|");
            if (countryCodes != null)
            {
                int number = countryCodes.length;
                if (number >1)
                    country = null;
                else
                    country = countryCodes[0];
            }
        }
        return country;

    }

}
