package com.xj.coolweather.activity;


import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.xj.coolweather.db.CoolWeatherDB;
import com.xj.coolweather.model.City;
import com.xj.coolweather.model.County;
import com.xj.coolweather.model.Province;
import com.xj.coolweather.util.HttpCallbackListener;
import com.xj.coolweather.util.HttpUtil;
import com.xj.coolweather.util.Utility;
import com.xujia.coolweather.R;

import java.util.ArrayList;
import java.util.List;

public class ChooseAreaActivity extends Activity {
public static final int LEVEL_PROVINCE = 0;
public static final int LEVEL_CITY = 1;
public static final int LEVEL_COUNTY = 2;
private ProgressDialog progressDialog;
private TextView titleText;
private ListView listView;
private ArrayAdapter<String> adapter;
private CoolWeatherDB coolWeatherDB;
private List<String> dataList = new ArrayList<String>();
private List<Province> provinceList;
private List<City> cityList;
private List<County> countyList;
private Province selectedProvince;
private  City selectedCity;
private int currentLevel;

    @Override
        protected void onCreate(Bundle savedInstanceState) {
            // TODO Auto-generated method stub
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.choose_area);
            listView = (ListView)findViewById(R.id.list_view);
            titleText = (TextView)findViewById(R.id.title_text);
            coolWeatherDB = CoolWeatherDB.getInstance(this);
            adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,dataList);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    // TODO Auto-generated method stub
                    if(currentLevel == LEVEL_PROVINCE)  {
                        selectedProvince =  provinceList.get(arg2);
                        queryCities();
                    }else if(currentLevel == LEVEL_CITY)    {
                        selectedCity = cityList.get(arg2);
                        queryCounties();
                    }
                }
            });
            queryProvinces();
        }
        
    private void queryProvinces()   {
        provinceList =  coolWeatherDB.loadProvince();
        if(provinceList.size() > 0) {
            dataList.clear();
            for(Province province :provinceList)    {
                dataList.add(province.getProvice_name());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            titleText.setText("中国");
            currentLevel = LEVEL_PROVINCE;
        }else {
            queryFromServer(null,"province");
        }
    }
    
    private void queryCities()  {
        cityList =  coolWeatherDB.loadCity(selectedProvince.getId());
        if(cityList.size() > 0) {
            dataList.clear();
            for(City city :cityList)    {
                dataList.add(city.getCity_name());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            titleText.setText(selectedProvince.getProvice_name());
            currentLevel = LEVEL_CITY;
        }else {
            queryFromServer(selectedProvince.getProvince_code(),"city");
        }
    }
    
    private void queryCounties()    {
        countyList =  coolWeatherDB.loadCounty(selectedCity.getId());
        if(countyList.size() > 0) {
            dataList.clear();
            for(County county :countyList)    {
                dataList.add(county.getCounty_name());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            titleText.setText(selectedCity.getCity_name());
            currentLevel = LEVEL_COUNTY;
        }else {
            queryFromServer(selectedCity.getCity_code(),"county");
        }
    }
    
    private void queryFromServer(final String code,final String type)   {
        String address;
        if(code != null)    {
            address = "http://www.weather.com.cn/data/list3/city"+code+".xml";
        }else {
            address = "http://www.weather.com.cn/data/list3/city.xml";
        }
        showProgressDialog();
        HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
         
            @Override
            public void onFinish(String response) {
                // TODO Auto-generated method stub
                boolean result = false;
                if(type.equals("province")) {
                    result =  Utility.handleProvincesResponce(coolWeatherDB, response);
                }else if(type.equals("city"))   {
                    result = Utility.handleCtiiesResponce(coolWeatherDB, response, selectedProvince.getId());
                }else if(type.equals("county")) {
                    result = Utility.handleCountyResponce(coolWeatherDB, response, selectedCity.getId());
                }
                
                if(result)  {
                    runOnUiThread(new Runnable() {
                        
                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                          closeProgressDialog();
                          if("province".equals(type))   {
                              queryProvinces();
                          }else if(type.equals("city")) {
                              queryCities();
                          }else if(type.equals("county"))   {
                              queryCounties();
                          }
                        }
                    });
                }
            }
            
            @Override
            public void onError(Exception e) {
                // TODO Auto-generated method stub
                runOnUiThread(new Runnable() {
                    
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        closeProgressDialog();
                    }
                });
            }
        });
    }
    
    /**
     * 显示进度框
     */
    private void showProgressDialog()   {
        if(progressDialog == null)  {
            progressDialog =  new ProgressDialog(this);
            progressDialog.setMessage("正在加载");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }
    private void closeProgressDialog()  {
        if(progressDialog != null)  {
            progressDialog.dismiss();
        }
    }
    
    @Override
        public void onBackPressed() {
            // TODO Auto-generated method stub
           if(currentLevel ==LEVEL_COUNTY)    {
               queryCities();
           }else if(currentLevel == LEVEL_CITY) {
               queryProvinces();
           }else    {
               finish();
           }
        }
}
