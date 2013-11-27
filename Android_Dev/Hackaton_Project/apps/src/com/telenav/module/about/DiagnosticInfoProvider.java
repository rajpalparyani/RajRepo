/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 *
 */
package com.telenav.module.about;

import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.serverproxy.NetworkStatusManager;
import com.telenav.location.TnLocationManager;
import com.telenav.log.mis.RadioStatusLogger;
import com.telenav.radio.TnRadioManager;
import com.telenav.res.IStringAbout;
import com.telenav.res.IStringCommon;
import com.telenav.res.ResourceManager;
import com.telenav.telephony.TnBatteryManager;
import com.telenav.telephony.TnSimCardInfo;
import com.telenav.telephony.TnTelephonyManager;

/**
 *@author dfqin (dfqin@telenav.cn)
 *@date 2011-1-20
 */

public class DiagnosticInfoProvider implements IDiagnosticInfoProvider 
{
	private static final String PERCENT_SIGN = "%";
	
	private static final String VALUE_UNRECOGNIZE = "CANNOT_RECOGNIZE_IT";
	
	public DiagnosticItem getDiagnosticItem(int id) 
	{
		return this.createItem(id);
	}
	
	protected String getItemName(int id)
	{
		String name = "";
		switch(id)
		{
			case DiagnosticInfo.ID_LOGIN_ACCOUNT:
			{
				name = ResourceManager.getInstance().getCurrentBundle()
					.getString(IStringAbout.RES_DIAG_PHONE_NUM, IStringAbout.FAMILY_ABOUT);
				break;
			}
			case DiagnosticInfo.ID_SERVICE_PLAN:
			{
				name = ResourceManager.getInstance().getCurrentBundle()
					.getString(IStringAbout.RES_DIAG_SERVICE_CODE, IStringAbout.FAMILY_ABOUT);
				break;
			}
			case DiagnosticInfo.ID_GPS_STATUS:
			{
				name = ResourceManager.getInstance().getCurrentBundle()
					.getString(IStringAbout.RES_DIAG_GPS_STATUS, IStringAbout.FAMILY_ABOUT);
				break;
			}
			case DiagnosticInfo.ID_NETWORK_STATUS:
			{
					name = ResourceManager.getInstance().getCurrentBundle()
					  .getString(IStringAbout.RES_DIAG_NET_STATUS, IStringAbout.FAMILY_ABOUT);
					break;
			}
			case DiagnosticInfo.ID_LOCATION_PERMISSION:
			{
				name = ResourceManager.getInstance().getCurrentBundle()
				  .getString(IStringAbout.RES_DIAG_LOCATION_PERMIT, IStringAbout.FAMILY_ABOUT);
				break;
			}
			case DiagnosticInfo.ID_NETGUARD_SETTINGS:
			{
			    name = ResourceManager.getInstance().getCurrentBundle()
                  .getString(IStringAbout.RES_DIAG_NETGUARD_SET, IStringAbout.FAMILY_ABOUT);
			    break;
			}
			case DiagnosticInfo.ID_DATA_ROAMING_STATUS:
			{
				name = ResourceManager.getInstance().getCurrentBundle()
				  .getString(IStringAbout.RES_DIAG_DATA_ROAM_STATUS, IStringAbout.FAMILY_ABOUT);
				break;
			}
			case DiagnosticInfo.ID_AIRPLANE_MODE:
			{
				name = ResourceManager.getInstance().getCurrentBundle()
				  .getString(IStringAbout.RES_DIAG_AIRPLANE_MODE, IStringAbout.FAMILY_ABOUT);
				break;
			}
			case DiagnosticInfo.ID_DATA_SERVICE:
			{
				name =  ResourceManager.getInstance().getCurrentBundle()
                  .getString(IStringAbout.RES_DIAG_DATA_SERVICE, IStringAbout.FAMILY_ABOUT);
				break;
			}
			case DiagnosticInfo.ID_BATTERY_LEVEL:
			{
				name =  ResourceManager.getInstance().getCurrentBundle()
                  .getString(IStringAbout.RES_DIAG_BATTERY_LEVEL, IStringAbout.FAMILY_ABOUT);
				break;
			}
		}
		
		return name;
	}
	
	protected String getItemValue(int id)
	{
	
		String strValue = ResourceManager.getInstance().getCurrentBundle()
			.getString(IStringAbout.RES_UNRECOGNIZE, IStringAbout.FAMILY_ABOUT);
		switch(id)
		{
			case DiagnosticInfo.ID_LOGIN_ACCOUNT:
			{
				strValue = TnTelephonyManager.getInstance().getPhoneNumber();
				if(strValue == null)
				{
				    strValue  =  ResourceManager.getInstance().getCurrentBundle()
				      .getString(IStringAbout.RES_UNRECOGNIZE, IStringAbout.FAMILY_ABOUT);
				}
				break;
			}
			case DiagnosticInfo.ID_SERVICE_PLAN:
			{
				
				strValue =  DaoManager.getInstance().getBillingAccountDao().getSocType();
				if(strValue == null)
				{
				    strValue =  ResourceManager.getInstance().getCurrentBundle()
                    .getString(IStringAbout.RES_UNRECOGNIZE, IStringAbout.FAMILY_ABOUT);
				}
				break;
			}
			case DiagnosticInfo.ID_NETWORK_STATUS:
			{
			    TnSimCardInfo simCardInfo = TnTelephonyManager.getInstance().getSimCardInfo();
                if(simCardInfo.simCardId == null || simCardInfo.simCardId.length() == 0)
                {
                    strValue = ResourceManager.getInstance().getCurrentBundle()
                    .getString(IStringAbout.RES_UNABLE, IStringAbout.FAMILY_ABOUT);
                }
                else
                {
                    String netType = "-1";
                    if (TnRadioManager.getInstance().getCellInfo() != null)
                    {
                        netType = TnRadioManager.getInstance().getCellInfo().networkType;
                    }
                    String strNetType = convertNetType(Integer.parseInt(netType));

                    boolean isConnected = NetworkStatusManager.getInstance().isConnected();

                    int signalStrengh = RadioStatusLogger.getInstance().getSignalStrengh();
                    String strSignal = null;
                    if (isConnected)
                    {
                        strSignal = convertSignalLevel(signalStrengh);
                        strValue = strSignal + " " + strNetType;
                    }
                    else
                    {
                        strValue = ResourceManager.getInstance().getCurrentBundle()
                                .getString(IStringAbout.RES_UNABLE, IStringAbout.FAMILY_ABOUT);
                    }
                }
				break;
			}
			case DiagnosticInfo.ID_LOCATION_PERMISSION:
			{
			    boolean isGpsProviderAvialable = TnLocationManager.getInstance().isGpsProviderAvailable(TnLocationManager.GPS_179_PROVIDER);
				if(isGpsProviderAvialable)
				{
				    strValue = ResourceManager.getInstance().getCurrentBundle()
		            .getString(IStringAbout.RES_ENABLE, IStringAbout.FAMILY_ABOUT);
				}
				else
				{
				    strValue = ResourceManager.getInstance().getCurrentBundle()
		            .getString(IStringAbout.RES_UNABLE, IStringAbout.FAMILY_ABOUT);
				}
				break;
			}
			case DiagnosticInfo.ID_NETGUARD_SETTINGS:
            {
                strValue = ResourceManager.getInstance().getCurrentBundle()
                .getString(IStringCommon.RES_BTTN_NO, IStringCommon.FAMILY_COMMON);
                break;
            }
			case DiagnosticInfo.ID_DATA_ROAMING_STATUS:
			{
				boolean isRoam = TnRadioManager.getInstance().isRoaming() ;
				if(isRoam)
				{
				    strValue = ResourceManager.getInstance().getCurrentBundle()
                    .getString(IStringCommon.RES_BTTN_YES, IStringCommon.FAMILY_COMMON);
				}
				else
				{
				    strValue = ResourceManager.getInstance().getCurrentBundle()
                    .getString(IStringCommon.RES_BTTN_NO, IStringCommon.FAMILY_COMMON);
				}
				break;
			}
			case DiagnosticInfo.ID_AIRPLANE_MODE:
			{
				boolean isAirMode = TnRadioManager.getInstance().isAirportMode();
				if(isAirMode)
				{
				    strValue = ResourceManager.getInstance().getCurrentBundle()
                    .getString(IStringCommon.RES_BTTN_YES, IStringCommon.FAMILY_COMMON);
				}
				else
				{
				    strValue = ResourceManager.getInstance().getCurrentBundle()
                    .getString(IStringCommon.RES_BTTN_NO, IStringCommon.FAMILY_COMMON);
				}
				break;
			}
			case DiagnosticInfo.ID_DATA_SERVICE:
			{
			    TnSimCardInfo simCardInfo = TnTelephonyManager.getInstance().getSimCardInfo();
			    if(simCardInfo.simCardId == null || simCardInfo.simCardId.length() == 0)
			    {
			        strValue = ResourceManager.getInstance().getCurrentBundle()
                    .getString(IStringCommon.RES_BTTN_NO, IStringCommon.FAMILY_COMMON);
			    }
			    else
                {
                    boolean isData = NetworkStatusManager.getInstance().isConnected();
                    if (isData)
                    {
                        strValue = ResourceManager.getInstance().getCurrentBundle().getString(IStringCommon.RES_BTTN_YES, IStringCommon.FAMILY_COMMON);
                    }
                    else
                    {
                        strValue = ResourceManager.getInstance().getCurrentBundle().getString(IStringCommon.RES_BTTN_NO, IStringCommon.FAMILY_COMMON);
                    }
                }
				break;
			}
			case DiagnosticInfo.ID_BATTERY_LEVEL:
			{
				int batteryLevel = TnBatteryManager.getInstance().getBatteryLevel();
				if(batteryLevel >= 0 && batteryLevel <= 100)
				{				    
				    strValue = batteryLevel + PERCENT_SIGN;
				}
				else
				{
				    strValue =  ResourceManager.getInstance().getCurrentBundle()
	                  .getString(IStringAbout.RES_UNRECOGNIZE, IStringAbout.FAMILY_ABOUT);
				}
				break;
			}
		}
	
		return strValue;
	}
	
	private DiagnosticItem createItem(int id)
	{
		DiagnosticItem item = new DiagnosticItem(getItemName(id), getItemValue(id), id);
		
		return item;
	}
	
	public void update(DiagnosticItem item)
	{
		if (item != null)
		{
			String value = this.getItemValue(item.getId());
			if (!VALUE_UNRECOGNIZE.equalsIgnoreCase(value))
			{
				item.updateValue(value);
			}
		}
	}
	
	
    private String convertSignalLevel(int level)
    {
        String levelInPercent = "0";

        if (level > -78)
        {
            levelInPercent = "100";
        }
        else if(level > -87)
        {
            levelInPercent = "80";
        }
        else if(level > -93)
        {
            levelInPercent = "60";
        }
        else if(level > -102)
        {
            levelInPercent = "40";
        }
        else if(level > -121)
        {
            levelInPercent = "20";
        }
        
        return levelInPercent + PERCENT_SIGN;
    }
    
    private String convertNetType(int netType)
    {
        switch(netType)
        {
            case 0:
            return "UNKNOWN";
            
            case 1:
            return "GPRS";
            
            case 2:
            return "EDGE";
            
            case 3:
            return "UMTS";
            
            case 4:
            return "CDMA";
            
            case 5:
            return "EVDO_0";
            
            case 6:
            return "EVDO_A";
            
            case 7:
            return "1xRTT";
            
            case 8:
            return "HSDPA";
            
            case 9:
            return "HSUPA";
            
            case 10:
            return "HSPA ";
        }
        return "NOT GET";
    }
}
