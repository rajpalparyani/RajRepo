/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 *
 */
package com.telenav.module.about;

import java.math.BigDecimal;
import java.util.Vector;

import com.telenav.app.CommManager;
import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.datatypes.preference.Preference;
import com.telenav.data.serverproxy.AbstractServerProxy;
import com.telenav.data.serverproxy.IServerProxyConstants;
import com.telenav.data.serverproxy.impl.ILoginProxy;
import com.telenav.data.serverproxy.impl.ServerProxyFactory;
import com.telenav.location.TnLocation;
import com.telenav.module.location.LocationListener;
import com.telenav.module.location.LocationProvider;
import com.telenav.mvc.AbstractCommonNetworkModel;
import com.telenav.res.IStringAbout;
import com.telenav.res.ResourceManager;


/**
 *@author dfqin (dfqin@telenav.cn)
 *@date 2011-1-20
 */
class AboutModel  extends AbstractCommonNetworkModel implements IAboutConstants, LocationListener
{
    private static final String PERCENT_SIGN = "%";
    private static final long MAX_GPS_EXPIRE = 10*1000; //After 10 seconds, we thought the gps fix is expried
    private static final int GPS_DECREASE_PERCENT = 20; 
	private int[] diagnosticItemIds = new int[] 
    {
			DiagnosticInfo.ID_LOGIN_ACCOUNT,
			DiagnosticInfo.ID_SERVICE_PLAN,
			DiagnosticInfo.ID_GPS_STATUS,
            DiagnosticInfo.ID_NETWORK_STATUS,
            DiagnosticInfo.ID_LOCATION_PERMISSION,
            DiagnosticInfo.ID_DATA_ROAMING_STATUS,
            DiagnosticInfo.ID_AIRPLANE_MODE,
            DiagnosticInfo.ID_DATA_SERVICE,
            DiagnosticInfo.ID_BATTERY_LEVEL
    };
	AboutModel()
	{
		
	}
	
	protected void doActionDelegate(int actionId)
	{
		switch(actionId)
		{
			case ACTION_ABOUT_INIT:
				break;
			case ACTION_GETINFO:
			{
				LocationProvider.getInstance().getCurrentLocation(LocationProvider.GPS_VALID_TIME,
                        LocationProvider.NETWORK_LOCATION_VALID_TIME, (long)4000, this,
                        LocationProvider.TYPE_GPS, 1, true); 
				break;
			}
			case ACTION_FORGET_PIN:
            {
                doForgotPin();
                break;
            }
			case ACTION_REFRESH:
			{
			    doRefresh();
			    break;
			}
		}
	}
	
	
	public void requestCompleted(TnLocation[] locations, int statusCode)
    {
		Vector items = getDiagnosticItemList();
		TnLocation location = null;
        if (statusCode == LocationProvider.STATUS_SUCCESS)
        {
            location = locations[0];
           
        }
        else
        {
        	location = null;//fail
        }
     
        updateGpsItem(location, items);
        this.put(KEY_V_DIAGNOSTIC_VECTOR, items);
        postModelEvent(EVENT_MODEL_GOTO_TOOLS);    
    }
	
	private Vector getDiagnosticItemList()
	{
        Vector list = new Vector();

        for (int i = 0; i < diagnosticItemIds.length; i++)
        {
            DiagnosticInfo.getInstance().setProvider(new DiagnosticInfoProvider());
            DiagnosticItem item = DiagnosticInfo.getInstance().getDiagnosticItem(
                diagnosticItemIds[i]);
            list.addElement(item);
        }

        return list;
	}
	
	private void updateGpsItem(TnLocation data,Vector item)
	{
		DiagnosticItem gpsItem = this.findDiagnosticItem(DiagnosticInfo.ID_GPS_STATUS, item);
        if (gpsItem != null)
        {
        	gpsItem.updateValue(getGpsStatusDisplay(data));
        	gpsItem.setDetailLines(getGpsDetailLines(data));
        }
	}
	
	private DiagnosticItem findDiagnosticItem(int id, Vector items)
    {
        DiagnosticItem item = null;
	        
        for (int i=0; i<items.size(); i++)
        {
            DiagnosticItem tmp = (DiagnosticItem) items.elementAt(i);
            if (tmp.getId() == id)
            {
                return tmp;
            }
        }
	        
        return item;
    }
	
	private String getGpsStatusDisplay(TnLocation data)
    {
        if (data == null)
        {            
            
            String na = ResourceManager.getInstance().getCurrentBundle()
            	.getString(IStringAbout.RES_UNRECOGNIZE, IStringAbout.FAMILY_ABOUT);
            return na;
        }
        else
        {
            int percent = 0;
            int numOfSat = data.getSatellites();
            if (numOfSat <= 0)
            {
                percent = 0;
            }
            else if (numOfSat <= 1)
            {
                percent = 10;
            }
            else if (numOfSat <= 2)
            {
                percent = 25;
            }
            else if (numOfSat <= 3)
            {
                percent = 50;
            }
            else if (numOfSat <= 4)
            {
                percent = 80;
            }
            else
            {
                percent = 100;
            }
            // If this gps fix data is expire over 10 seconds, gps singal will decrease 20%
            // Every 10 seconds decrease 20%
            long intervalTime = System.currentTimeMillis() - data.getLocalTimeStamp();
            if(intervalTime > MAX_GPS_EXPIRE * (100 / GPS_DECREASE_PERCENT))
            {
                percent = 0;
            }
            else
            {
                percent = percent - GPS_DECREASE_PERCENT *(int)(intervalTime / MAX_GPS_EXPIRE);
                if(percent < 0)
                {
                    percent = 0;
                }
            }
            
            return percent + PERCENT_SIGN;
        }
    }
	
	
	private String getGpsDetailLines(TnLocation data)
    {
        
        StringBuffer buf = new StringBuffer();
        String latitude = ResourceManager.getInstance().getCurrentBundle()
        	.getString(IStringAbout.RES_DIAG_GPS_LAT, IStringAbout.FAMILY_ABOUT);
        String longtitude = ResourceManager.getInstance().getCurrentBundle()
        	.getString(IStringAbout.RES_DIAG_GPS_LON, IStringAbout.FAMILY_ABOUT);
        String fixTime = ResourceManager.getInstance().getCurrentBundle()
        	.getString(IStringAbout.RES_DIAG_GPS_FIXTIME, IStringAbout.FAMILY_ABOUT);
        String numOfSatellites = ResourceManager.getInstance().getCurrentBundle()
        	.getString(IStringAbout.RES_DIAG_GPS_SATELLITENUM, IStringAbout.FAMILY_ABOUT);
        String accuracy = ResourceManager.getInstance().getCurrentBundle()
        	.getString(IStringAbout.RES_DIAG_GPS_ACCURACY, IStringAbout.FAMILY_ABOUT);
        String na = ResourceManager.getInstance().getCurrentBundle()
        	.getString(IStringAbout.RES_UNRECOGNIZE, IStringAbout.FAMILY_ABOUT);
        
        if (data == null)
        {
            buf.append(latitude + ": " + na + " \n ")
                .append(longtitude + ": " + na + " \n ")
                .append(fixTime + ": " + na + " \n")
                .append(numOfSatellites + ": " + na + " \n ")
                .append(accuracy + ": " + na);
        }
        else
        {  
            String accuracyValue = "";
            Preference distanceUnitPreference = DaoManager.getInstance().getPreferenceDao().getPreference(Preference.ID_PREFERENCE_DISTANCEUNIT);
            int distanceUnitValue = Preference.UNIT_METRIC;
            if (distanceUnitPreference != null)
            {
                distanceUnitValue = distanceUnitPreference.getIntValue();
            }
            accuracyValue = ResourceManager.getInstance().getStringConverter().convertDistanceMeterToMile(data.getAccuracy(), distanceUnitValue);
            buf.append("  " + latitude + ": ").append(new BigDecimal(data.getLatitude()).divide(new BigDecimal(100000)).setScale(5)).append(" \n ")
                .append("  " + longtitude + ": ").append(new BigDecimal(data.getLongitude()).divide(new BigDecimal(100000)).setScale(5)).append(" \n ")
                .append("  " + fixTime + ": ").append(data.getLocalTimeStamp()).append(" \n ")
                .append("  " + numOfSatellites + ": ").append(data.getSatellites()).append(" \n ")
                .append("  " + accuracy + ": ").append(accuracyValue);
        }
        return buf.toString();
    }
	
    protected void doForgotPin()
    {
        ILoginProxy loginProxy = ServerProxyFactory.getInstance().createLoginProxy(null,
            CommManager.getInstance().getComm(), this, null);
        loginProxy.sendForgetPin();
    }

    protected void doRefresh()
    {
        TnLocation currentLocation = LocationProvider.getInstance().getCurrentLocation(
            LocationProvider.TYPE_GPS);
        if (currentLocation == null)
        {
            LocationProvider.getInstance().getCurrentLocation(
                LocationProvider.GPS_VALID_TIME,
                LocationProvider.NETWORK_LOCATION_VALID_TIME, (long) 4000, this,
                LocationProvider.TYPE_GPS, 1);
        }
        else
        {
            Vector items = getDiagnosticItemList();
            updateGpsItem(currentLocation, items);
            this.put(KEY_V_DIAGNOSTIC_VECTOR, items);
            postModelEvent(EVENT_MODEL_GOTO_TOOLS);
        }

    }

    protected void transactionFinishedDelegate(AbstractServerProxy proxy, String jobId)
    {
        if (proxy instanceof ILoginProxy)
        {
            if (proxy.getRequestAction().equals(IServerProxyConstants.ACT_FORGET_PIN))
            {
                if (proxy.getStatus() == IServerProxyConstants.SUCCESS) 
                {
                    this.postModelEvent(EVENT_MODEL_FORGET_PIN);
                }
                else
                {
                    String ptnNull = ResourceManager.getInstance().getCurrentBundle()
                    .getString(IStringAbout.RES_PIN_ERROR, IStringAbout.FAMILY_ABOUT);
                    this.postErrorMessage(ptnNull);
                }
            }
        }
    }

}
