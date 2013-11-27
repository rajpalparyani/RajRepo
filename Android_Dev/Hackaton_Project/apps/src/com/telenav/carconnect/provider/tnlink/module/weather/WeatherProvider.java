/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * WeatherProvider.java
 *
 */
package com.telenav.carconnect.provider.tnlink.module.weather;

import java.util.Vector;

import com.telenav.app.CommManager;
import com.telenav.carconnect.CarConnectManager;
import com.telenav.carconnect.LogUtil;
import com.telenav.carconnect.provider.AbstractProvider;
import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.dao.misc.PreferenceDao;
import com.telenav.data.datatypes.address.Stop;
import com.telenav.data.datatypes.preference.Preference;
import com.telenav.data.datatypes.weather.Weather;
import com.telenav.data.datatypes.weather.WeatherInfo;
import com.telenav.data.serverproxy.AbstractServerProxy;
import com.telenav.data.serverproxy.IServerProxyListener;
import com.telenav.data.serverproxy.IUserProfileProvider;
import com.telenav.data.serverproxy.impl.IWeatherProxy;
import com.telenav.data.serverproxy.impl.ServerProxyFactory;
import com.telenav.module.AppConfigHelper;
import com.telenav.module.UserProfileProvider;
import com.telenav.mvc.AbstractController;
import com.telenav.mvc.AbstractModel;
import com.telenav.mvc.ICommonConstants;
import com.telenav.navsdk.events.PointOfInterestData;
import com.telenav.navsdk.events.PointOfInterestData.PointOfInterest;
import com.telenav.navsdk.events.WeatherData.DailyWeatherInfo;
import com.telenav.navsdk.events.WeatherEvents.WeatherNearPOIError;
import com.telenav.navsdk.events.WeatherEvents.WeatherNearPOIRequest;
import com.telenav.navsdk.events.WeatherEvents.WeatherNearPOIResponse;
import com.telenav.res.IStringDashboard;
import com.telenav.res.ResourceManager;
import com.telenav.navsdk.events.WeatherData.WeatherCondition;

/**
 *@author tpeng
 *@date 2012-5-29
 */
public class WeatherProvider extends AbstractProvider implements IServerProxyListener
{
    protected static final String[] canadianCarrier = {"Rogers","Fido","BellMob","VMC"};

    @Override
    public void handle(String eventType, Object eventData)
    {
        // TODO Auto-generated method stub
        if("WeatherNearPOIRequest".equals(eventType))
        {
            LogUtil.logInfo("Weather","Handle weather request!");
            WeatherNearPOIRequest request=(WeatherNearPOIRequest)eventData;
            if(request!=null)
            {
                PointOfInterest poi=request.getNearPOI();
                if(poi!=null)
                {
                    Stop stop = new Stop();
                    if (poi.hasAddress())
                    {
                        PointOfInterestData.Address poiAddr = poi.getAddress();
                        stop.setCity(poiAddr.getCity());
                        stop.setCountry(poiAddr.getCountry());
                        stop.setCounty(poiAddr.getCounty());
                        stop.setCrossStreetName(poiAddr.getCrossStreetName());
                        stop.setPostalCode(poiAddr.getPostalCode());
                        stop.setProvince(poiAddr.getProvince());
                        stop.setStreetName(poiAddr.getStreetName());
                        stop.setHouseNumber(poiAddr.getDoorNumber());
                    }
                    if (poi.hasLocation())
                    {
                        PointOfInterestData.Location location = poi.getLocation();
                        stop.setLat((int)(location.getLatitude()*100000));
                        stop.setLon((int)(location.getLongitude()*100000));
                    }
                    
                    boolean isCanadianCarrier = false;
                    boolean useOriginalPicCode = true;
                    for (int i = 0; i < canadianCarrier.length; i++ )
                    {
                        if (canadianCarrier[i].equalsIgnoreCase(AppConfigHelper.brandName))
                        {
                            isCanadianCarrier = true;
                        }
                    }
                    
                    IWeatherProxy proxy = ServerProxyFactory.getInstance().createWeatherProxy(null, CommManager.getInstance().getComm(), this, new UserProfileProvider());
                    
                    proxy.requestWeather(stop, isCanadianCarrier, useOriginalPicCode);
                    
                    //IWeatherProxy proxy = new ProtoBufWeatherProxy(null, CommManager.getInstance().getComm(), this, null);
                    //proxy.requestWeather(stop, false,true);
                }
             }
         }
    }

    @Override
    public void register()
    {
        // TODO Auto-generated method stub
        getEventBus().subscribe("WeatherNearPOIRequest", this);
        
    }

    @Override
    public void unregister()
    {
        // TODO Auto-generated method stub
        getEventBus().unsubscribe("WeatherNearPOIRequest", this);
    }

    @Override
    public void transactionFinished(AbstractServerProxy proxy, String jobId)
    {
        IWeatherProxy weatherProxy = (IWeatherProxy)proxy;
        Weather weather = weatherProxy.getWeather();
        Vector v = weather.getWeekWeatherInfos();
        if (v == null)
        {
            onError();
            return;
        }
        final int size = v.size();
        WeatherNearPOIResponse.Builder reponseBuilder=WeatherNearPOIResponse.newBuilder();
        for (int index = 0; index < size; index++)
        {
            WeatherInfo info = (WeatherInfo) v.elementAt(index);
            int temp = 0;
            int weatherCode = info.getWeatherCode();
            try
            {
                if(index==0)
                {
                    String sTemp=weather.getCurrentWeatherInfo().getTemp();
                    temp = parseInt(sTemp);
                    temp = convertTemp(temp);
                    weatherCode = weather.getCurrentWeatherInfo().getWeatherCode();
                }
                
                String sLow = info.getLow();
                String sHigh = info.getHigh();
                weatherCode = convertWeatherCode(weatherCode);
                int low = parseInt(sLow);
                int high = parseInt(sHigh);
                low = convertTemp(low);
                high = convertTemp(high);
                LogUtil.logInfo("Weather", "weather code:" + weatherCode);
                LogUtil.logInfo("Weather", "low:" + low);
                LogUtil.logInfo("Weather", "high:" + high);
                LogUtil.logInfo("Weather", "temp:" + temp);
                DailyWeatherInfo.Builder dailyBuilder = DailyWeatherInfo.newBuilder();
                dailyBuilder.setMaxTempInDegreesCelsius(high);
                dailyBuilder.setMinTempInDegreesCelsius(low);
                com.telenav.navsdk.events.WeatherData.WeatherInfo.Builder weatherBuilder = com.telenav.navsdk.events.WeatherData.WeatherInfo
                        .newBuilder();
                weatherBuilder.setTemperatureInDegreesCelsius(temp);
                weatherBuilder.addConditions(WeatherCondition.valueOf(weatherCode));
                reponseBuilder.addDaily(dailyBuilder);
                reponseBuilder.addWeather(weatherBuilder);
            }
            catch (Exception e)
            {
                LogUtil.logInfo("Weather", "Exception to get weather info:" + e);
                onError();
            }
            CarConnectManager.getEventBus().broadcast("WeatherNearPOIResponse", reponseBuilder.build());
        }
    }
    
    private int convertWeatherCode(int weatherCode)
    {
        return weatherCode;
    }

    private int parseInt(String data)
    {
        int ret=0;
        if(data!=null&&data.trim().length()>0)
        {
            try{
            ret=Integer.parseInt(data);
            }catch(Exception e)
            {}
        }
        return ret;
    }
    
    private int convertTemp(int temp)
    {
        PreferenceDao prefDao = ((DaoManager) DaoManager.getInstance()).getPreferenceDao();
        int systemUnits = prefDao.getIntValue(Preference.ID_PREFERENCE_DISTANCEUNIT);
        if (systemUnits == Preference.UNIT_METRIC)
        {
            return  temp;
        }
        else
        {
            return (temp - 32) * 5 / 9;
        }
    }

    @Override
    public void updateTransactionStatus(AbstractServerProxy proxy, byte progress)
    {
        // TODO Auto-generated method stub
        LogUtil.logInfo("Weather","updateTransactionStatus");
    }

    @Override
    public void networkError(AbstractServerProxy proxy, byte statusCode, String jobId)
    {
        // TODO Auto-generated method stub
        LogUtil.logInfo("Weather","networkError");
        onError();
    }

    @Override
    public void transactionError(AbstractServerProxy proxy)
    {
        // TODO Auto-generated method stub
        LogUtil.logInfo("Weather","transactionError");
        onError();
        
    }

    @Override
    public boolean isAllowNetworkRequest(AbstractServerProxy proxy)
    {
        // TODO Auto-generated method stub
        return true;
    }
    
    private void onError()
    {
        WeatherNearPOIError.Builder builder=WeatherNearPOIError.newBuilder();
        CarConnectManager.getEventBus().broadcast("WeatherNearPOIError", builder.build());
    }

}
