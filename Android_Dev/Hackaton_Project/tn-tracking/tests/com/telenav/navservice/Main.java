/*package com.telenav.navservice;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.StringTokenizer;

import com.telenav.io.TnProperties;
import com.telenav.location.ITnLocationListener;
import com.telenav.location.TnCriteria;
import com.telenav.location.TnLocation;
import com.telenav.location.TnLocationException;
import com.telenav.location.TnLocationManager;
import com.telenav.location.TnLocationProvider;
import com.telenav.logger.DefaultLoggerFilter;
import com.telenav.logger.ILoggerListener;
import com.telenav.logger.Logger;
import com.telenav.navservice.Controller.IControllerListener;
import com.telenav.navservice.model.App;
import com.telenav.navservice.util.Constants;
import com.telenav.network.TnNetworkManager;
import com.telenav.network.j2se.J2seNetworkManager;
import com.telenav.radio.TnRadioManager;
import com.telenav.radio.j2se.J2seRadioManager;
import com.telenav.telephony.TnTelephonyManager;
import com.telenav.telephony.j2se.J2seTelephonyManager;


class ReplayLocationProvider extends TnLocationProvider implements Runnable
{
    private FileReader fr;
    private BufferedReader br;
    
    private long timeDiff;
    
    private ITnLocationListener listener;
    private long minTime;
    private long lastUpdateTime;
    
    public ReplayLocationProvider(String name, String fileName)
    {
        super(name);
        try{
            fr = new FileReader(fileName);
            br = new BufferedReader(fr);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        new Thread(this).start();
    }

    @Override
    protected TnLocation getLastKnownLocation() throws TnLocationException
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected TnLocation getLocation(int timeout) throws TnLocationException
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected void requestLocationUpdates(long minTime, float minDistance,
            int timeout, int maxAge, ITnLocationListener listener)
    {
        this.lastUpdateTime = System.currentTimeMillis();
        this.minTime = minTime;
        this.listener = listener;
    }

    @Override
    protected void reset()
    {
        this.listener = null;
    }
    
    private TnLocation convert(String s)
    {
        TnLocation location = new TnLocation(TnLocationManager.GPS_179_PROVIDER);
        
        StringTokenizer st = new StringTokenizer(s, ",");
        long gpsTime = Long.parseLong(st.nextToken());
        long systemTime = Long.parseLong(st.nextToken());
        if (timeDiff == 0)
        {
            timeDiff = System.currentTimeMillis() - systemTime;
        }
        gpsTime += timeDiff;
        systemTime += timeDiff;
        location.setTime(gpsTime / 10);
        location.setLocalTimeStamp(systemTime);
        
        double lat = Double.parseDouble(st.nextToken());
        location.setLatitude((int)(lat * 100000));
        
        double lon = Double.parseDouble(st.nextToken());
        location.setLongitude((int)(lon * 100000));
        
        double speed = Double.parseDouble(st.nextToken());
        location.setSpeed((int)(speed * 9));
        
        double heading = Double.parseDouble(st.nextToken());
        location.setHeading((int)heading);
        
        double altitude = Double.parseDouble(st.nextToken());
        location.setAltitude((float)altitude);
        
        double accuracy = Double.parseDouble(st.nextToken());
        location.setAccuracy((int)(accuracy/1.1132));
        
        location.setValid(location.getLatitude() != 0 || location.getLongitude() != 0);
        
        return location;
    }

    @Override
    public void run()
    {
        if (br == null)
            return;
        
        while(true)
        {
            try{
                String s = br.readLine();
                if (s == null || s.length() == 0)
                    break;
                
                TnLocation loc = convert(s);
                long time = loc.getLocalTimeStamp() - System.currentTimeMillis();
                if (time > 0)
                    Thread.sleep(time);
                
                if (listener != null && System.currentTimeMillis() - lastUpdateTime >= minTime)
                {
                    lastUpdateTime += minTime;
                    listener.onLocationChanged(this, loc);
                }
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }
    
}

class ReplayLocationManager extends TnLocationManager
{
    private ReplayLocationProvider p;
    
    public ReplayLocationManager(String fileName)
    {
        this.p = new ReplayLocationProvider(TnLocationManager.GPS_179_PROVIDER, fileName);
    }
    
    @Override
    protected TnLocationProvider getProvider(TnCriteria criteria)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected TnLocationProvider getProviderDelegate(String provider)
    {
        if (TnLocationManager.GPS_179_PROVIDER.equals(provider))
            return p;
        return null;
    }

    @Override
    public boolean isGpsProviderAvailable(String provider)
    {
        if (TnLocationManager.GPS_179_PROVIDER.equals(provider))
            return true;
        
        return false;
    }
    
}

public class Main implements IControllerListener, IClientStateMonitor, ILoggerListener
{
    private static String gpsFileName = "c:/20110431-145156.log";
    
    private Controller controller;
    
    public static void main(String[] args)
    {
        Main main = new Main();
        main.handle();
    }
    
    private void handle()
    {
        try{
            Logger.start();
            Logger.add(this, new DefaultLoggerFilter(Logger.INFO, "*"));//FYI:don't print log, just use null instead of "*"
            
            TnNetworkManager.init(new J2seNetworkManager());
            TnTelephonyManager.init(new J2seTelephonyManager(new TnProperties()
            {
                
                @Override
                public int size()
                {
                    // TODO Auto-generated method stub
                    return 0;
                }
                
                @Override
                public Enumeration propertyNames()
                {
                    // TODO Auto-generated method stub
                    return null;
                }
                
                @Override
                public void load(InputStream is) throws IOException
                {
                    // TODO Auto-generated method stub
                    
                }
                
                @Override
                public String getProperty(String name)
                {
                    // TODO Auto-generated method stub
                    return null;
                }
            }));
            TnRadioManager.init(new J2seRadioManager());
            TnLocationManager.init(new ReplayLocationManager(gpsFileName));
            
            App app = new App();
            app.setPlatform(Constants.OS_ANDROID);
            controller = new Controller(app, this, this);
            controller.start();
            
            NavServiceParameter param = new NavServiceParameter();
            param.setAppName("ATT_Nav");
            param.setAppVersion("7.0.1000");
            param.setCarrierName("ATT");
            param.setLogEnabled(true);
            param.setRouteId("1");
            param.setForeground(true);
            param.setServerUrl("http://qa-unit03.telenav.com:8080/resource-cserver/telenav-server-pb");
            param.setUserId("tel:thisisatestid");
            controller.setParameters(param);
            
            
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public void stopService()
    {
        
    }

    @Override
    public boolean isClientRunning()
    {
        return true;
    }

    @Override
    public void log(int level, String clazz, String message, Throwable t,
            Object[] params)
    {
        System.out.print("["+clazz+"] ");
        if (message != null)
            System.out.println(message);
        if (t != null)
            t.printStackTrace();
    }
}
*/