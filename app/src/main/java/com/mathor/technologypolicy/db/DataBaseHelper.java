package com.mathor.technologypolicy.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * 数据库帮助类
 */
public class DataBaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "userInfo.db";
    private static final int VERSION = 1;

    private static final String CREATE_TABLE_STUDENTS = "CREATE TABLE IF NOT EXISTS users_tab(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "name TEXT,phone_num TEXT,icon_path TEXT,tuisong_tag TEXT)";

    private static final String DROP_TABLE_STUDENTS = "DROP TABLE IF EXISTS users_tab";

    public DataBaseHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_STUDENTS);
        Log.e("onCreate==", "DataBaseHelper--onCreate");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        sqLiteDatabase.execSQL(DROP_TABLE_STUDENTS);
        sqLiteDatabase.execSQL(CREATE_TABLE_STUDENTS);
        Log.e("onUpgrade==", "DataBaseHelper--onCreate");
    }
}
