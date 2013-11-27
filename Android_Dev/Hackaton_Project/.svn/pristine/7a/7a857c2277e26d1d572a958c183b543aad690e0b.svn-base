package com.telenav.dsr.pcm.endpointer;

import com.telenav.dsr.DsrManager;

/**
 * Parameters used by the end-pointing algorithm
 * 
 * @author xiaochuann@telenav.com, 2010/09
 *
 */
public class EPParameters 
{
    public static final int DEFAULT_MAX_IW_PAUSE_MS = 1000;
    public static final int DEFAULT_START_OFFSET_MS = 300;
    public static final int DEFAULT_END_OFFSET_MS = 500;
    public static final int DEFAULT_SAMPLING_RATE = DsrManager.PCM_FREQUENCY;
    public static final int DEFAULT_FRAME_LENGTH_IN_MS = 20;
    public static final int DEFAULT_FRAME_SHIFT_IN_MS = 20;
    public static final int DEFAULT_ENG_RANK = 10;
    public static final float DEFAULT_ENG_FACTOR_SOU = (float) 0.025;
    public static final float DEFAULT_ENG_FACTOR_EOU = (float) 0.025;
    
	// sampling rate in Hz
    int samplingRate = DEFAULT_SAMPLING_RATE;
    // frame length in milliseconds or samples
    int frameLengthInMs = DEFAULT_FRAME_LENGTH_IN_MS;
    int frameLengthInSamples;
    // frame shift in milliseconds or samples
    int frameShiftInMs = DEFAULT_FRAME_SHIFT_IN_MS;
//    int frameShiftInSamples;

    // parameter for energy estimation
    int engRank = DEFAULT_ENG_RANK;

    // energy factor for threshold (0.025*32768 in default)
    // SOU: start of utterance
    float engFactorSOU = DEFAULT_ENG_FACTOR_SOU;
    int engThreshSOU;
    // EOU: start of utterance
    float engFactorEOU = DEFAULT_ENG_FACTOR_EOU;
    int engThreshEOU;

    // maximum inter-word pause in milliseconds or frames
    int maxIWPauseMs = DEFAULT_MAX_IW_PAUSE_MS;
    int maxIWPauseFrames;

    // start point offset to allow more signals before utterance begins
    int startOffsetMs = DEFAULT_START_OFFSET_MS;
    int startOffsetFrames;
    //  end point offset to allow more signals after utterance ends
    int endOffsetMs = DEFAULT_END_OFFSET_MS;
    int endOffsetFrames;
	
    // use all the default settings
	public EPParameters() {
        init();
	}
	
    public EPParameters(int samplingRate, int frameLengthInMs,
			int frameShiftInMs, float engFactorSOU, float engFactorEOU,
			int maxPauseMs,	int engRank, int startOffsetMs, int endOffsetMs) {
    	this.samplingRate = samplingRate;
    	this.frameLengthInMs = frameLengthInMs;
    	this.frameShiftInMs = frameShiftInMs;
    	this.engFactorSOU = engFactorSOU;
    	this.engFactorEOU = engFactorEOU;
    	this.maxIWPauseMs = maxPauseMs;
    	this.engRank = engRank;
    	this.startOffsetMs = startOffsetMs;
    	this.endOffsetMs = endOffsetMs;
        init();
	}

	private void init() {
		frameLengthInSamples = samplingRate * frameLengthInMs / 1000;
//		frameShiftInSamples = samplingRate * frameShiftInMs / 1000;
		engThreshSOU = (int) ( engFactorSOU * 32768 );
		engThreshEOU = (int) ( engFactorEOU * 32768 );
		maxIWPauseFrames = maxIWPauseMs / frameShiftInMs;
		startOffsetFrames = startOffsetMs / frameShiftInMs;
		endOffsetFrames	= endOffsetMs / frameShiftInMs;
	}
}
