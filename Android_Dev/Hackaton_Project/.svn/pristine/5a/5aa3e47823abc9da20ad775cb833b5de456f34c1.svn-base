package com.telenav.datatypes;

/**
 * hide
 *
 */
public abstract class GlobalCoordinateUtil
{
    public static final int GLOBAL_LENGTH       = 1 << 25;
    
    // add by zdong to get better performance in RIM
    private static final int GLOBAL_LENGTH_24   = 1 << 24;
    
	static private Object mutex = new Object();

	static protected void clear()
	{
    	synchronized (mutex)
    	{
    	    LatsConstants.lats = null;
    	    GlobalsConstants.globals = null;
    	}
	}
	
	static public int computeLat(int x)
	{
		if (x >= GlobalsConstants.globals[0]) 
		{
			return LatsConstants.lats[0];
		}
		else if (x <= GlobalsConstants.globals[GlobalsConstants.globals.length - 1]) 
		{
			return LatsConstants.lats[LatsConstants.lats.length - 1];
		}
		else
		{
			int index = binaryFindHelper(x, 0, GlobalsConstants.globals.length - 1, GlobalsConstants.globals, false);
			
			// System.out.println("index: "+index+"\t"+globals[index]+"\t"+lats[index]+"\t"+globals[index + 1]+"\t"+lats[index + 1]+"\t"+x);
			
			return (int) interpolate(GlobalsConstants.globals[index], LatsConstants.lats[index], GlobalsConstants.globals[index + 1], LatsConstants.lats[index + 1], x, false);
		}
	}	

	
	static public int computeGlobal(int x)
	{
		if (x <= LatsConstants.lats[0]) 
		{
			return GlobalsConstants.globals[0];
		}
		else if (x >= LatsConstants.lats[LatsConstants.lats.length - 1]) 
		{
			return GlobalsConstants.globals[GlobalsConstants.globals.length - 1];
		}
		else
		{
			int index = binaryFindHelper(x, 0, LatsConstants.lats.length - 1, LatsConstants.lats, true);
			
			// System.out.println("index: "+index+"\t"+(x - lats[index])+"\t"+(lats[index+1]-x));
			
			return (int) interpolate(LatsConstants.lats[index], GlobalsConstants.globals[index], LatsConstants.lats[index + 1], GlobalsConstants.globals[index + 1], x, true);
		}
	}	
	
    public static int earthLonToGlobal(int earthLon)
    {
        long temp = (long)earthLon * GLOBAL_LENGTH;
        long retL = (temp / 36000000) + GLOBAL_LENGTH_24;
        return (int) retL;
    }
    
    public static int globalToEarthLon(int globalX)
    {
        long temp = ((long)globalX - GLOBAL_LENGTH_24) * 36000000;
        long retL = (temp / GLOBAL_LENGTH) - 1;
        return (int) retL;
    }

    /**
     * The formula
     * 
     * double posY = Math.log(Math.tan((earthLat/100000 + 90) * Math.PI / 360));
     * posY = 0.5 - posY / 2 / Math.PI;
     * posY = posY * (1 << 25)
     */
    public static int earthLatToGlobal(int earthLat)
    {
        int global = GlobalCoordinateUtil.computeGlobal(earthLat);
        return global;
    }
    
    /**
     * The formula
     * 
     * globalY = globalY / (1 << 25)
     * double lat = (0.5 - globalY) * 2 * Math.PI;
     * 
     * lat = Math.atan(Math.exp(lat)) / Math.PI * 360 - 90;
     */
    public static int globalToEarthLat(int globalY)
    {   
        int lat = GlobalCoordinateUtil.computeLat(globalY);
        return lat;
    }

	static private int binaryFindHelper(int x, int index0, int index1, int[] arr, boolean isAcending)
	{
		if (index1 - index0 <= 1) return index0;
		
		int middleIndex = (index0 + index1) >> 1;
		if (isAcending)
		{
			if (x < arr[middleIndex])
			{
				return binaryFindHelper(x, index0, middleIndex, arr, isAcending);
			}
			else if (x > arr[middleIndex])
			{
				return binaryFindHelper(x, middleIndex, index1, arr, isAcending);
			}
			else
			{
				return middleIndex;
			}
		}
		else
		{
			if (x > arr[middleIndex])
			{
				return binaryFindHelper(x, index0, middleIndex, arr, isAcending);
			}
			else if (x < arr[middleIndex])
			{
				return binaryFindHelper(x, middleIndex, index1, arr, isAcending);
			}
			else
			{
				return middleIndex;
			}			
		}
	}

	static private long interpolate(int x0, int y0, int x1, int y1, int x, boolean isForward)
	{
		if ((x1 - x0) == 0) return y0;

		int dx1 = x1 - x0;
		int dy1 = y1 - y0;
		int dx  =  x - x0;
		
		int dyDown = (int) ((long) dy1 * dx / dx1);
		
		if (isForward)
		{
			// System.out.println("dx1: "+dx1+", dy1: "+dy1+", dx: "+dx+", dyDown: "+dyDown+", y0: "+y0);
			return dyDown + y0;
		}
		else
		{		
			int dyUp;
			if (dyDown < 0)
			{
				dyUp = dyDown - 1;			
			}
			else
			{
				dyUp = dyDown + 1;
			}
			
			int dxDown = (int) ((long) dx1 * dyDown / dy1);
			int dxUp = (int) ((long) dx1 * dyUp / dy1);
			
			if (Math.abs(dxUp - dx) < Math.abs(dxDown - dx))
			{
				//System.out.println("return up");
				return dyUp + y0;
			}
			else 
			{
				//System.out.println("return down");
				return dyDown + y0;
			}
		}
	}

}
