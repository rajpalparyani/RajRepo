/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * HttpProxy.java
 *
 */
package com.telenav.carconnect.provider.tnlink.module.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.ByteArrayBuffer;

import android.util.Log;

import com.telenav.navsdk.events.HttpProxyData.HttpProxyRequestType;
import com.telenav.navsdk.events.HttpProxyEvents.HttpProxyRequest;

/**
 *@author xiangli;flliu@telenav.cn
 *@date 2012-2-28
 */
public class HttpProxy
{
//    private static AssetManager manager;
    private static LinkedBlockingQueue<HttpProxyRequest> jobs=new LinkedBlockingQueue<HttpProxyRequest>();
    private static final String TAG="HttpProxy";
    private static final int WORKER_SIZE=5;
    private static Downloader[] workers=new Downloader[WORKER_SIZE];
//    private static HttpProxy instance=new HttpProxy();
    private static final int BUFFER_SIZE=2048;
    
    private HttpProxy()
    {
        
    }
    
    public static void appendRequest(HttpProxyRequest request)
    {
       // Log.i(TAG,"AppendRequest:"+request.toString());
        Log.i(TAG,"AppendRequest:"+request.getUrl());
        jobs.offer(request);
    }
    
    public static void cancelAllJobs()
    {
        for(int i=0;i<WORKER_SIZE;++i)
        {
            workers[i].cancel();
            workers[i].interrupt();
        }
    }
    
    private static class Downloader extends Thread{
        private boolean cancelled=false;
        private void handleHttpResponse(HttpProxyRequest request, HttpResponse httpResponse)
        {
            StatusLine status = httpResponse.getStatusLine();
            int id=request.getRequestId();
            // Get Headers string and format it to send back to client;
            Header[] headers=httpResponse.getAllHeaders();
            StringBuffer sb=new StringBuffer();
            for(int i=0;i<headers.length;++i)
            {
                sb.append(headers[i].toString());
                sb.append("\r\n");
            }
            sb.append("\r\n");
           
            // Get Input stream ,and send readBuffer to client;
             HttpEntity entity = httpResponse.getEntity();
             try {
                     InputStream inputStream = entity.getContent();
                     byte[] data=new byte[BUFFER_SIZE];
                     ByteArrayBuffer bab=new ByteArrayBuffer(BUFFER_SIZE);
                     int read=0;
                     while((read=inputStream.read(data))!=-1)
                     {
                         bab.append(data,0,read);
                     }
                     // broadcast HttpProxy Response back;
                     HttpController.sendResponse(id, sb.toString(), bab.toByteArray(), status.getStatusCode());
                 }
                 //Exception handling code;
                 catch (IllegalStateException e) {
                     Log.e(TAG,"IllegalStateException",e);
                 } catch (IOException e) {
                     Log.e(TAG,"IOException",e);
                 }catch(IllegalArgumentException e)
                 {
                     Log.e(TAG,"IllegalArgumentException",e);
                 }
            
        }
        private void processPost(HttpProxyRequest request)
        {
            try {
            URL url = new URL(request.getUrl()); 
            URLConnection conn=url.openConnection();
            conn.setDoOutput(true);
            // Send client posted data buf to remote server;
            OutputStream out=conn.getOutputStream();
            out.write(request.getPayload().toByteArray());  
            out.close();
            Map<String,List<String>> headers= conn.getHeaderFields() ;
            // Get Http Header list,and format it ,Then send back to client as header filed;
            Set<String> key = headers.keySet();
            StringBuffer sb=new StringBuffer();
            for (Iterator it = key.iterator(); it.hasNext();) {
                String headerName = (String) it.next();
                List<String> headerValues =headers.get(headerName);
                sb.append(headerName+":");
                Iterator<String> ti= headerValues.listIterator();
                while(ti.hasNext())
                {
                    sb.append(ti.next()+";");
                }
                
                sb.append("\r\n");
            }
            sb.append("\r\n");
            // Get Input stream ,and send readBuffer to client;
            InputStream inputStream=conn.getInputStream();
             ByteArrayBuffer bab=new ByteArrayBuffer(BUFFER_SIZE);
             byte[] data=new byte[BUFFER_SIZE];
             int read=0;
             while((read=inputStream.read(data))!=-1)
             {
                 bab.append(data,0,read);
             }
             
            HttpController.sendResponse(request.getRequestId(), sb.toString(), bab.toByteArray(), 200);

            } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        private void processGet(HttpProxyRequest request)
        {
            try {
                String url=request.getUrl();
                String newUrl = url;
                HttpClient client=new DefaultHttpClient();

                HttpResponse httpResponse ;
                HttpGet get=new HttpGet(newUrl);  
                httpResponse  = client.execute(get);
                handleHttpResponse(request,httpResponse);
                }
                 catch (IllegalStateException e) {
                     Log.e(TAG,"IllegalStateException",e);
                 } catch (IOException e) {
                     Log.e(TAG,"IOException",e);
                 }catch(IllegalArgumentException e)
                 {
                     Log.e(TAG,"IllegalArgumentException",e);
                 }
        }
        public void run()
        {
            while(!cancelled)
            {
                try {
                    HttpProxyRequest request=jobs.take();
                   
                    // if the Proxy request is type GET, Handle with processGet
                    if(request.getRequestType() == HttpProxyRequestType.HttpProxyRequestType_Get )
                    {
                        
                        processGet(request);
                    }
                    // if the Proxy request is type POST, Handle with processPost
                    else if(request.getRequestType() == HttpProxyRequestType.HttpProxyRequestType_Post)
                    {
                      processPost(request);  
                    }
                 
                   
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    Log.e(TAG,"InterruptedException",e);
                    break;
                } catch (IllegalStateException e) {
                    // TODO Auto-generated catch block
                    Log.e(TAG,"IllegalStateException",e);
                } catch(IllegalArgumentException e)
                {
                    Log.e(TAG,"IllegalArgumentException",e);
                }
            }
        }
        
             
        public void cancel()
        {
            cancelled=true;
        }
    }
    
    static{
        for(int i=0;i<WORKER_SIZE;i++)
        {
            workers[i]= new Downloader();
            workers[i].start();
        }
    }
    
    
}
