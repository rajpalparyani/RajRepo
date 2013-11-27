/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * SoapRequester.java
 *
 */
package com.telenav.carconnect.provider.applink;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Hashtable;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.telenav.app.CommManager;
import com.telenav.comm.Comm;
import com.telenav.comm.Host;
import com.telenav.comm.ICommCallback;
import com.telenav.comm.ICommStreamHandler;
import com.telenav.data.serverproxy.ServerProxyConfig;
import com.telenav.logger.Logger;

/**
 *@author chihlh
 *@date Mar 29, 2012
 */
public class SoapSender 
{
    public static interface Receiver
    {
        /**
         * Callback set by requester to receive the result of SOAP request
         * @param doc - the result  of the SOAP request sent. the value is null if there is error.
         */
        public void receive(Document doc, long id);
    }

    private final Receiver receiver;
    private final Hashtable headers;
    
    public SoapSender(Receiver r)
    {
        receiver = r;
        headers = new Hashtable();
        //headers.put("Content-Type", "application/soap+xml; charset=utf-8");
        headers.put("Content-Type", "text/xml; charset=utf-8");
    }
    
    
    public void sendRequest(Host host, String request, long requestId)
    {
        Logger.log(Logger.INFO, this.getClass().getName(), "CarConnect: SoapSender.sendRequest host=" + host.host + ":" +host.port + ", request = " + request);
        Comm comm = CommManager.getInstance().getComm();
        CommProcessor callback = new CommProcessor(requestId);
        try
        {
            comm.sendData("Soap", host, request.getBytes("UTF-8"), callback, null, 
                ServerProxyConfig.defaultRetries, ServerProxyConfig.defaultTimeout, 
                callback, headers);
        }
        catch (UnsupportedEncodingException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    private class CommProcessor implements ICommCallback, ICommStreamHandler
    {
        
        public CommProcessor(long requestID)
        {
            this.requestID=requestID;
        }
    
        @Override
        public boolean send(OutputStream os, CommResponse response, ICommCallback callback)
                throws IOException
        {
            return true; // no additional stuff to send
        }
        
        private Document returnedDoc = null;
        private long requestID=0L;
        
        @Override
        public boolean receive(InputStream is, long length, CommResponse response,
                ICommCallback callback) throws IOException
        {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder;
            boolean success = false;
            try
            {
                docBuilder = docFactory.newDocumentBuilder();
                Document doc = docBuilder.parse(is);
                success = true;
                returnedDoc = doc;
            }
            catch (ParserConfigurationException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            catch (SAXException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            
            return success;
        }

        @Override
        public boolean isAllowNetworkRequest(CommResponse response)
        {
            return true;
        }

        @Override
        public void networkError(CommResponse response)
        {
            Logger.log(Logger.INFO, this.getClass().getName(), "CarConnect: SoapSender has networkError status = " + response.status + ", msg = " + response.errorMessage);
            receiver.receive(null,requestID);
        }

        @Override
        public void networkFinished(CommResponse response)
        {
            receiver.receive(returnedDoc,requestID);
        }

        @Override
        public void handleChild(CommResponse response)
        {
            // do nothing
        }

        @Override
        public void updateProgress(CommResponse response)
        {
            // do nothing
        }
    }

}
