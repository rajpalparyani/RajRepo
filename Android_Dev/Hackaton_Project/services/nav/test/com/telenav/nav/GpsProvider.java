///**
// *
// * Copyright 2011 TeleNav, Inc. All rights reserved.
// * GpsProvider2.java
// *
// */
//package com.telenav.nav;
//
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileReader;
//import java.io.IOException;
//import java.io.StringReader;
//import java.net.URL;
//import java.util.Vector;
//
//import com.telenav.location.TnLocation;
//
///**
// *@author yning
// *@date 2011-6-13
// */
//public class GpsProvider implements INavGpsProvider
//{
//    int count = 0;
//
//    Vector<TnLocation> fakeLocations = new Vector<TnLocation>();
//
//    boolean isRepeatLast = false;
//    GpsProvider(String s, boolean isRepeatLast)
//    {
//        this.isRepeatLast = isRepeatLast;
//        try
//        {
//            String line = null;
//            BufferedReader reader = new BufferedReader(new StringReader(s));
//            while ((line = reader.readLine()) != null)
//            {
//                if(line.trim().length() == 0)
//                {
//                    continue;
//                }
//                int startIndex = line.indexOf('{');
//                int endIndex = line.indexOf('}');
//                int seprateIndex = line.indexOf(',');
//                String latitude = line.substring(startIndex + 1, seprateIndex).trim();
//                startIndex = seprateIndex + 1;
//                seprateIndex = line.indexOf(',', startIndex);
//                String longtitude = line.substring(startIndex, seprateIndex).trim();
//                startIndex = seprateIndex + 1;
//                seprateIndex = line.indexOf(',', startIndex);
//                String speedStr =  line.substring(startIndex, seprateIndex).trim();
//                startIndex = seprateIndex + 1;
//                seprateIndex = line.indexOf(',', startIndex);
//                String headingStr = line.substring(startIndex, seprateIndex).trim();
//                startIndex = seprateIndex + 1;
//                seprateIndex = line.indexOf(',', startIndex);
//                String accuracyStr = line.substring(startIndex, seprateIndex).trim();
//                startIndex = seprateIndex + 1;
//                seprateIndex = line.indexOf(',', startIndex);
//                String valid = line.substring(startIndex, seprateIndex).trim();
//                startIndex = seprateIndex + 1;
//                String satellite = line.substring(startIndex, endIndex).trim();
//                
//                int lat = Integer.parseInt(latitude);
//                int lon = Integer.parseInt(longtitude);
//                int speed = Integer.parseInt(speedStr);
//                int heading = Integer.parseInt(headingStr);
//                int accuracy = Integer.parseInt(accuracyStr);
//                boolean isValid = valid.equals("true") ? true : false;
//                int satelliteNum = Integer.parseInt(satellite);
//                
//                TnLocation location = new TnLocation("");
//                location.setAccuracy(accuracy);
//                location.setHeading(heading);
//                location.setLatitude(lat);
//                location.setLongitude(lon);
//                location.setSatellites(satelliteNum);
//                location.setSpeed(speed);
//                location.setTime(System.currentTimeMillis());
//                location.setLocalTimeStamp(System.currentTimeMillis());
//                location.setValid(isValid);
//                
//                fakeLocations.addElement(location);
//            }
//
//            reader.close();
//        }
//        catch (FileNotFoundException e)
//        {
//            e.printStackTrace();
//        }
//        catch (IOException e)
//        {
//            e.printStackTrace();
//        }
//    }
//
//
//    public int getFixes(int numFixes, TnLocation[] data)
//    {
//        int size = fakeLocations.size();
//        if (count < size)
//        {
//            TnLocation location = fakeLocations.elementAt(count);
//            location.setTime(System.currentTimeMillis());
//            location.setLocalTimeStamp(System.currentTimeMillis());
//            
//            count++;
//            if(location == null)
//            {
//                data[0] = null;
//                return 0;
//            }
//            else
//            {
//                data[0].set(location);
//                return 1;
//            }
//        }
//        else if(isRepeatLast)
//        {
//            TnLocation location = fakeLocations.elementAt(size - 1);
//            location.setTime(System.currentTimeMillis());
//            location.setLocalTimeStamp(System.currentTimeMillis());
//            count++;
//            if(location == null)
//            {
//                data[0] = null;
//                return 0;
//            }
//            else
//            {
//                data[0].set(location);
//                return 1;
//            }
//        }
//        else
//        {
//            count++;
//            data[0] = null;
//            return 0;
//        }
//    }
//
//
//    public int getLastKnownHeading()
//    {
//        return 0;
//    }
//    
//    private String getFilePath()
//    {
//        URL url = GpsProvider.class.getResource("");
//        String userDir = null;
//        try
//        {
//            userDir = java.net.URLDecoder.decode(url.getPath(), "utf-8");
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//        
//        return userDir;
//    }
//}
