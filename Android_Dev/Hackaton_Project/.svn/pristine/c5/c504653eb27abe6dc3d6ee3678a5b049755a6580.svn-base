package com.telenav.navservice.location;

import com.telenav.location.ITnLocationListener;
import com.telenav.location.TnLocation;
import com.telenav.location.TnLocationManager;
import com.telenav.location.TnLocationProvider;
import com.telenav.logger.Logger;
import com.telenav.radio.TnCellInfo;
import com.telenav.radio.TnRadioManager;

public class CellRecorder implements ITnLocationListener
{
    protected String provider;
    protected LocationBuffer cellBuffer;
    
    protected int sampleIntervalInMillis;
    
    protected boolean isGettingCellLocation;
    
    public CellRecorder(String provider, LocationBuffer buffer)
    {
        this.provider = provider;
        this.cellBuffer = buffer;
    }
    
    public void setParameters(int sampleInterval) 
    {
        Logger.log(Logger.INFO, this.getClass().getName(), "CellRecorder: setParamters() sampleInterval = "+sampleInterval);
        this.sampleIntervalInMillis = sampleInterval * 1000;
        stopCellLocation();
        if (this.sampleIntervalInMillis > 0)
        {
            //wait sometime for location to stop
            try{
                Thread.sleep(200);
            }catch(Exception e)
            {
                Logger.log(this.getClass().getName(), e);
            }
            startCellLocation();
        }
    }

    public void onLocationChanged(TnLocationProvider provider,
            TnLocation location)
    {
        Logger.log(Logger.INFO, this.getClass().getName(), "cell location received");
        if (location == null || !location.isValid())
        {
            Logger.log(Logger.INFO, this.getClass().getName(), "cell location not valid, return");
            return;
        }
        
        TnCellLocation cellLoc = new TnCellLocation(location);
        TnCellInfo cellInfo = TnRadioManager.getInstance().getCellInfo();
        cellLoc.setCellInfo(cellInfo);
        this.cellBuffer.addLocation(cellLoc);
    }

    public void onStatusChanged(TnLocationProvider provider, int status)
    {
        
    }
    
    protected synchronized void startCellLocation()
    {
        if (isGettingCellLocation)
            return;
        
        Logger.log(Logger.INFO, this.getClass().getName(), "startCellLocation");
        if (TnLocationManager.getInstance() != null)
            TnLocationManager.getInstance().requestLocationUpdates(
                    provider, this.sampleIntervalInMillis, 0, 60000, 0, this);
        isGettingCellLocation = true;
    }
    
    protected synchronized void stopCellLocation()
    {
        if (!isGettingCellLocation)
            return;
        
        Logger.log(Logger.INFO, this.getClass().getName(), "stopCellLocation");
        if (TnLocationManager.getInstance() != null)
            TnLocationManager.getInstance().reset(provider);
        isGettingCellLocation = false;
    }
}
