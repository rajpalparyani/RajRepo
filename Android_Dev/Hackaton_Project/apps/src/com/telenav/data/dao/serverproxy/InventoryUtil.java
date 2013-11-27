/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * InventoryUtil.java
 *
 */
package com.telenav.data.dao.serverproxy;

import java.util.Enumeration;

import com.telenav.persistent.TnStore;

/**
 *@author fqming (fqming@telenav.cn)
 *@date Jul 19, 2010
 */
abstract class InventoryUtil
{
    public static String getInventory(TnStore store)
    {
        if(store == null)
            return "0";
        
        String strInventory = "";
        
        boolean hasData = false;
        int[] inventory = new int[store.size()];
        Enumeration keys = store.keys();
        if (keys != null)
        {
            while (keys.hasMoreElements())
            {
                String keyStr = (String) keys.nextElement();
                int key = Integer.parseInt(keyStr);
                Object obj = store.get(key);
                if(obj != null)
                {
                    byte[] buf = (byte[]) obj;
                    inventory = setInt(inventory, buf.length, key);
                    hasData = true;
                }
            }
        }
        
        if(!hasData)
        {
            return "0";
        }
        strInventory = createInventoryString(inventory);
        return strInventory;
    }
    
    /** append a int into int array, checking for overflow */
    private static int[] setInt(int[] buff, int b, int counter)
    {
        // is it necessary to do dynamic size up here?
        // double (*2) is not good enough, what if initial size is 1 and counter is 10? one double not enough
        // recursive double sounds expensive
        // temp solution: enlarge till enough * 2

        if (counter >= buff.length)
        {
            // old way
            // int[] newBuff = new int[2 * buff.length];
            int[] newBuff = new int[2 * counter];
            System.arraycopy(buff, 0, newBuff, 0, buff.length);
            buff = newBuff;
        }

        buff[counter] = b;
        return buff;
    }
    
    /** create compressed inventory from the detailed one */
    private static String createInventoryString(int [] inventory)
    {
        int [] compressedInfo = createInventoryInt(inventory);
        long totalSize = 0;

        for (int index = 0; index < inventory.length; index++)
        {
            totalSize += inventory[index];
        }

        return convertInventoryToString(compressedInfo, totalSize);
    }
    
    /** create compressed inventory from the detailed one */
    private static int[] createInventoryInt(int [] inventory)
    {
        int [] compressedInfo = null;
//          long totalSize = 0;

        if (inventory != null && inventory.length > 0)
        {
            compressedInfo = new int[(inventory.length >> 5) + 1];

            for (int index = 0; index < inventory.length; index++)
            {
                if (inventory[index] != 0)
                {
                    int infoIndex = index >> 5;

                    // should do 'or', not 'and'
                    compressedInfo[ infoIndex ] |= (1 << (index & 0x1f));
                }
            }
        }

        return compressedInfo;
    }
    
    /** convert inventory from the int[] to String notation */
    private static String convertInventoryToString(int [] compressedInfo, long totalSize)
    {
        StringBuffer buf = new StringBuffer();
        for (int index = 0; index < compressedInfo.length; index++)
        {
            buf.append(compressedInfo[index]);
            buf.append(",");
        }

        buf.append(totalSize);  // no comma "," following should be good for convert...

        return buf.toString();
    }
}
