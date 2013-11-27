package com.telenav.dsr.pcm.endpointer;

/**
 * Utilities for digital signal processing
 * 
 * @author xiaochuann@telenav.com, 2010/09
 *
 */
public class DSPUtil {

	/**
	 * Calculate the backward derivative of the input signal:
	 *    diffSig[n] = sig[n] - sig[n-1], 
	 * where "init" is the initial value of the signal.
	 * 
	 * This operation is a equivalent to a high-pass filter
	 * in the frequency domain. It boots the high frequency
	 * components and attenuates the low frequency components
	 * of the input signal.
	 * 
	 */
	public static int[] derivativeFilter(int[] sig, int init) {
		if (sig == null) return null;
		
		int[] diffSig = new int[sig.length];
		diffSig[0] = sig[0] - init;
		for (int i=1; i<sig.length; i++ ) {
			diffSig[i] = sig[i] - sig[i-1];
		}
		return diffSig;
	}

	/**
	 * A fast energy estimator of speech signal in a frame.
	 * 
	 * The ith maximum value of the frame's data is returned
	 * as a fast estimator.
	 * 
	 */
	public static int fastEnergy(int[] sig, int ith) {
		if (sig.length < ith) ith = sig.length;
		quicksortFirstK(sig, 0, sig.length-1, ith);
		return sig[ith-1];
	}
	
	private static void quicksortFirstK(int[] sig, int lo, int hi, int k) {
	    
		 if ( hi > lo ) {
			 int partitionPivotIndex = lo;
			 int newPivotIndex = partition(sig, lo, hi, partitionPivotIndex);
			 if (newPivotIndex != hi)
			 {
			     quicksortFirstK(sig, lo, newPivotIndex-1, k);
			 }
			 if ( newPivotIndex < k ) {
				 quicksortFirstK(sig, newPivotIndex+1, hi, k);
			 }
		 }
	}
	
	private static int partition(int[] sig, int lo, int hi, int pivotIndex) {
		int pivotValue = sig[pivotIndex];
		swap(sig, pivotIndex, hi);	//send pivot item to the back
		int index = lo;				//keep track of the front ends

		//check from the front to the back
		for (int i = lo; i < hi; i++) {
			//swap if the current value is bigger than the pivot
		    if ( sig[i] >= pivotValue ) {
		         swap(sig, i, index);
		         index++;
		    }
		}
		swap(sig, hi, index); //put pivot item in the middle
		return index;
	}

	private static void swap(int[] sig, int i, int j) {
		int temp = sig[i];
		sig[i] = sig[j];
		sig[j] = temp;
	}
	
}
