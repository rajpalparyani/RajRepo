/**
 * Copyright(c) 2007 TeleNav, Inc.
 *
 * History:
 *   Mar 1, 2009 1:00:00 PM Created by NieLei
 */
package com.telenav.data.polygon;

import java.util.ArrayList;

/**
 *
 * @author NieLei E-mail:lnie@telenav.cn
 * @version 1.0 CreateTime:Mar 1, 2009 1:00:00 PM
 *
 */
public class FeatureGroup
{
    private byte[] datas = null;
    private FeatureAF[] features = null;

    /**
     * 
     * @param tree
     * @param index
     * @param datas
     * @param dimension
     */
    public FeatureGroup(TTree tree, int index, byte[] datas, int dimension) {
       this.datas = datas;
       if (dimension == 2) {
            try {
                int pos = 0;
                int linkNum = MmdUtil.readShortInt(datas, pos);
                features = new FeatureAF[linkNum];
                int lastPointer = 0;
                pos += 2;
                for (int i = 0; i < linkNum; i++) {
                    int pointer = MmdUtil.readInt(datas, pos); //data pointer
                    pos += 4;
                    Bounds bounds = MmdUtil.readBounds(datas, pos);
                    pos += 16;
                    features[i] = new FeatureAF(pointer, bounds);
                    if (i > 0) {
                        features[i - 1].setDataSize(pointer - lastPointer);
                    }
                    lastPointer = pointer;
                }
                features[linkNum - 1].setDataSize(datas.length - lastPointer);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void search(ArrayList<FeatureAF> featureList, Bounds query, MmdFileReader fileReader) {
        for (int i = 0, size = features.length; i < size; i++) {
            if (features[i].checkRange(query)) {
                features[i].buildFeature(datas);
                featureList.add(features[i]);
            }
        }
    }
}