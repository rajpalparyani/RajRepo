package com.telenav.dsr.util;

import java.io.IOException;
import java.io.OutputStream;


/**
 * The stream encoder for DSR request
 * 
 * @author zhdong
 * 
 */
public class StreamEncoder
{
    protected int UNIT_SIZE = 32;

    protected int FULL_UNIT_FLAG = 31;

    // private int FULL_UNIT_END_PACKET_FLAG = 0x3f;

    protected OutputStream packet;

    protected byte bufferOffset = 0;
    
    /**
     * constructor of streamEncoder.
     * @param dataPacket
     */
    public StreamEncoder(OutputStream dataPacket)
    {
        this.packet = dataPacket;
    }

    /**
     * get integer value from buffer by specific offset.
     * @param buf
     * @param offset
     * @return int value
     */
    public static int getInt(byte[] buf, int offset)
    {
        if (buf == null || offset + 4 > buf.length)
        {
            return -1;
        }

        // int value
        int ret = 0;
        int tmp = 0;
        for (int i = 3; i >= 0; i--)
        {
            tmp = buf[i + offset];
            if (tmp < 0)
            {
                tmp += 128;
                tmp |= 0x80;
            }

            ret |= (tmp << (3 - i) * 8);
        }
        return ret;
    }

    /**
     * Encode meta data to specific standard before write to streamer. See {@link MetaUtil} for how to generate byte[]
     * 
     * @param meta
     * @param offset
     * @param len
     * @throws IOException
     */
    public void encodeMeta(byte[] meta, int offset, int len) throws IOException
    {
        byte[] buf = new byte[33];
        if (len + offset > meta.length)
        {
            len = meta.length - offset;
        }
        System.arraycopy(meta, offset, buf, 0, len);
        buf[32] = (byte) (31);
        buf[32] = (byte) (buf[32] | 0x40);
        packet.write(buf);
    }

    /**
     * Encode the byte as the DSR protocol.
     * 
     * @param bs
     * @param offset
     * @param length
     * @throws IOException
     */
    public void encode(byte[] bs, int offset, int length) throws IOException
    {
        if (bs == null)
        {
            return;
        }
        if (offset + length > bs.length)
        {
            length = bs.length - offset;
        }
        while (length > 0)
        {
            if (this.bufferOffset == UNIT_SIZE)
            {
                // Add a unit end flag byte
                packet.write(FULL_UNIT_FLAG);
                bufferOffset = 0;
            }
            int copyLen = length;
            
            if (this.bufferOffset + copyLen > UNIT_SIZE)
            {
                copyLen = UNIT_SIZE - this.bufferOffset;
            }
            packet.write(bs, offset, copyLen);
            this.bufferOffset += copyLen;
            length -= copyLen;
            offset += copyLen;
        }
    }

    public void endMetaEncoding() throws IOException
    {
        byte[] buf = new byte[33];
        buf[32] = (byte) (96);
        packet.write(buf);
    }

    /**
     * Finish encode for last several audio byte[].
     * 
     * @throws IOException
     */
    public void endIncompleteAudio() throws IOException
    {
        try
        {
            if (bufferOffset != 0)
            {
                int paddingSize = UNIT_SIZE - bufferOffset;
                while (paddingSize-- > 0)
                {
                    packet.write(0);
                }
                int flag = bufferOffset - 1;
                packet.write(flag);
            }
        }
        finally
        {
            bufferOffset = 0;
        }
        packet.flush();
    }

    /**
     * Write meta data to streamer. See {@link MetaUtil} for how to generate byte[]
     * 
     * @param metaData
     * @param offset
     * @param len
     * @throws IOException
     */
    public void writeMeta(byte[] metaData, int offset, int len) throws IOException
    {
        if (metaData == null || offset < 0 || len < 0)
        {
            // System.err.println("illegal IO info");
            return;
        }

        // System.out.println("Send meta:len is " + metaData.length);
        if (offset + len > metaData.length)
        {
            len = metaData.length - offset;
        }

        int metaUnit = len / 32 + 1;
        for (int i = 0; i < metaUnit; i++)
        {
            // System.out.println("Send meta[" + i + "]");
            if (i != metaUnit - 1)
            {
                encodeMeta(metaData, offset, 32);
                offset += 32;
                len -= 32;
            }
            else
            {
                encodeMeta(metaData, offset, len);
            }
        }
    }
}
