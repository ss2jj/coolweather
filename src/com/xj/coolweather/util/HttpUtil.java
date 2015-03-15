package com.xj.coolweather.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpUtil {
    
 /*
  * ∑¢ÀÕhttp«Î«Û   
  */
public static void sendHttpRequest(final String address,final HttpCallbackListener listener)    {
    
    new Thread(new Runnable() {
        HttpURLConnection connection;
        @Override
        public void run() {
            // TODO Auto-generated method stub
            URL url;
            try {
                url = new URL(address);
                connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(8000);
                connection.setReadTimeout(8000);
                connection.setRequestMethod("GET");
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while((line=reader.readLine())!=null)   {
                    response.append(line);
                }
                if(listener != null)    {
                    listener.onFinish(response.toString());
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                if(listener != null)    {
                    listener.onError(e);
                }
            }finally    {
                connection.disconnect();
            }
           
        }
    }).start();
}
}
