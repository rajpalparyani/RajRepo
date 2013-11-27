/**
 *
 * Copyright 2013 TeleNav, Inc. All rights reserved.
 * SortUtil.java
 *
 */
package com.telenav.data.sort.util;

import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import com.telenav.data.datatypes.address.Address;
import com.telenav.sort.SortAlgorithm;

/**
 * @author yren
 * @date 2013-3-11
 */
public class SortUtil
{
    private static String GROUP_MY_UP_COMING_EVENTS = "MY UPCOMING EVENTS";
    private static String GROUP_TODAY = "TODAY";
    private static String GROUP_YESTER_DAY = "YESTERDAY";
    private static String GROUP_A_WEEK = "THIS WEEK";
    private static String GROUP_A_MONTH = "THIS MONTH";
    private static String GROUP_OLDER = "OLDER";
    private static String GROUP_NUMERRIC = "#";
    
    public final static int SORT_TYPE_ALPHABET = 1;
    public final static int SORT_TYPE_DATE = 2;
    public final static int SORT_TYPE_DISTANCE = 3;
    
    private static SortUtil instance = new SortUtil();

    public static SortUtil getInstance()
    {
        return instance;
    }

    public void sortByAlphabet(Vector elements)
    {
        sortByAlphabet(elements, SortAlgorithm.MERGE_SORT, true);
    }

    public void sortByAlphabet(Vector elements, int sortType, boolean needGroup)
    {
        if (elements == null || elements.size() < 1)
        {
            return;
        }

        MyPlaceCompareableAddress[] compareableAddresses = new MyPlaceCompareableAddress[elements.size()];
        for (int i = 0; i < elements.size(); i++)
        {
            Address address = (Address) elements.elementAt(i);
            if (address.getType() != Address.TYPE_GROUP)
            {
                MyPlaceCompareableAddress obj = new MyPlaceCompareableAddress(address);
                obj.compareValue = (address.getDisplayedText().trim().equals("") ? " " : address.getDisplayedText()).toUpperCase();
                obj.setSortType(SortUtil.SORT_TYPE_ALPHABET);
                compareableAddresses[i] = obj;
            }
        }
        
        elements.clear();

        SortAlgorithm.sort(compareableAddresses, sortType);

        char lastChar = ' ';

        for (int i = 0; i < compareableAddresses.length; i++)
        {
            if (compareableAddresses[i] != null)
            {
                Address tempAddress = ((MyPlaceCompareableAddress) compareableAddresses[i]).address;
                if (needGroup)
                {
                    String tempStr = tempAddress.getDisplayedText().length() == 0 ? " " : tempAddress.getDisplayedText();
                    char tempChar = tempStr.charAt(0);
                    if (Character.isLetter(tempChar) && Character.toUpperCase(lastChar) != Character.toUpperCase(tempChar))
                    {
                        Address groupAddress = new Address();
                        groupAddress.setType(Address.TYPE_GROUP);
                        groupAddress.setLabel(String.valueOf(tempChar).toUpperCase());
                        elements.add(groupAddress);
                    }
                    else if (!Character.isLetter(tempChar) && (Character.isLetter(lastChar) || i == 0))
                    {
                        Address groupAddress = new Address();
                        groupAddress.setType(Address.TYPE_GROUP);
                        groupAddress.setLabel(GROUP_NUMERRIC);
                        elements.add(groupAddress);
                    }
                    lastChar = tempChar;
                }
                elements.add(tempAddress);
            }
        }
    }

    public void sortByDistance(Vector elements)
    {
        sortByDistance(elements, SortAlgorithm.MERGE_SORT, false);
    }
    
    public void sortByDistance(Vector elements, int sortType, boolean needGroup)
    {
        if (elements == null || elements.size() < 1)
        {
            return;
        }

        MyPlaceCompareableAddress[] compareableAddresses = new MyPlaceCompareableAddress[elements.size()];
        for (int i = 0; i < elements.size(); i++)
        {
            Address address = (Address) elements.elementAt(i);
            if(address.getType() != Address.TYPE_GROUP);
            {
                MyPlaceCompareableAddress obj = new MyPlaceCompareableAddress(address);
                obj.compareValue = Integer.valueOf(address.getDistance());
                obj.setSortType(SortUtil.SORT_TYPE_DISTANCE);
                compareableAddresses[i] = obj;
            }
        }
        
        elements.clear();

        SortAlgorithm.sort(compareableAddresses, sortType);
        
        for (int i = 0; i < compareableAddresses.length; i++)
        {
            if (compareableAddresses[i] != null)
            {
                Address tempAddress = ((MyPlaceCompareableAddress) compareableAddresses[i]).address;
                elements.add(tempAddress);
            }
        }
    }

    public void sortByTimeStamp(Vector elements)
    {
        sortByTimeStamp(elements, SortAlgorithm.MERGE_SORT, true, true);
    }
    
    public void sortByTimeStamp(Vector elements, boolean needUpcomingEventCategory)
    {
        sortByTimeStamp(elements, SortAlgorithm.MERGE_SORT, true, needUpcomingEventCategory);
    }
    
    public void sortByTimeStamp(Vector elements, int sortType, boolean needGroup, boolean needUpcomingEventCategory)
    {
        if (elements == null || elements.size() < 1)
        {
            return;
        }

        Vector upComingEvents = new Vector();
        if (needUpcomingEventCategory)
        {
            upComingEvents = getUpComingEvents(elements, sortType, needGroup);
        }
        
        MyPlaceCompareableAddress[] compareableAddresses = new MyPlaceCompareableAddress[elements.size()];
        for (int i = 0; i < elements.size(); i++)
        {
            Address address = (Address) elements.elementAt(i);
            if (address.getType() != Address.TYPE_GROUP)
            {
                MyPlaceCompareableAddress obj = new MyPlaceCompareableAddress(address);
                obj.compareValue = Long.valueOf(getCompareTime(address, needUpcomingEventCategory));
                obj.setSortType(SortUtil.SORT_TYPE_DATE);
                compareableAddresses[i] = obj;
            }
        }
        
        elements.clear();

        SortAlgorithm.sort(compareableAddresses, sortType);
        
        long currentTimeStamp = System.currentTimeMillis();
        byte[] groupState = new byte[]
        { 0, 0, 0, 0, 0 };

        if (upComingEvents.size() > 0)
        {
            //Upcoming events should sort by chronological order
            Address groupAddress = new Address();
            groupAddress.setType(Address.TYPE_GROUP);
            groupAddress.setLabel(GROUP_MY_UP_COMING_EVENTS);
            elements.add(groupAddress);
            for (int i = 0; i < upComingEvents.size(); i++)
            {
                Address tempAddress = (Address)upComingEvents.elementAt(i);
                elements.add(tempAddress);
            }
        }
        
        for (int i = 0; i < compareableAddresses.length; i++)
        {
            if (compareableAddresses[i] != null)
            {
                Address tempAddress = ((MyPlaceCompareableAddress) compareableAddresses[i]).address;
                if (needGroup)
                {
                    if (isToday(currentTimeStamp, getCompareTime(tempAddress, needUpcomingEventCategory)))
                    {
                        if (groupState[0] == 0)
                        {
                            Address groupAddress = new Address();
                            groupAddress.setType(Address.TYPE_GROUP);
                            groupAddress.setLabel(GROUP_TODAY);
                            elements.add(groupAddress);
                            groupState[0] = 1;
                        }
                    }
                    else if (isYesterDay(currentTimeStamp, getCompareTime(tempAddress, needUpcomingEventCategory)))
                    {
                        if (groupState[1] == 0)
                        {
                            Address groupAddress = new Address();
                            groupAddress.setType(Address.TYPE_GROUP);
                            groupAddress.setLabel(GROUP_YESTER_DAY);
                            elements.add(groupAddress);
                            groupState[0] = 1;
                            groupState[1] = 1;
                        }
                    }
                    else if (isInWeek(currentTimeStamp, getCompareTime(tempAddress, needUpcomingEventCategory)))
                    {
                        if (groupState[2] == 0)
                        {
                            Address groupAddress = new Address();
                            groupAddress.setType(Address.TYPE_GROUP);
                            groupAddress.setLabel(GROUP_A_WEEK);
                            elements.add(groupAddress);
                            groupState[0] = 1;
                            groupState[1] = 1;
                            groupState[2] = 1;
                        }
                    }
                    else if (isInMonth(currentTimeStamp, getCompareTime(tempAddress, needUpcomingEventCategory)))
                    {
                        if (groupState[3] == 0)
                        {
                            Address groupAddress = new Address();
                            groupAddress.setType(Address.TYPE_GROUP);
                            groupAddress.setLabel(GROUP_A_MONTH);
                            elements.add(groupAddress);
                            groupState[0] = 1;
                            groupState[1] = 1;
                            groupState[2] = 1;
                            groupState[3] = 1;
                        }
                    }
                    else if (groupState[4] == 0)
                    {
                        Address groupAddress = new Address();
                        groupAddress.setType(Address.TYPE_GROUP);
                        groupAddress.setLabel(GROUP_OLDER);
                        elements.add(groupAddress);
                        groupState[0] = 1;
                        groupState[1] = 1;
                        groupState[2] = 1;
                        groupState[3] = 1;
                        groupState[4] = 1;
                    }
                }
                elements.add(tempAddress);
            }
        }
    }
    
    private long getCompareTime(Address address, boolean needUpcomingEvent)
    {
        long time;

        if (address.getEventId() > 0 && needUpcomingEvent)
        {
            if (address.getEventStartTime() > 0)
            {
                time = address.getEventStartTime() * 1000;
            }
            else
            {
                time = address.getUpdateTime();
            }
        }
        else
        {
            time = address.getUpdateTime();
        }

        return time;
    }
    
    public Vector getUpComingEvents(Vector elements, int sortType, boolean needGroup)
    {
        MyPlaceCompareableAddress[] compareableAddresses = new MyPlaceCompareableAddress[elements.size()];
        for (int i = 0; i < elements.size(); i++)
        {
            Address address = (Address) elements.elementAt(i);
            if (address.getType() != Address.TYPE_GROUP)
            {
                MyPlaceCompareableAddress obj = new MyPlaceCompareableAddress(address);
                obj.compareValue = Long.valueOf(address.getEventStartTime() * 1000);
                obj.setSortType(SortUtil.SORT_TYPE_DATE);
                compareableAddresses[i] = obj;
            }
        }
        
        SortAlgorithm.sort(compareableAddresses, sortType);
        
        long currentTimeStamp = System.currentTimeMillis();
        
        Vector events = new Vector();
        for (int i = 0; i < compareableAddresses.length; i++)
        {
            if (compareableAddresses[i] != null)
            {
                Address tempAddress = ((MyPlaceCompareableAddress) compareableAddresses[i]).address;
                
                if(isUpComingEvent(currentTimeStamp, tempAddress.getEventStartTime() * 1000))
                {
                    events.addElement(tempAddress);
                    elements.removeElement(tempAddress);
                }
                else
                {
                    break;
                }
            }
        }
        
        return events;
    }
    
    public boolean isUpComingEvent(long timeStampFrom, long timeStamp2To)
    {
        if (timeStamp2To <= 0)
        {
            return false;
        }
        
        return timeStamp2To >= timeStampFrom;
    }
    
    public boolean isToday(long timeStampFrom, long timeStamp2To)
    {
        Date dateFrom = new Date(timeStampFrom);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateFrom);
        int fromDayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
        int fromYear = calendar.get(Calendar.YEAR);

        Date dateTo = new Date(timeStamp2To);
        calendar.setTime(dateTo);
        int toDayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
        int toYear = calendar.get(Calendar.YEAR);
        return fromYear == toYear && fromDayOfYear == toDayOfYear;
    }
    
    public boolean isYesterDay(long timeStampFrom, long timeStamp2To)
    {
        Date dateFrom = new Date(timeStampFrom);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateFrom);
        int fromDayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
        int fromYear = calendar.get(Calendar.YEAR);
        int fromMaxiumDayOfYear = calendar.getActualMaximum(Calendar.DAY_OF_YEAR);

        Date dateTo = new Date(timeStamp2To);
        calendar.setTime(dateTo);
        int toDayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
        int toYear = calendar.get(Calendar.YEAR);
        int toMinimumDayOfYear = calendar.getActualMinimum(Calendar.DAY_OF_YEAR);
        return (fromYear == toYear && fromDayOfYear == toDayOfYear + 1)
                || (fromYear + 1 == toYear && fromDayOfYear == fromMaxiumDayOfYear && toDayOfYear == toMinimumDayOfYear);
    }
    
    public boolean isInWeek(long timeStampFrom, long timeStamp2To)
    {
        Date dateFrom = new Date(timeStampFrom);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateFrom);
        int fromWeekOfYear = calendar.get(Calendar.WEEK_OF_YEAR);
        int fromYear = calendar.get(Calendar.YEAR);

        Date dateTo = new Date(timeStamp2To);
        calendar.setTime(dateTo);
        int toWeekOfYear = calendar.get(Calendar.WEEK_OF_YEAR);
        int toYear = calendar.get(Calendar.YEAR);
        return fromYear == toYear && fromWeekOfYear == toWeekOfYear;
    }
    
    public boolean isInMonth(long timeStampFrom, long timeStamp2To)
    {
        Date dateFrom = new Date(timeStampFrom);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateFrom);
        int fromMonth = calendar.get(Calendar.MONTH);
        int fromYear = calendar.get(Calendar.YEAR);

        Date dateTo = new Date(timeStamp2To);
        calendar.setTime(dateTo);
        int toMonth = calendar.get(Calendar.MONTH);
        int toYear = calendar.get(Calendar.YEAR);
        return fromYear == toYear && fromMonth == toMonth;
    }

}
