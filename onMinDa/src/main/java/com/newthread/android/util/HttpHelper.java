package com.newthread.android.util;

import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class HttpHelper {
    private static final String CHARSET = "UTF-8";
    private static final int DEFAULT_CONNECTION_TIMEOUT = (20 * 1000); // milliseconds
    private static final int DEFAULT_SOCKET_TIMEOUT = (30 * 1000); // milliseconds
    private static final String tag = "HttpHelper";
    
    // Get String
    public static String get(String url) { 
        HttpGet httpRequest = new HttpGet(url); 
        try { 
            HttpClient httpclient = new DefaultHttpClient(); 
            HttpResponse httpResponse = httpclient.execute(httpRequest); 
 
            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) { 
                String str = EntityUtils.toString(httpResponse.getEntity(), "UTF-8"); 
                return str; 
            } 
        } catch (Exception e) { 
            e.printStackTrace(); 
            Logger.i(tag, e.toString());
        } 
        return null; 
    } 
    
    // Ger InputStream 
    public static InputStream getStream(String url) { 
        HttpGet httpRequest = new HttpGet(url); 
        try { 
            HttpClient httpclient = new DefaultHttpClient(); 
            HttpResponse httpResponse = httpclient.execute(httpRequest); 
 
            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) { 
                String str = EntityUtils.toString(httpResponse.getEntity(), "UTF-8"); 
                return new ByteArrayInputStream(str.getBytes()); 
            } 
        } catch (Exception e) { 
            e.printStackTrace(); 
            Logger.i(tag, e.toString());
        } 
        return null; 
    }
    
    // Post
    public static String post(final String path, final Map<String, String> params) throws Exception {
        HttpURLConnection conn = null;
        DataOutputStream outStream = null;
        try {
//            URL url = new URL(Config.SERVER + path);
        	 URL url = null;
            byte[] data = parseParams(params).getBytes(CHARSET);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(DEFAULT_CONNECTION_TIMEOUT);
            conn.setReadTimeout(DEFAULT_SOCKET_TIMEOUT);
            conn.setUseCaches(false);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Charset", CHARSET);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length", String.valueOf(data.length));
            outStream = new DataOutputStream(conn.getOutputStream());
            outStream.write(data);
            outStream.flush();
            
            if (conn.getResponseCode() == 200) {
                InputStream in = conn.getInputStream();
                StringBuffer sb = new StringBuffer();
                InputStreamReader reader = new InputStreamReader(in, CHARSET);
                char[] buff = new char[1024];
                int len;
                while ((len = reader.read(buff)) > 0) {
                    sb.append(buff, 0, len);
                }
                return sb.toString();
            } else {
                throw new Exception("ResponseCode=" + conn.getResponseCode());
            }
            
        } catch (IOException ex) {
            ex.printStackTrace();
            
        } finally {
            if (null != outStream) {
                try {
                    outStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != conn) {
                conn.disconnect();
            }
        }
        
        return null;
    }
    
    private static String parseParams(Map<String, String> params) {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            builder.append(entry.getKey() + "=" + entry.getValue() + "&");
        }
        return builder.toString();
    }
}
