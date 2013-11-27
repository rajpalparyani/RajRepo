package com.telenav.dsr.util;

import com.telenav.dsr.DsrManager;
import com.telenav.dsr.IMetaInfoProvider;
import com.telenav.module.media.IAudioConstants;


/**
 * It's used for generate MetaData for DSR protocol.
 * 
 * @author zhdong.
 *
 */
public class MetaUtil
{
    public static final byte ENCODING_FORMAT_AMR         = 0;
    public static final byte ENCODING_FORMAT_SPEEX_8K    = 3;
    public static final byte ENCODING_FORMAT_SPEEX_16K   = 5;
    
    /**
     * Generate Meta data.
     * 
     * @param passcode
     * @param type
     * @param fmt
     * @param stop
     * @param mandatoryNode
     * @param logNode
     * @return generated bytes
     */
    public static byte[] generateMeta(byte[] passcode, byte type, IMetaInfoProvider infoProvider)
    {
        byte[] stopBs = infoProvider.getAnchorStopData();
        byte[] mandatoryBs = infoProvider.getMadantoryData();
        byte[] logBs = infoProvider.getLogData();
        
        if(stopBs == null || mandatoryBs == null || logBs == null)
            return null;

        byte[] meta = new byte[32 + 1 + 1 + 4 + stopBs.length + 4 + mandatoryBs.length + 4 + logBs.length];
        if (passcode == null || passcode.length < 32)
        {
            byte[] tmp = new byte[32];
            if (passcode != null)
            {
                System.arraycopy(passcode, 0, tmp, 0, passcode.length);
            }
            passcode = tmp;
        }
        System.arraycopy(passcode, 0, meta, 0, 32);
        meta[32] = type;
        meta[33] = (byte)getEncodingFormat(infoProvider.getFormat());

        getIntBytes(stopBs.length, meta, 34);
        System.arraycopy(stopBs, 0, meta, 38, stopBs.length);

        getIntBytes(mandatoryBs.length, meta, 38 + stopBs.length);
        System.arraycopy(mandatoryBs, 0, meta, 42 + stopBs.length, mandatoryBs.length);

        getIntBytes(logBs.length, meta, 42 + stopBs.length + mandatoryBs.length);
        System.arraycopy(mandatoryBs, 0, meta, 46 + stopBs.length + mandatoryBs.length, logBs.length);

        return meta;
    }
    
    private static byte getEncodingFormat(int audioFormat)
    {
        if (audioFormat == IAudioConstants.RECORD_FORMAT_PCM)
        {
            switch (DsrManager.PCM_FREQUENCY)
            {
            case 16000:
                return ENCODING_FORMAT_SPEEX_16K;
            default:
                return ENCODING_FORMAT_SPEEX_8K;
            }
        }
        
        return ENCODING_FORMAT_AMR;
    }

    /**
     * Set the int bytes to the buf byte array from offset
     * 
     * @param value
     * @param buf
     * @param offset
     */
    public static void getIntBytes(int value, byte[] buf, int offset)
    {
        if (buf == null || offset + 4 > buf.length)
        {
            return;
        }
        for (int i = 0; i < 4; i++)
        {
            buf[i + offset] = (byte) (value >> (32 - (i + 1) * 8));
        }
    }

}