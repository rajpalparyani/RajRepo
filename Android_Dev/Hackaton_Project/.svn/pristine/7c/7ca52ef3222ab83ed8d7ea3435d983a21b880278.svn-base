/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * MD5Helper.java
 *
 */
package com.telenav.module.sync.apache;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Hashtable;

import com.telenav.datatypes.DataUtil;
import com.telenav.io.TnGZIPInputStream;
import com.telenav.io.TnIoManager;

/**
 * @author wzhu (wzhu@telenav.cn)
 * @date 2011-1-10
 */
class I18nHelper
{
    public static String getFileName(String name, I18nFile parentFile)
    {
        String path = name;

        if (parentFile != null)
        {
            String parentName = parentFile.getName();
            if(parentName.indexOf('$') != -1)
            {
                int tagIndex = parentName.indexOf(I18nRequestManager.INDEX_TAG);
                if (tagIndex != -1)
                {
                    parentName = parentName.substring(0, tagIndex);
                }
                parentName = parentName.replace('$', '/');
                path = parentName + "/" + name;
            }
            else
            {
                path = I18nRequestManager.ROOT_PATH + "/" + name;
            }
        }

        if (path.endsWith(I18nRequestManager.INDEX_TAG))
        {
            path = path + I18nRequestManager.INDEX_SUFFIX;
        }

        return path;
    }

    public static final int BUFFER = 1024;

    public static byte[] decompress(byte[] data) throws IOException
    {
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        decompress(bais, baos);
        byte[] decompressedData = baos.toByteArray();
        baos.close();
        bais.close();
        return decompressedData;
    }

    private static void decompress(InputStream is, OutputStream os) throws IOException
    {
        TnGZIPInputStream gis = TnIoManager.getInstance().createGZIPInputStream(is);
        int count;
        byte data[] = new byte[BUFFER];
        while ((count = gis.read(data, 0, BUFFER)) != -1)
        {
            os.write(data, 0, count);
        }
        gis.close();
    }

    public static Hashtable getIndexes(byte[] data)
    {
        Hashtable table = new Hashtable();
        if (data == null || data.length == 0)
            return table;
        else
        {
            int index = 0;
            while (index < data.length && data[index] != (byte) 0xff)
            {
                index += 4;
                long offset = DataUtil.readLong(data, index);
                index += 8;
                long length = DataUtil.readLong(data, index);
                index += 8;
                byte nameSize = data[index++];

                byte[] nameData = new byte[(int) nameSize];
                byte[] md5Data = new byte[(int) (length - nameSize)];
                System.arraycopy(data, (int) offset, nameData, 0, (int) nameSize);
                System.arraycopy(data, (int) (offset + nameSize), md5Data, 0, (int) (length - nameSize));
                String name = new String(nameData);
                table.put(name, md5Data);
            }
            return table;
        }
    }

    public static byte[] getIndexesFromLocaleFile(String name) throws IOException
    {
        InputStream in = TnIoManager.getInstance().openFileFromAppBundle(name);
        if (in == null)
            return null;

        byte[] data = TnIoManager.readBytes(in);
        in.close();

        return data;
    }
}
