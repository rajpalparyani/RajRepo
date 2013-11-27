/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * StringConverter.java
 *
 */
package com.telenav.res.converter;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import com.telenav.data.datatypes.address.Stop;
import com.telenav.util.StringTokenizer;

/**
 * @author fqming (fqming@telenav.cn)
 * @date 2010-11-25
 */
public abstract class StringConverter
{
    // --------------------------------------------------------------
    // conversion from deg^-5 to metric and us customary units
    // to round up to 0.1 km, add 1000/10/2 meters
    // to round up to 0.1 mi, add 5280/10/2 (feet)
    public static final int [] ROUND = { 50, 264 };
    // to convert 1e-5 to meters, multiply by 9119/8192
    // to convert 1e-5 to feet, multiply by 29919/8192
    public static final int [] DEG2SHORT = { 9119, 29919 };
    // to convert short units (meters/feet) to long units (tenths of km/miles)
    // metric - multiply by 8192 * 2 / (1000/10) divided by 8192 * 2 --- (164) 
    // miles  - multiply by 8192 * 2 / (5280/10) divided by 8192 * 2 --- (31)
    public static final int [] SHORT2LONG = { 164, 31 };
    // maximum short units displayed before converting to long units
    public static final int MAX_SHORT_UNITS = 500;

    // maximum long units for which include numbers after decimal point, in 10ths of long units
    public static final int MAX_LONG_DETAILED = 200; // 20km, 20mi

    public static final int DM6TOSPEED_SHIFT = 15;

    public static final int[] DM6TOSPEED = { 12976, 8064 };
    
    
    public static final int UNIT_METER = 0;
    public static final int UNIT_MILE = 1;
    public static final int UNIT_KM = 2;
    public static final int UNIT_FOOT = 3;
    
    public static final double KM_TO_MILE = 0.621;
    public static final int KM_TO_METER = 1000;
    public static final double METER_TO_KM = 0.001;
    public static final double METER_TO_FT = 3.281;

    public static final int DISTANCE_UNIT_CRITICAL_POINT = 500;
    
    public static final String DATA_SIZE_UNIT_BYTE = "B";
    
    public static final String DATA_SIZE_UNIT_KB = "KB";
    
    public static final String DATA_SIZE_UNIT_MB = "MB";
    
    public static final String DATA_SIZE_UNIT_GB = "GB";
    
    /**
     * convert distance to string format.
     * 
     * @param distance
     * @param systemUnits 0 - KM,  1 - MI
     * 
     * @return a string
     */
    public abstract String convertDistance(long distance, int systemUnits);
    
    /**
     * convert meter distance unit to mile unit
     * @param distanceInMeter
     * @param unit
     * @return a string
     */
    public abstract String convertDistanceMeterToMile(long distanceInMeter, int unit);
    
    /**
     * convert heading to string format.
     * 
     * @param heading 0 - 360 degree
     * @return a string
     */
    public abstract String convertHeading(int heading);
    
    /**
     * convert the speed.
     * 
     * @param speed
     * @return
     */
    public abstract String convertSpeed(int speed, int systemUnits);
    
    /**
     * convert the long time as string format like 10 h : 30 m.
     * 
     * @param timemillis time
     * @return a string
     */
    public abstract String convertCostTime(long timemillis);
    
    /**
     * convert the long time as string format like 10 h : 30 m PM.
     * 
     * @param timemillis time
     * @return a string
     */
    public abstract String convertTime(long timemillis);
    
    public abstract String convertTimeToDay(long timemillis);
    
    public abstract String convertAddress(Stop address, boolean needLabel);
    
    public abstract String convertSecondLine(Stop address);
    
    /**
     * convert the phone number as '000-000-0000'
     * 
     * @param phone
     * @return like '000-000-0000'
     */
    public abstract String convertPhoneNumber(String phone);
    
    
    public abstract String convertStopFirstLine(Stop stop);
    
    /**
     * convert the string like 'hello ${0}, are you ok?'
     * 
     * @param pattern like 'hello ${0}, are you ok?'
     * @param values the string of index position.
     * @return a string
     */
    public String convert(String pattern, String[] values)
    {
        if (pattern == null || values == null)
            return null;
        
        String value;
        for (int i = 0; i < values.length; i ++)
        {
            value = values[i];
            pattern = replace(pattern, "${" + i + "}", value);
        }
        
        return pattern;
    }
    
    private String replace(String orig, String oldSubStr, String newSubStr)
    {
        int startIndex = orig.indexOf(oldSubStr);
        if (startIndex == -1)
            return orig;

        String pre = "";
        String sub = "";
        if (startIndex != 0)
        {
            pre = orig.substring(0, startIndex);
        }
        int endIndex = startIndex + oldSubStr.length();
        if (endIndex < orig.length())
        {
            sub = orig.substring(endIndex);
        }
        
        return pre + newSubStr + sub;
    }
    
    public static boolean isWordMatched(String prefix, String priStr)
    {
        prefix = prefix.trim();
        if(prefix.length() == 0)
        {
            return true;
        }
        
        priStr = priStr.toUpperCase();

        priStr = priStr.replace(':', ' ');
        priStr = priStr.replace('.', ' ');
        priStr = priStr.replace(';', ' ');
        priStr = priStr.replace(',', ' ');

        priStr = priStr.trim();
        
        String tempPrefix1 = prefix.toUpperCase();
        String tempPrefix2 = tempPrefix1;
        
        if (tempPrefix2.length() > 0 && tempPrefix2.charAt(0) !=  ' ')
        {
            tempPrefix2 = " " + tempPrefix2;
        }
        
        if (tempPrefix1.length() == 0 || priStr.startsWith(tempPrefix1) || priStr.indexOf(tempPrefix2) > 0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    String convertAirport(String airport)
    {
        airport = airport.toLowerCase();
        String newAirport = convertAirport(airport, " ");
        newAirport = convertAirport(newAirport, "-");
        return newAirport;
    }
    
    String convertAirport(String airport, String deltim)
    {
        StringBuffer newAirport = new StringBuffer();
        StringTokenizer st = new StringTokenizer(airport, deltim, true);
        boolean hasMore = st.hasMoreTokens();
        if(!hasMore)
        {
            newAirport.append(airport);
        }
        while(st.hasMoreTokens())
        {
            String token = st.nextToken();
            if(token.length() > 1)
            {
                newAirport.append(Character.toUpperCase(token.charAt(0))).append(token.substring(1));
            }
            else
            {
                newAirport.append(token);
            }
        }
        
        return newAirport.toString();
    }

    public String converAirportLabel(String airport)
    {
        int index = airport.indexOf(":");
        String airportLabel;
        if (index != -1)
        {
            airportLabel = airport.substring(index + 1);
        }
        else
        {
            airportLabel = airport;
        }
        String convertedAirport = convertAirport(airportLabel);
        return index != -1 ? airport.substring(0, index + 1) + convertedAirport : convertedAirport;
    }
    
    public String convertLatLon(int latOrLon)
    {
        StringBuffer output = new StringBuffer();
        if(latOrLon < 0)
        {
            output.append("-");
        }
        
        latOrLon = Math.abs(latOrLon);
        if(latOrLon > 100000)
        {
            output.append(latOrLon/100000);
            output.append(".");
            output.append(getDecimalLatLon(latOrLon % 100000));
        }
        else
        {
            output.append("0.");
            output.append(getDecimalLatLon(latOrLon));
        }
        
        return output.toString();
    }
    
    private String getDecimalLatLon(int latOrLon)
    {
        if(latOrLon > 100000)
            return "";
        
        StringBuffer output = new StringBuffer();
        
        String tmp = String.valueOf(latOrLon).trim();
        int zeroShift = 5 - tmp.length();
        if(zeroShift > 0)
        {
            for(int i = 0; i < zeroShift; i ++)
            {
                output.append("0");
            }
        }
        output.append(tmp);
        
        return output.toString();
    }
    
    /**
     * Return the data size in B,KB,MB,GB format.
     * @param sizeInByte The data size in byte.
     * @param precision  The precision of the decimal digits.
     * @param forceCarryDigit  if set to true, when doing round operation, it will carry digit if the value > 0; else, it will carry digit if the value >= 5.
     * <br>e.g., if set to true, 0.1111 will turn to 0.112 for precision of 3. 
     * <br>If set to false, 0.1111 will turn to 0.111 but 0.1115 will be 0.112.
     * @return for sizeInByte <= 0, will return 0B
     * <br> for sizeInByte < 1024, will return xB.
     * <br> for sizeInByte < 1048576, will return xKB.
     * <br> for sizeInByte < 1073741824L, will return xMB.
     * <br> for sizeInByte >= 1073741824L, will return xGB.
     */
    public String convertDataSizeString(long sizeInByte, int precision, boolean forceCarryDigit)
    {
        StringBuilder sb = new StringBuilder();
        
        if (sizeInByte <= 0)
        {
            return "0B";
        }
        if (sizeInByte < 1024)
        {
            sb.append(sizeInByte);
            sb.append("B");
        }
        else 
        {
            if (sizeInByte < 1024 * 1024) //< 1MB
            {
                long threshold = 1024;

                String result = getDisplayedFloatValueStr(sizeInByte, threshold, precision, forceCarryDigit);
                if (checkThreshold(result, 1024))
                {
                    sb.append(result);
                    sb.append(DATA_SIZE_UNIT_KB);
                }
                else
                {
                    sb.append(1);
                    sb.append(DATA_SIZE_UNIT_MB);
                }
            }
            else if (sizeInByte < 1024L * 1024 * 1024) //< 1GB
            {
                long threshold = 1024 * 1024;
                
                String result = getDisplayedFloatValueStr(sizeInByte, threshold, precision, forceCarryDigit);
                if (checkThreshold(result, 1024))
                {
                    sb.append(result);
                    sb.append(DATA_SIZE_UNIT_MB);
                }
                else
                {
                    sb.append(1);
                    sb.append(DATA_SIZE_UNIT_GB);
                }
            }
            else
            {
                long threshold = 1024 * 1024 * 1024;
                
                String result = getDisplayedFloatValueStr(sizeInByte, threshold, precision, forceCarryDigit);
                sb.append(result);
                sb.append(DATA_SIZE_UNIT_GB);
            }
        }            
        
        return sb.toString();
    }
    
    /**
     * Get the 
     * @param sizeInByte Data size in byte.
     * @param threshold The size of 1KB, 1MB or 1GB, depends on invoker.
     * @param precision The precision of decimal digits.
     * @param forceCarryDigit if set to true, when doing round operation, it will carry digit if the value > 0; else, it will carry digit if the value >= 5.
     * <br>e.g., if set to true, 0.1111 will turn to 0.112 for precision of 3. 
     * <br>If set to false, 0.1111 will turn to 0.111 but 0.1115 will be 0.112.
     * @return The value of the float after considering carry digit.
     */
    private String getDisplayedFloatValueStr(long sizeInByte, long threshold, int precision, boolean forceCarryDigit)
    {
        boolean isNegtive = false;
        
        if(sizeInByte < 0)
        {
            isNegtive = true;
            sizeInByte *= (-1);
        }
        
        long integer = sizeInByte / threshold;
        
        int shift = 1;
        
        //shift the number with the precision.
        //e.g., 1/3 will get 0.3333333333.
        //if precision is 2, we will get 0.33.
        //But float operation may lose precision.
        //So make it as 1 * 100 / 3. we get 33.33333
        //The precision is good. We only check the first decimal digit.
        for(int i = 0; i < precision; i++)
        {
            shift *= 10;
        }
        
        double demicalFloat = ((double)(sizeInByte % threshold) * shift) / threshold;
        
        int demicalInt = 0;
        if(forceCarryDigit)
        {
            //It will carry digit if the first decimal digit > 0.
            demicalInt = (int)(demicalFloat + 0.9);
        }
        else
        {
            //It will carry digit if the first decimal digit >= 5.
            demicalInt = (int)(demicalFloat + 0.5);
        }
        
        if(demicalInt >= shift)
        {
            demicalInt -= shift;
            integer++;
        }
        
        StringBuilder tmp = new StringBuilder();
        if(demicalInt > 0)
        {
            tmp.append(demicalInt);
            while(tmp.length() < precision)
            {
                tmp.insert(0, 0);
            }
            
            while(tmp.charAt(tmp.length() - 1) == '0')
            {
                tmp.deleteCharAt(tmp.length() - 1);
            }
        }
        
        StringBuilder sb = new StringBuilder();
        if(isNegtive)
        {
            sb.append("-");
        }
        sb.append(integer);
        if(demicalInt > 0)
        {
            sb.append(".").append(tmp.toString());
        }
        
        return sb.toString();
    }
    
    /**
     * 
     * @param floatStr
     * @return
     */
    private boolean checkThreshold(String floatStr, long threshold)
    {
        BigDecimal checkPoint = new BigDecimal(threshold);
        BigDecimal bigDecimal = new BigDecimal(floatStr);
        
        if(bigDecimal.compareTo(checkPoint) > -1)
        {
            return false;
        }
        else
        {
            return true;
        }
    }
    
    public String getDateString(long time, int style)
    {
        Date date = new Date(time);
        DateFormat dateFormat = DateFormat.getDateInstance(style, Locale.getDefault());
        String result = dateFormat.format(date);
        
        return result;
    }
}
