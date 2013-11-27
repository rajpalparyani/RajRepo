package com.telenav.datatypes.traffic;


public class TrafficSegment 
{
    public static final int TYPE_TRAFFIC_SEGMENT         = 55;
    
    protected int length;        // DM5
    protected int postedSpeed;   // DM6 per second
    protected int delay;         // second
    protected int travelTime;    // second
    protected int avgSpeed;      // DM6 per second
    protected int slowestSpeed;  // DM6 per second
    protected int jamTrend;
    protected long time;         // millis

    protected String name;	
    protected String[] tmcIDs;
	
    protected TrafficIncident[] incidents;
    
    protected TrafficSegment()
    {
        
    }
	
	public int getAvgSpeed() 
	{
		return avgSpeed;
	}
	
	public void setAvgSpeed(int avgSpeed) 
	{
		this.avgSpeed = avgSpeed;
	}
	
	public int getDelay() 
	{
		return delay;
	}
	
	public void setDelay(int delay) 
	{
		this.delay = delay;
	}
	
	public int getJamTrend() 
	{
		return jamTrend;
	}
	
	public void setJamTrend(int jamTrend) 
	{
		this.jamTrend = jamTrend;
	}
	
	public int getLength() 
	{
		return length;
	}
	
	public void setLength(int length) 
	{
		this.length = length;
	}
	
	public String getName() 
	{
		return name;
	}
	
	public void setName(String name) 
	{
		this.name = name;
	}
	
	public int getPostedSpeed() 
	{
		return postedSpeed;
	}
	
	public void setPostedSpeed(int postedSpeed) 
	{
		this.postedSpeed = postedSpeed;
	}
	
	public int getSlowestSpeed() 
	{
		return slowestSpeed;
	}
	
	public void setSlowestSpeed(int slowestSpeed) 
	{
		this.slowestSpeed = slowestSpeed;
	}
	
	public long getTime() 
	{
		return time;
	}
	
	public void setTime(long time) 
	{
		this.time = time;
	}
	
	public String[] getTmcIDs() 
	{
		return tmcIDs;
	}
	
	public void setTmcIDs(String[] tmcIDs) 
	{
		this.tmcIDs = tmcIDs;
	}
	
	public int getTravelTime() 
	{
		return travelTime;
	}
	
	public void setTravelTime(int travelTime) 
	{
		this.travelTime = travelTime;
	}
	
	public TrafficIncident[] getIncidents() 
	{
		return incidents;
	}
	
	public void setIncidents(TrafficIncident[] incidents) 
	{
		this.incidents = incidents;
	}
}
