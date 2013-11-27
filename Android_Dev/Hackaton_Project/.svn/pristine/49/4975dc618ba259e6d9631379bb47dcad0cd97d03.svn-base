/**
 *
 * Copyright 2013 TeleNav, Inc. All rights reserved.
 * DwfNetwork.java
 *
 */
package com.telenav.dwf.request;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

/**
 * @author fangquanm
 * @date Jul 9, 2013
 */
public class DwfNetwork
{
    private static DwfNetwork instance = new DwfNetwork();

    private DwfNetwork()
    {
    }

    public static DwfNetwork getInstance()
    {
        return instance;
    }

    public JSONObject post(String url, HashMap<String, String> headers, HttpEntity entity) throws Throwable
    {
        DefaultHttpClient client = newHttpClient(url.startsWith("https://"));

        HttpPost post = new HttpPost(url);

        if (headers != null && !headers.isEmpty())
        {
            Iterator<Entry<String, String>> headerIter = headers.entrySet().iterator();
            while (headerIter.hasNext())
            {
                Entry<String, String> entry = headerIter.next();
                post.setHeader(entry.getKey(), entry.getValue());
            }
        }

        post.setEntity(entity);

        HttpResponse response = client.execute(post);

        int status = response.getStatusLine().getStatusCode();

        if (status == HttpStatus.SC_OK)
        {
            byte[] responseData = readBytes(response.getEntity().getContent());

            String s = new String(responseData, "utf-8");

            return new JSONObject(s);
        }

        return null;
    }

    public DefaultHttpClient newHttpClient(boolean isSSL)
    {
        DefaultHttpClient client = null;
        if (isSSL)
        {
            try
            {
                KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
                trustStore.load(null, null);

                SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
                sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

                HttpParams params = new BasicHttpParams();
                HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
                HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

                SchemeRegistry registry = new SchemeRegistry();
                registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
                registry.register(new Scheme("https", sf, 443));

                ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

                client = new DefaultHttpClient(ccm, params);
            }
            catch (Exception e)
            {
                e.printStackTrace();

                client = new DefaultHttpClient();
            }
        }
        else
        {
            client = new DefaultHttpClient();
        }

        HttpParams params = client.getParams();

        HttpConnectionParams.setConnectionTimeout(params, 60000);
        HttpConnectionParams.setSoTimeout(params, 60000);

        client.setHttpRequestRetryHandler(new DefaultHttpRequestRetryHandler(2, true));

        return client;
    }

    public static byte[] readBytes(InputStream is) throws IOException
    {
        ByteArrayOutputStream baos = null;

        try
        {
            baos = new ByteArrayOutputStream();

            byte[] buffer = new byte[512];

            int l = 0;
            while ((l = is.read(buffer)) > 0)
            {
                baos.write(buffer, 0, l);
            }

            return baos.toByteArray();
        }
        catch (IOException e)
        {
            throw e;
        }
        finally
        {
            try
            {
                if (baos != null)
                {
                    baos.close();
                    baos = null;
                }
            }
            catch (IOException e)
            {
                baos = null;
                throw e;
            }
        }
    }

    public class MySSLSocketFactory extends SSLSocketFactory
    {
        SSLContext sslContext = SSLContext.getInstance("TLS");

        public MySSLSocketFactory(KeyStore truststore) throws NoSuchAlgorithmException, KeyManagementException,
                KeyStoreException, UnrecoverableKeyException
        {
            super(truststore);

            TrustManager tm = new X509TrustManager()
            {
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException
                {
                }

                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException
                {
                }

                public X509Certificate[] getAcceptedIssuers()
                {
                    return null;
                }
            };

            sslContext.init(null, new TrustManager[]
            { tm }, null);
        }

        @Override
        public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException,
                UnknownHostException
        {
            return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
        }

        @Override
        public Socket createSocket() throws IOException
        {
            return sslContext.getSocketFactory().createSocket();
        }
    }
}
