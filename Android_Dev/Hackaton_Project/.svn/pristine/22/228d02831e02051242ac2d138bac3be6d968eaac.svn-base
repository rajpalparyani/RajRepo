package com.telenav.datatypes.map;

public class Tile
{
    public final static int TILE_SIZE_POWER_64 = 6;
    public final static int TILE_SIZE_POWER_128 = 7;
    public final static int TILE_SIZE_POWER_256 = 8;
    
    public final static int TILE_PIXEL_64 = 64;
    public final static int TILE_PIXEL_128 = 128;
    public final static int TILE_PIXEL_256 = 256;

    public static final byte STATUS_NORMAL_TILE         = 0;
    public static final byte STATUS_EMPTY_TILE          = 1;
    public static final byte STATUS_NO_TILE             = 2;
    public static final byte STATUS_TIMEOUT             = 3;
    public static final byte STATUS_NO_BIN_DATA         = 4;
    public static final byte STATUS_PNG_TILE            = 5;
    public static final byte STATUS_ONLY_TILE_MARKS     = 6;
    public static final byte STATUS_ONLY_TILE_EDGES     = 7;
    
    protected long id;
    protected int zoom;
    protected boolean isPng;
    protected byte[] binary;
    protected byte status;
    protected int pixel;
    protected boolean isEmpty;
    
    protected long expireTime;
    
    /**
     * the data for rendering, different map implementation could
     * have different data structure
     */
    protected Object renderingData;
    
    protected Tile()
    {
        
    }
    
    public long getExpireTime()
    {
        return expireTime;
    }

    public byte getStatus()
    {
        return status;
    }

    public void setExpireTime(long expireTime)
    {
        this.expireTime = expireTime;
    }

    public void setStatus(byte status)
    {
        this.status = status;
    }

    public byte[] getBinary()
    {
        return binary;
    }

    public long getId()
    {
        return id;
    }

    public int getZoom()
    {
        return zoom;
    }

    public void setBinary(byte[] data)
    {
        this.binary = data;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public void setZoom(int zoom)
    {
        this.zoom = zoom;
    }

    public boolean isPng()
    {
        return isPng;
    }
    
    public void setIsPng(boolean b)
    {
        this.isPng = b;
    }

    public int getPixel()
    {
        return this.pixel;
    }
    
    public void setPixel(int pixel)
    {
        this.pixel = pixel;
    }    

    public boolean isEmpty()
    {
        return isEmpty;
    }
    
    public void setIsEmpty(boolean b)
    {
        this.isEmpty = b;
    }

    public Object getRenderingData()
    {
        return renderingData;
    }
    
    public void setRenderingData(Object rd)
    {
        this.renderingData = rd;
    }
}
