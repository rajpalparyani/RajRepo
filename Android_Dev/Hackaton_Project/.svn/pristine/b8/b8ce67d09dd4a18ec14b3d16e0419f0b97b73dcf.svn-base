/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * I18n2ExcelProperties.java
 *
 */
package com.telenav.tools.i18n2excelproperties;

import java.io.IOException;
import java.util.StringTokenizer;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.Map.Entry;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2011-5-4
 */
public class I18n2ExcelProperties
{
    public final static String FORMAT_EXCEL = "excel";
    public final static String FORMAT_PROPERTIES = "properties";
    
    private static String[] locales = new String[]{"en_US", "es_MX"};
    
    private String excelPath;

    private String propertiesPath;

    private String outputPath;
    
    private String outputFormat;
    
    public static void main(String[] args) throws IOException
    {
    	if(args[4].indexOf("${") == -1 )
    	{
    		StringTokenizer st = new StringTokenizer(args[4], "-");
    		
    		locales = new String[st.countTokens()];
    		int i = 0;
    		while(st.hasMoreTokens())
    		{
    			locales[i] = st.nextToken();
    			i++;
    		}
   	}
        I18n2ExcelProperties properties = new I18n2ExcelProperties(args[0], args[1], args[2], args[3]);
		properties.execute();
    }
    
    public I18n2ExcelProperties(String excelPath, String propertiesPath, String outputPath, String outputFormat)
    {
        this.excelPath = excelPath.indexOf("${") != -1 ? "" : excelPath;
        this.propertiesPath = propertiesPath.indexOf("${") != -1 ? "" : propertiesPath;
        this.outputPath = outputPath.indexOf("${") != -1 ? "" : outputPath;
        this.outputFormat = outputFormat.indexOf("${") != -1 ? "" : outputFormat;
		System.out.println("excelPath:" + this.excelPath);
		System.out.println("propertiesPath:" + this.propertiesPath);
		System.out.println("outputPath:" + this.outputPath);
		System.out.println("outputFormat:" + this.outputFormat);
		for(int i = 0; i< locales.length; i++)
		{
		    System.out.println("locales:" + this.locales[i]);
		}
    }

    public void execute() throws IOException
    {
        //<file, TreeMap<locale, TreeMap<key, string>>>
        TreeMap<String, TreeMap<String, TreeMap<String, String>>> resultMap = null;
        TreeMap<String, TreeMap<String, TreeMap<String, String>>> propertiesMap = PropertiesReader.readProperties(propertiesPath);
        if (propertiesMap != null && propertiesMap.size() > 0)
        {
            resultMap = propertiesMap;
        }
        TreeMap<String, TreeMap<String, TreeMap<String, String>>> excelMap = ExcelReader.readExcel(this.excelPath);
        
        //merge excel & property file
        if(excelMap != null && excelMap.size() > 0)
        {
            if(resultMap == null || resultMap.size() == 0)
            {
                resultMap = excelMap;
            }
            else
            {
                System.out.println("---------------------start merge---------------------");
                Iterator<Entry<String, TreeMap<String, TreeMap<String, String>>>> resultIterator = resultMap.entrySet().iterator();
                while(resultIterator.hasNext())
                {
                    Entry<String, TreeMap<String, TreeMap<String, String>>> resultEntry = resultIterator.next();
                    String fileKey = resultEntry.getKey();
                    System.out.println("---------Begin file Name: " + fileKey);
                    //<locale, TreeMap<key, string>>
                    TreeMap<String, TreeMap<String, String>> resultLocaleMap = resultEntry.getValue();
                    Iterator<Entry<String, TreeMap<String, String>>> resultLocaleIterator = resultLocaleMap.entrySet().iterator();
                    if(excelMap.containsKey(fileKey))
                    {
                        //<locale, TreeMap<key, string>>
                        TreeMap<String, TreeMap<String, String>> localeMap = excelMap.get(fileKey);
                        
                        while(resultLocaleIterator.hasNext())
                        {
                            Entry<String, TreeMap<String, String>> resultLocaleEntry = resultLocaleIterator.next();
                            TreeMap<String, String> resultPropertyMap = resultLocaleEntry.getValue();
                            String locale = resultLocaleEntry.getKey();
                            if(localeMap.containsKey(locale))
                            {
                                TreeMap<String, String> propertyMap = localeMap.get(locale);
                                Iterator<Entry<String, String>> resultPropertyIerator = resultPropertyMap.entrySet().iterator();
                                while(resultPropertyIerator.hasNext())
                                {
                                    Entry<String, String> resultPropertyEntry = resultPropertyIerator.next();
                                    String resultKey = resultPropertyEntry.getKey();
                                    if(propertyMap.containsKey(resultKey))
                                    {
                                        String property = propertyMap.get(resultKey);
                                        if(property != null && property.trim().length() > 0)
                                        {
                                            if(getMainLocale().equals(locale))//print changed value for main locale
                                            {
                                                if (!property.equals(resultPropertyEntry.getValue()))
                                                {
                                                    System.out.println("Id: " + resultKey + ", Old Value: " + resultPropertyEntry.getValue() + ", New Value: " + property);
                                                }
                                            }
                                            property = property.trim();
                                            resultPropertyEntry.setValue(property);
                                        }
                                    }
                                }
                            }
                        }
                    }
                    System.out.println("---------End file Name: " + fileKey);
                }
                
                System.out.println("---------------------end merge---------------------");
            }
        }
        
        if (FORMAT_EXCEL.equals(this.outputFormat))
        {
            ExcelWriter.writeExcel(resultMap, outputPath);
        }
        else if (FORMAT_PROPERTIES.equals(this.outputFormat))
        {
            PropertiesWriter.writeProperties(resultMap, outputPath);
        }
    }

    public static boolean supportLocale(String locale)
    {
        for(int i = 0; i < locales.length; i++)
        {
            if(locales[i].equals(locale))
                return true;
        }
        
        return false;
    }
    
    public static String getMainLocale()
    {
        return locales[0];
    }
}
