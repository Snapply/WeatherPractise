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
                //WeatherNow.add(nowObject.getString("pres"));
                WeatherNow.add(nowObject.getString("pcpn"));
                WeatherNow.add(nowObject.getJSONObject("wind").getString("dir"));
                WeatherNow.add(nowObject.getJSONObject("wind").getString("sc"));
                WeatherNow.add(nowObject.getJSONObject("wind").getString("spd"));
                for (String temp : WeatherNow) {
                    LogUtil.d("ParseHttpResponse: WeatherNow数据-->" + temp);
                }
                listener.onWeatherComplete(WeatherNow);
            }

            /*
            for (int i=0;i < jsonArray.length();i++) {
                switch (jsonArray.getString(i)) {
                    case "basic" :
                    {
                        LogUtil.d("ParseHttpResponse: 解析Basic数据");
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String city = jsonObject.getString("city");
                        String country = jsonObject.getString("cnty");
                        String updatetime = jsonObject.getJSONObject("update").getString("loc");
                        Basic.add(city);
                        Basic.add(country);
                        Basic.add(updatetime);
                        break;
                    }
                    case "status" :
                    {
                        LogUtil.d("ParseHttpResponse: 解析status数据");
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String statusCode = jsonObject.getString("status");
                        Status.add(statusCode);
                        break;
                    }
                    case "now" :
                    {
                        LogUtil.d("ParseHttpResponse: 解析天气数据");
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String weatherDesc = jsonObject.getJSONObject("cond").getString("txt");
                        String temp = jsonObject.getString("tmp");
                        String feeltemp = jsonObject.getString("fl");
                        String shidu = jsonObject.getString("hum");
                        String presure = jsonObject.getString("pres");
                        String rainValue = jsonObject.getString("pcpn");
                        String windDirection = jsonObject.getJSONObject("wind").getString("dir");
                        String windDegree = jsonObject.getJSONObject("wind").getString("sc");
                        String windSpeed = jsonObject.getJSONObject("wind").getString("spd");
                        WeatherNow.add(weatherDesc);
                        WeatherNow.add(temp);
                        WeatherNow.add(feeltemp);
                        WeatherNow.add(shidu);
                        WeatherNow.add(presure);
                        WeatherNow.add(rainValue);
                        WeatherNow.add(windDirection);
                        WeatherNow.add(windDegree);
                        WeatherNow.add(windSpeed);
                        break;
                    }
                    default:
                        break;
                }
            }
            */
            /*
            LogUtil.d("ParseHttpResponse: 存储天气数据");
            listener.onBasicComplete(Basic);
            listener.onStatusComplete(Status);
            listener.onWeatherComplete(WeatherNow);
            */
        } catch (Exception e) {
            if (listener != null) {
                listener.onError(e);
            }
        }
    }
}