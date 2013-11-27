/**
 * Copyright(c) 2007 TeleNav, Inc.
 *
 * History:
 *   Mar 1, 2009 1:00:00 PM Created by NieLei
 */
package com.telenav.data.polygon;



/**
 * The <code>FeatureAF</code> class.
 *
 * @author <a href="mailto:lwang@telenav.cn">wangliang</a>
 * @version 1.0 2010-6-11 16:00:09
 */
public class FeatureAF
    extends Feature
{
	Polygon polygon = null;
	public Polygon getPolygon() {
		return polygon;
	}

	/**
	 * 
	 * @param pointer
	 * @param bounds
	 */
    public FeatureAF(int pointer, Bounds bounds) {
        this.pointer = pointer;
        this.bounds = bounds;
        polygon = new Polygon();
    }

    /**
     * parse the binary to Area feature object
     */
    public void parseFeature(byte[] datas) {
        try {
            int pos = pointer;
            int keyNum = MmdUtil.readShortInt(datas, pos);
            pos += 2;
            if (keyNum > 0) {
                byte[] buff = new byte[keyNum];
                System.arraycopy(datas, pos, buff, 0, keyNum);
                String key = new String(buff, CharsetName);
                String[] ss = key.split("\\|");
                if(ss.length == 2){
                	this.key = ss[1];
                }
            }
            pos += keyNum;

            int pointNum = MmdUtil.readInt(datas, pos);
            pos += 4;

            for (int i = 0; i < pointNum; i++) {
                int lat = MmdUtil.readInt(datas, pos);
                pos += 4;
                int lon = MmdUtil.readInt(datas, pos);
                pos += 4;
                polygon.addPoint(lat, lon);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String toString() {
        StringBuffer sb = new StringBuffer(256);
        sb.append("AF:");
        sb.append("[key]=" + key);
        return sb.toString();
    }

    public String getMndString(boolean isOffsetPoint) {
    	StringBuffer strBuff = new StringBuffer();
    	return strBuff.toString();
    }
}