/**
 * 
 */
package com.telenav.localproxy.request;

import com.telenav.location.TnLocation;


/**
 * @author yxyao
 *
 */
public class GpsEncryptRequest{

    private final TnLocation loc;
    
    /**
     * 
     */
    public GpsEncryptRequest(TnLocation loc) {
        this.loc = loc;
    }
    
    public void doInNative() {
        int[] encFix =  new int[3];
        int[] fix = new int[]{loc.getLatitude(),loc.getLongitude(),(int)(loc.getTime()/100)};
        encrypt(fix, encFix);
        
        loc.setLatitude(encFix[0]);
        loc.setLongitude(encFix[1]);
        loc.setEncrypt(true);
    }

    public byte[] getResponse() {
        // TODO Auto-generated method stub
        return null;
    }

    public void onCancel() {
        // TODO Auto-generated method stub
        
    }

    public native boolean encrypt(int[] fix,int[] encFix);
}
