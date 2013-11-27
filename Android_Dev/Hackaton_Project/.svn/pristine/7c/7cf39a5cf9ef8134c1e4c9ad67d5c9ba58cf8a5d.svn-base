/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * TnMapConfigData.java
 *
 */
package com.telenav.map.opengl.java.proxy;

import org.json.tnme.JSONObject;
import org.json.tnme.JSONTokener;

import com.telenav.logger.Logger;
import com.telenav.map.opengl.java.TnMapCanvas;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2010-10-8
 */
public class TnMapConfigData extends TnMapResourceData
{
    TnMapCanvas m_canvas_wptr;

    public TnMapConfigData(TnMapCanvas canvas, String resourceName)
    {
        super(resourceName);
        m_canvas_wptr = canvas;
    }

    public void setRequestResult(int result)
    {
        switch (result)
        {

            case TN_MAP_REQUEST_RESULT_COMPLETE:
            {
                if (m_canvas_wptr != null)
                {
                    try
                    {
                        String s = new String(this.m_resource);
                        JSONTokener tokener = new JSONTokener(s);
                        JSONObject object = (JSONObject) tokener.nextValue();

                        m_canvas_wptr.setConfigurationData(object);
                    }
                    catch(Exception e)
                    {
                        Logger.log(this.getClass().getName(), e);
                    }
                }
                break;
            }
               

            case TN_MAP_REQUEST_RESULT_FAIL:
            {
                // std::ostringstream out;
                // out << "OpenGL Map Engine: " << GetResourceName() << ": Fail" << std::endl;
                // TnMapLogError(out.str());
//                System.out.println("OpenGL Map Engine: " +GetResourceName() + ": Fail");
                break;
            }
            case TN_MAP_REQUEST_RESULT_TIMEOUT:
            {
                // std::ostringstream out;
                // out << "OpenGL Map Engine: " << GetResourceName() << ": Timeout" << std::endl;
                // TnMapLogError(out.str());
//                System.out.println("OpenGL Map Engine: " +GetResourceName() + ": Timeout");
                break;
            }
            case TN_MAP_REQUEST_RESULT_NOT_FOUND:
            {
                // std::ostringstream out;
                // out << "OpenGL Map Engine: " << GetResourceName() << ": NotFound" << std::endl;
                // TnMapLogError(out.str());
                // }
//                System.out.println("OpenGL Map Engine: " +GetResourceName() + ": NotFound");
                break;
            }
        }
    }
}
