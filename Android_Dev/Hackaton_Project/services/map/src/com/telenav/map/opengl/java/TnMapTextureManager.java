/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * TnMapTextureManager.java
 *
 */
package com.telenav.map.opengl.java;

import java.nio.Buffer;
import java.util.Vector;

import com.telenav.tnui.opengles.TnGL10;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2010-10-8
 */
public class TnMapTextureManager
{
    Vector m_Resident = new Vector();

    // Current GPU memory budget
    int m_Budget;

    // Current GPU memory in use
    int m_Used;
    
    TnGL10 gl11;
 // Construct a resource manager with a given GPU memory budget
    public TnMapTextureManager(TnGL10 gl11, int memoryBudget)
    {
        this.gl11 = gl11;
            m_Budget = memoryBudget;
            m_Used = 0;
    }

    // Put a previously unmanaged texture under management, returning weak pointer as handle
    public TnMapTexture manageTexture(TnMapTexture texture)
    {
        if(texture != null)
        {
            // Increment our record of memory usage
            m_Used += texture.size();

            // Remove resources until we are under budget, or until we have no textures left
            while((m_Used > m_Budget) && (!m_Resident.isEmpty()))
            {
                // Decrement used budget
                m_Used -= ((TnMapTexture)m_Resident.firstElement()).size();

                // Remove resource
                m_Resident.removeElementAt(0);
            }

            // Add to end of our LRU collection
            m_Resident.addElement(texture);

            return texture;
        }
        else
        {
            // Not a valid texture. Return invalid handle
            return null;
        }
    }

    // Explicitly remove a texture (represented by handle) from management, may destroy texture
    public void removeTexture(TnMapTexture texture)
    {
        // Lock the weak pointer

        // Is it resident?
        if(texture != null)
        {
            // Yes. Remove it from our collection
            m_Resident.removeElement(texture);

            // Decrement our record of memory usage
            m_Used -= texture.size();
        }
    }

    // Access a managed texture
    public TnMapTexture accessTexture(TnMapTexture texture)
    {
        // Lock the weak pointer

        // Is it resident?
        if(texture != null)
        {
            // Yes. Remove it from our collection
            m_Resident.removeElement(texture);

            // Add to end of our LRU collection
            m_Resident.addElement(texture);
        }

        // Return the texture
        return texture;
    }
}
