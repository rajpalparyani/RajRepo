/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * PropertiesReader.java
 *
 */
package com.telenav.tools.i18n2excelproperties;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.TreeMap;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2011-5-4
 */
public class PropertiesReader
{
    /**
     * 
     * @param propertiesPath
     * 
     * @return <file, TreeMap<locale, TreeMap<key, string>>>
     * 
     * @throws IOException
     */
    public static TreeMap<String, TreeMap<String, TreeMap<String, String>>> readProperties(String propertiesPath) throws IOException
    {
        if (propertiesPath == null || propertiesPath.trim().length() == 0)
            return null;

        if (!propertiesPath.endsWith("\\i18n"))
            throw new IllegalArgumentException("properties path should be a i18n directory.");

        TreeMap<String, TreeMap<String, TreeMap<String, String>>> propertiesMaps = new TreeMap<String, TreeMap<String, TreeMap<String, String>>>();

        File file = new File(propertiesPath);
        File[] files = file.listFiles();

        for (int i = 0; i < files.length; i++)
        {
            File localeDir = files[i];
            if (!I18n2ExcelProperties.supportLocale(localeDir.getName()))
            {
                continue;
            }

            File[] stringFiles = localeDir.listFiles(new FilenameFilter()
            {
                public boolean accept(File dir, String name)
                {
                    if (name.endsWith("strings"))
                        return true;
                    return false;
                }
            });
            if (stringFiles.length == 0)
                continue;

            File[] propertyFiles = stringFiles[0].listFiles(new FilenameFilter()
            {
                public boolean accept(File dir, String name)
                {
                    if (name.endsWith("properties"))
                        return true;
                    return false;
                }
            });

            for (int j = 0; j < propertyFiles.length; j++)
            {
                PropertiesMap properties = new PropertiesMap();
                properties.load(new FileInputStream(propertyFiles[j]));
                String fileName = propertyFiles[j].getName();
                String key = fileName.substring(0, fileName.lastIndexOf("."));
                TreeMap<String, TreeMap<String, String>> localeMaps;
                if (propertiesMaps.containsKey(key))
                {
                    localeMaps = propertiesMaps.get(key);
                }
                else
                {
                    localeMaps = new TreeMap<String, TreeMap<String, String>>();
                    propertiesMaps.put(key, localeMaps);
                }
                localeMaps.put(localeDir.getName(), properties);
            }
        }
        
        return propertiesMaps;
    }
}
