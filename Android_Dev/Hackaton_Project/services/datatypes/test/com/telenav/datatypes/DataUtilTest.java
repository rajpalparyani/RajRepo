package com.telenav.datatypes;

import org.junit.Assert;

import junit.framework.TestCase;

public class DataUtilTest extends TestCase
{
    public void testReadWriteLong()
    {
        byte[] buff = new byte[100];
        DataUtil.writeLong(buff, 12345, 50);
        assertEquals(12345, DataUtil.readLong(buff, 50));
    }

    public void testReadWriteInt()
    {
        byte[] buff = new byte[100];
        DataUtil.writeInt(buff, 999, 50);
        assertEquals(999, DataUtil.readInt(buff, 50));
    }

    public void testReadWriteShort()
    {
        byte[] buff = new byte[100];
        DataUtil.writeShort(buff, 123, 50);
        assertEquals(123, DataUtil.readShort(buff, 50));
    }

    public void testIntArrayByteArrayTransform()
    {
        int[] intArr = new int[100];

        for (int i = 0; i < 100; i++)
        {
            intArr[i] = i;
        }

        byte[] byteArr = DataUtil.intArrayToByteArray(intArr);

        int[] intArrNew = DataUtil.byteArrayToIntArray(byteArr);

        // check if intArrNew and intArr are the same

        for (int i = 0; i < 100; i++)
        {
            assertEquals(intArr[i], intArrNew[i]);
        }
        
        assertNull(DataUtil.byteArrayToIntArray(null));
        assertNull(DataUtil.intArrayToByteArray(null));
    }

    public void testGetCosLat()
    {
        assertEquals(7022, DataUtil.getCosLat(3097876));
    }

    public void testXCosY()
    {
        assertEquals(5266, DataUtil.xCosY(8192, 50));
    }

    public void testSin()
    {
        assertEquals(44695, DataUtil.sin(35000));
        assertEquals(-65536, DataUtil.sin(69120));
    }

    public void testCos()
    {
        assertEquals(-47142, DataUtil.cos(35000));
    }

    public void testXSinY()
    {
        assertEquals(-6275, DataUtil.xSinY(8192, 310));
    }

    public void testMul()
    {
        assertEquals(153, DataUtil.mul(5000, 2000));
    }

    public void testDistance()
    {
        assertEquals(4301, DataUtil.distance(3500, 2500));
        assertEquals(4301, DataUtil.distance(-3500, -2500));
        assertEquals(0, DataUtil.distance(0, 0));
    }

    public void testSetByte()
    {
        byte[] buff = new byte[30];
        byte[] result = DataUtil.setByte(buff, (byte) 3, 200);
        assertEquals(3, result[200]);
    }

    public void testGpsDistance()
    {
        assertEquals(368, DataUtil.gpsDistance(300, 500, 3500));
        assertEquals(1, DataUtil.gpsDistance(0, 0, 3500));
    }

    public void testGpsDistance2()
    {
        assertEquals(93721, DataUtil.gpsDistance2(300, 500, 1000));
    }

    public void testBearing()
    {
        assertEquals(60, DataUtil.bearing(300, 500));
        assertEquals(143, DataUtil.bearing(3320000, 11200000, 3100000, 11400000));
    }

    public void testCross()
    {
        int[] reference = new int[3];
        reference[0] = 3160000;
        reference[1] = 12200000;
        reference[2] = 1;
        int[] other = new int[3];
        other[0] = 3160120;
        other[1] = 12200330;
        other[2] = 1;
        assertEquals(593696572, DataUtil.cross(reference, other, 200));
    }

    public void testProject()
    {
        int[] reference = new int[3];
        reference[0] = 3160000;
        reference[1] = 12200000;
        reference[2] = 1;
        int[] other = new int[3];
        other[0] = 3160120;
        other[1] = 12200330;
        other[2] = 1;
        assertEquals(-1296123449, DataUtil.project(reference, other, 200));
    }

    public void testSleep()
    {
        long start = System.currentTimeMillis();
        DataUtil.sleep(100);
        assertEquals(true, System.currentTimeMillis() - start >= 100);
    }

    public void testPow()
    {
        assertEquals(4, DataUtil.pow(3, 2, 2));
    }

    public void testatan2()
    {
        assertEquals(9728, DataUtil.atan2(20, 25));
        assertEquals(57344, DataUtil.atan2(-1, -1));
        assertEquals(34304, DataUtil.atan2(1, -1));
        assertEquals(80384, DataUtil.atan2(-1, 1));
        assertEquals(0, DataUtil.atan2(0, 0));
    }

    public void testDiv()
    {
        assertEquals(234837, DataUtil.div(43, 12));
    }

    public void testMileToKm()
    {
        assertEquals(160, DataUtil.mileToKm(100));
    }

    public void testKmToMile()
    {
        assertEquals(99, DataUtil.kmToMile(160));
    }

    public void testMileToDm5()
    {
        assertEquals(231310, DataUtil.mileToDm5(160));
    }

    public void testKmToDm5()
    {
        assertEquals(179662, DataUtil.kmToDm5(200));
    }

    public void testDm5ToMile()
    {
        assertEquals(8, DataUtil.dm5ToMile(12345));
    }

    public void testDm5ToKm()
    {
        assertEquals(13, DataUtil.dm5ToKm(12345));
    }

    public void testDm6ToMile()
    {
        assertEquals(7, DataUtil.dm6ToMile(123450));
    }

    public void testDm6ToKm()
    {
        assertEquals(13, DataUtil.dm6ToKm(123450));
    }

    public void testRound()
    {
        assertEquals(2500, DataUtil.round(10000, 2));
        assertEquals(2, DataUtil.round(13, 3));
        assertEquals(-2, DataUtil.round(-11, 3));
        assertEquals(-3, DataUtil.round(-13, 3));
    }

    public void testDotProd()
    {
        assertEquals(3200, DataUtil.dotProd(20, 30, 40, 80));
    }

    public void testGetDist2()
    {
        assertEquals(29, DataUtil.getDist2(20, 30, 40, 80, 25, 28));
        assertEquals(100, DataUtil.getDist2(20, 30, 40, 50, 40, 60));
        assertEquals(34, DataUtil.getDist2(20, 30, 40, 60, 40, 50));
    }

    public void testIsIntersecting()
    {
        assertEquals(true, DataUtil.isIntersecting(200, 200, 500, 500, 500, 200, 200, 500));
        assertEquals(false, DataUtil.isIntersecting(200, 200, 500, 500, 200, 300, 550, 800));
        assertEquals(false, DataUtil.isIntersecting(0, 0, 200, 200, 300, 300, 550, 800));
    }

    public void testIsLeft()
    {
        assertEquals(1, DataUtil.isLeft(200, 200, 500, 500, 200, 300, 10));
    }

    public void testCalcLineHeading()
    {
        assertEquals(270, DataUtil.calcLineHeading(0, 0, 120, 0));
    }

    public void testGetValueLength()
    {
        assertEquals(2, DataUtil.getValueLength(2833));
        byte[] buff = new byte[128];
        for (int i = 0; i < 128; i++)
        {
            buff[i] = (byte) i;
        }
        assertEquals(1, DataUtil.getValueLength(buff, 0));
        assertEquals(2, DataUtil.getValueLength(buff, 1));
        assertEquals(3, DataUtil.getValueLength(buff, 3));
        assertEquals(4, DataUtil.getValueLength(buff, 7));
        assertEquals(5, DataUtil.getValueLength(buff, 15));
        assertEquals(6, DataUtil.getValueLength(buff, 31));
        assertEquals(7, DataUtil.getValueLength(buff, 63));
        assertEquals(9, DataUtil.getValueLength(buff, 127));
        
        assertEquals(1, DataUtil.getValueLength(0));
        assertEquals(2, DataUtil.getValueLength(64));
        assertEquals(3, DataUtil.getValueLength(8192));
        assertEquals(4, DataUtil.getValueLength(1048576));
        assertEquals(5, DataUtil.getValueLength(134217728));
        assertEquals(6, DataUtil.getValueLength(17179869184L));
        assertEquals(7, DataUtil.getValueLength(2199023255552L));
        assertEquals(9, DataUtil.getValueLength(281474976710656L));
    }

    public void testReadOffset()
    {
        byte[] buff = new byte[128];
        for (int i = 0; i < 128; i++)
        {
            buff[i] = (byte) i;
        }

        int[] offset = new int[1];
        DataUtil.readOffset(buff, offset);
        assertEquals(1, offset[0]);
        
        offset[0] = 1;
        DataUtil.readOffset(buff, offset);
        assertEquals(3, offset[0]);
        
        offset[0] = 3;
        DataUtil.readOffset(buff, offset);
        assertEquals(6, offset[0]);
        
        offset[0] = 7;
        DataUtil.readOffset(buff, offset);
        assertEquals(11, offset[0]);
        
        offset[0] = 15;
        DataUtil.readOffset(buff, offset);
        assertEquals(20, offset[0]);
        
        offset[0] = 79;
        DataUtil.readOffset(buff, offset);
        assertEquals(84, offset[0]);
        
        offset[0] = 31;
        DataUtil.readOffset(buff, offset);
        assertEquals(37, offset[0]);
        
        offset[0] = 63;
        DataUtil.readOffset(buff, offset);
        assertEquals(70, offset[0]);
        
        offset[0] = 127;
        DataUtil.readOffset(buff, offset);
        assertEquals(136, offset[0]);
    }

    public void testReadValue()
    {
        byte[] buff = new byte[136];
        for (int i = 0; i < 136; i++)
        {
            buff[i] = (byte) i;
        }

        int[] offset = new int[1];
        assertEquals(0, DataUtil.readValue(buff, offset));
        
        offset[0] = 1;
        assertEquals(128, DataUtil.readValue(buff, offset));
        
        offset[0] = 3;
        assertEquals(41088, DataUtil.readValue(buff, offset));
        
        offset[0] = 7;
        assertEquals(10522752, DataUtil.readValue(buff, offset));
        
        offset[0] = 15;
        assertEquals(2559608960l, DataUtil.readValue(buff, offset));
        
        offset[0] = 31;
        assertEquals(620833047680l, DataUtil.readValue(buff, offset));
        
        offset[0] = 63;
        assertEquals(152318977016448l, DataUtil.readValue(buff, offset));
        
        offset[0] = 127;
        assertEquals(-8681104427521506944l, DataUtil.readValue(buff, offset));
    }

    public void testWriteValue()
    {
        byte[] buff = new byte[32];
        DataUtil.writeValue(buff, 1000, 0);

        assertEquals(1000, DataUtil.readValue(buff, new int[]
        { 0 }));
        
        buff = new byte[32];
        byte[] expected = new byte[32];
        expected[0] = 20;
        assertEquals(1, DataUtil.writeValue(buff, 10, 0));
        Assert.assertArrayEquals(expected, buff);
        
        buff = new byte[32];
        expected = new byte[32];
        expected[0] = 1;
        expected[1] = 1;
        assertEquals(2, DataUtil.writeValue(buff, 64, 0));
        Assert.assertArrayEquals(expected, buff);
        
        buff = new byte[32];
        expected = new byte[32];
        expected[0] = 3;
        expected[1] = 0;
        expected[2] = 1;
        assertEquals(3, DataUtil.writeValue(buff, 8192, 0));
        Assert.assertArrayEquals(expected, buff);
        
        buff = new byte[32];
        expected = new byte[32];
        expected[0] = 7;
        expected[1] = 0;
        expected[2] = 0;
        expected[3] = 1;
        assertEquals(4, DataUtil.writeValue(buff, 1048576, 0));
        Assert.assertArrayEquals(expected, buff);
        
        buff = new byte[32];
        expected = new byte[32];
        expected[0] = 15;
        expected[1] = 0;
        expected[2] = 0;
        expected[3] = 0;
        expected[4] = 1;
        assertEquals(5, DataUtil.writeValue(buff, 134217728, 0));
        Assert.assertArrayEquals(expected, buff);
        
        buff = new byte[32];
        expected = new byte[32];
        expected[0] = 31;
        expected[1] = 0;
        expected[2] = 0;
        expected[3] = 0;
        expected[4] = 0;
        expected[5] = 1;
        assertEquals(6, DataUtil.writeValue(buff, 17179869184l, 0));
        Assert.assertArrayEquals(expected, buff);
        
        buff = new byte[32];
        expected = new byte[32];
        expected[0] = 63;
        expected[1] = 0;
        expected[2] = 0;
        expected[3] = 0;
        expected[4] = 0;
        expected[5] = 0;
        expected[6] = 1;
        assertEquals(7, DataUtil.writeValue(buff, 2199023255552L, 0));
        Assert.assertArrayEquals(expected, buff);
        
        buff = new byte[32];
        expected = new byte[32];
        expected[0] = -1;
        expected[1] = 0;
        expected[2] = 0;
        expected[3] = 0;
        expected[4] = 0;
        expected[5] = 0;
        expected[6] = 0;
        expected[7] = 1;
        expected[8] = 0;
        assertEquals(9, DataUtil.writeValue(buff, 281474976710656L, 0));
        Assert.assertArrayEquals(expected, buff);
    }

    public void testCheck()
    {
        // it should not throw exceptions
        DataUtil.check(9, "This is a test!");
    }

    public void testBuildBox4()
    {
        int[][] box = DataUtil.buildBox4(32, 23, 45, 89);
        assertEquals(32, box[0][0]);
        assertEquals(23, box[0][1]);
        assertEquals(32, box[1][0]);
        assertEquals(89, box[1][1]);
        assertEquals(45, box[2][0]);
        assertEquals(89, box[2][1]);
        assertEquals(45, box[3][0]);
        assertEquals(23, box[3][1]);
    }

    public void testIntersect()
    {
        assertEquals(true, DataUtil.intersect(23, 23, 44, 89, new int[]
        { 1, 34, 50, 76 }));
    }

    public void testComputeBoundingBox()
    {
        int[] result = DataUtil.computeBoundingBox(new int[]
        { 23, 45, 13, 45, 18, 56, 12, 45 }, 4);
        assertEquals(12, result[0]);
        assertEquals(45, result[1]);
        assertEquals(23, result[2]);
        assertEquals(56, result[3]);
    }

    public void testInRect()
    {
        assertEquals(false, DataUtil.inRect(22, 67, 30, 234, 0, 0));
        assertEquals(true, DataUtil.inRect(22, 67, 30, 234, 27, 150));
    }

    public void testIsRectIntersecting()
    {
        assertEquals(false, DataUtil.isRectIntersecting(22, 67, 2, 2, 45, 35, 2, 3));
    }

    public void testIsValidBoundingBox()
    {
        assertEquals(false, DataUtil.isValidBoundingBox(new int[]
        { 1, 1, 0, 0 }));

        assertEquals(true, DataUtil.isValidBoundingBox(new int[]
        { 1, 1, 5, 3 }));
    }

    public void testDegreeToRadian()
    {
        double result = DataUtil.degreeToRadian(100);
        assertEquals(true, result > 1.745 && result < 1.746);
    }

    public void testRadianToDegree()
    {
        double result = DataUtil.radianToDegree(100);
        assertEquals(true, result > 5729.57 && result < 5729.58);
    }

}