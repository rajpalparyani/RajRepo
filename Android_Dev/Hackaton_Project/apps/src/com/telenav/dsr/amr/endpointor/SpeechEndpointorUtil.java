/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.telenav.dsr.amr.endpointor;

/**
 *
 * @author yueyulin
 */
public class SpeechEndpointorUtil {
    public static final float[] LOGS = {
        0.17609125905568124f,
        0.09691001300805642f,
        0.05115252244738129f,
        0.02632893872234915f,
        0.013363961557981502f,
        0.006733382658968403f,
        0.003379740651380597f,
        0.0016931580194449755f,
        0.000847404f,
        0.000423909f,
        0.000212006f,
        0.000106016f,
        0.000053011f};
    public static final float[] LOGS2N = {
        0.3010299956639812f,//2^1
        0.6020599913279624f,//2^2
        0.9030899869919435f,//2^3
        1.2041199826559248f,//16 2^4
        1.505149978319906f,
        1.806179973983887f,
        2.1072099696478683f,
        2.4082399653118496f,
        2.709269960975831f,
        3.010299956639812f,
        3.3113299523037933f,
        3.612359947967774f,
        3.9133899436317554f,
        4.214419939295737f,
        4.515449934959718f,
        4.816479930623699f,
        5.11750992628768f,
        5.418539921951662f,
        5.719569917615643f,
        6.020599913279624f,
        6.321629908943605f,
        6.622659904607587f,
        6.923689900271567f,
        7.224719895935548f,
        7.52574989159953f,
        7.826779887263511f,
        8.127809882927492f,
        8.428839878591473f,
        8.729869874255455f,
        9.03089987f};//2^30 1<<31

    public static float log10x(int intValue) {
    	//System.out.println("intValue:"+intValue);
    	if(intValue <= 1){
    		return 0;
    	}
        long x = intValue;
        x <<= 30;
        //System.out.printf("%X\n", x);

        //Find the first set bit
        int i ;
        for (i = 30; i > 0; ) {
            final long X = 1l << (30 + i);
            //System.out.printf("%X\n", X);
            if((x & X) != 0){
                break;
            }
            i --;
        }
        x >>= (i+1);
        //System.out.printf("%X\n", x);
        float y = LOGS2N[i];
        //System.out.println(y);
        boolean isNotExact = true;
        long l = 0;
        /*int iterationOut = 0;
        int iterationIn = 0;*/
        do {
            //iterationOut ++;
            //System.out.printf("%X\n", x);
            for(i = 0;i < LOGS.length;i ++){
                //iterationIn ++;
                l = x + (x >> (i + 1));
                //System.out.printf("  %X\n",l);
                if(l <= 0x3fffffff){
                    break ;
                }
            }
            if(l >= 0x3fffffff || i == LOGS.length){
                break;
            }
            x = l;
            y -= LOGS[i];

        }while(isNotExact);
        /*System.out.printf("  %X\n",l);
        System.out.println("in/Out="+iterationIn+"/"+iterationOut);*/
        return y ;
    }

    public static float[] LOGTABLE ;
    public static long INIT_TABLE_TIME = 0;
    public static void init(){
    	/*
        long start = System.currentTimeMillis();
        LOGTABLE = new float[0x7fff-1];
        for(int i = 0;i < LOGTABLE.length;i ++){
            LOGTABLE[i] = log10x((i+1)*(i+1));
        }
        long end = System.currentTimeMillis();
        INIT_TABLE_TIME = (end-start);
        System.out.print("finish init");*/
    }


    public static double logRootMeanSquare(short[] samples) {
        double sumOfSquares = 0.0f;
        for (int i = 0; i < samples.length; i++) {
            double sample = samples[i];
            sumOfSquares += sample * sample;
        }
        double rootMeanSquare = Math.sqrt
                (sumOfSquares / samples.length);
        rootMeanSquare = Math.max(rootMeanSquare, 1);
        return (LOGTABLE[(int)rootMeanSquare] * 20);
    }
}
