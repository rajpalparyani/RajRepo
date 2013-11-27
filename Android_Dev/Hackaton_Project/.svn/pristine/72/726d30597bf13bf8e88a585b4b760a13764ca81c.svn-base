/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.telenav.dsr.amr.endpointor;

/**
 * 
 * @author yueyulin
 */
public class SpeechClassifierEndpointer
{
    // protected double averageNumber = 1;
    protected double adjustment = 0.003;

    public double level = 0; // average signal level

    public double backgroundSingalLevel = 100; // background signal level

    // protected double background = 1E5;
    protected double minSignal = 1E-3; // minimum valid signal level

    public double threshold = 30;

    // public double threshold = 1E5;

    public SpeechClassifierEndpointer(int threshold)
    {
        this.threshold = threshold;
    }

    public boolean isSpeech(double current, double threshold)
    {
        // System.out.println("current: " + current);
        boolean isSpeech = false;
        if (current >= minSignal)
        {
            // level = ((level * averageNumber) + current) / (averageNumber + 1);
            level = (level + current) / 2;
            if (current < backgroundSingalLevel)
            {
                backgroundSingalLevel = current;
            }
            else
            {
                backgroundSingalLevel += (current - backgroundSingalLevel) * adjustment;
            }
            if (level < backgroundSingalLevel)
            {
                level = backgroundSingalLevel;
            }
            // System.out.println(level+"-"+background+"---"+threshold);
            isSpeech = (level - backgroundSingalLevel > threshold);
        }
        return isSpeech;
    }

    public boolean isSpeech(double current)
    {
        return isSpeech(current, threshold);
    }

}
