/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * PropertiesWriter.java
 *
 */
package com.telenav.tools.i18n2excelproperties;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.Map.Entry;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2011-5-4
 */
public class PropertiesWriter
{
    public static void writeProperties(TreeMap<String, TreeMap<String, TreeMap<String, String>>> maps, String outputFilePath) throws IOException
    {
        Iterator<Entry<String, TreeMap<String, TreeMap<String, String>>>> entrySet = maps.entrySet().iterator();
        while (entrySet.hasNext())
        {
            Entry<String, TreeMap<String, TreeMap<String, String>>> entry = entrySet.next();
            String propertiesName = entry.getKey();
            TreeMap<String, TreeMap<String, String>> localeMap = entry.getValue();
            Iterator<Entry<String, TreeMap<String, String>>> localeIterator = localeMap.entrySet().iterator();
            while (localeIterator.hasNext())
            {
                Entry<String, TreeMap<String, String>> localeEntry = localeIterator.next();
                String locale = localeEntry.getKey();
                TreeMap<String, String> map = localeEntry.getValue();
                StringBuffer sb = new StringBuffer();
                Iterator<Entry<String, String>> propertyIterator = map.entrySet().iterator();
                while (propertyIterator.hasNext())
                {
                    Entry<String, String> propertyEntry = propertyIterator.next();
                    sb.append(propertyEntry.getKey());
                    sb.append('=');
                    sb.append(propertyEntry.getValue());
                    sb.append(System.getProperty("line.separator"));
                }
                
                String path = outputFilePath + "\\" + locale + "\\strings";
                File propertyFile = new File(path);
                propertyFile.mkdirs();
                path += "\\" + propertiesName + ".properties";
                propertyFile = new File(path);
                propertyFile.createNewFile();
                writeFile(sb, propertyFile);
            }
        }
    }

    private static void writeFile(StringBuffer stringBuffer, File file)
    {
        Writer writer = null;
        try
        {
			writer = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
            writer.write(stringBuffer.toString(), 0, stringBuffer.length());
            writer.flush();

            writer.close();
            writer = null;
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (writer != null)
            {
                try
                {
                    writer.close();
                }
                catch (IOException e)
                {
                }
            }
        }
    }
}
