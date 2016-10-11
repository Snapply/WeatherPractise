package net;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import Tool.LogUtil;

/**
 * Created by luweiling on 2016/9/30 0030.
 */
public class HttpConnection {
    public static void sendRequest(final String address, final CallBackListener listener) {
        LogUtil.d("HttpConnection: 建立链接");
        new Thread(new Runnable() {
            @Override
            public void run() {
                LogUtil.d("HttpConnection: 开启链接子线程");
                HttpURLConnection connection = null;
                try {
                    URL url = new URL(address);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    InputStream inputStream = connection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        response.append(line);
                    }
                    listener.onComplete(response.toString());
                } catch (Exception e) {
                    listener.onError(e);
                } finally {
                    if (connection != null)
                        connection.disconnect();
                }
            }
        }).start();
    }
}
