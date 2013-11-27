/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * ProtoBufWeatherProxy.java
 *
 */
package com.telenav.data.serverproxy.impl.protobuf;

import java.io.IOException;
import java.util.Vector;

import com.telenav.comm.Comm;
import com.telenav.comm.Host;
import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.dao.misc.PreferenceDao;
import com.telenav.data.datatypes.address.Stop;
import com.telenav.data.datatypes.weather.Weather;
import com.telenav.data.datatypes.weather.WeatherInfo;
import com.telenav.data.serverproxy.IServerProxyConstants;
import com.telenav.data.serverproxy.IServerProxyListener;
import com.telenav.data.serverproxy.IUserProfileProvider;
import com.telenav.data.serverproxy.RequestItem;
import com.telenav.data.serverproxy.impl.IWeatherProxy;
import com.telenav.j2me.datatypes.ProtocolBuffer;
import com.telenav.j2me.framework.protocol.ProtoWeatherInfo;
import com.telenav.j2me.framework.protocol.ProtoWeatherReq;
import com.telenav.j2me.framework.protocol.ProtoWeatherResp;
import com.telenav.j2me.framework.protocol.ProtoWeatherView;
import com.telenav.logger.Logger;
import com.telenav.res.ResourceManager;

/**
 * @author chrbu (chrbu@telenav.cn)
 * @date Dec 30, 2011
 */
public class ProtoBufWeatherProxy extends AbstractProtobufServerProxy implements
        IWeatherProxy
{
    private static final int USE_ORIGINAL_PIC_CODE_INDEX = 3;
    
    private static final int LOCALE_INDEX = 2;

    private static final int ISCANADIANCARRIER_INDEX = 1;

    private static final int STOP_INDEX = 0;

    private Weather mWeather;

    public ProtoBufWeatherProxy(Host host, Comm comm,
            IServerProxyListener serverProxyListener, IUserProfileProvider userProfileProvider)
    {
        super(host, comm, serverProxyListener, userProfileProvider);
    }

    protected String requestWeather(Stop stop, boolean isCanadianCarrier, String locale, boolean useOriginalPicCode, int retryTimes, int timeout)
    {
        try
        {
            RequestItem reqItem = new RequestItem(IServerProxyConstants.ACT_GET_WEATHER,
                    this);
            reqItem.params = new Vector();
            reqItem.params.addElement(stop);
            reqItem.params.addElement(isCanadianCarrier);
            reqItem.params.addElement(locale);
            reqItem.params.addElement(useOriginalPicCode);
            if (Logger.DEBUG)
            {
                Logger.log(Logger.INFO, this.getClass().getName(), " CR send weather!!!");
            }
            return sendRequest(createProtoBufReq(reqItem), IServerProxyConstants.ACT_GET_WEATHER, retryTimes, timeout);
        }
        catch (Exception e)
        {
            Logger.log(this.getClass().getName(), e);
            if (listener != null)
                listener.networkError(this, IServerProxyListener.EXCEPTION_SEND, null);
            return "";
        }
    }

    public String requestWeather(Stop stop, boolean isCanadianCarrier, boolean useOriginalPicCode, int retryTimes, int timeout)
    {
        try
        {
           return  requestWeather(stop, isCanadianCarrier, ResourceManager.getInstance()
                    .getCurrentLocale(), useOriginalPicCode, retryTimes, timeout);
        }
        catch (Exception e)
        {
            Logger.log(this.getClass().getName(), e);
            if (listener != null)
                listener.networkError(this, IServerProxyListener.EXCEPTION_SEND, null);
            return "";
        }
    }

    protected void addRequestArgs(Vector requestVector, RequestItem requestItem)
            throws Exception
    {
        if (IServerProxyConstants.ACT_GET_WEATHER.equals(requestItem.action))
        {
            ProtoWeatherReq.Builder builder = ProtoWeatherReq.newBuilder();
            if (requestItem.params.size() >= 4)
            {
                builder.setLocation(ProtoBufServerProxyUtil
                        .convertStop((Stop) requestItem.params.elementAt(STOP_INDEX)));
                builder.setIsCanadianCarrier((Boolean) requestItem.params
                        .elementAt(ISCANADIANCARRIER_INDEX));
                builder.setLocale((String) requestItem.params.elementAt(LOCALE_INDEX));
                builder.setUseOriginalPicCode ((Boolean) requestItem.params
                    .elementAt(USE_ORIGINAL_PIC_CODE_INDEX));
                ProtocolBuffer pb = new ProtocolBuffer();
                pb.setBufferData(convertToByteArray(builder.build()));
                pb.setObjType(requestItem.action);
                requestVector.add(pb);
            }
        }
    }

    protected int parseRequestSpecificData(ProtocolBuffer protoBuf) throws IOException
    {

        if (IServerProxyConstants.ACT_GET_WEATHER.equals(protoBuf.getObjType()))
        {
            ProtoWeatherResp resp = ProtoWeatherResp.parseFrom(protoBuf.getBufferData());
            this.errorMessage = resp.getErrorMessage();
            this.status = resp.getStatus();
            mWeather = new Weather();
            if (status == IServerProxyConstants.SUCCESS)
            {
                mWeather.setCanadianCarrier(resp.getIsCanadianCarrier());
                mWeather.setLocale(resp.getLocale());
                mWeather.setStop(ProtoBufServerProxyUtil.convertStop(resp.getLocation()));
                ProtoWeatherView weatherView = resp.getView();
                if (weatherView != null)
                {
                    mWeather.setCurrentWeatherInfo(getWeatherInfo(weatherView
                            .getCurrentWeatherInfo()));
                    int size = weatherView.getWeekWeatherInfos().size();
                    Vector weatherVector = new Vector(size);
                    for (int index = 0; index < size; index++)
                    {
                        ProtoWeatherInfo protoWeatherInfo = (ProtoWeatherInfo) weatherView
                                .getWeekWeatherInfos().get(index);
                        weatherVector.add(getWeatherInfo(protoWeatherInfo));
                    }
                    mWeather.setWeekWeatherInfos(weatherVector);
                }
            }
        }

        return status;
    }

    private WeatherInfo getWeatherInfo(ProtoWeatherInfo protoWeatherInfo)
    {
        WeatherInfo weatherInfo = new WeatherInfo();
        weatherInfo.setDate(protoWeatherInfo.getDate());
        weatherInfo.setDayOfWeek(protoWeatherInfo.getDayOfWeek());
        weatherInfo.setFeel(protoWeatherInfo.getFeel());
        weatherInfo.setHigh(protoWeatherInfo.getHigh());
        weatherInfo.setHumidity(protoWeatherInfo.getHumidity());
        weatherInfo.setLow(protoWeatherInfo.getLow());
        weatherInfo.setStatus(protoWeatherInfo.getStatus());
        String tempStr = protoWeatherInfo.getTemp();
        PreferenceDao prefDao = ((DaoManager) DaoManager.getInstance()).getPreferenceDao();
        weatherInfo.setTemp(tempStr);
        weatherInfo.setTemperatureCode(protoWeatherInfo.getTemperatureCode());
        weatherInfo.setWeatherCode(protoWeatherInfo.getWeatherCode());
        weatherInfo.setWind(protoWeatherInfo.getWind());
        weatherInfo.setWindDirection(protoWeatherInfo.getWindDirection());
        weatherInfo.setWindSpeed(protoWeatherInfo.getWindSpeed());
        return weatherInfo;
    }

    public Weather getWeather()
    {
        return mWeather;
    }

}
