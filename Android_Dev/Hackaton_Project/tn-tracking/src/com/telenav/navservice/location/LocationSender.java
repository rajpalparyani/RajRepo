package com.telenav.navservice.location;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Vector;

import com.telenav.location.TnLocation;
import com.telenav.logger.Logger;
import com.telenav.navservice.INotify;
import com.telenav.navservice.Util;
import com.telenav.navservice.model.App;
import com.telenav.navservice.network.TnNetwork;
import com.telenav.navservice.util.Constants;
import com.telenav.radio.TnCellInfo;

public class LocationSender implements Runnable
{
    //Udp packet is limited to 1024 bytes, so we can not send more than 50 fixes at a time
    //use 40 here to give it a buffer
    protected static final int MAX_FIXES_IN_ONE_PACKET          = 40;
    
    protected static final int JOB_FLUSH                        = 1;
    
    protected int gpsSendingIntervalInMillis, cellSendingIntervalInMillis;
    protected boolean isAttachCell;
    protected LocationBuffer gpsBuffer, cellBuffer;
    protected String url;
    protected App app;
    
    protected long lastGpsSendingTime, lastCellSendingTime;
    
    protected Object mutex = new Object();
    protected boolean isRunning, isStarted;
    
    protected Vector jobQueue = new Vector();

    protected TnNetwork network = new TnNetwork();

    protected INotify notify;
    
    public LocationSender(LocationBuffer gpsBuffer, LocationBuffer cellBuffer, INotify notify)
    {
        this.gpsBuffer = gpsBuffer;
        this.cellBuffer = cellBuffer;
        this.notify = notify;
    }
    
    public void setGpsSendingInterval(int gpsSendingInterval, boolean isAttachCell)
    {
        this.gpsSendingIntervalInMillis = gpsSendingInterval * 1000;
        this.isAttachCell = isAttachCell;
        
        this.lastGpsSendingTime = System.currentTimeMillis();
    }
    
    public void setCellSendingInterval(int cellSendingInterval) 
    {
        this.cellSendingIntervalInMillis = cellSendingInterval * 1000;
        
        this.lastCellSendingTime = System.currentTimeMillis();
    }
    
    public void setParameters(            
            String url,
            App app)
    {
        this.url = url;
        this.app = app;
    }
    
    public void start()
    {
        if (isStarted)
        {
            Logger.log(Logger.INFO, this.getClass().getName(), "Can not restart LocationSender, throwing exception");
            throw new IllegalStateException("LocationSender can not be re-used after stop() being called. Create a new instance to use");
        }
        isRunning = true;
        Thread thread = new Thread(this);
        thread.start();
        isStarted = true;
        Logger.log(Logger.INFO, this.getClass().getName(), "LocationSender started");
    }
    
    /**
     * send all locations to server then stop the thread
     */
    public void stop()
    {
        Logger.log(Logger.INFO, this.getClass().getName(), "trying to stop LocationSender");
        flush();
        isRunning = false;
        synchronized(mutex)
        {
            mutex.notify();
        }
    }
    
    /**
     * send all locations to server
     */
    public void flush()
    {
        Logger.log(Logger.INFO, this.getClass().getName(), "trying to flush LocationSender");
        jobQueue.addElement(new Integer(JOB_FLUSH));
        synchronized(mutex)
        {
            mutex.notify();
        }
    }

    public void run()
    {
        long waitTime = 1000;
        while(isRunning || jobQueue.size() > 0)
        {
            try{
                if (jobQueue.size() == 0)
                {
                    Logger.log(Logger.INFO, this.getClass().getName(), "LocationSender: wait time = "+waitTime);
                    synchronized(mutex)
                    {
                        mutex.wait(waitTime);
                    }
                }
                waitTime = 1000;
                
                if (System.currentTimeMillis() - lastGpsSendingTime >= this.gpsSendingIntervalInMillis)
                {
                    sendGpsData(isAttachCell);
                }
                if (System.currentTimeMillis() - lastCellSendingTime >= this.cellSendingIntervalInMillis)
                {
                    sendCellData();
                }
                long waitTimeBeforeNextGpsSending = lastGpsSendingTime + gpsSendingIntervalInMillis - System.currentTimeMillis();
                long waitTimeBeforeNextCellSending = lastCellSendingTime + cellSendingIntervalInMillis - System.currentTimeMillis();
                long minWaitTime = Math.min(waitTimeBeforeNextGpsSending, waitTimeBeforeNextCellSending);
                waitTime = Math.min(15000, minWaitTime);
                if (waitTime <= 0)
                    waitTime = 1000;
                
                if (isAttachCell)
                {
                    if (gpsBuffer.size() + cellBuffer.size() >= MAX_FIXES_IN_ONE_PACKET)
                    {
                        if (gpsBuffer.size() > 0)
                        {
                            sendGpsData(isAttachCell);
                        }
                        else
                        {
                            sendCellData();
                        }
                    }
                }
                else
                {
                    if (gpsBuffer.size() >= MAX_FIXES_IN_ONE_PACKET)
                    {
                        sendGpsData(false);
                    }
                    if (cellBuffer.size() >= MAX_FIXES_IN_ONE_PACKET)
                    {
                        sendCellData();
                    }
                }
                
                synchronized(jobQueue)
                {
                    //finish all jobs in queue even if the isRunning is false. So it can send all data to server even if it's stopped
                    while (jobQueue.size() > 0)
                    {
                        int job = ((Integer)jobQueue.elementAt(0)).intValue();
                        jobQueue.removeElementAt(0);
                        handleJob(job);
                    }
                }
                
                //notify that locationSender is running
                if (this.notify != null && isRunning)
                    this.notify.notify(this);
            }
            catch(Throwable t)
            {
                Logger.log(this.getClass().getName(), t);
            }
        }
        
        Logger.log(Logger.INFO, this.getClass().getName(), "LocationSender stopped");
    }
    
    protected void handleJob(int job) throws Exception
    {
        if (job == JOB_FLUSH)
        {
            handleFlush();
        }
    }
    
    protected void handleFlush() throws Exception
    {
        Logger.log(Logger.INFO, this.getClass().getName(), "Flushing data");
        boolean isSend = sendGpsData(isAttachCell);
        if (!isAttachCell || !isSend)
            sendCellData();
    }
    
    protected boolean sendGpsData(boolean attachCell) throws IOException
    {
        if (gpsBuffer.size() == 0)
            return false;
        
        int size = Math.min(gpsBuffer.size(), MAX_FIXES_IN_ONE_PACKET);
        if (size > 0)
        {
            Vector gpsFixes = gpsBuffer.extractLocations(size);
            Vector cellFixes = null;
            if (attachCell)
            {
                int cellSize = Math.min(cellBuffer.size(), MAX_FIXES_IN_ONE_PACKET - size);
                if (cellSize > 0)
                {
                    cellFixes = cellBuffer.extractLocations(cellSize);
                }
            }
            
            byte[] payload = createPayload(gpsFixes, cellFixes);
            if (payload != null && payload.length > 0)
            {
                lastGpsSendingTime = System.currentTimeMillis();
                Logger.log(Logger.INFO, this.getClass().getName(), "sending gps data: size = "+payload.length);
                Logger.log(Logger.INFO, this.getClass().getName(), "gps data hex: "+Util.toHex(payload));
                network.sendUdp(url, payload);
            }
            else
            {
                Logger.log(Logger.INFO, this.getClass().getName(), "gps data is empty");
            }
            return true;
        }
        else
        {
            Logger.log(Logger.INFO, this.getClass().getName(), "No gps data to send");
        }
        return false;
    }
    
    protected void sendCellData() throws IOException
    {
        if (cellBuffer.size() == 0)
            return;
        
        int size = Math.min(cellBuffer.size(), MAX_FIXES_IN_ONE_PACKET);
        if (size > 0)
        {
            Vector cellFixes = cellBuffer.extractLocations(size);
            
            byte[] payload = createPayload(null, cellFixes);
            if (payload != null && payload.length > 0)
            {
                lastCellSendingTime = System.currentTimeMillis();
                Logger.log(Logger.INFO, this.getClass().getName(), "sending cell data, size = "+payload.length);
                Logger.log(Logger.INFO, this.getClass().getName(), "cell data hex: "+Util.toHex(payload));
                network.sendUdp(url, payload);
            }
            else
            {
                Logger.log(Logger.INFO, this.getClass().getName(), "cell data is empty");
            }
        }
        else
        {
            Logger.log(Logger.INFO, this.getClass().getName(), "No cell data to send");
        }
    }
    
    protected byte[] createPayload(Vector gpsFixes, Vector cellFixes) throws IOException
    {
        int refLat = 0, refLon = 0;
        long refTime = 0;
        if (gpsFixes != null && gpsFixes.size() > 0)
        {
            Logger.log(Logger.INFO, this.getClass().getName(), "prepare gps fixes for sending, count = "+gpsFixes.size());
            int minLat = Integer.MAX_VALUE, minLon = Integer.MAX_VALUE;
            int maxLat = Integer.MIN_VALUE, maxLon = Integer.MIN_VALUE;
            for (int i=0; i<gpsFixes.size(); i++)
            {
                TnLocation loc = (TnLocation)gpsFixes.elementAt(i);
                if (minLat > loc.getLatitude())
                    minLat = loc.getLatitude();
                if (minLon > loc.getLongitude())
                    minLon = loc.getLongitude();
                if (maxLat < loc.getLatitude())
                    maxLat = loc.getLatitude();
                if (maxLon < loc.getLongitude())
                    maxLon = loc.getLongitude();
                
                if (i == 0)
                    refTime = loc.getTime() * 10; //convert to ms
            }
            refLat = (minLat + maxLat) * 10 / 2; //convert to dm6
            refLon = (minLon + maxLon) * 10 / 2; //convert to dm6
        }
        else if (cellFixes != null && cellFixes.size() > 0)
        {
            Logger.log(Logger.INFO, this.getClass().getName(), "prepare cell locations for sending, count = "+cellFixes.size());
            TnCellLocation cellLoc = (TnCellLocation)cellFixes.elementAt(0);
            refLat = cellLoc.getLatitude() * 10; //convert to dm6
            refLon = cellLoc.getLongitude() * 10; //convert to dm6
            refTime = cellLoc.getTime() * 10; //convert to ms
        }
        else
        {
            //It should not run to here
            Logger.log(Logger.INFO, this.getClass().getName(), "shoudl not run to here, something wrong!!!");
            return null;
        }
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream os = new DataOutputStream(baos);
        
        //create header;
        os.writeByte(3); //type
        os.writeByte(1); //version
        os.writeUTF(app.getUserId());
        os.writeByte(app.getApplication());
        os.writeUTF(app.getCarrier());
        os.writeByte(app.getRadioType());
        os.writeByte(app.getPlatform());
        os.writeUTF(app.getDeviceName());
        os.writeUTF(app.getFirmwareVersion());
        os.writeInt(refLat);
        os.writeInt(refLon);
        os.writeLong(refTime);
        os.writeUTF(app.getRouteId());
        os.writeByte(0); //header marker
        
        //create gps data
        if (gpsFixes != null && gpsFixes.size() > 0)
        {
            for (int i=0; i<gpsFixes.size(); i++)
            {
                TnLocation loc = (TnLocation)gpsFixes.elementAt(i);
                os.writeByte(1); //sub type
                os.writeInt((int)(loc.getTime()*10 - refTime));
                writeShort(os, loc.getLatitude()*10 - refLat);
                writeShort(os, loc.getLongitude()*10 - refLon);
                writeShort(os, loc.getHeading());
                writeShort(os, (int)loc.getAltitude());
                os.writeByte((loc.getSpeed() / 9)); //need m/s here
                writeShort(os, loc.getAccuracy());
            }
        }
        
        //create cell data
        if (cellFixes != null && cellFixes.size() > 0)
        {
            for (int i=0; i<cellFixes.size(); i++)
            {
                TnCellLocation loc = (TnCellLocation)cellFixes.elementAt(i);
                TnCellInfo cellInfo = loc.getCellInfo();
                if (cellInfo == null)
                {
                    continue;
                }
                
                if (cellInfo.networkRadioMode == TnCellInfo.MODE_GSM)
                {
                    writeGsm(os, cellInfo, loc, refLat, refLon, refTime);
                }
                else
                {
                    writeCdma(os, cellInfo, loc, refLat, refLon, refTime);
                }
            }
        }
        os.flush();
        byte[] b = baos.toByteArray();
        
        try {
        	os.close();
        } catch (Exception e) {
        	Logger.log(this.getClass().getName(), e);
        }
        
        return b;
    }
    
    protected void writeShort(DataOutputStream os, int value) throws IOException
    {
        if (value > 32767)
            os.writeShort(32767);
        else if (value < -32768)
            os.writeShort(-32768);
        else
            os.writeShort(value);
    }
    
    protected int parseInt(String s)
    {
        try{
            return Integer.parseInt(s);
        }catch(Exception e)
        {
            Logger.log(this.getClass().getName(), e);
        }
        return 0;
    }
    
    protected void writeGsm(DataOutputStream os, TnCellInfo cellInfo, TnCellLocation loc,
            int refLat, int refLon, long refTime) throws IOException
    {
        os.writeByte(2); //sub type
        os.writeInt((int)(loc.getTime()*10 - refTime));
        os.writeByte(Constants.convertNetworkType(cellInfo.networkType));
        writeShort(os, 0); //home MCC
        writeShort(os, 0); //home MNC
        os.writeInt(parseInt(cellInfo.baseStationId));
        os.writeInt(parseInt(cellInfo.cellId));
        os.writeInt(parseInt(cellInfo.locationAreaCode));
        writeShort(os, parseInt(cellInfo.countryCode)); //MCC
        writeShort(os, parseInt(cellInfo.networkCode)); //MNC
        os.writeByte(0); //signal strength
        writeShort(os, 0); //age
        writeShort(os, 0); //timing advance
        writeShort(os, loc.getLatitude()*10 - refLat);
        writeShort(os, loc.getLongitude()*10 - refLon);
        os.writeInt(0); //base station lat
        os.writeInt(0); //base station lon
    }

    protected void writeCdma(DataOutputStream os, TnCellInfo cellInfo, TnCellLocation loc,
            int refLat, int refLon, long refTime) throws IOException
    {
        os.writeByte(4); //sub type
        os.writeInt((int)(loc.getTime()*10 - refTime));
        os.writeInt(parseInt(cellInfo.networkCode));
        os.writeInt(parseInt(cellInfo.locationAreaCode));
        os.writeInt(parseInt(cellInfo.cellId));
        os.writeByte(0); //signal strength
        writeShort(os, 0); //age
        writeShort(os, 0); //time advance
        writeShort(os, loc.getLatitude()*10 - refLat);
        writeShort(os, loc.getLongitude()*10 - refLon);
    }
    
    public boolean isRunning()
    {
        return isRunning;
    }
}
