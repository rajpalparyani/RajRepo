/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * StringConverter_en_US.java
 *
 */
package com.telenav.res.converter;

import java.util.Calendar;
import java.util.Date;

import com.telenav.data.datatypes.address.Stop;
import com.telenav.data.datatypes.preference.Preference;
import com.telenav.datatypes.DataUtil;
import com.telenav.i18n.ResourceBundle;
import com.telenav.module.AppConfigHelper;
import com.telenav.res.IStringCommon;
import com.telenav.res.ResourceManager;

/**
 * @author fqming (fqming@telenav.cn)
 * @date 2010-11-25
 */
public class StringConverter_en_US extends StringConverter
{

    public static final String[] MONTH = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };

    public String convertCostTime(long timemillis)
    {
        int hours = 0;
        int min = 0;

        int time = (int) (timemillis / 1000);

        // ensure not to return "00:00" if time > 0
        if (time > 0)
            min = 1;

        if (time >= 60)
        {
            min = time / 60;

            if (min >= 60)
            {
                hours = min / 60;
                min = Math.max(0, min - (hours * 60));// remove hours from minutes
            }

            if (hours > 24)
            {
                return hours + "hr";
            }
        }

        if (hours <= 0)
            return min + "min";
        return hours + "hr " + min + "min";
    }

    public String convertDistance(long dist, int systemUnits)
    {
        if (systemUnits < 0 || systemUnits >= DEG2SHORT.length)
        {
            return "";
        }
        
        ResourceBundle bundle = ResourceManager.getInstance().getCurrentBundle();
        StringBuffer buf = new StringBuffer();
        boolean isLongUnit = false;// default to short units (m, feet) first

        // convert 1e-5 degrees to short units
        dist *= DEG2SHORT[systemUnits];
        dist = dist >> DataUtil.SHIFT;

        // if dist > MAX_SHORT_DISPLAYED feet or meters, convert to 0.1 km or mi
        if (dist > MAX_SHORT_UNITS)
        {
            isLongUnit = true;
            dist += ROUND[systemUnits];
            dist *= SHORT2LONG[systemUnits];
            dist = dist >> (DataUtil.SHIFT + 1);
        }

        if (isLongUnit)
        {
            String tempDist = "" + dist;
            if (dist > 9) // has multiple digits, copy all but last digit
            {
                buf.append(tempDist.substring(0, tempDist.length() - 1));
            }
            else
            {
                buf.append("0"); // place a zero before decimal point
            }

            // below MAX_LONG_DETAILED, include decimal point and tenths place

            if (dist < MAX_LONG_DETAILED)
            {
                buf.append(".");
                buf.append(tempDist.charAt(tempDist.length() - 1));
            }

            // append long units notation
            buf.append(systemUnits == 0 ? " " + bundle.getString(IStringCommon.RES_KILOMETER, IStringCommon.FAMILY_COMMON) : " "
                    + bundle.getString(IStringCommon.RES_MILE, IStringCommon.FAMILY_COMMON));
        }
        else
        {
            // just append distance and short units notation
            buf.append(dist);
            buf.append(systemUnits == 0 ? " " + bundle.getString(IStringCommon.RES_METER, IStringCommon.FAMILY_COMMON) : " "
                    + bundle.getString(IStringCommon.RES_FEET, IStringCommon.FAMILY_COMMON));
        }

        return buf.toString();
    }

   

    public String convertDistanceMeterToMile(long distanceInMeter, int unit)
    {
        String distance = "";
        ResourceBundle bundle = ResourceManager.getInstance().getCurrentBundle();
        if (distanceInMeter < 0)
            return distance;
        double distanceInFeet = distanceInMeter * METER_TO_FT;
        if (UNIT_METER == unit)// Km/Meters
        {
            if (distanceInFeet <= DISTANCE_UNIT_CRITICAL_POINT)
            {
                distance = formatToInt(distanceInMeter) + " " + bundle.getString(IStringCommon.RES_METER, IStringCommon.FAMILY_COMMON);
            }
            else
            {
                double distance_format = distanceInMeter * METER_TO_KM;
                String ditance_value = formatToOneDecimal(distance_format);
                if(distance_format > 100.00)
                {    
                    if(ditance_value.contains("."))
                    {
                        ditance_value = ditance_value.substring(0, ditance_value.indexOf("."));
                    } 
                } 
                distance = ditance_value + " " + bundle.getString(IStringCommon.RES_KILOMETER,
                            IStringCommon.FAMILY_COMMON);
            }
        }
        else if (unit == UNIT_MILE)// Mi/Ft
        {
            if (distanceInFeet <= DISTANCE_UNIT_CRITICAL_POINT)
            {
                distance = formatToInt(distanceInFeet) + " " + bundle.getString(IStringCommon.RES_FEET, IStringCommon.FAMILY_COMMON);
            }
            else
            {
                double distance_format = (distanceInMeter * KM_TO_MILE) / KM_TO_METER;
                String ditance_value = formatToOneDecimal(distance_format);
                if(distance_format > 100.00)
                {    
                    if(ditance_value.contains("."))
                    {
                        ditance_value = ditance_value.substring(0, ditance_value.indexOf("."));
                    } 
                } 
                distance = ditance_value + " " + bundle.getString(IStringCommon.RES_MILE, IStringCommon.FAMILY_COMMON);
            }
        }
        return distance;
    }

    protected static String formatToInt(double distanceDouble)
    {
        String distanceStr = "" + distanceDouble;
        int decimalIndex = distanceStr.indexOf('.');
        if(decimalIndex > 0 && decimalIndex + 1 < distanceStr.length())
        {
            String last = distanceStr.substring(decimalIndex + 1, decimalIndex + 2);
            int lastInt = Integer.parseInt(last);
            if(lastInt >= 5)
            {
                distanceStr = "" + (distanceDouble + 1.0d);
            }
            distanceStr = distanceStr.substring(0, decimalIndex);
        }
        return distanceStr;
    }
    
    protected static String formatToOneDecimal(double distanceDouble)
    {
        String distanceStr = "" + distanceDouble;
        int decimalIndex = distanceStr.indexOf('.');
        if(decimalIndex > 0 && decimalIndex + 2 < distanceStr.length())
        {
            String last = distanceStr.substring(decimalIndex + 2, decimalIndex + 3);
            int lastInt = Integer.parseInt(last);
            if(lastInt >= 5)
            {
                distanceStr = "" + (distanceDouble + 0.1d);
            }
            distanceStr = distanceStr.substring(0, distanceStr.indexOf('.') + 2);
        }
        return distanceStr;
    }
    
    public String convertHeading(int heading)
    {
        // make sure heading angle > 0
        while (heading < 0)
        {
            heading += 360;
        }

        int index = (2 * heading + 45) / 90;
        index = index % 8;
        switch (index)
        {
            case 0:
                return ResourceManager.getInstance().getCurrentBundle().getString(IStringCommon.RES_NORTH, IStringCommon.FAMILY_COMMON);
            case 1:
                return ResourceManager.getInstance().getCurrentBundle().getString(IStringCommon.RES_NORTH_EAST, IStringCommon.FAMILY_COMMON);
            case 2:
                return ResourceManager.getInstance().getCurrentBundle().getString(IStringCommon.RES_EAST, IStringCommon.FAMILY_COMMON);
            case 3:
                return ResourceManager.getInstance().getCurrentBundle().getString(IStringCommon.RES_SOUTH_EAST, IStringCommon.FAMILY_COMMON);
            case 4:
                return ResourceManager.getInstance().getCurrentBundle().getString(IStringCommon.RES_SOUTH, IStringCommon.FAMILY_COMMON);
            case 5:
                return ResourceManager.getInstance().getCurrentBundle().getString(IStringCommon.RES_SOUTH_WEST, IStringCommon.FAMILY_COMMON);
            case 6:
                return ResourceManager.getInstance().getCurrentBundle().getString(IStringCommon.RES_WEST, IStringCommon.FAMILY_COMMON);
            case 7:
                return ResourceManager.getInstance().getCurrentBundle().getString(IStringCommon.RES_NORTH_WEST, IStringCommon.FAMILY_COMMON);
        }
        return ResourceManager.getInstance().getCurrentBundle().getString(IStringCommon.RES_NORTH, IStringCommon.FAMILY_COMMON);
    }

    public String convertSpeed(int speed, int systemUnits)
    {
        if (speed < 0)
        {
            return "NA";
        }

        ResourceBundle bundle = ResourceManager.getInstance().getCurrentBundle();

        int displaySpeed = speed;
        //The speed unit is default as KPH now which is coming from Navsdk
//        displaySpeed *= DM6TOSPEED[systemUnits];
//        displaySpeed >>= DM6TOSPEED_SHIFT;
        if (Preference.UNIT_USCUSTOM == systemUnits)
        {
            displaySpeed *= KM_TO_MILE;
        }

        return "" + displaySpeed + " " + ((systemUnits == Preference.UNIT_METRIC ? bundle.getString(IStringCommon.RES_KPH, IStringCommon.FAMILY_COMMON):
            bundle.getString(IStringCommon.RES_MPH, IStringCommon.FAMILY_COMMON)));
    }

    public String convertTime(long timemillis)
    {
        if(timemillis <= 0)
        {
            return "";
        }
        Calendar c = Calendar.getInstance();
        c.setTime(new Date(timemillis));
        StringBuffer timeStr = new StringBuffer("");
        int hour = c.get(Calendar.HOUR);
        if (hour == 0)
        {
            hour = 12;
        }
        timeStr.append(hour);
        timeStr.append(":");
        int min = c.get(Calendar.MINUTE);
        if (min < 10)
        {
            timeStr.append("0");
        }
        timeStr.append(min);
        timeStr.append(c.get(Calendar.AM_PM) == Calendar.AM ? " am" : " pm");
        return timeStr.toString();
    }

    public String convertAddress(Stop stop, boolean needLabel)
    {
        if(stop == null)
        {
            return "";
        }
        StringBuffer displayString = new StringBuffer(32);
        if (needLabel && stop.getLabel() != null && stop.getLabel().length() > 0 && (stop.getFirstLine() == null || !stop.getFirstLine().startsWith(stop.getLabel()))
                && (stop.getCity() != null && !stop.getLabel().trim().equals(stop.getCity().trim())))
        {
            displayString.append(stop.getLabel());
        }

        if (stop.getFirstLine() != null && stop.getFirstLine().length() > 0)
        {
            if (displayString.length() > 0)
            {
                displayString.append(", ");
            }
            displayString.append(stop.getFirstLine());
        }

        String city = stop.getCity();
        if (city != null)
        {
            city = city.trim();
            if (city.length() > 0)
            {
                if (displayString.length() > 0) // fix 8594
                {
                    displayString.append(", "); // fix 8924 zdong
                }
                displayString.append(city);
            }
        }
        String province = stop.getProvince();
        if (province != null)
        {
            province = province.trim();
            if (province.length() > 0)
            {
                if (displayString.length() > 0) // fix 8594
                {
                    displayString.append(", "); // fix 8924 zdong
                }
                displayString.append(province);
            }
        }
        
        if (stop.getPostalCode() != null && stop.getPostalCode().length() > 0)
        {
            try
            {
                int postalCode = Integer.parseInt(stop.getPostalCode());
                if(postalCode != 0)
                {
                    if (displayString.length() > 0) 
                    {
                        displayString.append(" ");
                    }
                    displayString.append(stop.getPostalCode());
                }
            }
            catch (Exception e)
            {
                if (displayString.length() > 0) 
                {
                    displayString.append(", ");
                }
                displayString.append(stop.getPostalCode());
            }
        }
        

        // fix bug 16371
        if (stop.getLat() != 0 && stop.getLon() != 0 && displayString.length() == 0)
        {
            ResourceBundle bundle = ResourceManager.getInstance().getCurrentBundle();
            
            String latStr = convertLatLon(stop.getLat());
            String lonStr = convertLatLon(stop.getLon());
            displayString.append(bundle.getString(IStringCommon.RES_LAT, IStringCommon.FAMILY_COMMON));
            displayString.append(' ');
            displayString.append(latStr);
            displayString.append(", ");
            displayString.append(bundle.getString(IStringCommon.RES_LON, IStringCommon.FAMILY_COMMON));
            displayString.append(' ');
            displayString.append(lonStr);
        }
        return displayString.toString();
    }
    
    public String convertTimeToDay(long timemillis)
    {
        Date date = new Date(timemillis);
        StringBuffer buffer = new StringBuffer();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int month = calendar.get(Calendar.MONTH);
        buffer.append(MONTH[month]);
        buffer.append(" ");
        buffer.append(calendar.get(Calendar.DAY_OF_MONTH));
        buffer.append(", ");
        buffer.append(calendar.get(Calendar.YEAR));
        return buffer.toString();
    }

    public String convertSecondLine(Stop address)
    {
        if(address == null)
            return null;
        StringBuffer buffer = new StringBuffer();
        if(address.getCity() != null && !"".equals(address.getCity()))
        {
            buffer.append(address.getCity());
        }
        if(address.getProvince() != null &&!"".equals(address.getProvince()))
        {
            if(buffer.length() > 0)
            {
                buffer.append(", ");
            }
            buffer.append(address.getProvince());
        }
        if (address.getPostalCode() != null && address.getPostalCode().length() > 0)
        {
            try
            {
                int postalCode = Integer.parseInt(address.getPostalCode());
                if(postalCode != 0)
                {
                    if (buffer.length() > 0) 
                    {
                        buffer.append(" ");
                    }
                    buffer.append(address.getPostalCode());
                }
            }
            catch (Exception e)
            {
                if (buffer.length() > 0) 
                {
                    buffer.append(", ");
                }
                buffer.append(address.getPostalCode());
            }
        }
        return buffer.toString();
    }

    public String convertPhoneNumber(String phone)
    {
        if (AppConfigHelper.isGlobalPtn())
        {
            return phone;
        }
        else
        {
            phone = checkPhoneNumber(phone);
            if (phone.length() < 7)
                return phone;
            String number = "";
            if(phone.length() == 7)
            {
                number = phone.substring(0, 3) + "-" + phone.substring(3);
            }
            else if(phone.length() > 7 && phone.length() <= 10)
            {
                int index = phone.length() - 7;
                number = "(" + phone.substring(0, index) + ")" + " " + phone.substring(index, index + 3) + "-" + phone.substring(index + 3);
            }
            else if(phone.length() >= 11)
            {
                String realPhone = phone.substring(phone.length()-10);
                int index = realPhone.length() - 7;
                number = "(" + realPhone.substring(0, index) + ")" + " " + realPhone.substring(index, index + 3) + "-" + realPhone.substring(index + 3);
            }
            return number;
        }
    }
    
    private String checkPhoneNumber(String phoneNum)
    {
        if(phoneNum == null || phoneNum.trim().length() == 0)
        {
            return "";
        }
        
        StringBuffer phone = new StringBuffer();
        for(int i = 0; i < phoneNum.length(); i++)
        {
            if(Character.isDigit(phoneNum.charAt(i)))
            {
                phone.append(phoneNum.charAt(i));
            }
        }
        
        return phone.toString();
    }
    
    public String convertStopFirstLine(Stop stop)
    {
        if (stop == null)
        {
            return "";
        }
        String firstLine = "";
        String street = stop.getStreetName();
        String houseNumber = stop.getHouseNumber();
        String crossStreet = stop.getCrossStreetName();
        if (street != null && street.length() > 0)
        {
            if (street.indexOf("\\") > 0)
            {
                street = street.substring(0, street.indexOf("\\"));
            }
            if (houseNumber != null && houseNumber.length() > 0)
            {
                firstLine = houseNumber + " " + street;
            }
            else if (crossStreet != null && crossStreet.length() > 0)
            {
                if (crossStreet.indexOf("\\") > 0)
                {
                    crossStreet = crossStreet.substring(0, crossStreet.indexOf("\\"));
                }
                firstLine = crossStreet + " at " + street; // fix bug 8924 zdong
            }
            else
            {
                firstLine = street;
            }
        }
        else
        {
            firstLine = "";
        }

        return firstLine;
    }
}
