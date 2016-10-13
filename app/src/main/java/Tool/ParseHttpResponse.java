package Tool;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by luweiling on 2016/9/30 0030.
 */
public class ParseHttpResponse {

    public static void parseResponse(String jsonData,SaveParseHttpResponseListener listener) {
        LogUtil.d("ParseHttpResponse: 解析数据");
        ArrayList<String> Basic = new ArrayList<>();
        ArrayList<String> Status = new ArrayList<>();
        ArrayList<String> WeatherNow = new ArrayList<>();
        try {
            LogUtil.d("ParseHttpResponse: 开始解析json数据");
            JSONObject origin = new JSONObject(jsonData);
            JSONArray jsonArray = origin.getJSONArray("HeWeather data service 3.0");
            //LogUtil.d("ParseHttpResponse: json数组长度=" + jsonArray.length());
            //LogUtil.d("ParseHttpResponse: json内容==》" + jsonArray.toString());
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            JSONObject basicObject = jsonObject.getJSONObject("basic");
            JSONObject nowObject = jsonObject.getJSONObject("now");
            //JSONArray daili_forecast_array = jsonObject.getJSONArray("daily_forecast");

            //存储basic信息
            {
                LogUtil.d("ParseHttpResponse: 解析Basic数据");
                Basic.add(basicObject.getString("city"));
                Basic.add(basicObject.getString("cnty"));
                Basic.add(basicObject.getJSONObject("update").getString("loc"));
                for (String temp : Basic) {
                    LogUtil.d("ParseHttpResponse: Basic数据-->" + temp);
                }
                listener.onBasicComplete(Basic);
            }

            //存储Status信息
            {
                LogUtil.d("ParseHttpResponse: 解析status数据");
                Status.add(jsonObject.getString("status"));
                for (String temp : Status) {
                    LogUtil.d("ParseHttpResponse: Status数据-->" + temp);
                }
                listener.onStatusComplete(Status);
            }

            //存储now实时天气信息
            {
                LogUtil.d("ParseHttpResponse: 解析天气数据");
                WeatherNow.add(nowObject.getJSONObject("cond").getString("txt"));
                WeatherNow.add(nowObject.getString("tmp"));
                WeatherNow.add(nowObject.getString("fl"));
                WeatherNow.add(nowObject.getString("hum"));
                WeatherNow.add(nowObject.getString("pcpn"));
                WeatherNow.add(nowObject.getJSONObject("wind").getString("dir"));
                WeatherNow.add(nowObject.getJSONObject("wind").getString("sc"));
                WeatherNow.add(nowObject.getJSONObject("wind").getString("spd"));

                {
                    if (nowObject.has("pres")) {
                        WeatherNow.add(nowObject.getString("pres"));
                    } else {
                        WeatherNow.add("-");
                    }
                }

                //遍历数据打印
                {
                    for (String temp : WeatherNow) {
                        LogUtil.d("ParseHttpResponse: WeatherNow数据-->" + temp);
                    }
                }
                listener.onWeatherComplete(WeatherNow);
            }

        } catch (Exception e) {
            if (listener != null) {
                listener.onError(e);
            }
        }
    }
}