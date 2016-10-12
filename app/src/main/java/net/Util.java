package net;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import java.util.ArrayList;

import Tool.LogUtil;
import Tool.ParseHttpResponse;
import Tool.SaveParseHttpResponseListener;

/**
 * Created by luweiling on 2016/9/30 0030.
 */
public class Util {

    public static void saveWeatherInformation(final Context context, String weatherResponss) {
        LogUtil.d("Util: 存储天气数据至本地");
        final SharedPreferences.Editor editor = context.getSharedPreferences("data",Context.MODE_PRIVATE).edit();
        ParseHttpResponse.parseResponse(weatherResponss, new SaveParseHttpResponseListener() {
            @Override
            public void onBasicComplete(ArrayList<String> list) {
                if (!list.isEmpty()) {
                    editor.putBoolean("city_selected",true);
                    editor.putString("city",list.get(0));
                    editor.putString("country",list.get(1));
                    editor.putString("updateTime",list.get(2));
                } else {
                    editor.putBoolean("city_selected",false);
                }
                editor.commit();
            }

            @Override
            public void onStatusComplete(ArrayList<String> list) {
                editor.putString("statusCode",list.get(0));
                editor.commit();
            }

            @Override
            public void onWeatherComplete(ArrayList<String> list) {
                editor.putString("weatherDesc",list.get(0));
                editor.putString("temp",list.get(1));
                editor.putString("feelTemp",list.get(2));
                editor.putString("shidu",list.get(3));
                editor.putString("pressure",list.get(4));
                editor.putString("rainValue",list.get(5));
                editor.putString("windDirection",list.get(6));
                editor.putString("windDegree",list.get(7));
                editor.putString("windSpeed",list.get(8));
                editor.commit();
            }

            @Override
            public void onError(Exception e) {
                LogUtil.d("Util; Util错误信息-->" + e.toString());
                e.printStackTrace();
            }
        });
    }
}
