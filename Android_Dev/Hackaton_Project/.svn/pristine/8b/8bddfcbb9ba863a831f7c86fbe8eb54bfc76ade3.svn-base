/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * TxNodeAddressSerializable.java
 *
 */
package com.telenav.data.serializable.txnode;

import java.util.Vector;

import com.telenav.data.datatypes.address.Address;
import com.telenav.data.datatypes.address.AddressRecevier;
import com.telenav.data.datatypes.address.FavoriteCatalog;
import com.telenav.data.datatypes.address.SentAddress;
import com.telenav.data.datatypes.address.Stop;
import com.telenav.data.serializable.IAddressSerializable;
import com.telenav.logger.Logger;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2010-12-21
 */
class TxNodeAddressSerializable implements IAddressSerializable
{
    public Address createAddress(byte[] data)
    {
        Node node = new Node(data, 0);
        
        return createAddress(node);
    }       

    static Address createAddress(Node node)
    {
        Address address = new Address();
        if (node != null && (node.getValueAt(0) == Address.TYPE_STOP_POI_WRAPPER))
        {
            address.setType((byte)node.getValueAt(1));
            address.setStatus((byte)node.getValueAt(2));
            address.setId(node.getValueAt(3));
            address.setLabel(node.getStringAt(0));
            
            boolean isReadByUser = node.getValueAt(7) == 1 ? true : false;
            address.setReadByUser(isReadByUser);
            
            if(node.getStringsSize() > 1)
            {
                address.setSharedFromUser(node.getStringAt(1));
                address.setSharedFromPTN(node.getStringAt(2));
            }

            address.setSelectedIndex((int)node.getValueAt(8));
            
            int size = (int)node.getValueAt(4);
            Vector categories = new Vector(size);
            for(int i = 0; i < size && node.getStringsSize() >= 5; i++)
            {
                categories.insertElementAt(node.getStringAt(i+4),i);
            }
            address.setCatagories(categories);
            
            if(node.getValuesSize() > 6)
            {
                address.setCreateTime(node.getValueAt(5));
                address.setUpdateTime(node.getValueAt(6));
            }
            
            if(node.getValuesSize() > 9)
            {
                long eventStartTime = node.getValueAt(9);
                address.setEventStartTime(eventStartTime);
            }
            
            if(node.getValuesSize() > 10)
            {
                long eventEndTime = node.getValueAt(10);
                address.setEventEndTime(eventEndTime);
            }
            
            if(node.getValuesSize() > 11)
            {
                int isUseEventStartTime = (int)node.getValueAt(11);
                address.setUseEventStartTime(isUseEventStartTime == 1 ? true : false);
            }
            
            if(node.getValuesSize() > 12)
            {
                int isUseEventEndTime = (int)node.getValueAt(12);
                address.setUseEventEndTime(isUseEventEndTime == 1 ? true : false);
            }
            
            if(node.getValuesSize() > 13)
            {
                long eventId = node.getValueAt(13);
                address.setEventId(eventId);
            }
            
            if(node.getValuesSize() > 14)
            {
                int isEventDetailAvailable = (int)node.getValueAt(14);
                address.setIsEventDataAvailable(isEventDetailAvailable == 1 ? true : false);
            }

            if (node.getStringsSize() > 3)
            {
                address.setEventVenue(node.getStringAt(3));
            }
            
            if (node.getStringsSize() > size + 4)
                address.setPhoneNumber(node.getStringAt(size + 4));

            if (address.getType() == Address.TYPE_FAVORITE_POI || address.getType() == Address.TYPE_RECENT_POI )
            {
                address.setPoi(TxNodePoiSerializable.createPoi(node.getChildAt(0))); 
                if(address.getLabel() == null || address.getLabel().trim().length() <= 0 )
                {
                    // wrap poi
                    address.setLabel(address.getPoi().getBizPoi().getBrand());
                }
            }
            else
            {
                Stop stop = createStop(node.getChildAt(0));
                address.setStop(stop);
                if(address.getLabel() == null || address.getLabel().trim().length() <= 0 )
                {
                    // wrap poi
                    String strView = stop.getLabel() ;
                    if(strView != null && strView.trim().length() > 0)
                    {
                        address.setLabel(strView);
                    }
                }
            }
        }
        
        return address;
    }
    
    public byte[] toBytes(Address address)
    {
        return toNode(address).toBinary();
    }
    
    static Node toNode(Address address)
    {
        Node node = new Node();
        // add values
        node.addValue(Address.TYPE_STOP_POI_WRAPPER); // V[0]
        node.addValue(address.getType()); // V[1]
        node.addValue(address.getStatus()); // V[2]
        node.addValue(address.getId()); // V[3]
        int size = address.getCatagories() == null ? 0 : address.getCatagories().size();
        node.addValue(size); // V[4]
        node.addValue(address.getCreateTime());//V[5]
        node.addValue(address.getUpdateTime());//V[6]
        int isReadByUser = address.isReadByUser() == true ? 1 : 0;
        node.addValue(isReadByUser);//V[7]
        node.addValue(address.getSelectedIndex());//V[8]
        node.addValue(address.getEventStartTime()); //V[9]
        node.addValue(address.getEventEndTime()); //V[10]
        node.addValue(address.isUseEventStartTime() ? 1 : 0); //V[11]
        node.addValue(address.isUseEventEndTime() ? 1 : 0); //V[12]
        node.addValue(address.getEventId()); //V[13]
        node.addValue(address.isEventDataAvailable() ? 1 : 0); //V[14]

        node.addString(address.getLabel()); // MSG[0]
        node.addString(address.getSharedFromUser()); // MSG[1]
        node.addString(address.getSharedFromPTN()); // MSG[2]
        node.addString(address.getEventVenue()); //MSG[3]

        // add category names

        for (int i = 0; i < size; i++)
        {
            node.addString((String) address.getCatagories().elementAt(i));
        }

        // add phone number
        if (address.getPhoneNumber() != null)
            node.addString(address.getPhoneNumber());

        // add children
        if (address.getType() == Address.TYPE_FAVORITE_POI || address.getType() == Address.TYPE_RECENT_POI)
        {
            // add child
            if (address.getPoi() != null)
            {
                node.addChild(TxNodePoiSerializable.toNode(address.getPoi()));
            }
        }
        else
        {
            if (address.getStop() != null)
            {
                Node child = toNode(address.getStop());
                node.addChild(child);
            }
        }
        
        return node;
    }
    
    
    public FavoriteCatalog createFavoriteCatalog(byte[] data)
    {
        Node node = new Node(data, 0);
        
        return createFavoriteCatalog(node);
    }
    
    static FavoriteCatalog createFavoriteCatalog(Node node)
    {
        FavoriteCatalog catalog = null;
        if (node != null && (node.getValueAt(0) == FavoriteCatalog.TYPE_FAVORITE_CATEGORY))
        {
            catalog = new FavoriteCatalog();
            
            catalog.setCatType((byte) node.getValueAt(1));
            catalog.setId(node.getValueAt(2));
            catalog.setStatus((byte)node.getValueAt(3));
            catalog.setName(node.getStringAt(0));
            if(node.getValuesSize() > 5)
            {
                catalog.setCreateTime(node.getValueAt(4));
                catalog.setUpdateTime(node.getValueAt(5));
            }
        }
        
        return catalog;
    }
    
    public byte[] toBytes(FavoriteCatalog catalog) 
    {
        return toNode(catalog).toBinary();
    }
    
    static Node toNode(FavoriteCatalog catalog)
    {
        Node node = new Node();
        //add values
        node.addValue(FavoriteCatalog.TYPE_FAVORITE_CATEGORY); //V[0]
        node.addValue(catalog.getCatType()); //V[1]
        node.addValue(catalog.getId()); //V[2]
        node.addValue(catalog.getStatus()); // V[3]
        node.addValue(catalog.getCreateTime());//V[4]
        node.addValue(catalog.getUpdateTime());//V[5]
        //add messages
        node.addString(catalog.getName());

        return node;
    }
    
    public Stop createStop(byte[] data)
    {
        Node node = new Node(data, 0);
        
        return createStop(node);
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
                        stop.setId(Long.parseLong(strID));
                    }
                    catch (Throwable e)
                    {
                        Logger.log(TxNodeAddressSerializable.class.getClass().getName(), e);
                    }
                }
            }
            stop.setPostalCode(node.getStringAt(index++));
            stop.setCountry(node.getStringAt(index++));
            
            stop.setHouseNumber(node.getStringAt(index++));
            stop.setSuite(node.getStringAt(index++));
            stop.setStreetName(node.getStringAt(index++));
            stop.setCrossStreetName(node.getStringAt(index++));
            stop.setCounty(node.getStringAt(index++));
            stop.setSubLocality(node.getStringAt(index++));
            stop.setLocality(node.getStringAt(index++));
            stop.setLocale(node.getStringAt(index++));
            stop.setSubStreet(node.getStringAt(index++));
            stop.setBuildingName(node.getStringAt(index++));
            stop.setAddressId(node.getStringAt(index++));
        }
        if(parent.getValueAt(0) == Stop.TYPE_AIRPORT)
        {
            stop.setLabel(parent.getStringAt(0));
        }
        
        return stop;
    }
    
    public byte[] toBytes(Stop stop)
    {
        return toNode(stop).toBinary();
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
        node.addString("" + stop.getId());//4
        node.addString(stop.getPostalCode());//5
        node.addString(stop.getCountry());//6
        
        node.addString(stop.getHouseNumber());
        node.addString(stop.getSuite());
        node.addString(stop.getStreetName());
        node.addString(stop.getCrossStreetName());
        node.addString(stop.getCounty());
        node.addString(stop.getSubLocality());
        node.addString(stop.getLocality());
        node.addString(stop.getLocale());
        node.addString(stop.getSubStreet());
        node.addString(stop.getBuildingName());
        node.addString(stop.getAddressId());
        
        return node;
    }
    static Node toNode(AddressRecevier addressRecevier)
    {
        Node node = new Node();
        node.addValue(AddressRecevier.TYPE_ADDRESS_RECEIVER);
        for (int i = 0; i < addressRecevier.getReceivers().size(); i++)
        {
            node.addString((String) addressRecevier.getReceivers().elementAt(i));
        }
        return node;
    }
	
    public byte[] toBytes(SentAddress sentAddress)
    {
        return toNode(sentAddress).toBinary();
    }

    private Node toNode(SentAddress sentAddress)
    {
        Node node = new Node();
        node.addValue(SentAddress.TYPE_SENT_ADDRESS);
        node.addValue(sentAddress.getCreatedTime());
        node.addString(sentAddress.getLabel());
        node.addString(sentAddress.getStreet());
        node.addString(sentAddress.getCity());
        node.addString(sentAddress.getState());
        node.addString(sentAddress.getZip());
        node.addString(sentAddress.getCountry());
        if(sentAddress.getReceiver() != null)
        {
            node.addChild(toNode(sentAddress.getReceiver()));
        }
        return node;
    }

    public SentAddress createSentAddress(byte[] data)
    {
        return createSentAddress(new Node(data, 0));
    }
    
    static SentAddress createSentAddress(Node node)
    {
        SentAddress sentAddress = new SentAddress();
        if(node != null && node.getValueAt(0) == SentAddress.TYPE_SENT_ADDRESS)
        {
            sentAddress.setCreatedTime(node.getValueAt(1));
            sentAddress.setLabel(node.getStringAt(0));
            sentAddress.setStreet(node.getStringAt(1));
            sentAddress.setCity(node.getStringAt(2));
            sentAddress.setState(node.getStringAt(3));
            sentAddress.setZip(node.getStringAt(4));
            sentAddress.setCountry(node.getStringAt(5));
            AddressRecevier addressRecevier = new AddressRecevier();
            Vector receivers = new Vector();
            for(int i = 0; i < node.getChildAt(0).getStringsSize(); i ++)
            {
                receivers.addElement(node.getChildAt(0).getStringAt(i));
            }
            addressRecevier.setReceivers(receivers);
            sentAddress.setReceiver(addressRecevier);
        }
        return sentAddress;
    }
}
