package com.xj.coolweather.activity;


import android.app.Activity;
import android.app.DownloadManager.Query;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xj.coolweather.util.HttpCallbackListener;
import com.xj.coolweather.util.HttpUtil;
import com.xj.coolweather.util.Utility;
import com.xujia.coolweather.R;


public class WeatherActivity extends Activity implements OnClickListener{
    private LinearLayout weatherInfoLayout;
    private TextView cityNameText;
    private  TextView publishText;
    private TextView weatherDespText;
    private TextView temp1Text;
    private TextView temp2Text;
    private TextView currentDateText;
    private Button switchCity;
    private Button refreshWeather;
    
    
@Override
protected void onCreate(Bundle savedInstanceState) {
    // TODO Auto-generated method stub
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.weather_layout);
    weatherInfoLayout =  (LinearLayout)findViewById(R.id.weather_info_layout);
    cityNameText = (TextView) findViewById(R.id.city_name);
    publishText =  (TextView) findViewById(R.id.publish_text);
    weatherDespText = (TextView) findViewById(R.id.weather_desp);
    temp1Text = (TextView) findViewById(R.id.temp1);
    temp2Text = (TextView) findViewById(R.id.temp2);
    currentDateText = (TextView) findViewById(R.id.current_date);
    switchCity = (Button) findViewById(R.id.switch_city);
    refreshWeather = (Button) findViewById(R.id.refresh_weather);
    String countyCode = getIntent().getStringExtra("county_code");
    if(countyCode != null)  {
        publishText.setText("同步中...");
        weatherInfoLayout.setVisibility(View.INVISIBLE);
        cityNameText.setVisibility(View.INVISIBLE);
        queryWeatherCode(countyCode);
    }else   {
        showWeather();
    }
    switchCity.setOnClickListener(this);
    refreshWeather.setOnClickListener(this);
    
}
private void queryWeatherCode(String countyCode)    {
    String address = "http://www.weather.com.cn/data/list3/city"+countyCode+".xml";
    queryFromServer(address,"countyCode");
}
private void queryWeatherInfo(String weatherCode)   {
    String address = "http://www.weather.com.cn/data/cityinfo/"+weatherCode+".xml";
    queryFromServer(address,"weatherCode");
}
private void queryFromServer(final String address,final String type)    {
    HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
        
        @Override
        public void onFinish(String response) {
            // TODO Auto-generated method stub
            if(type.equals("countyCode"))   {
                if(response != null)    {
                    String array[] =  response.split("\\|");
                    if(array != null && array.length == 2)   {
                       String weatherCode = array[1];
                       queryWeatherCode(weatherCode);
                    }
                }
            }else if(type.equals("weatherCode"))    {
                Utility.handleWeatherResponse(WeatherActivity.this, response);
                runOnUiThread(new Runnable() {
                    
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        showWeather();
                    }
                });
            }
        }
        
        @Override
        public void onError(Exception e) {
            // TODO Auto-generated method stub
            
        }
    });
}
private void showWeather()  {
    SharedPreferences prefs  = PreferenceManager.getDefaultSharedPreferences(this);
    cityNameText.setText(prefs.getString("city_name", ""));
    temp1Text.setText(prefs.getString("temp1", ""));
    temp2Text.setText(prefs.getString("temp2", ""));
    weatherDespText.setText(prefs.getString("weather_desp", ""));
    publishText.setText("今天"+prefs.getString("publish_time", "")+"发布");
    currentDateText.setText(prefs.getString("current_date", ""));
    weatherInfoLayout.setVisibility(View.VISIBLE);
    cityNameText.setVisibility(View.VISIBLE);
}
@Override
public void onClick(View arg0) {
    // TODO Auto-generated method stub
  if(arg0.getId() == R.id.switch_city)  {
      Intent intent = new Intent(this,ChooseAreaActivity.class);
      intent.putExtra("from_weather_activity", true);
      startActivity(intent);
      finish();
  }  else if(arg0.getId() == R.id.refresh_weather)  {
      publishText.setText("同步中");
      SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
      String weatherCode = prefs.getString("weather_code", "");
      if(weatherCode != null)   {
          queryWeatherCode(weatherCode);
      }
  }
}
}
