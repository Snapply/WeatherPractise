package db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import Tool.LogUtil;
import model.City;
import model.County;
import model.Province;

/**
 * Created by luweiling on 2016/9/29 0029.
 */
public class CityListDatabase {

    private static final String DB_name = "citylist";

    private static final int version = 1;

    private SQLiteDatabase database;

    public CityListDatabase(Context context) {
        CityDatabasesHelper cityDatabasesHelper = new CityDatabasesHelper(context,DB_name,null,version);
        database = cityDatabasesHelper.getReadableDatabase();
    }

    public List<Province> LoadProvince() {
        LogUtil.d("Database: 装载省级数据列表");
        List<Province> resullt = new ArrayList<>();
        Cursor cursor = database.query(DB_name,new String[]{"province"}, null,null,null,null,null);
        if (cursor.moveToFirst()) {
            do {
                String provinceName = cursor.getString(cursor.getColumnIndex("province"));
                Province province = new Province();
                province.setName(provinceName);
                resullt.add(province);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return resullt;
    }

    public ArrayList<City> LoadCity(Province province) {
        LogUtil.d("Database: 装载市级数据列表");
        ArrayList<City> result = new ArrayList<>();
        Cursor cursor = database.query(DB_name,new String[]{"city"}, "province = ?",new String[]{province.getName()},null,null,null);
        if (cursor.moveToFirst()) {
            do {
                String cityname = cursor.getString(cursor.getColumnIndex("city"));
                City city = new City();
                city.setName(cityname);
                result.add(city);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return result;
    }

    public ArrayList<County> LoadCounty(Province province,City city) {
        LogUtil.d("Database: 装载地区数据列表");
        ArrayList<County> result = new ArrayList<>();
        Cursor cursor = database.query(DB_name,new String[]{"ID","name","county"}, "province = ? and city = ?",new String[]{province.getName(),city.getName()},null,null,null );
        if (cursor.moveToFirst()) {
            do {
                String countyID = cursor.getString(cursor.getColumnIndex("ID"));
                String countyCode = cursor.getString(cursor.getColumnIndex("name"));
                String countyName = cursor.getString(cursor.getColumnIndex("county"));
                County county = new County();
                county.setCountyName(countyName);
                county.setCountyCode(countyCode);
                county.setCountyID(countyID);
                result.add(county);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return result;
    }

    public String getID(String ProvinceName,String CityName,String CountyName) {
        String result;
        Cursor cursor = database.query(DB_name,new String[]{"ID"},"province = ? and city = ? and county = ?",new String[]{ProvinceName,CityName,CountyName},null,null,null);
        cursor.moveToFirst();
        result = cursor.getString(cursor.getColumnIndex("ID"));
        return result;
    }
}
