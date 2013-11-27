package com.telenav.datatypes.route;

import java.util.Hashtable;
import java.util.Vector;

import com.telenav.datatypes.DataUtil;
import com.telenav.datatypes.GlobalCoordinateUtil;
import com.telenav.datatypes.audio.AudioDataNode;
import com.telenav.location.TnLocation;
import com.telenav.logger.Logger;

class SegmentAudioAssistance
{
    boolean shouldPlayActionAudio= false;
    
    boolean shouldPlayPrepareAudio = false;
    
    boolean shouldPlayInfoAudio = false;
    
    boolean shouldPlayActionAudio4NextSeg = false;
    
    boolean shouldPlayPrepareAudio4NextSeg = false;
    
    boolean shouldPlayInfoAudio4NextSeg = false;
    
    int dist2Turn4ConfusingIntersection = Integer.MAX_VALUE;
    
    boolean isConfusingIntersectionInfoAvailable = false;
    
    // audio type could be IRoute.AUDIO_TYPE_INFO, IRoute.AUDIO_TYPE_PREP or IRoute_TYPE_ACTION
    public boolean shouldPlayAudio(int audioType)
    {
        if (audioType == Route.AUDIO_TYPE_ACTION)
        {
            return this.shouldPlayActionAudio;
        }
        else if (audioType == Route.AUDIO_TYPE_PREP)
        {
            return this.shouldPlayPrepareAudio;
        }
        else if (audioType == Route.AUDIO_TYPE_INFO)
        {
            return this.shouldPlayInfoAudio;
        }
        
        return false;
    }
    
    public boolean shouldPlayAudio4NextSegment(int audioType)
    {
        if (audioType == Route.AUDIO_TYPE_ACTION)
        {
            return this.shouldPlayActionAudio4NextSeg;
        }
        else if (audioType == Route.AUDIO_TYPE_PREP)
        {
            return this.shouldPlayPrepareAudio4NextSeg;
        }
        else if (audioType == Route.AUDIO_TYPE_INFO)
        {
            return this.shouldPlayInfoAudio4NextSeg;
        }
        
        return false;
    }
    
    public boolean isConfusingIntersectionInfoAvailable()
    {
        return this.isConfusingIntersectionInfoAvailable;
    }
    
    // if there is a confusing intersection, get its distance to turn
    // return Integer.MAX_VALUE if this value is not available 
    public int getDist2Turn4ConfusingIntersection()
    {
        return dist2Turn4ConfusingIntersection;
    }
}

public class Segment
{   
    protected boolean DEBUG = false;
    protected boolean DEBUG_VALIDITY = false;

    protected int[] edgesID;
    protected RouteEdge[] edges;
   
    protected int[] boundingBox;
    protected boolean isValid;
    
    protected int length;
    
    protected int calculatedLength;
    
    protected byte segmentType;
    protected byte turnType;
    protected byte roadType;
    protected byte exitNumber;

    protected int speedLimit;

    protected int streetNameId;
    protected int streetAliasId;
    protected String streetName;
    protected String streetAlias;
    protected String exitName;
    
    protected int[] laneInfos;
    protected int[] laneTypes;
    
    protected int cosLat;
    protected int distanceToDest;
    protected int instructionLagDist;

    protected int[] thresholds = new int[5];
    protected int[] minAudioDistance = new int[3];
    protected int[] minAudioTime = new int[3];
    protected boolean[] isPlayed = new boolean[] { false, false, false };
    
    protected AudioDataNode actionAudio;
    protected AudioDataNode prepAudio;
    protected AudioDataNode infoAudio;
    
    protected boolean needReadjustThresholds;
    
    protected boolean isEdgeResolved;
    
    protected int[][] decimatedPointsGlobal;
    
    protected int decimatedZoom = -1;
    protected int decimatedStartEdge = -1, decimatedEndEdge = -1, decimatedStartPoint = -1, decimatedEndPoint = -1;
    protected Object decimatedMutex = new Object();

    protected AudioDataNode segmentAudioNode;

    protected SegmentAudioAssistance audioAssistance;
    
    protected int audioAssembleType;
    protected TnLocation turnLocation; 
    
    protected int secondsToTurn;
    
    protected boolean isTightTurn;

    public TnLocation getTurnLocation()
    {
        return turnLocation;
    }

    public void setTurnLocation(TnLocation turnLocation)
    {
        this.turnLocation = turnLocation;
    }

    protected Segment()
    {
        
    }
    
    // XXX: clean-up
    public void calcDeviationThresholds(boolean isLastSegment, int [] rules)
    {
        if (rules == null) return;

        int errSize = rules[Route.RULE_MIN_GPS_THRESH];
        //calculate range deviation threshold as % of segment length
        //NOTE: x * 10 >> 10 is almost the same as x / 100, but faster
        int decimationErr = (this.length * 10 * rules[Route.RULE_RANGE_LEN_PERCENT]) >> 10;
        decimationErr = Math.min(decimationErr, rules[Route.RULE_MAX_DECIM_ERR]);
        decimationErr = Math.max(decimationErr, rules[Route.RULE_MIN_DECIM_ERR]);

        int rangeDev = errSize + decimationErr;

        //if last segment: loosen up dev threshold
        if (isLastSegment)
        {
            rangeDev *= 3;
            //clip it at the ceiling
            rangeDev = Math.min(rangeDev, rules[Route.RULE_MAX_RANGE_DEV]);
        }
        //if ramp, set it to max
        if (this.roadType == DataUtil.RAMP) rangeDev = rules[Route.RULE_MAX_RANGE_DEV];

        //use 90% of that --> convert from Meters to DM5
        //NOTE: x * 900 >> 10 is almost the same as x * 90/100, but faster
        //RM 09/20/04 - commenting out, since gpsreader converted to DM5
        //rangeDev = (rangeDev * 920) >> 10;

        //calculate node deviation threshold as % of range deviation threshold
        //NOTE: x * 10 >> 10 is almost the same as x / 100, but faster
        int nodeDev = (rangeDev * 10 * rules[Route.RULE_NODE_RANGE_PERCENT]) >> 10;

        //calculate the new route threshold as % of range deviation threshold
        //NOTE: x * 10 >> 10 is almost the same as x / 100, but faster
        int newRouteRange = (rangeDev * 10 * rules[Route.RULE_NEW_ROUTE_RANGE]) >> 10;

        //calculate the regain threshold as % of range deviation threshold
        //NOTE: x * 10 >> 10 is almost the same as x / 100, but faster
        int regainRange = (rangeDev * 10 * rules[Route.RULE_REGAIN_RANGE]) >> 10;

        //QH: limit the regain range
        if (regainRange > 90)
            regainRange = 90;

        this.thresholds[Route.THRESHOLD_INDEX_RANGE_DEVIATION] = rangeDev;
        this.thresholds[Route.THRESHOLD_INDEX_NODE_DEVIATION] = nodeDev;
        this.thresholds[Route.THRESHOLD_INDEX_HEADING_DEVIATION] = (byte) rules[Route.RULE_ROUTE_HEADING_DEV];
        this.thresholds[Route.THRESHOLD_INDEX_NEW_ROUTE] = newRouteRange;
        this.thresholds[Route.THRESHOLD_INDEX_REGAIN] = regainRange;
    }

    // XXX: clean-up
    public void calculateThresholds(int [] rules)
    {
        int minContDistance, minActionDistance, minPrepDistance;
        byte minContTime, minActionTime, minPrepTime;

        if (rules == null) return;

        minPrepDistance = (Math.max(rules[Route.RULE_PREP_DISTANCE_A], rules[Route.RULE_PREP_DISTANCE_B] * speedLimit) >> DataUtil.SHIFT);
        minActionDistance = (Math.max(rules[Route.RULE_ACT_DISTANCE_A], rules[Route.RULE_ACT_DISTANCE_B] * speedLimit) >> DataUtil.SHIFT);

        minActionTime = (byte) (Math.max(rules[Route.RULE_ACT_TIME_A], rules[Route.RULE_ACT_TIME_B] * speedLimit) >> DataUtil.SHIFT);
        minPrepTime = (byte) (Math.max(rules[Route.RULE_PREP_TIME_A], rules[Route.RULE_PREP_TIME_B] * speedLimit) >> DataUtil.SHIFT);

        if (this.roadType == DataUtil.HIGHWAY) minPrepTime += (byte) rules[Route.RULE_HIGHWAY_OFFSET]; // add 2 ticks for highways

        if (rules[Route.RULE_PREP_L4_DIST_A] != 0 &&
            rules[Route.RULE_PREP_L4_DIST_B] != 0)
        {
            minPrepDistance = (Math.max(rules[Route.RULE_PREP_L4_DIST_A], rules[Route.RULE_PREP_L4_DIST_B] * speedLimit) >> DataUtil.SHIFT);

            minPrepTime += rules[Route.RULE_L4_PREP_OFFSET];
            minActionTime += rules[Route.RULE_L4_ACT_OFFSET];
        }

        minContTime = (byte) 127;
        minContDistance = (0xffffffff) >>> 1;

        if (rules[Route.RULE_CONT_DIST_OFFSET] != -1 &&
            rules[Route.RULE_CONT_TIME_OFFSET] != -1)
        {
            minContDistance = minPrepDistance + rules[Route.RULE_CONT_DIST_OFFSET];
            minContTime = (byte) (minPrepTime + rules[Route.RULE_CONT_TIME_OFFSET]);
        }

        minAudioDistance[Route.AUDIO_TYPE_ACTION] = minActionDistance;
        minAudioDistance[Route.AUDIO_TYPE_INFO] = minContDistance;
        minAudioDistance[Route.AUDIO_TYPE_PREP] = minPrepDistance;

        minAudioTime[Route.AUDIO_TYPE_ACTION] = minActionTime;
        minAudioTime[Route.AUDIO_TYPE_INFO] = minContTime;
        minAudioTime[Route.AUDIO_TYPE_PREP] = minPrepTime;

        /*
        minActionAudioDistance = minActionDistance;
        minActionAudioTime = minActionTime;
        minContinueDistance = minContDistance;
        minContinueTime = minContTime;
        minPrepareAudioDistance = minPrepDistance;
        minPrepareAudioTime = minPrepTime;
        */
    }

    public int getEdgeID(int index)
    {
        return edgesID[index];
    }

    public int edgesSize()
    {
        return this.edgesID == null ? 0 : this.edgesID.length;
    }
    
    public byte getSegmentType()
    {
        return segmentType;
    }

    public byte getTurnType()
    {
        return turnType;
    }

    public byte getRoadType()
    {
        return roadType;
    }

    public Integer getFirstEdgeId()
    {
        return new Integer(edgesID[0]);
    }
    
    public int getCosLat()
    {
        return cosLat;
    }

    public void setCosLat(int cosLat)
    {
        this.cosLat = cosLat;
    }
    
    /*
    public void getDeltaRs(int segIndex, int[] deltaRs)
    {
        for (int i = 0; i < this.deltaRs[segIndex].length; i++) 
        {
            deltaRs[i] = this.deltaRs[segIndex][i];
        }
    }
    */

    public int getLength()
    {
        return length;
    }
    
    public void setLength(int length)
    {
        this.length = length;
    }

    public int getDistanceToDest()
    {
        return distanceToDest;
    }
    
    public void setDistanceToDest(int dist)
    {
        this.distanceToDest = dist;
    }

    public void setPlayed(byte audioType, boolean isPlayed)
    {
        this.isPlayed[audioType] = isPlayed;
    }
    
    public boolean isPlayed(byte audioType)
    {
        return this.isPlayed[audioType];
    }

    public int getThreshold(int type)
    {
        return thresholds[type];
    }

    public void setThreshold(int type, int value)
    {
        thresholds[type] = value;
    }
    
    public int getMinAudioDistance(int audioType)
    {
        return this.minAudioDistance[audioType];
    }
    
    public int getMinAudioTime(int audioType)
    {
        return this.minAudioTime[audioType];
    }
    
    public int getInstructionLagDist()
    {
        return instructionLagDist;
    }
    
    public void setInstructionLagDist(int lag)
    {
        this.instructionLagDist = lag;
    }

    public void setNeedReadjustThresholds(boolean b)
    {
        this.needReadjustThresholds = b;
    }
    
    public boolean isNeedReadjustThresholds()
    {
        return needReadjustThresholds;
    }
    
    public int[] getBoundingBox()
    {
        return boundingBox;
    }
    
    public void setBoundingBox(int[] boundingBox)
    {
        this.boundingBox = boundingBox;
        isValid = DataUtil.isValidBoundingBox(boundingBox);
    }

    public int getStreetNameId()
    {
        return streetNameId;
    }

    public int getStreetAliasId()
    {
        return streetAliasId;
    }

    public String getStreetName()
    {
        return streetName;
    }

    public String getStreetAlias()
    {
        return streetAlias;
    }
    
    public void setStreetNameId(int streetNameId)
    {
        this.streetNameId = streetNameId;
    }

    public void setStreetAliasId(int streetAliasId)
    {
        this.streetAliasId = streetAliasId;
    }

    public void setStreetName(String streetName)
    {
        this.streetName = streetName;
    }

    public void setStreetAlias(String streetAlias)
    {
        this.streetAlias = streetAlias;
    }

    public int calculateUILagDist(Segment curSeg, Segment prevSeg, int [] rules)
    {
        int currRoadType = curSeg.getRoadType();
        int prevRoadType = prevSeg.getRoadType();
    
        //int [] rules = thresholdRules;
        if (rules == null) return 0 ;
    
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
            (prevSeg.getTurnType() == Route.L2L_U_TURN))
        {
            return curSeg.getLength() + 1;
        }
    
        // set ui buffer based on segment length
        if (curSeg.getLength() > rules[Route.RULE_LAG_MIN_SEG_LEN])
        {
            return  rules[Route.RULE_LAG_DEFAULT];
        }
        else if (curSeg.getLength() > rules[Route.RULE_LAG_DEFAULT])
        {
            return rules[Route.RULE_LAG_MIN];
        }
        else
        {
            return 0;
        }
    } // calculateUILagDist

    public void setEdgesId(int[] edgesId)
    {
        this.edgesID = edgesId;
    }
    
    private boolean recalculateBoundingBox()
    {
        int minLat = Integer.MAX_VALUE;
        int minLon = Integer.MAX_VALUE;
        int maxLat = Integer.MIN_VALUE;
        int maxLon = Integer.MIN_VALUE;
        
        int numEdges = 0;
        for (int k = 0; k < edgesSize(); k ++)
        {
            RouteEdge edge = getEdge(k);
            if (edge == null || edge.numPoints() == 0 || !edge.isValid()) continue;
                    numEdges++;
            int minEdgeLat = edge.minLat(); 
            int minEdgeLon = edge.minLon();
            int maxEdgeLat = edge.maxLat();
            int maxEdgeLon = edge.maxLon();

            if (minEdgeLat < minLat)
                minLat = minEdgeLat;
            if (maxEdgeLat > maxLat)
                maxLat = maxEdgeLat;
            if (minEdgeLon < minLon)
                minLon = minEdgeLon;
            if (maxEdgeLon > maxLon)
                maxLon = maxEdgeLon;
        }

        boundingBox = new int[]{minLat, minLon, maxLat, maxLon};
        isValid = DataUtil.isValidBoundingBox(boundingBox);
        
        return isValid;
    }
    
    public int minLat()
    {
        if (boundingBox == null)
        {
            return 0;
        }
        return boundingBox[0];
    }    
    
    public int maxLat()
    {
        if (boundingBox == null)
        {
            return 0;
        }
        return boundingBox[2];
    }    
    
    public int minLon()
    {
        if (boundingBox == null)
        {
            return 0;
        }
        return boundingBox[1];
    }    
    
    public int maxLon()
    {
        if (boundingBox == null)
        {
            return 0;
        }
        return boundingBox[3];
    }    
    
    public void setSegmentType(byte type)
    {
        this.segmentType = type;
    }

    public void setTurnType(byte turnType)
    {
        this.turnType = turnType;
    }

    public void setRoadType(byte roadType)
    {
        this.roadType = roadType;
    }

    public int getSpeedLimit()
    {
        return this.speedLimit;
    }

    public void setSpeedLimit(int speedLimit)
    {
        this.speedLimit = speedLimit;
    }

    public boolean isEdgeResolved()
    {
        return isEdgeResolved;
    }

    public void setEdgeResolved(boolean edgeResolved)
    {
        this.isEdgeResolved = edgeResolved;
    }
    
    public void setAudioAssembleType(int audioAssembleType)
    {
        this.audioAssembleType = audioAssembleType;
    }
    
    public int getAudioAssembleType()
    {
        return audioAssembleType;
    }
    
    // used only by segments generated by SPT 
    public void resolveEdgeReferences(boolean isLastSegment, int[] rules, DataUtil ignoreme)
    {
        if (isEdgeResolved || edges == null || edges.length == 0)
        {
            return;
        }
        
        cosLat = DataUtil.getCosLat((edges[0].latAt(0)));
        calculatedLength = 0;
        
        for (int i=0; i< edges.length; i++)
        {
            RouteEdge edge = edges[i];
            
            if (edge.getID() < 0) // edge generated by SPT
            {
                edge.setSegment(this);
            }
                
            calculatedLength += edge.getLength();
        }
        
        if (this.length == 0)
            this.length = calculatedLength;
        
        isEdgeResolved = true;
        
        //calculate the thresholds
        calculateThresholds(rules);
        
        calcDeviationThresholds(isLastSegment, rules);
    }
    
    /**
     * resolves route references
     */
    public void resolveEdgeReferences(boolean isLastSegment, Hashtable edgesTable, Hashtable routeStrings, int[] rules)
    {
        if (isEdgeResolved)
        {
            return;
        }

        String name = (String) routeStrings.get(new Integer(streetNameId));
        if (name != null && this.streetName == null)
        {
            setStreetName(name);
        }
        
        name = (String) routeStrings.get(new Integer(streetAliasId));
        if (name != null)
        {
            setStreetAlias(name);
        }
        
        isEdgeResolved = true;
        
        calculatedLength = 0;

        Vector v = new Vector(edgesSize());
        for (int k = 0; k < edgesSize(); k ++)
        {
            int id = getEdgeID(k);
            RouteEdge edge = (RouteEdge)edgesTable.get(new Integer(id));
            if (edge != null)
            {
                edge.setSegment(this);
                calculatedLength += edge.getLength();
                v.addElement(edge);
            }
            else
            {
                isEdgeResolved = false;
                break;
            }
        }
        
        if (isEdgeResolved)
        {
            RouteEdge[] newEdges = new RouteEdge[v.size()];
            v.copyInto(newEdges);
            setEdges(newEdges);
            cosLat = DataUtil.getCosLat((edges[0].latAt(0)));
        }
        
        //calculate the thresholds
        calculateThresholds(rules);
        
        calcDeviationThresholds(isLastSegment, rules);
        
    } // resolveMapReferences

    public int getCalculatedLength()
    {
        return calculatedLength;
    }
    
    public AudioDataNode getActionAudio()
    {
        return actionAudio;
    }
    
    public void setActionAudio(AudioDataNode actionAudio)
    {
        this.actionAudio = actionAudio;
    }
    
    public AudioDataNode getPrepAudio()
    {
        return prepAudio;
    }
    
    public void setPrepAudio(AudioDataNode prepAudio)
    {
        this.prepAudio = prepAudio;
    }
    
    public AudioDataNode getInfoAudio()
    {
        return this.infoAudio;
    }
    
    public void setInfoAudio(AudioDataNode infoAudio)
    {
        this.infoAudio = infoAudio;
    }

    public boolean isEdgePresent(int index)
    {
        return edges != null && index < edges.length;
    }

    public RouteEdge getEdge(int index)
    {
        if (isEdgePresent(index))
            return edges[index];
        return null;
    }

    public void setEdges(RouteEdge[] edges)
    {
        this.edges = edges;
        recalculateBoundingBox();
    }

    public String getExitName() {
        return exitName;
    }

    public byte getExitNumber() {
        return exitNumber;
    }

    public void setExitName(String exitName) {
        this.exitName = exitName;
    }

    public void setExitNumber(byte exitNumber) {
        this.exitNumber = exitNumber;
    }
    
    public void clearDecimatedPoints()
    {
        this.decimatedPointsGlobal = null;
    }

    public int[][] getDecimatedPointsGlobal(int zoom, int startEdge, int endEdge, 
            int startPoint, int endPoint, int deltaLat, int deltaLon)
    {
        synchronized (decimatedMutex)
        {
            if (decimatedZoom != zoom)
            {
                decimatedPointsGlobal = null;
                decimatedZoom = zoom;
            }
            
            if (decimatedPointsGlobal == null)
            {
                // replace
                return process(zoom, startEdge, endEdge, startPoint, endPoint, deltaLat, deltaLon);
            }
            else
            {
                // need to check start and end              
                if (startEdge == decimatedStartEdge && endEdge == decimatedEndEdge && 
                    startPoint == decimatedStartPoint && endPoint == decimatedEndPoint)
                {
                    return decimatedPointsGlobal;
                }
                else
                {
                    return process(zoom, startEdge, endEdge, startPoint, endPoint, deltaLat, deltaLon);
                }
            }
        }
        
        // return decimatedPoints;
    }

    private int[][] process(int zoom, int startEdge, int endEdge, 
            int startPoint, int endPoint, int deltaLat, int deltaLon)
    {
        int[][] pointsGlobal = getDecimatedPointsGlobalHelper(zoom, startEdge, endEdge, startPoint, endPoint, deltaLat, deltaLon);
        this.decimatedStartEdge = startEdge;
        this.decimatedEndEdge = endEdge;
        this.decimatedStartPoint = startPoint;
        this.decimatedEndPoint = endPoint;
        
        this.decimatedPointsGlobal = pointsGlobal;
        return pointsGlobal;
    }
    
    
    private int[][] getDecimatedPointsGlobalHelper(int zoom, int startEdge, int endEdge, 
            int startPoint, int endPoint, 
            int deltaLat, int deltaLon)
    {
        int counter = 0;
        int[][] decimated = new int[8][];
        
        boolean isFirst = true;
        int latAnchor = 0, lonAnchor = 0;
        
        //int minDelta = 2;
        
        RouteEdge edge;
        int lat0, lon0;     
        int nTotal = 0;
        
        // System.out.println("Segment::getDecimatedPoints, startEdge: "+startEdge+", endEdge: "+endEdge+", startPoint: "+startPoint+", endPoint: "+endPoint);
        
        for (int k = startEdge; k < endEdge + 1; k++)
        {
            edge = getEdge(k);
            if (edge == null) continue;
            
            nTotal += edge.numPoints();
            
            int start = 1;
            int end = edge.numPoints() - 1;
            if (k == startEdge && startPoint != -1) start = startPoint + 1;
            if (k == endEdge && endPoint != -1) end = endPoint;
            
            for (int m = start; m < end + 1; m++)
            {
                if (isFirst)
                {
                    lat0 = edge.latAt(m - 1);
                    lon0 = edge.lonAt(m - 1);
                    
                    decimated[counter] = new int[2];
                    decimated[counter][DataUtil.LAT] = lat0;
                    decimated[counter][DataUtil.LON] = lon0;
                    
                    counter++;

                    latAnchor = lat0;
                    lonAnchor = lon0;
                    
                    isFirst = false;
                }
                
                int lat1 = edge.latAt(m);
                int lon1 = edge.lonAt(m);
                
                int dLat = Math.abs(lat1 - latAnchor);
                int dLon = Math.abs(lon1 - lonAnchor);
                
                boolean isLastPoint = (k == endEdge && m == edge.numPoints() - 1);
                
                if (dLat > deltaLat || dLon > deltaLon || isLastPoint)
                {
                    // expand
                    if (counter >= decimated.length)
                    {
                        // System.out.println("Segment::getDecimatedPoints - expand, counter: "+counter+", edge.pointsSize(): "+edge.pointsSize()+", edge index: "+k+", free: "+Runtime.getRuntime().freeMemory()+", zoom: "+decimatedZoom);
                        
                        int[][] temp = new int[2 * decimated.length][];
                        System.arraycopy(decimated, 0, temp, 0, decimated.length);
                        decimated = temp;
                    }
                    
                    decimated[counter] = new int[2];
                    decimated[counter][0] = lat1;
                    decimated[counter][1] = lon1;

                    counter++;

                    latAnchor = lat1;
                    lonAnchor = lon1;
                }
            }
        }

        // please note that we don't copy the actual points, just references to them
        int[][] tempPoints = new int[counter][];
        for (int i = 0; i < counter; i++)
        {
            tempPoints[i] = decimated[i];
            tempPoints[i][DataUtil.LAT] = GlobalCoordinateUtil.earthLatToGlobal(tempPoints[i][DataUtil.LAT]);
            tempPoints[i][DataUtil.LON] = GlobalCoordinateUtil.earthLonToGlobal(tempPoints[i][DataUtil.LON]);
        }

        return tempPoints;
    }

    public AudioDataNode getSegmentAudioNode()
    {
        return segmentAudioNode;
    }

    public void setSegmentAudioNode(AudioDataNode audioNode)
    {
        this.segmentAudioNode = audioNode;
    }

    public int[] getLaneInfos()
    {
        return laneInfos;
    }

    public int[] getLaneTypes()
    {
        return laneTypes;
    }

    public void setLaneInfos(int[] infos)
    {
        this.laneInfos = infos;
    }

    public void setLaneTypes(int[] type)
    {
        this.laneTypes = type;
    }

    public void getEdgesInClip(int clipLatMin, int clipLonMin,
                               int clipLatMax, int clipLonMax, 
                               Vector edgesToAddTo)
    {
        if (edges == null || 
            !DataUtil.intersect(clipLatMin, clipLonMin, clipLatMax, clipLonMax, getBoundingBox()))
        {
            return;
        }
                
        for (int i = 0; i < edges.length; i++)
        {
            RouteEdge edge = edges[i];
                    
            if (edge != null && edge.isInClip(clipLatMin, clipLonMin, clipLatMax, clipLonMax))
            {
                edgesToAddTo.addElement(edge);
            }
        }
    }
    
    public boolean isAudioReady()
    {
        return segmentAudioNode!=null ||infoAudio!=null || prepAudio!=null || actionAudio!=null; 
    }
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    public SegmentAudioAssistance getAudioAssistance()
    {
        return this.audioAssistance;
    }
    
    void setAudioAssistance(SegmentAudioAssistance ass)
    {
        this.audioAssistance = ass;
    }
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void error(String message, boolean flag)
    {
        if (flag)
        {
            throw new IllegalStateException(message);
        }
        else
        {
            Logger.log(Logger.INFO, this.getClass().getName(), message);
        }
    }
    
    public void setSecondsToTurn(int secondsToTurn)
    {
        this.secondsToTurn = secondsToTurn;
    }
    
    public int getSecondsToTurn()
    {
        return this.secondsToTurn;
    }

    public boolean isTightTurn()
    {
        return isTightTurn;
    }

    public void setTightTurn(boolean isTightTurn)
    {
        this.isTightTurn = isTightTurn;
    }
    
    
}
