/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * NavUtil.java
 *
 */
package com.telenav.datatypes.nav;

import com.telenav.datatypes.DataUtil;
import com.telenav.datatypes.route.Route;
import com.telenav.datatypes.route.RouteEdge;
import com.telenav.datatypes.route.Segment;

public class NavUtil
{
    private int[] thresholdRules = new int[]
    { 1638400, 30720, 24576, 0, 4915200, 81920, 0, 0, 49152, 293, 40, 150, 1, 2, 0, 0, 40, 15, 250, 500, 250, 200, 1, 100, 8, 12, 5, 20,
            200, 150, 75, 60, 135, 30, 250, 50000, 95, 0, 5, /*RULE_HEAD_DEV_COUNT*/8, 3, 8, 0, 0 };

    private int[] regainerRules = new int[]
    { 30, 6, 10, 200, 100, 200, 100, 50, 500, 30, 75, 20, 90, 15, 40, 90 };

    private int[] devTuningRules = new int[]
    { 0, 12, 4, -4, 100, 120, 150, 1, 0, 4, 3, 2 };

    private int[][] kTables = new int[][]
    {
        { 1994, 4768, 6712, 7573, 7910, 8050, 8114, 8145 },
        { 2181, 4898, 6761, 7588, 7915, 8052, 8114, 8146 },
        { 6906, 7784, 8001, 8082, 8121, 8143, 8156, 8164 },
        { 6903, 7784, 8001, 8082, 8121, 8143, 8156, 8164 },
        { 119, 42, 13, 4, 2, 1, 0, 0 },
        { 143, 51, 16, 5, 2, 1, 0, 0 },
        { 267, 95, 29, 9, 3, 1, 1, 0 },
        { 213, 75, 23, 7, 2, 0, 0, 0 } 
    };
    
    protected long lastGPSErrorTime = -1;
    protected int lastDevThreshold = -1;

    protected NavUtil()
    {
        
    }
    
    public void loadNavigationRules(NavigationRules navigationRule)
    {
        setNavigationRules(navigationRule);
    }
    
    public static int calcEdgeHeading(RouteEdge edge, int pointIndex)
    {
        if (pointIndex == edge.numPoints() - 1)
            pointIndex--;
        int nextPointIndex = pointIndex + 1;
        int lat1 = edge.latAt(pointIndex);
        int lon1 = edge.lonAt(pointIndex);
        int lat2 = edge.latAt(nextPointIndex);
        int lon2 = edge.lonAt(nextPointIndex);
        int dist = DataUtil.distance(lat1-lat2, lon1-lon2);
        if (dist < 6)
        {
            //the distance is too small, bearing is not accurate
            if (nextPointIndex < edge.numPoints()-1)
            {
                nextPointIndex ++;
                lat2 = edge.latAt(nextPointIndex);
                lon2 = edge.lonAt(nextPointIndex);
            }
            else if (pointIndex > 0)
            {
                pointIndex --;
                lat1 = edge.latAt(pointIndex);
                lon1 = edge.lonAt(pointIndex);
            }
        }
        return DataUtil.bearing(lat1, lon1, lat2, lon2);
    }
    
    public int[] getThresholdRules()
    {
        return thresholdRules;
    }

    public int[] getRegainerRules()
    {
        return regainerRules;
    }

    public int[] getDevTuningRules()
    {
        return devTuningRules;
    }

    public int[][] getKTables()
    {
        return kTables;
    }

    public void adjustDeviationThresholds(TnNavLocation gpsfix, boolean isRamp, NavState onRoute)
    {
        if (!isRamp)
        {
            Segment seg = onRoute.getCurrentSegment();
            RouteEdge edge = onRoute.getCurrentEdge();
            
            int[] rules = thresholdRules;

            int rangeDev = calcRangeDevThreshold(edge.getRoadType(), gpsfix );
            
            //use 90% of that --> convert from Meters to DM5
            //NOTE: x * 900 >> 10 is almost the same as x * 90/100, but faster
            //RM - 09/20/04 - no need, since gpsreader changed to DM5
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

            seg.setThreshold(Route.THRESHOLD_INDEX_RANGE_DEVIATION, rangeDev);
            seg.setThreshold(Route.THRESHOLD_INDEX_NODE_DEVIATION, nodeDev);
            seg.setThreshold(Route.THRESHOLD_INDEX_NEW_ROUTE, newRouteRange);
            seg.setThreshold(Route.THRESHOLD_INDEX_REGAIN, regainRange);
            seg.setNeedReadjustThresholds(true);//so we re-adjust it later
                
        }
    }
    
    public int getRoadError(int roadType)
    {
        if (roadType == DataUtil.ARTERIAL)
            return 15;
        else if (roadType == DataUtil.HIGHWAY)
            return 20;
        else
            return 10;
    }
    
    public void resetRangeDevThreshold()
    {
        lastGPSErrorTime = -1;
        lastDevThreshold = -1;
    }
    
    public int calcRangeDevThreshold(TnNavLocation fix)
    {
        int gpsError = fix.getAccuracy();
        int [] rules = getThresholdRules();

        //cap gps error at min and max          
        gpsError = Math.max(rules[Route.RULE_MIN_GPS_ERR], gpsError);
        gpsError = Math.min(rules[Route.RULE_MAX_GPS_ERR], gpsError);             
    
        // figure out the delta time since last threshold calc
        long deltat = fix.getTime() - lastGPSErrorTime;
        
        //don't calc twice for one gps fix
        if (deltat <= 0)
            return lastDevThreshold;
        
        long threshold = 0;
        
        long gpsGainFactor = rules[Route.RULE_GPS_GAIN_FACTOR];
        long gpsGainScale = (0 != rules[Route.RULE_GPS_GAIN_SCALE]) ?
                  (((long)gpsError * (long)gpsError) * 100 / rules[Route.RULE_GPS_GAIN_SCALE] + 100) :
                  0;
                  
        // cap threshold                        
        long gainThreshold = Math.min(rules[Route.RULE_MAX_RANGE_DEV],
                                        gpsError * gpsGainFactor * gpsGainScale / 10000);                    
        
        // if last thresh not initialized or if the current error size is greater than the
        // last dev threshhold, we want an immediate gain on the threshold, calculated by
        // using the current gps error size.  Otherwise, we want the higher of the gain and decay thesholds
        if (lastGPSErrorTime == -1)
        {
            //TODO: this number should be from server side depends on the segment or edge position
            threshold = gainThreshold;
        }
        else if (gpsError > lastDevThreshold)
        { 
            threshold = gainThreshold; 
        }
        else
        {
            // decay the range deviation
            long scale = DataUtil.pow((int)deltat, rules[Route.RULE_GPS_DECAY_TIME_SCALE], 100);
            long decayFactor = (rules[Route.RULE_GPS_DECAY_FACTOR]) / ((0 == scale) ? 1 : scale);
            long decayThreshold = (((lastDevThreshold - gpsError) * decayFactor) / 100) + gpsError;
              
            threshold = Math.max(gainThreshold, decayThreshold);
        } // else
                          
        // save threshold values
        lastGPSErrorTime = fix.getTime();
        lastDevThreshold = (int)threshold;
        
        return (int)threshold;
    }
    
    public int calcRangeDevThreshold(int roadType, TnNavLocation gpsfix) 
    {
        int roadError = getRoadError(roadType);
        int threshold = calcRangeDevThreshold(gpsfix);
          
        // calculate range deviation threshold as a combination of threshold and decimation error
        int rangeDev = threshold + roadError;
        return rangeDev;
    }

    private void setNavigationRules(NavigationRules rule) 
    {
        if(rule.thresholdRules != null)
        {
            thresholdRules = rule.thresholdRules;
        }
        if(rule.regainerRules != null)
        {
            regainerRules = rule.regainerRules;
        }
        if(rule.devTuningRules != null)
        {
            devTuningRules = rule.devTuningRules;
        }
        if(rule.kTables != null)
        {
            kTables = rule.kTables;
        }
//        if (node == null) return;
//
//        thresholdRules = new int[44];
//        // first 12 are ints
//        for (int j = 1; j <= 12; j++)
//        {
//            thresholdRules[j - 1] = (int) node.getValueAt(j);
//        }
//
//        // next 6 are bytes.
//        for (int j = 13; j <= 18; j++)
//        {
//            thresholdRules[j - 1] = (byte) node.getValueAt(j);
//        }
//
//        // ------ dev thresholds node --------
//        if (node.getChildrenSize() > 0)
//        {
//            Node devRulesNode = node.getChildAt(0);
//
//            // first 4 (18-21) are ints
//            for (int j = 19; j <= 22; j++)
//            {
//                thresholdRules[j - 1] = (int) devRulesNode.getValueAt(j - 18);
//            }
//
//            // next 6 (22-27) are bytes
//            for (int j = 23; j <= 28; j++)
//            {
//                thresholdRules[j - 1] = (byte) devRulesNode.getValueAt(j - 18);
//            }
//
//            // two more ints - map section trigger rule
//            thresholdRules[28] = (int) devRulesNode.getValueAt(11);
//            // new route threshold
//            thresholdRules[29] = (int) devRulesNode.getValueAt(12);
//            // regain threshold
//            thresholdRules[30] = (int) devRulesNode.getValueAt(13);
//            // min gps err threshold
//            thresholdRules[31] = (int) devRulesNode.getValueAt(14);
//            // adi new route threshold
//            thresholdRules[32] = (int) devRulesNode.getValueAt(15);
//            // communication delay time (seconds)
//            thresholdRules[33] = (int) devRulesNode.getValueAt(16);
//            // gps gain factor
//            thresholdRules[Route.RULE_GPS_GAIN_FACTOR] = (int) devRulesNode.getValueAt(17);
//            // gps gain scale
//            thresholdRules[Route.RULE_GPS_GAIN_SCALE] = (int) devRulesNode.getValueAt(18);
//            
//            // time decay factor
//            thresholdRules[Route.RULE_GPS_DECAY_FACTOR] = (int) devRulesNode.getValueAt(19);
//            // time decay scale
//            thresholdRules[Route.RULE_GPS_DECAY_TIME_SCALE] = (int) devRulesNode.getValueAt(20);
//
//            // position deviation counter limit
//            thresholdRules[Route.RULE_POS_DEV_COUNT] = (int) devRulesNode.getValueAt(21);
//            // heading deviation deviation counter limit
//            thresholdRules[Route.RULE_HEAD_DEV_COUNT] = (int) devRulesNode.getValueAt(22);
//            // position deviation regain limit
//            thresholdRules[Route.RULE_POS_DEV_REGAIN_COUNT] = (int) devRulesNode.getValueAt(23);
//            // heading deviation regain limit
//            thresholdRules[Route.RULE_HEAD_DEV_REGAIN_COUNT] = (int) devRulesNode.getValueAt(24);
//            
//            // --------------- regainer rules ----------
//            if (node.getChildrenSize() > 1)
//            {
//                for (int i = 1; i < node.getChildrenSize(); i++)
//                {
//                    Node curNode = node.getChildAt(i);
//                    int nodeType = (int) curNode.getValueAt(0);
//                    
//                    if (nodeType == INavEngineConstants.TYPE_REGAINER_RULES)
//                    {
//                        // ---------------- regainer rules -------------
//                        regainerRules = new int[curNode.getValuesSize() - 1];//first
//                        // one
//                        // is
//                        // TYPE
//                        for (int j = 0; j < curNode.getValuesSize() - 1; j++)
//                        {
//                            regainerRules[j] = (int) curNode.getValueAt(j + 1);
//                        }
//                    }
//                    else if (nodeType == INavEngineConstants.TYPE_ZOOM_RULES)
//                    {
//                        // ---------------- zoom rules -------------
//                        int numLevels = curNode.getValuesSize() - 1; // first
//                        // one
//                        // is
//                        // TYPE.
//                        byte[] zoomRules = new byte[numLevels];
//                        for (int j = 0; j < numLevels; j++)
//                        {
//                            zoomRules[j] = (byte) curNode.getValueAt(j + 1);
//                        }
//                    }
//                    else if (nodeType == INavEngineConstants.TYPE_K_VALUES)
//                    {
//                        // ----------- k-tables --------------
//                        if (curNode.getBinaryData() != null && curNode.getBinaryData().length > 0)
//                        {
//                            int len = (curNode.getBinaryData().length >> 4);// 2
//                            // bytes
//                            // per
//                            // entry,
//                            // 8
//                            // tables
//
//                            kTables = new int[8][len];
//                            for (int j = 0; j < len; j++)
//                            {
//                                kTables[INavEngineConstants.K11][j] = DataUtil.readShort(curNode.getBinaryData(), j << 4);
//                                kTables[INavEngineConstants.K22][j] = DataUtil.readShort(curNode.getBinaryData(), (j << 4) + 2);
//                                kTables[INavEngineConstants.K33][j] = DataUtil.readShort(curNode.getBinaryData(), (j << 4) + 4);
//                                kTables[INavEngineConstants.K44][j] = DataUtil.readShort(curNode.getBinaryData(), (j << 4) + 6);
//                                kTables[INavEngineConstants.K13][j] = DataUtil.readShort(curNode.getBinaryData(), (j << 4) + 8);
//                                kTables[INavEngineConstants.K24][j] = DataUtil.readShort(curNode.getBinaryData(), (j << 4) + 10);
//                                kTables[INavEngineConstants.K31][j] = DataUtil.readShort(curNode.getBinaryData(), (j << 4) + 12);
//                                kTables[INavEngineConstants.K42][j] = DataUtil.readShort(curNode.getBinaryData(), (j << 4) + 14);
//                            }
//                        }
//                    }
//                }
//
//            }
//
//        }
//        
//        if (node.getValuesSize() >= 21)
//        {
//            thresholdRules[Route.RULE_LAST_TRIP_INFO_EXPIRE_TIME] = (int) node.getValueAt(19);
//            thresholdRules[Route.RULE_DEV_GEOFENCE_RADIUS] = (int) node.getValueAt(20);
//        }
    }

    public int getLastDevThreshold()
    {
        return lastDevThreshold;
    }

}
