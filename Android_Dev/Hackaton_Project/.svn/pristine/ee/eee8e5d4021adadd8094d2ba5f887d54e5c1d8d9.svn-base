package com.telenav.gps;

import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

import com.telenav.location.AbstractTnMockLocationProvider;
import com.telenav.location.TnLocation;
import com.telenav.location.TnLocationException;
import com.telenav.logger.Logger;
import com.telenav.persistent.ITnExternalStorageListener;
import com.telenav.persistent.TnFileConnection;
import com.telenav.persistent.TnPersistentManager;
import com.telenav.persistent.TnStore;

public class ReplayLocationProvider extends AbstractTnMockLocationProvider
{
    // maximum number of GPS source files should be loaded into memory
    // used it to control memory usage for a long time testing 
    public static int MAX_FILE_LOAD_COUNT        =   2;
    
    private int curGpsRecordIndex;
    
    private int[] gpsRecordCounts;
    
    private Vector gpsRecords;
    
    private long deltTime;
    
    private int curGpsFileIndex;
    
    private String gpsSourcePath;
    
    private Vector gpsSourceFileNames;
    
    private Object mutex = new Object();
    
    public ReplayLocationProvider(String name)
    {
        super(name);
    }

    public void loadGpsSourceFiles(Vector fileNames)
    {
        if (fileNames == null || fileNames.size() == 0) return;
        
        synchronized (mutex)
        {
            deltTime = 0;
            curGpsRecordIndex = -1;
            gpsRecords = new Vector();
            curGpsFileIndex = 0;
            
            gpsSourceFileNames = fileNames;
            
            try
            {
                gpsRecordCounts = new int[fileNames.size()];
                for (int i = 0; i < MAX_FILE_LOAD_COUNT && i < fileNames.size(); i++)
                {
                    loadGpsSourceFile(i);
                }
                
            }
            catch (Exception ex)
            {
                Logger.log(getClass().getName(), ex);
            }
        }
    }
    
    public void setDeltTime(long deltTime)
    {
        synchronized (mutex)
        {
            this.deltTime = deltTime;
        }    
    }
    
    private void loadGpsSourceFile(int fileIndex) throws IOException
    {
        String fileName = (String)gpsSourceFileNames.elementAt(fileIndex);
        TnFileConnection file = TnPersistentManager.getInstance().openFileConnection(
             fileName, "", TnPersistentManager.FILE_SYSTEM_EXTERNAL);
        
        InputStream is = file.openInputStream();
        byte[] data = new byte[is.available()];
        is.read(data);
        is.close();
        file.close();
        
        String content = new String(data);
        
        int recordCount = gpsRecords.size();
        int startIndex = 0;
        int endIndex = content.indexOf('\r');
        while (endIndex != -1)
        {
            String record = content.substring(startIndex, endIndex);
            TnLocation location = parseGpsRecord(record);
            if (location != null)
            {    
                gpsRecords.addElement(location);
            }
            
            startIndex = endIndex + 2; 
            endIndex = content.indexOf('\r', startIndex);
        }
        
        gpsRecordCounts[fileIndex] = gpsRecords.size() - recordCount;
    }
    
    private TnLocation parseGpsRecord(String record)
    {
        if (record.length() == 0) return null;
        
        TnLocation location = new TnLocation(name);
        
        int count = 1;
        int startIndex = 0;
        int endIndex = record.indexOf(',');
        while (endIndex != -1)
        {
            String value = record.substring(startIndex, endIndex);
            switch (count)
            {
                case 1:
                {
                    location.setLocalTimeStamp(Long.parseLong(value));
                    break;
                }
                case 2:
                {
                    location.setTime(Long.parseLong(value));
                    break;
                }
                case 3:
                {
                    location.setLatitude(Integer.parseInt(value));
                    break;
                }
                case 4:
                {
                    location.setLongitude(Integer.parseInt(value));
                    break;
                }
                case 5:
                {
                    location.setSpeed(Integer.parseInt(value));
                    break;
                }
                case 6:
                {
                    location.setHeading(Integer.parseInt(value));
                    break;
                }
                case 7:
                {
                    location.setAccuracy(Integer.parseInt(value));
                    break;
                }
            }
            
            startIndex = endIndex + 1;
            endIndex = record.indexOf(',', startIndex);
            
            count ++; 
        }
        
        location.setSatellites(Integer.parseInt(record.substring(startIndex)));
        return location;
    }
    
    protected TnLocation getLocationDelegate(int timeout) throws TnLocationException 
    {
        synchronized (mutex)
        {
            if (gpsRecords == null || gpsRecords.size() == 0 || deltTime == 0) 
                return null;
            
            int index = curGpsRecordIndex + 1; 
            if (curGpsRecordIndex == -1) index = 0;
            
            long now = System.currentTimeMillis();
            while (index < gpsRecords.size())
            {
                TnLocation fix = (TnLocation)gpsRecords.elementAt(index);
                long fixTimestamp = fix.getLocalTimeStamp();
                
                if (fixTimestamp + deltTime > now)
                {
                    if (index > 0)
                    {    
                        if (index - 1 == curGpsRecordIndex) 
                        {
                            return null;
                        }
                        
                        curGpsRecordIndex = index - 1;
                        TnLocation lastFix = (TnLocation)gpsRecords.elementAt(curGpsRecordIndex);
                        
                        TnLocation curFix = new TnLocation(lastFix.getProvider());
                        curFix.set(lastFix);
                        
                        curFix.setValid(true);
                        curFix.setTime((lastFix.getTime() * 10 + deltTime)/ 10);
                        curFix.setLocalTimeStamp(System.currentTimeMillis());
                          
                        if (curGpsRecordIndex > gpsRecordCounts[curGpsFileIndex])
                        {
                            if (curGpsFileIndex + MAX_FILE_LOAD_COUNT < gpsRecordCounts.length)
                            {
                                try
                                {
                                    // do not load all GPS source file into memory to save memory
                                    for (int i= 0; i < gpsRecordCounts[curGpsFileIndex] - 1; i ++)
                                    {
                                        gpsRecords.removeElementAt(0);     
                                    }
                                    
                                    curGpsRecordIndex = 0;
                                    loadGpsSourceFile(curGpsFileIndex ++);
                                }
                                catch (Exception ex)
                                {
                                    Logger.log(getClass().getName(), ex);
                                }
                            }//end if
                        }//end if 
                        
                        return curFix;
                    }// end if    
                }// end if
                
                index ++;
            }//end while
            
            return null;
        }    
    }
   

}
