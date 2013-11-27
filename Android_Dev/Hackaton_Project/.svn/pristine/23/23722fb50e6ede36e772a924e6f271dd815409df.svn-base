/**
 * 
 */
package com.telenav.sdk.maitai;

import java.util.Vector;


/**
 * @author xinrongl
 *
 */
public interface IMaiTaiParameter
{
    public final static String ACTION_NAVTO         = "navTo";
    public final static String ACTION_DIRECTIONS    = "directions";
    public final static String ACTION_MAP           = "map";
    public final static String ACTION_SEARCH        = "search";
    
    //below is the MaiTai 2.0 actions
    public final static String ACTION_DRIVETO       = "driveTo";
    public static final String ACTION_SET_ADDRESS      = "setAddress";
    public final static String KEY_STATUS           = "status";
    public final static String KEY_STATUS_MSG       = "status_msg";
    
    //below is the AC actions, validate address for MaiTai
    public final static String ACTION_VALIDATE      = "validate_address";
    
    public final static String ACTION_VIEW        = "view";
    
    public String getAction();
        
    public String getAddress();
    public String getOrigAddress();
    public String getDestAddress();
    
    public int getLat();
    public int getLon();
    public String getAddrId();
    
    public int getLocationErrorSize();
    public String getSearchTerm();
    public String getLabel();

    public Vector getMarkersStops();
    
    public boolean needPrompt();
}
