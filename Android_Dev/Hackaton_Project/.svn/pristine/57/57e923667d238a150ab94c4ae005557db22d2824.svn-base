/**
 * 
 * NativeSpeexEncoder: a speex encoder based on native speex library,
 * built specifically for Android platform.
 * 
 * Changed from the server-side SpeexEncoder wrapper, it shares the
 * same interface as the pure JAVA-based JSpeexEncoder.
 * 
 *   - Xiaochuan Niu, Jan 2011
 * 
 */
package com.telenav.app.android.jni;

import java.util.Arrays;

import com.telenav.audio.codec.AbstractEncoder;
import com.telenav.logger.Logger;
import com.telenav.module.AppConfigHelper;

/**
 *
 * @author xiaochuann@telenav.com
 * 
 * Add support for WB and UWB enconding, June 3, 2011
 * 
 */
public class NativeSpeexEncoder extends AbstractEncoder {

    /** the fixed frame length chosen by the encoding algorithm */    
    static final int FRAME_LENGTH = 20; // in ms 

    private int SAMPLE_RATE;
    private int PCM_BUFFER_SIZE; // (320 in 8kHz, 640 in 16kHz)

    private long ptr;
    private byte[] buffer;
    private int bufferOffset = 0;

    static {
        Logger.log(Logger.INFO, "NativeSpeexEncoder" ,"speedxEncorderLib:" + AppConfigHelper.speedxEncorderLib );
        System.loadLibrary(AppConfigHelper.speedxEncorderLib);
    }

    public void finishInput() {
        if(bufferOffset > 0){
            Arrays.fill(buffer, bufferOffset, buffer.length-1, (byte)0);
            encodeFrame(ptr, buffer, 0, PCM_BUFFER_SIZE);
            bufferOffset = 0;
        }
    }

    // initialize the encoder with mode
    public void initEncoder(int mode) throws Exception {
    	switch (mode) {
    		case SPEEX_MODEID_NB:  SAMPLE_RATE =   8000; break;
    		case SPEEX_MODEID_WB:  SAMPLE_RATE =  16000; break;
    		case SPEEX_MODEID_UWB: SAMPLE_RATE =  32000; break;    		
    		default: throw new Exception("Unsupported speex encoding mode!!!");
    	}
        ptr = createEncoder(mode);

    	PCM_BUFFER_SIZE = SAMPLE_RATE * 2 * FRAME_LENGTH / 1000; // 16bit input      
        buffer = new byte[PCM_BUFFER_SIZE];
    }
    
    public void destroyEncoder() {
        if(ptr != 0){
            destroyEncoder(ptr);
        }
        ptr = 0;
    }
    
    public void reset()
    {
        ptr = 0;
        bufferOffset = 0;
    }

    public void writePCM(byte[] data, int offset, int size) {
    	if (bufferOffset > 0){
            //Write the previous buffer first
            int cpySize = PCM_BUFFER_SIZE - bufferOffset ;
            if(cpySize > size){
                cpySize = size;
            }
            System.arraycopy(data, offset, buffer, bufferOffset, cpySize);
            bufferOffset += cpySize;
            if(bufferOffset == PCM_BUFFER_SIZE){
                encodeFrame(ptr, buffer, 0, PCM_BUFFER_SIZE);
                bufferOffset = 0;
            }
            size -= cpySize;
            offset += cpySize;
        }
        for(;size >= PCM_BUFFER_SIZE;){
            encodeFrame(ptr, data, offset, PCM_BUFFER_SIZE);
            offset += PCM_BUFFER_SIZE;
            size -= PCM_BUFFER_SIZE;
        }
        if(size > 0){
            //copy rest bytes to buffer
            System.arraycopy(data, offset, buffer, 0, size);
            bufferOffset = size;
        }
    }

    public byte[] getEncodedData() {
        return getEncodedAudio(ptr);
    }

    public static native long createEncoder(int mode);

    public static native void setQuality(long ptr, int quality);

    public static native void setVbr(long ptr, boolean isVbr);

    public static native void setComplexity(long ptr, int complexity);

    public static native void setHighpass(long ptr, int highpass);

    public static native void setSamplingrate(long ptr, int samplingrate);

    public static native long encodeFrame(long ptr, byte[] data, int offset, int length);

    public static native byte[] getEncodedAudio(long ptr);

    public static native void destroyEncoder(long ptr);
}
