package com.xj.coolweather.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.xj.coolweather.service.AutoUpdateService;

public class AutoUpdateReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context arg0, Intent arg1) {
        // TODO Auto-generated method stub
        Intent  i = new Intent(arg0,AutoUpdateService.class);
        arg0.startService(i);
    }

}
