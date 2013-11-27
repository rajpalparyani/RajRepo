package com.telenav.navservice;

public class Util
{
    public static String toHex(byte[] bytes)
    {
        if (bytes == null)
            return "null";
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < bytes.length; i++)
        {
            String hex = Integer.toHexString(0xff & bytes[i]);
            if (hex.length() < 2)
                buf.append("0");
            buf.append(hex);
        }
        return buf.toString();
    }

    public static byte[] fromHex(String s)
    {
        byte[] result = new byte[s.length() / 2];
        for (int i = 0; i < result.length; i++)
        {
            result[i] = (byte) Integer.parseInt(s.substring(i * 2, i * 2 + 2),
                    16);
        }
        return result;
    }
}
