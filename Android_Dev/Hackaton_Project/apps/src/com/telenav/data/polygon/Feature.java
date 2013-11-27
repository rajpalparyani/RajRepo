/**
 * Copyright(c) 2010 TeleNav, Inc.
 *
 * History:
 *   2010-6-11 16:00:09 Created by Wangliang
 */
package com.telenav.data.polygon;


/**
 * The <code>Feature</code> abstract class.
 *
 * @author <a href="mailto:lwang@telenav.cn">wangliang</a>
 * @version 1.0 2010-6-11 16:00:09
 */
public abstract class Feature {
    public static final String CharsetName = "UTF-8";
    protected String key;
    protected int size;
    protected Bounds bounds = null;
    protected int pointer;
   
    /*
     * 0:not init feature
     * 1:building feature
     * 2:builded feature
     * 3:build error
     */
    private int status = 0;

    public void setDataSize(int size) {
        this.size = size;
    }

    protected void buildFeature(byte[] datas) {
        if (status == 2) {
            return;
        }
        else if (status == 0) {
            status = 1;
            parseFeature(datas);
            status = 2;
        }
        else if (status == 1) {
            while (true) {
                try {
                    Thread.sleep(2);
                    if (status != 1) {
                        break;
                    }
                }
                catch (Exception e) {
                }
            }
        }
    }


    public boolean checkRange(Bounds range) {
        return range.intersects(bounds);
    }

 
    protected abstract void parseFeature(byte[] datas);

    public abstract String getMndString(boolean isOffsetPoint);
}