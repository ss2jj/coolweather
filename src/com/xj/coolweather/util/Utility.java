package com.xj.coolweather.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.xj.coolweather.db.CoolWeatherDB;
import com.xj.coolweather.model.City;
import com.xj.coolweather.model.County;
import com.xj.coolweather.model.Province;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utility {
/*
 * 解析和返回省级数据
 */
    public synchronized static boolean handleProvincesResponce(CoolWeatherDB db,String response)    {
        if(response != null)    {
            String [] allProvinces = response.split(",");
            if(allProvinces != null && allProvinces.length>0)   {
            for(int i =0;i<allProvinces.length;i++)    {
                String [] array = allProvinces[i].split("\\|");
                Province province = new Province();
                province.setProvice_name(array[1]);
                province.setProvince_code(array[0]);
                db.save(province);
            }
            return true;
            }
        }
        return false;
    }
    
    
    /*
     * 解析和返回市级数据
     */
        public synchronized static boolean handleCtiiesResponce(CoolWeatherDB db,String response,int provinceId)    {
            if(response != null)    {
                String [] allCities = response.split(",");
                if(allCities != null && allCities.length>0)   {
                for(int i =0;i<allCities.length;i++)    {
                    String [] array = allCities[i].split("\\|");
                    City city = new City();
                    city.setCity_name(array[1]);
                    city.setCity_code(array[0]);
                    city.setProvince_id(provinceId);
                    db.save(city);
                }
                return true;
                }
            }
            return false;
        }
        
        /*
         * 解析和返回县级数据
         */
            public synchronized static boolean handleCountyResponce(CoolWeatherDB db,String response,int cityId)    {
                if(response != null)    {
                    String [] countys = response.split(",");
                    if(countys != null && countys.length>0)   {
                    for(int i =0;i<countys.length;i++)    {
                        String [] array = countys[i].split("\\|");
                        County county = new County();
                        county.setCounty_name(array[1]);
                        county.setCounty_code(array[0]);
                        county.setCity_id(cityId);
                        db.save(county);
                    }
                    return true;
                    }
                }
                return false;
            }
        
      /**
       * 解析json天气数据
       */
        public static void handleWeatherResponse(Context context,String response)   {
            JSONObject jsonObject;
            try {
                jsonObject = new JSONObject(response);
                JSONObject weatherInfo =  jsonObject.getJSONObject("weahterinfo");
                String cityName = weatherInfo.getString("city");
                String weatherCode = weatherInfo.getString("cityid");
                String temp1 = weatherInfo.getString("temp1");
                String temp2 = weatherInfo.getString("temp2");
                String weatherDesp = weatherInfo.getString("weather");
                String publishTime = weatherInfo.getString("ptime");
                saveWeatherInfo(context,cityName,weatherCode,temp1,temp2,weatherDesp,publishTime);
                
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
          
        }
        //将天气数据保存到本地
        public static void  saveWeatherInfo(Context context,String cityName,String weatherCode,String temp1,String temp2,String weatherDesp,String publishTime) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日",Locale.CHINA);
            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
           editor.putBoolean("city_selected", true);
            editor.putString("city_name", cityName);
            editor.putString("weather_code", weatherCode);
            editor.putString("temp1", temp1);
            editor.putString("temp2", temp2);
            editor.putString("weather_desp", weatherDesp);
            editor.putString("publish_time", publishTime);
            editor.putString("current_date", sdf.format(new Date()));
            editor.commit();

        }

}
