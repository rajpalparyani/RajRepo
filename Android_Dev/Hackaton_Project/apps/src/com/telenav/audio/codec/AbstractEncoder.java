/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.telenav.audio.codec;

/**
 * 
 * @author yueyulin Oct 2, 2008
 */
public abstract class AbstractEncoder
{
    /* below values are for the speex encoding mode when initialize the encoder */
    /** modeID for the defined narrowband mode */
    public static final int SPEEX_MODEID_NB     = 0;
    /** modeID for the defined wideband mode */
    public static final int SPEEX_MODEID_WB     = 1;
    /** modeID for the defined ultra-wideband mode */
    public static final int SPEEX_MODEID_UWB    = 2;
    

    private static int initCount;
    private static AbstractEncoder encoder;
    
    public static void init(AbstractEncoder xencoder)
    {
        if(initCount >= 1)
            return;
        
        encoder = xencoder;
        initCount++;
       
    }
    
    public static AbstractEncoder getInstance()
    {
        return encoder;
    }
    
    public abstract void reset();
    
    public abstract void finishInput();

    public abstract void initEncoder(int mode) throws Exception;

    public abstract void writePCM(byte[] data, int offset, int size);
    
    public abstract void destroyEncoder();

    public abstract byte[] getEncodedData();
}
