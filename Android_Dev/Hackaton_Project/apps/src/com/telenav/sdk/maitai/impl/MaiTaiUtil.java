/**
 * 
 */
package com.telenav.sdk.maitai.impl;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.telenav.logger.Logger;
import com.telenav.util.URLDecoder;

/**
 * @author xinrongl
 *
 */
public class MaiTaiUtil
{
    private static MaiTaiUtil instance;
    
    public static MaiTaiUtil getInstance()
    {
        if (instance == null)
        {
            instance = new MaiTaiUtil();
        }
        return instance;
    }
    
    public static Vector parseRequestUri(String requestUri)
    {
        Vector list = new Vector();
        
        Hashtable<String, String> table = new Hashtable<String, String>();
        
        if (requestUri.indexOf("&amp;") >= 0)
        {
            requestUri = requestUri.replaceAll("&amp;", "&");
        }
        
        int schemeIndex = requestUri.indexOf("://");
        if (schemeIndex == -1)
            schemeIndex = requestUri.indexOf(":");
        if (schemeIndex == -1)
            return list;
        String scheme = requestUri.substring(0, schemeIndex);
        scheme = decoding(scheme);
        if(scheme != null)
        {
            table.put("scheme", scheme);
        }
        
        int actionIndex = requestUri.indexOf('?', schemeIndex);
        String action = "";
        if (actionIndex != -1)
        {
            action = requestUri.substring(schemeIndex + 3, actionIndex);
        }
        else
        {
            actionIndex = schemeIndex;
        }
        action = decoding(action);
        if (action != null)
        {
            table.put("action", action);
        }
        
        String query = requestUri.substring(actionIndex + 1);
        String pair = null;
        int index = 0;
        while (index != -1 && index < query.length() - 1)
        {
            int pairIndex = query.indexOf('&', index);
            if (pairIndex == -1)
            {
                pair = query.substring(index);
                index = query.length();
            }
            else
            {
                pair = query.substring(index, pairIndex);
                index = pairIndex + 1;
            }
            pair = decoding(pair);
            if (pair != null)
            {
                int equalIndex = pair.indexOf('=');
                if (equalIndex == -1) continue;
                String key = pair.substring(0, equalIndex).trim();
                String val = pair.substring(equalIndex + 1).trim();
                table.put(key, val);
            }
        }
        
        Enumeration<String> emun = table.keys();
        
        while(emun.hasMoreElements())
        {
            String key = emun.nextElement();
            String value = table.get(key);
            
            list.addElement(new String[] {key, value});
        }
                
        return list;
    }
    
    private static String decoding(String str)
    {
        String decodeStr = null;
        try
        {
            decodeStr = URLDecoder.decode(str, "UTF-8");
        }
        catch(Exception e)
        {
            Logger.log("MaiTaiUtil", e);
        }
        return decodeStr;
    }
    
    public String getValue(Vector list, String key)
    {
        int size = list == null ? 0 : list.size();
        
        for (int i = 0; i < size; i ++)
        {
            String[] pair = (String[])list.elementAt(i);
            if (key != null && key.equals(pair[0]))
                return pair[1];
        }
        
        return null;
    }
    
    public Vector getValues(Vector list, String key)
    {
        int size = list == null ? 0 : list.size();
        
        Vector v = new Vector();
        for (int i = 0; i < size; i ++)
        {
            String[] pair = (String[])list.elementAt(i);
            if (key != null && key.equals(pair[0]))
                v.addElement(pair[1]);
        }
        
        return v;
    }
    
    public String parseLocation(String str)
    {
        Pattern p = Pattern.compile("-?\\d+.\\d+(,|\\+)-?\\d+.\\d+");
        Matcher m = p.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (m.find()) 
        {
            sb.append(m.group());
        }
        
        if (sb.length() == 0)
        {
            if (str.indexOf("|") != -1) 
            {
                String[] strs = str.split("\\|");
                for(int i = 0; i < strs.length; i ++)
                {
                    if (strs[i].indexOf(":") == -1 && !strs[i].equals(""))
                    {
                        return strs[i];
                    }
                }
            } 
            else if (str.indexOf("%7C") != -1)
            {
                String[] strs = str.split("%7C");
                for(int i = 0; i < strs.length; i ++)
                {
                    if (strs[i].indexOf(":") == -1 && !strs[i].equals(""))
                    {
                        return strs[i];
                    }
                }
            }
        }
        return sb.toString();
    }
}
