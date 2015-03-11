package com.xj.coolweather.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.xj.coolweather.model.City;
import com.xj.coolweather.model.County;
import com.xj.coolweather.model.Province;

import java.util.ArrayList;
import java.util.List;

public class CoolWeatherDB {
public static final String DB_NAME = "cool_weather";
public static final int version = 1;
private static CoolWeatherDB coolWeatherDB;
private SQLiteDatabase db;

private CoolWeatherDB(Context context)  {
    CoolWeatherOpenHelper dbHelper =  new CoolWeatherOpenHelper(context, DB_NAME, null, version);
    db =  dbHelper.getWritableDatabase();
}

public static synchronized   CoolWeatherDB getInstance(Context context)   {
    if(coolWeatherDB == null)   {
        coolWeatherDB = new CoolWeatherDB(context);
    }
    return coolWeatherDB;
}

/*
 * 存储province数据
 */
public void save(Province province) {
    if(province != null)    {
        ContentValues values = new ContentValues();
        values.put("province_name", province.getProvice_name());
        values.put("province_code", province.getProvince_code());
        db.insert("province", null, values);
    }
}
/*
 * 从数据路读取所有province信息
 */
public List<Province>  loadProvince()   {
    List<Province> list  = new ArrayList<Province>();
    Cursor cursor = db.query("province",null, null, null, null, null, null);
    if(cursor.moveToFirst())    {
        do  {
            Province province = new Province();
            province.setProvice_name(cursor.getString(cursor.getColumnIndex("province_name")));
            province.setProvince_code(cursor.getString(cursor.getColumnIndex("province_code")));
            list.add(province);
        }while(cursor.moveToNext());
    }
    return list;
}

/*
 * 存储city数据
 */
public void save(City city) {
    if(city != null)    {
        ContentValues values = new ContentValues();
        values.put("city_name", city.getCity_name());
        values.put("city_code", city.getCity_code());
        values.put("province_id", city.getProvince_id());
        db.insert("city", null, values);
    }
}
/*
 * 从数据库读取某个省份的所有city信息
 */
public List<City>  loadCity(int province_id)   {
    List<City> list  = new ArrayList<City>();
    Cursor cursor = db.query("city",null, "province_id=?", new String[]{String.valueOf(province_id)}, null, null, null);
    if(cursor.moveToFirst())    {
        do  {
            City city = new City();
            city.setCity_name(cursor.getString(cursor.getColumnIndex("city_name")));
            city.setCity_code(cursor.getString(cursor.getColumnIndex("city_code")));
            city.setProvince_id(province_id);
            list.add(city);
        }while(cursor.moveToNext());
    }
    return list;
}
/*
 * 存储county数据
 */
public void save(County county) {
    if(county != null)    {
        ContentValues values = new ContentValues();
        values.put("county_name", county.getCounty_name());
        values.put("county_code", county.getCounty_code());
        values.put("city_id", county.getCity_id());
        db.insert("county", null, values);
    }
}
/*
 * 从数据库读取某个city的所有county信息
 */
public List<County>  loadCounty(int city_id)   {
    List<County> list  = new ArrayList<County>();
    Cursor cursor = db.query("county",null, "city_id=?", new String[]{String.valueOf(city_id)}, null, null, null);
    if(cursor.moveToFirst())    {
        do  {
            County county = new County();
            county.setCounty_name(cursor.getString(cursor.getColumnIndex("county_name")));
            county.setCounty_code(cursor.getString(cursor.getColumnIndex("county_code")));
            county.setCity_id(city_id);
            list.add(county);
        }while(cursor.moveToNext());
    }
    return list;
}
}
