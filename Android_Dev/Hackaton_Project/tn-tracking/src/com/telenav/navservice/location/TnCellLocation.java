package com.telenav.navservice.location;

import com.telenav.location.TnLocation;
import com.telenav.radio.TnCellInfo;

public class TnCellLocation extends TnLocation
{
    protected TnLocation location;
    protected TnCellInfo cellInfo;
    
    public TnCellLocation(TnLocation location)
    {
        super(location.getProvider());
        super.time = location.getTime();
        super.latitude = location.getLatitude();
        super.longitude = location.getLongitude();
        super.speed = location.getSpeed();
        super.heading = location.getHeading();
        super.altitude = location.getAltitude();
        super.accuracy = location.getAccuracy();
        super.localTime = location.getLocalTimeStamp();
        super.satellites = location.getSatellites();
        super.isValid = location.isValid();
        super.hasEnc = location.isEncrypt();
    }

    public TnCellInfo getCellInfo()
    {
        return cellInfo;
    }

    public void setCellInfo(TnCellInfo cellInfo)
    {
        this.cellInfo = cellInfo;
    }

}
