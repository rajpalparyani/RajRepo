package com.telenav.datatypes.map;


public class TileArray
{
    //tile ids
    protected long[] ids = new long[20];
    
    //to support continuous map when crossing the border in x direction
    protected int[] lonShiftCount = new int[20];
    
    //to support map borders in y direction
    protected int[] latShiftCount = new int[20];
    
    protected int size = 0;
    protected int zoom;
    
    protected int[][] columns;

    protected TileArray(int[][] columns,
            int zoom)
    {
        this.zoom = zoom;
        this.columns = columns;
        
        createTileIDs(zoom);
    }

    public int getZoom()
    {
        return zoom;
    }
    
    public int getLonShift(int index)
    {
        return lonShiftCount[index];
    }
    
    public int getLatShift(int index)
    {
        return latShiftCount[index];
    }

    public long getTileIDAt(int index)
    {
        return ids[index];
    }

    public int getTileIDSize()
    {
        return this.size;
    }

    public boolean contains(long id)
    {
        for (int i = 0; i < this.size; i++)
        {
            if (ids[i] == id) 
                return true;
        }
        return false;
    }

    private void createTileIDs(int zoom)
    {
        this.size = 0;

        for (int i = 0; i < columns.length; i++)
        {
            int[] col = columns[i];

            size += (col[2] - col[1] + 1);
        }

        if (ids.length < this.size)
        {
            ids = new long[size];
            lonShiftCount = new int[size];
            latShiftCount = new int[size];
        }

        int counter = 0;

        for (int i = 0; i < columns.length; i++)
        {
            //System.out.println("getTileIDs: "+tilesID[i][0]+", "+tilesID[i][1]+", "+i);
            int[] col = columns[i];

            int lonIndex = col[0];
            int lonShift = 0;
            int tileCount = MapUtil.getTileCount(zoom);
            
            while (lonIndex < 0)
            {
                lonIndex += tileCount;
                lonShift--;
            }
            while(lonIndex >= tileCount)
            {
                lonIndex -= tileCount;
                lonShift++;
            }
            
            for (int j = col[1]; j <= col[2]; j++)
            {
                int latIndex = j;
                int latShift = 0;
                while (latIndex < 0)
                {
                    latIndex += tileCount;
                    latShift--;
                }
                while(latIndex >= tileCount)
                {
                    latIndex -= tileCount;
                    latShift++;
                }
                
                long tileID = MapUtil.composeID(latIndex, lonIndex, zoom);
                ids[counter] = tileID;
                lonShiftCount[counter] = lonShift;
                latShiftCount[counter] = latShift;
                counter++;
            }
        }
        
    }

    
    public long[] getBottomIds()
    {
        if (columns != null && columns.length > 0)
        {
            long[] ids = new long[columns.length];

            for (int i = 0; i < columns.length; i++)
            {
                int[] col = columns[i];

                int lonIndex = col[0];
                int latIndex = col[2];

                long tileID = MapUtil.composeID(latIndex, lonIndex, zoom);
                ids[i] = tileID;
            }

            return ids;
        }
        else
        {
            return null;
        }
    }
    
    public long[] getTopIds()
    {
        if (columns != null && columns.length > 0)
        {
            long[] ids = new long[columns.length];

            for (int i = 0; i < columns.length; i++)
            {
                int[] col = columns[i];

                int lonIndex = col[0];
                int latIndex = col[1];

                long tileID = MapUtil.composeID(latIndex, lonIndex, zoom);
                ids[i] = tileID;
            }

            return ids;
        }
        else
        {
            return null;
        }
    }
    public long[] getRightIds()
    {
        if (columns != null && columns.length > 0)
        {
            int[] col = columns[columns.length - 1];
            long[] ids = new long[col[2] - col[1] + 1];
            int counter = 0;
            for (int j = col[1]; j <= col[2]; j++, counter++)
            {
                int lonIndex = col[0];
                int latIndex = j;

                long tileID = MapUtil.composeID(latIndex, lonIndex, zoom);
                ids[counter] = tileID;
            }

            return ids;
        }
        else
        {
            return null;
        }
    }

    public long[] getLeftIds()
    {
        if (columns != null && columns.length > 0)
        {
            int[] col = columns[0];
            long[] ids = new long[col[2] - col[1] + 1];
            int counter = 0;
            for (int j = col[1]; j <= col[2]; j++, counter++)
            {
                int lonIndex = col[0];
                int latIndex = j;

                long tileID = MapUtil.composeID(latIndex, lonIndex, zoom);
                ids[counter] = tileID;
            }

            return ids;
        }
        else
        {
            return null;
        }
    }
    
    public long getCenterId()
    {
        if (columns != null && columns.length > 0)
        {
            int center = (columns.length + 1) / 2;
            
            if (center < 0 || center >= columns.length) 
                return 0;
            int[] col = columns[center];

            int lonIndex = col[0];
                
            int centerJ = (col[1] + col[2] + 2) / 2;
                
            int latIndex = centerJ;
            
            return MapUtil.composeID(latIndex, lonIndex, zoom);
        }
        return 0;
    }
    
    public long[] getCenterIds(int radius)
    {
        if (columns != null && columns.length > 0)
        {
            long[] ids = new long[4 * radius * radius];

            //int counter = 0;
            int center = (columns.length + 1) / 2;
            
            int index = 0;
            for (int i = center - radius; i < center + radius; i++)
            {
                if (i < 0 || i >= columns.length) continue;
                
                int[] col = columns[i];

                int lonIndex = col[0];
                
                int centerJ = (col[1] + col[2] + 2) / 2;
                
                for (int j = centerJ - radius; j < centerJ + radius; j++)
                {
                    if (j < col[1] || j > col[2]) continue;
                    
                    int latIndex = j;
                    long tileID = MapUtil.composeID(latIndex, lonIndex, zoom);                    
                    ids[index] = tileID;
                    index++;
                }
            }

            return ids;
        }
        else
        {
            return null;
        }
    }
}
