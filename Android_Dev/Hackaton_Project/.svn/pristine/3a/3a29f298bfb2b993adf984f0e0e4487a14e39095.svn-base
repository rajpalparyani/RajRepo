/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * LookupAabUriTest.java
 *
 */
package com.telenav.sdk.maitai.impl;

import org.json.tnme.JSONObject;

import junit.framework.TestCase;

/**
 *@author qli (qli@telenav.cn)
 *@date 2011-9-30
 */
public class LookupAabUriTest extends TestCase
{
    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception
    {
        super.tearDown();
    }
    
    public void testParseXml()
    {
        String actUrl = null;
        
        String respOK = "<ns3:LookupUrlResponse xmlns:ns3=\"http://telenav.com/tnapi/datatypes/tnurl/v10/\"><status><code>OK</code><message>OK</message></status><url>telenav://driveTo?c=cn&amp;cb=planner%3A%2F%2F08092009&amp;coord1=37.366269%2C-122.02358&amp;coord2=37.337988%2C-121.9853&amp;lbl=Company&amp;type=DIR&amp;v=2.0&amp;k=AQAAASSR1YUgf%2F%2F%2F%2F%2F%2F%2F%2F%2F8AAAABAAAAAQEAAAAQUm2yry0wCYjB0QetcacwXwEAAAAOAwAAABEAAAAZAAAAAQA%3D</url><function>driveTo</function><expiryDate>1332817096593</expiryDate></ns3:LookupUrlResponse>";
        String expUrl = "telenav://driveTo?c=cn&amp;cb=planner%3A%2F%2F08092009&amp;coord1=37.366269%2C-122.02358&amp;coord2=37.337988%2C-121.9853&amp;lbl=Company&amp;type=DIR&amp;v=2.0&amp;k=AQAAASSR1YUgf%2F%2F%2F%2F%2F%2F%2F%2F%2F8AAAABAAAAAQEAAAAQUm2yry0wCYjB0QetcacwXwEAAAAOAwAAABEAAAAZAAAAAQA%3D";
        actUrl = LookupAabUri.parseXml(respOK);
        
        assertEquals(expUrl, actUrl);
        
        String respErr = "<ns3:LookupUrlResponse xmlns:ns3=\"http://telenav.com/tnapi/datatypes/tnurl/v10/\"><status><code>Err</code><message>OK</message></status><url>telenav://driveTo?c=cn&amp;cb=planner%3A%2F%2F08092009&amp;coord1=37.366269%2C-122.02358&amp;coord2=37.337988%2C-121.9853&amp;lbl=Company&amp;type=DIR&amp;v=2.0&amp;k=AQAAASSR1YUgf%2F%2F%2F%2F%2F%2F%2F%2F%2F8AAAABAAAAAQEAAAAQUm2yry0wCYjB0QetcacwXwEAAAAOAwAAABEAAAAZAAAAAQA%3D</url><function>driveTo</function><expiryDate>1332817096593</expiryDate></ns3:LookupUrlResponse>";
        actUrl = LookupAabUri.parseXml(respErr);
        
        assertNull(actUrl);
        
        //remove code tag
        respErr = "<ns3:LookupUrlResponse xmlns:ns3=\"http://telenav.com/tnapi/datatypes/tnurl/v10/\"><status><message>OK</message></status><url>telenav://driveTo?c=cn&amp;cb=planner%3A%2F%2F08092009&amp;coord1=37.366269%2C-122.02358&amp;coord2=37.337988%2C-121.9853&amp;lbl=Company&amp;type=DIR&amp;v=2.0&amp;k=AQAAASSR1YUgf%2F%2F%2F%2F%2F%2F%2F%2F%2F8AAAABAAAAAQEAAAAQUm2yry0wCYjB0QetcacwXwEAAAAOAwAAABEAAAAZAAAAAQA%3D</url><function>driveTo</function><expiryDate>1332817096593</expiryDate></ns3:LookupUrlResponse>";
        actUrl = LookupAabUri.parseXml(respErr);
        
        assertNull(actUrl);
        
        //use tnurl tag instead of url tag
        respErr = "<ns3:LookupUrlResponse xmlns:ns3=\"http://telenav.com/tnapi/datatypes/tnurl/v10/\"><status><code>OK</code><message>OK</message></status><tnurl>telenav://driveTo?c=cn&amp;cb=planner%3A%2F%2F08092009&amp;coord1=37.366269%2C-122.02358&amp;coord2=37.337988%2C-121.9853&amp;lbl=Company&amp;type=DIR&amp;v=2.0&amp;k=AQAAASSR1YUgf%2F%2F%2F%2F%2F%2F%2F%2F%2F8AAAABAAAAAQEAAAAQUm2yry0wCYjB0QetcacwXwEAAAAOAwAAABEAAAAZAAAAAQA%3D</tnurl><function>driveTo</function><expiryDate>1332817096593</expiryDate></ns3:LookupUrlResponse>";
        actUrl = LookupAabUri.parseXml(respErr);
        
        assertNull(actUrl);
    }
    
    public void testParseJson()
    {
        String actUrl = null;
        
        String respOK = "{\"LookupUrlResponse\":{\"status\":{\"code\":\"OK\",\"message\":\"OK\"},\"url\":\"telenav://map?k=AQAAASfkqn44f%2F%2F%2F%2F%2F%2F%2F%2F%2F8AAAABAAAAAQEAAAAQdg%2FEwNOtKiC3f4KheNaj6QEAAAAOAwAAAC4AAAA4AAAAAQA%3D&addr=3330 KiferRd,Sunnyvale,CA&v=2.0\",\"function\":\"map\",\"expiryDate\":1301873796320}}";
        String expUrl = "telenav://map?k=AQAAASfkqn44f%2F%2F%2F%2F%2F%2F%2F%2F%2F8AAAABAAAAAQEAAAAQdg%2FEwNOtKiC3f4KheNaj6QEAAAAOAwAAAC4AAAA4AAAAAQA%3D&addr=3330 KiferRd,Sunnyvale,CA&v=2.0";
        
        try
        {
            JSONObject o = new JSONObject(respOK);
            actUrl = LookupAabUri.parseJson(o);
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        assertEquals(expUrl, actUrl);
        
        
        String respErr = "{\"LookupUrlResponse\":{\"status\":{\"code\":\"Err\",\"message\":\"OK\"},\"url\":\"telenav://map?k=AQAAASfkqn44f%2F%2F%2F%2F%2F%2F%2F%2F%2F8AAAABAAAAAQEAAAAQdg%2FEwNOtKiC3f4KheNaj6QEAAAAOAwAAAC4AAAA4AAAAAQA%3D&addr=3330 KiferRd,Sunnyvale,CA&v=2.0\",\"function\":\"map\",\"expiryDate\":1301873796320}}";
        try
        {
            JSONObject o = new JSONObject(respErr);
            actUrl = LookupAabUri.parseJson(o);
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        assertNull(actUrl);
        
        //remove code string
        respErr = "{\"LookupUrlResponse\":{\"status\":{\"message\":\"OK\"},\"url\":\"telenav://map?k=AQAAASfkqn44f%2F%2F%2F%2F%2F%2F%2F%2F%2F8AAAABAAAAAQEAAAAQdg%2FEwNOtKiC3f4KheNaj6QEAAAAOAwAAAC4AAAA4AAAAAQA%3D&addr=3330 KiferRd,Sunnyvale,CA&v=2.0\",\"function\":\"map\",\"expiryDate\":1301873796320}}";
        try
        {
            JSONObject o = new JSONObject(respErr);
            actUrl = LookupAabUri.parseJson(o);
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        assertNull(actUrl);
        
        //use tnurl instead of url
        respErr = "{\"LookupUrlResponse\":{\"status\":{\"code\":\"OK\",\"message\":\"OK\"},\"tnurl\":\"telenav://map?k=AQAAASfkqn44f%2F%2F%2F%2F%2F%2F%2F%2F%2F8AAAABAAAAAQEAAAAQdg%2FEwNOtKiC3f4KheNaj6QEAAAAOAwAAAC4AAAA4AAAAAQA%3D&addr=3330 KiferRd,Sunnyvale,CA&v=2.0\",\"function\":\"map\",\"expiryDate\":1301873796320}}";
        try
        {
            JSONObject o = new JSONObject(respErr);
            actUrl = LookupAabUri.parseJson(o);
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        assertNull(actUrl);
    }
}
