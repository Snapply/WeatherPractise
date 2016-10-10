package activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.luweiling.weatherpractise.R;

import java.util.ArrayList;
import java.util.List;

import Tool.GetContext;
import db.CityListDatabase;
import model.City;
import model.County;
import model.Province;


/**
 * Created by luweiling on 2016/9/30 0030.
 */
public class Select_Activity extends Activity {

    private TextView titleView;

    private ListView listView;

    private ArrayAdapter<String> adapter;

    private ArrayList<String> datalist = new ArrayList<>();

    private static final int ProvinceLevel = 1;

    private static final int CityLevel = 2;

    private static final int CountyLevel = 3;

    private static final int SelectLevel = 4;

    public int CurrentLevel = ProvinceLevel;

    private CityListDatabase database;

    public String selectID;

    private String selectProvince;

    private String selectCity;

    private String selectCounty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences = getSharedPreferences("data",MODE_PRIVATE);
        if (preferences != null) {
            if (preferences.getBoolean("city_selected",false)) {
                Intent intent = new Intent(this,Weather_Activity.class);
                startActivity(intent);
                finish();
            }
        }
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.selectcitylist_layout);
        titleView = (TextView) findViewById(R.id.title_view);
        listView = (ListView) findViewById(R.id.select_list);
        database = new CityListDatabase(this);
        datalist = init();
        adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,datalist);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (CurrentLevel) {
                    case ProvinceLevel:
                    {
                        List<Province> list = database.LoadProvince();
                        ArrayList<String> data = new ArrayList<String>();
                        for (Province province : list) {
                            data.add(province.getName());
                        }
                        datalist.clear();
                        datalist.addAll(data);
                        datalist.notify();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                titleView.setText("中国");
                                adapter.notifyDataSetChanged();
                            }
                        });
                        CurrentLevel = CityLevel;
                        break;
                    }
                    case CityLevel:
                    {
                        List<City> list = new ArrayList<City>();
                        Province province = new Province();
                        selectProvince = datalist.get(position);
                        province.setName(selectProvince);
                        list = database.LoadCity(province);
                        ArrayList<String> data = new ArrayList<String>();
                        for (City city : list) {
                            data.add(city.getName());
                        }
                        datalist.clear();
                        datalist.addAll(data);
                        datalist.notify();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                titleView.setText(selectProvince);
                                adapter.notifyDataSetChanged();
                            }
                        });
                        CurrentLevel = CountyLevel;
                        break;
                    }
                    case CountyLevel:
                    {
                        List<County> list = new ArrayList<County>();
                        City city = new City();
                        selectCity = datalist.get(position);
                        city.setName(selectCity);
                        Province province = new Province();
                        province.setName(selectProvince);
                        list = database.LoadCounty(province,city);
                        ArrayList<String> data = new ArrayList<String>();
                        for (County county : list) {
                            data.add(county.getCountyName());
                        }
                        datalist.clear();
                        datalist.addAll(data);
                        datalist.notify();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                titleView.setText(selectCity);
                                adapter.notifyDataSetChanged();
                            }
                        });
                        CurrentLevel = SelectLevel;
                        break;
                    }
                    case SelectLevel:
                    {
                        selectCounty = datalist.get(position);
                        selectID = database.getID(selectProvince,selectCity,selectCounty);
                        SharedPreferences.Editor editor = (SharedPreferences.Editor) GetContext.getContext().getSharedPreferences("selected",MODE_PRIVATE);
                        editor.putString("city_selected_id",null);
                        editor.commit();
                        Intent intent = new Intent(Select_Activity.this,Weather_Activity.class);
                        startActivity(intent);
                        finish();
                        CurrentLevel = ProvinceLevel;
                        selectProvince = null;
                        selectCity = null;
                        selectCounty = null;
                        break;
                    }
                    default:
                        break;
                }
            }
        });
    }

    private ArrayList<String> init() {
        ArrayList<String> result = new ArrayList<>();
        List<Province> list = database.LoadProvince();
        for (Province province : list) {
            result.add(province.getName());
        }
        return result;
    }
}
