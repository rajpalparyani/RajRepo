/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * LocationDayModeCalculater.java
 *
 */
package com.telenav.module.location;

import java.util.Calendar;
import java.util.TimeZone;

import com.telenav.location.TnLocation;
import com.telenav.logger.Logger;

/**
 *@author hchai
 *@date 2012-7-13
 */
public class LocationDayModeCalculater71
{

    private final static double pi = 3.1415926535897932384626433832795;
    private final static double tpi = 2 * pi;
    private final static double degs = 180.0/pi;
    private final static double rads = pi/180.0;

    private final static double SunDia = 0.53;     // Sunradius degrees

    private final static double AirRefr = 34.0/60.0; // athmospheric refraction degrees //

    private static boolean isDayMode = true;
    static long lastCheckedTime = -1L;
    private static TnLocation lastLocation = null;
    
    //   Get the days to J2000
    //   h is UT in decimal hours
    //   FNday only works between 1901 to 2099 - see Meeus chapter 7

    private static double fnDay (int y, int m, int d, float h) 
    {
        int luku = - 7 * (y + (m + 9)/12)/4 + 275*m/9 + d;
        
        // type casting necessary on PC DOS and TClite to avoid overflow
        
        luku+= (int)y*367;
        return (double)luku - 730531.5 + h/24.0;
    }

    //   the function below returns an angle in the range
    //   0 to 2*pi

    private static double fnRange (double x) {
        double b = x / tpi;
        double a = tpi * (b - (long)(b));
        if (a < 0) a = tpi + a;
        return a;
    }

    // Calculating the hourangle
    //
    private static double f0(double lat, double declin) {
        double fo,dfo;
        // Correction: different sign at S HS
        dfo = rads*(0.5*SunDia + AirRefr); if (lat < 0.0) dfo = -dfo;
        fo = Math.tan(declin + dfo) * Math.tan(lat*rads);
        if (fo>0.99999) fo=1.0; // to avoid overflow //
        fo = asin(fo) + pi/2.0;
        return fo;
    }

    // Calculating the hourangle for twilight times
    //
    private static double f1(double lat, double declin)
    {
        double fi,df1;
        // Correction: different sign at S HS
        df1 = rads * 6.0; if (lat < 0.0) df1 = -df1;
        fi = Math.tan(declin + df1) * Math.tan(lat*rads);
        if (fi>0.99999) fi=1.0; // to avoid overflow //
        fi = asin(fi) + pi/2.0;
        return fi;
    }


    //   Find the ecliptic longitude of the Sun

    private static double fnSun (double d) 
    {
        //   mean longitude of the Sun
        
        double l = fnRange(280.461 * rads + .9856474 * rads * d);
        
        //   mean anomaly of the Sun
        
        double g = fnRange(357.528 * rads + .9856003 * rads * d);
        
        //   Ecliptic longitude of the Sun
        
        return fnRange(l + 1.915 * rads * Math.sin(g) + .02 * rads * Math.sin(2 * g));
    }

    public static boolean isDayMode()
    {
        if(lastCheckedTime <= 0)
        {
            int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
            isDayMode = hour >= 6 && hour <= 19;
        }
        
        checkDayMode(lastLocation);
        
        return isDayMode;
    }

    public static void checkDayMode(TnLocation location)
    {
        if(location == null)
            return;
        
        long interval = System.currentTimeMillis() - lastCheckedTime;
        if(interval < 60000L && interval > 0L)
            return;
        
        try
        {
            lastLocation = location;
            
            int lat = location.getLatitude();
            int lon = location.getLongitude();
            
            if (lat == 0 && lon == 0)
            {
                //invalid location
                return;
            }

            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1;
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            float[] timeRise = new float[1];
            float[] timeSet = new float[1];

            genSunRiseSetInGMT(year, month, day, (float) (lat / 100000.0), (float) (lon / 100000.0), timeRise, timeSet);

            isDayMode = true;
            float time1, time2;

            if (timeRise[0] > timeSet[0])
            {
                isDayMode = false;
                time1 = timeSet[0];
                time2 = timeRise[0];
            }
            else
            {
                isDayMode = true;
                time1 = timeRise[0];
                time2 = timeSet[0];
            }

            float currHour = hour + (float) (minute / 60.0);
            if (currHour < time1 || currHour >= time2)
            {
                isDayMode = !isDayMode;
            }
            
            lastCheckedTime = System.currentTimeMillis();
            
        }
        catch(Exception e)
        {
            Logger.log(LocationDayModeChecker.class.getName(), e);
        }
    }
    
    private static void genSunRiseSetInGMT(int y, int m, int day, float latit, float longit, float[] rise_time, float[] set_time)
    {
        double riset, settm;
            
        double d = fnDay(y, m, day, 12);
        
        //   Use FNsun to find the ecliptic longitude of the
        //   Sun
        
        double lambda = fnSun(d);
        
        //   Obliquity of the ecliptic
        
        double obliq = 23.439 * rads - .0000004 * rads * d;
        
        //   Find the RA and DEC of the Sun
        
        double alpha = atan2(Math.cos(obliq) * Math.sin(lambda), Math.cos(lambda));
        double delta = asin(Math.sin(obliq) * Math.sin(lambda));
        
        // Find the Equation of Time
        // in minutes
        // Correction suggested by David Smith
        double l = fnRange(280.461 * rads + .9856474 * rads * d);
        double ll = l - alpha;
        
        if (l < pi) ll += tpi;
        
        double equation = 1440.0 * (1.0 - ll / tpi);
        double ha = f0(latit,delta);
        double hb = f1(latit,delta);
        double twx = hb - ha;   // length of twilight in radians
        
        twx = 12.0*twx/pi;      // length of twilight in hours
        
        // Conversion of angle to hours and minutes //
        double daylen = degs*ha/7.5;
        
        if (daylen<0.0001) {daylen = 0.0;}
        
        // arctic winter     //
        
        riset = 12.0 - 12.0 * ha/pi - longit/15.0 + equation/60.0;
        
        settm = 12.0 + 12.0 * ha/pi - longit/15.0 + equation/60.0;
        
        //double twam = riset - twx;    // morning twilight begin
        double twpm = settm + twx;      // evening twilight end
        
        //if (riset > 24.0) riset-= 24.0;
        //if (settm > 24.0) settm-= 24.0;
        
        rise_time[0] = (float) riset;
        set_time[0]  = (float) twpm;
        
        //the rise_time maybe 50+, so -24 is not enough
        if(rise_time[0] >= 24.0)
        {
            rise_time[0] = rise_time[0] % 24;
        }
        else if(rise_time[0] < 0)
        {
            rise_time[0] = rise_time[0] % 24 + 24.0f;
        }
        
        //the set_time maybe 50+, so -24 is not enough
        if(set_time[0] >= 24.0)
        {
            set_time[0] = set_time[0] % 24;
        }
        else if(set_time[0] < 0)
        {
            set_time[0] = set_time[0] % 24 + 24.0f;
        }
    }
    
    private static final double sq2p1 = 2.414213562373095048802e0;
    private static final double sq2m1  = .414213562373095048802e0;
    private static final double p4  = .161536412982230228262e2;
    private static final double p3  = .26842548195503973794141e3;
    private static final double p2  = .11530293515404850115428136e4;
    private static final double p1  = .178040631643319697105464587e4;
    private static final double p0  = .89678597403663861959987488e3;
    private static final double q4  = .5895697050844462222791e2;
    private static final double q3  = .536265374031215315104235e3;
    private static final double q2  = .16667838148816337184521798e4;
    private static final double q1  = .207933497444540981287275926e4;
    private static final double q0  = .89678597403663861962481162e3;
    private static final double PIO2 = 1.5707963267948966135E0;
    private static final double nan = (0.0/0.0);
    
    private static double mxatan(double arg)
    {
        double argsq, value;

        argsq = arg*arg;
        value = ((((p4*argsq + p3)*argsq + p2)*argsq + p1)*argsq + p0);
        value = value/(((((argsq + q4)*argsq + q3)*argsq + q2)*argsq + q1)*argsq + q0);
        return value*arg;
    }

    // reduce
    private static double msatan(double arg)
    {
        if(arg < sq2m1)
            return mxatan(arg);
        if(arg > sq2p1)
            return PIO2 - mxatan(1/arg);
            return PIO2/2 + mxatan((arg-1)/(arg+1));
    }

    // implementation of atan
    private static double atan(double arg)
    {
        if(arg > 0)
            return msatan(arg);
        return -msatan(-arg);
    }

    // implementation of atan2
    private static double atan2(double arg1, double arg2)
    {
        if(Math.abs((arg1+arg2) - arg1) < 0.0000001)
        {
            if(arg1 >= 0)
            return PIO2;
                return -PIO2;
        }
        arg1 = atan(arg1/arg2);
        if(arg2 < 0)
       {
            if(arg1 <= 0)
                return arg1 + Math.PI;
            return arg1 - Math.PI;
        }
        return arg1;
    
    }

    // implementation of asin
    private static double asin(double arg)
    {
        double temp;
        int sign;

        sign = 0;
        if(arg < 0)
        {
            arg = -arg;
            sign++;
        }
        if(arg > 1)
            return nan;
        temp = Math.sqrt(1 - arg*arg);
        if(arg > 0.7)
            temp = PIO2 - atan(temp/arg);
        else
            temp = atan(arg/temp);
        if(sign > 0)
            temp = -temp;
        return temp;
    }

}
