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
            JSONArray jsonArray = new JSONArray(jsonData);
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
            LogUtil.d("ParseHttpResponse: 存储天气数据");
            listener.onBasicComplete(Basic);
            listener.onStatusComplete(Status);
            listener.onWeatherComplete(WeatherNow);
        } catch (Exception e) {
            if (listener != null) {
                listener.onError(e);
            }
        }
    }
}