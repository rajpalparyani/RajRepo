/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * TnMap.java
 *
 */
package com.telenav.map.opengl.java;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2010-9-26
 */
public class TnMap
{
    public static class TnMapColor
    {
        public float r;
        public float g;
        public float b;
        public float a;
        
        public TnMapColor()
        {
            
        }
        
        public TnMapColor(int r, int g, int b, int a)
        {
            this.r = r;
            this.g = g;
            this.b = b;
            this.a = a;
        }
        
        public TnMapColor(String name)
        {
            if(name.indexOf("#") != -1)
            {
                long color = Long.parseLong(name.substring(1), 16);
                r = (float)((color >> 24) & 0xff);
                g = (float)((color >> 16) & 0xff);
                b = (float)((color >> 8) & 0xff);
                a = (float)((color >> 0) & 0xff);
            }
        }
        
        public static int size()
        {
            return 4 * 4;
        }
    }
    
    public static class TnMapVertex3f4b
    {
        public float x;
        public float y;
        public float z;
        public TnMapColor color = new TnMapColor();
        
        public static int size()
        {
            return 3 * 4 + TnMapColor.size();
        }
    };
    
    public static class TnMapVertex5f4b
    {
        public float x;
        public float y;
        public float z;
        public TnMapColor color = new TnMapColor();
        public float s;
        public float t;
        
        public static int size()
        {
            return 5 * 4 + TnMapColor.size();
        }
    };
    
    public static class TnMapVertex5f
    {
        public float x;
        public float y;
        public float z;
        public float s;
        public float t;
        
        public static int size()
        {
            return 5 * 4;
        }
    };
    
//    public static class TnMapVertex5f3n
//    {
//        public float x;
//        public float y;
//        public float z;
//        public float s;
//        public float t;
//        public float xN;
//        public float yN;
//        public float zN;
//    };
    
    public static class TnMapRect 
    {
        public int x;
        public int y;
        public int dx;
        public int dy;
    };
    
    public static class GLMapContext
    {
        public float   fMapZoom;
        public int    nMapZoom;
        public int    nTileBits;
        
        public int    nAltitude;
        public float   fViewAngle;
        public float   fHeading;
        
        public int    nGlobalX;
        public int    nGlobalY;
        
        public int    nStatus;
        
        public TnMapRect   rcView;

        public float[]      m_matrixModel = new float[16];
        public float[]      m_matrixProj = new float[16];
    };
    
    public static class TnMapDataBuf
    {
        int[] m_pBuf;

        int m_nDataSize;

        int m_nBufCap;

        public TnMapDataBuf()
        {

        }

        public void addBufferWithSize(int nIncSize)
        {
            int uInc = 4096;
            int uAllocSize;

            nIncSize += m_nDataSize;

            if (m_pBuf == null)
            {
                uAllocSize = uInc;
                while (nIncSize > uAllocSize)
                    uAllocSize += uInc;
                m_pBuf = new int[uAllocSize >> 2];
                m_nBufCap = uAllocSize;
            }
            else if (nIncSize > m_nBufCap)
            {
                uAllocSize = m_nBufCap + uInc;
                while (nIncSize > uAllocSize)
                    uAllocSize += uInc;
                int[] pNewBuf = new int[uAllocSize >> 2];

                System.arraycopy(m_pBuf, 0, pNewBuf, 0, m_pBuf.length);

                m_pBuf = pNewBuf;

                m_nBufCap = uAllocSize;
            }
        }

        public void release()
        {
            m_nDataSize = 0;
            m_nBufCap = 0;
            m_pBuf = null;
        }

        public int[] getBuf()
        {
            return m_pBuf;
        }

        public int getDataSize()
        {
            return m_nDataSize;
        }

        public void setDataSize(int nSize)
        {
            m_nDataSize = nSize;
        }

        public void addDataSize(int nSize)
        {
            m_nDataSize += nSize;
        }

        // public:
        // void AddBufferWithSize(uint32_t nIncSize);
        // void Release();
        // int8_t* GetBuf() const { return m_pBuf; }
        // uint32_t GetDataSize() const { return m_nDataSize; }
        // void SetDataSize(uint32_t nSize) { m_nDataSize = nSize; }
        // void AddDataSize(uint32_t nSize) { m_nDataSize += nSize; }
        // int8_t* GetEmptyStart() { return (m_pBuf + m_nDataSize); }
        //            
        //        
        // };
    }
}
