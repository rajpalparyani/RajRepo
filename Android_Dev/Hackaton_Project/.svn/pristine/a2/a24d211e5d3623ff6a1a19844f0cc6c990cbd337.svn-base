/**
 *
 * Copyright 2013 TeleNav, Inc. All rights reserved.
 * RecordActivity.java
 *
 */
package com.telenav.app.android.scout_us;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.format.Time;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;

/**
 *@author bduan
 *@date 2013-3-3
 */
public class RecordActivity extends Activity
{
    public static final int REQUEST_RECORD_AUDIO = 1000;
    
    public static final int SUCCESSFUL = 1;
    
    public static final int FAIL = 0;
    
    public static final String RECORD_AUDIO_PATH = "AUDIO_PATH";
    
    
    protected static final int MAX_TIME = 20;

    protected static final int SAMPLE_RATE = 16000;

    protected ProgressBar progressBar_time = null;
    
    protected ProgressBar mProgressBar = null;

    protected int second = 0;

    protected AudioRecord mRecorder;

    protected File mRecording;

    protected short[] mBuffer;

    protected boolean mIsRecording = false;
    
    protected Timer timer = null;
    
    protected String path = null;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        getIntentData();
        
        setButtonCmd();
        
        progressBar_time = (ProgressBar)findViewById(R.id.progressBar_time);
        progressBar_time.setMax(MAX_TIME);
        progressBar_time.setProgress(0);
        setupTimer();
        
        mProgressBar = (ProgressBar)findViewById(R.id.progressBar_volume);
        
        initRecorder();
        
        startRecorder();
    }

    protected void getIntentData()
    {
        Intent intent = getIntent();
        Uri uri = intent.getData();
        if (uri != null)
        {
            path = uri.getPath();
        }
    }
    
    protected void setButtonCmd()
    {
        Button btn_ok = (Button)findViewById(R.id.btn_OK);
        Button btn_skip = (Button)findViewById(R.id.btn_SKIP);    
        
        btn_ok.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                stopRecorder(true);
            }
        });
        
        btn_skip.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                stopRecorder(false);
            }
        });
    }

    protected void startRecorder()
    {
        mIsRecording = true;
        mRecorder.startRecording();
        mRecording = getFile("raw");
        startBufferedWrite(mRecording);        
    }
    
    protected synchronized void stopRecorder(boolean isSaveFile)
    {
        Intent data = null;
        if (mIsRecording)
        {
            mIsRecording = false;
            mRecorder.stop();

            if (isSaveFile)
            {
                File waveFile = getFile("wav");
                try
                {
                    rawToWave(mRecording, waveFile);
                }
                catch (IOException e)
                {
                }
                
                data = new Intent();
                data.putExtra(RECORD_AUDIO_PATH, waveFile.getAbsolutePath());
            }
        }
        
        this.setResult(isSaveFile ? SUCCESSFUL : FAIL, data);
        RecordActivity.this.finish();
    }

    protected void initRecorder()
    {
        int bufferSize = AudioRecord.getMinBufferSize(SAMPLE_RATE,
            AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
        mBuffer = new short[bufferSize];
        mRecorder = new AudioRecord(MediaRecorder.AudioSource.MIC, SAMPLE_RATE,
                AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, bufferSize);
    }

    protected File getFile(final String suffix)
    {
        Time time = new Time();
        time.setToNow();
        return new File(getFolder(),
                time.format("%Y%m%d%H%M%S") + "." + suffix);
    }
    
    protected File getFolder()
    {
        File file = Environment.getExternalStorageDirectory();

        if(path == null)
        {
            path = file.getAbsolutePath() + "/feedback_log";
        }
        
        File logFoler = new File(path);

        if (!logFoler.exists())
        {
            try
            {
                logFoler.mkdir();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        
        return logFoler;
    }
    
    protected void startBufferedWrite(final File file)
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                DataOutputStream output = null;
                try
                {
                    output = new DataOutputStream(new BufferedOutputStream(
                            new FileOutputStream(file)));
                    while (mIsRecording)
                    {
                        double sum = 0;
                        int readSize = mRecorder.read(mBuffer, 0, mBuffer.length);
                        for (int i = 0; i < readSize; i++)
                        {
                            output.writeShort(mBuffer[i]);
                            sum += mBuffer[i] * mBuffer[i];
                        }
                        if (readSize > 0)
                        {
                            final double amplitude = sum / readSize;
                            mProgressBar.setProgress((int) Math.sqrt(amplitude));
                        }
                    }
                }
                catch (IOException e)
                {
                }
                finally
                {
                    mProgressBar.setProgress(0);
                    if (output != null)
                    {
                        try
                        {
                            output.flush();
                        }
                        catch (IOException e)
                        {
                        }
                        finally
                        {
                            try
                            {
                                output.close();
                            }
                            catch (IOException e)
                            {
                            }
                        }
                    }
                }
            }
        }).start();
    }

    protected void rawToWave(final File rawFile, final File waveFile) throws IOException
    {

        byte[] rawData = new byte[(int) rawFile.length()];
        DataInputStream input = null;
        try
        {
            input = new DataInputStream(new FileInputStream(rawFile));
            input.read(rawData);
        }
        finally
        {
            if (input != null)
            {
                input.close();
            }
        }

        DataOutputStream output = null;
        try
        {
            output = new DataOutputStream(new FileOutputStream(waveFile));
            // WAVE header
            // see http://ccrma.stanford.edu/courses/422/projects/WaveFormat/
            writeString(output, "RIFF"); // chunk id
            writeInt(output, 36 + rawData.length); // chunk size
            writeString(output, "WAVE"); // format
            writeString(output, "fmt "); // subchunk 1 id
            writeInt(output, 16); // subchunk 1 size
            writeShort(output, (short) 1); // audio format (1 = PCM)
            writeShort(output, (short) 1); // number of channels
            writeInt(output, SAMPLE_RATE); // sample rate
            writeInt(output, SAMPLE_RATE * 2); // byte rate
            writeShort(output, (short) 2); // block align
            writeShort(output, (short) 16); // bits per sample
            writeString(output, "data"); // subchunk 2 id
            writeInt(output, rawData.length); // subchunk 2 size
            // Audio data (conversion big endian -> little endian)
            short[] shorts = new short[rawData.length / 2];
            ByteBuffer.wrap(rawData).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer()
                    .get(shorts);
            ByteBuffer bytes = ByteBuffer.allocate(shorts.length * 2);
            for (short s : shorts)
            {
                bytes.putShort(s);
            }
            output.write(bytes.array());
        }
        finally
        {
            if (output != null)
            {
                output.close();
            }
        }
    }
    
    protected void writeInt(final DataOutputStream output, final int value)
            throws IOException
    {
        output.write(value >> 0);
        output.write(value >> 8);
        output.write(value >> 16);
        output.write(value >> 24);
    }

    protected void writeShort(final DataOutputStream output, final short value)
            throws IOException
    {
        output.write(value >> 0);
        output.write(value >> 8);
    }

    protected void writeString(final DataOutputStream output, final String value)
            throws IOException
    {
        for (int i = 0; i < value.length(); i++)
        {
            output.write(value.charAt(i));
        }
    }
    
    protected void setupTimer()
    {
        timer = new Timer("Update progress bar");
        timer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        progressBar_time.setProgress(++second);
                        
                        if (second == MAX_TIME)
                        {
                            stopRecorder(true);
                        }
                    }
                });
            }
        }, 1, 1000);        
    }
    
    @Override
    public void onDestroy()
    {
        if (timer != null)
        {
            timer.cancel();
        }
        
        if (mRecorder != null)
        {
            mRecorder.release();
        }
        
        super.onDestroy();
    }
}
