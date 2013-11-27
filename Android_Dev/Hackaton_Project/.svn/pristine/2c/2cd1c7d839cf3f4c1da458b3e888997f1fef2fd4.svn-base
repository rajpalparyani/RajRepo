/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * AddressDao.java
 *
 */
package com.telenav.searchwidget.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.Vector;

import com.telenav.searchwidget.app.AppConfigHelper;
import com.telenav.searchwidget.data.datatypes.address.Stop;

/**
 *@author hchai
 *@date 2011-7-25
 */
public class AddressDao
{
    private final static String KEY_HOME_ADDRESS   = "1";
    private final static String KEY_OFFICE_ADDRESS = "2";
    
    private static String ADDRESS_DIRECTORY;
    private static String FILE_HOME_NAME;
    private static String FILE_OFFICE_NAME;    
    private static String FILE_INDEX;
    
    private static AddressDao inst = new AddressDao();
    private String rootPath;
    
    private AddressDao()
    {
        ADDRESS_DIRECTORY = "TN70_" + AppConfigHelper.brandName + "_express_address";
        FILE_HOME_NAME      = ADDRESS_DIRECTORY + "/" + KEY_HOME_ADDRESS + ".bin";
        FILE_OFFICE_NAME    = ADDRESS_DIRECTORY + "/" + KEY_OFFICE_ADDRESS + ".bin";
        FILE_INDEX = ADDRESS_DIRECTORY + ".index";
    }
    
    public static AddressDao getInstance()
    {
        return inst;
    }
    
    public void setRootPath(String rootPath)
    {
        this.rootPath = rootPath;
    }

    private byte[] readData(String name)
    {
        FileInputStream fis = null;
        byte[] data = null;
        try
        {
            File f = new File(this.rootPath + "/" + name);
            if(f.exists())
            {
                fis = new FileInputStream(f);
                int size = fis.available();
                data = new byte[size];
                fis.read(data);
            }
        }
        catch(Throwable e)
        {
        }
        finally
        {
            try
            {
                if (fis != null)
                {
                    fis.close();
                }
            }
            catch (Exception e)
            {
            }
        }
        
        return data;
    }
    
    private void writeData(String name, byte[] buffer)
    {
        FileOutputStream fos = null;
        try
        {
            File dir = new File(this.rootPath + "/" + ADDRESS_DIRECTORY);
            if (!dir.exists())
            {
            	if (!dir.mkdir()) return;
            }
            
            File f = new File(this.rootPath + "/" + name);
            if (f.exists())
            {
            	if (!f.delete()) return;
            }
            if (f.createNewFile())
            {
                fos = new FileOutputStream(f);
                fos.write(buffer);
            }            
        }
        catch (SecurityException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
        	e.printStackTrace();
        }
        finally
        {
            try
            {
                if (fos != null)
                {
                    fos.close();
                }
            }
            catch (Exception e)
            {                    
            }
        }
    }
    
    public Stop getHomeStop()
    {
        Stop stop = null;
        byte[] data = readData(FILE_HOME_NAME);
        if (data != null)
        {
            Node node = new Node(data, 0);
            stop = createStop(node);
        }
        
        return stop;
    }
    
    public Stop getOfficeStop()
    {
        Stop stop = null;
        byte[] data = readData(FILE_OFFICE_NAME);
        if (data != null)
        {
            Node node = new Node(data, 0);
            stop = createStop(node);
        }
        
        return stop;
    }
    
    public void setHomeStop(Stop home)
    {
        Node homeNode = toNode(home);
        
        writeData(FILE_HOME_NAME, homeNode.toBinary());
        
        Vector v = readIndexes(FILE_INDEX);
        if (!v.contains(KEY_HOME_ADDRESS))
        {
            v.addElement(KEY_HOME_ADDRESS);
        }
        saveIndexes(v, FILE_INDEX);
    }
    
    public void setOfficeStop(Stop office)
    {
        Node officeNode = toNode(office);
        
        writeData(FILE_OFFICE_NAME, officeNode.toBinary());
        
        Vector v = readIndexes(FILE_INDEX);
        if (!v.contains(KEY_OFFICE_ADDRESS))
        {
            v.addElement(KEY_OFFICE_ADDRESS);
        }
        saveIndexes(v, FILE_INDEX);
    }
    
    private Vector readIndexes(String indexKey)
    {
        Vector v = new Vector();

        byte[] buffer = readData(indexKey);
        if(buffer != null && buffer.length > 0)
        {
            StringTokenizer st = new StringTokenizer(new String(buffer), ",");
            while(st.hasMoreTokens())
            {
                String key = st.nextToken();
                if(key.length() > 0)
                {
                    v.addElement(key);
                }
            }
        }
        
        return v;
    }

    private void saveIndexes(Vector v, String indexKey)
    {
        if(v == null)
            return;
        
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < v.size(); i++)
        {
            String tmpKey = (String)v.elementAt(i);
            sb.append(tmpKey + ",");
        }
        
        writeData(indexKey, sb.toString().getBytes());
    }
    
    static Stop createStop(Node parent)
    {
        Stop stop = null;
        Node node = parent;
        if(parent.getValueAt(0) == Stop.TYPE_AIRPORT)
        {
            node = parent.getChildAt(0);
        }
        if(node != null && (node.getValueAt(0) == Stop.TYPE_STOP))
        {
            stop = new Stop();
            int index = 1;
            stop.setLat((int) node.getValueAt(index++));
            stop.setLon((int) node.getValueAt(index++));
            stop.setType((byte) node.getValueAt(index++));
            stop.setStatus((byte) node.getValueAt(index++));
            stop.setIsGeocoded(node.getValueAt(index++) == 1);
            index++; // ignore hash code
            stop.setSharedAddress(((int) node.getValueAt(index++)) == 1 ? true : false);

            index = 0;
            stop.setLabel(node.getStringAt(index++));
            String firstLine = node.getStringAt(index++);
            stop.setFirstLine(firstLine);
            stop.setCity(node.getStringAt(index++));
            stop.setProvince(node.getStringAt(index++));

            // System.out.println("Stop.toStop: firstLine: "+firstLine+", city: "+stop.getCity()+", state: "+stop.getProvince()+", label: "+stop.getLabel());
            if(node.getStringsSize() >= 7)
            {
                String strID = node.getStringAt(index++);
                if (strID != null && strID.length() > 0)
                {
                    try
                    {
                        stop.setID(Long.parseLong(strID));
                    }
                    catch (Throwable e)
                    {
//                        Logger.log(TxNodeAddressSerializable.class.getClass().getName(), e);
                    }
                }
            }
            stop.setPostalCode(node.getStringAt(index++));
            stop.setCountry(node.getStringAt(index++));
            stop.parseFirstLine(firstLine);
        }
        if(parent.getValueAt(0) == Stop.TYPE_AIRPORT)
        {
            stop.setLabel(parent.getStringAt(0));
        }
        
        return stop;
    }
    
    static Node toNode(Stop stop)
    {
        Node node = new Node();
        node.addValue(Stop.TYPE_STOP);//0
        node.addValue(stop.getLat());//1
        node.addValue(stop.getLon());//2
        node.addValue(stop.getType());//3
        node.addValue(stop.getStatus());//4
        node.addValue(stop.isGeocoded() ? 1:0);//5
        node.addValue(0);//6 //hash code
        node.addValue(stop.isSharedAddress() ? 1:0);
     
        String firstLine = stop.getFirstLine();
        
        node.addString(stop.getLabel());//0
        node.addString(firstLine);//1
        node.addString(stop.getCity());//2
        node.addString(stop.getProvince());//3
        node.addString("" + stop.getID());//4
        node.addString(stop.getPostalCode());//5
        node.addString(stop.getCountry());//6
        
        return node;
    }

}
