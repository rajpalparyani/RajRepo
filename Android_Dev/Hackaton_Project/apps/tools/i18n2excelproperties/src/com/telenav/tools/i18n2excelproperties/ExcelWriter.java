/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * ExcelWriter.java
 *
 */
package com.telenav.tools.i18n2excelproperties;

import java.awt.Color;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2011-5-4
 */
public class ExcelWriter
{
    public static void writeExcel(TreeMap<String, TreeMap<String, TreeMap<String, String>>> maps, String outputFilePath) throws IOException
    {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFCellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFillForegroundColor(new XSSFColor(new Color(228, 232, 243)));
        headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        XSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setWrapText(true);
        XSSFCellStyle sameCellStyle = workbook.createCellStyle();
        sameCellStyle.setFillForegroundColor(new XSSFColor(new Color(128, 128, 128)));
        sameCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        sameCellStyle.setWrapText(true);
        XSSFCellStyle emptyCellStyle = workbook.createCellStyle();
        emptyCellStyle.setFillForegroundColor(new XSSFColor(new Color(255, 0, 0)));
        emptyCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        Iterator<Entry<String, TreeMap<String, TreeMap<String, String>>>> entrySet = maps.entrySet().iterator();
        
        while(entrySet.hasNext())
        {
            Entry<String, TreeMap<String, TreeMap<String, String>>> entry = entrySet.next();
            String sheetName = entry.getKey();
            XSSFSheet sheet = workbook.createSheet(sheetName);
            TreeMap<String, TreeMap<String, String>> localeMap = entry.getValue();
            Iterator<String> localeIterator = localeMap.keySet().iterator();
            
            int rowCount = 0;
            XSSFRow firstRow = sheet.createRow(rowCount);
            
            createFirstRow(firstRow, localeIterator, headerCellStyle);
            
            TreeMap<String, String> mainLocale = localeMap.get(I18n2ExcelProperties.getMainLocale());
            for(int i = 0; i < localeMap.size() + 1; i++)
            {
                if(i == 0)
                {
                    sheet.setColumnWidth(i, 2500);
                }
                else
                {
                    sheet.setColumnWidth(i, 15000);
                }
            }
            Iterator<Entry<String, String>> propertySet = mainLocale.entrySet().iterator();
            while(propertySet.hasNext())
            {
                rowCount++;
                Entry<String, String> propertyEntry = propertySet.next();
                XSSFRow propertyRow = sheet.createRow(rowCount);
                XSSFCell idcell = propertyRow.createCell(0);
                idcell.setCellValue(propertyEntry.getKey());
                XSSFCell valuecell = propertyRow.createCell(1);
                valuecell.setCellStyle(cellStyle);
                String localeStr = propertyEntry.getValue();
                localeStr = convertString(localeStr);
                valuecell.setCellValue(localeStr);
            }
            
            for(int i = 1; i < rowCount + 1; i++)
            {
                XSSFRow row = sheet.getRow(i);
                String id = row.getCell(0).getStringCellValue();
                for(int j = 1; j < firstRow.getPhysicalNumberOfCells(); j++)
                {
                    String locale = firstRow.getCell(j).getStringCellValue();
                    if(locale != null)
                    {
                        TreeMap<String, String> localePropertyMap = localeMap.get(locale);
                        if(localePropertyMap.containsKey(id))
                        {
                            String localeStr = localePropertyMap.get(id);
                            localeStr = convertString(localeStr);
                            XSSFCell valuecell = row.createCell(j);
                            valuecell.setCellStyle(cellStyle);
                            if(!locale.equals(I18n2ExcelProperties.getMainLocale()) && mainLocale != null && mainLocale.containsKey(id))
                            {
                                String mainStr = mainLocale.get(id);
                                if(localeStr.equals(mainStr))
                                {
                                    valuecell.setCellStyle(sameCellStyle);
                                }
                            }
                            valuecell.setCellValue(localeStr);
                        }
                        else
                        {
                            XSSFCell valuecell = row.createCell(j);
                            valuecell.setCellStyle(emptyCellStyle);
                        }
                    }
                }
            }
        }
        
        FileOutputStream out = new FileOutputStream(outputFilePath);
        workbook.write(out);
        out.close();
    }
    
    private static String convertString(String str)
    {
        if(str.indexOf("\n") != -1)
        {
            str = str.replaceAll("\n", "\\\\n");
        }
        
        return str;
    }
    
    private static void createFirstRow(XSSFRow firstRow, Iterator<String> localeIterator, XSSFCellStyle headerCellStyle)
    {
        int i = 0;
        XSSFCell cell = firstRow.createCell(i);
        cell.setCellValue("ID");
        cell.setCellStyle(headerCellStyle);
        while(localeIterator.hasNext())
        {
            i++;
            String locale = localeIterator.next();
            cell = firstRow.createCell(i);
            cell.setCellValue(locale);
            cell.setCellStyle(headerCellStyle);
        }
    }
}
