/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * MapDataUpgradeInfo.java
 *
 */
package com.telenav.data.datatypes.map;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2011-2-23
 */
public class MapDataUpgradeInfo
{
    private int upgradeMode;

    private String version;

    private String name;

    private String buildNumber;

    private String region;

    private String state;

    private String url;

    private String summary;

    private String mapDataSize;

    public int getUpgradeMode()
    {
        return upgradeMode;
    }

    public void setUpgradeMode(int upgradeMode)
    {
        this.upgradeMode = upgradeMode;
    }

    public void setVersion(String version)
    {
        this.version = version;
    }

    public String getVersion()
    {
        return this.version;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getBuildNumber()
    {
        return buildNumber;
    }

    public void setBuildNumber(String buildNumber)
    {
        this.buildNumber = buildNumber;
    }

    public String getRegion()
    {
        return region;
    }

    public void setRegion(String region)
    {
        this.region = region;
    }

    public String getState()
    {
        return state;
    }

    public void setState(String state)
    {
        this.state = state;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public String getSummary()
    {
        return summary;
    }

    public void setSummary(String summary)
    {
        this.summary = summary;
    }

    public String getMapDataSize()
    {
        return mapDataSize;
    }

    public void setMapDataSize(String mapDataSize)
    {
        this.mapDataSize = mapDataSize;
    }
}
