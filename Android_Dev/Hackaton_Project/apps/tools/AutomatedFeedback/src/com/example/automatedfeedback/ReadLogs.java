/**
 *
 * Copyright 2013 TeleNav, Inc. All rights reserved.
 * ReadLogs.java
 *
 */
package com.example.automatedfeedback;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.widget.TextView;

/**
 * @author bduan
 * @date 2013-3-3
 */
public class ReadLogs extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_logs);

        try
        {
            Process process = Runtime.getRuntime().exec("logcat -d");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
                    process.getInputStream()));

            File root = Environment.getExternalStorageDirectory();
            File logFoler = new File(root.getAbsolutePath() + "/test_zip");
            if(!logFoler.exists())
            {
                logFoler.mkdir();
            }
            
            File file = new File(logFoler, "logcat.txt");
            
            if(!file.exists())
            {
                file.createNewFile();
            }
            
            StringBuilder log = new StringBuilder();
            String line;
            
            FileWriter writer = new FileWriter(file);
            
            while ((line = bufferedReader.readLine()) != null)
            {
                log.append(line);
                writer.write(line + "\n");
            }
            TextView tv = (TextView) findViewById(R.id.textview_logs);
            tv.setText(log.toString());
            
            writer.close();
            
            File zipFile = new File(Environment.getExternalStorageDirectory(), "log.zip");
            
            ZipUtility.zipDirectory(logFoler, zipFile);
        }
        catch (Exception e)
        {
        }
    }
}
