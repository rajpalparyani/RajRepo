package com.telenav.mapcontent;

import com.telenav.datatypes.DataUtil;
import com.telenav.datatypes.Node;
import com.telenav.datatypes.map.Tile;
import com.telenav.logger.Logger;

public abstract class AbstractStreamer
{
    public static final int NEW_NAVMAP_TYPE_ALL_IN_ONE_PNG          = 1260;
    public static final int NEW_NAVMAP_TYPE_ALL_IN_ONE_PNG_TRAFFIC  = 1261;
    public static final int NEW_NAVMAP_TYPE_ALL_IN_ONE_VECTOR_MAP   = 1270;
    public static final int NEW_NAVMAP_TYPE_ALL_IN_ONE_VECTOR_TRAFFIC = 1271;
    
    public static final int ACT_OLD_VECTOR_TILE_REQUEST              = 2;
    public static final int ACT_OLD_VECTOR_TRAFFIC_REQUEST           = 1243;
    
    protected static final byte SIZE_BYTE = 1;

    protected static final byte SIZE_SHORT = 2;

    protected static final byte SIZE_INT = 4;
    
    protected int actionId;

    public AbstractStreamer(int actionId)
    {
        this.actionId = actionId;
    }
    
    public abstract String getActionString();
    
    public int getActionId()
    {
        return actionId;
    }
    
    protected boolean isTrafficStreamer()
    {
        return actionId == NEW_NAVMAP_TYPE_ALL_IN_ONE_PNG_TRAFFIC || actionId == NEW_NAVMAP_TYPE_ALL_IN_ONE_VECTOR_TRAFFIC
                        || actionId == ACT_OLD_VECTOR_TRAFFIC_REQUEST;
    }

    protected boolean isPngTile(int action)
    {
        if (action == NEW_NAVMAP_TYPE_ALL_IN_ONE_PNG || action == NEW_NAVMAP_TYPE_ALL_IN_ONE_PNG_TRAFFIC)
            return true;
        
        return false;
    }

    protected int getSize(byte b)
    {
        if (b < 0)
        {
            return b + 256;
        }
        return b;
    }

    protected int readBySize(byte[] buff, int pos, int size)
    {
        if (size == SIZE_INT)
        {
            return DataUtil.readInt(buff, pos);
        }
        else if (size == SIZE_SHORT)
        {
            return DataUtil.readShort(buff, pos);
        }
        else
        {
            return buff[pos];
        }
    }

    protected static String readString(byte[] buff, int len, int pos)
    {
        byte[] bytes = new byte[len];
        for (int i = 0; i < len; i++)
        {
            bytes[i] = (byte) buff[pos + i];
        }
        try
        {
            return new String(bytes, "UTF-8");

        }
        catch (Throwable e)
        {
            Logger.log(AbstractStreamer.class.getName(), e);

            return "";
        }
    }

    protected void addMandatoryNode(Node requestNode)
    {
        Node[] mandatoryNodes = TileProvider.getMandatoryNodes();
        for (int i = 0; i < mandatoryNodes.length; i++)
        {
            requestNode.addChild(mandatoryNodes[i]);
        }
    }
    
    public abstract byte[] createServerRequest(long[] tileIds);
    
    public abstract Tile[] parseServerResponse(byte[] response);
    
    public abstract Tile unmarshal(Tile tile);
}
