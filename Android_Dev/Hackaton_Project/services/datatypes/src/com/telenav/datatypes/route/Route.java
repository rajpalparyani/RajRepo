package com.telenav.datatypes.route;

import java.util.Hashtable;
import java.util.List;

import com.telenav.datatypes.DataUtil;
import com.telenav.datatypes.audio.AudioDataNode;

public class Route
{
    public static int WAYPOINTS_LAT_INDEX = 0;
    public static int WAYPOINTS_LON_INDEX = 1;
    
    public static final int PEDESTRIAN_SPEED            = 10;
    
    //-------------- route styles ---------------------
    //jyxu fix bug: dont change me! because it is the same to sever ids.
    public static final byte ROUTE_FASTEST              = 1;
    public static final byte ROUTE_SHORTEST             = 2;
//    public static final byte ROUTE_PREFER_STREET        = 3;
//    public static final byte ROUTE_PREFER_HIGHWAY       = 5;
    public static final byte ROUTE_AVOID_HIGHWAY        = 3;
    public static final byte ROUTE_PEDESTRIAN           = 7;
//    public static final byte ROUTE_ASK_ME_EACH_TRIP     = 99;
    
    //turn type definition
    public static final byte TURN_TYPE_CONTINUE                              = 0; //default turn type
    public static final byte TURN_TYPE_TURN_SLIGHT_RIGHT                     = 1;
    public static final byte TURN_TYPE_TURN_RIGHT                            = 2;
    public static final byte TURN_TYPE_TURN_HARD_RIGHT                       = 3;
    public static final byte TURN_TYPE_U_TURN                                = 4;
    public static final byte TURN_TYPE_TURN_HARD_LEFT                        = 5;
    public static final byte TURN_TYPE_TURN_LEFT                             = 6;
    public static final byte TURN_TYPE_TURN_SLIGHT_LEFT                      = 7;
    
    public static final byte TURN_TYPE_ENTER_LEFT                            = 10;
    public static final byte TURN_TYPE_ENTER_RIGHT                           = 11;
    public static final byte TURN_TYPE_EXIT_LEFT                             = 12;
    public static final byte TURN_TYPE_EXIT_RIGHT                            = 13;
    public static final byte TURN_TYPE_MERGE_LEFT                            = 14;
    public static final byte TURN_TYPE_MERGE_RIGHT                           = 15;
    public static final byte TURN_TYPE_MERGE_AHEAD                           = 16;
    public static final byte TURN_TYPE_DESTINATION_LEFT                      = 17;
    public static final byte TURN_TYPE_DESTINATION_RIGHT                     = 18;
    public static final byte TURN_TYPE_DESTINATION_AHEAD                     = 19;
    public static final byte TURN_TYPE_ROUNDABOUT_ENTER                      = 101;
    public static final byte TURN_TYPE_ROUNDABOUT_EXIT                       = 102;
    
    public static final byte TURN_TYPE_STAY_LEFT                             = 26;
    public static final byte TURN_TYPE_STAY_RIGHT                            = 27;
    public static final byte TURN_TYPE_START                                 = 28;
    
    // Guoyuan on 4/19/08: EU specific turn types (for left side driving countries like UK)
    public static final byte TURN_TYPE_U_TURN_L                              = 50;    
    public static final byte TURN_TYPE_ROUNDABOUT_ENTER_L                    = 21;
    public static final byte TURN_TYPE_ROUNDABOUT_EXIT_L                     = 22;
    
    
    //JY add these from CN Yaoyuxin
    public static final byte TURN_TYPE_FERRY_ENTER             = 50;
    public static final byte TURN_TYPE_FERRY_EXIT              = 51;
    public static final byte TURN_TYPE_STAY_MIDDLE             = 61;
    public static final byte TURN_TYPE_F2Z_ENTER_LEFT          = 62;
    public static final byte TURN_TYPE_Z2F_EXIT_LEFT           = 63;
    public static final byte TURN_TYPE_MULIT_FORK_STAY_RIGHT   = 77;
    public static final byte TURN_TYPE_MULIT_FORK_STAY_LEFT    = 76;
    public static final byte TURN_TYPE_H2R_EXIT_MIDDLE         = 60;
    public static final byte TURN_TYPE_H2H_EXIT_LEFT           = 67;
    public static final byte TURN_TYPE_H2H_EXIT_RIGHT          = 70;
    public static final byte TURN_TYPE_L2H_ENTER               = 88;
    public static final byte TURN_TYPE_F2Z_ENTER_RIGHT         = 113;
    public static final byte TURN_TYPE_Z2F_EXIT_RIGHT          = 114;
    public static final byte TURN_TYPE_LEFT_U_TURN             = 120;
    public static final byte TURN_TYPE_RIGHT_U_TURN            = 121;
    public static final byte TURN_TYPE_WAYPOINT_LEFT           = 97;
    public static final byte TURN_TYPE_WAYPOINT_RIGHT          = 98;
    public static final byte TURN_TYPE_WAYPOINT_AHEAD          = 99;

    //------ special turn types
    public static final byte L2L_CONTINUE       = 0;
    public static final byte L2L_U_TURN         = 4;
    public static final byte H2H_MERGE_LEFT     = 14;
    public static final byte H2H_MERGE_RIGHT    = 15;
    public static final byte H2H_MERGE_AHEAD    = 16;
    

    //------------- THRESHOLD RULE INDICIES ------------------
    public static final byte RULE_ACT_DISTANCE_A    = 0;
    public static final byte RULE_ACT_DISTANCE_B    = 1;    
    public static final byte RULE_ACT_TIME_A        = 2;
    public static final byte RULE_ACT_TIME_B        = 3;
    public static final byte RULE_PREP_DISTANCE_A   = 4;
    public static final byte RULE_PREP_DISTANCE_B   = 5;
    public static final byte RULE_PREP_L4_DIST_A    = 6;
    public static final byte RULE_PREP_L4_DIST_B    = 7;
    public static final byte RULE_PREP_TIME_A       = 8;
    public static final byte RULE_PREP_TIME_B       = 9;
    public static final byte RULE_CONT_DIST_OFFSET  = 10;
    public static final byte RULE_LAG_MIN_SEG_LEN   = 11;
    
    public static final byte RULE_CONT_TIME_OFFSET  = 12;
    public static final byte RULE_HIGHWAY_OFFSET    = 13;
    public static final byte RULE_L4_PREP_OFFSET    = 14;
    public static final byte RULE_L4_ACT_OFFSET     = 15;
    public static final byte RULE_LAG_DEFAULT       = 16;
    public static final byte RULE_LAG_MIN           = 17;
    
    public static final byte RULE_MAX_RANGE_DEV     = 18;
    public static final byte RULE_MAX_DECIM_ERR     = 19;
    public static final byte RULE_MAX_GPS_ERR       = 20;
    public static final byte RULE_WALK_BACK_TOL     = 21;
    public static final byte RULE_RANGE_LEN_PERCENT = 22;
    public static final byte RULE_NODE_RANGE_PERCENT= 23;
    public static final byte RULE_ROUTE_HEADING_DEV = 24;
    public static final byte RULE_NODE_HEADING_DEV  = 25;
    public static final byte RULE_MIN_DECIM_ERR     = 26;
    public static final byte RULE_MIN_GPS_ERR       = 27;
    public static final byte RULE_SECTION_TRIGGER   = 28;
    public static final byte RULE_NEW_ROUTE_RANGE   = 29;
    public static final byte RULE_REGAIN_RANGE      = 30;
    public static final byte RULE_MIN_GPS_THRESH    = 31;
    public static final byte RULE_ADI_NEW_ROUTE_THRESHOLD = 32;
    public static final byte RULE_COMMUNICATION_DELAY_TIME = 33;
    
    // threshold calculation constants
    public static final byte RULE_GPS_GAIN_FACTOR       = 34;
    public static final byte RULE_GPS_GAIN_SCALE        = 35;
    public static final byte RULE_GPS_DECAY_FACTOR      = 36;
    public static final byte RULE_GPS_DECAY_TIME_SCALE  = 37;

    // dev counter limits
    public static final byte RULE_POS_DEV_COUNT         = 38;
    public static final byte RULE_HEAD_DEV_COUNT        = 39;
    public static final byte RULE_POS_DEV_REGAIN_COUNT  = 40;
    public static final byte RULE_HEAD_DEV_REGAIN_COUNT = 41;
    
    public static final byte RULE_LAST_TRIP_INFO_EXPIRE_TIME    = 42;
    public static final byte RULE_DEV_GEOFENCE_RADIUS           = 43;

    final static public int THRESHOLD_INDEX_RANGE_DEVIATION     = 0;
    final static public int THRESHOLD_INDEX_NODE_DEVIATION      = 1;
    final static public int THRESHOLD_INDEX_HEADING_DEVIATION   = 2;
    final static public int THRESHOLD_INDEX_NEW_ROUTE           = 3;
    final static public int THRESHOLD_INDEX_REGAIN              = 4;

    final static public byte AUDIO_TYPE_PREP            = 0;
    final static public byte AUDIO_TYPE_ACTION          = 1;
    final static public byte AUDIO_TYPE_INFO            = 2;
    final static public byte AUDIO_TYPE_ADI             = 3;
    final static public byte AUDIO_TYPE_ADI_TURN        = 4;
    final static public byte AUDIO_TYPE_EDGE            = 5;
    
    
    protected Segment[] segments;
    protected long originTimeStamp;
    protected int routeID, originVn, originVe;
    protected int walkBackTolerance;
    
    protected int trafficDelayTime;
    protected int markedTrafficDelayDistToDest;
    
    protected int[] boundingBox;
    
    protected int length = -1;
    protected boolean isTilesRequested;
    protected int tileRequestSegmentIndex;
    protected int tileRequestEdgeIndex;
    protected int tileRequestShapeIndex;    

    protected int audioRequestSegmentIndex = -1;
    protected AudioDataNode destinationAudio;
    
    protected boolean isLocallyGenerated;
    
    protected long totalTimeInSeconds;
    
    protected List wayPoints;
    
    protected boolean isCompeleted;
    
    public boolean isCompeleted()
    {
        return isCompeleted;
    }

    public void setCompeleted(boolean isCompeleted)
    {
        this.isCompeleted = isCompeleted;
    }

    public long getTotalTimeInSeconds()
    {
        return totalTimeInSeconds;
    }

    public void setTotalTimeInSeconds(long totalTimeInSeconds)
    {
        this.totalTimeInSeconds = totalTimeInSeconds;
    }

    protected Route()
    {
        
    }
    
    public Segment segmentAt(int index)
    {
        if (segments != null && index >= 0 && index < segments.length)
            return segments[index];
            
        return null;    
    }

    public int segmentsSize()
    {
        if (segments == null) return 0;
        
        return segments.length;
    }

    public int getRouteID()
    {
        return routeID;
    }
    
    public void setRouteID(int routeID)
    {
        this.routeID = routeID;
    }

    public long getOriginTimeStamp()
    {
        return originTimeStamp;
    }
    
    public void setOriginTimeStamp(long originTimeStamp)
    {
        this.originTimeStamp = originTimeStamp;
    }

    public int getOriginVn()
    {
        return originVn;
    }
    
    public void setOriginVn(int originVn)
    {
        this.originVn = originVn;
    }

    public int getOriginVe()
    {
        return originVe;
    }
    
    public void setOriginVe(int originVe)
    {
        this.originVe = originVe;
    }

    public int getWalkBackTolerance()
    {
        return walkBackTolerance;
    }

    public int[] getBoundingBox()
    {
        if (boundingBox == null)
        {
            calculateBoundingBox();
        }
        return boundingBox;
    }
    
    private void calculateBoundingBox()
    {
        int minRouteLat = Integer.MAX_VALUE;
        int minRouteLon = Integer.MAX_VALUE;
        int maxRouteLat = Integer.MIN_VALUE;
        int maxRouteLon = Integer.MIN_VALUE;
        for (int j = 0; j < segmentsSize(); j ++)
        {
            Segment seg = segmentAt(j);

            if (seg.minLat() < minRouteLat)
                minRouteLat = seg.minLat();
                    
            if (seg.maxLat() > maxRouteLat)
                maxRouteLat = seg.maxLat();
                    
            if (seg.minLon() < minRouteLon)
                minRouteLon = seg.minLon();
                    
            if (seg.maxLon() > maxRouteLon)
                maxRouteLon = seg.maxLon();
        }

        boundingBox = new int[]{minRouteLat, minRouteLon, maxRouteLat, maxRouteLon};
    }
    
    public void setBoundingBox(int[] boundingBox)
    {
        this.boundingBox = boundingBox;
    }

    public Segment[] getSegments()
    {
        return this.segments;
    }
    
    public void setSegments(Segment[] segments)
    {
        this.segments = segments;
    }

    public int calcETA(boolean isConstantSpeed, int constantSpeed)
    {
        return calcETA(0, 0, 0, 0, isConstantSpeed, constantSpeed);
    }
    
    public int calcETA(int startSegIndex, int startEdgeIndex, int startPointIndex, int range,
            boolean useConstantSpeed, int constantSpeed)
    {
        int eta = 0;
        
        //calc the start segment
        Segment seg = segmentAt(startSegIndex);
        if(seg == null)
            return 60 * 1000;
        
        int len = seg.getLength();
        
        for (int i=0; i<startEdgeIndex; i++)
        {
            RouteEdge edge = seg.getEdge(i);
            if (edge != null)
                len -= edge.getLength();
        }
        
        RouteEdge edge = seg.getEdge(startEdgeIndex);
        if (edge != null)
        {
            for (int i=0; i<startPointIndex; i++)
            {
                len -= edge.getDeltaRs(i);
            }
        }
        
        len -= range;
        eta += len * 10 / (useConstantSpeed?constantSpeed : seg.getSpeedLimit());
        
        //calc remain segments
        for (int i=startSegIndex+1; i<segmentsSize(); i++)
        {
            Segment tempSeg = segmentAt(i);
            if(tempSeg != null)
            {
                eta += tempSeg.getLength() * 10 / (useConstantSpeed ? constantSpeed : tempSeg.getSpeedLimit());
                
                len += tempSeg.getLength();
            }
        }
        
        // adjust eta by traffic delay time if it is available
        int distToDest = len;
        if (this.trafficDelayTime > 0 && distToDest > 0 && this.markedTrafficDelayDistToDest > 0)
        {
            if (len > this.markedTrafficDelayDistToDest)
            {
                eta += this.trafficDelayTime;
            }
            else
            {
                eta += this.trafficDelayTime * len / markedTrafficDelayDistToDest;
            }
        }
        
        return (eta < 60) ? 60 * 1000 : eta * 1000; //fix bug 22470
    }
    
    public boolean isPartial()
    {
        if (segments == null)
            return true;
        
        for (int i=segments.length-1; i>=0; i--)
        {
            if (segments[i] == null || !segments[i].isEdgeResolved())
                return true;
        }
        
        return false;
    }
    
    public int getLength()
    {
        if (length == -1)
        {
            length = 0;
            for (int i=0; i<segments.length; i++)
            {
                if (segments[i] == null)
                    continue;
                
                length += segments[i].getLength();
            }
        }
        return length;
    }

    /**
     * resolves route references given a map manager
     * it's called for the first route comes
     * @param manager   [in]    map data
     */
    public void resolveReferences(Hashtable edgesTable, Hashtable routeStrings, int[] thresholdRules)
    {
        for (int i = 0; i < segments.length; ++i)
        {
            segments[i].resolveEdgeReferences(i==segments.length-1, edgesTable, routeStrings, thresholdRules);
        }
        
        //calc the distance from dest for each of the segments.
        int distToDest = 0;

        for (int i = segments.length - 1; i >= 0; i--)
        {
            distToDest += segments[i].getLength();
            segments[i].setDistanceToDest(distToDest);
        }
        
        //resolve ui lag thresholds
        for (int i = 1; i < segments.length; i++)
        {
            segments[i].setInstructionLagDist(calculateUILagDist(segments[i], segments[i-1], thresholdRules));
        }

        if (thresholdRules != null)
        {
            //route walk back tolerance     
            this.walkBackTolerance = thresholdRules[RULE_WALK_BACK_TOL];
        }
        
    } // resolveMapReferences
    
    
    /**
     * calculate up update lag distance
     * @return  update lag distance
     */
    int calculateUILagDist(
        Segment curSeg, 
        Segment prevSeg,
        int[] rules)
    {
        if (rules == null)
            return 0;
        
        int currRoadType = curSeg.getRoadType();
        int prevRoadType = prevSeg.getRoadType();

        // if it's the first segment, there should be no lag
        // or
        // if the previous segment is ramp or highway and current segment
        // is ramp or highway, dont use buffer,
        // since the user will not likely be stopped
        if (((prevRoadType == DataUtil.RAMP) || (prevRoadType == DataUtil.HIGHWAY)) &&
            ((currRoadType == DataUtil.RAMP) || (currRoadType == DataUtil.HIGHWAY)))
        {
            return 0; 
        }

        // if the previous segment has a uturn turn type and the current segment is an intersection,
        // we want to buffer the entire current section
        if ((currRoadType == DataUtil.INTERSECTION) &&
            (prevSeg.getTurnType() == L2L_U_TURN))
        { 
            return curSeg.getLength() + 1; 
        }
        
        // set ui buffer based on segment length
        if (curSeg.getLength() > rules[RULE_LAG_MIN_SEG_LEN])
        { 
            return  rules[RULE_LAG_DEFAULT]; 
        }
        else if (curSeg.getLength() > rules[RULE_LAG_DEFAULT])
        { 
            return rules[RULE_LAG_MIN]; 
        }
        else
        { 
            return 0; 
        }
    } // calculateUILagDist
    
    public void setTileRequestIndices(int indexSeg, int indexEdge, int indexShape)
    {
        this.tileRequestSegmentIndex = indexSeg;
        this.tileRequestEdgeIndex = indexEdge;
        this.tileRequestShapeIndex = indexShape;        
    }
    
    public int getTileRequestSegmentIndex()
    {
        return this.tileRequestSegmentIndex;
    }
    
    public int getTileRequestEdgeIndex()
    {
        return this.tileRequestEdgeIndex;
    }
    
    public int getTileRequestShapeIndex()
    {
        return this.tileRequestShapeIndex;
    }

    public void setIsTileRequestFinished(boolean b)
    {
        this.isTilesRequested = b;
    }
    
    public boolean isTileRequestFinished()
    {
        return isTilesRequested;
    }

    public void setAudioRequestSegmentIndex(int indexSeg)
    {
        this.audioRequestSegmentIndex = indexSeg;
    }
    
    public int getAudioRequestSegmentIndex()
    {
        return this.audioRequestSegmentIndex;
    }

    public AudioDataNode getDestinationAudio()
    {
        return this.destinationAudio;
    }

    public void setDestinationAudio(AudioDataNode node)
    {
        this.destinationAudio = node;
    }
    
    public void setTrafficDelayTime(int delayTime, int markedDistToDest)
    {
        this.trafficDelayTime = delayTime;
        this.markedTrafficDelayDistToDest = markedDistToDest;
    }
    
    public int getTrafficDelayTime()
    {
        return this.trafficDelayTime;
    }
    
    public int getMarkedTrafficDelayDistToDest()
    {
        return this.markedTrafficDelayDistToDest;
    }

    public boolean isLocallyGenerated() 
    {
        return isLocallyGenerated;
    }

    public void setIsLocallyGenerated(boolean isLocallyGenerated) 
    {
        this.isLocallyGenerated = isLocallyGenerated;
    }
    
    public int getWayPointsSize()
    {
        return wayPoints == null ? 0 : wayPoints.size();
    }
    
    public void setWayPoints(
            List wayPoints)
    {
        this.wayPoints = wayPoints;
    }
    
    public int[] getOriginLatLon()
    {
        return null;
    }
    
    public int[] getDestLatLon()
    {
        return null;
    }
}
