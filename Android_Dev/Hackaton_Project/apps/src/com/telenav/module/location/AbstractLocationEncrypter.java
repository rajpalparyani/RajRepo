/**
 * 
 */
package com.telenav.module.location;

import com.telenav.location.TnLocation;

/**
 * @author yxyao
 * 
 */
public abstract class AbstractLocationEncrypter
{

    private static AbstractLocationEncrypter instance;
    
    /**
     * @return the instance
     */
    public static AbstractLocationEncrypter getInstance()
    {
        return instance;
    }
    
    public static void init(AbstractLocationEncrypter encrypterImpl)
    {
        instance = encrypterImpl;
    }
    
    public void encrypt(TnLocation[] locations)
    {
        if (locations != null && locations.length > 0)
        {
            int index = 0;
            TnLocation loc;
            int deltaLat = 0, deltaLon = 0;
            for (; index < locations.length; index++)
            {

                loc = locations[index];
                if (loc != null && !loc.isEncrypt())
                {
                    int oldLat = loc.getLatitude();
                    int oldLon = loc.getLongitude();
                    encryptDelegate(loc);
                    deltaLat = loc.getLatitude() - oldLat;
                    deltaLon = loc.getLongitude() - oldLon;
                    loc.setEncrypt(true);
                    break;
                }
            }

            for (; index < locations.length; index++)
            {
                loc = locations[index];
                if (loc!=null && !loc.isEncrypt())
                {
                    loc.setLatitude(loc.getLatitude() + deltaLat);
                    loc.setLongitude(loc.getLongitude() + deltaLon);
                    loc.setEncrypt(true);
                }
            }
        }
    }

    public void encrypt(TnLocation loc)
    {
        if (loc != null && !loc.isEncrypt())
        {
            encryptDelegate(loc);
            loc.setEncrypt(true);
        }
    }
    
    protected abstract void encryptDelegate(TnLocation loc);
}
