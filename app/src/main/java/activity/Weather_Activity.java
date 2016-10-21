package activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.example.luweiling.weatherpractise.R;

import net.CallBackListener;
import net.HttpConnection;
import net.Util;

import Tool.GetContext;
import Tool.LogUtil;

/**
 * Created by luweiling on 2016/9/30 0030.
 */
public class Weather_Activity extends Activity {
    private TextView city;
    private TextView updateTime;
    private TextView weatherDesc;
    private TextView temp;
    private TextView feelTemp;
    private TextView shidu;
    private TextView pressure;
    private TextView rainValue;
    private TextView windDirection;
    private TextView windDegree;
    private TextView windSpeed;

    private Button fresh;
    private Button select;

    @Override
    protected void onResume() {
        LogUtil.d("Weather_Activity: onResume");
        super.onResume();
        new Thread(new Runnable() {
            @Override
            public void run() {
                {
                    saveData();
                }
            }
        }).start();
        freshView();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.d("Weather_Activity: 天气界面 onCreate");
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.weatherpage_layout);
        city = (TextView) findViewById(R.id.city);
        updateTime = (TextView) findViewById(R.id.updateTime);
        weatherDesc = (TextView) findViewById(R.id.weatherDesc);
        temp = (TextView) findViewById(R.id.temp);
        feelTemp = (TextView) findViewById(R.id.feelTemp);
        shidu = (TextView) findViewById(R.id.shidu);
        pressure = (TextView) findViewById(R.id.presure);
        rainValue = (TextView) findViewById(R.id.rainValue);
        windDirection = (TextView) findViewById(R.id.windDirection);
        windDegree = (TextView) findViewById(R.id.windDegree);
        windSpeed = (TextView) findViewById(R.id.windSpeed);

        fresh = (Button) findViewById(R.id.fresh);
        select = (Button) findViewById(R.id.select);


        fresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        saveData();
                    }
                }).start();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        freshView();
                    }
                });
            }
        });

        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = GetContext.getContext().getSharedPreferences("data",MODE_PRIVATE).edit();
                editor.putBoolean("city_selected",false);
                editor.commit();
                Intent intent = new Intent(Weather_Activity.this,Select_Activity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void saveData() {
        LogUtil.d("Weather_Activity: 天气数据刷新");
        SharedPreferences sharedPreferences = getSharedPreferences("select",MODE_PRIVATE);
        String URL = "https://api.heweather.com/x3/weather?";
        String weatherID = "cityid=" + sharedPreferences.getString("selectID",null);
        String key = "&key=4da31d32471e493fb672119675da8b92";
        String address = URL + weatherID + key;
        HttpConnection.sendRequest(address, new CallBackListener() {
            @Override
            public void onComplete(String Response) {
                Util.saveWeatherInformation(GetContext.getContext(),Response);
            }

            @Override
            public void onError(Exception e) {
                LogUtil.d("Weather_Activity: 主界面错误-->" + e.toString());
                e.printStackTrace();
            }
        });
    }

    private void freshView() {
        LogUtil.d("Weather_Activity: 界面刷新");
        SharedPreferences sharedPreferences = getSharedPreferences("data",MODE_PRIVATE);
        city.setText(sharedPreferences.getString("city",null));
        updateTime.setText(sharedPreferences.getString("updateTime",null));
        weatherDesc.setText(sharedPreferences.getString("weatherDesc",null));
        temp.setText(sharedPreferences.getString("temp",null) + "℃");
        feelTemp.setText(sharedPreferences.getString("feelTemp",null) + "℃");
        shidu.setText(sharedPreferences.getString("shidu",null) + "%");
        pressure.setText(sharedPreferences.getString("pressure","*") + "hPa");
        rainValue.setText(sharedPreferences.getString("rainValue",null) + "mm");
        windDirection.setText(sharedPreferences.getString("windDirection",null));
        windDegree.setText(sharedPreferences.getString("windDegree",null) + "级");
        windSpeed.setText(sharedPreferences.getString("windSpeed",null) + "m/s");
    }
}
