package com.telenav.datatypes;

import com.telenav.logger.Logger;

public abstract class DataUtil
{
    public static final int MIN_LATITUDE  = -9000000;
    public static final int MAX_LATITUDE  =  9000000;
    public static final int MIN_LONGITUDE = -18000000;
    public static final int MAX_LONGITUDE =  18000000;


    public static final byte SIZE_BYTE      = 1;
    public static final byte SIZE_SHORT     = 2;
    public static final byte SIZE_INT       = 4;
    public static final byte SIZE_LONG      = 8;

    private static final byte FIRST_BIT        = -128;
    private static final byte SECOND_BIT       = 64;
    private static final byte THIRD_BIT        = 32;
    private static final byte FORTH_BIT        = 16;
    private static final byte FIFTH_BIT        = 8;
    private static final byte SIXTH_BIT        = 4;
    private static final byte SEVENTH_BIT      = 2;
    private static final byte EIGHTH_BIT       = 1;

    public static final int FIX_BITS = 8;
    public static final int DFIX_BITS = 16;
    //---------- scale and shift for DM5 data --------------
    /** SCALE */
    public static final long SCALE = 8192;

    /** corresponding SHIFT value */
    public static final int SHIFT = 13; //log base 2 of SCALE
    public static final byte LAT        = 0;
    public static final byte LON        = 1;
    public static final byte DELTAR     = 2;

    public static final byte SCREEN_X   = 0;
    public static final byte SCREEN_Y   = 1;

    // -----polygon type
    public static final byte TURNINDICATOR      = 1;
    public static final byte UNKNOWN            = 2;
    public static final byte GEOPOLITICAL       = 3;
    public static final byte PARK               = 4;
    public static final byte CAMPUS             = 5;
    public static final byte WATER              = 6;
    public static final byte INTERIOR           = 7;
    public static final byte BUILDING           = 8;

    //  public static final byte ISLAND             = 18;

    // -----road type
    public static final byte RAMP               = 11;
    public static final byte HIGHWAYRAMP        = 12;
    public static final byte SMALLSTREET        = 13;
    public static final byte LOCAL_STREET       = 14;
    public static final byte INTERSECTION       = 15;
    public static final byte ARTERIAL           = 16;
    public static final byte HIGHWAY            = 17;   

    public static final byte TRAFFIC_CIRCLE     = 50;
    public static final byte ROUND_ABOUT        = 51;


    //-------other type
    public static final byte RAIL               = 21;
    public static final byte CANAL              = 22;
    public static final byte STATEBORDER        = 23;
    public static final byte NATIONBORDER       = 24;
    public static final byte WATER_LINE         = 25;
    public static final byte FERRYBOAT_LINE     = 26;
    public static final byte DATE_LINE          = 27;



    //------- mark types
    public static final byte UNKNOWN_CENTER     = 31;
    public static final byte PARK_CENTER        = 32;
    public static final byte CAMPUS_CENTER      = 33;
    public static final byte WATER_CENTER       = 34;
    public static final byte INTERIOR_CENTER    = 35;
    public static final byte BUILDING_CENTER    = 36;
    public static final byte CITY_CENTER        = 37;
    public static final byte STATE_CENTER       = 38;

    private static final int sinTable[] = { 0, 1143, 2287, 3429, 4571, 5711,
        6850, 7986, 9120, 10252, 11380, 12504, 13625, 14742, 15854, 16961,
        18064, 19160, 20251, 21336, 22414, 23486, 24550, 25606, 26655,
        27696, 28729, 29752, 30767, 31772, 32767, 33753, 34728, 35693,
        36647, 37589, 38521, 39440, 40347, 41243, 42125, 42995, 43852,
        44695, 45525, 46340, 47142, 47929, 48702, 49460, 50203, 50931,
        51643, 52339, 53019, 53683, 54331, 54963, 55577, 56175, 56755,
        57319, 57864, 58393, 58903, 59395, 59870, 60326, 60763, 61183,
        61583, 61965, 62328, 62672, 62997, 63302, 63589, 63856, 64103,
        64331, 64540, 64729, 64898, 65047, 65176, 65286, 65376, 65446,
        65496, 65526, 0x10000 };

    private static final int atanTable[] = { 0, 0, 256, 512, 768, 1024, 1280,
        1536, 1792, 2048, 2048, 2304, 2560, 2816, 3072, 3328, 3584, 3584,
        3840, 4096, 4352, 4608, 4608, 4864, 5120, 5376, 5632, 5632, 5888,
        6144, 6400, 6400, 6656, 6912, 6912, 7168, 7424, 7680, 7680, 7936,
        8192, 8192, 8448, 8448, 8704, 8960, 8960, 9216, 9216, 9472, 9472,
        9728, 9984, 9984, 10240, 10240, 10496, 10496, 10752, 10752, 11008,
        11008, 11264, 11264, 11264 };

    private static final int sqrtTable[] = { 0x40000000, 0x4001fff8, 0x4007ff80,
        0x4011fd79, 0x401ff804, 0x4031ec87, 0x4047d7ad, 0x4061b56a,
        0x407f80fe, 0x40a134f9, 0x40c6cb42, 0x40f03d1b, 0x411d8325,
        0x414e956c, 0x41836b64, 0x41bbfbfc, 0x41f83d9b, 0x4238262d,
        0x427bab2b, 0x42c2c19f, 0x430d5e30, 0x435b7529, 0x43acfa7f,
        0x4401e1db, 0x445a1ea3, 0x44b5a3fe, 0x451464df, 0x4576540c,
        0x45db6424, 0x464387a8, 0x46aeb0fe, 0x471cd27d, 0x478dde6e,
        0x4801c717, 0x48787ebb, 0x48f1f7a3, 0x496e2425, 0x49ecf6a2,
        0x4a6e6191, 0x4af25781, 0x4b78cb1a, 0x4c01af24, 0x4c8cf689,
        0x4d1a9459, 0x4daa7bca, 0x4e3ca03c, 0x4ed0f53c, 0x4f676e85,
        0x50000000, 0x509a9dc9, 0x51373c2e, 0x51d5cfaf, 0x52764d01,
        0x5318a90f, 0x53bcd8f8, 0x5462d210, 0x550a89e3, 0x55b3f633,
        0x565f0cf6, 0x570bc45b, 0x57ba12c3, 0x5869eec9, 0x591b4f3a,
        0x59ce2b18, 0x5a82799a, 0x5a82799a };
    
    public static final double KM_TO_MILE = 0.6214;

    private final static int errorMargin = 100;

    public static void writeLong(byte [] buff, long num, int offset)
    {
        for (int i = 0; i < 8; i++)
        {
            buff[offset+i] = (byte) (num & 0xFF);
            num = num >> 8;
        }
    }

    public static long readLong(byte[] buff, int offset)
    {
        long mask = 0xFF;
        long sum = 0;
        int i;
        for (i = 0; i < 7; i++)
        {
            sum += (mask & buff[offset+i]) << (8 * i);
        }

        sum += (long) buff[offset+i] << (8 * i);
        return sum;
    }

    /**
     * convert byte array into int 
     * NOTE: cast to INT essential for sign propagation 
     */
     public static int readInt(byte[] buff, int offset)
     {
         int mask = 0xFF;
         return (mask & buff[offset]) + ((mask & buff[offset+1]) << 8) + ((mask & buff[offset+2]) << 16) + ((int) buff[offset+3] << 24);
     }

     /**
      * write an int into a byte array
      * 
      * @param num - the number to write
      * @param offset - the offset into the array to write it to 
      */
     public static void writeInt(byte [] buff, int num, int offset)
     {
         buff[offset] = (byte) (num & 0xFF);
         num = num >> 8;
         buff[offset+1] = (byte) (num & 0xFF);
         num = num >> 8;
         buff[offset+2] = (byte) (num & 0xFF);
         num = num >> 8;
         buff[offset+3] = (byte) (num & 0xFF);
     }

     /**
      * Convert from an int array to a byte array
      * 
      * @param value - the int array to be converted
      * @return the byte array
      */
     public static byte[] intArrayToByteArray(int[] value)
     {
         if (value == null)
             return null;
         byte[] buff = new byte[value.length << 2];
         for (int i=0; i<value.length; i++)
         {
             writeInt(buff, value[i], i<<2);
         }
         return buff;
     }

     /**
      * Convert a byte array back to an int array
      * 
      * @param buff - the byte array
      * @return the int array
      */
     public static int[] byteArrayToIntArray(byte[] buff)
     {
         if (buff == null)
             return null;
         int[] value = new int[buff.length >> 2];
         for (int i=0; i<value.length; i++)
         {
             value[i] = readInt(buff, i<<2);
         }
         return value;
     }

     /**
      * convert byte array into short (cast to int) value 
      * NOTE: cast to INT essential for sign propagation 
      */
     public static int readShort(byte[] buff, int offset)
     {
         int mask = 0xFF;
         return (mask & buff[offset]) + (buff[offset+1] << 8);
     }

     /**
      * write an int as SHORT into the byte array: only write 2 bytes.
      * @param buff
      * @param num
      * @param offset
      * @throws Exception
      */
     public static void writeShort(byte [] buff, int num, int offset)
     {
         buff[offset] = (byte) (num & 0xFF);
         num = num >> 8;
         buff[offset+1] = (byte) (num & 0xFF);
     }


     public static int getCosLat(int lat)
     {
         return (int) xCosY(SCALE, ( 21 * Math.abs(lat) ) >> 21);
     }

     /**
      * calculate X * cosine ( Y ) using the approximation table
      * @param x - X value (SCALE)
      * @param y - Y value (angle)
      * @return X * cosine ( Y )
      */
     public static long xCosY(long x, int y)
     {
         x *= cos(y << FIX_BITS);
         return (x+32767) >> (FIX_BITS*2);
     }

     private final static int TWO_PI = 0x16800;
     private final static int PI = 46080;
     private final static int HALF_PI = 23040;

     /*
      * angle should be left shifted by 8
      * result is left shifted by 16
      */
     public static int sin(int angle)
     {
         int sign = 1;
         if (angle < 0)
         {
             angle = -angle;
             sign = -1;
         }

         angle %= TWO_PI;

         if (angle > PI)
         {
             angle = TWO_PI - angle;
             sign = -sign;
         }

         if (angle > HALF_PI)
         {
             angle = PI - angle;
         }

         return sign * sinTable[angle >> 8];
     }

     /*
      * angle should be left shifted by 8
      * result is left shifted by 16
      */
     public static int cos(int angle)
     {
         return sin(HALF_PI - angle);
     }

     /**
      * calculate X * sin ( Y ) = X * cos ( Y + 270 )
      */
     public static long xSinY(long x, int y)
     {
         return xCosY(x, y + 270);
     }

     /*
      * i, j are left shifted by 16
      */
     public final static int mul(int i, int j)
     {
         return (int) ((long) i * (long) j + 32768L >> 16);
     }

     /**
      * @param dx
      * @param dy
      * @return   sqrt(dx*dx + dy*dy)
      */
     public static final int distance(int dx, int dy)
     {
         // for better performance
         // int k = Math.abs(dx);                
         int k;
         if (dx > 0)
         {
             k = dx;
         }
         else
         {
             k = -dx;
         }

         // int l = Math.abs(dy);
         int l;
         if (dy > 0)
         {
             l = dy;
         }
         else
         {
             l = -dy;
         }


         if (k > l)
         {
             int m = k;
             k = l;
             l = m;
         }
         if (l == 0)
         {
             return 0;
         } else
         {
             int m = div(k, l);
             int n = m >> 10;
             int d = (m & 0x3ff) << 6;
             m = mul(0x10000 - d, sqrtTable[n]) + mul(d, sqrtTable[n + 1]);
             m >>= 14;
             return mul(l, m);
         }
     }


     /** append a byte into byte array, checking for overflow */
     static protected byte[] setByte(byte[] buff, byte b, int counter)
     {
         while (counter >= buff.length)
         {
             byte[] newBuff = new byte[2 * buff.length];
             for (int i = 0; i < buff.length; i++) newBuff[i] = buff[i];
             buff = newBuff;
         }

         buff[counter] = b;
         return buff;
     }

     /**
      * calculate the gps distance between two points
      * @param       deltaLat                [in]    dm5
      * @param       deltaLon                [in]    dm5
      * @param       scaledCosLat    [in]
      * @return      gps distance (dm5)
      */
     public static int gpsDistance(int deltaLat, int deltaLon, int cosLat)
     {
         int distance;
         distance = distance(
                 deltaLat,
                 (int)((long)(deltaLon) * cosLat) >> SHIFT);

         //sanity check
         if (distance == 0) distance = 1;
         return distance;
     } // gpsDistance

     /**
      * calculate the square of gps distance between two points
      * @param       deltaLat                [in]    dm5
      * @param       deltaLon                [in]    dm5
      * @param       scaledCosLat    [in]
      * @return      gps distance (dm5)
      */
     public static long gpsDistance2(int deltaLat, int deltaLon, int cosLat)
     {
         long deltaLonCos = ((long)deltaLon * cosLat) >> SHIFT; 
         return (long)deltaLat * deltaLat + deltaLonCos * deltaLonCos; 
     }

     public static int bearing(int hereLat, int hereLon, int thereLat, int thereLon)
     {
         // northing
         int dn = thereLat - hereLat;
         // convert avg lat in DM5 to degrees
         int avgLatDeg = (int) ((21 * (thereLat + hereLat)) >> 22);
         // easting
         int de = (int)xCosY(thereLon - hereLon, avgLatDeg);

         return bearing(dn, de);
     }

     public static int bearing(int dn, int de)
     {
         int d1 = de << FIX_BITS;
         int d2 = dn << FIX_BITS;

         int angle = (atan2(d1, d2)>> FIX_BITS);

         angle = angle % 360;
         if (angle < 0)
         {
             angle = 360 + angle;
         }

         //              System.out.println("DataUtil::bearing, dn, de: "+dn+", "+de+", "+angle);

         return angle;
     }


     public static int cross(int [] reference, int [] other, int coslat)
     {
         //check for deltar = 0
         if (reference[DELTAR] == 0)
             return 1;

         long reflength = (long) reference[DELTAR] * SCALE * SCALE;

         //return cross product of the 2 shape points
         //TODO: figure out the range at which cast to int will BREAK things !!!
         return (int) (((long) reference[LAT] * other[LON] - reference[LON] * other[LAT]) * 
                 SCALE * coslat / reflength);
     }

     public static int project(int[] reference, int[] other, int coslat)
     {
         //check for deltar = 0
         if (reference[DELTAR] == 0)
             return 1;

         //coslat = SCALE (8192) * cosine ( lattitude )
         //so multiply non-cosine weighted terms by SCALE (8192) as well
         //RM: NOTE, lat needs to have SCALE ^ 2, while lon needs to have
         //coslat ^ 2, and reflengh needs to have SCALE * rss !!!!
         long dotprod = (long) reference[LAT] * SCALE * other[LAT] * SCALE + 
         (long) coslat * reference[LON] * coslat * other[LON];

         long reflength = (long) reference[DELTAR] * SCALE * SCALE;

         long result = dotprod / reflength;
         //System.out.println("^^^ dotprod = " + dotprod + " reflen = " + reflength + " long project = " + result);
         return (int) result;
     }


     public static void sleep(long time)
     {
         try
         {
             Thread.sleep(time);
         }
         catch (Throwable e)
         {
             Logger.log(DataUtil.class.getName(), e);
         }
     }

     public static long pow(int x,int y, int scale)
     {
         long ret = 1;
         //long scale_factor = 1;
         for (int i = 0; i < y; ++i)
         { 
             ret *= x;
             if (i != 0)
             { ret /= scale; }
         } // for

         return ret;
     }

     /**
      * calculate the angle. 
      * On screen coordinate, the 0 degree is from left to right, 90 degrees is from top to bottom
      * 180 degrees is from right to left, and 270 degrees is from bottom to top
      * atan2(0, 1) = 0
      * atan2(1, 0) = 90
      * atan2(0, -1) = 180
      * atan2(-1, 0) = 270 
      * @param delY
      * @param delX
      * @return - result is in [0, 360)
      */
     public static int atan2(int delY, int delX)
     {
         int k;
         if (delX > 0 && delY >= 0) // 1st quadrant
         {
             k = 0;
         }
         else if (delX <= 0 && delY > 0) // 2nd quadrant
         {
             int l = -delX;
             delX = delY;
             delY = l;
             k = 23040; //90 * 256
         } 
         else if (delX < 0 && delY <= 0) // 3rd quadrant
         {
             delX = -delX;
             delY = -delY;
             k = 46080; // 180 * 256
         } 
         else if (delX >= 0 && delY < 0) // 4th quadrant
         {
             int i1 = delX;
             delX = -delY;
             delY = i1;
             k = 0x10e00; // 270 * 256
         } 
         else
         {
             return 0; // both delY and delX are zero
         }

         // each quadrant is divided into two halves, each subtending 45 deg
         if (delX >= delY)
         {
             int j1 = div(delY, delX) >> 10;
             k += atanTable[j1];
         } 
         else
         {
             int k1 = div(delX, delY) >> 10;
             k += 23040 - atanTable[k1];
         }
         return k;
     }

     /*
      * result is left shifted by 16
      */
     public static int div(int i, int j)
     {
         return (int) (((long) i << 16) / (long) j);
     }
     
     public static int mileToKm(int mile)
     {
         return (mile * 13184) >> SHIFT;
     }
     
     public static int kmToMileInHighAccuracy(int km)
     {
        double dResult = km * KM_TO_MILE;
        int result = (int) (dResult + 0.5);

        return result;
     }
     
     public static int kmToMile(int km)
     {
         return (km * 5090) >> SHIFT;
     }
     
     public static int mileToDm5(int mile)
     {
         return (mile * 23131) >> 4;
     }
     
     public static int kmToDm5(int km)
     {
         return (km * 14373) >> 4;
     }
     
     public static int dm5ToMile(int dm5)
     {
         return (dm5 * 11) >> (SHIFT + 1);
     }
     
     public static int dm5ToKm(int dm5)
     {
         return (dm5 * 9) >> SHIFT;
     }

     public static int dm6ToMile(int dm6)
     {
         return dm6 >> (SHIFT + 1);
     }
     
     public static int dm6ToKm(int dm6)
     {
         return (dm6 * 7) >> (SHIFT + 3);
     }
     
     public static int round(long d, int shift)
     {
         int mask = (1 << shift) - 1;
         int dRoundOff = (1 << shift) >> 1;
         
         if (d > 0)
         {
             if ((d & mask) <= dRoundOff)
             {
                 d >>= shift;
             }
             else
             {
                 d = (d >> shift) + 1;
             }
         }
         else
         {
             if (((-d) & mask) < dRoundOff)
             {
                 d >>= shift;
             }
             else
             {
                 d = (d >> shift) - 1;
             }
         }
         return (int) d;
     }

     public static int dotProd(int aX, int aY, int bX, int bY)
     {
         return aX * bX + aY * bY;
     }

     public static int getDist2(int x0, int y0, int x1, int y1, int x, int y)
     {
         int vX = x1 - x0;
         int vY = y1 - y0;
         int wX = x - x0;
         int wY = y - y0;
         
         int c1 = dotProd(wX, wY, vX, vY);
         
         if (c1 <= 0)
         {
             return wX * wX + wY * wY;
         }
         else
         {
             int c2 = dotProd(vX, vY, vX, vY);
             
             if (c2 <= c1)
             {
                 int dx = x - x1;
                 int dy = y - y1;
                 return dx * dx + dy * dy;
             }
             else
             {
                 int pbX = x0 + vX * c1 / c2;
                 int pbY = y0 + vY * c1 / c2;
                 
                 int dx = x - pbX;
                 int dy = y - pbY;
                 return dx * dx + dy * dy;
             }
         }
     }
     
     /**
      * Checks if two lines are intersected
      */
     public static boolean isIntersecting(int lon1, int lat1, int lon2,int lat2, 
             int lon3, int lat3, int lon4, int lat4) 
     {
         if (isMinGreaterThanMax(lon1, lon2, lon3, lon4) ||
                 isMinGreaterThanMax(lon3, lon4, lon1, lon2) ||
                 isMinGreaterThanMax(lat1, lat2, lat3, lat4) ||
                 isMinGreaterThanMax(lat3, lat4, lat1, lat2))
         {
             return false;
         }

         return isLeft(lon1, lat1, lon3, lat3, lon4, lat4, errorMargin) * 
         isLeft(lon2, lat2, lon3, lat3, lon4, lat4, errorMargin) <= 0 && 
         isLeft(lon3, lat3, lon1, lat1, lon2, lat2, errorMargin) *  
         isLeft(lon4, lat4, lon1, lat1, lon2, lat2, errorMargin) <= 0;
     }

     /**
      * if Math.min(min1, min2) > Math.max(max1, max2) return true
      */
     private static boolean isMinGreaterThanMax(int min1, int min2, int max1, int max2)
     {
         int min = min1 < min2 ? min1 : min2;
         int max = max1 > max2 ? max1 : max2;
         return min > max;
     }



     /**
      *  Checks if point(x0,y0) is at the left space of line (x1,y1) - (x,y)
      * 
      * @param x0
      * @param y0
      * @param x1
      * @param y1
      * @param x
      * @param y
      * 
      * @return 0 :  if point(x0,y0) is on the line
      *         1 :  if point(x0,y0) is at the left space of the line 
      *        -1 :  if point(x0,y0) is NOT at the left space of the line  
      */
     public static int isLeft(int x0, int y0, int x1, int y1, int x, int y, int errorMargin)
     {
         int value = (x1 - x0) * (y - y0) - (x - x0) * (y1 - y0);

         //do most possible check first, it will be more effective - zdong
         return value >= errorMargin ?  1 :
             value <=-errorMargin ? -1 :
                 0;
     }

     /**
      *  return clockwise angle for the line heading
      */
     public static int calcLineHeading(int xFrom, int yFrom, int xTo, int yTo)
     {
         int d1 = (xTo - xFrom) << FIX_BITS;
         int d2 = (yTo - yFrom) << FIX_BITS;

         int angle = (-(atan2(d1, d2)>> FIX_BITS)) % 360;

         while(angle < 0) angle += 360;

         return angle;
     }       

     public static int getValueLength(byte[] buff, int offset)
     {
         if (offset >= buff.length) return Integer.MAX_VALUE / 2;

         byte first = buff[offset];
         if ((first & EIGHTH_BIT) == 0)
         {
             return 1;
         }
         else if ((first & SEVENTH_BIT) == 0)
         {
             return 2;
         }
         else if ((first & 31) != 31) //last five bits are not all "1"
         {
             if ((first & SIXTH_BIT) == 0)
             {
                 return 3;
             }
             else if ((first & FIFTH_BIT) == 0)
             {
                 return 4;
             }
             else
             {
                 return 5;
             }
         }
         else
         {
             if ((first & THIRD_BIT) == 0)
             {
                 return 6;
             }
             else if ((first & SECOND_BIT) == 0)
             {
                 return 7;
             }
             else
             {
                 return 9;
             }
         }

     }

     public static void readOffset(byte[] buff, int[] offset)
     {
         byte first = buff[offset[0]];
         if ((first & EIGHTH_BIT) == 0)
         {
             offset[0] += 1;
         }
         else if ((first & SEVENTH_BIT) == 0)
         {
             offset[0] += 2;
         }
         else if ((first & 31) != 31) //last five bits are not all "1"
         {
             if ((first & SIXTH_BIT) == 0)
             {
                 offset[0] += 3;
             }
             else if ((first & FIFTH_BIT) == 0)
             {
                 offset[0] += 4;
             }
             else
             {
                 offset[0] += 5;
             }
         }
         else
         {
             if ((first & THIRD_BIT) == 0)
             {
                 offset[0] += 6;
             }
             else if ((first & SECOND_BIT) == 0)
             {
                 offset[0] += 7;
             }
             else
             {
                 offset[0] += 9;
             }
         }
     }


     //read value from buff started at offset[0], the value size is self-defined
     public static long readValue(byte[] buff, int[] offset)
     {
         long value;
         byte first = buff[offset[0]];
         if ((first & EIGHTH_BIT) == 0)
         {
             value = (first >> 1);
             offset[0] += 1;
         }
         else if ((first & SEVENTH_BIT) == 0)
         {
             value = read(buff, offset[0], 2)>>2;
             offset[0] += 2;
         }
         else if ((first & 31) != 31) //last five bits are not all "1"
         {
             if ((first & SIXTH_BIT) == 0)
             {
                 value = read(buff, offset[0], 3)>>3;
                 offset[0] += 3;
             }
             else if ((first & FIFTH_BIT) == 0)
             {
                 value = read(buff, offset[0], 4)>>4;
                 offset[0] += 4;
             }
             else
             {
                 value = read(buff, offset[0], 5)>>5;
                 offset[0] += 5;
             }
         }
         else
         {
             if ((first & THIRD_BIT) == 0)
             {
                 value = read(buff, offset[0], 6)>>6;
                 offset[0] += 6;
             }
             else if ((first & SECOND_BIT) == 0)
             {
                 value = read(buff, offset[0], 7)>>7;
                 offset[0] += 7;
             }
             else
             {
                 value = read(buff, offset[0]+1, 8);
                 offset[0] += 9;
             }
         }

         return value;
     }


     //write value into buff started at offset, and also write the value size info
     public static int writeValue(byte[] buff, long value, int offset)
     {
         if (value >= -64 && value <= 63) //2^6
         {
             buff[offset] = (byte)(value<<1);
             return 1;
         }
         else if (value >= -8192 && value <= 8191) //2^13
         {
             write(buff, offset, 2, (value<<2) | 1);
             return 2;
         }
         else if (value >= -17179869184L && value <= 17179869183L) //2^34
         {
             if (value >= -1048576 && value <= 1048575) //2^20
             {
                 write(buff, offset, 3, (value<<3) | 3);
                 return 3;
             }
             else if (value >= -134217728 && value <= 134217727) //2^27
             {
                 write(buff, offset, 4, (value<<4) | 7);
                 return 4;
             }
             else
             {
                 write(buff, offset, 5, (value<<5) | 15);
                 return 5;
             }
         }
         else
         {
             if (value >= -2199023255552L && value <= 2199023255551L) //2^41
             {
                 write(buff, offset, 6, (value<<6) | 31);
                 return 6;
             }
             else if (value >= -281474976710656L && value <= 281474976710655L) //2^48
             {
                 write(buff, offset, 7, (value<<7) | 63);
                 return 7;
             }
             else
             {
                 buff[offset] = -1;
                 write(buff, offset+1, 8, value);
                 return 9;
             }
         }
     }

     //get byte size for specified value
     public static int getValueLength(long value)
     {
         if (value >= -64 && value <= 63) //2^6
         {
             return 1;
         }
         else if (value >= -8192 && value <= 8191) //2^13
         {
             return 2;
         }
         else if (value >= -17179869184L && value <= 17179869183L) //2^34
         {
             if (value >= -1048576 && value <= 1048575) //2^20
             {
                 return 3;
             }
             else if (value >= -134217728 && value <= 134217727) //2^27
             {
                 return 4;
             }
             else
             {
                 return 5;
             }
         }
         else
         {
             if (value >= -2199023255552L && value <= 2199023255551L) //2^41
             {
                 return 6;
             }
             else if (value >= -281474976710656L && value <= 281474976710655L) //2^48
             {
                 return 7;
             }
             else
             {
                 return 9;
             }
         }
     }

     /**
      * read bytes from buff and convert to long 
      * @param buff [in] 
      * @param offset start index 
      * @param count [in] the size of bytes for the value
      * @return
      */
     private static long read(byte[] buff, int offset, int count)
     {
         int mask = 0xFF;
         long value = 0;
         for (int i=0; i<count - 1; i++)
         {
             value += (mask & (long)buff[offset+i]) << (i * 8); 
         }
         value += (long)buff[offset+count-1] << ((count -1)*8);
         return value;
     }

     //write bytes into buff
     private static void write(byte[] buff, int offset, int count, long value)
     {
         for (int i=0; i<count; i++)
         {
             buff[offset+i] = (byte)(value & 0xFF);
             value >>= 8;
         }
     }

     /**
      * Checks if the value is within "md5 precision" distance of illegal integer value.
      * Note that had to check for -1 and 0 first, since Math.abs(MIN_VALUE) == MIN_VALUE.
      * 
      * @param value number to check
      * @param message message to add to the exception if check failed
      */
     public static void check(int value, String message)
     {
         if ((value != -1 && Math.abs(value - Integer.MAX_VALUE) < 1000000) ||
                 (value != 0  && Math.abs(value - Integer.MIN_VALUE) < 1000000))
             throw new IllegalArgumentException(message + value);
     }

     private final static String BAD_MINX = "Bad minX: ";
     private final static String BAD_MINY = "Bad minY: ";
     private final static String BAD_MAXX = "Bad maxX: ";
     private final static String BAD_MAXY = "Bad maxY: ";

     /**
      * Builds a bounding box, given boundaries
      * 
      * @param minX minimum x value
      * @param minY minimum y value
      * @param maxX maximum x value
      * @param maxY maximum y value
      * @return an array of 4 elements containing coordinates of 4 corner points of the box
      */
     public static int [][] buildBox4(int minX, int minY, int maxX, int maxY)
     {
         check(minX, BAD_MINX);
         check(maxX, BAD_MAXX);
         check(minY, BAD_MINY);
         check(maxY, BAD_MAXY);

         int[][] box = new int[4][];

         for (int i = 0; i < 4; i++)
         {
             box[i] = new int[2];
         }
         box[0][0] = box[1][0] = Math.min(minX, maxX);
         box[2][0] = box[3][0] = Math.max(maxX, minX);
         box[0][1] = box[3][1] = Math.min(minY, maxY);
         box[1][1] = box[2][1] = Math.max(maxY, minY);

         return box;
     }

     /**
      * Checks if two boxes intersect. The first box is specified
      * as four coordinates; the second - as an array of points.
      * @param minX first box min x
      * @param minY first box min y
      * @param maxX first box max x
      * @param maxY first box max y
      * @param box second box
      * @return true if second box != null and these two have common points
      */
     public static boolean intersect(int minX, int minY, int maxX, int maxY, 
             int[] box)
     {
         if (box == null) return false;

         int boxMinX = box[0];
         int boxMaxX = box[2];
         int boxMinY = box[1];
         int boxMaxY = box[3];

         return minX <= boxMaxX && maxX >= boxMinX && 
         minY <= boxMaxY && maxY >= boxMinY;
     }

     public static int[] computeBoundingBox(int[] points, int nPoints)
     {
         int minX = Integer.MAX_VALUE;
         int minY = Integer.MAX_VALUE;
         int maxX = Integer.MIN_VALUE;
         int maxY = Integer.MIN_VALUE;
         int i2 = 0;

         for (int i = 0; i < nPoints; i++)
         {
             int x = points[i2++];

             if (x < minX)
                 minX = x;

             if (x > maxX)
                 maxX = x;

             int y = points[i2++];

             if (y < minY)
                 minY = y;

             if (y > maxY)
                 maxY = y;
         }

         return new int[]{minX, minY, maxX, maxY};
     }

     public static boolean inRect(int x1, int y1, int x2, int y2, int x, int y)
     {
         if (x >= x1 && x < x2 && y >= y1 && y < y2)
             return true;

         return false;
     }

     public static boolean isRectIntersecting(int x1, int y1, int w1, int h1, 
             int x2, int y2, int w2, int h2)
     {
         int xIntersection = Math.max(x1, x2);
         int yIntersection = Math.max(y1, y2);

         int xMaxInter = Math.min(x1 + w1, x2 + w2);
         int yMaxInter = Math.min(y1 + h1, y2 + h2);
         int wIntersection = xMaxInter - xIntersection;
         int hIntersection = yMaxInter - yIntersection;

         if (wIntersection > 0 && hIntersection > 0)
             return true;

         return false;
     }

     public static boolean isValidBoundingBox(int[] boundingBox)
     {
         boolean isValid = boundingBox[0] >= MIN_LATITUDE && boundingBox[2] <= MAX_LATITUDE &&
         boundingBox[1] >= MIN_LONGITUDE && boundingBox[3] <= MAX_LONGITUDE &&
         boundingBox[0] <= boundingBox[2] && boundingBox[1] <= boundingBox[3];
         return isValid;
     }
     
    public static double degreeToRadian(double d)
    {
        return d * 3.14159265358979323846 / 180.0;
    }
    
    public static double radianToDegree(double r)
    {
        return r * 180.0 / 3.14159265358979323846;
    }
    
    public static boolean isLatLonValid(double lat, double lon)
    {
        boolean result = false;
        if (lat > -90.0d && lat < 90.0d && lon > -180.0d && lon < 180.0d)
        {
            result = true;
        }
        return result;
    }
}
