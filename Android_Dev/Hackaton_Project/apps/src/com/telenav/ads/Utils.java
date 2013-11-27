package com.telenav.ads;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Utils
{
    public static String md5Hash(String s)
    {
        MessageDigest md_md5;
        try
        {
            md_md5 = MessageDigest.getInstance("MD5");
            md_md5.update(s.getBytes());
            byte[] md5hash = md_md5.digest();

            BigInteger b = new BigInteger(1, md5hash);
            return b.toString(16);
        }
        catch (NoSuchAlgorithmException e)
        {
        }
        return "";
    }

    public static String sha1Hash(String s)
    {
        MessageDigest md_sha1;
        try
        {
            md_sha1 = MessageDigest.getInstance("SHA-1");
            md_sha1.update(s.getBytes());
            byte[] sha1hash = md_sha1.digest();

            BigInteger b = new BigInteger(1, sha1hash);
            return String.format("%0" + (sha1hash.length << 1) + "x", b);
        }
        catch (NoSuchAlgorithmException e)
        {
        }
        return "";
    }
}
