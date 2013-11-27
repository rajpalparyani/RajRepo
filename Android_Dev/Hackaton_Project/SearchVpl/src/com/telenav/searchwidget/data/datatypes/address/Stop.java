package com.telenav.searchwidget.data.datatypes.address;

/**
 * @author alexg
 *
 */
public class Stop
{
    public static final int TYPE_STOP                               = 28;
    public static final int TYPE_AIRPORT                               = 140;

    //type definition
    public static final byte STOP_GENERIC   = 0;
    public static final byte STOP_FAVORITE  = 1;
    public static final byte STOP_RECENT    = 2;
    public static final byte STOP_WAYPOINT  = 3;
    public static final byte STOP_AIRPORT   = 4;
    public static final byte STOP_CITY      = 5;
    public static final byte STOP_CURRENT_LOCATION  = 6; 
    public static final byte STOP_POI       = 7;
    public static final int CLIENT_STOP_TYPE_CURSOR_ADDRESS = 8;
    
    //public static final byte STOP_CURSOR_ADDRESS  = 7;
    //removed by zhdong, there should be no this type
    
    //status definition
    public static final byte STOP_UNCHANGED = 0;
    public static final byte STOP_ADDED     = 1;
    public static final byte STOP_DELETED   = 2;
    public static final byte STOP_RENAMED   = 3; 

    protected byte stopType = STOP_GENERIC;
    protected byte stopStatus = STOP_UNCHANGED;
    protected int hashCode;
    
    protected int lat;
    protected int lon; 
    protected long id;

    protected String city;
    protected String country;
    protected String crossStreetName;
    protected String postalCode;
    protected String province;
    protected String streetName; 
    protected String streetNumber;
    protected String label;
    protected String firstLine;

    protected boolean isGeocoded;
        
    protected byte[] firstLineAudio;
    protected byte[] lastLineAudio;
    
    protected boolean isSharedAddress = false;

    public Stop()
    {
        
    }
    
    public String getCity()
    {
        return city;
    }

    public void setCity(String city)
    {
        this.city = city;
    }

    public String getLabel()
    {
        return label;
    }
    
    public void setLabel(String label)
    {
        this.label = label;
    }

    public long getID()
    {
        return this.id;
    }
    
    public void setID(long id)
    {
        this.id = id;
    }
        
    public String getCountry()
    {
            return this.country;
    }

    public void setCountry(String country)
    {
            this.country = country;
    }

    public String getCrossStreetName()
    {
            return this.crossStreetName;
    }

    public void setCrossStreetName(String crossStreetName)
    {
            this.crossStreetName = crossStreetName;
    }

    public int getLat()
    {
            return lat;
    }

    public void setLat(int lat)
    {
            this.lat = lat;
    }

    public int getLon()
    {
            return this.lon;
    }

    public void setLon(int lon)
    {
            this.lon = lon;
    }

    public String getPostalCode()
    {
            return this.postalCode;
    }

    public void setPostalCode(String postalCode)
    {
            this.postalCode = postalCode;
    }

    public String getProvince()
    {
            return this.province;
    }

    public void setProvince(String province)
    {
            this.province = province;
    }

    public String getStreetName()
    {
            return this.streetName;
    }

    public boolean isValid()
    {
        return this.lat != 0 || this.lon != 0;
    }
    
    public void setStreetName(String streetName)
    {
            this.streetName = streetName;
    }

    public String getStreetNumber()
    {
            return this.streetNumber;
    }

    public void setStreetNumber(String streetNumber)
    {
            this.streetNumber = streetNumber;
    }

    public byte getType()
    {
        return this.stopType;
    }
    
    public void setType(byte type)
    {
        this.stopType = type;
    }
    
    public byte getStatus()
    {
            return this.stopStatus;
    }
    
    public void setStatus(byte status)
    {
            this.stopStatus = status;
    }
        
    public boolean isGeocoded()
    {
        return this.isGeocoded;
    }
    
    public void setIsGeocoded(boolean b)
    {
        this.isGeocoded = b;
    }
        
    public String getFirstLine()
    {
        if (firstLine != null)
            return firstLine;
        
        if (streetName != null && streetName.length() > 0)
        {
            if(streetName.indexOf("\\") > 0)
            {
                streetName = streetName.substring(0, this.streetName.indexOf("\\"));
            }
            if (this.streetNumber != null && this.streetNumber.length() > 0)
            {
                firstLine = this.streetNumber + " " + this.streetName;
            }
            else if (this.crossStreetName != null && this.crossStreetName.length() > 0)
            {
                //firstLine = this.crossStreetName+"@"+this.streetName;    
                if(this.crossStreetName.indexOf("\\") > 0)
                {
                    this.crossStreetName = this.crossStreetName.substring(0, this.crossStreetName.indexOf("\\"));
                }
                firstLine = this.crossStreetName+" at "+this.streetName; // fix bug 8924 zdong
            }
            else
            {
                firstLine = this.streetName; 
            }
        }
        else
        {
            firstLine = "";
        }
        

        return firstLine;
    }

    final static String AT = " at ";
    final static String NONE = "";
    
    public void parseFirstLine(String firstLine)
    {
		if(firstLine == null)
		{
			return;
		}
		
        firstLine = firstLine.trim();
        if (firstLine != null && firstLine.length() > 0)
        {
            int ind = firstLine.indexOf('@');
            int length = 1;
            if (ind == -1)
            {
                ind = firstLine.indexOf(AT);
                length = 4;
            }

            if (ind != -1)
            {
                setCrossStreetName(firstLine.substring(0, ind).trim());
                setStreetName(firstLine.substring(ind + length).trim());
            }
            else
            {
                // number street
                ind = firstLine.indexOf(' ');
                setStreetNumber(ind == -1 ? NONE : firstLine.substring(0, ind).trim());
                setStreetName(firstLine.substring(ind + 1).trim());
            }
        }
    }
    
    public void setFirstLine(String firstLine)
    {
        this.firstLine = firstLine;
    }
        
    public boolean equalsIgnoreCase(Stop stop)
    {
        if (stop == null)
            return false;

        // fix bug 12665 
        // compare cross stop . maybe upset with cross and street
        if (!equal(firstLine, stop.getFirstLine()))
        {
        	// we must return false when the firstLine are different. fix bug 72978.
            if (this.streetName != null && this.streetName.length() > 0 && this.crossStreetName != null && this.crossStreetName.length() > 0)
            {
                String tmpFirstLine =  this.crossStreetName+" at "+this.streetName;
                if (equal(tmpFirstLine, stop.getFirstLine()))
                {
                	return true;
                }
                else
                {
                    tmpFirstLine = this.streetName +" at "+ this.crossStreetName;
                    if (equal(tmpFirstLine, stop.getFirstLine()))
                    {
                    	return true;
                    }
                }
            }
            return false;
        }
        if (!equal(city, stop.getCity()))               
            return false;           
        if (!equal(province, stop.getProvince()))
            return false;           
        if (!equal(postalCode, stop.getPostalCode()))
            return false;           
        if (Math.abs(lat - stop.getLat()) > 5 || Math.abs(lon - stop.getLon()) > 5)
            return false;           
            
        return true;
    }
        
    protected static boolean equal(String s1, String s2)
    {
        if (s1 != null)
            s1 = s1.toLowerCase().trim();
        if (s2 != null)
            s2 = s2.toLowerCase().trim();

        return isEmpty(s1) ? isEmpty(s2) : s1.equals(s2);
    }

    public byte[] getFirstLineAudio()
    {
        return firstLineAudio;
    }
    
    public void setFirstLineAudio(byte[] firstLineAudio)
    {
        this.firstLineAudio = firstLineAudio;
    }

    public byte[] getLastLineAudio()
    {
        return this.lastLineAudio;
    }
    
    public void setLastLineAudio(byte[] lastLineAudio)
    {
        this.lastLineAudio = lastLineAudio;
    }
    
    public boolean isSharedAddress()
    {
        return this.isSharedAddress;
    }
    
    public void setSharedAddress(boolean isSharedAddress)
    {
        this.isSharedAddress = isSharedAddress;
    }

    public static boolean isEmpty(String s)
    {
        return s == null || s.length() == 0;
    }
}
