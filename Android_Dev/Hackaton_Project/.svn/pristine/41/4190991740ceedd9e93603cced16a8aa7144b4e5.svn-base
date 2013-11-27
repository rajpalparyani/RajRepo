package com.telenav.dsr.pcm.endpointer;


/**
 * A speech end-point detector based on Jordan's first
 * algorithm proposal. 
 * 
 * This is a JAVA implementation of a time-domain energy
 * based end-pointer detector. It applies a fast energy estimator
 * to the derivative of input signal, and makes decisions
 * on the starting and ending points of the speech signal.
 * 
 * @author xiaochuann@telenav.com, 2010/09
 *
 */
public class JEndPointer {
	// The internal states of the end-pointer
	public static class EPSTATE {
	    
	    private final String name;
	    
	    private EPSTATE(String name)
	    {
	        this.name = name;
	    }
	    
	    public String toString()
	    {
	        return this.name;
	    }
	    
	    public static final EPSTATE RESET    = new EPSTATE("RESET");    // the end-pointer is initialized
	    public static final EPSTATE PREUTT   = new EPSTATE("PREUTT");   // the end-pointer is trying to decide the start point
	    public static final EPSTATE INUTT    = new EPSTATE("INUTT");    // the start point has been determined
	    public static final EPSTATE OUTUTT   = new EPSTATE("OUTUTT");   // the end point has been determined
	    public static final EPSTATE END      = new EPSTATE("END");      // reach the end of data
	    public static final EPSTATE ERROR    = new EPSTATE("ERROR");    // something wrong
	};

	private EPSTATE	epstate;

	// parameter settings for the algorithm
	private EPParameters params = new EPParameters();
	
	// frame indices 
	private int currentFrame;
	private int startFrame;
	private int endFrame;
	private int firstStart;
	private int lastEnd;

	// initial sample value
	private int lastSample;

	// energy estimators of 3 consequent frames
	private int ppEng;	// previous-previous
	private int pEng;	// previous
	private int cEng; 	// current
	
	private int counter;
	
	
	/**
	 * Constructor sets up all the basic speech parameters
	 * with default settings.
	 */
	public JEndPointer() {
		init();
	}

	/**
	 * Constructor sets up speech parameters
	 */
	public JEndPointer(int samplingRate, int frameLengthInMs,
			int frameShiftInMs, float engFactorSOU, float engFactorEOU,
			int maxPauseMs,	int engRank, int startOffsetMs, int endOffsetMs) {
		
		setParams(samplingRate, frameLengthInMs, frameShiftInMs,
				engFactorSOU, engFactorEOU, maxPauseMs, engRank, startOffsetMs, endOffsetMs);
		init();
	}
	
	/**
	 * Set speech parameters
	 */
	public void setParams(int samplingRate, int frameLengthInMs,
			int frameShiftInMs, float engFactorSOU, float engFactorEOU, int maxPauseMs,
			int engRank, int startOffsetMs, int endOffsetMs) {
		
		params = new EPParameters(samplingRate, frameLengthInMs,
				frameShiftInMs, engFactorSOU, engFactorEOU, maxPauseMs, engRank,
				startOffsetMs, endOffsetMs);
	
	}
	
	/**
	 * Initialize the end-pointer algorithm
	 */
	private void init() {
		this.currentFrame = -1;			// no data available yet
		this.startFrame = -1;
		this.endFrame = -1;
		this.firstStart = -1;
		this.lastEnd = -1;
		this.epstate = EPSTATE.RESET;
		
		this.ppEng = 0;
		this.pEng = 0;
		this.cEng = 0;
		
		this.counter = 0;
	}

	/**
	 * Reset the end-pointer
	 */
	public void reset() {
		init();
	}

	/**
	 * Get the start point before adjustment (for evaluation)
	 */
	public int getFirstStart() {
		return firstStart;
	}

	/**
	 * Get the end point before adjustment (for evaluation)
	 */
	public int getLastEnd() {
		return lastEnd;
	}

	/**
	 * Get the frame index of the start point (for application)
	 */
	public int getStartFrame() {
		return startFrame;
	}

	/**
	 * Get the frame index of the end point (for application)
	 */
	public int getEndFrame() {
		return endFrame;
	}

	/**
	 * Add a frame of speech data to the end-pointer
	 * to determine the start and end points of the
	 * utterance.
	 * 
	 */
	public EPSTATE addFrame(int[] sig) {
		// verify the frame length
		int sigLength = sig.length;
		if ( sigLength != params.frameLengthInSamples ) {
			epstate = EPSTATE.ERROR;
			return epstate;
		}

		currentFrame ++;
		
		if ( epstate == EPSTATE.OUTUTT ) {
			if ( endFrame <= currentFrame ) epstate = EPSTATE.END;
			return epstate;	
		}
		
		if ( counter>0 ) counter++;
			
		// calculate the derivative of the signal
		// in order to relatively boost the higher
		// frequency components in the signal
		if ( currentFrame == 0 ) lastSample = sig[0];
		int[] dSig = DSPUtil.derivativeFilter(sig, lastSample);
		// update the initial status for the next frame
		lastSample = sig[sig.length-1];
		
		// estimate the short-time energy of 3 consequent frames 
		ppEng = pEng;
		pEng = cEng;
		cEng = DSPUtil.fastEnergy(dSig, params.engRank);
		
		// update the end-points status
		if ( currentFrame<2 ) {
			epstate = EPSTATE.PREUTT;
		} else {
			// pick start-point candidates
			if ( (ppEng<params.engThreshSOU) && (pEng>params.engThreshSOU) && (cEng>params.engThreshSOU) ) {
				// a start point candidate is found
				if ( firstStart == -1 ) {
					firstStart = currentFrame-2;
					epstate = EPSTATE.INUTT;

					// allow to adjust the start point earlier 
					startFrame = firstStart - params.startOffsetFrames;
					if ( startFrame < 0) startFrame = 0;
				}
				// reset the counter
				counter = 0;
			}
			// pick end-point candidates
			if ( (ppEng>params.engThreshEOU) && (pEng>params.engThreshEOU) && (cEng<params.engThreshEOU) ) {
				// an end point candidate is found
				lastEnd = currentFrame;
				// reset the counter
				counter = 1;
			}
			// confirm the end-point after a maximum inter-word pause 
			if ( counter >= params.maxIWPauseFrames ) {
				epstate = EPSTATE.OUTUTT;

				// set the end point, allow to be adjusted 
				endFrame = lastEnd + params.endOffsetFrames;
				if ( endFrame <= currentFrame ) epstate = EPSTATE.END;
			}
		}
		
		// return the end-pointer status information
		return epstate;
	}
	
}
