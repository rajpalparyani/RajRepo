/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * ExcelReader.java
 *
 */
package com.telenav.tools.i18n2excelproperties;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.TreeMap;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2011-5-4
 */
public class ExcelReader
{
    /**
     * 
     * @param excelFileName
     * 
     * @return <file, TreeMap<locale, TreeMap<key, string>>>
     * 
     * @throws IOException
     */
    public static TreeMap<String, TreeMap<String, TreeMap<String, String>>> readExcel(String excelFileName) throws IOException
    {
        if(excelFileName == null || excelFileName.trim().length() == 0)
            return null;
        
        XSSFWorkbook workbook = null;
        try
        {
            workbook = new XSSFWorkbook(new FileInputStream(excelFileName));
        }
        catch(Exception e)
        {
            return null;
        }
        
        if(workbook == null)
            return null;
        
        TreeMap<String, TreeMap<String, TreeMap<String, String>>> propertiesMaps = new TreeMap<String, TreeMap<String, TreeMap<String, String>>>();
        int sheetNumber = workbook.getNumberOfSheets();
        for (int i = 0; i < sheetNumber; i++)
        {
            XSSFSheet sheet = workbook.getSheetAt(i);
            XSSFRow firstRow = sheet.getRow(0);
            String sheetName = sheet.getSheetName();
            
            TreeMap<String, TreeMap<String, String>> localeMaps;
            if (propertiesMaps.containsKey(sheetName))
            {
                localeMaps = propertiesMaps.get(sheetName);
            }
            else
            {
                localeMaps = new TreeMap<String, TreeMap<String, String>>();
                propertiesMaps.put(sheetName, localeMaps);
            }
            for(int j = 1; j < firstRow.getPhysicalNumberOfCells(); j++)
            {
                XSSFCell localeCell = firstRow.getCell(j);
                String locale = localeCell.getStringCellValue();
                TreeMap<String, String> propertyMap = new TreeMap<String, String>(); 
                localeMaps.put(locale, propertyMap);
                for(int m = 1; m < sheet.getPhysicalNumberOfRows(); m++)
                {
                    XSSFRow row = sheet.getRow(m);
                    if(row.getCell(0) == null)
                    	continue;	
					String id = "";
					XSSFCell tempCell = row.getCell(0);	
					if(tempCell.getCellType() == Cell.CELL_TYPE_NUMERIC)
					{
						id = tempCell.getNumericCellValue() + "";
					}
					else if(tempCell.getCellType() == Cell.CELL_TYPE_STRING)
					{
						id = tempCell.getStringCellValue();
					}
					XSSFCell tempCellj = row.getCell(j);
					String value = "";
					if(tempCellj.getCellType() == Cell.CELL_TYPE_NUMERIC)
					{
						value = row.getCell(j).getNumericCellValue() + "";
					}
					else if(tempCellj.getCellType() == Cell.CELL_TYPE_STRING)
					{
						value = row.getCell(j).getStringCellValue();
					}
                    propertyMap.put(id, value);
                }
            }
        }

        return propertiesMaps;
    }
}
