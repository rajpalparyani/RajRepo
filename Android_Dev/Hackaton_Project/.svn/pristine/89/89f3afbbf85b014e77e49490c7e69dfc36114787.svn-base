package com.telenav.module.nav.movingmap;

import java.util.Vector;

import com.telenav.datatypes.DataUtil;
import com.telenav.datatypes.GlobalCoordinateUtil;
import com.telenav.datatypes.map.MapTile;
import com.telenav.datatypes.map.MapUtil;
import com.telenav.datatypes.map.VectorMapEdge;
import com.telenav.datatypes.nav.TnNavLocation;

class MapTileAdiStatusJudger
{
    // judging result indicates current GPS location is not on any roads for sure
    public static final int STATUS_ADI = 0;

    // judging result indicates current GPS location is on one road for sure
    public static final int STATUS_ON_ROAD = 1;

    // there is no enough map tiles data to make ADI status judgment
    public static final int STATUS_NO_DATA = 2;

    // add for lineClip method
    static final int TOP = 0x1;

    static final int BOTTOM = 0x2;

    static final int RIGHT = 0x4;

    static final int LEFT = 0x8;

    // x or y of point
    static final int POINT_X = 0;

    static final int POINT_Y = 1;

    private Vector tiles;

    public void setMapTiles(Vector mapTiles)
    {
        this.tiles = mapTiles;
    }

    public void addMapTiles(Vector addedMapTile)
    {
        if (addedMapTile != null && addedMapTile.size() > 0)
        {
            tiles.addAll(addedMapTile);
            this.setMapTiles(tiles);
        }
    }

    /**
     * judge current GPS is on road , in ADI status or there is no enough map tile data to make judgment
     */
    public int isOnRoad(TnNavLocation gpsFix, int[] adiThresholds, boolean ignoreHeading)
    {
        if (tiles == null || tiles.size() == 0 || gpsFix == null)
            return STATUS_NO_DATA;

        int lat = gpsFix.getLatitude();
        int lon = gpsFix.getLongitude();

        // four values for each adiThreshold in earth coordinate
        // 0: lat + adiThreshold, 1: lat - adiThreshold, 2: lon + adiThreshold, 3: lon - adiThreshold
        int[][] latLon = new int[adiThresholds.length][4];
        int cosLat = DataUtil.getCosLat(lat);
        if (cosLat == 0)
            cosLat = 1;
        for (int i = 0; i < adiThresholds.length; i++)
        {
            // convert the thresholds to longitude
            int adiThresholdsInLon = (int) (((long) adiThresholds[i] << DataUtil.SHIFT) / cosLat);

            latLon[i][0] = lat + adiThresholds[i];
            latLon[i][1] = lat - adiThresholds[i];
            latLon[i][2] = lon + adiThresholdsInLon;
            latLon[i][3] = lon - adiThresholdsInLon;
        }

        boolean isInsideAnyMapTile = false;
        for (int i = 0; i < tiles.size(); i++)
        {
            // check if location is inside the bounding box of the map tile first
            MapTile tile = (MapTile) tiles.elementAt(i);
            int globalLonCenter = MapUtil.calcGlobalCenterX(tile.getId(), tile.getZoom());
            int globalLatCenter = MapUtil.calcGlobalCenterY(tile.getId(), tile.getZoom());
            int tileWidth = 1 << (MapUtil.TILE_SIZE_POWER + tile.getZoom());
            int minLat = GlobalCoordinateUtil.globalToEarthLat(globalLatCenter + tileWidth / 2);
            int maxLat = GlobalCoordinateUtil.globalToEarthLat(globalLatCenter - tileWidth / 2);
            int minLon = GlobalCoordinateUtil.globalToEarthLon(globalLonCenter - tileWidth / 2);
            int maxLon = GlobalCoordinateUtil.globalToEarthLon(globalLonCenter + tileWidth / 2);

            boolean isInside = false;
            for (int j = 0; j < adiThresholds.length; j++)
            {
                if (latLon[j][0] > minLat && latLon[j][1] < maxLat && latLon[j][2] > minLon && latLon[j][3] < maxLon)
                {
                    isInside = true;
                    break;
                }
            }

            if (isInside)
                isInsideAnyMapTile = true;
            else
                continue;

            VectorMapEdge[] edges = tile.getMapEdges();
            if (edges == null)
                continue;

            int edgeSize = edges.length;
            for (int j = 0; j < edgeSize; j++)
            {
                VectorMapEdge edge = edges[j];
                if (edge.getRoadType() < DataUtil.RAMP || edge.getRoadType() > DataUtil.HIGHWAY)
                {
                    if (edge.getRoadType() != DataUtil.ROUND_ABOUT)
                        continue;
                }

                int adiThresholdIndex = 0;
                if (edge.getRoadType() == DataUtil.ARTERIAL)
                    adiThresholdIndex = 1;
                else if (edge.getRoadType() == DataUtil.HIGHWAY)
                    adiThresholdIndex = 2;

                if (findMeOnEdge(gpsFix, edge, adiThresholds[adiThresholdIndex], ignoreHeading))
                {
                    return STATUS_ON_ROAD;
                }
            }// edn for
        }// end for

        if (!isInsideAnyMapTile)
            return STATUS_NO_DATA;

        return STATUS_ADI;
    }

    /**
     * Find me on one edge of map tiles.
     * 
     * @return
     */
    private boolean findMeOnEdge(TnNavLocation gpsFix, VectorMapEdge tileEdge, int nThreshold, boolean ignoreHeading)
    {
        int lat = gpsFix.getLatitude();
        int lon = gpsFix.getLongitude();

        int[] point1 = new int[2];
        int[] point2 = new int[2];
        int[] leftTop = new int[2];
        int[] rightBotton = new int[2];

        int realX = 0, realY = 0;
        int realX2 = 0, realY2 = 0;

        if (null == tileEdge)
        {
            return false;
        }

        leftTop[POINT_X] = -nThreshold;
        leftTop[POINT_Y] = -nThreshold;

        rightBotton[POINT_X] = nThreshold;
        rightBotton[POINT_Y] = nThreshold;

        int size = tileEdge.getShapePointsSize();
        if (size < 2)
            return false;

        for (int i = 0; i < size - 1; i++)
        {
            realY = tileEdge.latAt(i);
            realX = tileEdge.lonAt(i);

            realY2 = tileEdge.latAt(i + 1);
            realX2 = tileEdge.lonAt(i + 1);

            point1[POINT_X] = realX - lon;
            point1[POINT_Y] = lat - realY;

            point2[POINT_X] = realX2 - lon;
            point2[POINT_Y] = lat - realY2;

            if (lineClip(point1, point2, leftTop, rightBotton))
            {
                int x1 = point1[POINT_X];
                int y1 = point1[POINT_Y];
                int x2 = point2[POINT_X];
                int y2 = point2[POINT_Y];

                int nCrossProduct = x1 * y2 - x2 * y1;
                nCrossProduct = nCrossProduct * nCrossProduct;

                int nP12P2 = (x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2);
                if (0 == nP12P2 || (nCrossProduct / nP12P2) <= (nThreshold * nThreshold))
                {
                    if (ignoreHeading || gpsFix.getSpeed() < gpsFix.getMinUsableSpeed())
                    {
                        return true;
                    }
                    else
                    {
                        int edgeHeading = DataUtil.bearing(realY, realX, realY2, realX2);
                        int headingThresh = 64;

                        int headingDiff = Math.abs(gpsFix.getHeading() - edgeHeading);
                        if (headingDiff < headingThresh || (180 - headingDiff) < headingThresh)
                        {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    /*
     * cohen-sutherland line clip arithmetric @param p1: [in/out] line start point and end point @param p2: [in/out]
     * line start point and end point @param leftTop: [in] the lefttop clip region @param rightBottom: [in] the
     * rightBottom clip region @return: FALSE if both the p1 and p2 are out of clip region after operation, otherwise
     * TRUE
     */
    private boolean lineClip(int[] p1, int[] p2, int[] leftTop, int[] rightBottom)
    {
        int outcode0, outcode1, outcodeOut;
        boolean accept = false, done = false;

        outcode0 = compOutCode(p1, leftTop, rightBottom);
        outcode1 = compOutCode(p2, leftTop, rightBottom);

        do
        {
            if (0 == (outcode0 | outcode1))
            { // outcode0 == 0 and outcode1 == 0
              // both p1 and p2 are within the clip rectangle
                accept = true;
                done = true;
            }
            else if (0 != (outcode0 & outcode1))
                // outcode0 > 0 and outcode1 > 0
                // both p1 and p2 are out of the clip rectangle, and in the same area
                done = true;
            else
            {
                int x, y;
                boolean bool = (0 != outcode0); // c++ : 0 -> false ; other -> true
                outcodeOut = bool ? outcode0 : outcode1;
                if (0 != (outcodeOut & TOP))
                {
                    x = p1[POINT_X] + (p2[POINT_X] - p1[POINT_X]) * (rightBottom[POINT_Y] - p1[POINT_Y])
                            / (p2[POINT_Y] - p1[POINT_Y]);
                    y = rightBottom[POINT_Y];
                }
                else if (0 != (outcodeOut & BOTTOM))
                {
                    x = p1[POINT_X] + (p2[POINT_X] - p1[POINT_X]) * (leftTop[POINT_Y] - p1[POINT_Y])
                            / (p2[POINT_Y] - p1[POINT_Y]);
                    y = leftTop[POINT_Y];
                }
                else if (0 != (outcodeOut & RIGHT))
                {
                    y = p1[POINT_Y] + (p2[POINT_Y] - p1[POINT_Y]) * (rightBottom[POINT_X] - p1[POINT_X])
                            / (p2[POINT_X] - p1[POINT_X]);
                    x = rightBottom[POINT_X];
                }
                else
                {
                    y = p1[POINT_Y] + (p2[POINT_Y] - p1[POINT_Y]) * (leftTop[POINT_X] - p1[POINT_X])
                            / (p2[POINT_X] - p1[POINT_X]);
                    x = leftTop[POINT_X];
                }
                if (outcodeOut == outcode0)
                {
                    p1[POINT_X] = x;
                    p1[POINT_Y] = y;
                    outcode0 = compOutCode(p1, leftTop, rightBottom);
                }
                else
                {
                    p2[POINT_X] = x;
                    p2[POINT_Y] = y;
                    outcode1 = compOutCode(p2, leftTop, rightBottom);
                }
            }
        } while (false == done);

        return accept;

    }

    /*
     * computer out code which is the region where point is at clip rectangle @param p: the point to compute @param
     * leftTop,rightBottom: clip rectangle @return: the combination of LEFT, RIGHT, TOP, BOTTOM say: if the point is at
     * the LEFT of clip rectangle, return LEFT, but if at the TOP of clip rectangle also, return LEFT | TOP;
     */
    private int compOutCode(int[] p, int[] leftTop, int[] rightBottom)
    {
        int outcode = 0;
        if (p[POINT_Y] > rightBottom[POINT_Y])
            outcode |= TOP;
        else if (p[POINT_Y] < leftTop[POINT_Y])
            outcode |= BOTTOM;
        if (p[POINT_X] > rightBottom[POINT_X])
            outcode |= RIGHT;
        else if (p[POINT_X] < leftTop[POINT_X])
            outcode |= LEFT;

        return outcode;
    }

}
