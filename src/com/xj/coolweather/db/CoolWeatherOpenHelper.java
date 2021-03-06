package com.xj.coolweather.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class CoolWeatherOpenHelper extends SQLiteOpenHelper {
    /**
     *������� 
    */
     private static final String CREATE_PROVINCE = "create table province ("
             + "id integer primary key autoincrement,"
             + "province_name text,"
             + "province_code text)";   
     private static final String CREATE_CITY = "create table city ("
             + "id integer primary key autoincrement,"
             + "city_name text,"
             + "city_code text,"
             + "province_id integer)";   
     private static final String CREATE_COUNTY = "create table county ("
             + "id integer primary key autoincrement,"
             + "county_name text,"
             + "county_code text,"
             + "city_id integer)";   
    public CoolWeatherOpenHelper(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate(SQLiteDatabase arg0) {
        // TODO Auto-generated method stub
        arg0.execSQL(CREATE_PROVINCE);
        arg0.execSQL(CREATE_CITY);
        arg0.execSQL(CREATE_COUNTY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
        // TODO Auto-generated method stub
        
    }

}
