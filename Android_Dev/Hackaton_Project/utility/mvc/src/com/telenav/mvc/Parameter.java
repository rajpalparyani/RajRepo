package com.telenav.mvc;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

/**
 * 
 * Parameter is a data container.<br>
 * 
 * Q: What is the difference between fetch and get?<br>
 * 
 * A: Fetch means query and remove. Get means only query, the container does not changed.<br>
 * 
 * @author zhdong
 * 
 */
public class Parameter
{

    /**
     * The data content
     */
    private Hashtable content = new Hashtable();

    /**
     * Get the value according to the key
     * 
     * @param key
     * @return value
     */
    public synchronized Object get(Integer key)
    {
        if (key == null)
            return null;
        return content.get(key);
    }

    /**
     * Get the value according to the key and then remove it.
     * 
     * @param key
     * @return value
     */
    public synchronized Object fetch(Integer key)
    {
        if (key == null)
            return null;

        Object value = content.get(key);
        content.remove(key);
        return value;
    }

    /**
     * Get the boolean value according to the key
     * 
     * @param key
     * @return value
     */
    public synchronized boolean getBool(Integer key)
    {
        Object o = get(key);
        if (o != null && o instanceof Boolean)
        {
            Boolean b = (Boolean)o;
            return b.booleanValue();
        }

        return false;
    }
    
    /**
     *  return default value if key does not exist  
     * 
     * @param key
     * @param defaultValue
     */
    public synchronized boolean optBool(Integer key, boolean defaultValue)
    {
        Object o = get(key);
        if (o != null && o instanceof Boolean)
        {
            Boolean b = (Boolean)o;
            return b.booleanValue();
        }

        return defaultValue;
    }

    /**
     * Get the boolean value according to the key and then remove it.
     * 
     * @param key
     * @return value
     */
    public synchronized boolean fetchBool(Integer key)
    {
        Object o = fetch(key);
        if (o != null && o instanceof Boolean)
        {
            Boolean b = (Boolean)o;
            return b.booleanValue();
        }

        return false;
    }

    /**
     * Get the string value according to the key
     * 
     * @param key
     * @return value
     */
    public synchronized String getString(Integer key)
    {
        Object o = get(key);
        if (o != null && o instanceof String)
            return (String) o;

        return null;
    }
    
    /**
     *  return "" if key does not exist  
     * 
     * @param key
     */
    public synchronized String optString(Integer key)
    {
        return optString(key,"");
    } 

    /**
     *  return default value if key does not exist  
     * 
     * @param key
     * @param defaultValue
     */
    public synchronized String optString(Integer key, String defaultValue)
    {
        Object o = get(key);
        if (o != null && o instanceof String)
            return (String) o;

        return defaultValue;
    }
    
    /**
     * Get the string value according to the key and then remove it.
     * 
     * @param key
     * @return value
     */
    public synchronized String fetchString(Integer key)
    {
        Object o = fetch(key);
        if (o != null && o instanceof String)
            return (String) o;

        return null;
    }

    /**
     * Get the int value according to the key.
     * 
     * NOTE: if the key does not exist, it will return -1.
     * 
     * @param key
     * @return value
     */
    public synchronized int getInt(Integer key)
    {
        Object o = get(key);
        if (o != null)
        {
            if (o instanceof Integer)
            {
                Integer i = (Integer) o;
                return i.intValue();
            }
            else if (o instanceof String)
            {
                return Integer.parseInt(o.toString());
            }
        }

        return -1;
    }
    
    public synchronized int optInt(Integer key, int defaultValue)
    {
        Object o = get(key);
        if (o != null && o instanceof Integer)
        {
            Integer i = (Integer)o;
            return i.intValue();
        }

        return defaultValue;
    }
    
    /**
     * Get the int value according to the key and then remove it.
     * 
     * NOTE: if the key does not exist, it will return -1.
     * 
     * @param key
     * @return value
     */
    public synchronized int fetchInt(Integer key)
    {
        Object o = fetch(key);
        if (o != null && o instanceof Integer)
        {
            Integer i = (Integer)o;
            return i.intValue();
        }

        return -1;
    }
    
    

    /**
     * Get the vector value according to the key.
     * 
     * @param key
     * @return value
     */
    public synchronized Vector getVector(Integer key)
    {
        Object o = get(key);
        if (o != null && o instanceof Vector)
            return (Vector) o;

        return new Vector();
    }
    
    
    /**
     * Get the vector value according to the key and then remove it.
     * 
     * @param key
     * @return value
     */
    public synchronized Vector fetchVector(Integer key)
    {
        Object o = fetch(key);
        if (o != null && o instanceof Vector)
            return (Vector) o;

        return new Vector();
    }   
    

    /**
     * Put the key-value pair
     * 
     * @param key
     * @param value
     */
    public synchronized void put(Integer key, Object value)
    {
        if (key == null)
            return;

        if (value == null)
        {
            remove(key);
            return;
        }

        content.put(key, value);
    }

    /**
     * Put the key with an int value
     * 
     * @param key
     * @param value
     */
    public synchronized void put(Integer key, int value)
    {
        Integer i = new Integer(value);        
        put(key, i);
    }

    /**
     * Put the key with a boolean value.
     * 
     * @param key
     * @param value
     */
    public synchronized void put(Integer key, boolean value)
    {
        Boolean b = new Boolean(value);
        put(key, b);
    }

    /**
     * Remove a key-value pair according to the key, do nothing if doesn't find the key
     * 
     * @param key
     */
    public synchronized void remove(Integer key)
    {
        if (key == null)
            return;
        content.remove(key);
    }

    /**
     * Remove all
     */
    public synchronized void removeAll()
    {
        content.clear();
    }
    
    /**
     * To string format.
     */
    public String toString()
    {
        Enumeration en = content.keys();
        StringBuffer sb = new StringBuffer();
        while (en.hasMoreElements())
        {
            Object key = en.nextElement();
            Object value = content.get(key);
            
            if (value == this)
            {
                // ignore itself to avoid StackOverflowError
                continue;
            }
            
            sb.append('(');
            sb.append(key);
            sb.append(',');
            sb.append(value);
            sb.append(')');

        }
        return sb.toString();
    }
    
    /**
     * Copy given data to current Parameter. If same key exist, override it.
     * 
     * @param parameter
     */
    public synchronized void add(Parameter parameter)
    {
        if (parameter == null)
        {
            return;
        }

        Enumeration en = parameter.content.keys();

        while (en.hasMoreElements())
        {
            Integer key = (Integer)en.nextElement();
            content.put(key, parameter.get(key));
        }
    }
    
    

    /**  Main function for test.
    public static void main(String[] args)
    {
        Parameter p = new Parameter();

        Object key = new Object();
        Object value = "123456";

        System.out.println("put(key, value); key=" + key + ",value=" + value);
        p.put(key, value);
        System.out.println("p.get(key)=" + p.get(key));
        System.out.println("p.fetch(key)=" + p.fetch(key));
        System.out.println("p.get(key)=" + p.get(key));
        
        value = "666666";

        System.out.println("put(key, value); key=" + key + ",value=" + value);
        p.put(key, value);
        System.out.println("p.fetch(key)=" + p.fetch(key));
        System.out.println("p.get(key)=" + p.get(key));
        
        
        p.put("p1_key", "p1_value");
        p.put("p_key", "p_value_p1");
        
        Parameter p2 = new Parameter();
        p2.put("p2_key", "p2_value");
        p2.put("p_key", "p_value_p2");
        
        p2.add(p);
        
        System.out.println(p2);
    }
    **/
}
