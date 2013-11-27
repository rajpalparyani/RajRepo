/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * AddressFilter.java
 *
 */
package com.telenav.module.ac;

import java.util.Vector;

import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.datatypes.address.Address;
import com.telenav.data.datatypes.address.FavoriteCatalog;
import com.telenav.data.datatypes.address.Stop;
import com.telenav.res.IStringAc;
import com.telenav.res.ResourceManager;

/**
 *@author bduan
 *@date 2010-12-20
 */
public class AddressListFilter
{
    
    public static Vector getFilteredList(String prefix, Vector allAddressesList, boolean isNeedConsiderHomeWork)
    {
        return getFilteredList(prefix, allAddressesList, isNeedConsiderHomeWork, true);
    }
    /**
     * Filter a vector to find out all the suitable addresses or categories with the given prefix.
     * 
     * @param prefix a prefix of a string
     * @param allAddressesList a vector with all the addresses or the categories
     * @param isNeedConsiderHomeWork TODO
     * @return the matching addresses or categories in a vector
     */
    public static Vector getFilteredList(String prefix, Vector allAddressesList, boolean isNeedConsiderHomeWork, boolean isNeedCompareLabel)
    {
        if (allAddressesList == null || allAddressesList.size() == 0  )
            return new Vector();
        if( prefix == null || prefix.trim().length() == 0)
            return allAddressesList;

        Vector filterVector = new Vector();
        for (int i = 0; i < allAddressesList.size(); i++)
        {
            Object obj = allAddressesList.elementAt(i);
            if (obj instanceof Address)
            {
                Address address = (Address) obj;
                String str;
                if(isNeedCompareLabel)
                {
                    if(getLabel(address, isNeedConsiderHomeWork) != null)
                    {
                        str = getLabel(address, isNeedConsiderHomeWork);
                        if (isAcceptable(str, prefix))
                        {
                            filterVector.addElement(obj);
                        }
                        else
                        {
                            str = ResourceManager.getInstance().getStringConverter().convertAddress(address.getStop(), true);
                            if (isAcceptable(str, prefix))
                            {
                                filterVector.addElement(obj);
                            }
                        }
                    }
                    else
                    {
                        str = ResourceManager.getInstance().getStringConverter().convertAddress(address.getStop(), true);
                        if (isAcceptable(str, prefix))
                        {
                            filterVector.addElement(obj);
                        }
                    }
                }
                else
                {
                    str = ResourceManager.getInstance().getStringConverter().convertAddress(address.getStop(), true);
                    if (isAcceptable(str, prefix))
                    {
                        filterVector.addElement(obj);
                    }
                }
            }
            else if (obj instanceof FavoriteCatalog)
            {
                FavoriteCatalog catalog = (FavoriteCatalog) obj;
                String name = catalog.getName();
                if (isAcceptable(name, prefix))
                {
                    filterVector.addElement(obj);
                }
            }
            else if(obj instanceof String)
            {
                String str = (String) obj;
                if (isAcceptable(str, prefix))
                {
                    filterVector.addElement(str);
                }
            }
            else if(obj instanceof Stop)
            {
                Stop stop = (Stop) obj;
                String label = ResourceManager.getInstance().getStringConverter().convertAddress(stop, false);
                if (isAcceptable(label, prefix))
                {
                    filterVector.addElement(stop);
                }
            }
            else 
            {
                throw new IllegalArgumentException("Currently not support this kind of object: " + obj.getClass().getName());
            }
        }

        if (filterVector != null && filterVector.size() == 1 && filterVector.elementAt(0).toString().equalsIgnoreCase(prefix))
        {
            filterVector.removeAllElements();
        }

        return filterVector;
    }

    public static boolean isHome(Address address)
    {
        if(address == null || address.getStop() == null)
        {
            return false;
        }
        Stop home = DaoManager.getInstance().getAddressDao().getHomeAddress();
        
        if(address.getStop().equalsIgnoreCase(home))
        {
            return true;
        }
        
        return false;
    }
    
    public static boolean isWork(Address address)
    {
        if(address == null || address.getStop() == null)
        {
            return false;
        }
        
        Stop work = DaoManager.getInstance().getAddressDao().getOfficeAddress();
        
        if(address.getStop().equalsIgnoreCase(work))
        {
            return true;
        }
        
        return false;
    }
    
    protected static String getLabel(Address address, boolean isNeedConsiderHomeWork)
    {
        String label = "";
        
        if(address != null)
        {
            if(isNeedConsiderHomeWork)
            {
                String sHome = ResourceManager.getInstance().getCurrentBundle().getString(IStringAc.RES_BTTN_HOME, 
                    IStringAc.FAMILY_AC);
                
                String sWork = ResourceManager.getInstance().getCurrentBundle().getString(IStringAc.RES_BTN_WORK, 
                    IStringAc.FAMILY_AC);
                
                if(isHome(address))
                {
                    label = sHome;
                }
                else if(isWork(address))
                {
                    label = sWork;
                }
                else
                {
                    label = address.getLabel();
                }
            }
            else
            {
                label = address.getLabel();
            }
        }
        
        return label;
    }
    
    /**
     * Return true if the given string has same prefix, otherwise false.
     * 
     * @param label a given string 
     * @param prefix prefix of a string
     * @return true if the given string has same prefix, otherwise false
     */
    protected static boolean isAcceptable(String label, String prefix)
    {
        if (prefix == null || prefix.trim().length() == 0)
        {
            return true;
        }
        else
        {
            if (label != null && label.length() != 0)
            {
                label = label.toUpperCase();
                prefix = prefix.trim().toUpperCase();

                int prefixIndex = label.indexOf(prefix);
                if (prefixIndex != -1)
                {
                    return true;
                }
            }
        }
        return false;
    }
}
