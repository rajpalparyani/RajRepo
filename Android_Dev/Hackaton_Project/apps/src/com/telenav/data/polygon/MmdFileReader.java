/**
 * Copyright(c) 2007 TeleNav, Inc.
 *
 * History:
 *   Mar 1, 2009 1:00:00 PM Created by NieLei
 */
package com.telenav.data.polygon;

import java.io.File;
import java.io.RandomAccessFile;


/**
 *
 * @author NieLei E-mail:lnie@telenav.cn
 * @version 1.0 CreateTime:Mar 1, 2009 1:00:00 PM
 *
 */
public class MmdFileReader
{
    /**
	 * 
	 */
	private static final int FILE_HEADER_LENGTH = 124;
	private Object lock = new Object();
    private RandomAccessFile raf = null;

    private TTree m_tree = null;
    private int m_linkNum = 0;

    /**
     * mmd file
     * @param f
     */
    public MmdFileReader(File f) {
        if (f == null || !f.exists()) {
            return;
        }
        if (f.length() < 124 + 8) { //filehead 124 + contenthead 8
            return;
        }
        try {
            raf = new RandomAccessFile(f, "r");

            raf.seek(FILE_HEADER_LENGTH);

            byte[] buff = new byte[8];
            raf.read(buff);
            int dimension = MmdUtil.readShortInt(buff, 0);
            int treeLevel = MmdUtil.readShortInt(buff, 2);
            int linkNum = MmdUtil.readInt(buff, 4);
            m_linkNum = linkNum;
            buff = new byte[linkNum * 26];
            raf.read(buff);
            int pos = 0;

            m_tree = new TTree(this, dimension, treeLevel, linkNum);
            for (int i = 0; i < linkNum; i++) {
                int linkPos = MmdUtil.readInt(buff, pos); //link pointer
                pos += 4;
                short subNodeCount = MmdUtil.readShortInt(buff, pos); //sub node count
                pos += 2;
                int pointer = MmdUtil.readInt(buff, pos); //if has data,the pointer > 0,else pointer = 0
                pos += 4;
                int minX = MmdUtil.readInt(buff, pos);
                pos += 4;
                int minY = MmdUtil.readInt(buff, pos);
                pos += 4;
                int maxX = MmdUtil.readInt(buff, pos);
                pos += 4;
                int maxY = MmdUtil.readInt(buff, pos);
                pos += 4;
                m_tree.insertNode(i, linkPos, subNodeCount, pointer, minX, minY, maxX, maxY);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getLinkNum() {
        return m_linkNum;
    }

    /**
     *  get index
     * @return index tree
     */
    public TTree getTree() {
        return m_tree;
    }

    /**
     * this pointer is for data pointer,the data size is at the pointer position
     * @param pointer
     * @return
     */
    public byte[] loadData(int pointer) {
        synchronized (lock) {
            try {
                raf.seek(pointer);
                byte[] size = new byte[4];
                int ch = raf.read(size);
                if (ch != -1)
                {
                    int num = MmdUtil.readInt(size, 0);
                    raf.seek(pointer + 4);
                    byte[] buff = new byte[num];
                    int ret = raf.read(buff);
                    if (ret == num)
                    {
                        return buff;
                    }
                }
            }
            catch (Exception e) {
            }
        }
        return null;
    }
    
//    public static void main(String[] args)  {
//        MmdFileReader reader = new MmdFileReader(new File("D2_L11_0.mmd"));
//        TTree tree = reader.getTree();
//
//        long t0 = System.currentTimeMillis();
//        String result = tree.getRegion(36256019,-116719025);
//        System.out.println("result:" + result);
//        System.err.println("use time:" + (System.currentTimeMillis() - t0));
//        
//        result = tree.getRegion(42537419,-96530274);
//        System.out.println("result:" + result);
//        System.err.println("use time:" + (System.currentTimeMillis() - t0));
//        
//        result = tree.getRegion(42448309,-96423157);
//        System.out.println("result:" + result);
//        System.err.println("use time:" + (System.currentTimeMillis() - t0));
//        
//        //Hawaii
//        result = tree.getRegion(19630327,-155601013);
//        System.out.println("result:" + result);
//        System.err.println("use time:" + (System.currentTimeMillis() - t0));
//        
//        //Puerto Rico
//        result = tree.getRegion(18049936,-67259949);
//        System.out.println("result:" + result);
//        System.err.println("use time:" + (System.currentTimeMillis() - t0));
//        
//        //No data
//        result = tree.getRegion(0,0);
//        System.out.println("result:" + result);
//        System.err.println("use time:" + (System.currentTimeMillis() - t0));
//        
//        
//        result = tree.getRegion(37689486,-122491280, 37815982,-122373864);
//        System.out.println("result:" + result);
//        System.err.println("use time:" + (System.currentTimeMillis() - t0));
//    }
}
