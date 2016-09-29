package db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by luweiling on 2016/9/29 0029.
 */
public class CityDatabasesHelper extends SQLiteOpenHelper {

    private static final String CREATE_LIST = "create table citylist (" +
            "ID text primary key," +
            "name text," +
            "county text," +
            "city text," +
            "province text)";


    public CityDatabasesHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_LIST);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
