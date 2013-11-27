package com.example.automatedfeedback;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity
{
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public boolean onMenuItemSelected(int featureId, MenuItem item)
    {
        boolean isHandle = super.onMenuItemSelected(featureId, item);
        
        if (item.getItemId() == R.id.action_report_issue)
        {
            Intent result = new Intent();
            
            File file = Environment.getExternalStorageDirectory();
            File logFoler = new File(file.getAbsolutePath() + "/feedback_log_1");
            Uri uri = Uri.fromFile(logFoler);
            
            result.setData(uri);
            result.setClassName(RecordActivity.class.getPackage().getName(), RecordActivity.class.getName());
            startActivityForResult(result, RecordActivity.REQUEST_RECORD_AUDIO);
            
            isHandle = true;
        }
        else if (item.getItemId() == R.id.action_read_logs)
        {
            Intent result = new Intent();
            
            result.setClassName(ReadLogs.class.getPackage().getName(), ReadLogs.class.getName());
            startActivity(result);
            
            isHandle = true;
        }
        else if (item.getItemId() == R.id.action_screen_shot)
        {
            getScreenShots();
            
            isHandle = true;
        }
        else if(item.getItemId() == R.id.action_send_mail)
        {
            File file = new File(Environment.getExternalStorageDirectory() + "/Vlog.xml");

            Intent intent = getSendEmailIntent(new File[]
            { file });

            try
            {
                startActivity(Intent.createChooser(intent, "Send mail..."));
            }
            catch (android.content.ActivityNotFoundException ex)
            {
                Toast.makeText(MainActivity.this,
                    "There are no email clients installed.", Toast.LENGTH_SHORT).show();
            }

            isHandle = true;
        }
        
        return isHandle;
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        System.out.println("DB-Test --> requestCode : " + requestCode + " , resultCode : " + resultCode);
        
        if (resultCode == RecordActivity.SUCCESSFUL)
        {
            Bundle bundle = data.getExtras();
            if(bundle.containsKey(RecordActivity.RECORD_AUDIO_PATH))
            {
                String path = (String)bundle.get(RecordActivity.RECORD_AUDIO_PATH);
                System.out.println("DB-Test --> path : " + path);
            }
        }
        
        super.onActivityResult(requestCode, resultCode, data);
    }
    
    public Intent getSendEmailIntent(File[] files)
    {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"recipient@example.com"});
        i.putExtra(Intent.EXTRA_SUBJECT, "issue_222");
        i.putExtra(Intent.EXTRA_TEXT   , "body of email");
        i.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(files[0]));
        
        return i;
//        GregorianCalendar date = new GregorianCalendar();
//        int day = date.get(Calendar.DAY_OF_MONTH);
//        int month = date.get(Calendar.MONTH) + 1;
//        int year = date.get(Calendar.YEAR);
//        int hour = date.get(Calendar.HOUR_OF_DAY);
//
//        // Adding trailing 0 for filename
//        String month_2 = ((month < 10) ? "0" : "") + (month);
//        String day_2 = ((day < 10) ? "0" : "") + (day);
//        String logfile = year + month_2 + day_2 + hour;
//
//        final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND_MULTIPLE);
//
//        // Use Gmail only to send
//        emailIntent.setClassName("com.google.android.gm",
//            "com.google.android.gm.ComposeActivityGmail");
//        emailIntent.setType("plain/text");
//
//        // Add the recipients
//        // emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[] { "namratav@telenav.com" });
//        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Bug Report["
//                + logfile + "]");
//        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Scout Testing");
//
//        ArrayList<Uri> uris = new ArrayList<Uri>();
//        // sending multiple files as attachments
//        for (File file : files)
//        {
//            Uri u = Uri.fromFile(file);
//            if (u != null)
//            {
//                uris.add(u);
//            }
//        }
//        emailIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
//
//        return emailIntent;
    }

    protected void getScreenShots()
    {
        String mPath = Environment.getExternalStorageDirectory().toString() + "/"
                + "s.png";

        // create bitmap screen capture
        Bitmap bitmap;
        View v1 = getWindow().getDecorView();
        v1.setDrawingCacheEnabled(true);
        bitmap = Bitmap.createBitmap(v1.getDrawingCache());
        v1.setDrawingCacheEnabled(false);

        OutputStream fout = null;
        File imageFile = new File(mPath);

        try
        {
            fout = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fout);
            fout.flush();
            fout.close();

        }
        catch (FileNotFoundException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
