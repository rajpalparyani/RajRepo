/**
 * Copyright(c) 2007 TeleNav, Inc.
 *
 * History:
 *   Mar 1, 2009 1:00:00 PM Created by NieLei
 */
package com.telenav.data.polygon;

import java.util.ArrayList;

import android.graphics.Point;
import android.graphics.Rect;

/**
 *
 * @author NieLei E-mail:lnie@telenav.cn
 * @version 1.0 CreateTime:Mar 1, 2009 1:00:00 PM
 *
 */
public class TTree
{
    private MmdFileReader m_fileReader = null;
    private int m_dimension = -1;
    private int m_treeLevel = 0;
    private int m_linkNum = 0;

    private int[] m_linkPosArr = null;
    private short[] m_subCountArr = null;
    private int[] m_dataPosArr = null;
    private int[] m_minXArr = null;
    private int[] m_minYArr = null;
    private int[] m_maxXArr = null;
    private int[] m_maxYArr = null;
    private FeatureGroup[] m_featGroupArr = null;

    public TTree(MmdFileReader fileReader,int dimension, int treeLevel, int linkNum) {
        m_fileReader = fileReader;
        m_dimension = dimension;
        m_treeLevel = treeLevel;
        m_linkNum = linkNum;

        m_linkPosArr = new int[linkNum];
        m_subCountArr = new short[linkNum];
        m_dataPosArr = new int[linkNum];
        m_minXArr = new int[linkNum];
        m_minYArr = new int[linkNum];
        m_maxXArr = new int[linkNum];
        m_maxYArr = new int[linkNum];
        m_featGroupArr = new FeatureGroup[linkNum];
    }

    public void insertNode(int index, int linkPos, short subCount, int dataPos, int minX, int minY, int maxX, int maxY) {
        m_linkPosArr[index] = linkPos;
        m_subCountArr[index] = subCount;
        m_dataPosArr[index] = dataPos;
        m_minXArr[index] = minX;
        m_minYArr[index] = minY;
        m_maxXArr[index] = maxX;
        m_maxYArr[index] = maxY;
    }

    public int getDimension() {
        return m_dimension;
    }

    public int getTreeLevel() {
        return m_treeLevel;
    }

    public int getLinkNum() {
        return m_linkNum;
    }

    /**
     * 
     * @return region identifier (Country/SateName), delimiter is "|" when regions > 1 
     */
    public String getRegion(int lat1, int lon1, int lat2, int lon2){
    	ArrayList<FeatureAF> resultList = searchArea(lat1, lon1, lat2, lon2);
    	Rect rectangle = new Rect(lat1, lon1, lat2 - lat1, lon2 - lon1);
        return getRegionInfo(resultList, rectangle);
    }
    
    /**
     * 
     * @param lat Md6
     * @param lon Md6
     * @return region identifier (Country/SateName), delimiter is "|" when regions > 1
     */
    public String getRegion(int lat, int lon) {
    	ArrayList<FeatureAF> resultList = searchArea(lat, lon, lat + 10, lon + 10);
    	return getRegionInfo(resultList, new Point(lat, lon));
    }
    
    /**
     * @param lat1 Md6
     * @param lon1 Md6
     * @param lat2 Md6
     * @param lon2 Md6
     * @return Area feature lists whose bounding box intersect with the given bounding box, 
     */
    public  ArrayList<FeatureAF> searchArea(int lat1, int lon1, int lat2, int lon2) {
        try {
            if (lat1 > lat2) {
                int tmp = lat2;
                lat2 = lat1;
                lat1 = tmp;
            }
            if (lon1 > lon2) {
                int tmp = lon2;
                lon2 = lon1;
                lon1 = tmp;
            }

            Bounds query = new Bounds(lat1, lat2, lon1, lon2);
            ArrayList<FeatureAF> resultList = new ArrayList<FeatureAF>();
            search(resultList, query, 0);
            return resultList;
        }
        catch (Exception e) {
        	e.printStackTrace();
        }
        return null;
    }

	/**
	 * @param resultList
	 * @param rectangle
	 * @return Region info whose polygon intersects with given bounding box
	 */
	private String getRegionInfo(ArrayList<FeatureAF> resultList,
			Rect rectangle) {
		StringBuffer strBuff = new StringBuffer();
		for (int i = 0; i < resultList.size(); i++) {
		    Feature feature = (FeatureAF)resultList.get(i);
		    FeatureAF featureAF = ((FeatureAF) feature);
		    Polygon polygon = featureAF.getPolygon();
			if(polygon.intersects(rectangle))
		    	strBuff.append(featureAF.key + "|");
		}

		return strBuff.toString();
	}
	
	/**
	 * @param resultList
	 * @param rectangle
	 * @return Region info whose polygon contains the point
	 */
	private String getRegionInfo(ArrayList<FeatureAF> resultList,
			Point point) {
		StringBuffer strBuff = new StringBuffer();
		for (int i = 0; i < resultList.size(); i++) {
		    Feature feature = (FeatureAF)resultList.get(i);
		    FeatureAF featureAF = ((FeatureAF) feature);
		    Polygon polygon = featureAF.getPolygon();
			if(polygon.contains(point))
		    	strBuff.append(featureAF.key + "|");
		}

		return strBuff.toString();
	}

    public void search(ArrayList<FeatureAF> featureList, Bounds query, int index) {
        if (!query.intersects(m_minXArr[index], m_minYArr[index], m_maxXArr[index], m_maxYArr[index])) {
            return;
        }
        if (m_dataPosArr[index] > 0) {

            if (m_featGroupArr[index] == null) {
                byte[] datas = m_fileReader.loadData(m_dataPosArr[index]);
                if (datas == null) {
                    m_featGroupArr[index] = null;
                }
                else {
                    FeatureGroup fg = new FeatureGroup(this, index, datas, m_dimension);
                    m_featGroupArr[index] = fg;
                }
            }
            if (m_featGroupArr[index] != null) {
                m_featGroupArr[index].search(featureList, query, m_fileReader);
            }
        }
        int count = m_subCountArr[index];
        if (count > 0) {
            for (int i = 0; i < count; i++) {
                int pos = m_linkPosArr[index];
                if (pos >= 0) {
                    int subIndex = pos + i;
                    if (subIndex >= 0 && subIndex < m_linkNum) {
                        search(featureList, query, subIndex);
                    }
                }
            }
        }
    }
}
