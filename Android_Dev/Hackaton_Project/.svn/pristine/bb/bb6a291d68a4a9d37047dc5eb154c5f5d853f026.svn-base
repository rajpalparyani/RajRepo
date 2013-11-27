package com.telenav.mapcontent;

import java.util.Vector;

import com.telenav.datatypes.DataUtil;
import com.telenav.datatypes.GlobalCoordinateUtil;
import com.telenav.datatypes.Node;
import com.telenav.datatypes.NodeFactory;
import com.telenav.datatypes.map.MapDataFactory;
import com.telenav.datatypes.map.MapTile;
import com.telenav.datatypes.map.MapUtil;
import com.telenav.datatypes.map.Tile;
import com.telenav.datatypes.map.TileMark;
import com.telenav.datatypes.map.TrafficTile;
import com.telenav.datatypes.map.VectorMapEdge;
import com.telenav.datatypes.traffic.TrafficDataFactory;
import com.telenav.datatypes.traffic.VectorTrafficEdge;
import com.telenav.datatypes.traffic.TrafficIncident;

/*
 * Streamer is able to create requests and parse responses for PNG map, PNG traffic, 
 * vector map, vector traffic
 */
public class Streamer extends AbstractStreamer
{
    public static final String ACT_MAP_TILE = "Map";

    public Streamer(int actionId)
    {
        super(actionId);
    }

    public String getActionString()
    {
        return ACT_MAP_TILE;
    }
    
    public byte[] createServerRequest(long[] tileIds)
    {
        int action = getActionId();

        Node mapNode = NodeFactory.getInstance().createNode();
        mapNode.addString(ACT_MAP_TILE);

        boolean isAdded = false;

        for (int i = 0; i < tileIds.length; i++)
        {
            buildRequestNode(action, tileIds[i], mapNode);
            isAdded = true;
        }

        Node reqNode = createTilesNode(mapNode);

        if (!isAdded)
        {
            return null;
        }
        else
        {
            return reqNode.toBinary();
        }
    }

    public Tile[] parseServerResponse(byte[] response)
    {
        Node rootNode = NodeFactory.getInstance().createNode(response, 0);
        if (rootNode.getChildrenSize() == 0)
            return null;
        Node child = rootNode.getChildAt(0);
        if (child.getChildrenSize() == 0)
        {
            Tile[] tiles = new Tile[1];
            if (child.getBinaryData() != null)
            {
                Tile tile = parseTileNode(child);
                tiles[0] = tile;
            }
            return tiles;
        }
        else
        {
            Tile[] tiles = new Tile[child.getChildrenSize()];
            for (int i = 0; i < child.getChildrenSize(); i++)
            {
                Node tileNode = child.getChildAt(i);
                tiles[i] = parseTileNode(tileNode);
            }

            return tiles;
        }
    }

    protected Tile parseTileNode(Node tileNode)
    {
        if(tileNode == null)
            return null;

        int action = (int)tileNode.getValueAt(0);

        if(tileNode.getChildrenSize() < 1 && tileNode.getStringsSize() > 0)
        {
            System.out.println("------wrong tile node response------");
            System.out.println(tileNode.toString());
        }

        long tileID = tileNode.getValueAt(1);
        int zoom = (int) tileNode.getValueAt(2);
        int pixel = (int) tileNode.getValueAt(3);

        byte[] data = tileNode.getBinaryData();

        if(pixel == 0)
        {
            pixel = MapUtil.TILE_PIXEL_SIZE;
        }

        if (isTrafficStreamer())
        {
            TrafficTile tile = MapDataFactory.getInstance().createTrafficTile();
            tile.setId(tileID);
            tile.setZoom(zoom);
            tile.setBinary(data);
            tile.setPixel(pixel);

            tile.setIsPng(isPngTile(action));

            if (isEmptyTrafficTile(tile))
                tile.setIsEmpty(true);

            tile.setExpireTime(System.currentTimeMillis() + MapContentConfig.TRAFFIC_TILE_EXPIRE_TIME);

            return tile;
        }
        else
        {
            MapTile tile = MapDataFactory.getInstance().createMapTile();
            tile.setId(tileID);
            tile.setZoom(zoom);
            tile.setBinary(data);

            tile.setPixel(pixel);
            tile.setIsPng(isPngTile(action));
            tile.setExpireTime(System.currentTimeMillis() + MapContentConfig.MAP_TILE_EXPIRE_TIME);

            return tile;
        }
    }

    // This is a hack and only works with 128x128 tiles, we need a flag from server
    protected boolean isEmptyTrafficTile(TrafficTile tile)
    {
        if (tile == null || tile.getBinary() == null)
            return true;

        if (tile.getBinary().length == 389)
        {
            return true;
        }

        return false;
    }

    public Tile unmarshal(Tile tile)
    {
        return unmarshal(tile, -1);
    }

    public Tile unmarshal(Tile tile, int action)
    {
        if (isTrafficStreamer())
        {
            if(tile instanceof TrafficTile)
            {
                TrafficTile trafficTile = (TrafficTile) tile;
                if (trafficTile.isPng())
                {
                    return tile;
                }
                else
                {
                    return unmarshalTrafficTile(trafficTile.getBinary(), trafficTile.getId(), 
                            trafficTile.getZoom(), trafficTile.getPixel(), trafficTile.getExpireTime());
                }
            }
        }
        else
        {
            if(tile instanceof MapTile)
            {
                MapTile mapTile = (MapTile) tile;
                if (mapTile.isPng())
                {
                    return tile;
                }
                else
                {
                    return unmarshalMapTile(mapTile.getBinary(), mapTile.getId(), 
                            mapTile.getZoom(), mapTile.getPixel(), mapTile.getExpireTime());
                }
            }
        }
        
        return null;
    }

    public MapTile unmarshalMapTile(byte[] buff, long tileID, int zoom, int pixel, long expireTime)
    {
        byte[] data = buff;
        if (data == null)
        {
            return null;
        }

        MapTile tile = MapDataFactory.getInstance().createMapTile();
        tile.setExpireTime(expireTime);
        
        if (data.length == 0) 
        {               
            return tile;        
        }

        int[] offset = new int[]{1};

        DataUtil.readValue(data, offset); //tile id
        DataUtil.readValue(data, offset); //zoom
        DataUtil.readValue(data, offset); //type

        tile.setId(tileID);
        tile.setZoom(zoom);
        tile.setPixel(pixel);
        tile.setStatus((byte) DataUtil.readValue(data, offset));

        int edgeCount = (int)DataUtil.readValue(data, offset);
        int markCount = (int)DataUtil.readValue(data, offset);

        boolean hasStreetName = (buff[0] != Tile.STATUS_ONLY_TILE_EDGES);

        int pointCount = 0;
        if (edgeCount > 0 || markCount > 0)
        {
            int latCenter = DataUtil.readInt(data, offset[0]);
            offset[0] += 4;
            int lonCenter = DataUtil.readInt(data, offset[0]);
            offset[0] += 4;
            int scale = data[offset[0]];
            offset[0]++;

            VectorMapEdge[] edges = null;
            if (edgeCount > 0)
            {                
                edges = new VectorMapEdge[edgeCount];
                for (int i=0; i<edgeCount; i++)
                {
                    VectorMapEdge edge = MapDataFactory.getInstance().createMapEdge();

                    unmarshalMapEdge(data, offset, edge, latCenter, lonCenter, scale, hasStreetName);

                    int j = 0;
                    for (; j< i; j++)
                    {
                        VectorMapEdge e = edges[j];
                        if (edge.getRoadType() < e.getRoadType())
                        {
                            for (int k = i; k > j; k--)
                                edges[k] = edges[k-1];    

                            edges[j] = edge;

                            break;
                        }

                    }// end for

                    if (j == i)
                        edges[i] = edge; 

                    pointCount += edge.getShapePointsSize();
                }

                tile.setMapEdges(edges);
            }

            if (markCount > 0)
            {
                TileMark[] tileMarks = new TileMark[markCount];
                for (int i=0; i<markCount; i++)
                {
                    TileMark tileMark = MapDataFactory.getInstance().createTileMark();
                    unmarshalTileMark(data, offset, tileMark, latCenter, lonCenter, scale);
                    tileMarks[i] = tileMark;

                    if (edges != null)
                    {
                        String name = tileMark.getName();
                        byte markType = tileMark.getMarkType();
                        if (name != null && 
                                name.length() == 1 &&
                                !name.equals("*"))
                        {
                            VectorMapEdge edge = getClosestMapEdge(edges, tileMark);
                            tileMark.setMapEdge(edge);
                        }
                    }
                }
                tile.setTileMarks(tileMarks);
            }
        }
        MapUtil.decimateMapTile(tile);
        return tile;
    }

    private int unmarshalMapEdge(byte[] data, int[] offset,
            VectorMapEdge edge, int latCenter, int lonCenter, 
            int scale, boolean hasStreetName)
    {
        int startOffset = offset[0];
        try
        {            
            edge.setRoadType(data[offset[0]]);
            if (edge.getRoadType() < 0)
            {
                edge.setIsIsland(true);
                edge.setRoadType((byte)-edge.getRoadType());
            }
            else
            {
                edge.setIsIsland(false);
            }

            offset[0]++;

            if (hasStreetName)
            {   
                int streetNameLen = (int)DataUtil.readValue(data, offset);
                if (streetNameLen > 0)
                {
                    try
                    {
                        edge.setStreetName(new String(data, offset[0], streetNameLen, "UTF-8"));
                    }
                    catch (Throwable e)
                    {
                        e.printStackTrace();
                    }
                    offset[0] += streetNameLen;
                }
            }
            
            int pointsCount = DataUtil.readShort(data, offset[0]);
            offset[0] += 2;
            if (pointsCount > 0)
            {
                boolean needStoreData = true;
                int[] shapePoints = new int[pointsCount<<1];
                int prevLat = 0, prevLon = 0, pLat, pLon, globalLat, globalLon;
                int doubleI = 0;
                for (int i=0; i<pointsCount; i++,doubleI+=2)
                {
                    if (i == 0)
                    {
                        pLat = ((int)DataUtil.readValue(data, offset) << scale) + latCenter;
                        pLon = ((int)DataUtil.readValue(data, offset) << scale) + lonCenter;
                    }
                    else
                    {
                        pLat = (int)(DataUtil.readValue(data, offset) << scale) + prevLat;
                        pLon = (int)(DataUtil.readValue(data, offset) << scale) + prevLon;
                    }
                    prevLat = pLat;
                    prevLon = pLon;

                    if (needStoreData)
                    {
                        globalLat = GlobalCoordinateUtil.earthLatToGlobal(pLat);
                        globalLon = GlobalCoordinateUtil.earthLonToGlobal(pLon);
                        
                        if (doubleI >= 4)
                        {
                            if (globalLat == shapePoints[doubleI - 4 + DataUtil.LAT] && globalLon == shapePoints[doubleI - 4 + DataUtil.LON])
                            {
                                //the shape point data has problem, do not store them
                                //sometime there are duplicate shape points
                                needStoreData = false;
                                int[] tempSp = new int[doubleI];
                                System.arraycopy(shapePoints, 0, tempSp, 0, doubleI);
                                shapePoints = tempSp;
                            }
                        }
                        shapePoints[doubleI + DataUtil.LAT] = globalLat;
                        shapePoints[doubleI + DataUtil.LON] = globalLon;
                    }
                }

                if (shapePoints.length > 2)
                {
                    edge.setShapePoints(shapePoints);

                    if (edge.getStreetName() != null)
                    {
                        edge.setId(edge.getStreetName().hashCode());
                    }
                    else
                    {
                        edge.setId(-1);
                    }
                }
            }
        }
        catch (Throwable e)
        {
            e.printStackTrace();
        }

        return offset[0] - startOffset;
    }

    private VectorMapEdge getClosestMapEdge(VectorMapEdge[] edges, TileMark tileMark)
    {
        int pLat = tileMark.getPositionLat();    // dataPool.getInt2(from, IDataUtil.LAT);
        int pLon = tileMark.getPositionLon();    // dataPool.getInt2(from, IDataUtil.LON);

        VectorMapEdge closestEdge = null;
        int minDist2 = Integer.MAX_VALUE;
        for (int i = 0; i < edges.length; i++)
        {
            VectorMapEdge e = edges[i];
            if (e.getShapePointsSize() > 1)
            {
                for (int j = 2; j < (e.getShapePointsSize()<<1) - 1; j+=2)
                {
                    int pLat0 = e.latAt((j - 2)>>1);
                    int pLon0 = e.lonAt((j - 2)>>1);
                    int pLat1 = e.latAt(j>>1);
                    int pLon1 = e.lonAt(j>>1);

                    int dist2 = DataUtil.getDist2(pLon0, pLat0, pLon1, pLat1, pLon, pLat);
                    if (dist2 < minDist2)
                    {
                        closestEdge = edges[i];
                        minDist2 = dist2;
                    }
                }
            }
        }

        return closestEdge;
    }


    protected int unmarshalTileMark(byte[] data, int[] offset, 
            TileMark tileMark, int latCenter, int lonCenter, int scale)
    {
        int startIndex = offset[0];

        //type
        tileMark.setMarkType(data[offset[0]]);
        offset[0]++;

        //FIXME: what does lable type means ?
        tileMark.setLabelType(data[offset[0]]);
        offset[0]++;

        //name
        int nameLength = (int)DataUtil.readValue(data, offset);
        if (nameLength > 0)
        {
            try 
            {
                String s = new String(data, offset[0], nameLength, "UTF-8");
                tileMark.setName(s);
            } 
            catch (Throwable e) 
            {
                e.printStackTrace();
            }
            offset[0] += nameLength;
        }

        //heading
        tileMark.setHeading((short)DataUtil.readValue(data, offset));

        //style
        tileMark.setStyle((byte)DataUtil.readValue(data, offset));

        //size
        tileMark.setSize((byte)DataUtil.readValue(data, offset));

        //lat, lon
        int lat = (int)DataUtil.readValue(data, offset);
        int lon = (int)DataUtil.readValue(data, offset);

        int latP = (lat << scale) + latCenter;
        int lonP = (lon << scale) + lonCenter;

        int globalLat = GlobalCoordinateUtil.earthLatToGlobal(latP);
        int globalLon = GlobalCoordinateUtil.earthLonToGlobal(lonP);

        tileMark.setPosition(globalLat, globalLon);

        return offset[0] - startIndex;
    }

    private boolean lessThan(int v1, int v2, int zoom)
    {
        if (v1 <= v2 + (2 << zoom) )
            return true;

        return false;
    }

    protected TrafficTile unmarshalTrafficTile(byte[] buff, long tileID, int zoom, int pixel, long expireTime)
    {
        if (buff != null && buff.length > 3)
        {
            int latCenter = DataUtil.readInt(buff, 0);
            int lonCenter = DataUtil.readInt(buff, 4);

            byte size = buff[8];

            TrafficTile mt = MapDataFactory.getInstance().createTrafficTile();
            mt.setId(tileID);
            mt.setZoom(zoom);
            mt.setPixel(pixel);
            mt.setExpireTime(expireTime);

            int pos = 9;
            short edgeSize = (short) readBySize(buff, pos, SIZE_SHORT);
            pos += SIZE_SHORT;

            Vector tmcIds = getTmcIds(buff, true);
            VectorTrafficEdge[] trafficEdges = new VectorTrafficEdge[edgeSize];
            for (int i = 0; i < trafficEdges.length; ++i)
            {
                trafficEdges[i] = TrafficDataFactory.getInstance().createTrafficEdge();
                pos = parseEdge(buff, pos, trafficEdges[i], tmcIds, size, latCenter, lonCenter, true);
            }

            // unmarshal incidents
            pos = calculateIncidentsPos(buff);
            int incidentSize = readBySize(buff, pos, SIZE_INT);
            pos += SIZE_INT;

            TrafficIncident[] trafficIncidents = new TrafficIncident[incidentSize];

            for (int i = 0; i < incidentSize; ++i)
            {
                TrafficIncident incident = TrafficDataFactory.getInstance().createTrafficIncident();
                trafficIncidents[i] = incident;
                incident.setLat(readBySize(buff, pos, SIZE_INT));
                pos += SIZE_INT;
                incident.setLon(readBySize(buff, pos, SIZE_INT));
                pos += SIZE_INT;
                incident.setSeverity((byte) readBySize(buff, pos, SIZE_INT));
                pos += SIZE_INT;

                incident.setLaneClosed((byte) buff[pos++]);
                incident.setIncidentType((byte) buff[pos++]);

                int strLen = getSize(buff[pos++]);
                incident.setCrossSt(readString(buff, strLen, pos));
                pos += strLen;

                strLen = getSize(buff[pos++]);
                incident.setMsg(readString(buff, strLen, pos));

                pos += strLen;

                int[] screenPos = new int[2];
                incident.setScreenPos(screenPos);
            }

            mt.setTrafficEdges(trafficEdges);
            mt.setTrafficIncidents(trafficIncidents);
            return mt;
        }
        else
        {
            return null;
        }
    }

    private Vector getTmcIds(byte[] buff, boolean hasTraffic)
    {
        int pos = posTmcId(buff, hasTraffic);


        short tmcIdSize = (short) readBySize(buff, pos, SIZE_SHORT);
        pos += SIZE_SHORT;

        Vector v = new Vector(tmcIdSize);
        for (int i = 0; i < tmcIdSize; ++i)
        {
            int length = buff[pos++];
            String sName = new String(buff, pos, length);
            v.addElement(sName);
            pos += length;
        }

        return v;
    }

    private int posTmcId(byte[] buff, boolean hasTraffic)
    {
        int pos = 8;
        int size = buff[pos++];

        short edgeSize = (short) readBySize(buff, pos, SIZE_SHORT);
        pos += SIZE_SHORT;

        for (int i = 0; i < edgeSize; ++i)
        {
            pos += 6;// road type, direction, speed limit(short), tmc Id(short)
            if (hasTraffic)
            {
                pos += 2; // traffic speed
            }

            int pointSize = getSize(buff[pos++]);
            pos += size * pointSize * 2;
        }

        return pos;
    }

    private int parseEdge(byte[] buff, int pos, VectorTrafficEdge e, Vector tmcIds, int size, int latCenter, int lonCenter, boolean hasTraffic)
    {
        e.setRoadType(buff[pos++]); // road type
        e.setDirectionality(buff[pos++]); // directionality
        e.setSpeedLimit((short) readBySize(buff, pos, SIZE_SHORT)); // speed limit

        pos += SIZE_SHORT;

        short tmcIdIndex = (short) readBySize(buff, pos, SIZE_SHORT); // tmc id index

        if (tmcIdIndex != -1 && tmcIdIndex < tmcIds.size())
        {
            e.setTmcId((String) tmcIds.elementAt(tmcIdIndex));
        }
        pos += SIZE_SHORT;

        if (hasTraffic)
        {
            e.setTrafficSpeed((short) readBySize(buff, pos, SIZE_SHORT));
            pos += SIZE_SHORT;
        }

        int pointSize = getSize(buff[pos++]);

        int[] shapePoints = new int[pointSize << 1];
        int doubleSize = size << 1;
        int offset;
        int[] tempPoint = new int[2];
        for (int i = 0; i < pointSize; ++i)
        {
            read2DArrayBySize(buff, pos, size, latCenter, lonCenter, tempPoint);

            int globalLat = GlobalCoordinateUtil.earthLatToGlobal(tempPoint[DataUtil.LAT]);
            int globalLon = GlobalCoordinateUtil.earthLonToGlobal(tempPoint[DataUtil.LON]);

            offset = i << 1;
            shapePoints[offset] = globalLat;
            shapePoints[offset + 1] = globalLon;

            pos += doubleSize;
        }
        e.setShapePoints(shapePoints);

        return pos;
    }

    /**
     * caclulate the position of incidents
     * 
     * @param buff
     * @return
     */
    private int calculateIncidentsPos(byte[] buff)
    {
        int pos = posTmcId(buff, true);

        short tmcIdSize = (short) readBySize(buff, pos, SIZE_SHORT);
        pos += SIZE_SHORT;

        for (int i = 0; i < tmcIdSize; ++i)
        {
            int length = buff[pos++];
            pos += length;
        }
        return pos;

    }

    private void read2DArrayBySize(byte[] buff, int pos, int size, int latCenter, int lonCenter, int[] result)
    {
        if (size == SIZE_INT)
        {
            result[0] = DataUtil.readInt(buff, pos);
            result[1] = DataUtil.readInt(buff, pos + SIZE_INT);
        }
        else if (size == SIZE_SHORT)
        {
            result[0] = (short) DataUtil.readShort(buff, pos);
            result[1] = (short) DataUtil.readShort(buff, pos + SIZE_SHORT);
        }
        else
        {
            result[0] = buff[pos];
            result[1] = buff[pos + SIZE_BYTE];
        }

        result[0] += latCenter;
        result[1] += lonCenter;

    }

    // =======================================================================//
    private void buildRequestNode(int action, long tileId, Node mapNode)
    {
        int pixel = MapUtil.TILE_PIXEL_SIZE;
        Node tileNode = createTileNode(action, tileId, pixel);
        mapNode.addChild(tileNode);
    }

    private Node createTileNode(int action, long tileId, int pixel)
    {
        Node tileReq = NodeFactory.getInstance().createNode();
        tileReq.addValue(action);
        tileReq.addValue(tileId);
        tileReq.addValue(MapUtil.getZoomFromID(tileId));
        tileReq.addValue(pixel);
        return tileReq;
    }

    private Node createTilesNode(Node mapNode)
    {
        Node request = NodeFactory.getInstance().createNode();
        request.addChild(mapNode);
        addMandatoryNode(request);

        return request;
    }


}
