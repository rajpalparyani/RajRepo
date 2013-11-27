package com.telenav.datatypes.traffic;

import com.telenav.datatypes.audio.AudioDataNode;

public class TrafficIncident 
{
    /*
     * Incident Types
     */
    final static public int TYPE_ACCIDENT = 1;
    final static public int TYPE_CONGESTION = 2;
    final static public int TYPE_CONSTRUCTION = 3;
    final static public int TYPE_DISABLED_CAR = 4;
    final static public int TYPE_EVENT = 5;
    final static public int TYPE_MISC = 6;
    final static public int TYPE_CAMERA = 7;
    final static public int TYPE_SPEED_TRAP = 8;
    
    /*
     * Severity of incidents
     */
    final static public int SEVERITY_SEVERE = 1;
    final static public int SEVERITY_MAJOR = 2;
    final static public int SEVERITY_MINOR = 3;

    
    static final int TYPE_TRAFFIC_INCIDENT        = 54;
    
    protected byte incidentType;
    protected byte severity;
    protected byte laneClosed;
    protected long id;
    protected int lat;				// DM5
    protected int lon;				// DM5
    protected int delay;			// seconds
    protected int edgeID;
    protected String msg;
    protected String crossSt;
    protected AudioDataNode locationAudio;
    protected AudioDataNode streetAudio;
    protected String incidentTypeString;
    protected VectorTrafficEdge edge;
    protected boolean isAvoidIncidentResultInFasterRoute = true;

    protected TrafficIncident()
    {
    }
    
	/**
	 *
	 * @param TrafficIncident
	 * @return
	 *
	 * values:
	 * 0 - TYPE_TRAFFIC_INCIDENT
	 * 1 - incident type
	 * 2 - severity
	 * 3 - laneClosed
	 * 4 - Id
	 * 6 - lat
	 * 7 - lon
	 * 8 - edgeID
	 * 9 - delay
	 *
	 * Msg
	 * 0 - msg
	 * 1 - cross street
     *
	 * Children
	 * 0 - streeAudio
	 * 1 - locationAudio
	 *
	 **/

//	public void toNode(Node node)
//	{
//		node.addValue(TYPE_TRAFFIC_INCIDENT);
//		node.addValue(incidentType);
//		node.addValue(severity);
//		node.addValue(laneClosed);
//		node.addValue(id);
//		node.addValue(lat);
//		node.addValue(lon);
//		node.addValue(edgeID);
//		node.addValue(delay);
//
//		node.addString(msg);
//		node.addString(crossSt);
//		
//		node.addChild(streetAudio);
//		node.addChild(locationAudio);
//	}
//
//	public static TrafficIncident toTrafficIncident(Node node)
//	{
//		TrafficIncident incident = null;
//
//		// Sanitary check
//		if( node != null &&
//			(int)node.getValueAt(0) == TYPE_TRAFFIC_INCIDENT)
//		{
//			incident = new TrafficIncident();
//			incident.incidentType = (byte)node.getValueAt(1);
//			incident.severity = (byte)node.getValueAt(2);
//			incident.laneClosed = (byte)node.getValueAt(3);
//			incident.id = (long)node.getValueAt(4);
//			incident.lat = (int)node.getValueAt(5);
//			incident.lon = (int)node.getValueAt(6);			
//			incident.edgeID = (int)node.getValueAt(7);
//			incident.delay = (int)node.getValueAt(8);
//
//			incident.msg = node.getStringAt(0);
//			incident.crossSt = node.getStringAt(1);
//		}
//		if(incident != null)
//		{
//    		if( node.getChildrenSize() >= 2 )
//    		{
//    			incident.streetAudio = node.getChildAt(0);
//    			incident.locationAudio = node.getChildAt(1);
//    		} else if (node.getChildrenSize() == 1)
//    		{
//    			incident.streetAudio = node.getChildAt(0);
//    		}
////    		incident.incidentTypeString = res.getString( IResourceConstants.RES_TRAFFIC_ACCIDENT + incident.incidentType -1);
//		}
//		return incident;
//	}

	public String getCrossSt() {
		return crossSt;
	}

	public void setCrossSt(String crossSt) {
		this.crossSt = crossSt;
	}

	public int getDelay() {
		return delay;
	}

	public void setDelay(int delay) {
		this.delay = delay;
	}

	public int getEdgeID() {
		return edgeID;
	}

	public void setEdgeID(int edgeID) {
		this.edgeID = edgeID;
	}

	public String getId() {
		return String.valueOf(id);
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getLaneClosed() {
		return laneClosed;
	}

	public void setLaneClosed(byte laneClosed) {
		this.laneClosed = laneClosed;
	}

	public int getLat() {
		return lat;
	}

	public void setLat(int lat) {
		this.lat = lat;
	}

	public AudioDataNode getLocationAudio() {
		return locationAudio;
	}

	public void setLocationAudio(AudioDataNode locationAudio) {
		this.locationAudio = locationAudio;
	}

	public int getLon() {
		return lon;
	}

	public void setLon(int lon) {
		this.lon = lon;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public byte getSeverity() {
		return severity;
	}

	public void setSeverity(byte severity) {
		this.severity = severity;
	}

	public AudioDataNode getStreetAudio() {
		return streetAudio;
	}

	public void setStreetAudio(AudioDataNode streetAudio) {
		this.streetAudio = streetAudio;
	}

    public int getIncidentType()
    {
        return incidentType;
    }
    
    public void setIncidentType(byte type)
    {
        this.incidentType = type;
    }

	//===============================================================
	//  Implementation for IFocusableFeature 
	//===============================================================
	int optionIndex = 0;
	int[] earthLocation;  
	int[] screenPos; 
	boolean isFocused = false;
	//int distJudgeThreshold = 15; // in screen pixel 
	int distJudgeThreshold = 30; // in screen pixel
	
	public void setFocusableFeatureType(byte type) 
	{
		// do nothing
	}

	public void setOptionIndex(int index) 
	{
		this.optionIndex = index ;
	}

	public int getOptionIndex() 
	{
		return this.optionIndex;
	}

	public void setDisplayInfo(String info) 
	{
		// do nothing 
	}

//	public String getDisplayInfo() 
//	{
//		if (this.incidentType > 0 && this.incidentType <= TYPE_MISC)
//			return INCIDENT_TYPES[this.incidentType];
//		else
//			return TRAFFIC_UNKNOWN;
//	}

	public void setEarthLocation(int[] pos) 
	{
		this.earthLocation = pos;
	}

	public int[] getEarchLocation() 
	{
		return this.earthLocation;
	}

	public void setScreenPos(int[] pos) 
	{
		this.screenPos = pos;
	}

	public int[] getScreenPos() 
	{
		return this.screenPos;
	}

	public void setFocused(boolean isFocused) 
	{
		this.isFocused = isFocused;
	}

	public boolean isFocused() 
	{
		return this.isFocused ;
	}

	public void setDistJudgeThreshold(int threshold) 
	{
        this.distJudgeThreshold = threshold;		
	}

	public int getDistJudgeThreshold() 
	{
		return this.distJudgeThreshold;
	}

    public boolean isAvoidIncidentResultInFasterRoute()
    {
        return isAvoidIncidentResultInFasterRoute;
    }

    public void setIsAvoidIncidentResultInFasterRoute(boolean flag)
    {
        this.isAvoidIncidentResultInFasterRoute = flag;
    }
	
}
