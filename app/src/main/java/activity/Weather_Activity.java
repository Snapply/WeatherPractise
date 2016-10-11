package activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.luweiling.weatherpractise.R;

import net.CallBackListener;
import net.HttpConnection;
import net.Util;

import java.util.ArrayList;

import Tool.GetContext;
import Tool.LogUtil;
import Tool.ParseHttpResponse;
import Tool.SaveParseHttpResponseListener;

/**
 * Created by luweiling on 2016/9/30 0030.
 */
public class Weather_Activity extends Activity {
    private TextView city = (TextView) findViewById(R.id.city);
    private TextView updateTime = (TextView) findViewById(R.id.updateTime);
    private TextView weatherDesc = (TextView) findViewById(R.id.weatherDesc);
    private TextView temp = (TextView) findViewById(R.id.temp);
    private TextView feelTemp = (TextView) findViewById(R.id.feelTemp);
    private TextView shidu = (TextView) findViewById(R.id.shidu);
    private TextView presure = (TextView) findViewById(R.id.presure);
    private TextView rainValue = (TextView) findViewById(R.id.rainValue);
    private TextView windDirection = (TextView) findViewById(R.id.windDirection);
    private TextView windDegree = (TextView) findViewById(R.id.windDegree);
    private TextView windSpeed = (TextView) findViewById(R.id.windSpeed);

    private Button fresh = (Button) findViewById(R.id.fresh);
    private Button select = (Button) findViewById(R.id.select);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.d("Weather_Activity: 天气界面");
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.weatherpage_layout);
        SharedPreferences sharedPreferences = getSharedPreferences("data",MODE_PRIVATE);
        if (sharedPreferences.getBoolean("city_selected",false)) {
            this.setView();
        }
        fresh();
        setView();
        fresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fresh();
                setView();
            }
        });
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Weather_Activity.this,Select_Activity.class);
                startActivity(intent);
                fresh();
            }
        });
    }

    private void fresh() {
        LogUtil.d("Weather_Activity: 天气数据刷新");
        SharedPreferences sharedPreferences = getSharedPreferences("selected",MODE_PRIVATE);
        String URL = "https://api.heweather.com/x3/weather?";
        String weatherID = "cityid=" + sharedPreferences.getString("city_selected_id",null);
        String key = "&key=4da31d32471e493fb672119675da8b92";
        String address = URL + weatherID + key;
        HttpConnection.sendRequest(address, new CallBackListener() {
            @Override
            public void onComplete(String Response) {
                Util.saveWeatherInformation(GetContext.getContext(),Response);
            }

            @Override
            public void onErroe(Exception e) {
                Toast.makeText(GetContext.getContext(), e.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setView() {
        LogUtil.d("Weather_Activity: 界面刷新");
        SharedPreferences sharedPreferences = getSharedPreferences("data",MODE_PRIVATE);
        city.setText(sharedPreferences.getString("city",null));
        updateTime.setText(sharedPreferences.getString("updatetime",null));
        weatherDesc.setText(sharedPreferences.getString("weatherDesc",null));
        temp.setText(sharedPreferences.getString("temp",null));
        feelTemp.setText(sharedPreferences.getString("feeltemp",null));
        shidu.setText(sharedPreferences.getString("shidu",null));
        presure.setText(sharedPreferences.getString("presure",null));
        rainValue.setText(sharedPreferences.getString("rainValue",null));
        windDirection.setText(sharedPreferences.getString("windDirection",null));
        windDegree.setText(sharedPreferences.getString("windDegree",null));
        windSpeed.setText(sharedPreferences.getString("windSpeed",null));
    }
}
