package com.telenav.data.datatypes.address;

import java.util.Vector;

import com.telenav.data.datatypes.poi.Poi;
import com.telenav.location.TnLocation;
import com.telenav.sort.IComparable;

public class Address extends CommonDBdata implements IComparable
{
    public static final int TYPE_STOP_POI_WRAPPER        = 199;
    //FLAG
    public static final byte TYPE_FAVORITE_POI      = 0;
    public static final byte TYPE_FAVORITE_STOP     = 1;
    public static final byte TYPE_RECENT_POI        = 2;
    public static final byte TYPE_RECENT_STOP       = 3;

    public static final byte TYPE_FAVORITE = 4;
    public static final byte TYPE_RECENT = 5 ;
    public static final byte TYPE_HOME = 6 ;
    public static final byte TYPE_WORK = 7 ;
    public static final byte TYPE_AIRPORT = 8 ;
    public static final byte TYPE_ALL = 9 ;
    
    
    public static final byte TYPE_GROUP = 127 ;
    
    public static final byte SOURCE_UNKOWN           = 0;
    public static final byte SOURCE_RECENT_PLACES    = 1;
    public static final byte SOURCE_RESUME_TRIP      = 2;
    public static final byte SOURCE_FAVORITES        = 3;
    public static final byte SOURCE_RECEIVED_ADDRESS = 4;
    public static final byte SOURCE_SEARCH           = 5;
    public static final byte SOURCE_MAP              = 6;
    public static final byte SOURCE_EMAIL            = 7;
    public static final byte SOURCE_CALENDAR         = 8;
    public static final byte SOURCE_CONTACTS         = 9;
    public static final byte SOURCE_API              = 10;
    public static final byte SOURCE_DSR              = 11;
    public static final byte SOURCE_PREDEFINED       = 13;
    public static final byte SOURCE_TYPE_IN          = 14;

    protected Stop stop = null;
    protected Poi	poi	 = null;
    protected String phoneNumber;
    protected byte type = TYPE_RECENT_STOP;
    protected byte source;
    protected Vector categories;
    //For Browser to judge it is a poi or a stop
    protected String entityType;

    //[MRD]Label is a required field that must be unique.
    protected String label = "";
    protected long   createTime;
    protected long   updateTime;
    protected String sharedFromUser = "";
    protected String sharedFromPTN  = "";
    
    protected int selectedIndex;

    protected boolean isExistedInFavorite;
    
    protected boolean isReadByUser ;
    
    //these varible are not consistent, be careful to use them
    protected int distance;
    
    protected TnLocation currentLocation;
    
    protected long eventId;
    
    protected long eventStartTime = -1;
    
    protected long eventEndTime = -1;
    
    protected boolean useEventStartTime;
    
    protected boolean useEventEndTime;
    
    protected String venue = null;
    
    protected boolean isEventDataAvailable = false;
    
    public Address()
    {
        this(SOURCE_UNKOWN);
    }
    
    public boolean isReadByUser()
    {
        return isReadByUser;
    }

    public void setReadByUser(boolean isReadByUser)
    {
        this.isReadByUser = isReadByUser;
    }

    public Address(byte source)
    {
        this.source = source;
        this.status = ADDED ; 
    }

    public Vector getCatagories() 
    {
        return this.categories;
    }

    public String getLabel() 
    {
        if (label == null)
        {
            label = "";
        }
        return this.label;
    }
    
    public long getCreateTime()
    {
        return this.createTime;
    }
    
    public void setCreateTime(long createTime)
    {
        this.createTime = createTime;
        this.updateTime = createTime;
    }
    
    public long getUpdateTime()
    {
        return this.updateTime;
    }
    
    public void setUpdateTime(long updateTime)
    {
        this.updateTime = updateTime;
    //   convertToString(updateTime,label);
    }
    
 // Fix the null label issue of getLabel()
    public String getDisplayedText()
    {
        String label = this.getLabel();
        if (label == null || label.trim().length() == 0)
        {
            Poi poi = this.getPoi();
            if (poi != null && poi.getBizPoi() != null && poi.getBizPoi().getBrand() != null && poi.getBizPoi().getBrand().trim().length() > 0)
            {
                label = poi.getBizPoi().getBrand();
            }
            
            if(label == null || label.trim().length() == 0)
            {
                label = getStopText(this.getStop());
            }
        }
        return label;
    }

    public String getName()
    {
        String label = this.getLabel();
        if (label == null || label.trim().length() == 0)
        {
            Poi poi = this.getPoi();
            if (poi != null && poi.getBizPoi() != null && poi.getBizPoi().getBrand() != null && poi.getBizPoi().getBrand().trim().length() > 0)
            {
                label = poi.getBizPoi().getBrand();
            }
            
            if(label == null || label.trim().length() == 0)
            {
                label = getStopLabel(this.getStop());
            }
        }
        return label;
    }

    private String getStopLabel(Stop stop)
    {
        if (stop == null)
        {
            return null;
        }
        String label = stop.getLabel();
        if (label != null && label.trim().length() != 0)
        {
            return label;
        }
        return null;
    }

    private String getStopText(Stop stop)
    {
        if(stop == null)
        {
            return null;
        }        
        String label = stop.getLabel();
        if(label != null && label.trim().length() != 0)
        {
            return label;
        }
        String firstLine = stop.getFirstLine();
        String city = stop.getCity();
        String province = stop.getProvince();
        if (firstLine != null && firstLine.trim().length() != 0)
        {
            label = firstLine;
        }
        else if (city != null && city.trim().length() != 0)
        {
            label = city;
        }
        else if (province != null && province.trim().length() != 0)
        {
            label = province;
        }
        else
        {
            label = stop.getLat() + ", " + stop.getLon();
        }
        return label;
    }

    public Poi getPoi() 
    {
        return this.poi;
    }

    public Stop getStop() 
    {
        if (this.stop != null)
        {
            return this.stop;
        }
        else if (this.poi != null)
        {
            if (this.poi.getStop() != null)
                return this.poi.getStop();
            else if (this.poi.getBizPoi() != null)
                return this.poi.getBizPoi().getStop();
        }

        return null;
    }

    public byte getType() 
    {
        return this.type;
    }
    
    public byte getSource()
    {
        return this.source;
    }
    
    public void setSource(byte source)
    {
        this.source = source;
    }
    
    public String getSharedFromUser()
    {
        return this.sharedFromUser ;
    }

    public void setSharedFromUser(String sharedFromUser)
    {
        this.sharedFromUser = sharedFromUser;
    }
    
    public String getSharedFromPTN()
    {
        return this.sharedFromPTN;
    }
    
    public void setSharedFromPTN(String sharedFromPTN)
    {
        this.sharedFromPTN = sharedFromPTN;
    }

    public void setSelectedIndex(int selectedIndex)
    {
        this.selectedIndex = selectedIndex;
    }
    
    public int getSelectedIndex()
    {
        return this.selectedIndex;
    }
    
    public void setCatagories(Vector vec) 
    {
        this.categories = vec;
    }

    public void setLabel(String label) 
    {
        this.label = label;
    }

    public void setPoi(Poi poi) {

        this.poi = poi ;
        if(this.label == null || this.label.trim().length() <= 0)
        {
            // wrap poi
            this.label = getDisplayedText();
        }
    }
    
    public void setEntityType(String entityType)
    {
        this.entityType = entityType;
    }
    
    public String getEntityType()
    {
        return this.entityType;
    }
    
    public void setNearbyStop(Stop newStop)
    {
        this.stop = newStop ;
        if(this.label == null || this.label.trim().length() <= 0 )
        {
            // wrap poi
            String strView = getDisplayedText();
            if(strView != null && strView.trim().length() > 0)
            {
                this.label = strView;
            }
        } 
    }

    public void setStop(Stop newStop) 
    {
        //this.type = TYPE_RECENT_STOP ;
        this.stop = newStop ;
        if(this.label == null || this.label.trim().length() <= 0 )
        {
            // wrap poi
            String strView = getDisplayedText();
            if(strView != null && strView.trim().length() > 0)
            {
                this.label = strView;
            }
        } 
        
        if(stop != null && (stop.getLabel() == null || stop.getLabel().length() <= 0)
                && (label != null && label.length() > 0))
        {
            stop.setLabel(label);
        }
    }

    public void setType(int type) 
    {
        this.type = (byte)type;
    }

    public boolean isSameAddress(Address desAddr)
    {
        if (this.type != desAddr.getType()) return false;

        if (this.type == TYPE_RECENT_POI || this.type == TYPE_FAVORITE_POI)
        {
            if (this.getPoi() != null && this.getPoi().getBizPoi() != null && this.getPoi().getBizPoi().getPoiId() != null 
                    && desAddr.getPoi() != null && desAddr.getPoi().getBizPoi() != null && desAddr.getPoi().getBizPoi().getPoiId() != null
                    && this.getPoi().getBizPoi().getPoiId().equals( desAddr.getPoi().getBizPoi().getPoiId()))
            {
                return true;
            }    
        } 
        else 
        {
            if (this.getStop().equalsIgnoreCase(desAddr.getStop()))
            {
                //fix 54703
                //only the address with same stop and same label will treat as same address.
                if (((this.label == null || this.label.length() == 0) && (desAddr.getLabel() == null || desAddr.getLabel().length() == 0))
                        || this.label.equalsIgnoreCase(desAddr.getLabel()))
                    return true;
            }
        }
        return false;
    }

    boolean isCursorAddress;

    public boolean isCursorAddress()
    {
        return isCursorAddress;
    }

    public void setCursorAddress(boolean isCursorAddress)
    {
        this.isCursorAddress = isCursorAddress;
    }

    public String getPhoneNumber()
    {
    	if(phoneNumber == null || phoneNumber.length() == 0)
    	{
    		if(this.getPoi() != null && this.getPoi().getBizPoi() != null)
    		{
    			phoneNumber = this.getPoi().getBizPoi().getPhoneNumber();
    		}
    	}
        return phoneNumber;
    }

    public void setPhoneNumber(String s)
    {
        this.phoneNumber = s;
    }

    public void setExistedInFavorite(boolean isExistedInFavorite)
    {
        this.isExistedInFavorite = isExistedInFavorite;
    }
    
    public boolean isExistedInFavorite()
    {
        return this.isExistedInFavorite;
    }
    
    public boolean isValid()
    {
        Stop stop = this.getValidStop();
        if(stop != null && stop.isValid())
        {
            return true;
            
        }else
        {
            return false;
        }
    }
    
    protected Stop getValidStop() 
    {
        if (this.stop != null && this.stop.isValid())
        {
            return this.stop;
        }
        else if (this.poi != null)
        {
            if (this.poi.getStop() != null && this.poi.getStop().isValid())
                return this.poi.getStop();
            else if (this.poi.getBizPoi() != null)
            {
                if (this.poi.getBizPoi().getStop() != null
                        && this.poi.getBizPoi().getStop().isValid())
                    return this.poi.getBizPoi().getStop();
            }
        }

        return null;
    }

    @Override
    public int compareTo(Object another)
    {
        if (!(another instanceof Address))
        {
            throw new RuntimeException("another is not an Address Object" + another);
        }

        Address anotherAddress = (Address) another;

        long timeStamp = this.updateTime > 0 ? this.updateTime : this.createTime;
        long anotherTimeStamp = anotherAddress.updateTime > 0 ? anotherAddress.updateTime : anotherAddress.createTime;

        if (timeStamp > anotherTimeStamp)
        {
            return 1;
        }
        else if (timeStamp == anotherTimeStamp)
        {
            return 0;
        }
        else
        {
            return -1;
        }
    }
    
    public int getDistance()
    {
        return distance;
    }

    public void setDistance(int distance)
    {
        this.distance = distance;
    }

    public TnLocation getCurrentLocation()
    {
        return currentLocation;
    }

    public void setCurrentLocation(TnLocation currentLocation)
    {
        this.currentLocation = currentLocation;
    }
    
  /* public static String convertToString(long timestamp,String label)
    {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddhhmmss");
        String str = df.format(timestamp);
        System.out.println("update time is "+str+ "with label"+label);
        return str;
    }*/
    
    public void setEventId(long eventId)
    {
        this.eventId = eventId;
    }
    
    public long getEventId()
    {
        return this.eventId;
    }
    
    public void setEventStartTime(long eventStartTime)
    {
        this.eventStartTime = eventStartTime;
    }
    
    public long getEventStartTime()
    {
        return this.eventStartTime;
    }
    
    public void setEventEndTime(long eventEndTime)
    {
        this.eventEndTime = eventEndTime;
    }
    
    public long getEventEndTime()
    {
        return eventEndTime;
    }
    
    public void setUseEventStartTime(boolean useEventStartTime)
    {
        this.useEventStartTime = useEventStartTime;
    }
    
    public boolean isUseEventStartTime()
    {
        return this.useEventStartTime;
    }
    
    public void setUseEventEndTime(boolean useEventEndTime)
    {
        this.useEventEndTime = useEventEndTime;
    }
    
    public boolean isUseEventEndTime()
    {
        return this.useEventEndTime;
    }
    
    public void setEventVenue(String venue)
    {
        this.venue = venue;
    }
    
    public String getEventVenue()
    {
        return this.venue;
    }
    
    public void setIsEventDataAvailable(boolean isEventDataAvailable)
    {
        this.isEventDataAvailable = isEventDataAvailable;
    }
    
    public boolean isEventDataAvailable()
    {
        return isEventDataAvailable;
    }
}
