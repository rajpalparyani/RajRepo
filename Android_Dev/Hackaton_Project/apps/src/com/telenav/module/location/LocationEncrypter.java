/**
 * 
 */
package com.telenav.module.location;

import com.telenav.localproxy.request.GpsEncryptRequest;
import com.telenav.location.TnLocation;

/**
 * @author yxyao
 * 
 */
public class LocationEncrypter extends AbstractLocationEncrypter
{
    static boolean encryptEnabled;
    
    static{
        try
        {
            System.loadLibrary("OMS_HYBRID_ENGINE_JNIUTILS");
            encryptEnabled = true;
        } catch (Throwable t)
        {
            encryptEnabled = false;
        }
    }
    
    private GpsEncryptRequest encryptJni;
    
    /**
     * 
     */
    public LocationEncrypter()
    {
        encryptJni = new GpsEncryptRequest(null);
    }
    
    protected void encryptDelegate(TnLocation loc)
    {
        if(!encryptEnabled){
            return;
        }
        int[] encFix = new int[3];
        int[] fix = new int[] { loc.getLatitude(), loc.getLongitude(), (int) (loc.getTime() / 100) };
        encryptJni.encrypt(fix, encFix);
        loc.setLatitude(encFix[0]);
        loc.setLongitude(encFix[1]);
    }
}
