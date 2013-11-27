/**
 * Copyright(c) 2007 TeleNav, Inc.
 *
 * History:
 *   Mar 1, 2009 1:00:00 PM Created by NieLei
 */
package com.telenav.data.polygon;

import java.io.IOException;


/**
 *
 * @author NieLei E-mail:lnie@telenav.cn
 * @version 1.0 CreateTime:Mar 1, 2009 1:00:00 PM
 *
 */
public class MmdUtil
{

    public static Bounds readBounds(byte[] datas, int pos) throws IOException {
        int minX = MmdUtil.readInt(datas, pos);
        int minY = MmdUtil.readInt(datas, pos + 4);
        int maxX = MmdUtil.readInt(datas, pos + 8);
        int maxY = MmdUtil.readInt(datas, pos + 12);
        return new Bounds(minX, maxX, minY, maxY);
    }

    public static int readInt(byte[] datas, int pos) throws IOException {
        int ch1 = datas[pos++];
        if (ch1 < 0) {
            ch1 += 256;
        }
        int ch2 = datas[pos++];
        if (ch2 < 0) {
            ch2 += 256;
        }
        int ch3 = datas[pos++];
        if (ch3 < 0) {
            ch3 += 256;
        }
        int ch4 = datas[pos++];
        if (ch4 < 0) {
            ch4 += 256;
        }
        return ((ch1 << 0) + (ch2 << 8) + (ch3 << 16) + (ch4 << 24));
    }

    public static short readShortInt(byte[] datas, int pos) throws IOException {
        int ch1 = datas[pos++];
        if (ch1 < 0) {
            ch1 += 256;
        }
        int ch2 = datas[pos++];
        if (ch2 < 0) {
            ch2 += 256;
        }
        return (short) ((ch1 << 0) + (ch2 << 8));
    }
}
