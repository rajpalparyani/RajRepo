/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * DsrServerDrivenParamsDao.java
 *
 */
package com.telenav.data.dao.serverproxy;

import com.telenav.persistent.TnStore;

/**
 * @author xinrongl (xinrongl@telenav.com)
 * @date Feb 25, 2011
 */

public class DsrServerDrivenParamsDao extends ServerDrivenParamsDao
{
    /**
     * The speech end threshold for DSR, for example "0$30::50$30::110$0"
     */
    public final static String DSR_SPEECH_END_THRESHOLD = "DSR_SPEECH_END_THRESHOLD";
    
    /**
     * DSR record format, it could be "AMR" or "PCM". By default it's "PCM".
     */
    public final static String DSR_RECORD_FORMAT = "DSR_RECORD_FORMAT";
    
    /////////////////////////////////////////////////////////////////////////////////////////////////
    
    //pcm ending point parameters
    public final static String maxIWPauseMs = "maxIWPauseMs";
    
    public final static String startOffsetMs = "startOffsetMs";
    
    public final static String endOffsetMs = "endOffsetMs";
    
    public final static String samplingRate = "samplingRate";
    
    public final static String frameLengthInMs = "frameLengthInMs";
    
    public final static String frameShiftInMs = "frameShiftInMs";
    
    public final static String engRank = "engRank";
    
    public final static String engFactorSOU = "engFactorSOU";
    
    public final static String engFactorEOU = "engFactorEOU";
    
    /////////////////////////////////////////////////////////////////////////////////////////////////
    


    public DsrServerDrivenParamsDao(TnStore serverDrivenStore)
    {
        super(serverDrivenStore);
    }

}
