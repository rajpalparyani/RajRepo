/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * TnMapSkyDome.java
 *
 */
package com.telenav.map.opengl.java;

import com.telenav.map.opengl.java.TnMap.TnMapVertex5f;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2010-10-5
 */
public class TnMapSkyDome
{
    public int m_nFanVertex;

    public int m_nStripVertex;

    TnMapTexture m_Texture;

    TnMapVertex5f[] m_pVectices;

    public final static float PI = 3.1415927f;

    public TnMapSkyDome(TnMapTextureLoader loader)
    {
        m_nFanVertex = 0;
        m_nStripVertex = 0;
        m_Texture = loader.syncLoad(":skydome");
        m_pVectices = null;

        float fPhiDelta = PI / 90.0f;
        float fPhiCap = PI / 2.0f * 0.4f;
        float fHeight = 1.0f;
        float fWidth = fHeight * 2.0f * (float) Math.sin(fPhiCap);
        float fThetaDelta = PI / 20.0f;

        float fPhi, fTheta;
        int nVertices, nRings, nSlice, nIndex;
        float nHeightDelta;
        nRings = (int) (fPhiCap / fPhiDelta - 1);
        nHeightDelta = fHeight * (float) Math.cos(fPhiCap / fPhiDelta * fPhiDelta);

        nSlice = (int) Math.floor(2.0f * PI / fThetaDelta + 0.5f); // Quick fix to avoid using round function
        nVertices = 1 + (nSlice + 1) + (nRings * (nSlice + 1) * 2);

        m_pVectices = new TnMapVertex5f[nVertices];
        for(int i = 0; i < nVertices; i++)
        {
            m_pVectices[i] = new TnMapVertex5f();
        }
        nIndex = 0;

        float fCentralS = 0.5f;
        float fCentralT = 0.5f;
        // put the central top point
        m_pVectices[nIndex].x = 0;
        m_pVectices[nIndex].y = 0;
        m_pVectices[nIndex].z = fHeight - nHeightDelta;
        m_pVectices[nIndex].s = fCentralS;
        m_pVectices[nIndex].t = fCentralT;

        nIndex++;

        // put the triangle fan vertices
        fTheta = 0;
        fPhi = fPhiDelta;

        float nRadius = fHeight;
        float nSubRadius = nRadius * (float) Math.sin(fPhi);
        float nHeight = nRadius * (float) Math.cos(fPhi);
        int nStartIndex = nIndex;

        for (int i = 0; i < nSlice; i++)
        {
            m_pVectices[nIndex].x = nSubRadius * (float) Math.cos(fTheta);
            m_pVectices[nIndex].y = nSubRadius * (float) Math.sin(fTheta);
            m_pVectices[nIndex].z = nHeight - nHeightDelta;
            m_pVectices[nIndex].s = fCentralS + m_pVectices[nIndex].x / fWidth;
            m_pVectices[nIndex].t = fCentralT + m_pVectices[nIndex].y / fWidth;
            fTheta -= fThetaDelta;
            nIndex++;
        }

        m_pVectices[nIndex] = m_pVectices[nStartIndex];
        nIndex++;
        m_nFanVertex = nIndex;

        float nSubRadius1, nSubRadius2, nHeight1, nHeight2;

        // put the triangle trips vertices
        for (int i = 0; i < nRings; i++)
        {
            nSubRadius1 = nRadius * (float) Math.sin(fPhi);
            nHeight1 = nRadius * (float) Math.cos(fPhi);
            fPhi += fPhiDelta;
            nSubRadius2 = nRadius * (float) Math.sin(fPhi);
            nHeight2 = nRadius * (float) Math.cos(fPhi);
            fTheta = 0;
            nStartIndex = nIndex;
            for (int j = 0; j < nSlice; j++)
            {
                m_pVectices[nIndex].x = nSubRadius1 * (float) Math.cos(fTheta);
                m_pVectices[nIndex].y = nSubRadius1 * (float) Math.sin(fTheta);
                m_pVectices[nIndex].z = nHeight1 - nHeightDelta;
                m_pVectices[nIndex].s = fCentralS + m_pVectices[nIndex].x / fWidth;
                m_pVectices[nIndex].t = fCentralT + m_pVectices[nIndex].y / fWidth;
                nIndex++;
                m_pVectices[nIndex].x = nSubRadius2 * (float) Math.cos(fTheta);
                m_pVectices[nIndex].y = nSubRadius2 * (float) Math.sin(fTheta);
                m_pVectices[nIndex].z = nHeight2 - nHeightDelta;
                m_pVectices[nIndex].s = fCentralS + m_pVectices[nIndex].x / fWidth;
                m_pVectices[nIndex].t = fCentralT + m_pVectices[nIndex].y / fWidth;
                nIndex++;

                fTheta -= fThetaDelta;
            }
            m_pVectices[nIndex] = m_pVectices[nStartIndex];
            nIndex++;
            m_pVectices[nIndex] = m_pVectices[nStartIndex + 1];
            nIndex++;
            m_nStripVertex = nIndex - m_nFanVertex;
        }

    }

    public void destroy()
    {
//        System.out.println("*****TnMapSkyDome destroy-----");
        m_pVectices = null;
    }

    public void reload(TnMapTextureLoader loader)
    {
        m_Texture = loader.syncLoad(":skydome");
    }

    // Accessors
    public int getFanVertexCount()
    {
        return m_nFanVertex;
    }

    public int getStripVertexCount()
    {
        return m_nStripVertex;
    }

    public TnMapTexture getTexture()
    {
        return m_Texture;
    }

    public TnMapVertex5f[] getVertices()
    {
        return m_pVectices;
    }
}
