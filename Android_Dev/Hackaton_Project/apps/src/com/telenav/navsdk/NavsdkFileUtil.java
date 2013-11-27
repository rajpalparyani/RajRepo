/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * NavsdkFileUtil.java
 *
 */
package com.telenav.navsdk;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

/**
 * @author hchai
 * @date 2012-3-29
 */
public class NavsdkFileUtil
{
    private static Context context;

    private final static String COPY_FROM_SDCARD_DIR = "_navsdkdemo_in";

    private final static String COPY_TO_SDCARD_DIR = "_navsdkdemo_out";

    private final static String NAVSDK_AUDIO_DIRECTORY = "NavAudio";

    public static void copyDefaultUrl()
    {
        copyAssetFile2Cache("defaultUrl.bin");
    }

    public static void init(Context ctx)
    {
        context = ctx;
    }

    public static void copyAssetFile2Cache(String fileName)
    {
        InputStream input = null;
        FileOutputStream stream = null;
        try
        {
            input = context.getResources().getAssets().open(fileName);
            File file = new File(context.getCacheDir() + "/" + fileName);
            if (!file.exists())
            {
                if(!file.createNewFile())
                {
                    Log.e("NavsdkFileUtil", "errorMsg = Cannot create file " + file.getPath());
                    return;
                }
            }
            stream = new FileOutputStream(file);
            if (input != null)
            {
                byte[] datas = new byte[input.available()];
                if (input.read(datas) > 0)
                {
                    stream.write(datas);
                }
                stream.flush();
                stream.close();
                input.close();
            }
        }
        catch (IOException e)
        {
            Log.e(context.getClass().getName(), "Can't copy " + fileName + "to the devices!");
        }
        finally
        {
            try
            {
                if (input != null)
                {
                    input.close();
                }
            }
            catch (IOException ex)
            {
                Log.e("NavsdkFileUtil", "errorMsg = " + ex.toString());
            }
            try
            {
                if(stream != null)
                {
                    stream.close();
                }
            }
            catch (IOException ex)
            {
                Log.e("NavsdkFileUtil", "errorMsg = " + ex.toString());
            }
        }
    }

    public static void copyResourceFile2CacheSubDir(String fileName, String subDir)
    {
        InputStream input = null;
        FileOutputStream stream = null;
        try
        {
            File inputFile = new File(context.getFilesDir(), fileName);
            input = new FileInputStream(inputFile);
            File file = new File(context.getCacheDir() + "/" + subDir, fileName);
            if (!file.exists())
            {
                if(!file.createNewFile())
                {
                    Log.e("NavsdkFileUtil", "errorMsg = Cannot create file " + file.getPath());
                    return;
                }
            }
            stream = new FileOutputStream(file);
            if (input != null)
            {
                byte[] datas = new byte[input.available()];
                if (input.read(datas) > 0)
                {
                    stream.write(datas);
                }
                stream.flush();
                stream.close();
                input.close();
            }
        }
        catch (IOException e)
        {
            Log.e(context.getClass().getName(), "Can't copy " + fileName + "to cache dir" + subDir + "!");
        }
        finally
        {
            try
            {
                if (input != null)
                {
                    input.close();
                }
            }
            catch (IOException ex)
            {
                Log.e("NavsdkFileUtil", "errorMsg = " + ex.toString());
            }
            try
            {
                if(stream != null)
                {
                    stream.close();
                }
            }
            catch (IOException ex)
            {
                Log.e("NavsdkFileUtil", "errorMsg = " + ex.toString());
            }
        }
    }

    public static void copyAssetIcon()
    {
        InputStream input = null;
        FileOutputStream stream = null;
        try
        {
            input = context.getResources().getAssets().open("icon.png");
            stream = context.openFileOutput("icon.png", Context.MODE_WORLD_READABLE
                    | Context.MODE_WORLD_WRITEABLE);
            if (input != null)
            {
                byte[] datas = new byte[input.available()];
                if (input.read(datas) > 0)
                {
                    stream.write(datas);
                }
                input.close();
                stream.flush();
                stream.close();
            }
        }
        catch (IOException e)
        {
            Log.e(context.getClass().getName(), "Can't copy icon.png to the devices!");
        }
        finally
        {
            try
            {
                if (input != null)
                {
                    input.close();
                }
            }
            catch (IOException ex)
            {
                Log.e("NavsdkFileUtil", "errorMsg = " + ex.toString());
            }
            try
            {
                if(stream != null)
                {
                    stream.close();
                }
            }
            catch (IOException ex)
            {
                Log.e("NavsdkFileUtil", "errorMsg = " + ex.toString());
            }
        }
    }

    public static String getNavsdkAudioDirectory()
    {
        return context.getFilesDir().getAbsolutePath() + "/" + NAVSDK_AUDIO_DIRECTORY + "/audios/";
    }

    public static String getNavsdkAudioRuleDirectory()
    {
        return context.getFilesDir().getAbsolutePath() + "/" + NAVSDK_AUDIO_DIRECTORY + "/rules/";
    }

    public static String getNavsdkMapDownloadDirectory()
    {
        String mapDownloadPath = null;
        File file = context.getExternalFilesDir(null);
        if (file != null)
        {
            mapDownloadPath = file.getAbsolutePath();
        }
        else
        {
            Log.e("NavsdkFileUtil", "ExternalFilesDir not available");
        }

        return mapDownloadPath;
    }
    
    public static String getNavsdkResourcePath()
    {
        String path = null;
        try
        {
            path = context.getFilesDir().getAbsolutePath() + "/";
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
        return path;
    }
    
    public static void createFile(String dir, String name, byte[] data)
    {
        File d = new File(dir);
        if (!d.exists())
        {
            if(!d.mkdirs())
            {
                Log.e("NavsdkFileUtil", "errorMsg = Cannot make dir " + dir);
                return;
            }
        }
        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        BufferedOutputStream bfos = null;
        FileOutputStream fos = null;
        File file = new File(dir + name);
        try
        {
            if(!file.createNewFile())
            {
                Log.e("NavsdkFileUtil", "errorMsg = Cannot create file " + file.getPath());
                return;
            }
            fos = new FileOutputStream(file);
            bfos = new BufferedOutputStream(fos, BUFFER);
            byte buffer[] = new byte[BUFFER];
            int count;
            while ((count = bis.read(buffer, 0, BUFFER)) != -1)
            {
                bfos.write(buffer, 0, count);
            }
            bfos.flush();
            bfos.close();
            fos.flush();
            fos.close();
            bis.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if(bis != null)
                {
                    bis.close();
                }
            }
            catch (IOException ex)
            {
                Log.e("NavsdkFileUtil", "errorMsg = " + ex.toString());
            }
            try
            {
                if (bfos != null)
                {
                    bfos.close();
                }
            }
            catch (IOException ex)
            {
                Log.e("NavsdkFileUtil", "errorMsg = " + ex.toString());
            }
            try
            {
                if (fos != null)
                {
                    fos.close();
                }
            }
            catch (IOException ex)
            {
                Log.e("NavsdkFileUtil", "errorMsg = " + ex.toString());
            }
        }
    }
    
    public static byte[] readFile(String dir, String name)
    {
        byte[] data = null;
        
        File file = new File(dir + "/" + name);
        
        if(!file.exists())
        {
            Log.e("NavsdkFileUtil", "errorMsg = Cannot read file " + file.getPath());
            return null;
        }
        
        InputStream is = null;
        BufferedInputStream bis = null;
        ByteArrayOutputStream bos = null;
        try
        {
            is = new FileInputStream(file);
            bis = new BufferedInputStream(is, BUFFER);
            bos = new ByteArrayOutputStream(BUFFER);
            byte buffer[] = new byte[BUFFER];
            int count;;
            while((count = bis.read(buffer, 0, BUFFER)) != -1)
            {
                bos.write(buffer, 0, count);
            }
            
            bos.flush();
            data = bos.toByteArray();
        }
        catch(Exception e)
        {
            Log.e("NavsdkFileUtil", "errorMsg = " + e.toString());
        }
        finally
        {
            if(is != null)
            {
                try
                {
                    is.close();
                }
                catch (IOException e)
                {
                    Log.e("NavsdkFileUtil", "errorMsg = " + e.toString());
                }
            }
            if(bis != null)
            {
                try
                {
                    bis.close();
                }
                catch (IOException e)
                {
                    Log.e("NavsdkFileUtil", "errorMsg = " + e.toString());
                }
            }
            if(bos != null)
            {
                try
                {
                    bos.close();
                }
                catch (IOException e)
                {
                    Log.e("NavsdkFileUtil", "errorMsg = " + e.toString());
                }
            }
        }
        
        return data;
    }

    public static void copyDir(String dir)
    {
        try
        {
            String[] files = context.getAssets().list(dir);
            int count = files.length;
            for (int i = 0; i < count; i++)
            {
                if (isFile(files[i]))
                {
                    copyFile(dir + "/" + files[i]);
                }
                else
                {
                    createDirectory(dir + "/" + files[i]);
                    copyDir(dir + "/" + files[i]);
                }
            }
        }
        catch (IOException e)
        {
            Log.e(context.getClass().getName(), "Can't get directory:" + dir + " in the devices!");
        }
    }

    private static boolean isFile(String path)
    {
        if (path.contains("."))
            return true;
        else
            return false;
    }

    private static void createDirectory(String path)
    {
        File file = new File("/data/data/" + context.getPackageName() + "/" + path);
        if (!file.exists())
        {
            if(!file.mkdirs())
            {
                Log.e("NavsdkFileUtil", "errorMsg = Cannot make dir " + path);
            }
        }
    }

    private static void copyFile(String destFilePath)
    {
        try
        {
            InputStream input = context.getAssets().open(destFilePath);
            Log.d("NavSDK", "copy file to " + destFilePath);
            FileOutputStream output = new FileOutputStream("/data/data/" + context.getPackageName() + "/" + destFilePath);
            copy(input, output);
        }
        catch (IOException e)
        {
            Log.e(context.getClass().getName(), "Can't copy " + destFilePath + " to the devices!");
        }
    }

    private static void copy(InputStream input, OutputStream output)
    {
        try
        {
            if (input != null)
            {
                byte[] datas = new byte[input.available()];
                if (input.read(datas) > 0)
                {
                    output.write(datas);
                }
                output.close();
                input.close();
            }
            output.flush();
            output.close();
        }
        catch (IOException e)
        {
            Log.e("NavsdkFileUtil", "errorMsg = " + e.toString());
        }
        finally
        {
            try
            {
                if (input != null)
                {
                    input.close();
                }
            }
            catch (IOException ex)
            {
                Log.e("NavsdkFileUtil", "errorMsg = " + ex.toString());
            }
            try
            {
                if(output != null)
                {
                    output.close();
                }
            }
            catch (IOException ex)
            {
                Log.e("NavsdkFileUtil", "errorMsg = " + ex.toString());
            }
        }
    }

    public static void copyAssetFile2Resource(String fileName)
    {
        copyAssetFile2Resource(fileName, false);
    }

    public static void copyAssetFile2Resource(String fileName, boolean forceCopy)
    {
        copyAssetFile2Resource(fileName, fileName, forceCopy);
    }

    public static void copyAssetFile2Resource(String srcFileName, String destFileName, boolean forceCopy)
    {
        File resDir = new File(context.getFilesDir().getPath());
        if (resDir.exists())
        {
            String[] fileNames = context.getFilesDir().list();

            if (fileNames != null)
            {
                boolean isFileExisted = false;
                for (int i = 0; i < fileNames.length; i++)
                {
                    if (srcFileName.equals(fileNames[i]))
                    {
                        isFileExisted = true;
                    }
                }

                if (!forceCopy && fileNames != null && isFileExisted) // DRAFT commented by hchai, because sometime
                // bad datas.
                {
                    return;
                }
            }
        }
        else
        {
            if (!resDir.mkdir())
            {
                Log.e("NavsdkFileUtil", "errorMsg = Cannot make dir " + resDir.getPath());
                return;
            }
        }
        InputStream input = null;
        OutputStream output = null;
        try
        {
            input = context.getAssets().open(srcFileName);
            output = new FileOutputStream(context.getFilesDir().getPath() + "/" + destFileName);
            if (input != null)
            {
                byte[] datas = new byte[input.available()];
                if (input.read(datas) > 0)
                {
                    output.write(datas);
                }
                input.close();
                output.flush();
                output.close();
            }
        }
        catch (IOException e)
        {
            Log.e("NavsdkFileUtil", "Can't copy " + srcFileName + " to files/" + destFileName + " !");
            Log.e("NavsdkFileUtil", "errorMsg = " + e.toString());
        }
        finally
        {
            try
            {
                if (input != null)
                {
                    input.close();
                }
            }
            catch (IOException e)
            {
                Log.e("NavsdkFileUtil", "errorMsg = " + e.toString());
            }
            try
            {
                if (output != null)
                {
                    output.close();
                }
            }
            catch (IOException e)
            {
                Log.e("NavsdkFileUtil", "errorMsg = " + e.toString());
            }
        }
    }

    private static void copyAssets(String assetDir, String dir)
    {
        String[] files;
        try
        {
            files = context.getResources().getAssets().list(assetDir);
        }
        catch (IOException e1)
        {
            return;
        }
        File mWorkingPath = new File(dir);
        // if this directory does not exists, make one.
        if (!mWorkingPath.exists())
        {
            if (!mWorkingPath.mkdirs())
            {
                Log.e("--CopyAssets--", "cannot create directory.");
            }
        }
        for (int i = 0; i < files.length; i++)
        {
            InputStream in = null;
            OutputStream out = null;
            try
            {
                String fileName = files[i];
                if (!fileName.contains("."))
                {
                    if (0 == assetDir.length())
                    {
                        copyAssets(fileName, dir + "/" + fileName + "/");
                    }
                    else
                    {
                        copyAssets(assetDir + "/" + fileName, dir + "/" + assetDir + "/" + fileName + "/");
                    }
                    continue;
                }
                File outFile = new File(mWorkingPath, fileName);
                if (outFile.exists())
                {
                    if(!outFile.delete())
                    {
                        Log.e("NavsdkFileUtil", "errorMsg = Cannot delete file " + outFile.getPath());
                        return;
                    }
                }
                if (0 != assetDir.length())
                    in = context.getAssets().open(assetDir + "/" + fileName);
                else
                    in = context.getAssets().open(fileName);
                out = new FileOutputStream(outFile);

                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0)
                {
                    out.write(buf, 0, len);
                }

                in.close();
                out.flush();
                out.close();
            }
            catch (IOException e)
            {
                Log.e("NavsdkFileUtil", "errorMsg = " +e.toString());
            }
            finally
            {
                try
                {
                    if (in != null)
                    {
                        in.close();
                    }
                }
                catch (IOException ex)
                {
                    Log.e("NavsdkFileUtil", "errorMsg = " + ex.toString());
                }
                try
                {
                    if(out != null)
                    {
                        out.close();
                    }
                }
                catch (IOException ex)
                {
                    Log.e("NavsdkFileUtil", "errorMsg = " + ex.toString());
                }
            }
        }
    }

    public static void copyAssetFolder2Resource(String dir)
    {
        copyAssets(dir, context.getFilesDir().getPath());
    }

    public static void enableNavLog(boolean enable)
    {
        OutputStream stream = null;
        try
        {
            stream = new FileOutputStream(context.getCacheDir().getPath() + '/' + "log.bin");
            stream.write(enable ? "1".getBytes() : "0".getBytes());
            stream.flush();
            stream.close();
        }
        catch (IOException e)
        {
            Log.e(context.getClass().getName(), "Can't copy defaultUrl.bin to the devices!");
        }
        finally
        {
            try
            {
                if(stream != null)
                {
                    stream.close();
                }
            }
            catch (IOException ex)
            {
                Log.e("NavsdkFileUtil", "errorMsg = " + ex.toString());
            }
        }
    }

    public static boolean cleanCacheFile(String fileName)
    {
        File file = new File(context.getCacheDir().getPath() + '/' + fileName);
        return file.delete();
    }

    public static boolean copyCacheFileToSdcard(String fileName)
    {
        File sdcardDir = Environment.getExternalStorageDirectory();
        File parentDir = new File(sdcardDir.getPath() + "/" + COPY_TO_SDCARD_DIR);
        if (!parentDir.mkdir())
        {
            Log.e("NavsdkFileUtil", "errorMsg = Cannot make dir " + parentDir.getPath());
            return false;
        }
        copyFile(context.getCacheDir().getPath() + '/' + fileName, parentDir.getPath() + '/' + fileName);
        return true;
    }

    public static boolean copyCacheDirToSdcard(String dirName)
    {
        File sdcardDir = Environment.getExternalStorageDirectory();
        File parentDir = new File(sdcardDir.getPath() + "/" + dirName);
        if (!parentDir.mkdir())
        {
            Log.e("NavsdkFileUtil", "errorMsg = Cannot make dir " + parentDir.getPath());
            return false;
        }

        String[] fileNames = context.getCacheDir().list();

        if (fileNames != null && fileNames.length > 0)
        {
            for (int i = 0; i < fileNames.length; i++)
            {
                copyFile(context.getCacheDir().getPath() + '/' + fileNames[i], parentDir.getPath() + '/' + fileNames[i]);
            }
        }
        return true;
    }

    public static boolean copySdcardFileToCache(String fileName)
    {
        File sdcardDir = Environment.getExternalStorageDirectory();
        File cacheDir = new File(context.getCacheDir().getPath());
        if (!cacheDir.mkdir())
        {
            Log.e("NavsdkFileUtil", "errorMsg = Cannot make dir " + cacheDir.getPath());
            return false;
        }
        File parentDir = new File(sdcardDir.getPath() + "/" + COPY_FROM_SDCARD_DIR);
        copyFile(parentDir.getPath() + '/' + fileName, context.getCacheDir().getPath() + '/' + fileName);
        return true;
    }

    public static boolean copySdcardFileToResource(String fileName)
    {
        File sdcardDir = Environment.getExternalStorageDirectory();
        File resDir = new File(context.getFilesDir().getPath());
        if (!resDir.mkdir())
        {
            Log.e("NavsdkFileUtil", "errorMsg = Cannot make dir " + resDir.getPath());
            return false;
        }
        File parentDir = new File(sdcardDir.getPath() + "/" + COPY_FROM_SDCARD_DIR);
        copyFile(parentDir.getPath() + '/' + fileName, context.getFilesDir().getPath() + '/' + fileName);
        return true;
    }

    public static boolean copySdcardDirToCache(String dirName)
    {
        File sdcardDir = Environment.getExternalStorageDirectory();
        File cacheDir = new File(context.getCacheDir().getPath());
        if (!cacheDir.mkdir())
        {
            Log.e("NavsdkFileUtil", "errorMsg = Cannot make dir " + cacheDir.getPath());
            return false;
        }
        File parentDir = new File(sdcardDir.getPath() + "/" + dirName);
        String[] fileNames = parentDir.list();
        if (fileNames != null && fileNames.length > 0)
        {
            for (int i = 0; i < fileNames.length; i++)
            {
                copyFile(parentDir.getPath() + '/' + fileNames[i], context.getCacheDir().getPath() + '/' + fileNames[i]);
            }
        }
        return true;
    }

    public static boolean copySdcardDirToResource(String dirName)
    {
        File sdcardDir = Environment.getExternalStorageDirectory();
        File resDir = new File(context.getFilesDir().getPath());
        if (!resDir.mkdir())
        {
            Log.e("NavsdkFileUtil", "errorMsg = Cannot make dir " + resDir.getPath());
            return false;
        }
        File parentDir = new File(sdcardDir.getPath() + "/" + dirName);
        String[] fileNames = parentDir.list();
        File[] files = parentDir.listFiles();
        if (fileNames != null && fileNames.length > 0)
        {
            for (int i = 0; i < fileNames.length; i++)
            {
                if (!files[i].isDirectory())
                {
                    copyFile(parentDir.getPath() + '/' + fileNames[i], context.getFilesDir().getPath() + '/' + fileNames[i]);
                }
            }
        }
        return true;
    }

    private static boolean copyFile(String srcPath, String destPath)
    {
        InputStream input = null;
        OutputStream output = null;
        boolean success = false;
        try
        {
            input = new FileInputStream(srcPath);
            output = new FileOutputStream(destPath);
            if (input != null)
            {
                byte[] datas = new byte[input.available()];
                if (input.read(datas) > 0)
                {
                    output.write(datas);
                }
                input.close();
                output.flush();
                output.close();
            }
            success = true;
        }
        catch (IOException e)
        {
            Log.e("NavsdkFileUtil", "Can't copy " + srcPath + " to " + destPath + " !");
        }
        finally
        {
            try
            {
                if (input != null)
                {
                    input.close();
                }
            }
            catch (IOException ex)
            {
                Log.e("NavsdkFileUtil", "errorMsg = " + ex.toString());
            }
            try
            {
                if(output != null)
                {
                    output.close();
                }
            }
            catch (IOException ex)
            {
                Log.e("NavsdkFileUtil", "errorMsg = " + ex.toString());
            }
        }
        return success;
    }

    static final int BUFFER = 1024;

    private static final String ALGORITHM = "PBEWithMD5AndDES";

    public static void zip(String zipFileName, File inputFile, String pwd)
    {
        ZipOutputStream out = null;
        try
        {
            out = new ZipOutputStream(new FileOutputStream(zipFileName));
            zip(out, inputFile, "", pwd);
            out.close();
        }
        catch (IOException e)
        {
            Log.e("NavsdkFileUtil", "errorMsg = " + e.toString());
        }
        finally
        {
            try
            {
                if (out != null)
                {
                    out.close();
                }
            }
            catch (IOException ex)
            {
                Log.e("NavsdkFileUtil", "errorMsg = " + ex.toString());
            }
        }
    }

    public static void zip(ZipOutputStream outputStream, File file, String base, String pwd)
    {

        if (file.isDirectory())
        {
            File[] fl = file.listFiles();
            try
            {
                outputStream.putNextEntry(new ZipEntry(base + "/"));
                base = base.length() == 0 ? "" : base + "/";
                for (int i = 0; i < fl.length; i++)
                {
                    zip(outputStream, fl[i], base + fl[i].getName(), pwd);
                }
                outputStream.flush();
                outputStream.close();
            }
            catch (IOException e)
            {
                Log.e("NavsdkFileUtil", "errorMsg = " + e.toString());
            }
            finally
            {
                try
                {
                    if (outputStream != null)
                    {
                        outputStream.close();
                    }
                }
                catch (IOException ex)
                {
                    Log.e("NavsdkFileUtil", "errorMsg = " + ex.toString());
                }
            }  
        }
        else
        {
            FileInputStream inputStream = null;
            try
            {
                outputStream.putNextEntry(new ZipEntry(base));
                inputStream = new FileInputStream(file);
                // normal zip file
                if (pwd == null || pwd.trim().equals(""))
                {
                    int b;
                    while ((b = inputStream.read()) != -1)
                    {
                        outputStream.write(b);
                    }
                    inputStream.close();
                }
                // zip file
                else
                {
                    PBEKeySpec keySpec = new PBEKeySpec(pwd.toCharArray());
                    SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
                    SecretKey passwordKey = keyFactory.generateSecret(keySpec);
                    byte[] salt = new byte[8];
                    Random rnd = new Random();
                    rnd.nextBytes(salt);
                    int iterations = 100;
                    PBEParameterSpec parameterSpec = new PBEParameterSpec(salt, iterations);
                    Cipher cipher = Cipher.getInstance(ALGORITHM);
                    cipher.init(Cipher.ENCRYPT_MODE, passwordKey, parameterSpec);
                    outputStream.write(salt);
                    byte[] input = new byte[64];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(input)) != -1)
                    {
                        byte[] output = cipher.update(input, 0, bytesRead);
                        if (output != null)
                        {
                            outputStream.write(output);
                        }
                    }
                    byte[] output = cipher.doFinal();
                    if (output != null)
                    {
                        outputStream.write(output);
                    }
                    inputStream.close();
                    outputStream.flush();
                    outputStream.close();
                }
            }
            catch (IOException e)
            {
                Log.e("NavsdkFileUtil", "errorMsg = " + e.toString());
            }
            catch (Exception e)
            {
                Log.e("NavsdkFileUtil", "errorMsg = " + e.toString());
            }
            finally
            {
                try
                {
                    if (inputStream != null)
                    {
                        inputStream.close();
                    }
                }
                catch (IOException ex)
                {
                    Log.e("NavsdkFileUtil", "errorMsg = " + ex.toString());
                }
                try
                {
                    if (outputStream != null)
                    {
                        outputStream.close();
                    }
                }
                catch (IOException ex)
                {
                    Log.e("NavsdkFileUtil", "errorMsg = " + ex.toString());
                }
            }
        }
        if(!file.delete())
        {
            Log.e("NavsdkFileUtil", "errorMsg = Cannot delete file " + file.getPath());
        }
    }

    public static void unzip(String assertZipFileName, String outputDirectory)
    {
        try
        {
            ZipInputStream inputStream = new ZipInputStream(new BufferedInputStream(context.getAssets()
                    .open("navSDK_cache.zip")));
            unzip(inputStream, outputDirectory, null);
        }
        catch (IOException e)
        {
            Log.e(context.getClass().getName(), "Can't unzip " + assertZipFileName + " to the directory: " + outputDirectory);
        }
    }

    public static void unzip(ZipInputStream inputStream, String outputDirectory, String pwd)
    {
        ZipEntry zipEntry = null;
        FileOutputStream outputStream = null;
        BufferedOutputStream dest = null;
        try
        {
            while ((zipEntry = inputStream.getNextEntry()) != null)
            {
                byte data[] = new byte[BUFFER];
                int count;

                if (zipEntry.isDirectory())
                {
                    String name = zipEntry.getName();
                    name = name.substring(0, name.length() - 1);
                    File file = new File(outputDirectory + File.separator + name);
                    if(!file.mkdir())
                    {
                        Log.e("NavsdkFileUtil", "errorMsg = Cannot make dir " + file.getPath());
                        return;
                    }                
                }
                else
                {

                    File file = new File(outputDirectory + File.separator + zipEntry.getName());
                    if(!file.createNewFile())
                    {
                        Log.e("NavsdkFileUtil", "errorMsg = Cannot create file " + file.getPath());
                        return;
                    }
                    outputStream = new FileOutputStream(file);
                    // normal unzip file
                    if (pwd == null || pwd.trim().equals(""))
                    {
                        // int b;
                        // while ((b = inputStream.read()) != -1)
                        // {
                        // outputStream.write(b);
                        // }
                        // outputStream.close();
                        dest = new BufferedOutputStream(outputStream, BUFFER);
                        while ((count = inputStream.read(data, 0, BUFFER)) != -1)
                        {
                            dest.write(data, 0, count);
                        }
                        dest.flush();
                        dest.close();
                        outputStream.flush();
                        outputStream.close();
                    }
                    // unzip file
                    else
                    {
                        PBEKeySpec keySpec = new PBEKeySpec(pwd.toCharArray());
                        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
                        SecretKey passwordKey = keyFactory.generateSecret(keySpec);
                        byte[] salt = new byte[8];
                        if(inputStream.read(salt) <= 0)
                        {
                            return;
                        }
                        int iterations = 100;
                        PBEParameterSpec parameterSpec = new PBEParameterSpec(salt, iterations);
                        Cipher cipher = Cipher.getInstance(ALGORITHM);
                        cipher.init(Cipher.DECRYPT_MODE, passwordKey, parameterSpec);
                        byte[] input = new byte[64];
                        int bytesRead;
                        while ((bytesRead = inputStream.read(input)) != -1)
                        {
                            byte[] output = cipher.update(input, 0, bytesRead);
                            if (output != null)
                            {
                                outputStream.write(output);
                            }
                        }
                        byte[] output = cipher.doFinal();
                        if (output != null)
                        {
                            outputStream.write(output);
                        }
                        outputStream.flush();
                        outputStream.close();
                    }
                }
            }
            inputStream.close();
        }
        catch (IOException e)
        {
            Log.e("NavsdkFileUtil", "errorMsg = " + e.toString());
        }
        catch (Exception e)
        {
            Log.e("NavsdkFileUtil", "errorMsg = " + e.toString());
        }
        finally
        {
            try
            {
                if(inputStream != null)
                {
                    inputStream.close();
                }
            }
            catch (IOException e)
            {
                Log.e("NavsdkFileUtil", "errorMsg = " + e.toString());
            }
            try
            {
                if(dest != null)
                {
                    dest.close();
                }
            }
            catch (IOException e)
            {
                Log.e("NavsdkFileUtil", "errorMsg = " + e.toString());
            }
            try
            {
                if(outputStream != null)
                {
                    outputStream.close();
                }
            }
            catch (IOException e)
            {
                Log.e("NavsdkFileUtil", "errorMsg = " + e.toString());
            }
        }
    }

    public static boolean deleteFile(String fileName)
    {
        File file = new File(fileName);
        if (file.isFile() && file.exists())
        {
            return file.delete();
        }
        else
        {
            return false;
        }
    }

    public static boolean deleteDirectory(String dir)
    {
        if (!dir.endsWith(File.separator))
        {
            dir = dir + File.separator;
        }
        File dirFile = new File(dir);
        if (!dirFile.exists() || !dirFile.isDirectory())
        {
            return false;
        }
        boolean flag = true;
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++)
        {
            if (files[i].isFile())
            {
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag)
                {
                    break;
                }
            }
            else
            {
                flag = deleteDirectory(files[i].getAbsolutePath());
                if (!flag)
                {
                    break;
                }
            }
        }

        if (!flag)
        {
            return false;
        }

        return dirFile.delete();
    }
}
