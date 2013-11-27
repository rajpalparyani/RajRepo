/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * TnProperties.java
 *
 */
package com.telenav.io;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;

/**
 * A Properties object is a Hashtable where the keys and values must be Strings. Each property can have a default
 * Properties list which specifies the default values to be used when a given key is not found in this Properties
 * instance.
 * 
 *@author fqming (fqming@telenav.cn)
 *@date Jul 13, 2010
 */
public interface TnProperties
{
    /**
     * Loads properties from the specified InputStream. The encoding is ISO8859-1. The Properties file is interpreted
     * according to the following rules:
     * 
     * Empty lines are ignored. 
     * Lines starting with either a "#" or a "!" are comment lines and are ignored. 
     * A backslash at the end of the line escapes the following newline character ("\r", "\n", "\r\n"). If there's a whitespace
     * after the backslash it will just escape that whitespace instead of concatenating the lines. This does not apply
     * to comment lines. 
     * A property line consists of the key, the space between the key and the value, and the value.
     * The key goes up to the first whitespace, "=" or ":" that is not escaped. The space between the key and the value
     * contains either one whitespace, one "=" or one ":" and any number of additional whitespaces before and after that
     * character. The value starts with the first character after the space between the key and the value. 
     * Following
     * escape sequences are recognized: "\ ", "\\", "\r", "\n", "\!", "\#", "\t", "\b", "\f", and "\\uXXXX" (unicode
     * character).
     * 
     * @param is the inputstream.
     */
    public void load(InputStream is) throws IOException;

    /**
     * Searches for the property with the specified name. If the property is not found, the default Properties are
     * checked. If the property is not found in the default Properties, null is returned.
     * 
     * @param name the name of the property to find.
     * @return the named property value, or null if it can't be found.
     */
    public String getProperty(String name);

    /**
     * Returns all of the property names that this Properties object contains.
     * 
     * @return an Enumeration containing the names of all properties that this Properties object contains. 
     */
    public Enumeration propertyNames();

    /**
     *    Retrieves the number of keys in this Properties.
     * @return
     */
    public int size();
}
