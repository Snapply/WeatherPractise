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
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream inputStream = connection.getInputStream();
                    //LogUtil.d("HttpConnection: InputStream--->" + inputStream.toString());
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));
                    StringBuilder response = new StringBuilder();
                    String line = null;

                    while ((line = bufferedReader.readLine()) != null) {
                        //LogUtil.d("HttpConnection: Line===>" + line);
                        response.append(line);
                    }


                    /*
                    //验证JSON数据完整性
                    String[] temp = response.toString().split(",");
                    for (String str : temp) {
                        LogUtil.d("截取json，循环打印---->" + str);
                    }
                    */


                    //LogUtil.d("HttpConnection: 返回JSON数据===>" + response.toString());
                    if (listener != null) {
                        listener.onComplete(response.toString());
                    }
                    bufferedReader.close();
                    inputStream.close();
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
