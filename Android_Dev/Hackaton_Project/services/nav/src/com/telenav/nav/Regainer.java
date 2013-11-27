package com.telenav.nav;

import java.util.Vector;

import com.telenav.datatypes.DataUtil;
import com.telenav.datatypes.nav.INavEngineConstants;
import com.telenav.datatypes.nav.NavDataFactory;
import com.telenav.datatypes.nav.NavState;
import com.telenav.datatypes.nav.NavUtil;
import com.telenav.datatypes.nav.TnNavLocation;
import com.telenav.datatypes.route.Route;
import com.telenav.datatypes.route.RouteEdge;
import com.telenav.datatypes.route.RouteWrapper;
import com.telenav.datatypes.route.Segment;
import com.telenav.logger.Logger;

/**
 * regains user position on the route, or supplies a best route entry point for ADI
 */
class Regainer
{
    public static final int UNINITIALIZED = -1;

    public static final int SNAP_TO_SUCCESS = 0;

    public static final int OFF_ROUTE = 1;

    public static final int WRONG_WAY = 2;

    public static final int PSEUDO_NAV = 3;

    // add for lineClip method
    public static final int TOP = 0x1;

    public static final int BOTTOM = 0x2;

    public static final int RIGHT = 0x4;

    public static final int LEFT = 0x8;

    protected static final int DIST = 3; // distance index

    protected static final byte EDGE_CANDIDATES = 0;

    protected static final byte NODE_CANDIDATES = 1;

    private static final int SHORTER_ROUTE_SCORE = 5;

    protected NavState lastValid;

    protected int betterCandidateScore = Integer.MIN_VALUE;

    // holds snap result
    protected boolean snapResultIsValid;

    protected int snapResultLat;

    protected int snapResultLon;

    protected int snapResultDeltar;

    protected int snapResultDist;

    protected boolean snapResultForwardAlign;

    protected int snapResultPointIndex;

    protected int snapResultHeadingDiff;

    // holds snap temp result
    protected boolean tempSnapResultIsValid;

    protected int tempSnapResultLat;

    protected int tempSnapResultLon;

    protected int tempSnapResultDeltar;

    protected int tempSnapResultDist;

    protected boolean tempSnapResultForwardAlign;

    protected int tempSnapResultPointIndex;

    protected int tempSnapResultHeadingDiff;

    // holds snap better result
    protected boolean betterSnapResultIsValid;

    protected int betterSnapResultLat;

    protected int betterSnapResultLon;

    protected int betterSnapResultDeltar;

    protected int betterSnapResultDist;

    protected boolean betterSnapResultForwardAlign;

    protected int betterSnapResultPointIndex;

    protected int betterSnapResultHeadingDiff;

    // holds eligible route candidates
    protected int routeCandidateRouteProxy;

    protected Segment routeCandidateSegment;

    protected RouteEdge routeCandidateEdge;

    protected int routeCandidateSegmentIndex;

    protected int routeCandidateEdgeIndex;

    // holds best route candidates
    protected int bestRouteCandidateRouteProxy;

    protected Segment bestRouteCandidateSegment;

    protected int bestRouteCandidateSegmentIndex;

    protected int bestRouteCandidateEdgeIndex;

    // holds current candidates
    protected RouteEdge candidateEdge;

    // holds best candidates
    protected boolean bestCandidateIsValid;

    protected RouteEdge bestCandidateEdge;

    // thresholds and weights
    protected int speedHeadingThresh;

    protected int headingThresh;

    protected int candidateDistThresh;

    protected int headingThreshBits;

    protected int candidateDistThreshBits;

    protected int regainPosWeight;

    protected int regainHeadingWeight;

    protected int regainLastRouteWeight;

    protected int adiPosWeight;

    protected int adiHeadingWeight;

    protected int adiLastValidWeight;

    protected boolean isAdi;

    protected boolean ignoreHeading;

    // regain results
    protected NavState onRoute;

    protected int[] Vb = new int[3];

    protected int[] Va = new int[2];

    protected int[] Vi = new int[4];
    
    protected NavUtil navUtil;

    protected NavEngineJob engineJob;
    
    /**
     * constructor
     * 
     * @param speedHeadinThres [in] speed below which user heading is not considered valid, dm6/s
     * @param headingThreshBits [in] threshold to check user heading and edge alignment, defined as 2^headingThresh
     * @param candidateThreshBits [in] maximum distance which a candidate node or edge will be considere, defined as
     *            2^candidateThresh
     * @param regainThreshBits [in] maximum distance which a user position can be regained, defined as 2^regainThresh
     * @param regainPosWeight [in] position component weight in the regainer scoring formula,
     * @param regainHeadingWeight [in] heading component weight in the regainer scoring formula
     * @param regainLastRouteWeight [in] last route weight in the regainer scoring formula
     * @param adiPosWeight [in] position component weight in the adi scoring formula
     * @param adiHeadingWeight [in] heading component weight in the adi scoring formula
     * @param adiLastValidWeight [in] last valid position component weight in adi scoring formula
     */
    private Regainer(int speedHeadingThresh, int headingThreshBits, int candidateDistThreshBits, int regainPosWeight,
            int regainHeadingWeight, int regainLastRouteWeight, int adiPosWeight, int adiHeadingWeight, int adiLastValidWeight, NavUtil navUtil, NavEngineJob engineJob)
    {
        this.speedHeadingThresh = speedHeadingThresh;
        this.headingThreshBits = headingThreshBits;
        this.candidateDistThreshBits = candidateDistThreshBits;
        this.headingThresh = 1 << this.headingThreshBits;
        this.candidateDistThresh = 1 << this.candidateDistThreshBits;
        this.regainPosWeight = regainPosWeight;
        this.regainHeadingWeight = regainHeadingWeight;
        this.regainLastRouteWeight = regainLastRouteWeight;
        this.adiPosWeight = adiPosWeight;
        this.adiHeadingWeight = adiHeadingWeight;
        this.adiLastValidWeight = adiLastValidWeight;
        this.navUtil = navUtil;
        this.engineJob = engineJob;
    }

    public Regainer(int[] regainerRules, NavUtil navUtil, NavEngineJob engineJob)
    {
        this(regainerRules[INavEngineConstants.RULE_MIN_SPEED], // speed heading threshold
                regainerRules[INavEngineConstants.RULE_HEADING_BITS], // heading threshold bits (2^6 = 64)
                regainerRules[INavEngineConstants.RULE_DISTANCE_BITS], // candidate distance treshold bits (2^10 = 1024)
                regainerRules[INavEngineConstants.RULE_REGAIN_POSITION], // regain position component weight
                regainerRules[INavEngineConstants.RULE_REGAIN_HEADING], // regain heading component weight
                regainerRules[INavEngineConstants.RULE_REGAIN_LAST_ROUTE], // regain last route component weight
                regainerRules[INavEngineConstants.RULE_ADI_POSITION], // adi position component weight
                regainerRules[INavEngineConstants.RULE_ADI_HEADING], // adi heading component weight
                regainerRules[INavEngineConstants.RULE_ADI_LAST_POSITION], // adi last valid position weight
                navUtil, engineJob
        );
    }

    /**
     * clean snap results
     */
    void cleanTempSnapResult()
    {
        tempSnapResultIsValid = false;
        tempSnapResultLat = Integer.MIN_VALUE;
        tempSnapResultLon = Integer.MIN_VALUE;
        tempSnapResultDeltar = Integer.MIN_VALUE;
        tempSnapResultDist = Integer.MAX_VALUE; // must be MAX value, will find the minimal value
        tempSnapResultForwardAlign = false;
        tempSnapResultPointIndex = Integer.MIN_VALUE;
        tempSnapResultHeadingDiff = Integer.MAX_VALUE;
    }

    /**
     * clean better snap results
     */
    void cleanBetterSnapResult()
    {
        betterSnapResultIsValid = false;
        betterSnapResultLat = Integer.MIN_VALUE;
        betterSnapResultLon = Integer.MIN_VALUE;
        betterSnapResultDeltar = Integer.MIN_VALUE;
        betterSnapResultDist = Integer.MAX_VALUE; // must be MAX value
        betterSnapResultForwardAlign = false;
        betterSnapResultPointIndex = Integer.MIN_VALUE;
        betterSnapResultHeadingDiff = Integer.MAX_VALUE;
    }

    /**
     * clean current candidate
     */
    void cleanCurrentCandidate()
    {
        candidateEdge = null;
    }

    /**
     * clean route candidate
     */
    void cleanRouteCandidate()
    {
        routeCandidateRouteProxy = RouteWrapper.ROUTE_NONE;
        routeCandidateSegment = null;
        routeCandidateEdge = null;
        routeCandidateSegmentIndex = Integer.MAX_VALUE;
        routeCandidateEdgeIndex = Integer.MAX_VALUE;
    }

    /**
     * resets regain results
     */
    void resetResults()
    {
        this.onRoute = null;
    }

    /**
     * attempts to regain user on route
     * 
     * @param gpsFixes [in] list of user gps fixes, first element being the latest fix (Vector<GPSData>)
     * @param routes [in] routes
     * @param lastValid [in] last valid user position, used for ADI
     * @param strictHeading [in] true to disallow regain without valid heading
     * @return regainer status
     */
    protected int regain(Vector gpsFixes, NavState lastValid, boolean strictHeading, boolean isAdi,
            boolean ignoreHeading)
    {
        return regain(resolveFixes(gpsFixes), lastValid, strictHeading, isAdi, ignoreHeading);
    }

    /**
     * attempts to regain user on route
     * 
     * @param fix [in] user gps position
     * @param routes [in] routes
     * @param lastValid [in] last valid user position, used for ADI
     * @param strictHeading [in] true to disallow regain without valid heading
     * @param ignoreHeading [in] true to ignore heading check. This is for the initial regain when after route come
     *            back.
     * @return regainer status
     */
    public int regain(TnNavLocation fix, NavState lastValid, boolean strictHeading, boolean isAdi, boolean ignoreHeading)
    {

        this.onRoute = lastValid;
        
        if (lastValid != null && lastValid.getRoute() != RouteWrapper.ROUTE_NONE)
            bestRouteCandidateRouteProxy = lastValid.getRoute();
        else
            bestRouteCandidateRouteProxy = ((Route)RouteWrapper.getInstance().getRoutes().elementAt(0)).getRouteID();

        this.isAdi = isAdi;
        this.ignoreHeading = ignoreHeading;

        if (Logger.DEBUG)
        {
            Logger.log(Logger.INFO, this.getClass().getName(), "Regainer: start regain, isAdi=" + isAdi);
        }
        if (fix == null)
        {
            if (Logger.DEBUG)
            {
                Logger.log(Logger.INFO, this.getClass().getName(), "fix is null in regain");
            }
            return SNAP_TO_SUCCESS;
        }

        if (Logger.DEBUG)
        {
            Logger.log(Logger.INFO, this.getClass().getName(), "regaining with fix: " + fix.getLatitude() + "," + fix.getLongitude() + ","
                    + fix.getSpeed() + "," + fix.getHeading() + "," + fix.getAccuracy());
            Logger.log(Logger.INFO, this.getClass().getName(), "candidate threshold: " + this.candidateDistThresh);

//            Logger.log(Logger.INFO, this.getClass().getName(), "finding candidates...");
        }

        // get the eligible candidates
        bestCandidateIsValid = false;
        betterCandidateScore = Integer.MIN_VALUE;

        this.lastValid = lastValid;
        if (Logger.DEBUG)
        {
            Logger.log(Logger.INFO, this.getClass().getName(), "lastValid onRoute = " + lastValid);
        }
        evaluateBestCandidates(fix, strictHeading);

        if (bestCandidateIsValid || isAdi)
        {
            resetResults();

            this.onRoute = NavDataFactory.getInstance().createNavState(bestRouteCandidateRouteProxy);

            if (RouteWrapper.getInstance().getRoutes() != null && bestRouteCandidateRouteProxy != RouteWrapper.ROUTE_NONE)
            {
                for (int i = 0; i < RouteWrapper.getInstance().getRoutes().size(); i++)
                {
                    if (((Route)RouteWrapper.getInstance().getRoutes().elementAt(i)).getRouteID() == bestRouteCandidateRouteProxy)
                    {
                        RouteWrapper.getInstance().setCurrentRouteId(bestRouteCandidateRouteProxy);
                        break;
                    }
                }
            }

            if (Logger.DEBUG)
            {
                Logger.log(Logger.INFO, this.getClass().getName(), "regainer reset OnRoute " + bestRouteCandidateSegmentIndex + " "
                        + bestRouteCandidateEdgeIndex + " " + snapResultLat + "," + snapResultLon);
            }

            // System.out.println("Regainer::regain, bestRouteCandidateSegmentIndex: "+bestRouteCandidateSegmentIndex);
            this.onRoute.set(bestRouteCandidateSegmentIndex, bestRouteCandidateEdgeIndex, snapResultPointIndex, snapResultDeltar);
            if (this.onRoute.getPosition() != null)
            {
                this.onRoute.getPosition().setLatitude(snapResultLat);
                this.onRoute.getPosition().setLongitude(snapResultLon);
                this.onRoute.getPosition().setTime(fix.getTime());
            }

            if (bestCandidateIsValid)
            {
                if (Logger.DEBUG)
                {
                    Logger.log(Logger.INFO, this.getClass().getName(), "Regainer:returning SNAP_TO_SUCCESS");
                }

                TnNavLocation currPos = this.onRoute.getPosition();
                currPos.set(fix);
                return SNAP_TO_SUCCESS;
            }
            else if (isAdi && bestRouteCandidateSegment != null)
            {
                RouteEdge edge = bestRouteCandidateSegment.getEdge(bestRouteCandidateEdgeIndex);
                int pointIndex = snapResultPointIndex;
                int heading = NavUtil.calcEdgeHeading(edge, pointIndex);
                this.onRoute.getPosition().setHeading(heading);
            }
        }

        if (Logger.DEBUG)
        {
            Logger.log(Logger.INFO, this.getClass().getName(), "Regainer: returning OFF_ROUTE");
        }

        return OFF_ROUTE;
    }

    /**
     * selects better candidate.
     * 
     * @param lastValid [in] last valid user position, used for ADI
     * @return
     */
    private void evaluateBetterCandidate(TnNavLocation fix)
    {
        int curScore;

//        if (Logger.DEBUG)
//        {
//            Logger.log(Logger.INFO, this.getClass().getName(), "finding better candidate");
//        }

        int regainThreshold = calcRegainerThreshold(routeCandidateSegment, routeCandidateEdge, fix);
        if (regainThreshold <= 0)
            regainThreshold = 1;

        // score it
        if (isAdi)
        {
            curScore = scoreADICandidate(betterSnapResultDist, betterSnapResultHeadingDiff, fix.getSpeed(), routeCandidateSegment
                    .getThreshold(Route.THRESHOLD_INDEX_REGAIN) * 2);
        }
        else
        {
            curScore = scoreRegainCandidate(betterSnapResultDist, betterSnapResultHeadingDiff, regainThreshold);
        }

        // add additional weight if candidate is member of last route
        if (lastValid != null && lastValid.getRoute() != RouteWrapper.ROUTE_NONE && routeCandidateRouteProxy == lastValid.getRoute())
        {
//            if (Logger.DEBUG)
//            {
//                Logger.log(Logger.INFO, this.getClass().getName(), "current routeID, add score " + this.regainLastRouteWeight);
//            }

            if (isAdi && fix != null && fix.getSpeed() > fix.getMinUsableSpeed())
            {
                // prefer last route only a little bit in ADI mode, check both bug 54165 and bug 55586
                curScore += 1;
            }
            else
            {
                Route routeCandidateRouteProxyRoute = RouteWrapper.getInstance().getRoute(routeCandidateRouteProxy);
                if (!isAdi && routeCandidateRouteProxyRoute.isLocallyGenerated() && fix != null && fix.getSpeed() > fix.getMinUsableSpeed())
                {
                    // for the same edge in nominal route and local generated route
                    // make the score of nominal route a little bit higher
                    curScore -= 1;
                }
                else
                {
                    curScore += this.regainLastRouteWeight;
                }
            }
        }

//        if (Logger.DEBUG)
//        {
//            Logger.log(Logger.INFO, this.getClass().getName(), "edge: " + routeCandidateSegment.getStreetName() + " " + betterSnapResultLat
//                    + "," + betterSnapResultLon + " " + routeCandidateSegmentIndex + " current score=" + curScore + " max "
//                    + betterCandidateScore);
//        }
        
        Route bestRouteCandidateRouteProxyRoute = RouteWrapper.getInstance().getRoute(bestRouteCandidateRouteProxy);
        Route routeCandidateRouteProxyRoute = RouteWrapper.getInstance().getRoute(routeCandidateRouteProxy);
        if (bestRouteCandidateRouteProxyRoute != null && !bestRouteCandidateRouteProxyRoute.isLocallyGenerated())
        {
            if (bestRouteCandidateRouteProxyRoute.getLength() < routeCandidateRouteProxyRoute.getLength())
            {
                betterCandidateScore += SHORTER_ROUTE_SCORE * 100 / regainThreshold;
            }
            else if (bestRouteCandidateRouteProxyRoute.getLength() > routeCandidateRouteProxyRoute.getLength())
            {
                curScore += SHORTER_ROUTE_SCORE * 100 / regainThreshold;
            }
        }
        if (betterCandidateScore < curScore)
        {
            betterCandidateScore = curScore;

            // set value of bestRouteCandidate
            bestRouteCandidateRouteProxy = routeCandidateRouteProxy;
            bestRouteCandidateSegment = routeCandidateSegment;
            bestRouteCandidateSegmentIndex = routeCandidateSegmentIndex;
            bestRouteCandidateEdgeIndex = routeCandidateEdgeIndex;

            // set value of betterCandidate
            bestCandidateEdge = candidateEdge;

            // set value of snapResult
            snapResultIsValid = true;
            snapResultLat = betterSnapResultLat;
            snapResultLon = betterSnapResultLon;
            snapResultDeltar = betterSnapResultDeltar;
            snapResultDist = betterSnapResultDist;
            snapResultForwardAlign = betterSnapResultForwardAlign;
            snapResultPointIndex = betterSnapResultPointIndex;
            snapResultHeadingDiff = betterSnapResultHeadingDiff;
        }
//        if (Logger.DEBUG)
//        {
//            Logger.log(Logger.INFO, this.getClass().getName(), "best candidate so far --> " + snapResultLat + "," + snapResultLon + " "
//                    + routeCandidateSegmentIndex);
//        }
    }

    /**
     * scores regain candidate according to regain candidate scoring formula (see Regain Design.doc)
     * 
     * @param dist [in] distance from candidate to user position
     * @param headingDiff [in] difference between user heading and edge heading
     * @param regainThresh [in] regain threshold
     * @return numerical score for regain candidate
     */
    private int scoreRegainCandidate(int dist, int headingDiff, int regainThresh)
    {
        int score = (this.regainPosWeight * Math.abs(regainThresh - dist) / regainThresh);

        if (headingDiff < Integer.MAX_VALUE)
        {
            score += (this.regainHeadingWeight * Math.abs(this.headingThresh - headingDiff) >> this.headingThreshBits);
        }
        return score;
    }

    /**
     * scores adi entry point candidate according to adi candidate scoring formula (see Regain Design.doc)
     * 
     * @param dist [in] distance from candidate to user position
     * @param headingDiff [in] difference between user heading and edge heading
     * @param speed [in] user speed
     * @return numerical score for adi candidate
     */
    private int scoreADICandidate(int dist, int headingDiff, int speed, int adiThresh)
    {
        if (dist > this.candidateDistThresh)
            return 0;

        return (this.adiPosWeight * Math.abs(this.candidateDistThresh - dist) >> this.candidateDistThreshBits);

        // QH: don't use heading, which cause bug 14712, adi point jumps
        // + (this.adiHeadingWeight * (1-(Math.abs(180 - headingDiff) / 180)))
        // * ((speed >= this.speedHeadingThresh) ? 1 : 0);
    }

    /**
     * scores adi entry point candidate according to adi candidate scoring formula (see Regain Design.doc)
     * 
     * @param candidate [in] candidate
     * @param fix [in] user fix
     * @param speed [in] user speed
     * @return numerical score for adi candidate
     */
    /*private int scoreADICandidate(int candidateLat, int candidateLon, int cosLat, GpsData fix, int adiThresh)
    {
        // calculate the node metrics
        int adiDist = DataUtil.gpsDistance(fix.getLat() - candidateLat, fix.getLon() - candidateLon, cosLat);
        int adiHeading = DataUtil.bearing(fix.getLat(), fix.getLon(), candidateLat, candidateLon);

        int headingDiff = Math.abs(fix.getHeading() - adiHeading);
        if (headingDiff > 180)
        {
            headingDiff = 360 - headingDiff;
        }

        return scoreADICandidate(adiDist, headingDiff, fix.getSpeed(), adiThresh);
    }*/

    /**
     * evaluates the candidacy of a single edge
     * 
     * @param fix [in] user fix
     * @param route [in] current route
     * @param segment [in] current segment
     * @param edge [in] current edge
     * @param segIndex [in] current segment index
     * @param edgeIndex [in] current edge index
     * @param cosLat [in] scaled cos lat
     * @param strictHeading [in] true to disallow regain without valid heading
     * @param searchedCandidates [in/out] candidates already searched
     * @param unfilteredRegainCandidates [in/out] candidates for regain not filtered with regain threshold. used to
     *            calculate adi candidates later
     * @param shortestCandidate [in/out] stores the shortest distance edge
     */
    private void evaluateEdgeCandidacy(TnNavLocation fix, Route routeProxy, Segment segment, RouteEdge edge, int segIndex, int edgeIndex,
            int cosLat, boolean strictHeading)
    {
        // attempt to snap

        cleanCurrentCandidate();
        cleanBetterSnapResult();

        try
        {
            if ((strictHeading) && (fix.getSpeed() < this.speedHeadingThresh))
            {
                betterSnapResultIsValid = false;
            }
            else
            {
                snapEdge(segment, edgeIndex, fix);
            }
        } // try
        catch (Throwable ex)
        {
            Logger.log(this.getClass().getName(), ex);
            return;
        } // catch

        // adjust regain threshold for adi
        int regainThreshold = 0;
        // if( isAdi )
        // {
        // regainThreshold = segment.getThreshold(INavConstants.THRESHOLD_INDEX_REGAIN) >> 1;
        // }
        // else
        {
            regainThreshold = calcRegainerThreshold(segment, edge, fix);
        }

        // if snapped failed or not within candidate threshold, skip
        if (!betterSnapResultIsValid || (betterSnapResultDist > this.candidateDistThresh) || !betterSnapResultForwardAlign
                || (betterSnapResultDist > regainThreshold))
        {
            if (isAdi)
            {
                // evaluate this segment for adi purpose
                //too many logs, disable it
//                if (Logger.DEBUG)
//                {
//                    Logger.log(Logger.INFO, this.getClass().getName(), "adi special" + betterSnapResultIsValid + "," + betterSnapResultDist
//                            + "," + candidateDistThresh + "," + betterSnapResultForwardAlign + "," + betterSnapResultDist + ","
//                            + (segment.getThreshold(Route.THRESHOLD_INDEX_REGAIN) * 2));
//                }
            }
            else
            {
                //too many logs, disable it
//                if (Logger.DEBUG)
//                {
//                    Logger.log(Logger.INFO, this.getClass().getName(), "return out of evaluateEdgeCandidacy " + segment.getStreetName());
//                }
                return;
            }
        } // if
        else
        {
            bestCandidateIsValid = true;
        }

        /* create new Candidate */
        // save route indicies
        cleanRouteCandidate();
        routeCandidateRouteProxy = routeProxy.getRouteID();
        routeCandidateSegment = segment;
        routeCandidateEdge = edge;
        routeCandidateSegmentIndex = segIndex;
        routeCandidateEdgeIndex = edgeIndex;

        // save edge candidate
        candidateEdge = edge;

        /* score Candidate get better Candidate */
        evaluateBetterCandidate(fix);
    }

    /**
     * finds candidates
     * 
     * @param fix [in] user fix
     * @param routes [in] routes to search
     * @param strictHeading [in] true to disallow regain without valid heading
     * @param regainCandidates [out] candidates for regain. Vector<Candidate>
     * @param unfilteredRegainCandidates [out] candidates for regain not filtered with regain threshold. used to
     *            calculate adi candidates later. Hashtable<edge, Candidate>
     * @param wrongWayCandidates [out] candidates for wrong way. can be set to null. Vector<Candidate>
     * @param shortestCandidate [out] edge that's closes to user fix candidate, size = 1, Vector<Candidate>
     */
    private void evaluateBestCandidates(TnNavLocation fix, boolean strictHeading)
    {
//        if (Logger.DEBUG)
//        {
//            Logger.log(Logger.INFO, this.getClass().getName(), "evaluateBestCandidates");
//        }

        // search all the routes
        Route curRoute;
        Segment curSegment;
        RouteEdge curEdge = null;
        int cosLat = 0;
        for (int routeIndex = 0; routeIndex < RouteWrapper.getInstance().getRoutes().size(); ++routeIndex)
        {
            curRoute = (Route)RouteWrapper.getInstance().getRoutes().elementAt(routeIndex);
//            if (Logger.DEBUG)
//            {
//                Logger.log(Logger.INFO, this.getClass().getName(), "evaluate route = " + routeIndex + ", segments count = "
//                        + curRoute.segmentsSize());
//            }

            int prevSegDistance = 0;
            int curSegDistance = 0;
            int segIncreaseCount = 0;
            int curRouteSize = curRoute.segmentsSize();

            // assemble list of route edges
            for (int segIndex = 0; segIndex < curRouteSize; ++segIndex)
            {
                curSegment = curRoute.segmentAt(segIndex);

                // edges of some segments may not be gotten from server
                if (curSegment.edgesSize() == 0 || !curSegment.isEdgeResolved())
                    continue;

                //too many logs, disable it
//                if (Logger.DEBUG)
//                {
//                    Logger.log(Logger.INFO, this.getClass().getName(), "evaluate segment = " + segIndex + ", edge count = "
//                            + curSegment.edgesSize());
//                }

                cosLat = curSegment.getCosLat();

                // check the distance from the fix to the head of the segment
                // if it's increasing for more then 5 consecutive times, dont bother checking it
                RouteEdge edge = curSegment.getEdge(0);
                if (edge == null || edge.numPoints() == 0)
                    continue;

                curSegDistance = DataUtil.gpsDistance(fix.getLatitude() - edge.latAt(0), fix.getLongitude() - edge.lonAt(0), cosLat);
                // initialize prev distance if necessary
                if (prevSegDistance == 0)
                    prevSegDistance = curSegDistance;

                // porting from CN side(lksun)'s change. DuanBo 2008-06-06
                // fixed lksun
                // if the current seg distance extends the max cursegment range.
                // the double segment length. ignore it anyway.
                if (curSegDistance > (curSegment.getLength() << 1))
                {
                    continue;
                }

                if (curSegDistance > prevSegDistance)
                    segIncreaseCount++;
                else
                    segIncreaseCount = 0;

                // change 5 -> 9 .
                // now encounter a case , 5 is not enough especially for CN road status. so adjust to 9 .
                // if (segIncreaseCount > 5) //only check segIncreaseCount, distance checking may not safe
                if (segIncreaseCount > 9) // only check segIncreaseCount, distance checking may not safe
                {
//                    if (Logger.DEBUG)
//                    {
//                        Logger.log(Logger.INFO, this.getClass().getName(), "breaking out of segments! curdist = " + curSegDistance
//                                + " increase count = " + segIncreaseCount);
//                    }
                    break;// we dont need to go further on this route
                }

                int prevEdgeDistance = 0;
                int curEdgeDistance = 0;
                int edgeIncreaseCount = 0;
                for (int edgeIndex = 0; edgeIndex < curSegment.edgesSize(); ++edgeIndex)
                {
                    curEdge = curSegment.getEdge(edgeIndex);
                    if (curEdge == null)
                        continue;

                    // check the distance from the fix to the head of the edge
                    // if it's increasing for more then 5 consecutive times
                    // or it's over 50,000 DM5, dont bother checking it
                    curEdgeDistance = DataUtil.gpsDistance(fix.getLatitude() - curEdge.latAt(0), fix.getLongitude() - curEdge.lonAt(0), cosLat);

                    // initialize prev distance if necessary
                    if (prevEdgeDistance == 0)
                        prevEdgeDistance = curEdgeDistance;

                    if (curEdgeDistance > prevEdgeDistance)
                        edgeIncreaseCount++;
                    else
                        edgeIncreaseCount = 0;

                    if (edgeIncreaseCount > 5)
                    {
//                        if (Logger.DEBUG)
//                        {
//                            Logger.log(Logger.INFO, this.getClass().getName(), "breaking out of edges ! curdist = " + curEdgeDistance
//                                    + " edge increase count = " + edgeIncreaseCount);
//                        }
                        break;// we dont need to go further on this segment
                    }
                    // FIXME optimization code
                    /**
                     * if (!dataUtil.isInBoundingBox(fix.getLon(), fix.getLat(), curEdge.xmin-maxRegainerDistThreshold,
                     * curEdge.ymin-maxRegainerDistThreshold, curEdge.xmax+maxRegainerDistThreshold,
                     * curEdge.ymax+maxRegainerDistThreshold)) continue;
                     **/
                    evaluateEdgeCandidacy(fix, curRoute, curSegment, curEdge, segIndex, edgeIndex, cosLat, strictHeading);
                }
            }
        }
    }

    /**
     * finds the closet point on the edge to a given fix
     * 
     * @param edge [in] edge
     * @param deltaRs [in] list of deltaR's for edge
     * @param fix [in] user position
     * @param coslat [in] coslat
     * @return snap results
     * @throws Exception
     */
    public boolean snapEdge(Segment seg, int edgeIndex, TnNavLocation fix)
    {
        RouteEdge edge = seg.getEdge(edgeIndex);
        if (edge == null)
            return false;

        int nNumSearchPoints = (edge.numPoints()) - 1;
        for (int i = 0; i < nNumSearchPoints; ++i)
        {
            cleanTempSnapResult();

            int lat = edge.latAt(i);
            int lon = edge.lonAt(i);
            int latNext = edge.latAt(i + 1);
            int lonNext = edge.lonAt(i + 1);

            snap2PtSeg(lat, lon, edge.getDeltaRs(i), latNext, lonNext, fix, seg.getCosLat(),
                (edge.getRoadType() == DataUtil.RAMP || ignoreHeading));
            // save snapped point with shortest distance
            if (tempSnapResultIsValid && tempSnapResultDist < betterSnapResultDist)
            {
                betterSnapResultIsValid = tempSnapResultIsValid;
                betterSnapResultLat = tempSnapResultLat;
                betterSnapResultLon = tempSnapResultLon;
                betterSnapResultDeltar = tempSnapResultDeltar;
                betterSnapResultDist = tempSnapResultDist;
                betterSnapResultForwardAlign = tempSnapResultForwardAlign;
                betterSnapResultPointIndex = i;
                betterSnapResultHeadingDiff = tempSnapResultHeadingDiff;
            }
        }
        return betterSnapResultIsValid;
    }

    /**
     * finds the closes point on the segment to a given fix.
     * 
     * @param headPt [in] head point of a two point segment
     * @param tailPt [in] tail point of a two point segment
     * @param fix [in] real fix
     * @param coslat [in] scaled cos lat
     * @param strictHeading
     * @return snap results
     * @since tn client 3.0
     */
    synchronized void snap2PtSeg(int headPtLat, int headPtLon, int headDeltaR, int tailPtLat, int tailPtLon, TnNavLocation fix, int coslat,
            boolean ignoreHeading)
    {
        Vb[DataUtil.LAT] = tailPtLat - headPtLat;
        Vb[DataUtil.LON] = tailPtLon - headPtLon;
        Vb[DataUtil.DELTAR] = headDeltaR; // Vb length is length of segment

        Va[DataUtil.LAT] = fix.getLatitude() - headPtLat;
        Va[DataUtil.LON] = fix.getLongitude() - headPtLon;

        // if the distance is greater than the tolerence, return null
        tempSnapResultIsValid = true;
        try
        {
            Vi[DataUtil.DELTAR] = DataUtil.project(Vb, Va, coslat);
        }
        catch (Throwable ex)
        {
            // #if DEBUG
            ex.printStackTrace();
            // #endif
            tempSnapResultIsValid = false;
            return;
        }

        // calculate the vector from the head point to the intersect
        if (0 == Vb[DataUtil.DELTAR])
        {
            Vi[0] = 0;
            Vi[1] = 0;
        }
        else
        {
            Vi[0] = (Vb[0] * Vi[DataUtil.DELTAR]) / Vb[DataUtil.DELTAR];
            Vi[1] = (Vb[1] * Vi[DataUtil.DELTAR]) / Vb[DataUtil.DELTAR];
        }

        // there are three cases. The intersect point can be on the segment, before the segment
        // on the same line, or after the segment on the same line. if the point lies on the segment,
        // calculate the intersect using the head point and Vi. If the point does not lie on the segment,
        // we want the segment endpoint closest to the point we just calculated

        if (Vi[DataUtil.DELTAR] < 0 || (Vb[0] == 0 && Vb[1] == 0))
        {
            // if Vi is a negative distance, that means our intersect point lies
            // before the segment. in this case, assign the head point
            tempSnapResultLat = headPtLat;
            tempSnapResultLon = headPtLon;
            tempSnapResultDeltar = 0;
            // BR - 12/15/06 - no DR offset becase we're at the head of the vector
            tempSnapResultDist = DataUtil.gpsDistance(Va[0], Va[1], coslat);
        }
        else if (Vi[DataUtil.DELTAR] > Vb[DataUtil.DELTAR])
        {
            // if Vi length is greather than Vb length, the point lies beyond the segment.
            // in this case, assign the tail point
            tempSnapResultLon = tailPtLon;
            tempSnapResultLat = tailPtLat;
            tempSnapResultDeltar = Vb[DataUtil.DELTAR];

            // BR - 12/15/06 - full DR offset - we're at the tail of the vector
            tempSnapResultDist = DataUtil.gpsDistance(fix.getLatitude() - tailPtLat, fix.getLongitude() - tailPtLon, coslat);
            // RM - 12/15/06 - doubling distance to fake threshold / 4 for this case
            tempSnapResultDist *= this.navUtil.getDevTuningRules()[INavEngineConstants.RULE_REGAINER_OFF_EDGE];
        }
        else
        {
            // if Vi length is shorter than Vb length and is positive, the intersect lies
            // on the segment.
            // tempSnapResultIsValid = true;
            tempSnapResultLat = headPtLat + Vi[0];
            tempSnapResultLon = headPtLon + Vi[1];
            tempSnapResultDeltar = Vi[DataUtil.DELTAR];

            // BR 12/15/06 - projected distance from the head.
            tempSnapResultDist = Math.abs(DataUtil.cross(Vb, Va, coslat));

        }

        // if the distance is greater than the tolerence, don't consider regainable
        // RM - 12/14/06 - need to check whether radial distance is too big vs. threshold
        // because point may not be orthogonal to the segment
        if (tempSnapResultDist > this.candidateDistThresh)
        {
            tempSnapResultIsValid = false;

            // in Adi, continue calculate
            if (!isAdi)
            {
                return;
            }
        }

        // check to see if the fix heading and the segment heading are aligned.
        // only use heading if speed is above minimum threshold
        int align = 0;
        int headingDiff = 0;
        if (fix.getSpeed() >= this.speedHeadingThresh)
        {
            int segmentHeading = DataUtil.bearing(headPtLat, headPtLon, tailPtLat, tailPtLon);

            // calculate heading difference
            headingDiff = Math.abs(segmentHeading - fix.getHeading());
            if (headingDiff > 180)
            {
                headingDiff = 360 - headingDiff;
            }

            tempSnapResultHeadingDiff = headingDiff;
            tempSnapResultForwardAlign = true;

            //http://jira.telenav.com:8080/browse/TNANDROID-2796
            int tempThresh = this.headingThresh;
            if (isAdi)
            {
                if ((180 - headingDiff) < tempThresh)
                {
                    align = 1; // wrong way
                    tempSnapResultIsValid = false;
                }
                return;
            }

            // check alignment
            if (headingDiff < tempThresh)
            {
                align = 0; // same direction
            }
            else if ((180 - headingDiff) < tempThresh)
            {
                align = 1; // wrong way
                tempSnapResultIsValid = false;
            }
            else
            {
                // ignore heading will be true for ramp or intial regain
                // when route come back. When it is true, lossen up heading
                // check. Only let it fail when user is totally opposite
                // with the segment.

                if (ignoreHeading)
                {
                    align = 0;
                }
                else
                {
                    tempSnapResultIsValid = false;
                    align = 2; // wrong heading
                }
            }
        }

        tempSnapResultForwardAlign = (0 == align);
    }

    /**
     * resolves list of gps fixes to one coordinate, speed, and heading
     * 
     * @param gpsFixes [in] list of gps fixes
     * @return single gps fix representing the current user position
     */
    private TnNavLocation resolveFixes(Vector gpsFixes)
    {
        // TODO we may want a smarter implmentation. for now, just return the
        // first fix
        return (TnNavLocation) gpsFixes.firstElement();
    }

    private int calcRegainerThreshold(Segment seg, RouteEdge edge, TnNavLocation gpsfix)
    {
        NavUtil navUtil = this.navUtil;
        int[] rules = navUtil.getThresholdRules();
        int regainThreshold = 0;
        int gpsError = gpsfix.getAccuracy();
        if (gpsError > rules[Route.RULE_MIN_GPS_ERR])
        {
            int rangeDev = navUtil.calcRangeDevThreshold(edge.getRoadType(), gpsfix);
            regainThreshold = (rangeDev * 10 * rules[Route.RULE_REGAIN_RANGE]) >> 10;
        }
        else
        {
            regainThreshold = seg.getThreshold(Route.THRESHOLD_INDEX_REGAIN);
        }
        return regainThreshold;
    }

    public NavState getOnRoute()
    {
        return onRoute;
    }

} // class Regainer
