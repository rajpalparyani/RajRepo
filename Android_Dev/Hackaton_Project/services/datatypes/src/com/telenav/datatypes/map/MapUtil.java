package com.telenav.datatypes.map;

import java.util.Vector;

import com.telenav.datatypes.DataUtil;
import com.telenav.datatypes.GlobalCoordinateUtil;
import com.telenav.logger.Logger;

public abstract class MapUtil
{
    public static int TILE_SIZE_POWER                       = 6;
    
    public static int TILE_PIXEL_SIZE                       = 64;

    public static int MAX_REQUEST_TILE_COUNT                = 24;
    
    public static int MAX_TILE_REQUEST_CACHE_SIZE           = 128;
    
    public static int PNG_MAP_TILE_IMAGE_CACHE_SIZE         = 32;
    
    public static int PNG_MAP_TILE_DATA_CACHE_SIZE          = 128;
    
    public static int PNG_TRAFFIC_TILE_IMAGE_CACHE_SIZE     = 32;
    
    public static int PNG_TRAFFIC_TILE_DATA_CACHE_SIZE      = 128;
    
    public static int TILE_COUNT_ON_SCREEN                  = 12;
    
    public static final int SHIFT = 13;
    
    public static final int RENDER_QFACTOR = 4;

    final static private int POLYGON_POINTS_EXPAND = 30;

    public static byte[] ROAD_TYPES = 
    {    // value should asc
        DataUtil.RAMP,
        DataUtil.HIGHWAYRAMP,
        DataUtil.SMALLSTREET,
        DataUtil.LOCAL_STREET,
        DataUtil.INTERSECTION,
        DataUtil.ARTERIAL,
        DataUtil.HIGHWAY
    };

    public static byte[] POLYGON_TYPE = {  // value should asc
        DataUtil.TURNINDICATOR  ,
        DataUtil.UNKNOWN ,
        DataUtil.GEOPOLITICAL ,
        DataUtil.PARK ,
        DataUtil.CAMPUS ,
        DataUtil.WATER ,
        DataUtil.INTERIOR ,
        DataUtil.BUILDING
    };
    
    private static byte[] roadTypesConvertionTable = new byte[] {
        DataUtil.LOCAL_STREET,             // 0
        DataUtil.RAMP,                     // 1
        DataUtil.HIGHWAY,                  // 2
        DataUtil.INTERSECTION,             // 3
        DataUtil.ARTERIAL,                 // 4
        DataUtil.TRAFFIC_CIRCLE,           // 5
        DataUtil.ROUND_ABOUT,              // 6
        DataUtil.ROUND_ABOUT,              // 7
        DataUtil.ROUND_ABOUT,              // 8
        DataUtil.ROUND_ABOUT,              // 9
        DataUtil.TURNINDICATOR,            // 10
        DataUtil.UNKNOWN,                  // 11
        DataUtil.GEOPOLITICAL,             // 12
        DataUtil.PARK,                     // 13
        DataUtil.CAMPUS,                   // 14
        DataUtil.WATER,                    // 15
        DataUtil.INTERIOR,                 // 16
        DataUtil.BUILDING                  // 17
    };

    public static int[] VECTOR_MAP_DECIMATION_THRESHOLD =
    {
        1, 1, 1, 16, 16, 32, 32, 128, 128, 512, 512, 2048, 2048, 8192, 8192, 32768, 32768, 65536, 65536, 65536
    };
    
    private static int[] polygonPoints = new int[POLYGON_POINTS_EXPAND];
    private static int[] intersectedPoint = new int[2];
    private static int[] edgeRoundHeadPoints = new int[10];
    private static int[] edgeRoundTailPoints = new int[10];
    
    private static int[] decimationTempPoints = new int[100];
    
    public static void setParameters(int screenWidth, int screenHeight, 
            int tileSizePower)
    {
        TILE_SIZE_POWER = tileSizePower;
        TILE_PIXEL_SIZE = 1 << TILE_SIZE_POWER;
        
        if (screenWidth == 0 || screenHeight == 0)
        {
            screenWidth = 480;
            screenHeight = 854;
        }
        int wCount = screenWidth / TILE_PIXEL_SIZE + 1;
        if (screenWidth % TILE_PIXEL_SIZE != 0)
            wCount ++;
        
        int hCount = screenWidth / TILE_PIXEL_SIZE + 1;
        if (screenWidth % TILE_PIXEL_SIZE != 0)
            hCount ++;
        
        TILE_COUNT_ON_SCREEN = MAX_REQUEST_TILE_COUNT = wCount * hCount;
        MAX_TILE_REQUEST_CACHE_SIZE = MAX_REQUEST_TILE_COUNT * 4;
        PNG_MAP_TILE_IMAGE_CACHE_SIZE = TILE_COUNT_ON_SCREEN * 2;
        PNG_MAP_TILE_DATA_CACHE_SIZE = TILE_COUNT_ON_SCREEN * 5;
    }
    
    public static int shiftRight(long m)
    {
        return shiftRight(m, SHIFT);
    }

    public static int shiftRight(long m, int shift)
    {
        return shiftRight(m, shift, true);
    }

    public static int shiftRight(long m, int shift, boolean needRound)
    {
        boolean isNegative;
        long n;
        if (m < 0)
        {
            n = -m;
            isNegative = true;
        }
        else
        {
            n = m;
            isNegative = false;
        }
        long k = n - (n >> shift << shift);
        n >>= shift;
        if (needRound && k >= (1 << (shift-1)))
            n++;
        if (isNegative)
            return (int)(-n);
        else
            return (int)n;
    }

    public static boolean isPolygon(byte roadType)
    {
        return roadType >= DataUtil.TURNINDICATOR && roadType <= DataUtil.BUILDING;
    }
    
    //TODO: duplicate code
    public static int createPolygonForEdge(int[] edgePoints, int numPoints, int d, boolean needRoundEnd)
    {
        if (numPoints <= 1)
            return -1;
        
        short count = 0;
        
        int edgePointsLength = numPoints << 1;
        if (needRoundEnd)
        {
            createEdgeRoundEndPoints(edgePoints[2+DataUtil.LAT], edgePoints[2+DataUtil.LON],
                    edgePoints[DataUtil.LAT], edgePoints[DataUtil.LON], 
                    d, edgeRoundHeadPoints, RENDER_QFACTOR);
            
            createEdgeRoundEndPoints(edgePoints[((numPoints-2)<<1)+DataUtil.LAT], edgePoints[((numPoints-2)<<1)+DataUtil.LON],
                    edgePoints[((numPoints-1)<<1)+DataUtil.LAT], edgePoints[((numPoints-1)<<1)+DataUtil.LON], 
                    d, edgeRoundTailPoints, RENDER_QFACTOR);
        }
        
        for (int i = 0, step = 2;  (step == 2  && i <= edgePointsLength - 2) || 
                                   (step == -2 && i > 0); i+=step)
        {
            if (i == edgePointsLength - 2)
            {
                if (needRoundEnd)
                {
                    // expand polygon points if needed
                    if ( (count + edgeRoundTailPoints.length/2) << 1 > polygonPoints.length)
                    {
                        int[] temp = new int[polygonPoints.length + POLYGON_POINTS_EXPAND];
                        System.arraycopy(polygonPoints, 0, temp, 0, polygonPoints.length);
                        polygonPoints = temp;
                    }
                    
                    //copy the points
                    for (int j=0; j<edgeRoundTailPoints.length; j+=2)
                    {
                        polygonPoints[(count << 1)] = edgeRoundTailPoints[j];
                        polygonPoints[(count << 1)+1] = edgeRoundTailPoints[j+1];
                        count ++;
                    }
                }
                step = - 2;
            }
            
            int lat1 = edgePoints[i + DataUtil.LAT];
            int lon1 = edgePoints[i + DataUtil.LON];
            int lat2 = edgePoints[i + step + DataUtil.LAT];
            int lon2 = edgePoints[i + step + DataUtil.LON];

            // expand polygon points if needed
            if ( (count + 2) << 1 > polygonPoints.length)
            {
                int[] temp = new int[polygonPoints.length + POLYGON_POINTS_EXPAND];
                System.arraycopy(polygonPoints, 0, temp, 0, polygonPoints.length);
                polygonPoints = temp;
            }
            
            calcShiftPoints(lat1, lon1, lat2, lon2, polygonPoints, count, d);
            count += 2; 
            
            if (i >= 2)
            {
                int index = count << 1; 
                lat1 = polygonPoints[index - 8 + DataUtil.LAT];
                lon1 = polygonPoints[index - 8 + DataUtil.LON];
                lat2 = polygonPoints[index - 6 + DataUtil.LAT];
                lon2 = polygonPoints[index - 6 + DataUtil.LON];
                
                int lat3 = polygonPoints[index - 4 + DataUtil.LAT];
                int lon3 = polygonPoints[index - 4 + DataUtil.LON];
                int lat4 = polygonPoints[index - 2 + DataUtil.LAT];
                int lon4 = polygonPoints[index - 2 + DataUtil.LON];
                
                if ( calcLineIntersecting(lon1,lat1,lon2,lat2,lon3,lat3,lon4,lat4, intersectedPoint))
                {
                    polygonPoints[index - 6 + DataUtil.LAT] = intersectedPoint[1];
                    polygonPoints[index - 6 + DataUtil.LON] = intersectedPoint[0];
                    
                    polygonPoints[index - 4 + DataUtil.LAT] = polygonPoints[index - 2 + DataUtil.LAT];
                    polygonPoints[index - 4 + DataUtil.LON] = polygonPoints[index - 2 + DataUtil.LON];
                    
                    count --; 
                } 
                
            }// end if 
            
        }// end for 
        
        if (needRoundEnd)
        {
            // expand polygon points if needed
            if ( (count + edgeRoundHeadPoints.length/2) << 1 > polygonPoints.length)
            {
                int[] temp = new int[polygonPoints.length + POLYGON_POINTS_EXPAND];
                System.arraycopy(polygonPoints, 0, temp, 0, polygonPoints.length);
                polygonPoints = temp;
            }
            //copy the points
            for (int j=0; j<edgeRoundHeadPoints.length; j+=2)
            {
                polygonPoints[(count << 1)] = edgeRoundHeadPoints[j];
                polygonPoints[(count << 1)+1] = edgeRoundHeadPoints[j+1];
                count ++;
            }
        }
        
        return count;
    }
    
    //TODO: duplicate code
    /**
     * create the edge band in counter-clockwise
     * @param edgePoints
     * @param d
     * @param cosLat
     * @return
     */
    public static int createPolygonForEdge(VectorMapEdge e, int d, boolean needRoundEnd)
    {
        int numPoints = e.getShapePointsSize();
        if (numPoints <= 1)
            return -1;
        
        short count = 0;
        
        int edgePointsLength = numPoints << 1;
        if (needRoundEnd)
        {
            createEdgeRoundEndPoints(e.latAt(1), e.lonAt(1),
                    e.latAt(0), e.lonAt(0), 
                    d, edgeRoundHeadPoints, RENDER_QFACTOR);
            
            createEdgeRoundEndPoints(e.latAt(numPoints-2), e.lonAt(numPoints-2),
                    e.latAt(numPoints-1), e.lonAt(numPoints-1), 
                    d, edgeRoundTailPoints, RENDER_QFACTOR);
        }
        
        for (int i = 0, step = 2;  (step == 2  && i <= edgePointsLength - 2) || 
                                   (step == -2 && i > 0); i+=step)
        {
            if (i == edgePointsLength - 2)
            {
                if (needRoundEnd)
                {
                    // expand polygon points if needed
                    if ( (count + edgeRoundTailPoints.length/2) << 1 > polygonPoints.length)
                    {
                        int[] temp = new int[polygonPoints.length + POLYGON_POINTS_EXPAND];
                        System.arraycopy(polygonPoints, 0, temp, 0, polygonPoints.length);
                        polygonPoints = temp;
                    }
                    
                    //copy the points
                    for (int j=0; j<edgeRoundTailPoints.length; j+=2)
                    {
                        polygonPoints[(count << 1)] = edgeRoundTailPoints[j];
                        polygonPoints[(count << 1)+1] = edgeRoundTailPoints[j+1];
                        count ++;
                    }
                }
                step = - 2;
            }
            
            int lat1 = e.latAt(i>>1);
            int lon1 = e.lonAt(i>>1);
            int lat2 = e.latAt((i + step)>>1);
            int lon2 = e.lonAt((i + step)>>1);

            // expand polygon points if needed
            if ( (count + 2) << 1 > polygonPoints.length)
            {
                int[] temp = new int[polygonPoints.length + POLYGON_POINTS_EXPAND];
                System.arraycopy(polygonPoints, 0, temp, 0, polygonPoints.length);
                polygonPoints = temp;
            }
            
            calcShiftPoints(lat1, lon1, lat2, lon2, polygonPoints, count, d);
            count += 2; 
            
            if (i >= 2)
            {
                int index = count << 1; 
                lat1 = polygonPoints[index - 8 + DataUtil.LAT];
                lon1 = polygonPoints[index - 8 + DataUtil.LON];
                lat2 = polygonPoints[index - 6 + DataUtil.LAT];
                lon2 = polygonPoints[index - 6 + DataUtil.LON];
                
                int lat3 = polygonPoints[index - 4 + DataUtil.LAT];
                int lon3 = polygonPoints[index - 4 + DataUtil.LON];
                int lat4 = polygonPoints[index - 2 + DataUtil.LAT];
                int lon4 = polygonPoints[index - 2 + DataUtil.LON];
                
                if ( calcLineIntersecting(lon1,lat1,lon2,lat2,lon3,lat3,lon4,lat4, intersectedPoint))
                {
                    polygonPoints[index - 6 + DataUtil.LAT] = intersectedPoint[1];
                    polygonPoints[index - 6 + DataUtil.LON] = intersectedPoint[0];
                    
                    polygonPoints[index - 4 + DataUtil.LAT] = polygonPoints[index - 2 + DataUtil.LAT];
                    polygonPoints[index - 4 + DataUtil.LON] = polygonPoints[index - 2 + DataUtil.LON];
                    
                    count --; 
                } 
                
            }// end if 
            
        }// end for 
        
        if (needRoundEnd)
        {
            // expand polygon points if needed
            if ( (count + edgeRoundHeadPoints.length/2) << 1 > polygonPoints.length)
            {
                int[] temp = new int[polygonPoints.length + POLYGON_POINTS_EXPAND];
                System.arraycopy(polygonPoints, 0, temp, 0, polygonPoints.length);
                polygonPoints = temp;
            }
            //copy the points
            for (int j=0; j<edgeRoundHeadPoints.length; j+=2)
            {
                polygonPoints[(count << 1)] = edgeRoundHeadPoints[j];
                polygonPoints[(count << 1)+1] = edgeRoundHeadPoints[j+1];
                count ++;
            }
        }
        
        return count;
    }
    
    /**
     * this function must be called after {@link #createPolygonForEdge(int[], int, boolean)}
     * @return
     */
    public static int[] getPolygonPointsForEdge()
    {
        return polygonPoints;
    }
    
    /**
     * create the points of round end counter-clockwise
     */
    private static void createEdgeRoundEndPoints(int startLat, int startLon, int endLat, int endLon, int shiftWidth, int[] points, int qFactor)
    {
        int r360 = 360 << DataUtil.FIX_BITS;
        int angle = DataUtil.atan2(endLat - startLat, endLon - startLon);
        int index = 0;
        for (int i=60; i>=-60; i-=30)
        {
            int angle1 = angle + (i << DataUtil.FIX_BITS);
            if (angle1 < 0)
                angle1 += r360;
            if (angle1 >= r360)
                angle1 -= r360;
            
            long dx = (long)shiftWidth * DataUtil.cos(angle1);
            long dy = (long)shiftWidth * DataUtil.sin(angle1);
            points[(index<<1)+DataUtil.LAT] = (int)((endLat << qFactor) + (dy << qFactor >> DataUtil.DFIX_BITS));
            points[(index<<1)+DataUtil.LON] = (int)((endLon << qFactor) + (dx << qFactor >> DataUtil.DFIX_BITS));
            index ++;
        }        
    }
    
    public static byte convertRoadType(int roadType50)
    {
        return roadTypesConvertionTable[roadType50];
    }

    public static void calcShiftPoints(int lat1, int lon1, int lat2, int lon2,
            int[] points, int count, int shiftWidth)
    {
        lat1 <<= RENDER_QFACTOR;
        lon1 <<= RENDER_QFACTOR;
        lat2 <<= RENDER_QFACTOR;
        lon2 <<= RENDER_QFACTOR;
        
        int dLat = (lat2 - lat1);
        int dLon = (lon2 - lon1);
        int dist = DataUtil.distance(dLat, dLon);
        
        int deltLat = 0;
        int deltLon = 0;

        if (dist != 0)
        {
            deltLat =  (int)( ((long)dLon << RENDER_QFACTOR) * shiftWidth / dist);
            deltLon = -(int)( ((long)dLat << RENDER_QFACTOR) * shiftWidth / dist);
        }
        points[(count << 1) + DataUtil.LAT] = lat1 + deltLat;
        points[(count << 1) + DataUtil.LON] = lon1 + deltLon;

        points[(count << 1) + 2 + DataUtil.LAT] = lat2 + deltLat;
        points[(count << 1) + 2 + DataUtil.LON] = lon2 + deltLon;
    }
    
    public static boolean calcLineIntersecting(int x1, int y1, int x2,
            int y2, int x3, int y3, int x4, int y4,
            int[] intersectedPos) 
    {

        // if two lines intersect, the bouding rects of them should intersect.
        if (Math.min(x1, x2) > Math.max(x3, x4)
                || Math.min(x3, x4) > Math.max(x1, x2)
                || Math.min(y1, y2) > Math.max(y3, y4)
                || Math.min(y3, y4) > Math.max(y1, y2))
        {
            return false;
        }

        // judge if the two lines are directly connected 
        if (((x1 - x3 == 0) && (y1 - y3 == 0))
                || ((x1 - x4 == 0) && (y1 - y4 == 0))) 
        {
            intersectedPos[0] = x1;
            intersectedPos[1] = y1;
            return true;

        } else if (((x2 - x3) == 0 && (y2 - y3) == 0)
                || ((x2 - x4 == 0) && ((y2 - y4) == 0))) 
        {
            intersectedPos[0] = x2;
            intersectedPos[1] = y2;
            return true;
        }

        // strange situation
        if (x1 == x2 && x3 == x4)
            return false;

        // The lines must intersect in this special cases
        // as we have checked whether the bouding rects intersect already
        if (x1 == x2) 
        {
            intersectedPos[0] = x1;
            intersectedPos[1] = 
                y3 + (int)(((long)y4 - y3) * ((long)x1 - x3)/ ((long)x4 - x3));

            return true;

        } else if (x3 == x4) 
        {
            intersectedPos[0] = x3;
            intersectedPos[1] = 
                y1 + (int)(((long)y2 - y1) * ((long)x3 - x1) / ((long)x2 - x1));
            return true;
        }

        //calculate slopes for each line
        long k1 = ((long)(y1 - y2) << 8) / (x1 - x2);
        long k2 = ((long)(y3 - y4) << 8) / (x3 - x4);

        if (k1 == k2)
            return false;

        long b1 = ((long)y1 << 8) - k1 * x1;
        long b2 = ((long)y3 << 8) - k2 * x3;

        // the two lines will intersect at point (x,y) 
        long x = ((long)(b2 - b1) << 8) / (k1 - k2);
        long x0 = x >> 8;
        if (x0 >= Math.min(x1, x2) && x0 <= Math.max(x1, x2)) 
        {
            int y = (int)(( (x * k1 >> 8) + b1) >> 8);
            intersectedPos[0] = (int)x0;
            intersectedPos[1] = y;

            return true;

        } else
            return false;

    }
    /**
     *  
     * @param lat   earth lat
     * @param lon   earth lon
     * @param radius
     * @return
     */
    
    public static TileArray getTilesArroundLocation(int lat, int lon, int radius) 
    {
        int minLat = lat - radius;
        int maxLat = lat + radius;
        int minLon = lon - radius;
        int maxLon = lon + radius;
        return MapUtil.computeTileArrayWithEarthCoordinate(new int[][]{{minLat, maxLon}, {maxLat, maxLon}, {maxLat, minLon}, {minLat, minLon}}, 2, false);
    }
    /**
     * 
     * @param points earth coordinate
     * @param zoom
     * @param isYaxisPointUp
     * @return
     */
    
    public static TileArray computeTileArrayWithEarthCoordinate(int[][] points, int zoom, boolean isYaxisPointUp)
    {
        for(int i = 0;i<points.length; i++)
        {
            points[i][DataUtil.LAT] = GlobalCoordinateUtil.earthLatToGlobal(points[i][DataUtil.LAT]);
            points[i][DataUtil.LON] = GlobalCoordinateUtil.earthLonToGlobal(points[i][DataUtil.LON]);
        }
        return computeTileArray(points , zoom, isYaxisPointUp);
    }
    
    
    /**
     * 
     * @param points  must be the global lat lon
     * @param zoom
     * @param isYaxisPointUp
     * @return
     */
    public static TileArray computeTileArray(int[][] points, int zoom, boolean isYaxisPointUp)
    {
        if (isYaxisPointUp)
        {
            //the axis Y is from bottom to top in "points", we need to convert it to the one from top to bottom,
            //which is same as the tile id coordination.
            int[][] points2 = new int[4][2];
            for (int i=0; i<4; i++)
            {
                points2[i][DataUtil.LAT] = GlobalCoordinateUtil.GLOBAL_LENGTH - points[i][DataUtil.LAT];
                points2[i][DataUtil.LON] = points[i][DataUtil.LON];
            }
        
            points = points2;
        }
        
        //find left and right X index
        int leftX = Integer.MAX_VALUE;
        int rightX = Integer.MIN_VALUE;
        for (int i=0; i<points.length; i++)
        {
            if (points[i][DataUtil.LON] < leftX)
                leftX = points[i][DataUtil.LON];
            if (points[i][DataUtil.LON] > rightX)
                rightX = points[i][DataUtil.LON];
        }
        int leftXIndex = calcGlobalIndex(leftX, zoom);
        int rightXIndex = calcGlobalIndex(rightX, zoom);
        
        //allocate space to store the tiles range
        //index 0 - flag, 0 means empty, 1 means not empty
        //index 1 - top tile y index
        //index 2 - bottom tile y index
        int[][] columns = new int[rightXIndex - leftXIndex + 1][3];
        
        //calcuate the top and bottom y indexes for each x index
        findTopBottom(points[0][DataUtil.LON], points[0][DataUtil.LAT], 
                points[1][DataUtil.LON], points[1][DataUtil.LAT], columns, zoom, leftXIndex);
        findTopBottom(points[1][DataUtil.LON], points[1][DataUtil.LAT], 
                points[2][DataUtil.LON], points[2][DataUtil.LAT], columns, zoom, leftXIndex);
        findTopBottom(points[2][DataUtil.LON], points[2][DataUtil.LAT], 
                points[3][DataUtil.LON], points[3][DataUtil.LAT], columns, zoom, leftXIndex);
        findTopBottom(points[3][DataUtil.LON], points[3][DataUtil.LAT], 
                points[0][DataUtil.LON], points[0][DataUtil.LAT], columns, zoom, leftXIndex);
        
        for (int i=0; i<columns.length; i++)
            columns[i][0] = leftXIndex + i;
        
        return MapDataFactory.getInstance().createTileArray(columns, zoom);
    }
    
    private static void findTopBottom(int startX, int startY, int endX, int endY, int[][] columns, int zoom, int leftXIndex)
    {
        if (Math.abs(endX - startX) > Math.abs(endY - startY))
        {
            if (startX > endX)
            {
                int temp = startX;
                startX = endX;
                endX = temp;
                temp = startY;
                startY = endY;
                endY = temp;
            }
            
            int startXIndex = calcGlobalIndex(startX, zoom);
            int startYIndex = calcGlobalIndex(startY, zoom);
            addTile(startXIndex-leftXIndex, startYIndex, columns);
            
            int endXIndex = calcGlobalIndex(endX, zoom);
            int endYIndex = calcGlobalIndex(endY, zoom);
            addTile(endXIndex-leftXIndex, endYIndex, columns);
            
            for (int i=startXIndex+1; i<=endXIndex; i++)
            {
                int globalX = calcGlobalFromIndex(i, zoom);
                int globalY = (int)((long)(globalX - startX) * (endY - startY) / (endX - startX)) + startY;
                int yIndex = calcGlobalIndex(globalY, zoom);
                addTile(i-1-leftXIndex, yIndex, columns);
                addTile(i-leftXIndex, yIndex, columns);
            }
        }
        else
        {
            if (startY > endY)
            {
                int temp = startX;
                startX = endX;
                endX = temp;
                temp = startY;
                startY = endY;
                endY = temp;
            }
            
            int startXIndex = calcGlobalIndex(startX, zoom);
            int startYIndex = calcGlobalIndex(startY, zoom);
            addTile(startXIndex-leftXIndex, startYIndex, columns);
            
            int endXIndex = calcGlobalIndex(endX, zoom);
            int endYIndex = calcGlobalIndex(endY, zoom);
            addTile(endXIndex-leftXIndex, endYIndex, columns);
            
            for (int i=startYIndex+1; i<=endYIndex; i++)
            {
                int globalY = calcGlobalFromIndex(i, zoom);
                int globalX = (int)((long)(globalY - startY) * (endX - startX) / (endY - startY)) + startX;
                int xIndex = calcGlobalIndex(globalX, zoom);
                addTile(xIndex-leftXIndex, i-1, columns);
                addTile(xIndex-leftXIndex, i, columns);
            }
        }
    }
    
    private static void addTile(int index, int yIndex, int[][] columns)
    {
        if (columns[index][0] == 0)
        {
            columns[index][0] = 1;
            columns[index][1] = yIndex;
            columns[index][2] = yIndex;
        }
        else
        {
            if (yIndex < columns[index][1])
                columns[index][1] = yIndex;
            if (yIndex > columns[index][2])
                columns[index][2] = yIndex;
        }
        
    }
    
    public static int getMaxZoom()
    {
        return 25 - TILE_SIZE_POWER;
    }

    /**
     * 
     * @param points
     * @param distThreshold decimation threshold in DM5
     * @param p1 temporary array for decimation calculation, must be int[3]
     * @param p2 temporary array for decimation calculation, must be int[2]
     * @return
     */
    public static void decimatePoints(Vector points, int distThreshold, int[] p1, int[] p2)
    {
        if (points.size() <= 2)
            return;
        
        try{
            int startIndex = 0;
            int endIndex = 2;
            while(true)
            {
                if (endIndex >= points.size())
                    break;
                
                int[] startPoint = (int[])points.elementAt(startIndex);
                int[] endPoint = (int[])points.elementAt(endIndex);
                p1[0] = endPoint[DataUtil.LAT] - startPoint[DataUtil.LAT];
                p1[1] = endPoint[DataUtil.LON] - startPoint[DataUtil.LON];
                int cosLat = DataUtil.getCosLat(endPoint[DataUtil.LAT]);
                p1[2] = DataUtil.gpsDistance(p1[0], p1[1], cosLat);
                boolean isAcc = true;
                
                for (int i=startIndex+1; i<endIndex; i++)
                {
                    int[] latlon = (int[])points.elementAt(i);
                    p2[0] = latlon[DataUtil.LAT] - startPoint[DataUtil.LAT];
                    p2[1] = latlon[DataUtil.LON] - startPoint[DataUtil.LON];
                    int dist = Math.abs(DataUtil.cross(p1, p2, cosLat));
                    if (dist > distThreshold)
                    {
                        isAcc = false;
                        break;
                    }
                }
                
                if (isAcc && endIndex < points.size()-1)
                {
                    endIndex ++;
                }
                else
                {
                    if (!isAcc)
                    {
                        endIndex--;
                    }
                    for (int i=startIndex+1; i<endIndex; i++)
                    {
                        int[] temp = (int[])points.elementAt(i);
                        temp[0] = temp[1] = 0;
                    }
                    startIndex = endIndex;
                    endIndex = startIndex + 2;
                }
            }
            
            for (int i=points.size()-1; i>= 0; i--)
            {
                int[] temp = (int[])points.elementAt(i);
                if (temp[0] == 0 && temp[1] == 0)
                    points.removeElementAt(i);
            }
        
        }catch(Exception e)
        {
            Logger.log(MapUtil.class.getName(), e);
        }
    }

    /**
     * 
     * @param points
     * @param numPonits
     * @param distThreshold decimation threshold in DM5
     * @param p1 temporary array for decimation calculation, must be int[3]
     * @param p2 temporary array for decimation calculation, must be int[2]
     * @return the decimated number of points
     */
    public static int decimateGlobalPoints(int[] points, int numPoints, int distThreshold, int[] p1, int[] p2)
    {
        if (numPoints <= 2)
            return numPoints;
        
        try{
            int startIndex = 0;
            int endIndex = 2;
            while(true)
            {
                if (endIndex >= numPoints)
                    break;
                
                int startPointLat = points[(startIndex<<1) + DataUtil.LAT];
                int startPointLon = points[(startIndex<<1) + DataUtil.LON];
                int endPointLat = points[(endIndex<<1) + DataUtil.LAT];
                int endPointLon = points[(endIndex<<1) + DataUtil.LON];
                p1[0] = endPointLat - startPointLat;
                p1[1] = endPointLon - startPointLon;
                int cosLat = DataUtil.getCosLat(0); //it's already global coordinates
                p1[2] = DataUtil.distance(p1[0], p1[1]);
                boolean isAcceptable = true;
                
                for (int i=startIndex+1; i<endIndex; i++)
                {
                    int lat = points[(i<<1) + DataUtil.LAT];
                    int lon = points[(i<<1) + DataUtil.LON];
                    p2[0] = lat - startPointLat;
                    p2[1] = lon - startPointLon;
                    int dist = Math.abs(DataUtil.cross(p1, p2, cosLat));
                    if (dist > distThreshold)
                    {
                        isAcceptable = false;
                        break;
                    }
                }
                
                if (isAcceptable && endIndex < (points.length>>1)-1)
                {
                    endIndex ++;
                }
                else
                {
                    if (!isAcceptable)
                    {
                        endIndex--;
                    }
                    for (int i=startIndex+1; i<endIndex; i++)
                    {
                        points[(i<<1)] = 0;
                        points[(i<<1) + 1] = 0;
                    }
                    startIndex = endIndex;
                    endIndex = startIndex + 2;
                }
            }
            
            int count = 0;
            for (int i=0; i<(numPoints<<1)-1; i+=2)
            {
                if (points[i] != 0 || points[i+1] != 0)
                    count ++;
            }
            if (count == numPoints)
                return numPoints;
            
            int offset = 0;
            for (int i=0; i<(count<<1)-1; i+=2)
            {
                while (points[i+offset] == 0 && points[i+offset+1] == 0)
                {
                    offset += 2;
                }
                if (offset == 0)
                    continue;
                
                points[i] = points[i+offset];
                points[i+1] = points[i+offset+1];
                points[i+offset] = 0;
                points[i+offset+1] = 0;
            }

            return count;
        }catch(Exception e)
        {
            Logger.log(MapUtil.class.getName(), e);
        }
        return -1;
    }
    
    public static void decimateMapTile(MapTile tile)
    {
        VectorMapEdge[] edges = tile.getMapEdges();
        if (edges == null)
            return;
        
        int[] p1 = new int[3];
        int[] p2 = new int[2];
        for (int i=0; i<edges.length; i++)
        {
            VectorMapEdge e = edges[i];
            int numPoints = e.getShapePointsSize();
            if (decimationTempPoints.length < numPoints << 1)
            {
                decimationTempPoints = new int[numPoints << 1];
            }
            for (int j=0; j<numPoints; j++)
            {
                decimationTempPoints[(j<<1)+DataUtil.LAT] = e.latAt(j);
                decimationTempPoints[(j<<1)+DataUtil.LON] = e.lonAt(j);
            }
            int decimatedNumPoints = decimateGlobalPoints(decimationTempPoints, numPoints, 
                    VECTOR_MAP_DECIMATION_THRESHOLD[tile.getZoom()], 
                    p1, p2);
            if (decimatedNumPoints != -1 && decimatedNumPoints != numPoints)
            {
                int[] decimatedPoints = new int[decimatedNumPoints << 1];
                System.arraycopy(decimationTempPoints, 0, decimatedPoints, 0, decimatedNumPoints << 1);
                e.setShapePoints(decimatedPoints);
            }
        }
    }
    
    public static int[] buildBox(int[] points, int numPoints)
    {
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;
        for (int i=0; i<(numPoints<<1)-1; i+=2)
        {
            int x = points[i+DataUtil.LON];
            int y = points[i+DataUtil.LAT];
            if (minX > x)
                minX = x;
            if (minY > y)
                minY = y;
            if (maxX < x)
                maxX = x;
            if (maxY < y)
                maxY = y;
        }
        return new int[]{minY, minX, maxY, maxX};
    }

    /**
     * calculate the higher zoom tile id from lower zoom tile id
     * @param lowerZoomTileId
     * @param lowZoom
     * @param highZoom
     * @return
     */
    public static long getHigherZoomTileId(long lowerZoomTileId, int lowZoom, int highZoom)
    {
       if (lowZoom > highZoom)
           return Long.MIN_VALUE;
       if (lowZoom == highZoom)
           return lowerZoomTileId;
       
       int lonIndex = calcGlobalXIndex(lowerZoomTileId);
       int latIndex = calcGlobalYIndex(lowerZoomTileId);
       
       lonIndex >>= (highZoom - lowZoom);
       latIndex >>= (highZoom - lowZoom);
       return composeID(latIndex, lonIndex, highZoom);
    }

    public static long composeID(int latIndex, int lonIndex, int zoom)
    {
        return ((long)zoom << 48) | ((long)lonIndex << 24) | latIndex; 
    }

    public static int getZoomFromID(long id)
    {
        return (int)((id >> 48) & 0xFF);
    }

    /**
     * 
     * @param global :
     *            can be global x coordinate or y coordintate
     * @return
     */
    public static int calcGlobalIndex(int global , int zoom)
    {       
        return global >> (TILE_SIZE_POWER + zoom);
    }
    
    public static int calcGlobalFromIndex(int index, int zoom)
    {
        return index << (TILE_SIZE_POWER + zoom);
    }

    public static int calcGlobalXIndex(long tileID)
    {
        long xIndex = (tileID >> 24) & 0xFFFFFF;
        return (int)xIndex;
    }
    
    public static int calcGlobalYIndex(long tileID)
    {
        long yIndex = tileID & 0xFFFFFF;
        return (int)yIndex;
    }
    
    public static int calcGlobalCenterX(long tileID, int zoom)
    {
        long xIndex = (tileID >> 24) & 0xFFFFFF;

        long tempZoom = (tileID >> 48) & 0xFF;
        if (tempZoom != 0 && tempZoom != zoom)
        {
            Logger.log(Logger.INFO, MapUtil.class.getName(), "zoom is wrong in calcGlobalCenterX()");
        }
        
        long centerX = ((2 * xIndex + 1) * GlobalCoordinateUtil.GLOBAL_LENGTH) >> (25 - TILE_SIZE_POWER + 1 - zoom);
        return (int)centerX;
    }
    
    public static int calcGlobalCenterY(long tileID, int zoom)
    {
        long yIndex = tileID & 0xFFFFFF;
        
        long tempZoom = (tileID >> 48) & 0xFF;
        if (tempZoom != 0 && tempZoom != zoom)
        {
            Logger.log(Logger.INFO, MapUtil.class.getName(), "zoom is wrong in calcGlobalCenterY()");
        }
        
        long centerY = ((2 * yIndex + 1) * GlobalCoordinateUtil.GLOBAL_LENGTH) >> (25 - TILE_SIZE_POWER + 1 - zoom);
        return (int)centerY;
    }

    public static int getTileCount(int zoom)
    {
        return GlobalCoordinateUtil.GLOBAL_LENGTH >> (zoom + TILE_SIZE_POWER);
    }

}

