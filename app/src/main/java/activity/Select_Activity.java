package activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import Tool.LogUtil;
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

    private static final int ProvinceSelectLevel = 1;
    private static final int CitySelectLevel = 2;
    private static final int CountySelectLevel = 3;
    private static final int IDSelectLevel = 4;
    public int CurrentLevel = ProvinceSelectLevel;

    private CityListDatabase database;

    public String selectedID;
    private String selectedProvince;
    private String selectedCity;
    private String selectedCounty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences = getSharedPreferences("data",MODE_PRIVATE);
        LogUtil.d("Select_Activity: 界面选择");
        if (preferences != null) {
            if (preferences.getBoolean("city_selected",false)) {
                LogUtil.d("Select_Activity：界面跳转");
                Intent intent = new Intent(this,Weather_Activity.class);
                startActivity(intent);
                finish();
            }
        }
        LogUtil.d("Select_Activity: 界面不跳转,继续执行");
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.selectcitylist_layout);
        titleView = (TextView) findViewById(R.id.title_view);
        listView = (ListView) findViewById(R.id.select_list);
        database = new CityListDatabase(this);
        datalist = init();
        adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,datalist);
        listView.setAdapter(adapter);
        titleView.setText("中国");
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (CurrentLevel) {
                    case ProvinceSelectLevel:
                    {
                        CurrentLevel = CitySelectLevel;
                        Province selectProvince = new Province();
                        selectProvince.setName(datalist.get(position));
                        selectedProvince = datalist.get(position);
                        ArrayList<City> cityArrayList = database.LoadCity(selectProvince);
                        ArrayList<String> temp = new ArrayList<String>();
                        ArrayList<String> list = new ArrayList<String>();
                        for (City city : cityArrayList) {
                            temp.add(city.getName());
                        }
                        for (String cityName : temp) {
                            if (!list.contains(cityName)) {
                                list.add(cityName);
                            }
                        }
                        datalist.clear();
                        datalist.addAll(list);
                        adapter.notifyDataSetChanged();
                        listView.setSelection(0);
                        titleView.setText(selectedProvince);
                        break;
                    }
                    case CitySelectLevel:
                    {
                        CurrentLevel = CountySelectLevel;
                        Province selectProvince = new Province();
                        selectProvince.setName(selectedProvince);
                        City selectCity = new City();
                        selectedCity = datalist.get(position);
                        selectCity.setName(selectedCity);
                        ArrayList<County> countyArrayList = database.LoadCounty(selectProvince,selectCity);
                        ArrayList<String> temp = new ArrayList<String>();
                        ArrayList<String> list = new ArrayList<String>();
                        for (County county : countyArrayList) {
                            temp.add(county.getCountyName());
                        }
                        for (String countyName : temp) {
                            if (!list.contains(countyName)) {
                                list.add(countyName);
                            }
                        }
                        datalist.clear();
                        datalist.addAll(list);
                        adapter.notifyDataSetChanged();
                        listView.setSelection(0);
                        titleView.setText(selectedCity);
                        break;
                    }
                    case CountySelectLevel:
                    {
                        CurrentLevel = ProvinceSelectLevel;
                        selectedCounty = datalist.get(position);
                        selectedID = database.getID(selectedProvince, selectedCity, selectedCounty);

                        //参数重置
                        {
                            selectedProvince = null;
                            selectedCity = null;
                            selectedCounty = null;
                        }

                        SharedPreferences.Editor editor = getSharedPreferences("selected",MODE_PRIVATE).edit();
                        editor.putString("city_selected_id",selectedID);
                        editor.commit();
                        Intent intent = new Intent(Select_Activity.this,Weather_Activity.class);
                        startActivityForResult(intent,100);
                        finish();
                        break;
                    }
                    default:
                        break;
                }
            }
        });
    }

    private ArrayList<String> init() {
        LogUtil.d("Select_Activity: datalist初始化，取数据库数据");
        ArrayList<String> result = new ArrayList<>();
        ArrayList<String> temp = new ArrayList<>();
        List<Province> list = database.LoadProvince();
        for (Province province : list) {
            //LogUtil.d("Select_Activity: init datalist provinceName = " + province.getName());
            temp.add(province.getName());
        }
        for (String provinceName : temp) {
            if (!result.contains(provinceName)) {
                result.add(provinceName);
            }
        }
        return result;
    }
}
