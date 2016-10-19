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
    public int CurrentLevel = ProvinceSelectLevel;

    private CityListDatabase database;

    public String selectedID;
    private String selectedProvinceName;
    private String selectedCityName;
    private String selectedCountyName;

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
                        /*
                        Province selectProvince = new Province();
                        selectProvince.setName(datalist.get(position));
                        selectedProvinceName = datalist.get(position);
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
                        titleView.setText(selectedProvinceName);
                        */
                        selectedProvinceName = datalist.get(position);
                        Province selectProvince = new Province();
                        selectProvince.setName(selectedProvinceName);
                        LoadCityList(selectProvince);
                        break;
                    }
                    case CitySelectLevel:
                    {
                        CurrentLevel = CountySelectLevel;
                        /*
                        Province selectProvince = new Province();
                        selectProvince.setName(selectedProvinceName);
                        City selectCity = new City();
                        selectedCityName = datalist.get(position);
                        selectCity.setName(selectedCityName);
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
                        titleView.setText(selectedCityName);
                        */
                        selectedCityName = datalist.get(position);
                        Province selectProvince = new Province();
                        City selectCity = new City();
                        selectProvince.setName(selectedProvinceName);
                        selectCity.setName(selectedCityName);
                        LoadCountyList(selectProvince,selectCity);
                        break;
                    }
                    case CountySelectLevel:
                    {
                        CurrentLevel = ProvinceSelectLevel;
                        selectedCountyName = datalist.get(position);
                        selectedID = database.getID(selectedProvinceName, selectedCityName, selectedCountyName);

                        //存储选择数据
                        {
                            SharedPreferences.Editor editor = getSharedPreferences("select",MODE_PRIVATE).edit();
                            editor.putString("selectProvinceName",selectedProvinceName);
                            editor.putString("selectCityName",selectedCityName);
                            editor.putString("selectCountyName",selectedCountyName);
                            editor.putString("selectID",selectedID);
                            editor.commit();
                        }

                        //参数重置
                        {
                            selectedProvinceName = null;
                            selectedCityName = null;
                            selectedCountyName = null;
                            selectedID = null;
                        }

                        Intent intent = new Intent(Select_Activity.this,Weather_Activity.class);
                        startActivity(intent);
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
        List<Province> list = database.LoadProvince();
        for (Province province : list) {
            //LogUtil.d("Select_Activity: init datalist provinceName = " + province.getName());
            result.add(province.getName());
        }
        return result;
    }

    private void LoadProvinceList() {
        List<Province> provinceList = new ArrayList<>();
        provinceList = database.LoadProvince();
        List<String> provinceNameList = new ArrayList<>();
        for(Province province : provinceList) {
            if ( !(provinceNameList.contains(new String(province.getName())))) {
                provinceNameList.add(province.getName());
            }
        }
        datalist.clear();
        datalist.addAll(provinceNameList);
        adapter.notifyDataSetChanged();
        listView.setSelection(0);
        titleView.setText("中国");
    }

    private void LoadCityList(Province province) {
        List<City> cityList = new ArrayList<>();
        cityList = database.LoadCity(province);
        List<String> cityNameList = new ArrayList<>();
        for (City city : cityList) {
            if ( !(cityNameList.contains(city.getName()))) {
                cityNameList.add(city.getName());
            }
        }
        datalist.clear();
        datalist.addAll(cityNameList);
        adapter.notifyDataSetChanged();
        listView.setSelection(0);
        titleView.setText(province.getName());
    }

    private void LoadCountyList(Province province,City city) {
        List<County> countyList = new ArrayList<>();
        countyList = database.LoadCounty(province,city);
        List<String> countyNameList = new ArrayList<>();
        for (County county : countyList) {
            countyNameList.add(county.getCountyName());
        }
        datalist.clear();
        datalist.addAll(countyNameList);
        adapter.notifyDataSetChanged();
        listView.setSelection(0);
        titleView.setText(city.getName());
    }

    @Override
    public void onBackPressed() {
        switch (CurrentLevel) {
            case CountySelectLevel:
                CurrentLevel = CitySelectLevel;
                Province province = new Province();
                province.setName(selectedProvinceName);
                LoadCityList(province);
                break;
            case CitySelectLevel:
                CurrentLevel = ProvinceSelectLevel;
                LoadProvinceList();
                break;
            case ProvinceSelectLevel:
                finish();
                break;
            default:
                break;
        }
    }
}
