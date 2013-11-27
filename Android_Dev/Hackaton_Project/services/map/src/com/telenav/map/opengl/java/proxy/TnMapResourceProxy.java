/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * AndroidResourceProxy.java
 *
 */
package com.telenav.map.opengl.java.proxy;

import java.util.Hashtable;

/**
 *@author jyxu (jyxu@telenav.cn)
 *@date 2010-10-08
 */

public class TnMapResourceProxy implements ITnMapResourceProxy
{
    private static Hashtable fileNameResourceIdMapping;
    
    public static void initMapping(Hashtable mapping)
    {
        fileNameResourceIdMapping = mapping;
    }
    
    public static String resourceToFilename(String resource)
    {
        if(":configuration".equals(resource))
        {
            return "config_day.json";
        }
        else if(":skydome".equals(resource))
        {
            return ":SkyDome";
        }
        
        return resource;
    }
 
    public void requestResource(TnMapResourceData data_ptr)
    {
//    	 if (data_ptr != null){
//             String filepath;
//             String resourcePath="/sdcard/opengl_map_engine/Resources/";
//             filepath = resourcePath;
//             filepath +=  ResourceToFilename(data_ptr.GetResourceName());
//             File path = new File(filepath);
//           
//           if(path != null)
//           {
//               try
//               {
//                   InputStream fis = new FileInputStream(path);
//	               int size = fis.available();
//	               
//	               byte[]  resource = new byte[size];
//	               int result = fis.read(resource);
//	               if (size == result)
//	               {
//	              	 data_ptr.SetResource(resource,size);
//	              	 data_ptr.SetRequestResult(TnMapDataRequest.eTnMapRequestResult_Complete);
//	               }
//	               else
//	               {
//	              	 data_ptr.SetRequestResult(TnMapDataRequest.eTnMapRequestResult_Fail);
//	               }
//               }
//               catch(Exception e)
//               {
//                   e.printStackTrace();
//               }
//              
//           }
//           else
//           {
//               data_ptr.SetRequestResult(TnMapDataRequest.eTnMapRequestResult_NotFound);
//           }
//             
//             
//         }
//    	 else
//         {
//         }
    }
    
    
    public void cancelRequest(TnMapResourceData data)
    {


    }

    public AbstractTextureImage getTextureResource(String fileName)
    {
        fileName = fileName.toLowerCase();
        AbstractTextureImage img = (AbstractTextureImage) fileNameResourceIdMapping.get(fileName);
        return img;
    }
}
