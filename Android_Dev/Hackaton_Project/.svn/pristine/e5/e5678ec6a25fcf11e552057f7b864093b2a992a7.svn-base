/**
 *
 * Copyright 2013 TeleNav, Inc. All rights reserved.
 * GrouppedCompareableAddress.java
 *
 */
package com.telenav.data.sort.util;

import com.telenav.sort.IComparable;
import com.telenav.data.datatypes.address.Address;

/**
 *@author yren
 *@date 2013-3-11
 */
public class MyPlaceCompareableAddress implements IComparable
{
    String groupName;
    Address address = null;
    Object compareValue;
    int sortType = -1;
    
    public MyPlaceCompareableAddress(Address address)
    {
        this.address = address;
    }
    
    public MyPlaceCompareableAddress(String groupName)
    {
        this.groupName = groupName;
    }
    
    public void setSortType(int sortType)
    {
        this.sortType = sortType;
    }
    
    public int compareTo(Object another)
    {
        if (this.compareValue.getClass() != ((MyPlaceCompareableAddress) another).compareValue.getClass())
        {
            throw new ClassCastException("Cannot compare!");
        }

        // timestamp
        if (this.sortType == SortUtil.SORT_TYPE_DATE)
        {
            Long thisCompareValue = (Long) this.compareValue;
            Long anotherCompareValue = (Long) (((MyPlaceCompareableAddress) another).compareValue);
            if (thisCompareValue.compareTo(anotherCompareValue) == 1)
            {
                return -1;
            }
            else if (thisCompareValue.compareTo(anotherCompareValue) == -1)
            {
                return 1;
            }
            else
            {
                return 0;
            }
        }
        // distance
        else if (this.sortType == SortUtil.SORT_TYPE_DISTANCE)
        {
            Integer thisCompareValue = (Integer) this.compareValue;
            Integer anotherCompareValue = (Integer) (((MyPlaceCompareableAddress) another).compareValue);
            return thisCompareValue.compareTo(anotherCompareValue);
        }
        // name
        else if (this.sortType == SortUtil.SORT_TYPE_ALPHABET)
        {
            String thisCompareValue = (String) (this.compareValue);
            String anotherCompareValue = (String) (((MyPlaceCompareableAddress) another).compareValue);
            if (thisCompareValue.length() == 0)
            {
                thisCompareValue = " ";
            }

            if (anotherCompareValue.length() == 0)
            {
                anotherCompareValue = " ";
            }

            char c1 = thisCompareValue.charAt(0);
            char c2 = anotherCompareValue.charAt(0);
            if (Character.isLetter(c1) && !Character.isLetter(c2))
            {
                return -1;
            }
            if (!Character.isLetter(c1) && Character.isLetter(c2))
            {
                return 1;
            }
            return thisCompareValue.compareTo(anotherCompareValue);
        }
        else
        {
            return 0;
        }
    }

}
