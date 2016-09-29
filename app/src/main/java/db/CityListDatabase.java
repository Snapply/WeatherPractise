package db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

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
        List<Province> resullt = new ArrayList<>();
        Cursor cursor = database.query(DB_name,null,null,null,null,null,null);
        if (cursor.moveToFirst()) {
            do {
                String provincename = cursor.getString(cursor.getColumnIndex("province"));
                Province province = new Province();
                province.setName(provincename);
                resullt.add(province);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return resullt;
    }

    public List<City> LoadCity(Province province) {
        List<City> result = new ArrayList<>();
        Cursor cursor = database.query(DB_name,null,"province = ?",new String[]{province.getName()},null,null,null);
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

    public List<County> LoadCounty(City city) {
        List<County> result = new ArrayList<>();
        Cursor cursor = database.query(DB_name,null,"city = ?",new String[]{city.getName()},null,null,null );
        if (cursor.moveToFirst()) {
            do {
                String countyName = cursor.getString(cursor.getColumnIndex("county"));
                String countyCode = cursor.getString(cursor.getColumnIndex("name"));
                String countyID = cursor.getString(cursor.getColumnIndex("ID"));
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
}
