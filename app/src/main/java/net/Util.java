package net;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import java.util.ArrayList;

import Tool.ParseHttpResponse;
import Tool.SaveParseHttpResponseListener;

/**
 * Created by luweiling on 2016/9/30 0030.
 */
public class Util {

    public void saveWeatherInformation(final Context context, String weatherResponss) {
        final SharedPreferences.Editor editor = context.getSharedPreferences("data",Context.MODE_PRIVATE).edit();
        new ParseHttpResponse().parseResponse(weatherResponss, new SaveParseHttpResponseListener() {
            @Override
            public void onBasicComplete(ArrayList<String> list) {
                editor.putString("city",list.get(0));
                editor.putString("country",list.get(1));
                editor.putString("updateTime",list.get(2));
            }

            @Override
            public void onStatusComplete(ArrayList<String> list) {
                editor.putString("statusCode",list.get(0));
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
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        editor.commit();
    }
}