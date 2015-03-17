package com.xj.coolweather.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;

import com.xj.coolweather.broadcast.AutoUpdateReceiver;
import com.xj.coolweather.util.HttpCallbackListener;
import com.xj.coolweather.util.HttpUtil;
import com.xj.coolweather.util.Utility;

public class AutoUpdateService extends Service{

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub
        new Thread(){
            public void run()   {
                updateWeather();
            }
        }.start();
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int anHour = 8*60*60*1000;
        long triggerAtTime = SystemClock.elapsedRealtime()+anHour;
        Intent i = new Intent(this,AutoUpdateReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
        manager. set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
        
        
        return super.onStartCommand(intent, flags, startId);
    }
    private void updateWeather()    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherCode = prefs.getString("weather_code", "");
        String address = "http://www.weather.com.cn/data/cityinfo/"+weatherCode +".htm";
        HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
            
            @Override
            public void onFinish(String response) {
                // TODO Auto-generated method stub
                Utility.handleWeatherResponse(AutoUpdateService.this, response);
            }
            
            @Override
            public void onError(Exception e) {
                // TODO Auto-generated method stub
                
            }
        });
    }
}
