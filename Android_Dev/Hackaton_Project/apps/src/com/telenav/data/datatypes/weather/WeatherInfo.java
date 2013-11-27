/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * WeatherInfo.java
 *
 */
package com.telenav.data.datatypes.weather;

/**
 * @author chrbu
 * @date 2011-12-31
 */
public class WeatherInfo
{

    private String temp;

    private String status;

    private String high;

    private String low;

    private int dayOfWeek;

    private String feel;

    private String wind;

    private String humidity;

    private String date;

    private String windDirection;

    private String windSpeed;

    private int weatherCode;

    private int temperatureCode;

    public String getTemp()
    {
        return temp;
    }

    public void setTemp(String temp)
    {
        this.temp = temp;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getHigh()
    {
        return high;
    }

    public void setHigh(String high)
    {
        this.high = high;
    }

    public String getLow()
    {
        return low;
    }

    public void setLow(String low)
    {
        this.low = low;
    }

    public int getDayOfWeek()
    {
        return dayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek)
    {
        this.dayOfWeek = dayOfWeek;
    }

    public String getFeel()
    {
        return feel;
    }

    public void setFeel(String feel)
    {
        this.feel = feel;
    }

    public String getWind()
    {
        return wind;
    }

    public void setWind(String wind)
    {
        this.wind = wind;
    }

    public String getHumidity()
    {
        return humidity;
    }

    public void setHumidity(String humidity)
    {
        this.humidity = humidity;
    }

    public String getDate()
    {
        return date;
    }

    public void setDate(String date)
    {
        this.date = date;
    }

    public String getWindDirection()
    {
        return windDirection;
    }

    public void setWindDirection(String windDirection)
    {
        this.windDirection = windDirection;
    }

    public String getWindSpeed()
    {
        return windSpeed;
    }

    public void setWindSpeed(String windSpeed)
    {
        this.windSpeed = windSpeed;
    }

    public int getWeatherCode()
    {
        return weatherCode;
    }

    public void setWeatherCode(int weatherCode)
    {
        this.weatherCode = weatherCode;
    }

    public int getTemperatureCode()
    {
        return temperatureCode;
    }

    public void setTemperatureCode(int temperatureCode)
    {
        this.temperatureCode = temperatureCode;
    }

}
