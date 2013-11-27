package com.telenav.data.datatypes.address;

/**
 * @author alexg
 * 
 */
public class Stop extends CommonDBdata implements Cloneable
{
    public static final int TYPE_STOP = 28;

    public static final int TYPE_AIRPORT = 140;

    // type definition
    public static final byte STOP_GENERIC = 0;

    public static final byte STOP_FAVORITE = 1;

    public static final byte STOP_RECENT = 2;

    public static final byte STOP_WAYPOINT = 3;

    public static final byte STOP_AIRPORT = 4;

    public static final byte STOP_CITY = 5;

    public static final byte STOP_CURRENT_LOCATION = 6;

    public static final byte STOP_POI = 7;

    public static final int CLIENT_STOP_TYPE_CURSOR_ADDRESS = 8;
    
    public static final byte STOP_ANCHOR = 9;

    protected byte stopType = STOP_GENERIC;

    protected int hashCode;

    // these field is not in protocol after ACE 4.0
    protected String label;

    protected String firstLine;

    // new added fields
    protected String houseNumber;

    protected String suite;

    protected String street;

    protected String crossStreet;

    protected String city;

    protected String county;

    protected String state;

    protected String country;

    protected String postalCode;

    protected String subLocality;

    protected String locality;

    protected String locale;

    protected String subStreet;

    protected String buildingName;

    protected String addressId;

    protected int lat;

    protected int lon;

    protected boolean isGeocoded;

    protected byte[] firstLineAudio;

    protected byte[] lastLineAudio;

    protected boolean isSharedAddress = false;
    
    protected int score = -1;

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
        return this.crossStreet;
    }

    public void setCrossStreetName(String crossStreetName)
    {
        this.crossStreet = crossStreetName;
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
        return this.state;
    }

    public void setProvince(String state)
    {
        this.state = state;
    }

    public String getStreetName()
    {
        return this.street;
    }

    public boolean isValid()
    {
        return this.lat != 0 || this.lon != 0;
    }

    public void setStreetName(String streetName)
    {
        this.street = streetName;
    }

    public byte getType()
    {
        return this.stopType;
    }

    public void setType(byte type)
    {
        this.stopType = type;
    }

    public boolean isGeocoded()
    {
        return this.isGeocoded;
    }

    public void setIsGeocoded(boolean b)
    {
        this.isGeocoded = b;
    }

    public String getCombinedCrossStreetName()
    {
        String result = null;
        if (street != null && street.length() > 0 && this.crossStreet != null && this.crossStreet.length() > 0)
        {
            result = this.crossStreet + " at " + this.street; // fix bug 8924 zdong
        }
        return result;
    }
    
    public String getFirstLine()
    {
//        if (firstLine != null)
//            return firstLine;
//
//        if (street != null && street.length() > 0)
//        {
//            if (street.indexOf("\\") > 0)
//            {
//                street = street.substring(0, this.street.indexOf("\\"));
//            }
//            if (this.houseNumber != null && this.houseNumber.length() > 0)
//            {
//                firstLine = this.houseNumber + " " + this.street;
//            }
//            else if (this.crossStreet != null && this.crossStreet.length() > 0)
//            {
//                // firstLine = this.crossStreetName+"@"+this.streetName;
//                if (this.crossStreet.indexOf("\\") > 0)
//                {
//                    this.crossStreet = this.crossStreet.substring(0, this.crossStreet
//                            .indexOf("\\"));
//                }
//                firstLine = this.crossStreet + " at " + this.street; // fix bug 8924 zdong
//            }
//            else
//            {
//                firstLine = this.street;
//            }
//        }
//        else
//        {
//            firstLine = "";
//        }

        return firstLine;
    }

    final static String AT = " at ";

    final static String NONE = "";

    public void parseFirstLine(String firstLine)
    {
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
                setHouseNumber(ind == -1 ? NONE : firstLine.substring(0, ind).trim());
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
            if (this.street != null && this.street.length() > 0
                    && this.crossStreet != null && this.crossStreet.length() > 0)
            {
                String tmpFirstLine = this.crossStreet + " at " + this.street;
                if (equal(tmpFirstLine, stop.getFirstLine()))
                {
                    return true;
                }
                else
                {
                    tmpFirstLine = this.street + " at " + this.crossStreet;
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
        if (!equal(state, stop.getProvince()))
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

    /**
     * @return the county
     */
    public String getCounty()
    {
        return county;
    }

    /**
     * @param county the county to set
     */
    public void setCounty(String county)
    {
        this.county = county;
    }

    public void setAddressId(String addressId)
    {
        this.addressId = addressId;
    }

    public String getAddressId()
    {
        return this.addressId;
    }

    public void setBuildingName(String buildingName)
    {
        this.buildingName = buildingName;
    }

    public String getBuildingName()
    {
        return this.buildingName;
    }

    public void setLocale(String locale)
    {
        this.locale = locale;
    }

    public String getLocale()
    {
        return this.locale;
    }

    public void setLocality(String locality)
    {
        this.locality = locality;
    }

    public String getLocality()
    {
        return this.locality;
    }

    public void setSubLocality(String subLocality)
    {
        this.subLocality = subLocality;
    }

    public String getSubLocality()
    {
        return this.subLocality;
    }

    public void setSubStreet(String subStreet)
    {
        this.subStreet = subStreet;
    }

    public String getSubStreet()
    {
        return this.subStreet;
    }

    public void setSuite(String suite)
    {
        this.suite = suite;
    }

    public String getSuite()
    {
        return this.suite;
    }

    public void setHouseNumber(String houseNumber)
    {
        this.houseNumber = houseNumber;
    }

    public String getHouseNumber()
    {
        return this.houseNumber;
    }
    
    public void setScore(int score)
    {
        this.score = score;
    }
    
    public int getScore()
    {
        return this.score;
    }
    
    public Object clone(){
        Stop stop = null;
        try{
            stop = (Stop)super.clone();
        }catch(CloneNotSupportedException e){
            e.printStackTrace();
        }
        return stop;
    }
}
